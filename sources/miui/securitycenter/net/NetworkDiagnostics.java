package miui.securitycenter.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkUtils;
import android.os.SystemClock;
import android.os.UserHandle;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructTimeval;
import android.util.Log;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import libcore.io.IoUtils;

public class NetworkDiagnostics {
    private static final String TAG = "NetworkDiagnostics";
    private static final InetAddress TEST_DNS4 = NetworkUtils.numericToInetAddress("8.8.8.8");
    private static final InetAddress TEST_DNS6 = NetworkUtils.numericToInetAddress("2001:4860:4860::8888");

    public enum DnsResponseCode {
        NOERROR,
        FORMERR,
        SERVFAIL,
        NXDOMAIN,
        NOTIMP,
        REFUSED
    }

    private static class SimpleSocketCheck implements Closeable {
        protected final int mAddressFamily;
        protected final long mDeadlineTime;
        protected FileDescriptor mFileDescriptor;
        protected final Integer mInterfaceIndex;
        protected Network mNetwork;
        protected SocketAddress mSocketAddress;
        protected final InetAddress mSource;
        protected final InetAddress mTarget;

        /* JADX WARNING: Removed duplicated region for block: B:17:0x0048  */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x0046  */
        protected SimpleSocketCheck(android.net.Network r5, android.net.LinkProperties r6, java.net.InetAddress r7, java.net.InetAddress r8, long r9) {
            /*
            r4 = this;
            r4.<init>();
            r4.mNetwork = r5;
            r0 = miui.securitycenter.net.NetworkDiagnostics.now();
            r0 = r0 + r9;
            r4.mDeadlineTime = r0;
            r0 = 0;
            if (r6 == 0) goto L_0x001a;
        L_0x000f:
            r1 = r6.getInterfaceName();
            r1 = miui.securitycenter.net.NetworkDiagnostics.getInterfaceIndex(r1);
            r4.mInterfaceIndex = r1;
            goto L_0x001c;
        L_0x001a:
            r4.mInterfaceIndex = r0;
        L_0x001c:
            r1 = r8 instanceof java.net.Inet6Address;
            if (r1 == 0) goto L_0x0050;
        L_0x0020:
            r1 = 0;
            r2 = r8.isLinkLocalAddress();
            if (r2 == 0) goto L_0x0043;
        L_0x0027:
            r2 = r4.mInterfaceIndex;
            if (r2 == 0) goto L_0x0043;
            r2 = r8.getAddress();	 Catch:{ UnknownHostException -> 0x003b }
            r3 = r4.mInterfaceIndex;	 Catch:{ UnknownHostException -> 0x003b }
            r3 = r3.intValue();	 Catch:{ UnknownHostException -> 0x003b }
            r0 = java.net.Inet6Address.getByAddress(r0, r2, r3);	 Catch:{ UnknownHostException -> 0x003b }
            goto L_0x0044;
        L_0x003b:
            r0 = move-exception;
            r2 = "NetworkDiagnostics";
            r3 = "SimpleSocketCheck";
            android.util.Log.e(r2, r3, r0);
        L_0x0043:
            r0 = r1;
        L_0x0044:
            if (r0 == 0) goto L_0x0048;
        L_0x0046:
            r1 = r0;
            goto L_0x0049;
        L_0x0048:
            r1 = r8;
        L_0x0049:
            r4.mTarget = r1;
            r1 = android.system.OsConstants.AF_INET6;
            r4.mAddressFamily = r1;
            goto L_0x0056;
        L_0x0050:
            r4.mTarget = r8;
            r0 = android.system.OsConstants.AF_INET;
            r4.mAddressFamily = r0;
        L_0x0056:
            r4.mSource = r7;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.securitycenter.net.NetworkDiagnostics$SimpleSocketCheck.<init>(android.net.Network, android.net.LinkProperties, java.net.InetAddress, java.net.InetAddress, long):void");
        }

        protected SimpleSocketCheck(Network network, LinkProperties lp, InetAddress target, long timeoutMs) {
            this(network, lp, null, target, timeoutMs);
        }

        /* Access modifiers changed, original: protected */
        public void setupSocket(int sockType, int protocol, long writeTimeout, long readTimeout, int dstPort) throws ErrnoException, IOException {
            this.mFileDescriptor = Os.socket(this.mAddressFamily, sockType, protocol);
            Os.setsockoptTimeval(this.mFileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_SNDTIMEO, StructTimeval.fromMillis(writeTimeout));
            Os.setsockoptTimeval(this.mFileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_RCVTIMEO, StructTimeval.fromMillis(readTimeout));
            this.mNetwork.bindSocket(this.mFileDescriptor);
            InetAddress inetAddress = this.mSource;
            if (inetAddress != null) {
                Os.bind(this.mFileDescriptor, inetAddress, 0);
            }
            Os.connect(this.mFileDescriptor, this.mTarget, dstPort);
            this.mSocketAddress = Os.getsockname(this.mFileDescriptor);
        }

        /* Access modifiers changed, original: protected */
        public String getSocketAddressString() {
            return String.format(this.mSocketAddress.getAddress() instanceof Inet6Address ? "[%s]:%d" : "%s:%d", new Object[]{this.mSocketAddress.getAddress().getHostAddress(), Integer.valueOf(this.mSocketAddress.getPort())});
        }

        public void close() {
            IoUtils.closeQuietly(this.mFileDescriptor);
        }
    }

