package com.android.internal.os;

import android.net.NetworkStatsSysApp;
import android.net.NetworkStatsSysApp.Entry;
import android.os.BatteryStats.ControllerActivityCounter;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Process;
import android.telephony.ModemActivityInfo;
import android.util.ArrayMap;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import com.android.internal.net.NetworkStatsFactorySysApp;
import com.android.internal.os.BatteryStatsImpl.ControllerActivityCounterImpl;
import com.android.internal.os.BatteryStatsImpl.DualTimer;
import com.android.internal.os.BatteryStatsImpl.LongSamplingCounter;
import com.android.internal.os.BatteryStatsImpl.OverflowArrayMap;
import com.android.internal.os.BatteryStatsImpl.StopwatchTimer;
import com.android.internal.os.BatteryStatsImpl.TimeBase;
import com.android.internal.os.BatteryStatsImpl.TimeBaseObs;
import com.android.internal.os.BatteryStatsImpl.Timer;
import com.android.internal.util.ArrayUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

public class UidAppBatteryStatsImpl {
    private static final String ANDROID = "android";
    private static final boolean DEBUG_APP = false;
    private static final int MAX_WAKELOCKS_PER_UID = 100;
    private static final int NUM_BT_TX_LEVELS = 1;
    private static final int NUM_WIFI_TX_LEVELS = 1;
    private static final String SUFIX_REMOTE = "remote";
    private static final String SYSTEM_SERVER = "system_server";
    private static final String TAG = "UidAppBatteryStatsImpl";
    private static final String UID1000 = "android.uid.system:1000";
    public static final int WAKE_LOCK_WEIGHT = 50;
    protected static PowerProfile mPowerProfile;
    private boolean mCalledFromSource = false;
    ArrayMap<String, String> mCommHashToCmdline = new ArrayMap();
    ArrayMap<String, String> mCommToCmdline = new ArrayMap();
    private final KernelSysAppCpuTimeReader mKernelSysAppCpuTimeReader = new KernelSysAppCpuTimeReader();
    private NetworkStatsSysApp mLastModemNetworkStatsSysApp = new NetworkStatsSysApp(0, -1);
    protected ArrayList<StopwatchTimer> mLastPartialTimers = new ArrayList();
    private NetworkStatsSysApp mLastWifiNetworkStatsSysApp = new NetworkStatsSysApp(0, -1);
    private NetworkStatsSysApp mMobileDelta;
    private final NetworkStatsFactorySysApp mNetworkStatsFactory = new NetworkStatsFactorySysApp();
    final ArrayList<StopwatchTimer> mPartialTimers = new ArrayList();
    private final ArrayMap<String, Long> mPkgCpuTime = new ArrayMap();
    final SparseArray<ArrayList<StopwatchTimer>> mSensorTimersSys = new SparseArray();
    private final int mUid;
    final ArrayMap<String, UidPackage> mUidPackageStats = new ArrayMap();
    private boolean mUseBpfStats;
    private NetworkStatsSysApp mWifiDelta;

    public static class UidPackage {
        protected BatteryStatsImpl mBsi;
        LongSamplingCounter[][] mCpuClusterSpeed;
        LongSamplingCounter mMobileRadioActiveCount;
        LongSamplingCounter mMobileRadioActiveTime;
        private ControllerActivityCounterImpl mModemControllerActivity;
        LongSamplingCounter[] mNetworkByteActivityCounters;
        LongSamplingCounter[] mNetworkPacketActivityCounters;
        public final TimeBase mOnBatteryBackgroundTimeBase;
        public final TimeBase mOnBatteryScreenOffBackgroundTimeBase;
        final String mPackageName;
        int mProcessState = 21;
        final ArrayMap<String, Proc> mProcessStats = new ArrayMap();
        final SparseArray<Sensor> mSensorStats = new SparseArray();
        LongSamplingCounter mSystemCpuTime;
        public UidAppBatteryStatsImpl mUbsi;
        LongSamplingCounter mUserCpuTime;
        final OverflowArrayMap<Wakelock> mWakelockStats;
        private ControllerActivityCounterImpl mWifiControllerActivity;
        boolean mWifiScanStarted;
        DualTimer mWifiScanTimer;

        /* renamed from: com.android.internal.os.UidAppBatteryStatsImpl$UidPackage$1 */
        class AnonymousClass1 extends OverflowArrayMap<Wakelock> {
            AnonymousClass1(BatteryStatsImpl x0, int x1) {
                Objects.requireNonNull(x0);
                super(x1);
            }

            public Wakelock instantiateObject() {
                return new Wakelock(UidPackage.this.mBsi, UidPackage.this);
            }
        }

        public static class Proc implements TimeBaseObs {
            boolean mActive = true;
            protected BatteryStatsImpl mBsi;
            long mForegroundTime;
            long mLoadedForegroundTime;
            final String mName;
            long mUnpluggedForegroundTime;

            public Proc(BatteryStatsImpl bsi, String name) {
                this.mBsi = bsi;
                this.mName = name;
                this.mBsi.mOnBatteryTimeBase.add(this);
            }

            public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
                this.mUnpluggedForegroundTime = this.mForegroundTime;
            }

            public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            }

            public void detach() {
                this.mActive = false;
                this.mBsi.mOnBatteryTimeBase.remove(this);
            }

            public boolean reset(boolean detachIfReset) {
                this.mUnpluggedForegroundTime = 0;
                this.mLoadedForegroundTime = 0;
                this.mForegroundTime = 0;
                if (detachIfReset) {
                    detach();
                }
                return true;
            }

            /* Access modifiers changed, original: 0000 */
            public void writeToParcelLocked(Parcel out) {
                out.writeLong(this.mForegroundTime);
                out.writeLong(this.mLoadedForegroundTime);
                out.writeLong(this.mUnpluggedForegroundTime);
            }

            /* Access modifiers changed, original: 0000 */
            public void readFromParcelLocked(Parcel in) {
                this.mForegroundTime = in.readLong();
                this.mLoadedForegroundTime = in.readLong();
                this.mUnpluggedForegroundTime = in.readLong();
            }

            public void addForegroundTimeLocked(long ttime) {
                this.mForegroundTime += ttime;
            }

            public boolean isActive() {
                return this.mActive;
            }

