package miui.util;

import android.os.HwBinder;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.util.Slog;

public class IMiOob {
    private static final String DEFAULT = "default";
    private static volatile IMiOob INSTANCE = null;
    private static final String INTERFACE_DESCRIPTOR = "vendor.xiaomi.hardware.mioob@1.0::IMiOob";
    private static final int MIOOB_SET_BT_STATE = 1;
    private static final int MIOOB_SET_RX_CR = 2;
    private static final String SERVICE_NAME = "vendor.xiaomi.hardware.mioob@1.0::IMiOob";
    public static final int STATUS_SUCCESS = 0;
    private static final String TAG = "IMiOob";

    public static IMiOob getInstance() {
        if (INSTANCE == null) {
            synchronized (IMiOob.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IMiOob();
                }
            }
        }
        return INSTANCE;
    }

    public boolean setBtState(int state) {
        String str = "vendor.xiaomi.hardware.mioob@1.0::IMiOob";
        String str2 = TAG;
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                hidl_request.writeInt32(state);
                hwService.transact(1, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                if (hidl_reply.readInt32() != 0) {
                    Slog.e(str2, "set bt state failed!");
                    hidl_reply.release();
                    return false;
                }
                hidl_reply.release();
                return true;
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setBtState transact failed. ");
            stringBuilder.append(e);
            Slog.e(str2, stringBuilder.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "Failed calling setBtState!");
        return false;
    }

    public boolean setRxCr(String cr) {
        String str = "vendor.xiaomi.hardware.mioob@1.0::IMiOob";
        String str2 = TAG;
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                hidl_request.writeString(cr);
                hwService.transact(2, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                int val = hidl_reply.readInt32();
                StringBuilder stringBuilder;
                if (val != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Set mioob charging switch failed, err = ");
                    stringBuilder.append(val);
                    Slog.e(str2, stringBuilder.toString());
                    hidl_reply.release();
                    return false;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Mioob set rx cr: ");
                stringBuilder.append(cr);
                stringBuilder.append(" successfully.");
                Slog.i(str2, stringBuilder.toString());
                hidl_reply.release();
                return true;
            }
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("setRxCr transact failed. ");
            stringBuilder2.append(e);
            Slog.e(str2, stringBuilder2.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "Failed calling setRxCr!");
        return false;
    }
}
