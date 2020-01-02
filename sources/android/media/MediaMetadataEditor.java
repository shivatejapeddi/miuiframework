package android.media;

import android.graphics.Bitmap;
import android.media.MediaMetadata.Builder;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseIntArray;

@Deprecated
public abstract class MediaMetadataEditor {
    public static final int BITMAP_KEY_ARTWORK = 100;
    public static final int KEY_EDITABLE_MASK = 536870911;
    protected static final SparseIntArray METADATA_KEYS_TYPE = new SparseIntArray(17);
    protected static final int METADATA_TYPE_BITMAP = 2;
    protected static final int METADATA_TYPE_INVALID = -1;
    protected static final int METADATA_TYPE_LONG = 0;
    protected static final int METADATA_TYPE_RATING = 3;
    protected static final int METADATA_TYPE_STRING = 1;
    public static final int RATING_KEY_BY_OTHERS = 101;
    public static final int RATING_KEY_BY_USER = 268435457;
    private static final String TAG = "MediaMetadataEditor";
    protected boolean mApplied = false;
    protected boolean mArtworkChanged = false;
    protected long mEditableKeys;
    protected Bitmap mEditorArtwork;
    protected Bundle mEditorMetadata;
    protected Builder mMetadataBuilder;
    protected boolean mMetadataChanged = false;

    public abstract void apply();

    protected MediaMetadataEditor() {
    }

    public synchronized void clear() {
        if (this.mApplied) {
            Log.e(TAG, "Can't clear a previously applied MediaMetadataEditor");
            return;
        }
        this.mEditorMetadata.clear();
        this.mEditorArtwork = null;
        this.mMetadataBuilder = new Builder();
    }

