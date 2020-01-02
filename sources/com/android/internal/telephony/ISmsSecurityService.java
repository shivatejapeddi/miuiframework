package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISmsSecurityService extends IInterface {

    public static class Default implements ISmsSecurityService {
        public boolean register(ISmsSecurityAgent agent) throws RemoteException {
            return false;
        }

        public boolean unregister(ISmsSecurityAgent agent) throws RemoteException {
            return false;
        }

        public boolean sendResponse(SmsAuthorizationRequest request, boolean authorized) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISmsSecurityService {
        private static final String DESCRIPTOR = "com.android.internal.telephony.ISmsSecurityService";
        static final int TRANSACTION_register = 1;
        static final int TRANSACTION_sendResponse = 3;
        static final int TRANSACTION_unregister = 2;

        private static class Proxy implements ISmsSecurityService {
            public static ISmsSecurityService sDefaultImpl;
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

            public boolean register(ISmsSecurityAgent agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(agent != null ? agent.asBinder() : null);
                    boolean z = false;
                    if (this.mRemote.transact(1, _data, _reply, z) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            z = true;
                        }
                        boolean _result = z;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    z = Stub.getDefaultImpl().register(agent);
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean unregister(ISmsSecurityAgent agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(agent != null ? agent.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().unregister(agent);
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

            public boolean sendResponse(SmsAuthorizationRequest request, boolean authorized) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(authorized ? 1 : 0);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().sendResponse(request, authorized);
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

        public static ISmsSecurityService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISmsSecurityService)) {
                return new Proxy(obj);
            }
            return (ISmsSecurityService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "register";
            }
            if (transactionCode == 2) {
                return "unregister";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "sendResponse";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            boolean _result;
            if (code == 1) {
                data.enforceInterface(descriptor);
                _result = register(com.android.internal.telephony.ISmsSecurityAgent.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                _result = unregister(com.android.internal.telephony.ISmsSecurityAgent.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 3) {
                SmsAuthorizationRequest _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (SmsAuthorizationRequest) SmsAuthorizationRequest.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                boolean _result2 = sendResponse(_arg0, data.readInt() != 0);
                reply.writeNoException();
                reply.writeInt(_result2);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ISmsSecurityService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISmsSecurityService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean register(ISmsSecurityAgent iSmsSecurityAgent) throws RemoteException;

    boolean sendResponse(SmsAuthorizationRequest smsAuthorizationRequest, boolean z) throws RemoteException;

    boolean unregister(ISmsSecurityAgent iSmsSecurityAgent) throws RemoteException;
}
