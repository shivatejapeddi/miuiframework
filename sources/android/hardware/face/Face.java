package android.hardware.face;

import android.hardware.biometrics.BiometricAuthenticator.Identifier;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class Face extends Identifier {
    public static final Creator<Face> CREATOR = new Creator<Face>() {
        public Face createFromParcel(Parcel in) {
            return new Face(in, null);
        }

        public Face[] newArray(int size) {
            return new Face[size];
        }
    };

    /* synthetic */ Face(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public Face(CharSequence name, int faceId, long deviceId) {
        super(name, faceId, deviceId);
    }

    private Face(Parcel in) {
        super(in.readString(), in.readInt(), in.readLong());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getName().toString());
        out.writeInt(getBiometricId());
        out.writeLong(getDeviceId());
    }
}
