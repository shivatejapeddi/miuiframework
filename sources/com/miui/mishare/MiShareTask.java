package com.miui.mishare;

import android.content.ClipData;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class MiShareTask implements Parcelable {
    public static final Creator<MiShareTask> CREATOR = new Creator<MiShareTask>() {
        public MiShareTask createFromParcel(Parcel in) {
            return new MiShareTask(in);
        }

        public MiShareTask[] newArray(int size) {
            return new MiShareTask[size];
        }
    };
    public ClipData clipData;
    public int count;
    public RemoteDevice device;
    public int deviceX;
    public int deviceY;
    public String mimeType;
    public boolean send;
    public String taskId;
    public int tbHeight;
    public int tbWidth;

    protected MiShareTask(Parcel in) {
        this.send = in.readByte() != (byte) 0;
        this.taskId = in.readString();
        this.count = in.readInt();
        this.device = (RemoteDevice) in.readParcelable(RemoteDevice.class.getClassLoader());
        this.deviceX = in.readInt();
        this.deviceY = in.readInt();
        this.clipData = (ClipData) in.readParcelable(ClipData.class.getClassLoader());
        this.mimeType = in.readString();
        this.tbWidth = in.readInt();
        this.tbHeight = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) this.send);
        dest.writeString(this.taskId);
        dest.writeInt(this.count);
        dest.writeParcelable(this.device, flags);
        dest.writeInt(this.deviceX);
        dest.writeInt(this.deviceY);
        dest.writeParcelable(this.clipData, flags);
        dest.writeString(this.mimeType);
        dest.writeInt(this.tbWidth);
        dest.writeInt(this.tbHeight);
    }

    public int describeContents() {
        return 0;
    }

    public int hashCode() {
        return Objects.hashCode(this.taskId);
    }

    public boolean equals(Object obj) {
        if (obj instanceof MiShareTask) {
            return Objects.equals(this.taskId, ((MiShareTask) obj).taskId);
        }
        return super.equals(obj);
    }
}
