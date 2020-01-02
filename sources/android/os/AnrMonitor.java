package android.os;

import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.statistics.PerfEvent;
import android.os.statistics.PerfSupervisionSettings;
import android.telecom.Logging.Session;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.util.SparseLongArray;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.android.internal.annotations.GuardedBy;
import com.miui.internal.search.SettingsTree;
import com.miui.whetstone.Event.EventLogTags;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import miui.mqsas.sdk.MQSEventManagerDelegate;
import miui.os.Build;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnrMonitor {
    public static final String ANR_DIRECTORY = "/data/anr/";
    public static final String ANR_INFO_HEAD = "anr_info_";
    public static final int ANR_INFO_LIMIT = 5;
    public static final int ANR_TRACES_LIMIT = 5;
    public static final long BINDER_CALL_MONITOR_TIMEOUT = 1000;
    private static final boolean CHECK_PARCEL_SIZE_ENABLE;
    private static final int CHECK_PARCEL_SIZE_KB = SystemProperties.getInt("persist.binder.check.size", 200);
    private static final Date DATE = new Date();
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss.SSS");
    public static final boolean DBG = SystemProperties.getBoolean("anr.monitor.debug.on", false);
    private static final long DEFAULT_LOCK_WAIT_THRESHOLD = 500;
    public static final int DEFAULT_MESSAGE_HISTORY_DUMP_DURATION = 10000;
    public static final String DUMP_APP_TRACE_TAG = "dump-app-trace";
    public static final long DUMP_MESSAGE_TIMEOUT = 500;
    private static final String DUMP_TRACE_TAG = "DUMP_APP_TRACE";
    public static final int INPUT_DISPATCHING_TIMEOUT = 8000;
    public static final long INPUT_MONITOR_TIMEOUT = 1000;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    public static final long LOCK_WAIT_THRESHOLD = SystemProperties.getLong("persist.vm.lockprof.threshold", 500);
    public static final long LT_MESSAGE_CHECK_TIME = 200;
    public static final int MAX_MESSAGE_SUMMARY_HISTORY = 50;
    public static final long MAX_TIMEOUT = 100000;
    public static final long MESSAGE_EXECUTION_TIMEOUT = 2000;
    public static final long MESSAGE_MONITOR_TIMEOUT = (Build.IS_ALPHA_BUILD ? MESSAGE_EXECUTION_TIMEOUT : 3000);
    private static final long MESSAGE_UPLOAD_CHECK_TIME = 3000;
    private static final boolean MONITOR_MSG_EXECUTION = SystemProperties.getBoolean("monitor.msg.execution", false);
    private static final String MONITOR_TAG = "MIUI-BLOCK-MONITOR";
    private static final boolean NOT_CTS_BUILD = SystemProperties.getBoolean("persist.sys.miui_optimization", true);
    public static final long PERF_EVENT_LOGGING_TIMEOUT = 3000;
    private static final String SCREEN_HAND_PROP = "persist.sys.screen_hang.time";
    private static final int SCREEN_HANG_TIMEOUT = SystemProperties.getInt(SCREEN_HAND_PROP, EventLogTags.BOOT_PROGRESS_START);
    public static final String SEPARATOR = ",";
    private static final String TAG = "AnrMonitor";
    public static final String TRACES_FILE_TYPE = ".txt";
    public static final String TRACES_HEAD = "traces_";
    private static final String TRACE_DIR = "/data/anr/";
    @GuardedBy({"mInputEvnetStartTimeSeqMap"})
    private static final SparseLongArray mInputEvnetStartTimeSeqMap = new SparseLongArray();
    private static final Runnable mScreenHangRunnable = new Runnable() {
        public void run() {
            MQSEventManagerDelegate.getInstance().reportHangException();
            AnrMonitor.getWorkHandler().removeCallbacks(AnrMonitor.mScreenHangRunnable);
        }
    };
    @GuardedBy({"AnrMonitor.class"})
    private static int sBinderRecordIndex = 0;
    @GuardedBy({"AnrMonitor.class"})
    private static int sInputRecordIndex = 0;
    private static final Object sInstanceSync = new Object();
    private static Boolean sIsSystemApp;
    private static final ArrayList<String> sMonitorList = new ArrayList();
    @GuardedBy({"AnrMonitor.class"})
    private static int sMsgRecordIndex = 0;
    private static String sPkgName;
    private static String sProcessName;
    private static boolean sSystemBootCompleted;
    @GuardedBy({"AnrMonitor.class"})
    private static final UploadInfo[] sUploadBinderRecords = new UploadInfo[10];
    @GuardedBy({"AnrMonitor.class"})
    private static final UploadInfo[] sUploadInputRecords = new UploadInfo[10];
    @GuardedBy({"AnrMonitor.class"})
    private static final UploadInfo[] sUploadMsgRecords = new UploadInfo[10];
    private static int sVersionCode;
    private static String sVersionName;
    @GuardedBy({"sInstanceSync"})
    private static volatile WorkHandler sWorkHandler;

    public static class FileInfo implements Comparable<FileInfo> {
        private File mFile;
        private long mModifiedTime;

        public FileInfo(File file, long modifiedTime) {
            this.mFile = file;
            this.mModifiedTime = modifiedTime;
        }

        public File getFile() {
            return this.mFile;
        }

        public long getModifiedTime() {
            return this.mModifiedTime;
        }

        public int compareTo(FileInfo another) {
            return compareTo(another.getModifiedTime(), this.mModifiedTime);
        }

        public int compareTo(long x, long y) {
            if (x < y) {
                return -1;
            }
            return x == y ? 0 : 1;
        }
    }

    public static class TimerThread extends Thread {
        private boolean completed;
        private long timeout;

        public TimerThread(long timeout) {
            this.timeout = timeout;
        }

        public void run() {
            super.run();
            finishRun();
        }

        public synchronized void finishRun() {
            this.completed = true;
            notify();
        }

        public synchronized void startAndWait() {
            try {
                start();
                wait(this.timeout);
                if (!this.completed) {
                    interrupt();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
    }

    public static class UploadInfo {
        private static final String JSON_BASE_INFO_TAG = "baseInfo";
        private static final String JSON_INFO_ARRAY_TAG = "msgs";
        private static final String JSON_INFO_TAG = "msg";
        private static final String JSON_PACKAGE_NAME_TAG = "packageName";
        private static final String JSON_PROCESS_NAME_TAG = "processName";
        private static final String JSON_THREAD_NAME_TAG = "threadName";
        private static final String JSON_TOOKTIME_TAG = "tookTime";
        private static final String JSON_VERSION_CODE_TAG = "versionCode";
        private static final String JSON_VERSION_NAME_TAG = "versionName";
        String info;
        String packageName;
        String processName;
        String threadName;
        long tookTime;
        String versionCode;
        String versionName;

        public String getInfo() {
            return this.info;
        }

        public String getProcessName() {
            return this.processName;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public String getVersionName() {
            return this.versionName;
        }

        public String getVersionCode() {
            return this.versionCode;
        }

        public String getThreadName() {
            return this.threadName;
        }

        public long getTookTime() {
            return this.tookTime;
        }

        public String getKeyWord() {
            try {
                JSONObject object = new JSONObject();
                object.put(JSON_TOOKTIME_TAG, getTookTime());
                return object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }

        private static String createJsonString(UploadInfo[] infos) {
            try {
                JSONObject object = new JSONObject();
                JSONObject baseInfo = new JSONObject();
                baseInfo.put("processName", AnrMonitor.currentProcessName());
                baseInfo.put("packageName", AnrMonitor.currentPackageName());
                baseInfo.put(JSON_VERSION_NAME_TAG, AnrMonitor.currentVersionName());
                baseInfo.put(JSON_VERSION_CODE_TAG, AnrMonitor.currentVersionCode());
                object.put(JSON_BASE_INFO_TAG, baseInfo);
                JSONArray jsonArray = new JSONArray();
                if (infos != null && infos.length > 0) {
                    for (UploadInfo info : infos) {
                        JSONObject obj = new JSONObject();
                        obj.put("msg", info.info);
                        obj.put("threadName", info.threadName);
                        obj.put(JSON_TOOKTIME_TAG, info.tookTime);
                        jsonArray.put(obj);
                    }
                }
                object.put(JSON_INFO_ARRAY_TAG, jsonArray);
                if (AnrMonitor.DBG) {
                    String str = AnrMonitor.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("createJsonString ");
                    stringBuilder.append(object.toString());
                    Slog.d(str, stringBuilder.toString());
                }
                return object.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        public static String getMatchTag(UploadInfo msg) {
            try {
                String matchString = getSaveContent(msg);
                return matchString.substring(0, matchString.lastIndexOf(","));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static String getSaveContent(UploadInfo msg) {
            try {
                JSONObject object = new JSONObject();
                JSONObject baseInfo = new JSONObject();
                baseInfo.put("processName", msg.processName);
                baseInfo.put("packageName", msg.packageName);
                baseInfo.put(JSON_VERSION_NAME_TAG, msg.versionName);
                baseInfo.put(JSON_VERSION_CODE_TAG, msg.versionCode);
                object.put(JSON_BASE_INFO_TAG, baseInfo);
                object.put("msg", msg.info);
                object.put("threadName", msg.threadName);
                object.put(JSON_TOOKTIME_TAG, msg.tookTime);
                return object.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static UploadInfo getInfoByJsonString(String info) {
            try {
                JSONObject object = new JSONObject(info);
                JSONObject baseInfoObject = object.getJSONObject(JSON_BASE_INFO_TAG);
                String processName = baseInfoObject.optString("processName");
                String packageName = baseInfoObject.optString("packageName");
                String versionName = baseInfoObject.optString(JSON_VERSION_NAME_TAG);
                String versionCode = baseInfoObject.optString(JSON_VERSION_CODE_TAG);
                UploadInfo uploadInfo = new UploadInfo();
                uploadInfo.info = object.optString("msg");
                uploadInfo.processName = processName;
                uploadInfo.packageName = packageName;
                uploadInfo.versionName = versionName;
                uploadInfo.versionCode = versionCode;
                uploadInfo.threadName = object.optString("threadName");
                uploadInfo.tookTime = object.optLong(JSON_TOOKTIME_TAG);
                if (AnrMonitor.DBG) {
                    String str = AnrMonitor.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("getInfo info : ");
                    stringBuilder.append(uploadInfo);
                    Slog.d(str, stringBuilder.toString());
                }
                return uploadInfo;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static String getBaseInfo(String info) {
            try {
                return new JSONObject(info).getJSONObject(JSON_BASE_INFO_TAG).toString();
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static UploadInfo[] getInfoArray(String info) {
            JSONException e;
            try {
                try {
                    JSONObject object = new JSONObject(info);
                    JSONArray msgs = object.getJSONArray(JSON_INFO_ARRAY_TAG);
                    if (msgs == null) {
                        return null;
                    }
                    JSONObject baseInfoObject = object.getJSONObject(JSON_BASE_INFO_TAG);
                    String processName = baseInfoObject.optString("processName");
                    String packageName = baseInfoObject.optString("packageName");
                    String versionName = baseInfoObject.optString(JSON_VERSION_NAME_TAG);
                    String versionCode = baseInfoObject.optString(JSON_VERSION_CODE_TAG);
                    int size = msgs.length();
                    UploadInfo[] arrays = new UploadInfo[size];
                    for (int i = 0; i < size; i++) {
                        JSONObject obj = msgs.getJSONObject(i);
                        UploadInfo uploadInfo = new UploadInfo();
                        uploadInfo.info = obj.optString("msg");
                        uploadInfo.processName = processName;
                        uploadInfo.packageName = packageName;
                        uploadInfo.versionName = versionName;
                        uploadInfo.versionCode = versionCode;
                        uploadInfo.threadName = obj.optString("threadName");
                        uploadInfo.tookTime = obj.optLong(JSON_TOOKTIME_TAG);
                        arrays[i] = uploadInfo;
                        if (AnrMonitor.DBG) {
                            String str = AnrMonitor.TAG;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("getInfoArray i ");
                            stringBuilder.append(i);
                            stringBuilder.append(" info : ");
                            stringBuilder.append(uploadInfo);
                            Slog.d(str, stringBuilder.toString());
                        }
                    }
                    return arrays;
                } catch (JSONException e2) {
                    e = e2;
                    e.printStackTrace();
                    return null;
                }
            } catch (JSONException e3) {
                e = e3;
                String str2 = info;
                e.printStackTrace();
                return null;
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("info : ");
            stringBuilder.append(this.info);
            stringBuilder.append(" processName : ");
            stringBuilder.append(this.processName);
            stringBuilder.append(" versionName ");
            stringBuilder.append(this.versionName);
            stringBuilder.append(" versionCode ");
            stringBuilder.append(this.versionCode);
            stringBuilder.append(" tookTime : ");
            stringBuilder.append(this.tookTime);
            return stringBuilder.toString();
        }
    }

    private static final class WorkHandler extends Handler {
        static final int MONITOR_EXECUTION_TIMEOUT_MSG = 1;
        static final int REPORT_RECORDS = 2;

        public WorkHandler(Looper looper) {
            super(looper, null);
        }

        public void handleMessage(Message msg) {
            String str = "opt";
            String str2 = ",";
            int i = msg.what;
            if (i == 1) {
                Bundle data = msg.getData();
                if (data != null && AnrMonitor.isSystemBootCompleted() && !AnrMonitor.isSystemServer()) {
                    String monitorMsg = data.getString("monitor_msg", "no monitor message");
                    try {
                        JSONObject object = new JSONObject();
                        object.put("dump_service", Context.ACTIVITY_SERVICE);
                        JSONArray args = new JSONArray();
                        JSONObject opt1 = new JSONObject();
                        opt1.put(str, AnrMonitor.DUMP_APP_TRACE_TAG);
                        args.put(opt1);
                        JSONObject opt2 = new JSONObject();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(Process.myPid());
                        stringBuilder.append(str2);
                        stringBuilder.append(AnrMonitor.currentProcessName());
                        stringBuilder.append(str2);
                        stringBuilder.append(AnrMonitor.currentPackageName());
                        stringBuilder.append(str2);
                        stringBuilder.append("Event Time : ");
                        stringBuilder.append(AnrMonitor.toCalendarTime(System.currentTimeMillis()));
                        stringBuilder.append("\n");
                        stringBuilder.append(monitorMsg);
                        opt2.put(str, stringBuilder.toString());
                        args.put(opt2);
                        object.put("args", args);
                        MQSEventManagerDelegate.getInstance().reportSimpleEvent(-1, object.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (i == 2) {
                try {
                    str = msg.obj;
                    if (!TextUtils.isEmpty(str) && AnrMonitor.isSystemBootCompleted()) {
                        MQSEventManagerDelegate.getInstance().reportSimpleEvent(3, str);
                    }
                } catch (Exception e2) {
                    Log.e(AnrMonitor.TAG, "report record error.", e2);
                }
            }
        }
    }

    static {
        boolean z = true;
        if (CHECK_PARCEL_SIZE_KB <= 0) {
            z = false;
        }
        CHECK_PARCEL_SIZE_ENABLE = z;
        if (Build.IS_ALPHA_BUILD) {
            sMonitorList.add("com.android.systemui");
            sMonitorList.add(SettingsTree.SETTINGS_PACKAGE);
            sMonitorList.add(TelephonyManager.PHONE_PROCESS_NAME);
            sMonitorList.add("com.miui.home");
        }
    }

    public static boolean canMonitorAnr() {
        return !Build.IS_STABLE_VERSION && NOT_CTS_BUILD;
    }

    public static synchronized String toCalendarTime(long time) {
        String format;
        synchronized (AnrMonitor.class) {
            DATE.setTime(time);
            format = DATE_FORMATTER.format(DATE);
        }
        return format;
    }

    public static File createFile(String filePath) {
        return createFile(filePath, true);
    }

    public static File createFile(String filePath, boolean forceClear) {
        File file = new File(filePath);
        try {
            File tracesDir = file.getParentFile();
            if (!tracesDir.exists()) {
                tracesDir.mkdirs();
                if (!SELinux.restorecon(tracesDir)) {
                    return null;
                }
            }
            FileUtils.setPermissions(tracesDir.getPath(), 509, -1, -1);
            if (forceClear && file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileUtils.setPermissions(file.getPath(), 438, -1, -1);
            return file;
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to create file: ");
            stringBuilder.append(filePath);
            Slog.w(TAG, stringBuilder.toString(), e);
            return null;
        }
    }

    public static boolean isFileAvailable(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public static void readFileToWriter(String srcPath, Writer writer) throws IOException {
        File file = new File(srcPath);
        if (isFileAvailable(file) && writer != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LINE_SEPARATOR);
            stringBuilder.append("------ cat ");
            stringBuilder.append(srcPath);
            stringBuilder.append(" ------");
            stringBuilder.append(LINE_SEPARATOR);
            writer.write(stringBuilder.toString());
            writer.write(readFile(file));
        }
    }

    public static String readFile(File file) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            String readLine;
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            StringBuilder strBuffer = new StringBuilder();
            while (true) {
                readLine = bufferedReader.readLine();
                String line = readLine;
                if (readLine == null) {
                    break;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(line);
                stringBuilder.append(LINE_SEPARATOR);
                strBuffer.append(stringBuilder.toString());
            }
            readLine = strBuffer.toString();
            try {
                bufferedReader.close();
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return readLine;
        } catch (Exception e2) {
            e2.printStackTrace();
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                    return null;
                }
            }
            if (fileReader != null) {
                fileReader.close();
            }
            return null;
        } catch (Throwable th) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }

    public static void dumpBinderInfo(int pid, Writer writer) {
        try {
            readFileToWriter("/sys/kernel/debug/binder/failed_transaction_log", writer);
            readFileToWriter("/sys/kernel/debug/binder/transaction_log", writer);
            readFileToWriter("/sys/kernel/debug/binder/transactions", writer);
            readFileToWriter("/sys/kernel/debug/binder/stats", writer);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("/sys/kernel/debug/binder/proc/");
            stringBuilder.append(pid);
            readFileToWriter(stringBuilder.toString(), writer);
        } catch (IOException e) {
            Log.e(TAG, "dumpBinderInfo fail", e);
        }
    }

    public static void dumpCpuInfo(int pid, Writer writer) {
        String str = "/proc/";
        try {
            String srcPath = new StringBuilder();
            srcPath.append(str);
            srcPath.append(pid);
            srcPath.append("/schedstat");
            readFileToWriter(srcPath.toString(), writer);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(pid);
            stringBuilder.append("/stat");
            readFileToWriter(stringBuilder.toString(), writer);
        } catch (IOException e) {
            Log.e(TAG, "dumpCpuInfo fail", e);
        }
    }

    private static String getAnrPath(boolean bgAnr, String anrProcessName) {
        StringBuilder stringBuilder;
        String extraInfoFileName = "";
        if (bgAnr) {
            extraInfoFileName = "anr_info.txt";
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(ANR_INFO_HEAD);
            stringBuilder.append(anrProcessName);
            stringBuilder.append(Session.SESSION_SEPARATION_CHAR_CHILD);
            stringBuilder.append(toCalendarTime(System.currentTimeMillis()));
            stringBuilder.append(TRACES_FILE_TYPE);
            extraInfoFileName = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("/data/anr/");
        stringBuilder.append(extraInfoFileName);
        return stringBuilder.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x0083 A:{SYNTHETIC, Splitter:B:35:0x0083} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008d A:{SYNTHETIC, Splitter:B:41:0x008d} */
    public static java.io.File dumpAnrInfo(java.lang.StringBuilder r11, java.lang.String r12, java.lang.String r13, int r14, java.util.ArrayList<java.lang.Integer> r15, android.util.SparseArray<java.lang.Boolean> r16, java.lang.String[] r17, boolean r18, boolean r19) {
        /*
        r1 = 0;
        r2 = android.os.SystemClock.uptimeMillis();	 Catch:{ Exception -> 0x0074, all -> 0x006c }
        r4 = r13;
        r5 = r19;
        r0 = getAnrPath(r5, r13);	 Catch:{ Exception -> 0x0069, all -> 0x0066 }
        r6 = r0;
        r0 = createFile(r6);	 Catch:{ Exception -> 0x0069, all -> 0x0066 }
        r7 = r0;
        r0 = isFileAvailable(r7);	 Catch:{ Exception -> 0x0069, all -> 0x0066 }
        if (r0 == 0) goto L_0x005d;
    L_0x0018:
        r0 = new java.io.FileWriter;	 Catch:{ Exception -> 0x0069, all -> 0x0066 }
        r8 = 1;
        r0.<init>(r7, r8);	 Catch:{ Exception -> 0x0069, all -> 0x0066 }
        r1 = r0;
        r0 = r11.toString();	 Catch:{ Exception -> 0x0069, all -> 0x0066 }
        r1.write(r0);	 Catch:{ Exception -> 0x0069, all -> 0x0066 }
        if (r18 == 0) goto L_0x002b;
    L_0x0028:
        r11.append(r12);	 Catch:{ Exception -> 0x0069, all -> 0x0066 }
    L_0x002b:
        r8 = r12;
        r1.write(r12);	 Catch:{ Exception -> 0x005b }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x005b }
        r0.<init>();	 Catch:{ Exception -> 0x005b }
        r9 = LINE_SEPARATOR;	 Catch:{ Exception -> 0x005b }
        r0.append(r9);	 Catch:{ Exception -> 0x005b }
        r9 = "(dump anr info success and took ";
        r0.append(r9);	 Catch:{ Exception -> 0x005b }
        r9 = android.os.SystemClock.uptimeMillis();	 Catch:{ Exception -> 0x005b }
        r9 = r9 - r2;
        r0.append(r9);	 Catch:{ Exception -> 0x005b }
        r9 = "ms)";
        r0.append(r9);	 Catch:{ Exception -> 0x005b }
        r0 = r0.toString();	 Catch:{ Exception -> 0x005b }
        r1.write(r0);	 Catch:{ Exception -> 0x005b }
        r1.close();	 Catch:{ IOException -> 0x0059 }
        goto L_0x005a;
    L_0x0059:
        r0 = move-exception;
    L_0x005a:
        return r7;
    L_0x005b:
        r0 = move-exception;
        goto L_0x0079;
    L_0x005d:
        r8 = r12;
        if (r1 == 0) goto L_0x0087;
    L_0x0060:
        r1.close();	 Catch:{ IOException -> 0x0064 }
    L_0x0063:
        goto L_0x0087;
    L_0x0064:
        r0 = move-exception;
        goto L_0x0063;
    L_0x0066:
        r0 = move-exception;
        r8 = r12;
        goto L_0x0071;
    L_0x0069:
        r0 = move-exception;
        r8 = r12;
        goto L_0x0079;
    L_0x006c:
        r0 = move-exception;
        r8 = r12;
        r4 = r13;
        r5 = r19;
    L_0x0071:
        r2 = r1;
        r1 = r0;
        goto L_0x008b;
    L_0x0074:
        r0 = move-exception;
        r8 = r12;
        r4 = r13;
        r5 = r19;
    L_0x0079:
        r2 = "AnrMonitor";
        r3 = "Error happens when dumping anr info";
        android.util.Slog.e(r2, r3, r0);	 Catch:{ all -> 0x0089 }
        if (r1 == 0) goto L_0x0087;
    L_0x0083:
        r1.close();	 Catch:{ IOException -> 0x0064 }
        goto L_0x0063;
    L_0x0087:
        r0 = 0;
        return r0;
    L_0x0089:
        r0 = move-exception;
        goto L_0x0071;
    L_0x008b:
        if (r2 == 0) goto L_0x0092;
    L_0x008d:
        r2.close();	 Catch:{ IOException -> 0x0091 }
        goto L_0x0092;
    L_0x0091:
        r0 = move-exception;
    L_0x0092:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.AnrMonitor.dumpAnrInfo(java.lang.StringBuilder, java.lang.String, java.lang.String, int, java.util.ArrayList, android.util.SparseArray, java.lang.String[], boolean, boolean):java.io.File");
    }

    private static boolean isAllowedMonitor(BaseLooper looper) {
        if (looper != null) {
            return looper.isMonitorAnr();
        }
        return Process.myPid() == Process.myTid();
    }

    public static boolean isAllowedMonitorBinderCallSize(int dataSize) {
        return canMonitorAnr() && CHECK_PARCEL_SIZE_ENABLE && dataSize >= CHECK_PARCEL_SIZE_KB * 1024;
    }

    public static boolean isAllowedMonitorBinderCall(BaseLooper looper) {
        return isAllowedMonitor(looper);
    }

    public static boolean isAllowedMonitorInput(BaseLooper looper) {
        return isAllowedMonitor(looper);
    }

    public static boolean isLongTimeMsg(MessageMonitorInfo monitorInfo) {
        long tookTime = monitorInfo.getTookTime();
        if (tookTime <= 200 || tookTime >= MAX_TIMEOUT) {
            return false;
        }
        return true;
    }

    public static void checkMsgTime(Message msg, MessageMonitorInfo monitorInfo) {
        if (canMonitorAnr()) {
            long tookTime = monitorInfo.getTookTime();
            long tookTimeAfterDispatch = monitorInfo.getTookTimeAfterDispatch();
            if (tookTime <= MAX_TIMEOUT && tookTime >= 0) {
                if (tookTimeAfterDispatch > MESSAGE_MONITOR_TIMEOUT) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("The msg ");
                    stringBuilder.append(monitorInfo.getMonitorMessage());
                    stringBuilder.append(" took ");
                    stringBuilder.append(tookTime);
                    stringBuilder.append("ms and took ");
                    stringBuilder.append(tookTimeAfterDispatch);
                    stringBuilder.append("ms after dispatch.");
                    logAnr(stringBuilder.toString(), null);
                }
                if (tookTimeAfterDispatch > 3000) {
                    addMessageToHistory(msg, monitorInfo);
                }
            }
        }
    }

    private static synchronized void addMessageToHistory(Message msg, MessageMonitorInfo monitorInfo) {
        synchronized (AnrMonitor.class) {
            if (msg.target != null) {
                String str;
                UploadInfo uploadMsgInfo = new UploadInfo();
                StringBuilder b = new StringBuilder();
                b.append("The message {");
                if (msg.callback != null) {
                    b.append(" callback=");
                    b.append(msg.callback.getClass().getName());
                } else {
                    b.append(" what=");
                    b.append(msg.what);
                }
                b.append(" target=");
                b.append(msg.target.getClass().getName());
                b.append("}");
                uploadMsgInfo.info = b.toString();
                uploadMsgInfo.threadName = currentThreadName();
                uploadMsgInfo.tookTime = monitorInfo.getTookTimeAfterDispatch();
                if (MONITOR_MSG_EXECUTION) {
                    str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("The msg ");
                    stringBuilder.append(monitorInfo.getMonitorMessage());
                    stringBuilder.append(" add to history sMsgRecordIndex ");
                    stringBuilder.append(sMsgRecordIndex);
                    Log.d(str, stringBuilder.toString());
                }
                UploadInfo[] uploadInfoArr = sUploadMsgRecords;
                int i = sMsgRecordIndex;
                sMsgRecordIndex = i + 1;
                uploadInfoArr[i] = uploadMsgInfo;
                if (sMsgRecordIndex >= sUploadMsgRecords.length) {
                    str = UploadInfo.createJsonString(sUploadMsgRecords);
                    if (!TextUtils.isEmpty(str)) {
                        Message message = Message.obtain();
                        message.what = 2;
                        message.obj = str;
                        if (MONITOR_MSG_EXECUTION) {
                            String str2 = TAG;
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("report content : ");
                            stringBuilder2.append(str);
                            Log.d(str2, stringBuilder2.toString());
                        }
                        getWorkHandler().sendMessageAtFrontOfQueue(message);
                    }
                    sMsgRecordIndex = 0;
                }
            }
        }
    }

    public static void checkInputTime(long startTime, InputEvent event) {
        String str = "ms.";
        if (canMonitorAnr()) {
            long tookTime = SystemClock.uptimeMillis() - startTime;
            if (tookTime > 1000) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("The input: ");
                    stringBuilder.append(event);
                    stringBuilder.append(" took ");
                    stringBuilder.append(tookTime);
                    stringBuilder.append(str);
                    logAnr(stringBuilder.toString(), null);
                } catch (Exception e) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("checkInputTime failed and took time is : ");
                    stringBuilder2.append(tookTime);
                    stringBuilder2.append(str);
                    logAnr(stringBuilder2.toString(), e);
                }
            }
        }
    }

    /* JADX WARNING: Missing block: B:13:0x0030, code skipped:
            if (r0 <= 1000) goto L_0x0063;
     */
    /* JADX WARNING: Missing block: B:14:0x0032, code skipped:
            addInputToHistory(r8, r9, r10, r0);
            r2 = new java.lang.StringBuilder();
            r2.append("The input: ");
            r2.append(inputEventToString(r8));
            r2.append(" took ");
            r2.append(r0);
            r2.append("ms. Send to InputChannel ");
            r2.append(r10.getName());
            logAnr(r2.toString(), null);
     */
    /* JADX WARNING: Missing block: B:15:0x0063, code skipped:
            return;
     */
    public static void checkInputTime(android.view.InputEvent r8, android.view.InputEventReceiver r9, android.view.InputChannel r10) {
        /*
        r0 = canMonitorAnr();
        if (r0 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = 0;
        r2 = mInputEvnetStartTimeSeqMap;
        monitor-enter(r2);
        r3 = mInputEvnetStartTimeSeqMap;	 Catch:{ all -> 0x0064 }
        r4 = r8.getSequenceNumber();	 Catch:{ all -> 0x0064 }
        r3 = r3.indexOfKey(r4);	 Catch:{ all -> 0x0064 }
        if (r3 >= 0) goto L_0x001a;
    L_0x0018:
        monitor-exit(r2);	 Catch:{ all -> 0x0064 }
        return;
    L_0x001a:
        r4 = android.os.SystemClock.uptimeMillis();	 Catch:{ all -> 0x0064 }
        r6 = mInputEvnetStartTimeSeqMap;	 Catch:{ all -> 0x0064 }
        r6 = r6.valueAt(r3);	 Catch:{ all -> 0x0064 }
        r0 = r4 - r6;
        r4 = mInputEvnetStartTimeSeqMap;	 Catch:{ all -> 0x0064 }
        r4.removeAt(r3);	 Catch:{ all -> 0x0064 }
        monitor-exit(r2);	 Catch:{ all -> 0x0064 }
        r2 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x0063;
    L_0x0032:
        addInputToHistory(r8, r9, r10, r0);
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "The input: ";
        r2.append(r3);
        r3 = inputEventToString(r8);
        r2.append(r3);
        r3 = " took ";
        r2.append(r3);
        r2.append(r0);
        r3 = "ms. Send to InputChannel ";
        r2.append(r3);
        r3 = r10.getName();
        r2.append(r3);
        r2 = r2.toString();
        r3 = 0;
        logAnr(r2, r3);
    L_0x0063:
        return;
    L_0x0064:
        r3 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0064 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.AnrMonitor.checkInputTime(android.view.InputEvent, android.view.InputEventReceiver, android.view.InputChannel):void");
    }

    public static void dispatchInputEventStart(InputEvent event) {
        synchronized (mInputEvnetStartTimeSeqMap) {
            mInputEvnetStartTimeSeqMap.put(event.getSequenceNumber(), SystemClock.uptimeMillis());
        }
    }

    private static synchronized void addInputToHistory(InputEvent event, InputEventReceiver receiver, InputChannel inputChannel, long tookTime) {
        synchronized (AnrMonitor.class) {
            UploadInfo uploadInputInfo = new UploadInfo();
            StringBuilder b = new StringBuilder();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The inputEvent ");
            stringBuilder.append(inputEventToString(event));
            stringBuilder.append(" {");
            b.append(stringBuilder.toString());
            b.append(" target reveicer = ");
            b.append(receiver.getClass().getName());
            b.append(" target InputChannel = ");
            b.append(inputChannelToString(inputChannel));
            b.append("}");
            uploadInputInfo.info = b.toString();
            uploadInputInfo.tookTime = tookTime;
            UploadInfo[] uploadInfoArr = sUploadInputRecords;
            int i = sInputRecordIndex;
            sInputRecordIndex = i + 1;
            uploadInfoArr[i] = uploadInputInfo;
            if (sInputRecordIndex >= sUploadInputRecords.length) {
                String reportContent = UploadInfo.createJsonString(sUploadInputRecords);
                if (!TextUtils.isEmpty(reportContent)) {
                    Message message = Message.obtain();
                    message.what = 2;
                    message.obj = reportContent;
                    if (MONITOR_MSG_EXECUTION) {
                        String str = TAG;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("report content : ");
                        stringBuilder2.append(reportContent);
                        Log.d(str, stringBuilder2.toString());
                    }
                    getWorkHandler().sendMessageAtFrontOfQueue(message);
                }
                sInputRecordIndex = 0;
            }
        }
    }

    private static String inputEventToString(InputEvent event) {
        StringBuilder msg = new StringBuilder();
        String str = " }";
        if (event instanceof KeyEvent) {
            KeyEvent keyEvent = (KeyEvent) event;
            msg.append("KeyEvent { action=");
            msg.append(KeyEvent.actionToString(keyEvent.getAction()));
            msg.append(", keyCode=");
            msg.append(KeyEvent.keyCodeToString(keyEvent.getKeyCode()));
            msg.append(str);
            return msg.toString();
        }
        MotionEvent motionEvent = (MotionEvent) event;
        msg.append("MotionEvent { action=");
        msg.append(MotionEvent.actionToString(motionEvent.getAction()));
        msg.append(str);
        return msg.toString();
    }

    private static String inputChannelToString(InputChannel inputChannel) {
        String str = "uninitialized";
        String str2 = "null";
        if (inputChannel == null) {
            return str2;
        }
        try {
            String inputChannelName = inputChannel.getName();
            if (inputChannelName.equals(str)) {
                return str;
            }
            String[] splitResult = inputChannelName.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            if (splitResult.length >= 2) {
                return splitResult[splitResult.length - 2];
            }
            return inputChannelName;
        } catch (ArrayIndexOutOfBoundsException e) {
            logAnr("Error getting inputChannel name ", e);
            return str2;
        }
    }

    static synchronized String currentPackageName() {
        String str;
        synchronized (AnrMonitor.class) {
            String pkgName = ActivityThread.currentPackageName();
            if (TextUtils.isEmpty(pkgName)) {
                sPkgName = "system_server";
            } else {
                sPkgName = pkgName;
            }
            str = sPkgName;
        }
        return str;
    }

    static synchronized boolean isSystemServer() {
        boolean equals;
        synchronized (AnrMonitor.class) {
            equals = "system_server".equals(currentPackageName());
        }
        return equals;
    }

    static synchronized boolean isSystemApp() {
        boolean z;
        synchronized (AnrMonitor.class) {
            z = false;
            if (sIsSystemApp == null) {
                try {
                    Context context = ActivityThread.currentApplication().getApplicationContext();
                    if (context != null) {
                        PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                        if (!(info == null || info.applicationInfo == null)) {
                            boolean z2 = true;
                            boolean isSystemApp = (info.applicationInfo.flags & 1) != 0;
                            boolean isSystemUpdateApp = (info.applicationInfo.flags & 128) != 0;
                            if (!isSystemApp) {
                                if (!isSystemUpdateApp) {
                                    z2 = false;
                                }
                            }
                            sIsSystemApp = new Boolean(z2);
                        }
                    }
                } catch (Exception e) {
                }
            }
            if (sIsSystemApp != null) {
                z = sIsSystemApp.booleanValue();
            }
        }
        return z;
    }

    static String currentProcessName() {
        if (TextUtils.isEmpty(sProcessName)) {
            String processName = ActivityThread.currentProcessName();
            if (TextUtils.isEmpty(processName)) {
                sProcessName = "system_server";
            } else {
                sProcessName = processName;
            }
        }
        return sProcessName;
    }

    static String currentThreadName() {
        return Thread.currentThread().getName();
    }

    static int currentVersionCode() {
        if (sVersionCode == 0) {
            try {
                Context context = ActivityThread.currentApplication().getApplicationContext();
                if (context != null) {
                    PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    if (!(info == null || info.versionCode == 0)) {
                        sVersionCode = info.versionCode;
                    }
                }
            } catch (Exception e) {
            }
        }
        return sVersionCode;
    }

    static String currentVersionName() {
        if (TextUtils.isEmpty(sVersionName)) {
            try {
                Context context = ActivityThread.currentApplication().getApplicationContext();
                if (context != null) {
                    PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    if (!(info == null || info.versionName == null)) {
                        sVersionName = info.versionName;
                    }
                }
            } catch (Exception e) {
            }
        }
        return sVersionName;
    }

    static void startMonitor(Message msg, MessageMonitorInfo monitorInfo) {
        if (canMonitorMessage()) {
            String info = monitorInfo.getMonitorMessage();
            if (!TextUtils.isEmpty(info)) {
                Message monitorMsg = getWorkHandler().obtainMessage();
                monitorMsg.what = 1;
                monitorMsg.obj = monitorInfo.createMonitorDigest();
                Bundle b = new Bundle();
                b.putString("monitor_msg", info);
                monitorMsg.setData(b);
                getWorkHandler().sendMessageDelayed(monitorMsg, MESSAGE_EXECUTION_TIMEOUT);
            }
        }
    }

    static void finishMonitor(Message msg, MessageMonitorInfo monitorInfo) {
        if (canMonitorMessage()) {
            getWorkHandler().removeMessages(1, monitorInfo.getMonitorDigest());
        }
    }

    static boolean canMonitorMessage() {
        boolean z = true;
        if (MONITOR_MSG_EXECUTION) {
            return true;
        }
        if (sMonitorList.isEmpty() || !sMonitorList.contains(currentPackageName())) {
            z = false;
        }
        return z;
    }

    public static Handler getWorkHandler() {
        if (sWorkHandler == null) {
            synchronized (sInstanceSync) {
                if (sWorkHandler == null) {
                    HandlerThread monitorThread = new HandlerThread("work-thread");
                    monitorThread.start();
                    sWorkHandler = new WorkHandler(monitorThread.getLooper());
                }
            }
        }
        return sWorkHandler;
    }

    public static void checkBinderCallTime(long startTime) {
        if (canMonitorAnr()) {
            long tookTime = SystemClock.uptimeMillis() - startTime;
            if (tookTime > 1000) {
                if (!PerfSupervisionSettings.isPerfEventReportable() && tookTime >= 3000) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("The binder call took ");
                    stringBuilder.append(tookTime);
                    stringBuilder.append("ms.");
                    logAnr(stringBuilder.toString(), new Throwable());
                }
                addBinderCallTimeToHistory(tookTime);
            }
        }
    }

    private static synchronized void addBinderCallTimeToHistory(long tookTime) {
        synchronized (AnrMonitor.class) {
            UploadInfo uploadBinderInfo = new UploadInfo();
            StringBuilder b = new StringBuilder();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The binder call ");
            stringBuilder.append(binderCallToString());
            b.append(stringBuilder.toString());
            uploadBinderInfo.info = b.toString();
            uploadBinderInfo.tookTime = tookTime;
            uploadBinderInfo.threadName = currentThreadName();
            UploadInfo[] uploadInfoArr = sUploadBinderRecords;
            int i = sBinderRecordIndex;
            sBinderRecordIndex = i + 1;
            uploadInfoArr[i] = uploadBinderInfo;
            if (sBinderRecordIndex >= sUploadBinderRecords.length) {
                String reportContent = UploadInfo.createJsonString(sUploadBinderRecords);
                if (!TextUtils.isEmpty(reportContent)) {
                    Message message = Message.obtain();
                    message.what = 2;
                    message.obj = reportContent;
                    if (MONITOR_MSG_EXECUTION) {
                        String str = TAG;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("binder call report content : ");
                        stringBuilder2.append(reportContent);
                        Log.d(str, stringBuilder2.toString());
                    }
                    getWorkHandler().sendMessageAtFrontOfQueue(message);
                }
                sBinderRecordIndex = 0;
            }
        }
    }

    private static String binderCallToString() {
        StringBuilder b = new StringBuilder();
        String[] caller = Debug.getCallers(3, 1).split(":");
        if (caller.length != 2 || TextUtils.isEmpty(caller[0])) {
            return "";
        }
        b.append("{ ");
        b.append(caller[0]);
        b.append(" }");
        return b.toString();
    }

    public static void doDump(String cmds) {
        try {
            JSONObject object = new JSONObject(cmds);
            String service = object.optString("dump_service");
            JSONArray array = object.getJSONArray("args");
            int size = array.length();
            String[] args = new String[array.length()];
            for (int i = 0; i < size; i++) {
                args[i] = array.getJSONObject(i).optString("opt");
            }
            try {
                ServiceManager.getService(service).dump(FileDescriptor.out, args);
            } catch (RemoteException e) {
                Log.e(TAG, "dump failed", e);
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    private static boolean isSystemBootCompleted() {
        if (!sSystemBootCompleted) {
            sSystemBootCompleted = "1".equals(SystemProperties.get("sys.boot_completed"));
        }
        return sSystemBootCompleted;
    }

    public static void logAnr(String msg, Throwable tr) {
        String str = MONITOR_TAG;
        if (tr != null) {
            Slog.w(str, msg, tr);
        } else {
            Slog.w(str, msg);
        }
    }

    public static void logPerfEvent(String msg, PerfEvent event) {
        String str = MONITOR_TAG;
        Slog.w(str, msg);
        if (event != null) {
            Slog.w(str, event.toJson().toString());
        }
    }

    public static void logDumpTrace(String msg, Throwable tr) {
        String str = DUMP_TRACE_TAG;
        if (tr != null) {
            Slog.w(str, msg, tr);
        } else {
            Slog.d(str, msg);
        }
    }

    public static void renameTraces(String processName) {
        String tracesPath = SystemProperties.get("dalvik.vm.stack-trace-file", null);
        if (tracesPath != null && tracesPath.length() != 0) {
            File traceRenameFile = new File(tracesPath);
            String newTracesPath = new StringBuilder();
            newTracesPath.append("/data/anr/traces_");
            newTracesPath.append(processName);
            newTracesPath.append(Session.SESSION_SEPARATION_CHAR_CHILD);
            newTracesPath.append(toCalendarTime(System.currentTimeMillis()));
            newTracesPath.append(TRACES_FILE_TYPE);
            traceRenameFile.renameTo(new File(newTracesPath.toString()));
            File tracesDir = new File(tracesPath.substring(null, tracesPath.lastIndexOf("/")));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(TRACES_HEAD);
            stringBuilder.append(processName);
            deleteUnnecessaryFileIfNeeded(tracesDir, stringBuilder.toString(), 5);
            stringBuilder = new StringBuilder();
            stringBuilder.append(ANR_INFO_HEAD);
            stringBuilder.append(processName);
            deleteUnnecessaryFileIfNeeded(tracesDir, stringBuilder.toString(), 5);
        }
    }

    private static void deleteUnnecessaryFileIfNeeded(File dir, String key, int limit) {
        if (dir != null && dir.isDirectory()) {
            int i;
            List<FileInfo> fileInfoList = new ArrayList();
            for (File subFile : dir.listFiles()) {
                String filename = subFile.getName();
                if (filename != null && filename.contains(key)) {
                    fileInfoList.add(new FileInfo(subFile, subFile.lastModified()));
                }
            }
            try {
                Collections.sort(fileInfoList);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            for (i = limit; i < fileInfoList.size(); i++) {
                File file = ((FileInfo) fileInfoList.get(i)).getFile();
                if (file != null) {
                    file.delete();
                }
            }
        }
    }

    public static void screenHangMonitor(int keyCode, boolean down, long pressTime) {
        if (keyCode == 26) {
            if (down) {
                getWorkHandler().postDelayed(mScreenHangRunnable, ((long) SCREEN_HANG_TIMEOUT) + pressTime);
            } else {
                getWorkHandler().removeCallbacks(mScreenHangRunnable);
            }
        }
    }
}
