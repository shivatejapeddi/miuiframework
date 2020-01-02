package android.app.admin;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.DateFormat;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PasswordMetrics implements Parcelable {
    private static final int CHAR_DIGIT = 2;
    private static final int CHAR_LOWER_CASE = 0;
    private static final int CHAR_SYMBOL = 3;
    private static final int CHAR_UPPER_CASE = 1;
    public static final Creator<PasswordMetrics> CREATOR = new Creator<PasswordMetrics>() {
        public PasswordMetrics createFromParcel(Parcel in) {
            return new PasswordMetrics(in, null);
        }

        public PasswordMetrics[] newArray(int size) {
            return new PasswordMetrics[size];
        }
    };
    public static final int MAX_ALLOWED_SEQUENCE = 3;
    public int length;
    public int letters;
    public int lowerCase;
    public int nonLetter;
    public int numeric;
    public int quality;
    public int symbols;
    public int upperCase;

    @Retention(RetentionPolicy.SOURCE)
    private @interface CharacterCatagory {
    }

    private static class PasswordComplexityBucket {
        private static final PasswordComplexityBucket[] BUCKETS = new PasswordComplexityBucket[]{HIGH, MEDIUM, LOW};
        private static final PasswordComplexityBucket HIGH = new PasswordComplexityBucket(327680, new PasswordMetrics(196608, 8), new PasswordMetrics(262144, 6), new PasswordMetrics(327680, 6));
        private static final PasswordComplexityBucket LOW = new PasswordComplexityBucket(65536, new PasswordMetrics(65536), new PasswordMetrics(131072), new PasswordMetrics(196608), new PasswordMetrics(262144), new PasswordMetrics(327680));
        private static final PasswordComplexityBucket MEDIUM = new PasswordComplexityBucket(196608, new PasswordMetrics(196608, 4), new PasswordMetrics(262144, 4), new PasswordMetrics(327680, 4));
        private static final PasswordComplexityBucket NONE = new PasswordComplexityBucket(0, new PasswordMetrics());
        private final int mComplexityLevel;
        private final PasswordMetrics[] mMetrics;

        private PasswordComplexityBucket(int complexityLevel, PasswordMetrics... metricsArray) {
            int previousQuality = 0;
            int length = metricsArray.length;
            int i = 0;
            while (i < length) {
                PasswordMetrics metrics = metricsArray[i];
                if (metrics.quality >= previousQuality) {
                    previousQuality = metrics.quality;
                    i++;
                } else {
                    throw new IllegalArgumentException("metricsArray must be sorted in ascending order of quality");
                }
            }
            this.mMetrics = metricsArray;
            this.mComplexityLevel = complexityLevel;
        }

        private static PasswordComplexityBucket complexityLevelToBucket(int complexityLevel) {
            for (PasswordComplexityBucket bucket : BUCKETS) {
                if (bucket.mComplexityLevel == complexityLevel) {
                    return bucket;
                }
            }
            return NONE;
        }
    }

    public PasswordMetrics() {
        this.quality = 0;
        this.length = 0;
        this.letters = 0;
        this.upperCase = 0;
        this.lowerCase = 0;
        this.numeric = 0;
        this.symbols = 0;
        this.nonLetter = 0;
    }

    public PasswordMetrics(int quality) {
        this.quality = 0;
        this.length = 0;
        this.letters = 0;
        this.upperCase = 0;
        this.lowerCase = 0;
        this.numeric = 0;
        this.symbols = 0;
        this.nonLetter = 0;
        this.quality = quality;
    }

    public PasswordMetrics(int quality, int length) {
        this.quality = 0;
        this.length = 0;
        this.letters = 0;
        this.upperCase = 0;
        this.lowerCase = 0;
        this.numeric = 0;
        this.symbols = 0;
        this.nonLetter = 0;
        this.quality = quality;
        this.length = length;
    }

    public PasswordMetrics(int quality, int length, int letters, int upperCase, int lowerCase, int numeric, int symbols, int nonLetter) {
        this(quality, length);
        this.letters = letters;
        this.upperCase = upperCase;
        this.lowerCase = lowerCase;
        this.numeric = numeric;
        this.symbols = symbols;
        this.nonLetter = nonLetter;
    }

    private PasswordMetrics(Parcel in) {
        this.quality = 0;
        this.length = 0;
        this.letters = 0;
        this.upperCase = 0;
        this.lowerCase = 0;
        this.numeric = 0;
        this.symbols = 0;
        this.nonLetter = 0;
        this.quality = in.readInt();
        this.length = in.readInt();
        this.letters = in.readInt();
        this.upperCase = in.readInt();
        this.lowerCase = in.readInt();
        this.numeric = in.readInt();
        this.symbols = in.readInt();
        this.nonLetter = in.readInt();
    }

    public static int complexityLevelToMinQuality(int complexityLevel) {
        return PasswordComplexityBucket.complexityLevelToBucket(complexityLevel).mMetrics[0].quality;
    }

    public static PasswordMetrics getMinimumMetrics(int complexityLevel, int userEnteredPasswordQuality, int requestedQuality, boolean requiresNumeric, boolean requiresLettersOrSymbols) {
        return getTargetQualityMetrics(complexityLevel, Math.max(userEnteredPasswordQuality, getActualRequiredQuality(requestedQuality, requiresNumeric, requiresLettersOrSymbols)));
    }

    @VisibleForTesting
    public static PasswordMetrics getTargetQualityMetrics(int complexityLevel, int targetQuality) {
        PasswordComplexityBucket targetBucket = PasswordComplexityBucket.complexityLevelToBucket(complexityLevel);
        for (PasswordMetrics metrics : targetBucket.mMetrics) {
            if (targetQuality == metrics.quality) {
                return metrics;
            }
        }
        return targetBucket.mMetrics[0];
    }

    @VisibleForTesting
    public static int getActualRequiredQuality(int requestedQuality, boolean requiresNumeric, boolean requiresLettersOrSymbols) {
        if (requestedQuality != 393216) {
            return requestedQuality;
        }
        if (requiresNumeric && requiresLettersOrSymbols) {
            return 327680;
        }
        if (requiresLettersOrSymbols) {
            return 262144;
        }
        if (requiresNumeric) {
            return 131072;
        }
        return 0;
    }

    public static int sanitizeComplexityLevel(int complexityLevel) {
        return PasswordComplexityBucket.complexityLevelToBucket(complexityLevel).mComplexityLevel;
    }

    public boolean isDefault() {
        return this.quality == 0 && this.length == 0 && this.letters == 0 && this.upperCase == 0 && this.lowerCase == 0 && this.numeric == 0 && this.symbols == 0 && this.nonLetter == 0;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.quality);
        dest.writeInt(this.length);
        dest.writeInt(this.letters);
        dest.writeInt(this.upperCase);
        dest.writeInt(this.lowerCase);
        dest.writeInt(this.numeric);
        dest.writeInt(this.symbols);
        dest.writeInt(this.nonLetter);
    }

    public static PasswordMetrics computeForCredential(int type, byte[] credential) {
        if (type == 2) {
            Preconditions.checkNotNull(credential, "credential cannot be null");
            return computeForPassword(credential);
        } else if (type == 1) {
            return new PasswordMetrics(65536);
        } else {
            return new PasswordMetrics(0);
        }
    }

    public static PasswordMetrics computeForPassword(byte[] password) {
        int i;
        int quality;
        byte[] bArr = password;
        int lowerCase = 0;
        int numeric = 0;
        int nonLetter = 0;
        int length = bArr.length;
        boolean z = false;
        int symbols = 0;
        int upperCase = 0;
        int letters = 0;
        for (byte b : bArr) {
            int categoryChar = categoryChar((char) b);
            if (categoryChar == 0) {
                letters++;
                lowerCase++;
            } else if (categoryChar == 1) {
                letters++;
                upperCase++;
            } else if (categoryChar == 2) {
                numeric++;
                nonLetter++;
            } else if (categoryChar == 3) {
                symbols++;
                nonLetter++;
            }
        }
        boolean hasNumeric = numeric > 0;
        if (letters + symbols > 0) {
            z = true;
        }
        boolean hasNonNumeric = z;
        if (hasNonNumeric && hasNumeric) {
            quality = 327680;
        } else if (hasNonNumeric) {
            quality = 262144;
        } else if (hasNumeric) {
            if (maxLengthSequence(password) > 3) {
                i = 131072;
            } else {
                i = 196608;
            }
            quality = i;
        } else {
            quality = 0;
        }
        return new PasswordMetrics(quality, length, letters, upperCase, lowerCase, numeric, symbols, nonLetter);
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!(other instanceof PasswordMetrics)) {
            return false;
        }
        PasswordMetrics o = (PasswordMetrics) other;
        if (this.quality == o.quality && this.length == o.length && this.letters == o.letters && this.upperCase == o.upperCase && this.lowerCase == o.lowerCase && this.numeric == o.numeric && this.symbols == o.symbols && this.nonLetter == o.nonLetter) {
            z = true;
        }
        return z;
    }

    private boolean satisfiesBucket(PasswordMetrics... bucket) {
        boolean z = false;
        for (PasswordMetrics metrics : bucket) {
            if (this.quality == metrics.quality) {
                if (this.length >= metrics.length) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    public static int maxLengthSequence(byte[] bytes) {
        if (bytes.length == 0) {
            return 0;
        }
        char previousChar = (char) bytes[0];
        int category = categoryChar(previousChar);
        int diff = 0;
        boolean hasDiff = false;
        int maxLength = 0;
        int startSequence = 0;
        for (int current = 1; current < bytes.length; current++) {
            char currentChar = (char) bytes[current];
            int categoryCurrent = categoryChar(currentChar);
            int currentDiff = currentChar - previousChar;
            if (categoryCurrent != category || Math.abs(currentDiff) > maxDiffCategory(category)) {
                maxLength = Math.max(maxLength, current - startSequence);
                startSequence = current;
                hasDiff = false;
                category = categoryCurrent;
            } else {
                if (hasDiff && currentDiff != diff) {
                    maxLength = Math.max(maxLength, current - startSequence);
                    startSequence = current - 1;
                }
                diff = currentDiff;
                hasDiff = true;
            }
            previousChar = currentChar;
        }
        return Math.max(maxLength, bytes.length - startSequence);
    }

    private static int categoryChar(char c) {
        if (DateFormat.AM_PM <= c && c <= DateFormat.TIME_ZONE) {
            return 0;
        }
        if (DateFormat.CAPITAL_AM_PM <= c && c <= 'Z') {
            return 1;
        }
        if ('0' > c || c > '9') {
            return 3;
        }
        return 2;
    }

    private static int maxDiffCategory(int category) {
        if (category == 0 || category == 1) {
            return 1;
        }
        if (category != 2) {
            return 0;
        }
        return 10;
    }

    public int determineComplexity() {
        for (PasswordComplexityBucket bucket : PasswordComplexityBucket.BUCKETS) {
            if (satisfiesBucket(bucket.mMetrics)) {
                return bucket.mComplexityLevel;
            }
        }
        return 0;
    }
}
