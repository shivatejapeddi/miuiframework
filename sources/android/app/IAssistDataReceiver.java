package android.app;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAssistDataReceiver extends IInterface {

    public static class Default implements IAssistDataReceiver {
        public void onHandleAssistData(Bundle resultData) throws RemoteException {
        }

        public void onHandleAssistScreenshot(Bitmap screenshot) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAssistDataReceiver {
        private static final String DESCRIPTOR = "android.app.IAssistDataReceiver";
        static final int TRANSACTION_onHandleAssistData = 1;
        static final int TRANSACTION_onHandleAssistScreenshot = 2;

        private static class Proxy implements IAssistDataReceiver {
            public static IAssistDataReceiver sDefaultImpl;
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

            public void onHandleAssistData(Bundle resultData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (resultData != null) {
                        _data.writeInt(1);
                        resultData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onHandleAssistData(resultData);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onHandleAssistScreenshot(Bitmap screenshot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (screenshot != null) {
                        _data.writeInt(1);
                        screenshot.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onHandleAssistScreenshot(screenshot);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAssistDataReceiver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAssistDataReceiver)) {
                return new Proxy(obj);
            }
            return (IAssistDataReceiver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onHandleAssistData";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "onHandleAssistScreenshot";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                Bundle _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onHandleAssistData(_arg0);
                return true;
            } else if (code == 2) {
                Bitmap _arg02;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                onHandleAssistScreenshot(_arg02);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IAssistDataReceiver impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAssistDataReceiver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    void onHandleAssistData(Bundle bundle) throws RemoteException;

    @UnsupportedAppUsage
    void onHandleAssistScreenshot(Bitmap bitmap) throws RemoteException;
}
