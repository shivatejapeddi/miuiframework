package android.net.wifi.p2p;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWifiP2pManager extends IInterface {

    public static class Default implements IWifiP2pManager {
        public Messenger getMessenger(IBinder binder) throws RemoteException {
            return null;
        }

        public Messenger getP2pStateMachineMessenger() throws RemoteException {
            return null;
        }

        public void close(IBinder binder) throws RemoteException {
        }

        public void setMiracastMode(int mode) throws RemoteException {
        }

        public void checkConfigureWifiDisplayPermission() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWifiP2pManager {
        private static final String DESCRIPTOR = "android.net.wifi.p2p.IWifiP2pManager";
        static final int TRANSACTION_checkConfigureWifiDisplayPermission = 5;
        static final int TRANSACTION_close = 3;
        static final int TRANSACTION_getMessenger = 1;
        static final int TRANSACTION_getP2pStateMachineMessenger = 2;
        static final int TRANSACTION_setMiracastMode = 4;

        private static class Proxy implements IWifiP2pManager {
            public static IWifiP2pManager sDefaultImpl;
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

            public Messenger getMessenger(IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    Messenger messenger = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        messenger = Stub.getDefaultImpl();
                        if (messenger != 0) {
                            messenger = Stub.getDefaultImpl().getMessenger(binder);
                            return messenger;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        messenger = (Messenger) Messenger.CREATOR.createFromParcel(_reply);
                    } else {
                        messenger = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return messenger;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Messenger getP2pStateMachineMessenger() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Messenger messenger = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        messenger = Stub.getDefaultImpl();
                        if (messenger != 0) {
                            messenger = Stub.getDefaultImpl().getP2pStateMachineMessenger();
                            return messenger;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        messenger = (Messenger) Messenger.CREATOR.createFromParcel(_reply);
                    } else {
                        messenger = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return messenger;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void close(IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().close(binder);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setMiracastMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMiracastMode(mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void checkConfigureWifiDisplayPermission() throws RemoteException {
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
                    Stub.getDefaultImpl().checkConfigureWifiDisplayPermission();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiP2pManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWifiP2pManager)) {
                return new Proxy(obj);
            }
            return (IWifiP2pManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getMessenger";
            }
            if (transactionCode == 2) {
                return "getP2pStateMachineMessenger";
            }
            if (transactionCode == 3) {
                return "close";
            }
            if (transactionCode == 4) {
                return "setMiracastMode";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "checkConfigureWifiDisplayPermission";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                Messenger _result = getMessenger(data.readStrongBinder());
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
                Messenger _result2 = getP2pStateMachineMessenger();
                reply.writeNoException();
                if (_result2 != null) {
                    reply.writeInt(1);
                    _result2.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                close(data.readStrongBinder());
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                setMiracastMode(data.readInt());
                reply.writeNoException();
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                checkConfigureWifiDisplayPermission();
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IWifiP2pManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWifiP2pManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void checkConfigureWifiDisplayPermission() throws RemoteException;

    void close(IBinder iBinder) throws RemoteException;

    Messenger getMessenger(IBinder iBinder) throws RemoteException;

    Messenger getP2pStateMachineMessenger() throws RemoteException;

    void setMiracastMode(int i) throws RemoteException;
}
