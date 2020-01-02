package com.miui.whetstone;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.os.RemoteException;
import com.miui.whetstone.IWhetstone.Stub;
import java.util.List;

public class WhetstoneServiceNative extends Stub {
    public int registerMiuiWhetstoneCloudSync(ComponentName component, CloudControlInfo info) throws RemoteException {
        return 0;
    }

    public int registerMiuiWhetstoneCloudSyncList(ComponentName component, List<CloudControlInfo> list) throws RemoteException {
        return 0;
    }

    public int unregisterMiuiWhetstoneCloudSync(ComponentName component) throws RemoteException {
        return 0;
    }

    public void recordRTCWakeupInfo(int uid, PendingIntent operation, boolean status) {
    }

    public void log(int tag, byte[] data) throws RemoteException {
    }
}
