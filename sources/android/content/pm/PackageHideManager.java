package android.content.pm;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.miui.Shell;
import android.os.Process;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.util.Xml;
import com.android.ims.ImsConfig;
import com.android.internal.util.FastXmlSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import miui.maml.util.net.SimpleRequest;
import miui.os.Build;
import miui.util.FeatureParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class PackageHideManager {
    private static final String APP_HIDE_SWITCH_FILE = "/data/system/app_hide_switch.xml";
    private static final int APP_HIDE_SWITCH_FILE_VERSION = 2;
    private static String TAG = PackageHideManager.class.getName();
    private static AppHideConfig appHideConfig;
    private static File mFile;
    private static volatile PackageHideManager sInstance = null;
    private Map<String, AppHideItem> mShouldHideApks = new HashMap();

    static class AppHideConfig {
        boolean isFunctionOpen;
        boolean isHide;
        int version;

        AppHideConfig() {
        }
    }

    static class AppHideItem {
        public static final String PATH_DILIMITER = ";";
        boolean isHide;
        String packageName;
        String[] pathArray;

        public AppHideItem(String packageName, boolean isHide, String... path) {
            this.packageName = packageName;
            this.isHide = isHide;
            this.pathArray = path;
        }

        public String getJoinPath() {
            Object[] objArr = this.pathArray;
            if (objArr == null) {
                return null;
            }
            return TextUtils.join((CharSequence) ";", objArr);
        }
    }

    public static PackageHideManager getInstance(boolean isFirstBoot) {
        if (sInstance == null) {
            synchronized (PackageHideManager.class) {
                if (sInstance == null) {
                    sInstance = new PackageHideManager(isFirstBoot);
                }
            }
        }
        return sInstance;
    }

    private PackageHideManager(boolean isFirstBoot) {
        init(isFirstBoot);
    }

    private void init(boolean isFirstBoot) {
        if (isValidDevice()) {
            AppHideConfig appHideConfig;
            mFile = new File(APP_HIDE_SWITCH_FILE);
            readAppHideConfig();
            if (appHideConfig == null) {
                appHideConfig = new AppHideConfig();
                appHideConfig = appHideConfig;
                appHideConfig.isFunctionOpen = false;
                appHideConfig.isHide = false;
                appHideConfig.version = 2;
            }
            if (isFirstBoot) {
                appHideConfig = appHideConfig;
                appHideConfig.isFunctionOpen = true;
                appHideConfig.isHide = true;
                initHideApks();
                writeAppHideConfig();
            } else if (mFile.exists() && appHideConfig.version != 2) {
                appHideConfig.version = 2;
                initHideApks();
                writeAppHideConfig();
            }
        }
    }

    private boolean writeAppHideConfig() {
        if (mFile == null) {
            return false;
        }
        String tmpPath;
        Slog.d(TAG, "start writeAppHideConfig()");
        OutputStream stream = null;
        boolean result = true;
        if (isSystemServer()) {
            tmpPath = mFile.getAbsolutePath();
        } else {
            tmpPath = new StringBuilder();
            tmpPath.append(mFile.getAbsolutePath());
            tmpPath.append(Session.SESSION_SEPARATION_CHAR_CHILD);
            tmpPath.append(System.currentTimeMillis());
            tmpPath.append("_.bak");
            tmpPath = tmpPath.toString();
        }
        try {
            if (isSystemServer()) {
                stream = new FileOutputStream(tmpPath);
            } else {
                stream = new ByteArrayOutputStream();
            }
            XmlSerializer out = new FastXmlSerializer();
            out.setOutput(stream, SimpleRequest.UTF8);
            out.startDocument(null, Boolean.valueOf(true));
            out.startTag(null, "app-hide");
            out.attribute(null, "isFunctionOpen", Boolean.toString(appHideConfig.isFunctionOpen));
            out.attribute(null, "version", Integer.toString(appHideConfig.version));
            out.attribute(null, "isHide", Boolean.toString(appHideConfig.isHide));
            if (this.mShouldHideApks != null) {
                for (AppHideItem item : this.mShouldHideApks.values()) {
                    if (!TextUtils.isEmpty(item.packageName)) {
                        if (!TextUtils.isEmpty(item.getJoinPath())) {
                            out.startTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
                            out.attribute(null, "package", item.packageName);
                            out.attribute(null, "path", item.getJoinPath());
                            out.attribute(null, "isHide", Boolean.toString(item.isHide));
                            out.endTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
                        }
                    }
                }
            }
            out.endTag(null, "app-hide");
            out.endDocument();
            if (!isSystemServer()) {
                result = Shell.writeByteArray(tmpPath, false, ((ByteArrayOutputStream) stream).toByteArray());
            }
            try {
                stream.close();
            } catch (Exception e) {
                result = false;
            }
        } catch (Exception e2) {
            Slog.w(TAG, "Failed to write state, restoring backup.", e2);
            result = false;
            if (stream != null) {
                stream.close();
            }
        } catch (Throwable th) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e3) {
                }
            }
        }
        if (result && !isSystemServer()) {
            try {
                synchronized (mFile) {
                    result = Shell.move(tmpPath, mFile.getAbsolutePath());
                }
            } catch (Exception e4) {
                Shell.remove(tmpPath);
                result = false;
            }
        }
        return result;
    }

    private void readAppHideConfig() {
        String str;
        StringBuilder stringBuilder;
        if (mFile != null) {
            Slog.d(TAG, "start readAppHideConfig()");
            synchronized (mFile) {
                InputStream stream = null;
                try {
                    if (isSystemServer()) {
                        stream = new FileInputStream(mFile);
                    } else {
                        byte[] bytes = Shell.readByteArray(mFile.getAbsolutePath());
                        if (bytes == null) {
                            if (stream != null) {
                                try {
                                    stream.close();
                                } catch (IOException e) {
                                }
                            }
                            return;
                        }
                        stream = new ByteArrayInputStream(bytes);
                    }
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(stream, null);
                    moveToNextStartTag(parser);
                    if (parser.getName().equals("app-hide")) {
                        appHideConfig = new AppHideConfig();
                        appHideConfig.isFunctionOpen = Boolean.parseBoolean(parser.getAttributeValue(null, "isFunctionOpen"));
                        appHideConfig.version = Integer.parseInt(parser.getAttributeValue(null, "version"));
                        appHideConfig.isHide = Boolean.parseBoolean(parser.getAttributeValue(null, "isHide"));
                    }
                    while (moveToNextStartTag(parser)) {
                        if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                            String packageName = parser.getAttributeValue(null, "package");
                            String path = parser.getAttributeValue(null, "path");
                            boolean isHide = Boolean.parseBoolean(parser.getAttributeValue(null, "isHide"));
                            if (!(TextUtils.isEmpty(packageName) || TextUtils.isEmpty(path))) {
                                this.mShouldHideApks.put(packageName, new AppHideItem(packageName, isHide, TextUtils.split(path, ";")));
                            }
                            String str2 = TAG;
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("read item: ");
                            stringBuilder2.append(packageName);
                            stringBuilder2.append(": ");
                            stringBuilder2.append(path);
                            Log.e(str2, stringBuilder2.toString());
                        }
                    }
                    try {
                        stream.close();
                    } catch (IOException e2) {
                    }
                } catch (IllegalStateException e3) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed parsing ");
                    stringBuilder.append(e3);
                    Slog.w(str, stringBuilder.toString());
                    if (stream != null) {
                        stream.close();
                    }
                } catch (NullPointerException e4) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed parsing ");
                    stringBuilder.append(e4);
                    Slog.w(str, stringBuilder.toString());
                    if (stream != null) {
                        stream.close();
                    }
                } catch (NumberFormatException e5) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed parsing ");
                    stringBuilder.append(e5);
                    Slog.w(str, stringBuilder.toString());
                    if (stream != null) {
                        stream.close();
                    }
                } catch (XmlPullParserException e6) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed parsing ");
                    stringBuilder.append(e6);
                    Slog.w(str, stringBuilder.toString());
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e7) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed parsing ");
                    stringBuilder.append(e7);
                    Slog.w(str, stringBuilder.toString());
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IndexOutOfBoundsException e8) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed parsing ");
                    stringBuilder.append(e8);
                    Slog.w(str, stringBuilder.toString());
                    if (stream != null) {
                        stream.close();
                    }
                } catch (Exception e9) {
                    try {
                        str = TAG;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed parsing ");
                        stringBuilder.append(e9);
                        Slog.w(str, stringBuilder.toString());
                        if (stream != null) {
                            stream.close();
                        }
                    } catch (Throwable th) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e10) {
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0013 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0012 A:{RETURN} */
    private boolean moveToNextStartTag(org.xmlpull.v1.XmlPullParser r6) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r5 = this;
        r0 = 0;
        if (r6 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r6.next();
        r2 = r1;
        r3 = 1;
        r4 = 2;
        if (r1 == r4) goto L_0x0010;
    L_0x000d:
        if (r2 == r3) goto L_0x0010;
    L_0x000f:
        goto L_0x0004;
    L_0x0010:
        if (r2 == r4) goto L_0x0013;
    L_0x0012:
        return r0;
    L_0x0013:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageHideManager.moveToNextStartTag(org.xmlpull.v1.XmlPullParser):boolean");
    }

    private boolean isSystemServer() {
        return Process.myUid() == 1000;
    }

    private void initHideApks() {
        this.mShouldHideApks.clear();
        String[] pkgNameList = FeatureParser.getStringArray("hidden_app_packagename_list");
        String[] appPathList = FeatureParser.getStringArray("hidden_app_path_list");
        if (pkgNameList != null && appPathList != null && pkgNameList.length == appPathList.length) {
            for (int i = 0; i < pkgNameList.length; i++) {
                this.mShouldHideApks.put(pkgNameList[i], new AppHideItem(pkgNameList[i], true, appPathList[i].split(",")));
            }
        }
    }

    public boolean setHideApp(Context context, boolean hide) {
        if (isValidDevice()) {
            AppHideConfig appHideConfig = appHideConfig;
            if (!(appHideConfig == null || appHideConfig.isHide == hide)) {
                appHideConfig.isHide = hide;
                if (!hide) {
                    clearUserAleadyInstalled(context);
                }
                return writeAppHideConfig();
            }
        }
        return false;
    }

    public boolean setHideApp(Context context, String packageName, boolean hide) {
        if (TextUtils.isEmpty(packageName) || !isValidDevice() || appHideConfig == null) {
            return false;
        }
        AppHideItem item = (AppHideItem) this.mShouldHideApks.get(packageName);
        if (item == null || item.isHide == hide) {
            return false;
        }
        item.isHide = hide;
        if (!hide) {
            clearUserAleadyInstalled(context, item);
        }
        return writeAppHideConfig();
    }

    public List<String> getIgnoreApkPathList() {
        List<String> pathList = new ArrayList();
        if (isAppHide()) {
            for (AppHideItem item : this.mShouldHideApks.values()) {
                if (item.isHide && item.pathArray != null) {
                    for (String path : item.pathArray) {
                        pathList.add(path);
                    }
                }
            }
        }
        return pathList;
    }

    public List<String> getIgnoreApkPkgNameList() {
        List<String> pkgNameList = new ArrayList();
        Set<String> keySet = this.mShouldHideApks;
        if (keySet != null) {
            for (String pkg : keySet.keySet()) {
                pkgNameList.add(pkg);
            }
        }
        return pkgNameList;
    }

    private void clearUserAleadyInstalled(Context context) {
        for (AppHideItem item : this.mShouldHideApks.values()) {
            clearUserAleadyInstalled(context, item);
        }
    }

    private void clearUserAleadyInstalled(Context context, AppHideItem item) {
        try {
            int i = 0;
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(item.packageName, 0);
            if (applicationInfo != null && item.pathArray != null) {
                String[] strArr = item.pathArray;
                int length = strArr.length;
                while (i < length) {
                    String path = strArr[i];
                    if (!(applicationInfo.sourceDir == null || applicationInfo.sourceDir.equalsIgnoreCase(path))) {
                        File file = new File(path);
                        if (file.exists() && file.isFile()) {
                            file.delete();
                        }
                    }
                    i++;
                }
            }
        } catch (NameNotFoundException e) {
        }
    }

    public boolean isFunctionOpen() {
        if (isValidDevice()) {
            AppHideConfig appHideConfig = appHideConfig;
            if (appHideConfig != null && appHideConfig.isFunctionOpen) {
                return true;
            }
        }
        return false;
    }

    public boolean isAppHide() {
        if (isValidDevice()) {
            AppHideConfig appHideConfig = appHideConfig;
            if (appHideConfig != null && appHideConfig.isFunctionOpen && appHideConfig.isHide) {
                return true;
            }
        }
        return false;
    }

    public boolean isAppHide(String packageName) {
        if (TextUtils.isEmpty(packageName) || !isAppHide()) {
            return false;
        }
        AppHideItem item = (AppHideItem) this.mShouldHideApks.get(packageName);
        if (item == null || !item.isHide) {
            return false;
        }
        return true;
    }

    public static boolean isValidDevice() {
        return FeatureParser.getBoolean("support_app_hiding", false) && Build.IS_CM_CUSTOMIZATION;
    }
}
