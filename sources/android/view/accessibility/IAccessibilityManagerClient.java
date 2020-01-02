package android.view.accessibility;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAccessibilityManagerClient extends IInterface {

    public static abstract class Stub extends Binder implements IAccessibilityManagerClient {
        private static final String DESCRIPTOR = "android.view.accessibility.IAccessibilityManagerClient";
        static final int TRANSACTION_notifyServicesStateChanged = 2;
        static final int TRANSACTION_setRelevantEventTypes = 3;
        static final int TRANSACTION_setState = 1;

        private static class Proxy implements IAccessibilityManagerClient {
            public static IAccessibilityManagerClient sDefaultImpl;
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

            public void setState(int stateFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stateFlags);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setState(stateFlags);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyServicesStateChanged(long updatedUiTimeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(updatedUiTimeout);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyServicesStateChanged(updatedUiTimeout);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setRelevantEventTypes(int eventTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(eventTypes);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setRelevantEventTypes(eventTypes);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAccessibilityManagerClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAccessibilityManagerClient)) {
                return new Proxy(obj);
            }
            return (IAccessibilityManagerClient) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "setState";
            }
            if (transactionCode == 2) {
                return "notifyServicesStateChanged";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "setRelevantEventTypes";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                setState(data.readInt());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                notifyServicesStateChanged(data.readLong());
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                setRelevantEventTypes(data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IAccessibilityManagerClient impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAccessibilityManagerClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IAccessibilityManagerClient {
        public void setState(int stateFlags) throws RemoteException {
        }

        public void notifyServicesStateChanged(long updatedUiTimeout) throws RemoteException {
        }

        public void setRelevantEventTypes(int eventTypes) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void notifyServicesStateChanged(long j) throws RemoteException;

    void setRelevantEventTypes(int i) throws RemoteException;

    void setState(int i) throws RemoteException;
}
