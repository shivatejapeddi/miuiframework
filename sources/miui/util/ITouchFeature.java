package miui.util;

import android.os.HwBinder;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.RemoteException;
import android.util.Slog;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ITouchFeature {
    private static final String DEFAULT = "default";
    private static final int GET_MODE_CUR_VALUE = 2;
    private static final int GET_MODE_DEF_VALUE = 5;
    private static final int GET_MODE_MAX_VALUE = 3;
    private static final int GET_MODE_MIN_VALUE = 4;
    private static final int GET_MODE_VALUES = 7;
    private static volatile ITouchFeature INSTANCE = null;
    private static final String INTERFACE_DESCRIPTOR = "vendor.xiaomi.hardware.touchfeature@1.0::ITouchFeature";
    private static final int MODE_RESET = 6;
    private static final String SERVICE_NAME = "vendor.xiaomi.hardware.touchfeature@1.0::ITouchFeature";
    private static final int SET_MODE_VALUE = 1;
    private static final String TAG = "ITouchFeature";
    public static final int TOUCH_ACTIVE_MODE = 1;
    public static final int TOUCH_EDGE_FILTER = 7;
    public static final int TOUCH_GAME_MODE = 0;
    public static final int TOUCH_MODE_DIRECTION = 8;
    public static final int TOUCH_MODE_NUM = 9;
    public static final int TOUCH_TOLERANCE = 3;
    public static final int TOUCH_UP_THRESHOLD = 2;
    public static final int TOUCH_WGH_MAX = 5;
    public static final int TOUCH_WGH_MIN = 4;
    public static final int TOUCH_WGH_STEP = 6;

    public static ITouchFeature getInstance() {
        if (INSTANCE == null) {
            synchronized (ITouchFeature.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ITouchFeature();
                }
            }
        }
        return INSTANCE;
    }

    public boolean setTouchMode(int mode, int value) {
        String str = "vendor.xiaomi.hardware.touchfeature@1.0::ITouchFeature";
        String str2 = TAG;
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                hidl_request.writeInt32(mode);
                hidl_request.writeInt32(value);
                hwService.transact(1, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                int val = hidl_reply.readInt32();
                if (val != 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("setTouchMode failed. ret = ");
                    stringBuilder.append(val);
                    Slog.e(str2, stringBuilder.toString());
                    hidl_reply.release();
                    return false;
                }
                hidl_reply.release();
                return true;
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("transact failed. ");
            stringBuilder2.append(e);
            Slog.e(str2, stringBuilder2.toString());
        } catch (NoSuchElementException e2) {
            Slog.e(str2, e2.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "setTouchMode failed.");
        return false;
    }

    public int getTouchModeCurValue(int mode) {
        String str = "vendor.xiaomi.hardware.touchfeature@1.0::ITouchFeature";
        String str2 = TAG;
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                hidl_request.writeInt32(mode);
                hwService.transact(2, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                int val = hidl_reply.readInt32();
                if (val < 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("getTouchModeCurValue failed. ret = ");
                    stringBuilder.append(val);
                    Slog.e(str2, stringBuilder.toString());
                    hidl_reply.release();
                    return -1;
                }
                hidl_reply.release();
                return val;
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("transact failed. ");
            stringBuilder2.append(e);
            Slog.e(str2, stringBuilder2.toString());
        } catch (NoSuchElementException e2) {
            Slog.e(str2, e2.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "getTouchModeCurValue failed.");
        return -1;
    }

    public int getTouchModeMaxValue(int mode) {
        String str = "vendor.xiaomi.hardware.touchfeature@1.0::ITouchFeature";
        String str2 = TAG;
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                hidl_request.writeInt32(mode);
                hwService.transact(3, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                int val = hidl_reply.readInt32();
                if (val < 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("getTouchModeMaxValue failed. ret = ");
                    stringBuilder.append(val);
                    Slog.e(str2, stringBuilder.toString());
                    hidl_reply.release();
                    return -1;
                }
                hidl_reply.release();
                return val;
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("transact failed. ");
            stringBuilder2.append(e);
            Slog.e(str2, stringBuilder2.toString());
        } catch (NoSuchElementException e2) {
            Slog.e(str2, e2.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "getTouchModeMaxValue failed.");
        return -1;
    }

    public int getTouchModeMinValue(int mode) {
        String str = "vendor.xiaomi.hardware.touchfeature@1.0::ITouchFeature";
        String str2 = TAG;
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                hidl_request.writeInt32(mode);
                hwService.transact(4, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                int val = hidl_reply.readInt32();
                if (val < 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("getTouchModeMinValue failed. ret = ");
                    stringBuilder.append(val);
                    Slog.e(str2, stringBuilder.toString());
                    hidl_reply.release();
                    return -1;
                }
                hidl_reply.release();
                return val;
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("transact failed. ");
            stringBuilder2.append(e);
            Slog.e(str2, stringBuilder2.toString());
        } catch (NoSuchElementException e2) {
            Slog.e(str2, e2.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "getTouchModeMinValue failed.");
        return -1;
    }

    public int getTouchModeDefValue(int mode) {
        String str = "vendor.xiaomi.hardware.touchfeature@1.0::ITouchFeature";
        String str2 = TAG;
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                hidl_request.writeInt32(mode);
                hwService.transact(5, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                int val = hidl_reply.readInt32();
                if (val < 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("getTouchModeDefValue failed. ret = ");
                    stringBuilder.append(val);
                    Slog.e(str2, stringBuilder.toString());
                    hidl_reply.release();
                    return -1;
                }
                hidl_reply.release();
                return val;
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("transact failed. ");
            stringBuilder2.append(e);
            Slog.e(str2, stringBuilder2.toString());
        } catch (NoSuchElementException e2) {
            Slog.e(str2, e2.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "getTouchModeDefValue failed.");
        return -1;
    }

    public int[] getModeValues(int mode) {
        String str = "vendor.xiaomi.hardware.touchfeature@1.0::ITouchFeature";
        String str2 = TAG;
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                hidl_request.writeInt32(mode);
                hwService.transact(7, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                ArrayList<Integer> val = hidl_reply.readInt32Vector();
                int n = val.size();
                int[] array = new int[n];
                for (int i = 0; i < n; i++) {
                    array[i] = ((Integer) val.get(i)).intValue();
                }
                hidl_reply.release();
                return array;
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("transact failed. ");
            stringBuilder.append(e);
            Slog.e(str2, stringBuilder.toString());
        } catch (NoSuchElementException e2) {
            Slog.e(str2, e2.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "getModeValues failed.");
        return new int[4];
    }

    public boolean resetTouchMode(int mode) {
        String str = "vendor.xiaomi.hardware.touchfeature@1.0::ITouchFeature";
        String str2 = TAG;
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                hidl_request.writeInt32(mode);
                hwService.transact(6, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                int val = hidl_reply.readInt32();
                if (val != 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("resetTouchMode failed. ret = ");
                    stringBuilder.append(val);
                    Slog.e(str2, stringBuilder.toString());
                    hidl_reply.release();
                    return false;
                }
                hidl_reply.release();
                return true;
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("transact failed. ");
            stringBuilder2.append(e);
            Slog.e(str2, stringBuilder2.toString());
        } catch (NoSuchElementException e2) {
            Slog.e(str2, e2.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "resetTouchMode failed.");
        return false;
    }
}
