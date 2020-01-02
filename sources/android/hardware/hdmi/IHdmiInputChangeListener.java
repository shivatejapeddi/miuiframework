package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IHdmiInputChangeListener extends IInterface {

    public static abstract class Stub extends Binder implements IHdmiInputChangeListener {
        private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiInputChangeListener";
        static final int TRANSACTION_onChanged = 1;

        private static class Proxy implements IHdmiInputChangeListener {
            public static IHdmiInputChangeListener sDefaultImpl;
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

            public void onChanged(HdmiDeviceInfo device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onChanged(device);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IHdmiInputChangeListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IHdmiInputChangeListener)) {
                return new Proxy(obj);
            }
            return (IHdmiInputChangeListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                HdmiDeviceInfo _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (HdmiDeviceInfo) HdmiDeviceInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onChanged(_arg0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IHdmiInputChangeListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IHdmiInputChangeListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IHdmiInputChangeListener {
        public void onChanged(HdmiDeviceInfo device) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onChanged(HdmiDeviceInfo hdmiDeviceInfo) throws RemoteException;
}
