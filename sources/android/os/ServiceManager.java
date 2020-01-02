package android.os;

import android.annotation.UnsupportedAppUsage;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.BinderInternal;
import com.android.internal.util.StatLogger;
import java.util.Map;

public final class ServiceManager {
    private static final int GET_SERVICE_LOG_EVERY_CALLS_CORE = SystemProperties.getInt("debug.servicemanager.log_calls_core", 100);
    private static final int GET_SERVICE_LOG_EVERY_CALLS_NON_CORE = SystemProperties.getInt("debug.servicemanager.log_calls", 200);
    private static final long GET_SERVICE_SLOW_THRESHOLD_US_CORE = ((long) (SystemProperties.getInt("debug.servicemanager.slow_call_core_ms", 10) * 1000));
    private static final long GET_SERVICE_SLOW_THRESHOLD_US_NON_CORE = ((long) (SystemProperties.getInt("debug.servicemanager.slow_call_ms", 50) * 1000));
    private static final int SLOW_LOG_INTERVAL_MS = 5000;
    private static final int STATS_LOG_INTERVAL_MS = 5000;
    private static final String TAG = "ServiceManager";
    @UnsupportedAppUsage
    private static Map<String, IBinder> sCache = new ArrayMap();
    @GuardedBy({"sLock"})
    private static int sGetServiceAccumulatedCallCount;
    @GuardedBy({"sLock"})
    private static int sGetServiceAccumulatedUs;
    @GuardedBy({"sLock"})
    private static long sLastSlowLogActualTime;
    @GuardedBy({"sLock"})
    private static long sLastSlowLogUptime;
    @GuardedBy({"sLock"})
    private static long sLastStatsLogUptime;
    private static final Object sLock = new Object();
    @UnsupportedAppUsage
    private static IServiceManager sServiceManager;
    public static final StatLogger sStatLogger = new StatLogger(new String[]{"getService()"});

