package android.service.autofill;

import android.icu.text.DateFormat;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.view.autofill.AutofillValue;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;
import java.util.Date;

public final class DateValueSanitizer extends InternalSanitizer implements Sanitizer, Parcelable {
    public static final Creator<DateValueSanitizer> CREATOR = new Creator<DateValueSanitizer>() {
        public DateValueSanitizer createFromParcel(Parcel parcel) {
            return new DateValueSanitizer((DateFormat) parcel.readSerializable());
        }

        public DateValueSanitizer[] newArray(int size) {
            return new DateValueSanitizer[size];
        }
    };
    private static final String TAG = "DateValueSanitizer";
    private final DateFormat mDateFormat;

    public DateValueSanitizer(DateFormat dateFormat) {
        this.mDateFormat = (DateFormat) Preconditions.checkNotNull(dateFormat);
    }

    public AutofillValue sanitize(AutofillValue value) {
        String str = " to ";
        String str2 = TAG;
        if (value == null) {
            Log.w(str2, "sanitize() called with null value");
            return null;
        } else if (value.isDate()) {
            try {
                Date date = new Date(value.getDateValue());
                String converted = this.mDateFormat.format(date);
                if (Helper.sDebug) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Transformed ");
                    stringBuilder.append(date);
                    stringBuilder.append(str);
                    stringBuilder.append(converted);
                    Log.d(str2, stringBuilder.toString());
                }
                Date sanitized = this.mDateFormat.parse(converted);
                if (Helper.sDebug) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Sanitized to ");
                    stringBuilder2.append(sanitized);
                    Log.d(str2, stringBuilder2.toString());
                }
                return AutofillValue.forDate(sanitized.getTime());
            } catch (Exception e) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Could not apply ");
                stringBuilder3.append(this.mDateFormat);
                stringBuilder3.append(str);
                stringBuilder3.append(value);
                stringBuilder3.append(": ");
                stringBuilder3.append(e);
                Log.w(str2, stringBuilder3.toString());
                return null;
            }
        } else {
            if (Helper.sDebug) {
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append(value);
                stringBuilder4.append(" is not a date");
                Log.d(str2, stringBuilder4.toString());
            }
            return null;
        }
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DateValueSanitizer: [dateFormat=");
        stringBuilder.append(this.mDateFormat);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeSerializable(this.mDateFormat);
    }
}
