package android.util;

import android.annotation.UnsupportedAppUsage;
import android.os.SystemClock;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import libcore.timezone.CountryTimeZones;
import libcore.timezone.CountryTimeZones.TimeZoneMapping;
import libcore.timezone.TimeZoneFinder;
import libcore.timezone.ZoneInfoDB;

public class TimeUtils {
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    public static final long NANOS_PER_MS = 1000000;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    public static final SimpleDateFormat sDumpDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static char[] sFormatStr = new char[29];
    private static final Object sFormatSync = new Object();
    private static SimpleDateFormat sLoggingFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static char[] sTmpFormatStr = new char[29];

    public static TimeZone getTimeZone(int offset, boolean dst, long when, String country) {
        android.icu.util.TimeZone icuTimeZone = getIcuTimeZone(offset, dst, when, country);
        return icuTimeZone != null ? TimeZone.getTimeZone(icuTimeZone.getID()) : null;
    }

    private static android.icu.util.TimeZone getIcuTimeZone(int offset, boolean dst, long when, String country) {
        if (country == null) {
            return null;
        }
        return TimeZoneFinder.getInstance().lookupTimeZoneByCountryAndOffset(country, offset, dst, when, android.icu.util.TimeZone.getDefault());
    }

    public static List<String> getTimeZoneIdsForCountryCode(String countryCode) {
        if (countryCode != null) {
            CountryTimeZones countryTimeZones = TimeZoneFinder.getInstance().lookupCountryTimeZones(countryCode.toLowerCase());
            if (countryTimeZones == null) {
                return null;
            }
            List<String> timeZoneIds = new ArrayList();
            for (TimeZoneMapping timeZoneMapping : countryTimeZones.getTimeZoneMappings()) {
                if (timeZoneMapping.showInPicker) {
                    timeZoneIds.add(timeZoneMapping.timeZoneId);
                }
            }
            return Collections.unmodifiableList(timeZoneIds);
        }
        throw new NullPointerException("countryCode == null");
    }

    public static String getTimeZoneDatabaseVersion() {
        return ZoneInfoDB.getInstance().getVersion();
    }

    private static int accumField(int amt, int suffix, boolean always, int zeropad) {
        if (amt > 999) {
            int num = 0;
            while (amt != 0) {
                num++;
                amt /= 10;
            }
            return num + suffix;
        } else if (amt > 99 || (always && zeropad >= 3)) {
            return suffix + 3;
        } else {
            if (amt > 9 || (always && zeropad >= 2)) {
                return suffix + 2;
            }
            if (always || amt > 0) {
                return suffix + 1;
            }
            return 0;
        }
    }

    private static int printFieldLocked(char[] formatStr, int amt, char suffix, int pos, boolean always, int zeropad) {
        if (!always && amt <= 0) {
            return pos;
        }
        int startPos = pos;
        int tmp;
        if (amt > 999) {
            tmp = 0;
            while (amt != 0) {
                char[] cArr = sTmpFormatStr;
                if (tmp >= cArr.length) {
                    break;
                }
                cArr[tmp] = (char) ((amt % 10) + 48);
                tmp++;
                amt /= 10;
            }
            for (tmp--; tmp >= 0; tmp--) {
                formatStr[pos] = sTmpFormatStr[tmp];
                pos++;
            }
        } else {
            if ((always && zeropad >= 3) || amt > 99) {
                tmp = amt / 100;
                formatStr[pos] = (char) (tmp + 48);
                pos++;
                amt -= tmp * 100;
            }
            if ((always && zeropad >= 2) || amt > 9 || startPos != pos) {
                tmp = amt / 10;
                formatStr[pos] = (char) (tmp + 48);
                pos++;
                amt -= tmp * 10;
            }
            formatStr[pos] = (char) (amt + 48);
            pos++;
        }
        formatStr[pos] = suffix;
        return pos + 1;
    }