    private static class DnsUdpCheck extends SimpleSocketCheck {
        private static final int DNS_SERVER_PORT = 53;
        private static final int PACKET_BUFSIZE = 512;
        private static final int RR_TYPE_A = 1;
        private static final int RR_TYPE_AAAA = 28;
        private static final int TIMEOUT_RECV = 500;
        private static final int TIMEOUT_SEND = 100;
        private final int mQueryType;
        private final Random mRandom = new Random();

        public DnsUdpCheck(Network network, LinkProperties lp, InetAddress target, long timeoutMs) {
            super(network, lp, target, timeoutMs);
            if (this.mAddressFamily == OsConstants.AF_INET6) {
                this.mQueryType = 28;
            } else {
                this.mQueryType = 1;
            }
        }

        public boolean call() {
            String str = "DnsUdpCheck";
            String str2 = NetworkDiagnostics.TAG;
            boolean ret = false;
            try {
                setupSocket(OsConstants.SOCK_DGRAM, OsConstants.IPPROTO_UDP, 100, 500, 53);
                byte[] dnsPacket = getDnsQueryPacket(Integer.valueOf(this.mRandom.nextInt(900000) + UserHandle.PER_USER_RANGE).toString());
                int count = 0;
                while (NetworkDiagnostics.now() < this.mDeadlineTime - 1000) {
                    count++;
                    try {
                        Os.write(this.mFileDescriptor, dnsPacket, 0, dnsPacket.length);
                        try {
                            Os.read(this.mFileDescriptor, ByteBuffer.allocate(512));
                            ret = true;
                            break;
                        } catch (ErrnoException | InterruptedIOException e) {
                        }
                    } catch (ErrnoException | InterruptedIOException e2) {
                        Log.e(str2, str, e2);
                    }
                }
                close();
                return ret;
            } catch (ErrnoException | IOException e3) {
                Log.e(str2, str, e3);
                return false;
            }
        }

        private byte[] getDnsQueryPacket(String sixRandomDigits) {
            byte[] rnd = sixRandomDigits.getBytes(StandardCharsets.US_ASCII);
            return new byte[]{(byte) this.mRandom.nextInt(), (byte) this.mRandom.nextInt(), (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 17, rnd[0], rnd[1], rnd[2], rnd[3], rnd[4], rnd[5], (byte) 45, (byte) 97, (byte) 110, (byte) 100, (byte) 114, (byte) 111, (byte) 105, (byte) 100, (byte) 45, (byte) 100, (byte) 115, (byte) 6, (byte) 109, (byte) 101, (byte) 116, (byte) 114, (byte) 105, (byte) 99, (byte) 7, (byte) 103, (byte) 115, (byte) 116, (byte) 97, (byte) 116, (byte) 105, (byte) 99, (byte) 3, (byte) 99, (byte) 111, (byte) 109, (byte) 0, (byte) 0, (byte) this.mQueryType, (byte) 0, (byte) 1};
        }
    }

    private static class IcmpCheck extends SimpleSocketCheck {
        private static final int ICMPV4_ECHO_REQUEST = 8;
        private static final int ICMPV6_ECHO_REQUEST = 128;
        private static final int PACKET_BUFSIZE = 512;
        private static final int TIMEOUT_RECV = 300;
        private static final int TIMEOUT_SEND = 100;
        private final int mIcmpType;
        private final int mProtocol;

