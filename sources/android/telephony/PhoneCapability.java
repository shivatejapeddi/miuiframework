package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PhoneCapability implements Parcelable {
    public static final Creator<PhoneCapability> CREATOR = new Creator() {
        public PhoneCapability createFromParcel(Parcel in) {
            return new PhoneCapability(in, null);
        }

        public PhoneCapability[] newArray(int size) {
            return new PhoneCapability[size];
        }
    };
    public static final PhoneCapability DEFAULT_DSDS_CAPABILITY;
    public static final PhoneCapability DEFAULT_SSSS_CAPABILITY;
    public final List<ModemInfo> logicalModemList;
    public final int max5G;
    public final int maxActiveData;
    public final int maxActiveVoiceCalls;
    public final boolean validationBeforeSwitchSupported;

    /* synthetic */ PhoneCapability(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    static {
        ModemInfo modemInfo1 = new ModemInfo(0, 0, true, true);
        ModemInfo modemInfo2 = new ModemInfo(1, 0, true, true);
        List<ModemInfo> logicalModemList = new ArrayList();
        logicalModemList.add(modemInfo1);
        logicalModemList.add(modemInfo2);
        DEFAULT_DSDS_CAPABILITY = new PhoneCapability(1, 1, 0, logicalModemList, false);
        List logicalModemList2 = new ArrayList();
        logicalModemList2.add(modemInfo1);
        DEFAULT_SSSS_CAPABILITY = new PhoneCapability(1, 1, 0, logicalModemList2, false);
    }

    public PhoneCapability(int maxActiveVoiceCalls, int maxActiveData, int max5G, List<ModemInfo> logicalModemList, boolean validationBeforeSwitchSupported) {
        this.maxActiveVoiceCalls = maxActiveVoiceCalls;
        this.maxActiveData = maxActiveData;
        this.max5G = max5G;
        this.logicalModemList = logicalModemList == null ? new ArrayList() : logicalModemList;
        this.validationBeforeSwitchSupported = validationBeforeSwitchSupported;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("maxActiveVoiceCalls=");
        stringBuilder.append(this.maxActiveVoiceCalls);
        stringBuilder.append(" maxActiveData=");
        stringBuilder.append(this.maxActiveData);
        stringBuilder.append(" max5G=");
        stringBuilder.append(this.max5G);
        stringBuilder.append("logicalModemList:");
        stringBuilder.append(Arrays.toString(this.logicalModemList.toArray()));
        return stringBuilder.toString();
    }

    private PhoneCapability(Parcel in) {
        this.maxActiveVoiceCalls = in.readInt();
        this.maxActiveData = in.readInt();
        this.max5G = in.readInt();
        this.validationBeforeSwitchSupported = in.readBoolean();
        this.logicalModemList = new ArrayList();
        in.readList(this.logicalModemList, ModemInfo.class.getClassLoader());
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.maxActiveVoiceCalls), Integer.valueOf(this.maxActiveData), Integer.valueOf(this.max5G), this.logicalModemList, Boolean.valueOf(this.validationBeforeSwitchSupported)});
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (o == null || !(o instanceof PhoneCapability) || hashCode() != o.hashCode()) {
            return false;
        }
        if (this == o) {
            return true;
        }
        PhoneCapability s = (PhoneCapability) o;
        if (this.maxActiveVoiceCalls == s.maxActiveVoiceCalls && this.maxActiveData == s.maxActiveData && this.max5G == s.max5G && this.validationBeforeSwitchSupported == s.validationBeforeSwitchSupported && this.logicalModemList.equals(s.logicalModemList)) {
            z = true;
        }
        return z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxActiveVoiceCalls);
        dest.writeInt(this.maxActiveData);
        dest.writeInt(this.max5G);
        dest.writeBoolean(this.validationBeforeSwitchSupported);
        dest.writeList(this.logicalModemList);
    }
}
