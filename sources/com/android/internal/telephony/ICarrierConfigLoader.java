package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteException;

public interface ICarrierConfigLoader extends IInterface {

    public static class Default implements ICarrierConfigLoader {
        public PersistableBundle getConfigForSubId(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public void overrideConfig(int subId, PersistableBundle overrides) throws RemoteException {
        }

        public void notifyConfigChangedForSubId(int subId) throws RemoteException {
        }

        public void updateConfigForPhoneId(int phoneId, String simState) throws RemoteException {
        }

        public String getDefaultCarrierServicePackageName() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ICarrierConfigLoader {
        private static final String DESCRIPTOR = "com.android.internal.telephony.ICarrierConfigLoader";
        static final int TRANSACTION_getConfigForSubId = 1;
        static final int TRANSACTION_getDefaultCarrierServicePackageName = 5;
        static final int TRANSACTION_notifyConfigChangedForSubId = 3;
        static final int TRANSACTION_overrideConfig = 2;
        static final int TRANSACTION_updateConfigForPhoneId = 4;

        private static class Proxy implements ICarrierConfigLoader {
            public static ICarrierConfigLoader sDefaultImpl;
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

            public PersistableBundle getConfigForSubId(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    PersistableBundle persistableBundle = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        persistableBundle = Stub.getDefaultImpl();
                        if (persistableBundle != 0) {
                            persistableBundle = Stub.getDefaultImpl().getConfigForSubId(subId, callingPackage);
                            return persistableBundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        persistableBundle = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(_reply);
                    } else {
                        persistableBundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return persistableBundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void overrideConfig(int subId, PersistableBundle overrides) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (overrides != null) {
                        _data.writeInt(1);
                        overrides.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().overrideConfig(subId, overrides);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyConfigChangedForSubId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyConfigChangedForSubId(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateConfigForPhoneId(int phoneId, String simState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(simState);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateConfigForPhoneId(phoneId, simState);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDefaultCarrierServicePackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDefaultCarrierServicePackageName();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
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

        public static ICarrierConfigLoader asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICarrierConfigLoader)) {
                return new Proxy(obj);
            }
            return (ICarrierConfigLoader) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getConfigForSubId";
            }
            if (transactionCode == 2) {
                return "overrideConfig";
            }
            if (transactionCode == 3) {
                return "notifyConfigChangedForSubId";
            }
            if (transactionCode == 4) {
                return "updateConfigForPhoneId";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "getDefaultCarrierServicePackageName";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                PersistableBundle _result = getConfigForSubId(data.readInt(), data.readString());
                reply.writeNoException();
                if (_result != null) {
                    reply.writeInt(1);
                    _result.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 2) {
                PersistableBundle _arg1;
                data.enforceInterface(descriptor);
                int _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                overrideConfig(_arg0, _arg1);
                reply.writeNoException();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                notifyConfigChangedForSubId(data.readInt());
                reply.writeNoException();
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                updateConfigForPhoneId(data.readInt(), data.readString());
                reply.writeNoException();
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                String _result2 = getDefaultCarrierServicePackageName();
                reply.writeNoException();
                reply.writeString(_result2);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ICarrierConfigLoader impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ICarrierConfigLoader getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    PersistableBundle getConfigForSubId(int i, String str) throws RemoteException;

    String getDefaultCarrierServicePackageName() throws RemoteException;

    void notifyConfigChangedForSubId(int i) throws RemoteException;

    void overrideConfig(int i, PersistableBundle persistableBundle) throws RemoteException;

    void updateConfigForPhoneId(int i, String str) throws RemoteException;
}