        public IcmpCheck(Network network, LinkProperties lp, InetAddress source, InetAddress target, long timeoutMs) {
            super(network, lp, source, target, timeoutMs);
            if (this.mAddressFamily == OsConstants.AF_INET6) {
                this.mProtocol = OsConstants.IPPROTO_ICMPV6;
                this.mIcmpType = 128;
                return;
            }
            this.mProtocol = OsConstants.IPPROTO_ICMP;
            this.mIcmpType = 8;
        }

        public IcmpCheck(Network network, LinkProperties lp, InetAddress target, long timeoutMs) {
            this(network, lp, null, target, timeoutMs);
        }

        public boolean call() throws Exception {
            String str = "IcmpCheck";
            String str2 = NetworkDiagnostics.TAG;
            boolean ret = false;
            try {
                setupSocket(OsConstants.SOCK_DGRAM, this.mProtocol, 100, 300, 0);
                byte[] icmpPacket = new byte[]{(byte) this.mIcmpType, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
                int count = 0;
                while (NetworkDiagnostics.now() < this.mDeadlineTime - 400) {
                    count++;
                    icmpPacket[icmpPacket.length - 1] = (byte) count;
                    try {
                        Os.write(this.mFileDescriptor, icmpPacket, 0, icmpPacket.length);
                        try {
                            Os.read(this.mFileDescriptor, ByteBuffer.allocate(512));
                            ret = true;
                            break;
                        } catch (ErrnoException | InterruptedIOException e) {
                        }
                    } catch (ErrnoException | InterruptedIOException e2) {
                        Log.e(str2, str, e2);
                    }
                }
                close();
                return ret;
            } catch (ErrnoException | IOException e3) {
                Log.e(str2, str, e3);
                return false;
            }
        }
    }

    private static final long now() {
        return SystemClock.elapsedRealtime();
    }

    private static Integer getInterfaceIndex(String ifname) {
        try {
            return Integer.valueOf(NetworkInterface.getByName(ifname).getIndex());
        } catch (NullPointerException | SocketException e) {
            return null;
        }
    }

    public static Boolean activeNetworkIcmpCheck(Context context, InetAddress target, Long timeoutMs) {
        Boolean valueOf = Boolean.valueOf(false);
        if (context == null || timeoutMs.longValue() == 0) {
            Log.i(TAG, "activeNetworkIcmpCheck. invalidate parameter");
            return valueOf;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm == null) {
            return valueOf;
        }
        LinkProperties lp = cm.getActiveLinkProperties();
        if (target == null && lp != null) {
            if (lp.hasGlobalIPv6Address() || lp.hasIPv6DefaultRoute()) {
                target = Inet6Address.LOOPBACK;
            } else {
                target = Inet4Address.LOOPBACK;
            }
        }
        return Boolean.valueOf(icmpCheck(cm.getActiveNetwork(), lp, target, timeoutMs.longValue()));
    }

    public static boolean icmpCheck(Network network, LinkProperties lp, InetAddress target, long timeoutMs) {
        String str = TAG;
        if (network == null || lp == null || target == null || timeoutMs == 0) {
            Log.i(str, "icmpCheck. invalidate parameter");
            return false;
        }
        try {
            return new IcmpCheck(network, new LinkProperties(lp), target, timeoutMs).call();
        } catch (Exception e) {
            Log.d(str, "icmpCheck", e);
            return false;
        }
    }

    public static Boolean activeNetworkDnsCheck(Context context, InetAddress target, Long timeoutMs) {
        Boolean valueOf = Boolean.valueOf(false);
        if (context == null || timeoutMs.longValue() == 0) {
            Log.i(TAG, "activeNetworkIcmpCheck. invalidate parameter");
            return valueOf;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm == null) {
            return valueOf;
        }
        LinkProperties lp = cm.getActiveLinkProperties();
        if (target == null && lp != null) {
            if (lp.hasGlobalIPv6Address() || lp.hasIPv6DefaultRoute()) {
                target = TEST_DNS4;
            } else {
                target = TEST_DNS6;
            }
        }
        return Boolean.valueOf(dnsCheck(cm.getActiveNetwork(), lp, target, timeoutMs));
    }

    public static boolean dnsCheck(Network network, LinkProperties lp, InetAddress target, Long timeoutMs) {
        String str = TAG;
        if (!(network == null || lp == null || target == null)) {
            try {
                if (timeoutMs.longValue() != 0) {
                    return new DnsUdpCheck(network, new LinkProperties(lp), target, timeoutMs.longValue()).call();
                }
            } catch (Exception e) {
                Log.d(str, "dnsCheck", e);
                return false;
            }
        }
        Log.i(str, "dnsCheck. invalidate parameter");
        return false;
    }
}
