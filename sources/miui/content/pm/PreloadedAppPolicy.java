package miui.content.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.IPackageInstallObserver2;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import miui.app.ToggleManager;
import miui.content.pm.IPreloadedAppManager.Stub;
import miui.os.Build;

public class PreloadedAppPolicy {
    private static ArrayList<String> sAllowDisableSystemApps = new ArrayList();
    private static ArrayList<String> sProtectedDataApps = new ArrayList();
    public final int INSTALL_FLAG_NEED_CONFIRM = 1;
    public final int INSTALL_FLAG_SHOW_TOAST = 2;

    static {
        if (VERSION.SDK_INT >= 23 && !Build.IS_INTERNATIONAL_BUILD) {
            sProtectedDataApps.add("com.xiaomi.pass");
            sProtectedDataApps.add("com.xiaomi.scanner");
            sProtectedDataApps.add("com.xiaomi.gamecenter");
            sProtectedDataApps.add("com.miui.weather2");
            sProtectedDataApps.add("com.miui.notes");
            sProtectedDataApps.add("com.miui.compass");
            sProtectedDataApps.add("com.miui.calculator");
            sProtectedDataApps.add("com.miui.screenrecorder");
            sProtectedDataApps.add("com.miui.cleanmaster");
            sProtectedDataApps.add("com.android.midrive");
            sProtectedDataApps.add("com.duokan.reader");
            sProtectedDataApps.add("com.mfashiongallery.emag");
            sProtectedDataApps.add("com.android.email");
            sProtectedDataApps.add("com.miui.virtualsim");
            sProtectedDataApps.add("com.xiaomi.gamecenter.pad");
            sProtectedDataApps.add("com.xiaomi.drivemode");
            sProtectedDataApps.add("com.miui.smarttravel");
            sAllowDisableSystemApps.add("com.miui.personalassistant");
            sAllowDisableSystemApps.add("com.miui.voip");
            sAllowDisableSystemApps.add("com.miui.yellowpage");
        } else if (Build.IS_INTERNATIONAL_BUILD) {
            sProtectedDataApps.add("com.facemoji.lite.xiaomi");
            sProtectedDataApps.add(ToggleManager.PKG_NAME_MIDROP);
        }
    }

    public static boolean isProtectedDataApp(Context context, String packageName, int flags) {
        return sProtectedDataApps.contains(packageName);
    }

    public static boolean isAllowDisableSystemApp(Context context, String packageName, int flags) {
        return sAllowDisableSystemApps.contains(packageName);
    }

    public static boolean installPreloadedDataApp(final Context context, final String packageName, final IPackageInstallObserver2 observer, final int flags) {
        if (TextUtils.isEmpty(packageName) || !sProtectedDataApps.contains(packageName)) {
            return false;
        }
        Intent intent = new Intent("com.xiaomi.market.PreloadedDataAppInstallService");
        intent.setPackage("com.xiaomi.market");
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentServices(intent, 0);
        if (resolveInfoList == null || resolveInfoList.isEmpty()) {
            return false;
        }
        context.bindService(intent, new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    Stub.asInterface(service).reinstallPreloadedApp2(packageName, new IPackageInstallObserver2.Stub() {
                        public void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) throws RemoteException {
                            context.unbindService(this);
                            if (observer != null) {
                                observer.onPackageInstalled(basePackageName, returnCode, msg, extras);
                            }
                        }

                        public void onUserActionRequired(Intent intent) {
                        }
                    }, flags);
                } catch (Exception e) {
                    Log.e(PreloadedAppPolicy.class.getName(), e.getMessage(), e);
                    context.unbindService(this);
                }
            }

            public void onServiceDisconnected(ComponentName name) {
            }
        }, 1);
        return true;
    }
}
