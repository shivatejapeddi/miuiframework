package android.hardware.fingerprint;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public class FingerprintFidoOut implements Parcelable {
    public static final Creator<FingerprintFidoOut> CREATOR = new Creator<FingerprintFidoOut>() {
        public FingerprintFidoOut createFromParcel(Parcel source) {
            return new FingerprintFidoOut(source);
        }

        public FingerprintFidoOut[] newArray(int size) {
            return new FingerprintFidoOut[size];
        }
    };
    private int fingerId;
    private byte[] uvt;

    public FingerprintFidoOut(Parcel in) {
        byte[] ba = in.createByteArray();
        in.unmarshall(ba, 0, ba.length);
        this.uvt = ba;
        this.fingerId = in.readInt();
    }

    public byte[] getUvt() {
        return this.uvt;
    }

    public void setUvt(byte[] uvt) {
        this.uvt = uvt;
    }

    public int getFingerId() {
        return this.fingerId;
    }

    public void setFingerId(int fingerId) {
        this.fingerId = fingerId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.uvt);
        dest.writeInt(this.fingerId);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FingerprintFidoOut [uvt=");
        stringBuilder.append(Arrays.toString(this.uvt));
        stringBuilder.append(", fingerId=");
        stringBuilder.append(this.fingerId);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