    /* JADX WARNING: Missing block: B:13:0x003d, code skipped:
            return;
     */
    public synchronized void addEditableKey(int r5) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.mApplied;	 Catch:{ all -> 0x003e }
        if (r0 == 0) goto L_0x000e;
    L_0x0005:
        r0 = "MediaMetadataEditor";
        r1 = "Can't change editable keys of a previously applied MetadataEditor";
        android.util.Log.e(r0, r1);	 Catch:{ all -> 0x003e }
        monitor-exit(r4);
        return;
    L_0x000e:
        r0 = 268435457; // 0x10000001 float:2.5243552E-29 double:1.326247374E-315;
        if (r5 != r0) goto L_0x0021;
    L_0x0013:
        r0 = r4.mEditableKeys;	 Catch:{ all -> 0x003e }
        r2 = 536870911; // 0x1fffffff float:1.0842021E-19 double:2.652494734E-315;
        r2 = r2 & r5;
        r2 = (long) r2;	 Catch:{ all -> 0x003e }
        r0 = r0 | r2;
        r4.mEditableKeys = r0;	 Catch:{ all -> 0x003e }
        r0 = 1;
        r4.mMetadataChanged = r0;	 Catch:{ all -> 0x003e }
        goto L_0x003c;
    L_0x0021:
        r0 = "MediaMetadataEditor";
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x003e }
        r1.<init>();	 Catch:{ all -> 0x003e }
        r2 = "Metadata key ";
        r1.append(r2);	 Catch:{ all -> 0x003e }
        r1.append(r5);	 Catch:{ all -> 0x003e }
        r2 = " cannot be edited";
        r1.append(r2);	 Catch:{ all -> 0x003e }
        r1 = r1.toString();	 Catch:{ all -> 0x003e }
        android.util.Log.e(r0, r1);	 Catch:{ all -> 0x003e }
    L_0x003c:
        monitor-exit(r4);
        return;
    L_0x003e:
        r5 = move-exception;
        monitor-exit(r4);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaMetadataEditor.addEditableKey(int):void");
    }

    /* JADX WARNING: Missing block: B:12:0x001c, code skipped:
            return;
     */
    public synchronized void removeEditableKeys() {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.mApplied;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x000e;
    L_0x0005:
        r0 = "MediaMetadataEditor";
        r1 = "Can't remove all editable keys of a previously applied MetadataEditor";
        android.util.Log.e(r0, r1);	 Catch:{ all -> 0x001d }
        monitor-exit(r4);
        return;
    L_0x000e:
        r0 = r4.mEditableKeys;	 Catch:{ all -> 0x001d }
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 == 0) goto L_0x001b;
    L_0x0016:
        r4.mEditableKeys = r2;	 Catch:{ all -> 0x001d }
        r0 = 1;
        r4.mMetadataChanged = r0;	 Catch:{ all -> 0x001d }
    L_0x001b:
        monitor-exit(r4);
        return;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r4);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaMetadataEditor.removeEditableKeys():void");
    }

    public synchronized int[] getEditableKeys() {
        if (this.mEditableKeys != 268435457) {
            return null;
        }
        return new int[]{268435457};
    }

    public synchronized MediaMetadataEditor putString(int key, String value) throws IllegalArgumentException {
        if (this.mApplied) {
            Log.e(TAG, "Can't edit a previously applied MediaMetadataEditor");
            return this;
        } else if (METADATA_KEYS_TYPE.get(key, -1) == 1) {
            this.mEditorMetadata.putString(String.valueOf(key), value);
            this.mMetadataChanged = true;
            return this;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid type 'String' for key ");
            stringBuilder.append(key);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public synchronized MediaMetadataEditor putLong(int key, long value) throws IllegalArgumentException {
        if (this.mApplied) {
            Log.e(TAG, "Can't edit a previously applied MediaMetadataEditor");
            return this;
        } else if (METADATA_KEYS_TYPE.get(key, -1) == 0) {
            this.mEditorMetadata.putLong(String.valueOf(key), value);
            this.mMetadataChanged = true;
            return this;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid type 'long' for key ");
            stringBuilder.append(key);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public synchronized MediaMetadataEditor putBitmap(int key, Bitmap bitmap) throws IllegalArgumentException {
        if (this.mApplied) {
            Log.e(TAG, "Can't edit a previously applied MediaMetadataEditor");
            return this;
        } else if (key == 100) {
            this.mEditorArtwork = bitmap;
            this.mArtworkChanged = true;
            return this;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid type 'Bitmap' for key ");
            stringBuilder.append(key);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public synchronized MediaMetadataEditor putObject(int key, Object value) throws IllegalArgumentException {
        if (this.mApplied) {
            Log.e(TAG, "Can't edit a previously applied MediaMetadataEditor");
            return this;
        }
        int i = METADATA_KEYS_TYPE.get(key, -1);
        StringBuilder stringBuilder;
        if (i != 0) {
            if (i == 1) {
                if (value != null) {
                    if (!(value instanceof String)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Not a String for key ");
                        stringBuilder.append(key);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                }
                return putString(key, (String) value);
            } else if (i == 2) {
                if (value != null) {
                    if (!(value instanceof Bitmap)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Not a Bitmap for key ");
                        stringBuilder.append(key);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                }
                return putBitmap(key, (Bitmap) value);
            } else if (i == 3) {
                this.mEditorMetadata.putParcelable(String.valueOf(key), (Parcelable) value);
                this.mMetadataChanged = true;
                return this;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid key ");
                stringBuilder.append(key);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        } else if (value instanceof Long) {
            return putLong(key, ((Long) value).longValue());
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Not a non-null Long for key ");
            stringBuilder.append(key);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public synchronized long getLong(int key, long defaultValue) throws IllegalArgumentException {
        if (METADATA_KEYS_TYPE.get(key, -1) == 0) {
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid type 'long' for key ");
            stringBuilder.append(key);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        return this.mEditorMetadata.getLong(String.valueOf(key), defaultValue);
    }

    public synchronized String getString(int key, String defaultValue) throws IllegalArgumentException {
        if (METADATA_KEYS_TYPE.get(key, -1) == 1) {
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid type 'String' for key ");
            stringBuilder.append(key);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        return this.mEditorMetadata.getString(String.valueOf(key), defaultValue);
    }

    public synchronized Bitmap getBitmap(int key, Bitmap defaultValue) throws IllegalArgumentException {
        if (key != 100) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid type 'Bitmap' for key ");
            stringBuilder.append(key);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        return this.mEditorArtwork != null ? this.mEditorArtwork : defaultValue;
    }

    /* JADX WARNING: Missing block: B:25:0x003a, code skipped:
            return r3.mEditorArtwork != null ? r3.mEditorArtwork : r5;
     */
    public synchronized java.lang.Object getObject(int r4, java.lang.Object r5) throws java.lang.IllegalArgumentException {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = METADATA_KEYS_TYPE;	 Catch:{ all -> 0x008a }
        r1 = -1;
        r0 = r0.get(r4, r1);	 Catch:{ all -> 0x008a }
        if (r0 == 0) goto L_0x006c;
    L_0x000a:
        r1 = 1;
        if (r0 == r1) goto L_0x0052;
    L_0x000d:
        r1 = 2;
        if (r0 == r1) goto L_0x002d;
    L_0x0010:
        r1 = 3;
        if (r0 != r1) goto L_0x003b;
    L_0x0013:
        r0 = r3.mEditorMetadata;	 Catch:{ all -> 0x008a }
        r1 = java.lang.String.valueOf(r4);	 Catch:{ all -> 0x008a }
        r0 = r0.containsKey(r1);	 Catch:{ all -> 0x008a }
        if (r0 == 0) goto L_0x002b;
    L_0x001f:
        r0 = r3.mEditorMetadata;	 Catch:{ all -> 0x008a }
        r1 = java.lang.String.valueOf(r4);	 Catch:{ all -> 0x008a }
        r0 = r0.getParcelable(r1);	 Catch:{ all -> 0x008a }
        monitor-exit(r3);
        return r0;
    L_0x002b:
        monitor-exit(r3);
        return r5;
    L_0x002d:
        r0 = 100;
        if (r4 != r0) goto L_0x003b;
    L_0x0031:
        r0 = r3.mEditorArtwork;	 Catch:{ all -> 0x008a }
        if (r0 == 0) goto L_0x0038;
    L_0x0035:
        r0 = r3.mEditorArtwork;	 Catch:{ all -> 0x008a }
        goto L_0x0039;
    L_0x0038:
        r0 = r5;
    L_0x0039:
        monitor-exit(r3);
        return r0;
    L_0x003b:
        r0 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x008a }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008a }
        r1.<init>();	 Catch:{ all -> 0x008a }
        r2 = "Invalid key ";
        r1.append(r2);	 Catch:{ all -> 0x008a }
        r1.append(r4);	 Catch:{ all -> 0x008a }
        r1 = r1.toString();	 Catch:{ all -> 0x008a }
        r0.<init>(r1);	 Catch:{ all -> 0x008a }
        throw r0;	 Catch:{ all -> 0x008a }
    L_0x0052:
        r0 = r3.mEditorMetadata;	 Catch:{ all -> 0x008a }
        r1 = java.lang.String.valueOf(r4);	 Catch:{ all -> 0x008a }
        r0 = r0.containsKey(r1);	 Catch:{ all -> 0x008a }
        if (r0 == 0) goto L_0x006a;
    L_0x005e:
        r0 = r3.mEditorMetadata;	 Catch:{ all -> 0x008a }
        r1 = java.lang.String.valueOf(r4);	 Catch:{ all -> 0x008a }
        r0 = r0.getString(r1);	 Catch:{ all -> 0x008a }
        monitor-exit(r3);
        return r0;
    L_0x006a:
        monitor-exit(r3);
        return r5;
    L_0x006c:
        r0 = r3.mEditorMetadata;	 Catch:{ all -> 0x008a }
        r1 = java.lang.String.valueOf(r4);	 Catch:{ all -> 0x008a }
        r0 = r0.containsKey(r1);	 Catch:{ all -> 0x008a }
        if (r0 == 0) goto L_0x0088;
    L_0x0078:
        r0 = r3.mEditorMetadata;	 Catch:{ all -> 0x008a }
        r1 = java.lang.String.valueOf(r4);	 Catch:{ all -> 0x008a }
        r0 = r0.getLong(r1);	 Catch:{ all -> 0x008a }
        r0 = java.lang.Long.valueOf(r0);	 Catch:{ all -> 0x008a }
        monitor-exit(r3);
        return r0;
    L_0x0088:
        monitor-exit(r3);
        return r5;
    L_0x008a:
        r4 = move-exception;
        monitor-exit(r3);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaMetadataEditor.getObject(int, java.lang.Object):java.lang.Object");
    }

    static {
        METADATA_KEYS_TYPE.put(0, 0);
        METADATA_KEYS_TYPE.put(14, 0);
        METADATA_KEYS_TYPE.put(9, 0);
        METADATA_KEYS_TYPE.put(8, 0);
        METADATA_KEYS_TYPE.put(1, 1);
        METADATA_KEYS_TYPE.put(13, 1);
        METADATA_KEYS_TYPE.put(7, 1);
        METADATA_KEYS_TYPE.put(2, 1);
        METADATA_KEYS_TYPE.put(3, 1);
        METADATA_KEYS_TYPE.put(15, 1);
        METADATA_KEYS_TYPE.put(4, 1);
        METADATA_KEYS_TYPE.put(5, 1);
        METADATA_KEYS_TYPE.put(6, 1);
        METADATA_KEYS_TYPE.put(11, 1);
        METADATA_KEYS_TYPE.put(1000, 1);
        METADATA_KEYS_TYPE.put(100, 2);
        METADATA_KEYS_TYPE.put(101, 3);
        METADATA_KEYS_TYPE.put(268435457, 3);
    }
}
