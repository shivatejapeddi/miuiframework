package android.content;

import android.annotation.UnsupportedAppUsage;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ContentProviderOperation implements Parcelable {
    public static final Creator<ContentProviderOperation> CREATOR = new Creator<ContentProviderOperation>() {
        public ContentProviderOperation createFromParcel(Parcel source) {
            return new ContentProviderOperation(source, null);
        }

        public ContentProviderOperation[] newArray(int size) {
            return new ContentProviderOperation[size];
        }
    };
    private static final String TAG = "ContentProviderOperation";
    public static final int TYPE_ASSERT = 4;
    @UnsupportedAppUsage
    public static final int TYPE_DELETE = 3;
    @UnsupportedAppUsage
    public static final int TYPE_INSERT = 1;
    @UnsupportedAppUsage
    public static final int TYPE_UPDATE = 2;
    private final Integer mExpectedCount;
    private final boolean mFailureAllowed;
    @UnsupportedAppUsage
    private final String mSelection;
    private final String[] mSelectionArgs;
    private final Map<Integer, Integer> mSelectionArgsBackReferences;
    @UnsupportedAppUsage
    private final int mType;
    @UnsupportedAppUsage
    private final Uri mUri;
    private final ContentValues mValues;
    private final ContentValues mValuesBackReferences;
    private final boolean mYieldAllowed;

    public static class Builder {
        private Integer mExpectedCount;
        private boolean mFailureAllowed;
        private String mSelection;
        private String[] mSelectionArgs;
        private Map<Integer, Integer> mSelectionArgsBackReferences;
        private final int mType;
        private final Uri mUri;
        private ContentValues mValues;
        private ContentValues mValuesBackReferences;
        private boolean mYieldAllowed;

        /* synthetic */ Builder(int x0, Uri x1, AnonymousClass1 x2) {
            this(x0, x1);
        }

        private Builder(int type, Uri uri) {
            if (uri != null) {
                this.mType = type;
                this.mUri = uri;
                return;
            }
            throw new IllegalArgumentException("uri must not be null");
        }

        public ContentProviderOperation build() {
            ContentValues contentValues;
            String str = "Empty values";
            if (this.mType == 2) {
                contentValues = this.mValues;
                if (contentValues == null || contentValues.isEmpty()) {
                    contentValues = this.mValuesBackReferences;
                    if (contentValues == null || contentValues.isEmpty()) {
                        throw new IllegalArgumentException(str);
                    }
                }
            }
            if (this.mType == 4) {
                contentValues = this.mValues;
                if (contentValues == null || contentValues.isEmpty()) {
                    contentValues = this.mValuesBackReferences;
                    if ((contentValues == null || contentValues.isEmpty()) && this.mExpectedCount == null) {
                        throw new IllegalArgumentException(str);
                    }
                }
            }
            return new ContentProviderOperation(this, null);
        }

        public Builder withValueBackReferences(ContentValues backReferences) {
            int i = this.mType;
            if (i == 1 || i == 2 || i == 4) {
                this.mValuesBackReferences = backReferences;
                return this;
            }
            throw new IllegalArgumentException("only inserts, updates, and asserts can have value back-references");
        }

        public Builder withValueBackReference(String key, int previousResult) {
            int i = this.mType;
            if (i == 1 || i == 2 || i == 4) {
                if (this.mValuesBackReferences == null) {
                    this.mValuesBackReferences = new ContentValues();
                }
                this.mValuesBackReferences.put(key, Integer.valueOf(previousResult));
                return this;
            }
            throw new IllegalArgumentException("only inserts, updates, and asserts can have value back-references");
        }

        public Builder withSelectionBackReference(int selectionArgIndex, int previousResult) {
            int i = this.mType;
            if (i == 2 || i == 3 || i == 4) {
                if (this.mSelectionArgsBackReferences == null) {
                    this.mSelectionArgsBackReferences = new HashMap();
                }
                this.mSelectionArgsBackReferences.put(Integer.valueOf(selectionArgIndex), Integer.valueOf(previousResult));
                return this;
            }
            throw new IllegalArgumentException("only updates, deletes, and asserts can have selection back-references");
        }

        public Builder withValues(ContentValues values) {
            int i = this.mType;
            if (i == 1 || i == 2 || i == 4) {
                if (this.mValues == null) {
                    this.mValues = new ContentValues();
                }
                this.mValues.putAll(values);
                return this;
            }
            throw new IllegalArgumentException("only inserts, updates, and asserts can have values");
        }

        public Builder withValue(String key, Object value) {
            int i = this.mType;
            if (i == 1 || i == 2 || i == 4) {
                if (this.mValues == null) {
                    this.mValues = new ContentValues();
                }
                if (value == null) {
                    this.mValues.putNull(key);
                } else if (value instanceof String) {
                    this.mValues.put(key, (String) value);
                } else if (value instanceof Byte) {
                    this.mValues.put(key, (Byte) value);
                } else if (value instanceof Short) {
                    this.mValues.put(key, (Short) value);
                } else if (value instanceof Integer) {
                    this.mValues.put(key, (Integer) value);
                } else if (value instanceof Long) {
                    this.mValues.put(key, (Long) value);
                } else if (value instanceof Float) {
                    this.mValues.put(key, (Float) value);
                } else if (value instanceof Double) {
                    this.mValues.put(key, (Double) value);
                } else if (value instanceof Boolean) {
                    this.mValues.put(key, (Boolean) value);
                } else if (value instanceof byte[]) {
                    this.mValues.put(key, (byte[]) value);
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("bad value type: ");
                    stringBuilder.append(value.getClass().getName());
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
                return this;
            }
            throw new IllegalArgumentException("only inserts and updates can have values");
        }

        public Builder withSelection(String selection, String[] selectionArgs) {
            int i = this.mType;
            if (i == 2 || i == 3 || i == 4) {
                this.mSelection = selection;
                if (selectionArgs == null) {
                    this.mSelectionArgs = null;
                } else {
                    this.mSelectionArgs = new String[selectionArgs.length];
                    System.arraycopy(selectionArgs, 0, this.mSelectionArgs, 0, selectionArgs.length);
                }
                return this;
            }
            throw new IllegalArgumentException("only updates, deletes, and asserts can have selections");
        }

        public Builder withExpectedCount(int count) {
            int i = this.mType;
            if (i == 2 || i == 3 || i == 4) {
                this.mExpectedCount = Integer.valueOf(count);
                return this;
            }
            throw new IllegalArgumentException("only updates, deletes, and asserts can have expected counts");
        }

        public Builder withYieldAllowed(boolean yieldAllowed) {
            this.mYieldAllowed = yieldAllowed;
            return this;
        }

        public Builder withFailureAllowed(boolean failureAllowed) {
            this.mFailureAllowed = failureAllowed;
            return this;
        }
    }

    private ContentProviderOperation(Builder builder) {
        this.mType = builder.mType;
        this.mUri = builder.mUri;
        this.mValues = builder.mValues;
        this.mSelection = builder.mSelection;
        this.mSelectionArgs = builder.mSelectionArgs;
        this.mExpectedCount = builder.mExpectedCount;
        this.mSelectionArgsBackReferences = builder.mSelectionArgsBackReferences;
        this.mValuesBackReferences = builder.mValuesBackReferences;
        this.mYieldAllowed = builder.mYieldAllowed;
        this.mFailureAllowed = builder.mFailureAllowed;
    }

    private ContentProviderOperation(Parcel source) {
        ContentValues contentValues;
        this.mType = source.readInt();
        this.mUri = (Uri) Uri.CREATOR.createFromParcel(source);
        Map map = null;
        this.mValues = source.readInt() != 0 ? (ContentValues) ContentValues.CREATOR.createFromParcel(source) : null;
        this.mSelection = source.readInt() != 0 ? source.readString() : null;
        this.mSelectionArgs = source.readInt() != 0 ? source.readStringArray() : null;
        this.mExpectedCount = source.readInt() != 0 ? Integer.valueOf(source.readInt()) : null;
        if (source.readInt() != 0) {
            contentValues = (ContentValues) ContentValues.CREATOR.createFromParcel(source);
        } else {
            contentValues = null;
        }
        this.mValuesBackReferences = contentValues;
        if (source.readInt() != 0) {
            map = new HashMap();
        }
        this.mSelectionArgsBackReferences = map;
        if (this.mSelectionArgsBackReferences != null) {
            int count = source.readInt();
            for (int i = 0; i < count; i++) {
                this.mSelectionArgsBackReferences.put(Integer.valueOf(source.readInt()), Integer.valueOf(source.readInt()));
            }
        }
        boolean z = false;
        this.mYieldAllowed = source.readInt() != 0;
        if (source.readInt() != 0) {
            z = true;
        }
        this.mFailureAllowed = z;
    }

    public ContentProviderOperation(ContentProviderOperation cpo, Uri withUri) {
        this.mType = cpo.mType;
        this.mUri = withUri;
        this.mValues = cpo.mValues;
        this.mSelection = cpo.mSelection;
        this.mSelectionArgs = cpo.mSelectionArgs;
        this.mExpectedCount = cpo.mExpectedCount;
        this.mSelectionArgsBackReferences = cpo.mSelectionArgsBackReferences;
        this.mValuesBackReferences = cpo.mValuesBackReferences;
        this.mYieldAllowed = cpo.mYieldAllowed;
        this.mFailureAllowed = cpo.mFailureAllowed;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        Uri.writeToParcel(dest, this.mUri);
        if (this.mValues != null) {
            dest.writeInt(1);
            this.mValues.writeToParcel(dest, 0);
        } else {
            dest.writeInt(0);
        }
        if (this.mSelection != null) {
            dest.writeInt(1);
            dest.writeString(this.mSelection);
        } else {
            dest.writeInt(0);
        }
        if (this.mSelectionArgs != null) {
            dest.writeInt(1);
            dest.writeStringArray(this.mSelectionArgs);
        } else {
            dest.writeInt(0);
        }
        if (this.mExpectedCount != null) {
            dest.writeInt(1);
            dest.writeInt(this.mExpectedCount.intValue());
        } else {
            dest.writeInt(0);
        }
        if (this.mValuesBackReferences != null) {
            dest.writeInt(1);
            this.mValuesBackReferences.writeToParcel(dest, 0);
        } else {
            dest.writeInt(0);
        }
        if (this.mSelectionArgsBackReferences != null) {
            dest.writeInt(1);
            dest.writeInt(this.mSelectionArgsBackReferences.size());
            for (Entry<Integer, Integer> entry : this.mSelectionArgsBackReferences.entrySet()) {
                dest.writeInt(((Integer) entry.getKey()).intValue());
                dest.writeInt(((Integer) entry.getValue()).intValue());
            }
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.mYieldAllowed);
        dest.writeInt(this.mFailureAllowed);
    }

    public static Builder newInsert(Uri uri) {
        return new Builder(1, uri, null);
    }

    public static Builder newUpdate(Uri uri) {
        return new Builder(2, uri, null);
    }

    public static Builder newDelete(Uri uri) {
        return new Builder(3, uri, null);
    }

    public static Builder newAssertQuery(Uri uri) {
        return new Builder(4, uri, null);
    }

    public Uri getUri() {
        return this.mUri;
    }

    public boolean isYieldAllowed() {
        return this.mYieldAllowed;
    }

    public boolean isFailureAllowed() {
        return this.mFailureAllowed;
    }

    @UnsupportedAppUsage
    public int getType() {
        return this.mType;
    }

    public boolean isInsert() {
        return this.mType == 1;
    }

    public boolean isDelete() {
        return this.mType == 3;
    }

    public boolean isUpdate() {
        return this.mType == 2;
    }

    public boolean isAssertQuery() {
        return this.mType == 4;
    }

    public boolean isWriteOperation() {
        int i = this.mType;
        return i == 3 || i == 1 || i == 2;
    }

    public boolean isReadOperation() {
        return this.mType == 4;
    }

    public ContentProviderResult apply(ContentProvider provider, ContentProviderResult[] backRefs, int numBackRefs) throws OperationApplicationException {
        if (!this.mFailureAllowed) {
            return applyInternal(provider, backRefs, numBackRefs);
        }
        try {
            return applyInternal(provider, backRefs, numBackRefs);
        } catch (Exception e) {
            return new ContentProviderResult(e.getMessage());
        }
    }

    private ContentProviderResult applyInternal(ContentProvider provider, ContentProviderResult[] backRefs, int numBackRefs) throws OperationApplicationException {
        ContentValues values = resolveValueBackReferences(backRefs, numBackRefs);
        String[] selectionArgs = resolveSelectionArgsBackReferences(backRefs, numBackRefs);
        int i = this.mType;
        StringBuilder stringBuilder;
        if (i == 1) {
            Uri newUri = provider.insert(this.mUri, values);
            if (newUri != null) {
                return new ContentProviderResult(newUri);
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Insert into ");
            stringBuilder.append(this.mUri);
            stringBuilder.append(" returned no result");
            throw new OperationApplicationException(stringBuilder.toString());
        }
        if (i == 3) {
            i = provider.delete(this.mUri, this.mSelection, selectionArgs);
        } else if (i == 2) {
            i = provider.update(this.mUri, values, this.mSelection, selectionArgs);
        } else if (i == 4) {
            String[] projection;
            if (values != null) {
                ArrayList<String> projectionList = new ArrayList();
                for (Entry<String, Object> entry : values.valueSet()) {
                    projectionList.add((String) entry.getKey());
                }
                projection = (String[]) projectionList.toArray(new String[projectionList.size()]);
            } else {
                projection = null;
            }
            Cursor cursor = provider.query(this.mUri, projection, this.mSelection, selectionArgs, null);
            try {
                int numRows = cursor.getCount();
                if (projection != null) {
                    while (cursor.moveToNext()) {
                        int i2 = 0;
                        while (i2 < projection.length) {
                            String cursorValue = cursor.getString(i2);
                            String expectedValue = values.getAsString(projection[i2]);
                            if (TextUtils.equals(cursorValue, expectedValue)) {
                                i2++;
                            } else {
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("Found value ");
                                stringBuilder2.append(cursorValue);
                                stringBuilder2.append(" when expected ");
                                stringBuilder2.append(expectedValue);
                                stringBuilder2.append(" for column ");
                                stringBuilder2.append(projection[i2]);
                                throw new OperationApplicationException(stringBuilder2.toString());
                            }
                        }
                    }
                }
                cursor.close();
                i = numRows;
            } catch (Throwable th) {
                cursor.close();
            }
        } else {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("bad type, ");
            stringBuilder3.append(this.mType);
            throw new IllegalStateException(stringBuilder3.toString());
        }
        Integer num = this.mExpectedCount;
        if (num == null || num.intValue() == i) {
            return new ContentProviderResult(i);
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Expected ");
        stringBuilder.append(this.mExpectedCount);
        stringBuilder.append(" rows but actual ");
        stringBuilder.append(i);
        throw new OperationApplicationException(stringBuilder.toString());
    }

    public ContentValues resolveValueBackReferences(ContentProviderResult[] backRefs, int numBackRefs) {
        if (this.mValuesBackReferences == null) {
            return this.mValues;
        }
        ContentValues contentValues = this.mValues;
        if (contentValues == null) {
            contentValues = new ContentValues();
        } else {
            contentValues = new ContentValues(contentValues);
        }
        for (Entry<String, Object> entry : this.mValuesBackReferences.valueSet()) {
            String key = (String) entry.getKey();
            Integer backRefIndex = this.mValuesBackReferences.getAsInteger(key);
            if (backRefIndex != null) {
                contentValues.put(key, Long.valueOf(backRefToValue(backRefs, numBackRefs, backRefIndex)));
            } else {
                Log.e(TAG, toString());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("values backref ");
                stringBuilder.append(key);
                stringBuilder.append(" is not an integer");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        return contentValues;
    }

    public String[] resolveSelectionArgsBackReferences(ContentProviderResult[] backRefs, int numBackRefs) {
        if (this.mSelectionArgsBackReferences == null) {
            return this.mSelectionArgs;
        }
        String[] strArr = this.mSelectionArgs;
        String[] newArgs = new String[strArr.length];
        System.arraycopy(strArr, 0, newArgs, 0, strArr.length);
        for (Entry<Integer, Integer> selectionArgBackRef : this.mSelectionArgsBackReferences.entrySet()) {
            newArgs[((Integer) selectionArgBackRef.getKey()).intValue()] = String.valueOf(backRefToValue(backRefs, numBackRefs, Integer.valueOf(((Integer) selectionArgBackRef.getValue()).intValue())));
        }
        return newArgs;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mType: ");
        stringBuilder.append(this.mType);
        stringBuilder.append(", mUri: ");
        stringBuilder.append(this.mUri);
        stringBuilder.append(", mSelection: ");
        stringBuilder.append(this.mSelection);
        stringBuilder.append(", mExpectedCount: ");
        stringBuilder.append(this.mExpectedCount);
        stringBuilder.append(", mYieldAllowed: ");
        stringBuilder.append(this.mYieldAllowed);
        stringBuilder.append(", mValues: ");
        stringBuilder.append(this.mValues);
        stringBuilder.append(", mValuesBackReferences: ");
        stringBuilder.append(this.mValuesBackReferences);
        stringBuilder.append(", mSelectionArgsBackReferences: ");
        stringBuilder.append(this.mSelectionArgsBackReferences);
        return stringBuilder.toString();
    }

    private long backRefToValue(ContentProviderResult[] backRefs, int numBackRefs, Integer backRefIndex) {
        if (backRefIndex.intValue() < numBackRefs) {
            ContentProviderResult backRef = backRefs[backRefIndex.intValue()];
            if (backRef.uri != null) {
                return ContentUris.parseId(backRef.uri);
            }
            return (long) backRef.count.intValue();
        }
        Log.e(TAG, toString());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("asked for back ref ");
        stringBuilder.append(backRefIndex);
        stringBuilder.append(" but there are only ");
        stringBuilder.append(numBackRefs);
        stringBuilder.append(" back refs");
        throw new ArrayIndexOutOfBoundsException(stringBuilder.toString());
    }

    public int describeContents() {
        return 0;
    }
}
