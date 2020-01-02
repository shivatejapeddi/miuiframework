package android.telephony;

import android.os.SystemProperties;
import android.text.TextUtils;
import com.android.internal.telephony.TelephonyProperties;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.TelephonyManagerEx;

class PhoneNumberUtilsInjector {
    PhoneNumberUtilsInjector() {
    }

    static void appendNonSeparator(StringBuilder sb, char c, int pos) {
        if (!(pos == 0 && c == '+') && Character.digit(c, 10) == -1 && PhoneNumberUtils.isNonSeparator(c)) {
            sb.append(c);
        }
    }

    public static boolean matchBrazilSuccess(String a, int alen, String b, int blen) {
        if (containsCountryCode(SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY), TtmlUtils.TAG_BR) && matchBrazilCarrierCodeAndPrefix(a, alen) && matchBrazilCarrierCodeAndPrefix(b, blen)) {
            return true;
        }
        return false;
    }

    public static String removeBrazilCarrierCodeAndPrefix(String a) {
        if (containsCountryCode(SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY), TtmlUtils.TAG_BR)) {
            if (a.length() > 3 && matchBrazilCarrierCodeAndPrefix(a, 3)) {
                return a.substring(3);
            }
            if (a.length() > 1 && matchBrazilCarrierCodeAndPrefix(a, 1)) {
                return a.substring(1);
            }
        }
        return a;
    }

    private static boolean containsCountryCode(String property, String countryCode) {
        if (TextUtils.isEmpty(property)) {
            return false;
        }
        for (String code : property.split(",")) {
            if (TextUtils.equals(code, countryCode)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchBrazilCarrierCodeAndPrefix(String a, int len) {
        if (len == 1) {
            if (a.charAt(0) == '0') {
                return true;
            }
        } else if (len == 3 && a.charAt(0) == '0') {
            if (a.charAt(1) == '1' && a.charAt(2) == '2') {
                return true;
            }
            if (a.charAt(1) == '1' && a.charAt(2) == '5') {
                return true;
            }
            if (a.charAt(1) == '1' && a.charAt(2) == '7') {
                return true;
            }
            if (a.charAt(1) == '2' && a.charAt(2) == '1') {
                return true;
            }
            if (a.charAt(1) == '2' && a.charAt(2) == '3') {
                return true;
            }
            if (a.charAt(1) == '2' && a.charAt(2) == '5') {
                return true;
            }
            if (a.charAt(1) == '3' && a.charAt(2) == '1') {
                return true;
            }
            if (a.charAt(1) == '3' && a.charAt(2) == '2') {
                return true;
            }
            if (a.charAt(1) == '4' && a.charAt(2) == '1') {
                return true;
            }
            return a.charAt(1) == '4' && a.charAt(2) == '3';
        }
    }

    static String getCdmaTelephonyProperty(String property, String defaultValue) {
        String ps = SystemProperties.get(property);
        if (ps == null || ps.length() == 0) {
            return defaultValue;
        }
        String[] values = ps.split(",");
        if (values.length == 1) {
            return ps;
        }
        String defaultType = Integer.toString(null);
        String cdmaType = Integer.toString(2);
        int i = 0;
        while (i < values.length) {
            if (cdmaType.equals(TelephonyManagerEx.getDefault().getTelephonyProperty(i, TelephonyProperties.CURRENT_ACTIVE_PHONE, defaultType))) {
                return values[i] == null ? defaultValue : values[i];
            }
            i++;
        }
        return defaultValue;
    }
}
