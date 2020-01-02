package com.android.internal.app;

import android.content.Context;
import android.miui.R;
import android.os.Build.VERSION;
import com.android.internal.app.LocalePicker.LocaleInfo;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import miui.os.Build;

class LocalePickerInjector {
    LocalePickerInjector() {
    }

    static void removeDuplicate(List<String> list) {
        if (list != null) {
            int i = list.size() - 1;
            while (i > 0) {
                if (list.get(i) != null && ((String) list.get(i)).equals(list.get(i - 1))) {
                    list.remove(i);
                }
                i--;
            }
        }
    }

    static String getDisplayLanguage(Locale l, String[] specialLocaleCodes, String[] specialLocaleNames, String defaultDisplayName) {
        String code = l.toString();
        for (int i = 0; i < specialLocaleCodes.length; i++) {
            if (specialLocaleCodes[i].equals(code)) {
                return specialLocaleNames[i];
            }
        }
        return defaultDisplayName;
    }

    static void sortLocaleInfos(LocaleInfo[] localeInfos, Context context) {
        int resId;
        if (Build.IS_INTERNATIONAL_BUILD) {
            resId = R.array.organized_languages_international;
        } else {
            resId = R.array.organized_languages_default;
        }
        final String[] topLocales = context.getResources().getStringArray(resId);
        Arrays.sort(localeInfos, new Comparator<LocaleInfo>() {
            public int compare(LocaleInfo lhs, LocaleInfo rhs) {
                return findTopLocale(topLocales, lhs) - findTopLocale(topLocales, rhs);
            }

            private int findTopLocale(String[] topLocales, LocaleInfo localeInfo) {
                for (int i = 0; i < topLocales.length; i++) {
                    if (topLocales[i].equals(localeInfo.getLocale().toString())) {
                        return i;
                    }
                }
                return topLocales.length;
            }
        });
    }

    static void addLocaleLanguages(List<String> list) {
        if (Build.IS_INTERNATIONAL_BUILD && VERSION.SDK_INT >= 28 && list != null) {
            list.add("ar-EG-u-nu-latn");
            list.add("fa-IR-u-nu-latn");
        }
    }
}
