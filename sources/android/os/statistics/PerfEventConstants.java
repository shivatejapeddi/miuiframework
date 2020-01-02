package android.os.statistics;

import android.util.SparseArray;
import java.util.HashMap;

public class PerfEventConstants {
    private static final HashMap<String, Integer> EVENT_TYPE_CODE_MAPPING = new HashMap();
    private static final SparseArray<String> EVENT_TYPE_MAPPING = new SparseArray();
    public static final String FIELD_BEGIN_TIME = "beginTime";
    public static final String FIELD_END_REAL_TIME = "endRealTime";
    public static final String FIELD_END_TIME = "endTime";
    public static final String FIELD_EVENT_FLAGS = "eventFlags";
    public static final String FIELD_EVENT_SEQ = "seq";
    public static final String FIELD_EVENT_TYPE = "eventType";
    public static final String FIELD_EVENT_TYPE_NAME = "eventTypeName";
    public static final String FIELD_OCCUR_TIME = "occurTime";
    public static final String FIELD_PACKAGE_NAME = "packageName";
    public static final String FIELD_PID = "pid";
    public static final String FIELD_PROCESS_NAME = "processName";
    public static final String FIELD_RUNNABLE_TIME = "runnableTime";
    public static final String FIELD_RUNNING_TIME = "runningTime";
    public static final String FIELD_SCHED_POLICY = "policy";
    public static final String FIELD_SCHED_PRIORITY = "priority";
    public static final String FIELD_SLEEPING_TIME = "sleepingTime";
    public static final String FIELD_THREAD_ID = "threadId";
    public static final String FIELD_THREAD_NAME = "threadName";
    public static final int FLAG_BLOCKED = 256;
    public static final int FLAG_BLOCKED_BY_CROSS_PROCESS = 1024;
    public static final int FLAG_BLOCKED_BY_MULTIBPLE_BLOCKER = 16384;
    public static final int FLAG_BLOCKED_BY_ONE_COINCIDED_BLOCKER = 4096;
    public static final int FLAG_BLOCKED_BY_ONE_INCLUSIVE_BLOCKER = 2048;
    public static final int FLAG_BLOCKED_BY_ONE_OVERLAPPED_BLOCKER = 8192;
    public static final int FLAG_BLOCKED_BY_SAME_PROCESS = 512;
    public static final int FLAG_BLOCKER = 65536;
    public static final int FLAG_BLOCKER_TO_CROSS_PROCESS = 262144;
    public static final int FLAG_BLOCKER_TO_SAME_PROCESS = 131072;
    public static final int FLAG_DETAILS_SOURCE_MASK = 251658240;
    public static final int FLAG_HAS_KERNEL_LAZYINFO = 64;
    public static final int FLAG_HAS_PROC_LAZYINFO = 16;
    public static final int FLAG_INITIATOR_POSITION_MASK = 6;
    public static final int FLAG_INITIATOR_POSITION_MASTER = 4;
    public static final int FLAG_INITIATOR_POSITION_SLAVE = 2;
    public static final int FLAG_INITIATOR_POSITION_UNKNOWN = 0;
    public static final int FLAG_KERNEL_LAZYINFO_RESOLVED = 128;
    public static final int FLAG_NO_CHECK = 268435456;
    public static final int FLAG_PROC_LAZYINFO_RESOLVED = 32;
    public static final int FLAG_ROOT_EVENT = 1;
    public static final int FLAG_USER_PERCEPTIBLE = 1048576;
    public static final int MACRO_EVENT_TYPE_COUNT = 7;
    public static final int MACRO_EVENT_TYPE_START = 65536;
    public static final int MAX_PERF_EVENT_PARCEL_SIZE = 4096;
    public static final int MICRO_EVENT_TYPE_COUNT = 16;
    public static final int MICRO_EVENT_TYPE_START = 0;
    public static final int TYPE_BINDER_STARVATION = 65540;
    public static final int TYPE_E2ESCENARIO_ONCE = 65541;
    public static final int TYPE_LOOPER_CHECKPOINT = 12;
    public static final int TYPE_LOOPER_ONCE = 11;
    public static final int TYPE_MEMORY_SLOWPATH = 65542;
    public static final int TYPE_MM_SLOWPATH = 15;
    public static final int TYPE_PLACE_HOLDER_1 = 65537;
    public static final int TYPE_PLACE_HOLDER_2 = 65539;
    public static final int TYPE_SCHED_WAIT = 13;
    public static final int TYPE_SCHED_WAKE = 14;
    public static final int TYPE_SINGLE_BINDER_CALL = 4;
    public static final int TYPE_SINGLE_BINDER_EXECUTION = 5;
    public static final int TYPE_SINGLE_CONDITION_AWAKEN = 2;
    public static final int TYPE_SINGLE_CONDITION_WAIT = 3;
    public static final int TYPE_SINGLE_EVENT_LOG_ITEM = 65536;
    public static final int TYPE_SINGLE_INPUT_EVENT = 9;
    public static final int TYPE_SINGLE_JANK_RECORD = 65538;
    public static final int TYPE_SINGLE_JNI_METHOD = 10;
    public static final int TYPE_SINGLE_LOCK_HOLD = 0;
    public static final int TYPE_SINGLE_LOCK_WAIT = 1;
    public static final int TYPE_SINGLE_LOOPER_MESSAGE = 8;
    public static final int TYPE_SINGLE_SYSTEM_TRACE_EVENT = 7;
    public static final int TYPE_SINGLE_TRACE_POINT = 6;

