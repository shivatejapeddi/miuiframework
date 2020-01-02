package android.os;

import android.annotation.SystemApi;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.ArrayMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@SystemApi
public final class BatterySaverPolicyConfig implements Parcelable {
    public static final Creator<BatterySaverPolicyConfig> CREATOR = new Creator<BatterySaverPolicyConfig>() {
        public BatterySaverPolicyConfig createFromParcel(Parcel in) {
            return new BatterySaverPolicyConfig(in, null);
        }

        public BatterySaverPolicyConfig[] newArray(int size) {
            return new BatterySaverPolicyConfig[size];
        }
    };
    private final float mAdjustBrightnessFactor;
    private final boolean mAdvertiseIsEnabled;
    private final boolean mDeferFullBackup;
    private final boolean mDeferKeyValueBackup;
    private final Map<String, String> mDeviceSpecificSettings;
    private final boolean mDisableAnimation;
    private final boolean mDisableAod;
    private final boolean mDisableLaunchBoost;
    private final boolean mDisableOptionalSensors;
    private final boolean mDisableSoundTrigger;
    private final boolean mDisableVibration;
    private final boolean mEnableAdjustBrightness;
    private final boolean mEnableDataSaver;
    private final boolean mEnableFirewall;
    private final boolean mEnableNightMode;
    private final boolean mEnableQuickDoze;
    private final boolean mForceAllAppsStandby;
    private final boolean mForceBackgroundCheck;
    private final int mLocationMode;

    public static final class Builder {
        private float mAdjustBrightnessFactor = 1.0f;
        private boolean mAdvertiseIsEnabled = false;
        private boolean mDeferFullBackup = false;
        private boolean mDeferKeyValueBackup = false;
        private final ArrayMap<String, String> mDeviceSpecificSettings = new ArrayMap();
        private boolean mDisableAnimation = false;
        private boolean mDisableAod = false;
        private boolean mDisableLaunchBoost = false;
        private boolean mDisableOptionalSensors = false;
        private boolean mDisableSoundTrigger = false;
        private boolean mDisableVibration = false;
        private boolean mEnableAdjustBrightness = false;
        private boolean mEnableDataSaver = false;
        private boolean mEnableFirewall = false;
        private boolean mEnableNightMode = false;
        private boolean mEnableQuickDoze = false;
        private boolean mForceAllAppsStandby = false;
        private boolean mForceBackgroundCheck = false;
        private int mLocationMode = 0;

        public Builder setAdjustBrightnessFactor(float adjustBrightnessFactor) {
            this.mAdjustBrightnessFactor = adjustBrightnessFactor;
            return this;
        }

        public Builder setAdvertiseIsEnabled(boolean advertiseIsEnabled) {
            this.mAdvertiseIsEnabled = advertiseIsEnabled;
            return this;
        }

        public Builder setDeferFullBackup(boolean deferFullBackup) {
            this.mDeferFullBackup = deferFullBackup;
            return this;
        }

        public Builder setDeferKeyValueBackup(boolean deferKeyValueBackup) {
            this.mDeferKeyValueBackup = deferKeyValueBackup;
            return this;
        }

        public Builder addDeviceSpecificSetting(String key, String value) {
            if (key != null) {
                key = key.trim();
                if (TextUtils.isEmpty(key)) {
                    throw new IllegalArgumentException("Key cannot be empty");
                }
                this.mDeviceSpecificSettings.put(key, TextUtils.emptyIfNull(value));
                return this;
            }
            throw new IllegalArgumentException("Key cannot be null");
        }

        public Builder setDisableAnimation(boolean disableAnimation) {
            this.mDisableAnimation = disableAnimation;
            return this;
        }

        public Builder setDisableAod(boolean disableAod) {
            this.mDisableAod = disableAod;
            return this;
        }

        public Builder setDisableLaunchBoost(boolean disableLaunchBoost) {
            this.mDisableLaunchBoost = disableLaunchBoost;
            return this;
        }

        public Builder setDisableOptionalSensors(boolean disableOptionalSensors) {
            this.mDisableOptionalSensors = disableOptionalSensors;
            return this;
        }

        public Builder setDisableSoundTrigger(boolean disableSoundTrigger) {
            this.mDisableSoundTrigger = disableSoundTrigger;
            return this;
        }

        public Builder setDisableVibration(boolean disableVibration) {
            this.mDisableVibration = disableVibration;
            return this;
        }

        public Builder setEnableAdjustBrightness(boolean enableAdjustBrightness) {
            this.mEnableAdjustBrightness = enableAdjustBrightness;
            return this;
        }

        public Builder setEnableDataSaver(boolean enableDataSaver) {
            this.mEnableDataSaver = enableDataSaver;
            return this;
        }

        public Builder setEnableFirewall(boolean enableFirewall) {
            this.mEnableFirewall = enableFirewall;
            return this;
        }

