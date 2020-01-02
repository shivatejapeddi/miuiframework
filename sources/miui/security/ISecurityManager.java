package miui.security;

import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.app.IWakePathCallback;
import java.util.List;

public interface ISecurityManager extends IInterface {

    public static class Default implements ISecurityManager {
        public void killNativePackageProcesses(int uid, String pkgName) throws RemoteException {
        }

        public String getPackageNameByPid(int pid) throws RemoteException {
            return null;
        }

        public boolean checkSmsBlocked(Intent intent) throws RemoteException {
            return false;
        }

        public boolean startInterceptSmsBySender(String pkgName, String sender, int count) throws RemoteException {
            return false;
        }

        public boolean stopInterceptSmsBySender() throws RemoteException {
            return false;
        }

        public void addAccessControlPass(String packageName) throws RemoteException {
        }

        public void removeAccessControlPass(String packageName) throws RemoteException {
        }

        public boolean checkAccessControlPass(String packageName, Intent intent) throws RemoteException {
            return false;
        }

        public boolean getApplicationAccessControlEnabled(String packageName) throws RemoteException {
            return false;
        }

        public void setApplicationAccessControlEnabled(String packageName, boolean enabled) throws RemoteException {
        }

        public boolean getApplicationChildrenControlEnabled(String packageName) throws RemoteException {
            return false;
        }

        public void setApplicationChildrenControlEnabled(String packageName, boolean enabled) throws RemoteException {
        }

        public void setWakeUpTime(String componentName, long timeInSeconds) throws RemoteException {
        }

        public long getWakeUpTime(String componentName) throws RemoteException {
            return 0;
        }

        public boolean putSystemDataStringFile(String path, String value) throws RemoteException {
            return false;
        }

        public String readSystemDataStringFile(String path) throws RemoteException {
            return null;
        }

        public void pushWakePathData(int wakeType, ParceledListSlice wakePathRuleInfos, int userId) throws RemoteException {
        }

        public void pushWakePathWhiteList(List<String> list, int userId) throws RemoteException {
        }

        public void setTrackWakePathCallListLogEnabled(boolean enabled) throws RemoteException {
        }

        public ParceledListSlice getWakePathCallListLog() throws RemoteException {
            return null;
        }

        public int getAppPermissionControlOpen(int userId) throws RemoteException {
            return 0;
        }

        public void setAppPermissionControlOpen(int status) throws RemoteException {
        }

        public void registerWakePathCallback(IWakePathCallback callback) throws RemoteException {
        }

        public void removeAccessControlPassAsUser(String packageName, int userId) throws RemoteException {
        }

        public boolean needFinishAccessControl(IBinder token) throws RemoteException {
            return false;
        }

        public void finishAccessControl(String packageName, int userId) throws RemoteException {
        }

        public int activityResume(Intent intent) throws RemoteException {
            return 0;
        }

        public void setCoreRuntimePermissionEnabled(boolean grant, int flags) throws RemoteException {
        }

        public void grantRuntimePermission(String packageName) throws RemoteException {
        }

        public int getCurrentUserId() throws RemoteException {
            return 0;
        }

        public boolean checkAccessControlPassAsUser(String packageName, Intent intent, int userId) throws RemoteException {
            return false;
        }

        public boolean getApplicationAccessControlEnabledAsUser(String packageName, int userId) throws RemoteException {
            return false;
        }

        public void removeWakePathData(int userId) throws RemoteException {
        }

        public boolean checkAllowStartActivity(String callerPkgName, String calleePkgName, Intent intent, int userId) throws RemoteException {
            return false;
        }

        public int getSysAppCracked() throws RemoteException {
            return 0;
        }

        public void grantInstallPermission(String packageName, String name) throws RemoteException {
        }

        public void pushWakePathConfirmDialogWhiteList(int type, List<String> list) throws RemoteException {
        }

        public void addAccessControlPassForUser(String packageName, int userId) throws RemoteException {
        }

        public void setApplicationAccessControlEnabledForUser(String packageName, boolean enabled, int userId) throws RemoteException {
        }

        public boolean isRestrictedAppNet(String packageName) throws RemoteException {
            return false;
        }

        public boolean writeAppHideConfig(boolean hide) throws RemoteException {
            return false;
        }

        public void saveIcon(String fileName, Bitmap icon) throws RemoteException {
        }

        public boolean addMiuiFirewallSharedUid(int uid) throws RemoteException {
            return false;
        }

        public boolean setMiuiFirewallRule(String packageName, int uid, int rule, int type) throws RemoteException {
            return false;
        }

        public boolean setCurrentNetworkState(int state) throws RemoteException {
            return false;
        }

        public void setIncompatibleAppList(List<String> list) throws RemoteException {
        }

        public List<String> getIncompatibleAppList() throws RemoteException {
            return null;
        }

        public ParceledListSlice getWakePathComponents(String packageName) throws RemoteException {
            return null;
        }

        public void offerGoogleBaseCallBack(ISecurityCallback client) throws RemoteException {
        }

        public void notifyAppsPreInstalled() throws RemoteException {
        }

        public boolean getApplicationMaskNotificationEnabledAsUser(String packageName, int userId) throws RemoteException {
            return false;
        }

        public void setApplicationMaskNotificationEnabledForUser(String packageName, boolean enabled, int userId) throws RemoteException {
        }

        public boolean areNotificationsEnabledForPackage(String packageName, int uid) throws RemoteException {
            return false;
        }

        public void setNotificationsEnabledForPackage(String packageName, int uid, boolean enabled) throws RemoteException {
        }

