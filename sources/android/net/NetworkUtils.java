package android.net;

import android.annotation.UnsupportedAppUsage;
import android.net.DnsResolver.DnsResponse;
import android.net.shared.Inet4AddressUtils;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.util.Pair;
import java.io.FileDescriptor;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import org.apache.miui.commons.lang3.ClassUtils;

public class NetworkUtils {
    private static final int[] ADDRESS_FAMILIES = new int[]{OsConstants.AF_INET, OsConstants.AF_INET6};
    private static final String TAG = "NetworkUtils";

    public static native void attachDropAllBPFFilter(FileDescriptor fileDescriptor) throws SocketException;

    public static native boolean bindProcessToNetwork(int i);

    @Deprecated
    public static native boolean bindProcessToNetworkForHostResolution(int i);

    public static native int bindSocketToNetwork(int i, int i2);

    public static native void detachBPFFilter(FileDescriptor fileDescriptor) throws SocketException;

    public static native int getBoundNetworkForProcess();

    public static native Network getDnsNetwork() throws ErrnoException;

    public static native TcpRepairWindow getTcpRepairWindow(FileDescriptor fileDescriptor) throws ErrnoException;

    public static native boolean protectFromVpn(int i);

    public static native boolean queryUserAccess(int i, int i2);

    public static native void resNetworkCancel(FileDescriptor fileDescriptor);

    public static native FileDescriptor resNetworkQuery(int i, String str, int i2, int i3, int i4) throws ErrnoException;

    public static native DnsResponse resNetworkResult(FileDescriptor fileDescriptor) throws ErrnoException;

    public static native FileDescriptor resNetworkSend(int i, byte[] bArr, int i2, int i3) throws ErrnoException;

    public static native void setupRaSocket(FileDescriptor fileDescriptor, int i) throws SocketException;

    @UnsupportedAppUsage
    public static boolean protectFromVpn(FileDescriptor fd) {
        return protectFromVpn(fd.getInt$());
    }

    @Deprecated
    @UnsupportedAppUsage
    public static InetAddress intToInetAddress(int hostAddress) {
        return Inet4AddressUtils.intToInet4AddressHTL(hostAddress);
    }

    @Deprecated
    public static int inetAddressToInt(Inet4Address inetAddr) throws IllegalArgumentException {
        return Inet4AddressUtils.inet4AddressToIntHTL(inetAddr);
    }

    @Deprecated
    @UnsupportedAppUsage
    public static int prefixLengthToNetmaskInt(int prefixLength) throws IllegalArgumentException {
        return Inet4AddressUtils.prefixLengthToV4NetmaskIntHTL(prefixLength);
    }

    public static int netmaskIntToPrefixLength(int netmask) {
        return Integer.bitCount(netmask);
    }

    @Deprecated
    @UnsupportedAppUsage
    public static int netmaskToPrefixLength(Inet4Address netmask) {
        return Inet4AddressUtils.netmaskToPrefixLength(netmask);
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static InetAddress numericToInetAddress(String addrString) throws IllegalArgumentException {
        return InetAddress.parseNumericAddress(addrString);
    }

    public static void maskRawAddress(byte[] array, int prefixLength) {
        if (prefixLength < 0 || prefixLength > array.length * 8) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("IP address with ");
            stringBuilder.append(array.length);
            stringBuilder.append(" bytes has invalid prefix length ");
            stringBuilder.append(prefixLength);
            throw new RuntimeException(stringBuilder.toString());
        }
        int offset = prefixLength / 8;
        byte mask = (byte) ((byte) -1 << (8 - (prefixLength % 8)));
        if (offset < array.length) {
            array[offset] = (byte) (array[offset] & mask);
        }
        while (true) {
            offset++;
            if (offset < array.length) {
                array[offset] = (byte) 0;
            } else {
                return;
            }
        }
    }

