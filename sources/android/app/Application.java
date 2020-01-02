package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread.ActivityClientRecord;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Slog;
import android.view.autofill.AutofillManager.AutofillClient;
import android.view.autofill.Helper;
import java.util.ArrayList;
import miui.os.DeviceFeature;
import miui.slide.AppSlideConfig;
import miui.slide.ISlideManagerService.Stub;
import miui.slide.SlideManagerService;

public class Application extends ContextWrapper implements ComponentCallbacks2 {
    private static final String TAG = "Application";
    @UnsupportedAppUsage
    private ArrayList<ActivityLifecycleCallbacks> mActivityLifecycleCallbacks = new ArrayList();
    @UnsupportedAppUsage
    private ArrayList<OnProvideAssistDataListener> mAssistCallbacks = null;
    @UnsupportedAppUsage
    private ArrayList<ComponentCallbacks> mComponentCallbacks = new ArrayList();
    @UnsupportedAppUsage
    public LoadedApk mLoadedApk;

    public interface ActivityLifecycleCallbacks {
        void onActivityCreated(Activity activity, Bundle bundle);

        void onActivityDestroyed(Activity activity);

        void onActivityPaused(Activity activity);

        void onActivityResumed(Activity activity);

        void onActivitySaveInstanceState(Activity activity, Bundle bundle);

        void onActivityStarted(Activity activity);

        void onActivityStopped(Activity activity);

        void onActivityPreCreated(Activity activity, Bundle savedInstanceState) {
        }

        void onActivityPostCreated(Activity activity, Bundle savedInstanceState) {
        }

        void onActivityPreStarted(Activity activity) {
        }

        void onActivityPostStarted(Activity activity) {
        }

        void onActivityPreResumed(Activity activity) {
        }

        void onActivityPostResumed(Activity activity) {
        }

        void onActivityPrePaused(Activity activity) {
        }

        void onActivityPostPaused(Activity activity) {
        }

        void onActivityPreStopped(Activity activity) {
        }

        void onActivityPostStopped(Activity activity) {
        }

        void onActivityPreSaveInstanceState(Activity activity, Bundle outState) {
        }

        void onActivityPostSaveInstanceState(Activity activity, Bundle outState) {
        }

        void onActivityPreDestroyed(Activity activity) {
        }

        void onActivityPostDestroyed(Activity activity) {
        }
    }

    public interface OnProvideAssistDataListener {
        void onProvideAssistData(Activity activity, Bundle bundle);
    }

    public Application() {
        super(null);
    }

    public void onCreate() {
        ApplicationInjector.onCreate(this);
        try {
            if (DeviceFeature.hasMirihiSupport()) {
                String packageName = getPackageName();
                AppGlobals.mAppSlideConfig = Stub.asInterface(ServiceManager.getService(SlideManagerService.SERVICE_NAME)).getAppSlideConfig(packageName, getPackageManager().getPackageInfo(packageName, 0).versionCode);
            }
        } catch (Exception e) {
            Slog.d(AppSlideConfig.TAG, e.toString());
        }
    }

    public void onTerminate() {
    }

