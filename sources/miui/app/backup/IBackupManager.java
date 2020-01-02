package miui.app.backup;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.util.List;

public interface IBackupManager extends IInterface {

    public static class Default implements IBackupManager {
        public void backupPackage(ParcelFileDescriptor outFileDescriptor, ParcelFileDescriptor readSide, String pkg, int feature, String pwd, String encryptedPwd, boolean includeApk, boolean forceBackup, boolean shouldSkipData, IPackageBackupRestoreObserver observer) throws RemoteException {
        }

        public void startConfirmationUi(int token, String action) throws RemoteException {
        }

        public void restoreFile(ParcelFileDescriptor bakFd, String pwd, boolean forceBackup, IPackageBackupRestoreObserver observer) throws RemoteException {
        }

        public boolean acquire(IBackupServiceStateObserver stateObserver, IBinder iCaller) throws RemoteException {
            return false;
        }

        public void release(IBackupServiceStateObserver stateObserver) throws RemoteException {
        }

        public void setFutureTask(List<String> list) throws RemoteException {
        }

        public void errorOccur(int err) throws RemoteException {
        }

        public String getCurrentRunningPackage() throws RemoteException {
            return null;
        }

        public int getCurrentWorkingFeature() throws RemoteException {
            return 0;
        }

        public int getState() throws RemoteException {
            return 0;
        }

        public void writeMiuiBackupHeader(ParcelFileDescriptor outFileDescriptor) throws RemoteException {
        }

        public void readMiuiBackupHeader(ParcelFileDescriptor inFileDescriptor) throws RemoteException {
        }

        public void onApkInstalled() throws RemoteException {
        }

        public void addCompletedSize(long size) throws RemoteException {
        }

        public boolean isNeedBeKilled(String pkg) throws RemoteException {
            return false;
        }

        public void setIsNeedBeKilled(String pkg, boolean isNeedBeKilled) throws RemoteException {
        }

        public boolean isRunningFromMiui(int fd) throws RemoteException {
            return false;
        }

        public boolean isServiceIdle() throws RemoteException {
            return false;
        }

        public void setCustomProgress(int progType, int prog, int total) throws RemoteException {
        }

        public int getBackupTimeoutScale() throws RemoteException {
            return 0;
        }

        public boolean shouldSkipData() throws RemoteException {
            return false;
        }

