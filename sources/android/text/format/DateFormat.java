package android.text.format;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.provider.Settings.System;
import android.text.SpannableStringBuilder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import libcore.icu.ICU;
import libcore.icu.LocaleData;

public class DateFormat {
    @Deprecated
    public static final char AM_PM = 'a';
    @Deprecated
    public static final char CAPITAL_AM_PM = 'A';
    @Deprecated
    public static final char DATE = 'd';
    @Deprecated
    public static final char DAY = 'E';
    @Deprecated
    public static final char HOUR = 'h';
    @Deprecated
    public static final char HOUR_OF_DAY = 'k';
    @Deprecated
    public static final char MINUTE = 'm';
    @Deprecated
    public static final char MONTH = 'M';
    @Deprecated
    public static final char QUOTE = '\'';
    @Deprecated
    public static final char SECONDS = 's';
    @Deprecated
    public static final char STANDALONE_MONTH = 'L';
    @Deprecated
    public static final char TIME_ZONE = 'z';
    @Deprecated
    public static final char YEAR = 'y';
    private static boolean sIs24Hour;
    private static Locale sIs24HourLocale;
    private static final Object sLocaleLock = new Object();

    public static boolean is24HourFormat(Context context) {
        return is24HourFormat(context, context.getUserId());
    }

    @UnsupportedAppUsage
    public static boolean is24HourFormat(Context context, int userHandle) {
        String value = System.getStringForUser(context.getContentResolver(), System.TIME_12_24, userHandle);
        if (value != null) {
            return value.equals("24");
        }
        return is24HourLocale(context.getResources().getConfiguration().locale);
    }

