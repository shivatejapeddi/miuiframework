package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public final class ConnectionInfo implements Parcelable {
    public static final Creator<ConnectionInfo> CREATOR = new Creator<ConnectionInfo>() {
        public ConnectionInfo createFromParcel(Parcel in) {
            String remoteAddress = "Invalid InetAddress";
            int protocol = in.readInt();
            try {
                InetAddress localAddress = InetAddress.getByAddress(in.createByteArray());
                int localPort = in.readInt();
                try {
                    remoteAddress = InetAddress.getByAddress(in.createByteArray());
                    return new ConnectionInfo(protocol, new InetSocketAddress(localAddress, localPort), new InetSocketAddress(remoteAddress, in.readInt()));
                } catch (UnknownHostException e) {
                    throw new IllegalArgumentException(remoteAddress);
                }
            } catch (UnknownHostException e2) {
                throw new IllegalArgumentException(remoteAddress);
            }
        }

        public ConnectionInfo[] newArray(int size) {
            return new ConnectionInfo[size];
        }
    };
    public final InetSocketAddress local;
    public final int protocol;
    public final InetSocketAddress remote;

    public int describeContents() {
        return 0;
    }

    public ConnectionInfo(int protocol, InetSocketAddress local, InetSocketAddress remote) {
        this.protocol = protocol;
        this.local = local;
        this.remote = remote;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.protocol);
        out.writeByteArray(this.local.getAddress().getAddress());
        out.writeInt(this.local.getPort());
        out.writeByteArray(this.remote.getAddress().getAddress());
        out.writeInt(this.remote.getPort());
    }
}
