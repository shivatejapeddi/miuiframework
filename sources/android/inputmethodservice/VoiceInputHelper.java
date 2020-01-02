package android.inputmethodservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.inputmethodservice.RecodingStateAnimatorView.State;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.miui.R;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MiuiSettings;
import android.provider.MiuiSettings.XSpace;
import android.provider.Settings.Secure;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.android.internal.telephony.IccCardConstants;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import miui.os.Build;

public class VoiceInputHelper {
    public static final String API_KEY = "GKGRAg3Rim3Ku6_ZLM77dwjADiVKLrhtva7D8A-9now";
    public static final String CLIENT_ID = "2882303761517744214";
    private static final boolean DEBUG = true;
    private static final String DETECTION_PREFIX = "...";
    private static final int DETECTION_SHOW_MAX_LEN = 14;
    public static final String MI_BRAIN_ASR_SERVICE = "com.xiaomi.mibrain.speech.asr.AsrService";
    public static final String MI_BRAIN_PKG = "com.xiaomi.mibrain.speech";
    public static final String PERMISSION_RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static final String SIGN_SECRET = "iQGcaANtifIKTw3bRdXOmzpxx9h1jegkIlMsQt2ly6T8udJcLw7VYy7PuyCcE59tuatzhCwzZUMoR1qHa992jw";
    private static final String TAG = "VoiceInputHelper";
    private static ArrayList<String> blacklist = new ArrayList<String>() {
        {
            add("com.taobao.taobao");
            add("com.tencent.qqmusic");
            add("com.jingdong.app.mall");
            add("com.tmall.wireless");
            add("com.baidu.searchbox");
            add("com.baidu.BaiduMap");
            add("com.baidu.browser.apps");
            add("com.youku.phone");
            add("com.qiyi.video");
            add("com.ximalaya.ting.android");
            add(XSpace.QQ_PACKAGE_NAME);
            add("com.tencent.mm");
        }
    };
    private static OnAudioFocusChangeListener sAudioFocusChangeLister = new OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
        }
    };
    private static SpeechRecognizationCallback sCallback = null;
    private static boolean sIsEnabled = true;
    private static boolean sIsInSpeech = false;
    private static boolean sIsSpeechRecognizerReady = false;
    private static Random sRandom = null;
    private static SettingsObserver sSettingsObserver = null;
    private static SpeechRecognizer sSpeechRecognizer;
    private static Set<String> supportedImes = new HashSet<String>() {
        {
            add("com.baidu.input_mi");
            add("com.sohu.inputmethod.sogou.xiaomi");
        }
    };

    interface SpeechRecognizationCallback {
        void updateAnimation(State state);

        void updateText(String str);

        void updateText(String str, boolean z);
    }

    private static class SettingsObserver extends ContentObserver {
        private Context mContext;

        public SettingsObserver(Context ctx, Handler handler) {
            super(handler);
            this.mContext = ctx;
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            try {
                boolean z = true;
                if (Secure.getInt(this.mContext.getContentResolver(), MiuiSettings.Secure.ENABLE_MIUI_IME_XIAOAI_VOICE, 1) == 0) {
                    z = false;
                }
                VoiceInputHelper.sIsEnabled = z;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("enable status changed: ");
                stringBuilder.append(VoiceInputHelper.sIsEnabled);
                VoiceInputHelper.log(stringBuilder.toString());
            } catch (Exception e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("fail to read user choice, ");
                stringBuilder2.append(e);
                VoiceInputHelper.log(stringBuilder2.toString());
            }
        }
    }

    static class SpeechListener implements RecognitionListener {
        boolean hasResult = false;
        WeakReference<InputMethodService> imsWeakRef;
        Context mContext;

        public SpeechListener(Context ctx, InputMethodService ims) {
            this.mContext = ctx;
            this.imsWeakRef = new WeakReference(ims);
        }

        public void onReadyForSpeech(Bundle params) {
        }

        public void onBeginningOfSpeech() {
            VoiceInputHelper.log("onBeginningOfSpeech");
            if (VoiceInputHelper.sCallback != null) {
                VoiceInputHelper.sCallback.updateText(this.mContext.getResources().getString(R.string.voice_begin));
            }
            VoiceInputHelper.sIsInSpeech = true;
            this.hasResult = false;
        }

        public void onRmsChanged(float rmsdB) {
        }

        public void onBufferReceived(byte[] buffer) {
        }

        public void onEndOfSpeech() {
            VoiceInputHelper.log("onEndOfSpeech...");
            if (VoiceInputHelper.sCallback != null) {
                VoiceInputHelper.sCallback.updateAnimation(null);
            }
            VoiceInputHelper.sIsInSpeech = false;
        }

        public void onError(int error) {
            String hintToUser = "";
            hintToUser = (error == 1 || error == 2) ? this.mContext.getResources().getString(R.string.voice_input_error_network) : (error == 6 || error == 7) ? "" : this.mContext.getResources().getString(R.string.voice_input_error_other);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onError:");
            stringBuilder.append(VoiceInputHelper.getErrorStr(error));
            stringBuilder.append("(");
            stringBuilder.append(error);
            stringBuilder.append(")");
            VoiceInputHelper.log(true, stringBuilder.toString());
            if (VoiceInputHelper.sCallback != null) {
                VoiceInputHelper.sCallback.updateAnimation(null);
                if (!this.hasResult && !hintToUser.isEmpty()) {
                    VoiceInputHelper.sCallback.updateText(hintToUser, false);
                }
            }
        }

        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onResults, matched size=");
                stringBuilder.append(matches.size());
                VoiceInputHelper.log(stringBuilder.toString());
                String ret = "";
                Iterator it = matches.iterator();
                while (it.hasNext()) {
                    String val = (String) it.next();
                    if (!val.isEmpty()) {
                        ret = val;
                        break;
                    }
                }
                if (!ret.isEmpty()) {
                    WeakReference weakReference = this.imsWeakRef;
                    InputConnection ic = null;
                    InputMethodService ims = weakReference != null ? (InputMethodService) weakReference.get() : null;
                    if (ims != null) {
                        ic = ims.getCurrentInputConnection();
                    }
                    if (ic != null) {
                        if (VoiceInputHelper.sCallback != null) {
                            VoiceInputHelper.sCallback.updateText(ret);
                        }
                        ic.commitText(ret, 1);
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("commitText=");
                        stringBuilder2.append(ret);
                        VoiceInputHelper.log(stringBuilder2.toString());
                        this.hasResult = true;
                    } else {
                        VoiceInputHelper.log(true, "input connection is null");
                    }
                }
            }
        }

        public void onPartialResults(Bundle partialResults) {
            ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onPartialResults, matched size=");
                stringBuilder.append(matches.size());
                VoiceInputHelper.log(stringBuilder.toString());
                String partial = "";
                Iterator it = matches.iterator();
                while (it.hasNext()) {
                    partial = (String) it.next();
                }
                if (!(VoiceInputHelper.sCallback == null || TextUtils.isEmpty(partial))) {
                    VoiceInputHelper.sCallback.updateText(partial);
                    VoiceInputHelper.sCallback.updateAnimation(State.RECORDING_HAS_VOICE);
                }
            }
        }

        public void onEvent(int eventType, Bundle params) {
            VoiceInputHelper.log("onEvent");
        }
    }

    public static void setCallback(SpeechRecognizationCallback callback) {
        sCallback = callback;
    }

    public static void initSpeech(Context context, InputMethodService ims) {
        initSpeech(context, ims, null);
    }

    public static void initSpeech(Context context, InputMethodService ims, SpeechRecognizationCallback callback) {
        String str = MiuiSettings.Secure.ENABLE_MIUI_IME_XIAOAI_VOICE;
        log("initSpeech....start");
        sIsSpeechRecognizerReady = false;
        try {
            sIsEnabled = Secure.getInt(context.getContentResolver(), str, 1) != 0;
            if (sSettingsObserver == null) {
                Context appCtx = context.getApplicationContext();
                sSettingsObserver = new SettingsObserver(appCtx, new Handler());
                appCtx.getContentResolver().registerContentObserver(Secure.getUriFor(str), false, sSettingsObserver, appCtx.getUserId());
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("fail to read user choice, ");
            stringBuilder.append(e);
            log(stringBuilder.toString());
        }
        if (enforceAudioPermission(context)) {
            destroy();
            if (enforceMiSpeechSupport(context)) {
                sSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context, new ComponentName(MI_BRAIN_PKG, MI_BRAIN_ASR_SERVICE));
                log("Mi brain supported.");
                sSpeechRecognizer.setRecognitionListener(new SpeechListener(context, ims));
                sIsSpeechRecognizerReady = true;
                sCallback = callback;
                log(true, "initSpeech...success.");
                return;
            }
            log(true, "Mi brain UN-supported.");
            return;
        }
        log(true, "No permission:android.permission.RECORD_AUDIO");
    }

    public static void destroy() {
        SpeechRecognizer speechRecognizer = sSpeechRecognizer;
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            sSpeechRecognizer = null;
            sIsSpeechRecognizerReady = false;
            log("destroy...success.");
            return;
        }
        log("destroy...sSpeechRecognizer already is null.");
    }

    public static void setIsInSpeech(boolean inSpeech) {
        sIsInSpeech = inSpeech;
    }

    public static void startListening(Context ctx) {
        if (!sIsSpeechRecognizerReady || sSpeechRecognizer == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[START]failed, sIsSpeechRecognizerReady=");
            stringBuilder.append(sIsSpeechRecognizerReady);
            log(true, stringBuilder.toString());
            return;
        }
        String pkgName = ctx.getPackageName();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("[START]voice recognizer...pkgName=");
        stringBuilder2.append(pkgName);
        log(stringBuilder2.toString());
        int result = ((AudioManager) ctx.getSystemService("audio")).requestAudioFocus(sAudioFocusChangeLister, 3, 4);
        if (result != 1) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Fail to request audio focus, result: ");
            stringBuilder3.append(result);
            log(true, stringBuilder3.toString());
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        if (!TextUtils.isEmpty(pkgName)) {
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, pkgName);
        }
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra("useVad", false);
        intent.putExtra("client_id", CLIENT_ID);
        intent.putExtra("api_key", API_KEY);
        intent.putExtra("sign_secret", SIGN_SECRET);
        intent.putExtra("miref", "com.miui.im_voice");
        sSpeechRecognizer.startListening(intent);
    }

    public static void stopListening(Context ctx) {
        if (sIsInSpeech) {
            if (sIsSpeechRecognizerReady) {
                SpeechRecognizer speechRecognizer = sSpeechRecognizer;
                if (speechRecognizer != null) {
                    speechRecognizer.stopListening();
                    if (((AudioManager) ctx.getSystemService("audio")).abandonAudioFocus(sAudioFocusChangeLister) != 1) {
                        log(true, "Fail to recover the audio focus");
                    }
                    log("[STOP]voice recognizer...");
                    return;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[STOP]ignore, sIsSpeechRecognizerReady=");
            stringBuilder.append(sIsSpeechRecognizerReady);
            log(true, stringBuilder.toString());
            return;
        }
        log("[STOP]ignore, engine already stopped by itself automatically.");
    }

    public static boolean enforceMiSpeechSupport(Context context) {
        Intent intent = new Intent();
        intent.setClassName(MI_BRAIN_PKG, MI_BRAIN_ASR_SERVICE);
        List<ResolveInfo> list = context.getPackageManager().queryIntentServices(intent, 0);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("enforceMiSpeechSupport, service size=");
        stringBuilder.append(list.size());
        log(stringBuilder.toString());
        if (list.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isSpeechRecognizerReady() {
        return sIsSpeechRecognizerReady;
    }

    public static boolean enforceAudioPermission(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.RECORD_AUDIO") == 0;
    }

    public static boolean showSpeechBar(EditorInfo editorInfo, boolean isFullScreenMode, String imePkgName) {
        boolean z = false;
        if (!sIsEnabled) {
            return false;
        }
        if (!isSpeechRecognizerReady()) {
            log(true, "SpeechRecognizer is NOT ready!");
            return false;
        } else if (isFullScreenMode || Build.IS_INTERNATIONAL_BUILD || editorInfo == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("isFullScreenMode=");
            stringBuilder.append(isFullScreenMode);
            stringBuilder.append(", isInternationalBuild=");
            stringBuilder.append(Build.IS_INTERNATIONAL_BUILD);
            stringBuilder.append(", editorInfo=");
            stringBuilder.append(editorInfo == null ? "null" : editorInfo);
            log(stringBuilder.toString());
            return false;
        } else {
            int textVariation = editorInfo.inputType & InputType.TYPE_MASK_VARIATION;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("inputType=");
            stringBuilder2.append(editorInfo.inputType);
            stringBuilder2.append(", textVariation=");
            stringBuilder2.append(textVariation);
            log(stringBuilder2.toString());
            if ((textVariation & 16) == 16 || (textVariation & 144) == 144 || (textVariation & 128) == 128) {
                log("showSpeechBar...password!");
                return false;
            } else if (supportedImes.contains(imePkgName)) {
                Iterator it = blacklist.iterator();
                while (it.hasNext()) {
                    if (((String) it.next()).contains(editorInfo.packageName)) {
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("showSpeechBar...hit blacklist, pkgName=");
                        stringBuilder3.append(editorInfo.packageName);
                        log(stringBuilder3.toString());
                        return false;
                    }
                }
                int imeOptions = editorInfo.imeOptions & 255;
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("Masked imeOptions=");
                stringBuilder4.append(imeOptions);
                log(stringBuilder4.toString());
                if (imeOptions == 3 || imeOptions == 2) {
                    z = true;
                }
                return z;
            } else {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("IME not supported, pkg=");
                stringBuilder2.append(imePkgName);
                log(true, stringBuilder2.toString());
                return false;
            }
        }
    }

    public static String getErrorStr(int error) {
        String errorReason = IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        switch (error) {
            case 1:
                return "ERROR_NETWORK_TIMEOUT";
            case 2:
                return "ERROR_NETWORK";
            case 3:
                return "ERROR_AUDIO";
            case 4:
                return "ERROR_SERVER";
            case 5:
                return "ERROR_CLIENT";
            case 6:
                return "ERROR_SPEECH_TIMEOUT";
            case 7:
                return "ERROR_NO_MATCH";
            case 8:
                return "ERROR_RECOGNIZER_BUSY";
            case 9:
                return "ERROR_INSUFFICIENT_PERMISSIONS";
            default:
                return errorReason;
        }
    }

    public static String formatText(String text) {
        String formatted = "";
        if (TextUtils.isEmpty(text)) {
            return formatted;
        }
        String stringBuilder;
        if (text.length() > 14) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("...");
            stringBuilder2.append(text.substring(text.length() - 14));
            stringBuilder = stringBuilder2.toString();
        } else {
            stringBuilder = text;
        }
        return stringBuilder;
    }

    public static void log(String msg) {
        log(false, msg);
    }

    public static void log(boolean debug, String msg) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Niel...");
        stringBuilder.append(msg);
        Log.d(TAG, stringBuilder.toString());
    }
}
