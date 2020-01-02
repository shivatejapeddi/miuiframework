package miui.process;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IForegroundInfoListener extends IInterface {

    public static class Default implements IForegroundInfoListener {
        public void onForegroundInfoChanged(ForegroundInfo foregroundInfo) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IForegroundInfoListener {
        private static final String DESCRIPTOR = "miui.process.IForegroundInfoListener";
        static final int TRANSACTION_onForegroundInfoChanged = 1;

        private static class Proxy implements IForegroundInfoListener {
            public static IForegroundInfoListener sDefaultImpl;
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

            public void onForegroundInfoChanged(ForegroundInfo foregroundInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (foregroundInfo != null) {
                        _data.writeInt(1);
                        foregroundInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onForegroundInfoChanged(foregroundInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IForegroundInfoListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IForegroundInfoListener)) {
                return new Proxy(obj);
            }
            return (IForegroundInfoListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onForegroundInfoChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                ForegroundInfo _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ForegroundInfo) ForegroundInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onForegroundInfoChanged(_arg0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IForegroundInfoListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IForegroundInfoListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onForegroundInfoChanged(ForegroundInfo foregroundInfo) throws RemoteException;
}
