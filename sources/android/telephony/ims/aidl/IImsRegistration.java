package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IImsRegistration extends IInterface {

    public static class Default implements IImsRegistration {
        public int getRegistrationTechnology() throws RemoteException {
            return 0;
        }

        public void addRegistrationCallback(IImsRegistrationCallback c) throws RemoteException {
        }

        public void removeRegistrationCallback(IImsRegistrationCallback c) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IImsRegistration {
        private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsRegistration";
        static final int TRANSACTION_addRegistrationCallback = 2;
        static final int TRANSACTION_getRegistrationTechnology = 1;
        static final int TRANSACTION_removeRegistrationCallback = 3;

        private static class Proxy implements IImsRegistration {
            public static IImsRegistration sDefaultImpl;
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

            public int getRegistrationTechnology() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getRegistrationTechnology();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addRegistrationCallback(IImsRegistrationCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addRegistrationCallback(c);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeRegistrationCallback(IImsRegistrationCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeRegistrationCallback(c);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsRegistration asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IImsRegistration)) {
                return new Proxy(obj);
            }
            return (IImsRegistration) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getRegistrationTechnology";
            }
            if (transactionCode == 2) {
                return "addRegistrationCallback";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "removeRegistrationCallback";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                int _result = getRegistrationTechnology();
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                addRegistrationCallback(android.telephony.ims.aidl.IImsRegistrationCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                removeRegistrationCallback(android.telephony.ims.aidl.IImsRegistrationCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IImsRegistration impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IImsRegistration getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addRegistrationCallback(IImsRegistrationCallback iImsRegistrationCallback) throws RemoteException;

    int getRegistrationTechnology() throws RemoteException;

    void removeRegistrationCallback(IImsRegistrationCallback iImsRegistrationCallback) throws RemoteException;
}
