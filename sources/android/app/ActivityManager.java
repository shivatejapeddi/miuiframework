package android.app;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.IActivityManager.Stub;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.ParceledListSlice;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.ColorSpace.Named;
import android.graphics.GraphicBuffer;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.BatteryStats;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.WorkSource;
import android.util.ArrayMap;
import android.util.Singleton;
import android.util.Size;
import com.android.internal.R;
import com.android.internal.app.LocalePicker;
import com.android.internal.app.procstats.ProcessStats;
import com.android.internal.os.RoSystemProperties;
import com.android.internal.os.TransferPipe;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.MemInfoReader;
import com.android.server.LocalServices;
import com.miui.internal.transition.IMiuiAppTransitionAnimationHelper;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.xmlpull.v1.XmlSerializer;

public class ActivityManager {
    public static final String ACTION_REPORT_HEAP_LIMIT = "android.app.action.REPORT_HEAP_LIMIT";
    public static final int APP_START_MODE_DELAYED = 1;
    public static final int APP_START_MODE_DELAYED_RIGID = 2;
    public static final int APP_START_MODE_DISABLED = 3;
    public static final int APP_START_MODE_NORMAL = 0;
    public static final int ASSIST_CONTEXT_AUTOFILL = 2;
    public static final int ASSIST_CONTEXT_BASIC = 0;
    public static final int ASSIST_CONTEXT_FULL = 1;
    public static final int BROADCAST_FAILED_USER_STOPPED = -2;
    public static final int BROADCAST_STICKY_CANT_HAVE_PERMISSION = -1;
    public static final int BROADCAST_SUCCESS = 0;
    public static final int BUGREPORT_OPTION_FULL = 0;
    public static final int BUGREPORT_OPTION_INTERACTIVE = 1;
    public static final int BUGREPORT_OPTION_REMOTE = 2;
    public static final int BUGREPORT_OPTION_TELEPHONY = 4;
    public static final int BUGREPORT_OPTION_WEAR = 3;
    public static final int BUGREPORT_OPTION_WIFI = 5;
    public static final int COMPAT_MODE_ALWAYS = -1;
    public static final int COMPAT_MODE_DISABLED = 0;
    public static final int COMPAT_MODE_ENABLED = 1;
    public static final int COMPAT_MODE_NEVER = -2;
    public static final int COMPAT_MODE_TOGGLE = 2;
    public static final int COMPAT_MODE_UNKNOWN = -3;
    private static final boolean DEVELOPMENT_FORCE_LOW_RAM = SystemProperties.getBoolean("debug.force_low_ram", false);
    private static final int FIRST_START_FATAL_ERROR_CODE = -100;
    private static final int FIRST_START_NON_FATAL_ERROR_CODE = 100;
    private static final int FIRST_START_SUCCESS_CODE = 0;
    public static final int FLAG_AND_LOCKED = 2;
    public static final int FLAG_AND_UNLOCKED = 4;
    public static final int FLAG_AND_UNLOCKING_OR_UNLOCKED = 8;
    public static final int FLAG_OR_STOPPED = 1;
    @UnsupportedAppUsage
    private static final Singleton<IActivityManager> IActivityManagerSingleton = new Singleton<IActivityManager>() {
        /* Access modifiers changed, original: protected */
        public IActivityManager create() {
            return Stub.asInterface(ServiceManager.getService(Context.ACTIVITY_SERVICE));
        }
    };
    public static final int INSTR_FLAG_DISABLE_HIDDEN_API_CHECKS = 1;
    public static final int INSTR_FLAG_MOUNT_EXTERNAL_STORAGE_FULL = 2;
    @UnsupportedAppUsage
    public static final int INTENT_SENDER_ACTIVITY = 2;
    public static final int INTENT_SENDER_ACTIVITY_RESULT = 3;
    public static final int INTENT_SENDER_BROADCAST = 1;
    public static final int INTENT_SENDER_FOREGROUND_SERVICE = 5;
    public static final int INTENT_SENDER_SERVICE = 4;
    private static final int LAST_START_FATAL_ERROR_CODE = -1;
    private static final int LAST_START_NON_FATAL_ERROR_CODE = 199;
    private static final int LAST_START_SUCCESS_CODE = 99;
    public static final int LOCK_TASK_MODE_LOCKED = 1;
    public static final int LOCK_TASK_MODE_NONE = 0;
    public static final int LOCK_TASK_MODE_PINNED = 2;
    public static final int MAX_PROCESS_STATE = 21;
    public static final String META_HOME_ALTERNATE = "android.app.home.alternate";
    public static final int MIN_PROCESS_STATE = 0;
    public static final int MOVE_TASK_NO_USER_ACTION = 2;
    public static final int MOVE_TASK_WITH_HOME = 1;
    public static final int PROCESS_STATE_BACKUP = 10;
    @UnsupportedAppUsage
    public static final int PROCESS_STATE_BOUND_FOREGROUND_SERVICE = 6;
    public static final int PROCESS_STATE_BOUND_TOP = 4;
    @UnsupportedAppUsage
    public static final int PROCESS_STATE_CACHED_ACTIVITY = 17;
    public static final int PROCESS_STATE_CACHED_ACTIVITY_CLIENT = 18;
    public static final int PROCESS_STATE_CACHED_EMPTY = 20;
    public static final int PROCESS_STATE_CACHED_RECENT = 19;
    @UnsupportedAppUsage
    public static final int PROCESS_STATE_FOREGROUND_SERVICE = 5;
    public static final int PROCESS_STATE_FOREGROUND_SERVICE_LOCATION = 3;
    public static final int PROCESS_STATE_HEAVY_WEIGHT = 14;
    @UnsupportedAppUsage
    public static final int PROCESS_STATE_HOME = 15;
    @UnsupportedAppUsage
    public static final int PROCESS_STATE_IMPORTANT_BACKGROUND = 8;
    public static final int PROCESS_STATE_IMPORTANT_FOREGROUND = 7;
    public static final int PROCESS_STATE_LAST_ACTIVITY = 16;
    public static final int PROCESS_STATE_NONEXISTENT = 21;
    public static final int PROCESS_STATE_PERSISTENT = 0;
    public static final int PROCESS_STATE_PERSISTENT_UI = 1;
    @UnsupportedAppUsage
    public static final int PROCESS_STATE_RECEIVER = 12;
    @UnsupportedAppUsage
    public static final int PROCESS_STATE_SERVICE = 11;
    @UnsupportedAppUsage
    public static final int PROCESS_STATE_TOP = 2;
    public static final int PROCESS_STATE_TOP_SLEEPING = 13;
    public static final int PROCESS_STATE_TRANSIENT_BACKGROUND = 9;
    public static final int PROCESS_STATE_UNKNOWN = -1;
    public static final int RECENT_IGNORE_UNAVAILABLE = 2;
    public static final int RECENT_WITH_EXCLUDED = 1;
    public static final int START_ABORTED = 102;
    public static final int START_ASSISTANT_HIDDEN_SESSION = -90;
    public static final int START_ASSISTANT_NOT_ACTIVE_SESSION = -89;
    public static final int START_CANCELED = -96;
    public static final int START_CLASS_NOT_FOUND = -92;
    public static final int START_DELIVERED_TO_TOP = 3;
    public static final int START_FLAG_DEBUG = 2;
    public static final int START_FLAG_NATIVE_DEBUGGING = 8;
    public static final int START_FLAG_ONLY_IF_NEEDED = 1;
    public static final int START_FLAG_TRACK_ALLOCATION = 4;
    public static final int START_FORWARD_AND_REQUEST_CONFLICT = -93;
    public static final int START_INTENT_NOT_RESOLVED = -91;
    public static final int START_NOT_ACTIVITY = -95;
    public static final int START_NOT_CURRENT_USER_ACTIVITY = -98;
    public static final int START_NOT_VOICE_COMPATIBLE = -97;
    public static final int START_PERMISSION_DENIED = -94;
    public static final int START_RETURN_INTENT_TO_CALLER = 1;
    public static final int START_RETURN_LOCK_TASK_MODE_VIOLATION = 101;
    public static final int START_SUCCESS = 0;
    public static final int START_SWITCHES_CANCELED = 100;
    public static final int START_TASK_TO_FRONT = 2;
    public static final int START_VOICE_HIDDEN_SESSION = -100;
    public static final int START_VOICE_NOT_ACTIVE_SESSION = -99;
    private static String TAG = "ActivityManager";
    public static final int UID_OBSERVER_ACTIVE = 8;
    public static final int UID_OBSERVER_CACHED = 16;
    public static final int UID_OBSERVER_GONE = 2;
    public static final int UID_OBSERVER_IDLE = 4;
    public static final int UID_OBSERVER_PROCSTATE = 1;
    public static final int USER_OP_ERROR_IS_SYSTEM = -3;
    public static final int USER_OP_ERROR_RELATED_USERS_CANNOT_STOP = -4;
    public static final int USER_OP_IS_CURRENT = -2;
    public static final int USER_OP_SUCCESS = 0;
    public static final int USER_OP_UNKNOWN_USER = -1;
    private static volatile boolean sSystemReady = false;
    Point mAppTaskThumbnailSize;
    @UnsupportedAppUsage
    private final Context mContext;
    final ArrayMap<OnUidImportanceListener, UidObserver> mImportanceListeners = new ArrayMap();

