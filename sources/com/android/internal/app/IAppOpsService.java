package com.android.internal.app;

import android.annotation.UnsupportedAppUsage;
import android.app.AppOpsManager.HistoricalOps;
import android.app.AppOpsManager.PackageOps;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteCallback;
import android.os.RemoteException;
import java.util.List;

public interface IAppOpsService extends IInterface {

    public static class Default implements IAppOpsService {
        public int checkOperation(int code, int uid, String packageName) throws RemoteException {
            return 0;
        }

        public int noteOperation(int code, int uid, String packageName) throws RemoteException {
            return 0;
        }

        public int startOperation(IBinder token, int code, int uid, String packageName, boolean startIfModeDefault) throws RemoteException {
            return 0;
        }

        public void finishOperation(IBinder token, int code, int uid, String packageName) throws RemoteException {
        }

        public void startWatchingMode(int op, String packageName, IAppOpsCallback callback) throws RemoteException {
        }

        public void stopWatchingMode(IAppOpsCallback callback) throws RemoteException {
        }

        public IBinder getToken(IBinder clientToken) throws RemoteException {
            return null;
        }

        public int permissionToOpCode(String permission) throws RemoteException {
            return 0;
        }

        public int checkAudioOperation(int code, int usage, int uid, String packageName) throws RemoteException {
            return 0;
        }

        public int noteProxyOperation(int code, int proxyUid, String proxyPackageName, int callingUid, String callingPackageName) throws RemoteException {
            return 0;
        }

        public int checkPackage(int uid, String packageName) throws RemoteException {
            return 0;
        }

        public List<PackageOps> getPackagesForOps(int[] ops) throws RemoteException {
            return null;
        }

        public List<PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException {
            return null;
        }

        public void getHistoricalOps(int uid, String packageName, List<String> list, long beginTimeMillis, long endTimeMillis, int flags, RemoteCallback callback) throws RemoteException {
        }

        public void getHistoricalOpsFromDiskRaw(int uid, String packageName, List<String> list, long beginTimeMillis, long endTimeMillis, int flags, RemoteCallback callback) throws RemoteException {
        }

        public void offsetHistory(long duration) throws RemoteException {
        }

        public void setHistoryParameters(int mode, long baseSnapshotInterval, int compressionStep) throws RemoteException {
        }

        public void addHistoricalOps(HistoricalOps ops) throws RemoteException {
        }

        public void resetHistoryParameters() throws RemoteException {
        }

        public void clearHistory() throws RemoteException {
        }

        public List<PackageOps> getUidOps(int uid, int[] ops) throws RemoteException {
            return null;
        }

        public void setUidMode(int code, int uid, int mode) throws RemoteException {
        }

        public void setMode(int code, int uid, String packageName, int mode) throws RemoteException {
        }

        public void resetAllModes(int reqUserId, String reqPackageName) throws RemoteException {
        }

        public void setAudioRestriction(int code, int usage, int uid, int mode, String[] exceptionPackages) throws RemoteException {
        }

        public void setUserRestrictions(Bundle restrictions, IBinder token, int userHandle) throws RemoteException {
        }

        public void setUserRestriction(int code, boolean restricted, IBinder token, int userHandle, String[] exceptionPackages) throws RemoteException {
        }

        public void removeUser(int userHandle) throws RemoteException {
        }

        public int registerCallback(IOpsCallback callback) throws RemoteException {
            return 0;
        }

        public int checkOperationInternal(int code, int uid, String packageName) throws RemoteException {
            return 0;
        }

        public void startWatchingActive(int[] ops, IAppOpsActiveCallback callback) throws RemoteException {
        }

        public void stopWatchingActive(IAppOpsActiveCallback callback) throws RemoteException {
        }

        public boolean isOperationActive(int code, int uid, String packageName) throws RemoteException {
            return false;
        }

        public void startWatchingModeWithFlags(int op, String packageName, int flags, IAppOpsCallback callback) throws RemoteException {
        }

        public void startWatchingNoted(int[] ops, IAppOpsNotedCallback callback) throws RemoteException {
        }

        public void stopWatchingNoted(IAppOpsNotedCallback callback) throws RemoteException {
        }

        public int checkOperationRaw(int code, int uid, String packageName) throws RemoteException {
            return 0;
        }

