package android.os.statistics;

import android.app.job.JobInfo;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.statistics.PerfEvent.DetailFields;
import android.text.TextUtils;
import android.util.EventLog;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventLogSuperviser {
    private static final String[] emptyEventValueStrs = new String[0];
    private static final int[] supervisedEventLogTags = new int[]{EventLogTags.POWER_SCREEN_STATE, 30008, EventLogTags.AM_ACTIVITY_LAUNCH_TIME_MIUI};

    public static class EventLogFields extends DetailFields {
        private static final String FIELD_EVENT_LOG_TAG_ID = "eventLogTagId";
        private static final String FIELD_EVENT_LOG_TAG_NAME = "eventLogTagName";
        private static final String FIELD_EVENT_LOG_TIME = "eventLogTime";
        private static final String FIELD_EVENT_LOG_VALUE_STRS = "eventlogValues";
        public long currentTimeMillis;
        public int eventLogTagId;
        public String eventLogTagName;
        public Object eventLogValueObject;
        public String[] eventLogValueStrs;

        public EventLogFields() {
            super(false);
        }

        public void fillIn(JniParcel dataParcel) {
            throw new UnsupportedOperationException();
        }

        public void resolveLazyInfo() {
            int i = this.eventLogTagId;
            if (i != 0) {
                this.eventLogTagName = EventLog.getTagName(i);
                if (TextUtils.isEmpty(this.eventLogTagName)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("[");
                    stringBuilder.append(String.valueOf(this.eventLogTagId));
                    stringBuilder.append("]");
                    this.eventLogTagName = stringBuilder.toString();
                }
            }
            Object obj = this.eventLogValueObject;
            if (obj == null) {
                this.eventLogValueStrs = EventLogSuperviser.emptyEventValueStrs;
            } else if (obj instanceof Object[]) {
                Object[] objects = (Object[]) obj;
                this.eventLogValueStrs = new String[objects.length];
                for (int i2 = 0; i2 < objects.length; i2++) {
                    this.eventLogValueStrs[i2] = String.valueOf(objects[i2]);
                }
            } else {
                this.eventLogValueStrs = new String[1];
                this.eventLogValueStrs[0] = String.valueOf(obj);
            }
            this.eventLogValueObject = null;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(this.currentTimeMillis);
            dest.writeInt(this.eventLogTagId);
            dest.writeString(this.eventLogTagName);
            ParcelUtils.writeStringArray(dest, this.eventLogValueStrs);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.currentTimeMillis = source.readLong();
            this.eventLogTagId = source.readInt();
            this.eventLogTagName = source.readString();
            if (this.eventLogTagName == null) {
                this.eventLogTagName = "";
            }
            this.eventLogValueStrs = ParcelUtils.readStringArray(source);
            if (this.eventLogValueStrs == null) {
                this.eventLogValueStrs = EventLogSuperviser.emptyEventValueStrs;
            }
            this.eventLogValueObject = null;
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(FIELD_EVENT_LOG_TIME, this.currentTimeMillis);
                json.put(FIELD_EVENT_LOG_TAG_ID, this.eventLogTagId);
                json.put(FIELD_EVENT_LOG_TAG_NAME, this.eventLogTagName);
                if (this.eventLogValueStrs != null) {
                    json.put(FIELD_EVENT_LOG_VALUE_STRS, new JSONArray(this.eventLogValueStrs));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static class EventLogTags {
        public static final int AM_ACTIVITY_LAUNCH_TIME = 30009;
        public static final int AM_ACTIVITY_LAUNCH_TIME_MIUI = 30088;
        public static final int AM_ANR = 30008;
        public static final int AM_BIND_SERVICE = 30071;
        public static final int AM_CRASH = 30039;
        public static final int AM_CREATE_SERVICE = 30030;
        public static final int AM_LOW_MEMORY = 30017;
        public static final int AM_MEMINFO = 30046;
        public static final int AM_MEM_FACTOR = 30050;
        public static final int AM_PROCESS_START_TIMEOUT = 30037;
        public static final int AM_PROC_START = 30014;
        public static final int AM_PROVIDER_LOST_PROCESS = 30036;
        public static final int AM_RELAUNCH_RESUME_ACTIVITY = 30019;
        public static final int AM_RESUME_ACTIVITY = 30007;
        public static final int AM_START_SERVICE = 30070;
        public static final int AM_SWITCH_USER = 30041;
        public static final int CONTENT_QUERY_SAMPLE = 52002;
        public static final int CONTENT_UPDATE_SAMPLE = 52003;
        public static final int POWER_SCREEN_STATE = 2728;

        private EventLogTags() {
        }
    }

    public static class SingleEventLogItem extends MacroscopicEvent<EventLogFields> {
        public static final Creator<SingleEventLogItem> CREATOR = new Creator<SingleEventLogItem>() {
            public SingleEventLogItem createFromParcel(Parcel source) {
                SingleEventLogItem object = new SingleEventLogItem();
                object.readFromParcel(source);
                return object;
            }

            public SingleEventLogItem[] newArray(int size) {
                return new SingleEventLogItem[size];
            }
        };

        public SingleEventLogItem() {
            super(65536, new EventLogFields());
        }
    }

    private static boolean isSupervised(int tag) {
        return PerfSupervisionSettings.isPerfEventReportable() && Arrays.binarySearch(supervisedEventLogTags, tag) >= 0;
    }

    public static void notifyEvent(int tag, int value) {
        if (isSupervised(tag)) {
            notifyEventWithObject(tag, Integer.valueOf(value));
        }
    }

    public static void notifyEvent(int tag, long value) {
        if (isSupervised(tag)) {
            notifyEventWithObject(tag, Long.valueOf(value));
        }
    }

    public static void notifyEvent(int tag, float value) {
        if (isSupervised(tag)) {
            notifyEventWithObject(tag, Float.valueOf(value));
        }
    }

    public static void notifyEvent(int tag, String str) {
        if (isSupervised(tag)) {
            notifyEventWithObject(tag, str);
        }
    }

    public static void notifyEvent(int tag, Object... list) {
        if (isSupervised(tag)) {
            notifyEventWithObject(tag, list);
        }
    }

    private static void notifyEventWithObject(int tag, Object eventLogValueObject) {
        SingleEventLogItem item;
        if (tag == 30008) {
            long uptimeMillis = OsUtils.getCoarseUptimeMillisFast();
            item = new SingleEventLogItem();
            Object[] objects = (Object[]) eventLogValueObject;
            item.eventFlags = 1048576 | item.eventFlags;
            item.beginUptimeMillis = uptimeMillis - JobInfo.MIN_BACKOFF_MILLIS;
            item.endUptimeMillis = uptimeMillis;
            item.inclusionId = PerfEvent.generateCoordinationId(((Integer) objects[1]).intValue());
        } else if (tag == EventLogTags.AM_ACTIVITY_LAUNCH_TIME_MIUI) {
            Object[] objects2 = (Object[]) eventLogValueObject;
            long durationMillis = Math.min(((Long) objects2[3]).longValue(), ((Long) objects2[4]).longValue());
            if (durationMillis < 1500 || durationMillis >= 120000) {
                item = null;
            } else {
                long uptimeMillis2 = OsUtils.getCoarseUptimeMillisFast();
                SingleEventLogItem item2 = new SingleEventLogItem();
                item2.eventFlags = 1048576 | item2.eventFlags;
                item2.beginUptimeMillis = uptimeMillis2 - durationMillis;
                item2.endUptimeMillis = uptimeMillis2;
                item2.inclusionId = PerfEvent.generateCoordinationId(((Integer) objects2[5]).intValue());
                item = item2;
            }
        } else {
            long uptimeMillis3 = OsUtils.getCoarseUptimeMillisFast();
            item = new SingleEventLogItem();
            item.beginUptimeMillis = uptimeMillis3;
            item.endUptimeMillis = uptimeMillis3;
        }
        if (item != null) {
            EventLogFields details = (EventLogFields) item.getDetailsFields();
            details.pid = PerfSuperviser.MY_PID;
            details.currentTimeMillis = System.currentTimeMillis();
            details.eventLogTagId = tag;
            details.eventLogValueObject = eventLogValueObject;
            PerfEventReporter.report(item);
        }
    }
}