    static {
        String str = "LockHold";
        EVENT_TYPE_MAPPING.put(0, str);
        String str2 = "LockWait";
        EVENT_TYPE_MAPPING.put(1, str2);
        String str3 = "ConditionAwaken";
        EVENT_TYPE_MAPPING.put(2, str3);
        String str4 = "ConditionWait";
        EVENT_TYPE_MAPPING.put(3, str4);
        String str5 = "BinderCall";
        EVENT_TYPE_MAPPING.put(4, str5);
        String str6 = "BinderExecution";
        EVENT_TYPE_MAPPING.put(5, str6);
        String str7 = "TracePoint";
        EVENT_TYPE_MAPPING.put(6, str7);
        EVENT_TYPE_MAPPING.put(7, "SystemTraceEvent");
        EVENT_TYPE_MAPPING.put(8, "LooperMessage");
        EVENT_TYPE_MAPPING.put(9, "InputEvent");
        EVENT_TYPE_MAPPING.put(10, "JniMethod");
        EVENT_TYPE_MAPPING.put(11, "LooperOnce");
        EVENT_TYPE_MAPPING.put(12, "LooperCheckPoint");
        EVENT_TYPE_MAPPING.put(13, "SchedWait");
        EVENT_TYPE_MAPPING.put(14, "SchedWake");
        EVENT_TYPE_MAPPING.put(15, "MMSlowpath");
        EVENT_TYPE_MAPPING.put(65536, "EventLog");
        EVENT_TYPE_MAPPING.put(65538, "JankRecord");
        EVENT_TYPE_MAPPING.put(65540, "BinderStarvation");
        EVENT_TYPE_MAPPING.put(65541, "E2EScenarioOnce");
        EVENT_TYPE_MAPPING.put(65542, "MemorySlowpath");
        EVENT_TYPE_CODE_MAPPING.put(str, Integer.valueOf(0));
        EVENT_TYPE_CODE_MAPPING.put(str2, Integer.valueOf(1));
        EVENT_TYPE_CODE_MAPPING.put(str3, Integer.valueOf(2));
        EVENT_TYPE_CODE_MAPPING.put(str4, Integer.valueOf(3));
        EVENT_TYPE_CODE_MAPPING.put(str5, Integer.valueOf(4));
        EVENT_TYPE_CODE_MAPPING.put(str6, Integer.valueOf(5));
        EVENT_TYPE_CODE_MAPPING.put(str7, Integer.valueOf(6));
        EVENT_TYPE_CODE_MAPPING.put("SystemTraceEvent", Integer.valueOf(7));
        EVENT_TYPE_CODE_MAPPING.put("LooperMessage", Integer.valueOf(8));
        EVENT_TYPE_CODE_MAPPING.put("InputEvent", Integer.valueOf(9));
        EVENT_TYPE_CODE_MAPPING.put("JniMethod", Integer.valueOf(10));
        EVENT_TYPE_CODE_MAPPING.put("LooperOnce", Integer.valueOf(11));
        EVENT_TYPE_CODE_MAPPING.put("LooperCheckPoint", Integer.valueOf(12));
        EVENT_TYPE_CODE_MAPPING.put("SchedWait", Integer.valueOf(13));
        EVENT_TYPE_CODE_MAPPING.put("SchedWake", Integer.valueOf(14));
        EVENT_TYPE_CODE_MAPPING.put("MMSlowpath", Integer.valueOf(15));
        EVENT_TYPE_CODE_MAPPING.put("EventLog", Integer.valueOf(65536));
        EVENT_TYPE_CODE_MAPPING.put("JankRecord", Integer.valueOf(65538));
        EVENT_TYPE_CODE_MAPPING.put("BinderStarvation", Integer.valueOf(65540));
        EVENT_TYPE_CODE_MAPPING.put("E2EScenarioOnce", Integer.valueOf(65541));
        EVENT_TYPE_CODE_MAPPING.put("MemorySlowpath", Integer.valueOf(65542));
    }

    public static String getTypeName(int eventType) {
        return (String) EVENT_TYPE_MAPPING.get(eventType, "UnKnown");
    }

    public static int getTypeCode(String typeName) {
        if (EVENT_TYPE_CODE_MAPPING.containsKey(typeName)) {
            return ((Integer) EVENT_TYPE_CODE_MAPPING.get(typeName)).intValue();
        }
        return -1;
    }

    public static boolean isBlockedBy(int blockedEventType, int blockerEventType) {
        boolean z = false;
        if (blockedEventType == 1) {
            if (blockerEventType == 0) {
                z = true;
            }
            return z;
        } else if (blockedEventType == 13) {
            if (blockerEventType == 14) {
                z = true;
            }
            return z;
        } else if (blockedEventType == 3) {
            if (blockerEventType == 2) {
                z = true;
            }
            return z;
        } else if (blockedEventType != 4) {
            return false;
        } else {
            if (blockerEventType == 5) {
                z = true;
            }
            return z;
        }
    }
}
