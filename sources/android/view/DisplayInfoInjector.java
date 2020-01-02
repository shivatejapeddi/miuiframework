package android.view;

import android.app.AppGlobals;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import com.android.internal.R;
import java.util.ArrayList;
import miui.os.MiuiInit;
import miui.util.CustomizeUtil;

public class DisplayInfoInjector {
    private static final ArrayList<String> NOTCH_BLACK_LIST = new ArrayList<String>() {
        {
            add("com.duokan.reader");
            add("com.chaozh.iReaderFree");
            add("com.qq.ac.android");
            add("bubei.tingshu");
            add("com.sogou.novel");
            add("com.tencent.qqmusic");
            add("com.ss.android.article.video");
            add("com.tencent.karaoke");
            add("com.youku.phone");
            add("tv.danmaku.bili");
            add("com.storm.smart");
            add("com.babycloud.hanju");
        }
    };
    private static final ArrayList<String> SCALE_BLACK_LIST = new ArrayList<String>() {
        {
            add("com.miui.home");
            add("com.android.camera");
            add("com.baidu.input_mi");
            add("com.iflytek.inputmethod.miui");
            add("com.sohu.inputmethod.sogou.xiaomi");
            add("com.miui.securityinputmethod");
        }
    };
    private static String mAppName;
    private static int mNotchConfig;

    static int adjustWidthIfNeeded(Configuration configuration, int width, int appWidth, int appHeight, String packageName) {
        if (((configuration != null && configuration.orientation == 2) || appWidth > appHeight) && width == appWidth && CustomizeUtil.HAS_NOTCH) {
            if (SystemProperties.getBoolean("persist.sys.miui_optimization", "1".equals(SystemProperties.get("ro.miui.cts")) ^ 1)) {
                int uid = Process.myUid();
                if (uid < 10000) {
                    return width;
                }
                if (mAppName == null) {
                    mAppName = getAppName(uid);
                    mNotchConfig = MiuiInit.getNotchConfig(mAppName);
                }
                if ((mNotchConfig & 256) == 0) {
                    width -= Resources.getSystem().getDimensionPixelSize(R.dimen.status_bar_height);
                }
            }
        }
        return width;
    }

    static int adjustHeightIfNeeded(Configuration configuration, int height, int appWidth, int appHeight, String packageName) {
        if (((configuration != null && configuration.orientation == 1) || appWidth < appHeight) && height == appHeight && CustomizeUtil.HAS_NOTCH) {
            if (SystemProperties.getBoolean("persist.sys.miui_optimization", 1 ^ "1".equals(SystemProperties.get("ro.miui.cts")))) {
                int uid = Process.myUid();
                if (uid < 10000) {
                    return height;
                }
                if (mAppName == null) {
                    mAppName = getAppName(uid);
                    mNotchConfig = MiuiInit.getNotchConfig(mAppName);
                }
                if ((mNotchConfig & 256) == 0 && NOTCH_BLACK_LIST.contains(mAppName)) {
                    height -= Resources.getSystem().getDimensionPixelSize(R.dimen.status_bar_height);
                }
            }
        }
        return height;
    }

    static int adjustWidthIfNeededCurve(Configuration configuration, int width, int appWidth, int appHeight, int physicalWidth, int physicalHeight, String packageName) {
        if (((configuration != null && configuration.orientation == 1) || appWidth < appHeight) && width == appWidth) {
            int uid = Process.myUid();
            if (uid < 10000) {
                return width;
            }
            if (mAppName == null) {
                mAppName = getAppName(uid);
            }
            if (!SCALE_BLACK_LIST.contains(mAppName)) {
                width = physicalWidth;
            }
        }
        return width;
    }

    static int adjustHeightIfNeededCurve(Configuration configuration, int height, int appWidth, int appHeight, int physicalWidth, int physicalHeight, String packageName) {
        if (((configuration != null && configuration.orientation == 2) || appWidth > appHeight) && height == appHeight) {
            int uid = Process.myUid();
            if (uid < 10000) {
                return height;
            }
            if (mAppName == null) {
                mAppName = getAppName(uid);
            }
            if (!SCALE_BLACK_LIST.contains(mAppName)) {
                height = physicalWidth;
            }
        }
        return height;
    }

    static String getAppName(int uid) {
        String appName = "";
        if (uid < 10000) {
            return appName;
        }
        String[] packageNames = null;
        try {
            packageNames = AppGlobals.getPackageManager().getPackagesForUid(uid);
        } catch (RemoteException e) {
        }
        if (packageNames != null && packageNames.length > 0) {
            appName = packageNames[0];
        }
        return appName;
    }
}
