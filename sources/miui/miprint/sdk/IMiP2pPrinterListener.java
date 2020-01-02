package miui.miprint.sdk;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiP2pPrinterListener extends IInterface {

    public static class Default implements IMiP2pPrinterListener {
        public void onP2pPrinterChange(P2pPrinterInfo printer, int state) throws RemoteException {
        }

        public void onPrinterConnectionComplete(P2pPrinterInfo printer, int state) throws RemoteException {
        }

        public void onPrinterConnectionDelay(WifiP2pDevice device, int delayed) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiP2pPrinterListener {
        private static final String DESCRIPTOR = "miui.miprint.sdk.IMiP2pPrinterListener";
        static final int TRANSACTION_onP2pPrinterChange = 1;
        static final int TRANSACTION_onPrinterConnectionComplete = 2;
        static final int TRANSACTION_onPrinterConnectionDelay = 3;

        private static class Proxy implements IMiP2pPrinterListener {
            public static IMiP2pPrinterListener sDefaultImpl;
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

            public void onP2pPrinterChange(P2pPrinterInfo printer, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printer != null) {
                        _data.writeInt(1);
                        printer.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onP2pPrinterChange(printer, state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onPrinterConnectionComplete(P2pPrinterInfo printer, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printer != null) {
                        _data.writeInt(1);
                        printer.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onPrinterConnectionComplete(printer, state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onPrinterConnectionDelay(WifiP2pDevice device, int delayed) throws RemoteException {
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
                    _data.writeInt(delayed);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onPrinterConnectionDelay(device, delayed);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiP2pPrinterListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiP2pPrinterListener)) {
                return new Proxy(obj);
            }
            return (IMiP2pPrinterListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onP2pPrinterChange";
            }
            if (transactionCode == 2) {
                return "onPrinterConnectionComplete";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "onPrinterConnectionDelay";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            P2pPrinterInfo _arg0;
            if (code == 1) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (P2pPrinterInfo) P2pPrinterInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onP2pPrinterChange(_arg0, data.readInt());
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (P2pPrinterInfo) P2pPrinterInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onPrinterConnectionComplete(_arg0, data.readInt());
                reply.writeNoException();
                return true;
            } else if (code == 3) {
                WifiP2pDevice _arg02;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (WifiP2pDevice) WifiP2pDevice.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                onPrinterConnectionDelay(_arg02, data.readInt());
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMiP2pPrinterListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiP2pPrinterListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onP2pPrinterChange(P2pPrinterInfo p2pPrinterInfo, int i) throws RemoteException;

    void onPrinterConnectionComplete(P2pPrinterInfo p2pPrinterInfo, int i) throws RemoteException;

    void onPrinterConnectionDelay(WifiP2pDevice wifiP2pDevice, int i) throws RemoteException;
}
