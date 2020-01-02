package miui.mqsas.sdk.event;

import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BootEvent implements Parcelable {
    public static final String ACTION_BOOT_AMS_READY = "AmsReady";
    public static final String ACTION_BOOT_BOOT_COMPLETE = "BootComplete";
    public static final String ACTION_BOOT_DEXOPT = "Dexopt";
    public static final String ACTION_BOOT_PMS_SCAN = "PmsScan";
    public static final String ACTION_BOOT_SYSTEM_RUN = "SystemRun";
    public static final String ACTION_BOOT_UI_READY = "UIReady";
    public static final Creator<BootEvent> CREATOR = new Creator<BootEvent>() {
        public BootEvent createFromParcel(Parcel in) {
            return new BootEvent(in, null);
        }

        public BootEvent[] newArray(int size) {
            return new BootEvent[size];
        }
    };
    public static final int TYPE_BOOT_FIRST = 2;
    public static final int TYPE_BOOT_NORMAL = 1;
    public static final int TYPE_BOOT_OTA = 3;
    private int mBootType;
    private String mDetailAmsReady;
    private String mDetailBootComplete;
    private String mDetailDexopt;
    private String mDetailPmsScan;
    private String mDetailSystemRun;
    private String mDetailUIReady;
    private String mMiuiVersion;
    private long mPeriodAmsReady;
    private long mPeriodBootComplete;
    private long mPeriodDexopt;
    private long mPeriodPmsScan;
    private long mPeriodSystemRun;
    private long mPeriodUIReady;
    private long mTimeStamp;
    private int mType;

    /* synthetic */ BootEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public BootEvent() {
        this.mType = -1;
        this.mTimeStamp = -1;
        this.mMiuiVersion = VERSION.INCREMENTAL;
        this.mBootType = -1;
        this.mPeriodSystemRun = -1;
        this.mPeriodPmsScan = -1;
        this.mPeriodDexopt = -1;
        this.mPeriodAmsReady = -1;
        this.mPeriodUIReady = -1;
        this.mPeriodBootComplete = -1;
        String str = "";
        this.mDetailSystemRun = str;
        this.mDetailPmsScan = str;
        this.mDetailDexopt = str;
        this.mDetailAmsReady = str;
        this.mDetailUIReady = str;
        this.mDetailBootComplete = str;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeLong(this.mTimeStamp);
        dest.writeInt(this.mBootType);
        dest.writeLong(this.mPeriodSystemRun);
        dest.writeLong(this.mPeriodPmsScan);
        dest.writeLong(this.mPeriodDexopt);
        dest.writeLong(this.mPeriodAmsReady);
        dest.writeLong(this.mPeriodUIReady);
        dest.writeLong(this.mPeriodBootComplete);
        dest.writeString(this.mDetailSystemRun);
        dest.writeString(this.mDetailPmsScan);
        dest.writeString(this.mDetailDexopt);
        dest.writeString(this.mDetailAmsReady);
        dest.writeString(this.mDetailUIReady);
        dest.writeString(this.mDetailBootComplete);
        dest.writeString(this.mMiuiVersion);
    }

    private BootEvent(Parcel source) {
        this.mType = source.readInt();
        this.mTimeStamp = source.readLong();
        this.mBootType = source.readInt();
        this.mPeriodSystemRun = source.readLong();
        this.mPeriodPmsScan = source.readLong();
        this.mPeriodDexopt = source.readLong();
        this.mPeriodAmsReady = source.readLong();
        this.mPeriodUIReady = source.readLong();
        this.mPeriodBootComplete = source.readLong();
        this.mDetailSystemRun = source.readString();
        this.mDetailPmsScan = source.readString();
        this.mDetailDexopt = source.readString();
        this.mDetailAmsReady = source.readString();
        this.mDetailUIReady = source.readString();
        this.mDetailBootComplete = source.readString();
        this.mMiuiVersion = source.readString();
    }

    private String formatTime(long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BootEvent {mTimeStamp=");
        stringBuilder.append(formatTime(this.mTimeStamp));
        stringBuilder.append(",mBootType=");
        stringBuilder.append(bootTypeToString(this.mBootType));
        String str = ",mPeriod";
        stringBuilder.append(str);
        String str2 = ACTION_BOOT_SYSTEM_RUN;
        stringBuilder.append(str2);
        String str3 = ":";
        stringBuilder.append(str3);
        stringBuilder.append(this.mPeriodSystemRun);
        stringBuilder.append(str);
        String str4 = ACTION_BOOT_PMS_SCAN;
        stringBuilder.append(str4);
        stringBuilder.append(str3);
        stringBuilder.append(this.mPeriodPmsScan);
        stringBuilder.append(str);
        String str5 = ACTION_BOOT_DEXOPT;
        stringBuilder.append(str5);
        stringBuilder.append(str3);
        stringBuilder.append(this.mPeriodDexopt);
        stringBuilder.append(str);
        String str6 = ACTION_BOOT_AMS_READY;
        stringBuilder.append(str6);
        stringBuilder.append(str3);
        stringBuilder.append(this.mPeriodAmsReady);
        stringBuilder.append(str);
        String str7 = ACTION_BOOT_UI_READY;
        stringBuilder.append(str7);
        stringBuilder.append(str3);
        stringBuilder.append(this.mPeriodUIReady);
        stringBuilder.append(str);
        str = ACTION_BOOT_BOOT_COMPLETE;
        stringBuilder.append(str);
        stringBuilder.append(str3);
        stringBuilder.append(this.mPeriodBootComplete);
        String str8 = ",mDetail";
        stringBuilder.append(str8);
        stringBuilder.append(str2);
        stringBuilder.append(str3);
        stringBuilder.append(this.mDetailSystemRun);
        stringBuilder.append(str8);
        stringBuilder.append(str4);
        stringBuilder.append(str3);
        stringBuilder.append(this.mDetailPmsScan);
        stringBuilder.append(str8);
        stringBuilder.append(str5);
        stringBuilder.append(str3);
        stringBuilder.append(this.mDetailDexopt);
        stringBuilder.append(str8);
        stringBuilder.append(str6);
        stringBuilder.append(str3);
        stringBuilder.append(this.mDetailAmsReady);
        stringBuilder.append(str8);
        stringBuilder.append(str7);
        stringBuilder.append(str3);
        stringBuilder.append(this.mDetailUIReady);
        stringBuilder.append(str8);
        stringBuilder.append(str);
        stringBuilder.append(str3);
        stringBuilder.append(this.mDetailBootComplete);
        stringBuilder.append(",mMiuiVersion:");
        stringBuilder.append(this.mMiuiVersion);
        stringBuilder.append("}");
        sb.append(stringBuilder.toString());
        return sb.toString();
    }

    public static String bootTypeToString(int bootType) {
        if (bootType == 1) {
            return "NormalBoot";
        }
        if (bootType == 2) {
            return "FirstBoot";
        }
        if (bootType != 3) {
            return "Unknown";
        }
        return "OtaBoot";
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    public void setTimeStamp(long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public String getMiuiVersion() {
        return this.mMiuiVersion;
    }

    public void setMiuiVersion(String mMiuiVersion) {
        this.mMiuiVersion = mMiuiVersion;
    }

    public int getBootType() {
        return this.mBootType;
    }

    public void setBootType(int mBootType) {
        this.mBootType = mBootType;
    }

    public long getPeriodSystemRun() {
        return this.mPeriodSystemRun;
    }

    public void setPeriodSystemRun(long mPeriodSystemRun) {
        this.mPeriodSystemRun = mPeriodSystemRun;
    }

    public long getPeriodPmsScan() {
        return this.mPeriodPmsScan;
    }

    public void setPeriodPmsScan(long mPeriodPmsScan) {
        this.mPeriodPmsScan = mPeriodPmsScan;
    }

    public long getPeriodDexopt() {
        return this.mPeriodDexopt;
    }

    public void setPeriodDexopt(long mPeriodDexopt) {
        this.mPeriodDexopt = mPeriodDexopt;
    }

    public long getPeriodAmsReady() {
        return this.mPeriodAmsReady;
    }

    public void setPeriodAmsReady(long mPeriodAmsReady) {
        this.mPeriodAmsReady = mPeriodAmsReady;
    }

    public long getPeriodUIReady() {
        return this.mPeriodUIReady;
    }

    public void setPeriodUIReady(long mPeriodUIReady) {
        this.mPeriodUIReady = mPeriodUIReady;
    }

    public long getPeriodBootComplete() {
        return this.mPeriodBootComplete;
    }

    public void setPeriodBootComplete(long mPeriodBootComplete) {
        this.mPeriodBootComplete = mPeriodBootComplete;
    }

    public String getDetailSystemRun() {
        return this.mDetailSystemRun;
    }

    public void setDetailSystemRun(String mDetailSystemRun) {
        this.mDetailSystemRun = mDetailSystemRun;
    }

    public String getDetailPmsScan() {
        return this.mDetailPmsScan;
    }

    public void setDetailPmsScan(String mDetailPmsScan) {
        this.mDetailPmsScan = mDetailPmsScan;
    }

    public String getDetailDexopt() {
        return this.mDetailDexopt;
    }

    public void setDetailDexopt(String mDetailDexopt) {
        this.mDetailDexopt = mDetailDexopt;
    }

    public String getDetailAmsReady() {
        return this.mDetailAmsReady;
    }

    public void setDetailAmsReady(String mDetailAmsReady) {
        this.mDetailAmsReady = mDetailAmsReady;
    }

    public String getDetailUIReady() {
        return this.mDetailUIReady;
    }

    public void setDetailUIReady(String mDetailUIReady) {
        this.mDetailUIReady = mDetailUIReady;
    }

    public String getDetailBootComplete() {
        return this.mDetailBootComplete;
    }

    public void setDetailBootComplete(String mDetailBootComplete) {
        this.mDetailBootComplete = mDetailBootComplete;
    }
}
