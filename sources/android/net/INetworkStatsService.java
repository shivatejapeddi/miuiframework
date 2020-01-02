package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.net.VpnInfo;

public interface INetworkStatsService extends IInterface {

    public static class Default implements INetworkStatsService {
        public INetworkStatsSession openSession() throws RemoteException {
            return null;
        }

        public INetworkStatsSession openSessionForUsageStats(int flags, String callingPackage) throws RemoteException {
            return null;
        }

        public NetworkStats getDataLayerSnapshotForUid(int uid) throws RemoteException {
            return null;
        }

        public NetworkStats getDetailedUidStats(String[] requiredIfaces) throws RemoteException {
            return null;
        }

        public String[] getMobileIfaces() throws RemoteException {
            return null;
        }

        public void incrementOperationCount(int uid, int tag, int operationCount) throws RemoteException {
        }

        public void forceUpdateIfaces(Network[] defaultNetworks, VpnInfo[] vpnArray, NetworkState[] networkStates, String activeIface) throws RemoteException {
        }

        public void forceUpdate() throws RemoteException {
        }

        public DataUsageRequest registerUsageCallback(String callingPackage, DataUsageRequest request, Messenger messenger, IBinder binder) throws RemoteException {
            return null;
        }

        public void unregisterUsageRequest(DataUsageRequest request) throws RemoteException {
        }

        public long getUidStats(int uid, int type) throws RemoteException {
            return 0;
        }

        public long getIfaceStats(String iface, int type) throws RemoteException {
            return 0;
        }

