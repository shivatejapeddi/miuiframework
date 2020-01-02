package android.net.wifi;

import android.annotation.UnsupportedAppUsage;
import android.content.pm.ParceledListSlice;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.wifi.hotspot2.IProvisioningCallback;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.WorkSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IWifiManager extends IInterface {

    public static class Default implements IWifiManager {
        public long getSupportedFeatures() throws RemoteException {
            return 0;
        }

        public WifiActivityEnergyInfo reportActivityInfo() throws RemoteException {
            return null;
        }

        public void requestActivityInfo(ResultReceiver result) throws RemoteException {
        }

        public ParceledListSlice getConfiguredNetworks(String packageName) throws RemoteException {
            return null;
        }

        public ParceledListSlice getPrivilegedConfiguredNetworks(String packageName) throws RemoteException {
            return null;
        }

        public Map getAllMatchingFqdnsForScanResults(List<ScanResult> list) throws RemoteException {
            return null;
        }

        public Map getMatchingOsuProviders(List<ScanResult> list) throws RemoteException {
            return null;
        }

        public Map getMatchingPasspointConfigsForOsuProviders(List<OsuProvider> list) throws RemoteException {
            return null;
        }

        public int addOrUpdateNetwork(WifiConfiguration config, String packageName) throws RemoteException {
            return 0;
        }

        public boolean addOrUpdatePasspointConfiguration(PasspointConfiguration config, String packageName) throws RemoteException {
            return false;
        }

        public boolean removePasspointConfiguration(String fqdn, String packageName) throws RemoteException {
            return false;
        }

        public List<PasspointConfiguration> getPasspointConfigurations(String packageName) throws RemoteException {
            return null;
        }

        public List<WifiConfiguration> getWifiConfigsForPasspointProfiles(List<String> list) throws RemoteException {
            return null;
        }

        public void queryPasspointIcon(long bssid, String fileName) throws RemoteException {
        }

        public int matchProviderWithCurrentNetwork(String fqdn) throws RemoteException {
            return 0;
        }

        public void deauthenticateNetwork(long holdoff, boolean ess) throws RemoteException {
        }

        public boolean removeNetwork(int netId, String packageName) throws RemoteException {
            return false;
        }

        public boolean enableNetwork(int netId, boolean disableOthers, String packageName) throws RemoteException {
            return false;
        }

        public boolean disableNetwork(int netId, String packageName) throws RemoteException {
            return false;
        }

        public boolean startScan(String packageName) throws RemoteException {
            return false;
        }

        public List<ScanResult> getScanResults(String callingPackage) throws RemoteException {
            return null;
        }

        public boolean disconnect(String packageName) throws RemoteException {
            return false;
        }

        public boolean reconnect(String packageName) throws RemoteException {
            return false;
        }

        public boolean reassociate(String packageName) throws RemoteException {
            return false;
        }

        public WifiInfo getConnectionInfo(String callingPackage) throws RemoteException {
            return null;
        }

        public boolean setWifiEnabled(String packageName, boolean enable) throws RemoteException {
            return false;
        }

        public int getWifiEnabledState() throws RemoteException {
            return 0;
        }

        public void setCountryCode(String country) throws RemoteException {
        }

        public String getCountryCode() throws RemoteException {
            return null;
        }

        public boolean isDualBandSupported() throws RemoteException {
            return false;
        }

        public boolean needs5GHzToAnyApBandConversion() throws RemoteException {
            return false;
        }

        public DhcpInfo getDhcpInfo() throws RemoteException {
            return null;
        }

        public boolean isScanAlwaysAvailable() throws RemoteException {
            return false;
        }

        public boolean acquireWifiLock(IBinder lock, int lockType, String tag, WorkSource ws) throws RemoteException {
            return false;
        }

        public void updateWifiLockWorkSource(IBinder lock, WorkSource ws) throws RemoteException {
        }

        public boolean releaseWifiLock(IBinder lock) throws RemoteException {
            return false;
        }

        public void initializeMulticastFiltering() throws RemoteException {
        }

        public boolean isMulticastEnabled() throws RemoteException {
            return false;
        }

        public void acquireMulticastLock(IBinder binder, String tag) throws RemoteException {
        }

        public void releaseMulticastLock(String tag) throws RemoteException {
        }

        public void updateInterfaceIpState(String ifaceName, int mode) throws RemoteException {
        }

        public boolean startSoftAp(WifiConfiguration wifiConfig) throws RemoteException {
            return false;
        }

        public boolean stopSoftAp() throws RemoteException {
            return false;
        }

        public int startLocalOnlyHotspot(Messenger messenger, IBinder binder, String packageName) throws RemoteException {
            return 0;
        }

        public void stopLocalOnlyHotspot() throws RemoteException {
        }

        public void startWatchLocalOnlyHotspot(Messenger messenger, IBinder binder) throws RemoteException {
        }

        public void stopWatchLocalOnlyHotspot() throws RemoteException {
        }

        public int getWifiApEnabledState() throws RemoteException {
            return 0;
        }

        public WifiConfiguration getWifiApConfiguration() throws RemoteException {
            return null;
        }

        public boolean setWifiApConfiguration(WifiConfiguration wifiConfig, String packageName) throws RemoteException {
            return false;
        }

        public void notifyUserOfApBandConversion(String packageName) throws RemoteException {
        }

        public Messenger getWifiServiceMessenger(String packageName) throws RemoteException {
            return null;
        }

        public void enableTdls(String remoteIPAddress, boolean enable) throws RemoteException {
        }

        public void enableTdlsWithMacAddress(String remoteMacAddress, boolean enable) throws RemoteException {
        }

        public String getCurrentNetworkWpsNfcConfigurationToken() throws RemoteException {
            return null;
        }

        public void enableVerboseLogging(int verbose) throws RemoteException {
        }

        public int getVerboseLoggingLevel() throws RemoteException {
            return 0;
        }

        public void enableWifiConnectivityManager(boolean enabled) throws RemoteException {
        }

        public void disableEphemeralNetwork(String SSID, String packageName) throws RemoteException {
        }

        public void factoryReset(String packageName) throws RemoteException {
        }

        public Network getCurrentNetwork() throws RemoteException {
            return null;
        }

        public byte[] retrieveBackupData() throws RemoteException {
            return null;
        }

        public void restoreBackupData(byte[] data) throws RemoteException {
        }

        public void restoreSupplicantBackupData(byte[] supplicantData, byte[] ipConfigData) throws RemoteException {
        }

        public void startSubscriptionProvisioning(OsuProvider provider, IProvisioningCallback callback) throws RemoteException {
        }

        public void registerSoftApCallback(IBinder binder, ISoftApCallback callback, int callbackIdentifier) throws RemoteException {
        }

        public void unregisterSoftApCallback(int callbackIdentifier) throws RemoteException {
        }

        public void addOnWifiUsabilityStatsListener(IBinder binder, IOnWifiUsabilityStatsListener listener, int listenerIdentifier) throws RemoteException {
        }

        public void removeOnWifiUsabilityStatsListener(int listenerIdentifier) throws RemoteException {
        }

        public void registerTrafficStateCallback(IBinder binder, ITrafficStateCallback callback, int callbackIdentifier) throws RemoteException {
        }

        public void unregisterTrafficStateCallback(int callbackIdentifier) throws RemoteException {
        }

        public String getCapabilities(String capaType) throws RemoteException {
            return null;
        }

        public int dppAddBootstrapQrCode(String uri) throws RemoteException {
            return 0;
        }

        public int dppBootstrapGenerate(WifiDppConfig config) throws RemoteException {
            return 0;
        }

        public String dppGetUri(int bootstrap_id) throws RemoteException {
            return null;
        }

        public int dppBootstrapRemove(int bootstrap_id) throws RemoteException {
            return 0;
        }

        public int dppListen(String frequency, int dpp_role, boolean qr_mutual, boolean netrole_ap) throws RemoteException {
            return 0;
        }

        public void dppStopListen() throws RemoteException {
        }

        public int dppConfiguratorAdd(String curve, String key, int expiry) throws RemoteException {
            return 0;
        }

        public int dppConfiguratorRemove(int config_id) throws RemoteException {
            return 0;
        }

        public int dppStartAuth(WifiDppConfig config) throws RemoteException {
            return 0;
        }

        public String dppConfiguratorGetKey(int id) throws RemoteException {
            return null;
        }

        public boolean isExtendingWifi() throws RemoteException {
            return false;
        }

        public boolean isWifiCoverageExtendFeatureEnabled() throws RemoteException {
            return false;
        }

        public void enableWifiCoverageExtendFeature(boolean enable) throws RemoteException {
        }

        public void registerNetworkRequestMatchCallback(IBinder binder, INetworkRequestMatchCallback callback, int callbackIdentifier) throws RemoteException {
        }

        public void unregisterNetworkRequestMatchCallback(int callbackIdentifier) throws RemoteException {
        }

        public int addNetworkSuggestions(List<WifiNetworkSuggestion> list, String packageName) throws RemoteException {
            return 0;
        }

        public int removeNetworkSuggestions(List<WifiNetworkSuggestion> list, String packageName) throws RemoteException {
            return 0;
        }

        public String[] getFactoryMacAddresses() throws RemoteException {
            return null;
        }

        public void setDeviceMobilityState(int state) throws RemoteException {
        }

        public void startDppAsConfiguratorInitiator(IBinder binder, String enrolleeUri, int selectedNetworkId, int netRole, IDppCallback callback) throws RemoteException {
        }

        public void startDppAsEnrolleeInitiator(IBinder binder, String configuratorUri, IDppCallback callback) throws RemoteException {
        }

        public void stopDppSession() throws RemoteException {
        }

        public void updateWifiUsabilityScore(int seqNum, int score, int predictionHorizonSec) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWifiManager {
        private static final String DESCRIPTOR = "android.net.wifi.IWifiManager";
        static final int TRANSACTION_acquireMulticastLock = 39;
        static final int TRANSACTION_acquireWifiLock = 34;
        static final int TRANSACTION_addNetworkSuggestions = 88;
        static final int TRANSACTION_addOnWifiUsabilityStatsListener = 68;
        static final int TRANSACTION_addOrUpdateNetwork = 9;
        static final int TRANSACTION_addOrUpdatePasspointConfiguration = 10;
        static final int TRANSACTION_deauthenticateNetwork = 16;
        static final int TRANSACTION_disableEphemeralNetwork = 59;
        static final int TRANSACTION_disableNetwork = 19;
        static final int TRANSACTION_disconnect = 22;
        static final int TRANSACTION_dppAddBootstrapQrCode = 73;
        static final int TRANSACTION_dppBootstrapGenerate = 74;
        static final int TRANSACTION_dppBootstrapRemove = 76;
        static final int TRANSACTION_dppConfiguratorAdd = 79;
        static final int TRANSACTION_dppConfiguratorGetKey = 82;
        static final int TRANSACTION_dppConfiguratorRemove = 80;
        static final int TRANSACTION_dppGetUri = 75;
        static final int TRANSACTION_dppListen = 77;
        static final int TRANSACTION_dppStartAuth = 81;
        static final int TRANSACTION_dppStopListen = 78;
        static final int TRANSACTION_enableNetwork = 18;
        static final int TRANSACTION_enableTdls = 53;
        static final int TRANSACTION_enableTdlsWithMacAddress = 54;
        static final int TRANSACTION_enableVerboseLogging = 56;
        static final int TRANSACTION_enableWifiConnectivityManager = 58;
        static final int TRANSACTION_enableWifiCoverageExtendFeature = 85;
        static final int TRANSACTION_factoryReset = 60;
        static final int TRANSACTION_getAllMatchingFqdnsForScanResults = 6;
        static final int TRANSACTION_getCapabilities = 72;
        static final int TRANSACTION_getConfiguredNetworks = 4;
        static final int TRANSACTION_getConnectionInfo = 25;
        static final int TRANSACTION_getCountryCode = 29;
        static final int TRANSACTION_getCurrentNetwork = 61;
        static final int TRANSACTION_getCurrentNetworkWpsNfcConfigurationToken = 55;
        static final int TRANSACTION_getDhcpInfo = 32;
        static final int TRANSACTION_getFactoryMacAddresses = 90;
        static final int TRANSACTION_getMatchingOsuProviders = 7;
        static final int TRANSACTION_getMatchingPasspointConfigsForOsuProviders = 8;
        static final int TRANSACTION_getPasspointConfigurations = 12;
        static final int TRANSACTION_getPrivilegedConfiguredNetworks = 5;
        static final int TRANSACTION_getScanResults = 21;
        static final int TRANSACTION_getSupportedFeatures = 1;
        static final int TRANSACTION_getVerboseLoggingLevel = 57;
        static final int TRANSACTION_getWifiApConfiguration = 49;
        static final int TRANSACTION_getWifiApEnabledState = 48;
        static final int TRANSACTION_getWifiConfigsForPasspointProfiles = 13;
        static final int TRANSACTION_getWifiEnabledState = 27;
        static final int TRANSACTION_getWifiServiceMessenger = 52;
        static final int TRANSACTION_initializeMulticastFiltering = 37;
        static final int TRANSACTION_isDualBandSupported = 30;
        static final int TRANSACTION_isExtendingWifi = 83;
        static final int TRANSACTION_isMulticastEnabled = 38;
        static final int TRANSACTION_isScanAlwaysAvailable = 33;
        static final int TRANSACTION_isWifiCoverageExtendFeatureEnabled = 84;
        static final int TRANSACTION_matchProviderWithCurrentNetwork = 15;
        static final int TRANSACTION_needs5GHzToAnyApBandConversion = 31;
        static final int TRANSACTION_notifyUserOfApBandConversion = 51;
        static final int TRANSACTION_queryPasspointIcon = 14;
        static final int TRANSACTION_reassociate = 24;
        static final int TRANSACTION_reconnect = 23;
        static final int TRANSACTION_registerNetworkRequestMatchCallback = 86;
        static final int TRANSACTION_registerSoftApCallback = 66;
        static final int TRANSACTION_registerTrafficStateCallback = 70;
        static final int TRANSACTION_releaseMulticastLock = 40;
        static final int TRANSACTION_releaseWifiLock = 36;
        static final int TRANSACTION_removeNetwork = 17;
        static final int TRANSACTION_removeNetworkSuggestions = 89;
        static final int TRANSACTION_removeOnWifiUsabilityStatsListener = 69;
        static final int TRANSACTION_removePasspointConfiguration = 11;
        static final int TRANSACTION_reportActivityInfo = 2;
        static final int TRANSACTION_requestActivityInfo = 3;
        static final int TRANSACTION_restoreBackupData = 63;
        static final int TRANSACTION_restoreSupplicantBackupData = 64;
        static final int TRANSACTION_retrieveBackupData = 62;
        static final int TRANSACTION_setCountryCode = 28;
        static final int TRANSACTION_setDeviceMobilityState = 91;
        static final int TRANSACTION_setWifiApConfiguration = 50;
        static final int TRANSACTION_setWifiEnabled = 26;
        static final int TRANSACTION_startDppAsConfiguratorInitiator = 92;
        static final int TRANSACTION_startDppAsEnrolleeInitiator = 93;
        static final int TRANSACTION_startLocalOnlyHotspot = 44;
        static final int TRANSACTION_startScan = 20;
        static final int TRANSACTION_startSoftAp = 42;
        static final int TRANSACTION_startSubscriptionProvisioning = 65;
        static final int TRANSACTION_startWatchLocalOnlyHotspot = 46;
        static final int TRANSACTION_stopDppSession = 94;
        static final int TRANSACTION_stopLocalOnlyHotspot = 45;
        static final int TRANSACTION_stopSoftAp = 43;
        static final int TRANSACTION_stopWatchLocalOnlyHotspot = 47;
        static final int TRANSACTION_unregisterNetworkRequestMatchCallback = 87;
        static final int TRANSACTION_unregisterSoftApCallback = 67;
        static final int TRANSACTION_unregisterTrafficStateCallback = 71;
        static final int TRANSACTION_updateInterfaceIpState = 41;
        static final int TRANSACTION_updateWifiLockWorkSource = 35;
        static final int TRANSACTION_updateWifiUsabilityScore = 95;

        private static class Proxy implements IWifiManager {
            public static IWifiManager sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public long getSupportedFeatures() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getSupportedFeatures();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WifiActivityEnergyInfo reportActivityInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    WifiActivityEnergyInfo wifiActivityEnergyInfo = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        wifiActivityEnergyInfo = Stub.getDefaultImpl();
                        if (wifiActivityEnergyInfo != 0) {
                            wifiActivityEnergyInfo = Stub.getDefaultImpl().reportActivityInfo();
                            return wifiActivityEnergyInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        wifiActivityEnergyInfo = (WifiActivityEnergyInfo) WifiActivityEnergyInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        wifiActivityEnergyInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return wifiActivityEnergyInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestActivityInfo(ResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().requestActivityInfo(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public ParceledListSlice getConfiguredNetworks(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    ParceledListSlice parceledListSlice = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getConfiguredNetworks(packageName);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getPrivilegedConfiguredNetworks(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    ParceledListSlice parceledListSlice = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getPrivilegedConfiguredNetworks(packageName);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Map getAllMatchingFqdnsForScanResults(List<ScanResult> scanResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(scanResult);
                    Map map = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        map = Stub.getDefaultImpl();
                        if (map != 0) {
                            map = Stub.getDefaultImpl().getAllMatchingFqdnsForScanResults(scanResult);
                            return map;
                        }
                    }
                    _reply.readException();
                    map = getClass().getClassLoader();
                    HashMap _result = _reply.readHashMap(map);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Map getMatchingOsuProviders(List<ScanResult> scanResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(scanResult);
                    Map map = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        map = Stub.getDefaultImpl();
                        if (map != 0) {
                            map = Stub.getDefaultImpl().getMatchingOsuProviders(scanResult);
                            return map;
                        }
                    }
                    _reply.readException();
                    map = getClass().getClassLoader();
                    HashMap _result = _reply.readHashMap(map);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Map getMatchingPasspointConfigsForOsuProviders(List<OsuProvider> osuProviders) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(osuProviders);
                    Map map = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        map = Stub.getDefaultImpl();
                        if (map != 0) {
                            map = Stub.getDefaultImpl().getMatchingPasspointConfigsForOsuProviders(osuProviders);
                            return map;
                        }
                    }
                    _reply.readException();
                    map = getClass().getClassLoader();
                    HashMap _result = _reply.readHashMap(map);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int addOrUpdateNetwork(WifiConfiguration config, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    int i = this.mRemote;
                    if (!i.transact(9, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().addOrUpdateNetwork(config, packageName);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addOrUpdatePasspointConfiguration(PasspointConfiguration config, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().addOrUpdatePasspointConfiguration(config, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removePasspointConfiguration(String fqdn, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fqdn);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removePasspointConfiguration(fqdn, packageName);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PasspointConfiguration> getPasspointConfigurations(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    List<PasspointConfiguration> list = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPasspointConfigurations(packageName);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PasspointConfiguration.CREATOR);
                    List<PasspointConfiguration> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<WifiConfiguration> getWifiConfigsForPasspointProfiles(List<String> fqdnList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(fqdnList);
                    List<WifiConfiguration> list = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getWifiConfigsForPasspointProfiles(fqdnList);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(WifiConfiguration.CREATOR);
                    List<WifiConfiguration> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void queryPasspointIcon(long bssid, String fileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(bssid);
                    _data.writeString(fileName);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().queryPasspointIcon(bssid, fileName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int matchProviderWithCurrentNetwork(String fqdn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fqdn);
                    int i = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().matchProviderWithCurrentNetwork(fqdn);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deauthenticateNetwork(long holdoff, boolean ess) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(holdoff);
                    _data.writeInt(ess ? 1 : 0);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deauthenticateNetwork(holdoff, ess);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeNetwork(int netId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeNetwork(netId, packageName);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enableNetwork(int netId, boolean disableOthers, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    boolean _result = true;
                    _data.writeInt(disableOthers ? 1 : 0);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().enableNetwork(netId, disableOthers, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disableNetwork(int netId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().disableNetwork(netId, packageName);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startScan(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().startScan(packageName);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<ScanResult> getScanResults(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    List<ScanResult> list = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getScanResults(callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(ScanResult.CREATOR);
                    List<ScanResult> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disconnect(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().disconnect(packageName);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean reconnect(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().reconnect(packageName);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean reassociate(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().reassociate(packageName);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WifiInfo getConnectionInfo(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    WifiInfo wifiInfo = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        wifiInfo = Stub.getDefaultImpl();
                        if (wifiInfo != 0) {
                            wifiInfo = Stub.getDefaultImpl().getConnectionInfo(callingPackage);
                            return wifiInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        wifiInfo = (WifiInfo) WifiInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        wifiInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return wifiInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setWifiEnabled(String packageName, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setWifiEnabled(packageName, enable);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getWifiEnabledState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getWifiEnabledState();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCountryCode(String country) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(country);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCountryCode(country);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCountryCode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCountryCode();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDualBandSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDualBandSupported();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean needs5GHzToAnyApBandConversion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().needs5GHzToAnyApBandConversion();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public DhcpInfo getDhcpInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    DhcpInfo dhcpInfo = 32;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        dhcpInfo = Stub.getDefaultImpl();
                        if (dhcpInfo != 0) {
                            dhcpInfo = Stub.getDefaultImpl().getDhcpInfo();
                            return dhcpInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        dhcpInfo = (DhcpInfo) DhcpInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        dhcpInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return dhcpInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isScanAlwaysAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isScanAlwaysAvailable();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean acquireWifiLock(IBinder lock, int lockType, String tag, WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    _data.writeInt(lockType);
                    _data.writeString(tag);
                    boolean _result = true;
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().acquireWifiLock(lock, lockType, tag, ws);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateWifiLockWorkSource(IBinder lock, WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateWifiLockWorkSource(lock, ws);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean releaseWifiLock(IBinder lock) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(36, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().releaseWifiLock(lock);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void initializeMulticastFiltering() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().initializeMulticastFiltering();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isMulticastEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(38, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isMulticastEnabled();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void acquireMulticastLock(IBinder binder, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(tag);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().acquireMulticastLock(binder, tag);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releaseMulticastLock(String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(tag);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().releaseMulticastLock(tag);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateInterfaceIpState(String ifaceName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ifaceName);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateInterfaceIpState(ifaceName, mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startSoftAp(WifiConfiguration wifiConfig) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (wifiConfig != null) {
                        _data.writeInt(1);
                        wifiConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().startSoftAp(wifiConfig);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean stopSoftAp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().stopSoftAp();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startLocalOnlyHotspot(Messenger messenger, IBinder binder, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    _data.writeString(packageName);
                    int i = this.mRemote;
                    if (!i.transact(44, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().startLocalOnlyHotspot(messenger, binder, packageName);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopLocalOnlyHotspot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopLocalOnlyHotspot();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startWatchLocalOnlyHotspot(Messenger messenger, IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startWatchLocalOnlyHotspot(messenger, binder);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopWatchLocalOnlyHotspot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopWatchLocalOnlyHotspot();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getWifiApEnabledState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 48;
                    if (!this.mRemote.transact(48, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getWifiApEnabledState();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WifiConfiguration getWifiApConfiguration() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    WifiConfiguration wifiConfiguration = 49;
                    if (!this.mRemote.transact(49, _data, _reply, 0)) {
                        wifiConfiguration = Stub.getDefaultImpl();
                        if (wifiConfiguration != 0) {
                            wifiConfiguration = Stub.getDefaultImpl().getWifiApConfiguration();
                            return wifiConfiguration;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        wifiConfiguration = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(_reply);
                    } else {
                        wifiConfiguration = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return wifiConfiguration;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setWifiApConfiguration(WifiConfiguration wifiConfig, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (wifiConfig != null) {
                        _data.writeInt(1);
                        wifiConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(50, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setWifiApConfiguration(wifiConfig, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyUserOfApBandConversion(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyUserOfApBandConversion(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Messenger getWifiServiceMessenger(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    Messenger messenger = 52;
                    if (!this.mRemote.transact(52, _data, _reply, 0)) {
                        messenger = Stub.getDefaultImpl();
                        if (messenger != 0) {
                            messenger = Stub.getDefaultImpl().getWifiServiceMessenger(packageName);
                            return messenger;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        messenger = (Messenger) Messenger.CREATOR.createFromParcel(_reply);
                    } else {
                        messenger = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return messenger;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableTdls(String remoteIPAddress, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(remoteIPAddress);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableTdls(remoteIPAddress, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableTdlsWithMacAddress(String remoteMacAddress, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(remoteMacAddress);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableTdlsWithMacAddress(remoteMacAddress, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurrentNetworkWpsNfcConfigurationToken() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 55;
                    if (!this.mRemote.transact(55, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCurrentNetworkWpsNfcConfigurationToken();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableVerboseLogging(int verbose) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(verbose);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableVerboseLogging(verbose);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVerboseLoggingLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 57;
                    if (!this.mRemote.transact(57, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getVerboseLoggingLevel();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableWifiConnectivityManager(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(58, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableWifiConnectivityManager(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableEphemeralNetwork(String SSID, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(SSID);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableEphemeralNetwork(SSID, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void factoryReset(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().factoryReset(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Network getCurrentNetwork() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Network network = 61;
                    if (!this.mRemote.transact(61, _data, _reply, 0)) {
                        network = Stub.getDefaultImpl();
                        if (network != 0) {
                            network = Stub.getDefaultImpl().getCurrentNetwork();
                            return network;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        network = (Network) Network.CREATOR.createFromParcel(_reply);
                    } else {
                        network = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return network;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] retrieveBackupData() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    byte[] bArr = 62;
                    if (!this.mRemote.transact(62, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().retrieveBackupData();
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restoreBackupData(byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(data);
                    if (this.mRemote.transact(63, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restoreBackupData(data);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restoreSupplicantBackupData(byte[] supplicantData, byte[] ipConfigData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(supplicantData);
                    _data.writeByteArray(ipConfigData);
                    if (this.mRemote.transact(64, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restoreSupplicantBackupData(supplicantData, ipConfigData);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startSubscriptionProvisioning(OsuProvider provider, IProvisioningCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(65, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startSubscriptionProvisioning(provider, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerSoftApCallback(IBinder binder, ISoftApCallback callback, int callbackIdentifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(callbackIdentifier);
                    if (this.mRemote.transact(66, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerSoftApCallback(binder, callback, callbackIdentifier);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterSoftApCallback(int callbackIdentifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callbackIdentifier);
                    if (this.mRemote.transact(67, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterSoftApCallback(callbackIdentifier);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addOnWifiUsabilityStatsListener(IBinder binder, IOnWifiUsabilityStatsListener listener, int listenerIdentifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(listenerIdentifier);
                    if (this.mRemote.transact(68, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addOnWifiUsabilityStatsListener(binder, listener, listenerIdentifier);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeOnWifiUsabilityStatsListener(int listenerIdentifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(listenerIdentifier);
                    if (this.mRemote.transact(69, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeOnWifiUsabilityStatsListener(listenerIdentifier);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerTrafficStateCallback(IBinder binder, ITrafficStateCallback callback, int callbackIdentifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(callbackIdentifier);
                    if (this.mRemote.transact(70, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerTrafficStateCallback(binder, callback, callbackIdentifier);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterTrafficStateCallback(int callbackIdentifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callbackIdentifier);
                    if (this.mRemote.transact(71, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterTrafficStateCallback(callbackIdentifier);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCapabilities(String capaType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(capaType);
                    String str = 72;
                    if (!this.mRemote.transact(72, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCapabilities(capaType);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int dppAddBootstrapQrCode(String uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uri);
                    int i = 73;
                    if (!this.mRemote.transact(73, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().dppAddBootstrapQrCode(uri);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int dppBootstrapGenerate(WifiDppConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(74, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().dppBootstrapGenerate(config);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String dppGetUri(int bootstrap_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(bootstrap_id);
                    String str = 75;
                    if (!this.mRemote.transact(75, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().dppGetUri(bootstrap_id);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int dppBootstrapRemove(int bootstrap_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(bootstrap_id);
                    int i = 76;
                    if (!this.mRemote.transact(76, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().dppBootstrapRemove(bootstrap_id);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int dppListen(String frequency, int dpp_role, boolean qr_mutual, boolean netrole_ap) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(frequency);
                    _data.writeInt(dpp_role);
                    int i = 1;
                    int i2 = 0;
                    _data.writeInt(qr_mutual ? 1 : 0);
                    if (!netrole_ap) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(77, _data, _reply, 0)) {
                        i2 = Stub.getDefaultImpl();
                        if (i2 != 0) {
                            i2 = Stub.getDefaultImpl().dppListen(frequency, dpp_role, qr_mutual, netrole_ap);
                            return i2;
                        }
                    }
                    _reply.readException();
                    i2 = _reply.readInt();
                    i = i2;
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dppStopListen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(78, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dppStopListen();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int dppConfiguratorAdd(String curve, String key, int expiry) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(curve);
                    _data.writeString(key);
                    _data.writeInt(expiry);
                    int i = 79;
                    if (!this.mRemote.transact(79, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().dppConfiguratorAdd(curve, key, expiry);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int dppConfiguratorRemove(int config_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(config_id);
                    int i = 80;
                    if (!this.mRemote.transact(80, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().dppConfiguratorRemove(config_id);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int dppStartAuth(WifiDppConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(81, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().dppStartAuth(config);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String dppConfiguratorGetKey(int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    String str = 82;
                    if (!this.mRemote.transact(82, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().dppConfiguratorGetKey(id);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isExtendingWifi() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(83, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isExtendingWifi();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isWifiCoverageExtendFeatureEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(84, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isWifiCoverageExtendFeatureEnabled();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableWifiCoverageExtendFeature(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(85, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableWifiCoverageExtendFeature(enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerNetworkRequestMatchCallback(IBinder binder, INetworkRequestMatchCallback callback, int callbackIdentifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(callbackIdentifier);
                    if (this.mRemote.transact(86, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerNetworkRequestMatchCallback(binder, callback, callbackIdentifier);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterNetworkRequestMatchCallback(int callbackIdentifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callbackIdentifier);
                    if (this.mRemote.transact(87, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterNetworkRequestMatchCallback(callbackIdentifier);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int addNetworkSuggestions(List<WifiNetworkSuggestion> networkSuggestions, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(networkSuggestions);
                    _data.writeString(packageName);
                    int i = 88;
                    if (!this.mRemote.transact(88, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().addNetworkSuggestions(networkSuggestions, packageName);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int removeNetworkSuggestions(List<WifiNetworkSuggestion> networkSuggestions, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(networkSuggestions);
                    _data.writeString(packageName);
                    int i = 89;
                    if (!this.mRemote.transact(89, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().removeNetworkSuggestions(networkSuggestions, packageName);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getFactoryMacAddresses() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 90;
                    if (!this.mRemote.transact(90, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getFactoryMacAddresses();
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDeviceMobilityState(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    if (this.mRemote.transact(91, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDeviceMobilityState(state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startDppAsConfiguratorInitiator(IBinder binder, String enrolleeUri, int selectedNetworkId, int netRole, IDppCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(enrolleeUri);
                    _data.writeInt(selectedNetworkId);
                    _data.writeInt(netRole);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(92, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startDppAsConfiguratorInitiator(binder, enrolleeUri, selectedNetworkId, netRole, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startDppAsEnrolleeInitiator(IBinder binder, String configuratorUri, IDppCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(configuratorUri);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(93, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startDppAsEnrolleeInitiator(binder, configuratorUri, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopDppSession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(94, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopDppSession();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateWifiUsabilityScore(int seqNum, int score, int predictionHorizonSec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(seqNum);
                    _data.writeInt(score);
                    _data.writeInt(predictionHorizonSec);
                    if (this.mRemote.transact(95, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateWifiUsabilityScore(seqNum, score, predictionHorizonSec);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWifiManager)) {
                return new Proxy(obj);
            }
            return (IWifiManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getSupportedFeatures";
                case 2:
                    return "reportActivityInfo";
                case 3:
                    return "requestActivityInfo";
                case 4:
                    return "getConfiguredNetworks";
                case 5:
                    return "getPrivilegedConfiguredNetworks";
                case 6:
                    return "getAllMatchingFqdnsForScanResults";
                case 7:
                    return "getMatchingOsuProviders";
                case 8:
                    return "getMatchingPasspointConfigsForOsuProviders";
                case 9:
                    return "addOrUpdateNetwork";
                case 10:
                    return "addOrUpdatePasspointConfiguration";
                case 11:
                    return "removePasspointConfiguration";
                case 12:
                    return "getPasspointConfigurations";
                case 13:
                    return "getWifiConfigsForPasspointProfiles";
                case 14:
                    return "queryPasspointIcon";
                case 15:
                    return "matchProviderWithCurrentNetwork";
                case 16:
                    return "deauthenticateNetwork";
                case 17:
                    return "removeNetwork";
                case 18:
                    return "enableNetwork";
                case 19:
                    return "disableNetwork";
                case 20:
                    return "startScan";
                case 21:
                    return "getScanResults";
                case 22:
                    return "disconnect";
                case 23:
                    return "reconnect";
                case 24:
                    return "reassociate";
                case 25:
                    return "getConnectionInfo";
                case 26:
                    return "setWifiEnabled";
                case 27:
                    return "getWifiEnabledState";
                case 28:
                    return "setCountryCode";
                case 29:
                    return "getCountryCode";
                case 30:
                    return "isDualBandSupported";
                case 31:
                    return "needs5GHzToAnyApBandConversion";
                case 32:
                    return "getDhcpInfo";
                case 33:
                    return "isScanAlwaysAvailable";
                case 34:
                    return "acquireWifiLock";
                case 35:
                    return "updateWifiLockWorkSource";
                case 36:
                    return "releaseWifiLock";
                case 37:
                    return "initializeMulticastFiltering";
                case 38:
                    return "isMulticastEnabled";
                case 39:
                    return "acquireMulticastLock";
                case 40:
                    return "releaseMulticastLock";
                case 41:
                    return "updateInterfaceIpState";
                case 42:
                    return "startSoftAp";
                case 43:
                    return "stopSoftAp";
                case 44:
                    return "startLocalOnlyHotspot";
                case 45:
                    return "stopLocalOnlyHotspot";
                case 46:
                    return "startWatchLocalOnlyHotspot";
                case 47:
                    return "stopWatchLocalOnlyHotspot";
                case 48:
                    return "getWifiApEnabledState";
                case 49:
                    return "getWifiApConfiguration";
                case 50:
                    return "setWifiApConfiguration";
                case 51:
                    return "notifyUserOfApBandConversion";
                case 52:
                    return "getWifiServiceMessenger";
                case 53:
                    return "enableTdls";
                case 54:
                    return "enableTdlsWithMacAddress";
                case 55:
                    return "getCurrentNetworkWpsNfcConfigurationToken";
                case 56:
                    return "enableVerboseLogging";
                case 57:
                    return "getVerboseLoggingLevel";
                case 58:
                    return "enableWifiConnectivityManager";
                case 59:
                    return "disableEphemeralNetwork";
                case 60:
                    return "factoryReset";
                case 61:
                    return "getCurrentNetwork";
                case 62:
                    return "retrieveBackupData";
                case 63:
                    return "restoreBackupData";
                case 64:
                    return "restoreSupplicantBackupData";
                case 65:
                    return "startSubscriptionProvisioning";
                case 66:
                    return "registerSoftApCallback";
                case 67:
                    return "unregisterSoftApCallback";
                case 68:
                    return "addOnWifiUsabilityStatsListener";
                case 69:
                    return "removeOnWifiUsabilityStatsListener";
                case 70:
                    return "registerTrafficStateCallback";
                case 71:
                    return "unregisterTrafficStateCallback";
                case 72:
                    return "getCapabilities";
                case 73:
                    return "dppAddBootstrapQrCode";
                case 74:
                    return "dppBootstrapGenerate";
                case 75:
                    return "dppGetUri";
                case 76:
                    return "dppBootstrapRemove";
                case 77:
                    return "dppListen";
                case 78:
                    return "dppStopListen";
                case 79:
                    return "dppConfiguratorAdd";
                case 80:
                    return "dppConfiguratorRemove";
                case 81:
                    return "dppStartAuth";
                case 82:
                    return "dppConfiguratorGetKey";
                case 83:
                    return "isExtendingWifi";
                case 84:
                    return "isWifiCoverageExtendFeatureEnabled";
                case 85:
                    return "enableWifiCoverageExtendFeature";
                case 86:
                    return "registerNetworkRequestMatchCallback";
                case 87:
                    return "unregisterNetworkRequestMatchCallback";
                case 88:
                    return "addNetworkSuggestions";
                case 89:
                    return "removeNetworkSuggestions";
                case 90:
                    return "getFactoryMacAddresses";
                case 91:
                    return "setDeviceMobilityState";
                case 92:
                    return "startDppAsConfiguratorInitiator";
                case 93:
                    return "startDppAsEnrolleeInitiator";
                case 94:
                    return "stopDppSession";
                case 95:
                    return "updateWifiUsabilityScore";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                boolean _arg1 = false;
                ParceledListSlice _result;
                Map _result2;
                WifiConfiguration _arg0;
                int _result3;
                boolean _result4;
                int _result5;
                boolean _result6;
                boolean _result7;
                String _arg02;
                int _result8;
                String _result9;
                IBinder _arg03;
                Messenger _arg04;
                int _result10;
                WifiDppConfig _arg05;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        long _result11 = getSupportedFeatures();
                        reply.writeNoException();
                        parcel2.writeLong(_result11);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        WifiActivityEnergyInfo _result12 = reportActivityInfo();
                        reply.writeNoException();
                        if (_result12 != null) {
                            parcel2.writeInt(1);
                            _result12.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        ResultReceiver _arg06;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        requestActivityInfo(_arg06);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _result = getConfiguredNetworks(data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result = getPrivilegedConfiguredNetworks(data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _result2 = getAllMatchingFqdnsForScanResults(parcel.createTypedArrayList(ScanResult.CREATOR));
                        reply.writeNoException();
                        parcel2.writeMap(_result2);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result2 = getMatchingOsuProviders(parcel.createTypedArrayList(ScanResult.CREATOR));
                        reply.writeNoException();
                        parcel2.writeMap(_result2);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _result2 = getMatchingPasspointConfigsForOsuProviders(parcel.createTypedArrayList(OsuProvider.CREATOR));
                        reply.writeNoException();
                        parcel2.writeMap(_result2);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = addOrUpdateNetwork(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 10:
                        PasspointConfiguration _arg07;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (PasspointConfiguration) PasspointConfiguration.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        _result4 = addOrUpdatePasspointConfiguration(_arg07, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result4 = removePasspointConfiguration(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        List<PasspointConfiguration> _result13 = getPasspointConfigurations(data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result13);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        List<WifiConfiguration> _result14 = getWifiConfigsForPasspointProfiles(data.createStringArrayList());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result14);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        queryPasspointIcon(data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _result5 = matchProviderWithCurrentNetwork(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        long _arg08 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        deauthenticateNetwork(_arg08, _arg1);
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _result4 = removeNetwork(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _result5 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result6 = enableNetwork(_result5, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _result4 = disableNetwork(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _result7 = startScan(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        List<ScanResult> _result15 = getScanResults(data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result15);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _result7 = disconnect(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result7 = reconnect(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _result7 = reassociate(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        WifiInfo _result16 = getConnectionInfo(data.readString());
                        reply.writeNoException();
                        if (_result16 != null) {
                            parcel2.writeInt(1);
                            _result16.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result4 = setWifiEnabled(_arg02, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        _result8 = getWifiEnabledState();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        setCountryCode(data.readString());
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        _result9 = getCountryCode();
                        reply.writeNoException();
                        parcel2.writeString(_result9);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isDualBandSupported();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _arg1 = needs5GHzToAnyApBandConversion();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        DhcpInfo _result17 = getDhcpInfo();
                        reply.writeNoException();
                        if (_result17 != null) {
                            parcel2.writeInt(1);
                            _result17.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isScanAlwaysAvailable();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 34:
                        WorkSource _arg3;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readStrongBinder();
                        _result5 = data.readInt();
                        String _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        boolean _result18 = acquireWifiLock(_arg03, _result5, _arg2, _arg3);
                        reply.writeNoException();
                        parcel2.writeInt(_result18);
                        return true;
                    case 35:
                        WorkSource _arg12;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg12 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        updateWifiLockWorkSource(_arg03, _arg12);
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        _result7 = releaseWifiLock(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        initializeMulticastFiltering();
                        reply.writeNoException();
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isMulticastEnabled();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        acquireMulticastLock(data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        releaseMulticastLock(data.readString());
                        reply.writeNoException();
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        updateInterfaceIpState(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = startSoftAp(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        _arg1 = stopSoftAp();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Messenger) Messenger.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result10 = startLocalOnlyHotspot(_arg04, data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        stopLocalOnlyHotspot();
                        reply.writeNoException();
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Messenger) Messenger.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        startWatchLocalOnlyHotspot(_arg04, data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        stopWatchLocalOnlyHotspot();
                        reply.writeNoException();
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        _result8 = getWifiApEnabledState();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        WifiConfiguration _result19 = getWifiApConfiguration();
                        reply.writeNoException();
                        if (_result19 != null) {
                            parcel2.writeInt(1);
                            _result19.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = setWifiApConfiguration(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        notifyUserOfApBandConversion(data.readString());
                        reply.writeNoException();
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        Messenger _result20 = getWifiServiceMessenger(data.readString());
                        reply.writeNoException();
                        if (_result20 != null) {
                            parcel2.writeInt(1);
                            _result20.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        enableTdls(_arg02, _arg1);
                        reply.writeNoException();
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        enableTdlsWithMacAddress(_arg02, _arg1);
                        reply.writeNoException();
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        _result9 = getCurrentNetworkWpsNfcConfigurationToken();
                        reply.writeNoException();
                        parcel2.writeString(_result9);
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        enableVerboseLogging(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        _result8 = getVerboseLoggingLevel();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 58:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        enableWifiConnectivityManager(_arg1);
                        reply.writeNoException();
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        disableEphemeralNetwork(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        factoryReset(data.readString());
                        reply.writeNoException();
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        Network _result21 = getCurrentNetwork();
                        reply.writeNoException();
                        if (_result21 != null) {
                            parcel2.writeInt(1);
                            _result21.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 62:
                        parcel.enforceInterface(descriptor);
                        byte[] _result22 = retrieveBackupData();
                        reply.writeNoException();
                        parcel2.writeByteArray(_result22);
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        restoreBackupData(data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        restoreSupplicantBackupData(data.createByteArray(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 65:
                        OsuProvider _arg09;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg09 = (OsuProvider) OsuProvider.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg09 = null;
                        }
                        startSubscriptionProvisioning(_arg09, android.net.wifi.hotspot2.IProvisioningCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        registerSoftApCallback(data.readStrongBinder(), android.net.wifi.ISoftApCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        unregisterSoftApCallback(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        addOnWifiUsabilityStatsListener(data.readStrongBinder(), android.net.wifi.IOnWifiUsabilityStatsListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 69:
                        parcel.enforceInterface(descriptor);
                        removeOnWifiUsabilityStatsListener(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 70:
                        parcel.enforceInterface(descriptor);
                        registerTrafficStateCallback(data.readStrongBinder(), android.net.wifi.ITrafficStateCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 71:
                        parcel.enforceInterface(descriptor);
                        unregisterTrafficStateCallback(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 72:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getCapabilities(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_arg02);
                        return true;
                    case 73:
                        parcel.enforceInterface(descriptor);
                        _result5 = dppAddBootstrapQrCode(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 74:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (WifiDppConfig) WifiDppConfig.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        _result5 = dppBootstrapGenerate(_arg05);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 75:
                        parcel.enforceInterface(descriptor);
                        _arg02 = dppGetUri(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg02);
                        return true;
                    case 76:
                        parcel.enforceInterface(descriptor);
                        _result5 = dppBootstrapRemove(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 77:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _result3 = data.readInt();
                        _result6 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        int _result23 = dppListen(_arg02, _result3, _result6, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result23);
                        return true;
                    case 78:
                        parcel.enforceInterface(descriptor);
                        dppStopListen();
                        reply.writeNoException();
                        return true;
                    case 79:
                        parcel.enforceInterface(descriptor);
                        _result10 = dppConfiguratorAdd(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 80:
                        parcel.enforceInterface(descriptor);
                        _result5 = dppConfiguratorRemove(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 81:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (WifiDppConfig) WifiDppConfig.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        _result5 = dppStartAuth(_arg05);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 82:
                        parcel.enforceInterface(descriptor);
                        _arg02 = dppConfiguratorGetKey(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg02);
                        return true;
                    case 83:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isExtendingWifi();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 84:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isWifiCoverageExtendFeatureEnabled();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 85:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        enableWifiCoverageExtendFeature(_arg1);
                        reply.writeNoException();
                        return true;
                    case 86:
                        parcel.enforceInterface(descriptor);
                        registerNetworkRequestMatchCallback(data.readStrongBinder(), android.net.wifi.INetworkRequestMatchCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 87:
                        parcel.enforceInterface(descriptor);
                        unregisterNetworkRequestMatchCallback(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 88:
                        parcel.enforceInterface(descriptor);
                        _result3 = addNetworkSuggestions(parcel.createTypedArrayList(WifiNetworkSuggestion.CREATOR), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 89:
                        parcel.enforceInterface(descriptor);
                        _result3 = removeNetworkSuggestions(parcel.createTypedArrayList(WifiNetworkSuggestion.CREATOR), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 90:
                        parcel.enforceInterface(descriptor);
                        String[] _result24 = getFactoryMacAddresses();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result24);
                        return true;
                    case 91:
                        parcel.enforceInterface(descriptor);
                        setDeviceMobilityState(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 92:
                        parcel.enforceInterface(descriptor);
                        startDppAsConfiguratorInitiator(data.readStrongBinder(), data.readString(), data.readInt(), data.readInt(), android.net.wifi.IDppCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 93:
                        parcel.enforceInterface(descriptor);
                        startDppAsEnrolleeInitiator(data.readStrongBinder(), data.readString(), android.net.wifi.IDppCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 94:
                        parcel.enforceInterface(descriptor);
                        stopDppSession();
                        reply.writeNoException();
                        return true;
                    case 95:
                        parcel.enforceInterface(descriptor);
                        updateWifiUsabilityScore(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IWifiManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWifiManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void acquireMulticastLock(IBinder iBinder, String str) throws RemoteException;

    boolean acquireWifiLock(IBinder iBinder, int i, String str, WorkSource workSource) throws RemoteException;

    int addNetworkSuggestions(List<WifiNetworkSuggestion> list, String str) throws RemoteException;

    void addOnWifiUsabilityStatsListener(IBinder iBinder, IOnWifiUsabilityStatsListener iOnWifiUsabilityStatsListener, int i) throws RemoteException;

    int addOrUpdateNetwork(WifiConfiguration wifiConfiguration, String str) throws RemoteException;

    boolean addOrUpdatePasspointConfiguration(PasspointConfiguration passpointConfiguration, String str) throws RemoteException;

    void deauthenticateNetwork(long j, boolean z) throws RemoteException;

    void disableEphemeralNetwork(String str, String str2) throws RemoteException;

    boolean disableNetwork(int i, String str) throws RemoteException;

    boolean disconnect(String str) throws RemoteException;

    int dppAddBootstrapQrCode(String str) throws RemoteException;

    int dppBootstrapGenerate(WifiDppConfig wifiDppConfig) throws RemoteException;

    int dppBootstrapRemove(int i) throws RemoteException;

    int dppConfiguratorAdd(String str, String str2, int i) throws RemoteException;

    String dppConfiguratorGetKey(int i) throws RemoteException;

    int dppConfiguratorRemove(int i) throws RemoteException;

    String dppGetUri(int i) throws RemoteException;

    int dppListen(String str, int i, boolean z, boolean z2) throws RemoteException;

    int dppStartAuth(WifiDppConfig wifiDppConfig) throws RemoteException;

    void dppStopListen() throws RemoteException;

    boolean enableNetwork(int i, boolean z, String str) throws RemoteException;

    void enableTdls(String str, boolean z) throws RemoteException;

    void enableTdlsWithMacAddress(String str, boolean z) throws RemoteException;

    void enableVerboseLogging(int i) throws RemoteException;

    void enableWifiConnectivityManager(boolean z) throws RemoteException;

    void enableWifiCoverageExtendFeature(boolean z) throws RemoteException;

    void factoryReset(String str) throws RemoteException;

    Map getAllMatchingFqdnsForScanResults(List<ScanResult> list) throws RemoteException;

    String getCapabilities(String str) throws RemoteException;

    ParceledListSlice getConfiguredNetworks(String str) throws RemoteException;

    WifiInfo getConnectionInfo(String str) throws RemoteException;

    String getCountryCode() throws RemoteException;

    @UnsupportedAppUsage
    Network getCurrentNetwork() throws RemoteException;

    String getCurrentNetworkWpsNfcConfigurationToken() throws RemoteException;

    DhcpInfo getDhcpInfo() throws RemoteException;

    String[] getFactoryMacAddresses() throws RemoteException;

    Map getMatchingOsuProviders(List<ScanResult> list) throws RemoteException;

    Map getMatchingPasspointConfigsForOsuProviders(List<OsuProvider> list) throws RemoteException;

    List<PasspointConfiguration> getPasspointConfigurations(String str) throws RemoteException;

    ParceledListSlice getPrivilegedConfiguredNetworks(String str) throws RemoteException;

    List<ScanResult> getScanResults(String str) throws RemoteException;

    long getSupportedFeatures() throws RemoteException;

    int getVerboseLoggingLevel() throws RemoteException;

    @UnsupportedAppUsage
    WifiConfiguration getWifiApConfiguration() throws RemoteException;

    @UnsupportedAppUsage
    int getWifiApEnabledState() throws RemoteException;

    List<WifiConfiguration> getWifiConfigsForPasspointProfiles(List<String> list) throws RemoteException;

    int getWifiEnabledState() throws RemoteException;

    Messenger getWifiServiceMessenger(String str) throws RemoteException;

    void initializeMulticastFiltering() throws RemoteException;

    boolean isDualBandSupported() throws RemoteException;

    boolean isExtendingWifi() throws RemoteException;

    boolean isMulticastEnabled() throws RemoteException;

    boolean isScanAlwaysAvailable() throws RemoteException;

    boolean isWifiCoverageExtendFeatureEnabled() throws RemoteException;

    int matchProviderWithCurrentNetwork(String str) throws RemoteException;

    boolean needs5GHzToAnyApBandConversion() throws RemoteException;

    void notifyUserOfApBandConversion(String str) throws RemoteException;

    void queryPasspointIcon(long j, String str) throws RemoteException;

    boolean reassociate(String str) throws RemoteException;

    boolean reconnect(String str) throws RemoteException;

    void registerNetworkRequestMatchCallback(IBinder iBinder, INetworkRequestMatchCallback iNetworkRequestMatchCallback, int i) throws RemoteException;

    void registerSoftApCallback(IBinder iBinder, ISoftApCallback iSoftApCallback, int i) throws RemoteException;

    void registerTrafficStateCallback(IBinder iBinder, ITrafficStateCallback iTrafficStateCallback, int i) throws RemoteException;

    void releaseMulticastLock(String str) throws RemoteException;

    boolean releaseWifiLock(IBinder iBinder) throws RemoteException;

    boolean removeNetwork(int i, String str) throws RemoteException;

    int removeNetworkSuggestions(List<WifiNetworkSuggestion> list, String str) throws RemoteException;

    void removeOnWifiUsabilityStatsListener(int i) throws RemoteException;

    boolean removePasspointConfiguration(String str, String str2) throws RemoteException;

    WifiActivityEnergyInfo reportActivityInfo() throws RemoteException;

    void requestActivityInfo(ResultReceiver resultReceiver) throws RemoteException;

    void restoreBackupData(byte[] bArr) throws RemoteException;

    void restoreSupplicantBackupData(byte[] bArr, byte[] bArr2) throws RemoteException;

    byte[] retrieveBackupData() throws RemoteException;

    void setCountryCode(String str) throws RemoteException;

    void setDeviceMobilityState(int i) throws RemoteException;

    boolean setWifiApConfiguration(WifiConfiguration wifiConfiguration, String str) throws RemoteException;

    boolean setWifiEnabled(String str, boolean z) throws RemoteException;

    void startDppAsConfiguratorInitiator(IBinder iBinder, String str, int i, int i2, IDppCallback iDppCallback) throws RemoteException;

    void startDppAsEnrolleeInitiator(IBinder iBinder, String str, IDppCallback iDppCallback) throws RemoteException;

    int startLocalOnlyHotspot(Messenger messenger, IBinder iBinder, String str) throws RemoteException;

    boolean startScan(String str) throws RemoteException;

    boolean startSoftAp(WifiConfiguration wifiConfiguration) throws RemoteException;

    void startSubscriptionProvisioning(OsuProvider osuProvider, IProvisioningCallback iProvisioningCallback) throws RemoteException;

    void startWatchLocalOnlyHotspot(Messenger messenger, IBinder iBinder) throws RemoteException;

    void stopDppSession() throws RemoteException;

    void stopLocalOnlyHotspot() throws RemoteException;

    boolean stopSoftAp() throws RemoteException;

    void stopWatchLocalOnlyHotspot() throws RemoteException;

    void unregisterNetworkRequestMatchCallback(int i) throws RemoteException;

    void unregisterSoftApCallback(int i) throws RemoteException;

    void unregisterTrafficStateCallback(int i) throws RemoteException;

    void updateInterfaceIpState(String str, int i) throws RemoteException;

    void updateWifiLockWorkSource(IBinder iBinder, WorkSource workSource) throws RemoteException;

    void updateWifiUsabilityScore(int i, int i2, int i3) throws RemoteException;
}
