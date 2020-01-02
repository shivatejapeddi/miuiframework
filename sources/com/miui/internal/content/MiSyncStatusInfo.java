package com.miui.internal.content;

import android.os.Parcel;

public class MiSyncStatusInfo {
    static final int MICLOUD_VERSION = 2;
    public String lastResultMessage;

    public MiSyncStatusInfo() {
        this.lastResultMessage = null;
    }

    public MiSyncStatusInfo(Parcel parcel) {
        int micloudVersion = 0;
        if (parcel.dataAvail() > 0) {
            micloudVersion = parcel.readInt();
        }
        if (micloudVersion == 1) {
            parcel.readLong();
            this.lastResultMessage = parcel.readString();
        } else if (micloudVersion >= 2) {
            this.lastResultMessage = parcel.readString();
        } else {
            this.lastResultMessage = null;
        }
    }

    public MiSyncStatusInfo(MiSyncStatusInfo other) {
        this.lastResultMessage = other.lastResultMessage;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(2);
        parcel.writeString(this.lastResultMessage);
    }
}
