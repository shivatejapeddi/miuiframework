package android.service.euicc;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class DownloadSubscriptionResult implements Parcelable {
    public static final Creator<DownloadSubscriptionResult> CREATOR = new Creator<DownloadSubscriptionResult>() {
        public DownloadSubscriptionResult createFromParcel(Parcel in) {
            return new DownloadSubscriptionResult(in, null);
        }

        public DownloadSubscriptionResult[] newArray(int size) {
            return new DownloadSubscriptionResult[size];
        }
    };
    private final int mCardId;
    private final int mResolvableErrors;
    private final int mResult;

    /* synthetic */ DownloadSubscriptionResult(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public DownloadSubscriptionResult(int result, int resolvableErrors, int cardId) {
        this.mResult = result;
        this.mResolvableErrors = resolvableErrors;
        this.mCardId = cardId;
    }

    public int getResult() {
        return this.mResult;
    }

    public int getResolvableErrors() {
        return this.mResolvableErrors;
    }

    public int getCardId() {
        return this.mCardId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mResult);
        dest.writeInt(this.mResolvableErrors);
        dest.writeInt(this.mCardId);
    }

    public int describeContents() {
        return 0;
    }

    private DownloadSubscriptionResult(Parcel in) {
        this.mResult = in.readInt();
        this.mResolvableErrors = in.readInt();
        this.mCardId = in.readInt();
    }
}
