package android.os;

import android.annotation.UnsupportedAppUsage;
import android.net.INetworkManagementEventObserver;
import android.net.ITetheringStatsProvider;
import android.net.InterfaceConfiguration;
import android.net.Network;
import android.net.NetworkStats;
import android.net.RouteInfo;
import android.net.UidRange;
import java.util.List;

public interface INetworkManagementService extends IInterface {

    public static class Default implements INetworkManagementService {
        public void registerObserver(INetworkManagementEventObserver obs) throws RemoteException {
        }

        public void unregisterObserver(INetworkManagementEventObserver obs) throws RemoteException {
        }

        public String[] listInterfaces() throws RemoteException {
            return null;
        }

        public InterfaceConfiguration getInterfaceConfig(String iface) throws RemoteException {
            return null;
        }

        public void setInterfaceConfig(String iface, InterfaceConfiguration cfg) throws RemoteException {
        }

        public void clearInterfaceAddresses(String iface) throws RemoteException {
        }

        public void setInterfaceDown(String iface) throws RemoteException {
        }

        public void setInterfaceUp(String iface) throws RemoteException {
        }

        public void setInterfaceIpv6PrivacyExtensions(String iface, boolean enable) throws RemoteException {
        }

        public void disableIpv6(String iface) throws RemoteException {
        }

        public void enableIpv6(String iface) throws RemoteException {
        }

        public void setIPv6AddrGenMode(String iface, int mode) throws RemoteException {
        }

        public void addRoute(int netId, RouteInfo route) throws RemoteException {
        }

        public void removeRoute(int netId, RouteInfo route) throws RemoteException {
        }

        public void setMtu(String iface, int mtu) throws RemoteException {
        }

        public void shutdown() throws RemoteException {
        }

        public boolean getIpForwardingEnabled() throws RemoteException {
            return false;
        }

        public void setIpForwardingEnabled(boolean enabled) throws RemoteException {
        }

        public void startTethering(String[] dhcpRanges) throws RemoteException {
        }

        public void stopTethering() throws RemoteException {
        }

        public boolean isTetheringStarted() throws RemoteException {
            return false;
        }

        public void tetherInterface(String iface) throws RemoteException {
        }

        public void untetherInterface(String iface) throws RemoteException {
        }

        public String[] listTetheredInterfaces() throws RemoteException {
            return null;
        }

        public void setDnsForwarders(Network network, String[] dns) throws RemoteException {
        }

        public String[] getDnsForwarders() throws RemoteException {
            return null;
        }

        public void startInterfaceForwarding(String fromIface, String toIface) throws RemoteException {
        }

        public void stopInterfaceForwarding(String fromIface, String toIface) throws RemoteException {
        }

        public void enableNat(String internalInterface, String externalInterface) throws RemoteException {
        }

        public void disableNat(String internalInterface, String externalInterface) throws RemoteException {
        }

        public void registerTetheringStatsProvider(ITetheringStatsProvider provider, String name) throws RemoteException {
        }

        public void unregisterTetheringStatsProvider(ITetheringStatsProvider provider) throws RemoteException {
        }

        public void tetherLimitReached(ITetheringStatsProvider provider) throws RemoteException {
        }

        public NetworkStats getNetworkStatsSummaryDev() throws RemoteException {
            return null;
        }

        public NetworkStats getNetworkStatsSummaryXt() throws RemoteException {
            return null;
        }

        public NetworkStats getNetworkStatsDetail() throws RemoteException {
            return null;
        }

        public NetworkStats getNetworkStatsUidDetail(int uid, String[] ifaces) throws RemoteException {
            return null;
        }

        public NetworkStats getNetworkStatsTethering(int how) throws RemoteException {
            return null;
        }

        public void setInterfaceQuota(String iface, long quotaBytes) throws RemoteException {
        }

        public void removeInterfaceQuota(String iface) throws RemoteException {
        }

        public void setInterfaceAlert(String iface, long alertBytes) throws RemoteException {
        }

        public void removeInterfaceAlert(String iface) throws RemoteException {
        }

        public void setGlobalAlert(long alertBytes) throws RemoteException {
        }

        public void setUidMeteredNetworkBlacklist(int uid, boolean enable) throws RemoteException {
        }

        public void setUidMeteredNetworkWhitelist(int uid, boolean enable) throws RemoteException {
        }

        public boolean setDataSaverModeEnabled(boolean enable) throws RemoteException {
            return false;
        }

        public void setUidCleartextNetworkPolicy(int uid, int policy) throws RemoteException {
        }

        public boolean isBandwidthControlEnabled() throws RemoteException {
            return false;
        }

        public void addIdleTimer(String iface, int timeout, int type) throws RemoteException {
        }

        public void removeIdleTimer(String iface) throws RemoteException {
        }

        public void setFirewallEnabled(boolean enabled) throws RemoteException {
        }

        public boolean isFirewallEnabled() throws RemoteException {
            return false;
        }

        public void setFirewallInterfaceRule(String iface, boolean allow) throws RemoteException {
        }

        public void setFirewallUidRule(int chain, int uid, int rule) throws RemoteException {
        }

