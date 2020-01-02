package android.app;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class WaitResult implements Parcelable {
    public static final Creator<WaitResult> CREATOR = new Creator<WaitResult>() {
        public WaitResult createFromParcel(Parcel source) {
            return new WaitResult(source, null);
        }

        public WaitResult[] newArray(int size) {
            return new WaitResult[size];
        }
    };
    public static final int INVALID_DELAY = -1;
    public static final int LAUNCH_STATE_COLD = 1;
    public static final int LAUNCH_STATE_HOT = 3;
    public static final int LAUNCH_STATE_WARM = 2;
    public int launchState;
    public int result;
    public boolean timeout;
    public long totalTime;
    public ComponentName who;

    @Retention(RetentionPolicy.SOURCE)
    public @interface LaunchState {
    }

    /* synthetic */ WaitResult(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.result);
        dest.writeInt(this.timeout);
        ComponentName.writeToParcel(this.who, dest);
        dest.writeLong(this.totalTime);
        dest.writeInt(this.launchState);
    }

    private WaitResult(Parcel source) {
        this.result = source.readInt();
        this.timeout = source.readInt() != 0;
        this.who = ComponentName.readFromParcel(source);
        this.totalTime = source.readLong();
        this.launchState = source.readInt();
    }

    public void dump(PrintWriter pw, String prefix) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("WaitResult:");
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("  result=");
        stringBuilder.append(this.result);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("  timeout=");
        stringBuilder.append(this.timeout);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("  who=");
        stringBuilder.append(this.who);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("  totalTime=");
        stringBuilder.append(this.totalTime);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("  launchState=");
        stringBuilder.append(this.launchState);
        pw.println(stringBuilder.toString());
    }

    public static String launchStateToString(int type) {
        if (type == 1) {
            return "COLD";
        }
        if (type == 2) {
            return "WARM";
        }
        if (type == 3) {
            return "HOT";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UNKNOWN (");
        stringBuilder.append(type);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
