package android.net.wifi.rtt;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IRttCallback extends IInterface {

    public static class Default implements IRttCallback {
        public void onRangingFailure(int status) throws RemoteException {
        }

        public void onRangingResults(List<RangingResult> list) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRttCallback {
        private static final String DESCRIPTOR = "android.net.wifi.rtt.IRttCallback";
        static final int TRANSACTION_onRangingFailure = 1;
        static final int TRANSACTION_onRangingResults = 2;

        private static class Proxy implements IRttCallback {
            public static IRttCallback sDefaultImpl;
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

            public void onRangingFailure(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRangingFailure(status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRangingResults(List<RangingResult> results) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(results);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRangingResults(results);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRttCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRttCallback)) {
                return new Proxy(obj);
            }
            return (IRttCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onRangingFailure";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "onRangingResults";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onRangingFailure(data.readInt());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onRangingResults(data.createTypedArrayList(RangingResult.CREATOR));
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IRttCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRttCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onRangingFailure(int i) throws RemoteException;

    void onRangingResults(List<RangingResult> list) throws RemoteException;
}
