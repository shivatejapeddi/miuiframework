package com.android.internal.inputmethod;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.InputChannel;
import com.android.internal.view.IInputMethodSession;

public interface IMultiClientInputMethodPrivilegedOperations extends IInterface {

    public static class Default implements IMultiClientInputMethodPrivilegedOperations {
        public IBinder createInputMethodWindowToken(int displayId) throws RemoteException {
            return null;
        }

        public void deleteInputMethodWindowToken(IBinder token) throws RemoteException {
        }

        public void acceptClient(int clientId, IInputMethodSession session, IMultiClientInputMethodSession multiClientSession, InputChannel writeChannel) throws RemoteException {
        }

        public void reportImeWindowTarget(int clientId, int targetWindowHandle, IBinder imeWindowToken) throws RemoteException {
        }

        public boolean isUidAllowedOnDisplay(int displayId, int uid) throws RemoteException {
            return false;
        }

        public void setActive(int clientId, boolean active) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMultiClientInputMethodPrivilegedOperations {
        private static final String DESCRIPTOR = "com.android.internal.inputmethod.IMultiClientInputMethodPrivilegedOperations";
        static final int TRANSACTION_acceptClient = 3;
        static final int TRANSACTION_createInputMethodWindowToken = 1;
        static final int TRANSACTION_deleteInputMethodWindowToken = 2;
        static final int TRANSACTION_isUidAllowedOnDisplay = 5;
        static final int TRANSACTION_reportImeWindowTarget = 4;
        static final int TRANSACTION_setActive = 6;

        private static class Proxy implements IMultiClientInputMethodPrivilegedOperations {
            public static IMultiClientInputMethodPrivilegedOperations sDefaultImpl;
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

            public IBinder createInputMethodWindowToken(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    IBinder iBinder = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        iBinder = Stub.getDefaultImpl();
                        if (iBinder != 0) {
                            iBinder = Stub.getDefaultImpl().createInputMethodWindowToken(displayId);
                            return iBinder;
                        }
                    }
                    _reply.readException();
                    iBinder = _reply.readStrongBinder();
                    IBinder _result = iBinder;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteInputMethodWindowToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteInputMethodWindowToken(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void acceptClient(int clientId, IInputMethodSession session, IMultiClientInputMethodSession multiClientSession, InputChannel writeChannel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (multiClientSession != null) {
                        iBinder = multiClientSession.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    if (writeChannel != null) {
                        _data.writeInt(1);
                        writeChannel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().acceptClient(clientId, session, multiClientSession, writeChannel);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportImeWindowTarget(int clientId, int targetWindowHandle, IBinder imeWindowToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(targetWindowHandle);
                    _data.writeStrongBinder(imeWindowToken);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportImeWindowTarget(clientId, targetWindowHandle, imeWindowToken);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isUidAllowedOnDisplay(int displayId, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUidAllowedOnDisplay(displayId, uid);
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

            public void setActive(int clientId, boolean active) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(active ? 1 : 0);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setActive(clientId, active);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMultiClientInputMethodPrivilegedOperations asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMultiClientInputMethodPrivilegedOperations)) {
                return new Proxy(obj);
            }
            return (IMultiClientInputMethodPrivilegedOperations) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "createInputMethodWindowToken";
                case 2:
                    return "deleteInputMethodWindowToken";
                case 3:
                    return "acceptClient";
                case 4:
                    return "reportImeWindowTarget";
                case 5:
                    return "isUidAllowedOnDisplay";
                case 6:
                    return "setActive";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        IBinder _result = createInputMethodWindowToken(data.readInt());
                        reply.writeNoException();
                        reply.writeStrongBinder(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        deleteInputMethodWindowToken(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 3:
                        InputChannel _arg3;
                        data.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        IInputMethodSession _arg1 = com.android.internal.view.IInputMethodSession.Stub.asInterface(data.readStrongBinder());
                        IMultiClientInputMethodSession _arg2 = com.android.internal.inputmethod.IMultiClientInputMethodSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg3 = (InputChannel) InputChannel.CREATOR.createFromParcel(data);
                        } else {
                            _arg3 = null;
                        }
                        acceptClient(_arg0, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        reportImeWindowTarget(data.readInt(), data.readInt(), data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        boolean _result2 = isUidAllowedOnDisplay(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        setActive(data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IMultiClientInputMethodPrivilegedOperations impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMultiClientInputMethodPrivilegedOperations getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void acceptClient(int i, IInputMethodSession iInputMethodSession, IMultiClientInputMethodSession iMultiClientInputMethodSession, InputChannel inputChannel) throws RemoteException;

    IBinder createInputMethodWindowToken(int i) throws RemoteException;

    void deleteInputMethodWindowToken(IBinder iBinder) throws RemoteException;

    boolean isUidAllowedOnDisplay(int i, int i2) throws RemoteException;

    void reportImeWindowTarget(int i, int i2, IBinder iBinder) throws RemoteException;

    void setActive(int i, boolean z) throws RemoteException;
}
