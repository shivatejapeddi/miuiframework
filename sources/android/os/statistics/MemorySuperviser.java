package android.os.statistics;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.statistics.MicroscopicEvent.MeasurementEventFields;
import org.json.JSONException;
import org.json.JSONObject;

public class MemorySuperviser {

    public static final class Slowpath extends MicroscopicEvent<SlowpathFields> {
        public static final Creator<Slowpath> CREATOR = new Creator<Slowpath>() {
            public Slowpath createFromParcel(Parcel source) {
                Slowpath object = new Slowpath();
                object.readFromParcel(source);
                return object;
            }

            public Slowpath[] newArray(int size) {
                return new Slowpath[size];
            }
        };

        public Slowpath() {
            super(15, new SlowpathFields());
        }
    }

    public static class SlowpathFields extends MeasurementEventFields {
        private static final String FIELD_ORDER = "order";
        public int order;
        public int version;

        public SlowpathFields() {
            super(false);
        }

        public void fillIn(JniParcel dataParcel) {
            this.version = dataParcel.readInt();
            if (this.version >= 0) {
                this.pid = dataParcel.readInt();
                this.threadId = dataParcel.readInt();
                this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(dataParcel.readInt());
                this.schedPriority = dataParcel.readInt();
                this.runningTimeMs = dataParcel.readInt();
                this.runnableTimeMs = 0;
                this.sleepingTimeMs = 0;
                this.order = dataParcel.readInt();
            }
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.version);
            if (this.version >= 0) {
                dest.writeInt(this.pid);
                dest.writeInt(this.threadId);
                dest.writeInt(this.schedPolicy);
                dest.writeInt(this.schedPriority);
                dest.writeInt(this.runningTimeMs);
                dest.writeInt(this.order);
            }
        }

        public void readFromParcel(Parcel source) {
            this.version = source.readInt();
            if (this.version >= 0) {
                this.pid = source.readInt();
                this.threadId = source.readInt();
                this.schedPolicy = OsUtils.decodeThreadSchedulePolicy(source.readInt());
                this.schedPriority = source.readInt();
                this.runningTimeMs = source.readInt();
                this.runnableTimeMs = 0;
                this.sleepingTimeMs = 0;
                this.order = source.readInt();
            }
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(FIELD_ORDER, this.order);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