        public void setFirewallUidRules(int chain, int[] uids, int[] rules) throws RemoteException {
        }

        public void setFirewallChainEnabled(int chain, boolean enable) throws RemoteException {
        }

        public void addVpnUidRanges(int netId, UidRange[] ranges) throws RemoteException {
        }

        public void removeVpnUidRanges(int netId, UidRange[] ranges) throws RemoteException {
        }

        public void registerNetworkActivityListener(INetworkActivityListener listener) throws RemoteException {
        }

        public void unregisterNetworkActivityListener(INetworkActivityListener listener) throws RemoteException {
        }

        public boolean isNetworkActive() throws RemoteException {
            return false;
        }

        public void addInterfaceToNetwork(String iface, int netId) throws RemoteException {
        }

        public void removeInterfaceFromNetwork(String iface, int netId) throws RemoteException {
        }

        public void addLegacyRouteForNetId(int netId, RouteInfo routeInfo, int uid) throws RemoteException {
        }

        public void setDefaultNetId(int netId) throws RemoteException {
        }

        public void clearDefaultNetId() throws RemoteException {
        }

        public void setNetworkPermission(int netId, int permission) throws RemoteException {
        }

        public void allowProtect(int uid) throws RemoteException {
        }

        public void denyProtect(int uid) throws RemoteException {
        }

        public void addInterfaceToLocalNetwork(String iface, List<RouteInfo> list) throws RemoteException {
        }

        public void removeInterfaceFromLocalNetwork(String iface) throws RemoteException {
        }

        public int removeRoutesFromLocalNetwork(List<RouteInfo> list) throws RemoteException {
            return 0;
        }

        public void setAllowOnlyVpnForUids(boolean enable, UidRange[] uidRanges) throws RemoteException {
        }

        public boolean isNetworkRestricted(int uid) throws RemoteException {
            return false;
        }

