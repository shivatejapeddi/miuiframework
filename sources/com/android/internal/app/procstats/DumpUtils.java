package com.android.internal.app.procstats;

import android.accounts.GrantCredentialsPermissionActivity;
import android.app.backup.FullBackup;
import android.content.Context;
import android.miui.BiometricConnect;
import android.os.IncidentManager;
import android.os.UserHandle;
import android.provider.CalendarContract.CalendarCache;
import android.provider.MiuiSettings.System;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.app.DumpHeapActivity;
import com.miui.mishare.RemoteDevice;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.apache.miui.commons.lang3.ClassUtils;

public final class DumpUtils {
    public static final String[] ADJ_MEM_NAMES_CSV = new String[]{"norm", "mod", System.POWER_MODE_VALUE_LOW, "crit"};
    static final int[] ADJ_MEM_PROTO_ENUMS = new int[]{1, 2, 3, 4};
    static final String[] ADJ_MEM_TAGS = new String[]{"n", "m", "l", FullBackup.CACHE_TREE_TOKEN};
    public static final String[] ADJ_SCREEN_NAMES_CSV = new String[]{"off", "on"};
    static final int[] ADJ_SCREEN_PROTO_ENUMS = new int[]{1, 2};
    static final String[] ADJ_SCREEN_TAGS = new String[]{"0", "1"};
    static final String CSV_SEP = "\t";
    public static final String[] STATE_LABELS = new String[14];
    public static final String STATE_LABEL_CACHED = "  (Cached)";
    public static final String STATE_LABEL_TOTAL = "     TOTAL";
    public static final String[] STATE_NAMES = new String[14];
    public static final String[] STATE_NAMES_CSV = new String[14];
    static final int[] STATE_PROTO_ENUMS = new int[14];
    static final String[] STATE_TAGS = new String[14];

    static {
        String[] strArr = STATE_NAMES;
        strArr[0] = "Persist";
        strArr[1] = "Top";
        strArr[2] = "ImpFg";
        strArr[3] = "ImpBg";
        strArr[4] = "Backup";
        strArr[5] = "Service";
        strArr[6] = "ServRst";
        strArr[7] = "Receivr";
        strArr[8] = "HeavyWt";
        strArr[9] = "Home";
        strArr[10] = "LastAct";
        strArr[11] = "CchAct";
        strArr[12] = "CchCAct";
        strArr[13] = "CchEmty";
        strArr = STATE_LABELS;
        strArr[0] = "Persistent";
        strArr[1] = "       Top";
        strArr[2] = "    Imp Fg";
        strArr[3] = "    Imp Bg";
        strArr[4] = "    Backup";
        strArr[5] = "   Service";
        strArr[6] = "Service Rs";
        strArr[7] = "  Receiver";
        strArr[8] = " Heavy Wgt";
        strArr[9] = "    (Home)";
        strArr[10] = "(Last Act)";
        strArr[11] = " (Cch Act)";
        strArr[12] = "(Cch CAct)";
        strArr[13] = "(Cch Emty)";
        strArr = STATE_NAMES_CSV;
        strArr[0] = "pers";
        strArr[1] = "top";
        strArr[2] = "impfg";
        strArr[3] = "impbg";
        strArr[4] = Context.BACKUP_SERVICE;
        strArr[5] = "service";
        strArr[6] = "service-rs";
        strArr[7] = IncidentManager.URI_PARAM_RECEIVER_CLASS;
        strArr[8] = "heavy";
        strArr[9] = CalendarCache.TIMEZONE_TYPE_HOME;
        strArr[10] = "lastact";
        strArr[11] = "cch-activity";
        strArr[12] = "cch-aclient";
        strArr[13] = "cch-empty";
        strArr = STATE_TAGS;
        strArr[0] = "p";
        strArr[1] = IncidentManager.URI_PARAM_TIMESTAMP;
        strArr[2] = FullBackup.FILES_TREE_TOKEN;
        strArr[3] = "b";
        strArr[4] = "u";
        strArr[5] = RemoteDevice.KEY_STATUS;
        strArr[6] = "x";
        strArr[7] = "r";
        strArr[8] = BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_W;
        strArr[9] = BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_H;
        strArr[10] = "l";
        strArr[11] = FullBackup.APK_TREE_TOKEN;
        strArr[12] = FullBackup.CACHE_TREE_TOKEN;
        strArr[13] = "e";
        int[] iArr = STATE_PROTO_ENUMS;
        iArr[0] = 1;
        iArr[1] = 2;
        iArr[2] = 3;
        iArr[3] = 4;
        iArr[4] = 5;
        iArr[5] = 6;
        iArr[6] = 7;
        iArr[7] = 8;
        iArr[8] = 9;
        iArr[9] = 10;
        iArr[10] = 11;
        iArr[11] = 12;
        iArr[12] = 13;
        iArr[13] = 14;
    }

    private DumpUtils() {
    }

    public static void printScreenLabel(PrintWriter pw, int offset) {
        if (offset == -1) {
            pw.print("     ");
        } else if (offset == 0) {
            pw.print("SOff/");
        } else if (offset != 4) {
            pw.print("????/");
        } else {
            pw.print(" SOn/");
        }
    }