        public void delCacheBackup() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBackupManager {
        private static final String DESCRIPTOR = "miui.app.backup.IBackupManager";
        static final int TRANSACTION_acquire = 4;
        static final int TRANSACTION_addCompletedSize = 14;
        static final int TRANSACTION_backupPackage = 1;
        static final int TRANSACTION_delCacheBackup = 22;
        static final int TRANSACTION_errorOccur = 7;
        static final int TRANSACTION_getBackupTimeoutScale = 20;
        static final int TRANSACTION_getCurrentRunningPackage = 8;
        static final int TRANSACTION_getCurrentWorkingFeature = 9;
        static final int TRANSACTION_getState = 10;
        static final int TRANSACTION_isNeedBeKilled = 15;
        static final int TRANSACTION_isRunningFromMiui = 17;
        static final int TRANSACTION_isServiceIdle = 18;
        static final int TRANSACTION_onApkInstalled = 13;
        static final int TRANSACTION_readMiuiBackupHeader = 12;
        static final int TRANSACTION_release = 5;
        static final int TRANSACTION_restoreFile = 3;
        static final int TRANSACTION_setCustomProgress = 19;
        static final int TRANSACTION_setFutureTask = 6;
        static final int TRANSACTION_setIsNeedBeKilled = 16;
        static final int TRANSACTION_shouldSkipData = 21;
        static final int TRANSACTION_startConfirmationUi = 2;
        static final int TRANSACTION_writeMiuiBackupHeader = 11;

        private static class Proxy implements IBackupManager {
            public static IBackupManager sDefaultImpl;
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

            public void backupPackage(ParcelFileDescriptor outFileDescriptor, ParcelFileDescriptor readSide, String pkg, int feature, String pwd, String encryptedPwd, boolean includeApk, boolean forceBackup, boolean shouldSkipData, IPackageBackupRestoreObserver observer) throws RemoteException {
                ParcelFileDescriptor parcelFileDescriptor = outFileDescriptor;
                ParcelFileDescriptor parcelFileDescriptor2 = readSide;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (parcelFileDescriptor != null) {
                        _data.writeInt(1);
                        parcelFileDescriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (parcelFileDescriptor2 != null) {
                        _data.writeInt(1);
                        parcelFileDescriptor2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(pkg);
                    _data.writeInt(feature);
                    _data.writeString(pwd);
                    _data.writeString(encryptedPwd);
                    _data.writeInt(includeApk ? 1 : 0);
                    _data.writeInt(forceBackup ? 1 : 0);
                    _data.writeInt(shouldSkipData ? 1 : 0);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().backupPackage(outFileDescriptor, readSide, pkg, feature, pwd, encryptedPwd, includeApk, forceBackup, shouldSkipData, observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startConfirmationUi(int token, String action) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeString(action);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startConfirmationUi(token, action);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restoreFile(ParcelFileDescriptor bakFd, String pwd, boolean forceBackup, IPackageBackupRestoreObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (bakFd != null) {
                        _data.writeInt(1);
                        bakFd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(pwd);
                    if (!forceBackup) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restoreFile(bakFd, pwd, forceBackup, observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean acquire(IBackupServiceStateObserver stateObserver, IBinder iCaller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(stateObserver != null ? stateObserver.asBinder() : null);
                    _data.writeStrongBinder(iCaller);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().acquire(stateObserver, iCaller);
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

            public void release(IBackupServiceStateObserver stateObserver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(stateObserver != null ? stateObserver.asBinder() : null);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().release(stateObserver);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFutureTask(List<String> packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packages);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFutureTask(packages);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void errorOccur(int err) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(err);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().errorOccur(err);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurrentRunningPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCurrentRunningPackage();
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

            public int getCurrentWorkingFeature() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCurrentWorkingFeature();
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

            public int getState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getState();
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

            public void writeMiuiBackupHeader(ParcelFileDescriptor outFileDescriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (outFileDescriptor != null) {
                        _data.writeInt(1);
                        outFileDescriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().writeMiuiBackupHeader(outFileDescriptor);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void readMiuiBackupHeader(ParcelFileDescriptor inFileDescriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (inFileDescriptor != null) {
                        _data.writeInt(1);
                        inFileDescriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().readMiuiBackupHeader(inFileDescriptor);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onApkInstalled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onApkInstalled();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addCompletedSize(long size) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(size);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addCompletedSize(size);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isNeedBeKilled(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isNeedBeKilled(pkg);
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

            public void setIsNeedBeKilled(String pkg, boolean isNeedBeKilled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(isNeedBeKilled ? 1 : 0);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIsNeedBeKilled(pkg, isNeedBeKilled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRunningFromMiui(int fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fd);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isRunningFromMiui(fd);
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

            public boolean isServiceIdle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isServiceIdle();
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

            public void setCustomProgress(int progType, int prog, int total) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(progType);
                    _data.writeInt(prog);
                    _data.writeInt(total);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCustomProgress(progType, prog, total);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getBackupTimeoutScale() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getBackupTimeoutScale();
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

            public boolean shouldSkipData() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().shouldSkipData();
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

            public void delCacheBackup() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().delCacheBackup();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBackupManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBackupManager)) {
                return new Proxy(obj);
            }
            return (IBackupManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "backupPackage";
                case 2:
                    return "startConfirmationUi";
                case 3:
                    return "restoreFile";
                case 4:
                    return "acquire";
                case 5:
                    return "release";
                case 6:
                    return "setFutureTask";
                case 7:
                    return "errorOccur";
                case 8:
                    return "getCurrentRunningPackage";
                case 9:
                    return "getCurrentWorkingFeature";
                case 10:
                    return "getState";
                case 11:
                    return "writeMiuiBackupHeader";
                case 12:
                    return "readMiuiBackupHeader";
                case 13:
                    return "onApkInstalled";
                case 14:
                    return "addCompletedSize";
                case 15:
                    return "isNeedBeKilled";
                case 16:
                    return "setIsNeedBeKilled";
                case 17:
                    return "isRunningFromMiui";
                case 18:
                    return "isServiceIdle";
                case 19:
                    return "setCustomProgress";
                case 20:
                    return "getBackupTimeoutScale";
                case 21:
                    return "shouldSkipData";
                case 22:
                    return "delCacheBackup";
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
                boolean _arg1 = false;
                int _result;
                ParcelFileDescriptor _arg0;
                boolean _result2;
                switch (i) {
                    case 1:
                        ParcelFileDescriptor _arg02;
                        ParcelFileDescriptor _arg12;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg12 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        backupPackage(_arg02, _arg12, data.readString(), data.readInt(), data.readString(), data.readString(), data.readInt() != 0, data.readInt() != 0, data.readInt() != 0, miui.app.backup.IPackageBackupRestoreObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        startConfirmationUi(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 3:
                        ParcelFileDescriptor _arg03;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        String _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        restoreFile(_arg03, _arg13, _arg1, miui.app.backup.IPackageBackupRestoreObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        boolean _result3 = acquire(miui.app.backup.IBackupServiceStateObserver.Stub.asInterface(data.readStrongBinder()), data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        release(miui.app.backup.IBackupServiceStateObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        setFutureTask(data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        errorOccur(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        String _result4 = getCurrentRunningPackage();
                        reply.writeNoException();
                        parcel2.writeString(_result4);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _result = getCurrentWorkingFeature();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _result = getState();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        writeMiuiBackupHeader(_arg0);
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        readMiuiBackupHeader(_arg0);
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        onApkInstalled();
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        addCompletedSize(data.readLong());
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _result2 = isNeedBeKilled(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        String _arg04 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setIsNeedBeKilled(_arg04, _arg1);
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _result2 = isRunningFromMiui(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isServiceIdle();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        setCustomProgress(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _result = getBackupTimeoutScale();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _arg1 = shouldSkipData();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        delCacheBackup();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IBackupManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBackupManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean acquire(IBackupServiceStateObserver iBackupServiceStateObserver, IBinder iBinder) throws RemoteException;

    void addCompletedSize(long j) throws RemoteException;

    void backupPackage(ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, String str, int i, String str2, String str3, boolean z, boolean z2, boolean z3, IPackageBackupRestoreObserver iPackageBackupRestoreObserver) throws RemoteException;

    void delCacheBackup() throws RemoteException;

    void errorOccur(int i) throws RemoteException;

    int getBackupTimeoutScale() throws RemoteException;

    String getCurrentRunningPackage() throws RemoteException;

    int getCurrentWorkingFeature() throws RemoteException;

    int getState() throws RemoteException;

    boolean isNeedBeKilled(String str) throws RemoteException;

    boolean isRunningFromMiui(int i) throws RemoteException;

    boolean isServiceIdle() throws RemoteException;

    void onApkInstalled() throws RemoteException;

    void readMiuiBackupHeader(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void release(IBackupServiceStateObserver iBackupServiceStateObserver) throws RemoteException;

    void restoreFile(ParcelFileDescriptor parcelFileDescriptor, String str, boolean z, IPackageBackupRestoreObserver iPackageBackupRestoreObserver) throws RemoteException;

    void setCustomProgress(int i, int i2, int i3) throws RemoteException;

    void setFutureTask(List<String> list) throws RemoteException;

    void setIsNeedBeKilled(String str, boolean z) throws RemoteException;

    boolean shouldSkipData() throws RemoteException;

    void startConfirmationUi(int i, String str) throws RemoteException;

    void writeMiuiBackupHeader(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;
}
