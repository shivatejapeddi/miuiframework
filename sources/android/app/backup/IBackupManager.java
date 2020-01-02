package android.app.backup;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.TextUtils;

public interface IBackupManager extends IInterface {

    public static class Default implements IBackupManager {
        public void dataChangedForUser(int userId, String packageName) throws RemoteException {
        }

        public void dataChanged(String packageName) throws RemoteException {
        }

        public void clearBackupDataForUser(int userId, String transportName, String packageName) throws RemoteException {
        }

        public void clearBackupData(String transportName, String packageName) throws RemoteException {
        }

        public void initializeTransportsForUser(int userId, String[] transportNames, IBackupObserver observer) throws RemoteException {
        }

        public void agentConnectedForUser(int userId, String packageName, IBinder agent) throws RemoteException {
        }

        public void agentConnected(String packageName, IBinder agent) throws RemoteException {
        }

        public void agentDisconnectedForUser(int userId, String packageName) throws RemoteException {
        }

        public void agentDisconnected(String packageName) throws RemoteException {
        }

        public void restoreAtInstallForUser(int userId, String packageName, int token) throws RemoteException {
        }

        public void restoreAtInstall(String packageName, int token) throws RemoteException {
        }

        public void setBackupEnabledForUser(int userId, boolean isEnabled) throws RemoteException {
        }

        public void setBackupEnabled(boolean isEnabled) throws RemoteException {
        }

        public void setAutoRestoreForUser(int userId, boolean doAutoRestore) throws RemoteException {
        }

        public void setAutoRestore(boolean doAutoRestore) throws RemoteException {
        }

        public boolean isBackupEnabledForUser(int userId) throws RemoteException {
            return false;
        }

        public boolean isBackupEnabled() throws RemoteException {
            return false;
        }

        public boolean setBackupPassword(String currentPw, String newPw) throws RemoteException {
            return false;
        }

        public boolean hasBackupPassword() throws RemoteException {
            return false;
        }

        public void backupNowForUser(int userId) throws RemoteException {
        }

        public void backupNow() throws RemoteException {
        }

        public void adbBackup(int userId, ParcelFileDescriptor fd, boolean includeApks, boolean includeObbs, boolean includeShared, boolean doWidgets, boolean allApps, boolean allIncludesSystem, boolean doCompress, boolean doKeyValue, String[] packageNames) throws RemoteException {
        }

        public void fullTransportBackupForUser(int userId, String[] packageNames) throws RemoteException {
        }

        public void adbRestore(int userId, ParcelFileDescriptor fd) throws RemoteException {
        }

        public void acknowledgeFullBackupOrRestoreForUser(int userId, int token, boolean allow, String curPassword, String encryptionPassword, IFullBackupRestoreObserver observer) throws RemoteException {
        }

        public void acknowledgeFullBackupOrRestore(int token, boolean allow, String curPassword, String encryptionPassword, IFullBackupRestoreObserver observer) throws RemoteException {
        }

        public void updateTransportAttributesForUser(int userId, ComponentName transportComponent, String name, Intent configurationIntent, String currentDestinationString, Intent dataManagementIntent, CharSequence dataManagementLabel) throws RemoteException {
        }

        public String getCurrentTransportForUser(int userId) throws RemoteException {
            return null;
        }

        public String getCurrentTransport() throws RemoteException {
            return null;
        }

        public ComponentName getCurrentTransportComponentForUser(int userId) throws RemoteException {
            return null;
        }

        public String[] listAllTransportsForUser(int userId) throws RemoteException {
            return null;
        }

        public String[] listAllTransports() throws RemoteException {
            return null;
        }

        public ComponentName[] listAllTransportComponentsForUser(int userId) throws RemoteException {
            return null;
        }

        public String[] getTransportWhitelist() throws RemoteException {
            return null;
        }

        public String selectBackupTransportForUser(int userId, String transport) throws RemoteException {
            return null;
        }

        public String selectBackupTransport(String transport) throws RemoteException {
            return null;
        }

        public void selectBackupTransportAsyncForUser(int userId, ComponentName transport, ISelectBackupTransportCallback listener) throws RemoteException {
        }

        public Intent getConfigurationIntentForUser(int userId, String transport) throws RemoteException {
            return null;
        }

        public Intent getConfigurationIntent(String transport) throws RemoteException {
            return null;
        }

        public String getDestinationStringForUser(int userId, String transport) throws RemoteException {
            return null;
        }

        public String getDestinationString(String transport) throws RemoteException {
            return null;
        }

        public Intent getDataManagementIntentForUser(int userId, String transport) throws RemoteException {
            return null;
        }

