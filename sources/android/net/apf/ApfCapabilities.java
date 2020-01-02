package android.net.apf;

import android.annotation.SystemApi;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.R;

@SystemApi
public final class ApfCapabilities implements Parcelable {
    public static final Creator<ApfCapabilities> CREATOR = new Creator<ApfCapabilities>() {
        public ApfCapabilities createFromParcel(Parcel in) {
            return new ApfCapabilities(in, null);
        }

        public ApfCapabilities[] newArray(int size) {
            return new ApfCapabilities[size];
        }
    };
    public final int apfPacketFormat;
    public final int apfVersionSupported;
    public final int maximumApfProgramSize;

    /* synthetic */ ApfCapabilities(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public ApfCapabilities(int apfVersionSupported, int maximumApfProgramSize, int apfPacketFormat) {
        this.apfVersionSupported = apfVersionSupported;
        this.maximumApfProgramSize = maximumApfProgramSize;
        this.apfPacketFormat = apfPacketFormat;
    }

    private ApfCapabilities(Parcel in) {
        this.apfVersionSupported = in.readInt();
        this.maximumApfProgramSize = in.readInt();
        this.apfPacketFormat = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.apfVersionSupported);
        dest.writeInt(this.maximumApfProgramSize);
        dest.writeInt(this.apfPacketFormat);
    }

    public String toString() {
        return String.format("%s{version: %d, maxSize: %d, format: %d}", new Object[]{getClass().getSimpleName(), Integer.valueOf(this.apfVersionSupported), Integer.valueOf(this.maximumApfProgramSize), Integer.valueOf(this.apfPacketFormat)});
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof ApfCapabilities)) {
            return false;
        }
        ApfCapabilities other = (ApfCapabilities) obj;
        if (this.apfVersionSupported == other.apfVersionSupported && this.maximumApfProgramSize == other.maximumApfProgramSize && this.apfPacketFormat == other.apfPacketFormat) {
            z = true;
        }
        return z;
    }

    public boolean hasDataAccess() {
        return this.apfVersionSupported >= 4;
    }

    public static boolean getApfDrop8023Frames() {
        return Resources.getSystem().getBoolean(R.bool.config_apfDrop802_3Frames);
    }

    public static int[] getApfEtherTypeBlackList() {
        return Resources.getSystem().getIntArray(R.array.config_apfEthTypeBlackList);
    }
}
