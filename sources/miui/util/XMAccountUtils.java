package miui.util;

import android.text.TextUtils;
import java.util.regex.Pattern;

public class XMAccountUtils {
    public static boolean isXiaomiAccount(String address) {
        if (TextUtils.isEmpty(address)) {
            return false;
        }
        return address.endsWith("@xiaomi.com");
    }

    public static boolean isXiaomiJID(String address) {
        if (isXiaomiAccount(address)) {
            return isNumeric(trimDomainSuffix(address));
        }
        return false;
    }

    public static boolean isNumeric(String account) {
        return Pattern.compile("[0-9]*").matcher(account).matches();
    }

    public static String trimDomainSuffix(String account) {
        if (TextUtils.isEmpty(account)) {
            return null;
        }
        int index = account.indexOf("@");
        if (index > 0) {
            return account.substring(0, index);
        }
        return account;
    }
}
