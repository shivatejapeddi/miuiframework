package android.os.statistics;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.statistics.MicroscopicEvent.BlockedEventFields;
import android.os.statistics.MicroscopicEvent.BlockerEventFields;
import org.json.JSONException;
import org.json.JSONObject;

public class SchedSuperviser {

    public static final class SchedWait extends MicroscopicEvent<SchedWaitFields> {
        public static final Creator<SchedWait> CREATOR = new Creator<SchedWait>() {
            public SchedWait createFromParcel(Parcel source) {
                SchedWait object = new SchedWait();
                object.readFromParcel(source);
                return object;
            }

            public SchedWait[] newArray(int size) {
                return new SchedWait[size];
            }
        };

        public SchedWait() {
            super(13, new SchedWaitFields());
        }
    }

    public static class SchedWaitFields extends BlockedEventFields {
        private static final String FIELD_INTERRUPTIBLE = "interruptible";
        private static final String FIELD_WAIT_REASON = "waitReason";
        private static final String FIELD_WAKING_PID = "wakingPid";
        private static final String FIELD_WAKING_THREAD_ID = "wakingThreadId";
        private static final String FIELD_WCHAN = "wchan";
        public boolean interruptible;
        public int version;
        public String waitReason;
        public int wakingPid = -1;
        public int wakingThreadId = -1;
        public long wchan;

        public SchedWaitFields() {
            super(false);
        }

        public void fillIn(JniParcel dataParcel) {
            this.version = dataParcel.readInt();
            if (this.version >= 0) {
                this.pid = dataParcel.readInt();
                this.threadId = dataParcel.readInt();
                this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(dataParcel.readInt());
                this.schedPriority = dataParcel.readInt();
                this.wakingPid = dataParcel.readInt();
                this.wakingThreadId = dataParcel.readInt();
                this.wchan = dataParcel.readLong();
                this.waitReason = "";
                boolean z = true;
                if (dataParcel.readInt() != 1) {
                    z = false;
                }
                this.interruptible = z;
            }
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.version);
            if (this.version >= 0) {
                dest.writeInt(this.pid);
                dest.writeInt(this.threadId);
                dest.writeInt(this.schedPolicy);
                dest.writeInt(this.schedPriority);
                dest.writeInt(this.wakingPid);
                dest.writeInt(this.wakingThreadId);
                dest.writeLong(this.wchan);
                dest.writeInt(this.interruptible);
            }
        }

        public void readFromParcel(Parcel source) {
            this.version = source.readInt();
            if (this.version >= 0) {
                this.pid = source.readInt();
                this.threadId = source.readInt();
                this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(source.readInt());
                this.schedPriority = source.readInt();
                this.wakingPid = source.readInt();
                this.wakingThreadId = source.readInt();
                this.wchan = source.readLong();
                this.waitReason = "";
                boolean z = true;
                if (source.readInt() != 1) {
                    z = false;
                }
                this.interruptible = z;
            }
        }

        public void resolveKernelLazyInfo() {
            this.waitReason = OsUtils.translateKernelAddress(this.wchan);
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(FIELD_WAKING_PID, this.wakingPid);
                json.put(FIELD_WAKING_THREAD_ID, this.wakingThreadId);
                json.put(FIELD_WCHAN, this.wchan);
                json.put(FIELD_WAIT_REASON, this.waitReason);
                json.put(FIELD_INTERRUPTIBLE, this.interruptible);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static final class SchedWake extends MicroscopicEvent<SchedWakeFields> {
        public static final Creator<SchedWake> CREATOR = new Creator<SchedWake>() {
            public SchedWake createFromParcel(Parcel source) {
                SchedWake object = new SchedWake();
                object.readFromParcel(source);
                return object;
            }

            public SchedWake[] newArray(int size) {
                return new SchedWake[size];
            }
        };

        public SchedWake() {
            super(14, new SchedWakeFields());
        }
    }

    public static class SchedWakeFields extends BlockerEventFields {
        private static final String FIELD_AWAKEN_PID = "awakenPid";
        private static final String FIELD_AWAKEN_THREAD_ID = "awakenThreadId";
        public int awakenPid;
        public int awakenThreadId;
        public String processName;
        public int version;

        public SchedWakeFields() {
            super(false);
        }

        public void fillIn(JniParcel dataParcel) {
            this.processName = dataParcel.readString();
            if (dataParcel.readInt() >= 0) {
                this.pid = dataParcel.readInt();
                this.threadId = dataParcel.readInt();
                this.threadName = dataParcel.readString();
                this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(dataParcel.readInt());
                this.schedPriority = dataParcel.readInt();
                this.awakenPid = dataParcel.readInt();
                this.awakenThreadId = dataParcel.readInt();
            }
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.processName);
            dest.writeInt(this.version);
            if (this.version >= 0) {
                dest.writeInt(this.pid);
                dest.writeInt(this.threadId);
                dest.writeString(this.threadName);
                dest.writeInt(this.schedPolicy);
                dest.writeInt(this.schedPriority);
                dest.writeInt(this.awakenPid);
                dest.writeInt(this.awakenThreadId);
            }
        }

        public void readFromParcel(Parcel source) {
            this.processName = source.readString();
            if (source.readInt() >= 0) {
                this.pid = source.readInt();
                this.threadId = source.readInt();
                this.threadName = source.readString();
                this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(source.readInt());
                this.schedPriority = source.readInt();
                this.awakenPid = source.readInt();
                this.awakenThreadId = source.readInt();
            }
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(PerfEventConstants.FIELD_PROCESS_NAME, this.processName);
                json.put(FIELD_AWAKEN_PID, this.awakenPid);
                json.put(FIELD_AWAKEN_THREAD_ID, this.awakenThreadId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
