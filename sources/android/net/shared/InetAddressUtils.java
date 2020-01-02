package android.net.shared;

import android.os.Parcel;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressUtils {
    public static void parcelInetAddress(Parcel parcel, InetAddress address, int flags) {
        parcel.writeByteArray(address != null ? address.getAddress() : null);
    }

    public static InetAddress unparcelInetAddress(Parcel in) {
        byte[] addressArray = in.createByteArray();
        InetAddress inetAddress = null;
        if (addressArray == null) {
            return null;
        }
        try {
            inetAddress = InetAddress.getByAddress(addressArray);
            return inetAddress;
        } catch (UnknownHostException e) {
            return inetAddress;
        }
    }

    private InetAddressUtils() {
    }
}
