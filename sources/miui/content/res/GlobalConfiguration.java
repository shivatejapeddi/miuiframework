package miui.content.res;

import android.app.ActivityManagerNative;
import android.content.res.Configuration;
import android.content.res.MiuiConfiguration;
import android.os.RemoteException;

@Deprecated
public class GlobalConfiguration {
    public static Configuration get() throws RemoteException {
        return ActivityManagerNative.getDefault().getConfiguration();
    }

    public static void update(Configuration configuration) throws RemoteException {
        ActivityManagerNative.getDefault().updateConfiguration(configuration);
    }

    public static MiuiConfiguration getExtraConfig(Configuration configuration) {
        return configuration.extraConfig;
    }
}
