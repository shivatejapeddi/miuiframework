package android.media;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
import java.util.Set;

public final class MediaMetadata implements Parcelable {
    public static final Creator<MediaMetadata> CREATOR = new Creator<MediaMetadata>() {
        public MediaMetadata createFromParcel(Parcel in) {
            return new MediaMetadata(in, null);
        }

        public MediaMetadata[] newArray(int size) {
            return new MediaMetadata[size];
        }
    };
    private static final SparseArray<String> EDITOR_KEY_MAPPING = new SparseArray();
    private static final ArrayMap<String, Integer> METADATA_KEYS_TYPE = new ArrayMap();
    public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
    public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
    public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
    public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
    public static final String METADATA_KEY_ART = "android.media.metadata.ART";
    public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
    public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
    public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
    public static final String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";
    public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
    public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
    public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
    public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
    public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
    public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
    public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
    public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
    public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
    public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
    public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
    public static final String METADATA_KEY_LYRIC = "android.media.metadata.LYRIC";
    public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
    public static final String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";
    public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
    public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
    public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
    public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
    public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
    public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
    public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
    private static final int METADATA_TYPE_BITMAP = 2;
    private static final int METADATA_TYPE_INVALID = -1;
    private static final int METADATA_TYPE_LONG = 0;
    private static final int METADATA_TYPE_RATING = 3;
    private static final int METADATA_TYPE_TEXT = 1;
    private static final String[] PREFERRED_BITMAP_ORDER;
    private static final String[] PREFERRED_DESCRIPTION_ORDER = new String[]{METADATA_KEY_TITLE, METADATA_KEY_ARTIST, METADATA_KEY_ALBUM, METADATA_KEY_ALBUM_ARTIST, METADATA_KEY_WRITER, METADATA_KEY_AUTHOR, METADATA_KEY_COMPOSER};
    private static final String[] PREFERRED_URI_ORDER;
    private static final String TAG = "MediaMetadata";
    private final Bundle mBundle;
    private MediaDescription mDescription;

    @Retention(RetentionPolicy.SOURCE)
    public @interface BitmapKey {
    }

    public static final class Builder {
        private final Bundle mBundle;

        public Builder() {
            this.mBundle = new Bundle();
        }

        public Builder(MediaMetadata source) {
            this.mBundle = new Bundle(source.mBundle);
        }

        public Builder(MediaMetadata source, int maxBitmapSize) {
            this(source);
            for (String key : this.mBundle.keySet()) {
                Bitmap value = this.mBundle.get(key);
                if (value != null && (value instanceof Bitmap)) {
                    Bitmap bmp = value;
                    if (bmp.getHeight() > maxBitmapSize || bmp.getWidth() > maxBitmapSize) {
                        putBitmap(key, scaleBitmap(bmp, maxBitmapSize));
                    }
                }
            }
        }

