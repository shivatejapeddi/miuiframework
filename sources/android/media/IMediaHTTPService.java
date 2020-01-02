package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMediaHTTPService extends IInterface {

    public static class Default implements IMediaHTTPService {
        public IMediaHTTPConnection makeHTTPConnection() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMediaHTTPService {
        private static final String DESCRIPTOR = "android.media.IMediaHTTPService";
        static final int TRANSACTION_makeHTTPConnection = 1;

        private static class Proxy implements IMediaHTTPService {
            public static IMediaHTTPService sDefaultImpl;
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

            public IMediaHTTPConnection makeHTTPConnection() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IMediaHTTPConnection iMediaHTTPConnection = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        iMediaHTTPConnection = Stub.getDefaultImpl();
                        if (iMediaHTTPConnection != 0) {
                            iMediaHTTPConnection = Stub.getDefaultImpl().makeHTTPConnection();
                            return iMediaHTTPConnection;
                        }
                    }
                    _reply.readException();
                    iMediaHTTPConnection = android.media.IMediaHTTPConnection.Stub.asInterface(_reply.readStrongBinder());
                    IMediaHTTPConnection _result = iMediaHTTPConnection;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMediaHTTPService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMediaHTTPService)) {
                return new Proxy(obj);
            }
            return (IMediaHTTPService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "makeHTTPConnection";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                IMediaHTTPConnection _result = makeHTTPConnection();
                reply.writeNoException();
                reply.writeStrongBinder(_result != null ? _result.asBinder() : null);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMediaHTTPService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMediaHTTPService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    IMediaHTTPConnection makeHTTPConnection() throws RemoteException;
}
