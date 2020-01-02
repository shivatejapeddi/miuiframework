package android.view.textclassifier;

import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.textclassifier.TextClassifier.EntityConfig;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public final class TextLinks implements Parcelable {
    public static final int APPLY_STRATEGY_IGNORE = 0;
    public static final int APPLY_STRATEGY_REPLACE = 1;
    public static final Creator<TextLinks> CREATOR = new Creator<TextLinks>() {
        public TextLinks createFromParcel(Parcel in) {
            return new TextLinks(in, null);
        }

        public TextLinks[] newArray(int size) {
            return new TextLinks[size];
        }
    };
    public static final int STATUS_DIFFERENT_TEXT = 3;
    public static final int STATUS_LINKS_APPLIED = 0;
    public static final int STATUS_NO_LINKS_APPLIED = 2;
    public static final int STATUS_NO_LINKS_FOUND = 1;
    public static final int STATUS_UNSUPPORTED_CHARACTER = 4;
    private final Bundle mExtras;
    private final String mFullText;
    private final List<TextLink> mLinks;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ApplyStrategy {
    }

    public static final class Builder {
        private Bundle mExtras;
        private final String mFullText;
        private final ArrayList<TextLink> mLinks = new ArrayList();

        public Builder(String fullText) {
            this.mFullText = (String) Preconditions.checkNotNull(fullText);
        }

        public Builder addLink(int start, int end, Map<String, Float> entityScores) {
            return addLink(start, end, entityScores, Bundle.EMPTY, null);
        }

        public Builder addLink(int start, int end, Map<String, Float> entityScores, Bundle extras) {
            return addLink(start, end, entityScores, extras, null);
        }

        /* Access modifiers changed, original: 0000 */
        public Builder addLink(int start, int end, Map<String, Float> entityScores, URLSpan urlSpan) {
            return addLink(start, end, entityScores, Bundle.EMPTY, urlSpan);
        }

        private Builder addLink(int start, int end, Map<String, Float> entityScores, Bundle extras, URLSpan urlSpan) {
            this.mLinks.add(new TextLink(start, end, new EntityConfidence((Map) entityScores), extras, urlSpan, null));
            return this;
        }

        public Builder clearTextLinks() {
            this.mLinks.clear();
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public TextLinks build() {
            String str = this.mFullText;
            ArrayList arrayList = this.mLinks;
            Bundle bundle = this.mExtras;
            if (bundle == null) {
                bundle = Bundle.EMPTY;
            }
            return new TextLinks(str, arrayList, bundle, null);
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
        private final LocaleList mDefaultLocales;
        private final EntityConfig mEntityConfig;
        private final Bundle mExtras;
        private final boolean mLegacyFallback;
        private final CharSequence mText;

        public static final class Builder {
            private LocaleList mDefaultLocales;
            private EntityConfig mEntityConfig;
            private Bundle mExtras;
            private boolean mLegacyFallback = true;
            private final CharSequence mText;

            public Builder(CharSequence text) {
                this.mText = (CharSequence) Preconditions.checkNotNull(text);
            }

            public Builder setDefaultLocales(LocaleList defaultLocales) {
                this.mDefaultLocales = defaultLocales;
                return this;
            }

            public Builder setEntityConfig(EntityConfig entityConfig) {
                this.mEntityConfig = entityConfig;
                return this;
            }

            public Builder setLegacyFallback(boolean legacyFallback) {
                this.mLegacyFallback = legacyFallback;
                return this;
            }

            public Builder setExtras(Bundle extras) {
                this.mExtras = extras;
                return this;
            }

            public Request build() {
                CharSequence charSequence = this.mText;
                LocaleList localeList = this.mDefaultLocales;
                EntityConfig entityConfig = this.mEntityConfig;
                boolean z = this.mLegacyFallback;
                Bundle bundle = this.mExtras;
                if (bundle == null) {
                    bundle = Bundle.EMPTY;
                }
                return new Request(charSequence, localeList, entityConfig, z, bundle, null);
            }
        }

        /* synthetic */ Request(CharSequence x0, LocaleList x1, EntityConfig x2, boolean x3, Bundle x4, AnonymousClass1 x5) {
            this(x0, x1, x2, x3, x4);
        }

        private Request(CharSequence text, LocaleList defaultLocales, EntityConfig entityConfig, boolean legacyFallback, Bundle extras) {
            this.mText = text;
            this.mDefaultLocales = defaultLocales;
            this.mEntityConfig = entityConfig;
            this.mLegacyFallback = legacyFallback;
            this.mExtras = extras;
        }

        public CharSequence getText() {
            return this.mText;
        }

        public LocaleList getDefaultLocales() {
            return this.mDefaultLocales;
        }

        public EntityConfig getEntityConfig() {
            return this.mEntityConfig;
        }

        public boolean isLegacyFallback() {
            return this.mLegacyFallback;
        }

        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public void setCallingPackageName(String callingPackageName) {
            this.mCallingPackageName = callingPackageName;
        }

        public String getCallingPackageName() {
            return this.mCallingPackageName;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mText.toString());
            dest.writeParcelable(this.mDefaultLocales, flags);
            dest.writeParcelable(this.mEntityConfig, flags);
            dest.writeString(this.mCallingPackageName);
            dest.writeBundle(this.mExtras);
        }

        private static Request readFromParcel(Parcel in) {
            String text = in.readString();
            LocaleList defaultLocales = (LocaleList) in.readParcelable(null);
            EntityConfig entityConfig = (EntityConfig) in.readParcelable(null);
            String callingPackageName = in.readString();
            Request request = new Request(text, defaultLocales, entityConfig, true, in.readBundle());
            request.setCallingPackageName(callingPackageName);
            return request;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }

    public static final class TextLink implements Parcelable {
        public static final Creator<TextLink> CREATOR = new Creator<TextLink>() {
            public TextLink createFromParcel(Parcel in) {
                return TextLink.readFromParcel(in);
            }

            public TextLink[] newArray(int size) {
                return new TextLink[size];
            }
        };
        private final int mEnd;
        private final EntityConfidence mEntityScores;
        private final Bundle mExtras;
        private final int mStart;
        private final URLSpan mUrlSpan;

        /* synthetic */ TextLink(int x0, int x1, EntityConfidence x2, Bundle x3, URLSpan x4, AnonymousClass1 x5) {
            this(x0, x1, x2, x3, x4);
        }

        private TextLink(int start, int end, EntityConfidence entityConfidence, Bundle extras, URLSpan urlSpan) {
            Preconditions.checkNotNull(entityConfidence);
            boolean z = true;
            Preconditions.checkArgument(entityConfidence.getEntities().isEmpty() ^ 1);
            if (start > end) {
                z = false;
            }
            Preconditions.checkArgument(z);
            Preconditions.checkNotNull(extras);
            this.mStart = start;
            this.mEnd = end;
            this.mEntityScores = entityConfidence;
            this.mUrlSpan = urlSpan;
            this.mExtras = extras;
        }

        public int getStart() {
            return this.mStart;
        }

        public int getEnd() {
            return this.mEnd;
        }

        public int getEntityCount() {
            return this.mEntityScores.getEntities().size();
        }

        public String getEntity(int index) {
            return (String) this.mEntityScores.getEntities().get(index);
        }

        public float getConfidenceScore(String entityType) {
            return this.mEntityScores.getConfidenceScore(entityType);
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public String toString() {
            return String.format(Locale.US, "TextLink{start=%s, end=%s, entityScores=%s, urlSpan=%s}", new Object[]{Integer.valueOf(this.mStart), Integer.valueOf(this.mEnd), this.mEntityScores, this.mUrlSpan});
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            this.mEntityScores.writeToParcel(dest, flags);
            dest.writeInt(this.mStart);
            dest.writeInt(this.mEnd);
            dest.writeBundle(this.mExtras);
        }

        private static TextLink readFromParcel(Parcel in) {
            EntityConfidence entityConfidence = (EntityConfidence) EntityConfidence.CREATOR.createFromParcel(in);
            return new TextLink(in.readInt(), in.readInt(), entityConfidence, in.readBundle(), null);
        }
    }

    public static class TextLinkSpan extends ClickableSpan {
        public static final int INVOCATION_METHOD_KEYBOARD = 1;
        public static final int INVOCATION_METHOD_TOUCH = 0;
        public static final int INVOCATION_METHOD_UNSPECIFIED = -1;
        private final TextLink mTextLink;

        @Retention(RetentionPolicy.SOURCE)
        public @interface InvocationMethod {
        }

        public TextLinkSpan(TextLink textLink) {
            this.mTextLink = textLink;
        }

        public void onClick(View widget) {
            onClick(widget, -1);
        }

        public final void onClick(View widget, int invocationMethod) {
            if (widget instanceof TextView) {
                TextView textView = (TextView) widget;
                if (TextClassificationManager.getSettings(textView.getContext()).isSmartLinkifyEnabled()) {
                    if (invocationMethod != 0) {
                        textView.handleClick(this);
                    } else {
                        textView.requestActionMode(this);
                    }
                } else if (this.mTextLink.mUrlSpan != null) {
                    this.mTextLink.mUrlSpan.onClick(textView);
                } else {
                    textView.handleClick(this);
                }
            }
        }

        public final TextLink getTextLink() {
            return this.mTextLink;
        }

        @VisibleForTesting(visibility = Visibility.PRIVATE)
        public final String getUrl() {
            if (this.mTextLink.mUrlSpan != null) {
                return this.mTextLink.mUrlSpan.getURL();
            }
            return null;
        }
    }

    /* synthetic */ TextLinks(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    /* synthetic */ TextLinks(String x0, ArrayList x1, Bundle x2, AnonymousClass1 x3) {
        this(x0, x1, x2);
    }

    private TextLinks(String fullText, ArrayList<TextLink> links, Bundle extras) {
        this.mFullText = fullText;
        this.mLinks = Collections.unmodifiableList(links);
        this.mExtras = extras;
    }

    public String getText() {
        return this.mFullText;
    }

    public Collection<TextLink> getLinks() {
        return this.mLinks;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public int apply(Spannable text, int applyStrategy, Function<TextLink, TextLinkSpan> spanFactory) {
        Preconditions.checkNotNull(text);
        return new android.view.textclassifier.TextLinksParams.Builder().setApplyStrategy(applyStrategy).setSpanFactory(spanFactory).build().apply(text, this);
    }

    public String toString() {
        return String.format(Locale.US, "TextLinks{fullText=%s, links=%s}", new Object[]{this.mFullText, this.mLinks});
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFullText);
        dest.writeTypedList(this.mLinks);
        dest.writeBundle(this.mExtras);
    }

    private TextLinks(Parcel in) {
        this.mFullText = in.readString();
        this.mLinks = in.createTypedArrayList(TextLink.CREATOR);
        this.mExtras = in.readBundle();
    }
}
