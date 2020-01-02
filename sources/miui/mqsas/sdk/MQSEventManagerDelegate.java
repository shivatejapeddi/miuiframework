package miui.mqsas.sdk;

import android.content.pm.ParceledListSlice;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telecom.Logging.Session;
import android.util.Slog;
import java.util.List;
import miui.mqsas.IMQSService;
import miui.mqsas.IMQSService.Stub;
import miui.mqsas.sdk.event.AnrEvent;
import miui.mqsas.sdk.event.BootEvent;
import miui.mqsas.sdk.event.FeatureEvent;
import miui.mqsas.sdk.event.JavaExceptionEvent;
import miui.mqsas.sdk.event.PackageEvent;
import miui.mqsas.sdk.event.ScreenOnEvent;
import miui.mqsas.sdk.event.WatchdogEvent;

public class MQSEventManagerDelegate {
    private static boolean DEBUG = false;
    private static final String MQS_SERVICE_NAME = "miui.mqsas.MQSService";
    public static final String PROPERTY_POWER_OFF = "persist.sys.poweroff";
    private static final String TAG = MQSEventManagerDelegate.class.getSimpleName();
    private static MQSEventManagerDelegate sInstance = null;
    private int SHOULD_NOT_DUMPFD = 0;
    private int SHOULD_NOT_DUMPHEAP = 0;
    DeathRecipient mDeathHandler = new DeathRecipient() {
        public void binderDied() {
            Slog.w(MQSEventManagerDelegate.TAG, "Mqsas binderDied!");
            MQSEventManagerDelegate.this.mService = null;
        }
    };
    private IMQSService mService;

    public int checkIfNeedDumpheap(JavaExceptionEvent event) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("checkIfNeedDumpheap:");
            stringBuilder.append(event.toString());
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                return service.checkIfNeedDumpheap(event);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("checkIfNeedDumpheap error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
        return this.SHOULD_NOT_DUMPHEAP;
    }

    public int checkIfNeedDumpFd(JavaExceptionEvent event) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("checkIfNeedDumpFd:");
            stringBuilder.append(event.toString());
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                return service.checkIfNeedDumpFd(event);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("checkIfNeedDumpFd error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
        return this.SHOULD_NOT_DUMPFD;
    }

