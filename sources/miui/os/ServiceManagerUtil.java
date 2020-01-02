package miui.os;

import android.os.IBinder;
import android.os.ServiceManager;

@Deprecated
public class ServiceManagerUtil {
    public static IBinder getService(String name) {
        return ServiceManager.getService(name);
    }
}