    /* JADX WARNING: Missing block: B:72:0x0136, code skipped:
            if (r9 != r7) goto L_0x013d;
     */
    private static int formatDurationLocked(long r27, int r29) {
        /*
        r0 = r27;
        r2 = r29;
        r3 = sFormatStr;
        r3 = r3.length;
        if (r3 >= r2) goto L_0x000d;
    L_0x0009:
        r3 = new char[r2];
        sFormatStr = r3;
    L_0x000d:
        r3 = sFormatStr;
        r4 = 0;
        r6 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        r7 = 32;
        if (r6 != 0) goto L_0x0029;
    L_0x0017:
        r4 = 0;
        r2 = r2 + -1;
    L_0x001a:
        if (r4 >= r2) goto L_0x0022;
    L_0x001c:
        r5 = r4 + 1;
        r3[r4] = r7;
        r4 = r5;
        goto L_0x001a;
    L_0x0022:
        r5 = 48;
        r3[r4] = r5;
        r5 = r4 + 1;
        return r5;
    L_0x0029:
        r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r4 <= 0) goto L_0x0031;
    L_0x002d:
        r4 = 43;
        r10 = r4;
        goto L_0x0035;
    L_0x0031:
        r4 = 45;
        r0 = -r0;
        r10 = r4;
    L_0x0035:
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r8 = r0 % r4;
        r11 = (int) r8;
        r4 = r0 / r4;
        r4 = (double) r4;
        r4 = java.lang.Math.floor(r4);
        r4 = (int) r4;
        r5 = 0;
        r6 = 0;
        r8 = 0;
        r9 = 86400; // 0x15180 float:1.21072E-40 double:4.26873E-319;
        if (r4 < r9) goto L_0x0050;
    L_0x004a:
        r5 = r4 / r9;
        r9 = r9 * r5;
        r4 = r4 - r9;
        r12 = r5;
        goto L_0x0051;
    L_0x0050:
        r12 = r5;
    L_0x0051:
        r5 = 3600; // 0xe10 float:5.045E-42 double:1.7786E-320;
        if (r4 < r5) goto L_0x005c;
    L_0x0055:
        r5 = r4 / 3600;
        r6 = r5 * 3600;
        r4 = r4 - r6;
        r13 = r5;
        goto L_0x005d;
    L_0x005c:
        r13 = r6;
    L_0x005d:
        r5 = 60;
        if (r4 < r5) goto L_0x0069;
    L_0x0061:
        r5 = r4 / 60;
        r6 = r5 * 60;
        r4 = r4 - r6;
        r15 = r4;
        r14 = r5;
        goto L_0x006b;
    L_0x0069:
        r15 = r4;
        r14 = r8;
    L_0x006b:
        r4 = 0;
        r16 = 3;
        r9 = 2;
        r8 = 0;
        r6 = 1;
        if (r2 == 0) goto L_0x00a8;
    L_0x0073:
        r5 = accumField(r12, r6, r8, r8);
        if (r5 <= 0) goto L_0x007a;
    L_0x0079:
        r8 = r6;
    L_0x007a:
        r8 = accumField(r13, r6, r8, r9);
        r5 = r5 + r8;
        if (r5 <= 0) goto L_0x0083;
    L_0x0081:
        r8 = r6;
        goto L_0x0084;
    L_0x0083:
        r8 = 0;
    L_0x0084:
        r8 = accumField(r14, r6, r8, r9);
        r5 = r5 + r8;
        if (r5 <= 0) goto L_0x008d;
    L_0x008b:
        r8 = r6;
        goto L_0x008e;
    L_0x008d:
        r8 = 0;
    L_0x008e:
        r8 = accumField(r15, r6, r8, r9);
        r5 = r5 + r8;
        if (r5 <= 0) goto L_0x0098;
    L_0x0095:
        r8 = r16;
        goto L_0x0099;
    L_0x0098:
        r8 = 0;
    L_0x0099:
        r8 = accumField(r11, r9, r6, r8);
        r8 = r8 + r6;
        r5 = r5 + r8;
    L_0x009f:
        if (r5 >= r2) goto L_0x00a8;
    L_0x00a1:
        r3[r4] = r7;
        r4 = r4 + 1;
        r5 = r5 + 1;
        goto L_0x009f;
    L_0x00a8:
        r3[r4] = r10;
        r17 = r4 + 1;
        r8 = r17;
        if (r2 == 0) goto L_0x00b2;
    L_0x00b0:
        r4 = r6;
        goto L_0x00b3;
    L_0x00b2:
        r4 = 0;
    L_0x00b3:
        r18 = r4;
        r7 = 100;
        r19 = 0;
        r20 = 0;
        r4 = r3;
        r5 = r12;
        r21 = r6;
        r6 = r7;
        r7 = r17;
        r22 = r8;
        r23 = 0;
        r8 = r19;
        r19 = r9;
        r9 = r20;
        r9 = printFieldLocked(r4, r5, r6, r7, r8, r9);
        r6 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        r8 = r22;
        if (r9 == r8) goto L_0x00d9;
    L_0x00d6:
        r17 = r21;
        goto L_0x00db;
    L_0x00d9:
        r17 = r23;
    L_0x00db:
        if (r18 == 0) goto L_0x00e0;
    L_0x00dd:
        r20 = r19;
        goto L_0x00e2;
    L_0x00e0:
        r20 = r23;
    L_0x00e2:
        r4 = r3;
        r5 = r13;
        r7 = r9;
        r24 = r8;
        r8 = r17;
        r17 = r9;
        r9 = r20;
        r9 = printFieldLocked(r4, r5, r6, r7, r8, r9);
        r6 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        r8 = r24;
        if (r9 == r8) goto L_0x00fa;
    L_0x00f7:
        r17 = r21;
        goto L_0x00fc;
    L_0x00fa:
        r17 = r23;
    L_0x00fc:
        if (r18 == 0) goto L_0x0101;
    L_0x00fe:
        r20 = r19;
        goto L_0x0103;
    L_0x0101:
        r20 = r23;
    L_0x0103:
        r4 = r3;
        r5 = r14;
        r7 = r9;
        r25 = r8;
        r8 = r17;
        r17 = r9;
        r9 = r20;
        r9 = printFieldLocked(r4, r5, r6, r7, r8, r9);
        r6 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        r8 = r25;
        if (r9 == r8) goto L_0x0119;
    L_0x0118:
        goto L_0x011b;
    L_0x0119:
        r21 = r23;
    L_0x011b:
        if (r18 == 0) goto L_0x011e;
    L_0x011d:
        goto L_0x0120;
    L_0x011e:
        r19 = r23;
    L_0x0120:
        r4 = r3;
        r5 = r15;
        r7 = r9;
        r26 = r8;
        r8 = r21;
        r17 = r9;
        r9 = r19;
        r9 = printFieldLocked(r4, r5, r6, r7, r8, r9);
        r6 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        r8 = 1;
        if (r18 == 0) goto L_0x0139;
    L_0x0134:
        r7 = r26;
        if (r9 == r7) goto L_0x013b;
    L_0x0138:
        goto L_0x013d;
    L_0x0139:
        r7 = r26;
    L_0x013b:
        r16 = r23;
    L_0x013d:
        r4 = r3;
        r5 = r11;
        r17 = r7;
        r7 = r9;
        r19 = r9;
        r9 = r16;
        r4 = printFieldLocked(r4, r5, r6, r7, r8, r9);
        r5 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        r3[r4] = r5;
        r5 = r4 + 1;
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.TimeUtils.formatDurationLocked(long, int):int");
    }

    public static void formatDuration(long duration, StringBuilder builder) {
        synchronized (sFormatSync) {
            builder.append(sFormatStr, 0, formatDurationLocked(duration, 0));
        }
    }

    public static void formatDuration(long duration, StringBuilder builder, int fieldLen) {
        synchronized (sFormatSync) {
            builder.append(sFormatStr, 0, formatDurationLocked(duration, fieldLen));
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static void formatDuration(long duration, PrintWriter pw, int fieldLen) {
        synchronized (sFormatSync) {
            pw.print(new String(sFormatStr, 0, formatDurationLocked(duration, fieldLen)));
        }
    }

    public static String formatDuration(long duration) {
        String str;
        synchronized (sFormatSync) {
            str = new String(sFormatStr, 0, formatDurationLocked(duration, 0));
        }
        return str;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static void formatDuration(long duration, PrintWriter pw) {
        formatDuration(duration, pw, 0);
    }

    public static void formatDuration(long time, long now, PrintWriter pw) {
        if (time == 0) {
            pw.print("--");
        } else {
            formatDuration(time - now, pw, 0);
        }
    }

    public static String formatUptime(long time) {
        long diff = time - SystemClock.uptimeMillis();
        StringBuilder stringBuilder;
        if (diff > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(time);
            stringBuilder.append(" (in ");
            stringBuilder.append(diff);
            stringBuilder.append(" ms)");
            return stringBuilder.toString();
        } else if (diff < 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(time);
            stringBuilder.append(" (");
            stringBuilder.append(-diff);
            stringBuilder.append(" ms ago)");
            return stringBuilder.toString();
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(time);
            stringBuilder.append(" (now)");
            return stringBuilder.toString();
        }
    }

    @UnsupportedAppUsage
    public static String logTimeOfDay(long millis) {
        Calendar c = Calendar.getInstance();
        if (millis < 0) {
            return Long.toString(millis);
        }
        c.setTimeInMillis(millis);
        return String.format("%tm-%td %tH:%tM:%tS.%tL", new Object[]{c, c, c, c, c, c});
    }

    public static String formatForLogging(long millis) {
        if (millis <= 0) {
            return "unknown";
        }
        return sLoggingFormat.format(new Date(millis));
    }

    public static void dumpTime(PrintWriter pw, long time) {
        pw.print(sDumpDateFormat.format(new Date(time)));
    }

    public static void dumpTimeWithDelta(PrintWriter pw, long time, long now) {
        pw.print(sDumpDateFormat.format(new Date(time)));
        if (time == now) {
            pw.print(" (now)");
            return;
        }
        pw.print(" (");
        formatDuration(time, now, pw);
        pw.print(")");
    }
}
