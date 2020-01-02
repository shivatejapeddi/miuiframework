package miui.util;

import android.os.HwBinder;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.util.Slog;

public class IWirelessSwitch {
    private static final String DEFAULT = "default";
    private static final int GET_WIRELESS_STATUS = 3;
    private static volatile IWirelessSwitch INSTANCE = null;
    private static final String INTERFACE_DESCRIPTOR = "vendor.xiaomi.hardware.wireless@1.0::IWirelessSwitch";
    private static final int IS_WIRELESS_SUPPORTED = 1;
    private static final String SERVICE_NAME = "vendor.xiaomi.hardware.wireless@1.0::IWirelessSwitch";
    private static final int SET_WIRELESS_ENABLED = 2;
    public static final int STATUS_BAD_VALUE = 3;
    public static final int STATUS_FAILURE_UNKNOWN = 1;
    public static final int STATUS_NOT_SUPPORTED = 2;
    public static final int STATUS_SUCCESS = 0;
    private static final String TAG = "IWirelessSwitch";
    public static final int WIRELESS_DISABLED = 1;
    public static final int WIRELESS_ENABLED = 0;
    public static final int WIRELESS_NOT_SUPPORTED = 2;
    public static final int WIRELESS_STATUS_UNKNOWN = 3;

    public static IWirelessSwitch getInstance() {
        if (INSTANCE == null) {
            synchronized (IWirelessSwitch.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IWirelessSwitch();
                }
            }
        }
        return INSTANCE;
    }

    public boolean isWirelessChargingSupported() {
        String str = "vendor.xiaomi.hardware.wireless@1.0::IWirelessSwitch";
        String str2 = TAG;
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                hwService.transact(1, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                if (hidl_reply.readInt32() == 0) {
                    Slog.e(str2, "Wireless charging switch not supported!");
                    hidl_reply.release();
                    return false;
                }
                hidl_reply.release();
                return true;
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("isWirelessChargingSupported transact failed. ");
            stringBuilder.append(e);
            Slog.e(str2, stringBuilder.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "Failed calling isWirelessChargingSupported!");
        return false;
    }

    public int setWirelessChargingEnabled(boolean enable) {
        String str = "vendor.xiaomi.hardware.wireless@1.0::IWirelessSwitch";
        String str2 = TAG;
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                hidl_request.writeBool(enable);
                hwService.transact(2, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                int val = hidl_reply.readInt32();
                StringBuilder stringBuilder;
                if (val == 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Set wireless charging switch to ");
                    stringBuilder.append(enable);
                    stringBuilder.append(" successfully.");
                    Slog.i(str2, stringBuilder.toString());
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Set wireless charging switch failed, err = ");
                    stringBuilder.append(val);
                    Slog.e(str2, stringBuilder.toString());
                }
                hidl_reply.release();
                return val;
            }
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("setWirelessChargingEnabled transact failed. ");
            stringBuilder2.append(e);
            Slog.e(str2, stringBuilder2.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "Failed calling setWirelessChargingEnabled!");
        return 1;
    }

    public int getWirelessChargingStatus() {
        String str = "vendor.xiaomi.hardware.wireless@1.0::IWirelessSwitch";
        String str2 = TAG;
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                hwService.transact(3, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                int val = hidl_reply.readInt32();
                StringBuilder stringBuilder;
                if (val == 0 || val == 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Current wireless charging status = ");
                    stringBuilder.append(val);
                    Slog.i(str2, stringBuilder.toString());
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid wireless charging status = ");
                    stringBuilder.append(val);
                    Slog.e(str2, stringBuilder.toString());
                }
                hidl_reply.release();
                return val;
            }
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("getWirelessChargingStatus transact failed. ");
            stringBuilder2.append(e);
            Slog.e(str2, stringBuilder2.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "Failed calling getWirelessChargingStatus!");
        return 3;
    }
}
