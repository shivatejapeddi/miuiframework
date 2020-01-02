package android.hardware.fingerprint;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public class FingerprintFidoIn implements Parcelable {
    public static final Creator<FingerprintFidoIn> CREATOR = new Creator<FingerprintFidoIn>() {
        public FingerprintFidoIn createFromParcel(Parcel source) {
            return new FingerprintFidoIn(source);
        }

        public FingerprintFidoIn[] newArray(int size) {
            return new FingerprintFidoIn[size];
        }
    };
    public String AAID;
    private byte[] nonce;

    public FingerprintFidoIn(Parcel in) {
        this.AAID = in.readString();
        byte[] ba = in.createByteArray();
        in.unmarshall(ba, 0, ba.length);
        this.nonce = ba;
    }

    public byte[] getNonce() {
        return this.nonce;
    }

    public void setNonce(byte[] nonce) {
        this.nonce = nonce;
    }

    public String getAAID() {
        return this.AAID;
    }

    public void setAAID(String AAID) {
        this.AAID = AAID;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.AAID);
        dest.writeByteArray(this.nonce);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FingerprintFidoIn [AAID=");
        stringBuilder.append(this.AAID);
        stringBuilder.append(", nonce=");
        stringBuilder.append(Arrays.toString(this.nonce));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
