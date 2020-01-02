package com.miui.mishare;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class RemoteDevice implements Parcelable {
    public static final Creator<RemoteDevice> CREATOR = new Creator<RemoteDevice>() {
        public RemoteDevice createFromParcel(Parcel in) {
            return new RemoteDevice(in);
        }

        public RemoteDevice[] newArray(int size) {
            return new RemoteDevice[size];
        }
    };
    public static final String KEY_BLUETOOTH_LE_MAC_ADDRESS = "ble_ad";
    public static final String KEY_DEVICE_CODE = "dc";
    public static final String KEY_MANUFACTURE_CODE = "mc";
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_NICKNAME_HAS_MORE = "nickname_has_more";
    public static final String KEY_PORT = "p";
    public static final String KEY_RSSI = "rssi";
    public static final String KEY_STATUS = "s";
    public static final String KEY_SUPPORTED_GUIDING_NETWORK_TYPE = "sgnt";
    public static final String KEY_WIFI_AP_5G = "ap_5g";
    public static final String KEY_WIFI_AP_SSID = "ap_ssid";
    public static final String KEY_WIFI_P2P_FREQUENCY = "p2p_freq";
    public static final String KEY_WIFI_P2P_MAC_ADDRESS = "p2p_ad";
    private String mDeviceId;
    private Bundle mExtras;

    public static final class Builder {
        private String mDeviceId;
        private Bundle mExtras;

        public Builder putExtras(Bundle bundle) {
            this.mExtras = bundle;
            return this;
        }

        public Builder setDeviceId(String deviceId) {
            this.mDeviceId = deviceId;
            return this;
        }

        public RemoteDevice build() {
            return new RemoteDevice(this.mDeviceId, this.mExtras, null);
        }
    }

    /* synthetic */ RemoteDevice(String x0, Bundle x1, AnonymousClass1 x2) {
        this(x0, x1);
    }

    protected RemoteDevice(Parcel in) {
        this.mDeviceId = in.readString();
        this.mExtras = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mDeviceId);
        dest.writeBundle(this.mExtras);
    }

    public int describeContents() {
        return 0;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public boolean isPC() {
        Bundle bundle = this.mExtras;
        return bundle != null && bundle.getInt(KEY_SUPPORTED_GUIDING_NETWORK_TYPE, -1) == 2;
    }

    private RemoteDevice(String deviceId, Bundle extras) {
        this.mDeviceId = deviceId;
        this.mExtras = extras;
    }

    public boolean equals(Object obj) {
        if (obj instanceof RemoteDevice) {
            return ((RemoteDevice) obj).mDeviceId.equals(this.mDeviceId);
        }
        return super.equals(obj);
    }

    public int hashCode() {
        return Objects.hashCode(this.mDeviceId);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RemoteDevice: ");
        stringBuilder.append(this.mDeviceId);
        return stringBuilder.toString();
    }
}
