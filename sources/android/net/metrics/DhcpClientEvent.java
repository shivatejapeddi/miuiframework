package android.net.metrics;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.net.metrics.IpConnectivityLog.Event;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

@SystemApi
public final class DhcpClientEvent implements Event {
    public static final Creator<DhcpClientEvent> CREATOR = new Creator<DhcpClientEvent>() {
        public DhcpClientEvent createFromParcel(Parcel in) {
            return new DhcpClientEvent(in, null);
        }

        public DhcpClientEvent[] newArray(int size) {
            return new DhcpClientEvent[size];
        }
    };
    public final int durationMs;
    public final String msg;

    public static final class Builder {
        private int mDurationMs;
        private String mMsg;

        public Builder setMsg(String msg) {
            this.mMsg = msg;
            return this;
        }

        public Builder setDurationMs(int durationMs) {
            this.mDurationMs = durationMs;
            return this;
        }

        public DhcpClientEvent build() {
            return new DhcpClientEvent(this.mMsg, this.mDurationMs, null);
        }
    }

    /* synthetic */ DhcpClientEvent(String x0, int x1, AnonymousClass1 x2) {
        this(x0, x1);
    }

    @UnsupportedAppUsage
    private DhcpClientEvent(String msg, int durationMs) {
        this.msg = msg;
        this.durationMs = durationMs;
    }

    private DhcpClientEvent(Parcel in) {
        this.msg = in.readString();
        this.durationMs = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.msg);
        out.writeInt(this.durationMs);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return String.format("DhcpClientEvent(%s, %dms)", new Object[]{this.msg, Integer.valueOf(this.durationMs)});
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null || !obj.getClass().equals(DhcpClientEvent.class)) {
            return false;
        }
        DhcpClientEvent other = (DhcpClientEvent) obj;
        if (TextUtils.equals(this.msg, other.msg) && this.durationMs == other.durationMs) {
            z = true;
        }
        return z;
    }
}
