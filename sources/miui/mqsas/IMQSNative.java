package miui.mqsas;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.util.List;

public interface IMQSNative extends IInterface {

    public static class Default implements IMQSNative {
        public void captureLog(String type, String headline, List<String> list, List<String> list2, boolean offline, int id, boolean upload, String where, List<String> list3) throws RemoteException {
        }

        public ParcelFileDescriptor attachSockFilter(String srcMac, String dstMac, int srcIp, int dstIp, int proto, int srcPort, int dstPort, byte[] identifyData, int retDataLen, String iface) throws RemoteException {
            return null;
        }

        public void setCoreFilter(int pid, boolean isFull) throws RemoteException {
        }

        public int runCommand(String action, String params, int timeout) throws RemoteException {
            return 0;
        }

        public String getFreeFragStats() throws RemoteException {
            return null;
        }

        public int createFileInPersist(String path) throws RemoteException {
            return 0;
        }

        public void writeToPersistFile(String path, String buff) throws RemoteException {
        }

        public String readFile(String path) throws RemoteException {
            return null;
        }

        public String getFilesFromPersist() throws RemoteException {
            return null;
        }

        public void defragDataPartition() throws RemoteException {
        }

        public void stopDefrag() throws RemoteException {
        }

        public ParcelFileDescriptor getPSISockFd() throws RemoteException {
            return null;
        }

        public void addPSITriggerCommand(int callbackKey, String node, String command) throws RemoteException {
        }

