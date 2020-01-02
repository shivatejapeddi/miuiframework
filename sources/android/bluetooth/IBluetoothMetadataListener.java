package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IBluetoothMetadataListener extends IInterface {

    public static abstract class Stub extends Binder implements IBluetoothMetadataListener {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothMetadataListener";
        static final int TRANSACTION_onMetadataChanged = 1;

        private static class Proxy implements IBluetoothMetadataListener {
            public static IBluetoothMetadataListener sDefaultImpl;
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

            public void onMetadataChanged(BluetoothDevice devices, int key, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (devices != null) {
                        _data.writeInt(1);
                        devices.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(key);
                    _data.writeByteArray(value);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onMetadataChanged(devices, key, value);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothMetadataListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothMetadataListener)) {
                return new Proxy(obj);
            }
            return (IBluetoothMetadataListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onMetadataChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                BluetoothDevice _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onMetadataChanged(_arg0, data.readInt(), data.createByteArray());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IBluetoothMetadataListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetoothMetadataListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IBluetoothMetadataListener {
        public void onMetadataChanged(BluetoothDevice devices, int key, byte[] value) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onMetadataChanged(BluetoothDevice bluetoothDevice, int i, byte[] bArr) throws RemoteException;
}
