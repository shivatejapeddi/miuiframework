package android.telephony.ims;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.ims.RcsTypeIdPair;
import java.util.ArrayList;
import java.util.List;

public class RcsMessageQueryResultParcelable implements Parcelable {
    public static final Creator<RcsMessageQueryResultParcelable> CREATOR = new Creator<RcsMessageQueryResultParcelable>() {
        public RcsMessageQueryResultParcelable createFromParcel(Parcel in) {
            return new RcsMessageQueryResultParcelable(in, null);
        }

        public RcsMessageQueryResultParcelable[] newArray(int size) {
            return new RcsMessageQueryResultParcelable[size];
        }
    };
    final RcsQueryContinuationToken mContinuationToken;
    final List<RcsTypeIdPair> mMessageTypeIdPairs;

    public RcsMessageQueryResultParcelable(RcsQueryContinuationToken continuationToken, List<RcsTypeIdPair> messageTypeIdPairs) {
        this.mContinuationToken = continuationToken;
        this.mMessageTypeIdPairs = messageTypeIdPairs;
    }

    private RcsMessageQueryResultParcelable(Parcel in) {
        this.mContinuationToken = (RcsQueryContinuationToken) in.readParcelable(RcsQueryContinuationToken.class.getClassLoader());
        this.mMessageTypeIdPairs = new ArrayList();
        in.readTypedList(this.mMessageTypeIdPairs, RcsTypeIdPair.CREATOR);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mContinuationToken, flags);
        dest.writeTypedList(this.mMessageTypeIdPairs);
    }
}