        public Intent getDataManagementIntent(String transport) throws RemoteException {
            return null;
        }

        public CharSequence getDataManagementLabelForUser(int userId, String transport) throws RemoteException {
            return null;
        }

        public IRestoreSession beginRestoreSessionForUser(int userId, String packageName, String transportID) throws RemoteException {
            return null;
        }

        public void opCompleteForUser(int userId, int token, long result) throws RemoteException {
        }

        public void opComplete(int token, long result) throws RemoteException {
        }

        public void setBackupServiceActive(int whichUser, boolean makeActive) throws RemoteException {
        }

        public boolean isBackupServiceActive(int whichUser) throws RemoteException {
            return false;
        }

        public long getAvailableRestoreTokenForUser(int userId, String packageName) throws RemoteException {
            return 0;
        }

        public boolean isAppEligibleForBackupForUser(int userId, String packageName) throws RemoteException {
            return false;
        }

        public String[] filterAppsEligibleForBackupForUser(int userId, String[] packages) throws RemoteException {
            return null;
        }

        public int requestBackupForUser(int userId, String[] packages, IBackupObserver observer, IBackupManagerMonitor monitor, int flags) throws RemoteException {
            return 0;
        }

        public int requestBackup(String[] packages, IBackupObserver observer, IBackupManagerMonitor monitor, int flags) throws RemoteException {
            return 0;
        }

        public void cancelBackupsForUser(int userId) throws RemoteException {
        }

        public void cancelBackups() throws RemoteException {
        }

        public UserHandle getUserForAncestralSerialNumber(long ancestralSerialNumber) throws RemoteException {
            return null;
        }

