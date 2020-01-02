package android.telecom;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

public final class PhoneAccountSuggestion implements Parcelable {
    public static final Creator<PhoneAccountSuggestion> CREATOR = new Creator<PhoneAccountSuggestion>() {
        public PhoneAccountSuggestion createFromParcel(Parcel in) {
            return new PhoneAccountSuggestion(in, null);
        }

        public PhoneAccountSuggestion[] newArray(int size) {
            return new PhoneAccountSuggestion[size];
        }
    };
    public static final int REASON_FREQUENT = 2;
    public static final int REASON_INTRA_CARRIER = 1;
    public static final int REASON_NONE = 0;
    public static final int REASON_OTHER = 4;
    public static final int REASON_USER_SET = 3;
    private PhoneAccountHandle mHandle;
    private int mReason;
    private boolean mShouldAutoSelect;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SuggestionReason {
    }

    /* synthetic */ PhoneAccountSuggestion(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public PhoneAccountSuggestion(PhoneAccountHandle handle, int reason, boolean shouldAutoSelect) {
        this.mHandle = handle;
        this.mReason = reason;
        this.mShouldAutoSelect = shouldAutoSelect;
    }

    private PhoneAccountSuggestion(Parcel in) {
        this.mHandle = (PhoneAccountHandle) in.readParcelable(PhoneAccountHandle.class.getClassLoader());
        this.mReason = in.readInt();
        this.mShouldAutoSelect = in.readByte() != (byte) 0;
    }

    public PhoneAccountHandle getPhoneAccountHandle() {
        return this.mHandle;
    }

    public int getReason() {
        return this.mReason;
    }

    public boolean shouldAutoSelect() {
        return this.mShouldAutoSelect;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mHandle, flags);
        dest.writeInt(this.mReason);
        dest.writeByte((byte) this.mShouldAutoSelect);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PhoneAccountSuggestion that = (PhoneAccountSuggestion) o;
        if (!(this.mReason == that.mReason && this.mShouldAutoSelect == that.mShouldAutoSelect && Objects.equals(this.mHandle, that.mHandle))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mHandle, Integer.valueOf(this.mReason), Boolean.valueOf(this.mShouldAutoSelect)});
    }
}
