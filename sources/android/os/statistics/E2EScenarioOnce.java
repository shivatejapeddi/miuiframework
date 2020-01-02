package android.os.statistics;

import android.os.BatteryManager;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.statistics.PerfEvent.DetailFields;
import android.provider.SearchIndexablesContract.RawData;
import org.json.JSONException;
import org.json.JSONObject;

public class E2EScenarioOnce extends MacroscopicEvent<E2EScenarioOnceDetails> {
    public static final Creator<E2EScenarioOnce> CREATOR = new Creator<E2EScenarioOnce>() {
        public E2EScenarioOnce createFromParcel(Parcel source) {
            E2EScenarioOnce object = new E2EScenarioOnce();
            object.readFromParcel(source);
            return object;
        }

        public E2EScenarioOnce[] newArray(int size) {
            return new E2EScenarioOnce[size];
        }
    };

    public static class E2EScenarioOnceDetails extends DetailFields {
        public int batteryLevel;
        public int batteryTemperature;
        public String beginPackageName;
        public int beginPid;
        public String beginProcessName;
        public int beginTid;
        public long beginWalltimeMillis;
        public String endPackageName;
        public int endPid;
        public String endProcessName;
        public int endTid;
        public long endWalltimeMillis;
        public boolean isCharging;
        public E2EScenarioPayload payload;
        public E2EScenario scenario;
        public long scenarioOnceId;
        public E2EScenarioSettings settings;
        public String tag;

        public E2EScenarioOnceDetails() {
            super(false);
        }

        public void fillIn(JniParcel dataParcel) {
            throw new UnsupportedOperationException();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(this.scenarioOnceId);
            dest.writeParcelable(this.scenario, flags);
            dest.writeParcelable(this.settings, flags);
            dest.writeString(this.tag);
            dest.writeParcelable(this.payload, flags);
            dest.writeInt(this.isCharging);
            dest.writeInt(this.batteryLevel);
            dest.writeInt(this.batteryTemperature);
            dest.writeLong(this.beginWalltimeMillis);
            dest.writeInt(this.beginPid);
            dest.writeInt(this.beginTid);
            dest.writeString(this.beginProcessName);
            dest.writeString(this.beginPackageName);
            dest.writeLong(this.endWalltimeMillis);
            dest.writeInt(this.endPid);
            dest.writeInt(this.endTid);
            dest.writeString(this.endProcessName);
            dest.writeString(this.endPackageName);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.scenarioOnceId = source.readLong();
            this.scenario = (E2EScenario) source.readParcelable(null);
            this.settings = (E2EScenarioSettings) source.readParcelable(null);
            this.tag = source.readString();
            this.payload = (E2EScenarioPayload) source.readParcelable(null);
            boolean z = true;
            if (source.readInt() != 1) {
                z = false;
            }
            this.isCharging = z;
            this.batteryLevel = source.readInt();
            this.batteryTemperature = source.readInt();
            this.beginWalltimeMillis = source.readLong();
            this.beginPid = source.readInt();
            this.beginTid = source.readInt();
            this.beginProcessName = source.readString();
            this.beginPackageName = source.readString();
            this.endWalltimeMillis = source.readLong();
            this.endPid = source.readInt();
            this.endTid = source.readInt();
            this.endProcessName = source.readString();
            this.endPackageName = source.readString();
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put("scenarioOnceId", this.scenarioOnceId);
                json.put("scenario", this.scenario.toJson());
                if (!(this.payload == null || this.payload.isEmpty())) {
                    json.put(RawData.PAYLOAD, this.payload.toJson());
                }
                json.put("charging", this.isCharging);
                json.put("batteryLevel", this.batteryLevel);
                json.put(BatteryManager.EXTRA_TEMPERATURE, this.batteryTemperature);
                json.put("beginWalltime", this.beginWalltimeMillis);
                json.put("beginPid", this.beginPid);
                json.put("beginTid", this.beginTid);
                json.put("beginProcessName", this.beginProcessName);
                json.put("beginPackageName", this.beginPackageName);
                json.put("endWalltime", this.endWalltimeMillis);
                json.put("endPid", this.endPid);
                json.put("endTid", this.endTid);
                json.put("endProcessName", this.endProcessName);
                json.put("endPackageName", this.endPackageName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public E2EScenarioOnce() {
        super(65541, new E2EScenarioOnceDetails());
    }
}
