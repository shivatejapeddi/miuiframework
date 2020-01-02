package com.android.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager.Stub;
import android.os.Build;
import android.os.DropBoxManager;
import android.os.Environment;
import android.os.FileObserver;
import android.os.FileUtils;
import android.os.RecoverySystem;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.provider.Downloads;
import android.text.TextUtils;
import android.util.AtomicFile;
import android.util.EventLog;
import android.util.Slog;
import android.util.StatsLogInternal;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.FastXmlSerializer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlSerializer;

public class BootReceiver extends BroadcastReceiver {
    private static final String FSCK_FS_MODIFIED = "FILE SYSTEM WAS MODIFIED";
    private static final String FSCK_PASS_PATTERN = "Pass ([1-9]E?):";
    private static final String FSCK_TREE_OPTIMIZATION_PATTERN = "Inode [0-9]+ extent tree.*could be shorter";
    private static final int FS_STAT_FS_FIXED = 1024;
    private static final String FS_STAT_PATTERN = "fs_stat,[^,]*/([^/,]+),(0x[0-9a-fA-F]+)";
    private static final String LAST_HEADER_FILE = "last-header.txt";
    private static final String[] LAST_KMSG_FILES = new String[]{"/sys/fs/pstore/console-ramoops", "/proc/last_kmsg"};
    private static final String LAST_SHUTDOWN_TIME_PATTERN = "powerctl_shutdown_time_ms:([0-9]+):([0-9]+)";
    private static final String LOG_FILES_FILE = "log-files.xml";
    private static final int LOG_SIZE = (SystemProperties.getInt("ro.debuggable", 0) == 1 ? 98304 : 65536);
    private static final String METRIC_SHUTDOWN_TIME_START = "begin_shutdown";
    private static final String METRIC_SYSTEM_SERVER = "shutdown_system_server";
    private static final String[] MOUNT_DURATION_PROPS_POSTFIX = new String[]{"early", "default", "late"};
    private static final String OLD_UPDATER_CLASS = "com.google.android.systemupdater.SystemUpdateReceiver";
    private static final String OLD_UPDATER_PACKAGE = "com.google.android.systemupdater";
    private static final String SHUTDOWN_METRICS_FILE = "/data/system/shutdown-metrics.txt";
    private static final String SHUTDOWN_TRON_METRICS_PREFIX = "shutdown_";
    private static final String TAG = "BootReceiver";
    private static final String TAG_TOMBSTONE = "SYSTEM_TOMBSTONE";
    private static final File TOMBSTONE_DIR = new File("/data/tombstones");
    private static final int UMOUNT_STATUS_NOT_AVAILABLE = 4;
    private static final File lastHeaderFile = new File(Environment.getDataSystemDirectory(), LAST_HEADER_FILE);
    private static final AtomicFile sFile = new AtomicFile(new File(Environment.getDataSystemDirectory(), LOG_FILES_FILE), "log-files");
    private static FileObserver sTombstoneObserver = null;

    public void onReceive(final Context context, Intent intent) {
        new Thread() {
            public void run() {
                String str = BootReceiver.TAG;
                try {
                    BootReceiver.this.logBootEvents(context);
                } catch (Exception e) {
                    Slog.e(str, "Can't log boot events", e);
                }
                Exception e2 = null;
                try {
                    e2 = Stub.asInterface(ServiceManager.getService("package")).isOnlyCoreApps();
                } catch (RemoteException e3) {
                }
                if (e2 == null) {
                    try {
                        BootReceiver.this.removeOldUpdatePackages(context);
                    } catch (Exception e22) {
                        Slog.e(str, "Can't remove old update packages", e22);
                    }
                }
            }
        }.start();
    }

    private void removeOldUpdatePackages(Context context) {
        Downloads.removeAllDownloadsByPackage(context, OLD_UPDATER_PACKAGE, OLD_UPDATER_CLASS);
    }

    private String getPreviousBootHeaders() {
        String str = null;
        try {
            str = FileUtils.readTextFile(lastHeaderFile, 0, null);
            return str;
        } catch (IOException e) {
            return str;
        }
    }

    private String getCurrentBootHeaders() throws IOException {
        StringBuilder stringBuilder = new StringBuilder(512);
        stringBuilder.append("Build: ");
        stringBuilder.append(Build.FINGERPRINT);
        String str = "\n";
        stringBuilder.append(str);
        stringBuilder.append("Hardware: ");
        stringBuilder.append(Build.BOARD);
        stringBuilder.append(str);
        stringBuilder.append("Revision: ");
        stringBuilder.append(SystemProperties.get("ro.revision", ""));
        stringBuilder.append(str);
        stringBuilder.append("Bootloader: ");
        stringBuilder.append(Build.BOOTLOADER);
        stringBuilder.append(str);
        stringBuilder.append("Radio: ");
        stringBuilder.append(Build.getRadioVersion());
        stringBuilder.append(str);
        stringBuilder.append("Kernel: ");
        stringBuilder.append(FileUtils.readTextFile(new File("/proc/version"), 1024, "...\n"));
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    private String getBootHeadersToLogAndUpdate() throws IOException {
        String oldHeaders = getPreviousBootHeaders();
        String newHeaders = getCurrentBootHeaders();
        try {
            FileUtils.stringToFile(lastHeaderFile, newHeaders);
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error writing ");
            stringBuilder.append(lastHeaderFile);
            Slog.e(TAG, stringBuilder.toString(), e);
        }
        StringBuilder stringBuilder2;
        if (oldHeaders == null) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("isPrevious: false\n");
            stringBuilder2.append(newHeaders);
            return stringBuilder2.toString();
        }
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("isPrevious: true\n");
        stringBuilder2.append(oldHeaders);
        return stringBuilder2.toString();
    }

