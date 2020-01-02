package com.android.internal.os;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;

@VisibleForTesting(visibility = Visibility.PROTECTED)
public final class ProcStatsUtil {
    private static final boolean DEBUG = false;
    private static final int READ_SIZE = 1024;
    private static final String TAG = "ProcStatsUtil";

    private ProcStatsUtil() {
    }

    @VisibleForTesting(visibility = Visibility.PROTECTED)
    public static String readNullSeparatedFile(String path) {
        String contents = readSingleLineProcFile(path);
        if (contents == null) {
            return null;
        }
        int endIndex = contents.indexOf("\u0000\u0000");
        if (endIndex != -1) {
            contents = contents.substring(0, endIndex);
        }
        return contents.replace("\u0000", WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
    }

    @VisibleForTesting(visibility = Visibility.PROTECTED)
    public static String readSingleLineProcFile(String path) {
        return readTerminatedProcFile(path, (byte) 10);
    }

    /* JADX WARNING: Missing block: B:53:?, code skipped:
            r1.close();
     */
    public static java.lang.String readTerminatedProcFile(java.lang.String r10, byte r11) {
        /*
        r0 = android.os.StrictMode.allowThreadDiskReads();
        r1 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0074, all -> 0x006f }
        r1.<init>(r10);	 Catch:{ IOException -> 0x0074, all -> 0x006f }
        r2 = 0;
        r3 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r4 = new byte[r3];	 Catch:{ all -> 0x0063 }
    L_0x000e:
        r5 = r1.read(r4);	 Catch:{ all -> 0x0063 }
        if (r5 > 0) goto L_0x0015;
    L_0x0014:
        goto L_0x004c;
    L_0x0015:
        r6 = -1;
        r7 = 0;
    L_0x0017:
        if (r7 >= r5) goto L_0x0022;
    L_0x0019:
        r8 = r4[r7];	 Catch:{ all -> 0x0063 }
        if (r8 != r11) goto L_0x001f;
    L_0x001d:
        r6 = r7;
        goto L_0x0022;
    L_0x001f:
        r7 = r7 + 1;
        goto L_0x0017;
    L_0x0022:
        r7 = -1;
        r8 = 0;
        if (r6 == r7) goto L_0x0028;
    L_0x0026:
        r7 = 1;
        goto L_0x0029;
    L_0x0028:
        r7 = r8;
    L_0x0029:
        if (r7 == 0) goto L_0x0039;
    L_0x002b:
        if (r2 != 0) goto L_0x0039;
    L_0x002d:
        r3 = new java.lang.String;	 Catch:{ all -> 0x0063 }
        r3.<init>(r4, r8, r6);	 Catch:{ all -> 0x0063 }
        r1.close();	 Catch:{ IOException -> 0x0074, all -> 0x006f }
        android.os.StrictMode.setThreadPolicy(r0);
        return r3;
    L_0x0039:
        if (r2 != 0) goto L_0x0041;
    L_0x003b:
        r9 = new java.io.ByteArrayOutputStream;	 Catch:{ all -> 0x0063 }
        r9.<init>(r3);	 Catch:{ all -> 0x0063 }
        r2 = r9;
    L_0x0041:
        if (r7 == 0) goto L_0x0045;
    L_0x0043:
        r9 = r6;
        goto L_0x0046;
    L_0x0045:
        r9 = r5;
    L_0x0046:
        r2.write(r4, r8, r9);	 Catch:{ all -> 0x0063 }
        if (r7 == 0) goto L_0x0062;
    L_0x004c:
        if (r2 != 0) goto L_0x0057;
    L_0x004e:
        r3 = "";
        r1.close();	 Catch:{ IOException -> 0x0074, all -> 0x006f }
        android.os.StrictMode.setThreadPolicy(r0);
        return r3;
    L_0x0057:
        r3 = r2.toString();	 Catch:{ all -> 0x0063 }
        r1.close();	 Catch:{ IOException -> 0x0074, all -> 0x006f }
        android.os.StrictMode.setThreadPolicy(r0);
        return r3;
    L_0x0062:
        goto L_0x000e;
    L_0x0063:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x0065 }
    L_0x0065:
        r3 = move-exception;
        r1.close();	 Catch:{ all -> 0x006a }
        goto L_0x006e;
    L_0x006a:
        r4 = move-exception;
        r2.addSuppressed(r4);	 Catch:{ IOException -> 0x0074, all -> 0x006f }
    L_0x006e:
        throw r3;	 Catch:{ IOException -> 0x0074, all -> 0x006f }
    L_0x006f:
        r1 = move-exception;
        android.os.StrictMode.setThreadPolicy(r0);
        throw r1;
    L_0x0074:
        r1 = move-exception;
        r2 = 0;
        android.os.StrictMode.setThreadPolicy(r0);
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ProcStatsUtil.readTerminatedProcFile(java.lang.String, byte):java.lang.String");
    }
}
