package android.util;

import android.net.Network;
import android.net.SntpClient;
import java.net.InetAddress;

class NtpTrustedTimeInjector {
    private static final boolean DBG = true;
    private static final int NTP_PORT = 123;
    private static final String TAG = "NtpTrustedTimeInjector";

    NtpTrustedTimeInjector() {
    }

    static boolean requestTime(SntpClient client, String host, int timeout, Network network) {
        String str = TAG;
        StringBuilder stringBuilder;
        try {
            for (InetAddress addr : InetAddress.getAllByName(host)) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("host ");
                stringBuilder2.append(host);
                stringBuilder2.append(", which address ");
                stringBuilder2.append(addr.getHostAddress());
                Log.d(str, stringBuilder2.toString());
                if (client.requestTime(addr, 123, timeout, network)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("client.requestTime = ");
                    stringBuilder.append(client.getNtpTime());
                    stringBuilder.append(", cachedNtpElapsedRealtime = ");
                    stringBuilder.append(client.getNtpTimeReference());
                    Log.d(str, stringBuilder.toString());
                    return true;
                }
            }
        } catch (Exception e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("requestTime: ");
            stringBuilder.append(e);
            Log.d(str, stringBuilder.toString());
        }
        return false;
    }
}
