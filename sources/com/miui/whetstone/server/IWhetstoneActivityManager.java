package com.miui.whetstone.server;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.miui.whetstone.IPowerKeeperPolicy;
import com.miui.whetstone.strategy.WhetstonePackageInfo;
import java.util.List;

public interface IWhetstoneActivityManager extends IInterface {

    public static class Default implements IWhetstoneActivityManager {
        public String getPackageNamebyPid(int pid) throws RemoteException {
            return null;
        }

        public String[] getBackgroundAPPS() throws RemoteException {
            return null;
        }

        public boolean scheduleTrimMemory(int pid, int level) throws RemoteException {
            return false;
        }

        public boolean scheduleStopService(String packageName, ComponentName name) throws RemoteException {
            return false;
        }

        public boolean distoryActivity(int pid) throws RemoteException {
            return false;
        }

        public void bindWhetstoneService(IBinder client) throws RemoteException {
        }

        public int getSystemPid() throws RemoteException {
            return 0;
        }

        public boolean setPerformanceComponents(ComponentName[] name) throws RemoteException {
            return false;
        }

        public long getAndroidCachedEmptyProcessMemory() throws RemoteException {
            return 0;
        }

        public void updateApplicationsMemoryThreshold(List<String> list) throws RemoteException {
        }

        public void checkApplicationsMemoryThreshold(String packageName, int pid, long threshold) throws RemoteException {
        }

        public boolean putUidFrozenState(int uid, int state) throws RemoteException {
            return false;
        }

        public void updateApplicationByLockedState(String packageName, boolean locked) throws RemoteException {
        }

        public void updateApplicationByLockedStateWithUserId(String packageName, boolean locked, int userId) throws RemoteException {
        }

        public IPowerKeeperPolicy getPowerKeeperPolicy() throws RemoteException {
            return null;
        }

        public int getPartialWakeLockHoldByUid(int uid) throws RemoteException {
            return 0;
        }

        public void clearDeadAppFromNative() throws RemoteException {
        }

        public void updateUserLockedAppList(List<String> list) throws RemoteException {
        }

        public void updateUserLockedAppListWithUserId(List<String> list, int userId) throws RemoteException {
        }

        public boolean checkIfPackageIsLocked(String packageName) throws RemoteException {
            return false;
        }

        public boolean checkIfPackageIsLockedWithUserId(String packageName, int userId) throws RemoteException {
            return false;
        }

        public void updateFrameworkCommonConfig(String json) throws RemoteException {
        }

        public boolean getProcessReceiverState(int pid) throws RemoteException {
            return false;
        }

        public boolean isProcessExecutingServices(int pid) throws RemoteException {
            return false;
        }

        public void addAppToServiceControlWhitelist(List<String> list) throws RemoteException {
        }

        public void removeAppFromServiceControlWhitelist(String pkgName) throws RemoteException {
        }

        public boolean removeTaskById(int taskId, boolean killProcess) throws RemoteException {
            return false;
        }

        public boolean getConnProviderNames(String packageName, int userId, List<String> list) throws RemoteException {
            return false;
        }

        public void setWhetstonePackageInfo(List<WhetstonePackageInfo> list, boolean isAppend) throws RemoteException {
        }

        public void setGmsBlockerEnable(int uid, boolean enable) throws RemoteException {
        }

        public boolean initGmsChain(String name, int uid, String rule) throws RemoteException {
            return false;
        }

