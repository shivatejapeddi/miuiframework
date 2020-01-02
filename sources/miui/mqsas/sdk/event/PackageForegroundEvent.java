package miui.mqsas.sdk.event;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.DateFormat;

public class PackageForegroundEvent implements Parcelable {
    public static final Creator<PackageForegroundEvent> CREATOR = new Creator<PackageForegroundEvent>() {
        public PackageForegroundEvent createFromParcel(Parcel in) {
            return new PackageForegroundEvent(in);
        }

        public PackageForegroundEvent[] newArray(int size) {
            return new PackageForegroundEvent[size];
        }
    };
    private String componentName;
    private long foregroundTime;
    private int identity;
    private boolean isColdStart;
    private String packageName;
    private int pid;

    public PackageForegroundEvent() {
        String str = "";
        this.packageName = str;
        this.componentName = str;
        this.identity = -1;
        this.pid = -1;
        this.foregroundTime = -1;
        this.isColdStart = false;
    }

    protected PackageForegroundEvent(Parcel in) {
        this.packageName = in.readString();
        this.componentName = in.readString();
        this.identity = in.readInt();
        this.pid = in.readInt();
        this.foregroundTime = in.readLong();
        boolean z = true;
        if (in.readInt() != 1) {
            z = false;
        }
        this.isColdStart = z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.componentName);
        dest.writeInt(this.identity);
        dest.writeInt(this.pid);
        dest.writeLong(this.foregroundTime);
        dest.writeInt(this.isColdStart);
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getComponentName() {
        return this.componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public int getIdentity() {
        return this.identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getPid() {
        return this.pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public long getForegroundTime() {
        return this.foregroundTime;
    }

    public void setForegroundTime(long foregroundTime) {
        this.foregroundTime = foregroundTime;
    }

    public boolean isColdStart() {
        return this.isColdStart;
    }

    public void setColdStart(boolean coldStart) {
        this.isColdStart = coldStart;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PackageForegroundEvent{packageName='");
        stringBuilder.append(this.packageName);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", componentName='");
        stringBuilder.append(this.componentName);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", identity=");
        stringBuilder.append(this.identity);
        stringBuilder.append(", pid=");
        stringBuilder.append(this.pid);
        stringBuilder.append(", foregroundTime=");
        stringBuilder.append(this.foregroundTime);
        stringBuilder.append(", isColdStart=");
        stringBuilder.append(this.isColdStart);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        PackageForegroundEvent o = (PackageForegroundEvent) obj;
        if (this.packageName.equals(o.packageName) && this.componentName.equals(o.componentName) && this.identity == o.identity && this.pid == o.pid) {
            z = true;
        }
        return z;
    }
}
