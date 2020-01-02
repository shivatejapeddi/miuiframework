package miui.mqsas.fdmonitor;

import android.app.ApplicationErrorReport.CrashInfo;
import android.os.SystemProperties;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Slog;
import com.android.internal.util.FastPrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import libcore.io.IoUtils;
import miui.mqsas.oom.OOMEventManager;
import miui.mqsas.sdk.MQSEventManagerDelegate;
import miui.mqsas.sdk.event.JavaExceptionEvent;

public class FdInfoManager {
    private static String[] INTERESTED_CLASS = new String[]{OOMEventManager.JAVA_EXCEPTION_OOM_CLASS};
    private static String[] INTERESTED_EXCEPTION_MSG = new String[]{"Could not allocate JNI Env", "Could not allocate dup blob fd", "Could not read input channel file descriptors from parcel", "pthread_create", "InputChannel is not initialized", "Could not open input channel pair"};
    private static String TAG = "FdInfoManager";
    private static boolean dump_system_once = false;

    public static boolean isInterestedFdLeakEvent(JavaExceptionEvent event, Throwable e) {
        String eventExceptionClass;
        String eventExceptionMsg;
        String exceptionClass;
        String str;
        StringBuilder stringBuilder;
        if (event != null) {
            eventExceptionClass = event.getExceptionClassName();
            eventExceptionMsg = event.getExceptionMessage();
        } else if (e != null) {
            eventExceptionClass = new CrashInfo(e);
            String str2 = eventExceptionClass.exceptionClassName;
            eventExceptionMsg = eventExceptionClass.exceptionMessage;
            eventExceptionClass = str2;
        } else {
            Slog.d(TAG, "MIUI_FD Interested Fd leak events, wrong params");
            return false;
        }
        for (String exceptionClass2 : INTERESTED_CLASS) {
            if (exceptionClass2.equals(eventExceptionClass)) {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("MIUI_FD Interested Fd leak events, exceptionClass : ");
                stringBuilder.append(exceptionClass2);
                Slog.d(str, stringBuilder.toString());
                return true;
            }
        }
        String[] strArr = INTERESTED_EXCEPTION_MSG;
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            exceptionClass2 = strArr[i];
            if (eventExceptionMsg == null || !eventExceptionMsg.contains(exceptionClass2)) {
                i++;
            } else {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("MIUI_FD Interested Fd leak events, exceptionMsg : ");
                stringBuilder.append(exceptionClass2);
                Slog.d(str, stringBuilder.toString());
                return true;
            }
        }
        return false;
    }

    public static synchronized void checkEventAndDumpFd(JavaExceptionEvent event, int pid) {
        synchronized (FdInfoManager.class) {
            if (dump_system_once && event.isSystem()) {
                return;
            }
            int shouldDumpFd = MQSEventManagerDelegate.getInstance().checkIfNeedDumpFd(event);
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MIUI_FD in checkEventAndDumpFd shouldDumpFd : ");
            stringBuilder.append(shouldDumpFd);
            Slog.d(str, stringBuilder.toString());
            if (shouldDumpFd == 1) {
                dumpOpenFds(event, pid);
            }
            if (isMtbfTest() && event.isSystem()) {
                dumpProcessMaps(pid);
            }
            dump_system_once = event.isSystem();
        }
    }

    private static boolean isMtbfTest() {
        if (SystemProperties.getInt("ro.debuggable", 0) == 1 && SystemProperties.getInt("ro.miui.mtbftest", 0) == 1) {
            return true;
        }
        return false;
    }

    private static void dumpProcessMaps(int pid) {
        String str = "MIUI collect process maps of: ";
        String str2;
        StringBuilder stringBuilder;
        try {
            str2 = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(pid);
            Slog.i(str2, stringBuilder.toString());
            File fin = new File("/proc/self/maps");
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("/data/anr/system_server_");
            stringBuilder2.append(pid);
            stringBuilder2.append(".maps");
            copyFileInternal(fin, new File(stringBuilder2.toString()));
            String str3 = TAG;
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str);
            stringBuilder3.append(pid);
            stringBuilder3.append(" complete.");
            Slog.i(str3, stringBuilder3.toString());
        } catch (IOException e) {
            str2 = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to collect maps file for: ");
            stringBuilder.append(pid);
            Slog.w(str2, stringBuilder.toString(), e);
        }
    }

    private static void copyFileInternal(File from, File to) throws IOException {
        FileInputStream in = new FileInputStream(from);
        FileOutputStream out = new FileOutputStream(to);
        long size = 0;
        do {
            try {
            } catch (ErrnoException e) {
                e.rethrowAsIOException();
            } catch (Throwable th) {
                IoUtils.closeQuietly(in);
                IoUtils.closeQuietly(out);
            }
        } while (Os.sendfile(out.getFD(), in.getFD(), null, 514288) != 0);
        IoUtils.closeQuietly(in);
        IoUtils.closeQuietly(out);
    }

    private static void dumpOpenFds(JavaExceptionEvent event, int pid) {
        String processName = event.getProcessName();
        File[] fds = new File("/proc/self/fd").listFiles();
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MIUI_FD pid : ");
        stringBuilder.append(pid);
        String str2 = " fds.size: ";
        stringBuilder.append(str2);
        stringBuilder.append(fds != null ? Integer.valueOf(fds.length) : "null");
        Slog.w(str, stringBuilder.toString());
        if (fds == null || fds.length == 0) {
            Slog.w(TAG, "failed to read fds!");
            return;
        }
        PrintWriter pw = null;
        try {
            if (isMtbfTest() && event.isSystem()) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("/data/anr/system_server_");
                stringBuilder2.append(pid);
                stringBuilder2.append(".fds");
                pw = new FastPrintWriter(new FileOutputStream(stringBuilder2.toString()));
                stringBuilder = new StringBuilder();
                stringBuilder.append("MIUI FD pid: ");
                stringBuilder.append(pid);
                stringBuilder.append(str2);
                stringBuilder.append(fds.length);
                pw.println(stringBuilder.toString());
            }
            for (File fd : fds) {
                String fd_path = fd.getAbsolutePath();
                String linkPath = Os.readlink(fd_path);
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("MIUI_FD ");
                stringBuilder3.append(fd_path);
                stringBuilder3.append(" -----> ");
                stringBuilder3.append(linkPath);
                logOut(pw, stringBuilder3.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            IoUtils.closeQuietly(null);
        }
        IoUtils.closeQuietly(pw);
    }

    private static void logOut(PrintWriter pw, String msg) {
        if (pw != null) {
            pw.println(msg);
        }
        Slog.w(TAG, msg);
    }
}