    /* JADX WARNING: Missing block: B:11:0x0014, code skipped:
            r1 = java.text.DateFormat.getTimeInstance(1, r4);
     */
    /* JADX WARNING: Missing block: B:12:0x001b, code skipped:
            if ((r1 instanceof java.text.SimpleDateFormat) == false) goto L_0x002c;
     */
    /* JADX WARNING: Missing block: B:13:0x001d, code skipped:
            r2 = hasDesignator(((java.text.SimpleDateFormat) r1).toPattern(), 'H');
     */
    /* JADX WARNING: Missing block: B:14:0x002c, code skipped:
            r2 = false;
     */
    /* JADX WARNING: Missing block: B:15:0x002e, code skipped:
            r3 = sLocaleLock;
     */
    /* JADX WARNING: Missing block: B:16:0x0030, code skipped:
            monitor-enter(r3);
     */
    /* JADX WARNING: Missing block: B:18:?, code skipped:
            sIs24HourLocale = r4;
            sIs24Hour = r2;
     */
    /* JADX WARNING: Missing block: B:19:0x0035, code skipped:
            monitor-exit(r3);
     */
    /* JADX WARNING: Missing block: B:20:0x0036, code skipped:
            return r2;
     */
    public static boolean is24HourLocale(java.util.Locale r4) {
        /*
        r0 = sLocaleLock;
        monitor-enter(r0);
        r1 = sIs24HourLocale;	 Catch:{ all -> 0x003a }
        if (r1 == 0) goto L_0x0013;
    L_0x0007:
        r1 = sIs24HourLocale;	 Catch:{ all -> 0x003a }
        r1 = r1.equals(r4);	 Catch:{ all -> 0x003a }
        if (r1 == 0) goto L_0x0013;
    L_0x000f:
        r1 = sIs24Hour;	 Catch:{ all -> 0x003a }
        monitor-exit(r0);	 Catch:{ all -> 0x003a }
        return r1;
    L_0x0013:
        monitor-exit(r0);	 Catch:{ all -> 0x003a }
        r0 = 1;
        r1 = java.text.DateFormat.getTimeInstance(r0, r4);
        r0 = r1 instanceof java.text.SimpleDateFormat;
        if (r0 == 0) goto L_0x002c;
    L_0x001d:
        r0 = r1;
        r0 = (java.text.SimpleDateFormat) r0;
        r2 = r0.toPattern();
        r3 = 72;
        r0 = hasDesignator(r2, r3);
        r2 = r0;
        goto L_0x002e;
    L_0x002c:
        r0 = 0;
        r2 = r0;
    L_0x002e:
        r3 = sLocaleLock;
        monitor-enter(r3);
        sIs24HourLocale = r4;	 Catch:{ all -> 0x0037 }
        sIs24Hour = r2;	 Catch:{ all -> 0x0037 }
        monitor-exit(r3);	 Catch:{ all -> 0x0037 }
        return r2;
    L_0x0037:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0037 }
        throw r0;
    L_0x003a:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x003a }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.format.DateFormat.is24HourLocale(java.util.Locale):boolean");
    }

    public static String getBestDateTimePattern(Locale locale, String skeleton) {
        return ICU.getBestDateTimePattern(skeleton, locale);
    }

    public static java.text.DateFormat getTimeFormat(Context context) {
        return new SimpleDateFormat(getTimeFormatString(context), context.getResources().getConfiguration().locale);
    }

    @UnsupportedAppUsage
    public static String getTimeFormatString(Context context) {
        return getTimeFormatString(context, context.getUserId());
    }

    @UnsupportedAppUsage
    public static String getTimeFormatString(Context context, int userHandle) {
        LocaleData d = LocaleData.get(context.getResources().getConfiguration().locale);
        return is24HourFormat(context, userHandle) ? d.timeFormat_Hm : d.timeFormat_hm;
    }

    public static java.text.DateFormat getDateFormat(Context context) {
        return java.text.DateFormat.getDateInstance(3, context.getResources().getConfiguration().locale);
    }

    public static java.text.DateFormat getLongDateFormat(Context context) {
        return java.text.DateFormat.getDateInstance(1, context.getResources().getConfiguration().locale);
    }

    public static java.text.DateFormat getMediumDateFormat(Context context) {
        return java.text.DateFormat.getDateInstance(2, context.getResources().getConfiguration().locale);
    }

    public static char[] getDateFormatOrder(Context context) {
        return ICU.getDateFormatOrder(getDateFormatString(context));
    }

    private static String getDateFormatString(Context context) {
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(3, context.getResources().getConfiguration().locale);
        if (df instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) df).toPattern();
        }
        throw new AssertionError("!(df instanceof SimpleDateFormat)");
    }

    public static CharSequence format(CharSequence inFormat, long inTimeInMillis) {
        return format(inFormat, new Date(inTimeInMillis));
    }

    public static CharSequence format(CharSequence inFormat, Date inDate) {
        Calendar c = new GregorianCalendar();
        c.setTime(inDate);
        return format(inFormat, c);
    }

    @UnsupportedAppUsage
    public static boolean hasSeconds(CharSequence inFormat) {
        return hasDesignator(inFormat, 's');
    }

    @UnsupportedAppUsage
    public static boolean hasDesignator(CharSequence inFormat, char designator) {
        if (inFormat == null) {
            return false;
        }
        int length = inFormat.length();
        boolean insideQuote = false;
        for (int i = 0; i < length; i++) {
            char c = inFormat.charAt(i);
            boolean z = true;
            if (c == QUOTE) {
                if (insideQuote) {
                    z = false;
                }
                insideQuote = z;
            } else if (!insideQuote && c == designator) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:62:0x00e7 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00e7 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00e7 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00e7 A:{SYNTHETIC} */
    public static java.lang.CharSequence format(java.lang.CharSequence r8, java.util.Calendar r9) {
        /*
        r0 = new android.text.SpannableStringBuilder;
        r0.<init>(r8);
        r1 = java.util.Locale.getDefault();
        r1 = libcore.icu.LocaleData.get(r1);
        r2 = r8.length();
        r3 = 0;
    L_0x0012:
        if (r3 >= r2) goto L_0x00ea;
    L_0x0014:
        r4 = 1;
        r5 = r0.charAt(r3);
        r6 = 39;
        if (r5 != r6) goto L_0x0027;
    L_0x001d:
        r4 = appendQuotedText(r0, r3);
        r2 = r0.length();
        goto L_0x00e7;
    L_0x0027:
        r6 = r3 + r4;
        if (r6 >= r2) goto L_0x0036;
    L_0x002b:
        r6 = r3 + r4;
        r6 = r0.charAt(r6);
        if (r6 != r5) goto L_0x0036;
    L_0x0033:
        r4 = r4 + 1;
        goto L_0x0027;
    L_0x0036:
        r6 = 65;
        if (r5 == r6) goto L_0x00cb;
    L_0x003a:
        r6 = 69;
        if (r5 == r6) goto L_0x00c1;
    L_0x003e:
        r6 = 72;
        if (r5 == r6) goto L_0x00b6;
    L_0x0042:
        r6 = 97;
        if (r5 == r6) goto L_0x00cb;
    L_0x0046:
        r6 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        if (r5 == r6) goto L_0x00a5;
    L_0x004a:
        r7 = 107; // 0x6b float:1.5E-43 double:5.3E-322;
        if (r5 == r7) goto L_0x00b6;
    L_0x004e:
        r7 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        if (r5 == r7) goto L_0x009a;
    L_0x0052:
        r7 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        if (r5 == r7) goto L_0x008f;
    L_0x0056:
        r7 = 99;
        if (r5 == r7) goto L_0x00c1;
    L_0x005a:
        r7 = 100;
        if (r5 == r7) goto L_0x0085;
    L_0x005e:
        r7 = 121; // 0x79 float:1.7E-43 double:6.0E-322;
        if (r5 == r7) goto L_0x007b;
    L_0x0062:
        r7 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        if (r5 == r7) goto L_0x0076;
    L_0x0066:
        switch(r5) {
            case 75: goto L_0x00a5;
            case 76: goto L_0x006c;
            case 77: goto L_0x006c;
            default: goto L_0x0069;
        };
    L_0x0069:
        r6 = 0;
        goto L_0x00d8;
    L_0x006c:
        r6 = 2;
        r6 = r9.get(r6);
        r6 = getMonthString(r1, r6, r4, r5);
        goto L_0x00d8;
    L_0x0076:
        r6 = getTimeZoneString(r9, r4);
        goto L_0x00d8;
    L_0x007b:
        r6 = 1;
        r6 = r9.get(r6);
        r6 = getYearString(r6, r4);
        goto L_0x00d8;
    L_0x0085:
        r6 = 5;
        r6 = r9.get(r6);
        r6 = zeroPad(r6, r4);
        goto L_0x00d8;
    L_0x008f:
        r6 = 13;
        r6 = r9.get(r6);
        r6 = zeroPad(r6, r4);
        goto L_0x00d8;
    L_0x009a:
        r6 = 12;
        r6 = r9.get(r6);
        r6 = zeroPad(r6, r4);
        goto L_0x00d8;
    L_0x00a5:
        r7 = 10;
        r7 = r9.get(r7);
        if (r5 != r6) goto L_0x00b1;
    L_0x00ad:
        if (r7 != 0) goto L_0x00b1;
    L_0x00af:
        r7 = 12;
    L_0x00b1:
        r6 = zeroPad(r7, r4);
        goto L_0x00d8;
    L_0x00b6:
        r6 = 11;
        r6 = r9.get(r6);
        r6 = zeroPad(r6, r4);
        goto L_0x00d8;
    L_0x00c1:
        r6 = 7;
        r6 = r9.get(r6);
        r6 = getDayOfWeekString(r1, r6, r4, r5);
        goto L_0x00d8;
    L_0x00cb:
        r6 = r1.amPm;
        r7 = 9;
        r7 = r9.get(r7);
        r7 = r7 + 0;
        r6 = r6[r7];
    L_0x00d8:
        if (r6 == 0) goto L_0x00e7;
    L_0x00da:
        r7 = r3 + r4;
        r0.replace(r3, r7, r6);
        r4 = r6.length();
        r2 = r0.length();
    L_0x00e7:
        r3 = r3 + r4;
        goto L_0x0012;
    L_0x00ea:
        r3 = r8 instanceof android.text.Spanned;
        if (r3 == 0) goto L_0x00f4;
    L_0x00ee:
        r3 = new android.text.SpannedString;
        r3.<init>(r0);
        return r3;
    L_0x00f4:
        r3 = r0.toString();
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.format.DateFormat.format(java.lang.CharSequence, java.util.Calendar):java.lang.CharSequence");
    }

    private static String getDayOfWeekString(LocaleData ld, int day, int count, int kind) {
        boolean standalone = kind == 99;
        if (count == 5) {
            return standalone ? ld.tinyStandAloneWeekdayNames[day] : ld.tinyWeekdayNames[day];
        } else if (count == 4) {
            return standalone ? ld.longStandAloneWeekdayNames[day] : ld.longWeekdayNames[day];
        } else {
            return standalone ? ld.shortStandAloneWeekdayNames[day] : ld.shortWeekdayNames[day];
        }
    }

    private static String getMonthString(LocaleData ld, int month, int count, int kind) {
        boolean standalone = kind == 76;
        if (count == 5) {
            return standalone ? ld.tinyStandAloneMonthNames[month] : ld.tinyMonthNames[month];
        } else if (count == 4) {
            return standalone ? ld.longStandAloneMonthNames[month] : ld.longMonthNames[month];
        } else if (count != 3) {
            return zeroPad(month + 1, count);
        } else {
            return standalone ? ld.shortStandAloneMonthNames[month] : ld.shortMonthNames[month];
        }
    }

    private static String getTimeZoneString(Calendar inDate, int count) {
        TimeZone tz = inDate.getTimeZone();
        if (count < 2) {
            return formatZoneOffset(inDate.get(16) + inDate.get(15), count);
        }
        return tz.getDisplayName(inDate.get(16) != 0, 0);
    }

    private static String formatZoneOffset(int offset, int count) {
        offset /= 1000;
        StringBuilder tb = new StringBuilder();
        if (offset < 0) {
            tb.insert(0, "-");
            offset = -offset;
        } else {
            tb.insert(0, "+");
        }
        int minutes = (offset % 3600) / 60;
        tb.append(zeroPad(offset / 3600, 2));
        tb.append(zeroPad(minutes, 2));
        return tb.toString();
    }

    private static String getYearString(int year, int count) {
        if (count <= 2) {
            return zeroPad(year % 100, 2);
        }
        return String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf(year)});
    }

    public static int appendQuotedText(SpannableStringBuilder formatString, int index) {
        int length = formatString.length();
        if (index + 1 >= length || formatString.charAt(index + 1) != QUOTE) {
            int count = 0;
            formatString.delete(index, index + 1);
            length--;
            while (index < length) {
                if (formatString.charAt(index) != QUOTE) {
                    index++;
                    count++;
                } else if (index + 1 >= length || formatString.charAt(index + 1) != QUOTE) {
                    formatString.delete(index, index + 1);
                    break;
                } else {
                    formatString.delete(index, index + 1);
                    length--;
                    count++;
                    index++;
                }
            }
            return count;
        }
        formatString.delete(index, index + 1);
        return 1;
    }

    private static String zeroPad(int inValue, int inMinDigits) {
        Locale locale = Locale.getDefault();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("%0");
        stringBuilder.append(inMinDigits);
        stringBuilder.append("d");
        return String.format(locale, stringBuilder.toString(), new Object[]{Integer.valueOf(inValue)});
    }
}
