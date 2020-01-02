package android.view.textclassifier;

import android.icu.util.ULocale;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.Preconditions;
import java.util.Locale;
import java.util.Map;

public final class TextLanguage implements Parcelable {
    public static final Creator<TextLanguage> CREATOR = new Creator<TextLanguage>() {
        public TextLanguage createFromParcel(Parcel in) {
            return TextLanguage.readFromParcel(in);
        }

        public TextLanguage[] newArray(int size) {
            return new TextLanguage[size];
        }
    };
    static final TextLanguage EMPTY = new Builder().build();
    private final Bundle mBundle;
    private final EntityConfidence mEntityConfidence;
    private final String mId;

    public static final class Builder {
        private Bundle mBundle;
        private final Map<String, Float> mEntityConfidenceMap = new ArrayMap();
        private String mId;

        public Builder putLocale(ULocale locale, float confidenceScore) {
            Preconditions.checkNotNull(locale);
            this.mEntityConfidenceMap.put(locale.toLanguageTag(), Float.valueOf(confidenceScore));
            return this;
        }

        public Builder setId(String id) {
            this.mId = id;
            return this;
        }

        public Builder setExtras(Bundle bundle) {
            this.mBundle = (Bundle) Preconditions.checkNotNull(bundle);
            return this;
        }

        public TextLanguage build() {
            Bundle bundle = this.mBundle;
            if (bundle == null) {
                bundle = Bundle.EMPTY;
            }
            this.mBundle = bundle;
            return new TextLanguage(this.mId, new EntityConfidence(this.mEntityConfidenceMap), this.mBundle, null);
        }
    }

    public static final class Request implements Parcelable {
        public static final Creator<Request> CREATOR = new Creator<Request>() {
            public Request createFromParcel(Parcel in) {
                return Request.readFromParcel(in);
            }

            public Request[] newArray(int size) {
                return new Request[size];
            }
        };
        private String mCallingPackageName;
        private final Bundle mExtra;
        private final CharSequence mText;

        public static final class Builder {
            private Bundle mBundle;
            private final CharSequence mText;

            public Builder(CharSequence text) {
                this.mText = (CharSequence) Preconditions.checkNotNull(text);
            }

            public Builder setExtras(Bundle bundle) {
                this.mBundle = (Bundle) Preconditions.checkNotNull(bundle);
                return this;
            }

            public Request build() {
                String charSequence = this.mText.toString();
                Bundle bundle = this.mBundle;
                if (bundle == null) {
                    bundle = Bundle.EMPTY;
                }
                return new Request(charSequence, bundle, null);
            }
        }

        /* synthetic */ Request(CharSequence x0, Bundle x1, AnonymousClass1 x2) {
            this(x0, x1);
        }

        private Request(CharSequence text, Bundle bundle) {
            this.mText = text;
            this.mExtra = bundle;
        }

        public CharSequence getText() {
            return this.mText;
        }

        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public void setCallingPackageName(String callingPackageName) {
            this.mCallingPackageName = callingPackageName;
        }

        public String getCallingPackageName() {
            return this.mCallingPackageName;
        }

        public Bundle getExtras() {
            return this.mExtra;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeCharSequence(this.mText);
            dest.writeString(this.mCallingPackageName);
            dest.writeBundle(this.mExtra);
        }

        private static Request readFromParcel(Parcel in) {
            CharSequence text = in.readCharSequence();
            String callingPackageName = in.readString();
            Request request = new Request(text, in.readBundle());
            request.setCallingPackageName(callingPackageName);
            return request;
        }
    }

    /* synthetic */ TextLanguage(String x0, EntityConfidence x1, Bundle x2, AnonymousClass1 x3) {
        this(x0, x1, x2);
    }

    private TextLanguage(String id, EntityConfidence entityConfidence, Bundle bundle) {
        this.mId = id;
        this.mEntityConfidence = entityConfidence;
        this.mBundle = bundle;
    }

    public String getId() {
        return this.mId;
    }

    public int getLocaleHypothesisCount() {
        return this.mEntityConfidence.getEntities().size();
    }

    public ULocale getLocale(int index) {
        return ULocale.forLanguageTag((String) this.mEntityConfidence.getEntities().get(index));
    }

    public float getConfidenceScore(ULocale locale) {
        return this.mEntityConfidence.getConfidenceScore(locale.toLanguageTag());
    }

    public Bundle getExtras() {
        return this.mBundle;
    }

    public String toString() {
        return String.format(Locale.US, "TextLanguage {id=%s, locales=%s, bundle=%s}", new Object[]{this.mId, this.mEntityConfidence, this.mBundle});
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        this.mEntityConfidence.writeToParcel(dest, flags);
        dest.writeBundle(this.mBundle);
    }

    private static TextLanguage readFromParcel(Parcel in) {
        return new TextLanguage(in.readString(), (EntityConfidence) EntityConfidence.CREATOR.createFromParcel(in), in.readBundle());
    }
}
