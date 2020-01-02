package android.os;

import android.animation.ValueAnimator;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.MessageQueue.IdleHandler;
import android.os.Parcelable.Creator;
import android.os.storage.IStorageManager;
import android.os.strictmode.CleartextNetworkViolation;
import android.os.strictmode.ContentUriWithoutPermissionViolation;
import android.os.strictmode.CredentialProtectedWhileLockedViolation;
import android.os.strictmode.CustomViolation;
import android.os.strictmode.DiskReadViolation;
import android.os.strictmode.DiskWriteViolation;
import android.os.strictmode.ExplicitGcViolation;
import android.os.strictmode.FileUriExposedViolation;
import android.os.strictmode.ImplicitDirectBootViolation;
import android.os.strictmode.InstanceCountViolation;
import android.os.strictmode.IntentReceiverLeakedViolation;
import android.os.strictmode.LeakedClosableViolation;
import android.os.strictmode.NetworkViolation;
import android.os.strictmode.ResourceMismatchViolation;
import android.os.strictmode.ServiceConnectionLeakedViolation;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.os.strictmode.UnbufferedIoViolation;
import android.os.strictmode.UntaggedSocketViolation;
import android.os.strictmode.Violation;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Printer;
import android.util.Singleton;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.RuntimeInit;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.HexDump;
import dalvik.system.BlockGuard;
import dalvik.system.BlockGuard.Policy;
import dalvik.system.CloseGuard;
import dalvik.system.CloseGuard.Reporter;
import dalvik.system.VMDebug;
import dalvik.system.VMRuntime;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import miui.mqsas.oom.OOMEventManager;

public final class StrictMode {
    private static final String CLEARTEXT_PROPERTY = "persist.sys.strictmode.clear";
    public static final int DETECT_DISK_READ = 2;
    public static final int DETECT_DISK_WRITE = 1;
    private static final int DETECT_THREAD_ALL = 65535;
    private static final int DETECT_THREAD_CUSTOM = 8;
    private static final int DETECT_THREAD_DISK_READ = 2;
    private static final int DETECT_THREAD_DISK_WRITE = 1;
    private static final int DETECT_THREAD_EXPLICIT_GC = 64;
    private static final int DETECT_THREAD_NETWORK = 4;
    private static final int DETECT_THREAD_RESOURCE_MISMATCH = 16;
    private static final int DETECT_THREAD_UNBUFFERED_IO = 32;
    private static final int DETECT_VM_ACTIVITY_LEAKS = 4;
    private static final int DETECT_VM_ALL = 65535;
    private static final int DETECT_VM_CLEARTEXT_NETWORK = 64;
    private static final int DETECT_VM_CLOSABLE_LEAKS = 2;
    private static final int DETECT_VM_CONTENT_URI_WITHOUT_PERMISSION = 128;
    private static final int DETECT_VM_CREDENTIAL_PROTECTED_WHILE_LOCKED = 2048;
    private static final int DETECT_VM_CURSOR_LEAKS = 1;
    private static final int DETECT_VM_FILE_URI_EXPOSURE = 32;
    private static final int DETECT_VM_IMPLICIT_DIRECT_BOOT = 1024;
    private static final int DETECT_VM_INSTANCE_LEAKS = 8;
    private static final int DETECT_VM_NON_SDK_API_USAGE = 512;
    private static final int DETECT_VM_REGISTRATION_LEAKS = 16;
    private static final int DETECT_VM_UNTAGGED_SOCKET = 256;
    private static final boolean DISABLE = false;
    public static final String DISABLE_PROPERTY = "persist.sys.strictmode.disable";
    private static final HashMap<Class, Integer> EMPTY_CLASS_LIMIT_MAP = new HashMap();
    private static final ViolationLogger LOGCAT_LOGGER = -$$Lambda$StrictMode$1yH8AK0bTwVwZOb9x8HoiSBdzr0.INSTANCE;
    private static final boolean LOG_V = Log.isLoggable(TAG, 2);
    private static final int MAX_OFFENSES_PER_LOOP = 10;
    private static final int MAX_SPAN_TAGS = 20;
    private static final long MIN_DIALOG_INTERVAL_MS = 30000;
    private static final long MIN_LOG_INTERVAL_MS = 1000;
    private static final long MIN_VM_INTERVAL_MS = 1000;
    public static final int NETWORK_POLICY_ACCEPT = 0;
    public static final int NETWORK_POLICY_LOG = 1;
    public static final int NETWORK_POLICY_REJECT = 2;
    private static final Span NO_OP_SPAN = new Span() {
        public void finish() {
        }
    };
    public static final int PENALTY_ALL = -65536;
    public static final int PENALTY_DEATH = 268435456;
    public static final int PENALTY_DEATH_ON_CLEARTEXT_NETWORK = 16777216;
    public static final int PENALTY_DEATH_ON_FILE_URI_EXPOSURE = 8388608;
    public static final int PENALTY_DEATH_ON_NETWORK = 33554432;
    public static final int PENALTY_DIALOG = 536870912;
    public static final int PENALTY_DROPBOX = 67108864;
    public static final int PENALTY_FLASH = 134217728;
    public static final int PENALTY_GATHER = Integer.MIN_VALUE;
    public static final int PENALTY_LOG = 1073741824;
    private static final String TAG = "StrictMode";
    private static final ThreadLocal<AndroidBlockGuardPolicy> THREAD_ANDROID_POLICY = new ThreadLocal<AndroidBlockGuardPolicy>() {
        /* Access modifiers changed, original: protected */
        public AndroidBlockGuardPolicy initialValue() {
            return new AndroidBlockGuardPolicy(0);
        }
    };
    private static final ThreadLocal<Handler> THREAD_HANDLER = new ThreadLocal<Handler>() {
        /* Access modifiers changed, original: protected */
        public Handler initialValue() {
            return new Handler();
        }
    };
    public static final String VISUAL_PROPERTY = "persist.sys.strictmode.visual";
    private static final dalvik.system.BlockGuard.VmPolicy VM_ANDROID_POLICY = new dalvik.system.BlockGuard.VmPolicy() {
        public void onPathAccess(String path) {
            if (path != null) {
                if (path.startsWith("/data/user/") || path.startsWith("/data/media/") || path.startsWith("/data/system_ce/") || path.startsWith("/data/misc_ce/") || path.startsWith("/data/vendor_ce/") || path.startsWith("/storage/emulated/")) {
                    int third = path.indexOf(47, path.indexOf(47, 1) + 1);
                    int fourth = path.indexOf(47, third + 1);
                    if (fourth != -1) {
                        try {
                            StrictMode.onCredentialProtectedPathAccess(path, Integer.parseInt(path.substring(third + 1, fourth)));
                        } catch (NumberFormatException e) {
                        }
                    }
                } else if (path.startsWith(OOMEventManager.FILE_DIR_DATA_APP)) {
                    StrictMode.onCredentialProtectedPathAccess(path, 0);
                }
            }
        }
    };
    private static final ThreadLocal<ArrayList<ViolationInfo>> gatheredViolations = new ThreadLocal<ArrayList<ViolationInfo>>() {
        /* Access modifiers changed, original: protected */
        public ArrayList<ViolationInfo> initialValue() {
            return null;
        }
    };
    private static final AtomicInteger sDropboxCallsInFlight = new AtomicInteger(0);
    @GuardedBy({"StrictMode.class"})
    private static final HashMap<Class, Integer> sExpectedActivityInstanceCount = new HashMap();
    private static boolean sIsIdlerRegistered = false;
    private static long sLastInstanceCountCheckMillis = 0;
    @UnsupportedAppUsage
    private static final HashMap<Integer, Long> sLastVmViolationTime = new HashMap();
    private static volatile ViolationLogger sLogger = LOGCAT_LOGGER;
    private static final Consumer<String> sNonSdkApiUsageConsumer = -$$Lambda$StrictMode$lu9ekkHJ2HMz0jd3F8K8MnhenxQ.INSTANCE;
    private static final IdleHandler sProcessIdleHandler = new IdleHandler() {
        public boolean queueIdle() {
            long now = SystemClock.uptimeMillis();
            if (now - StrictMode.sLastInstanceCountCheckMillis > 30000) {
                StrictMode.sLastInstanceCountCheckMillis = now;
                StrictMode.conditionallyCheckInstanceCounts();
            }
            return true;
        }
    };
    private static final ThreadLocal<ThreadSpanState> sThisThreadSpanState = new ThreadLocal<ThreadSpanState>() {
        /* Access modifiers changed, original: protected */
        public ThreadSpanState initialValue() {
            return new ThreadSpanState();
        }
    };
    private static final ThreadLocal<Executor> sThreadViolationExecutor = new ThreadLocal();
    private static final ThreadLocal<OnThreadViolationListener> sThreadViolationListener = new ThreadLocal();
    private static volatile boolean sUserKeyUnlocked = false;
    private static volatile VmPolicy sVmPolicy = VmPolicy.LAX;
    @UnsupportedAppUsage
    private static Singleton<IWindowManager> sWindowManager = new Singleton<IWindowManager>() {
        /* Access modifiers changed, original: protected */
        public IWindowManager create() {
            return Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
        }
    };
    @UnsupportedAppUsage
    private static final ThreadLocal<ArrayList<ViolationInfo>> violationsBeingTimed = new ThreadLocal<ArrayList<ViolationInfo>>() {
        /* Access modifiers changed, original: protected */
        public ArrayList<ViolationInfo> initialValue() {
            return new ArrayList();
        }
    };

