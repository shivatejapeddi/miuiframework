package miui.content.pm;

import android.content.pm.IPackageInstallObserver2;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPreloadedAppManager extends IInterface {

    public static class Default implements IPreloadedAppManager {
        public void reinstallPreloadedApp(String packagName, IPackageInstallObserver2 observer, int flags) throws RemoteException {
        }

        public void reinstallPreloadedApp2(String packagName, IPackageInstallObserver2 observer, int flags) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPreloadedAppManager {
        private static final String DESCRIPTOR = "miui.content.pm.IPreloadedAppManager";
        static final int TRANSACTION_reinstallPreloadedApp = 1;
        static final int TRANSACTION_reinstallPreloadedApp2 = 2;

        private static class Proxy implements IPreloadedAppManager {
            public static IPreloadedAppManager sDefaultImpl;
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

            public void reinstallPreloadedApp(String packagName, IPackageInstallObserver2 observer, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packagName);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reinstallPreloadedApp(packagName, observer, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reinstallPreloadedApp2(String packagName, IPackageInstallObserver2 observer, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packagName);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reinstallPreloadedApp2(packagName, observer, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPreloadedAppManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPreloadedAppManager)) {
                return new Proxy(obj);
            }
            return (IPreloadedAppManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "reinstallPreloadedApp";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "reinstallPreloadedApp2";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                reinstallPreloadedApp(data.readString(), android.content.pm.IPackageInstallObserver2.Stub.asInterface(data.readStrongBinder()), data.readInt());
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                reinstallPreloadedApp2(data.readString(), android.content.pm.IPackageInstallObserver2.Stub.asInterface(data.readStrongBinder()), data.readInt());
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IPreloadedAppManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPreloadedAppManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void reinstallPreloadedApp(String str, IPackageInstallObserver2 iPackageInstallObserver2, int i) throws RemoteException;

    void reinstallPreloadedApp2(String str, IPackageInstallObserver2 iPackageInstallObserver2, int i) throws RemoteException;
}
