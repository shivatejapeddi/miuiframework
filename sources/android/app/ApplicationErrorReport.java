package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.util.Printer;
import com.android.internal.util.FastPrintWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ApplicationErrorReport implements Parcelable {
    public static final Creator<ApplicationErrorReport> CREATOR = new Creator<ApplicationErrorReport>() {
        public ApplicationErrorReport createFromParcel(Parcel source) {
            return new ApplicationErrorReport(source);
        }

        public ApplicationErrorReport[] newArray(int size) {
            return new ApplicationErrorReport[size];
        }
    };
    static final String DEFAULT_ERROR_RECEIVER_PROPERTY = "ro.error.receiver.default";
    static final String SYSTEM_APPS_ERROR_RECEIVER_PROPERTY = "ro.error.receiver.system.apps";
    public static final int TYPE_ANR = 2;
    public static final int TYPE_BATTERY = 3;
    public static final int TYPE_CRASH = 1;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_RUNNING_SERVICE = 5;
    public AnrInfo anrInfo;
    public BatteryInfo batteryInfo;
    public CrashInfo crashInfo;
    public String installerPackageName;
    public String packageName;
    public String processName;
    public RunningServiceInfo runningServiceInfo;
    public boolean systemApp;
    public long time;
    public int type;

    public static class AnrInfo {
        public String activity;
        public String cause;
        public String info;

        public AnrInfo(Parcel in) {
            this.activity = in.readString();
            this.cause = in.readString();
            this.info = in.readString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.activity);
            dest.writeString(this.cause);
            dest.writeString(this.info);
        }

        public void dump(Printer pw, String prefix) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("activity: ");
            stringBuilder.append(this.activity);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("cause: ");
            stringBuilder.append(this.cause);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("info: ");
            stringBuilder.append(this.info);
            pw.println(stringBuilder.toString());
        }
    }

    public static class BatteryInfo {
        public String checkinDetails;
        public long durationMicros;
        public String usageDetails;
        public int usagePercent;

        public BatteryInfo(Parcel in) {
            this.usagePercent = in.readInt();
            this.durationMicros = in.readLong();
            this.usageDetails = in.readString();
            this.checkinDetails = in.readString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.usagePercent);
            dest.writeLong(this.durationMicros);
            dest.writeString(this.usageDetails);
            dest.writeString(this.checkinDetails);
        }

        public void dump(Printer pw, String prefix) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("usagePercent: ");
            stringBuilder.append(this.usagePercent);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("durationMicros: ");
            stringBuilder.append(this.durationMicros);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("usageDetails: ");
            stringBuilder.append(this.usageDetails);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("checkinDetails: ");
            stringBuilder.append(this.checkinDetails);
            pw.println(stringBuilder.toString());
        }
    }

    public static class CrashInfo {
        public String crashTag;
        public String exceptionClassName;
        public String exceptionMessage;
        public String stackTrace;
        public String throwClassName;
        public String throwFileName;
        public int throwLineNumber;
        public String throwMethodName;

        public CrashInfo(Throwable tr) {
            String msg;
            Writer sw = new StringWriter();
            PrintWriter pw = new FastPrintWriter(sw, false, 256);
            tr.printStackTrace(pw);
            pw.flush();
            this.stackTrace = sanitizeString(sw.toString());
            this.exceptionMessage = tr.getMessage();
            Throwable rootTr = tr;
            while (tr.getCause() != null) {
                tr = tr.getCause();
                if (tr.getStackTrace() != null && tr.getStackTrace().length > 0) {
                    rootTr = tr;
                }
                msg = tr.getMessage();
                if (msg != null && msg.length() > 0) {
                    this.exceptionMessage = msg;
                }
            }
            this.exceptionClassName = rootTr.getClass().getName();
            if (rootTr.getStackTrace().length > 0) {
                StackTraceElement trace = rootTr.getStackTrace()[0];
                this.throwFileName = trace.getFileName();
                this.throwClassName = trace.getClassName();
                this.throwMethodName = trace.getMethodName();
                this.throwLineNumber = trace.getLineNumber();
            } else {
                msg = "unknown";
                this.throwFileName = msg;
                this.throwClassName = msg;
                this.throwMethodName = msg;
                this.throwLineNumber = 0;
            }
            this.exceptionMessage = sanitizeString(this.exceptionMessage);
        }

        public void appendStackTrace(String tr) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.stackTrace);
            stringBuilder.append(tr);
            this.stackTrace = sanitizeString(stringBuilder.toString());
        }

        private String sanitizeString(String s) {
            int acceptableLength = 10240 + 10240;
            if (s == null || s.length() <= acceptableLength) {
                return s;
            }
            String replacement = new StringBuilder();
            replacement.append("\n[TRUNCATED ");
            replacement.append(s.length() - acceptableLength);
            replacement.append(" CHARS]\n");
            replacement = replacement.toString();
            StringBuilder sb = new StringBuilder(replacement.length() + acceptableLength);
            sb.append(s.substring(0, 10240));
            sb.append(replacement);
            sb.append(s.substring(s.length() - 10240));
            return sb.toString();
        }

        public CrashInfo(Parcel in) {
            this.exceptionClassName = in.readString();
            this.exceptionMessage = in.readString();
            this.throwFileName = in.readString();
            this.throwClassName = in.readString();
            this.throwMethodName = in.readString();
            this.throwLineNumber = in.readInt();
            this.stackTrace = in.readString();
            this.crashTag = in.readString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            int start = dest.dataPosition();
            dest.writeString(this.exceptionClassName);
            dest.writeString(this.exceptionMessage);
            dest.writeString(this.throwFileName);
            dest.writeString(this.throwClassName);
            dest.writeString(this.throwMethodName);
            dest.writeInt(this.throwLineNumber);
            dest.writeString(this.stackTrace);
            dest.writeString(this.crashTag);
            int total = dest.dataPosition() - start;
        }

        public void dump(Printer pw, String prefix) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("exceptionClassName: ");
            stringBuilder.append(this.exceptionClassName);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("exceptionMessage: ");
            stringBuilder.append(this.exceptionMessage);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("throwFileName: ");
            stringBuilder.append(this.throwFileName);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("throwClassName: ");
            stringBuilder.append(this.throwClassName);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("throwMethodName: ");
            stringBuilder.append(this.throwMethodName);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("throwLineNumber: ");
            stringBuilder.append(this.throwLineNumber);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("stackTrace: ");
            stringBuilder.append(this.stackTrace);
            pw.println(stringBuilder.toString());
        }
    }

    public static class ParcelableCrashInfo extends CrashInfo implements Parcelable {
        public static final Creator<ParcelableCrashInfo> CREATOR = new Creator<ParcelableCrashInfo>() {
            public ParcelableCrashInfo createFromParcel(Parcel in) {
                return new ParcelableCrashInfo(in);
            }

            public ParcelableCrashInfo[] newArray(int size) {
                return new ParcelableCrashInfo[size];
            }
        };

        public ParcelableCrashInfo(Throwable tr) {
            super(tr);
        }

        public ParcelableCrashInfo(Parcel in) {
            super(in);
        }

        public int describeContents() {
            return 0;
        }
    }

    public static class RunningServiceInfo {
        public long durationMillis;
        public String serviceDetails;

        public RunningServiceInfo(Parcel in) {
            this.durationMillis = in.readLong();
            this.serviceDetails = in.readString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.durationMillis);
            dest.writeString(this.serviceDetails);
        }

        public void dump(Printer pw, String prefix) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("durationMillis: ");
            stringBuilder.append(this.durationMillis);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("serviceDetails: ");
            stringBuilder.append(this.serviceDetails);
            pw.println(stringBuilder.toString());
        }
    }

    ApplicationErrorReport(Parcel in) {
        readFromParcel(in);
    }

    public static ComponentName getErrorReportReceiver(Context context, String packageName, int appFlags) {
        if (Global.getInt(context.getContentResolver(), Global.SEND_ACTION_APP_ERROR, 0) == 0) {
            return null;
        }
        ComponentName result;
        PackageManager pm = context.getPackageManager();
        String candidate = null;
        try {
            candidate = pm.getInstallerPackageName(packageName);
        } catch (IllegalArgumentException e) {
        }
        if (candidate != null) {
            result = getErrorReportReceiver(pm, packageName, candidate);
            if (result != null) {
                return result;
            }
        }
        if ((appFlags & 1) != 0) {
            result = getErrorReportReceiver(pm, packageName, SystemProperties.get(SYSTEM_APPS_ERROR_RECEIVER_PROPERTY));
            if (result != null) {
                return result;
            }
        }
        return getErrorReportReceiver(pm, packageName, SystemProperties.get(DEFAULT_ERROR_RECEIVER_PROPERTY));
    }

    /* JADX WARNING: Missing block: B:14:0x0032, code skipped:
            return null;
     */
    static android.content.ComponentName getErrorReportReceiver(android.content.pm.PackageManager r4, java.lang.String r5, java.lang.String r6) {
        /*
        r0 = 0;
        if (r6 == 0) goto L_0x0032;
    L_0x0003:
        r1 = r6.length();
        if (r1 != 0) goto L_0x000a;
    L_0x0009:
        goto L_0x0032;
    L_0x000a:
        r1 = r6.equals(r5);
        if (r1 == 0) goto L_0x0011;
    L_0x0010:
        return r0;
    L_0x0011:
        r1 = new android.content.Intent;
        r2 = "android.intent.action.APP_ERROR";
        r1.<init>(r2);
        r1.setPackage(r6);
        r2 = 0;
        r2 = r4.resolveActivity(r1, r2);
        if (r2 == 0) goto L_0x0031;
    L_0x0022:
        r3 = r2.activityInfo;
        if (r3 != 0) goto L_0x0027;
    L_0x0026:
        goto L_0x0031;
    L_0x0027:
        r0 = new android.content.ComponentName;
        r3 = r2.activityInfo;
        r3 = r3.name;
        r0.<init>(r6, r3);
        return r0;
    L_0x0031:
        return r0;
    L_0x0032:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ApplicationErrorReport.getErrorReportReceiver(android.content.pm.PackageManager, java.lang.String, java.lang.String):android.content.ComponentName");
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.packageName);
        dest.writeString(this.installerPackageName);
        dest.writeString(this.processName);
        dest.writeLong(this.time);
        dest.writeInt(this.systemApp);
        dest.writeInt(this.crashInfo != null ? 1 : 0);
        int i = this.type;
        if (i == 1) {
            CrashInfo crashInfo = this.crashInfo;
            if (crashInfo != null) {
                crashInfo.writeToParcel(dest, flags);
            }
        } else if (i == 2) {
            this.anrInfo.writeToParcel(dest, flags);
        } else if (i == 3) {
            this.batteryInfo.writeToParcel(dest, flags);
        } else if (i == 5) {
            this.runningServiceInfo.writeToParcel(dest, flags);
        }
    }

    public void readFromParcel(Parcel in) {
        this.type = in.readInt();
        this.packageName = in.readString();
        this.installerPackageName = in.readString();
        this.processName = in.readString();
        this.time = in.readLong();
        boolean z = false;
        this.systemApp = in.readInt() == 1;
        if (in.readInt() == 1) {
            z = true;
        }
        boolean hasCrashInfo = z;
        int i = this.type;
        if (i == 1) {
            this.crashInfo = hasCrashInfo ? new CrashInfo(in) : null;
            this.anrInfo = null;
            this.batteryInfo = null;
            this.runningServiceInfo = null;
        } else if (i == 2) {
            this.anrInfo = new AnrInfo(in);
            this.crashInfo = null;
            this.batteryInfo = null;
            this.runningServiceInfo = null;
        } else if (i == 3) {
            this.batteryInfo = new BatteryInfo(in);
            this.anrInfo = null;
            this.crashInfo = null;
            this.runningServiceInfo = null;
        } else if (i == 5) {
            this.batteryInfo = null;
            this.anrInfo = null;
            this.crashInfo = null;
            this.runningServiceInfo = new RunningServiceInfo(in);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void dump(Printer pw, String prefix) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("type: ");
        stringBuilder.append(this.type);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("packageName: ");
        stringBuilder.append(this.packageName);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("installerPackageName: ");
        stringBuilder.append(this.installerPackageName);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("processName: ");
        stringBuilder.append(this.processName);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("time: ");
        stringBuilder.append(this.time);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("systemApp: ");
        stringBuilder.append(this.systemApp);
        pw.println(stringBuilder.toString());
        int i = this.type;
        if (i == 1) {
            this.crashInfo.dump(pw, prefix);
        } else if (i == 2) {
            this.anrInfo.dump(pw, prefix);
        } else if (i == 3) {
            this.batteryInfo.dump(pw, prefix);
        } else if (i == 5) {
            this.runningServiceInfo.dump(pw, prefix);
        }
    }
}
