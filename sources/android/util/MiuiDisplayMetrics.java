package android.util;

import android.content.res.Resources;
import android.os.SystemProperties;

public final class MiuiDisplayMetrics {
    @Deprecated
    public static int DENSITY_DEVICE = getMiuiDeviceDensity();
    public static final int DENSITY_NXHGITH = 440;
    public static final String PROP_MIUI_DENSITY = "persist.miui.density_v2";

    public static final int getNxhdpiDensity() {
        return 440;
    }

    public static int getFactoryDeviceDensity() {
        return SystemProperties.getInt("ro.sf.lcd_density", 160);
    }

    private static int getMiuiDeviceDensity() {
        return SystemProperties.getInt(PROP_MIUI_DENSITY, getFactoryDeviceDensity());
    }

    public static boolean setOverlayDensity(int overlayDensity) {
        int factoryDensity = getFactoryDeviceDensity();
        boolean z = false;
        if (((float) factoryDensity) / 1.5f > ((float) overlayDensity) || ((double) overlayDensity) > ((double) factoryDensity) * 1.5d || Resources.getSystem().getDisplayMetrics().densityDpi == overlayDensity) {
            return false;
        }
        String valueOf = String.valueOf(overlayDensity);
        String str = PROP_MIUI_DENSITY;
        SystemProperties.set(str, valueOf);
        if (overlayDensity == SystemProperties.getInt(str, -1)) {
            z = true;
        }
        return z;
    }
}
