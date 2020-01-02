package android.hardware.display;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.MathUtils;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

@SystemApi
public final class BrightnessCorrection implements Parcelable {
    public static final Creator<BrightnessCorrection> CREATOR = new Creator<BrightnessCorrection>() {
        public BrightnessCorrection createFromParcel(Parcel in) {
            if (in.readInt() != 1) {
                return null;
            }
            return ScaleAndTranslateLog.readFromParcel(in);
        }

        public BrightnessCorrection[] newArray(int size) {
            return new BrightnessCorrection[size];
        }
    };
    private static final int SCALE_AND_TRANSLATE_LOG = 1;
    private static final String TAG_SCALE_AND_TRANSLATE_LOG = "scale-and-translate-log";
    private BrightnessCorrectionImplementation mImplementation;

    private interface BrightnessCorrectionImplementation {
        float apply(float f);

        void saveToXml(XmlSerializer xmlSerializer) throws IOException;

        String toString();

        void writeToParcel(Parcel parcel);
    }

    private static class ScaleAndTranslateLog implements BrightnessCorrectionImplementation {
        private static final String ATTR_SCALE = "scale";
        private static final String ATTR_TRANSLATE = "translate";
        private static final float MAX_SCALE = 2.0f;
        private static final float MAX_TRANSLATE = 0.7f;
        private static final float MIN_SCALE = 0.5f;
        private static final float MIN_TRANSLATE = -0.6f;
        private final float mScale;
        private final float mTranslate;

        ScaleAndTranslateLog(float scale, float translate) {
            if (Float.isNaN(scale) || Float.isNaN(translate)) {
                throw new IllegalArgumentException("scale and translate must be numbers");
            }
            this.mScale = MathUtils.constrain(scale, 0.5f, (float) MAX_SCALE);
            this.mTranslate = MathUtils.constrain(translate, (float) MIN_TRANSLATE, (float) MAX_TRANSLATE);
        }

        public float apply(float brightness) {
            return MathUtils.exp((this.mScale * MathUtils.log(brightness)) + this.mTranslate);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ScaleAndTranslateLog(");
            stringBuilder.append(this.mScale);
            stringBuilder.append(", ");
            stringBuilder.append(this.mTranslate);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (o == this) {
                return true;
            }
            if (!(o instanceof ScaleAndTranslateLog)) {
                return false;
            }
            ScaleAndTranslateLog other = (ScaleAndTranslateLog) o;
            if (!(other.mScale == this.mScale && other.mTranslate == this.mTranslate)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return (((1 * 31) + Float.hashCode(this.mScale)) * 31) + Float.hashCode(this.mTranslate);
        }

        public void writeToParcel(Parcel dest) {
            dest.writeInt(1);
            dest.writeFloat(this.mScale);
            dest.writeFloat(this.mTranslate);
        }

        public void saveToXml(XmlSerializer serializer) throws IOException {
            String str = BrightnessCorrection.TAG_SCALE_AND_TRANSLATE_LOG;
            serializer.startTag(null, str);
            serializer.attribute(null, "scale", Float.toString(this.mScale));
            serializer.attribute(null, ATTR_TRANSLATE, Float.toString(this.mTranslate));
            serializer.endTag(null, str);
        }

        static BrightnessCorrection readFromParcel(Parcel in) {
            return BrightnessCorrection.createScaleAndTranslateLog(in.readFloat(), in.readFloat());
        }

        static BrightnessCorrection loadFromXml(XmlPullParser parser) throws IOException, XmlPullParserException {
            return BrightnessCorrection.createScaleAndTranslateLog(BrightnessCorrection.loadFloatFromXml(parser, "scale"), BrightnessCorrection.loadFloatFromXml(parser, ATTR_TRANSLATE));
        }
    }

    private BrightnessCorrection(BrightnessCorrectionImplementation implementation) {
        this.mImplementation = implementation;
    }

    public static BrightnessCorrection createScaleAndTranslateLog(float scale, float translate) {
        return new BrightnessCorrection(new ScaleAndTranslateLog(scale, translate));
    }

    public float apply(float brightness) {
        return this.mImplementation.apply(brightness);
    }

    public String toString() {
        return this.mImplementation.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BrightnessCorrection) {
            return ((BrightnessCorrection) o).mImplementation.equals(this.mImplementation);
        }
        return false;
    }

    public int hashCode() {
        return this.mImplementation.hashCode();
    }

    public void writeToParcel(Parcel dest, int flags) {
        this.mImplementation.writeToParcel(dest);
    }

    public int describeContents() {
        return 0;
    }

    public void saveToXml(XmlSerializer serializer) throws IOException {
        this.mImplementation.saveToXml(serializer);
    }

    public static BrightnessCorrection loadFromXml(XmlPullParser parser) throws IOException, XmlPullParserException {
        int depth = parser.getDepth();
        while (XmlUtils.nextElementWithin(parser, depth)) {
            if (TAG_SCALE_AND_TRANSLATE_LOG.equals(parser.getName())) {
                return ScaleAndTranslateLog.loadFromXml(parser);
            }
        }
        return null;
    }

    private static float loadFloatFromXml(XmlPullParser parser, String attribute) {
        try {
            return Float.parseFloat(parser.getAttributeValue(null, attribute));
        } catch (NullPointerException | NumberFormatException e) {
            return Float.NaN;
        }
    }
}
