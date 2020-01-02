package android.telephony.ims;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;

public abstract class RcsEventDescriptor implements Parcelable {
    protected final long mTimestamp;

    @VisibleForTesting(visibility = Visibility.PROTECTED)
    public abstract RcsEvent createRcsEvent(RcsControllerCall rcsControllerCall);

    RcsEventDescriptor(long timestamp) {
        this.mTimestamp = timestamp;
    }

    RcsEventDescriptor(Parcel in) {
        this.mTimestamp = in.readLong();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mTimestamp);
    }

    public int describeContents() {
        return 0;
    }
}
