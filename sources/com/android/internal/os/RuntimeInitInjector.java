package com.android.internal.os;

import android.app.ApplicationErrorReport.CrashInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Slog;
import miui.mqsas.fdmonitor.FdInfoManager;
import miui.mqsas.oom.OOMEventManager;
import miui.mqsas.sdk.MQSEventManagerDelegate;
import miui.mqsas.sdk.event.JavaExceptionEvent;

public class RuntimeInitInjector {
    public static String getDefaultUserAgent() {
        String model;
        StringBuilder result = new StringBuilder(64);
        result.append("Dalvik/");
        result.append(System.getProperty("java.vm.version"));
        result.append(" (Linux; U; Android ");
        String version = VERSION.RELEASE;
        result.append(version.length() > 0 ? version : "1.0");
        if ("REL".equals(VERSION.CODENAME)) {
            model = Build.MODEL;
            if (model.length() > 0) {
                result.append("; ");
                result.append(model);
            }
        }
        model = VERSION.INCREMENTAL;
        if (model.length() > 0) {
            result.append(" MIUI/");
            result.append(model);
        }
        result.append(")");
        return result.toString();
    }

    public static void onJE(int pid, String processName, String pknName, String threadName, Throwable e, String prefix, boolean isSystem) {
        CrashInfo crashInfo = new CrashInfo(e);
        JavaExceptionEvent event = new JavaExceptionEvent();
        event.setPid(pid);
        event.setProcessName(processName);
        event.setPackageName(pknName);
        event.setTimeStamp(System.currentTimeMillis());
        event.setThreadName(threadName);
        event.setPrefix(prefix);
        event.setSystem(isSystem);
        event.setStackTrace(crashInfo.stackTrace);
        event.setExceptionClassName(crashInfo.exceptionClassName);
        event.setExceptionMessage(crashInfo.exceptionMessage);
        if (FdInfoManager.isInterestedFdLeakEvent(event, null)) {
            FdInfoManager.checkEventAndDumpFd(event, pid);
        }
        if (OOMEventManager.isInterestedOOMEvent(event)) {
            int result = OOMEventManager.checkEventAndDumpheap(event, pknName);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("checkEventAndDumpheap result : ");
            stringBuilder.append(result);
            String str = "RuntimeInjector";
            Slog.d(str, stringBuilder.toString());
            if (result == 1) {
                event.setJeCategroy(JavaExceptionEvent.CATEGROY_JE_OOM_NEED_DUMPHEAP);
            } else if (result == -1) {
                Slog.d(str, "oom too noisy return, no need onJE.");
                return;
            }
        }
        MQSEventManagerDelegate.getInstance().reportJavaExceptionEvent(event);
    }
}