    private void logBootEvents(Context ctx) throws IOException {
        String str;
        String lastKmsgFooter;
        DropBoxManager db = (DropBoxManager) ctx.getSystemService(Context.DROPBOX_SERVICE);
        String headers = getBootHeadersToLogAndUpdate();
        String bootReason = SystemProperties.get("ro.boot.bootreason", null);
        String recovery = RecoverySystem.handleAftermath(ctx);
        if (!(recovery == null || db == null)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(headers);
            stringBuilder.append(recovery);
            db.addText("SYSTEM_RECOVERY_LOG", stringBuilder.toString());
        }
        String lastKmsgFooter2 = "";
        if (bootReason != null) {
            StringBuilder stringBuilder2 = new StringBuilder(512);
            str = "\n";
            stringBuilder2.append(str);
            stringBuilder2.append("Boot info:\n");
            stringBuilder2.append("Last boot reason: ");
            stringBuilder2.append(bootReason);
            stringBuilder2.append(str);
            lastKmsgFooter = stringBuilder2.toString();
        } else {
            lastKmsgFooter = lastKmsgFooter2;
        }
        HashMap<String, Long> timestamps = readTimestamps();
        str = "ro.runtime.firstboot";
        if (SystemProperties.getLong(str, 0) == 0) {
            if (!StorageManager.inCryptKeeperBounce()) {
                SystemProperties.set(str, Long.toString(System.currentTimeMillis()));
            }
            if (db != null) {
                db.addText("SYSTEM_BOOT", headers);
            }
            DropBoxManager dropBoxManager = db;
            HashMap<String, Long> hashMap = timestamps;
            str = headers;
            String str2 = lastKmsgFooter;
            addFileWithFootersToDropBox(dropBoxManager, hashMap, str, str2, "/proc/last_kmsg", -LOG_SIZE, "SYSTEM_LAST_KMSG");
            dropBoxManager = db;
            addFileWithFootersToDropBox(dropBoxManager, hashMap, str, str2, "/sys/fs/pstore/console-ramoops", -LOG_SIZE, "SYSTEM_LAST_KMSG");
            dropBoxManager = db;
            addFileWithFootersToDropBox(dropBoxManager, hashMap, str, str2, "/sys/fs/pstore/console-ramoops-0", -LOG_SIZE, "SYSTEM_LAST_KMSG");
            dropBoxManager = db;
            addFileToDropBox(dropBoxManager, hashMap, str, "/cache/recovery/log", -LOG_SIZE, "SYSTEM_RECOVERY_LOG");
            dropBoxManager = db;
            addFileToDropBox(dropBoxManager, hashMap, str, "/cache/recovery/last_kmsg", -LOG_SIZE, "SYSTEM_RECOVERY_KMSG");
            addAuditErrorsToDropBox(db, timestamps, headers, -LOG_SIZE, "SYSTEM_AUDIT");
        } else if (db != null) {
            db.addText("SYSTEM_RESTART", headers);
        }
        logFsShutdownTime();
        logFsMountTime();
        addFsckErrorsToDropBoxAndLogFsStat(db, timestamps, headers, -LOG_SIZE, "SYSTEM_FSCK");
        logSystemServerShutdownTimeMetrics();
        File[] tombstoneFiles = TOMBSTONE_DIR.listFiles();
        int i = 0;
        while (tombstoneFiles != null && i < tombstoneFiles.length) {
            if (tombstoneFiles[i].isFile()) {
                addFileToDropBox(db, timestamps, headers, tombstoneFiles[i].getPath(), LOG_SIZE, TAG_TOMBSTONE);
            }
            i++;
        }
        writeTimestamps(timestamps);
        final DropBoxManager dropBoxManager2 = db;
        final String str3 = headers;
        sTombstoneObserver = new FileObserver(TOMBSTONE_DIR.getPath(), 256) {
            public void onEvent(int event, String path) {
                HashMap<String, Long> timestamps = BootReceiver.readTimestamps();
                try {
                    File file = new File(BootReceiver.TOMBSTONE_DIR, path);
                    if (file.isFile() && file.getName().startsWith("tombstone_")) {
                        BootReceiver.addFileToDropBox(dropBoxManager2, timestamps, str3, file.getPath(), BootReceiver.LOG_SIZE, BootReceiver.TAG_TOMBSTONE);
                    }
                } catch (IOException e) {
                    Slog.e(BootReceiver.TAG, "Can't log tombstone", e);
                }
                BootReceiver.this.writeTimestamps(timestamps);
            }
        };
        sTombstoneObserver.startWatching();
    }

    private static void addFileToDropBox(DropBoxManager db, HashMap<String, Long> timestamps, String headers, String filename, int maxSize, String tag) throws IOException {
        addFileWithFootersToDropBox(db, timestamps, headers, "", filename, maxSize, tag);
    }

