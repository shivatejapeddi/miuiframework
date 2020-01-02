package android.content.pm;

import android.content.IntentSender;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

public interface IPackageInstallerSession extends IInterface {

    public static class Default implements IPackageInstallerSession {
        public void setClientProgress(float progress) throws RemoteException {
        }

        public void addClientProgress(float progress) throws RemoteException {
        }

        public String[] getNames() throws RemoteException {
            return null;
        }

        public ParcelFileDescriptor openWrite(String name, long offsetBytes, long lengthBytes) throws RemoteException {
            return null;
        }

        public ParcelFileDescriptor openRead(String name) throws RemoteException {
            return null;
        }

        public void write(String name, long offsetBytes, long lengthBytes, ParcelFileDescriptor fd) throws RemoteException {
        }

        public void removeSplit(String splitName) throws RemoteException {
        }

        public void close() throws RemoteException {
        }

        public void commit(IntentSender statusReceiver, boolean forTransferred) throws RemoteException {
        }

        public void transfer(String packageName) throws RemoteException {
        }

        public void abandon() throws RemoteException {
        }

        public boolean isMultiPackage() throws RemoteException {
            return false;
        }

        public int[] getChildSessionIds() throws RemoteException {
            return null;
        }

        public void addChildSessionId(int sessionId) throws RemoteException {
        }

        public void removeChildSessionId(int sessionId) throws RemoteException {
        }

        public int getParentSessionId() throws RemoteException {
            return 0;
        }

        public boolean isStaged() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPackageInstallerSession {
        private static final String DESCRIPTOR = "android.content.pm.IPackageInstallerSession";
        static final int TRANSACTION_abandon = 11;
        static final int TRANSACTION_addChildSessionId = 14;
        static final int TRANSACTION_addClientProgress = 2;
        static final int TRANSACTION_close = 8;
        static final int TRANSACTION_commit = 9;
        static final int TRANSACTION_getChildSessionIds = 13;
        static final int TRANSACTION_getNames = 3;
        static final int TRANSACTION_getParentSessionId = 16;
        static final int TRANSACTION_isMultiPackage = 12;
        static final int TRANSACTION_isStaged = 17;
        static final int TRANSACTION_openRead = 5;
        static final int TRANSACTION_openWrite = 4;
        static final int TRANSACTION_removeChildSessionId = 15;
        static final int TRANSACTION_removeSplit = 7;
        static final int TRANSACTION_setClientProgress = 1;
        static final int TRANSACTION_transfer = 10;
        static final int TRANSACTION_write = 6;

        private static class Proxy implements IPackageInstallerSession {
            public static IPackageInstallerSession sDefaultImpl;
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

            public void setClientProgress(float progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(progress);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setClientProgress(progress);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addClientProgress(float progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(progress);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addClientProgress(progress);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getNames() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getNames();
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor openWrite(String name, long offsetBytes, long lengthBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeLong(offsetBytes);
                    _data.writeLong(lengthBytes);
                    ParcelFileDescriptor parcelFileDescriptor = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().openWrite(name, offsetBytes, lengthBytes);
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor openRead(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    ParcelFileDescriptor parcelFileDescriptor = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().openRead(name);
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void write(String name, long offsetBytes, long lengthBytes, ParcelFileDescriptor fd) throws RemoteException {
                Throwable th;
                long j;
                long j2;
                ParcelFileDescriptor parcelFileDescriptor = fd;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(name);
                        try {
                            _data.writeLong(offsetBytes);
                        } catch (Throwable th2) {
                            th = th2;
                            j = lengthBytes;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeLong(lengthBytes);
                            if (parcelFileDescriptor != null) {
                                _data.writeInt(1);
                                parcelFileDescriptor.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().write(name, offsetBytes, lengthBytes, fd);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        j2 = offsetBytes;
                        j = lengthBytes;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    String str = name;
                    j2 = offsetBytes;
                    j = lengthBytes;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void removeSplit(String splitName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(splitName);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeSplit(splitName);
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
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void commit(IntentSender statusReceiver, boolean forTransferred) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (statusReceiver != null) {
                        _data.writeInt(1);
                        statusReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!forTransferred) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().commit(statusReceiver, forTransferred);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void transfer(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().transfer(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void abandon() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().abandon();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isMultiPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isMultiPackage();
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

            public int[] getChildSessionIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int[] iArr = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getChildSessionIds();
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addChildSessionId(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addChildSessionId(sessionId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeChildSessionId(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeChildSessionId(sessionId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getParentSessionId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getParentSessionId();
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

            public boolean isStaged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isStaged();
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

        public static IPackageInstallerSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPackageInstallerSession)) {
                return new Proxy(obj);
            }
            return (IPackageInstallerSession) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setClientProgress";
                case 2:
                    return "addClientProgress";
                case 3:
                    return "getNames";
                case 4:
                    return "openWrite";
                case 5:
                    return "openRead";
                case 6:
                    return "write";
                case 7:
                    return "removeSplit";
                case 8:
                    return "close";
                case 9:
                    return "commit";
                case 10:
                    return "transfer";
                case 11:
                    return "abandon";
                case 12:
                    return "isMultiPackage";
                case 13:
                    return "getChildSessionIds";
                case 14:
                    return "addChildSessionId";
                case 15:
                    return "removeChildSessionId";
                case 16:
                    return "getParentSessionId";
                case 17:
                    return "isStaged";
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
                boolean z = false;
                boolean _result;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        setClientProgress(data.readFloat());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        addClientProgress(data.readFloat());
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        String[] _result2 = getNames();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result2);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        ParcelFileDescriptor _result3 = openWrite(data.readString(), data.readLong(), data.readLong());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        ParcelFileDescriptor _result4 = openRead(data.readString());
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 6:
                        ParcelFileDescriptor _arg3;
                        parcel.enforceInterface(descriptor);
                        String _arg0 = data.readString();
                        long _arg1 = data.readLong();
                        long _arg2 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg3 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        write(_arg0, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        removeSplit(data.readString());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        close();
                        reply.writeNoException();
                        return true;
                    case 9:
                        IntentSender _arg02;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (IntentSender) IntentSender.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        commit(_arg02, z);
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        transfer(data.readString());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        abandon();
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _result = isMultiPackage();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        int[] _result5 = getChildSessionIds();
                        reply.writeNoException();
                        parcel2.writeIntArray(_result5);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        addChildSessionId(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        removeChildSessionId(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        int _result6 = getParentSessionId();
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _result = isStaged();
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

        public static boolean setDefaultImpl(IPackageInstallerSession impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPackageInstallerSession getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void abandon() throws RemoteException;

    void addChildSessionId(int i) throws RemoteException;

    void addClientProgress(float f) throws RemoteException;

    void close() throws RemoteException;

    void commit(IntentSender intentSender, boolean z) throws RemoteException;

    int[] getChildSessionIds() throws RemoteException;

    String[] getNames() throws RemoteException;

    int getParentSessionId() throws RemoteException;

    boolean isMultiPackage() throws RemoteException;

    boolean isStaged() throws RemoteException;

    ParcelFileDescriptor openRead(String str) throws RemoteException;

    ParcelFileDescriptor openWrite(String str, long j, long j2) throws RemoteException;

    void removeChildSessionId(int i) throws RemoteException;

    void removeSplit(String str) throws RemoteException;

    void setClientProgress(float f) throws RemoteException;

    void transfer(String str) throws RemoteException;

    void write(String str, long j, long j2, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;
}