    public static InetAddress getNetworkPart(InetAddress address, int prefixLength) {
        byte[] array = address.getAddress();
        maskRawAddress(array, prefixLength);
        try {
            return InetAddress.getByAddress(array);
        } catch (UnknownHostException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getNetworkPart error - ");
            stringBuilder.append(e.toString());
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage
    public static int getImplicitNetmask(Inet4Address address) {
        return Inet4AddressUtils.getImplicitNetmask(address);
    }

    public static Pair<InetAddress, Integer> parseIpAndMask(String ipAndMaskString) {
        InetAddress address = null;
        int prefixLength = -1;
        try {
            String[] pieces = ipAndMaskString.split("/", 2);
            prefixLength = Integer.parseInt(pieces[1]);
            address = InetAddress.parseNumericAddress(pieces[0]);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | NullPointerException | NumberFormatException e) {
        }
        if (address != null && prefixLength != -1) {
            return new Pair(address, Integer.valueOf(prefixLength));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid IP address and mask ");
        stringBuilder.append(ipAndMaskString);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static boolean addressTypeMatches(InetAddress left, InetAddress right) {
        return ((left instanceof Inet4Address) && (right instanceof Inet4Address)) || ((left instanceof Inet6Address) && (right instanceof Inet6Address));
    }

    public static InetAddress hexToInet6Address(String addrHexString) throws IllegalArgumentException {
        try {
            return numericToInetAddress(String.format(Locale.US, "%s:%s:%s:%s:%s:%s:%s:%s", new Object[]{addrHexString.substring(0, 4), addrHexString.substring(4, 8), addrHexString.substring(8, 12), addrHexString.substring(12, 16), addrHexString.substring(16, 20), addrHexString.substring(20, 24), addrHexString.substring(24, 28), addrHexString.substring(28, 32)}));
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("error in hexToInet6Address(");
            stringBuilder.append(addrHexString);
            stringBuilder.append("): ");
            stringBuilder.append(e);
            Log.e(TAG, stringBuilder.toString());
            throw new IllegalArgumentException(e);
        }
    }

    public static String[] makeStrings(Collection<InetAddress> addrs) {
        String[] result = new String[addrs.size()];
        int i = 0;
        for (InetAddress addr : addrs) {
            int i2 = i + 1;
            result[i] = addr.getHostAddress();
            i = i2;
        }
        return result;
    }

    @UnsupportedAppUsage
    public static String trimV4AddrZeros(String addr) {
        if (addr == null) {
            return null;
        }
        String[] octets = addr.split("\\.");
        if (octets.length != 4) {
            return addr;
        }
        StringBuilder builder = new StringBuilder(16);
        int i = 0;
        while (i < 4) {
            try {
                if (octets[i].length() > 3) {
                    return addr;
                }
                builder.append(Integer.parseInt(octets[i]));
                if (i < 3) {
                    builder.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                }
                i++;
            } catch (NumberFormatException e) {
                return addr;
            }
        }
        return builder.toString();
    }

    private static TreeSet<IpPrefix> deduplicatePrefixSet(TreeSet<IpPrefix> src) {
        TreeSet<IpPrefix> dst = new TreeSet(src.comparator());
        Iterator it = src.iterator();
        while (it.hasNext()) {
            IpPrefix newPrefix = (IpPrefix) it.next();
            Iterator it2 = dst.iterator();
            while (it2.hasNext()) {
                if (((IpPrefix) it2.next()).containsPrefix(newPrefix)) {
                    break;
                }
            }
            dst.add(newPrefix);
        }
        return dst;
    }

    public static long routedIPv4AddressCount(TreeSet<IpPrefix> prefixes) {
        long routedIPCount = 0;
        Iterator it = deduplicatePrefixSet(prefixes).iterator();
        while (it.hasNext()) {
            IpPrefix prefix = (IpPrefix) it.next();
            if (!prefix.isIPv4()) {
                Log.wtf(TAG, "Non-IPv4 prefix in routedIPv4AddressCount");
            }
            routedIPCount += 1 << (32 - prefix.getPrefixLength());
        }
        return routedIPCount;
    }

    public static BigInteger routedIPv6AddressCount(TreeSet<IpPrefix> prefixes) {
        BigInteger routedIPCount = BigInteger.ZERO;
        Iterator it = deduplicatePrefixSet(prefixes).iterator();
        while (it.hasNext()) {
            IpPrefix prefix = (IpPrefix) it.next();
            if (!prefix.isIPv6()) {
                Log.wtf(TAG, "Non-IPv6 prefix in routedIPv6AddressCount");
            }
            routedIPCount = routedIPCount.add(BigInteger.ONE.shiftLeft(128 - prefix.getPrefixLength()));
        }
        return routedIPCount;
    }

    public static boolean isWeaklyValidatedHostname(String hostname) {
        String weakHostnameRegex = "^[a-zA-Z0-9_.-]+$";
        if (!hostname.matches("^[a-zA-Z0-9_.-]+$")) {
            return false;
        }
        for (int address_family : ADDRESS_FAMILIES) {
            if (Os.inet_pton(address_family, hostname) != null) {
                return false;
            }
        }
        return true;
    }
}
