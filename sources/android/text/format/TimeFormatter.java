package android.text.format;

import android.content.res.Resources;
import com.android.internal.R;
import java.nio.CharBuffer;
import java.util.Formatter;
import java.util.Locale;
import libcore.icu.LocaleData;
import libcore.util.ZoneInfo;
import libcore.util.ZoneInfo.WallTime;

class TimeFormatter {
    private static final int DAYSPERLYEAR = 366;
    private static final int DAYSPERNYEAR = 365;
    private static final int DAYSPERWEEK = 7;
    private static final int FORCE_LOWER_CASE = -1;
    private static final int HOURSPERDAY = 24;
    private static final int MINSPERHOUR = 60;
    private static final int MONSPERYEAR = 12;
    private static final int SECSPERMIN = 60;
    private static String sDateOnlyFormat;
    private static String sDateTimeFormat;
    private static Locale sLocale;
    private static LocaleData sLocaleData;
    private static String sTimeOnlyFormat;
    private final String dateOnlyFormat;
    private final String dateTimeFormat;
    private final LocaleData localeData;
    private Formatter numberFormatter;
    private StringBuilder outputBuilder;
    private final String timeOnlyFormat;

    public TimeFormatter() {
        synchronized (TimeFormatter.class) {
            Locale locale = Locale.getDefault();
            if (sLocale == null || !locale.equals(sLocale)) {
                sLocale = locale;
                sLocaleData = LocaleData.get(locale);
                Resources r = Resources.getSystem();
                sTimeOnlyFormat = r.getString(R.string.time_of_day);
                sDateOnlyFormat = r.getString(R.string.month_day_year);
                sDateTimeFormat = r.getString(R.string.date_and_time);
            }
            this.dateTimeFormat = sDateTimeFormat;
            this.timeOnlyFormat = sTimeOnlyFormat;
            this.dateOnlyFormat = sDateOnlyFormat;
            this.localeData = sLocaleData;
        }
    }

