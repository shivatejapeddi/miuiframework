package android.service.euicc;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.euicc.DownloadableSubscription;
import java.util.Arrays;
import java.util.List;

@SystemApi
public final class GetDefaultDownloadableSubscriptionListResult implements Parcelable {
    public static final Creator<GetDefaultDownloadableSubscriptionListResult> CREATOR = new Creator<GetDefaultDownloadableSubscriptionListResult>() {
        public GetDefaultDownloadableSubscriptionListResult createFromParcel(Parcel in) {
            return new GetDefaultDownloadableSubscriptionListResult(in, null);
        }

        public GetDefaultDownloadableSubscriptionListResult[] newArray(int size) {
            return new GetDefaultDownloadableSubscriptionListResult[size];
        }
    };
    private final DownloadableSubscription[] mSubscriptions;
    @Deprecated
    @UnsupportedAppUsage
    public final int result;

    public int getResult() {
        return this.result;
    }

    public List<DownloadableSubscription> getDownloadableSubscriptions() {
        DownloadableSubscription[] downloadableSubscriptionArr = this.mSubscriptions;
        if (downloadableSubscriptionArr == null) {
            return null;
        }
        return Arrays.asList(downloadableSubscriptionArr);
    }

    public GetDefaultDownloadableSubscriptionListResult(int result, DownloadableSubscription[] subscriptions) {
        this.result = result;
        if (this.result == 0) {
            this.mSubscriptions = subscriptions;
        } else if (subscriptions == null) {
            this.mSubscriptions = null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error result with non-null subscriptions: ");
            stringBuilder.append(result);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private GetDefaultDownloadableSubscriptionListResult(Parcel in) {
        this.result = in.readInt();
        this.mSubscriptions = (DownloadableSubscription[]) in.createTypedArray(DownloadableSubscription.CREATOR);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.result);
        dest.writeTypedArray(this.mSubscriptions, flags);
    }

    public int describeContents() {
        return 0;
    }
}
