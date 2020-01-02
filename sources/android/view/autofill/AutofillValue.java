package android.view.autofill;

import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.Objects;

public final class AutofillValue implements Parcelable {
    public static final Creator<AutofillValue> CREATOR = new Creator<AutofillValue>() {
        public AutofillValue createFromParcel(Parcel source) {
            return new AutofillValue(source, null);
        }

        public AutofillValue[] newArray(int size) {
            return new AutofillValue[size];
        }
    };
    private static final String TAG = "AutofillValue";
    private final int mType;
    private final Object mValue;

    private AutofillValue(int type, Object value) {
        this.mType = type;
        this.mValue = value;
    }

    public CharSequence getTextValue() {
        boolean isText = isText();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("value must be a text value, not type=");
        stringBuilder.append(this.mType);
        Preconditions.checkState(isText, stringBuilder.toString());
        return (CharSequence) this.mValue;
    }

    public boolean isText() {
        return this.mType == 1;
    }

    public boolean getToggleValue() {
        boolean isToggle = isToggle();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("value must be a toggle value, not type=");
        stringBuilder.append(this.mType);
        Preconditions.checkState(isToggle, stringBuilder.toString());
        return ((Boolean) this.mValue).booleanValue();
    }

    public boolean isToggle() {
        return this.mType == 2;
    }

    public int getListValue() {
        boolean isList = isList();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("value must be a list value, not type=");
        stringBuilder.append(this.mType);
        Preconditions.checkState(isList, stringBuilder.toString());
        return ((Integer) this.mValue).intValue();
    }

    public boolean isList() {
        return this.mType == 3;
    }

    public long getDateValue() {
        boolean isDate = isDate();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("value must be a date value, not type=");
        stringBuilder.append(this.mType);
        Preconditions.checkState(isDate, stringBuilder.toString());
        return ((Long) this.mValue).longValue();
    }

    public boolean isDate() {
        return this.mType == 4;
    }

    public boolean isEmpty() {
        return isText() && ((CharSequence) this.mValue).length() == 0;
    }

    public int hashCode() {
        return this.mType + this.mValue.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AutofillValue other = (AutofillValue) obj;
        if (this.mType != other.mType) {
            return false;
        }
        if (isText()) {
            return this.mValue.toString().equals(other.mValue.toString());
        }
        return Objects.equals(this.mValue, other.mValue);
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder string = new StringBuilder();
        string.append("[type=");
        string.append(this.mType);
        string = string.append(", value=");
        if (isText()) {
            Helper.appendRedacted(string, (CharSequence) this.mValue);
        } else {
            string.append(this.mValue);
        }
        string.append(']');
        return string.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mType);
        int i = this.mType;
        if (i == 1) {
            parcel.writeCharSequence((CharSequence) this.mValue);
        } else if (i == 2) {
            parcel.writeInt(((Boolean) this.mValue).booleanValue());
        } else if (i == 3) {
            parcel.writeInt(((Integer) this.mValue).intValue());
        } else if (i == 4) {
            parcel.writeLong(((Long) this.mValue).longValue());
        }
    }

    private AutofillValue(Parcel parcel) {
        this.mType = parcel.readInt();
        int i = this.mType;
        boolean z = true;
        if (i == 1) {
            this.mValue = parcel.readCharSequence();
        } else if (i == 2) {
            if (parcel.readInt() == 0) {
                z = false;
            }
            this.mValue = Boolean.valueOf(z);
        } else if (i == 3) {
            this.mValue = Integer.valueOf(parcel.readInt());
        } else if (i == 4) {
            this.mValue = Long.valueOf(parcel.readLong());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("type=");
            stringBuilder.append(this.mType);
            stringBuilder.append(" not valid");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static AutofillValue forText(CharSequence value) {
        if (Helper.sVerbose && !Looper.getMainLooper().isCurrentThread()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("forText() not called on main thread: ");
            stringBuilder.append(Thread.currentThread());
            Log.v(TAG, stringBuilder.toString());
        }
        if (value == null) {
            return null;
        }
        return new AutofillValue(1, TextUtils.trimNoCopySpans(value));
    }

    public static AutofillValue forToggle(boolean value) {
        return new AutofillValue(2, Boolean.valueOf(value));
    }

    public static AutofillValue forList(int value) {
        return new AutofillValue(3, Integer.valueOf(value));
    }

    public static AutofillValue forDate(long value) {
        return new AutofillValue(4, Long.valueOf(value));
    }
}
