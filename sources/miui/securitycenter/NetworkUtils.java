package miui.securitycenter;

import android.accounts.GrantCredentialsPermissionActivity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.IConnectivityManager;
import android.net.IConnectivityManager.Stub;
import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.net.IpConfiguration.ProxySettings;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkStats;
import android.net.NetworkStats.Entry;
import android.net.StaticIpConfiguration;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.ActionListener;
import android.os.INetworkManagementService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.system.Os;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.PhoneConstants;
import dalvik.system.PathClassLoader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetworkUtils {
    private static final String TAG = "OverLayUtil";
    private static INetworkManagementService mNMService;
    private static ClassLoader mSystemServiceClassLoader;

    private NetworkUtils() {
    }

    public static void setMobileDataState(Context context, boolean mobileDataEnabled) {
        ((TelephonyManager) context.getSystemService("phone")).setDataEnabled(mobileDataEnabled);
    }

    public static void saveWifiConfiguration(Context context, InetAddress dns, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        if (context == null || dns == null || wifiConf == null) {
            Log.i(TAG, "saveWifiConfiguration:  invalidate parameter!");
            return;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager != null) {
            boolean isModified = false;
            StaticIpConfiguration config;
            if (wifiConf.getIpAssignment() == IpAssignment.STATIC) {
                config = wifiConf.getStaticIpConfiguration();
                InetAddress oldDns = null;
                if (config.dnsServers.size() > 0) {
                    oldDns = (InetAddress) config.dnsServers.get(0);
                }
                config.dnsServers.clear();
                config.dnsServers.add(dns);
                if (oldDns != null) {
                    config.dnsServers.add(oldDns);
                }
                isModified = true;
            } else if (wifiConf.getIpAssignment() == IpAssignment.DHCP) {
                config = new StaticIpConfiguration();
                DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
                if (dhcpInfo != null) {
                    config.ipAddress = new LinkAddress(android.net.NetworkUtils.intToInetAddress(dhcpInfo.ipAddress), android.net.NetworkUtils.netmaskIntToPrefixLength(dhcpInfo.netmask));
                    config.gateway = android.net.NetworkUtils.intToInetAddress(dhcpInfo.gateway);
                    config.dnsServers.add(dns);
                    try {
                        config.dnsServers.add(android.net.NetworkUtils.intToInetAddress(dhcpInfo.dns1));
                    } catch (Exception e) {
                        config.dnsServers.add(android.net.NetworkUtils.numericToInetAddress("8.8.8.8"));
                    }
                    wifiConf.setIpConfiguration(new IpConfiguration(IpAssignment.STATIC, ProxySettings.NONE, config, null));
                }
                isModified = true;
            }
            if (isModified) {
                wifiManager.save(wifiConf, new ActionListener() {
                    public void onSuccess() {
                        Log.i(NetworkUtils.TAG, "save  wifi configuration success!");
                    }

                    public void onFailure(int reason) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Failure to save wifi configuration! reason=");
                        stringBuilder.append(reason);
                        Log.i(NetworkUtils.TAG, stringBuilder.toString());
                    }
                });
            }
        }
    }

    public static String getMobileIface(Context context) {
        LinkProperties activeLink = null;
        try {
            activeLink = Stub.asInterface(ServiceManager.getService("connectivity")).getLinkPropertiesForType(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (activeLink != null) {
            return activeLink.getInterfaceName();
        }
        return "";
    }

    public static void vpnPrepareAndAuthorize(String packageName) {
        IConnectivityManager service = Stub.asInterface(ServiceManager.getService("connectivity"));
        try {
            int userId = UserHandle.myUserId();
            if (service.prepareVpn(null, packageName, userId)) {
                service.setVpnPackageAuthorization(packageName, userId, true);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "prepareAndAuthorize", e);
        }
    }

    public static boolean isVpnConnected() {
        boolean z = false;
        try {
            if (Stub.asInterface(ServiceManager.getService("connectivity")).getVpnConfig(UserHandle.myUserId()) != null) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            Log.e(TAG, "isVpnConnected", e);
            return false;
        }
    }

    public static NetworkStats getAdjustedNetworkStatsTethering() {
        String str = "an exception occurred!!";
        String str2 = TAG;
        NetworkStats networkStatsTethering = null;
        try {
            if (mNMService == null) {
                mNMService = INetworkManagementService.Stub.asInterface(ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE));
            }
            networkStatsTethering = mNMService.getNetworkStatsTethering(1);
            if (networkStatsTethering != null && networkStatsTethering.size() > 0) {
                try {
                    if (mSystemServiceClassLoader == null) {
                        String systemServerClasspath = Os.getenv("SYSTEMSERVERCLASSPATH");
                        if (systemServerClasspath != null) {
                            mSystemServiceClassLoader = new PathClassLoader(systemServerClasspath, ClassLoader.getSystemClassLoader());
                        } else {
                            mSystemServiceClassLoader = Thread.currentThread().getContextClassLoader();
                        }
                    }
                    Class.forName("com.android.server.NetPluginDelegate", false, mSystemServiceClassLoader).getMethod("getTetherStats", new Class[]{NetworkStats.class, NetworkStats.class, NetworkStats.class}).invoke(null, new Object[]{networkStatsTethering, null, null});
                } catch (ClassNotFoundException e) {
                } catch (Exception e2) {
                    Log.e(str2, str, e2);
                }
            }
        } catch (Exception e22) {
            Log.e(str2, str, e22);
        }
        return networkStatsTethering;
    }

    public static ArrayList<Map<String, String>> getNetworkStatsTethering() {
        ArrayList<Map<String, String>> result = null;
        NetworkStats networkStatsTethering = getAdjustedNetworkStatsTethering();
        Entry entry = null;
        if (networkStatsTethering != null) {
            result = new ArrayList();
            for (int i = 0; i < networkStatsTethering.size(); i++) {
                entry = networkStatsTethering.getValues(i, entry);
                Map<String, String> info = new HashMap();
                info.put(GrantCredentialsPermissionActivity.EXTRAS_REQUESTING_UID, String.valueOf(entry.uid));
                info.put(PhoneConstants.DATA_IFACE_NAME_KEY, entry.iface);
                info.put("rxBytes", String.valueOf(entry.rxBytes));
                info.put("txBytes", String.valueOf(entry.txBytes));
                info.put("tag", String.valueOf(entry.tag));
                result.add(info);
            }
        }
        return result;
    }
}
