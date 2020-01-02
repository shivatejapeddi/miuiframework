package miui.securitycenter.net;

import android.accounts.GrantCredentialsPermissionActivity;
import android.content.Context;
import android.net.NetworkStats;
import android.net.NetworkStats.Entry;
import android.net.TrafficStats;
import android.os.INetworkManagementService;
import android.os.INetworkManagementService.Stub;
import android.os.ServiceManager;
import com.android.internal.telephony.PhoneConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import miui.securitycenter.NetworkUtils;

public class NetworkStatWrapper {
    private static INetworkManagementService mNMService;
    private static NetworkStats mPreSnapshot = null;
    private static ArrayList<Map<String, String>> mStatsInfo = new ArrayList();

    private NetworkStatWrapper() {
    }

    public static ArrayList<Map<String, String>> getStatsInfo() {
        try {
            NetworkStats newSnapshot = getNetworkStatsDetail();
            if (newSnapshot == null) {
                return mStatsInfo;
            }
            NetworkStats newTetheringSnapshot = NetworkUtils.getAdjustedNetworkStatsTethering();
            if (newTetheringSnapshot != null && newTetheringSnapshot.size() > 0) {
                newSnapshot.combineAllValues(newTetheringSnapshot);
            }
            if (mPreSnapshot == null) {
                mPreSnapshot = newSnapshot;
            } else {
                mStatsInfo.clear();
                NetworkStats delta = NetworkStats.subtract(newSnapshot, mPreSnapshot, null, null);
                mPreSnapshot = newSnapshot;
                Entry entry = null;
                if (delta != null) {
                    for (int i = 0; i < delta.size(); i++) {
                        entry = delta.getValues(i, entry);
                        Map<String, String> info = new HashMap();
                        info.put(GrantCredentialsPermissionActivity.EXTRAS_REQUESTING_UID, String.valueOf(entry.uid));
                        info.put(PhoneConstants.DATA_IFACE_NAME_KEY, entry.iface);
                        info.put("rxBytes", String.valueOf(entry.rxBytes));
                        info.put("txBytes", String.valueOf(entry.txBytes));
                        info.put("tag", String.valueOf(entry.tag));
                        mStatsInfo.add(info);
                    }
                }
            }
            return mStatsInfo;
        } catch (Exception e) {
        }
    }

    private static NetworkStats getNetworkStatsDetail() {
        try {
            if (mNMService == null) {
                mNMService = Stub.asInterface(ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE));
            }
            return mNMService.getNetworkStatsDetail();
        } catch (Exception e) {
            return null;
        }
    }

    public static long getTxBytes(String iface) {
        return TrafficStats.getTxBytes(iface);
    }

    public static long getRxBytes(String iface) {
        return TrafficStats.getRxBytes(iface);
    }
}