    public void onConfigurationChanged(Configuration newConfig) {
        Object[] callbacks = collectComponentCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ComponentCallbacks) obj).onConfigurationChanged(newConfig);
            }
        }
    }

    public void onLowMemory() {
        Object[] callbacks = collectComponentCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ComponentCallbacks) obj).onLowMemory();
            }
        }
    }

    public void onTrimMemory(int level) {
        Object[] callbacks = collectComponentCallbacks();
        if (callbacks != null) {
            for (Object c : callbacks) {
                if (c instanceof ComponentCallbacks2) {
                    ((ComponentCallbacks2) c).onTrimMemory(level);
                }
            }
        }
    }

    public void registerComponentCallbacks(ComponentCallbacks callback) {
        synchronized (this.mComponentCallbacks) {
            this.mComponentCallbacks.add(callback);
        }
    }

    public void unregisterComponentCallbacks(ComponentCallbacks callback) {
        synchronized (this.mComponentCallbacks) {
            this.mComponentCallbacks.remove(callback);
        }
    }

    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        synchronized (this.mActivityLifecycleCallbacks) {
            this.mActivityLifecycleCallbacks.add(callback);
        }
    }

    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        synchronized (this.mActivityLifecycleCallbacks) {
            this.mActivityLifecycleCallbacks.remove(callback);
        }
    }

    public void registerOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        synchronized (this) {
            if (this.mAssistCallbacks == null) {
                this.mAssistCallbacks = new ArrayList();
            }
            this.mAssistCallbacks.add(callback);
        }
    }

    public void unregisterOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        synchronized (this) {
            if (this.mAssistCallbacks != null) {
                this.mAssistCallbacks.remove(callback);
            }
        }
    }

    public static String getProcessName() {
        return ActivityThread.currentProcessName();
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final void attach(Context context) {
        attachBaseContext(context);
        this.mLoadedApk = ContextImpl.getImpl(context).mPackageInfo;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPreCreated(Activity activity, Bundle savedInstanceState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPreCreated(activity, savedInstanceState);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityCreated(Activity activity, Bundle savedInstanceState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityCreated(activity, savedInstanceState);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPostCreated(Activity activity, Bundle savedInstanceState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPostCreated(activity, savedInstanceState);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPreStarted(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPreStarted(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityStarted(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityStarted(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPostStarted(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPostStarted(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPreResumed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPreResumed(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityResumed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityResumed(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPostResumed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPostResumed(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPrePaused(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPrePaused(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPaused(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPaused(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPostPaused(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPostPaused(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPreStopped(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPreStopped(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityStopped(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityStopped(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPostStopped(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPostStopped(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPreSaveInstanceState(Activity activity, Bundle outState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPreSaveInstanceState(activity, outState);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivitySaveInstanceState(Activity activity, Bundle outState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivitySaveInstanceState(activity, outState);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPostSaveInstanceState(Activity activity, Bundle outState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPostSaveInstanceState(activity, outState);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPreDestroyed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPreDestroyed(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityDestroyed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityDestroyed(activity);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityPostDestroyed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPostDestroyed(activity);
            }
        }
    }

    private Object[] collectComponentCallbacks() {
        Object[] callbacks = null;
        synchronized (this.mComponentCallbacks) {
            if (this.mComponentCallbacks.size() > 0) {
                callbacks = this.mComponentCallbacks.toArray();
            }
        }
        return callbacks;
    }

    @UnsupportedAppUsage
    private Object[] collectActivityLifecycleCallbacks() {
        Object[] callbacks = null;
        synchronized (this.mActivityLifecycleCallbacks) {
            if (this.mActivityLifecycleCallbacks.size() > 0) {
                callbacks = this.mActivityLifecycleCallbacks.toArray();
            }
        }
        return callbacks;
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:8:0x000e, code skipped:
            if (r0 == null) goto L_0x001e;
     */
    /* JADX WARNING: Missing block: B:9:0x0010, code skipped:
            r1 = 0;
     */
    /* JADX WARNING: Missing block: B:11:0x0012, code skipped:
            if (r1 >= r0.length) goto L_0x001e;
     */
    /* JADX WARNING: Missing block: B:12:0x0014, code skipped:
            ((android.app.Application.OnProvideAssistDataListener) r0[r1]).onProvideAssistData(r4, r5);
            r1 = r1 + 1;
     */
    /* JADX WARNING: Missing block: B:13:0x001e, code skipped:
            return;
     */
    public void dispatchOnProvideAssistData(android.app.Activity r4, android.os.Bundle r5) {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = r3.mAssistCallbacks;	 Catch:{ all -> 0x001f }
        if (r0 != 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r3);	 Catch:{ all -> 0x001f }
        return;
    L_0x0007:
        r0 = r3.mAssistCallbacks;	 Catch:{ all -> 0x001f }
        r0 = r0.toArray();	 Catch:{ all -> 0x001f }
        monitor-exit(r3);	 Catch:{ all -> 0x001f }
        if (r0 == 0) goto L_0x001e;
    L_0x0010:
        r1 = 0;
    L_0x0011:
        r2 = r0.length;
        if (r1 >= r2) goto L_0x001e;
    L_0x0014:
        r2 = r0[r1];
        r2 = (android.app.Application.OnProvideAssistDataListener) r2;
        r2.onProvideAssistData(r4, r5);
        r1 = r1 + 1;
        goto L_0x0011;
    L_0x001e:
        return;
    L_0x001f:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x001f }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Application.dispatchOnProvideAssistData(android.app.Activity, android.os.Bundle):void");
    }

    public AutofillClient getAutofillClient() {
        AutofillClient client = super.getAutofillClient();
        if (client != null) {
            return client;
        }
        boolean z = Helper.sVerbose;
        String str = TAG;
        if (z) {
            Log.v(str, "getAutofillClient(): null on super, trying to find activity thread");
        }
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        if (activityThread == null) {
            return null;
        }
        int activityCount = activityThread.mActivities.size();
        for (int i = 0; i < activityCount; i++) {
            ActivityClientRecord record = (ActivityClientRecord) activityThread.mActivities.valueAt(i);
            if (record != null) {
                Activity activity = record.activity;
                if (activity != null && activity.getWindow().getDecorView().hasFocus()) {
                    if (Helper.sVerbose) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("getAutofillClient(): found activity for ");
                        stringBuilder.append(this);
                        stringBuilder.append(": ");
                        stringBuilder.append(activity);
                        Log.v(str, stringBuilder.toString());
                    }
                    return activity;
                }
            }
        }
        if (Helper.sVerbose) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("getAutofillClient(): none of the ");
            stringBuilder2.append(activityCount);
            stringBuilder2.append(" activities on ");
            stringBuilder2.append(this);
            stringBuilder2.append(" have focus");
            Log.v(str, stringBuilder2.toString());
        }
        return null;
    }
}
