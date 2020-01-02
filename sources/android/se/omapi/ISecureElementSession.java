package android.se.omapi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISecureElementSession extends IInterface {

    public static class Default implements ISecureElementSession {
        public byte[] getAtr() throws RemoteException {
            return null;
        }

        public void close() throws RemoteException {
        }

        public void closeChannels() throws RemoteException {
        }

        public boolean isClosed() throws RemoteException {
            return false;
        }

        public ISecureElementChannel openBasicChannel(byte[] aid, byte p2, ISecureElementListener listener) throws RemoteException {
            return null;
        }

        public ISecureElementChannel openLogicalChannel(byte[] aid, byte p2, ISecureElementListener listener) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISecureElementSession {
        private static final String DESCRIPTOR = "android.se.omapi.ISecureElementSession";
        static final int TRANSACTION_close = 2;
        static final int TRANSACTION_closeChannels = 3;
        static final int TRANSACTION_getAtr = 1;
        static final int TRANSACTION_isClosed = 4;
        static final int TRANSACTION_openBasicChannel = 5;
        static final int TRANSACTION_openLogicalChannel = 6;

        private static class Proxy implements ISecureElementSession {
            public static ISecureElementSession sDefaultImpl;
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

            public byte[] getAtr() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    byte[] bArr = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getAtr();
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void close() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().close();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void closeChannels() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().closeChannels();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isClosed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isClosed();
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

            public ISecureElementChannel openBasicChannel(byte[] aid, byte p2, ISecureElementListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(aid);
                    _data.writeByte(p2);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    ISecureElementChannel iSecureElementChannel = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        iSecureElementChannel = Stub.getDefaultImpl();
                        if (iSecureElementChannel != 0) {
                            iSecureElementChannel = Stub.getDefaultImpl().openBasicChannel(aid, p2, listener);
                            return iSecureElementChannel;
                        }
                    }
                    _reply.readException();
                    iSecureElementChannel = android.se.omapi.ISecureElementChannel.Stub.asInterface(_reply.readStrongBinder());
                    ISecureElementChannel _result = iSecureElementChannel;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ISecureElementChannel openLogicalChannel(byte[] aid, byte p2, ISecureElementListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(aid);
                    _data.writeByte(p2);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    ISecureElementChannel iSecureElementChannel = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        iSecureElementChannel = Stub.getDefaultImpl();
                        if (iSecureElementChannel != 0) {
                            iSecureElementChannel = Stub.getDefaultImpl().openLogicalChannel(aid, p2, listener);
                            return iSecureElementChannel;
                        }
                    }
                    _reply.readException();
                    iSecureElementChannel = android.se.omapi.ISecureElementChannel.Stub.asInterface(_reply.readStrongBinder());
                    ISecureElementChannel _result = iSecureElementChannel;
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

        public static ISecureElementSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISecureElementSession)) {
                return new Proxy(obj);
            }
            return (ISecureElementSession) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getAtr";
                case 2:
                    return "close";
                case 3:
                    return "closeChannels";
                case 4:
                    return "isClosed";
                case 5:
                    return "openBasicChannel";
                case 6:
                    return "openLogicalChannel";
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
                IBinder iBinder = null;
                ISecureElementChannel _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        byte[] _result2 = getAtr();
                        reply.writeNoException();
                        reply.writeByteArray(_result2);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        close();
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        closeChannels();
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        boolean _result3 = isClosed();
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result = openBasicChannel(data.createByteArray(), data.readByte(), android.se.omapi.ISecureElementListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result != null) {
                            iBinder = _result.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result = openLogicalChannel(data.createByteArray(), data.readByte(), android.se.omapi.ISecureElementListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result != null) {
                            iBinder = _result.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ISecureElementSession impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISecureElementSession getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void close() throws RemoteException;

    void closeChannels() throws RemoteException;

    byte[] getAtr() throws RemoteException;

    boolean isClosed() throws RemoteException;

    ISecureElementChannel openBasicChannel(byte[] bArr, byte b, ISecureElementListener iSecureElementListener) throws RemoteException;

    ISecureElementChannel openLogicalChannel(byte[] bArr, byte b, ISecureElementListener iSecureElementListener) throws RemoteException;
}
