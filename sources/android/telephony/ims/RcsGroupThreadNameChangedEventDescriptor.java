package android.telephony.ims;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;

public class RcsGroupThreadNameChangedEventDescriptor extends RcsGroupThreadEventDescriptor {
    public static final Creator<RcsGroupThreadNameChangedEventDescriptor> CREATOR = new Creator<RcsGroupThreadNameChangedEventDescriptor>() {
        public RcsGroupThreadNameChangedEventDescriptor createFromParcel(Parcel in) {
            return new RcsGroupThreadNameChangedEventDescriptor(in);
        }

        public RcsGroupThreadNameChangedEventDescriptor[] newArray(int size) {
            return new RcsGroupThreadNameChangedEventDescriptor[size];
        }
    };
    private final String mNewName;

    public RcsGroupThreadNameChangedEventDescriptor(long timestamp, int rcsGroupThreadId, int originatingParticipantId, String newName) {
        super(timestamp, rcsGroupThreadId, originatingParticipantId);
        this.mNewName = newName;
    }

    @VisibleForTesting(visibility = Visibility.PROTECTED)
    public RcsGroupThreadNameChangedEvent createRcsEvent(RcsControllerCall rcsControllerCall) {
        return new RcsGroupThreadNameChangedEvent(this.mTimestamp, new RcsGroupThread(rcsControllerCall, this.mRcsGroupThreadId), new RcsParticipant(rcsControllerCall, this.mOriginatingParticipantId), this.mNewName);
    }

    protected RcsGroupThreadNameChangedEventDescriptor(Parcel in) {
        super(in);
        this.mNewName = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mNewName);
    }
}
