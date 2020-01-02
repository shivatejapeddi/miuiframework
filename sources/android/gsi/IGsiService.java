package android.gsi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

public interface IGsiService extends IInterface {
    public static final int BOOT_STATUS_DISABLED = 1;
    public static final int BOOT_STATUS_ENABLED = 3;
    public static final int BOOT_STATUS_NOT_INSTALLED = 0;
    public static final int BOOT_STATUS_SINGLE_BOOT = 2;
    public static final int BOOT_STATUS_WILL_WIPE = 4;
    public static final int INSTALL_ERROR_FILE_SYSTEM_CLUTTERED = 3;
    public static final int INSTALL_ERROR_GENERIC = 1;
    public static final int INSTALL_ERROR_NO_SPACE = 2;
    public static final int INSTALL_OK = 0;
    public static final int STATUS_COMPLETE = 2;
    public static final int STATUS_NO_OPERATION = 0;
    public static final int STATUS_WORKING = 1;

    public static class Default implements IGsiService {
        public int startGsiInstall(long gsiSize, long userdataSize, boolean wipeUserdata) throws RemoteException {
            return 0;
        }

        public boolean commitGsiChunkFromStream(ParcelFileDescriptor stream, long bytes) throws RemoteException {
            return false;
        }

        public GsiProgress getInstallProgress() throws RemoteException {
            return null;
        }

        public boolean commitGsiChunkFromMemory(byte[] bytes) throws RemoteException {
            return false;
        }

        public int setGsiBootable(boolean oneShot) throws RemoteException {
            return 0;
        }

        public boolean isGsiEnabled() throws RemoteException {
            return false;
        }

        public boolean cancelGsiInstall() throws RemoteException {
            return false;
        }

        public boolean isGsiInstallInProgress() throws RemoteException {
            return false;
        }

        public boolean removeGsiInstall() throws RemoteException {
            return false;
        }

        public boolean disableGsiInstall() throws RemoteException {
            return false;
        }

        public long getUserdataImageSize() throws RemoteException {
            return 0;
        }

        public boolean isGsiRunning() throws RemoteException {
            return false;
        }

        public boolean isGsiInstalled() throws RemoteException {
            return false;
        }

        public int getGsiBootStatus() throws RemoteException {
            return 0;
        }

        public String getInstalledGsiImageDir() throws RemoteException {
            return null;
        }

        public int beginGsiInstall(GsiInstallParams params) throws RemoteException {
            return 0;
        }