    public interface ViolationLogger {
        void log(ViolationInfo violationInfo);
    }

    public static class Span {
        private final ThreadSpanState mContainerState;
        private long mCreateMillis;
        private String mName;
        private Span mNext;
        private Span mPrev;

        Span(ThreadSpanState threadState) {
            this.mContainerState = threadState;
        }

        protected Span() {
            this.mContainerState = null;
        }

        /* JADX WARNING: Missing block: B:23:0x0070, code skipped:
            return;
     */
        @android.annotation.UnsupportedAppUsage
        public void finish() {
            /*
            r4 = this;
            r0 = r4.mContainerState;
            monitor-enter(r0);
            r1 = r4.mName;	 Catch:{ all -> 0x0071 }
            if (r1 != 0) goto L_0x0009;
        L_0x0007:
            monitor-exit(r0);	 Catch:{ all -> 0x0071 }
            return;
        L_0x0009:
            r1 = r4.mPrev;	 Catch:{ all -> 0x0071 }
            if (r1 == 0) goto L_0x0013;
        L_0x000d:
            r1 = r4.mPrev;	 Catch:{ all -> 0x0071 }
            r2 = r4.mNext;	 Catch:{ all -> 0x0071 }
            r1.mNext = r2;	 Catch:{ all -> 0x0071 }
        L_0x0013:
            r1 = r4.mNext;	 Catch:{ all -> 0x0071 }
            if (r1 == 0) goto L_0x001d;
        L_0x0017:
            r1 = r4.mNext;	 Catch:{ all -> 0x0071 }
            r2 = r4.mPrev;	 Catch:{ all -> 0x0071 }
            r1.mPrev = r2;	 Catch:{ all -> 0x0071 }
        L_0x001d:
            r1 = r0.mActiveHead;	 Catch:{ all -> 0x0071 }
            if (r1 != r4) goto L_0x0025;
        L_0x0021:
            r1 = r4.mNext;	 Catch:{ all -> 0x0071 }
            r0.mActiveHead = r1;	 Catch:{ all -> 0x0071 }
        L_0x0025:
            r1 = r0.mActiveSize;	 Catch:{ all -> 0x0071 }
            r1 = r1 + -1;
            r0.mActiveSize = r1;	 Catch:{ all -> 0x0071 }
            r1 = android.os.StrictMode.LOG_V;	 Catch:{ all -> 0x0071 }
            if (r1 == 0) goto L_0x0053;
        L_0x0031:
            r1 = "StrictMode";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0071 }
            r2.<init>();	 Catch:{ all -> 0x0071 }
            r3 = "Span finished=";
            r2.append(r3);	 Catch:{ all -> 0x0071 }
            r3 = r4.mName;	 Catch:{ all -> 0x0071 }
            r2.append(r3);	 Catch:{ all -> 0x0071 }
            r3 = "; size=";
            r2.append(r3);	 Catch:{ all -> 0x0071 }
            r3 = r0.mActiveSize;	 Catch:{ all -> 0x0071 }
            r2.append(r3);	 Catch:{ all -> 0x0071 }
            r2 = r2.toString();	 Catch:{ all -> 0x0071 }
            android.util.Log.d(r1, r2);	 Catch:{ all -> 0x0071 }
        L_0x0053:
            r1 = -1;
            r4.mCreateMillis = r1;	 Catch:{ all -> 0x0071 }
            r1 = 0;
            r4.mName = r1;	 Catch:{ all -> 0x0071 }
            r4.mPrev = r1;	 Catch:{ all -> 0x0071 }
            r4.mNext = r1;	 Catch:{ all -> 0x0071 }
            r1 = r0.mFreeListSize;	 Catch:{ all -> 0x0071 }
            r2 = 5;
            if (r1 >= r2) goto L_0x006f;
        L_0x0063:
            r1 = r0.mFreeListHead;	 Catch:{ all -> 0x0071 }
            r4.mNext = r1;	 Catch:{ all -> 0x0071 }
            r0.mFreeListHead = r4;	 Catch:{ all -> 0x0071 }
            r1 = r0.mFreeListSize;	 Catch:{ all -> 0x0071 }
            r1 = r1 + 1;
            r0.mFreeListSize = r1;	 Catch:{ all -> 0x0071 }
        L_0x006f:
            monitor-exit(r0);	 Catch:{ all -> 0x0071 }
            return;
        L_0x0071:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0071 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.os.StrictMode$Span.finish():void");
        }
    }

    private static class AndroidBlockGuardPolicy implements Policy {
        private ArrayMap<Integer, Long> mLastViolationTime;
        private int mThreadPolicyMask;

        public AndroidBlockGuardPolicy(int threadPolicyMask) {
            this.mThreadPolicyMask = threadPolicyMask;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AndroidBlockGuardPolicy; mPolicyMask=");
            stringBuilder.append(this.mThreadPolicyMask);
            return stringBuilder.toString();
        }

        public int getPolicyMask() {
            return this.mThreadPolicyMask;
        }

        public void onWriteToDisk() {
            if ((this.mThreadPolicyMask & 1) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                startHandlingViolationException(new DiskWriteViolation());
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void onCustomSlowCall(String name) {
            if ((this.mThreadPolicyMask & 8) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                startHandlingViolationException(new CustomViolation(name));
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void onResourceMismatch(Object tag) {
            if ((this.mThreadPolicyMask & 16) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                startHandlingViolationException(new ResourceMismatchViolation(tag));
            }
        }

        public void onUnbufferedIO() {
            if ((this.mThreadPolicyMask & 32) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                startHandlingViolationException(new UnbufferedIoViolation());
            }
        }

        public void onReadFromDisk() {
            if ((this.mThreadPolicyMask & 2) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                startHandlingViolationException(new DiskReadViolation());
            }
        }

        public void onNetwork() {
            int i = this.mThreadPolicyMask;
            if ((i & 4) != 0) {
                if ((i & 33554432) != 0) {
                    throw new NetworkOnMainThreadException();
                } else if (!StrictMode.tooManyViolationsThisLoop()) {
                    startHandlingViolationException(new NetworkViolation());
                }
            }
        }

        public void onExplicitGc() {
            if ((this.mThreadPolicyMask & 64) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                startHandlingViolationException(new ExplicitGcViolation());
            }
        }

        public int getThreadPolicyMask() {
            return this.mThreadPolicyMask;
        }

        public void setThreadPolicyMask(int threadPolicyMask) {
            this.mThreadPolicyMask = threadPolicyMask;
        }

        /* Access modifiers changed, original: 0000 */
        public void startHandlingViolationException(Violation e) {
            ViolationInfo info = new ViolationInfo(e, this.mThreadPolicyMask & -65536);
            info.violationUptimeMillis = SystemClock.uptimeMillis();
            handleViolationWithTimingAttempt(info);
        }

        /* Access modifiers changed, original: 0000 */
        public void handleViolationWithTimingAttempt(ViolationInfo info) {
            if (Looper.myLooper() == null || info.mPenaltyMask == 268435456) {
                info.durationMillis = -1;
                onThreadPolicyViolation(info);
                return;
            }
            ArrayList<ViolationInfo> records = (ArrayList) StrictMode.violationsBeingTimed.get();
            if (records.size() < 10) {
                records.add(info);
                if (records.size() <= 1) {
                    IWindowManager windowManager = info.penaltyEnabled(134217728) ? (IWindowManager) StrictMode.sWindowManager.get() : null;
                    if (windowManager != null) {
                        try {
                            windowManager.showStrictModeViolation(true);
                        } catch (RemoteException e) {
                        }
                    }
                    ((Handler) StrictMode.THREAD_HANDLER.get()).postAtFrontOfQueue(new -$$Lambda$StrictMode$AndroidBlockGuardPolicy$9nBulCQKaMajrWr41SB7f7YRT1I(this, windowManager, records));
                }
            }
        }

        public /* synthetic */ void lambda$handleViolationWithTimingAttempt$0$StrictMode$AndroidBlockGuardPolicy(IWindowManager windowManager, ArrayList records) {
            long loopFinishTime = SystemClock.uptimeMillis();
            if (windowManager != null) {
                try {
                    windowManager.showStrictModeViolation(false);
                } catch (RemoteException e) {
                }
            }
            for (int n = 0; n < records.size(); n++) {
                ViolationInfo v = (ViolationInfo) records.get(n);
                v.violationNumThisLoop = n + 1;
                v.durationMillis = (int) (loopFinishTime - v.violationUptimeMillis);
                onThreadPolicyViolation(v);
            }
            records.clear();
        }

        /* Access modifiers changed, original: 0000 */
        public void onThreadPolicyViolation(ViolationInfo info) {
            ViolationInfo violationInfo = info;
            boolean access$900 = StrictMode.LOG_V;
            String str = StrictMode.TAG;
            if (access$900) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onThreadPolicyViolation; penalty=");
                stringBuilder.append(info.mPenaltyMask);
                Log.d(str, stringBuilder.toString());
            }
            boolean z = true;
            if (violationInfo.penaltyEnabled(Integer.MIN_VALUE)) {
                ArrayList<ViolationInfo> violations = (ArrayList) StrictMode.gatheredViolations.get();
                if (violations == null) {
                    violations = new ArrayList(1);
                    StrictMode.gatheredViolations.set(violations);
                }
                Iterator it = violations.iterator();
                while (it.hasNext()) {
                    if (info.getStackTrace().equals(((ViolationInfo) it.next()).getStackTrace())) {
                        return;
                    }
                }
                violations.add(violationInfo);
                return;
            }
            int penaltyMask;
            Integer crashFingerprint = Integer.valueOf(info.hashCode());
            long lastViolationTime = 0;
            ArrayMap arrayMap = this.mLastViolationTime;
            if (arrayMap != null) {
                Long vtime = (Long) arrayMap.get(crashFingerprint);
                if (vtime != null) {
                    lastViolationTime = vtime.longValue();
                }
            } else {
                this.mLastViolationTime = new ArrayMap(1);
            }
            long now = SystemClock.uptimeMillis();
            this.mLastViolationTime.put(crashFingerprint, Long.valueOf(now));
            long timeSinceLastViolationMillis = lastViolationTime == 0 ? Long.MAX_VALUE : now - lastViolationTime;
            if (violationInfo.penaltyEnabled(1073741824) && timeSinceLastViolationMillis > 1000) {
                StrictMode.sLogger.log(violationInfo);
            }
            Violation violation = info.mViolation;
            int penaltyMask2 = 0;
            if (violationInfo.penaltyEnabled(536870912) && timeSinceLastViolationMillis > 30000) {
                penaltyMask2 = 0 | 536870912;
            }
            if (violationInfo.penaltyEnabled(67108864) && lastViolationTime == 0) {
                penaltyMask = penaltyMask2 | 67108864;
            } else {
                penaltyMask = penaltyMask2;
            }
            if (penaltyMask != 0) {
                if (info.mPenaltyMask != 67108864) {
                    z = false;
                }
                if (z) {
                    StrictMode.dropboxViolationAsync(penaltyMask, violationInfo);
                } else {
                    StrictMode.handleApplicationStrictModeViolation(penaltyMask, violationInfo);
                }
            }
            if (violationInfo.penaltyEnabled(268435456)) {
                throw new RuntimeException("StrictMode ThreadPolicy violation", violation);
            }
            OnThreadViolationListener listener = (OnThreadViolationListener) StrictMode.sThreadViolationListener.get();
            Executor executor = (Executor) StrictMode.sThreadViolationExecutor.get();
            if (!(listener == null || executor == null)) {
                try {
                    executor.execute(new -$$Lambda$StrictMode$AndroidBlockGuardPolicy$FxZGA9KtfTewqdcxlUwvIe5Nx9I(listener, violation));
                } catch (RejectedExecutionException e) {
                    Log.e(str, "ThreadPolicy penaltyCallback failed", e);
                }
            }
        }

