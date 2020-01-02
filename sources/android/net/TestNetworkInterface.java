package android.net;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class TestNetworkInterface implements Parcelable {
    public static final Creator<TestNetworkInterface> CREATOR = new Creator<TestNetworkInterface>() {
        public TestNetworkInterface createFromParcel(Parcel in) {
            return new TestNetworkInterface(in, null);
        }

        public TestNetworkInterface[] newArray(int size) {
            return new TestNetworkInterface[size];
        }
    };
    private final ParcelFileDescriptor mFileDescriptor;
    private final String mInterfaceName;

    public int describeContents() {
        return this.mFileDescriptor != null ? 1 : 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.mFileDescriptor, 1);
        out.writeString(this.mInterfaceName);
    }

    public TestNetworkInterface(ParcelFileDescriptor pfd, String intf) {
        this.mFileDescriptor = pfd;
        this.mInterfaceName = intf;
    }

    private TestNetworkInterface(Parcel in) {
        this.mFileDescriptor = (ParcelFileDescriptor) in.readParcelable(ParcelFileDescriptor.class.getClassLoader());
        this.mInterfaceName = in.readString();
    }

    public ParcelFileDescriptor getFileDescriptor() {
        return this.mFileDescriptor;
    }

    public String getInterfaceName() {
        return this.mInterfaceName;
    }
}
