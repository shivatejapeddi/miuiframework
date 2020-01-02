package android.view.contentcapture;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.ContentCaptureOptions;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.WindowManager.LayoutParams;
import android.view.contentcapture.IContentCaptureManager.Stub;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import com.android.internal.util.SyncResultReceiver;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

public final class ContentCaptureManager {
    public static final int DEFAULT_IDLE_FLUSHING_FREQUENCY_MS = 5000;
    public static final int DEFAULT_LOG_HISTORY_SIZE = 10;
    public static final int DEFAULT_MAX_BUFFER_SIZE = 100;
    public static final int DEFAULT_TEXT_CHANGE_FLUSHING_FREQUENCY_MS = 1000;
    public static final String DEVICE_CONFIG_PROPERTY_IDLE_FLUSH_FREQUENCY = "idle_flush_frequency";
    public static final String DEVICE_CONFIG_PROPERTY_IDLE_UNBIND_TIMEOUT = "idle_unbind_timeout";
    public static final String DEVICE_CONFIG_PROPERTY_LOGGING_LEVEL = "logging_level";
    public static final String DEVICE_CONFIG_PROPERTY_LOG_HISTORY_SIZE = "log_history_size";
    public static final String DEVICE_CONFIG_PROPERTY_MAX_BUFFER_SIZE = "max_buffer_size";
    public static final String DEVICE_CONFIG_PROPERTY_SERVICE_EXPLICITLY_ENABLED = "service_explicitly_enabled";
    public static final String DEVICE_CONFIG_PROPERTY_TEXT_CHANGE_FLUSH_FREQUENCY = "text_change_flush_frequency";
    public static final int LOGGING_LEVEL_DEBUG = 1;
    public static final int LOGGING_LEVEL_OFF = 0;
    public static final int LOGGING_LEVEL_VERBOSE = 2;
    public static final int RESULT_CODE_FALSE = 2;
    public static final int RESULT_CODE_OK = 0;
    public static final int RESULT_CODE_SECURITY_EXCEPTION = -1;
    public static final int RESULT_CODE_TRUE = 1;
    private static final int SYNC_CALLS_TIMEOUT_MS = 5000;
    private static final String TAG = ContentCaptureManager.class.getSimpleName();
    private final Context mContext;
    @GuardedBy({"mLock"})
    private int mFlags;
    private final Handler mHandler;
    private final Object mLock = new Object();
    @GuardedBy({"mLock"})
    private MainContentCaptureSession mMainSession;
    final ContentCaptureOptions mOptions;
    private final IContentCaptureManager mService;

    private interface MyRunnable {
        void run(SyncResultReceiver syncResultReceiver) throws RemoteException;
    }

    public interface ContentCaptureClient {
        ComponentName contentCaptureClientGetComponentName();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LoggingLevel {
    }

    public ContentCaptureManager(Context context, IContentCaptureManager service, ContentCaptureOptions options) {
        this.mContext = (Context) Preconditions.checkNotNull(context, "context cannot be null");
        this.mService = (IContentCaptureManager) Preconditions.checkNotNull(service, "service cannot be null");
        this.mOptions = (ContentCaptureOptions) Preconditions.checkNotNull(options, "options cannot be null");
        ContentCaptureHelper.setLoggingLevel(this.mOptions.loggingLevel);
        if (ContentCaptureHelper.sVerbose) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Constructor for ");
            stringBuilder.append(context.getPackageName());
            Log.v(str, stringBuilder.toString());
        }
        this.mHandler = Handler.createAsync(Looper.getMainLooper());
    }

