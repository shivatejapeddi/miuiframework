package android.security.keystore;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class KeystoreResponse implements Parcelable {
    public static final Creator<KeystoreResponse> CREATOR = new Creator<KeystoreResponse>() {
        public KeystoreResponse createFromParcel(Parcel in) {
            return new KeystoreResponse(in.readInt(), in.readString());
        }

        public KeystoreResponse[] newArray(int size) {
            return new KeystoreResponse[size];
        }
    };
    public final int error_code_;
    public final String error_msg_;

    protected KeystoreResponse(int error_code, String error_msg) {
        this.error_code_ = error_code;
        this.error_msg_ = error_msg;
    }

    public final int getErrorCode() {
        return this.error_code_;
    }

    public final String getErrorMessage() {
        return this.error_msg_;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.error_code_);
        out.writeString(this.error_msg_);
    }
}
