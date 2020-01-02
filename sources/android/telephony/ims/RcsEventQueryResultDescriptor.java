package android.telephony.ims;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RcsEventQueryResultDescriptor implements Parcelable {
    public static final Creator<RcsEventQueryResultDescriptor> CREATOR = new Creator<RcsEventQueryResultDescriptor>() {
        public RcsEventQueryResultDescriptor createFromParcel(Parcel in) {
            return new RcsEventQueryResultDescriptor(in);
        }

        public RcsEventQueryResultDescriptor[] newArray(int size) {
            return new RcsEventQueryResultDescriptor[size];
        }
    };
    private final RcsQueryContinuationToken mContinuationToken;
    private final List<RcsEventDescriptor> mEvents;

    public RcsEventQueryResultDescriptor(RcsQueryContinuationToken continuationToken, List<RcsEventDescriptor> events) {
        this.mContinuationToken = continuationToken;
        this.mEvents = events;
    }

    /* Access modifiers changed, original: protected */
    public RcsEventQueryResult getRcsEventQueryResult(RcsControllerCall rcsControllerCall) {
        return new RcsEventQueryResult(this.mContinuationToken, (List) this.mEvents.stream().map(new -$$Lambda$RcsEventQueryResultDescriptor$0eoTyoA0JNoBx53J3zXvi1fQcnA(rcsControllerCall)).collect(Collectors.toList()));
    }

    protected RcsEventQueryResultDescriptor(Parcel in) {
        this.mContinuationToken = (RcsQueryContinuationToken) in.readParcelable(RcsQueryContinuationToken.class.getClassLoader());
        this.mEvents = new LinkedList();
        in.readList(this.mEvents, null);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mContinuationToken, flags);
        dest.writeList(this.mEvents);
    }
}
