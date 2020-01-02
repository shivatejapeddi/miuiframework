package android.net.wifi.aware;

import android.net.TransportInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.net.Inet6Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;

public final class WifiAwareNetworkInfo implements TransportInfo, Parcelable {
    public static final Creator<WifiAwareNetworkInfo> CREATOR = new Creator<WifiAwareNetworkInfo>() {
        public WifiAwareNetworkInfo createFromParcel(Parcel in) {
            try {
                byte[] addr = in.createByteArray();
                String interfaceName = in.readString();
                NetworkInterface ni = null;
                if (interfaceName != null) {
                    try {
                        ni = NetworkInterface.getByName(interfaceName);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }
                return new WifiAwareNetworkInfo(Inet6Address.getByAddress(null, addr, ni), in.readInt(), in.readInt());
            } catch (UnknownHostException e2) {
                e2.printStackTrace();
                return null;
            }
        }

        public WifiAwareNetworkInfo[] newArray(int size) {
            return new WifiAwareNetworkInfo[size];
        }
    };
    private Inet6Address mIpv6Addr;
    private int mPort = 0;
    private int mTransportProtocol = -1;

    public WifiAwareNetworkInfo(Inet6Address ipv6Addr) {
        this.mIpv6Addr = ipv6Addr;
    }

    public WifiAwareNetworkInfo(Inet6Address ipv6Addr, int port, int transportProtocol) {
        this.mIpv6Addr = ipv6Addr;
        this.mPort = port;
        this.mTransportProtocol = transportProtocol;
    }

    public Inet6Address getPeerIpv6Addr() {
        return this.mIpv6Addr;
    }

    public int getPort() {
        return this.mPort;
    }

    public int getTransportProtocol() {
        return this.mTransportProtocol;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.mIpv6Addr.getAddress());
        NetworkInterface ni = this.mIpv6Addr.getScopedInterface();
        dest.writeString(ni == null ? null : ni.getName());
        dest.writeInt(this.mPort);
        dest.writeInt(this.mTransportProtocol);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("AwareNetworkInfo: IPv6=");
        stringBuilder.append(this.mIpv6Addr);
        stringBuilder.append(", port=");
        stringBuilder.append(this.mPort);
        stringBuilder.append(", transportProtocol=");
        stringBuilder.append(this.mTransportProtocol);
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiAwareNetworkInfo)) {
            return false;
        }
        WifiAwareNetworkInfo lhs = (WifiAwareNetworkInfo) obj;
        if (!(Objects.equals(this.mIpv6Addr, lhs.mIpv6Addr) && this.mPort == lhs.mPort && this.mTransportProtocol == lhs.mTransportProtocol)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mIpv6Addr, Integer.valueOf(this.mPort), Integer.valueOf(this.mTransportProtocol)});
    }
}
