package android.content;

import android.os.Parcel;

public class MiSyncResult {
    public String resultMessage;

    public MiSyncResult() {
        this.resultMessage = "";
    }

    public MiSyncResult(Parcel parcel) {
        this.resultMessage = parcel.readString();
    }

    public void clear() {
        this.resultMessage = "";
    }

    public void writeToParcel(Parcel parcel) {
        parcel.writeString(this.resultMessage);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" resultMessage");
        sb.append(this.resultMessage);
        return sb.toString();
    }
}
