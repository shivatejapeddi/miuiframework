package miui.util;

import android.os.HwBinder;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.util.Slog;

public class IOtgSwitch {
    private static final String DEFAULT = "default";
    private static final int GET_OTG_STATUS = 3;
    public static volatile IOtgSwitch INSTANCE = null;
    private static final String INTERFACE_DESCRIPTOR = "vendor.xiaomi.hardware.otgswitch@1.0::IOtgSwitch";
    private static final int IS_OTG_SUPPORTED = 1;
    public static final int OTG_DISABLED = 1;
    public static final int OTG_ENABLED = 0;
    public static final int OTG_NOT_SUPPORTED = 2;
    public static final int OTG_STATUS_UNKNOWN = 3;
    private static final String SERVICE_NAME = "vendor.xiaomi.hardware.otgswitch@1.0::IOtgSwitch";
    private static final int SET_OTG_ENABLED = 2;
    public static final int STATUS_BAD_VALUE = 3;
    public static final int STATUS_FAILURE_UNKNOWN = 1;
    public static final int STATUS_NOT_SUPPORTED = 2;
    public static final int STATUS_SUCCESS = 0;
    private static final String TAG = "IOtgSwitch";

    public static IOtgSwitch getInstance() {
        if (INSTANCE == null) {
            synchronized (IOtgSwitch.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IOtgSwitch();
                }
            }
        }
        return INSTANCE;
    }

    public boolean isOtgSupported() {
        String str = "vendor.xiaomi.hardware.otgswitch@1.0::IOtgSwitch";
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
                    Slog.e(str2, "OTG switch not supported!");
                    hidl_reply.release();
                    return false;
                }
                hidl_reply.release();
                return true;
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("transact failed. ");
            stringBuilder.append(e);
            Slog.e(str2, stringBuilder.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "Failed calling isOtgSupported!");
        return false;
    }

    public int setOtgEnabled(boolean enable) {
        String str = "vendor.xiaomi.hardware.otgswitch@1.0::IOtgSwitch";
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
                    stringBuilder.append("Set OTG switch to ");
                    stringBuilder.append(enable);
                    stringBuilder.append(" successfully.");
                    Slog.i(str2, stringBuilder.toString());
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Set OTG switch failed, err = ");
                    stringBuilder.append(val);
                    Slog.e(str2, stringBuilder.toString());
                }
                hidl_reply.release();
                return val;
            }
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("transact failed. ");
            stringBuilder2.append(e);
            Slog.e(str2, stringBuilder2.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "Failed calling setOtgEnabled!");
        return 1;
    }

    public int getOtgStatus() {
        String str = "vendor.xiaomi.hardware.otgswitch@1.0::IOtgSwitch";
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
                    stringBuilder.append("Current OTG status = ");
                    stringBuilder.append(val);
                    Slog.i(str2, stringBuilder.toString());
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid OTG status = ");
                    stringBuilder.append(val);
                    Slog.e(str2, stringBuilder.toString());
                }
                hidl_reply.release();
                return val;
            }
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("transact failed. ");
            stringBuilder2.append(e);
            Slog.e(str2, stringBuilder2.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "Failed calling getOtgStatus!");
        return 3;
    }
}
