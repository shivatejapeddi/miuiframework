package android.net;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Deprecated
public class NetworkBadging {
    public static final int BADGING_4K = 30;
    public static final int BADGING_HD = 20;
    public static final int BADGING_NONE = 0;
    public static final int BADGING_SD = 10;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Badging {
    }

    private NetworkBadging() {
    }

    public static Drawable getWifiIcon(int signalLevel, int badging, Theme theme) {
        return Resources.getSystem().getDrawable(getWifiSignalResource(signalLevel), theme);
    }

    private static int getWifiSignalResource(int signalLevel) {
        if (signalLevel == 0) {
            return R.drawable.ic_wifi_signal_0;
        }
        if (signalLevel == 1) {
            return R.drawable.ic_wifi_signal_1;
        }
        if (signalLevel == 2) {
            return R.drawable.ic_wifi_signal_2;
        }
        if (signalLevel == 3) {
            return R.drawable.ic_wifi_signal_3;
        }
        if (signalLevel == 4) {
            return R.drawable.ic_wifi_signal_4;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid signal level: ");
        stringBuilder.append(signalLevel);
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
