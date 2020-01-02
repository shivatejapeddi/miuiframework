package android.content.rollback;

import android.content.IntentSender;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRollbackManager extends IInterface {

    public static class Default implements IRollbackManager {
        public ParceledListSlice getAvailableRollbacks() throws RemoteException {
            return null;
        }

        public ParceledListSlice getRecentlyExecutedRollbacks() throws RemoteException {
            return null;
        }

        public void commitRollback(int rollbackId, ParceledListSlice causePackages, String callerPackageName, IntentSender statusReceiver) throws RemoteException {
        }

        public void snapshotAndRestoreUserData(String packageName, int[] userIds, int appId, long ceDataInode, String seInfo, int token) throws RemoteException {
        }

        public void reloadPersistedData() throws RemoteException {
        }

        public void expireRollbackForPackage(String packageName) throws RemoteException {
        }

        public boolean notifyStagedSession(int sessionId) throws RemoteException {
            return false;
        }

        public void notifyStagedApkSession(int originalSessionId, int apkSessionId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRollbackManager {
        private static final String DESCRIPTOR = "android.content.rollback.IRollbackManager";
        static final int TRANSACTION_commitRollback = 3;
        static final int TRANSACTION_expireRollbackForPackage = 6;
        static final int TRANSACTION_getAvailableRollbacks = 1;
        static final int TRANSACTION_getRecentlyExecutedRollbacks = 2;
        static final int TRANSACTION_notifyStagedApkSession = 8;
        static final int TRANSACTION_notifyStagedSession = 7;
        static final int TRANSACTION_reloadPersistedData = 5;
        static final int TRANSACTION_snapshotAndRestoreUserData = 4;

        private static class Proxy implements IRollbackManager {
            public static IRollbackManager sDefaultImpl;
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

            public ParceledListSlice getAvailableRollbacks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice parceledListSlice = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getAvailableRollbacks();
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

            public ParceledListSlice getRecentlyExecutedRollbacks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice parceledListSlice = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getRecentlyExecutedRollbacks();
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

            public void commitRollback(int rollbackId, ParceledListSlice causePackages, String callerPackageName, IntentSender statusReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rollbackId);
                    if (causePackages != null) {
                        _data.writeInt(1);
                        causePackages.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackageName);
                    if (statusReceiver != null) {
                        _data.writeInt(1);
                        statusReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().commitRollback(rollbackId, causePackages, callerPackageName, statusReceiver);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void snapshotAndRestoreUserData(String packageName, int[] userIds, int appId, long ceDataInode, String seInfo, int token) throws RemoteException {
                Throwable th;
                int[] iArr;
                int i;
                long j;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        iArr = userIds;
                        i = appId;
                        j = ceDataInode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeIntArray(userIds);
                    } catch (Throwable th3) {
                        th = th3;
                        i = appId;
                        j = ceDataInode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(appId);
                        try {
                            _data.writeLong(ceDataInode);
                            _data.writeString(seInfo);
                            _data.writeInt(token);
                            if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().snapshotAndRestoreUserData(packageName, userIds, appId, ceDataInode, seInfo, token);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        j = ceDataInode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = packageName;
                    iArr = userIds;
                    i = appId;
                    j = ceDataInode;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void reloadPersistedData() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reloadPersistedData();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void expireRollbackForPackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().expireRollbackForPackage(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean notifyStagedSession(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().notifyStagedSession(sessionId);
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

            public void notifyStagedApkSession(int originalSessionId, int apkSessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(originalSessionId);
                    _data.writeInt(apkSessionId);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyStagedApkSession(originalSessionId, apkSessionId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRollbackManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRollbackManager)) {
                return new Proxy(obj);
            }
            return (IRollbackManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getAvailableRollbacks";
                case 2:
                    return "getRecentlyExecutedRollbacks";
                case 3:
                    return "commitRollback";
                case 4:
                    return "snapshotAndRestoreUserData";
                case 5:
                    return "reloadPersistedData";
                case 6:
                    return "expireRollbackForPackage";
                case 7:
                    return "notifyStagedSession";
                case 8:
                    return "notifyStagedApkSession";
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
                ParceledListSlice _result;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _result = getAvailableRollbacks();
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
                        _result = getRecentlyExecutedRollbacks();
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        IntentSender _arg3;
                        parcel.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _result = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        String _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (IntentSender) IntentSender.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        commitRollback(_arg0, _result, _arg2, _arg3);
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        snapshotAndRestoreUserData(data.readString(), data.createIntArray(), data.readInt(), data.readLong(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        reloadPersistedData();
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        expireRollbackForPackage(data.readString());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        boolean _result2 = notifyStagedSession(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        notifyStagedApkSession(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IRollbackManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRollbackManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void commitRollback(int i, ParceledListSlice parceledListSlice, String str, IntentSender intentSender) throws RemoteException;

    void expireRollbackForPackage(String str) throws RemoteException;

    ParceledListSlice getAvailableRollbacks() throws RemoteException;

    ParceledListSlice getRecentlyExecutedRollbacks() throws RemoteException;

    void notifyStagedApkSession(int i, int i2) throws RemoteException;

    boolean notifyStagedSession(int i) throws RemoteException;

    void reloadPersistedData() throws RemoteException;

    void snapshotAndRestoreUserData(String str, int[] iArr, int i, long j, String str2, int i2) throws RemoteException;
}
