package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface INetworkScoreService extends IInterface {

    public static class Default implements INetworkScoreService {
        public boolean updateScores(ScoredNetwork[] networks) throws RemoteException {
            return false;
        }

        public boolean clearScores() throws RemoteException {
            return false;
        }

        public boolean setActiveScorer(String packageName) throws RemoteException {
            return false;
        }

        public void disableScoring() throws RemoteException {
        }

        public void registerNetworkScoreCache(int networkType, INetworkScoreCache scoreCache, int filterType) throws RemoteException {
        }

        public void unregisterNetworkScoreCache(int networkType, INetworkScoreCache scoreCache) throws RemoteException {
        }

        public boolean requestScores(NetworkKey[] networks) throws RemoteException {
            return false;
        }

        public boolean isCallerActiveScorer(int callingUid) throws RemoteException {
            return false;
        }

        public String getActiveScorerPackage() throws RemoteException {
            return null;
        }

        public NetworkScorerAppData getActiveScorer() throws RemoteException {
            return null;
        }

        public List<NetworkScorerAppData> getAllValidScorers() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INetworkScoreService {
        private static final String DESCRIPTOR = "android.net.INetworkScoreService";
        static final int TRANSACTION_clearScores = 2;
        static final int TRANSACTION_disableScoring = 4;
        static final int TRANSACTION_getActiveScorer = 10;
        static final int TRANSACTION_getActiveScorerPackage = 9;
        static final int TRANSACTION_getAllValidScorers = 11;
        static final int TRANSACTION_isCallerActiveScorer = 8;
        static final int TRANSACTION_registerNetworkScoreCache = 5;
        static final int TRANSACTION_requestScores = 7;
        static final int TRANSACTION_setActiveScorer = 3;
        static final int TRANSACTION_unregisterNetworkScoreCache = 6;
        static final int TRANSACTION_updateScores = 1;

        private static class Proxy implements INetworkScoreService {
            public static INetworkScoreService sDefaultImpl;
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

            public boolean updateScores(ScoredNetwork[] networks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = false;
                    _data.writeTypedArray(networks, 0);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            _result = true;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().updateScores(networks);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean clearScores() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().clearScores();
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

            public boolean setActiveScorer(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setActiveScorer(packageName);
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

            public void disableScoring() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableScoring();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerNetworkScoreCache(int networkType, INetworkScoreCache scoreCache, int filterType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeStrongBinder(scoreCache != null ? scoreCache.asBinder() : null);
                    _data.writeInt(filterType);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerNetworkScoreCache(networkType, scoreCache, filterType);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterNetworkScoreCache(int networkType, INetworkScoreCache scoreCache) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeStrongBinder(scoreCache != null ? scoreCache.asBinder() : null);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterNetworkScoreCache(networkType, scoreCache);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean requestScores(NetworkKey[] networks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = false;
                    _data.writeTypedArray(networks, 0);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            _result = true;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().requestScores(networks);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isCallerActiveScorer(int callingUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callingUid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isCallerActiveScorer(callingUid);
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

            public String getActiveScorerPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getActiveScorerPackage();
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

            public NetworkScorerAppData getActiveScorer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    NetworkScorerAppData networkScorerAppData = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        networkScorerAppData = Stub.getDefaultImpl();
                        if (networkScorerAppData != 0) {
                            networkScorerAppData = Stub.getDefaultImpl().getActiveScorer();
                            return networkScorerAppData;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkScorerAppData = (NetworkScorerAppData) NetworkScorerAppData.CREATOR.createFromParcel(_reply);
                    } else {
                        networkScorerAppData = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkScorerAppData;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<NetworkScorerAppData> getAllValidScorers() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<NetworkScorerAppData> list = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAllValidScorers();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(NetworkScorerAppData.CREATOR);
                    List<NetworkScorerAppData> _result = list;
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

        public static INetworkScoreService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INetworkScoreService)) {
                return new Proxy(obj);
            }
            return (INetworkScoreService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "updateScores";
                case 2:
                    return "clearScores";
                case 3:
                    return "setActiveScorer";
                case 4:
                    return "disableScoring";
                case 5:
                    return "registerNetworkScoreCache";
                case 6:
                    return "unregisterNetworkScoreCache";
                case 7:
                    return "requestScores";
                case 8:
                    return "isCallerActiveScorer";
                case 9:
                    return "getActiveScorerPackage";
                case 10:
                    return "getActiveScorer";
                case 11:
                    return "getAllValidScorers";
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
                boolean _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _result = updateScores((ScoredNetwork[]) data.createTypedArray(ScoredNetwork.CREATOR));
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        boolean _result2 = clearScores();
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result = setActiveScorer(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        disableScoring();
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        registerNetworkScoreCache(data.readInt(), android.net.INetworkScoreCache.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        unregisterNetworkScoreCache(data.readInt(), android.net.INetworkScoreCache.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result = requestScores((NetworkKey[]) data.createTypedArray(NetworkKey.CREATOR));
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result = isCallerActiveScorer(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        String _result3 = getActiveScorerPackage();
                        reply.writeNoException();
                        reply.writeString(_result3);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        NetworkScorerAppData _result4 = getActiveScorer();
                        reply.writeNoException();
                        if (_result4 != null) {
                            reply.writeInt(1);
                            _result4.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        List<NetworkScorerAppData> _result5 = getAllValidScorers();
                        reply.writeNoException();
                        reply.writeTypedList(_result5);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(INetworkScoreService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INetworkScoreService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean clearScores() throws RemoteException;

    void disableScoring() throws RemoteException;

    NetworkScorerAppData getActiveScorer() throws RemoteException;

    String getActiveScorerPackage() throws RemoteException;

    List<NetworkScorerAppData> getAllValidScorers() throws RemoteException;

    boolean isCallerActiveScorer(int i) throws RemoteException;

    void registerNetworkScoreCache(int i, INetworkScoreCache iNetworkScoreCache, int i2) throws RemoteException;

    boolean requestScores(NetworkKey[] networkKeyArr) throws RemoteException;

    boolean setActiveScorer(String str) throws RemoteException;

    void unregisterNetworkScoreCache(int i, INetworkScoreCache iNetworkScoreCache) throws RemoteException;

    boolean updateScores(ScoredNetwork[] scoredNetworkArr) throws RemoteException;
}