        public Builder putText(String key, CharSequence value) {
            if (!MediaMetadata.METADATA_KEYS_TYPE.containsKey(key) || ((Integer) MediaMetadata.METADATA_KEYS_TYPE.get(key)).intValue() == 1) {
                this.mBundle.putCharSequence(key, value);
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The ");
            stringBuilder.append(key);
            stringBuilder.append(" key cannot be used to put a CharSequence");
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder putString(String key, String value) {
            if (!MediaMetadata.METADATA_KEYS_TYPE.containsKey(key) || ((Integer) MediaMetadata.METADATA_KEYS_TYPE.get(key)).intValue() == 1) {
                this.mBundle.putCharSequence(key, value);
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The ");
            stringBuilder.append(key);
            stringBuilder.append(" key cannot be used to put a String");
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder putLong(String key, long value) {
            if (!MediaMetadata.METADATA_KEYS_TYPE.containsKey(key) || ((Integer) MediaMetadata.METADATA_KEYS_TYPE.get(key)).intValue() == 0) {
                this.mBundle.putLong(key, value);
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The ");
            stringBuilder.append(key);
            stringBuilder.append(" key cannot be used to put a long");
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder putRating(String key, Rating value) {
            if (!MediaMetadata.METADATA_KEYS_TYPE.containsKey(key) || ((Integer) MediaMetadata.METADATA_KEYS_TYPE.get(key)).intValue() == 3) {
                this.mBundle.putParcelable(key, value);
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The ");
            stringBuilder.append(key);
            stringBuilder.append(" key cannot be used to put a Rating");
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder putBitmap(String key, Bitmap value) {
            if (!MediaMetadata.METADATA_KEYS_TYPE.containsKey(key) || ((Integer) MediaMetadata.METADATA_KEYS_TYPE.get(key)).intValue() == 2) {
                this.mBundle.putParcelable(key, value);
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The ");
            stringBuilder.append(key);
            stringBuilder.append(" key cannot be used to put a Bitmap");
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public MediaMetadata build() {
            return new MediaMetadata(this.mBundle, null);
        }

        private Bitmap scaleBitmap(Bitmap bmp, int maxSize) {
            float maxSizeF = (float) maxSize;
            float scale = Math.min(maxSizeF / ((float) bmp.getWidth()), maxSizeF / ((float) bmp.getHeight()));
            return Bitmap.createScaledBitmap(bmp, (int) (((float) bmp.getWidth()) * scale), (int) (((float) bmp.getHeight()) * scale), true);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LongKey {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RatingKey {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TextKey {
    }

    static {
        String str = METADATA_KEY_ALBUM_ART;
        String str2 = METADATA_KEY_DISPLAY_ICON;
        String str3 = METADATA_KEY_ART;
        PREFERRED_BITMAP_ORDER = new String[]{str2, str3, str};
        String str4 = METADATA_KEY_ALBUM_ART_URI;
        String str5 = METADATA_KEY_ART_URI;
        String str6 = METADATA_KEY_DISPLAY_ICON_URI;
        PREFERRED_URI_ORDER = new String[]{str6, str5, str4};
        ArrayMap arrayMap = METADATA_KEYS_TYPE;
        Integer valueOf = Integer.valueOf(1);
        String str7 = METADATA_KEY_TITLE;
        arrayMap.put(str7, valueOf);
        arrayMap = METADATA_KEYS_TYPE;
        String str8 = METADATA_KEY_ARTIST;
        arrayMap.put(str8, valueOf);
        arrayMap = METADATA_KEYS_TYPE;
        Integer valueOf2 = Integer.valueOf(0);
        String str9 = METADATA_KEY_DURATION;
        arrayMap.put(str9, valueOf2);
        arrayMap = METADATA_KEYS_TYPE;
        String str10 = METADATA_KEY_ALBUM;
        arrayMap.put(str10, valueOf);
        arrayMap = METADATA_KEYS_TYPE;
        String str11 = METADATA_KEY_AUTHOR;
        arrayMap.put(str11, valueOf);
        METADATA_KEYS_TYPE.put(METADATA_KEY_WRITER, valueOf);
        METADATA_KEYS_TYPE.put(METADATA_KEY_COMPOSER, valueOf);
        METADATA_KEYS_TYPE.put(METADATA_KEY_COMPILATION, valueOf);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DATE, valueOf);
        METADATA_KEYS_TYPE.put(METADATA_KEY_YEAR, valueOf2);
        METADATA_KEYS_TYPE.put(METADATA_KEY_GENRE, valueOf);
        METADATA_KEYS_TYPE.put(METADATA_KEY_TRACK_NUMBER, valueOf2);
        METADATA_KEYS_TYPE.put(METADATA_KEY_NUM_TRACKS, valueOf2);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISC_NUMBER, valueOf2);
        METADATA_KEYS_TYPE.put(METADATA_KEY_ALBUM_ARTIST, valueOf);
        arrayMap = METADATA_KEYS_TYPE;
        Integer valueOf3 = Integer.valueOf(2);
        arrayMap.put(str3, valueOf3);
        METADATA_KEYS_TYPE.put(str5, valueOf);
        METADATA_KEYS_TYPE.put(str, valueOf3);
        METADATA_KEYS_TYPE.put(str4, valueOf);
        ArrayMap arrayMap2 = METADATA_KEYS_TYPE;
        Integer valueOf4 = Integer.valueOf(3);
        arrayMap2.put(METADATA_KEY_USER_RATING, valueOf4);
        METADATA_KEYS_TYPE.put(METADATA_KEY_RATING, valueOf4);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_TITLE, valueOf);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_SUBTITLE, valueOf);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DISPLAY_DESCRIPTION, valueOf);
        METADATA_KEYS_TYPE.put(str2, valueOf3);
        METADATA_KEYS_TYPE.put(str6, valueOf);
        METADATA_KEYS_TYPE.put(METADATA_KEY_BT_FOLDER_TYPE, valueOf2);
        METADATA_KEYS_TYPE.put(METADATA_KEY_MEDIA_ID, valueOf);
        METADATA_KEYS_TYPE.put(METADATA_KEY_MEDIA_URI, valueOf);
        METADATA_KEYS_TYPE.put(METADATA_KEY_LYRIC, valueOf);
        EDITOR_KEY_MAPPING.put(100, str3);
        EDITOR_KEY_MAPPING.put(101, METADATA_KEY_RATING);
        EDITOR_KEY_MAPPING.put(268435457, METADATA_KEY_USER_RATING);
        EDITOR_KEY_MAPPING.put(1, str10);
        EDITOR_KEY_MAPPING.put(13, METADATA_KEY_ALBUM_ARTIST);
        EDITOR_KEY_MAPPING.put(2, str8);
        EDITOR_KEY_MAPPING.put(3, str11);
        EDITOR_KEY_MAPPING.put(0, METADATA_KEY_TRACK_NUMBER);
        EDITOR_KEY_MAPPING.put(4, METADATA_KEY_COMPOSER);
        EDITOR_KEY_MAPPING.put(15, METADATA_KEY_COMPILATION);
        EDITOR_KEY_MAPPING.put(5, METADATA_KEY_DATE);
        EDITOR_KEY_MAPPING.put(14, METADATA_KEY_DISC_NUMBER);
        EDITOR_KEY_MAPPING.put(9, str9);
        EDITOR_KEY_MAPPING.put(6, METADATA_KEY_GENRE);
        EDITOR_KEY_MAPPING.put(10, METADATA_KEY_NUM_TRACKS);
        EDITOR_KEY_MAPPING.put(7, str7);
        EDITOR_KEY_MAPPING.put(11, METADATA_KEY_WRITER);
        EDITOR_KEY_MAPPING.put(8, METADATA_KEY_YEAR);
        EDITOR_KEY_MAPPING.put(1000, METADATA_KEY_LYRIC);
    }

    private MediaMetadata(Bundle bundle) {
        this.mBundle = new Bundle(bundle);
    }

    private MediaMetadata(Parcel in) {
        this.mBundle = in.readBundle();
    }

    public boolean containsKey(String key) {
        return this.mBundle.containsKey(key);
    }

    public CharSequence getText(String key) {
        return this.mBundle.getCharSequence(key);
    }

    public String getString(String key) {
        CharSequence text = getText(key);
        if (text != null) {
            return text.toString();
        }
        return null;
    }

    public long getLong(String key) {
        return this.mBundle.getLong(key, 0);
    }

    public Rating getRating(String key) {
        try {
            return (Rating) this.mBundle.getParcelable(key);
        } catch (Exception e) {
            Log.w(TAG, "Failed to retrieve a key as Rating.", e);
            return null;
        }
    }

    public Bitmap getBitmap(String key) {
        try {
            return (Bitmap) this.mBundle.getParcelable(key);
        } catch (Exception e) {
            Log.w(TAG, "Failed to retrieve a key as Bitmap.", e);
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mBundle);
    }

    public int size() {
        return this.mBundle.size();
    }

    public Set<String> keySet() {
        return this.mBundle.keySet();
    }

    public MediaDescription getDescription() {
        MediaDescription mediaDescription = this.mDescription;
        if (mediaDescription != null) {
            return mediaDescription;
        }
        int textIndex;
        String[] strArr;
        String next;
        String mediaId = getString(METADATA_KEY_MEDIA_ID);
        CharSequence[] text = new CharSequence[3];
        Bitmap icon = null;
        Uri iconUri = null;
        CharSequence displayText = getText(METADATA_KEY_DISPLAY_TITLE);
        if (TextUtils.isEmpty(displayText)) {
            textIndex = 0;
            int keyIndex = 0;
            while (textIndex < text.length) {
                String[] strArr2 = PREFERRED_DESCRIPTION_ORDER;
                if (keyIndex >= strArr2.length) {
                    break;
                }
                int keyIndex2 = keyIndex + 1;
                CharSequence next2 = getText(strArr2[keyIndex]);
                if (!TextUtils.isEmpty(next2)) {
                    int textIndex2 = textIndex + 1;
                    text[textIndex] = next2;
                    textIndex = textIndex2;
                }
                keyIndex = keyIndex2;
            }
        } else {
            text[0] = displayText;
            text[1] = getText(METADATA_KEY_DISPLAY_SUBTITLE);
            text[2] = getText(METADATA_KEY_DISPLAY_DESCRIPTION);
        }
        textIndex = 0;
        while (true) {
            strArr = PREFERRED_BITMAP_ORDER;
            if (textIndex >= strArr.length) {
                break;
            }
            Bitmap next3 = getBitmap(strArr[textIndex]);
            if (next3 != null) {
                icon = next3;
                break;
            }
            textIndex++;
        }
        textIndex = 0;
        while (true) {
            strArr = PREFERRED_URI_ORDER;
            if (textIndex >= strArr.length) {
                break;
            }
            next = getString(strArr[textIndex]);
            if (!TextUtils.isEmpty(next)) {
                iconUri = Uri.parse(next);
                break;
            }
            textIndex++;
        }
        Uri mediaUri = null;
        next = getString(METADATA_KEY_MEDIA_URI);
        if (!TextUtils.isEmpty(next)) {
            mediaUri = Uri.parse(next);
        }
        android.media.MediaDescription.Builder bob = new android.media.MediaDescription.Builder();
        bob.setMediaId(mediaId);
        bob.setTitle(text[0]);
        bob.setSubtitle(text[1]);
        bob.setDescription(text[2]);
        bob.setIconBitmap(icon);
        bob.setIconUri(iconUri);
        bob.setMediaUri(mediaUri);
        Bundle bundle = this.mBundle;
        String str = METADATA_KEY_BT_FOLDER_TYPE;
        if (bundle.containsKey(str)) {
            bundle = new Bundle();
            bundle.putLong(MediaDescription.EXTRA_BT_FOLDER_TYPE, getLong(str));
            bob.setExtras(bundle);
        }
        this.mDescription = bob.build();
        return this.mDescription;
    }

    @UnsupportedAppUsage
    public static String getKeyFromMetadataEditorKey(int editorKey) {
        return (String) EDITOR_KEY_MAPPING.get(editorKey, null);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MediaMetadata)) {
            return false;
        }
        MediaMetadata m = (MediaMetadata) o;
        for (int i = 0; i < METADATA_KEYS_TYPE.size(); i++) {
            String key = (String) METADATA_KEYS_TYPE.keyAt(i);
            int intValue = ((Integer) METADATA_KEYS_TYPE.valueAt(i)).intValue();
            if (intValue != 0) {
                if (intValue == 1 && !Objects.equals(getString(key), m.getString(key))) {
                    return false;
                }
            } else if (getLong(key) != m.getLong(key)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hashCode = 17;
        for (int i = 0; i < METADATA_KEYS_TYPE.size(); i++) {
            String key = (String) METADATA_KEYS_TYPE.keyAt(i);
            int intValue = ((Integer) METADATA_KEYS_TYPE.valueAt(i)).intValue();
            if (intValue == 0) {
                hashCode = (hashCode * 31) + Long.hashCode(getLong(key));
            } else if (intValue == 1) {
                hashCode = (hashCode * 31) + Objects.hash(new Object[]{getString(key)});
            }
        }
        return hashCode;
    }
}
