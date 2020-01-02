package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.BroadcastReceiver;
import android.content.BroadcastReceiver.PendingResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.IIntentReceiver.Stub;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageParser;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.dex.ArtManager;
import android.content.pm.split.SplitDependencyLoader;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Resources;
import android.miui.ResourcesManager;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Process;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.view.DisplayAdjustments;
import com.android.internal.util.ArrayUtils;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;

public final class LoadedApk {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final boolean DEBUG = false;
    private static final String PROPERTY_NAME_APPEND_NATIVE = "pi.append_native_lib_paths";
    static final String TAG = "LoadedApk";
    @UnsupportedAppUsage
    private final ActivityThread mActivityThread;
    private AppComponentFactory mAppComponentFactory;
    @UnsupportedAppUsage
    private String mAppDir;
    @UnsupportedAppUsage
    private Application mApplication;
    @UnsupportedAppUsage
    private ApplicationInfo mApplicationInfo;
    @UnsupportedAppUsage
    private final ClassLoader mBaseClassLoader;
    @UnsupportedAppUsage
    private ClassLoader mClassLoader;
    private File mCredentialProtectedDataDirFile;
    @UnsupportedAppUsage
    private String mDataDir;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private File mDataDirFile;
    private ClassLoader mDefaultClassLoader;
    private File mDeviceProtectedDataDirFile;
    @UnsupportedAppUsage
    private final DisplayAdjustments mDisplayAdjustments = new DisplayAdjustments();
    private final boolean mIncludeCode;
    @UnsupportedAppUsage
    private String mLibDir;
    private String[] mOverlayDirs;
    @UnsupportedAppUsage
    final String mPackageName;
    @UnsupportedAppUsage
    private final ArrayMap<Context, ArrayMap<BroadcastReceiver, ReceiverDispatcher>> mReceivers = new ArrayMap();
    private final boolean mRegisterPackage;
    @UnsupportedAppUsage
    private String mResDir;
    @UnsupportedAppUsage
    Resources mResources;
    private final boolean mSecurityViolation;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final ArrayMap<Context, ArrayMap<ServiceConnection, ServiceDispatcher>> mServices = new ArrayMap();
    private String[] mSplitAppDirs;
    private String[] mSplitClassLoaderNames;
    private SplitDependencyLoaderImpl mSplitLoader;
    private String[] mSplitNames;
    @UnsupportedAppUsage
    private String[] mSplitResDirs;
    private final ArrayMap<Context, ArrayMap<ServiceConnection, ServiceDispatcher>> mUnboundServices = new ArrayMap();
    private final ArrayMap<Context, ArrayMap<BroadcastReceiver, ReceiverDispatcher>> mUnregisteredReceivers = new ArrayMap();

    static final class ReceiverDispatcher {
        final Handler mActivityThread;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        final Context mContext;
        boolean mForgotten;
        final Stub mIIntentReceiver;
        final Instrumentation mInstrumentation;
        final IntentReceiverLeaked mLocation;
        @UnsupportedAppUsage
        final BroadcastReceiver mReceiver;
        final boolean mRegistered;
        RuntimeException mUnregisterLocation;

        final class Args extends PendingResult {
            private Intent mCurIntent;
            private boolean mDispatched;
            private final boolean mOrdered;
            private boolean mRunCalled;
            final /* synthetic */ ReceiverDispatcher this$0;

            public Args(ReceiverDispatcher this$0, Intent intent, int resultCode, String resultData, Bundle resultExtras, boolean ordered, boolean sticky, int sendingUser) {
                ReceiverDispatcher receiverDispatcher = this$0;
                this.this$0 = receiverDispatcher;
                super(resultCode, resultData, resultExtras, receiverDispatcher.mRegistered ? 1 : 2, ordered, sticky, receiverDispatcher.mIIntentReceiver.asBinder(), sendingUser, intent.getFlags());
                this.mCurIntent = intent;
                this.mOrdered = ordered;
            }

            public final Runnable getRunnable() {
                return new -$$Lambda$LoadedApk$ReceiverDispatcher$Args$_BumDX2UKsnxLVrE6UJsJZkotuA(this);
            }

            public /* synthetic */ void lambda$getRunnable$0$LoadedApk$ReceiverDispatcher$Args() {
                BroadcastReceiver receiver = this.this$0.mReceiver;
                boolean ordered = this.mOrdered;
                IActivityManager mgr = ActivityManager.getService();
                Intent intent = this.mCurIntent;
                if (intent == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Null intent being dispatched, mDispatched=");
                    stringBuilder.append(this.mDispatched);
                    stringBuilder.append(this.mRunCalled ? ", run() has already been called" : "");
                    Log.wtf(LoadedApk.TAG, stringBuilder.toString());
                }
                this.mCurIntent = null;
                this.mDispatched = true;
                this.mRunCalled = true;
                if (receiver == null || intent == null || this.this$0.mForgotten) {
                    if (this.this$0.mRegistered && ordered) {
                        sendFinished(mgr);
                    }
                    return;
                }
                Trace.traceBegin(64, "broadcastReceiveReg");
                long startTime = SystemClock.uptimeMillis();
                try {
                    ClassLoader cl = this.this$0.mReceiver.getClass().getClassLoader();
                    intent.setExtrasClassLoader(cl);
                    intent.prepareToEnterProcess();
                    setExtrasClassLoader(cl);
                    receiver.setPendingResult(this);
                    receiver.onReceive(this.this$0.mContext, intent);
                } catch (Exception e) {
                    if (this.this$0.mRegistered && ordered) {
                        sendFinished(mgr);
                    }
                    if (this.this$0.mInstrumentation == null || !this.this$0.mInstrumentation.onException(this.this$0.mReceiver, e)) {
                        Trace.traceEnd(64);
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Error receiving broadcast ");
                        stringBuilder2.append(intent);
                        stringBuilder2.append(" in ");
                        stringBuilder2.append(this.this$0.mReceiver);
                        throw new RuntimeException(stringBuilder2.toString(), e);
                    }
                }
                if (receiver.getPendingResult() != null) {
                    finish();
                }
                ActivityThreadInjector.checkHandleMessageTime(startTime, 200);
                Trace.traceEnd(64);
            }
        }

        static final class InnerReceiver extends Stub {
            final WeakReference<ReceiverDispatcher> mDispatcher;
            final ReceiverDispatcher mStrongRef;

            InnerReceiver(ReceiverDispatcher rd, boolean strong) {
                this.mDispatcher = new WeakReference(rd);
                this.mStrongRef = strong ? rd : null;
            }

            public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
                ReceiverDispatcher rd;
                Bundle bundle = extras;
                if (intent == null) {
                    Log.wtf(LoadedApk.TAG, "Null intent received");
                    rd = null;
                } else {
                    rd = (ReceiverDispatcher) this.mDispatcher.get();
                }
                if (rd != null) {
                    rd.performReceive(intent, resultCode, data, extras, ordered, sticky, sendingUser);
                    return;
                }
                IActivityManager mgr = ActivityManager.getService();
                if (bundle != null) {
                    try {
                        bundle.setAllowFds(false);
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
                mgr.finishReceiver(this, resultCode, data, extras, false, intent.getFlags());
            }
        }

        ReceiverDispatcher(BroadcastReceiver receiver, Context context, Handler activityThread, Instrumentation instrumentation, boolean registered) {
            if (activityThread != null) {
                this.mIIntentReceiver = new InnerReceiver(this, registered ^ 1);
                this.mReceiver = receiver;
                this.mContext = context;
                this.mActivityThread = activityThread;
                this.mInstrumentation = instrumentation;
                this.mRegistered = registered;
                this.mLocation = new IntentReceiverLeaked(null);
                this.mLocation.fillInStackTrace();
                return;
            }
            throw new NullPointerException("Handler must not be null");
        }

