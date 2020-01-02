package miui.maml.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageInstallObserver2.Stub;
import android.os.Bundle;
import android.os.RemoteException;
import miui.content.pm.PreloadedAppPolicy;

public class PreloadAppPolicyHelper {
    public static boolean installPreloadedDataApp(final Context context, String packageName, final Intent intent, final Bundle bundle) {
        return PreloadedAppPolicy.installPreloadedDataApp(context, packageName, new Stub() {
            public void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) throws RemoteException {
                Utils.startActivity(context, intent, bundle);
            }

            public void onUserActionRequired(Intent intent) {
            }
        }, 1);
    }
}
