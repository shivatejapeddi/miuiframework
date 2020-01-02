package com.android.internal.app;

import android.annotation.UnsupportedAppUsage;
import android.bluetooth.BluetoothActivityEnergyInfo;
import android.net.wifi.WifiActivityEnergyInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.WorkSource;
import android.os.connectivity.CellularBatteryStats;
import android.os.connectivity.GpsBatteryStats;
import android.os.connectivity.WifiBatteryStats;
import android.os.health.HealthStatsParceler;
import android.telephony.ModemActivityInfo;
import android.telephony.SignalStrength;

public interface IBatteryStats extends IInterface {

    public static class Default implements IBatteryStats {
        public void noteStartSensor(int uid, int sensor) throws RemoteException {
        }

        public void noteStopSensor(int uid, int sensor) throws RemoteException {
        }

        public void noteStartVideo(int uid) throws RemoteException {
        }

        public void noteStopVideo(int uid) throws RemoteException {
        }

        public void noteStartAudio(int uid) throws RemoteException {
        }

        public void noteStopAudio(int uid) throws RemoteException {
        }

        public void noteResetVideo() throws RemoteException {
        }

        public void noteResetAudio() throws RemoteException {
        }

        public void noteFlashlightOn(int uid) throws RemoteException {
        }

        public void noteFlashlightOff(int uid) throws RemoteException {
        }

        public void noteStartCamera(int uid) throws RemoteException {
        }

        public void noteStopCamera(int uid) throws RemoteException {
        }

        public void noteResetCamera() throws RemoteException {
        }

        public void noteResetFlashlight() throws RemoteException {
        }

        public byte[] getStatistics() throws RemoteException {
            return null;
        }

        public ParcelFileDescriptor getStatisticsStream() throws RemoteException {
            return null;
        }

        public boolean isCharging() throws RemoteException {
            return false;
        }

        public long computeBatteryTimeRemaining() throws RemoteException {
            return 0;
        }

        public long computeChargeTimeRemaining() throws RemoteException {
            return 0;
        }

        public void noteEvent(int code, String name, int uid) throws RemoteException {
        }

        public void noteSyncStart(String name, int uid) throws RemoteException {
        }

        public void noteSyncFinish(String name, int uid) throws RemoteException {
        }

        public void noteJobStart(String name, int uid, int standbyBucket, int jobid) throws RemoteException {
        }

        public void noteJobFinish(String name, int uid, int stopReason, int standbyBucket, int jobid) throws RemoteException {
        }

        public void noteStartWakelock(int uid, int pid, String name, String historyName, int type, boolean unimportantForLogging) throws RemoteException {
        }

        public void noteStopWakelock(int uid, int pid, String name, String historyName, int type) throws RemoteException {
        }

        public void noteStartWakelockFromSource(WorkSource ws, int pid, String name, String historyName, int type, boolean unimportantForLogging) throws RemoteException {
        }

        public void noteChangeWakelockFromSource(WorkSource ws, int pid, String name, String histyoryName, int type, WorkSource newWs, int newPid, String newName, String newHistoryName, int newType, boolean newUnimportantForLogging) throws RemoteException {
        }

        public void noteStopWakelockFromSource(WorkSource ws, int pid, String name, String historyName, int type) throws RemoteException {
        }

        public void noteLongPartialWakelockStart(String name, String historyName, int uid) throws RemoteException {
        }

        public void noteLongPartialWakelockStartFromSource(String name, String historyName, WorkSource workSource) throws RemoteException {
        }

        public void noteLongPartialWakelockFinish(String name, String historyName, int uid) throws RemoteException {
        }

        public void noteLongPartialWakelockFinishFromSource(String name, String historyName, WorkSource workSource) throws RemoteException {
        }

        public void noteVibratorOn(int uid, long durationMillis) throws RemoteException {
        }

        public void noteVibratorOff(int uid) throws RemoteException {
        }

        public void noteGpsChanged(WorkSource oldSource, WorkSource newSource) throws RemoteException {
        }

        public void noteGpsSignalQuality(int signalLevel) throws RemoteException {
        }

        public void noteScreenState(int state) throws RemoteException {
        }

        public void noteScreenBrightness(int brightness) throws RemoteException {
        }

        public void noteUserActivity(int uid, int event) throws RemoteException {
        }

        public void noteWakeUp(String reason, int reasonUid) throws RemoteException {
        }

        public void noteInteractive(boolean interactive) throws RemoteException {
        }

        public void noteConnectivityChanged(int type, String extra) throws RemoteException {
        }

        public void noteMobileRadioPowerState(int powerState, long timestampNs, int uid) throws RemoteException {
        }

        public void notePhoneOn() throws RemoteException {
        }

        public void notePhoneOff() throws RemoteException {
        }

        public void notePhoneSignalStrength(SignalStrength signalStrength) throws RemoteException {
        }

        public void notePhoneDataConnectionState(int dataType, boolean hasData) throws RemoteException {
        }

        public void notePhoneState(int phoneState) throws RemoteException {
        }

        public void noteWifiOn() throws RemoteException {
        }

        public void noteWifiOff() throws RemoteException {
        }

        public void noteWifiRunning(WorkSource ws) throws RemoteException {
        }

        public void noteWifiRunningChanged(WorkSource oldWs, WorkSource newWs) throws RemoteException {
        }

        public void noteWifiStopped(WorkSource ws) throws RemoteException {
        }

        public void noteWifiState(int wifiState, String accessPoint) throws RemoteException {
        }

        public void noteWifiSupplicantStateChanged(int supplState, boolean failedAuth) throws RemoteException {
        }

        public void noteWifiRssiChanged(int newRssi) throws RemoteException {
        }

        public void noteFullWifiLockAcquired(int uid) throws RemoteException {
        }

        public void noteFullWifiLockReleased(int uid) throws RemoteException {
        }

        public void noteWifiScanStarted(int uid) throws RemoteException {
        }

        public void noteWifiScanStopped(int uid) throws RemoteException {
        }

        public void noteWifiMulticastEnabled(int uid) throws RemoteException {
        }

        public void noteWifiMulticastDisabled(int uid) throws RemoteException {
        }

        public void noteFullWifiLockAcquiredFromSource(WorkSource ws) throws RemoteException {
        }

        public void noteFullWifiLockReleasedFromSource(WorkSource ws) throws RemoteException {
        }

