package android.os.statistics;

import android.os.statistics.BinderSuperviser.BinderStarvation;
import android.os.statistics.BinderSuperviser.SingleBinderCall;
import android.os.statistics.BinderSuperviser.SingleBinderExecution;
import android.os.statistics.EventLogSuperviser.SingleEventLogItem;
import android.os.statistics.LooperMessageSuperviser.SingleLooperMessage;
import android.os.statistics.MemorySuperviser.Slowpath;
import android.os.statistics.MonitorSuperviser.SingleConditionAwaken;
import android.os.statistics.MonitorSuperviser.SingleConditionWait;
import android.os.statistics.MonitorSuperviser.SingleLockHold;
import android.os.statistics.MonitorSuperviser.SingleLockWait;
import android.os.statistics.SchedSuperviser.SchedWait;
import android.os.statistics.SchedSuperviser.SchedWake;

public class PerfEventFactory {
    private static final int MACRO_EVENT_TYPE_INDEX_OFFSET = -65520;

    public static PerfEvent createPerfEvent(int eventType, int eventFlags, long beginUptimeMillis, long endUptimeMillis, long inclusionId, long synchronizationId, long eventSeq, JniParcel dataParcel, Class[] javaStackTraceClasses, StackTraceElement[] javaStackTraceElements, NativeBackTrace nativeBackTrace) {
        PerfEvent perfEvent = createPerfEvent(eventType);
        if (perfEvent != null) {
            perfEvent.fillIn(eventFlags, beginUptimeMillis, endUptimeMillis, inclusionId, synchronizationId, eventSeq, dataParcel, javaStackTraceClasses, javaStackTraceElements, nativeBackTrace);
        }
        return perfEvent;
    }

    public static PerfEvent createPerfEvent(int eventType, int eventFlags, long beginUptimeMillis, long endUptimeMillis, long inclusionId, long synchronizationId, long eventSeq) {
        PerfEvent perfEvent = createPerfEvent(eventType);
        if (perfEvent != null) {
            perfEvent.eventFlags = eventFlags;
            perfEvent.beginUptimeMillis = beginUptimeMillis;
            perfEvent.endUptimeMillis = endUptimeMillis;
            perfEvent.inclusionId = inclusionId;
            perfEvent.synchronizationId = synchronizationId;
            perfEvent.eventSeq = eventSeq;
        }
        return perfEvent;
    }

    public static PerfEvent createPerfEvent(int eventType) {
        switch (eventType < 65536 ? eventType : MACRO_EVENT_TYPE_INDEX_OFFSET + eventType) {
            case 0:
                return new SingleLockHold();
            case 1:
                return new SingleLockWait();
            case 2:
                return new SingleConditionAwaken();
            case 3:
                return new SingleConditionWait();
            case 4:
                return new SingleBinderCall();
            case 5:
                return new SingleBinderExecution();
            case 6:
                return null;
            case 7:
                return null;
            case 8:
                return new SingleLooperMessage();
            case 9:
                return null;
            case 10:
                return new SingleJniMethod();
            case 11:
                return new LooperOnce();
            case 12:
                return new LooperCheckPoint();
            case 13:
                return new SchedWait();
            case 14:
                return new SchedWake();
            case 15:
                return new Slowpath();
            case 16:
                return new SingleEventLogItem();
            case 18:
                return new SingleJankRecord();
            case 20:
                return new BinderStarvation();
            case 21:
                return new E2EScenarioOnce();
            case 22:
                return null;
            default:
                return null;
        }
    }
}
