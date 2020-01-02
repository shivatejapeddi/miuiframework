package com.android.internal.location;

import android.annotation.UnsupportedAppUsage;
import android.location.LocationRequest;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.TimeUtils;
import java.util.ArrayList;
import java.util.List;

public final class ProviderRequest implements Parcelable {
    public static final Creator<ProviderRequest> CREATOR = new Creator<ProviderRequest>() {
        public ProviderRequest createFromParcel(Parcel in) {
            ProviderRequest request = new ProviderRequest();
            boolean z = true;
            if (in.readInt() != 1) {
                z = false;
            }
            request.reportLocation = z;
            request.interval = in.readLong();
            request.lowPowerMode = in.readBoolean();
            request.locationSettingsIgnored = in.readBoolean();
            int count = in.readInt();
            for (int i = 0; i < count; i++) {
                request.locationRequests.add((LocationRequest) LocationRequest.CREATOR.createFromParcel(in));
            }
            return request;
        }

        public ProviderRequest[] newArray(int size) {
            return new ProviderRequest[size];
        }
    };
    @UnsupportedAppUsage
    public long interval = Long.MAX_VALUE;
    @UnsupportedAppUsage
    public final List<LocationRequest> locationRequests = new ArrayList();
    public boolean locationSettingsIgnored = false;
    public boolean lowPowerMode = false;
    @UnsupportedAppUsage
    public boolean reportLocation = false;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.reportLocation);
        parcel.writeLong(this.interval);
        parcel.writeBoolean(this.lowPowerMode);
        parcel.writeBoolean(this.locationSettingsIgnored);
        parcel.writeInt(this.locationRequests.size());
        for (LocationRequest request : this.locationRequests) {
            request.writeToParcel(parcel, flags);
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("ProviderRequest[");
        if (this.reportLocation) {
            s.append("ON");
            s.append(" interval=");
            TimeUtils.formatDuration(this.interval, s);
            if (this.lowPowerMode) {
                s.append(" lowPowerMode");
            }
            if (this.locationSettingsIgnored) {
                s.append(" locationSettingsIgnored");
            }
        } else {
            s.append("OFF");
        }
        s.append(']');
        return s.toString();
    }
}
