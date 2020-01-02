package miui.mqsas.sdk.event;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BrightnessEvent implements Parcelable {
    public static final Creator<BrightnessEvent> CREATOR = new Creator<BrightnessEvent>() {
        public BrightnessEvent createFromParcel(Parcel source) {
            return new BrightnessEvent(source, null);
        }

        public BrightnessEvent[] newArray(int size) {
            return new BrightnessEvent[size];
        }
    };
    public static final int EVENT_BRIGHTNESS_MODE_CHANGED = 2;
    public static final int EVENT_OTHER_CHANGED_BRIGHTNESS = 1;
    public static final int EVENT_USER_CHANGED_BRIGHTNESS = 0;
    public static final int EVENT_WINDOW_CHANGED_BRIGHTNESS = 3;
    private float ambient_lux;
    private int battery_level;
    private String extra;
    private int gpu_load;
    private int ime_md5;
    private float last_observed_lux;
    private float new_value;
    private float old_value;
    private int orientation;
    private String ring_lux;
    private int screen_brightness;
    private String spline;
    private long time_stamp;
    private String top_package;
    private int type;

    /* synthetic */ BrightnessEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public String toSimpleString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append(this.type);
        String str = ",";
        stringBuilder.append(str);
        stringBuilder.append(this.old_value);
        stringBuilder.append(str);
        stringBuilder.append(this.new_value);
        stringBuilder.append(str);
        stringBuilder.append(this.ambient_lux);
        stringBuilder.append(str);
        stringBuilder.append(this.last_observed_lux);
        stringBuilder.append(str);
        stringBuilder.append(this.screen_brightness);
        stringBuilder.append(str);
        stringBuilder.append(this.orientation);
        stringBuilder.append(str);
        stringBuilder.append(this.ime_md5);
        stringBuilder.append(str);
        stringBuilder.append(this.top_package);
        stringBuilder.append(str);
        stringBuilder.append(this.gpu_load);
        stringBuilder.append(str);
        stringBuilder.append(this.battery_level);
        stringBuilder.append(str);
        stringBuilder.append(this.extra);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append(timestamp2String(this.time_stamp));
        String str = ",";
        stringBuilder.append(str);
        stringBuilder.append(this.type);
        stringBuilder.append(str);
        stringBuilder.append(this.old_value);
        stringBuilder.append(str);
        stringBuilder.append(this.new_value);
        stringBuilder.append(str);
        stringBuilder.append(this.ambient_lux);
        stringBuilder.append(str);
        stringBuilder.append(this.last_observed_lux);
        stringBuilder.append(str);
        stringBuilder.append(this.screen_brightness);
        stringBuilder.append(str);
        stringBuilder.append(this.orientation);
        stringBuilder.append(str);
        stringBuilder.append(this.ime_md5);
        stringBuilder.append(str);
        stringBuilder.append(this.top_package);
        stringBuilder.append(str);
        stringBuilder.append(this.gpu_load);
        stringBuilder.append(str);
        stringBuilder.append(this.battery_level);
        stringBuilder.append(str);
        stringBuilder.append(this.spline);
        stringBuilder.append(str);
        stringBuilder.append(this.ring_lux);
        stringBuilder.append(str);
        stringBuilder.append(this.extra);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private BrightnessEvent(Parcel source) {
        this.type = source.readInt();
        this.old_value = source.readFloat();
        this.new_value = source.readFloat();
        this.ambient_lux = source.readFloat();
        this.last_observed_lux = source.readFloat();
        this.screen_brightness = source.readInt();
        this.orientation = source.readInt();
        this.spline = source.readString();
        this.ring_lux = source.readString();
        this.top_package = source.readString();
        this.ime_md5 = source.readInt();
        this.gpu_load = source.readInt();
        this.battery_level = source.readInt();
        this.time_stamp = source.readLong();
        this.extra = source.readString();
    }

    public void setEventType(int type) {
        this.type = type;
    }

    public int getEventType() {
        return this.type;
    }

    public void setOldValue(float oldValue) {
        this.old_value = oldValue;
    }

    public float getOldValue() {
        return this.old_value;
    }

    public void setNewValue(float newValue) {
        this.new_value = newValue;
    }

    public float getNewValue() {
        return this.new_value;
    }

    public void setAmbientLux(float ambientLux) {
        this.ambient_lux = ambientLux;
    }

    public float getAmbientLux() {
        return this.ambient_lux;
    }

    public void setLastObservedLux(float lastObservedLux) {
        this.last_observed_lux = lastObservedLux;
    }

    public float getLastObservedLux() {
        return this.last_observed_lux;
    }

    public void setScreenBrightness(int screenBrightness) {
        this.screen_brightness = screenBrightness;
    }

    public int getScreenBrightness() {
        return this.screen_brightness;
    }

    public void setSpline(String spline) {
        this.spline = spline;
    }

    public String getSpline() {
        return this.spline;
    }

    public void setRingLux(String ringLux) {
        this.ring_lux = ringLux;
    }

    public String getRingLux() {
        return this.ring_lux;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setScreenAutoBrightness(int screenAutoBrightness) {
        this.ime_md5 = screenAutoBrightness;
    }

    public int getScreenAutoBrightness() {
        return this.ime_md5;
    }

    public void setForegroundPackage(String top_package) {
        this.top_package = top_package;
    }

    public String getForegroundPackage() {
        return this.top_package;
    }

    public void setGpuLoad(int gpuLoad) {
        this.gpu_load = gpuLoad;
    }

    public int getGpuLoad() {
        return this.gpu_load;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.battery_level = batteryLevel;
    }

    public int getBatteryLevel() {
        return this.battery_level;
    }

    public void setTimeStamp(long timeStamp) {
        this.time_stamp = timeStamp;
    }

    public long getTimeStamp() {
        return this.time_stamp;
    }

    public void setExtra(String string) {
        this.extra = string;
    }

    public String getExtra() {
        return this.extra;
    }

    public static String timestamp2String(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss").format(new Date(time));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeFloat(this.old_value);
        dest.writeFloat(this.new_value);
        dest.writeFloat(this.ambient_lux);
        dest.writeFloat(this.last_observed_lux);
        dest.writeInt(this.screen_brightness);
        dest.writeInt(this.orientation);
        dest.writeString(this.spline);
        dest.writeString(this.ring_lux);
        dest.writeString(this.top_package);
        dest.writeInt(this.ime_md5);
        dest.writeInt(this.gpu_load);
        dest.writeInt(this.battery_level);
        dest.writeLong(this.time_stamp);
        dest.writeString(this.extra);
    }

    public int describeContents() {
        return 0;
    }
}
