package com.android.internal.os;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.bluetooth.BluetoothActivityEnergyInfo;
import android.bluetooth.UidTraffic;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.INetworkStatsService;
import android.net.NetworkStats;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.BatteryStats;
import android.os.BatteryStats.ControllerActivityCounter;
import android.os.BatteryStats.DailyItem;
import android.os.BatteryStats.HistoryEventTracker;
import android.os.BatteryStats.HistoryItem;
import android.os.BatteryStats.HistoryPrinter;
import android.os.BatteryStats.HistoryStepDetails;
import android.os.BatteryStats.HistoryTag;
import android.os.BatteryStats.LevelStepTracker;
import android.os.BatteryStats.LongCounter;
import android.os.BatteryStats.LongCounterArray;
import android.os.BatteryStats.PackageChange;
import android.os.BatteryStats.Uid.Pid;
import android.os.BatteryStats.Uid.Proc.ExcessivePower;
import android.os.Build;
import android.os.Handler;
import android.os.IBatteryPropertiesRegistrar;
import android.os.IBatteryPropertiesRegistrar.Stub;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.WorkSource;
import android.os.WorkSource.WorkChain;
import android.os.connectivity.CellularBatteryStats;
import android.os.connectivity.GpsBatteryStats;
import android.os.connectivity.WifiBatteryStats;
import android.provider.Settings.Global;
import android.provider.Telephony.BaseMmsColumns;
import android.telephony.ModemActivityInfo;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.IntArray;
import android.util.KeyValueListParser;
import android.util.Log;
import android.util.LogWriter;
import android.util.LongSparseArray;
import android.util.LongSparseLongArray;
import android.util.MutableInt;
import android.util.Pools.Pool;
import android.util.Pools.SynchronizedPool;
import android.util.Printer;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.util.StatsLogInternal;
import android.util.TimeUtils;
import android.util.Xml;
import com.android.ims.ImsConfig;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.logging.EventLogTags;
import com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidActiveTimeReader;
import com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidClusterTimeReader;
import com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidFreqTimeReader;
import com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidUserSysTimeReader;
import com.android.internal.os.RpmStats.PowerStateElement;
import com.android.internal.os.RpmStats.PowerStatePlatformSleepState;
import com.android.internal.os.RpmStats.PowerStateSubsystem;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.FastXmlSerializer;
import com.android.internal.util.XmlUtils;
import com.miui.mishare.RemoteDevice;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import libcore.util.EmptyArray;
import miui.os.DeviceFeature;
import miui.process.ProcessList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class BatteryStatsImpl extends BatteryStats {
    static final int BATTERY_DELTA_LEVEL_FLAG = 1;
    public static final int BATTERY_PLUGGED_NONE = 0;
    @UnsupportedAppUsage
    public static final Creator<BatteryStatsImpl> CREATOR = new Creator<BatteryStatsImpl>() {
        public BatteryStatsImpl createFromParcel(Parcel in) {
            return new BatteryStatsImpl(in);
        }

        public BatteryStatsImpl[] newArray(int size) {
            return new BatteryStatsImpl[size];
        }
    };
    private static final boolean DEBUG = false;
    public static final boolean DEBUG_ENERGY = false;
    private static final boolean DEBUG_ENERGY_CPU = false;
    private static final boolean DEBUG_HISTORY = false;
    private static final boolean DEBUG_MEMORY = false;
    static final long DELAY_UPDATE_WAKELOCKS = 5000;
    static final int DELTA_BATTERY_CHARGE_FLAG = 16777216;
    static final int DELTA_BATTERY_LEVEL_FLAG = 524288;
    static final int DELTA_EVENT_FLAG = 8388608;
    static final int DELTA_STATE2_FLAG = 2097152;
    static final int DELTA_STATE_FLAG = 1048576;
    static final int DELTA_STATE_MASK = -33554432;
    static final int DELTA_TIME_ABS = 524285;
    static final int DELTA_TIME_INT = 524286;
    static final int DELTA_TIME_LONG = 524287;
    static final int DELTA_TIME_MASK = 524287;
    static final int DELTA_WAKELOCK_FLAG = 4194304;
    private static final int MAGIC = -1166707595;
    static final int MAX_DAILY_ITEMS = 10;
    static final int MAX_LEVEL_STEPS = 200;
    private static final int MAX_WAKELOCKS_PER_UID;
    private static final double MILLISECONDS_IN_HOUR = 3600000.0d;
    private static final long MILLISECONDS_IN_YEAR = 31536000000L;
    static final int MSG_REPORT_CHARGING = 3;
    static final int MSG_REPORT_CPU_UPDATE_NEEDED = 1;
    static final int MSG_REPORT_POWER_CHANGE = 2;
    static final int MSG_REPORT_RESET_STATS = 4;
    private static final int NUM_BT_TX_LEVELS = 1;
    private static final int NUM_WIFI_TX_LEVELS = 1;
    private static final long RPM_STATS_UPDATE_FREQ_MS = 1000;
    static final int STATE_BATTERY_HEALTH_MASK = 7;
    static final int STATE_BATTERY_HEALTH_SHIFT = 26;
    static final int STATE_BATTERY_MASK = -16777216;
    static final int STATE_BATTERY_PLUG_MASK = 3;
    static final int STATE_BATTERY_PLUG_SHIFT = 24;
    static final int STATE_BATTERY_STATUS_MASK = 7;
    static final int STATE_BATTERY_STATUS_SHIFT = 29;
    private static final String TAG = "BatteryStatsImpl";
    private static final int USB_DATA_CONNECTED = 2;
    private static final int USB_DATA_DISCONNECTED = 1;
    private static final int USB_DATA_UNKNOWN = 0;
    private static final boolean USE_OLD_HISTORY = false;
    static final int VERSION = 186;
    @VisibleForTesting
    public static final int WAKE_LOCK_WEIGHT = 50;
    final HistoryEventTracker mActiveEvents;
    int mActiveHistoryStates;
    int mActiveHistoryStates2;
    int mAudioOnNesting;
    StopwatchTimer mAudioOnTimer;
    final ArrayList<StopwatchTimer> mAudioTurnedOnTimers;
    final BatteryStatsHistory mBatteryStatsHistory;
    ControllerActivityCounterImpl mBluetoothActivity;
    int mBluetoothScanNesting;
    final ArrayList<StopwatchTimer> mBluetoothScanOnTimers;
    @VisibleForTesting(visibility = Visibility.PACKAGE)
    protected StopwatchTimer mBluetoothScanTimer;
    private BatteryCallback mCallback;
    int mCameraOnNesting;
    StopwatchTimer mCameraOnTimer;
    final ArrayList<StopwatchTimer> mCameraTurnedOnTimers;
    int mChangedStates;
    int mChangedStates2;
    final LevelStepTracker mChargeStepTracker;
    boolean mCharging;
    public final AtomicFile mCheckinFile;
    protected Clocks mClocks;
    @GuardedBy({"this"})
    final Constants mConstants;
    private long[] mCpuFreqs;
    @GuardedBy({"this"})
    private long mCpuTimeReadsTrackingStartTime;
    @VisibleForTesting
    protected KernelCpuUidActiveTimeReader mCpuUidActiveTimeReader;
    @VisibleForTesting
    protected KernelCpuUidClusterTimeReader mCpuUidClusterTimeReader;
    @VisibleForTesting
    protected KernelCpuUidFreqTimeReader mCpuUidFreqTimeReader;
    @VisibleForTesting
    protected KernelCpuUidUserSysTimeReader mCpuUidUserSysTimeReader;
    final HistoryStepDetails mCurHistoryStepDetails;
    long mCurStepCpuSystemTime;
    long mCurStepCpuUserTime;
    int mCurStepMode;
    long mCurStepStatIOWaitTime;
    long mCurStepStatIdleTime;
    long mCurStepStatIrqTime;
    long mCurStepStatSoftIrqTime;
    long mCurStepStatSystemTime;
    long mCurStepStatUserTime;
    int mCurrentBatteryLevel;
    final LevelStepTracker mDailyChargeStepTracker;
    final LevelStepTracker mDailyDischargeStepTracker;
    public final AtomicFile mDailyFile;
    final ArrayList<DailyItem> mDailyItems;
    ArrayList<PackageChange> mDailyPackageChanges;
    long mDailyStartTime;
    private final Runnable mDeferSetCharging;
    int mDeviceIdleMode;
    StopwatchTimer mDeviceIdleModeFullTimer;
    StopwatchTimer mDeviceIdleModeLightTimer;
    boolean mDeviceIdling;
    StopwatchTimer mDeviceIdlingTimer;
    boolean mDeviceLightIdling;
    StopwatchTimer mDeviceLightIdlingTimer;
    int mDischargeAmountScreenDoze;
    int mDischargeAmountScreenDozeSinceCharge;
    int mDischargeAmountScreenOff;
    int mDischargeAmountScreenOffSinceCharge;
    int mDischargeAmountScreenOn;
    int mDischargeAmountScreenOnSinceCharge;
    private LongSamplingCounter mDischargeCounter;
    int mDischargeCurrentLevel;
    private LongSamplingCounter mDischargeDeepDozeCounter;
    private LongSamplingCounter mDischargeLightDozeCounter;
    int mDischargePlugLevel;
    private LongSamplingCounter mDischargeScreenDozeCounter;
    int mDischargeScreenDozeUnplugLevel;
    private LongSamplingCounter mDischargeScreenOffCounter;
    int mDischargeScreenOffUnplugLevel;
    int mDischargeScreenOnUnplugLevel;
    int mDischargeStartLevel;
    final LevelStepTracker mDischargeStepTracker;
    int mDischargeUnplugLevel;
    boolean mDistributeWakelockCpu;
    final ArrayList<StopwatchTimer> mDrawTimers;
    String mEndPlatformVersion;
    private int mEstimatedBatteryCapacity;
    private ExternalStatsSync mExternalSync;
    int mFlashlightOnNesting;
    StopwatchTimer mFlashlightOnTimer;
    final ArrayList<StopwatchTimer> mFlashlightTurnedOnTimers;
    @UnsupportedAppUsage
    final ArrayList<StopwatchTimer> mFullTimers;
    final ArrayList<StopwatchTimer> mFullWifiLockTimers;
    boolean mGlobalWifiRunning;
    StopwatchTimer mGlobalWifiRunningTimer;
    int mGpsNesting;
    int mGpsSignalQualityBin;
    @VisibleForTesting(visibility = Visibility.PACKAGE)
    protected final StopwatchTimer[] mGpsSignalQualityTimer;
    public Handler mHandler;
    boolean mHasBluetoothReporting;
    boolean mHasModemReporting;
    boolean mHasWifiReporting;
    protected boolean mHaveBatteryLevel;
    int mHighDischargeAmountSinceCharge;
    HistoryItem mHistory;
    final HistoryItem mHistoryAddTmp;
    long mHistoryBaseTime;
    final Parcel mHistoryBuffer;
    int mHistoryBufferLastPos;
    HistoryItem mHistoryCache;
    final HistoryItem mHistoryCur;
    HistoryItem mHistoryEnd;
    private HistoryItem mHistoryIterator;
    HistoryItem mHistoryLastEnd;
    final HistoryItem mHistoryLastLastWritten;
    final HistoryItem mHistoryLastWritten;
    final HistoryItem mHistoryReadTmp;
    final HashMap<HistoryTag, Integer> mHistoryTagPool;
    int mInitStepMode;
    private String mInitialAcquireWakeName;
    private int mInitialAcquireWakeUid;
    boolean mInteractive;
    StopwatchTimer mInteractiveTimer;
    boolean mIsCellularTxPowerHigh;
    @GuardedBy({"this"})
    private boolean mIsPerProcessStateCpuDataStale;
    final SparseIntArray mIsolatedUids;
    private boolean mIteratingHistory;
    @VisibleForTesting
    protected KernelCpuSpeedReader[] mKernelCpuSpeedReaders;
    private final KernelMemoryBandwidthStats mKernelMemoryBandwidthStats;
    private final LongSparseArray<SamplingTimer> mKernelMemoryStats;
    @VisibleForTesting
    protected KernelSingleUidTimeReader mKernelSingleUidTimeReader;
    private final KernelWakelockReader mKernelWakelockReader;
    private final HashMap<String, SamplingTimer> mKernelWakelockStats;
    private final BluetoothActivityInfoCache mLastBluetoothActivityInfo;
    int mLastChargeStepLevel;
    int mLastChargingStateLevel;
    int mLastDischargeStepLevel;
    long mLastHistoryElapsedRealtime;
    HistoryStepDetails mLastHistoryStepDetails;
    byte mLastHistoryStepLevel;
    long mLastIdleTimeStart;
    private ModemActivityInfo mLastModemActivityInfo;
    @GuardedBy({"mModemNetworkLock"})
    private NetworkStats mLastModemNetworkStats;
    @VisibleForTesting
    protected ArrayList<StopwatchTimer> mLastPartialTimers;
    private long mLastRpmStatsUpdateTimeMs;
    long mLastStepCpuSystemTime;
    long mLastStepCpuUserTime;
    long mLastStepStatIOWaitTime;
    long mLastStepStatIdleTime;
    long mLastStepStatIrqTime;
    long mLastStepStatSoftIrqTime;
    long mLastStepStatSystemTime;
    long mLastStepStatUserTime;
    String mLastWakeupReason;
    long mLastWakeupUptimeMs;
    @GuardedBy({"mWifiNetworkLock"})
    private NetworkStats mLastWifiNetworkStats;
    long mLastWriteTime;
    long mLongestFullIdleTime;
    long mLongestLightIdleTime;
    int mLowDischargeAmountSinceCharge;
    int mMaxChargeStepLevel;
    private int mMaxLearnedBatteryCapacity;
    int mMinDischargeStepLevel;
    private int mMinLearnedBatteryCapacity;
    LongSamplingCounter mMobileRadioActiveAdjustedTime;
    StopwatchTimer mMobileRadioActivePerAppTimer;
    long mMobileRadioActiveStartTime;
    StopwatchTimer mMobileRadioActiveTimer;
    LongSamplingCounter mMobileRadioActiveUnknownCount;
    LongSamplingCounter mMobileRadioActiveUnknownTime;
    int mMobileRadioPowerState;
    int mModStepMode;
    ControllerActivityCounterImpl mModemActivity;
    @GuardedBy({"mModemNetworkLock"})
    private String[] mModemIfaces;
    private final Object mModemNetworkLock;
    final LongSamplingCounter[] mNetworkByteActivityCounters;
    final LongSamplingCounter[] mNetworkPacketActivityCounters;
    private final Pool<NetworkStats> mNetworkStatsPool;
    int mNextHistoryTagIdx;
    long mNextMaxDailyDeadline;
    long mNextMinDailyDeadline;
    boolean mNoAutoReset;
    @GuardedBy({"this"})
    private int mNumAllUidCpuTimeReads;
    @GuardedBy({"this"})
    private long mNumBatchedSingleUidCpuTimeReads;
    private int mNumConnectivityChange;
    int mNumHistoryItems;
    int mNumHistoryTagChars;
    @GuardedBy({"this"})
    private long mNumSingleUidCpuTimeReads;
    @GuardedBy({"this"})
    private int mNumUidsRemoved;
    boolean mOnBattery;
    @VisibleForTesting
    protected boolean mOnBatteryInternal;
    protected final TimeBase mOnBatteryScreenOffTimeBase;
    protected final TimeBase mOnBatteryTimeBase;
    @VisibleForTesting
    @UnsupportedAppUsage
    protected ArrayList<StopwatchTimer> mPartialTimers;
    @GuardedBy({"this"})
    @VisibleForTesting(visibility = Visibility.PACKAGE)
    protected Queue<UidToRemove> mPendingRemovedUids;
    @GuardedBy({"this"})
    @VisibleForTesting
    protected final SparseIntArray mPendingUids;
    @GuardedBy({"this"})
    public boolean mPerProcStateCpuTimesAvailable;
    int mPhoneDataConnectionType;
    final StopwatchTimer[] mPhoneDataConnectionsTimer;
    boolean mPhoneOn;
    StopwatchTimer mPhoneOnTimer;
    private int mPhoneServiceState;
    private int mPhoneServiceStateRaw;
    StopwatchTimer mPhoneSignalScanningTimer;
    int mPhoneSignalStrengthBin;
    int mPhoneSignalStrengthBinRaw;
    final StopwatchTimer[] mPhoneSignalStrengthsTimer;
    private int mPhoneSimStateRaw;
    private final PlatformIdleStateCallback mPlatformIdleStateCallback;
    @VisibleForTesting
    protected PowerProfile mPowerProfile;
    boolean mPowerSaveModeEnabled;
    StopwatchTimer mPowerSaveModeEnabledTimer;
    boolean mPretendScreenOff;
    public final RailEnergyDataCallback mRailEnergyDataCallback;
    int mReadHistoryChars;
    final HistoryStepDetails mReadHistoryStepDetails;
    String[] mReadHistoryStrings;
    int[] mReadHistoryUids;
    private boolean mReadOverflow;
    long mRealtime;
    long mRealtimeStart;
    public boolean mRecordAllHistory;
    protected boolean mRecordingHistory;
    private final HashMap<String, SamplingTimer> mRpmStats;
    int mScreenBrightnessBin;
    final StopwatchTimer[] mScreenBrightnessTimer;
    @VisibleForTesting(visibility = Visibility.PACKAGE)
    protected StopwatchTimer mScreenDozeTimer;
    private final HashMap<String, SamplingTimer> mScreenOffRpmStats;
    @VisibleForTesting(visibility = Visibility.PACKAGE)
    protected StopwatchTimer mScreenOnTimer;
    @VisibleForTesting(visibility = Visibility.PACKAGE)
    protected int mScreenState;
    int mSensorNesting;
    final SparseArray<ArrayList<StopwatchTimer>> mSensorTimers;
    boolean mShuttingDown;
    long mStartClockTime;
    int mStartCount;
    String mStartPlatformVersion;
    private final AtomicFile mStatsFile;
    long mTempTotalCpuSystemTimeUs;
    long mTempTotalCpuUserTimeUs;
    final HistoryStepDetails mTmpHistoryStepDetails;
    private final RailStats mTmpRailStats;
    private final RpmStats mTmpRpmStats;
    private final KernelWakelockStats mTmpWakelockStats;
    long mTrackRunningHistoryElapsedRealtime;
    long mTrackRunningHistoryUptime;
    final SparseArray<Uid> mUidStats;
    long mUptime;
    long mUptimeStart;
    int mUsbDataState;
    @VisibleForTesting
    protected UserInfoProvider mUserInfoProvider;
    int mVideoOnNesting;
    StopwatchTimer mVideoOnTimer;
    final ArrayList<StopwatchTimer> mVideoTurnedOnTimers;
    long[][] mWakeLockAllocationsUs;
    boolean mWakeLockImportant;
    int mWakeLockNesting;
    private final HashMap<String, SamplingTimer> mWakeupReasonStats;
    StopwatchTimer mWifiActiveTimer;
    ControllerActivityCounterImpl mWifiActivity;
    final SparseArray<ArrayList<StopwatchTimer>> mWifiBatchedScanTimers;
    int mWifiFullLockNesting;
    @GuardedBy({"mWifiNetworkLock"})
    private String[] mWifiIfaces;
    int mWifiMulticastNesting;
    final ArrayList<StopwatchTimer> mWifiMulticastTimers;
    StopwatchTimer mWifiMulticastWakelockTimer;
    private final Object mWifiNetworkLock;
    boolean mWifiOn;
    StopwatchTimer mWifiOnTimer;
    int mWifiRadioPowerState;
    final ArrayList<StopwatchTimer> mWifiRunningTimers;
    int mWifiScanNesting;
    final ArrayList<StopwatchTimer> mWifiScanTimers;
    int mWifiSignalStrengthBin;
    final StopwatchTimer[] mWifiSignalStrengthsTimer;
    int mWifiState;
    final StopwatchTimer[] mWifiStateTimer;
    int mWifiSupplState;
    final StopwatchTimer[] mWifiSupplStateTimer;
    @UnsupportedAppUsage
    final ArrayList<StopwatchTimer> mWindowTimers;
    final ReentrantLock mWriteLock;

    public interface TimeBaseObs {
        void detach();

        void onTimeStarted(long j, long j2, long j3);

        void onTimeStopped(long j, long j2, long j3);

        boolean reset(boolean z);
    }

    public static abstract class Timer extends android.os.BatteryStats.Timer implements TimeBaseObs {
        protected final Clocks mClocks;
        protected int mCount;
        protected final TimeBase mTimeBase;
        protected long mTimeBeforeMark;
        protected long mTotalTime;
        protected final int mType;

        public abstract int computeCurrentCountLocked();

        public abstract long computeRunTimeLocked(long j);

        public Timer(Clocks clocks, int type, TimeBase timeBase, Parcel in) {
            this.mClocks = clocks;
            this.mType = type;
            this.mTimeBase = timeBase;
            this.mCount = in.readInt();
            this.mTotalTime = in.readLong();
            this.mTimeBeforeMark = in.readLong();
            timeBase.add(this);
        }

        public Timer(Clocks clocks, int type, TimeBase timeBase) {
            this.mClocks = clocks;
            this.mType = type;
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            out.writeInt(computeCurrentCountLocked());
            out.writeLong(computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs)));
            out.writeLong(this.mTimeBeforeMark);
        }

        public boolean reset(boolean detachIfReset) {
            this.mTimeBeforeMark = 0;
            this.mTotalTime = 0;
            this.mCount = 0;
            if (detachIfReset) {
                detach();
            }
            return true;
        }

        public void detach() {
            this.mTimeBase.remove(this);
        }

        public void onTimeStarted(long elapsedRealtime, long timeBaseUptime, long baseRealtime) {
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            this.mTotalTime = computeRunTimeLocked(baseRealtime);
            this.mCount = computeCurrentCountLocked();
        }

        @UnsupportedAppUsage
        public static void writeTimerToParcel(Parcel out, Timer timer, long elapsedRealtimeUs) {
            if (timer == null) {
                out.writeInt(0);
                return;
            }
            out.writeInt(1);
            timer.writeToParcel(out, elapsedRealtimeUs);
        }

        @UnsupportedAppUsage
        public long getTotalTimeLocked(long elapsedRealtimeUs, int which) {
            return computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs));
        }

        @UnsupportedAppUsage
        public int getCountLocked(int which) {
            return computeCurrentCountLocked();
        }

        public long getTimeSinceMarkLocked(long elapsedRealtimeUs) {
            return computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs)) - this.mTimeBeforeMark;
        }

        public void logState(Printer pw, String prefix) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("mCount=");
            stringBuilder.append(this.mCount);
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("mTotalTime=");
            stringBuilder.append(this.mTotalTime);
            pw.println(stringBuilder.toString());
        }

        public void writeSummaryFromParcelLocked(Parcel out, long elapsedRealtimeUs) {
            out.writeLong(computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs)));
            out.writeInt(computeCurrentCountLocked());
        }

        public void readSummaryFromParcelLocked(Parcel in) {
            this.mTotalTime = in.readLong();
            this.mCount = in.readInt();
            this.mTimeBeforeMark = this.mTotalTime;
        }
    }

    public static class BatchTimer extends Timer {
        boolean mInDischarge;
        long mLastAddedDuration;
        long mLastAddedTime;
        final Uid mUid;

        BatchTimer(Clocks clocks, Uid uid, int type, TimeBase timeBase, Parcel in) {
            super(clocks, type, timeBase, in);
            this.mUid = uid;
            this.mLastAddedTime = in.readLong();
            this.mLastAddedDuration = in.readLong();
            this.mInDischarge = timeBase.isRunning();
        }

        BatchTimer(Clocks clocks, Uid uid, int type, TimeBase timeBase) {
            super(clocks, type, timeBase);
            this.mUid = uid;
            this.mInDischarge = timeBase.isRunning();
        }

        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeLong(this.mLastAddedTime);
            out.writeLong(this.mLastAddedDuration);
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            recomputeLastDuration(this.mClocks.elapsedRealtime() * 1000, false);
            this.mInDischarge = false;
            super.onTimeStopped(elapsedRealtime, baseUptime, baseRealtime);
        }

        public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
            recomputeLastDuration(elapsedRealtime, false);
            this.mInDischarge = true;
            if (this.mLastAddedTime == elapsedRealtime) {
                this.mTotalTime += this.mLastAddedDuration;
            }
            super.onTimeStarted(elapsedRealtime, baseUptime, baseRealtime);
        }

        public void logState(Printer pw, String prefix) {
            super.logState(pw, prefix);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("mLastAddedTime=");
            stringBuilder.append(this.mLastAddedTime);
            stringBuilder.append(" mLastAddedDuration=");
            stringBuilder.append(this.mLastAddedDuration);
            pw.println(stringBuilder.toString());
        }

        private long computeOverage(long curTime) {
            if (this.mLastAddedTime > 0) {
                return this.mLastAddedDuration - curTime;
            }
            return 0;
        }

        private void recomputeLastDuration(long curTime, boolean abort) {
            long overage = computeOverage(curTime);
            if (overage > 0) {
                if (this.mInDischarge) {
                    this.mTotalTime -= overage;
                }
                if (abort) {
                    this.mLastAddedTime = 0;
                    return;
                }
                this.mLastAddedTime = curTime;
                this.mLastAddedDuration -= overage;
            }
        }

        public void addDuration(BatteryStatsImpl stats, long durationMillis) {
            long now = this.mClocks.elapsedRealtime() * 1000;
            recomputeLastDuration(now, true);
            this.mLastAddedTime = now;
            this.mLastAddedDuration = 1000 * durationMillis;
            if (this.mInDischarge) {
                this.mTotalTime += this.mLastAddedDuration;
                this.mCount++;
            }
        }

        public void abortLastDuration(BatteryStatsImpl stats) {
            recomputeLastDuration(this.mClocks.elapsedRealtime() * 1000, true);
        }

        /* Access modifiers changed, original: protected */
        public int computeCurrentCountLocked() {
            return this.mCount;
        }

        /* Access modifiers changed, original: protected */
        public long computeRunTimeLocked(long curBatteryRealtime) {
            long overage = computeOverage(this.mClocks.elapsedRealtime() * 1000);
            if (overage <= 0) {
                return this.mTotalTime;
            }
            this.mTotalTime = overage;
            return overage;
        }

        public boolean reset(boolean detachIfReset) {
            long now = this.mClocks.elapsedRealtime() * 1000;
            recomputeLastDuration(now, true);
            boolean stillActive = this.mLastAddedTime == now;
            boolean z = !stillActive && detachIfReset;
            super.reset(z);
            if (stillActive) {
                return false;
            }
            return true;
        }
    }

    public interface BatteryCallback {
        void batteryNeedsCpuUpdate();

        void batteryPowerChanged(boolean z);

        void batterySendBroadcast(Intent intent);

        void batteryStatsReset();
    }

    private final class BluetoothActivityInfoCache {
        long energy;
        long idleTimeMs;
        long rxTimeMs;
        long txTimeMs;
        SparseLongArray uidRxBytes;
        SparseLongArray uidTxBytes;

        private BluetoothActivityInfoCache() {
            this.uidRxBytes = new SparseLongArray();
            this.uidTxBytes = new SparseLongArray();
        }

        /* synthetic */ BluetoothActivityInfoCache(BatteryStatsImpl x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: 0000 */
        public void set(BluetoothActivityEnergyInfo info) {
            this.idleTimeMs = info.getControllerIdleTimeMillis();
            this.rxTimeMs = info.getControllerRxTimeMillis();
            this.txTimeMs = info.getControllerTxTimeMillis();
            this.energy = info.getControllerEnergyUsed();
            if (info.getUidTraffic() != null) {
                for (UidTraffic traffic : info.getUidTraffic()) {
                    this.uidRxBytes.put(traffic.getUid(), traffic.getRxBytes());
                    this.uidTxBytes.put(traffic.getUid(), traffic.getTxBytes());
                }
            }
        }
    }

    public interface Clocks {
        long elapsedRealtime();

        long uptimeMillis();
    }

    @VisibleForTesting
    public final class Constants extends ContentObserver {
        private static final int DEFAULT_BATTERY_CHARGED_DELAY_MS = 900000;
        private static final long DEFAULT_BATTERY_LEVEL_COLLECTION_DELAY_MS = 300000;
        private static final long DEFAULT_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = 600000;
        private static final long DEFAULT_KERNEL_UID_READERS_THROTTLE_TIME = 1000;
        private static final int DEFAULT_MAX_HISTORY_BUFFER_KB = 128;
        private static final int DEFAULT_MAX_HISTORY_BUFFER_LOW_RAM_DEVICE_KB = 64;
        private static final int DEFAULT_MAX_HISTORY_FILES = 32;
        private static final int DEFAULT_MAX_HISTORY_FILES_LOW_RAM_DEVICE = 64;
        private static final long DEFAULT_PROC_STATE_CPU_TIMES_READ_DELAY_MS = 5000;
        private static final boolean DEFAULT_TRACK_CPU_ACTIVE_CLUSTER_TIME = true;
        private static final boolean DEFAULT_TRACK_CPU_TIMES_BY_PROC_STATE = false;
        private static final long DEFAULT_UID_REMOVE_DELAY_MS = 300000;
        public static final String KEY_BATTERY_CHARGED_DELAY_MS = "battery_charged_delay_ms";
        public static final String KEY_BATTERY_LEVEL_COLLECTION_DELAY_MS = "battery_level_collection_delay_ms";
        public static final String KEY_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = "external_stats_collection_rate_limit_ms";
        public static final String KEY_KERNEL_UID_READERS_THROTTLE_TIME = "kernel_uid_readers_throttle_time";
        public static final String KEY_MAX_HISTORY_BUFFER_KB = "max_history_buffer_kb";
        public static final String KEY_MAX_HISTORY_FILES = "max_history_files";
        public static final String KEY_PROC_STATE_CPU_TIMES_READ_DELAY_MS = "proc_state_cpu_times_read_delay_ms";
        public static final String KEY_TRACK_CPU_ACTIVE_CLUSTER_TIME = "track_cpu_active_cluster_time";
        public static final String KEY_TRACK_CPU_TIMES_BY_PROC_STATE = "track_cpu_times_by_proc_state";
        public static final String KEY_UID_REMOVE_DELAY_MS = "uid_remove_delay_ms";
        public int BATTERY_CHARGED_DELAY_MS = DEFAULT_BATTERY_CHARGED_DELAY_MS;
        public long BATTERY_LEVEL_COLLECTION_DELAY_MS = 300000;
        public long EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = DEFAULT_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS;
        public long KERNEL_UID_READERS_THROTTLE_TIME;
        public int MAX_HISTORY_BUFFER;
        public int MAX_HISTORY_FILES;
        public long PROC_STATE_CPU_TIMES_READ_DELAY_MS = 5000;
        public boolean TRACK_CPU_ACTIVE_CLUSTER_TIME = true;
        public boolean TRACK_CPU_TIMES_BY_PROC_STATE = false;
        public long UID_REMOVE_DELAY_MS = 300000;
        private final KeyValueListParser mParser = new KeyValueListParser(',');
        private ContentResolver mResolver;

        public Constants(Handler handler) {
            super(handler);
            if (ActivityManager.isLowRamDeviceStatic()) {
                this.MAX_HISTORY_FILES = 64;
                this.MAX_HISTORY_BUFFER = 65536;
                return;
            }
            this.MAX_HISTORY_FILES = 32;
            this.MAX_HISTORY_BUFFER = 131072;
        }

        public void startObserving(ContentResolver resolver) {
            this.mResolver = resolver;
            this.mResolver.registerContentObserver(Global.getUriFor(Global.BATTERY_STATS_CONSTANTS), false, this);
            this.mResolver.registerContentObserver(Global.getUriFor(Global.BATTERY_CHARGING_STATE_UPDATE_DELAY), false, this);
            updateConstants();
        }

        public void onChange(boolean selfChange, Uri uri) {
            if (uri.equals(Global.getUriFor(Global.BATTERY_CHARGING_STATE_UPDATE_DELAY))) {
                synchronized (BatteryStatsImpl.this) {
                    updateBatteryChargedDelayMsLocked();
                }
                return;
            }
            updateConstants();
        }

        private void updateConstants() {
            synchronized (BatteryStatsImpl.this) {
                int i;
                try {
                    this.mParser.setString(Global.getString(this.mResolver, Global.BATTERY_STATS_CONSTANTS));
                } catch (IllegalArgumentException e) {
                    Slog.e(BatteryStatsImpl.TAG, "Bad batterystats settings", e);
                }
                updateTrackCpuTimesByProcStateLocked(this.TRACK_CPU_TIMES_BY_PROC_STATE, this.mParser.getBoolean(KEY_TRACK_CPU_TIMES_BY_PROC_STATE, false));
                this.TRACK_CPU_ACTIVE_CLUSTER_TIME = this.mParser.getBoolean(KEY_TRACK_CPU_ACTIVE_CLUSTER_TIME, true);
                updateProcStateCpuTimesReadDelayMs(this.PROC_STATE_CPU_TIMES_READ_DELAY_MS, this.mParser.getLong(KEY_PROC_STATE_CPU_TIMES_READ_DELAY_MS, 5000));
                updateKernelUidReadersThrottleTime(this.KERNEL_UID_READERS_THROTTLE_TIME, this.mParser.getLong(KEY_KERNEL_UID_READERS_THROTTLE_TIME, 1000));
                updateUidRemoveDelay(this.mParser.getLong(KEY_UID_REMOVE_DELAY_MS, 300000));
                this.EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = this.mParser.getLong(KEY_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS, DEFAULT_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS);
                this.BATTERY_LEVEL_COLLECTION_DELAY_MS = this.mParser.getLong(KEY_BATTERY_LEVEL_COLLECTION_DELAY_MS, 300000);
                KeyValueListParser keyValueListParser = this.mParser;
                String str = KEY_MAX_HISTORY_FILES;
                int i2 = 64;
                if (ActivityManager.isLowRamDeviceStatic()) {
                    i = 64;
                } else {
                    i = 32;
                }
                this.MAX_HISTORY_FILES = keyValueListParser.getInt(str, i);
                keyValueListParser = this.mParser;
                str = KEY_MAX_HISTORY_BUFFER_KB;
                if (!ActivityManager.isLowRamDeviceStatic()) {
                    i2 = 128;
                }
                this.MAX_HISTORY_BUFFER = keyValueListParser.getInt(str, i2) * 1024;
                updateBatteryChargedDelayMsLocked();
            }
        }

        private void updateBatteryChargedDelayMsLocked() {
            int delay = Global.getInt(this.mResolver, Global.BATTERY_CHARGING_STATE_UPDATE_DELAY, -1);
            this.BATTERY_CHARGED_DELAY_MS = delay >= 0 ? delay : this.mParser.getInt(KEY_BATTERY_CHARGED_DELAY_MS, DEFAULT_BATTERY_CHARGED_DELAY_MS);
        }

        private void updateTrackCpuTimesByProcStateLocked(boolean wasEnabled, boolean isEnabled) {
            this.TRACK_CPU_TIMES_BY_PROC_STATE = isEnabled;
            if (isEnabled && !wasEnabled) {
                BatteryStatsImpl.this.mIsPerProcessStateCpuDataStale = true;
                BatteryStatsImpl.this.mExternalSync.scheduleCpuSyncDueToSettingChange();
                BatteryStatsImpl.this.mNumSingleUidCpuTimeReads = 0;
                BatteryStatsImpl.this.mNumBatchedSingleUidCpuTimeReads = 0;
                BatteryStatsImpl batteryStatsImpl = BatteryStatsImpl.this;
                batteryStatsImpl.mCpuTimeReadsTrackingStartTime = batteryStatsImpl.mClocks.uptimeMillis();
            }
        }

        private void updateProcStateCpuTimesReadDelayMs(long oldDelayMillis, long newDelayMillis) {
            this.PROC_STATE_CPU_TIMES_READ_DELAY_MS = newDelayMillis;
            if (oldDelayMillis != newDelayMillis) {
                BatteryStatsImpl.this.mNumSingleUidCpuTimeReads = 0;
                BatteryStatsImpl.this.mNumBatchedSingleUidCpuTimeReads = 0;
                BatteryStatsImpl batteryStatsImpl = BatteryStatsImpl.this;
                batteryStatsImpl.mCpuTimeReadsTrackingStartTime = batteryStatsImpl.mClocks.uptimeMillis();
            }
        }

        private void updateKernelUidReadersThrottleTime(long oldTimeMs, long newTimeMs) {
            this.KERNEL_UID_READERS_THROTTLE_TIME = newTimeMs;
            if (oldTimeMs != newTimeMs) {
                BatteryStatsImpl.this.mCpuUidUserSysTimeReader.setThrottle(this.KERNEL_UID_READERS_THROTTLE_TIME);
                BatteryStatsImpl.this.mCpuUidFreqTimeReader.setThrottle(this.KERNEL_UID_READERS_THROTTLE_TIME);
                BatteryStatsImpl.this.mCpuUidActiveTimeReader.setThrottle(this.KERNEL_UID_READERS_THROTTLE_TIME);
                BatteryStatsImpl.this.mCpuUidClusterTimeReader.setThrottle(this.KERNEL_UID_READERS_THROTTLE_TIME);
            }
        }

        private void updateUidRemoveDelay(long newTimeMs) {
            this.UID_REMOVE_DELAY_MS = newTimeMs;
            BatteryStatsImpl.this.clearPendingRemovedUids();
        }

        public void dumpLocked(PrintWriter pw) {
            pw.print(KEY_TRACK_CPU_TIMES_BY_PROC_STATE);
            String str = "=";
            pw.print(str);
            pw.println(this.TRACK_CPU_TIMES_BY_PROC_STATE);
            pw.print(KEY_TRACK_CPU_ACTIVE_CLUSTER_TIME);
            pw.print(str);
            pw.println(this.TRACK_CPU_ACTIVE_CLUSTER_TIME);
            pw.print(KEY_PROC_STATE_CPU_TIMES_READ_DELAY_MS);
            pw.print(str);
            pw.println(this.PROC_STATE_CPU_TIMES_READ_DELAY_MS);
            pw.print(KEY_KERNEL_UID_READERS_THROTTLE_TIME);
            pw.print(str);
            pw.println(this.KERNEL_UID_READERS_THROTTLE_TIME);
            pw.print(KEY_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS);
            pw.print(str);
            pw.println(this.EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS);
            pw.print(KEY_BATTERY_LEVEL_COLLECTION_DELAY_MS);
            pw.print(str);
            pw.println(this.BATTERY_LEVEL_COLLECTION_DELAY_MS);
            pw.print(KEY_MAX_HISTORY_FILES);
            pw.print(str);
            pw.println(this.MAX_HISTORY_FILES);
            pw.print(KEY_MAX_HISTORY_BUFFER_KB);
            pw.print(str);
            pw.println(this.MAX_HISTORY_BUFFER / 1024);
            pw.print(KEY_BATTERY_CHARGED_DELAY_MS);
            pw.print(str);
            pw.println(this.BATTERY_CHARGED_DELAY_MS);
        }
    }

    public static class ControllerActivityCounterImpl extends ControllerActivityCounter implements Parcelable {
        private final LongSamplingCounter mIdleTimeMillis;
        private final LongSamplingCounter mMonitoredRailChargeConsumedMaMs;
        private final LongSamplingCounter mPowerDrainMaMs;
        private final LongSamplingCounter mRxTimeMillis;
        private final LongSamplingCounter mScanTimeMillis;
        private final LongSamplingCounter mSleepTimeMillis;
        private final LongSamplingCounter[] mTxTimeMillis;

        public ControllerActivityCounterImpl(TimeBase timeBase, int numTxStates) {
            this.mIdleTimeMillis = new LongSamplingCounter(timeBase);
            this.mScanTimeMillis = new LongSamplingCounter(timeBase);
            this.mSleepTimeMillis = new LongSamplingCounter(timeBase);
            this.mRxTimeMillis = new LongSamplingCounter(timeBase);
            this.mTxTimeMillis = new LongSamplingCounter[numTxStates];
            for (int i = 0; i < numTxStates; i++) {
                this.mTxTimeMillis[i] = new LongSamplingCounter(timeBase);
            }
            this.mPowerDrainMaMs = new LongSamplingCounter(timeBase);
            this.mMonitoredRailChargeConsumedMaMs = new LongSamplingCounter(timeBase);
        }

        public ControllerActivityCounterImpl(TimeBase timeBase, int numTxStates, Parcel in) {
            this.mIdleTimeMillis = new LongSamplingCounter(timeBase, in);
            this.mScanTimeMillis = new LongSamplingCounter(timeBase, in);
            this.mSleepTimeMillis = new LongSamplingCounter(timeBase, in);
            this.mRxTimeMillis = new LongSamplingCounter(timeBase, in);
            if (in.readInt() == numTxStates) {
                this.mTxTimeMillis = new LongSamplingCounter[numTxStates];
                for (int i = 0; i < numTxStates; i++) {
                    this.mTxTimeMillis[i] = new LongSamplingCounter(timeBase, in);
                }
                this.mPowerDrainMaMs = new LongSamplingCounter(timeBase, in);
                this.mMonitoredRailChargeConsumedMaMs = new LongSamplingCounter(timeBase, in);
                return;
            }
            throw new ParcelFormatException("inconsistent tx state lengths");
        }

        public void readSummaryFromParcel(Parcel in) {
            this.mIdleTimeMillis.readSummaryFromParcelLocked(in);
            this.mScanTimeMillis.readSummaryFromParcelLocked(in);
            this.mSleepTimeMillis.readSummaryFromParcelLocked(in);
            this.mRxTimeMillis.readSummaryFromParcelLocked(in);
            int recordedTxStates = in.readInt();
            LongSamplingCounter[] longSamplingCounterArr = this.mTxTimeMillis;
            if (recordedTxStates == longSamplingCounterArr.length) {
                for (LongSamplingCounter counter : longSamplingCounterArr) {
                    counter.readSummaryFromParcelLocked(in);
                }
                this.mPowerDrainMaMs.readSummaryFromParcelLocked(in);
                this.mMonitoredRailChargeConsumedMaMs.readSummaryFromParcelLocked(in);
                return;
            }
            throw new ParcelFormatException("inconsistent tx state lengths");
        }

        public int describeContents() {
            return 0;
        }

        public void writeSummaryToParcel(Parcel dest) {
            this.mIdleTimeMillis.writeSummaryFromParcelLocked(dest);
            this.mScanTimeMillis.writeSummaryFromParcelLocked(dest);
            this.mSleepTimeMillis.writeSummaryFromParcelLocked(dest);
            this.mRxTimeMillis.writeSummaryFromParcelLocked(dest);
            dest.writeInt(this.mTxTimeMillis.length);
            for (LongSamplingCounter counter : this.mTxTimeMillis) {
                counter.writeSummaryFromParcelLocked(dest);
            }
            this.mPowerDrainMaMs.writeSummaryFromParcelLocked(dest);
            this.mMonitoredRailChargeConsumedMaMs.writeSummaryFromParcelLocked(dest);
        }

        public void writeToParcel(Parcel dest, int flags) {
            this.mIdleTimeMillis.writeToParcel(dest);
            this.mScanTimeMillis.writeToParcel(dest);
            this.mSleepTimeMillis.writeToParcel(dest);
            this.mRxTimeMillis.writeToParcel(dest);
            dest.writeInt(this.mTxTimeMillis.length);
            for (LongSamplingCounter counter : this.mTxTimeMillis) {
                counter.writeToParcel(dest);
            }
            this.mPowerDrainMaMs.writeToParcel(dest);
            this.mMonitoredRailChargeConsumedMaMs.writeToParcel(dest);
        }

        public void reset(boolean detachIfReset) {
            this.mIdleTimeMillis.reset(detachIfReset);
            this.mScanTimeMillis.reset(detachIfReset);
            this.mSleepTimeMillis.reset(detachIfReset);
            this.mRxTimeMillis.reset(detachIfReset);
            for (LongSamplingCounter counter : this.mTxTimeMillis) {
                counter.reset(detachIfReset);
            }
            this.mPowerDrainMaMs.reset(detachIfReset);
            this.mMonitoredRailChargeConsumedMaMs.reset(detachIfReset);
        }

        public void detach() {
            this.mIdleTimeMillis.detach();
            this.mScanTimeMillis.detach();
            this.mSleepTimeMillis.detach();
            this.mRxTimeMillis.detach();
            for (LongSamplingCounter counter : this.mTxTimeMillis) {
                counter.detach();
            }
            this.mPowerDrainMaMs.detach();
            this.mMonitoredRailChargeConsumedMaMs.detach();
        }

        public LongSamplingCounter getIdleTimeCounter() {
            return this.mIdleTimeMillis;
        }

        public LongSamplingCounter getScanTimeCounter() {
            return this.mScanTimeMillis;
        }

        public LongSamplingCounter getSleepTimeCounter() {
            return this.mSleepTimeMillis;
        }

        public LongSamplingCounter getRxTimeCounter() {
            return this.mRxTimeMillis;
        }

        public LongSamplingCounter[] getTxTimeCounters() {
            return this.mTxTimeMillis;
        }

        public LongSamplingCounter getPowerCounter() {
            return this.mPowerDrainMaMs;
        }

        public LongSamplingCounter getMonitoredRailChargeConsumedMaMs() {
            return this.mMonitoredRailChargeConsumedMaMs;
        }
    }

    public static class Counter extends android.os.BatteryStats.Counter implements TimeBaseObs {
        @UnsupportedAppUsage
        final AtomicInteger mCount = new AtomicInteger();
        final TimeBase mTimeBase;

        public Counter(TimeBase timeBase, Parcel in) {
            this.mTimeBase = timeBase;
            this.mCount.set(in.readInt());
            timeBase.add(this);
        }

        public Counter(TimeBase timeBase) {
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        public void writeToParcel(Parcel out) {
            out.writeInt(this.mCount.get());
        }

        public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
        }

        public static void writeCounterToParcel(Parcel out, Counter counter) {
            if (counter == null) {
                out.writeInt(0);
                return;
            }
            out.writeInt(1);
            counter.writeToParcel(out);
        }

        public static Counter readCounterFromParcel(TimeBase timeBase, Parcel in) {
            if (in.readInt() == 0) {
                return null;
            }
            return new Counter(timeBase, in);
        }

        public int getCountLocked(int which) {
            return this.mCount.get();
        }

        public void logState(Printer pw, String prefix) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("mCount=");
            stringBuilder.append(this.mCount.get());
            pw.println(stringBuilder.toString());
        }

        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public void stepAtomic() {
            if (this.mTimeBase.isRunning()) {
                this.mCount.incrementAndGet();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void addAtomic(int delta) {
            if (this.mTimeBase.isRunning()) {
                this.mCount.addAndGet(delta);
            }
        }

        public boolean reset(boolean detachIfReset) {
            this.mCount.set(0);
            if (detachIfReset) {
                detach();
            }
            return true;
        }

        public void detach() {
            this.mTimeBase.remove(this);
        }

        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public void writeSummaryFromParcelLocked(Parcel out) {
            out.writeInt(this.mCount.get());
        }

        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public void readSummaryFromParcelLocked(Parcel in) {
            this.mCount.set(in.readInt());
        }
    }

    public static class StopwatchTimer extends Timer {
        long mAcquireTime = -1;
        @VisibleForTesting
        public boolean mInList;
        int mNesting;
        long mTimeout;
        final ArrayList<StopwatchTimer> mTimerPool;
        final Uid mUid;
        long mUpdateTime;

        public StopwatchTimer(Clocks clocks, Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase, Parcel in) {
            super(clocks, type, timeBase, in);
            this.mUid = uid;
            this.mTimerPool = timerPool;
            this.mUpdateTime = in.readLong();
        }

        public StopwatchTimer(Clocks clocks, Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase) {
            super(clocks, type, timeBase);
            this.mUid = uid;
            this.mTimerPool = timerPool;
        }

        public void setTimeout(long timeout) {
            this.mTimeout = timeout;
        }

        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeLong(this.mUpdateTime);
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            if (this.mNesting > 0) {
                super.onTimeStopped(elapsedRealtime, baseUptime, baseRealtime);
                this.mUpdateTime = baseRealtime;
            }
        }

        public void logState(Printer pw, String prefix) {
            super.logState(pw, prefix);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("mNesting=");
            stringBuilder.append(this.mNesting);
            stringBuilder.append(" mUpdateTime=");
            stringBuilder.append(this.mUpdateTime);
            stringBuilder.append(" mAcquireTime=");
            stringBuilder.append(this.mAcquireTime);
            pw.println(stringBuilder.toString());
        }

        public void startRunningLocked(long elapsedRealtimeMs) {
            int i = this.mNesting;
            this.mNesting = i + 1;
            if (i == 0) {
                long batteryRealtime = this.mTimeBase.getRealtime(1000 * elapsedRealtimeMs);
                this.mUpdateTime = batteryRealtime;
                ArrayList arrayList = this.mTimerPool;
                if (arrayList != null) {
                    refreshTimersLocked(batteryRealtime, arrayList, null);
                    this.mTimerPool.add(this);
                }
                if (this.mTimeBase.isRunning()) {
                    this.mCount++;
                    this.mAcquireTime = this.mTotalTime;
                    return;
                }
                this.mAcquireTime = -1;
            }
        }

        public boolean isRunningLocked() {
            return this.mNesting > 0;
        }

        public void stopRunningLocked(long elapsedRealtimeMs) {
            int i = this.mNesting;
            if (i != 0) {
                i--;
                this.mNesting = i;
                if (i == 0) {
                    long batteryRealtime = this.mTimeBase.getRealtime(1000 * elapsedRealtimeMs);
                    ArrayList arrayList = this.mTimerPool;
                    if (arrayList != null) {
                        refreshTimersLocked(batteryRealtime, arrayList, null);
                        this.mTimerPool.remove(this);
                    } else {
                        this.mNesting = 1;
                        this.mTotalTime = computeRunTimeLocked(batteryRealtime);
                        this.mNesting = 0;
                    }
                    if (this.mAcquireTime >= 0 && this.mTotalTime == this.mAcquireTime) {
                        this.mCount--;
                    }
                }
            }
        }

        public void stopAllRunningLocked(long elapsedRealtimeMs) {
            if (this.mNesting > 0) {
                this.mNesting = 1;
                stopRunningLocked(elapsedRealtimeMs);
            }
        }

        private static long refreshTimersLocked(long batteryRealtime, ArrayList<StopwatchTimer> pool, StopwatchTimer self) {
            long selfTime = 0;
            int N = pool.size();
            for (int i = N - 1; i >= 0; i--) {
                StopwatchTimer t = (StopwatchTimer) pool.get(i);
                long heldTime = batteryRealtime - t.mUpdateTime;
                if (heldTime > 0) {
                    long myTime = heldTime / ((long) N);
                    if (t == self) {
                        selfTime = myTime;
                    }
                    t.mTotalTime += myTime;
                }
                t.mUpdateTime = batteryRealtime;
            }
            return selfTime;
        }

        /* Access modifiers changed, original: protected */
        public long computeRunTimeLocked(long curBatteryRealtime) {
            long j = this.mTimeout;
            long j2 = 0;
            if (j > 0) {
                long j3 = this.mUpdateTime;
                if (curBatteryRealtime > j3 + j) {
                    curBatteryRealtime = j3 + j;
                }
            }
            j = this.mTotalTime;
            if (this.mNesting > 0) {
                j2 = curBatteryRealtime - this.mUpdateTime;
                ArrayList arrayList = this.mTimerPool;
                j2 /= (long) (arrayList != null ? arrayList.size() : 1);
            }
            return j + j2;
        }

        /* Access modifiers changed, original: protected */
        public int computeCurrentCountLocked() {
            return this.mCount;
        }

        public boolean reset(boolean detachIfReset) {
            boolean z = true;
            boolean canDetach = this.mNesting <= 0;
            if (!(canDetach && detachIfReset)) {
                z = false;
            }
            super.reset(z);
            if (this.mNesting > 0) {
                this.mUpdateTime = this.mTimeBase.getRealtime(this.mClocks.elapsedRealtime() * 1000);
            }
            this.mAcquireTime = -1;
            return canDetach;
        }

        @UnsupportedAppUsage
        public void detach() {
            super.detach();
            ArrayList arrayList = this.mTimerPool;
            if (arrayList != null) {
                arrayList.remove(this);
            }
        }

        public void readSummaryFromParcelLocked(Parcel in) {
            super.readSummaryFromParcelLocked(in);
            this.mNesting = 0;
        }

        public void setMark(long elapsedRealtimeMs) {
            long batteryRealtime = this.mTimeBase.getRealtime(1000 * elapsedRealtimeMs);
            if (this.mNesting > 0) {
                ArrayList arrayList = this.mTimerPool;
                if (arrayList != null) {
                    refreshTimersLocked(batteryRealtime, arrayList, this);
                } else {
                    this.mTotalTime += batteryRealtime - this.mUpdateTime;
                    this.mUpdateTime = batteryRealtime;
                }
            }
            this.mTimeBeforeMark = this.mTotalTime;
        }
    }

    public static class DurationTimer extends StopwatchTimer {
        long mCurrentDurationMs;
        long mMaxDurationMs;
        long mStartTimeMs = -1;
        long mTotalDurationMs;

        public DurationTimer(Clocks clocks, Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase, Parcel in) {
            super(clocks, uid, type, timerPool, timeBase, in);
            this.mMaxDurationMs = in.readLong();
            this.mTotalDurationMs = in.readLong();
            this.mCurrentDurationMs = in.readLong();
        }

        public DurationTimer(Clocks clocks, Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase) {
            super(clocks, uid, type, timerPool, timeBase);
        }

        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeLong(getMaxDurationMsLocked(elapsedRealtimeUs / 1000));
            out.writeLong(this.mTotalDurationMs);
            out.writeLong(getCurrentDurationMsLocked(elapsedRealtimeUs / 1000));
        }

        public void writeSummaryFromParcelLocked(Parcel out, long elapsedRealtimeUs) {
            super.writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
            out.writeLong(getMaxDurationMsLocked(elapsedRealtimeUs / 1000));
            out.writeLong(getTotalDurationMsLocked(elapsedRealtimeUs / 1000));
        }

        public void readSummaryFromParcelLocked(Parcel in) {
            super.readSummaryFromParcelLocked(in);
            this.mMaxDurationMs = in.readLong();
            this.mTotalDurationMs = in.readLong();
            this.mStartTimeMs = -1;
            this.mCurrentDurationMs = 0;
        }

        public void onTimeStarted(long elapsedRealtimeUs, long baseUptime, long baseRealtime) {
            super.onTimeStarted(elapsedRealtimeUs, baseUptime, baseRealtime);
            if (this.mNesting > 0) {
                this.mStartTimeMs = baseRealtime / 1000;
            }
        }

        public void onTimeStopped(long elapsedRealtimeUs, long baseUptime, long baseRealtimeUs) {
            super.onTimeStopped(elapsedRealtimeUs, baseUptime, baseRealtimeUs);
            if (this.mNesting > 0) {
                this.mCurrentDurationMs += (baseRealtimeUs / 1000) - this.mStartTimeMs;
            }
            this.mStartTimeMs = -1;
        }

        public void logState(Printer pw, String prefix) {
            super.logState(pw, prefix);
        }

        public void startRunningLocked(long elapsedRealtimeMs) {
            super.startRunningLocked(elapsedRealtimeMs);
            if (this.mNesting == 1 && this.mTimeBase.isRunning()) {
                this.mStartTimeMs = this.mTimeBase.getRealtime(elapsedRealtimeMs * 1000) / 1000;
            }
        }

        public void stopRunningLocked(long elapsedRealtimeMs) {
            if (this.mNesting == 1) {
                long durationMs = getCurrentDurationMsLocked(elapsedRealtimeMs);
                this.mTotalDurationMs += durationMs;
                if (durationMs > this.mMaxDurationMs) {
                    this.mMaxDurationMs = durationMs;
                }
                this.mStartTimeMs = -1;
                this.mCurrentDurationMs = 0;
            }
            super.stopRunningLocked(elapsedRealtimeMs);
        }

        public boolean reset(boolean detachIfReset) {
            boolean result = super.reset(detachIfReset);
            this.mMaxDurationMs = 0;
            this.mTotalDurationMs = 0;
            this.mCurrentDurationMs = 0;
            if (this.mNesting > 0) {
                this.mStartTimeMs = this.mTimeBase.getRealtime(this.mClocks.elapsedRealtime() * 1000) / 1000;
            } else {
                this.mStartTimeMs = -1;
            }
            return result;
        }

        public long getMaxDurationMsLocked(long elapsedRealtimeMs) {
            if (this.mNesting > 0) {
                long durationMs = getCurrentDurationMsLocked(elapsedRealtimeMs);
                if (durationMs > this.mMaxDurationMs) {
                    return durationMs;
                }
            }
            return this.mMaxDurationMs;
        }

        public long getCurrentDurationMsLocked(long elapsedRealtimeMs) {
            long durationMs = this.mCurrentDurationMs;
            if (this.mNesting <= 0 || !this.mTimeBase.isRunning()) {
                return durationMs;
            }
            return durationMs + ((this.mTimeBase.getRealtime(elapsedRealtimeMs * 1000) / 1000) - this.mStartTimeMs);
        }

        public long getTotalDurationMsLocked(long elapsedRealtimeMs) {
            return this.mTotalDurationMs + getCurrentDurationMsLocked(elapsedRealtimeMs);
        }
    }

    public static class DualTimer extends DurationTimer {
        private final DurationTimer mSubTimer;

        public DualTimer(Clocks clocks, Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase, TimeBase subTimeBase, Parcel in) {
            super(clocks, uid, type, timerPool, timeBase, in);
            this.mSubTimer = new DurationTimer(clocks, uid, type, null, subTimeBase, in);
        }

        public DualTimer(Clocks clocks, Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase, TimeBase subTimeBase) {
            super(clocks, uid, type, timerPool, timeBase);
            this.mSubTimer = new DurationTimer(clocks, uid, type, null, subTimeBase);
        }

        public DurationTimer getSubTimer() {
            return this.mSubTimer;
        }

        public void startRunningLocked(long elapsedRealtimeMs) {
            super.startRunningLocked(elapsedRealtimeMs);
            this.mSubTimer.startRunningLocked(elapsedRealtimeMs);
        }

        public void stopRunningLocked(long elapsedRealtimeMs) {
            super.stopRunningLocked(elapsedRealtimeMs);
            this.mSubTimer.stopRunningLocked(elapsedRealtimeMs);
        }

        public void stopAllRunningLocked(long elapsedRealtimeMs) {
            super.stopAllRunningLocked(elapsedRealtimeMs);
            this.mSubTimer.stopAllRunningLocked(elapsedRealtimeMs);
        }

        public boolean reset(boolean detachIfReset) {
            if ((false | (this.mSubTimer.reset(false) ^ 1)) | (super.reset(detachIfReset) ^ 1)) {
                return false;
            }
            return true;
        }

        public void detach() {
            this.mSubTimer.detach();
            super.detach();
        }

        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            this.mSubTimer.writeToParcel(out, elapsedRealtimeUs);
        }

        public void writeSummaryFromParcelLocked(Parcel out, long elapsedRealtimeUs) {
            super.writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
            this.mSubTimer.writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
        }

        public void readSummaryFromParcelLocked(Parcel in) {
            super.readSummaryFromParcelLocked(in);
            this.mSubTimer.readSummaryFromParcelLocked(in);
        }
    }

    public interface ExternalStatsSync {
        public static final int UPDATE_ALL = 31;
        public static final int UPDATE_BT = 8;
        public static final int UPDATE_CPU = 1;
        public static final int UPDATE_RADIO = 4;
        public static final int UPDATE_RPM = 16;
        public static final int UPDATE_WIFI = 2;

        void cancelCpuSyncDueToWakelockChange();

        Future<?> scheduleCopyFromAllUidsCpuTimes(boolean z, boolean z2);

        Future<?> scheduleCpuSyncDueToRemovedUid(int i);

        Future<?> scheduleCpuSyncDueToScreenStateChange(boolean z, boolean z2);

        Future<?> scheduleCpuSyncDueToSettingChange();

        Future<?> scheduleCpuSyncDueToWakelockChange(long j);

        Future<?> scheduleReadProcStateCpuTimes(boolean z, boolean z2, long j);

        Future<?> scheduleSync(String str, int i);

        Future<?> scheduleSyncDueToBatteryLevelChange(long j);
    }

    @VisibleForTesting
    public static class LongSamplingCounter extends LongCounter implements TimeBaseObs {
        private long mCount;
        final TimeBase mTimeBase;

        public LongSamplingCounter(TimeBase timeBase, Parcel in) {
            this.mTimeBase = timeBase;
            this.mCount = in.readLong();
            timeBase.add(this);
        }

        public LongSamplingCounter(TimeBase timeBase) {
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        public void writeToParcel(Parcel out) {
            out.writeLong(this.mCount);
        }

        public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
        }

        public long getCountLocked(int which) {
            return this.mCount;
        }

        public void logState(Printer pw, String prefix) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("mCount=");
            stringBuilder.append(this.mCount);
            pw.println(stringBuilder.toString());
        }

        public void addCountLocked(long count) {
            addCountLocked(count, this.mTimeBase.isRunning());
        }

        public void addCountLocked(long count, boolean isRunning) {
            if (isRunning) {
                this.mCount += count;
            }
        }

        public boolean reset(boolean detachIfReset) {
            this.mCount = 0;
            if (detachIfReset) {
                detach();
            }
            return true;
        }

        public void detach() {
            this.mTimeBase.remove(this);
        }

        public void writeSummaryFromParcelLocked(Parcel out) {
            out.writeLong(this.mCount);
        }

        public void readSummaryFromParcelLocked(Parcel in) {
            this.mCount = in.readLong();
        }
    }

    @VisibleForTesting
    public static class LongSamplingCounterArray extends LongCounterArray implements TimeBaseObs {
        public long[] mCounts;
        final TimeBase mTimeBase;

        /* synthetic */ LongSamplingCounterArray(TimeBase x0, Parcel x1, AnonymousClass1 x2) {
            this(x0, x1);
        }

        private LongSamplingCounterArray(TimeBase timeBase, Parcel in) {
            this.mTimeBase = timeBase;
            this.mCounts = in.createLongArray();
            timeBase.add(this);
        }

        public LongSamplingCounterArray(TimeBase timeBase) {
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        private void writeToParcel(Parcel out) {
            out.writeLongArray(this.mCounts);
        }

        public void onTimeStarted(long elapsedRealTime, long baseUptime, long baseRealtime) {
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
        }

        public long[] getCountsLocked(int which) {
            long[] jArr = this.mCounts;
            return jArr == null ? null : Arrays.copyOf(jArr, jArr.length);
        }

        public void logState(Printer pw, String prefix) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("mCounts=");
            stringBuilder.append(Arrays.toString(this.mCounts));
            pw.println(stringBuilder.toString());
        }

        public void addCountLocked(long[] counts) {
            addCountLocked(counts, this.mTimeBase.isRunning());
        }

        public void addCountLocked(long[] counts, boolean isRunning) {
            if (counts != null && isRunning) {
                if (this.mCounts == null) {
                    this.mCounts = new long[counts.length];
                }
                for (int i = 0; i < counts.length; i++) {
                    long[] jArr = this.mCounts;
                    jArr[i] = jArr[i] + counts[i];
                }
            }
        }

        public int getSize() {
            long[] jArr = this.mCounts;
            return jArr == null ? 0 : jArr.length;
        }

        public boolean reset(boolean detachIfReset) {
            long[] jArr = this.mCounts;
            if (jArr != null) {
                Arrays.fill(jArr, 0);
            }
            if (detachIfReset) {
                detach();
            }
            return true;
        }

        public void detach() {
            this.mTimeBase.remove(this);
        }

        private void writeSummaryToParcelLocked(Parcel out) {
            out.writeLongArray(this.mCounts);
        }

        private void readSummaryFromParcelLocked(Parcel in) {
            this.mCounts = in.createLongArray();
        }

        public static void writeToParcel(Parcel out, LongSamplingCounterArray counterArray) {
            if (counterArray != null) {
                out.writeInt(1);
                counterArray.writeToParcel(out);
                return;
            }
            out.writeInt(0);
        }

        public static LongSamplingCounterArray readFromParcel(Parcel in, TimeBase timeBase) {
            if (in.readInt() != 0) {
                return new LongSamplingCounterArray(timeBase, in);
            }
            return null;
        }

        public static void writeSummaryToParcelLocked(Parcel out, LongSamplingCounterArray counterArray) {
            if (counterArray != null) {
                out.writeInt(1);
                counterArray.writeSummaryToParcelLocked(out);
                return;
            }
            out.writeInt(0);
        }

        public static LongSamplingCounterArray readSummaryFromParcelLocked(Parcel in, TimeBase timeBase) {
            if (in.readInt() == 0) {
                return null;
            }
            LongSamplingCounterArray counterArray = new LongSamplingCounterArray(timeBase);
            counterArray.readSummaryFromParcelLocked(in);
            return counterArray;
        }
    }

    final class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper, null, true);
        }

        public void handleMessage(Message msg) {
            BatteryCallback cb = BatteryStatsImpl.this.mCallback;
            int i = msg.what;
            boolean z = true;
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i == 4 && cb != null) {
                            cb.batteryStatsReset();
                        }
                    } else if (cb != null) {
                        String action;
                        synchronized (BatteryStatsImpl.this) {
                            if (BatteryStatsImpl.this.mCharging) {
                                action = BatteryManager.ACTION_CHARGING;
                            } else {
                                action = BatteryManager.ACTION_DISCHARGING;
                            }
                        }
                        Intent intent = new Intent(action);
                        intent.addFlags(67108864);
                        cb.batterySendBroadcast(intent);
                    }
                } else if (cb != null) {
                    if (msg.arg1 == 0) {
                        z = false;
                    }
                    cb.batteryPowerChanged(z);
                }
            } else if (cb != null) {
                cb.batteryNeedsCpuUpdate();
            }
        }
    }

    public abstract class OverflowArrayMap<T> {
        private static final String OVERFLOW_NAME = "*overflow*";
        ArrayMap<String, MutableInt> mActiveOverflow;
        T mCurOverflow;
        long mLastCleanupTime;
        long mLastClearTime;
        long mLastOverflowFinishTime;
        long mLastOverflowTime;
        final ArrayMap<String, T> mMap = new ArrayMap();
        final int mUid;

        public abstract T instantiateObject();

        public OverflowArrayMap(int uid) {
            this.mUid = uid;
        }

        public ArrayMap<String, T> getMap() {
            return this.mMap;
        }

        public void clear() {
            this.mLastClearTime = SystemClock.elapsedRealtime();
            this.mMap.clear();
            this.mCurOverflow = null;
            this.mActiveOverflow = null;
        }

        public void add(String name, T obj) {
            if (name == null) {
                name = "";
            }
            this.mMap.put(name, obj);
            if (OVERFLOW_NAME.equals(name)) {
                this.mCurOverflow = obj;
            }
        }

        public void cleanup() {
            this.mLastCleanupTime = SystemClock.elapsedRealtime();
            ArrayMap arrayMap = this.mActiveOverflow;
            if (arrayMap != null && arrayMap.size() == 0) {
                this.mActiveOverflow = null;
            }
            arrayMap = this.mActiveOverflow;
            String str = BatteryStatsImpl.TAG;
            String str2 = OVERFLOW_NAME;
            StringBuilder stringBuilder;
            if (arrayMap == null) {
                if (this.mMap.containsKey(str2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Cleaning up with no active overflow, but have overflow entry ");
                    stringBuilder.append(this.mMap.get(str2));
                    Slog.wtf(str, stringBuilder.toString());
                    this.mMap.remove(str2);
                }
                this.mCurOverflow = null;
            } else if (this.mCurOverflow == null || !this.mMap.containsKey(str2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Cleaning up with active overflow, but no overflow entry: cur=");
                stringBuilder.append(this.mCurOverflow);
                stringBuilder.append(" map=");
                stringBuilder.append(this.mMap.get(str2));
                Slog.wtf(str, stringBuilder.toString());
            }
        }

        public T startObject(String name) {
            if (name == null) {
                name = "";
            }
            T obj = this.mMap.get(name);
            if (obj != null) {
                return obj;
            }
            T instantiateObject;
            ArrayMap arrayMap = this.mActiveOverflow;
            String str = OVERFLOW_NAME;
            if (arrayMap != null) {
                MutableInt over = (MutableInt) arrayMap.get(name);
                if (over != null) {
                    obj = this.mCurOverflow;
                    if (obj == null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Have active overflow ");
                        stringBuilder.append(name);
                        stringBuilder.append(" but null overflow");
                        Slog.wtf(BatteryStatsImpl.TAG, stringBuilder.toString());
                        instantiateObject = instantiateObject();
                        this.mCurOverflow = instantiateObject;
                        obj = instantiateObject;
                        this.mMap.put(str, obj);
                    }
                    over.value++;
                    return obj;
                }
            }
            if (this.mMap.size() >= BatteryStatsImpl.MAX_WAKELOCKS_PER_UID) {
                obj = this.mCurOverflow;
                if (obj == null) {
                    instantiateObject = instantiateObject();
                    this.mCurOverflow = instantiateObject;
                    obj = instantiateObject;
                    this.mMap.put(str, obj);
                }
                if (this.mActiveOverflow == null) {
                    this.mActiveOverflow = new ArrayMap();
                }
                this.mActiveOverflow.put(name, new MutableInt(1));
                this.mLastOverflowTime = SystemClock.elapsedRealtime();
                return obj;
            }
            obj = instantiateObject();
            this.mMap.put(name, obj);
            return obj;
        }

        public T stopObject(String name) {
            if (name == null) {
                name = "";
            }
            T obj = this.mMap.get(name);
            if (obj != null) {
                return obj;
            }
            ArrayMap arrayMap = this.mActiveOverflow;
            if (arrayMap != null) {
                MutableInt over = (MutableInt) arrayMap.get(name);
                if (over != null) {
                    obj = this.mCurOverflow;
                    if (obj != null) {
                        over.value--;
                        if (over.value <= 0) {
                            this.mActiveOverflow.remove(name);
                            this.mLastOverflowFinishTime = SystemClock.elapsedRealtime();
                        }
                        return obj;
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find object for ");
            sb.append(name);
            sb.append(" in uid ");
            sb.append(this.mUid);
            sb.append(" mapsize=");
            sb.append(this.mMap.size());
            sb.append(" activeoverflow=");
            sb.append(this.mActiveOverflow);
            sb.append(" curoverflow=");
            sb.append(this.mCurOverflow);
            long now = SystemClock.elapsedRealtime();
            if (this.mLastOverflowTime != 0) {
                sb.append(" lastOverflowTime=");
                TimeUtils.formatDuration(this.mLastOverflowTime - now, sb);
            }
            if (this.mLastOverflowFinishTime != 0) {
                sb.append(" lastOverflowFinishTime=");
                TimeUtils.formatDuration(this.mLastOverflowFinishTime - now, sb);
            }
            if (this.mLastClearTime != 0) {
                sb.append(" lastClearTime=");
                TimeUtils.formatDuration(this.mLastClearTime - now, sb);
            }
            if (this.mLastCleanupTime != 0) {
                sb.append(" lastCleanupTime=");
                TimeUtils.formatDuration(this.mLastCleanupTime - now, sb);
            }
            Slog.wtf(BatteryStatsImpl.TAG, sb.toString());
            return null;
        }
    }

    public interface PlatformIdleStateCallback {
        void fillLowPowerStats(RpmStats rpmStats);

        String getPlatformLowPowerStats();

        String getSubsystemLowPowerStats();
    }

    public interface RailEnergyDataCallback {
        void fillRailDataStats(RailStats railStats);
    }

    public static class SamplingTimer extends Timer {
        int mCurrentReportedCount;
        long mCurrentReportedTotalTime;
        boolean mTimeBaseRunning;
        boolean mTrackingReportedValues;
        int mUnpluggedReportedCount;
        long mUnpluggedReportedTotalTime;
        int mUpdateVersion;

        @VisibleForTesting
        public SamplingTimer(Clocks clocks, TimeBase timeBase, Parcel in) {
            boolean z = false;
            super(clocks, 0, timeBase, in);
            this.mCurrentReportedCount = in.readInt();
            this.mUnpluggedReportedCount = in.readInt();
            this.mCurrentReportedTotalTime = in.readLong();
            this.mUnpluggedReportedTotalTime = in.readLong();
            if (in.readInt() == 1) {
                z = true;
            }
            this.mTrackingReportedValues = z;
            this.mTimeBaseRunning = timeBase.isRunning();
        }

        @VisibleForTesting
        public SamplingTimer(Clocks clocks, TimeBase timeBase) {
            super(clocks, 0, timeBase);
            this.mTrackingReportedValues = false;
            this.mTimeBaseRunning = timeBase.isRunning();
        }

        public void endSample() {
            this.mTotalTime = computeRunTimeLocked(0);
            this.mCount = computeCurrentCountLocked();
            this.mCurrentReportedTotalTime = 0;
            this.mUnpluggedReportedTotalTime = 0;
            this.mCurrentReportedCount = 0;
            this.mUnpluggedReportedCount = 0;
        }

        public void setUpdateVersion(int version) {
            this.mUpdateVersion = version;
        }

        public int getUpdateVersion() {
            return this.mUpdateVersion;
        }

        public void update(long totalTime, int count) {
            if (this.mTimeBaseRunning && !this.mTrackingReportedValues) {
                this.mUnpluggedReportedTotalTime = totalTime;
                this.mUnpluggedReportedCount = count;
            }
            this.mTrackingReportedValues = true;
            if (totalTime < this.mCurrentReportedTotalTime || count < this.mCurrentReportedCount) {
                endSample();
            }
            this.mCurrentReportedTotalTime = totalTime;
            this.mCurrentReportedCount = count;
        }

        public void add(long deltaTime, int deltaCount) {
            update(this.mCurrentReportedTotalTime + deltaTime, this.mCurrentReportedCount + deltaCount);
        }

        public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
            super.onTimeStarted(elapsedRealtime, baseUptime, baseRealtime);
            if (this.mTrackingReportedValues) {
                this.mUnpluggedReportedTotalTime = this.mCurrentReportedTotalTime;
                this.mUnpluggedReportedCount = this.mCurrentReportedCount;
            }
            this.mTimeBaseRunning = true;
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            super.onTimeStopped(elapsedRealtime, baseUptime, baseRealtime);
            this.mTimeBaseRunning = false;
        }

        public void logState(Printer pw, String prefix) {
            super.logState(pw, prefix);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("mCurrentReportedCount=");
            stringBuilder.append(this.mCurrentReportedCount);
            stringBuilder.append(" mUnpluggedReportedCount=");
            stringBuilder.append(this.mUnpluggedReportedCount);
            stringBuilder.append(" mCurrentReportedTotalTime=");
            stringBuilder.append(this.mCurrentReportedTotalTime);
            stringBuilder.append(" mUnpluggedReportedTotalTime=");
            stringBuilder.append(this.mUnpluggedReportedTotalTime);
            pw.println(stringBuilder.toString());
        }

        /* Access modifiers changed, original: protected */
        public long computeRunTimeLocked(long curBatteryRealtime) {
            long j = this.mTotalTime;
            long j2 = (this.mTimeBaseRunning && this.mTrackingReportedValues) ? this.mCurrentReportedTotalTime - this.mUnpluggedReportedTotalTime : 0;
            return j + j2;
        }

        /* Access modifiers changed, original: protected */
        public int computeCurrentCountLocked() {
            int i = this.mCount;
            int i2 = (this.mTimeBaseRunning && this.mTrackingReportedValues) ? this.mCurrentReportedCount - this.mUnpluggedReportedCount : 0;
            return i + i2;
        }

        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeInt(this.mCurrentReportedCount);
            out.writeInt(this.mUnpluggedReportedCount);
            out.writeLong(this.mCurrentReportedTotalTime);
            out.writeLong(this.mUnpluggedReportedTotalTime);
            out.writeInt(this.mTrackingReportedValues);
        }

        public boolean reset(boolean detachIfReset) {
            super.reset(detachIfReset);
            this.mTrackingReportedValues = false;
            this.mUnpluggedReportedTotalTime = 0;
            this.mUnpluggedReportedCount = 0;
            return true;
        }
    }

    public static class SystemClocks implements Clocks {
        public long elapsedRealtime() {
            return SystemClock.elapsedRealtime();
        }

        public long uptimeMillis() {
            return SystemClock.uptimeMillis();
        }
    }

    public static class TimeBase {
        protected final Collection<TimeBaseObs> mObservers;
        protected long mPastRealtime;
        protected long mPastUptime;
        protected long mRealtime;
        protected long mRealtimeStart;
        protected boolean mRunning;
        protected long mUnpluggedRealtime;
        protected long mUnpluggedUptime;
        protected long mUptime;
        protected long mUptimeStart;

        public void dump(PrintWriter pw, String prefix) {
            StringBuilder sb = new StringBuilder(128);
            pw.print(prefix);
            pw.print("mRunning=");
            pw.println(this.mRunning);
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mUptime=");
            BatteryStats.formatTimeMs(sb, this.mUptime / 1000);
            pw.println(sb.toString());
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mRealtime=");
            BatteryStats.formatTimeMs(sb, this.mRealtime / 1000);
            pw.println(sb.toString());
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mPastUptime=");
            BatteryStats.formatTimeMs(sb, this.mPastUptime / 1000);
            sb.append("mUptimeStart=");
            BatteryStats.formatTimeMs(sb, this.mUptimeStart / 1000);
            sb.append("mUnpluggedUptime=");
            BatteryStats.formatTimeMs(sb, this.mUnpluggedUptime / 1000);
            pw.println(sb.toString());
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mPastRealtime=");
            BatteryStats.formatTimeMs(sb, this.mPastRealtime / 1000);
            sb.append("mRealtimeStart=");
            BatteryStats.formatTimeMs(sb, this.mRealtimeStart / 1000);
            sb.append("mUnpluggedRealtime=");
            BatteryStats.formatTimeMs(sb, this.mUnpluggedRealtime / 1000);
            pw.println(sb.toString());
        }

        public TimeBase(boolean isLongList) {
            this.mObservers = isLongList ? new HashSet() : new ArrayList();
        }

        public TimeBase() {
            this(false);
        }

        public void add(TimeBaseObs observer) {
            this.mObservers.add(observer);
        }

        public void remove(TimeBaseObs observer) {
            this.mObservers.remove(observer);
        }

        public boolean hasObserver(TimeBaseObs observer) {
            return this.mObservers.contains(observer);
        }

        public void init(long uptime, long realtime) {
            this.mRealtime = 0;
            this.mUptime = 0;
            this.mPastUptime = 0;
            this.mPastRealtime = 0;
            this.mUptimeStart = uptime;
            this.mRealtimeStart = realtime;
            this.mUnpluggedUptime = getUptime(this.mUptimeStart);
            this.mUnpluggedRealtime = getRealtime(this.mRealtimeStart);
        }

        public void reset(long uptime, long realtime) {
            if (this.mRunning) {
                this.mUptimeStart = uptime;
                this.mRealtimeStart = realtime;
                this.mUnpluggedUptime = getUptime(uptime);
                this.mUnpluggedRealtime = getRealtime(realtime);
                return;
            }
            this.mPastUptime = 0;
            this.mPastRealtime = 0;
        }

        public long computeUptime(long curTime, int which) {
            return this.mUptime + getUptime(curTime);
        }

        public long computeRealtime(long curTime, int which) {
            return this.mRealtime + getRealtime(curTime);
        }

        public long getUptime(long curTime) {
            long time = this.mPastUptime;
            if (this.mRunning) {
                return time + (curTime - this.mUptimeStart);
            }
            return time;
        }

        public long getRealtime(long curTime) {
            long time = this.mPastRealtime;
            if (this.mRunning) {
                return time + (curTime - this.mRealtimeStart);
            }
            return time;
        }

        public long getUptimeStart() {
            return this.mUptimeStart;
        }

        public long getRealtimeStart() {
            return this.mRealtimeStart;
        }

        public boolean isRunning() {
            return this.mRunning;
        }

        public boolean setRunning(boolean running, long uptime, long realtime) {
            boolean z = running;
            long j = uptime;
            long j2 = realtime;
            if (this.mRunning == z) {
                return false;
            }
            this.mRunning = z;
            if (z) {
                this.mUptimeStart = j;
                this.mRealtimeStart = j2;
                long batteryUptime = getUptime(j);
                this.mUnpluggedUptime = batteryUptime;
                long batteryRealtime = getRealtime(j2);
                this.mUnpluggedRealtime = batteryRealtime;
                for (TimeBaseObs onTimeStarted : this.mObservers) {
                    onTimeStarted.onTimeStarted(realtime, batteryUptime, batteryRealtime);
                }
            } else {
                this.mPastUptime += j - this.mUptimeStart;
                this.mPastRealtime += j2 - this.mRealtimeStart;
                long batteryUptime2 = getUptime(j);
                long batteryRealtime2 = getRealtime(j2);
                for (TimeBaseObs onTimeStarted2 : this.mObservers) {
                    onTimeStarted2.onTimeStopped(realtime, batteryUptime2, batteryRealtime2);
                }
            }
            return true;
        }

        public void readSummaryFromParcel(Parcel in) {
            this.mUptime = in.readLong();
            this.mRealtime = in.readLong();
        }

        public void writeSummaryToParcel(Parcel out, long uptime, long realtime) {
            out.writeLong(computeUptime(uptime, 0));
            out.writeLong(computeRealtime(realtime, 0));
        }

        public void readFromParcel(Parcel in) {
            this.mRunning = false;
            this.mUptime = in.readLong();
            this.mPastUptime = in.readLong();
            this.mUptimeStart = in.readLong();
            this.mRealtime = in.readLong();
            this.mPastRealtime = in.readLong();
            this.mRealtimeStart = in.readLong();
            this.mUnpluggedUptime = in.readLong();
            this.mUnpluggedRealtime = in.readLong();
        }

        public void writeToParcel(Parcel out, long uptime, long realtime) {
            long runningUptime = getUptime(uptime);
            long runningRealtime = getRealtime(realtime);
            out.writeLong(this.mUptime);
            out.writeLong(runningUptime);
            out.writeLong(this.mUptimeStart);
            out.writeLong(this.mRealtime);
            out.writeLong(runningRealtime);
            out.writeLong(this.mRealtimeStart);
            out.writeLong(this.mUnpluggedUptime);
            out.writeLong(this.mUnpluggedRealtime);
        }
    }

    public static class Uid extends android.os.BatteryStats.Uid {
        static final int NO_BATCHED_SCAN_STARTED = -1;
        DualTimer mAggregatedPartialWakelockTimer;
        StopwatchTimer mAudioTurnedOnTimer;
        private ControllerActivityCounterImpl mBluetoothControllerActivity;
        Counter mBluetoothScanResultBgCounter;
        Counter mBluetoothScanResultCounter;
        DualTimer mBluetoothScanTimer;
        DualTimer mBluetoothUnoptimizedScanTimer;
        protected BatteryStatsImpl mBsi;
        StopwatchTimer mCameraTurnedOnTimer;
        IntArray mChildUids;
        LongSamplingCounter mCpuActiveTimeMs;
        LongSamplingCounter[][] mCpuClusterSpeedTimesUs;
        LongSamplingCounterArray mCpuClusterTimesMs;
        LongSamplingCounterArray mCpuFreqTimeMs;
        long mCurStepSystemTime;
        long mCurStepUserTime;
        StopwatchTimer mFlashlightTurnedOnTimer;
        StopwatchTimer mForegroundActivityTimer;
        StopwatchTimer mForegroundServiceTimer;
        boolean mFullWifiLockOut;
        StopwatchTimer mFullWifiLockTimer;
        boolean mInForegroundService = false;
        final ArrayMap<String, SparseIntArray> mJobCompletions = new ArrayMap();
        final OverflowArrayMap<DualTimer> mJobStats;
        Counter mJobsDeferredCount;
        Counter mJobsDeferredEventCount;
        final Counter[] mJobsFreshnessBuckets;
        LongSamplingCounter mJobsFreshnessTimeMs;
        long mLastStepSystemTime;
        long mLastStepUserTime;
        LongSamplingCounter mMobileRadioActiveCount;
        LongSamplingCounter mMobileRadioActiveTime;
        private LongSamplingCounter mMobileRadioApWakeupCount;
        private ControllerActivityCounterImpl mModemControllerActivity;
        LongSamplingCounter[] mNetworkByteActivityCounters;
        LongSamplingCounter[] mNetworkPacketActivityCounters;
        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public final TimeBase mOnBatteryBackgroundTimeBase;
        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public final TimeBase mOnBatteryScreenOffBackgroundTimeBase;
        final ArrayMap<String, Pkg> mPackageStats = new ArrayMap();
        final SparseArray<Pid> mPids = new SparseArray();
        LongSamplingCounterArray[] mProcStateScreenOffTimeMs;
        LongSamplingCounterArray[] mProcStateTimeMs;
        int mProcessState = 21;
        StopwatchTimer[] mProcessStateTimer;
        final ArrayMap<String, Proc> mProcessStats = new ArrayMap();
        LongSamplingCounterArray mScreenOffCpuFreqTimeMs;
        final SparseArray<Sensor> mSensorStats = new SparseArray();
        final OverflowArrayMap<DualTimer> mSyncStats;
        LongSamplingCounter mSystemCpuTime;
        final int mUid;
        Counter[] mUserActivityCounters;
        LongSamplingCounter mUserCpuTime;
        BatchTimer mVibratorOnTimer;
        StopwatchTimer mVideoTurnedOnTimer;
        final OverflowArrayMap<Wakelock> mWakelockStats;
        int mWifiBatchedScanBinStarted = -1;
        StopwatchTimer[] mWifiBatchedScanTimer;
        private ControllerActivityCounterImpl mWifiControllerActivity;
        StopwatchTimer mWifiMulticastTimer;
        int mWifiMulticastWakelockCount;
        private LongSamplingCounter mWifiRadioApWakeupCount;
        boolean mWifiRunning;
        StopwatchTimer mWifiRunningTimer;
        boolean mWifiScanStarted;
        DualTimer mWifiScanTimer;

        /* renamed from: com.android.internal.os.BatteryStatsImpl$Uid$1 */
        class AnonymousClass1 extends OverflowArrayMap<Wakelock> {
            AnonymousClass1(BatteryStatsImpl x0, int uid) {
                Objects.requireNonNull(x0);
                super(uid);
            }

            public Wakelock instantiateObject() {
                return new Wakelock(Uid.this.mBsi, Uid.this);
            }
        }

        /* renamed from: com.android.internal.os.BatteryStatsImpl$Uid$2 */
        class AnonymousClass2 extends OverflowArrayMap<DualTimer> {
            AnonymousClass2(BatteryStatsImpl x0, int uid) {
                Objects.requireNonNull(x0);
                super(uid);
            }

            public DualTimer instantiateObject() {
                Clocks clocks = Uid.this.mBsi.mClocks;
                Uid uid = Uid.this;
                return new DualTimer(clocks, uid, 13, null, uid.mBsi.mOnBatteryTimeBase, Uid.this.mOnBatteryBackgroundTimeBase);
            }
        }

        /* renamed from: com.android.internal.os.BatteryStatsImpl$Uid$3 */
        class AnonymousClass3 extends OverflowArrayMap<DualTimer> {
            AnonymousClass3(BatteryStatsImpl x0, int uid) {
                Objects.requireNonNull(x0);
                super(uid);
            }

            public DualTimer instantiateObject() {
                Clocks clocks = Uid.this.mBsi.mClocks;
                Uid uid = Uid.this;
                return new DualTimer(clocks, uid, 14, null, uid.mBsi.mOnBatteryTimeBase, Uid.this.mOnBatteryBackgroundTimeBase);
            }
        }

        public static class Pkg extends android.os.BatteryStats.Uid.Pkg implements TimeBaseObs {
            protected BatteryStatsImpl mBsi;
            final ArrayMap<String, Serv> mServiceStats = new ArrayMap();
            ArrayMap<String, Counter> mWakeupAlarms = new ArrayMap();

            public static class Serv extends android.os.BatteryStats.Uid.Pkg.Serv implements TimeBaseObs {
                protected BatteryStatsImpl mBsi;
                protected boolean mLaunched;
                protected long mLaunchedSince;
                protected long mLaunchedTime;
                protected int mLaunches;
                protected Pkg mPkg;
                protected boolean mRunning;
                protected long mRunningSince;
                protected long mStartTime;
                protected int mStarts;

                public Serv(BatteryStatsImpl bsi) {
                    this.mBsi = bsi;
                    this.mBsi.mOnBatteryTimeBase.add(this);
                }

                public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
                }

                public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
                }

                public boolean reset(boolean detachIfReset) {
                    if (detachIfReset) {
                        detach();
                    }
                    return true;
                }

                public void detach() {
                    this.mBsi.mOnBatteryTimeBase.remove(this);
                }

                public void readFromParcelLocked(Parcel in) {
                    this.mStartTime = in.readLong();
                    this.mRunningSince = in.readLong();
                    boolean z = true;
                    this.mRunning = in.readInt() != 0;
                    this.mStarts = in.readInt();
                    this.mLaunchedTime = in.readLong();
                    this.mLaunchedSince = in.readLong();
                    if (in.readInt() == 0) {
                        z = false;
                    }
                    this.mLaunched = z;
                    this.mLaunches = in.readInt();
                }

                public void writeToParcelLocked(Parcel out) {
                    out.writeLong(this.mStartTime);
                    out.writeLong(this.mRunningSince);
                    out.writeInt(this.mRunning);
                    out.writeInt(this.mStarts);
                    out.writeLong(this.mLaunchedTime);
                    out.writeLong(this.mLaunchedSince);
                    out.writeInt(this.mLaunched);
                    out.writeInt(this.mLaunches);
                }

                public long getLaunchTimeToNowLocked(long batteryUptime) {
                    if (this.mLaunched) {
                        return (this.mLaunchedTime + batteryUptime) - this.mLaunchedSince;
                    }
                    return this.mLaunchedTime;
                }

                public long getStartTimeToNowLocked(long batteryUptime) {
                    if (this.mRunning) {
                        return (this.mStartTime + batteryUptime) - this.mRunningSince;
                    }
                    return this.mStartTime;
                }

                @UnsupportedAppUsage
                public void startLaunchedLocked() {
                    if (!this.mLaunched) {
                        this.mLaunches++;
                        this.mLaunchedSince = this.mBsi.getBatteryUptimeLocked();
                        this.mLaunched = true;
                    }
                }

                @UnsupportedAppUsage
                public void stopLaunchedLocked() {
                    if (this.mLaunched) {
                        long time = this.mBsi.getBatteryUptimeLocked() - this.mLaunchedSince;
                        if (time > 0) {
                            this.mLaunchedTime += time;
                        } else {
                            this.mLaunches--;
                        }
                        this.mLaunched = false;
                    }
                }

                @UnsupportedAppUsage
                public void startRunningLocked() {
                    if (!this.mRunning) {
                        this.mStarts++;
                        this.mRunningSince = this.mBsi.getBatteryUptimeLocked();
                        this.mRunning = true;
                    }
                }

                @UnsupportedAppUsage
                public void stopRunningLocked() {
                    if (this.mRunning) {
                        long time = this.mBsi.getBatteryUptimeLocked() - this.mRunningSince;
                        if (time > 0) {
                            this.mStartTime += time;
                        } else {
                            this.mStarts--;
                        }
                        this.mRunning = false;
                    }
                }

                @UnsupportedAppUsage
                public BatteryStatsImpl getBatteryStats() {
                    return this.mBsi;
                }

                public int getLaunches(int which) {
                    return this.mLaunches;
                }

                public long getStartTime(long now, int which) {
                    return getStartTimeToNowLocked(now);
                }

                public int getStarts(int which) {
                    return this.mStarts;
                }
            }

            public Pkg(BatteryStatsImpl bsi) {
                this.mBsi = bsi;
                this.mBsi.mOnBatteryScreenOffTimeBase.add(this);
            }

            public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
            }

            public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            }

            public boolean reset(boolean detachIfReset) {
                if (detachIfReset) {
                    detach();
                }
                return true;
            }

            public void detach() {
                int j;
                this.mBsi.mOnBatteryScreenOffTimeBase.remove(this);
                for (j = this.mWakeupAlarms.size() - 1; j >= 0; j--) {
                    BatteryStatsImpl.detachIfNotNull((TimeBaseObs) (Counter) this.mWakeupAlarms.valueAt(j));
                }
                for (j = this.mServiceStats.size() - 1; j >= 0; j--) {
                    BatteryStatsImpl.detachIfNotNull((TimeBaseObs) (Serv) this.mServiceStats.valueAt(j));
                }
            }

            /* Access modifiers changed, original: 0000 */
            public void readFromParcelLocked(Parcel in) {
                int i;
                int numWA = in.readInt();
                this.mWakeupAlarms.clear();
                for (i = 0; i < numWA; i++) {
                    this.mWakeupAlarms.put(in.readString(), new Counter(this.mBsi.mOnBatteryScreenOffTimeBase, in));
                }
                i = in.readInt();
                this.mServiceStats.clear();
                for (int m = 0; m < i; m++) {
                    String serviceName = in.readString();
                    Serv serv = new Serv(this.mBsi);
                    this.mServiceStats.put(serviceName, serv);
                    serv.readFromParcelLocked(in);
                }
            }

            /* Access modifiers changed, original: 0000 */
            public void writeToParcelLocked(Parcel out) {
                int i;
                int numWA = this.mWakeupAlarms.size();
                out.writeInt(numWA);
                for (i = 0; i < numWA; i++) {
                    out.writeString((String) this.mWakeupAlarms.keyAt(i));
                    ((Counter) this.mWakeupAlarms.valueAt(i)).writeToParcel(out);
                }
                i = this.mServiceStats.size();
                out.writeInt(i);
                for (int i2 = 0; i2 < i; i2++) {
                    out.writeString((String) this.mServiceStats.keyAt(i2));
                    ((Serv) this.mServiceStats.valueAt(i2)).writeToParcelLocked(out);
                }
            }

            public ArrayMap<String, ? extends android.os.BatteryStats.Counter> getWakeupAlarmStats() {
                return this.mWakeupAlarms;
            }

            public void noteWakeupAlarmLocked(String tag) {
                Counter c = (Counter) this.mWakeupAlarms.get(tag);
                if (c == null) {
                    c = new Counter(this.mBsi.mOnBatteryScreenOffTimeBase);
                    this.mWakeupAlarms.put(tag, c);
                }
                c.stepAtomic();
            }

            public ArrayMap<String, ? extends android.os.BatteryStats.Uid.Pkg.Serv> getServiceStats() {
                return this.mServiceStats;
            }

            /* Access modifiers changed, original: final */
            public final Serv newServiceStatsLocked() {
                return new Serv(this.mBsi);
            }
        }

        public static class Proc extends android.os.BatteryStats.Uid.Proc implements TimeBaseObs {
            boolean mActive = true;
            protected BatteryStatsImpl mBsi;
            ArrayList<ExcessivePower> mExcessivePower;
            long mForegroundTime;
            final String mName;
            int mNumAnrs;
            int mNumCrashes;
            int mStarts;
            long mSystemTime;
            long mUserTime;

            public Proc(BatteryStatsImpl bsi, String name) {
                this.mBsi = bsi;
                this.mName = name;
                this.mBsi.mOnBatteryTimeBase.add(this);
            }

            public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
            }

            public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            }

            public boolean reset(boolean detachIfReset) {
                if (detachIfReset) {
                    detach();
                }
                return true;
            }

            public void detach() {
                this.mActive = false;
                this.mBsi.mOnBatteryTimeBase.remove(this);
            }

            public int countExcessivePowers() {
                ArrayList arrayList = this.mExcessivePower;
                return arrayList != null ? arrayList.size() : 0;
            }

            public ExcessivePower getExcessivePower(int i) {
                ArrayList arrayList = this.mExcessivePower;
                if (arrayList != null) {
                    return (ExcessivePower) arrayList.get(i);
                }
                return null;
            }

            public void addExcessiveCpu(long overTime, long usedTime) {
                if (this.mExcessivePower == null) {
                    this.mExcessivePower = new ArrayList();
                }
                ExcessivePower ew = new ExcessivePower();
                ew.type = 2;
                ew.overTime = overTime;
                ew.usedTime = usedTime;
                this.mExcessivePower.add(ew);
            }

            /* Access modifiers changed, original: 0000 */
            public void writeExcessivePowerToParcelLocked(Parcel out) {
                int N = this.mExcessivePower;
                if (N == 0) {
                    out.writeInt(0);
                    return;
                }
                N = N.size();
                out.writeInt(N);
                for (int i = 0; i < N; i++) {
                    ExcessivePower ew = (ExcessivePower) this.mExcessivePower.get(i);
                    out.writeInt(ew.type);
                    out.writeLong(ew.overTime);
                    out.writeLong(ew.usedTime);
                }
            }

            /* Access modifiers changed, original: 0000 */
            public void readExcessivePowerFromParcelLocked(Parcel in) {
                int N = in.readInt();
                if (N == 0) {
                    this.mExcessivePower = null;
                } else if (N <= 10000) {
                    this.mExcessivePower = new ArrayList();
                    for (int i = 0; i < N; i++) {
                        ExcessivePower ew = new ExcessivePower();
                        ew.type = in.readInt();
                        ew.overTime = in.readLong();
                        ew.usedTime = in.readLong();
                        this.mExcessivePower.add(ew);
                    }
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("File corrupt: too many excessive power entries ");
                    stringBuilder.append(N);
                    throw new ParcelFormatException(stringBuilder.toString());
                }
            }

            /* Access modifiers changed, original: 0000 */
            public void writeToParcelLocked(Parcel out) {
                out.writeLong(this.mUserTime);
                out.writeLong(this.mSystemTime);
                out.writeLong(this.mForegroundTime);
                out.writeInt(this.mStarts);
                out.writeInt(this.mNumCrashes);
                out.writeInt(this.mNumAnrs);
                writeExcessivePowerToParcelLocked(out);
            }

            /* Access modifiers changed, original: 0000 */
            public void readFromParcelLocked(Parcel in) {
                this.mUserTime = in.readLong();
                this.mSystemTime = in.readLong();
                this.mForegroundTime = in.readLong();
                this.mStarts = in.readInt();
                this.mNumCrashes = in.readInt();
                this.mNumAnrs = in.readInt();
                readExcessivePowerFromParcelLocked(in);
            }

            @UnsupportedAppUsage
            public void addCpuTimeLocked(int utime, int stime) {
                addCpuTimeLocked(utime, stime, this.mBsi.mOnBatteryTimeBase.isRunning());
            }

            public void addCpuTimeLocked(int utime, int stime, boolean isRunning) {
                if (isRunning) {
                    this.mUserTime += (long) utime;
                    this.mSystemTime += (long) stime;
                }
            }

            @UnsupportedAppUsage
            public void addForegroundTimeLocked(long ttime) {
                this.mForegroundTime += ttime;
            }

            @UnsupportedAppUsage
            public void incStartsLocked() {
                this.mStarts++;
            }

            public void incNumCrashesLocked() {
                this.mNumCrashes++;
            }

            public void incNumAnrsLocked() {
                this.mNumAnrs++;
            }

            public boolean isActive() {
                return this.mActive;
            }

            @UnsupportedAppUsage
            public long getUserTime(int which) {
                return this.mUserTime;
            }

            @UnsupportedAppUsage
            public long getSystemTime(int which) {
                return this.mSystemTime;
            }

            @UnsupportedAppUsage
            public long getForegroundTime(int which) {
                return this.mForegroundTime;
            }

            @UnsupportedAppUsage
            public int getStarts(int which) {
                return this.mStarts;
            }

            public int getNumCrashes(int which) {
                return this.mNumCrashes;
            }

            public int getNumAnrs(int which) {
                return this.mNumAnrs;
            }
        }

        public static class Sensor extends android.os.BatteryStats.Uid.Sensor {
            protected BatteryStatsImpl mBsi;
            final int mHandle;
            DualTimer mTimer;
            protected Uid mUid;

            public Sensor(BatteryStatsImpl bsi, Uid uid, int handle) {
                this.mBsi = bsi;
                this.mUid = uid;
                this.mHandle = handle;
            }

            private DualTimer readTimersFromParcel(TimeBase timeBase, TimeBase bgTimeBase, Parcel in) {
                if (in.readInt() == 0) {
                    return null;
                }
                ArrayList<StopwatchTimer> pool = (ArrayList) this.mBsi.mSensorTimers.get(this.mHandle);
                if (pool == null) {
                    pool = new ArrayList();
                    this.mBsi.mSensorTimers.put(this.mHandle, pool);
                }
                return new DualTimer(this.mBsi.mClocks, this.mUid, 0, pool, timeBase, bgTimeBase, in);
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

            @UnsupportedAppUsage
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

            @UnsupportedAppUsage
            public int getHandle() {
                return this.mHandle;
            }

            public void detachFromTimeBase() {
                BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mTimer);
            }
        }

        public static class Wakelock extends android.os.BatteryStats.Uid.Wakelock {
            protected BatteryStatsImpl mBsi;
            StopwatchTimer mTimerDraw;
            StopwatchTimer mTimerFull;
            DualTimer mTimerPartial;
            StopwatchTimer mTimerWindow;
            protected Uid mUid;

            public Wakelock(BatteryStatsImpl bsi, Uid uid) {
                this.mBsi = bsi;
                this.mUid = uid;
            }

            private StopwatchTimer readStopwatchTimerFromParcel(int type, ArrayList<StopwatchTimer> pool, TimeBase timeBase, Parcel in) {
                if (in.readInt() == 0) {
                    return null;
                }
                return new StopwatchTimer(this.mBsi.mClocks, this.mUid, type, pool, timeBase, in);
            }

            private DualTimer readDualTimerFromParcel(int type, ArrayList<StopwatchTimer> pool, TimeBase timeBase, TimeBase bgTimeBase, Parcel in) {
                if (in.readInt() == 0) {
                    return null;
                }
                return new DualTimer(this.mBsi.mClocks, this.mUid, type, pool, timeBase, bgTimeBase, in);
            }

            /* Access modifiers changed, original: 0000 */
            public boolean reset() {
                boolean wlactive = (((false | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mTimerFull, false) ^ 1)) | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mTimerPartial, false) ^ 1)) | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mTimerWindow, false) ^ 1)) | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mTimerDraw, false) ^ 1);
                if (!wlactive) {
                    BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mTimerFull);
                    this.mTimerFull = null;
                    BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mTimerPartial);
                    this.mTimerPartial = null;
                    BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mTimerWindow);
                    this.mTimerWindow = null;
                    BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mTimerDraw);
                    this.mTimerDraw = null;
                }
                if (wlactive) {
                    return false;
                }
                return true;
            }

            /* Access modifiers changed, original: 0000 */
            public void readFromParcelLocked(TimeBase timeBase, TimeBase screenOffTimeBase, TimeBase screenOffBgTimeBase, Parcel in) {
                this.mTimerPartial = readDualTimerFromParcel(0, this.mBsi.mPartialTimers, screenOffTimeBase, screenOffBgTimeBase, in);
                this.mTimerFull = readStopwatchTimerFromParcel(1, this.mBsi.mFullTimers, timeBase, in);
                this.mTimerWindow = readStopwatchTimerFromParcel(2, this.mBsi.mWindowTimers, timeBase, in);
                this.mTimerDraw = readStopwatchTimerFromParcel(18, this.mBsi.mDrawTimers, timeBase, in);
            }

            /* Access modifiers changed, original: 0000 */
            public void writeToParcelLocked(Parcel out, long elapsedRealtimeUs) {
                Timer.writeTimerToParcel(out, this.mTimerPartial, elapsedRealtimeUs);
                Timer.writeTimerToParcel(out, this.mTimerFull, elapsedRealtimeUs);
                Timer.writeTimerToParcel(out, this.mTimerWindow, elapsedRealtimeUs);
                Timer.writeTimerToParcel(out, this.mTimerDraw, elapsedRealtimeUs);
            }

            @UnsupportedAppUsage
            public Timer getWakeTime(int type) {
                if (type == 0) {
                    return this.mTimerPartial;
                }
                if (type == 1) {
                    return this.mTimerFull;
                }
                if (type == 2) {
                    return this.mTimerWindow;
                }
                if (type == 18) {
                    return this.mTimerDraw;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("type = ");
                stringBuilder.append(type);
                throw new IllegalArgumentException(stringBuilder.toString());
            }

            public void detachFromTimeBase() {
                BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mTimerPartial);
                BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mTimerFull);
                BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mTimerWindow);
                BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mTimerDraw);
            }
        }

        public Uid(BatteryStatsImpl bsi, int uid) {
            this.mBsi = bsi;
            this.mUid = uid;
            this.mOnBatteryBackgroundTimeBase = new TimeBase(false);
            this.mOnBatteryBackgroundTimeBase.init(this.mBsi.mClocks.uptimeMillis() * 1000, this.mBsi.mClocks.elapsedRealtime() * 1000);
            this.mOnBatteryScreenOffBackgroundTimeBase = new TimeBase(false);
            this.mOnBatteryScreenOffBackgroundTimeBase.init(this.mBsi.mClocks.uptimeMillis() * 1000, this.mBsi.mClocks.elapsedRealtime() * 1000);
            this.mUserCpuTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mSystemCpuTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mCpuActiveTimeMs = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mCpuClusterTimesMs = new LongSamplingCounterArray(this.mBsi.mOnBatteryTimeBase);
            BatteryStatsImpl batteryStatsImpl = this.mBsi;
            Objects.requireNonNull(batteryStatsImpl);
            this.mWakelockStats = new AnonymousClass1(batteryStatsImpl, uid);
            batteryStatsImpl = this.mBsi;
            Objects.requireNonNull(batteryStatsImpl);
            this.mSyncStats = new AnonymousClass2(batteryStatsImpl, uid);
            batteryStatsImpl = this.mBsi;
            Objects.requireNonNull(batteryStatsImpl);
            this.mJobStats = new AnonymousClass3(batteryStatsImpl, uid);
            this.mWifiRunningTimer = new StopwatchTimer(this.mBsi.mClocks, this, 4, this.mBsi.mWifiRunningTimers, this.mBsi.mOnBatteryTimeBase);
            this.mFullWifiLockTimer = new StopwatchTimer(this.mBsi.mClocks, this, 5, this.mBsi.mFullWifiLockTimers, this.mBsi.mOnBatteryTimeBase);
            this.mWifiScanTimer = new DualTimer(this.mBsi.mClocks, this, 6, this.mBsi.mWifiScanTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            this.mWifiBatchedScanTimer = new StopwatchTimer[5];
            this.mWifiMulticastTimer = new StopwatchTimer(this.mBsi.mClocks, this, 7, this.mBsi.mWifiMulticastTimers, this.mBsi.mOnBatteryTimeBase);
            this.mProcessStateTimer = new StopwatchTimer[7];
            this.mJobsDeferredEventCount = new Counter(this.mBsi.mOnBatteryTimeBase);
            this.mJobsDeferredCount = new Counter(this.mBsi.mOnBatteryTimeBase);
            this.mJobsFreshnessTimeMs = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mJobsFreshnessBuckets = new Counter[BatteryStats.JOB_FRESHNESS_BUCKETS.length];
        }

        @VisibleForTesting
        public void setProcessStateForTest(int procState) {
            this.mProcessState = procState;
        }

        public long[] getCpuFreqTimes(int which) {
            return nullIfAllZeros(this.mCpuFreqTimeMs, which);
        }

        public long[] getScreenOffCpuFreqTimes(int which) {
            return nullIfAllZeros(this.mScreenOffCpuFreqTimeMs, which);
        }

        public long getCpuActiveTime() {
            return this.mCpuActiveTimeMs.getCountLocked(0);
        }

        public long[] getCpuClusterTimes() {
            return nullIfAllZeros(this.mCpuClusterTimesMs, 0);
        }

        /* JADX WARNING: Missing block: B:13:0x001e, code skipped:
            return null;
     */
        public long[] getCpuFreqTimes(int r3, int r4) {
            /*
            r2 = this;
            r0 = 0;
            if (r3 < 0) goto L_0x001e;
        L_0x0003:
            r1 = 7;
            if (r3 < r1) goto L_0x0007;
        L_0x0006:
            goto L_0x001e;
        L_0x0007:
            r1 = r2.mProcStateTimeMs;
            if (r1 != 0) goto L_0x000c;
        L_0x000b:
            return r0;
        L_0x000c:
            r1 = r2.mBsi;
            r1 = r1.mPerProcStateCpuTimesAvailable;
            if (r1 != 0) goto L_0x0015;
        L_0x0012:
            r2.mProcStateTimeMs = r0;
            return r0;
        L_0x0015:
            r0 = r2.mProcStateTimeMs;
            r0 = r0[r4];
            r0 = r2.nullIfAllZeros(r0, r3);
            return r0;
        L_0x001e:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl$Uid.getCpuFreqTimes(int, int):long[]");
        }

        /* JADX WARNING: Missing block: B:13:0x001e, code skipped:
            return null;
     */
        public long[] getScreenOffCpuFreqTimes(int r3, int r4) {
            /*
            r2 = this;
            r0 = 0;
            if (r3 < 0) goto L_0x001e;
        L_0x0003:
            r1 = 7;
            if (r3 < r1) goto L_0x0007;
        L_0x0006:
            goto L_0x001e;
        L_0x0007:
            r1 = r2.mProcStateScreenOffTimeMs;
            if (r1 != 0) goto L_0x000c;
        L_0x000b:
            return r0;
        L_0x000c:
            r1 = r2.mBsi;
            r1 = r1.mPerProcStateCpuTimesAvailable;
            if (r1 != 0) goto L_0x0015;
        L_0x0012:
            r2.mProcStateScreenOffTimeMs = r0;
            return r0;
        L_0x0015:
            r0 = r2.mProcStateScreenOffTimeMs;
            r0 = r0[r4];
            r0 = r2.nullIfAllZeros(r0, r3);
            return r0;
        L_0x001e:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl$Uid.getScreenOffCpuFreqTimes(int, int):long[]");
        }

        public void addIsolatedUid(int isolatedUid) {
            IntArray intArray = this.mChildUids;
            if (intArray == null) {
                this.mChildUids = new IntArray();
            } else if (intArray.indexOf(isolatedUid) >= 0) {
                return;
            }
            this.mChildUids.add(isolatedUid);
        }

        public void removeIsolatedUid(int isolatedUid) {
            IntArray intArray = this.mChildUids;
            int idx = intArray == null ? -1 : intArray.indexOf(isolatedUid);
            if (idx >= 0) {
                this.mChildUids.remove(idx);
            }
        }

        private long[] nullIfAllZeros(LongSamplingCounterArray cpuTimesMs, int which) {
            if (cpuTimesMs == null) {
                return null;
            }
            long[] counts = cpuTimesMs.getCountsLocked(which);
            if (counts == null) {
                return null;
            }
            for (int i = counts.length - 1; i >= 0; i--) {
                if (counts[i] != 0) {
                    return counts;
                }
            }
            return null;
        }

        private void addProcStateTimesMs(int procState, long[] cpuTimesMs, boolean onBattery) {
            if (this.mProcStateTimeMs == null) {
                this.mProcStateTimeMs = new LongSamplingCounterArray[7];
            }
            LongSamplingCounterArray[] longSamplingCounterArrayArr = this.mProcStateTimeMs;
            if (longSamplingCounterArrayArr[procState] == null || longSamplingCounterArrayArr[procState].getSize() != cpuTimesMs.length) {
                BatteryStatsImpl.detachIfNotNull(this.mProcStateTimeMs[procState]);
                this.mProcStateTimeMs[procState] = new LongSamplingCounterArray(this.mBsi.mOnBatteryTimeBase);
            }
            this.mProcStateTimeMs[procState].addCountLocked(cpuTimesMs, onBattery);
        }

        private void addProcStateScreenOffTimesMs(int procState, long[] cpuTimesMs, boolean onBatteryScreenOff) {
            if (this.mProcStateScreenOffTimeMs == null) {
                this.mProcStateScreenOffTimeMs = new LongSamplingCounterArray[7];
            }
            LongSamplingCounterArray[] longSamplingCounterArrayArr = this.mProcStateScreenOffTimeMs;
            if (longSamplingCounterArrayArr[procState] == null || longSamplingCounterArrayArr[procState].getSize() != cpuTimesMs.length) {
                BatteryStatsImpl.detachIfNotNull(this.mProcStateScreenOffTimeMs[procState]);
                this.mProcStateScreenOffTimeMs[procState] = new LongSamplingCounterArray(this.mBsi.mOnBatteryScreenOffTimeBase);
            }
            this.mProcStateScreenOffTimeMs[procState].addCountLocked(cpuTimesMs, onBatteryScreenOff);
        }

        public Timer getAggregatedPartialWakelockTimer() {
            return this.mAggregatedPartialWakelockTimer;
        }

        @UnsupportedAppUsage
        public ArrayMap<String, ? extends android.os.BatteryStats.Uid.Wakelock> getWakelockStats() {
            return this.mWakelockStats.getMap();
        }

        public Timer getMulticastWakelockStats() {
            return this.mWifiMulticastTimer;
        }

        public ArrayMap<String, ? extends android.os.BatteryStats.Timer> getSyncStats() {
            return this.mSyncStats.getMap();
        }

        public ArrayMap<String, ? extends android.os.BatteryStats.Timer> getJobStats() {
            return this.mJobStats.getMap();
        }

        public ArrayMap<String, SparseIntArray> getJobCompletionStats() {
            return this.mJobCompletions;
        }

        @UnsupportedAppUsage
        public SparseArray<? extends android.os.BatteryStats.Uid.Sensor> getSensorStats() {
            return this.mSensorStats;
        }

        @UnsupportedAppUsage
        public ArrayMap<String, ? extends android.os.BatteryStats.Uid.Proc> getProcessStats() {
            return this.mProcessStats;
        }

        public ArrayMap<String, ? extends android.os.BatteryStats.Uid.Pkg> getPackageStats() {
            return this.mPackageStats;
        }

        @UnsupportedAppUsage
        public int getUid() {
            return this.mUid;
        }

        public void noteWifiRunningLocked(long elapsedRealtimeMs) {
            if (!this.mWifiRunning) {
                this.mWifiRunning = true;
                if (this.mWifiRunningTimer == null) {
                    this.mWifiRunningTimer = new StopwatchTimer(this.mBsi.mClocks, this, 4, this.mBsi.mWifiRunningTimers, this.mBsi.mOnBatteryTimeBase);
                }
                this.mWifiRunningTimer.startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteWifiStoppedLocked(long elapsedRealtimeMs) {
            if (this.mWifiRunning) {
                this.mWifiRunning = false;
                this.mWifiRunningTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteFullWifiLockAcquiredLocked(long elapsedRealtimeMs) {
            if (!this.mFullWifiLockOut) {
                this.mFullWifiLockOut = true;
                if (this.mFullWifiLockTimer == null) {
                    this.mFullWifiLockTimer = new StopwatchTimer(this.mBsi.mClocks, this, 5, this.mBsi.mFullWifiLockTimers, this.mBsi.mOnBatteryTimeBase);
                }
                this.mFullWifiLockTimer.startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteFullWifiLockReleasedLocked(long elapsedRealtimeMs) {
            if (this.mFullWifiLockOut) {
                this.mFullWifiLockOut = false;
                this.mFullWifiLockTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteWifiScanStartedLocked(long elapsedRealtimeMs) {
            if (!this.mWifiScanStarted) {
                this.mWifiScanStarted = true;
                if (this.mWifiScanTimer == null) {
                    this.mWifiScanTimer = new DualTimer(this.mBsi.mClocks, this, 6, this.mBsi.mWifiScanTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
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

        public void noteWifiBatchedScanStartedLocked(int csph, long elapsedRealtimeMs) {
            int bin = 0;
            while (csph > 8 && bin < 4) {
                csph >>= 3;
                bin++;
            }
            int i = this.mWifiBatchedScanBinStarted;
            if (i != bin) {
                if (i != -1) {
                    this.mWifiBatchedScanTimer[i].stopRunningLocked(elapsedRealtimeMs);
                }
                this.mWifiBatchedScanBinStarted = bin;
                if (this.mWifiBatchedScanTimer[bin] == null) {
                    makeWifiBatchedScanBin(bin, null);
                }
                this.mWifiBatchedScanTimer[bin].startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteWifiBatchedScanStoppedLocked(long elapsedRealtimeMs) {
            int i = this.mWifiBatchedScanBinStarted;
            if (i != -1) {
                this.mWifiBatchedScanTimer[i].stopRunningLocked(elapsedRealtimeMs);
                this.mWifiBatchedScanBinStarted = -1;
            }
        }

        public void noteWifiMulticastEnabledLocked(long elapsedRealtimeMs) {
            if (this.mWifiMulticastWakelockCount == 0) {
                if (this.mWifiMulticastTimer == null) {
                    this.mWifiMulticastTimer = new StopwatchTimer(this.mBsi.mClocks, this, 7, this.mBsi.mWifiMulticastTimers, this.mBsi.mOnBatteryTimeBase);
                }
                this.mWifiMulticastTimer.startRunningLocked(elapsedRealtimeMs);
            }
            this.mWifiMulticastWakelockCount++;
        }

        public void noteWifiMulticastDisabledLocked(long elapsedRealtimeMs) {
            int i = this.mWifiMulticastWakelockCount;
            if (i != 0) {
                this.mWifiMulticastWakelockCount = i - 1;
                if (this.mWifiMulticastWakelockCount == 0) {
                    this.mWifiMulticastTimer.stopRunningLocked(elapsedRealtimeMs);
                }
            }
        }

        public ControllerActivityCounter getWifiControllerActivity() {
            return this.mWifiControllerActivity;
        }

        public ControllerActivityCounter getBluetoothControllerActivity() {
            return this.mBluetoothControllerActivity;
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

        public ControllerActivityCounterImpl getOrCreateBluetoothControllerActivityLocked() {
            if (this.mBluetoothControllerActivity == null) {
                this.mBluetoothControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 1);
            }
            return this.mBluetoothControllerActivity;
        }

        public ControllerActivityCounterImpl getOrCreateModemControllerActivityLocked() {
            if (this.mModemControllerActivity == null) {
                this.mModemControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 5);
            }
            return this.mModemControllerActivity;
        }

        public StopwatchTimer createAudioTurnedOnTimerLocked() {
            if (this.mAudioTurnedOnTimer == null) {
                this.mAudioTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 15, this.mBsi.mAudioTurnedOnTimers, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mAudioTurnedOnTimer;
        }

        public void noteAudioTurnedOnLocked(long elapsedRealtimeMs) {
            createAudioTurnedOnTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteAudioTurnedOffLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mAudioTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetAudioLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mAudioTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public StopwatchTimer createVideoTurnedOnTimerLocked() {
            if (this.mVideoTurnedOnTimer == null) {
                this.mVideoTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 8, this.mBsi.mVideoTurnedOnTimers, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mVideoTurnedOnTimer;
        }

        public void noteVideoTurnedOnLocked(long elapsedRealtimeMs) {
            createVideoTurnedOnTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteVideoTurnedOffLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mVideoTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetVideoLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mVideoTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public StopwatchTimer createFlashlightTurnedOnTimerLocked() {
            if (this.mFlashlightTurnedOnTimer == null) {
                this.mFlashlightTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 16, this.mBsi.mFlashlightTurnedOnTimers, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mFlashlightTurnedOnTimer;
        }

        public void noteFlashlightTurnedOnLocked(long elapsedRealtimeMs) {
            createFlashlightTurnedOnTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteFlashlightTurnedOffLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mFlashlightTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetFlashlightLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mFlashlightTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public StopwatchTimer createCameraTurnedOnTimerLocked() {
            if (this.mCameraTurnedOnTimer == null) {
                this.mCameraTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 17, this.mBsi.mCameraTurnedOnTimers, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mCameraTurnedOnTimer;
        }

        public void noteCameraTurnedOnLocked(long elapsedRealtimeMs) {
            createCameraTurnedOnTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteCameraTurnedOffLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mCameraTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetCameraLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mCameraTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public StopwatchTimer createForegroundActivityTimerLocked() {
            if (this.mForegroundActivityTimer == null) {
                this.mForegroundActivityTimer = new StopwatchTimer(this.mBsi.mClocks, this, 10, null, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mForegroundActivityTimer;
        }

        public StopwatchTimer createForegroundServiceTimerLocked() {
            if (this.mForegroundServiceTimer == null) {
                this.mForegroundServiceTimer = new StopwatchTimer(this.mBsi.mClocks, this, 22, null, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mForegroundServiceTimer;
        }

        public DualTimer createAggregatedPartialWakelockTimerLocked() {
            if (this.mAggregatedPartialWakelockTimer == null) {
                this.mAggregatedPartialWakelockTimer = new DualTimer(this.mBsi.mClocks, this, 20, null, this.mBsi.mOnBatteryScreenOffTimeBase, this.mOnBatteryScreenOffBackgroundTimeBase);
            }
            return this.mAggregatedPartialWakelockTimer;
        }

        public DualTimer createBluetoothScanTimerLocked() {
            if (this.mBluetoothScanTimer == null) {
                this.mBluetoothScanTimer = new DualTimer(this.mBsi.mClocks, this, 19, this.mBsi.mBluetoothScanOnTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            }
            return this.mBluetoothScanTimer;
        }

        public DualTimer createBluetoothUnoptimizedScanTimerLocked() {
            if (this.mBluetoothUnoptimizedScanTimer == null) {
                this.mBluetoothUnoptimizedScanTimer = new DualTimer(this.mBsi.mClocks, this, 21, null, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            }
            return this.mBluetoothUnoptimizedScanTimer;
        }

        public void noteBluetoothScanStartedLocked(long elapsedRealtimeMs, boolean isUnoptimized) {
            createBluetoothScanTimerLocked().startRunningLocked(elapsedRealtimeMs);
            if (isUnoptimized) {
                createBluetoothUnoptimizedScanTimerLocked().startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteBluetoothScanStoppedLocked(long elapsedRealtimeMs, boolean isUnoptimized) {
            DualTimer dualTimer = this.mBluetoothScanTimer;
            if (dualTimer != null) {
                dualTimer.stopRunningLocked(elapsedRealtimeMs);
            }
            if (isUnoptimized) {
                dualTimer = this.mBluetoothUnoptimizedScanTimer;
                if (dualTimer != null) {
                    dualTimer.stopRunningLocked(elapsedRealtimeMs);
                }
            }
        }

        public void noteResetBluetoothScanLocked(long elapsedRealtimeMs) {
            DualTimer dualTimer = this.mBluetoothScanTimer;
            if (dualTimer != null) {
                dualTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
            dualTimer = this.mBluetoothUnoptimizedScanTimer;
            if (dualTimer != null) {
                dualTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public Counter createBluetoothScanResultCounterLocked() {
            if (this.mBluetoothScanResultCounter == null) {
                this.mBluetoothScanResultCounter = new Counter(this.mBsi.mOnBatteryTimeBase);
            }
            return this.mBluetoothScanResultCounter;
        }

        public Counter createBluetoothScanResultBgCounterLocked() {
            if (this.mBluetoothScanResultBgCounter == null) {
                this.mBluetoothScanResultBgCounter = new Counter(this.mOnBatteryBackgroundTimeBase);
            }
            return this.mBluetoothScanResultBgCounter;
        }

        public void noteBluetoothScanResultsLocked(int numNewResults) {
            createBluetoothScanResultCounterLocked().addAtomic(numNewResults);
            createBluetoothScanResultBgCounterLocked().addAtomic(numNewResults);
        }

        public void noteActivityResumedLocked(long elapsedRealtimeMs) {
            createForegroundActivityTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteActivityPausedLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mForegroundActivityTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteForegroundServiceResumedLocked(long elapsedRealtimeMs) {
            createForegroundServiceTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteForegroundServicePausedLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mForegroundServiceTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public BatchTimer createVibratorOnTimerLocked() {
            if (this.mVibratorOnTimer == null) {
                this.mVibratorOnTimer = new BatchTimer(this.mBsi.mClocks, this, 9, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mVibratorOnTimer;
        }

        public void noteVibratorOnLocked(long durationMillis) {
            createVibratorOnTimerLocked().addDuration(this.mBsi, durationMillis);
        }

        public void noteVibratorOffLocked() {
            BatchTimer batchTimer = this.mVibratorOnTimer;
            if (batchTimer != null) {
                batchTimer.abortLastDuration(this.mBsi);
            }
        }

        @UnsupportedAppUsage
        public long getWifiRunningTime(long elapsedRealtimeUs, int which) {
            StopwatchTimer stopwatchTimer = this.mWifiRunningTimer;
            if (stopwatchTimer == null) {
                return 0;
            }
            return stopwatchTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        public long getFullWifiLockTime(long elapsedRealtimeUs, int which) {
            StopwatchTimer stopwatchTimer = this.mFullWifiLockTimer;
            if (stopwatchTimer == null) {
                return 0;
            }
            return stopwatchTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        @UnsupportedAppUsage
        public long getWifiScanTime(long elapsedRealtimeUs, int which) {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null) {
                return 0;
            }
            return dualTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        public int getWifiScanCount(int which) {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null) {
                return 0;
            }
            return dualTimer.getCountLocked(which);
        }

        public Timer getWifiScanTimer() {
            return this.mWifiScanTimer;
        }

        public int getWifiScanBackgroundCount(int which) {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null || dualTimer.getSubTimer() == null) {
                return 0;
            }
            return this.mWifiScanTimer.getSubTimer().getCountLocked(which);
        }

        public long getWifiScanActualTime(long elapsedRealtimeUs) {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null) {
                return 0;
            }
            return dualTimer.getTotalDurationMsLocked((500 + elapsedRealtimeUs) / 1000) * 1000;
        }

        public long getWifiScanBackgroundTime(long elapsedRealtimeUs) {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null || dualTimer.getSubTimer() == null) {
                return 0;
            }
            return this.mWifiScanTimer.getSubTimer().getTotalDurationMsLocked((500 + elapsedRealtimeUs) / 1000) * 1000;
        }

        public Timer getWifiScanBackgroundTimer() {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null) {
                return null;
            }
            return dualTimer.getSubTimer();
        }

        public long getWifiBatchedScanTime(int csphBin, long elapsedRealtimeUs, int which) {
            if (csphBin < 0 || csphBin >= 5) {
                return 0;
            }
            StopwatchTimer[] stopwatchTimerArr = this.mWifiBatchedScanTimer;
            if (stopwatchTimerArr[csphBin] == null) {
                return 0;
            }
            return stopwatchTimerArr[csphBin].getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        public int getWifiBatchedScanCount(int csphBin, int which) {
            if (csphBin < 0 || csphBin >= 5) {
                return 0;
            }
            StopwatchTimer[] stopwatchTimerArr = this.mWifiBatchedScanTimer;
            if (stopwatchTimerArr[csphBin] == null) {
                return 0;
            }
            return stopwatchTimerArr[csphBin].getCountLocked(which);
        }

        public long getWifiMulticastTime(long elapsedRealtimeUs, int which) {
            StopwatchTimer stopwatchTimer = this.mWifiMulticastTimer;
            if (stopwatchTimer == null) {
                return 0;
            }
            return stopwatchTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        public Timer getAudioTurnedOnTimer() {
            return this.mAudioTurnedOnTimer;
        }

        public Timer getVideoTurnedOnTimer() {
            return this.mVideoTurnedOnTimer;
        }

        public Timer getFlashlightTurnedOnTimer() {
            return this.mFlashlightTurnedOnTimer;
        }

        public Timer getCameraTurnedOnTimer() {
            return this.mCameraTurnedOnTimer;
        }

        public Timer getForegroundActivityTimer() {
            return this.mForegroundActivityTimer;
        }

        public Timer getForegroundServiceTimer() {
            return this.mForegroundServiceTimer;
        }

        public Timer getBluetoothScanTimer() {
            return this.mBluetoothScanTimer;
        }

        public Timer getBluetoothScanBackgroundTimer() {
            DualTimer dualTimer = this.mBluetoothScanTimer;
            if (dualTimer == null) {
                return null;
            }
            return dualTimer.getSubTimer();
        }

        public Timer getBluetoothUnoptimizedScanTimer() {
            return this.mBluetoothUnoptimizedScanTimer;
        }

        public Timer getBluetoothUnoptimizedScanBackgroundTimer() {
            DualTimer dualTimer = this.mBluetoothUnoptimizedScanTimer;
            if (dualTimer == null) {
                return null;
            }
            return dualTimer.getSubTimer();
        }

        public Counter getBluetoothScanResultCounter() {
            return this.mBluetoothScanResultCounter;
        }

        public Counter getBluetoothScanResultBgCounter() {
            return this.mBluetoothScanResultBgCounter;
        }

        /* Access modifiers changed, original: 0000 */
        public void makeProcessState(int i, Parcel in) {
            if (i >= 0 && i < 7) {
                BatteryStatsImpl.detachIfNotNull(this.mProcessStateTimer[i]);
                if (in == null) {
                    this.mProcessStateTimer[i] = new StopwatchTimer(this.mBsi.mClocks, this, 12, null, this.mBsi.mOnBatteryTimeBase);
                } else {
                    this.mProcessStateTimer[i] = new StopwatchTimer(this.mBsi.mClocks, this, 12, null, this.mBsi.mOnBatteryTimeBase, in);
                }
            }
        }

        public long getProcessStateTime(int state, long elapsedRealtimeUs, int which) {
            if (state < 0 || state >= 7) {
                return 0;
            }
            StopwatchTimer[] stopwatchTimerArr = this.mProcessStateTimer;
            if (stopwatchTimerArr[state] == null) {
                return 0;
            }
            return stopwatchTimerArr[state].getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        public Timer getProcessStateTimer(int state) {
            if (state < 0 || state >= 7) {
                return null;
            }
            return this.mProcessStateTimer[state];
        }

        public Timer getVibratorOnTimer() {
            return this.mVibratorOnTimer;
        }

        public void noteUserActivityLocked(int type) {
            if (this.mUserActivityCounters == null) {
                initUserActivityLocked();
            }
            if (type < 0 || type >= NUM_USER_ACTIVITY_TYPES) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown user activity type ");
                stringBuilder.append(type);
                stringBuilder.append(" was specified.");
                Slog.w(BatteryStatsImpl.TAG, stringBuilder.toString(), new Throwable());
                return;
            }
            this.mUserActivityCounters[type].stepAtomic();
        }

        public boolean hasUserActivity() {
            return this.mUserActivityCounters != null;
        }

        public int getUserActivityCount(int type, int which) {
            Counter[] counterArr = this.mUserActivityCounters;
            if (counterArr == null) {
                return 0;
            }
            return counterArr[type].getCountLocked(which);
        }

        /* Access modifiers changed, original: 0000 */
        public void makeWifiBatchedScanBin(int i, Parcel in) {
            if (i >= 0 && i < 5) {
                ArrayList<StopwatchTimer> collected = (ArrayList) this.mBsi.mWifiBatchedScanTimers.get(i);
                if (collected == null) {
                    collected = new ArrayList();
                    this.mBsi.mWifiBatchedScanTimers.put(i, collected);
                }
                BatteryStatsImpl.detachIfNotNull(this.mWifiBatchedScanTimer[i]);
                if (in == null) {
                    this.mWifiBatchedScanTimer[i] = new StopwatchTimer(this.mBsi.mClocks, this, 11, collected, this.mBsi.mOnBatteryTimeBase);
                } else {
                    this.mWifiBatchedScanTimer[i] = new StopwatchTimer(this.mBsi.mClocks, this, 11, collected, this.mBsi.mOnBatteryTimeBase, in);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void initUserActivityLocked() {
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs[]) this.mUserActivityCounters);
            this.mUserActivityCounters = new Counter[NUM_USER_ACTIVITY_TYPES];
            for (int i = 0; i < NUM_USER_ACTIVITY_TYPES; i++) {
                this.mUserActivityCounters[i] = new Counter(this.mBsi.mOnBatteryTimeBase);
            }
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
                Slog.w(BatteryStatsImpl.TAG, stringBuilder.toString(), new Throwable());
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

        public long getUserCpuTimeUs(int which) {
            return this.mUserCpuTime.getCountLocked(which);
        }

        public long getSystemCpuTimeUs(int which) {
            return this.mSystemCpuTime.getCountLocked(which);
        }

        public long getTimeAtCpuSpeed(int cluster, int step, int which) {
            LongSamplingCounter[] cpuSpeedTimesUs = this.mCpuClusterSpeedTimesUs;
            if (cpuSpeedTimesUs != null && cluster >= 0 && cluster < cpuSpeedTimesUs.length) {
                cpuSpeedTimesUs = cpuSpeedTimesUs[cluster];
                if (cpuSpeedTimesUs != null && step >= 0 && step < cpuSpeedTimesUs.length) {
                    LongSamplingCounter c = cpuSpeedTimesUs[step];
                    if (c != null) {
                        return c.getCountLocked(which);
                    }
                }
            }
            return 0;
        }

        public void noteMobileRadioApWakeupLocked() {
            if (this.mMobileRadioApWakeupCount == null) {
                this.mMobileRadioApWakeupCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            }
            this.mMobileRadioApWakeupCount.addCountLocked(1);
        }

        public long getMobileRadioApWakeupCount(int which) {
            LongSamplingCounter longSamplingCounter = this.mMobileRadioApWakeupCount;
            if (longSamplingCounter != null) {
                return longSamplingCounter.getCountLocked(which);
            }
            return 0;
        }

        public void noteWifiRadioApWakeupLocked() {
            if (this.mWifiRadioApWakeupCount == null) {
                this.mWifiRadioApWakeupCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            }
            this.mWifiRadioApWakeupCount.addCountLocked(1);
        }

        public long getWifiRadioApWakeupCount(int which) {
            LongSamplingCounter longSamplingCounter = this.mWifiRadioApWakeupCount;
            if (longSamplingCounter != null) {
                return longSamplingCounter.getCountLocked(which);
            }
            return 0;
        }

        public void getDeferredJobsCheckinLineLocked(StringBuilder sb, int which) {
            sb.setLength(0);
            int deferredEventCount = this.mJobsDeferredEventCount.getCountLocked(which);
            if (deferredEventCount != 0) {
                int deferredCount = this.mJobsDeferredCount.getCountLocked(which);
                long totalLatency = this.mJobsFreshnessTimeMs.getCountLocked(which);
                sb.append(deferredEventCount);
                sb.append(',');
                sb.append(deferredCount);
                sb.append(',');
                sb.append(totalLatency);
                for (int i = 0; i < BatteryStats.JOB_FRESHNESS_BUCKETS.length; i++) {
                    if (this.mJobsFreshnessBuckets[i] == null) {
                        sb.append(",0");
                    } else {
                        sb.append(",");
                        sb.append(this.mJobsFreshnessBuckets[i].getCountLocked(which));
                    }
                }
            }
        }

        public void getDeferredJobsLineLocked(StringBuilder sb, int which) {
            sb.setLength(0);
            int deferredEventCount = this.mJobsDeferredEventCount.getCountLocked(which);
            if (deferredEventCount != 0) {
                int deferredCount = this.mJobsDeferredCount.getCountLocked(which);
                long totalLatency = this.mJobsFreshnessTimeMs.getCountLocked(which);
                sb.append("times=");
                sb.append(deferredEventCount);
                String str = ", ";
                sb.append(str);
                sb.append("count=");
                sb.append(deferredCount);
                sb.append(str);
                sb.append("totalLatencyMs=");
                sb.append(totalLatency);
                sb.append(str);
                for (int i = 0; i < BatteryStats.JOB_FRESHNESS_BUCKETS.length; i++) {
                    sb.append("<");
                    sb.append(BatteryStats.JOB_FRESHNESS_BUCKETS[i]);
                    sb.append("ms=");
                    Counter[] counterArr = this.mJobsFreshnessBuckets;
                    if (counterArr[i] == null) {
                        sb.append("0");
                    } else {
                        sb.append(counterArr[i].getCountLocked(which));
                    }
                    sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void initNetworkActivityLocked() {
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs[]) this.mNetworkByteActivityCounters);
            this.mNetworkByteActivityCounters = new LongSamplingCounter[10];
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs[]) this.mNetworkPacketActivityCounters);
            this.mNetworkPacketActivityCounters = new LongSamplingCounter[10];
            for (int i = 0; i < 10; i++) {
                this.mNetworkByteActivityCounters[i] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
                this.mNetworkPacketActivityCounters[i] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            }
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mMobileRadioActiveTime);
            this.mMobileRadioActiveTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mMobileRadioActiveCount);
            this.mMobileRadioActiveCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
        }

        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public boolean reset(long uptime, long realtime) {
            int i;
            int ij;
            boolean active = false;
            this.mOnBatteryBackgroundTimeBase.init(uptime, realtime);
            this.mOnBatteryScreenOffBackgroundTimeBase.init(uptime, realtime);
            StopwatchTimer stopwatchTimer = this.mWifiRunningTimer;
            if (stopwatchTimer != null) {
                active = (false | (stopwatchTimer.reset(false) ^ 1)) | this.mWifiRunning;
            }
            stopwatchTimer = this.mFullWifiLockTimer;
            if (stopwatchTimer != null) {
                active = (active | (stopwatchTimer.reset(false) ^ 1)) | this.mFullWifiLockOut;
            }
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer != null) {
                active = (active | (dualTimer.reset(false) ^ 1)) | this.mWifiScanStarted;
            }
            if (this.mWifiBatchedScanTimer != null) {
                for (i = 0; i < 5; i++) {
                    StopwatchTimer[] stopwatchTimerArr = this.mWifiBatchedScanTimer;
                    if (stopwatchTimerArr[i] != null) {
                        active |= stopwatchTimerArr[i].reset(false) ^ 1;
                    }
                }
                active |= this.mWifiBatchedScanBinStarted != -1 ? 1 : 0;
            }
            stopwatchTimer = this.mWifiMulticastTimer;
            if (stopwatchTimer != null) {
                active = (active | (stopwatchTimer.reset(false) ^ 1)) | (this.mWifiMulticastWakelockCount > 0 ? 1 : 0);
            }
            active = ((((((((active | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mAudioTurnedOnTimer, false) ^ 1)) | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mVideoTurnedOnTimer, false) ^ 1)) | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mFlashlightTurnedOnTimer, false) ^ 1)) | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mCameraTurnedOnTimer, false) ^ 1)) | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mForegroundActivityTimer, false) ^ 1)) | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mForegroundServiceTimer, false) ^ 1)) | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mAggregatedPartialWakelockTimer, false) ^ 1)) | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mBluetoothScanTimer, false) ^ 1)) | (BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mBluetoothUnoptimizedScanTimer, false) ^ 1);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mBluetoothScanResultCounter, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mBluetoothScanResultBgCounter, false);
            if (this.mProcessStateTimer != null) {
                for (i = 0; i < 7; i++) {
                    active |= BatteryStatsImpl.resetIfNotNull(this.mProcessStateTimer[i], false) ^ 1;
                }
                active |= this.mProcessState != 21 ? 1 : 0;
            }
            BatchTimer batchTimer = this.mVibratorOnTimer;
            if (batchTimer != null) {
                if (batchTimer.reset(false)) {
                    this.mVibratorOnTimer.detach();
                    this.mVibratorOnTimer = null;
                } else {
                    active = true;
                }
            }
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[]) this.mUserActivityCounters, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[]) this.mNetworkByteActivityCounters, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[]) this.mNetworkPacketActivityCounters, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mMobileRadioActiveTime, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mMobileRadioActiveCount, false);
            BatteryStatsImpl.resetIfNotNull(this.mWifiControllerActivity, false);
            BatteryStatsImpl.resetIfNotNull(this.mBluetoothControllerActivity, false);
            BatteryStatsImpl.resetIfNotNull(this.mModemControllerActivity, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mUserCpuTime, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mSystemCpuTime, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[][]) this.mCpuClusterSpeedTimesUs, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mCpuFreqTimeMs, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mScreenOffCpuFreqTimeMs, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mCpuActiveTimeMs, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mCpuClusterTimesMs, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[]) this.mProcStateTimeMs, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[]) this.mProcStateScreenOffTimeMs, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mMobileRadioApWakeupCount, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mWifiRadioApWakeupCount, false);
            ArrayMap<String, Wakelock> wakeStats = this.mWakelockStats.getMap();
            for (int iw = wakeStats.size() - 1; iw >= 0; iw--) {
                if (((Wakelock) wakeStats.valueAt(iw)).reset()) {
                    wakeStats.removeAt(iw);
                } else {
                    active = true;
                }
            }
            this.mWakelockStats.cleanup();
            ArrayMap<String, DualTimer> syncStats = this.mSyncStats.getMap();
            for (int is = syncStats.size() - 1; is >= 0; is--) {
                DualTimer timer = (DualTimer) syncStats.valueAt(is);
                if (timer.reset(false)) {
                    syncStats.removeAt(is);
                    timer.detach();
                } else {
                    active = true;
                }
            }
            this.mSyncStats.cleanup();
            ArrayMap<String, DualTimer> jobStats = this.mJobStats.getMap();
            for (ij = jobStats.size() - 1; ij >= 0; ij--) {
                DualTimer timer2 = (DualTimer) jobStats.valueAt(ij);
                if (timer2.reset(false)) {
                    jobStats.removeAt(ij);
                    timer2.detach();
                } else {
                    active = true;
                }
            }
            this.mJobStats.cleanup();
            this.mJobCompletions.clear();
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mJobsDeferredEventCount, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mJobsDeferredCount, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs) this.mJobsFreshnessTimeMs, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[]) this.mJobsFreshnessBuckets, false);
            for (ij = this.mSensorStats.size() - 1; ij >= 0; ij--) {
                if (((Sensor) this.mSensorStats.valueAt(ij)).reset()) {
                    this.mSensorStats.removeAt(ij);
                } else {
                    active = true;
                }
            }
            for (ij = this.mProcessStats.size() - 1; ij >= 0; ij--) {
                ((Proc) this.mProcessStats.valueAt(ij)).detach();
            }
            this.mProcessStats.clear();
            for (ij = this.mPids.size() - 1; ij >= 0; ij--) {
                if (((Pid) this.mPids.valueAt(ij)).mWakeNesting > 0) {
                    active = true;
                } else {
                    this.mPids.removeAt(ij);
                }
            }
            for (ij = this.mPackageStats.size() - 1; ij >= 0; ij--) {
                ((Pkg) this.mPackageStats.valueAt(ij)).detach();
            }
            this.mPackageStats.clear();
            this.mLastStepSystemTime = 0;
            this.mLastStepUserTime = 0;
            this.mCurStepSystemTime = 0;
            this.mCurStepUserTime = 0;
            if (active) {
                return false;
            }
            return true;
        }

        /* Access modifiers changed, original: 0000 */
        public void detachFromTimeBase() {
            int ij;
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mWifiRunningTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mFullWifiLockTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mWifiScanTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs[]) this.mWifiBatchedScanTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mWifiMulticastTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mAudioTurnedOnTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mVideoTurnedOnTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mFlashlightTurnedOnTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mCameraTurnedOnTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mForegroundActivityTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mForegroundServiceTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mAggregatedPartialWakelockTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mBluetoothScanTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mBluetoothUnoptimizedScanTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mBluetoothScanResultCounter);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mBluetoothScanResultBgCounter);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs[]) this.mProcessStateTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mVibratorOnTimer);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs[]) this.mUserActivityCounters);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs[]) this.mNetworkByteActivityCounters);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs[]) this.mNetworkPacketActivityCounters);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mMobileRadioActiveTime);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mMobileRadioActiveCount);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mMobileRadioApWakeupCount);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mWifiRadioApWakeupCount);
            BatteryStatsImpl.detachIfNotNull(this.mWifiControllerActivity);
            BatteryStatsImpl.detachIfNotNull(this.mBluetoothControllerActivity);
            BatteryStatsImpl.detachIfNotNull(this.mModemControllerActivity);
            this.mPids.clear();
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mUserCpuTime);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mSystemCpuTime);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs[][]) this.mCpuClusterSpeedTimesUs);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mCpuActiveTimeMs);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mCpuFreqTimeMs);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mScreenOffCpuFreqTimeMs);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mCpuClusterTimesMs);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs[]) this.mProcStateTimeMs);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs[]) this.mProcStateScreenOffTimeMs);
            ArrayMap<String, Wakelock> wakeStats = this.mWakelockStats.getMap();
            for (int iw = wakeStats.size() - 1; iw >= 0; iw--) {
                ((Wakelock) wakeStats.valueAt(iw)).detachFromTimeBase();
            }
            ArrayMap<String, DualTimer> syncStats = this.mSyncStats.getMap();
            for (int is = syncStats.size() - 1; is >= 0; is--) {
                BatteryStatsImpl.detachIfNotNull((TimeBaseObs) (DualTimer) syncStats.valueAt(is));
            }
            ArrayMap<String, DualTimer> jobStats = this.mJobStats.getMap();
            for (ij = jobStats.size() - 1; ij >= 0; ij--) {
                BatteryStatsImpl.detachIfNotNull((TimeBaseObs) (DualTimer) jobStats.valueAt(ij));
            }
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mJobsDeferredEventCount);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mJobsDeferredCount);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs) this.mJobsFreshnessTimeMs);
            BatteryStatsImpl.detachIfNotNull((TimeBaseObs[]) this.mJobsFreshnessBuckets);
            for (ij = this.mSensorStats.size() - 1; ij >= 0; ij--) {
                ((Sensor) this.mSensorStats.valueAt(ij)).detachFromTimeBase();
            }
            for (ij = this.mProcessStats.size() - 1; ij >= 0; ij--) {
                ((Proc) this.mProcessStats.valueAt(ij)).detach();
            }
            this.mProcessStats.clear();
            for (ij = this.mPackageStats.size() - 1; ij >= 0; ij--) {
                ((Pkg) this.mPackageStats.valueAt(ij)).detach();
            }
            this.mPackageStats.clear();
        }

        /* Access modifiers changed, original: 0000 */
        public void writeJobCompletionsToParcelLocked(Parcel out) {
            int NJC = this.mJobCompletions.size();
            out.writeInt(NJC);
            for (int ijc = 0; ijc < NJC; ijc++) {
                out.writeString((String) this.mJobCompletions.keyAt(ijc));
                SparseIntArray types = (SparseIntArray) this.mJobCompletions.valueAt(ijc);
                int NT = types.size();
                out.writeInt(NT);
                for (int it = 0; it < NT; it++) {
                    out.writeInt(types.keyAt(it));
                    out.writeInt(types.valueAt(it));
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void writeToParcelLocked(Parcel out, long uptimeUs, long elapsedRealtimeUs) {
            int iw;
            int ij;
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
            ArrayMap<String, DualTimer> syncStats = this.mSyncStats.getMap();
            int NS = syncStats.size();
            parcel.writeInt(NS);
            for (int is = 0; is < NS; is++) {
                parcel.writeString((String) syncStats.keyAt(is));
                Timer.writeTimerToParcel(parcel, (DualTimer) syncStats.valueAt(is), j);
            }
            ArrayMap<String, DualTimer> jobStats = this.mJobStats.getMap();
            int NJ = jobStats.size();
            parcel.writeInt(NJ);
            for (ij = 0; ij < NJ; ij++) {
                parcel.writeString((String) jobStats.keyAt(ij));
                Timer.writeTimerToParcel(parcel, (DualTimer) jobStats.valueAt(ij), j);
            }
            writeJobCompletionsToParcelLocked(out);
            this.mJobsDeferredEventCount.writeToParcel(parcel);
            this.mJobsDeferredCount.writeToParcel(parcel);
            this.mJobsFreshnessTimeMs.writeToParcel(parcel);
            for (ij = 0; ij < BatteryStats.JOB_FRESHNESS_BUCKETS.length; ij++) {
                Counter.writeCounterToParcel(parcel, this.mJobsFreshnessBuckets[ij]);
            }
            ij = this.mSensorStats.size();
            parcel.writeInt(ij);
            for (ise = 0; ise < ij; ise++) {
                parcel.writeInt(this.mSensorStats.keyAt(ise));
                ((Sensor) this.mSensorStats.valueAt(ise)).writeToParcelLocked(parcel, j);
            }
            ise = this.mProcessStats.size();
            parcel.writeInt(ise);
            for (ip = 0; ip < ise; ip++) {
                parcel.writeString((String) this.mProcessStats.keyAt(ip));
                ((Proc) this.mProcessStats.valueAt(ip)).writeToParcelLocked(parcel);
            }
            parcel.writeInt(this.mPackageStats.size());
            for (Entry<String, Pkg> pkgEntry : this.mPackageStats.entrySet()) {
                parcel.writeString((String) pkgEntry.getKey());
                ((Pkg) pkgEntry.getValue()).writeToParcelLocked(parcel);
            }
            int i = 0;
            if (this.mWifiRunningTimer != null) {
                parcel.writeInt(1);
                this.mWifiRunningTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mFullWifiLockTimer != null) {
                parcel.writeInt(1);
                this.mFullWifiLockTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mWifiScanTimer != null) {
                parcel.writeInt(1);
                this.mWifiScanTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            for (ip = 0; ip < 5; ip++) {
                if (this.mWifiBatchedScanTimer[ip] != null) {
                    parcel.writeInt(1);
                    this.mWifiBatchedScanTimer[ip].writeToParcel(parcel, j);
                } else {
                    parcel.writeInt(0);
                }
            }
            if (this.mWifiMulticastTimer != null) {
                parcel.writeInt(1);
                this.mWifiMulticastTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mAudioTurnedOnTimer != null) {
                parcel.writeInt(1);
                this.mAudioTurnedOnTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mVideoTurnedOnTimer != null) {
                parcel.writeInt(1);
                this.mVideoTurnedOnTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mFlashlightTurnedOnTimer != null) {
                parcel.writeInt(1);
                this.mFlashlightTurnedOnTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mCameraTurnedOnTimer != null) {
                parcel.writeInt(1);
                this.mCameraTurnedOnTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mForegroundActivityTimer != null) {
                parcel.writeInt(1);
                this.mForegroundActivityTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mForegroundServiceTimer != null) {
                parcel.writeInt(1);
                this.mForegroundServiceTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mAggregatedPartialWakelockTimer != null) {
                parcel.writeInt(1);
                this.mAggregatedPartialWakelockTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mBluetoothScanTimer != null) {
                parcel.writeInt(1);
                this.mBluetoothScanTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mBluetoothUnoptimizedScanTimer != null) {
                parcel.writeInt(1);
                this.mBluetoothUnoptimizedScanTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mBluetoothScanResultCounter != null) {
                parcel.writeInt(1);
                this.mBluetoothScanResultCounter.writeToParcel(parcel);
            } else {
                parcel.writeInt(0);
            }
            if (this.mBluetoothScanResultBgCounter != null) {
                parcel.writeInt(1);
                this.mBluetoothScanResultBgCounter.writeToParcel(parcel);
            } else {
                parcel.writeInt(0);
            }
            for (ip = 0; ip < 7; ip++) {
                if (this.mProcessStateTimer[ip] != null) {
                    parcel.writeInt(1);
                    this.mProcessStateTimer[ip].writeToParcel(parcel, j);
                } else {
                    parcel.writeInt(0);
                }
            }
            if (this.mVibratorOnTimer != null) {
                parcel.writeInt(1);
                this.mVibratorOnTimer.writeToParcel(parcel, j);
            } else {
                parcel.writeInt(0);
            }
            if (this.mUserActivityCounters != null) {
                parcel.writeInt(1);
                for (ip = 0; ip < NUM_USER_ACTIVITY_TYPES; ip++) {
                    this.mUserActivityCounters[ip].writeToParcel(parcel);
                }
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
            if (this.mBluetoothControllerActivity != null) {
                parcel.writeInt(1);
                this.mBluetoothControllerActivity.writeToParcel(parcel, 0);
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
            int NW2;
            ArrayMap<String, DualTimer> syncStats2;
            if (this.mCpuClusterSpeedTimesUs != null) {
                parcel.writeInt(1);
                parcel.writeInt(this.mCpuClusterSpeedTimesUs.length);
                LongSamplingCounter[][] longSamplingCounterArr = this.mCpuClusterSpeedTimesUs;
                int length = longSamplingCounterArr.length;
                while (i < length) {
                    ArrayMap<String, Wakelock> wakeStats2;
                    LongSamplingCounter[] cpuSpeeds = longSamplingCounterArr[i];
                    if (cpuSpeeds != null) {
                        wakeStats2 = wakeStats;
                        parcel.writeInt(1);
                        parcel.writeInt(cpuSpeeds.length);
                        wakeStats = cpuSpeeds.length;
                        NW2 = NW;
                        NW = 0;
                        while (NW < wakeStats) {
                            ArrayMap<String, Wakelock> arrayMap = wakeStats;
                            wakeStats = cpuSpeeds[NW];
                            if (wakeStats != null) {
                                syncStats2 = syncStats;
                                parcel.writeInt(1);
                                wakeStats.writeToParcel(parcel);
                            } else {
                                syncStats2 = syncStats;
                                parcel.writeInt(0);
                            }
                            NW++;
                            wakeStats = arrayMap;
                            syncStats = syncStats2;
                        }
                        syncStats2 = syncStats;
                    } else {
                        wakeStats2 = wakeStats;
                        NW2 = NW;
                        syncStats2 = syncStats;
                        parcel.writeInt(null);
                    }
                    i++;
                    wakeStats = wakeStats2;
                    NW = NW2;
                    syncStats = syncStats2;
                }
                NW2 = NW;
                syncStats2 = syncStats;
            } else {
                NW2 = NW;
                syncStats2 = syncStats;
                parcel.writeInt(null);
            }
            LongSamplingCounterArray.writeToParcel(parcel, this.mCpuFreqTimeMs);
            LongSamplingCounterArray.writeToParcel(parcel, this.mScreenOffCpuFreqTimeMs);
            this.mCpuActiveTimeMs.writeToParcel(parcel);
            this.mCpuClusterTimesMs.writeToParcel(parcel);
            LongSamplingCounterArray[] longSamplingCounterArrayArr = this.mProcStateTimeMs;
            if (longSamplingCounterArrayArr != null) {
                parcel.writeInt(longSamplingCounterArrayArr.length);
                for (LongSamplingCounterArray counters : this.mProcStateTimeMs) {
                    LongSamplingCounterArray.writeToParcel(parcel, counters);
                }
            } else {
                parcel.writeInt(0);
            }
            longSamplingCounterArrayArr = this.mProcStateScreenOffTimeMs;
            if (longSamplingCounterArrayArr != null) {
                parcel.writeInt(longSamplingCounterArrayArr.length);
                for (LongSamplingCounterArray counters2 : this.mProcStateScreenOffTimeMs) {
                    LongSamplingCounterArray.writeToParcel(parcel, counters2);
                }
            } else {
                parcel.writeInt(0);
            }
            if (this.mMobileRadioApWakeupCount != null) {
                parcel.writeInt(1);
                this.mMobileRadioApWakeupCount.writeToParcel(parcel);
            } else {
                parcel.writeInt(0);
            }
            if (this.mWifiRadioApWakeupCount != null) {
                parcel.writeInt(1);
                this.mWifiRadioApWakeupCount.writeToParcel(parcel);
                return;
            }
            parcel.writeInt(0);
        }

        /* Access modifiers changed, original: 0000 */
        public void readJobCompletionsFromParcelLocked(Parcel in) {
            int numJobCompletions = in.readInt();
            this.mJobCompletions.clear();
            for (int j = 0; j < numJobCompletions; j++) {
                String jobName = in.readString();
                int numTypes = in.readInt();
                if (numTypes > 0) {
                    SparseIntArray types = new SparseIntArray();
                    for (int k = 0; k < numTypes; k++) {
                        types.put(in.readInt(), in.readInt());
                    }
                    this.mJobCompletions.put(jobName, types);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void readFromParcelLocked(TimeBase timeBase, TimeBase screenOffTimeBase, Parcel in) {
            int j;
            String wakelockName;
            String syncName;
            OverflowArrayMap overflowArrayMap;
            Clocks clocks;
            TimeBase timeBase2;
            DualTimer dualTimer;
            TimeBase timeBase3;
            OverflowArrayMap overflowArrayMap2;
            int sensorNumber;
            StopwatchTimer stopwatchTimer;
            int i;
            Parcel parcel = in;
            this.mOnBatteryBackgroundTimeBase.readFromParcel(parcel);
            this.mOnBatteryScreenOffBackgroundTimeBase.readFromParcel(parcel);
            int numWakelocks = in.readInt();
            this.mWakelockStats.clear();
            for (j = 0; j < numWakelocks; j++) {
                wakelockName = in.readString();
                Wakelock wakelock = new Wakelock(this.mBsi, this);
                wakelock.readFromParcelLocked(timeBase, screenOffTimeBase, this.mOnBatteryScreenOffBackgroundTimeBase, parcel);
                this.mWakelockStats.add(wakelockName, wakelock);
            }
            TimeBase timeBase4 = timeBase;
            TimeBase timeBase5 = screenOffTimeBase;
            int numSyncs = in.readInt();
            this.mSyncStats.clear();
            int j2 = 0;
            while (j2 < numSyncs) {
                int numWakelocks2;
                syncName = in.readString();
                if (in.readInt() != 0) {
                    overflowArrayMap = this.mSyncStats;
                    clocks = this.mBsi.mClocks;
                    timeBase2 = this.mBsi.mOnBatteryTimeBase;
                    DualTimer dualTimer2 = dualTimer;
                    timeBase3 = this.mOnBatteryBackgroundTimeBase;
                    numWakelocks2 = numWakelocks;
                    overflowArrayMap2 = overflowArrayMap;
                    dualTimer = new DualTimer(clocks, this, 13, null, timeBase2, timeBase3, in);
                    overflowArrayMap2.add(syncName, dualTimer2);
                } else {
                    numWakelocks2 = numWakelocks;
                }
                j2++;
                numWakelocks = numWakelocks2;
            }
            numWakelocks = in.readInt();
            this.mJobStats.clear();
            j2 = 0;
            while (j2 < numWakelocks) {
                int numJobs;
                syncName = in.readString();
                if (in.readInt() != 0) {
                    overflowArrayMap = this.mJobStats;
                    clocks = this.mBsi.mClocks;
                    timeBase2 = this.mBsi.mOnBatteryTimeBase;
                    DualTimer dualTimer3 = dualTimer;
                    timeBase3 = this.mOnBatteryBackgroundTimeBase;
                    numJobs = numWakelocks;
                    overflowArrayMap2 = overflowArrayMap;
                    dualTimer = new DualTimer(clocks, this, 14, null, timeBase2, timeBase3, in);
                    overflowArrayMap2.add(syncName, dualTimer3);
                } else {
                    numJobs = numWakelocks;
                }
                j2++;
                numWakelocks = numJobs;
            }
            readJobCompletionsFromParcelLocked(parcel);
            this.mJobsDeferredEventCount = new Counter(this.mBsi.mOnBatteryTimeBase, parcel);
            this.mJobsDeferredCount = new Counter(this.mBsi.mOnBatteryTimeBase, parcel);
            this.mJobsFreshnessTimeMs = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, parcel);
            for (j = 0; j < BatteryStats.JOB_FRESHNESS_BUCKETS.length; j++) {
                this.mJobsFreshnessBuckets[j] = Counter.readCounterFromParcel(this.mBsi.mOnBatteryTimeBase, parcel);
            }
            numWakelocks = in.readInt();
            this.mSensorStats.clear();
            for (j = 0; j < numWakelocks; j++) {
                sensorNumber = in.readInt();
                Sensor sensor = new Sensor(this.mBsi, this, sensorNumber);
                sensor.readFromParcelLocked(this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase, parcel);
                this.mSensorStats.put(sensorNumber, sensor);
            }
            j2 = in.readInt();
            this.mProcessStats.clear();
            for (j = 0; j < j2; j++) {
                wakelockName = in.readString();
                Proc proc = new Proc(this.mBsi, wakelockName);
                proc.readFromParcelLocked(parcel);
                this.mProcessStats.put(wakelockName, proc);
            }
            int numPkgs = in.readInt();
            this.mPackageStats.clear();
            for (j = 0; j < numPkgs; j++) {
                wakelockName = in.readString();
                Pkg pkg = new Pkg(this.mBsi);
                pkg.readFromParcelLocked(parcel);
                this.mPackageStats.put(wakelockName, pkg);
            }
            this.mWifiRunning = false;
            if (in.readInt() != 0) {
                clocks = this.mBsi.mClocks;
                ArrayList arrayList = this.mBsi.mWifiRunningTimers;
                StopwatchTimer stopwatchTimer2 = r0;
                timeBase2 = this.mBsi.mOnBatteryTimeBase;
                stopwatchTimer = null;
                StopwatchTimer stopwatchTimer3 = new StopwatchTimer(clocks, this, 4, arrayList, timeBase2, in);
                this.mWifiRunningTimer = stopwatchTimer2;
            } else {
                stopwatchTimer = null;
                this.mWifiRunningTimer = stopwatchTimer;
            }
            this.mFullWifiLockOut = false;
            if (in.readInt() != 0) {
                this.mFullWifiLockTimer = new StopwatchTimer(this.mBsi.mClocks, this, 5, this.mBsi.mFullWifiLockTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mFullWifiLockTimer = stopwatchTimer;
            }
            this.mWifiScanStarted = false;
            if (in.readInt() != 0) {
                DualTimer dualTimer4 = dualTimer;
                i = 0;
                dualTimer = new DualTimer(this.mBsi.mClocks, this, 6, this.mBsi.mWifiScanTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase, in);
                this.mWifiScanTimer = dualTimer4;
            } else {
                i = 0;
                this.mWifiScanTimer = null;
            }
            this.mWifiBatchedScanBinStarted = -1;
            for (j = 0; j < 5; j++) {
                if (in.readInt() != 0) {
                    makeWifiBatchedScanBin(j, parcel);
                } else {
                    this.mWifiBatchedScanTimer[j] = null;
                }
            }
            this.mWifiMulticastWakelockCount = i;
            if (in.readInt() != 0) {
                this.mWifiMulticastTimer = new StopwatchTimer(this.mBsi.mClocks, this, 7, this.mBsi.mWifiMulticastTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mWifiMulticastTimer = null;
            }
            if (in.readInt() != 0) {
                this.mAudioTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 15, this.mBsi.mAudioTurnedOnTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mAudioTurnedOnTimer = null;
            }
            if (in.readInt() != 0) {
                this.mVideoTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 8, this.mBsi.mVideoTurnedOnTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mVideoTurnedOnTimer = null;
            }
            if (in.readInt() != 0) {
                this.mFlashlightTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 16, this.mBsi.mFlashlightTurnedOnTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mFlashlightTurnedOnTimer = null;
            }
            if (in.readInt() != 0) {
                this.mCameraTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 17, this.mBsi.mCameraTurnedOnTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mCameraTurnedOnTimer = null;
            }
            if (in.readInt() != 0) {
                this.mForegroundActivityTimer = new StopwatchTimer(this.mBsi.mClocks, this, 10, null, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mForegroundActivityTimer = null;
            }
            if (in.readInt() != 0) {
                this.mForegroundServiceTimer = new StopwatchTimer(this.mBsi.mClocks, this, 22, null, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mForegroundServiceTimer = null;
            }
            if (in.readInt() != 0) {
                this.mAggregatedPartialWakelockTimer = new DualTimer(this.mBsi.mClocks, this, 20, null, this.mBsi.mOnBatteryScreenOffTimeBase, this.mOnBatteryScreenOffBackgroundTimeBase, in);
            } else {
                this.mAggregatedPartialWakelockTimer = null;
            }
            if (in.readInt() != 0) {
                this.mBluetoothScanTimer = new DualTimer(this.mBsi.mClocks, this, 19, this.mBsi.mBluetoothScanOnTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase, in);
            } else {
                this.mBluetoothScanTimer = null;
            }
            if (in.readInt() != 0) {
                this.mBluetoothUnoptimizedScanTimer = new DualTimer(this.mBsi.mClocks, this, 21, null, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase, in);
            } else {
                this.mBluetoothUnoptimizedScanTimer = null;
            }
            if (in.readInt() != 0) {
                this.mBluetoothScanResultCounter = new Counter(this.mBsi.mOnBatteryTimeBase, parcel);
            } else {
                this.mBluetoothScanResultCounter = null;
            }
            if (in.readInt() != 0) {
                this.mBluetoothScanResultBgCounter = new Counter(this.mOnBatteryBackgroundTimeBase, parcel);
            } else {
                this.mBluetoothScanResultBgCounter = null;
            }
            this.mProcessState = 21;
            for (j = 0; j < 7; j++) {
                if (in.readInt() != 0) {
                    makeProcessState(j, parcel);
                } else {
                    this.mProcessStateTimer[j] = null;
                }
            }
            if (in.readInt() != 0) {
                this.mVibratorOnTimer = new BatchTimer(this.mBsi.mClocks, this, 9, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mVibratorOnTimer = null;
            }
            if (in.readInt() != 0) {
                this.mUserActivityCounters = new Counter[NUM_USER_ACTIVITY_TYPES];
                for (j = 0; j < NUM_USER_ACTIVITY_TYPES; j++) {
                    this.mUserActivityCounters[j] = new Counter(this.mBsi.mOnBatteryTimeBase, parcel);
                }
            } else {
                this.mUserActivityCounters = null;
            }
            if (in.readInt() != 0) {
                this.mNetworkByteActivityCounters = new LongSamplingCounter[10];
                this.mNetworkPacketActivityCounters = new LongSamplingCounter[10];
                for (sensorNumber = 0; sensorNumber < 10; sensorNumber++) {
                    this.mNetworkByteActivityCounters[sensorNumber] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, parcel);
                    this.mNetworkPacketActivityCounters[sensorNumber] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, parcel);
                }
                this.mMobileRadioActiveTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, parcel);
                this.mMobileRadioActiveCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, parcel);
            } else {
                this.mNetworkByteActivityCounters = null;
                this.mNetworkPacketActivityCounters = null;
            }
            if (in.readInt() != 0) {
                this.mWifiControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 1, parcel);
            } else {
                this.mWifiControllerActivity = null;
            }
            if (in.readInt() != 0) {
                this.mBluetoothControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 1, parcel);
            } else {
                this.mBluetoothControllerActivity = null;
            }
            if (in.readInt() != 0) {
                this.mModemControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 5, parcel);
            } else {
                this.mModemControllerActivity = null;
            }
            this.mUserCpuTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, parcel);
            this.mSystemCpuTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, parcel);
            if (in.readInt() != 0) {
                j = in.readInt();
                if (this.mBsi.mPowerProfile == null || this.mBsi.mPowerProfile.getNumCpuClusters() == j) {
                    this.mCpuClusterSpeedTimesUs = new LongSamplingCounter[j][];
                    sensorNumber = 0;
                    while (sensorNumber < j) {
                        if (in.readInt() != 0) {
                            int numSpeeds = in.readInt();
                            if (this.mBsi.mPowerProfile == null || this.mBsi.mPowerProfile.getNumSpeedStepsInCpuCluster(sensorNumber) == numSpeeds) {
                                LongSamplingCounter[] cpuSpeeds = new LongSamplingCounter[numSpeeds];
                                this.mCpuClusterSpeedTimesUs[sensorNumber] = cpuSpeeds;
                                for (int speed = 0; speed < numSpeeds; speed++) {
                                    if (in.readInt() != 0) {
                                        cpuSpeeds[speed] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, parcel);
                                    }
                                }
                            } else {
                                throw new ParcelFormatException("Incompatible number of cpu speeds");
                            }
                        }
                        this.mCpuClusterSpeedTimesUs[sensorNumber] = null;
                        sensorNumber++;
                    }
                } else {
                    throw new ParcelFormatException("Incompatible number of cpu clusters");
                }
            }
            this.mCpuClusterSpeedTimesUs = null;
            this.mCpuFreqTimeMs = LongSamplingCounterArray.readFromParcel(parcel, this.mBsi.mOnBatteryTimeBase);
            this.mScreenOffCpuFreqTimeMs = LongSamplingCounterArray.readFromParcel(parcel, this.mBsi.mOnBatteryScreenOffTimeBase);
            this.mCpuActiveTimeMs = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, parcel);
            this.mCpuClusterTimesMs = new LongSamplingCounterArray(this.mBsi.mOnBatteryTimeBase, parcel, null);
            j = in.readInt();
            if (j == 7) {
                this.mProcStateTimeMs = new LongSamplingCounterArray[j];
                for (sensorNumber = 0; sensorNumber < j; sensorNumber++) {
                    this.mProcStateTimeMs[sensorNumber] = LongSamplingCounterArray.readFromParcel(parcel, this.mBsi.mOnBatteryTimeBase);
                }
            } else {
                this.mProcStateTimeMs = null;
            }
            j = in.readInt();
            if (j == 7) {
                this.mProcStateScreenOffTimeMs = new LongSamplingCounterArray[j];
                for (sensorNumber = 0; sensorNumber < j; sensorNumber++) {
                    this.mProcStateScreenOffTimeMs[sensorNumber] = LongSamplingCounterArray.readFromParcel(parcel, this.mBsi.mOnBatteryScreenOffTimeBase);
                }
            } else {
                this.mProcStateScreenOffTimeMs = null;
            }
            if (in.readInt() != 0) {
                this.mMobileRadioApWakeupCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, parcel);
            } else {
                this.mMobileRadioApWakeupCount = null;
            }
            if (in.readInt() != 0) {
                this.mWifiRadioApWakeupCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, parcel);
            } else {
                this.mWifiRadioApWakeupCount = null;
            }
        }

        public void noteJobsDeferredLocked(int numDeferred, long sinceLast) {
            this.mJobsDeferredEventCount.addAtomic(1);
            this.mJobsDeferredCount.addAtomic(numDeferred);
            if (sinceLast != 0) {
                this.mJobsFreshnessTimeMs.addCountLocked(sinceLast);
                for (int i = 0; i < BatteryStats.JOB_FRESHNESS_BUCKETS.length; i++) {
                    if (sinceLast < BatteryStats.JOB_FRESHNESS_BUCKETS[i]) {
                        Counter[] counterArr = this.mJobsFreshnessBuckets;
                        if (counterArr[i] == null) {
                            counterArr[i] = new Counter(this.mBsi.mOnBatteryTimeBase);
                        }
                        this.mJobsFreshnessBuckets[i].addAtomic(1);
                        return;
                    }
                }
            }
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

        @GuardedBy({"mBsi"})
        public void updateUidProcessStateLocked(int procState) {
            boolean userAwareService = ActivityManager.isForegroundService(procState);
            int uidRunningState = BatteryStats.mapToInternalProcessState(procState);
            if (this.mProcessState != uidRunningState || userAwareService != this.mInForegroundService) {
                long elapsedRealtimeMs = this.mBsi.mClocks.elapsedRealtime();
                if (this.mProcessState != uidRunningState) {
                    long uptimeMs = this.mBsi.mClocks.uptimeMillis();
                    int i = this.mProcessState;
                    if (i != 21) {
                        this.mProcessStateTimer[i].stopRunningLocked(elapsedRealtimeMs);
                        if (this.mBsi.trackPerProcStateCpuTimes()) {
                            if (this.mBsi.mPendingUids.size() == 0) {
                                this.mBsi.mExternalSync.scheduleReadProcStateCpuTimes(this.mBsi.mOnBatteryTimeBase.isRunning(), this.mBsi.mOnBatteryScreenOffTimeBase.isRunning(), this.mBsi.mConstants.PROC_STATE_CPU_TIMES_READ_DELAY_MS);
                                this.mBsi.mNumSingleUidCpuTimeReads = 1 + this.mBsi.mNumSingleUidCpuTimeReads;
                            } else {
                                this.mBsi.mNumBatchedSingleUidCpuTimeReads = 1 + this.mBsi.mNumBatchedSingleUidCpuTimeReads;
                            }
                            if (this.mBsi.mPendingUids.indexOfKey(this.mUid) < 0 || ArrayUtils.contains(CRITICAL_PROC_STATES, this.mProcessState)) {
                                this.mBsi.mPendingUids.put(this.mUid, this.mProcessState);
                            }
                        } else {
                            this.mBsi.mPendingUids.clear();
                        }
                    }
                    this.mProcessState = uidRunningState;
                    if (uidRunningState != 21) {
                        if (this.mProcessStateTimer[uidRunningState] == null) {
                            makeProcessState(uidRunningState, null);
                        }
                        this.mProcessStateTimer[uidRunningState].startRunningLocked(elapsedRealtimeMs);
                    }
                    updateOnBatteryBgTimeBase(uptimeMs * 1000, elapsedRealtimeMs * 1000);
                    updateOnBatteryScreenOffBgTimeBase(uptimeMs * 1000, 1000 * elapsedRealtimeMs);
                }
                if (userAwareService != this.mInForegroundService) {
                    if (userAwareService) {
                        noteForegroundServiceResumedLocked(elapsedRealtimeMs);
                    } else {
                        noteForegroundServicePausedLocked(elapsedRealtimeMs);
                    }
                    this.mInForegroundService = userAwareService;
                }
            }
        }

        public boolean isInBackground() {
            return this.mProcessState >= 3;
        }

        public boolean updateOnBatteryBgTimeBase(long uptimeUs, long realtimeUs) {
            boolean z = this.mBsi.mOnBatteryTimeBase.isRunning() && isInBackground();
            return this.mOnBatteryBackgroundTimeBase.setRunning(z, uptimeUs, realtimeUs);
        }

        public boolean updateOnBatteryScreenOffBgTimeBase(long uptimeUs, long realtimeUs) {
            boolean z = this.mBsi.mOnBatteryScreenOffTimeBase.isRunning() && isInBackground();
            return this.mOnBatteryScreenOffBackgroundTimeBase.setRunning(z, uptimeUs, realtimeUs);
        }

        public SparseArray<? extends Pid> getPidStats() {
            return this.mPids;
        }

        public Pid getPidStatsLocked(int pid) {
            Pid p = (Pid) this.mPids.get(pid);
            if (p != null) {
                return p;
            }
            p = new Pid();
            this.mPids.put(pid, p);
            return p;
        }

        public Pkg getPackageStatsLocked(String name) {
            Pkg ps = (Pkg) this.mPackageStats.get(name);
            if (ps != null) {
                return ps;
            }
            ps = new Pkg(this.mBsi);
            this.mPackageStats.put(name, ps);
            return ps;
        }

        public Serv getServiceStatsLocked(String pkg, String serv) {
            Pkg ps = getPackageStatsLocked(pkg);
            Serv ss = (Serv) ps.mServiceStats.get(serv);
            if (ss != null) {
                return ss;
            }
            ss = ps.newServiceStatsLocked();
            ps.mServiceStats.put(serv, ss);
            return ss;
        }

        public void readSyncSummaryFromParcelLocked(String name, Parcel in) {
            DualTimer timer = (DualTimer) this.mSyncStats.instantiateObject();
            timer.readSummaryFromParcelLocked(in);
            this.mSyncStats.add(name, timer);
        }

        public void readJobSummaryFromParcelLocked(String name, Parcel in) {
            DualTimer timer = (DualTimer) this.mJobStats.instantiateObject();
            timer.readSummaryFromParcelLocked(in);
            this.mJobStats.add(name, timer);
        }

        public void readWakeSummaryFromParcelLocked(String wlName, Parcel in) {
            Wakelock wl = new Wakelock(this.mBsi, this);
            this.mWakelockStats.add(wlName, wl);
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl, 1).readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl, 0).readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl, 2).readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl, 18).readSummaryFromParcelLocked(in);
            }
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
            ArrayList<StopwatchTimer> timers = (ArrayList) this.mBsi.mSensorTimers.get(sensor);
            if (timers == null) {
                timers = new ArrayList();
                this.mBsi.mSensorTimers.put(sensor, timers);
            }
            t = new DualTimer(this.mBsi.mClocks, this, 3, timers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            se.mTimer = t;
            return t;
        }

        public void noteStartSyncLocked(String name, long elapsedRealtimeMs) {
            DualTimer t = (DualTimer) this.mSyncStats.startObject(name);
            if (t != null) {
                t.startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteStopSyncLocked(String name, long elapsedRealtimeMs) {
            DualTimer t = (DualTimer) this.mSyncStats.stopObject(name);
            if (t != null) {
                t.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteStartJobLocked(String name, long elapsedRealtimeMs) {
            DualTimer t = (DualTimer) this.mJobStats.startObject(name);
            if (t != null) {
                t.startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteStopJobLocked(String name, long elapsedRealtimeMs, int stopReason) {
            DualTimer t = (DualTimer) this.mJobStats.stopObject(name);
            if (t != null) {
                t.stopRunningLocked(elapsedRealtimeMs);
            }
            if (this.mBsi.mOnBatteryTimeBase.isRunning()) {
                SparseIntArray types = (SparseIntArray) this.mJobCompletions.get(name);
                if (types == null) {
                    types = new SparseIntArray();
                    this.mJobCompletions.put(name, types);
                }
                types.put(stopReason, types.get(stopReason, 0) + 1);
            }
        }

        public StopwatchTimer getWakelockTimerLocked(Wakelock wl, int type) {
            if (wl == null) {
                return null;
            }
            StopwatchTimer t;
            if (type == 0) {
                DualTimer t2 = wl.mTimerPartial;
                if (t2 == null) {
                    t2 = new DualTimer(this.mBsi.mClocks, this, 0, this.mBsi.mPartialTimers, this.mBsi.mOnBatteryScreenOffTimeBase, this.mOnBatteryScreenOffBackgroundTimeBase);
                    wl.mTimerPartial = t2;
                }
                return t2;
            } else if (type == 1) {
                t = wl.mTimerFull;
                if (t == null) {
                    t = new StopwatchTimer(this.mBsi.mClocks, this, 1, this.mBsi.mFullTimers, this.mBsi.mOnBatteryTimeBase);
                    wl.mTimerFull = t;
                }
                return t;
            } else if (type == 2) {
                t = wl.mTimerWindow;
                if (t == null) {
                    t = new StopwatchTimer(this.mBsi.mClocks, this, 2, this.mBsi.mWindowTimers, this.mBsi.mOnBatteryTimeBase);
                    wl.mTimerWindow = t;
                }
                return t;
            } else if (type == 18) {
                t = wl.mTimerDraw;
                if (t == null) {
                    t = new StopwatchTimer(this.mBsi.mClocks, this, 18, this.mBsi.mDrawTimers, this.mBsi.mOnBatteryTimeBase);
                    wl.mTimerDraw = t;
                }
                return t;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("type=");
                stringBuilder.append(type);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        public void noteStartWakeLocked(int pid, String name, int type, long elapsedRealtimeMs) {
            Wakelock wl = (Wakelock) this.mWakelockStats.startObject(name);
            if (wl != null) {
                getWakelockTimerLocked(wl, type).startRunningLocked(elapsedRealtimeMs);
            }
            if (type == 0) {
                createAggregatedPartialWakelockTimerLocked().startRunningLocked(elapsedRealtimeMs);
                if (pid >= 0) {
                    Pid p = getPidStatsLocked(pid);
                    int i = p.mWakeNesting;
                    p.mWakeNesting = i + 1;
                    if (i == 0) {
                        p.mWakeStartMs = elapsedRealtimeMs;
                    }
                }
            }
        }

        public void noteStopWakeLocked(int pid, String name, int type, long elapsedRealtimeMs) {
            Wakelock wl = (Wakelock) this.mWakelockStats.stopObject(name);
            if (wl != null) {
                getWakelockTimerLocked(wl, type).stopRunningLocked(elapsedRealtimeMs);
            }
            if (type == 0) {
                DualTimer dualTimer = this.mAggregatedPartialWakelockTimer;
                if (dualTimer != null) {
                    dualTimer.stopRunningLocked(elapsedRealtimeMs);
                }
                if (pid >= 0) {
                    Pid p = (Pid) this.mPids.get(pid);
                    if (p != null && p.mWakeNesting > 0) {
                        int i = p.mWakeNesting;
                        p.mWakeNesting = i - 1;
                        if (i == 1) {
                            p.mWakeSumMs += elapsedRealtimeMs - p.mWakeStartMs;
                            p.mWakeStartMs = 0;
                        }
                    }
                }
            }
        }

        public void reportExcessiveCpuLocked(String proc, long overTime, long usedTime) {
            Proc p = getProcessStatsLocked(proc);
            if (p != null) {
                p.addExcessiveCpu(overTime, usedTime);
            }
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

        public void noteStartGps(long elapsedRealtimeMs) {
            noteStartSensor(-10000, elapsedRealtimeMs);
        }

        public void noteStopGps(long elapsedRealtimeMs) {
            noteStopSensor(-10000, elapsedRealtimeMs);
        }

        public BatteryStatsImpl getBatteryStats() {
            return this.mBsi;
        }
    }

    @VisibleForTesting
    public final class UidToRemove {
        int endUid;
        int startUid;
        long timeAddedInQueue;

        public UidToRemove(BatteryStatsImpl this$0, int uid, long timestamp) {
            this(uid, uid, timestamp);
        }

        public UidToRemove(int startUid, int endUid, long timestamp) {
            this.startUid = startUid;
            this.endUid = endUid;
            this.timeAddedInQueue = timestamp;
        }

        /* Access modifiers changed, original: 0000 */
        public void remove() {
            int i = this.startUid;
            int i2 = this.endUid;
            if (i == i2) {
                BatteryStatsImpl.this.mCpuUidUserSysTimeReader.removeUid(this.startUid);
                BatteryStatsImpl.this.mCpuUidFreqTimeReader.removeUid(this.startUid);
                if (BatteryStatsImpl.this.mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME) {
                    BatteryStatsImpl.this.mCpuUidActiveTimeReader.removeUid(this.startUid);
                    BatteryStatsImpl.this.mCpuUidClusterTimeReader.removeUid(this.startUid);
                }
                if (BatteryStatsImpl.this.mKernelSingleUidTimeReader != null) {
                    BatteryStatsImpl.this.mKernelSingleUidTimeReader.removeUid(this.startUid);
                }
                BatteryStatsImpl.this.mNumUidsRemoved = BatteryStatsImpl.this.mNumUidsRemoved + 1;
            } else if (i < i2) {
                BatteryStatsImpl.this.mCpuUidFreqTimeReader.removeUidsInRange(this.startUid, this.endUid);
                BatteryStatsImpl.this.mCpuUidUserSysTimeReader.removeUidsInRange(this.startUid, this.endUid);
                if (BatteryStatsImpl.this.mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME) {
                    BatteryStatsImpl.this.mCpuUidActiveTimeReader.removeUidsInRange(this.startUid, this.endUid);
                    BatteryStatsImpl.this.mCpuUidClusterTimeReader.removeUidsInRange(this.startUid, this.endUid);
                }
                if (BatteryStatsImpl.this.mKernelSingleUidTimeReader != null) {
                    BatteryStatsImpl.this.mKernelSingleUidTimeReader.removeUidsInRange(this.startUid, this.endUid);
                }
                BatteryStatsImpl.this.mNumUidsRemoved = BatteryStatsImpl.this.mNumUidsRemoved + 1;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("End UID ");
                stringBuilder.append(this.endUid);
                stringBuilder.append(" is smaller than start UID ");
                stringBuilder.append(this.startUid);
                Slog.w(BatteryStatsImpl.TAG, stringBuilder.toString());
            }
        }
    }

    public static abstract class UserInfoProvider {
        private int[] userIds;

        public abstract int[] getUserIds();

        @VisibleForTesting
        public final void refreshUserIds() {
            this.userIds = getUserIds();
        }

        @VisibleForTesting
        public boolean exists(int userId) {
            int[] iArr = this.userIds;
            return iArr != null ? ArrayUtils.contains(iArr, userId) : true;
        }
    }

    static {
        if (ActivityManager.isLowRamDeviceStatic()) {
            MAX_WAKELOCKS_PER_UID = 40;
        } else {
            MAX_WAKELOCKS_PER_UID = 200;
        }
    }

    public LongSparseArray<SamplingTimer> getKernelMemoryStats() {
        return this.mKernelMemoryStats;
    }

    public void postBatteryNeedsCpuUpdateMsg() {
        this.mHandler.sendEmptyMessage(1);
    }

    /* JADX WARNING: Missing block: B:21:0x0032, code skipped:
            r1 = r0.size() - 1;
     */
    /* JADX WARNING: Missing block: B:22:0x0038, code skipped:
            if (r1 < 0) goto L_0x009a;
     */
    /* JADX WARNING: Missing block: B:23:0x003a, code skipped:
            r2 = r0.keyAt(r1);
            r3 = r0.valueAt(r1);
     */
    /* JADX WARNING: Missing block: B:24:0x0042, code skipped:
            monitor-enter(r10);
     */
    /* JADX WARNING: Missing block: B:26:?, code skipped:
            r4 = getAvailableUidStatsLocked(r2);
     */
    /* JADX WARNING: Missing block: B:27:0x0047, code skipped:
            if (r4 != null) goto L_0x004b;
     */
    /* JADX WARNING: Missing block: B:28:0x0049, code skipped:
            monitor-exit(r10);
     */
    /* JADX WARNING: Missing block: B:31:0x004d, code skipped:
            if (r4.mChildUids != null) goto L_0x0051;
     */
    /* JADX WARNING: Missing block: B:32:0x004f, code skipped:
            r5 = null;
     */
    /* JADX WARNING: Missing block: B:33:0x0051, code skipped:
            r5 = r4.mChildUids.toArray();
            r6 = r5.length - 1;
     */
    /* JADX WARNING: Missing block: B:34:0x005a, code skipped:
            if (r6 < 0) goto L_0x0067;
     */
    /* JADX WARNING: Missing block: B:35:0x005c, code skipped:
            r5[r6] = r4.mChildUids.get(r6);
            r6 = r6 - 1;
     */
    /* JADX WARNING: Missing block: B:36:0x0067, code skipped:
            monitor-exit(r10);
     */
    /* JADX WARNING: Missing block: B:37:0x0068, code skipped:
            r6 = r10.mKernelSingleUidTimeReader.readDeltaMs(r2);
     */
    /* JADX WARNING: Missing block: B:38:0x006e, code skipped:
            if (r5 == null) goto L_0x0084;
     */
    /* JADX WARNING: Missing block: B:39:0x0070, code skipped:
            r7 = r5.length - 1;
     */
    /* JADX WARNING: Missing block: B:40:0x0073, code skipped:
            if (r7 < 0) goto L_0x0084;
     */
    /* JADX WARNING: Missing block: B:41:0x0075, code skipped:
            r6 = addCpuTimes(r6, r10.mKernelSingleUidTimeReader.readDeltaMs(r5[r7]));
            r7 = r7 - 1;
     */
    /* JADX WARNING: Missing block: B:42:0x0084, code skipped:
            if (r11 == false) goto L_0x0094;
     */
    /* JADX WARNING: Missing block: B:43:0x0086, code skipped:
            if (r6 == null) goto L_0x0094;
     */
    /* JADX WARNING: Missing block: B:44:0x0088, code skipped:
            monitor-enter(r10);
     */
    /* JADX WARNING: Missing block: B:46:?, code skipped:
            com.android.internal.os.BatteryStatsImpl.Uid.access$200(r4, r3, r6, r11);
            com.android.internal.os.BatteryStatsImpl.Uid.access$300(r4, r3, r6, r12);
     */
    /* JADX WARNING: Missing block: B:47:0x008f, code skipped:
            monitor-exit(r10);
     */
    /* JADX WARNING: Missing block: B:52:0x0094, code skipped:
            r1 = r1 - 1;
     */
    /* JADX WARNING: Missing block: B:57:0x009a, code skipped:
            return;
     */
    public void updateProcStateCpuTimes(boolean r11, boolean r12) {
        /*
        r10 = this;
        monitor-enter(r10);
        r0 = r10.mConstants;	 Catch:{ all -> 0x009b }
        r0 = r0.TRACK_CPU_TIMES_BY_PROC_STATE;	 Catch:{ all -> 0x009b }
        if (r0 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r10);	 Catch:{ all -> 0x009b }
        return;
    L_0x0009:
        r0 = r10.initKernelSingleUidTimeReaderLocked();	 Catch:{ all -> 0x009b }
        if (r0 != 0) goto L_0x0011;
    L_0x000f:
        monitor-exit(r10);	 Catch:{ all -> 0x009b }
        return;
    L_0x0011:
        r0 = r10.mIsPerProcessStateCpuDataStale;	 Catch:{ all -> 0x009b }
        if (r0 == 0) goto L_0x001c;
    L_0x0015:
        r0 = r10.mPendingUids;	 Catch:{ all -> 0x009b }
        r0.clear();	 Catch:{ all -> 0x009b }
        monitor-exit(r10);	 Catch:{ all -> 0x009b }
        return;
    L_0x001c:
        r0 = r10.mPendingUids;	 Catch:{ all -> 0x009b }
        r0 = r0.size();	 Catch:{ all -> 0x009b }
        if (r0 != 0) goto L_0x0026;
    L_0x0024:
        monitor-exit(r10);	 Catch:{ all -> 0x009b }
        return;
    L_0x0026:
        r0 = r10.mPendingUids;	 Catch:{ all -> 0x009b }
        r0 = r0.clone();	 Catch:{ all -> 0x009b }
        r1 = r10.mPendingUids;	 Catch:{ all -> 0x009b }
        r1.clear();	 Catch:{ all -> 0x009b }
        monitor-exit(r10);	 Catch:{ all -> 0x009b }
        r1 = r0.size();
        r1 = r1 + -1;
    L_0x0038:
        if (r1 < 0) goto L_0x009a;
    L_0x003a:
        r2 = r0.keyAt(r1);
        r3 = r0.valueAt(r1);
        monitor-enter(r10);
        r4 = r10.getAvailableUidStatsLocked(r2);	 Catch:{ all -> 0x0097 }
        if (r4 != 0) goto L_0x004b;
    L_0x0049:
        monitor-exit(r10);	 Catch:{ all -> 0x0097 }
        goto L_0x0094;
    L_0x004b:
        r5 = r4.mChildUids;	 Catch:{ all -> 0x0097 }
        if (r5 != 0) goto L_0x0051;
    L_0x004f:
        r5 = 0;
        goto L_0x0067;
    L_0x0051:
        r5 = r4.mChildUids;	 Catch:{ all -> 0x0097 }
        r5 = r5.toArray();	 Catch:{ all -> 0x0097 }
        r6 = r5.length;	 Catch:{ all -> 0x0097 }
        r6 = r6 + -1;
    L_0x005a:
        if (r6 < 0) goto L_0x0067;
    L_0x005c:
        r7 = r4.mChildUids;	 Catch:{ all -> 0x0097 }
        r7 = r7.get(r6);	 Catch:{ all -> 0x0097 }
        r5[r6] = r7;	 Catch:{ all -> 0x0097 }
        r6 = r6 + -1;
        goto L_0x005a;
    L_0x0067:
        monitor-exit(r10);	 Catch:{ all -> 0x0097 }
        r6 = r10.mKernelSingleUidTimeReader;
        r6 = r6.readDeltaMs(r2);
        if (r5 == 0) goto L_0x0084;
    L_0x0070:
        r7 = r5.length;
        r7 = r7 + -1;
    L_0x0073:
        if (r7 < 0) goto L_0x0084;
    L_0x0075:
        r8 = r10.mKernelSingleUidTimeReader;
        r9 = r5[r7];
        r8 = r8.readDeltaMs(r9);
        r6 = r10.addCpuTimes(r6, r8);
        r7 = r7 + -1;
        goto L_0x0073;
    L_0x0084:
        if (r11 == 0) goto L_0x0094;
    L_0x0086:
        if (r6 == 0) goto L_0x0094;
    L_0x0088:
        monitor-enter(r10);
        r4.addProcStateTimesMs(r3, r6, r11);	 Catch:{ all -> 0x0091 }
        r4.addProcStateScreenOffTimesMs(r3, r6, r12);	 Catch:{ all -> 0x0091 }
        monitor-exit(r10);	 Catch:{ all -> 0x0091 }
        goto L_0x0094;
    L_0x0091:
        r7 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x0091 }
        throw r7;
    L_0x0094:
        r1 = r1 + -1;
        goto L_0x0038;
    L_0x0097:
        r4 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x0097 }
        throw r4;
    L_0x009a:
        return;
    L_0x009b:
        r0 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x009b }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.updateProcStateCpuTimes(boolean, boolean):void");
    }

    public void clearPendingRemovedUids() {
        long cutOffTime = this.mClocks.elapsedRealtime() - this.mConstants.UID_REMOVE_DELAY_MS;
        while (!this.mPendingRemovedUids.isEmpty() && ((UidToRemove) this.mPendingRemovedUids.peek()).timeAddedInQueue < cutOffTime) {
            ((UidToRemove) this.mPendingRemovedUids.poll()).remove();
        }
    }

    public void copyFromAllUidsCpuTimes() {
        synchronized (this) {
            copyFromAllUidsCpuTimes(this.mOnBatteryTimeBase.isRunning(), this.mOnBatteryScreenOffTimeBase.isRunning());
        }
    }

    public void copyFromAllUidsCpuTimes(boolean onBattery, boolean onBatteryScreenOff) {
        synchronized (this) {
            if (!this.mConstants.TRACK_CPU_TIMES_BY_PROC_STATE) {
            } else if (initKernelSingleUidTimeReaderLocked()) {
                SparseArray<long[]> allUidCpuFreqTimesMs = this.mCpuUidFreqTimeReader.getAllUidCpuFreqTimeMs();
                if (this.mIsPerProcessStateCpuDataStale) {
                    this.mKernelSingleUidTimeReader.setAllUidsCpuTimesMs(allUidCpuFreqTimesMs);
                    this.mIsPerProcessStateCpuDataStale = false;
                    this.mPendingUids.clear();
                    return;
                }
                for (int i = allUidCpuFreqTimesMs.size() - 1; i >= 0; i--) {
                    int uid = allUidCpuFreqTimesMs.keyAt(i);
                    Uid u = getAvailableUidStatsLocked(mapUid(uid));
                    if (u != null) {
                        long[] cpuTimesMs = (long[]) allUidCpuFreqTimesMs.valueAt(i);
                        if (cpuTimesMs != null) {
                            long[] deltaTimesMs = this.mKernelSingleUidTimeReader.computeDelta(uid, (long[]) cpuTimesMs.clone());
                            if (onBattery && deltaTimesMs != null) {
                                int procState;
                                int idx = this.mPendingUids.indexOfKey(uid);
                                if (idx >= 0) {
                                    procState = this.mPendingUids.valueAt(idx);
                                    this.mPendingUids.removeAt(idx);
                                } else {
                                    procState = u.mProcessState;
                                }
                                if (procState >= 0 && procState < 7) {
                                    u.addProcStateTimesMs(procState, deltaTimesMs, onBattery);
                                    u.addProcStateScreenOffTimesMs(procState, deltaTimesMs, onBatteryScreenOff);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @VisibleForTesting
    public long[] addCpuTimes(long[] timesA, long[] timesB) {
        if (timesA == null || timesB == null) {
            long[] jArr = timesA == null ? timesB == null ? null : timesB : timesA;
            return jArr;
        }
        for (int i = timesA.length - 1; i >= 0; i--) {
            timesA[i] = timesA[i] + timesB[i];
        }
        return timesA;
    }

    @GuardedBy({"this"})
    private boolean initKernelSingleUidTimeReaderLocked() {
        boolean z = false;
        if (this.mKernelSingleUidTimeReader == null) {
            PowerProfile powerProfile = this.mPowerProfile;
            if (powerProfile == null) {
                return false;
            }
            if (this.mCpuFreqs == null) {
                this.mCpuFreqs = this.mCpuUidFreqTimeReader.readFreqs(powerProfile);
            }
            long[] jArr = this.mCpuFreqs;
            if (jArr != null) {
                this.mKernelSingleUidTimeReader = new KernelSingleUidTimeReader(jArr.length);
            } else {
                this.mPerProcStateCpuTimesAvailable = this.mCpuUidFreqTimeReader.allUidTimesAvailable();
                return false;
            }
        }
        if (this.mCpuUidFreqTimeReader.allUidTimesAvailable() && this.mKernelSingleUidTimeReader.singleUidCpuTimesAvailable()) {
            z = true;
        }
        this.mPerProcStateCpuTimesAvailable = z;
        return true;
    }

    public Map<String, ? extends Timer> getRpmStats() {
        return this.mRpmStats;
    }

    public Map<String, ? extends Timer> getScreenOffRpmStats() {
        return this.mScreenOffRpmStats;
    }

    @UnsupportedAppUsage
    public Map<String, ? extends Timer> getKernelWakelockStats() {
        return this.mKernelWakelockStats;
    }

    public Map<String, ? extends Timer> getWakeupReasonStats() {
        return this.mWakeupReasonStats;
    }

    public long getUahDischarge(int which) {
        return this.mDischargeCounter.getCountLocked(which);
    }

    public long getUahDischargeScreenOff(int which) {
        return this.mDischargeScreenOffCounter.getCountLocked(which);
    }

    public long getUahDischargeScreenDoze(int which) {
        return this.mDischargeScreenDozeCounter.getCountLocked(which);
    }

    public long getUahDischargeLightDoze(int which) {
        return this.mDischargeLightDozeCounter.getCountLocked(which);
    }

    public long getUahDischargeDeepDoze(int which) {
        return this.mDischargeDeepDozeCounter.getCountLocked(which);
    }

    public int getEstimatedBatteryCapacity() {
        return this.mEstimatedBatteryCapacity;
    }

    public int getMinLearnedBatteryCapacity() {
        return this.mMinLearnedBatteryCapacity;
    }

    public int getMaxLearnedBatteryCapacity() {
        return this.mMaxLearnedBatteryCapacity;
    }

    public BatteryStatsImpl() {
        this(new SystemClocks());
    }

    public BatteryStatsImpl(Clocks clocks) {
        this.mKernelWakelockReader = new KernelWakelockReader();
        this.mTmpWakelockStats = new KernelWakelockStats();
        this.mCpuUidUserSysTimeReader = new KernelCpuUidUserSysTimeReader(true);
        this.mCpuUidFreqTimeReader = new KernelCpuUidFreqTimeReader(true);
        this.mCpuUidActiveTimeReader = new KernelCpuUidActiveTimeReader(true);
        this.mCpuUidClusterTimeReader = new KernelCpuUidClusterTimeReader(true);
        this.mKernelMemoryBandwidthStats = new KernelMemoryBandwidthStats();
        this.mKernelMemoryStats = new LongSparseArray();
        this.mPerProcStateCpuTimesAvailable = true;
        this.mPendingUids = new SparseIntArray();
        this.mCpuTimeReadsTrackingStartTime = SystemClock.uptimeMillis();
        this.mTmpRpmStats = new RpmStats();
        this.mLastRpmStatsUpdateTimeMs = -1000;
        this.mTmpRailStats = new RailStats();
        this.mPendingRemovedUids = new LinkedList();
        this.mDeferSetCharging = new Runnable() {
            /* JADX WARNING: Missing block: B:11:0x002a, code skipped:
            return;
     */
            public void run() {
                /*
                r7 = this;
                r0 = com.android.internal.os.BatteryStatsImpl.this;
                monitor-enter(r0);
                r1 = com.android.internal.os.BatteryStatsImpl.this;	 Catch:{ all -> 0x002b }
                r1 = r1.mOnBattery;	 Catch:{ all -> 0x002b }
                if (r1 == 0) goto L_0x000b;
            L_0x0009:
                monitor-exit(r0);	 Catch:{ all -> 0x002b }
                return;
            L_0x000b:
                r1 = com.android.internal.os.BatteryStatsImpl.this;	 Catch:{ all -> 0x002b }
                r2 = 1;
                r1 = r1.setChargingLocked(r2);	 Catch:{ all -> 0x002b }
                if (r1 == 0) goto L_0x0029;
            L_0x0014:
                r2 = com.android.internal.os.BatteryStatsImpl.this;	 Catch:{ all -> 0x002b }
                r2 = r2.mClocks;	 Catch:{ all -> 0x002b }
                r2 = r2.uptimeMillis();	 Catch:{ all -> 0x002b }
                r4 = com.android.internal.os.BatteryStatsImpl.this;	 Catch:{ all -> 0x002b }
                r4 = r4.mClocks;	 Catch:{ all -> 0x002b }
                r4 = r4.elapsedRealtime();	 Catch:{ all -> 0x002b }
                r6 = com.android.internal.os.BatteryStatsImpl.this;	 Catch:{ all -> 0x002b }
                r6.addHistoryRecordLocked(r4, r2);	 Catch:{ all -> 0x002b }
            L_0x0029:
                monitor-exit(r0);	 Catch:{ all -> 0x002b }
                return;
            L_0x002b:
                r1 = move-exception;
                monitor-exit(r0);	 Catch:{ all -> 0x002b }
                throw r1;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl$AnonymousClass1.run():void");
            }
        };
        this.mExternalSync = null;
        this.mUserInfoProvider = null;
        this.mIsolatedUids = new SparseIntArray();
        this.mUidStats = new SparseArray();
        this.mPartialTimers = new ArrayList();
        this.mFullTimers = new ArrayList();
        this.mWindowTimers = new ArrayList();
        this.mDrawTimers = new ArrayList();
        this.mSensorTimers = new SparseArray();
        this.mWifiRunningTimers = new ArrayList();
        this.mFullWifiLockTimers = new ArrayList();
        this.mWifiMulticastTimers = new ArrayList();
        this.mWifiScanTimers = new ArrayList();
        this.mWifiBatchedScanTimers = new SparseArray();
        this.mAudioTurnedOnTimers = new ArrayList();
        this.mVideoTurnedOnTimers = new ArrayList();
        this.mFlashlightTurnedOnTimers = new ArrayList();
        this.mCameraTurnedOnTimers = new ArrayList();
        this.mBluetoothScanOnTimers = new ArrayList();
        this.mLastPartialTimers = new ArrayList();
        this.mOnBatteryTimeBase = new TimeBase(true);
        this.mOnBatteryScreenOffTimeBase = new TimeBase(true);
        this.mActiveEvents = new HistoryEventTracker();
        this.mHaveBatteryLevel = false;
        this.mRecordingHistory = false;
        this.mHistoryBuffer = Parcel.obtain();
        this.mHistoryLastWritten = new HistoryItem();
        this.mHistoryLastLastWritten = new HistoryItem();
        this.mHistoryReadTmp = new HistoryItem();
        this.mHistoryAddTmp = new HistoryItem();
        this.mHistoryTagPool = new HashMap();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        this.mHistoryBufferLastPos = -1;
        this.mActiveHistoryStates = -1;
        this.mActiveHistoryStates2 = -1;
        this.mLastHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryUptime = 0;
        this.mHistoryCur = new HistoryItem();
        this.mLastHistoryStepDetails = null;
        this.mLastHistoryStepLevel = (byte) 0;
        this.mCurHistoryStepDetails = new HistoryStepDetails();
        this.mReadHistoryStepDetails = new HistoryStepDetails();
        this.mTmpHistoryStepDetails = new HistoryStepDetails();
        this.mScreenState = 0;
        this.mScreenBrightnessBin = -1;
        this.mScreenBrightnessTimer = new StopwatchTimer[5];
        this.mUsbDataState = 0;
        this.mGpsSignalQualityBin = -1;
        this.mGpsSignalQualityTimer = new StopwatchTimer[2];
        this.mPhoneSignalStrengthBin = -1;
        this.mPhoneSignalStrengthBinRaw = -1;
        this.mPhoneSignalStrengthsTimer = new StopwatchTimer[5];
        this.mPhoneDataConnectionType = -1;
        this.mPhoneDataConnectionsTimer = new StopwatchTimer[22];
        this.mNetworkByteActivityCounters = new LongSamplingCounter[10];
        this.mNetworkPacketActivityCounters = new LongSamplingCounter[10];
        this.mHasWifiReporting = false;
        this.mHasBluetoothReporting = false;
        this.mHasModemReporting = false;
        this.mWifiState = -1;
        this.mWifiStateTimer = new StopwatchTimer[8];
        this.mWifiSupplState = -1;
        this.mWifiSupplStateTimer = new StopwatchTimer[13];
        this.mWifiSignalStrengthBin = -1;
        this.mWifiSignalStrengthsTimer = new StopwatchTimer[5];
        this.mIsCellularTxPowerHigh = false;
        this.mMobileRadioPowerState = 1;
        this.mWifiRadioPowerState = 1;
        this.mCharging = true;
        this.mInitStepMode = 0;
        this.mCurStepMode = 0;
        this.mModStepMode = 0;
        this.mDischargeStepTracker = new LevelStepTracker(200);
        this.mDailyDischargeStepTracker = new LevelStepTracker(400);
        this.mChargeStepTracker = new LevelStepTracker(200);
        this.mDailyChargeStepTracker = new LevelStepTracker(400);
        this.mDailyStartTime = 0;
        this.mNextMinDailyDeadline = 0;
        this.mNextMaxDailyDeadline = 0;
        this.mDailyItems = new ArrayList();
        this.mLastWriteTime = 0;
        this.mPhoneServiceState = -1;
        this.mPhoneServiceStateRaw = -1;
        this.mPhoneSimStateRaw = -1;
        this.mEstimatedBatteryCapacity = -1;
        this.mMinLearnedBatteryCapacity = -1;
        this.mMaxLearnedBatteryCapacity = -1;
        this.mRpmStats = new HashMap();
        this.mScreenOffRpmStats = new HashMap();
        this.mKernelWakelockStats = new HashMap();
        this.mLastWakeupReason = null;
        this.mLastWakeupUptimeMs = 0;
        this.mWakeupReasonStats = new HashMap();
        this.mChangedStates = 0;
        this.mChangedStates2 = 0;
        this.mInitialAcquireWakeUid = -1;
        this.mWifiFullLockNesting = 0;
        this.mWifiScanNesting = 0;
        this.mWifiMulticastNesting = 0;
        this.mNetworkStatsPool = new SynchronizedPool(6);
        this.mWifiNetworkLock = new Object();
        this.mWifiIfaces = EmptyArray.STRING;
        this.mLastWifiNetworkStats = new NetworkStats(0, -1);
        this.mModemNetworkLock = new Object();
        this.mModemIfaces = EmptyArray.STRING;
        this.mLastModemNetworkStats = new NetworkStats(0, -1);
        this.mLastModemActivityInfo = new ModemActivityInfo(0, 0, 0, new int[0], 0, 0);
        this.mLastBluetoothActivityInfo = new BluetoothActivityInfoCache(this, null);
        this.mWriteLock = new ReentrantLock();
        init(clocks);
        this.mStatsFile = null;
        this.mCheckinFile = null;
        this.mDailyFile = null;
        this.mBatteryStatsHistory = null;
        this.mHandler = null;
        this.mPlatformIdleStateCallback = null;
        this.mRailEnergyDataCallback = null;
        this.mUserInfoProvider = null;
        this.mConstants = new Constants(this.mHandler);
        clearHistoryLocked();
    }

    private void init(Clocks clocks) {
        this.mClocks = clocks;
    }

    public SamplingTimer getRpmTimerLocked(String name) {
        SamplingTimer rpmt = (SamplingTimer) this.mRpmStats.get(name);
        if (rpmt != null) {
            return rpmt;
        }
        rpmt = new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase);
        this.mRpmStats.put(name, rpmt);
        return rpmt;
    }

    public SamplingTimer getScreenOffRpmTimerLocked(String name) {
        SamplingTimer rpmt = (SamplingTimer) this.mScreenOffRpmStats.get(name);
        if (rpmt != null) {
            return rpmt;
        }
        rpmt = new SamplingTimer(this.mClocks, this.mOnBatteryScreenOffTimeBase);
        this.mScreenOffRpmStats.put(name, rpmt);
        return rpmt;
    }

    public SamplingTimer getWakeupReasonTimerLocked(String name) {
        SamplingTimer timer = (SamplingTimer) this.mWakeupReasonStats.get(name);
        if (timer != null) {
            return timer;
        }
        timer = new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase);
        this.mWakeupReasonStats.put(name, timer);
        return timer;
    }

    public SamplingTimer getKernelWakelockTimerLocked(String name) {
        SamplingTimer kwlt = (SamplingTimer) this.mKernelWakelockStats.get(name);
        if (kwlt != null) {
            return kwlt;
        }
        kwlt = new SamplingTimer(this.mClocks, this.mOnBatteryScreenOffTimeBase);
        this.mKernelWakelockStats.put(name, kwlt);
        return kwlt;
    }

    public SamplingTimer getKernelMemoryTimerLocked(long bucket) {
        SamplingTimer kmt = (SamplingTimer) this.mKernelMemoryStats.get(bucket);
        if (kmt != null) {
            return kmt;
        }
        kmt = new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase);
        this.mKernelMemoryStats.put(bucket, kmt);
        return kmt;
    }

    private int writeHistoryTag(HistoryTag tag) {
        Integer idxObj = (Integer) this.mHistoryTagPool.get(tag);
        if (idxObj != null) {
            return idxObj.intValue();
        }
        int idx = this.mNextHistoryTagIdx;
        HistoryTag key = new HistoryTag();
        key.setTo(tag);
        tag.poolIdx = idx;
        this.mHistoryTagPool.put(key, Integer.valueOf(idx));
        this.mNextHistoryTagIdx++;
        this.mNumHistoryTagChars += key.string.length() + 1;
        return idx;
    }

    private void readHistoryTag(int index, HistoryTag tag) {
        String[] strArr = this.mReadHistoryStrings;
        if (index < strArr.length) {
            tag.string = strArr[index];
            tag.uid = this.mReadHistoryUids[index];
        } else {
            tag.string = null;
            tag.uid = 0;
        }
        tag.poolIdx = index;
    }

    public void writeHistoryDelta(Parcel dest, HistoryItem cur, HistoryItem last) {
        Parcel parcel = dest;
        HistoryItem historyItem = cur;
        HistoryItem historyItem2 = last;
        if (historyItem2 == null || historyItem.cmd != (byte) 0) {
            parcel.writeInt(DELTA_TIME_ABS);
            historyItem.writeToParcel(parcel, 0);
            return;
        }
        int deltaTimeToken;
        long deltaTime = historyItem.time - historyItem2.time;
        int lastBatteryLevelInt = buildBatteryLevelInt(historyItem2);
        int lastStateInt = buildStateInt(historyItem2);
        if (deltaTime < 0 || deltaTime > 2147483647L) {
            deltaTimeToken = EventLogTags.SYSUI_VIEW_VISIBILITY;
        } else if (deltaTime >= 524285) {
            deltaTimeToken = DELTA_TIME_INT;
        } else {
            deltaTimeToken = (int) deltaTime;
        }
        int firstToken = (historyItem.states & DELTA_STATE_MASK) | deltaTimeToken;
        int includeStepDetails = this.mLastHistoryStepLevel > historyItem.batteryLevel ? 1 : 0;
        boolean computeStepDetails = includeStepDetails != 0 || this.mLastHistoryStepDetails == null;
        int batteryLevelInt = buildBatteryLevelInt(historyItem) | includeStepDetails;
        boolean batteryLevelIntChanged = batteryLevelInt != lastBatteryLevelInt;
        if (batteryLevelIntChanged) {
            firstToken |= 524288;
        }
        int stateInt = buildStateInt(historyItem);
        boolean stateIntChanged = stateInt != lastStateInt;
        if (stateIntChanged) {
            firstToken |= 1048576;
        }
        boolean state2IntChanged = historyItem.states2 != historyItem2.states2;
        if (state2IntChanged) {
            firstToken |= 2097152;
        }
        if (!(historyItem.wakelockTag == null && historyItem.wakeReasonTag == null)) {
            firstToken |= 4194304;
        }
        if (historyItem.eventCode != 0) {
            firstToken |= 8388608;
        }
        boolean batteryChargeChanged = historyItem.batteryChargeUAh != historyItem2.batteryChargeUAh;
        if (batteryChargeChanged) {
            firstToken |= 16777216;
        }
        parcel.writeInt(firstToken);
        if (deltaTimeToken >= DELTA_TIME_INT) {
            if (deltaTimeToken == DELTA_TIME_INT) {
                parcel.writeInt((int) deltaTime);
            } else {
                parcel.writeLong(deltaTime);
            }
        }
        if (batteryLevelIntChanged) {
            parcel.writeInt(batteryLevelInt);
        }
        if (stateIntChanged) {
            parcel.writeInt(stateInt);
        }
        if (state2IntChanged) {
            parcel.writeInt(historyItem.states2);
        }
        if (!(historyItem.wakelockTag == null && historyItem.wakeReasonTag == null)) {
            int wakeReasonIndex;
            if (historyItem.wakelockTag != null) {
                lastStateInt = writeHistoryTag(historyItem.wakelockTag);
            } else {
                lastStateInt = 65535;
            }
            if (historyItem.wakeReasonTag != null) {
                wakeReasonIndex = writeHistoryTag(historyItem.wakeReasonTag);
            } else {
                wakeReasonIndex = 65535;
            }
            parcel.writeInt((wakeReasonIndex << 16) | lastStateInt);
        }
        if (historyItem.eventCode != 0) {
            parcel.writeInt((historyItem.eventCode & 65535) | (writeHistoryTag(historyItem.eventTag) << 16));
        }
        if (computeStepDetails) {
            PlatformIdleStateCallback platformIdleStateCallback = this.mPlatformIdleStateCallback;
            if (platformIdleStateCallback != null) {
                this.mCurHistoryStepDetails.statPlatformIdleState = platformIdleStateCallback.getPlatformLowPowerStats();
                this.mCurHistoryStepDetails.statSubsystemPowerState = this.mPlatformIdleStateCallback.getSubsystemLowPowerStats();
            }
            computeHistoryStepDetails(this.mCurHistoryStepDetails, this.mLastHistoryStepDetails);
            if (includeStepDetails != 0) {
                this.mCurHistoryStepDetails.writeToParcel(parcel);
            }
            HistoryStepDetails historyStepDetails = this.mCurHistoryStepDetails;
            historyItem.stepDetails = historyStepDetails;
            this.mLastHistoryStepDetails = historyStepDetails;
        } else {
            historyItem.stepDetails = null;
        }
        if (this.mLastHistoryStepLevel < historyItem.batteryLevel) {
            this.mLastHistoryStepDetails = null;
        }
        this.mLastHistoryStepLevel = historyItem.batteryLevel;
        if (batteryChargeChanged) {
            parcel.writeInt(historyItem.batteryChargeUAh);
        }
        parcel.writeDouble(historyItem.modemRailChargeMah);
        parcel.writeDouble(historyItem.wifiRailChargeMah);
    }

    private int buildBatteryLevelInt(HistoryItem h) {
        return (((h.batteryLevel << 25) & DELTA_STATE_MASK) | ((h.batteryTemperature << 15) & 33521664)) | ((h.batteryVoltage << 1) & 32766);
    }

    private void readBatteryLevelInt(int batteryLevelInt, HistoryItem out) {
        out.batteryLevel = (byte) ((DELTA_STATE_MASK & batteryLevelInt) >>> 25);
        out.batteryTemperature = (short) ((33521664 & batteryLevelInt) >>> 15);
        out.batteryVoltage = (char) ((batteryLevelInt & 32766) >>> 1);
    }

    private int buildStateInt(HistoryItem h) {
        int plugType = 0;
        if ((h.batteryPlugType & 1) != 0) {
            plugType = 1;
        } else if ((h.batteryPlugType & 2) != 0) {
            plugType = 2;
        } else if ((h.batteryPlugType & 4) != 0) {
            plugType = 3;
        }
        return ((((h.batteryStatus & 7) << 29) | ((h.batteryHealth & 7) << 26)) | ((plugType & 3) << 24)) | (h.states & 16777215);
    }

    private void computeHistoryStepDetails(HistoryStepDetails out, HistoryStepDetails last) {
        HistoryStepDetails tmp = last != null ? this.mTmpHistoryStepDetails : out;
        requestImmediateCpuUpdate();
        int NU;
        int i;
        Uid uid;
        if (last == null) {
            NU = this.mUidStats.size();
            for (i = 0; i < NU; i++) {
                uid = (Uid) this.mUidStats.valueAt(i);
                uid.mLastStepUserTime = uid.mCurStepUserTime;
                uid.mLastStepSystemTime = uid.mCurStepSystemTime;
            }
            this.mLastStepCpuUserTime = this.mCurStepCpuUserTime;
            this.mLastStepCpuSystemTime = this.mCurStepCpuSystemTime;
            this.mLastStepStatUserTime = this.mCurStepStatUserTime;
            this.mLastStepStatSystemTime = this.mCurStepStatSystemTime;
            this.mLastStepStatIOWaitTime = this.mCurStepStatIOWaitTime;
            this.mLastStepStatIrqTime = this.mCurStepStatIrqTime;
            this.mLastStepStatSoftIrqTime = this.mCurStepStatSoftIrqTime;
            this.mLastStepStatIdleTime = this.mCurStepStatIdleTime;
            tmp.clear();
            return;
        }
        out.userTime = (int) (this.mCurStepCpuUserTime - this.mLastStepCpuUserTime);
        out.systemTime = (int) (this.mCurStepCpuSystemTime - this.mLastStepCpuSystemTime);
        out.statUserTime = (int) (this.mCurStepStatUserTime - this.mLastStepStatUserTime);
        out.statSystemTime = (int) (this.mCurStepStatSystemTime - this.mLastStepStatSystemTime);
        out.statIOWaitTime = (int) (this.mCurStepStatIOWaitTime - this.mLastStepStatIOWaitTime);
        out.statIrqTime = (int) (this.mCurStepStatIrqTime - this.mLastStepStatIrqTime);
        out.statSoftIrqTime = (int) (this.mCurStepStatSoftIrqTime - this.mLastStepStatSoftIrqTime);
        out.statIdlTime = (int) (this.mCurStepStatIdleTime - this.mLastStepStatIdleTime);
        out.appCpuUid3 = -1;
        out.appCpuUid2 = -1;
        out.appCpuUid1 = -1;
        out.appCpuUTime3 = 0;
        out.appCpuUTime2 = 0;
        out.appCpuUTime1 = 0;
        out.appCpuSTime3 = 0;
        out.appCpuSTime2 = 0;
        out.appCpuSTime1 = 0;
        NU = this.mUidStats.size();
        for (i = 0; i < NU; i++) {
            uid = (Uid) this.mUidStats.valueAt(i);
            int totalUTime = (int) (uid.mCurStepUserTime - uid.mLastStepUserTime);
            int totalSTime = (int) (uid.mCurStepSystemTime - uid.mLastStepSystemTime);
            int totalTime = totalUTime + totalSTime;
            uid.mLastStepUserTime = uid.mCurStepUserTime;
            uid.mLastStepSystemTime = uid.mCurStepSystemTime;
            if (totalTime > out.appCpuUTime3 + out.appCpuSTime3) {
                if (totalTime <= out.appCpuUTime2 + out.appCpuSTime2) {
                    out.appCpuUid3 = uid.mUid;
                    out.appCpuUTime3 = totalUTime;
                    out.appCpuSTime3 = totalSTime;
                } else {
                    out.appCpuUid3 = out.appCpuUid2;
                    out.appCpuUTime3 = out.appCpuUTime2;
                    out.appCpuSTime3 = out.appCpuSTime2;
                    if (totalTime <= out.appCpuUTime1 + out.appCpuSTime1) {
                        out.appCpuUid2 = uid.mUid;
                        out.appCpuUTime2 = totalUTime;
                        out.appCpuSTime2 = totalSTime;
                    } else {
                        out.appCpuUid2 = out.appCpuUid1;
                        out.appCpuUTime2 = out.appCpuUTime1;
                        out.appCpuSTime2 = out.appCpuSTime1;
                        out.appCpuUid1 = uid.mUid;
                        out.appCpuUTime1 = totalUTime;
                        out.appCpuSTime1 = totalSTime;
                    }
                }
            }
        }
        this.mLastStepCpuUserTime = this.mCurStepCpuUserTime;
        this.mLastStepCpuSystemTime = this.mCurStepCpuSystemTime;
        this.mLastStepStatUserTime = this.mCurStepStatUserTime;
        this.mLastStepStatSystemTime = this.mCurStepStatSystemTime;
        this.mLastStepStatIOWaitTime = this.mCurStepStatIOWaitTime;
        this.mLastStepStatIrqTime = this.mCurStepStatIrqTime;
        this.mLastStepStatSoftIrqTime = this.mCurStepStatSoftIrqTime;
        this.mLastStepStatIdleTime = this.mCurStepStatIdleTime;
    }

    public void readHistoryDelta(Parcel src, HistoryItem cur) {
        int batteryLevelInt;
        int firstToken = src.readInt();
        int deltaTimeToken = EventLogTags.SYSUI_VIEW_VISIBILITY & firstToken;
        cur.cmd = (byte) 0;
        cur.numReadInts = 1;
        if (deltaTimeToken < DELTA_TIME_ABS) {
            cur.time += (long) deltaTimeToken;
        } else if (deltaTimeToken == DELTA_TIME_ABS) {
            cur.time = src.readLong();
            cur.numReadInts += 2;
            cur.readFromParcel(src);
            return;
        } else if (deltaTimeToken == DELTA_TIME_INT) {
            cur.time += (long) src.readInt();
            cur.numReadInts++;
        } else {
            cur.time += src.readLong();
            cur.numReadInts += 2;
        }
        if ((524288 & firstToken) != 0) {
            batteryLevelInt = src.readInt();
            readBatteryLevelInt(batteryLevelInt, cur);
            cur.numReadInts++;
        } else {
            batteryLevelInt = 0;
        }
        if ((1048576 & firstToken) != 0) {
            int stateInt = src.readInt();
            cur.states = (16777215 & stateInt) | (DELTA_STATE_MASK & firstToken);
            cur.batteryStatus = (byte) ((stateInt >> 29) & 7);
            cur.batteryHealth = (byte) ((stateInt >> 26) & 7);
            cur.batteryPlugType = (byte) ((stateInt >> 24) & 3);
            byte b = cur.batteryPlugType;
            if (b == (byte) 1) {
                cur.batteryPlugType = (byte) 1;
            } else if (b == (byte) 2) {
                cur.batteryPlugType = (byte) 2;
            } else if (b == (byte) 3) {
                cur.batteryPlugType = (byte) 4;
            }
            cur.numReadInts++;
        } else {
            cur.states = (firstToken & DELTA_STATE_MASK) | (cur.states & 16777215);
        }
        if ((2097152 & firstToken) != 0) {
            cur.states2 = src.readInt();
        }
        if ((4194304 & firstToken) != 0) {
            int indexes = src.readInt();
            int wakeLockIndex = indexes & 65535;
            int wakeReasonIndex = (indexes >> 16) & 65535;
            if (wakeLockIndex != 65535) {
                cur.wakelockTag = cur.localWakelockTag;
                readHistoryTag(wakeLockIndex, cur.wakelockTag);
            } else {
                cur.wakelockTag = null;
            }
            if (wakeReasonIndex != 65535) {
                cur.wakeReasonTag = cur.localWakeReasonTag;
                readHistoryTag(wakeReasonIndex, cur.wakeReasonTag);
            } else {
                cur.wakeReasonTag = null;
            }
            cur.numReadInts++;
        } else {
            cur.wakelockTag = null;
            cur.wakeReasonTag = null;
        }
        if ((8388608 & firstToken) != 0) {
            cur.eventTag = cur.localEventTag;
            int codeAndIndex = src.readInt();
            cur.eventCode = codeAndIndex & 65535;
            readHistoryTag((codeAndIndex >> 16) & 65535, cur.eventTag);
            cur.numReadInts++;
        } else {
            cur.eventCode = 0;
        }
        if ((batteryLevelInt & 1) != 0) {
            cur.stepDetails = this.mReadHistoryStepDetails;
            cur.stepDetails.readFromParcel(src);
        } else {
            cur.stepDetails = null;
        }
        if ((16777216 & firstToken) != 0) {
            cur.batteryChargeUAh = src.readInt();
        }
        cur.modemRailChargeMah = src.readDouble();
        cur.wifiRailChargeMah = src.readDouble();
    }

    public void commitCurrentHistoryBatchLocked() {
        this.mHistoryLastWritten.cmd = (byte) -1;
    }

    public void createFakeHistoryEvents(long numEvents) {
        for (long i = 0; i < numEvents; i++) {
            String str = "historyName1";
            String str2 = "name1";
            noteLongPartialWakelockStart(str2, str, 1000);
            noteLongPartialWakelockFinish(str2, str, 1000);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void addHistoryBufferLocked(long elapsedRealtimeMs, HistoryItem cur) {
        HistoryItem historyItem = cur;
        if (this.mHaveBatteryLevel && this.mRecordingHistory) {
            long elapsedRealtimeMs2;
            long timeDiff = (this.mHistoryBaseTime + elapsedRealtimeMs) - this.mHistoryLastWritten.time;
            int diffStates = this.mHistoryLastWritten.states ^ (historyItem.states & this.mActiveHistoryStates);
            int diffStates2 = this.mHistoryLastWritten.states2 ^ (historyItem.states2 & this.mActiveHistoryStates2);
            int lastDiffStates = this.mHistoryLastWritten.states ^ this.mHistoryLastLastWritten.states;
            int lastDiffStates2 = this.mHistoryLastWritten.states2 ^ this.mHistoryLastLastWritten.states2;
            if (this.mHistoryBufferLastPos >= 0 && this.mHistoryLastWritten.cmd == (byte) 0 && timeDiff < 1000 && (diffStates & lastDiffStates) == 0 && (diffStates2 & lastDiffStates2) == 0 && ((this.mHistoryLastWritten.wakelockTag == null || historyItem.wakelockTag == null) && ((this.mHistoryLastWritten.wakeReasonTag == null || historyItem.wakeReasonTag == null) && this.mHistoryLastWritten.stepDetails == null && ((this.mHistoryLastWritten.eventCode == 0 || historyItem.eventCode == 0) && this.mHistoryLastWritten.batteryLevel == historyItem.batteryLevel && this.mHistoryLastWritten.batteryStatus == historyItem.batteryStatus && this.mHistoryLastWritten.batteryHealth == historyItem.batteryHealth && this.mHistoryLastWritten.batteryPlugType == historyItem.batteryPlugType && this.mHistoryLastWritten.batteryTemperature == historyItem.batteryTemperature && this.mHistoryLastWritten.batteryVoltage == historyItem.batteryVoltage)))) {
                this.mHistoryBuffer.setDataSize(this.mHistoryBufferLastPos);
                this.mHistoryBuffer.setDataPosition(this.mHistoryBufferLastPos);
                this.mHistoryBufferLastPos = -1;
                long elapsedRealtimeMs3 = this.mHistoryLastWritten.time - this.mHistoryBaseTime;
                if (this.mHistoryLastWritten.wakelockTag != null) {
                    historyItem.wakelockTag = historyItem.localWakelockTag;
                    historyItem.wakelockTag.setTo(this.mHistoryLastWritten.wakelockTag);
                }
                if (this.mHistoryLastWritten.wakeReasonTag != null) {
                    historyItem.wakeReasonTag = historyItem.localWakeReasonTag;
                    historyItem.wakeReasonTag.setTo(this.mHistoryLastWritten.wakeReasonTag);
                }
                if (this.mHistoryLastWritten.eventCode != 0) {
                    historyItem.eventCode = this.mHistoryLastWritten.eventCode;
                    historyItem.eventTag = historyItem.localEventTag;
                    historyItem.eventTag.setTo(this.mHistoryLastWritten.eventTag);
                }
                this.mHistoryLastWritten.setTo(this.mHistoryLastLastWritten);
                elapsedRealtimeMs2 = elapsedRealtimeMs3;
            } else {
                elapsedRealtimeMs2 = elapsedRealtimeMs;
            }
            int dataSize = this.mHistoryBuffer.dataSize();
            byte b;
            if (dataSize >= this.mConstants.MAX_HISTORY_BUFFER) {
                long start = SystemClock.uptimeMillis();
                writeHistoryLocked(true);
                this.mBatteryStatsHistory.startNextFile();
                this.mHistoryBuffer.setDataSize(0);
                this.mHistoryBuffer.setDataPosition(0);
                this.mHistoryBuffer.setDataCapacity(this.mConstants.MAX_HISTORY_BUFFER / 2);
                this.mHistoryBufferLastPos = -1;
                long elapsedRealtime = this.mClocks.elapsedRealtime();
                long uptime = this.mClocks.uptimeMillis();
                HistoryItem newItem = new HistoryItem();
                newItem.setTo(historyItem);
                b = (byte) 0;
                timeDiff = newItem;
                startRecordingHistory(elapsedRealtime, uptime, false);
                addHistoryBufferLocked(elapsedRealtimeMs2, b, timeDiff);
                return;
            }
            long j = timeDiff;
            b = (byte) 0;
            if (dataSize == 0) {
                historyItem.currentTime = System.currentTimeMillis();
                addHistoryBufferLocked(elapsedRealtimeMs2, (byte) 7, historyItem);
            }
            addHistoryBufferLocked(elapsedRealtimeMs2, b, historyItem);
        }
    }

    private void addHistoryBufferLocked(long elapsedRealtimeMs, byte cmd, HistoryItem cur) {
        if (this.mIteratingHistory) {
            throw new IllegalStateException("Can't do this while iterating history!");
        }
        this.mHistoryBufferLastPos = this.mHistoryBuffer.dataPosition();
        this.mHistoryLastLastWritten.setTo(this.mHistoryLastWritten);
        this.mHistoryLastWritten.setTo(this.mHistoryBaseTime + elapsedRealtimeMs, cmd, cur);
        HistoryItem historyItem = this.mHistoryLastWritten;
        historyItem.states &= this.mActiveHistoryStates;
        historyItem = this.mHistoryLastWritten;
        historyItem.states2 &= this.mActiveHistoryStates2;
        writeHistoryDelta(this.mHistoryBuffer, this.mHistoryLastWritten, this.mHistoryLastLastWritten);
        this.mLastHistoryElapsedRealtime = elapsedRealtimeMs;
        cur.wakelockTag = null;
        cur.wakeReasonTag = null;
        cur.eventCode = 0;
        cur.eventTag = null;
    }

    /* Access modifiers changed, original: 0000 */
    public void addHistoryRecordLocked(long elapsedRealtimeMs, long uptimeMs) {
        long diffElapsed = this.mTrackRunningHistoryElapsedRealtime;
        if (diffElapsed != 0) {
            diffElapsed = elapsedRealtimeMs - diffElapsed;
            long diffUptime = uptimeMs - this.mTrackRunningHistoryUptime;
            if (diffUptime < diffElapsed - 20) {
                long wakeElapsedTime = elapsedRealtimeMs - (diffElapsed - diffUptime);
                this.mHistoryAddTmp.setTo(this.mHistoryLastWritten);
                HistoryItem historyItem = this.mHistoryAddTmp;
                historyItem.wakelockTag = null;
                historyItem.wakeReasonTag = null;
                historyItem.eventCode = 0;
                historyItem.states &= Integer.MAX_VALUE;
                addHistoryRecordInnerLocked(wakeElapsedTime, this.mHistoryAddTmp);
            }
        }
        HistoryItem historyItem2 = this.mHistoryCur;
        historyItem2.states |= Integer.MIN_VALUE;
        this.mTrackRunningHistoryElapsedRealtime = elapsedRealtimeMs;
        this.mTrackRunningHistoryUptime = uptimeMs;
        addHistoryRecordInnerLocked(elapsedRealtimeMs, this.mHistoryCur);
    }

    /* Access modifiers changed, original: 0000 */
    public void addHistoryRecordInnerLocked(long elapsedRealtimeMs, HistoryItem cur) {
        addHistoryBufferLocked(elapsedRealtimeMs, cur);
    }

    public void addHistoryEventLocked(long elapsedRealtimeMs, long uptimeMs, int code, String name, int uid) {
        HistoryItem historyItem = this.mHistoryCur;
        historyItem.eventCode = code;
        historyItem.eventTag = historyItem.localEventTag;
        this.mHistoryCur.eventTag.string = name;
        this.mHistoryCur.eventTag.uid = uid;
        addHistoryRecordLocked(elapsedRealtimeMs, uptimeMs);
    }

    /* Access modifiers changed, original: 0000 */
    public void addHistoryRecordLocked(long elapsedRealtimeMs, long uptimeMs, byte cmd, HistoryItem cur) {
        HistoryItem rec = this.mHistoryCache;
        if (rec != null) {
            this.mHistoryCache = rec.next;
        } else {
            rec = new HistoryItem();
        }
        rec.setTo(this.mHistoryBaseTime + elapsedRealtimeMs, cmd, cur);
        addHistoryRecordLocked(rec);
    }

    /* Access modifiers changed, original: 0000 */
    public void addHistoryRecordLocked(HistoryItem rec) {
        this.mNumHistoryItems++;
        rec.next = null;
        HistoryItem historyItem = this.mHistoryEnd;
        this.mHistoryLastEnd = historyItem;
        if (historyItem != null) {
            historyItem.next = rec;
            this.mHistoryEnd = rec;
            return;
        }
        this.mHistoryEnd = rec;
        this.mHistory = rec;
    }

    /* Access modifiers changed, original: 0000 */
    public void clearHistoryLocked() {
        this.mHistoryBaseTime = 0;
        this.mLastHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryUptime = 0;
        this.mHistoryBuffer.setDataSize(0);
        this.mHistoryBuffer.setDataPosition(0);
        this.mHistoryBuffer.setDataCapacity(this.mConstants.MAX_HISTORY_BUFFER / 2);
        this.mHistoryLastLastWritten.clear();
        this.mHistoryLastWritten.clear();
        this.mHistoryTagPool.clear();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        this.mHistoryBufferLastPos = -1;
        this.mActiveHistoryStates = -1;
        this.mActiveHistoryStates2 = -1;
    }

    @GuardedBy({"this"})
    public void updateTimeBasesLocked(boolean unplugged, int screenState, long uptime, long realtime) {
        boolean z = unplugged;
        long j = uptime;
        long j2 = realtime;
        boolean screenOff = isScreenOn(screenState) ^ 1;
        boolean updateOnBatteryTimeBase = z != this.mOnBatteryTimeBase.isRunning();
        boolean z2 = z && screenOff;
        boolean updateOnBatteryScreenOffTimeBase = z2 != this.mOnBatteryScreenOffTimeBase.isRunning();
        if (updateOnBatteryScreenOffTimeBase || updateOnBatteryTimeBase) {
            int i;
            if (updateOnBatteryScreenOffTimeBase) {
                updateKernelWakelocksLocked();
                updateBatteryPropertiesLocked();
            }
            if (updateOnBatteryTimeBase) {
                updateRpmStatsLocked();
            }
            this.mOnBatteryTimeBase.setRunning(unplugged, uptime, realtime);
            if (updateOnBatteryTimeBase) {
                for (i = this.mUidStats.size() - 1; i >= 0; i--) {
                    ((Uid) this.mUidStats.valueAt(i)).updateOnBatteryBgTimeBase(j, j2);
                }
            }
            if (updateOnBatteryScreenOffTimeBase) {
                TimeBase timeBase = this.mOnBatteryScreenOffTimeBase;
                boolean z3 = z && screenOff;
                timeBase.setRunning(z3, uptime, realtime);
                for (i = this.mUidStats.size() - 1; i >= 0; i--) {
                    ((Uid) this.mUidStats.valueAt(i)).updateOnBatteryScreenOffBgTimeBase(j, j2);
                }
            }
        }
    }

    private void updateBatteryPropertiesLocked() {
        try {
            IBatteryPropertiesRegistrar registrar = Stub.asInterface(ServiceManager.getService("batteryproperties"));
            if (registrar != null) {
                registrar.scheduleUpdate();
            }
        } catch (RemoteException e) {
        }
    }

    public void addIsolatedUidLocked(int isolatedUid, int appUid) {
        this.mIsolatedUids.put(isolatedUid, appUid);
        getUidStatsLocked(appUid).addIsolatedUid(isolatedUid);
    }

    public void scheduleRemoveIsolatedUidLocked(int isolatedUid, int appUid) {
        if (this.mIsolatedUids.get(isolatedUid, -1) == appUid) {
            ExternalStatsSync externalStatsSync = this.mExternalSync;
            if (externalStatsSync != null) {
                externalStatsSync.scheduleCpuSyncDueToRemovedUid(isolatedUid);
            }
        }
    }

    @GuardedBy({"this"})
    public void removeIsolatedUidLocked(int isolatedUid) {
        int idx = this.mIsolatedUids.indexOfKey(isolatedUid);
        if (idx >= 0) {
            getUidStatsLocked(this.mIsolatedUids.valueAt(idx)).removeIsolatedUid(isolatedUid);
            this.mIsolatedUids.removeAt(idx);
        }
        this.mPendingRemovedUids.add(new UidToRemove(this, isolatedUid, this.mClocks.elapsedRealtime()));
    }

    public int mapUid(int uid) {
        int isolated = this.mIsolatedUids.get(uid, -1);
        return isolated > 0 ? isolated : uid;
    }

    public void noteEventLocked(int code, String name, int uid) {
        uid = mapUid(uid);
        if (this.mActiveEvents.updateState(code, name, uid, 0)) {
            addHistoryEventLocked(this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis(), code, name, uid);
        }
    }

    public void noteCurrentTimeChangedLocked() {
        recordCurrentTimeChangeLocked(System.currentTimeMillis(), this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis());
    }

    public void noteProcessStartLocked(String name, int uid) {
        String processName = BatteryStatsImplInjector.getProcessName(name);
        uid = mapUid(uid);
        if (isOnBattery()) {
            getUidStatsLocked(uid).getProcessStatsLocked(processName).incStartsLocked();
        }
        if (this.mActiveEvents.updateState(32769, processName, uid, 0) && this.mRecordAllHistory) {
            addHistoryEventLocked(this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis(), 32769, name, uid);
        }
    }

    public void noteProcessCrashLocked(String name, int uid) {
        uid = mapUid(uid);
        if (isOnBattery()) {
            getUidStatsLocked(uid).getProcessStatsLocked(name).incNumCrashesLocked();
        }
    }

    public void noteProcessAnrLocked(String name, int uid) {
        uid = mapUid(uid);
        if (isOnBattery()) {
            getUidStatsLocked(uid).getProcessStatsLocked(name).incNumAnrsLocked();
        }
    }

    public void noteUidProcessStateLocked(int uid, int state) {
        if (uid == mapUid(uid)) {
            getUidStatsLocked(uid).updateUidProcessStateLocked(state);
        }
    }

    public void noteProcessFinishLocked(String name, int uid) {
        uid = mapUid(uid);
        if (this.mActiveEvents.updateState(16385, name, uid, 0) && this.mRecordAllHistory) {
            addHistoryEventLocked(this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis(), 16385, name, uid);
        }
    }

    public void noteSyncStartLocked(String name, int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        getUidStatsLocked(uid).noteStartSyncLocked(name, elapsedRealtime);
        if (this.mActiveEvents.updateState(32772, name, uid, 0)) {
            addHistoryEventLocked(elapsedRealtime, uptime, 32772, name, uid);
        }
    }

    public void noteSyncFinishLocked(String name, int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        getUidStatsLocked(uid).noteStopSyncLocked(name, elapsedRealtime);
        if (this.mActiveEvents.updateState(16388, name, uid, 0)) {
            addHistoryEventLocked(elapsedRealtime, uptime, 16388, name, uid);
        }
    }

    public void noteJobStartLocked(String name, int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        getUidStatsLocked(uid).noteStartJobLocked(name, elapsedRealtime);
        if (this.mActiveEvents.updateState(32774, name, uid, 0)) {
            addHistoryEventLocked(elapsedRealtime, uptime, 32774, name, uid);
        }
    }

    public void noteJobFinishLocked(String name, int uid, int stopReason) {
        uid = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        getUidStatsLocked(uid).noteStopJobLocked(name, elapsedRealtime, stopReason);
        if (this.mActiveEvents.updateState(16390, name, uid, 0)) {
            addHistoryEventLocked(elapsedRealtime, uptime, 16390, name, uid);
        }
    }

    public void noteJobsDeferredLocked(int uid, int numDeferred, long sinceLast) {
        getUidStatsLocked(mapUid(uid)).noteJobsDeferredLocked(numDeferred, sinceLast);
    }

    public void noteAlarmStartLocked(String name, WorkSource workSource, int uid) {
        noteAlarmStartOrFinishLocked(HistoryItem.EVENT_ALARM_START, name, workSource, uid);
    }

    public void noteAlarmFinishLocked(String name, WorkSource workSource, int uid) {
        noteAlarmStartOrFinishLocked(16397, name, workSource, uid);
    }

    private void noteAlarmStartOrFinishLocked(int historyItem, String name, WorkSource workSource, int uid) {
        int i = historyItem;
        String str = name;
        WorkSource workSource2 = workSource;
        if (this.mRecordAllHistory) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            int i2 = 0;
            int i3;
            if (workSource2 != null) {
                int uid2;
                int uid3;
                int i4;
                int i5 = 0;
                int uid4 = uid;
                while (i5 < workSource.size()) {
                    uid2 = mapUid(workSource2.get(i5));
                    if (this.mActiveEvents.updateState(i, str, uid2, i2)) {
                        uid3 = uid2;
                        i4 = i5;
                        i3 = i2;
                        addHistoryEventLocked(elapsedRealtime, uptime, historyItem, name, uid3);
                    } else {
                        uid3 = uid2;
                        i4 = i5;
                        i3 = i2;
                    }
                    i5 = i4 + 1;
                    i2 = i3;
                    uid4 = uid3;
                    workSource2 = workSource;
                }
                i4 = i5;
                i3 = i2;
                List<WorkChain> workChains = workSource.getWorkChains();
                if (workChains != null) {
                    List<WorkChain> workChains2;
                    i5 = 0;
                    while (i5 < workChains.size()) {
                        uid2 = mapUid(((WorkChain) workChains.get(i5)).getAttributionUid());
                        if (this.mActiveEvents.updateState(i, str, uid2, i3)) {
                            uid3 = uid2;
                            i4 = i5;
                            workChains2 = workChains;
                            addHistoryEventLocked(elapsedRealtime, uptime, historyItem, name, uid3);
                        } else {
                            uid3 = uid2;
                            i4 = i5;
                            workChains2 = workChains;
                        }
                        i5 = i4 + 1;
                        uid4 = uid3;
                        workChains = workChains2;
                    }
                    i4 = i5;
                    workChains2 = workChains;
                }
                i3 = uid4;
            } else {
                i3 = 0;
                i2 = mapUid(uid);
                if (this.mActiveEvents.updateState(i, str, i2, i3)) {
                    addHistoryEventLocked(elapsedRealtime, uptime, historyItem, name, i2);
                }
            }
        }
    }

    public void noteWakupAlarmLocked(String packageName, int uid, WorkSource workSource, String tag) {
        if (workSource != null) {
            for (int i = 0; i < workSource.size(); i++) {
                uid = workSource.get(i);
                String workSourceName = workSource.getName(i);
                if (isOnBattery()) {
                    getPackageStatsLocked(uid, workSourceName != null ? workSourceName : packageName).noteWakeupAlarmLocked(tag);
                }
            }
            ArrayList<WorkChain> workChains = workSource.getWorkChains();
            if (workChains != null) {
                for (int i2 = 0; i2 < workChains.size(); i2++) {
                    uid = ((WorkChain) workChains.get(i2)).getAttributionUid();
                    if (isOnBattery()) {
                        getPackageStatsLocked(uid, packageName).noteWakeupAlarmLocked(tag);
                    }
                }
            }
        } else if (isOnBattery()) {
            getPackageStatsLocked(uid, packageName).noteWakeupAlarmLocked(tag);
        }
    }

    private void requestWakelockCpuUpdate() {
        this.mExternalSync.scheduleCpuSyncDueToWakelockChange(5000);
    }

    private void requestImmediateCpuUpdate() {
        this.mExternalSync.scheduleCpuSyncDueToWakelockChange(0);
    }

    public void setRecordAllHistoryLocked(boolean enabled) {
        boolean z = enabled;
        this.mRecordAllHistory = z;
        HashMap<String, SparseIntArray> active;
        long mSecRealtime;
        long mSecUptime;
        SparseIntArray uids;
        int j;
        String str;
        int keyAt;
        int j2;
        String str2;
        SparseIntArray uids2;
        if (z) {
            active = this.mActiveEvents.getStateForEvent(1);
            if (active != null) {
                mSecRealtime = this.mClocks.elapsedRealtime();
                mSecUptime = this.mClocks.uptimeMillis();
                for (Entry<String, SparseIntArray> ent : active.entrySet()) {
                    uids = (SparseIntArray) ent.getValue();
                    j = 0;
                    while (j < uids.size()) {
                        str = (String) ent.getKey();
                        keyAt = uids.keyAt(j);
                        j2 = j;
                        str2 = str;
                        uids2 = uids;
                        addHistoryEventLocked(mSecRealtime, mSecUptime, 32769, str2, keyAt);
                        j = j2 + 1;
                        uids = uids2;
                    }
                    j2 = j;
                    uids2 = uids;
                }
                return;
            }
            return;
        }
        this.mActiveEvents.removeEvents(5);
        this.mActiveEvents.removeEvents(13);
        active = this.mActiveEvents.getStateForEvent(1);
        if (active != null) {
            mSecRealtime = this.mClocks.elapsedRealtime();
            mSecUptime = this.mClocks.uptimeMillis();
            for (Entry<String, SparseIntArray> ent2 : active.entrySet()) {
                uids = (SparseIntArray) ent2.getValue();
                j = 0;
                while (j < uids.size()) {
                    str = (String) ent2.getKey();
                    keyAt = uids.keyAt(j);
                    j2 = j;
                    str2 = str;
                    uids2 = uids;
                    addHistoryEventLocked(mSecRealtime, mSecUptime, 16385, str2, keyAt);
                    j = j2 + 1;
                    uids = uids2;
                }
                j2 = j;
                uids2 = uids;
            }
        }
    }

    public void setNoAutoReset(boolean enabled) {
        this.mNoAutoReset = enabled;
    }

    public void setPretendScreenOff(boolean pretendScreenOff) {
        if (this.mPretendScreenOff != pretendScreenOff) {
            this.mPretendScreenOff = pretendScreenOff;
            noteScreenStateLocked(pretendScreenOff ? 1 : 2);
        }
    }

    public void noteStartWakeLocked(int uid, int pid, WorkChain wc, String name, String historyName, int type, boolean unimportantForLogging, long elapsedRealtime, long uptime) {
        int i = type;
        long j = elapsedRealtime;
        long j2 = uptime;
        int uid2 = mapUid(uid);
        if (i == 0) {
            String historyName2;
            aggregateLastWakeupUptimeLocked(j2);
            if (historyName == null) {
                historyName2 = name;
            } else {
                historyName2 = historyName;
            }
            if (this.mRecordAllHistory && this.mActiveEvents.updateState(32773, historyName2, uid2, 0)) {
                addHistoryEventLocked(elapsedRealtime, uptime, 32773, historyName2, uid2);
            }
            HistoryItem historyItem;
            HistoryTag historyTag;
            if (this.mWakeLockNesting == 0) {
                historyItem = this.mHistoryCur;
                historyItem.states |= 1073741824;
                historyItem = this.mHistoryCur;
                historyItem.wakelockTag = historyItem.localWakelockTag;
                historyTag = this.mHistoryCur.wakelockTag;
                this.mInitialAcquireWakeName = historyName2;
                historyTag.string = historyName2;
                historyTag = this.mHistoryCur.wakelockTag;
                this.mInitialAcquireWakeUid = uid2;
                historyTag.uid = uid2;
                this.mWakeLockImportant = unimportantForLogging ^ 1;
                addHistoryRecordLocked(j, j2);
            } else if (!(this.mWakeLockImportant || unimportantForLogging || this.mHistoryLastWritten.cmd != (byte) 0)) {
                if (this.mHistoryLastWritten.wakelockTag != null) {
                    this.mHistoryLastWritten.wakelockTag = null;
                    historyItem = this.mHistoryCur;
                    historyItem.wakelockTag = historyItem.localWakelockTag;
                    historyTag = this.mHistoryCur.wakelockTag;
                    this.mInitialAcquireWakeName = historyName2;
                    historyTag.string = historyName2;
                    historyTag = this.mHistoryCur.wakelockTag;
                    this.mInitialAcquireWakeUid = uid2;
                    historyTag.uid = uid2;
                    addHistoryRecordLocked(j, j2);
                }
                this.mWakeLockImportant = true;
            }
            this.mWakeLockNesting++;
        }
        if (uid2 >= 0) {
            if (this.mOnBatteryScreenOffTimeBase.isRunning()) {
                requestWakelockCpuUpdate();
            }
            getUidStatsLocked(uid2).noteStartWakeLocked(pid, name, type, elapsedRealtime);
            if (wc != null) {
                StatsLogInternal.write(10, wc.getUids(), wc.getTags(), getPowerManagerWakeLockLevel(i), name, 1);
                return;
            }
            StatsLogInternal.write_non_chained(10, uid2, null, getPowerManagerWakeLockLevel(i), name, 1);
        }
    }

    public void noteStopWakeLocked(int uid, int pid, WorkChain wc, String name, String historyName, int type, long elapsedRealtime, long uptime) {
        int i = type;
        int uid2 = mapUid(uid);
        String historyName2;
        long j;
        long j2;
        if (i == 0) {
            this.mWakeLockNesting--;
            if (this.mRecordAllHistory) {
                if (historyName == null) {
                    historyName2 = name;
                } else {
                    historyName2 = historyName;
                }
                if (this.mActiveEvents.updateState(16389, historyName2, uid2, 0)) {
                    addHistoryEventLocked(elapsedRealtime, uptime, 16389, historyName2, uid2);
                }
            }
            if (this.mWakeLockNesting == 0) {
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states &= -1073741825;
                this.mInitialAcquireWakeName = null;
                this.mInitialAcquireWakeUid = -1;
                addHistoryRecordLocked(elapsedRealtime, uptime);
            } else {
                j = elapsedRealtime;
                j2 = uptime;
            }
        } else {
            j = elapsedRealtime;
            j2 = uptime;
            historyName2 = historyName;
        }
        if (uid2 >= 0) {
            if (this.mOnBatteryScreenOffTimeBase.isRunning()) {
                requestWakelockCpuUpdate();
            }
            getUidStatsLocked(uid2).noteStopWakeLocked(pid, name, type, elapsedRealtime);
            if (wc != null) {
                StatsLogInternal.write(10, wc.getUids(), wc.getTags(), getPowerManagerWakeLockLevel(i), name, 0);
                return;
            }
            StatsLogInternal.write_non_chained(10, uid2, null, getPowerManagerWakeLockLevel(i), name, 0);
        }
    }

    private int getPowerManagerWakeLockLevel(int battertStatsWakelockType) {
        if (battertStatsWakelockType == 0) {
            return 1;
        }
        if (battertStatsWakelockType == 1) {
            return 26;
        }
        String str = TAG;
        if (battertStatsWakelockType == 2) {
            Slog.e(str, "Illegal window wakelock type observed in batterystats.");
            return -1;
        } else if (battertStatsWakelockType == 18) {
            return 128;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Illegal wakelock type in batterystats: ");
            stringBuilder.append(battertStatsWakelockType);
            Slog.e(str, stringBuilder.toString());
            return -1;
        }
    }

    public void noteStartWakeFromSourceLocked(WorkSource ws, int pid, String name, String historyName, int type, boolean unimportantForLogging) {
        int i;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int N = ws.size();
        int i2 = 0;
        while (i2 < N) {
            int N2 = N;
            i = i2;
            noteStartWakeLocked(ws.get(i2), pid, null, name, historyName, type, unimportantForLogging, elapsedRealtime, uptime);
            i2 = i + 1;
            N = N2;
        }
        i = i2;
        List<WorkChain> wcs = ws.getWorkChains();
        if (wcs != null) {
            int i3;
            i2 = 0;
            while (i2 < wcs.size()) {
                WorkChain wc = (WorkChain) wcs.get(i2);
                List<WorkChain> wcs2 = wcs;
                i3 = i2;
                noteStartWakeLocked(wc.getAttributionUid(), pid, wc, name, historyName, type, unimportantForLogging, elapsedRealtime, uptime);
                i2 = i3 + 1;
                wcs = wcs2;
            }
            i3 = i2;
            return;
        }
    }

    public void noteChangeWakelockFromSourceLocked(WorkSource ws, int pid, String name, String historyName, int type, WorkSource newWs, int newPid, String newName, String newHistoryName, int newType, boolean newUnimportantForLogging) {
        int i;
        WorkChain newChain;
        List<WorkChain> newChains;
        int i2;
        WorkSource workSource = ws;
        WorkSource workSource2 = newWs;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        List<WorkChain>[] wcs = WorkSource.diffChains(workSource, workSource2);
        int NN = newWs.size();
        int i3 = 0;
        while (i3 < NN) {
            int NN2 = NN;
            i = i3;
            noteStartWakeLocked(workSource2.get(i3), newPid, null, newName, newHistoryName, newType, newUnimportantForLogging, elapsedRealtime, uptime);
            i3 = i + 1;
            NN = NN2;
        }
        i = i3;
        if (wcs != null) {
            List<WorkChain> newChains2 = wcs[0];
            if (newChains2 != null) {
                i3 = 0;
                while (i3 < newChains2.size()) {
                    newChain = (WorkChain) newChains2.get(i3);
                    newChains = newChains2;
                    i2 = i3;
                    noteStartWakeLocked(newChain.getAttributionUid(), newPid, newChain, newName, newHistoryName, newType, newUnimportantForLogging, elapsedRealtime, uptime);
                    i3 = i2 + 1;
                    newChains2 = newChains;
                }
                i2 = i3;
            }
        }
        i3 = ws.size();
        int i4 = 0;
        while (i4 < i3) {
            i = i4;
            noteStopWakeLocked(workSource.get(i4), pid, null, name, historyName, type, elapsedRealtime, uptime);
            i4 = i + 1;
        }
        i = i4;
        if (wcs != null) {
            List<WorkChain> goneChains = wcs[1];
            if (goneChains != null) {
                NN = 0;
                while (NN < goneChains.size()) {
                    newChain = (WorkChain) goneChains.get(NN);
                    newChains = goneChains;
                    i2 = NN;
                    noteStopWakeLocked(newChain.getAttributionUid(), pid, newChain, name, historyName, type, elapsedRealtime, uptime);
                    NN = i2 + 1;
                    goneChains = newChains;
                }
                i2 = NN;
                return;
            }
        }
    }

    public void noteStopWakeFromSourceLocked(WorkSource ws, int pid, String name, String historyName, int type) {
        int i;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int N = ws.size();
        int i2 = 0;
        while (i2 < N) {
            int N2 = N;
            i = i2;
            noteStopWakeLocked(ws.get(i2), pid, null, name, historyName, type, elapsedRealtime, uptime);
            i2 = i + 1;
            N = N2;
        }
        i = i2;
        List<WorkChain> wcs = ws.getWorkChains();
        if (wcs != null) {
            int i3;
            i2 = 0;
            while (i2 < wcs.size()) {
                WorkChain wc = (WorkChain) wcs.get(i2);
                List<WorkChain> wcs2 = wcs;
                i3 = i2;
                noteStopWakeLocked(wc.getAttributionUid(), pid, wc, name, historyName, type, elapsedRealtime, uptime);
                i2 = i3 + 1;
                wcs = wcs2;
            }
            i3 = i2;
            return;
        }
    }

    public void noteLongPartialWakelockStart(String name, String historyName, int uid) {
        noteLongPartialWakeLockStartInternal(name, historyName, mapUid(uid));
    }

    public void noteLongPartialWakelockStartFromSource(String name, String historyName, WorkSource workSource) {
        int N = workSource.size();
        for (int i = 0; i < N; i++) {
            noteLongPartialWakeLockStartInternal(name, historyName, mapUid(workSource.get(i)));
        }
        ArrayList<WorkChain> workChains = workSource.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteLongPartialWakeLockStartInternal(name, historyName, ((WorkChain) workChains.get(i2)).getAttributionUid());
            }
        }
    }

    private void noteLongPartialWakeLockStartInternal(String name, String historyName, int uid) {
        String historyName2;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (historyName == null) {
            historyName2 = name;
        } else {
            historyName2 = historyName;
        }
        if (this.mActiveEvents.updateState(HistoryItem.EVENT_LONG_WAKE_LOCK_START, historyName2, uid, 0)) {
            addHistoryEventLocked(elapsedRealtime, uptime, HistoryItem.EVENT_LONG_WAKE_LOCK_START, historyName2, uid);
        }
    }

    public void noteLongPartialWakelockFinish(String name, String historyName, int uid) {
        noteLongPartialWakeLockFinishInternal(name, historyName, mapUid(uid));
    }

    public void noteLongPartialWakelockFinishFromSource(String name, String historyName, WorkSource workSource) {
        int N = workSource.size();
        for (int i = 0; i < N; i++) {
            noteLongPartialWakeLockFinishInternal(name, historyName, mapUid(workSource.get(i)));
        }
        ArrayList<WorkChain> workChains = workSource.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteLongPartialWakeLockFinishInternal(name, historyName, ((WorkChain) workChains.get(i2)).getAttributionUid());
            }
        }
    }

    private void noteLongPartialWakeLockFinishInternal(String name, String historyName, int uid) {
        String historyName2;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (historyName == null) {
            historyName2 = name;
        } else {
            historyName2 = historyName;
        }
        if (this.mActiveEvents.updateState(HistoryItem.EVENT_LONG_WAKE_LOCK_FINISH, historyName2, uid, 0)) {
            addHistoryEventLocked(elapsedRealtime, uptime, HistoryItem.EVENT_LONG_WAKE_LOCK_FINISH, historyName2, uid);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void aggregateLastWakeupUptimeLocked(long uptimeMs) {
        SamplingTimer timer = this.mLastWakeupReason;
        if (timer != null) {
            long deltaUptime = uptimeMs - this.mLastWakeupUptimeMs;
            getWakeupReasonTimerLocked(timer).add(deltaUptime * 1000, 1);
            StatsLogInternal.write(36, this.mLastWakeupReason, 1000 * deltaUptime);
            this.mLastWakeupReason = null;
        }
    }

    public void noteWakeupReasonLocked(String reason) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        aggregateLastWakeupUptimeLocked(uptime);
        HistoryItem historyItem = this.mHistoryCur;
        historyItem.wakeReasonTag = historyItem.localWakeReasonTag;
        this.mHistoryCur.wakeReasonTag.string = reason;
        this.mHistoryCur.wakeReasonTag.uid = 0;
        this.mLastWakeupReason = reason;
        this.mLastWakeupUptimeMs = uptime;
        addHistoryRecordLocked(elapsedRealtime, uptime);
    }

    public boolean startAddingCpuLocked() {
        this.mExternalSync.cancelCpuSyncDueToWakelockChange();
        return this.mOnBatteryInternal;
    }

    public void finishAddingCpuLocked(int totalUTime, int totalSTime, int statUserTime, int statSystemTime, int statIOWaitTime, int statIrqTime, int statSoftIrqTime, int statIdleTime) {
        this.mCurStepCpuUserTime += (long) totalUTime;
        this.mCurStepCpuSystemTime += (long) totalSTime;
        this.mCurStepStatUserTime += (long) statUserTime;
        this.mCurStepStatSystemTime += (long) statSystemTime;
        this.mCurStepStatIOWaitTime += (long) statIOWaitTime;
        this.mCurStepStatIrqTime += (long) statIrqTime;
        this.mCurStepStatSoftIrqTime += (long) statSoftIrqTime;
        this.mCurStepStatIdleTime += (long) statIdleTime;
    }

    public void noteProcessDiedLocked(int uid, int pid) {
        Uid u = (Uid) this.mUidStats.get(mapUid(uid));
        if (u != null) {
            u.mPids.remove(pid);
        }
    }

    public long getProcessWakeTime(int uid, int pid, long realtime) {
        Uid u = (Uid) this.mUidStats.get(mapUid(uid));
        long j = 0;
        if (u != null) {
            Pid p = (Pid) u.mPids.get(pid);
            if (p != null) {
                long j2 = p.mWakeSumMs;
                if (p.mWakeNesting > 0) {
                    j = realtime - p.mWakeStartMs;
                }
                return j2 + j;
            }
        }
        return 0;
    }

    public void reportExcessiveCpuLocked(int uid, String proc, long overTime, long usedTime) {
        Uid u = (Uid) this.mUidStats.get(mapUid(uid));
        if (u != null) {
            u.reportExcessiveCpuLocked(proc, overTime, usedTime);
        }
    }

    public void noteStartSensorLocked(int uid, int sensor) {
        uid = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mSensorNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states |= 8388608;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mSensorNesting++;
        getUidStatsLocked(uid).noteStartSensor(sensor, elapsedRealtime);
    }

    public void noteStopSensorLocked(int uid, int sensor) {
        uid = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        this.mSensorNesting--;
        if (this.mSensorNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states &= -8388609;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        getUidStatsLocked(uid).noteStopSensor(sensor, elapsedRealtime);
    }

    public void noteGpsChangedLocked(WorkSource oldWs, WorkSource newWs) {
        int i;
        for (i = 0; i < newWs.size(); i++) {
            noteStartGpsLocked(newWs.get(i), null);
        }
        for (i = 0; i < oldWs.size(); i++) {
            noteStopGpsLocked(oldWs.get(i), null);
        }
        List<WorkChain>[] wcs = WorkSource.diffChains(oldWs, newWs);
        if (wcs != null) {
            List<WorkChain> newChains;
            int i2;
            if (wcs[0] != null) {
                newChains = wcs[0];
                for (i2 = 0; i2 < newChains.size(); i2++) {
                    noteStartGpsLocked(-1, (WorkChain) newChains.get(i2));
                }
            }
            if (wcs[1] != null) {
                newChains = wcs[1];
                for (i2 = 0; i2 < newChains.size(); i2++) {
                    noteStopGpsLocked(-1, (WorkChain) newChains.get(i2));
                }
            }
        }
    }

    private void noteStartGpsLocked(int uid, WorkChain workChain) {
        uid = getAttributionUid(uid, workChain);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mGpsNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states |= 536870912;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mGpsNesting++;
        if (workChain == null) {
            StatsLogInternal.write_non_chained(6, uid, null, 1);
        } else {
            StatsLogInternal.write(6, workChain.getUids(), workChain.getTags(), 1);
        }
        getUidStatsLocked(uid).noteStartGps(elapsedRealtime);
    }

    private void noteStopGpsLocked(int uid, WorkChain workChain) {
        uid = getAttributionUid(uid, workChain);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        this.mGpsNesting--;
        if (this.mGpsNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states &= -536870913;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            stopAllGpsSignalQualityTimersLocked(-1);
            this.mGpsSignalQualityBin = -1;
        }
        if (workChain == null) {
            StatsLogInternal.write_non_chained(6, uid, null, 0);
        } else {
            StatsLogInternal.write(6, workChain.getUids(), workChain.getTags(), 0);
        }
        getUidStatsLocked(uid).noteStopGps(elapsedRealtime);
    }

    public void noteGpsSignalQualityLocked(int signalLevel) {
        if (this.mGpsNesting != 0) {
            if (signalLevel < 0 || signalLevel >= 2) {
                stopAllGpsSignalQualityTimersLocked(-1);
                return;
            }
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            int i = this.mGpsSignalQualityBin;
            if (i != signalLevel) {
                if (i >= 0) {
                    this.mGpsSignalQualityTimer[i].stopRunningLocked(elapsedRealtime);
                }
                if (!this.mGpsSignalQualityTimer[signalLevel].isRunningLocked()) {
                    this.mGpsSignalQualityTimer[signalLevel].startRunningLocked(elapsedRealtime);
                }
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states2 = (historyItem.states2 & -129) | (signalLevel << 7);
                addHistoryRecordLocked(elapsedRealtime, uptime);
                this.mGpsSignalQualityBin = signalLevel;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0030  */
    @com.android.internal.annotations.GuardedBy({"this"})
    public void noteScreenStateLocked(int r23) {
        /*
        r22 = this;
        r12 = r22;
        r0 = r12.mPretendScreenOff;
        r1 = 1;
        if (r0 == 0) goto L_0x0009;
    L_0x0007:
        r0 = r1;
        goto L_0x000b;
    L_0x0009:
        r0 = r23;
    L_0x000b:
        r2 = 4;
        r3 = "BatteryStatsImpl";
        if (r0 <= r2) goto L_0x002b;
    L_0x0010:
        r2 = 5;
        if (r0 == r2) goto L_0x0028;
    L_0x0013:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r4 = "Unknown screen state (not mapped): ";
        r2.append(r4);
        r2.append(r0);
        r2 = r2.toString();
        android.util.Slog.wtf(r3, r2);
        goto L_0x002b;
    L_0x0028:
        r0 = 2;
        r13 = r0;
        goto L_0x002c;
    L_0x002b:
        r13 = r0;
    L_0x002c:
        r0 = r12.mScreenState;
        if (r0 == r13) goto L_0x0164;
    L_0x0030:
        r12.recordDailyStatsIfNeededLocked(r1);
        r14 = r12.mScreenState;
        r12.mScreenState = r13;
        if (r13 == 0) goto L_0x0063;
    L_0x0039:
        r0 = r13 + -1;
        r1 = r0 & 3;
        if (r1 != r0) goto L_0x004f;
    L_0x003f:
        r1 = r12.mModStepMode;
        r2 = r12.mCurStepMode;
        r3 = r2 & 3;
        r3 = r3 ^ r0;
        r1 = r1 | r3;
        r12.mModStepMode = r1;
        r1 = r2 & -4;
        r1 = r1 | r0;
        r12.mCurStepMode = r1;
        goto L_0x0063;
    L_0x004f:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unexpected screen state: ";
        r1.append(r2);
        r1.append(r13);
        r1 = r1.toString();
        android.util.Slog.wtf(r3, r1);
    L_0x0063:
        r0 = r12.mClocks;
        r10 = r0.elapsedRealtime();
        r0 = r12.mClocks;
        r8 = r0.uptimeMillis();
        r0 = 0;
        r1 = r12.isScreenDoze(r13);
        if (r1 == 0) goto L_0x0086;
    L_0x0076:
        r1 = r12.mHistoryCur;
        r2 = r1.states;
        r3 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        r2 = r2 | r3;
        r1.states = r2;
        r1 = r12.mScreenDozeTimer;
        r1.startRunningLocked(r10);
        r0 = 1;
        goto L_0x009c;
    L_0x0086:
        r1 = r12.isScreenDoze(r14);
        if (r1 == 0) goto L_0x009c;
    L_0x008c:
        r1 = r12.mHistoryCur;
        r2 = r1.states;
        r3 = -262145; // 0xfffffffffffbffff float:NaN double:NaN;
        r2 = r2 & r3;
        r1.states = r2;
        r1 = r12.mScreenDozeTimer;
        r1.stopRunningLocked(r10);
        r0 = 1;
    L_0x009c:
        r1 = r12.isScreenOn(r13);
        if (r1 == 0) goto L_0x00be;
    L_0x00a2:
        r1 = r12.mHistoryCur;
        r2 = r1.states;
        r3 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r2 = r2 | r3;
        r1.states = r2;
        r1 = r12.mScreenOnTimer;
        r1.startRunningLocked(r10);
        r1 = r12.mScreenBrightnessBin;
        if (r1 < 0) goto L_0x00bb;
    L_0x00b4:
        r2 = r12.mScreenBrightnessTimer;
        r1 = r2[r1];
        r1.startRunningLocked(r10);
    L_0x00bb:
        r0 = 1;
        r15 = r0;
        goto L_0x00e2;
    L_0x00be:
        r1 = r12.isScreenOn(r14);
        if (r1 == 0) goto L_0x00e1;
    L_0x00c4:
        r1 = r12.mHistoryCur;
        r2 = r1.states;
        r3 = -1048577; // 0xffffffffffefffff float:NaN double:NaN;
        r2 = r2 & r3;
        r1.states = r2;
        r1 = r12.mScreenOnTimer;
        r1.stopRunningLocked(r10);
        r1 = r12.mScreenBrightnessBin;
        if (r1 < 0) goto L_0x00de;
    L_0x00d7:
        r2 = r12.mScreenBrightnessTimer;
        r1 = r2[r1];
        r1.stopRunningLocked(r10);
    L_0x00de:
        r0 = 1;
        r15 = r0;
        goto L_0x00e2;
    L_0x00e1:
        r15 = r0;
    L_0x00e2:
        if (r15 == 0) goto L_0x00e7;
    L_0x00e4:
        r12.addHistoryRecordLocked(r10, r8);
    L_0x00e7:
        r0 = r12.mExternalSync;
        r1 = r12.mOnBatteryTimeBase;
        r1 = r1.isRunning();
        r2 = r12.mOnBatteryScreenOffTimeBase;
        r2 = r2.isRunning();
        r0.scheduleCpuSyncDueToScreenStateChange(r1, r2);
        r0 = r12.isScreenOn(r13);
        r16 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        if (r0 == 0) goto L_0x012a;
    L_0x0100:
        r0 = r12.mOnBatteryTimeBase;
        r1 = r0.isRunning();
        r0 = r12.mClocks;
        r2 = r0.uptimeMillis();
        r3 = r2 * r16;
        r5 = r10 * r16;
        r0 = r22;
        r2 = r13;
        r0.updateTimeBasesLocked(r1, r2, r3, r5);
        r1 = -1;
        r2 = -1;
        r3 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r4 = "screen";
        r18 = r8;
        r8 = r10;
        r20 = r10;
        r10 = r18;
        r0.noteStartWakeLocked(r1, r2, r3, r4, r5, r6, r7, r8, r10);
        goto L_0x015d;
    L_0x012a:
        r18 = r8;
        r20 = r10;
        r0 = r12.isScreenOn(r14);
        if (r0 == 0) goto L_0x015d;
    L_0x0134:
        r1 = -1;
        r2 = -1;
        r3 = 0;
        r6 = 0;
        r4 = "screen";
        r5 = "screen";
        r0 = r22;
        r7 = r20;
        r9 = r18;
        r0.noteStopWakeLocked(r1, r2, r3, r4, r5, r6, r7, r9);
        r0 = r12.mOnBatteryTimeBase;
        r1 = r0.isRunning();
        r0 = r12.mClocks;
        r2 = r0.uptimeMillis();
        r3 = r2 * r16;
        r5 = r20 * r16;
        r0 = r22;
        r2 = r13;
        r0.updateTimeBasesLocked(r1, r2, r3, r5);
    L_0x015d:
        r0 = r12.mOnBatteryInternal;
        if (r0 == 0) goto L_0x0164;
    L_0x0161:
        r12.updateDischargeScreenLevelsLocked(r14, r13);
    L_0x0164:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.noteScreenStateLocked(int):void");
    }

    @UnsupportedAppUsage
    public void noteScreenBrightnessLocked(int brightness) {
        int bin = brightness / ((1 << DeviceFeature.BACKLIGHT_BIT) / 5);
        if (bin < 0) {
            bin = 0;
        } else if (bin >= 5) {
            bin = 4;
        }
        if (this.mScreenBrightnessBin != bin) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states = (historyItem.states & -8) | (bin << 0);
            addHistoryRecordLocked(elapsedRealtime, uptime);
            if (this.mScreenState == 2) {
                int i = this.mScreenBrightnessBin;
                if (i >= 0) {
                    this.mScreenBrightnessTimer[i].stopRunningLocked(elapsedRealtime);
                }
                this.mScreenBrightnessTimer[bin].startRunningLocked(elapsedRealtime);
            }
            this.mScreenBrightnessBin = bin;
        }
    }

    @UnsupportedAppUsage
    public void noteUserActivityLocked(int uid, int event) {
        if (this.mOnBatteryInternal) {
            getUidStatsLocked(mapUid(uid)).noteUserActivityLocked(event);
        }
    }

    public void noteWakeUpLocked(String reason, int reasonUid) {
        addHistoryEventLocked(this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis(), 18, reason, reasonUid);
    }

    public void noteInteractiveLocked(boolean interactive) {
        if (this.mInteractive != interactive) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            this.mInteractive = interactive;
            if (interactive) {
                this.mInteractiveTimer.startRunningLocked(elapsedRealtime);
            } else {
                this.mInteractiveTimer.stopRunningLocked(elapsedRealtime);
            }
        }
    }

    public void noteConnectivityChangedLocked(int type, String extra) {
        addHistoryEventLocked(this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis(), 9, extra, type);
        this.mNumConnectivityChange++;
    }

    private void noteMobileRadioApWakeupLocked(long elapsedRealtimeMillis, long uptimeMillis, int uid) {
        uid = mapUid(uid);
        addHistoryEventLocked(elapsedRealtimeMillis, uptimeMillis, 19, "", uid);
        getUidStatsLocked(uid).noteMobileRadioApWakeupLocked();
    }

    public boolean noteMobileRadioPowerStateLocked(int powerState, long timestampNs, int uid) {
        int i = powerState;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mMobileRadioPowerState != i) {
            long j;
            boolean z = i == 2 || i == 3;
            boolean active = z;
            long realElapsedRealtimeMs;
            if (active) {
                if (uid > 0) {
                    noteMobileRadioApWakeupLocked(elapsedRealtime, uptime, uid);
                }
                j = timestampNs / TimeUtils.NANOS_PER_MS;
                realElapsedRealtimeMs = j;
                this.mMobileRadioActiveStartTime = j;
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states |= 33554432;
                j = realElapsedRealtimeMs;
            } else {
                j = timestampNs / TimeUtils.NANOS_PER_MS;
                realElapsedRealtimeMs = this.mMobileRadioActiveStartTime;
                if (j < realElapsedRealtimeMs) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Data connection inactive timestamp ");
                    stringBuilder.append(j);
                    stringBuilder.append(" is before start time ");
                    stringBuilder.append(realElapsedRealtimeMs);
                    Slog.wtf(TAG, stringBuilder.toString());
                    j = elapsedRealtime;
                } else if (j < elapsedRealtime) {
                    this.mMobileRadioActiveAdjustedTime.addCountLocked(elapsedRealtime - j);
                }
                HistoryItem historyItem2 = this.mHistoryCur;
                historyItem2.states &= -33554433;
            }
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mMobileRadioPowerState = i;
            if (active) {
                this.mMobileRadioActiveTimer.startRunningLocked(elapsedRealtime);
                this.mMobileRadioActivePerAppTimer.startRunningLocked(elapsedRealtime);
            } else {
                this.mMobileRadioActiveTimer.stopRunningLocked(j);
                this.mMobileRadioActivePerAppTimer.stopRunningLocked(j);
                return true;
            }
        }
        return false;
    }

    public void notePowerSaveModeLocked(boolean enabled) {
        if (this.mPowerSaveModeEnabled != enabled) {
            int i = 0;
            int stepState = enabled ? 4 : 0;
            int i2 = this.mModStepMode;
            int i3 = this.mCurStepMode;
            this.mModStepMode = i2 | ((i3 & 4) ^ stepState);
            this.mCurStepMode = (i3 & -5) | stepState;
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mPowerSaveModeEnabled = enabled;
            HistoryItem historyItem;
            if (enabled) {
                historyItem = this.mHistoryCur;
                historyItem.states2 |= Integer.MIN_VALUE;
                this.mPowerSaveModeEnabledTimer.startRunningLocked(elapsedRealtime);
            } else {
                historyItem = this.mHistoryCur;
                historyItem.states2 &= Integer.MAX_VALUE;
                this.mPowerSaveModeEnabledTimer.stopRunningLocked(elapsedRealtime);
            }
            addHistoryRecordLocked(elapsedRealtime, uptime);
            if (enabled) {
                i = 1;
            }
            StatsLogInternal.write(20, i);
        }
    }

    public void noteDeviceIdleModeLocked(int mode, String activeReason, int activeUid) {
        boolean nowIdling;
        boolean nowLightIdling;
        boolean nowLightIdling2;
        boolean nowIdling2;
        int statsmode;
        int i;
        int i2 = mode;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        boolean nowIdling3 = i2 == 2;
        if (this.mDeviceIdling && !nowIdling3 && activeReason == null) {
            nowIdling = true;
        } else {
            nowIdling = nowIdling3;
        }
        nowIdling3 = i2 == 1;
        if (!this.mDeviceLightIdling || nowIdling3 || nowIdling || activeReason != null) {
            nowLightIdling = nowIdling3;
        } else {
            nowLightIdling = true;
        }
        if (activeReason == null) {
            nowLightIdling2 = nowLightIdling;
            nowIdling2 = nowIdling;
        } else if (this.mDeviceIdling || this.mDeviceLightIdling) {
            nowLightIdling2 = nowLightIdling;
            nowIdling2 = nowIdling;
            addHistoryEventLocked(elapsedRealtime, uptime, 10, activeReason, activeUid);
        } else {
            nowLightIdling2 = nowLightIdling;
            nowIdling2 = nowIdling;
        }
        if (!(this.mDeviceIdling == nowIdling2 && this.mDeviceLightIdling == nowLightIdling2)) {
            if (nowIdling2) {
                statsmode = 2;
            } else if (nowLightIdling2) {
                statsmode = 1;
            } else {
                statsmode = 0;
            }
            StatsLogInternal.write(22, statsmode);
        }
        if (this.mDeviceIdling != nowIdling2) {
            this.mDeviceIdling = nowIdling2;
            statsmode = nowIdling2 ? 8 : 0;
            int i3 = this.mModStepMode;
            i = this.mCurStepMode;
            this.mModStepMode = i3 | ((i & 8) ^ statsmode);
            this.mCurStepMode = (i & -9) | statsmode;
            if (nowIdling2) {
                this.mDeviceIdlingTimer.startRunningLocked(elapsedRealtime);
            } else {
                this.mDeviceIdlingTimer.stopRunningLocked(elapsedRealtime);
            }
        }
        if (this.mDeviceLightIdling != nowLightIdling2) {
            this.mDeviceLightIdling = nowLightIdling2;
            if (nowLightIdling2) {
                this.mDeviceLightIdlingTimer.startRunningLocked(elapsedRealtime);
            } else {
                this.mDeviceLightIdlingTimer.stopRunningLocked(elapsedRealtime);
            }
        }
        if (this.mDeviceIdleMode != i2) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 = (historyItem.states2 & -100663297) | (i2 << 25);
            addHistoryRecordLocked(elapsedRealtime, uptime);
            long lastDuration = elapsedRealtime - this.mLastIdleTimeStart;
            this.mLastIdleTimeStart = elapsedRealtime;
            i = this.mDeviceIdleMode;
            if (i == 1) {
                if (lastDuration > this.mLongestLightIdleTime) {
                    this.mLongestLightIdleTime = lastDuration;
                }
                this.mDeviceIdleModeLightTimer.stopRunningLocked(elapsedRealtime);
            } else if (i == 2) {
                if (lastDuration > this.mLongestFullIdleTime) {
                    this.mLongestFullIdleTime = lastDuration;
                }
                this.mDeviceIdleModeFullTimer.stopRunningLocked(elapsedRealtime);
            }
            if (i2 == 1) {
                this.mDeviceIdleModeLightTimer.startRunningLocked(elapsedRealtime);
            } else if (i2 == 2) {
                this.mDeviceIdleModeFullTimer.startRunningLocked(elapsedRealtime);
            }
            this.mDeviceIdleMode = i2;
            StatsLogInternal.write(21, i2);
        }
    }

    public void notePackageInstalledLocked(String pkgName, long versionCode) {
        long j = versionCode;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        addHistoryEventLocked(elapsedRealtime, this.mClocks.uptimeMillis(), 11, pkgName, (int) j);
        PackageChange pc = new PackageChange();
        pc.mPackageName = pkgName;
        pc.mUpdate = true;
        pc.mVersionCode = j;
        addPackageChange(pc);
    }

    public void notePackageUninstalledLocked(String pkgName) {
        addHistoryEventLocked(this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis(), 12, pkgName, 0);
        PackageChange pc = new PackageChange();
        pc.mPackageName = pkgName;
        pc.mUpdate = true;
        addPackageChange(pc);
    }

    private void addPackageChange(PackageChange pc) {
        if (this.mDailyPackageChanges == null) {
            this.mDailyPackageChanges = new ArrayList();
        }
        this.mDailyPackageChanges.add(pc);
    }

    /* Access modifiers changed, original: 0000 */
    public void stopAllGpsSignalQualityTimersLocked(int except) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        for (int i = 0; i < 2; i++) {
            if (i != except) {
                while (this.mGpsSignalQualityTimer[i].isRunningLocked()) {
                    this.mGpsSignalQualityTimer[i].stopRunningLocked(elapsedRealtime);
                }
            }
        }
    }

    @UnsupportedAppUsage
    public void notePhoneOnLocked() {
        if (!this.mPhoneOn) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 |= 8388608;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mPhoneOn = true;
            this.mPhoneOnTimer.startRunningLocked(elapsedRealtime);
        }
    }

    @UnsupportedAppUsage
    public void notePhoneOffLocked() {
        if (this.mPhoneOn) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -8388609;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mPhoneOn = false;
            this.mPhoneOnTimer.stopRunningLocked(elapsedRealtime);
        }
    }

    private void registerUsbStateReceiver(Context context) {
        IntentFilter usbStateFilter = new IntentFilter();
        usbStateFilter.addAction(UsbManager.ACTION_USB_STATE);
        context.registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                boolean state = intent.getBooleanExtra("connected", false);
                synchronized (BatteryStatsImpl.this) {
                    BatteryStatsImpl.this.noteUsbConnectionStateLocked(state);
                }
            }
        }, usbStateFilter);
        synchronized (this) {
            if (this.mUsbDataState == 0) {
                Intent usbState = context.registerReceiver(null, usbStateFilter);
                boolean initState = false;
                if (usbState != null && usbState.getBooleanExtra("connected", false)) {
                    initState = true;
                }
                noteUsbConnectionStateLocked(initState);
            }
        }
    }

    private void noteUsbConnectionStateLocked(boolean connected) {
        int newState = connected ? 2 : 1;
        if (this.mUsbDataState != newState) {
            this.mUsbDataState = newState;
            HistoryItem historyItem;
            if (connected) {
                historyItem = this.mHistoryCur;
                historyItem.states2 |= 262144;
            } else {
                historyItem = this.mHistoryCur;
                historyItem.states2 &= -262145;
            }
            addHistoryRecordLocked(this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void stopAllPhoneSignalStrengthTimersLocked(int except) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        for (int i = 0; i < 5; i++) {
            if (i != except) {
                while (this.mPhoneSignalStrengthsTimer[i].isRunningLocked()) {
                    this.mPhoneSignalStrengthsTimer[i].stopRunningLocked(elapsedRealtime);
                }
            }
        }
    }

    private int fixPhoneServiceState(int state, int signalBin) {
        if (this.mPhoneSimStateRaw == 1 && state == 1 && signalBin > 0) {
            return 0;
        }
        return state;
    }

    private void updateAllPhoneStateLocked(int state, int simState, int strengthBin) {
        HistoryItem historyItem;
        boolean scanning = false;
        boolean newHistory = false;
        this.mPhoneServiceStateRaw = state;
        this.mPhoneSimStateRaw = simState;
        this.mPhoneSignalStrengthBinRaw = strengthBin;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (simState == 1 && state == 1 && strengthBin > 0) {
            state = 0;
        }
        if (state == 3) {
            strengthBin = -1;
        } else if (state != 0 && state == 1) {
            scanning = true;
            strengthBin = 0;
            if (!this.mPhoneSignalScanningTimer.isRunningLocked()) {
                historyItem = this.mHistoryCur;
                historyItem.states |= 2097152;
                newHistory = true;
                this.mPhoneSignalScanningTimer.startRunningLocked(elapsedRealtime);
                StatsLogInternal.write(94, state, simState, 0);
            }
        }
        if (!scanning && this.mPhoneSignalScanningTimer.isRunningLocked()) {
            historyItem = this.mHistoryCur;
            historyItem.states &= -2097153;
            newHistory = true;
            this.mPhoneSignalScanningTimer.stopRunningLocked(elapsedRealtime);
            StatsLogInternal.write(94, state, simState, strengthBin);
        }
        if (this.mPhoneServiceState != state) {
            historyItem = this.mHistoryCur;
            historyItem.states = (historyItem.states & -449) | (state << 6);
            newHistory = true;
            this.mPhoneServiceState = state;
        }
        int i = this.mPhoneSignalStrengthBin;
        if (i != strengthBin) {
            if (i >= 0) {
                this.mPhoneSignalStrengthsTimer[i].stopRunningLocked(elapsedRealtime);
            }
            if (strengthBin >= 0) {
                if (!this.mPhoneSignalStrengthsTimer[strengthBin].isRunningLocked()) {
                    this.mPhoneSignalStrengthsTimer[strengthBin].startRunningLocked(elapsedRealtime);
                }
                historyItem = this.mHistoryCur;
                historyItem.states = (historyItem.states & -57) | (strengthBin << 3);
                newHistory = true;
                StatsLogInternal.write(40, strengthBin);
            } else {
                stopAllPhoneSignalStrengthTimersLocked(-1);
            }
            this.mPhoneSignalStrengthBin = strengthBin;
        }
        if (newHistory) {
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
    }

    public void notePhoneStateLocked(int state, int simState) {
        updateAllPhoneStateLocked(state, simState, this.mPhoneSignalStrengthBinRaw);
    }

    @UnsupportedAppUsage
    public void notePhoneSignalStrengthLocked(SignalStrength signalStrength) {
        updateAllPhoneStateLocked(this.mPhoneServiceStateRaw, this.mPhoneSimStateRaw, signalStrength.getLevel());
    }

    @UnsupportedAppUsage
    public void notePhoneDataConnectionStateLocked(int dataType, boolean hasData) {
        int bin = 0;
        if (hasData) {
            if (dataType <= 0 || dataType > 20) {
                bin = 21;
            } else {
                bin = dataType;
            }
        }
        if (this.mPhoneDataConnectionType != bin) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states = (historyItem.states & -15873) | (bin << 9);
            addHistoryRecordLocked(elapsedRealtime, uptime);
            int i = this.mPhoneDataConnectionType;
            if (i >= 0) {
                this.mPhoneDataConnectionsTimer[i].stopRunningLocked(elapsedRealtime);
            }
            this.mPhoneDataConnectionType = bin;
            this.mPhoneDataConnectionsTimer[bin].startRunningLocked(elapsedRealtime);
        }
    }

    public void noteWifiOnLocked() {
        if (!this.mWifiOn) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 |= 268435456;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mWifiOn = true;
            this.mWifiOnTimer.startRunningLocked(elapsedRealtime);
            scheduleSyncExternalStatsLocked("wifi-off", 2);
        }
    }

    public void noteWifiOffLocked() {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mWifiOn) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -268435457;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mWifiOn = false;
            this.mWifiOnTimer.stopRunningLocked(elapsedRealtime);
            scheduleSyncExternalStatsLocked("wifi-on", 2);
        }
    }

    @UnsupportedAppUsage
    public void noteAudioOnLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mAudioOnNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states |= 4194304;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mAudioOnTimer.startRunningLocked(elapsedRealtime);
        }
        this.mAudioOnNesting++;
        getUidStatsLocked(uid).noteAudioTurnedOnLocked(elapsedRealtime);
    }

    @UnsupportedAppUsage
    public void noteAudioOffLocked(int uid) {
        if (this.mAudioOnNesting != 0) {
            uid = mapUid(uid);
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            int i = this.mAudioOnNesting - 1;
            this.mAudioOnNesting = i;
            if (i == 0) {
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states &= -4194305;
                addHistoryRecordLocked(elapsedRealtime, uptime);
                this.mAudioOnTimer.stopRunningLocked(elapsedRealtime);
            }
            getUidStatsLocked(uid).noteAudioTurnedOffLocked(elapsedRealtime);
        }
    }

    @UnsupportedAppUsage
    public void noteVideoOnLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mVideoOnNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 |= 1073741824;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mVideoOnTimer.startRunningLocked(elapsedRealtime);
        }
        this.mVideoOnNesting++;
        getUidStatsLocked(uid).noteVideoTurnedOnLocked(elapsedRealtime);
    }

    @UnsupportedAppUsage
    public void noteVideoOffLocked(int uid) {
        if (this.mVideoOnNesting != 0) {
            uid = mapUid(uid);
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            int i = this.mVideoOnNesting - 1;
            this.mVideoOnNesting = i;
            if (i == 0) {
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states2 &= -1073741825;
                addHistoryRecordLocked(elapsedRealtime, uptime);
                this.mVideoOnTimer.stopRunningLocked(elapsedRealtime);
            }
            getUidStatsLocked(uid).noteVideoTurnedOffLocked(elapsedRealtime);
        }
    }

    public void noteResetAudioLocked() {
        if (this.mAudioOnNesting > 0) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mAudioOnNesting = 0;
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states &= -4194305;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mAudioOnTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                ((Uid) this.mUidStats.valueAt(i)).noteResetAudioLocked(elapsedRealtime);
            }
        }
    }

    public void noteResetVideoLocked() {
        if (this.mVideoOnNesting > 0) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mAudioOnNesting = 0;
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -1073741825;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mVideoOnTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                ((Uid) this.mUidStats.valueAt(i)).noteResetVideoLocked(elapsedRealtime);
            }
        }
    }

    public void noteActivityResumedLocked(int uid) {
        getUidStatsLocked(mapUid(uid)).noteActivityResumedLocked(this.mClocks.elapsedRealtime());
    }

    public void noteActivityPausedLocked(int uid) {
        getUidStatsLocked(mapUid(uid)).noteActivityPausedLocked(this.mClocks.elapsedRealtime());
    }

    public void noteVibratorOnLocked(int uid, long durationMillis) {
        getUidStatsLocked(mapUid(uid)).noteVibratorOnLocked(durationMillis);
    }

    public void noteVibratorOffLocked(int uid) {
        getUidStatsLocked(mapUid(uid)).noteVibratorOffLocked();
    }

    public void noteFlashlightOnLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int i = this.mFlashlightOnNesting;
        this.mFlashlightOnNesting = i + 1;
        if (i == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 |= 134217728;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mFlashlightOnTimer.startRunningLocked(elapsedRealtime);
        }
        getUidStatsLocked(uid).noteFlashlightTurnedOnLocked(elapsedRealtime);
    }

    public void noteFlashlightOffLocked(int uid) {
        if (this.mFlashlightOnNesting != 0) {
            uid = mapUid(uid);
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            int i = this.mFlashlightOnNesting - 1;
            this.mFlashlightOnNesting = i;
            if (i == 0) {
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states2 &= -134217729;
                addHistoryRecordLocked(elapsedRealtime, uptime);
                this.mFlashlightOnTimer.stopRunningLocked(elapsedRealtime);
            }
            getUidStatsLocked(uid).noteFlashlightTurnedOffLocked(elapsedRealtime);
        }
    }

    public void noteCameraOnLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int i = this.mCameraOnNesting;
        this.mCameraOnNesting = i + 1;
        if (i == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 |= 2097152;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mCameraOnTimer.startRunningLocked(elapsedRealtime);
        }
        getUidStatsLocked(uid).noteCameraTurnedOnLocked(elapsedRealtime);
    }

    public void noteCameraOffLocked(int uid) {
        if (this.mCameraOnNesting != 0) {
            uid = mapUid(uid);
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            int i = this.mCameraOnNesting - 1;
            this.mCameraOnNesting = i;
            if (i == 0) {
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states2 &= -2097153;
                addHistoryRecordLocked(elapsedRealtime, uptime);
                this.mCameraOnTimer.stopRunningLocked(elapsedRealtime);
            }
            getUidStatsLocked(uid).noteCameraTurnedOffLocked(elapsedRealtime);
        }
    }

    public void noteResetCameraLocked() {
        if (this.mCameraOnNesting > 0) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mCameraOnNesting = 0;
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -2097153;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mCameraOnTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                ((Uid) this.mUidStats.valueAt(i)).noteResetCameraLocked(elapsedRealtime);
            }
        }
    }

    public void noteResetFlashlightLocked() {
        if (this.mFlashlightOnNesting > 0) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mFlashlightOnNesting = 0;
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -134217729;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mFlashlightOnTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                ((Uid) this.mUidStats.valueAt(i)).noteResetFlashlightLocked(elapsedRealtime);
            }
        }
    }

    private void noteBluetoothScanStartedLocked(WorkChain workChain, int uid, boolean isUnoptimized) {
        uid = getAttributionUid(uid, workChain);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mBluetoothScanNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 |= 1048576;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mBluetoothScanTimer.startRunningLocked(elapsedRealtime);
        }
        this.mBluetoothScanNesting++;
        getUidStatsLocked(uid).noteBluetoothScanStartedLocked(elapsedRealtime, isUnoptimized);
    }

    public void noteBluetoothScanStartedFromSourceLocked(WorkSource ws, boolean isUnoptimized) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteBluetoothScanStartedLocked(null, ws.get(i), isUnoptimized);
        }
        List<WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteBluetoothScanStartedLocked((WorkChain) workChains.get(i2), -1, isUnoptimized);
            }
        }
    }

    private void noteBluetoothScanStoppedLocked(WorkChain workChain, int uid, boolean isUnoptimized) {
        uid = getAttributionUid(uid, workChain);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        this.mBluetoothScanNesting--;
        if (this.mBluetoothScanNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -1048577;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mBluetoothScanTimer.stopRunningLocked(elapsedRealtime);
        }
        getUidStatsLocked(uid).noteBluetoothScanStoppedLocked(elapsedRealtime, isUnoptimized);
    }

    private int getAttributionUid(int uid, WorkChain workChain) {
        if (workChain != null) {
            return mapUid(workChain.getAttributionUid());
        }
        return mapUid(uid);
    }

    public void noteBluetoothScanStoppedFromSourceLocked(WorkSource ws, boolean isUnoptimized) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteBluetoothScanStoppedLocked(null, ws.get(i), isUnoptimized);
        }
        List<WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteBluetoothScanStoppedLocked((WorkChain) workChains.get(i2), -1, isUnoptimized);
            }
        }
    }

    public void noteResetBluetoothScanLocked() {
        if (this.mBluetoothScanNesting > 0) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mBluetoothScanNesting = 0;
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -1048577;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mBluetoothScanTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                ((Uid) this.mUidStats.valueAt(i)).noteResetBluetoothScanLocked(elapsedRealtime);
            }
        }
    }

    public void noteBluetoothScanResultsFromSourceLocked(WorkSource ws, int numNewResults) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            getUidStatsLocked(mapUid(ws.get(i))).noteBluetoothScanResultsLocked(numNewResults);
        }
        List<WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                getUidStatsLocked(mapUid(((WorkChain) workChains.get(i2)).getAttributionUid())).noteBluetoothScanResultsLocked(numNewResults);
            }
        }
    }

    private void noteWifiRadioApWakeupLocked(long elapsedRealtimeMillis, long uptimeMillis, int uid) {
        uid = mapUid(uid);
        addHistoryEventLocked(elapsedRealtimeMillis, uptimeMillis, 19, "", uid);
        getUidStatsLocked(uid).noteWifiRadioApWakeupLocked();
    }

    public void noteWifiRadioPowerState(int powerState, long timestampNs, int uid) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mWifiRadioPowerState != powerState) {
            boolean active = powerState == 2 || powerState == 3;
            HistoryItem historyItem;
            if (active) {
                if (uid > 0) {
                    noteWifiRadioApWakeupLocked(elapsedRealtime, uptime, uid);
                }
                historyItem = this.mHistoryCur;
                historyItem.states |= 67108864;
                this.mWifiActiveTimer.startRunningLocked(elapsedRealtime);
            } else {
                historyItem = this.mHistoryCur;
                historyItem.states &= -67108865;
                this.mWifiActiveTimer.stopRunningLocked(timestampNs / TimeUtils.NANOS_PER_MS);
            }
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mWifiRadioPowerState = powerState;
        }
    }

    public void noteWifiRunningLocked(WorkSource ws) {
        if (this.mGlobalWifiRunning) {
            Log.w(TAG, "noteWifiRunningLocked -- called while WIFI running");
            return;
        }
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        HistoryItem historyItem = this.mHistoryCur;
        historyItem.states2 |= 536870912;
        addHistoryRecordLocked(elapsedRealtime, uptime);
        this.mGlobalWifiRunning = true;
        this.mGlobalWifiRunningTimer.startRunningLocked(elapsedRealtime);
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            getUidStatsLocked(mapUid(ws.get(i))).noteWifiRunningLocked(elapsedRealtime);
        }
        List<WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                getUidStatsLocked(mapUid(((WorkChain) workChains.get(i2)).getAttributionUid())).noteWifiRunningLocked(elapsedRealtime);
            }
        }
        scheduleSyncExternalStatsLocked("wifi-running", 2);
    }

    public void noteWifiRunningChangedLocked(WorkSource oldWs, WorkSource newWs) {
        if (this.mGlobalWifiRunning) {
            int i;
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            int N = oldWs.size();
            for (int i2 = 0; i2 < N; i2++) {
                getUidStatsLocked(mapUid(oldWs.get(i2))).noteWifiStoppedLocked(elapsedRealtime);
            }
            List<WorkChain> workChains = oldWs.getWorkChains();
            if (workChains != null) {
                for (i = 0; i < workChains.size(); i++) {
                    getUidStatsLocked(mapUid(((WorkChain) workChains.get(i)).getAttributionUid())).noteWifiStoppedLocked(elapsedRealtime);
                }
            }
            N = newWs.size();
            for (i = 0; i < N; i++) {
                getUidStatsLocked(mapUid(newWs.get(i))).noteWifiRunningLocked(elapsedRealtime);
            }
            workChains = newWs.getWorkChains();
            if (workChains != null) {
                for (i = 0; i < workChains.size(); i++) {
                    getUidStatsLocked(mapUid(((WorkChain) workChains.get(i)).getAttributionUid())).noteWifiRunningLocked(elapsedRealtime);
                }
                return;
            }
            return;
        }
        Log.w(TAG, "noteWifiRunningChangedLocked -- called while WIFI not running");
    }

    public void noteWifiStoppedLocked(WorkSource ws) {
        if (this.mGlobalWifiRunning) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -536870913;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mGlobalWifiRunning = false;
            this.mGlobalWifiRunningTimer.stopRunningLocked(elapsedRealtime);
            int N = ws.size();
            for (int i = 0; i < N; i++) {
                getUidStatsLocked(mapUid(ws.get(i))).noteWifiStoppedLocked(elapsedRealtime);
            }
            List<WorkChain> workChains = ws.getWorkChains();
            if (workChains != null) {
                for (int i2 = 0; i2 < workChains.size(); i2++) {
                    getUidStatsLocked(mapUid(((WorkChain) workChains.get(i2)).getAttributionUid())).noteWifiStoppedLocked(elapsedRealtime);
                }
            }
            scheduleSyncExternalStatsLocked("wifi-stopped", 2);
            return;
        }
        Log.w(TAG, "noteWifiStoppedLocked -- called while WIFI not running");
    }

    public void noteWifiStateLocked(int wifiState, String accessPoint) {
        if (this.mWifiState != wifiState) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            int i = this.mWifiState;
            if (i >= 0) {
                this.mWifiStateTimer[i].stopRunningLocked(elapsedRealtime);
            }
            this.mWifiState = wifiState;
            this.mWifiStateTimer[wifiState].startRunningLocked(elapsedRealtime);
            scheduleSyncExternalStatsLocked("wifi-state", 2);
        }
    }

    public void noteWifiSupplicantStateChangedLocked(int supplState, boolean failedAuth) {
        if (this.mWifiSupplState != supplState) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            int i = this.mWifiSupplState;
            if (i >= 0) {
                this.mWifiSupplStateTimer[i].stopRunningLocked(elapsedRealtime);
            }
            this.mWifiSupplState = supplState;
            this.mWifiSupplStateTimer[supplState].startRunningLocked(elapsedRealtime);
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 = (historyItem.states2 & -16) | (supplState << 0);
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void stopAllWifiSignalStrengthTimersLocked(int except) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        for (int i = 0; i < 5; i++) {
            if (i != except) {
                while (this.mWifiSignalStrengthsTimer[i].isRunningLocked()) {
                    this.mWifiSignalStrengthsTimer[i].stopRunningLocked(elapsedRealtime);
                }
            }
        }
    }

    public void noteWifiRssiChangedLocked(int newRssi) {
        int strengthBin = WifiManager.calculateSignalLevel(newRssi, 5);
        if (this.mWifiSignalStrengthBin != strengthBin) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            int i = this.mWifiSignalStrengthBin;
            if (i >= 0) {
                this.mWifiSignalStrengthsTimer[i].stopRunningLocked(elapsedRealtime);
            }
            if (strengthBin >= 0) {
                if (!this.mWifiSignalStrengthsTimer[strengthBin].isRunningLocked()) {
                    this.mWifiSignalStrengthsTimer[strengthBin].startRunningLocked(elapsedRealtime);
                }
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states2 = (historyItem.states2 & -113) | (strengthBin << 4);
                addHistoryRecordLocked(elapsedRealtime, uptime);
            } else {
                stopAllWifiSignalStrengthTimersLocked(-1);
            }
            this.mWifiSignalStrengthBin = strengthBin;
        }
    }

    @UnsupportedAppUsage
    public void noteFullWifiLockAcquiredLocked(int uid) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mWifiFullLockNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states |= 268435456;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mWifiFullLockNesting++;
        getUidStatsLocked(uid).noteFullWifiLockAcquiredLocked(elapsedRealtime);
    }

    @UnsupportedAppUsage
    public void noteFullWifiLockReleasedLocked(int uid) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        this.mWifiFullLockNesting--;
        if (this.mWifiFullLockNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states &= -268435457;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        getUidStatsLocked(uid).noteFullWifiLockReleasedLocked(elapsedRealtime);
    }

    public void noteWifiScanStartedLocked(int uid) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mWifiScanNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states |= 134217728;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mWifiScanNesting++;
        getUidStatsLocked(uid).noteWifiScanStartedLocked(elapsedRealtime);
    }

    public void noteWifiScanStoppedLocked(int uid) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        this.mWifiScanNesting--;
        if (this.mWifiScanNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states &= -134217729;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        getUidStatsLocked(uid).noteWifiScanStoppedLocked(elapsedRealtime);
    }

    public void noteWifiBatchedScanStartedLocked(int uid, int csph) {
        uid = mapUid(uid);
        getUidStatsLocked(uid).noteWifiBatchedScanStartedLocked(csph, this.mClocks.elapsedRealtime());
    }

    public void noteWifiBatchedScanStoppedLocked(int uid) {
        uid = mapUid(uid);
        getUidStatsLocked(uid).noteWifiBatchedScanStoppedLocked(this.mClocks.elapsedRealtime());
    }

    @UnsupportedAppUsage
    public void noteWifiMulticastEnabledLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mWifiMulticastNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states |= 65536;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            if (!this.mWifiMulticastWakelockTimer.isRunningLocked()) {
                this.mWifiMulticastWakelockTimer.startRunningLocked(elapsedRealtime);
            }
        }
        this.mWifiMulticastNesting++;
        getUidStatsLocked(uid).noteWifiMulticastEnabledLocked(elapsedRealtime);
    }

    @UnsupportedAppUsage
    public void noteWifiMulticastDisabledLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        this.mWifiMulticastNesting--;
        if (this.mWifiMulticastNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states &= -65537;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            if (this.mWifiMulticastWakelockTimer.isRunningLocked()) {
                this.mWifiMulticastWakelockTimer.stopRunningLocked(elapsedRealtime);
            }
        }
        getUidStatsLocked(uid).noteWifiMulticastDisabledLocked(elapsedRealtime);
    }

    public void noteFullWifiLockAcquiredFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteFullWifiLockAcquiredLocked(mapUid(ws.get(i)));
        }
        List<WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteFullWifiLockAcquiredLocked(mapUid(((WorkChain) workChains.get(i2)).getAttributionUid()));
            }
        }
    }

    public void noteFullWifiLockReleasedFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteFullWifiLockReleasedLocked(mapUid(ws.get(i)));
        }
        List<WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteFullWifiLockReleasedLocked(mapUid(((WorkChain) workChains.get(i2)).getAttributionUid()));
            }
        }
    }

    public void noteWifiScanStartedFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiScanStartedLocked(mapUid(ws.get(i)));
        }
        List<WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteWifiScanStartedLocked(mapUid(((WorkChain) workChains.get(i2)).getAttributionUid()));
            }
        }
    }

    public void noteWifiScanStoppedFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiScanStoppedLocked(mapUid(ws.get(i)));
        }
        List<WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteWifiScanStoppedLocked(mapUid(((WorkChain) workChains.get(i2)).getAttributionUid()));
            }
        }
    }

    public void noteWifiBatchedScanStartedFromSourceLocked(WorkSource ws, int csph) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiBatchedScanStartedLocked(ws.get(i), csph);
        }
        List<WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteWifiBatchedScanStartedLocked(((WorkChain) workChains.get(i2)).getAttributionUid(), csph);
            }
        }
    }

    public void noteWifiBatchedScanStoppedFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiBatchedScanStoppedLocked(ws.get(i));
        }
        List<WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteWifiBatchedScanStoppedLocked(((WorkChain) workChains.get(i2)).getAttributionUid());
            }
        }
    }

    private static String[] includeInStringArray(String[] array, String str) {
        if (ArrayUtils.indexOf(array, str) >= 0) {
            return array;
        }
        String[] newArray = new String[(array.length + 1)];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = str;
        return newArray;
    }

    private static String[] excludeFromStringArray(String[] array, String str) {
        int index = ArrayUtils.indexOf(array, str);
        if (index < 0) {
            return array;
        }
        String[] newArray = new String[(array.length - 1)];
        if (index > 0) {
            System.arraycopy(array, 0, newArray, 0, index);
        }
        if (index < array.length - 1) {
            System.arraycopy(array, index + 1, newArray, index, (array.length - index) - 1);
        }
        return newArray;
    }

    public void noteNetworkInterfaceTypeLocked(String iface, int networkType) {
        if (!TextUtils.isEmpty(iface)) {
            synchronized (this.mModemNetworkLock) {
                if (ConnectivityManager.isNetworkTypeMobile(networkType)) {
                    this.mModemIfaces = includeInStringArray(this.mModemIfaces, iface);
                } else {
                    this.mModemIfaces = excludeFromStringArray(this.mModemIfaces, iface);
                }
            }
            synchronized (this.mWifiNetworkLock) {
                if (ConnectivityManager.isNetworkTypeWifi(networkType)) {
                    this.mWifiIfaces = includeInStringArray(this.mWifiIfaces, iface);
                } else {
                    this.mWifiIfaces = excludeFromStringArray(this.mWifiIfaces, iface);
                }
            }
        }
    }

    public String[] getWifiIfaces() {
        String[] strArr;
        synchronized (this.mWifiNetworkLock) {
            strArr = this.mWifiIfaces;
        }
        return strArr;
    }

    public String[] getMobileIfaces() {
        String[] strArr;
        synchronized (this.mModemNetworkLock) {
            strArr = this.mModemIfaces;
        }
        return strArr;
    }

    @UnsupportedAppUsage
    public long getScreenOnTime(long elapsedRealtimeUs, int which) {
        return this.mScreenOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getScreenOnCount(int which) {
        return this.mScreenOnTimer.getCountLocked(which);
    }

    public long getScreenDozeTime(long elapsedRealtimeUs, int which) {
        return this.mScreenDozeTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getScreenDozeCount(int which) {
        return this.mScreenDozeTimer.getCountLocked(which);
    }

    @UnsupportedAppUsage
    public long getScreenBrightnessTime(int brightnessBin, long elapsedRealtimeUs, int which) {
        return this.mScreenBrightnessTimer[brightnessBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public Timer getScreenBrightnessTimer(int brightnessBin) {
        return this.mScreenBrightnessTimer[brightnessBin];
    }

    public long getInteractiveTime(long elapsedRealtimeUs, int which) {
        return this.mInteractiveTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getPowerSaveModeEnabledTime(long elapsedRealtimeUs, int which) {
        return this.mPowerSaveModeEnabledTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getPowerSaveModeEnabledCount(int which) {
        return this.mPowerSaveModeEnabledTimer.getCountLocked(which);
    }

    public long getDeviceIdleModeTime(int mode, long elapsedRealtimeUs, int which) {
        if (mode == 1) {
            return this.mDeviceIdleModeLightTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }
        if (mode != 2) {
            return 0;
        }
        return this.mDeviceIdleModeFullTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getDeviceIdleModeCount(int mode, int which) {
        if (mode == 1) {
            return this.mDeviceIdleModeLightTimer.getCountLocked(which);
        }
        if (mode != 2) {
            return 0;
        }
        return this.mDeviceIdleModeFullTimer.getCountLocked(which);
    }

    public long getLongestDeviceIdleModeTime(int mode) {
        if (mode == 1) {
            return this.mLongestLightIdleTime;
        }
        if (mode != 2) {
            return 0;
        }
        return this.mLongestFullIdleTime;
    }

    public long getDeviceIdlingTime(int mode, long elapsedRealtimeUs, int which) {
        if (mode == 1) {
            return this.mDeviceLightIdlingTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }
        if (mode != 2) {
            return 0;
        }
        return this.mDeviceIdlingTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getDeviceIdlingCount(int mode, int which) {
        if (mode == 1) {
            return this.mDeviceLightIdlingTimer.getCountLocked(which);
        }
        if (mode != 2) {
            return 0;
        }
        return this.mDeviceIdlingTimer.getCountLocked(which);
    }

    public int getNumConnectivityChange(int which) {
        return this.mNumConnectivityChange;
    }

    public long getGpsSignalQualityTime(int strengthBin, long elapsedRealtimeUs, int which) {
        if (strengthBin < 0 || strengthBin >= 2) {
            return 0;
        }
        return this.mGpsSignalQualityTimer[strengthBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getGpsBatteryDrainMaMs() {
        if (this.mPowerProfile.getAveragePower(PowerProfile.POWER_GPS_OPERATING_VOLTAGE) / 1000.0d == 0.0d) {
            return 0;
        }
        double energyUsedMaMs = 0.0d;
        long rawRealtime = SystemClock.elapsedRealtime() * 1000;
        for (int i = 0; i < 2; i++) {
            energyUsedMaMs += this.mPowerProfile.getAveragePower(PowerProfile.POWER_GPS_SIGNAL_QUALITY_BASED, i) * ((double) (getGpsSignalQualityTime(i, rawRealtime, 0) / 1000));
        }
        return (long) energyUsedMaMs;
    }

    @UnsupportedAppUsage
    public long getPhoneOnTime(long elapsedRealtimeUs, int which) {
        return this.mPhoneOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getPhoneOnCount(int which) {
        return this.mPhoneOnTimer.getCountLocked(which);
    }

    @UnsupportedAppUsage
    public long getPhoneSignalStrengthTime(int strengthBin, long elapsedRealtimeUs, int which) {
        return this.mPhoneSignalStrengthsTimer[strengthBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @UnsupportedAppUsage
    public long getPhoneSignalScanningTime(long elapsedRealtimeUs, int which) {
        return this.mPhoneSignalScanningTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public Timer getPhoneSignalScanningTimer() {
        return this.mPhoneSignalScanningTimer;
    }

    @UnsupportedAppUsage
    public int getPhoneSignalStrengthCount(int strengthBin, int which) {
        return this.mPhoneSignalStrengthsTimer[strengthBin].getCountLocked(which);
    }

    public Timer getPhoneSignalStrengthTimer(int strengthBin) {
        return this.mPhoneSignalStrengthsTimer[strengthBin];
    }

    @UnsupportedAppUsage
    public long getPhoneDataConnectionTime(int dataType, long elapsedRealtimeUs, int which) {
        return this.mPhoneDataConnectionsTimer[dataType].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @UnsupportedAppUsage
    public int getPhoneDataConnectionCount(int dataType, int which) {
        return this.mPhoneDataConnectionsTimer[dataType].getCountLocked(which);
    }

    public Timer getPhoneDataConnectionTimer(int dataType) {
        return this.mPhoneDataConnectionsTimer[dataType];
    }

    @UnsupportedAppUsage
    public long getMobileRadioActiveTime(long elapsedRealtimeUs, int which) {
        return this.mMobileRadioActiveTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getMobileRadioActiveCount(int which) {
        return this.mMobileRadioActiveTimer.getCountLocked(which);
    }

    public long getMobileRadioActiveAdjustedTime(int which) {
        return this.mMobileRadioActiveAdjustedTime.getCountLocked(which);
    }

    public long getMobileRadioActiveUnknownTime(int which) {
        return this.mMobileRadioActiveUnknownTime.getCountLocked(which);
    }

    public int getMobileRadioActiveUnknownCount(int which) {
        return (int) this.mMobileRadioActiveUnknownCount.getCountLocked(which);
    }

    public long getWifiMulticastWakelockTime(long elapsedRealtimeUs, int which) {
        return this.mWifiMulticastWakelockTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getWifiMulticastWakelockCount(int which) {
        return this.mWifiMulticastWakelockTimer.getCountLocked(which);
    }

    @UnsupportedAppUsage
    public long getWifiOnTime(long elapsedRealtimeUs, int which) {
        return this.mWifiOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getWifiActiveTime(long elapsedRealtimeUs, int which) {
        return this.mWifiActiveTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @UnsupportedAppUsage
    public long getGlobalWifiRunningTime(long elapsedRealtimeUs, int which) {
        return this.mGlobalWifiRunningTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getWifiStateTime(int wifiState, long elapsedRealtimeUs, int which) {
        return this.mWifiStateTimer[wifiState].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getWifiStateCount(int wifiState, int which) {
        return this.mWifiStateTimer[wifiState].getCountLocked(which);
    }

    public Timer getWifiStateTimer(int wifiState) {
        return this.mWifiStateTimer[wifiState];
    }

    public long getWifiSupplStateTime(int state, long elapsedRealtimeUs, int which) {
        return this.mWifiSupplStateTimer[state].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getWifiSupplStateCount(int state, int which) {
        return this.mWifiSupplStateTimer[state].getCountLocked(which);
    }

    public Timer getWifiSupplStateTimer(int state) {
        return this.mWifiSupplStateTimer[state];
    }

    public long getWifiSignalStrengthTime(int strengthBin, long elapsedRealtimeUs, int which) {
        return this.mWifiSignalStrengthsTimer[strengthBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getWifiSignalStrengthCount(int strengthBin, int which) {
        return this.mWifiSignalStrengthsTimer[strengthBin].getCountLocked(which);
    }

    public Timer getWifiSignalStrengthTimer(int strengthBin) {
        return this.mWifiSignalStrengthsTimer[strengthBin];
    }

    public ControllerActivityCounter getBluetoothControllerActivity() {
        return this.mBluetoothActivity;
    }

    public ControllerActivityCounter getWifiControllerActivity() {
        return this.mWifiActivity;
    }

    public ControllerActivityCounter getModemControllerActivity() {
        return this.mModemActivity;
    }

    public boolean hasBluetoothActivityReporting() {
        return this.mHasBluetoothReporting;
    }

    public boolean hasWifiActivityReporting() {
        return this.mHasWifiReporting;
    }

    public boolean hasModemActivityReporting() {
        return this.mHasModemReporting;
    }

    public long getFlashlightOnTime(long elapsedRealtimeUs, int which) {
        return this.mFlashlightOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getFlashlightOnCount(int which) {
        return (long) this.mFlashlightOnTimer.getCountLocked(which);
    }

    public long getCameraOnTime(long elapsedRealtimeUs, int which) {
        return this.mCameraOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getBluetoothScanTime(long elapsedRealtimeUs, int which) {
        return this.mBluetoothScanTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @UnsupportedAppUsage
    public long getNetworkActivityBytes(int type, int which) {
        if (type >= 0) {
            LongSamplingCounter[] longSamplingCounterArr = this.mNetworkByteActivityCounters;
            if (type < longSamplingCounterArr.length) {
                return longSamplingCounterArr[type].getCountLocked(which);
            }
        }
        return 0;
    }

    public long getNetworkActivityPackets(int type, int which) {
        if (type >= 0) {
            LongSamplingCounter[] longSamplingCounterArr = this.mNetworkPacketActivityCounters;
            if (type < longSamplingCounterArr.length) {
                return longSamplingCounterArr[type].getCountLocked(which);
            }
        }
        return 0;
    }

    public long getStartClockTime() {
        long currentTime = System.currentTimeMillis();
        if (currentTime <= MILLISECONDS_IN_YEAR || this.mStartClockTime >= currentTime - MILLISECONDS_IN_YEAR) {
            long j = this.mStartClockTime;
            if (j <= currentTime) {
                return j;
            }
        }
        recordCurrentTimeChangeLocked(currentTime, this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis());
        return currentTime - (this.mClocks.elapsedRealtime() - (this.mRealtimeStart / 1000));
    }

    public String getStartPlatformVersion() {
        return this.mStartPlatformVersion;
    }

    public String getEndPlatformVersion() {
        return this.mEndPlatformVersion;
    }

    public int getParcelVersion() {
        return 186;
    }

    public boolean getIsOnBattery() {
        return this.mOnBattery;
    }

    @UnsupportedAppUsage
    public SparseArray<? extends android.os.BatteryStats.Uid> getUidStats() {
        return this.mUidStats;
    }

    private static <T extends TimeBaseObs> boolean resetIfNotNull(T t, boolean detachIfReset) {
        if (t != null) {
            return t.reset(detachIfReset);
        }
        return true;
    }

    private static <T extends TimeBaseObs> boolean resetIfNotNull(T[] t, boolean detachIfReset) {
        if (t == null) {
            return true;
        }
        boolean ret = true;
        for (TimeBaseObs resetIfNotNull : t) {
            ret &= resetIfNotNull(resetIfNotNull, detachIfReset);
        }
        return ret;
    }

    private static <T extends TimeBaseObs> boolean resetIfNotNull(T[][] t, boolean detachIfReset) {
        if (t == null) {
            return true;
        }
        boolean ret = true;
        for (TimeBaseObs[] resetIfNotNull : t) {
            ret &= resetIfNotNull(resetIfNotNull, detachIfReset);
        }
        return ret;
    }

    private static boolean resetIfNotNull(ControllerActivityCounterImpl counter, boolean detachIfReset) {
        if (counter != null) {
            counter.reset(detachIfReset);
        }
        return true;
    }

    private static <T extends TimeBaseObs> void detachIfNotNull(T t) {
        if (t != null) {
            t.detach();
        }
    }

    private static <T extends TimeBaseObs> void detachIfNotNull(T[] t) {
        if (t != null) {
            for (TimeBaseObs detachIfNotNull : t) {
                detachIfNotNull(detachIfNotNull);
            }
        }
    }

    private static <T extends TimeBaseObs> void detachIfNotNull(T[][] t) {
        if (t != null) {
            for (TimeBaseObs[] detachIfNotNull : t) {
                detachIfNotNull(detachIfNotNull);
            }
        }
    }

    private static void detachIfNotNull(ControllerActivityCounterImpl counter) {
        if (counter != null) {
            counter.detach();
        }
    }

    public long[] getCpuFreqs() {
        return this.mCpuFreqs;
    }

    public BatteryStatsImpl(File systemDir, Handler handler, PlatformIdleStateCallback cb, RailEnergyDataCallback railStatsCb, UserInfoProvider userInfoProvider) {
        this(new SystemClocks(), systemDir, handler, cb, railStatsCb, userInfoProvider);
    }

    private BatteryStatsImpl(Clocks clocks, File systemDir, Handler handler, PlatformIdleStateCallback cb, RailEnergyDataCallback railStatsCb, UserInfoProvider userInfoProvider) {
        int i;
        File file = systemDir;
        this.mKernelWakelockReader = new KernelWakelockReader();
        this.mTmpWakelockStats = new KernelWakelockStats();
        this.mCpuUidUserSysTimeReader = new KernelCpuUidUserSysTimeReader(true);
        this.mCpuUidFreqTimeReader = new KernelCpuUidFreqTimeReader(true);
        this.mCpuUidActiveTimeReader = new KernelCpuUidActiveTimeReader(true);
        this.mCpuUidClusterTimeReader = new KernelCpuUidClusterTimeReader(true);
        this.mKernelMemoryBandwidthStats = new KernelMemoryBandwidthStats();
        this.mKernelMemoryStats = new LongSparseArray();
        this.mPerProcStateCpuTimesAvailable = true;
        this.mPendingUids = new SparseIntArray();
        this.mCpuTimeReadsTrackingStartTime = SystemClock.uptimeMillis();
        this.mTmpRpmStats = new RpmStats();
        this.mLastRpmStatsUpdateTimeMs = -1000;
        this.mTmpRailStats = new RailStats();
        this.mPendingRemovedUids = new LinkedList();
        this.mDeferSetCharging = /* anonymous class already generated */;
        this.mExternalSync = null;
        this.mUserInfoProvider = null;
        this.mIsolatedUids = new SparseIntArray();
        this.mUidStats = new SparseArray();
        this.mPartialTimers = new ArrayList();
        this.mFullTimers = new ArrayList();
        this.mWindowTimers = new ArrayList();
        this.mDrawTimers = new ArrayList();
        this.mSensorTimers = new SparseArray();
        this.mWifiRunningTimers = new ArrayList();
        this.mFullWifiLockTimers = new ArrayList();
        this.mWifiMulticastTimers = new ArrayList();
        this.mWifiScanTimers = new ArrayList();
        this.mWifiBatchedScanTimers = new SparseArray();
        this.mAudioTurnedOnTimers = new ArrayList();
        this.mVideoTurnedOnTimers = new ArrayList();
        this.mFlashlightTurnedOnTimers = new ArrayList();
        this.mCameraTurnedOnTimers = new ArrayList();
        this.mBluetoothScanOnTimers = new ArrayList();
        this.mLastPartialTimers = new ArrayList();
        this.mOnBatteryTimeBase = new TimeBase(true);
        this.mOnBatteryScreenOffTimeBase = new TimeBase(true);
        this.mActiveEvents = new HistoryEventTracker();
        this.mHaveBatteryLevel = false;
        this.mRecordingHistory = false;
        this.mHistoryBuffer = Parcel.obtain();
        this.mHistoryLastWritten = new HistoryItem();
        this.mHistoryLastLastWritten = new HistoryItem();
        this.mHistoryReadTmp = new HistoryItem();
        this.mHistoryAddTmp = new HistoryItem();
        this.mHistoryTagPool = new HashMap();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        this.mHistoryBufferLastPos = -1;
        this.mActiveHistoryStates = -1;
        this.mActiveHistoryStates2 = -1;
        this.mLastHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryUptime = 0;
        this.mHistoryCur = new HistoryItem();
        this.mLastHistoryStepDetails = null;
        this.mLastHistoryStepLevel = (byte) 0;
        this.mCurHistoryStepDetails = new HistoryStepDetails();
        this.mReadHistoryStepDetails = new HistoryStepDetails();
        this.mTmpHistoryStepDetails = new HistoryStepDetails();
        this.mScreenState = 0;
        this.mScreenBrightnessBin = -1;
        this.mScreenBrightnessTimer = new StopwatchTimer[5];
        this.mUsbDataState = 0;
        this.mGpsSignalQualityBin = -1;
        this.mGpsSignalQualityTimer = new StopwatchTimer[2];
        this.mPhoneSignalStrengthBin = -1;
        this.mPhoneSignalStrengthBinRaw = -1;
        this.mPhoneSignalStrengthsTimer = new StopwatchTimer[5];
        this.mPhoneDataConnectionType = -1;
        this.mPhoneDataConnectionsTimer = new StopwatchTimer[22];
        this.mNetworkByteActivityCounters = new LongSamplingCounter[10];
        this.mNetworkPacketActivityCounters = new LongSamplingCounter[10];
        this.mHasWifiReporting = false;
        this.mHasBluetoothReporting = false;
        this.mHasModemReporting = false;
        this.mWifiState = -1;
        this.mWifiStateTimer = new StopwatchTimer[8];
        this.mWifiSupplState = -1;
        this.mWifiSupplStateTimer = new StopwatchTimer[13];
        this.mWifiSignalStrengthBin = -1;
        this.mWifiSignalStrengthsTimer = new StopwatchTimer[5];
        this.mIsCellularTxPowerHigh = false;
        this.mMobileRadioPowerState = 1;
        this.mWifiRadioPowerState = 1;
        this.mCharging = true;
        this.mInitStepMode = 0;
        this.mCurStepMode = 0;
        this.mModStepMode = 0;
        this.mDischargeStepTracker = new LevelStepTracker(200);
        this.mDailyDischargeStepTracker = new LevelStepTracker(400);
        this.mChargeStepTracker = new LevelStepTracker(200);
        this.mDailyChargeStepTracker = new LevelStepTracker(400);
        this.mDailyStartTime = 0;
        this.mNextMinDailyDeadline = 0;
        this.mNextMaxDailyDeadline = 0;
        this.mDailyItems = new ArrayList();
        this.mLastWriteTime = 0;
        this.mPhoneServiceState = -1;
        this.mPhoneServiceStateRaw = -1;
        this.mPhoneSimStateRaw = -1;
        this.mEstimatedBatteryCapacity = -1;
        this.mMinLearnedBatteryCapacity = -1;
        this.mMaxLearnedBatteryCapacity = -1;
        this.mRpmStats = new HashMap();
        this.mScreenOffRpmStats = new HashMap();
        this.mKernelWakelockStats = new HashMap();
        this.mLastWakeupReason = null;
        this.mLastWakeupUptimeMs = 0;
        this.mWakeupReasonStats = new HashMap();
        this.mChangedStates = 0;
        this.mChangedStates2 = 0;
        this.mInitialAcquireWakeUid = -1;
        this.mWifiFullLockNesting = 0;
        this.mWifiScanNesting = 0;
        this.mWifiMulticastNesting = 0;
        this.mNetworkStatsPool = new SynchronizedPool(6);
        this.mWifiNetworkLock = new Object();
        this.mWifiIfaces = EmptyArray.STRING;
        this.mLastWifiNetworkStats = new NetworkStats(0, -1);
        this.mModemNetworkLock = new Object();
        this.mModemIfaces = EmptyArray.STRING;
        this.mLastModemNetworkStats = new NetworkStats(0, -1);
        this.mLastModemActivityInfo = new ModemActivityInfo(0, 0, 0, new int[0], 0, 0);
        this.mLastBluetoothActivityInfo = new BluetoothActivityInfoCache(this, null);
        this.mWriteLock = new ReentrantLock();
        init(clocks);
        if (file == null) {
            this.mStatsFile = null;
            this.mBatteryStatsHistory = new BatteryStatsHistory(this, this.mHistoryBuffer);
        } else {
            this.mStatsFile = new AtomicFile(new File(file, "batterystats.bin"));
            this.mBatteryStatsHistory = new BatteryStatsHistory(this, file, this.mHistoryBuffer);
        }
        this.mCheckinFile = new AtomicFile(new File(file, "batterystats-checkin.bin"));
        this.mDailyFile = new AtomicFile(new File(file, "batterystats-daily.xml"));
        this.mHandler = new MyHandler(handler.getLooper());
        this.mConstants = new Constants(this.mHandler);
        this.mStartCount++;
        this.mScreenOnTimer = new StopwatchTimer(this.mClocks, null, -1, null, this.mOnBatteryTimeBase);
        this.mScreenDozeTimer = new StopwatchTimer(this.mClocks, null, -1, null, this.mOnBatteryTimeBase);
        for (i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i] = new StopwatchTimer(this.mClocks, null, -100 - i, null, this.mOnBatteryTimeBase);
        }
        this.mInteractiveTimer = new StopwatchTimer(this.mClocks, null, -10, null, this.mOnBatteryTimeBase);
        this.mPowerSaveModeEnabledTimer = new StopwatchTimer(this.mClocks, null, -2, null, this.mOnBatteryTimeBase);
        this.mDeviceIdleModeLightTimer = new StopwatchTimer(this.mClocks, null, -11, null, this.mOnBatteryTimeBase);
        this.mDeviceIdleModeFullTimer = new StopwatchTimer(this.mClocks, null, -14, null, this.mOnBatteryTimeBase);
        this.mDeviceLightIdlingTimer = new StopwatchTimer(this.mClocks, null, -15, null, this.mOnBatteryTimeBase);
        this.mDeviceIdlingTimer = new StopwatchTimer(this.mClocks, null, -12, null, this.mOnBatteryTimeBase);
        this.mPhoneOnTimer = new StopwatchTimer(this.mClocks, null, -3, null, this.mOnBatteryTimeBase);
        for (i = 0; i < 5; i++) {
            this.mPhoneSignalStrengthsTimer[i] = new StopwatchTimer(this.mClocks, null, -200 - i, null, this.mOnBatteryTimeBase);
        }
        this.mPhoneSignalScanningTimer = new StopwatchTimer(this.mClocks, null, -199, null, this.mOnBatteryTimeBase);
        for (i = 0; i < 22; i++) {
            this.mPhoneDataConnectionsTimer[i] = new StopwatchTimer(this.mClocks, null, -300 - i, null, this.mOnBatteryTimeBase);
        }
        for (i = 0; i < 10; i++) {
            this.mNetworkByteActivityCounters[i] = new LongSamplingCounter(this.mOnBatteryTimeBase);
            this.mNetworkPacketActivityCounters[i] = new LongSamplingCounter(this.mOnBatteryTimeBase);
        }
        this.mWifiActivity = new ControllerActivityCounterImpl(this.mOnBatteryTimeBase, 1);
        this.mBluetoothActivity = new ControllerActivityCounterImpl(this.mOnBatteryTimeBase, 1);
        this.mModemActivity = new ControllerActivityCounterImpl(this.mOnBatteryTimeBase, 5);
        this.mMobileRadioActiveTimer = new StopwatchTimer(this.mClocks, null, -400, null, this.mOnBatteryTimeBase);
        this.mMobileRadioActivePerAppTimer = new StopwatchTimer(this.mClocks, null, -401, null, this.mOnBatteryTimeBase);
        this.mMobileRadioActiveAdjustedTime = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mMobileRadioActiveUnknownTime = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mMobileRadioActiveUnknownCount = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mWifiMulticastWakelockTimer = new StopwatchTimer(this.mClocks, null, 23, null, this.mOnBatteryTimeBase);
        this.mWifiOnTimer = new StopwatchTimer(this.mClocks, null, -4, null, this.mOnBatteryTimeBase);
        this.mGlobalWifiRunningTimer = new StopwatchTimer(this.mClocks, null, -5, null, this.mOnBatteryTimeBase);
        for (i = 0; i < 8; i++) {
            this.mWifiStateTimer[i] = new StopwatchTimer(this.mClocks, null, -600 - i, null, this.mOnBatteryTimeBase);
        }
        for (i = 0; i < 13; i++) {
            this.mWifiSupplStateTimer[i] = new StopwatchTimer(this.mClocks, null, -700 - i, null, this.mOnBatteryTimeBase);
        }
        for (i = 0; i < 5; i++) {
            this.mWifiSignalStrengthsTimer[i] = new StopwatchTimer(this.mClocks, null, -800 - i, null, this.mOnBatteryTimeBase);
        }
        this.mWifiActiveTimer = new StopwatchTimer(this.mClocks, null, ProcessList.SYSTEM_ADJ, null, this.mOnBatteryTimeBase);
        for (i = 0; i < 2; i++) {
            this.mGpsSignalQualityTimer[i] = new StopwatchTimer(this.mClocks, null, -1000 - i, null, this.mOnBatteryTimeBase);
        }
        this.mAudioOnTimer = new StopwatchTimer(this.mClocks, null, -7, null, this.mOnBatteryTimeBase);
        this.mVideoOnTimer = new StopwatchTimer(this.mClocks, null, -8, null, this.mOnBatteryTimeBase);
        this.mFlashlightOnTimer = new StopwatchTimer(this.mClocks, null, -9, null, this.mOnBatteryTimeBase);
        this.mCameraOnTimer = new StopwatchTimer(this.mClocks, null, -13, null, this.mOnBatteryTimeBase);
        this.mBluetoothScanTimer = new StopwatchTimer(this.mClocks, null, -14, null, this.mOnBatteryTimeBase);
        this.mDischargeScreenOffCounter = new LongSamplingCounter(this.mOnBatteryScreenOffTimeBase);
        this.mDischargeScreenDozeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mDischargeLightDozeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mDischargeDeepDozeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mDischargeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mOnBatteryInternal = false;
        this.mOnBattery = false;
        initTimes(this.mClocks.uptimeMillis() * 1000, this.mClocks.elapsedRealtime() * 1000);
        String str = Build.ID;
        this.mEndPlatformVersion = str;
        this.mStartPlatformVersion = str;
        this.mDischargeStartLevel = 0;
        this.mDischargeUnplugLevel = 0;
        this.mDischargePlugLevel = -1;
        this.mDischargeCurrentLevel = 0;
        this.mCurrentBatteryLevel = 0;
        initDischarge();
        clearHistoryLocked();
        updateDailyDeadlineLocked();
        this.mPlatformIdleStateCallback = cb;
        this.mRailEnergyDataCallback = railStatsCb;
        this.mUserInfoProvider = userInfoProvider;
    }

    @UnsupportedAppUsage
    public BatteryStatsImpl(Parcel p) {
        this(new SystemClocks(), p);
    }

    public BatteryStatsImpl(Clocks clocks, Parcel p) {
        this.mKernelWakelockReader = new KernelWakelockReader();
        this.mTmpWakelockStats = new KernelWakelockStats();
        this.mCpuUidUserSysTimeReader = new KernelCpuUidUserSysTimeReader(true);
        this.mCpuUidFreqTimeReader = new KernelCpuUidFreqTimeReader(true);
        this.mCpuUidActiveTimeReader = new KernelCpuUidActiveTimeReader(true);
        this.mCpuUidClusterTimeReader = new KernelCpuUidClusterTimeReader(true);
        this.mKernelMemoryBandwidthStats = new KernelMemoryBandwidthStats();
        this.mKernelMemoryStats = new LongSparseArray();
        this.mPerProcStateCpuTimesAvailable = true;
        this.mPendingUids = new SparseIntArray();
        this.mCpuTimeReadsTrackingStartTime = SystemClock.uptimeMillis();
        this.mTmpRpmStats = new RpmStats();
        this.mLastRpmStatsUpdateTimeMs = -1000;
        this.mTmpRailStats = new RailStats();
        this.mPendingRemovedUids = new LinkedList();
        this.mDeferSetCharging = /* anonymous class already generated */;
        this.mExternalSync = null;
        this.mUserInfoProvider = null;
        this.mIsolatedUids = new SparseIntArray();
        this.mUidStats = new SparseArray();
        this.mPartialTimers = new ArrayList();
        this.mFullTimers = new ArrayList();
        this.mWindowTimers = new ArrayList();
        this.mDrawTimers = new ArrayList();
        this.mSensorTimers = new SparseArray();
        this.mWifiRunningTimers = new ArrayList();
        this.mFullWifiLockTimers = new ArrayList();
        this.mWifiMulticastTimers = new ArrayList();
        this.mWifiScanTimers = new ArrayList();
        this.mWifiBatchedScanTimers = new SparseArray();
        this.mAudioTurnedOnTimers = new ArrayList();
        this.mVideoTurnedOnTimers = new ArrayList();
        this.mFlashlightTurnedOnTimers = new ArrayList();
        this.mCameraTurnedOnTimers = new ArrayList();
        this.mBluetoothScanOnTimers = new ArrayList();
        this.mLastPartialTimers = new ArrayList();
        this.mOnBatteryTimeBase = new TimeBase(true);
        this.mOnBatteryScreenOffTimeBase = new TimeBase(true);
        this.mActiveEvents = new HistoryEventTracker();
        this.mHaveBatteryLevel = false;
        this.mRecordingHistory = false;
        this.mHistoryBuffer = Parcel.obtain();
        this.mHistoryLastWritten = new HistoryItem();
        this.mHistoryLastLastWritten = new HistoryItem();
        this.mHistoryReadTmp = new HistoryItem();
        this.mHistoryAddTmp = new HistoryItem();
        this.mHistoryTagPool = new HashMap();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        this.mHistoryBufferLastPos = -1;
        this.mActiveHistoryStates = -1;
        this.mActiveHistoryStates2 = -1;
        this.mLastHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryUptime = 0;
        this.mHistoryCur = new HistoryItem();
        this.mLastHistoryStepDetails = null;
        this.mLastHistoryStepLevel = (byte) 0;
        this.mCurHistoryStepDetails = new HistoryStepDetails();
        this.mReadHistoryStepDetails = new HistoryStepDetails();
        this.mTmpHistoryStepDetails = new HistoryStepDetails();
        this.mScreenState = 0;
        this.mScreenBrightnessBin = -1;
        this.mScreenBrightnessTimer = new StopwatchTimer[5];
        this.mUsbDataState = 0;
        this.mGpsSignalQualityBin = -1;
        this.mGpsSignalQualityTimer = new StopwatchTimer[2];
        this.mPhoneSignalStrengthBin = -1;
        this.mPhoneSignalStrengthBinRaw = -1;
        this.mPhoneSignalStrengthsTimer = new StopwatchTimer[5];
        this.mPhoneDataConnectionType = -1;
        this.mPhoneDataConnectionsTimer = new StopwatchTimer[22];
        this.mNetworkByteActivityCounters = new LongSamplingCounter[10];
        this.mNetworkPacketActivityCounters = new LongSamplingCounter[10];
        this.mHasWifiReporting = false;
        this.mHasBluetoothReporting = false;
        this.mHasModemReporting = false;
        this.mWifiState = -1;
        this.mWifiStateTimer = new StopwatchTimer[8];
        this.mWifiSupplState = -1;
        this.mWifiSupplStateTimer = new StopwatchTimer[13];
        this.mWifiSignalStrengthBin = -1;
        this.mWifiSignalStrengthsTimer = new StopwatchTimer[5];
        this.mIsCellularTxPowerHigh = false;
        this.mMobileRadioPowerState = 1;
        this.mWifiRadioPowerState = 1;
        this.mCharging = true;
        this.mInitStepMode = 0;
        this.mCurStepMode = 0;
        this.mModStepMode = 0;
        this.mDischargeStepTracker = new LevelStepTracker(200);
        this.mDailyDischargeStepTracker = new LevelStepTracker(400);
        this.mChargeStepTracker = new LevelStepTracker(200);
        this.mDailyChargeStepTracker = new LevelStepTracker(400);
        this.mDailyStartTime = 0;
        this.mNextMinDailyDeadline = 0;
        this.mNextMaxDailyDeadline = 0;
        this.mDailyItems = new ArrayList();
        this.mLastWriteTime = 0;
        this.mPhoneServiceState = -1;
        this.mPhoneServiceStateRaw = -1;
        this.mPhoneSimStateRaw = -1;
        this.mEstimatedBatteryCapacity = -1;
        this.mMinLearnedBatteryCapacity = -1;
        this.mMaxLearnedBatteryCapacity = -1;
        this.mRpmStats = new HashMap();
        this.mScreenOffRpmStats = new HashMap();
        this.mKernelWakelockStats = new HashMap();
        this.mLastWakeupReason = null;
        this.mLastWakeupUptimeMs = 0;
        this.mWakeupReasonStats = new HashMap();
        this.mChangedStates = 0;
        this.mChangedStates2 = 0;
        this.mInitialAcquireWakeUid = -1;
        this.mWifiFullLockNesting = 0;
        this.mWifiScanNesting = 0;
        this.mWifiMulticastNesting = 0;
        this.mNetworkStatsPool = new SynchronizedPool(6);
        this.mWifiNetworkLock = new Object();
        this.mWifiIfaces = EmptyArray.STRING;
        this.mLastWifiNetworkStats = new NetworkStats(0, -1);
        this.mModemNetworkLock = new Object();
        this.mModemIfaces = EmptyArray.STRING;
        this.mLastModemNetworkStats = new NetworkStats(0, -1);
        this.mLastModemActivityInfo = new ModemActivityInfo(0, 0, 0, new int[0], 0, 0);
        this.mLastBluetoothActivityInfo = new BluetoothActivityInfoCache(this, null);
        this.mWriteLock = new ReentrantLock();
        init(clocks);
        this.mStatsFile = null;
        this.mCheckinFile = null;
        this.mDailyFile = null;
        this.mHandler = null;
        this.mExternalSync = null;
        this.mConstants = new Constants(this.mHandler);
        clearHistoryLocked();
        this.mBatteryStatsHistory = new BatteryStatsHistory(this, this.mHistoryBuffer);
        readFromParcel(p);
        this.mPlatformIdleStateCallback = null;
        this.mRailEnergyDataCallback = null;
    }

    public void setPowerProfileLocked(PowerProfile profile) {
        this.mPowerProfile = profile;
        int numClusters = this.mPowerProfile.getNumCpuClusters();
        this.mKernelCpuSpeedReaders = new KernelCpuSpeedReader[numClusters];
        int firstCpuOfCluster = 0;
        for (int i = 0; i < numClusters; i++) {
            this.mKernelCpuSpeedReaders[i] = new KernelCpuSpeedReader(firstCpuOfCluster, this.mPowerProfile.getNumSpeedStepsInCpuCluster(i));
            firstCpuOfCluster += this.mPowerProfile.getNumCoresInCpuCluster(i);
        }
        if (this.mEstimatedBatteryCapacity == -1) {
            this.mEstimatedBatteryCapacity = (int) this.mPowerProfile.getBatteryCapacity();
        }
    }

    public void setCallback(BatteryCallback cb) {
        this.mCallback = cb;
    }

    public void setRadioScanningTimeoutLocked(long timeout) {
        StopwatchTimer stopwatchTimer = this.mPhoneSignalScanningTimer;
        if (stopwatchTimer != null) {
            stopwatchTimer.setTimeout(timeout);
        }
    }

    public void setExternalStatsSyncLocked(ExternalStatsSync sync) {
        this.mExternalSync = sync;
    }

    public void updateDailyDeadlineLocked() {
        long currentTime = System.currentTimeMillis();
        this.mDailyStartTime = currentTime;
        Calendar calDeadline = Calendar.getInstance();
        calDeadline.setTimeInMillis(currentTime);
        calDeadline.set(6, calDeadline.get(6) + 1);
        calDeadline.set(14, 0);
        calDeadline.set(13, 0);
        calDeadline.set(12, 0);
        calDeadline.set(11, 1);
        this.mNextMinDailyDeadline = calDeadline.getTimeInMillis();
        calDeadline.set(11, 3);
        this.mNextMaxDailyDeadline = calDeadline.getTimeInMillis();
    }

    public void recordDailyStatsIfNeededLocked(boolean settled) {
        long currentTime = System.currentTimeMillis();
        if (currentTime >= this.mNextMaxDailyDeadline) {
            recordDailyStatsLocked();
        } else if (settled && currentTime >= this.mNextMinDailyDeadline) {
            recordDailyStatsLocked();
        } else if (currentTime < this.mDailyStartTime - 86400000) {
            recordDailyStatsLocked();
        }
    }

    public void recordDailyStatsLocked() {
        DailyItem item = new DailyItem();
        item.mStartTime = this.mDailyStartTime;
        item.mEndTime = System.currentTimeMillis();
        boolean hasData = false;
        if (this.mDailyDischargeStepTracker.mNumStepDurations > 0) {
            hasData = true;
            item.mDischargeSteps = new LevelStepTracker(this.mDailyDischargeStepTracker.mNumStepDurations, this.mDailyDischargeStepTracker.mStepDurations);
        }
        if (this.mDailyChargeStepTracker.mNumStepDurations > 0) {
            hasData = true;
            item.mChargeSteps = new LevelStepTracker(this.mDailyChargeStepTracker.mNumStepDurations, this.mDailyChargeStepTracker.mStepDurations);
        }
        ArrayList arrayList = this.mDailyPackageChanges;
        if (arrayList != null) {
            hasData = true;
            item.mPackageChanges = arrayList;
            this.mDailyPackageChanges = null;
        }
        this.mDailyDischargeStepTracker.init();
        this.mDailyChargeStepTracker.init();
        updateDailyDeadlineLocked();
        if (hasData) {
            long startTime = SystemClock.uptimeMillis();
            this.mDailyItems.add(item);
            while (this.mDailyItems.size() > 10) {
                this.mDailyItems.remove(0);
            }
            final ByteArrayOutputStream memStream = new ByteArrayOutputStream();
            try {
                XmlSerializer out = new FastXmlSerializer();
                out.setOutput(memStream, StandardCharsets.UTF_8.name());
                writeDailyItemsLocked(out);
                final long initialTime = SystemClock.uptimeMillis() - startTime;
                BackgroundThread.getHandler().post(new Runnable() {
                    public void run() {
                        synchronized (BatteryStatsImpl.this.mCheckinFile) {
                            long startTime2 = SystemClock.uptimeMillis();
                            FileOutputStream stream = null;
                            try {
                                stream = BatteryStatsImpl.this.mDailyFile.startWrite();
                                memStream.writeTo(stream);
                                stream.flush();
                                BatteryStatsImpl.this.mDailyFile.finishWrite(stream);
                                EventLogTags.writeCommitSysConfigFile("batterystats-daily", (initialTime + SystemClock.uptimeMillis()) - startTime2);
                            } catch (IOException e) {
                                Slog.w("BatteryStats", "Error writing battery daily items", e);
                                BatteryStatsImpl.this.mDailyFile.failWrite(stream);
                            }
                        }
                    }
                });
            } catch (IOException e) {
            }
        }
    }

    private void writeDailyItemsLocked(XmlSerializer out) throws IOException {
        StringBuilder sb = new StringBuilder(64);
        out.startDocument(null, Boolean.valueOf(true));
        String str = "daily-items";
        out.startTag(null, str);
        for (int i = 0; i < this.mDailyItems.size(); i++) {
            DailyItem dit = (DailyItem) this.mDailyItems.get(i);
            String str2 = ImsConfig.EXTRA_CHANGED_ITEM;
            out.startTag(null, str2);
            out.attribute(null, BaseMmsColumns.START, Long.toString(dit.mStartTime));
            out.attribute(null, "end", Long.toString(dit.mEndTime));
            writeDailyLevelSteps(out, "dis", dit.mDischargeSteps, sb);
            writeDailyLevelSteps(out, "chg", dit.mChargeSteps, sb);
            if (dit.mPackageChanges != null) {
                for (int j = 0; j < dit.mPackageChanges.size(); j++) {
                    PackageChange pc = (PackageChange) dit.mPackageChanges.get(j);
                    String str3 = "pkg";
                    String str4;
                    if (pc.mUpdate) {
                        str4 = "upd";
                        out.startTag(null, str4);
                        out.attribute(null, str3, pc.mPackageName);
                        out.attribute(null, "ver", Long.toString(pc.mVersionCode));
                        out.endTag(null, str4);
                    } else {
                        str4 = "rem";
                        out.startTag(null, str4);
                        out.attribute(null, str3, pc.mPackageName);
                        out.endTag(null, str4);
                    }
                }
            }
            out.endTag(null, str2);
        }
        out.endTag(null, str);
        out.endDocument();
    }

    private void writeDailyLevelSteps(XmlSerializer out, String tag, LevelStepTracker steps, StringBuilder tmpBuilder) throws IOException {
        if (steps != null) {
            out.startTag(null, tag);
            out.attribute(null, "n", Integer.toString(steps.mNumStepDurations));
            for (int i = 0; i < steps.mNumStepDurations; i++) {
                String str = RemoteDevice.KEY_STATUS;
                out.startTag(null, str);
                tmpBuilder.setLength(0);
                steps.encodeEntryAt(i, tmpBuilder);
                out.attribute(null, BaseMmsColumns.MMS_VERSION, tmpBuilder.toString());
                out.endTag(null, str);
            }
            out.endTag(null, tag);
        }
    }

    public void readDailyStatsLocked() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Reading daily items from ");
        stringBuilder.append(this.mDailyFile.getBaseFile());
        Slog.d(TAG, stringBuilder.toString());
        this.mDailyItems.clear();
        try {
            FileInputStream stream = this.mDailyFile.openRead();
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, StandardCharsets.UTF_8.name());
                readDailyItemsLocked(parser);
                try {
                    stream.close();
                } catch (IOException e) {
                }
            } catch (XmlPullParserException e2) {
                stream.close();
            } catch (Throwable th) {
                try {
                    stream.close();
                } catch (IOException e3) {
                }
                throw th;
            }
        } catch (FileNotFoundException e4) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0058 A:{Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }} */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0012 A:{Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }} */
    private void readDailyItemsLocked(org.xmlpull.v1.XmlPullParser r9) {
        /*
        r8 = this;
        r0 = "Failed parsing daily ";
        r1 = "BatteryStatsImpl";
    L_0x0004:
        r2 = r9.next();	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        r3 = r2;
        r4 = 1;
        r5 = 2;
        if (r2 == r5) goto L_0x0010;
    L_0x000d:
        if (r3 == r4) goto L_0x0010;
    L_0x000f:
        goto L_0x0004;
    L_0x0010:
        if (r3 != r5) goto L_0x0058;
    L_0x0012:
        r2 = r9.getDepth();	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
    L_0x0016:
        r5 = r9.next();	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        r3 = r5;
        if (r5 == r4) goto L_0x00d8;
    L_0x001d:
        r5 = 3;
        if (r3 != r5) goto L_0x0026;
    L_0x0020:
        r6 = r9.getDepth();	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        if (r6 <= r2) goto L_0x00d8;
    L_0x0026:
        if (r3 == r5) goto L_0x0016;
    L_0x0028:
        r5 = 4;
        if (r3 != r5) goto L_0x002c;
    L_0x002b:
        goto L_0x0016;
    L_0x002c:
        r5 = r9.getName();	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        r6 = "item";
        r6 = r5.equals(r6);	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        if (r6 == 0) goto L_0x003c;
    L_0x0038:
        r8.readDailyItemTagLocked(r9);	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        goto L_0x0057;
    L_0x003c:
        r6 = new java.lang.StringBuilder;	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        r6.<init>();	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        r7 = "Unknown element under <daily-items>: ";
        r6.append(r7);	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        r7 = r9.getName();	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        r6.append(r7);	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        r6 = r6.toString();	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        android.util.Slog.w(r1, r6);	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r9);	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
    L_0x0057:
        goto L_0x0016;
    L_0x0058:
        r2 = new java.lang.IllegalStateException;	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        r4 = "no start tag found";
        r2.<init>(r4);	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
        throw r2;	 Catch:{ IllegalStateException -> 0x00c5, NullPointerException -> 0x00b1, NumberFormatException -> 0x009d, XmlPullParserException -> 0x0089, IOException -> 0x0075, IndexOutOfBoundsException -> 0x0061 }
    L_0x0061:
        r2 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r0);
        r3.append(r2);
        r0 = r3.toString();
        android.util.Slog.w(r1, r0);
        goto L_0x00d9;
    L_0x0075:
        r2 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r0);
        r3.append(r2);
        r0 = r3.toString();
        android.util.Slog.w(r1, r0);
        goto L_0x00d8;
    L_0x0089:
        r2 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r0);
        r3.append(r2);
        r0 = r3.toString();
        android.util.Slog.w(r1, r0);
        goto L_0x00d8;
    L_0x009d:
        r2 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r0);
        r3.append(r2);
        r0 = r3.toString();
        android.util.Slog.w(r1, r0);
        goto L_0x00d8;
    L_0x00b1:
        r2 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r0);
        r3.append(r2);
        r0 = r3.toString();
        android.util.Slog.w(r1, r0);
        goto L_0x00d8;
    L_0x00c5:
        r2 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r0);
        r3.append(r2);
        r0 = r3.toString();
        android.util.Slog.w(r1, r0);
    L_0x00d9:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.readDailyItemsLocked(org.xmlpull.v1.XmlPullParser):void");
    }

    /* Access modifiers changed, original: 0000 */
    public void readDailyItemTagLocked(XmlPullParser parser) throws NumberFormatException, XmlPullParserException, IOException {
        DailyItem dit = new DailyItem();
        String attr = parser.getAttributeValue(null, BaseMmsColumns.START);
        if (attr != null) {
            dit.mStartTime = Long.parseLong(attr);
        }
        attr = parser.getAttributeValue(null, "end");
        if (attr != null) {
            dit.mEndTime = Long.parseLong(attr);
        }
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                this.mDailyItems.add(dit);
            } else if (type != 3) {
                if (type != 4) {
                    String tagName = parser.getName();
                    String str = "dis";
                    if (tagName.equals(str)) {
                        readDailyItemTagDetailsLocked(parser, dit, false, str);
                    } else {
                        str = "chg";
                        if (tagName.equals(str)) {
                            readDailyItemTagDetailsLocked(parser, dit, true, str);
                        } else {
                            String str2 = "pkg";
                            if (tagName.equals("upd")) {
                                if (dit.mPackageChanges == null) {
                                    dit.mPackageChanges = new ArrayList();
                                }
                                PackageChange pc = new PackageChange();
                                pc.mUpdate = true;
                                pc.mPackageName = parser.getAttributeValue(null, str2);
                                String verStr = parser.getAttributeValue(null, "ver");
                                pc.mVersionCode = verStr != null ? Long.parseLong(verStr) : 0;
                                dit.mPackageChanges.add(pc);
                                XmlUtils.skipCurrentTag(parser);
                            } else if (tagName.equals("rem")) {
                                if (dit.mPackageChanges == null) {
                                    dit.mPackageChanges = new ArrayList();
                                }
                                PackageChange pc2 = new PackageChange();
                                pc2.mUpdate = false;
                                pc2.mPackageName = parser.getAttributeValue(null, str2);
                                dit.mPackageChanges.add(pc2);
                                XmlUtils.skipCurrentTag(parser);
                            } else {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Unknown element under <item>: ");
                                stringBuilder.append(parser.getName());
                                Slog.w(TAG, stringBuilder.toString());
                                XmlUtils.skipCurrentTag(parser);
                            }
                        }
                    }
                }
            }
        }
        this.mDailyItems.add(dit);
    }

    /* Access modifiers changed, original: 0000 */
    public void readDailyItemTagDetailsLocked(XmlPullParser parser, DailyItem dit, boolean isCharge, String tag) throws NumberFormatException, XmlPullParserException, IOException {
        String numAttr = parser.getAttributeValue(null, "n");
        String str = TAG;
        if (numAttr == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Missing 'n' attribute at ");
            stringBuilder.append(parser.getPositionDescription());
            Slog.w(str, stringBuilder.toString());
            XmlUtils.skipCurrentTag(parser);
            return;
        }
        int num = Integer.parseInt(numAttr);
        LevelStepTracker steps = new LevelStepTracker(num);
        if (isCharge) {
            dit.mChargeSteps = steps;
        } else {
            dit.mDischargeSteps = steps;
        }
        int i = 0;
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                steps.mNumStepDurations = i;
            } else if (type != 3) {
                if (type != 4) {
                    if (!RemoteDevice.KEY_STATUS.equals(parser.getName())) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Unknown element under <");
                        stringBuilder2.append(tag);
                        stringBuilder2.append(">: ");
                        stringBuilder2.append(parser.getName());
                        Slog.w(str, stringBuilder2.toString());
                        XmlUtils.skipCurrentTag(parser);
                    } else if (i < num) {
                        String valueAttr = parser.getAttributeValue(null, BaseMmsColumns.MMS_VERSION);
                        if (valueAttr != null) {
                            steps.decodeEntryAt(i, valueAttr);
                            i++;
                        }
                    }
                }
            }
        }
        steps.mNumStepDurations = i;
    }

    public DailyItem getDailyItemLocked(int daysAgo) {
        int index = (this.mDailyItems.size() - 1) - daysAgo;
        return index >= 0 ? (DailyItem) this.mDailyItems.get(index) : null;
    }

    public long getCurrentDailyStartTime() {
        return this.mDailyStartTime;
    }

    public long getNextMinDailyDeadline() {
        return this.mNextMinDailyDeadline;
    }

    public long getNextMaxDailyDeadline() {
        return this.mNextMaxDailyDeadline;
    }

    public boolean startIteratingOldHistoryLocked() {
        HistoryItem historyItem = this.mHistory;
        this.mHistoryIterator = historyItem;
        if (historyItem == null) {
            return false;
        }
        this.mHistoryBuffer.setDataPosition(0);
        this.mHistoryReadTmp.clear();
        this.mReadOverflow = false;
        this.mIteratingHistory = true;
        return true;
    }

    public boolean getNextOldHistoryLocked(HistoryItem out) {
        boolean end = this.mHistoryBuffer.dataPosition() >= this.mHistoryBuffer.dataSize();
        if (!end) {
            readHistoryDelta(this.mHistoryBuffer, this.mHistoryReadTmp);
            this.mReadOverflow |= this.mHistoryReadTmp.cmd == (byte) 6 ? 1 : 0;
        }
        HistoryItem cur = this.mHistoryIterator;
        String str = TAG;
        if (cur == null) {
            if (!(this.mReadOverflow || end)) {
                Slog.w(str, "Old history ends before new history!");
            }
            return false;
        }
        out.setTo(cur);
        this.mHistoryIterator = cur.next;
        if (!this.mReadOverflow) {
            if (end) {
                Slog.w(str, "New history ends before old history!");
            } else if (!out.same(this.mHistoryReadTmp)) {
                PrintWriter pw = new FastPrintWriter(new LogWriter(5, str));
                pw.println("Histories differ!");
                pw.println("Old history:");
                PrintWriter printWriter = pw;
                new HistoryPrinter().printNextItem(printWriter, out, 0, false, true);
                pw.println("New history:");
                new HistoryPrinter().printNextItem(printWriter, this.mHistoryReadTmp, 0, false, true);
                pw.flush();
            }
        }
        return true;
    }

    public void finishIteratingOldHistoryLocked() {
        this.mIteratingHistory = false;
        Parcel parcel = this.mHistoryBuffer;
        parcel.setDataPosition(parcel.dataSize());
        this.mHistoryIterator = null;
    }

    public int getHistoryTotalSize() {
        return this.mConstants.MAX_HISTORY_BUFFER * this.mConstants.MAX_HISTORY_FILES;
    }

    public int getHistoryUsedSize() {
        return this.mBatteryStatsHistory.getHistoryUsedSize();
    }

    @UnsupportedAppUsage
    public boolean startIteratingHistoryLocked() {
        this.mBatteryStatsHistory.startIteratingHistory();
        this.mReadOverflow = false;
        this.mIteratingHistory = true;
        this.mReadHistoryStrings = new String[this.mHistoryTagPool.size()];
        this.mReadHistoryUids = new int[this.mHistoryTagPool.size()];
        this.mReadHistoryChars = 0;
        for (Entry<HistoryTag, Integer> ent : this.mHistoryTagPool.entrySet()) {
            HistoryTag tag = (HistoryTag) ent.getKey();
            int idx = ((Integer) ent.getValue()).intValue();
            this.mReadHistoryStrings[idx] = tag.string;
            this.mReadHistoryUids[idx] = tag.uid;
            this.mReadHistoryChars += tag.string.length() + 1;
        }
        return true;
    }

    public int getHistoryStringPoolSize() {
        return this.mReadHistoryStrings.length;
    }

    public int getHistoryStringPoolBytes() {
        return (this.mReadHistoryStrings.length * 12) + (this.mReadHistoryChars * 2);
    }

    public String getHistoryTagPoolString(int index) {
        return this.mReadHistoryStrings[index];
    }

    public int getHistoryTagPoolUid(int index) {
        return this.mReadHistoryUids[index];
    }

    @UnsupportedAppUsage
    public boolean getNextHistoryLocked(HistoryItem out) {
        Parcel p = this.mBatteryStatsHistory.getNextParcel(out);
        if (p == null) {
            return false;
        }
        long lastRealtime = out.time;
        long lastWalltime = out.currentTime;
        readHistoryDelta(p, out);
        if (!(out.cmd == (byte) 5 || out.cmd == (byte) 7 || lastWalltime == 0)) {
            out.currentTime = (out.time - lastRealtime) + lastWalltime;
        }
        return true;
    }

    public void finishIteratingHistoryLocked() {
        this.mBatteryStatsHistory.finishIteratingHistory();
        this.mIteratingHistory = false;
        this.mReadHistoryStrings = null;
        this.mReadHistoryUids = null;
    }

    public long getHistoryBaseTime() {
        return this.mHistoryBaseTime;
    }

    public int getStartCount() {
        return this.mStartCount;
    }

    @UnsupportedAppUsage
    public boolean isOnBattery() {
        return this.mOnBattery;
    }

    public boolean isCharging() {
        return this.mCharging;
    }

    public boolean isScreenOn(int state) {
        return state == 2 || state == 5 || state == 6;
    }

    public boolean isScreenOff(int state) {
        return state == 1;
    }

    public boolean isScreenDoze(int state) {
        return state == 3 || state == 4;
    }

    /* Access modifiers changed, original: 0000 */
    public void initTimes(long uptime, long realtime) {
        this.mStartClockTime = System.currentTimeMillis();
        this.mOnBatteryTimeBase.init(uptime, realtime);
        this.mOnBatteryScreenOffTimeBase.init(uptime, realtime);
        this.mRealtime = 0;
        this.mUptime = 0;
        this.mRealtimeStart = realtime;
        this.mUptimeStart = uptime;
    }

    /* Access modifiers changed, original: 0000 */
    public void initDischarge() {
        this.mLowDischargeAmountSinceCharge = 0;
        this.mHighDischargeAmountSinceCharge = 0;
        this.mDischargeAmountScreenOn = 0;
        this.mDischargeAmountScreenOnSinceCharge = 0;
        this.mDischargeAmountScreenOff = 0;
        this.mDischargeAmountScreenOffSinceCharge = 0;
        this.mDischargeAmountScreenDoze = 0;
        this.mDischargeAmountScreenDozeSinceCharge = 0;
        this.mDischargeStepTracker.init();
        this.mChargeStepTracker.init();
        this.mDischargeScreenOffCounter.reset(false);
        this.mDischargeScreenDozeCounter.reset(false);
        this.mDischargeLightDozeCounter.reset(false);
        this.mDischargeDeepDozeCounter.reset(false);
        this.mDischargeCounter.reset(false);
    }

    public void resetAllStatsCmdLocked() {
        resetAllStatsLocked();
        long mSecUptime = this.mClocks.uptimeMillis();
        long uptime = mSecUptime * 1000;
        long mSecRealtime = this.mClocks.elapsedRealtime();
        long realtime = 1000 * mSecRealtime;
        this.mDischargeStartLevel = this.mHistoryCur.batteryLevel;
        pullPendingStateUpdatesLocked();
        addHistoryRecordLocked(mSecRealtime, mSecUptime);
        byte b = this.mHistoryCur.batteryLevel;
        this.mCurrentBatteryLevel = b;
        this.mDischargePlugLevel = b;
        this.mDischargeUnplugLevel = b;
        this.mDischargeCurrentLevel = b;
        this.mOnBatteryTimeBase.reset(uptime, realtime);
        this.mOnBatteryScreenOffTimeBase.reset(uptime, realtime);
        if ((this.mHistoryCur.states & 524288) == 0) {
            if (isScreenOn(this.mScreenState)) {
                this.mDischargeScreenOnUnplugLevel = this.mHistoryCur.batteryLevel;
                this.mDischargeScreenDozeUnplugLevel = 0;
                this.mDischargeScreenOffUnplugLevel = 0;
            } else if (isScreenDoze(this.mScreenState)) {
                this.mDischargeScreenOnUnplugLevel = 0;
                this.mDischargeScreenDozeUnplugLevel = this.mHistoryCur.batteryLevel;
                this.mDischargeScreenOffUnplugLevel = 0;
            } else {
                this.mDischargeScreenOnUnplugLevel = 0;
                this.mDischargeScreenDozeUnplugLevel = 0;
                this.mDischargeScreenOffUnplugLevel = this.mHistoryCur.batteryLevel;
            }
            this.mDischargeAmountScreenOn = 0;
            this.mDischargeAmountScreenOff = 0;
            this.mDischargeAmountScreenDoze = 0;
        }
        initActiveHistoryEventsLocked(mSecRealtime, mSecUptime);
    }

    private void resetAllStatsLocked() {
        int i;
        long uptimeMillis = this.mClocks.uptimeMillis();
        long elapsedRealtimeMillis = this.mClocks.elapsedRealtime();
        this.mStartCount = 0;
        initTimes(uptimeMillis * 1000, elapsedRealtimeMillis * 1000);
        this.mScreenOnTimer.reset(false);
        this.mScreenDozeTimer.reset(false);
        for (i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i].reset(false);
        }
        PowerProfile powerProfile = this.mPowerProfile;
        if (powerProfile != null) {
            this.mEstimatedBatteryCapacity = (int) powerProfile.getBatteryCapacity();
        } else {
            this.mEstimatedBatteryCapacity = -1;
        }
        this.mMinLearnedBatteryCapacity = -1;
        this.mMaxLearnedBatteryCapacity = -1;
        this.mInteractiveTimer.reset(false);
        this.mPowerSaveModeEnabledTimer.reset(false);
        this.mLastIdleTimeStart = elapsedRealtimeMillis;
        this.mLongestLightIdleTime = 0;
        this.mLongestFullIdleTime = 0;
        this.mDeviceIdleModeLightTimer.reset(false);
        this.mDeviceIdleModeFullTimer.reset(false);
        this.mDeviceLightIdlingTimer.reset(false);
        this.mDeviceIdlingTimer.reset(false);
        this.mPhoneOnTimer.reset(false);
        this.mAudioOnTimer.reset(false);
        this.mVideoOnTimer.reset(false);
        this.mFlashlightOnTimer.reset(false);
        this.mCameraOnTimer.reset(false);
        this.mBluetoothScanTimer.reset(false);
        for (i = 0; i < 5; i++) {
            this.mPhoneSignalStrengthsTimer[i].reset(false);
        }
        this.mPhoneSignalScanningTimer.reset(false);
        for (i = 0; i < 22; i++) {
            this.mPhoneDataConnectionsTimer[i].reset(false);
        }
        for (i = 0; i < 10; i++) {
            this.mNetworkByteActivityCounters[i].reset(false);
            this.mNetworkPacketActivityCounters[i].reset(false);
        }
        this.mMobileRadioActiveTimer.reset(false);
        this.mMobileRadioActivePerAppTimer.reset(false);
        this.mMobileRadioActiveAdjustedTime.reset(false);
        this.mMobileRadioActiveUnknownTime.reset(false);
        this.mMobileRadioActiveUnknownCount.reset(false);
        this.mWifiOnTimer.reset(false);
        this.mGlobalWifiRunningTimer.reset(false);
        for (i = 0; i < 8; i++) {
            this.mWifiStateTimer[i].reset(false);
        }
        for (i = 0; i < 13; i++) {
            this.mWifiSupplStateTimer[i].reset(false);
        }
        for (i = 0; i < 5; i++) {
            this.mWifiSignalStrengthsTimer[i].reset(false);
        }
        this.mWifiMulticastWakelockTimer.reset(false);
        this.mWifiActiveTimer.reset(false);
        this.mWifiActivity.reset(false);
        for (i = 0; i < 2; i++) {
            this.mGpsSignalQualityTimer[i].reset(false);
        }
        this.mBluetoothActivity.reset(false);
        this.mModemActivity.reset(false);
        this.mNumConnectivityChange = 0;
        i = 0;
        while (i < this.mUidStats.size()) {
            if (((Uid) this.mUidStats.valueAt(i)).reset(uptimeMillis * 1000, elapsedRealtimeMillis * 1000)) {
                ((Uid) this.mUidStats.valueAt(i)).detachFromTimeBase();
                SparseArray sparseArray = this.mUidStats;
                sparseArray.remove(sparseArray.keyAt(i));
                i--;
            }
            i++;
        }
        if (this.mRpmStats.size() > 0) {
            for (SamplingTimer timer : this.mRpmStats.values()) {
                this.mOnBatteryTimeBase.remove(timer);
            }
            this.mRpmStats.clear();
        }
        if (this.mScreenOffRpmStats.size() > 0) {
            for (SamplingTimer timer2 : this.mScreenOffRpmStats.values()) {
                this.mOnBatteryScreenOffTimeBase.remove(timer2);
            }
            this.mScreenOffRpmStats.clear();
        }
        if (this.mKernelWakelockStats.size() > 0) {
            for (SamplingTimer timer22 : this.mKernelWakelockStats.values()) {
                this.mOnBatteryScreenOffTimeBase.remove(timer22);
            }
            this.mKernelWakelockStats.clear();
        }
        if (this.mKernelMemoryStats.size() > 0) {
            for (int i2 = 0; i2 < this.mKernelMemoryStats.size(); i2++) {
                this.mOnBatteryTimeBase.remove((TimeBaseObs) this.mKernelMemoryStats.valueAt(i2));
            }
            this.mKernelMemoryStats.clear();
        }
        if (this.mWakeupReasonStats.size() > 0) {
            for (SamplingTimer timer222 : this.mWakeupReasonStats.values()) {
                this.mOnBatteryTimeBase.remove(timer222);
            }
            this.mWakeupReasonStats.clear();
        }
        this.mTmpRailStats.reset();
        this.mLastHistoryStepDetails = null;
        this.mLastStepCpuSystemTime = 0;
        this.mLastStepCpuUserTime = 0;
        this.mCurStepCpuSystemTime = 0;
        this.mCurStepCpuUserTime = 0;
        this.mCurStepCpuUserTime = 0;
        this.mLastStepCpuUserTime = 0;
        this.mCurStepCpuSystemTime = 0;
        this.mLastStepCpuSystemTime = 0;
        this.mCurStepStatUserTime = 0;
        this.mLastStepStatUserTime = 0;
        this.mCurStepStatSystemTime = 0;
        this.mLastStepStatSystemTime = 0;
        this.mCurStepStatIOWaitTime = 0;
        this.mLastStepStatIOWaitTime = 0;
        this.mCurStepStatIrqTime = 0;
        this.mLastStepStatIrqTime = 0;
        this.mCurStepStatSoftIrqTime = 0;
        this.mLastStepStatSoftIrqTime = 0;
        this.mCurStepStatIdleTime = 0;
        this.mLastStepStatIdleTime = 0;
        this.mNumAllUidCpuTimeReads = 0;
        this.mNumUidsRemoved = 0;
        initDischarge();
        clearHistoryLocked();
        this.mBatteryStatsHistory.resetAllFiles();
        this.mHandler.sendEmptyMessage(4);
    }

    private void initActiveHistoryEventsLocked(long elapsedRealtimeMs, long uptimeMs) {
        int i = 0;
        while (i < 22) {
            if (this.mRecordAllHistory || i != 1) {
                HashMap<String, SparseIntArray> active = this.mActiveEvents.getStateForEvent(i);
                if (active != null) {
                    for (Entry<String, SparseIntArray> ent : active.entrySet()) {
                        SparseIntArray uids = (SparseIntArray) ent.getValue();
                        for (int j = 0; j < uids.size(); j++) {
                            addHistoryEventLocked(elapsedRealtimeMs, uptimeMs, i, (String) ent.getKey(), uids.keyAt(j));
                        }
                    }
                }
            }
            i++;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void updateDischargeScreenLevelsLocked(int oldState, int newState) {
        updateOldDischargeScreenLevelLocked(oldState);
        updateNewDischargeScreenLevelLocked(newState);
    }

    private void updateOldDischargeScreenLevelLocked(int state) {
        int diff;
        if (isScreenOn(state)) {
            diff = this.mDischargeScreenOnUnplugLevel - this.mDischargeCurrentLevel;
            if (diff > 0) {
                this.mDischargeAmountScreenOn += diff;
                this.mDischargeAmountScreenOnSinceCharge += diff;
            }
        } else if (isScreenDoze(state)) {
            diff = this.mDischargeScreenDozeUnplugLevel - this.mDischargeCurrentLevel;
            if (diff > 0) {
                this.mDischargeAmountScreenDoze += diff;
                this.mDischargeAmountScreenDozeSinceCharge += diff;
            }
        } else if (isScreenOff(state)) {
            diff = this.mDischargeScreenOffUnplugLevel - this.mDischargeCurrentLevel;
            if (diff > 0) {
                this.mDischargeAmountScreenOff += diff;
                this.mDischargeAmountScreenOffSinceCharge += diff;
            }
        }
    }

    private void updateNewDischargeScreenLevelLocked(int state) {
        if (isScreenOn(state)) {
            this.mDischargeScreenOnUnplugLevel = this.mDischargeCurrentLevel;
            this.mDischargeScreenOffUnplugLevel = 0;
            this.mDischargeScreenDozeUnplugLevel = 0;
        } else if (isScreenDoze(state)) {
            this.mDischargeScreenOnUnplugLevel = 0;
            this.mDischargeScreenDozeUnplugLevel = this.mDischargeCurrentLevel;
            this.mDischargeScreenOffUnplugLevel = 0;
        } else if (isScreenOff(state)) {
            this.mDischargeScreenOnUnplugLevel = 0;
            this.mDischargeScreenDozeUnplugLevel = 0;
            this.mDischargeScreenOffUnplugLevel = this.mDischargeCurrentLevel;
        }
    }

    public void pullPendingStateUpdatesLocked() {
        if (this.mOnBatteryInternal) {
            int i = this.mScreenState;
            updateDischargeScreenLevelsLocked(i, i);
        }
    }

    private NetworkStats readNetworkStatsLocked(String[] ifaces) {
        String str = TAG;
        try {
            if (!ArrayUtils.isEmpty((Object[]) ifaces)) {
                INetworkStatsService statsService = INetworkStatsService.Stub.asInterface(ServiceManager.getService(Context.NETWORK_STATS_SERVICE));
                if (statsService != null) {
                    return statsService.getDetailedUidStats(ifaces);
                }
                Slog.e(str, "Failed to get networkStatsService ");
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("failed to read network stats for ifaces: ");
            stringBuilder.append(Arrays.toString(ifaces));
            stringBuilder.append(e);
            Slog.e(str, stringBuilder.toString());
        }
        return null;
    }

    /* JADX WARNING: Missing block: B:14:0x0035, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:85:0x0321, code skipped:
            return;
     */
    public void updateWifiState(android.net.wifi.WifiActivityEnergyInfo r45) {
        /*
        r44 = this;
        r1 = r44;
        r2 = 0;
        r3 = r1.mWifiNetworkLock;
        monitor-enter(r3);
        r0 = r1.mWifiIfaces;	 Catch:{ all -> 0x0329 }
        r0 = r1.readNetworkStatsLocked(r0);	 Catch:{ all -> 0x0329 }
        if (r0 == 0) goto L_0x0027;
    L_0x000e:
        r4 = r1.mLastWifiNetworkStats;	 Catch:{ all -> 0x0329 }
        r5 = r1.mNetworkStatsPool;	 Catch:{ all -> 0x0329 }
        r5 = r5.acquire();	 Catch:{ all -> 0x0329 }
        r5 = (android.net.NetworkStats) r5;	 Catch:{ all -> 0x0329 }
        r6 = 0;
        r4 = android.net.NetworkStats.subtract(r0, r4, r6, r6, r5);	 Catch:{ all -> 0x0329 }
        r2 = r4;
        r4 = r1.mNetworkStatsPool;	 Catch:{ all -> 0x0329 }
        r5 = r1.mLastWifiNetworkStats;	 Catch:{ all -> 0x0329 }
        r4.release(r5);	 Catch:{ all -> 0x0329 }
        r1.mLastWifiNetworkStats = r0;	 Catch:{ all -> 0x0329 }
    L_0x0027:
        monitor-exit(r3);	 Catch:{ all -> 0x0329 }
        monitor-enter(r44);
        r0 = r1.mOnBatteryInternal;	 Catch:{ all -> 0x0326 }
        if (r0 != 0) goto L_0x0036;
    L_0x002d:
        if (r2 == 0) goto L_0x0034;
    L_0x002f:
        r0 = r1.mNetworkStatsPool;	 Catch:{ all -> 0x0326 }
        r0.release(r2);	 Catch:{ all -> 0x0326 }
    L_0x0034:
        monitor-exit(r44);	 Catch:{ all -> 0x0326 }
        return;
    L_0x0036:
        r0 = r1.mClocks;	 Catch:{ all -> 0x0326 }
        r3 = r0.elapsedRealtime();	 Catch:{ all -> 0x0326 }
        r0 = new android.util.SparseLongArray;	 Catch:{ all -> 0x0326 }
        r0.<init>();	 Catch:{ all -> 0x0326 }
        r5 = new android.util.SparseLongArray;	 Catch:{ all -> 0x0326 }
        r5.<init>();	 Catch:{ all -> 0x0326 }
        r6 = 0;
        r8 = 0;
        if (r2 == 0) goto L_0x011d;
    L_0x004c:
        r12 = new android.net.NetworkStats$Entry;	 Catch:{ all -> 0x0326 }
        r12.<init>();	 Catch:{ all -> 0x0326 }
        r13 = r2.size();	 Catch:{ all -> 0x0326 }
        r14 = 0;
    L_0x0056:
        if (r14 >= r13) goto L_0x0114;
    L_0x0058:
        r15 = r2.getValues(r14, r12);	 Catch:{ all -> 0x0326 }
        r12 = r15;
        r10 = r12.rxBytes;	 Catch:{ all -> 0x0326 }
        r15 = 0;
        r10 = (r10 > r15 ? 1 : (r10 == r15 ? 0 : -1));
        if (r10 != 0) goto L_0x006f;
    L_0x0065:
        r10 = r12.txBytes;	 Catch:{ all -> 0x0326 }
        r10 = (r10 > r15 ? 1 : (r10 == r15 ? 0 : -1));
        if (r10 != 0) goto L_0x006f;
    L_0x006b:
        r23 = r3;
        goto L_0x010e;
    L_0x006f:
        r10 = r12.uid;	 Catch:{ all -> 0x0326 }
        r10 = r1.mapUid(r10);	 Catch:{ all -> 0x0326 }
        r17 = r1.getUidStatsLocked(r10);	 Catch:{ all -> 0x0326 }
        r10 = r12.rxBytes;	 Catch:{ all -> 0x0326 }
        r15 = 0;
        r10 = (r10 > r15 ? 1 : (r10 == r15 ? 0 : -1));
        if (r10 == 0) goto L_0x00c3;
    L_0x0081:
        r18 = 2;
        r10 = r12.rxBytes;	 Catch:{ all -> 0x0326 }
        r23 = r3;
        r3 = r12.rxPackets;	 Catch:{ all -> 0x0326 }
        r19 = r10;
        r21 = r3;
        r17.noteNetworkActivityLocked(r18, r19, r21);	 Catch:{ all -> 0x0326 }
        r3 = r12.set;	 Catch:{ all -> 0x0326 }
        if (r3 != 0) goto L_0x00a3;
    L_0x0094:
        r26 = 8;
        r3 = r12.rxBytes;	 Catch:{ all -> 0x0326 }
        r10 = r12.rxPackets;	 Catch:{ all -> 0x0326 }
        r25 = r17;
        r27 = r3;
        r29 = r10;
        r25.noteNetworkActivityLocked(r26, r27, r29);	 Catch:{ all -> 0x0326 }
    L_0x00a3:
        r3 = r1.mNetworkByteActivityCounters;	 Catch:{ all -> 0x0326 }
        r4 = 2;
        r3 = r3[r4];	 Catch:{ all -> 0x0326 }
        r10 = r12.rxBytes;	 Catch:{ all -> 0x0326 }
        r3.addCountLocked(r10);	 Catch:{ all -> 0x0326 }
        r3 = r1.mNetworkPacketActivityCounters;	 Catch:{ all -> 0x0326 }
        r3 = r3[r4];	 Catch:{ all -> 0x0326 }
        r10 = r12.rxPackets;	 Catch:{ all -> 0x0326 }
        r3.addCountLocked(r10);	 Catch:{ all -> 0x0326 }
        r3 = r17.getUid();	 Catch:{ all -> 0x0326 }
        r10 = r12.rxPackets;	 Catch:{ all -> 0x0326 }
        r0.put(r3, r10);	 Catch:{ all -> 0x0326 }
        r3 = r12.rxPackets;	 Catch:{ all -> 0x0326 }
        r8 = r8 + r3;
        goto L_0x00c5;
    L_0x00c3:
        r23 = r3;
    L_0x00c5:
        r3 = r12.txBytes;	 Catch:{ all -> 0x0326 }
        r10 = 0;
        r3 = (r3 > r10 ? 1 : (r3 == r10 ? 0 : -1));
        if (r3 == 0) goto L_0x010e;
    L_0x00cd:
        r26 = 3;
        r3 = r12.txBytes;	 Catch:{ all -> 0x0326 }
        r10 = r12.txPackets;	 Catch:{ all -> 0x0326 }
        r25 = r17;
        r27 = r3;
        r29 = r10;
        r25.noteNetworkActivityLocked(r26, r27, r29);	 Catch:{ all -> 0x0326 }
        r3 = r12.set;	 Catch:{ all -> 0x0326 }
        if (r3 != 0) goto L_0x00ef;
    L_0x00e0:
        r26 = 9;
        r3 = r12.txBytes;	 Catch:{ all -> 0x0326 }
        r10 = r12.txPackets;	 Catch:{ all -> 0x0326 }
        r25 = r17;
        r27 = r3;
        r29 = r10;
        r25.noteNetworkActivityLocked(r26, r27, r29);	 Catch:{ all -> 0x0326 }
    L_0x00ef:
        r3 = r1.mNetworkByteActivityCounters;	 Catch:{ all -> 0x0326 }
        r4 = 3;
        r3 = r3[r4];	 Catch:{ all -> 0x0326 }
        r10 = r12.txBytes;	 Catch:{ all -> 0x0326 }
        r3.addCountLocked(r10);	 Catch:{ all -> 0x0326 }
        r3 = r1.mNetworkPacketActivityCounters;	 Catch:{ all -> 0x0326 }
        r3 = r3[r4];	 Catch:{ all -> 0x0326 }
        r10 = r12.txPackets;	 Catch:{ all -> 0x0326 }
        r3.addCountLocked(r10);	 Catch:{ all -> 0x0326 }
        r3 = r17.getUid();	 Catch:{ all -> 0x0326 }
        r10 = r12.txPackets;	 Catch:{ all -> 0x0326 }
        r5.put(r3, r10);	 Catch:{ all -> 0x0326 }
        r3 = r12.txPackets;	 Catch:{ all -> 0x0326 }
        r6 = r6 + r3;
    L_0x010e:
        r14 = r14 + 1;
        r3 = r23;
        goto L_0x0056;
    L_0x0114:
        r23 = r3;
        r3 = r1.mNetworkStatsPool;	 Catch:{ all -> 0x0326 }
        r3.release(r2);	 Catch:{ all -> 0x0326 }
        r2 = 0;
        goto L_0x011f;
    L_0x011d:
        r23 = r3;
    L_0x011f:
        if (r45 == 0) goto L_0x031a;
    L_0x0121:
        r3 = 1;
        r1.mHasWifiReporting = r3;	 Catch:{ all -> 0x0316 }
        r3 = r45.getControllerTxTimeMillis();	 Catch:{ all -> 0x0316 }
        r10 = r45.getControllerRxTimeMillis();	 Catch:{ all -> 0x0316 }
        r12 = r45.getControllerScanTimeMillis();	 Catch:{ all -> 0x0316 }
        r17 = r45.getControllerIdleTimeMillis();	 Catch:{ all -> 0x0316 }
        r19 = r3 + r10;
        r19 = r19 + r17;
        r21 = r10;
        r25 = r3;
        r27 = 0;
        r29 = 0;
        r14 = r1.mUidStats;	 Catch:{ all -> 0x0316 }
        r14 = r14.size();	 Catch:{ all -> 0x0316 }
        r31 = 0;
        r15 = r31;
    L_0x014a:
        r33 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        if (r15 >= r14) goto L_0x017d;
    L_0x014e:
        r31 = r2;
        r2 = r1.mUidStats;	 Catch:{ all -> 0x0322 }
        r2 = r2.valueAt(r15);	 Catch:{ all -> 0x0322 }
        r2 = (com.android.internal.os.BatteryStatsImpl.Uid) r2;	 Catch:{ all -> 0x0322 }
        r35 = r12;
        r12 = r2.mWifiScanTimer;	 Catch:{ all -> 0x0322 }
        r37 = r8;
        r8 = r23 * r33;
        r8 = r12.getTimeSinceMarkLocked(r8);	 Catch:{ all -> 0x0322 }
        r8 = r8 / r33;
        r29 = r29 + r8;
        r8 = r2.mFullWifiLockTimer;	 Catch:{ all -> 0x0322 }
        r12 = r23 * r33;
        r8 = r8.getTimeSinceMarkLocked(r12);	 Catch:{ all -> 0x0322 }
        r8 = r8 / r33;
        r27 = r27 + r8;
        r15 = r15 + 1;
        r2 = r31;
        r12 = r35;
        r8 = r37;
        goto L_0x014a;
    L_0x017d:
        r31 = r2;
        r37 = r8;
        r35 = r12;
        r2 = 0;
    L_0x0184:
        if (r2 >= r14) goto L_0x022e;
    L_0x0186:
        r9 = r1.mUidStats;	 Catch:{ all -> 0x0322 }
        r9 = r9.valueAt(r2);	 Catch:{ all -> 0x0322 }
        r9 = (com.android.internal.os.BatteryStatsImpl.Uid) r9;	 Catch:{ all -> 0x0322 }
        r12 = r9.mWifiScanTimer;	 Catch:{ all -> 0x0322 }
        r39 = r9;
        r8 = r23 * r33;
        r8 = r12.getTimeSinceMarkLocked(r8);	 Catch:{ all -> 0x0322 }
        r8 = r8 / r33;
        r15 = 0;
        r12 = (r8 > r15 ? 1 : (r8 == r15 ? 0 : -1));
        if (r12 <= 0) goto L_0x01f2;
    L_0x01a0:
        r12 = r39;
        r13 = r12.mWifiScanTimer;	 Catch:{ all -> 0x0322 }
        r32 = r14;
        r14 = r23;
        r13.setMark(r14);	 Catch:{ all -> 0x0322 }
        r23 = r8;
        r40 = r8;
        r13 = (r29 > r10 ? 1 : (r29 == r10 ? 0 : -1));
        if (r13 <= 0) goto L_0x01be;
    L_0x01b3:
        r42 = r10 * r23;
        r42 = r42 / r29;
        r23 = r42;
        r42 = r8;
        r8 = r23;
        goto L_0x01c2;
    L_0x01be:
        r42 = r8;
        r8 = r23;
    L_0x01c2:
        r13 = (r29 > r3 ? 1 : (r29 == r3 ? 0 : -1));
        if (r13 <= 0) goto L_0x01d1;
    L_0x01c6:
        r23 = r3 * r40;
        r23 = r23 / r29;
        r40 = r23;
        r23 = r3;
        r3 = r40;
        goto L_0x01d5;
    L_0x01d1:
        r23 = r3;
        r3 = r40;
        r13 = r12.getOrCreateWifiControllerActivityLocked();	 Catch:{ all -> 0x0322 }
        r40 = r10;
        r10 = r13.getRxTimeCounter();	 Catch:{ all -> 0x0322 }
        r10.addCountLocked(r8);	 Catch:{ all -> 0x0322 }
        r10 = r13.getTxTimeCounters();	 Catch:{ all -> 0x0322 }
        r11 = 0;
        r10 = r10[r11];	 Catch:{ all -> 0x0322 }
        r10.addCountLocked(r3);	 Catch:{ all -> 0x0322 }
        r21 = r21 - r8;
        r25 = r25 - r3;
        goto L_0x01fe;
    L_0x01f2:
        r42 = r8;
        r40 = r10;
        r32 = r14;
        r14 = r23;
        r12 = r39;
        r23 = r3;
    L_0x01fe:
        r3 = r12.mFullWifiLockTimer;	 Catch:{ all -> 0x0322 }
        r8 = r14 * r33;
        r3 = r3.getTimeSinceMarkLocked(r8);	 Catch:{ all -> 0x0322 }
        r3 = r3 / r33;
        r8 = 0;
        r10 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1));
        if (r10 <= 0) goto L_0x0222;
    L_0x020e:
        r10 = r12.mFullWifiLockTimer;	 Catch:{ all -> 0x0322 }
        r10.setMark(r14);	 Catch:{ all -> 0x0322 }
        r10 = r3 * r17;
        r10 = r10 / r27;
        r13 = r12.getOrCreateWifiControllerActivityLocked();	 Catch:{ all -> 0x0322 }
        r13 = r13.getIdleTimeCounter();	 Catch:{ all -> 0x0322 }
        r13.addCountLocked(r10);	 Catch:{ all -> 0x0322 }
    L_0x0222:
        r2 = r2 + 1;
        r3 = r23;
        r10 = r40;
        r23 = r14;
        r14 = r32;
        goto L_0x0184;
    L_0x022e:
        r40 = r10;
        r32 = r14;
        r14 = r23;
        r23 = r3;
        r2 = 0;
        r3 = r2;
    L_0x0238:
        r2 = r5.size();	 Catch:{ all -> 0x0322 }
        if (r3 >= r2) goto L_0x025e;
    L_0x023e:
        r2 = r5.keyAt(r3);	 Catch:{ all -> 0x0322 }
        r2 = r1.getUidStatsLocked(r2);	 Catch:{ all -> 0x0322 }
        r8 = r5.valueAt(r3);	 Catch:{ all -> 0x0322 }
        r8 = r8 * r25;
        r8 = r8 / r6;
        r4 = r2.getOrCreateWifiControllerActivityLocked();	 Catch:{ all -> 0x0322 }
        r4 = r4.getTxTimeCounters();	 Catch:{ all -> 0x0322 }
        r10 = 0;
        r4 = r4[r10];	 Catch:{ all -> 0x0322 }
        r4.addCountLocked(r8);	 Catch:{ all -> 0x0322 }
        r3 = r3 + 1;
        goto L_0x0238;
    L_0x025e:
        r2 = 0;
        r3 = r2;
    L_0x0260:
        r2 = r0.size();	 Catch:{ all -> 0x0322 }
        if (r3 >= r2) goto L_0x0284;
    L_0x0266:
        r2 = r0.keyAt(r3);	 Catch:{ all -> 0x0322 }
        r2 = r1.getUidStatsLocked(r2);	 Catch:{ all -> 0x0322 }
        r8 = r0.valueAt(r3);	 Catch:{ all -> 0x0322 }
        r8 = r8 * r21;
        r8 = r8 / r37;
        r4 = r2.getOrCreateWifiControllerActivityLocked();	 Catch:{ all -> 0x0322 }
        r4 = r4.getRxTimeCounter();	 Catch:{ all -> 0x0322 }
        r4.addCountLocked(r8);	 Catch:{ all -> 0x0322 }
        r3 = r3 + 1;
        goto L_0x0260;
    L_0x0284:
        r2 = r1.mWifiActivity;	 Catch:{ all -> 0x0322 }
        r2 = r2.getRxTimeCounter();	 Catch:{ all -> 0x0322 }
        r3 = r45.getControllerRxTimeMillis();	 Catch:{ all -> 0x0322 }
        r2.addCountLocked(r3);	 Catch:{ all -> 0x0322 }
        r2 = r1.mWifiActivity;	 Catch:{ all -> 0x0322 }
        r2 = r2.getTxTimeCounters();	 Catch:{ all -> 0x0322 }
        r3 = 0;
        r2 = r2[r3];	 Catch:{ all -> 0x0322 }
        r3 = r45.getControllerTxTimeMillis();	 Catch:{ all -> 0x0322 }
        r2.addCountLocked(r3);	 Catch:{ all -> 0x0322 }
        r2 = r1.mWifiActivity;	 Catch:{ all -> 0x0322 }
        r2 = r2.getScanTimeCounter();	 Catch:{ all -> 0x0322 }
        r3 = r45.getControllerScanTimeMillis();	 Catch:{ all -> 0x0322 }
        r2.addCountLocked(r3);	 Catch:{ all -> 0x0322 }
        r2 = r1.mWifiActivity;	 Catch:{ all -> 0x0322 }
        r2 = r2.getIdleTimeCounter();	 Catch:{ all -> 0x0322 }
        r3 = r45.getControllerIdleTimeMillis();	 Catch:{ all -> 0x0322 }
        r2.addCountLocked(r3);	 Catch:{ all -> 0x0322 }
        r2 = r1.mPowerProfile;	 Catch:{ all -> 0x0322 }
        r3 = "wifi.controller.voltage";
        r2 = r2.getAveragePower(r3);	 Catch:{ all -> 0x0322 }
        r8 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r2 = r2 / r8;
        r8 = 0;
        r4 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x02e0;
    L_0x02d0:
        r4 = r1.mWifiActivity;	 Catch:{ all -> 0x0322 }
        r4 = r4.getPowerCounter();	 Catch:{ all -> 0x0322 }
        r8 = r45.getControllerEnergyUsed();	 Catch:{ all -> 0x0322 }
        r8 = (double) r8;	 Catch:{ all -> 0x0322 }
        r8 = r8 / r2;
        r8 = (long) r8;	 Catch:{ all -> 0x0322 }
        r4.addCountLocked(r8);	 Catch:{ all -> 0x0322 }
    L_0x02e0:
        r4 = r1.mTmpRailStats;	 Catch:{ all -> 0x0322 }
        r8 = r4.getWifiTotalEnergyUseduWs();	 Catch:{ all -> 0x0322 }
        r8 = (double) r8;	 Catch:{ all -> 0x0322 }
        r8 = r8 / r2;
        r8 = (long) r8;	 Catch:{ all -> 0x0322 }
        r4 = r1.mWifiActivity;	 Catch:{ all -> 0x0322 }
        r4 = r4.getMonitoredRailChargeConsumedMaMs();	 Catch:{ all -> 0x0322 }
        r4.addCountLocked(r8);	 Catch:{ all -> 0x0322 }
        r4 = r1.mHistoryCur;	 Catch:{ all -> 0x0322 }
        r10 = r4.wifiRailChargeMah;	 Catch:{ all -> 0x0322 }
        r12 = (double) r8;	 Catch:{ all -> 0x0322 }
        r33 = 4704985352480227328; // 0x414b774000000000 float:0.0 double:3600000.0;
        r12 = r12 / r33;
        r10 = r10 + r12;
        r4.wifiRailChargeMah = r10;	 Catch:{ all -> 0x0322 }
        r4 = r1.mClocks;	 Catch:{ all -> 0x0322 }
        r10 = r4.elapsedRealtime();	 Catch:{ all -> 0x0322 }
        r4 = r1.mClocks;	 Catch:{ all -> 0x0322 }
        r12 = r4.uptimeMillis();	 Catch:{ all -> 0x0322 }
        r1.addHistoryRecordLocked(r10, r12);	 Catch:{ all -> 0x0322 }
        r4 = r1.mTmpRailStats;	 Catch:{ all -> 0x0322 }
        r4.resetWifiTotalEnergyUsed();	 Catch:{ all -> 0x0322 }
        goto L_0x0320;
    L_0x0316:
        r0 = move-exception;
        r31 = r2;
        goto L_0x0327;
    L_0x031a:
        r31 = r2;
        r37 = r8;
        r14 = r23;
    L_0x0320:
        monitor-exit(r44);	 Catch:{ all -> 0x0322 }
        return;
    L_0x0322:
        r0 = move-exception;
        r2 = r31;
        goto L_0x0327;
    L_0x0326:
        r0 = move-exception;
    L_0x0327:
        monitor-exit(r44);	 Catch:{ all -> 0x0326 }
        throw r0;
    L_0x0329:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0329 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.updateWifiState(android.net.wifi.WifiActivityEnergyInfo):void");
    }

    private ModemActivityInfo getDeltaModemActivityInfo(ModemActivityInfo activityInfo) {
        if (activityInfo == null) {
            return null;
        }
        int[] txTimeMs = new int[5];
        for (int i = 0; i < 5; i++) {
            txTimeMs[i] = activityInfo.getTxTimeMillis()[i] - this.mLastModemActivityInfo.getTxTimeMillis()[i];
        }
        ModemActivityInfo modemActivityInfo = new ModemActivityInfo(activityInfo.getTimestamp(), activityInfo.getSleepTimeMillis() - this.mLastModemActivityInfo.getSleepTimeMillis(), activityInfo.getIdleTimeMillis() - this.mLastModemActivityInfo.getIdleTimeMillis(), txTimeMs, activityInfo.getRxTimeMillis() - this.mLastModemActivityInfo.getRxTimeMillis(), activityInfo.getEnergyUsed() - this.mLastModemActivityInfo.getEnergyUsed());
        this.mLastModemActivityInfo = activityInfo;
        return modemActivityInfo;
    }

    /* JADX WARNING: Missing block: B:11:0x0035, code skipped:
            monitor-enter(r36);
     */
    /* JADX WARNING: Missing block: B:14:0x0038, code skipped:
            if (r1.mOnBatteryInternal != false) goto L_0x0043;
     */
    /* JADX WARNING: Missing block: B:15:0x003a, code skipped:
            if (r3 == null) goto L_0x0041;
     */
    /* JADX WARNING: Missing block: B:17:?, code skipped:
            r1.mNetworkStatsPool.release(r3);
     */
    /* JADX WARNING: Missing block: B:18:0x0041, code skipped:
            monitor-exit(r36);
     */
    /* JADX WARNING: Missing block: B:19:0x0042, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:21:0x0046, code skipped:
            if (r2 == null) goto L_0x0133;
     */
    /* JADX WARNING: Missing block: B:22:0x0048, code skipped:
            r1.mHasModemReporting = true;
            r1.mModemActivity.getIdleTimeCounter().addCountLocked((long) r2.getIdleTimeMillis());
            r1.mModemActivity.getSleepTimeCounter().addCountLocked((long) r2.getSleepTimeMillis());
            r1.mModemActivity.getRxTimeCounter().addCountLocked((long) r2.getRxTimeMillis());
            r6 = 0;
     */
    /* JADX WARNING: Missing block: B:23:0x0075, code skipped:
            if (r6 >= 5) goto L_0x008c;
     */
    /* JADX WARNING: Missing block: B:24:0x0077, code skipped:
            r1.mModemActivity.getTxTimeCounters()[r6].addCountLocked((long) r2.getTxTimeMillis()[r6]);
            r6 = r6 + 1;
     */
    /* JADX WARNING: Missing block: B:25:0x008c, code skipped:
            r6 = r1.mPowerProfile.getAveragePower(com.android.internal.os.PowerProfile.POWER_MODEM_CONTROLLER_OPERATING_VOLTAGE) / 1000.0d;
     */
    /* JADX WARNING: Missing block: B:26:0x009f, code skipped:
            if (r6 == 0.0d) goto L_0x0133;
     */
    /* JADX WARNING: Missing block: B:27:0x00a1, code skipped:
            r8 = ((((double) r2.getSleepTimeMillis()) * r1.mPowerProfile.getAveragePower(com.android.internal.os.PowerProfile.POWER_MODEM_CONTROLLER_SLEEP)) + (((double) r2.getIdleTimeMillis()) * r1.mPowerProfile.getAveragePower(com.android.internal.os.PowerProfile.POWER_MODEM_CONTROLLER_IDLE))) + (((double) r2.getRxTimeMillis()) * r1.mPowerProfile.getAveragePower(com.android.internal.os.PowerProfile.POWER_MODEM_CONTROLLER_RX));
            r10 = r2.getTxTimeMillis();
            r11 = 0;
     */
    /* JADX WARNING: Missing block: B:29:0x00db, code skipped:
            if (r11 >= java.lang.Math.min(r10.length, 5)) goto L_0x00ee;
     */
    /* JADX WARNING: Missing block: B:30:0x00dd, code skipped:
            r8 = r8 + (((double) r10[r11]) * r1.mPowerProfile.getAveragePower(com.android.internal.os.PowerProfile.POWER_MODEM_CONTROLLER_TX, r11));
            r11 = r11 + 1;
     */
    /* JADX WARNING: Missing block: B:31:0x00ee, code skipped:
            r1.mModemActivity.getPowerCounter().addCountLocked((long) r8);
            r11 = (long) (((double) r1.mTmpRailStats.getCellularTotalEnergyUseduWs()) / r6);
            r1.mModemActivity.getMonitoredRailChargeConsumedMaMs().addCountLocked(r11);
            r13 = r1.mHistoryCur;
            r13.modemRailChargeMah += ((double) r11) / 3600000.0d;
            r1.addHistoryRecordLocked(r1.mClocks.elapsedRealtime(), r1.mClocks.uptimeMillis());
            r1.mTmpRailStats.resetCellularTotalEnergyUsed();
     */
    /* JADX WARNING: Missing block: B:32:0x012e, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:33:0x012f, code skipped:
            r16 = r2;
     */
    /* JADX WARNING: Missing block: B:35:?, code skipped:
            r4 = r1.mClocks.elapsedRealtime();
            r6 = r1.mMobileRadioActivePerAppTimer.getTimeSinceMarkLocked(1000 * r4);
            r1.mMobileRadioActivePerAppTimer.setMark(r4);
            r8 = 0;
            r10 = 0;
     */
    /* JADX WARNING: Missing block: B:36:0x014b, code skipped:
            if (r3 == null) goto L_0x02e2;
     */
    /* JADX WARNING: Missing block: B:37:0x014d, code skipped:
            r12 = new android.net.NetworkStats.Entry();
            r13 = r3.size();
     */
    /* JADX WARNING: Missing block: B:38:0x0156, code skipped:
            r14 = 0;
     */
    /* JADX WARNING: Missing block: B:40:0x0159, code skipped:
            if (r14 >= r13) goto L_0x0200;
     */
    /* JADX WARNING: Missing block: B:43:0x015f, code skipped:
            r12 = r3.getValues(r14, r12);
     */
    /* JADX WARNING: Missing block: B:46:0x0164, code skipped:
            if (r12.rxPackets != 0) goto L_0x0176;
     */
    /* JADX WARNING: Missing block: B:48:0x016a, code skipped:
            if (r12.txPackets != 0) goto L_0x0176;
     */
    /* JADX WARNING: Missing block: B:49:0x016c, code skipped:
            r1 = r36;
            r24 = r4;
            r26 = r6;
            r4 = 0;
     */
    /* JADX WARNING: Missing block: B:50:0x0176, code skipped:
            r8 = r8 + r12.rxPackets;
            r10 = r10 + r12.txPackets;
     */
    /* JADX WARNING: Missing block: B:51:0x017e, code skipped:
            r1 = r36;
     */
    /* JADX WARNING: Missing block: B:53:?, code skipped:
            r18 = r1.getUidStatsLocked(r1.mapUid(r12.uid));
            r24 = r4;
            r26 = r6;
            r18.noteNetworkActivityLocked(0, r12.rxBytes, r12.rxPackets);
            r18.noteNetworkActivityLocked(1, r12.txBytes, r12.txPackets);
     */
    /* JADX WARNING: Missing block: B:54:0x01aa, code skipped:
            if (r12.set != 0) goto L_0x01ca;
     */
    /* JADX WARNING: Missing block: B:55:0x01ac, code skipped:
            r18.noteNetworkActivityLocked(6, r12.rxBytes, r12.rxPackets);
            r18.noteNetworkActivityLocked(7, r12.txBytes, r12.txPackets);
     */
    /* JADX WARNING: Missing block: B:56:0x01ca, code skipped:
            r1.mNetworkByteActivityCounters[0].addCountLocked(r12.rxBytes);
            r1.mNetworkByteActivityCounters[1].addCountLocked(r12.txBytes);
            r4 = 0;
            r1.mNetworkPacketActivityCounters[0].addCountLocked(r12.rxPackets);
            r1.mNetworkPacketActivityCounters[1].addCountLocked(r12.txPackets);
     */
    /* JADX WARNING: Missing block: B:57:0x01f2, code skipped:
            r14 = r14 + 1;
            r0 = r4;
            r4 = r24;
            r6 = r26;
     */
    /* JADX WARNING: Missing block: B:58:0x01fb, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:59:0x01fc, code skipped:
            r1 = r36;
     */
    /* JADX WARNING: Missing block: B:60:0x0200, code skipped:
            r24 = r4;
            r26 = r6;
            r4 = r8 + r10;
     */
    /* JADX WARNING: Missing block: B:61:0x0208, code skipped:
            if (r4 <= 0) goto L_0x02c0;
     */
    /* JADX WARNING: Missing block: B:62:0x020a, code skipped:
            r0 = 0;
            r6 = r26;
     */
    /* JADX WARNING: Missing block: B:63:0x020d, code skipped:
            if (r0 >= r13) goto L_0x02ba;
     */
    /* JADX WARNING: Missing block: B:65:?, code skipped:
            r12 = r3.getValues(r0, r12);
     */
    /* JADX WARNING: Missing block: B:67:0x0218, code skipped:
            if (r12.rxPackets != 0) goto L_0x0227;
     */
    /* JADX WARNING: Missing block: B:71:0x021e, code skipped:
            if (r12.txPackets != 0) goto L_0x0227;
     */
    /* JADX WARNING: Missing block: B:72:0x0220, code skipped:
            r15 = r0;
            r16 = r2;
            r20 = r10;
     */
    /* JADX WARNING: Missing block: B:74:?, code skipped:
            r14 = r1.getUidStatsLocked(r1.mapUid(r12.uid));
            r15 = r0;
            r20 = r10;
            r0 = r12.rxPackets + r12.txPackets;
            r10 = (r6 * r0) / r4;
            r14.noteMobileRadioActiveTimeLocked(r10);
            r6 = r6 - r10;
            r4 = r4 - r0;
     */
    /* JADX WARNING: Missing block: B:75:0x0241, code skipped:
            if (r2 == null) goto L_0x02a2;
     */
    /* JADX WARNING: Missing block: B:76:0x0243, code skipped:
            r17 = r14.getOrCreateModemControllerActivityLocked();
     */
    /* JADX WARNING: Missing block: B:78:0x024a, code skipped:
            if (r8 <= 0) goto L_0x026a;
     */
    /* JADX WARNING: Missing block: B:79:0x024c, code skipped:
            r22 = r0;
     */
    /* JADX WARNING: Missing block: B:82:0x0252, code skipped:
            if (r12.rxPackets <= 0) goto L_0x0267;
     */
    /* JADX WARNING: Missing block: B:83:0x0254, code skipped:
            r26 = r4;
            r17.getRxTimeCounter().addCountLocked((r12.rxPackets * ((long) r2.getRxTimeMillis())) / r8);
     */
    /* JADX WARNING: Missing block: B:84:0x0267, code skipped:
            r26 = r4;
     */
    /* JADX WARNING: Missing block: B:85:0x026a, code skipped:
            r22 = r0;
            r26 = r4;
     */
    /* JADX WARNING: Missing block: B:87:0x0270, code skipped:
            if (r20 <= 0) goto L_0x029f;
     */
    /* JADX WARNING: Missing block: B:90:0x0276, code skipped:
            if (r12.txPackets <= 0) goto L_0x029f;
     */
    /* JADX WARNING: Missing block: B:91:0x0278, code skipped:
            r0 = null;
     */
    /* JADX WARNING: Missing block: B:93:0x027a, code skipped:
            if (r0 >= 5) goto L_0x029c;
     */
    /* JADX WARNING: Missing block: B:95:0x0284, code skipped:
            r16 = r2;
     */
    /* JADX WARNING: Missing block: B:97:?, code skipped:
            r17.getTxTimeCounters()[r0].addCountLocked((r12.txPackets * ((long) r2.getTxTimeMillis()[r0])) / r20);
     */
    /* JADX WARNING: Missing block: B:98:0x0293, code skipped:
            r0 = r0 + 1;
            r2 = r16;
     */
    /* JADX WARNING: Missing block: B:99:0x0298, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:100:0x0299, code skipped:
            r1 = r36;
     */
    /* JADX WARNING: Missing block: B:101:0x029c, code skipped:
            r16 = r2;
     */
    /* JADX WARNING: Missing block: B:102:0x029f, code skipped:
            r16 = r2;
     */
    /* JADX WARNING: Missing block: B:103:0x02a2, code skipped:
            r22 = r0;
            r16 = r2;
            r26 = r4;
     */
    /* JADX WARNING: Missing block: B:104:0x02a8, code skipped:
            r4 = r26;
     */
    /* JADX WARNING: Missing block: B:105:0x02aa, code skipped:
            r0 = r15 + 1;
            r1 = r36;
            r2 = r16;
            r10 = r20;
     */
    /* JADX WARNING: Missing block: B:106:0x02b4, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:107:0x02b5, code skipped:
            r16 = r2;
            r1 = r36;
     */
    /* JADX WARNING: Missing block: B:108:0x02ba, code skipped:
            r15 = r0;
            r16 = r2;
            r20 = r10;
     */
    /* JADX WARNING: Missing block: B:109:0x02c0, code skipped:
            r16 = r2;
            r20 = r10;
            r6 = r26;
     */
    /* JADX WARNING: Missing block: B:111:0x02c8, code skipped:
            if (r6 <= 0) goto L_0x02d9;
     */
    /* JADX WARNING: Missing block: B:112:0x02ca, code skipped:
            r1 = r36;
     */
    /* JADX WARNING: Missing block: B:114:?, code skipped:
            r1.mMobileRadioActiveUnknownTime.addCountLocked(r6);
            r1.mMobileRadioActiveUnknownCount.addCountLocked(1);
     */
    /* JADX WARNING: Missing block: B:115:0x02d9, code skipped:
            r1 = r36;
     */
    /* JADX WARNING: Missing block: B:116:0x02db, code skipped:
            r1.mNetworkStatsPool.release(r3);
     */
    /* JADX WARNING: Missing block: B:117:0x02e2, code skipped:
            r16 = r2;
            r24 = r4;
            r26 = r6;
     */
    /* JADX WARNING: Missing block: B:118:0x02e8, code skipped:
            monitor-exit(r36);
     */
    /* JADX WARNING: Missing block: B:119:0x02e9, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:120:0x02ea, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:121:0x02eb, code skipped:
            r16 = r2;
     */
    /* JADX WARNING: Missing block: B:122:0x02ed, code skipped:
            monitor-exit(r36);
     */
    /* JADX WARNING: Missing block: B:123:0x02ee, code skipped:
            throw r0;
     */
    /* JADX WARNING: Missing block: B:124:0x02ef, code skipped:
            r0 = th;
     */
    public void updateMobileRadioState(android.telephony.ModemActivityInfo r37) {
        /*
        r36 = this;
        r1 = r36;
        r2 = r36.getDeltaModemActivityInfo(r37);
        r1.addModemTxPowerToHistory(r2);
        r3 = 0;
        r4 = r1.mModemNetworkLock;
        monitor-enter(r4);
        r0 = r1.mModemIfaces;	 Catch:{ all -> 0x02f1 }
        r0 = r1.readNetworkStatsLocked(r0);	 Catch:{ all -> 0x02f1 }
        if (r0 == 0) goto L_0x0034;
    L_0x0015:
        r5 = r1.mLastModemNetworkStats;	 Catch:{ all -> 0x002f }
        r6 = r1.mNetworkStatsPool;	 Catch:{ all -> 0x002f }
        r6 = r6.acquire();	 Catch:{ all -> 0x002f }
        r6 = (android.net.NetworkStats) r6;	 Catch:{ all -> 0x002f }
        r7 = 0;
        r5 = android.net.NetworkStats.subtract(r0, r5, r7, r7, r6);	 Catch:{ all -> 0x002f }
        r3 = r5;
        r5 = r1.mNetworkStatsPool;	 Catch:{ all -> 0x002f }
        r6 = r1.mLastModemNetworkStats;	 Catch:{ all -> 0x002f }
        r5.release(r6);	 Catch:{ all -> 0x002f }
        r1.mLastModemNetworkStats = r0;	 Catch:{ all -> 0x002f }
        goto L_0x0034;
    L_0x002f:
        r0 = move-exception;
        r16 = r2;
        goto L_0x02f4;
    L_0x0034:
        monitor-exit(r4);	 Catch:{ all -> 0x02f1 }
        monitor-enter(r36);
        r0 = r1.mOnBatteryInternal;	 Catch:{ all -> 0x02ea }
        if (r0 != 0) goto L_0x0043;
    L_0x003a:
        if (r3 == 0) goto L_0x0041;
    L_0x003c:
        r0 = r1.mNetworkStatsPool;	 Catch:{ all -> 0x012e }
        r0.release(r3);	 Catch:{ all -> 0x012e }
    L_0x0041:
        monitor-exit(r36);	 Catch:{ all -> 0x012e }
        return;
    L_0x0043:
        r0 = 0;
        r4 = 5;
        r5 = 1;
        if (r2 == 0) goto L_0x0133;
    L_0x0048:
        r1.mHasModemReporting = r5;	 Catch:{ all -> 0x012e }
        r6 = r1.mModemActivity;	 Catch:{ all -> 0x012e }
        r6 = r6.getIdleTimeCounter();	 Catch:{ all -> 0x012e }
        r7 = r2.getIdleTimeMillis();	 Catch:{ all -> 0x012e }
        r7 = (long) r7;	 Catch:{ all -> 0x012e }
        r6.addCountLocked(r7);	 Catch:{ all -> 0x012e }
        r6 = r1.mModemActivity;	 Catch:{ all -> 0x012e }
        r6 = r6.getSleepTimeCounter();	 Catch:{ all -> 0x012e }
        r7 = r2.getSleepTimeMillis();	 Catch:{ all -> 0x012e }
        r7 = (long) r7;	 Catch:{ all -> 0x012e }
        r6.addCountLocked(r7);	 Catch:{ all -> 0x012e }
        r6 = r1.mModemActivity;	 Catch:{ all -> 0x012e }
        r6 = r6.getRxTimeCounter();	 Catch:{ all -> 0x012e }
        r7 = r2.getRxTimeMillis();	 Catch:{ all -> 0x012e }
        r7 = (long) r7;	 Catch:{ all -> 0x012e }
        r6.addCountLocked(r7);	 Catch:{ all -> 0x012e }
        r6 = 0;
    L_0x0075:
        if (r6 >= r4) goto L_0x008c;
    L_0x0077:
        r7 = r1.mModemActivity;	 Catch:{ all -> 0x012e }
        r7 = r7.getTxTimeCounters();	 Catch:{ all -> 0x012e }
        r7 = r7[r6];	 Catch:{ all -> 0x012e }
        r8 = r2.getTxTimeMillis();	 Catch:{ all -> 0x012e }
        r8 = r8[r6];	 Catch:{ all -> 0x012e }
        r8 = (long) r8;	 Catch:{ all -> 0x012e }
        r7.addCountLocked(r8);	 Catch:{ all -> 0x012e }
        r6 = r6 + 1;
        goto L_0x0075;
    L_0x008c:
        r6 = r1.mPowerProfile;	 Catch:{ all -> 0x012e }
        r7 = "modem.controller.voltage";
        r6 = r6.getAveragePower(r7);	 Catch:{ all -> 0x012e }
        r8 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r6 = r6 / r8;
        r8 = 0;
        r8 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r8 == 0) goto L_0x0133;
        r8 = r2.getSleepTimeMillis();	 Catch:{ all -> 0x012e }
        r8 = (double) r8;	 Catch:{ all -> 0x012e }
        r10 = r1.mPowerProfile;	 Catch:{ all -> 0x012e }
        r11 = "modem.controller.sleep";
        r10 = r10.getAveragePower(r11);	 Catch:{ all -> 0x012e }
        r8 = r8 * r10;
        r10 = r2.getIdleTimeMillis();	 Catch:{ all -> 0x012e }
        r10 = (double) r10;	 Catch:{ all -> 0x012e }
        r12 = r1.mPowerProfile;	 Catch:{ all -> 0x012e }
        r13 = "modem.controller.idle";
        r12 = r12.getAveragePower(r13);	 Catch:{ all -> 0x012e }
        r10 = r10 * r12;
        r8 = r8 + r10;
        r10 = r2.getRxTimeMillis();	 Catch:{ all -> 0x012e }
        r10 = (double) r10;	 Catch:{ all -> 0x012e }
        r12 = r1.mPowerProfile;	 Catch:{ all -> 0x012e }
        r13 = "modem.controller.rx";
        r12 = r12.getAveragePower(r13);	 Catch:{ all -> 0x012e }
        r10 = r10 * r12;
        r8 = r8 + r10;
        r10 = r2.getTxTimeMillis();	 Catch:{ all -> 0x012e }
        r11 = r0;
    L_0x00d6:
        r12 = r10.length;	 Catch:{ all -> 0x012e }
        r12 = java.lang.Math.min(r12, r4);	 Catch:{ all -> 0x012e }
        if (r11 >= r12) goto L_0x00ee;
    L_0x00dd:
        r12 = r10[r11];	 Catch:{ all -> 0x012e }
        r12 = (double) r12;	 Catch:{ all -> 0x012e }
        r14 = r1.mPowerProfile;	 Catch:{ all -> 0x012e }
        r15 = "modem.controller.tx";
        r14 = r14.getAveragePower(r15, r11);	 Catch:{ all -> 0x012e }
        r12 = r12 * r14;
        r8 = r8 + r12;
        r11 = r11 + 1;
        goto L_0x00d6;
    L_0x00ee:
        r11 = r1.mModemActivity;	 Catch:{ all -> 0x012e }
        r11 = r11.getPowerCounter();	 Catch:{ all -> 0x012e }
        r12 = (long) r8;	 Catch:{ all -> 0x012e }
        r11.addCountLocked(r12);	 Catch:{ all -> 0x012e }
        r11 = r1.mTmpRailStats;	 Catch:{ all -> 0x012e }
        r11 = r11.getCellularTotalEnergyUseduWs();	 Catch:{ all -> 0x012e }
        r11 = (double) r11;	 Catch:{ all -> 0x012e }
        r11 = r11 / r6;
        r11 = (long) r11;	 Catch:{ all -> 0x012e }
        r13 = r1.mModemActivity;	 Catch:{ all -> 0x012e }
        r13 = r13.getMonitoredRailChargeConsumedMaMs();	 Catch:{ all -> 0x012e }
        r13.addCountLocked(r11);	 Catch:{ all -> 0x012e }
        r13 = r1.mHistoryCur;	 Catch:{ all -> 0x012e }
        r14 = r13.modemRailChargeMah;	 Catch:{ all -> 0x012e }
        r4 = (double) r11;	 Catch:{ all -> 0x012e }
        r18 = 4704985352480227328; // 0x414b774000000000 float:0.0 double:3600000.0;
        r4 = r4 / r18;
        r14 = r14 + r4;
        r13.modemRailChargeMah = r14;	 Catch:{ all -> 0x012e }
        r4 = r1.mClocks;	 Catch:{ all -> 0x012e }
        r4 = r4.elapsedRealtime();	 Catch:{ all -> 0x012e }
        r13 = r1.mClocks;	 Catch:{ all -> 0x012e }
        r13 = r13.uptimeMillis();	 Catch:{ all -> 0x012e }
        r1.addHistoryRecordLocked(r4, r13);	 Catch:{ all -> 0x012e }
        r4 = r1.mTmpRailStats;	 Catch:{ all -> 0x012e }
        r4.resetCellularTotalEnergyUsed();	 Catch:{ all -> 0x012e }
        goto L_0x0133;
    L_0x012e:
        r0 = move-exception;
    L_0x012f:
        r16 = r2;
        goto L_0x02ed;
    L_0x0133:
        r4 = r1.mClocks;	 Catch:{ all -> 0x02ea }
        r4 = r4.elapsedRealtime();	 Catch:{ all -> 0x02ea }
        r6 = r1.mMobileRadioActivePerAppTimer;	 Catch:{ all -> 0x02ea }
        r7 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r7 = r7 * r4;
        r6 = r6.getTimeSinceMarkLocked(r7);	 Catch:{ all -> 0x02ea }
        r8 = r1.mMobileRadioActivePerAppTimer;	 Catch:{ all -> 0x02ea }
        r8.setMark(r4);	 Catch:{ all -> 0x02ea }
        r8 = 0;
        r10 = 0;
        if (r3 == 0) goto L_0x02e2;
    L_0x014d:
        r12 = new android.net.NetworkStats$Entry;	 Catch:{ all -> 0x02ea }
        r12.<init>();	 Catch:{ all -> 0x02ea }
        r13 = r3.size();	 Catch:{ all -> 0x02ea }
        r14 = 0;
    L_0x0157:
        r18 = 0;
        if (r14 >= r13) goto L_0x0200;
    L_0x015b:
        r15 = r3.getValues(r14, r12);	 Catch:{ all -> 0x012e }
        r12 = r15;
        r0 = r12.rxPackets;	 Catch:{ all -> 0x01fb }
        r0 = (r0 > r18 ? 1 : (r0 == r18 ? 0 : -1));
        if (r0 != 0) goto L_0x0176;
    L_0x0166:
        r0 = r12.txPackets;	 Catch:{ all -> 0x01fb }
        r0 = (r0 > r18 ? 1 : (r0 == r18 ? 0 : -1));
        if (r0 != 0) goto L_0x0176;
    L_0x016c:
        r1 = r36;
        r24 = r4;
        r26 = r6;
        r4 = 0;
        r5 = 1;
        goto L_0x01f2;
    L_0x0176:
        r0 = r12.rxPackets;	 Catch:{ all -> 0x01fb }
        r8 = r8 + r0;
        r0 = r12.txPackets;	 Catch:{ all -> 0x01fb }
        r10 = r10 + r0;
        r0 = r12.uid;	 Catch:{ all -> 0x01fb }
        r1 = r36;
        r0 = r1.mapUid(r0);	 Catch:{ all -> 0x012e }
        r18 = r1.getUidStatsLocked(r0);	 Catch:{ all -> 0x012e }
        r19 = 0;
        r24 = r4;
        r4 = r12.rxBytes;	 Catch:{ all -> 0x012e }
        r26 = r6;
        r6 = r12.rxPackets;	 Catch:{ all -> 0x012e }
        r20 = r4;
        r22 = r6;
        r18.noteNetworkActivityLocked(r19, r20, r22);	 Catch:{ all -> 0x012e }
        r29 = 1;
        r4 = r12.txBytes;	 Catch:{ all -> 0x012e }
        r6 = r12.txPackets;	 Catch:{ all -> 0x012e }
        r28 = r18;
        r30 = r4;
        r32 = r6;
        r28.noteNetworkActivityLocked(r29, r30, r32);	 Catch:{ all -> 0x012e }
        r0 = r12.set;	 Catch:{ all -> 0x012e }
        if (r0 != 0) goto L_0x01ca;
    L_0x01ac:
        r29 = 6;
        r4 = r12.rxBytes;	 Catch:{ all -> 0x012e }
        r6 = r12.rxPackets;	 Catch:{ all -> 0x012e }
        r28 = r18;
        r30 = r4;
        r32 = r6;
        r28.noteNetworkActivityLocked(r29, r30, r32);	 Catch:{ all -> 0x012e }
        r31 = 7;
        r4 = r12.txBytes;	 Catch:{ all -> 0x012e }
        r6 = r12.txPackets;	 Catch:{ all -> 0x012e }
        r30 = r18;
        r32 = r4;
        r34 = r6;
        r30.noteNetworkActivityLocked(r31, r32, r34);	 Catch:{ all -> 0x012e }
    L_0x01ca:
        r0 = r1.mNetworkByteActivityCounters;	 Catch:{ all -> 0x012e }
        r4 = 0;
        r0 = r0[r4];	 Catch:{ all -> 0x012e }
        r4 = r12.rxBytes;	 Catch:{ all -> 0x012e }
        r0.addCountLocked(r4);	 Catch:{ all -> 0x012e }
        r0 = r1.mNetworkByteActivityCounters;	 Catch:{ all -> 0x012e }
        r4 = 1;
        r0 = r0[r4];	 Catch:{ all -> 0x012e }
        r4 = r12.txBytes;	 Catch:{ all -> 0x012e }
        r0.addCountLocked(r4);	 Catch:{ all -> 0x012e }
        r0 = r1.mNetworkPacketActivityCounters;	 Catch:{ all -> 0x012e }
        r4 = 0;
        r0 = r0[r4];	 Catch:{ all -> 0x012e }
        r5 = r12.rxPackets;	 Catch:{ all -> 0x012e }
        r0.addCountLocked(r5);	 Catch:{ all -> 0x012e }
        r0 = r1.mNetworkPacketActivityCounters;	 Catch:{ all -> 0x012e }
        r5 = 1;
        r0 = r0[r5];	 Catch:{ all -> 0x012e }
        r6 = r12.txPackets;	 Catch:{ all -> 0x012e }
        r0.addCountLocked(r6);	 Catch:{ all -> 0x012e }
    L_0x01f2:
        r14 = r14 + 1;
        r0 = r4;
        r4 = r24;
        r6 = r26;
        goto L_0x0157;
    L_0x01fb:
        r0 = move-exception;
        r1 = r36;
        goto L_0x012f;
    L_0x0200:
        r24 = r4;
        r26 = r6;
        r4 = r8 + r10;
        r0 = (r4 > r18 ? 1 : (r4 == r18 ? 0 : -1));
        if (r0 <= 0) goto L_0x02c0;
    L_0x020a:
        r0 = 0;
        r6 = r26;
    L_0x020d:
        if (r0 >= r13) goto L_0x02ba;
    L_0x020f:
        r14 = r3.getValues(r0, r12);	 Catch:{ all -> 0x02b4 }
        r12 = r14;
        r14 = r12.rxPackets;	 Catch:{ all -> 0x02b4 }
        r14 = (r14 > r18 ? 1 : (r14 == r18 ? 0 : -1));
        if (r14 != 0) goto L_0x0227;
    L_0x021a:
        r14 = r12.txPackets;	 Catch:{ all -> 0x012e }
        r14 = (r14 > r18 ? 1 : (r14 == r18 ? 0 : -1));
        if (r14 != 0) goto L_0x0227;
    L_0x0220:
        r15 = r0;
        r16 = r2;
        r20 = r10;
        goto L_0x02aa;
    L_0x0227:
        r14 = r12.uid;	 Catch:{ all -> 0x02b4 }
        r14 = r1.mapUid(r14);	 Catch:{ all -> 0x02b4 }
        r14 = r1.getUidStatsLocked(r14);	 Catch:{ all -> 0x02b4 }
        r15 = r0;
        r0 = r12.rxPackets;	 Catch:{ all -> 0x02b4 }
        r20 = r10;
        r10 = r12.txPackets;	 Catch:{ all -> 0x02b4 }
        r0 = r0 + r10;
        r10 = r6 * r0;
        r10 = r10 / r4;
        r14.noteMobileRadioActiveTimeLocked(r10);	 Catch:{ all -> 0x02b4 }
        r6 = r6 - r10;
        r4 = r4 - r0;
        if (r2 == 0) goto L_0x02a2;
        r17 = r14.getOrCreateModemControllerActivityLocked();	 Catch:{ all -> 0x02b4 }
        r22 = (r8 > r18 ? 1 : (r8 == r18 ? 0 : -1));
        if (r22 <= 0) goto L_0x026a;
    L_0x024c:
        r22 = r0;
        r0 = r12.rxPackets;	 Catch:{ all -> 0x01fb }
        r0 = (r0 > r18 ? 1 : (r0 == r18 ? 0 : -1));
        if (r0 <= 0) goto L_0x0267;
    L_0x0254:
        r0 = r12.rxPackets;	 Catch:{ all -> 0x01fb }
        r26 = r4;
        r4 = r2.getRxTimeMillis();	 Catch:{ all -> 0x01fb }
        r4 = (long) r4;	 Catch:{ all -> 0x01fb }
        r0 = r0 * r4;
        r0 = r0 / r8;
        r4 = r17.getRxTimeCounter();	 Catch:{ all -> 0x01fb }
        r4.addCountLocked(r0);	 Catch:{ all -> 0x01fb }
        goto L_0x026e;
    L_0x0267:
        r26 = r4;
        goto L_0x026e;
    L_0x026a:
        r22 = r0;
        r26 = r4;
    L_0x026e:
        r0 = (r20 > r18 ? 1 : (r20 == r18 ? 0 : -1));
        if (r0 <= 0) goto L_0x029f;
    L_0x0272:
        r0 = r12.txPackets;	 Catch:{ all -> 0x02b4 }
        r0 = (r0 > r18 ? 1 : (r0 == r18 ? 0 : -1));
        if (r0 <= 0) goto L_0x029f;
    L_0x0278:
        r0 = 0;
    L_0x0279:
        r1 = 5;
        if (r0 >= r1) goto L_0x029c;
    L_0x027c:
        r4 = r12.txPackets;	 Catch:{ all -> 0x02b4 }
        r16 = r2.getTxTimeMillis();	 Catch:{ all -> 0x02b4 }
        r1 = r16[r0];	 Catch:{ all -> 0x02b4 }
        r16 = r2;
        r1 = (long) r1;
        r4 = r4 * r1;
        r1 = r4 / r20;
        r4 = r17.getTxTimeCounters();	 Catch:{ all -> 0x0298 }
        r4 = r4[r0];	 Catch:{ all -> 0x0298 }
        r4.addCountLocked(r1);	 Catch:{ all -> 0x0298 }
        r0 = r0 + 1;
        r2 = r16;
        goto L_0x0279;
    L_0x0298:
        r0 = move-exception;
        r1 = r36;
        goto L_0x02ed;
    L_0x029c:
        r16 = r2;
        goto L_0x02a8;
    L_0x029f:
        r16 = r2;
        goto L_0x02a8;
    L_0x02a2:
        r22 = r0;
        r16 = r2;
        r26 = r4;
    L_0x02a8:
        r4 = r26;
    L_0x02aa:
        r0 = r15 + 1;
        r1 = r36;
        r2 = r16;
        r10 = r20;
        goto L_0x020d;
    L_0x02b4:
        r0 = move-exception;
        r16 = r2;
        r1 = r36;
        goto L_0x02ed;
    L_0x02ba:
        r15 = r0;
        r16 = r2;
        r20 = r10;
        goto L_0x02c6;
    L_0x02c0:
        r16 = r2;
        r20 = r10;
        r6 = r26;
    L_0x02c6:
        r0 = (r6 > r18 ? 1 : (r6 == r18 ? 0 : -1));
        if (r0 <= 0) goto L_0x02d9;
    L_0x02ca:
        r1 = r36;
        r0 = r1.mMobileRadioActiveUnknownTime;	 Catch:{ all -> 0x02ef }
        r0.addCountLocked(r6);	 Catch:{ all -> 0x02ef }
        r0 = r1.mMobileRadioActiveUnknownCount;	 Catch:{ all -> 0x02ef }
        r10 = 1;
        r0.addCountLocked(r10);	 Catch:{ all -> 0x02ef }
        goto L_0x02db;
    L_0x02d9:
        r1 = r36;
    L_0x02db:
        r0 = r1.mNetworkStatsPool;	 Catch:{ all -> 0x02ef }
        r0.release(r3);	 Catch:{ all -> 0x02ef }
        r3 = 0;
        goto L_0x02e8;
    L_0x02e2:
        r16 = r2;
        r24 = r4;
        r26 = r6;
    L_0x02e8:
        monitor-exit(r36);	 Catch:{ all -> 0x02ef }
        return;
    L_0x02ea:
        r0 = move-exception;
        r16 = r2;
    L_0x02ed:
        monitor-exit(r36);	 Catch:{ all -> 0x02ef }
        throw r0;
    L_0x02ef:
        r0 = move-exception;
        goto L_0x02ed;
    L_0x02f1:
        r0 = move-exception;
        r16 = r2;
    L_0x02f4:
        monitor-exit(r4);	 Catch:{ all -> 0x02f6 }
        throw r0;
    L_0x02f6:
        r0 = move-exception;
        goto L_0x02f4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.updateMobileRadioState(android.telephony.ModemActivityInfo):void");
    }

    /* JADX WARNING: Missing block: B:23:0x0042, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:29:0x0058, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:31:0x005a, code skipped:
            return;
     */
    private synchronized void addModemTxPowerToHistory(android.telephony.ModemActivityInfo r11) {
        /*
        r10 = this;
        monitor-enter(r10);
        if (r11 != 0) goto L_0x0005;
    L_0x0003:
        monitor-exit(r10);
        return;
    L_0x0005:
        r0 = r11.getTxTimeMillis();	 Catch:{ all -> 0x005b }
        if (r0 == 0) goto L_0x0059;
    L_0x000b:
        r1 = r0.length;	 Catch:{ all -> 0x005b }
        r2 = 5;
        if (r1 == r2) goto L_0x0010;
    L_0x000f:
        goto L_0x0059;
    L_0x0010:
        r1 = r10.mClocks;	 Catch:{ all -> 0x005b }
        r1 = r1.elapsedRealtime();	 Catch:{ all -> 0x005b }
        r3 = r10.mClocks;	 Catch:{ all -> 0x005b }
        r3 = r3.uptimeMillis();	 Catch:{ all -> 0x005b }
        r5 = 0;
        r6 = 1;
        r7 = r6;
    L_0x001f:
        r8 = r0.length;	 Catch:{ all -> 0x005b }
        if (r7 >= r8) goto L_0x002c;
    L_0x0022:
        r8 = r0[r7];	 Catch:{ all -> 0x005b }
        r9 = r0[r5];	 Catch:{ all -> 0x005b }
        if (r8 <= r9) goto L_0x0029;
    L_0x0028:
        r5 = r7;
    L_0x0029:
        r7 = r7 + 1;
        goto L_0x001f;
    L_0x002c:
        r7 = 4;
        if (r5 != r7) goto L_0x0043;
    L_0x002f:
        r7 = r10.mIsCellularTxPowerHigh;	 Catch:{ all -> 0x005b }
        if (r7 != 0) goto L_0x0041;
    L_0x0033:
        r7 = r10.mHistoryCur;	 Catch:{ all -> 0x005b }
        r8 = r7.states2;	 Catch:{ all -> 0x005b }
        r9 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        r8 = r8 | r9;
        r7.states2 = r8;	 Catch:{ all -> 0x005b }
        r10.addHistoryRecordLocked(r1, r3);	 Catch:{ all -> 0x005b }
        r10.mIsCellularTxPowerHigh = r6;	 Catch:{ all -> 0x005b }
    L_0x0041:
        monitor-exit(r10);
        return;
    L_0x0043:
        r6 = r10.mIsCellularTxPowerHigh;	 Catch:{ all -> 0x005b }
        if (r6 == 0) goto L_0x0057;
    L_0x0047:
        r6 = r10.mHistoryCur;	 Catch:{ all -> 0x005b }
        r7 = r6.states2;	 Catch:{ all -> 0x005b }
        r8 = -524289; // 0xfffffffffff7ffff float:NaN double:NaN;
        r7 = r7 & r8;
        r6.states2 = r7;	 Catch:{ all -> 0x005b }
        r10.addHistoryRecordLocked(r1, r3);	 Catch:{ all -> 0x005b }
        r6 = 0;
        r10.mIsCellularTxPowerHigh = r6;	 Catch:{ all -> 0x005b }
    L_0x0057:
        monitor-exit(r10);
        return;
    L_0x0059:
        monitor-exit(r10);
        return;
    L_0x005b:
        r11 = move-exception;
        monitor-exit(r10);
        throw r11;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.addModemTxPowerToHistory(android.telephony.ModemActivityInfo):void");
    }

    public void updateBluetoothStateLocked(BluetoothActivityEnergyInfo info) {
        BluetoothActivityEnergyInfo bluetoothActivityEnergyInfo = info;
        BluetoothActivityEnergyInfo bluetoothActivityEnergyInfo2;
        if (bluetoothActivityEnergyInfo == null) {
            bluetoothActivityEnergyInfo2 = bluetoothActivityEnergyInfo;
        } else if (this.mOnBatteryInternal) {
            Uid u;
            int uidCount;
            long elapsedRealtimeMs;
            long idleTimeMs;
            long j;
            boolean normalizeScanTxTime;
            long txTimeMs;
            this.mHasBluetoothReporting = true;
            long elapsedRealtimeMs2 = this.mClocks.elapsedRealtime();
            long rxTimeMs = info.getControllerRxTimeMillis() - this.mLastBluetoothActivityInfo.rxTimeMs;
            long txTimeMs2 = info.getControllerTxTimeMillis() - this.mLastBluetoothActivityInfo.txTimeMs;
            long idleTimeMs2 = info.getControllerIdleTimeMillis() - this.mLastBluetoothActivityInfo.idleTimeMs;
            long totalScanTimeMs = 0;
            int uidCount2 = this.mUidStats.size();
            int i = 0;
            while (i < uidCount2) {
                u = (Uid) this.mUidStats.valueAt(i);
                if (u.mBluetoothScanTimer != null) {
                    totalScanTimeMs += u.mBluetoothScanTimer.getTimeSinceMarkLocked(elapsedRealtimeMs2 * 1000) / 1000;
                }
                i++;
                bluetoothActivityEnergyInfo = info;
            }
            boolean normalizeScanRxTime = totalScanTimeMs > rxTimeMs;
            boolean normalizeScanTxTime2 = totalScanTimeMs > txTimeMs2;
            long leftOverTxTimeMs = txTimeMs2;
            int i2 = 0;
            long leftOverRxTimeMs = rxTimeMs;
            while (i2 < uidCount2) {
                boolean normalizeScanRxTime2;
                u = (Uid) this.mUidStats.valueAt(i2);
                uidCount = uidCount2;
                if (u.mBluetoothScanTimer == null) {
                    normalizeScanRxTime2 = normalizeScanRxTime;
                    elapsedRealtimeMs = elapsedRealtimeMs2;
                    idleTimeMs = idleTimeMs2;
                } else {
                    idleTimeMs = idleTimeMs2;
                    idleTimeMs2 = u.mBluetoothScanTimer.getTimeSinceMarkLocked(elapsedRealtimeMs2 * 1000) / 1000;
                    if (idleTimeMs2 > 0) {
                        u.mBluetoothScanTimer.setMark(elapsedRealtimeMs2);
                        long scanTimeRxSinceMarkMs = idleTimeMs2;
                        long scanTimeTxSinceMarkMs = idleTimeMs2;
                        if (normalizeScanRxTime) {
                            elapsedRealtimeMs = elapsedRealtimeMs2;
                            elapsedRealtimeMs2 = (rxTimeMs * scanTimeRxSinceMarkMs) / totalScanTimeMs;
                        } else {
                            elapsedRealtimeMs = elapsedRealtimeMs2;
                            elapsedRealtimeMs2 = scanTimeRxSinceMarkMs;
                        }
                        if (normalizeScanTxTime2) {
                            j = idleTimeMs2;
                            idleTimeMs2 = (txTimeMs2 * scanTimeTxSinceMarkMs) / totalScanTimeMs;
                        } else {
                            idleTimeMs2 = scanTimeTxSinceMarkMs;
                        }
                        ControllerActivityCounterImpl counter = u.getOrCreateBluetoothControllerActivityLocked();
                        normalizeScanRxTime2 = normalizeScanRxTime;
                        counter.getRxTimeCounter().addCountLocked(elapsedRealtimeMs2);
                        counter.getTxTimeCounters()[0].addCountLocked(idleTimeMs2);
                        leftOverRxTimeMs -= elapsedRealtimeMs2;
                        leftOverTxTimeMs -= idleTimeMs2;
                    } else {
                        normalizeScanRxTime2 = normalizeScanRxTime;
                        elapsedRealtimeMs = elapsedRealtimeMs2;
                        j = idleTimeMs2;
                    }
                }
                i2++;
                uidCount2 = uidCount;
                idleTimeMs2 = idleTimeMs;
                normalizeScanRxTime = normalizeScanRxTime2;
                elapsedRealtimeMs2 = elapsedRealtimeMs;
            }
            elapsedRealtimeMs = elapsedRealtimeMs2;
            idleTimeMs = idleTimeMs2;
            uidCount = uidCount2;
            long totalTxBytes = 0;
            elapsedRealtimeMs2 = 0;
            UidTraffic[] uidTraffic = info.getUidTraffic();
            int numUids = uidTraffic != null ? uidTraffic.length : 0;
            uidCount2 = 0;
            while (uidCount2 < numUids) {
                UidTraffic traffic = uidTraffic[uidCount2];
                j = totalScanTimeMs;
                totalScanTimeMs = traffic.getRxBytes() - this.mLastBluetoothActivityInfo.uidRxBytes.get(traffic.getUid());
                normalizeScanTxTime = normalizeScanTxTime2;
                txTimeMs = txTimeMs2;
                txTimeMs2 = traffic.getTxBytes() - this.mLastBluetoothActivityInfo.uidTxBytes.get(traffic.getUid());
                this.mNetworkByteActivityCounters[4].addCountLocked(totalScanTimeMs);
                this.mNetworkByteActivityCounters[5].addCountLocked(txTimeMs2);
                Uid uidStatsLocked = getUidStatsLocked(mapUid(traffic.getUid()));
                uidStatsLocked.noteNetworkActivityLocked(4, totalScanTimeMs, 0);
                uidStatsLocked.noteNetworkActivityLocked(5, txTimeMs2, 0);
                elapsedRealtimeMs2 += totalScanTimeMs;
                totalTxBytes += txTimeMs2;
                uidCount2++;
                normalizeScanTxTime2 = normalizeScanTxTime;
                totalScanTimeMs = j;
                txTimeMs2 = txTimeMs;
            }
            txTimeMs = txTimeMs2;
            j = totalScanTimeMs;
            normalizeScanTxTime = normalizeScanTxTime2;
            long j2;
            UidTraffic[] uidTrafficArr;
            if ((totalTxBytes == 0 && elapsedRealtimeMs2 == 0) || (leftOverRxTimeMs == 0 && leftOverTxTimeMs == 0)) {
                j2 = elapsedRealtimeMs2;
                uidTrafficArr = uidTraffic;
            } else {
                int i3 = 0;
                while (i3 < numUids) {
                    UidTraffic traffic2 = uidTraffic[i3];
                    int uid = traffic2.getUid();
                    long rxBytes = traffic2.getRxBytes() - this.mLastBluetoothActivityInfo.uidRxBytes.get(uid);
                    long txBytes = traffic2.getTxBytes() - this.mLastBluetoothActivityInfo.uidTxBytes.get(uid);
                    Uid u2 = getUidStatsLocked(mapUid(uid));
                    ControllerActivityCounterImpl counter2 = u2.getOrCreateBluetoothControllerActivityLocked();
                    if (elapsedRealtimeMs2 <= 0 || rxBytes <= 0) {
                        j2 = elapsedRealtimeMs2;
                        Uid uid2 = u2;
                        uidTrafficArr = uidTraffic;
                    } else {
                        uidTrafficArr = uidTraffic;
                        j2 = elapsedRealtimeMs2;
                        counter2.getRxTimeCounter().addCountLocked((leftOverRxTimeMs * rxBytes) / elapsedRealtimeMs2);
                    }
                    if (totalTxBytes > 0 && txBytes > 0) {
                        counter2.getTxTimeCounters()[0].addCountLocked((leftOverTxTimeMs * txBytes) / totalTxBytes);
                    }
                    i3++;
                    elapsedRealtimeMs2 = j2;
                    uidTraffic = uidTrafficArr;
                }
                uidTrafficArr = uidTraffic;
            }
            this.mBluetoothActivity.getRxTimeCounter().addCountLocked(rxTimeMs);
            this.mBluetoothActivity.getTxTimeCounters()[0].addCountLocked(txTimeMs);
            this.mBluetoothActivity.getIdleTimeCounter().addCountLocked(idleTimeMs);
            double opVolt = this.mPowerProfile.getAveragePower(PowerProfile.POWER_BLUETOOTH_CONTROLLER_OPERATING_VOLTAGE) / 1000.0d;
            if (opVolt != 0.0d) {
                this.mBluetoothActivity.getPowerCounter().addCountLocked((long) (((double) (info.getControllerEnergyUsed() - this.mLastBluetoothActivityInfo.energy)) / opVolt));
            }
            this.mLastBluetoothActivityInfo.set(info);
        } else {
            bluetoothActivityEnergyInfo2 = bluetoothActivityEnergyInfo;
        }
    }

    public void updateRpmStatsLocked() {
        if (this.mPlatformIdleStateCallback != null) {
            String str;
            long now = SystemClock.elapsedRealtime();
            long j = 1000;
            if (now - this.mLastRpmStatsUpdateTimeMs >= 1000) {
                this.mPlatformIdleStateCallback.fillLowPowerStats(this.mTmpRpmStats);
                this.mLastRpmStatsUpdateTimeMs = now;
            }
            Iterator it = this.mTmpRpmStats.mPlatformLowPowerStats.entrySet().iterator();
            while (true) {
                str = ".";
                if (!it.hasNext()) {
                    break;
                }
                Entry<String, PowerStatePlatformSleepState> pstate = (Entry) it.next();
                String pName = (String) pstate.getKey();
                getRpmTimerLocked(pName).update(((PowerStatePlatformSleepState) pstate.getValue()).mTimeMs * j, ((PowerStatePlatformSleepState) pstate.getValue()).mCount);
                for (Entry<String, PowerStateElement> voter : ((PowerStatePlatformSleepState) pstate.getValue()).mVoters.entrySet()) {
                    String vName = new StringBuilder();
                    vName.append(pName);
                    vName.append(str);
                    vName.append((String) voter.getKey());
                    vName = vName.toString();
                    long now2 = now;
                    getRpmTimerLocked(vName).update(((PowerStateElement) voter.getValue()).mTimeMs * j, ((PowerStateElement) voter.getValue()).mCount);
                    now = now2;
                    j = 1000;
                }
                j = 1000;
            }
            for (Entry<String, PowerStateSubsystem> subsys : this.mTmpRpmStats.mSubsystemLowPowerStats.entrySet()) {
                String subsysName = (String) subsys.getKey();
                for (Entry<String, PowerStateElement> sstate : ((PowerStateSubsystem) subsys.getValue()).mStates.entrySet()) {
                    String name = new StringBuilder();
                    name.append(subsysName);
                    name.append(str);
                    name.append((String) sstate.getKey());
                    name = name.toString();
                    getRpmTimerLocked(name).update(((PowerStateElement) sstate.getValue()).mTimeMs * 1000, ((PowerStateElement) sstate.getValue()).mCount);
                }
            }
        }
    }

    public void updateRailStatsLocked() {
        if (this.mRailEnergyDataCallback != null && this.mTmpRailStats.isRailStatsAvailable()) {
            this.mRailEnergyDataCallback.fillRailDataStats(this.mTmpRailStats);
        }
    }

    public void updateKernelWakelocksLocked() {
        KernelWakelockStats wakelockStats = this.mKernelWakelockReader.readKernelWakelockStats(this.mTmpWakelockStats);
        String str = TAG;
        if (wakelockStats == null) {
            Slog.w(str, "Couldn't get kernel wake lock stats");
            return;
        }
        for (Entry<String, KernelWakelockStats.Entry> ent : wakelockStats.entrySet()) {
            String name = (String) ent.getKey();
            KernelWakelockStats.Entry kws = (KernelWakelockStats.Entry) ent.getValue();
            SamplingTimer kwlt = (SamplingTimer) this.mKernelWakelockStats.get(name);
            if (kwlt == null) {
                kwlt = new SamplingTimer(this.mClocks, this.mOnBatteryScreenOffTimeBase);
                this.mKernelWakelockStats.put(name, kwlt);
            }
            kwlt.update(kws.mTotalTime, kws.mCount);
            kwlt.setUpdateVersion(kws.mVersion);
        }
        int numWakelocksSetStale = 0;
        for (Entry<String, SamplingTimer> ent2 : this.mKernelWakelockStats.entrySet()) {
            SamplingTimer st = (SamplingTimer) ent2.getValue();
            if (st.getUpdateVersion() != wakelockStats.kernelWakelockVersion) {
                st.endSample();
                numWakelocksSetStale++;
            }
        }
        if (wakelockStats.isEmpty()) {
            Slog.wtf(str, "All kernel wakelocks had time of zero");
        }
        if (numWakelocksSetStale == this.mKernelWakelockStats.size()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("All kernel wakelocks were set stale. new version=");
            stringBuilder.append(wakelockStats.kernelWakelockVersion);
            Slog.wtf(str, stringBuilder.toString());
        }
    }

    public void updateKernelMemoryBandwidthLocked() {
        this.mKernelMemoryBandwidthStats.updateStats();
        LongSparseLongArray bandwidthEntries = this.mKernelMemoryBandwidthStats.getBandwidthEntries();
        int bandwidthEntryCount = bandwidthEntries.size();
        for (int i = 0; i < bandwidthEntryCount; i++) {
            SamplingTimer timer;
            int indexOfKey = this.mKernelMemoryStats.indexOfKey(bandwidthEntries.keyAt(i));
            int index = indexOfKey;
            if (indexOfKey >= 0) {
                timer = (SamplingTimer) this.mKernelMemoryStats.valueAt(index);
            } else {
                timer = new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase);
                this.mKernelMemoryStats.put(bandwidthEntries.keyAt(i), timer);
            }
            timer.update(bandwidthEntries.valueAt(i), 1);
        }
    }

    public boolean isOnBatteryLocked() {
        return this.mOnBatteryTimeBase.isRunning();
    }

    public boolean isOnBatteryScreenOffLocked() {
        return this.mOnBatteryScreenOffTimeBase.isRunning();
    }

    @GuardedBy({"this"})
    public void updateCpuTimeLocked(boolean onBattery, boolean onBatteryScreenOff) {
        PowerProfile powerProfile = this.mPowerProfile;
        if (powerProfile != null) {
            int i;
            if (this.mCpuFreqs == null) {
                this.mCpuFreqs = this.mCpuUidFreqTimeReader.readFreqs(powerProfile);
            }
            ArrayList<StopwatchTimer> partialTimersToConsider = null;
            if (onBatteryScreenOff) {
                partialTimersToConsider = new ArrayList();
                for (i = this.mPartialTimers.size() - 1; i >= 0; i--) {
                    StopwatchTimer timer = (StopwatchTimer) this.mPartialTimers.get(i);
                    if (!(!timer.mInList || timer.mUid == null || timer.mUid.mUid == 1000)) {
                        partialTimersToConsider.add(timer);
                    }
                }
            }
            markPartialTimersAsEligible();
            SparseLongArray updatedUids = null;
            if (onBattery) {
                this.mUserInfoProvider.refreshUserIds();
                if (!this.mCpuUidFreqTimeReader.perClusterTimesAvailable()) {
                    updatedUids = new SparseLongArray();
                }
                readKernelUidCpuTimesLocked(partialTimersToConsider, updatedUids, onBattery);
                if (updatedUids != null) {
                    updateClusterSpeedTimes(updatedUids, onBattery);
                }
                readKernelUidCpuFreqTimesLocked(partialTimersToConsider, onBattery, onBatteryScreenOff);
                this.mNumAllUidCpuTimeReads += 2;
                if (this.mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME) {
                    readKernelUidCpuActiveTimesLocked(onBattery);
                    readKernelUidCpuClusterTimesLocked(onBattery);
                    this.mNumAllUidCpuTimeReads += 2;
                }
                return;
            }
            this.mCpuUidUserSysTimeReader.readDelta(null);
            this.mCpuUidFreqTimeReader.readDelta(null);
            this.mNumAllUidCpuTimeReads += 2;
            if (this.mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME) {
                this.mCpuUidActiveTimeReader.readDelta(null);
                this.mCpuUidClusterTimeReader.readDelta(null);
                this.mNumAllUidCpuTimeReads += 2;
            }
            for (i = this.mKernelCpuSpeedReaders.length - 1; i >= 0; i--) {
                this.mKernelCpuSpeedReaders[i].readDelta();
            }
        }
    }

    @VisibleForTesting
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

    @VisibleForTesting
    public void updateClusterSpeedTimes(SparseLongArray updatedUids, boolean onBattery) {
        int speed;
        SparseLongArray sparseLongArray = updatedUids;
        long totalCpuClustersTimeMs = 0;
        long[][] clusterSpeedTimesMs = new long[this.mKernelCpuSpeedReaders.length][];
        int cluster = 0;
        while (true) {
            KernelCpuSpeedReader[] kernelCpuSpeedReaderArr = this.mKernelCpuSpeedReaders;
            if (cluster >= kernelCpuSpeedReaderArr.length) {
                break;
            }
            clusterSpeedTimesMs[cluster] = kernelCpuSpeedReaderArr[cluster].readDelta();
            if (clusterSpeedTimesMs[cluster] != null) {
                for (speed = clusterSpeedTimesMs[cluster].length - 1; speed >= 0; speed--) {
                    totalCpuClustersTimeMs += clusterSpeedTimesMs[cluster][speed];
                }
            }
            cluster++;
        }
        long[][] clusterSpeedTimesMs2;
        boolean z;
        if (totalCpuClustersTimeMs != 0) {
            int updatedUidsCount;
            cluster = updatedUids.size();
            speed = 0;
            while (speed < cluster) {
                Uid u = getUidStatsLocked(sparseLongArray.keyAt(speed));
                long appCpuTimeUs = sparseLongArray.valueAt(speed);
                int numClusters = this.mPowerProfile.getNumCpuClusters();
                if (u.mCpuClusterSpeedTimesUs == null || u.mCpuClusterSpeedTimesUs.length != numClusters) {
                    u.mCpuClusterSpeedTimesUs = new LongSamplingCounter[numClusters][];
                }
                int cluster2 = 0;
                while (cluster2 < clusterSpeedTimesMs.length) {
                    int speedsInCluster = clusterSpeedTimesMs[cluster2].length;
                    if (u.mCpuClusterSpeedTimesUs[cluster2] == null || speedsInCluster != u.mCpuClusterSpeedTimesUs[cluster2].length) {
                        u.mCpuClusterSpeedTimesUs[cluster2] = new LongSamplingCounter[speedsInCluster];
                    }
                    LongSamplingCounter[] cpuSpeeds = u.mCpuClusterSpeedTimesUs[cluster2];
                    int speed2 = 0;
                    while (speed2 < speedsInCluster) {
                        if (cpuSpeeds[speed2] == null) {
                            cpuSpeeds[speed2] = new LongSamplingCounter(this.mOnBatteryTimeBase);
                        }
                        clusterSpeedTimesMs2 = clusterSpeedTimesMs;
                        updatedUidsCount = cluster;
                        cpuSpeeds[speed2].addCountLocked((clusterSpeedTimesMs[cluster2][speed2] * appCpuTimeUs) / totalCpuClustersTimeMs, onBattery);
                        speed2++;
                        sparseLongArray = updatedUids;
                        clusterSpeedTimesMs = clusterSpeedTimesMs2;
                        cluster = updatedUidsCount;
                    }
                    z = onBattery;
                    clusterSpeedTimesMs2 = clusterSpeedTimesMs;
                    updatedUidsCount = cluster;
                    cluster2++;
                    sparseLongArray = updatedUids;
                }
                z = onBattery;
                clusterSpeedTimesMs2 = clusterSpeedTimesMs;
                updatedUidsCount = cluster;
                speed++;
                sparseLongArray = updatedUids;
            }
            z = onBattery;
            clusterSpeedTimesMs2 = clusterSpeedTimesMs;
            updatedUidsCount = cluster;
            return;
        }
        z = onBattery;
        clusterSpeedTimesMs2 = clusterSpeedTimesMs;
    }

    @VisibleForTesting
    public void readKernelUidCpuTimesLocked(ArrayList<StopwatchTimer> partialTimers, SparseLongArray updatedUids, boolean onBattery) {
        ArrayList arrayList = partialTimers;
        SparseLongArray sparseLongArray = updatedUids;
        boolean z = onBattery;
        this.mTempTotalCpuSystemTimeUs = 0;
        this.mTempTotalCpuUserTimeUs = 0;
        int numWakelocks = arrayList == null ? 0 : partialTimers.size();
        long startTimeMs = this.mClocks.uptimeMillis();
        this.mCpuUidUserSysTimeReader.readDelta(new -$$Lambda$BatteryStatsImpl$7bfIWpn8X2h-hSzLD6dcuK4Ljuw(this, numWakelocks, z, sparseLongArray));
        long elapsedTimeMs = this.mClocks.uptimeMillis() - startTimeMs;
        if (elapsedTimeMs >= 100) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Reading cpu stats took ");
            stringBuilder.append(elapsedTimeMs);
            stringBuilder.append("ms");
            Slog.d(TAG, stringBuilder.toString());
        }
        if (numWakelocks > 0) {
            this.mTempTotalCpuUserTimeUs = (this.mTempTotalCpuUserTimeUs * 50) / 100;
            this.mTempTotalCpuSystemTimeUs = (this.mTempTotalCpuSystemTimeUs * 50) / 100;
            int i = 0;
            while (i < numWakelocks) {
                StopwatchTimer timer = (StopwatchTimer) arrayList.get(i);
                int userTimeUs = (int) (this.mTempTotalCpuUserTimeUs / ((long) (numWakelocks - i)));
                int numWakelocks2 = numWakelocks;
                int systemTimeUs = (int) (this.mTempTotalCpuSystemTimeUs / ((long) (numWakelocks - i)));
                timer.mUid.mUserCpuTime.addCountLocked((long) userTimeUs, z);
                timer.mUid.mSystemCpuTime.addCountLocked((long) systemTimeUs, z);
                if (sparseLongArray != null) {
                    numWakelocks = timer.mUid.getUid();
                    sparseLongArray.put(numWakelocks, (sparseLongArray.get(numWakelocks, 0) + ((long) userTimeUs)) + ((long) systemTimeUs));
                }
                timer.mUid.getProcessStatsLocked("*wakelock*").addCpuTimeLocked(userTimeUs / 1000, systemTimeUs / 1000, z);
                this.mTempTotalCpuUserTimeUs -= (long) userTimeUs;
                this.mTempTotalCpuSystemTimeUs -= (long) systemTimeUs;
                i++;
                ArrayList<StopwatchTimer> arrayList2 = partialTimers;
                sparseLongArray = updatedUids;
                numWakelocks = numWakelocks2;
            }
            return;
        }
    }

    public /* synthetic */ void lambda$readKernelUidCpuTimesLocked$0$BatteryStatsImpl(int numWakelocks, boolean onBattery, SparseLongArray updatedUids, int uid, long[] timesUs) {
        boolean z = onBattery;
        SparseLongArray sparseLongArray = updatedUids;
        long userTimeUs = timesUs[0];
        long systemTimeUs = timesUs[1];
        int uid2 = mapUid(uid);
        boolean isIsolated = Process.isIsolated(uid2);
        String str = TAG;
        StringBuilder stringBuilder;
        if (isIsolated) {
            this.mCpuUidUserSysTimeReader.removeUid(uid2);
            stringBuilder = new StringBuilder();
            stringBuilder.append("Got readings for an isolated uid with no mapping: ");
            stringBuilder.append(uid2);
            Slog.d(str, stringBuilder.toString());
        } else if (this.mUserInfoProvider.exists(UserHandle.getUserId(uid2))) {
            Uid u = getUidStatsLocked(uid2);
            this.mTempTotalCpuUserTimeUs += userTimeUs;
            this.mTempTotalCpuSystemTimeUs += systemTimeUs;
            StringBuilder sb = null;
            if (numWakelocks > 0) {
                userTimeUs = (userTimeUs * 50) / 100;
                systemTimeUs = (50 * systemTimeUs) / 100;
            }
            if (sb != null) {
                sb.append("  adding to uid=");
                sb.append(u.mUid);
                sb.append(": u=");
                TimeUtils.formatDuration(userTimeUs / 1000, sb);
                sb.append(" s=");
                TimeUtils.formatDuration(systemTimeUs / 1000, sb);
                Slog.d(str, sb.toString());
            }
            u.mUserCpuTime.addCountLocked(userTimeUs, z);
            u.mSystemCpuTime.addCountLocked(systemTimeUs, z);
            if (sparseLongArray != null) {
                sparseLongArray.put(u.getUid(), userTimeUs + systemTimeUs);
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Got readings for an invalid user's uid ");
            stringBuilder.append(uid2);
            Slog.d(str, stringBuilder.toString());
            this.mCpuUidUserSysTimeReader.removeUid(uid2);
        }
    }

    @VisibleForTesting
    public void readKernelUidCpuFreqTimesLocked(ArrayList<StopwatchTimer> partialTimers, boolean onBattery, boolean onBatteryScreenOff) {
        ArrayList arrayList = partialTimers;
        boolean perClusterTimesAvailable = this.mCpuUidFreqTimeReader.perClusterTimesAvailable();
        int numWakelocks = arrayList == null ? 0 : partialTimers.size();
        int numClusters = this.mPowerProfile.getNumCpuClusters();
        this.mWakeLockAllocationsUs = null;
        long startTimeMs = this.mClocks.uptimeMillis();
        this.mCpuUidFreqTimeReader.readDelta(new -$$Lambda$BatteryStatsImpl$B-TmZhQb712ePnuJTxvMe7P-YwQ(this, onBattery, onBatteryScreenOff, perClusterTimesAvailable, numClusters, numWakelocks));
        long elapsedTimeMs = this.mClocks.uptimeMillis() - startTimeMs;
        if (elapsedTimeMs >= 100) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Reading cpu freq times took ");
            stringBuilder.append(elapsedTimeMs);
            stringBuilder.append("ms");
            Slog.d(TAG, stringBuilder.toString());
        }
        boolean perClusterTimesAvailable2;
        if (this.mWakeLockAllocationsUs != null) {
            int i = 0;
            while (i < numWakelocks) {
                ArrayList<StopwatchTimer> arrayList2;
                Uid u = ((StopwatchTimer) arrayList.get(i)).mUid;
                if (u.mCpuClusterSpeedTimesUs == null || u.mCpuClusterSpeedTimesUs.length != numClusters) {
                    detachIfNotNull(u.mCpuClusterSpeedTimesUs);
                    u.mCpuClusterSpeedTimesUs = new LongSamplingCounter[numClusters][];
                }
                int cluster = 0;
                while (cluster < numClusters) {
                    int speedsInCluster = this.mPowerProfile.getNumSpeedStepsInCpuCluster(cluster);
                    if (u.mCpuClusterSpeedTimesUs[cluster] == null || u.mCpuClusterSpeedTimesUs[cluster].length != speedsInCluster) {
                        detachIfNotNull(u.mCpuClusterSpeedTimesUs[cluster]);
                        u.mCpuClusterSpeedTimesUs[cluster] = new LongSamplingCounter[speedsInCluster];
                    }
                    LongSamplingCounter[] cpuTimeUs = u.mCpuClusterSpeedTimesUs[cluster];
                    int speed = 0;
                    while (speed < speedsInCluster) {
                        long elapsedTimeMs2;
                        if (cpuTimeUs[speed] == null) {
                            elapsedTimeMs2 = elapsedTimeMs;
                            cpuTimeUs[speed] = new LongSamplingCounter(this.mOnBatteryTimeBase);
                        } else {
                            elapsedTimeMs2 = elapsedTimeMs;
                        }
                        perClusterTimesAvailable2 = perClusterTimesAvailable;
                        elapsedTimeMs = this.mWakeLockAllocationsUs[cluster][speed] / ((long) (numWakelocks - i));
                        cpuTimeUs[speed].addCountLocked(elapsedTimeMs, onBattery);
                        long[] jArr = this.mWakeLockAllocationsUs[cluster];
                        jArr[speed] = jArr[speed] - elapsedTimeMs;
                        speed++;
                        arrayList2 = partialTimers;
                        elapsedTimeMs = elapsedTimeMs2;
                        perClusterTimesAvailable = perClusterTimesAvailable2;
                    }
                    perClusterTimesAvailable2 = perClusterTimesAvailable;
                    perClusterTimesAvailable = onBattery;
                    cluster++;
                    arrayList2 = partialTimers;
                    perClusterTimesAvailable = perClusterTimesAvailable2;
                }
                perClusterTimesAvailable2 = perClusterTimesAvailable;
                perClusterTimesAvailable = onBattery;
                i++;
                arrayList2 = partialTimers;
                perClusterTimesAvailable = perClusterTimesAvailable2;
            }
            perClusterTimesAvailable2 = perClusterTimesAvailable;
            perClusterTimesAvailable = onBattery;
            return;
        }
        perClusterTimesAvailable2 = perClusterTimesAvailable;
        perClusterTimesAvailable = onBattery;
    }

    public /* synthetic */ void lambda$readKernelUidCpuFreqTimesLocked$1$BatteryStatsImpl(boolean onBattery, boolean onBatteryScreenOff, boolean perClusterTimesAvailable, int numClusters, int numWakelocks, int uid, long[] cpuFreqTimeMs) {
        boolean z = onBattery;
        int i = numClusters;
        long[] jArr = cpuFreqTimeMs;
        int uid2 = mapUid(uid);
        boolean isIsolated = Process.isIsolated(uid2);
        String str = TAG;
        StringBuilder stringBuilder;
        if (isIsolated) {
            this.mCpuUidFreqTimeReader.removeUid(uid2);
            stringBuilder = new StringBuilder();
            stringBuilder.append("Got freq readings for an isolated uid with no mapping: ");
            stringBuilder.append(uid2);
            Slog.d(str, stringBuilder.toString());
        } else if (this.mUserInfoProvider.exists(UserHandle.getUserId(uid2))) {
            Uid u = getUidStatsLocked(uid2);
            if (u.mCpuFreqTimeMs == null || u.mCpuFreqTimeMs.getSize() != jArr.length) {
                detachIfNotNull(u.mCpuFreqTimeMs);
                u.mCpuFreqTimeMs = new LongSamplingCounterArray(this.mOnBatteryTimeBase);
            }
            u.mCpuFreqTimeMs.addCountLocked(jArr, z);
            if (u.mScreenOffCpuFreqTimeMs == null || u.mScreenOffCpuFreqTimeMs.getSize() != jArr.length) {
                detachIfNotNull(u.mScreenOffCpuFreqTimeMs);
                u.mScreenOffCpuFreqTimeMs = new LongSamplingCounterArray(this.mOnBatteryScreenOffTimeBase);
            }
            u.mScreenOffCpuFreqTimeMs.addCountLocked(jArr, onBatteryScreenOff);
            if (perClusterTimesAvailable) {
                if (u.mCpuClusterSpeedTimesUs == null || u.mCpuClusterSpeedTimesUs.length != i) {
                    detachIfNotNull(u.mCpuClusterSpeedTimesUs);
                    u.mCpuClusterSpeedTimesUs = new LongSamplingCounter[i][];
                }
                if (numWakelocks > 0 && this.mWakeLockAllocationsUs == null) {
                    this.mWakeLockAllocationsUs = new long[i][];
                }
                int freqIndex = 0;
                int cluster = 0;
                while (cluster < i) {
                    int speedsInCluster = this.mPowerProfile.getNumSpeedStepsInCpuCluster(cluster);
                    if (u.mCpuClusterSpeedTimesUs[cluster] == null || u.mCpuClusterSpeedTimesUs[cluster].length != speedsInCluster) {
                        detachIfNotNull(u.mCpuClusterSpeedTimesUs[cluster]);
                        u.mCpuClusterSpeedTimesUs[cluster] = new LongSamplingCounter[speedsInCluster];
                    }
                    if (numWakelocks > 0) {
                        long[][] jArr2 = this.mWakeLockAllocationsUs;
                        if (jArr2[cluster] == null) {
                            jArr2[cluster] = new long[speedsInCluster];
                        }
                    }
                    LongSamplingCounter[] cpuTimesUs = u.mCpuClusterSpeedTimesUs[cluster];
                    for (int speed = 0; speed < speedsInCluster; speed++) {
                        long j;
                        if (cpuTimesUs[speed] == null) {
                            cpuTimesUs[speed] = new LongSamplingCounter(this.mOnBatteryTimeBase);
                        }
                        long[][] jArr3 = this.mWakeLockAllocationsUs;
                        if (jArr3 != null) {
                            j = ((jArr[freqIndex] * 1000) * 50) / 100;
                            long[] jArr4 = jArr3[cluster];
                            jArr4[speed] = jArr4[speed] + ((jArr[freqIndex] * 1000) - j);
                        } else {
                            j = jArr[freqIndex] * 1000;
                        }
                        cpuTimesUs[speed].addCountLocked(j, z);
                        freqIndex++;
                    }
                    cluster++;
                }
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Got freq readings for an invalid user's uid ");
            stringBuilder.append(uid2);
            Slog.d(str, stringBuilder.toString());
            this.mCpuUidFreqTimeReader.removeUid(uid2);
        }
    }

    @VisibleForTesting
    public void readKernelUidCpuActiveTimesLocked(boolean onBattery) {
        long startTimeMs = this.mClocks.uptimeMillis();
        this.mCpuUidActiveTimeReader.readDelta(new -$$Lambda$BatteryStatsImpl$_l2oiaRDRhjCXI_PwXPsAhrgegI(this, onBattery));
        long elapsedTimeMs = this.mClocks.uptimeMillis() - startTimeMs;
        if (elapsedTimeMs >= 100) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Reading cpu active times took ");
            stringBuilder.append(elapsedTimeMs);
            stringBuilder.append("ms");
            Slog.d(TAG, stringBuilder.toString());
        }
    }

    public /* synthetic */ void lambda$readKernelUidCpuActiveTimesLocked$2$BatteryStatsImpl(boolean onBattery, int uid, Long cpuActiveTimesMs) {
        uid = mapUid(uid);
        boolean isIsolated = Process.isIsolated(uid);
        String str = TAG;
        StringBuilder stringBuilder;
        if (isIsolated) {
            this.mCpuUidActiveTimeReader.removeUid(uid);
            stringBuilder = new StringBuilder();
            stringBuilder.append("Got active times for an isolated uid with no mapping: ");
            stringBuilder.append(uid);
            Slog.w(str, stringBuilder.toString());
        } else if (this.mUserInfoProvider.exists(UserHandle.getUserId(uid))) {
            getUidStatsLocked(uid).mCpuActiveTimeMs.addCountLocked(cpuActiveTimesMs.longValue(), onBattery);
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Got active times for an invalid user's uid ");
            stringBuilder.append(uid);
            Slog.w(str, stringBuilder.toString());
            this.mCpuUidActiveTimeReader.removeUid(uid);
        }
    }

    @VisibleForTesting
    public void readKernelUidCpuClusterTimesLocked(boolean onBattery) {
        long startTimeMs = this.mClocks.uptimeMillis();
        this.mCpuUidClusterTimeReader.readDelta(new -$$Lambda$BatteryStatsImpl$Xvt9xdVPtevMWGIjcbxXf0_mr_c(this, onBattery));
        long elapsedTimeMs = this.mClocks.uptimeMillis() - startTimeMs;
        if (elapsedTimeMs >= 100) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Reading cpu cluster times took ");
            stringBuilder.append(elapsedTimeMs);
            stringBuilder.append("ms");
            Slog.d(TAG, stringBuilder.toString());
        }
    }

    public /* synthetic */ void lambda$readKernelUidCpuClusterTimesLocked$3$BatteryStatsImpl(boolean onBattery, int uid, long[] cpuClusterTimesMs) {
        uid = mapUid(uid);
        boolean isIsolated = Process.isIsolated(uid);
        String str = TAG;
        StringBuilder stringBuilder;
        if (isIsolated) {
            this.mCpuUidClusterTimeReader.removeUid(uid);
            stringBuilder = new StringBuilder();
            stringBuilder.append("Got cluster times for an isolated uid with no mapping: ");
            stringBuilder.append(uid);
            Slog.w(str, stringBuilder.toString());
        } else if (this.mUserInfoProvider.exists(UserHandle.getUserId(uid))) {
            getUidStatsLocked(uid).mCpuClusterTimesMs.addCountLocked(cpuClusterTimesMs, onBattery);
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Got cluster times for an invalid user's uid ");
            stringBuilder.append(uid);
            Slog.w(str, stringBuilder.toString());
            this.mCpuUidClusterTimeReader.removeUid(uid);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean setChargingLocked(boolean charging) {
        this.mHandler.removeCallbacks(this.mDeferSetCharging);
        if (this.mCharging == charging) {
            return false;
        }
        this.mCharging = charging;
        HistoryItem historyItem;
        if (charging) {
            historyItem = this.mHistoryCur;
            historyItem.states2 |= 16777216;
        } else {
            historyItem = this.mHistoryCur;
            historyItem.states2 &= -16777217;
        }
        this.mHandler.sendEmptyMessage(3);
        return true;
    }

    /* Access modifiers changed, original: protected */
    @GuardedBy({"this"})
    public void setOnBatteryLocked(long mSecRealtime, long mSecUptime, boolean onBattery, int oldStatus, int level, int chargeUAh) {
        boolean z = onBattery;
        int i = oldStatus;
        int i2 = level;
        int i3 = chargeUAh;
        boolean doWrite = false;
        Message m = this.mHandler.obtainMessage(2);
        m.arg1 = z;
        this.mHandler.sendMessage(m);
        long uptime = mSecUptime * 1000;
        long realtime = mSecRealtime * 1000;
        int screenState = this.mScreenState;
        HistoryItem historyItem;
        if (z) {
            boolean reset;
            int i4;
            if (this.mNoAutoReset) {
                reset = false;
            } else if (i == 5 || i2 >= 90 || (this.mDischargeCurrentLevel < 20 && i2 >= 80)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Resetting battery stats: level=");
                stringBuilder.append(i2);
                stringBuilder.append(" status=");
                stringBuilder.append(i);
                stringBuilder.append(" dischargeLevel=");
                stringBuilder.append(this.mDischargeCurrentLevel);
                stringBuilder.append(" lowAmount=");
                stringBuilder.append(getLowDischargeAmountSinceCharge());
                stringBuilder.append(" highAmount=");
                stringBuilder.append(getHighDischargeAmountSinceCharge());
                Slog.i(TAG, stringBuilder.toString());
                if (getLowDischargeAmountSinceCharge() >= 20) {
                    long startTime = SystemClock.uptimeMillis();
                    final Parcel parcel = Parcel.obtain();
                    writeSummaryToParcel(parcel, true);
                    reset = false;
                    final long initialTime = SystemClock.uptimeMillis() - startTime;
                    BackgroundThread.getHandler().post(new Runnable() {
                        public void run() {
                            synchronized (BatteryStatsImpl.this.mCheckinFile) {
                                Parcel parcel;
                                long startTime2 = SystemClock.uptimeMillis();
                                FileOutputStream stream = null;
                                try {
                                    stream = BatteryStatsImpl.this.mCheckinFile.startWrite();
                                    stream.write(parcel.marshall());
                                    stream.flush();
                                    BatteryStatsImpl.this.mCheckinFile.finishWrite(stream);
                                    EventLogTags.writeCommitSysConfigFile("batterystats-checkin", (initialTime + SystemClock.uptimeMillis()) - startTime2);
                                    parcel = parcel;
                                } catch (IOException e) {
                                    try {
                                        Slog.w("BatteryStats", "Error writing checkin battery statistics", e);
                                        BatteryStatsImpl.this.mCheckinFile.failWrite(stream);
                                        parcel = parcel;
                                    } catch (Throwable th) {
                                        parcel.recycle();
                                    }
                                }
                                parcel.recycle();
                            }
                        }
                    });
                } else {
                    reset = false;
                }
                resetAllStatsLocked();
                if (i3 > 0 && i2 > 0) {
                    this.mEstimatedBatteryCapacity = (int) (((double) (i3 / 1000)) / (((double) i2) / 100.0d));
                }
                this.mDischargeStartLevel = i2;
                this.mDischargeStepTracker.init();
                doWrite = true;
                reset = true;
            } else {
                reset = false;
            }
            if (this.mCharging) {
                setChargingLocked(false);
            }
            this.mLastChargingStateLevel = i2;
            this.mOnBatteryInternal = true;
            this.mOnBattery = true;
            this.mLastDischargeStepLevel = i2;
            this.mMinDischargeStepLevel = i2;
            this.mDischargeStepTracker.clearTime();
            this.mDailyDischargeStepTracker.clearTime();
            this.mInitStepMode = this.mCurStepMode;
            this.mModStepMode = 0;
            pullPendingStateUpdatesLocked();
            historyItem = this.mHistoryCur;
            historyItem.batteryLevel = (byte) i2;
            historyItem.states &= -524289;
            if (reset) {
                this.mRecordingHistory = true;
                i4 = 0;
                startRecordingHistory(mSecRealtime, mSecUptime, reset);
            } else {
                i4 = 0;
            }
            addHistoryRecordLocked(mSecRealtime, mSecUptime);
            this.mDischargeUnplugLevel = i2;
            this.mDischargeCurrentLevel = i2;
            if (isScreenOn(screenState)) {
                this.mDischargeScreenOnUnplugLevel = i2;
                this.mDischargeScreenDozeUnplugLevel = i4;
                this.mDischargeScreenOffUnplugLevel = i4;
            } else if (isScreenDoze(screenState)) {
                this.mDischargeScreenOnUnplugLevel = i4;
                this.mDischargeScreenDozeUnplugLevel = i2;
                this.mDischargeScreenOffUnplugLevel = i4;
            } else {
                this.mDischargeScreenOnUnplugLevel = i4;
                this.mDischargeScreenDozeUnplugLevel = i4;
                this.mDischargeScreenOffUnplugLevel = i2;
            }
            this.mDischargeAmountScreenOn = i4;
            this.mDischargeAmountScreenDoze = i4;
            this.mDischargeAmountScreenOff = i4;
            i4 = screenState;
            updateTimeBasesLocked(true, screenState, uptime, realtime);
            int i5 = i4;
        } else {
            int screenState2 = screenState;
            this.mLastChargingStateLevel = i2;
            this.mOnBatteryInternal = false;
            this.mOnBattery = false;
            pullPendingStateUpdatesLocked();
            historyItem = this.mHistoryCur;
            historyItem.batteryLevel = (byte) i2;
            historyItem.states |= 524288;
            addHistoryRecordLocked(mSecRealtime, mSecUptime);
            this.mDischargePlugLevel = i2;
            this.mDischargeCurrentLevel = i2;
            int i6 = this.mDischargeUnplugLevel;
            if (i2 < i6) {
                this.mLowDischargeAmountSinceCharge += (i6 - i2) - 1;
                this.mHighDischargeAmountSinceCharge += i6 - i2;
            }
            updateDischargeScreenLevelsLocked(screenState2, screenState2);
            updateTimeBasesLocked(false, screenState2, uptime, realtime);
            this.mChargeStepTracker.init();
            this.mLastChargeStepLevel = i2;
            this.mMaxChargeStepLevel = i2;
            this.mInitStepMode = this.mCurStepMode;
            this.mModStepMode = 0;
        }
        if ((doWrite || this.mLastWriteTime + 60000 < mSecRealtime) && this.mStatsFile != null && this.mBatteryStatsHistory.getActiveFile() != null) {
            writeAsyncLocked();
        }
    }

    private void startRecordingHistory(long elapsedRealtimeMs, long uptimeMs, boolean reset) {
        this.mRecordingHistory = true;
        this.mHistoryCur.currentTime = System.currentTimeMillis();
        addHistoryBufferLocked(elapsedRealtimeMs, reset ? (byte) 7 : (byte) 5, this.mHistoryCur);
        this.mHistoryCur.currentTime = 0;
        if (reset) {
            initActiveHistoryEventsLocked(elapsedRealtimeMs, uptimeMs);
        }
    }

    private void recordCurrentTimeChangeLocked(long currentTime, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mRecordingHistory) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.currentTime = currentTime;
            addHistoryBufferLocked(elapsedRealtimeMs, (byte) 5, historyItem);
            this.mHistoryCur.currentTime = 0;
        }
    }

    private void recordShutdownLocked(long elapsedRealtimeMs, long uptimeMs) {
        if (this.mRecordingHistory) {
            this.mHistoryCur.currentTime = System.currentTimeMillis();
            addHistoryBufferLocked(elapsedRealtimeMs, (byte) 8, this.mHistoryCur);
            this.mHistoryCur.currentTime = 0;
        }
    }

    private void scheduleSyncExternalStatsLocked(String reason, int updateFlags) {
        ExternalStatsSync externalStatsSync = this.mExternalSync;
        if (externalStatsSync != null) {
            externalStatsSync.scheduleSync(reason, updateFlags);
        }
    }

    @GuardedBy({"this"})
    public void setBatteryStateLocked(int status, int health, int plugType, int level, int temp, int volt, int chargeUAh, int chargeFullUAh) {
        HistoryItem historyItem;
        boolean z;
        long elapsedRealtime;
        long uptime;
        boolean onBattery;
        int i;
        int i2;
        boolean z2 = status;
        byte b = health;
        byte b2 = plugType;
        byte b3 = level;
        int i3 = volt;
        int i4 = chargeUAh;
        int i5 = chargeFullUAh;
        int temp2 = Math.max(0, temp);
        reportChangesToStatsLog(this.mHaveBatteryLevel ? this.mHistoryCur : null, z2, b2, b3);
        boolean onBattery2 = isOnBattery(b2, z2);
        long uptime2 = this.mClocks.uptimeMillis();
        long elapsedRealtime2 = this.mClocks.elapsedRealtime();
        if (!this.mHaveBatteryLevel) {
            this.mHaveBatteryLevel = true;
            if (onBattery2 == this.mOnBattery) {
                if (onBattery2) {
                    historyItem = this.mHistoryCur;
                    historyItem.states &= -524289;
                } else {
                    historyItem = this.mHistoryCur;
                    historyItem.states |= 524288;
                }
            }
            historyItem = this.mHistoryCur;
            historyItem.states2 |= 16777216;
            historyItem = this.mHistoryCur;
            historyItem.batteryStatus = (byte) z2;
            historyItem.batteryLevel = (byte) b3;
            historyItem.batteryChargeUAh = i4;
            this.mLastDischargeStepLevel = b3;
            this.mLastChargeStepLevel = b3;
            this.mMinDischargeStepLevel = b3;
            this.mMaxChargeStepLevel = b3;
            this.mLastChargingStateLevel = b3;
        } else if (!(this.mCurrentBatteryLevel == b3 && this.mOnBattery == onBattery2)) {
            z = b3 >= (byte) 100 && onBattery2;
            recordDailyStatsIfNeededLocked(z);
        }
        int oldStatus = this.mHistoryCur.batteryStatus;
        if (onBattery2) {
            this.mDischargeCurrentLevel = b3;
            if (this.mRecordingHistory) {
                elapsedRealtime = elapsedRealtime2;
                uptime = uptime2;
                onBattery = onBattery2;
            } else {
                this.mRecordingHistory = true;
                elapsedRealtime = elapsedRealtime2;
                uptime = uptime2;
                onBattery = onBattery2;
                startRecordingHistory(elapsedRealtime2, uptime2, true);
            }
        } else {
            elapsedRealtime = elapsedRealtime2;
            uptime = uptime2;
            onBattery = onBattery2;
            if (!(b3 >= (byte) 96 || z2 || this.mRecordingHistory)) {
                this.mRecordingHistory = true;
                startRecordingHistory(elapsedRealtime, uptime, true);
            }
        }
        this.mCurrentBatteryLevel = b3;
        if (this.mDischargePlugLevel < 0) {
            this.mDischargePlugLevel = b3;
        }
        onBattery2 = onBattery;
        long j;
        boolean z3;
        if (onBattery2 != this.mOnBattery) {
            historyItem = this.mHistoryCur;
            historyItem.batteryLevel = (byte) b3;
            historyItem.batteryStatus = (byte) z2;
            historyItem.batteryHealth = (byte) b;
            historyItem.batteryPlugType = (byte) b2;
            historyItem.batteryTemperature = (short) temp2;
            historyItem.batteryVoltage = (char) i3;
            if (i4 < historyItem.batteryChargeUAh) {
                long chargeDiff = (long) (this.mHistoryCur.batteryChargeUAh - i4);
                this.mDischargeCounter.addCountLocked(chargeDiff);
                this.mDischargeScreenOffCounter.addCountLocked(chargeDiff);
                if (isScreenDoze(this.mScreenState)) {
                    this.mDischargeScreenDozeCounter.addCountLocked(chargeDiff);
                }
                i = this.mDeviceIdleMode;
                if (i == 1) {
                    this.mDischargeLightDozeCounter.addCountLocked(chargeDiff);
                } else if (i == 2) {
                    this.mDischargeDeepDozeCounter.addCountLocked(chargeDiff);
                }
            }
            this.mHistoryCur.batteryChargeUAh = i4;
            onBattery = onBattery2;
            i4 = temp2;
            setOnBatteryLocked(elapsedRealtime, uptime, onBattery2, oldStatus, level, chargeUAh);
            temp = i4;
            j = elapsedRealtime;
            z3 = true;
        } else {
            onBattery = onBattery2;
            i4 = temp2;
            int i6 = oldStatus;
            z = false;
            if (this.mHistoryCur.batteryLevel != b3) {
                this.mHistoryCur.batteryLevel = (byte) b3;
                z = true;
                this.mExternalSync.scheduleSyncDueToBatteryLevelChange(this.mConstants.BATTERY_LEVEL_COLLECTION_DELAY_MS);
            }
            if (this.mHistoryCur.batteryStatus != z2) {
                this.mHistoryCur.batteryStatus = (byte) z2;
                z = true;
            }
            if (this.mHistoryCur.batteryHealth != b) {
                this.mHistoryCur.batteryHealth = (byte) b;
                z = true;
            }
            if (this.mHistoryCur.batteryPlugType != b2) {
                this.mHistoryCur.batteryPlugType = (byte) b2;
                z = true;
            }
            if (i4 >= this.mHistoryCur.batteryTemperature + 10 || i4 <= this.mHistoryCur.batteryTemperature - 10) {
                this.mHistoryCur.batteryTemperature = (short) i4;
                z = true;
            }
            if (i3 > this.mHistoryCur.batteryVoltage + 20 || i3 < this.mHistoryCur.batteryVoltage - 20) {
                this.mHistoryCur.batteryVoltage = (char) i3;
                z = true;
            }
            int i7 = chargeUAh;
            int temp3 = i4;
            if (i7 >= this.mHistoryCur.batteryChargeUAh + 10 || i7 <= this.mHistoryCur.batteryChargeUAh - 10) {
                if (i7 < this.mHistoryCur.batteryChargeUAh) {
                    j = (long) (this.mHistoryCur.batteryChargeUAh - i7);
                    this.mDischargeCounter.addCountLocked(j);
                    this.mDischargeScreenOffCounter.addCountLocked(j);
                    if (isScreenDoze(this.mScreenState)) {
                        this.mDischargeScreenDozeCounter.addCountLocked(j);
                    }
                    int i8 = this.mDeviceIdleMode;
                    z3 = true;
                    if (i8 == 1) {
                        this.mDischargeLightDozeCounter.addCountLocked(j);
                    } else if (i8 == 2) {
                        this.mDischargeDeepDozeCounter.addCountLocked(j);
                    }
                } else {
                    z3 = true;
                }
                this.mHistoryCur.batteryChargeUAh = i7;
                z = true;
            } else {
                z3 = true;
            }
            elapsedRealtime2 = ((((long) this.mInitStepMode) << 48) | (((long) this.mModStepMode) << 56)) | (((long) (b3 & 255)) << 40);
            byte b4;
            long j2;
            long j3;
            if (onBattery) {
                z |= setChargingLocked(false);
                b4 = this.mLastDischargeStepLevel;
                if (b4 != b3 && this.mMinDischargeStepLevel > b3) {
                    j2 = elapsedRealtime2;
                    j3 = elapsedRealtime;
                    this.mDischargeStepTracker.addLevelSteps(b4 - b3, j2, j3);
                    this.mDailyDischargeStepTracker.addLevelSteps(this.mLastDischargeStepLevel - b3, j2, j3);
                    this.mLastDischargeStepLevel = b3;
                    this.mMinDischargeStepLevel = b3;
                    this.mInitStepMode = this.mCurStepMode;
                    this.mModStepMode = 0;
                }
                temp = temp3;
            } else {
                if (b3 >= (byte) 90) {
                    z |= setChargingLocked(z3);
                    temp = temp3;
                } else if (this.mCharging) {
                    if (this.mLastChargeStepLevel > b3) {
                        z |= setChargingLocked(false);
                    }
                } else {
                    b4 = this.mLastChargeStepLevel;
                    if (b4 >= b3) {
                        if (b4 > b3) {
                            this.mHandler.removeCallbacks(this.mDeferSetCharging);
                        }
                    } else if (this.mHandler.hasCallbacks(this.mDeferSetCharging)) {
                    } else {
                        temp = temp3;
                        this.mHandler.postDelayed(this.mDeferSetCharging, (long) this.mConstants.BATTERY_CHARGED_DELAY_MS);
                    }
                }
                byte b5 = this.mLastChargeStepLevel;
                if (b5 != b3 && this.mMaxChargeStepLevel < b3) {
                    j2 = elapsedRealtime2;
                    j3 = elapsedRealtime;
                    this.mChargeStepTracker.addLevelSteps(b3 - b5, j2, j3);
                    this.mDailyChargeStepTracker.addLevelSteps(b3 - this.mLastChargeStepLevel, j2, j3);
                    this.mMaxChargeStepLevel = b3;
                    this.mInitStepMode = this.mCurStepMode;
                    this.mModStepMode = 0;
                }
                this.mLastChargeStepLevel = b3;
            }
            if (z) {
                addHistoryRecordLocked(elapsedRealtime, uptime);
            } else {
                uptime2 = uptime;
            }
        }
        if (!onBattery && (z2 || z2 == z3)) {
            this.mRecordingHistory = false;
        }
        i = this.mMinLearnedBatteryCapacity;
        if (i == -1) {
            i2 = chargeFullUAh;
            this.mMinLearnedBatteryCapacity = i2;
        } else {
            i2 = chargeFullUAh;
            this.mMinLearnedBatteryCapacity = Math.min(i, i2);
        }
        this.mMaxLearnedBatteryCapacity = Math.max(this.mMaxLearnedBatteryCapacity, i2);
    }

    public static boolean isOnBattery(int plugType, int status) {
        return plugType == 0 && status != 1;
    }

    private void reportChangesToStatsLog(HistoryItem recentPast, int status, int plugType, int level) {
        if (recentPast == null || recentPast.batteryStatus != status) {
            StatsLogInternal.write(31, status);
        }
        if (recentPast == null || recentPast.batteryPlugType != plugType) {
            StatsLogInternal.write(32, plugType);
        }
        if (recentPast == null || recentPast.batteryLevel != level) {
            StatsLogInternal.write(30, level);
        }
    }

    @UnsupportedAppUsage
    public long getAwakeTimeBattery() {
        return getBatteryUptimeLocked();
    }

    @UnsupportedAppUsage
    public long getAwakeTimePlugged() {
        return (this.mClocks.uptimeMillis() * 1000) - getAwakeTimeBattery();
    }

    public long computeUptime(long curTime, int which) {
        return this.mUptime + (curTime - this.mUptimeStart);
    }

    public long computeRealtime(long curTime, int which) {
        return this.mRealtime + (curTime - this.mRealtimeStart);
    }

    @UnsupportedAppUsage
    public long computeBatteryUptime(long curTime, int which) {
        return this.mOnBatteryTimeBase.computeUptime(curTime, which);
    }

    @UnsupportedAppUsage
    public long computeBatteryRealtime(long curTime, int which) {
        return this.mOnBatteryTimeBase.computeRealtime(curTime, which);
    }

    public long computeBatteryScreenOffUptime(long curTime, int which) {
        return this.mOnBatteryScreenOffTimeBase.computeUptime(curTime, which);
    }

    public long computeBatteryScreenOffRealtime(long curTime, int which) {
        return this.mOnBatteryScreenOffTimeBase.computeRealtime(curTime, which);
    }

    private long computeTimePerLevel(long[] steps, int numSteps) {
        if (numSteps <= 0) {
            return -1;
        }
        long total = 0;
        for (int i = 0; i < numSteps; i++) {
            total += steps[i] & BatteryStats.STEP_LEVEL_TIME_MASK;
        }
        return total / ((long) numSteps);
    }

    @UnsupportedAppUsage
    public long computeBatteryTimeRemaining(long curTime) {
        if (!this.mOnBattery || this.mDischargeStepTracker.mNumStepDurations < 1) {
            return -1;
        }
        long msPerLevel = this.mDischargeStepTracker.computeTimePerLevel();
        if (msPerLevel <= 0) {
            return -1;
        }
        return (((long) this.mCurrentBatteryLevel) * msPerLevel) * 1000;
    }

    public LevelStepTracker getDischargeLevelStepTracker() {
        return this.mDischargeStepTracker;
    }

    public LevelStepTracker getDailyDischargeLevelStepTracker() {
        return this.mDailyDischargeStepTracker;
    }

    public long computeChargeTimeRemaining(long curTime) {
        if (this.mOnBattery || this.mChargeStepTracker.mNumStepDurations < 1) {
            return -1;
        }
        long msPerLevel = this.mChargeStepTracker.computeTimePerLevel();
        if (msPerLevel <= 0) {
            return -1;
        }
        return (((long) (100 - this.mCurrentBatteryLevel)) * msPerLevel) * 1000;
    }

    public CellularBatteryStats getCellularBatteryStats() {
        long monitoredRailChargeConsumedMaMs;
        CellularBatteryStats s = new CellularBatteryStats();
        int which = 0;
        long rawRealTime = SystemClock.elapsedRealtime() * 1000;
        ControllerActivityCounter counter = getModemControllerActivity();
        long sleepTimeMs = counter.getSleepTimeCounter().getCountLocked(0);
        long idleTimeMs = counter.getIdleTimeCounter().getCountLocked(0);
        long rxTimeMs = counter.getRxTimeCounter().getCountLocked(0);
        long energyConsumedMaMs = counter.getPowerCounter().getCountLocked(0);
        long monitoredRailChargeConsumedMaMs2 = counter.getMonitoredRailChargeConsumedMaMs().getCountLocked(0);
        long[] timeInRatMs = new long[22];
        int i = 0;
        while (true) {
            int which2 = which;
            if (i >= timeInRatMs.length) {
                break;
            }
            timeInRatMs[i] = getPhoneDataConnectionTime(i, rawRealTime, 0) / 1000;
            i++;
            which = which2;
        }
        long[] timeInRxSignalStrengthLevelMs = new long[5];
        which = 0;
        while (true) {
            monitoredRailChargeConsumedMaMs = monitoredRailChargeConsumedMaMs2;
            if (which >= timeInRxSignalStrengthLevelMs.length) {
                break;
            }
            timeInRxSignalStrengthLevelMs[which] = getPhoneSignalStrengthTime(which, rawRealTime, 0) / 1000;
            which++;
            monitoredRailChargeConsumedMaMs2 = monitoredRailChargeConsumedMaMs;
        }
        long[] txTimeMs = new long[Math.min(5, counter.getTxTimeCounters().length)];
        int i2 = 0;
        long totalTxTimeMs = 0;
        while (i2 < txTimeMs.length) {
            ControllerActivityCounter counter2 = counter;
            txTimeMs[i2] = counter.getTxTimeCounters()[i2].getCountLocked(null);
            totalTxTimeMs += txTimeMs[i2];
            i2++;
            counter = counter2;
        }
        s.setLoggingDurationMs(computeBatteryRealtime(rawRealTime, 0) / 1000);
        s.setKernelActiveTimeMs(getMobileRadioActiveTime(rawRealTime, 0) / 1000);
        s.setNumPacketsTx(getNetworkActivityPackets(1, 0));
        s.setNumBytesTx(getNetworkActivityBytes(1, 0));
        s.setNumPacketsRx(getNetworkActivityPackets(0, 0));
        s.setNumBytesRx(getNetworkActivityBytes(0, 0));
        s.setSleepTimeMs(sleepTimeMs);
        s.setIdleTimeMs(idleTimeMs);
        s.setRxTimeMs(rxTimeMs);
        s.setEnergyConsumedMaMs(energyConsumedMaMs);
        s.setTimeInRatMs(timeInRatMs);
        s.setTimeInRxSignalStrengthLevelMs(timeInRxSignalStrengthLevelMs);
        s.setTxTimeMs(txTimeMs);
        s.setMonitoredRailChargeConsumedMaMs(monitoredRailChargeConsumedMaMs);
        return s;
    }

    public WifiBatteryStats getWifiBatteryStats() {
        long[] timeInStateMs;
        long rawRealTime;
        WifiBatteryStats s = new WifiBatteryStats();
        int which = 0;
        long rawRealTime2 = SystemClock.elapsedRealtime() * 1000;
        ControllerActivityCounter counter = getWifiControllerActivity();
        long idleTimeMs = counter.getIdleTimeCounter().getCountLocked(0);
        long scanTimeMs = counter.getScanTimeCounter().getCountLocked(0);
        long rxTimeMs = counter.getRxTimeCounter().getCountLocked(0);
        long txTimeMs = counter.getTxTimeCounters()[0].getCountLocked(0);
        long scanTimeMs2 = scanTimeMs;
        scanTimeMs = computeBatteryRealtime(SystemClock.elapsedRealtime() * 1000, 0) / 1000;
        long idleTimeMs2 = idleTimeMs;
        long sleepTimeMs = scanTimeMs - ((idleTimeMs + rxTimeMs) + txTimeMs);
        long energyConsumedMaMs = counter.getPowerCounter().getCountLocked(0);
        long monitoredRailChargeConsumedMaMs = counter.getMonitoredRailChargeConsumedMaMs().getCountLocked(0);
        int i = 0;
        long numAppScanRequest = 0;
        long sleepTimeMs2 = sleepTimeMs;
        while (i < this.mUidStats.size()) {
            numAppScanRequest += (long) ((Uid) this.mUidStats.valueAt(i)).mWifiScanTimer.getCountLocked(0);
            i++;
            which = which;
            rawRealTime2 = rawRealTime2;
        }
        long rawRealTime3 = rawRealTime2;
        which = 8;
        long[] timeInStateMs2 = new long[8];
        int i2 = 0;
        while (i2 < which) {
            timeInStateMs = timeInStateMs2;
            rawRealTime = rawRealTime3;
            timeInStateMs[i2] = getWifiStateTime(i2, rawRealTime, 0) / 1000;
            i2++;
            rawRealTime3 = rawRealTime;
            timeInStateMs2 = timeInStateMs;
            which = 8;
        }
        timeInStateMs = timeInStateMs2;
        rawRealTime = rawRealTime3;
        i2 = 13;
        long[] timeInSupplStateMs = new long[13];
        int i3 = 0;
        while (i3 < i2) {
            timeInSupplStateMs[i3] = getWifiSupplStateTime(i3, rawRealTime, 0) / 1000;
            i3++;
            i2 = 13;
        }
        long[] timeSignalStrengthTimeMs = new long[5];
        long monitoredRailChargeConsumedMaMs2 = monitoredRailChargeConsumedMaMs;
        for (int i4 = 0; i4 < 5; i4++) {
            timeSignalStrengthTimeMs[i4] = getWifiSignalStrengthTime(i4, rawRealTime, 0) / 1000;
        }
        long[] timeInStateMs3 = timeInStateMs;
        s.setLoggingDurationMs(computeBatteryRealtime(rawRealTime, 0) / 1000);
        s.setKernelActiveTimeMs(getWifiActiveTime(rawRealTime, 0) / 1000);
        s.setNumPacketsTx(getNetworkActivityPackets(3, 0));
        s.setNumBytesTx(getNetworkActivityBytes(3, 0));
        s.setNumPacketsRx(getNetworkActivityPackets(2, 0));
        s.setNumBytesRx(getNetworkActivityBytes(2, 0));
        s.setSleepTimeMs(sleepTimeMs2);
        s.setIdleTimeMs(idleTimeMs2);
        s.setRxTimeMs(rxTimeMs);
        s.setTxTimeMs(txTimeMs);
        s.setScanTimeMs(scanTimeMs2);
        s.setEnergyConsumedMaMs(energyConsumedMaMs);
        s.setNumAppScanRequest(numAppScanRequest);
        s.setTimeInStateMs(timeInStateMs3);
        s.setTimeInSupplicantStateMs(timeInSupplStateMs);
        s.setTimeInRxSignalStrengthLevelMs(timeSignalStrengthTimeMs);
        s.setMonitoredRailChargeConsumedMaMs(monitoredRailChargeConsumedMaMs2);
        return s;
    }

    public GpsBatteryStats getGpsBatteryStats() {
        GpsBatteryStats s = new GpsBatteryStats();
        long rawRealTime = SystemClock.elapsedRealtime() * 1000;
        s.setLoggingDurationMs(computeBatteryRealtime(rawRealTime, 0) / 1000);
        s.setEnergyConsumedMaMs(getGpsBatteryDrainMaMs());
        long[] time = new long[2];
        for (int i = 0; i < time.length; i++) {
            time[i] = getGpsSignalQualityTime(i, rawRealTime, 0) / 1000;
        }
        s.setTimeInGpsSignalQualityLevel(time);
        return s;
    }

    public LevelStepTracker getChargeLevelStepTracker() {
        return this.mChargeStepTracker;
    }

    public LevelStepTracker getDailyChargeLevelStepTracker() {
        return this.mDailyChargeStepTracker;
    }

    public ArrayList<PackageChange> getDailyPackageChanges() {
        return this.mDailyPackageChanges;
    }

    /* Access modifiers changed, original: protected */
    public long getBatteryUptimeLocked() {
        return this.mOnBatteryTimeBase.getUptime(this.mClocks.uptimeMillis() * 1000);
    }

    public long getBatteryUptime(long curTime) {
        return this.mOnBatteryTimeBase.getUptime(curTime);
    }

    @UnsupportedAppUsage
    public long getBatteryRealtime(long curTime) {
        return this.mOnBatteryTimeBase.getRealtime(curTime);
    }

    @UnsupportedAppUsage
    public int getDischargeStartLevel() {
        int dischargeStartLevelLocked;
        synchronized (this) {
            dischargeStartLevelLocked = getDischargeStartLevelLocked();
        }
        return dischargeStartLevelLocked;
    }

    public int getDischargeStartLevelLocked() {
        return this.mDischargeUnplugLevel;
    }

    @UnsupportedAppUsage
    public int getDischargeCurrentLevel() {
        int dischargeCurrentLevelLocked;
        synchronized (this) {
            dischargeCurrentLevelLocked = getDischargeCurrentLevelLocked();
        }
        return dischargeCurrentLevelLocked;
    }

    public int getDischargeCurrentLevelLocked() {
        return this.mDischargeCurrentLevel;
    }

    public int getLowDischargeAmountSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mLowDischargeAmountSinceCharge;
            if (this.mOnBattery && this.mDischargeCurrentLevel < this.mDischargeUnplugLevel) {
                val += (this.mDischargeUnplugLevel - this.mDischargeCurrentLevel) - 1;
            }
        }
        return val;
    }

    public int getHighDischargeAmountSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mHighDischargeAmountSinceCharge;
            if (this.mOnBattery && this.mDischargeCurrentLevel < this.mDischargeUnplugLevel) {
                val += this.mDischargeUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    @UnsupportedAppUsage
    public int getDischargeAmount(int which) {
        int dischargeAmount;
        if (which == 0) {
            dischargeAmount = getHighDischargeAmountSinceCharge();
        } else {
            dischargeAmount = getDischargeStartLevel() - getDischargeCurrentLevel();
        }
        if (dischargeAmount < 0) {
            return 0;
        }
        return dischargeAmount;
    }

    @UnsupportedAppUsage
    public int getDischargeAmountScreenOn() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenOn;
            if (this.mOnBattery && isScreenOn(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenOnUnplugLevel) {
                val += this.mDischargeScreenOnUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    public int getDischargeAmountScreenOnSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenOnSinceCharge;
            if (this.mOnBattery && isScreenOn(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenOnUnplugLevel) {
                val += this.mDischargeScreenOnUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    @UnsupportedAppUsage
    public int getDischargeAmountScreenOff() {
        int dischargeAmountScreenDoze;
        synchronized (this) {
            int val = this.mDischargeAmountScreenOff;
            if (this.mOnBattery && isScreenOff(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenOffUnplugLevel) {
                val += this.mDischargeScreenOffUnplugLevel - this.mDischargeCurrentLevel;
            }
            dischargeAmountScreenDoze = getDischargeAmountScreenDoze() + val;
        }
        return dischargeAmountScreenDoze;
    }

    public int getDischargeAmountScreenOffSinceCharge() {
        int dischargeAmountScreenDozeSinceCharge;
        synchronized (this) {
            int val = this.mDischargeAmountScreenOffSinceCharge;
            if (this.mOnBattery && isScreenOff(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenOffUnplugLevel) {
                val += this.mDischargeScreenOffUnplugLevel - this.mDischargeCurrentLevel;
            }
            dischargeAmountScreenDozeSinceCharge = getDischargeAmountScreenDozeSinceCharge() + val;
        }
        return dischargeAmountScreenDozeSinceCharge;
    }

    public int getDischargeAmountScreenDoze() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenDoze;
            if (this.mOnBattery && isScreenDoze(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenDozeUnplugLevel) {
                val += this.mDischargeScreenDozeUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    public int getDischargeAmountScreenDozeSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenDozeSinceCharge;
            if (this.mOnBattery && isScreenDoze(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenDozeUnplugLevel) {
                val += this.mDischargeScreenDozeUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    @UnsupportedAppUsage
    public Uid getUidStatsLocked(int uid) {
        Uid u = (Uid) this.mUidStats.get(uid);
        if (u != null) {
            return u;
        }
        u = new Uid(this, uid);
        this.mUidStats.put(uid, u);
        return u;
    }

    public Uid getAvailableUidStatsLocked(int uid) {
        return (Uid) this.mUidStats.get(uid);
    }

    public void onCleanupUserLocked(int userId) {
        int firstUidForUser = UserHandle.getUid(userId, 0);
        this.mPendingRemovedUids.add(new UidToRemove(firstUidForUser, UserHandle.getUid(userId, Process.LAST_ISOLATED_UID), this.mClocks.elapsedRealtime()));
    }

    public void onUserRemovedLocked(int userId) {
        int firstUidForUser = UserHandle.getUid(userId, 0);
        int lastUidForUser = UserHandle.getUid(userId, Process.LAST_ISOLATED_UID);
        this.mUidStats.put(firstUidForUser, null);
        this.mUidStats.put(lastUidForUser, null);
        int firstIndex = this.mUidStats.indexOfKey(firstUidForUser);
        int lastIndex = this.mUidStats.indexOfKey(lastUidForUser);
        for (int i = firstIndex; i <= lastIndex; i++) {
            Uid uid = (Uid) this.mUidStats.valueAt(i);
            if (uid != null) {
                uid.detachFromTimeBase();
            }
        }
        this.mUidStats.removeAtRange(firstIndex, (lastIndex - firstIndex) + 1);
    }

    @UnsupportedAppUsage
    public void removeUidStatsLocked(int uid) {
        Uid u = (Uid) this.mUidStats.get(uid);
        if (u != null) {
            u.detachFromTimeBase();
        }
        this.mUidStats.remove(uid);
        this.mPendingRemovedUids.add(new UidToRemove(this, uid, this.mClocks.elapsedRealtime()));
    }

    @UnsupportedAppUsage
    public Proc getProcessStatsLocked(int uid, String name) {
        return getUidStatsLocked(mapUid(uid)).getProcessStatsLocked(name);
    }

    @UnsupportedAppUsage
    public Pkg getPackageStatsLocked(int uid, String pkg) {
        return getUidStatsLocked(mapUid(uid)).getPackageStatsLocked(pkg);
    }

    @UnsupportedAppUsage
    public Serv getServiceStatsLocked(int uid, String pkg, String name) {
        return getUidStatsLocked(mapUid(uid)).getServiceStatsLocked(pkg, name);
    }

    public void shutdownLocked() {
        recordShutdownLocked(this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis());
        writeSyncLocked();
        this.mShuttingDown = true;
    }

    public boolean trackPerProcStateCpuTimes() {
        return this.mConstants.TRACK_CPU_TIMES_BY_PROC_STATE && this.mPerProcStateCpuTimesAvailable;
    }

    public void systemServicesReady(Context context) {
        this.mConstants.startObserving(context.getContentResolver());
        registerUsbStateReceiver(context);
    }

    public long getExternalStatsCollectionRateLimitMs() {
        long j;
        synchronized (this) {
            j = this.mConstants.EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS;
        }
        return j;
    }

    @GuardedBy({"this"})
    public void dumpConstantsLocked(PrintWriter pw) {
        this.mConstants.dumpLocked(pw);
    }

    @GuardedBy({"this"})
    public void dumpCpuStatsLocked(PrintWriter pw) {
        String str;
        String str2;
        int u;
        Uid uid;
        long[] times;
        int size = this.mUidStats.size();
        pw.println("Per UID CPU user & system time in ms:");
        int i = 0;
        while (true) {
            str = ": ";
            str2 = "  ";
            if (i >= size) {
                break;
            }
            u = this.mUidStats.keyAt(i);
            uid = (Uid) this.mUidStats.get(u);
            pw.print(str2);
            pw.print(u);
            pw.print(str);
            pw.print(uid.getUserCpuTimeUs(0) / 1000);
            pw.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            pw.println(uid.getSystemCpuTimeUs(0) / 1000);
            i++;
        }
        pw.println("Per UID CPU active time in ms:");
        for (i = 0; i < size; i++) {
            u = this.mUidStats.keyAt(i);
            uid = (Uid) this.mUidStats.get(u);
            if (uid.getCpuActiveTime() > 0) {
                pw.print(str2);
                pw.print(u);
                pw.print(str);
                pw.println(uid.getCpuActiveTime());
            }
        }
        pw.println("Per UID CPU cluster time in ms:");
        for (i = 0; i < size; i++) {
            u = this.mUidStats.keyAt(i);
            times = ((Uid) this.mUidStats.get(u)).getCpuClusterTimes();
            if (times != null) {
                pw.print(str2);
                pw.print(u);
                pw.print(str);
                pw.println(Arrays.toString(times));
            }
        }
        pw.println("Per UID CPU frequency time in ms:");
        for (i = 0; i < size; i++) {
            u = this.mUidStats.keyAt(i);
            times = ((Uid) this.mUidStats.get(u)).getCpuFreqTimes(0);
            if (times != null) {
                pw.print(str2);
                pw.print(u);
                pw.print(str);
                pw.println(Arrays.toString(times));
            }
        }
    }

    public void writeAsyncLocked() {
        writeStatsLocked(false);
        writeHistoryLocked(false);
    }

    public void writeSyncLocked() {
        writeStatsLocked(true);
        writeHistoryLocked(true);
    }

    /* Access modifiers changed, original: 0000 */
    public void writeStatsLocked(boolean sync) {
        if (this.mStatsFile == null) {
            Slog.w(TAG, "writeStatsLocked: no file associated with this instance");
        } else if (!this.mShuttingDown) {
            Parcel p = Parcel.obtain();
            long start = SystemClock.uptimeMillis();
            writeSummaryToParcel(p, false);
            this.mLastWriteTime = this.mClocks.elapsedRealtime();
            writeParcelToFileLocked(p, this.mStatsFile, sync);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void writeHistoryLocked(boolean sync) {
        if (this.mBatteryStatsHistory.getActiveFile() == null) {
            Slog.w(TAG, "writeHistoryLocked: no history file associated with this instance");
        } else if (!this.mShuttingDown) {
            Parcel p = Parcel.obtain();
            long start = SystemClock.uptimeMillis();
            writeHistoryBuffer(p, true, true);
            writeParcelToFileLocked(p, this.mBatteryStatsHistory.getActiveFile(), sync);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void writeParcelToFileLocked(final Parcel p, final AtomicFile file, boolean sync) {
        if (sync) {
            commitPendingDataToDisk(p, file);
        } else {
            BackgroundThread.getHandler().post(new Runnable() {
                public void run() {
                    BatteryStatsImpl.this.commitPendingDataToDisk(p, file);
                }
            });
        }
    }

    private void commitPendingDataToDisk(Parcel p, AtomicFile file) {
        this.mWriteLock.lock();
        FileOutputStream fos = null;
        try {
            long startTime = SystemClock.uptimeMillis();
            fos = file.startWrite();
            fos.write(p.marshall());
            fos.flush();
            file.finishWrite(fos);
            EventLogTags.writeCommitSysConfigFile(BatteryStats.SERVICE_NAME, SystemClock.uptimeMillis() - startTime);
        } catch (IOException e) {
            Slog.w(TAG, "Error writing battery statistics", e);
            file.failWrite(fos);
        } catch (Throwable th) {
            p.recycle();
            this.mWriteLock.unlock();
        }
        p.recycle();
        this.mWriteLock.unlock();
    }

    @UnsupportedAppUsage
    public void readLocked() {
        if (this.mDailyFile != null) {
            readDailyStatsLocked();
        }
        AtomicFile atomicFile = this.mStatsFile;
        String str = TAG;
        if (atomicFile == null) {
            Slog.w(str, "readLocked: no file associated with this instance");
        } else if (this.mBatteryStatsHistory.getActiveFile() == null) {
            Slog.w(str, "readLocked: no history file associated with this instance");
        } else {
            this.mUidStats.clear();
            Parcel stats = Parcel.obtain();
            try {
                long start = SystemClock.uptimeMillis();
                byte[] raw = this.mStatsFile.readFully();
                stats.unmarshall(raw, 0, raw.length);
                stats.setDataPosition(0);
                readSummaryFromParcel(stats);
            } catch (Exception e) {
                Slog.e(str, "Error reading battery statistics", e);
                resetAllStatsLocked();
            } catch (Throwable th) {
                stats.recycle();
            }
            stats.recycle();
            Parcel history = Parcel.obtain();
            try {
                long start2 = SystemClock.uptimeMillis();
                byte[] raw2 = this.mBatteryStatsHistory.getActiveFile().readFully();
                if (raw2.length > 0) {
                    history.unmarshall(raw2, 0, raw2.length);
                    history.setDataPosition(0);
                    readHistoryBuffer(history, true);
                }
            } catch (Exception e2) {
                Slog.e(str, "Error reading battery history", e2);
                clearHistoryLocked();
                this.mBatteryStatsHistory.resetAllFiles();
            } catch (Throwable th2) {
                history.recycle();
            }
            history.recycle();
            this.mEndPlatformVersion = Build.ID;
            if (this.mHistoryBuffer.dataPosition() > 0 || this.mBatteryStatsHistory.getFilesNumbers().size() > 1) {
                this.mRecordingHistory = true;
                long elapsedRealtime = this.mClocks.elapsedRealtime();
                long uptime = this.mClocks.uptimeMillis();
                addHistoryBufferLocked(elapsedRealtime, (byte) 4, this.mHistoryCur);
                startRecordingHistory(elapsedRealtime, uptime, false);
            }
            recordDailyStatsIfNeededLocked(false);
        }
    }

    public int describeContents() {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public void readHistoryBuffer(Parcel in, boolean andOldHistory) throws ParcelFormatException {
        int version = in.readInt();
        if (version != 186) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("readHistoryBuffer: version got ");
            stringBuilder.append(version);
            stringBuilder.append(", expected ");
            stringBuilder.append(186);
            stringBuilder.append("; erasing old stats");
            Slog.w("BatteryStats", stringBuilder.toString());
            return;
        }
        long historyBaseTime = in.readLong();
        this.mHistoryBuffer.setDataSize(0);
        this.mHistoryBuffer.setDataPosition(0);
        int bufSize = in.readInt();
        int curPos = in.dataPosition();
        StringBuilder stringBuilder2;
        if (bufSize >= this.mConstants.MAX_HISTORY_BUFFER * 100) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("File corrupt: history data buffer too large ");
            stringBuilder2.append(bufSize);
            throw new ParcelFormatException(stringBuilder2.toString());
        } else if ((bufSize & -4) == bufSize) {
            this.mHistoryBuffer.appendFrom(in, curPos, bufSize);
            in.setDataPosition(curPos + bufSize);
            if (andOldHistory) {
                readOldHistory(in);
            }
            this.mHistoryBaseTime = historyBaseTime;
            if (this.mHistoryBaseTime > 0) {
                this.mHistoryBaseTime = (this.mHistoryBaseTime - this.mClocks.elapsedRealtime()) + 1;
            }
        } else {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("File corrupt: history data buffer not aligned ");
            stringBuilder2.append(bufSize);
            throw new ParcelFormatException(stringBuilder2.toString());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void readOldHistory(Parcel in) {
    }

    /* Access modifiers changed, original: 0000 */
    public void writeHistoryBuffer(Parcel out, boolean inclData, boolean andOldHistory) {
        out.writeInt(186);
        out.writeLong(this.mHistoryBaseTime + this.mLastHistoryElapsedRealtime);
        if (inclData) {
            out.writeInt(this.mHistoryBuffer.dataSize());
            Parcel parcel = this.mHistoryBuffer;
            out.appendFrom(parcel, 0, parcel.dataSize());
            if (andOldHistory) {
                writeOldHistory(out);
            }
            return;
        }
        out.writeInt(0);
        out.writeInt(0);
    }

    /* Access modifiers changed, original: 0000 */
    public void writeOldHistory(Parcel out) {
    }

    public void readSummaryFromParcel(Parcel in) throws ParcelFormatException {
        BatteryStatsImpl batteryStatsImpl = this;
        Parcel parcel = in;
        int version = in.readInt();
        if (version != 186) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("readFromParcel: version got ");
            stringBuilder.append(version);
            stringBuilder.append(", expected ");
            stringBuilder.append(186);
            stringBuilder.append("; erasing old stats");
            Slog.w("BatteryStats", stringBuilder.toString());
            return;
        }
        int idx;
        int uid;
        int i;
        boolean inclHistory = in.readBoolean();
        if (inclHistory) {
            batteryStatsImpl.readHistoryBuffer(parcel, true);
            batteryStatsImpl.mBatteryStatsHistory.readFromParcel(parcel);
        }
        batteryStatsImpl.mHistoryTagPool.clear();
        boolean z = false;
        batteryStatsImpl.mNextHistoryTagIdx = 0;
        batteryStatsImpl.mNumHistoryTagChars = 0;
        int numTags = in.readInt();
        int i2 = 0;
        while (i2 < numTags) {
            idx = in.readInt();
            String str = in.readString();
            if (str != null) {
                uid = in.readInt();
                HistoryTag tag = new HistoryTag();
                tag.string = str;
                tag.uid = uid;
                tag.poolIdx = idx;
                batteryStatsImpl.mHistoryTagPool.put(tag, Integer.valueOf(idx));
                if (idx >= batteryStatsImpl.mNextHistoryTagIdx) {
                    batteryStatsImpl.mNextHistoryTagIdx = idx + 1;
                }
                batteryStatsImpl.mNumHistoryTagChars += tag.string.length() + 1;
                i2++;
            } else {
                throw new ParcelFormatException("null history tag string");
            }
        }
        batteryStatsImpl.mStartCount = in.readInt();
        batteryStatsImpl.mUptime = in.readLong();
        batteryStatsImpl.mRealtime = in.readLong();
        batteryStatsImpl.mStartClockTime = in.readLong();
        batteryStatsImpl.mStartPlatformVersion = in.readString();
        batteryStatsImpl.mEndPlatformVersion = in.readString();
        batteryStatsImpl.mOnBatteryTimeBase.readSummaryFromParcel(parcel);
        batteryStatsImpl.mOnBatteryScreenOffTimeBase.readSummaryFromParcel(parcel);
        batteryStatsImpl.mDischargeUnplugLevel = in.readInt();
        batteryStatsImpl.mDischargePlugLevel = in.readInt();
        batteryStatsImpl.mDischargeCurrentLevel = in.readInt();
        batteryStatsImpl.mCurrentBatteryLevel = in.readInt();
        batteryStatsImpl.mEstimatedBatteryCapacity = in.readInt();
        batteryStatsImpl.mMinLearnedBatteryCapacity = in.readInt();
        batteryStatsImpl.mMaxLearnedBatteryCapacity = in.readInt();
        batteryStatsImpl.mLowDischargeAmountSinceCharge = in.readInt();
        batteryStatsImpl.mHighDischargeAmountSinceCharge = in.readInt();
        batteryStatsImpl.mDischargeAmountScreenOnSinceCharge = in.readInt();
        batteryStatsImpl.mDischargeAmountScreenOffSinceCharge = in.readInt();
        batteryStatsImpl.mDischargeAmountScreenDozeSinceCharge = in.readInt();
        batteryStatsImpl.mDischargeStepTracker.readFromParcel(parcel);
        batteryStatsImpl.mChargeStepTracker.readFromParcel(parcel);
        batteryStatsImpl.mDailyDischargeStepTracker.readFromParcel(parcel);
        batteryStatsImpl.mDailyChargeStepTracker.readFromParcel(parcel);
        batteryStatsImpl.mDischargeCounter.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDischargeScreenOffCounter.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDischargeScreenDozeCounter.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDischargeLightDozeCounter.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDischargeDeepDozeCounter.readSummaryFromParcelLocked(parcel);
        i2 = in.readInt();
        if (i2 > 0) {
            batteryStatsImpl.mDailyPackageChanges = new ArrayList(i2);
            while (i2 > 0) {
                i2--;
                PackageChange pc = new PackageChange();
                pc.mPackageName = in.readString();
                pc.mUpdate = in.readInt() != 0;
                pc.mVersionCode = in.readLong();
                batteryStatsImpl.mDailyPackageChanges.add(pc);
            }
        } else {
            batteryStatsImpl.mDailyPackageChanges = null;
        }
        batteryStatsImpl.mDailyStartTime = in.readLong();
        batteryStatsImpl.mNextMinDailyDeadline = in.readLong();
        batteryStatsImpl.mNextMaxDailyDeadline = in.readLong();
        batteryStatsImpl.mStartCount++;
        batteryStatsImpl.mScreenState = 0;
        batteryStatsImpl.mScreenOnTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mScreenDozeTimer.readSummaryFromParcelLocked(parcel);
        for (i = 0; i < 5; i++) {
            batteryStatsImpl.mScreenBrightnessTimer[i].readSummaryFromParcelLocked(parcel);
        }
        batteryStatsImpl.mInteractive = false;
        batteryStatsImpl.mInteractiveTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mPhoneOn = false;
        batteryStatsImpl.mPowerSaveModeEnabledTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mLongestLightIdleTime = in.readLong();
        batteryStatsImpl.mLongestFullIdleTime = in.readLong();
        batteryStatsImpl.mDeviceIdleModeLightTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDeviceIdleModeFullTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDeviceLightIdlingTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDeviceIdlingTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mPhoneOnTimer.readSummaryFromParcelLocked(parcel);
        for (i = 0; i < 5; i++) {
            batteryStatsImpl.mPhoneSignalStrengthsTimer[i].readSummaryFromParcelLocked(parcel);
        }
        batteryStatsImpl.mPhoneSignalScanningTimer.readSummaryFromParcelLocked(parcel);
        for (i = 0; i < 22; i++) {
            batteryStatsImpl.mPhoneDataConnectionsTimer[i].readSummaryFromParcelLocked(parcel);
        }
        for (i = 0; i < 10; i++) {
            batteryStatsImpl.mNetworkByteActivityCounters[i].readSummaryFromParcelLocked(parcel);
            batteryStatsImpl.mNetworkPacketActivityCounters[i].readSummaryFromParcelLocked(parcel);
        }
        batteryStatsImpl.mMobileRadioPowerState = 1;
        batteryStatsImpl.mMobileRadioActiveTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mMobileRadioActivePerAppTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mMobileRadioActiveAdjustedTime.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mMobileRadioActiveUnknownTime.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mMobileRadioActiveUnknownCount.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mWifiMulticastWakelockTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mWifiRadioPowerState = 1;
        batteryStatsImpl.mWifiOn = false;
        batteryStatsImpl.mWifiOnTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mGlobalWifiRunning = false;
        batteryStatsImpl.mGlobalWifiRunningTimer.readSummaryFromParcelLocked(parcel);
        for (i = 0; i < 8; i++) {
            batteryStatsImpl.mWifiStateTimer[i].readSummaryFromParcelLocked(parcel);
        }
        for (i = 0; i < 13; i++) {
            batteryStatsImpl.mWifiSupplStateTimer[i].readSummaryFromParcelLocked(parcel);
        }
        for (i = 0; i < 5; i++) {
            batteryStatsImpl.mWifiSignalStrengthsTimer[i].readSummaryFromParcelLocked(parcel);
        }
        batteryStatsImpl.mWifiActiveTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mWifiActivity.readSummaryFromParcel(parcel);
        for (i = 0; i < 2; i++) {
            batteryStatsImpl.mGpsSignalQualityTimer[i].readSummaryFromParcelLocked(parcel);
        }
        batteryStatsImpl.mBluetoothActivity.readSummaryFromParcel(parcel);
        batteryStatsImpl.mModemActivity.readSummaryFromParcel(parcel);
        batteryStatsImpl.mHasWifiReporting = in.readInt() != 0;
        batteryStatsImpl.mHasBluetoothReporting = in.readInt() != 0;
        batteryStatsImpl.mHasModemReporting = in.readInt() != 0;
        batteryStatsImpl.mNumConnectivityChange = in.readInt();
        batteryStatsImpl.mFlashlightOnNesting = 0;
        batteryStatsImpl.mFlashlightOnTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mCameraOnNesting = 0;
        batteryStatsImpl.mCameraOnTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mBluetoothScanNesting = 0;
        batteryStatsImpl.mBluetoothScanTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mIsCellularTxPowerHigh = false;
        i = in.readInt();
        int NRPMS;
        StringBuilder stringBuilder2;
        if (i <= 10000) {
            int irpm;
            for (irpm = 0; irpm < i; irpm++) {
                if (in.readInt() != 0) {
                    batteryStatsImpl.getRpmTimerLocked(in.readString()).readSummaryFromParcelLocked(parcel);
                }
            }
            irpm = in.readInt();
            int NSORPMS;
            if (irpm <= 10000) {
                int irpm2;
                for (irpm2 = 0; irpm2 < irpm; irpm2++) {
                    if (in.readInt() != 0) {
                        batteryStatsImpl.getScreenOffRpmTimerLocked(in.readString()).readSummaryFromParcelLocked(parcel);
                    }
                }
                int NKW = in.readInt();
                int NKW2;
                if (NKW <= 10000) {
                    for (irpm2 = 0; irpm2 < NKW; irpm2++) {
                        if (in.readInt() != 0) {
                            batteryStatsImpl.getKernelWakelockTimerLocked(in.readString()).readSummaryFromParcelLocked(parcel);
                        }
                    }
                    int NWR = in.readInt();
                    int NWR2;
                    if (NWR <= 10000) {
                        for (irpm2 = 0; irpm2 < NWR; irpm2++) {
                            if (in.readInt() != 0) {
                                batteryStatsImpl.getWakeupReasonTimerLocked(in.readString()).readSummaryFromParcelLocked(parcel);
                            }
                        }
                        idx = in.readInt();
                        irpm2 = 0;
                        while (irpm2 < idx) {
                            if (in.readInt() != 0) {
                                NWR2 = NWR;
                                batteryStatsImpl.getKernelMemoryTimerLocked(in.readLong()).readSummaryFromParcelLocked(parcel);
                            } else {
                                NWR2 = NWR;
                            }
                            irpm2++;
                            NWR = NWR2;
                        }
                        uid = in.readInt();
                        int version2;
                        if (uid <= 10000) {
                            NWR = 0;
                            while (NWR < uid) {
                                int i3;
                                int i4;
                                boolean inclHistory2;
                                int numClusters;
                                int numTags2;
                                int NPKG;
                                int procState;
                                irpm2 = in.readInt();
                                Uid u = new Uid(batteryStatsImpl, irpm2);
                                batteryStatsImpl.mUidStats.put(irpm2, u);
                                u.mOnBatteryBackgroundTimeBase.readSummaryFromParcel(parcel);
                                u.mOnBatteryScreenOffBackgroundTimeBase.readSummaryFromParcel(parcel);
                                u.mWifiRunning = z;
                                if (in.readInt() != 0) {
                                    u.mWifiRunningTimer.readSummaryFromParcelLocked(parcel);
                                }
                                u.mFullWifiLockOut = z;
                                if (in.readInt() != 0) {
                                    u.mFullWifiLockTimer.readSummaryFromParcelLocked(parcel);
                                }
                                u.mWifiScanStarted = z;
                                if (in.readInt() != 0) {
                                    u.mWifiScanTimer.readSummaryFromParcelLocked(parcel);
                                }
                                u.mWifiBatchedScanBinStarted = -1;
                                for (i3 = 0; i3 < 5; i3++) {
                                    if (in.readInt() != 0) {
                                        u.makeWifiBatchedScanBin(i3, null);
                                        u.mWifiBatchedScanTimer[i3].readSummaryFromParcelLocked(parcel);
                                    }
                                }
                                u.mWifiMulticastWakelockCount = 0;
                                if (in.readInt() != 0) {
                                    u.mWifiMulticastTimer.readSummaryFromParcelLocked(parcel);
                                }
                                if (in.readInt() != 0) {
                                    u.createAudioTurnedOnTimerLocked().readSummaryFromParcelLocked(parcel);
                                }
                                if (in.readInt() != 0) {
                                    u.createVideoTurnedOnTimerLocked().readSummaryFromParcelLocked(parcel);
                                }
                                if (in.readInt() != 0) {
                                    u.createFlashlightTurnedOnTimerLocked().readSummaryFromParcelLocked(parcel);
                                }
                                if (in.readInt() != 0) {
                                    u.createCameraTurnedOnTimerLocked().readSummaryFromParcelLocked(parcel);
                                }
                                if (in.readInt() != 0) {
                                    u.createForegroundActivityTimerLocked().readSummaryFromParcelLocked(parcel);
                                }
                                if (in.readInt() != 0) {
                                    u.createForegroundServiceTimerLocked().readSummaryFromParcelLocked(parcel);
                                }
                                if (in.readInt() != 0) {
                                    u.createAggregatedPartialWakelockTimerLocked().readSummaryFromParcelLocked(parcel);
                                }
                                if (in.readInt() != 0) {
                                    u.createBluetoothScanTimerLocked().readSummaryFromParcelLocked(parcel);
                                }
                                if (in.readInt() != 0) {
                                    u.createBluetoothUnoptimizedScanTimerLocked().readSummaryFromParcelLocked(parcel);
                                }
                                if (in.readInt() != 0) {
                                    u.createBluetoothScanResultCounterLocked().readSummaryFromParcelLocked(parcel);
                                }
                                if (in.readInt() != 0) {
                                    u.createBluetoothScanResultBgCounterLocked().readSummaryFromParcelLocked(parcel);
                                }
                                u.mProcessState = 21;
                                for (i3 = 0; i3 < 7; i3++) {
                                    if (in.readInt() != 0) {
                                        u.makeProcessState(i3, null);
                                        u.mProcessStateTimer[i3].readSummaryFromParcelLocked(parcel);
                                    }
                                }
                                if (in.readInt() != 0) {
                                    u.createVibratorOnTimerLocked().readSummaryFromParcelLocked(parcel);
                                }
                                if (in.readInt() != 0) {
                                    if (u.mUserActivityCounters == null) {
                                        u.initUserActivityLocked();
                                    }
                                    for (i3 = 0; i3 < Uid.NUM_USER_ACTIVITY_TYPES; i3++) {
                                        u.mUserActivityCounters[i3].readSummaryFromParcelLocked(parcel);
                                    }
                                }
                                if (in.readInt() != 0) {
                                    if (u.mNetworkByteActivityCounters == null) {
                                        u.initNetworkActivityLocked();
                                    }
                                    for (i4 = 0; i4 < 10; i4++) {
                                        u.mNetworkByteActivityCounters[i4].readSummaryFromParcelLocked(parcel);
                                        u.mNetworkPacketActivityCounters[i4].readSummaryFromParcelLocked(parcel);
                                    }
                                    u.mMobileRadioActiveTime.readSummaryFromParcelLocked(parcel);
                                    u.mMobileRadioActiveCount.readSummaryFromParcelLocked(parcel);
                                }
                                u.mUserCpuTime.readSummaryFromParcelLocked(parcel);
                                u.mSystemCpuTime.readSummaryFromParcelLocked(parcel);
                                if (in.readInt() != 0) {
                                    i4 = in.readInt();
                                    PowerProfile powerProfile = batteryStatsImpl.mPowerProfile;
                                    if (powerProfile == null) {
                                        version2 = version;
                                    } else if (powerProfile.getNumCpuClusters() == i4) {
                                        version2 = version;
                                    } else {
                                        throw new ParcelFormatException("Incompatible cpu cluster arrangement");
                                    }
                                    detachIfNotNull(u.mCpuClusterSpeedTimesUs);
                                    u.mCpuClusterSpeedTimesUs = new LongSamplingCounter[i4][];
                                    version = 0;
                                    while (version < i4) {
                                        if (in.readInt() != 0) {
                                            boolean NSB = in.readInt();
                                            inclHistory2 = inclHistory;
                                            inclHistory = batteryStatsImpl.mPowerProfile;
                                            if (!inclHistory) {
                                                numClusters = i4;
                                                numTags2 = numTags;
                                            } else if (inclHistory.getNumSpeedStepsInCpuCluster(version) == NSB) {
                                                numClusters = i4;
                                                numTags2 = numTags;
                                            } else {
                                                StringBuilder stringBuilder3 = new StringBuilder();
                                                stringBuilder3.append("File corrupt: too many speed bins ");
                                                stringBuilder3.append(NSB);
                                                throw new ParcelFormatException(stringBuilder3.toString());
                                            }
                                            u.mCpuClusterSpeedTimesUs[version] = new LongSamplingCounter[NSB];
                                            inclHistory = false;
                                            while (inclHistory < NSB) {
                                                if (in.readInt() != 0) {
                                                    NPKG = i2;
                                                    u.mCpuClusterSpeedTimesUs[version][inclHistory] = new LongSamplingCounter(batteryStatsImpl.mOnBatteryTimeBase);
                                                    u.mCpuClusterSpeedTimesUs[version][inclHistory].readSummaryFromParcelLocked(parcel);
                                                } else {
                                                    NPKG = i2;
                                                }
                                                inclHistory++;
                                                i2 = NPKG;
                                            }
                                            NPKG = i2;
                                        } else {
                                            inclHistory2 = inclHistory;
                                            numClusters = i4;
                                            numTags2 = numTags;
                                            NPKG = i2;
                                            u.mCpuClusterSpeedTimesUs[version] = null;
                                        }
                                        version++;
                                        inclHistory = inclHistory2;
                                        i4 = numClusters;
                                        numTags = numTags2;
                                        i2 = NPKG;
                                    }
                                    inclHistory2 = inclHistory;
                                    numClusters = i4;
                                    numTags2 = numTags;
                                    NPKG = i2;
                                } else {
                                    version2 = version;
                                    inclHistory2 = inclHistory;
                                    numTags2 = numTags;
                                    NPKG = i2;
                                    detachIfNotNull(u.mCpuClusterSpeedTimesUs);
                                    u.mCpuClusterSpeedTimesUs = 0;
                                }
                                detachIfNotNull(u.mCpuFreqTimeMs);
                                u.mCpuFreqTimeMs = LongSamplingCounterArray.readSummaryFromParcelLocked(parcel, batteryStatsImpl.mOnBatteryTimeBase);
                                detachIfNotNull(u.mScreenOffCpuFreqTimeMs);
                                u.mScreenOffCpuFreqTimeMs = LongSamplingCounterArray.readSummaryFromParcelLocked(parcel, batteryStatsImpl.mOnBatteryScreenOffTimeBase);
                                u.mCpuActiveTimeMs.readSummaryFromParcelLocked(parcel);
                                u.mCpuClusterTimesMs.readSummaryFromParcelLocked(parcel);
                                version = in.readInt();
                                if (version == 7) {
                                    detachIfNotNull(u.mProcStateTimeMs);
                                    u.mProcStateTimeMs = new LongSamplingCounterArray[version];
                                    for (procState = 0; procState < version; procState++) {
                                        u.mProcStateTimeMs[procState] = LongSamplingCounterArray.readSummaryFromParcelLocked(parcel, batteryStatsImpl.mOnBatteryTimeBase);
                                    }
                                } else {
                                    detachIfNotNull(u.mProcStateTimeMs);
                                    u.mProcStateTimeMs = null;
                                }
                                version = in.readInt();
                                if (version == 7) {
                                    detachIfNotNull(u.mProcStateScreenOffTimeMs);
                                    u.mProcStateScreenOffTimeMs = new LongSamplingCounterArray[version];
                                    for (procState = 0; procState < version; procState++) {
                                        u.mProcStateScreenOffTimeMs[procState] = LongSamplingCounterArray.readSummaryFromParcelLocked(parcel, batteryStatsImpl.mOnBatteryScreenOffTimeBase);
                                    }
                                } else {
                                    detachIfNotNull(u.mProcStateScreenOffTimeMs);
                                    u.mProcStateScreenOffTimeMs = null;
                                }
                                if (in.readInt() != 0) {
                                    detachIfNotNull(u.mMobileRadioApWakeupCount);
                                    u.mMobileRadioApWakeupCount = new LongSamplingCounter(batteryStatsImpl.mOnBatteryTimeBase);
                                    u.mMobileRadioApWakeupCount.readSummaryFromParcelLocked(parcel);
                                } else {
                                    detachIfNotNull(u.mMobileRadioApWakeupCount);
                                    u.mMobileRadioApWakeupCount = null;
                                }
                                if (in.readInt() != 0) {
                                    detachIfNotNull(u.mWifiRadioApWakeupCount);
                                    u.mWifiRadioApWakeupCount = new LongSamplingCounter(batteryStatsImpl.mOnBatteryTimeBase);
                                    u.mWifiRadioApWakeupCount.readSummaryFromParcelLocked(parcel);
                                } else {
                                    detachIfNotNull(u.mWifiRadioApWakeupCount);
                                    u.mWifiRadioApWakeupCount = null;
                                }
                                i4 = in.readInt();
                                int length;
                                if (i4 <= MAX_WAKELOCKS_PER_UID + 1) {
                                    for (numTags = 0; numTags < i4; numTags++) {
                                        u.readWakeSummaryFromParcelLocked(in.readString(), parcel);
                                    }
                                    numTags = in.readInt();
                                    if (numTags <= MAX_WAKELOCKS_PER_UID + 1) {
                                        for (i2 = 0; i2 < numTags; i2++) {
                                            u.readSyncSummaryFromParcelLocked(in.readString(), parcel);
                                        }
                                        i2 = in.readInt();
                                        if (i2 <= MAX_WAKELOCKS_PER_UID + 1) {
                                            for (i3 = 0; i3 < i2; i3++) {
                                                u.readJobSummaryFromParcelLocked(in.readString(), parcel);
                                            }
                                            u.readJobCompletionsFromParcelLocked(parcel);
                                            u.mJobsDeferredEventCount.readSummaryFromParcelLocked(parcel);
                                            u.mJobsDeferredCount.readSummaryFromParcelLocked(parcel);
                                            u.mJobsFreshnessTimeMs.readSummaryFromParcelLocked(parcel);
                                            detachIfNotNull(u.mJobsFreshnessBuckets);
                                            procState = 0;
                                            while (procState < JOB_FRESHNESS_BUCKETS.length) {
                                                if (in.readInt() != 0) {
                                                    length = version;
                                                    numClusters = idx;
                                                    u.mJobsFreshnessBuckets[procState] = new Counter(u.mBsi.mOnBatteryTimeBase);
                                                    u.mJobsFreshnessBuckets[procState].readSummaryFromParcelLocked(parcel);
                                                } else {
                                                    length = version;
                                                    numClusters = idx;
                                                }
                                                procState++;
                                                version = length;
                                                idx = numClusters;
                                            }
                                            length = version;
                                            numClusters = idx;
                                            version = in.readInt();
                                            if (version <= 1000) {
                                                idx = 0;
                                                while (idx < version) {
                                                    int uid2;
                                                    i3 = in.readInt();
                                                    if (in.readInt() != 0) {
                                                        uid2 = irpm2;
                                                        u.getSensorTimerLocked(i3, true).readSummaryFromParcelLocked(parcel);
                                                    } else {
                                                        uid2 = irpm2;
                                                    }
                                                    idx++;
                                                    irpm2 = uid2;
                                                }
                                                version = in.readInt();
                                                if (version <= 1000) {
                                                    idx = 0;
                                                    while (idx < version) {
                                                        Proc p = u.getProcessStatsLocked(in.readString());
                                                        NKW2 = NKW;
                                                        p.mUserTime = in.readLong();
                                                        p.mSystemTime = in.readLong();
                                                        p.mForegroundTime = in.readLong();
                                                        p.mStarts = in.readInt();
                                                        p.mNumCrashes = in.readInt();
                                                        p.mNumAnrs = in.readInt();
                                                        p.readExcessivePowerFromParcelLocked(parcel);
                                                        idx++;
                                                        NKW = NKW2;
                                                    }
                                                    NKW2 = NKW;
                                                    version = in.readInt();
                                                    if (version <= 10000) {
                                                        int NS;
                                                        procState = 0;
                                                        while (procState < version) {
                                                            String pkgName = in.readString();
                                                            detachIfNotNull((Pkg) u.mPackageStats.get(pkgName));
                                                            Pkg p2 = u.getPackageStatsLocked(pkgName);
                                                            i3 = in.readInt();
                                                            if (i3 <= 10000) {
                                                                p2.mWakeupAlarms.clear();
                                                                irpm2 = 0;
                                                                while (irpm2 < i3) {
                                                                    NS = numTags;
                                                                    numTags = in.readString();
                                                                    NRPMS = i;
                                                                    NSORPMS = irpm;
                                                                    Counter c = new Counter(batteryStatsImpl.mOnBatteryScreenOffTimeBase);
                                                                    c.readSummaryFromParcelLocked(parcel);
                                                                    p2.mWakeupAlarms.put(numTags, c);
                                                                    irpm2++;
                                                                    numTags = NS;
                                                                    i = NRPMS;
                                                                    irpm = NSORPMS;
                                                                }
                                                                NS = numTags;
                                                                NRPMS = i;
                                                                NSORPMS = irpm;
                                                                numTags = in.readInt();
                                                                if (numTags <= 10000) {
                                                                    irpm = 0;
                                                                    while (irpm < numTags) {
                                                                        Serv s = u.getServiceStatsLocked(pkgName, in.readString());
                                                                        s.mStartTime = in.readLong();
                                                                        s.mStarts = in.readInt();
                                                                        s.mLaunches = in.readInt();
                                                                        irpm++;
                                                                        parcel = in;
                                                                    }
                                                                    procState++;
                                                                    batteryStatsImpl = this;
                                                                    parcel = in;
                                                                    i = NRPMS;
                                                                    irpm = NSORPMS;
                                                                } else {
                                                                    stringBuilder2 = new StringBuilder();
                                                                    stringBuilder2.append("File corrupt: too many services ");
                                                                    stringBuilder2.append(numTags);
                                                                    throw new ParcelFormatException(stringBuilder2.toString());
                                                                }
                                                            }
                                                            NS = numTags;
                                                            stringBuilder2 = new StringBuilder();
                                                            stringBuilder2.append("File corrupt: too many wakeup alarms ");
                                                            stringBuilder2.append(i3);
                                                            throw new ParcelFormatException(stringBuilder2.toString());
                                                        }
                                                        NS = numTags;
                                                        NRPMS = i;
                                                        NSORPMS = irpm;
                                                        NWR++;
                                                        z = false;
                                                        batteryStatsImpl = this;
                                                        parcel = in;
                                                        version = version2;
                                                        inclHistory = inclHistory2;
                                                        idx = numClusters;
                                                        numTags = numTags2;
                                                        i2 = NPKG;
                                                        NKW = NKW2;
                                                    } else {
                                                        stringBuilder2 = new StringBuilder();
                                                        stringBuilder2.append("File corrupt: too many packages ");
                                                        stringBuilder2.append(version);
                                                        throw new ParcelFormatException(stringBuilder2.toString());
                                                    }
                                                }
                                                stringBuilder2 = new StringBuilder();
                                                stringBuilder2.append("File corrupt: too many processes ");
                                                stringBuilder2.append(version);
                                                throw new ParcelFormatException(stringBuilder2.toString());
                                            }
                                            stringBuilder2 = new StringBuilder();
                                            stringBuilder2.append("File corrupt: too many sensors ");
                                            stringBuilder2.append(version);
                                            throw new ParcelFormatException(stringBuilder2.toString());
                                        }
                                        length = version;
                                        stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append("File corrupt: too many job timers ");
                                        stringBuilder2.append(i2);
                                        throw new ParcelFormatException(stringBuilder2.toString());
                                    }
                                    length = version;
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("File corrupt: too many syncs ");
                                    stringBuilder2.append(numTags);
                                    throw new ParcelFormatException(stringBuilder2.toString());
                                }
                                length = version;
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("File corrupt: too many wake locks ");
                                stringBuilder2.append(i4);
                                throw new ParcelFormatException(stringBuilder2.toString());
                            }
                            return;
                        }
                        version2 = version;
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("File corrupt: too many uids ");
                        stringBuilder2.append(uid);
                        throw new ParcelFormatException(stringBuilder2.toString());
                    }
                    NWR2 = NWR;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("File corrupt: too many wakeup reasons ");
                    stringBuilder2.append(NWR2);
                    throw new ParcelFormatException(stringBuilder2.toString());
                }
                NKW2 = NKW;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("File corrupt: too many kernel wake locks ");
                stringBuilder2.append(NKW2);
                throw new ParcelFormatException(stringBuilder2.toString());
            }
            NSORPMS = irpm;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("File corrupt: too many screen-off rpm stats ");
            stringBuilder2.append(NSORPMS);
            throw new ParcelFormatException(stringBuilder2.toString());
        }
        NRPMS = i;
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("File corrupt: too many rpm stats ");
        stringBuilder2.append(NRPMS);
        throw new ParcelFormatException(stringBuilder2.toString());
    }

    public void writeSummaryToParcel(Parcel out, boolean inclHistory) {
        int i;
        int i2;
        Timer rpmt;
        BatteryStatsImpl batteryStatsImpl = this;
        Parcel parcel = out;
        pullPendingStateUpdatesLocked();
        getStartClockTime();
        long NOW_SYS = batteryStatsImpl.mClocks.uptimeMillis() * 1000;
        long NOWREAL_SYS = batteryStatsImpl.mClocks.elapsedRealtime() * 1000;
        parcel.writeInt(186);
        out.writeBoolean(inclHistory);
        int i3 = 1;
        if (inclHistory) {
            batteryStatsImpl.writeHistoryBuffer(parcel, true, true);
            batteryStatsImpl.mBatteryStatsHistory.writeToParcel(parcel);
        }
        parcel.writeInt(batteryStatsImpl.mHistoryTagPool.size());
        for (Entry<HistoryTag, Integer> ent : batteryStatsImpl.mHistoryTagPool.entrySet()) {
            HistoryTag tag = (HistoryTag) ent.getKey();
            parcel.writeInt(((Integer) ent.getValue()).intValue());
            parcel.writeString(tag.string);
            parcel.writeInt(tag.uid);
        }
        parcel.writeInt(batteryStatsImpl.mStartCount);
        int i4 = 0;
        parcel.writeLong(batteryStatsImpl.computeUptime(NOW_SYS, 0));
        parcel.writeLong(batteryStatsImpl.computeRealtime(NOWREAL_SYS, 0));
        parcel.writeLong(batteryStatsImpl.mStartClockTime);
        parcel.writeString(batteryStatsImpl.mStartPlatformVersion);
        parcel.writeString(batteryStatsImpl.mEndPlatformVersion);
        Parcel parcel2 = out;
        long j = NOW_SYS;
        long j2 = NOWREAL_SYS;
        batteryStatsImpl.mOnBatteryTimeBase.writeSummaryToParcel(parcel2, j, j2);
        batteryStatsImpl.mOnBatteryScreenOffTimeBase.writeSummaryToParcel(parcel2, j, j2);
        parcel.writeInt(batteryStatsImpl.mDischargeUnplugLevel);
        parcel.writeInt(batteryStatsImpl.mDischargePlugLevel);
        parcel.writeInt(batteryStatsImpl.mDischargeCurrentLevel);
        parcel.writeInt(batteryStatsImpl.mCurrentBatteryLevel);
        parcel.writeInt(batteryStatsImpl.mEstimatedBatteryCapacity);
        parcel.writeInt(batteryStatsImpl.mMinLearnedBatteryCapacity);
        parcel.writeInt(batteryStatsImpl.mMaxLearnedBatteryCapacity);
        parcel.writeInt(getLowDischargeAmountSinceCharge());
        parcel.writeInt(getHighDischargeAmountSinceCharge());
        parcel.writeInt(getDischargeAmountScreenOnSinceCharge());
        parcel.writeInt(getDischargeAmountScreenOffSinceCharge());
        parcel.writeInt(getDischargeAmountScreenDozeSinceCharge());
        batteryStatsImpl.mDischargeStepTracker.writeToParcel(parcel);
        batteryStatsImpl.mChargeStepTracker.writeToParcel(parcel);
        batteryStatsImpl.mDailyDischargeStepTracker.writeToParcel(parcel);
        batteryStatsImpl.mDailyChargeStepTracker.writeToParcel(parcel);
        batteryStatsImpl.mDischargeCounter.writeSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDischargeScreenOffCounter.writeSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDischargeScreenDozeCounter.writeSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDischargeLightDozeCounter.writeSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDischargeDeepDozeCounter.writeSummaryFromParcelLocked(parcel);
        int NPKG = batteryStatsImpl.mDailyPackageChanges;
        if (NPKG != 0) {
            NPKG = NPKG.size();
            parcel.writeInt(NPKG);
            for (i = 0; i < NPKG; i++) {
                PackageChange pc = (PackageChange) batteryStatsImpl.mDailyPackageChanges.get(i);
                parcel.writeString(pc.mPackageName);
                parcel.writeInt(pc.mUpdate);
                parcel.writeLong(pc.mVersionCode);
            }
        } else {
            parcel.writeInt(0);
        }
        parcel.writeLong(batteryStatsImpl.mDailyStartTime);
        parcel.writeLong(batteryStatsImpl.mNextMinDailyDeadline);
        parcel.writeLong(batteryStatsImpl.mNextMaxDailyDeadline);
        batteryStatsImpl.mScreenOnTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mScreenDozeTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        NPKG = 0;
        while (true) {
            i2 = 5;
            if (NPKG >= 5) {
                break;
            }
            batteryStatsImpl.mScreenBrightnessTimer[NPKG].writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            NPKG++;
        }
        batteryStatsImpl.mInteractiveTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mPowerSaveModeEnabledTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        parcel.writeLong(batteryStatsImpl.mLongestLightIdleTime);
        parcel.writeLong(batteryStatsImpl.mLongestFullIdleTime);
        batteryStatsImpl.mDeviceIdleModeLightTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mDeviceIdleModeFullTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mDeviceLightIdlingTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mDeviceIdlingTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mPhoneOnTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        for (NPKG = 0; NPKG < 5; NPKG++) {
            batteryStatsImpl.mPhoneSignalStrengthsTimer[NPKG].writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        }
        batteryStatsImpl.mPhoneSignalScanningTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        for (NPKG = 0; NPKG < 22; NPKG++) {
            batteryStatsImpl.mPhoneDataConnectionsTimer[NPKG].writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        }
        for (NPKG = 0; NPKG < 10; NPKG++) {
            batteryStatsImpl.mNetworkByteActivityCounters[NPKG].writeSummaryFromParcelLocked(parcel);
            batteryStatsImpl.mNetworkPacketActivityCounters[NPKG].writeSummaryFromParcelLocked(parcel);
        }
        batteryStatsImpl.mMobileRadioActiveTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mMobileRadioActivePerAppTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mMobileRadioActiveAdjustedTime.writeSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mMobileRadioActiveUnknownTime.writeSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mMobileRadioActiveUnknownCount.writeSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mWifiMulticastWakelockTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mWifiOnTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mGlobalWifiRunningTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        for (NPKG = 0; NPKG < 8; NPKG++) {
            batteryStatsImpl.mWifiStateTimer[NPKG].writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        }
        for (NPKG = 0; NPKG < 13; NPKG++) {
            batteryStatsImpl.mWifiSupplStateTimer[NPKG].writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        }
        for (NPKG = 0; NPKG < 5; NPKG++) {
            batteryStatsImpl.mWifiSignalStrengthsTimer[NPKG].writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        }
        batteryStatsImpl.mWifiActiveTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mWifiActivity.writeSummaryToParcel(parcel);
        for (NPKG = 0; NPKG < 2; NPKG++) {
            batteryStatsImpl.mGpsSignalQualityTimer[NPKG].writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        }
        batteryStatsImpl.mBluetoothActivity.writeSummaryToParcel(parcel);
        batteryStatsImpl.mModemActivity.writeSummaryToParcel(parcel);
        parcel.writeInt(batteryStatsImpl.mHasWifiReporting);
        parcel.writeInt(batteryStatsImpl.mHasBluetoothReporting);
        parcel.writeInt(batteryStatsImpl.mHasModemReporting);
        parcel.writeInt(batteryStatsImpl.mNumConnectivityChange);
        batteryStatsImpl.mFlashlightOnTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mCameraOnTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        batteryStatsImpl.mBluetoothScanTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
        parcel.writeInt(batteryStatsImpl.mRpmStats.size());
        for (Entry<String, SamplingTimer> ent2 : batteryStatsImpl.mRpmStats.entrySet()) {
            rpmt = (Timer) ent2.getValue();
            if (rpmt != null) {
                parcel.writeInt(1);
                parcel.writeString((String) ent2.getKey());
                rpmt.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(0);
            }
        }
        parcel.writeInt(batteryStatsImpl.mScreenOffRpmStats.size());
        for (Entry<String, SamplingTimer> ent22 : batteryStatsImpl.mScreenOffRpmStats.entrySet()) {
            rpmt = (Timer) ent22.getValue();
            if (rpmt != null) {
                parcel.writeInt(1);
                parcel.writeString((String) ent22.getKey());
                rpmt.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(0);
            }
        }
        parcel.writeInt(batteryStatsImpl.mKernelWakelockStats.size());
        for (Entry<String, SamplingTimer> ent222 : batteryStatsImpl.mKernelWakelockStats.entrySet()) {
            rpmt = (Timer) ent222.getValue();
            if (rpmt != null) {
                parcel.writeInt(1);
                parcel.writeString((String) ent222.getKey());
                rpmt.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(0);
            }
        }
        parcel.writeInt(batteryStatsImpl.mWakeupReasonStats.size());
        for (Entry<String, SamplingTimer> ent2222 : batteryStatsImpl.mWakeupReasonStats.entrySet()) {
            SamplingTimer timer = (SamplingTimer) ent2222.getValue();
            if (timer != null) {
                parcel.writeInt(1);
                parcel.writeString((String) ent2222.getKey());
                timer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(0);
            }
        }
        parcel.writeInt(batteryStatsImpl.mKernelMemoryStats.size());
        for (NPKG = 0; NPKG < batteryStatsImpl.mKernelMemoryStats.size(); NPKG++) {
            Timer kmt = (Timer) batteryStatsImpl.mKernelMemoryStats.valueAt(NPKG);
            if (kmt != null) {
                parcel.writeInt(1);
                parcel.writeLong(batteryStatsImpl.mKernelMemoryStats.keyAt(NPKG));
                kmt.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(0);
            }
        }
        int NU = batteryStatsImpl.mUidStats.size();
        parcel.writeInt(NU);
        int iu = 0;
        while (iu < NU) {
            int length;
            int i5;
            LongSamplingCounterArray[] longSamplingCounterArrayArr;
            ArrayMap<String, DualTimer> syncStats;
            parcel.writeInt(batteryStatsImpl.mUidStats.keyAt(iu));
            Uid u = (Uid) batteryStatsImpl.mUidStats.valueAt(iu);
            TimeBase timeBase = u.mOnBatteryBackgroundTimeBase;
            parcel2 = out;
            Uid u2 = u;
            j = NOW_SYS;
            int NU2 = NU;
            int iu2 = iu;
            j2 = NOWREAL_SYS;
            timeBase.writeSummaryToParcel(parcel2, j, j2);
            u2.mOnBatteryScreenOffBackgroundTimeBase.writeSummaryToParcel(parcel2, j, j2);
            if (u2.mWifiRunningTimer != null) {
                parcel.writeInt(i3);
                u2.mWifiRunningTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mFullWifiLockTimer != null) {
                parcel.writeInt(i3);
                u2.mFullWifiLockTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mWifiScanTimer != null) {
                parcel.writeInt(i3);
                u2.mWifiScanTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            for (NPKG = 0; NPKG < i2; NPKG++) {
                if (u2.mWifiBatchedScanTimer[NPKG] != null) {
                    parcel.writeInt(i3);
                    u2.mWifiBatchedScanTimer[NPKG].writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
                } else {
                    parcel.writeInt(i4);
                }
            }
            if (u2.mWifiMulticastTimer != null) {
                parcel.writeInt(i3);
                u2.mWifiMulticastTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mAudioTurnedOnTimer != null) {
                parcel.writeInt(i3);
                u2.mAudioTurnedOnTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mVideoTurnedOnTimer != null) {
                parcel.writeInt(i3);
                u2.mVideoTurnedOnTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mFlashlightTurnedOnTimer != null) {
                parcel.writeInt(i3);
                u2.mFlashlightTurnedOnTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mCameraTurnedOnTimer != null) {
                parcel.writeInt(i3);
                u2.mCameraTurnedOnTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mForegroundActivityTimer != null) {
                parcel.writeInt(i3);
                u2.mForegroundActivityTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mForegroundServiceTimer != null) {
                parcel.writeInt(i3);
                u2.mForegroundServiceTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mAggregatedPartialWakelockTimer != null) {
                parcel.writeInt(i3);
                u2.mAggregatedPartialWakelockTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mBluetoothScanTimer != null) {
                parcel.writeInt(i3);
                u2.mBluetoothScanTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mBluetoothUnoptimizedScanTimer != null) {
                parcel.writeInt(i3);
                u2.mBluetoothUnoptimizedScanTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mBluetoothScanResultCounter != null) {
                parcel.writeInt(i3);
                u2.mBluetoothScanResultCounter.writeSummaryFromParcelLocked(parcel);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mBluetoothScanResultBgCounter != null) {
                parcel.writeInt(i3);
                u2.mBluetoothScanResultBgCounter.writeSummaryFromParcelLocked(parcel);
            } else {
                parcel.writeInt(i4);
            }
            for (NPKG = 0; NPKG < 7; NPKG++) {
                if (u2.mProcessStateTimer[NPKG] != null) {
                    parcel.writeInt(i3);
                    u2.mProcessStateTimer[NPKG].writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
                } else {
                    parcel.writeInt(i4);
                }
            }
            if (u2.mVibratorOnTimer != null) {
                parcel.writeInt(i3);
                u2.mVibratorOnTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mUserActivityCounters == null) {
                parcel.writeInt(i4);
            } else {
                parcel.writeInt(i3);
                for (NPKG = 0; NPKG < Uid.NUM_USER_ACTIVITY_TYPES; NPKG++) {
                    u2.mUserActivityCounters[NPKG].writeSummaryFromParcelLocked(parcel);
                }
            }
            if (u2.mNetworkByteActivityCounters == null) {
                parcel.writeInt(i4);
            } else {
                parcel.writeInt(i3);
                for (NPKG = 0; NPKG < 10; NPKG++) {
                    u2.mNetworkByteActivityCounters[NPKG].writeSummaryFromParcelLocked(parcel);
                    u2.mNetworkPacketActivityCounters[NPKG].writeSummaryFromParcelLocked(parcel);
                }
                u2.mMobileRadioActiveTime.writeSummaryFromParcelLocked(parcel);
                u2.mMobileRadioActiveCount.writeSummaryFromParcelLocked(parcel);
            }
            u2.mUserCpuTime.writeSummaryFromParcelLocked(parcel);
            u2.mSystemCpuTime.writeSummaryFromParcelLocked(parcel);
            if (u2.mCpuClusterSpeedTimesUs != null) {
                parcel.writeInt(i3);
                parcel.writeInt(u2.mCpuClusterSpeedTimesUs.length);
                LongSamplingCounter[][] longSamplingCounterArr = u2.mCpuClusterSpeedTimesUs;
                length = longSamplingCounterArr.length;
                for (i5 = i4; i5 < length; i5++) {
                    LongSamplingCounter[] cpuSpeeds = longSamplingCounterArr[i5];
                    if (cpuSpeeds != null) {
                        parcel.writeInt(i3);
                        parcel.writeInt(cpuSpeeds.length);
                        iu = cpuSpeeds.length;
                        for (i = i4; i < iu; i++) {
                            LongSamplingCounter c = cpuSpeeds[i];
                            if (c != null) {
                                parcel.writeInt(i3);
                                c.writeSummaryFromParcelLocked(parcel);
                            } else {
                                parcel.writeInt(i4);
                            }
                        }
                    } else {
                        parcel.writeInt(i4);
                    }
                }
            } else {
                parcel.writeInt(i4);
            }
            LongSamplingCounterArray.writeSummaryToParcelLocked(parcel, u2.mCpuFreqTimeMs);
            LongSamplingCounterArray.writeSummaryToParcelLocked(parcel, u2.mScreenOffCpuFreqTimeMs);
            u2.mCpuActiveTimeMs.writeSummaryFromParcelLocked(parcel);
            u2.mCpuClusterTimesMs.writeSummaryToParcelLocked(parcel);
            if (u2.mProcStateTimeMs != null) {
                parcel.writeInt(u2.mProcStateTimeMs.length);
                longSamplingCounterArrayArr = u2.mProcStateTimeMs;
                i = longSamplingCounterArrayArr.length;
                for (length = i4; length < i; length++) {
                    LongSamplingCounterArray.writeSummaryToParcelLocked(parcel, longSamplingCounterArrayArr[length]);
                }
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mProcStateScreenOffTimeMs != null) {
                parcel.writeInt(u2.mProcStateScreenOffTimeMs.length);
                longSamplingCounterArrayArr = u2.mProcStateScreenOffTimeMs;
                i = longSamplingCounterArrayArr.length;
                for (length = i4; length < i; length++) {
                    LongSamplingCounterArray.writeSummaryToParcelLocked(parcel, longSamplingCounterArrayArr[length]);
                }
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mMobileRadioApWakeupCount != null) {
                parcel.writeInt(i3);
                u2.mMobileRadioApWakeupCount.writeSummaryFromParcelLocked(parcel);
            } else {
                parcel.writeInt(i4);
            }
            if (u2.mWifiRadioApWakeupCount != null) {
                parcel.writeInt(i3);
                u2.mWifiRadioApWakeupCount.writeSummaryFromParcelLocked(parcel);
            } else {
                parcel.writeInt(i4);
            }
            ArrayMap<String, Wakelock> wakeStats = u2.mWakelockStats.getMap();
            i = wakeStats.size();
            parcel.writeInt(i);
            for (length = 0; length < i; length++) {
                parcel.writeString((String) wakeStats.keyAt(length));
                Wakelock wl = (Wakelock) wakeStats.valueAt(length);
                if (wl.mTimerFull != null) {
                    parcel.writeInt(i3);
                    wl.mTimerFull.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
                } else {
                    parcel.writeInt(i4);
                }
                if (wl.mTimerPartial != null) {
                    parcel.writeInt(i3);
                    wl.mTimerPartial.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
                } else {
                    parcel.writeInt(i4);
                }
                if (wl.mTimerWindow != null) {
                    parcel.writeInt(i3);
                    wl.mTimerWindow.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
                } else {
                    parcel.writeInt(i4);
                }
                if (wl.mTimerDraw != null) {
                    parcel.writeInt(i3);
                    wl.mTimerDraw.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
                } else {
                    parcel.writeInt(i4);
                }
            }
            ArrayMap<String, DualTimer> syncStats2 = u2.mSyncStats.getMap();
            i5 = syncStats2.size();
            parcel.writeInt(i5);
            for (NU = 0; NU < i5; NU++) {
                parcel.writeString((String) syncStats2.keyAt(NU));
                ((DualTimer) syncStats2.valueAt(NU)).writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            }
            ArrayMap<String, DualTimer> jobStats = u2.mJobStats.getMap();
            iu = jobStats.size();
            parcel.writeInt(iu);
            for (i2 = 0; i2 < iu; i2++) {
                parcel.writeString((String) jobStats.keyAt(i2));
                ((DualTimer) jobStats.valueAt(i2)).writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
            }
            u2.writeJobCompletionsToParcelLocked(parcel);
            u2.mJobsDeferredEventCount.writeSummaryFromParcelLocked(parcel);
            u2.mJobsDeferredCount.writeSummaryFromParcelLocked(parcel);
            u2.mJobsFreshnessTimeMs.writeSummaryFromParcelLocked(parcel);
            for (i4 = 0; i4 < JOB_FRESHNESS_BUCKETS.length; i4++) {
                if (u2.mJobsFreshnessBuckets[i4] != null) {
                    parcel.writeInt(i3);
                    u2.mJobsFreshnessBuckets[i4].writeSummaryFromParcelLocked(parcel);
                } else {
                    parcel.writeInt(0);
                }
            }
            i4 = u2.mSensorStats.size();
            parcel.writeInt(i4);
            i2 = 0;
            while (i2 < i4) {
                parcel.writeInt(u2.mSensorStats.keyAt(i2));
                Sensor se = (Sensor) u2.mSensorStats.valueAt(i2);
                ArrayMap<String, Wakelock> wakeStats2 = wakeStats;
                if (se.mTimer != null) {
                    parcel.writeInt(1);
                    se.mTimer.writeSummaryFromParcelLocked(parcel, NOWREAL_SYS);
                } else {
                    parcel.writeInt(0);
                }
                i2++;
                wakeStats = wakeStats2;
            }
            i3 = u2.mProcessStats.size();
            parcel.writeInt(i3);
            i2 = 0;
            while (i2 < i3) {
                parcel.writeString((String) u2.mProcessStats.keyAt(i2));
                Proc ps = (Proc) u2.mProcessStats.valueAt(i2);
                int NW = i;
                syncStats = syncStats2;
                parcel.writeLong(ps.mUserTime);
                parcel.writeLong(ps.mSystemTime);
                parcel.writeLong(ps.mForegroundTime);
                parcel.writeInt(ps.mStarts);
                parcel.writeInt(ps.mNumCrashes);
                parcel.writeInt(ps.mNumAnrs);
                ps.writeExcessivePowerToParcelLocked(parcel);
                i2++;
                i = NW;
                syncStats2 = syncStats;
            }
            syncStats = syncStats2;
            NPKG = u2.mPackageStats.size();
            parcel.writeInt(NPKG);
            if (NPKG > 0) {
                Iterator it = u2.mPackageStats.entrySet().iterator();
                while (it.hasNext()) {
                    Iterator it2;
                    int NS;
                    Entry<String, Pkg> ent3 = (Entry) it.next();
                    parcel.writeString((String) ent3.getKey());
                    Pkg ps2 = (Pkg) ent3.getValue();
                    i2 = ps2.mWakeupAlarms.size();
                    parcel.writeInt(i2);
                    int NP = NPKG;
                    NPKG = 0;
                    while (NPKG < i2) {
                        it2 = it;
                        parcel.writeString((String) ps2.mWakeupAlarms.keyAt(NPKG));
                        ((Counter) ps2.mWakeupAlarms.valueAt(NPKG)).writeSummaryFromParcelLocked(parcel);
                        NPKG++;
                        it = it2;
                    }
                    it2 = it;
                    i5 = ps2.mServiceStats.size();
                    parcel.writeInt(i5);
                    NPKG = 0;
                    while (NPKG < i5) {
                        parcel.writeString((String) ps2.mServiceStats.keyAt(NPKG));
                        Serv ss = (Serv) ps2.mServiceStats.valueAt(NPKG);
                        Entry<String, Pkg> ent4 = ent3;
                        NS = i5;
                        parcel.writeLong(ss.getStartTimeToNowLocked(batteryStatsImpl.mOnBatteryTimeBase.getUptime(NOW_SYS)));
                        parcel.writeInt(ss.mStarts);
                        parcel.writeInt(ss.mLaunches);
                        NPKG++;
                        batteryStatsImpl = this;
                        ent3 = ent4;
                        i5 = NS;
                    }
                    NS = i5;
                    batteryStatsImpl = this;
                    it = it2;
                    NPKG = NP;
                }
            }
            iu = iu2 + 1;
            i3 = 1;
            i4 = 0;
            i2 = 5;
            batteryStatsImpl = this;
            NU = NU2;
        }
    }

    public void readFromParcel(Parcel in) {
        readFromParcelLocked(in);
    }

    /* Access modifiers changed, original: 0000 */
    public void readFromParcelLocked(Parcel in) {
        Parcel parcel = in;
        int magic = in.readInt();
        if (magic == MAGIC) {
            int i;
            int i2;
            int irpm;
            int irpm2;
            int ikw;
            int iwr;
            int imt;
            readHistoryBuffer(parcel, false);
            this.mBatteryStatsHistory.readFromParcel(parcel);
            this.mStartCount = in.readInt();
            this.mStartClockTime = in.readLong();
            this.mStartPlatformVersion = in.readString();
            this.mEndPlatformVersion = in.readString();
            this.mUptime = in.readLong();
            this.mUptimeStart = in.readLong();
            this.mRealtime = in.readLong();
            this.mRealtimeStart = in.readLong();
            boolean z = true;
            this.mOnBattery = in.readInt() != 0;
            this.mEstimatedBatteryCapacity = in.readInt();
            this.mMinLearnedBatteryCapacity = in.readInt();
            this.mMaxLearnedBatteryCapacity = in.readInt();
            this.mOnBatteryInternal = false;
            this.mOnBatteryTimeBase.readFromParcel(parcel);
            this.mOnBatteryScreenOffTimeBase.readFromParcel(parcel);
            this.mScreenState = 0;
            Parcel parcel2 = in;
            this.mScreenOnTimer = new StopwatchTimer(this.mClocks, null, -1, null, this.mOnBatteryTimeBase, parcel2);
            this.mScreenDozeTimer = new StopwatchTimer(this.mClocks, null, -1, null, this.mOnBatteryTimeBase, parcel2);
            for (i = 0; i < 5; i++) {
                this.mScreenBrightnessTimer[i] = new StopwatchTimer(this.mClocks, null, -100 - i, null, this.mOnBatteryTimeBase, in);
            }
            this.mInteractive = false;
            parcel2 = in;
            this.mInteractiveTimer = new StopwatchTimer(this.mClocks, null, -10, null, this.mOnBatteryTimeBase, parcel2);
            this.mPhoneOn = false;
            this.mPowerSaveModeEnabledTimer = new StopwatchTimer(this.mClocks, null, -2, null, this.mOnBatteryTimeBase, parcel2);
            this.mLongestLightIdleTime = in.readLong();
            this.mLongestFullIdleTime = in.readLong();
            this.mDeviceIdleModeLightTimer = new StopwatchTimer(this.mClocks, null, -14, null, this.mOnBatteryTimeBase, parcel2);
            this.mDeviceIdleModeFullTimer = new StopwatchTimer(this.mClocks, null, -11, null, this.mOnBatteryTimeBase, parcel2);
            this.mDeviceLightIdlingTimer = new StopwatchTimer(this.mClocks, null, -15, null, this.mOnBatteryTimeBase, parcel2);
            this.mDeviceIdlingTimer = new StopwatchTimer(this.mClocks, null, -12, null, this.mOnBatteryTimeBase, parcel2);
            this.mPhoneOnTimer = new StopwatchTimer(this.mClocks, null, -3, null, this.mOnBatteryTimeBase, parcel2);
            for (i = 0; i < 5; i++) {
                this.mPhoneSignalStrengthsTimer[i] = new StopwatchTimer(this.mClocks, null, -200 - i, null, this.mOnBatteryTimeBase, in);
            }
            this.mPhoneSignalScanningTimer = new StopwatchTimer(this.mClocks, null, -199, null, this.mOnBatteryTimeBase, in);
            for (i = 0; i < 22; i++) {
                this.mPhoneDataConnectionsTimer[i] = new StopwatchTimer(this.mClocks, null, -300 - i, null, this.mOnBatteryTimeBase, in);
            }
            for (i2 = 0; i2 < 10; i2++) {
                this.mNetworkByteActivityCounters[i2] = new LongSamplingCounter(this.mOnBatteryTimeBase, parcel);
                this.mNetworkPacketActivityCounters[i2] = new LongSamplingCounter(this.mOnBatteryTimeBase, parcel);
            }
            this.mMobileRadioPowerState = 1;
            parcel2 = in;
            this.mMobileRadioActiveTimer = new StopwatchTimer(this.mClocks, null, -400, null, this.mOnBatteryTimeBase, parcel2);
            this.mMobileRadioActivePerAppTimer = new StopwatchTimer(this.mClocks, null, -401, null, this.mOnBatteryTimeBase, parcel2);
            this.mMobileRadioActiveAdjustedTime = new LongSamplingCounter(this.mOnBatteryTimeBase, parcel);
            this.mMobileRadioActiveUnknownTime = new LongSamplingCounter(this.mOnBatteryTimeBase, parcel);
            this.mMobileRadioActiveUnknownCount = new LongSamplingCounter(this.mOnBatteryTimeBase, parcel);
            this.mWifiMulticastWakelockTimer = new StopwatchTimer(this.mClocks, null, -4, null, this.mOnBatteryTimeBase, parcel2);
            this.mWifiRadioPowerState = 1;
            this.mWifiOn = false;
            this.mWifiOnTimer = new StopwatchTimer(this.mClocks, null, -4, null, this.mOnBatteryTimeBase, parcel2);
            this.mGlobalWifiRunning = false;
            this.mGlobalWifiRunningTimer = new StopwatchTimer(this.mClocks, null, -5, null, this.mOnBatteryTimeBase, parcel2);
            for (i = 0; i < 8; i++) {
                this.mWifiStateTimer[i] = new StopwatchTimer(this.mClocks, null, -600 - i, null, this.mOnBatteryTimeBase, in);
            }
            for (i = 0; i < 13; i++) {
                this.mWifiSupplStateTimer[i] = new StopwatchTimer(this.mClocks, null, -700 - i, null, this.mOnBatteryTimeBase, in);
            }
            for (i = 0; i < 5; i++) {
                this.mWifiSignalStrengthsTimer[i] = new StopwatchTimer(this.mClocks, null, -800 - i, null, this.mOnBatteryTimeBase, in);
            }
            this.mWifiActiveTimer = new StopwatchTimer(this.mClocks, null, ProcessList.SYSTEM_ADJ, null, this.mOnBatteryTimeBase, in);
            this.mWifiActivity = new ControllerActivityCounterImpl(this.mOnBatteryTimeBase, 1, parcel);
            for (i = 0; i < 2; i++) {
                this.mGpsSignalQualityTimer[i] = new StopwatchTimer(this.mClocks, null, -1000 - i, null, this.mOnBatteryTimeBase, in);
            }
            this.mBluetoothActivity = new ControllerActivityCounterImpl(this.mOnBatteryTimeBase, 1, parcel);
            this.mModemActivity = new ControllerActivityCounterImpl(this.mOnBatteryTimeBase, 5, parcel);
            this.mHasWifiReporting = in.readInt() != 0;
            this.mHasBluetoothReporting = in.readInt() != 0;
            if (in.readInt() == 0) {
                z = false;
            }
            this.mHasModemReporting = z;
            this.mNumConnectivityChange = in.readInt();
            this.mAudioOnNesting = 0;
            this.mAudioOnTimer = new StopwatchTimer(this.mClocks, null, -7, null, this.mOnBatteryTimeBase);
            this.mVideoOnNesting = 0;
            this.mVideoOnTimer = new StopwatchTimer(this.mClocks, null, -8, null, this.mOnBatteryTimeBase);
            this.mFlashlightOnNesting = 0;
            parcel2 = in;
            this.mFlashlightOnTimer = new StopwatchTimer(this.mClocks, null, -9, null, this.mOnBatteryTimeBase, parcel2);
            this.mCameraOnNesting = 0;
            this.mCameraOnTimer = new StopwatchTimer(this.mClocks, null, -13, null, this.mOnBatteryTimeBase, parcel2);
            this.mBluetoothScanNesting = 0;
            this.mBluetoothScanTimer = new StopwatchTimer(this.mClocks, null, -14, null, this.mOnBatteryTimeBase, parcel2);
            this.mIsCellularTxPowerHigh = false;
            this.mDischargeUnplugLevel = in.readInt();
            this.mDischargePlugLevel = in.readInt();
            this.mDischargeCurrentLevel = in.readInt();
            this.mCurrentBatteryLevel = in.readInt();
            this.mLowDischargeAmountSinceCharge = in.readInt();
            this.mHighDischargeAmountSinceCharge = in.readInt();
            this.mDischargeAmountScreenOn = in.readInt();
            this.mDischargeAmountScreenOnSinceCharge = in.readInt();
            this.mDischargeAmountScreenOff = in.readInt();
            this.mDischargeAmountScreenOffSinceCharge = in.readInt();
            this.mDischargeAmountScreenDoze = in.readInt();
            this.mDischargeAmountScreenDozeSinceCharge = in.readInt();
            this.mDischargeStepTracker.readFromParcel(parcel);
            this.mChargeStepTracker.readFromParcel(parcel);
            this.mDischargeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase, parcel);
            this.mDischargeScreenOffCounter = new LongSamplingCounter(this.mOnBatteryScreenOffTimeBase, parcel);
            this.mDischargeScreenDozeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase, parcel);
            this.mDischargeLightDozeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase, parcel);
            this.mDischargeDeepDozeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase, parcel);
            this.mLastWriteTime = in.readLong();
            this.mRpmStats.clear();
            i2 = in.readInt();
            for (irpm = 0; irpm < i2; irpm++) {
                if (in.readInt() != 0) {
                    this.mRpmStats.put(in.readString(), new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase, parcel));
                }
            }
            this.mScreenOffRpmStats.clear();
            irpm = in.readInt();
            for (irpm2 = 0; irpm2 < irpm; irpm2++) {
                if (in.readInt() != 0) {
                    this.mScreenOffRpmStats.put(in.readString(), new SamplingTimer(this.mClocks, this.mOnBatteryScreenOffTimeBase, parcel));
                }
            }
            this.mKernelWakelockStats.clear();
            irpm2 = in.readInt();
            for (ikw = 0; ikw < irpm2; ikw++) {
                if (in.readInt() != 0) {
                    this.mKernelWakelockStats.put(in.readString(), new SamplingTimer(this.mClocks, this.mOnBatteryScreenOffTimeBase, parcel));
                }
            }
            this.mWakeupReasonStats.clear();
            ikw = in.readInt();
            for (iwr = 0; iwr < ikw; iwr++) {
                if (in.readInt() != 0) {
                    this.mWakeupReasonStats.put(in.readString(), new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase, parcel));
                }
            }
            this.mKernelMemoryStats.clear();
            iwr = in.readInt();
            for (imt = 0; imt < iwr; imt++) {
                if (in.readInt() != 0) {
                    this.mKernelMemoryStats.put(Long.valueOf(in.readLong()).longValue(), new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase, parcel));
                }
            }
            this.mPartialTimers.clear();
            this.mFullTimers.clear();
            this.mWindowTimers.clear();
            this.mWifiRunningTimers.clear();
            this.mFullWifiLockTimers.clear();
            this.mWifiScanTimers.clear();
            this.mWifiBatchedScanTimers.clear();
            this.mWifiMulticastTimers.clear();
            this.mAudioTurnedOnTimers.clear();
            this.mVideoTurnedOnTimers.clear();
            this.mFlashlightTurnedOnTimers.clear();
            this.mCameraTurnedOnTimers.clear();
            imt = in.readInt();
            this.mUidStats.clear();
            for (int i3 = 0; i3 < imt; i3++) {
                int uid = in.readInt();
                Uid u = new Uid(this, uid);
                u.readFromParcelLocked(this.mOnBatteryTimeBase, this.mOnBatteryScreenOffTimeBase, parcel);
                this.mUidStats.append(uid, u);
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad magic number: #");
        stringBuilder.append(Integer.toHexString(magic));
        throw new ParcelFormatException(stringBuilder.toString());
    }

    public void writeToParcel(Parcel out, int flags) {
        writeToParcelLocked(out, true, flags);
    }

    public void writeToParcelWithoutUids(Parcel out, int flags) {
        writeToParcelLocked(out, false, flags);
    }

    /* Access modifiers changed, original: 0000 */
    public void writeToParcelLocked(Parcel out, boolean inclUids, int flags) {
        int i;
        SamplingTimer rpmt;
        Parcel parcel = out;
        pullPendingStateUpdatesLocked();
        getStartClockTime();
        long uSecUptime = this.mClocks.uptimeMillis() * 1000;
        long uSecRealtime = this.mClocks.elapsedRealtime() * 1000;
        long batteryRealtime = this.mOnBatteryTimeBase.getRealtime(uSecRealtime);
        long batteryScreenOffRealtime = this.mOnBatteryScreenOffTimeBase.getRealtime(uSecRealtime);
        parcel.writeInt(MAGIC);
        writeHistoryBuffer(parcel, true, false);
        this.mBatteryStatsHistory.writeToParcel(parcel);
        parcel.writeInt(this.mStartCount);
        parcel.writeLong(this.mStartClockTime);
        parcel.writeString(this.mStartPlatformVersion);
        parcel.writeString(this.mEndPlatformVersion);
        parcel.writeLong(this.mUptime);
        parcel.writeLong(this.mUptimeStart);
        parcel.writeLong(this.mRealtime);
        parcel.writeLong(this.mRealtimeStart);
        parcel.writeInt(this.mOnBattery);
        parcel.writeInt(this.mEstimatedBatteryCapacity);
        parcel.writeInt(this.mMinLearnedBatteryCapacity);
        parcel.writeInt(this.mMaxLearnedBatteryCapacity);
        Parcel parcel2 = out;
        long j = uSecUptime;
        int i2 = 0;
        int i3 = 1;
        long j2 = uSecRealtime;
        this.mOnBatteryTimeBase.writeToParcel(parcel2, j, j2);
        this.mOnBatteryScreenOffTimeBase.writeToParcel(parcel2, j, j2);
        this.mScreenOnTimer.writeToParcel(parcel, uSecRealtime);
        this.mScreenDozeTimer.writeToParcel(parcel, uSecRealtime);
        for (i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i].writeToParcel(parcel, uSecRealtime);
        }
        this.mInteractiveTimer.writeToParcel(parcel, uSecRealtime);
        this.mPowerSaveModeEnabledTimer.writeToParcel(parcel, uSecRealtime);
        parcel.writeLong(this.mLongestLightIdleTime);
        parcel.writeLong(this.mLongestFullIdleTime);
        this.mDeviceIdleModeLightTimer.writeToParcel(parcel, uSecRealtime);
        this.mDeviceIdleModeFullTimer.writeToParcel(parcel, uSecRealtime);
        this.mDeviceLightIdlingTimer.writeToParcel(parcel, uSecRealtime);
        this.mDeviceIdlingTimer.writeToParcel(parcel, uSecRealtime);
        this.mPhoneOnTimer.writeToParcel(parcel, uSecRealtime);
        for (i = 0; i < 5; i++) {
            this.mPhoneSignalStrengthsTimer[i].writeToParcel(parcel, uSecRealtime);
        }
        this.mPhoneSignalScanningTimer.writeToParcel(parcel, uSecRealtime);
        for (i = 0; i < 22; i++) {
            this.mPhoneDataConnectionsTimer[i].writeToParcel(parcel, uSecRealtime);
        }
        for (i = 0; i < 10; i++) {
            this.mNetworkByteActivityCounters[i].writeToParcel(parcel);
            this.mNetworkPacketActivityCounters[i].writeToParcel(parcel);
        }
        this.mMobileRadioActiveTimer.writeToParcel(parcel, uSecRealtime);
        this.mMobileRadioActivePerAppTimer.writeToParcel(parcel, uSecRealtime);
        this.mMobileRadioActiveAdjustedTime.writeToParcel(parcel);
        this.mMobileRadioActiveUnknownTime.writeToParcel(parcel);
        this.mMobileRadioActiveUnknownCount.writeToParcel(parcel);
        this.mWifiMulticastWakelockTimer.writeToParcel(parcel, uSecRealtime);
        this.mWifiOnTimer.writeToParcel(parcel, uSecRealtime);
        this.mGlobalWifiRunningTimer.writeToParcel(parcel, uSecRealtime);
        for (i = 0; i < 8; i++) {
            this.mWifiStateTimer[i].writeToParcel(parcel, uSecRealtime);
        }
        for (i = 0; i < 13; i++) {
            this.mWifiSupplStateTimer[i].writeToParcel(parcel, uSecRealtime);
        }
        for (i = 0; i < 5; i++) {
            this.mWifiSignalStrengthsTimer[i].writeToParcel(parcel, uSecRealtime);
        }
        this.mWifiActiveTimer.writeToParcel(parcel, uSecRealtime);
        this.mWifiActivity.writeToParcel(parcel, i2);
        for (i = 0; i < 2; i++) {
            this.mGpsSignalQualityTimer[i].writeToParcel(parcel, uSecRealtime);
        }
        this.mBluetoothActivity.writeToParcel(parcel, i2);
        this.mModemActivity.writeToParcel(parcel, i2);
        parcel.writeInt(this.mHasWifiReporting);
        parcel.writeInt(this.mHasBluetoothReporting);
        parcel.writeInt(this.mHasModemReporting);
        parcel.writeInt(this.mNumConnectivityChange);
        this.mFlashlightOnTimer.writeToParcel(parcel, uSecRealtime);
        this.mCameraOnTimer.writeToParcel(parcel, uSecRealtime);
        this.mBluetoothScanTimer.writeToParcel(parcel, uSecRealtime);
        parcel.writeInt(this.mDischargeUnplugLevel);
        parcel.writeInt(this.mDischargePlugLevel);
        parcel.writeInt(this.mDischargeCurrentLevel);
        parcel.writeInt(this.mCurrentBatteryLevel);
        parcel.writeInt(this.mLowDischargeAmountSinceCharge);
        parcel.writeInt(this.mHighDischargeAmountSinceCharge);
        parcel.writeInt(this.mDischargeAmountScreenOn);
        parcel.writeInt(this.mDischargeAmountScreenOnSinceCharge);
        parcel.writeInt(this.mDischargeAmountScreenOff);
        parcel.writeInt(this.mDischargeAmountScreenOffSinceCharge);
        parcel.writeInt(this.mDischargeAmountScreenDoze);
        parcel.writeInt(this.mDischargeAmountScreenDozeSinceCharge);
        this.mDischargeStepTracker.writeToParcel(parcel);
        this.mChargeStepTracker.writeToParcel(parcel);
        this.mDischargeCounter.writeToParcel(parcel);
        this.mDischargeScreenOffCounter.writeToParcel(parcel);
        this.mDischargeScreenDozeCounter.writeToParcel(parcel);
        this.mDischargeLightDozeCounter.writeToParcel(parcel);
        this.mDischargeDeepDozeCounter.writeToParcel(parcel);
        parcel.writeLong(this.mLastWriteTime);
        parcel.writeInt(this.mRpmStats.size());
        for (Entry<String, SamplingTimer> ent : this.mRpmStats.entrySet()) {
            rpmt = (SamplingTimer) ent.getValue();
            if (rpmt != null) {
                parcel.writeInt(i3);
                parcel.writeString((String) ent.getKey());
                rpmt.writeToParcel(parcel, uSecRealtime);
            } else {
                parcel.writeInt(i2);
            }
        }
        parcel.writeInt(this.mScreenOffRpmStats.size());
        for (Entry<String, SamplingTimer> ent2 : this.mScreenOffRpmStats.entrySet()) {
            rpmt = (SamplingTimer) ent2.getValue();
            if (rpmt != null) {
                parcel.writeInt(i3);
                parcel.writeString((String) ent2.getKey());
                rpmt.writeToParcel(parcel, uSecRealtime);
            } else {
                parcel.writeInt(i2);
            }
        }
        if (inclUids) {
            parcel.writeInt(this.mKernelWakelockStats.size());
            for (Entry<String, SamplingTimer> ent22 : this.mKernelWakelockStats.entrySet()) {
                rpmt = (SamplingTimer) ent22.getValue();
                if (rpmt != null) {
                    parcel.writeInt(i3);
                    parcel.writeString((String) ent22.getKey());
                    rpmt.writeToParcel(parcel, uSecRealtime);
                } else {
                    parcel.writeInt(i2);
                }
            }
            parcel.writeInt(this.mWakeupReasonStats.size());
            for (Entry<String, SamplingTimer> ent222 : this.mWakeupReasonStats.entrySet()) {
                rpmt = (SamplingTimer) ent222.getValue();
                if (rpmt != null) {
                    parcel.writeInt(i3);
                    parcel.writeString((String) ent222.getKey());
                    rpmt.writeToParcel(parcel, uSecRealtime);
                } else {
                    parcel.writeInt(i2);
                }
            }
        } else {
            parcel.writeInt(i2);
            parcel.writeInt(i2);
        }
        parcel.writeInt(this.mKernelMemoryStats.size());
        for (i = 0; i < this.mKernelMemoryStats.size(); i++) {
            SamplingTimer kmt = (SamplingTimer) this.mKernelMemoryStats.valueAt(i);
            if (kmt != null) {
                parcel.writeInt(i3);
                parcel.writeLong(this.mKernelMemoryStats.keyAt(i));
                kmt.writeToParcel(parcel, uSecRealtime);
            } else {
                parcel.writeInt(i2);
            }
        }
        if (inclUids) {
            i2 = this.mUidStats.size();
            parcel.writeInt(i2);
            for (i3 = 0; i3 < i2; i3++) {
                parcel.writeInt(this.mUidStats.keyAt(i3));
                ((Uid) this.mUidStats.valueAt(i3)).writeToParcelLocked(out, uSecUptime, uSecRealtime);
            }
            return;
        }
        parcel.writeInt(i2);
    }

    public void prepareForDumpLocked() {
        pullPendingStateUpdatesLocked();
        getStartClockTime();
    }

    public void dumpLocked(Context context, PrintWriter pw, int flags, int reqUid, long histStart) {
        super.dumpLocked(context, pw, flags, reqUid, histStart);
        pw.print("Total cpu time reads: ");
        pw.println(this.mNumSingleUidCpuTimeReads);
        pw.print("Batched cpu time reads: ");
        pw.println(this.mNumBatchedSingleUidCpuTimeReads);
        pw.print("Batching Duration (min): ");
        pw.println((this.mClocks.uptimeMillis() - this.mCpuTimeReadsTrackingStartTime) / 60000);
        pw.print("All UID cpu time reads since the later of device start or stats reset: ");
        pw.println(this.mNumAllUidCpuTimeReads);
        pw.print("UIDs removed since the later of device start or stats reset: ");
        pw.println(this.mNumUidsRemoved);
    }
}
