package android.telephony;

import android.Manifest.permission;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.UserInfo;
import android.location.LocationManager;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Binder;
import android.os.Build;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.widget.Toast;

public final class LocationAccessPolicy {
    private static final boolean DBG = false;
    public static final int MAX_SDK_FOR_ANY_ENFORCEMENT = 10000;
    private static final String TAG = "LocationAccessPolicy";

    public static class LocationPermissionQuery {
        public final String callingPackage;
        public final int callingPid;
        public final int callingUid;
        public final boolean logAsInfo;
        public final String method;
        public final int minSdkVersionForCoarse;
        public final int minSdkVersionForFine;

        public static class Builder {
            private String mCallingPackage;
            private int mCallingPid;
            private int mCallingUid;
            private boolean mLogAsInfo = false;
            private String mMethod;
            private int mMinSdkVersionForCoarse = Integer.MAX_VALUE;
            private int mMinSdkVersionForFine = Integer.MAX_VALUE;

            public Builder setCallingPackage(String callingPackage) {
                this.mCallingPackage = callingPackage;
                return this;
            }

            public Builder setCallingUid(int callingUid) {
                this.mCallingUid = callingUid;
                return this;
            }

            public Builder setCallingPid(int callingPid) {
                this.mCallingPid = callingPid;
                return this;
            }

            public Builder setMinSdkVersionForCoarse(int minSdkVersionForCoarse) {
                this.mMinSdkVersionForCoarse = minSdkVersionForCoarse;
                return this;
            }

            public Builder setMinSdkVersionForFine(int minSdkVersionForFine) {
                this.mMinSdkVersionForFine = minSdkVersionForFine;
                return this;
            }

            public Builder setMethod(String method) {
                this.mMethod = method;
                return this;
            }

            public Builder setLogAsInfo(boolean logAsInfo) {
                this.mLogAsInfo = logAsInfo;
                return this;
            }

            public LocationPermissionQuery build() {
                return new LocationPermissionQuery(this.mCallingPackage, this.mCallingUid, this.mCallingPid, this.mMinSdkVersionForCoarse, this.mMinSdkVersionForFine, this.mLogAsInfo, this.mMethod);
            }
        }

        private LocationPermissionQuery(String callingPackage, int callingUid, int callingPid, int minSdkVersionForCoarse, int minSdkVersionForFine, boolean logAsInfo, String method) {
            this.callingPackage = callingPackage;
            this.callingUid = callingUid;
            this.callingPid = callingPid;
            this.minSdkVersionForCoarse = minSdkVersionForCoarse;
            this.minSdkVersionForFine = minSdkVersionForFine;
            this.logAsInfo = logAsInfo;
            this.method = method;
        }
    }

    public enum LocationPermissionResult {
        ALLOWED,
        DENIED_SOFT,
        DENIED_HARD
    }

    private static void logError(Context context, LocationPermissionQuery query, String errorMsg) {
        boolean z = query.logAsInfo;
        String str = TAG;
        if (z) {
            Log.i(str, errorMsg);
            return;
        }
        Log.e(str, errorMsg);
        try {
            if (Build.IS_DEBUGGABLE) {
                Toast.makeText(context, (CharSequence) errorMsg, 0).show();
            }
        } catch (Throwable th) {
        }
    }

    private static LocationPermissionResult appOpsModeToPermissionResult(int appOpsMode) {
        if (appOpsMode == 0) {
            return LocationPermissionResult.ALLOWED;
        }
        if (appOpsMode != 2) {
            return LocationPermissionResult.DENIED_SOFT;
        }
        return LocationPermissionResult.DENIED_HARD;
    }

