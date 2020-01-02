package android.net;

import android.bluetooth.BluetoothHidDevice;
import android.net.SocketKeepalive.InvalidPacketException;
import android.net.util.IpUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.system.OsConstants;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class NattKeepalivePacketData extends KeepalivePacketData implements Parcelable {
    public static final Creator<NattKeepalivePacketData> CREATOR = new Creator<NattKeepalivePacketData>() {
        public NattKeepalivePacketData createFromParcel(Parcel in) {
            try {
                return NattKeepalivePacketData.nattKeepalivePacket(InetAddresses.parseNumericAddress(in.readString()), in.readInt(), InetAddresses.parseNumericAddress(in.readString()), in.readInt());
            } catch (InvalidPacketException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid NAT-T keepalive data: ");
                stringBuilder.append(e.error);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        public NattKeepalivePacketData[] newArray(int size) {
            return new NattKeepalivePacketData[size];
        }
    };

    private NattKeepalivePacketData(InetAddress srcAddress, int srcPort, InetAddress dstAddress, int dstPort, byte[] data) throws InvalidPacketException {
        super(srcAddress, srcPort, dstAddress, dstPort, data);
    }

    public static NattKeepalivePacketData nattKeepalivePacket(InetAddress srcAddress, int srcPort, InetAddress dstAddress, int dstPort) throws InvalidPacketException {
        if (!(srcAddress instanceof Inet4Address) || !(dstAddress instanceof Inet4Address)) {
            throw new InvalidPacketException(-21);
        } else if (dstPort == 4500) {
            ByteBuffer buf = ByteBuffer.allocate(29);
            buf.order(ByteOrder.BIG_ENDIAN);
            buf.putShort((short) 17664);
            buf.putShort((short) 29);
            buf.putInt(0);
            buf.put(BluetoothHidDevice.SUBCLASS1_KEYBOARD);
            buf.put((byte) OsConstants.IPPROTO_UDP);
            int ipChecksumOffset = buf.position();
            buf.putShort((short) 0);
            buf.put(srcAddress.getAddress());
            buf.put(dstAddress.getAddress());
            buf.putShort((short) srcPort);
            buf.putShort((short) dstPort);
            buf.putShort((short) (29 - 20));
            int udpChecksumOffset = buf.position();
            buf.putShort((short) 0);
            buf.put((byte) -1);
            buf.putShort(ipChecksumOffset, IpUtils.ipChecksum(buf, 0));
            buf.putShort(udpChecksumOffset, IpUtils.udpChecksum(buf, 0, 20));
            return new NattKeepalivePacketData(srcAddress, srcPort, dstAddress, dstPort, buf.array());
        } else {
            throw new InvalidPacketException(-22);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.srcAddress.getHostAddress());
        out.writeString(this.dstAddress.getHostAddress());
        out.writeInt(this.srcPort);
        out.writeInt(this.dstPort);
    }
}
