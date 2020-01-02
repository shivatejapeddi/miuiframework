package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.io.PrintWriter;

public final class LocusId implements Parcelable {
    public static final Creator<LocusId> CREATOR = new Creator<LocusId>() {
        public LocusId createFromParcel(Parcel parcel) {
            return new LocusId(parcel.readString());
        }

        public LocusId[] newArray(int size) {
            return new LocusId[size];
        }
    };
    private final String mId;

    public LocusId(String id) {
        this.mId = (String) Preconditions.checkStringNotEmpty(id, "id cannot be empty");
    }

    public String getId() {
        return this.mId;
    }

    public int hashCode() {
        int result = 1 * 31;
        String str = this.mId;
        return result + (str == null ? 0 : str.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocusId other = (LocusId) obj;
        String str = this.mId;
        if (str == null) {
            if (other.mId != null) {
                return false;
            }
        } else if (!str.equals(other.mId)) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LocusId[");
        stringBuilder.append(getSanitizedId());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public void dump(PrintWriter pw) {
        pw.print("id:");
        pw.println(getSanitizedId());
    }

    private String getSanitizedId() {
        int size = this.mId.length();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(size);
        stringBuilder.append("_chars");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mId);
    }
}
