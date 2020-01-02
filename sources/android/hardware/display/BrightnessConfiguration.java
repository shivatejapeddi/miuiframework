package android.hardware.display;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Pair;
import com.android.internal.util.Preconditions;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

@SystemApi
public final class BrightnessConfiguration implements Parcelable {
    private static final String ATTR_CATEGORY = "category";
    private static final String ATTR_DESCRIPTION = "description";
    private static final String ATTR_LUX = "lux";
    private static final String ATTR_NITS = "nits";
    private static final String ATTR_PACKAGE_NAME = "package-name";
    public static final Creator<BrightnessConfiguration> CREATOR = new Creator<BrightnessConfiguration>() {
        public BrightnessConfiguration createFromParcel(Parcel in) {
            int i;
            Builder builder = new Builder(in.createFloatArray(), in.createFloatArray());
            int n = in.readInt();
            for (i = 0; i < n; i++) {
                builder.addCorrectionByPackageName(in.readString(), (BrightnessCorrection) BrightnessCorrection.CREATOR.createFromParcel(in));
            }
            n = in.readInt();
            for (i = 0; i < n; i++) {
                builder.addCorrectionByCategory(in.readInt(), (BrightnessCorrection) BrightnessCorrection.CREATOR.createFromParcel(in));
            }
            builder.setDescription(in.readString());
            return builder.build();
        }

        public BrightnessConfiguration[] newArray(int size) {
            return new BrightnessConfiguration[size];
        }
    };
    private static final String TAG_BRIGHTNESS_CORRECTION = "brightness-correction";
    private static final String TAG_BRIGHTNESS_CORRECTIONS = "brightness-corrections";
    private static final String TAG_BRIGHTNESS_CURVE = "brightness-curve";
    private static final String TAG_BRIGHTNESS_POINT = "brightness-point";
    private final Map<Integer, BrightnessCorrection> mCorrectionsByCategory;
    private final Map<String, BrightnessCorrection> mCorrectionsByPackageName;
    private final String mDescription;
    private final float[] mLux;
    private final float[] mNits;

    public static class Builder {
        private static final int MAX_CORRECTIONS_BY_CATEGORY = 20;
        private static final int MAX_CORRECTIONS_BY_PACKAGE_NAME = 20;
        private Map<Integer, BrightnessCorrection> mCorrectionsByCategory;
        private Map<String, BrightnessCorrection> mCorrectionsByPackageName;
        private float[] mCurveLux;
        private float[] mCurveNits;
        private String mDescription;

        public Builder(float[] lux, float[] nits) {
            Preconditions.checkNotNull(lux);
            Preconditions.checkNotNull(nits);
            if (lux.length == 0 || nits.length == 0) {
                throw new IllegalArgumentException("Lux and nits arrays must not be empty");
            } else if (lux.length != nits.length) {
                throw new IllegalArgumentException("Lux and nits arrays must be the same length");
            } else if (lux[0] == 0.0f) {
                String str = BrightnessConfiguration.ATTR_LUX;
                Preconditions.checkArrayElementsInRange(lux, 0.0f, Float.MAX_VALUE, str);
                String str2 = BrightnessConfiguration.ATTR_NITS;
                Preconditions.checkArrayElementsInRange(nits, 0.0f, Float.MAX_VALUE, str2);
                checkMonotonic(lux, true, str);
                checkMonotonic(nits, false, str2);
                this.mCurveLux = lux;
                this.mCurveNits = nits;
                this.mCorrectionsByPackageName = new HashMap();
                this.mCorrectionsByCategory = new HashMap();
            } else {
                throw new IllegalArgumentException("Initial control point must be for 0 lux");
            }
        }

        public int getMaxCorrectionsByPackageName() {
            return 20;
        }

        public int getMaxCorrectionsByCategory() {
            return 20;
        }

        public Builder addCorrectionByPackageName(String packageName, BrightnessCorrection correction) {
            Objects.requireNonNull(packageName, "packageName must not be null");
            Objects.requireNonNull(correction, "correction must not be null");
            if (this.mCorrectionsByPackageName.size() < getMaxCorrectionsByPackageName()) {
                this.mCorrectionsByPackageName.put(packageName, correction);
                return this;
            }
            throw new IllegalArgumentException("Too many corrections by package name");
        }

        public Builder addCorrectionByCategory(int category, BrightnessCorrection correction) {
            Objects.requireNonNull(correction, "correction must not be null");
            if (this.mCorrectionsByCategory.size() < getMaxCorrectionsByCategory()) {
                this.mCorrectionsByCategory.put(Integer.valueOf(category), correction);
                return this;
            }
            throw new IllegalArgumentException("Too many corrections by category");
        }

        public Builder setDescription(String description) {
            this.mDescription = description;
            return this;
        }

        public BrightnessConfiguration build() {
            float[] fArr = this.mCurveLux;
            if (fArr != null) {
                float[] fArr2 = this.mCurveNits;
                if (fArr2 != null) {
                    return new BrightnessConfiguration(fArr, fArr2, this.mCorrectionsByPackageName, this.mCorrectionsByCategory, this.mDescription, null);
                }
            }
            throw new IllegalStateException("A curve must be set!");
        }

