package com.miui.whetstone.strategy;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;

public class WhetstonePackageInfo implements Parcelable, Cloneable {
    public static final int CAP_LOWMEM_KILL = 1;
    public static final int CAP_LOWMEM_THRESOLD_KILL = 2;
    public static final Creator<WhetstonePackageInfo> CREATOR = new Creator<WhetstonePackageInfo>() {
        public WhetstonePackageInfo createFromParcel(Parcel source) {
            return new WhetstonePackageInfo(source, null);
        }

        public WhetstonePackageInfo[] newArray(int size) {
            return new WhetstonePackageInfo[size];
        }
    };
    public static final int FLAG_ANDROID_PERSISTENT_APP = 262144;
    public static final int FLAG_APP_STOP = 1073741824;
    public static final int FLAG_APP_SYSTEM = Integer.MIN_VALUE;
    public static final int FLAG_BACKGROUND_START = 256;
    public static final int FLAG_BITMAPCACHE = 1;
    public static final int FLAG_DEAL_SCHEDULE = 4096;
    public static final int FLAG_DESTORYACTIVITY = 4;
    public static final int FLAG_DISABLEOPENGL = 2;
    public static final int FLAG_DISABLE_WAKELOCK = 16384;
    public static final int FLAG_ONEKEY_CLEAN_NO_UI_WHITE = 131072;
    public static final int FLAG_ONEKEY_CLEAN_WHITE = 65536;
    public static final int FLAG_SOFT_RESET = 1024;
    public static final int FLAG_TRIMBACKGOUNDAPPS = 32;
    public static final int FLAG_TRIMHEAPS = 64;
    public static final int FLAG_TRIMHEAPSIZE = 8192;
    public static final int FLAG_TRIMPROCESS_BY_ACTIVITY = 2048;
    public static final int FLAG_TRIMSERVICES = 16;
    public static final int FLAG_TRIM_OPENGL = 512;
    public static final int FLAG_UPDATESETTING = 8;
    public static final int FLAG_ZRAM = 128;
    public static final int TRIM_LEVEL_WHETSTONE_HEAP = 1001;
    public static final int TRIM_LEVLE_WHETSTONE_BITMAPCACHE = 1000;
    public static final int TRIM_LEVLE_WHETSTONE_DEFAULT = 1100;
    public static final int TRIM_LEVLE_WHETSTONE_NATIVE = 1002;
    public static final int TYPE_AUTO_START = 64;
    public static final int TYPE_GAME = 2;
    public static final int TYPE_IM_PUSH = 1;
    public static final int TYPE_INPUT_METHOD = 32;
    public static final int TYPE_LARGE_MEMORY = 128;
    public static final int TYPE_MUSIC = 4;
    public static final int TYPE_NOTE = 8;
    public static final int TYPE_NOTIFICATION = 16;
    public int capacity;
    public int flag;
    public long mBackGroundTime;
    public int mClearScore;
    public int mClearType;
    public int mExceptionAnrCount;
    public int mExceptionCrashCount;
    public int mExceptionTotalCount;
    public long mFirstExceptionTime;
    public long mForeGroundStartCount;
    public long mForeGroundTime;
    public int mHistoryOrder;
    public int mLifeOrder;
    public int mPermission;
    public int mPromoteLevel;
    public boolean mScreenOffClear;
    public long mStartTime;
    public long mTotalForeGroundTime;
    public int nonUiMemoryThresold;
    public String packageName;
    public String samePackageList;
    public int type;
    public int uiMemoryThresold;
    public int uid;

    public WhetstonePackageInfo(String name, boolean system) {
        this.packageName = name;
        this.flag = system ? Integer.MIN_VALUE : 0;
        this.type = 0;
        this.mScreenOffClear = false;
        this.mForeGroundStartCount = 0;
        this.mForeGroundTime = 0;
        this.mBackGroundTime = 0;
        this.mTotalForeGroundTime = 0;
        this.mHistoryOrder = 0;
        this.mLifeOrder = -1;
        this.mClearType = 0;
        this.mClearScore = 0;
        this.mPermission = -1;
        this.mPromoteLevel = -1;
    }

    public WhetstonePackageInfo(String name, String samePList, int packageCapability, int uiThreshold, int nonUiThreshold, boolean screenOffClear) {
        this.packageName = name;
        this.samePackageList = samePList;
        this.capacity = packageCapability;
        this.uiMemoryThresold = uiThreshold;
        this.nonUiMemoryThresold = nonUiThreshold;
        this.mScreenOffClear = screenOffClear;
        this.flag = 0;
        this.type = 0;
        this.mForeGroundStartCount = 0;
        this.mForeGroundTime = 0;
        this.mBackGroundTime = 0;
        this.mTotalForeGroundTime = 0;
        this.mHistoryOrder = 0;
        this.mLifeOrder = -1;
        this.mClearType = 0;
        this.mClearScore = 0;
        this.mPermission = -1;
        this.mPromoteLevel = -1;
    }

