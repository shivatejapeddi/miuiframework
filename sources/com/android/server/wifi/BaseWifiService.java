package com.android.server.wifi;

import android.content.pm.ParceledListSlice;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.wifi.IDppCallback;
import android.net.wifi.INetworkRequestMatchCallback;
import android.net.wifi.IOnWifiUsabilityStatsListener;
import android.net.wifi.ISoftApCallback;
import android.net.wifi.ITrafficStateCallback;
import android.net.wifi.IWifiManager.Stub;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiActivityEnergyInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiDppConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiNetworkSuggestion;
import android.net.wifi.hotspot2.IProvisioningCallback;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.WorkSource;
import java.util.List;
import java.util.Map;

public class BaseWifiService extends Stub {
    private static final String TAG = BaseWifiService.class.getSimpleName();

    public long getSupportedFeatures() {
        throw new UnsupportedOperationException();
    }

    public WifiActivityEnergyInfo reportActivityInfo() {
        throw new UnsupportedOperationException();
    }

    public void requestActivityInfo(ResultReceiver result) {
        throw new UnsupportedOperationException();
    }

    public ParceledListSlice getConfiguredNetworks(String packageName) {
        throw new UnsupportedOperationException();
    }

    public ParceledListSlice getPrivilegedConfiguredNetworks(String packageName) {
        throw new UnsupportedOperationException();
    }

    public Map<String, Map<Integer, List<ScanResult>>> getAllMatchingFqdnsForScanResults(List<ScanResult> list) {
        throw new UnsupportedOperationException();
    }

    public Map<OsuProvider, List<ScanResult>> getMatchingOsuProviders(List<ScanResult> list) {
        throw new UnsupportedOperationException();
    }

    public Map<OsuProvider, PasspointConfiguration> getMatchingPasspointConfigsForOsuProviders(List<OsuProvider> list) {
        throw new UnsupportedOperationException();
    }

    public int addOrUpdateNetwork(WifiConfiguration config, String packageName) {
        throw new UnsupportedOperationException();
    }

    public boolean addOrUpdatePasspointConfiguration(PasspointConfiguration config, String packageName) {
        throw new UnsupportedOperationException();
    }

    public boolean removePasspointConfiguration(String fqdn, String packageName) {
        throw new UnsupportedOperationException();
    }

    public List<PasspointConfiguration> getPasspointConfigurations(String packageName) {
        throw new UnsupportedOperationException();
    }

    public List<WifiConfiguration> getWifiConfigsForPasspointProfiles(List<String> list) {
        throw new UnsupportedOperationException();
    }

    public void queryPasspointIcon(long bssid, String fileName) {
        throw new UnsupportedOperationException();
    }

    public int matchProviderWithCurrentNetwork(String fqdn) {
        throw new UnsupportedOperationException();
    }

    public void deauthenticateNetwork(long holdoff, boolean ess) {
        throw new UnsupportedOperationException();
    }

    public boolean removeNetwork(int netId, String packageName) {
        throw new UnsupportedOperationException();
    }

    public boolean enableNetwork(int netId, boolean disableOthers, String packageName) {
        throw new UnsupportedOperationException();
    }

    public boolean disableNetwork(int netId, String packageName) {
        throw new UnsupportedOperationException();
    }

    public boolean startScan(String packageName) {
        throw new UnsupportedOperationException();
    }

    public List<ScanResult> getScanResults(String callingPackage) {
        throw new UnsupportedOperationException();
    }

    public boolean disconnect(String packageName) {
        throw new UnsupportedOperationException();
    }

    public boolean reconnect(String packageName) {
        throw new UnsupportedOperationException();
    }

    public boolean reassociate(String packageName) {
        throw new UnsupportedOperationException();
    }

    public WifiInfo getConnectionInfo(String callingPackage) {
        throw new UnsupportedOperationException();
    }

    public boolean setWifiEnabled(String packageName, boolean enable) {
        throw new UnsupportedOperationException();
    }

