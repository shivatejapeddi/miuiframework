package android.os;

public interface IDumpstateToken extends IInterface {

    public static class Default implements IDumpstateToken {
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDumpstateToken {
        private static final String DESCRIPTOR = "android.os.IDumpstateToken";

        private static class Proxy implements IDumpstateToken {
            public static IDumpstateToken sDefaultImpl;
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

        public static IDumpstateToken asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDumpstateToken)) {
                return new Proxy(obj);
            }
            return (IDumpstateToken) iin;
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

        public static boolean setDefaultImpl(IDumpstateToken impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDumpstateToken getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
