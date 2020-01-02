package com.miui.whetstone;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.job.JobInfo;
import android.bluetooth.BleScanWrapper;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.location.ILocationPolicyListener;
import android.location.LocationPolicyManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.LocalLog;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.android.internal.util.ArrayUtils;
import com.google.android.collect.Maps;
import com.miui.whetstone.IPowerKeeperPolicy.Stub;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PowerKeeperPolicy extends Stub {
    public static final int APP_BG_IDLE_LEVEL_DELAY = 1;
    public static final int APP_BG_IDLE_LEVEL_DISABLE = 2;
    public static final int APP_BG_IDLE_LEVEL_NORMAL = 0;
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    private static final int MSG_LE_SCAN_PAROLE_CHANGE = 1001;
    private static final int MSG_LE_SCAN_UID_RULE_CHANGE = 1000;
    private static final String TAG = "PowerKeeperPolicy";
    private static final boolean VERBOSE = Log.isLoggable("power.bluetooth", 3);
    private static final boolean debug = false;
    private static PowerKeeperPolicy sInstance;
    private final int[] SYSTEM_PID_STAT_FORMAT = new int[]{288, 4128, 4128};
    private int mAlarmDataTotal;
    private SparseArray<String[]> mAlarmRestricts;
    private SparseIntArray mAlarmUidData;
    private boolean mAppBGIdleFeatureStatus = false;
    private final SparseIntArray mAppBGIdleLevel = new SparseIntArray();
    private final Object mBleLock = new Object();
    private SparseArray<ArrayList<String>> mBlockedUidWakelocks = new SparseArray();
    private final SparseArray<Map<String, Integer>> mBrdcastUidTypeInfo;
    private int mBroadcastDataTotal;
    private SparseArray<String[]> mBroadcastRestricts;
    private Map<String, Integer> mBroadcastTypeData;
    private SparseIntArray mBroadcastUidData;
    private Map<BleScanWrapper, Client> mClientMap = Maps.newHashMap();
    private Context mContext;
    private Class<?> mExtraActivityManagerService;
    private boolean mLeScanFeatureEnable = false;
    private Handler mLeScanHandler;
    private LocalLog mLocalLog;
    private ILocationPolicyListener mLocationPolicyListener;
    private final Object mLock = new Object();
    private ParoleCheck mParoleCheck;
    private final Object mPolicyLock;
    private Class<?> mPowerManagerServiceInjector;
    private ClassLoader mSystemServiceClassLoader;
    private final String[] mSytemPidStatData = new String[2];
    private SparseBooleanArray mUidAllow = new SparseBooleanArray();
    private final SparseBooleanArray mUidBroadcastStat;
    private final SparseArray<Intent> mUidPushAlarmProperty;
    private final SparseBooleanArray mUidPushAlarmStat;
    private SparseBooleanArray mUidScanning = new SparseBooleanArray();
    private Intent pushAlarmLeaderIntent = null;
    private int pushAlarmLeaderUid;
    private IPowerKeeperClient sPowerKeeperService;
    private Method setAppToIdle;
    private Method setUidWakeLockDisabledState;

    class Client {
        private boolean mAllowed = false;
        private BleScanWrapper mBleScanWrapper;
        private DeathRecipient mDeathRecipient = null;
        private IBinder mIBinder;
        private boolean mScanning = false;
        private int mUid;

        public Client(BleScanWrapper bleScanWrapper, IBinder b, int uid) {
            this.mBleScanWrapper = bleScanWrapper;
            this.mIBinder = b;
            this.mUid = uid;
        }

        public void clearResource() {
            this.mBleScanWrapper = null;
            this.mIBinder = null;
            this.mDeathRecipient = null;
        }

        public void setScanning(boolean scanning) {
            if (PowerKeeperPolicy.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("uid = ");
                stringBuilder.append(this.mUid);
                stringBuilder.append(" setScanning, scanning = ");
                stringBuilder.append(scanning);
                Log.d(PowerKeeperPolicy.TAG, stringBuilder.toString());
            }
            this.mScanning = scanning;
        }

        public boolean getScanning() {
            return this.mScanning;
        }

        public void setAllowed(boolean allowed) {
            if (PowerKeeperPolicy.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("uid = ");
                stringBuilder.append(this.mUid);
                stringBuilder.append(" setAllowed, allowed = ");
                stringBuilder.append(allowed);
                Log.d(PowerKeeperPolicy.TAG, stringBuilder.toString());
            }
            this.mAllowed = allowed;
        }

        public boolean getAllowed() {
            return this.mAllowed;
        }

        public void linkToDeath(DeathRecipient deathRecipient) {
            String str = PowerKeeperPolicy.TAG;
            try {
                if (PowerKeeperPolicy.VERBOSE) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("linkToDeath for mUid=");
                    stringBuilder.append(this.mUid);
                    stringBuilder.append(" client: ");
                    stringBuilder.append(this.mBleScanWrapper);
                    Log.d(str, stringBuilder.toString());
                }
                this.mIBinder.linkToDeath(deathRecipient, 0);
                this.mDeathRecipient = deathRecipient;
            } catch (RemoteException e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unable to link deathRecipient for client: ");
                stringBuilder2.append(this.mBleScanWrapper);
                Log.e(str, stringBuilder2.toString());
            }
        }

        public void unlinkToDeath() {
            String str = PowerKeeperPolicy.TAG;
            if (this.mDeathRecipient != null) {
                try {
                    if (PowerKeeperPolicy.VERBOSE) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("unlinkToDeath for mUid=");
                        stringBuilder.append(this.mUid);
                        stringBuilder.append(" client: ");
                        stringBuilder.append(this.mBleScanWrapper);
                        Log.d(str, stringBuilder.toString());
                    }
                    this.mIBinder.unlinkToDeath(this.mDeathRecipient, 0);
                    this.mDeathRecipient = null;
                } catch (NoSuchElementException e) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Unable to unlink deathRecipient for client: ");
                    stringBuilder2.append(this.mBleScanWrapper);
                    Log.e(str, stringBuilder2.toString());
                }
            }
        }

        public void startLeScan() {
            if (this.mScanning) {
                if (PowerKeeperPolicy.DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("startLeScan, uid = ");
                    stringBuilder.append(this.mUid);
                    stringBuilder.append(" is scanning");
                    Log.d(PowerKeeperPolicy.TAG, stringBuilder.toString());
                }
                return;
            }
            try {
                IBinder b = ServiceManager.getService(BluetoothAdapter.BLUETOOTH_MANAGER_SERVICE);
                if (b != null) {
                    IBluetoothGatt gatt = IBluetoothManager.Stub.asInterface(b).getBluetoothGatt();
                    try {
                        PowerKeeperPolicy powerKeeperPolicy = PowerKeeperPolicy.this;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("ForceStartLeScan, uid = ");
                        stringBuilder2.append(this.mUid);
                        stringBuilder2.append(this.mBleScanWrapper);
                        powerKeeperPolicy.logdWithLocal(stringBuilder2.toString());
                        this.mBleScanWrapper.startScan(gatt);
                    } catch (RemoteException e) {
                        PowerKeeperPolicy powerKeeperPolicy2 = PowerKeeperPolicy.this;
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("startLeScan remoteException:");
                        stringBuilder3.append(e);
                        powerKeeperPolicy2.logeWithLocal(stringBuilder3.toString());
                    }
                }
            } catch (Exception e2) {
                PowerKeeperPolicy powerKeeperPolicy3 = PowerKeeperPolicy.this;
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("startLeScan:");
                stringBuilder4.append(e2);
                powerKeeperPolicy3.logeWithLocal(stringBuilder4.toString());
            }
            this.mScanning = true;
        }

        public void stopLeScan() {
            if (this.mScanning) {
                try {
                    IBinder b = ServiceManager.getService(BluetoothAdapter.BLUETOOTH_MANAGER_SERVICE);
                    if (b != null) {
                        IBluetoothGatt gatt = IBluetoothManager.Stub.asInterface(b).getBluetoothGatt();
                        PowerKeeperPolicy powerKeeperPolicy = PowerKeeperPolicy.this;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("ForceStopLeScan, uid = ");
                        stringBuilder.append(this.mUid);
                        stringBuilder.append(this.mBleScanWrapper);
                        powerKeeperPolicy.logdWithLocal(stringBuilder.toString());
                        try {
                            this.mBleScanWrapper.stopScan(gatt);
                        } catch (RemoteException e) {
                            PowerKeeperPolicy powerKeeperPolicy2 = PowerKeeperPolicy.this;
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("stopLeScan remoteException:");
                            stringBuilder2.append(e);
                            powerKeeperPolicy2.logeWithLocal(stringBuilder2.toString());
                        }
                    }
                } catch (Exception e2) {
                    PowerKeeperPolicy powerKeeperPolicy3 = PowerKeeperPolicy.this;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("stopLeScan:");
                    stringBuilder3.append(e2);
                    powerKeeperPolicy3.logeWithLocal(stringBuilder3.toString());
                }
                this.mScanning = false;
                return;
            }
            if (PowerKeeperPolicy.DEBUG) {
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("stopLeScan, uid = ");
                stringBuilder4.append(this.mUid);
                stringBuilder4.append(" is not scanning");
                Log.d(PowerKeeperPolicy.TAG, stringBuilder4.toString());
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(512);
            stringBuilder.append('{');
            stringBuilder.append("uid=");
            stringBuilder.append(this.mUid);
            stringBuilder.append(" isScan=");
            stringBuilder.append(this.mScanning);
            stringBuilder.append(" isAllow=");
            stringBuilder.append(this.mAllowed);
            stringBuilder.append(this.mBleScanWrapper);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    class ClientDeathRecipient implements DeathRecipient {
        BleScanWrapper mBleScanWrapper;
        final int mUid;

        public ClientDeathRecipient(BleScanWrapper bleScanWrapper, int uid) {
            this.mBleScanWrapper = bleScanWrapper;
            this.mUid = uid;
        }

        public void binderDied() {
            PowerKeeperPolicy powerKeeperPolicy = PowerKeeperPolicy.this;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Binder is dead - unregistering client (");
            stringBuilder.append(this.mBleScanWrapper);
            stringBuilder.append(")! uid=");
            stringBuilder.append(this.mUid);
            powerKeeperPolicy.logdWithLocal(stringBuilder.toString());
            synchronized (PowerKeeperPolicy.this.mBleLock) {
                Client client = (Client) PowerKeeperPolicy.this.mClientMap.get(this.mBleScanWrapper);
                if (client != null) {
                    client.stopLeScan();
                    client.unlinkToDeath();
                    client.clearResource();
                    PowerKeeperPolicy.this.mClientMap.remove(this.mBleScanWrapper);
                }
            }
        }
    }

    class ParoleCheck {
        private static final int MSG_PAROLE_CHECK_RUNNING = 1002;
        private static final int MSG_START_PAROLE_CHECK = 1001;
        private static final int STATE_RUNNING = 4;
        private static final int STATE_STARTED = 3;
        private static final int STATE_STARTING = 1;
        private static final int STATE_STOPPED = 0;
        private static final int STATE_STOPPING = 2;
        private boolean mInParole = false;
        private int mMsgWhat;
        private Handler mNotifyHandler;
        private long[] mParoleArray = new long[]{115200000, JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS};
        private Handler mParoleCheckHandler;
        private int mParoleIndex = 0;
        private Object mParoleLock = new Object();
        private int state = 0;

        public ParoleCheck(Handler h, int msgWhat) {
            this.mNotifyHandler = h;
            this.mMsgWhat = msgWhat;
            this.mParoleCheckHandler = new Handler(this.mNotifyHandler.getLooper(), new Callback(PowerKeeperPolicy.this) {
                public boolean handleMessage(Message msg) {
                    boolean access$1600;
                    synchronized (ParoleCheck.this.mParoleLock) {
                        access$1600 = ParoleCheck.this.onParoleMessageHandlerLocked(msg);
                    }
                    return access$1600;
                }
            });
        }

        public void startParoleCheck() {
            synchronized (this.mParoleLock) {
                if (this.state == 0) {
                    this.state = 1;
                    PowerKeeperPolicy.this.mLocalLog.log("startParoleCheck");
                    exitParoleLocked();
                    this.mParoleCheckHandler.sendEmptyMessage(1001);
                }
            }
        }

        public void stopParoleCheck() {
            synchronized (this.mParoleLock) {
                if (this.state != 0) {
                    this.state = 0;
                    PowerKeeperPolicy.this.mLocalLog.log("stopParoleCheck");
                    exitParoleLocked();
                    this.mParoleCheckHandler.removeCallbacksAndMessages(null);
                }
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:19:0x0033  */
        public void setParoleCheckParam(long[] r5) {
            /*
            r4 = this;
            r0 = r4.mParoleLock;
            monitor-enter(r0);
            r1 = r4.state;	 Catch:{ all -> 0x0038 }
            r2 = r4.state;	 Catch:{ all -> 0x0038 }
            r3 = 4;
            if (r2 != r3) goto L_0x0018;
        L_0x000a:
            r2 = r4.isInParoleState();	 Catch:{ all -> 0x0038 }
            if (r2 == 0) goto L_0x0013;
        L_0x0010:
            r4.exitParoleAndNotifyLocked();	 Catch:{ all -> 0x0038 }
        L_0x0013:
            r2 = com.miui.whetstone.PowerKeeperPolicy.this;	 Catch:{ all -> 0x0038 }
            r2.stopLeScanAllLocked();	 Catch:{ all -> 0x0038 }
        L_0x0018:
            r2 = 0;
            r4.mParoleIndex = r2;	 Catch:{ all -> 0x0038 }
            if (r5 == 0) goto L_0x002e;
        L_0x001d:
            r2 = r5.length;	 Catch:{ all -> 0x0038 }
            if (r2 != 0) goto L_0x0021;
        L_0x0020:
            goto L_0x002e;
        L_0x0021:
            r2 = r5.length;	 Catch:{ all -> 0x0038 }
            r2 = r2 % 2;
            if (r2 != 0) goto L_0x0031;
        L_0x0026:
            r2 = r5.length;	 Catch:{ all -> 0x0038 }
            r2 = java.util.Arrays.copyOf(r5, r2);	 Catch:{ all -> 0x0038 }
            r4.mParoleArray = r2;	 Catch:{ all -> 0x0038 }
            goto L_0x0031;
        L_0x002e:
            r2 = 0;
            r4.mParoleArray = r2;	 Catch:{ all -> 0x0038 }
        L_0x0031:
            if (r1 != r3) goto L_0x0036;
        L_0x0033:
            r4.startParoleCheck();	 Catch:{ all -> 0x0038 }
        L_0x0036:
            monitor-exit(r0);	 Catch:{ all -> 0x0038 }
            return;
        L_0x0038:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0038 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.whetstone.PowerKeeperPolicy$ParoleCheck.setParoleCheckParam(long[]):void");
        }

        public boolean isWorking() {
            synchronized (this.mParoleLock) {
                if (this.state == 0) {
                    return false;
                }
                return true;
            }
        }

        public boolean isInParoleState() {
            boolean z;
            synchronized (this.mParoleLock) {
                z = this.mInParole;
            }
            return z;
        }

        private void enterParoleLocked() {
            this.mInParole = true;
        }

        private void exitParoleLocked() {
            this.mInParole = false;
        }

        private void enterParoleAndNotifyLocked() {
            this.mInParole = true;
            this.mNotifyHandler.obtainMessage(this.mMsgWhat, 1, 0).sendToTarget();
        }

        private void exitParoleAndNotifyLocked() {
            this.mInParole = false;
            this.mNotifyHandler.obtainMessage(this.mMsgWhat, 0, 0).sendToTarget();
        }

        private boolean onParoleMessageHandlerLocked(Message msg) {
            int i = msg.what;
            if (i != 1001) {
                if (i != 1002) {
                    return false;
                }
                if (this.state != 4) {
                    return true;
                }
                if (this.mParoleArray == null) {
                    Log.e(PowerKeeperPolicy.TAG, "Parole check array is null");
                    return true;
                }
                if (this.mParoleIndex % 2 == 0) {
                    enterParoleAndNotifyLocked();
                } else {
                    exitParoleAndNotifyLocked();
                }
                i = this.mParoleArray.length;
                int i2 = this.mParoleIndex;
                if (i2 + 1 >= i) {
                    this.mParoleIndex = i2 - 1;
                } else {
                    this.mParoleIndex = i2 + 1;
                }
                this.mParoleCheckHandler.sendEmptyMessageDelayed(1002, this.mParoleArray[this.mParoleIndex]);
                return true;
            } else if (this.state != 1) {
                return true;
            } else {
                this.state = 4;
                this.mParoleIndex = 0;
                long[] jArr = this.mParoleArray;
                if (jArr == null) {
                    return true;
                }
                this.mParoleCheckHandler.sendEmptyMessageDelayed(1002, jArr[0]);
                return true;
            }
        }
    }

    public void setAppBGIdleFeatureEnable(boolean enable) {
        synchronized (this.mAppBGIdleLevel) {
            this.mAppBGIdleFeatureStatus = enable;
        }
    }

    public boolean getAppBGIdleFeatureEnable() {
        boolean z;
        synchronized (this.mAppBGIdleLevel) {
            z = this.mAppBGIdleFeatureStatus;
        }
        return z;
    }

    public void setAppBGIdleLevel(int uid, int level) {
        if (UserHandle.isApp(uid) && level <= 2 && level >= 0) {
            synchronized (this.mAppBGIdleLevel) {
                this.mAppBGIdleLevel.put(uid, level);
            }
        }
    }

    public int getAppBGIdleLevel(int uid) {
        if (!UserHandle.isApp(uid)) {
            return 0;
        }
        int i;
        synchronized (this.mAppBGIdleLevel) {
            i = this.mAppBGIdleLevel.get(uid, 0);
        }
        return i;
    }

    private PowerKeeperPolicy(ClassLoader cld) {
        this.mLocalLog = new LocalLog(DEBUG ? 512 : 256);
        this.mLocationPolicyListener = new ILocationPolicyListener.Stub() {
            public void onUidRulesChanged(int uid, int uidRules) {
                synchronized (PowerKeeperPolicy.this.mBleLock) {
                    boolean uidAllow;
                    int i = 1;
                    if (uidRules == 0 || uidRules == 1) {
                        uidAllow = true;
                    } else {
                        uidAllow = false;
                    }
                    if (PowerKeeperPolicy.this.mUidAllow.get(uid, true) == uidAllow) {
                        return;
                    }
                    PowerKeeperPolicy.this.mUidAllow.put(uid, uidAllow);
                    if (PowerKeeperPolicy.this.mLeScanFeatureEnable) {
                        Handler access$1000 = PowerKeeperPolicy.this.mLeScanHandler;
                        if (!uidAllow) {
                            i = 0;
                        }
                        access$1000.obtainMessage(1000, uid, i).sendToTarget();
                        return;
                    }
                }
            }

            public void onRestrictBackgroundChanged(boolean restrictBackground) {
            }
        };
        this.mPolicyLock = new Object();
        this.mAlarmRestricts = new SparseArray();
        this.mBroadcastRestricts = new SparseArray();
        this.mAlarmUidData = new SparseIntArray();
        this.mAlarmDataTotal = 0;
        this.mBroadcastTypeData = new HashMap();
        this.mBroadcastUidData = new SparseIntArray();
        this.mBroadcastDataTotal = 0;
        this.pushAlarmLeaderIntent = null;
        this.pushAlarmLeaderUid = -1;
        this.mUidPushAlarmStat = new SparseBooleanArray();
        this.mUidPushAlarmProperty = new SparseArray();
        try {
            this.mPowerManagerServiceInjector = Class.forName("com.android.server.power.PowerManagerServiceInjector", false, cld);
            this.setUidWakeLockDisabledState = this.mPowerManagerServiceInjector.getDeclaredMethod("setUidPartialWakeLockDisabledState", new Class[]{Integer.TYPE, String.class, Boolean.TYPE});
        } catch (Exception e) {
            String str = TAG;
            Log.e(str, str, e);
        }
        this.sPowerKeeperService = null;
        this.mContext = null;
        this.mUidBroadcastStat = new SparseBooleanArray();
        this.mBrdcastUidTypeInfo = new SparseArray();
    }

    public static PowerKeeperPolicy getInstance() {
        if (sInstance == null) {
            sInstance = new PowerKeeperPolicy(Thread.currentThread().getContextClassLoader());
        }
        return sInstance;
    }

    public String getPackageNameByPid(Context context, int pid) {
        String packageName = null;
        FileInputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("/proc/");
            stringBuilder.append(pid);
            stringBuilder.append("/cmdline");
            is = new FileInputStream(new File(stringBuilder.toString()));
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            packageName = br.readLine();
            if (packageName != null) {
                packageName = packageName.replace(0, ' ').trim();
            }
            try {
                br.close();
            } catch (IOException e) {
            }
            try {
                isr.close();
            } catch (IOException e2) {
            }
            try {
                is.close();
            } catch (IOException e3) {
            }
        } catch (Exception e4) {
            Log.e(TAG, "getPackageNameByPid", e4);
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e5) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e6) {
                }
            }
            if (is != null) {
                is.close();
            }
        } catch (Throwable th) {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e7) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e8) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e9) {
                }
            }
        }
        if (packageName == null) {
            for (RunningAppProcessInfo processInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
                if (processInfo.pid == pid) {
                    packageName = processInfo.processName;
                    break;
                }
            }
        }
        if (packageName == null) {
            return packageName;
        }
        int index = packageName.indexOf(":");
        if (index > 0) {
            return packageName.substring(0, index);
        }
        return packageName;
    }

    public int getOomAdjByPid(Context context, int pid) {
        String oom_adj = null;
        FileInputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("/proc/");
            stringBuilder.append(pid);
            stringBuilder.append("/oom_adj");
            is = new FileInputStream(new File(stringBuilder.toString()));
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            oom_adj = br.readLine();
            try {
                br.close();
            } catch (IOException e) {
            }
            try {
                isr.close();
            } catch (IOException e2) {
            }
            try {
                is.close();
            } catch (IOException e3) {
            }
        } catch (Exception e4) {
            Log.e(TAG, "getOomAdjByPid", e4);
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e5) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e6) {
                }
            }
            if (is != null) {
                is.close();
            }
        } catch (Throwable th) {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e7) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e8) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e9) {
                }
            }
        }
        if (oom_adj != null) {
            return Integer.valueOf(oom_adj).intValue();
        }
        return -100;
    }

    public void setContext(Context ctx) {
        if (this.mContext == null) {
            this.mContext = ctx;
            bleScanInit();
        }
    }

    public void setAlarmPolicy(AlarmPolicy[] policies, boolean clear) {
        if (Binder.getCallingUid() == 1000) {
            if (DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("setAlarmPolicy clear=");
                stringBuilder.append(clear);
                stringBuilder.append(" policies=");
                stringBuilder.append(Arrays.toString(policies));
                Log.d(TAG, stringBuilder.toString());
            }
            if (policies == null) {
                if (clear) {
                    synchronized (this.mAlarmRestricts) {
                        this.mAlarmRestricts.clear();
                    }
                }
                return;
            }
            synchronized (this.mAlarmRestricts) {
                if (clear) {
                    try {
                        this.mAlarmRestricts.clear();
                    } catch (Throwable th) {
                    }
                }
                for (int i = 0; i < policies.length; i++) {
                    if (policies[i] != null) {
                        if (policies[i].mTags != null) {
                            this.mAlarmRestricts.put(policies[i].mUid, policies[i].mTags);
                        } else if (!clear) {
                            this.mAlarmRestricts.delete(policies[i].mUid);
                        }
                    }
                }
            }
        }
    }

    public AlarmPolicy[] getAlarmPolicies() {
        AlarmPolicy[] policies;
        synchronized (this.mAlarmRestricts) {
            int size = this.mAlarmRestricts.size();
            policies = new AlarmPolicy[size];
            for (int i = 0; i < size; i++) {
                policies[i].mUid = this.mAlarmRestricts.keyAt(i);
                policies[i].mTags = (String[]) this.mAlarmRestricts.valueAt(i);
            }
        }
        return policies;
    }

    public void setBroadcastPolicy(BroadcastPolicy[] policies, boolean clear) {
        if (Binder.getCallingUid() == 1000 && policies != null) {
            if (clear) {
                this.mBroadcastRestricts.clear();
            }
            int i = 0;
            while (i < policies.length) {
                if (policies[i] != null) {
                    if (policies[i].mRestrictTypes != null && policies[i].mRestrictTypes.length != 0) {
                        this.mBroadcastRestricts.put(policies[i].mUid, policies[i].mRestrictTypes);
                    } else if (!clear) {
                        this.mBroadcastRestricts.delete(policies[i].mUid);
                    }
                }
                i++;
            }
        }
    }

    public BroadcastPolicy[] getBroadcastPolicies() {
        BroadcastPolicy[] policies = new BroadcastPolicy[this.mBroadcastRestricts.size()];
        for (int i = 0; i < this.mBroadcastRestricts.size(); i++) {
            if (policies[i] != null) {
                policies[i].mUid = this.mBroadcastRestricts.keyAt(i);
                policies[i].mRestrictTypes = (String[]) this.mBroadcastRestricts.get(policies[i].mUid);
            }
        }
        return policies;
    }

    public void setAppBroadcastControlStat(int uid, boolean isBlocked) {
        synchronized (this.mUidBroadcastStat) {
            if (UserHandle.isApp(uid)) {
                this.mUidBroadcastStat.put(uid, isBlocked);
                return;
            }
        }
    }

    public boolean getAppBroadcastControlStat(int uid) {
        if (!UserHandle.isApp(uid)) {
            return false;
        }
        boolean z;
        synchronized (this.mUidBroadcastStat) {
            z = this.mUidBroadcastStat.get(uid);
        }
        return z;
    }

    private boolean hasAlarmRestrict(int uid, String tag) {
        Object[] tags;
        boolean ret = false;
        synchronized (this.mAlarmRestricts) {
            tags = (String[]) this.mAlarmRestricts.get(uid);
        }
        if (tags != null) {
            try {
                Object operateTag = tag.split(":")[1];
                if (tags.length == 0 || ArrayUtils.contains(tags, operateTag)) {
                    ret = true;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hasAlarmRestrict uid=");
            stringBuilder.append(uid);
            stringBuilder.append(" tag=");
            stringBuilder.append(tag);
            stringBuilder.append(" ret=");
            stringBuilder.append(ret);
            Log.d(TAG, stringBuilder.toString());
        }
        return ret;
    }

    private boolean hasBroadcastRestrict(int uid, String type) {
        String[] broadcastRestrictTypes;
        boolean ret = false;
        synchronized (this.mPolicyLock) {
            broadcastRestrictTypes = (String[]) this.mBroadcastRestricts.get(uid);
        }
        if (broadcastRestrictTypes != null && type != null) {
            if (broadcastRestrictTypes.length == 1) {
                if ("a.jack.bc.1".equals(broadcastRestrictTypes[0])) {
                    return true;
                }
            }
            for (String equals : broadcastRestrictTypes) {
                if (equals.equals(type)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    public boolean isAlarmAllowedLocked(int pid, int uid, String tag) {
        boolean alarmAllowed = true;
        if (hasAlarmRestrict(uid, tag)) {
            alarmAllowed = false;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("isAlarmAllowedLocked, uid = ");
            stringBuilder.append(uid);
            stringBuilder.append(",  tag = ");
            stringBuilder.append(tag);
            stringBuilder.append(", return :false");
            Log.d(TAG, stringBuilder.toString());
        }
        if (!alarmAllowed) {
            this.mAlarmDataTotal++;
            this.mAlarmUidData.put(uid, this.mAlarmUidData.get(uid, 0) + 1);
        }
        return alarmAllowed;
    }

    public boolean isBroadcastAllowedLocked(int pid, int uid, String type) {
        boolean broadcastAllowed = true;
        if (type == null || !getAppBroadcastControlStat(uid)) {
            return true;
        }
        if (hasBroadcastRestrict(uid, type)) {
            broadcastAllowed = false;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("isBroadcastAllowedLocked, uid = ");
            stringBuilder.append(uid);
            stringBuilder.append(", type = ");
            stringBuilder.append(type);
            stringBuilder.append(", return :");
            stringBuilder.append(false);
            Log.d(TAG, stringBuilder.toString());
        }
        if (!broadcastAllowed) {
            this.mBroadcastDataTotal++;
            Integer count = (Integer) this.mBroadcastTypeData.get(type);
            if (count == null) {
                count = Integer.valueOf(0);
            }
            Map map = this.mBroadcastTypeData;
            Integer valueOf = Integer.valueOf(count.intValue() + 1);
            count = valueOf;
            map.put(type, valueOf);
            count = Integer.valueOf(this.mBroadcastUidData.get(uid, 0));
            SparseIntArray sparseIntArray = this.mBroadcastUidData;
            valueOf = Integer.valueOf(count.intValue() + 1);
            count = valueOf;
            sparseIntArray.put(uid, valueOf.intValue());
            Map<String, Integer> brdCastTypeData = (Map) this.mBrdcastUidTypeInfo.get(uid);
            if (brdCastTypeData == null) {
                brdCastTypeData = new HashMap();
                this.mBrdcastUidTypeInfo.put(uid, brdCastTypeData);
            }
            valueOf = (Integer) brdCastTypeData.get(type);
            if (valueOf == null) {
                valueOf = Integer.valueOf(0);
            }
            Integer valueOf2 = Integer.valueOf(valueOf.intValue() + 1);
            valueOf = valueOf2;
            brdCastTypeData.put(type, valueOf2);
        }
        return broadcastAllowed;
    }

    public void updateWakelockBlockedUid(int uid, String tag, boolean isBlocked) {
        int callingUid = Binder.getCallingUid();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Caller[");
        stringBuilder.append(callingUid);
        stringBuilder.append("] updateWakelockBlockedUid:uid=");
        stringBuilder.append(uid);
        stringBuilder.append(", tag=");
        stringBuilder.append(tag);
        stringBuilder.append(", block=");
        stringBuilder.append(isBlocked);
        Slog.d(TAG, stringBuilder.toString());
        if (callingUid == 1000) {
            boolean changed = false;
            synchronized (this.mLock) {
                ArrayList<String> otags;
                if (this.mBlockedUidWakelocks.indexOfKey(uid) >= 0) {
                    otags = (ArrayList) this.mBlockedUidWakelocks.get(uid);
                    if (isBlocked) {
                        if (tag == null && otags != null) {
                            this.mBlockedUidWakelocks.put(uid, null);
                            changed = true;
                        } else if (!(tag == null || otags == null || otags.contains(tag))) {
                            otags.add(tag);
                            changed = true;
                        }
                    } else if (tag == null) {
                        this.mBlockedUidWakelocks.remove(uid);
                        changed = true;
                    } else if (otags == null) {
                        String str = TAG;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("cannot unblock the wakelock[");
                        stringBuilder2.append(tag);
                        stringBuilder2.append("] for uid ");
                        stringBuilder2.append(uid);
                        stringBuilder2.append(", please unblock all the wakelock with a null tag");
                        Slog.w(str, stringBuilder2.toString());
                    } else if (otags.contains(tag)) {
                        otags.remove(tag);
                        if (otags.size() == 0) {
                            this.mBlockedUidWakelocks.remove(uid);
                        }
                        changed = true;
                    }
                } else if (isBlocked) {
                    changed = true;
                    otags = null;
                    if (tag != null) {
                        otags = new ArrayList();
                        otags.add(tag);
                    }
                    this.mBlockedUidWakelocks.put(uid, otags);
                }
            }
            if (changed) {
                try {
                    this.setUidWakeLockDisabledState.invoke(this.mPowerManagerServiceInjector, new Object[]{Integer.valueOf(uid), tag, Boolean.valueOf(isBlocked)});
                } catch (ReflectiveOperationException e) {
                    Log.e(TAG, "updateWakelockBlockedUid", e);
                } catch (Exception e2) {
                    Log.e(TAG, "updateWakelockBlockedUid", e2);
                }
            }
        }
    }

    public boolean isWakelockDisabledByPolicy(String tag, int uid) {
        boolean disabled = false;
        synchronized (this.mLock) {
            if (this.mBlockedUidWakelocks.indexOfKey(uid) >= 0) {
                disabled = true;
                ArrayList<String> tags = (ArrayList) this.mBlockedUidWakelocks.get(uid);
                if (tags != null) {
                    disabled = false;
                    if (tag != null) {
                        Iterator it = tags.iterator();
                        while (it.hasNext()) {
                            if (Pattern.compile((String) it.next()).matcher(tag).find()) {
                                disabled = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return disabled;
    }

    public void notifyFrozenAppWakeUpByHighPriorityAlarm(int uid) {
        IPowerKeeperClient iPowerKeeperClient = this.sPowerKeeperService;
        if (iPowerKeeperClient != null) {
            try {
                iPowerKeeperClient.notifyFrozenAppWakeUpByHighPriorityAlarm(uid);
            } catch (Exception e) {
                Log.e(TAG, "notifyFrozenAppWakeUpByBroacastReceive failed", e);
            }
        }
    }

    public void perfThermalBreakAcquire(int boostTimeOut) {
        IPowerKeeperClient iPowerKeeperClient = this.sPowerKeeperService;
        if (iPowerKeeperClient != null) {
            try {
                iPowerKeeperClient.perfThermalBreakAcquire(boostTimeOut);
            } catch (Exception e) {
                Log.e(TAG, "perfThermalBreakAcquire failed", e);
            }
        }
    }

    public void offerPowerKeeperIBinder(final IBinder b) {
        this.sPowerKeeperService = IPowerKeeperClient.Stub.asInterface(b);
        try {
            b.linkToDeath(new DeathRecipient() {
                public void binderDied() {
                    b.unlinkToDeath(this, 0);
                    PowerKeeperPolicy.this.sPowerKeeperService = null;
                    Slog.d(PowerKeeperPolicy.TAG, "powerkeeper died, reset handle to null");
                    PowerKeeperPolicy.this.restoreFakeGpsStatus();
                    PowerKeeperPolicy.this.restoreAlarm();
                }
            }, 0);
        } catch (Exception e) {
            Log.e(TAG, "offerPowerKeeperIBinder", e);
        }
    }

    public void setAppPushAlarmProperty(int uid, Intent intent, boolean isEnable) {
        synchronized (this.mLock) {
            this.mUidPushAlarmProperty.put(uid, intent);
            this.mUidPushAlarmStat.put(uid, isEnable);
        }
    }

    public Intent getAppPushAlarmProperty(int uid) {
        Intent intent;
        synchronized (this.mUidPushAlarmProperty) {
            intent = (Intent) this.mUidPushAlarmProperty.get(uid);
        }
        return intent;
    }

    public boolean getAppPushAlarmFunc(int uid) {
        boolean result;
        synchronized (this.mUidPushAlarmStat) {
            result = this.mUidPushAlarmStat.get(uid, false);
        }
        return result;
    }

    public void setAppPushAlarmLeader(int uid, Intent intent) {
        synchronized (this.mLock) {
            this.pushAlarmLeaderUid = uid;
            this.pushAlarmLeaderIntent = intent;
            this.mUidPushAlarmProperty.put(uid, intent);
            this.mUidPushAlarmStat.put(uid, true);
        }
    }

    public int getAppPushAlarmLeaderUid() {
        int i;
        synchronized (this.mLock) {
            i = this.pushAlarmLeaderUid;
        }
        return i;
    }

    private Intent getAppPushAlarmLeaderIntent() {
        Intent intent;
        synchronized (this.mLock) {
            intent = this.pushAlarmLeaderIntent;
        }
        return intent;
    }

    private String getPkgNameByUid(int uid) {
        String[] packagesForUid = this.mContext.getPackageManager().getPackagesForUid(uid);
        if (packagesForUid == null || packagesForUid.length == 0) {
            return null;
        }
        return packagesForUid[0];
    }

    private void dumpBrdCastManageInfo(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("---------------------------------------------");
        JSONArray jsonArray = new JSONArray();
        synchronized (this.mBrdcastUidTypeInfo) {
            int i = 0;
            while (i < this.mBrdcastUidTypeInfo.size()) {
                try {
                    JSONObject jsonForApp = new JSONObject();
                    String pkgName = getPkgNameByUid(this.mBrdcastUidTypeInfo.keyAt(i));
                    if (pkgName == null) {
                        pkgName = Integer.toString(this.mBrdcastUidTypeInfo.keyAt(i));
                    }
                    jsonForApp.put("pkgName", pkgName);
                    JSONArray broadCastArray = new JSONArray();
                    for (Entry<String, Integer> entry : ((Map) this.mBrdcastUidTypeInfo.valueAt(i)).entrySet()) {
                        JSONObject jsonBroadCast = new JSONObject();
                        jsonBroadCast.put("Name", entry.getKey());
                        jsonBroadCast.put("Cnts", entry.getValue());
                        broadCastArray.put(jsonBroadCast);
                    }
                    jsonForApp.put("broadcast", broadCastArray);
                    jsonArray.put(jsonForApp);
                    i++;
                } catch (JSONException e) {
                    Log.e(TAG, "dumpBrdCastManageInfo", e);
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("broadCastInfo:");
        stringBuilder.append(jsonArray.toString());
        pw.println(stringBuilder.toString());
        pw.println("---------------------------------------------");
    }

    public void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        int size;
        int i;
        int i2;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        writer.println("\nDump of PowerKeeperPolicy:\n");
        int i3 = 0;
        for (String arg : args) {
            if (arg.equalsIgnoreCase("broadCastInfo")) {
                dumpBrdCastManageInfo(fd, writer, args);
                return;
            }
        }
        StringBuilder sb = new StringBuilder(1024).append(10);
        synchronized (this.mAlarmRestricts) {
            size = this.mAlarmRestricts.size();
            for (i = 0; i < size; i++) {
                String[] temp = (String[]) this.mAlarmRestricts.valueAt(i);
                if (temp != null) {
                    sb.append('[');
                    sb.append(this.mAlarmRestricts.keyAt(i));
                    sb.append("]=");
                    sb.append(temp.length == 0 ? "all" : Arrays.toString(temp));
                    sb.append(10);
                }
            }
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("====mAlarmRestricts:");
        stringBuilder3.append(sb);
        writer.println(stringBuilder3.toString());
        sb.setLength(0);
        stringBuilder3 = new StringBuilder();
        stringBuilder3.append("\n====Total block alarm ");
        stringBuilder3.append(this.mAlarmDataTotal);
        stringBuilder3.append(" times");
        sb.append(stringBuilder3.toString());
        int size2 = this.mAlarmUidData.size();
        for (i2 = 0; i2 < size2; i2++) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("\nmAlarmUidData[");
            stringBuilder.append(this.mAlarmUidData.keyAt(i2));
            stringBuilder.append("] = ");
            stringBuilder.append(this.mAlarmUidData.valueAt(i2));
            sb.append(stringBuilder.toString());
        }
        writer.println(sb.toString());
        writer.println("\n====mBroadcastRestricts:");
        for (i2 = 0; i2 < this.mBroadcastRestricts.size(); i2++) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("------[");
            stringBuilder.append(this.mBroadcastRestricts.keyAt(i2));
            stringBuilder.append("] = ");
            writer.println(stringBuilder.toString());
            SparseArray sparseArray = this.mBroadcastRestricts;
            for (String item : (String[]) sparseArray.get(sparseArray.keyAt(i2))) {
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("----------");
                stringBuilder4.append(item);
                writer.println(stringBuilder4.toString());
            }
        }
        StringBuilder stringBuilder5 = new StringBuilder();
        stringBuilder5.append("\n====Total block broadcast ");
        stringBuilder5.append(this.mBroadcastDataTotal);
        stringBuilder5.append(" times");
        writer.println(stringBuilder5.toString());
        for (Entry<String, Integer> entry : this.mBroadcastTypeData.entrySet()) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("mBroadcastTypeData[");
            stringBuilder2.append((String) entry.getKey());
            stringBuilder2.append("] = ");
            stringBuilder2.append(entry.getValue());
            writer.println(stringBuilder2.toString());
        }
        for (i2 = 0; i2 < this.mBroadcastUidData.size(); i2++) {
            size = this.mBroadcastUidData.keyAt(i2);
            i = this.mBroadcastUidData.get(size);
            StringBuilder stringBuilder6 = new StringBuilder();
            stringBuilder6.append("mBroadcastUidData[");
            stringBuilder6.append(size);
            stringBuilder6.append("] = ");
            stringBuilder6.append(i);
            writer.println(stringBuilder6.toString());
        }
        writer.println("\n====mBlockedWakelocks====\n\t");
        for (i2 = 0; i2 < this.mBlockedUidWakelocks.size(); i2++) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(" uid:");
            stringBuilder.append(this.mBlockedUidWakelocks.keyAt(i2));
            stringBuilder.append("\t");
            writer.print(stringBuilder.toString());
            if (this.mBlockedUidWakelocks.valueAt(i2) != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(" tags:");
                stringBuilder.append(((ArrayList) this.mBlockedUidWakelocks.valueAt(i2)).toString());
                writer.println(stringBuilder.toString());
            } else {
                writer.println();
            }
        }
        writer.println("\n====mUidBroadcastStat====\n\t");
        synchronized (this.mUidBroadcastStat) {
            for (i2 = 0; i2 < this.mUidBroadcastStat.size(); i2++) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(" uid:");
                stringBuilder2.append(this.mUidBroadcastStat.keyAt(i2));
                stringBuilder2.append(" policy = ");
                stringBuilder2.append(this.mUidBroadcastStat.valueAt(i2));
                writer.println(stringBuilder2.toString());
            }
        }
        stringBuilder5 = new StringBuilder();
        stringBuilder5.append("\n====AppBGIdleFeatureIs====");
        stringBuilder5.append(getAppBGIdleFeatureEnable());
        writer.println(stringBuilder5.toString());
        if (getAppBGIdleFeatureEnable()) {
            synchronized (this.mAppBGIdleLevel) {
                while (i3 < this.mAppBGIdleLevel.size()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(" uid:");
                    stringBuilder.append(this.mAppBGIdleLevel.keyAt(i3));
                    stringBuilder.append(" level = ");
                    stringBuilder.append(this.mAppBGIdleLevel.valueAt(i3));
                    writer.println(stringBuilder.toString());
                    i3++;
                }
            }
        }
        dumpBleScan(fd, writer, args);
    }

    public void bleScanInit() {
        if (this.mContext != null) {
            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            this.mLeScanHandler = new Handler(handlerThread.getLooper(), new Callback() {
                public boolean handleMessage(Message msg) {
                    boolean access$400;
                    synchronized (PowerKeeperPolicy.this.mBleLock) {
                        access$400 = PowerKeeperPolicy.this.onLeScanMessageHandler(msg);
                    }
                    return access$400;
                }
            });
            this.mParoleCheck = new ParoleCheck(this.mLeScanHandler, 1001);
            LocationPolicyManager.from(this.mContext).registerListener(this.mLocationPolicyListener);
        }
    }

    private boolean onLeScanMessageHandler(Message msg) {
        int i = msg.what;
        boolean z = true;
        if (i != 1000) {
            if (i != 1001) {
                return false;
            }
            if (!this.mLeScanFeatureEnable) {
                return true;
            }
            if (msg.arg1 <= 0) {
                z = false;
            }
            boolean inParole = z;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("leScan parole change, inParole = ");
            stringBuilder.append(inParole);
            logdWithLocal(stringBuilder.toString());
            if (inParole) {
                startLeScanAllLocked();
                return true;
            }
            stopLeScanAllLocked();
            return true;
        } else if (!this.mLeScanFeatureEnable) {
            return true;
        } else {
            i = msg.arg1;
            if (msg.arg2 <= 0) {
                z = false;
            }
            boolean uidAllow = z;
            z = false;
            boolean leScanAllowedAfter = false;
            String str = " allowNow=";
            String str2 = ", allowBefore= ";
            String str3 = "leScan uid rule change, uid = ";
            Client client;
            StringBuilder stringBuilder2;
            if (uidAllow) {
                client = getClient(i);
                if (client != null) {
                    z = client.getAllowed();
                    client.setAllowed(uidAllow);
                    leScanAllowedAfter = client.getAllowed();
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str3);
                    stringBuilder2.append(i);
                    stringBuilder2.append(str2);
                    stringBuilder2.append(z);
                    stringBuilder2.append(str);
                    stringBuilder2.append(leScanAllowedAfter);
                    logdWithLocal(stringBuilder2.toString());
                }
                if (!(z || !leScanAllowedAfter || this.mParoleCheck.isInParoleState())) {
                    startLeScanLocked(i);
                }
                if (hasRestrictedScaner() || !this.mParoleCheck.isWorking()) {
                    return true;
                }
                this.mParoleCheck.stopParoleCheck();
                return true;
            }
            client = getClient(i);
            if (client != null) {
                z = client.getAllowed();
                client.setAllowed(uidAllow);
                leScanAllowedAfter = client.getAllowed();
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str3);
                stringBuilder2.append(i);
                stringBuilder2.append(str2);
                stringBuilder2.append(z);
                stringBuilder2.append(str);
                stringBuilder2.append(leScanAllowedAfter);
                logdWithLocal(stringBuilder2.toString());
            }
            if (!(!z || leScanAllowedAfter || this.mParoleCheck.isInParoleState())) {
                stopLeScanLocked(i);
            }
            if (!hasRestrictedScaner() || this.mParoleCheck.isWorking()) {
                return true;
            }
            this.mParoleCheck.startParoleCheck();
            return true;
        }
    }

    private boolean checkLeScanRunningLocked() {
        boolean ret = false;
        synchronized (this.mBleLock) {
            for (Entry<BleScanWrapper, Client> entry : this.mClientMap.entrySet()) {
                if (((Client) entry.getValue()).getScanning()) {
                    ret = true;
                    break;
                }
            }
        }
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("checkLeScanRunningLocked: ret = ");
            stringBuilder.append(ret);
            Log.d(TAG, stringBuilder.toString());
        }
        return ret;
    }

    private boolean checkLeScanAllowedLocked() {
        boolean ret = false;
        synchronized (this.mBleLock) {
            for (Entry<BleScanWrapper, Client> entry : this.mClientMap.entrySet()) {
                if (((Client) entry.getValue()).getAllowed()) {
                    ret = true;
                    break;
                }
            }
        }
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("checkLeScanAllowedLocked: ret = ");
            stringBuilder.append(ret);
            Log.d(TAG, stringBuilder.toString());
        }
        return ret;
    }

    private boolean hasRestrictedScaner() {
        boolean ret = false;
        synchronized (this.mBleLock) {
            for (Entry<BleScanWrapper, Client> entry : this.mClientMap.entrySet()) {
                if (!((Client) entry.getValue()).getAllowed()) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    private Client getClient(int uid) {
        for (Entry<BleScanWrapper, Client> entry : this.mClientMap.entrySet()) {
            Client value = (Client) entry.getValue();
            if (value.mUid == uid) {
                return value;
            }
        }
        return null;
    }

    private boolean checkLeScanAllowedLocked(int uid) {
        boolean ret = false;
        synchronized (this.mBleLock) {
            for (Entry<BleScanWrapper, Client> entry : this.mClientMap.entrySet()) {
                Client value = (Client) entry.getValue();
                if (value.mUid == uid) {
                    ret = value.getAllowed();
                    break;
                }
            }
        }
        return ret;
    }

    private void setLeScanAllowedLocked(int uid, boolean allow) {
        for (Entry<BleScanWrapper, Client> entry : this.mClientMap.entrySet()) {
            if (((Client) entry.getValue()).mUid == uid) {
                ((Client) entry.getValue()).setAllowed(allow);
                if (DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("setLeScanAllowedLocked: uid = ");
                    stringBuilder.append(uid);
                    stringBuilder.append(", allow = ");
                    stringBuilder.append(allow);
                    Log.d(TAG, stringBuilder.toString());
                }
            }
        }
    }

    private boolean checkLeScanParoleLocked() {
        boolean ret = this.mParoleCheck.isInParoleState();
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("checkLeScanParoleLocked: ret = ");
            stringBuilder.append(ret);
            Log.d(TAG, stringBuilder.toString());
        }
        return ret;
    }

    private void startLeScanAllLocked() {
        if (DEBUG) {
            Log.d(TAG, "startLeScanAllLocked");
        }
        for (Entry<BleScanWrapper, Client> entry : this.mClientMap.entrySet()) {
            ((Client) entry.getValue()).startLeScan();
        }
    }

    private void startLeScanLocked(int uid) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("startLeScanLocked uid=");
            stringBuilder.append(uid);
            Log.d(TAG, stringBuilder.toString());
        }
        for (Entry<BleScanWrapper, Client> entry : this.mClientMap.entrySet()) {
            Client value = (Client) entry.getValue();
            if (value.mUid == uid) {
                value.startLeScan();
            }
        }
    }

    private void stopLeScanAllLocked() {
        if (DEBUG) {
            Log.d(TAG, "stopLeScanAllLocked");
        }
        for (Entry<BleScanWrapper, Client> entry : this.mClientMap.entrySet()) {
            ((Client) entry.getValue()).stopLeScan();
        }
    }

    private void stopLeScanLocked(int uid) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("stopLeScanLocked uid=");
            stringBuilder.append(uid);
            Log.d(TAG, stringBuilder.toString());
        }
        for (Entry<BleScanWrapper, Client> entry : this.mClientMap.entrySet()) {
            Client value = (Client) entry.getValue();
            if (value.mUid == uid) {
                value.stopLeScan();
            }
        }
    }

    /* JADX WARNING: Missing block: B:19:0x0069, code skipped:
            return;
     */
    public void setLeScanFeature(boolean r5) {
        /*
        r4 = this;
        r0 = r4.mContext;
        if (r0 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = r4.mBleLock;
        monitor-enter(r0);
        r1 = r4.mLeScanFeatureEnable;	 Catch:{ all -> 0x006a }
        if (r1 != r5) goto L_0x000e;
    L_0x000c:
        monitor-exit(r0);	 Catch:{ all -> 0x006a }
        return;
    L_0x000e:
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006a }
        r1.<init>();	 Catch:{ all -> 0x006a }
        r2 = "setLeScanFeature: ";
        r1.append(r2);	 Catch:{ all -> 0x006a }
        r1.append(r5);	 Catch:{ all -> 0x006a }
        r1 = r1.toString();	 Catch:{ all -> 0x006a }
        r4.logdWithLocal(r1);	 Catch:{ all -> 0x006a }
        r4.mLeScanFeatureEnable = r5;	 Catch:{ all -> 0x006a }
        if (r5 == 0) goto L_0x002d;
    L_0x0027:
        r1 = r4.mParoleCheck;	 Catch:{ all -> 0x006a }
        r1.startParoleCheck();	 Catch:{ all -> 0x006a }
        goto L_0x0068;
    L_0x002d:
        r1 = r4.mParoleCheck;	 Catch:{ all -> 0x006a }
        r1.stopParoleCheck();	 Catch:{ all -> 0x006a }
        r1 = r4.mLeScanHandler;	 Catch:{ all -> 0x006a }
        r2 = 0;
        r1.removeCallbacksAndMessages(r2);	 Catch:{ all -> 0x006a }
        r1 = r4.mClientMap;	 Catch:{ all -> 0x006a }
        r1 = r1.entrySet();	 Catch:{ all -> 0x006a }
        r1 = r1.iterator();	 Catch:{ all -> 0x006a }
    L_0x0042:
        r2 = r1.hasNext();	 Catch:{ all -> 0x006a }
        if (r2 == 0) goto L_0x005e;
    L_0x0048:
        r2 = r1.next();	 Catch:{ all -> 0x006a }
        r2 = (java.util.Map.Entry) r2;	 Catch:{ all -> 0x006a }
        r3 = r2.getValue();	 Catch:{ all -> 0x006a }
        r3 = (com.miui.whetstone.PowerKeeperPolicy.Client) r3;	 Catch:{ all -> 0x006a }
        r3.startLeScan();	 Catch:{ all -> 0x006a }
        r3.unlinkToDeath();	 Catch:{ all -> 0x006a }
        r3.clearResource();	 Catch:{ all -> 0x006a }
        goto L_0x0042;
    L_0x005e:
        r1 = r4.mClientMap;	 Catch:{ all -> 0x006a }
        r1.clear();	 Catch:{ all -> 0x006a }
        r1 = r4.mUidScanning;	 Catch:{ all -> 0x006a }
        r1.clear();	 Catch:{ all -> 0x006a }
    L_0x0068:
        monitor-exit(r0);	 Catch:{ all -> 0x006a }
        return;
    L_0x006a:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x006a }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.whetstone.PowerKeeperPolicy.setLeScanFeature(boolean):void");
    }

    public void setLeScanParam(Bundle bundle) {
        if (this.mContext != null) {
            synchronized (this.mBleLock) {
                if (bundle.containsKey("parolePeriodArray")) {
                    long[] parolePeriodArray = bundle.getLongArray("parolePeriodArray");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("setLeScanParam array=");
                    stringBuilder.append(Arrays.toString(parolePeriodArray));
                    logdWithLocal(stringBuilder.toString());
                    this.mParoleCheck.setParoleCheckParam(parolePeriodArray);
                }
            }
        }
    }

    public boolean isLeScanAllowed(int uid) {
        if (this.mContext == null) {
            return true;
        }
        synchronized (this.mBleLock) {
            boolean allow;
            try {
                allow = LocationPolicyManager.isAllowedByLocationPolicy(this.mContext, uid, 3);
                this.mUidAllow.put(uid, allow);
            } catch (Exception e) {
                allow = true;
                Log.e(TAG, "isLeScanAllowed", e);
            }
            if (this.mLeScanFeatureEnable) {
                if (!allow && checkLeScanAllowedLocked(uid)) {
                    allow = true;
                }
                if (!allow && checkLeScanParoleLocked()) {
                    allow = true;
                }
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("isLeScanAllowed: uid = ");
                    stringBuilder.append(uid);
                    stringBuilder.append(", allow = ");
                    stringBuilder.append(allow);
                    Log.d(str, stringBuilder.toString());
                }
                this.mUidScanning.put(uid, allow);
                return allow;
            }
            return true;
        }
    }

    public void enableATrace(boolean enable, String filterProcess) {
        String str = TAG;
        IPowerKeeperClient iPowerKeeperClient = this.sPowerKeeperService;
        if (iPowerKeeperClient != null) {
            try {
                iPowerKeeperClient.enableATrace(enable, filterProcess);
            } catch (RemoteException e) {
                Log.e(str, "EnableATrace remote failed", e);
            } catch (Exception e2) {
                Log.e(str, "EnableATrace failed", e2);
            }
        }
    }

    /* JADX WARNING: Missing block: B:25:0x00ac, code skipped:
            return;
     */
    public void startLeScan(android.os.Bundle r11) {
        /*
        r10 = this;
        r0 = r10.mContext;
        if (r0 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r1 = "android.permission.BLUETOOTH_ADMIN";
        r2 = "Need BLUETOOTH_ADMIN permission";
        r0.enforceCallingPermission(r1, r2);
        r0 = "BleScanWrapper";
        r0 = r11.getParcelable(r0);
        r0 = (android.bluetooth.BleScanWrapper) r0;
        r1 = "IBinder";
        r1 = r11.getBinder(r1);
        r2 = "uid";
        r2 = r11.getInt(r2);
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "startLeScan: ";
        r3.append(r4);
        r3.append(r0);
        r4 = " uid=";
        r3.append(r4);
        r3.append(r2);
        r3 = r3.toString();
        r10.logdWithLocal(r3);
        r3 = r10.mBleLock;
        monitor-enter(r3);
        r4 = r10.mLeScanFeatureEnable;	 Catch:{ all -> 0x00ad }
        if (r4 != 0) goto L_0x0047;
    L_0x0045:
        monitor-exit(r3);	 Catch:{ all -> 0x00ad }
        return;
    L_0x0047:
        r4 = r10.mUidAllow;	 Catch:{ all -> 0x00ad }
        r5 = 1;
        r4 = r4.get(r2, r5);	 Catch:{ all -> 0x00ad }
        r6 = r10.mUidScanning;	 Catch:{ all -> 0x00ad }
        r5 = r6.get(r2, r5);	 Catch:{ all -> 0x00ad }
        r6 = r10.mClientMap;	 Catch:{ all -> 0x00ad }
        r6 = r6.get(r0);	 Catch:{ all -> 0x00ad }
        r6 = (com.miui.whetstone.PowerKeeperPolicy.Client) r6;	 Catch:{ all -> 0x00ad }
        r7 = 0;
        if (r6 == 0) goto L_0x006f;
    L_0x005f:
        r8 = r6.getAllowed();	 Catch:{ all -> 0x00ad }
        r7 = r8;
        r6.unlinkToDeath();	 Catch:{ all -> 0x00ad }
        r6.clearResource();	 Catch:{ all -> 0x00ad }
        r8 = r10.mClientMap;	 Catch:{ all -> 0x00ad }
        r8.remove(r0);	 Catch:{ all -> 0x00ad }
    L_0x006f:
        r8 = new com.miui.whetstone.PowerKeeperPolicy$Client;	 Catch:{ all -> 0x00ad }
        r8.<init>(r0, r1, r2);	 Catch:{ all -> 0x00ad }
        r6 = r8;
        r6.setAllowed(r4);	 Catch:{ all -> 0x00ad }
        r6.setScanning(r5);	 Catch:{ all -> 0x00ad }
        r8 = new com.miui.whetstone.PowerKeeperPolicy$ClientDeathRecipient;	 Catch:{ all -> 0x00ad }
        r8.<init>(r0, r2);	 Catch:{ all -> 0x00ad }
        r6.linkToDeath(r8);	 Catch:{ all -> 0x00ad }
        r8 = r10.mClientMap;	 Catch:{ all -> 0x00ad }
        r8.put(r0, r6);	 Catch:{ all -> 0x00ad }
        r8 = r4;
        if (r7 != 0) goto L_0x0098;
    L_0x008b:
        if (r8 == 0) goto L_0x0098;
    L_0x008d:
        r9 = r10.mParoleCheck;	 Catch:{ all -> 0x00ad }
        r9 = r9.isInParoleState();	 Catch:{ all -> 0x00ad }
        if (r9 != 0) goto L_0x0098;
    L_0x0095:
        r6.startLeScan();	 Catch:{ all -> 0x00ad }
    L_0x0098:
        r9 = r10.hasRestrictedScaner();	 Catch:{ all -> 0x00ad }
        if (r9 != 0) goto L_0x00ab;
    L_0x009e:
        r9 = r10.mParoleCheck;	 Catch:{ all -> 0x00ad }
        r9 = r9.isWorking();	 Catch:{ all -> 0x00ad }
        if (r9 == 0) goto L_0x00ab;
    L_0x00a6:
        r9 = r10.mParoleCheck;	 Catch:{ all -> 0x00ad }
        r9.stopParoleCheck();	 Catch:{ all -> 0x00ad }
    L_0x00ab:
        monitor-exit(r3);	 Catch:{ all -> 0x00ad }
        return;
    L_0x00ad:
        r4 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x00ad }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.whetstone.PowerKeeperPolicy.startLeScan(android.os.Bundle):void");
    }

    /* JADX WARNING: Missing block: B:19:0x0079, code skipped:
            return;
     */
    public void stopLeScan(android.os.Bundle r8) {
        /*
        r7 = this;
        r0 = r7.mContext;
        if (r0 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r1 = "android.permission.BLUETOOTH_ADMIN";
        r2 = "Need BLUETOOTH_ADMIN permission";
        r0.enforceCallingPermission(r1, r2);
        r0 = "BleScanWrapper";
        r0 = r8.getParcelable(r0);
        r0 = (android.bluetooth.BleScanWrapper) r0;
        r1 = "IBinder";
        r1 = r8.getBinder(r1);
        r2 = "uid";
        r2 = r8.getInt(r2);
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "stopLeScan: ";
        r3.append(r4);
        r3.append(r0);
        r4 = " uid=";
        r3.append(r4);
        r3.append(r2);
        r3.append(r0);
        r3 = r3.toString();
        r7.logdWithLocal(r3);
        r3 = r7.mBleLock;
        monitor-enter(r3);
        r4 = r7.mLeScanFeatureEnable;	 Catch:{ all -> 0x007a }
        if (r4 != 0) goto L_0x004a;
    L_0x0048:
        monitor-exit(r3);	 Catch:{ all -> 0x007a }
        return;
    L_0x004a:
        r4 = 0;
        r5 = r7.mClientMap;	 Catch:{ all -> 0x007a }
        r5 = r5.get(r0);	 Catch:{ all -> 0x007a }
        r5 = (com.miui.whetstone.PowerKeeperPolicy.Client) r5;	 Catch:{ all -> 0x007a }
        if (r5 == 0) goto L_0x0065;
    L_0x0055:
        r6 = r5.getAllowed();	 Catch:{ all -> 0x007a }
        r4 = r6;
        r5.unlinkToDeath();	 Catch:{ all -> 0x007a }
        r5.clearResource();	 Catch:{ all -> 0x007a }
        r6 = r7.mClientMap;	 Catch:{ all -> 0x007a }
        r6.remove(r0);	 Catch:{ all -> 0x007a }
    L_0x0065:
        r6 = r7.hasRestrictedScaner();	 Catch:{ all -> 0x007a }
        if (r6 == 0) goto L_0x0078;
    L_0x006b:
        r6 = r7.mParoleCheck;	 Catch:{ all -> 0x007a }
        r6 = r6.isWorking();	 Catch:{ all -> 0x007a }
        if (r6 != 0) goto L_0x0078;
    L_0x0073:
        r6 = r7.mParoleCheck;	 Catch:{ all -> 0x007a }
        r6.startParoleCheck();	 Catch:{ all -> 0x007a }
    L_0x0078:
        monitor-exit(r3);	 Catch:{ all -> 0x007a }
        return;
    L_0x007a:
        r4 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x007a }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.whetstone.PowerKeeperPolicy.stopLeScan(android.os.Bundle):void");
    }

    public void checkNetworkState(final String url, final int netId, final INetStateCallback callback) {
        new Thread(new Runnable() {
            /* JADX WARNING: Missing block: B:21:0x0093, code skipped:
            if (r5 != null) goto L_0x006d;
     */
            public void run() {
                /*
                r9 = this;
                r0 = "connect to ";
                r1 = 0;
                r2 = com.miui.whetstone.PowerKeeperPolicy.this;
                r2 = r2.mContext;
                r3 = "connectivity";
                r2 = r2.getSystemService(r3);
                r2 = (android.net.ConnectivityManager) r2;
                r3 = r2.getActiveNetwork();
                r4 = "PowerKeeperPolicy";
                if (r3 == 0) goto L_0x009c;
            L_0x0019:
                r5 = r3.netId;
                r6 = r4;
                if (r5 == r6) goto L_0x0021;
            L_0x001f:
                goto L_0x009c;
            L_0x0021:
                r5 = 0;
                r6 = new java.net.URL;	 Catch:{ IOException -> 0x0073 }
                r7 = r3;	 Catch:{ IOException -> 0x0073 }
                r6.<init>(r7);	 Catch:{ IOException -> 0x0073 }
                r6 = r6.openConnection();	 Catch:{ IOException -> 0x0073 }
                r6 = (java.net.HttpURLConnection) r6;	 Catch:{ IOException -> 0x0073 }
                r5 = r6;
                r6 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
                r5.setConnectTimeout(r6);	 Catch:{ IOException -> 0x0073 }
                r6 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
                r5.setReadTimeout(r6);	 Catch:{ IOException -> 0x0073 }
                r5.connect();	 Catch:{ IOException -> 0x0073 }
                r6 = r5.getResponseCode();	 Catch:{ IOException -> 0x0073 }
                r7 = 204; // 0xcc float:2.86E-43 double:1.01E-321;
                if (r6 == r7) goto L_0x0049;
            L_0x0045:
                r7 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
                if (r6 != r7) goto L_0x004a;
            L_0x0049:
                r1 = 1;
            L_0x004a:
                r7 = com.miui.whetstone.PowerKeeperPolicy.DEBUG;	 Catch:{ IOException -> 0x0073 }
                if (r7 == 0) goto L_0x006c;
            L_0x0050:
                r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0073 }
                r7.<init>();	 Catch:{ IOException -> 0x0073 }
                r7.append(r0);	 Catch:{ IOException -> 0x0073 }
                r8 = r3;	 Catch:{ IOException -> 0x0073 }
                r7.append(r8);	 Catch:{ IOException -> 0x0073 }
                r8 = " return ";
                r7.append(r8);	 Catch:{ IOException -> 0x0073 }
                r7.append(r6);	 Catch:{ IOException -> 0x0073 }
                r7 = r7.toString();	 Catch:{ IOException -> 0x0073 }
                android.util.Log.d(r4, r7);	 Catch:{ IOException -> 0x0073 }
            L_0x006d:
                r5.disconnect();
                goto L_0x009c;
            L_0x0071:
                r0 = move-exception;
                goto L_0x0096;
            L_0x0073:
                r6 = move-exception;
                r7 = com.miui.whetstone.PowerKeeperPolicy.DEBUG;	 Catch:{ all -> 0x0071 }
                if (r7 == 0) goto L_0x0093;
            L_0x007a:
                r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0071 }
                r7.<init>();	 Catch:{ all -> 0x0071 }
                r7.append(r0);	 Catch:{ all -> 0x0071 }
                r0 = r3;	 Catch:{ all -> 0x0071 }
                r7.append(r0);	 Catch:{ all -> 0x0071 }
                r0 = " failed";
                r7.append(r0);	 Catch:{ all -> 0x0071 }
                r0 = r7.toString();	 Catch:{ all -> 0x0071 }
                android.util.Log.d(r4, r0);	 Catch:{ all -> 0x0071 }
            L_0x0093:
                if (r5 == 0) goto L_0x009c;
            L_0x0095:
                goto L_0x006d;
            L_0x0096:
                if (r5 == 0) goto L_0x009b;
            L_0x0098:
                r5.disconnect();
            L_0x009b:
                throw r0;
            L_0x009c:
                r0 = r5;	 Catch:{ RemoteException -> 0x00a4 }
                r2 = r4;	 Catch:{ RemoteException -> 0x00a4 }
                r0.onCheckComplete(r1, r2);	 Catch:{ RemoteException -> 0x00a4 }
                goto L_0x00aa;
            L_0x00a4:
                r0 = move-exception;
                r2 = "callback powerkeeper fail";
                android.util.Log.e(r4, r2, r0);
            L_0x00aa:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.miui.whetstone.PowerKeeperPolicy$AnonymousClass3.run():void");
            }
        }).start();
    }

    private void logdWithLocal(String logStr) {
        if (DEBUG) {
            Log.d(TAG, logStr);
        }
        this.mLocalLog.log(logStr);
    }

    private void logeWithLocal(String logStr) {
        Log.e(TAG, logStr);
        this.mLocalLog.log(logStr);
    }

    private void dumpBleScan(FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.println("\n====ble scan====\n\t");
        this.mLocalLog.dump(fd, writer, args);
        StringBuilder sb1 = new StringBuilder(512);
        StringBuilder sb2 = new StringBuilder(512);
        StringBuilder dumpString = new StringBuilder(4096);
        synchronized (this.mBleLock) {
            int size = this.mUidAllow.size();
            for (int i = 0; i < size; i++) {
                if (this.mUidAllow.valueAt(i)) {
                    sb1.append(this.mUidAllow.keyAt(i));
                    sb1.append(',');
                } else {
                    sb2.append(this.mUidAllow.keyAt(i));
                    sb2.append(',');
                }
            }
            dumpString.append(" allow uids:");
            dumpString.append(sb1);
            dumpString.append(10);
            dumpString.append(" not allow uids:");
            dumpString.append(sb2);
            dumpString.append(10);
            sb1.setLength(0);
            sb2.setLength(0);
            size = this.mUidScanning.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (this.mUidScanning.valueAt(i2)) {
                    sb1.append(this.mUidScanning.keyAt(i2));
                    sb1.append(',');
                } else {
                    sb2.append(this.mUidScanning.keyAt(i2));
                    sb2.append(',');
                }
            }
            dumpString.append(" scanning uids:");
            dumpString.append(sb1);
            dumpString.append(10);
            dumpString.append(" not scanning uids:");
            dumpString.append(sb2);
            dumpString.append(10);
            dumpString.append(" mClientMap:");
            for (Entry<BleScanWrapper, Client> entry : this.mClientMap.entrySet()) {
                dumpString.append(entry.getValue());
                dumpString.append(',');
            }
            dumpString.append("\n mParoleCheck: mInParole=");
            dumpString.append(this.mParoleCheck.mInParole);
            dumpString.append(" state=");
            dumpString.append(this.mParoleCheck.state);
        }
        writer.println(dumpString.toString());
    }

    private void restoreFakeGpsStatus() {
        Log.d(TAG, "restore miui gps status");
        LocationPolicyManager.from(this.mContext).setPhoneStationary(false, null);
        LocationPolicyManager.from(this.mContext).setFakeGpsFeatureOnState(false);
    }

    private void restoreAlarm() {
        synchronized (this.mAlarmRestricts) {
            this.mAlarmRestricts.clear();
        }
    }
}
