package com.miui.internal.search;

import android.app.backup.FullBackup;
import android.miui.BiometricConnect;
import android.os.IncidentManager;
import android.provider.Telephony.BaseMmsColumns;
import com.android.internal.telephony.PhoneConstants;
import com.miui.mishare.RemoteDevice;
import java.util.ArrayList;
import java.util.Comparator;

public class PinyinMatcher {
    private static String[] SHENGMU = new String[]{"b", "p", "m", FullBackup.FILES_TREE_TOKEN, "d", IncidentManager.URI_PARAM_TIMESTAMP, "n", "l", "g", FullBackup.KEY_VALUE_DATA_TOKEN, BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_H, "j", "q", "x", "z", FullBackup.CACHE_TREE_TOKEN, RemoteDevice.KEY_STATUS, "zh", "ch", "sh", "r", "y", BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_W};
    private static String[] YUNMU = new String[]{FullBackup.APK_TREE_TOKEN, "o", "e", "i", "u", BaseMmsColumns.MMS_VERSION, "ai", "ei", "ui", "ao", "ou", "iu", PhoneConstants.APN_TYPE_IA, "ie", "ve", "er", "an", "en", "in", "un", "vn", "ang", "eng", "ing", "ong", "iao", "ian", "iang", "iong", "ua", "uo", "uai", "uan", "van", "uang"};

    public static String[] splitPinyin(String s, boolean correction) {
        if (correction) {
            throw new IllegalArgumentException("Correction not supported yet");
        }
        ArrayList<String> hanzis = new ArrayList();
        while (s.length() > 0) {
            int i = 0;
            for (String sm : SHENGMU) {
                if (s.startsWith(sm)) {
                    hanzis.add(sm);
                    s = s.substring(sm.length());
                    break;
                }
            }
            String[] strArr = YUNMU;
            int length = strArr.length;
            while (i < length) {
                String ym = strArr[i];
                if (s.startsWith(ym)) {
                    hanzis.set(hanzis.size() - 1, ((String) hanzis.get(hanzis.size() - 1)).concat(ym));
                    s = s.substring(ym.length());
                    break;
                }
                i++;
            }
        }
        return (String[]) hanzis.toArray(new String[hanzis.size()]);
    }

    public static int pinyinLevenshtein(String s1, String s2) {
        return SearchUtils.Levenshtein(splitPinyin(s1, false), splitPinyin(s2, false), new Comparator<String>() {
            public int compare(String o1, String o2) {
                return 0;
            }
        });
    }
}
