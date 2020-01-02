package com.miui.whetstone.watermark;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWatermarkCallback extends IInterface {

    public static class Default implements IWatermarkCallback {
        public void onEncodeWatermark(Bitmap bmp) throws RemoteException {
        }

        public void onDecodeWatermark(String watermark) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWatermarkCallback {
        private static final String DESCRIPTOR = "com.miui.whetstone.watermark.IWatermarkCallback";
        static final int TRANSACTION_onDecodeWatermark = 2;
        static final int TRANSACTION_onEncodeWatermark = 1;

        private static class Proxy implements IWatermarkCallback {
            public static IWatermarkCallback sDefaultImpl;
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

            public void onEncodeWatermark(Bitmap bmp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bmp != null) {
                        _data.writeInt(1);
                        bmp.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onEncodeWatermark(bmp);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDecodeWatermark(String watermark) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(watermark);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDecodeWatermark(watermark);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWatermarkCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWatermarkCallback)) {
                return new Proxy(obj);
            }
            return (IWatermarkCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onEncodeWatermark";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "onDecodeWatermark";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                Bitmap _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onEncodeWatermark(_arg0);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onDecodeWatermark(data.readString());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IWatermarkCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWatermarkCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onDecodeWatermark(String str) throws RemoteException;

    void onEncodeWatermark(Bitmap bitmap) throws RemoteException;
}