    public String format(String pattern, WallTime wallTime, ZoneInfo zoneInfo) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            this.outputBuilder = stringBuilder;
            this.numberFormatter = new Formatter(stringBuilder, Locale.US);
            formatInternal(pattern, wallTime, zoneInfo);
            String result = stringBuilder.toString();
            if (this.localeData.zeroDigit != '0') {
                result = localizeDigits(result);
            }
            this.outputBuilder = null;
            this.numberFormatter = null;
            return result;
        } catch (Throwable th) {
            this.outputBuilder = null;
            this.numberFormatter = null;
        }
    }

    private String localizeDigits(String s) {
        int length = s.length();
        int offsetToLocalizedDigits = this.localeData.zeroDigit - 48;
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char ch = s.charAt(i);
            if (ch >= '0' && ch <= '9') {
                ch = (char) (ch + offsetToLocalizedDigits);
            }
            result.append(ch);
        }
        return result.toString();
    }

    private void formatInternal(String pattern, WallTime wallTime, ZoneInfo zoneInfo) {
        CharBuffer formatBuffer = CharBuffer.wrap(pattern);
        while (formatBuffer.remaining() > 0) {
            boolean outputCurrentChar = true;
            if (formatBuffer.get(formatBuffer.position()) == '%') {
                outputCurrentChar = handleToken(formatBuffer, wallTime, zoneInfo);
            }
            if (outputCurrentChar) {
                this.outputBuilder.append(formatBuffer.get(formatBuffer.position()));
            }
            formatBuffer.position(formatBuffer.position() + 1);
        }
    }

    /* JADX WARNING: Missing block: B:149:0x02fc, code skipped:
            if (r23.getMonth() < 0) goto L_0x0311;
     */
    /* JADX WARNING: Missing block: B:151:0x0302, code skipped:
            if (r23.getMonth() < 12) goto L_0x0305;
     */
    /* JADX WARNING: Missing block: B:152:0x0305, code skipped:
            r16 = r0.localeData.shortMonthNames[r23.getMonth()];
     */
    /* JADX WARNING: Missing block: B:153:0x0311, code skipped:
            modifyAndAppend(r16, r4);
     */
    /* JADX WARNING: Missing block: B:154:0x0316, code skipped:
            return false;
     */
    private boolean handleToken(java.nio.CharBuffer r22, libcore.util.ZoneInfo.WallTime r23, libcore.util.ZoneInfo r24) {
        /*
        r21 = this;
        r0 = r21;
        r1 = r22;
        r2 = r23;
        r3 = r24;
        r4 = 0;
    L_0x0009:
        r5 = r22.remaining();
        r6 = 1;
        if (r5 <= r6) goto L_0x03e5;
    L_0x0010:
        r5 = r22.position();
        r5 = r5 + r6;
        r1.position(r5);
        r5 = r22.position();
        r5 = r1.get(r5);
        r7 = 35;
        if (r5 == r7) goto L_0x03e2;
    L_0x0024:
        r7 = 43;
        r8 = 0;
        if (r5 == r7) goto L_0x03db;
    L_0x0029:
        r7 = 45;
        if (r5 == r7) goto L_0x03e2;
    L_0x002d:
        r9 = 48;
        if (r5 == r9) goto L_0x03e2;
    L_0x0031:
        r9 = 77;
        r10 = "%2d";
        r11 = "%d";
        r12 = "%02d";
        if (r5 == r9) goto L_0x03c3;
    L_0x003b:
        r9 = 112; // 0x70 float:1.57E-43 double:5.53E-322;
        r13 = 12;
        if (r5 == r9) goto L_0x03a9;
    L_0x0041:
        r9 = 79;
        if (r5 == r9) goto L_0x03a7;
    L_0x0045:
        r9 = 80;
        if (r5 == r9) goto L_0x038b;
    L_0x0049:
        r9 = 94;
        if (r5 == r9) goto L_0x03e2;
    L_0x004d:
        r9 = 95;
        if (r5 == r9) goto L_0x03e2;
    L_0x0051:
        r9 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        r14 = 7;
        if (r5 == r9) goto L_0x0317;
    L_0x0056:
        r15 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        r16 = "?";
        if (r5 == r15) goto L_0x02f8;
    L_0x005c:
        switch(r5) {
            case 65: goto L_0x02d8;
            case 66: goto L_0x0297;
            case 67: goto L_0x028f;
            case 68: goto L_0x0289;
            case 69: goto L_0x03a7;
            case 70: goto L_0x0283;
            case 71: goto L_0x0317;
            case 72: goto L_0x026d;
            case 73: goto L_0x024d;
            default: goto L_0x005f;
        };
    L_0x005f:
        switch(r5) {
            case 82: goto L_0x0247;
            case 83: goto L_0x0231;
            case 84: goto L_0x022b;
            case 85: goto L_0x020e;
            case 86: goto L_0x0317;
            case 87: goto L_0x01e8;
            case 88: goto L_0x01e2;
            case 89: goto L_0x01da;
            case 90: goto L_0x01c3;
            default: goto L_0x0062;
        };
    L_0x0062:
        switch(r5) {
            case 97: goto L_0x01a3;
            case 98: goto L_0x02f8;
            case 99: goto L_0x019d;
            case 100: goto L_0x0187;
            case 101: goto L_0x0171;
            default: goto L_0x0065;
        };
    L_0x0065:
        switch(r5) {
            case 106: goto L_0x0156;
            case 107: goto L_0x0140;
            case 108: goto L_0x0120;
            case 109: goto L_0x0109;
            case 110: goto L_0x0101;
            default: goto L_0x0068;
        };
    L_0x0068:
        switch(r5) {
            case 114: goto L_0x00fb;
            case 115: goto L_0x00ed;
            case 116: goto L_0x00e5;
            case 117: goto L_0x00cb;
            case 118: goto L_0x00c5;
            case 119: goto L_0x00b3;
            case 120: goto L_0x00ad;
            case 121: goto L_0x00a5;
            case 122: goto L_0x006c;
            default: goto L_0x006b;
        };
    L_0x006b:
        return r6;
    L_0x006c:
        r7 = r23.getIsDst();
        if (r7 >= 0) goto L_0x0073;
    L_0x0072:
        return r8;
    L_0x0073:
        r7 = r23.getGmtOffset();
        if (r7 >= 0) goto L_0x007d;
    L_0x0079:
        r9 = 45;
        r7 = -r7;
        goto L_0x007f;
    L_0x007d:
        r9 = 43;
    L_0x007f:
        r10 = r0.outputBuilder;
        r10.append(r9);
        r7 = r7 / 60;
        r10 = r7 / 60;
        r10 = r10 * 100;
        r12 = r7 % 60;
        r10 = r10 + r12;
        r7 = r0.numberFormatter;
        r12 = "%04d";
        r13 = "%4d";
        r14 = "%04d";
        r11 = getFormat(r4, r12, r13, r11, r14);
        r6 = new java.lang.Object[r6];
        r12 = java.lang.Integer.valueOf(r10);
        r6[r8] = r12;
        r7.format(r11, r6);
        return r8;
    L_0x00a5:
        r7 = r23.getYear();
        r0.outputYear(r7, r8, r6, r4);
        return r8;
    L_0x00ad:
        r6 = r0.dateOnlyFormat;
        r0.formatInternal(r6, r2, r3);
        return r8;
    L_0x00b3:
        r7 = r0.numberFormatter;
        r6 = new java.lang.Object[r6];
        r9 = r23.getWeekDay();
        r9 = java.lang.Integer.valueOf(r9);
        r6[r8] = r9;
        r7.format(r11, r6);
        return r8;
    L_0x00c5:
        r6 = "%e-%b-%Y";
        r0.formatInternal(r6, r2, r3);
        return r8;
    L_0x00cb:
        r7 = r23.getWeekDay();
        if (r7 != 0) goto L_0x00d2;
    L_0x00d1:
        goto L_0x00d6;
    L_0x00d2:
        r14 = r23.getWeekDay();
    L_0x00d6:
        r7 = r14;
        r9 = r0.numberFormatter;
        r6 = new java.lang.Object[r6];
        r10 = java.lang.Integer.valueOf(r7);
        r6[r8] = r10;
        r9.format(r11, r6);
        return r8;
    L_0x00e5:
        r6 = r0.outputBuilder;
        r7 = 9;
        r6.append(r7);
        return r8;
    L_0x00ed:
        r6 = r23.mktime(r24);
        r7 = r0.outputBuilder;
        r9 = java.lang.Integer.toString(r6);
        r7.append(r9);
        return r8;
    L_0x00fb:
        r6 = "%I:%M:%S %p";
        r0.formatInternal(r6, r2, r3);
        return r8;
    L_0x0101:
        r6 = r0.outputBuilder;
        r7 = 10;
        r6.append(r7);
        return r8;
    L_0x0109:
        r7 = r0.numberFormatter;
        r9 = getFormat(r4, r12, r10, r11, r12);
        r10 = new java.lang.Object[r6];
        r11 = r23.getMonth();
        r11 = r11 + r6;
        r6 = java.lang.Integer.valueOf(r11);
        r10[r8] = r6;
        r7.format(r9, r10);
        return r8;
    L_0x0120:
        r7 = r23.getHour();
        r7 = r7 % r13;
        if (r7 == 0) goto L_0x012d;
    L_0x0127:
        r7 = r23.getHour();
        r13 = r7 % 12;
    L_0x012d:
        r7 = r13;
        r9 = r0.numberFormatter;
        r10 = getFormat(r4, r10, r10, r11, r12);
        r6 = new java.lang.Object[r6];
        r11 = java.lang.Integer.valueOf(r7);
        r6[r8] = r11;
        r9.format(r10, r6);
        return r8;
    L_0x0140:
        r7 = r0.numberFormatter;
        r9 = getFormat(r4, r10, r10, r11, r12);
        r6 = new java.lang.Object[r6];
        r10 = r23.getHour();
        r10 = java.lang.Integer.valueOf(r10);
        r6[r8] = r10;
        r7.format(r9, r6);
        return r8;
    L_0x0156:
        r7 = r23.getYearDay();
        r7 = r7 + r6;
        r9 = r0.numberFormatter;
        r10 = "%03d";
        r12 = "%3d";
        r10 = getFormat(r4, r10, r12, r11, r10);
        r6 = new java.lang.Object[r6];
        r11 = java.lang.Integer.valueOf(r7);
        r6[r8] = r11;
        r9.format(r10, r6);
        return r8;
    L_0x0171:
        r7 = r0.numberFormatter;
        r9 = getFormat(r4, r10, r10, r11, r12);
        r6 = new java.lang.Object[r6];
        r10 = r23.getMonthDay();
        r10 = java.lang.Integer.valueOf(r10);
        r6[r8] = r10;
        r7.format(r9, r6);
        return r8;
    L_0x0187:
        r7 = r0.numberFormatter;
        r9 = getFormat(r4, r12, r10, r11, r12);
        r6 = new java.lang.Object[r6];
        r10 = r23.getMonthDay();
        r10 = java.lang.Integer.valueOf(r10);
        r6[r8] = r10;
        r7.format(r9, r6);
        return r8;
    L_0x019d:
        r6 = r0.dateTimeFormat;
        r0.formatInternal(r6, r2, r3);
        return r8;
    L_0x01a3:
        r7 = r23.getWeekDay();
        if (r7 < 0) goto L_0x01bc;
    L_0x01a9:
        r7 = r23.getWeekDay();
        if (r7 < r14) goto L_0x01b0;
    L_0x01af:
        goto L_0x01bc;
    L_0x01b0:
        r7 = r0.localeData;
        r7 = r7.shortWeekdayNames;
        r9 = r23.getWeekDay();
        r9 = r9 + r6;
        r16 = r7[r9];
        goto L_0x01bd;
    L_0x01bd:
        r6 = r16;
        r0.modifyAndAppend(r6, r4);
        return r8;
    L_0x01c3:
        r7 = r23.getIsDst();
        if (r7 >= 0) goto L_0x01ca;
    L_0x01c9:
        return r8;
    L_0x01ca:
        r7 = r23.getIsDst();
        if (r7 == 0) goto L_0x01d1;
    L_0x01d0:
        goto L_0x01d2;
    L_0x01d1:
        r6 = r8;
    L_0x01d2:
        r7 = r3.getDisplayName(r6, r8);
        r0.modifyAndAppend(r7, r4);
        return r8;
    L_0x01da:
        r7 = r23.getYear();
        r0.outputYear(r7, r6, r6, r4);
        return r8;
    L_0x01e2:
        r6 = r0.timeOnlyFormat;
        r0.formatInternal(r6, r2, r3);
        return r8;
    L_0x01e8:
        r7 = r23.getYearDay();
        r7 = r7 + r14;
        r9 = r23.getWeekDay();
        if (r9 == 0) goto L_0x01f9;
    L_0x01f3:
        r9 = r23.getWeekDay();
        r9 = r9 - r6;
        goto L_0x01fa;
    L_0x01f9:
        r9 = 6;
    L_0x01fa:
        r7 = r7 - r9;
        r7 = r7 / r14;
        r9 = r0.numberFormatter;
        r10 = getFormat(r4, r12, r10, r11, r12);
        r6 = new java.lang.Object[r6];
        r11 = java.lang.Integer.valueOf(r7);
        r6[r8] = r11;
        r9.format(r10, r6);
        return r8;
    L_0x020e:
        r7 = r0.numberFormatter;
        r9 = getFormat(r4, r12, r10, r11, r12);
        r6 = new java.lang.Object[r6];
        r10 = r23.getYearDay();
        r10 = r10 + r14;
        r11 = r23.getWeekDay();
        r10 = r10 - r11;
        r10 = r10 / r14;
        r10 = java.lang.Integer.valueOf(r10);
        r6[r8] = r10;
        r7.format(r9, r6);
        return r8;
    L_0x022b:
        r6 = "%H:%M:%S";
        r0.formatInternal(r6, r2, r3);
        return r8;
    L_0x0231:
        r7 = r0.numberFormatter;
        r9 = getFormat(r4, r12, r10, r11, r12);
        r6 = new java.lang.Object[r6];
        r10 = r23.getSecond();
        r10 = java.lang.Integer.valueOf(r10);
        r6[r8] = r10;
        r7.format(r9, r6);
        return r8;
    L_0x0247:
        r6 = "%H:%M";
        r0.formatInternal(r6, r2, r3);
        return r8;
    L_0x024d:
        r7 = r23.getHour();
        r7 = r7 % r13;
        if (r7 == 0) goto L_0x025a;
    L_0x0254:
        r7 = r23.getHour();
        r13 = r7 % 12;
    L_0x025a:
        r7 = r13;
        r9 = r0.numberFormatter;
        r10 = getFormat(r4, r12, r10, r11, r12);
        r6 = new java.lang.Object[r6];
        r11 = java.lang.Integer.valueOf(r7);
        r6[r8] = r11;
        r9.format(r10, r6);
        return r8;
    L_0x026d:
        r7 = r0.numberFormatter;
        r9 = getFormat(r4, r12, r10, r11, r12);
        r6 = new java.lang.Object[r6];
        r10 = r23.getHour();
        r10 = java.lang.Integer.valueOf(r10);
        r6[r8] = r10;
        r7.format(r9, r6);
        return r8;
    L_0x0283:
        r6 = "%Y-%m-%d";
        r0.formatInternal(r6, r2, r3);
        return r8;
    L_0x0289:
        r6 = "%m/%d/%y";
        r0.formatInternal(r6, r2, r3);
        return r8;
    L_0x028f:
        r7 = r23.getYear();
        r0.outputYear(r7, r6, r8, r4);
        return r8;
    L_0x0297:
        if (r4 != r7) goto L_0x02b9;
    L_0x0299:
        r6 = r23.getMonth();
        if (r6 < 0) goto L_0x02b3;
    L_0x029f:
        r6 = r23.getMonth();
        if (r6 < r13) goto L_0x02a6;
    L_0x02a5:
        goto L_0x02b3;
    L_0x02a6:
        r6 = r0.localeData;
        r6 = r6.longStandAloneMonthNames;
        r7 = r23.getMonth();
        r16 = r6[r7];
        r6 = r16;
        goto L_0x02b5;
    L_0x02b3:
        r6 = r16;
    L_0x02b5:
        r0.modifyAndAppend(r6, r4);
        goto L_0x02d7;
    L_0x02b9:
        r6 = r23.getMonth();
        if (r6 < 0) goto L_0x02d1;
    L_0x02bf:
        r6 = r23.getMonth();
        if (r6 < r13) goto L_0x02c6;
    L_0x02c5:
        goto L_0x02d1;
    L_0x02c6:
        r6 = r0.localeData;
        r6 = r6.longMonthNames;
        r7 = r23.getMonth();
        r16 = r6[r7];
        goto L_0x02d2;
    L_0x02d2:
        r6 = r16;
        r0.modifyAndAppend(r6, r4);
    L_0x02d7:
        return r8;
    L_0x02d8:
        r7 = r23.getWeekDay();
        if (r7 < 0) goto L_0x02f1;
    L_0x02de:
        r7 = r23.getWeekDay();
        if (r7 < r14) goto L_0x02e5;
    L_0x02e4:
        goto L_0x02f1;
    L_0x02e5:
        r7 = r0.localeData;
        r7 = r7.longWeekdayNames;
        r9 = r23.getWeekDay();
        r9 = r9 + r6;
        r16 = r7[r9];
        goto L_0x02f2;
    L_0x02f2:
        r6 = r16;
        r0.modifyAndAppend(r6, r4);
        return r8;
    L_0x02f8:
        r6 = r23.getMonth();
        if (r6 < 0) goto L_0x0310;
    L_0x02fe:
        r6 = r23.getMonth();
        if (r6 < r13) goto L_0x0305;
    L_0x0304:
        goto L_0x0310;
    L_0x0305:
        r6 = r0.localeData;
        r6 = r6.shortMonthNames;
        r7 = r23.getMonth();
        r16 = r6[r7];
        goto L_0x0311;
    L_0x0311:
        r6 = r16;
        r0.modifyAndAppend(r6, r4);
        return r8;
    L_0x0317:
        r7 = r23.getYear();
        r13 = r23.getYearDay();
        r15 = r23.getWeekDay();
    L_0x0323:
        r16 = isLeap(r7);
        r17 = 366; // 0x16e float:5.13E-43 double:1.81E-321;
        r18 = 365; // 0x16d float:5.11E-43 double:1.803E-321;
        if (r16 == 0) goto L_0x0330;
    L_0x032d:
        r16 = r17;
        goto L_0x0332;
    L_0x0330:
        r16 = r18;
    L_0x0332:
        r19 = r13 + 11;
        r19 = r19 - r15;
        r19 = r19 % 7;
        r9 = r19 + -3;
        r19 = r16 % 7;
        r8 = r9 - r19;
        r6 = -3;
        if (r8 >= r6) goto L_0x0343;
    L_0x0341:
        r8 = r8 + 7;
    L_0x0343:
        r8 = r8 + r16;
        if (r13 < r8) goto L_0x034c;
    L_0x0347:
        r7 = r7 + 1;
        r6 = 1;
        r14 = 1;
        goto L_0x0354;
    L_0x034c:
        if (r13 < r9) goto L_0x0378;
    L_0x034e:
        r6 = r13 - r9;
        r6 = r6 / r14;
        r14 = 1;
        r6 = r6 + r14;
    L_0x0354:
        r8 = 86;
        if (r5 != r8) goto L_0x036b;
    L_0x0358:
        r8 = r0.numberFormatter;
        r9 = getFormat(r4, r12, r10, r11, r12);
        r10 = new java.lang.Object[r14];
        r11 = java.lang.Integer.valueOf(r6);
        r12 = 0;
        r10[r12] = r11;
        r8.format(r9, r10);
        goto L_0x0377;
    L_0x036b:
        r12 = 0;
        r8 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        if (r5 != r8) goto L_0x0374;
    L_0x0370:
        r0.outputYear(r7, r12, r14, r4);
        goto L_0x0377;
    L_0x0374:
        r0.outputYear(r7, r14, r14, r4);
    L_0x0377:
        return r12;
    L_0x0378:
        r6 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        r7 = r7 + -1;
        r20 = isLeap(r7);
        if (r20 == 0) goto L_0x0383;
    L_0x0382:
        goto L_0x0385;
    L_0x0383:
        r17 = r18;
    L_0x0385:
        r13 = r13 + r17;
        r9 = r6;
        r6 = 1;
        r8 = 0;
        goto L_0x0323;
    L_0x038b:
        r6 = r23.getHour();
        if (r6 < r13) goto L_0x039a;
    L_0x0391:
        r6 = r0.localeData;
        r6 = r6.amPm;
        r7 = 1;
        r6 = r6[r7];
        r7 = 0;
        goto L_0x03a1;
    L_0x039a:
        r6 = r0.localeData;
        r6 = r6.amPm;
        r7 = 0;
        r6 = r6[r7];
        r8 = -1;
        r0.modifyAndAppend(r6, r8);
        return r7;
    L_0x03a7:
        goto L_0x0009;
    L_0x03a9:
        r6 = r23.getHour();
        if (r6 < r13) goto L_0x03b8;
    L_0x03af:
        r6 = r0.localeData;
        r6 = r6.amPm;
        r7 = 1;
        r6 = r6[r7];
        r7 = 0;
        goto L_0x03bf;
    L_0x03b8:
        r6 = r0.localeData;
        r6 = r6.amPm;
        r7 = 0;
        r6 = r6[r7];
    L_0x03bf:
        r0.modifyAndAppend(r6, r4);
        return r7;
    L_0x03c3:
        r7 = r8;
        r6 = r0.numberFormatter;
        r8 = getFormat(r4, r12, r10, r11, r12);
        r9 = 1;
        r9 = new java.lang.Object[r9];
        r10 = r23.getMinute();
        r10 = java.lang.Integer.valueOf(r10);
        r9[r7] = r10;
        r6.format(r8, r9);
        return r7;
    L_0x03db:
        r7 = r8;
        r6 = "%a %b %e %H:%M:%S %Z %Y";
        r0.formatInternal(r6, r2, r3);
        return r7;
    L_0x03e2:
        r4 = r5;
        goto L_0x0009;
    L_0x03e5:
        r5 = 1;
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.format.TimeFormatter.handleToken(java.nio.CharBuffer, libcore.util.ZoneInfo$WallTime, libcore.util.ZoneInfo):boolean");
    }

    private void modifyAndAppend(CharSequence str, int modifier) {
        int i;
        if (modifier == -1) {
            for (i = 0; i < str.length(); i++) {
                this.outputBuilder.append(brokenToLower(str.charAt(i)));
            }
        } else if (modifier == 35) {
            for (i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (brokenIsUpper(c)) {
                    c = brokenToLower(c);
                } else if (brokenIsLower(c)) {
                    c = brokenToUpper(c);
                }
                this.outputBuilder.append(c);
            }
        } else if (modifier != 94) {
            this.outputBuilder.append(str);
        } else {
            for (i = 0; i < str.length(); i++) {
                this.outputBuilder.append(brokenToUpper(str.charAt(i)));
            }
        }
    }

    private void outputYear(int value, boolean outputTop, boolean outputBottom, int modifier) {
        int i = modifier;
        int trail = value % 100;
        int lead = (value / 100) + (trail / 100);
        trail %= 100;
        if (trail < 0 && lead > 0) {
            trail += 100;
            lead--;
        } else if (lead < 0 && trail > 0) {
            trail -= 100;
            lead++;
        }
        String str = "%d";
        String str2 = "%2d";
        String str3 = "%02d";
        if (outputTop) {
            if (lead != 0 || trail >= 0) {
                this.numberFormatter.format(getFormat(i, str3, str2, str, str3), new Object[]{Integer.valueOf(lead)});
            } else {
                this.outputBuilder.append("-0");
            }
        }
        if (outputBottom) {
            int n = trail < 0 ? -trail : trail;
            this.numberFormatter.format(getFormat(i, str3, str2, str, str3), new Object[]{Integer.valueOf(n)});
        }
    }

    private static String getFormat(int modifier, String normal, String underscore, String dash, String zero) {
        if (modifier == 45) {
            return dash;
        }
        if (modifier == 48) {
            return zero;
        }
        if (modifier != 95) {
            return normal;
        }
        return underscore;
    }

    private static boolean isLeap(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    private static boolean brokenIsUpper(char toCheck) {
        return toCheck >= DateFormat.CAPITAL_AM_PM && toCheck <= 'Z';
    }

    private static boolean brokenIsLower(char toCheck) {
        return toCheck >= DateFormat.AM_PM && toCheck <= DateFormat.TIME_ZONE;
    }

    private static char brokenToLower(char input) {
        if (input < DateFormat.CAPITAL_AM_PM || input > 'Z') {
            return input;
        }
        return (char) ((input - 65) + 97);
    }

    private static char brokenToUpper(char input) {
        if (input < DateFormat.AM_PM || input > DateFormat.TIME_ZONE) {
            return input;
        }
        return (char) ((input - 97) + 65);
    }
}