        /* Access modifiers changed, original: 0000 */
        public void validate(Context context, Handler activityThread) {
            String str = ")";
            String str2 = " now ";
            String str3 = "Receiver ";
            StringBuilder stringBuilder;
            if (this.mContext != context) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str3);
                stringBuilder.append(this.mReceiver);
                stringBuilder.append(" registered with differing Context (was ");
                stringBuilder.append(this.mContext);
                stringBuilder.append(str2);
                stringBuilder.append(context);
                stringBuilder.append(str);
                throw new IllegalStateException(stringBuilder.toString());
            } else if (this.mActivityThread != activityThread) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str3);
                stringBuilder.append(this.mReceiver);
                stringBuilder.append(" registered with differing handler (was ");
                stringBuilder.append(this.mActivityThread);
                stringBuilder.append(str2);
                stringBuilder.append(activityThread);
                stringBuilder.append(str);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }

        /* Access modifiers changed, original: 0000 */
        public IntentReceiverLeaked getLocation() {
            return this.mLocation;
        }

        /* Access modifiers changed, original: 0000 */
        @UnsupportedAppUsage
        public BroadcastReceiver getIntentReceiver() {
            return this.mReceiver;
        }

        /* Access modifiers changed, original: 0000 */
        @UnsupportedAppUsage
        public IIntentReceiver getIIntentReceiver() {
            return this.mIIntentReceiver;
        }

        /* Access modifiers changed, original: 0000 */
        public void setUnregisterLocation(RuntimeException ex) {
            this.mUnregisterLocation = ex;
        }

        /* Access modifiers changed, original: 0000 */
        public RuntimeException getUnregisterLocation() {
            return this.mUnregisterLocation;
        }

        public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
            Args args = new Args(this, intent, resultCode, data, extras, ordered, sticky, sendingUser);
            if (intent == null) {
                Log.wtf(LoadedApk.TAG, "Null intent received");
            }
            if ((intent == null || !this.mActivityThread.post(args.getRunnable())) && this.mRegistered && ordered) {
                args.sendFinished(ActivityManager.getService());
            }
        }
    }

    static final class ServiceDispatcher {
        private final ArrayMap<ComponentName, ConnectionInfo> mActiveConnections = new ArrayMap();
        private final Executor mActivityExecutor;
        private final Handler mActivityThread;
        @UnsupportedAppUsage
        private final ServiceConnection mConnection;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        private final Context mContext;
        private final int mFlags;
        private boolean mForgotten;
        private final InnerConnection mIServiceConnection = new InnerConnection(this);
        private final ServiceConnectionLeaked mLocation;
        private RuntimeException mUnbindLocation;

        private static class ConnectionInfo {
            IBinder binder;
            DeathRecipient deathMonitor;

            private ConnectionInfo() {
            }
        }

        private final class DeathMonitor implements DeathRecipient {
            final ComponentName mName;
            final IBinder mService;

            DeathMonitor(ComponentName name, IBinder service) {
                this.mName = name;
                this.mService = service;
            }

            public void binderDied() {
                ServiceDispatcher.this.death(this.mName, this.mService);
            }
        }

        private static class InnerConnection extends IServiceConnection.Stub {
            @UnsupportedAppUsage
            final WeakReference<ServiceDispatcher> mDispatcher;

            InnerConnection(ServiceDispatcher sd) {
                this.mDispatcher = new WeakReference(sd);
            }

            public void connected(ComponentName name, IBinder service, boolean dead) throws RemoteException {
                ServiceDispatcher sd = (ServiceDispatcher) this.mDispatcher.get();
                if (sd != null) {
                    sd.connected(name, service, dead);
                }
            }
        }

        private final class RunConnection implements Runnable {
            final int mCommand;
            final boolean mDead;
            final ComponentName mName;
            final IBinder mService;

            RunConnection(ComponentName name, IBinder service, int command, boolean dead) {
                this.mName = name;
                this.mService = service;
                this.mCommand = command;
                this.mDead = dead;
            }

            public void run() {
                int i = this.mCommand;
                if (i == 0) {
                    ServiceDispatcher.this.doConnected(this.mName, this.mService, this.mDead);
                } else if (i == 1) {
                    ServiceDispatcher.this.doDeath(this.mName, this.mService);
                }
            }
        }

        @UnsupportedAppUsage
        ServiceDispatcher(ServiceConnection conn, Context context, Handler activityThread, int flags) {
            this.mConnection = conn;
            this.mContext = context;
            this.mActivityThread = activityThread;
            this.mActivityExecutor = null;
            this.mLocation = new ServiceConnectionLeaked(null);
            this.mLocation.fillInStackTrace();
            this.mFlags = flags;
        }

        ServiceDispatcher(ServiceConnection conn, Context context, Executor activityExecutor, int flags) {
            this.mConnection = conn;
            this.mContext = context;
            this.mActivityThread = null;
            this.mActivityExecutor = activityExecutor;
            this.mLocation = new ServiceConnectionLeaked(null);
            this.mLocation.fillInStackTrace();
            this.mFlags = flags;
        }

        /* Access modifiers changed, original: 0000 */
        public void validate(Context context, Handler activityThread, Executor activityExecutor) {
            String str = ")";
            String str2 = " now ";
            String str3 = "ServiceConnection ";
            StringBuilder stringBuilder;
            if (this.mContext != context) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str3);
                stringBuilder.append(this.mConnection);
                stringBuilder.append(" registered with differing Context (was ");
                stringBuilder.append(this.mContext);
                stringBuilder.append(str2);
                stringBuilder.append(context);
                stringBuilder.append(str);
                throw new RuntimeException(stringBuilder.toString());
            } else if (this.mActivityThread != activityThread) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str3);
                stringBuilder.append(this.mConnection);
                stringBuilder.append(" registered with differing handler (was ");
                stringBuilder.append(this.mActivityThread);
                stringBuilder.append(str2);
                stringBuilder.append(activityThread);
                stringBuilder.append(str);
                throw new RuntimeException(stringBuilder.toString());
            } else if (this.mActivityExecutor != activityExecutor) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str3);
                stringBuilder.append(this.mConnection);
                stringBuilder.append(" registered with differing executor (was ");
                stringBuilder.append(this.mActivityExecutor);
                stringBuilder.append(str2);
                stringBuilder.append(activityExecutor);
                stringBuilder.append(str);
                throw new RuntimeException(stringBuilder.toString());
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void doForget() {
            synchronized (this) {
                for (int i = 0; i < this.mActiveConnections.size(); i++) {
                    ConnectionInfo ci = (ConnectionInfo) this.mActiveConnections.valueAt(i);
                    ci.binder.unlinkToDeath(ci.deathMonitor, 0);
                }
                this.mActiveConnections.clear();
                this.mForgotten = true;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public ServiceConnectionLeaked getLocation() {
            return this.mLocation;
        }

        /* Access modifiers changed, original: 0000 */
        public ServiceConnection getServiceConnection() {
            return this.mConnection;
        }

        /* Access modifiers changed, original: 0000 */
        @UnsupportedAppUsage
        public IServiceConnection getIServiceConnection() {
            return this.mIServiceConnection;
        }

        /* Access modifiers changed, original: 0000 */
        public int getFlags() {
            return this.mFlags;
        }

        /* Access modifiers changed, original: 0000 */
        public void setUnbindLocation(RuntimeException ex) {
            this.mUnbindLocation = ex;
        }

        /* Access modifiers changed, original: 0000 */
        public RuntimeException getUnbindLocation() {
            return this.mUnbindLocation;
        }

        public void connected(ComponentName name, IBinder service, boolean dead) {
            Executor executor = this.mActivityExecutor;
            if (executor != null) {
                executor.execute(new RunConnection(name, service, 0, dead));
                return;
            }
            Handler handler = this.mActivityThread;
            if (handler != null) {
                handler.post(new RunConnection(name, service, 0, dead));
            } else {
                doConnected(name, service, dead);
            }
        }

        public void death(ComponentName name, IBinder service) {
            Executor executor = this.mActivityExecutor;
            if (executor != null) {
                executor.execute(new RunConnection(name, service, 1, false));
                return;
            }
            Handler handler = this.mActivityThread;
            if (handler != null) {
                handler.post(new RunConnection(name, service, 1, false));
            } else {
                doDeath(name, service);
            }
        }

        /* JADX WARNING: Missing block: B:26:0x004b, code skipped:
            if (r0 == null) goto L_0x0052;
     */
        /* JADX WARNING: Missing block: B:27:0x004d, code skipped:
            r4.mConnection.onServiceDisconnected(r5);
     */
        /* JADX WARNING: Missing block: B:28:0x0052, code skipped:
            if (r7 == false) goto L_0x0059;
     */
        /* JADX WARNING: Missing block: B:29:0x0054, code skipped:
            r4.mConnection.onBindingDied(r5);
     */
        /* JADX WARNING: Missing block: B:30:0x0059, code skipped:
            if (r6 == null) goto L_0x0061;
     */
        /* JADX WARNING: Missing block: B:31:0x005b, code skipped:
            r4.mConnection.onServiceConnected(r5, r6);
     */
        /* JADX WARNING: Missing block: B:32:0x0061, code skipped:
            r4.mConnection.onNullBinding(r5);
     */
        /* JADX WARNING: Missing block: B:33:0x0066, code skipped:
            return;
     */
        public void doConnected(android.content.ComponentName r5, android.os.IBinder r6, boolean r7) {
            /*
            r4 = this;
            monitor-enter(r4);
            r0 = r4.mForgotten;	 Catch:{ all -> 0x0067 }
            if (r0 == 0) goto L_0x0007;
        L_0x0005:
            monitor-exit(r4);	 Catch:{ all -> 0x0067 }
            return;
        L_0x0007:
            r0 = r4.mActiveConnections;	 Catch:{ all -> 0x0067 }
            r0 = r0.get(r5);	 Catch:{ all -> 0x0067 }
            r0 = (android.app.LoadedApk.ServiceDispatcher.ConnectionInfo) r0;	 Catch:{ all -> 0x0067 }
            if (r0 == 0) goto L_0x0017;
        L_0x0011:
            r1 = r0.binder;	 Catch:{ all -> 0x0067 }
            if (r1 != r6) goto L_0x0017;
        L_0x0015:
            monitor-exit(r4);	 Catch:{ all -> 0x0067 }
            return;
        L_0x0017:
            r1 = 0;
            if (r6 == 0) goto L_0x003c;
        L_0x001a:
            r2 = new android.app.LoadedApk$ServiceDispatcher$ConnectionInfo;	 Catch:{ all -> 0x0067 }
            r3 = 0;
            r2.<init>();	 Catch:{ all -> 0x0067 }
            r2.binder = r6;	 Catch:{ all -> 0x0067 }
            r3 = new android.app.LoadedApk$ServiceDispatcher$DeathMonitor;	 Catch:{ all -> 0x0067 }
            r3.<init>(r5, r6);	 Catch:{ all -> 0x0067 }
            r2.deathMonitor = r3;	 Catch:{ all -> 0x0067 }
            r3 = r2.deathMonitor;	 Catch:{ RemoteException -> 0x0034 }
            r6.linkToDeath(r3, r1);	 Catch:{ RemoteException -> 0x0034 }
            r3 = r4.mActiveConnections;	 Catch:{ RemoteException -> 0x0034 }
            r3.put(r5, r2);	 Catch:{ RemoteException -> 0x0034 }
            goto L_0x0041;
        L_0x0034:
            r1 = move-exception;
            r3 = r4.mActiveConnections;	 Catch:{ all -> 0x0067 }
            r3.remove(r5);	 Catch:{ all -> 0x0067 }
            monitor-exit(r4);	 Catch:{ all -> 0x0067 }
            return;
        L_0x003c:
            r2 = r4.mActiveConnections;	 Catch:{ all -> 0x0067 }
            r2.remove(r5);	 Catch:{ all -> 0x0067 }
        L_0x0041:
            if (r0 == 0) goto L_0x004a;
        L_0x0043:
            r2 = r0.binder;	 Catch:{ all -> 0x0067 }
            r3 = r0.deathMonitor;	 Catch:{ all -> 0x0067 }
            r2.unlinkToDeath(r3, r1);	 Catch:{ all -> 0x0067 }
        L_0x004a:
            monitor-exit(r4);	 Catch:{ all -> 0x0067 }
            if (r0 == 0) goto L_0x0052;
        L_0x004d:
            r1 = r4.mConnection;
            r1.onServiceDisconnected(r5);
        L_0x0052:
            if (r7 == 0) goto L_0x0059;
        L_0x0054:
            r1 = r4.mConnection;
            r1.onBindingDied(r5);
        L_0x0059:
            if (r6 == 0) goto L_0x0061;
        L_0x005b:
            r1 = r4.mConnection;
            r1.onServiceConnected(r5, r6);
            goto L_0x0066;
        L_0x0061:
            r1 = r4.mConnection;
            r1.onNullBinding(r5);
        L_0x0066:
            return;
        L_0x0067:
            r0 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0067 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.LoadedApk$ServiceDispatcher.doConnected(android.content.ComponentName, android.os.IBinder, boolean):void");
        }

        public void doDeath(ComponentName name, IBinder service) {
            synchronized (this) {
                ConnectionInfo old = (ConnectionInfo) this.mActiveConnections.get(name);
                if (old != null) {
                    if (old.binder == service) {
                        this.mActiveConnections.remove(name);
                        old.binder.unlinkToDeath(old.deathMonitor, 0);
                        this.mConnection.onServiceDisconnected(name);
                        return;
                    }
                }
            }
        }
    }

    private class SplitDependencyLoaderImpl extends SplitDependencyLoader<NameNotFoundException> {
        private final ClassLoader[] mCachedClassLoaders;
        private final String[][] mCachedResourcePaths;

        SplitDependencyLoaderImpl(SparseArray<int[]> dependencies) {
            super(dependencies);
            this.mCachedResourcePaths = new String[(LoadedApk.this.mSplitNames.length + 1)][];
            this.mCachedClassLoaders = new ClassLoader[(LoadedApk.this.mSplitNames.length + 1)];
        }

        /* Access modifiers changed, original: protected */
        public boolean isSplitCached(int splitIdx) {
            return this.mCachedClassLoaders[splitIdx] != null;
        }

        /* Access modifiers changed, original: protected */
        public void constructSplit(int splitIdx, int[] configSplitIndices, int parentSplitIdx) throws NameNotFoundException {
            ArrayList<String> splitPaths = new ArrayList();
            int i = 0;
            int length;
            if (splitIdx == 0) {
                LoadedApk.this.createOrUpdateClassLoaderLocked(null);
                this.mCachedClassLoaders[0] = LoadedApk.this.mClassLoader;
                for (int configSplitIdx : configSplitIndices) {
                    splitPaths.add(LoadedApk.this.mSplitResDirs[configSplitIdx - 1]);
                }
                this.mCachedResourcePaths[0] = (String[]) splitPaths.toArray(new String[splitPaths.size()]);
                return;
            }
            ClassLoader[] classLoaderArr = this.mCachedClassLoaders;
            classLoaderArr[splitIdx] = ApplicationLoaders.getDefault().getClassLoader(LoadedApk.this.mSplitAppDirs[splitIdx - 1], LoadedApk.this.getTargetSdkVersion(), false, null, null, classLoaderArr[parentSplitIdx], LoadedApk.this.mSplitClassLoaderNames[splitIdx - 1]);
            Collections.addAll(splitPaths, this.mCachedResourcePaths[parentSplitIdx]);
            splitPaths.add(LoadedApk.this.mSplitResDirs[splitIdx - 1]);
            length = configSplitIndices.length;
            while (i < length) {
                splitPaths.add(LoadedApk.this.mSplitResDirs[configSplitIndices[i] - 1]);
                i++;
            }
            this.mCachedResourcePaths[splitIdx] = (String[]) splitPaths.toArray(new String[splitPaths.size()]);
        }

        private int ensureSplitLoaded(String splitName) throws NameNotFoundException {
            int idx = 0;
            if (splitName != null) {
                idx = Arrays.binarySearch(LoadedApk.this.mSplitNames, splitName);
                if (idx >= 0) {
                    idx++;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Split name '");
                    stringBuilder.append(splitName);
                    stringBuilder.append("' is not installed");
                    throw new NameNotFoundException(stringBuilder.toString());
                }
            }
            loadDependenciesForSplit(idx);
            return idx;
        }

        /* Access modifiers changed, original: 0000 */
        public ClassLoader getClassLoaderForSplit(String splitName) throws NameNotFoundException {
            return this.mCachedClassLoaders[ensureSplitLoaded(splitName)];
        }

        /* Access modifiers changed, original: 0000 */
        public String[] getSplitPathsForSplit(String splitName) throws NameNotFoundException {
            return this.mCachedResourcePaths[ensureSplitLoaded(splitName)];
        }
    }

    private static class WarningContextClassLoader extends ClassLoader {
        private static boolean warned = false;

        private WarningContextClassLoader() {
        }

        private void warn(String methodName) {
            if (!warned) {
                warned = true;
                Thread.currentThread().setContextClassLoader(getParent());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ClassLoader.");
                stringBuilder.append(methodName);
                stringBuilder.append(": The class loader returned by Thread.getContextClassLoader() may fail for processes that host multiple applications. You should explicitly specify a context class loader. For example: Thread.setContextClassLoader(getClass().getClassLoader());");
                Slog.w(ActivityThread.TAG, stringBuilder.toString());
            }
        }

        public URL getResource(String resName) {
            warn("getResource");
            return getParent().getResource(resName);
        }

        public Enumeration<URL> getResources(String resName) throws IOException {
            warn("getResources");
            return getParent().getResources(resName);
        }

        public InputStream getResourceAsStream(String resName) {
            warn("getResourceAsStream");
            return getParent().getResourceAsStream(resName);
        }

        public Class<?> loadClass(String className) throws ClassNotFoundException {
            warn("loadClass");
            return getParent().loadClass(className);
        }

        public void setClassAssertionStatus(String cname, boolean enable) {
            warn("setClassAssertionStatus");
            getParent().setClassAssertionStatus(cname, enable);
        }

        public void setPackageAssertionStatus(String pname, boolean enable) {
            warn("setPackageAssertionStatus");
            getParent().setPackageAssertionStatus(pname, enable);
        }

        public void setDefaultAssertionStatus(boolean enable) {
            warn("setDefaultAssertionStatus");
            getParent().setDefaultAssertionStatus(enable);
        }

        public void clearAssertionStatus() {
            warn("clearAssertionStatus");
            getParent().clearAssertionStatus();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Application getApplication() {
        return this.mApplication;
    }

    public LoadedApk(ActivityThread activityThread, ApplicationInfo aInfo, CompatibilityInfo compatInfo, ClassLoader baseLoader, boolean securityViolation, boolean includeCode, boolean registerPackage) {
        this.mActivityThread = activityThread;
        setApplicationInfo(aInfo);
        this.mPackageName = aInfo.packageName;
        this.mBaseClassLoader = baseLoader;
        this.mSecurityViolation = securityViolation;
        this.mIncludeCode = includeCode;
        this.mRegisterPackage = registerPackage;
        this.mDisplayAdjustments.setCompatibilityInfo(compatInfo);
        this.mAppComponentFactory = createAppFactory(this.mApplicationInfo, this.mBaseClassLoader);
    }

    private static ApplicationInfo adjustNativeLibraryPaths(ApplicationInfo info) {
        if (!(info.primaryCpuAbi == null || info.secondaryCpuAbi == null)) {
            String runtimeIsa = VMRuntime.getRuntime().vmInstructionSet();
            String secondaryIsa = VMRuntime.getInstructionSet(info.secondaryCpuAbi);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ro.dalvik.vm.isa.");
            stringBuilder.append(secondaryIsa);
            String secondaryDexCodeIsa = SystemProperties.get(stringBuilder.toString());
            if (runtimeIsa.equals(secondaryDexCodeIsa.isEmpty() ? secondaryIsa : secondaryDexCodeIsa)) {
                ApplicationInfo modified = new ApplicationInfo(info);
                modified.nativeLibraryDir = modified.secondaryNativeLibraryDir;
                modified.primaryCpuAbi = modified.secondaryCpuAbi;
                return modified;
            }
        }
        return info;
    }

    LoadedApk(ActivityThread activityThread) {
        this.mActivityThread = activityThread;
        this.mApplicationInfo = new ApplicationInfo();
        String str = "android";
        this.mApplicationInfo.packageName = str;
        this.mPackageName = str;
        this.mAppDir = null;
        this.mResDir = null;
        this.mSplitAppDirs = null;
        this.mSplitResDirs = null;
        this.mSplitClassLoaderNames = null;
        this.mOverlayDirs = null;
        this.mDataDir = null;
        this.mDataDirFile = null;
        this.mDeviceProtectedDataDirFile = null;
        this.mCredentialProtectedDataDirFile = null;
        this.mLibDir = null;
        this.mBaseClassLoader = null;
        this.mSecurityViolation = false;
        this.mIncludeCode = true;
        this.mRegisterPackage = false;
        this.mResources = Resources.getSystem();
        this.mDefaultClassLoader = ClassLoader.getSystemClassLoader();
        this.mAppComponentFactory = createAppFactory(this.mApplicationInfo, this.mDefaultClassLoader);
        this.mClassLoader = this.mAppComponentFactory.instantiateClassLoader(this.mDefaultClassLoader, new ApplicationInfo(this.mApplicationInfo));
    }

    /* Access modifiers changed, original: 0000 */
    public void installSystemApplicationInfo(ApplicationInfo info, ClassLoader classLoader) {
        this.mApplicationInfo = info;
        this.mDefaultClassLoader = classLoader;
        this.mAppComponentFactory = createAppFactory(info, this.mDefaultClassLoader);
        this.mClassLoader = this.mAppComponentFactory.instantiateClassLoader(this.mDefaultClassLoader, new ApplicationInfo(this.mApplicationInfo));
    }

    private AppComponentFactory createAppFactory(ApplicationInfo appInfo, ClassLoader cl) {
        if (!(appInfo.appComponentFactory == null || cl == null)) {
            try {
                return (AppComponentFactory) cl.loadClass(appInfo.appComponentFactory).newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                Slog.e(TAG, "Unable to instantiate appComponentFactory", e);
            }
        }
        return AppComponentFactory.DEFAULT;
    }

    public AppComponentFactory getAppFactory() {
        return this.mAppComponentFactory;
    }

    @UnsupportedAppUsage
    public String getPackageName() {
        return this.mPackageName;
    }

    @UnsupportedAppUsage
    public ApplicationInfo getApplicationInfo() {
        return this.mApplicationInfo;
    }

    public int getTargetSdkVersion() {
        return this.mApplicationInfo.targetSdkVersion;
    }

    public boolean isSecurityViolation() {
        return this.mSecurityViolation;
    }

    @UnsupportedAppUsage
    public CompatibilityInfo getCompatibilityInfo() {
        return this.mDisplayAdjustments.getCompatibilityInfo();
    }

    public void setCompatibilityInfo(CompatibilityInfo compatInfo) {
        this.mDisplayAdjustments.setCompatibilityInfo(compatInfo);
    }

    private static String[] getLibrariesFor(String packageName) {
        try {
            ApplicationInfo ai = ActivityThread.getPackageManager().getApplicationInfo(packageName, 1024, UserHandle.myUserId());
            if (ai == null) {
                return null;
            }
            return ai.sharedLibraryFiles;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void updateApplicationInfo(ApplicationInfo aInfo, List<String> oldPaths) {
        setApplicationInfo(aInfo);
        List<String> newPaths = new ArrayList();
        makePaths(this.mActivityThread, aInfo, newPaths);
        List<String> addedPaths = new ArrayList(newPaths.size());
        if (oldPaths != null) {
            for (String path : newPaths) {
                String apkName = path.substring(path.lastIndexOf(File.separator));
                boolean match = false;
                for (String oldPath : oldPaths) {
                    if (apkName.equals(oldPath.substring(oldPath.lastIndexOf(File.separator)))) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    addedPaths.add(path);
                }
            }
        } else {
            addedPaths.addAll(newPaths);
        }
        synchronized (this) {
            createOrUpdateClassLoaderLocked(addedPaths);
            if (this.mResources != null) {
                try {
                    this.mResources = ResourcesManager.getInstance().getResources(null, this.mResDir, getSplitPaths(null), this.mOverlayDirs, this.mApplicationInfo.sharedLibraryFiles, 0, null, getCompatibilityInfo(), getClassLoader());
                } catch (NameNotFoundException e) {
                    throw new AssertionError("null split not found");
                }
            }
        }
        this.mAppComponentFactory = createAppFactory(aInfo, this.mDefaultClassLoader);
    }

    private void setApplicationInfo(ApplicationInfo aInfo) {
        int myUid = Process.myUid();
        aInfo = adjustNativeLibraryPaths(aInfo);
        this.mApplicationInfo = aInfo;
        this.mAppDir = aInfo.sourceDir;
        this.mResDir = aInfo.uid == myUid ? aInfo.sourceDir : aInfo.publicSourceDir;
        this.mOverlayDirs = aInfo.resourceDirs;
        this.mDataDir = aInfo.dataDir;
        this.mLibDir = aInfo.nativeLibraryDir;
        this.mDataDirFile = FileUtils.newFileOrNull(aInfo.dataDir);
        this.mDeviceProtectedDataDirFile = FileUtils.newFileOrNull(aInfo.deviceProtectedDataDir);
        this.mCredentialProtectedDataDirFile = FileUtils.newFileOrNull(aInfo.credentialProtectedDataDir);
        this.mSplitNames = aInfo.splitNames;
        this.mSplitAppDirs = aInfo.splitSourceDirs;
        this.mSplitResDirs = aInfo.uid == myUid ? aInfo.splitSourceDirs : aInfo.splitPublicSourceDirs;
        this.mSplitClassLoaderNames = aInfo.splitClassLoaderNames;
        if (aInfo.requestsIsolatedSplitLoading() && !ArrayUtils.isEmpty(this.mSplitNames)) {
            this.mSplitLoader = new SplitDependencyLoaderImpl(aInfo.splitDependencies);
        }
    }

    public static void makePaths(ActivityThread activityThread, ApplicationInfo aInfo, List<String> outZipPaths) {
        makePaths(activityThread, false, aInfo, outZipPaths, null);
    }

    private static void appendSharedLibrariesLibPathsIfNeeded(List<SharedLibraryInfo> sharedLibraries, ApplicationInfo aInfo, Set<String> outSeenPaths, List<String> outLibPaths) {
        if (sharedLibraries != null) {
            for (SharedLibraryInfo lib : sharedLibraries) {
                List<String> paths = lib.getAllCodePaths();
                outSeenPaths.addAll(paths);
                for (String path : paths) {
                    appendApkLibPathIfNeeded(path, aInfo, outLibPaths);
                }
                appendSharedLibrariesLibPathsIfNeeded(lib.getDependencies(), aInfo, outSeenPaths, outLibPaths);
            }
        }
    }

    public static void makePaths(ActivityThread activityThread, boolean isBundledApp, ApplicationInfo aInfo, List<String> outZipPaths, List<String> outLibPaths) {
        String instrumentationAppDir;
        String instrumentedAppDir;
        String instrumentedLibDir;
        String appDir = aInfo.sourceDir;
        String libDir = aInfo.nativeLibraryDir;
        outZipPaths.clear();
        outZipPaths.add(appDir);
        if (!(aInfo.splitSourceDirs == null || aInfo.requestsIsolatedSplitLoading())) {
            Collections.addAll(outZipPaths, aInfo.splitSourceDirs);
        }
        if (outLibPaths != null) {
            outLibPaths.clear();
        }
        String[] instrumentationLibs = null;
        if (activityThread != null) {
            String instrumentationPackageName = activityThread.mInstrumentationPackageName;
            instrumentationAppDir = activityThread.mInstrumentationAppDir;
            String[] instrumentationSplitAppDirs = activityThread.mInstrumentationSplitAppDirs;
            String instrumentationLibDir = activityThread.mInstrumentationLibDir;
            instrumentedAppDir = activityThread.mInstrumentedAppDir;
            String[] instrumentedSplitAppDirs = activityThread.mInstrumentedSplitAppDirs;
            instrumentedLibDir = activityThread.mInstrumentedLibDir;
            if (appDir.equals(instrumentationAppDir) || appDir.equals(instrumentedAppDir)) {
                outZipPaths.clear();
                outZipPaths.add(instrumentationAppDir);
                if (!aInfo.requestsIsolatedSplitLoading()) {
                    if (instrumentationSplitAppDirs != null) {
                        Collections.addAll(outZipPaths, instrumentationSplitAppDirs);
                    }
                    if (!instrumentationAppDir.equals(instrumentedAppDir)) {
                        outZipPaths.add(instrumentedAppDir);
                        if (instrumentedSplitAppDirs != null) {
                            Collections.addAll(outZipPaths, instrumentedSplitAppDirs);
                        }
                    }
                }
                if (outLibPaths != null) {
                    outLibPaths.add(instrumentationLibDir);
                    if (!instrumentationLibDir.equals(instrumentedLibDir)) {
                        outLibPaths.add(instrumentedLibDir);
                    }
                }
                if (!instrumentedAppDir.equals(instrumentationAppDir)) {
                    instrumentationLibs = getLibrariesFor(instrumentationPackageName);
                }
            }
        }
        if (outLibPaths != null) {
            if (outLibPaths.isEmpty()) {
                outLibPaths.add(libDir);
            }
            if (aInfo.primaryCpuAbi != null) {
                if (aInfo.targetSdkVersion < 24) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("/system/fake-libs");
                    stringBuilder.append(VMRuntime.is64BitAbi(aInfo.primaryCpuAbi) ? "64" : "");
                    outLibPaths.add(stringBuilder.toString());
                }
                for (String instrumentationAppDir2 : outZipPaths) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(instrumentationAppDir2);
                    stringBuilder2.append("!/lib/");
                    stringBuilder2.append(aInfo.primaryCpuAbi);
                    outLibPaths.add(stringBuilder2.toString());
                }
            }
            if (isBundledApp) {
                outLibPaths.add(System.getProperty("java.library.path"));
            }
        }
        Set<String> outSeenPaths = new LinkedHashSet();
        appendSharedLibrariesLibPathsIfNeeded(aInfo.sharedLibraryInfos, aInfo, outSeenPaths, outLibPaths);
        if (aInfo.sharedLibraryFiles != null) {
            int index = 0;
            for (String instrumentedLibDir2 : aInfo.sharedLibraryFiles) {
                if (!(outSeenPaths.contains(instrumentedLibDir2) || outZipPaths.contains(instrumentedLibDir2))) {
                    outZipPaths.add(index, instrumentedLibDir2);
                    index++;
                    appendApkLibPathIfNeeded(instrumentedLibDir2, aInfo, outLibPaths);
                }
            }
        }
        if (instrumentationLibs != null) {
            for (String instrumentedAppDir2 : instrumentationLibs) {
                if (!outZipPaths.contains(instrumentedAppDir2)) {
                    outZipPaths.add(0, instrumentedAppDir2);
                    appendApkLibPathIfNeeded(instrumentedAppDir2, aInfo, outLibPaths);
                }
            }
        }
    }

    private static void appendApkLibPathIfNeeded(String path, ApplicationInfo applicationInfo, List<String> outLibPaths) {
        if (outLibPaths != null && applicationInfo.primaryCpuAbi != null && path.endsWith(PackageParser.APK_FILE_EXTENSION) && applicationInfo.targetSdkVersion >= 26) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(path);
            stringBuilder.append("!/lib/");
            stringBuilder.append(applicationInfo.primaryCpuAbi);
            outLibPaths.add(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ClassLoader getSplitClassLoader(String splitName) throws NameNotFoundException {
        SplitDependencyLoaderImpl splitDependencyLoaderImpl = this.mSplitLoader;
        if (splitDependencyLoaderImpl == null) {
            return this.mClassLoader;
        }
        return splitDependencyLoaderImpl.getClassLoaderForSplit(splitName);
    }

    /* Access modifiers changed, original: 0000 */
    public String[] getSplitPaths(String splitName) throws NameNotFoundException {
        SplitDependencyLoaderImpl splitDependencyLoaderImpl = this.mSplitLoader;
        if (splitDependencyLoaderImpl == null) {
            return this.mSplitResDirs;
        }
        return splitDependencyLoaderImpl.getSplitPathsForSplit(splitName);
    }

    /* Access modifiers changed, original: 0000 */
    public ClassLoader createSharedLibraryLoader(SharedLibraryInfo sharedLibrary, boolean isBundledApp, String librarySearchPath, String libraryPermittedPath) {
        String jars;
        Iterable paths = sharedLibrary.getAllCodePaths();
        List<ClassLoader> sharedLibraries = createSharedLibrariesLoaders(sharedLibrary.getDependencies(), isBundledApp, librarySearchPath, libraryPermittedPath);
        if (paths.size() == 1) {
            jars = (String) paths.get(0);
        } else {
            jars = TextUtils.join(File.pathSeparator, paths);
        }
        return ApplicationLoaders.getDefault().getSharedLibraryClassLoaderWithSharedLibraries(jars, this.mApplicationInfo.targetSdkVersion, isBundledApp, librarySearchPath, libraryPermittedPath, null, null, sharedLibraries);
    }

    private List<ClassLoader> createSharedLibrariesLoaders(List<SharedLibraryInfo> sharedLibraries, boolean isBundledApp, String librarySearchPath, String libraryPermittedPath) {
        if (sharedLibraries == null) {
            return null;
        }
        List<ClassLoader> loaders = new ArrayList();
        for (SharedLibraryInfo info : sharedLibraries) {
            loaders.add(createSharedLibraryLoader(info, isBundledApp, librarySearchPath, libraryPermittedPath));
        }
        return loaders;
    }

    private ThreadPolicy allowThreadDiskReads() {
        if (this.mActivityThread == null) {
            return null;
        }
        return StrictMode.allowThreadDiskReads();
    }

    private void setThreadPolicy(ThreadPolicy policy) {
        if (this.mActivityThread != null && policy != null) {
            StrictMode.setThreadPolicy(policy);
        }
    }

    private void createOrUpdateClassLoaderLocked(List<String> addedPaths) {
        Iterable iterable = addedPaths;
        if (!this.mPackageName.equals("android")) {
            boolean isBundledApp;
            StringBuilder stringBuilder;
            String libraryPermittedPath;
            if (!(this.mActivityThread == null || Objects.equals(this.mPackageName, ActivityThread.currentPackageName()) || !this.mIncludeCode)) {
                try {
                    ActivityThread.getPackageManager().notifyPackageUse(this.mPackageName, 6);
                } catch (RemoteException re) {
                    throw re.rethrowFromSystemServer();
                }
            }
            if (this.mRegisterPackage) {
                try {
                    ActivityManager.getService().addPackageDependency(this.mPackageName);
                } catch (RemoteException re2) {
                    throw re2.rethrowFromSystemServer();
                }
            }
            Iterable zipPaths = new ArrayList(10);
            Iterable libPaths = new ArrayList(10);
            boolean isBundledApp2 = this.mApplicationInfo.isSystemApp() && !this.mApplicationInfo.isUpdatedSystemApp();
            String defaultSearchPaths = System.getProperty("java.library.path");
            String str = "/vendor/lib";
            boolean treatVendorApkAsUnbundled = defaultSearchPaths.contains(str) ^ true;
            if (this.mApplicationInfo.getCodePath() != null && this.mApplicationInfo.isVendor() && treatVendorApkAsUnbundled) {
                isBundledApp = false;
            } else {
                isBundledApp = isBundledApp2;
            }
            makePaths(this.mActivityThread, isBundledApp, this.mApplicationInfo, zipPaths, libPaths);
            String libraryPermittedPath2 = this.mDataDir;
            if (isBundledApp) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(libraryPermittedPath2);
                stringBuilder.append(File.pathSeparator);
                stringBuilder.append(Paths.get(getAppDir(), new String[0]).getParent().toString());
                libraryPermittedPath2 = stringBuilder.toString();
                stringBuilder = new StringBuilder();
                stringBuilder.append(libraryPermittedPath2);
                stringBuilder.append(File.pathSeparator);
                stringBuilder.append(defaultSearchPaths);
                libraryPermittedPath = stringBuilder.toString();
            } else {
                libraryPermittedPath = libraryPermittedPath2;
            }
            String librarySearchPath = TextUtils.join(File.pathSeparator, libPaths);
            boolean isBundledApp3;
            if (this.mIncludeCode) {
                String zip;
                ThreadPolicy oldPolicy;
                boolean needToSetupJitProfiles;
                StringBuilder stringBuilder2;
                isBundledApp3 = isBundledApp;
                String librarySearchPath2 = librarySearchPath;
                if (zipPaths.size() == 1) {
                    zip = (String) zipPaths.get(0);
                } else {
                    zip = TextUtils.join(File.pathSeparator, zipPaths);
                }
                boolean isBundledApp4;
                if (this.mDefaultClassLoader == null) {
                    oldPolicy = allowThreadDiskReads();
                    boolean z = isBundledApp3;
                    needToSetupJitProfiles = false;
                    isBundledApp4 = z;
                    this.mDefaultClassLoader = ApplicationLoaders.getDefault().getClassLoaderWithSharedLibraries(zip, this.mApplicationInfo.targetSdkVersion, z, librarySearchPath2, libraryPermittedPath, this.mBaseClassLoader, this.mApplicationInfo.classLoaderName, createSharedLibrariesLoaders(this.mApplicationInfo.sharedLibraryInfos, z, librarySearchPath2, libraryPermittedPath));
                    this.mAppComponentFactory = createAppFactory(this.mApplicationInfo, this.mDefaultClassLoader);
                    setThreadPolicy(oldPolicy);
                    needToSetupJitProfiles = true;
                } else {
                    needToSetupJitProfiles = false;
                    String str2 = libraryPermittedPath;
                    String str3 = librarySearchPath2;
                    isBundledApp4 = isBundledApp3;
                }
                if (!libPaths.isEmpty() && SystemProperties.getBoolean(PROPERTY_NAME_APPEND_NATIVE, true)) {
                    oldPolicy = allowThreadDiskReads();
                    try {
                        ApplicationLoaders.getDefault().addNative(this.mDefaultClassLoader, libPaths);
                    } finally {
                        setThreadPolicy(oldPolicy);
                    }
                }
                ArrayList extraLibPaths = new ArrayList(4);
                String abiSuffix = VMRuntime.getRuntime().is64Bit() ? "64" : "";
                libraryPermittedPath2 = "/apex/com.android.runtime/lib";
                if (!defaultSearchPaths.contains(libraryPermittedPath2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(libraryPermittedPath2);
                    stringBuilder.append(abiSuffix);
                    extraLibPaths.add(stringBuilder.toString());
                }
                if (!defaultSearchPaths.contains(str)) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(str);
                    stringBuilder3.append(abiSuffix);
                    extraLibPaths.add(stringBuilder3.toString());
                }
                libraryPermittedPath2 = "/odm/lib";
                if (!defaultSearchPaths.contains(libraryPermittedPath2)) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(libraryPermittedPath2);
                    stringBuilder2.append(abiSuffix);
                    extraLibPaths.add(stringBuilder2.toString());
                }
                libraryPermittedPath2 = "/product/lib";
                if (!defaultSearchPaths.contains(libraryPermittedPath2)) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(libraryPermittedPath2);
                    stringBuilder2.append(abiSuffix);
                    extraLibPaths.add(stringBuilder2.toString());
                }
                if (!extraLibPaths.isEmpty()) {
                    ThreadPolicy oldPolicy2 = allowThreadDiskReads();
                    try {
                        ApplicationLoaders.getDefault().addNative(this.mDefaultClassLoader, extraLibPaths);
                    } finally {
                        setThreadPolicy(oldPolicy2);
                    }
                }
                if (iterable != null && addedPaths.size() > 0) {
                    ApplicationLoaders.getDefault().addPath(this.mDefaultClassLoader, TextUtils.join(File.pathSeparator, iterable));
                    needToSetupJitProfiles = true;
                }
                if (!(!needToSetupJitProfiles || ActivityThread.isSystem() || this.mActivityThread == null)) {
                    setupJitProfileSupport();
                }
                if (this.mClassLoader == null) {
                    this.mClassLoader = this.mAppComponentFactory.instantiateClassLoader(this.mDefaultClassLoader, new ApplicationInfo(this.mApplicationInfo));
                }
                return;
            }
            if (this.mDefaultClassLoader == null) {
                ThreadPolicy oldPolicy3 = allowThreadDiskReads();
                this.mDefaultClassLoader = ApplicationLoaders.getDefault().getClassLoader("", this.mApplicationInfo.targetSdkVersion, isBundledApp, librarySearchPath, libraryPermittedPath, this.mBaseClassLoader, null);
                setThreadPolicy(oldPolicy3);
                this.mAppComponentFactory = AppComponentFactory.DEFAULT;
            } else {
                str = libraryPermittedPath;
                isBundledApp3 = isBundledApp;
            }
            if (this.mClassLoader == null) {
                this.mClassLoader = this.mAppComponentFactory.instantiateClassLoader(this.mDefaultClassLoader, new ApplicationInfo(this.mApplicationInfo));
            }
        } else if (this.mClassLoader == null) {
            ClassLoader classLoader = this.mBaseClassLoader;
            if (classLoader != null) {
                this.mDefaultClassLoader = classLoader;
            } else {
                this.mDefaultClassLoader = ClassLoader.getSystemClassLoader();
            }
            this.mAppComponentFactory = createAppFactory(this.mApplicationInfo, this.mDefaultClassLoader);
            this.mClassLoader = this.mAppComponentFactory.instantiateClassLoader(this.mDefaultClassLoader, new ApplicationInfo(this.mApplicationInfo));
        }
    }

    @UnsupportedAppUsage
    public ClassLoader getClassLoader() {
        ClassLoader classLoader;
        synchronized (this) {
            if (this.mClassLoader == null) {
                createOrUpdateClassLoaderLocked(null);
            }
            classLoader = this.mClassLoader;
        }
        return classLoader;
    }

    private void setupJitProfileSupport() {
        if (SystemProperties.getBoolean("dalvik.vm.usejitprofiles", false)) {
            BaseDexClassLoader.setReporter(DexLoadReporter.getInstance());
            if (this.mApplicationInfo.uid == Process.myUid()) {
                List<String> codePaths = new ArrayList();
                if ((this.mApplicationInfo.flags & 4) != 0) {
                    codePaths.add(this.mApplicationInfo.sourceDir);
                }
                if (this.mApplicationInfo.splitSourceDirs != null) {
                    Collections.addAll(codePaths, this.mApplicationInfo.splitSourceDirs);
                }
                if (!codePaths.isEmpty()) {
                    int i = codePaths.size() - 1;
                    while (i >= 0) {
                        VMRuntime.registerAppInfo(ArtManager.getCurrentProfilePath(this.mPackageName, UserHandle.myUserId(), i == 0 ? null : this.mApplicationInfo.splitNames[i - 1]), new String[]{(String) codePaths.get(i)});
                        i--;
                    }
                    DexLoadReporter.getInstance().registerAppDataDir(this.mPackageName, this.mDataDir);
                }
            }
        }
    }

    private void initializeJavaContextClassLoader() {
        try {
            PackageInfo pi = ActivityThread.getPackageManager().getPackageInfo(this.mPackageName, 268435456, UserHandle.myUserId());
            if (pi != null) {
                ClassLoader contextClassLoader;
                boolean sharable = true;
                boolean sharedUserIdSet = pi.sharedUserId != null;
                boolean processNameNotDefault = (pi.applicationInfo == null || this.mPackageName.equals(pi.applicationInfo.processName)) ? false : true;
                if (!(sharedUserIdSet || processNameNotDefault)) {
                    sharable = false;
                }
                if (sharable) {
                    contextClassLoader = new WarningContextClassLoader();
                } else {
                    contextClassLoader = this.mClassLoader;
                }
                Thread.currentThread().setContextClassLoader(contextClassLoader);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to get package info for ");
            stringBuilder.append(this.mPackageName);
            stringBuilder.append("; is package not installed?");
            throw new IllegalStateException(stringBuilder.toString());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public String getAppDir() {
        return this.mAppDir;
    }

    public String getLibDir() {
        return this.mLibDir;
    }

    @UnsupportedAppUsage
    public String getResDir() {
        return this.mResDir;
    }

    public String[] getSplitAppDirs() {
        return this.mSplitAppDirs;
    }

    @UnsupportedAppUsage
    public String[] getSplitResDirs() {
        return this.mSplitResDirs;
    }

    @UnsupportedAppUsage
    public String[] getOverlayDirs() {
        return this.mOverlayDirs;
    }

    public String getDataDir() {
        return this.mDataDir;
    }

    @UnsupportedAppUsage
    public File getDataDirFile() {
        return this.mDataDirFile;
    }

    public File getDeviceProtectedDataDirFile() {
        return this.mDeviceProtectedDataDirFile;
    }

    public File getCredentialProtectedDataDirFile() {
        return this.mCredentialProtectedDataDirFile;
    }

    @UnsupportedAppUsage
    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    @UnsupportedAppUsage
    public Resources getResources() {
        if (this.mResources == null) {
            try {
                this.mResources = ResourcesManager.getInstance().getResources(null, this.mResDir, getSplitPaths(null), this.mOverlayDirs, this.mApplicationInfo.sharedLibraryFiles, 0, null, getCompatibilityInfo(), getClassLoader());
                ResourcesManager.initMiuiResource(this.mResources, this.mPackageName);
            } catch (NameNotFoundException e) {
                throw new AssertionError("null split not found");
            }
        }
        return this.mResources;
    }

    @UnsupportedAppUsage
    public Application makeApplication(boolean forceDefaultAppClass, Instrumentation instrumentation) {
        StringBuilder stringBuilder;
        String str = ": ";
        Application application = this.mApplication;
        if (application != null) {
            return application;
        }
        Trace.traceBegin(64, "makeApplication");
        Application app = null;
        String appClass = this.mApplicationInfo.className;
        if (forceDefaultAppClass || appClass == null) {
            appClass = "android.app.Application";
        }
        try {
            ClassLoader cl = getClassLoader();
            if (!this.mPackageName.equals("android")) {
                Trace.traceBegin(64, "initializeJavaContextClassLoader");
                initializeJavaContextClassLoader();
                Trace.traceEnd(64);
            }
            ContextImpl appContext = ContextImpl.createAppContext(this.mActivityThread, this);
            app = this.mActivityThread.mInstrumentation.newApplication(cl, appClass, appContext);
            appContext.setOuterContext(app);
        } catch (Exception e) {
            if (!this.mActivityThread.mInstrumentation.onException(null, e)) {
                Trace.traceEnd(64);
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to instantiate application ");
                stringBuilder.append(appClass);
                stringBuilder.append(str);
                stringBuilder.append(e.toString());
                throw new RuntimeException(stringBuilder.toString(), e);
            }
        }
        this.mActivityThread.mAllApplications.add(app);
        this.mApplication = app;
        if (instrumentation != null) {
            try {
                instrumentation.callApplicationOnCreate(app);
            } catch (Exception e2) {
                if (!instrumentation.onException(app, e2)) {
                    Trace.traceEnd(64);
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to create application ");
                    stringBuilder.append(app.getClass().getName());
                    stringBuilder.append(str);
                    stringBuilder.append(e2.toString());
                    throw new RuntimeException(stringBuilder.toString(), e2);
                }
            }
        }
        SparseArray<String> packageIdentifiers = getAssets().getAssignedPackageIdentifiers();
        Exception e22 = packageIdentifiers.size();
        for (int i = 0; i < e22; i++) {
            int id = packageIdentifiers.keyAt(i);
            if (!(id == 1 || id == 127)) {
                rewriteRValues(getClassLoader(), (String) packageIdentifiers.valueAt(i), id);
            }
        }
        Trace.traceEnd(64);
        return app;
    }

    @UnsupportedAppUsage
    private void rewriteRValues(ClassLoader cl, String packageName, int id) {
        Throwable cause;
        StringBuilder stringBuilder;
        try {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(packageName);
            stringBuilder2.append(".R");
            try {
                try {
                    cl.loadClass(stringBuilder2.toString()).getMethod("onResourcesLoaded", new Class[]{Integer.TYPE}).invoke(null, new Object[]{Integer.valueOf(id)});
                } catch (IllegalAccessException e) {
                    cause = e;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to rewrite resource references for ");
                    stringBuilder.append(packageName);
                    throw new RuntimeException(stringBuilder.toString(), cause);
                } catch (InvocationTargetException cause2) {
                    cause2 = cause2.getCause();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to rewrite resource references for ");
                    stringBuilder.append(packageName);
                    throw new RuntimeException(stringBuilder.toString(), cause2);
                }
            } catch (NoSuchMethodException e2) {
            }
        } catch (ClassNotFoundException e3) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("No resource references to update in package ");
            stringBuilder3.append(packageName);
            Log.i(TAG, stringBuilder3.toString());
        }
    }

    public void removeContextRegistrations(Context context, String who, String what) {
        int i;
        boolean reportRegistrationLeaks = StrictMode.vmRegistrationLeaksEnabled();
        synchronized (this.mReceivers) {
            ArrayMap<BroadcastReceiver, ReceiverDispatcher> rmap = (ArrayMap) this.mReceivers.remove(context);
            i = 0;
            if (rmap != null) {
                int i2 = 0;
                while (i2 < rmap.size()) {
                    ReceiverDispatcher rd = (ReceiverDispatcher) rmap.valueAt(i2);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(what);
                    stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    stringBuilder.append(who);
                    stringBuilder.append(" has leaked IntentReceiver ");
                    stringBuilder.append(rd.getIntentReceiver());
                    stringBuilder.append(" that was originally registered here. Are you missing a call to unregisterReceiver()?");
                    IntentReceiverLeaked leak = new IntentReceiverLeaked(stringBuilder.toString());
                    leak.setStackTrace(rd.getLocation().getStackTrace());
                    Slog.e(ActivityThread.TAG, leak.getMessage(), leak);
                    if (reportRegistrationLeaks) {
                        StrictMode.onIntentReceiverLeaked(leak);
                    }
                    try {
                        ActivityManager.getService().unregisterReceiver(rd.getIIntentReceiver());
                        i2++;
                    } catch (RemoteException i3) {
                        throw i3.rethrowFromSystemServer();
                    }
                }
            }
            this.mUnregisteredReceivers.remove(context);
        }
        synchronized (this.mServices) {
            ArrayMap<ServiceConnection, ServiceDispatcher> smap = (ArrayMap) this.mServices.remove(context);
            if (smap != null) {
                while (i3 < smap.size()) {
                    ServiceDispatcher sd = (ServiceDispatcher) smap.valueAt(i3);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(what);
                    stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    stringBuilder2.append(who);
                    stringBuilder2.append(" has leaked ServiceConnection ");
                    stringBuilder2.append(sd.getServiceConnection());
                    stringBuilder2.append(" that was originally bound here");
                    ServiceConnectionLeaked leak2 = new ServiceConnectionLeaked(stringBuilder2.toString());
                    leak2.setStackTrace(sd.getLocation().getStackTrace());
                    Slog.e(ActivityThread.TAG, leak2.getMessage(), leak2);
                    if (reportRegistrationLeaks) {
                        StrictMode.onServiceConnectionLeaked(leak2);
                    }
                    try {
                        ActivityManager.getService().unbindService(sd.getIServiceConnection());
                        sd.doForget();
                        i3++;
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            }
            this.mUnboundServices.remove(context);
        }
    }

    public IIntentReceiver getReceiverDispatcher(BroadcastReceiver r, Context context, Handler handler, Instrumentation instrumentation, boolean registered) {
        IIntentReceiver iIntentReceiver;
        synchronized (this.mReceivers) {
            ReceiverDispatcher rd = null;
            ArrayMap<BroadcastReceiver, ReceiverDispatcher> map = null;
            if (registered) {
                try {
                    map = (ArrayMap) this.mReceivers.get(context);
                    if (map != null) {
                        rd = (ReceiverDispatcher) map.get(r);
                    }
                } finally {
                }
            }
            if (rd == null) {
                rd = new ReceiverDispatcher(r, context, handler, instrumentation, registered);
                if (registered) {
                    if (map == null) {
                        map = new ArrayMap();
                        this.mReceivers.put(context, map);
                    }
                    map.put(r, rd);
                }
            } else {
                rd.validate(context, handler);
            }
            rd.mForgotten = false;
            iIntentReceiver = rd.getIIntentReceiver();
        }
        return iIntentReceiver;
    }

    public IIntentReceiver forgetReceiverDispatcher(Context context, BroadcastReceiver r) {
        IIntentReceiver iIntentReceiver;
        synchronized (this.mReceivers) {
            ReceiverDispatcher rd;
            ArrayMap<BroadcastReceiver, ReceiverDispatcher> holder;
            RuntimeException ex;
            ArrayMap<BroadcastReceiver, ReceiverDispatcher> map = (ArrayMap) this.mReceivers.get(context);
            if (map != null) {
                rd = (ReceiverDispatcher) map.get(r);
                if (rd != null) {
                    map.remove(r);
                    if (map.size() == 0) {
                        this.mReceivers.remove(context);
                    }
                    if (r.getDebugUnregister()) {
                        holder = (ArrayMap) this.mUnregisteredReceivers.get(context);
                        if (holder == null) {
                            holder = new ArrayMap();
                            this.mUnregisteredReceivers.put(context, holder);
                        }
                        ex = new IllegalArgumentException("Originally unregistered here:");
                        ex.fillInStackTrace();
                        rd.setUnregisterLocation(ex);
                        holder.put(r, rd);
                    }
                    rd.mForgotten = true;
                    iIntentReceiver = rd.getIIntentReceiver();
                }
            }
            holder = (ArrayMap) this.mUnregisteredReceivers.get(context);
            if (holder != null) {
                rd = (ReceiverDispatcher) holder.get(r);
                if (rd != null) {
                    ex = rd.getUnregisterLocation();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unregistering Receiver ");
                    stringBuilder.append(r);
                    stringBuilder.append(" that was already unregistered");
                    throw new IllegalArgumentException(stringBuilder.toString(), ex);
                }
            }
            StringBuilder stringBuilder2;
            if (context == null) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unbinding Receiver ");
                stringBuilder2.append(r);
                stringBuilder2.append(" from Context that is no longer in use: ");
                stringBuilder2.append(context);
                throw new IllegalStateException(stringBuilder2.toString());
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Receiver not registered: ");
            stringBuilder2.append(r);
            throw new IllegalArgumentException(stringBuilder2.toString());
        }
        return iIntentReceiver;
    }

    @UnsupportedAppUsage
    public final IServiceConnection getServiceDispatcher(ServiceConnection c, Context context, Handler handler, int flags) {
        return getServiceDispatcherCommon(c, context, handler, null, flags);
    }

    public final IServiceConnection getServiceDispatcher(ServiceConnection c, Context context, Executor executor, int flags) {
        return getServiceDispatcherCommon(c, context, null, executor, flags);
    }

    private IServiceConnection getServiceDispatcherCommon(ServiceConnection c, Context context, Handler handler, Executor executor, int flags) {
        IServiceConnection iServiceConnection;
        synchronized (this.mServices) {
            ServiceDispatcher sd = null;
            ArrayMap<ServiceConnection, ServiceDispatcher> map = (ArrayMap) this.mServices.get(context);
            if (map != null) {
                sd = (ServiceDispatcher) map.get(c);
            }
            if (sd == null) {
                if (executor != null) {
                    sd = new ServiceDispatcher(c, context, executor, flags);
                } else {
                    sd = new ServiceDispatcher(c, context, handler, flags);
                }
                if (map == null) {
                    map = new ArrayMap();
                    this.mServices.put(context, map);
                }
                map.put(c, sd);
            } else {
                sd.validate(context, handler, executor);
            }
            iServiceConnection = sd.getIServiceConnection();
        }
        return iServiceConnection;
    }

    @UnsupportedAppUsage
    public IServiceConnection lookupServiceDispatcher(ServiceConnection c, Context context) {
        IServiceConnection iServiceConnection;
        synchronized (this.mServices) {
            ServiceDispatcher sd = null;
            ArrayMap<ServiceConnection, ServiceDispatcher> map = (ArrayMap) this.mServices.get(context);
            if (map != null) {
                sd = (ServiceDispatcher) map.get(c);
            }
            iServiceConnection = sd != null ? sd.getIServiceConnection() : null;
        }
        return iServiceConnection;
    }

    public final IServiceConnection forgetServiceDispatcher(Context context, ServiceConnection c) {
        IServiceConnection iServiceConnection;
        synchronized (this.mServices) {
            ServiceDispatcher sd;
            ArrayMap<ServiceConnection, ServiceDispatcher> holder;
            RuntimeException ex;
            ArrayMap<ServiceConnection, ServiceDispatcher> map = (ArrayMap) this.mServices.get(context);
            if (map != null) {
                sd = (ServiceDispatcher) map.get(c);
                if (sd != null) {
                    map.remove(c);
                    sd.doForget();
                    if (map.size() == 0) {
                        this.mServices.remove(context);
                    }
                    if ((sd.getFlags() & 2) != 0) {
                        holder = (ArrayMap) this.mUnboundServices.get(context);
                        if (holder == null) {
                            holder = new ArrayMap();
                            this.mUnboundServices.put(context, holder);
                        }
                        ex = new IllegalArgumentException("Originally unbound here:");
                        ex.fillInStackTrace();
                        sd.setUnbindLocation(ex);
                        holder.put(c, sd);
                    }
                    iServiceConnection = sd.getIServiceConnection();
                }
            }
            holder = (ArrayMap) this.mUnboundServices.get(context);
            if (holder != null) {
                sd = (ServiceDispatcher) holder.get(c);
                if (sd != null) {
                    ex = sd.getUnbindLocation();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unbinding Service ");
                    stringBuilder.append(c);
                    stringBuilder.append(" that was already unbound");
                    throw new IllegalArgumentException(stringBuilder.toString(), ex);
                }
            }
            StringBuilder stringBuilder2;
            if (context == null) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unbinding Service ");
                stringBuilder2.append(c);
                stringBuilder2.append(" from Context that is no longer in use: ");
                stringBuilder2.append(context);
                throw new IllegalStateException(stringBuilder2.toString());
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Service not registered: ");
            stringBuilder2.append(c);
            throw new IllegalArgumentException(stringBuilder2.toString());
        }
        return iServiceConnection;
    }
}
