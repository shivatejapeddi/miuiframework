package miui.app.backup;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPackageBackupRestoreObserver extends IInterface {

    public static abstract class Stub extends Binder implements IPackageBackupRestoreObserver {
        private static final String DESCRIPTOR = "miui.app.backup.IPackageBackupRestoreObserver";
        static final int TRANSACTION_onBackupEnd = 2;
        static final int TRANSACTION_onBackupStart = 1;
        static final int TRANSACTION_onCustomProgressChange = 7;
        static final int TRANSACTION_onError = 6;
        static final int TRANSACTION_onRestoreEnd = 4;
        static final int TRANSACTION_onRestoreError = 5;
        static final int TRANSACTION_onRestoreStart = 3;

        private static class Proxy implements IPackageBackupRestoreObserver {
            public static IPackageBackupRestoreObserver sDefaultImpl;
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

            public void onBackupStart(String pkg, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(feature);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onBackupStart(pkg, feature);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onBackupEnd(String pkg, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(feature);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onBackupEnd(pkg, feature);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRestoreStart(String pkg, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(feature);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRestoreStart(pkg, feature);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRestoreEnd(String pkg, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(feature);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRestoreEnd(pkg, feature);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRestoreError(String pkg, int feature, int err) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(feature);
                    _data.writeInt(err);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRestoreError(pkg, feature, err);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onError(String pkg, int feature, int err) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(feature);
                    _data.writeInt(err);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onError(pkg, feature, err);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCustomProgressChange(String pkg, int feature, int progType, long prog, long total) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                long j;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(pkg);
                    } catch (Throwable th2) {
                        th = th2;
                        i = feature;
                        i2 = progType;
                        j = prog;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(feature);
                        try {
                            _data.writeInt(progType);
                        } catch (Throwable th3) {
                            th = th3;
                            j = prog;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeLong(prog);
                            _data.writeLong(total);
                            try {
                                if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().onCustomProgressChange(pkg, feature, progType, prog, total);
                                _data.recycle();
                            } catch (Throwable th4) {
                                th = th4;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i2 = progType;
                        j = prog;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str = pkg;
                    i = feature;
                    i2 = progType;
                    j = prog;
                    _data.recycle();
                    throw th;
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPackageBackupRestoreObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPackageBackupRestoreObserver)) {
                return new Proxy(obj);
            }
            return (IPackageBackupRestoreObserver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onBackupStart";
                case 2:
                    return "onBackupEnd";
                case 3:
                    return "onRestoreStart";
                case 4:
                    return "onRestoreEnd";
                case 5:
                    return "onRestoreError";
                case 6:
                    return "onError";
                case 7:
                    return "onCustomProgressChange";
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
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        onBackupStart(data.readString(), data.readInt());
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        onBackupEnd(data.readString(), data.readInt());
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        onRestoreStart(data.readString(), data.readInt());
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        onRestoreEnd(data.readString(), data.readInt());
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        onRestoreError(data.readString(), data.readInt(), data.readInt());
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        onError(data.readString(), data.readInt(), data.readInt());
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        onCustomProgressChange(data.readString(), data.readInt(), data.readInt(), data.readLong(), data.readLong());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPackageBackupRestoreObserver impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPackageBackupRestoreObserver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IPackageBackupRestoreObserver {
        public void onBackupStart(String pkg, int feature) throws RemoteException {
        }

        public void onBackupEnd(String pkg, int feature) throws RemoteException {
        }

        public void onRestoreStart(String pkg, int feature) throws RemoteException {
        }

        public void onRestoreEnd(String pkg, int feature) throws RemoteException {
        }

        public void onRestoreError(String pkg, int feature, int err) throws RemoteException {
        }

        public void onError(String pkg, int feature, int err) throws RemoteException {
        }

        public void onCustomProgressChange(String pkg, int feature, int progType, long prog, long total) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onBackupEnd(String str, int i) throws RemoteException;

    void onBackupStart(String str, int i) throws RemoteException;

    void onCustomProgressChange(String str, int i, int i2, long j, long j2) throws RemoteException;

    void onError(String str, int i, int i2) throws RemoteException;

    void onRestoreEnd(String str, int i) throws RemoteException;

    void onRestoreError(String str, int i, int i2) throws RemoteException;

    void onRestoreStart(String str, int i) throws RemoteException;
}
