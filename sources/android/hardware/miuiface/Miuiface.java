package android.hardware.miuiface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class Miuiface implements Parcelable {
    public static final Creator<Miuiface> CREATOR = new Creator<Miuiface>() {
        public Miuiface createFromParcel(Parcel in) {
            return new Miuiface(in, null);
        }

        public Miuiface[] newArray(int size) {
            return new Miuiface[size];
        }
    };
    private long mDeviceId;
    private int mGroupId;
    private int mMiuifaceId;
    private CharSequence mName;

    /* synthetic */ Miuiface(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public Miuiface(CharSequence name, int groupId, int miuifaceId, long deviceId) {
        this.mName = name;
        this.mGroupId = groupId;
        this.mMiuifaceId = miuifaceId;
        this.mDeviceId = deviceId;
    }

    private Miuiface(Parcel in) {
        this.mName = in.readString();
        this.mGroupId = in.readInt();
        this.mMiuifaceId = in.readInt();
        this.mDeviceId = in.readLong();
    }

    public CharSequence getName() {
        return this.mName;
    }

    public int getMiuifaceId() {
        return this.mMiuifaceId;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public long getDeviceId() {
        return this.mDeviceId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mName.toString());
        out.writeInt(this.mGroupId);
        out.writeInt(this.mMiuifaceId);
        out.writeLong(this.mDeviceId);
    }
}
