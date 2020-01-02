package android.sysprop;

import android.os.SystemProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

public final class SetupWizardProperties {
    private SetupWizardProperties() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x004c A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x004c A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x004c A:{SKIP} */
    private static java.lang.Boolean tryParseBoolean(java.lang.String r6) {
        /*
        r0 = java.util.Locale.US;
        r0 = r6.toLowerCase(r0);
        r1 = r0.hashCode();
        r2 = 48;
        r3 = 3;
        r4 = 2;
        r5 = 1;
        if (r1 == r2) goto L_0x003f;
    L_0x0011:
        r2 = 49;
        if (r1 == r2) goto L_0x0035;
    L_0x0015:
        r2 = 3569038; // 0x36758e float:5.001287E-39 double:1.763339E-317;
        if (r1 == r2) goto L_0x002a;
    L_0x001a:
        r2 = 97196323; // 0x5cb1923 float:1.9099262E-35 double:4.8021364E-316;
        if (r1 == r2) goto L_0x0020;
    L_0x001f:
        goto L_0x0049;
    L_0x0020:
        r1 = "false";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x001f;
    L_0x0028:
        r0 = r3;
        goto L_0x004a;
    L_0x002a:
        r1 = "true";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x001f;
    L_0x0033:
        r0 = r5;
        goto L_0x004a;
    L_0x0035:
        r1 = "1";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x001f;
    L_0x003d:
        r0 = 0;
        goto L_0x004a;
    L_0x003f:
        r1 = "0";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x001f;
    L_0x0047:
        r0 = r4;
        goto L_0x004a;
    L_0x0049:
        r0 = -1;
    L_0x004a:
        if (r0 == 0) goto L_0x0057;
    L_0x004c:
        if (r0 == r5) goto L_0x0057;
    L_0x004e:
        if (r0 == r4) goto L_0x0054;
    L_0x0050:
        if (r0 == r3) goto L_0x0054;
    L_0x0052:
        r0 = 0;
        return r0;
    L_0x0054:
        r0 = java.lang.Boolean.FALSE;
        return r0;
    L_0x0057:
        r0 = java.lang.Boolean.TRUE;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.sysprop.SetupWizardProperties.tryParseBoolean(java.lang.String):java.lang.Boolean");
    }

    private static Integer tryParseInteger(String str) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Long tryParseLong(String str) {
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double tryParseDouble(String str) {
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String tryParseString(String str) {
        return "".equals(str) ? null : str;
    }

    private static <T extends Enum<T>> T tryParseEnum(Class<T> enumType, String str) {
        try {
            return Enum.valueOf(enumType, str.toUpperCase(Locale.US));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static <T> List<T> tryParseList(Function<String, T> elementParser, String str) {
        if ("".equals(str)) {
            return new ArrayList();
        }
        List<T> ret = new ArrayList();
        for (String element : str.split(",")) {
            ret.add(elementParser.apply(element));
        }
        return ret;
    }

    private static <T extends Enum<T>> List<T> tryParseEnumList(Class<T> enumType, String str) {
        if ("".equals(str)) {
            return new ArrayList();
        }
        List<T> ret = new ArrayList();
        for (String element : str.split(",")) {
            ret.add(tryParseEnum(enumType, element));
        }
        return ret;
    }

    private static <T> String formatList(List<T> list) {
        StringJoiner joiner = new StringJoiner(",");
        for (T element : list) {
            joiner.add(element == null ? "" : element.toString());
        }
        return joiner.toString();
    }

    private static <T extends Enum<T>> String formatEnumList(List<T> list, Function<T, String> elementFormatter) {
        StringJoiner joiner = new StringJoiner(",");
        for (T element : list) {
            joiner.add(element == null ? "" : (CharSequence) elementFormatter.apply(element));
        }
        return joiner.toString();
    }

    public static Optional<String> theme() {
        return Optional.ofNullable(tryParseString(SystemProperties.get("setupwizard.theme")));
    }

    public static void theme(String value) {
        SystemProperties.set("setupwizard.theme", value == null ? "" : value.toString());
    }

    public static List<String> esim_cid_ignore() {
        return tryParseList(-$$Lambda$SetupWizardProperties$0Kz3pPj1bN4LsGiOMvxPbgDHtMI.INSTANCE, SystemProperties.get("ro.setupwizard.esim_cid_ignore"));
    }
}
