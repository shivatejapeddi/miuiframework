package android.os.statistics;

import android.os.Handler;
import android.os.ILooperMonitorable;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.statistics.MicroscopicEvent.RootEventFields;
import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class LooperMessageSuperviser {

    public static class SingleLooperMessage extends MicroscopicEvent<SingleLooperMessageFields> {
        public static final Creator<SingleLooperMessage> CREATOR = new Creator<SingleLooperMessage>() {
            public SingleLooperMessage createFromParcel(Parcel source) {
                SingleLooperMessage object = new SingleLooperMessage();
                object.readFromParcel(source);
                return object;
            }

            public SingleLooperMessage[] newArray(int size) {
                return new SingleLooperMessage[size];
            }
        };

        public SingleLooperMessage() {
            super(8, new SingleLooperMessageFields());
        }
    }

    public static class SingleLooperMessageFields extends RootEventFields {
        private static final String FIELD_MESSAGE_NAME = "messageName";
        private static final String FIELD_PLAN_UPTIME = "planTime";
        public String messageCallback;
        public String messageName;
        public String messageTarget;
        public int messageWhat;
        public long planUptimeMillis;

        public SingleLooperMessageFields() {
            super(false);
        }

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            this.messageName = dataParcel.readString();
            this.messageTarget = dataParcel.readString();
            this.messageWhat = dataParcel.readInt();
            this.messageCallback = dataParcel.readString();
            this.planUptimeMillis = dataParcel.readLong();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.messageName);
            dest.writeString(this.messageTarget);
            dest.writeInt(this.messageWhat);
            dest.writeString(this.messageCallback);
            dest.writeLong(this.planUptimeMillis);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.messageName = source.readString();
            this.messageTarget = source.readString();
            this.messageWhat = source.readInt();
            this.messageCallback = source.readString();
            this.planUptimeMillis = source.readLong();
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                String value = this.messageName;
                if (TextUtils.isEmpty(value)) {
                    StringBuilder b = new StringBuilder();
                    b.append("{");
                    if (TextUtils.isEmpty(this.messageCallback)) {
                        b.append("what=");
                        b.append(this.messageWhat);
                    } else {
                        b.append("callback=");
                        b.append(this.messageCallback);
                    }
                    if (!TextUtils.isEmpty(this.messageTarget)) {
                        b.append(" target=");
                        b.append(this.messageTarget);
                    }
                    b.append("}");
                    value = b.toString();
                }
                json.put(FIELD_MESSAGE_NAME, value);
                json.put(FIELD_PLAN_UPTIME, this.planUptimeMillis);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static native void nativeBeginLooperMessage();

    private static native void nativeEndLooperMessage(String str, int i, String str2, long j);

    public static void beginLooperMessage(ILooperMonitorable looper, Message msg) {
        if (PerfSupervisionSettings.isPerfEventReportable() && looper.isMonitorLooper()) {
            nativeBeginLooperMessage();
        }
    }

    public static void endLooperMessage(ILooperMonitorable looper, Message msg, long msgPlanTimeMillis) {
        if (PerfSupervisionSettings.isPerfEventReportable() && looper.isMonitorLooper()) {
            Handler messageTarget = msg.getTarget();
            Runnable messageCallback = msg.getCallback();
            String str = null;
            String name = messageTarget == null ? null : messageTarget.getClass().getName();
            int i = msg.what;
            if (messageCallback != null) {
                str = messageCallback.getClass().getName();
            }
            nativeEndLooperMessage(name, i, str, msgPlanTimeMillis);
        }
    }
}
