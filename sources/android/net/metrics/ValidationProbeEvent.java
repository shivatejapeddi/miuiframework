package android.net.metrics;

import android.annotation.SystemApi;
import android.net.metrics.IpConnectivityLog.Event;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.util.MessageUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public final class ValidationProbeEvent implements Event {
    public static final Creator<ValidationProbeEvent> CREATOR = new Creator<ValidationProbeEvent>() {
        public ValidationProbeEvent createFromParcel(Parcel in) {
            return new ValidationProbeEvent(in, null);
        }

        public ValidationProbeEvent[] newArray(int size) {
            return new ValidationProbeEvent[size];
        }
    };
    public static final int DNS_FAILURE = 0;
    public static final int DNS_SUCCESS = 1;
    private static final int FIRST_VALIDATION = 256;
    public static final int PROBE_DNS = 0;
    public static final int PROBE_FALLBACK = 4;
    public static final int PROBE_HTTP = 1;
    public static final int PROBE_HTTPS = 2;
    public static final int PROBE_PAC = 3;
    public static final int PROBE_PRIVDNS = 5;
    private static final int REVALIDATION = 512;
    public final long durationMs;
    public final int probeType;
    public final int returnCode;

    public static final class Builder {
        private long mDurationMs;
        private int mProbeType;
        private int mReturnCode;

        public Builder setDurationMs(long durationMs) {
            this.mDurationMs = durationMs;
            return this;
        }

        public Builder setProbeType(int probeType, boolean firstValidation) {
            this.mProbeType = ValidationProbeEvent.makeProbeType(probeType, firstValidation);
            return this;
        }

        public Builder setReturnCode(int returnCode) {
            this.mReturnCode = returnCode;
            return this;
        }

        public ValidationProbeEvent build() {
            return new ValidationProbeEvent(this.mDurationMs, this.mProbeType, this.mReturnCode, null);
        }
    }

    static final class Decoder {
        static final SparseArray<String> constants = MessageUtils.findMessageNames(new Class[]{ValidationProbeEvent.class}, new String[]{"PROBE_", "FIRST_", "REVALIDATION"});

        Decoder() {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ReturnCode {
    }

    /* synthetic */ ValidationProbeEvent(long x0, int x1, int x2, AnonymousClass1 x3) {
        this(x0, x1, x2);
    }

    /* synthetic */ ValidationProbeEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    private ValidationProbeEvent(long durationMs, int probeType, int returnCode) {
        this.durationMs = durationMs;
        this.probeType = probeType;
        this.returnCode = returnCode;
    }

    private ValidationProbeEvent(Parcel in) {
        this.durationMs = in.readLong();
        this.probeType = in.readInt();
        this.returnCode = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.durationMs);
        out.writeInt(this.probeType);
        out.writeInt(this.returnCode);
    }

    public int describeContents() {
        return 0;
    }

    private static int makeProbeType(int probeType, boolean firstValidation) {
        return (probeType & 255) | (firstValidation ? 256 : 512);
    }

    public static String getProbeName(int probeType) {
        return (String) Decoder.constants.get(probeType & 255, "PROBE_???");
    }

    private static String getValidationStage(int probeType) {
        return (String) Decoder.constants.get(65280 & probeType, IccCardConstants.INTENT_VALUE_ICC_UNKNOWN);
    }

    public String toString() {
        return String.format("ValidationProbeEvent(%s:%d %s, %dms)", new Object[]{getProbeName(this.probeType), Integer.valueOf(this.returnCode), getValidationStage(this.probeType), Long.valueOf(this.durationMs)});
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null || !obj.getClass().equals(ValidationProbeEvent.class)) {
            return false;
        }
        ValidationProbeEvent other = (ValidationProbeEvent) obj;
        if (this.durationMs == other.durationMs && this.probeType == other.probeType && this.returnCode == other.returnCode) {
            z = true;
        }
        return z;
    }
}
