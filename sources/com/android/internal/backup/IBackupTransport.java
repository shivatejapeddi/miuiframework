package com.android.internal.backup;

import android.app.backup.RestoreDescription;
import android.app.backup.RestoreSet;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.text.TextUtils;

public interface IBackupTransport extends IInterface {

    public static class Default implements IBackupTransport {
        public String name() throws RemoteException {
            return null;
        }

        public Intent configurationIntent() throws RemoteException {
            return null;
        }

        public String currentDestinationString() throws RemoteException {
            return null;
        }

        public Intent dataManagementIntent() throws RemoteException {
            return null;
        }

        public CharSequence dataManagementIntentLabel() throws RemoteException {
            return null;
        }

        public String transportDirName() throws RemoteException {
            return null;
        }

        public long requestBackupTime() throws RemoteException {
            return 0;
        }

        public int initializeDevice() throws RemoteException {
            return 0;
        }

        public int performBackup(PackageInfo packageInfo, ParcelFileDescriptor inFd, int flags) throws RemoteException {
            return 0;
        }

        public int clearBackupData(PackageInfo packageInfo) throws RemoteException {
            return 0;
        }

        public int finishBackup() throws RemoteException {
            return 0;
        }

        public RestoreSet[] getAvailableRestoreSets() throws RemoteException {
            return null;
        }

        public long getCurrentRestoreSet() throws RemoteException {
            return 0;
        }

        public int startRestore(long token, PackageInfo[] packages) throws RemoteException {
            return 0;
        }

        public RestoreDescription nextRestorePackage() throws RemoteException {
            return null;
        }

        public int getRestoreData(ParcelFileDescriptor outFd) throws RemoteException {
            return 0;
        }

        public void finishRestore() throws RemoteException {
        }

        public long requestFullBackupTime() throws RemoteException {
            return 0;
        }

        public int performFullBackup(PackageInfo targetPackage, ParcelFileDescriptor socket, int flags) throws RemoteException {
            return 0;
        }

        public int checkFullBackupSize(long size) throws RemoteException {
            return 0;
        }

        public int sendBackupData(int numBytes) throws RemoteException {
            return 0;
        }

        public void cancelFullBackup() throws RemoteException {
        }

        public boolean isAppEligibleForBackup(PackageInfo targetPackage, boolean isFullBackup) throws RemoteException {
            return false;
        }

        public long getBackupQuota(String packageName, boolean isFullBackup) throws RemoteException {
            return 0;
        }

        public int getNextFullRestoreDataChunk(ParcelFileDescriptor socket) throws RemoteException {
            return 0;
        }

        public int abortFullRestore() throws RemoteException {
            return 0;
        }