        static /* synthetic */ void lambda$onThreadPolicyViolation$1(OnThreadViolationListener listener, Violation violation) {
            ThreadPolicy oldPolicy = StrictMode.allowThreadViolations();
            try {
                listener.onThreadViolation(violation);
            } finally {
                StrictMode.setThreadPolicy(oldPolicy);
            }
        }
    }

    private static class AndroidCloseGuardReporter implements Reporter {
        private AndroidCloseGuardReporter() {
        }

        /* synthetic */ AndroidCloseGuardReporter(AnonymousClass1 x0) {
            this();
        }

        public void report(String message, Throwable allocationSite) {
            StrictMode.onVmPolicyViolation(new LeakedClosableViolation(message, allocationSite));
        }
    }

    private static final class InstanceTracker {
        private static final HashMap<Class<?>, Integer> sInstanceCounts = new HashMap();
        private final Class<?> mKlass;

        public InstanceTracker(Object instance) {
            this.mKlass = instance.getClass();
            synchronized (sInstanceCounts) {
                Integer value = (Integer) sInstanceCounts.get(this.mKlass);
                int newValue = 1;
                if (value != null) {
                    newValue = 1 + value.intValue();
                }
                sInstanceCounts.put(this.mKlass, Integer.valueOf(newValue));
            }
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            try {
                synchronized (sInstanceCounts) {
                    Integer value = (Integer) sInstanceCounts.get(this.mKlass);
                    if (value != null) {
                        int newValue = value.intValue() - 1;
                        if (newValue > 0) {
                            sInstanceCounts.put(this.mKlass, Integer.valueOf(newValue));
                        } else {
                            sInstanceCounts.remove(this.mKlass);
                        }
                    }
                }
                super.finalize();
            } catch (Throwable th) {
                super.finalize();
            }
        }

        public static int getInstanceCount(Class<?> klass) {
            int intValue;
            synchronized (sInstanceCounts) {
                Integer value = (Integer) sInstanceCounts.get(klass);
                intValue = value != null ? value.intValue() : 0;
            }
            return intValue;
        }
    }

    public interface OnThreadViolationListener {
        void onThreadViolation(Violation violation);
    }

    public interface OnVmViolationListener {
        void onVmViolation(Violation violation);
    }

    public static final class ThreadPolicy {
        public static final ThreadPolicy LAX = new ThreadPolicy(0, null, null);
        final Executor mCallbackExecutor;
        final OnThreadViolationListener mListener;
        @UnsupportedAppUsage
        final int mask;

        public static final class Builder {
            private Executor mExecutor;
            private OnThreadViolationListener mListener;
            private int mMask;

            public Builder() {
                this.mMask = 0;
                this.mMask = 0;
            }

            public Builder(ThreadPolicy policy) {
                this.mMask = 0;
                this.mMask = policy.mask;
                this.mListener = policy.mListener;
                this.mExecutor = policy.mCallbackExecutor;
            }

            public Builder detectAll() {
                detectDiskReads();
                detectDiskWrites();
                detectNetwork();
                int targetSdk = VMRuntime.getRuntime().getTargetSdkVersion();
                if (targetSdk >= 11) {
                    detectCustomSlowCalls();
                }
                if (targetSdk >= 23) {
                    detectResourceMismatches();
                }
                if (targetSdk >= 26) {
                    detectUnbufferedIo();
                }
                return this;
            }

            public Builder permitAll() {
                return disable(65535);
            }

            public Builder detectNetwork() {
                return enable(4);
            }

            public Builder permitNetwork() {
                return disable(4);
            }

            public Builder detectDiskReads() {
                return enable(2);
            }

            public Builder permitDiskReads() {
                return disable(2);
            }

            public Builder detectCustomSlowCalls() {
                return enable(8);
            }

            public Builder permitCustomSlowCalls() {
                return disable(8);
            }

            public Builder permitResourceMismatches() {
                return disable(16);
            }

            public Builder detectUnbufferedIo() {
                return enable(32);
            }

            public Builder permitUnbufferedIo() {
                return disable(32);
            }

            public Builder detectResourceMismatches() {
                return enable(16);
            }

            public Builder detectDiskWrites() {
                return enable(1);
            }

            public Builder permitDiskWrites() {
                return disable(1);
            }

            public Builder detectExplicitGc() {
                return enable(64);
            }

            public Builder permitExplicitGc() {
                return disable(64);
            }

            public Builder penaltyDialog() {
                return enable(536870912);
            }

            public Builder penaltyDeath() {
                return enable(268435456);
            }

            public Builder penaltyDeathOnNetwork() {
                return enable(33554432);
            }

            public Builder penaltyFlashScreen() {
                return enable(134217728);
            }

            public Builder penaltyLog() {
                return enable(1073741824);
            }

            public Builder penaltyDropBox() {
                return enable(67108864);
            }

            public Builder penaltyListener(Executor executor, OnThreadViolationListener listener) {
                if (executor != null) {
                    this.mListener = listener;
                    this.mExecutor = executor;
                    return this;
                }
                throw new NullPointerException("executor must not be null");
            }

