package android.service.autofill;

import android.app.ActivityThread;
import android.content.ContentResolver;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class UserData implements FieldClassificationUserData, Parcelable {
    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        public UserData createFromParcel(Parcel parcel) {
            int i;
            String id = parcel.readString();
            String[] categoryIds = parcel.readStringArray();
            String[] values = parcel.readStringArray();
            String defaultAlgorithm = parcel.readString();
            Bundle defaultArgs = parcel.readBundle();
            ArrayMap<String, String> categoryAlgorithms = new ArrayMap();
            parcel.readMap(categoryAlgorithms, String.class.getClassLoader());
            ArrayMap<String, Bundle> categoryArgs = new ArrayMap();
            parcel.readMap(categoryArgs, Bundle.class.getClassLoader());
            Builder builder = new Builder(id, values[0], categoryIds[0]).setFieldClassificationAlgorithm(defaultAlgorithm, defaultArgs);
            for (i = 1; i < categoryIds.length; i++) {
                builder.add(values[i], categoryIds[i]);
            }
            i = categoryAlgorithms.size();
            if (i > 0) {
                for (int i2 = 0; i2 < i; i2++) {
                    String categoryId = (String) categoryAlgorithms.keyAt(i2);
                    builder.setFieldClassificationAlgorithmForCategory(categoryId, (String) categoryAlgorithms.valueAt(i2), (Bundle) categoryArgs.get(categoryId));
                }
            }
            return builder.build();
        }

        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };
    private static final int DEFAULT_MAX_CATEGORY_COUNT = 10;
    private static final int DEFAULT_MAX_FIELD_CLASSIFICATION_IDS_SIZE = 10;
    private static final int DEFAULT_MAX_USER_DATA_SIZE = 50;
    private static final int DEFAULT_MAX_VALUE_LENGTH = 100;
    private static final int DEFAULT_MIN_VALUE_LENGTH = 3;
    private static final String TAG = "UserData";
    private final ArrayMap<String, String> mCategoryAlgorithms;
    private final ArrayMap<String, Bundle> mCategoryArgs;
    private final String[] mCategoryIds;
    private final String mDefaultAlgorithm;
    private final Bundle mDefaultArgs;
    private final String mId;
    private final String[] mValues;

    public static final class Builder {
        private ArrayMap<String, String> mCategoryAlgorithms;
        private ArrayMap<String, Bundle> mCategoryArgs;
        private final ArrayList<String> mCategoryIds;
        private String mDefaultAlgorithm;
        private Bundle mDefaultArgs;
        private boolean mDestroyed;
        private final String mId;
        private final ArraySet<String> mUniqueCategoryIds = new ArraySet(UserData.getMaxCategoryCount());
        private final ArraySet<String> mUniqueValueCategoryPairs;
        private final ArrayList<String> mValues;

        public Builder(String id, String value, String categoryId) {
            this.mId = checkNotEmpty("id", id);
            checkNotEmpty("categoryId", categoryId);
            checkValidValue(value);
            int maxUserDataSize = UserData.getMaxUserDataSize();
            this.mCategoryIds = new ArrayList(maxUserDataSize);
            this.mValues = new ArrayList(maxUserDataSize);
            this.mUniqueValueCategoryPairs = new ArraySet(maxUserDataSize);
            addMapping(value, categoryId);
        }

        public Builder setFieldClassificationAlgorithm(String name, Bundle args) {
            throwIfDestroyed();
            this.mDefaultAlgorithm = name;
            this.mDefaultArgs = args;
            return this;
        }

        public Builder setFieldClassificationAlgorithmForCategory(String categoryId, String name, Bundle args) {
            throwIfDestroyed();
            Preconditions.checkNotNull(categoryId);
            if (this.mCategoryAlgorithms == null) {
                this.mCategoryAlgorithms = new ArrayMap(UserData.getMaxCategoryCount());
            }
            if (this.mCategoryArgs == null) {
                this.mCategoryArgs = new ArrayMap(UserData.getMaxCategoryCount());
            }
            this.mCategoryAlgorithms.put(categoryId, name);
            this.mCategoryArgs.put(categoryId, args);
            return this;
        }

        public Builder add(String value, String categoryId) {
            throwIfDestroyed();
            checkNotEmpty("categoryId", categoryId);
            checkValidValue(value);
            String str = "already added ";
            boolean z = true;
            if (!this.mUniqueCategoryIds.contains(categoryId)) {
                boolean z2 = this.mUniqueCategoryIds.size() < UserData.getMaxCategoryCount();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(this.mUniqueCategoryIds.size());
                stringBuilder.append(" unique category ids");
                Preconditions.checkState(z2, stringBuilder.toString());
            }
            if (this.mValues.size() >= UserData.getMaxUserDataSize()) {
                z = false;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(this.mValues.size());
            stringBuilder2.append(" elements");
            Preconditions.checkState(z, stringBuilder2.toString());
            addMapping(value, categoryId);
            return this;
        }

        private void addMapping(String value, String categoryId) {
            String pair = new StringBuilder();
            pair.append(value);
            pair.append(":");
            pair.append(categoryId);
            pair = pair.toString();
            if (this.mUniqueValueCategoryPairs.contains(pair)) {
                Log.w(UserData.TAG, "Ignoring entry with same value / category");
                return;
            }
            this.mCategoryIds.add(categoryId);
            this.mValues.add(value);
            this.mUniqueCategoryIds.add(categoryId);
            this.mUniqueValueCategoryPairs.add(pair);
        }

        private String checkNotEmpty(String name, String value) {
            Preconditions.checkNotNull(value);
            Preconditions.checkArgument(TextUtils.isEmpty(value) ^ 1, "%s cannot be empty", name);
            return value;
        }

        private void checkValidValue(String value) {
            Preconditions.checkNotNull(value);
            int length = value.length();
            int minValueLength = UserData.getMinValueLength();
            int maxValueLength = UserData.getMaxValueLength();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("value length (");
            stringBuilder.append(length);
            stringBuilder.append(")");
            Preconditions.checkArgumentInRange(length, minValueLength, maxValueLength, stringBuilder.toString());
        }

        public UserData build() {
            throwIfDestroyed();
            this.mDestroyed = true;
            return new UserData(this, null);
        }

        private void throwIfDestroyed() {
            if (this.mDestroyed) {
                throw new IllegalStateException("Already called #build()");
            }
        }
    }

    /* synthetic */ UserData(Builder x0, AnonymousClass1 x1) {
        this(x0);
    }

    private UserData(Builder builder) {
        this.mId = builder.mId;
        this.mCategoryIds = new String[builder.mCategoryIds.size()];
        builder.mCategoryIds.toArray(this.mCategoryIds);
        this.mValues = new String[builder.mValues.size()];
        builder.mValues.toArray(this.mValues);
        builder.mValues.toArray(this.mValues);
        this.mDefaultAlgorithm = builder.mDefaultAlgorithm;
        this.mDefaultArgs = builder.mDefaultArgs;
        this.mCategoryAlgorithms = builder.mCategoryAlgorithms;
        this.mCategoryArgs = builder.mCategoryArgs;
    }

    public String getFieldClassificationAlgorithm() {
        return this.mDefaultAlgorithm;
    }

    public Bundle getDefaultFieldClassificationArgs() {
        return this.mDefaultArgs;
    }

    public String getFieldClassificationAlgorithmForCategory(String categoryId) {
        Preconditions.checkNotNull(categoryId);
        ArrayMap arrayMap = this.mCategoryAlgorithms;
        if (arrayMap == null || !arrayMap.containsKey(categoryId)) {
            return null;
        }
        return (String) this.mCategoryAlgorithms.get(categoryId);
    }

    public String getId() {
        return this.mId;
    }

    public String[] getCategoryIds() {
        return this.mCategoryIds;
    }

    public String[] getValues() {
        return this.mValues;
    }

    public ArrayMap<String, String> getFieldClassificationAlgorithms() {
        return this.mCategoryAlgorithms;
    }

    public ArrayMap<String, Bundle> getFieldClassificationArgs() {
        return this.mCategoryArgs;
    }

    public void dump(String prefix, PrintWriter pw) {
        int i;
        pw.print(prefix);
        pw.print("id: ");
        pw.print(this.mId);
        pw.print(prefix);
        pw.print("Default Algorithm: ");
        pw.print(this.mDefaultAlgorithm);
        pw.print(prefix);
        pw.print("Default Args");
        pw.print(this.mDefaultArgs);
        ArrayMap arrayMap = this.mCategoryAlgorithms;
        String str = ": ";
        if (arrayMap != null && arrayMap.size() > 0) {
            pw.print(prefix);
            pw.print("Algorithms per category: ");
            for (i = 0; i < this.mCategoryAlgorithms.size(); i++) {
                pw.print(prefix);
                pw.print(prefix);
                pw.print((String) this.mCategoryAlgorithms.keyAt(i));
                pw.print(str);
                pw.println(Helper.getRedacted((CharSequence) this.mCategoryAlgorithms.valueAt(i)));
                pw.print("args=");
                pw.print(this.mCategoryArgs.get(this.mCategoryAlgorithms.keyAt(i)));
            }
        }
        pw.print(prefix);
        pw.print("Field ids size: ");
        pw.println(this.mCategoryIds.length);
        for (i = 0; i < this.mCategoryIds.length; i++) {
            pw.print(prefix);
            pw.print(prefix);
            pw.print(i);
            pw.print(str);
            pw.println(Helper.getRedacted(this.mCategoryIds[i]));
        }
        pw.print(prefix);
        pw.print("Values size: ");
        pw.println(this.mValues.length);
        for (i = 0; i < this.mValues.length; i++) {
            pw.print(prefix);
            pw.print(prefix);
            pw.print(i);
            pw.print(str);
            pw.println(Helper.getRedacted(this.mValues[i]));
        }
    }

    public static void dumpConstraints(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("maxUserDataSize: ");
        pw.println(getMaxUserDataSize());
        pw.print(prefix);
        pw.print("maxFieldClassificationIdsSize: ");
        pw.println(getMaxFieldClassificationIdsSize());
        pw.print(prefix);
        pw.print("maxCategoryCount: ");
        pw.println(getMaxCategoryCount());
        pw.print(prefix);
        pw.print("minValueLength: ");
        pw.println(getMinValueLength());
        pw.print(prefix);
        pw.print("maxValueLength: ");
        pw.println(getMaxValueLength());
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder builder = new StringBuilder("UserData: [id=").append(this.mId);
        builder.append(", categoryIds=");
        Helper.appendRedacted(builder, this.mCategoryIds);
        builder.append(", values=");
        Helper.appendRedacted(builder, this.mValues);
        builder.append("]");
        return builder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mId);
        parcel.writeStringArray(this.mCategoryIds);
        parcel.writeStringArray(this.mValues);
        parcel.writeString(this.mDefaultAlgorithm);
        parcel.writeBundle(this.mDefaultArgs);
        parcel.writeMap(this.mCategoryAlgorithms);
        parcel.writeMap(this.mCategoryArgs);
    }

    public static int getMaxUserDataSize() {
        return getInt(Secure.AUTOFILL_USER_DATA_MAX_USER_DATA_SIZE, 50);
    }

    public static int getMaxFieldClassificationIdsSize() {
        return getInt(Secure.AUTOFILL_USER_DATA_MAX_FIELD_CLASSIFICATION_IDS_SIZE, 10);
    }

    public static int getMaxCategoryCount() {
        return getInt(Secure.AUTOFILL_USER_DATA_MAX_CATEGORY_COUNT, 10);
    }

    public static int getMinValueLength() {
        return getInt(Secure.AUTOFILL_USER_DATA_MIN_VALUE_LENGTH, 3);
    }

    public static int getMaxValueLength() {
        return getInt(Secure.AUTOFILL_USER_DATA_MAX_VALUE_LENGTH, 100);
    }

    private static int getInt(String settings, int defaultValue) {
        ContentResolver cr = null;
        ActivityThread at = ActivityThread.currentActivityThread();
        if (at != null) {
            cr = at.getApplication().getContentResolver();
        }
        if (cr != null) {
            return Secure.getInt(cr, settings, defaultValue);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Could not read from ");
        stringBuilder.append(settings);
        stringBuilder.append("; hardcoding ");
        stringBuilder.append(defaultValue);
        Log.w(TAG, stringBuilder.toString());
        return defaultValue;
    }
}
