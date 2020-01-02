package miui.securitycenter.powercenter;

import android.accounts.GrantCredentialsPermissionActivity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class BatterySipper implements Comparable<BatterySipper> {
    static final int AMBIENT_DISPLAY = 11;
    static final int APP = 6;
    static final int BLUETOOTH = 4;
    static final int CAMERA = 9;
    static final int CELL = 1;
    static final int FLASHLIGHT = 7;
    static final int IDLE = 0;
    static final int OTHER = 10;
    static final int PHONE = 2;
    static final int SCREEN = 5;
    static final int USER = 8;
    static final int WIFI = 3;
    long cpuFgTime;
    long cpuTime;
    String defaultPackageName;
    int drainType;
    long gpsTime;
    long mobileRxBytes;
    long mobileTxBytes;
    String name;
    double noCoveragePercent;
    int uid = -1;
    long usageTime;
    double value;
    long wakeLockTime;
    long wifiRunningTime;
    long wifiRxBytes;
    long wifiTxBytes;

    public BatterySipper(Context context, int drainType, int uid, double value) {
        this.drainType = drainType;
        this.value = value;
        this.uid = uid;
        getNameAndPackageName(context);
    }

    public BatterySipper(Context context, String packageName, double value) {
        this.defaultPackageName = packageName;
        this.uid = 1000;
        this.drainType = 6;
        this.value = value;
        getOfficialName(context, packageName);
    }

    public double getSortValue() {
        return this.value;
    }

    public int compareTo(BatterySipper other) {
        return Double.compare(other.getSortValue(), getSortValue());
    }

    public Object getObjectValue(String key) {
        if (key.equals("name")) {
            return this.name;
        }
        if (key.equals(GrantCredentialsPermissionActivity.EXTRAS_REQUESTING_UID)) {
            return Integer.valueOf(this.uid);
        }
        if (key.equals("value")) {
            return Double.valueOf(this.value);
        }
        if (key.equals("drainType")) {
            return Integer.valueOf(this.drainType);
        }
        if (key.equals("usageTime")) {
            return Long.valueOf(this.usageTime);
        }
        if (key.equals("cpuTime")) {
            return Long.valueOf(this.cpuTime);
        }
        if (key.equals("gpsTime")) {
            return Long.valueOf(this.gpsTime);
        }
        if (key.equals("wifiRunningTime")) {
            return Long.valueOf(this.wifiRunningTime);
        }
        if (key.equals("cpuFgTime")) {
            return Long.valueOf(this.cpuFgTime);
        }
        if (key.equals("wakeLockTime")) {
            return Long.valueOf(this.wakeLockTime);
        }
        if (key.equals("mobileRxBytes")) {
            return Long.valueOf(this.mobileRxBytes);
        }
        if (key.equals("mobileTxBytes")) {
            return Long.valueOf(this.mobileTxBytes);
        }
        if (key.equals("noCoveragePercent")) {
            return Double.valueOf(this.noCoveragePercent);
        }
        if (key.equals("defaultPackageName")) {
            return this.defaultPackageName;
        }
        if (key.equals("wifiRxBytes")) {
            return Long.valueOf(this.wifiRxBytes);
        }
        if (key.equals("wifiTxBytes")) {
            return Long.valueOf(this.wifiTxBytes);
        }
        return null;
    }

    public int getDrainType() {
        return this.drainType;
    }

    public int getUid() {
        return this.uid;
    }

    public double getValue() {
        return this.value;
    }

    public String getPackageName() {
        return this.defaultPackageName;
    }

    private void getNameAndPackageName(Context context) {
        PackageManager pm = context.getPackageManager();
        String[] packages = pm.getPackagesForUid(this.uid);
        if (packages != null) {
            if (packages.length == 1) {
                try {
                    CharSequence cs = pm.getApplicationLabel(pm.getApplicationInfo(packages[0], 0));
                    if (cs != null) {
                        this.name = cs.toString();
                    }
                    this.defaultPackageName = packages[0];
                } catch (NameNotFoundException e) {
                }
            } else {
                int length = packages.length;
                int i = 0;
                while (i < length) {
                    String pkgName = packages[i];
                    try {
                        PackageInfo pi = pm.getPackageInfo(pkgName, 0);
                        if (pi.sharedUserLabel != 0) {
                            CharSequence cs2 = pm.getText(pkgName, pi.sharedUserLabel, pi.applicationInfo);
                            if (cs2 != null) {
                                this.name = cs2.toString();
                            }
                            this.defaultPackageName = pkgName;
                        } else {
                            i++;
                        }
                    } catch (NameNotFoundException e2) {
                    }
                }
            }
        }
    }

    private void getOfficialName(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        boolean find = false;
        try {
            CharSequence cs = pm.getApplicationLabel(pm.getApplicationInfo(pkgName, 0));
            if (cs != null) {
                this.name = cs.toString();
                find = true;
            }
        } catch (NameNotFoundException e) {
        }
        if (!find) {
            try {
                PackageInfo pi = pm.getPackageInfo(pkgName, 0);
                if (pi.sharedUserLabel != 0) {
                    CharSequence cs2 = pm.getText(pkgName, pi.sharedUserLabel, pi.applicationInfo);
                    if (cs2 != null) {
                        this.name = cs2.toString();
                        find = true;
                    }
                }
            } catch (NameNotFoundException e2) {
            }
        }
        if (!find) {
            this.name = this.defaultPackageName;
        }
    }
}