        public void noteWifiScanStartedFromSource(WorkSource ws) throws RemoteException {
        }

        public void noteWifiScanStoppedFromSource(WorkSource ws) throws RemoteException {
        }

        public void noteWifiBatchedScanStartedFromSource(WorkSource ws, int csph) throws RemoteException {
        }

        public void noteWifiBatchedScanStoppedFromSource(WorkSource ws) throws RemoteException {
        }

        public void noteWifiRadioPowerState(int powerState, long timestampNs, int uid) throws RemoteException {
        }

        public void noteNetworkInterfaceType(String iface, int type) throws RemoteException {
        }

        public void noteNetworkStatsEnabled() throws RemoteException {
        }

        public void noteDeviceIdleMode(int mode, String activeReason, int activeUid) throws RemoteException {
        }

        public void setBatteryState(int status, int health, int plugType, int level, int temp, int volt, int chargeUAh, int chargeFullUAh) throws RemoteException {
        }

        public long getAwakeTimeBattery() throws RemoteException {
            return 0;
        }

        public long getAwakeTimePlugged() throws RemoteException {
            return 0;
        }

        public void noteBleScanStarted(WorkSource ws, boolean isUnoptimized) throws RemoteException {
        }

        public void noteBleScanStopped(WorkSource ws, boolean isUnoptimized) throws RemoteException {
        }

        public void noteResetBleScan() throws RemoteException {
        }

        public void noteBleScanResults(WorkSource ws, int numNewResults) throws RemoteException {
        }

        public CellularBatteryStats getCellularBatteryStats() throws RemoteException {
            return null;
        }

        public WifiBatteryStats getWifiBatteryStats() throws RemoteException {
            return null;
        }

        public GpsBatteryStats getGpsBatteryStats() throws RemoteException {
            return null;
        }

        public HealthStatsParceler takeUidSnapshot(int uid) throws RemoteException {
            return null;
        }

        public HealthStatsParceler[] takeUidSnapshots(int[] uid) throws RemoteException {
            return null;
        }

        public void noteBluetoothControllerActivity(BluetoothActivityEnergyInfo info) throws RemoteException {
        }

        public void noteModemControllerActivity(ModemActivityInfo info) throws RemoteException {
        }

        public void noteWifiControllerActivity(WifiActivityEnergyInfo info) throws RemoteException {
        }