        public Builder setEnableNightMode(boolean enableNightMode) {
            this.mEnableNightMode = enableNightMode;
            return this;
        }

        public Builder setEnableQuickDoze(boolean enableQuickDoze) {
            this.mEnableQuickDoze = enableQuickDoze;
            return this;
        }

        public Builder setForceAllAppsStandby(boolean forceAllAppsStandby) {
            this.mForceAllAppsStandby = forceAllAppsStandby;
            return this;
        }

        public Builder setForceBackgroundCheck(boolean forceBackgroundCheck) {
            this.mForceBackgroundCheck = forceBackgroundCheck;
            return this;
        }

        public Builder setLocationMode(int locationMode) {
            this.mLocationMode = locationMode;
            return this;
        }

        public BatterySaverPolicyConfig build() {
            return new BatterySaverPolicyConfig(this, null);
        }
    }

    private BatterySaverPolicyConfig(Builder in) {
        this.mAdjustBrightnessFactor = Math.max(0.0f, Math.min(in.mAdjustBrightnessFactor, 1.0f));
        this.mAdvertiseIsEnabled = in.mAdvertiseIsEnabled;
        this.mDeferFullBackup = in.mDeferFullBackup;
        this.mDeferKeyValueBackup = in.mDeferKeyValueBackup;
        this.mDeviceSpecificSettings = Collections.unmodifiableMap(new ArrayMap(in.mDeviceSpecificSettings));
        this.mDisableAnimation = in.mDisableAnimation;
        this.mDisableAod = in.mDisableAod;
        this.mDisableLaunchBoost = in.mDisableLaunchBoost;
        this.mDisableOptionalSensors = in.mDisableOptionalSensors;
        this.mDisableSoundTrigger = in.mDisableSoundTrigger;
        this.mDisableVibration = in.mDisableVibration;
        this.mEnableAdjustBrightness = in.mEnableAdjustBrightness;
        this.mEnableDataSaver = in.mEnableDataSaver;
        this.mEnableFirewall = in.mEnableFirewall;
        this.mEnableNightMode = in.mEnableNightMode;
        this.mEnableQuickDoze = in.mEnableQuickDoze;
        this.mForceAllAppsStandby = in.mForceAllAppsStandby;
        this.mForceBackgroundCheck = in.mForceBackgroundCheck;
        this.mLocationMode = Math.max(0, Math.min(in.mLocationMode, 4));
    }

