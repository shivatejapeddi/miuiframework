package android.os;

import android.annotation.UnsupportedAppUsage;

public interface IPowerManager extends IInterface {

    public static class Default implements IPowerManager {
        public void acquireWakeLock(IBinder lock, int flags, String tag, String packageName, WorkSource ws, String historyTag) throws RemoteException {
        }

        public void acquireWakeLockWithUid(IBinder lock, int flags, String tag, String packageName, int uidtoblame) throws RemoteException {
        }

        public void releaseWakeLock(IBinder lock, int flags) throws RemoteException {
        }

        public void updateWakeLockUids(IBinder lock, int[] uids) throws RemoteException {
        }

        public void powerHint(int hintId, int data) throws RemoteException {
        }

        public void updateWakeLockWorkSource(IBinder lock, WorkSource ws, String historyTag) throws RemoteException {
        }

        public boolean isWakeLockLevelSupported(int level) throws RemoteException {
            return false;
        }

        public void userActivity(long time, int event, int flags) throws RemoteException {
        }

        public void wakeUp(long time, int reason, String details, String opPackageName) throws RemoteException {
        }

        public void goToSleep(long time, int reason, int flags) throws RemoteException {
        }

        public void nap(long time) throws RemoteException {
        }

        public boolean isInteractive() throws RemoteException {
            return false;
        }

        public boolean isPowerSaveMode() throws RemoteException {
            return false;
        }

        public PowerSaveState getPowerSaveState(int serviceType) throws RemoteException {
            return null;
        }

        public boolean setPowerSaveModeEnabled(boolean mode) throws RemoteException {
            return false;
        }

        public boolean setDynamicPowerSaveHint(boolean powerSaveHint, int disableThreshold) throws RemoteException {
            return false;
        }

        public boolean setAdaptivePowerSavePolicy(BatterySaverPolicyConfig config) throws RemoteException {
            return false;
        }

        public boolean setAdaptivePowerSaveEnabled(boolean enabled) throws RemoteException {
            return false;
        }

        public int getPowerSaveModeTrigger() throws RemoteException {
            return 0;
        }

        public boolean isDeviceIdleMode() throws RemoteException {
            return false;
        }

        public boolean isLightDeviceIdleMode() throws RemoteException {
            return false;
        }

        public void reboot(boolean confirm, String reason, boolean wait) throws RemoteException {
        }

        public void rebootSafeMode(boolean confirm, boolean wait) throws RemoteException {
        }

        public void shutdown(boolean confirm, String reason, boolean wait) throws RemoteException {
        }

        public void crash(String message) throws RemoteException {
        }

        public int getLastShutdownReason() throws RemoteException {
            return 0;
        }

        public int getLastSleepReason() throws RemoteException {
            return 0;
        }

        public void setStayOnSetting(int val) throws RemoteException {
        }

        public void boostScreenBrightness(long time) throws RemoteException {
        }

        public boolean isScreenBrightnessBoosted() throws RemoteException {
            return false;
        }

        public void setAttentionLight(boolean on, int color) throws RemoteException {
        }

        public void setDozeAfterScreenOff(boolean on) throws RemoteException {
        }

