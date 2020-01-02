package com.miui.whetstone.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.SystemProperties;
import com.miui.whetstone.strategy.WhetstonePackageInfo;
import java.util.ArrayList;
import java.util.List;

public class WhetstoneAppManager {
    private static final String TAG = "WhetstoneAppManager";
    private static final int UNINIT = -2;
    private static WhetstoneAppManager _sInstance;
    private static final Object sMutex = new Object();
    private static List<WhetstonePackageInfo> sPackages = new ArrayList();
    private IBinder mApplicationThread;
    private Context mContex;
    public boolean mEnable;
    public boolean mHasInit;

    private WhetstoneAppManager() {
        this.mApplicationThread = new WhetstoneApplicationThread();
        this.mHasInit = false;
        checkInit();
    }

    private void checkInit() {
    }

    private WhetstoneAppManager(Context context) {
        this();
        attach(context);
    }

    public void attach(Context appContext) {
    }

    public void onCreate(Application application) {
    }

    public static boolean getLeakCanaryWorksProperty() {
        return SystemProperties.get("persist.sys.mem_leak_debug", "false").equalsIgnoreCase("true");
    }

    public static synchronized WhetstoneAppManager getInstance() {
        WhetstoneAppManager whetstoneAppManager;
        synchronized (WhetstoneAppManager.class) {
            if (_sInstance == null) {
                _sInstance = new WhetstoneAppManager();
            }
            whetstoneAppManager = _sInstance;
        }
        return whetstoneAppManager;
    }

    public static synchronized WhetstoneAppManager getInstance(Context context) {
        WhetstoneAppManager whetstoneAppManager;
        synchronized (WhetstoneAppManager.class) {
            if (_sInstance == null) {
                _sInstance = new WhetstoneAppManager(context);
            }
            whetstoneAppManager = _sInstance;
        }
        return whetstoneAppManager;
    }

    public void onResume(Activity activity) {
    }

    public static void addBitmapDrawable(BitmapDrawable dr) {
    }

    public static void addDrawable(Drawable dr) {
    }

    public static void addBitmap(Bitmap bitmap) {
    }

    public static void restoreDirectBitmap(Bitmap bitmap) {
    }

    public static boolean isEnable() {
        return false;
    }

    public static boolean handleTrimMemory(int level) {
        return true;
    }

    public static void setHardwareRendererIfNeeded() {
    }

    public static void trimHeapSizeIfNeeded(ApplicationInfo info) {
    }

    public static void setWhetstonePackageRecordInfo(List<WhetstonePackageInfo> list, boolean isAppend) {
    }
}
