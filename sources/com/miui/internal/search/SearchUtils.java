package com.miui.internal.search;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import miui.os.Build;
import miui.text.ChinesePinyinConverter;
import miui.text.ChinesePinyinConverter.Token;
import miui.util.FeatureParser;
import miui.util.cache.Cache;
import miui.util.cache.LruCache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchUtils {
    private static final Pattern PATTERN_ALPHABET = Pattern.compile("\\w+");
    private static final String TAG = "Utils";
    private static Cache<String, Context> sContextCache;
    private static List<PackageInfo> sInstalledPackageList;

    public static String getString(Context ctx, String res) {
        String str = SettingsTree.SETTINGS_PACKAGE;
        String str2 = "string";
        if (!TextUtils.isEmpty(res)) {
            Resources resources;
            try {
                resources = ctx.getResources();
                str = resources.getString(resources.getIdentifier(res, str2, ctx.getPackageName()));
                return str;
            } catch (NotFoundException e) {
                try {
                    resources = getPackageContext(ctx, str).getResources();
                    return resources.getString(resources.getIdentifier(res, str2, str));
                } catch (NotFoundException e2) {
                }
            }
        }
        return "";
    }

    public static boolean getBoolean(Context ctx, String res) {
        if (!TextUtils.isEmpty(res)) {
            try {
                Resources resources = ctx.getResources();
                return resources.getBoolean(resources.getIdentifier(res, "bool", ctx.getPackageName()));
            } catch (NotFoundException e) {
            }
        }
        return false;
    }

    public static String getPinyin(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        StringBuilder pinyin = new StringBuilder();
        Iterator it = ChinesePinyinConverter.getInstance().get(str, false, true).iterator();
        while (it.hasNext()) {
            pinyin.append(((Token) it.next()).target);
        }
        return pinyin.toString();
    }

    public static int Levenshtein(char[] array1, char[] array2) {
        int j;
        if (array1.length < array2.length) {
            char[] temp = array1;
            array1 = array2;
            array2 = temp;
        }
        int n = array1.length;
        int m = array2.length;
        int[] f = new int[(m + 1)];
        for (j = 0; j <= m; j++) {
            f[j] = j;
        }
        for (j = 1; j <= n; j++) {
            int fi_1j_1 = j - 1;
            f[0] = j;
            for (int j2 = 1; j2 <= m; j2++) {
                int fi_1j = f[j2];
                f[j2] = Math.min(f[j2] + 1, (array1[j + -1] == array2[j2 + -1] ? 0 : 1) + fi_1j_1);
                f[j2] = Math.min(f[j2 - 1] + 1, f[j2]);
                fi_1j_1 = fi_1j;
            }
        }
        return f[m];
    }

    public static <T> int Levenshtein(T[] array1, T[] array2, Comparator<T> cmp) {
        int j;
        if (array1.length < array2.length) {
            T[] temp = array1;
            array1 = array2;
            array2 = temp;
        }
        int n = array1.length;
        int m = array2.length;
        int[] f = new int[(m + 1)];
        for (j = 0; j <= m; j++) {
            f[j] = j;
        }
        for (j = 1; j <= n; j++) {
            int fi_1j_1 = j - 1;
            f[0] = j;
            for (int j2 = 1; j2 <= m; j2++) {
                int fi_1j = f[j2];
                f[j2] = Math.min(f[j2] + 1, (cmp.compare(array1[j + -1], array2[j2 + -1]) == 0 ? 0 : 1) + fi_1j_1);
                f[j2] = Math.min(f[j2 - 1] + 1, f[j2]);
                fi_1j_1 = fi_1j;
            }
        }
        return f[m];
    }

    public static double doSimpleMatch(String str1, String str2) {
        double score = 0.0d;
        if (str1.length() > str2.length()) {
            String t = str1;
            str1 = str2;
            str2 = t;
        }
        if (!(TextUtils.isEmpty(str1) || TextUtils.isEmpty(str2))) {
            if (str2.equals(str1)) {
                score = 1.0d;
            }
            if (score == 0.0d && str2.startsWith(str1)) {
                score = 0.8d;
            }
            if (score == 0.0d && str2.contains(str1)) {
                score = 0.6d;
            }
            if (score == 0.0d) {
                score = 0.5d - (((double) Levenshtein(str1.toCharArray(), str2.toCharArray())) / ((double) Math.max(str1.length(), str2.length())));
            }
        }
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str1);
            stringBuilder.append(" - ");
            stringBuilder.append(str2);
            stringBuilder.append(": ");
            stringBuilder.append(score);
            Log.v(str, stringBuilder.toString());
        }
        return score;
    }

    public static double doPinyinMatch(String str1, String str2) {
        double score = 0.0d;
        if (!(TextUtils.isEmpty(str1) || TextUtils.isEmpty(str2) || !PATTERN_ALPHABET.matcher(str1).matches())) {
            if (str2.equals(str1)) {
                score = 1.0d;
            }
            if (score == 0.0d && str2.startsWith(str1)) {
                score = 0.8d;
            }
            if (score == 0.0d) {
                if (str2.contains(str1)) {
                    score = 0.6d;
                } else {
                    score = 0.5d;
                }
                score -= ((double) Levenshtein(str1.toCharArray(), str2.toCharArray())) / ((double) Math.max(str1.length(), str2.length()));
            }
        }
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str1);
            stringBuilder.append(" - ");
            stringBuilder.append(str2);
            stringBuilder.append(": ");
            stringBuilder.append(score);
            Log.v(str, stringBuilder.toString());
        }
        return score;
    }

    public static void logCost(double startTime, double endTime, Object extra) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(new Throwable().getStackTrace()[1]);
        stringBuilder.append(" (");
        stringBuilder.append(extra);
        stringBuilder.append(") takes ");
        stringBuilder.append(endTime - startTime);
        stringBuilder.append("ms");
        Log.d(TAG, stringBuilder.toString());
    }

    public static JSONObject readJSONObject(InputStream is) throws IOException, JSONException {
        String str = "close InputStream failed";
        String str2 = TAG;
        InputStreamReader reader = new InputStreamReader(is);
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[1024];
        while (true) {
            try {
                int read = reader.read(buffer, 0, buffer.length);
                int i = read;
                if (read <= 0) {
                    break;
                }
                builder.append(buffer, 0, i);
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(str2, str);
                }
            }
        }
        JSONObject jSONObject = new JSONObject(builder.toString());
        return jSONObject;
    }

    public static boolean isSecondSpace() {
        return UserHandle.myUserId() != 0;
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean removeViaSecondSpace(boolean isSecondSpace) {
        return !isSecondSpace && isSecondSpace();
    }

    public static boolean removeViaFeature(String feature) {
        if (TextUtils.isEmpty(feature)) {
            return false;
        }
        for (String cond : feature.split("&&")) {
            String cond2 = cond2.trim();
            boolean expect = true;
            if (cond2.startsWith("!")) {
                expect = false;
                cond2 = cond2.substring(1);
            }
            if (FeatureParser.getBoolean(cond2, false) != expect) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOldmanMode() {
        return Build.getUserMode() == 1;
    }

    public static boolean isPackageExisted(Context context, String pkg) {
        if (sInstalledPackageList == null) {
            sInstalledPackageList = context.getPackageManager().getInstalledPackages(128);
        }
        for (PackageInfo pi : sInstalledPackageList) {
            if (pkg.contains(pi.packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void clearPackageExistedCache() {
        sInstalledPackageList = null;
    }

    public static Context getPackageContext(Context context, String pkg) {
        String pkgOrigin = pkg;
        while (!TextUtils.isEmpty(pkg)) {
            if (pkg.equals(context.getPackageName())) {
                return context;
            }
            Cache cache = sContextCache;
            if (cache == null || cache.get(pkg) == null) {
                try {
                    Context newContext = context.createPackageContext(pkg, 3);
                    if (sContextCache == null) {
                        sContextCache = new LruCache(5);
                    }
                    sContextCache.put(pkg, newContext, 1);
                    return newContext;
                } catch (NameNotFoundException e) {
                    if (!pkg.contains(".")) {
                        break;
                    }
                    pkg = pkg.substring(0, pkg.lastIndexOf(46));
                }
            } else {
                return (Context) sContextCache.get(pkg);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Application package ");
        stringBuilder.append(pkgOrigin);
        stringBuilder.append(" not found");
        throw new RuntimeException(stringBuilder.toString());
    }

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }

    public static ArrayMap<String, String> jsonToMap(JSONObject jObject) throws JSONException {
        ArrayMap<String, String> map = new ArrayMap();
        if (jObject == null) {
            return map;
        }
        Iterator<?> keys = jObject.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, jObject.getString(key));
        }
        return map;
    }

    public static boolean isJSONArrayEmpty(JSONArray items) {
        return items == null || items.length() <= 0;
    }

    public static List<String> convertJsonArrayToList(JSONArray jsonArray) throws JSONException {
        List<String> result = new ArrayList();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                result.add(jsonArray.getString(i));
            }
        }
        return result;
    }

    public static String getString(Context mContext, String packageName, String res) {
        if (!TextUtils.isEmpty(res)) {
            try {
                Resources pRes = mContext.createPackageContext(packageName, null).getResources();
                if (pRes != null) {
                    int id = pRes.getIdentifier(res, "string", packageName);
                    if (id != 0) {
                        return pRes.getString(id);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static void writeJsonToFile(JSONObject jsonObject, File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(jsonObject.toString().getBytes());
            os.flush();
        } catch (IOException | OutOfMemoryError e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("write json data fail!");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
        } catch (Throwable th) {
            closeQuietly(os);
        }
        closeQuietly(os);
    }

    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("NumberFormatException: ");
            stringBuilder.append(e);
            Log.e(TAG, stringBuilder.toString());
            return -1;
        }
    }
}