        public long getTotalStats(int type) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INetworkStatsService {
        private static final String DESCRIPTOR = "android.net.INetworkStatsService";
        static final int TRANSACTION_forceUpdate = 8;
        static final int TRANSACTION_forceUpdateIfaces = 7;
        static final int TRANSACTION_getDataLayerSnapshotForUid = 3;
        static final int TRANSACTION_getDetailedUidStats = 4;
        static final int TRANSACTION_getIfaceStats = 12;
        static final int TRANSACTION_getMobileIfaces = 5;
        static final int TRANSACTION_getTotalStats = 13;
        static final int TRANSACTION_getUidStats = 11;
        static final int TRANSACTION_incrementOperationCount = 6;
        static final int TRANSACTION_openSession = 1;
        static final int TRANSACTION_openSessionForUsageStats = 2;
        static final int TRANSACTION_registerUsageCallback = 9;
        static final int TRANSACTION_unregisterUsageRequest = 10;

        private static class Proxy implements INetworkStatsService {
            public static INetworkStatsService sDefaultImpl;
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

            public INetworkStatsSession openSession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    INetworkStatsSession iNetworkStatsSession = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        iNetworkStatsSession = Stub.getDefaultImpl();
                        if (iNetworkStatsSession != 0) {
                            iNetworkStatsSession = Stub.getDefaultImpl().openSession();
                            return iNetworkStatsSession;
                        }
                    }
                    _reply.readException();
                    iNetworkStatsSession = android.net.INetworkStatsSession.Stub.asInterface(_reply.readStrongBinder());
                    INetworkStatsSession _result = iNetworkStatsSession;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public INetworkStatsSession openSessionForUsageStats(int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    INetworkStatsSession iNetworkStatsSession = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        iNetworkStatsSession = Stub.getDefaultImpl();
                        if (iNetworkStatsSession != 0) {
                            iNetworkStatsSession = Stub.getDefaultImpl().openSessionForUsageStats(flags, callingPackage);
                            return iNetworkStatsSession;
                        }
                    }
                    _reply.readException();
                    iNetworkStatsSession = android.net.INetworkStatsSession.Stub.asInterface(_reply.readStrongBinder());
                    INetworkStatsSession _result = iNetworkStatsSession;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkStats getDataLayerSnapshotForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    NetworkStats networkStats = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        networkStats = Stub.getDefaultImpl();
                        if (networkStats != 0) {
                            networkStats = Stub.getDefaultImpl().getDataLayerSnapshotForUid(uid);
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

            public NetworkStats getDetailedUidStats(String[] requiredIfaces) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(requiredIfaces);
                    NetworkStats networkStats = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        networkStats = Stub.getDefaultImpl();
                        if (networkStats != 0) {
                            networkStats = Stub.getDefaultImpl().getDetailedUidStats(requiredIfaces);
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

            public String[] getMobileIfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getMobileIfaces();
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

            public void incrementOperationCount(int uid, int tag, int operationCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(tag);
                    _data.writeInt(operationCount);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().incrementOperationCount(uid, tag, operationCount);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forceUpdateIfaces(Network[] defaultNetworks, VpnInfo[] vpnArray, NetworkState[] networkStates, String activeIface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(defaultNetworks, 0);
                    _data.writeTypedArray(vpnArray, 0);
                    _data.writeTypedArray(networkStates, 0);
                    _data.writeString(activeIface);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forceUpdateIfaces(defaultNetworks, vpnArray, networkStates, activeIface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forceUpdate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forceUpdate();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public DataUsageRequest registerUsageCallback(String callingPackage, DataUsageRequest request, Messenger messenger, IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    DataUsageRequest dataUsageRequest = 0;
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
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
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        dataUsageRequest = Stub.getDefaultImpl();
                        if (dataUsageRequest != 0) {
                            dataUsageRequest = Stub.getDefaultImpl().registerUsageCallback(callingPackage, request, messenger, binder);
                            return dataUsageRequest;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        dataUsageRequest = (DataUsageRequest) DataUsageRequest.CREATOR.createFromParcel(_reply);
                    } else {
                        dataUsageRequest = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return dataUsageRequest;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterUsageRequest(DataUsageRequest request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterUsageRequest(request);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getUidStats(int uid, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(type);
                    long j = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getUidStats(uid, type);
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

            public long getIfaceStats(String iface, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(type);
                    long j = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getIfaceStats(iface, type);
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

            public long getTotalStats(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    long j = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getTotalStats(type);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INetworkStatsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INetworkStatsService)) {
                return new Proxy(obj);
            }
            return (INetworkStatsService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "openSession";
                case 2:
                    return "openSessionForUsageStats";
                case 3:
                    return "getDataLayerSnapshotForUid";
                case 4:
                    return "getDetailedUidStats";
                case 5:
                    return "getMobileIfaces";
                case 6:
                    return "incrementOperationCount";
                case 7:
                    return "forceUpdateIfaces";
                case 8:
                    return "forceUpdate";
                case 9:
                    return "registerUsageCallback";
                case 10:
                    return "unregisterUsageRequest";
                case 11:
                    return "getUidStats";
                case 12:
                    return "getIfaceStats";
                case 13:
                    return "getTotalStats";
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
                IBinder iBinder = null;
                NetworkStats _result;
                long _result2;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        INetworkStatsSession _result3 = openSession();
                        reply.writeNoException();
                        if (_result3 != null) {
                            iBinder = _result3.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        INetworkStatsSession _result4 = openSessionForUsageStats(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result4 != null) {
                            iBinder = _result4.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result = getDataLayerSnapshotForUid(data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result = getDetailedUidStats(data.createStringArray());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        String[] _result5 = getMobileIfaces();
                        reply.writeNoException();
                        reply.writeStringArray(_result5);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        incrementOperationCount(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        forceUpdateIfaces((Network[]) data.createTypedArray(Network.CREATOR), (VpnInfo[]) data.createTypedArray(VpnInfo.CREATOR), (NetworkState[]) data.createTypedArray(NetworkState.CREATOR), data.readString());
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        forceUpdate();
                        reply.writeNoException();
                        return true;
                    case 9:
                        DataUsageRequest _arg1;
                        Messenger _arg2;
                        data.enforceInterface(descriptor);
                        String _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = (DataUsageRequest) DataUsageRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (Messenger) Messenger.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        DataUsageRequest _result6 = registerUsageCallback(_arg0, _arg1, _arg2, data.readStrongBinder());
                        reply.writeNoException();
                        if (_result6 != null) {
                            reply.writeInt(1);
                            _result6.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 10:
                        DataUsageRequest _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (DataUsageRequest) DataUsageRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        unregisterUsageRequest(_arg02);
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        _result2 = getUidStats(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeLong(_result2);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        _result2 = getIfaceStats(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeLong(_result2);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        long _result7 = getTotalStats(data.readInt());
                        reply.writeNoException();
                        reply.writeLong(_result7);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(INetworkStatsService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INetworkStatsService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    void forceUpdate() throws RemoteException;

    void forceUpdateIfaces(Network[] networkArr, VpnInfo[] vpnInfoArr, NetworkState[] networkStateArr, String str) throws RemoteException;

    @UnsupportedAppUsage
    NetworkStats getDataLayerSnapshotForUid(int i) throws RemoteException;

    NetworkStats getDetailedUidStats(String[] strArr) throws RemoteException;

    long getIfaceStats(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    String[] getMobileIfaces() throws RemoteException;

    long getTotalStats(int i) throws RemoteException;

    long getUidStats(int i, int i2) throws RemoteException;

    void incrementOperationCount(int i, int i2, int i3) throws RemoteException;

    @UnsupportedAppUsage
    INetworkStatsSession openSession() throws RemoteException;

    @UnsupportedAppUsage
    INetworkStatsSession openSessionForUsageStats(int i, String str) throws RemoteException;

    DataUsageRequest registerUsageCallback(String str, DataUsageRequest dataUsageRequest, Messenger messenger, IBinder iBinder) throws RemoteException;

    void unregisterUsageRequest(DataUsageRequest dataUsageRequest) throws RemoteException;
}
