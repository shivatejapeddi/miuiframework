package com.miui.mishare;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ConnectionConfig implements Parcelable {
    public static final Creator<ConnectionConfig> CREATOR = new Creator<ConnectionConfig>() {
        public ConnectionConfig createFromParcel(Parcel in) {
            return new ConnectionConfig(in);
        }

        public ConnectionConfig[] newArray(int size) {
            return new ConnectionConfig[size];
        }
    };
    public static final int GUIDING_NETWORK_TYPE_BLE_DIRECT_BROADCAST = 2;
    public static final int GUIDING_NETWORK_TYPE_GATT = 1;
    public static final String KEY_PC_FILE_COUNT = "pc_file_count";
    public static final String KEY_PC_TYPE_TASK_ID = "pc_t_id";
    public static final int MAIN_NETWORK_TYPE_WIFI_AP = 2;
    public static final int MAIN_NETWORK_TYPE_WIFI_P2P = 1;
    public static final int PROTOCOL_TYPE_MIOV_HTTP = 2;
    public static final int PROTOCOL_TYPE_MI_PC_HTTP = 3;
    private Bundle mExtras;
    private int mGuidingNetworkType;
    private int mMainNetworkType;
    private int mProtocolType;

    public static final class Builder {
        private Bundle mExtras;
        private int mGuidingNetworkType;
        private int mMainNetworkType;
        private int mProtocolType;

        public Builder setGuidingNetworkType(int guidingNetworkType) {
            this.mGuidingNetworkType = guidingNetworkType;
            return this;
        }

        public Builder setMainNetworkType(int mainNetworkType) {
            this.mMainNetworkType = mainNetworkType;
            return this;
        }

        public Builder setProtocolType(int protocolType) {
            this.mProtocolType = protocolType;
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public ConnectionConfig build() {
            return new ConnectionConfig(this.mGuidingNetworkType, this.mMainNetworkType, this.mProtocolType, this.mExtras, null);
        }
    }

    /* synthetic */ ConnectionConfig(int x0, int x1, int x2, Bundle x3, AnonymousClass1 x4) {
        this(x0, x1, x2, x3);
    }

    private ConnectionConfig(int guidingNetworkType, int mainNetworkType, int protocolType, Bundle extras) {
        this.mGuidingNetworkType = guidingNetworkType;
        this.mMainNetworkType = mainNetworkType;
        this.mProtocolType = protocolType;
        this.mExtras = extras;
    }

    protected ConnectionConfig(Parcel in) {
        this.mGuidingNetworkType = in.readInt();
        this.mMainNetworkType = in.readInt();
        this.mProtocolType = in.readInt();
        this.mExtras = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mGuidingNetworkType);
        dest.writeInt(this.mMainNetworkType);
        dest.writeInt(this.mProtocolType);
        dest.writeBundle(this.mExtras);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ConnectionConfig{ gt=");
        stringBuilder.append(this.mGuidingNetworkType);
        stringBuilder.append(", mt=");
        stringBuilder.append(this.mMainNetworkType);
        stringBuilder.append(", tt=");
        stringBuilder.append(this.mProtocolType);
        stringBuilder.append(", extras=");
        stringBuilder.append(this.mExtras);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public int getGuidingNetworkType() {
        return this.mGuidingNetworkType;
    }

    public int getMainNetworkType() {
        return this.mMainNetworkType;
    }

    public int getProtocolType() {
        return this.mProtocolType;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }
}