        public void flashDebugPolicy(int type) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMQSNative {
        private static final String DESCRIPTOR = "miui.mqsas.IMQSNative";
        static final int TRANSACTION_addPSITriggerCommand = 13;
        static final int TRANSACTION_attachSockFilter = 2;
        static final int TRANSACTION_captureLog = 1;
        static final int TRANSACTION_createFileInPersist = 6;
        static final int TRANSACTION_defragDataPartition = 10;
        static final int TRANSACTION_flashDebugPolicy = 14;
        static final int TRANSACTION_getFilesFromPersist = 9;
        static final int TRANSACTION_getFreeFragStats = 5;
        static final int TRANSACTION_getPSISockFd = 12;
        static final int TRANSACTION_readFile = 8;
        static final int TRANSACTION_runCommand = 4;
        static final int TRANSACTION_setCoreFilter = 3;
        static final int TRANSACTION_stopDefrag = 11;
        static final int TRANSACTION_writeToPersistFile = 7;

        private static class Proxy implements IMQSNative {
            public static IMQSNative sDefaultImpl;
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

            public void captureLog(String type, String headline, List<String> actions, List<String> parmas, boolean offline, int id, boolean upload, String where, List<String> includeFiles) throws RemoteException {
                Throwable th;
                List<String> list;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(type);
                        try {
                            _data.writeString(headline);
                        } catch (Throwable th2) {
                            th = th2;
                            list = actions;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeStringList(actions);
                            _data.writeStringList(parmas);
                            _data.writeInt(offline ? 1 : 0);
                            _data.writeInt(id);
                            _data.writeInt(upload ? 1 : 0);
                            _data.writeString(where);
                            _data.writeStringList(includeFiles);
                            if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().captureLog(type, headline, actions, parmas, offline, id, upload, where, includeFiles);
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
                        str = headline;
                        list = actions;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    String str2 = type;
                    str = headline;
                    list = actions;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public ParcelFileDescriptor attachSockFilter(String srcMac, String dstMac, int srcIp, int dstIp, int proto, int srcPort, int dstPort, byte[] identifyData, int retDataLen, String iface) throws RemoteException {
                Throwable th;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(srcMac);
                        try {
                            _data.writeString(dstMac);
                            _data.writeInt(srcIp);
                            _data.writeInt(dstIp);
                            _data.writeInt(proto);
                            _data.writeInt(srcPort);
                            _data.writeInt(dstPort);
                            _data.writeByteArray(identifyData);
                            _data.writeInt(retDataLen);
                            _data.writeString(iface);
                            ParcelFileDescriptor _result;
                            if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    _result = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                                } else {
                                    _result = null;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().attachSockFilter(srcMac, dstMac, srcIp, dstIp, proto, srcPort, dstPort, identifyData, retDataLen, iface);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str = dstMac;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String str2 = srcMac;
                    str = dstMac;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void setCoreFilter(int pid, boolean isFull) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(isFull ? 1 : 0);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCoreFilter(pid, isFull);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int runCommand(String action, String params, int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeString(params);
                    _data.writeInt(timeout);
                    int i = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().runCommand(action, params, timeout);
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

            public String getFreeFragStats() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getFreeFragStats();
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

            public int createFileInPersist(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    int i = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().createFileInPersist(path);
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

            public void writeToPersistFile(String path, String buff) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeString(buff);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().writeToPersistFile(path, buff);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String readFile(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    String str = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().readFile(path);
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

            public String getFilesFromPersist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getFilesFromPersist();
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

            public void defragDataPartition() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().defragDataPartition();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopDefrag() throws RemoteException {
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
                    Stub.getDefaultImpl().stopDefrag();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor getPSISockFd() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParcelFileDescriptor parcelFileDescriptor = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().getPSISockFd();
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

            public void addPSITriggerCommand(int callbackKey, String node, String command) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callbackKey);
                    _data.writeString(node);
                    _data.writeString(command);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPSITriggerCommand(callbackKey, node, command);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void flashDebugPolicy(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().flashDebugPolicy(type);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMQSNative asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMQSNative)) {
                return new Proxy(obj);
            }
            return (IMQSNative) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "captureLog";
                case 2:
                    return "attachSockFilter";
                case 3:
                    return "setCoreFilter";
                case 4:
                    return "runCommand";
                case 5:
                    return "getFreeFragStats";
                case 6:
                    return "createFileInPersist";
                case 7:
                    return "writeToPersistFile";
                case 8:
                    return "readFile";
                case 9:
                    return "getFilesFromPersist";
                case 10:
                    return "defragDataPartition";
                case 11:
                    return "stopDefrag";
                case 12:
                    return "getPSISockFd";
                case 13:
                    return "addPSITriggerCommand";
                case 14:
                    return "flashDebugPolicy";
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
            boolean z;
            if (i != 1598968902) {
                boolean z2 = false;
                ParcelFileDescriptor _result;
                String _result2;
                switch (i) {
                    case 1:
                        boolean z3 = false;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        captureLog(data.readString(), data.readString(), data.createStringArrayList(), data.createStringArrayList(), data.readInt() != 0 ? z : z3, data.readInt(), data.readInt() != 0 ? z : z3, data.readString(), data.createStringArrayList());
                        reply.writeNoException();
                        return z;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        int i2 = 0;
                        i2 = 1;
                        _result = attachSockFilter(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.createByteArray(), data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(i2);
                            _result.writeToParcel(parcel2, i2);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i2;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        setCoreFilter(_arg0, z2);
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        int _result3 = runCommand(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result2 = getFreeFragStats();
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        int _result4 = createFileInPersist(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        writeToPersistFile(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        String _result5 = readFile(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result5);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _result2 = getFilesFromPersist();
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        defragDataPartition();
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        stopDefrag();
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _result = getPSISockFd();
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        addPSITriggerCommand(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        flashDebugPolicy(data.readInt());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            z = true;
            parcel2.writeString(descriptor);
            return z;
        }

        public static boolean setDefaultImpl(IMQSNative impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMQSNative getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addPSITriggerCommand(int i, String str, String str2) throws RemoteException;

    ParcelFileDescriptor attachSockFilter(String str, String str2, int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, String str3) throws RemoteException;

    void captureLog(String str, String str2, List<String> list, List<String> list2, boolean z, int i, boolean z2, String str3, List<String> list3) throws RemoteException;

    int createFileInPersist(String str) throws RemoteException;

    void defragDataPartition() throws RemoteException;

    void flashDebugPolicy(int i) throws RemoteException;

    String getFilesFromPersist() throws RemoteException;

    String getFreeFragStats() throws RemoteException;

    ParcelFileDescriptor getPSISockFd() throws RemoteException;

    String readFile(String str) throws RemoteException;

    int runCommand(String str, String str2, int i) throws RemoteException;

    void setCoreFilter(int i, boolean z) throws RemoteException;

    void stopDefrag() throws RemoteException;

    void writeToPersistFile(String str, String str2) throws RemoteException;
}