        public void setAncestralSerialNumber(long ancestralSerialNumber) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBackupManager {
        private static final String DESCRIPTOR = "android.app.backup.IBackupManager";
        static final int TRANSACTION_acknowledgeFullBackupOrRestore = 26;
        static final int TRANSACTION_acknowledgeFullBackupOrRestoreForUser = 25;
        static final int TRANSACTION_adbBackup = 22;
        static final int TRANSACTION_adbRestore = 24;
        static final int TRANSACTION_agentConnected = 7;
        static final int TRANSACTION_agentConnectedForUser = 6;
        static final int TRANSACTION_agentDisconnected = 9;
        static final int TRANSACTION_agentDisconnectedForUser = 8;
        static final int TRANSACTION_backupNow = 21;
        static final int TRANSACTION_backupNowForUser = 20;
        static final int TRANSACTION_beginRestoreSessionForUser = 45;
        static final int TRANSACTION_cancelBackups = 56;
        static final int TRANSACTION_cancelBackupsForUser = 55;
        static final int TRANSACTION_clearBackupData = 4;
        static final int TRANSACTION_clearBackupDataForUser = 3;
        static final int TRANSACTION_dataChanged = 2;
        static final int TRANSACTION_dataChangedForUser = 1;
        static final int TRANSACTION_filterAppsEligibleForBackupForUser = 52;
        static final int TRANSACTION_fullTransportBackupForUser = 23;
        static final int TRANSACTION_getAvailableRestoreTokenForUser = 50;
        static final int TRANSACTION_getConfigurationIntent = 39;
        static final int TRANSACTION_getConfigurationIntentForUser = 38;
        static final int TRANSACTION_getCurrentTransport = 29;
        static final int TRANSACTION_getCurrentTransportComponentForUser = 30;
        static final int TRANSACTION_getCurrentTransportForUser = 28;
        static final int TRANSACTION_getDataManagementIntent = 43;
        static final int TRANSACTION_getDataManagementIntentForUser = 42;
        static final int TRANSACTION_getDataManagementLabelForUser = 44;
        static final int TRANSACTION_getDestinationString = 41;
        static final int TRANSACTION_getDestinationStringForUser = 40;
        static final int TRANSACTION_getTransportWhitelist = 34;
        static final int TRANSACTION_getUserForAncestralSerialNumber = 57;
        static final int TRANSACTION_hasBackupPassword = 19;
        static final int TRANSACTION_initializeTransportsForUser = 5;
        static final int TRANSACTION_isAppEligibleForBackupForUser = 51;
        static final int TRANSACTION_isBackupEnabled = 17;
        static final int TRANSACTION_isBackupEnabledForUser = 16;
        static final int TRANSACTION_isBackupServiceActive = 49;
        static final int TRANSACTION_listAllTransportComponentsForUser = 33;
        static final int TRANSACTION_listAllTransports = 32;
        static final int TRANSACTION_listAllTransportsForUser = 31;
        static final int TRANSACTION_opComplete = 47;
        static final int TRANSACTION_opCompleteForUser = 46;
        static final int TRANSACTION_requestBackup = 54;
        static final int TRANSACTION_requestBackupForUser = 53;
        static final int TRANSACTION_restoreAtInstall = 11;
        static final int TRANSACTION_restoreAtInstallForUser = 10;
        static final int TRANSACTION_selectBackupTransport = 36;
        static final int TRANSACTION_selectBackupTransportAsyncForUser = 37;
        static final int TRANSACTION_selectBackupTransportForUser = 35;
        static final int TRANSACTION_setAncestralSerialNumber = 58;
        static final int TRANSACTION_setAutoRestore = 15;
        static final int TRANSACTION_setAutoRestoreForUser = 14;
        static final int TRANSACTION_setBackupEnabled = 13;
        static final int TRANSACTION_setBackupEnabledForUser = 12;
        static final int TRANSACTION_setBackupPassword = 18;
        static final int TRANSACTION_setBackupServiceActive = 48;
        static final int TRANSACTION_updateTransportAttributesForUser = 27;

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

            public void dataChangedForUser(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dataChangedForUser(userId, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dataChanged(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dataChanged(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearBackupDataForUser(int userId, String transportName, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(transportName);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearBackupDataForUser(userId, transportName, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearBackupData(String transportName, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transportName);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearBackupData(transportName, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void initializeTransportsForUser(int userId, String[] transportNames, IBackupObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(transportNames);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().initializeTransportsForUser(userId, transportNames, observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void agentConnectedForUser(int userId, String packageName, IBinder agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(agent);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().agentConnectedForUser(userId, packageName, agent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void agentConnected(String packageName, IBinder agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(agent);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().agentConnected(packageName, agent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void agentDisconnectedForUser(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().agentDisconnectedForUser(userId, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void agentDisconnected(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().agentDisconnected(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restoreAtInstallForUser(int userId, String packageName, int token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    _data.writeInt(token);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restoreAtInstallForUser(userId, packageName, token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restoreAtInstall(String packageName, int token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(token);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restoreAtInstall(packageName, token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBackupEnabledForUser(int userId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBackupEnabledForUser(userId, isEnabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBackupEnabled(boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isEnabled ? 1 : 0);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBackupEnabled(isEnabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAutoRestoreForUser(int userId, boolean doAutoRestore) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(doAutoRestore ? 1 : 0);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAutoRestoreForUser(userId, doAutoRestore);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAutoRestore(boolean doAutoRestore) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(doAutoRestore ? 1 : 0);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAutoRestore(doAutoRestore);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isBackupEnabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isBackupEnabledForUser(userId);
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

            public boolean isBackupEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isBackupEnabled();
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

            public boolean setBackupPassword(String currentPw, String newPw) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(currentPw);
                    _data.writeString(newPw);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setBackupPassword(currentPw, newPw);
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

            public boolean hasBackupPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasBackupPassword();
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

            public void backupNowForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().backupNowForUser(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void backupNow() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().backupNow();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void adbBackup(int userId, ParcelFileDescriptor fd, boolean includeApks, boolean includeObbs, boolean includeShared, boolean doWidgets, boolean allApps, boolean allIncludesSystem, boolean doCompress, boolean doKeyValue, String[] packageNames) throws RemoteException {
                ParcelFileDescriptor parcelFileDescriptor = fd;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 1;
                    if (parcelFileDescriptor != null) {
                        _data.writeInt(1);
                        parcelFileDescriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(includeApks ? 1 : 0);
                    _data.writeInt(includeObbs ? 1 : 0);
                    _data.writeInt(includeShared ? 1 : 0);
                    _data.writeInt(doWidgets ? 1 : 0);
                    _data.writeInt(allApps ? 1 : 0);
                    _data.writeInt(allIncludesSystem ? 1 : 0);
                    _data.writeInt(doCompress ? 1 : 0);
                    if (!doKeyValue) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeStringArray(packageNames);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().adbBackup(userId, fd, includeApks, includeObbs, includeShared, doWidgets, allApps, allIncludesSystem, doCompress, doKeyValue, packageNames);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void fullTransportBackupForUser(int userId, String[] packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(packageNames);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fullTransportBackupForUser(userId, packageNames);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void adbRestore(int userId, ParcelFileDescriptor fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().adbRestore(userId, fd);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void acknowledgeFullBackupOrRestoreForUser(int userId, int token, boolean allow, String curPassword, String encryptionPassword, IFullBackupRestoreObserver observer) throws RemoteException {
                Throwable th;
                int i;
                String str;
                String str2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(userId);
                    } catch (Throwable th2) {
                        th = th2;
                        i = token;
                        str = curPassword;
                        str2 = encryptionPassword;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(token);
                        _data.writeInt(allow ? 1 : 0);
                    } catch (Throwable th3) {
                        th = th3;
                        str = curPassword;
                        str2 = encryptionPassword;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(curPassword);
                        try {
                            _data.writeString(encryptionPassword);
                            _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().acknowledgeFullBackupOrRestoreForUser(userId, token, allow, curPassword, encryptionPassword, observer);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        str2 = encryptionPassword;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i2 = userId;
                    i = token;
                    str = curPassword;
                    str2 = encryptionPassword;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void acknowledgeFullBackupOrRestore(int token, boolean allow, String curPassword, String encryptionPassword, IFullBackupRestoreObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(allow ? 1 : 0);
                    _data.writeString(curPassword);
                    _data.writeString(encryptionPassword);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().acknowledgeFullBackupOrRestore(token, allow, curPassword, encryptionPassword, observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateTransportAttributesForUser(int userId, ComponentName transportComponent, String name, Intent configurationIntent, String currentDestinationString, Intent dataManagementIntent, CharSequence dataManagementLabel) throws RemoteException {
                Throwable th;
                ComponentName componentName = transportComponent;
                Intent intent = configurationIntent;
                Intent intent2 = dataManagementIntent;
                CharSequence charSequence = dataManagementLabel;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(userId);
                        if (componentName != null) {
                            _data.writeInt(1);
                            componentName.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeString(name);
                        if (intent != null) {
                            _data.writeInt(1);
                            intent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeString(currentDestinationString);
                        if (intent2 != null) {
                            _data.writeInt(1);
                            intent2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (charSequence != null) {
                            _data.writeInt(1);
                            TextUtils.writeToParcel(charSequence, _data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().updateTransportAttributesForUser(userId, transportComponent, name, configurationIntent, currentDestinationString, dataManagementIntent, dataManagementLabel);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    int i = userId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public String getCurrentTransportForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    String str = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCurrentTransportForUser(userId);
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

            public String getCurrentTransport() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCurrentTransport();
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

            public ComponentName getCurrentTransportComponentForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    ComponentName componentName = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getCurrentTransportComponentForUser(userId);
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] listAllTransportsForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    String[] strArr = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().listAllTransportsForUser(userId);
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

            public String[] listAllTransports() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 32;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().listAllTransports();
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

            public ComponentName[] listAllTransportComponentsForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    ComponentName[] componentNameArr = 33;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        componentNameArr = Stub.getDefaultImpl();
                        if (componentNameArr != 0) {
                            componentNameArr = Stub.getDefaultImpl().listAllTransportComponentsForUser(userId);
                            return componentNameArr;
                        }
                    }
                    _reply.readException();
                    ComponentName[] _result = (ComponentName[]) _reply.createTypedArray(ComponentName.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getTransportWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 34;
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getTransportWhitelist();
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

            public String selectBackupTransportForUser(int userId, String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(transport);
                    String str = 35;
                    if (!this.mRemote.transact(35, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().selectBackupTransportForUser(userId, transport);
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

            public String selectBackupTransport(String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    String str = 36;
                    if (!this.mRemote.transact(36, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().selectBackupTransport(transport);
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

            public void selectBackupTransportAsyncForUser(int userId, ComponentName transport, ISelectBackupTransportCallback listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (transport != null) {
                        _data.writeInt(1);
                        transport.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().selectBackupTransportAsyncForUser(userId, transport, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Intent getConfigurationIntentForUser(int userId, String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(transport);
                    Intent intent = 38;
                    if (!this.mRemote.transact(38, _data, _reply, 0)) {
                        intent = Stub.getDefaultImpl();
                        if (intent != 0) {
                            intent = Stub.getDefaultImpl().getConfigurationIntentForUser(userId, transport);
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

            public Intent getConfigurationIntent(String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    Intent intent = 39;
                    if (!this.mRemote.transact(39, _data, _reply, 0)) {
                        intent = Stub.getDefaultImpl();
                        if (intent != 0) {
                            intent = Stub.getDefaultImpl().getConfigurationIntent(transport);
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

            public String getDestinationStringForUser(int userId, String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(transport);
                    String str = 40;
                    if (!this.mRemote.transact(40, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDestinationStringForUser(userId, transport);
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

            public String getDestinationString(String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    String str = 41;
                    if (!this.mRemote.transact(41, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDestinationString(transport);
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

            public Intent getDataManagementIntentForUser(int userId, String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(transport);
                    Intent intent = 42;
                    if (!this.mRemote.transact(42, _data, _reply, 0)) {
                        intent = Stub.getDefaultImpl();
                        if (intent != 0) {
                            intent = Stub.getDefaultImpl().getDataManagementIntentForUser(userId, transport);
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

            public Intent getDataManagementIntent(String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    Intent intent = 43;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        intent = Stub.getDefaultImpl();
                        if (intent != 0) {
                            intent = Stub.getDefaultImpl().getDataManagementIntent(transport);
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

            public CharSequence getDataManagementLabelForUser(int userId, String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(transport);
                    CharSequence charSequence = 44;
                    if (!this.mRemote.transact(44, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != 0) {
                            charSequence = Stub.getDefaultImpl().getDataManagementLabelForUser(userId, transport);
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

            public IRestoreSession beginRestoreSessionForUser(int userId, String packageName, String transportID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    _data.writeString(transportID);
                    IRestoreSession iRestoreSession = 45;
                    if (!this.mRemote.transact(45, _data, _reply, 0)) {
                        iRestoreSession = Stub.getDefaultImpl();
                        if (iRestoreSession != 0) {
                            iRestoreSession = Stub.getDefaultImpl().beginRestoreSessionForUser(userId, packageName, transportID);
                            return iRestoreSession;
                        }
                    }
                    _reply.readException();
                    iRestoreSession = android.app.backup.IRestoreSession.Stub.asInterface(_reply.readStrongBinder());
                    IRestoreSession _result = iRestoreSession;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void opCompleteForUser(int userId, int token, long result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(token);
                    _data.writeLong(result);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().opCompleteForUser(userId, token, result);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void opComplete(int token, long result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeLong(result);
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().opComplete(token, result);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBackupServiceActive(int whichUser, boolean makeActive) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(whichUser);
                    _data.writeInt(makeActive ? 1 : 0);
                    if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBackupServiceActive(whichUser, makeActive);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isBackupServiceActive(int whichUser) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(whichUser);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(49, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isBackupServiceActive(whichUser);
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

            public long getAvailableRestoreTokenForUser(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    long j = 50;
                    if (!this.mRemote.transact(50, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getAvailableRestoreTokenForUser(userId, packageName);
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

            public boolean isAppEligibleForBackupForUser(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(51, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAppEligibleForBackupForUser(userId, packageName);
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

            public String[] filterAppsEligibleForBackupForUser(int userId, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(packages);
                    String[] strArr = 52;
                    if (!this.mRemote.transact(52, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().filterAppsEligibleForBackupForUser(userId, packages);
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

            public int requestBackupForUser(int userId, String[] packages, IBackupObserver observer, IBackupManagerMonitor monitor, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(packages);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (monitor != null) {
                        iBinder = monitor.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeInt(flags);
                    int i = 53;
                    if (!this.mRemote.transact(53, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().requestBackupForUser(userId, packages, observer, monitor, flags);
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

            public int requestBackup(String[] packages, IBackupObserver observer, IBackupManagerMonitor monitor, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packages);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (monitor != null) {
                        iBinder = monitor.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeInt(flags);
                    int i = 54;
                    if (!this.mRemote.transact(54, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().requestBackup(packages, observer, monitor, flags);
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

            public void cancelBackupsForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(55, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelBackupsForUser(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelBackups() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelBackups();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public UserHandle getUserForAncestralSerialNumber(long ancestralSerialNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(ancestralSerialNumber);
                    UserHandle userHandle = 57;
                    if (!this.mRemote.transact(57, _data, _reply, 0)) {
                        userHandle = Stub.getDefaultImpl();
                        if (userHandle != 0) {
                            userHandle = Stub.getDefaultImpl().getUserForAncestralSerialNumber(ancestralSerialNumber);
                            return userHandle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        userHandle = (UserHandle) UserHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        userHandle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return userHandle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAncestralSerialNumber(long ancestralSerialNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(ancestralSerialNumber);
                    if (this.mRemote.transact(58, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAncestralSerialNumber(ancestralSerialNumber);
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
                    return "dataChangedForUser";
                case 2:
                    return "dataChanged";
                case 3:
                    return "clearBackupDataForUser";
                case 4:
                    return "clearBackupData";
                case 5:
                    return "initializeTransportsForUser";
                case 6:
                    return "agentConnectedForUser";
                case 7:
                    return "agentConnected";
                case 8:
                    return "agentDisconnectedForUser";
                case 9:
                    return "agentDisconnected";
                case 10:
                    return "restoreAtInstallForUser";
                case 11:
                    return "restoreAtInstall";
                case 12:
                    return "setBackupEnabledForUser";
                case 13:
                    return "setBackupEnabled";
                case 14:
                    return "setAutoRestoreForUser";
                case 15:
                    return "setAutoRestore";
                case 16:
                    return "isBackupEnabledForUser";
                case 17:
                    return "isBackupEnabled";
                case 18:
                    return "setBackupPassword";
                case 19:
                    return "hasBackupPassword";
                case 20:
                    return "backupNowForUser";
                case 21:
                    return "backupNow";
                case 22:
                    return "adbBackup";
                case 23:
                    return "fullTransportBackupForUser";
                case 24:
                    return "adbRestore";
                case 25:
                    return "acknowledgeFullBackupOrRestoreForUser";
                case 26:
                    return "acknowledgeFullBackupOrRestore";
                case 27:
                    return "updateTransportAttributesForUser";
                case 28:
                    return "getCurrentTransportForUser";
                case 29:
                    return "getCurrentTransport";
                case 30:
                    return "getCurrentTransportComponentForUser";
                case 31:
                    return "listAllTransportsForUser";
                case 32:
                    return "listAllTransports";
                case 33:
                    return "listAllTransportComponentsForUser";
                case 34:
                    return "getTransportWhitelist";
                case 35:
                    return "selectBackupTransportForUser";
                case 36:
                    return "selectBackupTransport";
                case 37:
                    return "selectBackupTransportAsyncForUser";
                case 38:
                    return "getConfigurationIntentForUser";
                case 39:
                    return "getConfigurationIntent";
                case 40:
                    return "getDestinationStringForUser";
                case 41:
                    return "getDestinationString";
                case 42:
                    return "getDataManagementIntentForUser";
                case 43:
                    return "getDataManagementIntent";
                case 44:
                    return "getDataManagementLabelForUser";
                case 45:
                    return "beginRestoreSessionForUser";
                case 46:
                    return "opCompleteForUser";
                case 47:
                    return "opComplete";
                case 48:
                    return "setBackupServiceActive";
                case 49:
                    return "isBackupServiceActive";
                case 50:
                    return "getAvailableRestoreTokenForUser";
                case 51:
                    return "isAppEligibleForBackupForUser";
                case 52:
                    return "filterAppsEligibleForBackupForUser";
                case 53:
                    return "requestBackupForUser";
                case 54:
                    return "requestBackup";
                case 55:
                    return "cancelBackupsForUser";
                case 56:
                    return "cancelBackups";
                case 57:
                    return "getUserForAncestralSerialNumber";
                case 58:
                    return "setAncestralSerialNumber";
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
                boolean _arg0 = false;
                int _arg02;
                boolean _result;
                boolean _result2;
                int _arg03;
                String _result3;
                String[] _result4;
                String _result5;
                Intent _result6;
                Intent _result7;
                switch (i) {
                    case 1:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        dataChangedForUser(data.readInt(), data.readString());
                        reply.writeNoException();
                        return z;
                    case 2:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        dataChanged(data.readString());
                        reply.writeNoException();
                        return z;
                    case 3:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        clearBackupDataForUser(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return z;
                    case 4:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        clearBackupData(data.readString(), data.readString());
                        reply.writeNoException();
                        return z;
                    case 5:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        initializeTransportsForUser(data.readInt(), data.createStringArray(), android.app.backup.IBackupObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return z;
                    case 6:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        agentConnectedForUser(data.readInt(), data.readString(), data.readStrongBinder());
                        reply.writeNoException();
                        return z;
                    case 7:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        agentConnected(data.readString(), data.readStrongBinder());
                        reply.writeNoException();
                        return z;
                    case 8:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        agentDisconnectedForUser(data.readInt(), data.readString());
                        reply.writeNoException();
                        return z;
                    case 9:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        agentDisconnected(data.readString());
                        reply.writeNoException();
                        return z;
                    case 10:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        restoreAtInstallForUser(data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 11:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        restoreAtInstall(data.readString(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 12:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg0 = z;
                        }
                        setBackupEnabledForUser(_arg02, _arg0);
                        reply.writeNoException();
                        return z;
                    case 13:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = z;
                        }
                        setBackupEnabled(_arg0);
                        reply.writeNoException();
                        return z;
                    case 14:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg0 = z;
                        }
                        setAutoRestoreForUser(_arg02, _arg0);
                        reply.writeNoException();
                        return z;
                    case 15:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = z;
                        }
                        setAutoRestore(_arg0);
                        reply.writeNoException();
                        return z;
                    case 16:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = isBackupEnabledForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 17:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = isBackupEnabled();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return z;
                    case 18:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result2 = setBackupPassword(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return z;
                    case 19:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = hasBackupPassword();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return z;
                    case 20:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        backupNowForUser(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 21:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        backupNow();
                        reply.writeNoException();
                        return z;
                    case 22:
                        ParcelFileDescriptor _arg1;
                        parcel.enforceInterface(descriptor);
                        int _arg04 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        boolean _arg2 = data.readInt() != 0;
                        boolean _arg3 = data.readInt() != 0;
                        boolean _arg4 = data.readInt() != 0;
                        boolean _arg5 = data.readInt() != 0;
                        boolean _arg6 = data.readInt() != 0;
                        boolean _arg7 = data.readInt() != 0;
                        boolean _arg8 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        z = true;
                        adbBackup(_arg04, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg0, data.createStringArray());
                        reply.writeNoException();
                        return z;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        fullTransportBackupForUser(data.readInt(), data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 24:
                        ParcelFileDescriptor _arg12;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        adbRestore(_arg03, _arg12);
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        acknowledgeFullBackupOrRestoreForUser(data.readInt(), data.readInt(), data.readInt() != 0, data.readString(), data.readString(), android.app.backup.IFullBackupRestoreObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        acknowledgeFullBackupOrRestore(data.readInt(), data.readInt() != 0, data.readString(), data.readString(), android.app.backup.IFullBackupRestoreObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 27:
                        ComponentName _arg13;
                        Intent _arg32;
                        Intent _arg52;
                        CharSequence _arg62;
                        parcel.enforceInterface(descriptor);
                        int _arg05 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg13 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        String _arg22 = data.readString();
                        if (data.readInt() != 0) {
                            _arg32 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        String _arg42 = data.readString();
                        if (data.readInt() != 0) {
                            _arg52 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg52 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg62 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg62 = null;
                        }
                        updateTransportAttributesForUser(_arg05, _arg13, _arg22, _arg32, _arg42, _arg52, _arg62);
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        _result3 = getCurrentTransportForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result3);
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        String _result8 = getCurrentTransport();
                        reply.writeNoException();
                        parcel2.writeString(_result8);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        ComponentName _result9 = getCurrentTransportComponentForUser(data.readInt());
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        String[] _result10 = listAllTransportsForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result10);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        _result4 = listAllTransports();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result4);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        ComponentName[] _result11 = listAllTransportComponentsForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result11, 1);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        _result4 = getTransportWhitelist();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result4);
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _result5 = selectBackupTransportForUser(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result5);
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        _result3 = selectBackupTransport(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result3);
                        return true;
                    case 37:
                        ComponentName _arg14;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg14 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        selectBackupTransportAsyncForUser(_arg03, _arg14, android.app.backup.ISelectBackupTransportCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        _result6 = getConfigurationIntentForUser(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        _result7 = getConfigurationIntent(data.readString());
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        _result5 = getDestinationStringForUser(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result5);
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        _result3 = getDestinationString(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result3);
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        _result6 = getDataManagementIntentForUser(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        _result7 = getDataManagementIntent(data.readString());
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        CharSequence _result12 = getDataManagementLabelForUser(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result12 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_result12, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        IRestoreSession _result13 = beginRestoreSessionForUser(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result13 != null ? _result13.asBinder() : null);
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        opCompleteForUser(data.readInt(), data.readInt(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        opComplete(data.readInt(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setBackupServiceActive(_arg02, _arg0);
                        reply.writeNoException();
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        _result = isBackupServiceActive(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        long _result14 = getAvailableRestoreTokenForUser(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result14);
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        _result2 = isAppEligibleForBackupForUser(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        String[] _result15 = filterAppsEligibleForBackupForUser(data.readInt(), data.createStringArray());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result15);
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        _arg03 = requestBackupForUser(data.readInt(), data.createStringArray(), android.app.backup.IBackupObserver.Stub.asInterface(data.readStrongBinder()), android.app.backup.IBackupManagerMonitor.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg03);
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        int _result16 = requestBackup(data.createStringArray(), android.app.backup.IBackupObserver.Stub.asInterface(data.readStrongBinder()), android.app.backup.IBackupManagerMonitor.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result16);
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        cancelBackupsForUser(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        cancelBackups();
                        reply.writeNoException();
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        UserHandle _result17 = getUserForAncestralSerialNumber(data.readLong());
                        reply.writeNoException();
                        if (_result17 != null) {
                            parcel2.writeInt(1);
                            _result17.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 58:
                        parcel.enforceInterface(descriptor);
                        setAncestralSerialNumber(data.readLong());
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

    @UnsupportedAppUsage
    void acknowledgeFullBackupOrRestore(int i, boolean z, String str, String str2, IFullBackupRestoreObserver iFullBackupRestoreObserver) throws RemoteException;

    void acknowledgeFullBackupOrRestoreForUser(int i, int i2, boolean z, String str, String str2, IFullBackupRestoreObserver iFullBackupRestoreObserver) throws RemoteException;

    void adbBackup(int i, ParcelFileDescriptor parcelFileDescriptor, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, String[] strArr) throws RemoteException;

    void adbRestore(int i, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void agentConnected(String str, IBinder iBinder) throws RemoteException;

    void agentConnectedForUser(int i, String str, IBinder iBinder) throws RemoteException;

    void agentDisconnected(String str) throws RemoteException;

    void agentDisconnectedForUser(int i, String str) throws RemoteException;

    void backupNow() throws RemoteException;

    void backupNowForUser(int i) throws RemoteException;

    IRestoreSession beginRestoreSessionForUser(int i, String str, String str2) throws RemoteException;

    void cancelBackups() throws RemoteException;

    void cancelBackupsForUser(int i) throws RemoteException;

    @UnsupportedAppUsage
    void clearBackupData(String str, String str2) throws RemoteException;

    void clearBackupDataForUser(int i, String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void dataChanged(String str) throws RemoteException;

    void dataChangedForUser(int i, String str) throws RemoteException;

    String[] filterAppsEligibleForBackupForUser(int i, String[] strArr) throws RemoteException;

    void fullTransportBackupForUser(int i, String[] strArr) throws RemoteException;

    long getAvailableRestoreTokenForUser(int i, String str) throws RemoteException;

    Intent getConfigurationIntent(String str) throws RemoteException;

    Intent getConfigurationIntentForUser(int i, String str) throws RemoteException;

    @UnsupportedAppUsage
    String getCurrentTransport() throws RemoteException;

    ComponentName getCurrentTransportComponentForUser(int i) throws RemoteException;

    String getCurrentTransportForUser(int i) throws RemoteException;

    Intent getDataManagementIntent(String str) throws RemoteException;

    Intent getDataManagementIntentForUser(int i, String str) throws RemoteException;

    CharSequence getDataManagementLabelForUser(int i, String str) throws RemoteException;

    String getDestinationString(String str) throws RemoteException;

    String getDestinationStringForUser(int i, String str) throws RemoteException;

    String[] getTransportWhitelist() throws RemoteException;

    UserHandle getUserForAncestralSerialNumber(long j) throws RemoteException;

    boolean hasBackupPassword() throws RemoteException;

    void initializeTransportsForUser(int i, String[] strArr, IBackupObserver iBackupObserver) throws RemoteException;

    boolean isAppEligibleForBackupForUser(int i, String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean isBackupEnabled() throws RemoteException;

    boolean isBackupEnabledForUser(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean isBackupServiceActive(int i) throws RemoteException;

    ComponentName[] listAllTransportComponentsForUser(int i) throws RemoteException;

    @UnsupportedAppUsage
    String[] listAllTransports() throws RemoteException;

    String[] listAllTransportsForUser(int i) throws RemoteException;

    void opComplete(int i, long j) throws RemoteException;

    void opCompleteForUser(int i, int i2, long j) throws RemoteException;

    int requestBackup(String[] strArr, IBackupObserver iBackupObserver, IBackupManagerMonitor iBackupManagerMonitor, int i) throws RemoteException;

    int requestBackupForUser(int i, String[] strArr, IBackupObserver iBackupObserver, IBackupManagerMonitor iBackupManagerMonitor, int i2) throws RemoteException;

    void restoreAtInstall(String str, int i) throws RemoteException;

    void restoreAtInstallForUser(int i, String str, int i2) throws RemoteException;

    @UnsupportedAppUsage
    String selectBackupTransport(String str) throws RemoteException;

    void selectBackupTransportAsyncForUser(int i, ComponentName componentName, ISelectBackupTransportCallback iSelectBackupTransportCallback) throws RemoteException;

    String selectBackupTransportForUser(int i, String str) throws RemoteException;

    void setAncestralSerialNumber(long j) throws RemoteException;

    @UnsupportedAppUsage
    void setAutoRestore(boolean z) throws RemoteException;

    void setAutoRestoreForUser(int i, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setBackupEnabled(boolean z) throws RemoteException;

    void setBackupEnabledForUser(int i, boolean z) throws RemoteException;

    boolean setBackupPassword(String str, String str2) throws RemoteException;

    void setBackupServiceActive(int i, boolean z) throws RemoteException;

    void updateTransportAttributesForUser(int i, ComponentName componentName, String str, Intent intent, String str2, Intent intent2, CharSequence charSequence) throws RemoteException;
}
