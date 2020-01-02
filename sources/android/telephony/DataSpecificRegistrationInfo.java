package android.telephony;

import android.annotation.SystemApi;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

@SystemApi
public final class DataSpecificRegistrationInfo implements Parcelable {
    public static final Creator<DataSpecificRegistrationInfo> CREATOR = new Creator<DataSpecificRegistrationInfo>() {
        public DataSpecificRegistrationInfo createFromParcel(Parcel source) {
            return new DataSpecificRegistrationInfo(source, null);
        }

        public DataSpecificRegistrationInfo[] newArray(int size) {
            return new DataSpecificRegistrationInfo[size];
        }
    };
    public final boolean isDcNrRestricted;
    public final boolean isEnDcAvailable;
    public final boolean isNrAvailable;
    public boolean mIsUsingCarrierAggregation;
    private final LteVopsSupportInfo mLteVopsSupportInfo;
    public final int maxDataCalls;

    /* synthetic */ DataSpecificRegistrationInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    DataSpecificRegistrationInfo(int maxDataCalls, boolean isDcNrRestricted, boolean isNrAvailable, boolean isEnDcAvailable, LteVopsSupportInfo lteVops, boolean isUsingCarrierAggregation) {
        this.maxDataCalls = maxDataCalls;
        this.isDcNrRestricted = isDcNrRestricted;
        this.isNrAvailable = isNrAvailable;
        this.isEnDcAvailable = isEnDcAvailable;
        this.mLteVopsSupportInfo = lteVops;
        this.mIsUsingCarrierAggregation = isUsingCarrierAggregation;
    }

    DataSpecificRegistrationInfo(DataSpecificRegistrationInfo dsri) {
        this.maxDataCalls = dsri.maxDataCalls;
        this.isDcNrRestricted = dsri.isDcNrRestricted;
        this.isNrAvailable = dsri.isNrAvailable;
        this.isEnDcAvailable = dsri.isEnDcAvailable;
        this.mLteVopsSupportInfo = dsri.mLteVopsSupportInfo;
        this.mIsUsingCarrierAggregation = dsri.mIsUsingCarrierAggregation;
    }

    private DataSpecificRegistrationInfo(Parcel source) {
        this.maxDataCalls = source.readInt();
        this.isDcNrRestricted = source.readBoolean();
        this.isNrAvailable = source.readBoolean();
        this.isEnDcAvailable = source.readBoolean();
        this.mLteVopsSupportInfo = (LteVopsSupportInfo) LteVopsSupportInfo.CREATOR.createFromParcel(source);
        this.mIsUsingCarrierAggregation = source.readBoolean();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxDataCalls);
        dest.writeBoolean(this.isDcNrRestricted);
        dest.writeBoolean(this.isNrAvailable);
        dest.writeBoolean(this.isEnDcAvailable);
        this.mLteVopsSupportInfo.writeToParcel(dest, flags);
        dest.writeBoolean(this.mIsUsingCarrierAggregation);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getName());
        stringBuilder.append(" :{");
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" maxDataCalls = ");
        stringBuilder2.append(this.maxDataCalls);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" isDcNrRestricted = ");
        stringBuilder2.append(this.isDcNrRestricted);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" isNrAvailable = ");
        stringBuilder2.append(this.isNrAvailable);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" isEnDcAvailable = ");
        stringBuilder2.append(this.isEnDcAvailable);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        stringBuilder2.append(this.mLteVopsSupportInfo.toString());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" mIsUsingCarrierAggregation = ");
        stringBuilder2.append(this.mIsUsingCarrierAggregation);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.maxDataCalls), Boolean.valueOf(this.isDcNrRestricted), Boolean.valueOf(this.isNrAvailable), Boolean.valueOf(this.isEnDcAvailable), this.mLteVopsSupportInfo, Boolean.valueOf(this.mIsUsingCarrierAggregation)});
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataSpecificRegistrationInfo)) {
            return false;
        }
        DataSpecificRegistrationInfo other = (DataSpecificRegistrationInfo) o;
        if (!(this.maxDataCalls == other.maxDataCalls && this.isDcNrRestricted == other.isDcNrRestricted && this.isNrAvailable == other.isNrAvailable && this.isEnDcAvailable == other.isEnDcAvailable && this.mLteVopsSupportInfo.equals(other.mLteVopsSupportInfo) && this.mIsUsingCarrierAggregation == other.mIsUsingCarrierAggregation)) {
            z = false;
        }
        return z;
    }

    public LteVopsSupportInfo getLteVopsSupportInfo() {
        return this.mLteVopsSupportInfo;
    }

    public void setIsUsingCarrierAggregation(boolean isUsingCarrierAggregation) {
        this.mIsUsingCarrierAggregation = isUsingCarrierAggregation;
    }

    public boolean isUsingCarrierAggregation() {
        return this.mIsUsingCarrierAggregation;
    }
}
