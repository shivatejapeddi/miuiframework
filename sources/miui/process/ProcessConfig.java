package miui.process;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProcessConfig implements Parcelable {
    public static final Creator<ProcessConfig> CREATOR = new Creator<ProcessConfig>() {
        public ProcessConfig createFromParcel(Parcel source) {
            return new ProcessConfig(source, null);
        }

        public ProcessConfig[] newArray(int size) {
            return new ProcessConfig[size];
        }
    };
    private static final int INVALID_ADJ = -10000;
    private static final int INVALID_TASK_ID = -1;
    private static final int INVALID_UID = -1;
    private static final int INVALID_USER_ID = -10000;
    public static final int KILL_LEVEL_FORCE_STOP = 104;
    public static final int KILL_LEVEL_KILL = 103;
    public static final int KILL_LEVEL_KILL_BACKGROUND = 102;
    public static final int KILL_LEVEL_TRIM_MEMORY = 101;
    public static final int KILL_LEVEL_UNKNOWN = 100;
    public static final int POLICY_AUTO_IDLE_KILL = 13;
    public static final int POLICY_AUTO_LOCK_OFF_CLEAN = 15;
    public static final int POLICY_AUTO_LOCK_OFF_CLEAN_BY_PRIORITY = 17;
    public static final int POLICY_AUTO_POWER_KILL = 11;
    public static final int POLICY_AUTO_SLEEP_CLEAN = 14;
    public static final int POLICY_AUTO_SYSTEM_ABNORMAL_CLEAN = 16;
    public static final int POLICY_AUTO_THERMAL_KILL = 12;
    public static final int POLICY_FORCE_CLEAN = 2;
    public static final int POLICY_GAME_CLEAN = 4;
    public static final int POLICY_GARBAGE_CLEAN = 6;
    public static final int POLICY_LOCK_SCREEN_CLEAN = 3;
    public static final int POLICY_ONE_KEY_CLEAN = 1;
    public static final int POLICY_OPTIMIZATION_CLEAN = 5;
    public static final int POLICY_SWIPE_UP_CLEAN = 7;
    public static final int POLICY_USER_DEFINED = 10;
    private String mKillingPackage;
    private ArrayMap<Integer, List<String>> mKillingPackageMaps;
    private int mPolicy;
    private int mPriority;
    private String mReason;
    private boolean mRemoveTaskNeeded;
    private List<Integer> mRemovingTaskIdList;
    private int mTaskId;
    private int mUid;
    private int mUserId;
    private List<String> mWhiteList;
    private long mkillingClockTime;

    /* synthetic */ ProcessConfig(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public ProcessConfig(int policy) {
        this.mUserId = -10000;
        this.mTaskId = -1;
        this.mUid = -1;
        this.mPriority = -10000;
        this.mPolicy = policy;
    }

    public ProcessConfig(int policy, String killingPackage, int uid) {
        this(policy);
        this.mKillingPackage = killingPackage;
        this.mUid = uid;
    }

    public ProcessConfig(int policy, String killingPackage, int userId, int taskId) {
        this(policy);
        this.mKillingPackage = killingPackage;
        this.mUserId = userId;
        this.mTaskId = taskId;
    }

    public ProcessConfig(int policy, int userId, ArrayMap<Integer, List<String>> killingPackageMaps) {
        this(policy);
        this.mUserId = userId;
        this.mKillingPackageMaps = killingPackageMaps;
    }

    public ProcessConfig(int policy, int userId, ArrayMap<Integer, List<String>> killingPackageMaps, String reason) {
        this(policy, userId, (ArrayMap) killingPackageMaps);
        this.mReason = reason;
    }

    public int getPolicy() {
        return this.mPolicy;
    }

    public List<String> getWhiteList() {
        return this.mWhiteList;
    }

    public String getKillingPackage() {
        return this.mKillingPackage;
    }

    public String getReason() {
        return this.mReason;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public int getUid() {
        return this.mUid;
    }

    public int getTaskId() {
        return this.mTaskId;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public boolean isRemoveTaskNeeded() {
        return this.mRemoveTaskNeeded;
    }

    public void setRemovingTaskIdList(List<Integer> taskIdList) {
        this.mRemovingTaskIdList = taskIdList;
    }

    public List<Integer> getRemovingTaskIdList() {
        return this.mRemovingTaskIdList;
    }

    public ArrayMap<Integer, List<String>> getKillingPackageMaps() {
        return this.mKillingPackageMaps;
    }

    public void setWhiteList(List<String> whiteList) {
        this.mWhiteList = whiteList;
    }

    public void setKillingPackage(String killingPackage) {
        this.mKillingPackage = killingPackage;
    }

    public void setKillingPackageMaps(ArrayMap<Integer, List<String>> killingPackageMaps) {
        this.mKillingPackageMaps = killingPackageMaps;
    }

    public void setReason(String reason) {
        this.mReason = reason;
    }

    public void setUserId(int userId) {
        this.mUserId = userId;
    }

    public void setUid(int uid) {
        this.mUid = uid;
    }

    public void setTaskId(int taskId) {
        this.mTaskId = taskId;
    }

    public void setPriority(int priority) {
        this.mPriority = priority;
    }

    public void setRemoveTaskNeeded(boolean removeTaskNeeded) {
        this.mRemoveTaskNeeded = removeTaskNeeded;
    }

    public void setKillingClockTime(long killingClockTime) {
        this.mkillingClockTime = killingClockTime;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mPolicy);
        dest.writeString(this.mReason);
        dest.writeString(this.mKillingPackage);
        dest.writeInt(this.mUserId);
        dest.writeInt(this.mTaskId);
        dest.writeInt(this.mUid);
        dest.writeInt(this.mPriority);
        dest.writeInt(this.mRemoveTaskNeeded);
        if (this.mWhiteList != null) {
            dest.writeInt(1);
            dest.writeList(this.mWhiteList);
        } else {
            dest.writeInt(0);
        }
        if (this.mRemovingTaskIdList != null) {
            dest.writeInt(1);
            dest.writeList(this.mRemovingTaskIdList);
        } else {
            dest.writeInt(0);
        }
        if (this.mKillingPackageMaps != null) {
            for (int i = 0; i < this.mKillingPackageMaps.size(); i++) {
                Integer level = (Integer) this.mKillingPackageMaps.keyAt(i);
                List<String> packages = (List) this.mKillingPackageMaps.valueAt(i);
                if (packages != null) {
                    dest.writeInt(1);
                    dest.writeInt(level.intValue());
                    dest.writeList(packages);
                }
            }
        }
        dest.writeInt(0);
    }

    public void readFromParcel(Parcel source) {
        this.mPolicy = source.readInt();
        this.mReason = source.readString();
        this.mKillingPackage = source.readString();
        this.mUserId = source.readInt();
        this.mTaskId = source.readInt();
        this.mUid = source.readInt();
        this.mPriority = source.readInt();
        this.mRemoveTaskNeeded = source.readInt() != 0;
        if (source.readInt() != 0) {
            this.mWhiteList = source.readArrayList(List.class.getClassLoader());
        }
        if (source.readInt() != 0) {
            this.mRemovingTaskIdList = source.readArrayList(List.class.getClassLoader());
        }
        while (source.readInt() != 0) {
            Integer level = Integer.valueOf(source.readInt());
            List<String> packages = source.readArrayList(List.class.getClassLoader());
            if (this.mKillingPackageMaps == null) {
                this.mKillingPackageMaps = new ArrayMap();
            }
            this.mKillingPackageMaps.put(level, packages);
        }
    }

    private ProcessConfig(Parcel source) {
        this.mUserId = -10000;
        this.mTaskId = -1;
        this.mUid = -1;
        this.mPriority = -10000;
        readFromParcel(source);
    }

    public boolean isUserIdInvalid() {
        return this.mUserId == -10000;
    }

    public boolean isTaskIdInvalid() {
        return this.mTaskId == -1;
    }

    public boolean isUidInvalid() {
        return this.mUid <= -1;
    }

    public boolean isPriorityInvalid() {
        return this.mPriority == -10000;
    }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ProcessConfig{mPolicy=");
        stringBuilder.append(this.mPolicy);
        stringBuilder.append(", mReason='");
        stringBuilder.append(this.mReason);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mKillingPackage='");
        stringBuilder.append(this.mKillingPackage);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mUserId=");
        stringBuilder.append(this.mUserId);
        stringBuilder.append(", mTaskId=");
        stringBuilder.append(this.mTaskId);
        stringBuilder.append(", mUid=");
        stringBuilder.append(this.mUid);
        stringBuilder.append(", mPriority=");
        stringBuilder.append(this.mPriority);
        stringBuilder.append(", mWhiteList=");
        stringBuilder.append(this.mWhiteList);
        stringBuilder.append(", mKillingPackageMaps=");
        stringBuilder.append(this.mKillingPackageMaps);
        stringBuilder.append(", mRemoveTaskNeeded=");
        stringBuilder.append(this.mRemoveTaskNeeded);
        stringBuilder.append(", mRemovingTaskIdList=");
        stringBuilder.append(this.mRemovingTaskIdList);
        stringBuilder.append(", mkillingClockTime=");
        stringBuilder.append(sdf.format(new Date(this.mkillingClockTime)));
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