    private BatterySaverPolicyConfig(Parcel in) {
        this.mAdjustBrightnessFactor = Math.max(0.0f, Math.min(in.readFloat(), 1.0f));
        this.mAdvertiseIsEnabled = in.readBoolean();
        this.mDeferFullBackup = in.readBoolean();
        this.mDeferKeyValueBackup = in.readBoolean();
        int size = in.readInt();
        Map<String, String> deviceSpecificSettings = new ArrayMap(size);
        for (int i = 0; i < size; i++) {
            String key = TextUtils.emptyIfNull(in.readString());
            String val = TextUtils.emptyIfNull(in.readString());
            if (!key.trim().isEmpty()) {
                deviceSpecificSettings.put(key, val);
            }
        }
        this.mDeviceSpecificSettings = Collections.unmodifiableMap(deviceSpecificSettings);
        this.mDisableAnimation = in.readBoolean();
        this.mDisableAod = in.readBoolean();
        this.mDisableLaunchBoost = in.readBoolean();
        this.mDisableOptionalSensors = in.readBoolean();
        this.mDisableSoundTrigger = in.readBoolean();
        this.mDisableVibration = in.readBoolean();
        this.mEnableAdjustBrightness = in.readBoolean();
        this.mEnableDataSaver = in.readBoolean();
        this.mEnableFirewall = in.readBoolean();
        this.mEnableNightMode = in.readBoolean();
        this.mEnableQuickDoze = in.readBoolean();
        this.mForceAllAppsStandby = in.readBoolean();
        this.mForceBackgroundCheck = in.readBoolean();
        this.mLocationMode = Math.max(0, Math.min(in.readInt(), 4));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.mAdjustBrightnessFactor);
        dest.writeBoolean(this.mAdvertiseIsEnabled);
        dest.writeBoolean(this.mDeferFullBackup);
        dest.writeBoolean(this.mDeferKeyValueBackup);
        Set<Entry<String, String>> entries = this.mDeviceSpecificSettings.entrySet();
        dest.writeInt(entries.size());
        for (Entry<String, String> entry : entries) {
            dest.writeString((String) entry.getKey());
            dest.writeString((String) entry.getValue());
        }
        dest.writeBoolean(this.mDisableAnimation);
        dest.writeBoolean(this.mDisableAod);
        dest.writeBoolean(this.mDisableLaunchBoost);
        dest.writeBoolean(this.mDisableOptionalSensors);
        dest.writeBoolean(this.mDisableSoundTrigger);
        dest.writeBoolean(this.mDisableVibration);
        dest.writeBoolean(this.mEnableAdjustBrightness);
        dest.writeBoolean(this.mEnableDataSaver);
        dest.writeBoolean(this.mEnableFirewall);
        dest.writeBoolean(this.mEnableNightMode);
        dest.writeBoolean(this.mEnableQuickDoze);
        dest.writeBoolean(this.mForceAllAppsStandby);
        dest.writeBoolean(this.mForceBackgroundCheck);
        dest.writeInt(this.mLocationMode);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator it = this.mDeviceSpecificSettings.entrySet().iterator();
        while (true) {
            String str = ",";
            if (it.hasNext()) {
                Entry<String, String> entry = (Entry) it.next();
                sb.append((String) entry.getKey());
                sb.append("=");
                sb.append((String) entry.getValue());
                sb.append(str);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("adjust_brightness_disabled=");
                stringBuilder.append(this.mEnableAdjustBrightness ^ 1);
                stringBuilder.append(",adjust_brightness_factor=");
                stringBuilder.append(this.mAdjustBrightnessFactor);
                stringBuilder.append(",advertise_is_enabled=");
                stringBuilder.append(this.mAdvertiseIsEnabled);
                stringBuilder.append(",animation_disabled=");
                stringBuilder.append(this.mDisableAnimation);
                stringBuilder.append(",aod_disabled=");
                stringBuilder.append(this.mDisableAod);
                stringBuilder.append(",datasaver_disabled=");
                stringBuilder.append(this.mEnableDataSaver ^ 1);
                stringBuilder.append(",enable_night_mode=");
                stringBuilder.append(this.mEnableNightMode);
                stringBuilder.append(",firewall_disabled=");
                stringBuilder.append(this.mEnableFirewall ^ 1);
                stringBuilder.append(",force_all_apps_standby=");
                stringBuilder.append(this.mForceAllAppsStandby);
                stringBuilder.append(",force_background_check=");
                stringBuilder.append(this.mForceBackgroundCheck);
                stringBuilder.append(",fullbackup_deferred=");
                stringBuilder.append(this.mDeferFullBackup);
                stringBuilder.append(",gps_mode=");
                stringBuilder.append(this.mLocationMode);
                stringBuilder.append(",keyvaluebackup_deferred=");
                stringBuilder.append(this.mDeferKeyValueBackup);
                stringBuilder.append(",launch_boost_disabled=");
                stringBuilder.append(this.mDisableLaunchBoost);
                stringBuilder.append(",optional_sensors_disabled=");
                stringBuilder.append(this.mDisableOptionalSensors);
                stringBuilder.append(",quick_doze_enabled=");
                stringBuilder.append(this.mEnableQuickDoze);
                stringBuilder.append(",soundtrigger_disabled=");
                stringBuilder.append(this.mDisableSoundTrigger);
                stringBuilder.append(",vibration_disabled=");
                stringBuilder.append(this.mDisableVibration);
                stringBuilder.append(str);
                stringBuilder.append(sb.toString());
                return stringBuilder.toString();
            }
        }
    }

    public float getAdjustBrightnessFactor() {
        return this.mAdjustBrightnessFactor;
    }

    public boolean getAdvertiseIsEnabled() {
        return this.mAdvertiseIsEnabled;
    }

    public boolean getDeferFullBackup() {
        return this.mDeferFullBackup;
    }

    public boolean getDeferKeyValueBackup() {
        return this.mDeferKeyValueBackup;
    }

    public Map<String, String> getDeviceSpecificSettings() {
        return this.mDeviceSpecificSettings;
    }

    public boolean getDisableAnimation() {
        return this.mDisableAnimation;
    }

    public boolean getDisableAod() {
        return this.mDisableAod;
    }

    public boolean getDisableLaunchBoost() {
        return this.mDisableLaunchBoost;
    }

    public boolean getDisableOptionalSensors() {
        return this.mDisableOptionalSensors;
    }

    public boolean getDisableSoundTrigger() {
        return this.mDisableSoundTrigger;
    }

    public boolean getDisableVibration() {
        return this.mDisableVibration;
    }

    public boolean getEnableAdjustBrightness() {
        return this.mEnableAdjustBrightness;
    }

    public boolean getEnableDataSaver() {
        return this.mEnableDataSaver;
    }

    public boolean getEnableFirewall() {
        return this.mEnableFirewall;
    }

    public boolean getEnableNightMode() {
        return this.mEnableNightMode;
    }

    public boolean getEnableQuickDoze() {
        return this.mEnableQuickDoze;
    }

    public boolean getForceAllAppsStandby() {
        return this.mForceAllAppsStandby;
    }

    public boolean getForceBackgroundCheck() {
        return this.mForceBackgroundCheck;
    }

    public int getLocationMode() {
        return this.mLocationMode;
    }
}
