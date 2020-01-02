package android.app.contentsuggestions;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IClassificationsCallback extends IInterface {

    public static abstract class Stub extends Binder implements IClassificationsCallback {
        private static final String DESCRIPTOR = "android.app.contentsuggestions.IClassificationsCallback";
        static final int TRANSACTION_onContentClassificationsAvailable = 1;

        private static class Proxy implements IClassificationsCallback {
            public static IClassificationsCallback sDefaultImpl;
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

            public void onContentClassificationsAvailable(int statusCode, List<ContentClassification> classifications) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(statusCode);
                    _data.writeTypedList(classifications);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onContentClassificationsAvailable(statusCode, classifications);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IClassificationsCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IClassificationsCallback)) {
                return new Proxy(obj);
            }
            return (IClassificationsCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onContentClassificationsAvailable";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onContentClassificationsAvailable(data.readInt(), data.createTypedArrayList(ContentClassification.CREATOR));
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IClassificationsCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IClassificationsCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IClassificationsCallback {
        public void onContentClassificationsAvailable(int statusCode, List<ContentClassification> list) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onContentClassificationsAvailable(int i, List<ContentClassification> list) throws RemoteException;
}
