package miui.security;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import java.util.List;
import miui.security.IAppRunningControlManager.Stub;

public class AppRunningControlManager {
    private static final String TAG = "AppRunningControlManager";
    private static AppRunningControlManager sInstance;
    private IAppRunningControlManager mService;

    private AppRunningControlManager(IBinder binder) {
        this.mService = Stub.asInterface(binder);
    }

    public static AppRunningControlManager getInstance() {
        if (sInstance == null) {
            try {
                IBinder greenKidBinder = ISecurityManager.Stub.asInterface(ServiceManager.getService(Context.SECURITY_SERVICE)).getAppRunningControlIBinder();
                if (greenKidBinder == null) {
                    Slog.d(TAG, "AppRunningControlIBinder is null");
                    return null;
                }
                sInstance = new AppRunningControlManager(greenKidBinder);
            } catch (Exception e) {
                throw new RuntimeException("system service died", e);
            }
        }
        return sInstance;
    }

    public void setDisallowRunningList(List<String> list, Intent intent) {
        try {
            this.mService.setDisallowRunningList(list, intent);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote service has died", e);
        }
    }

    private Intent getBlockActivityIntentInner(Context context, String packageName, Intent intent, boolean fromActivity, int requestCode) {
        try {
            return this.mService.getBlockActivityIntent(packageName, intent, fromActivity, requestCode);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote service has died", e);
            return null;
        }
    }

    public void setBlackListEnable(boolean isEnable) {
        try {
            this.mService.setBlackListEnable(isEnable);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote service has died", e);
        }
    }

    public static Intent getBlockActivityIntent(Context context, String packageName, Intent intent, boolean fromActivity, int requestCode) {
        AppRunningControlManager appRunningControlManager = getInstance();
        Intent result = null;
        if (appRunningControlManager != null) {
            result = appRunningControlManager.getBlockActivityIntentInner(context, packageName, intent, fromActivity, requestCode);
            if (result != null) {
                return result;
            }
        }
        return result;
    }

    public static boolean matchRule(String pkgName, int wakeType) {
        AppRunningControlManager appRunningControlManager = getInstance();
        if (appRunningControlManager != null) {
            return appRunningControlManager.matchRuleInner(pkgName, wakeType);
        }
        return false;
    }

    private boolean matchRuleInner(String pkgName, int wakeType) {
        try {
            return this.mService.matchRule(pkgName, wakeType);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote service has died", e);
            return false;
        }
    }

    public List<String> getNotDisallowList() {
        try {
            return this.mService.getNotDisallowList();
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote service has died", e);
            return null;
        }
    }
}
