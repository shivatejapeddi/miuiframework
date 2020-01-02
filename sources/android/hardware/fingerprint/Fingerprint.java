package android.hardware.fingerprint;

import android.hardware.biometrics.BiometricAuthenticator.Identifier;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class Fingerprint extends Identifier {
    public static final Creator<Fingerprint> CREATOR = new Creator<Fingerprint>() {
        public Fingerprint createFromParcel(Parcel in) {
            return new Fingerprint(in, null);
        }

        public Fingerprint[] newArray(int size) {
            return new Fingerprint[size];
        }
    };
    private int mGroupId;

    /* synthetic */ Fingerprint(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public Fingerprint(CharSequence name, int groupId, int fingerId, long deviceId) {
        super(name, fingerId, deviceId);
        this.mGroupId = groupId;
    }

    private Fingerprint(Parcel in) {
        super(in.readString(), in.readInt(), in.readLong());
        this.mGroupId = in.readInt();
    }

    public int getFingerId() {
        return 0;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getName().toString());
        out.writeInt(getBiometricId());
        out.writeLong(getDeviceId());
        out.writeInt(this.mGroupId);
    }
}
