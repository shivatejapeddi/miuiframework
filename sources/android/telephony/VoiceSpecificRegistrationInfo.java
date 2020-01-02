package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class VoiceSpecificRegistrationInfo implements Parcelable {
    public static final Creator<VoiceSpecificRegistrationInfo> CREATOR = new Creator<VoiceSpecificRegistrationInfo>() {
        public VoiceSpecificRegistrationInfo createFromParcel(Parcel source) {
            return new VoiceSpecificRegistrationInfo(source, null);
        }

        public VoiceSpecificRegistrationInfo[] newArray(int size) {
            return new VoiceSpecificRegistrationInfo[size];
        }
    };
    public final boolean cssSupported;
    public final int defaultRoamingIndicator;
    public final int roamingIndicator;
    public final int systemIsInPrl;

    /* synthetic */ VoiceSpecificRegistrationInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    VoiceSpecificRegistrationInfo(boolean cssSupported, int roamingIndicator, int systemIsInPrl, int defaultRoamingIndicator) {
        this.cssSupported = cssSupported;
        this.roamingIndicator = roamingIndicator;
        this.systemIsInPrl = systemIsInPrl;
        this.defaultRoamingIndicator = defaultRoamingIndicator;
    }

    VoiceSpecificRegistrationInfo(VoiceSpecificRegistrationInfo vsri) {
        this.cssSupported = vsri.cssSupported;
        this.roamingIndicator = vsri.roamingIndicator;
        this.systemIsInPrl = vsri.systemIsInPrl;
        this.defaultRoamingIndicator = vsri.defaultRoamingIndicator;
    }

    private VoiceSpecificRegistrationInfo(Parcel source) {
        this.cssSupported = source.readBoolean();
        this.roamingIndicator = source.readInt();
        this.systemIsInPrl = source.readInt();
        this.defaultRoamingIndicator = source.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.cssSupported);
        dest.writeInt(this.roamingIndicator);
        dest.writeInt(this.systemIsInPrl);
        dest.writeInt(this.defaultRoamingIndicator);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("VoiceSpecificRegistrationInfo { mCssSupported=");
        stringBuilder.append(this.cssSupported);
        stringBuilder.append(" mRoamingIndicator=");
        stringBuilder.append(this.roamingIndicator);
        stringBuilder.append(" mSystemIsInPrl=");
        stringBuilder.append(this.systemIsInPrl);
        stringBuilder.append(" mDefaultRoamingIndicator=");
        stringBuilder.append(this.defaultRoamingIndicator);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Boolean.valueOf(this.cssSupported), Integer.valueOf(this.roamingIndicator), Integer.valueOf(this.systemIsInPrl), Integer.valueOf(this.defaultRoamingIndicator)});
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof VoiceSpecificRegistrationInfo)) {
            return false;
        }
        VoiceSpecificRegistrationInfo other = (VoiceSpecificRegistrationInfo) o;
        if (!(this.cssSupported == other.cssSupported && this.roamingIndicator == other.roamingIndicator && this.systemIsInPrl == other.systemIsInPrl && this.defaultRoamingIndicator == other.defaultRoamingIndicator)) {
            z = false;
        }
        return z;
    }
}
