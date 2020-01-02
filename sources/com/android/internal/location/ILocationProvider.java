package com.android.internal.location;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.WorkSource;

public interface ILocationProvider extends IInterface {

    public static class Default implements ILocationProvider {
        public void setLocationProviderManager(ILocationProviderManager manager) throws RemoteException {
        }

        public void setRequest(ProviderRequest request, WorkSource ws) throws RemoteException {
        }

        public void sendExtraCommand(String command, Bundle extras) throws RemoteException {
        }

        public int getStatus(Bundle extras) throws RemoteException {
            return 0;
        }

        public long getStatusUpdateTime() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ILocationProvider {
        private static final String DESCRIPTOR = "com.android.internal.location.ILocationProvider";
        static final int TRANSACTION_getStatus = 4;
        static final int TRANSACTION_getStatusUpdateTime = 5;
        static final int TRANSACTION_sendExtraCommand = 3;
        static final int TRANSACTION_setLocationProviderManager = 1;
        static final int TRANSACTION_setRequest = 2;

        private static class Proxy implements ILocationProvider {
            public static ILocationProvider sDefaultImpl;
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

            public void setLocationProviderManager(ILocationProviderManager manager) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(manager != null ? manager.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setLocationProviderManager(manager);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setRequest(ProviderRequest request, WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setRequest(request, ws);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void sendExtraCommand(String command, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(command);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().sendExtraCommand(command, extras);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public int getStatus(Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getStatus(extras);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        extras.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getStatusUpdateTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getStatusUpdateTime();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
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

        public static ILocationProvider asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ILocationProvider)) {
                return new Proxy(obj);
            }
            return (ILocationProvider) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "setLocationProviderManager";
            }
            if (transactionCode == 2) {
                return "setRequest";
            }
            if (transactionCode == 3) {
                return "sendExtraCommand";
            }
            if (transactionCode == 4) {
                return "getStatus";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "getStatusUpdateTime";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                setLocationProviderManager(com.android.internal.location.ILocationProviderManager.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 2) {
                ProviderRequest _arg0;
                WorkSource _arg1;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ProviderRequest) ProviderRequest.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                if (data.readInt() != 0) {
                    _arg1 = (WorkSource) WorkSource.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                setRequest(_arg0, _arg1);
                return true;
            } else if (code == 3) {
                Bundle _arg12;
                data.enforceInterface(descriptor);
                String _arg02 = data.readString();
                if (data.readInt() != 0) {
                    _arg12 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                } else {
                    _arg12 = null;
                }
                sendExtraCommand(_arg02, _arg12);
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                Bundle _arg03 = new Bundle();
                int _result = getStatus(_arg03);
                reply.writeNoException();
                reply.writeInt(_result);
                reply.writeInt(1);
                _arg03.writeToParcel(reply, 1);
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                long _result2 = getStatusUpdateTime();
                reply.writeNoException();
                reply.writeLong(_result2);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ILocationProvider impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ILocationProvider getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    int getStatus(Bundle bundle) throws RemoteException;

    @UnsupportedAppUsage
    long getStatusUpdateTime() throws RemoteException;

    void sendExtraCommand(String str, Bundle bundle) throws RemoteException;

    void setLocationProviderManager(ILocationProviderManager iLocationProviderManager) throws RemoteException;

    void setRequest(ProviderRequest providerRequest, WorkSource workSource) throws RemoteException;
}
