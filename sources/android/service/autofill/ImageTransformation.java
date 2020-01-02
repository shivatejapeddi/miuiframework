package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class ImageTransformation extends InternalTransformation implements Transformation, Parcelable {
    public static final Creator<ImageTransformation> CREATOR = new Creator<ImageTransformation>() {
        public ImageTransformation createFromParcel(Parcel parcel) {
            Builder builder;
            AutofillId id = (AutofillId) parcel.readParcelable(null);
            Pattern[] regexs = (Pattern[]) parcel.readSerializable();
            int[] resIds = parcel.createIntArray();
            CharSequence[] contentDescriptions = parcel.readCharSequenceArray();
            CharSequence contentDescription = contentDescriptions[0];
            if (contentDescription != null) {
                builder = new Builder(id, regexs[0], resIds[0], contentDescription);
            } else {
                builder = new Builder(id, regexs[0], resIds[0]);
            }
            Builder builder2 = builder;
            int size = regexs.length;
            for (int i = 1; i < size; i++) {
                if (contentDescriptions[i] != null) {
                    builder2.addOption(regexs[i], resIds[i], contentDescriptions[i]);
                } else {
                    builder2.addOption(regexs[i], resIds[i]);
                }
            }
            return builder2.build();
        }

        public ImageTransformation[] newArray(int size) {
            return new ImageTransformation[size];
        }
    };
    private static final String TAG = "ImageTransformation";
    private final AutofillId mId;
    private final ArrayList<Option> mOptions;

    public static class Builder {
        private boolean mDestroyed;
        private final AutofillId mId;
        private final ArrayList<Option> mOptions = new ArrayList();

        @Deprecated
        public Builder(AutofillId id, Pattern regex, int resId) {
            this.mId = (AutofillId) Preconditions.checkNotNull(id);
            addOption(regex, resId);
        }

        public Builder(AutofillId id, Pattern regex, int resId, CharSequence contentDescription) {
            this.mId = (AutofillId) Preconditions.checkNotNull(id);
            addOption(regex, resId, contentDescription);
        }

        @Deprecated
        public Builder addOption(Pattern regex, int resId) {
            addOptionInternal(regex, resId, null);
            return this;
        }

        public Builder addOption(Pattern regex, int resId, CharSequence contentDescription) {
            addOptionInternal(regex, resId, (CharSequence) Preconditions.checkNotNull(contentDescription));
            return this;
        }

        private void addOptionInternal(Pattern regex, int resId, CharSequence contentDescription) {
            throwIfDestroyed();
            Preconditions.checkNotNull(regex);
            Preconditions.checkArgument(resId != 0);
            this.mOptions.add(new Option(regex, resId, contentDescription));
        }

        public ImageTransformation build() {
            throwIfDestroyed();
            this.mDestroyed = true;
            return new ImageTransformation(this, null);
        }

        private void throwIfDestroyed() {
            Preconditions.checkState(this.mDestroyed ^ 1, "Already called build()");
        }
    }

    private static final class Option {
        public final CharSequence contentDescription;
        public final Pattern pattern;
        public final int resId;

        Option(Pattern pattern, int resId, CharSequence contentDescription) {
            this.pattern = pattern;
            this.resId = resId;
            this.contentDescription = TextUtils.trimNoCopySpans(contentDescription);
        }
    }

    /* synthetic */ ImageTransformation(Builder x0, AnonymousClass1 x1) {
        this(x0);
    }

    private ImageTransformation(Builder builder) {
        this.mId = builder.mId;
        this.mOptions = builder.mOptions;
    }

    public void apply(ValueFinder finder, RemoteViews parentTemplate, int childViewId) throws Exception {
        String str = ": ";
        String value = finder.findByAutofillId(this.mId);
        String str2 = TAG;
        StringBuilder stringBuilder;
        if (value == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("No view for id ");
            stringBuilder.append(this.mId);
            Log.w(str2, stringBuilder.toString());
            return;
        }
        int size = this.mOptions.size();
        if (Helper.sDebug) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(size);
            stringBuilder2.append(" multiple options on id ");
            stringBuilder2.append(childViewId);
            stringBuilder2.append(" to compare against");
            Log.d(str2, stringBuilder2.toString());
        }
        int i = 0;
        while (i < size) {
            Option option = (Option) this.mOptions.get(i);
            try {
                if (option.pattern.matcher(value).matches()) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Found match at ");
                    stringBuilder3.append(i);
                    stringBuilder3.append(str);
                    stringBuilder3.append(option);
                    Log.d(str2, stringBuilder3.toString());
                    parentTemplate.setImageViewResource(childViewId, option.resId);
                    if (option.contentDescription != null) {
                        parentTemplate.setContentDescription(childViewId, option.contentDescription);
                    }
                    return;
                }
                i++;
            } catch (Exception e) {
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("Error matching regex #");
                stringBuilder4.append(i);
                stringBuilder4.append("(");
                stringBuilder4.append(option.pattern);
                stringBuilder4.append(") on id ");
                stringBuilder4.append(option.resId);
                stringBuilder4.append(str);
                stringBuilder4.append(e.getClass());
                Log.w(str2, stringBuilder4.toString());
                throw e;
            }
        }
        if (Helper.sDebug) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("No match for ");
            stringBuilder.append(value);
            Log.d(str2, stringBuilder.toString());
        }
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ImageTransformation: [id=");
        stringBuilder.append(this.mId);
        stringBuilder.append(", options=");
        stringBuilder.append(this.mOptions);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(this.mId, flags);
        int size = this.mOptions.size();
        Pattern[] patterns = new Pattern[size];
        int[] resIds = new int[size];
        CharSequence[] contentDescriptions = new String[size];
        for (int i = 0; i < size; i++) {
            Option option = (Option) this.mOptions.get(i);
            patterns[i] = option.pattern;
            resIds[i] = option.resId;
            contentDescriptions[i] = option.contentDescription;
        }
        parcel.writeSerializable(patterns);
        parcel.writeIntArray(resIds);
        parcel.writeCharSequenceArray(contentDescriptions);
    }
}
