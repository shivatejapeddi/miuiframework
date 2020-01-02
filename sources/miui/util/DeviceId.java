package miui.util;

import android.text.TextUtils;
import miui.net.ConnectivityHelper;
import miui.telephony.TelephonyManagerUtil;

public abstract class DeviceId {
    private DeviceId() {
    }

    public static String get() {
        String str = "";
        str = TelephonyManagerUtil.getDeviceId();
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        if (ConnectivityHelper.getInstance().isWifiOnly()) {
            str = ConnectivityHelper.getInstance().getMacAddress();
        }
        if (str == null) {
            str = "";
        }
        return str;
    }
}
