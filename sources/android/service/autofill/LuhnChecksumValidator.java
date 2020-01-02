package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Downloads;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;
import java.util.Arrays;

public final class LuhnChecksumValidator extends InternalValidator implements Validator, Parcelable {
    public static final Creator<LuhnChecksumValidator> CREATOR = new Creator<LuhnChecksumValidator>() {
        public LuhnChecksumValidator createFromParcel(Parcel parcel) {
            return new LuhnChecksumValidator((AutofillId[]) parcel.readParcelableArray(null, AutofillId.class));
        }

        public LuhnChecksumValidator[] newArray(int size) {
            return new LuhnChecksumValidator[size];
        }
    };
    private static final String TAG = "LuhnChecksumValidator";
    private final AutofillId[] mIds;

    public LuhnChecksumValidator(AutofillId... ids) {
        this.mIds = (AutofillId[]) Preconditions.checkArrayElementsNotNull(ids, Downloads.EXTRA_IDS);
    }

    private static boolean isLuhnChecksumValid(String number) {
        int sum = 0;
        boolean isDoubled = false;
        int i = number.length() - 1;
        while (true) {
            boolean z = false;
            if (i < 0) {
                break;
            }
            int digit = number.charAt(i) - 48;
            if (digit >= 0 && digit <= 9) {
                int addend;
                if (isDoubled) {
                    addend = digit * 2;
                    if (addend > 9) {
                        addend -= 9;
                    }
                } else {
                    addend = digit;
                }
                sum += addend;
                if (!isDoubled) {
                    z = true;
                }
                isDoubled = z;
            }
            i--;
        }
        if (sum % 10 == 0) {
            return true;
        }
        return false;
    }

    public boolean isValid(ValueFinder finder) {
        AutofillId[] autofillIdArr = this.mIds;
        if (autofillIdArr == null || autofillIdArr.length == 0) {
            return false;
        }
        StringBuilder builder = new StringBuilder();
        AutofillId[] autofillIdArr2 = this.mIds;
        int length = autofillIdArr2.length;
        int i = 0;
        while (true) {
            String str = TAG;
            if (i < length) {
                AutofillId id = autofillIdArr2[i];
                String partialNumber = finder.findByAutofillId(id);
                if (partialNumber == null) {
                    if (Helper.sDebug) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("No partial number for id ");
                        stringBuilder.append(id);
                        Log.d(str, stringBuilder.toString());
                    }
                    return false;
                }
                builder.append(partialNumber);
                i++;
            } else {
                String number = builder.toString();
                boolean valid = isLuhnChecksumValid(number);
                if (Helper.sDebug) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("isValid(");
                    stringBuilder2.append(number.length());
                    stringBuilder2.append(" chars): ");
                    stringBuilder2.append(valid);
                    Log.d(str, stringBuilder2.toString());
                }
                return valid;
            }
        }
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LuhnChecksumValidator: [ids=");
        stringBuilder.append(Arrays.toString(this.mIds));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelableArray(this.mIds, flags);
    }
}
