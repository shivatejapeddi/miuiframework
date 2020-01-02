package android.telecom;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import miui.telephony.PhoneNumberUtils;

public class ConferenceParticipant implements Parcelable {
    private static final String ANONYMOUS_INVALID_HOST = "anonymous.invalid";
    public static final Creator<ConferenceParticipant> CREATOR = new Creator<ConferenceParticipant>() {
        public ConferenceParticipant createFromParcel(Parcel source) {
            Parcel parcel = source;
            ClassLoader classLoader = ParcelableCall.class.getClassLoader();
            Uri handle = (Uri) parcel.readParcelable(classLoader);
            String displayName = source.readString();
            Uri endpoint = (Uri) parcel.readParcelable(classLoader);
            int state = source.readInt();
            long connectTime = source.readLong();
            long elapsedRealTime = source.readLong();
            int callDirection = source.readInt();
            int callDirection2 = callDirection;
            ConferenceParticipant participant = new ConferenceParticipant(handle, displayName, endpoint, state, callDirection);
            participant.setConnectTime(connectTime);
            participant.setConnectElapsedTime(elapsedRealTime);
            participant.setCallDirection(callDirection2);
            return participant;
        }

        public ConferenceParticipant[] newArray(int size) {
            return new ConferenceParticipant[size];
        }
    };
    private int mCallDirection;
    private long mConnectElapsedTime;
    private long mConnectTime;
    private final String mDisplayName;
    private final Uri mEndpoint;
    private final Uri mHandle;
    private final int mState;

    public ConferenceParticipant(Uri handle, String displayName, Uri endpoint, int state, int callDirection) {
        this.mHandle = handle;
        this.mDisplayName = displayName;
        this.mEndpoint = endpoint;
        this.mState = state;
        this.mCallDirection = callDirection;
    }

    public int describeContents() {
        return 0;
    }

    @VisibleForTesting
    public int getParticipantPresentation() {
        Uri address = getHandle();
        if (address == null) {
            return 2;
        }
        String number = address.getSchemeSpecificPart();
        if (TextUtils.isEmpty(number)) {
            return 2;
        }
        String[] numberParts = number.split("[;]")[null].split("[@]");
        if (numberParts.length == 2 && numberParts[1].equals(ANONYMOUS_INVALID_HOST)) {
            return 2;
        }
        return 1;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mHandle, 0);
        dest.writeString(this.mDisplayName);
        dest.writeParcelable(this.mEndpoint, 0);
        dest.writeInt(this.mState);
        dest.writeLong(this.mConnectTime);
        dest.writeLong(this.mConnectElapsedTime);
        dest.writeInt(this.mCallDirection);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ConferenceParticipant Handle: ");
        sb.append(PhoneNumberUtils.toLogSafePhoneNumber(this.mHandle.toString()));
        sb.append(" DisplayName: ");
        sb.append(Log.pii(this.mDisplayName));
        sb.append(" Endpoint: ");
        sb.append(Log.pii(this.mEndpoint));
        sb.append(" State: ");
        sb.append(Connection.stateToString(this.mState));
        sb.append(" ConnectTime: ");
        sb.append(getConnectTime());
        sb.append(" ConnectElapsedTime: ");
        sb.append(getConnectElapsedTime());
        sb.append(" Direction: ");
        sb.append(getCallDirection() == 0 ? "Incoming" : "Outgoing");
        sb.append("]");
        return sb.toString();
    }

    public Uri getHandle() {
        return this.mHandle;
    }

    public String getDisplayName() {
        return this.mDisplayName;
    }

    public Uri getEndpoint() {
        return this.mEndpoint;
    }

    public int getState() {
        return this.mState;
    }

    public long getConnectTime() {
        return this.mConnectTime;
    }

    public void setConnectTime(long connectTime) {
        this.mConnectTime = connectTime;
    }

    public long getConnectElapsedTime() {
        return this.mConnectElapsedTime;
    }

    public void setConnectElapsedTime(long connectElapsedTime) {
        this.mConnectElapsedTime = connectElapsedTime;
    }

    public int getCallDirection() {
        return this.mCallDirection;
    }

    public void setCallDirection(int callDirection) {
        this.mCallDirection = callDirection;
    }

    @VisibleForTesting
    public static Uri getParticipantAddress(Uri address, String countryIso) {
        if (address == null) {
            return address;
        }
        String number = address.getSchemeSpecificPart();
        if (TextUtils.isEmpty(number)) {
            return address;
        }
        String[] numberParts = number.split("[@;:]");
        if (numberParts.length == 0) {
            return address;
        }
        number = numberParts[0];
        String formattedNumber = null;
        if (!TextUtils.isEmpty(countryIso)) {
            formattedNumber = android.telephony.PhoneNumberUtils.formatNumberToE164(number, countryIso);
        }
        return Uri.fromParts(PhoneAccount.SCHEME_TEL, formattedNumber != null ? formattedNumber : number, null);
    }
}
