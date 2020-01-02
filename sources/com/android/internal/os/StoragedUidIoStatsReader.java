package com.android.internal.os;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StoragedUidIoStatsReader {
    private static final String TAG = StoragedUidIoStatsReader.class.getSimpleName();
    private static String sUidIoFile = "/proc/uid_io/stats";

    public interface Callback {
        void onUidStorageStats(int i, long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10);
    }

    @VisibleForTesting
    public StoragedUidIoStatsReader(String file) {
        sUidIoFile = file;
    }

    public void readAbsolute(Callback callback) {
        String uidStr;
        Throwable th;
        String str = ": ";
        int oldMask = StrictMode.allowThreadDiskReadsMask();
        try {
            BufferedReader reader = Files.newBufferedReader(new File(sUidIoFile).toPath());
            while (true) {
                try {
                    String readLine = reader.readLine();
                    String line = readLine;
                    if (readLine == null) {
                        break;
                    }
                    String[] fields = TextUtils.split(line, WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    if (fields.length != 11) {
                        readLine = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Malformed entry in ");
                        stringBuilder.append(sUidIoFile);
                        stringBuilder.append(str);
                        stringBuilder.append(line);
                        Slog.e(readLine, stringBuilder.toString());
                    } else {
                        uidStr = fields[0];
                        callback.onUidStorageStats(Integer.parseInt(fields[0], 10), Long.parseLong(fields[1], 10), Long.parseLong(fields[2], 10), Long.parseLong(fields[3], 10), Long.parseLong(fields[4], 10), Long.parseLong(fields[5], 10), Long.parseLong(fields[6], 10), Long.parseLong(fields[7], 10), Long.parseLong(fields[8], 10), Long.parseLong(fields[9], 10), Long.parseLong(fields[10], 10));
                    }
                } catch (NumberFormatException e) {
                    uidStr = TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Could not parse entry in ");
                    stringBuilder2.append(sUidIoFile);
                    stringBuilder2.append(str);
                    stringBuilder2.append(e.getMessage());
                    Slog.e(uidStr, stringBuilder2.toString());
                } catch (Throwable th2) {
                    th = th2;
                    try {
                    } catch (Throwable th22) {
                        Throwable th3 = th22;
                        if (reader != null) {
                            reader.close();
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e2) {
            try {
                String str2 = TAG;
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Failed to read ");
                stringBuilder3.append(sUidIoFile);
                stringBuilder3.append(str);
                stringBuilder3.append(e2.getMessage());
                Slog.e(str2, stringBuilder3.toString());
            } catch (Throwable th4) {
                StrictMode.setThreadPolicyMask(oldMask);
            }
        } catch (Throwable th222) {
            th.addSuppressed(th222);
        }
        StrictMode.setThreadPolicyMask(oldMask);
    }
}
