package android.net;

import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ResultReceiver;
import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.internal.net.VpnProfile;
import java.io.FileDescriptor;
import java.util.List;

public interface IConnectivityManager extends IInterface {

    public static class Default implements IConnectivityManager {
        public Network getActiveNetwork() throws RemoteException {
            return null;
        }

        public Network getActiveNetworkForUid(int uid, boolean ignoreBlocked) throws RemoteException {
            return null;
        }

        public NetworkInfo getActiveNetworkInfo() throws RemoteException {
            return null;
        }

        public NetworkInfo getActiveNetworkInfoForUid(int uid, boolean ignoreBlocked) throws RemoteException {
            return null;
        }

        public NetworkInfo getNetworkInfo(int networkType) throws RemoteException {
            return null;
        }

        public NetworkInfo getNetworkInfoForUid(Network network, int uid, boolean ignoreBlocked) throws RemoteException {
            return null;
        }

        public NetworkInfo[] getAllNetworkInfo() throws RemoteException {
            return null;
        }

        public Network getNetworkForType(int networkType) throws RemoteException {
            return null;
        }

        public Network[] getAllNetworks() throws RemoteException {
            return null;
        }

        public NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int userId) throws RemoteException {
            return null;
        }

        public boolean isNetworkSupported(int networkType) throws RemoteException {
            return false;
        }

        public LinkProperties getActiveLinkProperties() throws RemoteException {
            return null;
        }

        public LinkProperties getLinkPropertiesForType(int networkType) throws RemoteException {
            return null;
        }

        public LinkProperties getLinkProperties(Network network) throws RemoteException {
            return null;
        }

        public NetworkCapabilities getNetworkCapabilities(Network network) throws RemoteException {
            return null;
        }

        public NetworkState[] getAllNetworkState() throws RemoteException {
            return null;
        }

        public NetworkQuotaInfo getActiveNetworkQuotaInfo() throws RemoteException {
            return null;
        }

        public boolean isActiveNetworkMetered() throws RemoteException {
            return false;
        }

        public boolean requestRouteToHostAddress(int networkType, byte[] hostAddress) throws RemoteException {
            return false;
        }

        public int tether(String iface, String callerPkg) throws RemoteException {
            return 0;
        }

        public int untether(String iface, String callerPkg) throws RemoteException {
            return 0;
        }

        public int getLastTetherError(String iface) throws RemoteException {
            return 0;
        }

        public boolean isTetheringSupported(String callerPkg) throws RemoteException {
            return false;
        }

        public void startTethering(int type, ResultReceiver receiver, boolean showProvisioningUi, String callerPkg) throws RemoteException {
        }

        public void stopTethering(int type, String callerPkg) throws RemoteException {
        }

        public String[] getTetherableIfaces() throws RemoteException {
            return null;
        }

        public String[] getTetheredIfaces() throws RemoteException {
            return null;
        }

        public String[] getTetheringErroredIfaces() throws RemoteException {
            return null;
        }

        public String[] getTetheredDhcpRanges() throws RemoteException {
            return null;
        }

        public String[] getTetherableUsbRegexs() throws RemoteException {
            return null;
        }

        public String[] getTetherableWifiRegexs() throws RemoteException {
            return null;
        }

        public String[] getTetherableBluetoothRegexs() throws RemoteException {
            return null;
        }

        public int setUsbTethering(boolean enable, String callerPkg) throws RemoteException {
            return 0;
        }

        public void reportInetCondition(int networkType, int percentage) throws RemoteException {
        }

        public void reportNetworkConnectivity(Network network, boolean hasConnectivity) throws RemoteException {
        }

        public ProxyInfo getGlobalProxy() throws RemoteException {
            return null;
        }

        public void setGlobalProxy(ProxyInfo p) throws RemoteException {
        }

        public ProxyInfo getProxyForNetwork(Network nework) throws RemoteException {
            return null;
        }

        public boolean prepareVpn(String oldPackage, String newPackage, int userId) throws RemoteException {
            return false;
        }

        public void setVpnPackageAuthorization(String packageName, int userId, boolean authorized) throws RemoteException {
        }

        public ParcelFileDescriptor establishVpn(VpnConfig config) throws RemoteException {
            return null;
        }

        public VpnConfig getVpnConfig(int userId) throws RemoteException {
            return null;
        }

        public void startLegacyVpn(VpnProfile profile) throws RemoteException {
        }

        public LegacyVpnInfo getLegacyVpnInfo(int userId) throws RemoteException {
            return null;
        }

        public boolean updateLockdownVpn() throws RemoteException {
            return false;
        }

        public boolean isAlwaysOnVpnPackageSupported(int userId, String packageName) throws RemoteException {
            return false;
        }

        public boolean setAlwaysOnVpnPackage(int userId, String packageName, boolean lockdown, List<String> list) throws RemoteException {
            return false;
        }

        public String getAlwaysOnVpnPackage(int userId) throws RemoteException {
            return null;
        }

        public boolean isVpnLockdownEnabled(int userId) throws RemoteException {
            return false;
        }

        public List<String> getVpnLockdownWhitelist(int userId) throws RemoteException {
            return null;
        }

        public int checkMobileProvisioning(int suggestedTimeOutMs) throws RemoteException {
            return 0;
        }

        public String getMobileProvisioningUrl() throws RemoteException {
            return null;
        }

        public void setProvisioningNotificationVisible(boolean visible, int networkType, String action) throws RemoteException {
        }

        public void setAirplaneMode(boolean enable) throws RemoteException {
        }

        public int registerNetworkFactory(Messenger messenger, String name) throws RemoteException {
            return 0;
        }

        public boolean requestBandwidthUpdate(Network network) throws RemoteException {
            return false;
        }

        public void unregisterNetworkFactory(Messenger messenger) throws RemoteException {
        }

        public int registerNetworkAgent(Messenger messenger, NetworkInfo ni, LinkProperties lp, NetworkCapabilities nc, int score, NetworkMisc misc, int factorySerialNumber) throws RemoteException {
            return 0;
        }

