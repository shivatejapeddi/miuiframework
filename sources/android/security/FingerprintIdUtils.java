package android.security;

import android.content.Context;
import android.os.Environment;
import android.os.UserHandle;
import android.provider.MiuiSettings;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import miui.security.SecurityManager;

public class FingerprintIdUtils {
    private static final String FINGERPRINTID_USERID_TABLE = "fingerid_user_map.xml";

    public static int getSecondSpaceId(Context context) {
        return Secure.getIntForUser(context.getContentResolver(), MiuiSettings.Secure.SECOND_USER_ID, -10000, 0);
    }

    public static void putUserFingerprintIds(Context context, HashMap<String, Integer> fingeridMap) {
        if (fingeridMap != null && UserHandle.myUserId() == getSecondSpaceId(context)) {
            getSecurityManager(context).putSystemDataStringFile(getPathByUserId(UserHandle.myUserId()), map2Str(fingeridMap));
        }
    }

    public static HashMap<String, Integer> getUserFingerprintIds(Context context, int userId) {
        return FingerprintIdUtilsCompat.getValidFingerPrintIds(getUserFingerprintIdsFromXml(context, userId), context);
    }

    public static void deleteFingerprintById(Context context, String fingerprintId) {
        int secondUserId = getSecondSpaceId(context);
        HashMap<String, Integer> map = getUserFingerprintIdsFromXml(context, secondUserId);
        if (map != null) {
            map.remove(fingerprintId);
        }
        writeFingerprintIdsToXml(context, secondUserId, map);
    }

    private static HashMap<String, Integer> getUserFingerprintIdsFromXml(Context context, int userId) {
        return str2Map(getSecurityManager(context).readSystemDataStringFile(getPathByUserId(userId)));
    }

    public static void writeFingerprintIdsToXml(Context context, int useId, HashMap<String, Integer> map) {
        getSecurityManager(context).putSystemDataStringFile(getPathByUserId(useId), map2Str(map));
    }

    private static String getPathByUserId(int userId) {
        return new File(Environment.getUserSystemDirectory(userId), FINGERPRINTID_USERID_TABLE).getAbsolutePath();
    }

    private static SecurityManager getSecurityManager(Context context) {
        return (SecurityManager) context.getSystemService(Context.SECURITY_SERVICE);
    }

    private static String map2Str(HashMap<String, Integer> map) {
        StringBuilder buffer = new StringBuilder();
        Iterator<Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Integer> entry = (Entry) it.next();
            buffer.append((String) entry.getKey());
            buffer.append('=');
            buffer.append((Integer) entry.getValue());
            if (it.hasNext()) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }

    private static HashMap<String, Integer> str2Map(String str) {
        HashMap<String, Integer> map = new HashMap();
        if (!TextUtils.isEmpty(str)) {
            String[] packageArr = str.split(",");
            if (packageArr != null) {
                for (String pair : packageArr) {
                    int equalIndex = pair.indexOf(61);
                    map.put(pair.substring(null, equalIndex), Integer.valueOf(pair.substring(equalIndex + 1)));
                }
            }
        }
        return map;
    }
}
