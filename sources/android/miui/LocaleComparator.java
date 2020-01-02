package android.miui;

import com.android.internal.app.LocalePicker.LocaleInfo;
import java.util.Comparator;
import miui.os.Build;

public class LocaleComparator implements Comparator<LocaleInfo> {
    private static final int NON_TOP_LOCALE_INDEX = TOP_LOCALES.length;
    private static final String[] TOP_LOCALES;

    static {
        if (Build.IS_INTERNATIONAL_BUILD) {
            TOP_LOCALES = new String[]{"zh_CN", "en_US", "en_GB", "hi_IN", "in_ID", "ms_MY", "vi_VN", "zh_TW", "th_TH"};
        } else {
            TOP_LOCALES = new String[]{"zh_CN", "zh_TW", "en_US"};
        }
    }

    public int findTopLocale(LocaleInfo localeInfo) {
        int i = 0;
        while (true) {
            String[] strArr = TOP_LOCALES;
            if (i >= strArr.length) {
                return NON_TOP_LOCALE_INDEX;
            }
            if (strArr[i].equals(localeInfo.getLocale().toString())) {
                return i;
            }
            i++;
        }
    }

    public int compare(LocaleInfo lhs, LocaleInfo rhs) {
        int lpos = findTopLocale(lhs);
        int rpos = findTopLocale(rhs);
        return lpos == rpos ? lhs.compareTo(rhs) : lpos - rpos;
    }
}
