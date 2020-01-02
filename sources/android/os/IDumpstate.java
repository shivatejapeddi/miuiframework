package android.os;

import java.io.FileDescriptor;

public interface IDumpstate extends IInterface {
    public static final int BUGREPORT_MODE_DEFAULT = 6;
    public static final int BUGREPORT_MODE_FULL = 0;
    public static final int BUGREPORT_MODE_INTERACTIVE = 1;
    public static final int BUGREPORT_MODE_REMOTE = 2;
    public static final int BUGREPORT_MODE_TELEPHONY = 4;
    public static final int BUGREPORT_MODE_WEAR = 3;
    public static final int BUGREPORT_MODE_WIFI = 5;

    public static class Default implements IDumpstate {
        public IDumpstateToken setListener(String name, IDumpstateListener listener, boolean getSectionDetails) throws RemoteException {
            return null;
        }

        public void startBugreport(int callingUid, String callingPackage, FileDescriptor bugreportFd, FileDescriptor screenshotFd, int bugreportMode, IDumpstateListener listener) throws RemoteException {
        }

        public void cancelBugreport() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDumpstate {
        private static final String DESCRIPTOR = "android.os.IDumpstate";
        static final int TRANSACTION_cancelBugreport = 3;
        static final int TRANSACTION_setListener = 1;
        static final int TRANSACTION_startBugreport = 2;

        private static class Proxy implements IDumpstate {
            public static IDumpstate sDefaultImpl;
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

            public IDumpstateToken setListener(String name, IDumpstateListener listener, boolean getSectionDetails) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    IDumpstateToken iDumpstateToken = 0;
                    _data.writeInt(getSectionDetails ? 1 : 0);
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        iDumpstateToken = Stub.getDefaultImpl();
                        if (iDumpstateToken != 0) {
                            iDumpstateToken = Stub.getDefaultImpl().setListener(name, listener, getSectionDetails);
                            return iDumpstateToken;
                        }
                    }
                    _reply.readException();
                    iDumpstateToken = android.os.IDumpstateToken.Stub.asInterface(_reply.readStrongBinder());
                    IDumpstateToken _result = iDumpstateToken;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startBugreport(int callingUid, String callingPackage, FileDescriptor bugreportFd, FileDescriptor screenshotFd, int bugreportMode, IDumpstateListener listener) throws RemoteException {
                Throwable th;
                String str;
                FileDescriptor fileDescriptor;
                FileDescriptor fileDescriptor2;
                int i;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(callingUid);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPackage;
                        fileDescriptor = bugreportFd;
                        fileDescriptor2 = screenshotFd;
                        i = bugreportMode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPackage);
                        try {
                            _data.writeRawFileDescriptor(bugreportFd);
                            try {
                                _data.writeRawFileDescriptor(screenshotFd);
                            } catch (Throwable th3) {
                                th = th3;
                                i = bugreportMode;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            fileDescriptor2 = screenshotFd;
                            i = bugreportMode;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        fileDescriptor = bugreportFd;
                        fileDescriptor2 = screenshotFd;
                        i = bugreportMode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(bugreportMode);
                        _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().startBugreport(callingUid, callingPackage, bugreportFd, screenshotFd, bugreportMode, listener);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th7) {
                        th = th7;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    int i2 = callingUid;
                    str = callingPackage;
                    fileDescriptor = bugreportFd;
                    fileDescriptor2 = screenshotFd;
                    i = bugreportMode;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void cancelBugreport() throws RemoteException {
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
                    Stub.getDefaultImpl().cancelBugreport();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDumpstate asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDumpstate)) {
                return new Proxy(obj);
            }
            return (IDumpstate) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "setListener";
            }
            if (transactionCode == 2) {
                return "startBugreport";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "cancelBugreport";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i == 1) {
                parcel.enforceInterface(descriptor);
                IDumpstateToken _result = setListener(data.readString(), android.os.IDumpstateListener.Stub.asInterface(data.readStrongBinder()), data.readInt() != 0);
                reply.writeNoException();
                parcel2.writeStrongBinder(_result != null ? _result.asBinder() : null);
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(descriptor);
                startBugreport(data.readInt(), data.readString(), data.readRawFileDescriptor(), data.readRawFileDescriptor(), data.readInt(), android.os.IDumpstateListener.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(descriptor);
                cancelBugreport();
                reply.writeNoException();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                parcel2.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IDumpstate impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDumpstate getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void cancelBugreport() throws RemoteException;

    IDumpstateToken setListener(String str, IDumpstateListener iDumpstateListener, boolean z) throws RemoteException;

    void startBugreport(int i, String str, FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, int i2, IDumpstateListener iDumpstateListener) throws RemoteException;
}
