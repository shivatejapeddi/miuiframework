package android.hardware.iris;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IIrisService extends IInterface {

    public static class Default implements IIrisService {
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IIrisService {
        private static final String DESCRIPTOR = "android.hardware.iris.IIrisService";

        private static class Proxy implements IIrisService {
            public static IIrisService sDefaultImpl;
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IIrisService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IIrisService)) {
                return new Proxy(obj);
            }
            return (IIrisService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            return null;
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IIrisService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IIrisService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
