package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICountryDetector extends IInterface {

    public static class Default implements ICountryDetector {
        public Country detectCountry() throws RemoteException {
            return null;
        }

        public void addCountryListener(ICountryListener listener) throws RemoteException {
        }

        public void removeCountryListener(ICountryListener listener) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ICountryDetector {
        private static final String DESCRIPTOR = "android.location.ICountryDetector";
        static final int TRANSACTION_addCountryListener = 2;
        static final int TRANSACTION_detectCountry = 1;
        static final int TRANSACTION_removeCountryListener = 3;

        private static class Proxy implements ICountryDetector {
            public static ICountryDetector sDefaultImpl;
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

            public Country detectCountry() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Country country = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        country = Stub.getDefaultImpl();
                        if (country != 0) {
                            country = Stub.getDefaultImpl().detectCountry();
                            return country;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        country = (Country) Country.CREATOR.createFromParcel(_reply);
                    } else {
                        country = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return country;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addCountryListener(ICountryListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addCountryListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeCountryListener(ICountryListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeCountryListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICountryDetector asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICountryDetector)) {
                return new Proxy(obj);
            }
            return (ICountryDetector) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "detectCountry";
            }
            if (transactionCode == 2) {
                return "addCountryListener";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "removeCountryListener";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                Country _result = detectCountry();
                reply.writeNoException();
                if (_result != null) {
                    reply.writeInt(1);
                    _result.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                addCountryListener(android.location.ICountryListener.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                removeCountryListener(android.location.ICountryListener.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ICountryDetector impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ICountryDetector getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addCountryListener(ICountryListener iCountryListener) throws RemoteException;

    Country detectCountry() throws RemoteException;

    void removeCountryListener(ICountryListener iCountryListener) throws RemoteException;
}
