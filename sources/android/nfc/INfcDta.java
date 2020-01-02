package android.nfc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INfcDta extends IInterface {

    public static class Default implements INfcDta {
        public void enableDta() throws RemoteException {
        }

        public void disableDta() throws RemoteException {
        }

        public boolean enableServer(String serviceName, int serviceSap, int miu, int rwSize, int testCaseId) throws RemoteException {
            return false;
        }

        public void disableServer() throws RemoteException {
        }

        public boolean enableClient(String serviceName, int miu, int rwSize, int testCaseId) throws RemoteException {
            return false;
        }

        public void disableClient() throws RemoteException {
        }

        public boolean registerMessageService(String msgServiceName) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INfcDta {
        private static final String DESCRIPTOR = "android.nfc.INfcDta";
        static final int TRANSACTION_disableClient = 6;
        static final int TRANSACTION_disableDta = 2;
        static final int TRANSACTION_disableServer = 4;
        static final int TRANSACTION_enableClient = 5;
        static final int TRANSACTION_enableDta = 1;
        static final int TRANSACTION_enableServer = 3;
        static final int TRANSACTION_registerMessageService = 7;

        private static class Proxy implements INfcDta {
            public static INfcDta sDefaultImpl;
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

            public void enableDta() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableDta();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableDta() throws RemoteException {
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
                    Stub.getDefaultImpl().disableDta();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enableServer(String serviceName, int serviceSap, int miu, int rwSize, int testCaseId) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                int i4;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(serviceName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = serviceSap;
                        i2 = miu;
                        i3 = rwSize;
                        i4 = testCaseId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(serviceSap);
                        try {
                            _data.writeInt(miu);
                            try {
                                _data.writeInt(rwSize);
                            } catch (Throwable th3) {
                                th = th3;
                                i4 = testCaseId;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            i3 = rwSize;
                            i4 = testCaseId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i2 = miu;
                        i3 = rwSize;
                        i4 = testCaseId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(testCaseId);
                        try {
                            boolean z = false;
                            if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    z = true;
                                }
                                boolean _result = z;
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            boolean enableServer = Stub.getDefaultImpl().enableServer(serviceName, serviceSap, miu, rwSize, testCaseId);
                            _reply.recycle();
                            _data.recycle();
                            return enableServer;
                        } catch (Throwable th6) {
                            th = th6;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    String str = serviceName;
                    i = serviceSap;
                    i2 = miu;
                    i3 = rwSize;
                    i4 = testCaseId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void disableServer() throws RemoteException {
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
                    Stub.getDefaultImpl().disableServer();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enableClient(String serviceName, int miu, int rwSize, int testCaseId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(serviceName);
                    _data.writeInt(miu);
                    _data.writeInt(rwSize);
                    _data.writeInt(testCaseId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().enableClient(serviceName, miu, rwSize, testCaseId);
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

            public void disableClient() throws RemoteException {
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
                    Stub.getDefaultImpl().disableClient();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean registerMessageService(String msgServiceName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(msgServiceName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().registerMessageService(msgServiceName);
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

        public static INfcDta asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INfcDta)) {
                return new Proxy(obj);
            }
            return (INfcDta) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "enableDta";
                case 2:
                    return "disableDta";
                case 3:
                    return "enableServer";
                case 4:
                    return "disableServer";
                case 5:
                    return "enableClient";
                case 6:
                    return "disableClient";
                case 7:
                    return "registerMessageService";
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
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        enableDta();
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        disableDta();
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        boolean _result = enableServer(data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        disableServer();
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        boolean _result2 = enableClient(data.readString(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        disableClient();
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        boolean _result3 = registerMessageService(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(INfcDta impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INfcDta getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void disableClient() throws RemoteException;

    void disableDta() throws RemoteException;

    void disableServer() throws RemoteException;

    boolean enableClient(String str, int i, int i2, int i3) throws RemoteException;

    void enableDta() throws RemoteException;

    boolean enableServer(String str, int i, int i2, int i3, int i4) throws RemoteException;

    boolean registerMessageService(String str) throws RemoteException;
}