    private static void addFileWithFootersToDropBox(DropBoxManager db, HashMap<String, Long> timestamps, String headers, String footers, String filename, int maxSize, String tag) throws IOException {
        if (db != null && db.isTagEnabled(tag)) {
            File file = new File(filename);
            long fileTime = file.lastModified();
            if (fileTime > 0) {
                if (!timestamps.containsKey(filename) || ((Long) timestamps.get(filename)).longValue() != fileTime) {
                    timestamps.put(filename, Long.valueOf(fileTime));
                    String fileContents = FileUtils.readTextFile(file, maxSize, "[[TRUNCATED]]\n");
                    String text = new StringBuilder();
                    text.append(headers);
                    text.append(fileContents);
                    text.append(footers);
                    text = text.toString();
                    String str = TAG_TOMBSTONE;
                    if (tag.equals(str) && fileContents.contains(">>> system_server <<<")) {
                        addTextToDropBox(db, "system_server_native_crash", text, filename, maxSize);
                    }
                    if (tag.equals(str)) {
                        StatsLogInternal.write(186);
                    }
                    addTextToDropBox(db, tag, text, filename, maxSize);
                }
            }
        }
    }

    private static void addTextToDropBox(DropBoxManager db, String tag, String text, String filename, int maxSize) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Copying ");
        stringBuilder.append(filename);
        stringBuilder.append(" to DropBox (");
        stringBuilder.append(tag);
        stringBuilder.append(")");
        Slog.i(TAG, stringBuilder.toString());
        db.addText(tag, text);
        EventLog.writeEvent((int) DropboxLogTags.DROPBOX_FILE_COPY, filename, Integer.valueOf(maxSize), tag);
    }

    private static void addAuditErrorsToDropBox(DropBoxManager db, HashMap<String, Long> timestamps, String headers, int maxSize, String tag) throws IOException {
        DropBoxManager dropBoxManager = db;
        HashMap<String, Long> hashMap = timestamps;
        String str = tag;
        String str2;
        int i;
        if (dropBoxManager == null) {
            str2 = headers;
            i = maxSize;
        } else if (dropBoxManager.isTagEnabled(str)) {
            String str3 = TAG;
            Slog.i(str3, "Copying audit failures to DropBox");
            File file = new File("/proc/last_kmsg");
            long fileTime = file.lastModified();
            if (fileTime <= 0) {
                file = new File("/sys/fs/pstore/console-ramoops");
                fileTime = file.lastModified();
                if (fileTime <= 0) {
                    file = new File("/sys/fs/pstore/console-ramoops-0");
                    fileTime = file.lastModified();
                }
            }
            if (fileTime > 0) {
                if (!hashMap.containsKey(str) || ((Long) hashMap.get(str)).longValue() != fileTime) {
                    hashMap.put(str, Long.valueOf(fileTime));
                    String log = FileUtils.readTextFile(file, maxSize, "[[TRUNCATED]]\n");
                    StringBuilder sb = new StringBuilder();
                    str2 = "\n";
                    for (String line : log.split(str2)) {
                        if (line.contains("audit")) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(line);
                            stringBuilder.append(str2);
                            sb.append(stringBuilder.toString());
                        }
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Copied ");
                    stringBuilder2.append(sb.toString().length());
                    stringBuilder2.append(" worth of audits to DropBox");
                    Slog.i(str3, stringBuilder2.toString());
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(headers);
                    stringBuilder3.append(sb.toString());
                    dropBoxManager.addText(str, stringBuilder3.toString());
                }
            }
        } else {
            str2 = headers;
            i = maxSize;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x002e A:{RETURN} */
    private static void addFsckErrorsToDropBoxAndLogFsStat(android.os.DropBoxManager r21, java.util.HashMap<java.lang.String, java.lang.Long> r22, java.lang.String r23, int r24, java.lang.String r25) throws java.io.IOException {
        /*
        r6 = r21;
        r0 = 1;
        if (r6 == 0) goto L_0x0010;
    L_0x0005:
        r7 = r25;
        r1 = r6.isTagEnabled(r7);
        if (r1 != 0) goto L_0x000e;
    L_0x000d:
        goto L_0x0012;
    L_0x000e:
        r8 = r0;
        goto L_0x0014;
    L_0x0010:
        r7 = r25;
    L_0x0012:
        r0 = 0;
        r8 = r0;
    L_0x0014:
        r0 = 0;
        r1 = "BootReceiver";
        r2 = "Checking for fsck errors";
        android.util.Slog.i(r1, r2);
        r2 = new java.io.File;
        r3 = "/dev/fscklogs/log";
        r2.<init>(r3);
        r9 = r2;
        r10 = r9.lastModified();
        r2 = 0;
        r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1));
        if (r2 > 0) goto L_0x002f;
    L_0x002e:
        return;
    L_0x002f:
        r2 = "[[TRUNCATED]]\n";
        r12 = r24;
        r13 = android.os.FileUtils.readTextFile(r9, r12, r2);
        r2 = "fs_stat,[^,]*/([^/,]+),(0x[0-9a-fA-F]+)";
        r14 = java.util.regex.Pattern.compile(r2);
        r2 = "\n";
        r15 = r13.split(r2);
        r2 = 0;
        r3 = 0;
        r4 = r15.length;
        r5 = 0;
        r16 = r0;
        r20 = r3;
        r3 = r2;
        r2 = r20;
    L_0x004e:
        if (r5 >= r4) goto L_0x009c;
    L_0x0050:
        r0 = r15[r5];
        r17 = r4;
        r4 = "FILE SYSTEM WAS MODIFIED";
        r4 = r0.contains(r4);
        if (r4 == 0) goto L_0x0060;
    L_0x005c:
        r4 = 1;
        r16 = r4;
        goto L_0x0094;
    L_0x0060:
        r4 = "fs_stat";
        r4 = r0.contains(r4);
        if (r4 == 0) goto L_0x0090;
    L_0x0068:
        r4 = r14.matcher(r0);
        r18 = r4.find();
        if (r18 == 0) goto L_0x0077;
    L_0x0072:
        handleFsckFsStat(r4, r15, r2, r3);
        r2 = r3;
        goto L_0x0094;
    L_0x0077:
        r18 = r2;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r19 = r4;
        r4 = "cannot parse fs_stat:";
        r2.append(r4);
        r2.append(r0);
        r2 = r2.toString();
        android.util.Slog.w(r1, r2);
        goto L_0x0092;
    L_0x0090:
        r18 = r2;
    L_0x0092:
        r2 = r18;
        r3 = r3 + 1;
        r5 = r5 + 1;
        r4 = r17;
        goto L_0x004e;
    L_0x009c:
        r18 = r2;
        if (r8 == 0) goto L_0x00b7;
    L_0x00a0:
        if (r16 == 0) goto L_0x00b7;
    L_0x00a2:
        r4 = "/dev/fscklogs/log";
        r0 = r21;
        r1 = r22;
        r17 = r18;
        r2 = r23;
        r18 = r3;
        r3 = r4;
        r4 = r24;
        r5 = r25;
        addFileToDropBox(r0, r1, r2, r3, r4, r5);
        goto L_0x00bb;
    L_0x00b7:
        r17 = r18;
        r18 = r3;
    L_0x00bb:
        r9.delete();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.BootReceiver.addFsckErrorsToDropBoxAndLogFsStat(android.os.DropBoxManager, java.util.HashMap, java.lang.String, int, java.lang.String):void");
    }

    private static void logFsMountTime() {
        for (String propPostfix : MOUNT_DURATION_PROPS_POSTFIX) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ro.boottime.init.mount_all.");
            stringBuilder.append(propPostfix);
            int duration = SystemProperties.getInt(stringBuilder.toString(), 0);
            if (duration != 0) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("boot_mount_all_duration_");
                stringBuilder2.append(propPostfix);
                MetricsLogger.histogram(null, stringBuilder2.toString(), duration);
            }
        }
    }

    private static void logSystemServerShutdownTimeMetrics() {
        File metricsFile = new File(SHUTDOWN_METRICS_FILE);
        String metricsStr = null;
        boolean exists = metricsFile.exists();
        String str = TAG;
        if (exists) {
            try {
                metricsStr = FileUtils.readTextFile(metricsFile, 0, null);
            } catch (IOException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Problem reading ");
                stringBuilder.append(metricsFile);
                Slog.e(str, stringBuilder.toString(), e);
            }
        }
        if (!TextUtils.isEmpty(metricsStr)) {
            String duration = null;
            String start_time = null;
            String reason = null;
            String reboot = null;
            for (String keyValueStr : metricsStr.split(",")) {
                String[] keyValue = keyValueStr.split(":");
                if (keyValue.length != 2) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Wrong format of shutdown metrics - ");
                    stringBuilder2.append(metricsStr);
                    Slog.e(str, stringBuilder2.toString());
                } else {
                    if (keyValue[0].startsWith(SHUTDOWN_TRON_METRICS_PREFIX)) {
                        logTronShutdownMetric(keyValue[0], keyValue[1]);
                        if (keyValue[0].equals(METRIC_SYSTEM_SERVER)) {
                            duration = keyValue[1];
                        }
                    }
                    if (keyValue[0].equals("reboot")) {
                        reboot = keyValue[1];
                    } else if (keyValue[0].equals("reason")) {
                        reason = keyValue[1];
                    } else if (keyValue[0].equals(METRIC_SHUTDOWN_TIME_START)) {
                        start_time = keyValue[1];
                    }
                }
            }
            logStatsdShutdownAtom(reboot, reason, start_time, duration);
        }
        metricsFile.delete();
    }

    private static void logTronShutdownMetric(String metricName, String valueStr) {
        try {
            int value = Integer.parseInt(valueStr);
            if (value >= 0) {
                MetricsLogger.histogram(null, metricName, value);
            }
        } catch (NumberFormatException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot parse metric ");
            stringBuilder.append(metricName);
            stringBuilder.append(" int value - ");
            stringBuilder.append(valueStr);
            Slog.e(TAG, stringBuilder.toString());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0041  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0069  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x004b A:{SYNTHETIC, Splitter:B:14:0x004b} */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0070 A:{SYNTHETIC, Splitter:B:21:0x0070} */
    private static void logStatsdShutdownAtom(java.lang.String r17, java.lang.String r18, java.lang.String r19, java.lang.String r20) {
        /*
        r1 = r17;
        r2 = r19;
        r0 = 0;
        r3 = "<EMPTY>";
        r4 = 0;
        r6 = 0;
        r8 = "BootReceiver";
        if (r1 == 0) goto L_0x0039;
    L_0x000f:
        r9 = "y";
        r9 = r1.equals(r9);
        if (r9 == 0) goto L_0x001b;
    L_0x0018:
        r0 = 1;
        r9 = r0;
        goto L_0x003f;
    L_0x001b:
        r9 = "n";
        r9 = r1.equals(r9);
        if (r9 != 0) goto L_0x003e;
    L_0x0024:
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Unexpected value for reboot : ";
        r9.append(r10);
        r9.append(r1);
        r9 = r9.toString();
        android.util.Slog.e(r8, r9);
        goto L_0x003e;
    L_0x0039:
        r9 = "No value received for reboot";
        android.util.Slog.e(r8, r9);
    L_0x003e:
        r9 = r0;
    L_0x003f:
        if (r18 == 0) goto L_0x0044;
    L_0x0041:
        r3 = r18;
        goto L_0x0049;
    L_0x0044:
        r0 = "No value received for shutdown reason";
        android.util.Slog.e(r8, r0);
    L_0x0049:
        if (r2 == 0) goto L_0x0069;
    L_0x004b:
        r10 = java.lang.Long.parseLong(r19);	 Catch:{ NumberFormatException -> 0x0051 }
        r4 = r10;
    L_0x0050:
        goto L_0x006e;
    L_0x0051:
        r0 = move-exception;
        r10 = r0;
        r0 = r10;
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = "Cannot parse shutdown start time: ";
        r10.append(r11);
        r10.append(r2);
        r10 = r10.toString();
        android.util.Slog.e(r8, r10);
        goto L_0x0050;
    L_0x0069:
        r0 = "No value received for shutdown start time";
        android.util.Slog.e(r8, r0);
    L_0x006e:
        if (r20 == 0) goto L_0x008e;
    L_0x0070:
        r10 = java.lang.Long.parseLong(r20);	 Catch:{ NumberFormatException -> 0x0076 }
        r6 = r10;
    L_0x0075:
        goto L_0x0093;
    L_0x0076:
        r0 = move-exception;
        r10 = r0;
        r0 = r10;
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = "Cannot parse shutdown duration: ";
        r10.append(r11);
        r10.append(r2);
        r10 = r10.toString();
        android.util.Slog.e(r8, r10);
        goto L_0x0075;
    L_0x008e:
        r0 = "No value received for shutdown duration";
        android.util.Slog.e(r8, r0);
    L_0x0093:
        r10 = 56;
        r11 = r9;
        r12 = r3;
        r13 = r4;
        r15 = r6;
        android.util.StatsLogInternal.write(r10, r11, r12, r13, r15);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.BootReceiver.logStatsdShutdownAtom(java.lang.String, java.lang.String, java.lang.String, java.lang.String):void");
    }

    private static void logFsShutdownTime() {
        String str = TAG;
        File f = null;
        for (String fileName : LAST_KMSG_FILES) {
            File file = new File(fileName);
            if (file.exists()) {
                f = file;
                break;
            }
        }
        if (f != null) {
            try {
                Matcher matcher = Pattern.compile(LAST_SHUTDOWN_TIME_PATTERN, 8).matcher(FileUtils.readTextFile(f, -16384, null));
                String str2 = "boot_fs_shutdown_umount_stat";
                if (matcher.find()) {
                    MetricsLogger.histogram(null, "boot_fs_shutdown_duration", Integer.parseInt(matcher.group(1)));
                    MetricsLogger.histogram(null, str2, Integer.parseInt(matcher.group(2)));
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("boot_fs_shutdown,");
                    stringBuilder.append(matcher.group(1));
                    stringBuilder.append(",");
                    stringBuilder.append(matcher.group(2));
                    Slog.i(str, stringBuilder.toString());
                } else {
                    MetricsLogger.histogram(null, str2, 4);
                    Slog.w(str, "boot_fs_shutdown, string not found");
                }
            } catch (IOException e) {
                Slog.w(str, "cannot read last msg", e);
            }
        }
    }

    @VisibleForTesting
    public static int fixFsckFsStat(String partition, int statOrg, String[] lines, int startLineNumber, int endLineNumber) {
        String str = partition;
        int stat = statOrg;
        if ((stat & 1024) != 0) {
            String str2;
            String str3;
            boolean foundQuotaFix;
            Pattern passPattern = Pattern.compile(FSCK_PASS_PATTERN);
            Pattern treeOptPattern = Pattern.compile(FSCK_TREE_OPTIMIZATION_PATTERN);
            String currentPass = "";
            boolean foundTreeOptimization = false;
            boolean foundQuotaFix2 = false;
            boolean foundTimestampAdjustment = false;
            boolean foundOtherFix = false;
            String otherFixLine = null;
            int i = startLineNumber;
            while (true) {
                str2 = "fs_stat, partition:";
                str3 = TAG;
                if (i >= endLineNumber) {
                    foundQuotaFix = foundQuotaFix2;
                    break;
                }
                String line = lines[i];
                Pattern pattern;
                if (line.contains(FSCK_FS_MODIFIED)) {
                    pattern = passPattern;
                    foundQuotaFix = foundQuotaFix2;
                    break;
                }
                foundQuotaFix = foundQuotaFix2;
                if (line.startsWith("Pass ")) {
                    Matcher matcher = passPattern.matcher(line);
                    if (matcher.find()) {
                        currentPass = matcher.group(1);
                    }
                    pattern = passPattern;
                    foundQuotaFix2 = foundQuotaFix;
                } else {
                    String str4 = "1";
                    if (!line.startsWith("Inode ")) {
                        pattern = passPattern;
                        passPattern = "5";
                        if (line.startsWith("[QUOTA WARNING]") && currentPass.equals(passPattern)) {
                            passPattern = new StringBuilder();
                            passPattern.append(str2);
                            passPattern.append(str);
                            passPattern.append(" found quota warning:");
                            passPattern.append(line);
                            Slog.i(str3, passPattern.toString());
                            foundQuotaFix2 = true;
                            if (!foundTreeOptimization) {
                                otherFixLine = line;
                                foundQuotaFix = true;
                                break;
                            }
                        } else {
                            if (!line.startsWith("Update quota info") || currentPass.equals(passPattern) == null) {
                                if (line.startsWith("Timestamp(s) on inode") == null || line.contains("beyond 2310-04-04 are likely pre-1970") == null || currentPass.equals(str4) == null) {
                                    passPattern = line.trim();
                                    if (!(passPattern.isEmpty() || currentPass.isEmpty())) {
                                        foundOtherFix = true;
                                        otherFixLine = passPattern;
                                        break;
                                    }
                                }
                                passPattern = new StringBuilder();
                                passPattern.append(str2);
                                passPattern.append(str);
                                passPattern.append(" found timestamp adjustment:");
                                passPattern.append(line);
                                Slog.i(str3, passPattern.toString());
                                if (lines[i + 1].contains("Fix? yes") != null) {
                                    i++;
                                }
                                foundTimestampAdjustment = true;
                                foundQuotaFix2 = foundQuotaFix;
                            }
                            foundQuotaFix2 = foundQuotaFix;
                        }
                    } else if (treeOptPattern.matcher(line).find() && currentPass.equals(str4)) {
                        foundTreeOptimization = true;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(str2);
                        stringBuilder.append(str);
                        stringBuilder.append(" found tree optimization:");
                        stringBuilder.append(line);
                        Slog.i(str3, stringBuilder.toString());
                        pattern = passPattern;
                        foundQuotaFix2 = foundQuotaFix;
                    } else {
                        foundOtherFix = true;
                        otherFixLine = line;
                        pattern = passPattern;
                    }
                }
                i++;
                passPattern = pattern;
            }
            StringBuilder stringBuilder2;
            if (foundOtherFix) {
                if (otherFixLine == null) {
                    return stat;
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str2);
                stringBuilder2.append(str);
                stringBuilder2.append(" fix:");
                stringBuilder2.append(otherFixLine);
                Slog.i(str3, stringBuilder2.toString());
                return stat;
            } else if (foundQuotaFix && !foundTreeOptimization) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("fs_stat, got quota fix without tree optimization, partition:");
                stringBuilder2.append(str);
                Slog.i(str3, stringBuilder2.toString());
                return stat;
            } else if ((!foundTreeOptimization || !foundQuotaFix) && !foundTimestampAdjustment) {
                return stat;
            } else {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str2);
                stringBuilder2.append(str);
                stringBuilder2.append(" fix ignored");
                Slog.i(str3, stringBuilder2.toString());
                return stat & -1025;
            }
        }
        int i2 = endLineNumber;
        return stat;
    }

    private static void handleFsckFsStat(Matcher match, String[] lines, int startLineNumber, int endLineNumber) {
        String str = TAG;
        String partition = match.group(1);
        int stat = 2;
        StringBuilder stringBuilder;
        try {
            stat = Integer.decode(match.group(2)).intValue();
            stat = fixFsckFsStat(partition, stat, lines, startLineNumber, endLineNumber);
            stringBuilder = new StringBuilder();
            stringBuilder.append("boot_fs_stat_");
            stringBuilder.append(partition);
            MetricsLogger.histogram(null, stringBuilder.toString(), stat);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("fs_stat, partition:");
            stringBuilder2.append(partition);
            stringBuilder2.append(" stat:0x");
            stringBuilder2.append(Integer.toHexString(stat));
            Slog.i(str, stringBuilder2.toString());
        } catch (NumberFormatException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("cannot parse fs_stat: partition:");
            stringBuilder.append(partition);
            stringBuilder.append(" stat:");
            stringBuilder.append(match.group(stat));
            Slog.w(str, stringBuilder.toString());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x009a A:{SYNTHETIC, Splitter:B:37:0x009a} */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x002a A:{Catch:{ all -> 0x00a5 }} */
    /* JADX WARNING: Missing block: B:34:0x0093, code skipped:
            if (1 == null) goto L_0x0095;
     */
    /* JADX WARNING: Missing block: B:44:0x00a6, code skipped:
            if (r3 != null) goto L_0x00a8;
     */
    /* JADX WARNING: Missing block: B:46:?, code skipped:
            r3.close();
     */
    /* JADX WARNING: Missing block: B:71:0x0144, code skipped:
            if (r2 != false) goto L_0x0148;
     */
    private static java.util.HashMap<java.lang.String, java.lang.Long> readTimestamps() {
        /*
        r0 = sFile;
        monitor-enter(r0);
        r1 = new java.util.HashMap;	 Catch:{ all -> 0x0150 }
        r1.<init>();	 Catch:{ all -> 0x0150 }
        r2 = 0;
        r3 = sFile;	 Catch:{ FileNotFoundException -> 0x0121, IOException -> 0x0105, IllegalStateException -> 0x00ea, NullPointerException -> 0x00cf, XmlPullParserException -> 0x00b4 }
        r3 = r3.openRead();	 Catch:{ FileNotFoundException -> 0x0121, IOException -> 0x0105, IllegalStateException -> 0x00ea, NullPointerException -> 0x00cf, XmlPullParserException -> 0x00b4 }
        r4 = android.util.Xml.newPullParser();	 Catch:{ all -> 0x00a3 }
        r5 = java.nio.charset.StandardCharsets.UTF_8;	 Catch:{ all -> 0x00a3 }
        r5 = r5.name();	 Catch:{ all -> 0x00a3 }
        r4.setInput(r3, r5);	 Catch:{ all -> 0x00a3 }
    L_0x001c:
        r5 = r4.next();	 Catch:{ all -> 0x00a3 }
        r6 = r5;
        r7 = 1;
        r8 = 2;
        if (r5 == r8) goto L_0x0028;
    L_0x0025:
        if (r6 == r7) goto L_0x0028;
    L_0x0027:
        goto L_0x001c;
    L_0x0028:
        if (r6 != r8) goto L_0x009a;
    L_0x002a:
        r5 = r4.getDepth();	 Catch:{ all -> 0x00a3 }
    L_0x002e:
        r8 = r4.next();	 Catch:{ all -> 0x00a3 }
        r6 = r8;
        if (r8 == r7) goto L_0x008d;
    L_0x0035:
        r8 = 3;
        if (r6 != r8) goto L_0x003e;
    L_0x0038:
        r9 = r4.getDepth();	 Catch:{ all -> 0x00a3 }
        if (r9 <= r5) goto L_0x008d;
    L_0x003e:
        if (r6 == r8) goto L_0x002e;
    L_0x0040:
        r8 = 4;
        if (r6 != r8) goto L_0x0044;
    L_0x0043:
        goto L_0x002e;
    L_0x0044:
        r8 = r4.getName();	 Catch:{ all -> 0x00a3 }
        r9 = "log";
        r9 = r8.equals(r9);	 Catch:{ all -> 0x00a3 }
        if (r9 == 0) goto L_0x006f;
    L_0x0050:
        r9 = "filename";
        r10 = 0;
        r9 = r4.getAttributeValue(r10, r9);	 Catch:{ all -> 0x00a3 }
        r11 = "timestamp";
        r10 = r4.getAttributeValue(r10, r11);	 Catch:{ all -> 0x00a3 }
        r10 = java.lang.Long.valueOf(r10);	 Catch:{ all -> 0x00a3 }
        r10 = r10.longValue();	 Catch:{ all -> 0x00a3 }
        r12 = java.lang.Long.valueOf(r10);	 Catch:{ all -> 0x00a3 }
        r1.put(r9, r12);	 Catch:{ all -> 0x00a3 }
        goto L_0x008c;
    L_0x006f:
        r9 = "BootReceiver";
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a3 }
        r10.<init>();	 Catch:{ all -> 0x00a3 }
        r11 = "Unknown tag: ";
        r10.append(r11);	 Catch:{ all -> 0x00a3 }
        r11 = r4.getName();	 Catch:{ all -> 0x00a3 }
        r10.append(r11);	 Catch:{ all -> 0x00a3 }
        r10 = r10.toString();	 Catch:{ all -> 0x00a3 }
        android.util.Slog.w(r9, r10);	 Catch:{ all -> 0x00a3 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r4);	 Catch:{ all -> 0x00a3 }
    L_0x008c:
        goto L_0x002e;
    L_0x008d:
        r2 = 1;
        if (r3 == 0) goto L_0x0093;
    L_0x0090:
        r3.close();	 Catch:{ FileNotFoundException -> 0x0121, IOException -> 0x0105, IllegalStateException -> 0x00ea, NullPointerException -> 0x00cf, XmlPullParserException -> 0x00b4 }
    L_0x0093:
        if (r2 != 0) goto L_0x0148;
    L_0x0095:
        r1.clear();	 Catch:{ all -> 0x0150 }
        goto L_0x0148;
    L_0x009a:
        r5 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x00a3 }
        r7 = "no start tag found";
        r5.<init>(r7);	 Catch:{ all -> 0x00a3 }
        throw r5;	 Catch:{ all -> 0x00a3 }
    L_0x00a3:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x00a5 }
    L_0x00a5:
        r5 = move-exception;
        if (r3 == 0) goto L_0x00b0;
    L_0x00a8:
        r3.close();	 Catch:{ all -> 0x00ac }
        goto L_0x00b0;
    L_0x00ac:
        r6 = move-exception;
        r4.addSuppressed(r6);	 Catch:{ FileNotFoundException -> 0x0121, IOException -> 0x0105, IllegalStateException -> 0x00ea, NullPointerException -> 0x00cf, XmlPullParserException -> 0x00b4 }
    L_0x00b0:
        throw r5;	 Catch:{ FileNotFoundException -> 0x0121, IOException -> 0x0105, IllegalStateException -> 0x00ea, NullPointerException -> 0x00cf, XmlPullParserException -> 0x00b4 }
    L_0x00b1:
        r3 = move-exception;
        goto L_0x014a;
    L_0x00b4:
        r3 = move-exception;
        r4 = "BootReceiver";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b1 }
        r5.<init>();	 Catch:{ all -> 0x00b1 }
        r6 = "Failed parsing ";
        r5.append(r6);	 Catch:{ all -> 0x00b1 }
        r5.append(r3);	 Catch:{ all -> 0x00b1 }
        r5 = r5.toString();	 Catch:{ all -> 0x00b1 }
        android.util.Slog.w(r4, r5);	 Catch:{ all -> 0x00b1 }
        if (r2 != 0) goto L_0x0148;
    L_0x00ce:
        goto L_0x0095;
    L_0x00cf:
        r3 = move-exception;
        r4 = "BootReceiver";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b1 }
        r5.<init>();	 Catch:{ all -> 0x00b1 }
        r6 = "Failed parsing ";
        r5.append(r6);	 Catch:{ all -> 0x00b1 }
        r5.append(r3);	 Catch:{ all -> 0x00b1 }
        r5 = r5.toString();	 Catch:{ all -> 0x00b1 }
        android.util.Slog.w(r4, r5);	 Catch:{ all -> 0x00b1 }
        if (r2 != 0) goto L_0x0148;
    L_0x00e9:
        goto L_0x0095;
    L_0x00ea:
        r3 = move-exception;
        r4 = "BootReceiver";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b1 }
        r5.<init>();	 Catch:{ all -> 0x00b1 }
        r6 = "Failed parsing ";
        r5.append(r6);	 Catch:{ all -> 0x00b1 }
        r5.append(r3);	 Catch:{ all -> 0x00b1 }
        r5 = r5.toString();	 Catch:{ all -> 0x00b1 }
        android.util.Slog.w(r4, r5);	 Catch:{ all -> 0x00b1 }
        if (r2 != 0) goto L_0x0148;
    L_0x0104:
        goto L_0x0095;
    L_0x0105:
        r3 = move-exception;
        r4 = "BootReceiver";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b1 }
        r5.<init>();	 Catch:{ all -> 0x00b1 }
        r6 = "Failed parsing ";
        r5.append(r6);	 Catch:{ all -> 0x00b1 }
        r5.append(r3);	 Catch:{ all -> 0x00b1 }
        r5 = r5.toString();	 Catch:{ all -> 0x00b1 }
        android.util.Slog.w(r4, r5);	 Catch:{ all -> 0x00b1 }
        if (r2 != 0) goto L_0x0148;
    L_0x011f:
        goto L_0x0095;
    L_0x0121:
        r3 = move-exception;
        r4 = "BootReceiver";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b1 }
        r5.<init>();	 Catch:{ all -> 0x00b1 }
        r6 = "No existing last log timestamp file ";
        r5.append(r6);	 Catch:{ all -> 0x00b1 }
        r6 = sFile;	 Catch:{ all -> 0x00b1 }
        r6 = r6.getBaseFile();	 Catch:{ all -> 0x00b1 }
        r5.append(r6);	 Catch:{ all -> 0x00b1 }
        r6 = "; starting empty";
        r5.append(r6);	 Catch:{ all -> 0x00b1 }
        r5 = r5.toString();	 Catch:{ all -> 0x00b1 }
        android.util.Slog.i(r4, r5);	 Catch:{ all -> 0x00b1 }
        if (r2 != 0) goto L_0x0148;
    L_0x0146:
        goto L_0x0095;
    L_0x0148:
        monitor-exit(r0);	 Catch:{ all -> 0x0150 }
        return r1;
    L_0x014a:
        if (r2 != 0) goto L_0x014f;
    L_0x014c:
        r1.clear();	 Catch:{ all -> 0x0150 }
    L_0x014f:
        throw r3;	 Catch:{ all -> 0x0150 }
    L_0x0150:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0150 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.BootReceiver.readTimestamps():java.util.HashMap");
    }

    private void writeTimestamps(HashMap<String, Long> timestamps) {
        synchronized (sFile) {
            try {
                FileOutputStream stream = sFile.startWrite();
                try {
                    XmlSerializer out = new FastXmlSerializer();
                    out.setOutput(stream, StandardCharsets.UTF_8.name());
                    out.startDocument(null, Boolean.valueOf(true));
                    out.startTag(null, "log-files");
                    for (String filename : timestamps.keySet()) {
                        out.startTag(null, "log");
                        out.attribute(null, "filename", filename);
                        out.attribute(null, "timestamp", ((Long) timestamps.get(filename)).toString());
                        out.endTag(null, "log");
                    }
                    out.endTag(null, "log-files");
                    out.endDocument();
                    sFile.finishWrite(stream);
                } catch (IOException e) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to write timestamp file, using the backup: ");
                    stringBuilder.append(e);
                    Slog.w(str, stringBuilder.toString());
                    sFile.failWrite(stream);
                } catch (Throwable th) {
                }
            } catch (IOException e2) {
                String str2 = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Failed to write timestamp file: ");
                stringBuilder2.append(e2);
                Slog.w(str2, stringBuilder2.toString());
            }
        }
    }
}