        public IBinder getMiuiNetworkManager() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INetworkManagementService {
        private static final String DESCRIPTOR = "android.os.INetworkManagementService";
        static final int TRANSACTION_addIdleTimer = 49;
        static final int TRANSACTION_addInterfaceToLocalNetwork = 70;
        static final int TRANSACTION_addInterfaceToNetwork = 62;
        static final int TRANSACTION_addLegacyRouteForNetId = 64;
        static final int TRANSACTION_addRoute = 13;
        static final int TRANSACTION_addVpnUidRanges = 57;
        static final int TRANSACTION_allowProtect = 68;
        static final int TRANSACTION_clearDefaultNetId = 66;
        static final int TRANSACTION_clearInterfaceAddresses = 6;
        static final int TRANSACTION_denyProtect = 69;
        static final int TRANSACTION_disableIpv6 = 10;
        static final int TRANSACTION_disableNat = 30;
        static final int TRANSACTION_enableIpv6 = 11;
        static final int TRANSACTION_enableNat = 29;
        static final int TRANSACTION_getDnsForwarders = 26;
        static final int TRANSACTION_getInterfaceConfig = 4;
        static final int TRANSACTION_getIpForwardingEnabled = 17;
        static final int TRANSACTION_getMiuiNetworkManager = 75;
        static final int TRANSACTION_getNetworkStatsDetail = 36;
        static final int TRANSACTION_getNetworkStatsSummaryDev = 34;
        static final int TRANSACTION_getNetworkStatsSummaryXt = 35;
        static final int TRANSACTION_getNetworkStatsTethering = 38;
        static final int TRANSACTION_getNetworkStatsUidDetail = 37;
        static final int TRANSACTION_isBandwidthControlEnabled = 48;
        static final int TRANSACTION_isFirewallEnabled = 52;
        static final int TRANSACTION_isNetworkActive = 61;
        static final int TRANSACTION_isNetworkRestricted = 74;
        static final int TRANSACTION_isTetheringStarted = 21;
        static final int TRANSACTION_listInterfaces = 3;
        static final int TRANSACTION_listTetheredInterfaces = 24;
        static final int TRANSACTION_registerNetworkActivityListener = 59;
        static final int TRANSACTION_registerObserver = 1;
        static final int TRANSACTION_registerTetheringStatsProvider = 31;
        static final int TRANSACTION_removeIdleTimer = 50;
        static final int TRANSACTION_removeInterfaceAlert = 42;
        static final int TRANSACTION_removeInterfaceFromLocalNetwork = 71;
        static final int TRANSACTION_removeInterfaceFromNetwork = 63;
        static final int TRANSACTION_removeInterfaceQuota = 40;
        static final int TRANSACTION_removeRoute = 14;
        static final int TRANSACTION_removeRoutesFromLocalNetwork = 72;
        static final int TRANSACTION_removeVpnUidRanges = 58;
        static final int TRANSACTION_setAllowOnlyVpnForUids = 73;
        static final int TRANSACTION_setDataSaverModeEnabled = 46;
        static final int TRANSACTION_setDefaultNetId = 65;
        static final int TRANSACTION_setDnsForwarders = 25;
        static final int TRANSACTION_setFirewallChainEnabled = 56;
        static final int TRANSACTION_setFirewallEnabled = 51;
        static final int TRANSACTION_setFirewallInterfaceRule = 53;
        static final int TRANSACTION_setFirewallUidRule = 54;
        static final int TRANSACTION_setFirewallUidRules = 55;
        static final int TRANSACTION_setGlobalAlert = 43;
        static final int TRANSACTION_setIPv6AddrGenMode = 12;
        static final int TRANSACTION_setInterfaceAlert = 41;
        static final int TRANSACTION_setInterfaceConfig = 5;
        static final int TRANSACTION_setInterfaceDown = 7;
        static final int TRANSACTION_setInterfaceIpv6PrivacyExtensions = 9;
        static final int TRANSACTION_setInterfaceQuota = 39;
        static final int TRANSACTION_setInterfaceUp = 8;
        static final int TRANSACTION_setIpForwardingEnabled = 18;
        static final int TRANSACTION_setMtu = 15;
        static final int TRANSACTION_setNetworkPermission = 67;
        static final int TRANSACTION_setUidCleartextNetworkPolicy = 47;
        static final int TRANSACTION_setUidMeteredNetworkBlacklist = 44;
        static final int TRANSACTION_setUidMeteredNetworkWhitelist = 45;
        static final int TRANSACTION_shutdown = 16;
        static final int TRANSACTION_startInterfaceForwarding = 27;
        static final int TRANSACTION_startTethering = 19;
        static final int TRANSACTION_stopInterfaceForwarding = 28;
        static final int TRANSACTION_stopTethering = 20;
        static final int TRANSACTION_tetherInterface = 22;
        static final int TRANSACTION_tetherLimitReached = 33;
        static final int TRANSACTION_unregisterNetworkActivityListener = 60;
        static final int TRANSACTION_unregisterObserver = 2;
        static final int TRANSACTION_unregisterTetheringStatsProvider = 32;
        static final int TRANSACTION_untetherInterface = 23;

        private static class Proxy implements INetworkManagementService {
            public static INetworkManagementService sDefaultImpl;
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

            public void registerObserver(INetworkManagementEventObserver obs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(obs != null ? obs.asBinder() : null);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerObserver(obs);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterObserver(INetworkManagementEventObserver obs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(obs != null ? obs.asBinder() : null);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterObserver(obs);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] listInterfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().listInterfaces();
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

            public InterfaceConfiguration getInterfaceConfig(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    InterfaceConfiguration interfaceConfiguration = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        interfaceConfiguration = Stub.getDefaultImpl();
                        if (interfaceConfiguration != 0) {
                            interfaceConfiguration = Stub.getDefaultImpl().getInterfaceConfig(iface);
                            return interfaceConfiguration;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        interfaceConfiguration = (InterfaceConfiguration) InterfaceConfiguration.CREATOR.createFromParcel(_reply);
                    } else {
                        interfaceConfiguration = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return interfaceConfiguration;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInterfaceConfig(String iface, InterfaceConfiguration cfg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (cfg != null) {
                        _data.writeInt(1);
                        cfg.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInterfaceConfig(iface, cfg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearInterfaceAddresses(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearInterfaceAddresses(iface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInterfaceDown(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInterfaceDown(iface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInterfaceUp(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInterfaceUp(iface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInterfaceIpv6PrivacyExtensions(String iface, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInterfaceIpv6PrivacyExtensions(iface, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableIpv6(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableIpv6(iface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableIpv6(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableIpv6(iface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setIPv6AddrGenMode(String iface, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIPv6AddrGenMode(iface, mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addRoute(int netId, RouteInfo route) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    if (route != null) {
                        _data.writeInt(1);
                        route.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addRoute(netId, route);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeRoute(int netId, RouteInfo route) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    if (route != null) {
                        _data.writeInt(1);
                        route.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeRoute(netId, route);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMtu(String iface, int mtu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(mtu);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMtu(iface, mtu);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void shutdown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().shutdown();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getIpForwardingEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getIpForwardingEnabled();
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

            public void setIpForwardingEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIpForwardingEnabled(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startTethering(String[] dhcpRanges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(dhcpRanges);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startTethering(dhcpRanges);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopTethering() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopTethering();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isTetheringStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isTetheringStarted();
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

            public void tetherInterface(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().tetherInterface(iface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void untetherInterface(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().untetherInterface(iface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] listTetheredInterfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().listTetheredInterfaces();
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

            public void setDnsForwarders(Network network, String[] dns) throws RemoteException {
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
                    _data.writeStringArray(dns);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDnsForwarders(network, dns);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getDnsForwarders() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getDnsForwarders();
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

            public void startInterfaceForwarding(String fromIface, String toIface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fromIface);
                    _data.writeString(toIface);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startInterfaceForwarding(fromIface, toIface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopInterfaceForwarding(String fromIface, String toIface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fromIface);
                    _data.writeString(toIface);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopInterfaceForwarding(fromIface, toIface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableNat(String internalInterface, String externalInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(internalInterface);
                    _data.writeString(externalInterface);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableNat(internalInterface, externalInterface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableNat(String internalInterface, String externalInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(internalInterface);
                    _data.writeString(externalInterface);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableNat(internalInterface, externalInterface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerTetheringStatsProvider(ITetheringStatsProvider provider, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(provider != null ? provider.asBinder() : null);
                    _data.writeString(name);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerTetheringStatsProvider(provider, name);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterTetheringStatsProvider(ITetheringStatsProvider provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(provider != null ? provider.asBinder() : null);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterTetheringStatsProvider(provider);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void tetherLimitReached(ITetheringStatsProvider provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(provider != null ? provider.asBinder() : null);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().tetherLimitReached(provider);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkStats getNetworkStatsSummaryDev() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkStats networkStats = 34;
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        networkStats = Stub.getDefaultImpl();
                        if (networkStats != 0) {
                            networkStats = Stub.getDefaultImpl().getNetworkStatsSummaryDev();
                            return networkStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkStats = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
                    } else {
                        networkStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkStats getNetworkStatsSummaryXt() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkStats networkStats = 35;
                    if (!this.mRemote.transact(35, _data, _reply, 0)) {
                        networkStats = Stub.getDefaultImpl();
                        if (networkStats != 0) {
                            networkStats = Stub.getDefaultImpl().getNetworkStatsSummaryXt();
                            return networkStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkStats = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
                    } else {
                        networkStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkStats getNetworkStatsDetail() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkStats networkStats = 36;
                    if (!this.mRemote.transact(36, _data, _reply, 0)) {
                        networkStats = Stub.getDefaultImpl();
                        if (networkStats != 0) {
                            networkStats = Stub.getDefaultImpl().getNetworkStatsDetail();
                            return networkStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkStats = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
                    } else {
                        networkStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkStats getNetworkStatsUidDetail(int uid, String[] ifaces) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeStringArray(ifaces);
                    NetworkStats networkStats = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        networkStats = Stub.getDefaultImpl();
                        if (networkStats != 0) {
                            networkStats = Stub.getDefaultImpl().getNetworkStatsUidDetail(uid, ifaces);
                            return networkStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkStats = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
                    } else {
                        networkStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkStats getNetworkStatsTethering(int how) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(how);
                    NetworkStats networkStats = 38;
                    if (!this.mRemote.transact(38, _data, _reply, 0)) {
                        networkStats = Stub.getDefaultImpl();
                        if (networkStats != 0) {
                            networkStats = Stub.getDefaultImpl().getNetworkStatsTethering(how);
                            return networkStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkStats = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
                    } else {
                        networkStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInterfaceQuota(String iface, long quotaBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeLong(quotaBytes);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInterfaceQuota(iface, quotaBytes);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeInterfaceQuota(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeInterfaceQuota(iface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInterfaceAlert(String iface, long alertBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeLong(alertBytes);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInterfaceAlert(iface, alertBytes);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeInterfaceAlert(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeInterfaceAlert(iface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGlobalAlert(long alertBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(alertBytes);
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setGlobalAlert(alertBytes);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUidMeteredNetworkBlacklist(int uid, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUidMeteredNetworkBlacklist(uid, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUidMeteredNetworkWhitelist(int uid, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUidMeteredNetworkWhitelist(uid, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setDataSaverModeEnabled(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setDataSaverModeEnabled(enable);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUidCleartextNetworkPolicy(int uid, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(policy);
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUidCleartextNetworkPolicy(uid, policy);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isBandwidthControlEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(48, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isBandwidthControlEnabled();
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

            public void addIdleTimer(String iface, int timeout, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(timeout);
                    _data.writeInt(type);
                    if (this.mRemote.transact(49, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addIdleTimer(iface, timeout, type);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeIdleTimer(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(50, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeIdleTimer(iface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFirewallEnabled(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isFirewallEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(52, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isFirewallEnabled();
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

            public void setFirewallInterfaceRule(String iface, boolean allow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(allow ? 1 : 0);
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFirewallInterfaceRule(iface, allow);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallUidRule(int chain, int uid, int rule) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(chain);
                    _data.writeInt(uid);
                    _data.writeInt(rule);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFirewallUidRule(chain, uid, rule);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallUidRules(int chain, int[] uids, int[] rules) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(chain);
                    _data.writeIntArray(uids);
                    _data.writeIntArray(rules);
                    if (this.mRemote.transact(55, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFirewallUidRules(chain, uids, rules);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallChainEnabled(int chain, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(chain);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFirewallChainEnabled(chain, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addVpnUidRanges(int netId, UidRange[] ranges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeTypedArray(ranges, 0);
                    if (this.mRemote.transact(57, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addVpnUidRanges(netId, ranges);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeVpnUidRanges(int netId, UidRange[] ranges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeTypedArray(ranges, 0);
                    if (this.mRemote.transact(58, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeVpnUidRanges(netId, ranges);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerNetworkActivityListener(INetworkActivityListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerNetworkActivityListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterNetworkActivityListener(INetworkActivityListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterNetworkActivityListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isNetworkActive() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(61, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isNetworkActive();
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

            public void addInterfaceToNetwork(String iface, int netId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(netId);
                    if (this.mRemote.transact(62, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addInterfaceToNetwork(iface, netId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeInterfaceFromNetwork(String iface, int netId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(netId);
                    if (this.mRemote.transact(63, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeInterfaceFromNetwork(iface, netId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addLegacyRouteForNetId(int netId, RouteInfo routeInfo, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    if (routeInfo != null) {
                        _data.writeInt(1);
                        routeInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    if (this.mRemote.transact(64, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addLegacyRouteForNetId(netId, routeInfo, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDefaultNetId(int netId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    if (this.mRemote.transact(65, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDefaultNetId(netId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearDefaultNetId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(66, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearDefaultNetId();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNetworkPermission(int netId, int permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeInt(permission);
                    if (this.mRemote.transact(67, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNetworkPermission(netId, permission);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void allowProtect(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(68, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().allowProtect(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void denyProtect(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(69, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().denyProtect(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addInterfaceToLocalNetwork(String iface, List<RouteInfo> routes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeTypedList(routes);
                    if (this.mRemote.transact(70, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addInterfaceToLocalNetwork(iface, routes);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeInterfaceFromLocalNetwork(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(71, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeInterfaceFromLocalNetwork(iface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int removeRoutesFromLocalNetwork(List<RouteInfo> routes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(routes);
                    int i = 72;
                    if (!this.mRemote.transact(72, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().removeRoutesFromLocalNetwork(routes);
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

            public void setAllowOnlyVpnForUids(boolean enable, UidRange[] uidRanges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeTypedArray(uidRanges, 0);
                    if (this.mRemote.transact(73, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAllowOnlyVpnForUids(enable, uidRanges);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isNetworkRestricted(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(74, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isNetworkRestricted(uid);
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

            public IBinder getMiuiNetworkManager() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = 75;
                    if (!this.mRemote.transact(75, _data, _reply, 0)) {
                        iBinder = Stub.getDefaultImpl();
                        if (iBinder != 0) {
                            iBinder = Stub.getDefaultImpl().getMiuiNetworkManager();
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

        public static INetworkManagementService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INetworkManagementService)) {
                return new Proxy(obj);
            }
            return (INetworkManagementService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "registerObserver";
                case 2:
                    return "unregisterObserver";
                case 3:
                    return "listInterfaces";
                case 4:
                    return "getInterfaceConfig";
                case 5:
                    return "setInterfaceConfig";
                case 6:
                    return "clearInterfaceAddresses";
                case 7:
                    return "setInterfaceDown";
                case 8:
                    return "setInterfaceUp";
                case 9:
                    return "setInterfaceIpv6PrivacyExtensions";
                case 10:
                    return "disableIpv6";
                case 11:
                    return "enableIpv6";
                case 12:
                    return "setIPv6AddrGenMode";
                case 13:
                    return "addRoute";
                case 14:
                    return "removeRoute";
                case 15:
                    return "setMtu";
                case 16:
                    return "shutdown";
                case 17:
                    return "getIpForwardingEnabled";
                case 18:
                    return "setIpForwardingEnabled";
                case 19:
                    return "startTethering";
                case 20:
                    return "stopTethering";
                case 21:
                    return "isTetheringStarted";
                case 22:
                    return "tetherInterface";
                case 23:
                    return "untetherInterface";
                case 24:
                    return "listTetheredInterfaces";
                case 25:
                    return "setDnsForwarders";
                case 26:
                    return "getDnsForwarders";
                case 27:
                    return "startInterfaceForwarding";
                case 28:
                    return "stopInterfaceForwarding";
                case 29:
                    return "enableNat";
                case 30:
                    return "disableNat";
                case 31:
                    return "registerTetheringStatsProvider";
                case 32:
                    return "unregisterTetheringStatsProvider";
                case 33:
                    return "tetherLimitReached";
                case 34:
                    return "getNetworkStatsSummaryDev";
                case 35:
                    return "getNetworkStatsSummaryXt";
                case 36:
                    return "getNetworkStatsDetail";
                case 37:
                    return "getNetworkStatsUidDetail";
                case 38:
                    return "getNetworkStatsTethering";
                case 39:
                    return "setInterfaceQuota";
                case 40:
                    return "removeInterfaceQuota";
                case 41:
                    return "setInterfaceAlert";
                case 42:
                    return "removeInterfaceAlert";
                case 43:
                    return "setGlobalAlert";
                case 44:
                    return "setUidMeteredNetworkBlacklist";
                case 45:
                    return "setUidMeteredNetworkWhitelist";
                case 46:
                    return "setDataSaverModeEnabled";
                case 47:
                    return "setUidCleartextNetworkPolicy";
                case 48:
                    return "isBandwidthControlEnabled";
                case 49:
                    return "addIdleTimer";
                case 50:
                    return "removeIdleTimer";
                case 51:
                    return "setFirewallEnabled";
                case 52:
                    return "isFirewallEnabled";
                case 53:
                    return "setFirewallInterfaceRule";
                case 54:
                    return "setFirewallUidRule";
                case 55:
                    return "setFirewallUidRules";
                case 56:
                    return "setFirewallChainEnabled";
                case 57:
                    return "addVpnUidRanges";
                case 58:
                    return "removeVpnUidRanges";
                case 59:
                    return "registerNetworkActivityListener";
                case 60:
                    return "unregisterNetworkActivityListener";
                case 61:
                    return "isNetworkActive";
                case 62:
                    return "addInterfaceToNetwork";
                case 63:
                    return "removeInterfaceFromNetwork";
                case 64:
                    return "addLegacyRouteForNetId";
                case 65:
                    return "setDefaultNetId";
                case 66:
                    return "clearDefaultNetId";
                case 67:
                    return "setNetworkPermission";
                case 68:
                    return "allowProtect";
                case 69:
                    return "denyProtect";
                case 70:
                    return "addInterfaceToLocalNetwork";
                case 71:
                    return "removeInterfaceFromLocalNetwork";
                case 72:
                    return "removeRoutesFromLocalNetwork";
                case 73:
                    return "setAllowOnlyVpnForUids";
                case 74:
                    return "isNetworkRestricted";
                case 75:
                    return "getMiuiNetworkManager";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                boolean _arg0 = false;
                String[] _result;
                String _arg02;
                int _arg03;
                RouteInfo _arg1;
                NetworkStats _result2;
                int _arg04;
                boolean _result3;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        registerObserver(android.net.INetworkManagementEventObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        unregisterObserver(android.net.INetworkManagementEventObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result = listInterfaces();
                        reply.writeNoException();
                        reply.writeStringArray(_result);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        InterfaceConfiguration _result4 = getInterfaceConfig(data.readString());
                        reply.writeNoException();
                        if (_result4 != null) {
                            reply.writeInt(1);
                            _result4.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        InterfaceConfiguration _arg12;
                        data.enforceInterface(descriptor);
                        String _arg05 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (InterfaceConfiguration) InterfaceConfiguration.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        setInterfaceConfig(_arg05, _arg12);
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        clearInterfaceAddresses(data.readString());
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        setInterfaceDown(data.readString());
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        setInterfaceUp(data.readString());
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setInterfaceIpv6PrivacyExtensions(_arg02, _arg0);
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        disableIpv6(data.readString());
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        enableIpv6(data.readString());
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        setIPv6AddrGenMode(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (RouteInfo) RouteInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        addRoute(_arg03, _arg1);
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (RouteInfo) RouteInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        removeRoute(_arg03, _arg1);
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        setMtu(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        shutdown();
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        _arg0 = getIpForwardingEnabled();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setIpForwardingEnabled(_arg0);
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        startTethering(data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        stopTethering();
                        reply.writeNoException();
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        _arg0 = isTetheringStarted();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        tetherInterface(data.readString());
                        reply.writeNoException();
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        untetherInterface(data.readString());
                        reply.writeNoException();
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        _result = listTetheredInterfaces();
                        reply.writeNoException();
                        reply.writeStringArray(_result);
                        return true;
                    case 25:
                        Network _arg06;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (Network) Network.CREATOR.createFromParcel(data);
                        } else {
                            _arg06 = null;
                        }
                        setDnsForwarders(_arg06, data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        _result = getDnsForwarders();
                        reply.writeNoException();
                        reply.writeStringArray(_result);
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        startInterfaceForwarding(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        stopInterfaceForwarding(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 29:
                        data.enforceInterface(descriptor);
                        enableNat(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 30:
                        data.enforceInterface(descriptor);
                        disableNat(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 31:
                        data.enforceInterface(descriptor);
                        registerTetheringStatsProvider(android.net.ITetheringStatsProvider.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        return true;
                    case 32:
                        data.enforceInterface(descriptor);
                        unregisterTetheringStatsProvider(android.net.ITetheringStatsProvider.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 33:
                        data.enforceInterface(descriptor);
                        tetherLimitReached(android.net.ITetheringStatsProvider.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 34:
                        data.enforceInterface(descriptor);
                        _result2 = getNetworkStatsSummaryDev();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 35:
                        data.enforceInterface(descriptor);
                        _result2 = getNetworkStatsSummaryXt();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 36:
                        data.enforceInterface(descriptor);
                        _result2 = getNetworkStatsDetail();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 37:
                        data.enforceInterface(descriptor);
                        NetworkStats _result5 = getNetworkStatsUidDetail(data.readInt(), data.createStringArray());
                        reply.writeNoException();
                        if (_result5 != null) {
                            reply.writeInt(1);
                            _result5.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 38:
                        data.enforceInterface(descriptor);
                        NetworkStats _result6 = getNetworkStatsTethering(data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            reply.writeInt(1);
                            _result6.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 39:
                        data.enforceInterface(descriptor);
                        setInterfaceQuota(data.readString(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 40:
                        data.enforceInterface(descriptor);
                        removeInterfaceQuota(data.readString());
                        reply.writeNoException();
                        return true;
                    case 41:
                        data.enforceInterface(descriptor);
                        setInterfaceAlert(data.readString(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 42:
                        data.enforceInterface(descriptor);
                        removeInterfaceAlert(data.readString());
                        reply.writeNoException();
                        return true;
                    case 43:
                        data.enforceInterface(descriptor);
                        setGlobalAlert(data.readLong());
                        reply.writeNoException();
                        return true;
                    case 44:
                        data.enforceInterface(descriptor);
                        _arg04 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setUidMeteredNetworkBlacklist(_arg04, _arg0);
                        reply.writeNoException();
                        return true;
                    case 45:
                        data.enforceInterface(descriptor);
                        _arg04 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setUidMeteredNetworkWhitelist(_arg04, _arg0);
                        reply.writeNoException();
                        return true;
                    case 46:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result3 = setDataSaverModeEnabled(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 47:
                        data.enforceInterface(descriptor);
                        setUidCleartextNetworkPolicy(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 48:
                        data.enforceInterface(descriptor);
                        _arg0 = isBandwidthControlEnabled();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 49:
                        data.enforceInterface(descriptor);
                        addIdleTimer(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 50:
                        data.enforceInterface(descriptor);
                        removeIdleTimer(data.readString());
                        reply.writeNoException();
                        return true;
                    case 51:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setFirewallEnabled(_arg0);
                        reply.writeNoException();
                        return true;
                    case 52:
                        data.enforceInterface(descriptor);
                        _arg0 = isFirewallEnabled();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 53:
                        data.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setFirewallInterfaceRule(_arg02, _arg0);
                        reply.writeNoException();
                        return true;
                    case 54:
                        data.enforceInterface(descriptor);
                        setFirewallUidRule(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 55:
                        data.enforceInterface(descriptor);
                        setFirewallUidRules(data.readInt(), data.createIntArray(), data.createIntArray());
                        reply.writeNoException();
                        return true;
                    case 56:
                        data.enforceInterface(descriptor);
                        _arg04 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setFirewallChainEnabled(_arg04, _arg0);
                        reply.writeNoException();
                        return true;
                    case 57:
                        data.enforceInterface(descriptor);
                        addVpnUidRanges(data.readInt(), (UidRange[]) data.createTypedArray(UidRange.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 58:
                        data.enforceInterface(descriptor);
                        removeVpnUidRanges(data.readInt(), (UidRange[]) data.createTypedArray(UidRange.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 59:
                        data.enforceInterface(descriptor);
                        registerNetworkActivityListener(android.os.INetworkActivityListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 60:
                        data.enforceInterface(descriptor);
                        unregisterNetworkActivityListener(android.os.INetworkActivityListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 61:
                        data.enforceInterface(descriptor);
                        _arg0 = isNetworkActive();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 62:
                        data.enforceInterface(descriptor);
                        addInterfaceToNetwork(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 63:
                        data.enforceInterface(descriptor);
                        removeInterfaceFromNetwork(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 64:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (RouteInfo) RouteInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        addLegacyRouteForNetId(_arg03, _arg1, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 65:
                        data.enforceInterface(descriptor);
                        setDefaultNetId(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 66:
                        data.enforceInterface(descriptor);
                        clearDefaultNetId();
                        reply.writeNoException();
                        return true;
                    case 67:
                        data.enforceInterface(descriptor);
                        setNetworkPermission(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 68:
                        data.enforceInterface(descriptor);
                        allowProtect(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 69:
                        data.enforceInterface(descriptor);
                        denyProtect(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 70:
                        data.enforceInterface(descriptor);
                        addInterfaceToLocalNetwork(data.readString(), data.createTypedArrayList(RouteInfo.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 71:
                        data.enforceInterface(descriptor);
                        removeInterfaceFromLocalNetwork(data.readString());
                        reply.writeNoException();
                        return true;
                    case 72:
                        data.enforceInterface(descriptor);
                        _arg04 = removeRoutesFromLocalNetwork(data.createTypedArrayList(RouteInfo.CREATOR));
                        reply.writeNoException();
                        reply.writeInt(_arg04);
                        return true;
                    case 73:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setAllowOnlyVpnForUids(_arg0, (UidRange[]) data.createTypedArray(UidRange.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 74:
                        data.enforceInterface(descriptor);
                        _result3 = isNetworkRestricted(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 75:
                        data.enforceInterface(descriptor);
                        IBinder _result7 = getMiuiNetworkManager();
                        reply.writeNoException();
                        reply.writeStrongBinder(_result7);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(INetworkManagementService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INetworkManagementService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addIdleTimer(String str, int i, int i2) throws RemoteException;

    void addInterfaceToLocalNetwork(String str, List<RouteInfo> list) throws RemoteException;

    void addInterfaceToNetwork(String str, int i) throws RemoteException;

    void addLegacyRouteForNetId(int i, RouteInfo routeInfo, int i2) throws RemoteException;

    void addRoute(int i, RouteInfo routeInfo) throws RemoteException;

    void addVpnUidRanges(int i, UidRange[] uidRangeArr) throws RemoteException;

    void allowProtect(int i) throws RemoteException;

    void clearDefaultNetId() throws RemoteException;

    @UnsupportedAppUsage
    void clearInterfaceAddresses(String str) throws RemoteException;

    void denyProtect(int i) throws RemoteException;

    @UnsupportedAppUsage
    void disableIpv6(String str) throws RemoteException;

    @UnsupportedAppUsage
    void disableNat(String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void enableIpv6(String str) throws RemoteException;

    @UnsupportedAppUsage
    void enableNat(String str, String str2) throws RemoteException;

    String[] getDnsForwarders() throws RemoteException;

    @UnsupportedAppUsage
    InterfaceConfiguration getInterfaceConfig(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean getIpForwardingEnabled() throws RemoteException;

    IBinder getMiuiNetworkManager() throws RemoteException;

    NetworkStats getNetworkStatsDetail() throws RemoteException;

    NetworkStats getNetworkStatsSummaryDev() throws RemoteException;

    NetworkStats getNetworkStatsSummaryXt() throws RemoteException;

    NetworkStats getNetworkStatsTethering(int i) throws RemoteException;

    NetworkStats getNetworkStatsUidDetail(int i, String[] strArr) throws RemoteException;

    @UnsupportedAppUsage
    boolean isBandwidthControlEnabled() throws RemoteException;

    boolean isFirewallEnabled() throws RemoteException;

    boolean isNetworkActive() throws RemoteException;

    boolean isNetworkRestricted(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean isTetheringStarted() throws RemoteException;

    String[] listInterfaces() throws RemoteException;

    String[] listTetheredInterfaces() throws RemoteException;

    void registerNetworkActivityListener(INetworkActivityListener iNetworkActivityListener) throws RemoteException;

    @UnsupportedAppUsage
    void registerObserver(INetworkManagementEventObserver iNetworkManagementEventObserver) throws RemoteException;

    void registerTetheringStatsProvider(ITetheringStatsProvider iTetheringStatsProvider, String str) throws RemoteException;

    void removeIdleTimer(String str) throws RemoteException;

    void removeInterfaceAlert(String str) throws RemoteException;

    void removeInterfaceFromLocalNetwork(String str) throws RemoteException;

    void removeInterfaceFromNetwork(String str, int i) throws RemoteException;

    void removeInterfaceQuota(String str) throws RemoteException;

    void removeRoute(int i, RouteInfo routeInfo) throws RemoteException;

    int removeRoutesFromLocalNetwork(List<RouteInfo> list) throws RemoteException;

    void removeVpnUidRanges(int i, UidRange[] uidRangeArr) throws RemoteException;

    void setAllowOnlyVpnForUids(boolean z, UidRange[] uidRangeArr) throws RemoteException;

    boolean setDataSaverModeEnabled(boolean z) throws RemoteException;

    void setDefaultNetId(int i) throws RemoteException;

    void setDnsForwarders(Network network, String[] strArr) throws RemoteException;

    void setFirewallChainEnabled(int i, boolean z) throws RemoteException;

    void setFirewallEnabled(boolean z) throws RemoteException;

    void setFirewallInterfaceRule(String str, boolean z) throws RemoteException;

    void setFirewallUidRule(int i, int i2, int i3) throws RemoteException;

    void setFirewallUidRules(int i, int[] iArr, int[] iArr2) throws RemoteException;

    void setGlobalAlert(long j) throws RemoteException;

    @UnsupportedAppUsage
    void setIPv6AddrGenMode(String str, int i) throws RemoteException;

    void setInterfaceAlert(String str, long j) throws RemoteException;

    @UnsupportedAppUsage
    void setInterfaceConfig(String str, InterfaceConfiguration interfaceConfiguration) throws RemoteException;

    void setInterfaceDown(String str) throws RemoteException;

    @UnsupportedAppUsage
    void setInterfaceIpv6PrivacyExtensions(String str, boolean z) throws RemoteException;

    void setInterfaceQuota(String str, long j) throws RemoteException;

    void setInterfaceUp(String str) throws RemoteException;

    @UnsupportedAppUsage
    void setIpForwardingEnabled(boolean z) throws RemoteException;

    void setMtu(String str, int i) throws RemoteException;

    void setNetworkPermission(int i, int i2) throws RemoteException;

    void setUidCleartextNetworkPolicy(int i, int i2) throws RemoteException;

    void setUidMeteredNetworkBlacklist(int i, boolean z) throws RemoteException;

    void setUidMeteredNetworkWhitelist(int i, boolean z) throws RemoteException;

    void shutdown() throws RemoteException;

    void startInterfaceForwarding(String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void startTethering(String[] strArr) throws RemoteException;

    void stopInterfaceForwarding(String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void stopTethering() throws RemoteException;

    @UnsupportedAppUsage
    void tetherInterface(String str) throws RemoteException;

    void tetherLimitReached(ITetheringStatsProvider iTetheringStatsProvider) throws RemoteException;

    void unregisterNetworkActivityListener(INetworkActivityListener iNetworkActivityListener) throws RemoteException;

    @UnsupportedAppUsage
    void unregisterObserver(INetworkManagementEventObserver iNetworkManagementEventObserver) throws RemoteException;

    void unregisterTetheringStatsProvider(ITetheringStatsProvider iTetheringStatsProvider) throws RemoteException;

    @UnsupportedAppUsage
    void untetherInterface(String str) throws RemoteException;
}