            public Builder penaltyListener(OnThreadViolationListener listener, Executor executor) {
                return penaltyListener(executor, listener);
            }

            private Builder enable(int mask) {
                this.mMask |= mask;
                return this;
            }

            private Builder disable(int mask) {
                this.mMask &= ~mask;
                return this;
            }

            public ThreadPolicy build() {
                if (this.mListener == null) {
                    int i = this.mMask;
                    if (i != 0 && (i & 1946157056) == 0) {
                        penaltyLog();
                    }
                }
                return new ThreadPolicy(this.mMask, this.mListener, this.mExecutor, null);
            }
        }

        /* synthetic */ ThreadPolicy(int x0, OnThreadViolationListener x1, Executor x2, AnonymousClass1 x3) {
            this(x0, x1, x2);
        }

        private ThreadPolicy(int mask, OnThreadViolationListener listener, Executor executor) {
            this.mask = mask;
            this.mListener = listener;
            this.mCallbackExecutor = executor;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[StrictMode.ThreadPolicy; mask=");
            stringBuilder.append(this.mask);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ThreadPolicyMask {
    }

    private static class ThreadSpanState {
        public Span mActiveHead;
        public int mActiveSize;
        public Span mFreeListHead;
        public int mFreeListSize;

        private ThreadSpanState() {
        }

        /* synthetic */ ThreadSpanState(AnonymousClass1 x0) {
            this();
        }
    }

    public static final class ViolationInfo implements Parcelable {
        public static final Creator<ViolationInfo> CREATOR = new Creator<ViolationInfo>() {
            public ViolationInfo createFromParcel(Parcel in) {
                return new ViolationInfo(in);
            }

            public ViolationInfo[] newArray(int size) {
                return new ViolationInfo[size];
            }
        };
        public String broadcastIntentAction;
        public int durationMillis;
        private final Deque<StackTraceElement[]> mBinderStack;
        private final int mPenaltyMask;
        private String mStackTrace;
        private final Violation mViolation;
        public int numAnimationsRunning;
        public long numInstances;
        public String[] tags;
        public int violationNumThisLoop;
        public long violationUptimeMillis;

        ViolationInfo(Violation tr, int penaltyMask) {
            this.mBinderStack = new ArrayDeque();
            this.durationMillis = -1;
            this.numAnimationsRunning = 0;
            this.numInstances = -1;
            this.mViolation = tr;
            this.mPenaltyMask = penaltyMask;
            this.violationUptimeMillis = SystemClock.uptimeMillis();
            this.numAnimationsRunning = ValueAnimator.getCurrentAnimationsCount();
            Intent broadcastIntent = ActivityThread.getIntentBeingBroadcast();
            if (broadcastIntent != null) {
                this.broadcastIntentAction = broadcastIntent.getAction();
            }
            ThreadSpanState state = (ThreadSpanState) StrictMode.sThisThreadSpanState.get();
            if (tr instanceof InstanceCountViolation) {
                this.numInstances = ((InstanceCountViolation) tr).getNumberOfInstances();
            }
            synchronized (state) {
                int spanActiveCount = state.mActiveSize;
                if (spanActiveCount > 20) {
                    spanActiveCount = 20;
                }
                if (spanActiveCount != 0) {
                    this.tags = new String[spanActiveCount];
                    int index = 0;
                    for (Span iter = state.mActiveHead; iter != null && index < spanActiveCount; iter = iter.mNext) {
                        this.tags[index] = iter.mName;
                        index++;
                    }
                }
            }
        }

        public String getStackTrace() {
            if (this.mStackTrace == null) {
                Writer sw = new StringWriter();
                PrintWriter pw = new FastPrintWriter(sw, false, 256);
                this.mViolation.printStackTrace(pw);
                for (StackTraceElement[] traces : this.mBinderStack) {
                    pw.append("# via Binder call with stack:\n");
                    for (StackTraceElement traceElement : traces) {
                        pw.append("\tat ");
                        pw.append(traceElement.toString());
                        pw.append(10);
                    }
                }
                pw.flush();
                pw.close();
                this.mStackTrace = sw.toString();
            }
            return this.mStackTrace;
        }

        public Class<? extends Violation> getViolationClass() {
            return this.mViolation.getClass();
        }

        public String getViolationDetails() {
            return this.mViolation.getMessage();
        }

        /* Access modifiers changed, original: 0000 */
        public boolean penaltyEnabled(int p) {
            return (this.mPenaltyMask & p) != 0;
        }

        /* Access modifiers changed, original: 0000 */
        public void addLocalStack(Throwable t) {
            this.mBinderStack.addFirst(t.getStackTrace());
        }

        public int hashCode() {
            int result = 17;
            Violation violation = this.mViolation;
            if (violation != null) {
                result = (17 * 37) + violation.hashCode();
            }
            if (this.numAnimationsRunning != 0) {
                result *= 37;
            }
            String str = this.broadcastIntentAction;
            if (str != null) {
                result = (result * 37) + str.hashCode();
            }
            String[] strArr = this.tags;
            if (strArr != null) {
                for (String tag : strArr) {
                    result = (result * 37) + tag.hashCode();
                }
            }
            return result;
        }

        public ViolationInfo(Parcel in) {
            this(in, false);
        }

        public ViolationInfo(Parcel in, boolean unsetGatheringBit) {
            int i;
            this.mBinderStack = new ArrayDeque();
            this.durationMillis = -1;
            this.numAnimationsRunning = 0;
            this.numInstances = -1;
            this.mViolation = (Violation) in.readSerializable();
            int binderStackSize = in.readInt();
            for (i = 0; i < binderStackSize; i++) {
                StackTraceElement[] traceElements = new StackTraceElement[in.readInt()];
                for (int j = 0; j < traceElements.length; j++) {
                    traceElements[j] = new StackTraceElement(in.readString(), in.readString(), in.readString(), in.readInt());
                }
                this.mBinderStack.add(traceElements);
            }
            i = in.readInt();
            if (unsetGatheringBit) {
                this.mPenaltyMask = Integer.MAX_VALUE & i;
            } else {
                this.mPenaltyMask = i;
            }
            this.durationMillis = in.readInt();
            this.violationNumThisLoop = in.readInt();
            this.numAnimationsRunning = in.readInt();
            this.violationUptimeMillis = in.readLong();
            this.numInstances = in.readLong();
            this.broadcastIntentAction = in.readString();
            this.tags = in.readStringArray();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeSerializable(this.mViolation);
            dest.writeInt(this.mBinderStack.size());
            for (StackTraceElement[] traceElements : this.mBinderStack) {
                dest.writeInt(traceElements.length);
                for (StackTraceElement element : traceElements) {
                    dest.writeString(element.getClassName());
                    dest.writeString(element.getMethodName());
                    dest.writeString(element.getFileName());
                    dest.writeInt(element.getLineNumber());
                }
            }
            int start = dest.dataPosition();
            dest.writeInt(this.mPenaltyMask);
            dest.writeInt(this.durationMillis);
            dest.writeInt(this.violationNumThisLoop);
            dest.writeInt(this.numAnimationsRunning);
            dest.writeLong(this.violationUptimeMillis);
            dest.writeLong(this.numInstances);
            dest.writeString(this.broadcastIntentAction);
            dest.writeStringArray(this.tags);
            int total = dest.dataPosition() - start;
        }

        public void dump(Printer pw, String prefix) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("stackTrace: ");
            stringBuilder.append(getStackTrace());
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("penalty: ");
            stringBuilder.append(this.mPenaltyMask);
            pw.println(stringBuilder.toString());
            if (this.durationMillis != -1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(prefix);
                stringBuilder.append("durationMillis: ");
                stringBuilder.append(this.durationMillis);
                pw.println(stringBuilder.toString());
            }
            if (this.numInstances != -1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(prefix);
                stringBuilder.append("numInstances: ");
                stringBuilder.append(this.numInstances);
                pw.println(stringBuilder.toString());
            }
            if (this.violationNumThisLoop != 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(prefix);
                stringBuilder.append("violationNumThisLoop: ");
                stringBuilder.append(this.violationNumThisLoop);
                pw.println(stringBuilder.toString());
            }
            if (this.numAnimationsRunning != 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(prefix);
                stringBuilder.append("numAnimationsRunning: ");
                stringBuilder.append(this.numAnimationsRunning);
                pw.println(stringBuilder.toString());
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("violationUptimeMillis: ");
            stringBuilder.append(this.violationUptimeMillis);
            pw.println(stringBuilder.toString());
            if (this.broadcastIntentAction != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(prefix);
                stringBuilder.append("broadcastIntentAction: ");
                stringBuilder.append(this.broadcastIntentAction);
                pw.println(stringBuilder.toString());
            }
            String[] strArr = this.tags;
            if (strArr != null) {
                int index = 0;
                int length = strArr.length;
                int i = 0;
                while (i < length) {
                    String tag = strArr[i];
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(prefix);
                    stringBuilder2.append("tag[");
                    int index2 = index + 1;
                    stringBuilder2.append(index);
                    stringBuilder2.append("]: ");
                    stringBuilder2.append(tag);
                    pw.println(stringBuilder2.toString());
                    i++;
                    index = index2;
                }
            }
        }

        public int describeContents() {
            return 0;
        }
    }

    public static final class VmPolicy {
        public static final VmPolicy LAX = new VmPolicy(0, StrictMode.EMPTY_CLASS_LIMIT_MAP, null, null);
        final HashMap<Class, Integer> classInstanceLimit;
        final Executor mCallbackExecutor;
        final OnVmViolationListener mListener;
        @UnsupportedAppUsage
        final int mask;

        public static final class Builder {
            private HashMap<Class, Integer> mClassInstanceLimit;
            private boolean mClassInstanceLimitNeedCow;
            private Executor mExecutor;
            private OnVmViolationListener mListener;
            @UnsupportedAppUsage
            private int mMask;

            public Builder() {
                this.mClassInstanceLimitNeedCow = false;
                this.mMask = 0;
            }

            public Builder(VmPolicy base) {
                this.mClassInstanceLimitNeedCow = false;
                this.mMask = base.mask;
                this.mClassInstanceLimitNeedCow = true;
                this.mClassInstanceLimit = base.classInstanceLimit;
                this.mListener = base.mListener;
                this.mExecutor = base.mCallbackExecutor;
            }

            public Builder setClassInstanceLimit(Class klass, int instanceLimit) {
                if (klass != null) {
                    if (this.mClassInstanceLimitNeedCow) {
                        if (this.mClassInstanceLimit.containsKey(klass) && ((Integer) this.mClassInstanceLimit.get(klass)).intValue() == instanceLimit) {
                            return this;
                        }
                        this.mClassInstanceLimitNeedCow = false;
                        this.mClassInstanceLimit = (HashMap) this.mClassInstanceLimit.clone();
                    } else if (this.mClassInstanceLimit == null) {
                        this.mClassInstanceLimit = new HashMap();
                    }
                    this.mMask |= 8;
                    this.mClassInstanceLimit.put(klass, Integer.valueOf(instanceLimit));
                    return this;
                }
                throw new NullPointerException("klass == null");
            }

            public Builder detectActivityLeaks() {
                return enable(4);
            }

            public Builder permitActivityLeaks() {
                return disable(4);
            }

            public Builder detectNonSdkApiUsage() {
                return enable(512);
            }

            public Builder permitNonSdkApiUsage() {
                return disable(512);
            }

            public Builder detectAll() {
                detectLeakedSqlLiteObjects();
                int targetSdk = VMRuntime.getRuntime().getTargetSdkVersion();
                if (targetSdk >= 11) {
                    detectActivityLeaks();
                    detectLeakedClosableObjects();
                }
                if (targetSdk >= 16) {
                    detectLeakedRegistrationObjects();
                }
                if (targetSdk >= 18) {
                    detectFileUriExposure();
                }
                if (targetSdk >= 23 && SystemProperties.getBoolean(StrictMode.CLEARTEXT_PROPERTY, false)) {
                    detectCleartextNetwork();
                }
                if (targetSdk >= 26) {
                    detectContentUriWithoutPermission();
                    detectUntaggedSockets();
                }
                if (targetSdk >= 29) {
                    detectCredentialProtectedWhileLocked();
                }
                return this;
            }

            public Builder detectLeakedSqlLiteObjects() {
                return enable(1);
            }

            public Builder detectLeakedClosableObjects() {
                return enable(2);
            }

            public Builder detectLeakedRegistrationObjects() {
                return enable(16);
            }

            public Builder detectFileUriExposure() {
                return enable(32);
            }

            public Builder detectCleartextNetwork() {
                return enable(64);
            }

            public Builder detectContentUriWithoutPermission() {
                return enable(128);
            }

            public Builder detectUntaggedSockets() {
                return enable(256);
            }

            public Builder permitUntaggedSockets() {
                return disable(256);
            }

            public Builder detectImplicitDirectBoot() {
                return enable(1024);
            }

            public Builder permitImplicitDirectBoot() {
                return disable(1024);
            }

            public Builder detectCredentialProtectedWhileLocked() {
                return enable(2048);
            }

            public Builder permitCredentialProtectedWhileLocked() {
                return disable(2048);
            }

            public Builder penaltyDeath() {
                return enable(268435456);
            }

            public Builder penaltyDeathOnCleartextNetwork() {
                return enable(16777216);
            }

            public Builder penaltyDeathOnFileUriExposure() {
                return enable(8388608);
            }

            public Builder penaltyLog() {
                return enable(1073741824);
            }

            public Builder penaltyDropBox() {
                return enable(67108864);
            }

            public Builder penaltyListener(Executor executor, OnVmViolationListener listener) {
                if (executor != null) {
                    this.mListener = listener;
                    this.mExecutor = executor;
                    return this;
                }
                throw new NullPointerException("executor must not be null");
            }

            public Builder penaltyListener(OnVmViolationListener listener, Executor executor) {
                return penaltyListener(executor, listener);
            }

            private Builder enable(int mask) {
                this.mMask |= mask;
                return this;
            }

            /* Access modifiers changed, original: 0000 */
            public Builder disable(int mask) {
                this.mMask &= ~mask;
                return this;
            }

            public VmPolicy build() {
                if (this.mListener == null) {
                    int i = this.mMask;
                    if (i != 0 && (i & 1946157056) == 0) {
                        penaltyLog();
                    }
                }
                int i2 = this.mMask;
                HashMap hashMap = this.mClassInstanceLimit;
                if (hashMap == null) {
                    hashMap = StrictMode.EMPTY_CLASS_LIMIT_MAP;
                }
                return new VmPolicy(i2, hashMap, this.mListener, this.mExecutor, null);
            }
        }

        /* synthetic */ VmPolicy(int x0, HashMap x1, OnVmViolationListener x2, Executor x3, AnonymousClass1 x4) {
            this(x0, x1, x2, x3);
        }

        private VmPolicy(int mask, HashMap<Class, Integer> classInstanceLimit, OnVmViolationListener listener, Executor executor) {
            if (classInstanceLimit != null) {
                this.mask = mask;
                this.classInstanceLimit = classInstanceLimit;
                this.mListener = listener;
                this.mCallbackExecutor = executor;
                return;
            }
            throw new NullPointerException("classInstanceLimit == null");
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[StrictMode.VmPolicy; mask=");
            stringBuilder.append(this.mask);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface VmPolicyMask {
    }

    static /* synthetic */ void lambda$static$0(ViolationInfo info) {
        String msg;
        if (info.durationMillis != -1) {
            msg = new StringBuilder();
            msg.append("StrictMode policy violation; ~duration=");
            msg.append(info.durationMillis);
            msg.append(" ms:");
            msg = msg.toString();
        } else {
            msg = "StrictMode policy violation:";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(msg);
        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        stringBuilder.append(info.getStackTrace());
        Log.d(TAG, stringBuilder.toString());
    }

    public static void setViolationLogger(ViolationLogger listener) {
        if (listener == null) {
            listener = LOGCAT_LOGGER;
        }
        sLogger = listener;
    }

    private StrictMode() {
    }

    public static void setThreadPolicy(ThreadPolicy policy) {
        setThreadPolicyMask(policy.mask);
        sThreadViolationListener.set(policy.mListener);
        sThreadViolationExecutor.set(policy.mCallbackExecutor);
    }

    public static void setThreadPolicyMask(int threadPolicyMask) {
        setBlockGuardPolicy(threadPolicyMask);
        Binder.setThreadStrictModePolicy(threadPolicyMask);
    }

    private static void setBlockGuardPolicy(int threadPolicyMask) {
        if (threadPolicyMask == 0) {
            BlockGuard.setThreadPolicy(BlockGuard.LAX_POLICY);
            return;
        }
        AndroidBlockGuardPolicy androidPolicy;
        Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            androidPolicy = (AndroidBlockGuardPolicy) policy;
        } else {
            androidPolicy = (AndroidBlockGuardPolicy) THREAD_ANDROID_POLICY.get();
            BlockGuard.setThreadPolicy(androidPolicy);
        }
        androidPolicy.setThreadPolicyMask(threadPolicyMask);
    }

    private static void setBlockGuardVmPolicy(int vmPolicyMask) {
        if ((vmPolicyMask & 2048) != 0) {
            BlockGuard.setVmPolicy(VM_ANDROID_POLICY);
        } else {
            BlockGuard.setVmPolicy(BlockGuard.LAX_VM_POLICY);
        }
    }

    private static void setCloseGuardEnabled(boolean enabled) {
        if (!(CloseGuard.getReporter() instanceof AndroidCloseGuardReporter)) {
            CloseGuard.setReporter(new AndroidCloseGuardReporter());
        }
        CloseGuard.setEnabled(enabled);
    }

    @UnsupportedAppUsage
    public static int getThreadPolicyMask() {
        Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            return ((AndroidBlockGuardPolicy) policy).getThreadPolicyMask();
        }
        return 0;
    }

    public static ThreadPolicy getThreadPolicy() {
        return new ThreadPolicy(getThreadPolicyMask(), (OnThreadViolationListener) sThreadViolationListener.get(), (Executor) sThreadViolationExecutor.get(), null);
    }

    public static ThreadPolicy allowThreadDiskWrites() {
        return new ThreadPolicy(allowThreadDiskWritesMask(), (OnThreadViolationListener) sThreadViolationListener.get(), (Executor) sThreadViolationExecutor.get(), null);
    }

    public static int allowThreadDiskWritesMask() {
        int oldPolicyMask = getThreadPolicyMask();
        int newPolicyMask = oldPolicyMask & -4;
        if (newPolicyMask != oldPolicyMask) {
            setThreadPolicyMask(newPolicyMask);
        }
        return oldPolicyMask;
    }

    public static ThreadPolicy allowThreadDiskReads() {
        return new ThreadPolicy(allowThreadDiskReadsMask(), (OnThreadViolationListener) sThreadViolationListener.get(), (Executor) sThreadViolationExecutor.get(), null);
    }

    public static int allowThreadDiskReadsMask() {
        int oldPolicyMask = getThreadPolicyMask();
        int newPolicyMask = oldPolicyMask & -3;
        if (newPolicyMask != oldPolicyMask) {
            setThreadPolicyMask(newPolicyMask);
        }
        return oldPolicyMask;
    }

    public static ThreadPolicy allowThreadViolations() {
        ThreadPolicy oldPolicy = getThreadPolicy();
        setThreadPolicyMask(0);
        return oldPolicy;
    }

    public static VmPolicy allowVmViolations() {
        VmPolicy oldPolicy = getVmPolicy();
        sVmPolicy = VmPolicy.LAX;
        return oldPolicy;
    }

    /* JADX WARNING: Missing block: B:20:0x004e, code skipped:
            return false;
     */
    public static boolean isBundledSystemApp(android.content.pm.ApplicationInfo r4) {
        /*
        r0 = 1;
        if (r4 == 0) goto L_0x0050;
    L_0x0003:
        r1 = r4.packageName;
        if (r1 != 0) goto L_0x0008;
    L_0x0007:
        goto L_0x0050;
    L_0x0008:
        r1 = r4.isSystemApp();
        r2 = 0;
        if (r1 == 0) goto L_0x004f;
    L_0x000f:
        r1 = r4.packageName;
        r3 = "com.android.vending";
        r1 = r1.equals(r3);
        if (r1 != 0) goto L_0x004e;
    L_0x0019:
        r1 = r4.packageName;
        r3 = "com.android.chrome";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0024;
    L_0x0023:
        goto L_0x004e;
    L_0x0024:
        r1 = r4.packageName;
        r3 = "com.android.phone";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x002f;
    L_0x002e:
        return r2;
    L_0x002f:
        r1 = r4.packageName;
        r3 = "android";
        r1 = r1.equals(r3);
        if (r1 != 0) goto L_0x004d;
    L_0x0039:
        r1 = r4.packageName;
        r3 = "android.";
        r1 = r1.startsWith(r3);
        if (r1 != 0) goto L_0x004d;
    L_0x0043:
        r1 = r4.packageName;
        r3 = "com.android.";
        r1 = r1.startsWith(r3);
        if (r1 == 0) goto L_0x004f;
    L_0x004d:
        return r0;
    L_0x004e:
        return r2;
    L_0x004f:
        return r2;
    L_0x0050:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.StrictMode.isBundledSystemApp(android.content.pm.ApplicationInfo):boolean");
    }

    public static void initThreadDefaults(ApplicationInfo ai) {
        Builder builder = new Builder();
        if ((ai != null ? ai.targetSdkVersion : 10000) >= 11) {
            builder.detectNetwork();
            builder.penaltyDeathOnNetwork();
        }
        if (!(Build.IS_USER || SystemProperties.getBoolean(DISABLE_PROPERTY, false))) {
            if (Build.IS_USERDEBUG) {
                if (isBundledSystemApp(ai)) {
                    builder.detectAll();
                    builder.penaltyDropBox();
                    if (SystemProperties.getBoolean(VISUAL_PROPERTY, false)) {
                        builder.penaltyFlashScreen();
                    }
                }
            } else if (Build.IS_ENG && isBundledSystemApp(ai)) {
                builder.detectAll();
                builder.penaltyDropBox();
                builder.penaltyLog();
                builder.penaltyFlashScreen();
            }
        }
        setThreadPolicy(builder.build());
    }

    public static void initVmDefaults(ApplicationInfo ai) {
        Builder builder = new Builder();
        if ((ai != null ? ai.targetSdkVersion : 10000) >= 24) {
            builder.detectFileUriExposure();
            builder.penaltyDeathOnFileUriExposure();
        }
        if (!(Build.IS_USER || SystemProperties.getBoolean(DISABLE_PROPERTY, false))) {
            if (Build.IS_USERDEBUG) {
                if (isBundledSystemApp(ai)) {
                    builder.detectAll();
                    builder.permitActivityLeaks();
                    builder.penaltyDropBox();
                }
            } else if (Build.IS_ENG && isBundledSystemApp(ai)) {
                builder.detectAll();
                builder.penaltyDropBox();
                builder.penaltyLog();
            }
        }
        setVmPolicy(builder.build());
    }

    @UnsupportedAppUsage
    public static void enableDeathOnFileUriExposure() {
        sVmPolicy = new VmPolicy(8388608 | (sVmPolicy.mask | 32), sVmPolicy.classInstanceLimit, sVmPolicy.mListener, sVmPolicy.mCallbackExecutor, null);
    }

    @UnsupportedAppUsage
    public static void disableDeathOnFileUriExposure() {
        sVmPolicy = new VmPolicy(-8388641 & sVmPolicy.mask, sVmPolicy.classInstanceLimit, sVmPolicy.mListener, sVmPolicy.mCallbackExecutor, null);
    }

    private static boolean tooManyViolationsThisLoop() {
        return ((ArrayList) violationsBeingTimed.get()).size() >= 10;
    }

    private static void dropboxViolationAsync(int penaltyMask, ViolationInfo info) {
        int outstanding = sDropboxCallsInFlight.incrementAndGet();
        if (outstanding > 20) {
            sDropboxCallsInFlight.decrementAndGet();
            return;
        }
        if (LOG_V) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Dropboxing async; in-flight=");
            stringBuilder.append(outstanding);
            Log.d(TAG, stringBuilder.toString());
        }
        BackgroundThread.getHandler().post(new -$$Lambda$StrictMode$yZJXPvy2veRNA-xL_SWdXzX_OLg(penaltyMask, info));
    }

    static /* synthetic */ void lambda$dropboxViolationAsync$2(int penaltyMask, ViolationInfo info) {
        handleApplicationStrictModeViolation(penaltyMask, info);
        int outstandingInner = sDropboxCallsInFlight.decrementAndGet();
        if (LOG_V) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Dropbox complete; in-flight=");
            stringBuilder.append(outstandingInner);
            Log.d(TAG, stringBuilder.toString());
        }
    }

    private static void handleApplicationStrictModeViolation(int penaltyMask, ViolationInfo info) {
        String str = TAG;
        int oldMask = getThreadPolicyMask();
        try {
            setThreadPolicyMask(0);
            IActivityManager am = ActivityManager.getService();
            if (am == null) {
                Log.w(str, "No activity manager; failed to Dropbox violation.");
            } else {
                am.handleApplicationStrictModeViolation(RuntimeInit.getApplicationObject(), penaltyMask, info);
            }
        } catch (RemoteException e) {
            if (!(e instanceof DeadObjectException)) {
                Log.e(str, "RemoteException handling StrictMode violation", e);
            }
        } catch (Throwable th) {
            setThreadPolicyMask(oldMask);
        }
        setThreadPolicyMask(oldMask);
    }

    static boolean hasGatheredViolations() {
        return gatheredViolations.get() != null;
    }

    static void clearGatheredViolations() {
        gatheredViolations.set(null);
    }

    public static void conditionallyCheckInstanceCounts() {
        VmPolicy policy = getVmPolicy();
        int policySize = policy.classInstanceLimit.size();
        if (policySize != 0) {
            System.gc();
            System.runFinalization();
            System.gc();
            Class[] classes = (Class[]) policy.classInstanceLimit.keySet().toArray(new Class[policySize]);
            long[] instanceCounts = VMDebug.countInstancesOfClasses(classes, null);
            for (int i = 0; i < classes.length; i++) {
                Class klass = classes[i];
                int limit = ((Integer) policy.classInstanceLimit.get(klass)).intValue();
                long instances = instanceCounts[i];
                if (instances > ((long) limit)) {
                    onVmPolicyViolation(new InstanceCountViolation(klass, instances, limit));
                }
            }
        }
    }

    public static void setVmPolicy(VmPolicy policy) {
        synchronized (StrictMode.class) {
            sVmPolicy = policy;
            setCloseGuardEnabled(vmClosableObjectLeaksEnabled());
            Looper looper = Looper.getMainLooper();
            if (looper != null) {
                MessageQueue mq = looper.mQueue;
                if (policy.classInstanceLimit.size() != 0) {
                    if ((sVmPolicy.mask & -65536) != 0) {
                        if (!sIsIdlerRegistered) {
                            mq.addIdleHandler(sProcessIdleHandler);
                            sIsIdlerRegistered = true;
                        }
                    }
                }
                mq.removeIdleHandler(sProcessIdleHandler);
                sIsIdlerRegistered = false;
            }
            int networkPolicy = 0;
            if ((sVmPolicy.mask & 64) != 0) {
                if ((sVmPolicy.mask & 268435456) == 0) {
                    if ((sVmPolicy.mask & 16777216) == 0) {
                        networkPolicy = 1;
                    }
                }
                networkPolicy = 2;
            }
            INetworkManagementService netd = INetworkManagementService.Stub.asInterface(ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE));
            if (netd != null) {
                try {
                    netd.setUidCleartextNetworkPolicy(Process.myUid(), networkPolicy);
                } catch (RemoteException e) {
                }
            } else if (networkPolicy != 0) {
                Log.w(TAG, "Dropping requested network policy due to missing service!");
            }
            if ((sVmPolicy.mask & 512) != 0) {
                VMRuntime.setNonSdkApiUsageConsumer(sNonSdkApiUsageConsumer);
                VMRuntime.setDedupeHiddenApiWarnings(false);
            } else {
                VMRuntime.setNonSdkApiUsageConsumer(null);
                VMRuntime.setDedupeHiddenApiWarnings(true);
            }
            setBlockGuardVmPolicy(sVmPolicy.mask);
        }
    }

    public static VmPolicy getVmPolicy() {
        VmPolicy vmPolicy;
        synchronized (StrictMode.class) {
            vmPolicy = sVmPolicy;
        }
        return vmPolicy;
    }

    public static void enableDefaults() {
        setThreadPolicy(new Builder().detectAll().penaltyLog().build());
        setVmPolicy(new Builder().detectAll().penaltyLog().build());
    }

    public static boolean vmSqliteObjectLeaksEnabled() {
        return (sVmPolicy.mask & 1) != 0;
    }

    public static boolean vmClosableObjectLeaksEnabled() {
        return (sVmPolicy.mask & 2) != 0;
    }

    public static boolean vmRegistrationLeaksEnabled() {
        return (sVmPolicy.mask & 16) != 0;
    }

    public static boolean vmFileUriExposureEnabled() {
        return (sVmPolicy.mask & 32) != 0;
    }

    public static boolean vmCleartextNetworkEnabled() {
        return (sVmPolicy.mask & 64) != 0;
    }

    public static boolean vmContentUriWithoutPermissionEnabled() {
        return (sVmPolicy.mask & 128) != 0;
    }

    public static boolean vmUntaggedSocketEnabled() {
        return (sVmPolicy.mask & 256) != 0;
    }

    public static boolean vmImplicitDirectBootEnabled() {
        return (sVmPolicy.mask & 1024) != 0;
    }

    public static boolean vmCredentialProtectedWhileLockedEnabled() {
        return (sVmPolicy.mask & 2048) != 0;
    }

    public static void onSqliteObjectLeaked(String message, Throwable originStack) {
        onVmPolicyViolation(new SqliteObjectLeakedViolation(message, originStack));
    }

    @UnsupportedAppUsage
    public static void onWebViewMethodCalledOnWrongThread(Throwable originStack) {
        onVmPolicyViolation(new WebViewMethodCalledOnWrongThreadViolation(originStack));
    }

    public static void onIntentReceiverLeaked(Throwable originStack) {
        onVmPolicyViolation(new IntentReceiverLeakedViolation(originStack));
    }

    public static void onServiceConnectionLeaked(Throwable originStack) {
        onVmPolicyViolation(new ServiceConnectionLeakedViolation(originStack));
    }

    public static void onFileUriExposed(Uri uri, String location) {
        String message = new StringBuilder();
        message.append(uri);
        message.append(" exposed beyond app through ");
        message.append(location);
        message = message.toString();
        if ((sVmPolicy.mask & 8388608) == 0) {
            onVmPolicyViolation(new FileUriExposedViolation(message));
            return;
        }
        throw new FileUriExposedException(message);
    }

    public static void onContentUriWithoutPermission(Uri uri, String location) {
        onVmPolicyViolation(new ContentUriWithoutPermissionViolation(uri, location));
    }

    public static void onCleartextNetworkDetected(byte[] firstPacket) {
        StringBuilder stringBuilder;
        byte[] rawAddr = null;
        boolean forceDeath = false;
        if (firstPacket != null) {
            if (firstPacket.length >= 20 && (firstPacket[0] & 240) == 64) {
                rawAddr = new byte[4];
                System.arraycopy(firstPacket, 16, rawAddr, 0, 4);
            } else if (firstPacket.length >= 40 && (firstPacket[0] & 240) == 96) {
                rawAddr = new byte[16];
                System.arraycopy(firstPacket, 24, rawAddr, 0, 16);
            }
        }
        int uid = Process.myUid();
        String msg = new StringBuilder();
        msg.append("Detected cleartext network traffic from UID ");
        msg.append(uid);
        msg = msg.toString();
        if (rawAddr != null) {
            try {
                stringBuilder = new StringBuilder();
                stringBuilder.append(msg);
                stringBuilder.append(" to ");
                stringBuilder.append(InetAddress.getByAddress(rawAddr));
                msg = stringBuilder.toString();
            } catch (UnknownHostException e) {
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(msg);
        stringBuilder.append(HexDump.dumpHexString(firstPacket).trim());
        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        msg = stringBuilder.toString();
        if ((sVmPolicy.mask & 16777216) != 0) {
            forceDeath = true;
        }
        onVmPolicyViolation(new CleartextNetworkViolation(msg), forceDeath);
    }

    public static void onUntaggedSocket() {
        onVmPolicyViolation(new UntaggedSocketViolation());
    }

    public static void onImplicitDirectBoot() {
        onVmPolicyViolation(new ImplicitDirectBootViolation());
    }

    private static boolean isUserKeyUnlocked(int userId) {
        IStorageManager storage = IStorageManager.Stub.asInterface(ServiceManager.getService("mount"));
        if (storage != null) {
            try {
                return storage.isUserKeyUnlocked(userId);
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    private static void onCredentialProtectedPathAccess(String path, int userId) {
        if (userId == UserHandle.myUserId()) {
            if (!sUserKeyUnlocked) {
                if (isUserKeyUnlocked(userId)) {
                    sUserKeyUnlocked = true;
                    return;
                }
            }
            return;
        } else if (isUserKeyUnlocked(userId)) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Accessed credential protected path ");
        stringBuilder.append(path);
        stringBuilder.append(" while user ");
        stringBuilder.append(userId);
        stringBuilder.append(" was locked");
        onVmPolicyViolation(new CredentialProtectedWhileLockedViolation(stringBuilder.toString()));
    }

    public static void onVmPolicyViolation(Violation originStack) {
        onVmPolicyViolation(originStack, false);
    }

    public static void onVmPolicyViolation(Violation violation, boolean forceDeath) {
        Violation violation2 = violation;
        boolean penaltyLog = true;
        boolean penaltyDropbox = (sVmPolicy.mask & 67108864) != 0;
        boolean z = (sVmPolicy.mask & 268435456) != 0 || forceDeath;
        boolean penaltyDeath = z;
        if ((sVmPolicy.mask & 1073741824) == 0) {
            penaltyLog = false;
        }
        ViolationInfo info = new ViolationInfo(violation2, -65536 & sVmPolicy.mask);
        info.numAnimationsRunning = 0;
        info.tags = null;
        info.broadcastIntentAction = null;
        Integer fingerprint = Integer.valueOf(info.hashCode());
        long now = SystemClock.uptimeMillis();
        long timeSinceLastViolationMillis = Long.MAX_VALUE;
        synchronized (sLastVmViolationTime) {
            if (sLastVmViolationTime.containsKey(fingerprint)) {
                timeSinceLastViolationMillis = now - ((Long) sLastVmViolationTime.get(fingerprint)).longValue();
            }
            if (timeSinceLastViolationMillis > 1000) {
                sLastVmViolationTime.put(fingerprint, Long.valueOf(now));
            }
        }
        if (timeSinceLastViolationMillis > 1000) {
            if (penaltyLog && sLogger != null && timeSinceLastViolationMillis > 1000) {
                sLogger.log(info);
            }
            if (penaltyDropbox) {
                if (penaltyDeath) {
                    handleApplicationStrictModeViolation(67108864, info);
                } else {
                    dropboxViolationAsync(67108864, info);
                }
            }
            if (penaltyDeath) {
                System.err.println("StrictMode VmPolicy violation with POLICY_DEATH; shutting down.");
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
            if (!(sVmPolicy.mListener == null || sVmPolicy.mCallbackExecutor == null)) {
                try {
                    sVmPolicy.mCallbackExecutor.execute(new -$$Lambda$StrictMode$UFC_nI1x6u8ZwMQmA7bmj9NHZz4(sVmPolicy.mListener, violation2));
                } catch (RejectedExecutionException e) {
                    Log.e(TAG, "VmPolicy penaltyCallback failed", e);
                }
            }
        }
    }

    static /* synthetic */ void lambda$onVmPolicyViolation$3(OnVmViolationListener listener, Violation violation) {
        VmPolicy oldPolicy = allowVmViolations();
        try {
            listener.onVmViolation(violation);
        } finally {
            setVmPolicy(oldPolicy);
        }
    }

    static void writeGatheredViolationsToParcel(Parcel p) {
        ArrayList<ViolationInfo> violations = (ArrayList) gatheredViolations.get();
        if (violations == null) {
            p.writeInt(0);
        } else {
            int size = Math.min(violations.size(), 3);
            p.writeInt(size);
            for (int i = 0; i < size; i++) {
                ((ViolationInfo) violations.get(i)).writeToParcel(p, 0);
            }
        }
        gatheredViolations.set(null);
    }

    static void readAndHandleBinderCallViolations(Parcel p) {
        Throwable localCallSite = new Throwable();
        boolean currentlyGathering = (Integer.MIN_VALUE & getThreadPolicyMask()) != 0;
        int size = p.readInt();
        for (int i = 0; i < size; i++) {
            ViolationInfo info = new ViolationInfo(p, !currentlyGathering);
            info.addLocalStack(localCallSite);
            Policy policy = BlockGuard.getThreadPolicy();
            if (policy instanceof AndroidBlockGuardPolicy) {
                ((AndroidBlockGuardPolicy) policy).handleViolationWithTimingAttempt(info);
            }
        }
    }

    @UnsupportedAppUsage
    private static void onBinderStrictModePolicyChange(int newPolicy) {
        setBlockGuardPolicy(newPolicy);
    }

    @UnsupportedAppUsage
    public static Span enterCriticalSpan(String name) {
        if (Build.IS_USER) {
            return NO_OP_SPAN;
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name must be non-null and non-empty");
        }
        Span span;
        ThreadSpanState state = (ThreadSpanState) sThisThreadSpanState.get();
        synchronized (state) {
            if (state.mFreeListHead != null) {
                span = state.mFreeListHead;
                state.mFreeListHead = span.mNext;
                state.mFreeListSize--;
            } else {
                span = new Span(state);
            }
            span.mName = name;
            span.mCreateMillis = SystemClock.uptimeMillis();
            span.mNext = state.mActiveHead;
            span.mPrev = null;
            state.mActiveHead = span;
            state.mActiveSize++;
            if (span.mNext != null) {
                span.mNext.mPrev = span;
            }
            if (LOG_V) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Span enter=");
                stringBuilder.append(name);
                stringBuilder.append("; size=");
                stringBuilder.append(state.mActiveSize);
                Log.d(str, stringBuilder.toString());
            }
        }
        return span;
    }

    public static void noteSlowCall(String name) {
        Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            ((AndroidBlockGuardPolicy) policy).onCustomSlowCall(name);
        }
    }

    public static void noteResourceMismatch(Object tag) {
        Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            ((AndroidBlockGuardPolicy) policy).onResourceMismatch(tag);
        }
    }

    public static void noteUnbufferedIO() {
        Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            policy.onUnbufferedIO();
        }
    }

    public static void noteDiskRead() {
        Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            policy.onReadFromDisk();
        }
    }

    public static void noteDiskWrite() {
        Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            policy.onWriteToDisk();
        }
    }

    public static Object trackActivity(Object instance) {
        return new InstanceTracker(instance);
    }

    @UnsupportedAppUsage
    public static void incrementExpectedActivityCount(Class klass) {
        if (klass != null) {
            synchronized (StrictMode.class) {
                if ((sVmPolicy.mask & 4) == 0) {
                    return;
                }
                Integer expected = (Integer) sExpectedActivityInstanceCount.get(klass);
                Integer newExpected = true;
                if (expected != null) {
                    newExpected = 1 + expected.intValue();
                }
                sExpectedActivityInstanceCount.put(klass, Integer.valueOf(newExpected));
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0032  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x002c  */
    /* JADX WARNING: Missing block: B:22:0x0042, code skipped:
            if (android.os.StrictMode.InstanceTracker.getInstanceCount(r6) > r3) goto L_0x0045;
     */
    /* JADX WARNING: Missing block: B:23:0x0044, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:24:0x0045, code skipped:
            java.lang.System.gc();
            java.lang.System.runFinalization();
            java.lang.System.gc();
            r1 = dalvik.system.VMDebug.countInstancesOfClass(r6, false);
     */
    /* JADX WARNING: Missing block: B:25:0x0055, code skipped:
            if (r1 <= ((long) r3)) goto L_0x005f;
     */
    /* JADX WARNING: Missing block: B:26:0x0057, code skipped:
            onVmPolicyViolation(new android.os.strictmode.InstanceCountViolation(r6, r1, r3));
     */
    /* JADX WARNING: Missing block: B:27:0x005f, code skipped:
            return;
     */
    public static void decrementExpectedActivityCount(java.lang.Class r6) {
        /*
        if (r6 != 0) goto L_0x0003;
    L_0x0002:
        return;
    L_0x0003:
        r0 = android.os.StrictMode.class;
        monitor-enter(r0);
        r1 = sVmPolicy;	 Catch:{ all -> 0x0060 }
        r1 = r1.mask;	 Catch:{ all -> 0x0060 }
        r1 = r1 & 4;
        if (r1 != 0) goto L_0x0010;
    L_0x000e:
        monitor-exit(r0);	 Catch:{ all -> 0x0060 }
        return;
    L_0x0010:
        r1 = sExpectedActivityInstanceCount;	 Catch:{ all -> 0x0060 }
        r1 = r1.get(r6);	 Catch:{ all -> 0x0060 }
        r1 = (java.lang.Integer) r1;	 Catch:{ all -> 0x0060 }
        r2 = 0;
        if (r1 == 0) goto L_0x0029;
    L_0x001b:
        r3 = r1.intValue();	 Catch:{ all -> 0x0060 }
        if (r3 != 0) goto L_0x0022;
    L_0x0021:
        goto L_0x0029;
    L_0x0022:
        r3 = r1.intValue();	 Catch:{ all -> 0x0060 }
        r3 = r3 + -1;
        goto L_0x002a;
    L_0x0029:
        r3 = r2;
    L_0x002a:
        if (r3 != 0) goto L_0x0032;
    L_0x002c:
        r4 = sExpectedActivityInstanceCount;	 Catch:{ all -> 0x0060 }
        r4.remove(r6);	 Catch:{ all -> 0x0060 }
        goto L_0x003b;
    L_0x0032:
        r4 = sExpectedActivityInstanceCount;	 Catch:{ all -> 0x0060 }
        r5 = java.lang.Integer.valueOf(r3);	 Catch:{ all -> 0x0060 }
        r4.put(r6, r5);	 Catch:{ all -> 0x0060 }
    L_0x003b:
        r3 = r3 + 1;
        monitor-exit(r0);	 Catch:{ all -> 0x0060 }
        r0 = android.os.StrictMode.InstanceTracker.getInstanceCount(r6);
        if (r0 > r3) goto L_0x0045;
    L_0x0044:
        return;
    L_0x0045:
        java.lang.System.gc();
        java.lang.System.runFinalization();
        java.lang.System.gc();
        r1 = dalvik.system.VMDebug.countInstancesOfClass(r6, r2);
        r4 = (long) r3;
        r4 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1));
        if (r4 <= 0) goto L_0x005f;
    L_0x0057:
        r4 = new android.os.strictmode.InstanceCountViolation;
        r4.<init>(r6, r1, r3);
        onVmPolicyViolation(r4);
    L_0x005f:
        return;
    L_0x0060:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0060 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.StrictMode.decrementExpectedActivityCount(java.lang.Class):void");
    }
}
