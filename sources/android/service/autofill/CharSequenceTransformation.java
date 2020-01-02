package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telecom.Logging.Session;
import android.util.Log;
import android.util.Pair;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CharSequenceTransformation extends InternalTransformation implements Transformation, Parcelable {
    public static final Creator<CharSequenceTransformation> CREATOR = new Creator<CharSequenceTransformation>() {
        public CharSequenceTransformation createFromParcel(Parcel parcel) {
            AutofillId[] ids = (AutofillId[]) parcel.readParcelableArray(null, AutofillId.class);
            Pattern[] regexs = (Pattern[]) parcel.readSerializable();
            String[] substs = parcel.createStringArray();
            Builder builder = new Builder(ids[0], regexs[0], substs[0]);
            int size = ids.length;
            for (int i = 1; i < size; i++) {
                builder.addField(ids[i], regexs[i], substs[i]);
            }
            return builder.build();
        }

        public CharSequenceTransformation[] newArray(int size) {
            return new CharSequenceTransformation[size];
        }
    };
    private static final String TAG = "CharSequenceTransformation";
    private final LinkedHashMap<AutofillId, Pair<Pattern, String>> mFields;

    public static class Builder {
        private boolean mDestroyed;
        private final LinkedHashMap<AutofillId, Pair<Pattern, String>> mFields = new LinkedHashMap();

        public Builder(AutofillId id, Pattern regex, String subst) {
            addField(id, regex, subst);
        }

        public Builder addField(AutofillId id, Pattern regex, String subst) {
            throwIfDestroyed();
            Preconditions.checkNotNull(id);
            Preconditions.checkNotNull(regex);
            Preconditions.checkNotNull(subst);
            this.mFields.put(id, new Pair(regex, subst));
            return this;
        }

        public CharSequenceTransformation build() {
            throwIfDestroyed();
            this.mDestroyed = true;
            return new CharSequenceTransformation(this, null);
        }

        private void throwIfDestroyed() {
            Preconditions.checkState(this.mDestroyed ^ 1, "Already called build()");
        }
    }

    /* synthetic */ CharSequenceTransformation(Builder x0, AnonymousClass1 x1) {
        this(x0);
    }

    private CharSequenceTransformation(Builder builder) {
        this.mFields = builder.mFields;
    }

    public void apply(ValueFinder finder, RemoteViews parentTemplate, int childViewId) throws Exception {
        StringBuilder stringBuilder;
        StringBuilder converted = new StringBuilder();
        int size = this.mFields.size();
        boolean z = Helper.sDebug;
        String str = TAG;
        if (z) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(size);
            stringBuilder.append(" fields on id ");
            stringBuilder.append(childViewId);
            Log.d(str, stringBuilder.toString());
        }
        for (Entry<AutofillId, Pair<Pattern, String>> entry : this.mFields.entrySet()) {
            AutofillId id = (AutofillId) entry.getKey();
            Pair<Pattern, String> field = (Pair) entry.getValue();
            String value = finder.findByAutofillId(id);
            if (value == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("No value for id ");
                stringBuilder.append(id);
                Log.w(str, stringBuilder.toString());
                return;
            }
            try {
                Matcher matcher = ((Pattern) field.first).matcher(value);
                if (matcher.find()) {
                    converted.append(matcher.replaceAll((String) field.second));
                } else {
                    if (Helper.sDebug) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Match for ");
                        stringBuilder.append(field.first);
                        stringBuilder.append(" failed on id ");
                        stringBuilder.append(id);
                        Log.d(str, stringBuilder.toString());
                    }
                    return;
                }
            } catch (Exception e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Cannot apply ");
                stringBuilder2.append(((Pattern) field.first).pattern());
                stringBuilder2.append(Session.SUBSESSION_SEPARATION_CHAR);
                stringBuilder2.append((String) field.second);
                stringBuilder2.append(" to field with autofill id");
                stringBuilder2.append(id);
                stringBuilder2.append(": ");
                stringBuilder2.append(e.getClass());
                Log.w(str, stringBuilder2.toString());
                throw e;
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Converting text on child ");
        stringBuilder.append(childViewId);
        stringBuilder.append(" to ");
        stringBuilder.append(converted.length());
        stringBuilder.append("_chars");
        Log.d(str, stringBuilder.toString());
        parentTemplate.setCharSequence(childViewId, "setText", converted);
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MultipleViewsCharSequenceTransformation: [fields=");
        stringBuilder.append(this.mFields);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        int size = this.mFields.size();
        AutofillId[] ids = new AutofillId[size];
        Pattern[] regexs = new Pattern[size];
        String[] substs = new String[size];
        int i = 0;
        for (Entry<AutofillId, Pair<Pattern, String>> entry : this.mFields.entrySet()) {
            ids[i] = (AutofillId) entry.getKey();
            Pair<Pattern, String> pair = (Pair) entry.getValue();
            regexs[i] = (Pattern) pair.first;
            substs[i] = (String) pair.second;
            i++;
        }
        parcel.writeParcelableArray(ids, flags);
        parcel.writeSerializable(regexs);
        parcel.writeStringArray(substs);
    }
}