        public boolean setChargingStateUpdateDelayMillis(int delay) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBatteryStats {
        private static final String DESCRIPTOR = "com.android.internal.app.IBatteryStats";
        static final int TRANSACTION_computeBatteryTimeRemaining = 18;
        static final int TRANSACTION_computeChargeTimeRemaining = 19;
        static final int TRANSACTION_getAwakeTimeBattery = 75;
        static final int TRANSACTION_getAwakeTimePlugged = 76;
        static final int TRANSACTION_getCellularBatteryStats = 81;
        static final int TRANSACTION_getGpsBatteryStats = 83;
        static final int TRANSACTION_getStatistics = 15;
        static final int TRANSACTION_getStatisticsStream = 16;
        static final int TRANSACTION_getWifiBatteryStats = 82;
        static final int TRANSACTION_isCharging = 17;
        static final int TRANSACTION_noteBleScanResults = 80;
        static final int TRANSACTION_noteBleScanStarted = 77;
        static final int TRANSACTION_noteBleScanStopped = 78;
        static final int TRANSACTION_noteBluetoothControllerActivity = 86;
        static final int TRANSACTION_noteChangeWakelockFromSource = 28;
        static final int TRANSACTION_noteConnectivityChanged = 43;
        static final int TRANSACTION_noteDeviceIdleMode = 73;
        static final int TRANSACTION_noteEvent = 20;
        static final int TRANSACTION_noteFlashlightOff = 10;
        static final int TRANSACTION_noteFlashlightOn = 9;
        static final int TRANSACTION_noteFullWifiLockAcquired = 58;
        static final int TRANSACTION_noteFullWifiLockAcquiredFromSource = 64;
        static final int TRANSACTION_noteFullWifiLockReleased = 59;
        static final int TRANSACTION_noteFullWifiLockReleasedFromSource = 65;
        static final int TRANSACTION_noteGpsChanged = 36;
        static final int TRANSACTION_noteGpsSignalQuality = 37;
        static final int TRANSACTION_noteInteractive = 42;
        static final int TRANSACTION_noteJobFinish = 24;
        static final int TRANSACTION_noteJobStart = 23;
        static final int TRANSACTION_noteLongPartialWakelockFinish = 32;
        static final int TRANSACTION_noteLongPartialWakelockFinishFromSource = 33;
        static final int TRANSACTION_noteLongPartialWakelockStart = 30;
        static final int TRANSACTION_noteLongPartialWakelockStartFromSource = 31;
        static final int TRANSACTION_noteMobileRadioPowerState = 44;
        static final int TRANSACTION_noteModemControllerActivity = 87;
        static final int TRANSACTION_noteNetworkInterfaceType = 71;
        static final int TRANSACTION_noteNetworkStatsEnabled = 72;
        static final int TRANSACTION_notePhoneDataConnectionState = 48;
        static final int TRANSACTION_notePhoneOff = 46;
        static final int TRANSACTION_notePhoneOn = 45;
        static final int TRANSACTION_notePhoneSignalStrength = 47;
        static final int TRANSACTION_notePhoneState = 49;
        static final int TRANSACTION_noteResetAudio = 8;
        static final int TRANSACTION_noteResetBleScan = 79;
        static final int TRANSACTION_noteResetCamera = 13;
        static final int TRANSACTION_noteResetFlashlight = 14;
        static final int TRANSACTION_noteResetVideo = 7;
        static final int TRANSACTION_noteScreenBrightness = 39;
        static final int TRANSACTION_noteScreenState = 38;
        static final int TRANSACTION_noteStartAudio = 5;
        static final int TRANSACTION_noteStartCamera = 11;
        static final int TRANSACTION_noteStartSensor = 1;
        static final int TRANSACTION_noteStartVideo = 3;
        static final int TRANSACTION_noteStartWakelock = 25;
        static final int TRANSACTION_noteStartWakelockFromSource = 27;
        static final int TRANSACTION_noteStopAudio = 6;
        static final int TRANSACTION_noteStopCamera = 12;
        static final int TRANSACTION_noteStopSensor = 2;
        static final int TRANSACTION_noteStopVideo = 4;
        static final int TRANSACTION_noteStopWakelock = 26;
        static final int TRANSACTION_noteStopWakelockFromSource = 29;
        static final int TRANSACTION_noteSyncFinish = 22;
        static final int TRANSACTION_noteSyncStart = 21;
        static final int TRANSACTION_noteUserActivity = 40;
        static final int TRANSACTION_noteVibratorOff = 35;
        static final int TRANSACTION_noteVibratorOn = 34;
        static final int TRANSACTION_noteWakeUp = 41;
        static final int TRANSACTION_noteWifiBatchedScanStartedFromSource = 68;
        static final int TRANSACTION_noteWifiBatchedScanStoppedFromSource = 69;
        static final int TRANSACTION_noteWifiControllerActivity = 88;
        static final int TRANSACTION_noteWifiMulticastDisabled = 63;
        static final int TRANSACTION_noteWifiMulticastEnabled = 62;
        static final int TRANSACTION_noteWifiOff = 51;
        static final int TRANSACTION_noteWifiOn = 50;
        static final int TRANSACTION_noteWifiRadioPowerState = 70;
        static final int TRANSACTION_noteWifiRssiChanged = 57;
        static final int TRANSACTION_noteWifiRunning = 52;
        static final int TRANSACTION_noteWifiRunningChanged = 53;
        static final int TRANSACTION_noteWifiScanStarted = 60;
        static final int TRANSACTION_noteWifiScanStartedFromSource = 66;
        static final int TRANSACTION_noteWifiScanStopped = 61;
        static final int TRANSACTION_noteWifiScanStoppedFromSource = 67;
        static final int TRANSACTION_noteWifiState = 55;
        static final int TRANSACTION_noteWifiStopped = 54;
        static final int TRANSACTION_noteWifiSupplicantStateChanged = 56;
        static final int TRANSACTION_setBatteryState = 74;
        static final int TRANSACTION_setChargingStateUpdateDelayMillis = 89;
        static final int TRANSACTION_takeUidSnapshot = 84;
        static final int TRANSACTION_takeUidSnapshots = 85;

        private static class Proxy implements IBatteryStats {
            public static IBatteryStats sDefaultImpl;
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

            public void noteStartSensor(int uid, int sensor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(sensor);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteStartSensor(uid, sensor);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteStopSensor(int uid, int sensor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(sensor);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteStopSensor(uid, sensor);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteStartVideo(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteStartVideo(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteStopVideo(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteStopVideo(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteStartAudio(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteStartAudio(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteStopAudio(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteStopAudio(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteResetVideo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteResetVideo();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteResetAudio() throws RemoteException {
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
                    Stub.getDefaultImpl().noteResetAudio();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteFlashlightOn(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteFlashlightOn(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteFlashlightOff(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteFlashlightOff(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteStartCamera(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteStartCamera(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteStopCamera(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteStopCamera(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteResetCamera() throws RemoteException {
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
                    Stub.getDefaultImpl().noteResetCamera();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteResetFlashlight() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteResetFlashlight();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getStatistics() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    byte[] bArr = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getStatistics();
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor getStatisticsStream() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParcelFileDescriptor parcelFileDescriptor = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().getStatisticsStream();
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

            public boolean isCharging() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isCharging();
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

            public long computeBatteryTimeRemaining() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().computeBatteryTimeRemaining();
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

            public long computeChargeTimeRemaining() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().computeChargeTimeRemaining();
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

            public void noteEvent(int code, String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteEvent(code, name, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteSyncStart(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteSyncStart(name, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteSyncFinish(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteSyncFinish(name, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteJobStart(String name, int uid, int standbyBucket, int jobid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    _data.writeInt(standbyBucket);
                    _data.writeInt(jobid);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteJobStart(name, uid, standbyBucket, jobid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteJobFinish(String name, int uid, int stopReason, int standbyBucket, int jobid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    _data.writeInt(stopReason);
                    _data.writeInt(standbyBucket);
                    _data.writeInt(jobid);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteJobFinish(name, uid, stopReason, standbyBucket, jobid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteStartWakelock(int uid, int pid, String name, String historyName, int type, boolean unimportantForLogging) throws RemoteException {
                Throwable th;
                int i;
                String str;
                String str2;
                int i2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(uid);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        str = name;
                        str2 = historyName;
                        i2 = type;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                        try {
                            _data.writeString(name);
                            try {
                                _data.writeString(historyName);
                            } catch (Throwable th3) {
                                th = th3;
                                i2 = type;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            str2 = historyName;
                            i2 = type;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        str = name;
                        str2 = historyName;
                        i2 = type;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(type);
                        _data.writeInt(unimportantForLogging ? 1 : 0);
                    } catch (Throwable th6) {
                        th = th6;
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
                        Stub.getDefaultImpl().noteStartWakelock(uid, pid, name, historyName, type, unimportantForLogging);
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
                    int i3 = uid;
                    i = pid;
                    str = name;
                    str2 = historyName;
                    i2 = type;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void noteStopWakelock(int uid, int pid, String name, String historyName, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    _data.writeInt(type);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteStopWakelock(uid, pid, name, historyName, type);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteStartWakelockFromSource(WorkSource ws, int pid, String name, String historyName, int type, boolean unimportantForLogging) throws RemoteException {
                Throwable th;
                String str;
                String str2;
                int i;
                WorkSource workSource = ws;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i2 = 1;
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(pid);
                    } catch (Throwable th2) {
                        th = th2;
                        str = name;
                        str2 = historyName;
                        i = type;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(name);
                    } catch (Throwable th3) {
                        th = th3;
                        str2 = historyName;
                        i = type;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(historyName);
                        try {
                            _data.writeInt(type);
                            if (!unimportantForLogging) {
                                i2 = 0;
                            }
                            _data.writeInt(i2);
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().noteStartWakelockFromSource(ws, pid, name, historyName, type, unimportantForLogging);
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
                        i = type;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i3 = pid;
                    str = name;
                    str2 = historyName;
                    i = type;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void noteChangeWakelockFromSource(WorkSource ws, int pid, String name, String histyoryName, int type, WorkSource newWs, int newPid, String newName, String newHistoryName, int newType, boolean newUnimportantForLogging) throws RemoteException {
                Throwable th;
                WorkSource workSource = ws;
                WorkSource workSource2 = newWs;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                Parcel _reply2;
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (workSource != null) {
                        try {
                            _data.writeInt(1);
                            workSource.writeToParcel(_data, 0);
                        } catch (Throwable th2) {
                            th = th2;
                            _reply2 = _reply;
                        }
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(pid);
                    _data.writeString(name);
                    _data.writeString(histyoryName);
                    _data.writeInt(type);
                    if (workSource2 != null) {
                        _data.writeInt(1);
                        workSource2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(newPid);
                    _data.writeString(newName);
                    _data.writeString(newHistoryName);
                    _data.writeInt(newType);
                    if (!newUnimportantForLogging) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply2 = _reply;
                        _reply2.readException();
                        _reply2.recycle();
                        _data.recycle();
                        return;
                    }
                    _reply2 = _reply;
                    try {
                        Stub.getDefaultImpl().noteChangeWakelockFromSource(ws, pid, name, histyoryName, type, newWs, newPid, newName, newHistoryName, newType, newUnimportantForLogging);
                        _reply2.recycle();
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply2.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply2 = _reply;
                    _reply2.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void noteStopWakelockFromSource(WorkSource ws, int pid, String name, String historyName, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(pid);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    _data.writeInt(type);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteStopWakelockFromSource(ws, pid, name, historyName, type);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteLongPartialWakelockStart(String name, String historyName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteLongPartialWakelockStart(name, historyName, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteLongPartialWakelockStartFromSource(String name, String historyName, WorkSource workSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteLongPartialWakelockStartFromSource(name, historyName, workSource);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteLongPartialWakelockFinish(String name, String historyName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteLongPartialWakelockFinish(name, historyName, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteLongPartialWakelockFinishFromSource(String name, String historyName, WorkSource workSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteLongPartialWakelockFinishFromSource(name, historyName, workSource);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteVibratorOn(int uid, long durationMillis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeLong(durationMillis);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteVibratorOn(uid, durationMillis);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteVibratorOff(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteVibratorOff(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteGpsChanged(WorkSource oldSource, WorkSource newSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (oldSource != null) {
                        _data.writeInt(1);
                        oldSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (newSource != null) {
                        _data.writeInt(1);
                        newSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteGpsChanged(oldSource, newSource);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteGpsSignalQuality(int signalLevel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(signalLevel);
                    if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteGpsSignalQuality(signalLevel);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteScreenState(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteScreenState(state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteScreenBrightness(int brightness) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(brightness);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteScreenBrightness(brightness);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteUserActivity(int uid, int event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(event);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteUserActivity(uid, event);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWakeUp(String reason, int reasonUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    _data.writeInt(reasonUid);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWakeUp(reason, reasonUid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteInteractive(boolean interactive) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(interactive ? 1 : 0);
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteInteractive(interactive);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteConnectivityChanged(int type, String extra) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(extra);
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteConnectivityChanged(type, extra);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteMobileRadioPowerState(int powerState, long timestampNs, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(powerState);
                    _data.writeLong(timestampNs);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteMobileRadioPowerState(powerState, timestampNs, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notePhoneOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notePhoneOn();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notePhoneOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notePhoneOff();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notePhoneSignalStrength(SignalStrength signalStrength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (signalStrength != null) {
                        _data.writeInt(1);
                        signalStrength.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notePhoneSignalStrength(signalStrength);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notePhoneDataConnectionState(int dataType, boolean hasData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(dataType);
                    _data.writeInt(hasData ? 1 : 0);
                    if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notePhoneDataConnectionState(dataType, hasData);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notePhoneState(int phoneState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneState);
                    if (this.mRemote.transact(49, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notePhoneState(phoneState);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(50, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiOn();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiOff();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiRunning(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(52, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiRunning(ws);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiRunningChanged(WorkSource oldWs, WorkSource newWs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (oldWs != null) {
                        _data.writeInt(1);
                        oldWs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (newWs != null) {
                        _data.writeInt(1);
                        newWs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiRunningChanged(oldWs, newWs);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiStopped(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiStopped(ws);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiState(int wifiState, String accessPoint) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(wifiState);
                    _data.writeString(accessPoint);
                    if (this.mRemote.transact(55, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiState(wifiState, accessPoint);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiSupplicantStateChanged(int supplState, boolean failedAuth) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(supplState);
                    _data.writeInt(failedAuth ? 1 : 0);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiSupplicantStateChanged(supplState, failedAuth);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiRssiChanged(int newRssi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newRssi);
                    if (this.mRemote.transact(57, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiRssiChanged(newRssi);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteFullWifiLockAcquired(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(58, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteFullWifiLockAcquired(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteFullWifiLockReleased(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteFullWifiLockReleased(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiScanStarted(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiScanStarted(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiScanStopped(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(61, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiScanStopped(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiMulticastEnabled(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(62, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiMulticastEnabled(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiMulticastDisabled(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(63, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiMulticastDisabled(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteFullWifiLockAcquiredFromSource(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(64, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteFullWifiLockAcquiredFromSource(ws);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteFullWifiLockReleasedFromSource(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(65, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteFullWifiLockReleasedFromSource(ws);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiScanStartedFromSource(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(66, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiScanStartedFromSource(ws);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiScanStoppedFromSource(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(67, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiScanStoppedFromSource(ws);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiBatchedScanStartedFromSource(WorkSource ws, int csph) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(csph);
                    if (this.mRemote.transact(68, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiBatchedScanStartedFromSource(ws, csph);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiBatchedScanStoppedFromSource(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(69, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiBatchedScanStoppedFromSource(ws);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWifiRadioPowerState(int powerState, long timestampNs, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(powerState);
                    _data.writeLong(timestampNs);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(70, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWifiRadioPowerState(powerState, timestampNs, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteNetworkInterfaceType(String iface, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(type);
                    if (this.mRemote.transact(71, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteNetworkInterfaceType(iface, type);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteNetworkStatsEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(72, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteNetworkStatsEnabled();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteDeviceIdleMode(int mode, String activeReason, int activeUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeString(activeReason);
                    _data.writeInt(activeUid);
                    if (this.mRemote.transact(73, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteDeviceIdleMode(mode, activeReason, activeUid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBatteryState(int status, int health, int plugType, int level, int temp, int volt, int chargeUAh, int chargeFullUAh) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(status);
                    } catch (Throwable th2) {
                        th = th2;
                        i = health;
                        i2 = plugType;
                        i3 = level;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(health);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = plugType;
                        i3 = level;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(plugType);
                        try {
                            _data.writeInt(level);
                            _data.writeInt(temp);
                            _data.writeInt(volt);
                            _data.writeInt(chargeUAh);
                            _data.writeInt(chargeFullUAh);
                            if (this.mRemote.transact(74, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().setBatteryState(status, health, plugType, level, temp, volt, chargeUAh, chargeFullUAh);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i3 = level;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i4 = status;
                    i = health;
                    i2 = plugType;
                    i3 = level;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public long getAwakeTimeBattery() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 75;
                    if (!this.mRemote.transact(75, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getAwakeTimeBattery();
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

            public long getAwakeTimePlugged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 76;
                    if (!this.mRemote.transact(76, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getAwakeTimePlugged();
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

            public void noteBleScanStarted(WorkSource ws, boolean isUnoptimized) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!isUnoptimized) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(77, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteBleScanStarted(ws, isUnoptimized);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteBleScanStopped(WorkSource ws, boolean isUnoptimized) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!isUnoptimized) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(78, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteBleScanStopped(ws, isUnoptimized);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteResetBleScan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(79, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteResetBleScan();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteBleScanResults(WorkSource ws, int numNewResults) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(numNewResults);
                    if (this.mRemote.transact(80, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteBleScanResults(ws, numNewResults);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CellularBatteryStats getCellularBatteryStats() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    CellularBatteryStats cellularBatteryStats = 81;
                    if (!this.mRemote.transact(81, _data, _reply, 0)) {
                        cellularBatteryStats = Stub.getDefaultImpl();
                        if (cellularBatteryStats != 0) {
                            cellularBatteryStats = Stub.getDefaultImpl().getCellularBatteryStats();
                            return cellularBatteryStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        cellularBatteryStats = (CellularBatteryStats) CellularBatteryStats.CREATOR.createFromParcel(_reply);
                    } else {
                        cellularBatteryStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return cellularBatteryStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WifiBatteryStats getWifiBatteryStats() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    WifiBatteryStats wifiBatteryStats = 82;
                    if (!this.mRemote.transact(82, _data, _reply, 0)) {
                        wifiBatteryStats = Stub.getDefaultImpl();
                        if (wifiBatteryStats != 0) {
                            wifiBatteryStats = Stub.getDefaultImpl().getWifiBatteryStats();
                            return wifiBatteryStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        wifiBatteryStats = (WifiBatteryStats) WifiBatteryStats.CREATOR.createFromParcel(_reply);
                    } else {
                        wifiBatteryStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return wifiBatteryStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public GpsBatteryStats getGpsBatteryStats() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    GpsBatteryStats gpsBatteryStats = 83;
                    if (!this.mRemote.transact(83, _data, _reply, 0)) {
                        gpsBatteryStats = Stub.getDefaultImpl();
                        if (gpsBatteryStats != 0) {
                            gpsBatteryStats = Stub.getDefaultImpl().getGpsBatteryStats();
                            return gpsBatteryStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        gpsBatteryStats = (GpsBatteryStats) GpsBatteryStats.CREATOR.createFromParcel(_reply);
                    } else {
                        gpsBatteryStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return gpsBatteryStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public HealthStatsParceler takeUidSnapshot(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    HealthStatsParceler healthStatsParceler = 84;
                    if (!this.mRemote.transact(84, _data, _reply, 0)) {
                        healthStatsParceler = Stub.getDefaultImpl();
                        if (healthStatsParceler != 0) {
                            healthStatsParceler = Stub.getDefaultImpl().takeUidSnapshot(uid);
                            return healthStatsParceler;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        healthStatsParceler = (HealthStatsParceler) HealthStatsParceler.CREATOR.createFromParcel(_reply);
                    } else {
                        healthStatsParceler = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return healthStatsParceler;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public HealthStatsParceler[] takeUidSnapshots(int[] uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(uid);
                    HealthStatsParceler[] healthStatsParcelerArr = 85;
                    if (!this.mRemote.transact(85, _data, _reply, 0)) {
                        healthStatsParcelerArr = Stub.getDefaultImpl();
                        if (healthStatsParcelerArr != 0) {
                            healthStatsParcelerArr = Stub.getDefaultImpl().takeUidSnapshots(uid);
                            return healthStatsParcelerArr;
                        }
                    }
                    _reply.readException();
                    HealthStatsParceler[] _result = (HealthStatsParceler[]) _reply.createTypedArray(HealthStatsParceler.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteBluetoothControllerActivity(BluetoothActivityEnergyInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(86, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().noteBluetoothControllerActivity(info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void noteModemControllerActivity(ModemActivityInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(87, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().noteModemControllerActivity(info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void noteWifiControllerActivity(WifiActivityEnergyInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(88, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().noteWifiControllerActivity(info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public boolean setChargingStateUpdateDelayMillis(int delay) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(delay);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(89, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setChargingStateUpdateDelayMillis(delay);
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

        public static IBatteryStats asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBatteryStats)) {
                return new Proxy(obj);
            }
            return (IBatteryStats) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "noteStartSensor";
                case 2:
                    return "noteStopSensor";
                case 3:
                    return "noteStartVideo";
                case 4:
                    return "noteStopVideo";
                case 5:
                    return "noteStartAudio";
                case 6:
                    return "noteStopAudio";
                case 7:
                    return "noteResetVideo";
                case 8:
                    return "noteResetAudio";
                case 9:
                    return "noteFlashlightOn";
                case 10:
                    return "noteFlashlightOff";
                case 11:
                    return "noteStartCamera";
                case 12:
                    return "noteStopCamera";
                case 13:
                    return "noteResetCamera";
                case 14:
                    return "noteResetFlashlight";
                case 15:
                    return "getStatistics";
                case 16:
                    return "getStatisticsStream";
                case 17:
                    return "isCharging";
                case 18:
                    return "computeBatteryTimeRemaining";
                case 19:
                    return "computeChargeTimeRemaining";
                case 20:
                    return "noteEvent";
                case 21:
                    return "noteSyncStart";
                case 22:
                    return "noteSyncFinish";
                case 23:
                    return "noteJobStart";
                case 24:
                    return "noteJobFinish";
                case 25:
                    return "noteStartWakelock";
                case 26:
                    return "noteStopWakelock";
                case 27:
                    return "noteStartWakelockFromSource";
                case 28:
                    return "noteChangeWakelockFromSource";
                case 29:
                    return "noteStopWakelockFromSource";
                case 30:
                    return "noteLongPartialWakelockStart";
                case 31:
                    return "noteLongPartialWakelockStartFromSource";
                case 32:
                    return "noteLongPartialWakelockFinish";
                case 33:
                    return "noteLongPartialWakelockFinishFromSource";
                case 34:
                    return "noteVibratorOn";
                case 35:
                    return "noteVibratorOff";
                case 36:
                    return "noteGpsChanged";
                case 37:
                    return "noteGpsSignalQuality";
                case 38:
                    return "noteScreenState";
                case 39:
                    return "noteScreenBrightness";
                case 40:
                    return "noteUserActivity";
                case 41:
                    return "noteWakeUp";
                case 42:
                    return "noteInteractive";
                case 43:
                    return "noteConnectivityChanged";
                case 44:
                    return "noteMobileRadioPowerState";
                case 45:
                    return "notePhoneOn";
                case 46:
                    return "notePhoneOff";
                case 47:
                    return "notePhoneSignalStrength";
                case 48:
                    return "notePhoneDataConnectionState";
                case 49:
                    return "notePhoneState";
                case 50:
                    return "noteWifiOn";
                case 51:
                    return "noteWifiOff";
                case 52:
                    return "noteWifiRunning";
                case 53:
                    return "noteWifiRunningChanged";
                case 54:
                    return "noteWifiStopped";
                case 55:
                    return "noteWifiState";
                case 56:
                    return "noteWifiSupplicantStateChanged";
                case 57:
                    return "noteWifiRssiChanged";
                case 58:
                    return "noteFullWifiLockAcquired";
                case 59:
                    return "noteFullWifiLockReleased";
                case 60:
                    return "noteWifiScanStarted";
                case 61:
                    return "noteWifiScanStopped";
                case 62:
                    return "noteWifiMulticastEnabled";
                case 63:
                    return "noteWifiMulticastDisabled";
                case 64:
                    return "noteFullWifiLockAcquiredFromSource";
                case 65:
                    return "noteFullWifiLockReleasedFromSource";
                case 66:
                    return "noteWifiScanStartedFromSource";
                case 67:
                    return "noteWifiScanStoppedFromSource";
                case 68:
                    return "noteWifiBatchedScanStartedFromSource";
                case 69:
                    return "noteWifiBatchedScanStoppedFromSource";
                case 70:
                    return "noteWifiRadioPowerState";
                case 71:
                    return "noteNetworkInterfaceType";
                case 72:
                    return "noteNetworkStatsEnabled";
                case 73:
                    return "noteDeviceIdleMode";
                case 74:
                    return "setBatteryState";
                case 75:
                    return "getAwakeTimeBattery";
                case 76:
                    return "getAwakeTimePlugged";
                case 77:
                    return "noteBleScanStarted";
                case 78:
                    return "noteBleScanStopped";
                case 79:
                    return "noteResetBleScan";
                case 80:
                    return "noteBleScanResults";
                case 81:
                    return "getCellularBatteryStats";
                case 82:
                    return "getWifiBatteryStats";
                case 83:
                    return "getGpsBatteryStats";
                case 84:
                    return "takeUidSnapshot";
                case 85:
                    return "takeUidSnapshots";
                case 86:
                    return "noteBluetoothControllerActivity";
                case 87:
                    return "noteModemControllerActivity";
                case 88:
                    return "noteWifiControllerActivity";
                case 89:
                    return "setChargingStateUpdateDelayMillis";
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
                boolean _arg1 = false;
                long _result;
                String _arg0;
                String _arg12;
                WorkSource _arg2;
                WorkSource _arg02;
                WorkSource _arg13;
                int _arg03;
                switch (i) {
                    case 1:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteStartSensor(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 2:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteStopSensor(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 3:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteStartVideo(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 4:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteStopVideo(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 5:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteStartAudio(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 6:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteStopAudio(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 7:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteResetVideo();
                        reply.writeNoException();
                        return z;
                    case 8:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteResetAudio();
                        reply.writeNoException();
                        return z;
                    case 9:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteFlashlightOn(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 10:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteFlashlightOff(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 11:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteStartCamera(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 12:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteStopCamera(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 13:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteResetCamera();
                        reply.writeNoException();
                        return z;
                    case 14:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteResetFlashlight();
                        reply.writeNoException();
                        return z;
                    case 15:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        byte[] _result2 = getStatistics();
                        reply.writeNoException();
                        parcel2.writeByteArray(_result2);
                        return z;
                    case 16:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        ParcelFileDescriptor _result3 = getStatisticsStream();
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(i);
                            _result3.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 17:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isCharging();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 18:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = computeBatteryTimeRemaining();
                        reply.writeNoException();
                        parcel2.writeLong(_result);
                        return z;
                    case 19:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = computeChargeTimeRemaining();
                        reply.writeNoException();
                        parcel2.writeLong(_result);
                        return z;
                    case 20:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteEvent(data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 21:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteSyncStart(data.readString(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 22:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteSyncFinish(data.readString(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 23:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteJobStart(data.readString(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 24:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteJobFinish(data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 25:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteStartWakelock(data.readInt(), data.readInt(), data.readString(), data.readString(), data.readInt(), data.readInt() != 0 ? z : false);
                        reply.writeNoException();
                        return z;
                    case 26:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        noteStopWakelock(data.readInt(), data.readInt(), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 27:
                        WorkSource _arg04;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        noteStartWakelockFromSource(_arg04, data.readInt(), data.readString(), data.readString(), data.readInt(), data.readInt() != 0 ? z : false);
                        reply.writeNoException();
                        return z;
                    case 28:
                        WorkSource _arg05;
                        WorkSource _arg5;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        int _arg14 = data.readInt();
                        String _arg22 = data.readString();
                        String _arg3 = data.readString();
                        int _arg4 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg5 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        z = true;
                        noteChangeWakelockFromSource(_arg05, _arg14, _arg22, _arg3, _arg4, _arg5, data.readInt(), data.readString(), data.readString(), data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        return z;
                    case 29:
                        WorkSource _arg06;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        noteStopWakelockFromSource(_arg06, data.readInt(), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        noteLongPartialWakelockStart(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        noteLongPartialWakelockStartFromSource(_arg0, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        noteLongPartialWakelockFinish(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        noteLongPartialWakelockFinishFromSource(_arg0, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        noteVibratorOn(data.readInt(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        noteVibratorOff(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        noteGpsChanged(_arg02, _arg13);
                        reply.writeNoException();
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        noteGpsSignalQuality(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        noteScreenState(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        noteScreenBrightness(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        noteUserActivity(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        noteWakeUp(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        noteInteractive(_arg1);
                        reply.writeNoException();
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        noteConnectivityChanged(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        noteMobileRadioPowerState(data.readInt(), data.readLong(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        notePhoneOn();
                        reply.writeNoException();
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        notePhoneOff();
                        reply.writeNoException();
                        return true;
                    case 47:
                        SignalStrength _arg07;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (SignalStrength) SignalStrength.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        notePhoneSignalStrength(_arg07);
                        reply.writeNoException();
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        notePhoneDataConnectionState(_arg03, _arg1);
                        reply.writeNoException();
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        notePhoneState(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        noteWifiOn();
                        reply.writeNoException();
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        noteWifiOff();
                        reply.writeNoException();
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        noteWifiRunning(_arg02);
                        reply.writeNoException();
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        noteWifiRunningChanged(_arg02, _arg13);
                        reply.writeNoException();
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        noteWifiStopped(_arg02);
                        reply.writeNoException();
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        noteWifiState(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        noteWifiSupplicantStateChanged(_arg03, _arg1);
                        reply.writeNoException();
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        noteWifiRssiChanged(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 58:
                        parcel.enforceInterface(descriptor);
                        noteFullWifiLockAcquired(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        noteFullWifiLockReleased(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        noteWifiScanStarted(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        noteWifiScanStopped(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 62:
                        parcel.enforceInterface(descriptor);
                        noteWifiMulticastEnabled(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        noteWifiMulticastDisabled(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        noteFullWifiLockAcquiredFromSource(_arg02);
                        reply.writeNoException();
                        return true;
                    case 65:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        noteFullWifiLockReleasedFromSource(_arg02);
                        reply.writeNoException();
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        noteWifiScanStartedFromSource(_arg02);
                        reply.writeNoException();
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        noteWifiScanStoppedFromSource(_arg02);
                        reply.writeNoException();
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        noteWifiBatchedScanStartedFromSource(_arg02, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 69:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        noteWifiBatchedScanStoppedFromSource(_arg02);
                        reply.writeNoException();
                        return true;
                    case 70:
                        parcel.enforceInterface(descriptor);
                        noteWifiRadioPowerState(data.readInt(), data.readLong(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 71:
                        parcel.enforceInterface(descriptor);
                        noteNetworkInterfaceType(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 72:
                        parcel.enforceInterface(descriptor);
                        noteNetworkStatsEnabled();
                        reply.writeNoException();
                        return true;
                    case 73:
                        parcel.enforceInterface(descriptor);
                        noteDeviceIdleMode(data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 74:
                        parcel.enforceInterface(descriptor);
                        setBatteryState(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 75:
                        parcel.enforceInterface(descriptor);
                        _result = getAwakeTimeBattery();
                        reply.writeNoException();
                        parcel2.writeLong(_result);
                        return true;
                    case 76:
                        parcel.enforceInterface(descriptor);
                        _result = getAwakeTimePlugged();
                        reply.writeNoException();
                        parcel2.writeLong(_result);
                        return true;
                    case 77:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg13 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        noteBleScanStarted(_arg13, _arg1);
                        reply.writeNoException();
                        return true;
                    case 78:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg13 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        noteBleScanStopped(_arg13, _arg1);
                        reply.writeNoException();
                        return true;
                    case 79:
                        parcel.enforceInterface(descriptor);
                        noteResetBleScan();
                        reply.writeNoException();
                        return true;
                    case 80:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        noteBleScanResults(_arg02, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 81:
                        parcel.enforceInterface(descriptor);
                        CellularBatteryStats _result4 = getCellularBatteryStats();
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 82:
                        parcel.enforceInterface(descriptor);
                        WifiBatteryStats _result5 = getWifiBatteryStats();
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 83:
                        parcel.enforceInterface(descriptor);
                        GpsBatteryStats _result6 = getGpsBatteryStats();
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 84:
                        parcel.enforceInterface(descriptor);
                        HealthStatsParceler _result7 = takeUidSnapshot(data.readInt());
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 85:
                        parcel.enforceInterface(descriptor);
                        HealthStatsParceler[] _result8 = takeUidSnapshots(data.createIntArray());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result8, 1);
                        return true;
                    case 86:
                        BluetoothActivityEnergyInfo _arg08;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (BluetoothActivityEnergyInfo) BluetoothActivityEnergyInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg08 = null;
                        }
                        noteBluetoothControllerActivity(_arg08);
                        return true;
                    case 87:
                        ModemActivityInfo _arg09;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg09 = (ModemActivityInfo) ModemActivityInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg09 = null;
                        }
                        noteModemControllerActivity(_arg09);
                        return true;
                    case 88:
                        WifiActivityEnergyInfo _arg010;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg010 = (WifiActivityEnergyInfo) WifiActivityEnergyInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg010 = null;
                        }
                        noteWifiControllerActivity(_arg010);
                        return true;
                    case 89:
                        parcel.enforceInterface(descriptor);
                        boolean _result9 = setChargingStateUpdateDelayMillis(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result9);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            z = true;
            parcel2.writeString(descriptor);
            return z;
        }

        public static boolean setDefaultImpl(IBatteryStats impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBatteryStats getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    long computeBatteryTimeRemaining() throws RemoteException;

    @UnsupportedAppUsage
    long computeChargeTimeRemaining() throws RemoteException;

    @UnsupportedAppUsage
    long getAwakeTimeBattery() throws RemoteException;

    long getAwakeTimePlugged() throws RemoteException;

    CellularBatteryStats getCellularBatteryStats() throws RemoteException;

    GpsBatteryStats getGpsBatteryStats() throws RemoteException;

    @UnsupportedAppUsage
    byte[] getStatistics() throws RemoteException;

    ParcelFileDescriptor getStatisticsStream() throws RemoteException;

    WifiBatteryStats getWifiBatteryStats() throws RemoteException;

    @UnsupportedAppUsage
    boolean isCharging() throws RemoteException;

    void noteBleScanResults(WorkSource workSource, int i) throws RemoteException;

    void noteBleScanStarted(WorkSource workSource, boolean z) throws RemoteException;

    void noteBleScanStopped(WorkSource workSource, boolean z) throws RemoteException;

    void noteBluetoothControllerActivity(BluetoothActivityEnergyInfo bluetoothActivityEnergyInfo) throws RemoteException;

    void noteChangeWakelockFromSource(WorkSource workSource, int i, String str, String str2, int i2, WorkSource workSource2, int i3, String str3, String str4, int i4, boolean z) throws RemoteException;

    void noteConnectivityChanged(int i, String str) throws RemoteException;

    void noteDeviceIdleMode(int i, String str, int i2) throws RemoteException;

    void noteEvent(int i, String str, int i2) throws RemoteException;

    void noteFlashlightOff(int i) throws RemoteException;

    void noteFlashlightOn(int i) throws RemoteException;

    void noteFullWifiLockAcquired(int i) throws RemoteException;

    void noteFullWifiLockAcquiredFromSource(WorkSource workSource) throws RemoteException;

    void noteFullWifiLockReleased(int i) throws RemoteException;

    void noteFullWifiLockReleasedFromSource(WorkSource workSource) throws RemoteException;

    void noteGpsChanged(WorkSource workSource, WorkSource workSource2) throws RemoteException;

    void noteGpsSignalQuality(int i) throws RemoteException;

    void noteInteractive(boolean z) throws RemoteException;

    void noteJobFinish(String str, int i, int i2, int i3, int i4) throws RemoteException;

    void noteJobStart(String str, int i, int i2, int i3) throws RemoteException;

    void noteLongPartialWakelockFinish(String str, String str2, int i) throws RemoteException;

    void noteLongPartialWakelockFinishFromSource(String str, String str2, WorkSource workSource) throws RemoteException;

    void noteLongPartialWakelockStart(String str, String str2, int i) throws RemoteException;

    void noteLongPartialWakelockStartFromSource(String str, String str2, WorkSource workSource) throws RemoteException;

    void noteMobileRadioPowerState(int i, long j, int i2) throws RemoteException;

    void noteModemControllerActivity(ModemActivityInfo modemActivityInfo) throws RemoteException;

    void noteNetworkInterfaceType(String str, int i) throws RemoteException;

    void noteNetworkStatsEnabled() throws RemoteException;

    void notePhoneDataConnectionState(int i, boolean z) throws RemoteException;

    void notePhoneOff() throws RemoteException;

    void notePhoneOn() throws RemoteException;

    void notePhoneSignalStrength(SignalStrength signalStrength) throws RemoteException;

    void notePhoneState(int i) throws RemoteException;

    void noteResetAudio() throws RemoteException;

    void noteResetBleScan() throws RemoteException;

    void noteResetCamera() throws RemoteException;

    void noteResetFlashlight() throws RemoteException;

    void noteResetVideo() throws RemoteException;

    void noteScreenBrightness(int i) throws RemoteException;

    void noteScreenState(int i) throws RemoteException;

    void noteStartAudio(int i) throws RemoteException;

    void noteStartCamera(int i) throws RemoteException;

    void noteStartSensor(int i, int i2) throws RemoteException;

    void noteStartVideo(int i) throws RemoteException;

    void noteStartWakelock(int i, int i2, String str, String str2, int i3, boolean z) throws RemoteException;

    void noteStartWakelockFromSource(WorkSource workSource, int i, String str, String str2, int i2, boolean z) throws RemoteException;

    void noteStopAudio(int i) throws RemoteException;

    void noteStopCamera(int i) throws RemoteException;

    void noteStopSensor(int i, int i2) throws RemoteException;

    void noteStopVideo(int i) throws RemoteException;

    void noteStopWakelock(int i, int i2, String str, String str2, int i3) throws RemoteException;

    void noteStopWakelockFromSource(WorkSource workSource, int i, String str, String str2, int i2) throws RemoteException;

    void noteSyncFinish(String str, int i) throws RemoteException;

    void noteSyncStart(String str, int i) throws RemoteException;

    void noteUserActivity(int i, int i2) throws RemoteException;

    void noteVibratorOff(int i) throws RemoteException;

    void noteVibratorOn(int i, long j) throws RemoteException;

    void noteWakeUp(String str, int i) throws RemoteException;

    void noteWifiBatchedScanStartedFromSource(WorkSource workSource, int i) throws RemoteException;

    void noteWifiBatchedScanStoppedFromSource(WorkSource workSource) throws RemoteException;

    void noteWifiControllerActivity(WifiActivityEnergyInfo wifiActivityEnergyInfo) throws RemoteException;

    void noteWifiMulticastDisabled(int i) throws RemoteException;

    void noteWifiMulticastEnabled(int i) throws RemoteException;

    void noteWifiOff() throws RemoteException;

    void noteWifiOn() throws RemoteException;

    void noteWifiRadioPowerState(int i, long j, int i2) throws RemoteException;

    void noteWifiRssiChanged(int i) throws RemoteException;

    void noteWifiRunning(WorkSource workSource) throws RemoteException;

    void noteWifiRunningChanged(WorkSource workSource, WorkSource workSource2) throws RemoteException;

    void noteWifiScanStarted(int i) throws RemoteException;

    void noteWifiScanStartedFromSource(WorkSource workSource) throws RemoteException;

    void noteWifiScanStopped(int i) throws RemoteException;

    void noteWifiScanStoppedFromSource(WorkSource workSource) throws RemoteException;

    void noteWifiState(int i, String str) throws RemoteException;

    void noteWifiStopped(WorkSource workSource) throws RemoteException;

    void noteWifiSupplicantStateChanged(int i, boolean z) throws RemoteException;

    void setBatteryState(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) throws RemoteException;

    boolean setChargingStateUpdateDelayMillis(int i) throws RemoteException;

    HealthStatsParceler takeUidSnapshot(int i) throws RemoteException;

    HealthStatsParceler[] takeUidSnapshots(int[] iArr) throws RemoteException;
}