        public NetworkRequest requestNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, int timeoutSec, IBinder binder, int legacy) throws RemoteException {
            return null;
        }

        public NetworkRequest pendingRequestForNetwork(NetworkCapabilities networkCapabilities, PendingIntent operation) throws RemoteException {
            return null;
        }

        public void releasePendingNetworkRequest(PendingIntent operation) throws RemoteException {
        }

        public NetworkRequest listenForNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, IBinder binder) throws RemoteException {
            return null;
        }

        public void pendingListenForNetwork(NetworkCapabilities networkCapabilities, PendingIntent operation) throws RemoteException {
        }

        public void releaseNetworkRequest(NetworkRequest networkRequest) throws RemoteException {
        }

        public void setAcceptUnvalidated(Network network, boolean accept, boolean always) throws RemoteException {
        }

        public void setAcceptPartialConnectivity(Network network, boolean accept, boolean always) throws RemoteException {
        }

        public void setAvoidUnvalidated(Network network) throws RemoteException {
        }

        public void startCaptivePortalApp(Network network) throws RemoteException {
        }

        public void startCaptivePortalAppInternal(Network network, Bundle appExtras) throws RemoteException {
        }

        public boolean shouldAvoidBadWifi() throws RemoteException {
            return false;
        }

        public int getMultipathPreference(Network Network) throws RemoteException {
            return 0;
        }

        public NetworkRequest getDefaultRequest() throws RemoteException {
            return null;
        }

        public int getRestoreDefaultNetworkDelay(int networkType) throws RemoteException {
            return 0;
        }

        public boolean addVpnAddress(String address, int prefixLength) throws RemoteException {
            return false;
        }

        public boolean removeVpnAddress(String address, int prefixLength) throws RemoteException {
            return false;
        }

        public boolean setUnderlyingNetworksForVpn(Network[] networks) throws RemoteException {
            return false;
        }

        public void factoryReset() throws RemoteException {
        }

        public void startNattKeepalive(Network network, int intervalSeconds, ISocketKeepaliveCallback cb, String srcAddr, int srcPort, String dstAddr) throws RemoteException {
        }

        public void startNattKeepaliveWithFd(Network network, FileDescriptor fd, int resourceId, int intervalSeconds, ISocketKeepaliveCallback cb, String srcAddr, String dstAddr) throws RemoteException {
        }

        public void startTcpKeepalive(Network network, FileDescriptor fd, int intervalSeconds, ISocketKeepaliveCallback cb) throws RemoteException {
        }

        public void stopKeepalive(Network network, int slot) throws RemoteException {
        }

        public String getCaptivePortalServerUrl() throws RemoteException {
            return null;
        }

        public byte[] getNetworkWatchlistConfigHash() throws RemoteException {
            return null;
        }

        public int getConnectionOwnerUid(ConnectionInfo connectionInfo) throws RemoteException {
            return 0;
        }

        public boolean isCallerCurrentAlwaysOnVpnApp() throws RemoteException {
            return false;
        }

        public boolean isCallerCurrentAlwaysOnVpnLockdownApp() throws RemoteException {
            return false;
        }

        public void getLatestTetheringEntitlementResult(int type, ResultReceiver receiver, boolean showEntitlementUi, String callerPkg) throws RemoteException {
        }

        public void registerTetheringEventCallback(ITetheringEventCallback callback, String callerPkg) throws RemoteException {
        }

        public void unregisterTetheringEventCallback(ITetheringEventCallback callback, String callerPkg) throws RemoteException {
        }

        public IBinder startOrGetTestNetworkService() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IConnectivityManager {
        private static final String DESCRIPTOR = "android.net.IConnectivityManager";
        static final int TRANSACTION_addVpnAddress = 74;
        static final int TRANSACTION_checkMobileProvisioning = 51;
        static final int TRANSACTION_establishVpn = 41;
        static final int TRANSACTION_factoryReset = 77;
        static final int TRANSACTION_getActiveLinkProperties = 12;
        static final int TRANSACTION_getActiveNetwork = 1;
        static final int TRANSACTION_getActiveNetworkForUid = 2;
        static final int TRANSACTION_getActiveNetworkInfo = 3;
        static final int TRANSACTION_getActiveNetworkInfoForUid = 4;
        static final int TRANSACTION_getActiveNetworkQuotaInfo = 17;
        static final int TRANSACTION_getAllNetworkInfo = 7;
        static final int TRANSACTION_getAllNetworkState = 16;
        static final int TRANSACTION_getAllNetworks = 9;
        static final int TRANSACTION_getAlwaysOnVpnPackage = 48;
        static final int TRANSACTION_getCaptivePortalServerUrl = 82;
        static final int TRANSACTION_getConnectionOwnerUid = 84;
        static final int TRANSACTION_getDefaultNetworkCapabilitiesForUser = 10;
        static final int TRANSACTION_getDefaultRequest = 72;
        static final int TRANSACTION_getGlobalProxy = 36;
        static final int TRANSACTION_getLastTetherError = 22;
        static final int TRANSACTION_getLatestTetheringEntitlementResult = 87;
        static final int TRANSACTION_getLegacyVpnInfo = 44;
        static final int TRANSACTION_getLinkProperties = 14;
        static final int TRANSACTION_getLinkPropertiesForType = 13;
        static final int TRANSACTION_getMobileProvisioningUrl = 52;
        static final int TRANSACTION_getMultipathPreference = 71;
        static final int TRANSACTION_getNetworkCapabilities = 15;
        static final int TRANSACTION_getNetworkForType = 8;
        static final int TRANSACTION_getNetworkInfo = 5;
        static final int TRANSACTION_getNetworkInfoForUid = 6;
        static final int TRANSACTION_getNetworkWatchlistConfigHash = 83;
        static final int TRANSACTION_getProxyForNetwork = 38;
        static final int TRANSACTION_getRestoreDefaultNetworkDelay = 73;
        static final int TRANSACTION_getTetherableBluetoothRegexs = 32;
        static final int TRANSACTION_getTetherableIfaces = 26;
        static final int TRANSACTION_getTetherableUsbRegexs = 30;
        static final int TRANSACTION_getTetherableWifiRegexs = 31;
        static final int TRANSACTION_getTetheredDhcpRanges = 29;
        static final int TRANSACTION_getTetheredIfaces = 27;
        static final int TRANSACTION_getTetheringErroredIfaces = 28;
        static final int TRANSACTION_getVpnConfig = 42;
        static final int TRANSACTION_getVpnLockdownWhitelist = 50;
        static final int TRANSACTION_isActiveNetworkMetered = 18;
        static final int TRANSACTION_isAlwaysOnVpnPackageSupported = 46;
        static final int TRANSACTION_isCallerCurrentAlwaysOnVpnApp = 85;
        static final int TRANSACTION_isCallerCurrentAlwaysOnVpnLockdownApp = 86;
        static final int TRANSACTION_isNetworkSupported = 11;
        static final int TRANSACTION_isTetheringSupported = 23;
        static final int TRANSACTION_isVpnLockdownEnabled = 49;
        static final int TRANSACTION_listenForNetwork = 62;
        static final int TRANSACTION_pendingListenForNetwork = 63;
        static final int TRANSACTION_pendingRequestForNetwork = 60;
        static final int TRANSACTION_prepareVpn = 39;
        static final int TRANSACTION_registerNetworkAgent = 58;
        static final int TRANSACTION_registerNetworkFactory = 55;
        static final int TRANSACTION_registerTetheringEventCallback = 88;
        static final int TRANSACTION_releaseNetworkRequest = 64;
        static final int TRANSACTION_releasePendingNetworkRequest = 61;
        static final int TRANSACTION_removeVpnAddress = 75;
        static final int TRANSACTION_reportInetCondition = 34;
        static final int TRANSACTION_reportNetworkConnectivity = 35;
        static final int TRANSACTION_requestBandwidthUpdate = 56;
        static final int TRANSACTION_requestNetwork = 59;
        static final int TRANSACTION_requestRouteToHostAddress = 19;
        static final int TRANSACTION_setAcceptPartialConnectivity = 66;
        static final int TRANSACTION_setAcceptUnvalidated = 65;
        static final int TRANSACTION_setAirplaneMode = 54;
        static final int TRANSACTION_setAlwaysOnVpnPackage = 47;
        static final int TRANSACTION_setAvoidUnvalidated = 67;
        static final int TRANSACTION_setGlobalProxy = 37;
        static final int TRANSACTION_setProvisioningNotificationVisible = 53;
        static final int TRANSACTION_setUnderlyingNetworksForVpn = 76;
        static final int TRANSACTION_setUsbTethering = 33;
        static final int TRANSACTION_setVpnPackageAuthorization = 40;
        static final int TRANSACTION_shouldAvoidBadWifi = 70;
        static final int TRANSACTION_startCaptivePortalApp = 68;
        static final int TRANSACTION_startCaptivePortalAppInternal = 69;
        static final int TRANSACTION_startLegacyVpn = 43;
        static final int TRANSACTION_startNattKeepalive = 78;
        static final int TRANSACTION_startNattKeepaliveWithFd = 79;
        static final int TRANSACTION_startOrGetTestNetworkService = 90;
        static final int TRANSACTION_startTcpKeepalive = 80;
        static final int TRANSACTION_startTethering = 24;
        static final int TRANSACTION_stopKeepalive = 81;
        static final int TRANSACTION_stopTethering = 25;
        static final int TRANSACTION_tether = 20;
        static final int TRANSACTION_unregisterNetworkFactory = 57;
        static final int TRANSACTION_unregisterTetheringEventCallback = 89;
        static final int TRANSACTION_untether = 21;
        static final int TRANSACTION_updateLockdownVpn = 45;

        private static class Proxy implements IConnectivityManager {
            public static IConnectivityManager sDefaultImpl;
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

            public Network getActiveNetwork() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Network network = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        network = Stub.getDefaultImpl();
                        if (network != 0) {
                            network = Stub.getDefaultImpl().getActiveNetwork();
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

            public Network getActiveNetworkForUid(int uid, boolean ignoreBlocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(ignoreBlocked ? 1 : 0);
                    Network network = this.mRemote;
                    if (!network.transact(2, _data, _reply, 0)) {
                        network = Stub.getDefaultImpl();
                        if (network != null) {
                            network = Stub.getDefaultImpl().getActiveNetworkForUid(uid, ignoreBlocked);
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

            public NetworkInfo getActiveNetworkInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkInfo networkInfo = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        networkInfo = Stub.getDefaultImpl();
                        if (networkInfo != 0) {
                            networkInfo = Stub.getDefaultImpl().getActiveNetworkInfo();
                            return networkInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkInfo = (NetworkInfo) NetworkInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        networkInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkInfo getActiveNetworkInfoForUid(int uid, boolean ignoreBlocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(ignoreBlocked ? 1 : 0);
                    NetworkInfo networkInfo = this.mRemote;
                    if (!networkInfo.transact(4, _data, _reply, 0)) {
                        networkInfo = Stub.getDefaultImpl();
                        if (networkInfo != null) {
                            networkInfo = Stub.getDefaultImpl().getActiveNetworkInfoForUid(uid, ignoreBlocked);
                            return networkInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkInfo = (NetworkInfo) NetworkInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        networkInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkInfo getNetworkInfo(int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    NetworkInfo networkInfo = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        networkInfo = Stub.getDefaultImpl();
                        if (networkInfo != 0) {
                            networkInfo = Stub.getDefaultImpl().getNetworkInfo(networkType);
                            return networkInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkInfo = (NetworkInfo) NetworkInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        networkInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkInfo getNetworkInfoForUid(Network network, int uid, boolean ignoreBlocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    NetworkInfo networkInfo = 0;
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    if (!ignoreBlocked) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        networkInfo = Stub.getDefaultImpl();
                        if (networkInfo != 0) {
                            networkInfo = Stub.getDefaultImpl().getNetworkInfoForUid(network, uid, ignoreBlocked);
                            return networkInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkInfo = (NetworkInfo) NetworkInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        networkInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkInfo[] getAllNetworkInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkInfo[] networkInfoArr = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        networkInfoArr = Stub.getDefaultImpl();
                        if (networkInfoArr != 0) {
                            networkInfoArr = Stub.getDefaultImpl().getAllNetworkInfo();
                            return networkInfoArr;
                        }
                    }
                    _reply.readException();
                    NetworkInfo[] _result = (NetworkInfo[]) _reply.createTypedArray(NetworkInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Network getNetworkForType(int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    Network network = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        network = Stub.getDefaultImpl();
                        if (network != 0) {
                            network = Stub.getDefaultImpl().getNetworkForType(networkType);
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

            public Network[] getAllNetworks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Network[] networkArr = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        networkArr = Stub.getDefaultImpl();
                        if (networkArr != 0) {
                            networkArr = Stub.getDefaultImpl().getAllNetworks();
                            return networkArr;
                        }
                    }
                    _reply.readException();
                    Network[] _result = (Network[]) _reply.createTypedArray(Network.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    NetworkCapabilities[] networkCapabilitiesArr = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        networkCapabilitiesArr = Stub.getDefaultImpl();
                        if (networkCapabilitiesArr != 0) {
                            networkCapabilitiesArr = Stub.getDefaultImpl().getDefaultNetworkCapabilitiesForUser(userId);
                            return networkCapabilitiesArr;
                        }
                    }
                    _reply.readException();
                    NetworkCapabilities[] _result = (NetworkCapabilities[]) _reply.createTypedArray(NetworkCapabilities.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isNetworkSupported(int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isNetworkSupported(networkType);
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

            public LinkProperties getActiveLinkProperties() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    LinkProperties linkProperties = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        linkProperties = Stub.getDefaultImpl();
                        if (linkProperties != 0) {
                            linkProperties = Stub.getDefaultImpl().getActiveLinkProperties();
                            return linkProperties;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        linkProperties = (LinkProperties) LinkProperties.CREATOR.createFromParcel(_reply);
                    } else {
                        linkProperties = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return linkProperties;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public LinkProperties getLinkPropertiesForType(int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    LinkProperties linkProperties = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        linkProperties = Stub.getDefaultImpl();
                        if (linkProperties != 0) {
                            linkProperties = Stub.getDefaultImpl().getLinkPropertiesForType(networkType);
                            return linkProperties;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        linkProperties = (LinkProperties) LinkProperties.CREATOR.createFromParcel(_reply);
                    } else {
                        linkProperties = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return linkProperties;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public LinkProperties getLinkProperties(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    LinkProperties linkProperties = this.mRemote;
                    if (!linkProperties.transact(14, _data, _reply, 0)) {
                        linkProperties = Stub.getDefaultImpl();
                        if (linkProperties != null) {
                            linkProperties = Stub.getDefaultImpl().getLinkProperties(network);
                            return linkProperties;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        linkProperties = (LinkProperties) LinkProperties.CREATOR.createFromParcel(_reply);
                    } else {
                        linkProperties = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return linkProperties;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkCapabilities getNetworkCapabilities(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    NetworkCapabilities networkCapabilities = this.mRemote;
                    if (!networkCapabilities.transact(15, _data, _reply, 0)) {
                        networkCapabilities = Stub.getDefaultImpl();
                        if (networkCapabilities != null) {
                            networkCapabilities = Stub.getDefaultImpl().getNetworkCapabilities(network);
                            return networkCapabilities;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkCapabilities = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(_reply);
                    } else {
                        networkCapabilities = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkCapabilities;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkState[] getAllNetworkState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkState[] networkStateArr = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        networkStateArr = Stub.getDefaultImpl();
                        if (networkStateArr != 0) {
                            networkStateArr = Stub.getDefaultImpl().getAllNetworkState();
                            return networkStateArr;
                        }
                    }
                    _reply.readException();
                    NetworkState[] _result = (NetworkState[]) _reply.createTypedArray(NetworkState.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkQuotaInfo getActiveNetworkQuotaInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkQuotaInfo networkQuotaInfo = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        networkQuotaInfo = Stub.getDefaultImpl();
                        if (networkQuotaInfo != 0) {
                            networkQuotaInfo = Stub.getDefaultImpl().getActiveNetworkQuotaInfo();
                            return networkQuotaInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkQuotaInfo = (NetworkQuotaInfo) NetworkQuotaInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        networkQuotaInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkQuotaInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isActiveNetworkMetered() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isActiveNetworkMetered();
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

            public boolean requestRouteToHostAddress(int networkType, byte[] hostAddress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeByteArray(hostAddress);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().requestRouteToHostAddress(networkType, hostAddress);
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

            public int tether(String iface, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(callerPkg);
                    int i = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().tether(iface, callerPkg);
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

            public int untether(String iface, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(callerPkg);
                    int i = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().untether(iface, callerPkg);
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

            public int getLastTetherError(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    int i = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getLastTetherError(iface);
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

            public boolean isTetheringSupported(String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isTetheringSupported(callerPkg);
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

            public void startTethering(int type, ResultReceiver receiver, boolean showProvisioningUi, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    int i = 1;
                    if (receiver != null) {
                        _data.writeInt(1);
                        receiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!showProvisioningUi) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeString(callerPkg);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startTethering(type, receiver, showProvisioningUi, callerPkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopTethering(int type, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callerPkg);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopTethering(type, callerPkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getTetherableIfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getTetherableIfaces();
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

            public String[] getTetheredIfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getTetheredIfaces();
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

            public String[] getTetheringErroredIfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getTetheringErroredIfaces();
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

            public String[] getTetheredDhcpRanges() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getTetheredDhcpRanges();
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

            public String[] getTetherableUsbRegexs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getTetherableUsbRegexs();
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

            public String[] getTetherableWifiRegexs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getTetherableWifiRegexs();
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

            public String[] getTetherableBluetoothRegexs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 32;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getTetherableBluetoothRegexs();
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

            public int setUsbTethering(boolean enable, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeString(callerPkg);
                    int i = this.mRemote;
                    if (!i.transact(33, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().setUsbTethering(enable, callerPkg);
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

            public void reportInetCondition(int networkType, int percentage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeInt(percentage);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportInetCondition(networkType, percentage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportNetworkConnectivity(Network network, boolean hasConnectivity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!hasConnectivity) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportNetworkConnectivity(network, hasConnectivity);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ProxyInfo getGlobalProxy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ProxyInfo proxyInfo = 36;
                    if (!this.mRemote.transact(36, _data, _reply, 0)) {
                        proxyInfo = Stub.getDefaultImpl();
                        if (proxyInfo != 0) {
                            proxyInfo = Stub.getDefaultImpl().getGlobalProxy();
                            return proxyInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        proxyInfo = (ProxyInfo) ProxyInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        proxyInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return proxyInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGlobalProxy(ProxyInfo p) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (p != null) {
                        _data.writeInt(1);
                        p.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setGlobalProxy(p);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ProxyInfo getProxyForNetwork(Network nework) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (nework != null) {
                        _data.writeInt(1);
                        nework.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    ProxyInfo proxyInfo = this.mRemote;
                    if (!proxyInfo.transact(38, _data, _reply, 0)) {
                        proxyInfo = Stub.getDefaultImpl();
                        if (proxyInfo != null) {
                            proxyInfo = Stub.getDefaultImpl().getProxyForNetwork(nework);
                            return proxyInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        proxyInfo = (ProxyInfo) ProxyInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        proxyInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return proxyInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean prepareVpn(String oldPackage, String newPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(oldPackage);
                    _data.writeString(newPackage);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(39, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().prepareVpn(oldPackage, newPackage, userId);
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

            public void setVpnPackageAuthorization(String packageName, int userId, boolean authorized) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(authorized ? 1 : 0);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVpnPackageAuthorization(packageName, userId, authorized);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor establishVpn(VpnConfig config) throws RemoteException {
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
                    ParcelFileDescriptor parcelFileDescriptor = this.mRemote;
                    if (!parcelFileDescriptor.transact(41, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != null) {
                            parcelFileDescriptor = Stub.getDefaultImpl().establishVpn(config);
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public VpnConfig getVpnConfig(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    VpnConfig vpnConfig = 42;
                    if (!this.mRemote.transact(42, _data, _reply, 0)) {
                        vpnConfig = Stub.getDefaultImpl();
                        if (vpnConfig != 0) {
                            vpnConfig = Stub.getDefaultImpl().getVpnConfig(userId);
                            return vpnConfig;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        vpnConfig = (VpnConfig) VpnConfig.CREATOR.createFromParcel(_reply);
                    } else {
                        vpnConfig = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return vpnConfig;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startLegacyVpn(VpnProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startLegacyVpn(profile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public LegacyVpnInfo getLegacyVpnInfo(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    LegacyVpnInfo legacyVpnInfo = 44;
                    if (!this.mRemote.transact(44, _data, _reply, 0)) {
                        legacyVpnInfo = Stub.getDefaultImpl();
                        if (legacyVpnInfo != 0) {
                            legacyVpnInfo = Stub.getDefaultImpl().getLegacyVpnInfo(userId);
                            return legacyVpnInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        legacyVpnInfo = (LegacyVpnInfo) LegacyVpnInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        legacyVpnInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return legacyVpnInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean updateLockdownVpn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(45, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().updateLockdownVpn();
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

            public boolean isAlwaysOnVpnPackageSupported(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(46, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAlwaysOnVpnPackageSupported(userId, packageName);
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

            public boolean setAlwaysOnVpnPackage(int userId, String packageName, boolean lockdown, List<String> lockdownWhitelist) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    boolean _result = true;
                    _data.writeInt(lockdown ? 1 : 0);
                    _data.writeStringList(lockdownWhitelist);
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setAlwaysOnVpnPackage(userId, packageName, lockdown, lockdownWhitelist);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getAlwaysOnVpnPackage(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    String str = 48;
                    if (!this.mRemote.transact(48, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getAlwaysOnVpnPackage(userId);
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

            public boolean isVpnLockdownEnabled(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(49, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVpnLockdownEnabled(userId);
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

            public List<String> getVpnLockdownWhitelist(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 50;
                    if (!this.mRemote.transact(50, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getVpnLockdownWhitelist(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkMobileProvisioning(int suggestedTimeOutMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(suggestedTimeOutMs);
                    int i = 51;
                    if (!this.mRemote.transact(51, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkMobileProvisioning(suggestedTimeOutMs);
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

            public String getMobileProvisioningUrl() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 52;
                    if (!this.mRemote.transact(52, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getMobileProvisioningUrl();
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

            public void setProvisioningNotificationVisible(boolean visible, int networkType, String action) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visible ? 1 : 0);
                    _data.writeInt(networkType);
                    _data.writeString(action);
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setProvisioningNotificationVisible(visible, networkType, action);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAirplaneMode(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAirplaneMode(enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int registerNetworkFactory(Messenger messenger, String name) throws RemoteException {
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
                    _data.writeString(name);
                    int i = this.mRemote;
                    if (!i.transact(55, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().registerNetworkFactory(messenger, name);
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

            public boolean requestBandwidthUpdate(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().requestBandwidthUpdate(network);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterNetworkFactory(Messenger messenger) throws RemoteException {
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
                    if (this.mRemote.transact(57, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterNetworkFactory(messenger);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int registerNetworkAgent(Messenger messenger, NetworkInfo ni, LinkProperties lp, NetworkCapabilities nc, int score, NetworkMisc misc, int factorySerialNumber) throws RemoteException {
                Messenger messenger2 = messenger;
                NetworkInfo networkInfo = ni;
                LinkProperties linkProperties = lp;
                NetworkCapabilities networkCapabilities = nc;
                NetworkMisc networkMisc = misc;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (messenger2 != null) {
                        _data.writeInt(1);
                        messenger2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (networkInfo != null) {
                        _data.writeInt(1);
                        networkInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (linkProperties != null) {
                        _data.writeInt(1);
                        linkProperties.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(score);
                    if (networkMisc != null) {
                        _data.writeInt(1);
                        networkMisc.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(factorySerialNumber);
                    if (!this.mRemote.transact(58, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().registerNetworkAgent(messenger, ni, lp, nc, score, misc, factorySerialNumber);
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

            public NetworkRequest requestNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, int timeoutSec, IBinder binder, int legacy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkRequest networkRequest = 0;
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(timeoutSec);
                    _data.writeStrongBinder(binder);
                    _data.writeInt(legacy);
                    if (!this.mRemote.transact(59, _data, _reply, 0)) {
                        networkRequest = Stub.getDefaultImpl();
                        if (networkRequest != 0) {
                            networkRequest = Stub.getDefaultImpl().requestNetwork(networkCapabilities, messenger, timeoutSec, binder, legacy);
                            return networkRequest;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkRequest = (NetworkRequest) NetworkRequest.CREATOR.createFromParcel(_reply);
                    } else {
                        networkRequest = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkRequest;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkRequest pendingRequestForNetwork(NetworkCapabilities networkCapabilities, PendingIntent operation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkRequest networkRequest = 0;
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (operation != null) {
                        _data.writeInt(1);
                        operation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(60, _data, _reply, 0)) {
                        networkRequest = Stub.getDefaultImpl();
                        if (networkRequest != 0) {
                            networkRequest = Stub.getDefaultImpl().pendingRequestForNetwork(networkCapabilities, operation);
                            return networkRequest;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkRequest = (NetworkRequest) NetworkRequest.CREATOR.createFromParcel(_reply);
                    } else {
                        networkRequest = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkRequest;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releasePendingNetworkRequest(PendingIntent operation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (operation != null) {
                        _data.writeInt(1);
                        operation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(61, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().releasePendingNetworkRequest(operation);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkRequest listenForNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkRequest networkRequest = 0;
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    if (!this.mRemote.transact(62, _data, _reply, 0)) {
                        networkRequest = Stub.getDefaultImpl();
                        if (networkRequest != 0) {
                            networkRequest = Stub.getDefaultImpl().listenForNetwork(networkCapabilities, messenger, binder);
                            return networkRequest;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkRequest = (NetworkRequest) NetworkRequest.CREATOR.createFromParcel(_reply);
                    } else {
                        networkRequest = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkRequest;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void pendingListenForNetwork(NetworkCapabilities networkCapabilities, PendingIntent operation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (operation != null) {
                        _data.writeInt(1);
                        operation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(63, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().pendingListenForNetwork(networkCapabilities, operation);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releaseNetworkRequest(NetworkRequest networkRequest) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkRequest != null) {
                        _data.writeInt(1);
                        networkRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(64, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().releaseNetworkRequest(networkRequest);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAcceptUnvalidated(Network network, boolean accept, boolean always) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(accept ? 1 : 0);
                    if (!always) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(65, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAcceptUnvalidated(network, accept, always);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAcceptPartialConnectivity(Network network, boolean accept, boolean always) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(accept ? 1 : 0);
                    if (!always) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(66, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAcceptPartialConnectivity(network, accept, always);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAvoidUnvalidated(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(67, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAvoidUnvalidated(network);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startCaptivePortalApp(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(68, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startCaptivePortalApp(network);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startCaptivePortalAppInternal(Network network, Bundle appExtras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (appExtras != null) {
                        _data.writeInt(1);
                        appExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(69, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startCaptivePortalAppInternal(network, appExtras);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean shouldAvoidBadWifi() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(70, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().shouldAvoidBadWifi();
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

            public int getMultipathPreference(Network Network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (Network != null) {
                        _data.writeInt(1);
                        Network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(71, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getMultipathPreference(Network);
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

            public NetworkRequest getDefaultRequest() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkRequest networkRequest = 72;
                    if (!this.mRemote.transact(72, _data, _reply, 0)) {
                        networkRequest = Stub.getDefaultImpl();
                        if (networkRequest != 0) {
                            networkRequest = Stub.getDefaultImpl().getDefaultRequest();
                            return networkRequest;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkRequest = (NetworkRequest) NetworkRequest.CREATOR.createFromParcel(_reply);
                    } else {
                        networkRequest = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkRequest;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRestoreDefaultNetworkDelay(int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    int i = 73;
                    if (!this.mRemote.transact(73, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getRestoreDefaultNetworkDelay(networkType);
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

            public boolean addVpnAddress(String address, int prefixLength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(prefixLength);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(74, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().addVpnAddress(address, prefixLength);
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

            public boolean removeVpnAddress(String address, int prefixLength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(prefixLength);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(75, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeVpnAddress(address, prefixLength);
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

            public boolean setUnderlyingNetworksForVpn(Network[] networks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = false;
                    _data.writeTypedArray(networks, 0);
                    if (this.mRemote.transact(76, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            _result = true;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setUnderlyingNetworksForVpn(networks);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void factoryReset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(77, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().factoryReset();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startNattKeepalive(Network network, int intervalSeconds, ISocketKeepaliveCallback cb, String srcAddr, int srcPort, String dstAddr) throws RemoteException {
                Throwable th;
                int i;
                String str;
                String str2;
                Network network2 = network;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network2 != null) {
                        _data.writeInt(1);
                        network2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(intervalSeconds);
                        _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                        try {
                            _data.writeString(srcAddr);
                        } catch (Throwable th2) {
                            th = th2;
                            i = srcPort;
                            str = dstAddr;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str2 = srcAddr;
                        i = srcPort;
                        str = dstAddr;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(srcPort);
                    } catch (Throwable th4) {
                        th = th4;
                        str = dstAddr;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(dstAddr);
                        try {
                            if (this.mRemote.transact(78, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().startNattKeepalive(network, intervalSeconds, cb, srcAddr, srcPort, dstAddr);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i2 = intervalSeconds;
                    str2 = srcAddr;
                    i = srcPort;
                    str = dstAddr;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void startNattKeepaliveWithFd(Network network, FileDescriptor fd, int resourceId, int intervalSeconds, ISocketKeepaliveCallback cb, String srcAddr, String dstAddr) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                Network network2 = network;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network2 != null) {
                        _data.writeInt(1);
                        network2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeRawFileDescriptor(fd);
                    } catch (Throwable th2) {
                        th = th2;
                        i = resourceId;
                        i2 = intervalSeconds;
                        str = srcAddr;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(resourceId);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = intervalSeconds;
                        str = srcAddr;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(intervalSeconds);
                        _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    } catch (Throwable th4) {
                        th = th4;
                        str = srcAddr;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(srcAddr);
                        _data.writeString(dstAddr);
                        if (this.mRemote.transact(79, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().startNattKeepaliveWithFd(network, fd, resourceId, intervalSeconds, cb, srcAddr, dstAddr);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    FileDescriptor fileDescriptor = fd;
                    i = resourceId;
                    i2 = intervalSeconds;
                    str = srcAddr;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void startTcpKeepalive(Network network, FileDescriptor fd, int intervalSeconds, ISocketKeepaliveCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeRawFileDescriptor(fd);
                    _data.writeInt(intervalSeconds);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    if (this.mRemote.transact(80, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startTcpKeepalive(network, fd, intervalSeconds, cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopKeepalive(Network network, int slot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(slot);
                    if (this.mRemote.transact(81, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopKeepalive(network, slot);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCaptivePortalServerUrl() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 82;
                    if (!this.mRemote.transact(82, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCaptivePortalServerUrl();
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

            public byte[] getNetworkWatchlistConfigHash() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    byte[] bArr = 83;
                    if (!this.mRemote.transact(83, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getNetworkWatchlistConfigHash();
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

            public int getConnectionOwnerUid(ConnectionInfo connectionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (connectionInfo != null) {
                        _data.writeInt(1);
                        connectionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(84, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getConnectionOwnerUid(connectionInfo);
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

            public boolean isCallerCurrentAlwaysOnVpnApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(85, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isCallerCurrentAlwaysOnVpnApp();
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

            public boolean isCallerCurrentAlwaysOnVpnLockdownApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(86, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isCallerCurrentAlwaysOnVpnLockdownApp();
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

            public void getLatestTetheringEntitlementResult(int type, ResultReceiver receiver, boolean showEntitlementUi, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    int i = 1;
                    if (receiver != null) {
                        _data.writeInt(1);
                        receiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!showEntitlementUi) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeString(callerPkg);
                    if (this.mRemote.transact(87, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getLatestTetheringEntitlementResult(type, receiver, showEntitlementUi, callerPkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerTetheringEventCallback(ITetheringEventCallback callback, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(callerPkg);
                    if (this.mRemote.transact(88, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerTetheringEventCallback(callback, callerPkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterTetheringEventCallback(ITetheringEventCallback callback, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(callerPkg);
                    if (this.mRemote.transact(89, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterTetheringEventCallback(callback, callerPkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder startOrGetTestNetworkService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = 90;
                    if (!this.mRemote.transact(90, _data, _reply, 0)) {
                        iBinder = Stub.getDefaultImpl();
                        if (iBinder != 0) {
                            iBinder = Stub.getDefaultImpl().startOrGetTestNetworkService();
                            return iBinder;
                        }
                    }
                    _reply.readException();
                    iBinder = _reply.readStrongBinder();
                    IBinder _result = iBinder;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IConnectivityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IConnectivityManager)) {
                return new Proxy(obj);
            }
            return (IConnectivityManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getActiveNetwork";
                case 2:
                    return "getActiveNetworkForUid";
                case 3:
                    return "getActiveNetworkInfo";
                case 4:
                    return "getActiveNetworkInfoForUid";
                case 5:
                    return "getNetworkInfo";
                case 6:
                    return "getNetworkInfoForUid";
                case 7:
                    return "getAllNetworkInfo";
                case 8:
                    return "getNetworkForType";
                case 9:
                    return "getAllNetworks";
                case 10:
                    return "getDefaultNetworkCapabilitiesForUser";
                case 11:
                    return "isNetworkSupported";
                case 12:
                    return "getActiveLinkProperties";
                case 13:
                    return "getLinkPropertiesForType";
                case 14:
                    return "getLinkProperties";
                case 15:
                    return "getNetworkCapabilities";
                case 16:
                    return "getAllNetworkState";
                case 17:
                    return "getActiveNetworkQuotaInfo";
                case 18:
                    return "isActiveNetworkMetered";
                case 19:
                    return "requestRouteToHostAddress";
                case 20:
                    return "tether";
                case 21:
                    return "untether";
                case 22:
                    return "getLastTetherError";
                case 23:
                    return "isTetheringSupported";
                case 24:
                    return "startTethering";
                case 25:
                    return "stopTethering";
                case 26:
                    return "getTetherableIfaces";
                case 27:
                    return "getTetheredIfaces";
                case 28:
                    return "getTetheringErroredIfaces";
                case 29:
                    return "getTetheredDhcpRanges";
                case 30:
                    return "getTetherableUsbRegexs";
                case 31:
                    return "getTetherableWifiRegexs";
                case 32:
                    return "getTetherableBluetoothRegexs";
                case 33:
                    return "setUsbTethering";
                case 34:
                    return "reportInetCondition";
                case 35:
                    return "reportNetworkConnectivity";
                case 36:
                    return "getGlobalProxy";
                case 37:
                    return "setGlobalProxy";
                case 38:
                    return "getProxyForNetwork";
                case 39:
                    return "prepareVpn";
                case 40:
                    return "setVpnPackageAuthorization";
                case 41:
                    return "establishVpn";
                case 42:
                    return "getVpnConfig";
                case 43:
                    return "startLegacyVpn";
                case 44:
                    return "getLegacyVpnInfo";
                case 45:
                    return "updateLockdownVpn";
                case 46:
                    return "isAlwaysOnVpnPackageSupported";
                case 47:
                    return "setAlwaysOnVpnPackage";
                case 48:
                    return "getAlwaysOnVpnPackage";
                case 49:
                    return "isVpnLockdownEnabled";
                case 50:
                    return "getVpnLockdownWhitelist";
                case 51:
                    return "checkMobileProvisioning";
                case 52:
                    return "getMobileProvisioningUrl";
                case 53:
                    return "setProvisioningNotificationVisible";
                case 54:
                    return "setAirplaneMode";
                case 55:
                    return "registerNetworkFactory";
                case 56:
                    return "requestBandwidthUpdate";
                case 57:
                    return "unregisterNetworkFactory";
                case 58:
                    return "registerNetworkAgent";
                case 59:
                    return "requestNetwork";
                case 60:
                    return "pendingRequestForNetwork";
                case 61:
                    return "releasePendingNetworkRequest";
                case 62:
                    return "listenForNetwork";
                case 63:
                    return "pendingListenForNetwork";
                case 64:
                    return "releaseNetworkRequest";
                case 65:
                    return "setAcceptUnvalidated";
                case 66:
                    return "setAcceptPartialConnectivity";
                case 67:
                    return "setAvoidUnvalidated";
                case 68:
                    return "startCaptivePortalApp";
                case 69:
                    return "startCaptivePortalAppInternal";
                case 70:
                    return "shouldAvoidBadWifi";
                case 71:
                    return "getMultipathPreference";
                case 72:
                    return "getDefaultRequest";
                case 73:
                    return "getRestoreDefaultNetworkDelay";
                case 74:
                    return "addVpnAddress";
                case 75:
                    return "removeVpnAddress";
                case 76:
                    return "setUnderlyingNetworksForVpn";
                case 77:
                    return "factoryReset";
                case 78:
                    return "startNattKeepalive";
                case 79:
                    return "startNattKeepaliveWithFd";
                case 80:
                    return "startTcpKeepalive";
                case 81:
                    return "stopKeepalive";
                case 82:
                    return "getCaptivePortalServerUrl";
                case 83:
                    return "getNetworkWatchlistConfigHash";
                case 84:
                    return "getConnectionOwnerUid";
                case 85:
                    return "isCallerCurrentAlwaysOnVpnApp";
                case 86:
                    return "isCallerCurrentAlwaysOnVpnLockdownApp";
                case 87:
                    return "getLatestTetheringEntitlementResult";
                case 88:
                    return "registerTetheringEventCallback";
                case 89:
                    return "unregisterTetheringEventCallback";
                case 90:
                    return "startOrGetTestNetworkService";
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
                boolean z = false;
                Network _result;
                boolean _result2;
                LinkProperties _result3;
                boolean _result4;
                boolean _result5;
                int _result6;
                int _result7;
                int _arg0;
                ResultReceiver _arg1;
                String[] _result8;
                ProxyInfo _result9;
                String _arg02;
                String _arg12;
                Messenger _arg03;
                Messenger _arg04;
                NetworkRequest _result10;
                NetworkCapabilities _arg05;
                PendingIntent _arg13;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _result = getActiveNetwork();
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        Network _result11 = getActiveNetworkForUid(data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result11 != null) {
                            parcel2.writeInt(1);
                            _result11.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        NetworkInfo _result12 = getActiveNetworkInfo();
                        reply.writeNoException();
                        if (_result12 != null) {
                            parcel2.writeInt(1);
                            _result12.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        NetworkInfo _result13 = getActiveNetworkInfoForUid(data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result13 != null) {
                            parcel2.writeInt(1);
                            _result13.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        NetworkInfo _result14 = getNetworkInfo(data.readInt());
                        reply.writeNoException();
                        if (_result14 != null) {
                            parcel2.writeInt(1);
                            _result14.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        NetworkInfo _result15 = getNetworkInfoForUid(_result, data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result15 != null) {
                            parcel2.writeInt(1);
                            _result15.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        NetworkInfo[] _result16 = getAllNetworkInfo();
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result16, 1);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        Network _result17 = getNetworkForType(data.readInt());
                        reply.writeNoException();
                        if (_result17 != null) {
                            parcel2.writeInt(1);
                            _result17.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        Network[] _result18 = getAllNetworks();
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result18, 1);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        NetworkCapabilities[] _result19 = getDefaultNetworkCapabilitiesForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result19, 1);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result2 = isNetworkSupported(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        LinkProperties _result20 = getActiveLinkProperties();
                        reply.writeNoException();
                        if (_result20 != null) {
                            parcel2.writeInt(1);
                            _result20.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _result3 = getLinkPropertiesForType(data.readInt());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        _result3 = getLinkProperties(_result);
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        NetworkCapabilities _result21 = getNetworkCapabilities(_result);
                        reply.writeNoException();
                        if (_result21 != null) {
                            parcel2.writeInt(1);
                            _result21.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        NetworkState[] _result22 = getAllNetworkState();
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result22, 1);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        NetworkQuotaInfo _result23 = getActiveNetworkQuotaInfo();
                        reply.writeNoException();
                        if (_result23 != null) {
                            parcel2.writeInt(1);
                            _result23.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _result4 = isActiveNetworkMetered();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _result5 = requestRouteToHostAddress(data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _result6 = tether(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _result6 = untether(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _result7 = getLastTetherError(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result2 = isTetheringSupported(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        startTethering(_arg0, _arg1, z, data.readString());
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        stopTethering(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _result8 = getTetherableIfaces();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result8);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        _result8 = getTetheredIfaces();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result8);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        _result8 = getTetheringErroredIfaces();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result8);
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        _result8 = getTetheredDhcpRanges();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result8);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        _result8 = getTetherableUsbRegexs();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result8);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _result8 = getTetherableWifiRegexs();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result8);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        _result8 = getTetherableBluetoothRegexs();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result8);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        _result6 = setUsbTethering(z, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        reportInetCondition(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        reportNetworkConnectivity(_result, z);
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        _result9 = getGlobalProxy();
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result9 = (ProxyInfo) ProxyInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _result9 = null;
                        }
                        setGlobalProxy(_result9);
                        reply.writeNoException();
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        ProxyInfo _result24 = getProxyForNetwork(_result);
                        reply.writeNoException();
                        if (_result24 != null) {
                            parcel2.writeInt(1);
                            _result24.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        boolean _result25 = prepareVpn(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result25);
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _result7 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setVpnPackageAuthorization(_arg02, _result7, z);
                        reply.writeNoException();
                        return true;
                    case 41:
                        VpnConfig _arg06;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (VpnConfig) VpnConfig.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        ParcelFileDescriptor _result26 = establishVpn(_arg06);
                        reply.writeNoException();
                        if (_result26 != null) {
                            parcel2.writeInt(1);
                            _result26.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        VpnConfig _result27 = getVpnConfig(data.readInt());
                        reply.writeNoException();
                        if (_result27 != null) {
                            parcel2.writeInt(1);
                            _result27.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 43:
                        VpnProfile _arg07;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (VpnProfile) VpnProfile.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        startLegacyVpn(_arg07);
                        reply.writeNoException();
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        LegacyVpnInfo _result28 = getLegacyVpnInfo(data.readInt());
                        reply.writeNoException();
                        if (_result28 != null) {
                            parcel2.writeInt(1);
                            _result28.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        _result4 = updateLockdownVpn();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        _result5 = isAlwaysOnVpnPackageSupported(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        boolean _result29 = setAlwaysOnVpnPackage(_arg0, _arg12, z, data.createStringArrayList());
                        reply.writeNoException();
                        parcel2.writeInt(_result29);
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        _arg12 = getAlwaysOnVpnPackage(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        _result2 = isVpnLockdownEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        List<String> _result30 = getVpnLockdownWhitelist(data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringList(_result30);
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        _result7 = checkMobileProvisioning(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getMobileProvisioningUrl();
                        reply.writeNoException();
                        parcel2.writeString(_arg02);
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setProvisioningNotificationVisible(z, data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setAirplaneMode(z);
                        reply.writeNoException();
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Messenger) Messenger.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _result6 = registerNetworkFactory(_arg03, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        _result2 = requestBandwidthUpdate(_result);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Messenger) Messenger.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        unregisterNetworkFactory(_arg03);
                        reply.writeNoException();
                        return true;
                    case 58:
                        NetworkInfo _arg14;
                        LinkProperties _arg2;
                        NetworkCapabilities _arg3;
                        NetworkMisc _arg5;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Messenger) Messenger.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg14 = (NetworkInfo) NetworkInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (LinkProperties) LinkProperties.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        int _arg4 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg5 = (NetworkMisc) NetworkMisc.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        _arg0 = registerNetworkAgent(_arg04, _arg14, _arg2, _arg3, _arg4, _arg5, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 59:
                        NetworkCapabilities _arg08;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg08 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg04 = (Messenger) Messenger.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result10 = requestNetwork(_arg08, _arg04, data.readInt(), data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        if (_result10 != null) {
                            parcel2.writeInt(1);
                            _result10.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        NetworkRequest _result31 = pendingRequestForNetwork(_arg05, _arg13);
                        reply.writeNoException();
                        if (_result31 != null) {
                            parcel2.writeInt(1);
                            _result31.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 61:
                        PendingIntent _arg09;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg09 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg09 = null;
                        }
                        releasePendingNetworkRequest(_arg09);
                        reply.writeNoException();
                        return true;
                    case 62:
                        Messenger _arg15;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg15 = (Messenger) Messenger.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        NetworkRequest _result32 = listenForNetwork(_arg05, _arg15, data.readStrongBinder());
                        reply.writeNoException();
                        if (_result32 != null) {
                            parcel2.writeInt(1);
                            _result32.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        pendingListenForNetwork(_arg05, _arg13);
                        reply.writeNoException();
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result10 = (NetworkRequest) NetworkRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _result10 = null;
                        }
                        releaseNetworkRequest(_result10);
                        reply.writeNoException();
                        return true;
                    case 65:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        _result2 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setAcceptUnvalidated(_result, _result2, z);
                        reply.writeNoException();
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        _result2 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setAcceptPartialConnectivity(_result, _result2, z);
                        reply.writeNoException();
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        setAvoidUnvalidated(_result);
                        reply.writeNoException();
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        startCaptivePortalApp(_result);
                        reply.writeNoException();
                        return true;
                    case 69:
                        Bundle _arg16;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        if (data.readInt() != 0) {
                            _arg16 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        startCaptivePortalAppInternal(_result, _arg16);
                        reply.writeNoException();
                        return true;
                    case 70:
                        parcel.enforceInterface(descriptor);
                        _result4 = shouldAvoidBadWifi();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 71:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        _result7 = getMultipathPreference(_result);
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 72:
                        parcel.enforceInterface(descriptor);
                        _result10 = getDefaultRequest();
                        reply.writeNoException();
                        if (_result10 != null) {
                            parcel2.writeInt(1);
                            _result10.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 73:
                        parcel.enforceInterface(descriptor);
                        _result7 = getRestoreDefaultNetworkDelay(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 74:
                        parcel.enforceInterface(descriptor);
                        _result5 = addVpnAddress(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 75:
                        parcel.enforceInterface(descriptor);
                        _result5 = removeVpnAddress(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 76:
                        parcel.enforceInterface(descriptor);
                        _result2 = setUnderlyingNetworksForVpn((Network[]) parcel.createTypedArray(Network.CREATOR));
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 77:
                        parcel.enforceInterface(descriptor);
                        factoryReset();
                        reply.writeNoException();
                        return true;
                    case 78:
                        Network _arg010;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg010 = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg010 = null;
                        }
                        startNattKeepalive(_arg010, data.readInt(), android.net.ISocketKeepaliveCallback.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 79:
                        Network _arg011;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg011 = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg011 = null;
                        }
                        startNattKeepaliveWithFd(_arg011, data.readRawFileDescriptor(), data.readInt(), data.readInt(), android.net.ISocketKeepaliveCallback.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 80:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        startTcpKeepalive(_result, data.readRawFileDescriptor(), data.readInt(), android.net.ISocketKeepaliveCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 81:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (Network) Network.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        stopKeepalive(_result, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 82:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getCaptivePortalServerUrl();
                        reply.writeNoException();
                        parcel2.writeString(_arg02);
                        return true;
                    case 83:
                        parcel.enforceInterface(descriptor);
                        byte[] _result33 = getNetworkWatchlistConfigHash();
                        reply.writeNoException();
                        parcel2.writeByteArray(_result33);
                        return true;
                    case 84:
                        ConnectionInfo _arg012;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg012 = (ConnectionInfo) ConnectionInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg012 = null;
                        }
                        _result7 = getConnectionOwnerUid(_arg012);
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 85:
                        parcel.enforceInterface(descriptor);
                        _result4 = isCallerCurrentAlwaysOnVpnApp();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 86:
                        parcel.enforceInterface(descriptor);
                        _result4 = isCallerCurrentAlwaysOnVpnLockdownApp();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 87:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        getLatestTetheringEntitlementResult(_arg0, _arg1, z, data.readString());
                        reply.writeNoException();
                        return true;
                    case 88:
                        parcel.enforceInterface(descriptor);
                        registerTetheringEventCallback(android.net.ITetheringEventCallback.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        return true;
                    case 89:
                        parcel.enforceInterface(descriptor);
                        unregisterTetheringEventCallback(android.net.ITetheringEventCallback.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        return true;
                    case 90:
                        parcel.enforceInterface(descriptor);
                        IBinder _result34 = startOrGetTestNetworkService();
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result34);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IConnectivityManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IConnectivityManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean addVpnAddress(String str, int i) throws RemoteException;

    int checkMobileProvisioning(int i) throws RemoteException;

    ParcelFileDescriptor establishVpn(VpnConfig vpnConfig) throws RemoteException;

    void factoryReset() throws RemoteException;

    @UnsupportedAppUsage
    LinkProperties getActiveLinkProperties() throws RemoteException;

    Network getActiveNetwork() throws RemoteException;

    Network getActiveNetworkForUid(int i, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    NetworkInfo getActiveNetworkInfo() throws RemoteException;

    NetworkInfo getActiveNetworkInfoForUid(int i, boolean z) throws RemoteException;

    NetworkQuotaInfo getActiveNetworkQuotaInfo() throws RemoteException;

    @UnsupportedAppUsage
    NetworkInfo[] getAllNetworkInfo() throws RemoteException;

    @UnsupportedAppUsage
    NetworkState[] getAllNetworkState() throws RemoteException;

    Network[] getAllNetworks() throws RemoteException;

    String getAlwaysOnVpnPackage(int i) throws RemoteException;

    String getCaptivePortalServerUrl() throws RemoteException;

    int getConnectionOwnerUid(ConnectionInfo connectionInfo) throws RemoteException;

    NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int i) throws RemoteException;

    NetworkRequest getDefaultRequest() throws RemoteException;

    ProxyInfo getGlobalProxy() throws RemoteException;

    @UnsupportedAppUsage
    int getLastTetherError(String str) throws RemoteException;

    void getLatestTetheringEntitlementResult(int i, ResultReceiver resultReceiver, boolean z, String str) throws RemoteException;

    LegacyVpnInfo getLegacyVpnInfo(int i) throws RemoteException;

    LinkProperties getLinkProperties(Network network) throws RemoteException;

    LinkProperties getLinkPropertiesForType(int i) throws RemoteException;

    String getMobileProvisioningUrl() throws RemoteException;

    int getMultipathPreference(Network network) throws RemoteException;

    NetworkCapabilities getNetworkCapabilities(Network network) throws RemoteException;

    Network getNetworkForType(int i) throws RemoteException;

    NetworkInfo getNetworkInfo(int i) throws RemoteException;

    NetworkInfo getNetworkInfoForUid(Network network, int i, boolean z) throws RemoteException;

    byte[] getNetworkWatchlistConfigHash() throws RemoteException;

    ProxyInfo getProxyForNetwork(Network network) throws RemoteException;

    int getRestoreDefaultNetworkDelay(int i) throws RemoteException;

    String[] getTetherableBluetoothRegexs() throws RemoteException;

    @UnsupportedAppUsage
    String[] getTetherableIfaces() throws RemoteException;

    @UnsupportedAppUsage
    String[] getTetherableUsbRegexs() throws RemoteException;

    @UnsupportedAppUsage
    String[] getTetherableWifiRegexs() throws RemoteException;

    String[] getTetheredDhcpRanges() throws RemoteException;

    @UnsupportedAppUsage
    String[] getTetheredIfaces() throws RemoteException;

    @UnsupportedAppUsage
    String[] getTetheringErroredIfaces() throws RemoteException;

    VpnConfig getVpnConfig(int i) throws RemoteException;

    List<String> getVpnLockdownWhitelist(int i) throws RemoteException;

    boolean isActiveNetworkMetered() throws RemoteException;

    boolean isAlwaysOnVpnPackageSupported(int i, String str) throws RemoteException;

    boolean isCallerCurrentAlwaysOnVpnApp() throws RemoteException;

    boolean isCallerCurrentAlwaysOnVpnLockdownApp() throws RemoteException;

    boolean isNetworkSupported(int i) throws RemoteException;

    boolean isTetheringSupported(String str) throws RemoteException;

    boolean isVpnLockdownEnabled(int i) throws RemoteException;

    NetworkRequest listenForNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, IBinder iBinder) throws RemoteException;

    void pendingListenForNetwork(NetworkCapabilities networkCapabilities, PendingIntent pendingIntent) throws RemoteException;

    NetworkRequest pendingRequestForNetwork(NetworkCapabilities networkCapabilities, PendingIntent pendingIntent) throws RemoteException;

    boolean prepareVpn(String str, String str2, int i) throws RemoteException;

    int registerNetworkAgent(Messenger messenger, NetworkInfo networkInfo, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int i, NetworkMisc networkMisc, int i2) throws RemoteException;

    int registerNetworkFactory(Messenger messenger, String str) throws RemoteException;

    void registerTetheringEventCallback(ITetheringEventCallback iTetheringEventCallback, String str) throws RemoteException;

    void releaseNetworkRequest(NetworkRequest networkRequest) throws RemoteException;

    void releasePendingNetworkRequest(PendingIntent pendingIntent) throws RemoteException;

    boolean removeVpnAddress(String str, int i) throws RemoteException;

    void reportInetCondition(int i, int i2) throws RemoteException;

    void reportNetworkConnectivity(Network network, boolean z) throws RemoteException;

    boolean requestBandwidthUpdate(Network network) throws RemoteException;

    NetworkRequest requestNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, int i, IBinder iBinder, int i2) throws RemoteException;

    boolean requestRouteToHostAddress(int i, byte[] bArr) throws RemoteException;

    void setAcceptPartialConnectivity(Network network, boolean z, boolean z2) throws RemoteException;

    void setAcceptUnvalidated(Network network, boolean z, boolean z2) throws RemoteException;

    void setAirplaneMode(boolean z) throws RemoteException;

    boolean setAlwaysOnVpnPackage(int i, String str, boolean z, List<String> list) throws RemoteException;

    void setAvoidUnvalidated(Network network) throws RemoteException;

    void setGlobalProxy(ProxyInfo proxyInfo) throws RemoteException;

    void setProvisioningNotificationVisible(boolean z, int i, String str) throws RemoteException;

    boolean setUnderlyingNetworksForVpn(Network[] networkArr) throws RemoteException;

    int setUsbTethering(boolean z, String str) throws RemoteException;

    void setVpnPackageAuthorization(String str, int i, boolean z) throws RemoteException;

    boolean shouldAvoidBadWifi() throws RemoteException;

    void startCaptivePortalApp(Network network) throws RemoteException;

    void startCaptivePortalAppInternal(Network network, Bundle bundle) throws RemoteException;

    @UnsupportedAppUsage
    void startLegacyVpn(VpnProfile vpnProfile) throws RemoteException;

    void startNattKeepalive(Network network, int i, ISocketKeepaliveCallback iSocketKeepaliveCallback, String str, int i2, String str2) throws RemoteException;

    void startNattKeepaliveWithFd(Network network, FileDescriptor fileDescriptor, int i, int i2, ISocketKeepaliveCallback iSocketKeepaliveCallback, String str, String str2) throws RemoteException;

    IBinder startOrGetTestNetworkService() throws RemoteException;

    void startTcpKeepalive(Network network, FileDescriptor fileDescriptor, int i, ISocketKeepaliveCallback iSocketKeepaliveCallback) throws RemoteException;

    void startTethering(int i, ResultReceiver resultReceiver, boolean z, String str) throws RemoteException;

    void stopKeepalive(Network network, int i) throws RemoteException;

    void stopTethering(int i, String str) throws RemoteException;

    int tether(String str, String str2) throws RemoteException;

    void unregisterNetworkFactory(Messenger messenger) throws RemoteException;

    void unregisterTetheringEventCallback(ITetheringEventCallback iTetheringEventCallback, String str) throws RemoteException;

    int untether(String str, String str2) throws RemoteException;

    boolean updateLockdownVpn() throws RemoteException;
}
