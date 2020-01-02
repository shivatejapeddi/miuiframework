package android.os.statistics;

import android.os.Parcel;
import android.os.statistics.PerfEvent.DetailFields;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class MicroscopicEvent<T extends MicroEventFields> extends PerfEvent<T> {
    public static final int SCHED_POLICY_UNKNOWN = -1;

    public static class MicroEventFields extends DetailFields {
        public int threadId;

        public MicroEventFields(boolean _needStackTrace) {
            super(_needStackTrace);
        }

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            this.threadId = dataParcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.threadId);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.threadId = source.readInt();
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(PerfEventConstants.FIELD_THREAD_ID, this.threadId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class BlockedEventFields extends MicroEventFields {
        public int schedPolicy;
        public int schedPriority;

        public BlockedEventFields(boolean _needStackTrace) {
            super(_needStackTrace);
        }

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(dataParcel.readInt());
            this.schedPriority = dataParcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.schedPolicy);
            dest.writeInt(this.schedPriority);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(source.readInt());
            this.schedPriority = source.readInt();
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(PerfEventConstants.FIELD_SCHED_POLICY, this.schedPolicy);
                json.put("priority", this.schedPriority);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class RootEventFields extends MicroEventFields {
        public long endRealTimeMs;
        public String packageName;
        public String processName;
        public int runnableTimeMs;
        public int runningTimeMs;
        public int schedPolicy;
        public int schedPriority;
        public int sleepingTimeMs;
        public String threadName;

        public RootEventFields(boolean _needStackTrace) {
            super(_needStackTrace);
        }

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            this.processName = dataParcel.readString();
            this.packageName = dataParcel.readString();
            this.threadName = dataParcel.readString();
            this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(dataParcel.readInt());
            this.schedPriority = dataParcel.readInt();
            this.runningTimeMs = dataParcel.readInt();
            this.runnableTimeMs = dataParcel.readInt();
            this.sleepingTimeMs = dataParcel.readInt();
            this.endRealTimeMs = dataParcel.readLong();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.processName);
            dest.writeString(this.packageName);
            dest.writeString(this.threadName);
            dest.writeInt(this.schedPolicy);
            dest.writeInt(this.schedPriority);
            dest.writeInt(this.runningTimeMs);
            dest.writeInt(this.runnableTimeMs);
            dest.writeInt(this.sleepingTimeMs);
            dest.writeLong(this.endRealTimeMs);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.processName = source.readString();
            this.packageName = source.readString();
            this.threadName = source.readString();
            this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(source.readInt());
            this.schedPriority = source.readInt();
            this.runningTimeMs = source.readInt();
            this.runnableTimeMs = source.readInt();
            this.sleepingTimeMs = source.readInt();
            this.endRealTimeMs = source.readLong();
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(PerfEventConstants.FIELD_PROCESS_NAME, this.processName);
                json.put("packageName", this.packageName);
                json.put(PerfEventConstants.FIELD_THREAD_NAME, this.threadName);
                json.put(PerfEventConstants.FIELD_SCHED_POLICY, this.schedPolicy);
                json.put("priority", this.schedPriority);
                json.put(PerfEventConstants.FIELD_RUNNING_TIME, this.runningTimeMs);
                json.put(PerfEventConstants.FIELD_RUNNABLE_TIME, this.runnableTimeMs);
                json.put(PerfEventConstants.FIELD_SLEEPING_TIME, this.sleepingTimeMs);
                json.put(PerfEventConstants.FIELD_END_REAL_TIME, this.endRealTimeMs);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class MeasurementEventFields extends MicroEventFields {
        public int runnableTimeMs;
        public int runningTimeMs;
        public int schedPolicy;
        public int schedPriority;
        public int sleepingTimeMs;

        public MeasurementEventFields(boolean _needStackTrace) {
            super(_needStackTrace);
        }

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(dataParcel.readInt());
            this.schedPriority = dataParcel.readInt();
            this.runningTimeMs = dataParcel.readInt();
            this.runnableTimeMs = dataParcel.readInt();
            this.sleepingTimeMs = dataParcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.schedPolicy);
            dest.writeInt(this.schedPriority);
            dest.writeInt(this.runningTimeMs);
            dest.writeInt(this.runnableTimeMs);
            dest.writeInt(this.sleepingTimeMs);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(source.readInt());
            this.schedPriority = source.readInt();
            this.runningTimeMs = source.readInt();
            this.runnableTimeMs = source.readInt();
            this.sleepingTimeMs = source.readInt();
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(PerfEventConstants.FIELD_SCHED_POLICY, this.schedPolicy);
                json.put("priority", this.schedPriority);
                json.put(PerfEventConstants.FIELD_RUNNING_TIME, this.runningTimeMs);
                json.put(PerfEventConstants.FIELD_RUNNABLE_TIME, this.runnableTimeMs);
                json.put(PerfEventConstants.FIELD_SLEEPING_TIME, this.sleepingTimeMs);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class BlockerEventFields extends MicroEventFields {
        public int schedPolicy;
        public int schedPriority;
        public String threadName;

        public BlockerEventFields(boolean _needStackTrace) {
            super(_needStackTrace);
        }

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            this.threadName = dataParcel.readString();
            this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(dataParcel.readInt());
            this.schedPriority = dataParcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.threadName);
            dest.writeInt(this.schedPolicy);
            dest.writeInt(this.schedPriority);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.threadName = source.readString();
            this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(source.readInt());
            this.schedPriority = source.readInt();
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(PerfEventConstants.FIELD_THREAD_NAME, this.threadName);
                json.put(PerfEventConstants.FIELD_SCHED_POLICY, this.schedPolicy);
                json.put("priority", this.schedPriority);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    MicroscopicEvent(int _eventType, T _fields) {
        super(_eventType, _fields);
    }
}
