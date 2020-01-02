package android.telephony.ims;

import android.annotation.SystemApi;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telecom.Log;
import android.telephony.Rlog;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public final class ImsExternalCallState implements Parcelable {
    public static final int CALL_STATE_CONFIRMED = 1;
    public static final int CALL_STATE_TERMINATED = 2;
    public static final Creator<ImsExternalCallState> CREATOR = new Creator<ImsExternalCallState>() {
        public ImsExternalCallState createFromParcel(Parcel in) {
            return new ImsExternalCallState(in);
        }

        public ImsExternalCallState[] newArray(int size) {
            return new ImsExternalCallState[size];
        }
    };
    private static final String TAG = "ImsExternalCallState";
    private Uri mAddress;
    private int mCallId;
    private int mCallState;
    private int mCallType;
    private boolean mIsHeld;
    private boolean mIsPullable;
    private Uri mLocalAddress;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ExternalCallState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ExternalCallType {
    }

    public ImsExternalCallState(int callId, Uri address, boolean isPullable, int callState, int callType, boolean isCallheld) {
        this.mCallId = callId;
        this.mAddress = address;
        this.mIsPullable = isPullable;
        this.mCallState = callState;
        this.mCallType = callType;
        this.mIsHeld = isCallheld;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ImsExternalCallState = ");
        stringBuilder.append(this);
        Rlog.d(TAG, stringBuilder.toString());
    }

    public ImsExternalCallState(int callId, Uri address, Uri localAddress, boolean isPullable, int callState, int callType, boolean isCallheld) {
        this.mCallId = callId;
        this.mAddress = address;
        this.mLocalAddress = localAddress;
        this.mIsPullable = isPullable;
        this.mCallState = callState;
        this.mCallType = callType;
        this.mIsHeld = isCallheld;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ImsExternalCallState = ");
        stringBuilder.append(this);
        Rlog.d(TAG, stringBuilder.toString());
    }

    public ImsExternalCallState(String callId, Uri address, Uri localAddress, boolean isPullable, int callState, int callType, boolean isCallheld) {
        this.mCallId = getIdForString(callId);
        this.mAddress = address;
        this.mLocalAddress = localAddress;
        this.mIsPullable = isPullable;
        this.mCallState = callState;
        this.mCallType = callType;
        this.mIsHeld = isCallheld;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ImsExternalCallState = ");
        stringBuilder.append(this);
        Rlog.d(TAG, stringBuilder.toString());
    }

    public ImsExternalCallState(Parcel in) {
        this.mCallId = in.readInt();
        ClassLoader classLoader = ImsExternalCallState.class.getClassLoader();
        this.mAddress = (Uri) in.readParcelable(classLoader);
        this.mLocalAddress = (Uri) in.readParcelable(classLoader);
        boolean z = true;
        this.mIsPullable = in.readInt() != 0;
        this.mCallState = in.readInt();
        this.mCallType = in.readInt();
        if (in.readInt() == 0) {
            z = false;
        }
        this.mIsHeld = z;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ImsExternalCallState const = ");
        stringBuilder.append(this);
        Rlog.d(TAG, stringBuilder.toString());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mCallId);
        out.writeParcelable(this.mAddress, 0);
        out.writeParcelable(this.mLocalAddress, 0);
        out.writeInt(this.mIsPullable);
        out.writeInt(this.mCallState);
        out.writeInt(this.mCallType);
        out.writeInt(this.mIsHeld);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ImsExternalCallState writeToParcel = ");
        stringBuilder.append(out.toString());
        Rlog.d(TAG, stringBuilder.toString());
    }

    public int getCallId() {
        return this.mCallId;
    }

    public Uri getAddress() {
        return this.mAddress;
    }

    public Uri getLocalAddress() {
        return this.mLocalAddress;
    }

    public boolean isCallPullable() {
        return this.mIsPullable;
    }

    public int getCallState() {
        return this.mCallState;
    }

    public int getCallType() {
        return this.mCallType;
    }

    public boolean isCallHeld() {
        return this.mIsHeld;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ImsExternalCallState { mCallId = ");
        stringBuilder.append(this.mCallId);
        stringBuilder.append(", mAddress = ");
        stringBuilder.append(Log.pii(this.mAddress));
        stringBuilder.append(", mLocalAddress = ");
        stringBuilder.append(Log.pii(this.mLocalAddress));
        stringBuilder.append(", mIsPullable = ");
        stringBuilder.append(this.mIsPullable);
        stringBuilder.append(", mCallState = ");
        stringBuilder.append(this.mCallState);
        stringBuilder.append(", mCallType = ");
        stringBuilder.append(this.mCallType);
        stringBuilder.append(", mIsHeld = ");
        stringBuilder.append(this.mIsHeld);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private int getIdForString(String idString) {
        try {
            return Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return idString.hashCode();
        }
    }
}