        public boolean isAppHide() throws RemoteException {
            return false;
        }

        public boolean isFunctionOpen() throws RemoteException {
            return false;
        }

        public boolean setAppHide(boolean hide) throws RemoteException {
            return false;
        }

        public boolean isValidDevice() throws RemoteException {
            return false;
        }

        public boolean checkGameBoosterAntimsgPassAsUser(String packageName, Intent intent, int userId) throws RemoteException {
            return false;
        }

        public void setGameBoosterIBinder(IBinder gamebooster, int userId, boolean isGameMode) throws RemoteException {
        }

        public boolean getGameMode(int userId) throws RemoteException {
            return false;
        }

        public void setAccessControlPassword(String passwordType, String password, int userId) throws RemoteException {
        }

        public boolean checkAccessControlPassword(String passwordType, String password, int userId) throws RemoteException {
            return false;
        }

        public boolean haveAccessControlPassword(int userId) throws RemoteException {
            return false;
        }

        public String getAccessControlPasswordType(int userId) throws RemoteException {
            return null;
        }

        public void setAppPrivacyStatus(String packageName, boolean isOpen) throws RemoteException {
        }

        public boolean isAppPrivacyEnabled(String packageName) throws RemoteException {
            return false;
        }

        public boolean isAllowStartService(Intent service, int userId) throws RemoteException {
            return false;
        }

        public IBinder getTopActivity() throws RemoteException {
            return null;
        }

        public IBinder getAppRunningControlIBinder() throws RemoteException {
            return null;
        }

        public void watchGreenGuardProcess() throws RemoteException {
        }

        public int getSecondSpaceId() throws RemoteException {
            return 0;
        }

        public int moveTaskToStack(int taskId, int stackId, boolean toTop) throws RemoteException {
            return 0;
        }

        public int resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
            return 0;
        }

        public void setStickWindowName(String component) throws RemoteException {
        }

        public boolean getStickWindowName(String component) throws RemoteException {
            return false;
        }

        public void pushUpdatePkgsData(List<String> list, boolean enable) throws RemoteException {
        }

        public void setPrivacyApp(String packageName, int userId, boolean isPrivacy) throws RemoteException {
        }

        public boolean isPrivacyApp(String packageName, int userId) throws RemoteException {
            return false;
        }

        public List<String> getAllPrivacyApps(int userId) throws RemoteException {
            return null;
        }

        public void updateLauncherPackageNames() throws RemoteException {
        }

        public void grantRuntimePermissionAsUser(String packageName, String permName, int userId) throws RemoteException {
        }

        public void revokeRuntimePermissionAsUser(String packageName, String permName, int userId) throws RemoteException {
        }

        public void revokeRuntimePermissionAsUserNotKill(String packageName, String permName, int userId) throws RemoteException {
        }

        public int getPermissionFlagsAsUser(String permName, String packageName, int userId) throws RemoteException {
            return 0;
        }

