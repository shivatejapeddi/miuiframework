package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.util.TrafficStatsConstants;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class SntpClient {
    private static final boolean DBG = true;
    private static final int NTP_LEAP_NOSYNC = 3;
    private static final int NTP_MODE_BROADCAST = 5;
    private static final int NTP_MODE_CLIENT = 3;
    private static final int NTP_MODE_SERVER = 4;
    private static final int NTP_PACKET_SIZE = 48;
    private static final int NTP_PORT = 123;
    private static final int NTP_STRATUM_DEATH = 0;
    private static final int NTP_STRATUM_MAX = 15;
    private static final int NTP_VERSION = 3;
    private static final long OFFSET_1900_TO_1970 = 2208988800L;
    private static final int ORIGINATE_TIME_OFFSET = 24;
    private static final int RECEIVE_TIME_OFFSET = 32;
    private static final int REFERENCE_TIME_OFFSET = 16;
    private static final String TAG = "SntpClient";
    private static final int TRANSMIT_TIME_OFFSET = 40;
    private long mNtpTime;
    private long mNtpTimeReference;
    private long mRoundTripTime;

    private static class InvalidServerReplyException extends Exception {
        public InvalidServerReplyException(String message) {
            super(message);
        }
    }

    public boolean requestTime(String host, int timeout, Network network) {
        Network networkForResolv = network.getPrivateDnsBypassingCopy();
        try {
            return requestTime(networkForResolv.getByName(host), 123, timeout, networkForResolv);
        } catch (Exception e) {
            EventLogTags.writeNtpFailure(host, e.toString());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("request time failed: ");
            stringBuilder.append(e);
            Log.d(TAG, stringBuilder.toString());
            return false;
        }
    }

    public boolean requestTime(InetAddress address, int port, int timeout, Network network) {
        String str = TAG;
        DatagramSocket socket = null;
        int oldTag = TrafficStats.getAndSetThreadStatsTag(TrafficStatsConstants.TAG_SYSTEM_NTP);
        try {
            socket = new DatagramSocket();
            network.bindSocket(socket);
            socket.setSoTimeout(timeout);
            byte[] buffer = new byte[48];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
            buffer[0] = GsmAlphabet.GSM_EXTENDED_ESCAPE;
            long requestTime = System.currentTimeMillis();
            long requestTicks = SystemClock.elapsedRealtime();
            writeTimeStamp(buffer, 40, requestTime);
            socket.send(request);
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            DatagramPacket response2 = response;
            long responseTicks = SystemClock.elapsedRealtime();
            long responseTime = requestTime + (responseTicks - requestTicks);
            byte leap = (byte) ((buffer[0] >> 6) & 3);
            byte mode = (byte) (buffer[0] & 7);
            int stratum = buffer[1] & 255;
            long originateTime = readTimeStamp(buffer, 24);
            long receiveTime = readTimeStamp(buffer, 32);
            long transmitTime = readTimeStamp(buffer, 40);
            checkValidServerReply(leap, mode, stratum, transmitTime);
            long roundTripTime = (responseTicks - requestTicks) - (transmitTime - receiveTime);
            long clockOffset = ((receiveTime - originateTime) + (transmitTime - responseTime)) / 2;
            EventLogTags.writeNtpSuccess(address.toString(), roundTripTime, clockOffset);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("round trip: ");
            stringBuilder.append(roundTripTime);
            stringBuilder.append("ms, clock offset: ");
            stringBuilder.append(clockOffset);
            stringBuilder.append("ms");
            Log.d(str, stringBuilder.toString());
            this.mNtpTime = responseTime + clockOffset;
            this.mNtpTimeReference = responseTicks;
            this.mRoundTripTime = roundTripTime;
            socket.close();
            TrafficStats.setThreadStatsTag(oldTag);
            return true;
        } catch (Exception e) {
            EventLogTags.writeNtpFailure(address.toString(), e.toString());
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("request time failed: ");
            stringBuilder2.append(e);
            Log.d(str, stringBuilder2.toString());
            if (socket != null) {
                socket.close();
            }
            TrafficStats.setThreadStatsTag(oldTag);
            return false;
        } catch (Throwable th) {
            if (socket != null) {
                socket.close();
            }
            TrafficStats.setThreadStatsTag(oldTag);
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public boolean requestTime(String host, int timeout) {
        Log.w(TAG, "Shame on you for calling the hidden API requestTime()!");
        return false;
    }

    @UnsupportedAppUsage
    public long getNtpTime() {
        return this.mNtpTime;
    }

    @UnsupportedAppUsage
    public long getNtpTimeReference() {
        return this.mNtpTimeReference;
    }

    @UnsupportedAppUsage
    public long getRoundTripTime() {
        return this.mRoundTripTime;
    }

    private static void checkValidServerReply(byte leap, byte mode, int stratum, long transmitTime) throws InvalidServerReplyException {
        StringBuilder stringBuilder;
        if (leap == (byte) 3) {
            throw new InvalidServerReplyException("unsynchronized server");
        } else if (mode != (byte) 4 && mode != (byte) 5) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("untrusted mode: ");
            stringBuilder.append(mode);
            throw new InvalidServerReplyException(stringBuilder.toString());
        } else if (stratum == 0 || stratum > 15) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("untrusted stratum: ");
            stringBuilder.append(stratum);
            throw new InvalidServerReplyException(stringBuilder.toString());
        } else if (transmitTime == 0) {
            throw new InvalidServerReplyException("zero transmitTime");
        }
    }

    private long read32(byte[] buffer, int offset) {
        byte b0 = buffer[offset];
        byte b1 = buffer[offset + 1];
        byte b2 = buffer[offset + 2];
        byte b3 = buffer[offset + 3];
        return (((((long) ((b0 & 128) == 128 ? (b0 & 127) + 128 : b0)) << 24) + (((long) ((b1 & 128) == 128 ? (b1 & 127) + 128 : b1)) << 16)) + (((long) ((b2 & 128) == 128 ? (b2 & 127) + 128 : b2)) << 8)) + ((long) ((b3 & 128) == 128 ? 128 + (b3 & 127) : b3));
    }

    private long readTimeStamp(byte[] buffer, int offset) {
        long seconds = read32(buffer, offset);
        long fraction = read32(buffer, offset + 4);
        if (seconds == 0 && fraction == 0) {
            return 0;
        }
        return ((seconds - OFFSET_1900_TO_1970) * 1000) + ((1000 * fraction) / 4294967296L);
    }

    private void writeTimeStamp(byte[] buffer, int offset, long time) {
        byte[] bArr = buffer;
        int i = offset;
        if (time == 0) {
            Arrays.fill(bArr, i, i + 8, (byte) 0);
            return;
        }
        long seconds = time / 1000;
        long milliseconds = time - (seconds * 1000);
        seconds += OFFSET_1900_TO_1970;
        int i2 = i + 1;
        bArr[i] = (byte) ((int) (seconds >> 24));
        i = i2 + 1;
        bArr[i2] = (byte) ((int) (seconds >> 16));
        i2 = i + 1;
        bArr[i] = (byte) ((int) (seconds >> 8));
        i = i2 + 1;
        bArr[i2] = (byte) ((int) (seconds >> 0));
        long fraction = (4294967296L * milliseconds) / 1000;
        int i3 = i + 1;
        bArr[i] = (byte) ((int) (fraction >> 24));
        i = i3 + 1;
        bArr[i3] = (byte) ((int) (fraction >> 16));
        i3 = i + 1;
        bArr[i] = (byte) ((int) (fraction >> 8));
        i = i3 + 1;
        bArr[i3] = (byte) ((int) (Math.random() * 255.0d));
    }
}
