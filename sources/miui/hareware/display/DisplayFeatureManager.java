package miui.hareware.display;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import miui.util.FeatureParser;

public class DisplayFeatureManager {
    private static final String COLOR_SERVICE_NAME = "com.qti.snapdragon.sdk.display.IColorService";
    public static final int DEFALUT_GAMUT_MODE = 0;
    public static final int DEFALUT_SCREEN_CABC = 1;
    public static final int DEFALUT_SCREEN_COLOR = 2;
    public static final int DEFAULT_DISPLAY_EYECARE_LEVEL = 0;
    public static final int DEFAULT_SCREEN_SATURATION = getDefaultScreenSaturation();
    private static final String DISPLAY_FEATURE_SERVICE = "DisplayFeatureControl";
    public static final String PROPERTY_ASSERTIVE_DISPLAY = "persist.sys.ltm_enable";
    public static final String PROPERTY_DISPLAY_EYECARE = "persist.sys.display_eyecare";
    public static final String PROPERTY_GAMUT_MODE = "persist.sys.gamut_mode";
    public static final String PROPERTY_SCREEN_CABC = "persist.sys.display_cabc";
    public static final String PROPERTY_SCREEN_COLOR = "persist.sys.display_prefer";
    public static final String PROPERTY_SCREEN_SATURATION = "persist.sys.display_ce";
    public static final int SCREEN_COLOR_COOL = 3;
    public static final int SCREEN_COLOR_NATURE = 2;
    public static final int SCREEN_COLOR_WARM = 1;
    public static final int SCREEN_SATURATION_STANDARD = 10;
    public static final int SCREEN_SATURATION_VIVID = 11;
    private static String TAG = "DisplayFeatureManager";
    private static final int TRANSACTION_setActiveMode = 6;
    private static final int TRANSACTION_setDefaultMode = 12;
    private static DisplayFeatureManager sInstance;
    private DisplayFeatureServiceProxy mProxy;

    public static DisplayFeatureManager getInstance() {
        if (sInstance == null) {
            sInstance = new DisplayFeatureManager();
        }
        return sInstance;
    }

    private DisplayFeatureManager() {
        IBinder b = ServiceManager.getService(DISPLAY_FEATURE_SERVICE);
        if (b != null) {
            this.mProxy = new DisplayFeatureServiceProxy(b);
        }
    }

    public void setColorPrefer(int mode) {
        try {
            if (this.mProxy != null) {
                this.mProxy.setColorPrefer(0, mode);
            }
        } catch (Exception e) {
            Log.e(TAG, "set color prefer error.");
        }
        SystemProperties.set("persist.sys.display_prefer", String.valueOf(mode));
    }

    public int getColorPrefer() {
        return SystemProperties.getInt("persist.sys.display_prefer", 2);
    }

    public void setEyeCare(int level) {
        int ret = -1;
        try {
            if (this.mProxy != null) {
                ret = this.mProxy.setEyeCare(0, level);
            }
            IBinder colorService = ServiceManager.getService(COLOR_SERVICE_NAME);
            if (ret == -1 && colorService != null) {
                setActiveMode(colorService, 0, level);
                setDefaultMode(colorService, 0, level);
            }
        } catch (Exception e) {
            Log.e(TAG, "set eye care error.");
        }
        SystemProperties.set(PROPERTY_DISPLAY_EYECARE, String.valueOf(level));
    }

    public int getEyeCare() {
        return SystemProperties.getInt(PROPERTY_DISPLAY_EYECARE, 0);
    }

    private static int getDefaultScreenSaturation() {
        int defaultSaturationMode = 10;
        if (FeatureParser.getBoolean("is_hongmi", false)) {
            defaultSaturationMode = 11;
        }
        return FeatureParser.getInteger("display_ce", defaultSaturationMode);
    }

    public void setScreenSaturation(int value) {
        try {
            if (this.mProxy != null) {
                this.mProxy.setCE(0, value);
            }
        } catch (Exception e) {
            Log.e(TAG, "set screen ce error.");
        }
        SystemProperties.set("persist.sys.display_ce", String.valueOf(value));
    }

    public int getScreenSaturation() {
        return SystemProperties.getInt("persist.sys.display_ce", DEFAULT_SCREEN_SATURATION);
    }

    public void setScreenCabc(int value) {
        try {
            if (this.mProxy != null) {
                this.mProxy.setCABC(0, value);
            }
        } catch (Exception e) {
            Log.e(TAG, "set screen cabc error.");
        }
        SystemProperties.set(PROPERTY_SCREEN_CABC, String.valueOf(value));
    }

    public int getScreenCabc() {
        return SystemProperties.getInt(PROPERTY_SCREEN_CABC, 1);
    }

    public void setScreenGamut(int value) {
        try {
            if (this.mProxy != null) {
                this.mProxy.setGamutMode(0, value);
            }
        } catch (Exception e) {
            Log.e(TAG, "set screen gamut error.");
        }
        SystemProperties.set("persist.sys.gamut_mode", String.valueOf(value));
    }

    public int getScreenGamut() {
        return SystemProperties.getInt("persist.sys.gamut_mode", 0);
    }

    public void setAdEnable(boolean enable) {
        try {
            if (this.mProxy != null) {
                this.mProxy.setAd(0, enable ? 1 : 0, 255);
            }
        } catch (Exception e) {
            Log.e(TAG, "set assertive display error.");
        }
        SystemProperties.set("persist.sys.ltm_enable", String.valueOf(enable));
    }

    public boolean isAdEnable() {
        return SystemProperties.getBoolean("persist.sys.ltm_enable", true);
    }

    private int setActiveMode(IBinder colorService, int displayId, int modeId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(COLOR_SERVICE_NAME);
            data.writeInt(displayId);
            data.writeInt(modeId);
            colorService.transact(6, data, reply, 0);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    private int setDefaultMode(IBinder colorService, int displayId, int modeId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(COLOR_SERVICE_NAME);
            data.writeInt(displayId);
            data.writeInt(modeId);
            colorService.transact(12, data, reply, 0);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }
}
