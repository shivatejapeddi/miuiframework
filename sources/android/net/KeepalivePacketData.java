package android.net;

import android.net.SocketKeepalive.InvalidPacketException;
import android.net.util.IpUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.net.InetAddress;

public class KeepalivePacketData implements Parcelable {
    public static final Creator<KeepalivePacketData> CREATOR = new Creator<KeepalivePacketData>() {
        public KeepalivePacketData createFromParcel(Parcel in) {
            return new KeepalivePacketData(in);
        }

        public KeepalivePacketData[] newArray(int size) {
            return new KeepalivePacketData[size];
        }
    };
    protected static final int IPV4_HEADER_LENGTH = 20;
    private static final String TAG = "KeepalivePacketData";
    protected static final int UDP_HEADER_LENGTH = 8;
    public final InetAddress dstAddress;
    public final int dstPort;
    private final byte[] mPacket;
    public final InetAddress srcAddress;
    public final int srcPort;

    protected KeepalivePacketData(InetAddress srcAddress, int srcPort, InetAddress dstAddress, int dstPort, byte[] data) throws InvalidPacketException {
        this.srcAddress = srcAddress;
        this.dstAddress = dstAddress;
        this.srcPort = srcPort;
        this.dstPort = dstPort;
        this.mPacket = data;
        String str = TAG;
        if (srcAddress == null || dstAddress == null || !srcAddress.getClass().getName().equals(dstAddress.getClass().getName())) {
            Log.e(str, "Invalid or mismatched InetAddresses in KeepalivePacketData");
            throw new InvalidPacketException(-21);
        } else if (!IpUtils.isValidUdpOrTcpPort(srcPort) || !IpUtils.isValidUdpOrTcpPort(dstPort)) {
            Log.e(str, "Invalid ports in KeepalivePacketData");
            throw new InvalidPacketException(-22);
        }
    }

    public byte[] getPacket() {
        return (byte[]) this.mPacket.clone();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.srcAddress.getHostAddress());
        out.writeString(this.dstAddress.getHostAddress());
        out.writeInt(this.srcPort);
        out.writeInt(this.dstPort);
        out.writeByteArray(this.mPacket);
    }

    protected KeepalivePacketData(Parcel in) {
        this.srcAddress = NetworkUtils.numericToInetAddress(in.readString());
        this.dstAddress = NetworkUtils.numericToInetAddress(in.readString());
        this.srcPort = in.readInt();
        this.dstPort = in.readInt();
        this.mPacket = in.createByteArray();
    }
}
