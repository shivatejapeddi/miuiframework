package miui.mqsas.sdk;

import android.app.job.JobInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.util.Slog;
import miui.mqsas.sdk.event.BootEvent;
import org.json.JSONException;
import org.json.JSONObject;

public class BootEventManager {
    private static final int DELAY_TIME = 10000;
    private static final String RUNTIME_REBOOT_PROPERTIY = "sys.miui.runtime.reboot";
    private static final String TAG = BootEventManager.class.getSimpleName();
    private static BootEventManager sInstance = null;
    private int bootType = 1;
    private int dexoptSysAppCnt = 0;
    private int dexoptThirdAppCnt = 0;
    private int persistAppCount = 0;
    private long phaseAmsReady = 0;
    private long phaseBootComplete = 0;
    private long phaseBootDexopt = 0;
    private long phaseCoreAppDexopt = 0;
    private long phasePmsScanEnd = 0;
    private long phasePmsScanStart = 0;
    private long phaseSystemRun = 0;
    private long phaseUIReady = 0;
    private long phaseZygotePreload = 0;
    private int prebootAppCount = 0;
    private int systemAppCount = 0;
    private int thirdAppCount = 0;

    public static synchronized BootEventManager getInstance() {
        BootEventManager bootEventManager;
        synchronized (BootEventManager.class) {
            if (sInstance == null) {
                sInstance = new BootEventManager();
            }
            bootEventManager = sInstance;
        }
        return bootEventManager;
    }

    private BootEventManager() {
    }