        private static void checkMonotonic(float[] vals, boolean strictlyIncreasing, String name) {
            if (vals.length > 1) {
                float prev = vals[0];
                int i = 1;
                while (i < vals.length) {
                    if (prev > vals[i] || (prev == vals[i] && strictlyIncreasing)) {
                        String condition = strictlyIncreasing ? "strictly increasing" : "monotonic";
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(name);
                        stringBuilder.append(" values must be ");
                        stringBuilder.append(condition);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                    prev = vals[i];
                    i++;
                }
            }
        }
    }

    /* synthetic */ BrightnessConfiguration(float[] x0, float[] x1, Map x2, Map x3, String x4, AnonymousClass1 x5) {
        this(x0, x1, x2, x3, x4);
    }

    private BrightnessConfiguration(float[] lux, float[] nits, Map<String, BrightnessCorrection> correctionsByPackageName, Map<Integer, BrightnessCorrection> correctionsByCategory, String description) {
        this.mLux = lux;
        this.mNits = nits;
        this.mCorrectionsByPackageName = correctionsByPackageName;
        this.mCorrectionsByCategory = correctionsByCategory;
        this.mDescription = description;
    }

    public Pair<float[], float[]> getCurve() {
        float[] fArr = this.mLux;
        fArr = Arrays.copyOf(fArr, fArr.length);
        float[] fArr2 = this.mNits;
        return Pair.create(fArr, Arrays.copyOf(fArr2, fArr2.length));
    }

    public BrightnessCorrection getCorrectionByPackageName(String packageName) {
        return (BrightnessCorrection) this.mCorrectionsByPackageName.get(packageName);
    }

    public BrightnessCorrection getCorrectionByCategory(int category) {
        return (BrightnessCorrection) this.mCorrectionsByCategory.get(Integer.valueOf(category));
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void writeToParcel(Parcel dest, int flags) {
        BrightnessCorrection correction;
        dest.writeFloatArray(this.mLux);
        dest.writeFloatArray(this.mNits);
        dest.writeInt(this.mCorrectionsByPackageName.size());
        for (Entry<String, BrightnessCorrection> entry : this.mCorrectionsByPackageName.entrySet()) {
            correction = (BrightnessCorrection) entry.getValue();
            dest.writeString((String) entry.getKey());
            correction.writeToParcel(dest, flags);
        }
        dest.writeInt(this.mCorrectionsByCategory.size());
        for (Entry<Integer, BrightnessCorrection> entry2 : this.mCorrectionsByCategory.entrySet()) {
            correction = (BrightnessCorrection) entry2.getValue();
            dest.writeInt(((Integer) entry2.getKey()).intValue());
            correction.writeToParcel(dest, flags);
        }
        dest.writeString(this.mDescription);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        String str;
        StringBuilder stringBuilder;
        StringBuilder sb = new StringBuilder("BrightnessConfiguration{[");
        int size = this.mLux.length;
        int i = 0;
        while (true) {
            str = ", ";
            if (i >= size) {
                break;
            }
            if (i != 0) {
                sb.append(str);
            }
            sb.append("(");
            sb.append(this.mLux[i]);
            sb.append(str);
            sb.append(this.mNits[i]);
            sb.append(")");
            i++;
        }
        sb.append("], {");
        for (Entry<String, BrightnessCorrection> entry : this.mCorrectionsByPackageName.entrySet()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("'");
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append("': ");
            stringBuilder.append(entry.getValue());
            stringBuilder.append(str);
            sb.append(stringBuilder.toString());
        }
        for (Entry<Integer, BrightnessCorrection> entry2 : this.mCorrectionsByCategory.entrySet()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(entry2.getKey());
            stringBuilder.append(": ");
            stringBuilder.append(entry2.getValue());
            stringBuilder.append(str);
            sb.append(stringBuilder.toString());
        }
        sb.append("}, '");
        String str2 = this.mDescription;
        if (str2 != null) {
            sb.append(str2);
        }
        sb.append("'}");
        return sb.toString();
    }

    public int hashCode() {
        int result = (((((((1 * 31) + Arrays.hashCode(this.mLux)) * 31) + Arrays.hashCode(this.mNits)) * 31) + this.mCorrectionsByPackageName.hashCode()) * 31) + this.mCorrectionsByCategory.hashCode();
        String str = this.mDescription;
        if (str != null) {
            return (result * 31) + str.hashCode();
        }
        return result;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof BrightnessConfiguration)) {
            return false;
        }
        BrightnessConfiguration other = (BrightnessConfiguration) o;
        if (!(Arrays.equals(this.mLux, other.mLux) && Arrays.equals(this.mNits, other.mNits) && this.mCorrectionsByPackageName.equals(other.mCorrectionsByPackageName) && this.mCorrectionsByCategory.equals(other.mCorrectionsByCategory) && Objects.equals(this.mDescription, other.mDescription))) {
            z = false;
        }
        return z;
    }