    public void addForeGroundStartCount() {
        this.mForeGroundStartCount++;
    }

    public long getForeGroundStartCount() {
        return this.mForeGroundStartCount;
    }

    public void setForeGroundStartCount(long count) {
        this.mForeGroundStartCount = count;
    }

    public void setForeGroundStartTime() {
        this.mForeGroundTime = SystemClock.elapsedRealtime();
    }

    public void setBackGroundStartTime() {
        this.mBackGroundTime = SystemClock.elapsedRealtime();
    }

    public void endForeGround() {
        setBackGroundStartTime();
        this.mTotalForeGroundTime += SystemClock.elapsedRealtime() - this.mForeGroundTime;
    }

    public void startForceGround() {
        clearBackGroundStartTime();
        setForeGroundStartTime();
        addForeGroundStartCount();
    }

    public void clearForeGroundStartTime() {
        this.mForeGroundTime = 0;
    }

    public void clearBackGroundStartTime() {
        this.mBackGroundTime = 0;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return this.uid;
    }

    public boolean isEnable(int type) {
        return (this.flag & type) != 0;
    }

    public boolean isType(int flag) {
        return (this.type & flag) != 0;
    }

    public void addType(int flag) {
        this.type |= flag;
    }

    public void addFlag(int flg) {
        this.flag |= flg;
    }

    public boolean isSystemApp() {
        return (this.flag & Integer.MIN_VALUE) != 0;
    }

    public boolean isDisableWakelock() {
        return (this.flag & 16384) != 0;
    }

    public void setScore(int score) {
        this.mClearScore = score;
    }

    public int getScore() {
        return this.mClearScore;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeInt(this.flag);
        dest.writeInt(this.type);
        dest.writeInt(this.uid);
        dest.writeInt(this.mPermission);
        dest.writeInt(this.mPromoteLevel);
    }

    public void readFromParcel(Parcel source) {
        this.packageName = source.readString();
        this.flag = source.readInt();
        this.type = source.readInt();
        this.uid = source.readInt();
        this.mPermission = source.readInt();
        this.mPromoteLevel = source.readInt();
    }

    private WhetstonePackageInfo(Parcel source) {
        readFromParcel(source);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{WhetstonePackageInfo#PacakgeName:");
        stringBuilder.append(this.packageName);
        builder.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(" uid:");
        stringBuilder.append(this.uid);
        builder.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(" uiMemoryThresold:");
        stringBuilder.append(this.uiMemoryThresold);
        builder.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(" nonUiMemoryThresold:");
        stringBuilder.append(this.nonUiMemoryThresold);
        builder.append(stringBuilder.toString());
        String hexFlag = Integer.toHexString(this.flag);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" Flag:");
        stringBuilder2.append(this.flag);
        stringBuilder2.append(",0x");
        stringBuilder2.append(hexFlag);
        stringBuilder2.append(" [");
        builder.append(stringBuilder2.toString());
        if ((this.flag & 1) != 0) {
            builder.append(",BitmapCache");
        }
        if ((this.flag & 4) != 0) {
            builder.append(",DestoryActivity");
        }
        if ((this.flag & 2) != 0) {
            builder.append(",isOPENGLDiable");
        }
        if ((this.flag & 64) != 0) {
            builder.append(",TRIMHEAPS");
        }
        if ((this.flag & 512) != 0) {
            builder.append(",TRIM_OPENGL");
        }
        if ((this.flag & 1024) != 0) {
            builder.append(",SOFT_RESET");
        }
        if ((this.flag & Integer.MIN_VALUE) != 0) {
            builder.append(",APP_SYSTEM");
        }
        if ((this.flag & 128) != 0) {
            builder.append(",ZRAM");
        }
        if ((this.flag & 2048) != 0) {
            builder.append(",TRIMPROCESS_BY_ACTIVITY");
        }
        if ((this.flag & 4096) != 0) {
            builder.append(",FLAG_DEAL_SCHEDULE");
        }
        if ((this.flag & 8192) != 0) {
            builder.append(",FLAG_TRIMHEAPSIZE");
        }
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("] Type:");
        stringBuilder2.append(this.type);
        stringBuilder2.append("[");
        builder.append(stringBuilder2.toString());
        if ((this.type & 1) != 0) {
            builder.append(",IM_PUSH");
        }
        if ((this.type & 2) != 0) {
            builder.append(",Game");
        }
        if ((this.type & 8) != 0) {
            builder.append(",note");
        }
        if ((this.type & 16) != 0) {
            builder.append(",notification");
        }
        if ((this.type & 64) != 0) {
            builder.append(",AUTO_START");
        }
        if ((this.type & 128) != 0) {
            builder.append(",TYPE_LARGE_MEMORY");
        }
        builder.append("] }");
        return builder.toString();
    }

    public Object clone() {
        try {
            return (WhetstonePackageInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
