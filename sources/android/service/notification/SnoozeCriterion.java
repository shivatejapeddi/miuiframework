package android.service.notification;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class SnoozeCriterion implements Parcelable {
    public static final Creator<SnoozeCriterion> CREATOR = new Creator<SnoozeCriterion>() {
        public SnoozeCriterion createFromParcel(Parcel in) {
            return new SnoozeCriterion(in);
        }

        public SnoozeCriterion[] newArray(int size) {
            return new SnoozeCriterion[size];
        }
    };
    private final CharSequence mConfirmation;
    private final CharSequence mExplanation;
    private final String mId;

    public SnoozeCriterion(String id, CharSequence explanation, CharSequence confirmation) {
        this.mId = id;
        this.mExplanation = explanation;
        this.mConfirmation = confirmation;
    }

    protected SnoozeCriterion(Parcel in) {
        if (in.readByte() != (byte) 0) {
            this.mId = in.readString();
        } else {
            this.mId = null;
        }
        if (in.readByte() != (byte) 0) {
            this.mExplanation = in.readCharSequence();
        } else {
            this.mExplanation = null;
        }
        if (in.readByte() != (byte) 0) {
            this.mConfirmation = in.readCharSequence();
        } else {
            this.mConfirmation = null;
        }
    }

    public String getId() {
        return this.mId;
    }

    public CharSequence getExplanation() {
        return this.mExplanation;
    }

    public CharSequence getConfirmation() {
        return this.mConfirmation;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.mId != null) {
            dest.writeByte((byte) 1);
            dest.writeString(this.mId);
        } else {
            dest.writeByte((byte) 0);
        }
        if (this.mExplanation != null) {
            dest.writeByte((byte) 1);
            dest.writeCharSequence(this.mExplanation);
        } else {
            dest.writeByte((byte) 0);
        }
        if (this.mConfirmation != null) {
            dest.writeByte((byte) 1);
            dest.writeCharSequence(this.mConfirmation);
            return;
        }
        dest.writeByte((byte) 0);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SnoozeCriterion that = (SnoozeCriterion) o;
        String str = this.mId;
        if (!str == null ? str.equals(that.mId) : that.mId == null) {
            return false;
        }
        CharSequence charSequence = this.mExplanation;
        if (!charSequence == null ? charSequence.equals(that.mExplanation) : that.mExplanation == null) {
            return false;
        }
        charSequence = this.mConfirmation;
        if (charSequence != null) {
            z = charSequence.equals(that.mConfirmation);
        } else if (that.mConfirmation != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        String str = this.mId;
        int i = 0;
        int result = (str != null ? str.hashCode() : 0) * 31;
        CharSequence charSequence = this.mExplanation;
        int hashCode = (result + (charSequence != null ? charSequence.hashCode() : 0)) * 31;
        charSequence = this.mConfirmation;
        if (charSequence != null) {
            i = charSequence.hashCode();
        }
        return hashCode + i;
    }
}
