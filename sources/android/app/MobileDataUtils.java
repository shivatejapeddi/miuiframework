package android.app;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.telephony.TelephonyManager;

public class MobileDataUtils extends BaseMobileDataUtils {
    public static MobileDataUtils getInstance() {
        try {
            Class<?> mobileDataUtilsFactory = Class.forName("miui.msim.util.MSimMobileDataUtils");
            if (mobileDataUtilsFactory != null) {
                return (MobileDataUtils) mobileDataUtilsFactory.newInstance();
            }
        } catch (Exception e) {
        }
        return new MobileDataUtils();
    }

    public void registerContentObserver(Context context, ContentObserver observer) {
        context.getContentResolver().registerContentObserver(getMobileDataUri(), false, observer);
        TelephonyManager telephony = (TelephonyManager) context.getSystemService("phone");
        for (int i = 0; i < telephony.getPhoneCount(); i++) {
            context.getContentResolver().registerContentObserver(getMobileDataUri(i), false, observer);
        }
    }

    public Uri getMobileDataUri(int subId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Global.MOBILE_DATA);
        stringBuilder.append(subId);
        return Global.getUriFor(stringBuilder.toString());
    }

    public void enableMobileData(Context context, boolean isEnable) {
        ((TelephonyManager) context.getSystemService("phone")).setDataEnabled(isEnable);
    }

    public boolean isMobileEnable(Context context) {
        if (VERSION.SDK_INT > 23) {
            return ((TelephonyManager) context.getSystemService("phone")).getDataEnabled();
        }
        return super.isMobileEnable(context);
    }
}