    public int getWifiEnabledState() {
        throw new UnsupportedOperationException();
    }

    public void setCountryCode(String country) {
        throw new UnsupportedOperationException();
    }

    public String getCountryCode() {
        throw new UnsupportedOperationException();
    }

    public boolean isDualBandSupported() {
        throw new UnsupportedOperationException();
    }

    public boolean needs5GHzToAnyApBandConversion() {
        throw new UnsupportedOperationException();
    }

    public DhcpInfo getDhcpInfo() {
        throw new UnsupportedOperationException();
    }

    public boolean isScanAlwaysAvailable() {
        throw new UnsupportedOperationException();
    }

    public boolean acquireWifiLock(IBinder lock, int lockType, String tag, WorkSource ws) {
        throw new UnsupportedOperationException();
    }

    public void updateWifiLockWorkSource(IBinder lock, WorkSource ws) {
        throw new UnsupportedOperationException();
    }

    public boolean releaseWifiLock(IBinder lock) {
        throw new UnsupportedOperationException();
    }

    public void initializeMulticastFiltering() {
        throw new UnsupportedOperationException();
    }

    public boolean isMulticastEnabled() {
        throw new UnsupportedOperationException();
    }

    public void acquireMulticastLock(IBinder binder, String tag) {
        throw new UnsupportedOperationException();
    }

    public void releaseMulticastLock(String tag) {
        throw new UnsupportedOperationException();
    }

    public void updateInterfaceIpState(String ifaceName, int mode) {
        throw new UnsupportedOperationException();
    }

    public boolean startSoftAp(WifiConfiguration wifiConfig) {
        throw new UnsupportedOperationException();
    }

    public boolean stopSoftAp() {
        throw new UnsupportedOperationException();
    }

    public int startLocalOnlyHotspot(Messenger messenger, IBinder binder, String packageName) {
        throw new UnsupportedOperationException();
    }

    public void stopLocalOnlyHotspot() {
        throw new UnsupportedOperationException();
    }

    public void startWatchLocalOnlyHotspot(Messenger messenger, IBinder binder) {
        throw new UnsupportedOperationException();
    }

    public void stopWatchLocalOnlyHotspot() {
        throw new UnsupportedOperationException();
    }

    public int getWifiApEnabledState() {
        throw new UnsupportedOperationException();
    }

    public WifiConfiguration getWifiApConfiguration() {
        throw new UnsupportedOperationException();
    }

    public boolean setWifiApConfiguration(WifiConfiguration wifiConfig, String packageName) {
        throw new UnsupportedOperationException();
    }

    public void notifyUserOfApBandConversion(String packageName) {
        throw new UnsupportedOperationException();
    }

    public Messenger getWifiServiceMessenger(String packageName) {
        throw new UnsupportedOperationException();
    }

    public void enableTdls(String remoteIPAddress, boolean enable) {
        throw new UnsupportedOperationException();
    }

    public void enableTdlsWithMacAddress(String remoteMacAddress, boolean enable) {
        throw new UnsupportedOperationException();
    }

    public String getCurrentNetworkWpsNfcConfigurationToken() {
        throw new UnsupportedOperationException();
    }

    public void enableVerboseLogging(int verbose) {
        throw new UnsupportedOperationException();
    }

    public int getVerboseLoggingLevel() {
        throw new UnsupportedOperationException();
    }

    public void enableWifiConnectivityManager(boolean enabled) {
        throw new UnsupportedOperationException();
    }

    public void disableEphemeralNetwork(String SSID, String packageName) {
        throw new UnsupportedOperationException();
    }

    public void factoryReset(String packageName) {
        throw new UnsupportedOperationException();
    }

    public Network getCurrentNetwork() {
        throw new UnsupportedOperationException();
    }

    public byte[] retrieveBackupData() {
        throw new UnsupportedOperationException();
    }

    public void restoreBackupData(byte[] data) {
        throw new UnsupportedOperationException();
    }