    public static void printScreenLabelCsv(PrintWriter pw, int offset) {
        if (offset == -1) {
            return;
        }
        if (offset == 0) {
            pw.print(ADJ_SCREEN_NAMES_CSV[0]);
        } else if (offset != 4) {
            pw.print("???");
        } else {
            pw.print(ADJ_SCREEN_NAMES_CSV[1]);
        }
    }

    public static void printMemLabel(PrintWriter pw, int offset, char sep) {
        if (offset == -1) {
            pw.print("    ");
            if (sep != 0) {
                pw.print(' ');
            }
        } else if (offset == 0) {
            pw.print("Norm");
            if (sep != 0) {
                pw.print(sep);
            }
        } else if (offset == 1) {
            pw.print(" Mod");
            if (sep != 0) {
                pw.print(sep);
            }
        } else if (offset == 2) {
            pw.print(" Low");
            if (sep != 0) {
                pw.print(sep);
            }
        } else if (offset != 3) {
            pw.print("????");
            if (sep != 0) {
                pw.print(sep);
            }
        } else {
            pw.print("Crit");
            if (sep != 0) {
                pw.print(sep);
            }
        }
    }

    public static void printMemLabelCsv(PrintWriter pw, int offset) {
        if (offset < 0) {
            return;
        }
        if (offset <= 3) {
            pw.print(ADJ_MEM_NAMES_CSV[offset]);
        } else {
            pw.print("???");
        }
    }

    public static void printPercent(PrintWriter pw, double fraction) {
        fraction *= 100.0d;
        if (fraction < 1.0d) {
            pw.print(String.format("%.2f", new Object[]{Double.valueOf(fraction)}));
        } else if (fraction < 10.0d) {
            pw.print(String.format("%.1f", new Object[]{Double.valueOf(fraction)}));
        } else {
            pw.print(String.format("%.0f", new Object[]{Double.valueOf(fraction)}));
        }
        pw.print("%");
    }

    public static void printProcStateTag(PrintWriter pw, int state) {
        printArrayEntry(pw, STATE_TAGS, printArrayEntry(pw, ADJ_MEM_TAGS, printArrayEntry(pw, ADJ_SCREEN_TAGS, state, 56), 14), 1);
    }

    public static void printProcStateTagProto(ProtoOutputStream proto, long screenId, long memId, long stateId, int state) {
        ProtoOutputStream protoOutputStream = proto;
        long j = memId;
        ProtoOutputStream protoOutputStream2 = proto;
        long j2 = stateId;
        printProto(protoOutputStream2, j2, STATE_PROTO_ENUMS, printProto(protoOutputStream, j, ADJ_MEM_PROTO_ENUMS, printProto(proto, screenId, ADJ_SCREEN_PROTO_ENUMS, state, 56), 14), 1);
    }

    public static void printAdjTag(PrintWriter pw, int state) {
        printArrayEntry(pw, ADJ_MEM_TAGS, printArrayEntry(pw, ADJ_SCREEN_TAGS, state, 4), 1);
    }

    public static void printProcStateAdjTagProto(ProtoOutputStream proto, long screenId, long memId, int state) {
        ProtoOutputStream protoOutputStream = proto;
        long j = memId;
        printProto(protoOutputStream, j, ADJ_MEM_PROTO_ENUMS, printProto(proto, screenId, ADJ_SCREEN_PROTO_ENUMS, state, 56), 14);
    }

    public static void printProcStateDurationProto(ProtoOutputStream proto, long fieldId, int procState, long duration) {
        long stateToken = proto.start(fieldId);
        printProto(proto, 1159641169923L, STATE_PROTO_ENUMS, procState, 1);
        proto.write(1112396529668L, duration);
        proto.end(stateToken);
    }

    public static void printProcStateTagAndValue(PrintWriter pw, int state, long value) {
        pw.print(',');
        printProcStateTag(pw, state);
        pw.print(':');
        pw.print(value);
    }

    public static void printAdjTagAndValue(PrintWriter pw, int state, long value) {
        pw.print(',');
        printAdjTag(pw, state);
        pw.print(':');
        pw.print(value);
    }

    public static long dumpSingleTime(PrintWriter pw, String prefix, long[] durations, int curState, long curStartTime, long now) {
        int i;
        PrintWriter printWriter = pw;
        long totalTime = 0;
        int printedScreen = -1;
        int iscreen = 0;
        while (iscreen < 8) {
            int printedMem = -1;
            for (int imem = 0; imem < 4; imem++) {
                int state = imem + iscreen;
                long time = durations[state];
                String running = "";
                if (curState == state) {
                    time += now - curStartTime;
                    if (printWriter != null) {
                        running = " (running)";
                    }
                }
                if (time != 0) {
                    if (printWriter != null) {
                        pw.print(prefix);
                        int i2 = -1;
                        printScreenLabel(printWriter, printedScreen != iscreen ? iscreen : -1);
                        printedScreen = iscreen;
                        if (printedMem != imem) {
                            i2 = imem;
                        }
                        printMemLabel(printWriter, i2, 0);
                        printedMem = imem;
                        printWriter.print(": ");
                        TimeUtils.formatDuration(time, printWriter);
                        printWriter.println(running);
                    }
                    totalTime += time;
                }
            }
            i = curState;
            iscreen += 4;
        }
        i = curState;
        if (!(totalTime == 0 || printWriter == null)) {
            pw.print(prefix);
            printWriter.print("    TOTAL: ");
            TimeUtils.formatDuration(totalTime, printWriter);
            pw.println();
        }
        return totalTime;
    }