            public long getForegroundTime(int which) {
                long val = this.mForegroundTime;
                if (which == 1) {
                    return val - this.mLoadedForegroundTime;
                }
                if (which == 2) {
                    return val - this.mUnpluggedForegroundTime;
                }
                return val;
            }
        }

        public static class Sensor {
            protected BatteryStatsImpl mBsi;
            final int mHandle;
            protected UidPackage mSyspkg;
            DualTimer mTimer;

            public Sensor(BatteryStatsImpl bsi, UidPackage sp, int handle) {
                this.mBsi = bsi;
                this.mSyspkg = sp;
                this.mHandle = handle;
            }

            private DualTimer readTimersFromParcel(TimeBase timeBase, TimeBase bgTimeBase, Parcel in) {
                if (in.readInt() == 0) {
                    return null;
                }
                ArrayList<StopwatchTimer> pool = (ArrayList) this.mSyspkg.mUbsi.mSensorTimersSys.get(this.mHandle);
                if (pool == null) {
                    pool = new ArrayList();
                    this.mSyspkg.mUbsi.mSensorTimersSys.put(this.mHandle, pool);
                }
                return new DualTimer(this.mBsi.mClocks, null, 0, pool, timeBase, bgTimeBase, in);
            }

            /* Access modifiers changed, original: 0000 */
            public boolean reset() {
                if (!this.mTimer.reset(true)) {
                    return false;
                }
                this.mTimer = null;
                return true;
            }

            /* Access modifiers changed, original: 0000 */
            public void readFromParcelLocked(TimeBase timeBase, TimeBase bgTimeBase, Parcel in) {
                this.mTimer = readTimersFromParcel(timeBase, bgTimeBase, in);
            }

            /* Access modifiers changed, original: 0000 */
            public void writeToParcelLocked(Parcel out, long elapsedRealtimeUs) {
                Timer.writeTimerToParcel(out, this.mTimer, elapsedRealtimeUs);
            }

            public Timer getSensorTime() {
                return this.mTimer;
            }

            public Timer getSensorBackgroundTime() {
                DualTimer dualTimer = this.mTimer;
                if (dualTimer == null) {
                    return null;
                }
                return dualTimer.getSubTimer();
            }

            public int getHandle() {
                return this.mHandle;
            }
        }

        public static class Wakelock {
            protected BatteryStatsImpl mBsi;
            DualTimer mTimerPartial;
            protected UidPackage mUp;

            public Wakelock(BatteryStatsImpl bsi, UidPackage up) {
                this.mBsi = bsi;
                this.mUp = up;
            }

            private DualTimer readDualTimerFromParcel(int type, ArrayList<StopwatchTimer> pool, TimeBase timeBase, TimeBase bgTimeBase, Parcel in) {
                if (in.readInt() == 0) {
                    return null;
                }
                return new DualTimer(this.mBsi.mClocks, null, type, pool, timeBase, bgTimeBase, in);
            }

            /* Access modifiers changed, original: 0000 */
            public boolean reset() {
                boolean wlactive = false;
                DualTimer dualTimer = this.mTimerPartial;
                if (dualTimer != null) {
                    wlactive = false | (dualTimer.reset(false) ^ 1);
                }
                if (!wlactive) {
                    dualTimer = this.mTimerPartial;
                    if (dualTimer != null) {
                        dualTimer.detach();
                        this.mTimerPartial = null;
                    }
                }
                if (wlactive) {
                    return false;
                }
                return true;
            }

            /* Access modifiers changed, original: 0000 */
            public void readFromParcelLocked(TimeBase screenOffTimeBase, TimeBase screenOffBgTimeBase, Parcel in) {
                this.mTimerPartial = readDualTimerFromParcel(0, this.mUp.mUbsi.mPartialTimers, screenOffTimeBase, screenOffBgTimeBase, in);
            }

            /* Access modifiers changed, original: 0000 */
            public void writeToParcelLocked(Parcel out, long elapsedRealtimeUs) {
                Timer.writeTimerToParcel(out, this.mTimerPartial, elapsedRealtimeUs);
            }

            public Timer getWakeTime() {
                return this.mTimerPartial;
            }
        }

        public UidPackage(BatteryStatsImpl bsi, UidAppBatteryStatsImpl ubsi, String name) {
            this.mBsi = bsi;
            this.mUbsi = ubsi;
            this.mPackageName = name;
            this.mUserCpuTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mSystemCpuTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            BatteryStatsImpl batteryStatsImpl = this.mBsi;
            Objects.requireNonNull(batteryStatsImpl);
            this.mWakelockStats = new AnonymousClass1(batteryStatsImpl, -1);
            this.mOnBatteryScreenOffBackgroundTimeBase = new TimeBase();
            this.mOnBatteryScreenOffBackgroundTimeBase.init(this.mBsi.mClocks.uptimeMillis() * 1000, this.mBsi.mClocks.elapsedRealtime() * 1000);
            this.mOnBatteryBackgroundTimeBase = new TimeBase();
            this.mOnBatteryBackgroundTimeBase.init(this.mBsi.mClocks.uptimeMillis() * 1000, this.mBsi.mClocks.elapsedRealtime() * 1000);
            this.mWifiScanTimer = new DualTimer(this.mBsi.mClocks, null, -1, null, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
        }

        /* Access modifiers changed, original: 0000 */
        public void initNetworkActivityLocked() {
            this.mNetworkByteActivityCounters = new LongSamplingCounter[10];
            this.mNetworkPacketActivityCounters = new LongSamplingCounter[10];
            for (int i = 0; i < 10; i++) {
                this.mNetworkByteActivityCounters[i] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
                this.mNetworkPacketActivityCounters[i] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            }
            this.mMobileRadioActiveTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mMobileRadioActiveCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
        }

        /* Access modifiers changed, original: 0000 */
        public void noteNetworkActivityLocked(int type, long deltaBytes, long deltaPackets) {
            if (this.mNetworkByteActivityCounters == null) {
                initNetworkActivityLocked();
            }
            if (type < 0 || type >= 10) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown network activity type ");
                stringBuilder.append(type);
                stringBuilder.append(" was specified.");
                Slog.w(UidAppBatteryStatsImpl.TAG, stringBuilder.toString(), new Throwable());
                return;
            }
            this.mNetworkByteActivityCounters[type].addCountLocked(deltaBytes);
            this.mNetworkPacketActivityCounters[type].addCountLocked(deltaPackets);
        }

        /* Access modifiers changed, original: 0000 */
        public void noteMobileRadioActiveTimeLocked(long batteryUptime) {
            if (this.mNetworkByteActivityCounters == null) {
                initNetworkActivityLocked();
            }
            this.mMobileRadioActiveTime.addCountLocked(batteryUptime);
            this.mMobileRadioActiveCount.addCountLocked(1);
        }

        public boolean hasNetworkActivity() {
            return this.mNetworkByteActivityCounters != null;
        }

        public long getNetworkActivityBytes(int type, int which) {
            LongSamplingCounter[] longSamplingCounterArr = this.mNetworkByteActivityCounters;
            if (longSamplingCounterArr == null || type < 0 || type >= longSamplingCounterArr.length) {
                return 0;
            }
            return longSamplingCounterArr[type].getCountLocked(which);
        }

        public long getNetworkActivityPackets(int type, int which) {
            LongSamplingCounter[] longSamplingCounterArr = this.mNetworkPacketActivityCounters;
            if (longSamplingCounterArr == null || type < 0 || type >= longSamplingCounterArr.length) {
                return 0;
            }
            return longSamplingCounterArr[type].getCountLocked(which);
        }

        public long getMobileRadioActiveTime(int which) {
            LongSamplingCounter longSamplingCounter = this.mMobileRadioActiveTime;
            return longSamplingCounter != null ? longSamplingCounter.getCountLocked(which) : 0;
        }

        public int getMobileRadioActiveCount(int which) {
            LongSamplingCounter longSamplingCounter = this.mMobileRadioActiveCount;
            return longSamplingCounter != null ? (int) longSamplingCounter.getCountLocked(which) : 0;
        }

        public ControllerActivityCounter getWifiControllerActivity() {
            return this.mWifiControllerActivity;
        }

        public ControllerActivityCounter getModemControllerActivity() {
            return this.mModemControllerActivity;
        }

        public ControllerActivityCounterImpl getOrCreateWifiControllerActivityLocked() {
            if (this.mWifiControllerActivity == null) {
                this.mWifiControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 1);
            }
            return this.mWifiControllerActivity;
        }

        public ControllerActivityCounterImpl getOrCreateModemControllerActivityLocked() {
            if (this.mModemControllerActivity == null) {
                this.mModemControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 5);
            }
            return this.mModemControllerActivity;
        }

        public SparseArray<Sensor> getSensorStats() {
            return this.mSensorStats;
        }

        public DualTimer getSensorTimerLocked(int sensor, boolean create) {
            Sensor se = (Sensor) this.mSensorStats.get(sensor);
            if (se == null) {
                if (!create) {
                    return null;
                }
                se = new Sensor(this.mBsi, this, sensor);
                this.mSensorStats.put(sensor, se);
            }
            DualTimer t = se.mTimer;
            if (t != null) {
                return t;
            }
            ArrayList<StopwatchTimer> timers = (ArrayList) this.mUbsi.mSensorTimersSys.get(sensor);
            if (timers == null) {
                timers = new ArrayList();
                this.mUbsi.mSensorTimersSys.put(sensor, timers);
            }
            t = new DualTimer(this.mBsi.mClocks, null, 3, timers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            se.mTimer = t;
            return t;
        }

        public void noteStartSensor(int sensor, long elapsedRealtimeMs) {
            getSensorTimerLocked(sensor, true).startRunningLocked(elapsedRealtimeMs);
        }

        public void noteStopSensor(int sensor, long elapsedRealtimeMs) {
            DualTimer t = getSensorTimerLocked(sensor, null);
            if (t != null) {
                t.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteWifiScanStartedLocked(long elapsedRealtimeMs) {
            if (!this.mWifiScanStarted) {
                this.mWifiScanStarted = true;
                if (this.mWifiScanTimer == null) {
                    this.mWifiScanTimer = new DualTimer(this.mBsi.mClocks, null, -1, null, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
                }
                this.mWifiScanTimer.startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteWifiScanStoppedLocked(long elapsedRealtimeMs) {
            if (this.mWifiScanStarted) {
                this.mWifiScanStarted = false;
                this.mWifiScanTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public long getWifiScanTime(long elapsedRealtimeUs, int which) {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null) {
                return 0;
            }
            return dualTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        public boolean updateOnBatteryBgTimeBase(long uptimeUs, long realtimeUs) {
            boolean z = this.mBsi.mOnBatteryTimeBase.isRunning() && isInBackground();
            return this.mOnBatteryBackgroundTimeBase.setRunning(z, uptimeUs, realtimeUs);
        }

        public boolean updateOnBatteryScreenOffBgTimeBase(long uptimeUs, long realtimeUs) {
            boolean z = this.mBsi.mOnBatteryScreenOffTimeBase.isRunning() && isInBackground();
            return this.mOnBatteryScreenOffBackgroundTimeBase.setRunning(z, uptimeUs, realtimeUs);
        }

        public long getTimeAtCpuSpeed(int cluster, int step, int which) {
            LongSamplingCounter[] cpuSpeeds = this.mCpuClusterSpeed;
            if (cpuSpeeds != null && cluster >= 0 && cluster < cpuSpeeds.length) {
                cpuSpeeds = cpuSpeeds[cluster];
                if (cpuSpeeds != null && step >= 0 && step < cpuSpeeds.length) {
                    LongSamplingCounter c = cpuSpeeds[step];
                    if (c != null) {
                        return c.getCountLocked(which);
                    }
                }
            }
            return 0;
        }

        public long getUserCpuTimeUs(int which) {
            return this.mUserCpuTime.getCountLocked(which);
        }

        public long getSystemCpuTimeUs(int which) {
            return this.mSystemCpuTime.getCountLocked(which);
        }

        public String getPackageName() {
            return this.mPackageName;
        }

        public ArrayMap<String, Wakelock> getWakelockStats() {
            return this.mWakelockStats.getMap();
        }

        public boolean isInBackground() {
            return this.mProcessState >= 3;
        }

        public void updateUidAppProcessStateLocked(int procState) {
            int runningState;
            if (procState == 21) {
                runningState = 21;
            } else if (procState == 2) {
                runningState = 0;
            } else if (procState <= 5) {
                runningState = 1;
            } else if (procState <= 13) {
                runningState = 4;
            } else if (procState <= 7) {
                runningState = 2;
            } else if (procState <= 12) {
                runningState = 3;
            } else {
                runningState = 6;
            }
            if (this.mProcessState != runningState) {
                long elapsedRealtimeMs = this.mBsi.mClocks.elapsedRealtime();
                long uptimeMs = this.mBsi.mClocks.uptimeMillis();
                this.mProcessState = runningState;
                updateOnBatteryBgTimeBase(uptimeMs * 1000, elapsedRealtimeMs * 1000);
                updateOnBatteryScreenOffBgTimeBase(uptimeMs * 1000, 1000 * elapsedRealtimeMs);
            }
        }

        public StopwatchTimer getWakelockTimerLocked(Wakelock wl) {
            if (wl == null) {
                return null;
            }
            DualTimer t = wl.mTimerPartial;
            if (t == null) {
                t = new DualTimer(this.mBsi.mClocks, null, 0, this.mUbsi.mPartialTimers, this.mBsi.mOnBatteryScreenOffTimeBase, this.mOnBatteryScreenOffBackgroundTimeBase);
                wl.mTimerPartial = t;
            }
            return t;
        }

        public void noteStartWakeLocked(String name, long elapsedRealtimeMs) {
            Wakelock wl = (Wakelock) this.mWakelockStats.startObject(name);
            if (wl != null) {
                getWakelockTimerLocked(wl).startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteStopWakeLocked(String name, long elapsedRealtimeMs) {
            Wakelock wl = (Wakelock) this.mWakelockStats.stopObject(name);
            if (wl != null) {
                getWakelockTimerLocked(wl).stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void readWakeSummaryFromParcelLocked(String wlName, Parcel in) {
            Wakelock wl = new Wakelock(this.mBsi, this);
            this.mWakelockStats.add(wlName, wl);
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl).readSummaryFromParcelLocked(in);
            }
        }

        public boolean reset() {
            int iw;
            boolean active = false;
            this.mUserCpuTime.reset(false);
            this.mSystemCpuTime.reset(false);
            ArrayMap<String, Wakelock> wakeStats = this.mWakelockStats.getMap();
            for (iw = wakeStats.size() - 1; iw >= 0; iw--) {
                if (((Wakelock) wakeStats.valueAt(iw)).reset()) {
                    wakeStats.removeAt(iw);
                } else {
                    active = true;
                }
            }
            this.mWakelockStats.cleanup();
            if (this.mNetworkByteActivityCounters != null) {
                for (iw = 0; iw < 10; iw++) {
                    this.mNetworkByteActivityCounters[iw].reset(false);
                    this.mNetworkPacketActivityCounters[iw].reset(false);
                }
                this.mMobileRadioActiveTime.reset(false);
                this.mMobileRadioActiveCount.reset(false);
            }
            this.mOnBatteryBackgroundTimeBase.reset(this.mBsi.mClocks.elapsedRealtime() * 1000, this.mBsi.mClocks.uptimeMillis() * 1000);
            this.mOnBatteryScreenOffBackgroundTimeBase.reset(this.mBsi.mClocks.elapsedRealtime() * 1000, this.mBsi.mClocks.uptimeMillis() * 1000);
            for (iw = this.mProcessStats.size() - 1; iw >= 0; iw--) {
                ((Proc) this.mProcessStats.valueAt(iw)).detach();
            }
            this.mProcessStats.clear();
            if (!active) {
                this.mUserCpuTime.detach();
                this.mSystemCpuTime.detach();
                LongSamplingCounter[][] longSamplingCounterArr = this.mCpuClusterSpeed;
                if (longSamplingCounterArr != null) {
                    for (LongSamplingCounter[] cpuSpeeds : longSamplingCounterArr) {
                        if (cpuSpeeds != null) {
                            for (LongSamplingCounter c : cpuSpeeds) {
                                if (c != null) {
                                    c.detach();
                                }
                            }
                        }
                    }
                }
            }
            if (active) {
                return false;
            }
            return true;
        }

        public Proc getProcessStatsLocked(String name) {
            Proc ps = (Proc) this.mProcessStats.get(name);
            if (ps != null) {
                return ps;
            }
            ps = new Proc(this.mBsi, name);
            this.mProcessStats.put(name, ps);
            return ps;
        }

        public ArrayMap<String, Proc> getProcessStats() {
            return this.mProcessStats;
        }

        /* Access modifiers changed, original: 0000 */
        public void writeToParcelLocked(Parcel out, long uptimeUs, long elapsedRealtimeUs) {
            int iw;
            int ise;
            int ip;
            Parcel parcel = out;
            long j = elapsedRealtimeUs;
            Parcel parcel2 = out;
            long j2 = uptimeUs;
            long j3 = elapsedRealtimeUs;
            this.mOnBatteryBackgroundTimeBase.writeToParcel(parcel2, j2, j3);
            this.mOnBatteryScreenOffBackgroundTimeBase.writeToParcel(parcel2, j2, j3);
            ArrayMap<String, Wakelock> wakeStats = this.mWakelockStats.getMap();
            int NW = wakeStats.size();
            parcel.writeInt(NW);
            for (iw = 0; iw < NW; iw++) {
                parcel.writeString((String) wakeStats.keyAt(iw));
                ((Wakelock) wakeStats.valueAt(iw)).writeToParcelLocked(parcel, j);
            }
            iw = this.mSensorStats.size();
            parcel.writeInt(iw);
            for (ise = 0; ise < iw; ise++) {
                parcel.writeInt(this.mSensorStats.keyAt(ise));
                ((Sensor) this.mSensorStats.valueAt(ise)).writeToParcelLocked(parcel, j);
            }
            ise = this.mProcessStats.size();
            parcel.writeInt(ise);
            for (ip = 0; ip < ise; ip++) {
                parcel.writeString((String) this.mProcessStats.keyAt(ip));
                ((Proc) this.mProcessStats.valueAt(ip)).writeToParcelLocked(parcel);
            }
            int i = 0;
            int i2 = 1;
            if (this.mWifiScanTimer != null) {
                parcel.writeInt(1);
                this.mWifiScanTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mNetworkByteActivityCounters != null) {
                parcel.writeInt(1);
                for (ip = 0; ip < 10; ip++) {
                    this.mNetworkByteActivityCounters[ip].writeToParcel(parcel);
                    this.mNetworkPacketActivityCounters[ip].writeToParcel(parcel);
                }
                this.mMobileRadioActiveTime.writeToParcel(parcel);
                this.mMobileRadioActiveCount.writeToParcel(parcel);
            } else {
                parcel.writeInt(0);
            }
            if (this.mWifiControllerActivity != null) {
                parcel.writeInt(1);
                this.mWifiControllerActivity.writeToParcel(parcel, 0);
            } else {
                parcel.writeInt(0);
            }
            if (this.mModemControllerActivity != null) {
                parcel.writeInt(1);
                this.mModemControllerActivity.writeToParcel(parcel, 0);
            } else {
                parcel.writeInt(0);
            }
            this.mUserCpuTime.writeToParcel(parcel);
            this.mSystemCpuTime.writeToParcel(parcel);
            if (this.mCpuClusterSpeed != null) {
                parcel.writeInt(1);
                parcel.writeInt(this.mCpuClusterSpeed.length);
                LongSamplingCounter[][] longSamplingCounterArr = this.mCpuClusterSpeed;
                int length = longSamplingCounterArr.length;
                int i3 = 0;
                while (i3 < length) {
                    LongSamplingCounter[] cpuSpeeds = longSamplingCounterArr[i3];
                    if (cpuSpeeds != null) {
                        parcel.writeInt(i2);
                        parcel.writeInt(cpuSpeeds.length);
                        int length2 = cpuSpeeds.length;
                        int i4 = i;
                        while (i4 < length2) {
                            LongSamplingCounter c = cpuSpeeds[i4];
                            if (c != null) {
                                parcel.writeInt(i2);
                                c.writeToParcel(parcel);
                                i2 = 0;
                            } else {
                                i2 = 0;
                                parcel.writeInt(0);
                            }
                            i4++;
                            i = i2;
                            i2 = 1;
                        }
                        i2 = i;
                    } else {
                        i2 = i;
                        parcel.writeInt(i2);
                    }
                    i3++;
                    i = i2;
                    i2 = 1;
                }
                return;
            }
            parcel.writeInt(0);
        }

        /* Access modifiers changed, original: 0000 */
        public void readFromParcelLocked(TimeBase screenOffTimeBase, Parcel in) {
            int j;
            int k;
            int sensorNumber;
            int i;
            this.mOnBatteryBackgroundTimeBase.readFromParcel(in);
            this.mOnBatteryScreenOffBackgroundTimeBase.readFromParcel(in);
            int numWakelocks = in.readInt();
            this.mWakelockStats.clear();
            for (j = 0; j < numWakelocks; j++) {
                String wakelockName = in.readString();
                Wakelock wakelock = new Wakelock(this.mBsi, this);
                wakelock.readFromParcelLocked(screenOffTimeBase, this.mOnBatteryScreenOffBackgroundTimeBase, in);
                this.mWakelockStats.add(wakelockName, wakelock);
            }
            j = in.readInt();
            this.mSensorStats.clear();
            for (k = 0; k < j; k++) {
                sensorNumber = in.readInt();
                Sensor sensor = new Sensor(this.mBsi, this, sensorNumber);
                sensor.readFromParcelLocked(this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase, in);
                this.mSensorStats.put(sensorNumber, sensor);
            }
            k = in.readInt();
            this.mProcessStats.clear();
            for (sensorNumber = 0; sensorNumber < k; sensorNumber++) {
                String processName = in.readString();
                Proc proc = new Proc(this.mBsi, processName);
                proc.readFromParcelLocked(in);
                this.mProcessStats.put(processName, proc);
            }
            this.mProcessState = 21;
            this.mWifiScanStarted = false;
            if (in.readInt() != 0) {
                this.mWifiScanTimer = new DualTimer(this.mBsi.mClocks, null, -1, null, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase, in);
            } else {
                this.mWifiScanTimer = null;
            }
            if (in.readInt() != 0) {
                this.mNetworkByteActivityCounters = new LongSamplingCounter[10];
                this.mNetworkPacketActivityCounters = new LongSamplingCounter[10];
                for (i = 0; i < 10; i++) {
                    this.mNetworkByteActivityCounters[i] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
                    this.mNetworkPacketActivityCounters[i] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
                }
                this.mMobileRadioActiveTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
                this.mMobileRadioActiveCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mNetworkByteActivityCounters = null;
                this.mNetworkPacketActivityCounters = null;
            }
            if (in.readInt() != 0) {
                this.mWifiControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 1, in);
            } else {
                this.mWifiControllerActivity = null;
            }
            if (in.readInt() != 0) {
                this.mModemControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 5, in);
            } else {
                this.mModemControllerActivity = null;
            }
            this.mUserCpuTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
            this.mSystemCpuTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
            if (in.readInt() != 0) {
                sensorNumber = in.readInt();
                if (UidAppBatteryStatsImpl.mPowerProfile == null || UidAppBatteryStatsImpl.mPowerProfile.getNumCpuClusters() == sensorNumber) {
                    this.mCpuClusterSpeed = new LongSamplingCounter[sensorNumber][];
                    i = 0;
                    while (i < sensorNumber) {
                        if (in.readInt() != 0) {
                            int numSpeeds = in.readInt();
                            if (UidAppBatteryStatsImpl.mPowerProfile == null || UidAppBatteryStatsImpl.mPowerProfile.getNumSpeedStepsInCpuCluster(i) == numSpeeds) {
                                LongSamplingCounter[] cpuSpeeds = new LongSamplingCounter[numSpeeds];
                                this.mCpuClusterSpeed[i] = cpuSpeeds;
                                for (int speed = 0; speed < numSpeeds; speed++) {
                                    if (in.readInt() != 0) {
                                        cpuSpeeds[speed] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
                                    }
                                }
                            } else {
                                throw new ParcelFormatException("Incompatible number of cpu speeds");
                            }
                        }
                        this.mCpuClusterSpeed[i] = null;
                        i++;
                    }
                    return;
                }
                throw new ParcelFormatException("Incompatible number of cpu clusters");
            }
            this.mCpuClusterSpeed = null;
        }
    }

    public UidAppBatteryStatsImpl(int uid) {
        this.mUid = uid;
        this.mUseBpfStats = new File("/sys/fs/bpf/traffic_uid_stats_map").exists();
    }

    static void setPowerProfile(PowerProfile profile) {
        mPowerProfile = profile;
    }

    public void markPartialTimersAsEligible() {
        int i;
        if (ArrayUtils.referenceEquals(this.mPartialTimers, this.mLastPartialTimers)) {
            for (i = this.mPartialTimers.size() - 1; i >= 0; i--) {
                ((StopwatchTimer) this.mPartialTimers.get(i)).mInList = true;
            }
            return;
        }
        for (i = this.mLastPartialTimers.size() - 1; i >= 0; i--) {
            ((StopwatchTimer) this.mLastPartialTimers.get(i)).mInList = false;
        }
        this.mLastPartialTimers.clear();
        i = this.mPartialTimers.size();
        for (int i2 = 0; i2 < i; i2++) {
            StopwatchTimer timer = (StopwatchTimer) this.mPartialTimers.get(i2);
            timer.mInList = true;
            this.mLastPartialTimers.add(timer);
        }
    }

    public void readKernelSysAppCpuTimesLocked(BatteryStatsImpl bsi, boolean onBattery) {
        if (onBattery) {
            this.mPkgCpuTime.clear();
            long startTimeMs = bsi.mClocks.uptimeMillis();
            this.mKernelSysAppCpuTimeReader.readDelta(new -$$Lambda$UidAppBatteryStatsImpl$_6qSEH1he1nSVJ97cFOKql74mJ0(this, bsi, onBattery));
            long elapsedTimeMs = bsi.mClocks.uptimeMillis() - startTimeMs;
            if (elapsedTimeMs >= 100) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Reading cpu stats took ");
                stringBuilder.append(elapsedTimeMs);
                stringBuilder.append("ms");
                Slog.d(TAG, stringBuilder.toString());
            }
            return;
        }
        this.mKernelSysAppCpuTimeReader.readDelta(null);
    }

    public /* synthetic */ void lambda$readKernelSysAppCpuTimesLocked$0$UidAppBatteryStatsImpl(BatteryStatsImpl bsi, boolean onBattery, String packageName, long userTimeUs, long systemTimeUs) {
        UidPackage p = getUidPackageStatsLocked(bsi, packageName, -1, 0);
        if (p != null) {
            StringBuilder sb = null;
            if (sb != null) {
                sb.append("  adding to package=");
                sb.append(p.getPackageName());
                sb.append(": u=");
                TimeUtils.formatDuration(userTimeUs / 1000, sb);
                sb.append(" s=");
                TimeUtils.formatDuration(systemTimeUs / 1000, sb);
                Slog.d(TAG, sb.toString());
            }
            p.mUserCpuTime.addCountLocked(userTimeUs, onBattery);
            p.mSystemCpuTime.addCountLocked(systemTimeUs, onBattery);
            this.mPkgCpuTime.put(packageName, Long.valueOf(userTimeUs + systemTimeUs));
        }
    }

    public void updateClusterSpeedTimes(BatteryStatsImpl bsi, long[][] clusterSpeedTimesMs, boolean onBattery) {
        int cluster;
        int speed;
        UidAppBatteryStatsImpl uidAppBatteryStatsImpl = this;
        BatteryStatsImpl batteryStatsImpl = bsi;
        long[][] jArr = clusterSpeedTimesMs;
        long totalCpuClustersTimeMs = 0;
        for (cluster = 0; cluster < jArr.length; cluster++) {
            if (jArr[cluster] != null) {
                for (speed = jArr[cluster].length - 1; speed >= 0; speed--) {
                    totalCpuClustersTimeMs += jArr[cluster][speed];
                }
            }
        }
        boolean z;
        if (totalCpuClustersTimeMs != 0) {
            cluster = uidAppBatteryStatsImpl.mPkgCpuTime.size();
            speed = 0;
            while (speed < cluster) {
                UidPackage sp = uidAppBatteryStatsImpl.getUidPackageStatsLocked(batteryStatsImpl, (String) uidAppBatteryStatsImpl.mPkgCpuTime.keyAt(speed), -1, 0);
                long appCpuTimeUs = ((Long) uidAppBatteryStatsImpl.mPkgCpuTime.valueAt(speed)).longValue();
                int numClusters = mPowerProfile.getNumCpuClusters();
                if (sp.mCpuClusterSpeed == null || sp.mCpuClusterSpeed.length != numClusters) {
                    sp.mCpuClusterSpeed = new LongSamplingCounter[numClusters][];
                }
                int cluster2 = 0;
                while (cluster2 < jArr.length) {
                    int speedsInCluster = jArr[cluster2].length;
                    if (sp.mCpuClusterSpeed[cluster2] == null || speedsInCluster != sp.mCpuClusterSpeed[cluster2].length) {
                        sp.mCpuClusterSpeed[cluster2] = new LongSamplingCounter[speedsInCluster];
                    }
                    LongSamplingCounter[] cpuSpeeds = sp.mCpuClusterSpeed[cluster2];
                    int speed2 = 0;
                    while (speed2 < speedsInCluster) {
                        if (cpuSpeeds[speed2] == null) {
                            cpuSpeeds[speed2] = new LongSamplingCounter(batteryStatsImpl.mOnBatteryTimeBase);
                        }
                        cpuSpeeds[speed2].addCountLocked((jArr[cluster2][speed2] * appCpuTimeUs) / totalCpuClustersTimeMs, onBattery);
                        speed2++;
                        batteryStatsImpl = bsi;
                        jArr = clusterSpeedTimesMs;
                    }
                    z = onBattery;
                    cluster2++;
                    batteryStatsImpl = bsi;
                    jArr = clusterSpeedTimesMs;
                }
                z = onBattery;
                speed++;
                uidAppBatteryStatsImpl = this;
                batteryStatsImpl = bsi;
                jArr = clusterSpeedTimesMs;
            }
            z = onBattery;
            return;
        }
        z = onBattery;
    }

    public void readSummaryFromParcel(BatteryStatsImpl bsi, Parcel in) throws ParcelFormatException {
        int NS = in.readInt();
        if (NS <= 10000) {
            int iu = 0;
            while (iu < NS) {
                int i;
                int cluster;
                int NSB;
                int speed;
                String pn = in.readString();
                UidPackage sp = new UidPackage(bsi, this, pn);
                this.mUidPackageStats.put(pn, sp);
                sp.mOnBatteryBackgroundTimeBase.readSummaryFromParcel(in);
                sp.mOnBatteryScreenOffBackgroundTimeBase.readSummaryFromParcel(in);
                sp.mProcessState = 21;
                sp.mWifiScanStarted = false;
                if (in.readInt() != 0) {
                    sp.mWifiScanTimer.readSummaryFromParcelLocked(in);
                }
                if (in.readInt() != 0) {
                    if (sp.mNetworkByteActivityCounters == null) {
                        sp.initNetworkActivityLocked();
                    }
                    for (i = 0; i < 10; i++) {
                        sp.mNetworkByteActivityCounters[i].readSummaryFromParcelLocked(in);
                        sp.mNetworkPacketActivityCounters[i].readSummaryFromParcelLocked(in);
                    }
                    sp.mMobileRadioActiveTime.readSummaryFromParcelLocked(in);
                    sp.mMobileRadioActiveCount.readSummaryFromParcelLocked(in);
                }
                sp.mUserCpuTime.readSummaryFromParcelLocked(in);
                sp.mSystemCpuTime.readSummaryFromParcelLocked(in);
                if (in.readInt() != 0) {
                    i = in.readInt();
                    PowerProfile powerProfile = mPowerProfile;
                    if (powerProfile == null || powerProfile.getNumCpuClusters() == i) {
                        sp.mCpuClusterSpeed = new LongSamplingCounter[i][];
                        cluster = 0;
                        while (cluster < i) {
                            if (in.readInt() != 0) {
                                NSB = in.readInt();
                                PowerProfile powerProfile2 = mPowerProfile;
                                if (powerProfile2 == null || powerProfile2.getNumSpeedStepsInCpuCluster(cluster) == NSB) {
                                    sp.mCpuClusterSpeed[cluster] = new LongSamplingCounter[NSB];
                                    for (speed = 0; speed < NSB; speed++) {
                                        if (in.readInt() != 0) {
                                            sp.mCpuClusterSpeed[cluster][speed] = new LongSamplingCounter(bsi.mOnBatteryTimeBase);
                                            sp.mCpuClusterSpeed[cluster][speed].readSummaryFromParcelLocked(in);
                                        }
                                    }
                                } else {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("File corrupt: too many speed bins ");
                                    stringBuilder.append(NSB);
                                    throw new ParcelFormatException(stringBuilder.toString());
                                }
                            }
                            sp.mCpuClusterSpeed[cluster] = null;
                            cluster++;
                        }
                    } else {
                        throw new ParcelFormatException("Incompatible cpu cluster arrangement");
                    }
                }
                sp.mCpuClusterSpeed = null;
                i = in.readInt();
                if (i <= 101) {
                    int iw;
                    for (iw = 0; iw < i; iw++) {
                        sp.readWakeSummaryFromParcelLocked(in.readString(), in);
                    }
                    iw = in.readInt();
                    StringBuilder stringBuilder2;
                    if (iw <= 1000) {
                        for (NSB = 0; NSB < iw; NSB++) {
                            speed = in.readInt();
                            if (in.readInt() != 0) {
                                sp.getSensorTimerLocked(speed, true).readSummaryFromParcelLocked(in);
                            }
                        }
                        iw = in.readInt();
                        if (iw <= 1000) {
                            for (cluster = 0; cluster < iw; cluster++) {
                                Proc p = sp.getProcessStatsLocked(in.readString());
                                long readLong = in.readLong();
                                p.mLoadedForegroundTime = readLong;
                                p.mForegroundTime = readLong;
                            }
                            iu++;
                        } else {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("File corrupt: too many processes ");
                            stringBuilder2.append(iw);
                            throw new ParcelFormatException(stringBuilder2.toString());
                        }
                    }
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("File corrupt: too many sensors ");
                    stringBuilder2.append(iw);
                    throw new ParcelFormatException(stringBuilder2.toString());
                }
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("File corrupt: too many wake locks ");
                stringBuilder3.append(i);
                throw new ParcelFormatException(stringBuilder3.toString());
            }
            return;
        }
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("File corrupt: too many system packages ");
        stringBuilder4.append(NS);
        throw new ParcelFormatException(stringBuilder4.toString());
    }

    public void writeSummaryToParcel(Parcel out, long now_sys, long nowreal_sys) {
        Parcel parcel = out;
        long j = nowreal_sys;
        int NS = this.mUidPackageStats.size();
        parcel.writeInt(NS);
        for (int iu = 0; iu < NS; iu++) {
            int i;
            parcel.writeString((String) this.mUidPackageStats.keyAt(iu));
            UidPackage sp = (UidPackage) this.mUidPackageStats.valueAt(iu);
            Parcel parcel2 = out;
            long j2 = now_sys;
            long j3 = nowreal_sys;
            sp.mOnBatteryBackgroundTimeBase.writeSummaryToParcel(parcel2, j2, j3);
            sp.mOnBatteryScreenOffBackgroundTimeBase.writeSummaryToParcel(parcel2, j2, j3);
            if (sp.mWifiScanTimer != null) {
                parcel.writeInt(1);
                sp.mWifiScanTimer.writeSummaryFromParcelLocked(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (sp.mNetworkByteActivityCounters == null) {
                parcel.writeInt(0);
            } else {
                parcel.writeInt(1);
                for (int i2 = 0; i2 < 10; i2++) {
                    sp.mNetworkByteActivityCounters[i2].writeSummaryFromParcelLocked(parcel);
                    sp.mNetworkPacketActivityCounters[i2].writeSummaryFromParcelLocked(parcel);
                }
                sp.mMobileRadioActiveTime.writeSummaryFromParcelLocked(parcel);
                sp.mMobileRadioActiveCount.writeSummaryFromParcelLocked(parcel);
            }
            sp.mUserCpuTime.writeSummaryFromParcelLocked(parcel);
            sp.mSystemCpuTime.writeSummaryFromParcelLocked(parcel);
            if (sp.mCpuClusterSpeed != null) {
                parcel.writeInt(1);
                parcel.writeInt(sp.mCpuClusterSpeed.length);
                for (LongSamplingCounter[] cpuSpeeds : sp.mCpuClusterSpeed) {
                    if (cpuSpeeds != null) {
                        parcel.writeInt(1);
                        parcel.writeInt(cpuSpeeds.length);
                        for (LongSamplingCounter c : cpuSpeeds) {
                            if (c != null) {
                                parcel.writeInt(1);
                                c.writeSummaryFromParcelLocked(parcel);
                            } else {
                                parcel.writeInt(0);
                            }
                        }
                    } else {
                        parcel.writeInt(0);
                    }
                }
            } else {
                parcel.writeInt(0);
            }
            ArrayMap<String, Wakelock> wakeStats = sp.mWakelockStats.getMap();
            int size = wakeStats.size();
            parcel.writeInt(size);
            for (i = 0; i < size; i++) {
                parcel.writeString((String) wakeStats.keyAt(i));
                Wakelock wl = (Wakelock) wakeStats.valueAt(i);
                if (wl.mTimerPartial != null) {
                    parcel.writeInt(1);
                    wl.mTimerPartial.writeSummaryFromParcelLocked(parcel, j);
                } else {
                    parcel.writeInt(0);
                }
            }
            i = sp.mSensorStats.size();
            parcel.writeInt(i);
            for (int ise = 0; ise < i; ise++) {
                parcel.writeInt(sp.mSensorStats.keyAt(ise));
                Sensor se = (Sensor) sp.mSensorStats.valueAt(ise);
                if (se.mTimer != null) {
                    parcel.writeInt(1);
                    se.mTimer.writeSummaryFromParcelLocked(parcel, j);
                } else {
                    parcel.writeInt(0);
                }
            }
            int NP = sp.mProcessStats.size();
            parcel.writeInt(NP);
            for (int ip = 0; ip < NP; ip++) {
                parcel.writeString((String) sp.mProcessStats.keyAt(ip));
                parcel.writeLong(((Proc) sp.mProcessStats.valueAt(ip)).mForegroundTime);
            }
        }
    }

    public void readFromParcelLocked(BatteryStatsImpl bsi, Parcel in) {
        int numUidPackagess = in.readInt();
        this.mUidPackageStats.clear();
        this.mPartialTimers.clear();
        for (int i = 0; i < numUidPackagess; i++) {
            String packageName = in.readString();
            UidPackage sp = new UidPackage(bsi, this, packageName);
            sp.readFromParcelLocked(bsi.mOnBatteryScreenOffTimeBase, in);
            this.mUidPackageStats.put(packageName, sp);
        }
    }

    public void writeToParcelLocked(Parcel out, long now_sys, long nowreal_sys) {
        int ssize = this.mUidPackageStats.size();
        out.writeInt(ssize);
        for (int i = 0; i < ssize; i++) {
            out.writeString((String) this.mUidPackageStats.keyAt(i));
            ((UidPackage) this.mUidPackageStats.valueAt(i)).writeToParcelLocked(out, now_sys, nowreal_sys);
        }
    }

    public void resetAllStatsLocked() {
        int i = 0;
        while (i < this.mUidPackageStats.size()) {
            if (((UidPackage) this.mUidPackageStats.valueAt(i)).reset()) {
                ArrayMap arrayMap = this.mUidPackageStats;
                arrayMap.remove(arrayMap.keyAt(i));
                i--;
            }
            i++;
        }
        this.mCommToCmdline.clear();
        this.mCommHashToCmdline.clear();
    }

    public void clearAllPackagesLocked() {
        this.mCommToCmdline.clear();
        this.mCommHashToCmdline.clear();
    }

    private String getProcComm(int pid) {
        StringBuilder stringBuilder;
        String str = "failed to readProcCmdlineFile:";
        String str2 = TAG;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("/proc/");
        stringBuilder2.append(pid);
        stringBuilder2.append("/comm");
        File file = new File(stringBuilder2.toString());
        if (!file.isFile()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[100];
            int read = fis.read(buffer);
            int len = read;
            if (read != -1) {
                String str3 = new String(buffer, 0, len - 1);
                try {
                    fis.close();
                } catch (IOException ex) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(ex.getMessage());
                    Slog.e(str2, stringBuilder.toString());
                }
                return str3;
            }
            try {
                fis.close();
            } catch (IOException ex2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(ex2.getMessage());
                Slog.e(str2, stringBuilder.toString());
            }
            return null;
        } catch (IOException e) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex22) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(str);
                    stringBuilder3.append(ex22.getMessage());
                    Slog.e(str2, stringBuilder3.toString());
                }
            }
            return null;
        } catch (Throwable th) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex3) {
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append(str);
                    stringBuilder4.append(ex3.getMessage());
                    Slog.e(str2, stringBuilder4.toString());
                }
            }
        }
    }

    private String getProcCmdline(int pid) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/proc/");
        stringBuilder.append(pid);
        stringBuilder.append("/cmdline");
        return readProcCmdlineFile(stringBuilder.toString());
    }

    private String readProcCmdlineFile(String cmdlineFile) {
        StringBuilder stringBuilder;
        String str = "failed to readProcCmdlineFile:";
        String str2 = TAG;
        File file = new File(cmdlineFile);
        if (!file.isFile()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[256];
            int read = fis.read(buffer);
            int len = read;
            if (read != -1) {
                String cmd = new String(buffer, 0, len - 1);
                int start = 0;
                int end = cmd.length();
                if (cmd.contains("\u0000")) {
                    end = cmd.indexOf(0);
                }
                int in = cmd.lastIndexOf(47, end);
                if (in != -1) {
                    start = in + 1;
                }
                if (end > start) {
                    cmd = cmd.substring(start, end);
                }
                try {
                    fis.close();
                } catch (IOException ex) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(ex.getMessage());
                    Slog.e(str2, stringBuilder.toString());
                }
                return cmd;
            }
            try {
                fis.close();
            } catch (IOException ex2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(ex2.getMessage());
                Slog.e(str2, stringBuilder.toString());
            }
            return null;
        } catch (IOException e) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex22) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str);
                    stringBuilder2.append(ex22.getMessage());
                    Slog.e(str2, stringBuilder2.toString());
                }
            }
            return null;
        } catch (Throwable th) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex3) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(str);
                    stringBuilder3.append(ex3.getMessage());
                    Slog.e(str2, stringBuilder3.toString());
                }
            }
        }
    }

    private String getCommHash(String cmdline) {
        BigInteger hash = new BigInteger("5381");
        BigInteger maxLong = new BigInteger("1111111111111111111111111111111111111111111111111111111111111111", 2);
        for (int i = 0; i < cmdline.length(); i++) {
            BigInteger temp = hash;
            hash = hash.shiftLeft(5).and(maxLong).add(temp).and(maxLong).add(new BigInteger(String.valueOf(cmdline.charAt(i)))).and(maxLong);
        }
        return hash.toString();
    }

    private void updateAllPackages() {
        int[] pids = Process.getPids("/proc", null);
        int NP = pids == null ? 0 : pids.length;
        for (int i = 0; i < NP; i++) {
            int pid = pids[i];
            if (pid >= 0 && Process.getUidForPid(pid) == this.mUid) {
                String cmdline = getProcCmdline(pid);
                String comm = getProcComm(pid);
                if (!(cmdline == null || cmdline.isEmpty() || comm == null || comm.isEmpty() || this.mCommToCmdline.containsKey(comm))) {
                    this.mCommToCmdline.put(comm, cmdline);
                }
            }
        }
    }

    private void updateAllPackagesBpf() {
        int[] pids = Process.getPids("/proc", null);
        int NP = pids == null ? 0 : pids.length;
        for (int i = 0; i < NP; i++) {
            int pid = pids[i];
            if (pid >= 0 && Process.getUidForPid(pid) == this.mUid) {
                String cmdline = getProcCmdline(pid);
                String comm = getProcComm(pid);
                if (!(comm == null || comm.isEmpty())) {
                    String hash = getCommHash(comm);
                    if (!(cmdline == null || cmdline.isEmpty() || this.mCommHashToCmdline.containsKey(hash))) {
                        this.mCommHashToCmdline.put(hash, cmdline);
                    }
                }
            }
        }
    }

    public UidPackage getUidPackageStatsLocked(BatteryStatsImpl bsi, String packageName, int pid, int type) {
        String completeName = null;
        if (type != 0) {
            if (type == 1) {
                completeName = getProcCmdline(pid);
                if (!this.mCommToCmdline.containsValue(completeName)) {
                    updateAllPackages();
                }
                if (!this.mCommToCmdline.containsValue(completeName)) {
                    return null;
                }
            } else if (type == 2) {
                completeName = (packageName.equals("android") || packageName.equals(UID1000)) ? SYSTEM_SERVER : packageName;
            } else if (type == 3) {
                if (this.mUseBpfStats) {
                    if (this.mCommHashToCmdline.containsKey(packageName)) {
                        completeName = (String) this.mCommHashToCmdline.get(packageName);
                    } else {
                        updateAllPackagesBpf();
                        if (this.mCommHashToCmdline.containsKey(packageName)) {
                            completeName = (String) this.mCommHashToCmdline.get(packageName);
                        }
                    }
                } else if (this.mCommToCmdline.containsKey(packageName)) {
                    completeName = (String) this.mCommToCmdline.get(packageName);
                } else {
                    updateAllPackages();
                    if (this.mCommToCmdline.containsKey(packageName)) {
                        completeName = (String) this.mCommToCmdline.get(packageName);
                    }
                }
            }
        } else if (this.mCommToCmdline.containsKey(packageName)) {
            completeName = (String) this.mCommToCmdline.get(packageName);
        } else {
            updateAllPackages();
            if (this.mCommToCmdline.containsKey(packageName)) {
                completeName = (String) this.mCommToCmdline.get(packageName);
            }
        }
        if (completeName == null || completeName.isEmpty()) {
            return null;
        }
        UidPackage p = (UidPackage) this.mUidPackageStats.get(completeName);
        if (p == null) {
            if (completeName.equals(packageName) || !this.mUidPackageStats.containsKey(packageName)) {
                p = new UidPackage(bsi, this, completeName);
                this.mUidPackageStats.put(completeName, p);
            } else {
                this.mUidPackageStats.put(completeName, (UidPackage) this.mUidPackageStats.get(packageName));
                this.mUidPackageStats.remove(packageName);
            }
        }
        return p;
    }

    public ArrayMap<String, UidPackage> getUidPackageStats() {
        return this.mUidPackageStats;
    }

    public void updateOnBatteryBgTimeBase(long uptime, long realtime) {
        for (int i = 0; i < this.mUidPackageStats.size(); i++) {
            ((UidPackage) this.mUidPackageStats.valueAt(i)).updateOnBatteryBgTimeBase(uptime, realtime);
        }
    }

    public void updateOnBatteryScreenOffBgTimeBase(long uptime, long realtime) {
        for (int i = 0; i < this.mUidPackageStats.size(); i++) {
            ((UidPackage) this.mUidPackageStats.valueAt(i)).updateOnBatteryScreenOffBgTimeBase(uptime, realtime);
        }
    }

    public void noteUidProcessStateLocked(int uid, int state) {
        if (uid == this.mUid) {
            for (int i = 0; i < this.mUidPackageStats.size(); i++) {
                ((UidPackage) this.mUidPackageStats.valueAt(i)).updateUidAppProcessStateLocked(state);
            }
        }
    }

    public void noteStartWakeForSourceLocked(BatteryStatsImpl bsi, int uid, String pkgName, int pid, int type, String name, long elapsedRealtime) {
        this.mCalledFromSource = true;
        if (uid == this.mUid && type == 0) {
            UidPackage sp = null;
            if (!(pkgName == null || pkgName.isEmpty())) {
                sp = getUidPackageStatsLocked(bsi, pkgName, -1, 2);
            }
            if (sp == null) {
                sp = getUidPackageStatsLocked(bsi, null, pid, 1);
            }
            if (sp != null) {
                sp.noteStartWakeLocked(name, elapsedRealtime);
            }
        }
    }

    public void noteStopWakeForSourceLocked(BatteryStatsImpl bsi, int uid, String pkgName, int pid, int type, String name, long elapsedRealtime) {
        this.mCalledFromSource = true;
        if (uid == this.mUid && type == 0) {
            UidPackage sp = null;
            if (!(pkgName == null || pkgName.isEmpty())) {
                sp = getUidPackageStatsLocked(bsi, pkgName, -1, 2);
            }
            if (sp == null) {
                sp = getUidPackageStatsLocked(bsi, null, pid, 1);
            }
            if (sp != null) {
                sp.noteStopWakeLocked(name, elapsedRealtime);
            }
        }
    }

    public void noteStartWakeLocked(BatteryStatsImpl bsi, int uid, int pid, int type, String name, long elapsedRealtime) {
        if (this.mCalledFromSource) {
            this.mCalledFromSource = false;
            return;
        }
        if (uid == this.mUid && type == 0) {
            UidPackage sp = getUidPackageStatsLocked(bsi, null, pid, 1);
            if (sp != null) {
                sp.noteStartWakeLocked(name, elapsedRealtime);
            }
        }
    }

    public void noteStopWakeLocked(BatteryStatsImpl bsi, int uid, int pid, int type, String name, long elapsedRealtime) {
        if (this.mCalledFromSource) {
            this.mCalledFromSource = false;
            return;
        }
        if (uid == this.mUid && type == 0) {
            UidPackage sp = getUidPackageStatsLocked(bsi, null, pid, 1);
            if (sp != null) {
                sp.noteStopWakeLocked(name, elapsedRealtime);
            }
        }
    }

    public void noteStartSensorLocked(BatteryStatsImpl bsi, int uid, int sensor, long elapsedRealtime, String packageName) {
        if (uid == this.mUid && packageName != null && !packageName.isEmpty()) {
            UidPackage sp = getUidPackageStatsLocked(bsi, packageName, -1, 2);
            if (sp != null) {
                sp.noteStartSensor(sensor, elapsedRealtime);
            }
        }
    }

    public void noteStopSensorLocked(BatteryStatsImpl bsi, int uid, int sensor, long elapsedRealtime, String packageName) {
        if (uid == this.mUid && packageName != null && !packageName.isEmpty()) {
            if (packageName.equals("all")) {
                int NU = this.mUidPackageStats.size();
                for (int iu = 0; iu < NU; iu++) {
                    ((UidPackage) this.mUidPackageStats.valueAt(iu)).noteStopSensor(sensor, elapsedRealtime);
                }
                return;
            }
            UidPackage sp = getUidPackageStatsLocked(bsi, packageName, -1, 2);
            if (sp != null) {
                sp.noteStopSensor(sensor, elapsedRealtime);
            }
        }
    }

    public void noteWifiScanStartedLocked(BatteryStatsImpl bsi, int uid, String name, long realTime) {
        if (uid == this.mUid && name != null && !name.isEmpty()) {
            UidPackage sp = getUidPackageStatsLocked(bsi, name, -1, 2);
            if (sp != null) {
                sp.noteWifiScanStartedLocked(realTime);
            }
        }
    }

    public void noteWifiScanStoppedLocked(BatteryStatsImpl bsi, int uid, String name, long realTime) {
        if (uid == this.mUid && name != null && !name.isEmpty()) {
            UidPackage sp = getUidPackageStatsLocked(bsi, name, -1, 2);
            if (sp != null) {
                sp.noteWifiScanStoppedLocked(realTime);
            }
        }
    }

    private NetworkStatsSysApp readSysAppNetworkStatsLocked(String[] ifaces) {
        try {
            if (!ArrayUtils.isEmpty((Object[]) ifaces)) {
                return this.mNetworkStatsFactory.readNSDForSystemApp(ifaces, null);
            }
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("failed to read sys network stats for ifaces: ");
            stringBuilder.append(e.getMessage());
            Slog.e(TAG, stringBuilder.toString());
        }
        return null;
    }

    public void calculateWifiDelta(String[] wifiIfaces) {
        NetworkStatsSysApp latestStatsSys = readSysAppNetworkStatsLocked(wifiIfaces);
        if (latestStatsSys != null) {
            this.mWifiDelta = NetworkStatsSysApp.subtract(latestStatsSys, this.mLastWifiNetworkStatsSysApp, null);
            this.mLastWifiNetworkStatsSysApp = latestStatsSys;
        }
    }

    public void releaseWifiDelta() {
        if (this.mWifiDelta != null) {
            this.mWifiDelta = null;
        }
    }

    public void updateWifiForPackage(BatteryStatsImpl bsi) {
        if (this.mWifiDelta != null) {
            Entry entry = new Entry();
            int size = this.mWifiDelta.size();
            for (int i = 0; i < size; i++) {
                entry = this.mWifiDelta.getValues(i, entry);
                if (entry.rxBytes != 0 || entry.txBytes != 0) {
                    UidPackage sp = getUidPackageStatsLocked(bsi, entry.nameOrHash, -1, 3);
                    if (sp != null) {
                        if (entry.rxBytes != 0) {
                            sp.noteNetworkActivityLocked(2, entry.rxBytes, entry.rxPackets);
                        }
                        if (entry.txBytes != 0) {
                            sp.noteNetworkActivityLocked(3, entry.txBytes, entry.txPackets);
                        }
                    }
                }
            }
            this.mWifiDelta = null;
        }
    }

    public void distributeWifiTrafficToSysApp(long elapsedRealtimeMs, long totalScanTimeMs, long rxTimeMs, long txTimeMs) {
        long j = elapsedRealtimeMs;
        int spStatsSize = this.mUidPackageStats.size();
        for (int i = 0; i < spStatsSize; i++) {
            UidPackage sp = (UidPackage) this.mUidPackageStats.valueAt(i);
            long scanTimeSinceMarkMs = sp.mWifiScanTimer.getTimeSinceMarkLocked(j * 1000) / 1000;
            if (scanTimeSinceMarkMs > 0) {
                sp.mWifiScanTimer.setMark(j);
                long scanRxTimeSinceMarkMs = scanTimeSinceMarkMs;
                long scanTxTimeSinceMarkMs = scanTimeSinceMarkMs;
                if (totalScanTimeMs > rxTimeMs) {
                    scanRxTimeSinceMarkMs = (rxTimeMs * scanRxTimeSinceMarkMs) / totalScanTimeMs;
                }
                if (totalScanTimeMs > txTimeMs) {
                    scanTxTimeSinceMarkMs = (txTimeMs * scanTxTimeSinceMarkMs) / totalScanTimeMs;
                }
                ControllerActivityCounterImpl activityCounter = sp.getOrCreateWifiControllerActivityLocked();
                activityCounter.getRxTimeCounter().addCountLocked(scanRxTimeSinceMarkMs);
                activityCounter.getTxTimeCounters()[0].addCountLocked(scanTxTimeSinceMarkMs);
            }
        }
    }

    public void calculateMobileRadioDelta(String[] modemIfaces) {
        NetworkStatsSysApp latestStatsSys = readSysAppNetworkStatsLocked(modemIfaces);
        if (latestStatsSys != null) {
            this.mMobileDelta = NetworkStatsSysApp.subtract(latestStatsSys, this.mLastModemNetworkStatsSysApp, null);
            this.mLastModemNetworkStatsSysApp = latestStatsSys;
        }
    }

    public void releaseMobileRadioDelta() {
        if (this.mMobileDelta != null) {
            this.mMobileDelta = null;
        }
    }

    public void updateMobileRadioForPackage(BatteryStatsImpl bsi, ModemActivityInfo activityInfo, long radioTimeSys, long totalRxPackets, long totalTxPackets) {
        BatteryStatsImpl batteryStatsImpl = bsi;
        if (this.mMobileDelta != null) {
            int i;
            int i2;
            long j;
            Entry entry = new Entry();
            int size = this.mMobileDelta.size();
            int i3 = 0;
            while (true) {
                i = 3;
                i2 = -1;
                j = 0;
                if (i3 >= size) {
                    break;
                }
                entry = this.mMobileDelta.getValues(i3, entry);
                if (entry.rxPackets != 0 || entry.txPackets != 0) {
                    UidPackage sp = getUidPackageStatsLocked(batteryStatsImpl, entry.nameOrHash, -1, 3);
                    if (sp != null) {
                        UidPackage uidPackage = sp;
                        uidPackage.noteNetworkActivityLocked(0, entry.rxBytes, entry.rxPackets);
                        uidPackage.noteNetworkActivityLocked(1, entry.txBytes, entry.txPackets);
                    }
                }
                i3++;
            }
            long totalPackets = totalRxPackets + totalTxPackets;
            if (totalPackets > 0) {
                i3 = 0;
                long radioTimeSys2 = radioTimeSys;
                while (i3 < size) {
                    Entry entry2;
                    entry = this.mMobileDelta.getValues(i3, entry);
                    if (!(entry.rxPackets == j && entry.txPackets == j)) {
                        UidPackage sp2 = getUidPackageStatsLocked(batteryStatsImpl, entry.nameOrHash, i2, i);
                        if (sp2 != null) {
                            long appPackets = entry.rxPackets + entry.txPackets;
                            long appRadioTime = (radioTimeSys2 * appPackets) / totalPackets;
                            sp2.noteMobileRadioActiveTimeLocked(appRadioTime);
                            radioTimeSys2 -= appRadioTime;
                            totalPackets -= appPackets;
                            if (activityInfo != null) {
                                ControllerActivityCounterImpl activityCounter = sp2.getOrCreateModemControllerActivityLocked();
                                if (totalRxPackets > j) {
                                    if (entry.rxPackets > j) {
                                        activityCounter.getRxTimeCounter().addCountLocked((entry.rxPackets * ((long) activityInfo.getRxTimeMillis())) / totalRxPackets);
                                    }
                                }
                                if (totalTxPackets <= 0 || entry.txPackets <= 0) {
                                    entry2 = entry;
                                } else {
                                    int lvl = 0;
                                    while (lvl < 5) {
                                        entry2 = entry;
                                        activityCounter.getTxTimeCounters()[lvl].addCountLocked((entry.txPackets * ((long) activityInfo.getTxTimeMillis()[lvl])) / totalTxPackets);
                                        lvl++;
                                        batteryStatsImpl = bsi;
                                        entry = entry2;
                                    }
                                    entry2 = entry;
                                }
                            } else {
                                entry2 = entry;
                                radioTimeSys = appRadioTime;
                            }
                            i3++;
                            batteryStatsImpl = bsi;
                            entry = entry2;
                            i = 3;
                            i2 = -1;
                            j = 0;
                        }
                    }
                    entry2 = entry;
                    i3++;
                    batteryStatsImpl = bsi;
                    entry = entry2;
                    i = 3;
                    i2 = -1;
                    j = 0;
                }
            }
            this.mMobileDelta = null;
            return;
        }
    }
}
