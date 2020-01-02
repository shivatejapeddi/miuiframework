package android.view.textclassifier;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.RemoteAction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.SpannedString;
import android.util.ArrayMap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.textclassifier.TextClassifier.Utils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.Preconditions;
import com.google.android.textclassifier.AnnotatorModel.ClassificationResult;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class TextClassification implements Parcelable {
    public static final Creator<TextClassification> CREATOR = new Creator<TextClassification>() {
        public TextClassification createFromParcel(Parcel in) {
            return new TextClassification(in, null);
        }

        public TextClassification[] newArray(int size) {
            return new TextClassification[size];
        }
    };
    public static final TextClassification EMPTY = new Builder().build();
    private static final String LOG_TAG = "TextClassification";
    private static final int MAX_LEGACY_ICON_SIZE = 192;
    private final List<RemoteAction> mActions;
    private final EntityConfidence mEntityConfidence;
    private final Bundle mExtras;
    private final String mId;
    private final Drawable mLegacyIcon;
    private final Intent mLegacyIntent;
    private final String mLegacyLabel;
    private final OnClickListener mLegacyOnClickListener;
    private final String mText;

    public static final class Builder {
        private final ArrayList<Intent> mActionIntents = new ArrayList();
        private List<RemoteAction> mActions = new ArrayList();
        private final Map<String, ClassificationResult> mClassificationResults = new ArrayMap();
        private Bundle mExtras;
        private Bundle mForeignLanguageExtra;
        private String mId;
        private Drawable mLegacyIcon;
        private Intent mLegacyIntent;
        private String mLegacyLabel;
        private OnClickListener mLegacyOnClickListener;
        private String mText;
        private final Map<String, Float> mTypeScoreMap = new ArrayMap();

        public Builder setText(String text) {
            this.mText = text;
            return this;
        }

        public Builder setEntityType(String type, float confidenceScore) {
            setEntityType(type, confidenceScore, null);
            return this;
        }

        public Builder setEntityType(ClassificationResult classificationResult) {
            setEntityType(classificationResult.getCollection(), classificationResult.getScore(), classificationResult);
            return this;
        }

        private Builder setEntityType(String type, float confidenceScore, ClassificationResult classificationResult) {
            this.mTypeScoreMap.put(type, Float.valueOf(confidenceScore));
            this.mClassificationResults.put(type, classificationResult);
            return this;
        }

        public Builder addAction(RemoteAction action) {
            return addAction(action, null);
        }

        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public Builder addAction(RemoteAction action, Intent intent) {
            Preconditions.checkArgument(action != null);
            this.mActions.add(action);
            this.mActionIntents.add(intent);
            return this;
        }

        @Deprecated
        public Builder setIcon(Drawable icon) {
            this.mLegacyIcon = icon;
            return this;
        }

        @Deprecated
        public Builder setLabel(String label) {
            this.mLegacyLabel = label;
            return this;
        }

        @Deprecated
        public Builder setIntent(Intent intent) {
            this.mLegacyIntent = intent;
            return this;
        }

        @Deprecated
        public Builder setOnClickListener(OnClickListener onClickListener) {
            this.mLegacyOnClickListener = onClickListener;
            return this;
        }

        public Builder setId(String id) {
            this.mId = id;
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public Builder setForeignLanguageExtra(Bundle extra) {
            this.mForeignLanguageExtra = extra;
            return this;
        }

        public TextClassification build() {
            EntityConfidence entityConfidence = new EntityConfidence(this.mTypeScoreMap);
            return new TextClassification(this.mText, this.mLegacyIcon, this.mLegacyLabel, this.mLegacyIntent, this.mLegacyOnClickListener, this.mActions, entityConfidence, this.mId, buildExtras(entityConfidence), null);
        }

        private Bundle buildExtras(EntityConfidence entityConfidence) {
            Bundle extras = this.mExtras;
            if (extras == null) {
                extras = new Bundle();
            }
            if (this.mActionIntents.stream().anyMatch(-$$Lambda$L_UQMPjXwBN0ch4zL2dD82nf9RI.INSTANCE)) {
                ExtrasUtils.putActionsIntents(extras, this.mActionIntents);
            }
            Bundle bundle = this.mForeignLanguageExtra;
            if (bundle != null) {
                ExtrasUtils.putForeignLanguageExtra(extras, bundle);
            }
            List<String> sortedTypes = entityConfidence.getEntities();
            ArrayList<ClassificationResult> sortedEntities = new ArrayList();
            for (String type : sortedTypes) {
                sortedEntities.add((ClassificationResult) this.mClassificationResults.get(type));
            }
            ExtrasUtils.putEntities(extras, (ClassificationResult[]) sortedEntities.toArray(new ClassificationResult[0]));
            return extras.isEmpty() ? Bundle.EMPTY : extras;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface IntentType {
        public static final int ACTIVITY = 0;
        public static final int SERVICE = 1;
        public static final int UNSUPPORTED = -1;
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
        private final int mEndIndex;
        private final Bundle mExtras;
        private final ZonedDateTime mReferenceTime;
        private final int mStartIndex;
        private final CharSequence mText;

        public static final class Builder {
            private LocaleList mDefaultLocales;
            private final int mEndIndex;
            private Bundle mExtras;
            private ZonedDateTime mReferenceTime;
            private final int mStartIndex;
            private final CharSequence mText;

            public Builder(CharSequence text, int startIndex, int endIndex) {
                Utils.checkArgument(text, startIndex, endIndex);
                this.mText = text;
                this.mStartIndex = startIndex;
                this.mEndIndex = endIndex;
            }

            public Builder setDefaultLocales(LocaleList defaultLocales) {
                this.mDefaultLocales = defaultLocales;
                return this;
            }

            public Builder setReferenceTime(ZonedDateTime referenceTime) {
                this.mReferenceTime = referenceTime;
                return this;
            }

            public Builder setExtras(Bundle extras) {
                this.mExtras = extras;
                return this;
            }

            public Request build() {
                SpannedString spannedString = new SpannedString(this.mText);
                int i = this.mStartIndex;
                int i2 = this.mEndIndex;
                LocaleList localeList = this.mDefaultLocales;
                ZonedDateTime zonedDateTime = this.mReferenceTime;
                Bundle bundle = this.mExtras;
                if (bundle == null) {
                    bundle = Bundle.EMPTY;
                }
                return new Request(spannedString, i, i2, localeList, zonedDateTime, bundle, null);
            }
        }

        /* synthetic */ Request(CharSequence x0, int x1, int x2, LocaleList x3, ZonedDateTime x4, Bundle x5, AnonymousClass1 x6) {
            this(x0, x1, x2, x3, x4, x5);
        }

        private Request(CharSequence text, int startIndex, int endIndex, LocaleList defaultLocales, ZonedDateTime referenceTime, Bundle extras) {
            this.mText = text;
            this.mStartIndex = startIndex;
            this.mEndIndex = endIndex;
            this.mDefaultLocales = defaultLocales;
            this.mReferenceTime = referenceTime;
            this.mExtras = extras;
        }

        public CharSequence getText() {
            return this.mText;
        }

        public int getStartIndex() {
            return this.mStartIndex;
        }

        public int getEndIndex() {
            return this.mEndIndex;
        }

        public LocaleList getDefaultLocales() {
            return this.mDefaultLocales;
        }

        public ZonedDateTime getReferenceTime() {
            return this.mReferenceTime;
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
            dest.writeCharSequence(this.mText);
            dest.writeInt(this.mStartIndex);
            dest.writeInt(this.mEndIndex);
            dest.writeParcelable(this.mDefaultLocales, flags);
            ZonedDateTime zonedDateTime = this.mReferenceTime;
            dest.writeString(zonedDateTime == null ? null : zonedDateTime.toString());
            dest.writeString(this.mCallingPackageName);
            dest.writeBundle(this.mExtras);
        }

        private static Request readFromParcel(Parcel in) {
            CharSequence text = in.readCharSequence();
            int startIndex = in.readInt();
            int endIndex = in.readInt();
            ClassLoader classLoader = null;
            LocaleList defaultLocales = (LocaleList) in.readParcelable(null);
            String referenceTimeString = in.readString();
            if (referenceTimeString != null) {
                classLoader = ZonedDateTime.parse(referenceTimeString);
            }
            ZonedDateTime referenceTime = classLoader;
            String callingPackageName = in.readString();
            Request request = new Request(text, startIndex, endIndex, defaultLocales, referenceTime, in.readBundle());
            request.setCallingPackageName(callingPackageName);
            return request;
        }
    }

    /* synthetic */ TextClassification(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    /* synthetic */ TextClassification(String x0, Drawable x1, String x2, Intent x3, OnClickListener x4, List x5, EntityConfidence x6, String x7, Bundle x8, AnonymousClass1 x9) {
        this(x0, x1, x2, x3, x4, x5, x6, x7, x8);
    }

    private TextClassification(String text, Drawable legacyIcon, String legacyLabel, Intent legacyIntent, OnClickListener legacyOnClickListener, List<RemoteAction> actions, EntityConfidence entityConfidence, String id, Bundle extras) {
        this.mText = text;
        this.mLegacyIcon = legacyIcon;
        this.mLegacyLabel = legacyLabel;
        this.mLegacyIntent = legacyIntent;
        this.mLegacyOnClickListener = legacyOnClickListener;
        this.mActions = Collections.unmodifiableList(actions);
        this.mEntityConfidence = (EntityConfidence) Preconditions.checkNotNull(entityConfidence);
        this.mId = id;
        this.mExtras = extras;
    }

    public String getText() {
        return this.mText;
    }

    public int getEntityCount() {
        return this.mEntityConfidence.getEntities().size();
    }

    public String getEntity(int index) {
        return (String) this.mEntityConfidence.getEntities().get(index);
    }

    public float getConfidenceScore(String entity) {
        return this.mEntityConfidence.getConfidenceScore(entity);
    }

    public List<RemoteAction> getActions() {
        return this.mActions;
    }

    @Deprecated
    public Drawable getIcon() {
        return this.mLegacyIcon;
    }

    @Deprecated
    public CharSequence getLabel() {
        return this.mLegacyLabel;
    }

    @Deprecated
    public Intent getIntent() {
        return this.mLegacyIntent;
    }

    public OnClickListener getOnClickListener() {
        return this.mLegacyOnClickListener;
    }

    public String getId() {
        return this.mId;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public String toString() {
        return String.format(Locale.US, "TextClassification {text=%s, entities=%s, actions=%s, id=%s, extras=%s}", new Object[]{this.mText, this.mEntityConfidence, this.mActions, this.mId, this.mExtras});
    }

    public static OnClickListener createIntentOnClickListener(PendingIntent intent) {
        Preconditions.checkNotNull(intent);
        return new -$$Lambda$TextClassification$ysasaE5ZkXkkzjVWIJ06GTV92-g(intent);
    }

    static /* synthetic */ void lambda$createIntentOnClickListener$0(PendingIntent intent, View v) {
        try {
            intent.send();
        } catch (CanceledException e) {
            Log.e(LOG_TAG, "Error sending PendingIntent", e);
        }
    }

    public static PendingIntent createPendingIntent(Context context, Intent intent, int requestCode) {
        return PendingIntent.getActivity(context, requestCode, intent, 134217728);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mText);
        dest.writeTypedList(this.mActions);
        this.mEntityConfidence.writeToParcel(dest, flags);
        dest.writeString(this.mId);
        dest.writeBundle(this.mExtras);
    }

    private TextClassification(Parcel in) {
        this.mText = in.readString();
        this.mActions = in.createTypedArrayList(RemoteAction.CREATOR);
        if (this.mActions.isEmpty()) {
            this.mLegacyIcon = null;
            this.mLegacyLabel = null;
            this.mLegacyOnClickListener = null;
        } else {
            RemoteAction action = (RemoteAction) this.mActions.get(0);
            this.mLegacyIcon = maybeLoadDrawable(action.getIcon());
            this.mLegacyLabel = action.getTitle().toString();
            this.mLegacyOnClickListener = createIntentOnClickListener(((RemoteAction) this.mActions.get(0)).getActionIntent());
        }
        this.mLegacyIntent = null;
        this.mEntityConfidence = (EntityConfidence) EntityConfidence.CREATOR.createFromParcel(in);
        this.mId = in.readString();
        this.mExtras = in.readBundle();
    }

    private static Drawable maybeLoadDrawable(Icon icon) {
        if (icon == null) {
            return null;
        }
        int type = icon.getType();
        if (type == 1) {
            return new BitmapDrawable(Resources.getSystem(), icon.getBitmap());
        }
        if (type == 3) {
            return new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeByteArray(icon.getDataBytes(), icon.getDataOffset(), icon.getDataLength()));
        }
        if (type != 5) {
            return null;
        }
        return new AdaptiveIconDrawable(null, new BitmapDrawable(Resources.getSystem(), icon.getBitmap()));
    }
}
