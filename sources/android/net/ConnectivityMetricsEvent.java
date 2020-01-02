package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.BitUtils;

public final class ConnectivityMetricsEvent implements Parcelable {
    public static final Creator<ConnectivityMetricsEvent> CREATOR = new Creator<ConnectivityMetricsEvent>() {
        public ConnectivityMetricsEvent createFromParcel(Parcel source) {
            return new ConnectivityMetricsEvent(source, null);
        }

        public ConnectivityMetricsEvent[] newArray(int size) {
            return new ConnectivityMetricsEvent[size];
        }
    };
    public Parcelable data;
    public String ifname;
    public int netId;
    public long timestamp;
    public long transports;

    /* synthetic */ ConnectivityMetricsEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    private ConnectivityMetricsEvent(Parcel in) {
        this.timestamp = in.readLong();
        this.transports = in.readLong();
        this.netId = in.readInt();
        this.ifname = in.readString();
        this.data = in.readParcelable(null);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.timestamp);
        dest.writeLong(this.transports);
        dest.writeInt(this.netId);
        dest.writeString(this.ifname);
        dest.writeParcelable(this.data, 0);
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder("ConnectivityMetricsEvent(");
        r1 = new Object[2];
        int i = 0;
        r1[0] = Long.valueOf(this.timestamp);
        r1[1] = Long.valueOf(this.timestamp);
        buffer.append(String.format("%tT.%tL", r1));
        String str = ", ";
        if (this.netId != 0) {
            buffer.append(str);
            buffer.append("netId=");
            buffer.append(this.netId);
        }
        if (this.ifname != null) {
            buffer.append(str);
            buffer.append(this.ifname);
        }
        int[] unpackBits = BitUtils.unpackBits(this.transports);
        int length = unpackBits.length;
        while (i < length) {
            int t = unpackBits[i];
            buffer.append(str);
            buffer.append(NetworkCapabilities.transportNameOf(t));
            i++;
        }
        buffer.append("): ");
        buffer.append(this.data.toString());
        return buffer.toString();
    }
}
