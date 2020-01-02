package android.os.statistics;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.statistics.MicroscopicEvent.BlockedEventFields;
import android.os.statistics.MicroscopicEvent.BlockerEventFields;
import org.json.JSONException;
import org.json.JSONObject;

public class MonitorSuperviser {
    public static final int COMDITION_AWAKEN_INTERRUPTED = 2;
    public static final int COMDITION_AWAKEN_TIMEDOUT = 1;
    public static final int CONDITION_AWAKEN_NOTIFIED = 0;

    public static final class SingleConditionAwaken extends MicroscopicEvent<SingleConditionAwakenFields> {
        public static final Creator<SingleConditionAwaken> CREATOR = new Creator<SingleConditionAwaken>() {
            public SingleConditionAwaken createFromParcel(Parcel source) {
                SingleConditionAwaken object = new SingleConditionAwaken();
                object.readFromParcel(source);
                return object;
            }

            public SingleConditionAwaken[] newArray(int size) {
                return new SingleConditionAwaken[size];
            }
        };

        public SingleConditionAwaken() {
            super(2, new SingleConditionAwakenFields());
        }
    }

    public static class SingleMonitorReadyFields extends BlockerEventFields {
        private static final String FIELD_MONITOR_ID = "monitorId";
        private static final String FIELD_STACK = "stack";
        public long monitorId;
        public String[] stackTrace;

        public SingleMonitorReadyFields() {
            super(true);
        }

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            this.monitorId = dataParcel.readLong();
        }

        public void fillInStackTrace(Class[] javaStackTraceClasses, StackTraceElement[] javaStackTraceElements, NativeBackTrace nativeBackTrace) {
            this.stackTrace = StackUtils.getStackTrace(javaStackTraceElements, javaStackTraceClasses, null);
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(this.monitorId);
            ParcelUtils.writeStringArray(dest, this.stackTrace);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.monitorId = source.readLong();
            this.stackTrace = ParcelUtils.readStringArray(source);
            if (this.stackTrace == null) {
                this.stackTrace = StackUtils.emptyStack;
            }
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(FIELD_MONITOR_ID, this.monitorId);
                json.put(FIELD_STACK, JSONObject.wrap(this.stackTrace));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class SingleConditionAwakenFields extends SingleMonitorReadyFields {
        private static final String FIELD_PEER_THREAD_ID = "peerThreadId";
        public int peerThreadId;

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            this.peerThreadId = dataParcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.peerThreadId);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.peerThreadId = source.readInt();
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(FIELD_PEER_THREAD_ID, this.peerThreadId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static final class SingleConditionWait extends MicroscopicEvent<SingleConditionWaitFields> {
        public static final Creator<SingleConditionWait> CREATOR = new Creator<SingleConditionWait>() {
            public SingleConditionWait createFromParcel(Parcel source) {
                SingleConditionWait object = new SingleConditionWait();
                object.readFromParcel(source);
                return object;
            }

            public SingleConditionWait[] newArray(int size) {
                return new SingleConditionWait[size];
            }
        };

        public SingleConditionWait() {
            super(3, new SingleConditionWaitFields());
        }
    }

    public static class SingleMonitorWaitFields extends BlockedEventFields {
        private static final String FIELD_MONITOR_ID = "monitorId";
        private static final String FIELD_STACK = "stack";
        public long monitorId;
        public String[] stackTrace;

        public SingleMonitorWaitFields() {
            super(true);
        }

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            this.monitorId = dataParcel.readLong();
        }

        public void fillInStackTrace(Class[] javaStackTraceClasses, StackTraceElement[] javaStackTraceElements, NativeBackTrace nativeBackTrace) {
            this.stackTrace = StackUtils.getStackTrace(javaStackTraceElements, javaStackTraceClasses, null);
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(this.monitorId);
            ParcelUtils.writeStringArray(dest, this.stackTrace);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.monitorId = source.readLong();
            this.stackTrace = ParcelUtils.readStringArray(source);
            if (this.stackTrace == null) {
                this.stackTrace = StackUtils.emptyStack;
            }
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(FIELD_MONITOR_ID, this.monitorId);
                json.put(FIELD_STACK, JSONObject.wrap(this.stackTrace));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class SingleConditionWaitFields extends SingleMonitorWaitFields {
        private static final String FIELD_AWAKEN_REASON = "awakenReason";
        public int awakenReason;

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            this.awakenReason = dataParcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.awakenReason);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.awakenReason = source.readInt();
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(FIELD_AWAKEN_REASON, this.awakenReason);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static final class SingleLockHold extends MicroscopicEvent<SingleMonitorReadyFields> {
        public static final Creator<SingleLockHold> CREATOR = new Creator<SingleLockHold>() {
            public SingleLockHold createFromParcel(Parcel source) {
                SingleLockHold object = new SingleLockHold();
                object.readFromParcel(source);
                return object;
            }

            public SingleLockHold[] newArray(int size) {
                return new SingleLockHold[size];
            }
        };

        public SingleLockHold() {
            super(0, new SingleMonitorReadyFields());
        }
    }

    public static final class SingleLockWait extends MicroscopicEvent<SingleMonitorWaitFields> {
        public static final Creator<SingleLockWait> CREATOR = new Creator<SingleLockWait>() {
            public SingleLockWait createFromParcel(Parcel source) {
                SingleLockWait object = new SingleLockWait();
                object.readFromParcel(source);
                return object;
            }

            public SingleLockWait[] newArray(int size) {
                return new SingleLockWait[size];
            }
        };

        public SingleLockWait() {
            super(1, new SingleMonitorWaitFields());
        }
    }
}
