package com.miui.enterprise;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.miui.enterprise.IEnterpriseManager.Stub;

public class EnterpriseManager {
    public static final String APN_MANAGER = "apn_manager";
    public static final String APPLICATION_MANAGER = "application_manager";
    public static final String DEVICE_MANAGER = "device_manager";
    public static final String PHONE_MANAGER = "phone_manager";
    public static final String RESTRICTIONS_MANAGER = "restrictions_manager";
    public static final String SERVICE_NAME = "EnterpriseManager";
    private static IEnterpriseManager sEnterpriseManager;

    private EnterpriseManager() {
    }

    public static IBinder getEnterpriseService(String serviceName) {
        IEnterpriseManager iEnterpriseManager = sEnterpriseManager;
        String str = SERVICE_NAME;
        if (iEnterpriseManager == null) {
            sEnterpriseManager = Stub.asInterface(ServiceManager.getService(str));
        }
        try {
            return sEnterpriseManager.getService(serviceName);
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to get enterprise service: ");
            stringBuilder.append(serviceName);
            Slog.e(str, stringBuilder.toString(), e);
            return null;
        }
    }
}
