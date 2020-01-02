package android.telephony.ims;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class RcsOutgoingMessageCreationParams extends RcsMessageCreationParams implements Parcelable {
    public static final Creator<RcsOutgoingMessageCreationParams> CREATOR = new Creator<RcsOutgoingMessageCreationParams>() {
        public RcsOutgoingMessageCreationParams createFromParcel(Parcel in) {
            return new RcsOutgoingMessageCreationParams(in, null);
        }

        public RcsOutgoingMessageCreationParams[] newArray(int size) {
            return new RcsOutgoingMessageCreationParams[size];
        }
    };

    public static class Builder extends android.telephony.ims.RcsMessageCreationParams.Builder {
        public Builder(long originationTimestamp, int subscriptionId) {
            super(originationTimestamp, subscriptionId);
        }

        public RcsOutgoingMessageCreationParams build() {
            return new RcsOutgoingMessageCreationParams(this, null);
        }
    }

    private RcsOutgoingMessageCreationParams(Builder builder) {
        super((android.telephony.ims.RcsMessageCreationParams.Builder) builder);
    }

    private RcsOutgoingMessageCreationParams(Parcel in) {
        super(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest);
    }
}
