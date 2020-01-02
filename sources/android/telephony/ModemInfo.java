package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class ModemInfo implements Parcelable {
    public static final Creator<ModemInfo> CREATOR = new Creator() {
        public ModemInfo createFromParcel(Parcel in) {
            return new ModemInfo(in);
        }

        public ModemInfo[] newArray(int size) {
            return new ModemInfo[size];
        }
    };
    public final boolean isDataSupported;
    public final boolean isVoiceSupported;
    public final int modemId;
    public final int rat;

    public ModemInfo(int modemId) {
        this(modemId, 0, true, true);
    }

    public ModemInfo(int modemId, int rat, boolean isVoiceSupported, boolean isDataSupported) {
        this.modemId = modemId;
        this.rat = rat;
        this.isVoiceSupported = isVoiceSupported;
        this.isDataSupported = isDataSupported;
    }

    public ModemInfo(Parcel in) {
        this.modemId = in.readInt();
        this.rat = in.readInt();
        this.isVoiceSupported = in.readBoolean();
        this.isDataSupported = in.readBoolean();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("modemId=");
        stringBuilder.append(this.modemId);
        stringBuilder.append(" rat=");
        stringBuilder.append(this.rat);
        stringBuilder.append(" isVoiceSupported:");
        stringBuilder.append(this.isVoiceSupported);
        stringBuilder.append(" isDataSupported:");
        stringBuilder.append(this.isDataSupported);
        return stringBuilder.toString();
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.modemId), Integer.valueOf(this.rat), Boolean.valueOf(this.isVoiceSupported), Boolean.valueOf(this.isDataSupported)});
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (o == null || !(o instanceof ModemInfo) || hashCode() != o.hashCode()) {
            return false;
        }
        if (this == o) {
            return true;
        }
        ModemInfo s = (ModemInfo) o;
        if (this.modemId == s.modemId && this.rat == s.rat && this.isVoiceSupported == s.isVoiceSupported && this.isDataSupported == s.isDataSupported) {
            z = true;
        }
        return z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.modemId);
        dest.writeInt(this.rat);
        dest.writeBoolean(this.isVoiceSupported);
        dest.writeBoolean(this.isDataSupported);
    }
}