        public void updatePermissionFlagsAsUser(String permissionName, String packageName, int flagMask, int flagValues, int userId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISecurityManager {
        private static final String DESCRIPTOR = "miui.security.ISecurityManager";
        static final int TRANSACTION_activityResume = 27;
        static final int TRANSACTION_addAccessControlPass = 6;
        static final int TRANSACTION_addAccessControlPassForUser = 38;
        static final int TRANSACTION_addMiuiFirewallSharedUid = 43;
        static final int TRANSACTION_areNotificationsEnabledForPackage = 53;
        static final int TRANSACTION_checkAccessControlPass = 8;
        static final int TRANSACTION_checkAccessControlPassAsUser = 31;
        static final int TRANSACTION_checkAccessControlPassword = 63;
        static final int TRANSACTION_checkAllowStartActivity = 34;
        static final int TRANSACTION_checkGameBoosterAntimsgPassAsUser = 59;
        static final int TRANSACTION_checkSmsBlocked = 3;
        static final int TRANSACTION_finishAccessControl = 26;
        static final int TRANSACTION_getAccessControlPasswordType = 65;
        static final int TRANSACTION_getAllPrivacyApps = 80;
        static final int TRANSACTION_getAppPermissionControlOpen = 21;
        static final int TRANSACTION_getAppRunningControlIBinder = 70;
        static final int TRANSACTION_getApplicationAccessControlEnabled = 9;
        static final int TRANSACTION_getApplicationAccessControlEnabledAsUser = 32;
        static final int TRANSACTION_getApplicationChildrenControlEnabled = 11;
        static final int TRANSACTION_getApplicationMaskNotificationEnabledAsUser = 51;
        static final int TRANSACTION_getCurrentUserId = 30;
        static final int TRANSACTION_getGameMode = 61;
        static final int TRANSACTION_getIncompatibleAppList = 47;
        static final int TRANSACTION_getPackageNameByPid = 2;
        static final int TRANSACTION_getPermissionFlagsAsUser = 85;
        static final int TRANSACTION_getSecondSpaceId = 72;
        static final int TRANSACTION_getStickWindowName = 76;
        static final int TRANSACTION_getSysAppCracked = 35;
        static final int TRANSACTION_getTopActivity = 69;
        static final int TRANSACTION_getWakePathCallListLog = 20;
        static final int TRANSACTION_getWakePathComponents = 48;
        static final int TRANSACTION_getWakeUpTime = 14;
        static final int TRANSACTION_grantInstallPermission = 36;
        static final int TRANSACTION_grantRuntimePermission = 29;
        static final int TRANSACTION_grantRuntimePermissionAsUser = 82;
        static final int TRANSACTION_haveAccessControlPassword = 64;
        static final int TRANSACTION_isAllowStartService = 68;
        static final int TRANSACTION_isAppHide = 55;
        static final int TRANSACTION_isAppPrivacyEnabled = 67;
        static final int TRANSACTION_isFunctionOpen = 56;
        static final int TRANSACTION_isPrivacyApp = 79;
        static final int TRANSACTION_isRestrictedAppNet = 40;
        static final int TRANSACTION_isValidDevice = 58;
        static final int TRANSACTION_killNativePackageProcesses = 1;
        static final int TRANSACTION_moveTaskToStack = 73;
        static final int TRANSACTION_needFinishAccessControl = 25;
        static final int TRANSACTION_notifyAppsPreInstalled = 50;
        static final int TRANSACTION_offerGoogleBaseCallBack = 49;
        static final int TRANSACTION_pushUpdatePkgsData = 77;
        static final int TRANSACTION_pushWakePathConfirmDialogWhiteList = 37;
        static final int TRANSACTION_pushWakePathData = 17;
        static final int TRANSACTION_pushWakePathWhiteList = 18;
        static final int TRANSACTION_putSystemDataStringFile = 15;
        static final int TRANSACTION_readSystemDataStringFile = 16;
        static final int TRANSACTION_registerWakePathCallback = 23;
        static final int TRANSACTION_removeAccessControlPass = 7;
        static final int TRANSACTION_removeAccessControlPassAsUser = 24;
        static final int TRANSACTION_removeWakePathData = 33;
        static final int TRANSACTION_resizeTask = 74;
        static final int TRANSACTION_revokeRuntimePermissionAsUser = 83;
        static final int TRANSACTION_revokeRuntimePermissionAsUserNotKill = 84;
        static final int TRANSACTION_saveIcon = 42;
        static final int TRANSACTION_setAccessControlPassword = 62;
        static final int TRANSACTION_setAppHide = 57;
        static final int TRANSACTION_setAppPermissionControlOpen = 22;
        static final int TRANSACTION_setAppPrivacyStatus = 66;
        static final int TRANSACTION_setApplicationAccessControlEnabled = 10;
        static final int TRANSACTION_setApplicationAccessControlEnabledForUser = 39;
        static final int TRANSACTION_setApplicationChildrenControlEnabled = 12;
        static final int TRANSACTION_setApplicationMaskNotificationEnabledForUser = 52;
        static final int TRANSACTION_setCoreRuntimePermissionEnabled = 28;
        static final int TRANSACTION_setCurrentNetworkState = 45;
        static final int TRANSACTION_setGameBoosterIBinder = 60;
        static final int TRANSACTION_setIncompatibleAppList = 46;
        static final int TRANSACTION_setMiuiFirewallRule = 44;
        static final int TRANSACTION_setNotificationsEnabledForPackage = 54;
        static final int TRANSACTION_setPrivacyApp = 78;
        static final int TRANSACTION_setStickWindowName = 75;
        static final int TRANSACTION_setTrackWakePathCallListLogEnabled = 19;
        static final int TRANSACTION_setWakeUpTime = 13;
        static final int TRANSACTION_startInterceptSmsBySender = 4;
        static final int TRANSACTION_stopInterceptSmsBySender = 5;
        static final int TRANSACTION_updateLauncherPackageNames = 81;
        static final int TRANSACTION_updatePermissionFlagsAsUser = 86;
        static final int TRANSACTION_watchGreenGuardProcess = 71;
        static final int TRANSACTION_writeAppHideConfig = 41;

        private static class Proxy implements ISecurityManager {
            public static ISecurityManager sDefaultImpl;
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

            public void killNativePackageProcesses(int uid, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(pkgName);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().killNativePackageProcesses(uid, pkgName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPackageNameByPid(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    String str = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getPackageNameByPid(pid);
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

            public boolean checkSmsBlocked(Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        if (_reply.readInt() != 0) {
                            intent.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().checkSmsBlocked(intent);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startInterceptSmsBySender(String pkgName, String sender, int count) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeString(sender);
                    _data.writeInt(count);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().startInterceptSmsBySender(pkgName, sender, count);
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

            public boolean stopInterceptSmsBySender() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().stopInterceptSmsBySender();
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

            public void addAccessControlPass(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addAccessControlPass(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeAccessControlPass(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeAccessControlPass(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkAccessControlPass(String packageName, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().checkAccessControlPass(packageName, intent);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getApplicationAccessControlEnabled(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getApplicationAccessControlEnabled(packageName);
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

            public void setApplicationAccessControlEnabled(String packageName, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationAccessControlEnabled(packageName, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getApplicationChildrenControlEnabled(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getApplicationChildrenControlEnabled(packageName);
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

            public void setApplicationChildrenControlEnabled(String packageName, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationChildrenControlEnabled(packageName, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWakeUpTime(String componentName, long timeInSeconds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(componentName);
                    _data.writeLong(timeInSeconds);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWakeUpTime(componentName, timeInSeconds);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getWakeUpTime(String componentName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(componentName);
                    long j = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getWakeUpTime(componentName);
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

            public boolean putSystemDataStringFile(String path, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeString(value);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().putSystemDataStringFile(path, value);
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

            public String readSystemDataStringFile(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    String str = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().readSystemDataStringFile(path);
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

            public void pushWakePathData(int wakeType, ParceledListSlice wakePathRuleInfos, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(wakeType);
                    if (wakePathRuleInfos != null) {
                        _data.writeInt(1);
                        wakePathRuleInfos.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().pushWakePathData(wakeType, wakePathRuleInfos, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void pushWakePathWhiteList(List<String> wakePathWhiteList, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(wakePathWhiteList);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().pushWakePathWhiteList(wakePathWhiteList, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTrackWakePathCallListLogEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTrackWakePathCallListLogEnabled(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getWakePathCallListLog() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice parceledListSlice = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getWakePathCallListLog();
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAppPermissionControlOpen(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getAppPermissionControlOpen(userId);
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

            public void setAppPermissionControlOpen(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppPermissionControlOpen(status);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerWakePathCallback(IWakePathCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerWakePathCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeAccessControlPassAsUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeAccessControlPassAsUser(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean needFinishAccessControl(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().needFinishAccessControl(token);
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

            public void finishAccessControl(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishAccessControl(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int activityResume(Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(27, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().activityResume(intent);
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

            public void setCoreRuntimePermissionEnabled(boolean grant, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(grant ? 1 : 0);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCoreRuntimePermissionEnabled(grant, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantRuntimePermission(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantRuntimePermission(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCurrentUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCurrentUserId();
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

            public boolean checkAccessControlPassAsUser(String packageName, Intent intent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().checkAccessControlPassAsUser(packageName, intent, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getApplicationAccessControlEnabledAsUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getApplicationAccessControlEnabledAsUser(packageName, userId);
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

            public void removeWakePathData(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeWakePathData(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkAllowStartActivity(String callerPkgName, String calleePkgName, Intent intent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPkgName);
                    _data.writeString(calleePkgName);
                    boolean _result = true;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().checkAllowStartActivity(callerPkgName, calleePkgName, intent, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSysAppCracked() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 35;
                    if (!this.mRemote.transact(35, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSysAppCracked();
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

            public void grantInstallPermission(String packageName, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(name);
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantInstallPermission(packageName, name);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void pushWakePathConfirmDialogWhiteList(int type, List<String> whiteList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringList(whiteList);
                    if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().pushWakePathConfirmDialogWhiteList(type, whiteList);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addAccessControlPassForUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addAccessControlPassForUser(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setApplicationAccessControlEnabledForUser(String packageName, boolean enabled, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationAccessControlEnabledForUser(packageName, enabled, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRestrictedAppNet(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(40, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isRestrictedAppNet(packageName);
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

            public boolean writeAppHideConfig(boolean hide) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(hide ? 1 : 0);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().writeAppHideConfig(hide);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void saveIcon(String fileName, Bitmap icon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fileName);
                    if (icon != null) {
                        _data.writeInt(1);
                        icon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().saveIcon(fileName, icon);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addMiuiFirewallSharedUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().addMiuiFirewallSharedUid(uid);
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

            public boolean setMiuiFirewallRule(String packageName, int uid, int rule, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    _data.writeInt(rule);
                    _data.writeInt(type);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(44, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setMiuiFirewallRule(packageName, uid, rule, type);
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

            public boolean setCurrentNetworkState(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(45, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setCurrentNetworkState(state);
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

            public void setIncompatibleAppList(List<String> list) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(list);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIncompatibleAppList(list);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getIncompatibleAppList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<String> list = 47;
                    if (!this.mRemote.transact(47, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getIncompatibleAppList();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getWakePathComponents(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    ParceledListSlice parceledListSlice = 48;
                    if (!this.mRemote.transact(48, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getWakePathComponents(packageName);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void offerGoogleBaseCallBack(ISecurityCallback client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    if (this.mRemote.transact(49, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().offerGoogleBaseCallBack(client);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyAppsPreInstalled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(50, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyAppsPreInstalled();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public boolean getApplicationMaskNotificationEnabledAsUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(51, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getApplicationMaskNotificationEnabledAsUser(packageName, userId);
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

            public void setApplicationMaskNotificationEnabledForUser(String packageName, boolean enabled, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(52, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationMaskNotificationEnabledForUser(packageName, enabled, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean areNotificationsEnabledForPackage(String packageName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(53, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().areNotificationsEnabledForPackage(packageName, uid);
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

            public void setNotificationsEnabledForPackage(String packageName, int uid, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificationsEnabledForPackage(packageName, uid, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAppHide() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(55, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAppHide();
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

            public boolean isFunctionOpen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(56, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isFunctionOpen();
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

            public boolean setAppHide(boolean hide) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(hide ? 1 : 0);
                    if (this.mRemote.transact(57, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setAppHide(hide);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isValidDevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(58, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isValidDevice();
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

            public boolean checkGameBoosterAntimsgPassAsUser(String packageName, Intent intent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().checkGameBoosterAntimsgPassAsUser(packageName, intent, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGameBoosterIBinder(IBinder gamebooster, int userId, boolean isGameMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(gamebooster);
                    _data.writeInt(userId);
                    _data.writeInt(isGameMode ? 1 : 0);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setGameBoosterIBinder(gamebooster, userId, isGameMode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getGameMode(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(61, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getGameMode(userId);
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

            public void setAccessControlPassword(String passwordType, String password, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(passwordType);
                    _data.writeString(password);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(62, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAccessControlPassword(passwordType, password, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkAccessControlPassword(String passwordType, String password, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(passwordType);
                    _data.writeString(password);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(63, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().checkAccessControlPassword(passwordType, password, userId);
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

            public boolean haveAccessControlPassword(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(64, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().haveAccessControlPassword(userId);
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

            public String getAccessControlPasswordType(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    String str = 65;
                    if (!this.mRemote.transact(65, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getAccessControlPasswordType(userId);
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

            public void setAppPrivacyStatus(String packageName, boolean isOpen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(isOpen ? 1 : 0);
                    if (this.mRemote.transact(66, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppPrivacyStatus(packageName, isOpen);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAppPrivacyEnabled(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(67, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAppPrivacyEnabled(packageName);
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

            public boolean isAllowStartService(Intent service, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(68, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isAllowStartService(service, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder getTopActivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = 69;
                    if (!this.mRemote.transact(69, _data, _reply, 0)) {
                        iBinder = Stub.getDefaultImpl();
                        if (iBinder != 0) {
                            iBinder = Stub.getDefaultImpl().getTopActivity();
                            return iBinder;
                        }
                    }
                    _reply.readException();
                    iBinder = _reply.readStrongBinder();
                    IBinder _result = iBinder;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder getAppRunningControlIBinder() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = 70;
                    if (!this.mRemote.transact(70, _data, _reply, 0)) {
                        iBinder = Stub.getDefaultImpl();
                        if (iBinder != 0) {
                            iBinder = Stub.getDefaultImpl().getAppRunningControlIBinder();
                            return iBinder;
                        }
                    }
                    _reply.readException();
                    iBinder = _reply.readStrongBinder();
                    IBinder _result = iBinder;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void watchGreenGuardProcess() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(71, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().watchGreenGuardProcess();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSecondSpaceId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 72;
                    if (!this.mRemote.transact(72, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSecondSpaceId();
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

            public int moveTaskToStack(int taskId, int stackId, boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    _data.writeInt(toTop ? 1 : 0);
                    int i = this.mRemote;
                    if (!i.transact(73, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().moveTaskToStack(taskId, stackId, toTop);
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

            public int resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (bounds != null) {
                        _data.writeInt(1);
                        bounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(resizeMode);
                    int i = this.mRemote;
                    if (!i.transact(74, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().resizeTask(taskId, bounds, resizeMode);
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

            public void setStickWindowName(String component) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(component);
                    if (this.mRemote.transact(75, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setStickWindowName(component);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getStickWindowName(String component) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(component);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(76, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getStickWindowName(component);
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

            public void pushUpdatePkgsData(List<String> updatePkgsList, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(updatePkgsList);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(77, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().pushUpdatePkgsData(updatePkgsList, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPrivacyApp(String packageName, int userId, boolean isPrivacy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(isPrivacy ? 1 : 0);
                    if (this.mRemote.transact(78, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPrivacyApp(packageName, userId, isPrivacy);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPrivacyApp(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(79, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPrivacyApp(packageName, userId);
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

            public List<String> getAllPrivacyApps(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 80;
                    if (!this.mRemote.transact(80, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAllPrivacyApps(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateLauncherPackageNames() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(81, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateLauncherPackageNames();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantRuntimePermissionAsUser(String packageName, String permName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(82, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantRuntimePermissionAsUser(packageName, permName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void revokeRuntimePermissionAsUser(String packageName, String permName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(83, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().revokeRuntimePermissionAsUser(packageName, permName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void revokeRuntimePermissionAsUserNotKill(String packageName, String permName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(84, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().revokeRuntimePermissionAsUserNotKill(packageName, permName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPermissionFlagsAsUser(String permName, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permName);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    int i = 85;
                    if (!this.mRemote.transact(85, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPermissionFlagsAsUser(permName, packageName, userId);
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

            public void updatePermissionFlagsAsUser(String permissionName, String packageName, int flagMask, int flagValues, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permissionName);
                    _data.writeString(packageName);
                    _data.writeInt(flagMask);
                    _data.writeInt(flagValues);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(86, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updatePermissionFlagsAsUser(permissionName, packageName, flagMask, flagValues, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISecurityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISecurityManager)) {
                return new Proxy(obj);
            }
            return (ISecurityManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "killNativePackageProcesses";
                case 2:
                    return "getPackageNameByPid";
                case 3:
                    return "checkSmsBlocked";
                case 4:
                    return "startInterceptSmsBySender";
                case 5:
                    return "stopInterceptSmsBySender";
                case 6:
                    return "addAccessControlPass";
                case 7:
                    return "removeAccessControlPass";
                case 8:
                    return "checkAccessControlPass";
                case 9:
                    return "getApplicationAccessControlEnabled";
                case 10:
                    return "setApplicationAccessControlEnabled";
                case 11:
                    return "getApplicationChildrenControlEnabled";
                case 12:
                    return "setApplicationChildrenControlEnabled";
                case 13:
                    return "setWakeUpTime";
                case 14:
                    return "getWakeUpTime";
                case 15:
                    return "putSystemDataStringFile";
                case 16:
                    return "readSystemDataStringFile";
                case 17:
                    return "pushWakePathData";
                case 18:
                    return "pushWakePathWhiteList";
                case 19:
                    return "setTrackWakePathCallListLogEnabled";
                case 20:
                    return "getWakePathCallListLog";
                case 21:
                    return "getAppPermissionControlOpen";
                case 22:
                    return "setAppPermissionControlOpen";
                case 23:
                    return "registerWakePathCallback";
                case 24:
                    return "removeAccessControlPassAsUser";
                case 25:
                    return "needFinishAccessControl";
                case 26:
                    return "finishAccessControl";
                case 27:
                    return "activityResume";
                case 28:
                    return "setCoreRuntimePermissionEnabled";
                case 29:
                    return "grantRuntimePermission";
                case 30:
                    return "getCurrentUserId";
                case 31:
                    return "checkAccessControlPassAsUser";
                case 32:
                    return "getApplicationAccessControlEnabledAsUser";
                case 33:
                    return "removeWakePathData";
                case 34:
                    return "checkAllowStartActivity";
                case 35:
                    return "getSysAppCracked";
                case 36:
                    return "grantInstallPermission";
                case 37:
                    return "pushWakePathConfirmDialogWhiteList";
                case 38:
                    return "addAccessControlPassForUser";
                case 39:
                    return "setApplicationAccessControlEnabledForUser";
                case 40:
                    return "isRestrictedAppNet";
                case 41:
                    return "writeAppHideConfig";
                case 42:
                    return "saveIcon";
                case 43:
                    return "addMiuiFirewallSharedUid";
                case 44:
                    return "setMiuiFirewallRule";
                case 45:
                    return "setCurrentNetworkState";
                case 46:
                    return "setIncompatibleAppList";
                case 47:
                    return "getIncompatibleAppList";
                case 48:
                    return "getWakePathComponents";
                case 49:
                    return "offerGoogleBaseCallBack";
                case 50:
                    return "notifyAppsPreInstalled";
                case 51:
                    return "getApplicationMaskNotificationEnabledAsUser";
                case 52:
                    return "setApplicationMaskNotificationEnabledForUser";
                case 53:
                    return "areNotificationsEnabledForPackage";
                case 54:
                    return "setNotificationsEnabledForPackage";
                case 55:
                    return "isAppHide";
                case 56:
                    return "isFunctionOpen";
                case 57:
                    return "setAppHide";
                case 58:
                    return "isValidDevice";
                case 59:
                    return "checkGameBoosterAntimsgPassAsUser";
                case 60:
                    return "setGameBoosterIBinder";
                case 61:
                    return "getGameMode";
                case 62:
                    return "setAccessControlPassword";
                case 63:
                    return "checkAccessControlPassword";
                case 64:
                    return "haveAccessControlPassword";
                case 65:
                    return "getAccessControlPasswordType";
                case 66:
                    return "setAppPrivacyStatus";
                case 67:
                    return "isAppPrivacyEnabled";
                case 68:
                    return "isAllowStartService";
                case 69:
                    return "getTopActivity";
                case 70:
                    return "getAppRunningControlIBinder";
                case 71:
                    return "watchGreenGuardProcess";
                case 72:
                    return "getSecondSpaceId";
                case 73:
                    return "moveTaskToStack";
                case 74:
                    return "resizeTask";
                case 75:
                    return "setStickWindowName";
                case 76:
                    return "getStickWindowName";
                case 77:
                    return "pushUpdatePkgsData";
                case 78:
                    return "setPrivacyApp";
                case 79:
                    return "isPrivacyApp";
                case 80:
                    return "getAllPrivacyApps";
                case 81:
                    return "updateLauncherPackageNames";
                case 82:
                    return "grantRuntimePermissionAsUser";
                case 83:
                    return "revokeRuntimePermissionAsUser";
                case 84:
                    return "revokeRuntimePermissionAsUserNotKill";
                case 85:
                    return "getPermissionFlagsAsUser";
                case 86:
                    return "updatePermissionFlagsAsUser";
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
                String _result;
                Intent _arg0;
                boolean _result2;
                boolean _result3;
                String _arg02;
                boolean _result4;
                int _arg03;
                ParceledListSlice _arg12;
                int _result5;
                Intent _arg04;
                boolean _result6;
                int _arg13;
                IBinder _result7;
                int _result8;
                List<String> _arg05;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        killNativePackageProcesses(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _result = getPackageNameByPid(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = checkSmsBlocked(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        if (_arg0 != null) {
                            parcel2.writeInt(1);
                            _arg0.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _result3 = startInterceptSmsBySender(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg1 = stopInterceptSmsBySender();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        addAccessControlPass(data.readString());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        removeAccessControlPass(data.readString());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = checkAccessControlPass(_arg02, _arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _result4 = getApplicationAccessControlEnabled(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setApplicationAccessControlEnabled(_result, _arg1);
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result4 = getApplicationChildrenControlEnabled(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setApplicationChildrenControlEnabled(_result, _arg1);
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        setWakeUpTime(data.readString(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        long _result9 = getWakeUpTime(data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result9);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _result2 = putSystemDataStringFile(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _result = readSystemDataStringFile(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        pushWakePathData(_arg03, _arg12, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        pushWakePathWhiteList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setTrackWakePathCallListLogEnabled(_arg1);
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _arg12 = getWakePathCallListLog();
                        reply.writeNoException();
                        if (_arg12 != null) {
                            parcel2.writeInt(1);
                            _arg12.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _result5 = getAppPermissionControlOpen(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        setAppPermissionControlOpen(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        registerWakePathCallback(com.android.internal.app.IWakePathCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        removeAccessControlPassAsUser(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _result4 = needFinishAccessControl(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        finishAccessControl(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result5 = activityResume(_arg04);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setCoreRuntimePermissionEnabled(_arg1, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        grantRuntimePermission(data.readString());
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        _arg03 = getCurrentUserId();
                        reply.writeNoException();
                        parcel2.writeInt(_arg03);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = checkAccessControlPassAsUser(_arg02, _arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        _result2 = getApplicationAccessControlEnabledAsUser(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        removeWakePathData(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 34:
                        Intent _arg2;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result6 = checkAllowStartActivity(_arg02, _result, _arg2, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _arg03 = getSysAppCracked();
                        reply.writeNoException();
                        parcel2.writeInt(_arg03);
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        grantInstallPermission(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        pushWakePathConfirmDialogWhiteList(data.readInt(), data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        addAccessControlPassForUser(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setApplicationAccessControlEnabledForUser(_result, _arg1, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        _result4 = isRestrictedAppNet(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result4 = writeAppHideConfig(_arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 42:
                        Bitmap _arg14;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg14 = (Bitmap) Bitmap.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        saveIcon(_arg02, _arg14);
                        reply.writeNoException();
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        _result4 = addMiuiFirewallSharedUid(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        _result6 = setMiuiFirewallRule(data.readString(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        _result4 = setCurrentNetworkState(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        setIncompatibleAppList(data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        List<String> _result10 = getIncompatibleAppList();
                        reply.writeNoException();
                        parcel2.writeStringList(_result10);
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result11 = getWakePathComponents(data.readString());
                        reply.writeNoException();
                        if (_result11 != null) {
                            parcel2.writeInt(1);
                            _result11.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        offerGoogleBaseCallBack(miui.security.ISecurityCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        notifyAppsPreInstalled();
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        _result2 = getApplicationMaskNotificationEnabledAsUser(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setApplicationMaskNotificationEnabledForUser(_result, _arg1, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        _result2 = areNotificationsEnabledForPackage(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        _result = data.readString();
                        _arg13 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setNotificationsEnabledForPackage(_result, _arg13, _arg1);
                        reply.writeNoException();
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isAppHide();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isFunctionOpen();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result4 = setAppHide(_arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 58:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isValidDevice();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = checkGameBoosterAntimsgPassAsUser(_arg02, _arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        IBinder _arg06 = data.readStrongBinder();
                        _arg13 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setGameBoosterIBinder(_arg06, _arg13, _arg1);
                        reply.writeNoException();
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        _result4 = getGameMode(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 62:
                        parcel.enforceInterface(descriptor);
                        setAccessControlPassword(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        _result3 = checkAccessControlPassword(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        _result4 = haveAccessControlPassword(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 65:
                        parcel.enforceInterface(descriptor);
                        _result = getAccessControlPasswordType(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result);
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setAppPrivacyStatus(_result, _arg1);
                        reply.writeNoException();
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        _result4 = isAppPrivacyEnabled(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result2 = isAllowStartService(_arg04, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 69:
                        parcel.enforceInterface(descriptor);
                        _result7 = getTopActivity();
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result7);
                        return true;
                    case 70:
                        parcel.enforceInterface(descriptor);
                        _result7 = getAppRunningControlIBinder();
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result7);
                        return true;
                    case 71:
                        parcel.enforceInterface(descriptor);
                        watchGreenGuardProcess();
                        reply.writeNoException();
                        return true;
                    case 72:
                        parcel.enforceInterface(descriptor);
                        _arg03 = getSecondSpaceId();
                        reply.writeNoException();
                        parcel2.writeInt(_arg03);
                        return true;
                    case 73:
                        parcel.enforceInterface(descriptor);
                        _result5 = data.readInt();
                        _arg13 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result8 = moveTaskToStack(_result5, _arg13, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 74:
                        Rect _arg15;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg15 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        _result8 = resizeTask(_arg03, _arg15, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 75:
                        parcel.enforceInterface(descriptor);
                        setStickWindowName(data.readString());
                        reply.writeNoException();
                        return true;
                    case 76:
                        parcel.enforceInterface(descriptor);
                        _result4 = getStickWindowName(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 77:
                        parcel.enforceInterface(descriptor);
                        _arg05 = data.createStringArrayList();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        pushUpdatePkgsData(_arg05, _arg1);
                        reply.writeNoException();
                        return true;
                    case 78:
                        parcel.enforceInterface(descriptor);
                        _result = data.readString();
                        _arg13 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setPrivacyApp(_result, _arg13, _arg1);
                        reply.writeNoException();
                        return true;
                    case 79:
                        parcel.enforceInterface(descriptor);
                        _result2 = isPrivacyApp(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 80:
                        parcel.enforceInterface(descriptor);
                        _arg05 = getAllPrivacyApps(data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringList(_arg05);
                        return true;
                    case 81:
                        parcel.enforceInterface(descriptor);
                        updateLauncherPackageNames();
                        reply.writeNoException();
                        return true;
                    case 82:
                        parcel.enforceInterface(descriptor);
                        grantRuntimePermissionAsUser(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 83:
                        parcel.enforceInterface(descriptor);
                        revokeRuntimePermissionAsUser(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 84:
                        parcel.enforceInterface(descriptor);
                        revokeRuntimePermissionAsUserNotKill(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 85:
                        parcel.enforceInterface(descriptor);
                        _result8 = getPermissionFlagsAsUser(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 86:
                        parcel.enforceInterface(descriptor);
                        updatePermissionFlagsAsUser(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ISecurityManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISecurityManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int activityResume(Intent intent) throws RemoteException;

    void addAccessControlPass(String str) throws RemoteException;

    void addAccessControlPassForUser(String str, int i) throws RemoteException;

    boolean addMiuiFirewallSharedUid(int i) throws RemoteException;

    boolean areNotificationsEnabledForPackage(String str, int i) throws RemoteException;

    boolean checkAccessControlPass(String str, Intent intent) throws RemoteException;

    boolean checkAccessControlPassAsUser(String str, Intent intent, int i) throws RemoteException;

    boolean checkAccessControlPassword(String str, String str2, int i) throws RemoteException;

    boolean checkAllowStartActivity(String str, String str2, Intent intent, int i) throws RemoteException;

    boolean checkGameBoosterAntimsgPassAsUser(String str, Intent intent, int i) throws RemoteException;

    boolean checkSmsBlocked(Intent intent) throws RemoteException;

    void finishAccessControl(String str, int i) throws RemoteException;

    String getAccessControlPasswordType(int i) throws RemoteException;

    List<String> getAllPrivacyApps(int i) throws RemoteException;

    int getAppPermissionControlOpen(int i) throws RemoteException;

    IBinder getAppRunningControlIBinder() throws RemoteException;

    boolean getApplicationAccessControlEnabled(String str) throws RemoteException;

    boolean getApplicationAccessControlEnabledAsUser(String str, int i) throws RemoteException;

    boolean getApplicationChildrenControlEnabled(String str) throws RemoteException;

    boolean getApplicationMaskNotificationEnabledAsUser(String str, int i) throws RemoteException;

    int getCurrentUserId() throws RemoteException;

    boolean getGameMode(int i) throws RemoteException;

    List<String> getIncompatibleAppList() throws RemoteException;

    String getPackageNameByPid(int i) throws RemoteException;

    int getPermissionFlagsAsUser(String str, String str2, int i) throws RemoteException;

    int getSecondSpaceId() throws RemoteException;

    boolean getStickWindowName(String str) throws RemoteException;

    int getSysAppCracked() throws RemoteException;

    IBinder getTopActivity() throws RemoteException;

    ParceledListSlice getWakePathCallListLog() throws RemoteException;

    ParceledListSlice getWakePathComponents(String str) throws RemoteException;

    long getWakeUpTime(String str) throws RemoteException;

    void grantInstallPermission(String str, String str2) throws RemoteException;

    void grantRuntimePermission(String str) throws RemoteException;

    void grantRuntimePermissionAsUser(String str, String str2, int i) throws RemoteException;

    boolean haveAccessControlPassword(int i) throws RemoteException;

    boolean isAllowStartService(Intent intent, int i) throws RemoteException;

    boolean isAppHide() throws RemoteException;

    boolean isAppPrivacyEnabled(String str) throws RemoteException;

    boolean isFunctionOpen() throws RemoteException;

    boolean isPrivacyApp(String str, int i) throws RemoteException;

    boolean isRestrictedAppNet(String str) throws RemoteException;

    boolean isValidDevice() throws RemoteException;

    void killNativePackageProcesses(int i, String str) throws RemoteException;

    int moveTaskToStack(int i, int i2, boolean z) throws RemoteException;

    boolean needFinishAccessControl(IBinder iBinder) throws RemoteException;

    void notifyAppsPreInstalled() throws RemoteException;

    void offerGoogleBaseCallBack(ISecurityCallback iSecurityCallback) throws RemoteException;

    void pushUpdatePkgsData(List<String> list, boolean z) throws RemoteException;

    void pushWakePathConfirmDialogWhiteList(int i, List<String> list) throws RemoteException;

    void pushWakePathData(int i, ParceledListSlice parceledListSlice, int i2) throws RemoteException;

    void pushWakePathWhiteList(List<String> list, int i) throws RemoteException;

    boolean putSystemDataStringFile(String str, String str2) throws RemoteException;

    String readSystemDataStringFile(String str) throws RemoteException;

    void registerWakePathCallback(IWakePathCallback iWakePathCallback) throws RemoteException;

    void removeAccessControlPass(String str) throws RemoteException;

    void removeAccessControlPassAsUser(String str, int i) throws RemoteException;

    void removeWakePathData(int i) throws RemoteException;

    int resizeTask(int i, Rect rect, int i2) throws RemoteException;

    void revokeRuntimePermissionAsUser(String str, String str2, int i) throws RemoteException;

    void revokeRuntimePermissionAsUserNotKill(String str, String str2, int i) throws RemoteException;

    void saveIcon(String str, Bitmap bitmap) throws RemoteException;

    void setAccessControlPassword(String str, String str2, int i) throws RemoteException;

    boolean setAppHide(boolean z) throws RemoteException;

    void setAppPermissionControlOpen(int i) throws RemoteException;

    void setAppPrivacyStatus(String str, boolean z) throws RemoteException;

    void setApplicationAccessControlEnabled(String str, boolean z) throws RemoteException;

    void setApplicationAccessControlEnabledForUser(String str, boolean z, int i) throws RemoteException;

    void setApplicationChildrenControlEnabled(String str, boolean z) throws RemoteException;

    void setApplicationMaskNotificationEnabledForUser(String str, boolean z, int i) throws RemoteException;

    void setCoreRuntimePermissionEnabled(boolean z, int i) throws RemoteException;

    boolean setCurrentNetworkState(int i) throws RemoteException;

    void setGameBoosterIBinder(IBinder iBinder, int i, boolean z) throws RemoteException;

    void setIncompatibleAppList(List<String> list) throws RemoteException;

    boolean setMiuiFirewallRule(String str, int i, int i2, int i3) throws RemoteException;

    void setNotificationsEnabledForPackage(String str, int i, boolean z) throws RemoteException;

    void setPrivacyApp(String str, int i, boolean z) throws RemoteException;

    void setStickWindowName(String str) throws RemoteException;

    void setTrackWakePathCallListLogEnabled(boolean z) throws RemoteException;

    void setWakeUpTime(String str, long j) throws RemoteException;

    boolean startInterceptSmsBySender(String str, String str2, int i) throws RemoteException;

    boolean stopInterceptSmsBySender() throws RemoteException;

    void updateLauncherPackageNames() throws RemoteException;

    void updatePermissionFlagsAsUser(String str, String str2, int i, int i2, int i3) throws RemoteException;

    void watchGreenGuardProcess() throws RemoteException;

    boolean writeAppHideConfig(boolean z) throws RemoteException;
}