        public boolean forceSuspend() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPowerManager {
        private static final String DESCRIPTOR = "android.os.IPowerManager";
        static final int TRANSACTION_acquireWakeLock = 1;
        static final int TRANSACTION_acquireWakeLockWithUid = 2;
        static final int TRANSACTION_boostScreenBrightness = 29;
        static final int TRANSACTION_crash = 25;
        static final int TRANSACTION_forceSuspend = 33;
        static final int TRANSACTION_getLastShutdownReason = 26;
        static final int TRANSACTION_getLastSleepReason = 27;
        static final int TRANSACTION_getPowerSaveModeTrigger = 19;
        static final int TRANSACTION_getPowerSaveState = 14;
        static final int TRANSACTION_goToSleep = 10;
        static final int TRANSACTION_isDeviceIdleMode = 20;
        static final int TRANSACTION_isInteractive = 12;
        static final int TRANSACTION_isLightDeviceIdleMode = 21;
        static final int TRANSACTION_isPowerSaveMode = 13;
        static final int TRANSACTION_isScreenBrightnessBoosted = 30;
        static final int TRANSACTION_isWakeLockLevelSupported = 7;
        static final int TRANSACTION_nap = 11;
        static final int TRANSACTION_powerHint = 5;
        static final int TRANSACTION_reboot = 22;
        static final int TRANSACTION_rebootSafeMode = 23;
        static final int TRANSACTION_releaseWakeLock = 3;
        static final int TRANSACTION_setAdaptivePowerSaveEnabled = 18;
        static final int TRANSACTION_setAdaptivePowerSavePolicy = 17;
        static final int TRANSACTION_setAttentionLight = 31;
        static final int TRANSACTION_setDozeAfterScreenOff = 32;
        static final int TRANSACTION_setDynamicPowerSaveHint = 16;
        static final int TRANSACTION_setPowerSaveModeEnabled = 15;
        static final int TRANSACTION_setStayOnSetting = 28;
        static final int TRANSACTION_shutdown = 24;
        static final int TRANSACTION_updateWakeLockUids = 4;
        static final int TRANSACTION_updateWakeLockWorkSource = 6;
        static final int TRANSACTION_userActivity = 8;
        static final int TRANSACTION_wakeUp = 9;

        private static class Proxy implements IPowerManager {
            public static IPowerManager sDefaultImpl;
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

            public void acquireWakeLock(IBinder lock, int flags, String tag, String packageName, WorkSource ws, String historyTag) throws RemoteException {
                Throwable th;
                String str;
                String str2;
                String str3;
                int i;
                WorkSource workSource = ws;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeStrongBinder(lock);
                        try {
                            _data.writeInt(flags);
                        } catch (Throwable th2) {
                            th = th2;
                            str = tag;
                            str2 = packageName;
                            str3 = historyTag;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(tag);
                        } catch (Throwable th3) {
                            th = th3;
                            str2 = packageName;
                            str3 = historyTag;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i = flags;
                        str = tag;
                        str2 = packageName;
                        str3 = historyTag;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(packageName);
                        if (workSource != null) {
                            _data.writeInt(1);
                            workSource.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(historyTag);
                            if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().acquireWakeLock(lock, flags, tag, packageName, ws, historyTag);
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
                        str3 = historyTag;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    IBinder iBinder = lock;
                    i = flags;
                    str = tag;
                    str2 = packageName;
                    str3 = historyTag;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void acquireWakeLockWithUid(IBinder lock, int flags, String tag, String packageName, int uidtoblame) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    _data.writeInt(flags);
                    _data.writeString(tag);
                    _data.writeString(packageName);
                    _data.writeInt(uidtoblame);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().acquireWakeLockWithUid(lock, flags, tag, packageName, uidtoblame);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releaseWakeLock(IBinder lock, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().releaseWakeLock(lock, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateWakeLockUids(IBinder lock, int[] uids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    _data.writeIntArray(uids);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateWakeLockUids(lock, uids);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void powerHint(int hintId, int data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hintId);
                    _data.writeInt(data);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().powerHint(hintId, data);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void updateWakeLockWorkSource(IBinder lock, WorkSource ws, String historyTag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(historyTag);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateWakeLockWorkSource(lock, ws, historyTag);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isWakeLockLevelSupported(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(level);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isWakeLockLevelSupported(level);
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

            public void userActivity(long time, int event, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(time);
                    _data.writeInt(event);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().userActivity(time, event, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void wakeUp(long time, int reason, String details, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(time);
                    _data.writeInt(reason);
                    _data.writeString(details);
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().wakeUp(time, reason, details, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void goToSleep(long time, int reason, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(time);
                    _data.writeInt(reason);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().goToSleep(time, reason, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void nap(long time) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(time);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().nap(time);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isInteractive() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInteractive();
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

            public boolean isPowerSaveMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPowerSaveMode();
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

            public PowerSaveState getPowerSaveState(int serviceType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceType);
                    PowerSaveState powerSaveState = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        powerSaveState = Stub.getDefaultImpl();
                        if (powerSaveState != 0) {
                            powerSaveState = Stub.getDefaultImpl().getPowerSaveState(serviceType);
                            return powerSaveState;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        powerSaveState = (PowerSaveState) PowerSaveState.CREATOR.createFromParcel(_reply);
                    } else {
                        powerSaveState = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return powerSaveState;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setPowerSaveModeEnabled(boolean mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(mode ? 1 : 0);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setPowerSaveModeEnabled(mode);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setDynamicPowerSaveHint(boolean powerSaveHint, int disableThreshold) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(powerSaveHint ? 1 : 0);
                    _data.writeInt(disableThreshold);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setDynamicPowerSaveHint(powerSaveHint, disableThreshold);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setAdaptivePowerSavePolicy(BatterySaverPolicyConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setAdaptivePowerSavePolicy(config);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setAdaptivePowerSaveEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setAdaptivePowerSaveEnabled(enabled);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPowerSaveModeTrigger() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPowerSaveModeTrigger();
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

            public boolean isDeviceIdleMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDeviceIdleMode();
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

            public boolean isLightDeviceIdleMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isLightDeviceIdleMode();
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

            public void reboot(boolean confirm, String reason, boolean wait) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    _data.writeInt(confirm ? 1 : 0);
                    _data.writeString(reason);
                    if (!wait) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reboot(confirm, reason, wait);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void rebootSafeMode(boolean confirm, boolean wait) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    _data.writeInt(confirm ? 1 : 0);
                    if (!wait) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().rebootSafeMode(confirm, wait);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void shutdown(boolean confirm, String reason, boolean wait) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    _data.writeInt(confirm ? 1 : 0);
                    _data.writeString(reason);
                    if (!wait) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().shutdown(confirm, reason, wait);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void crash(String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(message);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().crash(message);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLastShutdownReason() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getLastShutdownReason();
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

            public int getLastSleepReason() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getLastSleepReason();
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

            public void setStayOnSetting(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setStayOnSetting(val);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void boostScreenBrightness(long time) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(time);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().boostScreenBrightness(time);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isScreenBrightnessBoosted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isScreenBrightnessBoosted();
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

            public void setAttentionLight(boolean on, int color) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    _data.writeInt(color);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAttentionLight(on, color);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDozeAfterScreenOff(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDozeAfterScreenOff(on);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean forceSuspend() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().forceSuspend();
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

        public static IPowerManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPowerManager)) {
                return new Proxy(obj);
            }
            return (IPowerManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "acquireWakeLock";
                case 2:
                    return "acquireWakeLockWithUid";
                case 3:
                    return "releaseWakeLock";
                case 4:
                    return "updateWakeLockUids";
                case 5:
                    return "powerHint";
                case 6:
                    return "updateWakeLockWorkSource";
                case 7:
                    return "isWakeLockLevelSupported";
                case 8:
                    return "userActivity";
                case 9:
                    return "wakeUp";
                case 10:
                    return "goToSleep";
                case 11:
                    return "nap";
                case 12:
                    return "isInteractive";
                case 13:
                    return "isPowerSaveMode";
                case 14:
                    return "getPowerSaveState";
                case 15:
                    return "setPowerSaveModeEnabled";
                case 16:
                    return "setDynamicPowerSaveHint";
                case 17:
                    return "setAdaptivePowerSavePolicy";
                case 18:
                    return "setAdaptivePowerSaveEnabled";
                case 19:
                    return "getPowerSaveModeTrigger";
                case 20:
                    return "isDeviceIdleMode";
                case 21:
                    return "isLightDeviceIdleMode";
                case 22:
                    return "reboot";
                case 23:
                    return "rebootSafeMode";
                case 24:
                    return "shutdown";
                case 25:
                    return "crash";
                case 26:
                    return "getLastShutdownReason";
                case 27:
                    return "getLastSleepReason";
                case 28:
                    return "setStayOnSetting";
                case 29:
                    return "boostScreenBrightness";
                case 30:
                    return "isScreenBrightnessBoosted";
                case 31:
                    return "setAttentionLight";
                case 32:
                    return "setDozeAfterScreenOff";
                case 33:
                    return "forceSuspend";
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
                boolean _result;
                int _result2;
                String _arg1;
                switch (i) {
                    case 1:
                        WorkSource _arg4;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg02 = data.readStrongBinder();
                        int _arg12 = data.readInt();
                        String _arg2 = data.readString();
                        String _arg3 = data.readString();
                        if (data.readInt() != 0) {
                            _arg4 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        acquireWakeLock(_arg02, _arg12, _arg2, _arg3, _arg4, data.readString());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        acquireWakeLockWithUid(data.readStrongBinder(), data.readInt(), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        releaseWakeLock(data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        updateWakeLockUids(data.readStrongBinder(), data.createIntArray());
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        powerHint(data.readInt(), data.readInt());
                        return true;
                    case 6:
                        WorkSource _arg13;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg03 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg13 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        updateWakeLockWorkSource(_arg03, _arg13, data.readString());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result = isWakeLockLevelSupported(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        userActivity(data.readLong(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        wakeUp(data.readLong(), data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        goToSleep(data.readLong(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        nap(data.readLong());
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isInteractive();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isPowerSaveMode();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        PowerSaveState _result3 = getPowerSaveState(data.readInt());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result = setPowerSaveModeEnabled(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        boolean _result4 = setDynamicPowerSaveHint(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 17:
                        BatterySaverPolicyConfig _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (BatterySaverPolicyConfig) BatterySaverPolicyConfig.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result = setAdaptivePowerSavePolicy(_arg04);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result = setAdaptivePowerSaveEnabled(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _result2 = getPowerSaveModeTrigger();
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isDeviceIdleMode();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isLightDeviceIdleMode();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt() != 0;
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        reboot(_result, _arg1, _arg0);
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        rebootSafeMode(_result, _arg0);
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt() != 0;
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        shutdown(_result, _arg1, _arg0);
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        crash(data.readString());
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _result2 = getLastShutdownReason();
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        _result2 = getLastSleepReason();
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        setStayOnSetting(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        boostScreenBrightness(data.readLong());
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isScreenBrightnessBoosted();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setAttentionLight(_arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setDozeAfterScreenOff(_arg0);
                        reply.writeNoException();
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        _arg0 = forceSuspend();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPowerManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPowerManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void acquireWakeLock(IBinder iBinder, int i, String str, String str2, WorkSource workSource, String str3) throws RemoteException;

    void acquireWakeLockWithUid(IBinder iBinder, int i, String str, String str2, int i2) throws RemoteException;

    void boostScreenBrightness(long j) throws RemoteException;

    void crash(String str) throws RemoteException;

    boolean forceSuspend() throws RemoteException;

    int getLastShutdownReason() throws RemoteException;

    int getLastSleepReason() throws RemoteException;

    int getPowerSaveModeTrigger() throws RemoteException;

    PowerSaveState getPowerSaveState(int i) throws RemoteException;

    @UnsupportedAppUsage
    void goToSleep(long j, int i, int i2) throws RemoteException;

    boolean isDeviceIdleMode() throws RemoteException;

    @UnsupportedAppUsage
    boolean isInteractive() throws RemoteException;

    boolean isLightDeviceIdleMode() throws RemoteException;

    boolean isPowerSaveMode() throws RemoteException;

    boolean isScreenBrightnessBoosted() throws RemoteException;

    boolean isWakeLockLevelSupported(int i) throws RemoteException;

    void nap(long j) throws RemoteException;

    void powerHint(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void reboot(boolean z, String str, boolean z2) throws RemoteException;

    void rebootSafeMode(boolean z, boolean z2) throws RemoteException;

    @UnsupportedAppUsage
    void releaseWakeLock(IBinder iBinder, int i) throws RemoteException;

    boolean setAdaptivePowerSaveEnabled(boolean z) throws RemoteException;

    boolean setAdaptivePowerSavePolicy(BatterySaverPolicyConfig batterySaverPolicyConfig) throws RemoteException;

    void setAttentionLight(boolean z, int i) throws RemoteException;

    void setDozeAfterScreenOff(boolean z) throws RemoteException;

    boolean setDynamicPowerSaveHint(boolean z, int i) throws RemoteException;

    boolean setPowerSaveModeEnabled(boolean z) throws RemoteException;

    void setStayOnSetting(int i) throws RemoteException;

    void shutdown(boolean z, String str, boolean z2) throws RemoteException;

    void updateWakeLockUids(IBinder iBinder, int[] iArr) throws RemoteException;

    void updateWakeLockWorkSource(IBinder iBinder, WorkSource workSource, String str) throws RemoteException;

    @UnsupportedAppUsage
    void userActivity(long j, int i, int i2) throws RemoteException;

    void wakeUp(long j, int i, String str, String str2) throws RemoteException;
}
