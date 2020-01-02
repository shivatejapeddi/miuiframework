package miui.mqsas.sdk.event;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PackageEvent implements Parcelable {
    public static final int ACTION_PACKAGE_INSTALL = 1;
    public static final int ACTION_PACKAGE_UNINSTALL = 2;
    public static final int ACTION_PACKAGE_UPDATE = 3;
    public static final Creator<PackageEvent> CREATOR = new Creator<PackageEvent>() {
        public PackageEvent createFromParcel(Parcel in) {
            return new PackageEvent(in, null);
        }

        public PackageEvent[] newArray(int size) {
            return new PackageEvent[size];
        }
    };
    private int mAction;
    private String mInstallerPkgName;
    private String mPackageName;
    private int mReturnCode;
    private String mReturnMsg;
    private long mTimeStamp;
    private int mType;
    private int mVersionCode;
    private String mVersionName;

    /* synthetic */ PackageEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public PackageEvent() {
        this.mType = -1;
        this.mTimeStamp = -1;
        this.mAction = -1;
        String str = "";
        this.mPackageName = str;
        this.mReturnCode = -1;
        this.mReturnMsg = str;
        this.mVersionCode = -1;
        this.mVersionName = str;
        this.mInstallerPkgName = str;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeLong(this.mTimeStamp);
        dest.writeInt(this.mAction);
        dest.writeString(this.mPackageName);
        dest.writeInt(this.mReturnCode);
        dest.writeString(this.mReturnMsg);
        dest.writeInt(this.mVersionCode);
        dest.writeString(this.mVersionName);
        dest.writeString(this.mInstallerPkgName);
    }

    private PackageEvent(Parcel source) {
        this.mType = source.readInt();
        this.mTimeStamp = source.readLong();
        this.mAction = source.readInt();
        this.mPackageName = source.readString();
        this.mReturnCode = source.readInt();
        this.mReturnMsg = source.readString();
        this.mVersionCode = source.readInt();
        this.mVersionName = source.readString();
        this.mInstallerPkgName = source.readString();
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    public void setTimeStamp(long timestamp) {
        this.mTimeStamp = timestamp;
    }

    public int getAction() {
        return this.mAction;
    }

    public void setAction(int action) {
        this.mAction = action;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public void setPackageName(String name) {
        this.mPackageName = name;
    }

    public int getReturnCode() {
        return this.mReturnCode;
    }

    public void setReturnCode(int returnCode) {
        this.mReturnCode = returnCode;
    }

    public String getReturnMsg() {
        return this.mReturnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.mReturnMsg = returnMsg;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public void setVersionCode(int versionCode) {
        this.mVersionCode = versionCode;
    }

    public String getVersionName() {
        return this.mVersionName;
    }

    public void setVersionName(String versionName) {
        this.mVersionName = versionName;
    }

    public String getInstallerPkgName() {
        return this.mInstallerPkgName;
    }

    public void setInstallerPkgName(String installerPkgName) {
        this.mInstallerPkgName = installerPkgName;
    }

    private String formatTime(long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PackageEvent { mTimeStamp=");
        stringBuilder.append(formatTime(this.mTimeStamp));
        stringBuilder.append(" mAction=");
        stringBuilder.append(actionToString(this.mAction));
        stringBuilder.append(" mPackageName=");
        stringBuilder.append(this.mPackageName);
        stringBuilder.append(" mReturnCode=");
        stringBuilder.append(this.mReturnCode);
        stringBuilder.append(" mReturnMsg=");
        stringBuilder.append(this.mReturnMsg);
        stringBuilder.append(" mVersionCode=");
        stringBuilder.append(this.mVersionCode);
        stringBuilder.append(" mVersionName=");
        stringBuilder.append(this.mVersionName);
        stringBuilder.append("mInstallerPkgName=");
        stringBuilder.append(this.mInstallerPkgName);
        sb.append(stringBuilder.toString());
        return sb.toString();
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PackageEvent { mAction=");
        stringBuilder.append(actionToString(this.mAction));
        stringBuilder.append(" mPackageName=");
        stringBuilder.append(this.mPackageName);
        stringBuilder.append(" mReturnCode=");
        stringBuilder.append(this.mReturnCode);
        sb.append(stringBuilder.toString());
        return sb.toString();
    }

    public static String actionToString(int action) {
        if (action == 1) {
            return "Install";
        }
        if (action == 2) {
            return "Uninstall";
        }
        if (action != 3) {
            return "Unknown";
        }
        return "Update";
    }
}