    public MainContentCaptureSession getMainContentCaptureSession() {
        MainContentCaptureSession mainContentCaptureSession;
        synchronized (this.mLock) {
            if (this.mMainSession == null) {
                this.mMainSession = new MainContentCaptureSession(this.mContext, this, this.mHandler, this.mService);
                if (ContentCaptureHelper.sVerbose) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("getMainContentCaptureSession(): created ");
                    stringBuilder.append(this.mMainSession);
                    Log.v(str, stringBuilder.toString());
                }
            }
            mainContentCaptureSession = this.mMainSession;
        }
        return mainContentCaptureSession;
    }

    public void onActivityCreated(IBinder applicationToken, ComponentName activityComponent) {
        if (!this.mOptions.lite) {
            synchronized (this.mLock) {
                getMainContentCaptureSession().start(applicationToken, activityComponent, this.mFlags);
            }
        }
    }

    public void onActivityResumed() {
        if (!this.mOptions.lite) {
            getMainContentCaptureSession().notifySessionLifecycle(true);
        }
    }

    public void onActivityPaused() {
        if (!this.mOptions.lite) {
            getMainContentCaptureSession().notifySessionLifecycle(false);
        }
    }

    public void onActivityDestroyed() {
        if (!this.mOptions.lite) {
            getMainContentCaptureSession().destroy();
        }
    }

    public void flush(int reason) {
        if (!this.mOptions.lite) {
            getMainContentCaptureSession().flush(reason);
        }
    }

    public ComponentName getServiceComponentName() {
        if (!isContentCaptureEnabled() && !this.mOptions.lite) {
            return null;
        }
        SyncResultReceiver resultReceiver = new SyncResultReceiver(5000);
        try {
            this.mService.getServiceComponentName(resultReceiver);
            return (ComponentName) resultReceiver.getParcelableResult();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static ComponentName getServiceSettingsComponentName() {
        IBinder binder = ServiceManager.checkService("content_capture");
        if (binder == null) {
            return null;
        }
        IContentCaptureManager service = Stub.asInterface(binder);
        SyncResultReceiver resultReceiver = new SyncResultReceiver(5000);
        try {
            service.getServiceSettingsActivity(resultReceiver);
            if (resultReceiver.getIntResult() != -1) {
                return (ComponentName) resultReceiver.getParcelableResult();
            }
            throw new SecurityException(resultReceiver.getStringResult());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isContentCaptureEnabled() {
        if (this.mOptions.lite) {
            return false;
        }
        MainContentCaptureSession mainSession;
        synchronized (this.mLock) {
            mainSession = this.mMainSession;
        }
        if (mainSession == null || !mainSession.isDisabled()) {
            return true;
        }
        return false;
    }

    public Set<ContentCaptureCondition> getContentCaptureConditions() {
        if (isContentCaptureEnabled() || this.mOptions.lite) {
            return ContentCaptureHelper.toSet(syncRun(new -$$Lambda$ContentCaptureManager$F5a5O5ubPHwlndmmnmOInl75_sQ(this)).getParcelableListResult());
        }
        return null;
    }

    public /* synthetic */ void lambda$getContentCaptureConditions$0$ContentCaptureManager(SyncResultReceiver r) throws RemoteException {
        this.mService.getContentCaptureConditions(this.mContext.getPackageName(), r);
    }

    public void setContentCaptureEnabled(boolean enabled) {
        MainContentCaptureSession mainSession;
        if (ContentCaptureHelper.sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setContentCaptureEnabled(): setting to ");
            stringBuilder.append(enabled);
            stringBuilder.append(" for ");
            stringBuilder.append(this.mContext);
            Log.d(str, stringBuilder.toString());
        }
        synchronized (this.mLock) {
            if (enabled) {
                this.mFlags &= -2;
            } else {
                this.mFlags |= 1;
            }
            mainSession = this.mMainSession;
        }
        if (mainSession != null) {
            mainSession.setDisabled(enabled ^ 1);
        }
    }

    public void updateWindowAttributes(LayoutParams params) {
        MainContentCaptureSession mainSession;
        if (ContentCaptureHelper.sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateWindowAttributes(): window flags=");
            stringBuilder.append(params.flags);
            Log.d(str, stringBuilder.toString());
        }
        boolean flagSecureEnabled = (params.flags & 8192) != 0;
        synchronized (this.mLock) {
            if (flagSecureEnabled) {
                this.mFlags |= 2;
            } else {
                this.mFlags &= -3;
            }
            mainSession = this.mMainSession;
        }
        if (mainSession != null) {
            mainSession.setDisabled(flagSecureEnabled);
        }
    }

    @SystemApi
    public boolean isContentCaptureFeatureEnabled() {
        int resultCode = syncRun(new -$$Lambda$ContentCaptureManager$uvjEvSXcmP7-uA6i89N3m1TrKCk(this)).getIntResult();
        if (resultCode == 1) {
            return true;
        }
        if (resultCode == 2) {
            return false;
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("received invalid result: ");
        stringBuilder.append(resultCode);
        Log.wtf(str, stringBuilder.toString());
        return false;
    }

    public /* synthetic */ void lambda$isContentCaptureFeatureEnabled$1$ContentCaptureManager(SyncResultReceiver r) throws RemoteException {
        this.mService.isContentCaptureFeatureEnabled(r);
    }

    public void removeData(DataRemovalRequest request) {
        Preconditions.checkNotNull(request);
        try {
            this.mService.removeData(request);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    private SyncResultReceiver syncRun(MyRunnable r) {
        SyncResultReceiver resultReceiver = new SyncResultReceiver(5000);
        try {
            r.run(resultReceiver);
            if (resultReceiver.getIntResult() != -1) {
                return resultReceiver;
            }
            throw new SecurityException(resultReceiver.getStringResult());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.println("ContentCaptureManager");
        String prefix2 = new StringBuilder();
        prefix2.append(prefix);
        prefix2.append("  ");
        prefix2 = prefix2.toString();
        synchronized (this.mLock) {
            pw.print(prefix2);
            pw.print("isContentCaptureEnabled(): ");
            pw.println(isContentCaptureEnabled());
            pw.print(prefix2);
            pw.print("Debug: ");
            pw.print(ContentCaptureHelper.sDebug);
            pw.print(" Verbose: ");
            pw.println(ContentCaptureHelper.sVerbose);
            pw.print(prefix2);
            pw.print("Context: ");
            pw.println(this.mContext);
            pw.print(prefix2);
            pw.print("User: ");
            pw.println(this.mContext.getUserId());
            pw.print(prefix2);
            pw.print("Service: ");
            pw.println(this.mService);
            pw.print(prefix2);
            pw.print("Flags: ");
            pw.println(this.mFlags);
            pw.print(prefix2);
            pw.print("Options: ");
            this.mOptions.dumpShort(pw);
            pw.println();
            if (this.mMainSession != null) {
                String prefix3 = new StringBuilder();
                prefix3.append(prefix2);
                prefix3.append("  ");
                prefix3 = prefix3.toString();
                pw.print(prefix2);
                pw.println("Main session:");
                this.mMainSession.dump(prefix3, pw);
            } else {
                pw.print(prefix2);
                pw.println("No sessions");
            }
        }
    }
}
