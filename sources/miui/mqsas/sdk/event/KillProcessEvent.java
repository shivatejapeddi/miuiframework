package miui.mqsas.sdk.event;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class KillProcessEvent implements Parcelable {
    public static final Creator<KillProcessEvent> CREATOR = new Creator<KillProcessEvent>() {
        public KillProcessEvent createFromParcel(Parcel in) {
            return new KillProcessEvent(in, null);
        }

        public KillProcessEvent[] newArray(int size) {
            return new KillProcessEvent[size];
        }
    };
    public static final String POLICY_EXCEPTION = "exception";
    public static final String POLICY_KILL_SELF = "killself";
    public static final String POLICY_LMK = "lowmemorykiller";
    public static final String POLICY_OTHER = "other";
    public static final String POLICY_POWERKEEPER = "powerkeeper";
    public static final String POLICY_SECURITY = "securitycenter";
    public static final String POLICY_SYSTEM = "system";
    public static final String POLICY_SYSTEMUI = "systemui";
    public static final String POLICY_UNUSE = "userunused";
    public static final String POLICY_WHETSTONE = "whetstone";
    public static final int PROCESS_STATE_NONEXISTENT = -1;
    public static final int UNKNOWN_ADJ = Integer.MAX_VALUE;
    private boolean isInterestingToUser;
    private String killedProc;
    private String killedReason;
    private long killedTime;
    private String policy;
    private int procAdj;
    private int procState;

    /* synthetic */ KillProcessEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public KillProcessEvent() {
        String str = "";
        this.policy = str;
        this.killedReason = str;
        this.killedProc = str;
        this.procState = -1;
        this.procAdj = Integer.MAX_VALUE;
        this.killedTime = -1;
        this.isInterestingToUser = false;
    }

    private KillProcessEvent(Parcel source) {
        this.policy = source.readString();
        this.killedReason = source.readString();
        this.killedProc = source.readString();
        this.procState = source.readInt();
        this.procAdj = source.readInt();
        this.killedTime = source.readLong();
        boolean z = true;
        if (source.readInt() != 1) {
            z = false;
        }
        this.isInterestingToUser = z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.policy);
        dest.writeString(this.killedReason);
        dest.writeString(this.killedProc);
        dest.writeInt(this.procState);
        dest.writeInt(this.procAdj);
        dest.writeLong(this.killedTime);
        dest.writeInt(this.isInterestingToUser);
    }

    public String getPolicy() {
        return this.policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getKilledReason() {
        return this.killedReason;
    }

    public void setKilledReason(String killedReason) {
        this.killedReason = killedReason;
    }

    public String getKilledProc() {
        return this.killedProc;
    }

    public void setKilledProc(String killedProc) {
        this.killedProc = killedProc;
    }

    public int getProcState() {
        return this.procState;
    }

    public void setProcState(int procState) {
        this.procState = procState;
    }

    public int getProcAdj() {
        return this.procAdj;
    }

    public void setProcAdj(int procAdj) {
        this.procAdj = procAdj;
    }

    public long getKilledTime() {
        return this.killedTime;
    }

    public void setKilledTime(long killedTime) {
        this.killedTime = killedTime;
    }

    public boolean isInterestingToUser() {
        return this.isInterestingToUser;
    }

    public void setInterestingToUser(boolean interestingToUser) {
        this.isInterestingToUser = interestingToUser;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("KillProcessEvent { policy=");
        stringBuilder.append(this.policy);
        stringBuilder.append(" reason=");
        stringBuilder.append(this.killedReason);
        stringBuilder.append(" killedProcess=");
        stringBuilder.append(this.killedProc);
        stringBuilder.append(" processState=");
        stringBuilder.append(this.procState);
        stringBuilder.append(" processAdj=");
        stringBuilder.append(this.procAdj);
        stringBuilder.append(" killedTime=");
        stringBuilder.append(this.killedTime);
        stringBuilder.append(" isInterestingToUser=");
        stringBuilder.append(this.isInterestingToUser);
        stringBuilder.append("}");
        sb.append(stringBuilder.toString());
        return sb.toString();
    }
}
