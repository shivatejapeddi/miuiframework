package android.view.contentcapture;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.PrintWriter;

public final class ContentCaptureSessionId implements Parcelable {
    public static final Creator<ContentCaptureSessionId> CREATOR = new Creator<ContentCaptureSessionId>() {
        public ContentCaptureSessionId createFromParcel(Parcel parcel) {
            return new ContentCaptureSessionId(parcel.readInt());
        }

        public ContentCaptureSessionId[] newArray(int size) {
            return new ContentCaptureSessionId[size];
        }
    };
    private final int mValue;

    public ContentCaptureSessionId(int value) {
        this.mValue = value;
    }

    public int getValue() {
        return this.mValue;
    }

    public int hashCode() {
        return (1 * 31) + this.mValue;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (this.mValue != ((ContentCaptureSessionId) obj).mValue) {
            return false;
        }
        return true;
    }

    public String toString() {
        return Integer.toString(this.mValue);
    }

    public void dump(PrintWriter pw) {
        pw.print(this.mValue);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mValue);
    }
}
