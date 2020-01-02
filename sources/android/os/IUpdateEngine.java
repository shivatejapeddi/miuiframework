package android.os;

public interface IUpdateEngine extends IInterface {

    public static class Default implements IUpdateEngine {
        public void applyPayload(String url, long payload_offset, long payload_size, String[] headerKeyValuePairs) throws RemoteException {
        }

        public boolean bind(IUpdateEngineCallback callback) throws RemoteException {
            return false;
        }

        public boolean unbind(IUpdateEngineCallback callback) throws RemoteException {
            return false;
        }

        public void suspend() throws RemoteException {
        }

        public void resume() throws RemoteException {
        }

        public void cancel() throws RemoteException {
        }

        public void resetStatus() throws RemoteException {
        }

        public boolean verifyPayloadApplicable(String metadataFilename) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IUpdateEngine {
        private static final String DESCRIPTOR = "android.os.IUpdateEngine";
        static final int TRANSACTION_applyPayload = 1;
        static final int TRANSACTION_bind = 2;
        static final int TRANSACTION_cancel = 6;
        static final int TRANSACTION_resetStatus = 7;
        static final int TRANSACTION_resume = 5;
        static final int TRANSACTION_suspend = 4;
        static final int TRANSACTION_unbind = 3;
        static final int TRANSACTION_verifyPayloadApplicable = 8;

        private static class Proxy implements IUpdateEngine {
            public static IUpdateEngine sDefaultImpl;
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

            public void applyPayload(String url, long payload_offset, long payload_size, String[] headerKeyValuePairs) throws RemoteException {
                Throwable th;
                long j;
                long j2;
                String[] strArr;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(url);
                    } catch (Throwable th2) {
                        th = th2;
                        j = payload_offset;
                        j2 = payload_size;
                        strArr = headerKeyValuePairs;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(payload_offset);
                    } catch (Throwable th3) {
                        th = th3;
                        j2 = payload_size;
                        strArr = headerKeyValuePairs;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(payload_size);
                        try {
                            _data.writeStringArray(headerKeyValuePairs);
                            if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().applyPayload(url, payload_offset, payload_size, headerKeyValuePairs);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        strArr = headerKeyValuePairs;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = url;
                    j = payload_offset;
                    j2 = payload_size;
                    strArr = headerKeyValuePairs;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean bind(IUpdateEngineCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().bind(callback);
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

            public boolean unbind(IUpdateEngineCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().unbind(callback);
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

            public void suspend() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().suspend();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resume() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resume();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancel();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resetStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resetStatus();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean verifyPayloadApplicable(String metadataFilename) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(metadataFilename);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().verifyPayloadApplicable(metadataFilename);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IUpdateEngine asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IUpdateEngine)) {
                return new Proxy(obj);
            }
            return (IUpdateEngine) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "applyPayload";
                case 2:
                    return "bind";
                case 3:
                    return "unbind";
                case 4:
                    return "suspend";
                case 5:
                    return "resume";
                case 6:
                    return "cancel";
                case 7:
                    return "resetStatus";
                case 8:
                    return "verifyPayloadApplicable";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                boolean _result;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        applyPayload(data.readString(), data.readLong(), data.readLong(), data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _result = bind(android.os.IUpdateEngineCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _result = unbind(android.os.IUpdateEngineCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        suspend();
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        resume();
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        cancel();
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        resetStatus();
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _result = verifyPayloadApplicable(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IUpdateEngine impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IUpdateEngine getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void applyPayload(String str, long j, long j2, String[] strArr) throws RemoteException;

    boolean bind(IUpdateEngineCallback iUpdateEngineCallback) throws RemoteException;

    void cancel() throws RemoteException;

    void resetStatus() throws RemoteException;

    void resume() throws RemoteException;

    void suspend() throws RemoteException;

    boolean unbind(IUpdateEngineCallback iUpdateEngineCallback) throws RemoteException;

    boolean verifyPayloadApplicable(String str) throws RemoteException;
}
