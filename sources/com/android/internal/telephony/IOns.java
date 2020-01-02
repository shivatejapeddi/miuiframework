package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.AvailableNetworkInfo;
import java.util.List;

public interface IOns extends IInterface {

    public static class Default implements IOns {
        public boolean setEnable(boolean enable, String callingPackage) throws RemoteException {
            return false;
        }

        public boolean isEnabled(String callingPackage) throws RemoteException {
            return false;
        }

        public void setPreferredDataSubscriptionId(int subId, boolean needValidation, ISetOpportunisticDataCallback callbackStub, String callingPackage) throws RemoteException {
        }

        public int getPreferredDataSubscriptionId(String callingPackage) throws RemoteException {
            return 0;
        }

        public void updateAvailableNetworks(List<AvailableNetworkInfo> list, IUpdateAvailableNetworksCallback callbackStub, String callingPackage) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IOns {
        private static final String DESCRIPTOR = "com.android.internal.telephony.IOns";
        static final int TRANSACTION_getPreferredDataSubscriptionId = 4;
        static final int TRANSACTION_isEnabled = 2;
        static final int TRANSACTION_setEnable = 1;
        static final int TRANSACTION_setPreferredDataSubscriptionId = 3;
        static final int TRANSACTION_updateAvailableNetworks = 5;

        private static class Proxy implements IOns {
            public static IOns sDefaultImpl;
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

            public boolean setEnable(boolean enable, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setEnable(enable, callingPackage);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEnabled(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isEnabled(callingPackage);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPreferredDataSubscriptionId(int subId, boolean needValidation, ISetOpportunisticDataCallback callbackStub, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(needValidation ? 1 : 0);
                    _data.writeStrongBinder(callbackStub != null ? callbackStub.asBinder() : null);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPreferredDataSubscriptionId(subId, needValidation, callbackStub, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPreferredDataSubscriptionId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    int i = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPreferredDataSubscriptionId(callingPackage);
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

            public void updateAvailableNetworks(List<AvailableNetworkInfo> availableNetworks, IUpdateAvailableNetworksCallback callbackStub, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(availableNetworks);
                    _data.writeStrongBinder(callbackStub != null ? callbackStub.asBinder() : null);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateAvailableNetworks(availableNetworks, callbackStub, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOns asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IOns)) {
                return new Proxy(obj);
            }
            return (IOns) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "setEnable";
            }
            if (transactionCode == 2) {
                return "isEnabled";
            }
            if (transactionCode == 3) {
                return "setPreferredDataSubscriptionId";
            }
            if (transactionCode == 4) {
                return "getPreferredDataSubscriptionId";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "updateAvailableNetworks";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            boolean _arg1 = false;
            int _arg0;
            if (code == 1) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg1 = true;
                }
                boolean _result = setEnable(_arg1, data.readString());
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                boolean _result2 = isEnabled(data.readString());
                reply.writeNoException();
                reply.writeInt(_result2);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = true;
                }
                setPreferredDataSubscriptionId(_arg0, _arg1, com.android.internal.telephony.ISetOpportunisticDataCallback.Stub.asInterface(data.readStrongBinder()), data.readString());
                reply.writeNoException();
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                _arg0 = getPreferredDataSubscriptionId(data.readString());
                reply.writeNoException();
                reply.writeInt(_arg0);
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                updateAvailableNetworks(data.createTypedArrayList(AvailableNetworkInfo.CREATOR), com.android.internal.telephony.IUpdateAvailableNetworksCallback.Stub.asInterface(data.readStrongBinder()), data.readString());
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IOns impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IOns getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int getPreferredDataSubscriptionId(String str) throws RemoteException;

    boolean isEnabled(String str) throws RemoteException;

    boolean setEnable(boolean z, String str) throws RemoteException;

    void setPreferredDataSubscriptionId(int i, boolean z, ISetOpportunisticDataCallback iSetOpportunisticDataCallback, String str) throws RemoteException;

    void updateAvailableNetworks(List<AvailableNetworkInfo> list, IUpdateAvailableNetworksCallback iUpdateAvailableNetworksCallback, String str) throws RemoteException;
}