    private static LocationPermissionResult checkAppLocationPermissionHelper(Context context, LocationPermissionQuery query, String permissionToCheck) {
        int appOpMode = permission.ACCESS_FINE_LOCATION;
        String locationTypeForLog = appOpMode.equals(permissionToCheck) ? "fine" : "coarse";
        StringBuilder stringBuilder;
        if (checkManifestPermission(context, query.callingPid, query.callingUid, permissionToCheck)) {
            appOpMode = ((AppOpsManager) context.getSystemService(AppOpsManager.class)).noteOpNoThrow(AppOpsManager.permissionToOpCode(permissionToCheck), query.callingUid, query.callingPackage);
            if (appOpMode == 0) {
                return LocationPermissionResult.ALLOWED;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(query.callingPackage);
            stringBuilder.append(" is aware of ");
            stringBuilder.append(locationTypeForLog);
            stringBuilder.append(" but the app-ops permission is specifically denied.");
            Log.i(TAG, stringBuilder.toString());
            return appOpsModeToPermissionResult(appOpMode);
        }
        appOpMode = appOpMode.equals(permissionToCheck) ? query.minSdkVersionForFine : query.minSdkVersionForCoarse;
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        String str2 = "Allowing ";
        if (appOpMode > 10000) {
            String errorMsg = new StringBuilder();
            errorMsg.append(str2);
            errorMsg.append(query.callingPackage);
            errorMsg.append(str);
            errorMsg.append(locationTypeForLog);
            errorMsg.append(" because we're not enforcing API ");
            errorMsg.append(appOpMode);
            errorMsg.append(" yet. Please fix this app because it will break in the future. Called from ");
            errorMsg.append(query.method);
            logError(context, query, errorMsg.toString());
            return null;
        } else if (isAppAtLeastSdkVersion(context, query.callingPackage, appOpMode)) {
            return LocationPermissionResult.DENIED_HARD;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(query.callingPackage);
            stringBuilder.append(str);
            stringBuilder.append(locationTypeForLog);
            stringBuilder.append(" because it doesn't target API ");
            stringBuilder.append(appOpMode);
            stringBuilder.append(" yet. Please fix this app. Called from ");
            stringBuilder.append(query.method);
            logError(context, query, stringBuilder.toString());
            return null;
        }
    }

    public static LocationPermissionResult checkLocationPermission(Context context, LocationPermissionQuery query) {
        if (query.callingUid == 1001 || query.callingUid == 1000 || query.callingUid == 0) {
            return LocationPermissionResult.ALLOWED;
        }
        if (!checkSystemLocationAccess(context, query.callingUid, query.callingPid)) {
            return LocationPermissionResult.DENIED_SOFT;
        }
        LocationPermissionResult resultForFine;
        if (query.minSdkVersionForFine < Integer.MAX_VALUE) {
            resultForFine = checkAppLocationPermissionHelper(context, query, permission.ACCESS_FINE_LOCATION);
            if (resultForFine != null) {
                return resultForFine;
            }
        }
        if (query.minSdkVersionForCoarse < Integer.MAX_VALUE) {
            resultForFine = checkAppLocationPermissionHelper(context, query, permission.ACCESS_COARSE_LOCATION);
            if (resultForFine != null) {
                return resultForFine;
            }
        }
        return LocationPermissionResult.ALLOWED;
    }

    private static boolean checkManifestPermission(Context context, int pid, int uid, String permissionToCheck) {
        return context.checkPermission(permissionToCheck, pid, uid) == 0;
    }

    private static boolean checkSystemLocationAccess(Context context, int uid, int pid) {
        boolean z = false;
        if (!isLocationModeEnabled(context, UserHandle.getUserId(uid))) {
            return false;
        }
        if (isCurrentProfile(context, uid) || checkInteractAcrossUsersFull(context, uid, pid)) {
            z = true;
        }
        return z;
    }

    private static boolean isLocationModeEnabled(Context context, int userId) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LocationManager.class);
        if (locationManager != null) {
            return locationManager.isLocationEnabledForUser(UserHandle.of(userId));
        }
        Log.w(TAG, "Couldn't get location manager, denying location access");
        return false;
    }

    private static boolean checkInteractAcrossUsersFull(Context context, int pid, int uid) {
        return checkManifestPermission(context, pid, uid, permission.INTERACT_ACROSS_USERS_FULL);
    }

    private static boolean isCurrentProfile(Context context, int uid) {
        long token = Binder.clearCallingIdentity();
        try {
            int currentUser = ActivityManager.getCurrentUser();
            int callingUserId = UserHandle.getUserId(uid);
            boolean z = true;
            if (callingUserId == currentUser) {
                return z;
            }
            for (UserInfo user : ((UserManager) context.getSystemService(UserManager.class)).getProfiles(currentUser)) {
                if (user.id == callingUserId) {
                    Binder.restoreCallingIdentity(token);
                    return z;
                }
            }
            Binder.restoreCallingIdentity(token);
            return false;
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    private static boolean isAppAtLeastSdkVersion(Context context, String pkgName, int sdkVersion) {
        try {
            if (context.getPackageManager().getApplicationInfo(pkgName, 0).targetSdkVersion >= sdkVersion) {
                return true;
            }
        } catch (NameNotFoundException e) {
        }
        return false;
    }
}
