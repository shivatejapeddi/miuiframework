package android.service.autofill;

import android.icu.text.DateFormat;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.Date;

public final class DateTransformation extends InternalTransformation implements Transformation, Parcelable {
    public static final Creator<DateTransformation> CREATOR = new Creator<DateTransformation>() {
        public DateTransformation createFromParcel(Parcel parcel) {
            return new DateTransformation((AutofillId) parcel.readParcelable(null), (DateFormat) parcel.readSerializable());
        }

        public DateTransformation[] newArray(int size) {
            return new DateTransformation[size];
        }
    };
    private static final String TAG = "DateTransformation";
    private final DateFormat mDateFormat;
    private final AutofillId mFieldId;

    public DateTransformation(AutofillId id, DateFormat dateFormat) {
        this.mFieldId = (AutofillId) Preconditions.checkNotNull(id);
        this.mDateFormat = (DateFormat) Preconditions.checkNotNull(dateFormat);
    }

    public void apply(ValueFinder finder, RemoteViews parentTemplate, int childViewId) throws Exception {
        String str = " to ";
        AutofillValue value = finder.findRawValueByAutofillId(this.mFieldId);
        String str2 = TAG;
        StringBuilder stringBuilder;
        if (value == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("No value for id ");
            stringBuilder.append(this.mFieldId);
            Log.w(str2, stringBuilder.toString());
        } else if (value.isDate()) {
            try {
                Date date = new Date(value.getDateValue());
                String transformed = this.mDateFormat.format(date);
                if (Helper.sDebug) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Transformed ");
                    stringBuilder2.append(date);
                    stringBuilder2.append(str);
                    stringBuilder2.append(transformed);
                    Log.d(str2, stringBuilder2.toString());
                }
                parentTemplate.setCharSequence(childViewId, "setText", transformed);
            } catch (Exception e) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Could not apply ");
                stringBuilder3.append(this.mDateFormat);
                stringBuilder3.append(str);
                stringBuilder3.append(value);
                stringBuilder3.append(": ");
                stringBuilder3.append(e);
                Log.w(str2, stringBuilder3.toString());
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Value for ");
            stringBuilder.append(this.mFieldId);
            stringBuilder.append(" is not date: ");
            stringBuilder.append(value);
            Log.w(str2, stringBuilder.toString());
        }
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DateTransformation: [id=");
        stringBuilder.append(this.mFieldId);
        stringBuilder.append(", format=");
        stringBuilder.append(this.mDateFormat);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(this.mFieldId, flags);
        parcel.writeSerializable(this.mDateFormat);
    }
}
