package android.net.wifi;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.os.UserHandle;
import java.util.List;

public final class SupplicantStateTrackerInjector {
    private static int sNetid = -1;
    private static boolean sNetworksDisabledDuringConnect;

    public static void handleNetworkConnectionComplete() {
        sNetworksDisabledDuringConnect = false;
        sNetid = -1;
    }

    public static void handleNetworkConnectionFailure(Context context, List<WifiConfiguration> configs, int netid) {
        if (sNetworksDisabledDuringConnect && sNetid == netid) {
            sNetworksDisabledDuringConnect = false;
            sNetid = -1;
            if (netid != -1 && configs != null) {
                Parcelable network = null;
                for (WifiConfiguration config : configs) {
                    if (config.networkId == netid) {
                        network = config;
                        break;
                    }
                }
                if (network != null && network.status == 1) {
                    Intent intent = new Intent("miui.intent.action.WIFI_CONNECTION_FAILURE");
                    intent.addFlags(335544320);
                    intent.putExtra(WifiManager.EXTRA_WIFI_CONFIGURATION, network);
                    context.sendBroadcastAsUser(intent, UserHandle.ALL);
                }
            }
        }
    }

    public static void handleConnectNetwork(int netid) {
        sNetid = netid;
        sNetworksDisabledDuringConnect = true;
    }

    public static boolean isConformAuthFailure(int netid, int authenticationFailuresCount) {
        return authenticationFailuresCount > 0 && sNetworksDisabledDuringConnect && netid != -1 && sNetid == netid;
    }
}
