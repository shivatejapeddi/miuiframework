package com.android.internal.app;

import android.annotation.UnsupportedAppUsage;
import android.app.Activity;
import android.app.ActivityTaskManager;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.metrics.LogMaker;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Slog;
import android.view.View;
import android.widget.Toast;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.telephony.PhoneConstants;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class IntentForwarderActivity extends Activity {
    private static final Set<String> ALLOWED_TEXT_MESSAGE_SCHEMES = new HashSet(Arrays.asList(new String[]{"sms", "smsto", PhoneConstants.APN_TYPE_MMS, "mmsto"}));
    public static String FORWARD_INTENT_TO_MANAGED_PROFILE = "com.android.internal.app.ForwardIntentToManagedProfile";
    public static String FORWARD_INTENT_TO_PARENT = "com.android.internal.app.ForwardIntentToParent";
    @UnsupportedAppUsage
    public static String TAG = "IntentForwarderActivity";
    private static final String TEL_SCHEME = "tel";
    private Injector mInjector;
    private MetricsLogger mMetricsLogger;

    public interface Injector {
        IPackageManager getIPackageManager();

        PackageManager getPackageManager();

        UserManager getUserManager();

        ResolveInfo resolveActivityAsUser(Intent intent, int i, int i2);

        void showToast(int i, int i2);
    }

    private class InjectorImpl implements Injector {
        private InjectorImpl() {
        }

        public IPackageManager getIPackageManager() {
            return AppGlobals.getPackageManager();
        }

        public UserManager getUserManager() {
            return (UserManager) IntentForwarderActivity.this.getSystemService(UserManager.class);
        }

        public PackageManager getPackageManager() {
            return IntentForwarderActivity.this.getPackageManager();
        }

        public ResolveInfo resolveActivityAsUser(Intent intent, int flags, int userId) {
            return getPackageManager().resolveActivityAsUser(intent, flags, userId);
        }

        public void showToast(int messageId, int duration) {
            Context context = IntentForwarderActivity.this;
            Toast.makeText(context, context.getString(messageId), duration).show();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        int userMessageId;
        int targetUserId;
        super.onCreate(savedInstanceState);
        this.mInjector = createInjector();
        Intent intentReceived = getIntent();
        String className = intentReceived.getComponent().getClassName();
        if (className.equals(FORWARD_INTENT_TO_PARENT)) {
            userMessageId = R.string.forward_intent_to_owner;
            targetUserId = getProfileParent();
            getMetricsLogger().write(new LogMaker((int) MetricsEvent.ACTION_SWITCH_SHARE_PROFILE).setSubtype(1));
        } else if (className.equals(FORWARD_INTENT_TO_MANAGED_PROFILE)) {
            userMessageId = R.string.forward_intent_to_work;
            targetUserId = getManagedProfile();
            getMetricsLogger().write(new LogMaker((int) MetricsEvent.ACTION_SWITCH_SHARE_PROFILE).setSubtype(2));
        } else {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(IntentForwarderActivity.class.getName());
            stringBuilder.append(" cannot be called directly");
            Slog.wtf(str, stringBuilder.toString());
            userMessageId = -1;
            targetUserId = -10000;
        }
        if (targetUserId == -10000) {
            finish();
            return;
        }
        int callingUserId = getUserId();
        Intent newIntent = canForward(intentReceived, targetUserId);
        if (newIntent != null) {
            if (Intent.ACTION_CHOOSER.equals(newIntent.getAction())) {
                Intent innerIntent = (Intent) newIntent.getParcelableExtra(Intent.EXTRA_INTENT);
                innerIntent.prepareToLeaveUser(callingUserId);
                innerIntent.fixUris(callingUserId);
            } else {
                newIntent.prepareToLeaveUser(callingUserId);
            }
            ResolveInfo ri = this.mInjector.resolveActivityAsUser(newIntent, 65536, targetUserId);
            try {
                startActivityAsCaller(newIntent, null, null, false, targetUserId);
            } catch (RuntimeException e) {
                int launchedFromUid = -1;
                String launchedFromPackage = "?";
                try {
                    launchedFromUid = ActivityTaskManager.getService().getLaunchedFromUid(getActivityToken());
                    launchedFromPackage = ActivityTaskManager.getService().getLaunchedFromPackage(getActivityToken());
                } catch (RemoteException e2) {
                }
                String str2 = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unable to launch as UID ");
                stringBuilder2.append(launchedFromUid);
                stringBuilder2.append(" package ");
                stringBuilder2.append(launchedFromPackage);
                stringBuilder2.append(", while running in ");
                stringBuilder2.append(ActivityThread.currentProcessName());
                Slog.wtf(str2, stringBuilder2.toString(), e);
            }
            if (shouldShowDisclosure(ri, intentReceived)) {
                this.mInjector.showToast(userMessageId, 1);
            }
        } else {
            String str3 = TAG;
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("the intent: ");
            stringBuilder3.append(intentReceived);
            stringBuilder3.append(" cannot be forwarded from user ");
            stringBuilder3.append(callingUserId);
            stringBuilder3.append(" to user ");
            stringBuilder3.append(targetUserId);
            Slog.wtf(str3, stringBuilder3.toString());
        }
        finish();
    }

    private boolean shouldShowDisclosure(ResolveInfo ri, Intent intent) {
        if (ri == null || ri.activityInfo == null) {
            return true;
        }
        if (ri.activityInfo.applicationInfo.isSystemApp() && (isDialerIntent(intent) || isTextMessageIntent(intent))) {
            return false;
        }
        return 1 ^ isTargetResolverOrChooserActivity(ri.activityInfo);
    }

    private boolean isTextMessageIntent(Intent intent) {
        return (Intent.ACTION_SENDTO.equals(intent.getAction()) || isViewActionIntent(intent)) && ALLOWED_TEXT_MESSAGE_SCHEMES.contains(intent.getScheme());
    }

    /* JADX WARNING: Missing block: B:11:0x0041, code skipped:
            if ("tel".equals(r3.getScheme()) != false) goto L_0x0046;
     */
    private boolean isDialerIntent(android.content.Intent r3) {
        /*
        r2 = this;
        r0 = r3.getAction();
        r1 = "android.intent.action.DIAL";
        r0 = r1.equals(r0);
        if (r0 != 0) goto L_0x0046;
    L_0x000c:
        r0 = r3.getAction();
        r1 = "android.intent.action.CALL";
        r0 = r1.equals(r0);
        if (r0 != 0) goto L_0x0046;
    L_0x0018:
        r0 = r3.getAction();
        r1 = "android.intent.action.CALL_PRIVILEGED";
        r0 = r1.equals(r0);
        if (r0 != 0) goto L_0x0046;
    L_0x0024:
        r0 = r3.getAction();
        r1 = "android.intent.action.CALL_EMERGENCY";
        r0 = r1.equals(r0);
        if (r0 != 0) goto L_0x0046;
    L_0x0030:
        r0 = r2.isViewActionIntent(r3);
        if (r0 == 0) goto L_0x0044;
    L_0x0036:
        r0 = r3.getScheme();
        r1 = "tel";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x0044;
    L_0x0043:
        goto L_0x0046;
    L_0x0044:
        r0 = 0;
        goto L_0x0047;
    L_0x0046:
        r0 = 1;
    L_0x0047:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.app.IntentForwarderActivity.isDialerIntent(android.content.Intent):boolean");
    }

    private boolean isViewActionIntent(Intent intent) {
        return "android.intent.action.VIEW".equals(intent.getAction()) && intent.hasCategory(Intent.CATEGORY_BROWSABLE);
    }

    private boolean isTargetResolverOrChooserActivity(ActivityInfo activityInfo) {
        boolean z = false;
        if (!"android".equals(activityInfo.packageName)) {
            return false;
        }
        if (ResolverActivity.class.getName().equals(activityInfo.name) || ChooserActivity.class.getName().equals(activityInfo.name)) {
            z = true;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public Intent canForward(Intent incomingIntent, int targetUserId) {
        Intent forwardIntent = new Intent(incomingIntent);
        forwardIntent.addFlags(View.SCROLLBARS_OUTSIDE_INSET);
        sanitizeIntent(forwardIntent);
        Intent intentToCheck = forwardIntent;
        if (Intent.ACTION_CHOOSER.equals(forwardIntent.getAction())) {
            if (forwardIntent.hasExtra(Intent.EXTRA_INITIAL_INTENTS)) {
                Slog.wtf(TAG, "An chooser intent with extra initial intents cannot be forwarded to a different user");
                return null;
            } else if (forwardIntent.hasExtra(Intent.EXTRA_REPLACEMENT_EXTRAS)) {
                Slog.wtf(TAG, "A chooser intent with replacement extras cannot be forwarded to a different user");
                return null;
            } else {
                intentToCheck = (Intent) forwardIntent.getParcelableExtra(Intent.EXTRA_INTENT);
                if (intentToCheck == null) {
                    Slog.wtf(TAG, "Cannot forward a chooser intent with no extra android.intent.extra.INTENT");
                    return null;
                }
            }
        }
        if (forwardIntent.getSelector() != null) {
            intentToCheck = forwardIntent.getSelector();
        }
        String resolvedType = intentToCheck.resolveTypeIfNeeded(getContentResolver());
        sanitizeIntent(intentToCheck);
        try {
            if (this.mInjector.getIPackageManager().canForwardTo(intentToCheck, resolvedType, getUserId(), targetUserId)) {
                return forwardIntent;
            }
            return null;
        } catch (RemoteException e) {
            Slog.e(TAG, "PackageManagerService is dead?");
        }
    }

    private int getManagedProfile() {
        for (UserInfo userInfo : this.mInjector.getUserManager().getProfiles(UserHandle.myUserId())) {
            if (userInfo.isManagedProfile()) {
                return userInfo.id;
            }
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(FORWARD_INTENT_TO_MANAGED_PROFILE);
        stringBuilder.append(" has been called, but there is no managed profile");
        Slog.wtf(str, stringBuilder.toString());
        return -10000;
    }

    private int getProfileParent() {
        UserInfo parent = this.mInjector.getUserManager().getProfileParent(UserHandle.myUserId());
        if (parent != null) {
            return parent.id;
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(FORWARD_INTENT_TO_PARENT);
        stringBuilder.append(" has been called, but there is no parent");
        Slog.wtf(str, stringBuilder.toString());
        return -10000;
    }

    private void sanitizeIntent(Intent intent) {
        intent.setPackage(null);
        intent.setComponent(null);
    }

    /* Access modifiers changed, original: protected */
    public MetricsLogger getMetricsLogger() {
        if (this.mMetricsLogger == null) {
            this.mMetricsLogger = new MetricsLogger();
        }
        return this.mMetricsLogger;
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public Injector createInjector() {
        return new InjectorImpl();
    }
}
