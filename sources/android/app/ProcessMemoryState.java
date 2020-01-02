package android.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class ProcessMemoryState implements Parcelable {
    public static final Creator<ProcessMemoryState> CREATOR = new Creator<ProcessMemoryState>() {
        public ProcessMemoryState createFromParcel(Parcel in) {
            return new ProcessMemoryState(in, null);
        }

        public ProcessMemoryState[] newArray(int size) {
            return new ProcessMemoryState[size];
        }
    };
    public final int oomScore;
    public final int pid;
    public final String processName;
    public final int uid;

    /* synthetic */ ProcessMemoryState(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public ProcessMemoryState(int uid, int pid, String processName, int oomScore) {
        this.uid = uid;
        this.pid = pid;
        this.processName = processName;
        this.oomScore = oomScore;
    }

    private ProcessMemoryState(Parcel in) {
        this.uid = in.readInt();
        this.pid = in.readInt();
        this.processName = in.readString();
        this.oomScore = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.uid);
        parcel.writeInt(this.pid);
        parcel.writeString(this.processName);
        parcel.writeInt(this.oomScore);
    }
}