    public static class ServiceNotFoundException extends Exception {
        public ServiceNotFoundException(String name) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No service published for: ");
            stringBuilder.append(name);
            super(stringBuilder.toString());
        }
    }

    interface Stats {
        public static final int COUNT = 1;
        public static final int GET_SERVICE = 0;
    }

    @UnsupportedAppUsage
    private static IServiceManager getIServiceManager() {
        IServiceManager iServiceManager = sServiceManager;
        if (iServiceManager != null) {
            return iServiceManager;
        }
        sServiceManager = ServiceManagerNative.asInterface(Binder.allowBlocking(BinderInternal.getContextObject()));
        return sServiceManager;
    }

    @UnsupportedAppUsage
    public static IBinder getService(String name) {
        try {
            IBinder service = (IBinder) sCache.get(name);
            if (service != null) {
                return service;
            }
            return Binder.allowBlocking(rawGetService(name));
        } catch (RemoteException e) {
            Log.e(TAG, "error in getService", e);
            return null;
        }
    }

    public static IBinder getServiceOrThrow(String name) throws ServiceNotFoundException {
        IBinder binder = getService(name);
        if (binder != null) {
            return binder;
        }
        throw new ServiceNotFoundException(name);
    }

    @UnsupportedAppUsage
    public static void addService(String name, IBinder service) {
        addService(name, service, false, 8);
    }

    @UnsupportedAppUsage
    public static void addService(String name, IBinder service, boolean allowIsolated) {
        addService(name, service, allowIsolated, 8);
    }

    @UnsupportedAppUsage
    public static void addService(String name, IBinder service, boolean allowIsolated, int dumpPriority) {
        try {
            getIServiceManager().addService(name, service, allowIsolated, dumpPriority);
        } catch (RemoteException e) {
            Log.e(TAG, "error in addService", e);
        }
    }

    @UnsupportedAppUsage
    public static IBinder checkService(String name) {
        try {
            IBinder service = (IBinder) sCache.get(name);
            if (service != null) {
                return service;
            }
            return Binder.allowBlocking(getIServiceManager().checkService(name));
        } catch (RemoteException e) {
            Log.e(TAG, "error in checkService", e);
            return null;
        }
    }

    @UnsupportedAppUsage
    public static String[] listServices() {
        try {
            return getIServiceManager().listServices(15);
        } catch (RemoteException e) {
            Log.e(TAG, "error in listServices", e);
            return null;
        }
    }

    public static void initServiceCache(Map<String, IBinder> cache) {
        if (sCache.size() == 0) {
            sCache.putAll(cache);
            return;
        }
        throw new IllegalStateException("setServiceCache may only be called once");
    }

    /* JADX WARNING: Missing block: B:32:0x0090, code skipped:
            return r4;
     */
    private static android.os.IBinder rawGetService(java.lang.String r19) throws android.os.RemoteException {
        /*
        r1 = r19;
        r0 = sStatLogger;
        r2 = r0.getTime();
        r0 = getIServiceManager();
        r4 = r0.getService(r1);
        r0 = sStatLogger;
        r5 = 0;
        r6 = r0.logDurationStat(r5, r2);
        r6 = (int) r6;
        r7 = android.os.Process.myUid();
        r8 = android.os.UserHandle.isCore(r7);
        if (r8 == 0) goto L_0x0025;
    L_0x0022:
        r9 = GET_SERVICE_SLOW_THRESHOLD_US_CORE;
        goto L_0x0027;
    L_0x0025:
        r9 = GET_SERVICE_SLOW_THRESHOLD_US_NON_CORE;
        r11 = sLock;
        monitor-enter(r11);
        r0 = sGetServiceAccumulatedUs;	 Catch:{ all -> 0x0091 }
        r0 = r0 + r6;
        sGetServiceAccumulatedUs = r0;	 Catch:{ all -> 0x0091 }
        r0 = sGetServiceAccumulatedCallCount;	 Catch:{ all -> 0x0091 }
        r0 = r0 + 1;
        sGetServiceAccumulatedCallCount = r0;	 Catch:{ all -> 0x0091 }
        r12 = android.os.SystemClock.uptimeMillis();	 Catch:{ all -> 0x0091 }
        r14 = (long) r6;
        r0 = (r14 > r9 ? 1 : (r14 == r9 ? 0 : -1));
        r14 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        if (r0 < 0) goto L_0x005f;
    L_0x0041:
        r16 = sLastSlowLogUptime;	 Catch:{ all -> 0x005b }
        r16 = r16 + r14;
        r0 = (r12 > r16 ? 1 : (r12 == r16 ? 0 : -1));
        if (r0 > 0) goto L_0x0050;
    L_0x0049:
        r16 = sLastSlowLogActualTime;	 Catch:{ all -> 0x005b }
        r14 = (long) r6;	 Catch:{ all -> 0x005b }
        r0 = (r16 > r14 ? 1 : (r16 == r14 ? 0 : -1));
        if (r0 >= 0) goto L_0x005f;
    L_0x0050:
        r0 = r6 / 1000;
        android.os.EventLogTags.writeServiceManagerSlow(r0, r1);	 Catch:{ all -> 0x005b }
        sLastSlowLogUptime = r12;	 Catch:{ all -> 0x005b }
        r14 = (long) r6;	 Catch:{ all -> 0x005b }
        sLastSlowLogActualTime = r14;	 Catch:{ all -> 0x005b }
        goto L_0x005f;
    L_0x005b:
        r0 = move-exception;
        r18 = r6;
        goto L_0x0094;
    L_0x005f:
        if (r8 == 0) goto L_0x0064;
    L_0x0061:
        r0 = GET_SERVICE_LOG_EVERY_CALLS_CORE;	 Catch:{ all -> 0x005b }
        goto L_0x0066;
    L_0x0064:
        r0 = GET_SERVICE_LOG_EVERY_CALLS_NON_CORE;	 Catch:{ all -> 0x0091 }
        r14 = sGetServiceAccumulatedCallCount;	 Catch:{ all -> 0x0091 }
        if (r14 < r0) goto L_0x008d;
    L_0x006b:
        r14 = sLastStatsLogUptime;	 Catch:{ all -> 0x0091 }
        r16 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r14 = r14 + r16;
        r14 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));
        if (r14 < 0) goto L_0x008d;
    L_0x0075:
        r14 = sGetServiceAccumulatedCallCount;	 Catch:{ all -> 0x0091 }
        r15 = sGetServiceAccumulatedUs;	 Catch:{ all -> 0x0091 }
        r15 = r15 / 1000;
        r16 = sLastStatsLogUptime;	 Catch:{ all -> 0x0091 }
        r18 = r6;
        r5 = r12 - r16;
        r5 = (int) r5;
        android.os.EventLogTags.writeServiceManagerStats(r14, r15, r5);	 Catch:{ all -> 0x0096 }
        r5 = 0;
        sGetServiceAccumulatedCallCount = r5;	 Catch:{ all -> 0x0096 }
        sGetServiceAccumulatedUs = r5;	 Catch:{ all -> 0x0096 }
        sLastStatsLogUptime = r12;	 Catch:{ all -> 0x0096 }
        goto L_0x008f;
    L_0x008d:
        r18 = r6;
    L_0x008f:
        monitor-exit(r11);	 Catch:{ all -> 0x0096 }
        return r4;
    L_0x0091:
        r0 = move-exception;
        r18 = r6;
    L_0x0094:
        monitor-exit(r11);	 Catch:{ all -> 0x0096 }
        throw r0;
    L_0x0096:
        r0 = move-exception;
        goto L_0x0094;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.ServiceManager.rawGetService(java.lang.String):android.os.IBinder");
    }
}
