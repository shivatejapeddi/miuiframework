package android.net;

public class PrivateDnsConnectivityChecker {
    private static final int CONNECTION_TIMEOUT_MS = 5000;
    private static final int PRIVATE_DNS_PORT = 853;
    private static final String TAG = "NetworkUtils";

    private PrivateDnsConnectivityChecker() {
    }

    /* JADX WARNING: Missing block: B:20:0x0052, code skipped:
            if (r4 != null) goto L_0x0054;
     */
    /* JADX WARNING: Missing block: B:22:?, code skipped:
            r4.close();
     */
    public static boolean canConnectToPrivateDnsServer(java.lang.String r8) {
        /*
        r0 = "NetworkUtils";
        r1 = javax.net.ssl.SSLSocketFactory.getDefault();
        r2 = -251; // 0xffffffffffffff05 float:NaN double:NaN;
        android.net.TrafficStats.setThreadStatsTag(r2);
        r2 = 1;
        r3 = 0;
        r4 = r1.createSocket();	 Catch:{ IOException -> 0x005d }
        r4 = (javax.net.ssl.SSLSocket) r4;	 Catch:{ IOException -> 0x005d }
        r5 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r4.setSoTimeout(r5);	 Catch:{ all -> 0x004f }
        r5 = new java.net.InetSocketAddress;	 Catch:{ all -> 0x004f }
        r6 = 853; // 0x355 float:1.195E-42 double:4.214E-321;
        r5.<init>(r8, r6);	 Catch:{ all -> 0x004f }
        r4.connect(r5);	 Catch:{ all -> 0x004f }
        r5 = r4.isConnected();	 Catch:{ all -> 0x004f }
        if (r5 != 0) goto L_0x003a;
    L_0x0028:
        r5 = "Connection to %s failed.";
        r6 = new java.lang.Object[r2];	 Catch:{ all -> 0x004f }
        r6[r3] = r8;	 Catch:{ all -> 0x004f }
        r5 = java.lang.String.format(r5, r6);	 Catch:{ all -> 0x004f }
        android.util.Log.w(r0, r5);	 Catch:{ all -> 0x004f }
        r4.close();	 Catch:{ IOException -> 0x005d }
        return r3;
    L_0x003a:
        r4.startHandshake();	 Catch:{ all -> 0x004f }
        r5 = "TLS handshake to %s succeeded.";
        r6 = new java.lang.Object[r2];	 Catch:{ all -> 0x004f }
        r6[r3] = r8;	 Catch:{ all -> 0x004f }
        r5 = java.lang.String.format(r5, r6);	 Catch:{ all -> 0x004f }
        android.util.Log.w(r0, r5);	 Catch:{ all -> 0x004f }
        r4.close();	 Catch:{ IOException -> 0x005d }
        return r2;
    L_0x004f:
        r5 = move-exception;
        throw r5;	 Catch:{ all -> 0x0051 }
    L_0x0051:
        r6 = move-exception;
        if (r4 == 0) goto L_0x005c;
    L_0x0054:
        r4.close();	 Catch:{ all -> 0x0058 }
        goto L_0x005c;
    L_0x0058:
        r7 = move-exception;
        r5.addSuppressed(r7);	 Catch:{ IOException -> 0x005d }
    L_0x005c:
        throw r6;	 Catch:{ IOException -> 0x005d }
    L_0x005d:
        r4 = move-exception;
        r2 = new java.lang.Object[r2];
        r2[r3] = r8;
        r5 = "TLS handshake to %s failed.";
        r2 = java.lang.String.format(r5, r2);
        android.util.Log.w(r0, r2, r4);
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.PrivateDnsConnectivityChecker.canConnectToPrivateDnsServer(java.lang.String):boolean");
    }
}