        public int getTransportFlags() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBackupTransport {
        private static final String DESCRIPTOR = "com.android.internal.backup.IBackupTransport";
        static final int TRANSACTION_abortFullRestore = 26;
        static final int TRANSACTION_cancelFullBackup = 22;
        static final int TRANSACTION_checkFullBackupSize = 20;
        static final int TRANSACTION_clearBackupData = 10;
        static final int TRANSACTION_configurationIntent = 2;
        static final int TRANSACTION_currentDestinationString = 3;
        static final int TRANSACTION_dataManagementIntent = 4;
        static final int TRANSACTION_dataManagementIntentLabel = 5;
        static final int TRANSACTION_finishBackup = 11;
        static final int TRANSACTION_finishRestore = 17;
        static final int TRANSACTION_getAvailableRestoreSets = 12;
        static final int TRANSACTION_getBackupQuota = 24;
        static final int TRANSACTION_getCurrentRestoreSet = 13;
        static final int TRANSACTION_getNextFullRestoreDataChunk = 25;
        static final int TRANSACTION_getRestoreData = 16;
        static final int TRANSACTION_getTransportFlags = 27;
        static final int TRANSACTION_initializeDevice = 8;
        static final int TRANSACTION_isAppEligibleForBackup = 23;
        static final int TRANSACTION_name = 1;
        static final int TRANSACTION_nextRestorePackage = 15;
        static final int TRANSACTION_performBackup = 9;
        static final int TRANSACTION_performFullBackup = 19;
        static final int TRANSACTION_requestBackupTime = 7;
        static final int TRANSACTION_requestFullBackupTime = 18;
        static final int TRANSACTION_sendBackupData = 21;
        static final int TRANSACTION_startRestore = 14;
        static final int TRANSACTION_transportDirName = 6;

        private static class Proxy implements IBackupTransport {
            public static IBackupTransport sDefaultImpl;
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

            public String name() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().name();
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

            public Intent configurationIntent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Intent intent = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        intent = Stub.getDefaultImpl();
                        if (intent != 0) {
                            intent = Stub.getDefaultImpl().configurationIntent();
                            return intent;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        intent = (Intent) Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        intent = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return intent;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String currentDestinationString() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().currentDestinationString();
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

            public Intent dataManagementIntent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Intent intent = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        intent = Stub.getDefaultImpl();
                        if (intent != 0) {
                            intent = Stub.getDefaultImpl().dataManagementIntent();
                            return intent;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        intent = (Intent) Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        intent = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return intent;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CharSequence dataManagementIntentLabel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    CharSequence charSequence = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != 0) {
                            charSequence = Stub.getDefaultImpl().dataManagementIntentLabel();
                            return charSequence;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        charSequence = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(_reply);
                    } else {
                        charSequence = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return charSequence;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String transportDirName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().transportDirName();
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

            public long requestBackupTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().requestBackupTime();
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

            public int initializeDevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().initializeDevice();
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

            public int performBackup(PackageInfo packageInfo, ParcelFileDescriptor inFd, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (packageInfo != null) {
                        _data.writeInt(1);
                        packageInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (inFd != null) {
                        _data.writeInt(1);
                        inFd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().performBackup(packageInfo, inFd, flags);
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

            public int clearBackupData(PackageInfo packageInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (packageInfo != null) {
                        _data.writeInt(1);
                        packageInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().clearBackupData(packageInfo);
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

            public int finishBackup() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().finishBackup();
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

            public RestoreSet[] getAvailableRestoreSets() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    RestoreSet[] restoreSetArr = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        restoreSetArr = Stub.getDefaultImpl();
                        if (restoreSetArr != 0) {
                            restoreSetArr = Stub.getDefaultImpl().getAvailableRestoreSets();
                            return restoreSetArr;
                        }
                    }
                    _reply.readException();
                    RestoreSet[] _result = (RestoreSet[]) _reply.createTypedArray(RestoreSet.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getCurrentRestoreSet() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getCurrentRestoreSet();
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

            public int startRestore(long token, PackageInfo[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(token);
                    _data.writeTypedArray(packages, 0);
                    int i = this.mRemote;
                    if (!i.transact(14, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().startRestore(token, packages);
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

            public RestoreDescription nextRestorePackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    RestoreDescription restoreDescription = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        restoreDescription = Stub.getDefaultImpl();
                        if (restoreDescription != 0) {
                            restoreDescription = Stub.getDefaultImpl().nextRestorePackage();
                            return restoreDescription;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        restoreDescription = (RestoreDescription) RestoreDescription.CREATOR.createFromParcel(_reply);
                    } else {
                        restoreDescription = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return restoreDescription;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRestoreData(ParcelFileDescriptor outFd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (outFd != null) {
                        _data.writeInt(1);
                        outFd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getRestoreData(outFd);
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

            public void finishRestore() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishRestore();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long requestFullBackupTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().requestFullBackupTime();
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

            public int performFullBackup(PackageInfo targetPackage, ParcelFileDescriptor socket, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (targetPackage != null) {
                        _data.writeInt(1);
                        targetPackage.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (socket != null) {
                        _data.writeInt(1);
                        socket.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().performFullBackup(targetPackage, socket, flags);
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

            public int checkFullBackupSize(long size) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(size);
                    int i = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkFullBackupSize(size);
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

            public int sendBackupData(int numBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(numBytes);
                    int i = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().sendBackupData(numBytes);
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

            public void cancelFullBackup() throws RemoteException {
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
                    Stub.getDefaultImpl().cancelFullBackup();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAppEligibleForBackup(PackageInfo targetPackage, boolean isFullBackup) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (targetPackage != null) {
                        _data.writeInt(1);
                        targetPackage.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(isFullBackup ? 1 : 0);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isAppEligibleForBackup(targetPackage, isFullBackup);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getBackupQuota(String packageName, boolean isFullBackup) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(isFullBackup ? 1 : 0);
                    long j = this.mRemote;
                    if (!j.transact(24, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != null) {
                            j = Stub.getDefaultImpl().getBackupQuota(packageName, isFullBackup);
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

            public int getNextFullRestoreDataChunk(ParcelFileDescriptor socket) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (socket != null) {
                        _data.writeInt(1);
                        socket.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(25, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getNextFullRestoreDataChunk(socket);
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

            public int abortFullRestore() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().abortFullRestore();
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

            public int getTransportFlags() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getTransportFlags();
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

        public static IBackupTransport asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBackupTransport)) {
                return new Proxy(obj);
            }
            return (IBackupTransport) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "name";
                case 2:
                    return "configurationIntent";
                case 3:
                    return "currentDestinationString";
                case 4:
                    return "dataManagementIntent";
                case 5:
                    return "dataManagementIntentLabel";
                case 6:
                    return "transportDirName";
                case 7:
                    return "requestBackupTime";
                case 8:
                    return "initializeDevice";
                case 9:
                    return "performBackup";
                case 10:
                    return "clearBackupData";
                case 11:
                    return "finishBackup";
                case 12:
                    return "getAvailableRestoreSets";
                case 13:
                    return "getCurrentRestoreSet";
                case 14:
                    return "startRestore";
                case 15:
                    return "nextRestorePackage";
                case 16:
                    return "getRestoreData";
                case 17:
                    return "finishRestore";
                case 18:
                    return "requestFullBackupTime";
                case 19:
                    return "performFullBackup";
                case 20:
                    return "checkFullBackupSize";
                case 21:
                    return "sendBackupData";
                case 22:
                    return "cancelFullBackup";
                case 23:
                    return "isAppEligibleForBackup";
                case 24:
                    return "getBackupQuota";
                case 25:
                    return "getNextFullRestoreDataChunk";
                case 26:
                    return "abortFullRestore";
                case 27:
                    return "getTransportFlags";
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
                boolean _arg1 = false;
                String _result;
                Intent _result2;
                long _result3;
                int _result4;
                PackageInfo _arg0;
                ParcelFileDescriptor _arg12;
                int _result5;
                int _result6;
                ParcelFileDescriptor _arg02;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _result = name();
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result2 = configurationIntent();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result = currentDestinationString();
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result2 = dataManagementIntent();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        CharSequence _result7 = dataManagementIntentLabel();
                        reply.writeNoException();
                        if (_result7 != null) {
                            reply.writeInt(1);
                            TextUtils.writeToParcel(_result7, reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result = transportDirName();
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result3 = requestBackupTime();
                        reply.writeNoException();
                        reply.writeLong(_result3);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result4 = initializeDevice();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (PackageInfo) PackageInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg12 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        _result5 = performBackup(_arg0, _arg12, data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (PackageInfo) PackageInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result6 = clearBackupData(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        _result4 = finishBackup();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        RestoreSet[] _result8 = getAvailableRestoreSets();
                        reply.writeNoException();
                        reply.writeTypedArray(_result8, 1);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _result3 = getCurrentRestoreSet();
                        reply.writeNoException();
                        reply.writeLong(_result3);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _result5 = startRestore(data.readLong(), (PackageInfo[]) data.createTypedArray(PackageInfo.CREATOR));
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        RestoreDescription _result9 = nextRestorePackage();
                        reply.writeNoException();
                        if (_result9 != null) {
                            reply.writeInt(1);
                            _result9.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result6 = getRestoreData(_arg02);
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        finishRestore();
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        _result3 = requestFullBackupTime();
                        reply.writeNoException();
                        reply.writeLong(_result3);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (PackageInfo) PackageInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg12 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        _result5 = performFullBackup(_arg0, _arg12, data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        _result4 = checkFullBackupSize(data.readLong());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        _result6 = sendBackupData(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        cancelFullBackup();
                        reply.writeNoException();
                        return true;
                    case 23:
                        PackageInfo _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (PackageInfo) PackageInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        boolean _result10 = isAppEligibleForBackup(_arg03, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result10);
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        String _arg04 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        long _result11 = getBackupQuota(_arg04, _arg1);
                        reply.writeNoException();
                        reply.writeLong(_result11);
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result6 = getNextFullRestoreDataChunk(_arg02);
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        _result4 = abortFullRestore();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        _result4 = getTransportFlags();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IBackupTransport impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBackupTransport getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int abortFullRestore() throws RemoteException;

    void cancelFullBackup() throws RemoteException;

    int checkFullBackupSize(long j) throws RemoteException;

    int clearBackupData(PackageInfo packageInfo) throws RemoteException;

    Intent configurationIntent() throws RemoteException;

    String currentDestinationString() throws RemoteException;

    Intent dataManagementIntent() throws RemoteException;

    CharSequence dataManagementIntentLabel() throws RemoteException;

    int finishBackup() throws RemoteException;

    void finishRestore() throws RemoteException;

    RestoreSet[] getAvailableRestoreSets() throws RemoteException;

    long getBackupQuota(String str, boolean z) throws RemoteException;

    long getCurrentRestoreSet() throws RemoteException;

    int getNextFullRestoreDataChunk(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    int getRestoreData(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    int getTransportFlags() throws RemoteException;

    int initializeDevice() throws RemoteException;

    boolean isAppEligibleForBackup(PackageInfo packageInfo, boolean z) throws RemoteException;

    String name() throws RemoteException;

    RestoreDescription nextRestorePackage() throws RemoteException;

    int performBackup(PackageInfo packageInfo, ParcelFileDescriptor parcelFileDescriptor, int i) throws RemoteException;

    int performFullBackup(PackageInfo packageInfo, ParcelFileDescriptor parcelFileDescriptor, int i) throws RemoteException;

    long requestBackupTime() throws RemoteException;

    long requestFullBackupTime() throws RemoteException;

    int sendBackupData(int i) throws RemoteException;

    int startRestore(long j, PackageInfo[] packageInfoArr) throws RemoteException;

    String transportDirName() throws RemoteException;
}