    public void reportAnrEvent(AnrEvent event) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportAnrEvent:");
            stringBuilder.append(event.toString());
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportAnrEvent(event);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportAnrEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportJavaExceptionEvent(JavaExceptionEvent event) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportJEEvent:");
            stringBuilder.append(event.toString());
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportJavaExceptionEvent(event);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportJEEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportWatchdogEvent(WatchdogEvent event) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportJWDTEvent:");
            stringBuilder.append(event.toString());
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportWatchdogEvent(event);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportJWDTEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportSimpleEvent(int type, String info) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportSimpleEvent:");
            stringBuilder.append(type);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(info);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportSimpleEvent(type, info);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportSimpleEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportScreenOnEvent(ScreenOnEvent event) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportScreenOnEvent: event =");
            stringBuilder.append(event);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportScreenOnEvent(event);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportScreenOnEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportConnectExceptionEvent(int type, int reason, String bssid) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportConnectExceptionEvent: ");
            stringBuilder.append(type);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(reason);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportConnectExceptionEvent(type, reason, bssid);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportConnectExceptionEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportPackageEvent(PackageEvent event) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportPackageEvent:");
            stringBuilder.append(event.toString());
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportPackageEvent(event);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportPackageEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportBootEvent(BootEvent event) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportBootEvent:");
            stringBuilder.append(event.toString());
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportBootEvent(event);
            }
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportBootEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportTelephonyEvent(int type, String info) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportTelephonyEvent:");
            stringBuilder.append(type);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(info);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportTelephonyEvent(type, info);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportTelephonyEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void onBootCompleted() {
        if (DEBUG) {
            Slog.i(TAG, "onBootCompleted");
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.onBootCompleted();
            }
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onBootCompleted error happened:");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
    }

    public void reportBroadcastEvent(ParceledListSlice reportEvents) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportBroadcastEvent:");
            stringBuilder.append(reportEvents.getList().toString());
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportBroadcastEvent(reportEvents);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportBroadcastEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportKillProcessEvents(ParceledListSlice events) {
        if (DEBUG) {
            Slog.i(TAG, "reportKillProcessEvents");
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportKillProcessEvents(events);
            }
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportKillProcessEvents error happened:");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
    }

    public void reportBluetoothEvent(int type, String info) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportBluetoothEvent:");
            stringBuilder.append(type);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(info);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportBluetoothEvent(type, info);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportBluetoothEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportEvent(String module, String info, boolean isGlobalNeed) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportEvent:");
            stringBuilder.append(module);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(info);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportEvent(module, info, isGlobalNeed);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportEventV2(String module, String info, String appId, boolean isGlobalNeed) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportEventV2:");
            stringBuilder.append(module);
            String str2 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            stringBuilder.append(str2);
            stringBuilder.append(info);
            stringBuilder.append(str2);
            stringBuilder.append(appId);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportEventV2(module, info, appId, isGlobalNeed);
            }
        } catch (Exception e) {
            String str3 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportEventV2 error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str3, stringBuilder2.toString());
        }
    }

    public void reportEvents(String module, List<String> info, boolean isGlobalNeed) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportEvents:");
            stringBuilder.append(module);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(info);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportEvents(module, info, isGlobalNeed);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportEvents error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportEventsV2(String module, List<String> infoList, String appId, boolean isGlobalNeed) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportEventsV2:");
            stringBuilder.append(module);
            String str2 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            stringBuilder.append(str2);
            stringBuilder.append(infoList);
            stringBuilder.append(str2);
            stringBuilder.append(appId);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportEventsV2(module, infoList, appId, isGlobalNeed);
            }
        } catch (Exception e) {
            String str3 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportEventsV2 error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str3, stringBuilder2.toString());
        }
    }

    public void reportXmsEvent(String module, String info) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportXmsEvent:");
            stringBuilder.append(module);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(info);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportXmsEvent(module, info);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("reportXmsEvent error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void reportFeatureEvent(FeatureEvent event) {
        if (DEBUG) {
            Slog.i(TAG, "reportFeatureEvent");
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportFeatureEvent(event);
            }
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportFeatureEvent error happened:");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
    }

    public String getOnlineRuleMatched(String module, String info) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getOnlineRuleMatched:");
            stringBuilder.append(module);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(info);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.getOnlineRuleMatched(module, info);
            }
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("getOnlineRuleMatched error happened:");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
        return null;
    }

    public void reportPackageForegroundEvents(ParceledListSlice events) {
        if (DEBUG) {
            Slog.i(TAG, "reportPackageForegroundEvents");
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportPackageForegroundEvents(events);
            }
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportPackageForegroundEvents error happened:");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
    }

    public void dumpBugReport() {
        if (DEBUG) {
            Slog.i(TAG, "dumpBugReport");
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.dumpBugReport();
            }
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("dumpBugReport error happened:");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
    }

    public void dialogButtonChecked(int operation, int eventType, String dgt, boolean isChecked) {
        if (DEBUG) {
            Slog.i(TAG, "dialogButtonChecked");
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.dialogButtonChecked(operation, eventType, dgt, isChecked);
            }
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportBrightnessEvents error happened:");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
    }

    public void setPowerOffTimeAndReason(String reason) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(reason);
        stringBuilder.append(Session.SESSION_SEPARATION_CHAR_CHILD);
        stringBuilder.append(System.currentTimeMillis());
        SystemProperties.set(PROPERTY_POWER_OFF, stringBuilder.toString());
    }

    public void reportHangException() {
        if (DEBUG) {
            Slog.i(TAG, "reportHangExceptionEvents");
        }
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportHangExceptionEvents();
            }
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportHangExceptionEvents error happened: ");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
    }

    public static synchronized MQSEventManagerDelegate getInstance() {
        MQSEventManagerDelegate mQSEventManagerDelegate;
        synchronized (MQSEventManagerDelegate.class) {
            if (sInstance == null) {
                sInstance = new MQSEventManagerDelegate();
            }
            mQSEventManagerDelegate = sInstance;
        }
        return mQSEventManagerDelegate;
    }

    private MQSEventManagerDelegate() {
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized IMQSService getMQSService() {
        if (this.mService == null) {
            this.mService = Stub.asInterface(ServiceManager.getService(MQS_SERVICE_NAME));
            if (this.mService != null) {
                try {
                    this.mService.asBinder().linkToDeath(this.mDeathHandler, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Slog.e(TAG, "failed to get MQSService.");
            }
        }
        return this.mService;
    }

    public void reportBrightnessEvent(int startOrEnd, int progress, int autoBrightnessEnabled, String extra) {
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportBrightnessEvent(startOrEnd, progress, autoBrightnessEnabled, extra);
            }
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportBrightnessEvents error happened:");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
    }

    public void reportBrightnessEvent(Bundle event) {
        try {
            IMQSService service = getMQSService();
            if (service != null) {
                service.reportBrightnessEventV2(event);
            }
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reportBrightnessEvents error happened:");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
    }
}