        public boolean setGmsChainState(String name, boolean enable) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWhetstoneActivityManager {
        private static final String DESCRIPTOR = "com.miui.whetstone.server.IWhetstoneActivityManager";
        static final int TRANSACTION_addAppToServiceControlWhitelist = 25;
        static final int TRANSACTION_bindWhetstoneService = 6;
        static final int TRANSACTION_checkApplicationsMemoryThreshold = 11;
        static final int TRANSACTION_checkIfPackageIsLocked = 20;
        static final int TRANSACTION_checkIfPackageIsLockedWithUserId = 21;
        static final int TRANSACTION_clearDeadAppFromNative = 17;
        static final int TRANSACTION_distoryActivity = 5;
        static final int TRANSACTION_getAndroidCachedEmptyProcessMemory = 9;
        static final int TRANSACTION_getBackgroundAPPS = 2;
        static final int TRANSACTION_getConnProviderNames = 28;
        static final int TRANSACTION_getPackageNamebyPid = 1;
        static final int TRANSACTION_getPartialWakeLockHoldByUid = 16;
        static final int TRANSACTION_getPowerKeeperPolicy = 15;
        static final int TRANSACTION_getProcessReceiverState = 23;
        static final int TRANSACTION_getSystemPid = 7;
        static final int TRANSACTION_initGmsChain = 31;
        static final int TRANSACTION_isProcessExecutingServices = 24;
        static final int TRANSACTION_putUidFrozenState = 12;
        static final int TRANSACTION_removeAppFromServiceControlWhitelist = 26;
        static final int TRANSACTION_removeTaskById = 27;
        static final int TRANSACTION_scheduleStopService = 4;
        static final int TRANSACTION_scheduleTrimMemory = 3;
        static final int TRANSACTION_setGmsBlockerEnable = 30;
        static final int TRANSACTION_setGmsChainState = 32;
        static final int TRANSACTION_setPerformanceComponents = 8;
        static final int TRANSACTION_setWhetstonePackageInfo = 29;
        static final int TRANSACTION_updateApplicationByLockedState = 13;
        static final int TRANSACTION_updateApplicationByLockedStateWithUserId = 14;
        static final int TRANSACTION_updateApplicationsMemoryThreshold = 10;
        static final int TRANSACTION_updateFrameworkCommonConfig = 22;
        static final int TRANSACTION_updateUserLockedAppList = 18;
        static final int TRANSACTION_updateUserLockedAppListWithUserId = 19;

        private static class Proxy implements IWhetstoneActivityManager {
            public static IWhetstoneActivityManager sDefaultImpl;
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

            public String getPackageNamebyPid(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    String str = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getPackageNamebyPid(pid);
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

            public String[] getBackgroundAPPS() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getBackgroundAPPS();
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

            public boolean scheduleTrimMemory(int pid, int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(level);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().scheduleTrimMemory(pid, level);
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

            public boolean scheduleStopService(String packageName, ComponentName name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().scheduleStopService(packageName, name);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean distoryActivity(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().distoryActivity(pid);
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

            public void bindWhetstoneService(IBinder client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().bindWhetstoneService(client);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSystemPid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSystemPid();
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

            public boolean setPerformanceComponents(ComponentName[] name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = false;
                    _data.writeTypedArray(name, 0);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            _result = true;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setPerformanceComponents(name);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getAndroidCachedEmptyProcessMemory() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getAndroidCachedEmptyProcessMemory();
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

            public void updateApplicationsMemoryThreshold(List<String> thresholds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(thresholds);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateApplicationsMemoryThreshold(thresholds);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void checkApplicationsMemoryThreshold(String packageName, int pid, long threshold) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeLong(threshold);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().checkApplicationsMemoryThreshold(packageName, pid, threshold);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public boolean putUidFrozenState(int uid, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(state);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().putUidFrozenState(uid, state);
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

            public void updateApplicationByLockedState(String packageName, boolean locked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(locked ? 1 : 0);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateApplicationByLockedState(packageName, locked);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateApplicationByLockedStateWithUserId(String packageName, boolean locked, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(locked ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateApplicationByLockedStateWithUserId(packageName, locked, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IPowerKeeperPolicy getPowerKeeperPolicy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IPowerKeeperPolicy iPowerKeeperPolicy = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        iPowerKeeperPolicy = Stub.getDefaultImpl();
                        if (iPowerKeeperPolicy != 0) {
                            iPowerKeeperPolicy = Stub.getDefaultImpl().getPowerKeeperPolicy();
                            return iPowerKeeperPolicy;
                        }
                    }
                    _reply.readException();
                    iPowerKeeperPolicy = com.miui.whetstone.IPowerKeeperPolicy.Stub.asInterface(_reply.readStrongBinder());
                    IPowerKeeperPolicy _result = iPowerKeeperPolicy;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPartialWakeLockHoldByUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    int i = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPartialWakeLockHoldByUid(uid);
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

            public void clearDeadAppFromNative() throws RemoteException {
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
                    Stub.getDefaultImpl().clearDeadAppFromNative();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateUserLockedAppList(List<String> lockedApps) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(lockedApps);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateUserLockedAppList(lockedApps);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateUserLockedAppListWithUserId(List<String> lockedApps, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(lockedApps);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateUserLockedAppListWithUserId(lockedApps, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkIfPackageIsLocked(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().checkIfPackageIsLocked(packageName);
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

            public boolean checkIfPackageIsLockedWithUserId(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().checkIfPackageIsLockedWithUserId(packageName, userId);
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

            public void updateFrameworkCommonConfig(String json) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(json);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateFrameworkCommonConfig(json);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getProcessReceiverState(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getProcessReceiverState(pid);
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

            public boolean isProcessExecutingServices(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isProcessExecutingServices(pid);
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

            public void addAppToServiceControlWhitelist(List<String> listPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(listPkg);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addAppToServiceControlWhitelist(listPkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeAppFromServiceControlWhitelist(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeAppFromServiceControlWhitelist(pkgName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeTaskById(int taskId, boolean killProcess) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _result = true;
                    _data.writeInt(killProcess ? 1 : 0);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().removeTaskById(taskId, killProcess);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getConnProviderNames(String packageName, int userId, List<String> providers) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeStringList(providers);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getConnProviderNames(packageName, userId, providers);
                            return z;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z2 = true;
                    }
                    z = z2;
                    _reply.readStringList(providers);
                    _reply.recycle();
                    _data.recycle();
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWhetstonePackageInfo(List<WhetstonePackageInfo> info, boolean isAppend) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(info);
                    _data.writeInt(isAppend ? 1 : 0);
                    if (this.mRemote.transact(29, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setWhetstonePackageInfo(info, isAppend);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setGmsBlockerEnable(int uid, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setGmsBlockerEnable(uid, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean initGmsChain(String name, int uid, String rule) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    _data.writeString(rule);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().initGmsChain(name, uid, rule);
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

            public boolean setGmsChainState(String name, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setGmsChainState(name, enable);
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

        public static IWhetstoneActivityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWhetstoneActivityManager)) {
                return new Proxy(obj);
            }
            return (IWhetstoneActivityManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getPackageNamebyPid";
                case 2:
                    return "getBackgroundAPPS";
                case 3:
                    return "scheduleTrimMemory";
                case 4:
                    return "scheduleStopService";
                case 5:
                    return "distoryActivity";
                case 6:
                    return "bindWhetstoneService";
                case 7:
                    return "getSystemPid";
                case 8:
                    return "setPerformanceComponents";
                case 9:
                    return "getAndroidCachedEmptyProcessMemory";
                case 10:
                    return "updateApplicationsMemoryThreshold";
                case 11:
                    return "checkApplicationsMemoryThreshold";
                case 12:
                    return "putUidFrozenState";
                case 13:
                    return "updateApplicationByLockedState";
                case 14:
                    return "updateApplicationByLockedStateWithUserId";
                case 15:
                    return "getPowerKeeperPolicy";
                case 16:
                    return "getPartialWakeLockHoldByUid";
                case 17:
                    return "clearDeadAppFromNative";
                case 18:
                    return "updateUserLockedAppList";
                case 19:
                    return "updateUserLockedAppListWithUserId";
                case 20:
                    return "checkIfPackageIsLocked";
                case 21:
                    return "checkIfPackageIsLockedWithUserId";
                case 22:
                    return "updateFrameworkCommonConfig";
                case 23:
                    return "getProcessReceiverState";
                case 24:
                    return "isProcessExecutingServices";
                case 25:
                    return "addAppToServiceControlWhitelist";
                case 26:
                    return "removeAppFromServiceControlWhitelist";
                case 27:
                    return "removeTaskById";
                case 28:
                    return "getConnProviderNames";
                case 29:
                    return "setWhetstonePackageInfo";
                case 30:
                    return "setGmsBlockerEnable";
                case 31:
                    return "initGmsChain";
                case 32:
                    return "setGmsChainState";
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
                boolean _result2;
                String _arg0;
                boolean _result3;
                int _result4;
                boolean _result5;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _result = getPackageNamebyPid(data.readInt());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        String[] _result6 = getBackgroundAPPS();
                        reply.writeNoException();
                        reply.writeStringArray(_result6);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result2 = scheduleTrimMemory(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 4:
                        ComponentName _arg12;
                        data.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        _result2 = scheduleStopService(_arg0, _arg12);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result3 = distoryActivity(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        bindWhetstoneService(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        int _result7 = getSystemPid();
                        reply.writeNoException();
                        reply.writeInt(_result7);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result3 = setPerformanceComponents((ComponentName[]) data.createTypedArray(ComponentName.CREATOR));
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        long _result8 = getAndroidCachedEmptyProcessMemory();
                        reply.writeNoException();
                        reply.writeLong(_result8);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        updateApplicationsMemoryThreshold(data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        checkApplicationsMemoryThreshold(data.readString(), data.readInt(), data.readLong());
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        _result2 = putUidFrozenState(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        updateApplicationByLockedState(_result, _arg1);
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        updateApplicationByLockedStateWithUserId(_result, _arg1, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        IPowerKeeperPolicy _result9 = getPowerKeeperPolicy();
                        reply.writeNoException();
                        reply.writeStrongBinder(_result9 != null ? _result9.asBinder() : null);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        _result4 = getPartialWakeLockHoldByUid(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        clearDeadAppFromNative();
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        updateUserLockedAppList(data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        updateUserLockedAppListWithUserId(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        _result3 = checkIfPackageIsLocked(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        _result2 = checkIfPackageIsLockedWithUserId(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        updateFrameworkCommonConfig(data.readString());
                        reply.writeNoException();
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        _result3 = getProcessReceiverState(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        _result3 = isProcessExecutingServices(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        addAppToServiceControlWhitelist(data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        removeAppFromServiceControlWhitelist(data.readString());
                        reply.writeNoException();
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        _result4 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result2 = removeTaskById(_result4, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _result4 = data.readInt();
                        List<String> _arg2 = data.createStringArrayList();
                        _result5 = getConnProviderNames(_arg0, _result4, _arg2);
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        reply.writeStringList(_arg2);
                        return true;
                    case 29:
                        data.enforceInterface(descriptor);
                        List<WhetstonePackageInfo> _arg02 = data.createTypedArrayList(WhetstonePackageInfo.CREATOR);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setWhetstonePackageInfo(_arg02, _arg1);
                        return true;
                    case 30:
                        data.enforceInterface(descriptor);
                        _result4 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setGmsBlockerEnable(_result4, _arg1);
                        reply.writeNoException();
                        return true;
                    case 31:
                        data.enforceInterface(descriptor);
                        _result5 = initGmsChain(data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 32:
                        data.enforceInterface(descriptor);
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result2 = setGmsChainState(_result, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IWhetstoneActivityManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWhetstoneActivityManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addAppToServiceControlWhitelist(List<String> list) throws RemoteException;

    void bindWhetstoneService(IBinder iBinder) throws RemoteException;

    void checkApplicationsMemoryThreshold(String str, int i, long j) throws RemoteException;

    boolean checkIfPackageIsLocked(String str) throws RemoteException;

    boolean checkIfPackageIsLockedWithUserId(String str, int i) throws RemoteException;

    void clearDeadAppFromNative() throws RemoteException;

    boolean distoryActivity(int i) throws RemoteException;

    long getAndroidCachedEmptyProcessMemory() throws RemoteException;

    String[] getBackgroundAPPS() throws RemoteException;

    boolean getConnProviderNames(String str, int i, List<String> list) throws RemoteException;

    String getPackageNamebyPid(int i) throws RemoteException;

    int getPartialWakeLockHoldByUid(int i) throws RemoteException;

    IPowerKeeperPolicy getPowerKeeperPolicy() throws RemoteException;

    boolean getProcessReceiverState(int i) throws RemoteException;

    int getSystemPid() throws RemoteException;

    boolean initGmsChain(String str, int i, String str2) throws RemoteException;

    boolean isProcessExecutingServices(int i) throws RemoteException;

    boolean putUidFrozenState(int i, int i2) throws RemoteException;

    void removeAppFromServiceControlWhitelist(String str) throws RemoteException;

    boolean removeTaskById(int i, boolean z) throws RemoteException;

    boolean scheduleStopService(String str, ComponentName componentName) throws RemoteException;

    boolean scheduleTrimMemory(int i, int i2) throws RemoteException;

    void setGmsBlockerEnable(int i, boolean z) throws RemoteException;

    boolean setGmsChainState(String str, boolean z) throws RemoteException;

    boolean setPerformanceComponents(ComponentName[] componentNameArr) throws RemoteException;

    void setWhetstonePackageInfo(List<WhetstonePackageInfo> list, boolean z) throws RemoteException;

    void updateApplicationByLockedState(String str, boolean z) throws RemoteException;

    void updateApplicationByLockedStateWithUserId(String str, boolean z, int i) throws RemoteException;

    void updateApplicationsMemoryThreshold(List<String> list) throws RemoteException;

    void updateFrameworkCommonConfig(String str) throws RemoteException;

    void updateUserLockedAppList(List<String> list) throws RemoteException;

    void updateUserLockedAppListWithUserId(List<String> list, int i) throws RemoteException;
}
