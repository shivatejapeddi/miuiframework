package miui.os;

import android.content.Context;
import android.os.IPowerManager;
import android.os.IPowerManager.Stub;
import android.os.RemoteException;
import android.os.ServiceManager;

@Deprecated
public class MiuiPowerManager {
    public static void reboot(boolean confim, String reason, boolean wait) {
        try {
            IPowerManager powermanager = Stub.asInterface(ServiceManager.getService(Context.POWER_SERVICE));
            if (powermanager != null) {
                powermanager.reboot(confim, reason, wait);
            }
        } catch (RemoteException e) {
        }
    }
}
