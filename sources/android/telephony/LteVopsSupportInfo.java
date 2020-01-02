package android.telephony;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public final class LteVopsSupportInfo implements Parcelable {
    public static final Creator<LteVopsSupportInfo> CREATOR = new Creator<LteVopsSupportInfo>() {
        public LteVopsSupportInfo createFromParcel(Parcel in) {
            return new LteVopsSupportInfo(in, null);
        }

        public LteVopsSupportInfo[] newArray(int size) {
            return new LteVopsSupportInfo[size];
        }
    };
    public static final int LTE_STATUS_NOT_AVAILABLE = 1;
    public static final int LTE_STATUS_NOT_SUPPORTED = 3;
    public static final int LTE_STATUS_SUPPORTED = 2;
    private final int mEmcBearerSupport;
    private final int mVopsSupport;

    @Retention(RetentionPolicy.SOURCE)
    public @interface LteVopsStatus {
    }

    public LteVopsSupportInfo(int vops, int emergency) {
        this.mVopsSupport = vops;
        this.mEmcBearerSupport = emergency;
    }

    public int getVopsSupport() {
        return this.mVopsSupport;
    }

    public int getEmcBearerSupport() {
        return this.mEmcBearerSupport;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mVopsSupport);
        out.writeInt(this.mEmcBearerSupport);
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (o == null || !(o instanceof LteVopsSupportInfo)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        LteVopsSupportInfo other = (LteVopsSupportInfo) o;
        if (this.mVopsSupport == other.mVopsSupport && this.mEmcBearerSupport == other.mEmcBearerSupport) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mVopsSupport), Integer.valueOf(this.mEmcBearerSupport)});
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LteVopsSupportInfo :  mVopsSupport = ");
        stringBuilder.append(this.mVopsSupport);
        stringBuilder.append(" mEmcBearerSupport = ");
        stringBuilder.append(this.mEmcBearerSupport);
        return stringBuilder.toString();
    }

    private LteVopsSupportInfo(Parcel in) {
        this.mVopsSupport = in.readInt();
        this.mEmcBearerSupport = in.readInt();
    }
}
