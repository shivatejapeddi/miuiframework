package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRemoteDisplayProvider extends IInterface {

    public static class Default implements IRemoteDisplayProvider {
        public void setCallback(IRemoteDisplayCallback callback) throws RemoteException {
        }

        public void setDiscoveryMode(int mode) throws RemoteException {
        }

        public void connect(String id) throws RemoteException {
        }

        public void disconnect(String id) throws RemoteException {
        }

        public void setVolume(String id, int volume) throws RemoteException {
        }

        public void adjustVolume(String id, int delta) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRemoteDisplayProvider {
        private static final String DESCRIPTOR = "android.media.IRemoteDisplayProvider";
        static final int TRANSACTION_adjustVolume = 6;
        static final int TRANSACTION_connect = 3;
        static final int TRANSACTION_disconnect = 4;
        static final int TRANSACTION_setCallback = 1;
        static final int TRANSACTION_setDiscoveryMode = 2;
        static final int TRANSACTION_setVolume = 5;

        private static class Proxy implements IRemoteDisplayProvider {
            public static IRemoteDisplayProvider sDefaultImpl;
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

            public void setCallback(IRemoteDisplayCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setCallback(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setDiscoveryMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setDiscoveryMode(mode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void connect(String id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().connect(id);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void disconnect(String id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().disconnect(id);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setVolume(String id, int volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    _data.writeInt(volume);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setVolume(id, volume);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void adjustVolume(String id, int delta) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    _data.writeInt(delta);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().adjustVolume(id, delta);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRemoteDisplayProvider asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRemoteDisplayProvider)) {
                return new Proxy(obj);
            }
            return (IRemoteDisplayProvider) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setCallback";
                case 2:
                    return "setDiscoveryMode";
                case 3:
                    return "connect";
                case 4:
                    return "disconnect";
                case 5:
                    return "setVolume";
                case 6:
                    return "adjustVolume";
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
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        setCallback(android.media.IRemoteDisplayCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        setDiscoveryMode(data.readInt());
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        connect(data.readString());
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        disconnect(data.readString());
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        setVolume(data.readString(), data.readInt());
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        adjustVolume(data.readString(), data.readInt());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IRemoteDisplayProvider impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRemoteDisplayProvider getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void adjustVolume(String str, int i) throws RemoteException;

    void connect(String str) throws RemoteException;

    void disconnect(String str) throws RemoteException;

    void setCallback(IRemoteDisplayCallback iRemoteDisplayCallback) throws RemoteException;

    void setDiscoveryMode(int i) throws RemoteException;

    void setVolume(String str, int i) throws RemoteException;
}