    public static void reportBootEvent() {
        BootEventManager manager = getInstance();
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("systemRun:");
        stringBuilder.append(manager.getSystemRun());
        stringBuilder.append(",zygotePreload:");
        stringBuilder.append(manager.getZygotePreload());
        stringBuilder.append(",pmsScan:");
        stringBuilder.append((manager.getPmsScanEnd() - manager.getPmsScanStart()) - manager.getCoreAppDexopt());
        stringBuilder.append(",bootDexopt:");
        stringBuilder.append(manager.getBootDexopt());
        stringBuilder.append(",coreAppDexopt:");
        stringBuilder.append(manager.getCoreAppDexopt());
        stringBuilder.append(",amsReady:");
        stringBuilder.append((manager.getAmsReady() - manager.getPmsScanEnd()) - manager.getBootDexopt());
        stringBuilder.append(",UIReady:");
        stringBuilder.append(manager.getUIReady() - manager.getAmsReady());
        stringBuilder.append(",bootComplete:");
        stringBuilder.append(manager.getBootComplete());
        Slog.d(str, stringBuilder.toString());
        final BootEvent event = manager.getBootEvent();
        Slog.d(TAG, event.toString());
        if (SystemProperties.getInt(RUNTIME_REBOOT_PROPERTIY, -1) < 1 || event.getBootType() != 1) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    Slog.d(BootEventManager.TAG, "Begin to report boot event");
                    MQSEventManagerDelegate.getInstance().reportBootEvent(event);
                }
            }, JobInfo.MIN_BACKOFF_MILLIS);
        } else {
            Slog.d(TAG, "Abnormal boot event, filter it");
        }
    }

    private BootEvent getBootEvent() {
        BootEvent event = new BootEvent();
        event.setType(128);
        event.setTimeStamp(System.currentTimeMillis());
        event.setBootType(getBootType());
        event.setPeriodSystemRun(getSystemRun());
        event.setPeriodPmsScan((getPmsScanEnd() - getPmsScanStart()) - getCoreAppDexopt());
        event.setPeriodDexopt(getBootDexopt() + getCoreAppDexopt());
        event.setPeriodAmsReady((getAmsReady() - getPmsScanEnd()) - getBootDexopt());
        event.setPeriodUIReady(getUIReady() - getAmsReady());
        event.setPeriodBootComplete(getBootComplete());
        event.setDetailSystemRun(createJsonObject(null, "zygotePreload", getZygotePreload()).toString());
        event.setDetailPmsScan(createJsonObject(createJsonObject(null, "sysAppCnt", (long) getSystemAppCount()), "thirdAppCnt", (long) getThirdAppCount()).toString());
        event.setDetailDexopt(createJsonObject(createJsonObject(null, "optSysAppCnt", (long) getDexoptSystemAppCount()), "optThirdAppCnt", (long) getDexoptThirdAppCount()).toString());
        event.setDetailAmsReady(createJsonObject(null, "prebootAppCnt", (long) getPrebootAppCount()).toString());
        event.setDetailUIReady(createJsonObject(null, "persistAppCnt", (long) getPersistAppCount()).toString());
        return event;
    }

    private static JSONObject createJsonObject(JSONObject obj, String key, long value) {
        obj = obj != null ? obj : new JSONObject();
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(value);
            obj.put(key, stringBuilder.toString());
        } catch (JSONException e) {
            String str = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("create jason object exception for ");
            stringBuilder2.append(key);
            stringBuilder2.append(", ");
            stringBuilder2.append(e);
            Slog.e(str, stringBuilder2.toString());
        }
        return obj;
    }

    public int getBootType() {
        return this.bootType;
    }

    public void setBootType(int bootType) {
        this.bootType = bootType;
    }

    public long getSystemRun() {
        return this.phaseSystemRun;
    }

    public void setSystemRun(long phaseSystemRun) {
        this.phaseSystemRun = phaseSystemRun;
    }

    public long getZygotePreload() {
        return this.phaseZygotePreload;
    }

    public void setZygotePreload(long phaseZygotePreload) {
        this.phaseZygotePreload = phaseZygotePreload;
    }

    public long getPmsScanStart() {
        return this.phasePmsScanStart;
    }

    public void setPmsScanStart(long phasePmsScanStart) {
        this.phasePmsScanStart = phasePmsScanStart;
    }

    public long getPmsScanEnd() {
        return this.phasePmsScanEnd;
    }

    public void setPmsScanEnd(long phasePmsScanEnd) {
        this.phasePmsScanEnd = phasePmsScanEnd;
    }

    public long getBootDexopt() {
        return this.phaseBootDexopt;
    }

    public void setBootDexopt(long phaseBootDexopt) {
        this.phaseBootDexopt = phaseBootDexopt;
    }

    public long getAmsReady() {
        return this.phaseAmsReady;
    }

    public void setAmsReady(long phaseAmsReady) {
        this.phaseAmsReady = phaseAmsReady;
    }

    public long getUIReady() {
        return this.phaseUIReady;
    }

    public void setUIReady(long phaseUIReady) {
        this.phaseUIReady = phaseUIReady;
    }

    public long getBootComplete() {
        return this.phaseBootComplete;
    }

    public void setBootComplete(long phaseBootComplete) {
        this.phaseBootComplete = phaseBootComplete;
    }

    public int getSystemAppCount() {
        return this.systemAppCount;
    }

    public void setSystemAppCount(int systemAppCount) {
        this.systemAppCount = systemAppCount;
    }

    public int getThirdAppCount() {
        return this.thirdAppCount;
    }

    public void setThirdAppCount(int thirdAppCount) {
        this.thirdAppCount = thirdAppCount;
    }

    public int getPrebootAppCount() {
        return this.prebootAppCount;
    }

    public void setPrebootAppCount(int prebootAppCount) {
        this.prebootAppCount = prebootAppCount;
    }

    public int getPersistAppCount() {
        return this.persistAppCount;
    }

    public void setPersistAppCount(int persistAppCount) {
        this.persistAppCount = persistAppCount;
    }

    public long getCoreAppDexopt() {
        return this.phaseCoreAppDexopt;
    }

    public void setCoreAppDexopt(long phaseCoreAppDexopt) {
        this.phaseCoreAppDexopt = phaseCoreAppDexopt;
    }

    public void setDexoptSystemAppCount(int dexoptSysAppCnt) {
        this.dexoptSysAppCnt = dexoptSysAppCnt;
    }

    public int getDexoptSystemAppCount() {
        return this.dexoptSysAppCnt;
    }

    public void setDexoptThirdAppCount(int dexoptThirdAppCnt) {
        this.dexoptThirdAppCnt = dexoptThirdAppCnt;
    }

    public int getDexoptThirdAppCount() {
        return this.dexoptThirdAppCnt;
    }
}
