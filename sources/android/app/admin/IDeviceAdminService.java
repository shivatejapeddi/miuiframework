package android.app.admin;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDeviceAdminService extends IInterface {

    public static abstract class Stub extends Binder implements IDeviceAdminService {
        private static final String DESCRIPTOR = "android.app.admin.IDeviceAdminService";

        private static class Proxy implements IDeviceAdminService {
            public static IDeviceAdminService sDefaultImpl;
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

        public static IDeviceAdminService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDeviceAdminService)) {
                return new Proxy(obj);
            }
            return (IDeviceAdminService) iin;
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

        public static boolean setDefaultImpl(IDeviceAdminService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDeviceAdminService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IDeviceAdminService {
        public IBinder asBinder() {
            return null;
        }
    }
}
