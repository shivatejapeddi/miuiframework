package android.os;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.backup.FullBackup;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.UserInfo;
import android.location.LocationManager;
import android.miui.BiometricConnect;
import android.net.TrafficStats;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.statistics.PerfEventConstants;
import android.provider.Telephony.BaseMmsColumns;
import android.telephony.SignalStrength;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.LongSparseArray;
import android.util.MutableBoolean;
import android.util.Pair;
import android.util.Printer;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.view.SurfaceControl;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BatterySipper;
import com.android.internal.os.BatterySipper.DrainType;
import com.android.internal.os.BatteryStatsHelper;
import com.android.internal.telephony.PhoneConstants;
import com.miui.mishare.RemoteDevice;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.miui.commons.lang3.CharUtils;

public abstract class BatteryStats implements Parcelable {
    private static final String AGGREGATED_WAKELOCK_DATA = "awl";
    public static final int AGGREGATED_WAKE_TYPE_PARTIAL = 20;
    private static final String APK_DATA = "apk";
    private static final String AUDIO_DATA = "aud";
    public static final int AUDIO_TURNED_ON = 15;
    private static final String BATTERY_DATA = "bt";
    private static final String BATTERY_DISCHARGE_DATA = "dc";
    private static final String BATTERY_LEVEL_DATA = "lv";
    private static final int BATTERY_STATS_CHECKIN_VERSION = 9;
    private static final String BLUETOOTH_CONTROLLER_DATA = "ble";
    private static final String BLUETOOTH_MISC_DATA = "blem";
    public static final int BLUETOOTH_SCAN_ON = 19;
    public static final int BLUETOOTH_UNOPTIMIZED_SCAN_ON = 21;
    private static final long BYTES_PER_GB = 1073741824;
    private static final long BYTES_PER_KB = 1024;
    private static final long BYTES_PER_MB = 1048576;
    private static final String CAMERA_DATA = "cam";
    public static final int CAMERA_TURNED_ON = 17;
    private static final String CELLULAR_CONTROLLER_NAME = "Cellular";
    private static final String CHARGE_STEP_DATA = "csd";
    private static final String CHARGE_TIME_REMAIN_DATA = "ctr";
    static final int CHECKIN_VERSION = 34;
    private static final String CPU_DATA = "cpu";
    private static final String CPU_TIMES_AT_FREQ_DATA = "ctf";
    private static final String DATA_CONNECTION_COUNT_DATA = "dcc";
    static final String[] DATA_CONNECTION_NAMES = new String[]{"none", "gprs", "edge", "umts", "cdma", "evdo_0", "evdo_A", "1xrtt", "hsdpa", "hsupa", "hspa", "iden", "evdo_b", "lte", "ehrpd", "hspap", "gsm", "td_scdma", "iwlan", "lte_ca", "nr", "other"};
    public static final int DATA_CONNECTION_NONE = 0;
    public static final int DATA_CONNECTION_OTHER = 21;
    private static final String DATA_CONNECTION_TIME_DATA = "dct";
    public static final int DEVICE_IDLE_MODE_DEEP = 2;
    public static final int DEVICE_IDLE_MODE_LIGHT = 1;
    public static final int DEVICE_IDLE_MODE_OFF = 0;
    private static final String DISCHARGE_STEP_DATA = "dsd";
    private static final String DISCHARGE_TIME_REMAIN_DATA = "dtr";
    public static final int DUMP_CHARGED_ONLY = 2;
    public static final int DUMP_DAILY_ONLY = 4;
    public static final int DUMP_DEVICE_WIFI_ONLY = 64;
    public static final int DUMP_HISTORY_ONLY = 8;
    public static final int DUMP_INCLUDE_HISTORY = 16;
    public static final int DUMP_VERBOSE = 32;
    private static final String FLASHLIGHT_DATA = "fla";
    public static final int FLASHLIGHT_TURNED_ON = 16;
    public static final int FOREGROUND_ACTIVITY = 10;
    private static final String FOREGROUND_ACTIVITY_DATA = "fg";
    public static final int FOREGROUND_SERVICE = 22;
    private static final String FOREGROUND_SERVICE_DATA = "fgs";
    public static final int FULL_WIFI_LOCK = 5;
    private static final String GLOBAL_BLUETOOTH_CONTROLLER_DATA = "gble";
    private static final String GLOBAL_CPU_FREQ_DATA = "gcf";
    private static final String GLOBAL_MODEM_CONTROLLER_DATA = "gmcd";
    private static final String GLOBAL_NETWORK_DATA = "gn";
    private static final String GLOBAL_WIFI_CONTROLLER_DATA = "gwfcd";
    private static final String GLOBAL_WIFI_DATA = "gwfl";
    private static final String HISTORY_DATA = "h";
    public static final String[] HISTORY_EVENT_CHECKIN_NAMES = new String[]{"Enl", "Epr", "Efg", "Etp", "Esy", "Ewl", "Ejb", "Eur", "Euf", "Ecn", "Eac", "Epi", "Epu", "Eal", "Est", "Eai", "Eaa", "Etw", "Esw", "Ewa", "Elw", "Eec"};
    public static final IntToString[] HISTORY_EVENT_INT_FORMATTERS;
    public static final String[] HISTORY_EVENT_NAMES = new String[]{"null", "proc", FOREGROUND_ACTIVITY_DATA, "top", "sync", "wake_lock_in", "job", "user", "userfg", "conn", "active", "pkginst", "pkgunin", "alarm", Context.STATS_MANAGER, "pkginactive", "pkgactive", "tmpwhitelist", "screenwake", "wakeupap", "longwake", "est_capacity"};
    public static final BitDescription[] HISTORY_STATE2_DESCRIPTIONS;
    public static final BitDescription[] HISTORY_STATE_DESCRIPTIONS;
    private static final String HISTORY_STRING_POOL = "hsp";
    public static final int JOB = 14;
    private static final String JOBS_DEFERRED_DATA = "jbd";
    private static final String JOB_COMPLETION_DATA = "jbc";
    private static final String JOB_DATA = "jb";
    public static final long[] JOB_FRESHNESS_BUCKETS = new long[]{3600000, 7200000, 14400000, 28800000, Long.MAX_VALUE};
    private static final String KERNEL_WAKELOCK_DATA = "kwl";
    private static final boolean LOCAL_LOGV = false;
    public static final int MAX_TRACKED_SCREEN_STATE = 4;
    public static final double MILLISECONDS_IN_HOUR = 3600000.0d;
    private static final String MISC_DATA = "m";
    private static final String MODEM_CONTROLLER_DATA = "mcd";
    public static final int NETWORK_BT_RX_DATA = 4;
    public static final int NETWORK_BT_TX_DATA = 5;
    private static final String NETWORK_DATA = "nt";
    public static final int NETWORK_MOBILE_BG_RX_DATA = 6;
    public static final int NETWORK_MOBILE_BG_TX_DATA = 7;
    public static final int NETWORK_MOBILE_RX_DATA = 0;
    public static final int NETWORK_MOBILE_TX_DATA = 1;
    public static final int NETWORK_WIFI_BG_RX_DATA = 8;
    public static final int NETWORK_WIFI_BG_TX_DATA = 9;
    public static final int NETWORK_WIFI_RX_DATA = 2;
    public static final int NETWORK_WIFI_TX_DATA = 3;
    @UnsupportedAppUsage
    public static final int NUM_DATA_CONNECTION_TYPES = 22;
    public static final int NUM_NETWORK_ACTIVITY_TYPES = 10;
    @UnsupportedAppUsage
    public static final int NUM_SCREEN_BRIGHTNESS_BINS = 5;
    public static final int NUM_WIFI_SIGNAL_STRENGTH_BINS = 5;
    public static final int NUM_WIFI_STATES = 8;
    public static final int NUM_WIFI_SUPPL_STATES = 13;
    private static final String POWER_USE_ITEM_DATA = "pwi";
    private static final String POWER_USE_SUMMARY_DATA = "pws";
    private static final String PROCESS_DATA = "pr";
    public static final int PROCESS_STATE = 12;
    private static final String RESOURCE_POWER_MANAGER_DATA = "rpm";
    public static final String RESULT_RECEIVER_CONTROLLER_KEY = "controller_activity";
    public static final int SCREEN_BRIGHTNESS_BRIGHT = 4;
    public static final int SCREEN_BRIGHTNESS_DARK = 0;
    private static final String SCREEN_BRIGHTNESS_DATA = "br";
    public static final int SCREEN_BRIGHTNESS_DIM = 1;
    public static final int SCREEN_BRIGHTNESS_LIGHT = 3;
    public static final int SCREEN_BRIGHTNESS_MEDIUM = 2;
    static final String[] SCREEN_BRIGHTNESS_NAMES;
    static final String[] SCREEN_BRIGHTNESS_SHORT_NAMES;
    protected static final boolean SCREEN_OFF_RPM_STATS_ENABLED = false;
    public static final int SENSOR = 3;
    private static final String SENSOR_DATA = "sr";
    public static final String SERVICE_NAME = "batterystats";
    private static final String SIGNAL_SCANNING_TIME_DATA = "sst";
    private static final String SIGNAL_STRENGTH_COUNT_DATA = "sgc";
    private static final String SIGNAL_STRENGTH_TIME_DATA = "sgt";
    private static final String STATE_TIME_DATA = "st";
    @Deprecated
    @UnsupportedAppUsage
    public static final int STATS_CURRENT = 1;
    public static final int STATS_SINCE_CHARGED = 0;
    @Deprecated
    public static final int STATS_SINCE_UNPLUGGED = 2;
    private static final String[] STAT_NAMES = new String[]{"l", FullBackup.CACHE_TREE_TOKEN, "u"};
    public static final long STEP_LEVEL_INITIAL_MODE_MASK = 71776119061217280L;
    public static final int STEP_LEVEL_INITIAL_MODE_SHIFT = 48;
    public static final long STEP_LEVEL_LEVEL_MASK = 280375465082880L;
    public static final int STEP_LEVEL_LEVEL_SHIFT = 40;
    public static final int[] STEP_LEVEL_MODES_OF_INTEREST = new int[]{7, 15, 11, 7, 7, 7, 7, 7, 15, 11};
    public static final int STEP_LEVEL_MODE_DEVICE_IDLE = 8;
    public static final String[] STEP_LEVEL_MODE_LABELS = new String[]{"screen off", "screen off power save", "screen off device idle", "screen on", "screen on power save", "screen doze", "screen doze power save", "screen doze-suspend", "screen doze-suspend power save", "screen doze-suspend device idle"};
    public static final int STEP_LEVEL_MODE_POWER_SAVE = 4;
    public static final int STEP_LEVEL_MODE_SCREEN_STATE = 3;
    public static final int[] STEP_LEVEL_MODE_VALUES = new int[]{0, 4, 8, 1, 5, 2, 6, 3, 7, 11};
    public static final long STEP_LEVEL_MODIFIED_MODE_MASK = -72057594037927936L;
    public static final int STEP_LEVEL_MODIFIED_MODE_SHIFT = 56;
    public static final long STEP_LEVEL_TIME_MASK = 1099511627775L;
    public static final int SYNC = 13;
    private static final String SYNC_DATA = "sy";
    private static final String TAG = "BatteryStats";
    private static final String UID_DATA = "uid";
    @VisibleForTesting
    public static final String UID_TIMES_TYPE_ALL = "A";
    private static final String USER_ACTIVITY_DATA = "ua";
    private static final String VERSION_DATA = "vers";
    private static final String VIBRATOR_DATA = "vib";
    public static final int VIBRATOR_ON = 9;
    private static final String VIDEO_DATA = "vid";
    public static final int VIDEO_TURNED_ON = 8;
    private static final String WAKELOCK_DATA = "wl";
    private static final String WAKEUP_ALARM_DATA = "wua";
    private static final String WAKEUP_REASON_DATA = "wr";
    public static final int WAKE_TYPE_DRAW = 18;
    public static final int WAKE_TYPE_FULL = 1;
    @UnsupportedAppUsage
    public static final int WAKE_TYPE_PARTIAL = 0;
    public static final int WAKE_TYPE_WINDOW = 2;
    public static final int WIFI_AGGREGATE_MULTICAST_ENABLED = 23;
    public static final int WIFI_BATCHED_SCAN = 11;
    private static final String WIFI_CONTROLLER_DATA = "wfcd";
    private static final String WIFI_CONTROLLER_NAME = "WiFi";
    private static final String WIFI_DATA = "wfl";
    private static final String WIFI_MULTICAST_DATA = "wmc";
    public static final int WIFI_MULTICAST_ENABLED = 7;
    private static final String WIFI_MULTICAST_TOTAL_DATA = "wmct";
    public static final int WIFI_RUNNING = 4;
    public static final int WIFI_SCAN = 6;
    private static final String WIFI_SIGNAL_STRENGTH_COUNT_DATA = "wsgc";
    private static final String WIFI_SIGNAL_STRENGTH_TIME_DATA = "wsgt";
    private static final String WIFI_STATE_COUNT_DATA = "wsc";
    static final String[] WIFI_STATE_NAMES = new String[]{"off", "scanning", "no_net", "disconn", "sta", "p2p", "sta_p2p", "soft_ap"};
    public static final int WIFI_STATE_OFF = 0;
    public static final int WIFI_STATE_OFF_SCANNING = 1;
    public static final int WIFI_STATE_ON_CONNECTED_P2P = 5;
    public static final int WIFI_STATE_ON_CONNECTED_STA = 4;
    public static final int WIFI_STATE_ON_CONNECTED_STA_P2P = 6;
    public static final int WIFI_STATE_ON_DISCONNECTED = 3;
    public static final int WIFI_STATE_ON_NO_NETWORKS = 2;
    public static final int WIFI_STATE_SOFT_AP = 7;
    private static final String WIFI_STATE_TIME_DATA = "wst";
    public static final int WIFI_SUPPL_STATE_ASSOCIATED = 7;
    public static final int WIFI_SUPPL_STATE_ASSOCIATING = 6;
    public static final int WIFI_SUPPL_STATE_AUTHENTICATING = 5;
    public static final int WIFI_SUPPL_STATE_COMPLETED = 10;
    private static final String WIFI_SUPPL_STATE_COUNT_DATA = "wssc";
    public static final int WIFI_SUPPL_STATE_DISCONNECTED = 1;
    public static final int WIFI_SUPPL_STATE_DORMANT = 11;
    public static final int WIFI_SUPPL_STATE_FOUR_WAY_HANDSHAKE = 8;
    public static final int WIFI_SUPPL_STATE_GROUP_HANDSHAKE = 9;
    public static final int WIFI_SUPPL_STATE_INACTIVE = 3;
    public static final int WIFI_SUPPL_STATE_INTERFACE_DISABLED = 2;
    public static final int WIFI_SUPPL_STATE_INVALID = 0;
    static final String[] WIFI_SUPPL_STATE_NAMES = new String[]{"invalid", "disconn", "disabled", "inactive", "scanning", "authenticating", "associating", "associated", "4-way-handshake", "group-handshake", "completed", "dormant", "uninit"};
    public static final int WIFI_SUPPL_STATE_SCANNING = 4;
    static final String[] WIFI_SUPPL_STATE_SHORT_NAMES = new String[]{"inv", "dsc", "dis", "inact", "scan", "auth", "ascing", "asced", "4-way", "group", "compl", "dorm", "uninit"};
    private static final String WIFI_SUPPL_STATE_TIME_DATA = "wsst";
    public static final int WIFI_SUPPL_STATE_UNINITIALIZED = 12;
    private static final IntToString sIntToString = -$$Lambda$q1UvBdLgHRZVzc68BxdksTmbuCw.INSTANCE;
    private static final IntToString sUidToString = -$$Lambda$IyvVQC-0mKtsfXbnO0kDL64hrk0.INSTANCE;
    private final StringBuilder mFormatBuilder = new StringBuilder(32);
    private final Formatter mFormatter = new Formatter(this.mFormatBuilder);

    @FunctionalInterface
    public interface IntToString {
        String applyAsString(int i);
    }

    public static final class BitDescription {
        public final int mask;
        public final String name;
        public final int shift;
        public final String shortName;
        public final String[] shortValues;
        public final String[] values;

        public BitDescription(int mask, String name, String shortName) {
            this.mask = mask;
            this.shift = -1;
            this.name = name;
            this.shortName = shortName;
            this.values = null;
            this.shortValues = null;
        }

        public BitDescription(int mask, int shift, String name, String shortName, String[] values, String[] shortValues) {
            this.mask = mask;
            this.shift = shift;
            this.name = name;
            this.shortName = shortName;
            this.values = values;
            this.shortValues = shortValues;
        }
    }

    public static abstract class ControllerActivityCounter {
        public abstract LongCounter getIdleTimeCounter();

        public abstract LongCounter getMonitoredRailChargeConsumedMaMs();

        public abstract LongCounter getPowerCounter();

        public abstract LongCounter getRxTimeCounter();

        public abstract LongCounter getScanTimeCounter();

        public abstract LongCounter getSleepTimeCounter();

        public abstract LongCounter[] getTxTimeCounters();
    }

    public static abstract class Counter {
        @UnsupportedAppUsage
        public abstract int getCountLocked(int i);

        public abstract void logState(Printer printer, String str);
    }

    public static final class DailyItem {
        public LevelStepTracker mChargeSteps;
        public LevelStepTracker mDischargeSteps;
        public long mEndTime;
        public ArrayList<PackageChange> mPackageChanges;
        public long mStartTime;
    }

    public static final class HistoryEventTracker {
        private final HashMap<String, SparseIntArray>[] mActiveEvents = new HashMap[22];

        public boolean updateState(int code, String name, int uid, int poolIdx) {
            int idx;
            HashMap<String, SparseIntArray> active;
            SparseIntArray uids;
            if ((32768 & code) != 0) {
                idx = code & HistoryItem.EVENT_TYPE_MASK;
                active = this.mActiveEvents[idx];
                if (active == null) {
                    active = new HashMap();
                    this.mActiveEvents[idx] = active;
                }
                uids = (SparseIntArray) active.get(name);
                if (uids == null) {
                    uids = new SparseIntArray();
                    active.put(name, uids);
                }
                if (uids.indexOfKey(uid) >= 0) {
                    return false;
                }
                uids.put(uid, poolIdx);
            } else if ((code & 16384) != 0) {
                active = this.mActiveEvents[code & HistoryItem.EVENT_TYPE_MASK];
                if (active == null) {
                    return false;
                }
                uids = (SparseIntArray) active.get(name);
                if (uids == null) {
                    return false;
                }
                idx = uids.indexOfKey(uid);
                if (idx < 0) {
                    return false;
                }
                uids.removeAt(idx);
                if (uids.size() <= 0) {
                    active.remove(name);
                }
            }
            return true;
        }

        public void removeEvents(int code) {
            this.mActiveEvents[HistoryItem.EVENT_TYPE_MASK & code] = null;
        }

        public HashMap<String, SparseIntArray> getStateForEvent(int code) {
            return this.mActiveEvents[code];
        }
    }

    public static final class HistoryItem implements Parcelable {
        public static final byte CMD_CURRENT_TIME = (byte) 5;
        public static final byte CMD_NULL = (byte) -1;
        public static final byte CMD_OVERFLOW = (byte) 6;
        public static final byte CMD_RESET = (byte) 7;
        public static final byte CMD_SHUTDOWN = (byte) 8;
        public static final byte CMD_START = (byte) 4;
        @UnsupportedAppUsage
        public static final byte CMD_UPDATE = (byte) 0;
        public static final int EVENT_ACTIVE = 10;
        public static final int EVENT_ALARM = 13;
        public static final int EVENT_ALARM_FINISH = 16397;
        public static final int EVENT_ALARM_START = 32781;
        public static final int EVENT_COLLECT_EXTERNAL_STATS = 14;
        public static final int EVENT_CONNECTIVITY_CHANGED = 9;
        public static final int EVENT_COUNT = 22;
        public static final int EVENT_FLAG_FINISH = 16384;
        public static final int EVENT_FLAG_START = 32768;
        public static final int EVENT_FOREGROUND = 2;
        public static final int EVENT_FOREGROUND_FINISH = 16386;
        public static final int EVENT_FOREGROUND_START = 32770;
        public static final int EVENT_JOB = 6;
        public static final int EVENT_JOB_FINISH = 16390;
        public static final int EVENT_JOB_START = 32774;
        public static final int EVENT_LONG_WAKE_LOCK = 20;
        public static final int EVENT_LONG_WAKE_LOCK_FINISH = 16404;
        public static final int EVENT_LONG_WAKE_LOCK_START = 32788;
        public static final int EVENT_NONE = 0;
        public static final int EVENT_PACKAGE_ACTIVE = 16;
        public static final int EVENT_PACKAGE_INACTIVE = 15;
        public static final int EVENT_PACKAGE_INSTALLED = 11;
        public static final int EVENT_PACKAGE_UNINSTALLED = 12;
        public static final int EVENT_PROC = 1;
        public static final int EVENT_PROC_FINISH = 16385;
        public static final int EVENT_PROC_START = 32769;
        public static final int EVENT_SCREEN_WAKE_UP = 18;
        public static final int EVENT_SYNC = 4;
        public static final int EVENT_SYNC_FINISH = 16388;
        public static final int EVENT_SYNC_START = 32772;
        public static final int EVENT_TEMP_WHITELIST = 17;
        public static final int EVENT_TEMP_WHITELIST_FINISH = 16401;
        public static final int EVENT_TEMP_WHITELIST_START = 32785;
        public static final int EVENT_TOP = 3;
        public static final int EVENT_TOP_FINISH = 16387;
        public static final int EVENT_TOP_START = 32771;
        public static final int EVENT_TYPE_MASK = -49153;
        public static final int EVENT_USER_FOREGROUND = 8;
        public static final int EVENT_USER_FOREGROUND_FINISH = 16392;
        public static final int EVENT_USER_FOREGROUND_START = 32776;
        public static final int EVENT_USER_RUNNING = 7;
        public static final int EVENT_USER_RUNNING_FINISH = 16391;
        public static final int EVENT_USER_RUNNING_START = 32775;
        public static final int EVENT_WAKEUP_AP = 19;
        public static final int EVENT_WAKE_LOCK = 5;
        public static final int EVENT_WAKE_LOCK_FINISH = 16389;
        public static final int EVENT_WAKE_LOCK_START = 32773;
        public static final int MOST_INTERESTING_STATES = 1835008;
        public static final int MOST_INTERESTING_STATES2 = -1749024768;
        public static final int SETTLE_TO_ZERO_STATES = -1900544;
        public static final int SETTLE_TO_ZERO_STATES2 = 1748959232;
        public static final int STATE2_BLUETOOTH_ON_FLAG = 4194304;
        public static final int STATE2_BLUETOOTH_SCAN_FLAG = 1048576;
        public static final int STATE2_CAMERA_FLAG = 2097152;
        public static final int STATE2_CELLULAR_HIGH_TX_POWER_FLAG = 524288;
        public static final int STATE2_CHARGING_FLAG = 16777216;
        public static final int STATE2_DEVICE_IDLE_MASK = 100663296;
        public static final int STATE2_DEVICE_IDLE_SHIFT = 25;
        public static final int STATE2_FLASHLIGHT_FLAG = 134217728;
        public static final int STATE2_GPS_SIGNAL_QUALITY_MASK = 128;
        public static final int STATE2_GPS_SIGNAL_QUALITY_SHIFT = 7;
        public static final int STATE2_PHONE_IN_CALL_FLAG = 8388608;
        public static final int STATE2_POWER_SAVE_FLAG = Integer.MIN_VALUE;
        public static final int STATE2_USB_DATA_LINK_FLAG = 262144;
        public static final int STATE2_VIDEO_ON_FLAG = 1073741824;
        public static final int STATE2_WIFI_ON_FLAG = 268435456;
        public static final int STATE2_WIFI_RUNNING_FLAG = 536870912;
        public static final int STATE2_WIFI_SIGNAL_STRENGTH_MASK = 112;
        public static final int STATE2_WIFI_SIGNAL_STRENGTH_SHIFT = 4;
        public static final int STATE2_WIFI_SUPPL_STATE_MASK = 15;
        public static final int STATE2_WIFI_SUPPL_STATE_SHIFT = 0;
        public static final int STATE_AUDIO_ON_FLAG = 4194304;
        public static final int STATE_BATTERY_PLUGGED_FLAG = 524288;
        public static final int STATE_BRIGHTNESS_MASK = 7;
        public static final int STATE_BRIGHTNESS_SHIFT = 0;
        public static final int STATE_CPU_RUNNING_FLAG = Integer.MIN_VALUE;
        public static final int STATE_DATA_CONNECTION_MASK = 15872;
        public static final int STATE_DATA_CONNECTION_SHIFT = 9;
        public static final int STATE_GPS_ON_FLAG = 536870912;
        public static final int STATE_MOBILE_RADIO_ACTIVE_FLAG = 33554432;
        public static final int STATE_PHONE_SCANNING_FLAG = 2097152;
        public static final int STATE_PHONE_SIGNAL_STRENGTH_MASK = 56;
        public static final int STATE_PHONE_SIGNAL_STRENGTH_SHIFT = 3;
        public static final int STATE_PHONE_STATE_MASK = 448;
        public static final int STATE_PHONE_STATE_SHIFT = 6;
        private static final int STATE_RESERVED_0 = 16777216;
        public static final int STATE_SCREEN_DOZE_FLAG = 262144;
        public static final int STATE_SCREEN_ON_FLAG = 1048576;
        public static final int STATE_SENSOR_ON_FLAG = 8388608;
        public static final int STATE_WAKE_LOCK_FLAG = 1073741824;
        public static final int STATE_WIFI_FULL_LOCK_FLAG = 268435456;
        public static final int STATE_WIFI_MULTICAST_ON_FLAG = 65536;
        public static final int STATE_WIFI_RADIO_ACTIVE_FLAG = 67108864;
        public static final int STATE_WIFI_SCAN_FLAG = 134217728;
        public int batteryChargeUAh;
        @UnsupportedAppUsage
        public byte batteryHealth;
        @UnsupportedAppUsage
        public byte batteryLevel;
        @UnsupportedAppUsage
        public byte batteryPlugType;
        @UnsupportedAppUsage
        public byte batteryStatus;
        public short batteryTemperature;
        @UnsupportedAppUsage
        public char batteryVoltage;
        @UnsupportedAppUsage
        public byte cmd = (byte) -1;
        public long currentTime;
        public int eventCode;
        public HistoryTag eventTag;
        public final HistoryTag localEventTag = new HistoryTag();
        public final HistoryTag localWakeReasonTag = new HistoryTag();
        public final HistoryTag localWakelockTag = new HistoryTag();
        public double modemRailChargeMah;
        public HistoryItem next;
        public int numReadInts;
        @UnsupportedAppUsage
        public int states;
        @UnsupportedAppUsage
        public int states2;
        public HistoryStepDetails stepDetails;
        @UnsupportedAppUsage
        public long time;
        public HistoryTag wakeReasonTag;
        public HistoryTag wakelockTag;
        public double wifiRailChargeMah;

        public boolean isDeltaData() {
            return this.cmd == (byte) 0;
        }

        public HistoryItem(long time, Parcel src) {
            this.time = time;
            this.numReadInts = 2;
            readFromParcel(src);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.time);
            int i = 0;
            int bat = ((((((this.cmd & 255) | ((this.batteryLevel << 8) & 65280)) | ((this.batteryStatus << 16) & SurfaceControl.FX_SURFACE_MASK)) | ((this.batteryHealth << 20) & UserInfo.FLAG__MASK_XSPACE_PROFILE_TYPE)) | ((this.batteryPlugType << 24) & PerfEventConstants.FLAG_DETAILS_SOURCE_MASK)) | (this.wakelockTag != null ? 268435456 : 0)) | (this.wakeReasonTag != null ? 536870912 : 0);
            if (this.eventCode != 0) {
                i = 1073741824;
            }
            dest.writeInt(bat | i);
            dest.writeInt((this.batteryTemperature & 65535) | ((this.batteryVoltage << 16) & -65536));
            dest.writeInt(this.batteryChargeUAh);
            dest.writeDouble(this.modemRailChargeMah);
            dest.writeDouble(this.wifiRailChargeMah);
            dest.writeInt(this.states);
            dest.writeInt(this.states2);
            HistoryTag historyTag = this.wakelockTag;
            if (historyTag != null) {
                historyTag.writeToParcel(dest, flags);
            }
            historyTag = this.wakeReasonTag;
            if (historyTag != null) {
                historyTag.writeToParcel(dest, flags);
            }
            int i2 = this.eventCode;
            if (i2 != 0) {
                dest.writeInt(i2);
                this.eventTag.writeToParcel(dest, flags);
            }
            byte b = this.cmd;
            if (b == (byte) 5 || b == (byte) 7) {
                dest.writeLong(this.currentTime);
            }
        }

        public void readFromParcel(Parcel src) {
            int start = src.dataPosition();
            int bat = src.readInt();
            this.cmd = (byte) (bat & 255);
            this.batteryLevel = (byte) ((bat >> 8) & 255);
            this.batteryStatus = (byte) ((bat >> 16) & 15);
            this.batteryHealth = (byte) ((bat >> 20) & 15);
            this.batteryPlugType = (byte) ((bat >> 24) & 15);
            int bat2 = src.readInt();
            this.batteryTemperature = (short) (bat2 & 65535);
            this.batteryVoltage = (char) (65535 & (bat2 >> 16));
            this.batteryChargeUAh = src.readInt();
            this.modemRailChargeMah = src.readDouble();
            this.wifiRailChargeMah = src.readDouble();
            this.states = src.readInt();
            this.states2 = src.readInt();
            if ((268435456 & bat) != 0) {
                this.wakelockTag = this.localWakelockTag;
                this.wakelockTag.readFromParcel(src);
            } else {
                this.wakelockTag = null;
            }
            if ((536870912 & bat) != 0) {
                this.wakeReasonTag = this.localWakeReasonTag;
                this.wakeReasonTag.readFromParcel(src);
            } else {
                this.wakeReasonTag = null;
            }
            if ((1073741824 & bat) != 0) {
                this.eventCode = src.readInt();
                this.eventTag = this.localEventTag;
                this.eventTag.readFromParcel(src);
            } else {
                this.eventCode = 0;
                this.eventTag = null;
            }
            byte b = this.cmd;
            if (b == (byte) 5 || b == (byte) 7) {
                this.currentTime = src.readLong();
            } else {
                this.currentTime = 0;
            }
            this.numReadInts += (src.dataPosition() - start) / 4;
        }

        public void clear() {
            this.time = 0;
            this.cmd = (byte) -1;
            this.batteryLevel = (byte) 0;
            this.batteryStatus = (byte) 0;
            this.batteryHealth = (byte) 0;
            this.batteryPlugType = (byte) 0;
            this.batteryTemperature = (short) 0;
            this.batteryVoltage = 0;
            this.batteryChargeUAh = 0;
            this.modemRailChargeMah = 0.0d;
            this.wifiRailChargeMah = 0.0d;
            this.states = 0;
            this.states2 = 0;
            this.wakelockTag = null;
            this.wakeReasonTag = null;
            this.eventCode = 0;
            this.eventTag = null;
        }

        public void setTo(HistoryItem o) {
            this.time = o.time;
            this.cmd = o.cmd;
            setToCommon(o);
        }

        public void setTo(long time, byte cmd, HistoryItem o) {
            this.time = time;
            this.cmd = cmd;
            setToCommon(o);
        }

        private void setToCommon(HistoryItem o) {
            this.batteryLevel = o.batteryLevel;
            this.batteryStatus = o.batteryStatus;
            this.batteryHealth = o.batteryHealth;
            this.batteryPlugType = o.batteryPlugType;
            this.batteryTemperature = o.batteryTemperature;
            this.batteryVoltage = o.batteryVoltage;
            this.batteryChargeUAh = o.batteryChargeUAh;
            this.modemRailChargeMah = o.modemRailChargeMah;
            this.wifiRailChargeMah = o.wifiRailChargeMah;
            this.states = o.states;
            this.states2 = o.states2;
            if (o.wakelockTag != null) {
                this.wakelockTag = this.localWakelockTag;
                this.wakelockTag.setTo(o.wakelockTag);
            } else {
                this.wakelockTag = null;
            }
            if (o.wakeReasonTag != null) {
                this.wakeReasonTag = this.localWakeReasonTag;
                this.wakeReasonTag.setTo(o.wakeReasonTag);
            } else {
                this.wakeReasonTag = null;
            }
            this.eventCode = o.eventCode;
            if (o.eventTag != null) {
                this.eventTag = this.localEventTag;
                this.eventTag.setTo(o.eventTag);
            } else {
                this.eventTag = null;
            }
            this.currentTime = o.currentTime;
        }

        public boolean sameNonEvent(HistoryItem o) {
            return this.batteryLevel == o.batteryLevel && this.batteryStatus == o.batteryStatus && this.batteryHealth == o.batteryHealth && this.batteryPlugType == o.batteryPlugType && this.batteryTemperature == o.batteryTemperature && this.batteryVoltage == o.batteryVoltage && this.batteryChargeUAh == o.batteryChargeUAh && this.modemRailChargeMah == o.modemRailChargeMah && this.wifiRailChargeMah == o.wifiRailChargeMah && this.states == o.states && this.states2 == o.states2 && this.currentTime == o.currentTime;
        }

        /* JADX WARNING: Missing block: B:11:0x0020, code skipped:
            return false;
     */
        /* JADX WARNING: Missing block: B:19:0x0033, code skipped:
            return false;
     */
        /* JADX WARNING: Missing block: B:27:0x0046, code skipped:
            return false;
     */
        public boolean same(android.os.BatteryStats.HistoryItem r4) {
            /*
            r3 = this;
            r0 = r3.sameNonEvent(r4);
            r1 = 0;
            if (r0 == 0) goto L_0x0049;
        L_0x0007:
            r0 = r3.eventCode;
            r2 = r4.eventCode;
            if (r0 == r2) goto L_0x000e;
        L_0x000d:
            goto L_0x0049;
        L_0x000e:
            r0 = r3.wakelockTag;
            r2 = r4.wakelockTag;
            if (r0 == r2) goto L_0x0021;
        L_0x0014:
            if (r0 == 0) goto L_0x0020;
        L_0x0016:
            if (r2 != 0) goto L_0x0019;
        L_0x0018:
            goto L_0x0020;
        L_0x0019:
            r0 = r0.equals(r2);
            if (r0 != 0) goto L_0x0021;
        L_0x001f:
            return r1;
        L_0x0020:
            return r1;
        L_0x0021:
            r0 = r3.wakeReasonTag;
            r2 = r4.wakeReasonTag;
            if (r0 == r2) goto L_0x0034;
        L_0x0027:
            if (r0 == 0) goto L_0x0033;
        L_0x0029:
            if (r2 != 0) goto L_0x002c;
        L_0x002b:
            goto L_0x0033;
        L_0x002c:
            r0 = r0.equals(r2);
            if (r0 != 0) goto L_0x0034;
        L_0x0032:
            return r1;
        L_0x0033:
            return r1;
        L_0x0034:
            r0 = r3.eventTag;
            r2 = r4.eventTag;
            if (r0 == r2) goto L_0x0047;
        L_0x003a:
            if (r0 == 0) goto L_0x0046;
        L_0x003c:
            if (r2 != 0) goto L_0x003f;
        L_0x003e:
            goto L_0x0046;
        L_0x003f:
            r0 = r0.equals(r2);
            if (r0 != 0) goto L_0x0047;
        L_0x0045:
            return r1;
        L_0x0046:
            return r1;
        L_0x0047:
            r0 = 1;
            return r0;
        L_0x0049:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.os.BatteryStats$HistoryItem.same(android.os.BatteryStats$HistoryItem):boolean");
        }
    }

    public static class HistoryPrinter {
        long lastTime = -1;
        int oldChargeMAh = -1;
        int oldHealth = -1;
        int oldLevel = -1;
        double oldModemRailChargeMah = -1.0d;
        int oldPlug = -1;
        int oldState = 0;
        int oldState2 = 0;
        int oldStatus = -1;
        int oldTemp = -1;
        int oldVolt = -1;
        double oldWifiRailChargeMah = -1.0d;

        /* Access modifiers changed, original: 0000 */
        public void reset() {
            this.oldState2 = 0;
            this.oldState = 0;
            this.oldLevel = -1;
            this.oldStatus = -1;
            this.oldHealth = -1;
            this.oldPlug = -1;
            this.oldTemp = -1;
            this.oldVolt = -1;
            this.oldChargeMAh = -1;
            this.oldModemRailChargeMah = -1.0d;
            this.oldWifiRailChargeMah = -1.0d;
        }

        public void printNextItem(PrintWriter pw, HistoryItem rec, long baseTime, boolean checkin, boolean verbose) {
            pw.print(printNextItem(rec, baseTime, checkin, verbose));
        }

        public void printNextItem(ProtoOutputStream proto, HistoryItem rec, long baseTime, boolean verbose) {
            for (String line : printNextItem(rec, baseTime, true, verbose).split("\n")) {
                proto.write(2237677961222L, line);
            }
        }

        private String printNextItem(HistoryItem rec, long baseTime, boolean checkin, boolean verbose) {
            HistoryItem historyItem = rec;
            StringBuilder item = new StringBuilder();
            String str = " (";
            String str2 = "h";
            if (checkin) {
                item.append(9);
                item.append(',');
                item.append(str2);
                item.append(',');
                if (this.lastTime < 0) {
                    item.append(historyItem.time - baseTime);
                } else {
                    item.append(historyItem.time - this.lastTime);
                }
                this.lastTime = historyItem.time;
            } else {
                item.append("  ");
                TimeUtils.formatDuration(historyItem.time - baseTime, item, 19);
                item.append(str);
                item.append(historyItem.numReadInts);
                item.append(") ");
            }
            String str3 = ":";
            if (historyItem.cmd == (byte) 4) {
                if (checkin) {
                    item.append(str3);
                }
                item.append("START\n");
                reset();
            } else {
                byte b = historyItem.cmd;
                String str4 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                String str5 = "\n";
                if (b == (byte) 5 || historyItem.cmd == (byte) 7) {
                    if (checkin) {
                        item.append(str3);
                    }
                    if (historyItem.cmd == (byte) 7) {
                        item.append("RESET:");
                        reset();
                    }
                    item.append("TIME:");
                    if (checkin) {
                        item.append(historyItem.currentTime);
                        item.append(str5);
                    } else {
                        item.append(str4);
                        item.append(DateFormat.format((CharSequence) "yyyy-MM-dd-HH-mm-ss", historyItem.currentTime).toString());
                        item.append(str5);
                    }
                } else if (historyItem.cmd == (byte) 8) {
                    if (checkin) {
                        item.append(str3);
                    }
                    item.append("SHUTDOWN\n");
                } else if (historyItem.cmd == (byte) 6) {
                    if (checkin) {
                        item.append(str3);
                    }
                    item.append("*OVERFLOW*\n");
                } else {
                    int i;
                    int idx;
                    if (!checkin) {
                        String str6 = "0";
                        String str7 = "00";
                        if (historyItem.batteryLevel < (byte) 10) {
                            item.append(str7);
                        } else if (historyItem.batteryLevel < (byte) 100) {
                            item.append(str6);
                        }
                        item.append(historyItem.batteryLevel);
                        if (verbose) {
                            item.append(str4);
                            if (historyItem.states >= 0) {
                                if (historyItem.states < 16) {
                                    item.append("0000000");
                                } else if (historyItem.states < 256) {
                                    item.append("000000");
                                } else if (historyItem.states < 4096) {
                                    item.append("00000");
                                } else if (historyItem.states < 65536) {
                                    item.append("0000");
                                } else if (historyItem.states < 1048576) {
                                    item.append("000");
                                } else if (historyItem.states < 16777216) {
                                    item.append(str7);
                                } else if (historyItem.states < 268435456) {
                                    item.append(str6);
                                }
                            }
                            item.append(Integer.toHexString(historyItem.states));
                        }
                    } else if (this.oldLevel != historyItem.batteryLevel) {
                        this.oldLevel = historyItem.batteryLevel;
                        item.append(",Bl=");
                        item.append(historyItem.batteryLevel);
                    }
                    if (this.oldStatus != historyItem.batteryStatus) {
                        this.oldStatus = historyItem.batteryStatus;
                        item.append(checkin ? ",Bs=" : " status=");
                        i = this.oldStatus;
                        if (i == 1) {
                            item.append(checkin ? "?" : "unknown");
                        } else if (i == 2) {
                            item.append(checkin ? FullBackup.CACHE_TREE_TOKEN : "charging");
                        } else if (i == 3) {
                            item.append(checkin ? "d" : "discharging");
                        } else if (i == 4) {
                            item.append(checkin ? "n" : "not-charging");
                        } else if (i != 5) {
                            item.append(i);
                        } else {
                            item.append(checkin ? FullBackup.FILES_TREE_TOKEN : "full");
                        }
                    }
                    if (this.oldHealth != historyItem.batteryHealth) {
                        this.oldHealth = historyItem.batteryHealth;
                        item.append(checkin ? ",Bh=" : " health=");
                        i = this.oldHealth;
                        switch (i) {
                            case 1:
                                item.append(checkin ? "?" : "unknown");
                                break;
                            case 2:
                                item.append(checkin ? "g" : "good");
                                break;
                            case 3:
                                item.append(checkin ? str2 : "overheat");
                                break;
                            case 4:
                                item.append(checkin ? "d" : "dead");
                                break;
                            case 5:
                                item.append(checkin ? BaseMmsColumns.MMS_VERSION : "over-voltage");
                                break;
                            case 6:
                                item.append(checkin ? FullBackup.FILES_TREE_TOKEN : "failure");
                                break;
                            case 7:
                                item.append(checkin ? FullBackup.CACHE_TREE_TOKEN : "cold");
                                break;
                            default:
                                item.append(i);
                                break;
                        }
                    }
                    if (this.oldPlug != historyItem.batteryPlugType) {
                        this.oldPlug = historyItem.batteryPlugType;
                        item.append(checkin ? ",Bp=" : " plug=");
                        i = this.oldPlug;
                        if (i == 0) {
                            item.append(checkin ? "n" : "none");
                        } else if (i == 1) {
                            item.append(checkin ? FullBackup.APK_TREE_TOKEN : "ac");
                        } else if (i == 2) {
                            item.append(checkin ? "u" : Context.USB_SERVICE);
                        } else if (i != 4) {
                            item.append(i);
                        } else {
                            item.append(checkin ? BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_W : "wireless");
                        }
                    }
                    if (this.oldTemp != historyItem.batteryTemperature) {
                        this.oldTemp = historyItem.batteryTemperature;
                        item.append(checkin ? ",Bt=" : " temp=");
                        item.append(this.oldTemp);
                    }
                    if (this.oldVolt != historyItem.batteryVoltage) {
                        this.oldVolt = historyItem.batteryVoltage;
                        item.append(checkin ? ",Bv=" : " volt=");
                        item.append(this.oldVolt);
                    }
                    int chargeMAh = historyItem.batteryChargeUAh / 1000;
                    if (this.oldChargeMAh != chargeMAh) {
                        this.oldChargeMAh = chargeMAh;
                        item.append(checkin ? ",Bcc=" : " charge=");
                        item.append(this.oldChargeMAh);
                    }
                    if (this.oldModemRailChargeMah != historyItem.modemRailChargeMah) {
                        this.oldModemRailChargeMah = historyItem.modemRailChargeMah;
                        item.append(checkin ? ",Mrc=" : " modemRailChargemAh=");
                        item.append(new DecimalFormat("#.##").format(this.oldModemRailChargeMah));
                    }
                    if (this.oldWifiRailChargeMah != historyItem.wifiRailChargeMah) {
                        this.oldWifiRailChargeMah = historyItem.wifiRailChargeMah;
                        item.append(checkin ? ",Wrc=" : " wifiRailChargemAh=");
                        item.append(new DecimalFormat("#.##").format(this.oldWifiRailChargeMah));
                    }
                    int i2 = 1;
                    BatteryStats.printBitDescriptions(item, this.oldState, historyItem.states, historyItem.wakelockTag, BatteryStats.HISTORY_STATE_DESCRIPTIONS, checkin ^ 1);
                    BatteryStats.printBitDescriptions(item, this.oldState2, historyItem.states2, null, BatteryStats.HISTORY_STATE2_DESCRIPTIONS, checkin ^ 1);
                    if (historyItem.wakeReasonTag != null) {
                        if (checkin) {
                            item.append(",wr=");
                            item.append(historyItem.wakeReasonTag.poolIdx);
                        } else {
                            item.append(" wake_reason=");
                            item.append(historyItem.wakeReasonTag.uid);
                            item.append(":\"");
                            item.append(historyItem.wakeReasonTag.string);
                            item.append("\"");
                        }
                    }
                    if (historyItem.eventCode != 0) {
                        String[] eventNames;
                        if (checkin) {
                            str4 = ",";
                        }
                        item.append(str4);
                        if ((historyItem.eventCode & 32768) != 0) {
                            item.append("+");
                        } else if ((historyItem.eventCode & 16384) != 0) {
                            item.append("-");
                        }
                        if (checkin) {
                            eventNames = BatteryStats.HISTORY_EVENT_CHECKIN_NAMES;
                        } else {
                            eventNames = BatteryStats.HISTORY_EVENT_NAMES;
                        }
                        idx = historyItem.eventCode & HistoryItem.EVENT_TYPE_MASK;
                        if (idx < 0 || idx >= eventNames.length) {
                            item.append(checkin ? "Ev" : "event");
                            item.append(idx);
                        } else {
                            item.append(eventNames[idx]);
                        }
                        item.append("=");
                        if (checkin) {
                            item.append(historyItem.eventTag.poolIdx);
                        } else {
                            item.append(BatteryStats.HISTORY_EVENT_INT_FORMATTERS[idx].applyAsString(historyItem.eventTag.uid));
                            item.append(":\"");
                            item.append(historyItem.eventTag.string);
                            item.append("\"");
                        }
                    }
                    item.append(str5);
                    if (historyItem.stepDetails != null) {
                        if (checkin) {
                            item.append(9);
                            item.append(',');
                            item.append(str2);
                            item.append(",0,Dcpu=");
                            item.append(historyItem.stepDetails.userTime);
                            item.append(str3);
                            item.append(historyItem.stepDetails.systemTime);
                            if (historyItem.stepDetails.appCpuUid1 >= 0) {
                                printStepCpuUidCheckinDetails(item, historyItem.stepDetails.appCpuUid1, historyItem.stepDetails.appCpuUTime1, historyItem.stepDetails.appCpuSTime1);
                                if (historyItem.stepDetails.appCpuUid2 >= 0) {
                                    printStepCpuUidCheckinDetails(item, historyItem.stepDetails.appCpuUid2, historyItem.stepDetails.appCpuUTime2, historyItem.stepDetails.appCpuSTime2);
                                }
                                if (historyItem.stepDetails.appCpuUid3 >= 0) {
                                    printStepCpuUidCheckinDetails(item, historyItem.stepDetails.appCpuUid3, historyItem.stepDetails.appCpuUTime3, historyItem.stepDetails.appCpuSTime3);
                                }
                            }
                            item.append(str5);
                            item.append(9);
                            item.append(',');
                            item.append(str2);
                            item.append(",0,Dpst=");
                            item.append(historyItem.stepDetails.statUserTime);
                            item.append(',');
                            item.append(historyItem.stepDetails.statSystemTime);
                            item.append(',');
                            item.append(historyItem.stepDetails.statIOWaitTime);
                            item.append(',');
                            item.append(historyItem.stepDetails.statIrqTime);
                            item.append(',');
                            item.append(historyItem.stepDetails.statSoftIrqTime);
                            item.append(',');
                            item.append(historyItem.stepDetails.statIdlTime);
                            item.append(',');
                            if (historyItem.stepDetails.statPlatformIdleState != null) {
                                item.append(historyItem.stepDetails.statPlatformIdleState);
                                if (historyItem.stepDetails.statSubsystemPowerState != null) {
                                    item.append(',');
                                }
                            }
                            if (historyItem.stepDetails.statSubsystemPowerState != null) {
                                item.append(historyItem.stepDetails.statSubsystemPowerState);
                            }
                            item.append(str5);
                        } else {
                            item.append("                 Details: cpu=");
                            item.append(historyItem.stepDetails.userTime);
                            item.append("u+");
                            item.append(historyItem.stepDetails.systemTime);
                            item.append(RemoteDevice.KEY_STATUS);
                            if (historyItem.stepDetails.appCpuUid1 >= 0) {
                                item.append(str);
                                printStepCpuUidDetails(item, historyItem.stepDetails.appCpuUid1, historyItem.stepDetails.appCpuUTime1, historyItem.stepDetails.appCpuSTime1);
                                if (historyItem.stepDetails.appCpuUid2 >= 0) {
                                    item.append(", ");
                                    printStepCpuUidDetails(item, historyItem.stepDetails.appCpuUid2, historyItem.stepDetails.appCpuUTime2, historyItem.stepDetails.appCpuSTime2);
                                }
                                if (historyItem.stepDetails.appCpuUid3 >= 0) {
                                    item.append(", ");
                                    printStepCpuUidDetails(item, historyItem.stepDetails.appCpuUid3, historyItem.stepDetails.appCpuUTime3, historyItem.stepDetails.appCpuSTime3);
                                }
                                item.append(')');
                            }
                            item.append(str5);
                            item.append("                          /proc/stat=");
                            item.append(historyItem.stepDetails.statUserTime);
                            item.append(" usr, ");
                            item.append(historyItem.stepDetails.statSystemTime);
                            item.append(" sys, ");
                            item.append(historyItem.stepDetails.statIOWaitTime);
                            item.append(" io, ");
                            item.append(historyItem.stepDetails.statIrqTime);
                            item.append(" irq, ");
                            item.append(historyItem.stepDetails.statSoftIrqTime);
                            item.append(" sirq, ");
                            item.append(historyItem.stepDetails.statIdlTime);
                            item.append(" idle");
                            i = (((historyItem.stepDetails.statUserTime + historyItem.stepDetails.statSystemTime) + historyItem.stepDetails.statIOWaitTime) + historyItem.stepDetails.statIrqTime) + historyItem.stepDetails.statSoftIrqTime;
                            idx = historyItem.stepDetails.statIdlTime + i;
                            if (idx > 0) {
                                item.append(str);
                                Object[] objArr = new Object[i2];
                                objArr[0] = Float.valueOf((((float) i) / ((float) idx)) * 100.0f);
                                item.append(String.format("%.1f%%", objArr));
                                item.append(" of ");
                                StringBuilder sb = new StringBuilder(64);
                                BatteryStats.formatTimeMsNoSpace(sb, (long) (idx * 10));
                                item.append(sb);
                                item.append(")");
                            }
                            item.append(", PlatformIdleStat ");
                            item.append(historyItem.stepDetails.statPlatformIdleState);
                            item.append(str5);
                            item.append(", SubsystemPowerState ");
                            item.append(historyItem.stepDetails.statSubsystemPowerState);
                            item.append(str5);
                        }
                    }
                    this.oldState = historyItem.states;
                    this.oldState2 = historyItem.states2;
                }
            }
            return item.toString();
        }

        private void printStepCpuUidDetails(StringBuilder sb, int uid, int utime, int stime) {
            UserHandle.formatUid(sb, uid);
            sb.append("=");
            sb.append(utime);
            sb.append("u+");
            sb.append(stime);
            sb.append(RemoteDevice.KEY_STATUS);
        }

        private void printStepCpuUidCheckinDetails(StringBuilder sb, int uid, int utime, int stime) {
            sb.append('/');
            sb.append(uid);
            String str = ":";
            sb.append(str);
            sb.append(utime);
            sb.append(str);
            sb.append(stime);
        }
    }

    public static final class HistoryStepDetails {
        public int appCpuSTime1;
        public int appCpuSTime2;
        public int appCpuSTime3;
        public int appCpuUTime1;
        public int appCpuUTime2;
        public int appCpuUTime3;
        public int appCpuUid1;
        public int appCpuUid2;
        public int appCpuUid3;
        public int statIOWaitTime;
        public int statIdlTime;
        public int statIrqTime;
        public String statPlatformIdleState;
        public int statSoftIrqTime;
        public String statSubsystemPowerState;
        public int statSystemTime;
        public int statUserTime;
        public int systemTime;
        public int userTime;

        public HistoryStepDetails() {
            clear();
        }

        public void clear() {
            this.systemTime = 0;
            this.userTime = 0;
            this.appCpuUid3 = -1;
            this.appCpuUid2 = -1;
            this.appCpuUid1 = -1;
            this.appCpuSTime3 = 0;
            this.appCpuUTime3 = 0;
            this.appCpuSTime2 = 0;
            this.appCpuUTime2 = 0;
            this.appCpuSTime1 = 0;
            this.appCpuUTime1 = 0;
        }

        public void writeToParcel(Parcel out) {
            out.writeInt(this.userTime);
            out.writeInt(this.systemTime);
            out.writeInt(this.appCpuUid1);
            out.writeInt(this.appCpuUTime1);
            out.writeInt(this.appCpuSTime1);
            out.writeInt(this.appCpuUid2);
            out.writeInt(this.appCpuUTime2);
            out.writeInt(this.appCpuSTime2);
            out.writeInt(this.appCpuUid3);
            out.writeInt(this.appCpuUTime3);
            out.writeInt(this.appCpuSTime3);
            out.writeInt(this.statUserTime);
            out.writeInt(this.statSystemTime);
            out.writeInt(this.statIOWaitTime);
            out.writeInt(this.statIrqTime);
            out.writeInt(this.statSoftIrqTime);
            out.writeInt(this.statIdlTime);
            out.writeString(this.statPlatformIdleState);
            out.writeString(this.statSubsystemPowerState);
        }

        public void readFromParcel(Parcel in) {
            this.userTime = in.readInt();
            this.systemTime = in.readInt();
            this.appCpuUid1 = in.readInt();
            this.appCpuUTime1 = in.readInt();
            this.appCpuSTime1 = in.readInt();
            this.appCpuUid2 = in.readInt();
            this.appCpuUTime2 = in.readInt();
            this.appCpuSTime2 = in.readInt();
            this.appCpuUid3 = in.readInt();
            this.appCpuUTime3 = in.readInt();
            this.appCpuSTime3 = in.readInt();
            this.statUserTime = in.readInt();
            this.statSystemTime = in.readInt();
            this.statIOWaitTime = in.readInt();
            this.statIrqTime = in.readInt();
            this.statSoftIrqTime = in.readInt();
            this.statIdlTime = in.readInt();
            this.statPlatformIdleState = in.readString();
            this.statSubsystemPowerState = in.readString();
        }
    }

    public static final class HistoryTag {
        public int poolIdx;
        public String string;
        public int uid;

        public void setTo(HistoryTag o) {
            this.string = o.string;
            this.uid = o.uid;
            this.poolIdx = o.poolIdx;
        }

        public void setTo(String _string, int _uid) {
            this.string = _string;
            this.uid = _uid;
            this.poolIdx = -1;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.string);
            dest.writeInt(this.uid);
        }

        public void readFromParcel(Parcel src) {
            this.string = src.readString();
            this.uid = src.readInt();
            this.poolIdx = -1;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            HistoryTag that = (HistoryTag) o;
            if (this.uid == that.uid && this.string.equals(that.string)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.string.hashCode() * 31) + this.uid;
        }
    }

    public static final class LevelStepTracker {
        public long mLastStepTime = -1;
        public int mNumStepDurations;
        public final long[] mStepDurations;

        public LevelStepTracker(int maxLevelSteps) {
            this.mStepDurations = new long[maxLevelSteps];
        }

        public LevelStepTracker(int numSteps, long[] steps) {
            this.mNumStepDurations = numSteps;
            this.mStepDurations = new long[numSteps];
            System.arraycopy(steps, 0, this.mStepDurations, 0, numSteps);
        }

        public long getDurationAt(int index) {
            return this.mStepDurations[index] & BatteryStats.STEP_LEVEL_TIME_MASK;
        }

        public int getLevelAt(int index) {
            return (int) ((this.mStepDurations[index] & BatteryStats.STEP_LEVEL_LEVEL_MASK) >> 40);
        }

        public int getInitModeAt(int index) {
            return (int) ((this.mStepDurations[index] & BatteryStats.STEP_LEVEL_INITIAL_MODE_MASK) >> 48);
        }

        public int getModModeAt(int index) {
            return (int) ((this.mStepDurations[index] & BatteryStats.STEP_LEVEL_MODIFIED_MODE_MASK) >> 56);
        }

        private void appendHex(long val, int topOffset, StringBuilder out) {
            boolean hasData = false;
            while (topOffset >= 0) {
                int digit = (int) ((val >> topOffset) & 15);
                topOffset -= 4;
                if (hasData || digit != 0) {
                    hasData = true;
                    if (digit < 0 || digit > 9) {
                        out.append((char) ((digit + 97) - 10));
                    } else {
                        out.append((char) (digit + 48));
                    }
                }
            }
        }

        public void encodeEntryAt(int index, StringBuilder out) {
            long item = this.mStepDurations[index];
            long duration = BatteryStats.STEP_LEVEL_TIME_MASK & item;
            int level = (int) ((BatteryStats.STEP_LEVEL_LEVEL_MASK & item) >> 40);
            int initMode = (int) ((BatteryStats.STEP_LEVEL_INITIAL_MODE_MASK & item) >> 48);
            int modMode = (int) ((BatteryStats.STEP_LEVEL_MODIFIED_MODE_MASK & item) >> 56);
            int i = (initMode & 3) + 1;
            if (i == 1) {
                out.append('f');
            } else if (i == 2) {
                out.append('o');
            } else if (i == 3) {
                out.append(DateFormat.DATE);
            } else if (i == 4) {
                out.append(DateFormat.TIME_ZONE);
            }
            if ((initMode & 4) != 0) {
                out.append('p');
            }
            if ((initMode & 8) != 0) {
                out.append('i');
            }
            i = (modMode & 3) + 1;
            if (i == 1) {
                out.append('F');
            } else if (i == 2) {
                out.append('O');
            } else if (i == 3) {
                out.append('D');
            } else if (i == 4) {
                out.append('Z');
            }
            if ((modMode & 4) != 0) {
                out.append('P');
            }
            if ((modMode & 8) != 0) {
                out.append('I');
            }
            out.append('-');
            appendHex((long) level, 4, out);
            out.append('-');
            appendHex(duration, 36, out);
        }

        public void decodeEntryAt(int index, String value) {
            char c;
            char c2;
            long j;
            String str = value;
            int N = value.length();
            int i = 0;
            long out = 0;
            while (true) {
                c = '-';
                if (i >= N) {
                    break;
                }
                char charAt = str.charAt(i);
                char c3 = charAt;
                if (charAt == '-') {
                    break;
                }
                i++;
                if (c3 == 'D') {
                    out |= 144115188075855872L;
                } else if (c3 == 'F') {
                    out |= 0;
                } else if (c3 == 'I') {
                    out |= 576460752303423488L;
                } else if (c3 == 'Z') {
                    out |= 216172782113783808L;
                } else if (c3 == DateFormat.DATE) {
                    out |= 562949953421312L;
                } else if (c3 == 'f') {
                    out |= 0;
                } else if (c3 == 'i') {
                    out |= 2251799813685248L;
                } else if (c3 == DateFormat.TIME_ZONE) {
                    out |= 844424930131968L;
                } else if (c3 == 'O') {
                    out |= 72057594037927936L;
                } else if (c3 == 'P') {
                    out |= 288230376151711744L;
                } else if (c3 == 'o') {
                    out |= 281474976710656L;
                } else if (c3 == 'p') {
                    out |= TrafficStats.PB_IN_BYTES;
                }
            }
            i++;
            long level = 0;
            while (true) {
                c2 = '9';
                j = 4;
                if (i >= N) {
                    break;
                }
                char charAt2 = str.charAt(i);
                char c4 = charAt2;
                if (charAt2 == '-') {
                    break;
                }
                i++;
                level <<= 4;
                charAt2 = c4;
                if (charAt2 >= '0' && charAt2 <= '9') {
                    level += (long) (charAt2 - 48);
                } else if (charAt2 >= DateFormat.AM_PM && charAt2 <= 'f') {
                    level += (long) ((charAt2 - 97) + 10);
                } else if (charAt2 >= DateFormat.CAPITAL_AM_PM && charAt2 <= 'F') {
                    level += (long) ((charAt2 - 65) + 10);
                }
            }
            i++;
            out |= (level << 40) & BatteryStats.STEP_LEVEL_LEVEL_MASK;
            long duration = 0;
            while (i < N) {
                char charAt3 = str.charAt(i);
                char c5 = charAt3;
                if (charAt3 == c) {
                    charAt3 = c5;
                    break;
                }
                i++;
                duration <<= j;
                charAt3 = c5;
                if (charAt3 >= '0' && charAt3 <= c2) {
                    duration += (long) (charAt3 - 48);
                    c = '-';
                    c2 = '9';
                    j = 4;
                } else if (charAt3 < DateFormat.AM_PM || charAt3 > 'f') {
                    if (charAt3 >= DateFormat.CAPITAL_AM_PM) {
                        c = 'F';
                        if (charAt3 <= 'F') {
                            duration += (long) ((charAt3 - 65) + 10);
                            charAt3 = 'F';
                            c = '-';
                            c2 = '9';
                            j = 4;
                        }
                    } else {
                        c = 'F';
                    }
                    charAt3 = c;
                    c = '-';
                    c2 = '9';
                    j = 4;
                } else {
                    duration += (long) ((charAt3 - 97) + 10);
                    c = '-';
                    c2 = '9';
                    j = 4;
                }
            }
            this.mStepDurations[index] = (BatteryStats.STEP_LEVEL_TIME_MASK & duration) | out;
        }

        public void init() {
            this.mLastStepTime = -1;
            this.mNumStepDurations = 0;
        }

        public void clearTime() {
            this.mLastStepTime = -1;
        }

        public long computeTimePerLevel() {
            long[] steps = this.mStepDurations;
            int numSteps = this.mNumStepDurations;
            if (numSteps <= 0) {
                return -1;
            }
            long total = 0;
            for (int i = 0; i < numSteps; i++) {
                total += steps[i] & BatteryStats.STEP_LEVEL_TIME_MASK;
            }
            return total / ((long) numSteps);
        }

        public long computeTimeEstimate(long modesOfInterest, long modeValues, int[] outNumOfInterest) {
            long[] steps = this.mStepDurations;
            int count = this.mNumStepDurations;
            if (count <= 0) {
                return -1;
            }
            long total = 0;
            int numOfInterest = 0;
            for (int i = 0; i < count; i++) {
                long initMode = (steps[i] & BatteryStats.STEP_LEVEL_INITIAL_MODE_MASK) >> 48;
                if ((((steps[i] & BatteryStats.STEP_LEVEL_MODIFIED_MODE_MASK) >> 56) & modesOfInterest) == 0 && (initMode & modesOfInterest) == modeValues) {
                    numOfInterest++;
                    total += steps[i] & BatteryStats.STEP_LEVEL_TIME_MASK;
                }
            }
            if (numOfInterest <= 0) {
                return -1;
            }
            if (outNumOfInterest != null) {
                outNumOfInterest[0] = numOfInterest;
            }
            return (total / ((long) numOfInterest)) * 100;
        }

        public void addLevelSteps(int numStepLevels, long modeBits, long elapsedRealtime) {
            int i = numStepLevels;
            long j = elapsedRealtime;
            int stepCount = this.mNumStepDurations;
            long lastStepTime = this.mLastStepTime;
            if (lastStepTime >= 0 && i > 0) {
                long[] steps = this.mStepDurations;
                long duration = j - lastStepTime;
                for (int i2 = 0; i2 < i; i2++) {
                    System.arraycopy(steps, 0, steps, 1, steps.length - 1);
                    long thisDuration = duration / ((long) (i - i2));
                    duration -= thisDuration;
                    if (thisDuration > BatteryStats.STEP_LEVEL_TIME_MASK) {
                        thisDuration = BatteryStats.STEP_LEVEL_TIME_MASK;
                    }
                    steps[0] = thisDuration | modeBits;
                }
                stepCount += i;
                if (stepCount > steps.length) {
                    stepCount = steps.length;
                }
            }
            this.mNumStepDurations = stepCount;
            this.mLastStepTime = j;
        }

        public void readFromParcel(Parcel in) {
            int N = in.readInt();
            if (N <= this.mStepDurations.length) {
                this.mNumStepDurations = N;
                for (int i = 0; i < N; i++) {
                    this.mStepDurations[i] = in.readLong();
                }
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("more step durations than available: ");
            stringBuilder.append(N);
            throw new ParcelFormatException(stringBuilder.toString());
        }

        public void writeToParcel(Parcel out) {
            int N = this.mNumStepDurations;
            out.writeInt(N);
            for (int i = 0; i < N; i++) {
                out.writeLong(this.mStepDurations[i]);
            }
        }
    }

    public static abstract class LongCounter {
        public abstract long getCountLocked(int i);

        public abstract void logState(Printer printer, String str);
    }

    public static abstract class LongCounterArray {
        public abstract long[] getCountsLocked(int i);

        public abstract void logState(Printer printer, String str);
    }

    public static final class PackageChange {
        public String mPackageName;
        public boolean mUpdate;
        public long mVersionCode;
    }

    public static abstract class Timer {
        @UnsupportedAppUsage
        public abstract int getCountLocked(int i);

        public abstract long getTimeSinceMarkLocked(long j);

        @UnsupportedAppUsage
        public abstract long getTotalTimeLocked(long j, int i);

        public abstract void logState(Printer printer, String str);

        public long getMaxDurationMsLocked(long elapsedRealtimeMs) {
            return -1;
        }

        public long getCurrentDurationMsLocked(long elapsedRealtimeMs) {
            return -1;
        }

        public long getTotalDurationMsLocked(long elapsedRealtimeMs) {
            return -1;
        }

        public Timer getSubTimer() {
            return null;
        }

        public boolean isRunningLocked() {
            return false;
        }
    }

    static final class TimerEntry {
        final int mId;
        final String mName;
        final long mTime;
        final Timer mTimer;

        TimerEntry(String name, int id, Timer timer, long time) {
            this.mName = name;
            this.mId = id;
            this.mTimer = timer;
            this.mTime = time;
        }
    }

    public static abstract class Uid {
        public static final int[] CRITICAL_PROC_STATES = new int[]{0, 3, 4, 1, 2};
        public static final int NUM_PROCESS_STATE = 7;
        public static final int NUM_USER_ACTIVITY_TYPES = USER_ACTIVITY_TYPES.length;
        public static final int NUM_WIFI_BATCHED_SCAN_BINS = 5;
        public static final int PROCESS_STATE_BACKGROUND = 3;
        public static final int PROCESS_STATE_CACHED = 6;
        public static final int PROCESS_STATE_FOREGROUND = 2;
        public static final int PROCESS_STATE_FOREGROUND_SERVICE = 1;
        public static final int PROCESS_STATE_HEAVY_WEIGHT = 5;
        static final String[] PROCESS_STATE_NAMES = new String[]{"Top", "Fg Service", "Foreground", "Background", "Top Sleeping", "Heavy Weight", "Cached"};
        public static final int PROCESS_STATE_TOP = 0;
        public static final int PROCESS_STATE_TOP_SLEEPING = 4;
        @VisibleForTesting
        public static final String[] UID_PROCESS_TYPES = new String[]{"T", "FS", "F", "B", "TS", "HW", "C"};
        static final String[] USER_ACTIVITY_TYPES = new String[]{"other", "button", "touch", Context.ACCESSIBILITY_SERVICE, Context.ATTENTION_SERVICE};

        public class Pid {
            public int mWakeNesting;
            public long mWakeStartMs;
            public long mWakeSumMs;
        }

        public static abstract class Pkg {

            public static abstract class Serv {
                @UnsupportedAppUsage
                public abstract int getLaunches(int i);

                @UnsupportedAppUsage
                public abstract long getStartTime(long j, int i);

                @UnsupportedAppUsage
                public abstract int getStarts(int i);
            }

            @UnsupportedAppUsage
            public abstract ArrayMap<String, ? extends Serv> getServiceStats();

            @UnsupportedAppUsage
            public abstract ArrayMap<String, ? extends Counter> getWakeupAlarmStats();
        }

        public static abstract class Proc {

            public static class ExcessivePower {
                public static final int TYPE_CPU = 2;
                public static final int TYPE_WAKE = 1;
                @UnsupportedAppUsage
                public long overTime;
                @UnsupportedAppUsage
                public int type;
                @UnsupportedAppUsage
                public long usedTime;
            }

            @UnsupportedAppUsage
            public abstract int countExcessivePowers();

            @UnsupportedAppUsage
            public abstract ExcessivePower getExcessivePower(int i);

            @UnsupportedAppUsage
            public abstract long getForegroundTime(int i);

            public abstract int getNumAnrs(int i);

            public abstract int getNumCrashes(int i);

            @UnsupportedAppUsage
            public abstract int getStarts(int i);

            @UnsupportedAppUsage
            public abstract long getSystemTime(int i);

            @UnsupportedAppUsage
            public abstract long getUserTime(int i);

            public abstract boolean isActive();
        }

        public static abstract class Sensor {
            @UnsupportedAppUsage
            public static final int GPS = -10000;

            @UnsupportedAppUsage
            public abstract int getHandle();

            public abstract Timer getSensorBackgroundTime();

            @UnsupportedAppUsage
            public abstract Timer getSensorTime();
        }

        public static abstract class Wakelock {
            @UnsupportedAppUsage
            public abstract Timer getWakeTime(int i);
        }

        public abstract Timer getAggregatedPartialWakelockTimer();

        @UnsupportedAppUsage
        public abstract Timer getAudioTurnedOnTimer();

        public abstract ControllerActivityCounter getBluetoothControllerActivity();

        public abstract Timer getBluetoothScanBackgroundTimer();

        public abstract Counter getBluetoothScanResultBgCounter();

        public abstract Counter getBluetoothScanResultCounter();

        public abstract Timer getBluetoothScanTimer();

        public abstract Timer getBluetoothUnoptimizedScanBackgroundTimer();

        public abstract Timer getBluetoothUnoptimizedScanTimer();

        public abstract Timer getCameraTurnedOnTimer();

        public abstract long getCpuActiveTime();

        public abstract long[] getCpuClusterTimes();

        public abstract long[] getCpuFreqTimes(int i);

        public abstract long[] getCpuFreqTimes(int i, int i2);

        public abstract void getDeferredJobsCheckinLineLocked(StringBuilder stringBuilder, int i);

        public abstract void getDeferredJobsLineLocked(StringBuilder stringBuilder, int i);

        public abstract Timer getFlashlightTurnedOnTimer();

        public abstract Timer getForegroundActivityTimer();

        public abstract Timer getForegroundServiceTimer();

        @UnsupportedAppUsage
        public abstract long getFullWifiLockTime(long j, int i);

        public abstract ArrayMap<String, SparseIntArray> getJobCompletionStats();

        public abstract ArrayMap<String, ? extends Timer> getJobStats();

        public abstract int getMobileRadioActiveCount(int i);

        @UnsupportedAppUsage
        public abstract long getMobileRadioActiveTime(int i);

        public abstract long getMobileRadioApWakeupCount(int i);

        public abstract ControllerActivityCounter getModemControllerActivity();

        public abstract Timer getMulticastWakelockStats();

        @UnsupportedAppUsage
        public abstract long getNetworkActivityBytes(int i, int i2);

        public abstract long getNetworkActivityPackets(int i, int i2);

        @UnsupportedAppUsage
        public abstract ArrayMap<String, ? extends Pkg> getPackageStats();

        public abstract SparseArray<? extends Pid> getPidStats();

        public abstract long getProcessStateTime(int i, long j, int i2);

        public abstract Timer getProcessStateTimer(int i);

        @UnsupportedAppUsage
        public abstract ArrayMap<String, ? extends Proc> getProcessStats();

        public abstract long[] getScreenOffCpuFreqTimes(int i);

        public abstract long[] getScreenOffCpuFreqTimes(int i, int i2);

        @UnsupportedAppUsage
        public abstract SparseArray<? extends Sensor> getSensorStats();

        public abstract ArrayMap<String, ? extends Timer> getSyncStats();

        public abstract long getSystemCpuTimeUs(int i);

        public abstract long getTimeAtCpuSpeed(int i, int i2, int i3);

        @UnsupportedAppUsage
        public abstract int getUid();

        public abstract int getUserActivityCount(int i, int i2);

        public abstract long getUserCpuTimeUs(int i);

        public abstract Timer getVibratorOnTimer();

        @UnsupportedAppUsage
        public abstract Timer getVideoTurnedOnTimer();

        @UnsupportedAppUsage
        public abstract ArrayMap<String, ? extends Wakelock> getWakelockStats();

        public abstract int getWifiBatchedScanCount(int i, int i2);

        @UnsupportedAppUsage
        public abstract long getWifiBatchedScanTime(int i, long j, int i2);

        public abstract ControllerActivityCounter getWifiControllerActivity();

        @UnsupportedAppUsage
        public abstract long getWifiMulticastTime(long j, int i);

        public abstract long getWifiRadioApWakeupCount(int i);

        @UnsupportedAppUsage
        public abstract long getWifiRunningTime(long j, int i);

        public abstract long getWifiScanActualTime(long j);

        public abstract int getWifiScanBackgroundCount(int i);

        public abstract long getWifiScanBackgroundTime(long j);

        public abstract Timer getWifiScanBackgroundTimer();

        public abstract int getWifiScanCount(int i);

        @UnsupportedAppUsage
        public abstract long getWifiScanTime(long j, int i);

        public abstract Timer getWifiScanTimer();

        public abstract boolean hasNetworkActivity();

        public abstract boolean hasUserActivity();

        public abstract void noteActivityPausedLocked(long j);

        public abstract void noteActivityResumedLocked(long j);

        public abstract void noteFullWifiLockAcquiredLocked(long j);

        public abstract void noteFullWifiLockReleasedLocked(long j);

        public abstract void noteUserActivityLocked(int i);

        public abstract void noteWifiBatchedScanStartedLocked(int i, long j);

        public abstract void noteWifiBatchedScanStoppedLocked(long j);

        public abstract void noteWifiMulticastDisabledLocked(long j);

        public abstract void noteWifiMulticastEnabledLocked(long j);

        public abstract void noteWifiRunningLocked(long j);

        public abstract void noteWifiScanStartedLocked(long j);

        public abstract void noteWifiScanStoppedLocked(long j);

        public abstract void noteWifiStoppedLocked(long j);
    }

    public abstract void commitCurrentHistoryBatchLocked();

    @UnsupportedAppUsage
    public abstract long computeBatteryRealtime(long j, int i);

    public abstract long computeBatteryScreenOffRealtime(long j, int i);

    public abstract long computeBatteryScreenOffUptime(long j, int i);

    @UnsupportedAppUsage
    public abstract long computeBatteryTimeRemaining(long j);

    @UnsupportedAppUsage
    public abstract long computeBatteryUptime(long j, int i);

    @UnsupportedAppUsage
    public abstract long computeChargeTimeRemaining(long j);

    public abstract long computeRealtime(long j, int i);

    public abstract long computeUptime(long j, int i);

    public abstract void finishIteratingHistoryLocked();

    public abstract void finishIteratingOldHistoryLocked();

    public abstract long getBatteryRealtime(long j);

    @UnsupportedAppUsage
    public abstract long getBatteryUptime(long j);

    public abstract ControllerActivityCounter getBluetoothControllerActivity();

    public abstract long getBluetoothScanTime(long j, int i);

    public abstract long getCameraOnTime(long j, int i);

    public abstract LevelStepTracker getChargeLevelStepTracker();

    public abstract long[] getCpuFreqs();

    public abstract long getCurrentDailyStartTime();

    public abstract LevelStepTracker getDailyChargeLevelStepTracker();

    public abstract LevelStepTracker getDailyDischargeLevelStepTracker();

    public abstract DailyItem getDailyItemLocked(int i);

    public abstract ArrayList<PackageChange> getDailyPackageChanges();

    public abstract int getDeviceIdleModeCount(int i, int i2);

    public abstract long getDeviceIdleModeTime(int i, long j, int i2);

    public abstract int getDeviceIdlingCount(int i, int i2);

    public abstract long getDeviceIdlingTime(int i, long j, int i2);

    public abstract int getDischargeAmount(int i);

    public abstract int getDischargeAmountScreenDoze();

    public abstract int getDischargeAmountScreenDozeSinceCharge();

    public abstract int getDischargeAmountScreenOff();

    public abstract int getDischargeAmountScreenOffSinceCharge();

    public abstract int getDischargeAmountScreenOn();

    public abstract int getDischargeAmountScreenOnSinceCharge();

    public abstract int getDischargeCurrentLevel();

    public abstract LevelStepTracker getDischargeLevelStepTracker();

    public abstract int getDischargeStartLevel();

    public abstract String getEndPlatformVersion();

    public abstract int getEstimatedBatteryCapacity();

    public abstract long getFlashlightOnCount(int i);

    public abstract long getFlashlightOnTime(long j, int i);

    @UnsupportedAppUsage
    public abstract long getGlobalWifiRunningTime(long j, int i);

    public abstract long getGpsBatteryDrainMaMs();

    public abstract long getGpsSignalQualityTime(int i, long j, int i2);

    public abstract int getHighDischargeAmountSinceCharge();

    public abstract long getHistoryBaseTime();

    public abstract int getHistoryStringPoolBytes();

    public abstract int getHistoryStringPoolSize();

    public abstract String getHistoryTagPoolString(int i);

    public abstract int getHistoryTagPoolUid(int i);

    public abstract int getHistoryTotalSize();

    public abstract int getHistoryUsedSize();

    public abstract long getInteractiveTime(long j, int i);

    public abstract boolean getIsOnBattery();

    public abstract LongSparseArray<? extends Timer> getKernelMemoryStats();

    public abstract Map<String, ? extends Timer> getKernelWakelockStats();

    public abstract long getLongestDeviceIdleModeTime(int i);

    public abstract int getLowDischargeAmountSinceCharge();

    public abstract int getMaxLearnedBatteryCapacity();

    public abstract int getMinLearnedBatteryCapacity();

    public abstract long getMobileRadioActiveAdjustedTime(int i);

    public abstract int getMobileRadioActiveCount(int i);

    public abstract long getMobileRadioActiveTime(long j, int i);

    public abstract int getMobileRadioActiveUnknownCount(int i);

    public abstract long getMobileRadioActiveUnknownTime(int i);

    public abstract ControllerActivityCounter getModemControllerActivity();

    public abstract long getNetworkActivityBytes(int i, int i2);

    public abstract long getNetworkActivityPackets(int i, int i2);

    @UnsupportedAppUsage
    public abstract boolean getNextHistoryLocked(HistoryItem historyItem);

    public abstract long getNextMaxDailyDeadline();

    public abstract long getNextMinDailyDeadline();

    public abstract boolean getNextOldHistoryLocked(HistoryItem historyItem);

    public abstract int getNumConnectivityChange(int i);

    public abstract int getParcelVersion();

    public abstract int getPhoneDataConnectionCount(int i, int i2);

    public abstract long getPhoneDataConnectionTime(int i, long j, int i2);

    public abstract Timer getPhoneDataConnectionTimer(int i);

    public abstract int getPhoneOnCount(int i);

    @UnsupportedAppUsage
    public abstract long getPhoneOnTime(long j, int i);

    public abstract long getPhoneSignalScanningTime(long j, int i);

    public abstract Timer getPhoneSignalScanningTimer();

    public abstract int getPhoneSignalStrengthCount(int i, int i2);

    @UnsupportedAppUsage
    public abstract long getPhoneSignalStrengthTime(int i, long j, int i2);

    public abstract Timer getPhoneSignalStrengthTimer(int i);

    public abstract int getPowerSaveModeEnabledCount(int i);

    public abstract long getPowerSaveModeEnabledTime(long j, int i);

    public abstract Map<String, ? extends Timer> getRpmStats();

    @UnsupportedAppUsage
    public abstract long getScreenBrightnessTime(int i, long j, int i2);

    public abstract Timer getScreenBrightnessTimer(int i);

    public abstract int getScreenDozeCount(int i);

    public abstract long getScreenDozeTime(long j, int i);

    public abstract Map<String, ? extends Timer> getScreenOffRpmStats();

    public abstract int getScreenOnCount(int i);

    @UnsupportedAppUsage
    public abstract long getScreenOnTime(long j, int i);

    public abstract long getStartClockTime();

    public abstract int getStartCount();

    public abstract String getStartPlatformVersion();

    public abstract long getUahDischarge(int i);

    public abstract long getUahDischargeDeepDoze(int i);

    public abstract long getUahDischargeLightDoze(int i);

    public abstract long getUahDischargeScreenDoze(int i);

    public abstract long getUahDischargeScreenOff(int i);

    @UnsupportedAppUsage
    public abstract SparseArray<? extends Uid> getUidStats();

    public abstract Map<String, ? extends Timer> getWakeupReasonStats();

    public abstract long getWifiActiveTime(long j, int i);

    public abstract ControllerActivityCounter getWifiControllerActivity();

    public abstract int getWifiMulticastWakelockCount(int i);

    public abstract long getWifiMulticastWakelockTime(long j, int i);

    @UnsupportedAppUsage
    public abstract long getWifiOnTime(long j, int i);

    public abstract int getWifiSignalStrengthCount(int i, int i2);

    public abstract long getWifiSignalStrengthTime(int i, long j, int i2);

    public abstract Timer getWifiSignalStrengthTimer(int i);

    public abstract int getWifiStateCount(int i, int i2);

    public abstract long getWifiStateTime(int i, long j, int i2);

    public abstract Timer getWifiStateTimer(int i);

    public abstract int getWifiSupplStateCount(int i, int i2);

    public abstract long getWifiSupplStateTime(int i, long j, int i2);

    public abstract Timer getWifiSupplStateTimer(int i);

    public abstract boolean hasBluetoothActivityReporting();

    public abstract boolean hasModemActivityReporting();

    public abstract boolean hasWifiActivityReporting();

    @UnsupportedAppUsage
    public abstract boolean startIteratingHistoryLocked();

    public abstract boolean startIteratingOldHistoryLocked();

    public abstract void writeToParcelWithoutUids(Parcel parcel, int i);

    static {
        String str = "light";
        SCREEN_BRIGHTNESS_NAMES = new String[]{"dark", "dim", "medium", str, "bright"};
        String str2 = "4";
        String str3 = "3";
        String str4 = "2";
        String str5 = "1";
        String str6 = "0";
        SCREEN_BRIGHTNESS_SHORT_NAMES = new String[]{str6, str5, str4, str3, str2};
        BitDescription[] bitDescriptionArr = new BitDescription[18];
        bitDescriptionArr[0] = new BitDescription(Integer.MIN_VALUE, "running", "r");
        bitDescriptionArr[1] = new BitDescription(1073741824, "wake_lock", BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_W);
        bitDescriptionArr[2] = new BitDescription(8388608, Context.SENSOR_SERVICE, RemoteDevice.KEY_STATUS);
        bitDescriptionArr[3] = new BitDescription(536870912, LocationManager.GPS_PROVIDER, "g");
        bitDescriptionArr[4] = new BitDescription(268435456, "wifi_full_lock", "Wl");
        bitDescriptionArr[5] = new BitDescription(134217728, "wifi_scan", "Ws");
        bitDescriptionArr[6] = new BitDescription(65536, "wifi_multicast", "Wm");
        bitDescriptionArr[7] = new BitDescription(67108864, "wifi_radio", "Wr");
        bitDescriptionArr[8] = new BitDescription(33554432, "mobile_radio", "Pr");
        bitDescriptionArr[9] = new BitDescription(2097152, "phone_scanning", "Psc");
        bitDescriptionArr[10] = new BitDescription(4194304, "audio", FullBackup.APK_TREE_TOKEN);
        bitDescriptionArr[11] = new BitDescription(1048576, "screen", "S");
        bitDescriptionArr[12] = new BitDescription(524288, BatteryManager.EXTRA_PLUGGED, "BP");
        bitDescriptionArr[13] = new BitDescription(262144, "screen_doze", "Sd");
        String[] strArr = DATA_CONNECTION_NAMES;
        bitDescriptionArr[14] = new BitDescription(HistoryItem.STATE_DATA_CONNECTION_MASK, 9, "data_conn", "Pcn", strArr, strArr);
        String str7 = "off";
        bitDescriptionArr[15] = new BitDescription(448, 6, "phone_state", "Pst", new String[]{"in", "out", PhoneConstants.APN_TYPE_EMERGENCY, str7}, new String[]{"in", "out", "em", str7});
        bitDescriptionArr[16] = new BitDescription(56, 3, "phone_signal_strength", "Pss", SignalStrength.SIGNAL_STRENGTH_NAMES, new String[]{"0", "1", "2", "3", "4", "5"});
        bitDescriptionArr[17] = new BitDescription(7, 0, "brightness", "Sb", SCREEN_BRIGHTNESS_NAMES, SCREEN_BRIGHTNESS_SHORT_NAMES);
        HISTORY_STATE_DESCRIPTIONS = bitDescriptionArr;
        BitDescription[] bitDescriptionArr2 = new BitDescription[16];
        bitDescriptionArr2[0] = new BitDescription(Integer.MIN_VALUE, "power_save", "ps");
        bitDescriptionArr2[1] = new BitDescription(1073741824, "video", BaseMmsColumns.MMS_VERSION);
        bitDescriptionArr2[2] = new BitDescription(536870912, "wifi_running", "Ww");
        bitDescriptionArr2[3] = new BitDescription(268435456, "wifi", "W");
        bitDescriptionArr2[4] = new BitDescription(134217728, "flashlight", "fl");
        bitDescriptionArr2[5] = new BitDescription(HistoryItem.STATE2_DEVICE_IDLE_MASK, 25, "device_idle", "di", new String[]{str7, str, "full", "???"}, new String[]{str7, str, "full", "???"});
        bitDescriptionArr2[6] = new BitDescription(16777216, "charging", "ch");
        bitDescriptionArr2[7] = new BitDescription(262144, "usb_data", "Ud");
        bitDescriptionArr2[8] = new BitDescription(8388608, "phone_in_call", "Pcl");
        bitDescriptionArr2[9] = new BitDescription(4194304, "bluetooth", "b");
        bitDescriptionArr2[10] = new BitDescription(112, 4, "wifi_signal_strength", "Wss", new String[]{str6, str5, str4, str3, str2}, new String[]{str6, str5, str4, str3, str2});
        bitDescriptionArr2[11] = new BitDescription(15, 0, "wifi_suppl", "Wsp", WIFI_SUPPL_STATE_NAMES, WIFI_SUPPL_STATE_SHORT_NAMES);
        bitDescriptionArr2[12] = new BitDescription(2097152, Context.CAMERA_SERVICE, "ca");
        bitDescriptionArr2[13] = new BitDescription(1048576, "ble_scan", "bles");
        bitDescriptionArr2[14] = new BitDescription(524288, "cellular_high_tx_power", "Chtp");
        bitDescriptionArr2[15] = new BitDescription(128, 7, "gps_signal_quality", "Gss", new String[]{"poor", "good"}, new String[]{"poor", "good"});
        HISTORY_STATE2_DESCRIPTIONS = bitDescriptionArr2;
        r0 = new IntToString[22];
        IntToString intToString = sUidToString;
        r0[0] = intToString;
        r0[1] = intToString;
        r0[2] = intToString;
        r0[3] = intToString;
        r0[4] = intToString;
        r0[5] = intToString;
        r0[6] = intToString;
        r0[7] = intToString;
        r0[8] = intToString;
        r0[9] = intToString;
        r0[10] = intToString;
        IntToString intToString2 = sIntToString;
        r0[11] = intToString2;
        r0[12] = intToString;
        r0[13] = intToString;
        r0[14] = intToString;
        r0[15] = intToString;
        r0[16] = intToString;
        r0[17] = intToString;
        r0[18] = intToString;
        r0[19] = intToString;
        r0[20] = intToString;
        r0[21] = intToString2;
        HISTORY_EVENT_INT_FORMATTERS = r0;
    }

    public static int mapToInternalProcessState(int procState) {
        if (procState == 21) {
            return 21;
        }
        if (procState == 2) {
            return 0;
        }
        if (ActivityManager.isForegroundService(procState)) {
            return 1;
        }
        if (procState <= 7) {
            return 2;
        }
        if (procState <= 12) {
            return 3;
        }
        if (procState <= 13) {
            return 4;
        }
        if (procState <= 14) {
            return 5;
        }
        return 6;
    }

    private static final void formatTimeRaw(StringBuilder out, long seconds) {
        long days = seconds / 86400;
        if (days != 0) {
            out.append(days);
            out.append("d ");
        }
        long used = ((days * 60) * 60) * 24;
        long hours = (seconds - used) / 3600;
        if (!(hours == 0 && used == 0)) {
            out.append(hours);
            out.append("h ");
        }
        used += (hours * 60) * 60;
        long mins = (seconds - used) / 60;
        if (!(mins == 0 && used == 0)) {
            out.append(mins);
            out.append("m ");
        }
        used += 60 * mins;
        if (seconds != 0 || used != 0) {
            out.append(seconds - used);
            out.append("s ");
        }
    }

    public static final void formatTimeMs(StringBuilder sb, long time) {
        long sec = time / 1000;
        formatTimeRaw(sb, sec);
        sb.append(time - (1000 * sec));
        sb.append("ms ");
    }

    public static final void formatTimeMsNoSpace(StringBuilder sb, long time) {
        long sec = time / 1000;
        formatTimeRaw(sb, sec);
        sb.append(time - (1000 * sec));
        sb.append("ms");
    }

    public final String formatRatioLocked(long num, long den) {
        if (den == 0) {
            return "--%";
        }
        float perc = (((float) num) / ((float) den)) * 100.0f;
        this.mFormatBuilder.setLength(0);
        this.mFormatter.format("%.1f%%", new Object[]{Float.valueOf(perc)});
        return this.mFormatBuilder.toString();
    }

    /* Access modifiers changed, original: final */
    public final String formatBytesLocked(long bytes) {
        this.mFormatBuilder.setLength(0);
        if (bytes < 1024) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(bytes);
            stringBuilder.append("B");
            return stringBuilder.toString();
        } else if (bytes < 1048576) {
            this.mFormatter.format("%.2fKB", new Object[]{Double.valueOf(((double) bytes) / 1024.0d)});
            return this.mFormatBuilder.toString();
        } else if (bytes < 1073741824) {
            this.mFormatter.format("%.2fMB", new Object[]{Double.valueOf(((double) bytes) / 1048576.0d)});
            return this.mFormatBuilder.toString();
        } else {
            this.mFormatter.format("%.2fGB", new Object[]{Double.valueOf(((double) bytes) / 1.073741824E9d)});
            return this.mFormatBuilder.toString();
        }
    }

    private static long roundUsToMs(long timeUs) {
        return (500 + timeUs) / 1000;
    }

    private static long computeWakeLock(Timer timer, long elapsedRealtimeUs, int which) {
        if (timer != null) {
            return (500 + timer.getTotalTimeLocked(elapsedRealtimeUs, which)) / 1000;
        }
        return 0;
    }

    private static final String printWakeLock(StringBuilder sb, Timer timer, long elapsedRealtimeUs, String name, int which, String linePrefix) {
        StringBuilder stringBuilder = sb;
        Timer timer2 = timer;
        long j = elapsedRealtimeUs;
        String str = name;
        int i = which;
        String str2 = linePrefix;
        if (timer2 != null) {
            long totalTimeMillis = computeWakeLock(timer2, j, i);
            int count = timer2.getCountLocked(i);
            if (totalTimeMillis != 0) {
                stringBuilder.append(str2);
                formatTimeMs(stringBuilder, totalTimeMillis);
                if (str != null) {
                    stringBuilder.append(str);
                    stringBuilder.append(' ');
                }
                stringBuilder.append('(');
                stringBuilder.append(count);
                stringBuilder.append(" times)");
                long maxDurationMs = timer2.getMaxDurationMsLocked(j / 1000);
                if (maxDurationMs >= 0) {
                    stringBuilder.append(" max=");
                    stringBuilder.append(maxDurationMs);
                }
                long totalDurMs = timer2.getTotalDurationMsLocked(j / 1000);
                if (totalDurMs > totalTimeMillis) {
                    stringBuilder.append(" actual=");
                    stringBuilder.append(totalDurMs);
                }
                if (timer.isRunningLocked()) {
                    long currentMs = timer2.getCurrentDurationMsLocked(j / 1000);
                    if (currentMs >= 0) {
                        stringBuilder.append(" (running for ");
                        stringBuilder.append(currentMs);
                        stringBuilder.append("ms)");
                    } else {
                        stringBuilder.append(" (running)");
                    }
                }
                return ", ";
            }
        }
        return str2;
    }

    private static final boolean printTimer(PrintWriter pw, StringBuilder sb, Timer timer, long rawRealtimeUs, int which, String prefix, String type) {
        StringBuilder stringBuilder = sb;
        Timer timer2 = timer;
        PrintWriter printWriter;
        String str;
        String str2;
        if (timer2 != null) {
            long totalTimeMs = (timer.getTotalTimeLocked(rawRealtimeUs, which) + 500) / 1000;
            int count = timer2.getCountLocked(which);
            if (totalTimeMs != 0) {
                stringBuilder.setLength(0);
                stringBuilder.append(prefix);
                stringBuilder.append("    ");
                stringBuilder.append(type);
                stringBuilder.append(": ");
                formatTimeMs(stringBuilder, totalTimeMs);
                stringBuilder.append("realtime (");
                stringBuilder.append(count);
                stringBuilder.append(" times)");
                long maxDurationMs = timer2.getMaxDurationMsLocked(rawRealtimeUs / 1000);
                if (maxDurationMs >= 0) {
                    stringBuilder.append(" max=");
                    stringBuilder.append(maxDurationMs);
                }
                if (timer.isRunningLocked()) {
                    long currentMs = timer2.getCurrentDurationMsLocked(rawRealtimeUs / 1000);
                    if (currentMs >= 0) {
                        stringBuilder.append(" (running for ");
                        stringBuilder.append(currentMs);
                        stringBuilder.append("ms)");
                    } else {
                        stringBuilder.append(" (running)");
                    }
                }
                printWriter = pw;
                pw.println(sb.toString());
                return true;
            }
            printWriter = pw;
            str = prefix;
            str2 = type;
        } else {
            printWriter = pw;
            int i = which;
            str = prefix;
            str2 = type;
        }
        return false;
    }

    private static final String printWakeLockCheckin(StringBuilder sb, Timer timer, long elapsedRealtimeUs, String name, int which, String linePrefix) {
        long totalTimeMicros;
        String stringBuilder;
        StringBuilder stringBuilder2 = sb;
        Timer timer2 = timer;
        long j = elapsedRealtimeUs;
        String str = name;
        int i = which;
        int count = 0;
        long max = 0;
        long current = 0;
        long totalDuration = 0;
        if (timer2 != null) {
            long totalTimeMicros2 = timer2.getTotalTimeLocked(j, i);
            count = timer2.getCountLocked(i);
            totalTimeMicros = totalTimeMicros2;
            current = timer2.getCurrentDurationMsLocked(j / 1000);
            max = timer2.getMaxDurationMsLocked(j / 1000);
            totalDuration = timer2.getTotalDurationMsLocked(j / 1000);
        } else {
            totalTimeMicros = 0;
        }
        stringBuilder2.append(linePrefix);
        stringBuilder2.append((totalTimeMicros + 500) / 1000);
        stringBuilder2.append(',');
        String str2 = ",";
        if (str != null) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str);
            stringBuilder3.append(str2);
            stringBuilder = stringBuilder3.toString();
        } else {
            stringBuilder = "";
        }
        stringBuilder2.append(stringBuilder);
        stringBuilder2.append(count);
        stringBuilder2.append(',');
        stringBuilder2.append(current);
        stringBuilder2.append(',');
        stringBuilder2.append(max);
        if (str != null) {
            stringBuilder2.append(',');
            stringBuilder2.append(totalDuration);
        }
        return str2;
    }

    private static final void dumpLineHeader(PrintWriter pw, int uid, String category, String type) {
        pw.print(9);
        pw.print(',');
        pw.print(uid);
        pw.print(',');
        pw.print(category);
        pw.print(',');
        pw.print(type);
    }

    @UnsupportedAppUsage
    private static final void dumpLine(PrintWriter pw, int uid, String category, String type, Object... args) {
        dumpLineHeader(pw, uid, category, type);
        for (Object arg : args) {
            pw.print(',');
            pw.print(arg);
        }
        pw.println();
    }

    private static final void dumpTimer(PrintWriter pw, int uid, String category, String type, Timer timer, long rawRealtime, int which) {
        if (timer != null) {
            long totalTime = roundUsToMs(timer.getTotalTimeLocked(rawRealtime, which));
            int count = timer.getCountLocked(which);
            if (totalTime != 0 || count != 0) {
                dumpLine(pw, uid, category, type, Long.valueOf(totalTime), Integer.valueOf(count));
            }
        }
    }

    private static void dumpTimer(ProtoOutputStream proto, long fieldId, Timer timer, long rawRealtimeUs, int which) {
        ProtoOutputStream protoOutputStream = proto;
        Timer timer2 = timer;
        if (timer2 != null) {
            long timeMs = roundUsToMs(timer.getTotalTimeLocked(rawRealtimeUs, which));
            int count = timer2.getCountLocked(which);
            long maxDurationMs = timer2.getMaxDurationMsLocked(rawRealtimeUs / 1000);
            long curDurationMs = timer2.getCurrentDurationMsLocked(rawRealtimeUs / 1000);
            long totalDurationMs = timer2.getTotalDurationMsLocked(rawRealtimeUs / 1000);
            if (!(timeMs == 0 && count == 0 && maxDurationMs == -1 && curDurationMs == -1 && totalDurationMs == -1)) {
                long token = proto.start(fieldId);
                protoOutputStream.write(1112396529665L, timeMs);
                protoOutputStream.write(1112396529666L, count);
                if (maxDurationMs != -1) {
                    protoOutputStream.write(1112396529667L, maxDurationMs);
                }
                if (curDurationMs != -1) {
                    protoOutputStream.write(1112396529668L, curDurationMs);
                }
                if (totalDurationMs != -1) {
                    protoOutputStream.write(1112396529669L, totalDurationMs);
                }
                protoOutputStream.end(token);
            }
        }
    }

    private static boolean controllerActivityHasData(ControllerActivityCounter counter, int which) {
        if (counter == null) {
            return false;
        }
        if (counter.getIdleTimeCounter().getCountLocked(which) != 0 || counter.getRxTimeCounter().getCountLocked(which) != 0 || counter.getPowerCounter().getCountLocked(which) != 0 || counter.getMonitoredRailChargeConsumedMaMs().getCountLocked(which) != 0) {
            return true;
        }
        for (LongCounter c : counter.getTxTimeCounters()) {
            if (c.getCountLocked(which) != 0) {
                return true;
            }
        }
        return false;
    }

    private static final void dumpControllerActivityLine(PrintWriter pw, int uid, String category, String type, ControllerActivityCounter counter, int which) {
        if (controllerActivityHasData(counter, which)) {
            dumpLineHeader(pw, uid, category, type);
            String str = ",";
            pw.print(str);
            pw.print(counter.getIdleTimeCounter().getCountLocked(which));
            pw.print(str);
            pw.print(counter.getRxTimeCounter().getCountLocked(which));
            pw.print(str);
            pw.print(((double) counter.getPowerCounter().getCountLocked(which)) / 3600000.0d);
            pw.print(str);
            pw.print(((double) counter.getMonitoredRailChargeConsumedMaMs().getCountLocked(which)) / 3600000.0d);
            for (LongCounter c : counter.getTxTimeCounters()) {
                pw.print(str);
                pw.print(c.getCountLocked(which));
            }
            pw.println();
        }
    }

    private static void dumpControllerActivityProto(ProtoOutputStream proto, long fieldId, ControllerActivityCounter counter, int which) {
        if (controllerActivityHasData(counter, which)) {
            long cToken = proto.start(fieldId);
            proto.write(1112396529665L, counter.getIdleTimeCounter().getCountLocked(which));
            proto.write(1112396529666L, counter.getRxTimeCounter().getCountLocked(which));
            proto.write(1112396529667L, ((double) counter.getPowerCounter().getCountLocked(which)) / 3600000.0d);
            proto.write(1103806595077L, ((double) counter.getMonitoredRailChargeConsumedMaMs().getCountLocked(which)) / 3600000.0d);
            LongCounter[] txCounters = counter.getTxTimeCounters();
            for (int i = 0; i < txCounters.length; i++) {
                LongCounter c = txCounters[i];
                long tToken = proto.start(2246267895812L);
                proto.write(1120986464257L, i);
                proto.write(1112396529666L, c.getCountLocked(which));
                proto.end(tToken);
            }
            proto.end(cToken);
        }
    }

    private final void printControllerActivityIfInteresting(PrintWriter pw, StringBuilder sb, String prefix, String controllerName, ControllerActivityCounter counter, int which) {
        if (controllerActivityHasData(counter, which)) {
            printControllerActivity(pw, sb, prefix, controllerName, counter, which);
        }
    }

    private final void printControllerActivity(PrintWriter pw, StringBuilder sb, String prefix, String controllerName, ControllerActivityCounter counter, int which) {
        int length;
        String str;
        long rxTimeMs;
        String str2;
        String[] powerLevel;
        long powerDrainMaMs;
        PrintWriter printWriter = pw;
        StringBuilder stringBuilder = sb;
        String str3 = controllerName;
        int i = which;
        long idleTimeMs = counter.getIdleTimeCounter().getCountLocked(i);
        long rxTimeMs2 = counter.getRxTimeCounter().getCountLocked(i);
        long powerDrainMaMs2 = counter.getPowerCounter().getCountLocked(i);
        long monitoredRailChargeConsumedMaMs = counter.getMonitoredRailChargeConsumedMaMs().getCountLocked(i);
        long totalControllerActivityTimeMs = computeBatteryRealtime(SystemClock.elapsedRealtime() * 1000, i) / 1000;
        long totalTxTimeMs = 0;
        LongCounter[] txTimeCounters = counter.getTxTimeCounters();
        long monitoredRailChargeConsumedMaMs2 = monitoredRailChargeConsumedMaMs;
        int i2 = 0;
        for (length = txTimeCounters.length; i2 < length; length = length) {
            totalTxTimeMs += txTimeCounters[i2].getCountLocked(i);
            i2++;
        }
        String str4 = " Sleep time:  ";
        String str5 = ")";
        long powerDrainMaMs3 = powerDrainMaMs2;
        String str6 = "(";
        String str7 = "     ";
        if (str3.equals(WIFI_CONTROLLER_NAME)) {
            String str8 = str4;
            monitoredRailChargeConsumedMaMs = counter.getScanTimeCounter().getCountLocked(i);
            stringBuilder.setLength(0);
            sb.append(prefix);
            stringBuilder.append(str7);
            stringBuilder.append(str3);
            stringBuilder.append(" Scan time:  ");
            formatTimeMs(stringBuilder, monitoredRailChargeConsumedMaMs);
            stringBuilder.append(str6);
            stringBuilder.append(formatRatioLocked(monitoredRailChargeConsumedMaMs, totalControllerActivityTimeMs));
            stringBuilder.append(str5);
            printWriter.println(sb.toString());
            monitoredRailChargeConsumedMaMs = totalControllerActivityTimeMs - ((idleTimeMs + rxTimeMs2) + totalTxTimeMs);
            stringBuilder.setLength(0);
            sb.append(prefix);
            stringBuilder.append(str7);
            stringBuilder.append(str3);
            str = str8;
            stringBuilder.append(str);
            formatTimeMs(stringBuilder, monitoredRailChargeConsumedMaMs);
            stringBuilder.append(str6);
            rxTimeMs = rxTimeMs2;
            stringBuilder.append(formatRatioLocked(monitoredRailChargeConsumedMaMs, totalControllerActivityTimeMs));
            stringBuilder.append(str5);
            printWriter.println(sb.toString());
        } else {
            rxTimeMs = rxTimeMs2;
            str = str4;
        }
        String str9 = CELLULAR_CONTROLLER_NAME;
        if (str3.equals(str9)) {
            length = which;
            str4 = str9;
            rxTimeMs2 = counter.getSleepTimeCounter().getCountLocked(length);
            str2 = str4;
            stringBuilder.setLength(0);
            sb.append(prefix);
            stringBuilder.append(str7);
            stringBuilder.append(str3);
            stringBuilder.append(str);
            formatTimeMs(stringBuilder, rxTimeMs2);
            stringBuilder.append(str6);
            stringBuilder.append(formatRatioLocked(rxTimeMs2, totalControllerActivityTimeMs));
            stringBuilder.append(str5);
            printWriter.println(sb.toString());
        } else {
            length = which;
            str2 = str9;
        }
        stringBuilder.setLength(0);
        sb.append(prefix);
        stringBuilder.append(str7);
        stringBuilder.append(str3);
        stringBuilder.append(" Idle time:   ");
        formatTimeMs(stringBuilder, idleTimeMs);
        stringBuilder.append(str6);
        stringBuilder.append(formatRatioLocked(idleTimeMs, totalControllerActivityTimeMs));
        stringBuilder.append(str5);
        printWriter.println(sb.toString());
        stringBuilder.setLength(0);
        sb.append(prefix);
        stringBuilder.append(str7);
        stringBuilder.append(str3);
        stringBuilder.append(" Rx time:     ");
        rxTimeMs2 = rxTimeMs;
        formatTimeMs(stringBuilder, rxTimeMs2);
        stringBuilder.append(str6);
        stringBuilder.append(formatRatioLocked(rxTimeMs2, totalControllerActivityTimeMs));
        stringBuilder.append(str5);
        printWriter.println(sb.toString());
        stringBuilder.setLength(0);
        sb.append(prefix);
        stringBuilder.append(str7);
        stringBuilder.append(str3);
        stringBuilder.append(" Tx time:     ");
        Object obj = (controllerName.hashCode() == -851952246 && str3.equals(str2)) ? null : -1;
        if (obj != null) {
            powerLevel = new String[]{"[0]", "[1]", "[2]", "[3]", "[4]"};
        } else {
            long j = rxTimeMs2;
            powerLevel = new String[]{"   less than 0dBm: ", "   0dBm to 8dBm: ", "   8dBm to 15dBm: ", "   15dBm to 20dBm: ", "   above 20dBm: "};
        }
        int numTxLvls = Math.min(counter.getTxTimeCounters().length, powerLevel.length);
        if (numTxLvls > 1) {
            printWriter.println(sb.toString());
            for (int lvl = 0; lvl < numTxLvls; lvl++) {
                rxTimeMs2 = counter.getTxTimeCounters()[lvl].getCountLocked(length);
                stringBuilder.setLength(0);
                sb.append(prefix);
                stringBuilder.append("    ");
                stringBuilder.append(powerLevel[lvl]);
                stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                formatTimeMs(stringBuilder, rxTimeMs2);
                stringBuilder.append(str6);
                stringBuilder.append(formatRatioLocked(rxTimeMs2, totalControllerActivityTimeMs));
                stringBuilder.append(str5);
                printWriter.println(sb.toString());
            }
        } else {
            long txLvlTimeMs = counter.getTxTimeCounters()[0].getCountLocked(length);
            formatTimeMs(stringBuilder, txLvlTimeMs);
            stringBuilder.append(str6);
            stringBuilder.append(formatRatioLocked(txLvlTimeMs, totalControllerActivityTimeMs));
            stringBuilder.append(str5);
            printWriter.println(sb.toString());
        }
        if (powerDrainMaMs3 > 0) {
            stringBuilder.setLength(0);
            sb.append(prefix);
            stringBuilder.append(str7);
            stringBuilder.append(str3);
            stringBuilder.append(" Battery drain: ");
            powerDrainMaMs = powerDrainMaMs3;
            stringBuilder.append(BatteryStatsHelper.makemAh(((double) powerDrainMaMs) / 3600000.0d));
            stringBuilder.append("mAh");
            printWriter.println(sb.toString());
        } else {
            powerDrainMaMs = powerDrainMaMs3;
        }
        if (monitoredRailChargeConsumedMaMs2 > 0) {
            stringBuilder.setLength(0);
            sb.append(prefix);
            stringBuilder.append(str7);
            stringBuilder.append(str3);
            stringBuilder.append(" Monitored rail energy drain: ");
            powerDrainMaMs3 = powerDrainMaMs;
            stringBuilder.append(new DecimalFormat("#.##").format(((double) monitoredRailChargeConsumedMaMs2) / 3600000.0d));
            stringBuilder.append(" mAh");
            printWriter.println(sb.toString());
            return;
        }
        String[] strArr = powerLevel;
        long j2 = monitoredRailChargeConsumedMaMs2;
    }

    public final void dumpCheckinLocked(Context context, PrintWriter pw, int which, int reqUid) {
        dumpCheckinLocked(context, pw, which, reqUid, BatteryStatsHelper.checkWifiOnly(context));
    }

    public final void dumpCheckinLocked(Context context, PrintWriter pw, int which, int reqUid, boolean wifiOnly) {
        PrintWriter printWriter = pw;
        int i = which;
        int i2 = reqUid;
        Integer rawRealtime = Integer.valueOf(0);
        String str;
        Object[] objArr;
        StringBuilder stringBuilder;
        if (i != 0) {
            str = STAT_NAMES[i];
            objArr = new Object[1];
            stringBuilder = new StringBuilder();
            stringBuilder.append("ERROR: BatteryStats.dumpCheckin called for which type ");
            stringBuilder.append(i);
            stringBuilder.append(" but only STATS_SINCE_CHARGED is supported.");
            objArr[0] = stringBuilder.toString();
            dumpLine(printWriter, 0, str, Notification.CATEGORY_ERROR, objArr);
            return;
        }
        SparseArray<? extends Uid> uidStats;
        int i3;
        int iw;
        int NU;
        String str2;
        Object[] args;
        SparseArray<? extends Uid> uidStats2;
        long rawRealtime2;
        Object obj;
        long totalTimeMicros;
        Timer screenOffTimer;
        boolean z;
        List<BatterySipper> sippers;
        Map<String, ? extends Timer> screenOffRpmStats;
        int uid;
        String label;
        long rawUptime = SystemClock.uptimeMillis() * 1000;
        long rawRealtimeMs = SystemClock.elapsedRealtime();
        long rawRealtime3 = rawRealtimeMs * 1000;
        long batteryUptime = getBatteryUptime(rawUptime);
        long whichBatteryUptime = computeBatteryUptime(rawUptime, i);
        long whichBatteryRealtime = computeBatteryRealtime(rawRealtime3, i);
        long whichBatteryScreenOffUptime = computeBatteryScreenOffUptime(rawUptime, i);
        long whichBatteryScreenOffRealtime = computeBatteryScreenOffRealtime(rawRealtime3, i);
        long totalRealtime = computeRealtime(rawRealtime3, i);
        long totalUptime = computeUptime(rawUptime, i);
        long screenOnTime = getScreenOnTime(rawRealtime3, i);
        long screenDozeTime = getScreenDozeTime(rawRealtime3, i);
        long interactiveTime = getInteractiveTime(rawRealtime3, i);
        long powerSaveModeEnabledTime = getPowerSaveModeEnabledTime(rawRealtime3, i);
        long deviceIdleModeLightTime = getDeviceIdleModeTime(1, rawRealtime3, i);
        long batteryUptime2 = batteryUptime;
        long deviceIdleModeFullTime = getDeviceIdleModeTime(2, rawRealtime3, i);
        long deviceLightIdlingTime = getDeviceIdlingTime(1, rawRealtime3, i);
        long deviceIdlingTime = getDeviceIdlingTime(2, rawRealtime3, i);
        int connChanges = getNumConnectivityChange(i);
        long phoneOnTime = getPhoneOnTime(rawRealtime3, i);
        long dischargeCount = getUahDischarge(i);
        long dischargeScreenOffCount = getUahDischargeScreenOff(i);
        long dischargeScreenDozeCount = getUahDischargeScreenDoze(i);
        long dischargeLightDozeCount = getUahDischargeLightDoze(i);
        long dischargeDeepDozeCount = getUahDischargeDeepDoze(i);
        StringBuilder sb = new StringBuilder(128);
        SparseArray<? extends Uid> uidStats3 = getUidStats();
        StringBuilder sb2 = sb;
        int NU2 = uidStats3.size();
        String category = STAT_NAMES[i];
        Object[] objArr2 = new Object[12];
        objArr2[0] = i == 0 ? Integer.valueOf(getStartCount()) : "N/A";
        objArr2[1] = Long.valueOf(whichBatteryRealtime / 1000);
        objArr2[2] = Long.valueOf(whichBatteryUptime / 1000);
        objArr2[3] = Long.valueOf(totalRealtime / 1000);
        objArr2[4] = Long.valueOf(totalUptime / 1000);
        objArr2[5] = Long.valueOf(getStartClockTime());
        objArr2[6] = Long.valueOf(whichBatteryScreenOffRealtime / 1000);
        objArr2[7] = Long.valueOf(whichBatteryScreenOffUptime / 1000);
        objArr2[8] = Integer.valueOf(getEstimatedBatteryCapacity());
        objArr2[9] = Integer.valueOf(getMinLearnedBatteryCapacity());
        objArr2[10] = Integer.valueOf(getMaxLearnedBatteryCapacity());
        objArr2[11] = Long.valueOf(screenDozeTime / 1000);
        long rawRealtimeMs2 = rawRealtimeMs;
        dumpLine(printWriter, 0, category, BATTERY_DATA, objArr2);
        int iu = 0;
        long partialWakeLockTimeTotal = 0;
        long fullWakeLockTimeTotal = 0;
        while (iu < NU2) {
            Uid u;
            Uid u2 = (Uid) uidStats3.valueAt(iu);
            ArrayMap<String, ? extends Wakelock> wakelocks = u2.getWakelockStats();
            uidStats = uidStats3;
            i3 = 1;
            iw = wakelocks.size() - 1;
            while (iw >= 0) {
                NU = NU2;
                Wakelock NU3 = (Wakelock) wakelocks.valueAt(iw);
                u = u2;
                Timer fullWakeTimer = NU3.getWakeTime(i3);
                if (fullWakeTimer != null) {
                    fullWakeLockTimeTotal += fullWakeTimer.getTotalTimeLocked(rawRealtime3, i);
                }
                fullWakeTimer = NU3.getWakeTime(0);
                if (fullWakeTimer != null) {
                    partialWakeLockTimeTotal += fullWakeTimer.getTotalTimeLocked(rawRealtime3, i);
                }
                iw--;
                NU2 = NU;
                u2 = u;
                i3 = 1;
            }
            u = u2;
            iu++;
            uidStats3 = uidStats;
        }
        uidStats = uidStats3;
        NU = NU2;
        long mobileRxTotalBytes = getNetworkActivityBytes(0, i);
        long mobileTxTotalBytes = getNetworkActivityBytes(1, i);
        long wifiRxTotalBytes = getNetworkActivityBytes(2, i);
        long wifiTxTotalBytes = getNetworkActivityBytes(3, i);
        long mobileRxTotalPackets = getNetworkActivityPackets(0, i);
        long mobileTxTotalPackets = getNetworkActivityPackets(1, i);
        long wifiRxTotalPackets = getNetworkActivityPackets(2, i);
        long wifiTxTotalPackets = getNetworkActivityPackets(3, i);
        long btRxTotalBytes = getNetworkActivityBytes(4, i);
        long btTxTotalBytes = getNetworkActivityBytes(5, i);
        dumpLine(printWriter, 0, category, GLOBAL_NETWORK_DATA, Long.valueOf(mobileRxTotalBytes), Long.valueOf(mobileTxTotalBytes), Long.valueOf(wifiRxTotalBytes), Long.valueOf(wifiTxTotalBytes), Long.valueOf(mobileRxTotalPackets), Long.valueOf(mobileTxTotalPackets), Long.valueOf(wifiRxTotalPackets), Long.valueOf(wifiTxTotalPackets), Long.valueOf(btRxTotalBytes), Long.valueOf(btTxTotalBytes));
        long batteryUptime3 = batteryUptime2;
        SparseArray<? extends Uid> uidStats4 = uidStats;
        rawUptime = 2;
        rawUptime = rawRealtime3;
        StringBuilder sb3 = sb2;
        long rawRealtimeMs3 = rawRealtimeMs2;
        dumpControllerActivityLine(pw, 0, category, GLOBAL_MODEM_CONTROLLER_DATA, getModemControllerActivity(), which);
        rawRealtimeMs2 = getWifiOnTime(rawUptime, i);
        long wifiRunningTime = getGlobalWifiRunningTime(rawUptime, i);
        dumpLine(printWriter, 0, category, GLOBAL_WIFI_DATA, Long.valueOf(rawRealtimeMs2 / 1000), Long.valueOf(wifiRunningTime / 1000), rawRealtime, rawRealtime, rawRealtime);
        PrintWriter printWriter2 = pw;
        String str3 = category;
        int i4 = which;
        dumpControllerActivityLine(printWriter2, 0, str3, GLOBAL_WIFI_CONTROLLER_DATA, getWifiControllerActivity(), i4);
        dumpControllerActivityLine(printWriter2, 0, str3, GLOBAL_BLUETOOTH_CONTROLLER_DATA, getBluetoothControllerActivity(), i4);
        dumpLine(printWriter, 0, category, MISC_DATA, Long.valueOf(screenOnTime / 1000), Long.valueOf(phoneOnTime / 1000), Long.valueOf(fullWakeLockTimeTotal / 1000), Long.valueOf(partialWakeLockTimeTotal / 1000), Long.valueOf(getMobileRadioActiveTime(rawUptime, i) / 1000), Long.valueOf(getMobileRadioActiveAdjustedTime(i) / 1000), Long.valueOf(interactiveTime / 1000), Long.valueOf(powerSaveModeEnabledTime / 1000), Integer.valueOf(connChanges), Long.valueOf(deviceIdleModeFullTime / 1000), Integer.valueOf(getDeviceIdleModeCount(2, i)), Long.valueOf(deviceIdlingTime / 1000), Integer.valueOf(getDeviceIdlingCount(2, i)), Integer.valueOf(getMobileRadioActiveCount(i)), Long.valueOf(getMobileRadioActiveUnknownTime(i) / 1000), Long.valueOf(deviceIdleModeLightTime / 1000), Integer.valueOf(getDeviceIdleModeCount(1, i)), Long.valueOf(deviceLightIdlingTime / 1000), Integer.valueOf(getDeviceIdlingCount(1, i)), Long.valueOf(getLongestDeviceIdleModeTime(1)), Long.valueOf(getLongestDeviceIdleModeTime(2)));
        i3 = 5;
        objArr = new Object[5];
        int i5 = 0;
        while (i5 < i3) {
            objArr[i5] = Long.valueOf(getScreenBrightnessTime(i5, rawUptime, i) / 1000);
            i5++;
            i3 = 5;
        }
        dumpLine(printWriter, 0, category, "br", objArr);
        i3 = 5;
        objArr = new Object[5];
        i5 = 0;
        while (i5 < i3) {
            objArr[i5] = Long.valueOf(getPhoneSignalStrengthTime(i5, rawUptime, i) / 1000);
            i5++;
            i3 = 5;
        }
        dumpLine(printWriter, 0, category, SIGNAL_STRENGTH_TIME_DATA, objArr);
        dumpLine(printWriter, 0, category, SIGNAL_SCANNING_TIME_DATA, Long.valueOf(getPhoneSignalScanningTime(rawUptime, i) / 1000));
        for (i3 = 0; i3 < 5; i3++) {
            objArr[i3] = Integer.valueOf(getPhoneSignalStrengthCount(i3, i));
        }
        dumpLine(printWriter, 0, category, SIGNAL_STRENGTH_COUNT_DATA, objArr);
        Object[] args2 = new Object[22];
        for (NU2 = 0; NU2 < 22; NU2++) {
            args2[NU2] = Long.valueOf(getPhoneDataConnectionTime(NU2, rawUptime, i) / 1000);
        }
        dumpLine(printWriter, 0, category, DATA_CONNECTION_TIME_DATA, args2);
        for (NU2 = 0; NU2 < 22; NU2++) {
            args2[NU2] = Integer.valueOf(getPhoneDataConnectionCount(NU2, i));
        }
        dumpLine(printWriter, 0, category, DATA_CONNECTION_COUNT_DATA, args2);
        NU2 = 8;
        args2 = new Object[8];
        i5 = 0;
        while (i5 < NU2) {
            args2[i5] = Long.valueOf(getWifiStateTime(i5, rawUptime, i) / 1000);
            i5++;
            NU2 = 8;
        }
        dumpLine(printWriter, 0, category, WIFI_STATE_TIME_DATA, args2);
        for (NU2 = 0; NU2 < 8; NU2++) {
            args2[NU2] = Integer.valueOf(getWifiStateCount(NU2, i));
        }
        dumpLine(printWriter, 0, category, WIFI_STATE_COUNT_DATA, args2);
        args2 = new Object[13];
        for (NU2 = 0; NU2 < 13; NU2++) {
            args2[NU2] = Long.valueOf(getWifiSupplStateTime(NU2, rawUptime, i) / 1000);
        }
        dumpLine(printWriter, 0, category, WIFI_SUPPL_STATE_TIME_DATA, args2);
        for (NU2 = 0; NU2 < 13; NU2++) {
            args2[NU2] = Integer.valueOf(getWifiSupplStateCount(NU2, i));
        }
        dumpLine(printWriter, 0, category, WIFI_SUPPL_STATE_COUNT_DATA, args2);
        NU2 = 5;
        Object[] args3 = new Object[5];
        i3 = 0;
        while (i3 < NU2) {
            args3[i3] = Long.valueOf(getWifiSignalStrengthTime(i3, rawUptime, i) / 1000);
            i3++;
            NU2 = 5;
        }
        dumpLine(printWriter, 0, category, WIFI_SIGNAL_STRENGTH_TIME_DATA, args3);
        for (i3 = 0; i3 < 5; i3++) {
            args3[i3] = Integer.valueOf(getWifiSignalStrengthCount(i3, i));
        }
        dumpLine(printWriter, 0, category, WIFI_SIGNAL_STRENGTH_COUNT_DATA, args3);
        long multicastWakeLockTimeTotalMicros = getWifiMulticastWakelockTime(rawUptime, i);
        int multicastWakeLockCountTotal = getWifiMulticastWakelockCount(i);
        dumpLine(printWriter, 0, category, WIFI_MULTICAST_TOTAL_DATA, Long.valueOf(multicastWakeLockTimeTotalMicros / 1000), Integer.valueOf(multicastWakeLockCountTotal));
        args2 = new Object[10];
        int i6 = 2;
        args2[2] = Integer.valueOf(getDischargeAmountScreenOnSinceCharge());
        args2[3] = Integer.valueOf(getDischargeAmountScreenOffSinceCharge());
        args2[4] = Long.valueOf(dischargeCount / 1000);
        args2[5] = Long.valueOf(dischargeScreenOffCount / 1000);
        args2[6] = Integer.valueOf(getDischargeAmountScreenDozeSinceCharge());
        args2[7] = Long.valueOf(dischargeScreenDozeCount / 1000);
        args2[8] = Long.valueOf(dischargeLightDozeCount / 1000);
        args2[9] = Long.valueOf(dischargeDeepDozeCount / 1000);
        dumpLine(printWriter, 0, category, "dc", args2);
        String str4 = "\"";
        if (i2 < 0) {
            Map<String, ? extends Timer> kernelWakelocks = getKernelWakelockStats();
            if (kernelWakelocks.size() > 0) {
                for (Entry<String, ? extends Timer> ent : kernelWakelocks.entrySet()) {
                    sb3.setLength(0);
                    str2 = str4;
                    args = args3;
                    i2 = i6;
                    Integer num = rawRealtime;
                    uidStats2 = uidStats4;
                    rawRealtime2 = rawUptime;
                    printWakeLockCheckin(sb3, (Timer) ent.getValue(), rawUptime, null, which, "");
                    args2 = new Object[i2];
                    sb = new StringBuilder();
                    sb.append(str2);
                    sb.append((String) ent.getKey());
                    sb.append(str2);
                    args2[0] = sb.toString();
                    args2[1] = sb3.toString();
                    dumpLine(printWriter, 0, category, KERNEL_WAKELOCK_DATA, args2);
                    str4 = str2;
                    rawUptime = rawRealtime2;
                    rawRealtime = num;
                    args3 = args;
                    uidStats4 = uidStats2;
                    i6 = i2;
                    i2 = reqUid;
                }
                uidStats2 = uidStats4;
                i2 = i6;
                obj = rawRealtime;
                rawRealtime2 = rawUptime;
                str2 = str4;
            } else {
                uidStats2 = uidStats4;
                i2 = 2;
                obj = rawRealtime;
                rawRealtime2 = rawUptime;
                str2 = str4;
            }
            Map<String, ? extends Timer> wakeupReasons = getWakeupReasonStats();
            if (wakeupReasons.size() > 0) {
                for (Entry<String, ? extends Timer> ent2 : wakeupReasons.entrySet()) {
                    totalTimeMicros = ((Timer) ent2.getValue()).getTotalTimeLocked(rawRealtime2, i);
                    args3 = ((Timer) ent2.getValue()).getCountLocked(i);
                    Object[] objArr3 = new Object[3];
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str2);
                    Map<String, ? extends Timer> wakeupReasons2 = wakeupReasons;
                    stringBuilder2.append((String) ent2.getKey());
                    stringBuilder2.append(str2);
                    objArr3[0] = stringBuilder2.toString();
                    objArr3[1] = Long.valueOf((totalTimeMicros + 500) / 1000);
                    objArr3[2] = Integer.valueOf(args3);
                    dumpLine(printWriter, 0, category, WAKEUP_REASON_DATA, objArr3);
                    wakeupReasons = wakeupReasons2;
                }
            }
        } else {
            obj = rawRealtime;
            uidStats2 = uidStats4;
            rawRealtime2 = rawUptime;
            str2 = str4;
        }
        Map<String, ? extends Timer> rpmStats = getRpmStats();
        Map<String, ? extends Timer> screenOffRpmStats2 = getScreenOffRpmStats();
        if (rpmStats.size() > 0) {
            Iterator it = rpmStats.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, ? extends Timer> ent3 = (Entry) it.next();
                sb3.setLength(0);
                Timer totalTimer = (Timer) ent3.getValue();
                totalTimeMicros = (totalTimer.getTotalTimeLocked(rawRealtime2, i) + 500) / 1000;
                i4 = totalTimer.getCountLocked(i);
                Iterator it2 = it;
                screenOffTimer = (Timer) screenOffRpmStats2.get(ent3.getKey());
                long totalTimeLocked;
                if (screenOffTimer != null) {
                    totalTimeLocked = (screenOffTimer.getTotalTimeLocked(rawRealtime2, i) + 500) / 1000;
                } else {
                    totalTimeLocked = 0;
                }
                if (screenOffTimer != null) {
                    int countLocked = screenOffTimer.getCountLocked(i);
                }
                Object[] objArr4 = new Object[3];
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str2);
                stringBuilder3.append((String) ent3.getKey());
                stringBuilder3.append(str2);
                objArr4[0] = stringBuilder3.toString();
                objArr4[1] = Long.valueOf(totalTimeMicros);
                objArr4[2] = Integer.valueOf(i4);
                dumpLine(printWriter, 0, category, RESOURCE_POWER_MANAGER_DATA, objArr4);
                it = it2;
            }
            z = false;
        } else {
            z = false;
        }
        BatteryStatsHelper helper = new BatteryStatsHelper(context, z, wifiOnly);
        helper.create(this);
        helper.refreshStats(i, -1);
        List<BatterySipper> sippers2 = helper.getUsageList();
        BatteryStatsHelper batteryStatsHelper;
        if (sippers2 == null || sippers2.size() <= 0) {
            sippers = sippers2;
            batteryStatsHelper = helper;
            screenOffRpmStats = screenOffRpmStats2;
        } else {
            int uid2;
            dumpLine(printWriter, 0, category, POWER_USE_SUMMARY_DATA, BatteryStatsHelper.makemAh(helper.getPowerProfile().getBatteryCapacity()), BatteryStatsHelper.makemAh(helper.getComputedPower()), BatteryStatsHelper.makemAh(helper.getMinDrainedPower()), BatteryStatsHelper.makemAh(helper.getMaxDrainedPower()));
            uid = 0;
            i3 = 0;
            while (i3 < sippers2.size()) {
                BatterySipper bs = (BatterySipper) sippers2.get(i3);
                uid2 = uid;
                switch (bs.drainType) {
                    case AMBIENT_DISPLAY:
                        sippers = sippers2;
                        label = "ambi";
                        uid = uid2;
                        break;
                    case IDLE:
                        sippers = sippers2;
                        label = "idle";
                        uid = uid2;
                        break;
                    case CELL:
                        sippers = sippers2;
                        label = "cell";
                        uid = uid2;
                        break;
                    case PHONE:
                        sippers = sippers2;
                        label = "phone";
                        uid = uid2;
                        break;
                    case WIFI:
                        sippers = sippers2;
                        label = "wifi";
                        uid = uid2;
                        break;
                    case BLUETOOTH:
                        sippers = sippers2;
                        label = "blue";
                        uid = uid2;
                        break;
                    case SCREEN:
                        sippers = sippers2;
                        label = "scrn";
                        uid = uid2;
                        break;
                    case FLASHLIGHT:
                        sippers = sippers2;
                        label = "flashlight";
                        uid = uid2;
                        break;
                    case APP:
                        sippers = sippers2;
                        uid = bs.uidObj.getUid();
                        label = "uid";
                        break;
                    case USER:
                        sippers = sippers2;
                        uid = UserHandle.getUid(bs.userId, null);
                        label = "user";
                        break;
                    case UNACCOUNTED:
                        sippers = sippers2;
                        label = "unacc";
                        uid = uid2;
                        break;
                    case OVERCOUNTED:
                        sippers = sippers2;
                        label = "over";
                        uid = uid2;
                        break;
                    case CAMERA:
                        sippers = sippers2;
                        label = Context.CAMERA_SERVICE;
                        uid = uid2;
                        break;
                    case MEMORY:
                        sippers = sippers2;
                        label = "memory";
                        uid = uid2;
                        break;
                    default:
                        sippers = sippers2;
                        label = "???";
                        uid = uid2;
                        break;
                }
                batteryStatsHelper = helper;
                r5 = new Object[5];
                screenOffRpmStats = screenOffRpmStats2;
                r5[1] = BatteryStatsHelper.makemAh(bs.totalPowerMah);
                r5[2] = Integer.valueOf(bs.shouldHide);
                r5[3] = BatteryStatsHelper.makemAh(bs.screenPowerMah);
                r5[4] = BatteryStatsHelper.makemAh(bs.proportionalSmearMah);
                dumpLine(printWriter, uid, category, POWER_USE_ITEM_DATA, r5);
                i3++;
                Context context2 = context;
                boolean z2 = wifiOnly;
                sippers2 = sippers;
                helper = batteryStatsHelper;
                screenOffRpmStats2 = screenOffRpmStats;
            }
            uid2 = uid;
            sippers = sippers2;
            batteryStatsHelper = helper;
            screenOffRpmStats = screenOffRpmStats2;
        }
        long[] cpuFreqs = getCpuFreqs();
        if (cpuFreqs != null) {
            sb3.setLength(0);
            i3 = 0;
            while (i3 < cpuFreqs.length) {
                sb = new StringBuilder();
                sb.append(i3 == 0 ? "" : ",");
                sb.append(cpuFreqs[i3]);
                sb3.append(sb.toString());
                i3++;
            }
            dumpLine(printWriter, 0, category, GLOBAL_CPU_FREQ_DATA, sb3.toString());
        }
        int iu2 = 0;
        while (true) {
            i4 = NU;
            if (iu2 < i4) {
                Map<String, ? extends Timer> rpmStats2;
                List<BatterySipper> sippers3;
                SparseArray<? extends Uid> uidStats5;
                int NU4;
                long unoptimizedScanMaxTimeBg;
                Map<String, ? extends Timer> screenOffRpmStats3;
                int iu3;
                String str5;
                long[] cpuFreqs2;
                StringBuilder sb4;
                long rawRealtimeMs4;
                long batteryUptime4;
                SparseArray<? extends Uid> uidStats6 = uidStats2;
                int uid3 = uidStats6.keyAt(iu2);
                i5 = reqUid;
                if (i5 < 0 || uid3 == i5) {
                    int i7;
                    Timer bleTimerBg;
                    int countBg;
                    int iu4;
                    String str6;
                    long actualTimeBg;
                    Timer bleTimer;
                    long rawRealtime4;
                    Timer unoptimizedScanTimer;
                    long unoptimizedScanMaxTime;
                    long unoptimizedScanTotalTimeBg;
                    long rawRealtimeMs5;
                    long rawRealtime5;
                    Timer timer;
                    Timer mcTimer;
                    ArrayMap<String, ? extends Wakelock> wakelocks2;
                    String wakelocks3;
                    ArrayMap<String, ? extends Timer> syncs;
                    Timer timer2;
                    long rawRealtime6;
                    Object[] objArr5;
                    String str7;
                    Uid u3 = (Uid) uidStats6.valueAt(iu2);
                    long mobileBytesRx = u3.getNetworkActivityBytes(0, i);
                    long mobileBytesTx = u3.getNetworkActivityBytes(1, i);
                    long wifiBytesRx = u3.getNetworkActivityBytes(2, i);
                    long wifiBytesTx = u3.getNetworkActivityBytes(3, i);
                    long mobilePacketsRx = u3.getNetworkActivityPackets(0, i);
                    long mobilePacketsTx = u3.getNetworkActivityPackets(1, i);
                    long mobileActiveTime = u3.getMobileRadioActiveTime(i);
                    NU = u3.getMobileRadioActiveCount(i);
                    long mobileWakeup = u3.getMobileRadioApWakeupCount(i);
                    long wifiPacketsRx = u3.getNetworkActivityPackets(2, i);
                    long wifiPacketsTx = u3.getNetworkActivityPackets(3, i);
                    long wifiWakeup = u3.getWifiRadioApWakeupCount(i);
                    long btBytesRx = u3.getNetworkActivityBytes(4, i);
                    long btBytesTx = u3.getNetworkActivityBytes(5, i);
                    long mobileBytesBgRx = u3.getNetworkActivityBytes(6, i);
                    long mobileBytesBgTx = u3.getNetworkActivityBytes(7, i);
                    long wifiBytesBgRx = u3.getNetworkActivityBytes(8, i);
                    long wifiBytesBgTx = u3.getNetworkActivityBytes(9, i);
                    long mobilePacketsBgRx = u3.getNetworkActivityPackets(6, i);
                    long mobilePacketsBgTx = u3.getNetworkActivityPackets(7, i);
                    long wifiPacketsBgRx = u3.getNetworkActivityPackets(8, i);
                    long wifiPacketsBgTx = u3.getNetworkActivityPackets(9, i);
                    if (mobileBytesRx > 0 || mobileBytesTx > 0 || wifiBytesRx > 0 || wifiBytesTx > 0 || mobilePacketsRx > 0 || mobilePacketsTx > 0 || wifiPacketsRx > 0 || wifiPacketsTx > 0 || mobileActiveTime > 0 || NU > 0 || btBytesRx > 0 || btBytesTx > 0 || mobileWakeup > 0 || wifiWakeup > 0 || mobileBytesBgRx > 0 || mobileBytesBgTx > 0 || wifiBytesBgRx > 0 || wifiBytesBgTx > 0 || mobilePacketsBgRx > 0 || mobilePacketsBgTx > 0 || wifiPacketsBgRx > 0 || wifiPacketsBgTx > 0) {
                        objArr = new Object[22];
                        i7 = 2;
                        objArr[2] = Long.valueOf(wifiBytesRx);
                        objArr[3] = Long.valueOf(wifiBytesTx);
                        objArr[4] = Long.valueOf(mobilePacketsRx);
                        objArr[5] = Long.valueOf(mobilePacketsTx);
                        objArr[6] = Long.valueOf(wifiPacketsRx);
                        objArr[7] = Long.valueOf(wifiPacketsTx);
                        objArr[8] = Long.valueOf(mobileActiveTime);
                        objArr[9] = Integer.valueOf(NU);
                        objArr[10] = Long.valueOf(btBytesRx);
                        objArr[11] = Long.valueOf(btBytesTx);
                        objArr[12] = Long.valueOf(mobileWakeup);
                        objArr[13] = Long.valueOf(wifiWakeup);
                        objArr[14] = Long.valueOf(mobileBytesBgRx);
                        objArr[15] = Long.valueOf(mobileBytesBgTx);
                        objArr[16] = Long.valueOf(wifiBytesBgRx);
                        objArr[17] = Long.valueOf(wifiBytesBgTx);
                        objArr[18] = Long.valueOf(mobilePacketsBgRx);
                        objArr[19] = Long.valueOf(mobilePacketsBgTx);
                        objArr[20] = Long.valueOf(wifiPacketsBgRx);
                        objArr[21] = Long.valueOf(wifiPacketsBgTx);
                        dumpLine(printWriter, uid3, category, NETWORK_DATA, objArr);
                    } else {
                        i7 = 2;
                    }
                    List<BatterySipper> list = sippers;
                    rpmStats2 = rpmStats;
                    rpmStats = i7;
                    sippers3 = list;
                    Uid u4 = u3;
                    long[] cpuFreqs3 = cpuFreqs;
                    uid = uid3;
                    uidStats5 = uidStats6;
                    NU4 = i4;
                    dumpControllerActivityLine(pw, uid3, category, MODEM_CONTROLLER_DATA, u3.getModemControllerActivity(), which);
                    long fullWifiLockOnTime = u4.getFullWifiLockTime(rawRealtime2, i);
                    long wifiScanTime = u4.getWifiScanTime(rawRealtime2, i);
                    int wifiScanCount = u4.getWifiScanCount(i);
                    int wifiScanCountBg = u4.getWifiScanBackgroundCount(i);
                    long wifiScanActualTimeMs = (u4.getWifiScanActualTime(rawRealtime2) + 500) / 1000;
                    long wifiScanActualTimeMsBg = (u4.getWifiScanBackgroundTime(rawRealtime2) + 500) / 1000;
                    long uidWifiRunningTime = u4.getWifiRunningTime(rawRealtime2, i);
                    if (!(fullWifiLockOnTime == 0 && wifiScanTime == 0 && wifiScanCount == 0 && wifiScanCountBg == 0 && wifiScanActualTimeMs == 0 && wifiScanActualTimeMsBg == 0 && uidWifiRunningTime == 0)) {
                        dumpLine(printWriter, uid, category, WIFI_DATA, Long.valueOf(fullWifiLockOnTime), Long.valueOf(wifiScanTime), Long.valueOf(uidWifiRunningTime), Integer.valueOf(wifiScanCount), obj, obj, obj, Integer.valueOf(wifiScanCountBg), Long.valueOf(wifiScanActualTimeMs), Long.valueOf(wifiScanActualTimeMsBg));
                    }
                    dumpControllerActivityLine(pw, uid, category, WIFI_CONTROLLER_DATA, u4.getWifiControllerActivity(), which);
                    Timer bleTimer2 = u4.getBluetoothScanTimer();
                    if (bleTimer2 != null) {
                        batteryUptime = (bleTimer2.getTotalTimeLocked(rawRealtime2, i) + 500) / 1000;
                        if (batteryUptime != 0) {
                            i5 = bleTimer2.getCountLocked(i);
                            bleTimerBg = u4.getBluetoothScanBackgroundTimer();
                            countBg = bleTimerBg != null ? bleTimerBg.getCountLocked(i) : 0;
                            iu4 = iu2;
                            str6 = str2;
                            rawUptime = rawRealtimeMs3;
                            rawRealtimeMs3 = bleTimer2.getTotalDurationMsLocked(rawUptime);
                            actualTimeBg = bleTimerBg != null ? bleTimerBg.getTotalDurationMsLocked(rawUptime) : 0;
                            if (u4.getBluetoothScanResultCounter() != null) {
                                uid3 = u4.getBluetoothScanResultCounter().getCountLocked(i);
                            } else {
                                uid3 = 0;
                            }
                            if (u4.getBluetoothScanResultBgCounter() != null) {
                                bleTimer = bleTimer2;
                                bleTimer2 = u4.getBluetoothScanResultBgCounter().getCountLocked(i);
                            } else {
                                bleTimer = bleTimer2;
                                bleTimer2 = null;
                            }
                            rawRealtime4 = rawRealtime2;
                            unoptimizedScanTimer = u4.getBluetoothUnoptimizedScanTimer();
                            long unoptimizedScanTotalTime = unoptimizedScanTimer != null ? unoptimizedScanTimer.getTotalDurationMsLocked(rawUptime) : 0;
                            unoptimizedScanMaxTime = unoptimizedScanTimer != null ? unoptimizedScanTimer.getMaxDurationMsLocked(rawUptime) : 0;
                            Timer unoptimizedScanTimerBg = u4.getBluetoothUnoptimizedScanBackgroundTimer();
                            unoptimizedScanTotalTimeBg = unoptimizedScanTimerBg != null ? unoptimizedScanTimerBg.getTotalDurationMsLocked(rawUptime) : 0;
                            unoptimizedScanMaxTimeBg = unoptimizedScanTimerBg != null ? unoptimizedScanTimerBg.getMaxDurationMsLocked(rawUptime) : 0;
                            dumpLine(printWriter, uid, category, BLUETOOTH_MISC_DATA, new Object[]{Long.valueOf(batteryUptime), Integer.valueOf(i5), Integer.valueOf(countBg), Long.valueOf(rawRealtimeMs3), Long.valueOf(actualTimeBg), Integer.valueOf(uid3), Integer.valueOf(bleTimer2), Long.valueOf(unoptimizedScanTotalTime), Long.valueOf(unoptimizedScanTotalTimeBg), Long.valueOf(unoptimizedScanMaxTime), Long.valueOf(unoptimizedScanMaxTimeBg)});
                        } else {
                            bleTimer = bleTimer2;
                            iu4 = iu2;
                            str6 = str2;
                            rawRealtime4 = rawRealtime2;
                            rawUptime = rawRealtimeMs3;
                        }
                    } else {
                        bleTimer = bleTimer2;
                        iu4 = iu2;
                        str6 = str2;
                        rawRealtime4 = rawRealtime2;
                        rawUptime = rawRealtimeMs3;
                    }
                    unoptimizedScanTimer = bleTimer;
                    dumpControllerActivityLine(pw, uid, category, BLUETOOTH_CONTROLLER_DATA, u4.getBluetoothControllerActivity(), which);
                    if (u4.hasUserActivity()) {
                        args2 = new Object[Uid.NUM_USER_ACTIVITY_TYPES];
                        z = false;
                        for (i5 = 0; i5 < Uid.NUM_USER_ACTIVITY_TYPES; i5++) {
                            uid3 = u4.getUserActivityCount(i5, i);
                            args2[i5] = Integer.valueOf(uid3);
                            if (uid3 != 0) {
                                z = true;
                            }
                        }
                        if (z) {
                            dumpLine(printWriter, uid, category, USER_ACTIVITY_DATA, args2);
                        }
                        args = args2;
                    }
                    if (u4.getAggregatedPartialWakelockTimer() != null) {
                        screenOffTimer = u4.getAggregatedPartialWakelockTimer();
                        long totTimeMs = screenOffTimer.getTotalDurationMsLocked(rawUptime);
                        bleTimerBg = screenOffTimer.getSubTimer();
                        rawRealtimeMs = bleTimerBg != null ? bleTimerBg.getTotalDurationMsLocked(rawUptime) : 0;
                        dumpLine(printWriter, uid, category, AGGREGATED_WAKELOCK_DATA, Long.valueOf(totTimeMs), Long.valueOf(rawRealtimeMs));
                    }
                    ArrayMap<String, ? extends Wakelock> wakelocks4 = u4.getWakelockStats();
                    i4 = wakelocks4.size() - 1;
                    while (i4 >= 0) {
                        Wakelock wl = (Wakelock) wakelocks4.valueAt(i4);
                        sb3.setLength(0);
                        rawRealtime3 = rawRealtime4;
                        actualTimeBg = rawUptime;
                        Wakelock wl2 = wl;
                        rawUptime = i4;
                        i4 = which;
                        bleTimer = unoptimizedScanTimer;
                        screenOffRpmStats3 = screenOffRpmStats;
                        iu3 = iu4;
                        rawRealtimeMs5 = actualTimeBg;
                        int iw2 = rawUptime;
                        String linePrefix = printWakeLockCheckin(sb3, wl.getWakeTime(1), rawRealtime3, FullBackup.FILES_TREE_TOKEN, i4, "");
                        Timer pTimer = wl2.getWakeTime(0);
                        linePrefix = printWakeLockCheckin(sb3, pTimer, rawRealtime3, "p", i4, linePrefix);
                        rawRealtime3 = rawRealtime4;
                        i4 = which;
                        str = printWakeLockCheckin(sb3, wl2.getWakeTime(2), rawRealtime3, BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_W, i4, printWakeLockCheckin(sb3, pTimer != null ? pTimer.getSubTimer() : null, rawRealtime3, "bp", i4, linePrefix));
                        if (sb3.length() > 0) {
                            label = (String) wakelocks4.keyAt(iw2);
                            if (label.indexOf(44) >= 0) {
                                label = label.replace(',', '_');
                            }
                            if (label.indexOf(10) >= 0) {
                                label = label.replace(10, '_');
                            }
                            if (label.indexOf(13) >= 0) {
                                label = label.replace(CharUtils.CR, '_');
                            }
                            dumpLine(printWriter, uid, category, WAKELOCK_DATA, label, sb3.toString());
                        }
                        i4 = iw2 - 1;
                        iu4 = iu3;
                        unoptimizedScanTimer = bleTimer;
                        rawUptime = rawRealtimeMs5;
                        screenOffRpmStats = screenOffRpmStats3;
                    }
                    rawRealtimeMs5 = rawUptime;
                    bleTimer = unoptimizedScanTimer;
                    screenOffRpmStats3 = screenOffRpmStats;
                    iu3 = iu4;
                    unoptimizedScanTimer = i4;
                    unoptimizedScanTimer = u4.getMulticastWakelockStats();
                    if (unoptimizedScanTimer != null) {
                        rawRealtime5 = rawRealtime4;
                        batteryUptime = unoptimizedScanTimer.getTotalTimeLocked(rawRealtime5, i) / 1000;
                        i5 = unoptimizedScanTimer.getCountLocked(i);
                        if (batteryUptime > 0) {
                            dumpLine(printWriter, uid, category, WIFI_MULTICAST_DATA, Long.valueOf(batteryUptime), Integer.valueOf(i5));
                        }
                    } else {
                        rawRealtime5 = rawRealtime4;
                    }
                    ArrayMap<String, ? extends Timer> syncs2 = u4.getSyncStats();
                    i3 = syncs2.size() - 1;
                    while (i3 >= 0) {
                        long bgTime;
                        timer = (Timer) syncs2.valueAt(i3);
                        rawRealtime3 = (timer.getTotalTimeLocked(rawRealtime5, i) + 500) / 1000;
                        countBg = timer.getCountLocked(i);
                        mcTimer = unoptimizedScanTimer;
                        unoptimizedScanTimer = timer.getSubTimer();
                        if (unoptimizedScanTimer != null) {
                            rawRealtime4 = rawRealtime5;
                            rawRealtime5 = rawRealtimeMs5;
                            bgTime = unoptimizedScanTimer.getTotalDurationMsLocked(rawRealtime5);
                        } else {
                            rawRealtime4 = rawRealtime5;
                            rawRealtime5 = rawRealtimeMs5;
                            bgTime = -1;
                        }
                        int bgCount = unoptimizedScanTimer != null ? unoptimizedScanTimer.getCountLocked(i) : -1;
                        if (rawRealtime3 != 0) {
                            Object[] objArr6 = new Object[5];
                            sb = new StringBuilder();
                            wakelocks2 = wakelocks4;
                            wakelocks3 = str6;
                            sb.append(wakelocks3);
                            syncs = syncs2;
                            sb.append((String) syncs2.keyAt(i3));
                            sb.append(wakelocks3);
                            objArr6[0] = sb.toString();
                            objArr6[1] = Long.valueOf(rawRealtime3);
                            objArr6[2] = Integer.valueOf(countBg);
                            objArr6[3] = Long.valueOf(bgTime);
                            objArr6[4] = Integer.valueOf(bgCount);
                            dumpLine(printWriter, uid, category, SYNC_DATA, objArr6);
                        } else {
                            syncs = syncs2;
                            timer2 = unoptimizedScanTimer;
                            wakelocks2 = wakelocks4;
                            wakelocks3 = str6;
                        }
                        i3--;
                        rawRealtimeMs5 = rawRealtime5;
                        str6 = wakelocks3;
                        unoptimizedScanTimer = mcTimer;
                        rawRealtime5 = rawRealtime4;
                        wakelocks4 = wakelocks2;
                        syncs2 = syncs;
                    }
                    rawRealtime4 = rawRealtime5;
                    syncs = syncs2;
                    mcTimer = unoptimizedScanTimer;
                    wakelocks2 = wakelocks4;
                    wakelocks3 = str6;
                    rawRealtime5 = rawRealtimeMs5;
                    ArrayMap<String, ? extends Timer> jobs = u4.getJobStats();
                    i3 = jobs.size() - 1;
                    while (i3 >= 0) {
                        timer = (Timer) jobs.valueAt(i3);
                        totalTimeMicros = rawRealtime4;
                        long totalTime = (timer.getTotalTimeLocked(totalTimeMicros, i) + 500) / 1000;
                        i5 = timer.getCountLocked(i);
                        Timer bgTimer = timer.getSubTimer();
                        actualTimeBg = bgTimer != null ? bgTimer.getTotalDurationMsLocked(rawRealtime5) : -1;
                        int bgCount2 = bgTimer != null ? bgTimer.getCountLocked(i) : -1;
                        if (totalTime != 0) {
                            rawRealtime4 = timer;
                            rawRealtime6 = totalTimeMicros;
                            objArr5 = new Object[5];
                            sb = new StringBuilder();
                            sb.append(wakelocks3);
                            sb.append((String) jobs.keyAt(i3));
                            sb.append(wakelocks3);
                            objArr5[0] = sb.toString();
                            objArr5[1] = Long.valueOf(totalTime);
                            objArr5[2] = Integer.valueOf(i5);
                            objArr5[3] = Long.valueOf(actualTimeBg);
                            objArr5[4] = Integer.valueOf(bgCount2);
                            dumpLine(printWriter, uid, category, JOB_DATA, objArr5);
                        } else {
                            rawRealtime4 = timer;
                            rawRealtime6 = totalTimeMicros;
                        }
                        i3--;
                        rawRealtime4 = rawRealtime6;
                    }
                    rawRealtime6 = rawRealtime4;
                    ArrayMap<String, SparseIntArray> completions = u4.getJobCompletionStats();
                    for (i3 = completions.size() - 1; i3 >= 0; i3--) {
                        SparseIntArray types = (SparseIntArray) completions.valueAt(i3);
                        if (types != null) {
                            objArr5 = new Object[6];
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(wakelocks3);
                            stringBuilder.append((String) completions.keyAt(i3));
                            stringBuilder.append(wakelocks3);
                            objArr5[0] = stringBuilder.toString();
                            objArr5[1] = Integer.valueOf(types.get(0, 0));
                            objArr5[2] = Integer.valueOf(types.get(1, 0));
                            objArr5[3] = Integer.valueOf(types.get(2, 0));
                            objArr5[4] = Integer.valueOf(types.get(3, 0));
                            objArr5[5] = Integer.valueOf(types.get(4, 0));
                            dumpLine(printWriter, uid, category, JOB_COMPLETION_DATA, objArr5);
                        }
                    }
                    u4.getDeferredJobsCheckinLineLocked(sb3, i);
                    if (sb3.length() > 0) {
                        dumpLine(printWriter, uid, category, JOBS_DEFERRED_DATA, sb3.toString());
                    }
                    printWriter2 = pw;
                    NU2 = uid;
                    str3 = category;
                    rawRealtime4 = rawRealtime6;
                    rawRealtime2 = rawRealtime5;
                    rawRealtime5 = rawRealtime4;
                    str5 = wakelocks3;
                    int i8 = which;
                    dumpTimer(printWriter2, NU2, str3, FLASHLIGHT_DATA, u4.getFlashlightTurnedOnTimer(), rawRealtime5, i8);
                    dumpTimer(printWriter2, NU2, str3, CAMERA_DATA, u4.getCameraTurnedOnTimer(), rawRealtime5, i8);
                    dumpTimer(printWriter2, NU2, str3, VIDEO_DATA, u4.getVideoTurnedOnTimer(), rawRealtime5, i8);
                    dumpTimer(printWriter2, NU2, str3, AUDIO_DATA, u4.getAudioTurnedOnTimer(), rawRealtime5, i8);
                    SparseArray<? extends Sensor> sensors = u4.getSensorStats();
                    i4 = sensors.size();
                    i3 = 0;
                    while (i3 < i4) {
                        int NSE;
                        Sensor se = (Sensor) sensors.valueAt(i3);
                        i5 = sensors.keyAt(i3);
                        bleTimerBg = se.getSensorTime();
                        if (bleTimerBg != null) {
                            NSE = i4;
                            i4 = rawRealtime4;
                            if ((bleTimerBg.getTotalTimeLocked(i4, i) + 500) / 1000 != 0) {
                                countBg = bleTimerBg.getCountLocked(i);
                                unoptimizedScanMaxTime = i4;
                                i4 = se.getSensorBackgroundTime();
                                iu2 = i4 != 0 ? i4.getCountLocked(i) : 0;
                                unoptimizedScanTotalTimeBg = bleTimerBg.getTotalDurationMsLocked(rawRealtime2);
                                unoptimizedScanMaxTimeBg = i4 != 0 ? i4.getTotalDurationMsLocked(rawRealtime2) : 0;
                                dumpLine(printWriter, uid, category, SENSOR_DATA, Integer.valueOf(i5), Long.valueOf(rawRealtime4), Integer.valueOf(countBg), Integer.valueOf(iu2), Long.valueOf(unoptimizedScanTotalTimeBg), Long.valueOf(unoptimizedScanMaxTimeBg));
                            } else {
                                timer2 = bleTimerBg;
                                unoptimizedScanMaxTime = i4;
                            }
                        } else {
                            timer2 = bleTimerBg;
                            NSE = i4;
                            unoptimizedScanMaxTime = rawRealtime4;
                        }
                        i3++;
                        i4 = NSE;
                        rawRealtime4 = unoptimizedScanMaxTime;
                    }
                    unoptimizedScanMaxTime = rawRealtime4;
                    printWriter2 = pw;
                    NU2 = uid;
                    str3 = category;
                    rawRealtime5 = rawRealtime4;
                    i8 = which;
                    dumpTimer(printWriter2, NU2, str3, VIBRATOR_DATA, u4.getVibratorOnTimer(), rawRealtime5, i8);
                    dumpTimer(printWriter2, NU2, str3, FOREGROUND_ACTIVITY_DATA, u4.getForegroundActivityTimer(), rawRealtime5, i8);
                    dumpTimer(printWriter2, NU2, str3, FOREGROUND_SERVICE_DATA, u4.getForegroundServiceTimer(), rawRealtime5, i8);
                    i3 = 7;
                    objArr = new Object[7];
                    rawRealtime3 = 0;
                    countBg = 0;
                    while (countBg < i3) {
                        rawRealtime5 = rawRealtime4;
                        rawRealtime4 = u4.getProcessStateTime(countBg, rawRealtime5, i);
                        rawRealtime3 += rawRealtime4;
                        objArr[countBg] = Long.valueOf((rawRealtime4 + 500) / 1000);
                        countBg++;
                        rawRealtime4 = rawRealtime5;
                        i3 = 7;
                    }
                    rawRealtime5 = rawRealtime4;
                    if (rawRealtime3 > 0) {
                        dumpLine(printWriter, uid, category, "st", objArr);
                    }
                    rawRealtime4 = u4.getUserCpuTimeUs(i);
                    unoptimizedScanMaxTime = u4.getSystemCpuTimeUs(i);
                    if (rawRealtime4 > 0 || unoptimizedScanMaxTime > 0) {
                        dumpLine(printWriter, uid, category, CPU_DATA, Long.valueOf(rawRealtime4 / 1000), Long.valueOf(unoptimizedScanMaxTime / 1000), obj);
                    }
                    if (cpuFreqs3 != null) {
                        long[] cpuFreqs4;
                        StringBuilder stringBuilder4;
                        long[] cpuFreqTimeMs = u4.getCpuFreqTimes(i);
                        if (cpuFreqTimeMs != null) {
                            cpuFreqs4 = cpuFreqs3;
                            cpuFreqs3 = objArr;
                            if (cpuFreqTimeMs.length == cpuFreqs4.length) {
                                sb3.setLength(0);
                                NU2 = 0;
                                while (NU2 < cpuFreqTimeMs.length) {
                                    stringBuilder4 = new StringBuilder();
                                    unoptimizedScanTotalTimeBg = rawRealtime3;
                                    stringBuilder4.append(NU2 == 0 ? "" : ",");
                                    stringBuilder4.append(cpuFreqTimeMs[NU2]);
                                    sb3.append(stringBuilder4.toString());
                                    NU2++;
                                    rawRealtime3 = unoptimizedScanTotalTimeBg;
                                }
                                objArr = u4.getScreenOffCpuFreqTimes(i);
                                if (objArr != null) {
                                    rawRealtime3 = null;
                                    while (rawRealtime3 < objArr.length) {
                                        StringBuilder stringBuilder5 = new StringBuilder();
                                        stringBuilder5.append(",");
                                        unoptimizedScanMaxTimeBg = rawRealtime5;
                                        stringBuilder5.append(objArr[rawRealtime3]);
                                        sb3.append(stringBuilder5.toString());
                                        rawRealtime3++;
                                        rawRealtime5 = unoptimizedScanMaxTimeBg;
                                    }
                                    unoptimizedScanMaxTimeBg = rawRealtime5;
                                } else {
                                    unoptimizedScanMaxTimeBg = rawRealtime5;
                                    for (rawRealtime3 = null; rawRealtime3 < cpuFreqTimeMs.length; rawRealtime3++) {
                                        sb3.append(",0");
                                    }
                                }
                                dumpLine(printWriter, uid, category, CPU_TIMES_AT_FREQ_DATA, UID_TIMES_TYPE_ALL, Integer.valueOf(cpuFreqTimeMs.length), sb3.toString());
                            } else {
                                unoptimizedScanMaxTimeBg = rawRealtime5;
                            }
                        } else {
                            unoptimizedScanMaxTimeBg = rawRealtime5;
                            cpuFreqs4 = cpuFreqs3;
                            cpuFreqs3 = objArr;
                        }
                        objArr = null;
                        while (objArr < 7) {
                            rawRealtime3 = u4.getCpuFreqTimes(i, objArr);
                            if (rawRealtime3 == null || rawRealtime3.length != cpuFreqs4.length) {
                                cpuFreqs2 = cpuFreqs4;
                            } else {
                                sb3.setLength(0);
                                uid3 = 0;
                                while (uid3 < rawRealtime3.length) {
                                    stringBuilder4 = new StringBuilder();
                                    stringBuilder4.append(uid3 == 0 ? "" : ",");
                                    stringBuilder4.append(rawRealtime3[uid3]);
                                    sb3.append(stringBuilder4.toString());
                                    uid3++;
                                }
                                long[] screenOffTimesMs = u4.getScreenOffCpuFreqTimes(i, objArr);
                                if (screenOffTimesMs != null) {
                                    countBg = 0;
                                    while (countBg < screenOffTimesMs.length) {
                                        StringBuilder stringBuilder6 = new StringBuilder();
                                        stringBuilder6.append(",");
                                        cpuFreqs2 = cpuFreqs4;
                                        stringBuilder6.append(screenOffTimesMs[countBg]);
                                        sb3.append(stringBuilder6.toString());
                                        countBg++;
                                        cpuFreqs4 = cpuFreqs2;
                                    }
                                    cpuFreqs2 = cpuFreqs4;
                                } else {
                                    cpuFreqs2 = cpuFreqs4;
                                    for (countBg = 0; countBg < rawRealtime3.length; countBg++) {
                                        sb3.append(",0");
                                    }
                                }
                                dumpLine(printWriter, uid, category, CPU_TIMES_AT_FREQ_DATA, Uid.UID_PROCESS_TYPES[objArr], Integer.valueOf(rawRealtime3.length), sb3.toString());
                            }
                            objArr++;
                            cpuFreqs4 = cpuFreqs2;
                        }
                        cpuFreqs2 = cpuFreqs4;
                    } else {
                        unoptimizedScanMaxTimeBg = rawRealtime5;
                        cpuFreqs2 = cpuFreqs3;
                    }
                    ArrayMap<String, ? extends Proc> processStats = u4.getProcessStats();
                    NU2 = processStats.size() - 1;
                    while (NU2 >= 0) {
                        Proc ps = (Proc) processStats.valueAt(NU2);
                        totalTimeMicros = ps.getUserTime(i);
                        rawRealtime5 = ps.getSystemTime(i);
                        long foregroundMillis = ps.getForegroundTime(i);
                        i8 = ps.getStarts(i);
                        int numCrashes = ps.getNumCrashes(i);
                        int numAnrs = ps.getNumAnrs(i);
                        if (totalTimeMicros == 0 && rawRealtime5 == 0 && foregroundMillis == 0 && i8 == 0 && numAnrs == 0 && numCrashes == 0) {
                            sb4 = sb3;
                            rawRealtimeMs4 = rawRealtime2;
                            str7 = str5;
                        } else {
                            sb4 = sb3;
                            r13 = new Object[7];
                            stringBuilder = new StringBuilder();
                            rawRealtimeMs4 = rawRealtime2;
                            str7 = str5;
                            stringBuilder.append(str7);
                            stringBuilder.append((String) processStats.keyAt(NU2));
                            stringBuilder.append(str7);
                            r13[0] = stringBuilder.toString();
                            r13[1] = Long.valueOf(totalTimeMicros);
                            r13[2] = Long.valueOf(rawRealtime5);
                            r13[3] = Long.valueOf(foregroundMillis);
                            r13[4] = Integer.valueOf(i8);
                            r13[5] = Integer.valueOf(numAnrs);
                            r13[6] = Integer.valueOf(numCrashes);
                            dumpLine(printWriter, uid, category, PROCESS_DATA, r13);
                        }
                        NU2--;
                        str5 = str7;
                        sb3 = sb4;
                        rawRealtime2 = rawRealtimeMs4;
                    }
                    sb4 = sb3;
                    rawRealtimeMs4 = rawRealtime2;
                    str7 = str5;
                    ArrayMap<String, ? extends Pkg> packageStats = u4.getPackageStats();
                    i5 = packageStats.size() - 1;
                    while (i5 >= 0) {
                        ArrayMap<String, ? extends Proc> processStats2;
                        Pkg ps2 = (Pkg) packageStats.valueAt(i5);
                        countBg = 0;
                        i4 = ps2.getWakeupAlarmStats();
                        iu2 = i4.size() - 1;
                        while (iu2 >= 0) {
                            countBg += ((Counter) i4.valueAt(iu2)).getCountLocked(i);
                            processStats2 = processStats;
                            dumpLine(printWriter, uid, category, WAKEUP_ALARM_DATA, ((String) i4.keyAt(iu2)).replace(',', '_'), Integer.valueOf(((Counter) i4.valueAt(iu2)).getCountLocked(i)));
                            iu2--;
                            processStats = processStats2;
                        }
                        processStats2 = processStats;
                        processStats = ps2.getServiceStats();
                        iu2 = processStats.size() - 1;
                        while (iu2 >= 0) {
                            Pkg ps3;
                            ArrayMap<String, ? extends Counter> alarms;
                            Serv ss = (Serv) processStats.valueAt(iu2);
                            str5 = str7;
                            batteryUptime4 = batteryUptime3;
                            batteryUptime3 = ss.getStartTime(batteryUptime4, i);
                            iw = ss.getStarts(i);
                            int launches = ss.getLaunches(i);
                            if (batteryUptime3 == 0 && iw == 0 && launches == 0) {
                                ps3 = ps2;
                                alarms = i4;
                            } else {
                                ps3 = ps2;
                                alarms = i4;
                                dumpLine(printWriter, uid, category, APK_DATA, new Object[]{Integer.valueOf(countBg), packageStats.keyAt(i5), processStats.keyAt(iu2), Long.valueOf(batteryUptime3 / 1000), Integer.valueOf(iw), Integer.valueOf(launches)});
                            }
                            iu2--;
                            batteryUptime3 = batteryUptime4;
                            i4 = alarms;
                            ps2 = ps3;
                            str7 = str5;
                        }
                        Object obj2 = i4;
                        batteryUptime4 = batteryUptime3;
                        i5--;
                        processStats = processStats2;
                        str7 = str7;
                    }
                    str5 = str7;
                    batteryUptime4 = batteryUptime3;
                } else {
                    cpuFreqs2 = cpuFreqs;
                    int i9 = 2;
                    uidStats5 = uidStats6;
                    NU4 = i4;
                    str5 = str2;
                    sb4 = sb3;
                    unoptimizedScanMaxTimeBg = rawRealtime2;
                    batteryUptime4 = batteryUptime3;
                    rawRealtimeMs4 = rawRealtimeMs3;
                    sippers3 = sippers;
                    screenOffRpmStats3 = screenOffRpmStats;
                    iu3 = iu2;
                    rpmStats2 = rpmStats;
                }
                iu2 = iu3 + 1;
                batteryUptime3 = batteryUptime4;
                sb3 = sb4;
                screenOffRpmStats = screenOffRpmStats3;
                rpmStats = rpmStats2;
                NU = NU4;
                uidStats2 = uidStats5;
                cpuFreqs = cpuFreqs2;
                rawRealtime2 = unoptimizedScanMaxTimeBg;
                str2 = str5;
                rawRealtimeMs3 = rawRealtimeMs4;
                sippers = sippers3;
            } else {
                return;
            }
        }
    }

    private void printmAh(PrintWriter printer, double power) {
        printer.print(BatteryStatsHelper.makemAh(power));
    }

    private void printmAh(StringBuilder sb, double power) {
        sb.append(BatteryStatsHelper.makemAh(power));
    }

    public final void dumpLocked(Context context, PrintWriter pw, String prefix, int which, int reqUid) {
        dumpLocked(context, pw, prefix, which, reqUid, BatteryStatsHelper.checkWifiOnly(context));
    }

    /* JADX WARNING: Removed duplicated region for block: B:466:0x19b9  */
    /* JADX WARNING: Removed duplicated region for block: B:463:0x19a2  */
    /* JADX WARNING: Removed duplicated region for block: B:473:0x19f3  */
    /* JADX WARNING: Removed duplicated region for block: B:469:0x19e2  */
    /* JADX WARNING: Removed duplicated region for block: B:484:0x1a49  */
    /* JADX WARNING: Removed duplicated region for block: B:406:0x1858  */
    /* JADX WARNING: Removed duplicated region for block: B:501:0x1aad  */
    /* JADX WARNING: Removed duplicated region for block: B:487:0x1a64  */
    /* JADX WARNING: Removed duplicated region for block: B:504:0x1ad4  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x1ce1  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x1bee  */
    /* JADX WARNING: Removed duplicated region for block: B:569:0x1d2e  */
    /* JADX WARNING: Removed duplicated region for block: B:565:0x1cf1  */
    /* JADX WARNING: Removed duplicated region for block: B:572:0x1d40  */
    /* JADX WARNING: Removed duplicated region for block: B:590:0x1e12  */
    /* JADX WARNING: Removed duplicated region for block: B:607:0x1ead  */
    /* JADX WARNING: Removed duplicated region for block: B:618:0x1f1b  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x1fb3  */
    /* JADX WARNING: Removed duplicated region for block: B:654:0x20ec  */
    /* JADX WARNING: Removed duplicated region for block: B:661:0x213e  */
    /* JADX WARNING: Removed duplicated region for block: B:675:0x21cf  */
    /* JADX WARNING: Removed duplicated region for block: B:670:0x2197  */
    /* JADX WARNING: Removed duplicated region for block: B:678:0x21d9  */
    /* JADX WARNING: Removed duplicated region for block: B:686:0x2208  */
    /* JADX WARNING: Removed duplicated region for block: B:705:0x22cf  */
    /* JADX WARNING: Removed duplicated region for block: B:757:0x2462  */
    /* JADX WARNING: Removed duplicated region for block: B:821:0x256e A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:775:0x2566  */
    /* JADX WARNING: Removed duplicated region for block: B:396:0x17df  */
    /* JADX WARNING: Removed duplicated region for block: B:395:0x17c6  */
    /* JADX WARNING: Removed duplicated region for block: B:402:0x182c  */
    /* JADX WARNING: Removed duplicated region for block: B:399:0x1822  */
    /* JADX WARNING: Removed duplicated region for block: B:406:0x1858  */
    /* JADX WARNING: Removed duplicated region for block: B:484:0x1a49  */
    /* JADX WARNING: Removed duplicated region for block: B:487:0x1a64  */
    /* JADX WARNING: Removed duplicated region for block: B:501:0x1aad  */
    /* JADX WARNING: Removed duplicated region for block: B:504:0x1ad4  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x1bee  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x1ce1  */
    /* JADX WARNING: Removed duplicated region for block: B:565:0x1cf1  */
    /* JADX WARNING: Removed duplicated region for block: B:569:0x1d2e  */
    /* JADX WARNING: Removed duplicated region for block: B:572:0x1d40  */
    /* JADX WARNING: Removed duplicated region for block: B:590:0x1e12  */
    /* JADX WARNING: Removed duplicated region for block: B:607:0x1ead  */
    /* JADX WARNING: Removed duplicated region for block: B:618:0x1f1b  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x1fb3  */
    /* JADX WARNING: Removed duplicated region for block: B:654:0x20ec  */
    /* JADX WARNING: Removed duplicated region for block: B:661:0x213e  */
    /* JADX WARNING: Removed duplicated region for block: B:664:0x2164  */
    /* JADX WARNING: Removed duplicated region for block: B:670:0x2197  */
    /* JADX WARNING: Removed duplicated region for block: B:675:0x21cf  */
    /* JADX WARNING: Removed duplicated region for block: B:678:0x21d9  */
    /* JADX WARNING: Removed duplicated region for block: B:686:0x2208  */
    /* JADX WARNING: Removed duplicated region for block: B:705:0x22cf  */
    /* JADX WARNING: Removed duplicated region for block: B:757:0x2462  */
    /* JADX WARNING: Removed duplicated region for block: B:775:0x2566  */
    /* JADX WARNING: Removed duplicated region for block: B:821:0x256e A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:391:0x16dc  */
    /* JADX WARNING: Removed duplicated region for block: B:377:0x1684  */
    /* JADX WARNING: Removed duplicated region for block: B:395:0x17c6  */
    /* JADX WARNING: Removed duplicated region for block: B:396:0x17df  */
    /* JADX WARNING: Removed duplicated region for block: B:399:0x1822  */
    /* JADX WARNING: Removed duplicated region for block: B:402:0x182c  */
    /* JADX WARNING: Removed duplicated region for block: B:484:0x1a49  */
    /* JADX WARNING: Removed duplicated region for block: B:406:0x1858  */
    /* JADX WARNING: Removed duplicated region for block: B:501:0x1aad  */
    /* JADX WARNING: Removed duplicated region for block: B:487:0x1a64  */
    /* JADX WARNING: Removed duplicated region for block: B:504:0x1ad4  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x1ce1  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x1bee  */
    /* JADX WARNING: Removed duplicated region for block: B:569:0x1d2e  */
    /* JADX WARNING: Removed duplicated region for block: B:565:0x1cf1  */
    /* JADX WARNING: Removed duplicated region for block: B:572:0x1d40  */
    /* JADX WARNING: Removed duplicated region for block: B:590:0x1e12  */
    /* JADX WARNING: Removed duplicated region for block: B:607:0x1ead  */
    /* JADX WARNING: Removed duplicated region for block: B:618:0x1f1b  */
    /* JADX WARNING: Removed duplicated region for block: B:621:0x1fb3  */
    /* JADX WARNING: Removed duplicated region for block: B:654:0x20ec  */
    /* JADX WARNING: Removed duplicated region for block: B:661:0x213e  */
    /* JADX WARNING: Removed duplicated region for block: B:664:0x2164  */
    /* JADX WARNING: Removed duplicated region for block: B:675:0x21cf  */
    /* JADX WARNING: Removed duplicated region for block: B:670:0x2197  */
    /* JADX WARNING: Removed duplicated region for block: B:678:0x21d9  */
    /* JADX WARNING: Removed duplicated region for block: B:686:0x2208  */
    /* JADX WARNING: Removed duplicated region for block: B:705:0x22cf  */
    /* JADX WARNING: Removed duplicated region for block: B:757:0x2462  */
    /* JADX WARNING: Removed duplicated region for block: B:821:0x256e A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:775:0x2566  */
    public final void dumpLocked(android.content.Context r227, java.io.PrintWriter r228, java.lang.String r229, int r230, int r231, boolean r232) {
        /*
        r226 = this;
        r7 = r226;
        r15 = r228;
        r14 = r229;
        r13 = r230;
        r11 = r231;
        if (r13 == 0) goto L_0x0026;
    L_0x000c:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "ERROR: BatteryStats.dump called for which type ";
        r0.append(r1);
        r0.append(r13);
        r1 = " but only STATS_SINCE_CHARGED is supported";
        r0.append(r1);
        r0 = r0.toString();
        r15.println(r0);
        return;
    L_0x0026:
        r0 = android.os.SystemClock.uptimeMillis();
        r16 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r9 = r0 * r16;
        r0 = android.os.SystemClock.elapsedRealtime();
        r5 = r0 * r16;
        r18 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r0 = r5 + r18;
        r3 = r0 / r16;
        r1 = r7.getBatteryUptime(r9);
        r20 = r3;
        r3 = r7.computeBatteryUptime(r9, r13);
        r22 = r1;
        r1 = r7.computeBatteryRealtime(r5, r13);
        r11 = r7.computeRealtime(r5, r13);
        r24 = r7.computeUptime(r9, r13);
        r26 = r3;
        r3 = r7.computeBatteryScreenOffUptime(r9, r13);
        r28 = r9;
        r9 = r7.computeBatteryScreenOffRealtime(r5, r13);
        r30 = r7.computeBatteryTimeRemaining(r5);
        r32 = r7.computeChargeTimeRemaining(r5);
        r34 = r3;
        r3 = r7.getScreenDozeTime(r5, r13);
        r0 = new java.lang.StringBuilder;
        r8 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r0.<init>(r8);
        r8 = r0;
        r0 = r226.getUidStats();
        r36 = r5;
        r5 = r0.size();
        r6 = r226.getEstimatedBatteryCapacity();
        r38 = r0;
        r0 = " mAh";
        r39 = r5;
        r5 = 0;
        if (r6 <= 0) goto L_0x00ab;
    L_0x008b:
        r8.setLength(r5);
        r8.append(r14);
        r5 = "  Estimated battery capacity: ";
        r8.append(r5);
        r41 = r3;
        r3 = (double) r6;
        r3 = com.android.internal.os.BatteryStatsHelper.makemAh(r3);
        r8.append(r3);
        r8.append(r0);
        r3 = r8.toString();
        r15.println(r3);
        goto L_0x00ad;
    L_0x00ab:
        r41 = r3;
    L_0x00ad:
        r5 = r226.getMinLearnedBatteryCapacity();
        if (r5 <= 0) goto L_0x00d3;
    L_0x00b3:
        r3 = 0;
        r8.setLength(r3);
        r8.append(r14);
        r3 = "  Min learned battery capacity: ";
        r8.append(r3);
        r3 = r5 / 1000;
        r3 = (double) r3;
        r3 = com.android.internal.os.BatteryStatsHelper.makemAh(r3);
        r8.append(r3);
        r8.append(r0);
        r3 = r8.toString();
        r15.println(r3);
    L_0x00d3:
        r4 = r226.getMaxLearnedBatteryCapacity();
        if (r4 <= 0) goto L_0x00fc;
    L_0x00d9:
        r3 = 0;
        r8.setLength(r3);
        r8.append(r14);
        r3 = "  Max learned battery capacity: ";
        r8.append(r3);
        r3 = r4 / 1000;
        r43 = r4;
        r3 = (double) r3;
        r3 = com.android.internal.os.BatteryStatsHelper.makemAh(r3);
        r8.append(r3);
        r8.append(r0);
        r3 = r8.toString();
        r15.println(r3);
        goto L_0x00fe;
    L_0x00fc:
        r43 = r4;
    L_0x00fe:
        r3 = 0;
        r8.setLength(r3);
        r8.append(r14);
        r3 = "  Time on battery: ";
        r8.append(r3);
        r3 = r1 / r16;
        formatTimeMs(r8, r3);
        r4 = "(";
        r8.append(r4);
        r3 = r7.formatRatioLocked(r1, r11);
        r8.append(r3);
        r3 = ") realtime, ";
        r8.append(r3);
        r45 = r5;
        r44 = r6;
        r5 = r26 / r16;
        formatTimeMs(r8, r5);
        r8.append(r4);
        r5 = r26;
        r3 = r7.formatRatioLocked(r5, r1);
        r8.append(r3);
        r3 = ") uptime";
        r8.append(r3);
        r3 = r8.toString();
        r15.println(r3);
        r3 = 0;
        r8.setLength(r3);
        r8.append(r14);
        r3 = "  Time on battery screen off: ";
        r8.append(r3);
        r5 = r9 / r16;
        formatTimeMs(r8, r5);
        r8.append(r4);
        r3 = r7.formatRatioLocked(r9, r1);
        r8.append(r3);
        r3 = ") realtime, ";
        r8.append(r3);
        r5 = r34 / r16;
        formatTimeMs(r8, r5);
        r8.append(r4);
        r5 = r34;
        r3 = r7.formatRatioLocked(r5, r1);
        r8.append(r3);
        r3 = ") uptime";
        r8.append(r3);
        r3 = r8.toString();
        r15.println(r3);
        r3 = 0;
        r8.setLength(r3);
        r8.append(r14);
        r3 = "  Time on battery screen doze: ";
        r8.append(r3);
        r5 = r41 / r16;
        formatTimeMs(r8, r5);
        r8.append(r4);
        r5 = r41;
        r3 = r7.formatRatioLocked(r5, r1);
        r8.append(r3);
        r3 = ")";
        r8.append(r3);
        r5 = r8.toString();
        r15.println(r5);
        r5 = 0;
        r8.setLength(r5);
        r8.append(r14);
        r5 = "  Total run time: ";
        r8.append(r5);
        r5 = r11 / r16;
        formatTimeMs(r8, r5);
        r5 = "realtime, ";
        r8.append(r5);
        r5 = r24 / r16;
        formatTimeMs(r8, r5);
        r5 = "uptime";
        r8.append(r5);
        r5 = r8.toString();
        r15.println(r5);
        r46 = 0;
        r5 = (r30 > r46 ? 1 : (r30 == r46 ? 0 : -1));
        if (r5 < 0) goto L_0x01f1;
    L_0x01d9:
        r5 = 0;
        r8.setLength(r5);
        r8.append(r14);
        r5 = "  Battery time remaining: ";
        r8.append(r5);
        r5 = r30 / r16;
        formatTimeMs(r8, r5);
        r5 = r8.toString();
        r15.println(r5);
    L_0x01f1:
        r5 = (r32 > r46 ? 1 : (r32 == r46 ? 0 : -1));
        if (r5 < 0) goto L_0x020d;
    L_0x01f5:
        r5 = 0;
        r8.setLength(r5);
        r8.append(r14);
        r5 = "  Charge time remaining: ";
        r8.append(r5);
        r5 = r32 / r16;
        formatTimeMs(r8, r5);
        r5 = r8.toString();
        r15.println(r5);
    L_0x020d:
        r5 = r7.getUahDischarge(r13);
        r48 = (r5 > r46 ? 1 : (r5 == r46 ? 0 : -1));
        if (r48 < 0) goto L_0x023d;
    L_0x0215:
        r48 = r9;
        r9 = 0;
        r8.setLength(r9);
        r8.append(r14);
        r9 = "  Discharge: ";
        r8.append(r9);
        r9 = (double) r5;
        r50 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r9 = r9 / r50;
        r9 = com.android.internal.os.BatteryStatsHelper.makemAh(r9);
        r8.append(r9);
        r8.append(r0);
        r9 = r8.toString();
        r15.println(r9);
        goto L_0x023f;
    L_0x023d:
        r48 = r9;
    L_0x023f:
        r9 = r7.getUahDischargeScreenOff(r13);
        r50 = (r9 > r46 ? 1 : (r9 == r46 ? 0 : -1));
        if (r50 < 0) goto L_0x026f;
    L_0x0247:
        r50 = r11;
        r11 = 0;
        r8.setLength(r11);
        r8.append(r14);
        r11 = "  Screen off discharge: ";
        r8.append(r11);
        r11 = (double) r9;
        r52 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r11 = r11 / r52;
        r11 = com.android.internal.os.BatteryStatsHelper.makemAh(r11);
        r8.append(r11);
        r8.append(r0);
        r11 = r8.toString();
        r15.println(r11);
        goto L_0x0271;
    L_0x026f:
        r50 = r11;
    L_0x0271:
        r11 = r7.getUahDischargeScreenDoze(r13);
        r52 = (r11 > r46 ? 1 : (r11 == r46 ? 0 : -1));
        if (r52 < 0) goto L_0x02a3;
    L_0x0279:
        r52 = r3;
        r3 = 0;
        r8.setLength(r3);
        r8.append(r14);
        r3 = "  Screen doze discharge: ";
        r8.append(r3);
        r53 = r1;
        r1 = (double) r11;
        r55 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r1 = r1 / r55;
        r1 = com.android.internal.os.BatteryStatsHelper.makemAh(r1);
        r8.append(r1);
        r8.append(r0);
        r1 = r8.toString();
        r15.println(r1);
        goto L_0x02a7;
    L_0x02a3:
        r53 = r1;
        r52 = r3;
    L_0x02a7:
        r2 = r5 - r9;
        r1 = (r2 > r46 ? 1 : (r2 == r46 ? 0 : -1));
        if (r1 < 0) goto L_0x02d5;
    L_0x02ad:
        r1 = 0;
        r8.setLength(r1);
        r8.append(r14);
        r1 = "  Screen on discharge: ";
        r8.append(r1);
        r55 = r5;
        r5 = (double) r2;
        r57 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r5 = r5 / r57;
        r1 = com.android.internal.os.BatteryStatsHelper.makemAh(r5);
        r8.append(r1);
        r8.append(r0);
        r1 = r8.toString();
        r15.println(r1);
        goto L_0x02d7;
    L_0x02d5:
        r55 = r5;
    L_0x02d7:
        r5 = r7.getUahDischargeLightDoze(r13);
        r1 = (r5 > r46 ? 1 : (r5 == r46 ? 0 : -1));
        if (r1 < 0) goto L_0x0307;
    L_0x02df:
        r1 = 0;
        r8.setLength(r1);
        r8.append(r14);
        r1 = "  Device light doze discharge: ";
        r8.append(r1);
        r57 = r2;
        r1 = (double) r5;
        r59 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r1 = r1 / r59;
        r1 = com.android.internal.os.BatteryStatsHelper.makemAh(r1);
        r8.append(r1);
        r8.append(r0);
        r1 = r8.toString();
        r15.println(r1);
        goto L_0x0309;
    L_0x0307:
        r57 = r2;
    L_0x0309:
        r2 = r7.getUahDischargeDeepDoze(r13);
        r1 = (r2 > r46 ? 1 : (r2 == r46 ? 0 : -1));
        if (r1 < 0) goto L_0x0339;
    L_0x0311:
        r1 = 0;
        r8.setLength(r1);
        r8.append(r14);
        r1 = "  Device deep doze discharge: ";
        r8.append(r1);
        r59 = r5;
        r5 = (double) r2;
        r61 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r5 = r5 / r61;
        r1 = com.android.internal.os.BatteryStatsHelper.makemAh(r5);
        r8.append(r1);
        r8.append(r0);
        r0 = r8.toString();
        r15.println(r0);
        goto L_0x033b;
    L_0x0339:
        r59 = r5;
    L_0x033b:
        r0 = "  Start clock time: ";
        r15.print(r0);
        r0 = r226.getStartClockTime();
        r5 = "yyyy-MM-dd-HH-mm-ss";
        r0 = android.text.format.DateFormat.format(r5, r0);
        r0 = r0.toString();
        r15.println(r0);
        r5 = r36;
        r0 = r7.getScreenOnTime(r5, r13);
        r36 = r11;
        r11 = r7.getInteractiveTime(r5, r13);
        r61 = r9;
        r9 = r7.getPowerSaveModeEnabledTime(r5, r13);
        r63 = r2;
        r3 = 1;
        r65 = r9;
        r9 = r7.getDeviceIdleModeTime(r3, r5, r13);
        r2 = 2;
        r67 = r9;
        r9 = r7.getDeviceIdleModeTime(r2, r5, r13);
        r69 = r9;
        r9 = r7.getDeviceIdlingTime(r3, r5, r13);
        r71 = r4;
        r3 = r7.getDeviceIdlingTime(r2, r5, r13);
        r74 = r3;
        r2 = r7.getPhoneOnTime(r5, r13);
        r76 = r7.getGlobalWifiRunningTime(r5, r13);
        r78 = r7.getWifiOnTime(r5, r13);
        r4 = 0;
        r8.setLength(r4);
        r8.append(r14);
        r4 = "  Screen on: ";
        r8.append(r4);
        r80 = r2;
        r2 = r0 / r16;
        formatTimeMs(r8, r2);
        r4 = r71;
        r8.append(r4);
        r2 = r53;
        r53 = r9;
        r9 = r7.formatRatioLocked(r0, r2);
        r8.append(r9);
        r10 = ") ";
        r8.append(r10);
        r9 = r7.getScreenOnCount(r13);
        r8.append(r9);
        r9 = "x, Interactive: ";
        r8.append(r9);
        r71 = r10;
        r9 = r11 / r16;
        formatTimeMs(r8, r9);
        r8.append(r4);
        r9 = r7.formatRatioLocked(r11, r2);
        r8.append(r9);
        r9 = r52;
        r8.append(r9);
        r10 = r8.toString();
        r15.println(r10);
        r10 = 0;
        r8.setLength(r10);
        r8.append(r14);
        r10 = "  Screen brightnesses:";
        r8.append(r10);
        r10 = 0;
        r52 = 0;
        r218 = r52;
        r52 = r10;
        r10 = r218;
    L_0x03f5:
        r82 = r11;
        r11 = 5;
        r12 = " ";
        if (r10 >= r11) goto L_0x0437;
    L_0x03fc:
        r84 = r2;
        r2 = r7.getScreenBrightnessTime(r10, r5, r13);
        r11 = (r2 > r46 ? 1 : (r2 == r46 ? 0 : -1));
        if (r11 != 0) goto L_0x0407;
    L_0x0406:
        goto L_0x0430;
    L_0x0407:
        r11 = "\n    ";
        r8.append(r11);
        r8.append(r14);
        r11 = 1;
        r52 = SCREEN_BRIGHTNESS_NAMES;
        r86 = r11;
        r11 = r52[r10];
        r8.append(r11);
        r8.append(r12);
        r11 = r2 / r16;
        formatTimeMs(r8, r11);
        r8.append(r4);
        r11 = r7.formatRatioLocked(r2, r0);
        r8.append(r11);
        r8.append(r9);
        r52 = r86;
    L_0x0430:
        r10 = r10 + 1;
        r11 = r82;
        r2 = r84;
        goto L_0x03f5;
    L_0x0437:
        r84 = r2;
        if (r52 != 0) goto L_0x0440;
    L_0x043b:
        r2 = " (no activity)";
        r8.append(r2);
    L_0x0440:
        r2 = r8.toString();
        r15.println(r2);
        r2 = (r65 > r46 ? 1 : (r65 == r46 ? 0 : -1));
        if (r2 == 0) goto L_0x0477;
    L_0x044b:
        r2 = 0;
        r8.setLength(r2);
        r8.append(r14);
        r2 = "  Power save mode enabled: ";
        r8.append(r2);
        r2 = r65 / r16;
        formatTimeMs(r8, r2);
        r8.append(r4);
        r10 = r65;
        r2 = r84;
        r65 = r0;
        r0 = r7.formatRatioLocked(r10, r2);
        r8.append(r0);
        r8.append(r9);
        r0 = r8.toString();
        r15.println(r0);
        goto L_0x047d;
    L_0x0477:
        r10 = r65;
        r2 = r84;
        r65 = r0;
    L_0x047d:
        r0 = (r53 > r46 ? 1 : (r53 == r46 ? 0 : -1));
        if (r0 == 0) goto L_0x04bd;
    L_0x0481:
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Device light idling: ";
        r8.append(r0);
        r0 = r53 / r16;
        formatTimeMs(r8, r0);
        r8.append(r4);
        r0 = r53;
        r53 = r10;
        r10 = r7.formatRatioLocked(r0, r2);
        r8.append(r10);
        r10 = r71;
        r8.append(r10);
        r84 = r0;
        r11 = 1;
        r0 = r7.getDeviceIdlingCount(r11, r13);
        r8.append(r0);
        r0 = "x";
        r8.append(r0);
        r0 = r8.toString();
        r15.println(r0);
        goto L_0x04c3;
    L_0x04bd:
        r84 = r53;
        r53 = r10;
        r10 = r71;
    L_0x04c3:
        r0 = (r67 > r46 ? 1 : (r67 == r46 ? 0 : -1));
        if (r0 == 0) goto L_0x0508;
    L_0x04c7:
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Idle mode light time: ";
        r8.append(r0);
        r0 = r67 / r16;
        formatTimeMs(r8, r0);
        r8.append(r4);
        r0 = r67;
        r11 = r7.formatRatioLocked(r0, r2);
        r8.append(r11);
        r8.append(r10);
        r11 = 1;
        r0 = r7.getDeviceIdleModeCount(r11, r13);
        r8.append(r0);
        r0 = "x";
        r8.append(r0);
        r0 = " -- longest ";
        r8.append(r0);
        r0 = r7.getLongestDeviceIdleModeTime(r11);
        formatTimeMs(r8, r0);
        r0 = r8.toString();
        r15.println(r0);
    L_0x0508:
        r0 = (r74 > r46 ? 1 : (r74 == r46 ? 0 : -1));
        if (r0 == 0) goto L_0x0541;
    L_0x050c:
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Device full idling: ";
        r8.append(r0);
        r0 = r74 / r16;
        formatTimeMs(r8, r0);
        r8.append(r4);
        r0 = r74;
        r11 = r7.formatRatioLocked(r0, r2);
        r8.append(r11);
        r8.append(r10);
        r11 = 2;
        r0 = r7.getDeviceIdlingCount(r11, r13);
        r8.append(r0);
        r0 = "x";
        r8.append(r0);
        r0 = r8.toString();
        r15.println(r0);
    L_0x0541:
        r0 = (r69 > r46 ? 1 : (r69 == r46 ? 0 : -1));
        if (r0 == 0) goto L_0x0586;
    L_0x0545:
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Idle mode full time: ";
        r8.append(r0);
        r0 = r69 / r16;
        formatTimeMs(r8, r0);
        r8.append(r4);
        r0 = r69;
        r11 = r7.formatRatioLocked(r0, r2);
        r8.append(r11);
        r8.append(r10);
        r11 = 2;
        r0 = r7.getDeviceIdleModeCount(r11, r13);
        r8.append(r0);
        r0 = "x";
        r8.append(r0);
        r0 = " -- longest ";
        r8.append(r0);
        r0 = r7.getLongestDeviceIdleModeTime(r11);
        formatTimeMs(r8, r0);
        r0 = r8.toString();
        r15.println(r0);
    L_0x0586:
        r0 = (r80 > r46 ? 1 : (r80 == r46 ? 0 : -1));
        if (r0 == 0) goto L_0x05b8;
    L_0x058a:
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Active phone call: ";
        r8.append(r0);
        r0 = r80 / r16;
        formatTimeMs(r8, r0);
        r8.append(r4);
        r0 = r80;
        r11 = r7.formatRatioLocked(r0, r2);
        r8.append(r11);
        r8.append(r10);
        r11 = r7.getPhoneOnCount(r13);
        r8.append(r11);
        r11 = "x";
        r8.append(r11);
        goto L_0x05ba;
    L_0x05b8:
        r0 = r80;
    L_0x05ba:
        r11 = r7.getNumConnectivityChange(r13);
        if (r11 == 0) goto L_0x05ce;
    L_0x05c0:
        r228.print(r229);
        r80 = r0;
        r0 = "  Connectivity changes: ";
        r15.print(r0);
        r15.println(r11);
        goto L_0x05d0;
    L_0x05ce:
        r80 = r0;
    L_0x05d0:
        r0 = 0;
        r86 = 0;
        r71 = new java.util.ArrayList;
        r71.<init>();
        r88 = r71;
        r71 = 0;
        r89 = r86;
        r86 = r0;
        r0 = r71;
    L_0x05e3:
        r1 = r39;
        if (r0 >= r1) goto L_0x067f;
    L_0x05e7:
        r39 = r1;
        r1 = r38;
        r38 = r1.valueAt(r0);
        r38 = (android.os.BatteryStats.Uid) r38;
        r71 = r1;
        r1 = r38.getWakelockStats();
        r91 = r1.size();
        r92 = r11;
        r11 = 1;
        r91 = r91 + -1;
        r11 = r91;
    L_0x0603:
        if (r11 < 0) goto L_0x066d;
    L_0x0605:
        r91 = r1.valueAt(r11);
        r93 = r10;
        r10 = r91;
        r10 = (android.os.BatteryStats.Uid.Wakelock) r10;
        r94 = r9;
        r91 = r12;
        r12 = 1;
        r9 = r10.getWakeTime(r12);
        if (r9 == 0) goto L_0x0620;
    L_0x061a:
        r95 = r9.getTotalTimeLocked(r5, r13);
        r86 = r86 + r95;
    L_0x0620:
        r101 = r9;
        r12 = 0;
        r9 = r10.getWakeTime(r12);
        if (r9 == 0) goto L_0x065e;
    L_0x0629:
        r102 = r9.getTotalTimeLocked(r5, r13);
        r12 = (r102 > r46 ? 1 : (r102 == r46 ? 0 : -1));
        if (r12 <= 0) goto L_0x0659;
    L_0x0631:
        if (r231 >= 0) goto L_0x0652;
    L_0x0633:
        r12 = new android.os.BatteryStats$TimerEntry;
        r95 = r1.keyAt(r11);
        r96 = r95;
        r96 = (java.lang.String) r96;
        r97 = r38.getUid();
        r95 = r12;
        r98 = r9;
        r99 = r102;
        r95.<init>(r96, r97, r98, r99);
        r95 = r10;
        r10 = r88;
        r10.add(r12);
        goto L_0x0656;
    L_0x0652:
        r95 = r10;
        r10 = r88;
    L_0x0656:
        r89 = r89 + r102;
        goto L_0x0662;
    L_0x0659:
        r95 = r10;
        r10 = r88;
        goto L_0x0662;
    L_0x065e:
        r95 = r10;
        r10 = r88;
    L_0x0662:
        r11 = r11 + -1;
        r88 = r10;
        r12 = r91;
        r10 = r93;
        r9 = r94;
        goto L_0x0603;
    L_0x066d:
        r94 = r9;
        r93 = r10;
        r91 = r12;
        r10 = r88;
        r0 = r0 + 1;
        r38 = r71;
        r11 = r92;
        r10 = r93;
        goto L_0x05e3;
    L_0x067f:
        r39 = r1;
        r94 = r9;
        r93 = r10;
        r92 = r11;
        r91 = r12;
        r71 = r38;
        r10 = r88;
        r0 = 0;
        r11 = r7.getNetworkActivityBytes(r0, r13);
        r9 = 1;
        r0 = r7.getNetworkActivityBytes(r9, r13);
        r38 = r10;
        r95 = r11;
        r9 = 2;
        r10 = r7.getNetworkActivityBytes(r9, r13);
        r12 = 3;
        r97 = r10;
        r9 = r7.getNetworkActivityBytes(r12, r13);
        r99 = r9;
        r11 = 0;
        r9 = r7.getNetworkActivityPackets(r11, r13);
        r101 = r9;
        r11 = 1;
        r9 = r7.getNetworkActivityPackets(r11, r13);
        r72 = r9;
        r12 = 2;
        r9 = r7.getNetworkActivityPackets(r12, r13);
        r11 = 3;
        r11 = r7.getNetworkActivityPackets(r11, r13);
        r104 = r0;
        r0 = 4;
        r0 = r7.getNetworkActivityBytes(r0, r13);
        r106 = r0;
        r0 = 5;
        r0 = r7.getNetworkActivityBytes(r0, r13);
        r108 = (r86 > r46 ? 1 : (r86 == r46 ? 0 : -1));
        if (r108 == 0) goto L_0x06f0;
    L_0x06d3:
        r108 = r0;
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Total full wakelock time: ";
        r8.append(r0);
        r0 = r86 + r18;
        r0 = r0 / r16;
        formatTimeMsNoSpace(r8, r0);
        r0 = r8.toString();
        r15.println(r0);
        goto L_0x06f2;
    L_0x06f0:
        r108 = r0;
    L_0x06f2:
        r0 = (r89 > r46 ? 1 : (r89 == r46 ? 0 : -1));
        if (r0 == 0) goto L_0x0710;
    L_0x06f6:
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Total partial wakelock time: ";
        r8.append(r0);
        r0 = r89 + r18;
        r0 = r0 / r16;
        formatTimeMsNoSpace(r8, r0);
        r0 = r8.toString();
        r15.println(r0);
        r110 = r7.getWifiMulticastWakelockTime(r5, r13);
        r1 = r7.getWifiMulticastWakelockCount(r13);
        r0 = (r110 > r46 ? 1 : (r110 == r46 ? 0 : -1));
        if (r0 == 0) goto L_0x0750;
    L_0x071d:
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Total WiFi Multicast wakelock Count: ";
        r8.append(r0);
        r8.append(r1);
        r0 = r8.toString();
        r15.println(r0);
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Total WiFi Multicast wakelock time: ";
        r8.append(r0);
        r112 = r110 + r18;
        r114 = r1;
        r0 = r112 / r16;
        formatTimeMsNoSpace(r8, r0);
        r0 = r8.toString();
        r15.println(r0);
        goto L_0x0752;
    L_0x0750:
        r114 = r1;
    L_0x0752:
        r0 = "";
        r15.println(r0);
        r228.print(r229);
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  CONNECTIVITY POWER SUMMARY START";
        r8.append(r0);
        r0 = r8.toString();
        r15.println(r0);
        r228.print(r229);
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Logging duration for connectivity statistics: ";
        r8.append(r0);
        r0 = r2 / r16;
        formatTimeMs(r8, r0);
        r0 = r8.toString();
        r15.println(r0);
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Cellular Statistics:";
        r8.append(r0);
        r0 = r8.toString();
        r15.println(r0);
        r228.print(r229);
        r1 = 0;
        r8.setLength(r1);
        r8.append(r14);
        r0 = "     Cellular kernel active time: ";
        r8.append(r0);
        r112 = r11;
        r11 = r7.getMobileRadioActiveTime(r5, r13);
        r115 = r2;
        r1 = r11 / r16;
        formatTimeMs(r8, r1);
        r8.append(r4);
        r1 = r115;
        r0 = r7.formatRatioLocked(r11, r1);
        r8.append(r0);
        r3 = r94;
        r8.append(r3);
        r0 = r8.toString();
        r15.println(r0);
        r94 = r226.getModemControllerActivity();
        r115 = "Cellular";
        r117 = r71;
        r118 = r106;
        r120 = r108;
        r218 = r11;
        r11 = r104;
        r104 = r218;
        r220 = r74;
        r74 = r84;
        r84 = r80;
        r80 = r220;
        r0 = r226;
        r122 = r22;
        r40 = r114;
        r71 = 0;
        r22 = r9;
        r9 = r1;
        r1 = r228;
        r2 = r8;
        r127 = r3;
        r125 = r20;
        r20 = r26;
        r26 = r34;
        r34 = r41;
        r41 = r80;
        r3 = r229;
        r80 = r9;
        r9 = r4;
        r4 = r115;
        r106 = r5;
        r88 = r9;
        r6 = r39;
        r39 = r45;
        r9 = r71;
        r5 = r94;
        r10 = r6;
        r6 = r230;
        r0.printControllerActivity(r1, r2, r3, r4, r5, r6);
        r0 = "     Cellular data received: ";
        r15.print(r0);
        r5 = r95;
        r0 = r7.formatBytesLocked(r5);
        r15.println(r0);
        r0 = "     Cellular data sent: ";
        r15.print(r0);
        r0 = r7.formatBytesLocked(r11);
        r15.println(r0);
        r0 = "     Cellular packets received: ";
        r15.print(r0);
        r3 = r101;
        r15.println(r3);
        r0 = "     Cellular packets sent: ";
        r15.print(r0);
        r1 = r72;
        r15.println(r1);
        r8.setLength(r9);
        r8.append(r14);
        r0 = "     Cellular Radio Access Technology:";
        r8.append(r0);
        r0 = 0;
        r45 = 0;
        r218 = r45;
        r45 = r0;
        r0 = r218;
    L_0x085f:
        r9 = 22;
        if (r0 >= r9) goto L_0x08d3;
    L_0x0863:
        r72 = r1;
        r52 = r10;
        r9 = r106;
        r1 = r7.getPhoneDataConnectionTime(r0, r9, r13);
        r94 = (r1 > r46 ? 1 : (r1 == r46 ? 0 : -1));
        if (r94 != 0) goto L_0x0880;
    L_0x0871:
        r101 = r3;
        r95 = r5;
        r106 = r11;
        r11 = r80;
        r6 = r88;
        r4 = r91;
        r5 = r93;
        goto L_0x08bb;
    L_0x0880:
        r101 = r3;
        r3 = "\n       ";
        r8.append(r3);
        r8.append(r14);
        r3 = 1;
        r4 = DATA_CONNECTION_NAMES;
        r45 = r3;
        r3 = r4.length;
        if (r0 >= r3) goto L_0x0895;
    L_0x0892:
        r3 = r4[r0];
        goto L_0x0897;
    L_0x0895:
        r3 = "ERROR";
    L_0x0897:
        r8.append(r3);
        r4 = r91;
        r8.append(r4);
        r95 = r5;
        r5 = r1 / r16;
        formatTimeMs(r8, r5);
        r6 = r88;
        r8.append(r6);
        r106 = r11;
        r11 = r80;
        r3 = r7.formatRatioLocked(r1, r11);
        r8.append(r3);
        r5 = r93;
        r8.append(r5);
    L_0x08bb:
        r0 = r0 + 1;
        r91 = r4;
        r93 = r5;
        r88 = r6;
        r80 = r11;
        r1 = r72;
        r5 = r95;
        r3 = r101;
        r11 = r106;
        r106 = r9;
        r10 = r52;
        r9 = 0;
        goto L_0x085f;
    L_0x08d3:
        r72 = r1;
        r101 = r3;
        r95 = r5;
        r52 = r10;
        r6 = r88;
        r4 = r91;
        r5 = r93;
        r9 = r106;
        r106 = r11;
        r11 = r80;
        if (r45 != 0) goto L_0x08ee;
    L_0x08e9:
        r0 = " (no activity)";
        r8.append(r0);
    L_0x08ee:
        r0 = r8.toString();
        r15.println(r0);
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "     Cellular Rx signal strength (RSRP):";
        r8.append(r0);
        r0 = "very poor (less than -128dBm): ";
        r1 = "poor (-128dBm to -118dBm): ";
        r2 = "moderate (-118dBm to -108dBm): ";
        r3 = "good (-108dBm to -98dBm): ";
        r15 = "great (greater than -98dBm): ";
        r0 = new java.lang.String[]{r0, r1, r2, r3, r15};
        r15 = r0;
        r0 = 0;
        r1 = 5;
        r2 = r15.length;
        r3 = java.lang.Math.min(r1, r2);
        r1 = 0;
        r45 = r0;
    L_0x091f:
        if (r1 >= r3) goto L_0x095c;
    L_0x0921:
        r80 = r3;
        r2 = r7.getPhoneSignalStrengthTime(r1, r9, r13);
        r0 = (r2 > r46 ? 1 : (r2 == r46 ? 0 : -1));
        if (r0 != 0) goto L_0x092e;
    L_0x092b:
        r93 = r9;
        goto L_0x0955;
    L_0x092e:
        r0 = "\n       ";
        r8.append(r0);
        r8.append(r14);
        r0 = 1;
        r45 = r0;
        r0 = r15[r1];
        r8.append(r0);
        r8.append(r4);
        r93 = r9;
        r9 = r2 / r16;
        formatTimeMs(r8, r9);
        r8.append(r6);
        r0 = r7.formatRatioLocked(r2, r11);
        r8.append(r0);
        r8.append(r5);
    L_0x0955:
        r1 = r1 + 1;
        r3 = r80;
        r9 = r93;
        goto L_0x091f;
    L_0x095c:
        r80 = r3;
        r93 = r9;
        if (r45 != 0) goto L_0x0967;
    L_0x0962:
        r0 = " (no activity)";
        r8.append(r0);
    L_0x0967:
        r0 = r8.toString();
        r10 = r228;
        r10.println(r0);
        r228.print(r229);
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Wifi Statistics:";
        r8.append(r0);
        r0 = r8.toString();
        r10.println(r0);
        r228.print(r229);
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "     Wifi kernel active time: ";
        r8.append(r0);
        r2 = r93;
        r0 = r7.getWifiActiveTime(r2, r13);
        r2 = r0 / r16;
        formatTimeMs(r8, r2);
        r8.append(r6);
        r2 = r7.formatRatioLocked(r0, r11);
        r8.append(r2);
        r9 = r127;
        r8.append(r9);
        r2 = r8.toString();
        r10.println(r2);
        r81 = r226.getWifiControllerActivity();
        r88 = "WiFi";
        r108 = r0;
        r0 = r226;
        r1 = r228;
        r115 = r11;
        r11 = r93;
        r2 = r8;
        r93 = r101;
        r3 = r229;
        r91 = r15;
        r15 = r4;
        r4 = r88;
        r9 = r5;
        r5 = r81;
        r81 = r9;
        r9 = r6;
        r6 = r230;
        r0.printControllerActivity(r1, r2, r3, r4, r5, r6);
        r0 = "     Wifi data received: ";
        r10.print(r0);
        r5 = r97;
        r0 = r7.formatBytesLocked(r5);
        r10.println(r0);
        r0 = "     Wifi data sent: ";
        r10.print(r0);
        r3 = r99;
        r0 = r7.formatBytesLocked(r3);
        r10.println(r0);
        r0 = "     Wifi packets received: ";
        r10.print(r0);
        r1 = r22;
        r10.println(r1);
        r0 = "     Wifi packets sent: ";
        r10.print(r0);
        r5 = r112;
        r10.println(r5);
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "     Wifi states:";
        r8.append(r0);
        r0 = 0;
        r22 = 0;
        r218 = r22;
        r22 = r0;
        r0 = r218;
    L_0x0a22:
        r99 = r1;
        r1 = 8;
        if (r0 >= r1) goto L_0x0a72;
    L_0x0a28:
        r1 = r7.getWifiStateTime(r0, r11, r13);
        r23 = (r1 > r46 ? 1 : (r1 == r46 ? 0 : -1));
        if (r23 != 0) goto L_0x0a39;
    L_0x0a30:
        r101 = r3;
        r112 = r5;
        r6 = r81;
        r3 = r115;
        goto L_0x0a65;
    L_0x0a39:
        r101 = r3;
        r3 = "\n       ";
        r8.append(r3);
        r3 = 1;
        r4 = WIFI_STATE_NAMES;
        r4 = r4[r0];
        r8.append(r4);
        r8.append(r15);
        r22 = r3;
        r3 = r1 / r16;
        formatTimeMs(r8, r3);
        r8.append(r9);
        r112 = r5;
        r3 = r115;
        r5 = r7.formatRatioLocked(r1, r3);
        r8.append(r5);
        r6 = r81;
        r8.append(r6);
    L_0x0a65:
        r0 = r0 + 1;
        r115 = r3;
        r81 = r6;
        r1 = r99;
        r3 = r101;
        r5 = r112;
        goto L_0x0a22;
    L_0x0a72:
        r101 = r3;
        r112 = r5;
        r6 = r81;
        r3 = r115;
        if (r22 != 0) goto L_0x0a81;
    L_0x0a7c:
        r0 = " (no activity)";
        r8.append(r0);
    L_0x0a81:
        r0 = r8.toString();
        r10.println(r0);
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "     Wifi supplicant states:";
        r8.append(r0);
        r0 = 0;
        r1 = 0;
    L_0x0a96:
        r2 = 13;
        if (r1 >= r2) goto L_0x0ad5;
    L_0x0a9a:
        r81 = r6;
        r5 = r7.getWifiSupplStateTime(r1, r11, r13);
        r2 = (r5 > r46 ? 1 : (r5 == r46 ? 0 : -1));
        if (r2 != 0) goto L_0x0aa9;
    L_0x0aa4:
        r22 = r11;
        r11 = r81;
        goto L_0x0acf;
    L_0x0aa9:
        r2 = "\n       ";
        r8.append(r2);
        r0 = 1;
        r2 = WIFI_SUPPL_STATE_NAMES;
        r2 = r2[r1];
        r8.append(r2);
        r8.append(r15);
        r22 = r11;
        r11 = r5 / r16;
        formatTimeMs(r8, r11);
        r8.append(r9);
        r2 = r7.formatRatioLocked(r5, r3);
        r8.append(r2);
        r11 = r81;
        r8.append(r11);
    L_0x0acf:
        r1 = r1 + 1;
        r6 = r11;
        r11 = r22;
        goto L_0x0a96;
    L_0x0ad5:
        r22 = r11;
        r11 = r6;
        if (r0 != 0) goto L_0x0adf;
    L_0x0ada:
        r1 = " (no activity)";
        r8.append(r1);
    L_0x0adf:
        r1 = r8.toString();
        r10.println(r1);
        r1 = 0;
        r8.setLength(r1);
        r8.append(r14);
        r1 = "     Wifi Rx signal strength (RSSI):";
        r8.append(r1);
        r1 = "very poor (less than -88.75dBm): ";
        r2 = "poor (-88.75 to -77.5dBm): ";
        r5 = "moderate (-77.5dBm to -66.25dBm): ";
        r6 = "good (-66.25dBm to -55dBm): ";
        r12 = "great (greater than -55dBm): ";
        r1 = new java.lang.String[]{r1, r2, r5, r6, r12};
        r12 = r1;
        r0 = 0;
        r1 = 5;
        r2 = r12.length;
        r6 = java.lang.Math.min(r1, r2);
        r1 = 0;
        r45 = r0;
    L_0x0b10:
        if (r1 >= r6) goto L_0x0b5a;
    L_0x0b12:
        r81 = r6;
        r5 = r22;
        r22 = r11;
        r10 = r7.getWifiSignalStrengthTime(r1, r5, r13);
        r0 = (r10 > r46 ? 1 : (r10 == r46 ? 0 : -1));
        if (r0 != 0) goto L_0x0b25;
    L_0x0b20:
        r114 = r5;
        r6 = r22;
        goto L_0x0b50;
    L_0x0b25:
        r0 = "\n    ";
        r8.append(r0);
        r8.append(r14);
        r0 = 1;
        r2 = "     ";
        r8.append(r2);
        r2 = r12[r1];
        r8.append(r2);
        r114 = r5;
        r5 = r10 / r16;
        formatTimeMs(r8, r5);
        r8.append(r9);
        r2 = r7.formatRatioLocked(r10, r3);
        r8.append(r2);
        r6 = r22;
        r8.append(r6);
        r45 = r0;
    L_0x0b50:
        r1 = r1 + 1;
        r10 = r228;
        r11 = r6;
        r6 = r81;
        r22 = r114;
        goto L_0x0b10;
    L_0x0b5a:
        r81 = r6;
        r6 = r11;
        r114 = r22;
        if (r45 != 0) goto L_0x0b66;
    L_0x0b61:
        r0 = " (no activity)";
        r8.append(r0);
    L_0x0b66:
        r0 = r8.toString();
        r10 = r228;
        r10.println(r0);
        r228.print(r229);
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r1 = "  GPS Statistics:";
        r8.append(r1);
        r1 = r8.toString();
        r10.println(r1);
        r8.setLength(r0);
        r8.append(r14);
        r0 = "     GPS signal quality (Top 4 Average CN0):";
        r8.append(r0);
        r0 = "poor (less than 20 dBHz): ";
        r1 = "good (greater than 20 dBHz): ";
        r0 = new java.lang.String[]{r0, r1};
        r11 = r0;
        r0 = r11.length;
        r5 = 2;
        r2 = java.lang.Math.min(r5, r0);
        r0 = 0;
    L_0x0ba2:
        if (r0 >= r2) goto L_0x0be3;
    L_0x0ba4:
        r23 = r2;
        r22 = r6;
        r5 = r114;
        r1 = r7.getGpsSignalQualityTime(r0, r5, r13);
        r88 = r12;
        r12 = "\n    ";
        r8.append(r12);
        r8.append(r14);
        r12 = "  ";
        r8.append(r12);
        r12 = r11[r0];
        r8.append(r12);
        r103 = r11;
        r11 = r1 / r16;
        formatTimeMs(r8, r11);
        r8.append(r9);
        r11 = r7.formatRatioLocked(r1, r3);
        r8.append(r11);
        r11 = r22;
        r8.append(r11);
        r0 = r0 + 1;
        r6 = r11;
        r2 = r23;
        r12 = r88;
        r11 = r103;
        r5 = 2;
        goto L_0x0ba2;
    L_0x0be3:
        r23 = r2;
        r103 = r11;
        r88 = r12;
        r11 = r6;
        r5 = r114;
        r0 = r8.toString();
        r10.println(r0);
        r1 = r226.getGpsBatteryDrainMaMs();
        r0 = (r1 > r46 ? 1 : (r1 == r46 ? 0 : -1));
        if (r0 <= 0) goto L_0x0c30;
    L_0x0bfb:
        r228.print(r229);
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "     GPS Battery Drain: ";
        r8.append(r0);
        r0 = new java.text.DecimalFormat;
        r12 = "#.##";
        r0.<init>(r12);
        r115 = r3;
        r3 = (double) r1;
        r129 = 4704985352480227328; // 0x414b774000000000 float:0.0 double:3600000.0;
        r3 = r3 / r129;
        r0 = r0.format(r3);
        r8.append(r0);
        r0 = "mAh";
        r8.append(r0);
        r0 = r8.toString();
        r10.println(r0);
        goto L_0x0c32;
    L_0x0c30:
        r115 = r3;
    L_0x0c32:
        r228.print(r229);
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  CONNECTIVITY POWER SUMMARY END";
        r8.append(r0);
        r0 = r8.toString();
        r10.println(r0);
        r0 = "";
        r10.println(r0);
        r228.print(r229);
        r0 = "  Bluetooth total received: ";
        r10.print(r0);
        r3 = r118;
        r0 = r7.formatBytesLocked(r3);
        r10.print(r0);
        r0 = ", sent: ";
        r10.print(r0);
        r22 = r11;
        r11 = r120;
        r0 = r7.formatBytesLocked(r11);
        r10.println(r0);
        r118 = r7.getBluetoothScanTime(r5, r13);
        r11 = r118 / r16;
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Bluetooth scan time: ";
        r8.append(r0);
        formatTimeMs(r8, r11);
        r0 = r8.toString();
        r10.println(r0);
        r114 = r226.getBluetoothControllerActivity();
        r118 = "Bluetooth";
        r0 = r226;
        r129 = r1;
        r1 = r228;
        r2 = r8;
        r131 = r11;
        r11 = r115;
        r115 = r3;
        r3 = r229;
        r4 = r118;
        r133 = r5;
        r6 = 2;
        r5 = r114;
        r218 = r81;
        r81 = r15;
        r15 = r22;
        r22 = r218;
        r6 = r230;
        r0.printControllerActivity(r1, r2, r3, r4, r5, r6);
        r228.println();
        r228.print(r229);
        r0 = "  Device battery use since last full charge";
        r10.println(r0);
        r228.print(r229);
        r0 = "    Amount discharged (lower bound): ";
        r10.print(r0);
        r0 = r226.getLowDischargeAmountSinceCharge();
        r10.println(r0);
        r228.print(r229);
        r0 = "    Amount discharged (upper bound): ";
        r10.print(r0);
        r0 = r226.getHighDischargeAmountSinceCharge();
        r10.println(r0);
        r228.print(r229);
        r0 = "    Amount discharged while screen on: ";
        r10.print(r0);
        r0 = r226.getDischargeAmountScreenOnSinceCharge();
        r10.println(r0);
        r228.print(r229);
        r0 = "    Amount discharged while screen off: ";
        r10.print(r0);
        r0 = r226.getDischargeAmountScreenOffSinceCharge();
        r10.println(r0);
        r228.print(r229);
        r0 = "    Amount discharged while screen doze: ";
        r10.print(r0);
        r0 = r226.getDischargeAmountScreenDozeSinceCharge();
        r10.println(r0);
        r228.println();
        r0 = new com.android.internal.os.BatteryStatsHelper;
        r6 = r227;
        r5 = r232;
        r1 = 0;
        r0.<init>(r6, r1, r5);
        r4 = r0;
        r4.create(r7);
        r2 = -1;
        r4.refreshStats(r13, r2);
        r0 = r4.getUsageList();
        if (r0 == 0) goto L_0x0f1f;
    L_0x0d24:
        r1 = r0.size();
        if (r1 <= 0) goto L_0x0f1f;
    L_0x0d2a:
        r228.print(r229);
        r1 = "  Estimated power use (mAh):";
        r10.println(r1);
        r228.print(r229);
        r1 = "    Capacity: ";
        r10.print(r1);
        r1 = r4.getPowerProfile();
        r2 = r1.getBatteryCapacity();
        r7.printmAh(r10, r2);
        r1 = ", Computed drain: ";
        r10.print(r1);
        r1 = r4.getComputedPower();
        r7.printmAh(r10, r1);
        r1 = ", actual drain: ";
        r10.print(r1);
        r1 = r4.getMinDrainedPower();
        r7.printmAh(r10, r1);
        r1 = r4.getMinDrainedPower();
        r118 = r4.getMaxDrainedPower();
        r1 = (r1 > r118 ? 1 : (r1 == r118 ? 0 : -1));
        if (r1 == 0) goto L_0x0d75;
    L_0x0d69:
        r1 = "-";
        r10.print(r1);
        r1 = r4.getMaxDrainedPower();
        r7.printmAh(r10, r1);
    L_0x0d75:
        r228.println();
        r1 = 0;
    L_0x0d79:
        r2 = r0.size();
        if (r1 >= r2) goto L_0x0f17;
    L_0x0d7f:
        r2 = r0.get(r1);
        r2 = (com.android.internal.os.BatterySipper) r2;
        r228.print(r229);
        r3 = android.os.BatteryStats.AnonymousClass2.$SwitchMap$com$android$internal$os$BatterySipper$DrainType;
        r118 = r0;
        r0 = r2.drainType;
        r0 = r0.ordinal();
        r0 = r3[r0];
        switch(r0) {
            case 1: goto L_0x0dfe;
            case 2: goto L_0x0df8;
            case 3: goto L_0x0df2;
            case 4: goto L_0x0dec;
            case 5: goto L_0x0de6;
            case 6: goto L_0x0de0;
            case 7: goto L_0x0dda;
            case 8: goto L_0x0dd4;
            case 9: goto L_0x0dc0;
            case 10: goto L_0x0db0;
            case 11: goto L_0x0daa;
            case 12: goto L_0x0da4;
            case 13: goto L_0x0d9e;
            default: goto L_0x0d97;
        };
    L_0x0d97:
        r0 = "    ???: ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0d9e:
        r0 = "    Camera: ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0da4:
        r0 = "    Over-counted: ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0daa:
        r0 = "    Unaccounted: ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0db0:
        r0 = "    User ";
        r10.print(r0);
        r0 = r2.userId;
        r10.print(r0);
        r0 = ": ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0dc0:
        r0 = "    Uid ";
        r10.print(r0);
        r0 = r2.uidObj;
        r0 = r0.getUid();
        android.os.UserHandle.formatUid(r10, r0);
        r0 = ": ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0dd4:
        r0 = "    Flashlight: ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0dda:
        r0 = "    Screen: ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0de0:
        r0 = "    Bluetooth: ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0de6:
        r0 = "    Wifi: ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0dec:
        r0 = "    Phone calls: ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0df2:
        r0 = "    Cell standby: ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0df8:
        r0 = "    Idle: ";
        r10.print(r0);
        goto L_0x0e04;
    L_0x0dfe:
        r0 = "    Ambient display: ";
        r10.print(r0);
    L_0x0e04:
        r5 = r2.totalPowerMah;
        r7.printmAh(r10, r5);
        r5 = r2.usagePowerMah;
        r136 = r11;
        r11 = r2.totalPowerMah;
        r0 = (r5 > r11 ? 1 : (r5 == r11 ? 0 : -1));
        r5 = 0;
        if (r0 == 0) goto L_0x0ebf;
    L_0x0e15:
        r0 = " (";
        r10.print(r0);
        r11 = r2.usagePowerMah;
        r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x0e2a;
    L_0x0e20:
        r0 = " usage=";
        r10.print(r0);
        r11 = r2.usagePowerMah;
        r7.printmAh(r10, r11);
    L_0x0e2a:
        r11 = r2.cpuPowerMah;
        r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x0e3a;
    L_0x0e30:
        r0 = " cpu=";
        r10.print(r0);
        r11 = r2.cpuPowerMah;
        r7.printmAh(r10, r11);
    L_0x0e3a:
        r11 = r2.wakeLockPowerMah;
        r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x0e4a;
    L_0x0e40:
        r0 = " wake=";
        r10.print(r0);
        r11 = r2.wakeLockPowerMah;
        r7.printmAh(r10, r11);
    L_0x0e4a:
        r11 = r2.mobileRadioPowerMah;
        r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x0e5a;
    L_0x0e50:
        r0 = " radio=";
        r10.print(r0);
        r11 = r2.mobileRadioPowerMah;
        r7.printmAh(r10, r11);
    L_0x0e5a:
        r11 = r2.wifiPowerMah;
        r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x0e6a;
    L_0x0e60:
        r0 = " wifi=";
        r10.print(r0);
        r11 = r2.wifiPowerMah;
        r7.printmAh(r10, r11);
    L_0x0e6a:
        r11 = r2.bluetoothPowerMah;
        r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x0e7a;
    L_0x0e70:
        r0 = " bt=";
        r10.print(r0);
        r11 = r2.bluetoothPowerMah;
        r7.printmAh(r10, r11);
    L_0x0e7a:
        r11 = r2.gpsPowerMah;
        r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x0e8a;
    L_0x0e80:
        r0 = " gps=";
        r10.print(r0);
        r11 = r2.gpsPowerMah;
        r7.printmAh(r10, r11);
    L_0x0e8a:
        r11 = r2.sensorPowerMah;
        r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x0e9a;
    L_0x0e90:
        r0 = " sensor=";
        r10.print(r0);
        r11 = r2.sensorPowerMah;
        r7.printmAh(r10, r11);
    L_0x0e9a:
        r11 = r2.cameraPowerMah;
        r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x0eaa;
    L_0x0ea0:
        r0 = " camera=";
        r10.print(r0);
        r11 = r2.cameraPowerMah;
        r7.printmAh(r10, r11);
    L_0x0eaa:
        r11 = r2.flashlightPowerMah;
        r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x0eba;
    L_0x0eb0:
        r0 = " flash=";
        r10.print(r0);
        r11 = r2.flashlightPowerMah;
        r7.printmAh(r10, r11);
    L_0x0eba:
        r0 = " )";
        r10.print(r0);
    L_0x0ebf:
        r11 = r2.totalSmearedPowerMah;
        r5 = r2.totalPowerMah;
        r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x0eff;
    L_0x0ec7:
        r0 = " Including smearing: ";
        r10.print(r0);
        r5 = r2.totalSmearedPowerMah;
        r7.printmAh(r10, r5);
        r0 = " (";
        r10.print(r0);
        r5 = r2.screenPowerMah;
        r11 = 0;
        r0 = (r5 > r11 ? 1 : (r5 == r11 ? 0 : -1));
        if (r0 == 0) goto L_0x0ee8;
    L_0x0ede:
        r0 = " screen=";
        r10.print(r0);
        r5 = r2.screenPowerMah;
        r7.printmAh(r10, r5);
    L_0x0ee8:
        r5 = r2.proportionalSmearMah;
        r11 = 0;
        r0 = (r5 > r11 ? 1 : (r5 == r11 ? 0 : -1));
        if (r0 == 0) goto L_0x0efa;
    L_0x0ef0:
        r0 = " proportional=";
        r10.print(r0);
        r5 = r2.proportionalSmearMah;
        r7.printmAh(r10, r5);
    L_0x0efa:
        r0 = " )";
        r10.print(r0);
    L_0x0eff:
        r0 = r2.shouldHide;
        if (r0 == 0) goto L_0x0f08;
    L_0x0f03:
        r0 = " Excluded from smearing";
        r10.print(r0);
    L_0x0f08:
        r228.println();
        r1 = r1 + 1;
        r6 = r227;
        r5 = r232;
        r0 = r118;
        r11 = r136;
        goto L_0x0d79;
    L_0x0f17:
        r118 = r0;
        r136 = r11;
        r228.println();
        goto L_0x0f23;
    L_0x0f1f:
        r118 = r0;
        r136 = r11;
    L_0x0f23:
        r11 = r4.getMobilemsppList();
        if (r11 == 0) goto L_0x0fcc;
    L_0x0f29:
        r0 = r11.size();
        if (r0 <= 0) goto L_0x0fcc;
    L_0x0f2f:
        r228.print(r229);
        r0 = "  Per-app mobile ms per packet:";
        r10.println(r0);
        r0 = 0;
        r2 = 0;
    L_0x0f3a:
        r3 = r11.size();
        if (r2 >= r3) goto L_0x0f9f;
    L_0x0f40:
        r3 = r11.get(r2);
        r3 = (com.android.internal.os.BatterySipper) r3;
        r5 = 0;
        r8.setLength(r5);
        r8.append(r14);
        r5 = "    Uid ";
        r8.append(r5);
        r5 = r3.uidObj;
        r5 = r5.getUid();
        android.os.UserHandle.formatUid(r8, r5);
        r5 = ": ";
        r8.append(r5);
        r5 = r3.mobilemspp;
        r5 = com.android.internal.os.BatteryStatsHelper.makemAh(r5);
        r8.append(r5);
        r5 = " (";
        r8.append(r5);
        r5 = r3.mobileRxPackets;
        r118 = r11;
        r11 = r3.mobileTxPackets;
        r5 = r5 + r11;
        r8.append(r5);
        r5 = " packets over ";
        r8.append(r5);
        r5 = r3.mobileActive;
        formatTimeMsNoSpace(r8, r5);
        r8.append(r15);
        r5 = r3.mobileActiveCount;
        r8.append(r5);
        r5 = "x";
        r8.append(r5);
        r5 = r8.toString();
        r10.println(r5);
        r5 = r3.mobileActive;
        r0 = r0 + r5;
        r2 = r2 + 1;
        r11 = r118;
        goto L_0x0f3a;
    L_0x0f9f:
        r118 = r11;
        r2 = 0;
        r8.setLength(r2);
        r8.append(r14);
        r2 = "    TOTAL TIME: ";
        r8.append(r2);
        formatTimeMs(r8, r0);
        r8.append(r9);
        r11 = r136;
        r2 = r7.formatRatioLocked(r0, r11);
        r8.append(r2);
        r6 = r127;
        r8.append(r6);
        r2 = r8.toString();
        r10.println(r2);
        r228.println();
        goto L_0x0fd2;
    L_0x0fcc:
        r118 = r11;
        r6 = r127;
        r11 = r136;
    L_0x0fd2:
        r0 = new android.os.BatteryStats$1;
        r0.<init>();
        r5 = r0;
        if (r231 >= 0) goto L_0x1208;
        r119 = r226.getKernelWakelockStats();
        r0 = r119.size();
        if (r0 <= 0) goto L_0x10cd;
    L_0x0fe5:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r2 = r0;
        r0 = r119.entrySet();
        r0 = r0.iterator();
    L_0x0ff3:
        r1 = r0.hasNext();
        if (r1 == 0) goto L_0x1039;
    L_0x0ff9:
        r1 = r0.next();
        r1 = (java.util.Map.Entry) r1;
        r3 = r1.getValue();
        r3 = (android.os.BatteryStats.Timer) r3;
        r124 = r4;
        r127 = r5;
        r4 = r133;
        r133 = computeWakeLock(r3, r4, r13);
        r136 = (r133 > r46 ? 1 : (r133 == r46 ? 0 : -1));
        if (r136 <= 0) goto L_0x102e;
    L_0x1013:
        r142 = r0;
        r0 = new android.os.BatteryStats$TimerEntry;
        r136 = r1.getKey();
        r137 = r136;
        r137 = (java.lang.String) r137;
        r138 = 0;
        r136 = r0;
        r139 = r3;
        r140 = r133;
        r136.<init>(r137, r138, r139, r140);
        r2.add(r0);
        goto L_0x1030;
    L_0x102e:
        r142 = r0;
    L_0x1030:
        r133 = r4;
        r4 = r124;
        r5 = r127;
        r0 = r142;
        goto L_0x0ff3;
    L_0x1039:
        r124 = r4;
        r127 = r5;
        r4 = r133;
        r0 = r2.size();
        if (r0 <= 0) goto L_0x10c2;
    L_0x1045:
        r3 = r127;
        java.util.Collections.sort(r2, r3);
        r228.print(r229);
        r0 = "  All kernel wake locks:";
        r10.println(r0);
        r0 = 0;
        r1 = r0;
    L_0x1054:
        r0 = r2.size();
        if (r1 >= r0) goto L_0x10b2;
    L_0x105a:
        r0 = r2.get(r1);
        r0 = (android.os.BatteryStats.TimerEntry) r0;
        r127 = ": ";
        r133 = r1;
        r1 = 0;
        r8.setLength(r1);
        r8.append(r14);
        r1 = "  Kernel Wake lock ";
        r8.append(r1);
        r1 = r0.mName;
        r8.append(r1);
        r1 = r0.mTimer;
        r134 = 0;
        r136 = r0;
        r0 = r8;
        r137 = r2;
        r114 = r3;
        r138 = -1;
        r2 = r4;
        r139 = r4;
        r4 = r134;
        r143 = r114;
        r5 = r230;
        r114 = r6;
        r6 = r127;
        r0 = printWakeLock(r0, r1, r2, r4, r5, r6);
        r1 = ": ";
        r1 = r0.equals(r1);
        if (r1 != 0) goto L_0x10a7;
    L_0x109b:
        r1 = " realtime";
        r8.append(r1);
        r1 = r8.toString();
        r10.println(r1);
    L_0x10a7:
        r1 = r133 + 1;
        r6 = r114;
        r2 = r137;
        r4 = r139;
        r3 = r143;
        goto L_0x1054;
    L_0x10b2:
        r133 = r1;
        r137 = r2;
        r143 = r3;
        r139 = r4;
        r114 = r6;
        r138 = -1;
        r228.println();
        goto L_0x10d7;
    L_0x10c2:
        r137 = r2;
        r139 = r4;
        r114 = r6;
        r143 = r127;
        r138 = -1;
        goto L_0x10d7;
    L_0x10cd:
        r124 = r4;
        r143 = r5;
        r114 = r6;
        r139 = r133;
        r138 = -1;
    L_0x10d7:
        r0 = r38.size();
        if (r0 <= 0) goto L_0x1153;
    L_0x10dd:
        r6 = r38;
        r5 = r143;
        java.util.Collections.sort(r6, r5);
        r228.print(r229);
        r0 = "  All partial wake locks:";
        r10.println(r0);
        r0 = 0;
        r4 = r0;
    L_0x10ee:
        r0 = r6.size();
        if (r4 >= r0) goto L_0x1144;
    L_0x10f4:
        r0 = r6.get(r4);
        r2 = r0;
        r2 = (android.os.BatteryStats.TimerEntry) r2;
        r0 = 0;
        r8.setLength(r0);
        r0 = "  Wake lock ";
        r8.append(r0);
        r0 = r2.mId;
        android.os.UserHandle.formatUid(r8, r0);
        r3 = r81;
        r8.append(r3);
        r0 = r2.mName;
        r8.append(r0);
        r1 = r2.mTimer;
        r38 = 0;
        r81 = ": ";
        r0 = r8;
        r127 = r2;
        r133 = r3;
        r2 = r139;
        r134 = r4;
        r4 = r38;
        r144 = r5;
        r5 = r230;
        r38 = r6;
        r6 = r81;
        printWakeLock(r0, r1, r2, r4, r5, r6);
        r0 = " realtime";
        r8.append(r0);
        r0 = r8.toString();
        r10.println(r0);
        r4 = r134 + 1;
        r6 = r38;
        r81 = r133;
        r5 = r144;
        goto L_0x10ee;
    L_0x1144:
        r134 = r4;
        r144 = r5;
        r38 = r6;
        r133 = r81;
        r38.clear();
        r228.println();
        goto L_0x1157;
    L_0x1153:
        r133 = r81;
        r144 = r143;
    L_0x1157:
        r81 = r226.getWakeupReasonStats();
        r0 = r81.size();
        if (r0 <= 0) goto L_0x1205;
    L_0x1161:
        r228.print(r229);
        r0 = "  All wakeup reasons:";
        r10.println(r0);
        r0 = new java.util.ArrayList;
        r0.<init>();
        r6 = r0;
        r0 = r81.entrySet();
        r0 = r0.iterator();
    L_0x1177:
        r1 = r0.hasNext();
        if (r1 == 0) goto L_0x11a7;
    L_0x117d:
        r1 = r0.next();
        r1 = (java.util.Map.Entry) r1;
        r2 = r1.getValue();
        r2 = (android.os.BatteryStats.Timer) r2;
        r3 = new android.os.BatteryStats$TimerEntry;
        r4 = r1.getKey();
        r146 = r4;
        r146 = (java.lang.String) r146;
        r147 = 0;
        r4 = r2.getCountLocked(r13);
        r4 = (long) r4;
        r145 = r3;
        r148 = r2;
        r149 = r4;
        r145.<init>(r146, r147, r148, r149);
        r6.add(r3);
        goto L_0x1177;
    L_0x11a7:
        r5 = r144;
        java.util.Collections.sort(r6, r5);
        r0 = 0;
        r4 = r0;
    L_0x11ae:
        r0 = r6.size();
        if (r4 >= r0) goto L_0x11fb;
    L_0x11b4:
        r0 = r6.get(r4);
        r2 = r0;
        r2 = (android.os.BatteryStats.TimerEntry) r2;
        r127 = ": ";
        r0 = 0;
        r8.setLength(r0);
        r8.append(r14);
        r0 = "  Wakeup reason ";
        r8.append(r0);
        r0 = r2.mName;
        r8.append(r0);
        r1 = r2.mTimer;
        r134 = 0;
        r136 = ": ";
        r0 = r8;
        r137 = r2;
        r2 = r139;
        r141 = r4;
        r4 = r134;
        r134 = r5;
        r5 = r230;
        r142 = r6;
        r6 = r136;
        printWakeLock(r0, r1, r2, r4, r5, r6);
        r0 = " realtime";
        r8.append(r0);
        r0 = r8.toString();
        r10.println(r0);
        r4 = r141 + 1;
        r5 = r134;
        r6 = r142;
        goto L_0x11ae;
    L_0x11fb:
        r141 = r4;
        r134 = r5;
        r142 = r6;
        r228.println();
        goto L_0x1214;
    L_0x1205:
        r134 = r144;
        goto L_0x1214;
    L_0x1208:
        r124 = r4;
        r114 = r6;
        r139 = r133;
        r138 = -1;
        r134 = r5;
        r133 = r81;
    L_0x1214:
        r6 = r226.getKernelMemoryStats();
        r0 = r6.size();
        if (r0 <= 0) goto L_0x125e;
    L_0x121e:
        r0 = "  Memory Stats";
        r10.println(r0);
        r0 = 0;
    L_0x1224:
        r1 = r6.size();
        if (r0 >= r1) goto L_0x1258;
    L_0x122a:
        r1 = 0;
        r8.setLength(r1);
        r2 = "  Bandwidth ";
        r8.append(r2);
        r2 = r6.keyAt(r0);
        r8.append(r2);
        r2 = " Time ";
        r8.append(r2);
        r2 = r6.valueAt(r0);
        r2 = (android.os.BatteryStats.Timer) r2;
        r3 = r139;
        r1 = r2.getTotalTimeLocked(r3, r13);
        r8.append(r1);
        r1 = r8.toString();
        r10.println(r1);
        r0 = r0 + 1;
        goto L_0x1224;
    L_0x1258:
        r3 = r139;
        r228.println();
        goto L_0x1260;
    L_0x125e:
        r3 = r139;
    L_0x1260:
        r71 = r226.getRpmStats();
        r0 = r71.size();
        if (r0 <= 0) goto L_0x138d;
    L_0x126a:
        r228.print(r229);
        r0 = "  Resource Power Manager Stats";
        r10.println(r0);
        r0 = r71.size();
        if (r0 <= 0) goto L_0x1354;
    L_0x1278:
        r0 = r71.entrySet();
        r0 = r0.iterator();
    L_0x1280:
        r1 = r0.hasNext();
        if (r1 == 0) goto L_0x131e;
    L_0x1286:
        r1 = r0.next();
        r1 = (java.util.Map.Entry) r1;
        r2 = r1.getKey();
        r2 = (java.lang.String) r2;
        r81 = r1.getValue();
        r81 = (android.os.BatteryStats.Timer) r81;
        r119 = r8;
        r8 = r228;
        r4 = r3;
        r127 = r114;
        r3 = 0;
        r114 = r6;
        r6 = r9;
        r218 = r74;
        r74 = r101;
        r101 = r99;
        r99 = r72;
        r72 = r218;
        r220 = r53;
        r53 = r61;
        r61 = r220;
        r9 = r119;
        r151 = r15;
        r15 = r52;
        r10 = r81;
        r152 = r11;
        r52 = r92;
        r154 = r133;
        r92 = r1;
        r218 = r88;
        r88 = r0;
        r0 = r104;
        r219 = r106;
        r107 = r218;
        r105 = r112;
        r112 = r120;
        r120 = r131;
        r131 = r118;
        r118 = r103;
        r103 = r219;
        r11 = r4;
        r13 = r230;
        r14 = r229;
        r155 = r15;
        r15 = r2;
        printTimer(r8, r9, r10, r11, r13, r14, r15);
        r10 = r228;
        r3 = r4;
        r9 = r6;
        r92 = r52;
        r6 = r114;
        r8 = r119;
        r114 = r127;
        r15 = r151;
        r11 = r152;
        r52 = r155;
        r218 = r0;
        r0 = r88;
        r88 = r107;
        r220 = r103;
        r103 = r118;
        r118 = r131;
        r131 = r120;
        r120 = r112;
        r112 = r105;
        r104 = r218;
        r106 = r220;
        r222 = r99;
        r99 = r101;
        r101 = r74;
        r74 = r72;
        r72 = r222;
        r224 = r53;
        r53 = r61;
        r61 = r224;
        goto L_0x1280;
    L_0x131e:
        r4 = r3;
        r119 = r8;
        r152 = r11;
        r151 = r15;
        r155 = r52;
        r52 = r92;
        r0 = r104;
        r127 = r114;
        r154 = r133;
        r3 = 0;
        r114 = r6;
        r6 = r9;
        r218 = r106;
        r107 = r88;
        r105 = r112;
        r112 = r120;
        r120 = r131;
        r131 = r118;
        r118 = r103;
        r103 = r218;
        r220 = r74;
        r74 = r101;
        r101 = r99;
        r99 = r72;
        r72 = r220;
        r222 = r53;
        r53 = r61;
        r61 = r222;
        goto L_0x1389;
    L_0x1354:
        r4 = r3;
        r119 = r8;
        r152 = r11;
        r151 = r15;
        r155 = r52;
        r52 = r92;
        r0 = r104;
        r127 = r114;
        r154 = r133;
        r3 = 0;
        r114 = r6;
        r6 = r9;
        r218 = r106;
        r107 = r88;
        r105 = r112;
        r112 = r120;
        r120 = r131;
        r131 = r118;
        r118 = r103;
        r103 = r218;
        r220 = r74;
        r74 = r101;
        r101 = r99;
        r99 = r72;
        r72 = r220;
        r222 = r53;
        r53 = r61;
        r61 = r222;
    L_0x1389:
        r228.println();
        goto L_0x13c2;
    L_0x138d:
        r4 = r3;
        r119 = r8;
        r152 = r11;
        r151 = r15;
        r155 = r52;
        r52 = r92;
        r0 = r104;
        r127 = r114;
        r154 = r133;
        r3 = 0;
        r114 = r6;
        r6 = r9;
        r218 = r106;
        r107 = r88;
        r105 = r112;
        r112 = r120;
        r120 = r131;
        r131 = r118;
        r118 = r103;
        r103 = r218;
        r220 = r74;
        r74 = r101;
        r101 = r99;
        r99 = r72;
        r72 = r220;
        r222 = r53;
        r53 = r61;
        r61 = r222;
    L_0x13c2:
        r15 = r226.getCpuFreqs();
        if (r15 == 0) goto L_0x13fe;
    L_0x13c8:
        r14 = r119;
        r14.setLength(r3);
        r2 = "  CPU freqs:";
        r14.append(r2);
        r2 = 0;
    L_0x13d3:
        r8 = r15.length;
        if (r2 >= r8) goto L_0x13ef;
    L_0x13d6:
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r13 = r154;
        r8.append(r13);
        r9 = r15[r2];
        r8.append(r9);
        r8 = r8.toString();
        r14.append(r8);
        r2 = r2 + 1;
        goto L_0x13d3;
    L_0x13ef:
        r13 = r154;
        r2 = r14.toString();
        r11 = r228;
        r11.println(r2);
        r228.println();
        goto L_0x1404;
    L_0x13fe:
        r11 = r228;
        r14 = r119;
        r13 = r154;
    L_0x1404:
        r2 = 0;
        r12 = r2;
    L_0x1406:
        r10 = r155;
        if (r12 >= r10) goto L_0x258c;
    L_0x140a:
        r9 = r117;
        r8 = r9.keyAt(r12);
        r2 = r231;
        if (r2 < 0) goto L_0x143c;
    L_0x1414:
        if (r8 == r2) goto L_0x143c;
    L_0x1416:
        r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        if (r8 == r3) goto L_0x143c;
    L_0x141a:
        r136 = r0;
        r198 = r4;
        r197 = r6;
        r117 = r9;
        r146 = r10;
        r4 = r11;
        r133 = r12;
        r209 = r13;
        r1 = r14;
        r145 = r15;
        r13 = r122;
        r203 = r125;
        r154 = r127;
        r168 = r151;
        r92 = 1;
        r12 = r229;
        r10 = r230;
        goto L_0x256e;
    L_0x143c:
        r3 = r9.valueAt(r12);
        r3 = (android.os.BatteryStats.Uid) r3;
        r228.print(r229);
        r2 = "  ";
        r11.print(r2);
        android.os.UserHandle.formatUid(r11, r8);
        r2 = ":";
        r11.println(r2);
        r88 = 0;
        r2 = r230;
        r92 = r8;
        r117 = r9;
        r155 = r10;
        r8 = 0;
        r9 = r3.getNetworkActivityBytes(r8, r2);
        r119 = r12;
        r154 = r13;
        r8 = 1;
        r12 = r3.getNetworkActivityBytes(r8, r2);
        r132 = r0;
        r8 = 2;
        r0 = r3.getNetworkActivityBytes(r8, r2);
        r8 = 3;
        r136 = r0;
        r0 = r3.getNetworkActivityBytes(r8, r2);
        r8 = 4;
        r139 = r0;
        r0 = r3.getNetworkActivityBytes(r8, r2);
        r8 = 5;
        r141 = r0;
        r0 = r3.getNetworkActivityBytes(r8, r2);
        r143 = r0;
        r8 = 0;
        r0 = r3.getNetworkActivityPackets(r8, r2);
        r146 = r14;
        r145 = r15;
        r8 = 1;
        r14 = r3.getNetworkActivityPackets(r8, r2);
        r147 = r6;
        r8 = 2;
        r6 = r3.getNetworkActivityPackets(r8, r2);
        r8 = 3;
        r148 = r6;
        r7 = r3.getNetworkActivityPackets(r8, r2);
        r156 = r7;
        r7 = r3.getMobileRadioActiveTime(r2);
        r6 = r3.getMobileRadioActiveCount(r2);
        r158 = r7;
        r8 = r6;
        r6 = r3.getFullWifiLockTime(r4, r2);
        r160 = r6;
        r6 = r3.getWifiScanTime(r4, r2);
        r162 = r6;
        r7 = r3.getWifiScanCount(r2);
        r6 = r3.getWifiScanBackgroundCount(r2);
        r164 = r6;
        r150 = r7;
        r6 = r3.getWifiScanActualTime(r4);
        r165 = r6;
        r6 = r3.getWifiScanBackgroundTime(r4);
        r167 = r6;
        r6 = r3.getWifiRunningTime(r4, r2);
        r169 = r6;
        r6 = r3.getMobileRadioApWakeupCount(r2);
        r171 = r4;
        r4 = r3.getWifiRadioApWakeupCount(r2);
        r173 = (r9 > r46 ? 1 : (r9 == r46 ? 0 : -1));
        if (r173 > 0) goto L_0x14fd;
    L_0x14e9:
        r173 = (r12 > r46 ? 1 : (r12 == r46 ? 0 : -1));
        if (r173 > 0) goto L_0x14fd;
    L_0x14ed:
        r173 = (r0 > r46 ? 1 : (r0 == r46 ? 0 : -1));
        if (r173 > 0) goto L_0x14fd;
    L_0x14f1:
        r173 = (r14 > r46 ? 1 : (r14 == r46 ? 0 : -1));
        if (r173 <= 0) goto L_0x14f6;
    L_0x14f5:
        goto L_0x14fd;
    L_0x14f6:
        r2 = r226;
        r174 = r148;
        r148 = r4;
        goto L_0x1533;
    L_0x14fd:
        r228.print(r229);
        r2 = "    Mobile network: ";
        r11.print(r2);
        r2 = r226;
        r174 = r148;
        r148 = r4;
        r4 = r2.formatBytesLocked(r9);
        r11.print(r4);
        r4 = " received, ";
        r11.print(r4);
        r4 = r2.formatBytesLocked(r12);
        r11.print(r4);
        r4 = " sent (packets ";
        r11.print(r4);
        r11.print(r0);
        r4 = " received, ";
        r11.print(r4);
        r11.print(r14);
        r4 = " sent)";
        r11.println(r4);
    L_0x1533:
        r4 = (r158 > r46 ? 1 : (r158 == r46 ? 0 : -1));
        if (r4 > 0) goto L_0x154e;
    L_0x1537:
        if (r8 <= 0) goto L_0x153a;
    L_0x1539:
        goto L_0x154e;
    L_0x153a:
        r4 = r229;
        r178 = r0;
        r176 = r9;
        r180 = r132;
        r5 = r146;
        r146 = r3;
        r132 = r12;
        r12 = r158;
        r158 = r8;
        goto L_0x15bb;
    L_0x154e:
        r5 = r146;
        r4 = 0;
        r5.setLength(r4);
        r4 = r229;
        r5.append(r4);
        r176 = r9;
        r9 = "    Mobile radio active: ";
        r5.append(r9);
        r9 = r158 / r16;
        formatTimeMs(r5, r9);
        r9 = r147;
        r5.append(r9);
        r146 = r3;
        r9 = r132;
        r132 = r12;
        r12 = r158;
        r3 = r2.formatRatioLocked(r12, r9);
        r5.append(r3);
        r3 = r151;
        r5.append(r3);
        r5.append(r8);
        r2 = "x";
        r5.append(r2);
        r158 = r0 + r14;
        r2 = (r158 > r46 ? 1 : (r158 == r46 ? 0 : -1));
        if (r2 != 0) goto L_0x1594;
    L_0x158d:
        r158 = 1;
        r178 = r0;
        r0 = r158;
        goto L_0x1598;
    L_0x1594:
        r178 = r0;
        r0 = r158;
    L_0x1598:
        r2 = " @ ";
        r5.append(r2);
        r151 = r3;
        r2 = r12 / r16;
        r2 = (double) r2;
        r158 = r8;
        r180 = r9;
        r8 = (double) r0;
        r2 = r2 / r8;
        r2 = com.android.internal.os.BatteryStatsHelper.makemAh(r2);
        r5.append(r2);
        r2 = " mspp";
        r5.append(r2);
        r2 = r5.toString();
        r11.println(r2);
    L_0x15bb:
        r0 = (r6 > r46 ? 1 : (r6 == r46 ? 0 : -1));
        if (r0 <= 0) goto L_0x15d6;
    L_0x15bf:
        r3 = 0;
        r5.setLength(r3);
        r5.append(r4);
        r0 = "    Mobile radio AP wakeups: ";
        r5.append(r0);
        r5.append(r6);
        r0 = r5.toString();
        r11.println(r0);
        goto L_0x15d7;
    L_0x15d6:
        r3 = 0;
    L_0x15d7:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r0.append(r4);
        r1 = "  ";
        r0.append(r1);
        r8 = r0.toString();
        r9 = r146.getModemControllerActivity();
        r10 = "Cellular";
        r1 = r136;
        r182 = r143;
        r143 = r178;
        r136 = r180;
        r218 = r12;
        r12 = r141;
        r141 = r218;
        r220 = r14;
        r14 = r139;
        r139 = r220;
        r0 = r226;
        r178 = r12;
        r12 = r1;
        r1 = r228;
        r180 = r6;
        r7 = r226;
        r6 = r230;
        r2 = r5;
        r184 = r127;
        r81 = r146;
        r185 = r151;
        r3 = r8;
        r8 = r4;
        r189 = r148;
        r187 = r171;
        r4 = r10;
        r10 = r5;
        r5 = r9;
        r146 = r10;
        r199 = r147;
        r127 = r158;
        r8 = r160;
        r191 = r162;
        r10 = r164;
        r193 = r165;
        r195 = r167;
        r197 = r169;
        r147 = r180;
        r0.printControllerActivityIfInteresting(r1, r2, r3, r4, r5, r6);
        r0 = (r12 > r46 ? 1 : (r12 == r46 ? 0 : -1));
        if (r0 > 0) goto L_0x164c;
    L_0x163a:
        r0 = (r14 > r46 ? 1 : (r14 == r46 ? 0 : -1));
        if (r0 > 0) goto L_0x164c;
    L_0x163e:
        r5 = r174;
        r0 = (r5 > r46 ? 1 : (r5 == r46 ? 0 : -1));
        if (r0 > 0) goto L_0x164e;
    L_0x1644:
        r0 = (r156 > r46 ? 1 : (r156 == r46 ? 0 : -1));
        if (r0 <= 0) goto L_0x1649;
    L_0x1648:
        goto L_0x164e;
    L_0x1649:
        r3 = r156;
        goto L_0x1680;
    L_0x164c:
        r5 = r174;
    L_0x164e:
        r228.print(r229);
        r0 = "    Wi-Fi network: ";
        r11.print(r0);
        r0 = r7.formatBytesLocked(r12);
        r11.print(r0);
        r0 = " received, ";
        r11.print(r0);
        r0 = r7.formatBytesLocked(r14);
        r11.print(r0);
        r0 = " sent (packets ";
        r11.print(r0);
        r11.print(r5);
        r0 = " received, ";
        r11.print(r0);
        r3 = r156;
        r11.print(r3);
        r0 = " sent)";
        r11.println(r0);
    L_0x1680:
        r0 = (r8 > r46 ? 1 : (r8 == r46 ? 0 : -1));
        if (r0 != 0) goto L_0x16dc;
    L_0x1684:
        r1 = r191;
        r0 = (r1 > r46 ? 1 : (r1 == r46 ? 0 : -1));
        if (r0 != 0) goto L_0x16cf;
    L_0x168a:
        if (r150 != 0) goto L_0x16cf;
    L_0x168c:
        if (r10 != 0) goto L_0x16cf;
    L_0x168e:
        r156 = r14;
        r14 = r193;
        r0 = (r14 > r46 ? 1 : (r14 == r46 ? 0 : -1));
        if (r0 != 0) goto L_0x16c6;
    L_0x1696:
        r158 = r12;
        r12 = r195;
        r0 = (r12 > r46 ? 1 : (r12 == r46 ? 0 : -1));
        if (r0 != 0) goto L_0x16c1;
    L_0x169e:
        r160 = r8;
        r8 = r197;
        r0 = (r8 > r46 ? 1 : (r8 == r46 ? 0 : -1));
        if (r0 == 0) goto L_0x16a7;
    L_0x16a6:
        goto L_0x16ea;
    L_0x16a7:
        r191 = r1;
        r174 = r5;
        r197 = r8;
        r9 = r10;
        r8 = r11;
        r0 = r146;
        r10 = r150;
        r200 = r160;
        r11 = r185;
        r1 = r187;
        r6 = r199;
        r5 = r229;
        r160 = r3;
        goto L_0x17c0;
    L_0x16c1:
        r160 = r8;
        r8 = r197;
        goto L_0x16ea;
    L_0x16c6:
        r160 = r8;
        r158 = r12;
        r12 = r195;
        r8 = r197;
        goto L_0x16ea;
    L_0x16cf:
        r160 = r8;
        r158 = r12;
        r156 = r14;
        r14 = r193;
        r12 = r195;
        r8 = r197;
        goto L_0x16ea;
    L_0x16dc:
        r160 = r8;
        r158 = r12;
        r156 = r14;
        r1 = r191;
        r14 = r193;
        r12 = r195;
        r8 = r197;
    L_0x16ea:
        r174 = r5;
        r0 = r146;
        r6 = 0;
        r0.setLength(r6);
        r5 = r229;
        r200 = r160;
        r0.append(r5);
        r6 = "    Wifi Running: ";
        r0.append(r6);
        r160 = r3;
        r3 = r8 / r16;
        formatTimeMs(r0, r3);
        r6 = r199;
        r0.append(r6);
        r3 = r152;
        r11 = r7.formatRatioLocked(r8, r3);
        r0.append(r11);
        r11 = ")\n";
        r0.append(r11);
        r0.append(r5);
        r11 = "    Full Wifi Lock: ";
        r0.append(r11);
        r197 = r8;
        r164 = r10;
        r8 = r200;
        r10 = r8 / r16;
        formatTimeMs(r0, r10);
        r0.append(r6);
        r10 = r7.formatRatioLocked(r8, r3);
        r0.append(r10);
        r10 = ")\n";
        r0.append(r10);
        r0.append(r5);
        r10 = "    Wifi Scan (blamed): ";
        r0.append(r10);
        r10 = r1 / r16;
        formatTimeMs(r0, r10);
        r0.append(r6);
        r10 = r7.formatRatioLocked(r1, r3);
        r0.append(r10);
        r11 = r185;
        r0.append(r11);
        r10 = r150;
        r0.append(r10);
        r191 = r1;
        r1 = "x\n";
        r0.append(r1);
        r0.append(r5);
        r1 = "    Wifi Scan (actual): ";
        r0.append(r1);
        r1 = r14 / r16;
        formatTimeMs(r0, r1);
        r0.append(r6);
        r1 = r187;
        r3 = 0;
        r8 = r7.computeBatteryRealtime(r1, r3);
        r3 = r7.formatRatioLocked(r14, r8);
        r0.append(r3);
        r0.append(r11);
        r0.append(r10);
        r3 = "x\n";
        r0.append(r3);
        r0.append(r5);
        r3 = "    Background Wifi Scan: ";
        r0.append(r3);
        r3 = r12 / r16;
        formatTimeMs(r0, r3);
        r0.append(r6);
        r3 = 0;
        r8 = r7.computeBatteryRealtime(r1, r3);
        r3 = r7.formatRatioLocked(r12, r8);
        r0.append(r3);
        r0.append(r11);
        r9 = r164;
        r0.append(r9);
        r3 = "x";
        r0.append(r3);
        r3 = r0.toString();
        r8 = r228;
        r8.println(r3);
    L_0x17c0:
        r3 = r189;
        r146 = (r3 > r46 ? 1 : (r3 == r46 ? 0 : -1));
        if (r146 <= 0) goto L_0x17df;
    L_0x17c6:
        r199 = r6;
        r6 = 0;
        r0.setLength(r6);
        r0.append(r5);
        r6 = "    WiFi AP wakeups: ";
        r0.append(r6);
        r0.append(r3);
        r6 = r0.toString();
        r8.println(r6);
        goto L_0x17e1;
    L_0x17df:
        r199 = r6;
    L_0x17e1:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r6.append(r5);
        r146 = r0;
        r0 = "  ";
        r6.append(r0);
        r6 = r6.toString();
        r149 = r81.getWifiControllerActivity();
        r150 = "WiFi";
        r151 = r146;
        r0 = r226;
        r193 = r14;
        r162 = r191;
        r14 = r1;
        r1 = r228;
        r2 = r151;
        r164 = r3;
        r3 = r6;
        r4 = r150;
        r6 = r5;
        r166 = r174;
        r5 = r149;
        r146 = r9;
        r195 = r12;
        r13 = r199;
        r12 = 0;
        r9 = r6;
        r6 = r230;
        r0.printControllerActivityIfInteresting(r1, r2, r3, r4, r5, r6);
        r0 = (r178 > r46 ? 1 : (r178 == r46 ? 0 : -1));
        if (r0 > 0) goto L_0x182c;
    L_0x1822:
        r5 = r182;
        r0 = (r5 > r46 ? 1 : (r5 == r46 ? 0 : -1));
        if (r0 <= 0) goto L_0x1829;
    L_0x1828:
        goto L_0x182e;
    L_0x1829:
        r2 = r178;
        goto L_0x1850;
    L_0x182c:
        r5 = r182;
    L_0x182e:
        r228.print(r229);
        r0 = "    Bluetooth network: ";
        r8.print(r0);
        r2 = r178;
        r0 = r7.formatBytesLocked(r2);
        r8.print(r0);
        r0 = " received, ";
        r8.print(r0);
        r0 = r7.formatBytesLocked(r5);
        r8.print(r0);
        r0 = " sent";
        r8.println(r0);
    L_0x1850:
        r4 = r81.getBluetoothScanTimer();
        r1 = " times)";
        if (r4 == 0) goto L_0x1a49;
    L_0x1858:
        r0 = r230;
        r149 = r4.getTotalTimeLocked(r14, r0);
        r149 = r149 + r18;
        r199 = r13;
        r12 = r149 / r16;
        r149 = (r12 > r46 ? 1 : (r12 == r46 ? 0 : -1));
        if (r149 == 0) goto L_0x1a37;
    L_0x1868:
        r178 = r2;
        r2 = r4.getCountLocked(r0);
        r3 = r81.getBluetoothScanBackgroundTimer();
        if (r3 == 0) goto L_0x1879;
    L_0x1874:
        r149 = r3.getCountLocked(r0);
        goto L_0x187b;
    L_0x1879:
        r149 = 0;
    L_0x187b:
        r150 = r149;
        r182 = r5;
        r149 = r10;
        r185 = r11;
        r10 = r125;
        r5 = r4.getTotalDurationMsLocked(r10);
        if (r3 == 0) goto L_0x1890;
    L_0x188b:
        r125 = r3.getTotalDurationMsLocked(r10);
        goto L_0x1892;
    L_0x1890:
        r125 = r46;
    L_0x1892:
        r168 = r125;
        r125 = r81.getBluetoothScanResultCounter();
        if (r125 == 0) goto L_0x18a3;
    L_0x189a:
        r7 = r81.getBluetoothScanResultCounter();
        r7 = r7.getCountLocked(r0);
        goto L_0x18a4;
    L_0x18a3:
        r7 = 0;
    L_0x18a4:
        r125 = r81.getBluetoothScanResultBgCounter();
        if (r125 == 0) goto L_0x18b5;
    L_0x18aa:
        r187 = r14;
        r14 = r81.getBluetoothScanResultBgCounter();
        r14 = r14.getCountLocked(r0);
        goto L_0x18b8;
    L_0x18b5:
        r187 = r14;
        r14 = 0;
    L_0x18b8:
        r15 = r81.getBluetoothUnoptimizedScanTimer();
        if (r15 == 0) goto L_0x18c3;
    L_0x18be:
        r125 = r15.getTotalDurationMsLocked(r10);
        goto L_0x18c5;
    L_0x18c3:
        r125 = r46;
    L_0x18c5:
        r170 = r125;
        if (r15 == 0) goto L_0x18ce;
    L_0x18c9:
        r125 = r15.getMaxDurationMsLocked(r10);
        goto L_0x18d0;
    L_0x18ce:
        r125 = r46;
    L_0x18d0:
        r172 = r125;
        r0 = r81.getBluetoothUnoptimizedScanBackgroundTimer();
        if (r0 == 0) goto L_0x18de;
    L_0x18d9:
        r125 = r0.getTotalDurationMsLocked(r10);
        goto L_0x18e0;
    L_0x18de:
        r125 = r46;
    L_0x18e0:
        r174 = r125;
        if (r0 == 0) goto L_0x18e9;
    L_0x18e4:
        r125 = r0.getMaxDurationMsLocked(r10);
        goto L_0x18eb;
    L_0x18e9:
        r125 = r46;
    L_0x18eb:
        r180 = r125;
        r125 = r10;
        r10 = r151;
        r11 = 0;
        r10.setLength(r11);
        r11 = (r5 > r12 ? 1 : (r5 == r12 ? 0 : -1));
        if (r11 == 0) goto L_0x191f;
    L_0x18f9:
        r10.append(r9);
        r11 = "    Bluetooth Scan (total blamed realtime): ";
        r10.append(r11);
        formatTimeMs(r10, r12);
        r11 = " (";
        r10.append(r11);
        r10.append(r2);
        r10.append(r1);
        r11 = r4.isRunningLocked();
        if (r11 == 0) goto L_0x191a;
    L_0x1915:
        r11 = " (currently running)";
        r10.append(r11);
    L_0x191a:
        r11 = "\n";
        r10.append(r11);
    L_0x191f:
        r10.append(r9);
        r11 = "    Bluetooth Scan (total actual realtime): ";
        r10.append(r11);
        formatTimeMs(r10, r5);
        r11 = " (";
        r10.append(r11);
        r10.append(r2);
        r10.append(r1);
        r11 = r4.isRunningLocked();
        if (r11 == 0) goto L_0x1940;
    L_0x193b:
        r11 = " (currently running)";
        r10.append(r11);
    L_0x1940:
        r11 = "\n";
        r10.append(r11);
        r11 = r4;
        r189 = r5;
        r4 = r168;
        r6 = (r4 > r46 ? 1 : (r4 == r46 ? 0 : -1));
        if (r6 > 0) goto L_0x1956;
    L_0x194e:
        r6 = r150;
        if (r6 <= 0) goto L_0x1953;
    L_0x1952:
        goto L_0x1958;
    L_0x1953:
        r150 = r2;
        goto L_0x1982;
    L_0x1956:
        r6 = r150;
    L_0x1958:
        r10.append(r9);
        r150 = r2;
        r2 = "    Bluetooth Scan (background realtime): ";
        r10.append(r2);
        formatTimeMs(r10, r4);
        r2 = " (";
        r10.append(r2);
        r10.append(r6);
        r10.append(r1);
        if (r3 == 0) goto L_0x197d;
    L_0x1972:
        r2 = r3.isRunningLocked();
        if (r2 == 0) goto L_0x197d;
    L_0x1978:
        r2 = " (currently running in background)";
        r10.append(r2);
    L_0x197d:
        r2 = "\n";
        r10.append(r2);
    L_0x1982:
        r10.append(r9);
        r2 = "    Bluetooth Scan Results: ";
        r10.append(r2);
        r10.append(r7);
        r2 = " (";
        r10.append(r2);
        r10.append(r14);
        r2 = " in background)";
        r10.append(r2);
        r151 = r1;
        r1 = r170;
        r168 = (r1 > r46 ? 1 : (r1 == r46 ? 0 : -1));
        if (r168 > 0) goto L_0x19b9;
    L_0x19a2:
        r168 = r3;
        r169 = r4;
        r3 = r174;
        r5 = (r3 > r46 ? 1 : (r3 == r46 ? 0 : -1));
        if (r5 <= 0) goto L_0x19ad;
    L_0x19ac:
        goto L_0x19bf;
    L_0x19ad:
        r174 = r1;
        r171 = r172;
        r1 = r180;
        r5 = r184;
        r173 = r0;
        goto L_0x1a2d;
    L_0x19b9:
        r168 = r3;
        r169 = r4;
        r3 = r174;
    L_0x19bf:
        r5 = "\n";
        r10.append(r5);
        r10.append(r9);
        r5 = "    Unoptimized Bluetooth Scan (realtime): ";
        r10.append(r5);
        formatTimeMs(r10, r1);
        r5 = " (max ";
        r10.append(r5);
        r174 = r1;
        r1 = r172;
        formatTimeMs(r10, r1);
        r5 = r184;
        r10.append(r5);
        if (r15 == 0) goto L_0x19f3;
    L_0x19e2:
        r171 = r15.isRunningLocked();
        if (r171 == 0) goto L_0x19f0;
    L_0x19e8:
        r171 = r1;
        r1 = " (currently running unoptimized)";
        r10.append(r1);
        goto L_0x19f5;
    L_0x19f0:
        r171 = r1;
        goto L_0x19f5;
    L_0x19f3:
        r171 = r1;
    L_0x19f5:
        if (r0 == 0) goto L_0x1a29;
    L_0x19f7:
        r1 = (r3 > r46 ? 1 : (r3 == r46 ? 0 : -1));
        if (r1 <= 0) goto L_0x1a29;
    L_0x19fb:
        r1 = "\n";
        r10.append(r1);
        r10.append(r9);
        r1 = "    Unoptimized Bluetooth Scan (background realtime): ";
        r10.append(r1);
        formatTimeMs(r10, r3);
        r1 = " (max ";
        r10.append(r1);
        r1 = r180;
        formatTimeMs(r10, r1);
        r10.append(r5);
        r173 = r0.isRunningLocked();
        if (r173 == 0) goto L_0x1a26;
    L_0x1a1e:
        r173 = r0;
        r0 = " (currently running unoptimized in background)";
        r10.append(r0);
        goto L_0x1a2d;
    L_0x1a26:
        r173 = r0;
        goto L_0x1a2d;
    L_0x1a29:
        r173 = r0;
        r1 = r180;
    L_0x1a2d:
        r0 = r10.toString();
        r8.println(r0);
        r88 = 1;
        goto L_0x1a5c;
    L_0x1a37:
        r178 = r2;
        r182 = r5;
        r149 = r10;
        r185 = r11;
        r187 = r14;
        r10 = r151;
        r5 = r184;
        r151 = r1;
        r11 = r4;
        goto L_0x1a5c;
    L_0x1a49:
        r178 = r2;
        r182 = r5;
        r149 = r10;
        r185 = r11;
        r199 = r13;
        r187 = r14;
        r10 = r151;
        r5 = r184;
        r151 = r1;
        r11 = r4;
    L_0x1a5c:
        r0 = r81.hasUserActivity();
        r7 = ", ";
        if (r0 == 0) goto L_0x1aad;
    L_0x1a64:
        r0 = 0;
        r1 = 0;
    L_0x1a66:
        r2 = android.os.BatteryStats.Uid.NUM_USER_ACTIVITY_TYPES;
        if (r1 >= r2) goto L_0x1a9d;
    L_0x1a6a:
        r2 = r230;
        r15 = r81;
        r3 = r15.getUserActivityCount(r1, r2);
        if (r3 == 0) goto L_0x1a94;
    L_0x1a74:
        if (r0 != 0) goto L_0x1a81;
    L_0x1a76:
        r4 = 0;
        r10.setLength(r4);
        r4 = "    User activity: ";
        r10.append(r4);
        r0 = 1;
        goto L_0x1a84;
    L_0x1a81:
        r10.append(r7);
    L_0x1a84:
        r10.append(r3);
        r13 = r154;
        r10.append(r13);
        r4 = android.os.BatteryStats.Uid.USER_ACTIVITY_TYPES;
        r4 = r4[r1];
        r10.append(r4);
        goto L_0x1a96;
    L_0x1a94:
        r13 = r154;
    L_0x1a96:
        r1 = r1 + 1;
        r154 = r13;
        r81 = r15;
        goto L_0x1a66;
    L_0x1a9d:
        r2 = r230;
        r15 = r81;
        r13 = r154;
        if (r0 == 0) goto L_0x1ab3;
    L_0x1aa5:
        r1 = r10.toString();
        r8.println(r1);
        goto L_0x1ab3;
    L_0x1aad:
        r2 = r230;
        r15 = r81;
        r13 = r154;
        r14 = r15.getWakelockStats();
        r0 = 0;
        r3 = 0;
        r168 = 0;
        r170 = 0;
        r6 = 0;
        r12 = r14.size();
        r81 = 1;
        r12 = r12 + -1;
        r202 = r168;
        r204 = r170;
        r218 = r0;
        r0 = r3;
        r3 = r218;
    L_0x1ad2:
        if (r12 < 0) goto L_0x1bce;
    L_0x1ad4:
        r81 = r14.valueAt(r12);
        r150 = r11;
        r11 = r81;
        r11 = (android.os.BatteryStats.Uid.Wakelock) r11;
        r81 = ": ";
        r168 = r0;
        r0 = 0;
        r10.setLength(r0);
        r10.append(r9);
        r0 = "    Wake lock ";
        r10.append(r0);
        r0 = r14.keyAt(r12);
        r0 = (java.lang.String) r0;
        r10.append(r0);
        r0 = 1;
        r1 = r11.getWakeTime(r0);
        r154 = "full";
        r218 = r168;
        r168 = r13;
        r169 = r14;
        r13 = r218;
        r0 = r10;
        r206 = r151;
        r151 = r12;
        r172 = r13;
        r170 = r178;
        r12 = r3;
        r4 = r2;
        r2 = r187;
        r14 = r4;
        r4 = r154;
        r154 = r5;
        r174 = r182;
        r5 = r230;
        r178 = r7;
        r7 = r6;
        r6 = r81;
        r81 = printWakeLock(r0, r1, r2, r4, r5, r6);
        r0 = 0;
        r179 = r11.getWakeTime(r0);
        r4 = "partial";
        r0 = r10;
        r1 = r179;
        r6 = r81;
        r81 = printWakeLock(r0, r1, r2, r4, r5, r6);
        if (r179 == 0) goto L_0x1b3d;
    L_0x1b38:
        r0 = r179.getSubTimer();
        goto L_0x1b3e;
    L_0x1b3d:
        r0 = 0;
    L_0x1b3e:
        r1 = r0;
        r4 = "background partial";
        r0 = r10;
        r2 = r187;
        r5 = r230;
        r6 = r81;
        r81 = printWakeLock(r0, r1, r2, r4, r5, r6);
        r0 = 2;
        r1 = r11.getWakeTime(r0);
        r4 = "window";
        r0 = r10;
        r6 = r81;
        r81 = printWakeLock(r0, r1, r2, r4, r5, r6);
        r0 = 18;
        r1 = r11.getWakeTime(r0);
        r4 = "draw";
        r0 = r10;
        r6 = r81;
        r0 = printWakeLock(r0, r1, r2, r4, r5, r6);
        r1 = " realtime";
        r10.append(r1);
        r1 = r10.toString();
        r8.println(r1);
        r88 = 1;
        r6 = r7 + 1;
        r1 = 1;
        r2 = r11.getWakeTime(r1);
        r3 = r187;
        r1 = computeWakeLock(r2, r3, r14);
        r1 = r1 + r12;
        r5 = 0;
        r7 = r11.getWakeTime(r5);
        r12 = computeWakeLock(r7, r3, r14);
        r12 = r172 + r12;
        r5 = 2;
        r7 = r11.getWakeTime(r5);
        r172 = computeWakeLock(r7, r3, r14);
        r7 = r6;
        r5 = r202;
        r202 = r5 + r172;
        r5 = 18;
        r5 = r11.getWakeTime(r5);
        r5 = computeWakeLock(r5, r3, r14);
        r81 = r0;
        r180 = r1;
        r0 = r204;
        r204 = r0 + r5;
        r0 = r151 + -1;
        r6 = r7;
        r2 = r14;
        r11 = r150;
        r5 = r154;
        r14 = r169;
        r182 = r174;
        r7 = r178;
        r3 = r180;
        r151 = r206;
        r178 = r170;
        r218 = r12;
        r12 = r0;
        r0 = r218;
        r13 = r168;
        goto L_0x1ad2;
    L_0x1bce:
        r172 = r0;
        r154 = r5;
        r150 = r11;
        r168 = r13;
        r169 = r14;
        r206 = r151;
        r170 = r178;
        r174 = r182;
        r0 = r204;
        r14 = r2;
        r178 = r7;
        r151 = r12;
        r12 = r3;
        r7 = r6;
        r3 = r187;
        r5 = r202;
        r2 = 1;
        if (r7 <= r2) goto L_0x1ce1;
    L_0x1bee:
        r179 = 0;
        r181 = 0;
        r2 = r15.getAggregatedPartialWakelockTimer();
        if (r2 == 0) goto L_0x1c1d;
    L_0x1bf8:
        r2 = r15.getAggregatedPartialWakelockTimer();
        r187 = r3;
        r3 = r125;
        r179 = r2.getTotalDurationMsLocked(r3);
        r11 = r2.getSubTimer();
        if (r11 == 0) goto L_0x1c10;
    L_0x1c0b:
        r125 = r11.getTotalDurationMsLocked(r3);
        goto L_0x1c12;
    L_0x1c10:
        r125 = r46;
    L_0x1c12:
        r181 = r125;
        r125 = r3;
        r81 = r15;
        r2 = r179;
        r14 = r181;
        goto L_0x1c27;
    L_0x1c1d:
        r187 = r3;
        r3 = r125;
        r81 = r15;
        r2 = r179;
        r14 = r181;
    L_0x1c27:
        r4 = (r2 > r46 ? 1 : (r2 == r46 ? 0 : -1));
        if (r4 != 0) goto L_0x1c44;
    L_0x1c2b:
        r4 = (r14 > r46 ? 1 : (r14 == r46 ? 0 : -1));
        if (r4 != 0) goto L_0x1c44;
    L_0x1c2f:
        r4 = (r12 > r46 ? 1 : (r12 == r46 ? 0 : -1));
        if (r4 != 0) goto L_0x1c44;
    L_0x1c33:
        r4 = (r172 > r46 ? 1 : (r172 == r46 ? 0 : -1));
        if (r4 != 0) goto L_0x1c44;
    L_0x1c37:
        r4 = (r5 > r46 ? 1 : (r5 == r46 ? 0 : -1));
        if (r4 == 0) goto L_0x1c3c;
    L_0x1c3b:
        goto L_0x1c44;
    L_0x1c3c:
        r11 = r178;
        r178 = r12;
        r12 = r172;
        goto L_0x1ceb;
    L_0x1c44:
        r4 = 0;
        r10.setLength(r4);
        r10.append(r9);
        r4 = "    TOTAL wake: ";
        r10.append(r4);
        r4 = 0;
        r11 = (r12 > r46 ? 1 : (r12 == r46 ? 0 : -1));
        if (r11 == 0) goto L_0x1c5e;
    L_0x1c55:
        r4 = 1;
        formatTimeMs(r10, r12);
        r11 = "full";
        r10.append(r11);
    L_0x1c5e:
        r11 = (r172 > r46 ? 1 : (r172 == r46 ? 0 : -1));
        if (r11 == 0) goto L_0x1c7e;
    L_0x1c62:
        if (r4 == 0) goto L_0x1c6a;
    L_0x1c64:
        r11 = r178;
        r10.append(r11);
        goto L_0x1c6c;
    L_0x1c6a:
        r11 = r178;
    L_0x1c6c:
        r4 = 1;
        r178 = r12;
        r12 = r172;
        formatTimeMs(r10, r12);
        r151 = r4;
        r4 = "blamed partial";
        r10.append(r4);
        r4 = r151;
        goto L_0x1c84;
    L_0x1c7e:
        r11 = r178;
        r178 = r12;
        r12 = r172;
    L_0x1c84:
        r151 = (r2 > r46 ? 1 : (r2 == r46 ? 0 : -1));
        if (r151 == 0) goto L_0x1c99;
    L_0x1c88:
        if (r4 == 0) goto L_0x1c8d;
    L_0x1c8a:
        r10.append(r11);
    L_0x1c8d:
        r4 = 1;
        formatTimeMs(r10, r2);
        r172 = r2;
        r2 = "actual partial";
        r10.append(r2);
        goto L_0x1c9b;
    L_0x1c99:
        r172 = r2;
    L_0x1c9b:
        r2 = (r14 > r46 ? 1 : (r14 == r46 ? 0 : -1));
        if (r2 == 0) goto L_0x1cad;
    L_0x1c9f:
        if (r4 == 0) goto L_0x1ca4;
    L_0x1ca1:
        r10.append(r11);
    L_0x1ca4:
        r4 = 1;
        formatTimeMs(r10, r14);
        r2 = "actual background partial";
        r10.append(r2);
    L_0x1cad:
        r2 = (r5 > r46 ? 1 : (r5 == r46 ? 0 : -1));
        if (r2 == 0) goto L_0x1cc0;
    L_0x1cb1:
        if (r4 == 0) goto L_0x1cb6;
    L_0x1cb3:
        r10.append(r11);
    L_0x1cb6:
        r4 = 1;
        formatTimeMs(r10, r5);
        r2 = "window";
        r10.append(r2);
    L_0x1cc0:
        r2 = (r0 > r46 ? 1 : (r0 == r46 ? 0 : -1));
        if (r2 == 0) goto L_0x1cd4;
    L_0x1cc4:
        if (r4 == 0) goto L_0x1ccb;
    L_0x1cc6:
        r2 = ",";
        r10.append(r2);
    L_0x1ccb:
        r4 = 1;
        formatTimeMs(r10, r0);
        r2 = "draw";
        r10.append(r2);
    L_0x1cd4:
        r2 = " realtime";
        r10.append(r2);
        r2 = r10.toString();
        r8.println(r2);
        goto L_0x1ceb;
    L_0x1ce1:
        r187 = r3;
        r81 = r15;
        r11 = r178;
        r178 = r12;
        r12 = r172;
    L_0x1ceb:
        r2 = r81.getMulticastWakelockStats();
        if (r2 == 0) goto L_0x1d2e;
    L_0x1cf1:
        r3 = r230;
        r14 = r187;
        r172 = r2.getTotalTimeLocked(r14, r3);
        r4 = r2.getCountLocked(r3);
        r151 = (r172 > r46 ? 1 : (r172 == r46 ? 0 : -1));
        if (r151 <= 0) goto L_0x1d2b;
    L_0x1d01:
        r180 = r0;
        r0 = 0;
        r10.setLength(r0);
        r10.append(r9);
        r0 = "    WiFi Multicast Wakelock";
        r10.append(r0);
        r0 = " count = ";
        r10.append(r0);
        r10.append(r4);
        r0 = " time = ";
        r10.append(r0);
        r0 = r172 + r18;
        r0 = r0 / r16;
        formatTimeMsNoSpace(r10, r0);
        r0 = r10.toString();
        r8.println(r0);
        goto L_0x1d34;
    L_0x1d2b:
        r180 = r0;
        goto L_0x1d34;
    L_0x1d2e:
        r3 = r230;
        r180 = r0;
        r14 = r187;
    L_0x1d34:
        r0 = r81.getSyncStats();
        r1 = r0.size();
        r4 = 1;
        r1 = r1 - r4;
    L_0x1d3e:
        if (r1 < 0) goto L_0x1df8;
    L_0x1d40:
        r4 = r0.valueAt(r1);
        r4 = (android.os.BatteryStats.Timer) r4;
        r172 = r4.getTotalTimeLocked(r14, r3);
        r172 = r172 + r18;
        r182 = r5;
        r5 = r172 / r16;
        r151 = r2;
        r2 = r4.getCountLocked(r3);
        r172 = r7;
        r7 = r4.getSubTimer();
        if (r7 == 0) goto L_0x1d67;
    L_0x1d5e:
        r187 = r12;
        r12 = r125;
        r125 = r7.getTotalDurationMsLocked(r12);
        goto L_0x1d6d;
    L_0x1d67:
        r187 = r12;
        r12 = r125;
        r125 = -1;
    L_0x1d6d:
        r189 = r125;
        if (r7 == 0) goto L_0x1d76;
    L_0x1d71:
        r125 = r7.getCountLocked(r3);
        goto L_0x1d78;
    L_0x1d76:
        r125 = r138;
    L_0x1d78:
        r126 = r125;
        r125 = r4;
        r4 = 0;
        r10.setLength(r4);
        r10.append(r9);
        r4 = "    Sync ";
        r10.append(r4);
        r4 = r0.keyAt(r1);
        r4 = (java.lang.String) r4;
        r10.append(r4);
        r4 = ": ";
        r10.append(r4);
        r4 = (r5 > r46 ? 1 : (r5 == r46 ? 0 : -1));
        if (r4 == 0) goto L_0x1dce;
    L_0x1d9a:
        formatTimeMs(r10, r5);
        r4 = "realtime (";
        r10.append(r4);
        r10.append(r2);
        r4 = r206;
        r10.append(r4);
        r191 = r5;
        r5 = r189;
        r173 = (r5 > r46 ? 1 : (r5 == r46 ? 0 : -1));
        if (r173 <= 0) goto L_0x1dc9;
    L_0x1db3:
        r10.append(r11);
        formatTimeMs(r10, r5);
        r173 = r0;
        r0 = "background (";
        r10.append(r0);
        r0 = r126;
        r10.append(r0);
        r10.append(r4);
        goto L_0x1ddd;
    L_0x1dc9:
        r173 = r0;
        r0 = r126;
        goto L_0x1ddd;
    L_0x1dce:
        r173 = r0;
        r191 = r5;
        r0 = r126;
        r5 = r189;
        r4 = r206;
        r0 = "(not used)";
        r10.append(r0);
    L_0x1ddd:
        r0 = r10.toString();
        r8.println(r0);
        r88 = 1;
        r1 = r1 + -1;
        r206 = r4;
        r125 = r12;
        r2 = r151;
        r7 = r172;
        r0 = r173;
        r5 = r182;
        r12 = r187;
        goto L_0x1d3e;
    L_0x1df8:
        r173 = r0;
        r151 = r2;
        r182 = r5;
        r172 = r7;
        r187 = r12;
        r12 = r125;
        r4 = r206;
        r0 = r81.getJobStats();
        r1 = r0.size();
        r2 = 1;
        r1 = r1 - r2;
    L_0x1e10:
        if (r1 < 0) goto L_0x1e9f;
    L_0x1e12:
        r2 = r0.valueAt(r1);
        r2 = (android.os.BatteryStats.Timer) r2;
        r5 = r2.getTotalTimeLocked(r14, r3);
        r5 = r5 + r18;
        r5 = r5 / r16;
        r7 = r2.getCountLocked(r3);
        r125 = r14;
        r14 = r2.getSubTimer();
        if (r14 == 0) goto L_0x1e31;
    L_0x1e2c:
        r189 = r14.getTotalDurationMsLocked(r12);
        goto L_0x1e33;
    L_0x1e31:
        r189 = -1;
    L_0x1e33:
        r191 = r189;
        if (r14 == 0) goto L_0x1e3c;
    L_0x1e37:
        r15 = r14.getCountLocked(r3);
        goto L_0x1e3e;
    L_0x1e3c:
        r15 = r138;
    L_0x1e3e:
        r184 = r2;
        r2 = 0;
        r10.setLength(r2);
        r10.append(r9);
        r2 = "    Job ";
        r10.append(r2);
        r2 = r0.keyAt(r1);
        r2 = (java.lang.String) r2;
        r10.append(r2);
        r2 = ": ";
        r10.append(r2);
        r2 = (r5 > r46 ? 1 : (r5 == r46 ? 0 : -1));
        if (r2 == 0) goto L_0x1e87;
    L_0x1e5e:
        formatTimeMs(r10, r5);
        r2 = "realtime (";
        r10.append(r2);
        r10.append(r7);
        r10.append(r4);
        r189 = r5;
        r5 = r191;
        r2 = (r5 > r46 ? 1 : (r5 == r46 ? 0 : -1));
        if (r2 <= 0) goto L_0x1e90;
    L_0x1e75:
        r10.append(r11);
        formatTimeMs(r10, r5);
        r2 = "background (";
        r10.append(r2);
        r10.append(r15);
        r10.append(r4);
        goto L_0x1e90;
    L_0x1e87:
        r189 = r5;
        r5 = r191;
        r2 = "(not used)";
        r10.append(r2);
    L_0x1e90:
        r2 = r10.toString();
        r8.println(r2);
        r88 = 1;
        r1 = r1 + -1;
        r14 = r125;
        goto L_0x1e10;
    L_0x1e9f:
        r125 = r14;
        r1 = r81.getJobCompletionStats();
        r2 = r1.size();
        r5 = 1;
        r2 = r2 - r5;
    L_0x1eab:
        if (r2 < 0) goto L_0x1f0c;
    L_0x1ead:
        r6 = r1.valueAt(r2);
        r6 = (android.util.SparseIntArray) r6;
        if (r6 == 0) goto L_0x1f00;
    L_0x1eb5:
        r228.print(r229);
        r7 = "    Job Completions ";
        r8.print(r7);
        r7 = r1.keyAt(r2);
        r7 = (java.lang.String) r7;
        r8.print(r7);
        r7 = ":";
        r8.print(r7);
        r7 = 0;
    L_0x1ecc:
        r14 = r6.size();
        if (r7 >= r14) goto L_0x1ef8;
    L_0x1ed2:
        r14 = r168;
        r8.print(r14);
        r15 = r6.keyAt(r7);
        r15 = android.app.job.JobParameters.getReasonName(r15);
        r8.print(r15);
        r15 = r199;
        r8.print(r15);
        r5 = r6.valueAt(r7);
        r8.print(r5);
        r5 = "x)";
        r8.print(r5);
        r7 = r7 + 1;
        r5 = 1;
        goto L_0x1ecc;
    L_0x1ef8:
        r14 = r168;
        r15 = r199;
        r228.println();
        goto L_0x1f04;
    L_0x1f00:
        r14 = r168;
        r15 = r199;
    L_0x1f04:
        r2 = r2 + -1;
        r168 = r14;
        r199 = r15;
        r5 = 1;
        goto L_0x1eab;
    L_0x1f0c:
        r14 = r168;
        r15 = r199;
        r2 = r81;
        r2.getDeferredJobsLineLocked(r10, r3);
        r5 = r10.length();
        if (r5 <= 0) goto L_0x1f27;
    L_0x1f1b:
        r5 = "    Jobs deferred on launch ";
        r8.print(r5);
        r5 = r10.toString();
        r8.println(r5);
    L_0x1f27:
        r5 = r2.getFlashlightTurnedOnTimer();
        r6 = "Flashlight";
        r7 = r9;
        r81 = r92;
        r191 = r197;
        r189 = r200;
        r9 = 2;
        r92 = 1;
        r218 = r141;
        r141 = r160;
        r160 = r218;
        r8 = r228;
        r135 = r0;
        r0 = r9;
        r128 = r146;
        r9 = r10;
        r146 = r155;
        r155 = r128;
        r128 = r10;
        r10 = r5;
        r5 = r228;
        r168 = r185;
        r184 = r195;
        r195 = r187;
        r188 = r2;
        r186 = r178;
        r2 = 0;
        r178 = r170;
        r170 = r158;
        r158 = r132;
        r133 = r119;
        r119 = r1;
        r0 = r12;
        r13 = r11;
        r11 = r125;
        r197 = r15;
        r15 = r13;
        r13 = r230;
        r207 = r14;
        r198 = r125;
        r125 = r156;
        r156 = r193;
        r14 = r229;
        r208 = r15;
        r15 = r6;
        r6 = printTimer(r8, r9, r10, r11, r13, r14, r15);
        r6 = r88 | r6;
        r10 = r188.getCameraTurnedOnTimer();
        r15 = "Camera";
        r9 = r128;
        r11 = r198;
        r8 = printTimer(r8, r9, r10, r11, r13, r14, r15);
        r6 = r6 | r8;
        r10 = r188.getVideoTurnedOnTimer();
        r15 = "Video";
        r8 = r228;
        r8 = printTimer(r8, r9, r10, r11, r13, r14, r15);
        r6 = r6 | r8;
        r10 = r188.getAudioTurnedOnTimer();
        r15 = "Audio";
        r8 = r228;
        r8 = printTimer(r8, r9, r10, r11, r13, r14, r15);
        r6 = r6 | r8;
        r15 = r188.getSensorStats();
        r14 = r15.size();
        r8 = 0;
    L_0x1fb1:
        if (r8 >= r14) goto L_0x20a9;
    L_0x1fb3:
        r9 = r15.valueAt(r8);
        r9 = (android.os.BatteryStats.Uid.Sensor) r9;
        r10 = r15.keyAt(r8);
        r13 = r128;
        r13.setLength(r2);
        r13.append(r7);
        r11 = "    Sensor ";
        r13.append(r11);
        r11 = r9.getHandle();
        r12 = -10000; // 0xffffffffffffd8f0 float:NaN double:NaN;
        if (r11 != r12) goto L_0x1fd8;
    L_0x1fd2:
        r12 = "GPS";
        r13.append(r12);
        goto L_0x1fdb;
    L_0x1fd8:
        r13.append(r11);
    L_0x1fdb:
        r12 = ": ";
        r13.append(r12);
        r12 = r9.getSensorTime();
        if (r12 == 0) goto L_0x207c;
    L_0x1fe6:
        r88 = r6;
        r6 = r198;
        r193 = r12.getTotalTimeLocked(r6, r3);
        r193 = r193 + r18;
        r128 = r10;
        r198 = r11;
        r10 = r193 / r16;
        r2 = r12.getCountLocked(r3);
        r194 = r14;
        r14 = r9.getSensorBackgroundTime();
        if (r14 == 0) goto L_0x2007;
    L_0x2002:
        r199 = r14.getCountLocked(r3);
        goto L_0x2009;
    L_0x2007:
        r199 = 0;
    L_0x2009:
        r200 = r199;
        r201 = r6;
        r6 = r12.getTotalDurationMsLocked(r0);
        if (r14 == 0) goto L_0x2018;
    L_0x2013:
        r203 = r14.getTotalDurationMsLocked(r0);
        goto L_0x201a;
    L_0x2018:
        r203 = r46;
    L_0x201a:
        r205 = r203;
        r199 = (r10 > r46 ? 1 : (r10 == r46 ? 0 : -1));
        if (r199 == 0) goto L_0x206a;
    L_0x2020:
        r199 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1));
        if (r199 == 0) goto L_0x202f;
    L_0x2024:
        formatTimeMs(r13, r10);
        r203 = r0;
        r0 = "blamed realtime, ";
        r13.append(r0);
        goto L_0x2031;
    L_0x202f:
        r203 = r0;
    L_0x2031:
        formatTimeMs(r13, r6);
        r0 = "realtime (";
        r13.append(r0);
        r13.append(r2);
        r13.append(r4);
        r0 = r205;
        r199 = (r0 > r46 ? 1 : (r0 == r46 ? 0 : -1));
        if (r199 != 0) goto L_0x2050;
    L_0x2046:
        r199 = r2;
        r2 = r200;
        if (r2 <= 0) goto L_0x204d;
    L_0x204c:
        goto L_0x2054;
    L_0x204d:
        r6 = r208;
        goto L_0x207b;
    L_0x2050:
        r199 = r2;
        r2 = r200;
    L_0x2054:
        r205 = r6;
        r6 = r208;
        r13.append(r6);
        formatTimeMs(r13, r0);
        r7 = "background (";
        r13.append(r7);
        r13.append(r2);
        r13.append(r4);
        goto L_0x207b;
    L_0x206a:
        r203 = r0;
        r199 = r2;
        r2 = r200;
        r0 = r205;
        r205 = r6;
        r6 = r208;
        r7 = "(not used)";
        r13.append(r7);
    L_0x207b:
        goto L_0x208f;
    L_0x207c:
        r203 = r0;
        r88 = r6;
        r128 = r10;
        r194 = r14;
        r201 = r198;
        r6 = r208;
        r198 = r11;
        r0 = "(not used)";
        r13.append(r0);
    L_0x208f:
        r0 = r13.toString();
        r5.println(r0);
        r0 = 1;
        r8 = r8 + 1;
        r7 = r229;
        r208 = r6;
        r128 = r13;
        r14 = r194;
        r198 = r201;
        r2 = 0;
        r6 = r0;
        r0 = r203;
        goto L_0x1fb1;
    L_0x20a9:
        r203 = r0;
        r88 = r6;
        r194 = r14;
        r13 = r128;
        r201 = r198;
        r6 = r208;
        r10 = r188.getVibratorOnTimer();
        r0 = "Vibrator";
        r8 = r228;
        r9 = r13;
        r11 = r201;
        r1 = r13;
        r13 = r230;
        r2 = r194;
        r14 = r229;
        r4 = r15;
        r15 = r0;
        r0 = printTimer(r8, r9, r10, r11, r13, r14, r15);
        r0 = r88 | r0;
        r10 = r188.getForegroundActivityTimer();
        r15 = "Foreground activities";
        r9 = r1;
        r7 = printTimer(r8, r9, r10, r11, r13, r14, r15);
        r0 = r0 | r7;
        r10 = r188.getForegroundServiceTimer();
        r15 = "Foreground services";
        r7 = printTimer(r8, r9, r10, r11, r13, r14, r15);
        r0 = r0 | r7;
        r7 = 0;
        r9 = 0;
    L_0x20e9:
        r10 = 7;
        if (r9 >= r10) goto L_0x2134;
    L_0x20ec:
        r12 = r188;
        r10 = r201;
        r13 = r12.getProcessStateTime(r9, r10, r3);
        r15 = (r13 > r46 ? 1 : (r13 == r46 ? 0 : -1));
        if (r15 <= 0) goto L_0x2129;
    L_0x20f8:
        r7 = r7 + r13;
        r15 = 0;
        r1.setLength(r15);
        r198 = r10;
        r10 = r229;
        r1.append(r10);
        r11 = "    ";
        r1.append(r11);
        r11 = android.os.BatteryStats.Uid.PROCESS_STATE_NAMES;
        r11 = r11[r9];
        r1.append(r11);
        r11 = " for: ";
        r1.append(r11);
        r200 = r13 + r18;
        r205 = r7;
        r7 = r200 / r16;
        formatTimeMs(r1, r7);
        r7 = r1.toString();
        r5.println(r7);
        r0 = 1;
        r7 = r205;
        goto L_0x212d;
    L_0x2129:
        r198 = r10;
        r10 = r229;
    L_0x212d:
        r9 = r9 + 1;
        r188 = r12;
        r201 = r198;
        goto L_0x20e9;
    L_0x2134:
        r10 = r229;
        r12 = r188;
        r198 = r201;
        r9 = (r7 > r46 ? 1 : (r7 == r46 ? 0 : -1));
        if (r9 <= 0) goto L_0x2158;
    L_0x213e:
        r9 = 0;
        r1.setLength(r9);
        r1.append(r10);
        r9 = "    Total running: ";
        r1.append(r9);
        r13 = r7 + r18;
        r13 = r13 / r16;
        formatTimeMs(r1, r13);
        r9 = r1.toString();
        r5.println(r9);
    L_0x2158:
        r13 = r12.getUserCpuTimeUs(r3);
        r200 = r12.getSystemCpuTimeUs(r3);
        r9 = (r13 > r46 ? 1 : (r13 == r46 ? 0 : -1));
        if (r9 > 0) goto L_0x216c;
    L_0x2164:
        r9 = (r200 > r46 ? 1 : (r200 == r46 ? 0 : -1));
        if (r9 <= 0) goto L_0x2169;
    L_0x2168:
        goto L_0x216c;
    L_0x2169:
        r205 = r7;
        goto L_0x2191;
    L_0x216c:
        r9 = 0;
        r1.setLength(r9);
        r1.append(r10);
        r9 = "    Total cpu time: u=";
        r1.append(r9);
        r205 = r7;
        r7 = r13 / r16;
        formatTimeMs(r1, r7);
        r7 = "s=";
        r1.append(r7);
        r7 = r200 / r16;
        formatTimeMs(r1, r7);
        r7 = r1.toString();
        r5.println(r7);
    L_0x2191:
        r7 = r12.getCpuFreqTimes(r3);
        if (r7 == 0) goto L_0x21cf;
    L_0x2197:
        r8 = 0;
        r1.setLength(r8);
        r8 = "    Total cpu time per freq:";
        r1.append(r8);
        r8 = 0;
    L_0x21a1:
        r9 = r7.length;
        if (r8 >= r9) goto L_0x21c3;
    L_0x21a4:
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r11 = r207;
        r9.append(r11);
        r207 = r13;
        r13 = r7[r8];
        r9.append(r13);
        r9 = r9.toString();
        r1.append(r9);
        r8 = r8 + 1;
        r13 = r207;
        r207 = r11;
        goto L_0x21a1;
    L_0x21c3:
        r11 = r207;
        r207 = r13;
        r8 = r1.toString();
        r5.println(r8);
        goto L_0x21d3;
    L_0x21cf:
        r11 = r207;
        r207 = r13;
    L_0x21d3:
        r8 = r12.getScreenOffCpuFreqTimes(r3);
        if (r8 == 0) goto L_0x2204;
    L_0x21d9:
        r9 = 0;
        r1.setLength(r9);
        r9 = "    Total screen-off cpu time per freq:";
        r1.append(r9);
        r9 = 0;
    L_0x21e3:
        r13 = r8.length;
        if (r9 >= r13) goto L_0x21fd;
    L_0x21e6:
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r13.append(r11);
        r14 = r8[r9];
        r13.append(r14);
        r13 = r13.toString();
        r1.append(r13);
        r9 = r9 + 1;
        goto L_0x21e3;
    L_0x21fd:
        r9 = r1.toString();
        r5.println(r9);
    L_0x2204:
        r9 = 0;
    L_0x2205:
        r13 = 7;
        if (r9 >= r13) goto L_0x22bd;
    L_0x2208:
        r13 = r12.getCpuFreqTimes(r3, r9);
        if (r13 == 0) goto L_0x225e;
    L_0x220e:
        r14 = 0;
        r1.setLength(r14);
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "    Cpu times per freq at state ";
        r14.append(r15);
        r15 = android.os.BatteryStats.Uid.PROCESS_STATE_NAMES;
        r15 = r15[r9];
        r14.append(r15);
        r15 = ":";
        r14.append(r15);
        r14 = r14.toString();
        r1.append(r14);
        r14 = 0;
    L_0x2230:
        r15 = r13.length;
        if (r14 >= r15) goto L_0x2252;
    L_0x2233:
        r15 = new java.lang.StringBuilder;
        r15.<init>();
        r15.append(r11);
        r88 = r7;
        r128 = r8;
        r7 = r13[r14];
        r15.append(r7);
        r7 = r15.toString();
        r1.append(r7);
        r14 = r14 + 1;
        r7 = r88;
        r8 = r128;
        goto L_0x2230;
    L_0x2252:
        r88 = r7;
        r128 = r8;
        r7 = r1.toString();
        r5.println(r7);
        goto L_0x2262;
    L_0x225e:
        r88 = r7;
        r128 = r8;
    L_0x2262:
        r7 = r12.getScreenOffCpuFreqTimes(r3, r9);
        if (r7 == 0) goto L_0x22b1;
    L_0x2268:
        r8 = 0;
        r1.setLength(r8);
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r14 = "   Screen-off cpu times per freq at state ";
        r8.append(r14);
        r14 = android.os.BatteryStats.Uid.PROCESS_STATE_NAMES;
        r14 = r14[r9];
        r8.append(r14);
        r14 = ":";
        r8.append(r14);
        r8 = r8.toString();
        r1.append(r8);
        r8 = 0;
    L_0x228a:
        r14 = r7.length;
        if (r8 >= r14) goto L_0x22a8;
    L_0x228d:
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r14.append(r11);
        r15 = r11;
        r10 = r7[r8];
        r14.append(r10);
        r10 = r14.toString();
        r1.append(r10);
        r8 = r8 + 1;
        r10 = r229;
        r11 = r15;
        goto L_0x228a;
    L_0x22a8:
        r15 = r11;
        r8 = r1.toString();
        r5.println(r8);
        goto L_0x22b2;
    L_0x22b1:
        r15 = r11;
    L_0x22b2:
        r9 = r9 + 1;
        r10 = r229;
        r11 = r15;
        r7 = r88;
        r8 = r128;
        goto L_0x2205;
    L_0x22bd:
        r88 = r7;
        r128 = r8;
        r15 = r11;
        r7 = r12.getProcessStats();
        r8 = r7.size();
        r8 = r8 + -1;
    L_0x22cd:
        if (r8 < 0) goto L_0x2448;
    L_0x22cf:
        r9 = r7.valueAt(r8);
        r9 = (android.os.BatteryStats.Uid.Proc) r9;
        r10 = r9.getUserTime(r3);
        r13 = r9.getSystemTime(r3);
        r188 = r4;
        r4 = r9.getForegroundTime(r3);
        r194 = r0;
        r0 = r9.getStarts(r3);
        r202 = r2;
        r2 = r9.getNumCrashes(r3);
        r209 = r15;
        r15 = r9.getNumAnrs(r3);
        if (r3 != 0) goto L_0x22fc;
    L_0x22f7:
        r210 = r9.countExcessivePowers();
        goto L_0x22fe;
    L_0x22fc:
        r210 = 0;
    L_0x22fe:
        r211 = r210;
        r210 = (r10 > r46 ? 1 : (r10 == r46 ? 0 : -1));
        if (r210 != 0) goto L_0x2323;
    L_0x2304:
        r210 = (r13 > r46 ? 1 : (r13 == r46 ? 0 : -1));
        if (r210 != 0) goto L_0x2323;
    L_0x2308:
        r210 = (r4 > r46 ? 1 : (r4 == r46 ? 0 : -1));
        if (r210 != 0) goto L_0x2323;
    L_0x230c:
        if (r0 != 0) goto L_0x2323;
    L_0x230e:
        r3 = r211;
        if (r3 != 0) goto L_0x2325;
    L_0x2312:
        if (r2 != 0) goto L_0x2325;
    L_0x2314:
        if (r15 == 0) goto L_0x2317;
    L_0x2316:
        goto L_0x2325;
    L_0x2317:
        r4 = r228;
        r216 = r6;
        r210 = r12;
        r0 = r194;
        r12 = r229;
        goto L_0x2437;
    L_0x2323:
        r3 = r211;
    L_0x2325:
        r210 = r12;
        r12 = 0;
        r1.setLength(r12);
        r12 = r229;
        r1.append(r12);
        r211 = r9;
        r9 = "    Proc ";
        r1.append(r9);
        r9 = r7.keyAt(r8);
        r9 = (java.lang.String) r9;
        r1.append(r9);
        r9 = ":\n";
        r1.append(r9);
        r1.append(r12);
        r9 = "      CPU: ";
        r1.append(r9);
        formatTimeMs(r1, r10);
        r9 = "usr + ";
        r1.append(r9);
        formatTimeMs(r1, r13);
        r9 = "krn ; ";
        r1.append(r9);
        formatTimeMs(r1, r4);
        r9 = "fg";
        r1.append(r9);
        if (r0 != 0) goto L_0x2371;
    L_0x2369:
        if (r2 != 0) goto L_0x2371;
    L_0x236b:
        if (r15 == 0) goto L_0x236e;
    L_0x236d:
        goto L_0x2371;
    L_0x236e:
        r212 = r0;
        goto L_0x23ae;
    L_0x2371:
        r9 = "\n";
        r1.append(r9);
        r1.append(r12);
        r9 = "      ";
        r1.append(r9);
        r9 = 0;
        if (r0 == 0) goto L_0x238d;
    L_0x2381:
        r9 = 1;
        r1.append(r0);
        r212 = r0;
        r0 = " starts";
        r1.append(r0);
        goto L_0x238f;
    L_0x238d:
        r212 = r0;
    L_0x238f:
        if (r2 == 0) goto L_0x239f;
    L_0x2391:
        if (r9 == 0) goto L_0x2396;
    L_0x2393:
        r1.append(r6);
    L_0x2396:
        r9 = 1;
        r1.append(r2);
        r0 = " crashes";
        r1.append(r0);
    L_0x239f:
        if (r15 == 0) goto L_0x23ae;
    L_0x23a1:
        if (r9 == 0) goto L_0x23a6;
    L_0x23a3:
        r1.append(r6);
    L_0x23a6:
        r1.append(r15);
        r0 = " anrs";
        r1.append(r0);
    L_0x23ae:
        r0 = r1.toString();
        r213 = r4;
        r4 = r228;
        r4.println(r0);
        r0 = 0;
    L_0x23ba:
        if (r0 >= r3) goto L_0x242e;
    L_0x23bc:
        r5 = r211;
        r9 = r5.getExcessivePower(r0);
        if (r9 == 0) goto L_0x241b;
    L_0x23c4:
        r228.print(r229);
        r211 = r2;
        r2 = "      * Killed for ";
        r4.print(r2);
        r2 = r9.type;
        r215 = r3;
        r3 = 2;
        if (r2 != r3) goto L_0x23db;
    L_0x23d5:
        r2 = "cpu";
        r4.print(r2);
        goto L_0x23e1;
    L_0x23db:
        r2 = "unknown";
        r4.print(r2);
    L_0x23e1:
        r2 = " use: ";
        r4.print(r2);
        r2 = r9.usedTime;
        android.util.TimeUtils.formatDuration(r2, r4);
        r2 = " over ";
        r4.print(r2);
        r2 = r9.overTime;
        android.util.TimeUtils.formatDuration(r2, r4);
        r2 = r9.overTime;
        r2 = (r2 > r46 ? 1 : (r2 == r46 ? 0 : -1));
        if (r2 == 0) goto L_0x2416;
    L_0x23fb:
        r2 = " (";
        r4.print(r2);
        r2 = r9.usedTime;
        r216 = 100;
        r2 = r2 * r216;
        r217 = r5;
        r216 = r6;
        r5 = r9.overTime;
        r2 = r2 / r5;
        r4.print(r2);
        r2 = "%)";
        r4.println(r2);
        goto L_0x2423;
    L_0x2416:
        r217 = r5;
        r216 = r6;
        goto L_0x2423;
    L_0x241b:
        r211 = r2;
        r215 = r3;
        r217 = r5;
        r216 = r6;
    L_0x2423:
        r0 = r0 + 1;
        r2 = r211;
        r3 = r215;
        r6 = r216;
        r211 = r217;
        goto L_0x23ba;
    L_0x242e:
        r215 = r3;
        r216 = r6;
        r217 = r211;
        r211 = r2;
        r0 = 1;
    L_0x2437:
        r8 = r8 + -1;
        r3 = r230;
        r5 = r4;
        r4 = r188;
        r2 = r202;
        r15 = r209;
        r12 = r210;
        r6 = r216;
        goto L_0x22cd;
    L_0x2448:
        r194 = r0;
        r202 = r2;
        r188 = r4;
        r4 = r5;
        r210 = r12;
        r209 = r15;
        r12 = r229;
        r0 = r210.getPackageStats();
        r2 = r0.size();
        r2 = r2 + -1;
    L_0x2460:
        if (r2 < 0) goto L_0x255e;
    L_0x2462:
        r228.print(r229);
        r3 = "    Apk ";
        r4.print(r3);
        r3 = r0.keyAt(r2);
        r3 = (java.lang.String) r3;
        r4.print(r3);
        r3 = ":";
        r4.println(r3);
        r3 = 0;
        r5 = r0.valueAt(r2);
        r5 = (android.os.BatteryStats.Uid.Pkg) r5;
        r6 = r5.getWakeupAlarmStats();
        r8 = r6.size();
        r8 = r8 + -1;
    L_0x2489:
        if (r8 < 0) goto L_0x24b9;
    L_0x248b:
        r228.print(r229);
        r9 = "      Wakeup alarm ";
        r4.print(r9);
        r9 = r6.keyAt(r8);
        r9 = (java.lang.String) r9;
        r4.print(r9);
        r9 = ": ";
        r4.print(r9);
        r9 = r6.valueAt(r8);
        r9 = (android.os.BatteryStats.Counter) r9;
        r10 = r230;
        r9 = r9.getCountLocked(r10);
        r4.print(r9);
        r9 = " times";
        r4.println(r9);
        r3 = 1;
        r8 = r8 + -1;
        goto L_0x2489;
    L_0x24b9:
        r10 = r230;
        r8 = r5.getServiceStats();
        r9 = r8.size();
        r9 = r9 + -1;
    L_0x24c5:
        if (r9 < 0) goto L_0x2542;
    L_0x24c7:
        r11 = r8.valueAt(r9);
        r11 = (android.os.BatteryStats.Uid.Pkg.Serv) r11;
        r13 = r122;
        r122 = r11.getStartTime(r13, r10);
        r15 = r11.getStarts(r10);
        r211 = r0;
        r0 = r11.getLaunches(r10);
        r212 = (r122 > r46 ? 1 : (r122 == r46 ? 0 : -1));
        if (r212 != 0) goto L_0x24eb;
    L_0x24e1:
        if (r15 != 0) goto L_0x24eb;
    L_0x24e3:
        if (r0 == 0) goto L_0x24e6;
    L_0x24e5:
        goto L_0x24eb;
    L_0x24e6:
        r212 = r5;
        r213 = r6;
        goto L_0x2537;
    L_0x24eb:
        r212 = r5;
        r5 = 0;
        r1.setLength(r5);
        r1.append(r12);
        r5 = "      Service ";
        r1.append(r5);
        r5 = r8.keyAt(r9);
        r5 = (java.lang.String) r5;
        r1.append(r5);
        r5 = ":\n";
        r1.append(r5);
        r1.append(r12);
        r5 = "        Created for: ";
        r1.append(r5);
        r213 = r6;
        r5 = r122 / r16;
        formatTimeMs(r1, r5);
        r5 = "uptime\n";
        r1.append(r5);
        r1.append(r12);
        r5 = "        Starts: ";
        r1.append(r5);
        r1.append(r15);
        r5 = ", launches: ";
        r1.append(r5);
        r1.append(r0);
        r5 = r1.toString();
        r4.println(r5);
        r3 = 1;
    L_0x2537:
        r9 = r9 + -1;
        r122 = r13;
        r0 = r211;
        r5 = r212;
        r6 = r213;
        goto L_0x24c5;
    L_0x2542:
        r211 = r0;
        r212 = r5;
        r213 = r6;
        r13 = r122;
        if (r3 != 0) goto L_0x2554;
    L_0x254c:
        r228.print(r229);
        r0 = "      (nothing executed)";
        r4.println(r0);
    L_0x2554:
        r194 = 1;
        r2 = r2 + -1;
        r122 = r13;
        r0 = r211;
        goto L_0x2460;
    L_0x255e:
        r10 = r230;
        r211 = r0;
        r13 = r122;
        if (r194 != 0) goto L_0x256e;
    L_0x2566:
        r228.print(r229);
        r0 = "    (nothing executed)";
        r4.println(r0);
    L_0x256e:
        r0 = r133 + 1;
        r7 = r226;
        r12 = r0;
        r11 = r4;
        r122 = r13;
        r15 = r145;
        r155 = r146;
        r127 = r154;
        r151 = r168;
        r6 = r197;
        r4 = r198;
        r125 = r203;
        r13 = r209;
        r3 = 0;
        r14 = r1;
        r0 = r136;
        goto L_0x1406;
    L_0x258c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.BatteryStats.dumpLocked(android.content.Context, java.io.PrintWriter, java.lang.String, int, int, boolean):void");
    }

    static void printBitDescriptions(StringBuilder sb, int oldval, int newval, HistoryTag wakelockTag, BitDescription[] descriptions, boolean longNames) {
        int diff = oldval ^ newval;
        if (diff != 0) {
            String str;
            String str2;
            boolean didWake = false;
            int i = 0;
            while (true) {
                str = "\"";
                str2 = ":\"";
                if (i >= descriptions.length) {
                    break;
                }
                BitDescription bd = descriptions[i];
                if ((bd.mask & diff) != 0) {
                    sb.append(longNames ? WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER : ",");
                    String str3 = "=";
                    if (bd.shift < 0) {
                        sb.append((bd.mask & newval) != 0 ? "+" : "-");
                        sb.append(longNames ? bd.name : bd.shortName);
                        if (bd.mask == 1073741824 && wakelockTag != null) {
                            didWake = true;
                            sb.append(str3);
                            if (longNames) {
                                UserHandle.formatUid(sb, wakelockTag.uid);
                                sb.append(str2);
                                sb.append(wakelockTag.string);
                                sb.append(str);
                            } else {
                                sb.append(wakelockTag.poolIdx);
                            }
                        }
                    } else {
                        sb.append(longNames ? bd.name : bd.shortName);
                        sb.append(str3);
                        int val = (bd.mask & newval) >> bd.shift;
                        if (bd.values == null || val < 0 || val >= bd.values.length) {
                            sb.append(val);
                        } else {
                            sb.append(longNames ? bd.values[val] : bd.shortValues[val]);
                        }
                    }
                }
                i++;
            }
            if (!(didWake || wakelockTag == null)) {
                sb.append(longNames ? " wake_lock=" : ",w=");
                if (longNames) {
                    UserHandle.formatUid(sb, wakelockTag.uid);
                    sb.append(str2);
                    sb.append(wakelockTag.string);
                    sb.append(str);
                } else {
                    sb.append(wakelockTag.poolIdx);
                }
            }
        }
    }

    public void prepareForDumpLocked() {
    }

    private void printSizeValue(PrintWriter pw, long size) {
        float result = (float) size;
        String suffix = "";
        if (result >= 10240.0f) {
            suffix = "KB";
            result /= 1024.0f;
        }
        if (result >= 10240.0f) {
            suffix = "MB";
            result /= 1024.0f;
        }
        if (result >= 10240.0f) {
            suffix = "GB";
            result /= 1024.0f;
        }
        if (result >= 10240.0f) {
            suffix = "TB";
            result /= 1024.0f;
        }
        if (result >= 10240.0f) {
            suffix = "PB";
            result /= 1024.0f;
        }
        pw.print((int) result);
        pw.print(suffix);
    }

    private static boolean dumpTimeEstimate(PrintWriter pw, String label1, String label2, String label3, long estimatedTime) {
        if (estimatedTime < 0) {
            return false;
        }
        pw.print(label1);
        pw.print(label2);
        pw.print(label3);
        StringBuilder sb = new StringBuilder(64);
        formatTimeMs(sb, estimatedTime);
        pw.print(sb);
        pw.println();
        return true;
    }

    private static boolean dumpDurationSteps(PrintWriter pw, String prefix, String header, LevelStepTracker steps, boolean checkin) {
        PrintWriter printWriter = pw;
        String str = header;
        LevelStepTracker levelStepTracker = steps;
        int i = 0;
        if (levelStepTracker == null) {
            return false;
        }
        int count = levelStepTracker.mNumStepDurations;
        if (count <= 0) {
            return false;
        }
        if (!checkin) {
            printWriter.println(str);
        }
        String[] lineArgs = new String[5];
        int i2 = 0;
        while (i2 < count) {
            int count2;
            long duration = levelStepTracker.getDurationAt(i2);
            int level = levelStepTracker.getLevelAt(i2);
            long initMode = (long) levelStepTracker.getInitModeAt(i2);
            long modMode = (long) levelStepTracker.getModModeAt(i2);
            if (checkin) {
                lineArgs[i] = Long.toString(duration);
                lineArgs[1] = Integer.toString(level);
                String str2 = "";
                if ((modMode & 3) == 0) {
                    count2 = count;
                    i = ((int) (initMode & 3)) + 1;
                    if (i == 1) {
                        lineArgs[2] = "s-";
                    } else if (i == 2) {
                        lineArgs[2] = "s+";
                    } else if (i == 3) {
                        lineArgs[2] = "sd";
                    } else if (i != 4) {
                        lineArgs[2] = "?";
                    } else {
                        lineArgs[2] = "sds";
                    }
                } else {
                    count2 = count;
                    lineArgs[2] = str2;
                }
                if ((modMode & 4) == 0) {
                    lineArgs[3] = (initMode & 4) != 0 ? "p+" : "p-";
                } else {
                    lineArgs[3] = str2;
                }
                if ((modMode & 8) == 0) {
                    lineArgs[4] = (8 & initMode) != 0 ? "i+" : "i-";
                } else {
                    lineArgs[4] = str2;
                }
                dumpLine(printWriter, 0, "i", str, (Object[]) lineArgs);
            } else {
                count2 = count;
                pw.print(prefix);
                printWriter.print("#");
                printWriter.print(i2);
                printWriter.print(": ");
                TimeUtils.formatDuration(duration, printWriter);
                printWriter.print(" to ");
                printWriter.print(level);
                boolean haveModes = false;
                String str3 = " (";
                if ((modMode & 3) == 0) {
                    printWriter.print(str3);
                    int i3 = ((int) (initMode & 3)) + 1;
                    if (i3 == 1) {
                        printWriter.print("screen-off");
                    } else if (i3 == 2) {
                        printWriter.print("screen-on");
                    } else if (i3 == 3) {
                        printWriter.print("screen-doze");
                    } else if (i3 != 4) {
                        printWriter.print("screen-?");
                    } else {
                        printWriter.print("screen-doze-suspend");
                    }
                    haveModes = true;
                }
                String str4 = ", ";
                if ((modMode & 4) == 0) {
                    printWriter.print(haveModes ? str4 : str3);
                    printWriter.print((initMode & 4) != 0 ? "power-save-on" : "power-save-off");
                    haveModes = true;
                }
                if ((modMode & 8) == 0) {
                    if (!haveModes) {
                        str4 = str3;
                    }
                    printWriter.print(str4);
                    printWriter.print((8 & initMode) != 0 ? "device-idle-on" : "device-idle-off");
                    haveModes = true;
                }
                if (haveModes) {
                    printWriter.print(")");
                }
                pw.println();
            }
            i2++;
            str = header;
            levelStepTracker = steps;
            count = count2;
            i = 0;
        }
        return true;
    }

    private static void dumpDurationSteps(ProtoOutputStream proto, long fieldId, LevelStepTracker steps) {
        ProtoOutputStream protoOutputStream = proto;
        LevelStepTracker levelStepTracker = steps;
        if (levelStepTracker != null) {
            int count = levelStepTracker.mNumStepDurations;
            for (int i = 0; i < count; i++) {
                int i2;
                long token = proto.start(fieldId);
                protoOutputStream.write(1112396529665L, levelStepTracker.getDurationAt(i));
                protoOutputStream.write(1120986464258L, levelStepTracker.getLevelAt(i));
                long initMode = (long) levelStepTracker.getInitModeAt(i);
                long modMode = (long) levelStepTracker.getModModeAt(i);
                int ds = 0;
                int i3 = 2;
                int i4 = 1;
                if ((modMode & 3) == 0) {
                    i2 = ((int) (3 & initMode)) + 1;
                    if (i2 == 1) {
                        ds = 2;
                    } else if (i2 == 2) {
                        ds = 1;
                    } else if (i2 == 3) {
                        ds = 3;
                    } else if (i2 != 4) {
                        ds = 5;
                    } else {
                        ds = 4;
                    }
                }
                protoOutputStream.write(1159641169923L, ds);
                i2 = 0;
                if ((modMode & 4) == 0) {
                    if ((4 & initMode) == 0) {
                        i4 = 2;
                    }
                    i2 = i4;
                }
                protoOutputStream.write(1159641169924L, i2);
                int im = 0;
                if ((modMode & 8) == 0) {
                    if ((8 & initMode) == 0) {
                        i3 = 3;
                    }
                    im = i3;
                }
                protoOutputStream.write(1159641169925L, im);
                protoOutputStream.end(token);
            }
        }
    }

    private void dumpHistoryLocked(PrintWriter pw, int flags, long histStart, boolean checkin) {
        PrintWriter printWriter = pw;
        HistoryPrinter hprinter = new HistoryPrinter();
        HistoryItem rec = new HistoryItem();
        long lastTime = -1;
        long baseTime = -1;
        boolean printed = false;
        HistoryEventTracker tracker = null;
        while (getNextHistoryLocked(rec)) {
            long baseTime2;
            long lastTime2 = rec.time;
            if (baseTime < 0) {
                baseTime2 = lastTime2;
            } else {
                baseTime2 = baseTime;
            }
            if (rec.time >= histStart) {
                boolean printed2;
                if (histStart < 0 || printed) {
                    printed2 = printed;
                } else {
                    boolean oldEventTag;
                    if (rec.cmd == (byte) 5 || rec.cmd == (byte) 7 || rec.cmd == (byte) 4 || rec.cmd == (byte) 8) {
                        printed2 = true;
                        oldEventTag = false;
                        hprinter.printNextItem(pw, rec, baseTime2, checkin, (flags & 32) != 0);
                        rec.cmd = oldEventTag;
                    } else if (rec.currentTime != 0) {
                        printed2 = true;
                        byte cmd = rec.cmd;
                        rec.cmd = (byte) 5;
                        hprinter.printNextItem(pw, rec, baseTime2, checkin, (flags & 32) != 0);
                        rec.cmd = cmd;
                        oldEventTag = false;
                    } else {
                        printed2 = printed;
                        oldEventTag = false;
                    }
                    if (tracker != null) {
                        int i;
                        HistoryTag oldEventTag2;
                        if (rec.cmd != (byte) 0) {
                            hprinter.printNextItem(pw, rec, baseTime2, checkin, (flags & 32) != 0 ? true : oldEventTag);
                            rec.cmd = oldEventTag;
                        }
                        int oldEventCode = rec.eventCode;
                        HistoryTag oldEventTag3 = rec.eventTag;
                        rec.eventTag = new HistoryTag();
                        int i2 = 0;
                        while (i2 < 22) {
                            HashMap<String, SparseIntArray> active = tracker.getStateForEvent(i2);
                            if (active != null) {
                                for (Entry<String, SparseIntArray> ent : active.entrySet()) {
                                    boolean j;
                                    SparseIntArray uids = (SparseIntArray) ent.getValue();
                                    printed = false;
                                    while (printed < uids.size()) {
                                        rec.eventCode = i2;
                                        rec.eventTag.string = (String) ent.getKey();
                                        rec.eventTag.uid = uids.keyAt(printed);
                                        rec.eventTag.poolIdx = uids.valueAt(printed);
                                        boolean z = (flags & 32) != 0 ? true : oldEventTag;
                                        SparseIntArray uids2 = uids;
                                        j = printed;
                                        i = i2;
                                        oldEventTag2 = oldEventTag3;
                                        hprinter.printNextItem(pw, rec, baseTime2, checkin, z);
                                        rec.wakeReasonTag = null;
                                        rec.wakelockTag = null;
                                        printed = j + 1;
                                        oldEventTag3 = oldEventTag2;
                                        uids = uids2;
                                        i2 = i;
                                        oldEventTag = false;
                                    }
                                    j = printed;
                                    i = i2;
                                    oldEventTag2 = oldEventTag3;
                                    oldEventTag = false;
                                }
                            }
                            i2++;
                            oldEventTag3 = oldEventTag3;
                            oldEventTag = false;
                        }
                        i = i2;
                        oldEventTag2 = oldEventTag3;
                        rec.eventCode = oldEventCode;
                        rec.eventTag = oldEventTag2;
                        tracker = null;
                    }
                }
                hprinter.printNextItem(pw, rec, baseTime2, checkin, (flags & 32) != 0);
                printed = printed2;
                lastTime = lastTime2;
                baseTime = baseTime2;
            } else {
                lastTime = lastTime2;
                baseTime = baseTime2;
            }
        }
        if (histStart >= 0) {
            commitCurrentHistoryBatchLocked();
            printWriter.print(checkin ? "NEXT: " : "  NEXT: ");
            printWriter.println(1 + lastTime);
        }
    }

    private void dumpDailyLevelStepSummary(PrintWriter pw, String prefix, String label, LevelStepTracker steps, StringBuilder tmpSb, int[] tmpOutInt) {
        PrintWriter printWriter = pw;
        String str = label;
        StringBuilder stringBuilder = tmpSb;
        if (steps != null) {
            long timeRemaining = steps.computeTimeEstimate(0, 0, tmpOutInt);
            String str2 = " steps)";
            String str3 = " (from ";
            if (timeRemaining >= 0) {
                pw.print(prefix);
                printWriter.print(str);
                printWriter.print(" total time: ");
                stringBuilder.setLength(0);
                formatTimeMs(stringBuilder, timeRemaining);
                printWriter.print(stringBuilder);
                printWriter.print(str3);
                printWriter.print(tmpOutInt[0]);
                printWriter.println(str2);
            }
            int i = 0;
            while (true) {
                int[] iArr = STEP_LEVEL_MODES_OF_INTEREST;
                if (i < iArr.length) {
                    int i2 = i;
                    long estimatedTime = steps.computeTimeEstimate((long) iArr[i], (long) STEP_LEVEL_MODE_VALUES[i], tmpOutInt);
                    if (estimatedTime > 0) {
                        pw.print(prefix);
                        printWriter.print(str);
                        printWriter.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        printWriter.print(STEP_LEVEL_MODE_LABELS[i2]);
                        printWriter.print(" time: ");
                        stringBuilder.setLength(0);
                        formatTimeMs(stringBuilder, estimatedTime);
                        printWriter.print(stringBuilder);
                        printWriter.print(str3);
                        printWriter.print(tmpOutInt[0]);
                        printWriter.println(str2);
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    private void dumpDailyPackageChanges(PrintWriter pw, String prefix, ArrayList<PackageChange> changes) {
        if (changes != null) {
            pw.print(prefix);
            pw.println("Package changes:");
            for (int i = 0; i < changes.size(); i++) {
                PackageChange pc = (PackageChange) changes.get(i);
                if (pc.mUpdate) {
                    pw.print(prefix);
                    pw.print("  Update ");
                    pw.print(pc.mPackageName);
                    pw.print(" vers=");
                    pw.println(pc.mVersionCode);
                } else {
                    pw.print(prefix);
                    pw.print("  Uninstall ");
                    pw.println(pc.mPackageName);
                }
            }
        }
    }

    public void dumpLocked(Context context, PrintWriter pw, int flags, int reqUid, long histStart) {
        long historyTotalSize;
        Throwable th;
        PrintWriter printWriter = pw;
        prepareForDumpLocked();
        boolean filtering = (flags & 14) != 0;
        if (!((flags & 8) == 0 && filtering)) {
            historyTotalSize = (long) getHistoryTotalSize();
            long historyUsedSize = (long) getHistoryUsedSize();
            if (startIteratingHistoryLocked()) {
                try {
                    printWriter.print("Battery History (");
                    printWriter.print((100 * historyUsedSize) / historyTotalSize);
                    printWriter.print("% used, ");
                    printSizeValue(printWriter, historyUsedSize);
                    printWriter.print(" used of ");
                    printSizeValue(printWriter, historyTotalSize);
                    printWriter.print(", ");
                    printWriter.print(getHistoryStringPoolSize());
                    printWriter.print(" strings using ");
                    printSizeValue(printWriter, (long) getHistoryStringPoolBytes());
                    printWriter.println("):");
                    try {
                        dumpHistoryLocked(pw, flags, histStart, false);
                        pw.println();
                        finishIteratingHistoryLocked();
                    } catch (Throwable th2) {
                        th = th2;
                        finishIteratingHistoryLocked();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    long j = historyUsedSize;
                    finishIteratingHistoryLocked();
                    throw th;
                }
            }
            if (startIteratingOldHistoryLocked()) {
                try {
                    HistoryItem rec = new HistoryItem();
                    printWriter.println("Old battery History:");
                    HistoryPrinter hprinter = new HistoryPrinter();
                    long baseTime = -1;
                    while (getNextOldHistoryLocked(rec)) {
                        long baseTime2;
                        if (baseTime < 0) {
                            baseTime2 = rec.time;
                        } else {
                            baseTime2 = baseTime;
                        }
                        hprinter.printNextItem(pw, rec, baseTime2, false, (flags & 32) != 0);
                        baseTime = baseTime2;
                    }
                    pw.println();
                } finally {
                    finishIteratingOldHistoryLocked();
                }
            }
        }
        if (!filtering || (flags & 6) != 0) {
            boolean z;
            String str;
            boolean z2;
            boolean z3;
            if (!filtering) {
                SparseArray<? extends Uid> uidStats = getUidStats();
                int NU = uidStats.size();
                boolean didPid = false;
                long nowRealtime = SystemClock.elapsedRealtime();
                for (int i = 0; i < NU; i++) {
                    SparseArray<? extends Pid> pids = ((Uid) uidStats.valueAt(i)).getPidStats();
                    if (pids != null) {
                        for (int j2 = 0; j2 < pids.size(); j2++) {
                            Pid pid = (Pid) pids.valueAt(j2);
                            if (!didPid) {
                                printWriter.println("Per-PID Stats:");
                                didPid = true;
                            }
                            long time = pid.mWakeSumMs + (pid.mWakeNesting > 0 ? nowRealtime - pid.mWakeStartMs : 0);
                            printWriter.print("  PID ");
                            printWriter.print(pids.keyAt(j2));
                            printWriter.print(" wake time: ");
                            TimeUtils.formatDuration(time, printWriter);
                            printWriter.println("");
                        }
                    }
                }
                if (didPid) {
                    pw.println();
                }
            }
            if (filtering && (flags & 2) == 0) {
                z = false;
            } else {
                str = "  ";
                if (dumpDurationSteps(printWriter, str, "Discharge step durations:", getDischargeLevelStepTracker(), false)) {
                    historyTotalSize = computeBatteryTimeRemaining(SystemClock.elapsedRealtime() * 1000);
                    if (historyTotalSize >= 0) {
                        printWriter.print("  Estimated discharge time remaining: ");
                        TimeUtils.formatDuration(historyTotalSize / 1000, printWriter);
                        pw.println();
                    }
                    LevelStepTracker steps = getDischargeLevelStepTracker();
                    int i2 = 0;
                    while (true) {
                        int[] iArr = STEP_LEVEL_MODES_OF_INTEREST;
                        if (i2 >= iArr.length) {
                            break;
                        }
                        dumpTimeEstimate(pw, "  Estimated ", STEP_LEVEL_MODE_LABELS[i2], " time: ", steps.computeTimeEstimate((long) iArr[i2], (long) STEP_LEVEL_MODE_VALUES[i2], null));
                        i2++;
                    }
                    pw.println();
                }
                z = false;
                if (dumpDurationSteps(printWriter, str, "Charge step durations:", getChargeLevelStepTracker(), false)) {
                    long timeRemaining = computeChargeTimeRemaining(SystemClock.elapsedRealtime() * 1000);
                    if (timeRemaining >= 0) {
                        printWriter.print("  Estimated charge time remaining: ");
                        TimeUtils.formatDuration(timeRemaining / 1000, printWriter);
                        pw.println();
                    }
                    pw.println();
                }
            }
            if (filtering && (flags & 4) == 0) {
                z2 = z;
                z3 = true;
            } else {
                String str2;
                LevelStepTracker dsteps;
                int[] outInt;
                boolean dit;
                CharSequence charSequence;
                DailyItem dit2;
                DailyItem dit3;
                printWriter.println("Daily stats:");
                printWriter.print("  Current start time: ");
                CharSequence charSequence2 = "yyyy-MM-dd-HH-mm-ss";
                printWriter.println(DateFormat.format(charSequence2, getCurrentDailyStartTime()).toString());
                printWriter.print("  Next min deadline: ");
                printWriter.println(DateFormat.format(charSequence2, getNextMinDailyDeadline()).toString());
                printWriter.print("  Next max deadline: ");
                printWriter.println(DateFormat.format(charSequence2, getNextMaxDailyDeadline()).toString());
                StringBuilder sb = new StringBuilder(64);
                int[] outInt2 = new int[1];
                LevelStepTracker dsteps2 = getDailyDischargeLevelStepTracker();
                LevelStepTracker csteps = getDailyChargeLevelStepTracker();
                ArrayList<PackageChange> pkgc = getDailyPackageChanges();
                str = "    ";
                ArrayList<PackageChange> pkgc2;
                LevelStepTracker csteps2;
                if (dsteps2.mNumStepDurations > 0 || csteps.mNumStepDurations > 0 || pkgc != null) {
                    if ((flags & 4) != 0) {
                        z3 = true;
                        str2 = str;
                        pkgc2 = pkgc;
                        csteps2 = csteps;
                        dsteps = dsteps2;
                        outInt = outInt2;
                        dit = z;
                        charSequence = charSequence2;
                    } else if (filtering) {
                        printWriter.println("  Current daily steps:");
                        str2 = str;
                        dumpDailyLevelStepSummary(pw, "    ", "Discharge", dsteps2, sb, outInt2);
                        dsteps = dsteps2;
                        outInt = outInt2;
                        dit = z;
                        charSequence = charSequence2;
                        z3 = true;
                        dumpDailyLevelStepSummary(pw, "    ", "Charge", csteps, sb, outInt);
                    } else {
                        z3 = true;
                        str2 = str;
                        pkgc2 = pkgc;
                        csteps2 = csteps;
                        dsteps = dsteps2;
                        outInt = outInt2;
                        dit = z;
                        charSequence = charSequence2;
                    }
                    if (dumpDurationSteps(printWriter, str2, "  Current daily discharge step durations:", dsteps, dit)) {
                        dumpDailyLevelStepSummary(pw, "      ", "Discharge", dsteps, sb, outInt);
                    }
                    if (dumpDurationSteps(printWriter, str2, "  Current daily charge step durations:", csteps2, dit)) {
                        dumpDailyLevelStepSummary(pw, "      ", "Charge", csteps2, sb, outInt);
                    }
                    dumpDailyPackageChanges(printWriter, str2, pkgc2);
                } else {
                    z3 = true;
                    str2 = str;
                    pkgc2 = pkgc;
                    csteps2 = csteps;
                    dsteps = dsteps2;
                    outInt = outInt2;
                    dit = z;
                    charSequence = charSequence2;
                }
                int curIndex = 0;
                while (true) {
                    DailyItem dailyItemLocked = getDailyItemLocked(curIndex);
                    dit2 = dailyItemLocked;
                    if (dailyItemLocked == null) {
                        break;
                    }
                    CharSequence charSequence3;
                    LevelStepTracker dsteps3;
                    String dsteps4;
                    int curIndex2 = curIndex + 1;
                    if ((flags & 4) != 0) {
                        pw.println();
                    }
                    printWriter.print("  Daily from ");
                    CharSequence charSequence4 = charSequence;
                    printWriter.print(DateFormat.format(charSequence4, dit2.mStartTime).toString());
                    printWriter.print(" to ");
                    printWriter.print(DateFormat.format(charSequence4, dit2.mEndTime).toString());
                    printWriter.println(":");
                    if ((flags & 4) != 0) {
                        charSequence3 = charSequence4;
                        dit3 = dit2;
                    } else if (filtering) {
                        LevelStepTracker levelStepTracker = dit2.mDischargeSteps;
                        PrintWriter printWriter2 = pw;
                        charSequence3 = charSequence4;
                        StringBuilder stringBuilder = sb;
                        dit3 = dit2;
                        int[] iArr2 = outInt;
                        dumpDailyLevelStepSummary(printWriter2, "    ", "Discharge", levelStepTracker, stringBuilder, iArr2);
                        dumpDailyLevelStepSummary(printWriter2, "    ", "Charge", dit3.mChargeSteps, stringBuilder, iArr2);
                        dsteps3 = dsteps;
                        dsteps = null;
                        dit = dsteps;
                        curIndex = curIndex2;
                        charSequence = charSequence3;
                        dsteps = dsteps3;
                    } else {
                        charSequence3 = charSequence4;
                        dit3 = dit2;
                    }
                    String str3 = "      ";
                    if (dumpDurationSteps(printWriter, str3, "    Discharge step durations:", dit3.mDischargeSteps, false)) {
                        dsteps3 = dsteps;
                        dsteps4 = str3;
                        dumpDailyLevelStepSummary(pw, "        ", "Discharge", dit3.mDischargeSteps, sb, outInt);
                    } else {
                        dsteps3 = dsteps;
                        dsteps4 = str3;
                    }
                    if (dumpDurationSteps(printWriter, dsteps4, "    Charge step durations:", dit3.mChargeSteps, false)) {
                        dsteps = null;
                        dumpDailyLevelStepSummary(pw, "        ", "Charge", dit3.mChargeSteps, sb, outInt);
                    } else {
                        dsteps = null;
                    }
                    dumpDailyPackageChanges(printWriter, str2, dit3.mPackageChanges);
                    dit = dsteps;
                    curIndex = curIndex2;
                    charSequence = charSequence3;
                    dsteps = dsteps3;
                }
                z2 = dit;
                dit3 = dit2;
                pw.println();
            }
            if (!(filtering && (flags & 2) == 0)) {
                printWriter.println("Statistics since last charge:");
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("  System starts: ");
                stringBuilder2.append(getStartCount());
                stringBuilder2.append(", currently on battery: ");
                stringBuilder2.append(getIsOnBattery());
                printWriter.println(stringBuilder2.toString());
                dumpLocked(context, pw, "", 0, reqUid, (flags & 64) != 0 ? z3 : z2);
                pw.println();
            }
        }
    }

    public void dumpCheckinLocked(Context context, PrintWriter pw, List<ApplicationInfo> apps, int flags, long histStart) {
        int i;
        PrintWriter printWriter = pw;
        List<ApplicationInfo> list = apps;
        String str = "\"";
        prepareForDumpLocked();
        r1 = new Object[4];
        boolean z = true;
        r1[1] = Integer.valueOf(getParcelVersion());
        r1[2] = getStartPlatformVersion();
        r1[3] = getEndPlatformVersion();
        String str2 = "i";
        dumpLine(printWriter, 0, str2, VERSION_DATA, r1);
        long now = getHistoryBaseTime() + SystemClock.elapsedRealtime();
        if ((flags & 24) != 0 && startIteratingHistoryLocked()) {
            i = 0;
            while (i < getHistoryStringPoolSize()) {
                try {
                    printWriter.print(9);
                    printWriter.print(',');
                    printWriter.print(HISTORY_STRING_POOL);
                    printWriter.print(',');
                    printWriter.print(i);
                    printWriter.print(",");
                    printWriter.print(getHistoryTagPoolUid(i));
                    printWriter.print(",\"");
                    printWriter.print(getHistoryTagPoolString(i).replace("\\", "\\\\").replace(str, "\\\""));
                    printWriter.print(str);
                    pw.println();
                    i++;
                } finally {
                    finishIteratingHistoryLocked();
                }
            }
            dumpHistoryLocked(pw, flags, histStart, true);
        }
        if ((flags & 8) == 0) {
            if (list != null) {
                SparseArray<Pair<ArrayList<String>, MutableBoolean>> uids = new SparseArray();
                for (i = 0; i < apps.size(); i++) {
                    ApplicationInfo ai = (ApplicationInfo) list.get(i);
                    Pair<ArrayList<String>, MutableBoolean> pkgs = (Pair) uids.get(UserHandle.getAppId(ai.uid));
                    if (pkgs == null) {
                        pkgs = new Pair(new ArrayList(), new MutableBoolean(false));
                        uids.put(UserHandle.getAppId(ai.uid), pkgs);
                    }
                    ((ArrayList) pkgs.first).add(ai.packageName);
                }
                SparseArray<? extends Uid> uidStats = getUidStats();
                int NU = uidStats.size();
                String[] lineArgs = new String[2];
                int i2 = 0;
                while (i2 < NU) {
                    int uid = UserHandle.getAppId(uidStats.keyAt(i2));
                    Pair<ArrayList<String>, MutableBoolean> pkgs2 = (Pair) uids.get(uid);
                    if (!(pkgs2 == null || ((MutableBoolean) pkgs2.second).value)) {
                        ((MutableBoolean) pkgs2.second).value = z;
                        int j = 0;
                        while (j < ((ArrayList) pkgs2.first).size()) {
                            lineArgs[0] = Integer.toString(uid);
                            lineArgs[1] = (String) ((ArrayList) pkgs2.first).get(j);
                            SparseArray<Pair<ArrayList<String>, MutableBoolean>> uids2 = uids;
                            dumpLine(printWriter, 0, str2, "uid", (Object[]) lineArgs);
                            j++;
                            uids = uids2;
                        }
                    }
                    i2++;
                    uids = uids;
                    z = true;
                }
            }
            if ((flags & 4) == 0) {
                String str3 = "";
                dumpDurationSteps(printWriter, str3, DISCHARGE_STEP_DATA, getDischargeLevelStepTracker(), true);
                String[] lineArgs2 = new String[1];
                long timeRemaining = computeBatteryTimeRemaining(SystemClock.elapsedRealtime() * 1000);
                if (timeRemaining >= 0) {
                    lineArgs2[0] = Long.toString(timeRemaining);
                    dumpLine(printWriter, 0, str2, DISCHARGE_TIME_REMAIN_DATA, (Object[]) lineArgs2);
                }
                dumpDurationSteps(printWriter, str3, CHARGE_STEP_DATA, getChargeLevelStepTracker(), true);
                long timeRemaining2 = computeChargeTimeRemaining(1000 * SystemClock.elapsedRealtime());
                if (timeRemaining2 >= 0) {
                    lineArgs2[0] = Long.toString(timeRemaining2);
                    dumpLine(printWriter, 0, str2, CHARGE_TIME_REMAIN_DATA, (Object[]) lineArgs2);
                }
                dumpCheckinLocked(context, pw, 0, -1, (flags & 64) != 0);
            }
        }
    }

    public void dumpProtoLocked(Context context, FileDescriptor fd, List<ApplicationInfo> apps, int flags, long histStart) {
        ProtoOutputStream proto = new ProtoOutputStream(fd);
        prepareForDumpLocked();
        if ((flags & 24) != 0) {
            dumpProtoHistoryLocked(proto, flags, histStart);
            proto.flush();
            return;
        }
        long bToken = proto.start(1146756268033L);
        proto.write(1120986464257L, 34);
        proto.write(1112396529666L, getParcelVersion());
        proto.write(1138166333443L, getStartPlatformVersion());
        proto.write(1138166333444L, getEndPlatformVersion());
        if ((flags & 4) == 0) {
            BatteryStatsHelper helper = new BatteryStatsHelper(context, false, (flags & 64) != 0);
            helper.create(this);
            helper.refreshStats(0, -1);
            dumpProtoAppsLocked(proto, helper, apps);
            dumpProtoSystemLocked(proto, helper);
        }
        proto.end(bToken);
        proto.flush();
    }

    private void dumpProtoAppsLocked(ProtoOutputStream proto, BatteryStatsHelper helper, List<ApplicationInfo> apps) {
        int i;
        int aid;
        List<BatterySipper> sippers;
        ProtoOutputStream protoOutputStream = proto;
        List<ApplicationInfo> list = apps;
        int which = 0;
        long rawUptimeUs = SystemClock.uptimeMillis() * 1000;
        long rawRealtimeMs = SystemClock.elapsedRealtime();
        long rawRealtimeUs = rawRealtimeMs * 1000;
        long batteryUptimeUs = getBatteryUptime(rawUptimeUs);
        SparseArray<ArrayList<String>> aidToPackages = new SparseArray();
        if (list != null) {
            for (i = 0; i < apps.size(); i++) {
                ArrayList<String> pkgs;
                ApplicationInfo ai = (ApplicationInfo) list.get(i);
                aid = UserHandle.getAppId(ai.uid);
                ArrayList<String> pkgs2 = (ArrayList) aidToPackages.get(aid);
                if (pkgs2 == null) {
                    pkgs = new ArrayList();
                    aidToPackages.put(aid, pkgs);
                } else {
                    pkgs = pkgs2;
                }
                pkgs.add(ai.packageName);
            }
        }
        SparseArray<BatterySipper> uidToSipper = new SparseArray();
        List<BatterySipper> sippers2 = helper.getUsageList();
        if (sippers2 != null) {
            i = 0;
            while (i < sippers2.size()) {
                BatterySipper bs = (BatterySipper) sippers2.get(i);
                sippers = sippers2;
                if (bs.drainType == DrainType.APP) {
                    uidToSipper.put(bs.uidObj.getUid(), bs);
                }
                i++;
                list = apps;
                sippers2 = sippers;
            }
            sippers = sippers2;
        } else {
            sippers = sippers2;
        }
        SparseArray<? extends Uid> uidStats = getUidStats();
        int n = uidStats.size();
        aid = 0;
        while (aid < n) {
            ArrayList<String> pkgs3;
            SparseArray<? extends Uid> uidStats2;
            int which2;
            ArrayMap<String, ? extends Pkg> packageStats;
            int ipkg;
            SparseArray<ArrayList<String>> aidToPackages2;
            long batteryUptimeUs2;
            long rawUptimeUs2;
            long rawRealtimeMs2;
            long rawRealtimeUs2;
            int isvc;
            long startTimeMs;
            int starts;
            long pToken;
            ProtoOutputStream protoOutputStream2;
            long bmToken;
            long[] cpuFreqTimeMs;
            Timer bleTimer;
            long cToken;
            SparseArray<BatterySipper> uidToSipper2;
            Uid u;
            long[] cpuFreqs;
            Timer bleTimer2;
            long jcToken;
            int r;
            ArrayMap<String, SparseIntArray> completions;
            int uid;
            long seToken;
            Timer bgTimer;
            int isy;
            int n2 = n;
            SparseArray<BatterySipper> uidToSipper3 = uidToSipper;
            Uid u2 = (Uid) uidStats.valueAt(aid);
            long uTkn = protoOutputStream.start(2246267895813L);
            n = uidStats.keyAt(aid);
            int iu = aid;
            SparseArray<ArrayList<String>> aidToPackages3 = aidToPackages;
            protoOutputStream.write(1120986464257L, n);
            aidToPackages = aidToPackages3;
            ArrayList<String> pkgs4 = (ArrayList) aidToPackages.get(UserHandle.getAppId(n));
            if (pkgs4 == null) {
                pkgs3 = new ArrayList();
            } else {
                pkgs3 = pkgs4;
            }
            ArrayMap<String, ? extends Pkg> packageStats2 = u2.getPackageStats();
            int uid2 = n;
            n = packageStats2.size() - 1;
            while (true) {
                uidStats2 = uidStats;
                which2 = which;
                if (n < 0) {
                    break;
                }
                String pkg = (String) packageStats2.keyAt(n);
                ArrayMap<String, ? extends Serv> serviceStats = ((Pkg) packageStats2.valueAt(n)).getServiceStats();
                if (serviceStats.size() == 0) {
                    packageStats = packageStats2;
                    ipkg = n;
                    aidToPackages2 = aidToPackages;
                    batteryUptimeUs2 = batteryUptimeUs;
                    rawUptimeUs2 = rawUptimeUs;
                    rawRealtimeMs2 = rawRealtimeMs;
                    rawRealtimeUs2 = rawRealtimeUs;
                } else {
                    String pkg2;
                    rawUptimeUs2 = rawUptimeUs;
                    rawRealtimeUs2 = rawRealtimeUs;
                    rawRealtimeUs = protoOutputStream.start(2246267895810L);
                    protoOutputStream.write(1138166333441L, pkg);
                    pkgs3.remove(pkg);
                    isvc = serviceStats.size() - 1;
                    while (isvc >= 0) {
                        Serv ss = (Serv) serviceStats.valueAt(isvc);
                        packageStats = packageStats2;
                        batteryUptimeUs2 = batteryUptimeUs;
                        SparseArray<ArrayList<String>> aidToPackages4 = aidToPackages;
                        startTimeMs = roundUsToMs(ss.getStartTime(batteryUptimeUs, 0));
                        aidToPackages2 = aidToPackages4;
                        starts = ss.getStarts(0);
                        pkg2 = pkg;
                        which = ss.getLaunches(0);
                        if (startTimeMs == 0 && starts == 0 && which == 0) {
                            ipkg = n;
                            rawRealtimeMs2 = rawRealtimeMs;
                        } else {
                            rawRealtimeMs2 = rawRealtimeMs;
                            int ipkg2 = n;
                            packageStats2 = protoOutputStream.start(2);
                            ipkg = ipkg2;
                            protoOutputStream.write(1138166333441L, (String) serviceStats.keyAt(isvc));
                            protoOutputStream.write(1112396529666L, startTimeMs);
                            protoOutputStream.write(1120986464259L, starts);
                            protoOutputStream.write(1120986464260L, which);
                            protoOutputStream.end(packageStats2);
                        }
                        isvc--;
                        packageStats2 = packageStats;
                        aidToPackages = aidToPackages2;
                        pkg = pkg2;
                        batteryUptimeUs = batteryUptimeUs2;
                        rawRealtimeMs = rawRealtimeMs2;
                        n = ipkg;
                    }
                    packageStats = packageStats2;
                    ipkg = n;
                    aidToPackages2 = aidToPackages;
                    batteryUptimeUs2 = batteryUptimeUs;
                    pkg2 = pkg;
                    rawRealtimeMs2 = rawRealtimeMs;
                    protoOutputStream.end(rawRealtimeUs);
                }
                n = ipkg - 1;
                which = which2;
                packageStats2 = packageStats;
                uidStats = uidStats2;
                rawUptimeUs = rawUptimeUs2;
                rawRealtimeUs = rawRealtimeUs2;
                aidToPackages = aidToPackages2;
                batteryUptimeUs = batteryUptimeUs2;
                rawRealtimeMs = rawRealtimeMs2;
            }
            packageStats = packageStats2;
            ipkg = n;
            aidToPackages2 = aidToPackages;
            batteryUptimeUs2 = batteryUptimeUs;
            rawUptimeUs2 = rawUptimeUs;
            rawRealtimeMs2 = rawRealtimeMs;
            rawRealtimeUs2 = rawRealtimeUs;
            Iterator it = pkgs3.iterator();
            while (it.hasNext()) {
                String p = (String) it.next();
                pToken = protoOutputStream.start(2246267895810L);
                protoOutputStream.write(1138166333441L, p);
                protoOutputStream.end(pToken);
            }
            if (u2.getAggregatedPartialWakelockTimer() != null) {
                Timer timer = u2.getAggregatedPartialWakelockTimer();
                rawUptimeUs = rawRealtimeMs2;
                startTimeMs = timer.getTotalDurationMsLocked(rawUptimeUs);
                Timer bgTimer2 = timer.getSubTimer();
                rawRealtimeMs = bgTimer2 != null ? bgTimer2.getTotalDurationMsLocked(rawUptimeUs) : 0;
                rawRealtimeUs = protoOutputStream.start(1146756268056L);
                protoOutputStream.write(1112396529665L, startTimeMs);
                protoOutputStream.write(1112396529666L, rawRealtimeMs);
                protoOutputStream.end(rawRealtimeUs);
            } else {
                rawUptimeUs = rawRealtimeMs2;
            }
            rawRealtimeMs = uTkn;
            ArrayMap<String, ? extends Pkg> packageStats3 = packageStats;
            List<BatterySipper> sippers3 = sippers;
            int n3 = n2;
            int iu2 = iu;
            int uid3 = uid2;
            SparseArray<ArrayList<String>> aidToPackages5 = aidToPackages2;
            long batteryUptimeUs3 = batteryUptimeUs2;
            long rawRealtimeMs3 = rawUptimeUs;
            SparseArray<BatterySipper> uidToSipper4 = uidToSipper3;
            Uid u3 = u2;
            dumpTimer(proto, 1146756268040L, u2.getAudioTurnedOnTimer(), rawRealtimeUs2, 0);
            dumpControllerActivityProto(protoOutputStream, 1146756268035L, u3.getBluetoothControllerActivity(), 0);
            Timer bleTimer3 = u3.getBluetoothScanTimer();
            if (bleTimer3 != null) {
                protoOutputStream2 = proto;
                bmToken = protoOutputStream.start(1146756268038L);
                batteryUptimeUs = rawRealtimeUs2;
                dumpTimer(protoOutputStream2, 1146756268033L, bleTimer3, batteryUptimeUs, 0);
                dumpTimer(protoOutputStream2, 1146756268034L, u3.getBluetoothScanBackgroundTimer(), batteryUptimeUs, 0);
                dumpTimer(protoOutputStream2, 1146756268035L, u3.getBluetoothUnoptimizedScanTimer(), batteryUptimeUs, 0);
                dumpTimer(protoOutputStream2, 1146756268036L, u3.getBluetoothUnoptimizedScanBackgroundTimer(), batteryUptimeUs, 0);
                protoOutputStream.write(1120986464261L, u3.getBluetoothScanResultCounter() != null ? u3.getBluetoothScanResultCounter().getCountLocked(0) : 0);
                protoOutputStream.write(1120986464262L, u3.getBluetoothScanResultBgCounter() != null ? u3.getBluetoothScanResultBgCounter().getCountLocked(0) : 0);
                protoOutputStream.end(bmToken);
            }
            dumpTimer(proto, 1146756268041L, u3.getCameraTurnedOnTimer(), rawRealtimeUs2, 0);
            batteryUptimeUs = protoOutputStream.start(1146756268039L);
            List<BatterySipper> sippers4 = sippers3;
            uid2 = n3;
            protoOutputStream.write((long) 1, roundUsToMs(u3.getUserCpuTimeUs(0)));
            protoOutputStream.write(1112396529666L, roundUsToMs(u3.getSystemCpuTimeUs(0)));
            long[] cpuFreqs2 = getCpuFreqs();
            if (cpuFreqs2 != null) {
                cpuFreqTimeMs = u3.getCpuFreqTimes(0);
                if (cpuFreqTimeMs == null || cpuFreqTimeMs.length != cpuFreqs2.length) {
                    bleTimer = bleTimer3;
                    packageStats = packageStats3;
                    batteryUptimeUs2 = rawRealtimeMs;
                } else {
                    long[] screenOffCpuFreqTimeMs = u3.getScreenOffCpuFreqTimes(0);
                    if (screenOffCpuFreqTimeMs == null) {
                        screenOffCpuFreqTimeMs = new long[cpuFreqTimeMs.length];
                    }
                    i = 0;
                    while (i < cpuFreqTimeMs.length) {
                        cToken = protoOutputStream.start(2246267895811L);
                        batteryUptimeUs2 = rawRealtimeMs;
                        protoOutputStream.write(1120986464257L, i + 1);
                        bleTimer = bleTimer3;
                        packageStats = packageStats3;
                        protoOutputStream.write((long) 2, cpuFreqTimeMs[i]);
                        protoOutputStream.write(1112396529667L, screenOffCpuFreqTimeMs[i]);
                        protoOutputStream.end(cToken);
                        i++;
                        bleTimer3 = bleTimer;
                        packageStats3 = packageStats;
                        rawRealtimeMs = batteryUptimeUs2;
                    }
                    bleTimer = bleTimer3;
                    packageStats = packageStats3;
                    batteryUptimeUs2 = rawRealtimeMs;
                }
            } else {
                bleTimer = bleTimer3;
                packageStats = packageStats3;
                batteryUptimeUs2 = rawRealtimeMs;
            }
            i = 0;
            while (true) {
                pToken = 1159641169921L;
                if (i >= 7) {
                    break;
                }
                long[] timesMs = u3.getCpuFreqTimes(0, i);
                if (timesMs == null || timesMs.length != cpuFreqs2.length) {
                    uidToSipper2 = uidToSipper4;
                    u = u3;
                    cpuFreqs = cpuFreqs2;
                    bleTimer2 = bleTimer;
                } else {
                    cpuFreqTimeMs = u3.getScreenOffCpuFreqTimes(0, i);
                    if (cpuFreqTimeMs == null) {
                        cpuFreqTimeMs = new long[timesMs.length];
                    }
                    rawRealtimeMs = protoOutputStream.start(2246267895812L);
                    protoOutputStream.write(1159641169921L, i);
                    n = 0;
                    while (n < timesMs.length) {
                        cpuFreqs = cpuFreqs2;
                        bleTimer2 = bleTimer;
                        rawRealtimeUs = protoOutputStream.start(2246267895810L);
                        protoOutputStream.write(1120986464257L, n + 1);
                        uidToSipper2 = uidToSipper4;
                        u = u3;
                        protoOutputStream.write((long) 2, timesMs[n]);
                        protoOutputStream.write(1112396529667L, cpuFreqTimeMs[n]);
                        protoOutputStream.end(rawRealtimeUs);
                        n++;
                        cpuFreqs2 = cpuFreqs;
                        bleTimer = bleTimer2;
                        uidToSipper4 = uidToSipper2;
                        u3 = u;
                    }
                    uidToSipper2 = uidToSipper4;
                    u = u3;
                    cpuFreqs = cpuFreqs2;
                    bleTimer2 = bleTimer;
                    protoOutputStream.end(rawRealtimeMs);
                }
                i++;
                cpuFreqs2 = cpuFreqs;
                bleTimer = bleTimer2;
                uidToSipper4 = uidToSipper2;
                u3 = u;
            }
            uidToSipper2 = uidToSipper4;
            u = u3;
            cpuFreqs = cpuFreqs2;
            bleTimer2 = bleTimer;
            protoOutputStream.end(batteryUptimeUs);
            protoOutputStream2 = proto;
            long cpuToken = batteryUptimeUs;
            batteryUptimeUs = rawRealtimeUs2;
            dumpTimer(protoOutputStream2, 1146756268042L, u.getFlashlightTurnedOnTimer(), batteryUptimeUs, 0);
            dumpTimer(protoOutputStream2, 1146756268043L, u.getForegroundActivityTimer(), batteryUptimeUs, 0);
            dumpTimer(protoOutputStream2, 1146756268044L, u.getForegroundServiceTimer(), batteryUptimeUs, 0);
            ArrayMap<String, SparseIntArray> completions2 = u.getJobCompletionStats();
            int[] reasons = new int[]{0, 1, 2, 3, 4};
            i = 0;
            while (i < completions2.size()) {
                long cpuToken2;
                SparseIntArray types = (SparseIntArray) completions2.valueAt(i);
                if (types != null) {
                    jcToken = protoOutputStream.start(2246267895824L);
                    protoOutputStream.write(1138166333441L, (String) completions2.keyAt(i));
                    isvc = reasons.length;
                    int i2 = 0;
                    while (i2 < isvc) {
                        r = reasons[i2];
                        cpuToken2 = cpuToken;
                        cpuToken = protoOutputStream.start(2246267895810L);
                        protoOutputStream.write(pToken, r);
                        protoOutputStream.write(1120986464258L, types.get(r, 0));
                        protoOutputStream.end(cpuToken);
                        i2++;
                        cpuToken = cpuToken2;
                        pToken = 1159641169921L;
                    }
                    cpuToken2 = cpuToken;
                    protoOutputStream.end(jcToken);
                } else {
                    cpuToken2 = cpuToken;
                }
                i++;
                cpuToken = cpuToken2;
                pToken = 1159641169921L;
            }
            ArrayMap<String, ? extends Timer> jobs = u.getJobStats();
            r = jobs.size() - 1;
            while (r >= 0) {
                Timer timer2 = (Timer) jobs.valueAt(r);
                Timer bgTimer3 = timer2.getSubTimer();
                batteryUptimeUs = protoOutputStream.start(2246267895823L);
                protoOutputStream.write(1138166333441L, (String) jobs.keyAt(r));
                protoOutputStream2 = proto;
                pToken = batteryUptimeUs;
                batteryUptimeUs = rawRealtimeUs2;
                int[] reasons2 = reasons;
                dumpTimer(protoOutputStream2, 1146756268034L, timer2, batteryUptimeUs, 0);
                dumpTimer(protoOutputStream2, 1146756268035L, bgTimer3, batteryUptimeUs, 0);
                protoOutputStream.end(pToken);
                r--;
                reasons = reasons2;
            }
            dumpControllerActivityProto(protoOutputStream, 1146756268036L, u.getModemControllerActivity(), 0);
            pToken = protoOutputStream.start(1146756268049L);
            Uid u4 = u;
            protoOutputStream.write(1112396529665L, u4.getNetworkActivityBytes(0, 0));
            protoOutputStream.write(1112396529666L, u4.getNetworkActivityBytes(1, 0));
            protoOutputStream.write(1112396529667L, u4.getNetworkActivityBytes(2, 0));
            protoOutputStream.write(1112396529668L, u4.getNetworkActivityBytes(3, 0));
            protoOutputStream.write(1112396529669L, u4.getNetworkActivityBytes(4, 0));
            protoOutputStream.write(1112396529670L, u4.getNetworkActivityBytes(5, 0));
            protoOutputStream.write(1112396529671L, u4.getNetworkActivityPackets(0, 0));
            protoOutputStream.write(1112396529672L, u4.getNetworkActivityPackets(1, 0));
            protoOutputStream.write(1112396529673L, u4.getNetworkActivityPackets(2, 0));
            protoOutputStream.write(1112396529674L, u4.getNetworkActivityPackets(3, 0));
            protoOutputStream.write(1112396529675L, roundUsToMs(u4.getMobileRadioActiveTime(0)));
            protoOutputStream.write(1120986464268L, u4.getMobileRadioActiveCount(0));
            protoOutputStream.write(1120986464269L, u4.getMobileRadioApWakeupCount(0));
            protoOutputStream.write(1120986464270L, u4.getWifiRadioApWakeupCount(0));
            protoOutputStream.write(1112396529679L, u4.getNetworkActivityBytes(6, 0));
            protoOutputStream.write(1112396529680L, u4.getNetworkActivityBytes(7, 0));
            protoOutputStream.write(1112396529681L, u4.getNetworkActivityBytes(8, 0));
            protoOutputStream.write(1112396529682L, u4.getNetworkActivityBytes(9, 0));
            protoOutputStream.write(1112396529683L, u4.getNetworkActivityPackets(6, 0));
            protoOutputStream.write(1112396529684L, u4.getNetworkActivityPackets(7, 0));
            protoOutputStream.write(1112396529685L, u4.getNetworkActivityPackets(8, 0));
            protoOutputStream.write(1112396529686L, u4.getNetworkActivityPackets(9, 0));
            protoOutputStream.end(pToken);
            SparseArray<BatterySipper> uidToSipper5 = uidToSipper2;
            int uid4 = uid3;
            BatterySipper bs2 = (BatterySipper) uidToSipper5.get(uid4);
            if (bs2 != null) {
                long bsToken = protoOutputStream.start(1146756268050L);
                uidToSipper2 = uidToSipper5;
                protoOutputStream.write(1103806595073L, bs2.totalPowerMah);
                protoOutputStream.write(1133871366146L, bs2.shouldHide);
                protoOutputStream.write(1103806595075L, bs2.screenPowerMah);
                protoOutputStream.write(1103806595076L, bs2.proportionalSmearMah);
                protoOutputStream.end(bsToken);
            } else {
                uidToSipper2 = uidToSipper5;
            }
            ArrayMap<String, ? extends Proc> processStats = u4.getProcessStats();
            i = processStats.size() - 1;
            while (i >= 0) {
                Proc ps = (Proc) processStats.valueAt(i);
                jcToken = protoOutputStream.start(2246267895827L);
                u = u4;
                protoOutputStream.write(1138166333441L, (String) processStats.keyAt(i));
                completions = completions2;
                protoOutputStream.write(1112396529666L, ps.getUserTime(0));
                protoOutputStream.write(1112396529667L, ps.getSystemTime(0));
                protoOutputStream.write(1112396529668L, ps.getForegroundTime(0));
                protoOutputStream.write(1120986464261L, ps.getStarts(0));
                protoOutputStream.write(1120986464262L, ps.getNumAnrs(0));
                protoOutputStream.write(1120986464263L, ps.getNumCrashes(0));
                protoOutputStream.end(jcToken);
                i--;
                completions2 = completions;
                u4 = u;
            }
            u = u4;
            completions = completions2;
            SparseArray<? extends Sensor> sensors = u.getSensorStats();
            r = 0;
            while (r < sensors.size()) {
                BatterySipper bs3;
                Sensor se = (Sensor) sensors.valueAt(r);
                bleTimer = se.getSensorTime();
                if (bleTimer == null) {
                    bs3 = bs2;
                    uid = uid4;
                } else {
                    Timer bgTimer4 = se.getSensorBackgroundTime();
                    starts = sensors.keyAt(r);
                    seToken = protoOutputStream.start(2246267895829L);
                    protoOutputStream.write(1120986464257L, starts);
                    protoOutputStream2 = proto;
                    long seToken2 = seToken;
                    seToken = 1120986464257L;
                    bs3 = bs2;
                    batteryUptimeUs = rawRealtimeUs2;
                    uid = uid4;
                    dumpTimer(protoOutputStream2, 1146756268034L, bleTimer, batteryUptimeUs, 0);
                    dumpTimer(protoOutputStream2, 1146756268035L, bgTimer4, batteryUptimeUs, 0);
                    protoOutputStream.end(seToken2);
                }
                r++;
                bs2 = bs3;
                uid4 = uid;
            }
            uid = uid4;
            i = 0;
            while (i < 7) {
                batteryUptimeUs = rawRealtimeUs2;
                u4 = u;
                jcToken = roundUsToMs(u4.getProcessStateTime(i, batteryUptimeUs, 0));
                if (jcToken == 0) {
                    rawRealtimeUs2 = batteryUptimeUs;
                } else {
                    rawRealtimeUs = protoOutputStream.start(2246267895828L);
                    rawRealtimeUs2 = jcToken;
                    protoOutputStream.write(1159641169921L, i);
                    cToken = rawRealtimeUs2;
                    rawRealtimeUs2 = batteryUptimeUs;
                    protoOutputStream.write(1112396529666L, cToken);
                    protoOutputStream.end(rawRealtimeUs);
                }
                i++;
                u = u4;
            }
            u4 = u;
            ArrayMap<String, ? extends Timer> syncs = u4.getSyncStats();
            uid4 = syncs.size() - 1;
            while (uid4 >= 0) {
                Timer timer3 = (Timer) syncs.valueAt(uid4);
                bgTimer = timer3.getSubTimer();
                batteryUptimeUs = protoOutputStream.start(2246267895830L);
                protoOutputStream.write(1138166333441L, (String) syncs.keyAt(uid4));
                protoOutputStream2 = proto;
                bmToken = rawRealtimeUs2;
                rawRealtimeUs2 = syncs;
                rawRealtimeUs = batteryUptimeUs;
                batteryUptimeUs = bmToken;
                isy = uid4;
                dumpTimer(protoOutputStream2, 1146756268034L, timer3, batteryUptimeUs, 0);
                dumpTimer(protoOutputStream2, 1146756268035L, bgTimer, batteryUptimeUs, 0);
                protoOutputStream.end(rawRealtimeUs);
                uid4 = isy - 1;
                syncs = rawRealtimeUs2;
                rawRealtimeUs2 = bmToken;
            }
            bmToken = rawRealtimeUs2;
            isy = uid4;
            if (u4.hasUserActivity()) {
                for (i = 0; i < Uid.NUM_USER_ACTIVITY_TYPES; i++) {
                    aid = u4.getUserActivityCount(i, 0);
                    if (aid != 0) {
                        startTimeMs = protoOutputStream.start(2246267895831L);
                        protoOutputStream.write(1159641169921L, i);
                        protoOutputStream.write(1120986464258L, aid);
                        protoOutputStream.end(startTimeMs);
                    }
                }
                rawRealtimeUs = 1120986464258L;
            } else {
                rawRealtimeUs = 1120986464258L;
            }
            protoOutputStream2 = proto;
            batteryUptimeUs = bmToken;
            dumpTimer(protoOutputStream2, 1146756268045L, u4.getVibratorOnTimer(), batteryUptimeUs, 0);
            dumpTimer(protoOutputStream2, 1146756268046L, u4.getVideoTurnedOnTimer(), batteryUptimeUs, 0);
            ArrayMap<String, ? extends Wakelock> wakelocks = u4.getWakelockStats();
            batteryUptimeUs = rawRealtimeUs;
            n3 = wakelocks.size() - 1;
            while (n3 >= 0) {
                Wakelock wl;
                Wakelock wl2 = (Wakelock) wakelocks.valueAt(n3);
                cToken = protoOutputStream.start(2246267895833L);
                protoOutputStream.write(1138166333441L, (String) wakelocks.keyAt(n3));
                isy = n3;
                rawRealtimeUs = cToken;
                Wakelock wl3 = wl2;
                ArrayMap<String, ? extends Wakelock> wakelocks2 = wakelocks;
                dumpTimer(proto, 1146756268034L, wl2.getWakeTime(1), bmToken, null);
                Wakelock wakelocks3 = wl3;
                bgTimer = wakelocks3.getWakeTime(0);
                if (bgTimer != null) {
                    protoOutputStream2 = proto;
                    batteryUptimeUs = bmToken;
                    seToken = pToken;
                    wl = wakelocks3;
                    dumpTimer(protoOutputStream2, 1146756268035L, bgTimer, batteryUptimeUs, null);
                    dumpTimer(protoOutputStream2, 1146756268036L, bgTimer.getSubTimer(), batteryUptimeUs, null);
                } else {
                    seToken = pToken;
                    wl = wakelocks3;
                }
                dumpTimer(proto, 1146756268037L, wl.getWakeTime(2), bmToken, null);
                protoOutputStream.end(rawRealtimeUs);
                n3 = isy - 1;
                wakelocks = wakelocks2;
                pToken = seToken;
            }
            seToken = pToken;
            isy = n3;
            dumpTimer(proto, 1146756268060L, u4.getMulticastWakelockStats(), bmToken, 0);
            n = 1;
            i = packageStats.size() - 1;
            while (i >= 0) {
                ArrayMap<String, ? extends Pkg> packageStats4 = packageStats;
                ArrayMap<String, ? extends Counter> alarms = ((Pkg) packageStats4.valueAt(i)).getWakeupAlarmStats();
                for (int iwa = alarms.size() - n; iwa >= 0; iwa--) {
                    long waToken = protoOutputStream.start(2246267895834L);
                    protoOutputStream.write(1138166333441L, (String) alarms.keyAt(iwa));
                    protoOutputStream.write(1120986464258L, ((Counter) alarms.valueAt(iwa)).getCountLocked(0));
                    protoOutputStream.end(waToken);
                }
                i--;
                packageStats = packageStats4;
                n = 1;
            }
            dumpControllerActivityProto(protoOutputStream, 1146756268037L, u4.getWifiControllerActivity(), 0);
            rawRealtimeUs = protoOutputStream.start(1146756268059L);
            batteryUptimeUs = bmToken;
            protoOutputStream.write(1112396529665L, roundUsToMs(u4.getFullWifiLockTime(batteryUptimeUs, 0)));
            dumpTimer(proto, 1146756268035L, u4.getWifiScanTimer(), batteryUptimeUs, 0);
            protoOutputStream.write(1112396529666L, roundUsToMs(u4.getWifiRunningTime(batteryUptimeUs, 0)));
            long rawRealtimeUs3 = batteryUptimeUs;
            dumpTimer(proto, 1146756268036L, u4.getWifiScanBackgroundTimer(), batteryUptimeUs, 0);
            protoOutputStream.end(rawRealtimeUs);
            protoOutputStream.end(batteryUptimeUs2);
            aid = iu2 + 1;
            which = which2;
            sippers = sippers4;
            aidToPackages = aidToPackages5;
            batteryUptimeUs = batteryUptimeUs3;
            rawRealtimeMs = rawRealtimeMs3;
            n = uid2;
            uidStats = uidStats2;
            rawRealtimeUs = rawRealtimeUs3;
            rawUptimeUs = rawUptimeUs2;
            uidToSipper = uidToSipper2;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:71:0x01c0 A:{Catch:{ all -> 0x0219 }} */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00ed A:{Catch:{ all -> 0x0219 }} */
    private void dumpProtoHistoryLocked(android.util.proto.ProtoOutputStream r27, int r28, long r29) {
        /*
        r26 = this;
        r1 = r26;
        r9 = r27;
        r0 = r26.startIteratingHistoryLocked();
        if (r0 != 0) goto L_0x000b;
    L_0x000a:
        return;
    L_0x000b:
        r0 = 34;
        r2 = 1120986464257; // 0x10500000001 float:1.4E-45 double:5.538409014424E-312;
        r9.write(r2, r0);
        r4 = 1112396529666; // 0x10300000002 float:2.8E-45 double:5.49596909861E-312;
        r0 = r26.getParcelVersion();
        r9.write(r4, r0);
        r0 = r26.getStartPlatformVersion();
        r4 = 1138166333443; // 0x10900000003 float:4.2E-45 double:5.623288846073E-312;
        r9.write(r4, r0);
        r6 = 1138166333444; // 0x10900000004 float:5.6E-45 double:5.62328884608E-312;
        r0 = r26.getEndPlatformVersion();
        r9.write(r6, r0);
        r0 = 0;
        r6 = r0;
    L_0x003c:
        r7 = r26.getHistoryStringPoolSize();	 Catch:{ all -> 0x0219 }
        if (r6 >= r7) goto L_0x0068;
    L_0x0042:
        r7 = 2246267895813; // 0x20b00000005 float:7.0E-45 double:1.1098037986773E-311;
        r7 = r9.start(r7);	 Catch:{ all -> 0x0219 }
        r9.write(r2, r6);	 Catch:{ all -> 0x0219 }
        r10 = 1120986464258; // 0x10500000002 float:2.8E-45 double:5.53840901443E-312;
        r12 = r1.getHistoryTagPoolUid(r6);	 Catch:{ all -> 0x0219 }
        r9.write(r10, r12);	 Catch:{ all -> 0x0219 }
        r10 = r1.getHistoryTagPoolString(r6);	 Catch:{ all -> 0x0219 }
        r9.write(r4, r10);	 Catch:{ all -> 0x0219 }
        r9.end(r7);	 Catch:{ all -> 0x0219 }
        r6 = r6 + 1;
        goto L_0x003c;
    L_0x0068:
        r2 = new android.os.BatteryStats$HistoryPrinter;	 Catch:{ all -> 0x0219 }
        r2.<init>();	 Catch:{ all -> 0x0219 }
        r3 = new android.os.BatteryStats$HistoryItem;	 Catch:{ all -> 0x0219 }
        r3.<init>();	 Catch:{ all -> 0x0219 }
        r10 = r3;
        r3 = -1;
        r5 = -1;
        r7 = 0;
        r11 = 0;
        r8 = r11;
        r12 = r8;
    L_0x007b:
        r8 = r1.getNextHistoryLocked(r10);	 Catch:{ all -> 0x0219 }
        r13 = 0;
        if (r8 == 0) goto L_0x01ef;
    L_0x0083:
        r16 = r12;
        r11 = r10.time;	 Catch:{ all -> 0x0219 }
        r3 = (r5 > r13 ? 1 : (r5 == r13 ? 0 : -1));
        if (r3 >= 0) goto L_0x008f;
    L_0x008b:
        r3 = r11;
        r17 = r3;
        goto L_0x0091;
    L_0x008f:
        r17 = r5;
    L_0x0091:
        r3 = r10.time;	 Catch:{ all -> 0x0219 }
        r3 = (r3 > r29 ? 1 : (r3 == r29 ? 0 : -1));
        if (r3 < 0) goto L_0x01e3;
    L_0x0097:
        r3 = (r29 > r13 ? 1 : (r29 == r13 ? 0 : -1));
        r19 = 1;
        if (r3 < 0) goto L_0x01c4;
    L_0x009d:
        if (r7 != 0) goto L_0x01c4;
    L_0x009f:
        r3 = r10.cmd;	 Catch:{ all -> 0x0219 }
        r4 = 5;
        if (r3 == r4) goto L_0x00d7;
    L_0x00a4:
        r3 = r10.cmd;	 Catch:{ all -> 0x0219 }
        r5 = 7;
        if (r3 == r5) goto L_0x00d7;
    L_0x00a9:
        r3 = r10.cmd;	 Catch:{ all -> 0x0219 }
        r5 = 4;
        if (r3 == r5) goto L_0x00d7;
    L_0x00ae:
        r3 = r10.cmd;	 Catch:{ all -> 0x0219 }
        r5 = 8;
        if (r3 != r5) goto L_0x00b5;
    L_0x00b4:
        goto L_0x00d7;
    L_0x00b5:
        r5 = r10.currentTime;	 Catch:{ all -> 0x0219 }
        r3 = (r5 > r13 ? 1 : (r5 == r13 ? 0 : -1));
        if (r3 == 0) goto L_0x00d5;
    L_0x00bb:
        r8 = 1;
        r3 = r10.cmd;	 Catch:{ all -> 0x0219 }
        r13 = r3;
        r10.cmd = r4;	 Catch:{ all -> 0x0219 }
        r3 = r28 & 32;
        if (r3 == 0) goto L_0x00c8;
    L_0x00c5:
        r7 = r19;
        goto L_0x00c9;
    L_0x00c8:
        r7 = r0;
    L_0x00c9:
        r3 = r27;
        r4 = r10;
        r5 = r17;
        r2.printNextItem(r3, r4, r5, r7);	 Catch:{ all -> 0x0219 }
        r10.cmd = r13;	 Catch:{ all -> 0x0219 }
        r13 = r8;
        goto L_0x00eb;
    L_0x00d5:
        r13 = r7;
        goto L_0x00eb;
    L_0x00d7:
        r13 = 1;
        r3 = r28 & 32;
        if (r3 == 0) goto L_0x00df;
    L_0x00dc:
        r8 = r19;
        goto L_0x00e0;
    L_0x00df:
        r8 = r0;
    L_0x00e0:
        r3 = r2;
        r4 = r27;
        r5 = r10;
        r6 = r17;
        r3.printNextItem(r4, r5, r6, r8);	 Catch:{ all -> 0x0219 }
        r10.cmd = r0;	 Catch:{ all -> 0x0219 }
    L_0x00eb:
        if (r16 == 0) goto L_0x01c0;
    L_0x00ed:
        r3 = r10.cmd;	 Catch:{ all -> 0x0219 }
        if (r3 == 0) goto L_0x0104;
    L_0x00f1:
        r3 = r28 & 32;
        if (r3 == 0) goto L_0x00f8;
    L_0x00f5:
        r8 = r19;
        goto L_0x00f9;
    L_0x00f8:
        r8 = r0;
    L_0x00f9:
        r3 = r2;
        r4 = r27;
        r5 = r10;
        r6 = r17;
        r3.printNextItem(r4, r5, r6, r8);	 Catch:{ all -> 0x0219 }
        r10.cmd = r0;	 Catch:{ all -> 0x0219 }
    L_0x0104:
        r3 = r10.eventCode;	 Catch:{ all -> 0x0219 }
        r14 = r3;
        r3 = r10.eventTag;	 Catch:{ all -> 0x0219 }
        r8 = r3;
        r3 = new android.os.BatteryStats$HistoryTag;	 Catch:{ all -> 0x0219 }
        r3.<init>();	 Catch:{ all -> 0x0219 }
        r10.eventTag = r3;	 Catch:{ all -> 0x0219 }
        r3 = 0;
        r6 = r3;
    L_0x0113:
        r3 = 22;
        if (r6 >= r3) goto L_0x01b2;
        r7 = r16;
        r3 = r7.getStateForEvent(r6);	 Catch:{ all -> 0x0219 }
        r16 = r3;
        if (r16 != 0) goto L_0x012a;
    L_0x0122:
        r25 = r6;
        r24 = r7;
        r15 = r8;
        r0 = 0;
        goto L_0x01aa;
    L_0x012a:
        r3 = r16.entrySet();	 Catch:{ all -> 0x0219 }
        r20 = r3.iterator();	 Catch:{ all -> 0x0219 }
    L_0x0132:
        r3 = r20.hasNext();	 Catch:{ all -> 0x0219 }
        if (r3 == 0) goto L_0x01a4;
    L_0x0138:
        r3 = r20.next();	 Catch:{ all -> 0x0219 }
        r3 = (java.util.Map.Entry) r3;	 Catch:{ all -> 0x0219 }
        r21 = r3;
        r3 = r21.getValue();	 Catch:{ all -> 0x0219 }
        r3 = (android.util.SparseIntArray) r3;	 Catch:{ all -> 0x0219 }
        r5 = r3;
        r3 = r0;
        r4 = r3;
    L_0x0149:
        r3 = r5.size();	 Catch:{ all -> 0x0219 }
        if (r4 >= r3) goto L_0x0198;
    L_0x014f:
        r10.eventCode = r6;	 Catch:{ all -> 0x0219 }
        r3 = r10.eventTag;	 Catch:{ all -> 0x0219 }
        r22 = r21.getKey();	 Catch:{ all -> 0x0219 }
        r0 = r22;
        r0 = (java.lang.String) r0;	 Catch:{ all -> 0x0219 }
        r3.string = r0;	 Catch:{ all -> 0x0219 }
        r0 = r10.eventTag;	 Catch:{ all -> 0x0219 }
        r3 = r5.keyAt(r4);	 Catch:{ all -> 0x0219 }
        r0.uid = r3;	 Catch:{ all -> 0x0219 }
        r0 = r10.eventTag;	 Catch:{ all -> 0x0219 }
        r3 = r5.valueAt(r4);	 Catch:{ all -> 0x0219 }
        r0.poolIdx = r3;	 Catch:{ all -> 0x0219 }
        r0 = r28 & 32;
        if (r0 == 0) goto L_0x0174;
    L_0x0171:
        r0 = r19;
        goto L_0x0175;
    L_0x0174:
        r0 = 0;
    L_0x0175:
        r3 = r2;
        r22 = r4;
        r4 = r27;
        r23 = r5;
        r5 = r10;
        r25 = r6;
        r24 = r7;
        r6 = r17;
        r15 = r8;
        r8 = r0;
        r3.printNextItem(r4, r5, r6, r8);	 Catch:{ all -> 0x0219 }
        r0 = 0;
        r10.wakeReasonTag = r0;	 Catch:{ all -> 0x0219 }
        r10.wakelockTag = r0;	 Catch:{ all -> 0x0219 }
        r4 = r22 + 1;
        r8 = r15;
        r5 = r23;
        r7 = r24;
        r6 = r25;
        r0 = 0;
        goto L_0x0149;
    L_0x0198:
        r22 = r4;
        r23 = r5;
        r25 = r6;
        r24 = r7;
        r15 = r8;
        r0 = 0;
        r0 = 0;
        goto L_0x0132;
    L_0x01a4:
        r25 = r6;
        r24 = r7;
        r15 = r8;
        r0 = 0;
    L_0x01aa:
        r6 = r25 + 1;
        r8 = r15;
        r16 = r24;
        r0 = 0;
        goto L_0x0113;
    L_0x01b2:
        r25 = r6;
        r15 = r8;
        r24 = r16;
        r0 = 0;
        r10.eventCode = r14;	 Catch:{ all -> 0x0219 }
        r10.eventTag = r15;	 Catch:{ all -> 0x0219 }
        r3 = 0;
        r24 = r3;
        goto L_0x01c8;
    L_0x01c0:
        r24 = r16;
        r0 = 0;
        goto L_0x01c8;
    L_0x01c4:
        r24 = r16;
        r0 = 0;
        r13 = r7;
    L_0x01c8:
        r3 = r28 & 32;
        if (r3 == 0) goto L_0x01cf;
    L_0x01cc:
        r8 = r19;
        goto L_0x01d0;
    L_0x01cf:
        r8 = 0;
    L_0x01d0:
        r3 = r2;
        r4 = r27;
        r5 = r10;
        r6 = r17;
        r3.printNextItem(r4, r5, r6, r8);	 Catch:{ all -> 0x0219 }
        r3 = r11;
        r7 = r13;
        r5 = r17;
        r12 = r24;
        r11 = r0;
        r0 = 0;
        goto L_0x007b;
    L_0x01e3:
        r24 = r16;
        r0 = 0;
        r3 = r11;
        r5 = r17;
        r12 = r24;
        r11 = r0;
        r0 = 0;
        goto L_0x007b;
    L_0x01ef:
        r24 = r12;
        r0 = (r29 > r13 ? 1 : (r29 == r13 ? 0 : -1));
        if (r0 < 0) goto L_0x0214;
    L_0x01f5:
        r26.commitCurrentHistoryBatchLocked();	 Catch:{ all -> 0x0219 }
        r11 = 2237677961222; // 0x20900000006 float:8.4E-45 double:1.105559807096E-311;
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0219 }
        r0.<init>();	 Catch:{ all -> 0x0219 }
        r8 = "NEXT: ";
        r0.append(r8);	 Catch:{ all -> 0x0219 }
        r13 = 1;
        r13 = r13 + r3;
        r0.append(r13);	 Catch:{ all -> 0x0219 }
        r0 = r0.toString();	 Catch:{ all -> 0x0219 }
        r9.write(r11, r0);	 Catch:{ all -> 0x0219 }
    L_0x0214:
        r26.finishIteratingHistoryLocked();
        return;
    L_0x0219:
        r0 = move-exception;
        r26.finishIteratingHistoryLocked();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.BatteryStats.dumpProtoHistoryLocked(android.util.proto.ProtoOutputStream, int, long):void");
    }

    private void dumpProtoSystemLocked(ProtoOutputStream proto, BatteryStatsHelper helper) {
        long timeRemainingUs;
        int telephonyNetworkType;
        int telephonyNetworkType2;
        long rawRealtimeUs;
        long pdcToken;
        int i;
        long i2;
        long gwToken;
        int i3;
        int multicastWakeLockCountTotal;
        int i4;
        int i5;
        int i6;
        ProtoOutputStream protoOutputStream = proto;
        long sToken = protoOutputStream.start(1146756268038L);
        long rawUptimeUs = SystemClock.uptimeMillis() * 1000;
        long rawRealtimeUs2 = SystemClock.elapsedRealtime() * 1000;
        long bToken = protoOutputStream.start(1146756268033L);
        long sToken2 = sToken;
        protoOutputStream.write(1112396529665L, getStartClockTime());
        protoOutputStream.write(1112396529666L, getStartCount());
        protoOutputStream.write(1112396529667L, computeRealtime(rawRealtimeUs2, 0) / 1000);
        protoOutputStream.write(1112396529668L, computeUptime(rawUptimeUs, 0) / 1000);
        protoOutputStream.write(1112396529669L, computeBatteryRealtime(rawRealtimeUs2, 0) / 1000);
        protoOutputStream.write(1112396529670L, computeBatteryUptime(rawUptimeUs, 0) / 1000);
        protoOutputStream.write(1112396529671L, computeBatteryScreenOffRealtime(rawRealtimeUs2, 0) / 1000);
        protoOutputStream.write(1112396529672L, computeBatteryScreenOffUptime(rawUptimeUs, 0) / 1000);
        protoOutputStream.write(1112396529673L, getScreenDozeTime(rawRealtimeUs2, 0) / 1000);
        protoOutputStream.write(1112396529674L, getEstimatedBatteryCapacity());
        protoOutputStream.write(1112396529675L, getMinLearnedBatteryCapacity());
        protoOutputStream.write(1112396529676L, getMaxLearnedBatteryCapacity());
        protoOutputStream.end(bToken);
        sToken = protoOutputStream.start(1146756268034L);
        protoOutputStream.write(1120986464257L, getLowDischargeAmountSinceCharge());
        protoOutputStream.write(1120986464258L, getHighDischargeAmountSinceCharge());
        protoOutputStream.write(1120986464259L, getDischargeAmountScreenOnSinceCharge());
        protoOutputStream.write(1120986464260L, getDischargeAmountScreenOffSinceCharge());
        protoOutputStream.write(1120986464261L, getDischargeAmountScreenDozeSinceCharge());
        protoOutputStream.write(1112396529670L, getUahDischarge(0) / 1000);
        protoOutputStream.write(1112396529671L, getUahDischargeScreenOff(0) / 1000);
        protoOutputStream.write(1112396529672L, getUahDischargeScreenDoze(0) / 1000);
        protoOutputStream.write(1112396529673L, getUahDischargeLightDoze(0) / 1000);
        protoOutputStream.write(1112396529674L, getUahDischargeDeepDoze(0) / 1000);
        protoOutputStream.end(sToken);
        long timeRemainingUs2 = computeChargeTimeRemaining(rawRealtimeUs2);
        if (timeRemainingUs2 >= 0) {
            protoOutputStream.write(1112396529667L, timeRemainingUs2 / 1000);
            timeRemainingUs = timeRemainingUs2;
        } else {
            timeRemainingUs2 = computeBatteryTimeRemaining(rawRealtimeUs2);
            if (timeRemainingUs2 >= 0) {
                protoOutputStream.write(1112396529668L, timeRemainingUs2 / 1000);
            } else {
                protoOutputStream.write(1112396529668L, -1);
            }
            timeRemainingUs = timeRemainingUs2;
        }
        dumpDurationSteps(protoOutputStream, 2246267895813L, getChargeLevelStepTracker());
        int i7 = 0;
        while (true) {
            boolean isNone = true;
            if (i7 >= 22) {
                break;
            }
            if (i7 != 0) {
                isNone = false;
            }
            telephonyNetworkType = i7;
            if (i7 == 21) {
                telephonyNetworkType2 = 0;
            } else {
                telephonyNetworkType2 = telephonyNetworkType;
            }
            rawRealtimeUs = rawRealtimeUs2;
            rawRealtimeUs2 = protoOutputStream.start(2246267895816L);
            if (isNone) {
                pdcToken = rawRealtimeUs2;
                protoOutputStream.write(1133871366146L, isNone);
            } else {
                pdcToken = rawRealtimeUs2;
                protoOutputStream.write(1159641169921L, telephonyNetworkType2);
            }
            i = i7;
            long pdcToken2 = pdcToken;
            rawRealtimeUs2 = rawRealtimeUs;
            rawRealtimeUs = sToken;
            dumpTimer(proto, 1146756268035L, getPhoneDataConnectionTimer(i7), rawRealtimeUs2, 0);
            protoOutputStream.end(pdcToken2);
            i7 = i + 1;
            Object obj = null;
            sToken = rawRealtimeUs;
        }
        i = i7;
        long rawRealtimeUs3 = rawRealtimeUs2;
        rawRealtimeUs = sToken;
        dumpDurationSteps(protoOutputStream, 2246267895814L, getDischargeLevelStepTracker());
        long[] cpuFreqs = getCpuFreqs();
        if (cpuFreqs != null) {
            for (long i22 : cpuFreqs) {
                protoOutputStream.write((long) SystemProto.CPU_FREQUENCY, i22);
            }
        }
        dumpControllerActivityProto(protoOutputStream, 1146756268041L, getBluetoothControllerActivity(), 0);
        dumpControllerActivityProto(protoOutputStream, 1146756268042L, getModemControllerActivity(), 0);
        rawRealtimeUs2 = protoOutputStream.start(1146756268044L);
        protoOutputStream.write(1112396529665L, getNetworkActivityBytes(0, 0));
        protoOutputStream.write(1112396529666L, getNetworkActivityBytes(1, 0));
        protoOutputStream.write(1112396529669L, getNetworkActivityPackets(0, 0));
        protoOutputStream.write(1112396529670L, getNetworkActivityPackets(1, 0));
        pdcToken = rawRealtimeUs2;
        protoOutputStream.write(1112396529667L, getNetworkActivityBytes(2, 0));
        protoOutputStream.write(1112396529668L, getNetworkActivityBytes(3, 0));
        protoOutputStream.write(1112396529671L, getNetworkActivityPackets(2, 0));
        protoOutputStream.write(1112396529672L, getNetworkActivityPackets(3, 0));
        protoOutputStream.write(1112396529673L, getNetworkActivityBytes(4, 0));
        protoOutputStream.write(1112396529674L, getNetworkActivityBytes(5, 0));
        long gnToken = pdcToken;
        protoOutputStream.end(gnToken);
        dumpControllerActivityProto(protoOutputStream, 1146756268043L, getWifiControllerActivity(), 0);
        bToken = protoOutputStream.start(1146756268045L);
        protoOutputStream = proto;
        sToken = rawRealtimeUs3;
        protoOutputStream.write(1112396529665L, getWifiOnTime(rawRealtimeUs3, 0) / 1000);
        protoOutputStream.write(1112396529666L, getGlobalWifiRunningTime(sToken, 0) / 1000);
        protoOutputStream.end(bToken);
        for (Entry<String, ? extends Timer> ent : getKernelWakelockStats().entrySet()) {
            rawRealtimeUs2 = protoOutputStream.start(2246267895822L);
            gwToken = bToken;
            protoOutputStream.write(1138166333441L, (String) ent.getKey());
            long gwToken2 = gwToken;
            long kwToken = rawRealtimeUs2;
            gwToken = timeRemainingUs;
            dumpTimer(proto, 1146756268034L, (Timer) ent.getValue(), sToken, 0);
            protoOutputStream.end(kwToken);
            bToken = gwToken2;
            timeRemainingUs = gwToken;
        }
        gwToken = timeRemainingUs;
        int i8 = 1;
        SparseArray<? extends Uid> uidStats = getUidStats();
        int iu = 0;
        long fullWakeLockTimeTotalUs = 0;
        long partialWakeLockTimeTotalUs = 0;
        while (iu < uidStats.size()) {
            ArrayMap<String, ? extends Wakelock> wakelocks = ((Uid) uidStats.valueAt(iu)).getWakelockStats();
            int iw = wakelocks.size() - i8;
            while (iw >= 0) {
                Wakelock wl = (Wakelock) wakelocks.valueAt(iw);
                Timer fullWakeTimer = wl.getWakeTime(i8);
                if (fullWakeTimer != null) {
                    i3 = 0;
                    fullWakeLockTimeTotalUs += fullWakeTimer.getTotalTimeLocked(sToken, 0);
                } else {
                    i3 = 0;
                }
                Timer partialWakeTimer = wl.getWakeTime(i3);
                if (partialWakeTimer != null) {
                    partialWakeLockTimeTotalUs += partialWakeTimer.getTotalTimeLocked(sToken, i3);
                }
                iw--;
                i8 = 1;
            }
            iu++;
            i8 = 1;
        }
        rawRealtimeUs2 = protoOutputStream.start(1146756268047L);
        protoOutputStream.write(1112396529665L, getScreenOnTime(sToken, 0) / 1000);
        protoOutputStream.write(1112396529666L, getPhoneOnTime(sToken, 0) / 1000);
        protoOutputStream.write(1112396529667L, fullWakeLockTimeTotalUs / 1000);
        protoOutputStream.write(1112396529668L, partialWakeLockTimeTotalUs / 1000);
        protoOutputStream.write(1112396529669L, getMobileRadioActiveTime(sToken, 0) / 1000);
        protoOutputStream.write(1112396529670L, getMobileRadioActiveAdjustedTime(0) / 1000);
        protoOutputStream.write(1120986464263L, getMobileRadioActiveCount(0));
        protoOutputStream.write(1120986464264L, getMobileRadioActiveUnknownTime(0) / 1000);
        protoOutputStream.write(1112396529673L, getInteractiveTime(sToken, 0) / 1000);
        protoOutputStream.write(1112396529674L, getPowerSaveModeEnabledTime(sToken, 0) / 1000);
        protoOutputStream.write(1120986464267L, getNumConnectivityChange(0));
        protoOutputStream.write(1112396529676L, getDeviceIdleModeTime(2, sToken, 0) / 1000);
        protoOutputStream.write(1120986464269L, getDeviceIdleModeCount(2, 0));
        protoOutputStream.write(1112396529678L, getDeviceIdlingTime(2, sToken, 0) / 1000);
        protoOutputStream.write(1120986464271L, getDeviceIdlingCount(2, 0));
        protoOutputStream.write(1112396529680L, getLongestDeviceIdleModeTime(2));
        protoOutputStream.write(1112396529681L, getDeviceIdleModeTime(1, sToken, 0) / 1000);
        protoOutputStream.write(1120986464274L, getDeviceIdleModeCount(1, 0));
        protoOutputStream.write(1112396529683L, getDeviceIdlingTime(1, sToken, 0) / 1000);
        protoOutputStream.write(1120986464276L, getDeviceIdlingCount(1, 0));
        protoOutputStream.write(1112396529685L, getLongestDeviceIdleModeTime(1));
        protoOutputStream.end(rawRealtimeUs2);
        timeRemainingUs = getWifiMulticastWakelockTime(sToken, 0);
        i3 = getWifiMulticastWakelockCount(0);
        bToken = protoOutputStream.start(1146756268055L);
        protoOutputStream.write(1112396529665L, timeRemainingUs / 1000);
        protoOutputStream.write(1120986464258L, i3);
        protoOutputStream.end(bToken);
        List<BatterySipper> sippers = helper.getUsageList();
        List<BatterySipper> sippers2;
        if (sippers != null) {
            telephonyNetworkType = 0;
            while (telephonyNetworkType < sippers.size()) {
                BatterySipper bs = (BatterySipper) sippers.get(telephonyNetworkType);
                iu = 0;
                int uid = 0;
                long wmctToken = bToken;
                switch (bs.drainType) {
                    case AMBIENT_DISPLAY:
                        iu = 13;
                        break;
                    case IDLE:
                        iu = 1;
                        break;
                    case CELL:
                        iu = 2;
                        break;
                    case PHONE:
                        iu = 3;
                        break;
                    case WIFI:
                        iu = 4;
                        break;
                    case BLUETOOTH:
                        iu = 5;
                        break;
                    case SCREEN:
                        iu = 7;
                        break;
                    case FLASHLIGHT:
                        iu = 6;
                        break;
                    case APP:
                        sippers2 = sippers;
                        continue;
                    case USER:
                        iu = 8;
                        uid = UserHandle.getUid(bs.userId, 0);
                        break;
                    case UNACCOUNTED:
                        iu = 9;
                        break;
                    case OVERCOUNTED:
                        iu = 10;
                        break;
                    case CAMERA:
                        iu = 11;
                        break;
                    case MEMORY:
                        iu = 12;
                        break;
                    default:
                        break;
                }
                bToken = bs;
                i22 = protoOutputStream.start(2246267895825L);
                sippers2 = sippers;
                protoOutputStream.write((long) 1, iu);
                protoOutputStream.write(1120986464258L, uid);
                protoOutputStream.write(1103806595075L, bToken.totalPowerMah);
                protoOutputStream.write(1133871366148L, bToken.shouldHide);
                protoOutputStream.write(1103806595077L, bToken.screenPowerMah);
                protoOutputStream.write(1103806595078L, bToken.proportionalSmearMah);
                protoOutputStream.end(i22);
                telephonyNetworkType++;
                bToken = wmctToken;
                sippers = sippers2;
            }
            sippers2 = sippers;
        } else {
            sippers2 = sippers;
        }
        timeRemainingUs = protoOutputStream.start(1146756268050L);
        protoOutputStream.write(1103806595073L, helper.getPowerProfile().getBatteryCapacity());
        protoOutputStream.write(1103806595074L, helper.getComputedPower());
        protoOutputStream.write(1103806595075L, helper.getMinDrainedPower());
        protoOutputStream.write(1103806595076L, helper.getMaxDrainedPower());
        protoOutputStream.end(timeRemainingUs);
        Map<String, ? extends Timer> rpmStats = getRpmStats();
        Map<String, ? extends Timer> screenOffRpmStats = getScreenOffRpmStats();
        for (Entry<String, ? extends Timer> ent2 : rpmStats.entrySet()) {
            long rpmToken = protoOutputStream.start(2246267895827L);
            protoOutputStream.write(1138166333441L, (String) ent2.getKey());
            long rpmToken2 = rpmToken;
            Map<String, ? extends Timer> screenOffRpmStats2 = screenOffRpmStats;
            rawRealtimeUs2 = sToken;
            multicastWakeLockCountTotal = i3;
            dumpTimer(proto, 1146756268034L, (Timer) ent2.getValue(), rawRealtimeUs2, 0);
            Map<String, ? extends Timer> screenOffRpmStats3 = screenOffRpmStats2;
            Map<String, ? extends Timer> screenOffRpmStats4 = screenOffRpmStats3;
            dumpTimer(proto, 1146756268035L, (Timer) screenOffRpmStats3.get(ent2.getKey()), rawRealtimeUs2, 0);
            protoOutputStream.end(rpmToken2);
            i3 = multicastWakeLockCountTotal;
            screenOffRpmStats = screenOffRpmStats4;
        }
        multicastWakeLockCountTotal = i3;
        i3 = 0;
        while (i3 < 5) {
            bToken = protoOutputStream.start(2246267895828L);
            protoOutputStream.write(1159641169921L, i3);
            long sbToken = bToken;
            i4 = i3;
            dumpTimer(proto, 1146756268034L, getScreenBrightnessTimer(i3), sToken, 0);
            protoOutputStream.end(sbToken);
            i3 = i4 + 1;
        }
        i4 = i3;
        dumpTimer(proto, 1146756268053L, getPhoneSignalScanningTimer(), sToken, 0);
        i3 = 0;
        while (i3 < 5) {
            bToken = protoOutputStream.start(2246267895824L);
            protoOutputStream.write(1159641169921L, i3);
            long pssToken = bToken;
            i4 = i3;
            dumpTimer(proto, 1146756268034L, getPhoneSignalStrengthTimer(i3), sToken, 0);
            protoOutputStream.end(pssToken);
            i3 = i4 + 1;
        }
        i4 = i3;
        for (Entry<String, ? extends Timer> ent3 : getWakeupReasonStats().entrySet()) {
            rawRealtimeUs2 = protoOutputStream.start(2246267895830L);
            protoOutputStream.write(1138166333441L, (String) ent3.getKey());
            long wrToken = rawRealtimeUs2;
            dumpTimer(proto, 1146756268034L, (Timer) ent3.getValue(), sToken, 0);
            protoOutputStream.end(wrToken);
        }
        i3 = 0;
        while (i3 < 5) {
            bToken = protoOutputStream.start(2246267895832L);
            protoOutputStream.write(1159641169921L, i3);
            long wssToken = bToken;
            i5 = i3;
            dumpTimer(proto, 1146756268034L, getWifiSignalStrengthTimer(i3), sToken, 0);
            protoOutputStream.end(wssToken);
            i3 = i5 + 1;
        }
        i5 = i3;
        i3 = 0;
        while (i3 < 8) {
            rawRealtimeUs2 = protoOutputStream.start(2246267895833L);
            protoOutputStream.write(1159641169921L, i3);
            long wsToken = rawRealtimeUs2;
            i6 = i3;
            dumpTimer(proto, 1146756268034L, getWifiStateTimer(i3), sToken, 0);
            protoOutputStream.end(wsToken);
            i3 = i6 + 1;
        }
        i6 = i3;
        i3 = 0;
        while (i3 < 13) {
            rawRealtimeUs2 = protoOutputStream.start(2246267895834L);
            protoOutputStream.write(1159641169921L, i3);
            long j = 1159641169921L;
            long wssToken2 = rawRealtimeUs2;
            i6 = i3;
            dumpTimer(proto, 1146756268034L, getWifiSupplStateTimer(i3), sToken, 0);
            protoOutputStream.end(wssToken2);
            i3 = i6 + 1;
        }
        protoOutputStream.end(sToken2);
    }
}