    public static class AppTask {
        private IAppTask mAppTaskImpl;

        public AppTask(IAppTask task) {
            this.mAppTaskImpl = task;
        }

        public void finishAndRemoveTask() {
            try {
                this.mAppTaskImpl.finishAndRemoveTask();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public RecentTaskInfo getTaskInfo() {
            try {
                return this.mAppTaskImpl.getTaskInfo();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void moveToFront() {
            try {
                this.mAppTaskImpl.moveToFront(ActivityThread.currentActivityThread().getApplicationThread(), ActivityThread.currentPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void startActivity(Context context, Intent intent, Bundle options) {
            ActivityThread thread = ActivityThread.currentActivityThread();
            thread.getInstrumentation().execStartActivityFromAppTask(context, thread.getApplicationThread(), this.mAppTaskImpl, intent, options);
        }

        public void setExcludeFromRecents(boolean exclude) {
            try {
                this.mAppTaskImpl.setExcludeFromRecents(exclude);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BugreportMode {
    }

    public static class MemoryInfo implements Parcelable {
        public static final Creator<MemoryInfo> CREATOR = new Creator<MemoryInfo>() {
            public MemoryInfo createFromParcel(Parcel source) {
                return new MemoryInfo(source, null);
            }

            public MemoryInfo[] newArray(int size) {
                return new MemoryInfo[size];
            }
        };
        public long availMem;
        @UnsupportedAppUsage
        public long foregroundAppThreshold;
        @UnsupportedAppUsage
        public long hiddenAppThreshold;
        public boolean lowMemory;
        @UnsupportedAppUsage
        public long secondaryServerThreshold;
        public long threshold;
        public long totalMem;
        @UnsupportedAppUsage
        public long visibleAppThreshold;

        /* synthetic */ MemoryInfo(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.availMem);
            dest.writeLong(this.totalMem);
            dest.writeLong(this.threshold);
            dest.writeInt(this.lowMemory);
            dest.writeLong(this.hiddenAppThreshold);
            dest.writeLong(this.secondaryServerThreshold);
            dest.writeLong(this.visibleAppThreshold);
            dest.writeLong(this.foregroundAppThreshold);
        }

        public void readFromParcel(Parcel source) {
            this.availMem = source.readLong();
            this.totalMem = source.readLong();
            this.threshold = source.readLong();
            this.lowMemory = source.readInt() != 0;
            this.hiddenAppThreshold = source.readLong();
            this.secondaryServerThreshold = source.readLong();
            this.visibleAppThreshold = source.readLong();
            this.foregroundAppThreshold = source.readLong();
        }

        private MemoryInfo(Parcel source) {
            readFromParcel(source);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MoveTaskFlags {
    }

    @SystemApi
    public interface OnUidImportanceListener {
        void onUidImportance(int i, int i2);
    }

    public static class ProcessErrorStateInfo implements Parcelable {
        public static final int CRASHED = 1;
        public static final Creator<ProcessErrorStateInfo> CREATOR = new Creator<ProcessErrorStateInfo>() {
            public ProcessErrorStateInfo createFromParcel(Parcel source) {
                return new ProcessErrorStateInfo(source, null);
            }

            public ProcessErrorStateInfo[] newArray(int size) {
                return new ProcessErrorStateInfo[size];
            }
        };
        public static final int NOT_RESPONDING = 2;
        public static final int NO_ERROR = 0;
        public int condition;
        public byte[] crashData;
        public String longMsg;
        public int pid;
        public String processName;
        public String shortMsg;
        public String stackTrace;
        public String tag;
        public int uid;

        /* synthetic */ ProcessErrorStateInfo(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        public ProcessErrorStateInfo() {
            this.crashData = null;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.condition);
            dest.writeString(this.processName);
            dest.writeInt(this.pid);
            dest.writeInt(this.uid);
            dest.writeString(this.tag);
            dest.writeString(this.shortMsg);
            dest.writeString(this.longMsg);
            dest.writeString(this.stackTrace);
        }

        public void readFromParcel(Parcel source) {
            this.condition = source.readInt();
            this.processName = source.readString();
            this.pid = source.readInt();
            this.uid = source.readInt();
            this.tag = source.readString();
            this.shortMsg = source.readString();
            this.longMsg = source.readString();
            this.stackTrace = source.readString();
        }

        private ProcessErrorStateInfo(Parcel source) {
            this.crashData = null;
            readFromParcel(source);
        }
    }

    public static class RecentTaskInfo extends TaskInfo implements Parcelable {
        public static final Creator<RecentTaskInfo> CREATOR = new Creator<RecentTaskInfo>() {
            public RecentTaskInfo createFromParcel(Parcel source) {
                return new RecentTaskInfo(source, null);
            }

            public RecentTaskInfo[] newArray(int size) {
                return new RecentTaskInfo[size];
            }
        };
        @Deprecated
        public int affiliatedTaskId;
        @Deprecated
        public CharSequence description;
        @Deprecated
        public int id;
        @Deprecated
        public int persistentId;

        /* synthetic */ RecentTaskInfo(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        private RecentTaskInfo(Parcel source) {
            readFromParcel(source);
        }

        public int describeContents() {
            return 0;
        }

        public void readFromParcel(Parcel source) {
            this.id = source.readInt();
            this.persistentId = source.readInt();
            super.readFromParcel(source);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.persistentId);
            super.writeToParcel(dest, flags);
        }

        public void dump(PrintWriter pw, String indent) {
            String activityType = WindowConfiguration.activityTypeToString(this.configuration.windowConfiguration.getActivityType());
            String windowingMode = WindowConfiguration.activityTypeToString(this.configuration.windowConfiguration.getActivityType());
            pw.println();
            String str = "   ";
            pw.print(str);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" id=");
            stringBuilder.append(this.persistentId);
            pw.print(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(" stackId=");
            stringBuilder.append(this.stackId);
            pw.print(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(" userId=");
            stringBuilder.append(this.userId);
            pw.print(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(" hasTask=");
            boolean z = true;
            stringBuilder.append(this.id != -1);
            pw.print(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(" lastActiveTime=");
            stringBuilder.append(this.lastActiveTime);
            pw.print(stringBuilder.toString());
            pw.println();
            pw.print(str);
            stringBuilder = new StringBuilder();
            stringBuilder.append(" baseIntent=");
            stringBuilder.append(this.baseIntent);
            pw.print(stringBuilder.toString());
            pw.println();
            pw.print(str);
            stringBuilder = new StringBuilder();
            stringBuilder.append(" isExcluded=");
            stringBuilder.append((this.baseIntent.getFlags() & 8388608) != 0);
            pw.print(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(" activityType=");
            stringBuilder.append(activityType);
            pw.print(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(" windowingMode=");
            stringBuilder.append(windowingMode);
            pw.print(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(" supportsSplitScreenMultiWindow=");
            stringBuilder.append(this.supportsSplitScreenMultiWindow);
            pw.print(stringBuilder.toString());
            if (this.taskDescription != null) {
                pw.println();
                pw.print(str);
                TaskDescription td = this.taskDescription;
                pw.print(" taskDescription {");
                stringBuilder = new StringBuilder();
                stringBuilder.append(" colorBackground=#");
                stringBuilder.append(Integer.toHexString(td.getBackgroundColor()));
                pw.print(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                stringBuilder.append(" colorPrimary=#");
                stringBuilder.append(Integer.toHexString(td.getPrimaryColor()));
                pw.print(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                stringBuilder.append(" iconRes=");
                stringBuilder.append(td.getIconResource() != 0);
                pw.print(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                stringBuilder.append(" iconBitmap=");
                if (td.getIconFilename() == null && td.getInMemoryIcon() == null) {
                    z = false;
                }
                stringBuilder.append(z);
                pw.print(stringBuilder.toString());
                pw.println(" }");
            }
        }
    }

    public static class RunningAppProcessInfo implements Parcelable {
        public static final Creator<RunningAppProcessInfo> CREATOR = new Creator<RunningAppProcessInfo>() {
            public RunningAppProcessInfo createFromParcel(Parcel source) {
                return new RunningAppProcessInfo(source, null);
            }

            public RunningAppProcessInfo[] newArray(int size) {
                return new RunningAppProcessInfo[size];
            }
        };
        public static final int FLAG_CANT_SAVE_STATE = 1;
        @UnsupportedAppUsage
        public static final int FLAG_HAS_ACTIVITIES = 4;
        @UnsupportedAppUsage
        public static final int FLAG_PERSISTENT = 2;
        public static final int IMPORTANCE_BACKGROUND = 400;
        public static final int IMPORTANCE_CACHED = 400;
        public static final int IMPORTANCE_CANT_SAVE_STATE = 350;
        public static final int IMPORTANCE_CANT_SAVE_STATE_PRE_26 = 170;
        @Deprecated
        public static final int IMPORTANCE_EMPTY = 500;
        public static final int IMPORTANCE_FOREGROUND = 100;
        public static final int IMPORTANCE_FOREGROUND_SERVICE = 125;
        public static final int IMPORTANCE_GONE = 1000;
        public static final int IMPORTANCE_PERCEPTIBLE = 230;
        public static final int IMPORTANCE_PERCEPTIBLE_PRE_26 = 130;
        public static final int IMPORTANCE_SERVICE = 300;
        public static final int IMPORTANCE_TOP_SLEEPING = 325;
        @Deprecated
        public static final int IMPORTANCE_TOP_SLEEPING_PRE_28 = 150;
        public static final int IMPORTANCE_VISIBLE = 200;
        public static final int REASON_PROVIDER_IN_USE = 1;
        public static final int REASON_SERVICE_IN_USE = 2;
        public static final int REASON_UNKNOWN = 0;
        @UnsupportedAppUsage
        public int flags;
        public int importance;
        public int importanceReasonCode;
        public ComponentName importanceReasonComponent;
        public int importanceReasonImportance;
        public int importanceReasonPid;
        public boolean isFocused;
        public long lastActivityTime;
        public int lastTrimLevel;
        public int lru;
        public int pid;
        public String[] pkgList;
        public String processName;
        @UnsupportedAppUsage
        public int processState;
        public int uid;

        @Retention(RetentionPolicy.SOURCE)
        public @interface Importance {
        }

        /* synthetic */ RunningAppProcessInfo(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        @UnsupportedAppUsage
        public static int procStateToImportance(int procState) {
            if (procState == 21) {
                return 1000;
            }
            if (procState >= 15) {
                return 400;
            }
            if (procState == 14) {
                return 350;
            }
            if (procState >= 13) {
                return 325;
            }
            if (procState >= 11) {
                return 300;
            }
            if (procState >= 9) {
                return 230;
            }
            if (procState >= 7) {
                return 200;
            }
            if (procState >= 3) {
                return 125;
            }
            return 100;
        }

        public static int procStateToImportanceForClient(int procState, Context clientContext) {
            return procStateToImportanceForTargetSdk(procState, clientContext.getApplicationInfo().targetSdkVersion);
        }

        public static int procStateToImportanceForTargetSdk(int procState, int targetSdkVersion) {
            int importance = procStateToImportance(procState);
            if (targetSdkVersion < 26) {
                if (importance == 230) {
                    return 130;
                }
                if (importance == 325) {
                    return 150;
                }
                if (importance == 350) {
                    return 170;
                }
            }
            return importance;
        }

        public static int importanceToProcState(int importance) {
            if (importance == 1000) {
                return 21;
            }
            if (importance >= 400) {
                return 15;
            }
            if (importance >= 350) {
                return 14;
            }
            if (importance >= 325) {
                return 13;
            }
            if (importance >= 300) {
                return 11;
            }
            if (importance >= 230) {
                return 9;
            }
            if (importance >= 200 || importance >= 150) {
                return 7;
            }
            if (importance >= 125) {
                return 5;
            }
            return 2;
        }

        public RunningAppProcessInfo() {
            this.importance = 100;
            this.importanceReasonCode = 0;
            this.processState = 7;
            this.isFocused = false;
            this.lastActivityTime = 0;
        }

        public RunningAppProcessInfo(String pProcessName, int pPid, String[] pArr) {
            this.processName = pProcessName;
            this.pid = pPid;
            this.pkgList = pArr;
            this.isFocused = false;
            this.lastActivityTime = 0;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.processName);
            dest.writeInt(this.pid);
            dest.writeInt(this.uid);
            dest.writeStringArray(this.pkgList);
            dest.writeInt(this.flags);
            dest.writeInt(this.lastTrimLevel);
            dest.writeInt(this.importance);
            dest.writeInt(this.lru);
            dest.writeInt(this.importanceReasonCode);
            dest.writeInt(this.importanceReasonPid);
            ComponentName.writeToParcel(this.importanceReasonComponent, dest);
            dest.writeInt(this.importanceReasonImportance);
            dest.writeInt(this.processState);
            dest.writeInt(this.isFocused);
            dest.writeLong(this.lastActivityTime);
        }

        public void readFromParcel(Parcel source) {
            this.processName = source.readString();
            this.pid = source.readInt();
            this.uid = source.readInt();
            this.pkgList = source.readStringArray();
            this.flags = source.readInt();
            this.lastTrimLevel = source.readInt();
            this.importance = source.readInt();
            this.lru = source.readInt();
            this.importanceReasonCode = source.readInt();
            this.importanceReasonPid = source.readInt();
            this.importanceReasonComponent = ComponentName.readFromParcel(source);
            this.importanceReasonImportance = source.readInt();
            this.processState = source.readInt();
            this.isFocused = source.readInt() != 0;
            this.lastActivityTime = source.readLong();
        }

        private RunningAppProcessInfo(Parcel source) {
            readFromParcel(source);
        }
    }

    public static class RunningServiceInfo implements Parcelable {
        public static final Creator<RunningServiceInfo> CREATOR = new Creator<RunningServiceInfo>() {
            public RunningServiceInfo createFromParcel(Parcel source) {
                return new RunningServiceInfo(source, null);
            }

            public RunningServiceInfo[] newArray(int size) {
                return new RunningServiceInfo[size];
            }
        };
        public static final int FLAG_FOREGROUND = 2;
        public static final int FLAG_PERSISTENT_PROCESS = 8;
        public static final int FLAG_STARTED = 1;
        public static final int FLAG_SYSTEM_PROCESS = 4;
        public long activeSince;
        public int clientCount;
        public int clientLabel;
        public String clientPackage;
        public int crashCount;
        public int flags;
        public boolean foreground;
        public long lastActivityTime;
        public int pid;
        public String process;
        public long restarting;
        public ComponentName service;
        public boolean started;
        public int uid;

        /* synthetic */ RunningServiceInfo(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            ComponentName.writeToParcel(this.service, dest);
            dest.writeInt(this.pid);
            dest.writeInt(this.uid);
            dest.writeString(this.process);
            dest.writeInt(this.foreground);
            dest.writeLong(this.activeSince);
            dest.writeInt(this.started);
            dest.writeInt(this.clientCount);
            dest.writeInt(this.crashCount);
            dest.writeLong(this.lastActivityTime);
            dest.writeLong(this.restarting);
            dest.writeInt(this.flags);
            dest.writeString(this.clientPackage);
            dest.writeInt(this.clientLabel);
        }

        public void readFromParcel(Parcel source) {
            this.service = ComponentName.readFromParcel(source);
            this.pid = source.readInt();
            this.uid = source.readInt();
            this.process = source.readString();
            boolean z = true;
            this.foreground = source.readInt() != 0;
            this.activeSince = source.readLong();
            if (source.readInt() == 0) {
                z = false;
            }
            this.started = z;
            this.clientCount = source.readInt();
            this.crashCount = source.readInt();
            this.lastActivityTime = source.readLong();
            this.restarting = source.readLong();
            this.flags = source.readInt();
            this.clientPackage = source.readString();
            this.clientLabel = source.readInt();
        }

        private RunningServiceInfo(Parcel source) {
            readFromParcel(source);
        }
    }

    public static class RunningTaskInfo extends TaskInfo implements Parcelable {
        public static final Creator<RunningTaskInfo> CREATOR = new Creator<RunningTaskInfo>() {
            public RunningTaskInfo createFromParcel(Parcel source) {
                return new RunningTaskInfo(source, null);
            }

            public RunningTaskInfo[] newArray(int size) {
                return new RunningTaskInfo[size];
            }
        };
        @Deprecated
        public CharSequence description;
        @Deprecated
        public int id;
        @Deprecated
        public int numRunning;
        @Deprecated
        public Bitmap thumbnail;

        /* synthetic */ RunningTaskInfo(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        private RunningTaskInfo(Parcel source) {
            readFromParcel(source);
        }

        public int describeContents() {
            return 0;
        }

        public void readFromParcel(Parcel source) {
            this.id = source.readInt();
            super.readFromParcel(source);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            super.writeToParcel(dest, flags);
        }
    }

    public static class StackInfo implements Parcelable {
        public static final Creator<StackInfo> CREATOR = new Creator<StackInfo>() {
            public StackInfo createFromParcel(Parcel source) {
                return new StackInfo(source, null);
            }

            public StackInfo[] newArray(int size) {
                return new StackInfo[size];
            }
        };
        @UnsupportedAppUsage
        public Rect bounds;
        public final Configuration configuration;
        @UnsupportedAppUsage
        public int displayId;
        @UnsupportedAppUsage
        public int position;
        @UnsupportedAppUsage
        public int stackId;
        @UnsupportedAppUsage
        public Rect[] taskBounds;
        @UnsupportedAppUsage
        public int[] taskIds;
        @UnsupportedAppUsage
        public String[] taskNames;
        @UnsupportedAppUsage
        public int[] taskUserIds;
        @UnsupportedAppUsage
        public ComponentName topActivity;
        @UnsupportedAppUsage
        public int userId;
        @UnsupportedAppUsage
        public boolean visible;

        /* synthetic */ StackInfo(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.stackId);
            dest.writeInt(this.bounds.left);
            dest.writeInt(this.bounds.top);
            dest.writeInt(this.bounds.right);
            dest.writeInt(this.bounds.bottom);
            dest.writeIntArray(this.taskIds);
            dest.writeStringArray(this.taskNames);
            Rect[] rectArr = this.taskBounds;
            int boundsCount = rectArr == null ? 0 : rectArr.length;
            dest.writeInt(boundsCount);
            for (int i = 0; i < boundsCount; i++) {
                dest.writeInt(this.taskBounds[i].left);
                dest.writeInt(this.taskBounds[i].top);
                dest.writeInt(this.taskBounds[i].right);
                dest.writeInt(this.taskBounds[i].bottom);
            }
            dest.writeIntArray(this.taskUserIds);
            dest.writeInt(this.displayId);
            dest.writeInt(this.userId);
            dest.writeInt(this.visible);
            dest.writeInt(this.position);
            if (this.topActivity != null) {
                dest.writeInt(1);
                this.topActivity.writeToParcel(dest, 0);
            } else {
                dest.writeInt(0);
            }
            this.configuration.writeToParcel(dest, flags);
        }

        public void readFromParcel(Parcel source) {
            this.stackId = source.readInt();
            this.bounds = new Rect(source.readInt(), source.readInt(), source.readInt(), source.readInt());
            this.taskIds = source.createIntArray();
            this.taskNames = source.createStringArray();
            int boundsCount = source.readInt();
            if (boundsCount > 0) {
                this.taskBounds = new Rect[boundsCount];
                for (int i = 0; i < boundsCount; i++) {
                    this.taskBounds[i] = new Rect();
                    this.taskBounds[i].set(source.readInt(), source.readInt(), source.readInt(), source.readInt());
                }
            } else {
                this.taskBounds = null;
            }
            this.taskUserIds = source.createIntArray();
            this.displayId = source.readInt();
            this.userId = source.readInt();
            this.visible = source.readInt() > 0;
            this.position = source.readInt();
            if (source.readInt() > 0) {
                this.topActivity = ComponentName.readFromParcel(source);
            }
            this.configuration.readFromParcel(source);
        }

        public StackInfo() {
            this.bounds = new Rect();
            this.configuration = new Configuration();
        }

        private StackInfo(Parcel source) {
            this.bounds = new Rect();
            this.configuration = new Configuration();
            readFromParcel(source);
        }

        @UnsupportedAppUsage
        public String toString(String prefix) {
            StringBuilder sb = new StringBuilder(256);
            sb.append(prefix);
            sb.append("Stack id=");
            sb.append(this.stackId);
            String str = " bounds=";
            sb.append(str);
            sb.append(this.bounds.toShortString());
            sb.append(" displayId=");
            sb.append(this.displayId);
            String str2 = " userId=";
            sb.append(str2);
            sb.append(this.userId);
            String str3 = "\n";
            sb.append(str3);
            sb.append(" configuration=");
            sb.append(this.configuration);
            sb.append(str3);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("  ");
            prefix = stringBuilder.toString();
            for (int i = 0; i < this.taskIds.length; i++) {
                sb.append(prefix);
                sb.append("taskId=");
                sb.append(this.taskIds[i]);
                sb.append(": ");
                sb.append(this.taskNames[i]);
                if (this.taskBounds != null) {
                    sb.append(str);
                    sb.append(this.taskBounds[i].toShortString());
                }
                sb.append(str2);
                sb.append(this.taskUserIds[i]);
                sb.append(" visible=");
                sb.append(this.visible);
                if (this.topActivity != null) {
                    sb.append(" topActivity=");
                    sb.append(this.topActivity);
                }
                sb.append(str3);
            }
            return sb.toString();
        }

        public String toString() {
            return toString("");
        }
    }

    public static class TaskDescription implements Parcelable {
        private static final String ATTR_TASKDESCRIPTIONCOLOR_BACKGROUND = "task_description_colorBackground";
        private static final String ATTR_TASKDESCRIPTIONCOLOR_PRIMARY = "task_description_color";
        private static final String ATTR_TASKDESCRIPTIONICON_FILENAME = "task_description_icon_filename";
        private static final String ATTR_TASKDESCRIPTIONICON_RESOURCE = "task_description_icon_resource";
        private static final String ATTR_TASKDESCRIPTIONLABEL = "task_description_label";
        public static final String ATTR_TASKDESCRIPTION_PREFIX = "task_description_";
        public static final Creator<TaskDescription> CREATOR = new Creator<TaskDescription>() {
            public TaskDescription createFromParcel(Parcel source) {
                return new TaskDescription(source, null);
            }

            public TaskDescription[] newArray(int size) {
                return new TaskDescription[size];
            }
        };
        private int mColorBackground;
        private int mColorPrimary;
        private boolean mEnsureNavigationBarContrastWhenTransparent;
        private boolean mEnsureStatusBarContrastWhenTransparent;
        private Bitmap mIcon;
        private String mIconFilename;
        private int mIconRes;
        private String mLabel;
        private int mNavigationBarColor;
        private int mStatusBarColor;

        @Deprecated
        public TaskDescription(String label, Bitmap icon, int colorPrimary) {
            this(label, icon, 0, null, colorPrimary, 0, 0, 0, false, false);
            if (colorPrimary != 0 && Color.alpha(colorPrimary) != 255) {
                throw new RuntimeException("A TaskDescription's primary color should be opaque");
            }
        }

        public TaskDescription(String label, int iconRes, int colorPrimary) {
            this(label, null, iconRes, null, colorPrimary, 0, 0, 0, false, false);
            if (colorPrimary != 0 && Color.alpha(colorPrimary) != 255) {
                throw new RuntimeException("A TaskDescription's primary color should be opaque");
            }
        }

        @Deprecated
        public TaskDescription(String label, Bitmap icon) {
            this(label, icon, 0, null, 0, 0, 0, 0, false, false);
        }

        public TaskDescription(String label, int iconRes) {
            this(label, null, iconRes, null, 0, 0, 0, 0, false, false);
        }

        public TaskDescription(String label) {
            this(label, null, 0, null, 0, 0, 0, 0, false, false);
        }

        public TaskDescription() {
            this(null, null, 0, null, 0, 0, 0, 0, false, false);
        }

        public TaskDescription(String label, Bitmap bitmap, int iconRes, String iconFilename, int colorPrimary, int colorBackground, int statusBarColor, int navigationBarColor, boolean ensureStatusBarContrastWhenTransparent, boolean ensureNavigationBarContrastWhenTransparent) {
            this.mLabel = label;
            this.mIcon = bitmap;
            this.mIconRes = iconRes;
            this.mIconFilename = iconFilename;
            this.mColorPrimary = colorPrimary;
            this.mColorBackground = colorBackground;
            this.mStatusBarColor = statusBarColor;
            this.mNavigationBarColor = navigationBarColor;
            this.mEnsureStatusBarContrastWhenTransparent = ensureStatusBarContrastWhenTransparent;
            this.mEnsureNavigationBarContrastWhenTransparent = ensureNavigationBarContrastWhenTransparent;
        }

        public TaskDescription(TaskDescription td) {
            copyFrom(td);
        }

        public void copyFrom(TaskDescription other) {
            this.mLabel = other.mLabel;
            this.mIcon = other.mIcon;
            this.mIconRes = other.mIconRes;
            this.mIconFilename = other.mIconFilename;
            this.mColorPrimary = other.mColorPrimary;
            this.mColorBackground = other.mColorBackground;
            this.mStatusBarColor = other.mStatusBarColor;
            this.mNavigationBarColor = other.mNavigationBarColor;
            this.mEnsureStatusBarContrastWhenTransparent = other.mEnsureStatusBarContrastWhenTransparent;
            this.mEnsureNavigationBarContrastWhenTransparent = other.mEnsureNavigationBarContrastWhenTransparent;
        }

        public void copyFromPreserveHiddenFields(TaskDescription other) {
            this.mLabel = other.mLabel;
            this.mIcon = other.mIcon;
            this.mIconRes = other.mIconRes;
            this.mIconFilename = other.mIconFilename;
            this.mColorPrimary = other.mColorPrimary;
            int i = other.mColorBackground;
            if (i != 0) {
                this.mColorBackground = i;
            }
            i = other.mStatusBarColor;
            if (i != 0) {
                this.mStatusBarColor = i;
            }
            i = other.mNavigationBarColor;
            if (i != 0) {
                this.mNavigationBarColor = i;
            }
            this.mEnsureStatusBarContrastWhenTransparent = other.mEnsureStatusBarContrastWhenTransparent;
            this.mEnsureNavigationBarContrastWhenTransparent = other.mEnsureNavigationBarContrastWhenTransparent;
        }

        private TaskDescription(Parcel source) {
            readFromParcel(source);
        }

        public void setLabel(String label) {
            this.mLabel = label;
        }

        public void setPrimaryColor(int primaryColor) {
            if (primaryColor == 0 || Color.alpha(primaryColor) == 255) {
                this.mColorPrimary = primaryColor;
                return;
            }
            throw new RuntimeException("A TaskDescription's primary color should be opaque");
        }

        public void setBackgroundColor(int backgroundColor) {
            if (backgroundColor == 0 || Color.alpha(backgroundColor) == 255) {
                this.mColorBackground = backgroundColor;
                return;
            }
            throw new RuntimeException("A TaskDescription's background color should be opaque");
        }

        public void setStatusBarColor(int statusBarColor) {
            this.mStatusBarColor = statusBarColor;
        }

        public void setNavigationBarColor(int navigationBarColor) {
            this.mNavigationBarColor = navigationBarColor;
        }

        @UnsupportedAppUsage
        public void setIcon(Bitmap icon) {
            this.mIcon = icon;
        }

        public void setIcon(int iconRes) {
            this.mIconRes = iconRes;
        }

        public void setIconFilename(String iconFilename) {
            this.mIconFilename = iconFilename;
            this.mIcon = null;
        }

        public String getLabel() {
            return this.mLabel;
        }

        public Bitmap getIcon() {
            Bitmap bitmap = this.mIcon;
            if (bitmap != null) {
                return bitmap;
            }
            return loadTaskDescriptionIcon(this.mIconFilename, UserHandle.myUserId());
        }

        public int getIconResource() {
            return this.mIconRes;
        }

        public String getIconFilename() {
            return this.mIconFilename;
        }

        @UnsupportedAppUsage
        public Bitmap getInMemoryIcon() {
            return this.mIcon;
        }

        @UnsupportedAppUsage
        public static Bitmap loadTaskDescriptionIcon(String iconFilename, int userId) {
            if (iconFilename == null) {
                return null;
            }
            try {
                return ActivityManager.getTaskService().getTaskDescriptionIcon(iconFilename, userId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public int getPrimaryColor() {
            return this.mColorPrimary;
        }

        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        public int getBackgroundColor() {
            return this.mColorBackground;
        }

        public int getStatusBarColor() {
            return this.mStatusBarColor;
        }

        public int getNavigationBarColor() {
            return this.mNavigationBarColor;
        }

        public boolean getEnsureStatusBarContrastWhenTransparent() {
            return this.mEnsureStatusBarContrastWhenTransparent;
        }

        public void setEnsureStatusBarContrastWhenTransparent(boolean ensureStatusBarContrastWhenTransparent) {
            this.mEnsureStatusBarContrastWhenTransparent = ensureStatusBarContrastWhenTransparent;
        }

        public boolean getEnsureNavigationBarContrastWhenTransparent() {
            return this.mEnsureNavigationBarContrastWhenTransparent;
        }

        public void setEnsureNavigationBarContrastWhenTransparent(boolean ensureNavigationBarContrastWhenTransparent) {
            this.mEnsureNavigationBarContrastWhenTransparent = ensureNavigationBarContrastWhenTransparent;
        }

        public void saveToXml(XmlSerializer out) throws IOException {
            String str = this.mLabel;
            if (str != null) {
                out.attribute(null, ATTR_TASKDESCRIPTIONLABEL, str);
            }
            int i = this.mColorPrimary;
            if (i != 0) {
                out.attribute(null, ATTR_TASKDESCRIPTIONCOLOR_PRIMARY, Integer.toHexString(i));
            }
            i = this.mColorBackground;
            if (i != 0) {
                out.attribute(null, ATTR_TASKDESCRIPTIONCOLOR_BACKGROUND, Integer.toHexString(i));
            }
            str = this.mIconFilename;
            if (str != null) {
                out.attribute(null, ATTR_TASKDESCRIPTIONICON_FILENAME, str);
            }
            i = this.mIconRes;
            if (i != 0) {
                out.attribute(null, ATTR_TASKDESCRIPTIONICON_RESOURCE, Integer.toString(i));
            }
        }

        public void restoreFromXml(String attrName, String attrValue) {
            if (ATTR_TASKDESCRIPTIONLABEL.equals(attrName)) {
                setLabel(attrValue);
            } else if (ATTR_TASKDESCRIPTIONCOLOR_PRIMARY.equals(attrName)) {
                setPrimaryColor((int) Long.parseLong(attrValue, 16));
            } else if (ATTR_TASKDESCRIPTIONCOLOR_BACKGROUND.equals(attrName)) {
                setBackgroundColor((int) Long.parseLong(attrValue, 16));
            } else if (ATTR_TASKDESCRIPTIONICON_FILENAME.equals(attrName)) {
                setIconFilename(attrValue);
            } else if (ATTR_TASKDESCRIPTIONICON_RESOURCE.equals(attrName)) {
                setIcon(Integer.parseInt(attrValue, 10));
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            if (this.mLabel == null) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                dest.writeString(this.mLabel);
            }
            if (this.mIcon == null) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                this.mIcon.writeToParcel(dest, 0);
            }
            dest.writeInt(this.mIconRes);
            dest.writeInt(this.mColorPrimary);
            dest.writeInt(this.mColorBackground);
            dest.writeInt(this.mStatusBarColor);
            dest.writeInt(this.mNavigationBarColor);
            dest.writeBoolean(this.mEnsureStatusBarContrastWhenTransparent);
            dest.writeBoolean(this.mEnsureNavigationBarContrastWhenTransparent);
            if (this.mIconFilename == null) {
                dest.writeInt(0);
                return;
            }
            dest.writeInt(1);
            dest.writeString(this.mIconFilename);
        }

        public void readFromParcel(Parcel source) {
            String str = null;
            this.mLabel = source.readInt() > 0 ? source.readString() : null;
            this.mIcon = source.readInt() > 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(source) : null;
            this.mIconRes = source.readInt();
            this.mColorPrimary = source.readInt();
            this.mColorBackground = source.readInt();
            this.mStatusBarColor = source.readInt();
            this.mNavigationBarColor = source.readInt();
            this.mEnsureStatusBarContrastWhenTransparent = source.readBoolean();
            this.mEnsureNavigationBarContrastWhenTransparent = source.readBoolean();
            if (source.readInt() > 0) {
                str = source.readString();
            }
            this.mIconFilename = str;
        }

        public String toString() {
            String str;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TaskDescription Label: ");
            stringBuilder.append(this.mLabel);
            stringBuilder.append(" Icon: ");
            stringBuilder.append(this.mIcon);
            stringBuilder.append(" IconRes: ");
            stringBuilder.append(this.mIconRes);
            stringBuilder.append(" IconFilename: ");
            stringBuilder.append(this.mIconFilename);
            stringBuilder.append(" colorPrimary: ");
            stringBuilder.append(this.mColorPrimary);
            stringBuilder.append(" colorBackground: ");
            stringBuilder.append(this.mColorBackground);
            stringBuilder.append(" statusBarColor: ");
            stringBuilder.append(this.mStatusBarColor);
            String str2 = " (contrast when transparent)";
            String str3 = "";
            if (this.mEnsureStatusBarContrastWhenTransparent) {
                str = str2;
            } else {
                str = str3;
            }
            stringBuilder.append(str);
            stringBuilder.append(" navigationBarColor: ");
            stringBuilder.append(this.mNavigationBarColor);
            if (!this.mEnsureNavigationBarContrastWhenTransparent) {
                str2 = str3;
            }
            stringBuilder.append(str2);
            return stringBuilder.toString();
        }
    }

    public static class TaskSnapshot implements Parcelable {
        public static final Creator<TaskSnapshot> CREATOR = new Creator<TaskSnapshot>() {
            public TaskSnapshot createFromParcel(Parcel source) {
                return new TaskSnapshot(source, null);
            }

            public TaskSnapshot[] newArray(int size) {
                return new TaskSnapshot[size];
            }
        };
        private final ColorSpace mColorSpace;
        private final Rect mContentInsets;
        private final boolean mIsRealSnapshot;
        private final boolean mIsTranslucent;
        private final int mOrientation;
        private final boolean mReducedResolution;
        private final float mScale;
        private final GraphicBuffer mSnapshot;
        private final int mSystemUiVisibility;
        private final ComponentName mTopActivityComponent;
        private final int mWindowingMode;

        /* synthetic */ TaskSnapshot(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        public TaskSnapshot(ComponentName topActivityComponent, GraphicBuffer snapshot, ColorSpace colorSpace, int orientation, Rect contentInsets, boolean reducedResolution, float scale, boolean isRealSnapshot, int windowingMode, int systemUiVisibility, boolean isTranslucent) {
            this.mTopActivityComponent = topActivityComponent;
            this.mSnapshot = snapshot;
            this.mColorSpace = colorSpace.getId() < 0 ? ColorSpace.get(Named.SRGB) : colorSpace;
            this.mOrientation = orientation;
            this.mContentInsets = new Rect(contentInsets);
            this.mReducedResolution = reducedResolution;
            this.mScale = scale;
            this.mIsRealSnapshot = isRealSnapshot;
            this.mWindowingMode = windowingMode;
            this.mSystemUiVisibility = systemUiVisibility;
            this.mIsTranslucent = isTranslucent;
        }

        private TaskSnapshot(Parcel source) {
            ColorSpace colorSpace;
            this.mTopActivityComponent = ComponentName.readFromParcel(source);
            this.mSnapshot = (GraphicBuffer) source.readParcelable(null);
            int colorSpaceId = source.readInt();
            if (colorSpaceId < 0 || colorSpaceId >= Named.values().length) {
                colorSpace = ColorSpace.get(Named.SRGB);
            } else {
                colorSpace = ColorSpace.get(Named.values()[colorSpaceId]);
            }
            this.mColorSpace = colorSpace;
            this.mOrientation = source.readInt();
            this.mContentInsets = (Rect) source.readParcelable(null);
            this.mReducedResolution = source.readBoolean();
            this.mScale = source.readFloat();
            this.mIsRealSnapshot = source.readBoolean();
            this.mWindowingMode = source.readInt();
            this.mSystemUiVisibility = source.readInt();
            this.mIsTranslucent = source.readBoolean();
        }

        public ComponentName getTopActivityComponent() {
            return this.mTopActivityComponent;
        }

        @UnsupportedAppUsage
        public GraphicBuffer getSnapshot() {
            return this.mSnapshot;
        }

        public ColorSpace getColorSpace() {
            return this.mColorSpace;
        }

        @UnsupportedAppUsage
        public int getOrientation() {
            return this.mOrientation;
        }

        @UnsupportedAppUsage
        public Rect getContentInsets() {
            return this.mContentInsets;
        }

        @UnsupportedAppUsage
        public boolean isReducedResolution() {
            return this.mReducedResolution;
        }

        @UnsupportedAppUsage
        public boolean isRealSnapshot() {
            return this.mIsRealSnapshot;
        }

        public boolean isTranslucent() {
            return this.mIsTranslucent;
        }

        public int getWindowingMode() {
            return this.mWindowingMode;
        }

        public int getSystemUiVisibility() {
            return this.mSystemUiVisibility;
        }

        @UnsupportedAppUsage
        public float getScale() {
            return this.mScale;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            ComponentName.writeToParcel(this.mTopActivityComponent, dest);
            dest.writeParcelable(this.mSnapshot, 0);
            dest.writeInt(this.mColorSpace.getId());
            dest.writeInt(this.mOrientation);
            dest.writeParcelable(this.mContentInsets, 0);
            dest.writeBoolean(this.mReducedResolution);
            dest.writeFloat(this.mScale);
            dest.writeBoolean(this.mIsRealSnapshot);
            dest.writeInt(this.mWindowingMode);
            dest.writeInt(this.mSystemUiVisibility);
            dest.writeBoolean(this.mIsTranslucent);
        }

        public String toString() {
            GraphicBuffer graphicBuffer = this.mSnapshot;
            int height = 0;
            int width = graphicBuffer != null ? graphicBuffer.getWidth() : 0;
            GraphicBuffer graphicBuffer2 = this.mSnapshot;
            if (graphicBuffer2 != null) {
                height = graphicBuffer2.getHeight();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TaskSnapshot{ mTopActivityComponent=");
            stringBuilder.append(this.mTopActivityComponent.flattenToShortString());
            stringBuilder.append(" mSnapshot=");
            stringBuilder.append(this.mSnapshot);
            stringBuilder.append(" (");
            stringBuilder.append(width);
            stringBuilder.append("x");
            stringBuilder.append(height);
            stringBuilder.append(") mColorSpace=");
            stringBuilder.append(this.mColorSpace.toString());
            stringBuilder.append(" mOrientation=");
            stringBuilder.append(this.mOrientation);
            stringBuilder.append(" mContentInsets=");
            stringBuilder.append(this.mContentInsets.toShortString());
            stringBuilder.append(" mReducedResolution=");
            stringBuilder.append(this.mReducedResolution);
            stringBuilder.append(" mScale=");
            stringBuilder.append(this.mScale);
            stringBuilder.append(" mIsRealSnapshot=");
            stringBuilder.append(this.mIsRealSnapshot);
            stringBuilder.append(" mWindowingMode=");
            stringBuilder.append(this.mWindowingMode);
            stringBuilder.append(" mSystemUiVisibility=");
            stringBuilder.append(this.mSystemUiVisibility);
            stringBuilder.append(" mIsTranslucent=");
            stringBuilder.append(this.mIsTranslucent);
            return stringBuilder.toString();
        }
    }

    static final class UidObserver extends IUidObserver.Stub {
        final Context mContext;
        final OnUidImportanceListener mListener;

        UidObserver(OnUidImportanceListener listener, Context clientContext) {
            this.mListener = listener;
            this.mContext = clientContext;
        }

        public void onUidStateChanged(int uid, int procState, long procStateSeq) {
            this.mListener.onUidImportance(uid, RunningAppProcessInfo.procStateToImportanceForClient(procState, this.mContext));
        }

        public void onUidGone(int uid, boolean disabled) {
            this.mListener.onUidImportance(uid, 1000);
        }

        public void onUidActive(int uid) {
        }

        public void onUidIdle(int uid, boolean disabled) {
        }

        public void onUidCachedChanged(int uid, boolean cached) {
        }
    }

    public static final int processStateAmToProto(int amInt) {
        switch (amInt) {
            case -1:
                return 999;
            case 0:
                return 1000;
            case 1:
                return 1001;
            case 2:
                return 1002;
            case 3:
            case 5:
                return 1003;
            case 4:
                return 1020;
            case 6:
                return 1004;
            case 7:
                return 1005;
            case 8:
                return 1006;
            case 9:
                return 1007;
            case 10:
                return 1008;
            case 11:
                return 1009;
            case 12:
                return 1010;
            case 13:
                return 1011;
            case 14:
                return 1012;
            case 15:
                return 1013;
            case 16:
                return 1014;
            case 17:
                return 1015;
            case 18:
                return 1016;
            case 19:
                return 1017;
            case 20:
                return 1018;
            case 21:
                return 1019;
            default:
                return 998;
        }
    }

    public static final boolean isProcStateBackground(int procState) {
        return procState >= 9;
    }

    public static boolean isForegroundService(int procState) {
        return procState == 3 || procState == 5;
    }

    @UnsupportedAppUsage
    ActivityManager(Context context, Handler handler) {
        this.mContext = context;
    }

    public static final boolean isStartResultSuccessful(int result) {
        return result >= 0 && result <= 99;
    }

    public static final boolean isStartResultFatalError(int result) {
        return -100 <= result && result <= -1;
    }

    public int getFrontActivityScreenCompatMode() {
        try {
            return getTaskService().getFrontActivityScreenCompatMode();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setFrontActivityScreenCompatMode(int mode) {
        try {
            getTaskService().setFrontActivityScreenCompatMode(mode);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getPackageScreenCompatMode(String packageName) {
        try {
            return getTaskService().getPackageScreenCompatMode(packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setPackageScreenCompatMode(String packageName, int mode) {
        try {
            getTaskService().setPackageScreenCompatMode(packageName, mode);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean getPackageAskScreenCompat(String packageName) {
        try {
            return getTaskService().getPackageAskScreenCompat(packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setPackageAskScreenCompat(String packageName, boolean ask) {
        try {
            getTaskService().setPackageAskScreenCompat(packageName, ask);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getMemoryClass() {
        return staticGetMemoryClass();
    }

    @UnsupportedAppUsage
    public static int staticGetMemoryClass() {
        String str = "";
        String vmHeapSize = SystemProperties.get("dalvik.vm.heapgrowthlimit", str);
        if (vmHeapSize == null || str.equals(vmHeapSize)) {
            return staticGetLargeMemoryClass();
        }
        return Integer.parseInt(vmHeapSize.substring(0, vmHeapSize.length() - 1));
    }

    public int getLargeMemoryClass() {
        return staticGetLargeMemoryClass();
    }

    public static int staticGetLargeMemoryClass() {
        String vmHeapSize = SystemProperties.get("dalvik.vm.heapsize", "16m");
        return Integer.parseInt(vmHeapSize.substring(0, vmHeapSize.length() - 1));
    }

    public boolean isLowRamDevice() {
        return isLowRamDeviceStatic();
    }

    @UnsupportedAppUsage
    public static boolean isLowRamDeviceStatic() {
        return RoSystemProperties.CONFIG_LOW_RAM || (Build.IS_DEBUGGABLE && DEVELOPMENT_FORCE_LOW_RAM);
    }

    public static boolean isSmallBatteryDevice() {
        return RoSystemProperties.CONFIG_SMALL_BATTERY;
    }

    @UnsupportedAppUsage
    public static boolean isHighEndGfx() {
        return (isLowRamDeviceStatic() || RoSystemProperties.CONFIG_AVOID_GFX_ACCEL || Resources.getSystem().getBoolean(R.bool.config_avoidGfxAccel)) ? false : true;
    }

    public long getTotalRam() {
        MemInfoReader memreader = new MemInfoReader();
        memreader.readMemInfo();
        return memreader.getTotalSize();
    }

    @Deprecated
    @UnsupportedAppUsage
    public static int getMaxRecentTasksStatic() {
        return ActivityTaskManager.getMaxRecentTasksStatic();
    }

    @Deprecated
    public static int getMaxNumPictureInPictureActions() {
        return 3;
    }

    @Deprecated
    public List<RecentTaskInfo> getRecentTasks(int maxNum, int flags) throws SecurityException {
        if (maxNum >= 0) {
            try {
                return getTaskService().getRecentTasks(maxNum, flags, this.mContext.getUserId()).getList();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("The requested number of tasks should be >= 0");
    }

    public List<AppTask> getAppTasks() {
        ArrayList<AppTask> tasks = new ArrayList();
        try {
            List<IBinder> appTasks = getTaskService().getAppTasks(this.mContext.getPackageName());
            int numAppTasks = appTasks.size();
            for (int i = 0; i < numAppTasks; i++) {
                tasks.add(new AppTask(IAppTask.Stub.asInterface((IBinder) appTasks.get(i))));
            }
            return tasks;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Size getAppTaskThumbnailSize() {
        Size size;
        synchronized (this) {
            ensureAppTaskThumbnailSizeLocked();
            size = new Size(this.mAppTaskThumbnailSize.x, this.mAppTaskThumbnailSize.y);
        }
        return size;
    }

    private void ensureAppTaskThumbnailSizeLocked() {
        if (this.mAppTaskThumbnailSize == null) {
            try {
                this.mAppTaskThumbnailSize = getTaskService().getAppTaskThumbnailSize();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public int addAppTask(Activity activity, Intent intent, TaskDescription description, Bitmap thumbnail) {
        Point size;
        synchronized (this) {
            ensureAppTaskThumbnailSizeLocked();
            size = this.mAppTaskThumbnailSize;
        }
        int tw = thumbnail.getWidth();
        int th = thumbnail.getHeight();
        if (!(tw == size.x && th == size.y)) {
            float scale;
            Bitmap bm = Bitmap.createBitmap(size.x, size.y, thumbnail.getConfig());
            float dx = 0.0f;
            if (size.x * tw > size.y * th) {
                scale = ((float) size.x) / ((float) th);
                dx = (((float) size.y) - (((float) tw) * scale)) * 0.5f;
            } else {
                scale = ((float) size.y) / ((float) tw);
                float dy = (((float) size.x) - (((float) th) * scale)) * 0.5f;
            }
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            matrix.postTranslate((float) ((int) (0.5f + dx)), 0.0f);
            Canvas canvas = new Canvas(bm);
            canvas.drawBitmap(thumbnail, matrix, null);
            canvas.setBitmap(null);
            thumbnail = bm;
        }
        if (description == null) {
            description = new TaskDescription();
        }
        try {
            return getTaskService().addAppTask(activity.getActivityToken(), intent, description, thumbnail);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public List<RunningTaskInfo> getRunningTasks(int maxNum) throws SecurityException {
        try {
            return getTaskService().getTasks(maxNum);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void moveTaskToFront(int taskId, int flags) {
        moveTaskToFront(taskId, flags, null);
    }

    public void moveTaskToFront(int taskId, int flags, Bundle options) {
        try {
            getTaskService().moveTaskToFront(ActivityThread.currentActivityThread().getApplicationThread(), this.mContext.getPackageName(), taskId, flags, options);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isActivityStartAllowedOnDisplay(Context context, int displayId, Intent intent) {
        try {
            return getTaskService().isActivityStartAllowedOnDisplay(displayId, intent, intent.resolveTypeIfNeeded(context.getContentResolver()), context.getUserId());
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    @Deprecated
    public List<RunningServiceInfo> getRunningServices(int maxNum) throws SecurityException {
        try {
            return getService().getServices(maxNum, 0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public PendingIntent getRunningServiceControlPanel(ComponentName service) throws SecurityException {
        try {
            return getService().getRunningServiceControlPanel(service);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void getMemoryInfo(MemoryInfo outInfo) {
        try {
            getService().getMemoryInfo(outInfo);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public boolean clearApplicationUserData(String packageName, IPackageDataObserver observer) {
        try {
            return getService().clearApplicationUserData(packageName, false, observer, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean clearApplicationUserData() {
        return clearApplicationUserData(this.mContext.getPackageName(), null);
    }

    @Deprecated
    public ParceledListSlice<GrantedUriPermission> getGrantedUriPermissions(String packageName) {
        return ((UriGrantsManager) this.mContext.getSystemService(Context.URI_GRANTS_SERVICE)).getGrantedUriPermissions(packageName);
    }

    @Deprecated
    public void clearGrantedUriPermissions(String packageName) {
        ((UriGrantsManager) this.mContext.getSystemService(Context.URI_GRANTS_SERVICE)).clearGrantedUriPermissions(packageName);
    }

    public List<ProcessErrorStateInfo> getProcessesInErrorState() {
        try {
            return getService().getProcessesInErrorState();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ApplicationInfo> getRunningExternalApplications() {
        try {
            return getService().getRunningExternalApplications();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isBackgroundRestricted() {
        try {
            return getService().isBackgroundRestricted(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean setProcessMemoryTrimLevel(String process, int userId, int level) {
        try {
            return getService().setProcessMemoryTrimLevel(process, userId, level);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<RunningAppProcessInfo> getRunningAppProcesses() {
        try {
            return getService().getRunningAppProcesses();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public int getPackageImportance(String packageName) {
        try {
            return RunningAppProcessInfo.procStateToImportanceForClient(getService().getPackageProcessState(packageName, this.mContext.getOpPackageName()), this.mContext);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public int getUidImportance(int uid) {
        try {
            return RunningAppProcessInfo.procStateToImportanceForClient(getService().getUidProcessState(uid, this.mContext.getOpPackageName()), this.mContext);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void addOnUidImportanceListener(OnUidImportanceListener listener, int importanceCutpoint) {
        synchronized (this) {
            if (this.mImportanceListeners.containsKey(listener)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Listener already registered: ");
                stringBuilder.append(listener);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            UidObserver observer = new UidObserver(listener, this.mContext);
            try {
                getService().registerUidObserver(observer, 3, RunningAppProcessInfo.importanceToProcState(importanceCutpoint), this.mContext.getOpPackageName());
                this.mImportanceListeners.put(listener, observer);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @SystemApi
    public void removeOnUidImportanceListener(OnUidImportanceListener listener) {
        synchronized (this) {
            UidObserver observer = (UidObserver) this.mImportanceListeners.remove(listener);
            if (observer != null) {
                try {
                    getService().unregisterUidObserver(observer);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Listener not registered: ");
            stringBuilder.append(listener);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static void getMyMemoryState(RunningAppProcessInfo outState) {
        try {
            getService().getMyMemoryState(outState);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public android.os.Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) {
        try {
            return getService().getProcessMemoryInfo(pids);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void restartPackage(String packageName) {
        killBackgroundProcesses(packageName);
    }

    public void killBackgroundProcesses(String packageName) {
        try {
            getService().killBackgroundProcesses(packageName, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void killUid(int uid, String reason) {
        try {
            getService().killUid(UserHandle.getAppId(uid), UserHandle.getUserId(uid), reason);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public void forceStopPackageAsUser(String packageName, int userId) {
        try {
            getService().forceStopPackage(packageName, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void forceStopPackage(String packageName) {
        forceStopPackageAsUser(packageName, this.mContext.getUserId());
    }

    @SystemApi
    public void setDeviceLocales(LocaleList locales) {
        LocalePicker.updateLocales(locales);
    }

    @SystemApi
    public Collection<Locale> getSupportedLocales() {
        ArrayList<Locale> locales = new ArrayList();
        for (String localeTag : LocalePicker.getSupportedLocales(this.mContext)) {
            locales.add(Locale.forLanguageTag(localeTag));
        }
        return locales;
    }

    public ConfigurationInfo getDeviceConfigurationInfo() {
        try {
            return getTaskService().getDeviceConfigurationInfo();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getLauncherLargeIconDensity() {
        Resources res = this.mContext.getResources();
        int density = res.getDisplayMetrics().densityDpi;
        if (res.getConfiguration().smallestScreenWidthDp < 600) {
            return density;
        }
        if (density == 120) {
            return 160;
        }
        if (density == 160) {
            return 240;
        }
        if (density == 213 || density == 240) {
            return 320;
        }
        if (density == 320) {
            return 480;
        }
        if (density != 480) {
            return (int) ((((float) density) * 1.5f) + 0.5f);
        }
        return 640;
    }

    public int getLauncherLargeIconSize() {
        return getLauncherLargeIconSizeInner(this.mContext);
    }

    static int getLauncherLargeIconSizeInner(Context context) {
        Resources res = context.getResources();
        int size = res.getDimensionPixelSize(17104896);
        if (res.getConfiguration().smallestScreenWidthDp < 600) {
            return size;
        }
        int density = res.getDisplayMetrics().densityDpi;
        if (density == 120) {
            return (size * 160) / 120;
        }
        if (density == 160) {
            return (size * 240) / 160;
        }
        if (density == 213) {
            return (size * 320) / 240;
        }
        if (density == 240) {
            return (size * 320) / 240;
        }
        if (density == 320) {
            return (size * 480) / 320;
        }
        if (density != 480) {
            return (int) ((((float) size) * 1.5f) + 0.5f);
        }
        return ((size * 320) * 2) / 480;
    }

    public static boolean isUserAMonkey() {
        try {
            return getService().isUserAMonkey();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public static boolean isRunningInTestHarness() {
        return SystemProperties.getBoolean("ro.test_harness", false);
    }

    public static boolean isRunningInUserTestHarness() {
        return SystemProperties.getBoolean("persist.sys.test_harness", false);
    }

    public void alwaysShowUnsupportedCompileSdkWarning(ComponentName activity) {
        try {
            getTaskService().alwaysShowUnsupportedCompileSdkWarning(activity);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public static int checkComponentPermission(String permission, int uid, int owningUid, boolean exported) {
        int appId = UserHandle.getAppId(uid);
        if (appId == 0 || appId == 1000) {
            return 0;
        }
        if (UserHandle.isIsolated(uid)) {
            return -1;
        }
        if (owningUid >= 0 && UserHandle.isSameApp(uid, owningUid)) {
            return 0;
        }
        if (!exported) {
            return -1;
        }
        if (permission == null) {
            return 0;
        }
        try {
            return AppGlobals.getPackageManager().checkUidPermission(permission, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static int checkUidPermission(String permission, int uid) {
        try {
            return AppGlobals.getPackageManager().checkUidPermission(permission, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, String name, String callerPackage) {
        if (UserHandle.getUserId(callingUid) == userId) {
            return userId;
        }
        try {
            return getService().handleIncomingUser(callingPid, callingUid, userId, allowAll, requireFull, name, callerPackage);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public static int getCurrentUser() {
        try {
            UserInfo ui = getService().getCurrentUser();
            return ui != null ? ui.id : 0;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public boolean switchUser(int userid) {
        try {
            return getService().switchUser(userid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean switchUser(UserHandle user) {
        if (user != null) {
            return switchUser(user.getIdentifier());
        }
        throw new IllegalArgumentException("UserHandle cannot be null.");
    }

    public static void logoutCurrentUser() {
        int currentUser = getCurrentUser();
        if (currentUser != 0) {
            try {
                getService().switchUser(0);
                getService().stopUser(currentUser, false, null);
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }
    }

    @UnsupportedAppUsage
    public boolean isUserRunning(int userId) {
        try {
            return getService().isUserRunning(userId, 0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isVrModePackageEnabled(ComponentName component) {
        try {
            return getService().isVrModePackageEnabled(component);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void dumpPackageState(FileDescriptor fd, String packageName) {
        dumpPackageStateStatic(fd, packageName);
    }

    public static void dumpPackageStateStatic(FileDescriptor fd, String packageName) {
        PrintWriter pw = new FastPrintWriter(new FileOutputStream(fd));
        dumpService(pw, fd, r5, new String[]{packageName});
        pw.println();
        dumpService(pw, fd, Context.ACTIVITY_SERVICE, new String[]{"-a", "package", packageName});
        pw.println();
        dumpService(pw, fd, "meminfo", new String[]{"--local", "--package", packageName});
        pw.println();
        dumpService(pw, fd, ProcessStats.SERVICE_NAME, new String[]{packageName});
        pw.println();
        dumpService(pw, fd, Context.USAGE_STATS_SERVICE, new String[]{packageName});
        pw.println();
        dumpService(pw, fd, BatteryStats.SERVICE_NAME, new String[]{packageName});
        pw.flush();
    }

    public static boolean isSystemReady() {
        if (!sSystemReady) {
            if (ActivityThread.isSystem()) {
                sSystemReady = ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).isSystemReady();
            } else {
                sSystemReady = true;
            }
        }
        return sSystemReady;
    }

    public static void broadcastStickyIntent(Intent intent, int userId) {
        broadcastStickyIntent(intent, -1, userId);
    }

    public static void broadcastStickyIntent(Intent intent, int appOp, int userId) {
        try {
            getService().broadcastIntent(null, intent, null, null, -1, null, null, null, appOp, null, false, true, userId);
        } catch (RemoteException e) {
        }
    }

    public static void resumeAppSwitches() throws RemoteException {
        getService().resumeAppSwitches();
    }

    public static void noteWakeupAlarm(PendingIntent ps, WorkSource workSource, int sourceUid, String sourcePkg, String tag) {
        try {
            getService().noteWakeupAlarm(ps != null ? ps.getTarget() : null, workSource, sourceUid, sourcePkg, tag);
        } catch (RemoteException e) {
        }
    }

    public static void noteAlarmStart(PendingIntent ps, WorkSource workSource, int sourceUid, String tag) {
        try {
            getService().noteAlarmStart(ps != null ? ps.getTarget() : null, workSource, sourceUid, tag);
        } catch (RemoteException e) {
        }
    }

    public static void noteAlarmFinish(PendingIntent ps, WorkSource workSource, int sourceUid, String tag) {
        try {
            getService().noteAlarmFinish(ps != null ? ps.getTarget() : null, workSource, sourceUid, tag);
        } catch (RemoteException e) {
        }
    }

    @UnsupportedAppUsage
    public static IActivityManager getService() {
        return (IActivityManager) IActivityManagerSingleton.get();
    }

    private static IActivityTaskManager getTaskService() {
        return ActivityTaskManager.getService();
    }

    private static void dumpService(PrintWriter pw, FileDescriptor fd, String name, String[] args) {
        pw.print("DUMP OF SERVICE ");
        pw.print(name);
        pw.println(":");
        IBinder service = ServiceManager.checkService(name);
        if (service == null) {
            pw.println("  (Service not found)");
            pw.flush();
            return;
        }
        pw.flush();
        String str = "Failure dumping service:";
        if (service instanceof Binder) {
            try {
                service.dump(fd, args);
            } catch (Throwable e) {
                pw.println(str);
                e.printStackTrace(pw);
                pw.flush();
            }
        } else {
            TransferPipe tp = null;
            try {
                pw.flush();
                tp = new TransferPipe();
                tp.setBufferPrefix("  ");
                service.dumpAsync(tp.getWriteFd().getFileDescriptor(), args);
                tp.go(fd, JobInfo.MIN_BACKOFF_MILLIS);
            } catch (Throwable e2) {
                if (tp != null) {
                    tp.kill();
                }
                pw.println(str);
                e2.printStackTrace(pw);
            }
        }
    }

    public void setWatchHeapLimit(long pssSize) {
        try {
            getService().setDumpHeapDebugLimit(null, 0, pssSize, this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearWatchHeapLimit() {
        try {
            getService().setDumpHeapDebugLimit(null, 0, 0, null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean isInLockTaskMode() {
        return getLockTaskModeState() != 0;
    }

    public int getLockTaskModeState() {
        try {
            return getTaskService().getLockTaskModeState();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void setVrThread(int tid) {
        try {
            getTaskService().setVrThread(tid);
        } catch (RemoteException e) {
        }
    }

    @SystemApi
    public static void setPersistentVrThread(int tid) {
        try {
            getService().setPersistentVrThread(tid);
        } catch (RemoteException e) {
        }
    }

    public void scheduleApplicationInfoChanged(List<String> packages, int userId) {
        try {
            getService().scheduleApplicationInfoChanged(packages, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void registerMiuiAppTransitionAnimationHelper(IMiuiAppTransitionAnimationHelper helper, int displayid) {
        if (helper != null) {
            try {
                getService().registerMiuiAppTransitionAnimationHelper(helper, displayid);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void unregisterMiuiAppTransitionAnimationHelper(int displayid) {
        try {
            getService().unregisterMiuiAppTransitionAnimationHelper(displayid);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setResizeWhiteList(List<String> whiteList) {
        try {
            getService().setResizeWhiteList(whiteList);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setResizeBlackList(List<String> blackList) {
        try {
            getService().setResizeBlackList(blackList);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
