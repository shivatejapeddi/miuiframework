package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.IApplicationThread.Stub;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.app.backup.BackupAgent;
import android.app.backup.FullBackup;
import android.app.servertransaction.ActivityLifecycleItem;
import android.app.servertransaction.ActivityRelaunchItem;
import android.app.servertransaction.ActivityResultItem;
import android.app.servertransaction.ClientTransaction;
import android.app.servertransaction.ClientTransactionItem;
import android.app.servertransaction.PendingTransactionActions;
import android.app.servertransaction.PendingTransactionActions.StopInfo;
import android.app.servertransaction.TransactionExecutor;
import android.app.servertransaction.TransactionExecutorHelper;
import android.app.slice.Slice;
import android.content.AutofillOptions;
import android.content.BroadcastReceiver;
import android.content.BroadcastReceiver.PendingResult;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.ContentCaptureOptions;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IContentProvider;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDebug;
import android.database.sqlite.SQLiteDebug.DbStats;
import android.database.sqlite.SQLiteDebug.PagerStats;
import android.ddm.DdmHandleAppName;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.HardwareRenderer;
import android.hardware.display.DisplayManagerGlobal;
import android.net.ConnectivityManager;
import android.net.Proxy;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.os.Environment;
import android.os.FileUtils;
import android.os.GraphicsEnvironment;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue.IdleHandler;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.BlockedNumberContract;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.DeviceConfig;
import android.provider.Settings.Global;
import android.renderscript.RenderScriptCacheDir;
import android.security.NetworkSecurityPolicy;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.system.StructStat;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
import android.util.MergedConfiguration;
import android.util.Pair;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SuperNotCalledException;
import android.util.proto.ProtoOutputStream;
import android.view.Choreographer;
import android.view.ContextThemeWrapper;
import android.view.ThreadedRenderer;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewManager;
import android.view.ViewRootImpl;
import android.view.ViewRootImpl.ActivityConfigCallback;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerGlobal;
import android.webkit.WebView;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.content.ReferrerIntent;
import com.android.internal.os.BinderInternal;
import com.android.internal.os.RuntimeInit;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.org.conscrypt.OpenSSLSocketImpl;
import com.android.org.conscrypt.TrustedCertificateStore;
import dalvik.system.CloseGuard;
import dalvik.system.VMDebug;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import libcore.io.ForwardingOs;
import libcore.io.IoUtils;
import libcore.io.Os;
import libcore.net.event.NetworkEventDispatcher;
import miui.maml.animation.SizeAnimation;

public final class ActivityThread extends ClientTransactionHandler {
    private static final int ACTIVITY_THREAD_CHECKIN_VERSION = 4;
    private static final long CONTENT_PROVIDER_RETAIN_TIME = 1000;
    private static final boolean DEBUG_BACKUP = false;
    public static final boolean DEBUG_BROADCAST = false;
    public static final boolean DEBUG_CONFIGURATION = false;
    public static final boolean DEBUG_MEMORY_TRIM = false;
    static final boolean DEBUG_MESSAGES = false;
    public static final boolean DEBUG_ORDER = false;
    private static final boolean DEBUG_PROVIDER = false;
    private static final boolean DEBUG_RESULTS = false;
    private static final boolean DEBUG_SERVICE = false;
    private static final String HEAP_COLUMN = "%13s %8s %8s %8s %8s %8s %8s %8s";
    private static final String HEAP_FULL_COLUMN = "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s";
    public static final long INVALID_PROC_STATE_SEQ = -1;
    private static final long MIN_TIME_BETWEEN_GCS = 5000;
    private static final String ONE_COUNT_COLUMN = "%21s %8d";
    private static final String ONE_COUNT_COLUMN_HEADER = "%21s %8s";
    private static final long PENDING_TOP_PROCESS_STATE_TIMEOUT = 1000;
    public static final String PROC_START_SEQ_IDENT = "seq=";
    private static final boolean REPORT_TO_ACTIVITY = true;
    public static final int SERVICE_DONE_EXECUTING_ANON = 0;
    public static final int SERVICE_DONE_EXECUTING_START = 1;
    public static final int SERVICE_DONE_EXECUTING_STOP = 2;
    private static final int SQLITE_MEM_RELEASED_EVENT_LOG_TAG = 75003;
    public static final String TAG = "ActivityThread";
    private static final Config THUMBNAIL_FORMAT = Config.RGB_565;
    private static final String TWO_COUNT_COLUMNS = "%21s %8d %21s %8d";
    private static final int VM_PROCESS_STATE_JANK_IMPERCEPTIBLE = 1;
    private static final int VM_PROCESS_STATE_JANK_PERCEPTIBLE = 0;
    static final boolean localLOGV = false;
    @UnsupportedAppUsage
    private static volatile ActivityThread sCurrentActivityThread;
    private static final ThreadLocal<Intent> sCurrentBroadcastIntent = new ThreadLocal();
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    static volatile Handler sMainThreadHandler;
    @UnsupportedAppUsage
    static volatile IPackageManager sPackageManager;
    private static boolean sWaitingToUse;
    @UnsupportedAppUsage
    final ArrayMap<IBinder, ActivityClientRecord> mActivities = new ArrayMap();
    final Map<IBinder, ClientTransactionItem> mActivitiesToBeDestroyed = Collections.synchronizedMap(new ArrayMap());
    @UnsupportedAppUsage
    final ArrayList<Application> mAllApplications = new ArrayList();
    @UnsupportedAppUsage
    final ApplicationThread mAppThread = new ApplicationThread(this, null);
    private final SparseArray<ArrayMap<String, BackupAgent>> mBackupAgentsByUser = new SparseArray();
    @UnsupportedAppUsage
    AppBindData mBoundApplication;
    Configuration mCompatConfiguration;
    @UnsupportedAppUsage
    Configuration mConfiguration;
    Bundle mCoreSettings = null;
    @UnsupportedAppUsage
    int mCurDefaultDisplayDpi;
    @UnsupportedAppUsage
    boolean mDensityCompatMode;
    final Executor mExecutor = new HandlerExecutor(this.mH);
    final GcIdler mGcIdler = new GcIdler();
    boolean mGcIdlerScheduled = false;
    @GuardedBy({"mGetProviderLocks"})
    final ArrayMap<ProviderKey, Object> mGetProviderLocks = new ArrayMap();
    @UnsupportedAppUsage
    final H mH = new H();
    boolean mHiddenApiWarningShown = false;
    @UnsupportedAppUsage
    Application mInitialApplication;
    @UnsupportedAppUsage
    Instrumentation mInstrumentation;
    @UnsupportedAppUsage
    String mInstrumentationAppDir = null;
    String mInstrumentationLibDir = null;
    String mInstrumentationPackageName = null;
    String[] mInstrumentationSplitAppDirs = null;
    @UnsupportedAppUsage
    String mInstrumentedAppDir = null;
    String mInstrumentedLibDir = null;
    String[] mInstrumentedSplitAppDirs = null;
    ArrayList<WeakReference<AssistStructure>> mLastAssistStructures = new ArrayList();
    @GuardedBy({"mAppThread"})
    private int mLastProcessState = -1;
    private int mLastSessionId;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    final ArrayMap<IBinder, ProviderClientRecord> mLocalProviders = new ArrayMap();
    @UnsupportedAppUsage
    final ArrayMap<ComponentName, ProviderClientRecord> mLocalProvidersByName = new ArrayMap();
    @UnsupportedAppUsage
    final Looper mLooper = Looper.myLooper();
    private Configuration mMainThreadConfig = new Configuration();
    @GuardedBy({"mNetworkPolicyLock"})
    private long mNetworkBlockSeq = -1;
    private final Object mNetworkPolicyLock = new Object();
    ActivityClientRecord mNewActivities = null;
    private final AtomicInteger mNumLaunchingActivities = new AtomicInteger();
    @UnsupportedAppUsage
    int mNumVisibleActivities = 0;
    final ArrayMap<Activity, ArrayList<OnActivityPausedListener>> mOnPauseListeners = new ArrayMap();
    @GuardedBy({"mResourcesManager"})
    @UnsupportedAppUsage
    final ArrayMap<String, WeakReference<LoadedApk>> mPackages = new ArrayMap();
    @GuardedBy({"mResourcesManager"})
    @UnsupportedAppUsage
    Configuration mPendingConfiguration = null;
    @GuardedBy({"mAppThread"})
    private int mPendingProcessState = -1;
    Profiler mProfiler;
    @GuardedBy({"mProviderAcquiringCountMap"})
    final ArrayMap<ProviderKey, ProviderAcquiringCount> mProviderAcquiringCountMap = new ArrayMap();
    @UnsupportedAppUsage
    final ArrayMap<ProviderKey, ProviderClientRecord> mProviderMap = new ArrayMap();
    @UnsupportedAppUsage
    final ArrayMap<IBinder, ProviderRefCount> mProviderRefCountMap = new ArrayMap();
    final PurgeIdler mPurgeIdler = new PurgeIdler();
    boolean mPurgeIdlerScheduled = false;
    @GuardedBy({"mResourcesManager"})
    final ArrayList<ActivityClientRecord> mRelaunchingActivities = new ArrayList();
    @GuardedBy({"this"})
    private Map<SafeCancellationTransport, CancellationSignal> mRemoteCancellations;
    @GuardedBy({"mResourcesManager"})
    @UnsupportedAppUsage
    final ArrayMap<String, WeakReference<LoadedApk>> mResourcePackages = new ArrayMap();
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final ResourcesManager mResourcesManager = ResourcesManager.getInstance();
    @UnsupportedAppUsage
    final ArrayMap<IBinder, Service> mServices = new ArrayMap();
    boolean mSomeActivitiesChanged = false;
    @UnsupportedAppUsage
    private ContextImpl mSystemContext;
    boolean mSystemThread = false;
    private ContextImpl mSystemUiContext;
    private final TransactionExecutor mTransactionExecutor = new TransactionExecutor(this);
    boolean mUpdatingSystemConfig = false;
    private final int mainThreadId = Process.myPid();

    public static final class ActivityClientRecord {
        @UnsupportedAppUsage
        Activity activity;
        @UnsupportedAppUsage
        ActivityInfo activityInfo;
        public IBinder assistToken;
        @UnsupportedAppUsage
        CompatibilityInfo compatInfo;
        ActivityConfigCallback configCallback;
        Configuration createdConfig;
        String embeddedID;
        boolean hideForNow;
        int ident;
        @UnsupportedAppUsage
        Intent intent;
        public final boolean isForward;
        boolean isTopResumedActivity;
        NonConfigurationInstances lastNonConfigurationInstances;
        boolean lastReportedTopResumedState;
        private int mLifecycleState;
        @GuardedBy({"this"})
        private Configuration mPendingOverrideConfig;
        Window mPendingRemoveWindow;
        WindowManager mPendingRemoveWindowManager;
        @UnsupportedAppUsage
        boolean mPreserveWindow;
        Configuration newConfig;
        ActivityClientRecord nextIdle;
        Configuration overrideConfig;
        @UnsupportedAppUsage
        public LoadedApk packageInfo;
        Activity parent;
        @UnsupportedAppUsage
        boolean paused;
        int pendingConfigChanges;
        List<ReferrerIntent> pendingIntents;
        List<ResultInfo> pendingResults;
        PersistableBundle persistentState;
        ProfilerInfo profilerInfo;
        String referrer;
        boolean startsNotResumed;
        Bundle state;
        @UnsupportedAppUsage
        boolean stopped;
        private Configuration tmpConfig;
        @UnsupportedAppUsage
        public IBinder token;
        IVoiceInteractor voiceInteractor;
        Window window;

        @VisibleForTesting
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        public ActivityClientRecord() {
            this.tmpConfig = new Configuration();
            this.mLifecycleState = 0;
            this.isForward = false;
            init();
        }

        public ActivityClientRecord(IBinder token, Intent intent, int ident, ActivityInfo info, Configuration overrideConfig, CompatibilityInfo compatInfo, String referrer, IVoiceInteractor voiceInteractor, Bundle state, PersistableBundle persistentState, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingNewIntents, boolean isForward, ProfilerInfo profilerInfo, ClientTransactionHandler client, IBinder assistToken) {
            CompatibilityInfo compatibilityInfo = compatInfo;
            this.tmpConfig = new Configuration();
            this.mLifecycleState = 0;
            this.token = token;
            this.assistToken = assistToken;
            this.ident = ident;
            this.intent = intent;
            this.referrer = referrer;
            this.voiceInteractor = voiceInteractor;
            this.activityInfo = info;
            this.compatInfo = compatibilityInfo;
            this.state = state;
            this.persistentState = persistentState;
            this.pendingResults = pendingResults;
            this.pendingIntents = pendingNewIntents;
            this.isForward = isForward;
            this.profilerInfo = profilerInfo;
            this.overrideConfig = overrideConfig;
            this.packageInfo = client.getPackageInfoNoCheck(this.activityInfo.applicationInfo, compatibilityInfo);
            init();
        }

        private void init() {
            this.parent = null;
            this.embeddedID = null;
            this.paused = false;
            this.stopped = false;
            this.hideForNow = false;
            this.nextIdle = null;
            this.configCallback = new -$$Lambda$ActivityThread$ActivityClientRecord$HOrG1qglSjSUHSjKBn2rXtX0gGg(this);
        }

        public /* synthetic */ void lambda$init$0$ActivityThread$ActivityClientRecord(Configuration overrideConfig, int newDisplayId) {
            Activity activity = this.activity;
            if (activity != null) {
                activity.mMainThread.handleActivityConfigurationChanged(this.token, overrideConfig, newDisplayId);
                return;
            }
            throw new IllegalStateException("Received config update for non-existing activity");
        }

        public int getLifecycleState() {
            return this.mLifecycleState;
        }

        public void setState(int newLifecycleState) {
            this.mLifecycleState = newLifecycleState;
            int i = this.mLifecycleState;
            if (i == 1) {
                this.paused = true;
                this.stopped = true;
            } else if (i == 2) {
                this.paused = true;
                this.stopped = false;
            } else if (i == 3) {
                this.paused = false;
                this.stopped = false;
            } else if (i == 4) {
                this.paused = true;
                this.stopped = false;
            } else if (i == 5) {
                this.paused = true;
                this.stopped = true;
            }
        }

        private boolean isPreHoneycomb() {
            Activity activity = this.activity;
            return activity != null && activity.getApplicationInfo().targetSdkVersion < 11;
        }

        private boolean isPreP() {
            Activity activity = this.activity;
            return activity != null && activity.getApplicationInfo().targetSdkVersion < 28;
        }

        public boolean isPersistable() {
            return this.activityInfo.persistableMode == 2;
        }

        public boolean isVisibleFromServer() {
            Activity activity = this.activity;
            return activity != null && activity.mVisibleFromServer;
        }

        public String toString() {
            Intent intent = this.intent;
            ComponentName componentName = intent != null ? intent.getComponent() : null;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ActivityRecord{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" token=");
            stringBuilder.append(this.token);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(componentName == null ? "no component name" : componentName.toShortString());
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        public String getStateString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ActivityClientRecord{");
            sb.append("paused=");
            sb.append(this.paused);
            String str = ", stopped=";
            sb.append(str);
            sb.append(this.stopped);
            sb.append(", hideForNow=");
            sb.append(this.hideForNow);
            sb.append(", startsNotResumed=");
            sb.append(this.startsNotResumed);
            sb.append(", isForward=");
            sb.append(this.isForward);
            sb.append(", pendingConfigChanges=");
            sb.append(this.pendingConfigChanges);
            sb.append(", preserveWindow=");
            sb.append(this.mPreserveWindow);
            String str2 = "}";
            if (this.activity != null) {
                sb.append(", Activity{");
                sb.append("resumed=");
                sb.append(this.activity.mResumed);
                sb.append(str);
                sb.append(this.activity.mStopped);
                sb.append(", finished=");
                sb.append(this.activity.isFinishing());
                sb.append(", destroyed=");
                sb.append(this.activity.isDestroyed());
                sb.append(", startedActivity=");
                sb.append(this.activity.mStartedActivity);
                sb.append(", changingConfigurations=");
                sb.append(this.activity.mChangingConfigurations);
                sb.append(str2);
            }
            sb.append(str2);
            return sb.toString();
        }
    }

    private static class AndroidOs extends ForwardingOs {
        public static void install() {
            if (ContentResolver.DEPRECATE_DATA_COLUMNS) {
                while (true) {
                    Os def = Os.getDefault();
                    if (Os.compareAndSetDefault(def, new AndroidOs(def))) {
                        return;
                    }
                }
            }
        }

        private AndroidOs(Os os) {
            super(os);
        }

        private FileDescriptor openDeprecatedDataPath(String path, int mode) throws ErrnoException {
            Uri uri = ContentResolver.translateDeprecatedDataPath(path);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Redirecting ");
            stringBuilder.append(path);
            stringBuilder.append(" to ");
            stringBuilder.append(uri);
            Log.v(ActivityThread.TAG, stringBuilder.toString());
            ContentResolver cr = ActivityThread.currentActivityThread().getApplication().getContentResolver();
            try {
                FileDescriptor fd = new FileDescriptor();
                fd.setInt$(cr.openFileDescriptor(uri, FileUtils.translateModePosixToString(mode)).detachFd());
                return fd;
            } catch (SecurityException e) {
                throw new ErrnoException(e.getMessage(), OsConstants.EACCES);
            } catch (FileNotFoundException e2) {
                throw new ErrnoException(e2.getMessage(), OsConstants.ENOENT);
            }
        }

        private void deleteDeprecatedDataPath(String path) throws ErrnoException {
            Uri uri = ContentResolver.translateDeprecatedDataPath(path);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Redirecting ");
            stringBuilder.append(path);
            stringBuilder.append(" to ");
            stringBuilder.append(uri);
            Log.v(ActivityThread.TAG, stringBuilder.toString());
            try {
                if (ActivityThread.currentActivityThread().getApplication().getContentResolver().delete(uri, null, null) == 0) {
                    throw new FileNotFoundException();
                }
            } catch (SecurityException e) {
                throw new ErrnoException(e.getMessage(), OsConstants.EACCES);
            } catch (FileNotFoundException e2) {
                throw new ErrnoException(e2.getMessage(), OsConstants.ENOENT);
            }
        }

        public boolean access(String path, int mode) throws ErrnoException {
            if (path == null || !path.startsWith(ContentResolver.DEPRECATE_DATA_PREFIX)) {
                return super.access(path, mode);
            }
            IoUtils.closeQuietly(openDeprecatedDataPath(path, FileUtils.translateModeAccessToPosix(mode)));
            return true;
        }

        public FileDescriptor open(String path, int flags, int mode) throws ErrnoException {
            if (path == null || !path.startsWith(ContentResolver.DEPRECATE_DATA_PREFIX)) {
                return super.open(path, flags, mode);
            }
            return openDeprecatedDataPath(path, mode);
        }

        public StructStat stat(String path) throws ErrnoException {
            if (path == null || !path.startsWith(ContentResolver.DEPRECATE_DATA_PREFIX)) {
                return super.stat(path);
            }
            FileDescriptor fd = openDeprecatedDataPath(path, OsConstants.O_RDONLY);
            try {
                StructStat fstat = android.system.Os.fstat(fd);
                return fstat;
            } finally {
                IoUtils.closeQuietly(fd);
            }
        }

        public void unlink(String path) throws ErrnoException {
            if (path == null || !path.startsWith(ContentResolver.DEPRECATE_DATA_PREFIX)) {
                super.unlink(path);
            } else {
                deleteDeprecatedDataPath(path);
            }
        }

        public void remove(String path) throws ErrnoException {
            if (path == null || !path.startsWith(ContentResolver.DEPRECATE_DATA_PREFIX)) {
                super.remove(path);
            } else {
                deleteDeprecatedDataPath(path);
            }
        }

        public void rename(String oldPath, String newPath) throws ErrnoException {
            try {
                super.rename(oldPath, newPath);
            } catch (ErrnoException e) {
                if (e.errno == OsConstants.EXDEV) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Recovering failed rename ");
                    stringBuilder.append(oldPath);
                    stringBuilder.append(" to ");
                    stringBuilder.append(newPath);
                    Log.v(ActivityThread.TAG, stringBuilder.toString());
                    try {
                        Files.move(new File(oldPath).toPath(), new File(newPath).toPath(), new CopyOption[0]);
                        return;
                    } catch (IOException e2) {
                        throw e;
                    }
                }
                throw e;
            }
        }
    }

    static final class AppBindData {
        @UnsupportedAppUsage
        ApplicationInfo appInfo;
        AutofillOptions autofillOptions;
        String buildSerial;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        CompatibilityInfo compatInfo;
        Configuration config;
        ContentCaptureOptions contentCaptureOptions;
        int debugMode;
        boolean enableBinderTracking;
        @UnsupportedAppUsage
        LoadedApk info;
        ProfilerInfo initProfilerInfo;
        @UnsupportedAppUsage
        Bundle instrumentationArgs;
        ComponentName instrumentationName;
        IUiAutomationConnection instrumentationUiAutomationConnection;
        IInstrumentationWatcher instrumentationWatcher;
        @UnsupportedAppUsage
        boolean persistent;
        @UnsupportedAppUsage
        String processName;
        @UnsupportedAppUsage
        List<ProviderInfo> providers;
        @UnsupportedAppUsage
        boolean restrictedBackupMode;
        boolean trackAllocation;

        AppBindData() {
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AppBindData{appInfo=");
            stringBuilder.append(this.appInfo);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    private class ApplicationThread extends Stub {
        private static final String DB_INFO_FORMAT = "  %8s %8s %14s %14s  %s";

        private ApplicationThread() {
        }

        /* synthetic */ ApplicationThread(ActivityThread x0, AnonymousClass1 x1) {
            this();
        }

        public final void scheduleSleeping(IBinder token, boolean sleeping) {
            ActivityThread.this.sendMessage(137, token, sleeping);
        }

        public final void scheduleReceiver(Intent intent, ActivityInfo info, CompatibilityInfo compatInfo, int resultCode, String data, Bundle extras, boolean sync, int sendingUser, int processState) {
            ActivityThread.this.updateProcessState(processState, false);
            ReceiverData receiverData = new ReceiverData(intent, resultCode, data, extras, sync, false, ActivityThread.this.mAppThread.asBinder(), sendingUser);
            receiverData.info = info;
            receiverData.compatInfo = compatInfo;
            receiverData.setHandler(ActivityThread.this.mH);
            ActivityThread.this.sendMessage(113, receiverData);
        }

        public final void scheduleCreateBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int backupMode, int userId) {
            CreateBackupAgentData d = new CreateBackupAgentData();
            d.appInfo = app;
            d.compatInfo = compatInfo;
            d.backupMode = backupMode;
            d.userId = userId;
            ActivityThread.this.sendMessage(128, d);
        }

        public final void scheduleDestroyBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int userId) {
            CreateBackupAgentData d = new CreateBackupAgentData();
            d.appInfo = app;
            d.compatInfo = compatInfo;
            d.userId = userId;
            ActivityThread.this.sendMessage(129, d);
        }

        public final void scheduleCreateService(IBinder token, ServiceInfo info, CompatibilityInfo compatInfo, int processState) {
            ActivityThread.this.updateProcessState(processState, false);
            CreateServiceData s = new CreateServiceData();
            s.token = token;
            s.info = info;
            s.compatInfo = compatInfo;
            ActivityThread.this.sendMessage(114, s);
        }

        public final void scheduleBindService(IBinder token, Intent intent, boolean rebind, int processState) {
            ActivityThread.this.updateProcessState(processState, false);
            BindServiceData s = new BindServiceData();
            s.token = token;
            s.intent = intent;
            s.rebind = rebind;
            ActivityThread.this.sendMessage(121, s);
        }

        public final void scheduleUnbindService(IBinder token, Intent intent) {
            BindServiceData s = new BindServiceData();
            s.token = token;
            s.intent = intent;
            ActivityThread.this.sendMessage(122, s);
        }

        public final void scheduleServiceArgs(IBinder token, ParceledListSlice args) {
            List<ServiceStartArgs> list = args.getList();
            for (int i = 0; i < list.size(); i++) {
                ServiceStartArgs ssa = (ServiceStartArgs) list.get(i);
                ServiceArgsData s = new ServiceArgsData();
                s.token = token;
                s.taskRemoved = ssa.taskRemoved;
                s.startId = ssa.startId;
                s.flags = ssa.flags;
                s.args = ssa.args;
                ActivityThread.this.sendMessage(115, s);
            }
        }

        public final void scheduleStopService(IBinder token) {
            ActivityThread.this.sendMessage(116, token);
        }

        public final void bindApplication(String processName, ApplicationInfo appInfo, List<ProviderInfo> providers, ComponentName instrumentationName, ProfilerInfo profilerInfo, Bundle instrumentationArgs, IInstrumentationWatcher instrumentationWatcher, IUiAutomationConnection instrumentationUiConnection, int debugMode, boolean enableBinderTracking, boolean trackAllocation, boolean isRestrictedBackupMode, boolean persistent, Configuration config, CompatibilityInfo compatInfo, Map services, Bundle coreSettings, String buildSerial, AutofillOptions autofillOptions, ContentCaptureOptions contentCaptureOptions) {
            ApplicationInfo applicationInfo = appInfo;
            ActivityThread.sWaitingToUse = applicationInfo.waitingToUse;
            if (services != null) {
                ServiceManager.initServiceCache(services);
            }
            setCoreSettings(coreSettings);
            AppBindData data = new AppBindData();
            data.processName = processName;
            data.appInfo = applicationInfo;
            data.providers = providers;
            data.instrumentationName = instrumentationName;
            data.instrumentationArgs = instrumentationArgs;
            data.instrumentationWatcher = instrumentationWatcher;
            data.instrumentationUiAutomationConnection = instrumentationUiConnection;
            data.debugMode = debugMode;
            data.enableBinderTracking = enableBinderTracking;
            data.trackAllocation = trackAllocation;
            data.restrictedBackupMode = isRestrictedBackupMode;
            data.persistent = persistent;
            data.config = config;
            data.compatInfo = compatInfo;
            data.initProfilerInfo = profilerInfo;
            data.buildSerial = buildSerial;
            data.autofillOptions = autofillOptions;
            data.contentCaptureOptions = contentCaptureOptions;
            ActivityThread.this.sendMessage(110, data);
        }

        public final void runIsolatedEntryPoint(String entryPoint, String[] entryPointArgs) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = entryPoint;
            args.arg2 = entryPointArgs;
            ActivityThread.this.sendMessage(158, args);
        }

        public final void scheduleExit() {
            ActivityThread.this.sendMessage(111, null);
        }

        public final void scheduleSuicide() {
            ActivityThread.this.sendMessage(130, null);
        }

        public void scheduleApplicationInfoChanged(ApplicationInfo ai) {
            ActivityThread.this.mH.removeMessages(156, ai);
            ActivityThread.this.sendMessage(156, ai);
        }

        public void updateTimeZone() {
            TimeZone.setDefault(null);
        }

        public void clearDnsCache() {
            InetAddress.clearDnsCache();
            NetworkEventDispatcher.getInstance().onNetworkConfigurationChanged();
        }

        public void updateHttpProxy() {
            ActivityThread.updateHttpProxy(ActivityThread.this.getApplication() != null ? ActivityThread.this.getApplication() : ActivityThread.this.getSystemContext());
        }

        public void processInBackground() {
            ActivityThread.this.mH.removeMessages(120);
            ActivityThread.this.mH.sendMessage(ActivityThread.this.mH.obtainMessage(120));
        }

        public void dumpService(ParcelFileDescriptor pfd, IBinder servicetoken, String[] args) {
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                data.fd = pfd.dup();
                data.token = servicetoken;
                data.args = args;
                ActivityThread.this.sendMessage(123, (Object) data, 0, 0, true);
            } catch (IOException e) {
                Slog.w(ActivityThread.TAG, "dumpService failed", e);
            } catch (Throwable th) {
                IoUtils.closeQuietly(pfd);
            }
            IoUtils.closeQuietly(pfd);
        }

        public void scheduleRegisteredReceiver(IIntentReceiver receiver, Intent intent, int resultCode, String dataStr, Bundle extras, boolean ordered, boolean sticky, int sendingUser, int processState) throws RemoteException {
            ActivityThread.this.updateProcessState(processState, false);
            receiver.performReceive(intent, resultCode, dataStr, extras, ordered, sticky, sendingUser);
        }

        public void scheduleLowMemory() {
            ActivityThread.this.sendMessage(124, null);
        }

        public void profilerControl(boolean start, ProfilerInfo profilerInfo, int profileType) {
            ActivityThread.this.sendMessage(127, profilerInfo, start, profileType);
        }

        public void dumpHeap(boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd, RemoteCallback finishCallback) {
            DumpHeapData dhd = new DumpHeapData();
            dhd.managed = managed;
            dhd.mallocInfo = mallocInfo;
            dhd.runGc = runGc;
            dhd.path = path;
            try {
                dhd.fd = fd.dup();
                dhd.finishCallback = finishCallback;
                ActivityThread.this.sendMessage(135, (Object) dhd, 0, 0, true);
            } catch (IOException e) {
                Slog.e(ActivityThread.TAG, "Failed to duplicate heap dump file descriptor", e);
            }
        }

        public void attachAgent(String agent) {
            ActivityThread.this.sendMessage(155, agent);
        }

        public void setSchedulingGroup(int group) {
            try {
                Process.setProcessGroup(Process.myPid(), group);
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed setting process group to ");
                stringBuilder.append(group);
                Slog.w(ActivityThread.TAG, stringBuilder.toString(), e);
            }
        }

        public void dispatchPackageBroadcast(int cmd, String[] packages) {
            ActivityThread.this.sendMessage(133, packages, cmd);
        }

        public void scheduleCrash(String msg) {
            ActivityThread.this.sendMessage(134, msg);
        }

        public void dumpActivity(ParcelFileDescriptor pfd, IBinder activitytoken, String prefix, String[] args) {
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                data.fd = pfd.dup();
                data.token = activitytoken;
                data.prefix = prefix;
                data.args = args;
                ActivityThread.this.sendMessage(136, (Object) data, 0, 0, true);
            } catch (IOException e) {
                Slog.w(ActivityThread.TAG, "dumpActivity failed", e);
            } catch (Throwable th) {
                IoUtils.closeQuietly(pfd);
            }
            IoUtils.closeQuietly(pfd);
        }

        public void dumpProvider(ParcelFileDescriptor pfd, IBinder providertoken, String[] args) {
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                data.fd = pfd.dup();
                data.token = providertoken;
                data.args = args;
                ActivityThread.this.sendMessage(141, (Object) data, 0, 0, true);
            } catch (IOException e) {
                Slog.w(ActivityThread.TAG, "dumpProvider failed", e);
            } catch (Throwable th) {
                IoUtils.closeQuietly(pfd);
            }
            IoUtils.closeQuietly(pfd);
        }

        public void dumpMemInfo(ParcelFileDescriptor pfd, MemoryInfo mem, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) {
            PrintWriter pw = new FastPrintWriter(new FileOutputStream(pfd.getFileDescriptor()));
            try {
                dumpMemInfo(pw, mem, checkin, dumpFullInfo, dumpDalvik, dumpSummaryOnly, dumpUnreachable);
                pw.flush();
                IoUtils.closeQuietly(pfd);
            } catch (Throwable th) {
                Throwable th2 = th;
                pw.flush();
                IoUtils.closeQuietly(pfd);
            }
        }

        private void dumpMemInfo(PrintWriter pw, MemoryInfo memInfo, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable) {
            PrintWriter printWriter = pw;
            long nativeMax = Debug.getNativeHeapSize() / 1024;
            long nativeAllocated = Debug.getNativeHeapAllocatedSize() / 1024;
            long nativeFree = Debug.getNativeHeapFreeSize() / 1024;
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long dalvikMax = runtime.totalMemory() / 1024;
            long dalvikFree = runtime.freeMemory() / 1024;
            long dalvikAllocated = dalvikMax - dalvikFree;
            Class[] classesToCount = new Class[]{ContextImpl.class, Activity.class, WebView.class, OpenSSLSocketImpl.class};
            long[] instanceCounts = VMDebug.countInstancesOfClasses(classesToCount, true);
            long appContextInstanceCount = instanceCounts[0];
            long activityInstanceCount = instanceCounts[1];
            long webviewInstanceCount = instanceCounts[2];
            long openSslSocketCount = instanceCounts[3];
            long viewInstanceCount = ViewDebug.getViewInstanceCount();
            long viewRootInstanceCount = ViewDebug.getViewRootImplCount();
            int globalAssetCount = AssetManager.getGlobalAssetCount();
            int globalAssetManagerCount = AssetManager.getGlobalAssetManagerCount();
            int binderLocalObjectCount = Debug.getBinderLocalObjectCount();
            int binderProxyObjectCount = Debug.getBinderProxyObjectCount();
            int binderDeathObjectCount = Debug.getBinderDeathObjectCount();
            long parcelSize = Parcel.getGlobalAllocSize();
            long parcelCount = Parcel.getGlobalAllocCount();
            int binderDeathObjectCount2 = binderDeathObjectCount;
            PagerStats stats = SQLiteDebug.getDatabaseInfo();
            int myPid = Process.myPid();
            String str = ActivityThread.this.mBoundApplication != null ? ActivityThread.this.mBoundApplication.processName : "unknown";
            long viewRootInstanceCount2 = viewRootInstanceCount;
            long viewInstanceCount2 = viewInstanceCount;
            long openSslSocketCount2 = openSslSocketCount;
            long activityInstanceCount2 = activityInstanceCount;
            long appContextInstanceCount2 = appContextInstanceCount;
            int i = 4;
            boolean z = true;
            PrintWriter printWriter2 = pw;
            PagerStats stats2 = stats;
            int globalAssetCount2 = globalAssetCount;
            int globalAssetManagerCount2 = globalAssetManagerCount;
            int binderLocalObjectCount2 = binderLocalObjectCount;
            int binderProxyObjectCount2 = binderProxyObjectCount;
            int binderDeathObjectCount3 = binderDeathObjectCount2;
            ActivityThread.dumpMemInfoTable(pw, memInfo, checkin, dumpFullInfo, dumpDalvik, dumpSummaryOnly, myPid, str, nativeMax, nativeAllocated, nativeFree, dalvikMax, dalvikAllocated, dalvikFree);
            DbStats dbStats;
            if (checkin) {
                printWriter2.print(viewInstanceCount2);
                printWriter2.print(',');
                long viewRootInstanceCount3 = viewRootInstanceCount2;
                printWriter2.print(viewRootInstanceCount3);
                printWriter2.print(',');
                printWriter2.print(appContextInstanceCount2);
                printWriter2.print(',');
                printWriter2.print(activityInstanceCount2);
                printWriter2.print(',');
                printWriter2.print(globalAssetCount2);
                printWriter2.print(',');
                printWriter2.print(globalAssetManagerCount2);
                printWriter2.print(',');
                printWriter2.print(binderLocalObjectCount2);
                printWriter2.print(',');
                printWriter2.print(binderProxyObjectCount2);
                printWriter2.print(',');
                printWriter2.print(binderDeathObjectCount3);
                printWriter2.print(',');
                printWriter2.print(openSslSocketCount2);
                printWriter2.print(',');
                PagerStats stats3 = stats2;
                printWriter2.print(stats3.memoryUsed / 1024);
                printWriter2.print(',');
                printWriter2.print(stats3.memoryUsed / 1024);
                printWriter2.print(',');
                printWriter2.print(stats3.pageCacheOverflow / 1024);
                printWriter2.print(',');
                printWriter2.print(stats3.largestMemAlloc / 1024);
                int i2 = 0;
                while (i2 < stats3.dbStats.size()) {
                    dbStats = (DbStats) stats3.dbStats.get(i2);
                    stats2 = stats3;
                    printWriter2.print(',');
                    printWriter2.print(dbStats.dbName);
                    printWriter2.print(',');
                    viewRootInstanceCount2 = viewRootInstanceCount3;
                    printWriter2.print(dbStats.pageSize);
                    printWriter2.print(',');
                    printWriter2.print(dbStats.dbSize);
                    printWriter2.print(',');
                    printWriter2.print(dbStats.lookaside);
                    printWriter2.print(',');
                    printWriter2.print(dbStats.cache);
                    printWriter2.print(',');
                    printWriter2.print(dbStats.cache);
                    i2++;
                    viewRootInstanceCount3 = viewRootInstanceCount2;
                    stats3 = stats2;
                }
                pw.println();
                return;
            }
            int i3;
            int i4;
            String str2;
            String str3 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            printWriter2.println(str3);
            printWriter2.println(" Objects");
            Object[] objArr = new Object[]{"Views:", Long.valueOf(viewRootInstanceCount), "ViewRootImpl:", Long.valueOf(viewRootInstanceCount2)};
            String str4 = ActivityThread.TWO_COUNT_COLUMNS;
            ActivityThread.printRow(printWriter2, str4, objArr);
            ActivityThread.printRow(printWriter2, str4, "AppContexts:", Long.valueOf(appContextInstanceCount), "Activities:", Long.valueOf(activityInstanceCount));
            ActivityThread.printRow(printWriter2, str4, "Assets:", Integer.valueOf(globalAssetCount), "AssetManagers:", Integer.valueOf(globalAssetManagerCount));
            ActivityThread.printRow(printWriter2, str4, "Local Binders:", Integer.valueOf(binderLocalObjectCount), "Proxy Binders:", Integer.valueOf(binderProxyObjectCount));
            ActivityThread.printRow(printWriter2, str4, "Parcel memory:", Long.valueOf(parcelSize / 1024), "Parcel count:", Long.valueOf(parcelCount));
            ActivityThread.printRow(printWriter2, str4, "Death Recipients:", Integer.valueOf(binderDeathObjectCount3), "OpenSSL Sockets:", Long.valueOf(openSslSocketCount2));
            objArr = new Object[]{"WebViews:", Long.valueOf(webviewInstanceCount)};
            String str5 = ActivityThread.ONE_COUNT_COLUMN;
            ActivityThread.printRow(printWriter2, str5, objArr);
            printWriter2.println(str3);
            printWriter2.println(" SQL");
            Object[] objArr2 = new Object[2];
            objArr2[0] = "MEMORY_USED:";
            PagerStats stats4 = stats2;
            objArr2[z] = Integer.valueOf(stats4.memoryUsed / 1024);
            ActivityThread.printRow(printWriter2, str5, objArr2);
            ActivityThread.printRow(printWriter2, str4, "PAGECACHE_OVERFLOW:", Integer.valueOf(stats4.pageCacheOverflow / 1024), "MALLOC_SIZE:", Integer.valueOf(stats4.largestMemAlloc / 1024));
            printWriter2.println(str3);
            int N = stats4.dbStats.size();
            if (N > 0) {
                printWriter2.println(" DATABASES");
                i3 = 5;
                Object[] objArr3 = new Object[]{"pgsz", "dbsz", "Lookaside(b)", "cache", "Dbname"};
                str5 = DB_INFO_FORMAT;
                ActivityThread.printRow(printWriter2, str5, objArr3);
                i4 = 0;
                while (i4 < N) {
                    dbStats = (DbStats) stats4.dbStats.get(i4);
                    globalAssetCount = N;
                    Object[] objArr4 = new Object[i3];
                    str2 = str3;
                    objArr4[0] = dbStats.pageSize > 0 ? String.valueOf(dbStats.pageSize) : str2;
                    objArr4[z] = dbStats.dbSize > 0 ? String.valueOf(dbStats.dbSize) : str2;
                    objArr4[2] = dbStats.lookaside > 0 ? String.valueOf(dbStats.lookaside) : str2;
                    objArr4[3] = dbStats.cache;
                    objArr4[4] = dbStats.dbName;
                    ActivityThread.printRow(printWriter2, str5, objArr4);
                    i4++;
                    N = globalAssetCount;
                    str3 = str2;
                    i3 = 5;
                }
                str2 = str3;
                i3 = 2;
            } else {
                str2 = str3;
                i3 = 2;
            }
            String assetAlloc = AssetManager.getAssetAllocations();
            if (assetAlloc != null) {
                str3 = str2;
                printWriter2.println(str3);
                printWriter2.println(" Asset Allocations");
                printWriter2.print(assetAlloc);
            } else {
                str3 = str2;
            }
            if (dumpUnreachable) {
                i4 = i3;
                if ((ActivityThread.this.mBoundApplication == null || (i4 & ActivityThread.this.mBoundApplication.appInfo.flags) == 0) && !Build.IS_DEBUGGABLE) {
                    z = false;
                }
                boolean showContents = z;
                printWriter2.println(str3);
                printWriter2.println(" Unreachable memory");
                printWriter2.print(Debug.getUnreachableMemory(100, showContents));
            }
        }

        public void dumpMemInfoProto(ParcelFileDescriptor pfd, MemoryInfo mem, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) {
            ProtoOutputStream proto = new ProtoOutputStream(pfd.getFileDescriptor());
            try {
                dumpMemInfo(proto, mem, dumpFullInfo, dumpDalvik, dumpSummaryOnly, dumpUnreachable);
            } finally {
                proto.flush();
                IoUtils.closeQuietly(pfd);
            }
        }

        private void dumpMemInfo(ProtoOutputStream proto, MemoryInfo memInfo, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable) {
            int n;
            ProtoOutputStream protoOutputStream = proto;
            long nativeMax = Debug.getNativeHeapSize() / 1024;
            long nativeAllocated = Debug.getNativeHeapAllocatedSize() / 1024;
            long nativeFree = Debug.getNativeHeapFreeSize() / 1024;
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long dalvikMax = runtime.totalMemory() / 1024;
            long dalvikFree = runtime.freeMemory() / 1024;
            long dalvikAllocated = dalvikMax - dalvikFree;
            r1 = new Class[4];
            boolean z = false;
            r1[0] = ContextImpl.class;
            r1[1] = Activity.class;
            r1[2] = WebView.class;
            r1[3] = OpenSSLSocketImpl.class;
            Class[] classesToCount = r1;
            long[] instanceCounts = VMDebug.countInstancesOfClasses(classesToCount, true);
            long appContextInstanceCount = instanceCounts[0];
            long activityInstanceCount = instanceCounts[1];
            long webviewInstanceCount = instanceCounts[2];
            long openSslSocketCount = instanceCounts[3];
            long viewInstanceCount = ViewDebug.getViewInstanceCount();
            long viewRootInstanceCount = ViewDebug.getViewRootImplCount();
            int globalAssetCount = AssetManager.getGlobalAssetCount();
            int globalAssetManagerCount = AssetManager.getGlobalAssetManagerCount();
            int binderLocalObjectCount = Debug.getBinderLocalObjectCount();
            int binderProxyObjectCount = Debug.getBinderProxyObjectCount();
            int binderDeathObjectCount = Debug.getBinderDeathObjectCount();
            long parcelSize = Parcel.getGlobalAllocSize();
            int binderDeathObjectCount2 = binderDeathObjectCount;
            Class[] classesToCount2 = classesToCount;
            long parcelCount = Parcel.getGlobalAllocCount();
            PagerStats stats = SQLiteDebug.getDatabaseInfo();
            long viewRootInstanceCount2 = viewRootInstanceCount;
            viewRootInstanceCount = protoOutputStream.start(1146756268033L);
            long appContextInstanceCount2 = appContextInstanceCount;
            protoOutputStream.write(1120986464257L, Process.myPid());
            protoOutputStream.write(1138166333442L, ActivityThread.this.mBoundApplication != null ? ActivityThread.this.mBoundApplication.processName : "unknown");
            long mToken = viewRootInstanceCount;
            long viewInstanceCount2 = viewInstanceCount;
            long openSslSocketCount2 = openSslSocketCount;
            long webviewInstanceCount2 = webviewInstanceCount;
            long activityInstanceCount2 = activityInstanceCount;
            long viewRootInstanceCount3 = viewRootInstanceCount2;
            long appContextInstanceCount3 = appContextInstanceCount2;
            PagerStats stats2 = stats;
            int globalAssetCount2 = globalAssetCount;
            int globalAssetManagerCount2 = globalAssetManagerCount;
            int binderLocalObjectCount2 = binderLocalObjectCount;
            int binderProxyObjectCount2 = binderProxyObjectCount;
            int binderDeathObjectCount3 = binderDeathObjectCount2;
            long parcelCount2 = parcelCount;
            binderLocalObjectCount = globalAssetCount2;
            ProtoOutputStream protoOutputStream2 = protoOutputStream;
            ActivityThread.dumpMemInfoTable(proto, memInfo, dumpDalvik, dumpSummaryOnly, nativeMax, nativeAllocated, nativeFree, dalvikMax, dalvikAllocated, dalvikFree);
            protoOutputStream2.end(mToken);
            viewInstanceCount = protoOutputStream2.start(1146756268034L);
            protoOutputStream2.write(1120986464257L, viewInstanceCount2);
            protoOutputStream2.write(1120986464258L, viewRootInstanceCount3);
            activityInstanceCount = appContextInstanceCount3;
            protoOutputStream2.write(1120986464259L, activityInstanceCount);
            webviewInstanceCount = activityInstanceCount2;
            protoOutputStream2.write(1120986464260L, webviewInstanceCount);
            int globalAssetCount3 = binderLocalObjectCount;
            protoOutputStream2.write(1120986464261L, globalAssetCount3);
            protoOutputStream2.write(1120986464262L, globalAssetManagerCount2);
            protoOutputStream2.write(1120986464263L, binderLocalObjectCount2);
            protoOutputStream2.write(1120986464264L, binderProxyObjectCount2);
            protoOutputStream2.write(1112396529673L, parcelSize / 1024);
            protoOutputStream2.write(1120986464266L, parcelCount2);
            protoOutputStream2.write(1120986464267L, binderDeathObjectCount3);
            protoOutputStream2.write(1120986464268L, openSslSocketCount2);
            protoOutputStream2.write(1120986464269L, webviewInstanceCount2);
            protoOutputStream2.end(viewInstanceCount);
            viewRootInstanceCount = protoOutputStream2.start(1146756268035L);
            PagerStats stats3 = stats2;
            protoOutputStream2.write(1120986464257L, stats3.memoryUsed / 1024);
            protoOutputStream2.write(1120986464258L, stats3.pageCacheOverflow / 1024);
            protoOutputStream2.write(1120986464259L, stats3.largestMemAlloc / 1024);
            int n2 = stats3.dbStats.size();
            int i = 0;
            while (true) {
                activityInstanceCount2 = webviewInstanceCount;
                if (i >= n2) {
                    break;
                }
                DbStats dbStats = (DbStats) stats3.dbStats.get(i);
                long dToken = protoOutputStream2.start(2246267895812L);
                stats2 = stats3;
                n = n2;
                appContextInstanceCount3 = activityInstanceCount;
                protoOutputStream2.write(1138166333441L, dbStats.dbName);
                protoOutputStream2.write(1120986464258L, dbStats.pageSize);
                protoOutputStream2.write(1120986464259L, dbStats.dbSize);
                protoOutputStream2.write(1120986464260L, dbStats.lookaside);
                protoOutputStream2.write(1138166333445L, dbStats.cache);
                protoOutputStream2.end(dToken);
                i++;
                n2 = n;
                webviewInstanceCount = activityInstanceCount2;
                activityInstanceCount = appContextInstanceCount3;
                stats3 = stats2;
            }
            n = n2;
            appContextInstanceCount3 = activityInstanceCount;
            protoOutputStream2.end(viewRootInstanceCount);
            String assetAlloc = AssetManager.getAssetAllocations();
            if (assetAlloc != null) {
                protoOutputStream2.write(1138166333444L, assetAlloc);
            }
            if (dumpUnreachable) {
                if (((ActivityThread.this.mBoundApplication == null ? 0 : ActivityThread.this.mBoundApplication.appInfo.flags) & 2) != 0 || Build.IS_DEBUGGABLE) {
                    z = true;
                }
                protoOutputStream2.write(1138166333445L, Debug.getUnreachableMemory(100, z));
                return;
            }
            i = globalAssetCount3;
        }

        public void dumpGfxInfo(ParcelFileDescriptor pfd, String[] args) {
            ActivityThread.this.nDumpGraphicsInfo(pfd.getFileDescriptor());
            WindowManagerGlobal.getInstance().dumpGfxInfo(pfd.getFileDescriptor(), args);
            IoUtils.closeQuietly(pfd);
        }

        private File getDatabasesDir(Context context) {
            return context.getDatabasePath(FullBackup.APK_TREE_TOKEN).getParentFile();
        }

        private void dumpDatabaseInfo(ParcelFileDescriptor pfd, String[] args, boolean isSystem) {
            PrintWriter pw = new FastPrintWriter(new FileOutputStream(pfd.getFileDescriptor()));
            SQLiteDebug.dump(new PrintWriterPrinter(pw), args, isSystem);
            pw.flush();
        }

        public void dumpDbInfo(ParcelFileDescriptor pfd, final String[] args) {
            if (ActivityThread.this.mSystemThread) {
                ParcelFileDescriptor dup;
                try {
                    dup = pfd.dup();
                    AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                        public void run() {
                            try {
                                ApplicationThread.this.dumpDatabaseInfo(dup, args, true);
                            } finally {
                                IoUtils.closeQuietly(dup);
                            }
                        }
                    });
                } catch (IOException e) {
                    dup = e;
                    String str = ActivityThread.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Could not dup FD ");
                    stringBuilder.append(pfd.getFileDescriptor().getInt$());
                    Log.w(str, stringBuilder.toString());
                } finally {
                    IoUtils.closeQuietly(pfd);
                }
            } else {
                dumpDatabaseInfo(pfd, args, false);
                IoUtils.closeQuietly(pfd);
            }
        }

        public void unstableProviderDied(IBinder provider) {
            ActivityThread.this.sendMessage(142, provider);
        }

        public void requestAssistContextExtras(IBinder activityToken, IBinder requestToken, int requestType, int sessionId, int flags) {
            RequestAssistContextExtras cmd = new RequestAssistContextExtras();
            cmd.activityToken = activityToken;
            cmd.requestToken = requestToken;
            cmd.requestType = requestType;
            cmd.sessionId = sessionId;
            cmd.flags = flags;
            ActivityThread.this.sendMessage(143, cmd);
        }

        public void setCoreSettings(Bundle coreSettings) {
            ActivityThread.this.sendMessage(138, coreSettings);
        }

        public void updatePackageCompatibilityInfo(String pkg, CompatibilityInfo info) {
            UpdateCompatibilityData ucd = new UpdateCompatibilityData();
            ucd.pkg = pkg;
            ucd.info = info;
            ActivityThread.this.sendMessage(139, ucd);
        }

        public void scheduleTrimMemory(int level) {
            Runnable r = PooledLambda.obtainRunnable(-$$Lambda$ActivityThread$ApplicationThread$tUGFX7CUhzB4Pg5wFd5yeqOnu38.INSTANCE, ActivityThread.this, Integer.valueOf(level)).recycleOnUse();
            Choreographer choreographer = Choreographer.getMainThreadInstance();
            if (choreographer != null) {
                choreographer.postCallback(4, r, null);
            } else {
                ActivityThread.this.mH.post(r);
            }
        }

        public void scheduleTranslucentConversionComplete(IBinder token, boolean drawComplete) {
            ActivityThread.this.sendMessage(144, token, drawComplete);
        }

        public void scheduleOnNewActivityOptions(IBinder token, Bundle options) {
            ActivityThread.this.sendMessage(146, new Pair(token, ActivityOptions.fromBundle(options)));
        }

        public void setProcessState(int state) {
            ActivityThread.this.updateProcessState(state, true);
        }

        public void setNetworkBlockSeq(long procStateSeq) {
            synchronized (ActivityThread.this.mNetworkPolicyLock) {
                ActivityThread.this.mNetworkBlockSeq = procStateSeq;
            }
        }

        public void scheduleInstallProvider(ProviderInfo provider) {
            ActivityThread.this.sendMessage(145, provider);
        }

        public final void updateTimePrefs(int timeFormatPreference) {
            Boolean timeFormatPreferenceBool;
            if (timeFormatPreference == 0) {
                timeFormatPreferenceBool = Boolean.FALSE;
            } else if (timeFormatPreference == 1) {
                timeFormatPreferenceBool = Boolean.TRUE;
            } else {
                timeFormatPreferenceBool = null;
            }
            DateFormat.set24HourTimePref(timeFormatPreferenceBool);
        }

        public void scheduleEnterAnimationComplete(IBinder token) {
            ActivityThread.this.sendMessage(149, token);
        }

        public void notifyCleartextNetwork(byte[] firstPacket) {
            if (StrictMode.vmCleartextNetworkEnabled()) {
                StrictMode.onCleartextNetworkDetected(firstPacket);
            }
        }

        public void startBinderTracking() {
            ActivityThread.this.sendMessage(150, null);
        }

        public void stopBinderTrackingAndDump(ParcelFileDescriptor pfd) {
            try {
                ActivityThread.this.sendMessage(151, pfd.dup());
            } catch (IOException e) {
            } catch (Throwable th) {
                IoUtils.closeQuietly(pfd);
            }
            IoUtils.closeQuietly(pfd);
        }

        public void scheduleLocalVoiceInteractionStarted(IBinder token, IVoiceInteractor voiceInteractor) throws RemoteException {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = token;
            args.arg2 = voiceInteractor;
            ActivityThread.this.sendMessage(154, args);
        }

        public void handleTrustStorageUpdate() {
            NetworkSecurityPolicy.getInstance().handleTrustStorageUpdate();
        }

        public void notifyPackageForeground() {
            if (ActivityThread.sWaitingToUse) {
                ActivityThread.sWaitingToUse = false;
            }
        }

        public void scheduleTransaction(ClientTransaction transaction) throws RemoteException {
            ActivityThread.this.scheduleTransaction(transaction);
        }

        public void requestDirectActions(IBinder activityToken, IVoiceInteractor interactor, RemoteCallback cancellationCallback, RemoteCallback callback) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            if (cancellationCallback != null) {
                ICancellationSignal transport = ActivityThread.this.createSafeCancellationTransport(cancellationSignal);
                Bundle cancellationResult = new Bundle();
                cancellationResult.putBinder(VoiceInteractor.KEY_CANCELLATION_SIGNAL, transport.asBinder());
                cancellationCallback.sendResult(cancellationResult);
            }
            ActivityThread.this.mH.sendMessage(PooledLambda.obtainMessage(-$$Lambda$ActivityThread$ApplicationThread$uR_ee-5oPoxu4U_by7wU55jwtdU.INSTANCE, ActivityThread.this, activityToken, interactor, cancellationSignal, callback));
        }

        public void performDirectAction(IBinder activityToken, String actionId, Bundle arguments, RemoteCallback cancellationCallback, RemoteCallback resultCallback) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            if (cancellationCallback != null) {
                ICancellationSignal transport = ActivityThread.this.createSafeCancellationTransport(cancellationSignal);
                Bundle cancellationResult = new Bundle();
                cancellationResult.putBinder(VoiceInteractor.KEY_CANCELLATION_SIGNAL, transport.asBinder());
                cancellationCallback.sendResult(cancellationResult);
            }
            ActivityThread.this.mH.sendMessage(PooledLambda.obtainMessage(-$$Lambda$ActivityThread$ApplicationThread$nBC_BR7B9W6ftKAxur3BC53SJYc.INSTANCE, ActivityThread.this, activityToken, actionId, arguments, cancellationSignal, resultCallback));
        }
    }

    static final class BindServiceData {
        @UnsupportedAppUsage
        Intent intent;
        boolean rebind;
        @UnsupportedAppUsage
        IBinder token;

        BindServiceData() {
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("BindServiceData{token=");
            stringBuilder.append(this.token);
            stringBuilder.append(" intent=");
            stringBuilder.append(this.intent);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    static final class ContextCleanupInfo {
        ContextImpl context;
        String what;
        String who;

        ContextCleanupInfo() {
        }
    }

    static final class CreateBackupAgentData {
        ApplicationInfo appInfo;
        int backupMode;
        CompatibilityInfo compatInfo;
        int userId;

        CreateBackupAgentData() {
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CreateBackupAgentData{appInfo=");
            stringBuilder.append(this.appInfo);
            stringBuilder.append(" backupAgent=");
            stringBuilder.append(this.appInfo.backupAgentName);
            stringBuilder.append(" mode=");
            stringBuilder.append(this.backupMode);
            stringBuilder.append(" userId=");
            stringBuilder.append(this.userId);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    static final class CreateServiceData {
        @UnsupportedAppUsage
        CompatibilityInfo compatInfo;
        @UnsupportedAppUsage
        ServiceInfo info;
        @UnsupportedAppUsage
        Intent intent;
        @UnsupportedAppUsage
        IBinder token;

        CreateServiceData() {
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CreateServiceData{token=");
            stringBuilder.append(this.token);
            stringBuilder.append(" className=");
            stringBuilder.append(this.info.name);
            stringBuilder.append(" packageName=");
            stringBuilder.append(this.info.packageName);
            stringBuilder.append(" intent=");
            stringBuilder.append(this.intent);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    static final class DumpComponentInfo {
        String[] args;
        ParcelFileDescriptor fd;
        String prefix;
        IBinder token;

        DumpComponentInfo() {
        }
    }

    static final class DumpHeapData {
        ParcelFileDescriptor fd;
        RemoteCallback finishCallback;
        public boolean mallocInfo;
        public boolean managed;
        String path;
        public boolean runGc;

        DumpHeapData() {
        }
    }

    final class GcIdler implements IdleHandler {
        GcIdler() {
        }

        public final boolean queueIdle() {
            ActivityThread.this.doGcIfNeeded();
            ActivityThread.this.purgePendingResources();
            return false;
        }
    }

    class H extends Handler {
        public static final int APPLICATION_INFO_CHANGED = 156;
        public static final int ATTACH_AGENT = 155;
        public static final int BIND_APPLICATION = 110;
        @UnsupportedAppUsage
        public static final int BIND_SERVICE = 121;
        public static final int CLEAN_UP_CONTEXT = 119;
        public static final int CONFIGURATION_CHANGED = 118;
        public static final int CREATE_BACKUP_AGENT = 128;
        @UnsupportedAppUsage
        public static final int CREATE_SERVICE = 114;
        public static final int DESTROY_BACKUP_AGENT = 129;
        public static final int DISPATCH_PACKAGE_BROADCAST = 133;
        public static final int DUMP_ACTIVITY = 136;
        public static final int DUMP_HEAP = 135;
        @UnsupportedAppUsage
        public static final int DUMP_PROVIDER = 141;
        public static final int DUMP_SERVICE = 123;
        @UnsupportedAppUsage
        public static final int ENTER_ANIMATION_COMPLETE = 149;
        public static final int EXECUTE_TRANSACTION = 159;
        @UnsupportedAppUsage
        public static final int EXIT_APPLICATION = 111;
        @UnsupportedAppUsage
        public static final int GC_WHEN_IDLE = 120;
        @UnsupportedAppUsage
        public static final int INSTALL_PROVIDER = 145;
        public static final int LOCAL_VOICE_INTERACTION_STARTED = 154;
        public static final int LOW_MEMORY = 124;
        public static final int ON_NEW_ACTIVITY_OPTIONS = 146;
        public static final int PROFILER_CONTROL = 127;
        public static final int PURGE_RESOURCES = 161;
        @UnsupportedAppUsage
        public static final int RECEIVER = 113;
        public static final int RELAUNCH_ACTIVITY = 160;
        @UnsupportedAppUsage
        public static final int REMOVE_PROVIDER = 131;
        public static final int REQUEST_ASSIST_CONTEXT_EXTRAS = 143;
        public static final int RUN_ISOLATED_ENTRY_POINT = 158;
        @UnsupportedAppUsage
        public static final int SCHEDULE_CRASH = 134;
        @UnsupportedAppUsage
        public static final int SERVICE_ARGS = 115;
        public static final int SET_CORE_SETTINGS = 138;
        public static final int SLEEPING = 137;
        public static final int START_BINDER_TRACKING = 150;
        public static final int STOP_BINDER_TRACKING_AND_DUMP = 151;
        @UnsupportedAppUsage
        public static final int STOP_SERVICE = 116;
        public static final int SUICIDE = 130;
        public static final int TRANSLUCENT_CONVERSION_COMPLETE = 144;
        @UnsupportedAppUsage
        public static final int UNBIND_SERVICE = 122;
        public static final int UNSTABLE_PROVIDER_DIED = 142;
        public static final int UPDATE_PACKAGE_COMPATIBILITY_INFO = 139;

        H() {
        }

        /* Access modifiers changed, original: 0000 */
        public String codeToString(int code) {
            return Integer.toString(code);
        }

        public void handleMessage(Message msg) {
            long startTime = SystemClock.uptimeMillis();
            boolean z = true;
            StringBuilder stringBuilder;
            ActivityThread activityThread;
            switch (msg.what) {
                case 110:
                    Trace.traceBegin(64, "bindApplication");
                    ActivityThread.this.handleBindApplication(msg.obj);
                    Trace.traceEnd(64);
                    break;
                case 111:
                    if (ActivityThread.this.mInitialApplication != null) {
                        ActivityThread.this.mInitialApplication.onTerminate();
                    }
                    Looper.myLooper().quit();
                    break;
                case 113:
                    Trace.traceBegin(64, "broadcastReceiveComp");
                    ActivityThread.this.handleReceiver((ReceiverData) msg.obj);
                    Trace.traceEnd(64);
                    break;
                case 114:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("serviceCreate: ");
                    stringBuilder.append(String.valueOf(msg.obj));
                    Trace.traceBegin(64, stringBuilder.toString());
                    ActivityThread.this.handleCreateService((CreateServiceData) msg.obj);
                    Trace.traceEnd(64);
                    break;
                case 115:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("serviceStart: ");
                    stringBuilder.append(String.valueOf(msg.obj));
                    Trace.traceBegin(64, stringBuilder.toString());
                    ActivityThread.this.handleServiceArgs((ServiceArgsData) msg.obj);
                    Trace.traceEnd(64);
                    break;
                case 116:
                    Trace.traceBegin(64, "serviceStop");
                    ActivityThread.this.handleStopService((IBinder) msg.obj);
                    ActivityThread.this.schedulePurgeIdler();
                    Trace.traceEnd(64);
                    break;
                case 118:
                    ActivityThread.this.handleConfigurationChanged((Configuration) msg.obj);
                    break;
                case 119:
                    ContextCleanupInfo cci = msg.obj;
                    cci.context.performFinalCleanup(cci.who, cci.what);
                    break;
                case 120:
                    ActivityThread.this.scheduleGcIdler();
                    break;
                case 121:
                    Trace.traceBegin(64, "serviceBind");
                    ActivityThread.this.handleBindService((BindServiceData) msg.obj);
                    Trace.traceEnd(64);
                    break;
                case 122:
                    Trace.traceBegin(64, "serviceUnbind");
                    ActivityThread.this.handleUnbindService((BindServiceData) msg.obj);
                    ActivityThread.this.schedulePurgeIdler();
                    Trace.traceEnd(64);
                    break;
                case 123:
                    ActivityThread.this.handleDumpService((DumpComponentInfo) msg.obj);
                    break;
                case 124:
                    Trace.traceBegin(64, "lowMemory");
                    ActivityThread.this.handleLowMemory();
                    Trace.traceEnd(64);
                    break;
                case 127:
                    activityThread = ActivityThread.this;
                    if (msg.arg1 == 0) {
                        z = false;
                    }
                    activityThread.handleProfilerControl(z, (ProfilerInfo) msg.obj, msg.arg2);
                    break;
                case 128:
                    Trace.traceBegin(64, "backupCreateAgent");
                    ActivityThread.this.handleCreateBackupAgent((CreateBackupAgentData) msg.obj);
                    Trace.traceEnd(64);
                    break;
                case 129:
                    Trace.traceBegin(64, "backupDestroyAgent");
                    ActivityThread.this.handleDestroyBackupAgent((CreateBackupAgentData) msg.obj);
                    Trace.traceEnd(64);
                    break;
                case 130:
                    Process.killProcess(Process.myPid());
                    break;
                case 131:
                    Trace.traceBegin(64, "providerRemove");
                    ActivityThread.this.completeRemoveProvider((ProviderRefCount) msg.obj);
                    Trace.traceEnd(64);
                    break;
                case 133:
                    Trace.traceBegin(64, "broadcastPackage");
                    ActivityThread.this.handleDispatchPackageBroadcast(msg.arg1, (String[]) msg.obj);
                    Trace.traceEnd(64);
                    break;
                case 134:
                    throw new RemoteServiceException((String) msg.obj);
                case 135:
                    ActivityThread.handleDumpHeap((DumpHeapData) msg.obj);
                    break;
                case 136:
                    ActivityThread.this.handleDumpActivity((DumpComponentInfo) msg.obj);
                    break;
                case 137:
                    Trace.traceBegin(64, "sleeping");
                    activityThread = ActivityThread.this;
                    IBinder iBinder = (IBinder) msg.obj;
                    if (msg.arg1 == 0) {
                        z = false;
                    }
                    activityThread.handleSleeping(iBinder, z);
                    Trace.traceEnd(64);
                    break;
                case 138:
                    Trace.traceBegin(64, "setCoreSettings");
                    ActivityThread.this.handleSetCoreSettings((Bundle) msg.obj);
                    Trace.traceEnd(64);
                    break;
                case 139:
                    ActivityThread.this.handleUpdatePackageCompatibilityInfo((UpdateCompatibilityData) msg.obj);
                    break;
                case 141:
                    ActivityThread.this.handleDumpProvider((DumpComponentInfo) msg.obj);
                    break;
                case 142:
                    ActivityThread.this.handleUnstableProviderDied((IBinder) msg.obj, false);
                    break;
                case 143:
                    ActivityThread.this.handleRequestAssistContextExtras((RequestAssistContextExtras) msg.obj);
                    break;
                case 144:
                    activityThread = ActivityThread.this;
                    IBinder iBinder2 = (IBinder) msg.obj;
                    if (msg.arg1 != 1) {
                        z = false;
                    }
                    activityThread.handleTranslucentConversionComplete(iBinder2, z);
                    break;
                case 145:
                    ActivityThread.this.handleInstallProvider((ProviderInfo) msg.obj);
                    break;
                case 146:
                    Pair<IBinder, ActivityOptions> pair = msg.obj;
                    ActivityThread.this.onNewActivityOptions((IBinder) pair.first, (ActivityOptions) pair.second);
                    break;
                case 149:
                    ActivityThread.this.handleEnterAnimationComplete((IBinder) msg.obj);
                    break;
                case 150:
                    ActivityThread.this.handleStartBinderTracking();
                    break;
                case 151:
                    ActivityThread.this.handleStopBinderTrackingAndDump((ParcelFileDescriptor) msg.obj);
                    break;
                case 154:
                    ActivityThread.this.handleLocalVoiceInteractionStarted((IBinder) ((SomeArgs) msg.obj).arg1, (IVoiceInteractor) ((SomeArgs) msg.obj).arg2);
                    break;
                case 155:
                    Application app = ActivityThread.this.getApplication();
                    ActivityThread.handleAttachAgent((String) msg.obj, app != null ? app.mLoadedApk : null);
                    break;
                case 156:
                    activityThread = ActivityThread.this;
                    activityThread.mUpdatingSystemConfig = true;
                    try {
                        activityThread.handleApplicationInfoChanged((ApplicationInfo) msg.obj);
                        break;
                    } finally {
                        ActivityThread.this.mUpdatingSystemConfig = false;
                    }
                case 158:
                    ActivityThread.this.handleRunIsolatedEntryPoint((String) ((SomeArgs) msg.obj).arg1, (String[]) ((SomeArgs) msg.obj).arg2);
                    break;
                case 159:
                    ClientTransaction transaction = msg.obj;
                    ActivityThread.this.mTransactionExecutor.execute(transaction);
                    if (ActivityThread.isSystem()) {
                        transaction.recycle();
                        break;
                    }
                    break;
                case 160:
                    ActivityThread.this.handleRelaunchActivityLocally((IBinder) msg.obj);
                    break;
                case 161:
                    ActivityThread.this.schedulePurgeIdler();
                    break;
            }
            Object obj = msg.obj;
            if (obj instanceof SomeArgs) {
                ((SomeArgs) obj).recycle();
            }
            ActivityThreadInjector.checkHandleMessageTime(startTime, msg);
        }
    }

    private class Idler implements IdleHandler {
        private Idler() {
        }

        /* synthetic */ Idler(ActivityThread x0, AnonymousClass1 x1) {
            this();
        }

        public final boolean queueIdle() {
            ActivityClientRecord a = ActivityThread.this.mNewActivities;
            boolean stopProfiling = false;
            if (!(ActivityThread.this.mBoundApplication == null || ActivityThread.this.mProfiler.profileFd == null || !ActivityThread.this.mProfiler.autoStopProfiler)) {
                stopProfiling = true;
            }
            if (a != null) {
                ActivityThread.this.mNewActivities = null;
                IActivityTaskManager am = ActivityTaskManager.getService();
                do {
                    if (!(a.activity == null || a.activity.mFinished)) {
                        try {
                            am.activityIdle(a.token, a.createdConfig, stopProfiling);
                            a.createdConfig = null;
                        } catch (RemoteException ex) {
                            throw ex.rethrowFromSystemServer();
                        }
                    }
                    ActivityClientRecord prev = a;
                    a = a.nextIdle;
                    prev.nextIdle = null;
                } while (a != null);
            }
            if (stopProfiling) {
                ActivityThread.this.mProfiler.stopProfiling();
            }
            ActivityThread.this.applyPendingProcessState();
            return false;
        }
    }

    static final class Profiler {
        boolean autoStopProfiler;
        boolean handlingProfiling;
        ParcelFileDescriptor profileFd;
        String profileFile;
        boolean profiling;
        int samplingInterval;
        boolean streamingOutput;

        Profiler() {
        }

        public void setProfiler(ProfilerInfo profilerInfo) {
            ParcelFileDescriptor fd = profilerInfo.profileFd;
            if (this.profiling) {
                if (fd != null) {
                    try {
                        fd.close();
                    } catch (IOException e) {
                    }
                }
                return;
            }
            ParcelFileDescriptor parcelFileDescriptor = this.profileFd;
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException e2) {
                }
            }
            this.profileFile = profilerInfo.profileFile;
            this.profileFd = fd;
            this.samplingInterval = profilerInfo.samplingInterval;
            this.autoStopProfiler = profilerInfo.autoStopProfiler;
            this.streamingOutput = profilerInfo.streamingOutput;
        }

        public void startProfiling() {
            if (this.profileFd != null && !this.profiling) {
                try {
                    VMDebug.startMethodTracing(this.profileFile, this.profileFd.getFileDescriptor(), (SystemProperties.getInt("debug.traceview-buffer-size-mb", 8) * 1024) * 1024, 0, this.samplingInterval != 0, this.samplingInterval, this.streamingOutput);
                    this.profiling = true;
                } catch (RuntimeException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Profiling failed on path ");
                    stringBuilder.append(this.profileFile);
                    String stringBuilder2 = stringBuilder.toString();
                    String str = ActivityThread.TAG;
                    Slog.w(str, stringBuilder2, e);
                    try {
                        this.profileFd.close();
                        this.profileFd = null;
                    } catch (IOException e2) {
                        Slog.w(str, "Failure closing profile fd", e2);
                    }
                }
            }
        }

        public void stopProfiling() {
            if (this.profiling) {
                this.profiling = false;
                Debug.stopMethodTracing();
                ParcelFileDescriptor parcelFileDescriptor = this.profileFd;
                if (parcelFileDescriptor != null) {
                    try {
                        parcelFileDescriptor.close();
                    } catch (IOException e) {
                    }
                }
                this.profileFd = null;
                this.profileFile = null;
            }
        }
    }

    static final class ProviderAcquiringCount {
        public int acquiringCount;

        ProviderAcquiringCount(int aCount) {
            this.acquiringCount = aCount;
        }
    }

    final class ProviderClientRecord {
        @UnsupportedAppUsage
        final ContentProviderHolder mHolder;
        @UnsupportedAppUsage
        final ContentProvider mLocalProvider;
        final String[] mNames;
        @UnsupportedAppUsage
        final IContentProvider mProvider;

        ProviderClientRecord(String[] names, IContentProvider provider, ContentProvider localProvider, ContentProviderHolder holder) {
            this.mNames = names;
            this.mProvider = provider;
            this.mLocalProvider = localProvider;
            this.mHolder = holder;
        }
    }

    private static final class ProviderKey {
        final String authority;
        final int userId;

        public ProviderKey(String authority, int userId) {
            this.authority = authority;
            this.userId = userId;
        }

        public boolean equals(Object o) {
            boolean z = false;
            if (!(o instanceof ProviderKey)) {
                return false;
            }
            ProviderKey other = (ProviderKey) o;
            if (Objects.equals(this.authority, other.authority) && this.userId == other.userId) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            String str = this.authority;
            return (str != null ? str.hashCode() : 0) ^ this.userId;
        }
    }

    private static final class ProviderRefCount {
        public final ProviderClientRecord client;
        public final ContentProviderHolder holder;
        public boolean removePending;
        public int stableCount;
        public int unstableCount;

        ProviderRefCount(ContentProviderHolder inHolder, ProviderClientRecord inClient, int sCount, int uCount) {
            this.holder = inHolder;
            this.client = inClient;
            this.stableCount = sCount;
            this.unstableCount = uCount;
        }
    }

    final class PurgeIdler implements IdleHandler {
        PurgeIdler() {
        }

        public boolean queueIdle() {
            ActivityThread.this.purgePendingResources();
            return false;
        }
    }

    static final class ReceiverData extends PendingResult {
        @UnsupportedAppUsage
        CompatibilityInfo compatInfo;
        @UnsupportedAppUsage
        ActivityInfo info;
        @UnsupportedAppUsage
        Intent intent;

        public ReceiverData(Intent intent, int resultCode, String resultData, Bundle resultExtras, boolean ordered, boolean sticky, IBinder token, int sendingUser) {
            super(resultCode, resultData, resultExtras, 0, ordered, sticky, token, sendingUser, intent.getFlags());
            this.intent = intent;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ReceiverData{intent=");
            stringBuilder.append(this.intent);
            stringBuilder.append(" packageName=");
            stringBuilder.append(this.info.packageName);
            stringBuilder.append(" resultCode=");
            stringBuilder.append(getResultCode());
            stringBuilder.append(" resultData=");
            stringBuilder.append(getResultData());
            stringBuilder.append(" resultExtras=");
            stringBuilder.append(getResultExtras(false));
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    static final class RequestAssistContextExtras {
        IBinder activityToken;
        int flags;
        IBinder requestToken;
        int requestType;
        int sessionId;

        RequestAssistContextExtras() {
        }
    }

    private static final class SafeCancellationTransport extends ICancellationSignal.Stub {
        private final WeakReference<ActivityThread> mWeakActivityThread;

        SafeCancellationTransport(ActivityThread activityThread, CancellationSignal cancellation) {
            this.mWeakActivityThread = new WeakReference(activityThread);
        }

        public void cancel() {
            ActivityThread activityThread = (ActivityThread) this.mWeakActivityThread.get();
            if (activityThread != null) {
                CancellationSignal cancellation = activityThread.removeSafeCancellationTransport(this);
                if (cancellation != null) {
                    cancellation.cancel();
                }
            }
        }
    }

    static final class ServiceArgsData {
        @UnsupportedAppUsage
        Intent args;
        int flags;
        int startId;
        boolean taskRemoved;
        @UnsupportedAppUsage
        IBinder token;

        ServiceArgsData() {
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ServiceArgsData{token=");
            stringBuilder.append(this.token);
            stringBuilder.append(" startId=");
            stringBuilder.append(this.startId);
            stringBuilder.append(" args=");
            stringBuilder.append(this.args);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    static final class UpdateCompatibilityData {
        CompatibilityInfo info;
        String pkg;

        UpdateCompatibilityData() {
        }
    }

    private native void nDumpGraphicsInfo(FileDescriptor fileDescriptor);

    private native void nInitZygoteChildHeapProfiling();

    private native void nPurgePendingResources();

    private SafeCancellationTransport createSafeCancellationTransport(CancellationSignal cancellationSignal) {
        SafeCancellationTransport transport;
        synchronized (this) {
            if (this.mRemoteCancellations == null) {
                this.mRemoteCancellations = new ArrayMap();
            }
            transport = new SafeCancellationTransport(this, cancellationSignal);
            this.mRemoteCancellations.put(transport, cancellationSignal);
        }
        return transport;
    }

    private CancellationSignal removeSafeCancellationTransport(SafeCancellationTransport transport) {
        CancellationSignal cancellation;
        synchronized (this) {
            cancellation = (CancellationSignal) this.mRemoteCancellations.remove(transport);
            if (this.mRemoteCancellations.isEmpty()) {
                this.mRemoteCancellations = null;
            }
        }
        return cancellation;
    }

    public static boolean isWaitingToUse() {
        return sWaitingToUse;
    }

    @UnsupportedAppUsage
    public static ActivityThread currentActivityThread() {
        return sCurrentActivityThread;
    }

    public static boolean isSystem() {
        return sCurrentActivityThread != null ? sCurrentActivityThread.mSystemThread : false;
    }

    public static String currentOpPackageName() {
        ActivityThread am = currentActivityThread();
        return (am == null || am.getApplication() == null) ? null : am.getApplication().getOpPackageName();
    }

    @UnsupportedAppUsage
    public static String currentPackageName() {
        ActivityThread am = currentActivityThread();
        if (am != null) {
            AppBindData appBindData = am.mBoundApplication;
            if (appBindData != null) {
                return appBindData.appInfo.packageName;
            }
        }
        return null;
    }

    @UnsupportedAppUsage
    public static String currentProcessName() {
        ActivityThread am = currentActivityThread();
        if (am != null) {
            AppBindData appBindData = am.mBoundApplication;
            if (appBindData != null) {
                return appBindData.processName;
            }
        }
        return null;
    }

    @UnsupportedAppUsage
    public static Application currentApplication() {
        ActivityThread am = currentActivityThread();
        return am != null ? am.mInitialApplication : null;
    }

    @UnsupportedAppUsage
    public static IPackageManager getPackageManager() {
        if (sPackageManager != null) {
            return sPackageManager;
        }
        sPackageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        return sPackageManager;
    }

    /* Access modifiers changed, original: 0000 */
    public Configuration applyConfigCompatMainThread(int displayDensity, Configuration config, CompatibilityInfo compat) {
        if (config == null) {
            return null;
        }
        if (!compat.supportsScreen()) {
            this.mMainThreadConfig.setTo(config);
            config = this.mMainThreadConfig;
            compat.applyToConfiguration(displayDensity, config);
        }
        return config;
    }

    /* Access modifiers changed, original: 0000 */
    public Resources getTopLevelResources(String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, LoadedApk pkgInfo) {
        return this.mResourcesManager.getResources(null, resDir, splitResDirs, overlayDirs, libDirs, displayId, null, pkgInfo.getCompatibilityInfo(), pkgInfo.getClassLoader());
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final Handler getHandler() {
        return this.mH;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final LoadedApk getPackageInfo(String packageName, CompatibilityInfo compatInfo, int flags) {
        return getPackageInfo(packageName, compatInfo, flags, UserHandle.myUserId());
    }

    /* JADX WARNING: Missing block: B:36:0x008e, code skipped:
            return r5;
     */
    /* JADX WARNING: Missing block: B:38:0x0090, code skipped:
            if (r1 == null) goto L_0x0097;
     */
    /* JADX WARNING: Missing block: B:40:0x0096, code skipped:
            return getPackageInfo(r1, r10, r11);
     */
    /* JADX WARNING: Missing block: B:41:0x0097, code skipped:
            return null;
     */
    public final android.app.LoadedApk getPackageInfo(java.lang.String r9, android.content.res.CompatibilityInfo r10, int r11, int r12) {
        /*
        r8 = this;
        r0 = android.os.UserHandle.myUserId();
        if (r0 == r12) goto L_0x0008;
    L_0x0006:
        r0 = 1;
        goto L_0x0009;
    L_0x0008:
        r0 = 0;
    L_0x0009:
        r1 = getPackageManager();	 Catch:{ RemoteException -> 0x009b }
        r2 = 268436480; // 0x10000400 float:2.524663E-29 double:1.32625243E-315;
        if (r12 >= 0) goto L_0x0017;
    L_0x0012:
        r3 = android.os.UserHandle.myUserId();	 Catch:{ RemoteException -> 0x009b }
        goto L_0x0018;
    L_0x0017:
        r3 = r12;
    L_0x0018:
        r1 = r1.getApplicationInfo(r9, r2, r3);	 Catch:{ RemoteException -> 0x009b }
        r2 = r8.mResourcesManager;
        monitor-enter(r2);
        if (r0 == 0) goto L_0x0024;
    L_0x0022:
        r3 = 0;
        goto L_0x0039;
    L_0x0024:
        r3 = r11 & 1;
        if (r3 == 0) goto L_0x0031;
    L_0x0028:
        r3 = r8.mPackages;	 Catch:{ all -> 0x0098 }
        r3 = r3.get(r9);	 Catch:{ all -> 0x0098 }
        r3 = (java.lang.ref.WeakReference) r3;	 Catch:{ all -> 0x0098 }
        goto L_0x0039;
    L_0x0031:
        r3 = r8.mResourcePackages;	 Catch:{ all -> 0x0098 }
        r3 = r3.get(r9);	 Catch:{ all -> 0x0098 }
        r3 = (java.lang.ref.WeakReference) r3;	 Catch:{ all -> 0x0098 }
    L_0x0039:
        r4 = 0;
        if (r3 == 0) goto L_0x0043;
    L_0x003c:
        r5 = r3.get();	 Catch:{ all -> 0x0098 }
        r5 = (android.app.LoadedApk) r5;	 Catch:{ all -> 0x0098 }
        goto L_0x0044;
    L_0x0043:
        r5 = r4;
    L_0x0044:
        if (r1 == 0) goto L_0x008f;
    L_0x0046:
        if (r5 == 0) goto L_0x008f;
    L_0x0048:
        r6 = isLoadedApkResourceDirsUpToDate(r5, r1);	 Catch:{ all -> 0x0098 }
        if (r6 != 0) goto L_0x0051;
    L_0x004e:
        r5.updateApplicationInfo(r1, r4);	 Catch:{ all -> 0x0098 }
    L_0x0051:
        r4 = r5.isSecurityViolation();	 Catch:{ all -> 0x0098 }
        if (r4 == 0) goto L_0x008d;
    L_0x0057:
        r4 = r11 & 2;
        if (r4 == 0) goto L_0x005c;
    L_0x005b:
        goto L_0x008d;
    L_0x005c:
        r4 = new java.lang.SecurityException;	 Catch:{ all -> 0x0098 }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0098 }
        r6.<init>();	 Catch:{ all -> 0x0098 }
        r7 = "Requesting code from ";
        r6.append(r7);	 Catch:{ all -> 0x0098 }
        r6.append(r9);	 Catch:{ all -> 0x0098 }
        r7 = " to be run in process ";
        r6.append(r7);	 Catch:{ all -> 0x0098 }
        r7 = r8.mBoundApplication;	 Catch:{ all -> 0x0098 }
        r7 = r7.processName;	 Catch:{ all -> 0x0098 }
        r6.append(r7);	 Catch:{ all -> 0x0098 }
        r7 = "/";
        r6.append(r7);	 Catch:{ all -> 0x0098 }
        r7 = r8.mBoundApplication;	 Catch:{ all -> 0x0098 }
        r7 = r7.appInfo;	 Catch:{ all -> 0x0098 }
        r7 = r7.uid;	 Catch:{ all -> 0x0098 }
        r6.append(r7);	 Catch:{ all -> 0x0098 }
        r6 = r6.toString();	 Catch:{ all -> 0x0098 }
        r4.<init>(r6);	 Catch:{ all -> 0x0098 }
        throw r4;	 Catch:{ all -> 0x0098 }
    L_0x008d:
        monitor-exit(r2);	 Catch:{ all -> 0x0098 }
        return r5;
    L_0x008f:
        monitor-exit(r2);	 Catch:{ all -> 0x0098 }
        if (r1 == 0) goto L_0x0097;
    L_0x0092:
        r2 = r8.getPackageInfo(r1, r10, r11);
        return r2;
    L_0x0097:
        return r4;
    L_0x0098:
        r3 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0098 }
        throw r3;
    L_0x009b:
        r1 = move-exception;
        r2 = r1.rethrowFromSystemServer();
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.getPackageInfo(java.lang.String, android.content.res.CompatibilityInfo, int, int):android.app.LoadedApk");
    }

    @UnsupportedAppUsage
    public final LoadedApk getPackageInfo(ApplicationInfo ai, CompatibilityInfo compatInfo, int flags) {
        boolean includeCode = (flags & 1) != 0;
        boolean z = includeCode && ai.uid != 0 && ai.uid != 1000 && (this.mBoundApplication == null || !UserHandle.isSameApp(ai.uid, this.mBoundApplication.appInfo.uid));
        boolean securityViolation = z;
        boolean registerPackage = includeCode && (1073741824 & flags) != 0;
        if ((flags & 3) != 1 || !securityViolation) {
            return getPackageInfo(ai, compatInfo, null, securityViolation, includeCode, registerPackage);
        }
        String msg = new StringBuilder();
        msg.append("Requesting code from ");
        msg.append(ai.packageName);
        String str = " (with uid ";
        msg.append(str);
        msg.append(ai.uid);
        String str2 = ")";
        msg.append(str2);
        msg = msg.toString();
        if (this.mBoundApplication != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(msg);
            stringBuilder.append(" to be run in process ");
            stringBuilder.append(this.mBoundApplication.processName);
            stringBuilder.append(str);
            stringBuilder.append(this.mBoundApplication.appInfo.uid);
            stringBuilder.append(str2);
            msg = stringBuilder.toString();
        }
        throw new SecurityException(msg);
    }

    @UnsupportedAppUsage
    public final LoadedApk getPackageInfoNoCheck(ApplicationInfo ai, CompatibilityInfo compatInfo) {
        return getPackageInfo(ai, compatInfo, null, false, true, false);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final LoadedApk peekPackageInfo(String packageName, boolean includeCode) {
        LoadedApk loadedApk;
        synchronized (this.mResourcesManager) {
            WeakReference<LoadedApk> ref;
            if (includeCode) {
                ref = (WeakReference) this.mPackages.get(packageName);
            } else {
                ref = (WeakReference) this.mResourcePackages.get(packageName);
            }
            loadedApk = ref != null ? (LoadedApk) ref.get() : null;
        }
        return loadedApk;
    }

    /* JADX WARNING: Missing block: B:22:0x004b, code skipped:
            return r13;
     */
    /* JADX WARNING: Missing block: B:40:0x00a4, code skipped:
            return r1;
     */
    private android.app.LoadedApk getPackageInfo(android.content.pm.ApplicationInfo r16, android.content.res.CompatibilityInfo r17, java.lang.ClassLoader r18, boolean r19, boolean r20, boolean r21) {
        /*
        r15 = this;
        r9 = r15;
        r10 = r16;
        r0 = android.os.UserHandle.myUserId();
        r1 = r10.uid;
        r1 = android.os.UserHandle.getUserId(r1);
        r2 = 1;
        r3 = 0;
        if (r0 == r1) goto L_0x0013;
    L_0x0011:
        r0 = r2;
        goto L_0x0014;
    L_0x0013:
        r0 = r3;
    L_0x0014:
        r11 = r0;
        r12 = r9.mResourcesManager;
        monitor-enter(r12);
        if (r11 == 0) goto L_0x001c;
    L_0x001a:
        r0 = 0;
        goto L_0x0033;
    L_0x001c:
        if (r20 == 0) goto L_0x0029;
    L_0x001e:
        r0 = r9.mPackages;	 Catch:{ all -> 0x00a5 }
        r1 = r10.packageName;	 Catch:{ all -> 0x00a5 }
        r0 = r0.get(r1);	 Catch:{ all -> 0x00a5 }
        r0 = (java.lang.ref.WeakReference) r0;	 Catch:{ all -> 0x00a5 }
        goto L_0x0033;
    L_0x0029:
        r0 = r9.mResourcePackages;	 Catch:{ all -> 0x00a5 }
        r1 = r10.packageName;	 Catch:{ all -> 0x00a5 }
        r0 = r0.get(r1);	 Catch:{ all -> 0x00a5 }
        r0 = (java.lang.ref.WeakReference) r0;	 Catch:{ all -> 0x00a5 }
    L_0x0033:
        r1 = 0;
        if (r0 == 0) goto L_0x003d;
    L_0x0036:
        r4 = r0.get();	 Catch:{ all -> 0x00a5 }
        r4 = (android.app.LoadedApk) r4;	 Catch:{ all -> 0x00a5 }
        goto L_0x003e;
    L_0x003d:
        r4 = r1;
    L_0x003e:
        r13 = r4;
        if (r13 == 0) goto L_0x004c;
    L_0x0041:
        r2 = isLoadedApkResourceDirsUpToDate(r13, r10);	 Catch:{ all -> 0x00a5 }
        if (r2 != 0) goto L_0x004a;
    L_0x0047:
        r13.updateApplicationInfo(r10, r1);	 Catch:{ all -> 0x00a5 }
    L_0x004a:
        monitor-exit(r12);	 Catch:{ all -> 0x00a5 }
        return r13;
    L_0x004c:
        r14 = new android.app.LoadedApk;	 Catch:{ all -> 0x00a5 }
        if (r20 == 0) goto L_0x0058;
    L_0x0050:
        r1 = r10.flags;	 Catch:{ all -> 0x00a5 }
        r1 = r1 & 4;
        if (r1 == 0) goto L_0x0058;
    L_0x0056:
        r7 = r2;
        goto L_0x0059;
    L_0x0058:
        r7 = r3;
    L_0x0059:
        r1 = r14;
        r2 = r15;
        r3 = r16;
        r4 = r17;
        r5 = r18;
        r6 = r19;
        r8 = r21;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ all -> 0x00a5 }
        r1 = r14;
        r2 = r9.mSystemThread;	 Catch:{ all -> 0x00a5 }
        if (r2 == 0) goto L_0x0085;
    L_0x006d:
        r2 = "android";
        r3 = r10.packageName;	 Catch:{ all -> 0x00a5 }
        r2 = r2.equals(r3);	 Catch:{ all -> 0x00a5 }
        if (r2 == 0) goto L_0x0085;
        r2 = r15.getSystemContext();	 Catch:{ all -> 0x00a5 }
        r2 = r2.mPackageInfo;	 Catch:{ all -> 0x00a5 }
        r2 = r2.getClassLoader();	 Catch:{ all -> 0x00a5 }
        r1.installSystemApplicationInfo(r10, r2);	 Catch:{ all -> 0x00a5 }
    L_0x0085:
        if (r11 == 0) goto L_0x0088;
    L_0x0087:
        goto L_0x00a3;
    L_0x0088:
        if (r20 == 0) goto L_0x0097;
    L_0x008a:
        r2 = r9.mPackages;	 Catch:{ all -> 0x00a5 }
        r3 = r10.packageName;	 Catch:{ all -> 0x00a5 }
        r4 = new java.lang.ref.WeakReference;	 Catch:{ all -> 0x00a5 }
        r4.<init>(r1);	 Catch:{ all -> 0x00a5 }
        r2.put(r3, r4);	 Catch:{ all -> 0x00a5 }
        goto L_0x00a3;
    L_0x0097:
        r2 = r9.mResourcePackages;	 Catch:{ all -> 0x00a5 }
        r3 = r10.packageName;	 Catch:{ all -> 0x00a5 }
        r4 = new java.lang.ref.WeakReference;	 Catch:{ all -> 0x00a5 }
        r4.<init>(r1);	 Catch:{ all -> 0x00a5 }
        r2.put(r3, r4);	 Catch:{ all -> 0x00a5 }
    L_0x00a3:
        monitor-exit(r12);	 Catch:{ all -> 0x00a5 }
        return r1;
    L_0x00a5:
        r0 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x00a5 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.getPackageInfo(android.content.pm.ApplicationInfo, android.content.res.CompatibilityInfo, java.lang.ClassLoader, boolean, boolean, boolean):android.app.LoadedApk");
    }

    private static boolean isLoadedApkResourceDirsUpToDate(LoadedApk loadedApk, ApplicationInfo appInfo) {
        Resources packageResources = loadedApk.mResources;
        Object[] overlayDirs = ArrayUtils.defeatNullable(loadedApk.getOverlayDirs());
        Object[] resourceDirs = ArrayUtils.defeatNullable(appInfo.resourceDirs);
        return (packageResources == null || packageResources.getAssets().isUpToDate()) && overlayDirs.length == resourceDirs.length && ArrayUtils.containsAll(overlayDirs, resourceDirs);
    }

    @UnsupportedAppUsage
    ActivityThread() {
    }

    @UnsupportedAppUsage
    public ApplicationThread getApplicationThread() {
        return this.mAppThread;
    }

    @UnsupportedAppUsage
    public Instrumentation getInstrumentation() {
        return this.mInstrumentation;
    }

    public boolean isProfiling() {
        Profiler profiler = this.mProfiler;
        return (profiler == null || profiler.profileFile == null || this.mProfiler.profileFd != null) ? false : true;
    }

    public String getProfileFilePath() {
        return this.mProfiler.profileFile;
    }

    @UnsupportedAppUsage
    public Looper getLooper() {
        return this.mLooper;
    }

    public Executor getExecutor() {
        return this.mExecutor;
    }

    @UnsupportedAppUsage
    public Application getApplication() {
        return this.mInitialApplication;
    }

    @UnsupportedAppUsage
    public String getProcessName() {
        return this.mBoundApplication.processName;
    }

    @UnsupportedAppUsage
    public ContextImpl getSystemContext() {
        ContextImpl contextImpl;
        synchronized (this) {
            if (this.mSystemContext == null) {
                this.mSystemContext = ContextImpl.createSystemContext(this);
            }
            contextImpl = this.mSystemContext;
        }
        return contextImpl;
    }

    public ContextImpl getSystemUiContext() {
        ContextImpl contextImpl;
        synchronized (this) {
            if (this.mSystemUiContext == null) {
                this.mSystemUiContext = ContextImpl.createSystemUiContext(getSystemContext());
            }
            contextImpl = this.mSystemUiContext;
        }
        return contextImpl;
    }

    public ContextImpl createSystemUiContext(int displayId) {
        return ContextImpl.createSystemUiContext(getSystemUiContext(), displayId);
    }

    public void installSystemApplicationInfo(ApplicationInfo info, ClassLoader classLoader) {
        synchronized (this) {
            getSystemContext().installSystemApplicationInfo(info, classLoader);
            getSystemUiContext().installSystemApplicationInfo(info, classLoader);
            this.mProfiler = new Profiler();
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void scheduleGcIdler() {
        if (!this.mGcIdlerScheduled) {
            this.mGcIdlerScheduled = true;
            Looper.myQueue().addIdleHandler(this.mGcIdler);
        }
        this.mH.removeMessages(120);
    }

    /* Access modifiers changed, original: 0000 */
    public void unscheduleGcIdler() {
        if (this.mGcIdlerScheduled) {
            this.mGcIdlerScheduled = false;
            Looper.myQueue().removeIdleHandler(this.mGcIdler);
        }
        this.mH.removeMessages(120);
    }

    /* Access modifiers changed, original: 0000 */
    public void schedulePurgeIdler() {
        if (!this.mPurgeIdlerScheduled) {
            this.mPurgeIdlerScheduled = true;
            Looper.myQueue().addIdleHandler(this.mPurgeIdler);
        }
        this.mH.removeMessages(161);
    }

    /* Access modifiers changed, original: 0000 */
    public void unschedulePurgeIdler() {
        if (this.mPurgeIdlerScheduled) {
            this.mPurgeIdlerScheduled = false;
            Looper.myQueue().removeIdleHandler(this.mPurgeIdler);
        }
        this.mH.removeMessages(161);
    }

    /* Access modifiers changed, original: 0000 */
    public void doGcIfNeeded() {
        doGcIfNeeded("bg");
    }

    /* Access modifiers changed, original: 0000 */
    public void doGcIfNeeded(String reason) {
        this.mGcIdlerScheduled = false;
        if (BinderInternal.getLastGcTime() + 5000 < SystemClock.uptimeMillis()) {
            BinderInternal.forceGc(reason);
        }
    }

    static void printRow(PrintWriter pw, String format, Object... objs) {
        pw.println(String.format(format, objs));
    }

    public static void dumpMemInfoTable(PrintWriter pw, MemoryInfo memInfo, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, int pid, String processName, long nativeMax, long nativeAllocated, long nativeFree, long dalvikMax, long dalvikAllocated, long dalvikFree) {
        PrintWriter printWriter = pw;
        MemoryInfo memoryInfo = memInfo;
        long j = nativeMax;
        long j2 = nativeAllocated;
        long j3 = nativeFree;
        long j4 = dalvikMax;
        long j5 = dalvikAllocated;
        long j6 = dalvikFree;
        int i;
        if (checkin) {
            printWriter.print(4);
            printWriter.print(',');
            printWriter.print(pid);
            printWriter.print(',');
            printWriter.print(processName);
            printWriter.print(',');
            printWriter.print(j);
            printWriter.print(',');
            printWriter.print(j4);
            printWriter.print(',');
            printWriter.print("N/A,");
            printWriter.print(j + j4);
            printWriter.print(',');
            printWriter.print(j2);
            printWriter.print(',');
            printWriter.print(j5);
            printWriter.print(',');
            printWriter.print("N/A,");
            printWriter.print(j2 + j5);
            printWriter.print(',');
            printWriter.print(j3);
            printWriter.print(',');
            printWriter.print(j6);
            printWriter.print(',');
            printWriter.print("N/A,");
            printWriter.print(j3 + j6);
            printWriter.print(',');
            printWriter.print(memoryInfo.nativePss);
            printWriter.print(',');
            printWriter.print(memoryInfo.dalvikPss);
            printWriter.print(',');
            printWriter.print(memoryInfo.otherPss);
            printWriter.print(',');
            printWriter.print(memInfo.getTotalPss());
            printWriter.print(',');
            printWriter.print(memoryInfo.nativeSwappablePss);
            printWriter.print(',');
            printWriter.print(memoryInfo.dalvikSwappablePss);
            printWriter.print(',');
            printWriter.print(memoryInfo.otherSwappablePss);
            printWriter.print(',');
            printWriter.print(memInfo.getTotalSwappablePss());
            printWriter.print(',');
            printWriter.print(memoryInfo.nativeSharedDirty);
            printWriter.print(',');
            printWriter.print(memoryInfo.dalvikSharedDirty);
            printWriter.print(',');
            printWriter.print(memoryInfo.otherSharedDirty);
            printWriter.print(',');
            printWriter.print(memInfo.getTotalSharedDirty());
            printWriter.print(',');
            printWriter.print(memoryInfo.nativeSharedClean);
            printWriter.print(',');
            printWriter.print(memoryInfo.dalvikSharedClean);
            printWriter.print(',');
            printWriter.print(memoryInfo.otherSharedClean);
            printWriter.print(',');
            printWriter.print(memInfo.getTotalSharedClean());
            printWriter.print(',');
            printWriter.print(memoryInfo.nativePrivateDirty);
            printWriter.print(',');
            printWriter.print(memoryInfo.dalvikPrivateDirty);
            printWriter.print(',');
            printWriter.print(memoryInfo.otherPrivateDirty);
            printWriter.print(',');
            printWriter.print(memInfo.getTotalPrivateDirty());
            printWriter.print(',');
            printWriter.print(memoryInfo.nativePrivateClean);
            printWriter.print(',');
            printWriter.print(memoryInfo.dalvikPrivateClean);
            printWriter.print(',');
            printWriter.print(memoryInfo.otherPrivateClean);
            printWriter.print(',');
            printWriter.print(memInfo.getTotalPrivateClean());
            printWriter.print(',');
            printWriter.print(memoryInfo.nativeSwappedOut);
            printWriter.print(',');
            printWriter.print(memoryInfo.dalvikSwappedOut);
            printWriter.print(',');
            printWriter.print(memoryInfo.otherSwappedOut);
            printWriter.print(',');
            printWriter.print(memInfo.getTotalSwappedOut());
            printWriter.print(',');
            if (memoryInfo.hasSwappedOutPss) {
                printWriter.print(memoryInfo.nativeSwappedOutPss);
                printWriter.print(',');
                printWriter.print(memoryInfo.dalvikSwappedOutPss);
                printWriter.print(',');
                printWriter.print(memoryInfo.otherSwappedOutPss);
                printWriter.print(',');
                printWriter.print(memInfo.getTotalSwappedOutPss());
                printWriter.print(',');
            } else {
                printWriter.print("N/A,");
                printWriter.print("N/A,");
                printWriter.print("N/A,");
                printWriter.print("N/A,");
            }
            for (i = 0; i < 17; i++) {
                printWriter.print(MemoryInfo.getOtherLabel(i));
                printWriter.print(',');
                printWriter.print(memoryInfo.getOtherPss(i));
                printWriter.print(',');
                printWriter.print(memoryInfo.getOtherSwappablePss(i));
                printWriter.print(',');
                printWriter.print(memoryInfo.getOtherSharedDirty(i));
                printWriter.print(',');
                printWriter.print(memoryInfo.getOtherSharedClean(i));
                printWriter.print(',');
                printWriter.print(memoryInfo.getOtherPrivateDirty(i));
                printWriter.print(',');
                printWriter.print(memoryInfo.getOtherPrivateClean(i));
                printWriter.print(',');
                printWriter.print(memoryInfo.getOtherSwappedOut(i));
                printWriter.print(',');
                if (memoryInfo.hasSwappedOutPss) {
                    printWriter.print(memoryInfo.getOtherSwappedOutPss(i));
                    printWriter.print(',');
                } else {
                    printWriter.print("N/A,");
                }
            }
            return;
        }
        Object obj;
        i = "------";
        String str = "";
        if (dumpSummaryOnly) {
            obj = i;
        } else {
            int i2;
            int mySwappablePss;
            int mySharedDirty;
            int myPrivateDirty;
            int mySharedClean;
            int myPrivateClean;
            Object[] objArr;
            int totalSwappedOutPss;
            String str2 = HEAP_FULL_COLUMN;
            String str3 = HEAP_COLUMN;
            Object[] objArr2;
            if (dumpFullInfo) {
                objArr2 = new Object[11];
                objArr2[0] = str;
                objArr2[1] = "Pss";
                objArr2[2] = "Pss";
                objArr2[3] = "Shared";
                objArr2[4] = "Private";
                objArr2[5] = "Shared";
                objArr2[6] = "Private";
                objArr2[7] = memoryInfo.hasSwappedOutPss ? "SwapPss" : "Swap";
                objArr2[8] = "Heap";
                objArr2[9] = "Heap";
                objArr2[10] = "Heap";
                printRow(printWriter, str2, objArr2);
                printRow(printWriter, str2, str, "Total", "Clean", "Dirty", "Dirty", "Clean", "Clean", "Dirty", SizeAnimation.INNER_TAG_NAME, "Alloc", "Free");
                printRow(printWriter, str2, str, i, i, i, i, i, i, i, i, i, i);
                objArr2 = new Object[11];
                objArr2[0] = "Native Heap";
                objArr2[1] = Integer.valueOf(memoryInfo.nativePss);
                objArr2[2] = Integer.valueOf(memoryInfo.nativeSwappablePss);
                objArr2[3] = Integer.valueOf(memoryInfo.nativeSharedDirty);
                objArr2[4] = Integer.valueOf(memoryInfo.nativePrivateDirty);
                objArr2[5] = Integer.valueOf(memoryInfo.nativeSharedClean);
                objArr2[6] = Integer.valueOf(memoryInfo.nativePrivateClean);
                objArr2[7] = Integer.valueOf(memoryInfo.hasSwappedOutPss ? memoryInfo.nativeSwappedOutPss : memoryInfo.nativeSwappedOut);
                objArr2[8] = Long.valueOf(nativeMax);
                objArr2[9] = Long.valueOf(nativeAllocated);
                objArr2[10] = Long.valueOf(nativeFree);
                printRow(printWriter, str2, objArr2);
                objArr2 = new Object[11];
                objArr2[0] = "Dalvik Heap";
                objArr2[1] = Integer.valueOf(memoryInfo.dalvikPss);
                objArr2[2] = Integer.valueOf(memoryInfo.dalvikSwappablePss);
                objArr2[3] = Integer.valueOf(memoryInfo.dalvikSharedDirty);
                objArr2[4] = Integer.valueOf(memoryInfo.dalvikPrivateDirty);
                objArr2[5] = Integer.valueOf(memoryInfo.dalvikSharedClean);
                objArr2[6] = Integer.valueOf(memoryInfo.dalvikPrivateClean);
                objArr2[7] = Integer.valueOf(memoryInfo.hasSwappedOutPss ? memoryInfo.dalvikSwappedOutPss : memoryInfo.dalvikSwappedOut);
                objArr2[8] = Long.valueOf(dalvikMax);
                objArr2[9] = Long.valueOf(dalvikAllocated);
                objArr2[10] = Long.valueOf(dalvikFree);
                printRow(printWriter, str2, objArr2);
            } else {
                objArr2 = new Object[8];
                objArr2[0] = str;
                objArr2[1] = "Pss";
                objArr2[2] = "Private";
                objArr2[3] = "Private";
                objArr2[4] = memoryInfo.hasSwappedOutPss ? "SwapPss" : "Swap";
                objArr2[5] = "Heap";
                objArr2[6] = "Heap";
                objArr2[7] = "Heap";
                printRow(printWriter, str3, objArr2);
                printRow(printWriter, str3, str, "Total", "Dirty", "Clean", "Dirty", SizeAnimation.INNER_TAG_NAME, "Alloc", "Free");
                printRow(printWriter, str3, str, i, i, i, i, i, i, i, i);
                Object[] objArr3 = new Object[8];
                objArr3[0] = "Native Heap";
                objArr3[1] = Integer.valueOf(memoryInfo.nativePss);
                objArr3[2] = Integer.valueOf(memoryInfo.nativePrivateDirty);
                objArr3[3] = Integer.valueOf(memoryInfo.nativePrivateClean);
                if (memoryInfo.hasSwappedOutPss) {
                    i2 = memoryInfo.nativeSwappedOutPss;
                } else {
                    i2 = memoryInfo.nativeSwappedOut;
                }
                objArr3[4] = Integer.valueOf(i2);
                objArr3[5] = Long.valueOf(nativeMax);
                objArr3[6] = Long.valueOf(nativeAllocated);
                objArr3[7] = Long.valueOf(nativeFree);
                printRow(printWriter, str3, objArr3);
                objArr3 = new Object[8];
                objArr3[0] = "Dalvik Heap";
                objArr3[1] = Integer.valueOf(memoryInfo.dalvikPss);
                objArr3[2] = Integer.valueOf(memoryInfo.dalvikPrivateDirty);
                objArr3[3] = Integer.valueOf(memoryInfo.dalvikPrivateClean);
                if (memoryInfo.hasSwappedOutPss) {
                    i2 = memoryInfo.dalvikSwappedOutPss;
                } else {
                    i2 = memoryInfo.dalvikSwappedOut;
                }
                objArr3[4] = Integer.valueOf(i2);
                objArr3[5] = Long.valueOf(dalvikMax);
                objArr3[6] = Long.valueOf(dalvikAllocated);
                objArr3[7] = Long.valueOf(dalvikFree);
                printRow(printWriter, str3, objArr3);
            }
            i2 = memoryInfo.otherPss;
            int otherSwappablePss = memoryInfo.otherSwappablePss;
            int otherSharedDirty = memoryInfo.otherSharedDirty;
            int otherPrivateDirty = memoryInfo.otherPrivateDirty;
            int otherPss = i2;
            int otherSharedClean = memoryInfo.otherSharedClean;
            int otherPrivateClean = memoryInfo.otherPrivateClean;
            int otherSwappedOut = memoryInfo.otherSwappedOut;
            int otherSwappedOutPss = memoryInfo.otherSwappedOutPss;
            i2 = 0;
            while (true) {
                obj = i;
                if (i2 >= 17) {
                    break;
                }
                i = memoryInfo.getOtherPss(i2);
                mySwappablePss = memoryInfo.getOtherSwappablePss(i2);
                mySharedDirty = memoryInfo.getOtherSharedDirty(i2);
                myPrivateDirty = memoryInfo.getOtherPrivateDirty(i2);
                mySharedClean = memoryInfo.getOtherSharedClean(i2);
                myPrivateClean = memoryInfo.getOtherPrivateClean(i2);
                int mySwappedOut = memoryInfo.getOtherSwappedOut(i2);
                int mySwappedOutPss = memoryInfo.getOtherSwappedOutPss(i2);
                if (i == 0 && mySharedDirty == 0 && myPrivateDirty == 0 && mySharedClean == 0 && myPrivateClean == 0) {
                    if ((memoryInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut) == 0) {
                        i2++;
                        j2 = nativeAllocated;
                        i = obj;
                    }
                }
                if (dumpFullInfo) {
                    objArr = new Object[11];
                    objArr[0] = MemoryInfo.getOtherLabel(i2);
                    objArr[1] = Integer.valueOf(i);
                    objArr[2] = Integer.valueOf(mySwappablePss);
                    objArr[3] = Integer.valueOf(mySharedDirty);
                    objArr[4] = Integer.valueOf(myPrivateDirty);
                    objArr[5] = Integer.valueOf(mySharedClean);
                    objArr[6] = Integer.valueOf(myPrivateClean);
                    objArr[7] = Integer.valueOf(memoryInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut);
                    objArr[8] = str;
                    objArr[9] = str;
                    objArr[10] = str;
                    printRow(printWriter, str2, objArr);
                } else {
                    objArr = new Object[8];
                    objArr[0] = MemoryInfo.getOtherLabel(i2);
                    objArr[1] = Integer.valueOf(i);
                    objArr[2] = Integer.valueOf(myPrivateDirty);
                    objArr[3] = Integer.valueOf(myPrivateClean);
                    objArr[4] = Integer.valueOf(memoryInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut);
                    objArr[5] = str;
                    objArr[6] = str;
                    objArr[7] = str;
                    printRow(printWriter, str3, objArr);
                }
                otherPss -= i;
                otherSwappablePss -= mySwappablePss;
                otherSharedDirty -= mySharedDirty;
                otherPrivateDirty -= myPrivateDirty;
                otherSharedClean -= mySharedClean;
                otherPrivateClean -= myPrivateClean;
                otherSwappedOut -= mySwappedOut;
                otherSwappedOutPss -= mySwappedOutPss;
                i2++;
                j2 = nativeAllocated;
                i = obj;
            }
            if (dumpFullInfo) {
                Object[] objArr4 = new Object[11];
                objArr4[0] = "Unknown";
                objArr4[1] = Integer.valueOf(otherPss);
                objArr4[2] = Integer.valueOf(otherSwappablePss);
                objArr4[3] = Integer.valueOf(otherSharedDirty);
                objArr4[4] = Integer.valueOf(otherPrivateDirty);
                objArr4[5] = Integer.valueOf(otherSharedClean);
                objArr4[6] = Integer.valueOf(otherPrivateClean);
                objArr4[7] = Integer.valueOf(memoryInfo.hasSwappedOutPss ? otherSwappedOutPss : otherSwappedOut);
                objArr4[8] = str;
                objArr4[9] = str;
                objArr4[10] = str;
                printRow(printWriter, str2, objArr4);
                objArr4 = new Object[11];
                objArr4[0] = "TOTAL";
                objArr4[1] = Integer.valueOf(memInfo.getTotalPss());
                objArr4[2] = Integer.valueOf(memInfo.getTotalSwappablePss());
                objArr4[3] = Integer.valueOf(memInfo.getTotalSharedDirty());
                objArr4[4] = Integer.valueOf(memInfo.getTotalPrivateDirty());
                objArr4[5] = Integer.valueOf(memInfo.getTotalSharedClean());
                objArr4[6] = Integer.valueOf(memInfo.getTotalPrivateClean());
                if (memoryInfo.hasSwappedOutPss) {
                    totalSwappedOutPss = memInfo.getTotalSwappedOutPss();
                } else {
                    totalSwappedOutPss = memInfo.getTotalSwappedOut();
                }
                objArr4[7] = Integer.valueOf(totalSwappedOutPss);
                objArr4[8] = Long.valueOf(nativeMax + j4);
                objArr4[9] = Long.valueOf(nativeAllocated + j5);
                objArr4[10] = Long.valueOf(nativeFree + dalvikFree);
                printRow(printWriter, str2, objArr4);
            } else {
                objArr = new Object[8];
                objArr[0] = "Unknown";
                objArr[1] = Integer.valueOf(otherPss);
                objArr[2] = Integer.valueOf(otherPrivateDirty);
                objArr[3] = Integer.valueOf(otherPrivateClean);
                objArr[4] = Integer.valueOf(memoryInfo.hasSwappedOutPss ? otherSwappedOutPss : otherSwappedOut);
                objArr[5] = str;
                objArr[6] = str;
                objArr[7] = str;
                printRow(printWriter, str3, objArr);
                objArr = new Object[8];
                objArr[0] = "TOTAL";
                objArr[1] = Integer.valueOf(memInfo.getTotalPss());
                objArr[2] = Integer.valueOf(memInfo.getTotalPrivateDirty());
                objArr[3] = Integer.valueOf(memInfo.getTotalPrivateClean());
                if (memoryInfo.hasSwappedOutPss) {
                    i = memInfo.getTotalSwappedOutPss();
                } else {
                    i = memInfo.getTotalSwappedOut();
                }
                objArr[4] = Integer.valueOf(i);
                objArr[5] = Long.valueOf(nativeMax + j4);
                objArr[6] = Long.valueOf(nativeAllocated + j5);
                objArr[7] = Long.valueOf(nativeFree + dalvikFree);
                printRow(printWriter, str3, objArr);
            }
            if (dumpDalvik) {
                printWriter.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                printWriter.println(" Dalvik Details");
                i = 17;
                while (i < 31) {
                    int i3;
                    int i4;
                    totalSwappedOutPss = memoryInfo.getOtherPss(i);
                    i2 = memoryInfo.getOtherSwappablePss(i);
                    int mySharedDirty2 = memoryInfo.getOtherSharedDirty(i);
                    mySwappablePss = memoryInfo.getOtherPrivateDirty(i);
                    mySharedDirty = memoryInfo.getOtherSharedClean(i);
                    myPrivateDirty = memoryInfo.getOtherPrivateClean(i);
                    mySharedClean = memoryInfo.getOtherSwappedOut(i);
                    myPrivateClean = memoryInfo.getOtherSwappedOutPss(i);
                    if (totalSwappedOutPss == 0 && mySharedDirty2 == 0 && mySwappablePss == 0 && mySharedDirty == 0 && myPrivateDirty == 0) {
                        if ((memoryInfo.hasSwappedOutPss ? myPrivateClean : mySharedClean) == 0) {
                            i3 = 9;
                            i++;
                            i4 = i3;
                        }
                    }
                    Object[] objArr5;
                    if (dumpFullInfo) {
                        objArr5 = new Object[11];
                        objArr5[0] = MemoryInfo.getOtherLabel(i);
                        objArr5[1] = Integer.valueOf(totalSwappedOutPss);
                        objArr5[2] = Integer.valueOf(i2);
                        objArr5[3] = Integer.valueOf(mySharedDirty2);
                        objArr5[4] = Integer.valueOf(mySwappablePss);
                        objArr5[5] = Integer.valueOf(mySharedDirty);
                        objArr5[6] = Integer.valueOf(myPrivateDirty);
                        objArr5[7] = Integer.valueOf(memoryInfo.hasSwappedOutPss ? myPrivateClean : mySharedClean);
                        objArr5[8] = str;
                        i3 = 9;
                        objArr5[9] = str;
                        objArr5[10] = str;
                        printRow(printWriter, str2, objArr5);
                    } else {
                        i3 = 9;
                        objArr5 = new Object[8];
                        objArr5[0] = MemoryInfo.getOtherLabel(i);
                        objArr5[1] = Integer.valueOf(totalSwappedOutPss);
                        objArr5[2] = Integer.valueOf(mySwappablePss);
                        objArr5[3] = Integer.valueOf(myPrivateDirty);
                        objArr5[4] = Integer.valueOf(memoryInfo.hasSwappedOutPss ? myPrivateClean : mySharedClean);
                        objArr5[5] = str;
                        objArr5[6] = str;
                        objArr5[7] = str;
                        printRow(printWriter, str3, objArr5);
                    }
                    i++;
                    i4 = i3;
                }
            }
        }
        printWriter.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        printWriter.println(" App Summary");
        printRow(printWriter, ONE_COUNT_COLUMN_HEADER, str, "Pss(KB)");
        printRow(printWriter, ONE_COUNT_COLUMN_HEADER, str, obj);
        printRow(printWriter, ONE_COUNT_COLUMN, "Java Heap:", Integer.valueOf(memInfo.getSummaryJavaHeap()));
        printRow(printWriter, ONE_COUNT_COLUMN, "Native Heap:", Integer.valueOf(memInfo.getSummaryNativeHeap()));
        printRow(printWriter, ONE_COUNT_COLUMN, "Code:", Integer.valueOf(memInfo.getSummaryCode()));
        printRow(printWriter, ONE_COUNT_COLUMN, "Stack:", Integer.valueOf(memInfo.getSummaryStack()));
        printRow(printWriter, ONE_COUNT_COLUMN, "Graphics:", Integer.valueOf(memInfo.getSummaryGraphics()));
        printRow(printWriter, ONE_COUNT_COLUMN, "Private Other:", Integer.valueOf(memInfo.getSummaryPrivateOther()));
        printRow(printWriter, ONE_COUNT_COLUMN, "System:", Integer.valueOf(memInfo.getSummarySystem()));
        printWriter.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        if (memoryInfo.hasSwappedOutPss) {
            printRow(printWriter, TWO_COUNT_COLUMNS, "TOTAL:", Integer.valueOf(memInfo.getSummaryTotalPss()), "TOTAL SWAP PSS:", Integer.valueOf(memInfo.getSummaryTotalSwapPss()));
        } else {
            printRow(printWriter, TWO_COUNT_COLUMNS, "TOTAL:", Integer.valueOf(memInfo.getSummaryTotalPss()), "TOTAL SWAP (KB):", Integer.valueOf(memInfo.getSummaryTotalSwap()));
        }
    }

    private static void dumpMemoryInfo(ProtoOutputStream proto, long fieldId, String name, int pss, int cleanPss, int sharedDirty, int privateDirty, int sharedClean, int privateClean, boolean hasSwappedOutPss, int dirtySwap, int dirtySwapPss) {
        ProtoOutputStream protoOutputStream = proto;
        long token = proto.start(fieldId);
        proto.write(1138166333441L, name);
        proto.write(1120986464258L, pss);
        proto.write(1120986464259L, cleanPss);
        proto.write(1120986464260L, sharedDirty);
        proto.write(1120986464261L, privateDirty);
        proto.write(1120986464262L, sharedClean);
        proto.write(1120986464263L, privateClean);
        if (hasSwappedOutPss) {
            proto.write(1120986464265L, dirtySwapPss);
            int i = dirtySwap;
        } else {
            int i2 = dirtySwapPss;
            proto.write(1120986464264L, dirtySwap);
        }
        proto.end(token);
    }

    public static void dumpMemInfoTable(ProtoOutputStream proto, MemoryInfo memInfo, boolean dumpDalvik, boolean dumpSummaryOnly, long nativeMax, long nativeAllocated, long nativeFree, long dalvikMax, long dalvikAllocated, long dalvikFree) {
        ProtoOutputStream protoOutputStream;
        MemoryInfo memoryInfo;
        ProtoOutputStream protoOutputStream2 = proto;
        MemoryInfo memoryInfo2 = memInfo;
        long j = nativeMax;
        long j2 = nativeAllocated;
        long j3 = nativeFree;
        long j4 = dalvikMax;
        long j5 = dalvikAllocated;
        long j6 = dalvikFree;
        if (dumpSummaryOnly) {
            protoOutputStream = protoOutputStream2;
            memoryInfo = memoryInfo2;
        } else {
            int myPss;
            int mySwappablePss;
            int mySharedDirty;
            int myPrivateDirty;
            int mySharedClean;
            int myPrivateClean;
            int i;
            long dvToken;
            j6 = protoOutputStream2.start(1146756268035L);
            long nhToken = j6;
            long j7 = j5;
            dumpMemoryInfo(proto, 1146756268033L, "Native Heap", memoryInfo2.nativePss, memoryInfo2.nativeSwappablePss, memoryInfo2.nativeSharedDirty, memoryInfo2.nativePrivateDirty, memoryInfo2.nativeSharedClean, memoryInfo2.nativePrivateClean, memoryInfo2.hasSwappedOutPss, memoryInfo2.nativeSwappedOut, memoryInfo2.nativeSwappedOutPss);
            protoOutputStream = proto;
            protoOutputStream.write(1120986464258L, nativeMax);
            protoOutputStream.write(1120986464259L, nativeAllocated);
            protoOutputStream.write(1120986464260L, nativeFree);
            protoOutputStream.end(nhToken);
            j4 = protoOutputStream.start(1146756268036L);
            MemoryInfo memoryInfo3 = memInfo;
            int i2 = memoryInfo3.dalvikPss;
            int i3 = memoryInfo3.dalvikSwappablePss;
            int i4 = memoryInfo3.dalvikSharedDirty;
            int i5 = memoryInfo3.dalvikPrivateDirty;
            int i6 = i4;
            int i7 = memoryInfo3.dalvikSharedClean;
            int i8 = i7;
            long dvToken2 = j4;
            dumpMemoryInfo(proto, 1146756268033L, "Dalvik Heap", i2, i3, i6, i5, i8, memoryInfo3.dalvikPrivateClean, memoryInfo3.hasSwappedOutPss, memoryInfo3.dalvikSwappedOut, memoryInfo3.dalvikSwappedOutPss);
            protoOutputStream.write(1120986464258L, dalvikMax);
            protoOutputStream.write(1120986464259L, j7);
            protoOutputStream.write(1120986464260L, dalvikFree);
            protoOutputStream.end(dvToken2);
            memoryInfo3 = memInfo;
            int otherPss = memoryInfo3.otherPss;
            int otherSwappablePss = memoryInfo3.otherSwappablePss;
            int otherSharedDirty = memoryInfo3.otherSharedDirty;
            int otherPrivateDirty = memoryInfo3.otherPrivateDirty;
            i2 = memoryInfo3.otherSharedClean;
            i3 = memoryInfo3.otherPrivateClean;
            int otherSwappedOut = memoryInfo3.otherSwappedOut;
            int otherSwappedOutPss = memoryInfo3.otherSwappedOutPss;
            int otherSwappablePss2 = otherSwappablePss;
            i6 = otherSharedDirty;
            int otherPrivateDirty2 = otherPrivateDirty;
            i8 = i2;
            int otherPrivateClean = i3;
            i3 = 0;
            int otherPss2 = otherPss;
            while (i3 < 17) {
                myPss = memoryInfo3.getOtherPss(i3);
                mySwappablePss = memoryInfo3.getOtherSwappablePss(i3);
                mySharedDirty = memoryInfo3.getOtherSharedDirty(i3);
                myPrivateDirty = memoryInfo3.getOtherPrivateDirty(i3);
                mySharedClean = memoryInfo3.getOtherSharedClean(i3);
                myPrivateClean = memoryInfo3.getOtherPrivateClean(i3);
                int mySwappedOut = memoryInfo3.getOtherSwappedOut(i3);
                int mySwappedOutPss = memoryInfo3.getOtherSwappedOutPss(i3);
                if (myPss == 0 && mySharedDirty == 0 && myPrivateDirty == 0 && mySharedClean == 0 && myPrivateClean == 0) {
                    if ((memoryInfo3.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut) == 0) {
                        i = i3;
                        dvToken = dvToken2;
                        i3 = i + 1;
                        memoryInfo3 = memInfo;
                        j = dalvikMax;
                        j4 = dalvikFree;
                        dvToken2 = dvToken;
                    }
                }
                MemoryInfo memoryInfo4 = memoryInfo3;
                dvToken = dvToken2;
                i = i3;
                dumpMemoryInfo(proto, 2246267895813L, MemoryInfo.getOtherLabel(i3), myPss, mySwappablePss, mySharedDirty, myPrivateDirty, mySharedClean, myPrivateClean, memoryInfo3.hasSwappedOutPss, mySwappedOut, mySwappedOutPss);
                otherPss2 -= myPss;
                otherSwappablePss2 -= mySwappablePss;
                i6 -= mySharedDirty;
                otherPrivateDirty2 -= myPrivateDirty;
                i8 -= mySharedClean;
                otherPrivateClean -= myPrivateClean;
                otherSwappedOut -= mySwappedOut;
                otherSwappedOutPss -= mySwappedOutPss;
                i3 = i + 1;
                memoryInfo3 = memInfo;
                j = dalvikMax;
                j4 = dalvikFree;
                dvToken2 = dvToken;
            }
            i = i3;
            dvToken = dvToken2;
            MemoryInfo memoryInfo5 = memInfo;
            memoryInfo = memoryInfo5;
            dumpMemoryInfo(proto, 1146756268038L, "Unknown", otherPss2, otherSwappablePss2, i6, otherPrivateDirty2, i8, otherPrivateClean, memoryInfo5.hasSwappedOutPss, otherSwappedOut, otherSwappedOutPss);
            long tToken = protoOutputStream.start(1146756268039L);
            dumpMemoryInfo(proto, 1146756268033L, "TOTAL", memInfo.getTotalPss(), memInfo.getTotalSwappablePss(), memInfo.getTotalSharedDirty(), memInfo.getTotalPrivateDirty(), memInfo.getTotalSharedClean(), memInfo.getTotalPrivateClean(), memoryInfo.hasSwappedOutPss, memInfo.getTotalSwappedOut(), memInfo.getTotalSwappedOutPss());
            protoOutputStream.write(1120986464258L, nativeMax + dalvikMax);
            protoOutputStream.write(1120986464259L, nativeAllocated + dalvikAllocated);
            protoOutputStream.write(1120986464260L, nativeFree + dalvikFree);
            j4 = tToken;
            protoOutputStream.end(j4);
            if (dumpDalvik) {
                int i9 = 17;
                while (i9 < 31) {
                    long tToken2;
                    int myPss2 = memoryInfo.getOtherPss(i9);
                    i = memoryInfo.getOtherSwappablePss(i9);
                    myPss = memoryInfo.getOtherSharedDirty(i9);
                    mySwappablePss = memoryInfo.getOtherPrivateDirty(i9);
                    mySharedDirty = memoryInfo.getOtherSharedClean(i9);
                    myPrivateDirty = memoryInfo.getOtherPrivateClean(i9);
                    mySharedClean = memoryInfo.getOtherSwappedOut(i9);
                    myPrivateClean = memoryInfo.getOtherSwappedOutPss(i9);
                    if (myPss2 == 0 && myPss == 0 && mySwappablePss == 0 && mySharedDirty == 0 && myPrivateDirty == 0) {
                        if ((memoryInfo.hasSwappedOutPss ? myPrivateClean : mySharedClean) == 0) {
                            tToken2 = j4;
                            i9++;
                            j4 = tToken2;
                        }
                    }
                    tToken2 = j4;
                    dumpMemoryInfo(proto, 2246267895816L, MemoryInfo.getOtherLabel(i9), myPss2, i, myPss, mySwappablePss, mySharedDirty, myPrivateDirty, memoryInfo.hasSwappedOutPss, mySharedClean, myPrivateClean);
                    i9++;
                    j4 = tToken2;
                }
            }
        }
        long asToken = protoOutputStream.start(1146756268041L);
        protoOutputStream.write(1120986464257L, memInfo.getSummaryJavaHeap());
        protoOutputStream.write(1120986464258L, memInfo.getSummaryNativeHeap());
        protoOutputStream.write(1120986464259L, memInfo.getSummaryCode());
        protoOutputStream.write(1120986464260L, memInfo.getSummaryStack());
        protoOutputStream.write(1120986464261L, memInfo.getSummaryGraphics());
        protoOutputStream.write(1120986464262L, memInfo.getSummaryPrivateOther());
        protoOutputStream.write(1120986464263L, memInfo.getSummarySystem());
        if (memoryInfo.hasSwappedOutPss) {
            protoOutputStream.write(1120986464264L, memInfo.getSummaryTotalSwapPss());
        } else {
            protoOutputStream.write(1120986464264L, memInfo.getSummaryTotalSwap());
        }
        protoOutputStream.end(asToken);
    }

    @UnsupportedAppUsage
    public void registerOnActivityPausedListener(Activity activity, OnActivityPausedListener listener) {
        synchronized (this.mOnPauseListeners) {
            ArrayList<OnActivityPausedListener> list = (ArrayList) this.mOnPauseListeners.get(activity);
            if (list == null) {
                list = new ArrayList();
                this.mOnPauseListeners.put(activity, list);
            }
            list.add(listener);
        }
    }

    @UnsupportedAppUsage
    public void unregisterOnActivityPausedListener(Activity activity, OnActivityPausedListener listener) {
        synchronized (this.mOnPauseListeners) {
            ArrayList<OnActivityPausedListener> list = (ArrayList) this.mOnPauseListeners.get(activity);
            if (list != null) {
                list.remove(listener);
            }
        }
    }

    public final ActivityInfo resolveActivityInfo(Intent intent) {
        ActivityInfo aInfo = intent.resolveActivityInfo(this.mInitialApplication.getPackageManager(), 1024);
        if (aInfo == null) {
            Instrumentation.checkStartActivityResult(-92, intent);
        }
        return aInfo;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final Activity startActivityNow(Activity parent, String id, Intent intent, ActivityInfo activityInfo, IBinder token, Bundle state, NonConfigurationInstances lastNonConfigurationInstances, IBinder assistToken) {
        ActivityClientRecord r = new ActivityClientRecord();
        r.token = token;
        r.assistToken = assistToken;
        r.ident = 0;
        r.intent = intent;
        r.state = state;
        r.parent = parent;
        r.embeddedID = id;
        r.activityInfo = activityInfo;
        r.lastNonConfigurationInstances = lastNonConfigurationInstances;
        return performLaunchActivity(r, null);
    }

    @UnsupportedAppUsage
    public final Activity getActivity(IBinder token) {
        ActivityClientRecord activityRecord = (ActivityClientRecord) this.mActivities.get(token);
        return activityRecord != null ? activityRecord.activity : null;
    }

    public ActivityClientRecord getActivityClient(IBinder token) {
        return (ActivityClientRecord) this.mActivities.get(token);
    }

    public void updatePendingConfiguration(Configuration config) {
        synchronized (this.mResourcesManager) {
            if (this.mPendingConfiguration == null || this.mPendingConfiguration.isOtherSeqNewer(config)) {
                this.mPendingConfiguration = config;
            }
        }
    }

    /* JADX WARNING: Missing block: B:14:0x002c, code skipped:
            return;
     */
    public void updateProcessState(int r6, boolean r7) {
        /*
        r5 = this;
        r0 = r5.mAppThread;
        monitor-enter(r0);
        r1 = r5.mLastProcessState;	 Catch:{ all -> 0x002d }
        if (r1 != r6) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x002d }
        return;
    L_0x0009:
        r5.mLastProcessState = r6;	 Catch:{ all -> 0x002d }
        r1 = 2;
        if (r6 != r1) goto L_0x0025;
    L_0x000e:
        r1 = r5.mNumLaunchingActivities;	 Catch:{ all -> 0x002d }
        r1 = r1.get();	 Catch:{ all -> 0x002d }
        if (r1 <= 0) goto L_0x0025;
    L_0x0016:
        r5.mPendingProcessState = r6;	 Catch:{ all -> 0x002d }
        r1 = r5.mH;	 Catch:{ all -> 0x002d }
        r2 = new android.app.-$$Lambda$ActivityThread$A4ykhsPb8qV3ffTqpQDklHSMDJ0;	 Catch:{ all -> 0x002d }
        r2.<init>(r5);	 Catch:{ all -> 0x002d }
        r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r1.postDelayed(r2, r3);	 Catch:{ all -> 0x002d }
        goto L_0x002b;
    L_0x0025:
        r1 = -1;
        r5.mPendingProcessState = r1;	 Catch:{ all -> 0x002d }
        r5.updateVmProcessState(r6);	 Catch:{ all -> 0x002d }
    L_0x002b:
        monitor-exit(r0);	 Catch:{ all -> 0x002d }
        return;
    L_0x002d:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x002d }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.updateProcessState(int, boolean):void");
    }

    private void updateVmProcessState(int processState) {
        int state;
        if (processState <= 7) {
            state = 0;
        } else {
            state = 1;
        }
        VMRuntime.getRuntime().updateProcessState(state);
    }

    /* JADX WARNING: Missing block: B:11:0x0016, code skipped:
            return;
     */
    private void applyPendingProcessState() {
        /*
        r3 = this;
        r0 = r3.mAppThread;
        monitor-enter(r0);
        r1 = r3.mPendingProcessState;	 Catch:{ all -> 0x0017 }
        r2 = -1;
        if (r1 != r2) goto L_0x000a;
    L_0x0008:
        monitor-exit(r0);	 Catch:{ all -> 0x0017 }
        return;
    L_0x000a:
        r1 = r3.mPendingProcessState;	 Catch:{ all -> 0x0017 }
        r3.mPendingProcessState = r2;	 Catch:{ all -> 0x0017 }
        r2 = r3.mLastProcessState;	 Catch:{ all -> 0x0017 }
        if (r1 != r2) goto L_0x0015;
    L_0x0012:
        r3.updateVmProcessState(r1);	 Catch:{ all -> 0x0017 }
    L_0x0015:
        monitor-exit(r0);	 Catch:{ all -> 0x0017 }
        return;
    L_0x0017:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0017 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.applyPendingProcessState():void");
    }

    public void countLaunchingActivities(int num) {
        this.mNumLaunchingActivities.getAndAdd(num);
    }

    @UnsupportedAppUsage
    public final void sendActivityResult(IBinder token, String id, int requestCode, int resultCode, Intent data) {
        ArrayList<ResultInfo> list = new ArrayList();
        list.add(new ResultInfo(id, requestCode, resultCode, data));
        ClientTransaction clientTransaction = ClientTransaction.obtain(this.mAppThread, token);
        clientTransaction.addCallback(ActivityResultItem.obtain(list));
        try {
            this.mAppThread.scheduleTransaction(clientTransaction);
        } catch (RemoteException e) {
        }
    }

    /* Access modifiers changed, original: 0000 */
    public TransactionExecutor getTransactionExecutor() {
        return this.mTransactionExecutor;
    }

    /* Access modifiers changed, original: 0000 */
    public void sendMessage(int what, Object obj) {
        sendMessage(what, obj, 0, 0, false);
    }

    private void sendMessage(int what, Object obj, int arg1) {
        sendMessage(what, obj, arg1, 0, false);
    }

    private void sendMessage(int what, Object obj, int arg1, int arg2) {
        sendMessage(what, obj, arg1, arg2, false);
    }

    private void sendMessage(int what, Object obj, int arg1, int arg2, boolean async) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        if (async) {
            msg.setAsynchronous(true);
        }
        this.mH.sendMessage(msg);
    }

    private void sendMessage(int what, Object obj, int arg1, int arg2, int seq) {
        Message msg = Message.obtain();
        msg.what = what;
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = obj;
        args.argi1 = arg1;
        args.argi2 = arg2;
        args.argi3 = seq;
        msg.obj = args;
        this.mH.sendMessage(msg);
    }

    /* Access modifiers changed, original: final */
    public final void scheduleContextCleanup(ContextImpl context, String who, String what) {
        ContextCleanupInfo cci = new ContextCleanupInfo();
        cci.context = context;
        cci.who = who;
        cci.what = what;
        sendMessage(119, cci);
    }

    /* JADX WARNING: Removed duplicated region for block: B:65:0x0146  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0133  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x015b A:{Catch:{ SuperNotCalledException -> 0x01ae, Exception -> 0x01a9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0172 A:{Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }} */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0166  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0183 A:{Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }} */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x017f A:{Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }} */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x024c  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x024c  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x024c  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x0238 A:{Splitter:B:20:0x0083, ExcHandler: Exception (e java.lang.Exception)} */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x024c  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x024c  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x024c  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x024c  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x024c  */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:122:0x0238, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:123:0x0239, code skipped:
            r1 = r6;
            r25 = r8;
            r28 = r9;
            r24 = r12;
            r2 = r13;
            r3 = r14;
            r4 = r15;
     */
    /* JADX WARNING: Missing block: B:129:0x0271, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:130:0x0272, code skipped:
            r1 = r6;
            r25 = r8;
            r7 = r9;
            r24 = r12;
            r2 = r13;
            r3 = r14;
            r4 = r15;
     */
    private android.app.Activity performLaunchActivity(android.app.ActivityThread.ActivityClientRecord r30, android.content.Intent r31) {
        /*
        r29 = this;
        r15 = r29;
        r14 = r30;
        r13 = r31;
        r12 = r14.activityInfo;
        r0 = r14.packageInfo;
        r11 = 1;
        if (r0 != 0) goto L_0x0017;
    L_0x000d:
        r0 = r12.applicationInfo;
        r1 = r14.compatInfo;
        r0 = r15.getPackageInfo(r0, r1, r11);
        r14.packageInfo = r0;
    L_0x0017:
        r0 = r14.intent;
        r0 = r0.getComponent();
        if (r0 != 0) goto L_0x0030;
    L_0x001f:
        r1 = r14.intent;
        r2 = r15.mInitialApplication;
        r2 = r2.getPackageManager();
        r0 = r1.resolveActivity(r2);
        r1 = r14.intent;
        r1.setComponent(r0);
    L_0x0030:
        r1 = r14.activityInfo;
        r1 = r1.targetActivity;
        if (r1 == 0) goto L_0x0046;
    L_0x0036:
        r1 = new android.content.ComponentName;
        r2 = r14.activityInfo;
        r2 = r2.packageName;
        r3 = r14.activityInfo;
        r3 = r3.targetActivity;
        r1.<init>(r2, r3);
        r0 = r1;
        r9 = r0;
        goto L_0x0047;
    L_0x0046:
        r9 = r0;
    L_0x0047:
        r8 = r29.createBaseContextForActivity(r30);
        r1 = 0;
        r0 = r8.getClassLoader();	 Catch:{ Exception -> 0x0079 }
        r2 = r15.mInstrumentation;	 Catch:{ Exception -> 0x0079 }
        r3 = r9.getClassName();	 Catch:{ Exception -> 0x0079 }
        r4 = r14.intent;	 Catch:{ Exception -> 0x0079 }
        r2 = r2.newActivity(r0, r3, r4);	 Catch:{ Exception -> 0x0079 }
        r1 = r2;
        r2 = r1.getClass();	 Catch:{ Exception -> 0x0079 }
        android.os.StrictMode.incrementExpectedActivityCount(r2);	 Catch:{ Exception -> 0x0079 }
        r2 = r14.intent;	 Catch:{ Exception -> 0x0079 }
        r2.setExtrasClassLoader(r0);	 Catch:{ Exception -> 0x0079 }
        r2 = r14.intent;	 Catch:{ Exception -> 0x0079 }
        r2.prepareToEnterProcess();	 Catch:{ Exception -> 0x0079 }
        r2 = r14.state;	 Catch:{ Exception -> 0x0079 }
        if (r2 == 0) goto L_0x0077;
    L_0x0072:
        r2 = r14.state;	 Catch:{ Exception -> 0x0079 }
        r2.setClassLoader(r0);	 Catch:{ Exception -> 0x0079 }
    L_0x0077:
        r6 = r1;
        goto L_0x0083;
    L_0x0079:
        r0 = move-exception;
        r2 = r15.mInstrumentation;
        r2 = r2.onException(r1, r0);
        if (r2 == 0) goto L_0x027c;
    L_0x0082:
        r6 = r1;
    L_0x0083:
        r0 = r14.packageInfo;	 Catch:{ SuperNotCalledException -> 0x0271, Exception -> 0x0238 }
        r1 = r15.mInstrumentation;	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r5 = 0;
        r7 = r0.makeApplication(r5, r1);	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        if (r6 == 0) goto L_0x020a;
    L_0x008e:
        r0 = r14.activityInfo;	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r1 = r8.getPackageManager();	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r10 = r0.loadLabel(r1);	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r0 = new android.content.res.Configuration;	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r1 = r15.mCompatConfiguration;	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r0.<init>(r1);	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r1 = r14.overrideConfig;	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        if (r1 == 0) goto L_0x00c2;
    L_0x00a3:
        r1 = r14.overrideConfig;	 Catch:{ SuperNotCalledException -> 0x00b6, Exception -> 0x00a9 }
        r0.updateFrom(r1);	 Catch:{ SuperNotCalledException -> 0x00b6, Exception -> 0x00a9 }
        goto L_0x00c2;
    L_0x00a9:
        r0 = move-exception;
        r1 = r6;
        r25 = r8;
        r28 = r9;
        r24 = r12;
        r2 = r13;
        r3 = r14;
        r4 = r15;
        goto L_0x0243;
    L_0x00b6:
        r0 = move-exception;
        r1 = r6;
        r25 = r8;
        r7 = r9;
        r24 = r12;
        r2 = r13;
        r3 = r14;
        r4 = r15;
        goto L_0x027b;
    L_0x00c2:
        r1 = 0;
        r2 = r14.mPendingRemoveWindow;	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r4 = 0;
        if (r2 == 0) goto L_0x00d6;
    L_0x00c8:
        r2 = r14.mPreserveWindow;	 Catch:{ SuperNotCalledException -> 0x00b6, Exception -> 0x00a9 }
        if (r2 == 0) goto L_0x00d6;
    L_0x00cc:
        r2 = r14.mPendingRemoveWindow;	 Catch:{ SuperNotCalledException -> 0x00b6, Exception -> 0x00a9 }
        r1 = r2;
        r14.mPendingRemoveWindow = r4;	 Catch:{ SuperNotCalledException -> 0x00b6, Exception -> 0x00a9 }
        r14.mPendingRemoveWindowManager = r4;	 Catch:{ SuperNotCalledException -> 0x00b6, Exception -> 0x00a9 }
        r20 = r1;
        goto L_0x00d8;
    L_0x00d6:
        r20 = r1;
    L_0x00d8:
        r8.setOuterContext(r6);	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r16 = r29.getInstrumentation();	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r3 = r14.token;	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r2 = r14.ident;	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r1 = r14.intent;	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r11 = r14.activityInfo;	 Catch:{ SuperNotCalledException -> 0x022d, Exception -> 0x0238 }
        r18 = r12;
        r12 = r14.parent;	 Catch:{ SuperNotCalledException -> 0x01fe, Exception -> 0x01f1 }
        r13 = r14.embeddedID;	 Catch:{ SuperNotCalledException -> 0x01ea, Exception -> 0x01e1 }
        r15 = r14.lastNonConfigurationInstances;	 Catch:{ SuperNotCalledException -> 0x01d7, Exception -> 0x01cb }
        r21 = r7;
        r7 = r14.referrer;	 Catch:{ SuperNotCalledException -> 0x01d7, Exception -> 0x01cb }
        r22 = r10;
        r10 = r14.voiceInteractor;	 Catch:{ SuperNotCalledException -> 0x01d7, Exception -> 0x01cb }
        r19 = r10;
        r10 = r14.configCallback;	 Catch:{ SuperNotCalledException -> 0x01d7, Exception -> 0x01cb }
        r23 = r10;
        r10 = r14.assistToken;	 Catch:{ SuperNotCalledException -> 0x01d7, Exception -> 0x01cb }
        r24 = r1;
        r1 = r6;
        r25 = r2;
        r2 = r8;
        r26 = r3;
        r3 = r29;
        r4 = r16;
        r5 = r26;
        r27 = r6;
        r6 = r25;
        r25 = r8;
        r8 = r24;
        r28 = r9;
        r9 = r11;
        r11 = r12;
        r24 = r18;
        r12 = r13;
        r13 = r15;
        r15 = r14;
        r14 = r0;
        r15 = r7;
        r16 = r19;
        r17 = r20;
        r18 = r23;
        r19 = r10;
        r7 = r21;
        r10 = r22;
        r1.attach(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19);	 Catch:{ SuperNotCalledException -> 0x01be, Exception -> 0x01b3 }
        r2 = r31;
        if (r2 == 0) goto L_0x0146;
    L_0x0133:
        r1 = r27;
        r1.mIntent = r2;	 Catch:{ SuperNotCalledException -> 0x013f, Exception -> 0x0138 }
        goto L_0x0148;
    L_0x0138:
        r0 = move-exception;
        r4 = r29;
        r3 = r30;
        goto L_0x0243;
    L_0x013f:
        r0 = move-exception;
        r4 = r29;
        r3 = r30;
        goto L_0x022a;
    L_0x0146:
        r1 = r27;
    L_0x0148:
        r3 = r30;
        r4 = 0;
        r3.lastNonConfigurationInstances = r4;	 Catch:{ SuperNotCalledException -> 0x01ae, Exception -> 0x01a9 }
        r29.checkAndBlockForNetworkAccess();	 Catch:{ SuperNotCalledException -> 0x01ae, Exception -> 0x01a9 }
        r4 = 0;
        r1.mStartedActivity = r4;	 Catch:{ SuperNotCalledException -> 0x01ae, Exception -> 0x01a9 }
        r5 = r3.activityInfo;	 Catch:{ SuperNotCalledException -> 0x01ae, Exception -> 0x01a9 }
        r5 = r5.getThemeResource();	 Catch:{ SuperNotCalledException -> 0x01ae, Exception -> 0x01a9 }
        if (r5 == 0) goto L_0x015e;
    L_0x015b:
        r1.setTheme(r5);	 Catch:{ SuperNotCalledException -> 0x01ae, Exception -> 0x01a9 }
    L_0x015e:
        r1.mCalled = r4;	 Catch:{ SuperNotCalledException -> 0x01ae, Exception -> 0x01a9 }
        r4 = r30.isPersistable();	 Catch:{ SuperNotCalledException -> 0x01ae, Exception -> 0x01a9 }
        if (r4 == 0) goto L_0x0172;
    L_0x0166:
        r4 = r29;
        r6 = r4.mInstrumentation;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r8 = r3.state;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r9 = r3.persistentState;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r6.callActivityOnCreate(r1, r8, r9);	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        goto L_0x017b;
    L_0x0172:
        r4 = r29;
        r6 = r4.mInstrumentation;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r8 = r3.state;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r6.callActivityOnCreate(r1, r8);	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
    L_0x017b:
        r6 = r1.mCalled;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        if (r6 == 0) goto L_0x0183;
    L_0x017f:
        r3.activity = r1;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        goto L_0x0214;
    L_0x0183:
        r6 = new android.util.SuperNotCalledException;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r8 = new java.lang.StringBuilder;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r8.<init>();	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r9 = "Activity ";
        r8.append(r9);	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r9 = r3.intent;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r9 = r9.getComponent();	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r9 = r9.toShortString();	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r8.append(r9);	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r9 = " did not call through to super.onCreate()";
        r8.append(r9);	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r8 = r8.toString();	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r6.<init>(r8);	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        throw r6;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
    L_0x01a9:
        r0 = move-exception;
        r4 = r29;
        goto L_0x0243;
    L_0x01ae:
        r0 = move-exception;
        r4 = r29;
        goto L_0x022a;
    L_0x01b3:
        r0 = move-exception;
        r4 = r29;
        r3 = r30;
        r2 = r31;
        r1 = r27;
        goto L_0x0243;
    L_0x01be:
        r0 = move-exception;
        r4 = r29;
        r3 = r30;
        r2 = r31;
        r1 = r27;
        r7 = r28;
        goto L_0x027b;
    L_0x01cb:
        r0 = move-exception;
        r4 = r29;
        r2 = r31;
        r1 = r6;
        r25 = r8;
        r28 = r9;
        r3 = r14;
        goto L_0x01fa;
    L_0x01d7:
        r0 = move-exception;
        r4 = r29;
        r2 = r31;
        r1 = r6;
        r25 = r8;
        r3 = r14;
        goto L_0x0205;
    L_0x01e1:
        r0 = move-exception;
        r2 = r31;
        r1 = r6;
        r25 = r8;
        r28 = r9;
        goto L_0x01f8;
    L_0x01ea:
        r0 = move-exception;
        r2 = r31;
        r1 = r6;
        r25 = r8;
        goto L_0x0203;
    L_0x01f1:
        r0 = move-exception;
        r1 = r6;
        r25 = r8;
        r28 = r9;
        r2 = r13;
    L_0x01f8:
        r3 = r14;
        r4 = r15;
    L_0x01fa:
        r24 = r18;
        goto L_0x0243;
    L_0x01fe:
        r0 = move-exception;
        r1 = r6;
        r25 = r8;
        r2 = r13;
    L_0x0203:
        r3 = r14;
        r4 = r15;
    L_0x0205:
        r24 = r18;
        r7 = r9;
        goto L_0x027b;
    L_0x020a:
        r1 = r6;
        r25 = r8;
        r28 = r9;
        r24 = r12;
        r2 = r13;
        r3 = r14;
        r4 = r15;
    L_0x0214:
        r5 = 1;
        r3.setState(r5);	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r5 = r4.mResourcesManager;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        monitor-enter(r5);	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
        r0 = r4.mActivities;	 Catch:{ all -> 0x0224 }
        r6 = r3.token;	 Catch:{ all -> 0x0224 }
        r0.put(r6, r3);	 Catch:{ all -> 0x0224 }
        monitor-exit(r5);	 Catch:{ all -> 0x0224 }
        goto L_0x024b;
    L_0x0224:
        r0 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0224 }
        throw r0;	 Catch:{ SuperNotCalledException -> 0x0229, Exception -> 0x0227 }
    L_0x0227:
        r0 = move-exception;
        goto L_0x0243;
    L_0x0229:
        r0 = move-exception;
    L_0x022a:
        r7 = r28;
        goto L_0x027b;
    L_0x022d:
        r0 = move-exception;
        r1 = r6;
        r25 = r8;
        r24 = r12;
        r2 = r13;
        r3 = r14;
        r4 = r15;
        r7 = r9;
        goto L_0x027b;
    L_0x0238:
        r0 = move-exception;
        r1 = r6;
        r25 = r8;
        r28 = r9;
        r24 = r12;
        r2 = r13;
        r3 = r14;
        r4 = r15;
    L_0x0243:
        r5 = r4.mInstrumentation;
        r5 = r5.onException(r1, r0);
        if (r5 == 0) goto L_0x024c;
    L_0x024b:
        return r1;
    L_0x024c:
        r5 = new java.lang.RuntimeException;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Unable to start activity ";
        r6.append(r7);
        r7 = r28;
        r6.append(r7);
        r8 = ": ";
        r6.append(r8);
        r8 = r0.toString();
        r6.append(r8);
        r6 = r6.toString();
        r5.<init>(r6, r0);
        throw r5;
    L_0x0271:
        r0 = move-exception;
        r1 = r6;
        r25 = r8;
        r7 = r9;
        r24 = r12;
        r2 = r13;
        r3 = r14;
        r4 = r15;
    L_0x027b:
        throw r0;
    L_0x027c:
        r25 = r8;
        r7 = r9;
        r5 = new java.lang.RuntimeException;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r8 = "Unable to instantiate activity ";
        r6.append(r8);
        r6.append(r7);
        r8 = ": ";
        r6.append(r8);
        r8 = r0.toString();
        r6.append(r8);
        r6 = r6.toString();
        r5.<init>(r6, r0);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.performLaunchActivity(android.app.ActivityThread$ActivityClientRecord, android.content.Intent):android.app.Activity");
    }

    public void handleStartActivity(ActivityClientRecord r, PendingTransactionActions pendingActions) {
        Activity activity = r.activity;
        if (r.activity != null) {
            if (!r.stopped) {
                throw new IllegalStateException("Can't start activity that is not stopped.");
            } else if (!r.activity.mFinished) {
                activity.performStart("handleStartActivity");
                r.setState(2);
                if (pendingActions != null) {
                    if (pendingActions.shouldRestoreInstanceState()) {
                        if (r.isPersistable()) {
                            if (!(r.state == null && r.persistentState == null)) {
                                this.mInstrumentation.callActivityOnRestoreInstanceState(activity, r.state, r.persistentState);
                            }
                        } else if (r.state != null) {
                            this.mInstrumentation.callActivityOnRestoreInstanceState(activity, r.state);
                        }
                    }
                    if (pendingActions.shouldCallOnPostCreate()) {
                        activity.mCalled = false;
                        if (r.isPersistable()) {
                            this.mInstrumentation.callActivityOnPostCreate(activity, r.state, r.persistentState);
                        } else {
                            this.mInstrumentation.callActivityOnPostCreate(activity, r.state);
                        }
                        if (!activity.mCalled) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Activity ");
                            stringBuilder.append(r.intent.getComponent().toShortString());
                            stringBuilder.append(" did not call through to super.onPostCreate()");
                            throw new SuperNotCalledException(stringBuilder.toString());
                        }
                    }
                }
            }
        }
    }

    private void checkAndBlockForNetworkAccess() {
        synchronized (this.mNetworkPolicyLock) {
            if (this.mNetworkBlockSeq != -1) {
                try {
                    ActivityManager.getService().waitForNetworkStateUpdate(this.mNetworkBlockSeq);
                    this.mNetworkBlockSeq = -1;
                } catch (RemoteException e) {
                }
            }
        }
    }

    private ContextImpl createBaseContextForActivity(ActivityClientRecord r) {
        try {
            ContextImpl appContext = ContextImpl.createActivityContext(this, r.packageInfo, r.activityInfo, r.token, ActivityTaskManager.getService().getActivityDisplayId(r.token), r.overrideConfig);
            DisplayManagerGlobal dm = DisplayManagerGlobal.getInstance();
            String pkgName = SystemProperties.get("debug.second-display.pkg");
            if (pkgName == null || pkgName.isEmpty() || !r.packageInfo.mPackageName.contains(pkgName)) {
                return appContext;
            }
            for (int id : dm.getDisplayIds()) {
                if (id != 0) {
                    return (ContextImpl) appContext.createDisplayContext(dm.getCompatibleDisplay(id, appContext.getResources()));
                }
            }
            return appContext;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Activity handleLaunchActivity(ActivityClientRecord r, PendingTransactionActions pendingActions, Intent customIntent) {
        unscheduleGcIdler();
        this.mSomeActivitiesChanged = true;
        if (r.profilerInfo != null) {
            this.mProfiler.setProfiler(r.profilerInfo);
            this.mProfiler.startProfiling();
        }
        handleConfigurationChanged(null, null);
        if (!(ThreadedRenderer.sRendererDisabled || (r.activityInfo.flags & 512) == 0)) {
            HardwareRenderer.preload();
        }
        WindowManagerGlobal.initialize();
        GraphicsEnvironment.hintActivityLaunch();
        Activity a = performLaunchActivity(r, customIntent);
        if (a != null) {
            r.createdConfig = new Configuration(this.mConfiguration);
            reportSizeConfigurations(r);
            if (!(r.activity.mFinished || pendingActions == null)) {
                pendingActions.setOldState(r.state);
                pendingActions.setRestoreInstanceState(true);
                pendingActions.setCallOnPostCreate(true);
            }
        } else {
            try {
                ActivityTaskManager.getService().finishActivity(r.token, 0, null, 0);
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
        return a;
    }

    private void reportSizeConfigurations(ActivityClientRecord r) {
        if (!this.mActivitiesToBeDestroyed.containsKey(r.token)) {
            Configuration[] configurations = r.activity.getResources().getSizeConfigurations();
            if (configurations != null) {
                SparseIntArray horizontal = new SparseIntArray();
                SparseIntArray vertical = new SparseIntArray();
                SparseIntArray smallest = new SparseIntArray();
                for (int i = configurations.length - 1; i >= 0; i--) {
                    Configuration config = configurations[i];
                    if (config.screenHeightDp != 0) {
                        vertical.put(config.screenHeightDp, 0);
                    }
                    if (config.screenWidthDp != 0) {
                        horizontal.put(config.screenWidthDp, 0);
                    }
                    if (config.smallestScreenWidthDp != 0) {
                        smallest.put(config.smallestScreenWidthDp, 0);
                    }
                }
                try {
                    ActivityTaskManager.getService().reportSizeConfigurations(r.token, horizontal.copyKeys(), vertical.copyKeys(), smallest.copyKeys());
                } catch (RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            }
        }
    }

    private void deliverNewIntents(ActivityClientRecord r, List<ReferrerIntent> intents) {
        int N = intents.size();
        for (int i = 0; i < N; i++) {
            ReferrerIntent intent = (ReferrerIntent) intents.get(i);
            intent.setExtrasClassLoader(r.activity.getClassLoader());
            intent.prepareToEnterProcess();
            r.activity.mFragments.noteStateNotSaved();
            this.mInstrumentation.callActivityOnNewIntent(r.activity, intent);
        }
    }

    public void handleNewIntent(IBinder token, List<ReferrerIntent> intents) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            checkAndBlockForNetworkAccess();
            deliverNewIntents(r, intents);
        }
    }

    public void handleRequestAssistContextExtras(RequestAssistContextExtras cmd) {
        Uri referrer;
        AssistStructure structure;
        RemoteException e;
        RequestAssistContextExtras requestAssistContextExtras = cmd;
        boolean notSecure = false;
        boolean forAutofill = requestAssistContextExtras.requestType == 2;
        if (this.mLastSessionId != requestAssistContextExtras.sessionId) {
            this.mLastSessionId = requestAssistContextExtras.sessionId;
            for (int i = this.mLastAssistStructures.size() - 1; i >= 0; i--) {
                AssistStructure structure2 = (AssistStructure) ((WeakReference) this.mLastAssistStructures.get(i)).get();
                if (structure2 != null) {
                    structure2.clearSendChannel();
                }
                this.mLastAssistStructures.remove(i);
            }
        }
        Bundle data = new Bundle();
        AssistStructure structure3 = null;
        AssistContent content = forAutofill ? null : new AssistContent();
        long startTime = SystemClock.uptimeMillis();
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(requestAssistContextExtras.activityToken);
        Uri referrer2 = null;
        if (r != null) {
            if (!forAutofill) {
                r.activity.getApplication().dispatchOnProvideAssistData(r.activity, data);
                r.activity.onProvideAssistData(data);
                referrer2 = r.activity.onProvideReferrer();
            }
            if (requestAssistContextExtras.requestType == 1 || forAutofill) {
                structure3 = new AssistStructure(r.activity, forAutofill, requestAssistContextExtras.flags);
                Intent activityIntent = r.activity.getIntent();
                if (r.window == null || (r.window.getAttributes().flags & 8192) == 0) {
                    notSecure = true;
                }
                if (activityIntent == null || !notSecure) {
                    if (!forAutofill) {
                        content.setDefaultIntent(new Intent());
                    }
                } else if (!forAutofill) {
                    Intent intent = new Intent(activityIntent);
                    intent.setFlags(intent.getFlags() & -67);
                    intent.removeUnsafeExtras();
                    content.setDefaultIntent(intent);
                }
                if (!forAutofill) {
                    r.activity.onProvideAssistContent(content);
                }
                referrer = referrer2;
            } else {
                referrer = referrer2;
            }
        } else {
            referrer = null;
        }
        if (structure3 == null) {
            structure = new AssistStructure();
        } else {
            structure = structure3;
        }
        structure.setAcquisitionStartTime(startTime);
        structure.setAcquisitionEndTime(SystemClock.uptimeMillis());
        this.mLastAssistStructures.add(new WeakReference(structure));
        try {
            try {
                ActivityTaskManager.getService().reportAssistContextExtras(requestAssistContextExtras.requestToken, data, structure, content, referrer);
            } catch (RemoteException e2) {
                e = e2;
            }
        } catch (RemoteException e3) {
            e = e3;
            ActivityClientRecord activityClientRecord = r;
            throw e.rethrowFromSystemServer();
        }
    }

    private void handleRequestDirectActions(IBinder activityToken, IVoiceInteractor interactor, CancellationSignal cancellationSignal, RemoteCallback callback) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(activityToken);
        String str = TAG;
        if (r == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("requestDirectActions(): no activity for ");
            stringBuilder.append(activityToken);
            Log.w(str, stringBuilder.toString());
            callback.sendResult(null);
            return;
        }
        int lifecycleState = r.getLifecycleState();
        if (lifecycleState < 2 || lifecycleState >= 5) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("requestDirectActions(");
            stringBuilder2.append(r);
            stringBuilder2.append("): wrong lifecycle: ");
            stringBuilder2.append(lifecycleState);
            Log.w(str, stringBuilder2.toString());
            callback.sendResult(null);
            return;
        }
        if (r.activity.mVoiceInteractor == null || r.activity.mVoiceInteractor.mInteractor.asBinder() != interactor.asBinder()) {
            if (r.activity.mVoiceInteractor != null) {
                r.activity.mVoiceInteractor.destroy();
            }
            r.activity.mVoiceInteractor = new VoiceInteractor(interactor, r.activity, r.activity, Looper.myLooper());
        }
        r.activity.onGetDirectActions(cancellationSignal, new -$$Lambda$ActivityThread$FmvGY8exyv0L0oqZrnunpl8OFI8(r, callback));
    }

    static /* synthetic */ void lambda$handleRequestDirectActions$0(ActivityClientRecord r, RemoteCallback callback, List actions) {
        Preconditions.checkNotNull(actions);
        Preconditions.checkCollectionElementsNotNull(actions, Slice.HINT_ACTIONS);
        if (actions.isEmpty()) {
            callback.sendResult(null);
            return;
        }
        int actionCount = actions.size();
        for (int i = 0; i < actionCount; i++) {
            ((DirectAction) actions.get(i)).setSource(r.activity.getTaskId(), r.activity.getAssistToken());
        }
        Bundle result = new Bundle();
        result.putParcelable(DirectAction.KEY_ACTIONS_LIST, new ParceledListSlice(actions));
        callback.sendResult(result);
    }

    private void handlePerformDirectAction(IBinder activityToken, String actionId, Bundle arguments, CancellationSignal cancellationSignal, RemoteCallback resultCallback) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(activityToken);
        if (r != null) {
            int lifecycleState = r.getLifecycleState();
            if (lifecycleState < 2 || lifecycleState >= 5) {
                resultCallback.sendResult(null);
                return;
            }
            Bundle nonNullArguments = arguments != null ? arguments : Bundle.EMPTY;
            Activity activity = r.activity;
            Objects.requireNonNull(resultCallback);
            activity.onPerformDirectAction(actionId, nonNullArguments, cancellationSignal, new -$$Lambda$ZsFzoG2loyqNOR2cNbo-thrNK5c(resultCallback));
        } else {
            resultCallback.sendResult(null);
        }
    }

    public void handleTranslucentConversionComplete(IBinder token, boolean drawComplete) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            r.activity.onTranslucentConversionComplete(drawComplete);
        }
    }

    public void onNewActivityOptions(IBinder token, ActivityOptions options) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            r.activity.onNewActivityOptions(options);
        }
    }

    public void handleInstallProvider(ProviderInfo info) {
        ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            installContentProviders(this.mInitialApplication, Arrays.asList(new ProviderInfo[]{info}));
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleEnterAnimationComplete(IBinder token) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            r.activity.dispatchEnterAnimationComplete();
        }
    }

    private void handleStartBinderTracking() {
        Binder.enableTracing();
    }

    private void handleStopBinderTrackingAndDump(ParcelFileDescriptor fd) {
        try {
            Binder.disableTracing();
            Binder.getTransactionTracker().writeTracesToFile(fd);
        } finally {
            IoUtils.closeQuietly(fd);
            Binder.getTransactionTracker().clearTraces();
        }
    }

    public void handleMultiWindowModeChanged(IBinder token, boolean isInMultiWindowMode, Configuration overrideConfig) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            Configuration newConfig = new Configuration(this.mConfiguration);
            if (overrideConfig != null) {
                newConfig.updateFrom(overrideConfig);
            }
            r.activity.dispatchMultiWindowModeChanged(isInMultiWindowMode, newConfig);
        }
    }

    public void handlePictureInPictureModeChanged(IBinder token, boolean isInPipMode, Configuration overrideConfig) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            Configuration newConfig = new Configuration(this.mConfiguration);
            if (overrideConfig != null) {
                newConfig.updateFrom(overrideConfig);
            }
            r.activity.dispatchPictureInPictureModeChanged(isInPipMode, newConfig);
        }
    }

    private void handleLocalVoiceInteractionStarted(IBinder token, IVoiceInteractor interactor) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            r.voiceInteractor = interactor;
            r.activity.setVoiceInteractor(interactor);
            if (interactor == null) {
                r.activity.onLocalVoiceInteractionStopped();
            } else {
                r.activity.onLocalVoiceInteractionStarted();
            }
        }
    }

    private static boolean attemptAttachAgent(String agent, ClassLoader classLoader) {
        try {
            VMDebug.attachAgent(agent, classLoader);
            return true;
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Attaching agent with ");
            stringBuilder.append(classLoader);
            stringBuilder.append(" failed: ");
            stringBuilder.append(agent);
            Slog.e(TAG, stringBuilder.toString());
            return false;
        }
    }

    static void handleAttachAgent(String agent, LoadedApk loadedApk) {
        ClassLoader classLoader = loadedApk != null ? loadedApk.getClassLoader() : null;
        if (!(attemptAttachAgent(agent, classLoader) || classLoader == null)) {
            attemptAttachAgent(agent, null);
        }
    }

    public static Intent getIntentBeingBroadcast() {
        return (Intent) sCurrentBroadcastIntent.get();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private void handleReceiver(ReceiverData data) {
        String str = ": ";
        unscheduleGcIdler();
        String component = data.intent.getComponent().getClassName();
        LoadedApk packageInfo = getPackageInfoNoCheck(data.info.applicationInfo, data.compatInfo);
        IActivityManager mgr = ActivityManager.getService();
        try {
            ContextImpl context = (ContextImpl) packageInfo.makeApplication(null, this.mInstrumentation).getBaseContext();
            if (data.info.splitName != null) {
                context = (ContextImpl) context.createContextForSplit(data.info.splitName);
            }
            ClassLoader cl = context.getClassLoader();
            data.intent.setExtrasClassLoader(cl);
            data.intent.prepareToEnterProcess();
            data.setExtrasClassLoader(cl);
            BroadcastReceiver receiver = packageInfo.getAppFactory().instantiateReceiver(cl, data.info.name, data.intent);
            try {
                sCurrentBroadcastIntent.set(data.intent);
                receiver.setPendingResult(data);
                receiver.onReceive(context.getReceiverRestrictedContext(), data.intent);
            } catch (Exception e) {
                data.sendFinished(mgr);
                if (!this.mInstrumentation.onException(receiver, e)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to start receiver ");
                    stringBuilder.append(component);
                    stringBuilder.append(str);
                    stringBuilder.append(e.toString());
                    throw new RuntimeException(stringBuilder.toString(), e);
                }
            } catch (Throwable th) {
                sCurrentBroadcastIntent.set(null);
            }
            sCurrentBroadcastIntent.set(null);
            if (receiver.getPendingResult() != null) {
                data.finish();
            }
        } catch (Exception e2) {
            data.sendFinished(mgr);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unable to instantiate receiver ");
            stringBuilder2.append(component);
            stringBuilder2.append(str);
            stringBuilder2.append(e2.toString());
            throw new RuntimeException(stringBuilder2.toString(), e2);
        }
    }

    private void handleCreateBackupAgent(CreateBackupAgentData data) {
        try {
            int i = getPackageManager().getPackageInfo(data.appInfo.packageName, 0, UserHandle.myUserId()).applicationInfo.uid;
            int myUid = Process.myUid();
            String str = TAG;
            if (i != myUid) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Asked to instantiate non-matching package ");
                stringBuilder.append(data.appInfo.packageName);
                Slog.w(str, stringBuilder.toString());
                return;
            }
            unscheduleGcIdler();
            LoadedApk packageInfo = getPackageInfoNoCheck(data.appInfo, data.compatInfo);
            String packageName = packageInfo.mPackageName;
            if (packageName == null) {
                Slog.d(str, "Asked to create backup agent for nonexistent package");
                return;
            }
            String classname = data.appInfo.backupAgentName;
            if (classname == null && (data.backupMode == 1 || data.backupMode == 3)) {
                classname = "android.app.backup.FullBackupAgent";
            }
            IBinder binder = null;
            try {
                ArrayMap<String, BackupAgent> backupAgents = getBackupAgentsForUser(data.userId);
                BackupAgent agent = (BackupAgent) backupAgents.get(packageName);
                if (agent != null) {
                    binder = agent.onBind();
                } else {
                    agent = (BackupAgent) packageInfo.getClassLoader().loadClass(classname).newInstance();
                    ContextImpl context = ContextImpl.createAppContext(this, packageInfo);
                    context.setOuterContext(agent);
                    agent.attach(context);
                    agent.onCreate(UserHandle.of(data.userId));
                    binder = agent.onBind();
                    backupAgents.put(packageName, agent);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Exception e2) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Agent threw during creation: ");
                stringBuilder2.append(e2);
                Slog.e(str, stringBuilder2.toString());
                if (data.backupMode != 2) {
                    if (data.backupMode != 3) {
                        throw e2;
                    }
                }
            } catch (Exception e3) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Unable to create BackupAgent ");
                stringBuilder3.append(classname);
                stringBuilder3.append(": ");
                stringBuilder3.append(e3.toString());
                throw new RuntimeException(stringBuilder3.toString(), e3);
            }
            ActivityManager.getService().backupAgentCreated(packageName, binder, data.userId);
        } catch (RemoteException e4) {
            throw e4.rethrowFromSystemServer();
        }
    }

    private void handleDestroyBackupAgent(CreateBackupAgentData data) {
        String packageName = getPackageInfoNoCheck(data.appInfo, data.compatInfo).mPackageName;
        ArrayMap<String, BackupAgent> backupAgents = getBackupAgentsForUser(data.userId);
        BackupAgent agent = (BackupAgent) backupAgents.get(packageName);
        String str = TAG;
        if (agent != null) {
            try {
                agent.onDestroy();
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Exception thrown in onDestroy by backup agent of ");
                stringBuilder.append(data.appInfo);
                Slog.w(str, stringBuilder.toString());
                e.printStackTrace();
            }
            backupAgents.remove(packageName);
            return;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Attempt to destroy unknown backup agent ");
        stringBuilder2.append(data);
        Slog.w(str, stringBuilder2.toString());
    }

    private ArrayMap<String, BackupAgent> getBackupAgentsForUser(int userId) {
        ArrayMap<String, BackupAgent> backupAgents = (ArrayMap) this.mBackupAgentsByUser.get(userId);
        if (backupAgents != null) {
            return backupAgents;
        }
        backupAgents = new ArrayMap();
        this.mBackupAgentsByUser.put(userId, backupAgents);
        return backupAgents;
    }

    @UnsupportedAppUsage
    private void handleCreateService(CreateServiceData data) {
        StringBuilder stringBuilder;
        String str = ": ";
        unscheduleGcIdler();
        LoadedApk packageInfo = getPackageInfoNoCheck(data.info.applicationInfo, data.compatInfo);
        Service service = null;
        try {
            service = packageInfo.getAppFactory().instantiateService(packageInfo.getClassLoader(), data.info.name, data.intent);
        } catch (Exception e) {
            if (!this.mInstrumentation.onException(null, e)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to instantiate service ");
                stringBuilder.append(data.info.name);
                stringBuilder.append(str);
                stringBuilder.append(e.toString());
                throw new RuntimeException(stringBuilder.toString(), e);
            }
        }
        try {
            Context context = ContextImpl.createAppContext(this, packageInfo);
            context.setOuterContext(service);
            Service service2 = service;
            Context context2 = context;
            service2.attach(context2, this, data.info.name, data.token, packageInfo.makeApplication(false, this.mInstrumentation), ActivityManager.getService());
            service.onCreate();
            this.mServices.put(data.token, service);
            ActivityManager.getService().serviceDoneExecuting(data.token, 0, 0, 0);
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        } catch (Exception e3) {
            if (!this.mInstrumentation.onException(service, e3)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to create service ");
                stringBuilder.append(data.info.name);
                stringBuilder.append(str);
                stringBuilder.append(e3.toString());
                throw new RuntimeException(stringBuilder.toString(), e3);
            }
        }
    }

    private void handleBindService(BindServiceData data) {
        Service s = (Service) this.mServices.get(data.token);
        if (s != null) {
            try {
                data.intent.setExtrasClassLoader(s.getClassLoader());
                data.intent.prepareToEnterProcess();
                if (data.rebind) {
                    s.onRebind(data.intent);
                    ActivityManager.getService().serviceDoneExecuting(data.token, 0, 0, 0);
                    return;
                }
                ActivityManager.getService().publishService(data.token, data.intent, s.onBind(data.intent));
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            } catch (Exception e) {
                if (!this.mInstrumentation.onException(s, e)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to bind to service ");
                    stringBuilder.append(s);
                    stringBuilder.append(" with ");
                    stringBuilder.append(data.intent);
                    stringBuilder.append(": ");
                    stringBuilder.append(e.toString());
                    throw new RuntimeException(stringBuilder.toString(), e);
                }
            }
        }
    }

    private void handleUnbindService(BindServiceData data) {
        Service s = (Service) this.mServices.get(data.token);
        if (s != null) {
            try {
                data.intent.setExtrasClassLoader(s.getClassLoader());
                data.intent.prepareToEnterProcess();
                boolean doRebind = s.onUnbind(data.intent);
                if (doRebind) {
                    ActivityManager.getService().unbindFinished(data.token, data.intent, doRebind);
                } else {
                    ActivityManager.getService().serviceDoneExecuting(data.token, 0, 0, 0);
                }
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            } catch (Exception e) {
                if (!this.mInstrumentation.onException(s, e)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to unbind to service ");
                    stringBuilder.append(s);
                    stringBuilder.append(" with ");
                    stringBuilder.append(data.intent);
                    stringBuilder.append(": ");
                    stringBuilder.append(e.toString());
                    throw new RuntimeException(stringBuilder.toString(), e);
                }
            }
        }
    }

    private void handleDumpService(DumpComponentInfo info) {
        ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            Service s = (Service) this.mServices.get(info.token);
            if (s != null) {
                PrintWriter pw = new FastPrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
                s.dump(info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        } catch (Throwable th) {
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleDumpActivity(DumpComponentInfo info) {
        ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(info.token);
            if (!(r == null || r.activity == null)) {
                PrintWriter pw = new FastPrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
                r.activity.dump(info.prefix, info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        } catch (Throwable th) {
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleDumpProvider(DumpComponentInfo info) {
        ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            ProviderClientRecord r = (ProviderClientRecord) this.mLocalProviders.get(info.token);
            if (!(r == null || r.mLocalProvider == null)) {
                PrintWriter pw = new FastPrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
                r.mLocalProvider.dump(info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        } catch (Throwable th) {
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleServiceArgs(ServiceArgsData data) {
        Service s = (Service) this.mServices.get(data.token);
        if (s != null) {
            try {
                int res;
                if (data.args != null) {
                    data.args.setExtrasClassLoader(s.getClassLoader());
                    data.args.prepareToEnterProcess();
                }
                if (data.taskRemoved) {
                    s.onTaskRemoved(data.args);
                    res = 1000;
                } else {
                    res = s.onStartCommand(data.args, data.flags, data.startId);
                }
                if (!ActivityThreadInjector.isPersistent(this.mBoundApplication)) {
                    QueuedWork.waitToFinish();
                }
                ActivityManager.getService().serviceDoneExecuting(data.token, 1, data.startId, res);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Exception e2) {
                if (!this.mInstrumentation.onException(s, e2)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to start service ");
                    stringBuilder.append(s);
                    stringBuilder.append(" with ");
                    stringBuilder.append(data.args);
                    stringBuilder.append(": ");
                    stringBuilder.append(e2.toString());
                    throw new RuntimeException(stringBuilder.toString(), e2);
                }
            }
        }
    }

    private void handleStopService(IBinder token) {
        Service s = (Service) this.mServices.remove(token);
        String str = TAG;
        if (s != null) {
            try {
                s.onDestroy();
                s.detachAndCleanUp();
                Context context = s.getBaseContext();
                if (context instanceof ContextImpl) {
                    ((ContextImpl) context).scheduleFinalCleanup(s.getClassName(), "Service");
                }
                if (!ActivityThreadInjector.isPersistent(this.mBoundApplication)) {
                    QueuedWork.waitToFinish();
                }
                ActivityManager.getService().serviceDoneExecuting(token, 2, 0, 0);
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Exception e2) {
                StringBuilder stringBuilder;
                if (this.mInstrumentation.onException(s, e2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("handleStopService: exception for ");
                    stringBuilder.append(token);
                    Slog.i(str, stringBuilder.toString(), e2);
                    return;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to stop service ");
                stringBuilder.append(s);
                stringBuilder.append(": ");
                stringBuilder.append(e2.toString());
                throw new RuntimeException(stringBuilder.toString(), e2);
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("handleStopService: token=");
        stringBuilder2.append(token);
        stringBuilder2.append(" not found.");
        Slog.i(str, stringBuilder2.toString());
    }

    @VisibleForTesting
    public ActivityClientRecord performResumeActivity(IBinder token, boolean finalStateRequest, String reason) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r == null || r.activity.mFinished) {
            return null;
        }
        if (r.getLifecycleState() == 3) {
            if (!finalStateRequest) {
                RuntimeException e = new IllegalStateException("Trying to resume activity which is already resumed");
                String message = e.getMessage();
                String str = TAG;
                Slog.e(str, message, e);
                Slog.e(str, r.getStateString());
            }
            return null;
        }
        if (finalStateRequest) {
            r.hideForNow = false;
            r.activity.mStartedActivity = false;
        }
        try {
            r.activity.onStateNotSaved();
            r.activity.mFragments.noteStateNotSaved();
            checkAndBlockForNetworkAccess();
            if (r.pendingIntents != null) {
                deliverNewIntents(r, r.pendingIntents);
                r.pendingIntents = null;
            }
            if (r.pendingResults != null) {
                deliverResults(r, r.pendingResults, reason);
                r.pendingResults = null;
            }
            r.activity.performResume(r.startsNotResumed, reason);
            r.state = null;
            r.persistentState = null;
            r.setState(3);
            reportTopResumedActivityChanged(r, r.isTopResumedActivity, "topWhenResuming");
        } catch (Exception e2) {
            if (!this.mInstrumentation.onException(r.activity, e2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to resume activity ");
                stringBuilder.append(r.intent.getComponent().toShortString());
                stringBuilder.append(": ");
                stringBuilder.append(e2.toString());
                throw new RuntimeException(stringBuilder.toString(), e2);
            }
        }
        return r;
    }

    static final void cleanUpPendingRemoveWindows(ActivityClientRecord r, boolean force) {
        if (!r.mPreserveWindow || force) {
            if (r.mPendingRemoveWindow != null) {
                r.mPendingRemoveWindowManager.removeViewImmediate(r.mPendingRemoveWindow.getDecorView());
                IBinder wtoken = r.mPendingRemoveWindow.getDecorView().getWindowToken();
                if (wtoken != null) {
                    WindowManagerGlobal.getInstance().closeAll(wtoken, r.activity.getClass().getName(), "Activity");
                }
            }
            r.mPendingRemoveWindow = null;
            r.mPendingRemoveWindowManager = null;
        }
    }

    public void handleResumeActivity(IBinder token, boolean finalStateRequest, boolean isForward, String reason) {
        unscheduleGcIdler();
        this.mSomeActivitiesChanged = true;
        ActivityClientRecord r = performResumeActivity(token, finalStateRequest, reason);
        if (r != null && !this.mActivitiesToBeDestroyed.containsKey(token)) {
            Activity a = r.activity;
            int forwardBit = isForward ? 256 : 0;
            boolean willBeVisible = a.mStartedActivity ^ true;
            if (!willBeVisible) {
                try {
                    willBeVisible = ActivityTaskManager.getService().willActivityBeVisible(a.getActivityToken());
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            if (r.window == null && !a.mFinished && willBeVisible) {
                r.window = r.activity.getWindow();
                View decor = r.window.getDecorView();
                decor.setVisibility(4);
                ViewManager wm = a.getWindowManager();
                LayoutParams l = r.window.getAttributes();
                a.mDecor = decor;
                l.type = 1;
                l.softInputMode |= forwardBit;
                if (r.mPreserveWindow) {
                    a.mWindowAdded = true;
                    r.mPreserveWindow = false;
                    ViewRootImpl impl = decor.getViewRootImpl();
                    if (impl != null) {
                        impl.notifyChildRebuilt();
                    }
                }
                if (a.mVisibleFromClient) {
                    if (a.mWindowAdded) {
                        a.onWindowAttributesChanged(l);
                    } else {
                        a.mWindowAdded = true;
                        wm.addView(decor, l);
                    }
                }
            } else if (!willBeVisible) {
                r.hideForNow = true;
            }
            cleanUpPendingRemoveWindows(r, false);
            if (!(r.activity.mFinished || !willBeVisible || r.activity.mDecor == null || r.hideForNow)) {
                if (r.newConfig != null) {
                    performConfigurationChangedForActivity(r, r.newConfig);
                    r.newConfig = null;
                }
                LayoutParams l2 = r.window.getAttributes();
                if ((256 & l2.softInputMode) != forwardBit) {
                    l2.softInputMode = (l2.softInputMode & TrafficStats.TAG_NETWORK_STACK_RANGE_END) | forwardBit;
                    if (r.activity.mVisibleFromClient) {
                        a.getWindowManager().updateViewLayout(r.window.getDecorView(), l2);
                    }
                }
                r.activity.mVisibleFromServer = true;
                this.mNumVisibleActivities++;
                if (r.activity.mVisibleFromClient) {
                    r.activity.makeVisible();
                }
            }
            r.nextIdle = this.mNewActivities;
            this.mNewActivities = r;
            Looper.myQueue().addIdleHandler(new Idler(this, null));
        }
    }

    public void handleTopResumedActivityChanged(IBinder token, boolean onTop, String reason) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r == null || r.activity == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Not found target activity to report position change for token: ");
            stringBuilder.append(token);
            Slog.w(TAG, stringBuilder.toString());
        } else if (r.isTopResumedActivity != onTop) {
            r.isTopResumedActivity = onTop;
            if (r.getLifecycleState() == 3) {
                reportTopResumedActivityChanged(r, onTop, "topStateChangedWhenResumed");
            }
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Activity top position already set to onTop=");
            stringBuilder2.append(onTop);
            throw new IllegalStateException(stringBuilder2.toString());
        }
    }

    private void reportTopResumedActivityChanged(ActivityClientRecord r, boolean onTop, String reason) {
        if (r.lastReportedTopResumedState != onTop) {
            r.lastReportedTopResumedState = onTop;
            r.activity.performTopResumedActivityChanged(onTop, reason);
        }
    }

    public void handlePauseActivity(IBinder token, boolean finished, boolean userLeaving, int configChanges, PendingTransactionActions pendingActions, String reason) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            if (userLeaving) {
                performUserLeavingActivity(r);
            }
            Activity activity = r.activity;
            activity.mConfigChangeFlags |= configChanges;
            performPauseActivity(r, finished, reason, pendingActions);
            if (r.isPreHoneycomb() && !ActivityThreadInjector.isPersistent(this.mBoundApplication)) {
                QueuedWork.waitToFinish();
            }
            this.mSomeActivitiesChanged = true;
        }
    }

    /* Access modifiers changed, original: final */
    public final void performUserLeavingActivity(ActivityClientRecord r) {
        this.mInstrumentation.callActivityOnUserLeaving(r.activity);
    }

    /* Access modifiers changed, original: final */
    public final Bundle performPauseActivity(IBinder token, boolean finished, String reason, PendingTransactionActions pendingActions) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        return r != null ? performPauseActivity(r, finished, reason, pendingActions) : null;
    }

    private Bundle performPauseActivity(ActivityClientRecord r, boolean finished, String reason, PendingTransactionActions pendingActions) {
        ArrayList<OnActivityPausedListener> listeners;
        Bundle bundle = null;
        if (r.paused) {
            if (r.activity.mFinished) {
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Performing pause of activity that is not resumed: ");
            stringBuilder.append(r.intent.getComponent().toShortString());
            RuntimeException e = new RuntimeException(stringBuilder.toString());
            Slog.e(TAG, e.getMessage(), e);
        }
        boolean shouldSaveState = true;
        if (finished) {
            r.activity.mFinished = true;
        }
        int i = 0;
        if (r.activity.mFinished || !r.isPreHoneycomb()) {
            shouldSaveState = false;
        }
        if (shouldSaveState) {
            callActivityOnSaveInstanceState(r);
        }
        performPauseActivityIfNeeded(r, reason);
        synchronized (this.mOnPauseListeners) {
            listeners = (ArrayList) this.mOnPauseListeners.remove(r.activity);
        }
        if (listeners != null) {
            i = listeners.size();
        }
        int size = i;
        for (i = 0; i < size; i++) {
            ((OnActivityPausedListener) listeners.get(i)).onPaused(r.activity);
        }
        Bundle oldState = pendingActions != null ? pendingActions.getOldState() : null;
        if (oldState != null && r.isPreHoneycomb()) {
            r.state = oldState;
        }
        if (shouldSaveState) {
            bundle = r.state;
        }
        return bundle;
    }

    private void performPauseActivityIfNeeded(ActivityClientRecord r, String reason) {
        if (!r.paused) {
            reportTopResumedActivityChanged(r, false, "pausing");
            try {
                r.activity.mCalled = false;
                this.mInstrumentation.callActivityOnPause(r.activity);
                if (r.activity.mCalled) {
                    r.setState(4);
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Activity ");
                stringBuilder.append(safeToComponentShortString(r.intent));
                stringBuilder.append(" did not call through to super.onPause()");
                throw new SuperNotCalledException(stringBuilder.toString());
            } catch (SuperNotCalledException e) {
                throw e;
            } catch (Exception e2) {
                if (!this.mInstrumentation.onException(r.activity, e2)) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Unable to pause activity ");
                    stringBuilder2.append(safeToComponentShortString(r.intent));
                    stringBuilder2.append(": ");
                    stringBuilder2.append(e2.toString());
                    throw new RuntimeException(stringBuilder2.toString(), e2);
                }
            }
        }
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final void performStopActivity(IBinder token, boolean saveState, String reason) {
        performStopActivityInner((ActivityClientRecord) this.mActivities.get(token), null, false, saveState, false, reason);
    }

    private void performStopActivityInner(ActivityClientRecord r, StopInfo info, boolean keepShown, boolean saveState, boolean finalStateRequest, String reason) {
        if (r != null) {
            if (!keepShown && r.stopped) {
                if (!r.activity.mFinished) {
                    if (!finalStateRequest) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Performing stop of activity that is already stopped: ");
                        stringBuilder.append(r.intent.getComponent().toShortString());
                        RuntimeException e = new RuntimeException(stringBuilder.toString());
                        String message = e.getMessage();
                        String str = TAG;
                        Slog.e(str, message, e);
                        Slog.e(str, r.getStateString());
                    }
                } else {
                    return;
                }
            }
            performPauseActivityIfNeeded(r, reason);
            if (info != null) {
                try {
                    info.setDescription(r.activity.onCreateDescription());
                } catch (Exception e2) {
                    if (!this.mInstrumentation.onException(r.activity, e2)) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Unable to save state of activity ");
                        stringBuilder2.append(r.intent.getComponent().toShortString());
                        stringBuilder2.append(": ");
                        stringBuilder2.append(e2.toString());
                        throw new RuntimeException(stringBuilder2.toString(), e2);
                    }
                }
            }
            if (!keepShown) {
                callActivityOnStop(r, saveState, reason);
            }
        }
    }

    private void callActivityOnStop(ActivityClientRecord r, boolean saveState, String reason) {
        boolean shouldSaveState = saveState && !r.activity.mFinished && r.state == null && !r.isPreHoneycomb();
        boolean isPreP = r.isPreP();
        if (shouldSaveState && isPreP) {
            callActivityOnSaveInstanceState(r);
        }
        try {
            r.activity.performStop(r.mPreserveWindow, reason);
        } catch (SuperNotCalledException e) {
            throw e;
        } catch (Exception e2) {
            if (!this.mInstrumentation.onException(r.activity, e2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to stop activity ");
                stringBuilder.append(r.intent.getComponent().toShortString());
                stringBuilder.append(": ");
                stringBuilder.append(e2.toString());
                throw new RuntimeException(stringBuilder.toString(), e2);
            }
        }
        r.setState(5);
        if (shouldSaveState && !isPreP) {
            callActivityOnSaveInstanceState(r);
        }
    }

    private void updateVisibility(ActivityClientRecord r, boolean show) {
        View v = r.activity.mDecor;
        if (v == null) {
            return;
        }
        if (show) {
            if (!r.activity.mVisibleFromServer) {
                r.activity.mVisibleFromServer = true;
                this.mNumVisibleActivities++;
                if (r.activity.mVisibleFromClient) {
                    r.activity.makeVisible();
                }
            }
            if (r.newConfig != null) {
                performConfigurationChangedForActivity(r, r.newConfig);
                r.newConfig = null;
            }
        } else if (r.activity.mVisibleFromServer) {
            r.activity.mVisibleFromServer = false;
            this.mNumVisibleActivities--;
            v.setVisibility(4);
        }
    }

    public void handleStopActivity(IBinder token, boolean show, int configChanges, PendingTransactionActions pendingActions, boolean finalStateRequest, String reason) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        Activity activity = r.activity;
        activity.mConfigChangeFlags |= configChanges;
        StopInfo stopInfo = new StopInfo();
        performStopActivityInner(r, stopInfo, show, true, finalStateRequest, reason);
        updateVisibility(r, show);
        if (!(r.isPreHoneycomb() || ActivityThreadInjector.isPersistent(this.mBoundApplication))) {
            QueuedWork.waitToFinish();
        }
        stopInfo.setActivity(r);
        stopInfo.setState(r.state);
        stopInfo.setPersistentState(r.persistentState);
        pendingActions.setStopInfo(stopInfo);
        this.mSomeActivitiesChanged = true;
    }

    public void reportStop(PendingTransactionActions pendingActions) {
        this.mH.post(pendingActions.getStopInfo());
    }

    public void performRestartActivity(IBinder token, boolean start) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r.stopped) {
            r.activity.performRestart(start, "performRestartActivity");
            if (start) {
                r.setState(2);
            }
        }
    }

    public void handleWindowVisibility(IBinder token, boolean show) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("handleWindowVisibility: no activity for token ");
            stringBuilder.append(token);
            Log.w(TAG, stringBuilder.toString());
            return;
        }
        if (!show && !r.stopped) {
            performStopActivityInner(r, null, show, false, false, "handleWindowVisibility");
        } else if (show && r.getLifecycleState() == 5) {
            unscheduleGcIdler();
            r.activity.performRestart(true, "handleWindowVisibility");
            r.setState(2);
        }
        if (r.activity.mDecor != null) {
            updateVisibility(r, show);
        }
        this.mSomeActivitiesChanged = true;
    }

    private void handleSleeping(IBinder token, boolean sleeping) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("handleSleeping: no activity for token ");
            stringBuilder.append(token);
            Log.w(TAG, stringBuilder.toString());
            return;
        }
        if (sleeping) {
            if (!(r.stopped || r.isPreHoneycomb())) {
                callActivityOnStop(r, true, "sleeping");
            }
            if (!(r.isPreHoneycomb() || ActivityThreadInjector.isPersistent(this.mBoundApplication))) {
                QueuedWork.waitToFinish();
            }
            try {
                ActivityTaskManager.getService().activitySlept(r.token);
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        } else if (r.stopped && r.activity.mVisibleFromServer) {
            r.activity.performRestart(true, "handleSleeping");
            r.setState(2);
        }
    }

    private void handleSetCoreSettings(Bundle coreSettings) {
        synchronized (this.mResourcesManager) {
            this.mCoreSettings = coreSettings;
        }
        onCoreSettingsChange();
    }

    private void onCoreSettingsChange() {
        if (updateDebugViewAttributeState()) {
            relaunchAllActivities(false);
        }
    }

    private boolean updateDebugViewAttributeState() {
        boolean previousState = View.sDebugViewAttributes;
        String str = "";
        View.sDebugViewAttributesApplicationPackage = this.mCoreSettings.getString(Global.DEBUG_VIEW_ATTRIBUTES_APPLICATION_PACKAGE, str);
        AppBindData appBindData = this.mBoundApplication;
        if (!(appBindData == null || appBindData.appInfo == null)) {
            str = this.mBoundApplication.appInfo.packageName;
        }
        boolean z = this.mCoreSettings.getInt(Global.DEBUG_VIEW_ATTRIBUTES, 0) != 0 || View.sDebugViewAttributesApplicationPackage.equals(str);
        View.sDebugViewAttributes = z;
        if (previousState != View.sDebugViewAttributes) {
            return true;
        }
        return false;
    }

    private void relaunchAllActivities(boolean preserveWindows) {
        for (Entry<IBinder, ActivityClientRecord> entry : this.mActivities.entrySet()) {
            ActivityClientRecord r = (ActivityClientRecord) entry.getValue();
            if (!r.activity.mFinished) {
                if (preserveWindows && r.window != null) {
                    r.mPreserveWindow = true;
                }
                scheduleRelaunchActivity((IBinder) entry.getKey());
            }
        }
    }

    private void handleUpdatePackageCompatibilityInfo(UpdateCompatibilityData data) {
        LoadedApk apk = peekPackageInfo(data.pkg, false);
        if (apk != null) {
            apk.setCompatibilityInfo(data.info);
        }
        apk = peekPackageInfo(data.pkg, true);
        if (apk != null) {
            apk.setCompatibilityInfo(data.info);
        }
        handleConfigurationChanged(this.mConfiguration, data.info);
        WindowManagerGlobal.getInstance().reportNewConfiguration(this.mConfiguration);
    }

    private void deliverResults(ActivityClientRecord r, List<ResultInfo> results, String reason) {
        int N = results.size();
        for (int i = 0; i < N; i++) {
            ResultInfo ri = (ResultInfo) results.get(i);
            try {
                if (ri.mData != null) {
                    ri.mData.setExtrasClassLoader(r.activity.getClassLoader());
                    ri.mData.prepareToEnterProcess();
                }
                r.activity.dispatchActivityResult(ri.mResultWho, ri.mRequestCode, ri.mResultCode, ri.mData, reason);
            } catch (Exception e) {
                if (!this.mInstrumentation.onException(r.activity, e)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failure delivering result ");
                    stringBuilder.append(ri);
                    stringBuilder.append(" to activity ");
                    stringBuilder.append(r.intent.getComponent().toShortString());
                    stringBuilder.append(": ");
                    stringBuilder.append(e.toString());
                    throw new RuntimeException(stringBuilder.toString(), e);
                }
            }
        }
    }

    public void handleSendResult(IBinder token, List<ResultInfo> results, String reason) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            boolean resumed = r.paused ^ true;
            if (!r.activity.mFinished && r.activity.mDecor != null && r.hideForNow && resumed) {
                updateVisibility(r, true);
            }
            if (resumed) {
                StringBuilder stringBuilder;
                try {
                    r.activity.mCalled = false;
                    this.mInstrumentation.callActivityOnPause(r.activity);
                    if (!r.activity.mCalled) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Activity ");
                        stringBuilder.append(r.intent.getComponent().toShortString());
                        stringBuilder.append(" did not call through to super.onPause()");
                        throw new SuperNotCalledException(stringBuilder.toString());
                    }
                } catch (SuperNotCalledException e) {
                    throw e;
                } catch (Exception e2) {
                    if (!this.mInstrumentation.onException(r.activity, e2)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unable to pause activity ");
                        stringBuilder.append(r.intent.getComponent().toShortString());
                        stringBuilder.append(": ");
                        stringBuilder.append(e2.toString());
                        throw new RuntimeException(stringBuilder.toString(), e2);
                    }
                }
            }
            checkAndBlockForNetworkAccess();
            deliverResults(r, results, reason);
            if (resumed) {
                r.activity.performResume(false, reason);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ActivityClientRecord performDestroyActivity(IBinder token, boolean finishing, int configChanges, boolean getNonConfigInstance, String reason) {
        StringBuilder stringBuilder;
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        Class<? extends Activity> activityClass = null;
        if (r != null) {
            activityClass = r.activity.getClass();
            Activity activity = r.activity;
            activity.mConfigChangeFlags |= configChanges;
            if (finishing) {
                r.activity.mFinished = true;
            }
            performPauseActivityIfNeeded(r, "destroy");
            if (!r.stopped) {
                callActivityOnStop(r, false, "destroy");
            }
            if (getNonConfigInstance) {
                try {
                    r.lastNonConfigurationInstances = r.activity.retainNonConfigurationInstances();
                } catch (Exception e) {
                    if (!this.mInstrumentation.onException(r.activity, e)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unable to retain activity ");
                        stringBuilder.append(r.intent.getComponent().toShortString());
                        stringBuilder.append(": ");
                        stringBuilder.append(e.toString());
                        throw new RuntimeException(stringBuilder.toString(), e);
                    }
                }
            }
            try {
                r.activity.mCalled = false;
                this.mInstrumentation.callActivityOnDestroy(r.activity);
                if (r.activity.mCalled) {
                    if (r.window != null) {
                        r.window.closeAllPanels();
                    }
                    r.setState(6);
                } else {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Activity ");
                    stringBuilder2.append(safeToComponentShortString(r.intent));
                    stringBuilder2.append(" did not call through to super.onDestroy()");
                    throw new SuperNotCalledException(stringBuilder2.toString());
                }
            } catch (SuperNotCalledException e2) {
                throw e2;
            } catch (Exception e3) {
                if (!this.mInstrumentation.onException(r.activity, e3)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to destroy activity ");
                    stringBuilder.append(safeToComponentShortString(r.intent));
                    stringBuilder.append(": ");
                    stringBuilder.append(e3.toString());
                    throw new RuntimeException(stringBuilder.toString(), e3);
                }
            }
        }
        schedulePurgeIdler();
        synchronized (this.mResourcesManager) {
            this.mActivities.remove(token);
        }
        StrictMode.decrementExpectedActivityCount(activityClass);
        return r;
    }

    private static String safeToComponentShortString(Intent intent) {
        ComponentName component = intent.getComponent();
        return component == null ? "[Unknown]" : component.toShortString();
    }

    public Map<IBinder, ClientTransactionItem> getActivitiesToBeDestroyed() {
        return this.mActivitiesToBeDestroyed;
    }

    public void handleDestroyActivity(IBinder token, boolean finishing, int configChanges, boolean getNonConfigInstance, String reason) {
        ActivityClientRecord r = performDestroyActivity(token, finishing, configChanges, getNonConfigInstance, reason);
        if (r != null) {
            cleanUpPendingRemoveWindows(r, finishing);
            WindowManager wm = r.activity.getWindowManager();
            View v = r.activity.mDecor;
            String str = "Activity";
            if (v != null) {
                if (r.activity.mVisibleFromServer) {
                    this.mNumVisibleActivities--;
                }
                IBinder wtoken = v.getWindowToken();
                if (r.activity.mWindowAdded) {
                    if (r.mPreserveWindow) {
                        r.mPendingRemoveWindow = r.window;
                        r.mPendingRemoveWindowManager = wm;
                        r.window.clearContentView();
                    } else {
                        wm.removeViewImmediate(v);
                    }
                }
                if (wtoken != null && r.mPendingRemoveWindow == null) {
                    WindowManagerGlobal.getInstance().closeAll(wtoken, r.activity.getClass().getName(), str);
                } else if (r.mPendingRemoveWindow != null) {
                    WindowManagerGlobal.getInstance().closeAllExceptView(token, v, r.activity.getClass().getName(), str);
                }
                r.activity.mDecor = null;
            }
            if (r.mPendingRemoveWindow == null) {
                WindowManagerGlobal.getInstance().closeAll(token, r.activity.getClass().getName(), str);
            }
            Context c = r.activity.getBaseContext();
            if (c instanceof ContextImpl) {
                ((ContextImpl) c).scheduleFinalCleanup(r.activity.getClass().getName(), str);
            }
        }
        if (finishing) {
            try {
                ActivityTaskManager.getService().activityDestroyed(token);
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
        this.mSomeActivitiesChanged = true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x003d  */
    public android.app.ActivityThread.ActivityClientRecord prepareRelaunchActivity(android.os.IBinder r7, java.util.List<android.app.ResultInfo> r8, java.util.List<com.android.internal.content.ReferrerIntent> r9, int r10, android.util.MergedConfiguration r11, boolean r12) {
        /*
        r6 = this;
        r0 = 0;
        r1 = 0;
        r2 = r6.mResourcesManager;
        monitor-enter(r2);
        r3 = 0;
    L_0x0006:
        r4 = r6.mRelaunchingActivities;	 Catch:{ all -> 0x0069 }
        r4 = r4.size();	 Catch:{ all -> 0x0069 }
        if (r3 >= r4) goto L_0x003b;
    L_0x000e:
        r4 = r6.mRelaunchingActivities;	 Catch:{ all -> 0x0069 }
        r4 = r4.get(r3);	 Catch:{ all -> 0x0069 }
        r4 = (android.app.ActivityThread.ActivityClientRecord) r4;	 Catch:{ all -> 0x0069 }
        r5 = r4.token;	 Catch:{ all -> 0x0069 }
        if (r5 != r7) goto L_0x0038;
    L_0x001a:
        r0 = r4;
        if (r8 == 0) goto L_0x0029;
    L_0x001d:
        r5 = r4.pendingResults;	 Catch:{ all -> 0x0069 }
        if (r5 == 0) goto L_0x0027;
    L_0x0021:
        r5 = r4.pendingResults;	 Catch:{ all -> 0x0069 }
        r5.addAll(r8);	 Catch:{ all -> 0x0069 }
        goto L_0x0029;
    L_0x0027:
        r4.pendingResults = r8;	 Catch:{ all -> 0x0069 }
    L_0x0029:
        if (r9 == 0) goto L_0x003b;
    L_0x002b:
        r5 = r4.pendingIntents;	 Catch:{ all -> 0x0069 }
        if (r5 == 0) goto L_0x0035;
    L_0x002f:
        r5 = r4.pendingIntents;	 Catch:{ all -> 0x0069 }
        r5.addAll(r9);	 Catch:{ all -> 0x0069 }
        goto L_0x003b;
    L_0x0035:
        r4.pendingIntents = r9;	 Catch:{ all -> 0x0069 }
        goto L_0x003b;
    L_0x0038:
        r3 = r3 + 1;
        goto L_0x0006;
    L_0x003b:
        if (r0 != 0) goto L_0x0051;
    L_0x003d:
        r3 = new android.app.ActivityThread$ActivityClientRecord;	 Catch:{ all -> 0x0069 }
        r3.<init>();	 Catch:{ all -> 0x0069 }
        r0 = r3;
        r0.token = r7;	 Catch:{ all -> 0x0069 }
        r0.pendingResults = r8;	 Catch:{ all -> 0x0069 }
        r0.pendingIntents = r9;	 Catch:{ all -> 0x0069 }
        r0.mPreserveWindow = r12;	 Catch:{ all -> 0x0069 }
        r3 = r6.mRelaunchingActivities;	 Catch:{ all -> 0x0069 }
        r3.add(r0);	 Catch:{ all -> 0x0069 }
        r1 = 1;
    L_0x0051:
        r3 = r11.getGlobalConfiguration();	 Catch:{ all -> 0x0069 }
        r0.createdConfig = r3;	 Catch:{ all -> 0x0069 }
        r3 = r11.getOverrideConfiguration();	 Catch:{ all -> 0x0069 }
        r0.overrideConfig = r3;	 Catch:{ all -> 0x0069 }
        r3 = r0.pendingConfigChanges;	 Catch:{ all -> 0x0069 }
        r3 = r3 | r10;
        r0.pendingConfigChanges = r3;	 Catch:{ all -> 0x0069 }
        monitor-exit(r2);	 Catch:{ all -> 0x0069 }
        if (r1 == 0) goto L_0x0067;
    L_0x0065:
        r2 = r0;
        goto L_0x0068;
    L_0x0067:
        r2 = 0;
    L_0x0068:
        return r2;
    L_0x0069:
        r3 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0069 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.prepareRelaunchActivity(android.os.IBinder, java.util.List, java.util.List, int, android.util.MergedConfiguration, boolean):android.app.ActivityThread$ActivityClientRecord");
    }

    /* JADX WARNING: Missing block: B:35:0x005c, code skipped:
            if (r12.createdConfig == null) goto L_0x0084;
     */
    /* JADX WARNING: Missing block: B:37:0x0060, code skipped:
            if (r10.mConfiguration == null) goto L_0x0076;
     */
    /* JADX WARNING: Missing block: B:39:0x006a, code skipped:
            if (r12.createdConfig.isOtherSeqNewer(r10.mConfiguration) == false) goto L_0x0084;
     */
    /* JADX WARNING: Missing block: B:41:0x0074, code skipped:
            if (r10.mConfiguration.diff(r12.createdConfig) == 0) goto L_0x0084;
     */
    /* JADX WARNING: Missing block: B:42:0x0076, code skipped:
            if (r1 == null) goto L_0x0080;
     */
    /* JADX WARNING: Missing block: B:44:0x007e, code skipped:
            if (r12.createdConfig.isOtherSeqNewer(r1) == false) goto L_0x0084;
     */
    /* JADX WARNING: Missing block: B:45:0x0080, code skipped:
            r14 = r12.createdConfig;
     */
    /* JADX WARNING: Missing block: B:46:0x0084, code skipped:
            r14 = r1;
     */
    /* JADX WARNING: Missing block: B:47:0x0085, code skipped:
            if (r14 == null) goto L_0x0091;
     */
    /* JADX WARNING: Missing block: B:48:0x0087, code skipped:
            r10.mCurDefaultDisplayDpi = r14.densityDpi;
            updateDefaultDensity();
            handleConfigurationChanged(r14, null);
     */
    /* JADX WARNING: Missing block: B:49:0x0091, code skipped:
            r15 = (android.app.ActivityThread.ActivityClientRecord) r10.mActivities.get(r12.token);
     */
    /* JADX WARNING: Missing block: B:50:0x009c, code skipped:
            if (r15 != null) goto L_0x009f;
     */
    /* JADX WARNING: Missing block: B:51:0x009e, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:52:0x009f, code skipped:
            r1 = r15.activity;
            r1.mConfigChangeFlags |= r13;
            r15.mPreserveWindow = r12.mPreserveWindow;
            r15.activity.mChangingConfigurations = true;
     */
    /* JADX WARNING: Missing block: B:55:0x00b0, code skipped:
            if (r15.mPreserveWindow == false) goto L_0x00bb;
     */
    /* JADX WARNING: Missing block: B:56:0x00b2, code skipped:
            android.view.WindowManagerGlobal.getWindowSession().prepareToReplaceWindows(r15.token, true);
     */
    /* JADX WARNING: Missing block: B:57:0x00bb, code skipped:
            handleRelaunchActivityInner(r15, r13, r12.pendingResults, r12.pendingIntents, r18, r12.startsNotResumed, r12.overrideConfig, "handleRelaunchActivity");
     */
    /* JADX WARNING: Missing block: B:58:0x00cf, code skipped:
            if (r11 == null) goto L_0x00d4;
     */
    /* JADX WARNING: Missing block: B:59:0x00d1, code skipped:
            r11.setReportRelaunchToWindowManager(true);
     */
    /* JADX WARNING: Missing block: B:60:0x00d4, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:61:0x00d5, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:63:0x00da, code skipped:
            throw r0.rethrowFromSystemServer();
     */
    public void handleRelaunchActivity(android.app.ActivityThread.ActivityClientRecord r17, android.app.servertransaction.PendingTransactionActions r18) {
        /*
        r16 = this;
        r10 = r16;
        r11 = r18;
        r16.unscheduleGcIdler();
        r0 = 1;
        r10.mSomeActivitiesChanged = r0;
        r1 = 0;
        r2 = 0;
        r3 = r10.mResourcesManager;
        monitor-enter(r3);
        r4 = r10.mRelaunchingActivities;	 Catch:{ all -> 0x00dd }
        r4 = r4.size();	 Catch:{ all -> 0x00dd }
        r5 = r17;
        r6 = r5.token;	 Catch:{ all -> 0x00db }
        r5 = 0;
        r7 = 0;
        r13 = r2;
        r12 = r5;
    L_0x001d:
        if (r7 >= r4) goto L_0x004b;
    L_0x001f:
        r2 = r10.mRelaunchingActivities;	 Catch:{ all -> 0x0047 }
        r2 = r2.get(r7);	 Catch:{ all -> 0x0047 }
        r2 = (android.app.ActivityThread.ActivityClientRecord) r2;	 Catch:{ all -> 0x0047 }
        r5 = r2.token;	 Catch:{ all -> 0x0047 }
        if (r5 != r6) goto L_0x0045;
    L_0x002b:
        r5 = r2;
        r8 = r5.pendingConfigChanges;	 Catch:{ all -> 0x0040 }
        r8 = r8 | r13;
        r9 = r10.mRelaunchingActivities;	 Catch:{ all -> 0x003b }
        r9.remove(r7);	 Catch:{ all -> 0x003b }
        r7 = r7 + -1;
        r4 = r4 + -1;
        r12 = r5;
        r13 = r8;
        goto L_0x0045;
    L_0x003b:
        r0 = move-exception;
        r12 = r5;
        r2 = r8;
        goto L_0x00e1;
    L_0x0040:
        r0 = move-exception;
        r12 = r5;
        r2 = r13;
        goto L_0x00e1;
    L_0x0045:
        r7 = r7 + r0;
        goto L_0x001d;
    L_0x0047:
        r0 = move-exception;
        r2 = r13;
        goto L_0x00e1;
    L_0x004b:
        if (r12 != 0) goto L_0x004f;
    L_0x004d:
        monitor-exit(r3);	 Catch:{ all -> 0x0047 }
        return;
    L_0x004f:
        r2 = r10.mPendingConfiguration;	 Catch:{ all -> 0x0047 }
        r5 = 0;
        if (r2 == 0) goto L_0x0059;
    L_0x0054:
        r2 = r10.mPendingConfiguration;	 Catch:{ all -> 0x0047 }
        r1 = r2;
        r10.mPendingConfiguration = r5;	 Catch:{ all -> 0x0047 }
    L_0x0059:
        monitor-exit(r3);	 Catch:{ all -> 0x0047 }
        r2 = r12.createdConfig;
        if (r2 == 0) goto L_0x0084;
    L_0x005e:
        r2 = r10.mConfiguration;
        if (r2 == 0) goto L_0x0076;
    L_0x0062:
        r2 = r12.createdConfig;
        r3 = r10.mConfiguration;
        r2 = r2.isOtherSeqNewer(r3);
        if (r2 == 0) goto L_0x0084;
    L_0x006c:
        r2 = r10.mConfiguration;
        r3 = r12.createdConfig;
        r2 = r2.diff(r3);
        if (r2 == 0) goto L_0x0084;
    L_0x0076:
        if (r1 == 0) goto L_0x0080;
    L_0x0078:
        r2 = r12.createdConfig;
        r2 = r2.isOtherSeqNewer(r1);
        if (r2 == 0) goto L_0x0084;
    L_0x0080:
        r1 = r12.createdConfig;
        r14 = r1;
        goto L_0x0085;
    L_0x0084:
        r14 = r1;
    L_0x0085:
        if (r14 == 0) goto L_0x0091;
    L_0x0087:
        r1 = r14.densityDpi;
        r10.mCurDefaultDisplayDpi = r1;
        r16.updateDefaultDensity();
        r10.handleConfigurationChanged(r14, r5);
    L_0x0091:
        r1 = r10.mActivities;
        r2 = r12.token;
        r1 = r1.get(r2);
        r15 = r1;
        r15 = (android.app.ActivityThread.ActivityClientRecord) r15;
        if (r15 != 0) goto L_0x009f;
    L_0x009e:
        return;
    L_0x009f:
        r1 = r15.activity;
        r2 = r1.mConfigChangeFlags;
        r2 = r2 | r13;
        r1.mConfigChangeFlags = r2;
        r1 = r12.mPreserveWindow;
        r15.mPreserveWindow = r1;
        r1 = r15.activity;
        r1.mChangingConfigurations = r0;
        r1 = r15.mPreserveWindow;	 Catch:{ RemoteException -> 0x00d5 }
        if (r1 == 0) goto L_0x00bb;
    L_0x00b2:
        r1 = android.view.WindowManagerGlobal.getWindowSession();	 Catch:{ RemoteException -> 0x00d5 }
        r2 = r15.token;	 Catch:{ RemoteException -> 0x00d5 }
        r1.prepareToReplaceWindows(r2, r0);	 Catch:{ RemoteException -> 0x00d5 }
        r4 = r12.pendingResults;
        r5 = r12.pendingIntents;
        r7 = r12.startsNotResumed;
        r8 = r12.overrideConfig;
        r9 = "handleRelaunchActivity";
        r1 = r16;
        r2 = r15;
        r3 = r13;
        r6 = r18;
        r1.handleRelaunchActivityInner(r2, r3, r4, r5, r6, r7, r8, r9);
        if (r11 == 0) goto L_0x00d4;
    L_0x00d1:
        r11.setReportRelaunchToWindowManager(r0);
    L_0x00d4:
        return;
    L_0x00d5:
        r0 = move-exception;
        r1 = r0.rethrowFromSystemServer();
        throw r1;
    L_0x00db:
        r0 = move-exception;
        goto L_0x00e0;
    L_0x00dd:
        r0 = move-exception;
        r5 = r17;
    L_0x00e0:
        r12 = r5;
    L_0x00e1:
        monitor-exit(r3);	 Catch:{ all -> 0x00e3 }
        throw r0;
    L_0x00e3:
        r0 = move-exception;
        goto L_0x00e1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.handleRelaunchActivity(android.app.ActivityThread$ActivityClientRecord, android.app.servertransaction.PendingTransactionActions):void");
    }

    /* Access modifiers changed, original: 0000 */
    public void scheduleRelaunchActivity(IBinder token) {
        this.mH.removeMessages(160, token);
        sendMessage(160, token);
    }

    private void handleRelaunchActivityLocally(IBinder token) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        String str = TAG;
        if (r == null) {
            Log.w(str, "Activity to relaunch no longer exists");
            return;
        }
        int prevState = r.getLifecycleState();
        if (prevState < 3 || prevState > 5) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Activity state must be in [ON_RESUME..ON_STOP] in order to be relaunched,current state is ");
            stringBuilder.append(prevState);
            Log.w(str, stringBuilder.toString());
            return;
        }
        ActivityRelaunchItem activityRelaunchItem = ActivityRelaunchItem.obtain(null, null, null, new MergedConfiguration(r.createdConfig != null ? r.createdConfig : this.mConfiguration, r.overrideConfig), r.mPreserveWindow);
        ActivityLifecycleItem lifecycleRequest = TransactionExecutorHelper.getLifecycleRequestForCurrentState(r);
        ClientTransaction transaction = ClientTransaction.obtain(this.mAppThread, r.token);
        transaction.addCallback(activityRelaunchItem);
        transaction.setLifecycleStateRequest(lifecycleRequest);
        executeTransaction(transaction);
    }

    private void handleRelaunchActivityInner(ActivityClientRecord r, int configChanges, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingIntents, PendingTransactionActions pendingActions, boolean startsNotResumed, Configuration overrideConfig, String reason) {
        ActivityClientRecord activityClientRecord = r;
        List<ResultInfo> list = pendingResults;
        List<ReferrerIntent> list2 = pendingIntents;
        String str = reason;
        Intent customIntent = activityClientRecord.activity.mIntent;
        if (!activityClientRecord.paused) {
            performPauseActivity(r, false, str, null);
        }
        if (!activityClientRecord.stopped) {
            callActivityOnStop(r, true, str);
        }
        handleDestroyActivity(activityClientRecord.token, false, configChanges, true, reason);
        activityClientRecord.activity = null;
        activityClientRecord.window = null;
        activityClientRecord.hideForNow = false;
        activityClientRecord.nextIdle = null;
        if (list != null) {
            if (activityClientRecord.pendingResults == null) {
                activityClientRecord.pendingResults = list;
            } else {
                activityClientRecord.pendingResults.addAll(list);
            }
        }
        if (list2 != null) {
            if (activityClientRecord.pendingIntents == null) {
                activityClientRecord.pendingIntents = list2;
            } else {
                activityClientRecord.pendingIntents.addAll(list2);
            }
        }
        activityClientRecord.startsNotResumed = startsNotResumed;
        activityClientRecord.overrideConfig = overrideConfig;
        handleLaunchActivity(r, pendingActions, customIntent);
    }

    public void reportRelaunch(IBinder token, PendingTransactionActions pendingActions) {
        try {
            ActivityTaskManager.getService().activityRelaunched(token);
            ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
            if (pendingActions.shouldReportRelaunchToWindowManager() && r != null && r.window != null) {
                r.window.reportActivityRelaunched();
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void callActivityOnSaveInstanceState(ActivityClientRecord r) {
        r.state = new Bundle();
        r.state.setAllowFds(false);
        if (r.isPersistable()) {
            r.persistentState = new PersistableBundle();
            this.mInstrumentation.callActivityOnSaveInstanceState(r.activity, r.state, r.persistentState);
            return;
        }
        this.mInstrumentation.callActivityOnSaveInstanceState(r.activity, r.state);
    }

    /* Access modifiers changed, original: 0000 */
    public ArrayList<ComponentCallbacks2> collectComponentCallbacks(boolean allActivities, Configuration newConfig) {
        int i;
        ArrayList<ComponentCallbacks2> callbacks = new ArrayList();
        synchronized (this.mResourcesManager) {
            int i2;
            int NAPP = this.mAllApplications.size();
            for (i = 0; i < NAPP; i++) {
                callbacks.add((ComponentCallbacks2) this.mAllApplications.get(i));
            }
            i = this.mActivities.size();
            for (i2 = 0; i2 < i; i2++) {
                ActivityClientRecord ar = (ActivityClientRecord) this.mActivities.valueAt(i2);
                Activity a = ar.activity;
                if (a != null) {
                    Configuration thisConfig = applyConfigCompatMainThread(this.mCurDefaultDisplayDpi, newConfig, ar.packageInfo.getCompatibilityInfo());
                    if (!ar.activity.mFinished && (allActivities || !ar.paused)) {
                        callbacks.add(a);
                    } else if (thisConfig != null) {
                        ar.newConfig = thisConfig;
                    }
                }
            }
            i2 = this.mServices.size();
            for (int i3 = 0; i3 < i2; i3++) {
                callbacks.add((ComponentCallbacks2) this.mServices.valueAt(i3));
            }
        }
        synchronized (this.mProviderMap) {
            int NPRV = this.mLocalProviders.size();
            for (i = 0; i < NPRV; i++) {
                callbacks.add(((ProviderClientRecord) this.mLocalProviders.valueAt(i)).mLocalProvider);
            }
        }
        return callbacks;
    }

    private void performConfigurationChangedForActivity(ActivityClientRecord r, Configuration newBaseConfig) {
        performConfigurationChangedForActivity(r, newBaseConfig, r.activity.getDisplayId(), false);
    }

    private Configuration performConfigurationChangedForActivity(ActivityClientRecord r, Configuration newBaseConfig, int displayId, boolean movedToDifferentDisplay) {
        r.tmpConfig.setTo(newBaseConfig);
        if (r.overrideConfig != null) {
            r.tmpConfig.updateFrom(r.overrideConfig);
        }
        Configuration reportedConfig = performActivityConfigurationChanged(r.activity, r.tmpConfig, r.overrideConfig, displayId, movedToDifferentDisplay);
        freeTextLayoutCachesIfNeeded(r.activity.mCurrentConfig.diff(r.tmpConfig));
        return reportedConfig;
    }

    private static Configuration createNewConfigAndUpdateIfNotNull(Configuration base, Configuration override) {
        if (override == null) {
            return base;
        }
        Configuration newConfig = new Configuration(base);
        newConfig.updateFrom(override);
        return newConfig;
    }

    private void performConfigurationChanged(ComponentCallbacks2 cb, Configuration newConfig) {
        Configuration contextThemeWrapperOverrideConfig = null;
        if (cb instanceof ContextThemeWrapper) {
            contextThemeWrapperOverrideConfig = ((ContextThemeWrapper) cb).getOverrideConfiguration();
        }
        cb.onConfigurationChanged(createNewConfigAndUpdateIfNotNull(newConfig, contextThemeWrapperOverrideConfig));
    }

    private Configuration performActivityConfigurationChanged(Activity activity, Configuration newConfig, Configuration amOverrideConfig, int displayId, boolean movedToDifferentDisplay) {
        if (activity != null) {
            IBinder activityToken = activity.getActivityToken();
            if (activityToken != null) {
                boolean shouldChangeConfig = false;
                if (activity.mCurrentConfig == null) {
                    shouldChangeConfig = true;
                } else {
                    int diff = activity.mCurrentConfig.diffPublicOnly(newConfig);
                    if (!(diff == 0 && this.mResourcesManager.isSameResourcesOverrideConfig(activityToken, amOverrideConfig)) && (!this.mUpdatingSystemConfig || ((~activity.mActivityInfo.getRealConfigChanged()) & diff) == 0)) {
                        shouldChangeConfig = true;
                    }
                }
                if (!shouldChangeConfig && !movedToDifferentDisplay) {
                    return null;
                }
                Configuration contextThemeWrapperOverrideConfig = activity.getOverrideConfiguration();
                this.mResourcesManager.updateResourcesForActivity(activityToken, createNewConfigAndUpdateIfNotNull(amOverrideConfig, contextThemeWrapperOverrideConfig), displayId, movedToDifferentDisplay);
                activity.mConfigChangeFlags = 0;
                activity.mCurrentConfig = new Configuration(newConfig);
                Configuration configToReport = createNewConfigAndUpdateIfNotNull(newConfig, contextThemeWrapperOverrideConfig);
                if (movedToDifferentDisplay) {
                    activity.dispatchMovedToDisplay(displayId, configToReport);
                }
                if (shouldChangeConfig) {
                    activity.mCalled = false;
                    activity.onConfigurationChanged(configToReport);
                    if (!activity.mCalled) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Activity ");
                        stringBuilder.append(activity.getLocalClassName());
                        stringBuilder.append(" did not call through to super.onConfigurationChanged()");
                        throw new SuperNotCalledException(stringBuilder.toString());
                    }
                }
                return configToReport;
            }
            throw new IllegalArgumentException("Activity token not set. Is the activity attached?");
        }
        throw new IllegalArgumentException("No activity provided.");
    }

    public final void applyConfigurationToResources(Configuration config) {
        synchronized (this.mResourcesManager) {
            this.mResourcesManager.applyConfigurationToResourcesLocked(config, null);
        }
    }

    /* Access modifiers changed, original: final */
    public final Configuration applyCompatConfiguration(int displayDensity) {
        Configuration config = this.mConfiguration;
        if (this.mCompatConfiguration == null) {
            this.mCompatConfiguration = new Configuration();
        }
        this.mCompatConfiguration.setTo(this.mConfiguration);
        if (this.mResourcesManager.applyCompatConfigurationLocked(displayDensity, this.mCompatConfiguration)) {
            return this.mCompatConfiguration;
        }
        return config;
    }

    public void handleConfigurationChanged(Configuration config) {
        Trace.traceBegin(64, "configChanged");
        this.mCurDefaultDisplayDpi = config.densityDpi;
        this.mUpdatingSystemConfig = true;
        try {
            handleConfigurationChanged(config, null);
            Trace.traceEnd(64);
        } finally {
            this.mUpdatingSystemConfig = false;
        }
    }

    /* JADX WARNING: Missing block: B:33:0x0091, code skipped:
            r2 = collectComponentCallbacks(false, r12);
            freeTextLayoutCachesIfNeeded(r5);
     */
    /* JADX WARNING: Missing block: B:34:0x0098, code skipped:
            if (r2 == null) goto L_0x00c8;
     */
    /* JADX WARNING: Missing block: B:35:0x009a, code skipped:
            r4 = r2.size();
            r6 = 0;
     */
    /* JADX WARNING: Missing block: B:36:0x009f, code skipped:
            if (r6 >= r4) goto L_0x00c8;
     */
    /* JADX WARNING: Missing block: B:37:0x00a1, code skipped:
            r7 = (android.content.ComponentCallbacks2) r2.get(r6);
     */
    /* JADX WARNING: Missing block: B:38:0x00a9, code skipped:
            if ((r7 instanceof android.app.Activity) == false) goto L_0x00be;
     */
    /* JADX WARNING: Missing block: B:39:0x00ab, code skipped:
            performConfigurationChangedForActivity((android.app.ActivityThread.ActivityClientRecord) r11.mActivities.get(((android.app.Activity) r7).getActivityToken()), r12);
     */
    /* JADX WARNING: Missing block: B:40:0x00be, code skipped:
            if (r3 != false) goto L_0x00c5;
     */
    /* JADX WARNING: Missing block: B:41:0x00c0, code skipped:
            performConfigurationChanged(r7, r12);
     */
    /* JADX WARNING: Missing block: B:42:0x00c5, code skipped:
            r6 = r6 + 1;
     */
    /* JADX WARNING: Missing block: B:43:0x00c8, code skipped:
            return;
     */
    private void handleConfigurationChanged(android.content.res.Configuration r12, android.content.res.CompatibilityInfo r13) {
        /*
        r11 = this;
        r0 = r11.getSystemContext();
        r0 = r0.getTheme();
        r1 = r11.getSystemUiContext();
        r1 = r1.getTheme();
        r2 = r11.mResourcesManager;
        monitor-enter(r2);
        r3 = r11.mPendingConfiguration;	 Catch:{ all -> 0x00c9 }
        if (r3 == 0) goto L_0x002c;
    L_0x0017:
        r3 = r11.mPendingConfiguration;	 Catch:{ all -> 0x00c9 }
        r3 = r3.isOtherSeqNewer(r12);	 Catch:{ all -> 0x00c9 }
        if (r3 != 0) goto L_0x0029;
    L_0x001f:
        r3 = r11.mPendingConfiguration;	 Catch:{ all -> 0x00c9 }
        r12 = r3;
        r3 = r12.densityDpi;	 Catch:{ all -> 0x00c9 }
        r11.mCurDefaultDisplayDpi = r3;	 Catch:{ all -> 0x00c9 }
        r11.updateDefaultDensity();	 Catch:{ all -> 0x00c9 }
    L_0x0029:
        r3 = 0;
        r11.mPendingConfiguration = r3;	 Catch:{ all -> 0x00c9 }
    L_0x002c:
        if (r12 != 0) goto L_0x0030;
    L_0x002e:
        monitor-exit(r2);	 Catch:{ all -> 0x00c9 }
        return;
    L_0x0030:
        r3 = r11.mConfiguration;	 Catch:{ all -> 0x00c9 }
        r4 = 0;
        if (r3 == 0) goto L_0x003f;
    L_0x0035:
        r3 = r11.mConfiguration;	 Catch:{ all -> 0x00c9 }
        r3 = r3.diffPublicOnly(r12);	 Catch:{ all -> 0x00c9 }
        if (r3 != 0) goto L_0x003f;
    L_0x003d:
        r3 = 1;
        goto L_0x0040;
    L_0x003f:
        r3 = r4;
    L_0x0040:
        r5 = r11.mResourcesManager;	 Catch:{ all -> 0x00c9 }
        r5.applyConfigurationToResourcesLocked(r12, r13);	 Catch:{ all -> 0x00c9 }
        r5 = r11.mInitialApplication;	 Catch:{ all -> 0x00c9 }
        r5 = r5.getApplicationContext();	 Catch:{ all -> 0x00c9 }
        r6 = r11.mResourcesManager;	 Catch:{ all -> 0x00c9 }
        r6 = r6.getConfiguration();	 Catch:{ all -> 0x00c9 }
        r6 = r6.getLocales();	 Catch:{ all -> 0x00c9 }
        r11.updateLocaleListFromAppContext(r5, r6);	 Catch:{ all -> 0x00c9 }
        r5 = r11.mConfiguration;	 Catch:{ all -> 0x00c9 }
        if (r5 != 0) goto L_0x0063;
    L_0x005c:
        r5 = new android.content.res.Configuration;	 Catch:{ all -> 0x00c9 }
        r5.<init>();	 Catch:{ all -> 0x00c9 }
        r11.mConfiguration = r5;	 Catch:{ all -> 0x00c9 }
    L_0x0063:
        r5 = r11.mConfiguration;	 Catch:{ all -> 0x00c9 }
        r5 = r5.isOtherSeqNewer(r12);	 Catch:{ all -> 0x00c9 }
        if (r5 != 0) goto L_0x006f;
    L_0x006b:
        if (r13 != 0) goto L_0x006f;
    L_0x006d:
        monitor-exit(r2);	 Catch:{ all -> 0x00c9 }
        return;
    L_0x006f:
        r5 = r11.mConfiguration;	 Catch:{ all -> 0x00c9 }
        r5 = r5.updateFrom(r12);	 Catch:{ all -> 0x00c9 }
        r6 = r11.mCurDefaultDisplayDpi;	 Catch:{ all -> 0x00c9 }
        r6 = r11.applyCompatConfiguration(r6);	 Catch:{ all -> 0x00c9 }
        r12 = r6;
        r6 = r0.getChangingConfigurations();	 Catch:{ all -> 0x00c9 }
        r6 = r6 & r5;
        if (r6 == 0) goto L_0x0086;
    L_0x0083:
        r0.rebase();	 Catch:{ all -> 0x00c9 }
    L_0x0086:
        r6 = r1.getChangingConfigurations();	 Catch:{ all -> 0x00c9 }
        r6 = r6 & r5;
        if (r6 == 0) goto L_0x0090;
    L_0x008d:
        r1.rebase();	 Catch:{ all -> 0x00c9 }
    L_0x0090:
        monitor-exit(r2);	 Catch:{ all -> 0x00c9 }
        r2 = r11.collectComponentCallbacks(r4, r12);
        freeTextLayoutCachesIfNeeded(r5);
        if (r2 == 0) goto L_0x00c8;
    L_0x009a:
        r4 = r2.size();
        r6 = 0;
    L_0x009f:
        if (r6 >= r4) goto L_0x00c8;
    L_0x00a1:
        r7 = r2.get(r6);
        r7 = (android.content.ComponentCallbacks2) r7;
        r8 = r7 instanceof android.app.Activity;
        if (r8 == 0) goto L_0x00be;
    L_0x00ab:
        r8 = r7;
        r8 = (android.app.Activity) r8;
        r9 = r11.mActivities;
        r10 = r8.getActivityToken();
        r9 = r9.get(r10);
        r9 = (android.app.ActivityThread.ActivityClientRecord) r9;
        r11.performConfigurationChangedForActivity(r9, r12);
        goto L_0x00c4;
    L_0x00be:
        if (r3 != 0) goto L_0x00c4;
    L_0x00c0:
        r11.performConfigurationChanged(r7, r12);
        goto L_0x00c5;
    L_0x00c5:
        r6 = r6 + 1;
        goto L_0x009f;
    L_0x00c8:
        return;
    L_0x00c9:
        r3 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x00c9 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.handleConfigurationChanged(android.content.res.Configuration, android.content.res.CompatibilityInfo):void");
    }

    public void handleSystemApplicationInfoChanged(ApplicationInfo ai) {
        Preconditions.checkState(this.mSystemThread, "Must only be called in the system process");
        handleApplicationInfoChanged(ai);
    }

    @VisibleForTesting(visibility = Visibility.PACKAGE)
    public void handleApplicationInfoChanged(ApplicationInfo ai) {
        LoadedApk apk;
        LoadedApk resApk;
        synchronized (this.mResourcesManager) {
            WeakReference<LoadedApk> ref = (WeakReference) this.mPackages.get(ai.packageName);
            apk = ref != null ? (LoadedApk) ref.get() : null;
            ref = (WeakReference) this.mResourcePackages.get(ai.packageName);
            resApk = ref != null ? (LoadedApk) ref.get() : null;
        }
        String[] oldResDirs = new String[2];
        int i = 0;
        if (apk != null) {
            oldResDirs[0] = apk.getResDir();
            ArrayList<String> oldPaths = new ArrayList();
            LoadedApk.makePaths(this, apk.getApplicationInfo(), oldPaths);
            apk.updateApplicationInfo(ai, oldPaths);
        }
        if (resApk != null) {
            oldResDirs[1] = resApk.getResDir();
            ArrayList<String> oldPaths2 = new ArrayList();
            LoadedApk.makePaths(this, resApk.getApplicationInfo(), oldPaths2);
            resApk.updateApplicationInfo(ai, oldPaths2);
        }
        synchronized (this.mResourcesManager) {
            this.mResourcesManager.applyNewResourceDirsLocked(ai, oldResDirs);
        }
        ApplicationPackageManager.configurationChanged();
        Configuration newConfig = new Configuration();
        Configuration configuration = this.mConfiguration;
        if (configuration != null) {
            i = configuration.assetsSeq;
        }
        newConfig.assetsSeq = i + 1;
        handleConfigurationChanged(newConfig, null);
        relaunchAllActivities(true);
    }

    static void freeTextLayoutCachesIfNeeded(int configDiff) {
        if (configDiff != 0) {
            if ((configDiff & 4) != 0) {
                Canvas.freeTextLayoutCaches();
            }
        }
    }

    public void updatePendingActivityConfiguration(IBinder activityToken, Configuration overrideConfig) {
        ActivityClientRecord r;
        synchronized (this.mResourcesManager) {
            r = (ActivityClientRecord) this.mActivities.get(activityToken);
        }
        if (r != null) {
            synchronized (r) {
                r.mPendingOverrideConfig = overrideConfig;
            }
        }
    }

    public void handleActivityConfigurationChanged(IBinder activityToken, Configuration overrideConfig, int displayId) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(activityToken);
        if (r != null && r.activity != null) {
            ViewRootImpl viewRoot;
            boolean movedToDifferentDisplay = (displayId == -1 || displayId == r.activity.getDisplayId()) ? false : true;
            synchronized (r) {
                if (!(r.mPendingOverrideConfig == null || r.mPendingOverrideConfig.isOtherSeqNewer(overrideConfig))) {
                    overrideConfig = r.mPendingOverrideConfig;
                }
                viewRoot = null;
                r.mPendingOverrideConfig = null;
            }
            if (r.overrideConfig == null || r.overrideConfig.isOtherSeqNewer(overrideConfig) || movedToDifferentDisplay) {
                r.overrideConfig = overrideConfig;
                if (r.activity.mDecor != null) {
                    viewRoot = r.activity.mDecor.getViewRootImpl();
                }
                if (movedToDifferentDisplay) {
                    Configuration reportedConfig = performConfigurationChangedForActivity(r, this.mCompatConfiguration, displayId, true);
                    if (viewRoot != null) {
                        viewRoot.onMovedToDisplay(displayId, reportedConfig);
                    }
                } else {
                    performConfigurationChangedForActivity(r, this.mCompatConfiguration);
                }
                if (viewRoot != null) {
                    viewRoot.updateConfiguration(displayId);
                }
                this.mSomeActivitiesChanged = true;
            }
        }
    }

    /* Access modifiers changed, original: final */
    public final void handleProfilerControl(boolean start, ProfilerInfo profilerInfo, int profileType) {
        if (start) {
            try {
                this.mProfiler.setProfiler(profilerInfo);
                this.mProfiler.startProfiling();
            } catch (RuntimeException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Profiling failed on path ");
                stringBuilder.append(profilerInfo.profileFile);
                stringBuilder.append(" -- can the process access this path?");
                Slog.w(str, stringBuilder.toString());
            } catch (Throwable th) {
                profilerInfo.closeFd();
            }
            profilerInfo.closeFd();
            return;
        }
        this.mProfiler.stopProfiling();
    }

    public void stopProfiling() {
        Profiler profiler = this.mProfiler;
        if (profiler != null) {
            profiler.stopProfiling();
        }
    }

    /* JADX WARNING: Missing block: B:20:0x003b, code skipped:
            if (r1 != null) goto L_0x003d;
     */
    /* JADX WARNING: Missing block: B:22:?, code skipped:
            r1.close();
     */
    static void handleDumpHeap(android.app.ActivityThread.DumpHeapData r5) {
        /*
        r0 = "ActivityThread";
        r1 = r5.runGc;
        if (r1 == 0) goto L_0x000f;
    L_0x0006:
        java.lang.System.gc();
        java.lang.System.runFinalization();
        java.lang.System.gc();
    L_0x000f:
        r1 = r5.fd;	 Catch:{ IOException -> 0x004d, RuntimeException -> 0x0046 }
        r2 = r5.managed;	 Catch:{ all -> 0x0038 }
        if (r2 == 0) goto L_0x001f;
    L_0x0015:
        r2 = r5.path;	 Catch:{ all -> 0x0038 }
        r3 = r1.getFileDescriptor();	 Catch:{ all -> 0x0038 }
        android.os.Debug.dumpHprofData(r2, r3);	 Catch:{ all -> 0x0038 }
        goto L_0x0032;
    L_0x001f:
        r2 = r5.mallocInfo;	 Catch:{ all -> 0x0038 }
        if (r2 == 0) goto L_0x002b;
    L_0x0023:
        r2 = r1.getFileDescriptor();	 Catch:{ all -> 0x0038 }
        android.os.Debug.dumpNativeMallocInfo(r2);	 Catch:{ all -> 0x0038 }
        goto L_0x0032;
    L_0x002b:
        r2 = r1.getFileDescriptor();	 Catch:{ all -> 0x0038 }
        android.os.Debug.dumpNativeHeap(r2);	 Catch:{ all -> 0x0038 }
    L_0x0032:
        if (r1 == 0) goto L_0x0073;
    L_0x0034:
        r1.close();	 Catch:{ IOException -> 0x004d, RuntimeException -> 0x0046 }
        goto L_0x0073;
    L_0x0038:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x003a }
    L_0x003a:
        r3 = move-exception;
        if (r1 == 0) goto L_0x0045;
    L_0x003d:
        r1.close();	 Catch:{ all -> 0x0041 }
        goto L_0x0045;
    L_0x0041:
        r4 = move-exception;
        r2.addSuppressed(r4);	 Catch:{ IOException -> 0x004d, RuntimeException -> 0x0046 }
    L_0x0045:
        throw r3;	 Catch:{ IOException -> 0x004d, RuntimeException -> 0x0046 }
    L_0x0046:
        r1 = move-exception;
        r2 = "Heap dumper threw a runtime exception";
        android.util.Slog.wtf(r0, r2, r1);
        goto L_0x0074;
    L_0x004d:
        r1 = move-exception;
        r2 = r5.managed;
        if (r2 == 0) goto L_0x006e;
    L_0x0052:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Managed heap dump failed on path ";
        r2.append(r3);
        r3 = r5.path;
        r2.append(r3);
        r3 = " -- can the process access this path?";
        r2.append(r3);
        r2 = r2.toString();
        android.util.Slog.w(r0, r2, r1);
        goto L_0x0073;
    L_0x006e:
        r2 = "Failed to dump heap";
        android.util.Slog.w(r0, r2, r1);
    L_0x0074:
        r0 = android.app.ActivityManager.getService();	 Catch:{ RemoteException -> 0x0089 }
        r1 = r5.path;	 Catch:{ RemoteException -> 0x0089 }
        r0.dumpHeapFinished(r1);	 Catch:{ RemoteException -> 0x0089 }
        r0 = r5.finishCallback;
        if (r0 == 0) goto L_0x0088;
    L_0x0082:
        r0 = r5.finishCallback;
        r1 = 0;
        r0.sendResult(r1);
    L_0x0088:
        return;
    L_0x0089:
        r0 = move-exception;
        r1 = r0.rethrowFromSystemServer();
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.handleDumpHeap(android.app.ActivityThread$DumpHeapData):void");
    }

    /* Access modifiers changed, original: final */
    /* JADX WARNING: Missing block: B:57:?, code skipped:
            getPackageManager().notifyPackagesReplacedReceived((java.lang.String[]) r7.toArray(new java.lang.String[0]));
     */
    public final void handleDispatchPackageBroadcast(int r17, java.lang.String[] r18) {
        /*
        r16 = this;
        r1 = r16;
        r2 = r17;
        r3 = r18;
        r4 = 0;
        r5 = 0;
        r6 = 1;
        if (r2 == 0) goto L_0x00eb;
    L_0x000b:
        r0 = 2;
        if (r2 == r0) goto L_0x00eb;
    L_0x000e:
        r0 = 3;
        if (r2 == r0) goto L_0x0013;
    L_0x0011:
        goto L_0x0138;
    L_0x0013:
        if (r3 != 0) goto L_0x0017;
    L_0x0015:
        goto L_0x0138;
    L_0x0017:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r7 = r0;
        r8 = r1.mResourcesManager;
        monitor-enter(r8);
        r0 = r3.length;	 Catch:{ all -> 0x00e8 }
        r0 = r0 - r6;
        r9 = r4;
        r4 = r0;
    L_0x0024:
        if (r4 < 0) goto L_0x00d0;
    L_0x0026:
        r0 = r3[r4];	 Catch:{ all -> 0x00e5 }
        r10 = r0;
        r0 = r1.mPackages;	 Catch:{ all -> 0x00e5 }
        r0 = r0.get(r10);	 Catch:{ all -> 0x00e5 }
        r0 = (java.lang.ref.WeakReference) r0;	 Catch:{ all -> 0x00e5 }
        r11 = 0;
        if (r0 == 0) goto L_0x003b;
    L_0x0034:
        r12 = r0.get();	 Catch:{ all -> 0x00e5 }
        r12 = (android.app.LoadedApk) r12;	 Catch:{ all -> 0x00e5 }
        goto L_0x003c;
    L_0x003b:
        r12 = r11;
    L_0x003c:
        if (r12 == 0) goto L_0x0042;
    L_0x003e:
        r9 = 1;
        r11 = r9;
        r9 = r0;
        goto L_0x005c;
    L_0x0042:
        r13 = r1.mResourcePackages;	 Catch:{ all -> 0x00e5 }
        r13 = r13.get(r10);	 Catch:{ all -> 0x00e5 }
        r13 = (java.lang.ref.WeakReference) r13;	 Catch:{ all -> 0x00e5 }
        r0 = r13;
        if (r0 == 0) goto L_0x0053;
    L_0x004d:
        r11 = r0.get();	 Catch:{ all -> 0x00e5 }
        r11 = (android.app.LoadedApk) r11;	 Catch:{ all -> 0x00e5 }
    L_0x0053:
        r12 = r11;
        if (r12 == 0) goto L_0x005a;
    L_0x0056:
        r9 = 1;
        r11 = r9;
        r9 = r0;
        goto L_0x005c;
    L_0x005a:
        r11 = r9;
        r9 = r0;
    L_0x005c:
        if (r12 == 0) goto L_0x00ca;
    L_0x005e:
        r7.add(r10);	 Catch:{ all -> 0x00c7 }
        r0 = sPackageManager;	 Catch:{ RemoteException -> 0x00c5 }
        r13 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r14 = android.os.UserHandle.myUserId();	 Catch:{ RemoteException -> 0x00c5 }
        r0 = r0.getApplicationInfo(r10, r13, r14);	 Catch:{ RemoteException -> 0x00c5 }
        r13 = r0;
        r0 = r1.mActivities;	 Catch:{ RemoteException -> 0x00c5 }
        r0 = r0.size();	 Catch:{ RemoteException -> 0x00c5 }
        if (r0 <= 0) goto L_0x009f;
    L_0x0076:
        r0 = r1.mActivities;	 Catch:{ RemoteException -> 0x00c5 }
        r0 = r0.values();	 Catch:{ RemoteException -> 0x00c5 }
        r0 = r0.iterator();	 Catch:{ RemoteException -> 0x00c5 }
    L_0x0080:
        r14 = r0.hasNext();	 Catch:{ RemoteException -> 0x00c5 }
        if (r14 == 0) goto L_0x009f;
    L_0x0086:
        r14 = r0.next();	 Catch:{ RemoteException -> 0x00c5 }
        r14 = (android.app.ActivityThread.ActivityClientRecord) r14;	 Catch:{ RemoteException -> 0x00c5 }
        r15 = r14.activityInfo;	 Catch:{ RemoteException -> 0x00c5 }
        r15 = r15.applicationInfo;	 Catch:{ RemoteException -> 0x00c5 }
        r15 = r15.packageName;	 Catch:{ RemoteException -> 0x00c5 }
        r15 = r15.equals(r10);	 Catch:{ RemoteException -> 0x00c5 }
        if (r15 == 0) goto L_0x009e;
    L_0x0098:
        r15 = r14.activityInfo;	 Catch:{ RemoteException -> 0x00c5 }
        r15.applicationInfo = r13;	 Catch:{ RemoteException -> 0x00c5 }
        r14.packageInfo = r12;	 Catch:{ RemoteException -> 0x00c5 }
    L_0x009e:
        goto L_0x0080;
    L_0x009f:
        r0 = new java.lang.String[r6];	 Catch:{ RemoteException -> 0x00c5 }
        r14 = r12.getResDir();	 Catch:{ RemoteException -> 0x00c5 }
        r0[r5] = r14;	 Catch:{ RemoteException -> 0x00c5 }
        r14 = r0;
        r0 = new java.util.ArrayList;	 Catch:{ RemoteException -> 0x00c5 }
        r0.<init>();	 Catch:{ RemoteException -> 0x00c5 }
        r15 = r0;
        r0 = r12.getApplicationInfo();	 Catch:{ RemoteException -> 0x00c5 }
        android.app.LoadedApk.makePaths(r1, r0, r15);	 Catch:{ RemoteException -> 0x00c5 }
        r12.updateApplicationInfo(r13, r15);	 Catch:{ RemoteException -> 0x00c5 }
        r6 = r1.mResourcesManager;	 Catch:{ RemoteException -> 0x00c5 }
        monitor-enter(r6);	 Catch:{ RemoteException -> 0x00c5 }
        r0 = r1.mResourcesManager;	 Catch:{ all -> 0x00c2 }
        r0.applyNewResourceDirsLocked(r13, r14);	 Catch:{ all -> 0x00c2 }
        monitor-exit(r6);	 Catch:{ all -> 0x00c2 }
        goto L_0x00ca;
    L_0x00c2:
        r0 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x00c2 }
        throw r0;	 Catch:{ RemoteException -> 0x00c5 }
    L_0x00c5:
        r0 = move-exception;
        goto L_0x00ca;
    L_0x00c7:
        r0 = move-exception;
        r4 = r11;
        goto L_0x00e9;
    L_0x00ca:
        r4 = r4 + -1;
        r9 = r11;
        r6 = 1;
        goto L_0x0024;
    L_0x00d0:
        monitor-exit(r8);	 Catch:{ all -> 0x00e5 }
        r0 = getPackageManager();	 Catch:{ RemoteException -> 0x00e1 }
        r4 = new java.lang.String[r5];	 Catch:{ RemoteException -> 0x00e1 }
        r4 = r7.toArray(r4);	 Catch:{ RemoteException -> 0x00e1 }
        r4 = (java.lang.String[]) r4;	 Catch:{ RemoteException -> 0x00e1 }
        r0.notifyPackagesReplacedReceived(r4);	 Catch:{ RemoteException -> 0x00e1 }
        goto L_0x00e3;
    L_0x00e1:
        r0 = move-exception;
    L_0x00e3:
        r4 = r9;
        goto L_0x0138;
    L_0x00e5:
        r0 = move-exception;
        r4 = r9;
        goto L_0x00e9;
    L_0x00e8:
        r0 = move-exception;
    L_0x00e9:
        monitor-exit(r8);	 Catch:{ all -> 0x00e8 }
        throw r0;
    L_0x00eb:
        if (r2 != 0) goto L_0x00ee;
    L_0x00ed:
        r5 = 1;
    L_0x00ee:
        if (r3 != 0) goto L_0x00f1;
    L_0x00f0:
        goto L_0x0138;
    L_0x00f1:
        r6 = r1.mResourcesManager;
        monitor-enter(r6);
        r0 = r3.length;	 Catch:{ all -> 0x013c }
        r7 = 1;
        r0 = r0 - r7;
    L_0x00f7:
        if (r0 < 0) goto L_0x0136;
    L_0x00f9:
        if (r4 != 0) goto L_0x0123;
    L_0x00fb:
        r7 = r1.mPackages;	 Catch:{ all -> 0x013c }
        r8 = r3[r0];	 Catch:{ all -> 0x013c }
        r7 = r7.get(r8);	 Catch:{ all -> 0x013c }
        r7 = (java.lang.ref.WeakReference) r7;	 Catch:{ all -> 0x013c }
        if (r7 == 0) goto L_0x010f;
    L_0x0107:
        r8 = r7.get();	 Catch:{ all -> 0x013c }
        if (r8 == 0) goto L_0x010f;
    L_0x010d:
        r4 = 1;
        goto L_0x0123;
    L_0x010f:
        r8 = r1.mResourcePackages;	 Catch:{ all -> 0x013c }
        r9 = r3[r0];	 Catch:{ all -> 0x013c }
        r8 = r8.get(r9);	 Catch:{ all -> 0x013c }
        r8 = (java.lang.ref.WeakReference) r8;	 Catch:{ all -> 0x013c }
        r7 = r8;
        if (r7 == 0) goto L_0x0123;
    L_0x011c:
        r8 = r7.get();	 Catch:{ all -> 0x013c }
        if (r8 == 0) goto L_0x0123;
    L_0x0122:
        r4 = 1;
    L_0x0123:
        if (r5 == 0) goto L_0x0133;
    L_0x0125:
        r7 = r1.mPackages;	 Catch:{ all -> 0x013c }
        r8 = r3[r0];	 Catch:{ all -> 0x013c }
        r7.remove(r8);	 Catch:{ all -> 0x013c }
        r7 = r1.mResourcePackages;	 Catch:{ all -> 0x013c }
        r8 = r3[r0];	 Catch:{ all -> 0x013c }
        r7.remove(r8);	 Catch:{ all -> 0x013c }
    L_0x0133:
        r0 = r0 + -1;
        goto L_0x00f7;
    L_0x0136:
        monitor-exit(r6);	 Catch:{ all -> 0x013c }
    L_0x0138:
        android.app.ApplicationPackageManager.handlePackageBroadcast(r2, r3, r4);
        return;
    L_0x013c:
        r0 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x013c }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.handleDispatchPackageBroadcast(int, java.lang.String[]):void");
    }

    /* Access modifiers changed, original: final */
    public final void handleLowMemory() {
        ArrayList<ComponentCallbacks2> callbacks = collectComponentCallbacks(true, null);
        int N = callbacks.size();
        for (int i = 0; i < N; i++) {
            ((ComponentCallbacks2) callbacks.get(i)).onLowMemory();
        }
        if (Process.myUid() != 1000) {
            EventLog.writeEvent((int) SQLITE_MEM_RELEASED_EVENT_LOG_TAG, SQLiteDatabase.releaseMemory());
        }
        Canvas.freeCaches();
        Canvas.freeTextLayoutCaches();
        BinderInternal.forceGc("mem");
    }

    private void handleTrimMemory(int level) {
        Trace.traceBegin(64, "trimMemory");
        ArrayList<ComponentCallbacks2> callbacks = collectComponentCallbacks(true, null);
        int N = callbacks.size();
        for (int i = 0; i < N; i++) {
            ((ComponentCallbacks2) callbacks.get(i)).onTrimMemory(level);
        }
        WindowManagerGlobal.getInstance().trimMemory(level);
        Trace.traceEnd(64);
        if (SystemProperties.getInt("debug.am.run_gc_trim_level", Integer.MAX_VALUE) <= level) {
            unscheduleGcIdler();
            doGcIfNeeded("tm");
        }
        if (SystemProperties.getInt("debug.am.run_mallopt_trim_level", Integer.MAX_VALUE) <= level) {
            unschedulePurgeIdler();
            purgePendingResources();
        }
    }

    private void setupGraphicsSupport(Context context) {
        Trace.traceBegin(64, "setupGraphicsSupport");
        if (!"android".equals(context.getPackageName())) {
            File cacheDir = context.getCacheDir();
            String str = TAG;
            if (cacheDir != null) {
                System.setProperty("java.io.tmpdir", cacheDir.getAbsolutePath());
            } else {
                Log.v(str, "Unable to initialize \"java.io.tmpdir\" property due to missing cache directory");
            }
            File codeCacheDir = context.createDeviceProtectedStorageContext().getCodeCacheDir();
            if (codeCacheDir != null) {
                try {
                    if (getPackageManager().getPackagesForUid(Process.myUid()) != null) {
                        HardwareRenderer.setupDiskCache(codeCacheDir);
                        RenderScriptCacheDir.setupDiskCache(codeCacheDir);
                    }
                } catch (RemoteException e) {
                    Trace.traceEnd(64);
                    throw e.rethrowFromSystemServer();
                }
            }
            Log.w(str, "Unable to use shader/script cache: missing code-cache directory");
        }
        GraphicsEnvironment.getInstance().setup(context, this.mCoreSettings);
        Trace.traceEnd(64);
    }

    private void updateDefaultDensity() {
        int densityDpi = this.mCurDefaultDisplayDpi;
        if (!this.mDensityCompatMode && densityDpi != 0 && densityDpi != DisplayMetrics.DENSITY_DEVICE) {
            DisplayMetrics.DENSITY_DEVICE = densityDpi;
            Bitmap.setDefaultDensity(densityDpi);
        }
    }

    private String getInstrumentationLibrary(ApplicationInfo appInfo, InstrumentationInfo insInfo) {
        if (!(appInfo.primaryCpuAbi == null || appInfo.secondaryCpuAbi == null || !appInfo.secondaryCpuAbi.equals(insInfo.secondaryCpuAbi))) {
            String secondaryIsa = VMRuntime.getInstructionSet(appInfo.secondaryCpuAbi);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ro.dalvik.vm.isa.");
            stringBuilder.append(secondaryIsa);
            String secondaryDexCodeIsa = SystemProperties.get(stringBuilder.toString());
            if (VMRuntime.getRuntime().vmInstructionSet().equals(secondaryDexCodeIsa.isEmpty() ? secondaryIsa : secondaryDexCodeIsa)) {
                return insInfo.secondaryNativeLibraryDir;
            }
        }
        return insInfo.nativeLibraryDir;
    }

    private void updateLocaleListFromAppContext(Context context, LocaleList newLocaleList) {
        Locale bestLocale = context.getResources().getConfiguration().getLocales().get(0);
        int newLocaleListSize = newLocaleList.size();
        for (int i = 0; i < newLocaleListSize; i++) {
            if (bestLocale.equals(newLocaleList.get(i))) {
                LocaleList.setDefault(newLocaleList, i);
                return;
            }
        }
        LocaleList.setDefault(new LocaleList(bestLocale, newLocaleList));
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x00a9  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00c6  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00c4  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x00e7 A:{SYNTHETIC, Splitter:B:24:0x00e7} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0108  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0115  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x016b  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0182  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0211  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x020f  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0237  */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x0313  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0253 A:{SYNTHETIC, Splitter:B:90:0x0253} */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x0343  */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x032e  */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x035d  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x034b  */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x0446  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x036d A:{SYNTHETIC, Splitter:B:122:0x036d} */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x0468  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x0460  */
    /* JADX WARNING: Removed duplicated region for block: B:151:0x049a A:{SYNTHETIC, Splitter:B:151:0x049a} */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x04e2 A:{SYNTHETIC, Splitter:B:172:0x04e2} */
    /* JADX WARNING: Removed duplicated region for block: B:184:0x0526  */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x0520  */
    @android.annotation.UnsupportedAppUsage
    private void handleBindApplication(android.app.ActivityThread.AppBindData r33) {
        /*
        r32 = this;
        r8 = r32;
        r9 = r33;
        r10 = android.os.SystemClock.uptimeMillis();
        r1 = 0;
        dalvik.system.VMRuntime.registerSensitiveThread();
        r0 = "debug.allocTracker.stackDepth";
        r12 = android.os.SystemProperties.get(r0);
        r0 = r12.length();
        if (r0 == 0) goto L_0x001f;
    L_0x0018:
        r0 = java.lang.Integer.parseInt(r12);
        dalvik.system.VMDebug.setAllocTrackerStackDepth(r0);
    L_0x001f:
        r0 = r9.trackAllocation;
        r13 = 1;
        if (r0 == 0) goto L_0x0027;
    L_0x0024:
        org.apache.harmony.dalvik.ddmc.DdmVmInternal.enableRecentAllocations(r13);
    L_0x0027:
        r2 = android.os.SystemClock.elapsedRealtime();
        r4 = android.os.SystemClock.uptimeMillis();
        android.os.Process.setStartTimes(r2, r4);
        r8.mBoundApplication = r9;
        r0 = new android.content.res.Configuration;
        r2 = r9.config;
        r0.<init>(r2);
        r8.mConfiguration = r0;
        r0 = new android.content.res.Configuration;
        r2 = r9.config;
        r0.<init>(r2);
        r8.mCompatConfiguration = r0;
        r0 = new android.app.ActivityThread$Profiler;
        r0.<init>();
        r8.mProfiler = r0;
        r0 = 0;
        r2 = r9.initProfilerInfo;
        if (r2 == 0) goto L_0x0086;
    L_0x0052:
        r2 = r8.mProfiler;
        r3 = r9.initProfilerInfo;
        r3 = r3.profileFile;
        r2.profileFile = r3;
        r2 = r8.mProfiler;
        r3 = r9.initProfilerInfo;
        r3 = r3.profileFd;
        r2.profileFd = r3;
        r2 = r8.mProfiler;
        r3 = r9.initProfilerInfo;
        r3 = r3.samplingInterval;
        r2.samplingInterval = r3;
        r2 = r8.mProfiler;
        r3 = r9.initProfilerInfo;
        r3 = r3.autoStopProfiler;
        r2.autoStopProfiler = r3;
        r2 = r8.mProfiler;
        r3 = r9.initProfilerInfo;
        r3 = r3.streamingOutput;
        r2.streamingOutput = r3;
        r2 = r9.initProfilerInfo;
        r2 = r2.attachAgentDuringBind;
        if (r2 == 0) goto L_0x0086;
    L_0x0080:
        r2 = r9.initProfilerInfo;
        r0 = r2.agent;
        r14 = r0;
        goto L_0x0087;
    L_0x0086:
        r14 = r0;
    L_0x0087:
        r0 = r9.processName;
        android.os.Process.setArgV0(r0);
        r0 = r9.processName;
        r2 = android.os.UserHandle.myUserId();
        android.ddm.DdmHandleAppName.setAppName(r0, r2);
        r0 = r9.appInfo;
        r0 = r0.packageName;
        dalvik.system.VMRuntime.setProcessPackageName(r0);
        r0 = r9.appInfo;
        r0 = r0.dataDir;
        dalvik.system.VMRuntime.setProcessDataDirectory(r0);
        r0 = r8.mProfiler;
        r0 = r0.profileFd;
        if (r0 == 0) goto L_0x00ae;
    L_0x00a9:
        r0 = r8.mProfiler;
        r0.startProfiling();
    L_0x00ae:
        r0 = r9.appInfo;
        r0 = r0.targetSdkVersion;
        r2 = 12;
        if (r0 > r2) goto L_0x00bb;
    L_0x00b6:
        r0 = android.os.AsyncTask.THREAD_POOL_EXECUTOR;
        android.os.AsyncTask.setDefaultExecutor(r0);
    L_0x00bb:
        r0 = r9.appInfo;
        r0 = r0.targetSdkVersion;
        r2 = 29;
        r15 = 0;
        if (r0 < r2) goto L_0x00c6;
    L_0x00c4:
        r0 = r13;
        goto L_0x00c7;
    L_0x00c6:
        r0 = r15;
    L_0x00c7:
        android.util.UtilConfig.setThrowExceptionForUpperArrayOutOfBounds(r0);
        r0 = r9.appInfo;
        r0 = r0.targetSdkVersion;
        android.os.Message.updateCheckRecycle(r0);
        r0 = r9.appInfo;
        r0 = r0.targetSdkVersion;
        android.graphics.ImageDecoder.sApiLevel = r0;
        r7 = 0;
        java.util.TimeZone.setDefault(r7);
        r0 = r9.config;
        r0 = r0.getLocales();
        android.os.LocaleList.setDefault(r0);
        r2 = r8.mResourcesManager;
        monitor-enter(r2);
        r0 = r8.mResourcesManager;	 Catch:{ all -> 0x05f2 }
        r3 = r9.config;	 Catch:{ all -> 0x05f2 }
        r4 = r9.compatInfo;	 Catch:{ all -> 0x05f2 }
        r0.applyConfigurationToResourcesLocked(r3, r4);	 Catch:{ all -> 0x05f2 }
        r0 = r9.config;	 Catch:{ all -> 0x05f2 }
        r0 = r0.densityDpi;	 Catch:{ all -> 0x05f2 }
        r8.mCurDefaultDisplayDpi = r0;	 Catch:{ all -> 0x05f2 }
        r0 = r8.mCurDefaultDisplayDpi;	 Catch:{ all -> 0x05f2 }
        r8.applyCompatConfiguration(r0);	 Catch:{ all -> 0x05f2 }
        monitor-exit(r2);	 Catch:{ all -> 0x05f2 }
        r0 = r9.appInfo;
        r2 = r9.compatInfo;
        r0 = r8.getPackageInfoNoCheck(r0, r2);
        r9.info = r0;
        if (r14 == 0) goto L_0x010d;
    L_0x0108:
        r0 = r9.info;
        handleAttachAgent(r14, r0);
    L_0x010d:
        r0 = r9.appInfo;
        r0 = r0.flags;
        r0 = r0 & 8192;
        if (r0 != 0) goto L_0x011d;
    L_0x0115:
        r8.mDensityCompatMode = r13;
        r0 = 160; // 0xa0 float:2.24E-43 double:7.9E-322;
        android.graphics.Bitmap.setDefaultDensity(r0);
        goto L_0x014b;
    L_0x011d:
        r0 = r9.appInfo;
        r0 = r0.getOverrideDensity();
        if (r0 == 0) goto L_0x014b;
    L_0x0125:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "override app density from ";
        r2.append(r3);
        r3 = android.util.DisplayMetrics.DENSITY_DEVICE;
        r2.append(r3);
        r3 = " to ";
        r2.append(r3);
        r2.append(r0);
        r2 = r2.toString();
        r3 = "ActivityThread";
        android.util.Log.d(r3, r2);
        r8.mDensityCompatMode = r13;
        android.graphics.Bitmap.setDefaultDensity(r0);
    L_0x014b:
        r32.updateDefaultDensity();
        r0 = r8.mCoreSettings;
        r2 = "time_12_24";
        r6 = r0.getString(r2);
        r0 = 0;
        if (r6 == 0) goto L_0x016b;
    L_0x015a:
        r2 = "24";
        r2 = r2.equals(r6);
        if (r2 == 0) goto L_0x0165;
    L_0x0162:
        r2 = java.lang.Boolean.TRUE;
        goto L_0x0167;
    L_0x0165:
        r2 = java.lang.Boolean.FALSE;
    L_0x0167:
        r0 = r2;
        r16 = r0;
        goto L_0x016d;
    L_0x016b:
        r16 = r0;
    L_0x016d:
        java.text.DateFormat.set24HourTimePref(r16);
        r32.updateDebugViewAttributeState();
        r0 = r9.appInfo;
        android.os.StrictMode.initThreadDefaults(r0);
        r0 = r9.appInfo;
        android.os.StrictMode.initVmDefaults(r0);
        r0 = r9.debugMode;
        r2 = 2;
        if (r0 == 0) goto L_0x01ed;
    L_0x0182:
        r0 = 8100; // 0x1fa4 float:1.135E-41 double:4.002E-320;
        android.os.Debug.changeDebugPort(r0);
        r0 = r9.debugMode;
        if (r0 != r2) goto L_0x01cc;
    L_0x018b:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r3 = "Application ";
        r0.append(r3);
        r3 = r9.info;
        r3 = r3.getPackageName();
        r0.append(r3);
        r3 = " is waiting for the debugger on port 8100...";
        r0.append(r3);
        r0 = r0.toString();
        r3 = "ActivityThread";
        android.util.Slog.w(r3, r0);
        r3 = android.app.ActivityManager.getService();
        r0 = r8.mAppThread;	 Catch:{ RemoteException -> 0x01c6 }
        r3.showWaitingForDebugger(r0, r13);	 Catch:{ RemoteException -> 0x01c6 }
        android.os.Debug.waitForDebugger();
        r0 = r8.mAppThread;	 Catch:{ RemoteException -> 0x01c0 }
        r3.showWaitingForDebugger(r0, r15);	 Catch:{ RemoteException -> 0x01c0 }
        goto L_0x01ed;
    L_0x01c0:
        r0 = move-exception;
        r2 = r0.rethrowFromSystemServer();
        throw r2;
    L_0x01c6:
        r0 = move-exception;
        r2 = r0.rethrowFromSystemServer();
        throw r2;
    L_0x01cc:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r3 = "Application ";
        r0.append(r3);
        r3 = r9.info;
        r3 = r3.getPackageName();
        r0.append(r3);
        r3 = " can be debugged on port 8100...";
        r0.append(r3);
        r0 = r0.toString();
        r3 = "ActivityThread";
        android.util.Slog.w(r3, r0);
    L_0x01ed:
        r0 = r9.appInfo;
        r17 = r0.isProfileableByShell();
        android.os.Trace.setAppTracingAllowed(r17);
        if (r17 == 0) goto L_0x01ff;
    L_0x01f8:
        r0 = r9.enableBinderTracking;
        if (r0 == 0) goto L_0x01ff;
    L_0x01fc:
        android.os.Binder.enableTracing();
    L_0x01ff:
        if (r17 != 0) goto L_0x0205;
    L_0x0201:
        r0 = android.os.Build.IS_DEBUGGABLE;
        if (r0 == 0) goto L_0x0208;
    L_0x0205:
        r32.nInitZygoteChildHeapProfiling();
    L_0x0208:
        r0 = r9.appInfo;
        r0 = r0.flags;
        r0 = r0 & r2;
        if (r0 == 0) goto L_0x0211;
    L_0x020f:
        r0 = r13;
        goto L_0x0212;
    L_0x0211:
        r0 = r15;
    L_0x0212:
        r18 = r0;
        if (r18 != 0) goto L_0x021d;
    L_0x0216:
        r0 = android.os.Build.IS_DEBUGGABLE;
        if (r0 == 0) goto L_0x021b;
    L_0x021a:
        goto L_0x021d;
    L_0x021b:
        r0 = r15;
        goto L_0x021e;
    L_0x021d:
        r0 = r13;
    L_0x021e:
        android.graphics.HardwareRenderer.setDebuggingEnabled(r0);
        r0 = r9.appInfo;
        r0 = r0.packageName;
        android.graphics.HardwareRenderer.setPackageName(r0);
        r2 = 64;
        r0 = "Setup proxies";
        android.os.Trace.traceBegin(r2, r0);
        r0 = "connectivity";
        r19 = android.os.ServiceManager.getService(r0);
        if (r19 == 0) goto L_0x024c;
    L_0x0237:
        r4 = android.net.IConnectivityManager.Stub.asInterface(r19);
        r0 = r4.getProxyForNetwork(r7);	 Catch:{ RemoteException -> 0x0243 }
        android.net.Proxy.setHttpProxySystemProperty(r0);	 Catch:{ RemoteException -> 0x0243 }
        goto L_0x024c;
    L_0x0243:
        r0 = move-exception;
        android.os.Trace.traceEnd(r2);
        r2 = r0.rethrowFromSystemServer();
        throw r2;
    L_0x024c:
        android.os.Trace.traceEnd(r2);
        r0 = r9.instrumentationName;
        if (r0 == 0) goto L_0x0313;
    L_0x0253:
        r0 = new android.app.ApplicationPackageManager;	 Catch:{ NameNotFoundException -> 0x02f9 }
        r4 = getPackageManager();	 Catch:{ NameNotFoundException -> 0x02f9 }
        r0.<init>(r7, r4);	 Catch:{ NameNotFoundException -> 0x02f9 }
        r4 = r9.instrumentationName;	 Catch:{ NameNotFoundException -> 0x02f9 }
        r0 = r0.getInstrumentationInfo(r4, r15);	 Catch:{ NameNotFoundException -> 0x02f9 }
        r4 = r9.appInfo;
        r4 = r4.primaryCpuAbi;
        r5 = r0.primaryCpuAbi;
        r4 = java.util.Objects.equals(r4, r5);
        if (r4 == 0) goto L_0x027b;
    L_0x026f:
        r4 = r9.appInfo;
        r4 = r4.secondaryCpuAbi;
        r5 = r0.secondaryCpuAbi;
        r4 = java.util.Objects.equals(r4, r5);
        if (r4 != 0) goto L_0x02cb;
    L_0x027b:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Package uses different ABI(s) than its instrumentation: package[";
        r4.append(r5);
        r5 = r9.appInfo;
        r5 = r5.packageName;
        r4.append(r5);
        r5 = "]: ";
        r4.append(r5);
        r5 = r9.appInfo;
        r5 = r5.primaryCpuAbi;
        r4.append(r5);
        r5 = ", ";
        r4.append(r5);
        r5 = r9.appInfo;
        r5 = r5.secondaryCpuAbi;
        r4.append(r5);
        r5 = " instrumentation[";
        r4.append(r5);
        r5 = r0.packageName;
        r4.append(r5);
        r5 = "]: ";
        r4.append(r5);
        r5 = r0.primaryCpuAbi;
        r4.append(r5);
        r5 = ", ";
        r4.append(r5);
        r5 = r0.secondaryCpuAbi;
        r4.append(r5);
        r4 = r4.toString();
        r5 = "ActivityThread";
        android.util.Slog.w(r5, r4);
    L_0x02cb:
        r4 = r0.packageName;
        r8.mInstrumentationPackageName = r4;
        r4 = r0.sourceDir;
        r8.mInstrumentationAppDir = r4;
        r4 = r0.splitSourceDirs;
        r8.mInstrumentationSplitAppDirs = r4;
        r4 = r9.appInfo;
        r4 = r8.getInstrumentationLibrary(r4, r0);
        r8.mInstrumentationLibDir = r4;
        r4 = r9.info;
        r4 = r4.getAppDir();
        r8.mInstrumentedAppDir = r4;
        r4 = r9.info;
        r4 = r4.getSplitAppDirs();
        r8.mInstrumentedSplitAppDirs = r4;
        r4 = r9.info;
        r4 = r4.getLibDir();
        r8.mInstrumentedLibDir = r4;
        r5 = r0;
        goto L_0x0315;
    L_0x02f9:
        r0 = move-exception;
        r2 = new java.lang.RuntimeException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Unable to find instrumentation info for: ";
        r3.append(r4);
        r4 = r9.instrumentationName;
        r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
    L_0x0313:
        r0 = 0;
        r5 = r0;
    L_0x0315:
        r0 = r9.info;
        r4 = android.app.ContextImpl.createAppContext(r8, r0);
        r0 = r8.mResourcesManager;
        r0 = r0.getConfiguration();
        r0 = r0.getLocales();
        r8.updateLocaleListFromAppContext(r4, r0);
        r0 = android.os.Process.isIsolated();
        if (r0 != 0) goto L_0x0343;
    L_0x032e:
        r20 = android.os.StrictMode.allowThreadDiskWritesMask();
        r0 = new android.util.BoostFramework;	 Catch:{ all -> 0x033e }
        r0.<init>(r4);	 Catch:{ all -> 0x033e }
        r1 = r0;
        android.os.StrictMode.setThreadPolicyMask(r20);
        r20 = r1;
        goto L_0x0345;
    L_0x033e:
        r0 = move-exception;
        android.os.StrictMode.setThreadPolicyMask(r20);
        throw r0;
    L_0x0343:
        r20 = r1;
    L_0x0345:
        r0 = android.os.Process.isIsolated();
        if (r0 != 0) goto L_0x035d;
    L_0x034b:
        r1 = android.os.StrictMode.allowThreadDiskWritesMask();
        r8.setupGraphicsSupport(r4);	 Catch:{ all -> 0x0357 }
        android.os.StrictMode.setThreadPolicyMask(r1);
        goto L_0x0360;
    L_0x0357:
        r0 = move-exception;
        r2 = r0;
        android.os.StrictMode.setThreadPolicyMask(r1);
        throw r2;
    L_0x035d:
        android.graphics.HardwareRenderer.setIsolatedProcess(r13);
    L_0x0360:
        r0 = "NetworkSecurityConfigProvider.install";
        android.os.Trace.traceBegin(r2, r0);
        android.security.net.config.NetworkSecurityConfigProvider.install(r4);
        android.os.Trace.traceEnd(r2);
        if (r5 == 0) goto L_0x0446;
    L_0x036d:
        r0 = getPackageManager();	 Catch:{ RemoteException -> 0x037c }
        r1 = r5.packageName;	 Catch:{ RemoteException -> 0x037c }
        r2 = android.os.UserHandle.myUserId();	 Catch:{ RemoteException -> 0x037c }
        r0 = r0.getApplicationInfo(r1, r15, r2);	 Catch:{ RemoteException -> 0x037c }
        goto L_0x037e;
    L_0x037c:
        r0 = move-exception;
        r0 = 0;
    L_0x037e:
        if (r0 != 0) goto L_0x0388;
    L_0x0380:
        r1 = new android.content.pm.ApplicationInfo;
        r1.<init>();
        r0 = r1;
        r3 = r0;
        goto L_0x0389;
    L_0x0388:
        r3 = r0;
    L_0x0389:
        r5.copyTo(r3);
        r0 = android.os.UserHandle.myUserId();
        r3.initForUser(r0);
        r0 = r9.compatInfo;
        r21 = r4.getClassLoader();
        r22 = 0;
        r23 = 1;
        r24 = 0;
        r1 = r32;
        r2 = r3;
        r25 = r3;
        r3 = r0;
        r27 = r4;
        r4 = r21;
        r15 = r5;
        r5 = r22;
        r28 = r6;
        r6 = r23;
        r7 = r24;
        r7 = r1.getPackageInfo(r2, r3, r4, r5, r6, r7);
        r0 = r27.getOpPackageName();
        r22 = android.app.ContextImpl.createAppContext(r8, r7, r0);
        r0 = r22.getClassLoader();	 Catch:{ Exception -> 0x041e }
        r1 = r9.instrumentationName;	 Catch:{ Exception -> 0x041e }
        r1 = r1.getClassName();	 Catch:{ Exception -> 0x041e }
        r1 = r0.loadClass(r1);	 Catch:{ Exception -> 0x041e }
        r1 = r1.newInstance();	 Catch:{ Exception -> 0x041e }
        r1 = (android.app.Instrumentation) r1;	 Catch:{ Exception -> 0x041e }
        r8.mInstrumentation = r1;	 Catch:{ Exception -> 0x041e }
        r5 = new android.content.ComponentName;
        r0 = r15.packageName;
        r1 = r15.name;
        r5.<init>(r0, r1);
        r1 = r8.mInstrumentation;
        r6 = r9.instrumentationWatcher;
        r0 = r9.instrumentationUiAutomationConnection;
        r2 = r32;
        r3 = r22;
        r4 = r27;
        r23 = r7;
        r7 = r0;
        r1.init(r2, r3, r4, r5, r6, r7);
        r0 = r8.mProfiler;
        r0 = r0.profileFile;
        if (r0 == 0) goto L_0x041d;
    L_0x03f7:
        r0 = r15.handleProfiling;
        if (r0 != 0) goto L_0x041d;
    L_0x03fb:
        r0 = r8.mProfiler;
        r0 = r0.profileFd;
        if (r0 != 0) goto L_0x041d;
    L_0x0401:
        r0 = r8.mProfiler;
        r0.handlingProfiling = r13;
        r1 = new java.io.File;
        r0 = r0.profileFile;
        r1.<init>(r0);
        r0 = r1;
        r1 = r0.getParentFile();
        r1.mkdirs();
        r1 = r0.toString();
        r2 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
        android.os.Debug.startMethodTracing(r1, r2);
    L_0x041d:
        goto L_0x0457;
    L_0x041e:
        r0 = move-exception;
        r23 = r7;
        r1 = new java.lang.RuntimeException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Unable to instantiate instrumentation ";
        r2.append(r3);
        r3 = r9.instrumentationName;
        r2.append(r3);
        r3 = ": ";
        r2.append(r3);
        r3 = r0.toString();
        r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2, r0);
        throw r1;
    L_0x0446:
        r27 = r4;
        r15 = r5;
        r28 = r6;
        r0 = new android.app.Instrumentation;
        r0.<init>();
        r8.mInstrumentation = r0;
        r0 = r8.mInstrumentation;
        r0.basicInit(r8);
    L_0x0457:
        r0 = r9.appInfo;
        r0 = r0.flags;
        r1 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r0 = r0 & r1;
        if (r0 == 0) goto L_0x0468;
    L_0x0460:
        r0 = dalvik.system.VMRuntime.getRuntime();
        r0.clearGrowthLimit();
        goto L_0x046f;
    L_0x0468:
        r0 = dalvik.system.VMRuntime.getRuntime();
        r0.clampGrowthLimit();
    L_0x046f:
        r0 = r9.appInfo;
        r1 = r27;
        android.app.ActivityThreadInjector.bindApplicationInjector(r1, r0);
        r2 = android.os.StrictMode.allowThreadDiskWrites();
        r3 = android.os.StrictMode.getThreadPolicy();
        r4 = 27;
        r0 = r9.info;	 Catch:{ all -> 0x05d9 }
        r5 = r9.restrictedBackupMode;	 Catch:{ all -> 0x05d9 }
        r6 = 0;
        r0 = r0.makeApplication(r5, r6);	 Catch:{ all -> 0x05d9 }
        r5 = r0;
        r0 = r9.autofillOptions;	 Catch:{ all -> 0x05d9 }
        r5.setAutofillOptions(r0);	 Catch:{ all -> 0x05d9 }
        r0 = r9.contentCaptureOptions;	 Catch:{ all -> 0x05d9 }
        r5.setContentCaptureOptions(r0);	 Catch:{ all -> 0x05d9 }
        r8.mInitialApplication = r5;	 Catch:{ all -> 0x05d9 }
        r0 = r9.restrictedBackupMode;	 Catch:{ all -> 0x05d9 }
        if (r0 != 0) goto L_0x04af;
    L_0x049a:
        r0 = r9.providers;	 Catch:{ all -> 0x04a8 }
        r0 = com.android.internal.util.ArrayUtils.isEmpty(r0);	 Catch:{ all -> 0x04a8 }
        if (r0 != 0) goto L_0x04af;
    L_0x04a2:
        r0 = r9.providers;	 Catch:{ all -> 0x04a8 }
        r8.installContentProviders(r5, r0);	 Catch:{ all -> 0x04a8 }
        goto L_0x04af;
    L_0x04a8:
        r0 = move-exception;
        r29 = r1;
        r27 = r12;
        goto L_0x05de;
    L_0x04af:
        r0 = r8.mInstrumentation;	 Catch:{ Exception -> 0x05ad }
        r6 = r9.instrumentationArgs;	 Catch:{ Exception -> 0x05ad }
        r0.onCreate(r6);	 Catch:{ Exception -> 0x05ad }
        r0 = r8.mInstrumentation;	 Catch:{ Exception -> 0x04bd }
        r0.callApplicationOnCreate(r5);	 Catch:{ Exception -> 0x04bd }
        goto L_0x04c6;
    L_0x04bd:
        r0 = move-exception;
        r6 = r8.mInstrumentation;	 Catch:{ all -> 0x05d9 }
        r6 = r6.onException(r5, r0);	 Catch:{ all -> 0x05d9 }
        if (r6 == 0) goto L_0x057e;
    L_0x04c6:
        r0 = r9.appInfo;
        r0 = r0.targetSdkVersion;
        if (r0 < r4) goto L_0x04d6;
    L_0x04cc:
        r0 = android.os.StrictMode.getThreadPolicy();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x04d9;
    L_0x04d6:
        android.os.StrictMode.setThreadPolicy(r2);
    L_0x04d9:
        android.provider.FontsContract.setApplicationContextForResources(r1);
        r0 = android.os.Process.isIsolated();
        if (r0 != 0) goto L_0x0514;
    L_0x04e2:
        r0 = getPackageManager();	 Catch:{ RemoteException -> 0x050e }
        r4 = r9.appInfo;	 Catch:{ RemoteException -> 0x050e }
        r4 = r4.packageName;	 Catch:{ RemoteException -> 0x050e }
        r6 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r7 = android.os.UserHandle.myUserId();	 Catch:{ RemoteException -> 0x050e }
        r0 = r0.getApplicationInfo(r4, r6, r7);	 Catch:{ RemoteException -> 0x050e }
        r4 = r0.metaData;	 Catch:{ RemoteException -> 0x050e }
        if (r4 == 0) goto L_0x050d;
    L_0x04f8:
        r4 = r0.metaData;	 Catch:{ RemoteException -> 0x050e }
        r6 = "preloaded_fonts";
        r7 = 0;
        r4 = r4.getInt(r6, r7);	 Catch:{ RemoteException -> 0x050e }
        if (r4 == 0) goto L_0x050d;
    L_0x0504:
        r6 = r9.info;	 Catch:{ RemoteException -> 0x050e }
        r6 = r6.getResources();	 Catch:{ RemoteException -> 0x050e }
        r6.preloadFonts(r4);	 Catch:{ RemoteException -> 0x050e }
    L_0x050d:
        goto L_0x0514;
    L_0x050e:
        r0 = move-exception;
        r4 = r0.rethrowFromSystemServer();
        throw r4;
    L_0x0514:
        r6 = android.os.SystemClock.uptimeMillis();
        r27 = r12;
        r12 = r6 - r10;
        r4 = (int) r12;
        r0 = 0;
        if (r1 == 0) goto L_0x0526;
    L_0x0520:
        r0 = r1.getPackageName();
        r12 = r0;
        goto L_0x0527;
    L_0x0526:
        r12 = r0;
    L_0x0527:
        if (r20 == 0) goto L_0x0579;
    L_0x0529:
        r0 = android.os.Process.isIsolated();
        if (r0 != 0) goto L_0x0579;
    L_0x052f:
        if (r12 == 0) goto L_0x0579;
    L_0x0531:
        r13 = 0;
        r0 = r1.getPackageCodePath();	 Catch:{ Exception -> 0x054d }
        r29 = r1;
        r1 = 47;
        r1 = r0.lastIndexOf(r1);	 Catch:{ Exception -> 0x0549 }
        r30 = r6;
        r6 = 0;
        r1 = r0.substring(r6, r1);	 Catch:{ Exception -> 0x0547 }
        r0 = r1;
        goto L_0x0569;
    L_0x0547:
        r0 = move-exception;
        goto L_0x0552;
    L_0x0549:
        r0 = move-exception;
        r30 = r6;
        goto L_0x0552;
    L_0x054d:
        r0 = move-exception;
        r29 = r1;
        r30 = r6;
    L_0x0552:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r6 = "HeavyGameThread () : Exception_1 = ";
        r1.append(r6);
        r1.append(r0);
        r1 = r1.toString();
        r6 = "ActivityThread";
        android.util.Slog.e(r6, r1);
        r0 = r13;
    L_0x0569:
        r22 = 2;
        r23 = 0;
        r21 = r20;
        r24 = r12;
        r25 = r4;
        r26 = r0;
        r21.perfUXEngine_events(r22, r23, r24, r25, r26);
        goto L_0x057d;
    L_0x0579:
        r29 = r1;
        r30 = r6;
    L_0x057d:
        return;
    L_0x057e:
        r29 = r1;
        r27 = r12;
        r1 = new java.lang.RuntimeException;	 Catch:{ all -> 0x05d7 }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x05d7 }
        r6.<init>();	 Catch:{ all -> 0x05d7 }
        r7 = "Unable to create application ";
        r6.append(r7);	 Catch:{ all -> 0x05d7 }
        r7 = r5.getClass();	 Catch:{ all -> 0x05d7 }
        r7 = r7.getName();	 Catch:{ all -> 0x05d7 }
        r6.append(r7);	 Catch:{ all -> 0x05d7 }
        r7 = ": ";
        r6.append(r7);	 Catch:{ all -> 0x05d7 }
        r7 = r0.toString();	 Catch:{ all -> 0x05d7 }
        r6.append(r7);	 Catch:{ all -> 0x05d7 }
        r6 = r6.toString();	 Catch:{ all -> 0x05d7 }
        r1.<init>(r6, r0);	 Catch:{ all -> 0x05d7 }
        throw r1;	 Catch:{ all -> 0x05d7 }
    L_0x05ad:
        r0 = move-exception;
        r29 = r1;
        r27 = r12;
        r1 = new java.lang.RuntimeException;	 Catch:{ all -> 0x05d7 }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x05d7 }
        r6.<init>();	 Catch:{ all -> 0x05d7 }
        r7 = "Exception thrown in onCreate() of ";
        r6.append(r7);	 Catch:{ all -> 0x05d7 }
        r7 = r9.instrumentationName;	 Catch:{ all -> 0x05d7 }
        r6.append(r7);	 Catch:{ all -> 0x05d7 }
        r7 = ": ";
        r6.append(r7);	 Catch:{ all -> 0x05d7 }
        r7 = r0.toString();	 Catch:{ all -> 0x05d7 }
        r6.append(r7);	 Catch:{ all -> 0x05d7 }
        r6 = r6.toString();	 Catch:{ all -> 0x05d7 }
        r1.<init>(r6, r0);	 Catch:{ all -> 0x05d7 }
        throw r1;	 Catch:{ all -> 0x05d7 }
    L_0x05d7:
        r0 = move-exception;
        goto L_0x05de;
    L_0x05d9:
        r0 = move-exception;
        r29 = r1;
        r27 = r12;
    L_0x05de:
        r1 = r9.appInfo;
        r1 = r1.targetSdkVersion;
        if (r1 < r4) goto L_0x05ee;
    L_0x05e4:
        r1 = android.os.StrictMode.getThreadPolicy();
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x05f1;
    L_0x05ee:
        android.os.StrictMode.setThreadPolicy(r2);
    L_0x05f1:
        throw r0;
    L_0x05f2:
        r0 = move-exception;
        r27 = r12;
    L_0x05f5:
        monitor-exit(r2);	 Catch:{ all -> 0x05f7 }
        throw r0;
    L_0x05f7:
        r0 = move-exception;
        goto L_0x05f5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.handleBindApplication(android.app.ActivityThread$AppBindData):void");
    }

    /* Access modifiers changed, original: final */
    public final void finishInstrumentation(int resultCode, Bundle results) {
        IActivityManager am = ActivityManager.getService();
        if (this.mProfiler.profileFile != null && this.mProfiler.handlingProfiling && this.mProfiler.profileFd == null) {
            Debug.stopMethodTracing();
        }
        try {
            am.finishInstrumentation(this.mAppThread, resultCode, results);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    private void installContentProviders(Context context, List<ProviderInfo> providers) {
        ArrayList<ContentProviderHolder> results = new ArrayList();
        for (ProviderInfo cpi : providers) {
            ContentProviderHolder cph = installProvider(context, null, cpi, false, true, true);
            if (cph != null) {
                cph.noReleaseNeeded = true;
                results.add(cph);
            }
        }
        try {
            ActivityManager.getService().publishContentProviders(getApplicationThread(), results);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:72:0x00e7  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x00cf  */
    /* JADX WARNING: Missing block: B:13:0x003d, code skipped:
            r0 = r1;
     */
    /* JADX WARNING: Missing block: B:44:0x00a8, code skipped:
            r7 = r1;
     */
    @android.annotation.UnsupportedAppUsage
    public final android.content.IContentProvider acquireProvider(android.content.Context r18, java.lang.String r19, int r20, boolean r21) {
        /*
        r17 = this;
        r8 = r17;
        r9 = r19;
        r10 = r20;
        r0 = new android.app.ActivityThread$ProviderKey;
        r0.<init>(r9, r10);
        r11 = r0;
        r12 = r18;
        r13 = r21;
        r14 = r8.acquireExistingProvider(r12, r11, r13);
        if (r14 == 0) goto L_0x0017;
    L_0x0016:
        return r14;
    L_0x0017:
        r7 = 0;
        r0 = android.os.Process.myTid();
        r1 = r8.mainThreadId;
        if (r0 != r1) goto L_0x004c;
    L_0x0020:
        r15 = r8.getGetProviderLock(r9, r10);	 Catch:{ RemoteException -> 0x0046 }
        monitor-enter(r15);	 Catch:{ RemoteException -> 0x0046 }
        r1 = android.app.ActivityManager.getService();	 Catch:{ all -> 0x0043 }
        r2 = r17.getApplicationThread();	 Catch:{ all -> 0x0043 }
        r3 = r18.getOpPackageName();	 Catch:{ all -> 0x0043 }
        r4 = r19;
        r5 = r20;
        r6 = r21;
        r0 = r1.getContentProvider(r2, r3, r4, r5, r6);	 Catch:{ all -> 0x0043 }
        r1 = r0;
        monitor-exit(r15);	 Catch:{ all -> 0x0040 }
        r0 = r1;
        goto L_0x00cd;
    L_0x0040:
        r0 = move-exception;
        r7 = r1;
        goto L_0x0044;
    L_0x0043:
        r0 = move-exception;
    L_0x0044:
        monitor-exit(r15);	 Catch:{ all -> 0x0043 }
        throw r0;	 Catch:{ RemoteException -> 0x0046 }
    L_0x0046:
        r0 = move-exception;
        r1 = r0.rethrowFromSystemServer();
        throw r1;
    L_0x004c:
        r1 = r8.mProviderAcquiringCountMap;
        monitor-enter(r1);
        r0 = r8.mProviderAcquiringCountMap;	 Catch:{ all -> 0x0101 }
        r0 = r0.get(r11);	 Catch:{ all -> 0x0101 }
        r0 = (android.app.ActivityThread.ProviderAcquiringCount) r0;	 Catch:{ all -> 0x0101 }
        r15 = 1;
        if (r0 != 0) goto L_0x0067;
    L_0x005a:
        r2 = new android.app.ActivityThread$ProviderAcquiringCount;	 Catch:{ all -> 0x0101 }
        r2.<init>(r15);	 Catch:{ all -> 0x0101 }
        r0 = r2;
        r2 = r8.mProviderAcquiringCountMap;	 Catch:{ all -> 0x0101 }
        r2.put(r11, r0);	 Catch:{ all -> 0x0101 }
        r6 = r0;
        goto L_0x0088;
    L_0x0067:
        r2 = r0.acquiringCount;	 Catch:{ all -> 0x0101 }
        r2 = r2 + r15;
        r0.acquiringCount = r2;	 Catch:{ all -> 0x0101 }
        r2 = "ActivityThread";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0101 }
        r3.<init>();	 Catch:{ all -> 0x0101 }
        r3.append(r9);	 Catch:{ all -> 0x0101 }
        r4 = " acquiringCount ";
        r3.append(r4);	 Catch:{ all -> 0x0101 }
        r4 = r0.acquiringCount;	 Catch:{ all -> 0x0101 }
        r3.append(r4);	 Catch:{ all -> 0x0101 }
        r3 = r3.toString();	 Catch:{ all -> 0x0101 }
        android.util.Log.i(r2, r3);	 Catch:{ all -> 0x0101 }
        r6 = r0;
    L_0x0088:
        monitor-exit(r1);	 Catch:{ all -> 0x0101 }
        monitor-enter(r6);
        r16 = r8.getGetProviderLock(r9, r10);	 Catch:{ RemoteException -> 0x00b8, all -> 0x00b5 }
        monitor-enter(r16);	 Catch:{ RemoteException -> 0x00b8, all -> 0x00b5 }
        r1 = android.app.ActivityManager.getService();	 Catch:{ all -> 0x00ad }
        r2 = r17.getApplicationThread();	 Catch:{ all -> 0x00ad }
        r3 = r18.getOpPackageName();	 Catch:{ all -> 0x00ad }
        r4 = r19;
        r5 = r20;
        r15 = r6;
        r6 = r21;
        r0 = r1.getContentProvider(r2, r3, r4, r5, r6);	 Catch:{ all -> 0x00b3 }
        r1 = r0;
        monitor-exit(r16);	 Catch:{ all -> 0x00aa }
        r7 = r1;
        goto L_0x00ba;
    L_0x00aa:
        r0 = move-exception;
        r7 = r1;
        goto L_0x00af;
    L_0x00ad:
        r0 = move-exception;
        r15 = r6;
    L_0x00af:
        monitor-exit(r16);	 Catch:{ all -> 0x00b3 }
        throw r0;	 Catch:{ RemoteException -> 0x00b1 }
    L_0x00b1:
        r0 = move-exception;
        goto L_0x00ba;
    L_0x00b3:
        r0 = move-exception;
        goto L_0x00af;
    L_0x00b5:
        r0 = move-exception;
        r15 = r6;
        goto L_0x00fd;
    L_0x00b8:
        r0 = move-exception;
        r15 = r6;
    L_0x00ba:
        monitor-exit(r15);	 Catch:{ all -> 0x00ff }
        r1 = r8.mProviderAcquiringCountMap;
        monitor-enter(r1);
        r0 = r15.acquiringCount;	 Catch:{ all -> 0x00fa }
        r2 = 1;
        r0 = r0 - r2;
        r15.acquiringCount = r0;	 Catch:{ all -> 0x00fa }
        if (r0 != 0) goto L_0x00cb;
    L_0x00c6:
        r0 = r8.mProviderAcquiringCountMap;	 Catch:{ all -> 0x00fa }
        r0.remove(r11);	 Catch:{ all -> 0x00fa }
    L_0x00cb:
        monitor-exit(r1);	 Catch:{ all -> 0x00fa }
        r0 = r7;
    L_0x00cd:
        if (r0 != 0) goto L_0x00e7;
    L_0x00cf:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Failed to find provider info for ";
        r1.append(r2);
        r1.append(r9);
        r1 = r1.toString();
        r2 = "ActivityThread";
        android.util.Slog.e(r2, r1);
        r1 = 0;
        return r1;
    L_0x00e7:
        r4 = r0.info;
        r5 = 1;
        r6 = r0.noReleaseNeeded;
        r1 = r17;
        r2 = r18;
        r3 = r0;
        r7 = r21;
        r0 = r1.installProvider(r2, r3, r4, r5, r6, r7);
        r1 = r0.provider;
        return r1;
    L_0x00fa:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x00fa }
        throw r0;
    L_0x00fd:
        monitor-exit(r15);	 Catch:{ all -> 0x00ff }
        throw r0;
    L_0x00ff:
        r0 = move-exception;
        goto L_0x00fd;
    L_0x0101:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0101 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.acquireProvider(android.content.Context, java.lang.String, int, boolean):android.content.IContentProvider");
    }

    private Object getGetProviderLock(String auth, int userId) {
        Object lock;
        ProviderKey key = new ProviderKey(auth, userId);
        synchronized (this.mGetProviderLocks) {
            lock = this.mGetProviderLocks.get(key);
            if (lock == null) {
                lock = key;
                this.mGetProviderLocks.put(key, lock);
            }
        }
        return lock;
    }

    private final void incProviderRefLocked(ProviderRefCount prc, boolean stable) {
        if (stable) {
            prc.stableCount++;
            if (prc.stableCount == 1) {
                int unstableDelta;
                if (prc.removePending) {
                    unstableDelta = -1;
                    prc.removePending = false;
                    this.mH.removeMessages(131, prc);
                } else {
                    unstableDelta = 0;
                }
                try {
                    ActivityManager.getService().refContentProvider(prc.holder.connection, 1, unstableDelta);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            return;
        }
        prc.unstableCount++;
        if (prc.unstableCount != 1) {
            return;
        }
        if (prc.removePending) {
            prc.removePending = false;
            this.mH.removeMessages(131, prc);
            return;
        }
        try {
            ActivityManager.getService().refContentProvider(prc.holder.connection, 0, 1);
        } catch (RemoteException e2) {
        }
    }

    @UnsupportedAppUsage
    public final IContentProvider acquireExistingProvider(Context c, String auth, int userId, boolean stable) {
        return acquireExistingProvider(c, new ProviderKey(auth, userId), stable);
    }

    /* JADX WARNING: Missing block: B:16:0x0057, code skipped:
            return r3;
     */
    public final android.content.IContentProvider acquireExistingProvider(android.content.Context r9, android.app.ActivityThread.ProviderKey r10, boolean r11) {
        /*
        r8 = this;
        r0 = r8.mProviderMap;
        monitor-enter(r0);
        r1 = r8.mProviderMap;	 Catch:{ all -> 0x0058 }
        r1 = r1.get(r10);	 Catch:{ all -> 0x0058 }
        r1 = (android.app.ActivityThread.ProviderClientRecord) r1;	 Catch:{ all -> 0x0058 }
        r2 = 0;
        if (r1 != 0) goto L_0x0010;
    L_0x000e:
        monitor-exit(r0);	 Catch:{ all -> 0x0058 }
        return r2;
    L_0x0010:
        r3 = r1.mProvider;	 Catch:{ all -> 0x0058 }
        r4 = r3.asBinder();	 Catch:{ all -> 0x0058 }
        r5 = r4.isBinderAlive();	 Catch:{ all -> 0x0058 }
        if (r5 != 0) goto L_0x0049;
    L_0x001c:
        r5 = "ActivityThread";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0058 }
        r6.<init>();	 Catch:{ all -> 0x0058 }
        r7 = "Acquiring provider ";
        r6.append(r7);	 Catch:{ all -> 0x0058 }
        r7 = r10.authority;	 Catch:{ all -> 0x0058 }
        r6.append(r7);	 Catch:{ all -> 0x0058 }
        r7 = " for user ";
        r6.append(r7);	 Catch:{ all -> 0x0058 }
        r7 = r10.userId;	 Catch:{ all -> 0x0058 }
        r6.append(r7);	 Catch:{ all -> 0x0058 }
        r7 = ": existing object's process dead";
        r6.append(r7);	 Catch:{ all -> 0x0058 }
        r6 = r6.toString();	 Catch:{ all -> 0x0058 }
        android.util.Log.i(r5, r6);	 Catch:{ all -> 0x0058 }
        r5 = 1;
        r8.handleUnstableProviderDiedLocked(r4, r5);	 Catch:{ all -> 0x0058 }
        monitor-exit(r0);	 Catch:{ all -> 0x0058 }
        return r2;
    L_0x0049:
        r2 = r8.mProviderRefCountMap;	 Catch:{ all -> 0x0058 }
        r2 = r2.get(r4);	 Catch:{ all -> 0x0058 }
        r2 = (android.app.ActivityThread.ProviderRefCount) r2;	 Catch:{ all -> 0x0058 }
        if (r2 == 0) goto L_0x0056;
    L_0x0053:
        r8.incProviderRefLocked(r2, r11);	 Catch:{ all -> 0x0058 }
    L_0x0056:
        monitor-exit(r0);	 Catch:{ all -> 0x0058 }
        return r3;
    L_0x0058:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0058 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.acquireExistingProvider(android.content.Context, android.app.ActivityThread$ProviderKey, boolean):android.content.IContentProvider");
    }

    /* JADX WARNING: Missing block: B:52:0x00a0, code skipped:
            return true;
     */
    @android.annotation.UnsupportedAppUsage
    public final boolean releaseProvider(android.content.IContentProvider r10, boolean r11) {
        /*
        r9 = this;
        r0 = 0;
        if (r10 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r10.asBinder();
        r2 = r9.mProviderMap;
        monitor-enter(r2);
        r3 = r9.mProviderRefCountMap;	 Catch:{ all -> 0x00a1 }
        r3 = r3.get(r1);	 Catch:{ all -> 0x00a1 }
        r3 = (android.app.ActivityThread.ProviderRefCount) r3;	 Catch:{ all -> 0x00a1 }
        if (r3 != 0) goto L_0x0017;
    L_0x0015:
        monitor-exit(r2);	 Catch:{ all -> 0x00a1 }
        return r0;
    L_0x0017:
        r4 = 0;
        r5 = -1;
        r6 = 1;
        if (r11 == 0) goto L_0x0044;
    L_0x001c:
        r7 = r3.stableCount;	 Catch:{ all -> 0x00a1 }
        if (r7 != 0) goto L_0x0022;
    L_0x0020:
        monitor-exit(r2);	 Catch:{ all -> 0x00a1 }
        return r0;
    L_0x0022:
        r7 = r3.stableCount;	 Catch:{ all -> 0x00a1 }
        r7 = r7 - r6;
        r3.stableCount = r7;	 Catch:{ all -> 0x00a1 }
        r7 = r3.stableCount;	 Catch:{ all -> 0x00a1 }
        if (r7 != 0) goto L_0x006a;
    L_0x002b:
        r7 = r3.unstableCount;	 Catch:{ all -> 0x00a1 }
        if (r7 != 0) goto L_0x0031;
    L_0x002f:
        r7 = r6;
        goto L_0x0032;
    L_0x0031:
        r7 = r0;
    L_0x0032:
        r4 = r7;
        r7 = android.app.ActivityManager.getService();	 Catch:{ RemoteException -> 0x0042 }
        r8 = r3.holder;	 Catch:{ RemoteException -> 0x0042 }
        r8 = r8.connection;	 Catch:{ RemoteException -> 0x0042 }
        if (r4 == 0) goto L_0x003e;
    L_0x003d:
        r0 = r6;
    L_0x003e:
        r7.refContentProvider(r8, r5, r0);	 Catch:{ RemoteException -> 0x0042 }
        goto L_0x0043;
    L_0x0042:
        r0 = move-exception;
    L_0x0043:
        goto L_0x006a;
    L_0x0044:
        r7 = r3.unstableCount;	 Catch:{ all -> 0x00a1 }
        if (r7 != 0) goto L_0x004a;
    L_0x0048:
        monitor-exit(r2);	 Catch:{ all -> 0x00a1 }
        return r0;
    L_0x004a:
        r7 = r3.unstableCount;	 Catch:{ all -> 0x00a1 }
        r7 = r7 - r6;
        r3.unstableCount = r7;	 Catch:{ all -> 0x00a1 }
        r7 = r3.unstableCount;	 Catch:{ all -> 0x00a1 }
        if (r7 != 0) goto L_0x006a;
    L_0x0053:
        r7 = r3.stableCount;	 Catch:{ all -> 0x00a1 }
        if (r7 != 0) goto L_0x0059;
    L_0x0057:
        r7 = r6;
        goto L_0x005a;
    L_0x0059:
        r7 = r0;
    L_0x005a:
        r4 = r7;
        if (r4 != 0) goto L_0x006a;
    L_0x005d:
        r7 = android.app.ActivityManager.getService();	 Catch:{ RemoteException -> 0x0069 }
        r8 = r3.holder;	 Catch:{ RemoteException -> 0x0069 }
        r8 = r8.connection;	 Catch:{ RemoteException -> 0x0069 }
        r7.refContentProvider(r8, r0, r5);	 Catch:{ RemoteException -> 0x0069 }
        goto L_0x006a;
    L_0x0069:
        r0 = move-exception;
    L_0x006a:
        if (r4 == 0) goto L_0x009f;
    L_0x006c:
        r0 = r3.removePending;	 Catch:{ all -> 0x00a1 }
        if (r0 != 0) goto L_0x0083;
    L_0x0070:
        r3.removePending = r6;	 Catch:{ all -> 0x00a1 }
        r0 = r9.mH;	 Catch:{ all -> 0x00a1 }
        r5 = 131; // 0x83 float:1.84E-43 double:6.47E-322;
        r0 = r0.obtainMessage(r5, r3);	 Catch:{ all -> 0x00a1 }
        r5 = r9.mH;	 Catch:{ all -> 0x00a1 }
        r7 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r5.sendMessageDelayed(r0, r7);	 Catch:{ all -> 0x00a1 }
        goto L_0x009f;
    L_0x0083:
        r0 = "ActivityThread";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a1 }
        r5.<init>();	 Catch:{ all -> 0x00a1 }
        r7 = "Duplicate remove pending of provider ";
        r5.append(r7);	 Catch:{ all -> 0x00a1 }
        r7 = r3.holder;	 Catch:{ all -> 0x00a1 }
        r7 = r7.info;	 Catch:{ all -> 0x00a1 }
        r7 = r7.name;	 Catch:{ all -> 0x00a1 }
        r5.append(r7);	 Catch:{ all -> 0x00a1 }
        r5 = r5.toString();	 Catch:{ all -> 0x00a1 }
        android.util.Slog.w(r0, r5);	 Catch:{ all -> 0x00a1 }
    L_0x009f:
        monitor-exit(r2);	 Catch:{ all -> 0x00a1 }
        return r6;
    L_0x00a1:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x00a1 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.releaseProvider(android.content.IContentProvider, boolean):boolean");
    }

    /* Access modifiers changed, original: final */
    /* JADX WARNING: Missing block: B:18:?, code skipped:
            android.app.ActivityManager.getService().removeContentProvider(r9.holder.connection, false);
     */
    public final void completeRemoveProvider(android.app.ActivityThread.ProviderRefCount r9) {
        /*
        r8 = this;
        r0 = r8.mProviderMap;
        monitor-enter(r0);
        r1 = r9.removePending;	 Catch:{ all -> 0x0054 }
        if (r1 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x0054 }
        return;
    L_0x0009:
        r1 = 0;
        r9.removePending = r1;	 Catch:{ all -> 0x0054 }
        r2 = r9.holder;	 Catch:{ all -> 0x0054 }
        r2 = r2.provider;	 Catch:{ all -> 0x0054 }
        r2 = r2.asBinder();	 Catch:{ all -> 0x0054 }
        r3 = r8.mProviderRefCountMap;	 Catch:{ all -> 0x0054 }
        r3 = r3.get(r2);	 Catch:{ all -> 0x0054 }
        r3 = (android.app.ActivityThread.ProviderRefCount) r3;	 Catch:{ all -> 0x0054 }
        if (r3 != r9) goto L_0x0023;
    L_0x001e:
        r4 = r8.mProviderRefCountMap;	 Catch:{ all -> 0x0054 }
        r4.remove(r2);	 Catch:{ all -> 0x0054 }
    L_0x0023:
        r4 = r8.mProviderMap;	 Catch:{ all -> 0x0054 }
        r4 = r4.size();	 Catch:{ all -> 0x0054 }
        r4 = r4 + -1;
    L_0x002b:
        if (r4 < 0) goto L_0x0045;
    L_0x002d:
        r5 = r8.mProviderMap;	 Catch:{ all -> 0x0054 }
        r5 = r5.valueAt(r4);	 Catch:{ all -> 0x0054 }
        r5 = (android.app.ActivityThread.ProviderClientRecord) r5;	 Catch:{ all -> 0x0054 }
        r6 = r5.mProvider;	 Catch:{ all -> 0x0054 }
        r6 = r6.asBinder();	 Catch:{ all -> 0x0054 }
        if (r6 != r2) goto L_0x0042;
    L_0x003d:
        r7 = r8.mProviderMap;	 Catch:{ all -> 0x0054 }
        r7.removeAt(r4);	 Catch:{ all -> 0x0054 }
    L_0x0042:
        r4 = r4 + -1;
        goto L_0x002b;
    L_0x0045:
        monitor-exit(r0);	 Catch:{ all -> 0x0054 }
        r0 = android.app.ActivityManager.getService();	 Catch:{ RemoteException -> 0x0052 }
        r2 = r9.holder;	 Catch:{ RemoteException -> 0x0052 }
        r2 = r2.connection;	 Catch:{ RemoteException -> 0x0052 }
        r0.removeContentProvider(r2, r1);	 Catch:{ RemoteException -> 0x0052 }
        goto L_0x0053;
    L_0x0052:
        r0 = move-exception;
    L_0x0053:
        return;
    L_0x0054:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0054 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.completeRemoveProvider(android.app.ActivityThread$ProviderRefCount):void");
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final void handleUnstableProviderDied(IBinder provider, boolean fromClient) {
        synchronized (this.mProviderMap) {
            handleUnstableProviderDiedLocked(provider, fromClient);
        }
    }

    /* Access modifiers changed, original: final */
    public final void handleUnstableProviderDiedLocked(IBinder provider, boolean fromClient) {
        ProviderRefCount prc = (ProviderRefCount) this.mProviderRefCountMap.get(provider);
        if (prc != null) {
            this.mProviderRefCountMap.remove(provider);
            for (int i = this.mProviderMap.size() - 1; i >= 0; i--) {
                ProviderClientRecord pr = (ProviderClientRecord) this.mProviderMap.valueAt(i);
                if (pr != null && pr.mProvider.asBinder() == provider) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Removing dead content provider:");
                    stringBuilder.append(pr.mProvider.toString());
                    Slog.i(TAG, stringBuilder.toString());
                    this.mProviderMap.removeAt(i);
                }
            }
            if (fromClient) {
                try {
                    ActivityManager.getService().unstableProviderDied(prc.holder.connection);
                } catch (RemoteException e) {
                }
            }
        }
    }

    /* Access modifiers changed, original: final */
    public final void appNotRespondingViaProvider(IBinder provider) {
        synchronized (this.mProviderMap) {
            ProviderRefCount prc = (ProviderRefCount) this.mProviderRefCountMap.get(provider);
            if (prc != null) {
                try {
                    ActivityManager.getService().appNotRespondingViaProvider(prc.holder.connection);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    private ProviderClientRecord installProviderAuthoritiesLocked(IContentProvider provider, ContentProvider localProvider, ContentProviderHolder holder) {
        String auth;
        String[] auths = holder.info.authority.split(";");
        int userId = UserHandle.getUserId(holder.info.applicationInfo.uid);
        int i = 0;
        if (provider != null) {
            for (String auth2 : auths) {
                int i2 = -1;
                switch (auth2.hashCode()) {
                    case -845193793:
                        if (auth2.equals(ContactsContract.AUTHORITY)) {
                            i2 = 0;
                            break;
                        }
                        break;
                    case -456066902:
                        if (auth2.equals(CalendarContract.AUTHORITY)) {
                            i2 = 4;
                            break;
                        }
                        break;
                    case -172298781:
                        if (auth2.equals(CallLog.AUTHORITY)) {
                            i2 = 1;
                            break;
                        }
                        break;
                    case 63943420:
                        if (auth2.equals(CallLog.SHADOW_AUTHORITY)) {
                            i2 = 2;
                            break;
                        }
                        break;
                    case 783201304:
                        if (auth2.equals(DeviceConfig.NAMESPACE_TELEPHONY)) {
                            i2 = 6;
                            break;
                        }
                        break;
                    case 1312704747:
                        if (auth2.equals("downloads")) {
                            i2 = 5;
                            break;
                        }
                        break;
                    case 1995645513:
                        if (auth2.equals(BlockedNumberContract.AUTHORITY)) {
                            i2 = 3;
                            break;
                        }
                        break;
                }
                switch (i2) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        Binder.allowBlocking(provider.asBinder());
                        break;
                    default:
                        break;
                }
            }
        }
        ProviderClientRecord pcr = new ProviderClientRecord(auths, provider, localProvider, holder);
        int length = auths.length;
        while (i < length) {
            auth2 = auths[i];
            ProviderKey key = new ProviderKey(auth2, userId);
            if (((ProviderClientRecord) this.mProviderMap.get(key)) != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Content provider ");
                stringBuilder.append(pcr.mHolder.info.name);
                stringBuilder.append(" already published as ");
                stringBuilder.append(auth2);
                Slog.w(TAG, stringBuilder.toString());
            } else {
                this.mProviderMap.put(key, pcr);
            }
            i++;
        }
        return pcr;
    }

    @UnsupportedAppUsage
    private ContentProviderHolder installProvider(Context context, ContentProviderHolder holder, ProviderInfo info, boolean noisy, boolean noReleaseNeeded, boolean stable) {
        Context c;
        IContentProvider provider;
        ContentProvider localProvider;
        Throwable th;
        ContentProviderHolder contentProviderHolder = holder;
        ProviderInfo providerInfo = info;
        boolean z = stable;
        Context context2;
        if (contentProviderHolder == null || contentProviderHolder.provider == null) {
            StringBuilder stringBuilder;
            if (noisy) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Loading provider ");
                stringBuilder.append(providerInfo.authority);
                stringBuilder.append(": ");
                stringBuilder.append(providerInfo.name);
                Slog.d(TAG, stringBuilder.toString());
            }
            c = null;
            ApplicationInfo ai = providerInfo.applicationInfo;
            if (context.getPackageName().equals(ai.packageName)) {
                c = context;
                context2 = context;
            } else {
                Application application = this.mInitialApplication;
                if (application == null || !application.getPackageName().equals(ai.packageName)) {
                    try {
                        try {
                            c = context.createPackageContext(ai.packageName, 1);
                        } catch (NameNotFoundException e) {
                        }
                    } catch (NameNotFoundException e2) {
                        context2 = context;
                    }
                } else {
                    c = this.mInitialApplication;
                    context2 = context;
                }
            }
            if (c == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to get context for package ");
                stringBuilder.append(ai.packageName);
                stringBuilder.append(" while loading content provider ");
                stringBuilder.append(providerInfo.name);
                Slog.w(TAG, stringBuilder.toString());
                return null;
            }
            if (providerInfo.splitName != null) {
                try {
                    c = c.createContextForSplit(providerInfo.splitName);
                } catch (NameNotFoundException e3) {
                    throw new RuntimeException(e3);
                }
            }
            try {
                ClassLoader cl = c.getClassLoader();
                LoadedApk packageInfo = peekPackageInfo(ai.packageName, true);
                if (packageInfo == null) {
                    packageInfo = getSystemContext().mPackageInfo;
                }
                ContentProvider localProvider2 = packageInfo.getAppFactory().instantiateProvider(cl, providerInfo.name);
                provider = localProvider2.getIContentProvider();
                if (provider == null) {
                    String str = TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Failed to instantiate class ");
                    stringBuilder2.append(providerInfo.name);
                    stringBuilder2.append(" from sourceDir ");
                    stringBuilder2.append(providerInfo.applicationInfo.sourceDir);
                    Slog.e(str, stringBuilder2.toString());
                    return null;
                }
                localProvider2.attachInfo(c, providerInfo);
                localProvider = localProvider2;
            } catch (Exception e4) {
                if (this.mInstrumentation.onException(null, e4)) {
                    return null;
                }
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Unable to get provider ");
                stringBuilder3.append(providerInfo.name);
                stringBuilder3.append(": ");
                stringBuilder3.append(e4.toString());
                throw new RuntimeException(stringBuilder3.toString(), e4);
            }
        }
        context2 = context;
        provider = contentProviderHolder.provider;
        localProvider = null;
        synchronized (this.mProviderMap) {
            try {
                ContentProviderHolder retHolder;
                IBinder localProvider3 = provider.asBinder();
                if (localProvider != null) {
                    ComponentName cname = new ComponentName(providerInfo.packageName, providerInfo.name);
                    ProviderClientRecord pr = (ProviderClientRecord) this.mLocalProvidersByName.get(cname);
                    if (pr != null) {
                        provider = pr.mProvider;
                    } else {
                        contentProviderHolder = new ContentProviderHolder(providerInfo);
                        contentProviderHolder.provider = provider;
                        contentProviderHolder.noReleaseNeeded = true;
                        pr = installProviderAuthoritiesLocked(provider, localProvider, contentProviderHolder);
                        this.mLocalProviders.put(localProvider3, pr);
                        this.mLocalProvidersByName.put(cname, pr);
                    }
                    retHolder = pr.mHolder;
                } else {
                    c = (ProviderRefCount) this.mProviderRefCountMap.get(localProvider3);
                    if (c == null) {
                        ProviderClientRecord client = installProviderAuthoritiesLocked(provider, localProvider, contentProviderHolder);
                        if (noReleaseNeeded) {
                            c = new ProviderRefCount(contentProviderHolder, client, 1000, 1000);
                        } else {
                            ProviderRefCount providerRefCount;
                            if (z) {
                                providerRefCount = new ProviderRefCount(contentProviderHolder, client, 1, 0);
                            } else {
                                providerRefCount = new ProviderRefCount(contentProviderHolder, client, 0, 1);
                            }
                            c = providerRefCount;
                        }
                        this.mProviderRefCountMap.put(localProvider3, c);
                    } else if (!noReleaseNeeded) {
                        incProviderRefLocked(c, z);
                        try {
                            ActivityManager.getService().removeContentProvider(contentProviderHolder.connection, z);
                        } catch (RemoteException e5) {
                        }
                    }
                    retHolder = c.holder;
                }
                return retHolder;
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    private void handleRunIsolatedEntryPoint(String entryPoint, String[] entryPointArgs) {
        try {
            Class.forName(entryPoint).getMethod("main", new Class[]{String[].class}).invoke(null, new Object[]{entryPointArgs});
            System.exit(0);
        } catch (ReflectiveOperationException e) {
            throw new AndroidRuntimeException("runIsolatedEntryPoint failed", e);
        }
    }

    @UnsupportedAppUsage
    private void attach(boolean system, long startSeq) {
        sCurrentActivityThread = this;
        this.mSystemThread = system;
        if (system) {
            DdmHandleAppName.setAppName("system_process", UserHandle.myUserId());
            try {
                this.mInstrumentation = new Instrumentation();
                this.mInstrumentation.basicInit(this);
                this.mInitialApplication = ContextImpl.createAppContext(this, getSystemContext().mPackageInfo).mPackageInfo.makeApplication(true, null);
                this.mInitialApplication.onCreate();
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to instantiate Application():");
                stringBuilder.append(e.toString());
                throw new RuntimeException(stringBuilder.toString(), e);
            }
        }
        DdmHandleAppName.setAppName("<pre-initialized>", UserHandle.myUserId());
        RuntimeInit.setApplicationObject(this.mAppThread.asBinder());
        try {
            ActivityManager.getService().attachApplication(this.mAppThread, startSeq);
            BinderInternal.addGcWatcher(new Runnable() {
                public void run() {
                    if (ActivityThread.this.mSomeActivitiesChanged) {
                        Runtime runtime = Runtime.getRuntime();
                        if (runtime.totalMemory() - runtime.freeMemory() > (3 * runtime.maxMemory()) / 4) {
                            ActivityThread.this.mSomeActivitiesChanged = false;
                            try {
                                ActivityTaskManager.getService().releaseSomeActivities(ActivityThread.this.mAppThread);
                            } catch (RemoteException e) {
                                throw e.rethrowFromSystemServer();
                            }
                        }
                    }
                }
            });
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
        ViewRootImpl.addConfigCallback(new -$$Lambda$ActivityThread$Wg40iAoNYFxps_KmrqtgptTB054(this));
    }

    public /* synthetic */ void lambda$attach$1$ActivityThread(Configuration globalConfig) {
        synchronized (this.mResourcesManager) {
            if (this.mResourcesManager.applyConfigurationToResourcesLocked(globalConfig, null)) {
                updateLocaleListFromAppContext(this.mInitialApplication.getApplicationContext(), this.mResourcesManager.getConfiguration().getLocales());
                if (this.mPendingConfiguration == null || this.mPendingConfiguration.isOtherSeqNewer(globalConfig)) {
                    this.mPendingConfiguration = globalConfig;
                    sendMessage(118, globalConfig);
                }
            }
        }
    }

    @UnsupportedAppUsage
    public static ActivityThread systemMain() {
        if (ActivityManager.isHighEndGfx()) {
            ThreadedRenderer.enableForegroundTrimming();
        } else {
            ThreadedRenderer.disable(true);
        }
        ActivityThread thread = new ActivityThread();
        thread.attach(true, 0);
        return thread;
    }

    public static void updateHttpProxy(Context context) {
        Proxy.setHttpProxySystemProperty(ConnectivityManager.from(context).getDefaultProxy());
    }

    @UnsupportedAppUsage
    public final void installSystemProviders(List<ProviderInfo> providers) {
        if (providers != null) {
            installContentProviders(this.mInitialApplication, providers);
        }
    }

    public int getIntCoreSetting(String key, int defaultValue) {
        synchronized (this.mResourcesManager) {
            if (this.mCoreSettings != null) {
                int i = this.mCoreSettings.getInt(key, defaultValue);
                return i;
            }
            return defaultValue;
        }
    }

    public static void main(String[] args) {
        Trace.traceBegin(64, "ActivityThreadMain");
        AndroidOs.install();
        CloseGuard.setEnabled(false);
        Environment.initForCurrentUser();
        TrustedCertificateStore.setDefaultUserDirectory(Environment.getUserConfigDirectory(UserHandle.myUserId()));
        Process.setArgV0("<pre-initialized>");
        Looper.prepareMainLooper();
        long startSeq = 0;
        if (args != null) {
            for (int i = args.length - 1; i >= 0; i--) {
                if (args[i] != null) {
                    String str = args[i];
                    String str2 = PROC_START_SEQ_IDENT;
                    if (str.startsWith(str2)) {
                        startSeq = Long.parseLong(args[i].substring(str2.length()));
                    }
                }
            }
        }
        ActivityThread thread = new ActivityThread();
        thread.attach(false, startSeq);
        if (sMainThreadHandler == null) {
            sMainThreadHandler = thread.getHandler();
        }
        Trace.traceEnd(64);
        Looper.loop();
        throw new RuntimeException("Main thread loop unexpectedly exited");
    }

    private void purgePendingResources() {
        Trace.traceBegin(64, "purgePendingResources");
        nPurgePendingResources();
        Trace.traceEnd(64);
    }
}