    public void restoreSupplicantBackupData(byte[] supplicantData, byte[] ipConfigData) {
        throw new UnsupportedOperationException();
    }

    public void startSubscriptionProvisioning(OsuProvider provider, IProvisioningCallback callback) {
        throw new UnsupportedOperationException();
    }

    public void registerSoftApCallback(IBinder binder, ISoftApCallback callback, int callbackIdentifier) {
        throw new UnsupportedOperationException();
    }

    public void unregisterSoftApCallback(int callbackIdentifier) {
        throw new UnsupportedOperationException();
    }

    public void registerTrafficStateCallback(IBinder binder, ITrafficStateCallback callback, int callbackIdentifier) {
        throw new UnsupportedOperationException();
    }

    public void unregisterTrafficStateCallback(int callbackIdentifier) {
        throw new UnsupportedOperationException();
    }

    public void registerNetworkRequestMatchCallback(IBinder binder, INetworkRequestMatchCallback callback, int callbackIdentifier) {
        throw new UnsupportedOperationException();
    }

    public void unregisterNetworkRequestMatchCallback(int callbackIdentifier) {
        throw new UnsupportedOperationException();
    }

    public int addNetworkSuggestions(List<WifiNetworkSuggestion> list, String callingPackageName) {
        throw new UnsupportedOperationException();
    }

    public int removeNetworkSuggestions(List<WifiNetworkSuggestion> list, String callingPackageName) {
        throw new UnsupportedOperationException();
    }

    public String[] getFactoryMacAddresses() {
        throw new UnsupportedOperationException();
    }

    public void setDeviceMobilityState(int state) {
        throw new UnsupportedOperationException();
    }

    public void startDppAsConfiguratorInitiator(IBinder binder, String enrolleeUri, int selectedNetworkId, int netRole, IDppCallback callback) {
        throw new UnsupportedOperationException();
    }

    public void startDppAsEnrolleeInitiator(IBinder binder, String configuratorUri, IDppCallback callback) {
        throw new UnsupportedOperationException();
    }

    public void enableWifiCoverageExtendFeature(boolean enable) {
        throw new UnsupportedOperationException();
    }

    public void stopDppSession() throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public void addOnWifiUsabilityStatsListener(IBinder binder, IOnWifiUsabilityStatsListener listener, int listenerIdentifier) {
        throw new UnsupportedOperationException();
    }

    public void removeOnWifiUsabilityStatsListener(int listenerIdentifier) {
        throw new UnsupportedOperationException();
    }

    public void updateWifiUsabilityScore(int seqNum, int score, int predictionHorizonSec) {
        throw new UnsupportedOperationException();
    }

    public boolean isWifiCoverageExtendFeatureEnabled() {
        throw new UnsupportedOperationException();
    }

    public boolean isExtendingWifi() {
        throw new UnsupportedOperationException();
    }

    public String getCapabilities(String capaType) {
        throw new UnsupportedOperationException();
    }

    public int dppAddBootstrapQrCode(String uri) {
        throw new UnsupportedOperationException();
    }

    public int dppBootstrapGenerate(WifiDppConfig config) {
        throw new UnsupportedOperationException();
    }

    public String dppGetUri(int bootstrap_id) {
        throw new UnsupportedOperationException();
    }

    public int dppBootstrapRemove(int bootstrap_id) {
        throw new UnsupportedOperationException();
    }

    public int dppListen(String frequency, int dpp_role, boolean qr_mutual, boolean netrole_ap) {
        throw new UnsupportedOperationException();
    }

    public void dppStopListen() {
        throw new UnsupportedOperationException();
    }

    public int dppConfiguratorAdd(String curve, String key, int expiry) {
        throw new UnsupportedOperationException();
    }

    public int dppConfiguratorRemove(int config_id) {
        throw new UnsupportedOperationException();
    }

    public int dppStartAuth(WifiDppConfig config) {
        throw new UnsupportedOperationException();
    }

    public String dppConfiguratorGetKey(int id) {
        throw new UnsupportedOperationException();
    }
}
