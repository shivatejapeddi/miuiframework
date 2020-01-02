package android.security;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class KeystoreArguments implements Parcelable {
    @UnsupportedAppUsage
    public static final Creator<KeystoreArguments> CREATOR = new Creator<KeystoreArguments>() {
        public KeystoreArguments createFromParcel(Parcel in) {
            return new KeystoreArguments(in, null);
        }

        public KeystoreArguments[] newArray(int size) {
            return new KeystoreArguments[size];
        }
    };
    public byte[][] args;

    /* synthetic */ KeystoreArguments(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public KeystoreArguments() {
        this.args = null;
    }

    @UnsupportedAppUsage
    public KeystoreArguments(byte[][] args) {
        this.args = args;
    }

    private KeystoreArguments(Parcel in) {
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int flags) {
        byte[][] bArr = this.args;
        int i = 0;
        if (bArr == null) {
            out.writeInt(0);
            return;
        }
        out.writeInt(bArr.length);
        bArr = this.args;
        int length = bArr.length;
        while (i < length) {
            out.writeByteArray(bArr[i]);
            i++;
        }
    }

    private void readFromParcel(Parcel in) {
        int length = in.readInt();
        this.args = new byte[length][];
        for (int i = 0; i < length; i++) {
            this.args[i] = in.createByteArray();
        }
    }

    public int describeContents() {
        return 0;
    }
}
