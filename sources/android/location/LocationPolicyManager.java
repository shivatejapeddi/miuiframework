package android.location;

import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.os.UserHandle;
import java.io.PrintWriter;

public class LocationPolicyManager {
    private static final boolean ALLOW_PLATFORM_APP_POLICY = true;
    public static final int OP_BLE_SCAN_LOCATION = 3;
    public static final int OP_GET_CELL_LOCATION = 1;
    public static final int OP_GET_GPS_LOCATION = 0;
    public static final int OP_WIFI_SCAN_LOCATION = 2;
    public static final int POLICY_NONE = 0;
    public static final int POLICY_REJECT_ALL_BACKGROUND = 255;
    public static final int POLICY_REJECT_HIGH_POWER_BACKGROUND = 1;
    public static final int POLICY_REJECT_NON_PASSIVE_BACKGROUND = 2;
    public static final int RULE_ALLOW_ALL = 0;
    public static final int RULE_REJECT_ALL = 255;
    public static final int RULE_REJECT_HIGH_POWER = 1;
    public static final int RULE_REJECT_NON_PASSIVE = 2;
    private static LocationPolicyManager sInstance = null;
    private ILocationPolicyManager mService;

    public LocationPolicyManager(ILocationPolicyManager service) {
        if (service != null) {
            this.mService = service;
            return;
        }
        throw new IllegalArgumentException("missing ILocationPolicyManager");
    }

    public static LocationPolicyManager from(Context context) {
        return (LocationPolicyManager) context.getSystemService(Context.LOCATION_POLICY_SERVICE);
    }

    public static boolean isAllowedByLocationPolicy(Context context, int op) {
        if (sInstance == null) {
            sInstance = from(context);
        }
        return sInstance.checkUidLocationOp(Binder.getCallingUid(), op);
    }

    public static boolean isAllowedByLocationPolicy(Context context, int uid, int op) {
        if (sInstance == null) {
            sInstance = from(context);
        }
        return sInstance.checkUidLocationOp(uid, op);
    }

    public void setUidPolicy(int uid, int policy) {
        try {
            this.mService.setUidPolicy(uid, policy);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int getUidPolicy(int uid) {
        try {
            return this.mService.getUidPolicy(uid);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setUidNavigationStart(int uid) {
        try {
            this.mService.setUidNavigationStart(uid);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setUidNavigationStop(int uid) {
        try {
            this.mService.setUidNavigationStop(uid);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUidNavigationScreenLock(int uid) {
        try {
            return this.mService.checkUidNavigationScreenLock(uid);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkUidLocationOp(int uid, int op) {
        try {
            return this.mService.checkUidLocationOp(uid, op);
        } catch (RemoteException e) {
            e.printStackTrace();
            return true;
        }
    }

    public int[] getUidsWithPolicy(int policy) {
        try {
            return this.mService.getUidsWithPolicy(policy);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new int[0];
        }
    }

    public void registerListener(ILocationPolicyListener listener) {
        try {
            this.mService.registerListener(listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unregisterListener(ILocationPolicyListener listener) {
        try {
            this.mService.unregisterListener(listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public LocationPolicy[] getLocationPolicies() {
        try {
            return this.mService.getLocationPolicies();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setLocationPolicies(LocationPolicy[] policies) {
        try {
            this.mService.setLocationPolicies(policies);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setRestrictBackground(boolean restrictBackground) {
        try {
            this.mService.setRestrictBackground(restrictBackground);
        } catch (RemoteException e) {
        }
    }

    public boolean getRestrictBackground() {
        try {
            return this.mService.getRestrictBackground();
        } catch (RemoteException e) {
            return false;
        }
    }

    public static boolean isUidValidForPolicy(Context context, int uid) {
        if (UserHandle.isApp(uid)) {
            return true;
        }
        return false;
    }

    public static void dumpPolicy(PrintWriter fout, int policy) {
        fout.write("[");
        if ((policy & 255) != 0) {
            fout.write("REJECT_ALL_BACKGROUND");
        }
        if ((policy & 1) != 0) {
            fout.write("REJECT_HIGH_POWER_BACKGROUND");
        }
        if ((policy & 2) != 0) {
            fout.write("REJECT_NON_PASSIVE_BACKGROUND");
        }
        fout.write("]");
    }

    public static void dumpRules(PrintWriter fout, int rules) {
        fout.write("[");
        if ((rules & 255) != 0) {
            fout.write("REJECT_ALL");
        }
        if ((rules & 1) != 0) {
            fout.write("REJECT_HIGH_POWER");
        }
        if ((rules & 2) != 0) {
            fout.write("REJECT_NON_PASSIVE");
        }
        fout.write("]");
    }

    public void setFakeGpsFeatureOnState(boolean on) {
        try {
            this.mService.setFakeGpsFeatureOnState(on);
        } catch (RemoteException e) {
        }
    }

    public void setPhoneStationary(boolean stationary, Location location) {
        try {
            this.mService.setPhoneStationary(stationary, location);
        } catch (RemoteException e) {
        }
    }
}
