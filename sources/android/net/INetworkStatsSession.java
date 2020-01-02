package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkStatsSession extends IInterface {

    public static class Default implements INetworkStatsSession {
        public NetworkStats getDeviceSummaryForNetwork(NetworkTemplate template, long start, long end) throws RemoteException {
            return null;
        }

        public NetworkStats getSummaryForNetwork(NetworkTemplate template, long start, long end) throws RemoteException {
            return null;
        }

        public NetworkStatsHistory getHistoryForNetwork(NetworkTemplate template, int fields) throws RemoteException {
            return null;
        }

        public NetworkStats getSummaryForAllUid(NetworkTemplate template, long start, long end, boolean includeTags) throws RemoteException {
            return null;
        }

        public NetworkStatsHistory getHistoryForUid(NetworkTemplate template, int uid, int set, int tag, int fields) throws RemoteException {
            return null;
        }

        public NetworkStatsHistory getHistoryIntervalForUid(NetworkTemplate template, int uid, int set, int tag, int fields, long start, long end) throws RemoteException {
            return null;
        }

        public int[] getRelevantUids() throws RemoteException {
            return null;
        }

        public void close() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INetworkStatsSession {
        private static final String DESCRIPTOR = "android.net.INetworkStatsSession";
        static final int TRANSACTION_close = 8;
        static final int TRANSACTION_getDeviceSummaryForNetwork = 1;
        static final int TRANSACTION_getHistoryForNetwork = 3;
        static final int TRANSACTION_getHistoryForUid = 5;
        static final int TRANSACTION_getHistoryIntervalForUid = 6;
        static final int TRANSACTION_getRelevantUids = 7;
        static final int TRANSACTION_getSummaryForAllUid = 4;
        static final int TRANSACTION_getSummaryForNetwork = 2;

        private static class Proxy implements INetworkStatsSession {
            public static INetworkStatsSession sDefaultImpl;
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

            public NetworkStats getDeviceSummaryForNetwork(NetworkTemplate template, long start, long end) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkStats networkStats = 0;
                    if (template != null) {
                        _data.writeInt(1);
                        template.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(start);
                    _data.writeLong(end);
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        networkStats = Stub.getDefaultImpl();
                        if (networkStats != 0) {
                            networkStats = Stub.getDefaultImpl().getDeviceSummaryForNetwork(template, start, end);
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

            public NetworkStats getSummaryForNetwork(NetworkTemplate template, long start, long end) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (template != null) {
                        _data.writeInt(1);
                        template.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(start);
                    _data.writeLong(end);
                    NetworkStats networkStats = this.mRemote;
                    if (!networkStats.transact(2, _data, _reply, 0)) {
                        networkStats = Stub.getDefaultImpl();
                        if (networkStats != null) {
                            networkStats = Stub.getDefaultImpl().getSummaryForNetwork(template, start, end);
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

            public NetworkStatsHistory getHistoryForNetwork(NetworkTemplate template, int fields) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (template != null) {
                        _data.writeInt(1);
                        template.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(fields);
                    NetworkStatsHistory networkStatsHistory = this.mRemote;
                    if (!networkStatsHistory.transact(3, _data, _reply, 0)) {
                        networkStatsHistory = Stub.getDefaultImpl();
                        if (networkStatsHistory != null) {
                            networkStatsHistory = Stub.getDefaultImpl().getHistoryForNetwork(template, fields);
                            return networkStatsHistory;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkStatsHistory = (NetworkStatsHistory) NetworkStatsHistory.CREATOR.createFromParcel(_reply);
                    } else {
                        networkStatsHistory = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkStatsHistory;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkStats getSummaryForAllUid(NetworkTemplate template, long start, long end, boolean includeTags) throws RemoteException {
                Throwable th;
                long j;
                NetworkTemplate networkTemplate = template;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (networkTemplate != null) {
                        _data.writeInt(1);
                        networkTemplate.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeLong(start);
                    } catch (Throwable th2) {
                        th = th2;
                        j = end;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(end);
                        if (!includeTags) {
                            i = 0;
                        }
                        _data.writeInt(i);
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        NetworkStats _result;
                        if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() != 0) {
                                _result = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
                            } else {
                                _result = null;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().getSummaryForAllUid(template, start, end, includeTags);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    long j2 = start;
                    j = end;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public NetworkStatsHistory getHistoryForUid(NetworkTemplate template, int uid, int set, int tag, int fields) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (template != null) {
                        _data.writeInt(1);
                        template.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    _data.writeInt(set);
                    _data.writeInt(tag);
                    _data.writeInt(fields);
                    NetworkStatsHistory networkStatsHistory = this.mRemote;
                    if (!networkStatsHistory.transact(5, _data, _reply, 0)) {
                        networkStatsHistory = Stub.getDefaultImpl();
                        if (networkStatsHistory != null) {
                            networkStatsHistory = Stub.getDefaultImpl().getHistoryForUid(template, uid, set, tag, fields);
                            return networkStatsHistory;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkStatsHistory = (NetworkStatsHistory) NetworkStatsHistory.CREATOR.createFromParcel(_reply);
                    } else {
                        networkStatsHistory = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkStatsHistory;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkStatsHistory getHistoryIntervalForUid(NetworkTemplate template, int uid, int set, int tag, int fields, long start, long end) throws RemoteException {
                Throwable th;
                int i;
                NetworkTemplate networkTemplate = template;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkTemplate != null) {
                        _data.writeInt(1);
                        networkTemplate.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(uid);
                    } catch (Throwable th2) {
                        th = th2;
                        i = set;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(set);
                        _data.writeInt(tag);
                        _data.writeInt(fields);
                        _data.writeLong(start);
                        _data.writeLong(end);
                        NetworkStatsHistory _result;
                        if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() != 0) {
                                _result = (NetworkStatsHistory) NetworkStatsHistory.CREATOR.createFromParcel(_reply);
                            } else {
                                _result = null;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().getHistoryIntervalForUid(template, uid, set, tag, fields, start, end);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i2 = uid;
                    i = set;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int[] getRelevantUids() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int[] iArr = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getRelevantUids();
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void close() throws RemoteException {
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
                    Stub.getDefaultImpl().close();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INetworkStatsSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INetworkStatsSession)) {
                return new Proxy(obj);
            }
            return (INetworkStatsSession) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getDeviceSummaryForNetwork";
                case 2:
                    return "getSummaryForNetwork";
                case 3:
                    return "getHistoryForNetwork";
                case 4:
                    return "getSummaryForAllUid";
                case 5:
                    return "getHistoryForUid";
                case 6:
                    return "getHistoryIntervalForUid";
                case 7:
                    return "getRelevantUids";
                case 8:
                    return "close";
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
                NetworkTemplate _arg0;
                NetworkStats _result;
                NetworkStatsHistory _result2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (NetworkTemplate) NetworkTemplate.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result = getDeviceSummaryForNetwork(_arg0, data.readLong(), data.readLong());
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
                        if (data.readInt() != 0) {
                            _arg0 = (NetworkTemplate) NetworkTemplate.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result = getSummaryForNetwork(_arg0, data.readLong(), data.readLong());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (NetworkTemplate) NetworkTemplate.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        NetworkStatsHistory _result3 = getHistoryForNetwork(_arg0, data.readInt());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (NetworkTemplate) NetworkTemplate.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result = getSummaryForAllUid(_arg0, data.readLong(), data.readLong(), data.readInt() != 0);
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
                        if (data.readInt() != 0) {
                            _arg0 = (NetworkTemplate) NetworkTemplate.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = getHistoryForUid(_arg0, data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (NetworkTemplate) NetworkTemplate.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = getHistoryIntervalForUid(_arg0, data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readLong(), data.readLong());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        int[] _result4 = getRelevantUids();
                        reply.writeNoException();
                        parcel2.writeIntArray(_result4);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        close();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(INetworkStatsSession impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INetworkStatsSession getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    void close() throws RemoteException;

    NetworkStats getDeviceSummaryForNetwork(NetworkTemplate networkTemplate, long j, long j2) throws RemoteException;

    @UnsupportedAppUsage
    NetworkStatsHistory getHistoryForNetwork(NetworkTemplate networkTemplate, int i) throws RemoteException;

    @UnsupportedAppUsage
    NetworkStatsHistory getHistoryForUid(NetworkTemplate networkTemplate, int i, int i2, int i3, int i4) throws RemoteException;

    NetworkStatsHistory getHistoryIntervalForUid(NetworkTemplate networkTemplate, int i, int i2, int i3, int i4, long j, long j2) throws RemoteException;

    int[] getRelevantUids() throws RemoteException;

    @UnsupportedAppUsage
    NetworkStats getSummaryForAllUid(NetworkTemplate networkTemplate, long j, long j2, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    NetworkStats getSummaryForNetwork(NetworkTemplate networkTemplate, long j, long j2) throws RemoteException;
}
