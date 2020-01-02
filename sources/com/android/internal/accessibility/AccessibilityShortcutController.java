package com.android.internal.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.Voice;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;
import com.android.internal.R;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class AccessibilityShortcutController {
    public static final ComponentName COLOR_INVERSION_COMPONENT_NAME;
    public static final ComponentName DALTONIZER_COMPONENT_NAME;
    private static final String TAG = "AccessibilityShortcutController";
    private static final AudioAttributes VIBRATION_ATTRIBUTES = new Builder().setContentType(4).setUsage(11).build();
    private static Map<ComponentName, ToggleableFrameworkFeatureInfo> sFrameworkShortcutFeaturesMap;
    private AlertDialog mAlertDialog;
    private final Context mContext;
    private boolean mEnabledOnLockScreen;
    public FrameworkObjectProvider mFrameworkObjectProvider = new FrameworkObjectProvider();
    private final Handler mHandler;
    private boolean mIsShortcutEnabled;
    private int mUserId;

    public static class FrameworkObjectProvider {
        public AccessibilityManager getAccessibilityManagerInstance(Context context) {
            return AccessibilityManager.getInstance(context);
        }

        public AlertDialog.Builder getAlertDialogBuilder(Context context) {
            return new AlertDialog.Builder(context);
        }

        public Toast makeToastFromText(Context context, CharSequence charSequence, int duration) {
            return Toast.makeText(context, charSequence, duration);
        }

        public Context getSystemUiContext() {
            return ActivityThread.currentActivityThread().getSystemUiContext();
        }

        public TextToSpeech getTextToSpeech(Context ctx, OnInitListener listener) {
            return new TextToSpeech(ctx, listener);
        }

        public Ringtone getRingtone(Context ctx, Uri uri) {
            return RingtoneManager.getRingtone(ctx, uri);
        }
    }

    public static class ToggleableFrameworkFeatureInfo {
        private int mIconDrawableId;
        private final int mLabelStringResourceId;
        private final String mSettingKey;
        private final String mSettingOffValue;
        private final String mSettingOnValue;

        ToggleableFrameworkFeatureInfo(String settingKey, String settingOnValue, String settingOffValue, int labelStringResourceId) {
            this.mSettingKey = settingKey;
            this.mSettingOnValue = settingOnValue;
            this.mSettingOffValue = settingOffValue;
            this.mLabelStringResourceId = labelStringResourceId;
        }

        public String getSettingKey() {
            return this.mSettingKey;
        }

        public String getSettingOnValue() {
            return this.mSettingOnValue;
        }

        public String getSettingOffValue() {
            return this.mSettingOffValue;
        }

        public String getLabel(Context context) {
            return context.getString(this.mLabelStringResourceId);
        }
    }

    private class TtsPrompt implements OnInitListener {
        private boolean mDismiss;
        private final CharSequence mText;
        private TextToSpeech mTts;

        TtsPrompt(String serviceName) {
            this.mText = AccessibilityShortcutController.this.mContext.getString(R.string.accessibility_shortcut_spoken_feedback, serviceName);
            this.mTts = AccessibilityShortcutController.this.mFrameworkObjectProvider.getTextToSpeech(AccessibilityShortcutController.this.mContext, this);
        }

        public void dismiss() {
            this.mDismiss = true;
            AccessibilityShortcutController.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$qdzoyIBhDB17ZFWPp1Rf8ICv-R8.INSTANCE, this.mTts));
        }

        public void onInit(int status) {
            if (status != 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Tts init fail, status=");
                stringBuilder.append(Integer.toString(status));
                Slog.d(AccessibilityShortcutController.TAG, stringBuilder.toString());
                AccessibilityShortcutController.this.playNotificationTone();
                return;
            }
            AccessibilityShortcutController.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$AccessibilityShortcutController$TtsPrompt$HwizF4cvqRFiaqAcMrC7W8y6zYA.INSTANCE, this));
        }

        private void play() {
            if (!this.mDismiss) {
                int status = -1;
                if (setLanguage(Locale.getDefault())) {
                    status = this.mTts.speak(this.mText, 0, null, null);
                }
                if (status != 0) {
                    Slog.d(AccessibilityShortcutController.TAG, "Tts play fail");
                    AccessibilityShortcutController.this.playNotificationTone();
                }
            }
        }

        private boolean setLanguage(Locale locale) {
            int status = this.mTts.isLanguageAvailable(locale);
            if (status == -1 || status == -2) {
                return false;
            }
            this.mTts.setLanguage(locale);
            Voice voice = this.mTts.getVoice();
            if (voice == null || (voice.getFeatures() != null && voice.getFeatures().contains(Engine.KEY_FEATURE_NOT_INSTALLED))) {
                return false;
            }
            return true;
        }
    }

    static {
        String str = "com.android.server.accessibility";
        COLOR_INVERSION_COMPONENT_NAME = new ComponentName(str, "ColorInversion");
        DALTONIZER_COMPONENT_NAME = new ComponentName(str, "Daltonizer");
    }

    public static String getTargetServiceComponentNameString(Context context, int userId) {
        String currentShortcutServiceId = Secure.getStringForUser(context.getContentResolver(), Secure.ACCESSIBILITY_SHORTCUT_TARGET_SERVICE, userId);
        if (currentShortcutServiceId != null) {
            return currentShortcutServiceId;
        }
        return context.getString(R.string.config_defaultAccessibilityService);
    }

    public static Map<ComponentName, ToggleableFrameworkFeatureInfo> getFrameworkShortcutFeaturesMap() {
        if (sFrameworkShortcutFeaturesMap == null) {
            Map<ComponentName, ToggleableFrameworkFeatureInfo> featuresMap = new ArrayMap(2);
            String str = "0";
            String str2 = "1";
            featuresMap.put(COLOR_INVERSION_COMPONENT_NAME, new ToggleableFrameworkFeatureInfo(Secure.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED, str2, str, R.string.color_inversion_feature_name));
            featuresMap.put(DALTONIZER_COMPONENT_NAME, new ToggleableFrameworkFeatureInfo(Secure.ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, str2, str, R.string.color_correction_feature_name));
            sFrameworkShortcutFeaturesMap = Collections.unmodifiableMap(featuresMap);
        }
        return sFrameworkShortcutFeaturesMap;
    }

    public AccessibilityShortcutController(Context context, Handler handler, int initialUserId) {
        this.mContext = context;
        this.mHandler = handler;
        this.mUserId = initialUserId;
        ContentObserver co = new ContentObserver(handler) {
            public void onChange(boolean selfChange, Uri uri, int userId) {
                if (userId == AccessibilityShortcutController.this.mUserId) {
                    AccessibilityShortcutController.this.onSettingsChanged();
                }
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Secure.getUriFor(Secure.ACCESSIBILITY_SHORTCUT_TARGET_SERVICE), false, co, -1);
        this.mContext.getContentResolver().registerContentObserver(Secure.getUriFor(Secure.ACCESSIBILITY_SHORTCUT_ENABLED), false, co, -1);
        this.mContext.getContentResolver().registerContentObserver(Secure.getUriFor(Secure.ACCESSIBILITY_SHORTCUT_ON_LOCK_SCREEN), false, co, -1);
        this.mContext.getContentResolver().registerContentObserver(Secure.getUriFor(Secure.ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN), false, co, -1);
        setCurrentUser(this.mUserId);
    }

    public void setCurrentUser(int currentUserId) {
        this.mUserId = currentUserId;
        onSettingsChanged();
    }

    public boolean isAccessibilityShortcutAvailable(boolean phoneLocked) {
        return this.mIsShortcutEnabled && (!phoneLocked || this.mEnabledOnLockScreen);
    }

    public void onSettingsChanged() {
        boolean z = true;
        boolean haveValidService = TextUtils.isEmpty(getTargetServiceComponentNameString(this.mContext, this.mUserId)) ^ true;
        ContentResolver cr = this.mContext.getContentResolver();
        boolean enabled = Secure.getIntForUser(cr, Secure.ACCESSIBILITY_SHORTCUT_ENABLED, 1, this.mUserId) == 1;
        this.mEnabledOnLockScreen = Secure.getIntForUser(cr, Secure.ACCESSIBILITY_SHORTCUT_ON_LOCK_SCREEN, Secure.getIntForUser(cr, Secure.ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN, 0, this.mUserId), this.mUserId) == 1;
        if (!(enabled && haveValidService)) {
            z = false;
        }
        this.mIsShortcutEnabled = z;
    }

    public void performAccessibilityShortcut() {
        String str = TAG;
        Slog.d(str, "Accessibility shortcut activated");
        ContentResolver cr = this.mContext.getContentResolver();
        int userId = ActivityManager.getCurrentUser();
        String str2 = Secure.ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN;
        int dialogAlreadyShown = Secure.getIntForUser(cr, str2, 0, userId);
        Vibrator vibrator = (Vibrator) this.mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(ArrayUtils.convertToLongArray(this.mContext.getResources().getIntArray(R.array.config_longPressVibePattern)), -1, VIBRATION_ATTRIBUTES);
        }
        if (dialogAlreadyShown == 0) {
            this.mAlertDialog = createShortcutWarningDialog(userId);
            AlertDialog alertDialog = this.mAlertDialog;
            if (alertDialog != null) {
                if (!performTtsPrompt(alertDialog)) {
                    playNotificationTone();
                }
                Window w = this.mAlertDialog.getWindow();
                LayoutParams attr = w.getAttributes();
                attr.type = 2009;
                w.setAttributes(attr);
                this.mAlertDialog.show();
                Secure.putIntForUser(cr, str2, 1, userId);
            } else {
                return;
            }
        }
        playNotificationTone();
        AlertDialog alertDialog2 = this.mAlertDialog;
        if (alertDialog2 != null) {
            alertDialog2.dismiss();
            this.mAlertDialog = null;
        }
        if (getShortcutFeatureDescription(false) == null) {
            Slog.e(str, "Accessibility shortcut set to invalid service");
            return;
        }
        AccessibilityServiceInfo serviceInfo = getInfoForTargetService();
        if (serviceInfo != null) {
            int i;
            String toastMessageFormatString = this.mContext;
            if (isServiceEnabled(serviceInfo)) {
                i = R.string.accessibility_shortcut_disabling_service;
            } else {
                i = R.string.accessibility_shortcut_enabling_service;
            }
            Toast warningToast = this.mFrameworkObjectProvider.makeToastFromText(this.mContext, String.format(toastMessageFormatString.getString(i), new Object[]{str2}), 1);
            LayoutParams windowParams = warningToast.getWindowParams();
            windowParams.privateFlags |= 16;
            warningToast.show();
        }
        this.mFrameworkObjectProvider.getAccessibilityManagerInstance(this.mContext).performAccessibilityShortcut();
    }

    private AlertDialog createShortcutWarningDialog(int userId) {
        if (getShortcutFeatureDescription(true) == null) {
            return null;
        }
        CharSequence warningMessage = String.format(this.mContext.getString(R.string.accessibility_shortcut_toogle_warning), new Object[]{serviceDescription});
        FrameworkObjectProvider frameworkObjectProvider = this.mFrameworkObjectProvider;
        return frameworkObjectProvider.getAlertDialogBuilder(frameworkObjectProvider.getSystemUiContext()).setTitle((int) R.string.accessibility_shortcut_warning_dialog_title).setMessage(warningMessage).setCancelable(false).setPositiveButton((int) R.string.leave_accessibility_shortcut_on, null).setNegativeButton((int) R.string.disable_accessibility_shortcut, new -$$Lambda$AccessibilityShortcutController$2NcDVJHkpsPbwr45v1_NfIM8row(this, userId)).setOnCancelListener(new -$$Lambda$AccessibilityShortcutController$T96D356-n5VObNOonEIYV8s83Fc(this, userId)).create();
    }

    public /* synthetic */ void lambda$createShortcutWarningDialog$0$AccessibilityShortcutController(int userId, DialogInterface d, int which) {
        Secure.putStringForUser(this.mContext.getContentResolver(), Secure.ACCESSIBILITY_SHORTCUT_TARGET_SERVICE, "", userId);
    }

    public /* synthetic */ void lambda$createShortcutWarningDialog$1$AccessibilityShortcutController(int userId, DialogInterface d) {
        Secure.putIntForUser(this.mContext.getContentResolver(), Secure.ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN, 0, userId);
    }

    private AccessibilityServiceInfo getInfoForTargetService() {
        String currentShortcutServiceString = getTargetServiceComponentNameString(this.mContext, -2);
        if (currentShortcutServiceString == null) {
            return null;
        }
        return this.mFrameworkObjectProvider.getAccessibilityManagerInstance(this.mContext).getInstalledServiceInfoWithComponentName(ComponentName.unflattenFromString(currentShortcutServiceString));
    }

    private String getShortcutFeatureDescription(boolean includeSummary) {
        String currentShortcutServiceString = getTargetServiceComponentNameString(this.mContext, -2);
        if (currentShortcutServiceString == null) {
            return null;
        }
        ComponentName targetComponentName = ComponentName.unflattenFromString(currentShortcutServiceString);
        ToggleableFrameworkFeatureInfo frameworkFeatureInfo = (ToggleableFrameworkFeatureInfo) getFrameworkShortcutFeaturesMap().get(targetComponentName);
        if (frameworkFeatureInfo != null) {
            return frameworkFeatureInfo.getLabel(this.mContext);
        }
        AccessibilityServiceInfo serviceInfo = this.mFrameworkObjectProvider.getAccessibilityManagerInstance(this.mContext).getInstalledServiceInfoWithComponentName(targetComponentName);
        if (serviceInfo == null) {
            return null;
        }
        PackageManager pm = this.mContext.getPackageManager();
        String label = serviceInfo.getResolveInfo().loadLabel(pm).toString();
        CharSequence summary = serviceInfo.loadSummary(pm);
        if (!includeSummary || TextUtils.isEmpty(summary)) {
            return label;
        }
        return String.format("%s\n%s", new Object[]{label, summary});
    }

    private boolean isServiceEnabled(AccessibilityServiceInfo serviceInfo) {
        return this.mFrameworkObjectProvider.getAccessibilityManagerInstance(this.mContext).getEnabledAccessibilityServiceList(-1).contains(serviceInfo);
    }

    private boolean hasFeatureLeanback() {
        return this.mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK);
    }

    private void playNotificationTone() {
        int audioAttributesUsage;
        if (hasFeatureLeanback()) {
            audioAttributesUsage = 11;
        } else {
            audioAttributesUsage = 10;
        }
        Ringtone tone = this.mFrameworkObjectProvider.getRingtone(this.mContext, System.DEFAULT_NOTIFICATION_URI);
        if (tone != null) {
            tone.setAudioAttributes(new Builder().setUsage(audioAttributesUsage).build());
            tone.play();
        }
    }

    /* JADX WARNING: Missing block: B:8:0x0028, code skipped:
            return false;
     */
    private boolean performTtsPrompt(android.app.AlertDialog r5) {
        /*
        r4 = this;
        r0 = 0;
        r1 = r4.getShortcutFeatureDescription(r0);
        r2 = r4.getInfoForTargetService();
        r3 = android.text.TextUtils.isEmpty(r1);
        if (r3 != 0) goto L_0x0028;
    L_0x000f:
        if (r2 != 0) goto L_0x0012;
    L_0x0011:
        goto L_0x0028;
    L_0x0012:
        r3 = r2.flags;
        r3 = r3 & 1024;
        if (r3 != 0) goto L_0x0019;
    L_0x0018:
        return r0;
    L_0x0019:
        r0 = new com.android.internal.accessibility.AccessibilityShortcutController$TtsPrompt;
        r0.<init>(r1);
        r3 = new com.android.internal.accessibility.-$$Lambda$AccessibilityShortcutController$cQtLiNhDc4H3BvMBZy00zj21oKg;
        r3.<init>(r0);
        r5.setOnDismissListener(r3);
        r3 = 1;
        return r3;
    L_0x0028:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.accessibility.AccessibilityShortcutController.performTtsPrompt(android.app.AlertDialog):boolean");
    }
}
