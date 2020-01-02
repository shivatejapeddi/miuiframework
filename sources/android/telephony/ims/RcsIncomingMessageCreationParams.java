package android.telephony.ims;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class RcsIncomingMessageCreationParams extends RcsMessageCreationParams implements Parcelable {
    public static final Creator<RcsIncomingMessageCreationParams> CREATOR = new Creator<RcsIncomingMessageCreationParams>() {
        public RcsIncomingMessageCreationParams createFromParcel(Parcel in) {
            return new RcsIncomingMessageCreationParams(in, null);
        }

        public RcsIncomingMessageCreationParams[] newArray(int size) {
            return new RcsIncomingMessageCreationParams[size];
        }
    };
    private final long mArrivalTimestamp;
    private final long mSeenTimestamp;
    private final int mSenderParticipantId;

    public static class Builder extends android.telephony.ims.RcsMessageCreationParams.Builder {
        private long mArrivalTimestamp;
        private long mSeenTimestamp;
        private RcsParticipant mSenderParticipant;

        public Builder(long originationTimestamp, long arrivalTimestamp, int subscriptionId) {
            super(originationTimestamp, subscriptionId);
            this.mArrivalTimestamp = arrivalTimestamp;
        }

        public Builder setSenderParticipant(RcsParticipant senderParticipant) {
            this.mSenderParticipant = senderParticipant;
            return this;
        }

        public Builder setArrivalTimestamp(long arrivalTimestamp) {
            this.mArrivalTimestamp = arrivalTimestamp;
            return this;
        }

        public Builder setSeenTimestamp(long seenTimestamp) {
            this.mSeenTimestamp = seenTimestamp;
            return this;
        }

        public RcsIncomingMessageCreationParams build() {
            return new RcsIncomingMessageCreationParams(this, null);
        }
    }

    private RcsIncomingMessageCreationParams(Builder builder) {
        super((android.telephony.ims.RcsMessageCreationParams.Builder) builder);
        this.mArrivalTimestamp = builder.mArrivalTimestamp;
        this.mSeenTimestamp = builder.mSeenTimestamp;
        this.mSenderParticipantId = builder.mSenderParticipant.getId();
    }

    private RcsIncomingMessageCreationParams(Parcel in) {
        super(in);
        this.mArrivalTimestamp = in.readLong();
        this.mSeenTimestamp = in.readLong();
        this.mSenderParticipantId = in.readInt();
    }

    public long getArrivalTimestamp() {
        return this.mArrivalTimestamp;
    }

    public long getSeenTimestamp() {
        return this.mSeenTimestamp;
    }

    public int getSenderParticipantId() {
        return this.mSenderParticipantId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest);
        dest.writeLong(this.mArrivalTimestamp);
        dest.writeLong(this.mSeenTimestamp);
        dest.writeInt(this.mSenderParticipantId);
    }
}
