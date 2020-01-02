package android.telephony;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.DateFormat;
import android.util.Log;
import java.util.Objects;
import java.util.regex.Pattern;

@SystemApi
public final class PhoneNumberRange implements Parcelable {
    public static final Creator<PhoneNumberRange> CREATOR = new Creator<PhoneNumberRange>() {
        public PhoneNumberRange createFromParcel(Parcel in) {
            return new PhoneNumberRange(in, null);
        }

        public PhoneNumberRange[] newArray(int size) {
            return new PhoneNumberRange[size];
        }
    };
    private final String mCountryCode;
    private final String mLowerBound;
    private final String mPrefix;
    private final String mUpperBound;

    /* synthetic */ PhoneNumberRange(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public PhoneNumberRange(String countryCode, String prefix, String lowerBound, String upperBound) {
        validateLowerAndUpperBounds(lowerBound, upperBound);
        String str = "[0-9]*";
        if (!Pattern.matches(str, countryCode)) {
            throw new IllegalArgumentException("Country code must be all numeric");
        } else if (Pattern.matches(str, prefix)) {
            this.mCountryCode = countryCode;
            this.mPrefix = prefix;
            this.mLowerBound = lowerBound;
            this.mUpperBound = upperBound;
        } else {
            throw new IllegalArgumentException("Prefix must be all numeric");
        }
    }

    private PhoneNumberRange(Parcel in) {
        this.mCountryCode = in.readStringNoHelper();
        this.mPrefix = in.readStringNoHelper();
        this.mLowerBound = in.readStringNoHelper();
        this.mUpperBound = in.readStringNoHelper();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringNoHelper(this.mCountryCode);
        dest.writeStringNoHelper(this.mPrefix);
        dest.writeStringNoHelper(this.mLowerBound);
        dest.writeStringNoHelper(this.mUpperBound);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PhoneNumberRange that = (PhoneNumberRange) o;
        if (!(Objects.equals(this.mCountryCode, that.mCountryCode) && Objects.equals(this.mPrefix, that.mPrefix) && Objects.equals(this.mLowerBound, that.mLowerBound) && Objects.equals(this.mUpperBound, that.mUpperBound))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mCountryCode, this.mPrefix, this.mLowerBound, this.mUpperBound});
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PhoneNumberRange{mCountryCode='");
        stringBuilder.append(this.mCountryCode);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mPrefix='");
        stringBuilder.append(this.mPrefix);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mLowerBound='");
        stringBuilder.append(this.mLowerBound);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mUpperBound='");
        stringBuilder.append(this.mUpperBound);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    private void validateLowerAndUpperBounds(String lowerBound, String upperBound) {
        if (lowerBound.length() == upperBound.length()) {
            String str = "[0-9]*";
            if (!Pattern.matches(str, lowerBound)) {
                throw new IllegalArgumentException("Lower bound must be all numeric");
            } else if (!Pattern.matches(str, upperBound)) {
                throw new IllegalArgumentException("Upper bound must be all numeric");
            } else if (Integer.parseInt(lowerBound) > Integer.parseInt(upperBound)) {
                throw new IllegalArgumentException("Lower bound must be lower than upper bound");
            } else {
                return;
            }
        }
        throw new IllegalArgumentException("Lower and upper bounds must have the same length");
    }

    public boolean matches(String number) {
        String numberPostfix;
        String normalizedNumber = number.replaceAll("[^0-9]", "");
        String prefixWithCountryCode = new StringBuilder();
        prefixWithCountryCode.append(this.mCountryCode);
        prefixWithCountryCode.append(this.mPrefix);
        prefixWithCountryCode = prefixWithCountryCode.toString();
        boolean z = false;
        if (normalizedNumber.startsWith(prefixWithCountryCode)) {
            numberPostfix = normalizedNumber.substring(prefixWithCountryCode.length());
        } else if (!normalizedNumber.startsWith(this.mPrefix)) {
            return false;
        } else {
            numberPostfix = normalizedNumber.substring(this.mPrefix.length());
        }
        try {
            int lower = Integer.parseInt(this.mLowerBound);
            int upper = Integer.parseInt(this.mUpperBound);
            int numberToCheck = Integer.parseInt(numberPostfix);
            if (numberToCheck <= upper && numberToCheck >= lower) {
                z = true;
            }
            return z;
        } catch (NumberFormatException e) {
            Log.e(PhoneNumberRange.class.getSimpleName(), "Invalid bounds or number.", e);
            return false;
        }
    }
}
