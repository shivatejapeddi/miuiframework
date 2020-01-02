package org.ifaa.android.manager;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.SystemProperties;

public abstract class IFAAManager {
    private static final int IFAA_VERSION_V2 = 2;
    private static final int IFAA_VERSION_V3 = 3;
    private static final int IFAA_VERSION_V4 = 4;
    static int sIfaaVer;
    static boolean sIsFod = SystemProperties.getBoolean("ro.hardware.fp.fod", false);

    public abstract String getDeviceModel();

    public abstract int getSupportBIOTypes(Context context);

    public abstract int getVersion();

    public native byte[] processCmd(Context context, byte[] bArr);

    public abstract int startBIOManager(Context context, int i);

    static {
        sIfaaVer = 1;
        if (VERSION.SDK_INT >= 28) {
            sIfaaVer = 4;
        } else if (sIsFod) {
            sIfaaVer = 3;
        } else if (VERSION.SDK_INT >= 24) {
            sIfaaVer = 2;
        } else {
            System.loadLibrary("teeclientjni");
        }
    }
}