        public int wipeGsiUserdata() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IGsiService {
        private static final String DESCRIPTOR = "android.gsi.IGsiService";
        static final int TRANSACTION_beginGsiInstall = 16;
        static final int TRANSACTION_cancelGsiInstall = 7;
        static final int TRANSACTION_commitGsiChunkFromMemory = 4;
        static final int TRANSACTION_commitGsiChunkFromStream = 2;
        static final int TRANSACTION_disableGsiInstall = 10;
        static final int TRANSACTION_getGsiBootStatus = 14;
        static final int TRANSACTION_getInstallProgress = 3;
        static final int TRANSACTION_getInstalledGsiImageDir = 15;
        static final int TRANSACTION_getUserdataImageSize = 11;
        static final int TRANSACTION_isGsiEnabled = 6;
        static final int TRANSACTION_isGsiInstallInProgress = 8;
        static final int TRANSACTION_isGsiInstalled = 13;
        static final int TRANSACTION_isGsiRunning = 12;
        static final int TRANSACTION_removeGsiInstall = 9;
        static final int TRANSACTION_setGsiBootable = 5;
        static final int TRANSACTION_startGsiInstall = 1;
        static final int TRANSACTION_wipeGsiUserdata = 17;

        private static class Proxy implements IGsiService {
            public static IGsiService sDefaultImpl;
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

            public int startGsiInstall(long gsiSize, long userdataSize, boolean wipeUserdata) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(gsiSize);
                    _data.writeLong(userdataSize);
                    int i = 0;
                    _data.writeInt(wipeUserdata ? 1 : 0);
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().startGsiInstall(gsiSize, userdataSize, wipeUserdata);
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

            public boolean commitGsiChunkFromStream(ParcelFileDescriptor stream, long bytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (stream != null) {
                        _data.writeInt(1);
                        stream.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(bytes);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().commitGsiChunkFromStream(stream, bytes);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public GsiProgress getInstallProgress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    GsiProgress gsiProgress = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        gsiProgress = Stub.getDefaultImpl();
                        if (gsiProgress != 0) {
                            gsiProgress = Stub.getDefaultImpl().getInstallProgress();
                            return gsiProgress;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        gsiProgress = (GsiProgress) GsiProgress.CREATOR.createFromParcel(_reply);
                    } else {
                        gsiProgress = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return gsiProgress;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean commitGsiChunkFromMemory(byte[] bytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(bytes);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().commitGsiChunkFromMemory(bytes);
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

            public int setGsiBootable(boolean oneShot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(oneShot ? 1 : 0);
                    int i = this.mRemote;
                    if (!i.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().setGsiBootable(oneShot);
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

            public boolean isGsiEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isGsiEnabled();
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

            public boolean cancelGsiInstall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().cancelGsiInstall();
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

            public boolean isGsiInstallInProgress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isGsiInstallInProgress();
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

            public boolean removeGsiInstall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeGsiInstall();
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

            public boolean disableGsiInstall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().disableGsiInstall();
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

            public long getUserdataImageSize() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getUserdataImageSize();
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

            public boolean isGsiRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isGsiRunning();
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

            public boolean isGsiInstalled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isGsiInstalled();
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

            public int getGsiBootStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getGsiBootStatus();
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

            public String getInstalledGsiImageDir() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getInstalledGsiImageDir();
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

            public int beginGsiInstall(GsiInstallParams params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().beginGsiInstall(params);
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

            public int wipeGsiUserdata() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().wipeGsiUserdata();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGsiService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGsiService)) {
                return new Proxy(obj);
            }
            return (IGsiService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "startGsiInstall";
                case 2:
                    return "commitGsiChunkFromStream";
                case 3:
                    return "getInstallProgress";
                case 4:
                    return "commitGsiChunkFromMemory";
                case 5:
                    return "setGsiBootable";
                case 6:
                    return "isGsiEnabled";
                case 7:
                    return "cancelGsiInstall";
                case 8:
                    return "isGsiInstallInProgress";
                case 9:
                    return "removeGsiInstall";
                case 10:
                    return "disableGsiInstall";
                case 11:
                    return "getUserdataImageSize";
                case 12:
                    return "isGsiRunning";
                case 13:
                    return "isGsiInstalled";
                case 14:
                    return "getGsiBootStatus";
                case 15:
                    return "getInstalledGsiImageDir";
                case 16:
                    return "beginGsiInstall";
                case 17:
                    return "wipeGsiUserdata";
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
                boolean _arg0 = false;
                int _result;
                int _result2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _result = startGsiInstall(data.readLong(), data.readLong(), data.readInt() != 0);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 2:
                        ParcelFileDescriptor _arg02;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        boolean _result3 = commitGsiChunkFromStream(_arg02, data.readLong());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        GsiProgress _result4 = getInstallProgress();
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        boolean _result5 = commitGsiChunkFromMemory(data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result2 = setGsiBootable(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isGsiEnabled();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _arg0 = cancelGsiInstall();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isGsiInstallInProgress();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _arg0 = removeGsiInstall();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _arg0 = disableGsiInstall();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        long _result6 = getUserdataImageSize();
                        reply.writeNoException();
                        parcel2.writeLong(_result6);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isGsiRunning();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isGsiInstalled();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _result = getGsiBootStatus();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        String _result7 = getInstalledGsiImageDir();
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return true;
                    case 16:
                        GsiInstallParams _arg03;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (GsiInstallParams) GsiInstallParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _result2 = beginGsiInstall(_arg03);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _result = wipeGsiUserdata();
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

        public static boolean setDefaultImpl(IGsiService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IGsiService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int beginGsiInstall(GsiInstallParams gsiInstallParams) throws RemoteException;

    boolean cancelGsiInstall() throws RemoteException;

    boolean commitGsiChunkFromMemory(byte[] bArr) throws RemoteException;

    boolean commitGsiChunkFromStream(ParcelFileDescriptor parcelFileDescriptor, long j) throws RemoteException;

    boolean disableGsiInstall() throws RemoteException;

    int getGsiBootStatus() throws RemoteException;

    GsiProgress getInstallProgress() throws RemoteException;

    String getInstalledGsiImageDir() throws RemoteException;

    long getUserdataImageSize() throws RemoteException;

    boolean isGsiEnabled() throws RemoteException;

    boolean isGsiInstallInProgress() throws RemoteException;

    boolean isGsiInstalled() throws RemoteException;

    boolean isGsiRunning() throws RemoteException;

    boolean removeGsiInstall() throws RemoteException;

    int setGsiBootable(boolean z) throws RemoteException;

    int startGsiInstall(long j, long j2, boolean z) throws RemoteException;

    int wipeGsiUserdata() throws RemoteException;
}
