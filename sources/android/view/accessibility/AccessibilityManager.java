package android.view.accessibility;

import android.Manifest.permission;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.view.IWindow;
import android.view.accessibility.IAccessibilityManagerClient.Stub;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IntPair;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AccessibilityManager {
    public static final String ACTION_CHOOSE_ACCESSIBILITY_BUTTON = "com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON";
    public static final int AUTOCLICK_DELAY_DEFAULT = 600;
    public static final int DALTONIZER_CORRECT_DEUTERANOMALY = 12;
    public static final int DALTONIZER_DISABLED = -1;
    @UnsupportedAppUsage
    public static final int DALTONIZER_SIMULATE_MONOCHROMACY = 0;
    private static final boolean DEBUG = false;
    public static final int FLAG_CONTENT_CONTROLS = 4;
    public static final int FLAG_CONTENT_ICONS = 1;
    public static final int FLAG_CONTENT_TEXT = 2;
    private static final String LOG_TAG = "AccessibilityManager";
    public static final int STATE_FLAG_ACCESSIBILITY_ENABLED = 1;
    public static final int STATE_FLAG_HIGH_TEXT_CONTRAST_ENABLED = 4;
    public static final int STATE_FLAG_TOUCH_EXPLORATION_ENABLED = 2;
    @UnsupportedAppUsage
    private static AccessibilityManager sInstance;
    @UnsupportedAppUsage
    static final Object sInstanceSync = new Object();
    AccessibilityPolicy mAccessibilityPolicy;
    @UnsupportedAppUsage
    private final ArrayMap<AccessibilityStateChangeListener, Handler> mAccessibilityStateChangeListeners = new ArrayMap();
    final Callback mCallback = new MyCallback(this, null);
    private final Stub mClient = new Stub() {
        public void setState(int state) {
            AccessibilityManager.this.mHandler.obtainMessage(1, state, 0).sendToTarget();
        }

        /* JADX WARNING: Missing block: B:9:0x0026, code skipped:
            r0 = r1.size();
            r2 = 0;
     */
        /* JADX WARNING: Missing block: B:10:0x002b, code skipped:
            if (r2 >= r0) goto L_0x0050;
     */
        /* JADX WARNING: Missing block: B:11:0x002d, code skipped:
            ((android.os.Handler) android.view.accessibility.AccessibilityManager.access$200(r6.this$0).valueAt(r2)).post(new android.view.accessibility.-$$Lambda$AccessibilityManager$1$o7fCplskH9NlBwJvkl6NoZ0L_BA(r6, (android.view.accessibility.AccessibilityManager.AccessibilityServicesStateChangeListener) android.view.accessibility.AccessibilityManager.access$200(r6.this$0).keyAt(r2)));
            r2 = r2 + 1;
     */
        /* JADX WARNING: Missing block: B:12:0x0050, code skipped:
            return;
     */
        public void notifyServicesStateChanged(long r7) {
            /*
            r6 = this;
            r0 = android.view.accessibility.AccessibilityManager.this;
            r0.updateUiTimeout(r7);
            r0 = android.view.accessibility.AccessibilityManager.this;
            r0 = r0.mLock;
            monitor-enter(r0);
            r1 = android.view.accessibility.AccessibilityManager.this;	 Catch:{ all -> 0x0051 }
            r1 = r1.mServicesStateChangeListeners;	 Catch:{ all -> 0x0051 }
            r1 = r1.isEmpty();	 Catch:{ all -> 0x0051 }
            if (r1 == 0) goto L_0x001a;
        L_0x0018:
            monitor-exit(r0);	 Catch:{ all -> 0x0051 }
            return;
        L_0x001a:
            r1 = new android.util.ArrayMap;	 Catch:{ all -> 0x0051 }
            r2 = android.view.accessibility.AccessibilityManager.this;	 Catch:{ all -> 0x0051 }
            r2 = r2.mServicesStateChangeListeners;	 Catch:{ all -> 0x0051 }
            r1.<init>(r2);	 Catch:{ all -> 0x0051 }
            monitor-exit(r0);	 Catch:{ all -> 0x0051 }
            r0 = r1.size();
            r2 = 0;
        L_0x002b:
            if (r2 >= r0) goto L_0x0050;
        L_0x002d:
            r3 = android.view.accessibility.AccessibilityManager.this;
            r3 = r3.mServicesStateChangeListeners;
            r3 = r3.keyAt(r2);
            r3 = (android.view.accessibility.AccessibilityManager.AccessibilityServicesStateChangeListener) r3;
            r4 = android.view.accessibility.AccessibilityManager.this;
            r4 = r4.mServicesStateChangeListeners;
            r4 = r4.valueAt(r2);
            r4 = (android.os.Handler) r4;
            r5 = new android.view.accessibility.-$$Lambda$AccessibilityManager$1$o7fCplskH9NlBwJvkl6NoZ0L_BA;
            r5.<init>(r6, r3);
            r4.post(r5);
            r2 = r2 + 1;
            goto L_0x002b;
        L_0x0050:
            return;
        L_0x0051:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0051 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager$AnonymousClass1.notifyServicesStateChanged(long):void");
        }

        public /* synthetic */ void lambda$notifyServicesStateChanged$0$AccessibilityManager$1(AccessibilityServicesStateChangeListener listener) {
            listener.onAccessibilityServicesStateChanged(AccessibilityManager.this);
        }

        public void setRelevantEventTypes(int eventTypes) {
            AccessibilityManager.this.mRelevantEventTypes = eventTypes;
        }
    };
    @UnsupportedAppUsage
    final Handler mHandler;
    private final ArrayMap<HighTextContrastChangeListener, Handler> mHighTextContrastStateChangeListeners = new ArrayMap();
    int mInteractiveUiTimeout;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    boolean mIsEnabled;
    @UnsupportedAppUsage(trackingBug = 123768939)
    boolean mIsHighTextContrastEnabled;
    boolean mIsTouchExplorationEnabled;
    @UnsupportedAppUsage
    private final Object mLock = new Object();
    int mNonInteractiveUiTimeout;
    int mRelevantEventTypes = -1;
    private SparseArray<List<AccessibilityRequestPreparer>> mRequestPreparerLists;
    @UnsupportedAppUsage
    private IAccessibilityManager mService;
    private final ArrayMap<AccessibilityServicesStateChangeListener, Handler> mServicesStateChangeListeners = new ArrayMap();
    private final ArrayMap<TouchExplorationStateChangeListener, Handler> mTouchExplorationStateChangeListeners = new ArrayMap();
    @UnsupportedAppUsage
    final int mUserId;

    public interface AccessibilityStateChangeListener {
        void onAccessibilityStateChanged(boolean z);
    }

    public interface HighTextContrastChangeListener {
        void onHighTextContrastStateChanged(boolean z);
    }

    public interface AccessibilityPolicy {
        List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int i, List<AccessibilityServiceInfo> list);

        List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(List<AccessibilityServiceInfo> list);

        int getRelevantEventTypes(int i);

        boolean isEnabled(boolean z);

        AccessibilityEvent onAccessibilityEvent(AccessibilityEvent accessibilityEvent, boolean z, int i);
    }

    public interface AccessibilityServicesStateChangeListener {
        void onAccessibilityServicesStateChanged(AccessibilityManager accessibilityManager);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ContentFlag {
    }

    private final class MyCallback implements Callback {
        public static final int MSG_SET_STATE = 1;

        private MyCallback() {
        }

        /* synthetic */ MyCallback(AccessibilityManager x0, AnonymousClass1 x1) {
            this();
        }

        public boolean handleMessage(Message message) {
            if (message.what == 1) {
                int state = message.arg1;
                synchronized (AccessibilityManager.this.mLock) {
                    AccessibilityManager.this.setStateLocked(state);
                }
            }
            return true;
        }
    }

    public interface TouchExplorationStateChangeListener {
        void onTouchExplorationStateChanged(boolean z);
    }

    @UnsupportedAppUsage
    public static AccessibilityManager getInstance(Context context) {
        synchronized (sInstanceSync) {
            if (sInstance == null) {
                int userId;
                if (!(Binder.getCallingUid() == 1000 || context.checkCallingOrSelfPermission(permission.INTERACT_ACROSS_USERS) == 0)) {
                    if (context.checkCallingOrSelfPermission(permission.INTERACT_ACROSS_USERS_FULL) != 0) {
                        userId = context.getUserId();
                        sInstance = new AccessibilityManager(context, null, userId);
                    }
                }
                userId = -2;
                sInstance = new AccessibilityManager(context, null, userId);
            }
        }
        return sInstance;
    }

    public AccessibilityManager(Context context, IAccessibilityManager service, int userId) {
        this.mHandler = new Handler(context.getMainLooper(), this.mCallback);
        this.mUserId = userId;
        synchronized (this.mLock) {
            tryConnectToServiceLocked(service);
        }
    }

    public AccessibilityManager(Handler handler, IAccessibilityManager service, int userId) {
        this.mHandler = handler;
        this.mUserId = userId;
        synchronized (this.mLock) {
            tryConnectToServiceLocked(service);
        }
    }

    public IAccessibilityManagerClient getClient() {
        return this.mClient;
    }

    @VisibleForTesting
    public Callback getCallback() {
        return this.mCallback;
    }

    public boolean isEnabled() {
        boolean z;
        synchronized (this.mLock) {
            if (!this.mIsEnabled) {
                if (this.mAccessibilityPolicy == null || !this.mAccessibilityPolicy.isEnabled(this.mIsEnabled)) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public boolean isTouchExplorationEnabled() {
        synchronized (this.mLock) {
            if (getServiceLocked() == null) {
                return false;
            }
            boolean z = this.mIsTouchExplorationEnabled;
            return z;
        }
    }

    @UnsupportedAppUsage
    public boolean isHighTextContrastEnabled() {
        synchronized (this.mLock) {
            if (getServiceLocked() == null) {
                return false;
            }
            boolean z = this.mIsHighTextContrastEnabled;
            return z;
        }
    }

    /* JADX WARNING: Missing block: B:30:?, code skipped:
            r4 = android.os.Binder.clearCallingIdentity();
     */
    /* JADX WARNING: Missing block: B:32:?, code skipped:
            r1.sendAccessibilityEvent(r2, r3);
     */
    /* JADX WARNING: Missing block: B:34:?, code skipped:
            android.os.Binder.restoreCallingIdentity(r4);
     */
    /* JADX WARNING: Missing block: B:35:0x005f, code skipped:
            if (r8 == r2) goto L_0x0064;
     */
    /* JADX WARNING: Missing block: B:36:0x0061, code skipped:
            r8.recycle();
     */
    /* JADX WARNING: Missing block: B:37:0x0064, code skipped:
            r2.recycle();
     */
    /* JADX WARNING: Missing block: B:40:?, code skipped:
            android.os.Binder.restoreCallingIdentity(r4);
     */
    /* JADX WARNING: Missing block: B:43:0x006f, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:45:?, code skipped:
            r4 = LOG_TAG;
            r5 = new java.lang.StringBuilder();
            r5.append("Error during sending ");
            r5.append(r2);
            r5.append(android.net.wifi.WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            android.util.Log.e(r4, r5.toString(), r0);
     */
    /* JADX WARNING: Missing block: B:46:0x008c, code skipped:
            if (r8 == r2) goto L_0x0064;
     */
    /* JADX WARNING: Missing block: B:47:0x008f, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:48:0x0090, code skipped:
            if (r8 != r2) goto L_0x0092;
     */
    /* JADX WARNING: Missing block: B:49:0x0092, code skipped:
            r8.recycle();
     */
    /* JADX WARNING: Missing block: B:50:0x0095, code skipped:
            r2.recycle();
     */
    public void sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent r8) {
        /*
        r7 = this;
        r0 = r7.mLock;
        monitor-enter(r0);
        r1 = r7.getServiceLocked();	 Catch:{ all -> 0x0099 }
        if (r1 != 0) goto L_0x000b;
    L_0x0009:
        monitor-exit(r0);	 Catch:{ all -> 0x0099 }
        return;
    L_0x000b:
        r2 = android.os.SystemClock.uptimeMillis();	 Catch:{ all -> 0x0099 }
        r8.setEventTime(r2);	 Catch:{ all -> 0x0099 }
        r2 = r7.mAccessibilityPolicy;	 Catch:{ all -> 0x0099 }
        if (r2 == 0) goto L_0x0024;
    L_0x0016:
        r2 = r7.mAccessibilityPolicy;	 Catch:{ all -> 0x0099 }
        r3 = r7.mIsEnabled;	 Catch:{ all -> 0x0099 }
        r4 = r7.mRelevantEventTypes;	 Catch:{ all -> 0x0099 }
        r2 = r2.onAccessibilityEvent(r8, r3, r4);	 Catch:{ all -> 0x0099 }
        if (r2 != 0) goto L_0x0025;
    L_0x0022:
        monitor-exit(r0);	 Catch:{ all -> 0x0099 }
        return;
    L_0x0024:
        r2 = r8;
    L_0x0025:
        r3 = r7.isEnabled();	 Catch:{ all -> 0x0099 }
        if (r3 != 0) goto L_0x0046;
    L_0x002b:
        r3 = android.os.Looper.myLooper();	 Catch:{ all -> 0x0099 }
        r4 = android.os.Looper.getMainLooper();	 Catch:{ all -> 0x0099 }
        if (r3 == r4) goto L_0x003e;
    L_0x0035:
        r4 = "AccessibilityManager";
        r5 = "AccessibilityEvent sent with accessibility disabled";
        android.util.Log.e(r4, r5);	 Catch:{ all -> 0x0099 }
        monitor-exit(r0);	 Catch:{ all -> 0x0099 }
        return;
    L_0x003e:
        r4 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0099 }
        r5 = "Accessibility off. Did you forget to check that?";
        r4.<init>(r5);	 Catch:{ all -> 0x0099 }
        throw r4;	 Catch:{ all -> 0x0099 }
    L_0x0046:
        r3 = r2.getEventType();	 Catch:{ all -> 0x0099 }
        r4 = r7.mRelevantEventTypes;	 Catch:{ all -> 0x0099 }
        r3 = r3 & r4;
        if (r3 != 0) goto L_0x0051;
    L_0x004f:
        monitor-exit(r0);	 Catch:{ all -> 0x0099 }
        return;
    L_0x0051:
        r3 = r7.mUserId;	 Catch:{ all -> 0x0099 }
        monitor-exit(r0);	 Catch:{ all -> 0x0099 }
        r4 = android.os.Binder.clearCallingIdentity();	 Catch:{ RemoteException -> 0x006f }
        r1.sendAccessibilityEvent(r2, r3);	 Catch:{ all -> 0x0068 }
        android.os.Binder.restoreCallingIdentity(r4);	 Catch:{ RemoteException -> 0x006f }
        if (r8 == r2) goto L_0x0064;
    L_0x0061:
        r8.recycle();
    L_0x0064:
        r2.recycle();
        goto L_0x008f;
    L_0x0068:
        r0 = move-exception;
        android.os.Binder.restoreCallingIdentity(r4);	 Catch:{ RemoteException -> 0x006f }
        throw r0;	 Catch:{ RemoteException -> 0x006f }
    L_0x006d:
        r0 = move-exception;
        goto L_0x0090;
    L_0x006f:
        r0 = move-exception;
        r4 = "AccessibilityManager";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006d }
        r5.<init>();	 Catch:{ all -> 0x006d }
        r6 = "Error during sending ";
        r5.append(r6);	 Catch:{ all -> 0x006d }
        r5.append(r2);	 Catch:{ all -> 0x006d }
        r6 = " ";
        r5.append(r6);	 Catch:{ all -> 0x006d }
        r5 = r5.toString();	 Catch:{ all -> 0x006d }
        android.util.Log.e(r4, r5, r0);	 Catch:{ all -> 0x006d }
        if (r8 == r2) goto L_0x0064;
    L_0x008e:
        goto L_0x0061;
    L_0x008f:
        return;
    L_0x0090:
        if (r8 == r2) goto L_0x0095;
    L_0x0092:
        r8.recycle();
    L_0x0095:
        r2.recycle();
        throw r0;
    L_0x0099:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0099 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent):void");
    }

    /* JADX WARNING: Missing block: B:19:?, code skipped:
            r1.interrupt(r2);
     */
    /* JADX WARNING: Missing block: B:20:0x0033, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:21:0x0034, code skipped:
            android.util.Log.e(LOG_TAG, "Error while requesting interrupt from all services. ", r0);
     */
    public void interrupt() {
        /*
        r5 = this;
        r0 = r5.mLock;
        monitor-enter(r0);
        r1 = r5.getServiceLocked();	 Catch:{ all -> 0x003c }
        if (r1 != 0) goto L_0x000b;
    L_0x0009:
        monitor-exit(r0);	 Catch:{ all -> 0x003c }
        return;
    L_0x000b:
        r2 = r5.isEnabled();	 Catch:{ all -> 0x003c }
        if (r2 != 0) goto L_0x002c;
    L_0x0011:
        r2 = android.os.Looper.myLooper();	 Catch:{ all -> 0x003c }
        r3 = android.os.Looper.getMainLooper();	 Catch:{ all -> 0x003c }
        if (r2 == r3) goto L_0x0024;
    L_0x001b:
        r3 = "AccessibilityManager";
        r4 = "Interrupt called with accessibility disabled";
        android.util.Log.e(r3, r4);	 Catch:{ all -> 0x003c }
        monitor-exit(r0);	 Catch:{ all -> 0x003c }
        return;
    L_0x0024:
        r3 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x003c }
        r4 = "Accessibility off. Did you forget to check that?";
        r3.<init>(r4);	 Catch:{ all -> 0x003c }
        throw r3;	 Catch:{ all -> 0x003c }
    L_0x002c:
        r2 = r5.mUserId;	 Catch:{ all -> 0x003c }
        monitor-exit(r0);	 Catch:{ all -> 0x003c }
        r1.interrupt(r2);	 Catch:{ RemoteException -> 0x0033 }
        goto L_0x003b;
    L_0x0033:
        r0 = move-exception;
        r3 = "AccessibilityManager";
        r4 = "Error while requesting interrupt from all services. ";
        android.util.Log.e(r3, r4, r0);
    L_0x003b:
        return;
    L_0x003c:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x003c }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.interrupt():void");
    }

    @Deprecated
    public List<ServiceInfo> getAccessibilityServiceList() {
        List<AccessibilityServiceInfo> infos = getInstalledAccessibilityServiceList();
        List<ServiceInfo> services = new ArrayList();
        int infoCount = infos.size();
        for (int i = 0; i < infoCount; i++) {
            services.add(((AccessibilityServiceInfo) infos.get(i)).getResolveInfo().serviceInfo);
        }
        return Collections.unmodifiableList(services);
    }

    /* JADX WARNING: Missing block: B:10:0x0012, code skipped:
            r0 = null;
     */
    /* JADX WARNING: Missing block: B:13:0x0017, code skipped:
            r0 = r1.getInstalledAccessibilityServiceList(r2);
     */
    /* JADX WARNING: Missing block: B:14:0x0019, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:15:0x001a, code skipped:
            android.util.Log.e(LOG_TAG, "Error while obtaining the installed AccessibilityServices. ", r3);
     */
    public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getInstalledAccessibilityServiceList() {
        /*
        r6 = this;
        r0 = r6.mLock;
        monitor-enter(r0);
        r1 = r6.getServiceLocked();	 Catch:{ all -> 0x0035 }
        if (r1 != 0) goto L_0x000f;
    L_0x0009:
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x0035 }
        monitor-exit(r0);	 Catch:{ all -> 0x0035 }
        return r2;
    L_0x000f:
        r2 = r6.mUserId;	 Catch:{ all -> 0x0035 }
        monitor-exit(r0);	 Catch:{ all -> 0x0035 }
        r0 = 0;
        r3 = r1.getInstalledAccessibilityServiceList(r2);	 Catch:{ RemoteException -> 0x0019 }
        r0 = r3;
        goto L_0x0021;
    L_0x0019:
        r3 = move-exception;
        r4 = "AccessibilityManager";
        r5 = "Error while obtaining the installed AccessibilityServices. ";
        android.util.Log.e(r4, r5, r3);
    L_0x0021:
        r3 = r6.mAccessibilityPolicy;
        if (r3 == 0) goto L_0x0029;
    L_0x0025:
        r0 = r3.getInstalledAccessibilityServiceList(r0);
    L_0x0029:
        if (r0 == 0) goto L_0x0030;
    L_0x002b:
        r3 = java.util.Collections.unmodifiableList(r0);
        return r3;
    L_0x0030:
        r3 = java.util.Collections.emptyList();
        return r3;
    L_0x0035:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0035 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.getInstalledAccessibilityServiceList():java.util.List");
    }

    /* JADX WARNING: Missing block: B:10:0x0012, code skipped:
            r0 = null;
     */
    /* JADX WARNING: Missing block: B:13:0x0017, code skipped:
            r0 = r1.getEnabledAccessibilityServiceList(r7, r2);
     */
    /* JADX WARNING: Missing block: B:14:0x0019, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:15:0x001a, code skipped:
            android.util.Log.e(LOG_TAG, "Error while obtaining the installed AccessibilityServices. ", r3);
     */
    public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int r7) {
        /*
        r6 = this;
        r0 = r6.mLock;
        monitor-enter(r0);
        r1 = r6.getServiceLocked();	 Catch:{ all -> 0x0035 }
        if (r1 != 0) goto L_0x000f;
    L_0x0009:
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x0035 }
        monitor-exit(r0);	 Catch:{ all -> 0x0035 }
        return r2;
    L_0x000f:
        r2 = r6.mUserId;	 Catch:{ all -> 0x0035 }
        monitor-exit(r0);	 Catch:{ all -> 0x0035 }
        r0 = 0;
        r3 = r1.getEnabledAccessibilityServiceList(r7, r2);	 Catch:{ RemoteException -> 0x0019 }
        r0 = r3;
        goto L_0x0021;
    L_0x0019:
        r3 = move-exception;
        r4 = "AccessibilityManager";
        r5 = "Error while obtaining the installed AccessibilityServices. ";
        android.util.Log.e(r4, r5, r3);
    L_0x0021:
        r3 = r6.mAccessibilityPolicy;
        if (r3 == 0) goto L_0x0029;
    L_0x0025:
        r0 = r3.getEnabledAccessibilityServiceList(r7, r0);
    L_0x0029:
        if (r0 == 0) goto L_0x0030;
    L_0x002b:
        r3 = java.util.Collections.unmodifiableList(r0);
        return r3;
    L_0x0030:
        r3 = java.util.Collections.emptyList();
        return r3;
    L_0x0035:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0035 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.getEnabledAccessibilityServiceList(int):java.util.List");
    }

    public boolean addAccessibilityStateChangeListener(AccessibilityStateChangeListener listener) {
        addAccessibilityStateChangeListener(listener, null);
        return true;
    }

    public void addAccessibilityStateChangeListener(AccessibilityStateChangeListener listener, Handler handler) {
        synchronized (this.mLock) {
            this.mAccessibilityStateChangeListeners.put(listener, handler == null ? this.mHandler : handler);
        }
    }

    public boolean removeAccessibilityStateChangeListener(AccessibilityStateChangeListener listener) {
        boolean z;
        synchronized (this.mLock) {
            int index = this.mAccessibilityStateChangeListeners.indexOfKey(listener);
            this.mAccessibilityStateChangeListeners.remove(listener);
            z = index >= 0;
        }
        return z;
    }

    public boolean addTouchExplorationStateChangeListener(TouchExplorationStateChangeListener listener) {
        addTouchExplorationStateChangeListener(listener, null);
        return true;
    }

    public void addTouchExplorationStateChangeListener(TouchExplorationStateChangeListener listener, Handler handler) {
        synchronized (this.mLock) {
            this.mTouchExplorationStateChangeListeners.put(listener, handler == null ? this.mHandler : handler);
        }
    }

    public boolean removeTouchExplorationStateChangeListener(TouchExplorationStateChangeListener listener) {
        boolean z;
        synchronized (this.mLock) {
            int index = this.mTouchExplorationStateChangeListeners.indexOfKey(listener);
            this.mTouchExplorationStateChangeListeners.remove(listener);
            z = index >= 0;
        }
        return z;
    }

    public void addAccessibilityServicesStateChangeListener(AccessibilityServicesStateChangeListener listener, Handler handler) {
        synchronized (this.mLock) {
            this.mServicesStateChangeListeners.put(listener, handler == null ? this.mHandler : handler);
        }
    }

    public void removeAccessibilityServicesStateChangeListener(AccessibilityServicesStateChangeListener listener) {
        synchronized (this.mLock) {
            this.mServicesStateChangeListeners.remove(listener);
        }
    }

    public void addAccessibilityRequestPreparer(AccessibilityRequestPreparer preparer) {
        if (this.mRequestPreparerLists == null) {
            this.mRequestPreparerLists = new SparseArray(1);
        }
        int id = preparer.getAccessibilityViewId();
        List<AccessibilityRequestPreparer> requestPreparerList = (List) this.mRequestPreparerLists.get(id);
        if (requestPreparerList == null) {
            requestPreparerList = new ArrayList(1);
            this.mRequestPreparerLists.put(id, requestPreparerList);
        }
        requestPreparerList.add(preparer);
    }

    public void removeAccessibilityRequestPreparer(AccessibilityRequestPreparer preparer) {
        if (this.mRequestPreparerLists != null) {
            int viewId = preparer.getAccessibilityViewId();
            List<AccessibilityRequestPreparer> requestPreparerList = (List) this.mRequestPreparerLists.get(viewId);
            if (requestPreparerList != null) {
                requestPreparerList.remove(preparer);
                if (requestPreparerList.isEmpty()) {
                    this.mRequestPreparerLists.remove(viewId);
                }
            }
        }
    }

    public int getRecommendedTimeoutMillis(int originalTimeout, int uiContentFlags) {
        boolean hasIconsOrText = false;
        boolean hasControls = (uiContentFlags & 4) != 0;
        if (!((uiContentFlags & 1) == 0 && (uiContentFlags & 2) == 0)) {
            hasIconsOrText = true;
        }
        int recommendedTimeout = originalTimeout;
        if (hasControls) {
            recommendedTimeout = Math.max(recommendedTimeout, this.mInteractiveUiTimeout);
        }
        if (hasIconsOrText) {
            return Math.max(recommendedTimeout, this.mNonInteractiveUiTimeout);
        }
        return recommendedTimeout;
    }

    public List<AccessibilityRequestPreparer> getRequestPreparersForAccessibilityId(int id) {
        SparseArray sparseArray = this.mRequestPreparerLists;
        if (sparseArray == null) {
            return null;
        }
        return (List) sparseArray.get(id);
    }

    public void addHighTextContrastStateChangeListener(HighTextContrastChangeListener listener, Handler handler) {
        synchronized (this.mLock) {
            this.mHighTextContrastStateChangeListeners.put(listener, handler == null ? this.mHandler : handler);
        }
    }

    public void removeHighTextContrastStateChangeListener(HighTextContrastChangeListener listener) {
        synchronized (this.mLock) {
            this.mHighTextContrastStateChangeListeners.remove(listener);
        }
    }

    public void setAccessibilityPolicy(AccessibilityPolicy policy) {
        synchronized (this.mLock) {
            this.mAccessibilityPolicy = policy;
        }
    }

    public boolean isAccessibilityVolumeStreamActive() {
        List<AccessibilityServiceInfo> serviceInfos = getEnabledAccessibilityServiceList(-1);
        for (int i = 0; i < serviceInfos.size(); i++) {
            if ((((AccessibilityServiceInfo) serviceInfos.get(i)).flags & 128) != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean sendFingerprintGesture(int keyCode) {
        synchronized (this.mLock) {
            IAccessibilityManager service = getServiceLocked();
            if (service == null) {
                return false;
            }
            try {
                return service.sendFingerprintGesture(keyCode);
            } catch (RemoteException e) {
                return false;
            }
        }
    }

    @SystemApi
    public int getAccessibilityWindowId(IBinder windowToken) {
        int i = -1;
        if (windowToken == null) {
            return -1;
        }
        synchronized (this.mLock) {
            IAccessibilityManager service = getServiceLocked();
            if (service == null) {
                return -1;
            }
            try {
                i = service.getAccessibilityWindowId(windowToken);
                return i;
            } catch (RemoteException e) {
                return i;
            }
        }
    }

    @UnsupportedAppUsage
    private void setStateLocked(int stateFlags) {
        boolean highTextContrastEnabled = false;
        boolean enabled = (stateFlags & 1) != 0;
        boolean touchExplorationEnabled = (stateFlags & 2) != 0;
        if ((stateFlags & 4) != 0) {
            highTextContrastEnabled = true;
        }
        boolean wasEnabled = isEnabled();
        boolean wasTouchExplorationEnabled = this.mIsTouchExplorationEnabled;
        boolean wasHighTextContrastEnabled = this.mIsHighTextContrastEnabled;
        this.mIsEnabled = enabled;
        this.mIsTouchExplorationEnabled = touchExplorationEnabled;
        this.mIsHighTextContrastEnabled = highTextContrastEnabled;
        if (wasEnabled != isEnabled()) {
            notifyAccessibilityStateChanged();
        }
        if (wasTouchExplorationEnabled != touchExplorationEnabled) {
            notifyTouchExplorationStateChanged();
        }
        if (wasHighTextContrastEnabled != highTextContrastEnabled) {
            notifyHighTextContrastStateChanged();
        }
    }

    public AccessibilityServiceInfo getInstalledServiceInfoWithComponentName(ComponentName componentName) {
        List<AccessibilityServiceInfo> installedServiceInfos = getInstalledAccessibilityServiceList();
        if (installedServiceInfos == null || componentName == null) {
            return null;
        }
        for (int i = 0; i < installedServiceInfos.size(); i++) {
            if (componentName.equals(((AccessibilityServiceInfo) installedServiceInfos.get(i)).getComponentName())) {
                return (AccessibilityServiceInfo) installedServiceInfos.get(i);
            }
        }
        return null;
    }

    public int addAccessibilityInteractionConnection(IWindow windowToken, String packageName, IAccessibilityInteractionConnection connection) {
        synchronized (this.mLock) {
            IAccessibilityManager service = getServiceLocked();
            if (service == null) {
                return -1;
            }
            int userId = this.mUserId;
            try {
                return service.addAccessibilityInteractionConnection(windowToken, connection, packageName, userId);
            } catch (RemoteException re) {
                Log.e(LOG_TAG, "Error while adding an accessibility interaction connection. ", re);
                return -1;
            }
        }
    }

    /* JADX WARNING: Missing block: B:9:?, code skipped:
            r1.removeAccessibilityInteractionConnection(r5);
     */
    /* JADX WARNING: Missing block: B:10:0x0010, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:11:0x0011, code skipped:
            android.util.Log.e(LOG_TAG, "Error while removing an accessibility interaction connection. ", r0);
     */
    public void removeAccessibilityInteractionConnection(android.view.IWindow r5) {
        /*
        r4 = this;
        r0 = r4.mLock;
        monitor-enter(r0);
        r1 = r4.getServiceLocked();	 Catch:{ all -> 0x0019 }
        if (r1 != 0) goto L_0x000b;
    L_0x0009:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        return;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        r1.removeAccessibilityInteractionConnection(r5);	 Catch:{ RemoteException -> 0x0010 }
        goto L_0x0018;
    L_0x0010:
        r0 = move-exception;
        r2 = "AccessibilityManager";
        r3 = "Error while removing an accessibility interaction connection. ";
        android.util.Log.e(r2, r3, r0);
    L_0x0018:
        return;
    L_0x0019:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.removeAccessibilityInteractionConnection(android.view.IWindow):void");
    }

    /* JADX WARNING: Missing block: B:9:?, code skipped:
            r1.performAccessibilityShortcut();
     */
    /* JADX WARNING: Missing block: B:10:0x0010, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:11:0x0011, code skipped:
            android.util.Log.e(LOG_TAG, "Error performing accessibility shortcut. ", r0);
     */
    @android.annotation.SystemApi
    public void performAccessibilityShortcut() {
        /*
        r4 = this;
        r0 = r4.mLock;
        monitor-enter(r0);
        r1 = r4.getServiceLocked();	 Catch:{ all -> 0x0019 }
        if (r1 != 0) goto L_0x000b;
    L_0x0009:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        return;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        r1.performAccessibilityShortcut();	 Catch:{ RemoteException -> 0x0010 }
        goto L_0x0018;
    L_0x0010:
        r0 = move-exception;
        r2 = "AccessibilityManager";
        r3 = "Error performing accessibility shortcut. ";
        android.util.Log.e(r2, r3, r0);
    L_0x0018:
        return;
    L_0x0019:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.performAccessibilityShortcut():void");
    }

    /* JADX WARNING: Missing block: B:9:?, code skipped:
            r1.notifyAccessibilityButtonClicked(r5);
     */
    /* JADX WARNING: Missing block: B:10:0x0010, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:11:0x0011, code skipped:
            android.util.Log.e(LOG_TAG, "Error while dispatching accessibility button click", r0);
     */
    public void notifyAccessibilityButtonClicked(int r5) {
        /*
        r4 = this;
        r0 = r4.mLock;
        monitor-enter(r0);
        r1 = r4.getServiceLocked();	 Catch:{ all -> 0x0019 }
        if (r1 != 0) goto L_0x000b;
    L_0x0009:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        return;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        r1.notifyAccessibilityButtonClicked(r5);	 Catch:{ RemoteException -> 0x0010 }
        goto L_0x0018;
    L_0x0010:
        r0 = move-exception;
        r2 = "AccessibilityManager";
        r3 = "Error while dispatching accessibility button click";
        android.util.Log.e(r2, r3, r0);
    L_0x0018:
        return;
    L_0x0019:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.notifyAccessibilityButtonClicked(int):void");
    }

    /* JADX WARNING: Missing block: B:9:?, code skipped:
            r1.notifyAccessibilityButtonVisibilityChanged(r5);
     */
    /* JADX WARNING: Missing block: B:10:0x0010, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:11:0x0011, code skipped:
            android.util.Log.e(LOG_TAG, "Error while dispatching accessibility button visibility change", r0);
     */
    public void notifyAccessibilityButtonVisibilityChanged(boolean r5) {
        /*
        r4 = this;
        r0 = r4.mLock;
        monitor-enter(r0);
        r1 = r4.getServiceLocked();	 Catch:{ all -> 0x0019 }
        if (r1 != 0) goto L_0x000b;
    L_0x0009:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        return;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        r1.notifyAccessibilityButtonVisibilityChanged(r5);	 Catch:{ RemoteException -> 0x0010 }
        goto L_0x0018;
    L_0x0010:
        r0 = move-exception;
        r2 = "AccessibilityManager";
        r3 = "Error while dispatching accessibility button visibility change";
        android.util.Log.e(r2, r3, r0);
    L_0x0018:
        return;
    L_0x0019:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.notifyAccessibilityButtonVisibilityChanged(boolean):void");
    }

    /* JADX WARNING: Missing block: B:9:?, code skipped:
            r1.setPictureInPictureActionReplacingConnection(r5);
     */
    /* JADX WARNING: Missing block: B:10:0x0010, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:11:0x0011, code skipped:
            android.util.Log.e(LOG_TAG, "Error setting picture in picture action replacement", r0);
     */
    public void setPictureInPictureActionReplacingConnection(android.view.accessibility.IAccessibilityInteractionConnection r5) {
        /*
        r4 = this;
        r0 = r4.mLock;
        monitor-enter(r0);
        r1 = r4.getServiceLocked();	 Catch:{ all -> 0x0019 }
        if (r1 != 0) goto L_0x000b;
    L_0x0009:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        return;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        r1.setPictureInPictureActionReplacingConnection(r5);	 Catch:{ RemoteException -> 0x0010 }
        goto L_0x0018;
    L_0x0010:
        r0 = move-exception;
        r2 = "AccessibilityManager";
        r3 = "Error setting picture in picture action replacement";
        android.util.Log.e(r2, r3, r0);
    L_0x0018:
        return;
    L_0x0019:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.setPictureInPictureActionReplacingConnection(android.view.accessibility.IAccessibilityInteractionConnection):void");
    }

    public String getAccessibilityShortcutService() {
        IAccessibilityManager service;
        synchronized (this.mLock) {
            service = getServiceLocked();
        }
        if (service != null) {
            try {
                return service.getAccessibilityShortcutService();
            } catch (RemoteException re) {
                re.rethrowFromSystemServer();
            }
        }
        return null;
    }

    private IAccessibilityManager getServiceLocked() {
        if (this.mService == null) {
            tryConnectToServiceLocked(null);
        }
        return this.mService;
    }

    private void tryConnectToServiceLocked(IAccessibilityManager service) {
        if (service == null) {
            IBinder iBinder = ServiceManager.getService(Context.ACCESSIBILITY_SERVICE);
            if (iBinder != null) {
                service = IAccessibilityManager.Stub.asInterface(iBinder);
            } else {
                return;
            }
        }
        try {
            long userStateAndRelevantEvents = service.addClient(this.mClient, this.mUserId);
            setStateLocked(IntPair.first(userStateAndRelevantEvents));
            this.mRelevantEventTypes = IntPair.second(userStateAndRelevantEvents);
            updateUiTimeout(service.getRecommendedTimeoutMillis());
            this.mService = service;
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "AccessibilityManagerService is dead", re);
        }
    }

    /* JADX WARNING: Missing block: B:9:0x0019, code skipped:
            r0 = r2.size();
            r3 = 0;
     */
    /* JADX WARNING: Missing block: B:10:0x001e, code skipped:
            if (r3 >= r0) goto L_0x0037;
     */
    /* JADX WARNING: Missing block: B:11:0x0020, code skipped:
            ((android.os.Handler) r2.valueAt(r3)).post(new android.view.accessibility.-$$Lambda$AccessibilityManager$yzw5NYY7_MfAQ9gLy3mVllchaXo((android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener) r2.keyAt(r3), r1));
            r3 = r3 + 1;
     */
    /* JADX WARNING: Missing block: B:12:0x0037, code skipped:
            return;
     */
    private void notifyAccessibilityStateChanged() {
        /*
        r7 = this;
        r0 = r7.mLock;
        monitor-enter(r0);
        r1 = r7.mAccessibilityStateChangeListeners;	 Catch:{ all -> 0x0038 }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x0038 }
        if (r1 == 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0038 }
        return;
    L_0x000d:
        r1 = r7.isEnabled();	 Catch:{ all -> 0x0038 }
        r2 = new android.util.ArrayMap;	 Catch:{ all -> 0x0038 }
        r3 = r7.mAccessibilityStateChangeListeners;	 Catch:{ all -> 0x0038 }
        r2.<init>(r3);	 Catch:{ all -> 0x0038 }
        monitor-exit(r0);	 Catch:{ all -> 0x0038 }
        r0 = r2.size();
        r3 = 0;
    L_0x001e:
        if (r3 >= r0) goto L_0x0037;
    L_0x0020:
        r4 = r2.keyAt(r3);
        r4 = (android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener) r4;
        r5 = r2.valueAt(r3);
        r5 = (android.os.Handler) r5;
        r6 = new android.view.accessibility.-$$Lambda$AccessibilityManager$yzw5NYY7_MfAQ9gLy3mVllchaXo;
        r6.<init>(r4, r1);
        r5.post(r6);
        r3 = r3 + 1;
        goto L_0x001e;
    L_0x0037:
        return;
    L_0x0038:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0038 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.notifyAccessibilityStateChanged():void");
    }

    /* JADX WARNING: Missing block: B:9:0x0017, code skipped:
            r0 = r2.size();
            r3 = 0;
     */
    /* JADX WARNING: Missing block: B:10:0x001c, code skipped:
            if (r3 >= r0) goto L_0x0035;
     */
    /* JADX WARNING: Missing block: B:11:0x001e, code skipped:
            ((android.os.Handler) r2.valueAt(r3)).post(new android.view.accessibility.-$$Lambda$AccessibilityManager$a0OtrjOl35tiW2vwyvAmY6_LiLI((android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener) r2.keyAt(r3), r1));
            r3 = r3 + 1;
     */
    /* JADX WARNING: Missing block: B:12:0x0035, code skipped:
            return;
     */
    private void notifyTouchExplorationStateChanged() {
        /*
        r7 = this;
        r0 = r7.mLock;
        monitor-enter(r0);
        r1 = r7.mTouchExplorationStateChangeListeners;	 Catch:{ all -> 0x0036 }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x0036 }
        if (r1 == 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0036 }
        return;
    L_0x000d:
        r1 = r7.mIsTouchExplorationEnabled;	 Catch:{ all -> 0x0036 }
        r2 = new android.util.ArrayMap;	 Catch:{ all -> 0x0036 }
        r3 = r7.mTouchExplorationStateChangeListeners;	 Catch:{ all -> 0x0036 }
        r2.<init>(r3);	 Catch:{ all -> 0x0036 }
        monitor-exit(r0);	 Catch:{ all -> 0x0036 }
        r0 = r2.size();
        r3 = 0;
    L_0x001c:
        if (r3 >= r0) goto L_0x0035;
    L_0x001e:
        r4 = r2.keyAt(r3);
        r4 = (android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener) r4;
        r5 = r2.valueAt(r3);
        r5 = (android.os.Handler) r5;
        r6 = new android.view.accessibility.-$$Lambda$AccessibilityManager$a0OtrjOl35tiW2vwyvAmY6_LiLI;
        r6.<init>(r4, r1);
        r5.post(r6);
        r3 = r3 + 1;
        goto L_0x001c;
    L_0x0035:
        return;
    L_0x0036:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0036 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.notifyTouchExplorationStateChanged():void");
    }

    /* JADX WARNING: Missing block: B:9:0x0017, code skipped:
            r0 = r2.size();
            r3 = 0;
     */
    /* JADX WARNING: Missing block: B:10:0x001c, code skipped:
            if (r3 >= r0) goto L_0x0035;
     */
    /* JADX WARNING: Missing block: B:11:0x001e, code skipped:
            ((android.os.Handler) r2.valueAt(r3)).post(new android.view.accessibility.-$$Lambda$AccessibilityManager$4M6GrmFiqsRwVzn352N10DcU6RM((android.view.accessibility.AccessibilityManager.HighTextContrastChangeListener) r2.keyAt(r3), r1));
            r3 = r3 + 1;
     */
    /* JADX WARNING: Missing block: B:12:0x0035, code skipped:
            return;
     */
    private void notifyHighTextContrastStateChanged() {
        /*
        r7 = this;
        r0 = r7.mLock;
        monitor-enter(r0);
        r1 = r7.mHighTextContrastStateChangeListeners;	 Catch:{ all -> 0x0036 }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x0036 }
        if (r1 == 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0036 }
        return;
    L_0x000d:
        r1 = r7.mIsHighTextContrastEnabled;	 Catch:{ all -> 0x0036 }
        r2 = new android.util.ArrayMap;	 Catch:{ all -> 0x0036 }
        r3 = r7.mHighTextContrastStateChangeListeners;	 Catch:{ all -> 0x0036 }
        r2.<init>(r3);	 Catch:{ all -> 0x0036 }
        monitor-exit(r0);	 Catch:{ all -> 0x0036 }
        r0 = r2.size();
        r3 = 0;
    L_0x001c:
        if (r3 >= r0) goto L_0x0035;
    L_0x001e:
        r4 = r2.keyAt(r3);
        r4 = (android.view.accessibility.AccessibilityManager.HighTextContrastChangeListener) r4;
        r5 = r2.valueAt(r3);
        r5 = (android.os.Handler) r5;
        r6 = new android.view.accessibility.-$$Lambda$AccessibilityManager$4M6GrmFiqsRwVzn352N10DcU6RM;
        r6.<init>(r4, r1);
        r5.post(r6);
        r3 = r3 + 1;
        goto L_0x001c;
    L_0x0035:
        return;
    L_0x0036:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0036 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.notifyHighTextContrastStateChanged():void");
    }

    private void updateUiTimeout(long uiTimeout) {
        this.mInteractiveUiTimeout = IntPair.first(uiTimeout);
        this.mNonInteractiveUiTimeout = IntPair.second(uiTimeout);
    }

    public static boolean isAccessibilityButtonSupported() {
        return Resources.getSystem().getBoolean(R.bool.config_showNavigationBar);
    }
}
