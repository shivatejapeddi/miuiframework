package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IBluetoothManagerCallback extends IInterface {

    public static abstract class Stub extends Binder implements IBluetoothManagerCallback {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothManagerCallback";
        static final int TRANSACTION_onBluetoothServiceDown = 2;
        static final int TRANSACTION_onBluetoothServiceUp = 1;
        static final int TRANSACTION_onBrEdrDown = 3;

        private static class Proxy implements IBluetoothManagerCallback {
            public static IBluetoothManagerCallback sDefaultImpl;
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

            public void onBluetoothServiceUp(IBluetooth bluetoothService) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(bluetoothService != null ? bluetoothService.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onBluetoothServiceUp(bluetoothService);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onBluetoothServiceDown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onBluetoothServiceDown();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onBrEdrDown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onBrEdrDown();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothManagerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothManagerCallback)) {
                return new Proxy(obj);
            }
            return (IBluetoothManagerCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onBluetoothServiceUp";
            }
            if (transactionCode == 2) {
                return "onBluetoothServiceDown";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "onBrEdrDown";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onBluetoothServiceUp(android.bluetooth.IBluetooth.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onBluetoothServiceDown();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onBrEdrDown();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IBluetoothManagerCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetoothManagerCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IBluetoothManagerCallback {
        public void onBluetoothServiceUp(IBluetooth bluetoothService) throws RemoteException {
        }

        public void onBluetoothServiceDown() throws RemoteException {
        }

        public void onBrEdrDown() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onBluetoothServiceDown() throws RemoteException;

    void onBluetoothServiceUp(IBluetooth iBluetooth) throws RemoteException;

    void onBrEdrDown() throws RemoteException;
}
