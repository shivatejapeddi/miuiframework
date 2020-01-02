package android.speech.tts;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Voice implements Parcelable {
    public static final Creator<Voice> CREATOR = new Creator<Voice>() {
        public Voice createFromParcel(Parcel in) {
            return new Voice(in, null);
        }

        public Voice[] newArray(int size) {
            return new Voice[size];
        }
    };
    public static final int LATENCY_HIGH = 400;
    public static final int LATENCY_LOW = 200;
    public static final int LATENCY_NORMAL = 300;
    public static final int LATENCY_VERY_HIGH = 500;
    public static final int LATENCY_VERY_LOW = 100;
    public static final int QUALITY_HIGH = 400;
    public static final int QUALITY_LOW = 200;
    public static final int QUALITY_NORMAL = 300;
    public static final int QUALITY_VERY_HIGH = 500;
    public static final int QUALITY_VERY_LOW = 100;
    private final Set<String> mFeatures;
    private final int mLatency;
    private final Locale mLocale;
    private final String mName;
    private final int mQuality;
    private final boolean mRequiresNetworkConnection;

    /* synthetic */ Voice(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public Voice(String name, Locale locale, int quality, int latency, boolean requiresNetworkConnection, Set<String> features) {
        this.mName = name;
        this.mLocale = locale;
        this.mQuality = quality;
        this.mLatency = latency;
        this.mRequiresNetworkConnection = requiresNetworkConnection;
        this.mFeatures = features;
    }

    private Voice(Parcel in) {
        this.mName = in.readString();
        this.mLocale = (Locale) in.readSerializable();
        this.mQuality = in.readInt();
        this.mLatency = in.readInt();
        boolean z = true;
        if (in.readByte() != (byte) 1) {
            z = false;
        }
        this.mRequiresNetworkConnection = z;
        this.mFeatures = new HashSet();
        Collections.addAll(this.mFeatures, in.readStringArray());
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeSerializable(this.mLocale);
        dest.writeInt(this.mQuality);
        dest.writeInt(this.mLatency);
        dest.writeByte((byte) this.mRequiresNetworkConnection);
        dest.writeStringList(new ArrayList(this.mFeatures));
    }

    public int describeContents() {
        return 0;
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public int getQuality() {
        return this.mQuality;
    }

    public int getLatency() {
        return this.mLatency;
    }

    public boolean isNetworkConnectionRequired() {
        return this.mRequiresNetworkConnection;
    }

    public String getName() {
        return this.mName;
    }

    public Set<String> getFeatures() {
        return this.mFeatures;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("Voice[Name: ");
        builder.append(this.mName);
        builder.append(", locale: ");
        builder.append(this.mLocale);
        builder.append(", quality: ");
        builder.append(this.mQuality);
        builder.append(", latency: ");
        builder.append(this.mLatency);
        builder.append(", requiresNetwork: ");
        builder.append(this.mRequiresNetworkConnection);
        builder.append(", features: ");
        builder.append(this.mFeatures.toString());
        builder.append("]");
        return builder.toString();
    }

    public int hashCode() {
        int result = 1 * 31;
        Set set = this.mFeatures;
        int i = 0;
        result = (((result + (set == null ? 0 : set.hashCode())) * 31) + this.mLatency) * 31;
        Locale locale = this.mLocale;
        int hashCode = (result + (locale == null ? 0 : locale.hashCode())) * 31;
        String str = this.mName;
        if (str != null) {
            i = str.hashCode();
        }
        return ((((hashCode + i) * 31) + this.mQuality) * 31) + (this.mRequiresNetworkConnection ? MetricsEvent.AUTOFILL_SERVICE_DISABLED_APP : MetricsEvent.ANOMALY_TYPE_UNOPTIMIZED_BT);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Voice other = (Voice) obj;
        Set set = this.mFeatures;
        if (set == null) {
            if (other.mFeatures != null) {
                return false;
            }
        } else if (!set.equals(other.mFeatures)) {
            return false;
        }
        if (this.mLatency != other.mLatency) {
            return false;
        }
        Locale locale = this.mLocale;
        if (locale == null) {
            if (other.mLocale != null) {
                return false;
            }
        } else if (!locale.equals(other.mLocale)) {
            return false;
        }
        String str = this.mName;
        if (str == null) {
            if (other.mName != null) {
                return false;
            }
        } else if (!str.equals(other.mName)) {
            return false;
        }
        if (this.mQuality == other.mQuality && this.mRequiresNetworkConnection == other.mRequiresNetworkConnection) {
            return true;
        }
        return false;
    }
}