    public void saveToXml(XmlSerializer serializer) throws IOException {
        String str;
        BrightnessCorrection correction;
        String str2 = TAG_BRIGHTNESS_CURVE;
        serializer.startTag(null, str2);
        String str3 = this.mDescription;
        if (str3 != null) {
            serializer.attribute(null, "description", str3);
        }
        for (int i = 0; i < this.mLux.length; i++) {
            String str4 = TAG_BRIGHTNESS_POINT;
            serializer.startTag(null, str4);
            serializer.attribute(null, ATTR_LUX, Float.toString(this.mLux[i]));
            serializer.attribute(null, ATTR_NITS, Float.toString(this.mNits[i]));
            serializer.endTag(null, str4);
        }
        serializer.endTag(null, str2);
        str2 = TAG_BRIGHTNESS_CORRECTIONS;
        serializer.startTag(null, str2);
        Iterator it = this.mCorrectionsByPackageName.entrySet().iterator();
        while (true) {
            boolean hasNext = it.hasNext();
            str = TAG_BRIGHTNESS_CORRECTION;
            if (!hasNext) {
                break;
            }
            Entry<String, BrightnessCorrection> entry = (Entry) it.next();
            String packageName = (String) entry.getKey();
            correction = (BrightnessCorrection) entry.getValue();
            serializer.startTag(null, str);
            serializer.attribute(null, ATTR_PACKAGE_NAME, packageName);
            correction.saveToXml(serializer);
            serializer.endTag(null, str);
        }
        for (Entry<Integer, BrightnessCorrection> entry2 : this.mCorrectionsByCategory.entrySet()) {
            int category = ((Integer) entry2.getKey()).intValue();
            correction = (BrightnessCorrection) entry2.getValue();
            serializer.startTag(null, str);
            serializer.attribute(null, "category", Integer.toString(category));
            correction.saveToXml(serializer);
            serializer.endTag(null, str);
        }
        serializer.endTag(null, str2);
    }

    public static BrightnessConfiguration loadFromXml(XmlPullParser parser) throws IOException, XmlPullParserException {
        int curveDepth;
        String description = null;
        List<Float> luxList = new ArrayList();
        List<Float> nitsList = new ArrayList();
        Map<String, BrightnessCorrection> correctionsByPackageName = new HashMap();
        Map<Integer, BrightnessCorrection> correctionsByCategory = new HashMap();
        int configDepth = parser.getDepth();
        while (XmlUtils.nextElementWithin(parser, configDepth)) {
            if (TAG_BRIGHTNESS_CURVE.equals(parser.getName())) {
                description = parser.getAttributeValue(null, "description");
                curveDepth = parser.getDepth();
                while (XmlUtils.nextElementWithin(parser, curveDepth)) {
                    if (TAG_BRIGHTNESS_POINT.equals(parser.getName())) {
                        float lux = loadFloatFromXml(parser, ATTR_LUX);
                        float nits = loadFloatFromXml(parser, ATTR_NITS);
                        luxList.add(Float.valueOf(lux));
                        nitsList.add(Float.valueOf(nits));
                    }
                }
            }
            if (TAG_BRIGHTNESS_CORRECTIONS.equals(parser.getName())) {
                curveDepth = parser.getDepth();
                while (XmlUtils.nextElementWithin(parser, curveDepth)) {
                    if (TAG_BRIGHTNESS_CORRECTION.equals(parser.getName())) {
                        String packageName = parser.getAttributeValue(null, ATTR_PACKAGE_NAME);
                        String categoryText = parser.getAttributeValue(null, "category");
                        BrightnessCorrection correction = BrightnessCorrection.loadFromXml(parser);
                        if (packageName != null) {
                            correctionsByPackageName.put(packageName, correction);
                        } else if (categoryText != null) {
                            try {
                                correctionsByCategory.put(Integer.valueOf(Integer.parseInt(categoryText)), correction);
                            } catch (NullPointerException | NumberFormatException e) {
                            }
                        }
                    }
                }
            }
        }
        curveDepth = luxList.size();
        float[] lux2 = new float[curveDepth];
        float[] nits2 = new float[curveDepth];
        for (int i = 0; i < curveDepth; i++) {
            lux2[i] = ((Float) luxList.get(i)).floatValue();
            nits2[i] = ((Float) nitsList.get(i)).floatValue();
        }
        Builder builder = new Builder(lux2, nits2);
        builder.setDescription(description);
        for (Entry<String, BrightnessCorrection> entry : correctionsByPackageName.entrySet()) {
            builder.addCorrectionByPackageName((String) entry.getKey(), (BrightnessCorrection) entry.getValue());
        }
        for (Entry<Integer, BrightnessCorrection> entry2 : correctionsByCategory.entrySet()) {
            builder.addCorrectionByCategory(((Integer) entry2.getKey()).intValue(), (BrightnessCorrection) entry2.getValue());
        }
        return builder.build();
    }

    private static float loadFloatFromXml(XmlPullParser parser, String attribute) {
        try {
            return Float.parseFloat(parser.getAttributeValue(null, attribute));
        } catch (NullPointerException | NumberFormatException e) {
            return Float.NaN;
        }
    }
}
