package android.os.statistics;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class PerfEvent<T extends DetailFields> implements Parcelable {
    public static final int TYPE_UNKNOWN = -1;
    public long beginUptimeMillis;
    private T details;
    public long endUptimeMillis;
    public int eventFlags;
    public long eventSeq;
    public final int eventType;
    public long inclusionId;
    private long persistentId;
    public long synchronizationId;

    public static class DetailFields {
        public final boolean needStackTrace;
        public int pid;

        public DetailFields(boolean _needStackTrace) {
            this.needStackTrace = _needStackTrace;
        }

        public void fillIn(JniParcel dataParcel) {
            this.pid = dataParcel.readInt();
        }

        public void fillInStackTrace(Class[] javaStackTraceClasses, StackTraceElement[] javaStackTraceElements, NativeBackTrace nativeBackTrace) {
        }

        public void resolveLazyInfo() {
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.pid);
        }

        public void readFromParcel(Parcel source) {
            this.pid = source.readInt();
        }

        public void resolveKernelLazyInfo() {
        }

        public void writeToJson(JSONObject json) {
            try {
                json.put("pid", this.pid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected PerfEvent(int _eventType, T _fields) {
        this.eventType = _eventType;
        this.details = _fields;
    }

    public int describeContents() {
        return 0;
    }

    public final void fillIn(int eventFlags, long beginUptimeMillis, long endUptimeMillis, long inclusionId, long synchronizationId, long eventSeq, JniParcel dataParcel, Class[] javaStackTraceClasses, StackTraceElement[] javaStackTraceElements, NativeBackTrace nativeBackTrace) {
        this.eventFlags = eventFlags;
        this.beginUptimeMillis = beginUptimeMillis;
        this.endUptimeMillis = endUptimeMillis;
        this.inclusionId = inclusionId;
        this.synchronizationId = synchronizationId;
        this.eventSeq = eventSeq;
        this.details.fillIn(dataParcel);
        if (this.details.needStackTrace) {
            this.details.fillInStackTrace(javaStackTraceClasses, javaStackTraceElements, nativeBackTrace);
            return;
        }
        Class[] clsArr = javaStackTraceClasses;
        StackTraceElement[] stackTraceElementArr = javaStackTraceElements;
        NativeBackTrace nativeBackTrace2 = nativeBackTrace;
    }

    public final void fillInSeq(long eventSeq) {
        this.eventSeq = eventSeq;
    }

    public final void fillInDetails(JniParcel dataParcel, Class[] javaStackTraceClasses, StackTraceElement[] javaStackTraceElements, NativeBackTrace nativeBackTrace) {
        this.details.fillIn(dataParcel);
        if (this.details.needStackTrace) {
            this.details.fillInStackTrace(javaStackTraceClasses, javaStackTraceElements, nativeBackTrace);
        }
    }

    public final void resolveLazyInfo() {
        int i = this.eventFlags;
        if ((i & 32) == 0) {
            this.eventFlags = i | 32;
            this.details.resolveLazyInfo();
        }
    }

    public boolean isMeaningful() {
        return true;
    }

    public final void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.eventType);
        dest.writeInt(this.eventFlags);
        dest.writeLong(this.beginUptimeMillis);
        dest.writeLong(this.endUptimeMillis);
        dest.writeLong(this.inclusionId);
        dest.writeLong(this.synchronizationId);
        dest.writeLong(this.eventSeq);
        this.details.writeToParcel(dest, flags);
    }

    public final void readFromParcel(Parcel source) {
        source.readInt();
        this.eventFlags = source.readInt();
        this.beginUptimeMillis = source.readLong();
        this.endUptimeMillis = source.readLong();
        this.inclusionId = source.readLong();
        this.synchronizationId = source.readLong();
        this.eventSeq = source.readLong();
        this.details.readFromParcel(source);
    }

    public final void readDetailsFromParcel(Parcel source) {
        this.details.readFromParcel(source);
    }

    public final void resolveKernelLazyInfo() {
        int i = this.eventFlags;
        if ((i & 128) == 0) {
            this.eventFlags = i | 128;
            this.details.resolveKernelLazyInfo();
        }
    }

    public final JSONObject toJson() {
        JSONObject json = new JSONObject();
        writeToJson(json);
        return json;
    }

    public final void writeToJson(JSONObject json) {
        try {
            json.put(PerfEventConstants.FIELD_EVENT_TYPE, this.eventType);
            json.put(PerfEventConstants.FIELD_EVENT_TYPE_NAME, PerfEventConstants.getTypeName(this.eventType));
            json.put("beginTime", this.beginUptimeMillis);
            json.put("endTime", this.endUptimeMillis);
            json.put(PerfEventConstants.FIELD_OCCUR_TIME, this.endUptimeMillis);
            json.put("seq", this.eventSeq);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.details.writeToJson(json);
    }

    public final T getDetailsFields() {
        return this.details;
    }

    public final long getPersistentId() {
        return this.persistentId;
    }

    public final void setPersistentId(long persistentId) {
        this.persistentId = persistentId;
    }

    public final void clearDetailFields() {
        this.details = null;
    }

    public static long generateCoordinationId(int pid, int code) {
        return (((long) pid) << 32) + ((long) code);
    }

    public static long generateCoordinationId(int pid) {
        return ((long) pid) << 32;
    }
}
