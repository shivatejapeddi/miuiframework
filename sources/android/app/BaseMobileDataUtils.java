package android.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.Settings.Global;
import android.telephony.TelephonyManager;

public class BaseMobileDataUtils {
    public Uri getMobileDataUri() {
        return Global.getUriFor(Global.MOBILE_DATA);
    }

    public boolean isMobileEnable(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getMobileDataEnabled();
    }

    public String getSubscriberId(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
    }

    public void onMobileDataChange(Context context) {
    }
}
