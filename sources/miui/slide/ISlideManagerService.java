package miui.slide;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISlideManagerService extends IInterface {

    public static class Default implements ISlideManagerService {
        public AppSlideConfig getAppSlideConfig(String packageName, int versionCode) throws RemoteException {
            return null;
        }

        public int getCameraStatus() throws RemoteException {
            return 0;
        }

        public void registerSlideChangeListener(String identity, ISlideChangeListener listener) throws RemoteException {
        }

        public void unregisterSlideChangeListener(ISlideChangeListener listener) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISlideManagerService {
        private static final String DESCRIPTOR = "miui.slide.ISlideManagerService";
        static final int TRANSACTION_getAppSlideConfig = 1;
        static final int TRANSACTION_getCameraStatus = 2;
        static final int TRANSACTION_registerSlideChangeListener = 3;
        static final int TRANSACTION_unregisterSlideChangeListener = 4;

        private static class Proxy implements ISlideManagerService {
            public static ISlideManagerService sDefaultImpl;
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

            public AppSlideConfig getAppSlideConfig(String packageName, int versionCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(versionCode);
                    AppSlideConfig appSlideConfig = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        appSlideConfig = Stub.getDefaultImpl();
                        if (appSlideConfig != 0) {
                            appSlideConfig = Stub.getDefaultImpl().getAppSlideConfig(packageName, versionCode);
                            return appSlideConfig;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        appSlideConfig = (AppSlideConfig) AppSlideConfig.CREATOR.createFromParcel(_reply);
                    } else {
                        appSlideConfig = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return appSlideConfig;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCameraStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCameraStatus();
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

            public void registerSlideChangeListener(String identity, ISlideChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(identity);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerSlideChangeListener(identity, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterSlideChangeListener(ISlideChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterSlideChangeListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISlideManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISlideManagerService)) {
                return new Proxy(obj);
            }
            return (ISlideManagerService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getAppSlideConfig";
            }
            if (transactionCode == 2) {
                return "getCameraStatus";
            }
            if (transactionCode == 3) {
                return "registerSlideChangeListener";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "unregisterSlideChangeListener";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                AppSlideConfig _result = getAppSlideConfig(data.readString(), data.readInt());
                reply.writeNoException();
                if (_result != null) {
                    reply.writeInt(1);
                    _result.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                int _result2 = getCameraStatus();
                reply.writeNoException();
                reply.writeInt(_result2);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                registerSlideChangeListener(data.readString(), miui.slide.ISlideChangeListener.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                unregisterSlideChangeListener(miui.slide.ISlideChangeListener.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ISlideManagerService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISlideManagerService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    AppSlideConfig getAppSlideConfig(String str, int i) throws RemoteException;

    int getCameraStatus() throws RemoteException;

    void registerSlideChangeListener(String str, ISlideChangeListener iSlideChangeListener) throws RemoteException;

    void unregisterSlideChangeListener(ISlideChangeListener iSlideChangeListener) throws RemoteException;
}
