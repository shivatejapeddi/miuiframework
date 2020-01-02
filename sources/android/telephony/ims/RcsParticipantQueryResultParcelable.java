package android.telephony.ims;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public final class RcsParticipantQueryResultParcelable implements Parcelable {
    public static final Creator<RcsParticipantQueryResultParcelable> CREATOR = new Creator<RcsParticipantQueryResultParcelable>() {
        public RcsParticipantQueryResultParcelable createFromParcel(Parcel in) {
            return new RcsParticipantQueryResultParcelable(in, null);
        }

        public RcsParticipantQueryResultParcelable[] newArray(int size) {
            return new RcsParticipantQueryResultParcelable[size];
        }
    };
    final RcsQueryContinuationToken mContinuationToken;
    final List<Integer> mParticipantIds;

    public RcsParticipantQueryResultParcelable(RcsQueryContinuationToken continuationToken, List<Integer> participantIds) {
        this.mContinuationToken = continuationToken;
        this.mParticipantIds = participantIds;
    }

    private RcsParticipantQueryResultParcelable(Parcel in) {
        this.mContinuationToken = (RcsQueryContinuationToken) in.readParcelable(RcsQueryContinuationToken.class.getClassLoader());
        this.mParticipantIds = new ArrayList();
        in.readList(this.mParticipantIds, Integer.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mContinuationToken, flags);
        dest.writeList(this.mParticipantIds);
    }
}
