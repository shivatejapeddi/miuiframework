package miui.securitycenter.utils;

import android.content.Context;
import android.content.Intent;
import android.net.IConnectivityManager;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.os.INetworkManagementService.Stub;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

public class MiAssistantUtil {
    private static final String EXTRA_LINK_PROPERTIES = "linkProperties";
    private static final String EXTRA_NETWORK_INFO = "networkinfo";
    private static final String INTERFACE_USBNET0 = "usbnet0";
    private static final String TAG = "MiAssistantManager";
    private static final String USB_SHARE_NET_STATE_CHANGE = "miui.intent.action.USB_SHARE_NET_STATE_CHANGE";

    private MiAssistantUtil() {
    }

    public static void usbnet0Down(Context context) {
        Intent intent = new Intent(USB_SHARE_NET_STATE_CHANGE);
        Parcelable info = new NetworkInfo(9, 0, "ETHERNET", "");
        info.setIsAvailable(false);
        info.setDetailedState(DetailedState.DISCONNECTED, null, null);
        intent.putExtra(EXTRA_NETWORK_INFO, info);
        intent.putExtra("linkProperties", new LinkProperties());
        context.sendBroadcast(intent);
        try {
            Stub.asInterface(ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE)).setInterfaceDown(INTERFACE_USBNET0);
        } catch (Exception e) {
            Log.w(TAG, "disable usbnet0 error");
        }
    }

    public static String getActiveInterfaceName() {
        LinkProperties activeLink = null;
        try {
            activeLink = IConnectivityManager.Stub.asInterface(ServiceManager.getService("connectivity")).getActiveLinkProperties();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (activeLink != null) {
            return activeLink.getInterfaceName();
        }
        Log.e(TAG, "activeLink is null");
        return "null";
    }
}