    public static void dumpAdjTimesCheckin(PrintWriter pw, String sep, long[] durations, int curState, long curStartTime, long now) {
        for (int iscreen = 0; iscreen < 8; iscreen += 4) {
            for (int imem = 0; imem < 4; imem++) {
                int state = imem + iscreen;
                long time = durations[state];
                if (curState == state) {
                    time += now - curStartTime;
                }
                if (time != 0) {
                    printAdjTagAndValue(pw, state, time);
                }
            }
        }
    }

    private static void dumpStateHeadersCsv(PrintWriter pw, String sep, int[] screenStates, int[] memStates, int[] procStates) {
        int NS = screenStates != null ? screenStates.length : 1;
        int NM = memStates != null ? memStates.length : 1;
        int NP = procStates != null ? procStates.length : 1;
        for (int is = 0; is < NS; is++) {
            for (int im = 0; im < NM; im++) {
                for (int ip = 0; ip < NP; ip++) {
                    pw.print(sep);
                    boolean printed = false;
                    if (screenStates != null && screenStates.length > 1) {
                        printScreenLabelCsv(pw, screenStates[is]);
                        printed = true;
                    }
                    String str = "-";
                    if (memStates != null && memStates.length > 1) {
                        if (printed) {
                            pw.print(str);
                        }
                        printMemLabelCsv(pw, memStates[im]);
                        printed = true;
                    }
                    if (procStates != null && procStates.length > 1) {
                        if (printed) {
                            pw.print(str);
                        }
                        pw.print(STATE_NAMES_CSV[procStates[ip]]);
                    }
                }
            }
        }
    }

    public static void dumpProcessSummaryLocked(PrintWriter pw, String prefix, String header, ArrayList<ProcessState> procs, int[] screenStates, int[] memStates, int[] procStates, long now, long totalTime) {
        for (int i = procs.size() - 1; i >= 0; i--) {
            ((ProcessState) procs.get(i)).dumpSummary(pw, prefix, header, screenStates, memStates, procStates, now, totalTime);
        }
        ArrayList<ProcessState> arrayList = procs;
    }

    public static void dumpProcessListCsv(PrintWriter pw, ArrayList<ProcessState> procs, boolean sepScreenStates, int[] screenStates, boolean sepMemStates, int[] memStates, boolean sepProcStates, int[] procStates, long now) {
        PrintWriter printWriter = pw;
        pw.print(DumpHeapActivity.KEY_PROCESS);
        String str = CSV_SEP;
        pw.print(str);
        pw.print(GrantCredentialsPermissionActivity.EXTRAS_REQUESTING_UID);
        pw.print(str);
        pw.print("vers");
        int[] iArr = null;
        int[] iArr2 = sepScreenStates ? screenStates : null;
        int[] iArr3 = sepMemStates ? memStates : null;
        if (sepProcStates) {
            iArr = procStates;
        }
        dumpStateHeadersCsv(pw, str, iArr2, iArr3, iArr);
        pw.println();
        for (int i = procs.size() - 1; i >= 0; i--) {
            ProcessState proc = (ProcessState) procs.get(i);
            pw.print(proc.getName());
            pw.print(str);
            UserHandle.formatUid(pw, proc.getUid());
            pw.print(str);
            pw.print(proc.getVersion());
            proc.dumpCsv(pw, sepScreenStates, screenStates, sepMemStates, memStates, sepProcStates, procStates, now);
            pw.println();
        }
        ArrayList<ProcessState> arrayList = procs;
    }

    public static int printArrayEntry(PrintWriter pw, String[] array, int value, int mod) {
        int index = value / mod;
        if (index < 0 || index >= array.length) {
            pw.print('?');
        } else {
            pw.print(array[index]);
        }
        return value - (index * mod);
    }

    public static int printProto(ProtoOutputStream proto, long fieldId, int[] enums, int value, int mod) {
        int index = value / mod;
        if (index >= 0 && index < enums.length) {
            proto.write(fieldId, enums[index]);
        }
        return value - (index * mod);
    }

    public static String collapseString(String pkgName, String itemName) {
        if (itemName.startsWith(pkgName)) {
            int ITEMLEN = itemName.length();
            int PKGLEN = pkgName.length();
            if (ITEMLEN == PKGLEN) {
                return "";
            }
            if (ITEMLEN >= PKGLEN && itemName.charAt(PKGLEN) == ClassUtils.PACKAGE_SEPARATOR_CHAR) {
                return itemName.substring(PKGLEN);
            }
        }
        return itemName;
    }
}
