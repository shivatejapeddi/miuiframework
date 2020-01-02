package com.miui.mishare;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiShareDiscoverCallback extends IInterface {

    public static class Default implements IMiShareDiscoverCallback {
        public void onDeviceUpdated(RemoteDevice device) throws RemoteException {
        }

        public void onDeviceLost(String deviceId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiShareDiscoverCallback {
        private static final String DESCRIPTOR = "com.miui.mishare.IMiShareDiscoverCallback";
        static final int TRANSACTION_onDeviceLost = 2;
        static final int TRANSACTION_onDeviceUpdated = 1;

        private static class Proxy implements IMiShareDiscoverCallback {
            public static IMiShareDiscoverCallback sDefaultImpl;
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

            public void onDeviceUpdated(RemoteDevice device) throws RemoteException {
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
                        Stub.getDefaultImpl().onDeviceUpdated(device);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDeviceLost(String deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(deviceId);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDeviceLost(deviceId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiShareDiscoverCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiShareDiscoverCallback)) {
                return new Proxy(obj);
            }
            return (IMiShareDiscoverCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onDeviceUpdated";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "onDeviceLost";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                RemoteDevice _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (RemoteDevice) RemoteDevice.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onDeviceUpdated(_arg0);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onDeviceLost(data.readString());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMiShareDiscoverCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiShareDiscoverCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onDeviceLost(String str) throws RemoteException;

    void onDeviceUpdated(RemoteDevice remoteDevice) throws RemoteException;
}
