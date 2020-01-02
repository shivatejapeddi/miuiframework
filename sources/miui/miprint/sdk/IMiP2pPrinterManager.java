package miui.miprint.sdk;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiP2pPrinterManager extends IInterface {

    public static class Default implements IMiP2pPrinterManager {
        public void registerP2pPrinterListener(IMiP2pPrinterListener listener) throws RemoteException {
        }

        public void unregisterP2pPrinterListener(IMiP2pPrinterListener listener) throws RemoteException {
        }

        public void getP2pPrinters() throws RemoteException {
        }

        public void connectP2pPrinter(WifiP2pDevice device) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiP2pPrinterManager {
        private static final String DESCRIPTOR = "miui.miprint.sdk.IMiP2pPrinterManager";
        static final int TRANSACTION_connectP2pPrinter = 4;
        static final int TRANSACTION_getP2pPrinters = 3;
        static final int TRANSACTION_registerP2pPrinterListener = 1;
        static final int TRANSACTION_unregisterP2pPrinterListener = 2;

        private static class Proxy implements IMiP2pPrinterManager {
            public static IMiP2pPrinterManager sDefaultImpl;
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

            public void registerP2pPrinterListener(IMiP2pPrinterListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerP2pPrinterListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterP2pPrinterListener(IMiP2pPrinterListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterP2pPrinterListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getP2pPrinters() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getP2pPrinters();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void connectP2pPrinter(WifiP2pDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().connectP2pPrinter(device);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiP2pPrinterManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiP2pPrinterManager)) {
                return new Proxy(obj);
            }
            return (IMiP2pPrinterManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "registerP2pPrinterListener";
            }
            if (transactionCode == 2) {
                return "unregisterP2pPrinterListener";
            }
            if (transactionCode == 3) {
                return "getP2pPrinters";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "connectP2pPrinter";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                registerP2pPrinterListener(miui.miprint.sdk.IMiP2pPrinterListener.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                unregisterP2pPrinterListener(miui.miprint.sdk.IMiP2pPrinterListener.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                getP2pPrinters();
                reply.writeNoException();
                return true;
            } else if (code == 4) {
                WifiP2pDevice _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (WifiP2pDevice) WifiP2pDevice.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                connectP2pPrinter(_arg0);
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMiP2pPrinterManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiP2pPrinterManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void connectP2pPrinter(WifiP2pDevice wifiP2pDevice) throws RemoteException;

    void getP2pPrinters() throws RemoteException;

    void registerP2pPrinterListener(IMiP2pPrinterListener iMiP2pPrinterListener) throws RemoteException;

    void unregisterP2pPrinterListener(IMiP2pPrinterListener iMiP2pPrinterListener) throws RemoteException;
}