        public void reloadNonHistoricalState() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAppOpsService {
        private static final String DESCRIPTOR = "com.android.internal.app.IAppOpsService";
        static final int TRANSACTION_addHistoricalOps = 18;
        static final int TRANSACTION_checkAudioOperation = 9;
        static final int TRANSACTION_checkOperation = 1;
        static final int TRANSACTION_checkOperationInternal = 30;
        static final int TRANSACTION_checkOperationRaw = 37;
        static final int TRANSACTION_checkPackage = 11;
        static final int TRANSACTION_clearHistory = 20;
        static final int TRANSACTION_finishOperation = 4;
        static final int TRANSACTION_getHistoricalOps = 14;
        static final int TRANSACTION_getHistoricalOpsFromDiskRaw = 15;
        static final int TRANSACTION_getOpsForPackage = 13;
        static final int TRANSACTION_getPackagesForOps = 12;
        static final int TRANSACTION_getToken = 7;
        static final int TRANSACTION_getUidOps = 21;
        static final int TRANSACTION_isOperationActive = 33;
        static final int TRANSACTION_noteOperation = 2;
        static final int TRANSACTION_noteProxyOperation = 10;
        static final int TRANSACTION_offsetHistory = 16;
        static final int TRANSACTION_permissionToOpCode = 8;
        static final int TRANSACTION_registerCallback = 29;
        static final int TRANSACTION_reloadNonHistoricalState = 38;
        static final int TRANSACTION_removeUser = 28;
        static final int TRANSACTION_resetAllModes = 24;
        static final int TRANSACTION_resetHistoryParameters = 19;
        static final int TRANSACTION_setAudioRestriction = 25;
        static final int TRANSACTION_setHistoryParameters = 17;
        static final int TRANSACTION_setMode = 23;
        static final int TRANSACTION_setUidMode = 22;
        static final int TRANSACTION_setUserRestriction = 27;
        static final int TRANSACTION_setUserRestrictions = 26;
        static final int TRANSACTION_startOperation = 3;
        static final int TRANSACTION_startWatchingActive = 31;
        static final int TRANSACTION_startWatchingMode = 5;
        static final int TRANSACTION_startWatchingModeWithFlags = 34;
        static final int TRANSACTION_startWatchingNoted = 35;
        static final int TRANSACTION_stopWatchingActive = 32;
        static final int TRANSACTION_stopWatchingMode = 6;
        static final int TRANSACTION_stopWatchingNoted = 36;

        private static class Proxy implements IAppOpsService {
            public static IAppOpsService sDefaultImpl;
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

            public int checkOperation(int code, int uid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkOperation(code, uid, packageName);
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

            public int noteOperation(int code, int uid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    int i = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().noteOperation(code, uid, packageName);
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

            public int startOperation(IBinder token, int code, int uid, String packageName, boolean startIfModeDefault) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(code);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeInt(startIfModeDefault ? 1 : 0);
                    int i = this.mRemote;
                    if (!i.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().startOperation(token, code, uid, packageName, startIfModeDefault);
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

            public void finishOperation(IBinder token, int code, int uid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(code);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishOperation(token, code, uid, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startWatchingMode(int op, String packageName, IAppOpsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(op);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startWatchingMode(op, packageName, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopWatchingMode(IAppOpsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopWatchingMode(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder getToken(IBinder clientToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clientToken);
                    IBinder iBinder = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        iBinder = Stub.getDefaultImpl();
                        if (iBinder != 0) {
                            iBinder = Stub.getDefaultImpl().getToken(clientToken);
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

            public int permissionToOpCode(String permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    int i = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().permissionToOpCode(permission);
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

            public int checkAudioOperation(int code, int usage, int uid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeInt(usage);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    int i = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkAudioOperation(code, usage, uid, packageName);
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

            public int noteProxyOperation(int code, int proxyUid, String proxyPackageName, int callingUid, String callingPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeInt(proxyUid);
                    _data.writeString(proxyPackageName);
                    _data.writeInt(callingUid);
                    _data.writeString(callingPackageName);
                    int i = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().noteProxyOperation(code, proxyUid, proxyPackageName, callingUid, callingPackageName);
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

            public int checkPackage(int uid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    int i = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkPackage(uid, packageName);
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

            public List<PackageOps> getPackagesForOps(int[] ops) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(ops);
                    List<PackageOps> list = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPackagesForOps(ops);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PackageOps.CREATOR);
                    List<PackageOps> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeIntArray(ops);
                    List<PackageOps> list = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getOpsForPackage(uid, packageName, ops);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PackageOps.CREATOR);
                    List<PackageOps> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getHistoricalOps(int uid, String packageName, List<String> ops, long beginTimeMillis, long endTimeMillis, int flags, RemoteCallback callback) throws RemoteException {
                Throwable th;
                String str;
                RemoteCallback remoteCallback = callback;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(uid);
                        try {
                            _data.writeString(packageName);
                            _data.writeStringList(ops);
                            _data.writeLong(beginTimeMillis);
                            _data.writeLong(endTimeMillis);
                            _data.writeInt(flags);
                            if (remoteCallback != null) {
                                _data.writeInt(1);
                                remoteCallback.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().getHistoricalOps(uid, packageName, ops, beginTimeMillis, endTimeMillis, flags, callback);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str = packageName;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i = uid;
                    str = packageName;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void getHistoricalOpsFromDiskRaw(int uid, String packageName, List<String> ops, long beginTimeMillis, long endTimeMillis, int flags, RemoteCallback callback) throws RemoteException {
                Throwable th;
                String str;
                RemoteCallback remoteCallback = callback;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(uid);
                        try {
                            _data.writeString(packageName);
                            _data.writeStringList(ops);
                            _data.writeLong(beginTimeMillis);
                            _data.writeLong(endTimeMillis);
                            _data.writeInt(flags);
                            if (remoteCallback != null) {
                                _data.writeInt(1);
                                remoteCallback.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().getHistoricalOpsFromDiskRaw(uid, packageName, ops, beginTimeMillis, endTimeMillis, flags, callback);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str = packageName;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i = uid;
                    str = packageName;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void offsetHistory(long duration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(duration);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().offsetHistory(duration);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setHistoryParameters(int mode, long baseSnapshotInterval, int compressionStep) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeLong(baseSnapshotInterval);
                    _data.writeInt(compressionStep);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setHistoryParameters(mode, baseSnapshotInterval, compressionStep);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addHistoricalOps(HistoricalOps ops) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ops != null) {
                        _data.writeInt(1);
                        ops.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addHistoricalOps(ops);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resetHistoryParameters() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resetHistoryParameters();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearHistory() throws RemoteException {
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
                    Stub.getDefaultImpl().clearHistory();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PackageOps> getUidOps(int uid, int[] ops) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeIntArray(ops);
                    List<PackageOps> list = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getUidOps(uid, ops);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PackageOps.CREATOR);
                    List<PackageOps> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUidMode(int code, int uid, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeInt(uid);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUidMode(code, uid, mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMode(int code, int uid, String packageName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMode(code, uid, packageName, mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resetAllModes(int reqUserId, String reqPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reqUserId);
                    _data.writeString(reqPackageName);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resetAllModes(reqUserId, reqPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAudioRestriction(int code, int usage, int uid, int mode, String[] exceptionPackages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeInt(usage);
                    _data.writeInt(uid);
                    _data.writeInt(mode);
                    _data.writeStringArray(exceptionPackages);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAudioRestriction(code, usage, uid, mode, exceptionPackages);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUserRestrictions(Bundle restrictions, IBinder token, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (restrictions != null) {
                        _data.writeInt(1);
                        restrictions.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(token);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserRestrictions(restrictions, token, userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUserRestriction(int code, boolean restricted, IBinder token, int userHandle, String[] exceptionPackages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeInt(restricted ? 1 : 0);
                    _data.writeStrongBinder(token);
                    _data.writeInt(userHandle);
                    _data.writeStringArray(exceptionPackages);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserRestriction(code, restricted, token, userHandle, exceptionPackages);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeUser(userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int registerCallback(IOpsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    int i = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().registerCallback(callback);
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

            public int checkOperationInternal(int code, int uid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    int i = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkOperationInternal(code, uid, packageName);
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

            public void startWatchingActive(int[] ops, IAppOpsActiveCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(ops);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startWatchingActive(ops, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopWatchingActive(IAppOpsActiveCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopWatchingActive(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isOperationActive(int code, int uid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isOperationActive(code, uid, packageName);
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

            public void startWatchingModeWithFlags(int op, String packageName, int flags, IAppOpsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(op);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startWatchingModeWithFlags(op, packageName, flags, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startWatchingNoted(int[] ops, IAppOpsNotedCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(ops);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startWatchingNoted(ops, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopWatchingNoted(IAppOpsNotedCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopWatchingNoted(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkOperationRaw(int code, int uid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    int i = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkOperationRaw(code, uid, packageName);
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

            public void reloadNonHistoricalState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reloadNonHistoricalState();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAppOpsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAppOpsService)) {
                return new Proxy(obj);
            }
            return (IAppOpsService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "checkOperation";
                case 2:
                    return "noteOperation";
                case 3:
                    return "startOperation";
                case 4:
                    return "finishOperation";
                case 5:
                    return "startWatchingMode";
                case 6:
                    return "stopWatchingMode";
                case 7:
                    return "getToken";
                case 8:
                    return "permissionToOpCode";
                case 9:
                    return "checkAudioOperation";
                case 10:
                    return "noteProxyOperation";
                case 11:
                    return "checkPackage";
                case 12:
                    return "getPackagesForOps";
                case 13:
                    return "getOpsForPackage";
                case 14:
                    return "getHistoricalOps";
                case 15:
                    return "getHistoricalOpsFromDiskRaw";
                case 16:
                    return "offsetHistory";
                case 17:
                    return "setHistoryParameters";
                case 18:
                    return "addHistoricalOps";
                case 19:
                    return "resetHistoryParameters";
                case 20:
                    return "clearHistory";
                case 21:
                    return "getUidOps";
                case 22:
                    return "setUidMode";
                case 23:
                    return "setMode";
                case 24:
                    return "resetAllModes";
                case 25:
                    return "setAudioRestriction";
                case 26:
                    return "setUserRestrictions";
                case 27:
                    return "setUserRestriction";
                case 28:
                    return "removeUser";
                case 29:
                    return "registerCallback";
                case 30:
                    return "checkOperationInternal";
                case 31:
                    return "startWatchingActive";
                case 32:
                    return "stopWatchingActive";
                case 33:
                    return "isOperationActive";
                case 34:
                    return "startWatchingModeWithFlags";
                case 35:
                    return "startWatchingNoted";
                case 36:
                    return "stopWatchingNoted";
                case 37:
                    return "checkOperationRaw";
                case 38:
                    return "reloadNonHistoricalState";
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
                int _result;
                int _result2;
                int _result3;
                int _arg0;
                String _arg1;
                List<String> _arg2;
                long _arg3;
                long _arg4;
                int _arg5;
                RemoteCallback _arg6;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _result = checkOperation(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _result = noteOperation(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _result2 = startOperation(data.readStrongBinder(), data.readInt(), data.readInt(), data.readString(), data.readInt() != 0);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        finishOperation(data.readStrongBinder(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        startWatchingMode(data.readInt(), data.readString(), com.android.internal.app.IAppOpsCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        stopWatchingMode(com.android.internal.app.IAppOpsCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        IBinder _result4 = getToken(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result4);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _result3 = permissionToOpCode(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        int _result5 = checkAudioOperation(data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _result2 = noteProxyOperation(data.readInt(), data.readInt(), data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        int _result6 = checkPackage(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        List<PackageOps> _result7 = getPackagesForOps(data.createIntArray());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result7);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        List<PackageOps> _result8 = getOpsForPackage(data.readInt(), data.readString(), data.createIntArray());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result8);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readString();
                        _arg2 = data.createStringArrayList();
                        _arg3 = data.readLong();
                        _arg4 = data.readLong();
                        _arg5 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg6 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        getHistoricalOps(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readString();
                        _arg2 = data.createStringArrayList();
                        _arg3 = data.readLong();
                        _arg4 = data.readLong();
                        _arg5 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg6 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        getHistoricalOpsFromDiskRaw(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        offsetHistory(data.readLong());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        setHistoryParameters(data.readInt(), data.readLong(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 18:
                        HistoricalOps _arg02;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (HistoricalOps) HistoricalOps.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        addHistoricalOps(_arg02);
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        resetHistoryParameters();
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        clearHistory();
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        List<PackageOps> _result9 = getUidOps(data.readInt(), data.createIntArray());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result9);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        setUidMode(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        setMode(data.readInt(), data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        resetAllModes(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        setAudioRestriction(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 26:
                        Bundle _arg03;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        setUserRestrictions(_arg03, data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        setUserRestriction(data.readInt(), data.readInt() != 0, data.readStrongBinder(), data.readInt(), data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        removeUser(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        _result3 = registerCallback(com.android.internal.app.IOpsCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        _result = checkOperationInternal(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        startWatchingActive(data.createIntArray(), com.android.internal.app.IAppOpsActiveCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        stopWatchingActive(com.android.internal.app.IAppOpsActiveCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        boolean _result10 = isOperationActive(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        startWatchingModeWithFlags(data.readInt(), data.readString(), data.readInt(), com.android.internal.app.IAppOpsCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        startWatchingNoted(data.createIntArray(), com.android.internal.app.IAppOpsNotedCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        stopWatchingNoted(com.android.internal.app.IAppOpsNotedCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        _result = checkOperationRaw(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        reloadNonHistoricalState();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IAppOpsService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAppOpsService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addHistoricalOps(HistoricalOps historicalOps) throws RemoteException;

    int checkAudioOperation(int i, int i2, int i3, String str) throws RemoteException;

    int checkOperation(int i, int i2, String str) throws RemoteException;

    int checkOperationInternal(int i, int i2, String str) throws RemoteException;

    int checkOperationRaw(int i, int i2, String str) throws RemoteException;

    int checkPackage(int i, String str) throws RemoteException;

    void clearHistory() throws RemoteException;

    @UnsupportedAppUsage
    void finishOperation(IBinder iBinder, int i, int i2, String str) throws RemoteException;

    void getHistoricalOps(int i, String str, List<String> list, long j, long j2, int i2, RemoteCallback remoteCallback) throws RemoteException;

    void getHistoricalOpsFromDiskRaw(int i, String str, List<String> list, long j, long j2, int i2, RemoteCallback remoteCallback) throws RemoteException;

    @UnsupportedAppUsage
    List<PackageOps> getOpsForPackage(int i, String str, int[] iArr) throws RemoteException;

    @UnsupportedAppUsage
    List<PackageOps> getPackagesForOps(int[] iArr) throws RemoteException;

    IBinder getToken(IBinder iBinder) throws RemoteException;

    List<PackageOps> getUidOps(int i, int[] iArr) throws RemoteException;

    boolean isOperationActive(int i, int i2, String str) throws RemoteException;

    int noteOperation(int i, int i2, String str) throws RemoteException;

    int noteProxyOperation(int i, int i2, String str, int i3, String str2) throws RemoteException;

    void offsetHistory(long j) throws RemoteException;

    int permissionToOpCode(String str) throws RemoteException;

    int registerCallback(IOpsCallback iOpsCallback) throws RemoteException;

    void reloadNonHistoricalState() throws RemoteException;

    void removeUser(int i) throws RemoteException;

    @UnsupportedAppUsage
    void resetAllModes(int i, String str) throws RemoteException;

    void resetHistoryParameters() throws RemoteException;

    void setAudioRestriction(int i, int i2, int i3, int i4, String[] strArr) throws RemoteException;

    void setHistoryParameters(int i, long j, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void setMode(int i, int i2, String str, int i3) throws RemoteException;

    void setUidMode(int i, int i2, int i3) throws RemoteException;

    void setUserRestriction(int i, boolean z, IBinder iBinder, int i2, String[] strArr) throws RemoteException;

    void setUserRestrictions(Bundle bundle, IBinder iBinder, int i) throws RemoteException;

    int startOperation(IBinder iBinder, int i, int i2, String str, boolean z) throws RemoteException;

    void startWatchingActive(int[] iArr, IAppOpsActiveCallback iAppOpsActiveCallback) throws RemoteException;

    void startWatchingMode(int i, String str, IAppOpsCallback iAppOpsCallback) throws RemoteException;

    void startWatchingModeWithFlags(int i, String str, int i2, IAppOpsCallback iAppOpsCallback) throws RemoteException;

    void startWatchingNoted(int[] iArr, IAppOpsNotedCallback iAppOpsNotedCallback) throws RemoteException;

    void stopWatchingActive(IAppOpsActiveCallback iAppOpsActiveCallback) throws RemoteException;

    void stopWatchingMode(IAppOpsCallback iAppOpsCallback) throws RemoteException;

    void stopWatchingNoted(IAppOpsNotedCallback iAppOpsNotedCallback) throws RemoteException;
}
