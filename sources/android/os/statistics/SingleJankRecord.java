package android.os.statistics;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.statistics.PerfEvent.DetailFields;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleJankRecord extends MacroscopicEvent<JankRecordFields> {
    public static final Creator<SingleJankRecord> CREATOR = new Creator<SingleJankRecord>() {
        public SingleJankRecord createFromParcel(Parcel source) {
            SingleJankRecord object = new SingleJankRecord();
            object.readFromParcel(source);
            return object;
        }

        public SingleJankRecord[] newArray(int size) {
            return new SingleJankRecord[size];
        }
    };

    public static class JankRecordFields extends DetailFields {
        private static final String FIELD_APP_CAUSE = "appCause";
        private static final String FIELD_BATTERY_LEVEL = "batteryLevel";
        private static final String FIELD_BATTERY_TEMP = "batteryTemperature";
        private static final String FIELD_CHARGING = "isCharging";
        private static final String FIELD_CONCLUSION = "conclusion";
        private static final String FIELD_MAX_FRAME_DURATION = "maxFrameDuration";
        private static final String FIELD_NUM_FRAMES = "numFrames";
        private static final String FIELD_RECEIVED_CURRENT_TIME = "receivedCurrentTime";
        private static final String FIELD_RECEIVED_UPTIME = "receivedUptime";
        private static final String FIELD_RENDER_THREAD_ID = "renderThreadTid";
        private static final String FIELD_SYS_CAUSE = "sysCause";
        private static final String FIELD_TOTAL_DURATION = "totalDuration";
        private static final String FIELD_WINDOW_NAME = "windowName";
        public String appCause;
        public int batteryLevel;
        public int batteryTemperature;
        public String conclusion;
        public boolean isCharging;
        public long maxFrameDuration;
        public long numFrames;
        public String packageName;
        public String processName;
        public long receivedCurrentTimeMillis;
        public long receivedUptimeMillis;
        public int renderThreadTid;
        public String sysCause;
        public long totalDuration;
        public String windowName;

        public JankRecordFields() {
            super(false);
            String str = "";
            this.windowName = str;
            this.appCause = str;
            this.sysCause = str;
            this.conclusion = str;
        }

        public void fillIn(JniParcel dataParcel) {
            throw new UnsupportedOperationException();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.processName);
            dest.writeString(this.packageName);
            dest.writeInt(this.renderThreadTid);
            dest.writeString(this.windowName);
            dest.writeString(this.appCause);
            dest.writeString(this.sysCause);
            dest.writeString(this.conclusion);
            dest.writeLong(this.totalDuration);
            dest.writeLong(this.maxFrameDuration);
            dest.writeLong(this.receivedUptimeMillis);
            dest.writeLong(this.receivedCurrentTimeMillis);
            dest.writeInt(this.isCharging);
            dest.writeInt(this.batteryLevel);
            dest.writeInt(this.batteryTemperature);
            dest.writeLong(this.numFrames);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            String processName = source.readString();
            processName = processName != null ? processName : "";
            String packageName = source.readString();
            if (packageName != null) {
            }
            this.renderThreadTid = source.readInt();
            this.windowName = source.readString();
            this.appCause = source.readString();
            this.sysCause = source.readString();
            this.conclusion = source.readString();
            this.totalDuration = source.readLong();
            this.maxFrameDuration = source.readLong();
            this.receivedUptimeMillis = source.readLong();
            this.receivedCurrentTimeMillis = source.readLong();
            boolean z = true;
            if (source.readInt() != 1) {
                z = false;
            }
            this.isCharging = z;
            this.batteryLevel = source.readInt();
            this.batteryTemperature = source.readInt();
            this.numFrames = source.readLong();
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(PerfEventConstants.FIELD_PROCESS_NAME, this.processName);
                json.put("packageName", this.packageName);
                json.put(FIELD_RENDER_THREAD_ID, this.renderThreadTid);
                json.put(FIELD_WINDOW_NAME, this.windowName);
                json.put(FIELD_APP_CAUSE, this.appCause);
                json.put(FIELD_SYS_CAUSE, this.sysCause);
                json.put(FIELD_CONCLUSION, this.conclusion);
                json.put(FIELD_TOTAL_DURATION, this.totalDuration);
                json.put(FIELD_MAX_FRAME_DURATION, this.maxFrameDuration);
                json.put(FIELD_RECEIVED_UPTIME, this.receivedUptimeMillis);
                json.put(FIELD_RECEIVED_CURRENT_TIME, this.receivedCurrentTimeMillis);
                json.put(FIELD_CHARGING, this.isCharging);
                json.put(FIELD_BATTERY_LEVEL, this.batteryLevel);
                json.put(FIELD_BATTERY_TEMP, this.batteryTemperature);
                json.put(FIELD_NUM_FRAMES, this.numFrames);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public SingleJankRecord() {
        super(65538, new JankRecordFields());
    }
}
