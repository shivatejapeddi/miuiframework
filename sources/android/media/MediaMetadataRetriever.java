package android.media;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.IBinder;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MediaMetadataRetriever implements AutoCloseable {
    private static final int EMBEDDED_PICTURE_TYPE_ANY = 65535;
    public static final int METADATA_KEY_ALBUM = 1;
    public static final int METADATA_KEY_ALBUMARTIST = 13;
    public static final int METADATA_KEY_ARTIST = 2;
    public static final int METADATA_KEY_AUTHOR = 3;
    public static final int METADATA_KEY_BITRATE = 20;
    public static final int METADATA_KEY_BITS_PER_SAMPLE = 39;
    public static final int METADATA_KEY_CAPTURE_FRAMERATE = 25;
    public static final int METADATA_KEY_CD_TRACK_NUMBER = 0;
    public static final int METADATA_KEY_COLOR_RANGE = 37;
    public static final int METADATA_KEY_COLOR_STANDARD = 35;
    public static final int METADATA_KEY_COLOR_TRANSFER = 36;
    public static final int METADATA_KEY_COMPILATION = 15;
    public static final int METADATA_KEY_COMPOSER = 4;
    public static final int METADATA_KEY_DATE = 5;
    public static final int METADATA_KEY_DISC_NUMBER = 14;
    public static final int METADATA_KEY_DURATION = 9;
    public static final int METADATA_KEY_EXIF_LENGTH = 34;
    public static final int METADATA_KEY_EXIF_OFFSET = 33;
    public static final int METADATA_KEY_GENRE = 6;
    public static final int METADATA_KEY_HAS_AUDIO = 16;
    public static final int METADATA_KEY_HAS_IMAGE = 26;
    public static final int METADATA_KEY_HAS_VIDEO = 17;
    public static final int METADATA_KEY_IMAGE_COUNT = 27;
    public static final int METADATA_KEY_IMAGE_HEIGHT = 30;
    public static final int METADATA_KEY_IMAGE_PRIMARY = 28;
    public static final int METADATA_KEY_IMAGE_ROTATION = 31;
    public static final int METADATA_KEY_IMAGE_WIDTH = 29;
    public static final int METADATA_KEY_IS_DRM = 22;
    public static final int METADATA_KEY_LOCATION = 23;
    public static final int METADATA_KEY_LYRIC = 1000;
    public static final int METADATA_KEY_MIMETYPE = 12;
    public static final int METADATA_KEY_NUM_TRACKS = 10;
    public static final int METADATA_KEY_SAMPLERATE = 38;
    public static final int METADATA_KEY_TIMED_TEXT_LANGUAGES = 21;
    public static final int METADATA_KEY_TITLE = 7;
    public static final int METADATA_KEY_VIDEO_FRAME_COUNT = 32;
    public static final int METADATA_KEY_VIDEO_HEIGHT = 19;
    public static final int METADATA_KEY_VIDEO_ROTATION = 24;
    public static final int METADATA_KEY_VIDEO_WIDTH = 18;
    public static final int METADATA_KEY_WRITER = 11;
    public static final int METADATA_KEY_YEAR = 8;
    public static final int OPTION_CLOSEST = 3;
    public static final int OPTION_CLOSEST_SYNC = 2;
    public static final int OPTION_NEXT_SYNC = 1;
    public static final int OPTION_PREVIOUS_SYNC = 0;
    private long mNativeContext;

    public static final class BitmapParams {
        private Config inPreferredConfig = Config.ARGB_8888;
        private Config outActualConfig = Config.ARGB_8888;

        public void setPreferredConfig(Config config) {
            if (config != null) {
                this.inPreferredConfig = config;
                return;
            }
            throw new IllegalArgumentException("preferred config can't be null");
        }

        public Config getPreferredConfig() {
            return this.inPreferredConfig;
        }

        public Config getActualConfig() {
            return this.outActualConfig;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Option {
    }

    private native List<Bitmap> _getFrameAtIndex(int i, int i2, BitmapParams bitmapParams);

    private native Bitmap _getFrameAtTime(long j, int i, int i2, int i3);

    private native Bitmap _getImageAtIndex(int i, BitmapParams bitmapParams);

    private native void _setDataSource(MediaDataSource mediaDataSource) throws IllegalArgumentException;

    private native void _setDataSource(IBinder iBinder, String str, String[] strArr, String[] strArr2) throws IllegalArgumentException;

    @UnsupportedAppUsage
    private native byte[] getEmbeddedPicture(int i);

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final native void native_finalize();

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static native void native_init();

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private native void native_setup();

    public native String extractMetadata(int i);

    public native Bitmap getThumbnailImageAtIndex(int i, BitmapParams bitmapParams, int i2, int i3);

    public native void release();

    public native void setDataSource(FileDescriptor fileDescriptor, long j, long j2) throws IllegalArgumentException;

    static {
        System.loadLibrary("media_jni");
        native_init();
    }

    public MediaMetadataRetriever() {
        native_setup();
    }

    /* JADX WARNING: Missing block: B:13:?, code skipped:
            r0.close();
     */
    public void setDataSource(java.lang.String r8) throws java.lang.IllegalArgumentException {
        /*
        r7 = this;
        if (r8 == 0) goto L_0x0035;
    L_0x0002:
        r0 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x002e, IOException -> 0x0027 }
        r0.<init>(r8);	 Catch:{ FileNotFoundException -> 0x002e, IOException -> 0x0027 }
        r2 = r0.getFD();	 Catch:{ all -> 0x001b }
        r3 = 0;
        r5 = 576460752303423487; // 0x7ffffffffffffff float:NaN double:3.7857669957336787E-270;
        r1 = r7;
        r1.setDataSource(r2, r3, r5);	 Catch:{ all -> 0x001b }
        r0.close();	 Catch:{ FileNotFoundException -> 0x002e, IOException -> 0x0027 }
        return;
    L_0x001b:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x001d }
    L_0x001d:
        r2 = move-exception;
        r0.close();	 Catch:{ all -> 0x0022 }
        goto L_0x0026;
    L_0x0022:
        r3 = move-exception;
        r1.addSuppressed(r3);	 Catch:{ FileNotFoundException -> 0x002e, IOException -> 0x0027 }
    L_0x0026:
        throw r2;	 Catch:{ FileNotFoundException -> 0x002e, IOException -> 0x0027 }
    L_0x0027:
        r0 = move-exception;
        r1 = new java.lang.IllegalArgumentException;
        r1.<init>();
        throw r1;
    L_0x002e:
        r0 = move-exception;
        r1 = new java.lang.IllegalArgumentException;
        r1.<init>();
        throw r1;
    L_0x0035:
        r0 = new java.lang.IllegalArgumentException;
        r0.<init>();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaMetadataRetriever.setDataSource(java.lang.String):void");
    }

    public void setDataSource(String uri, Map<String, String> headers) throws IllegalArgumentException {
        int i = 0;
        String[] keys = new String[headers.size()];
        String[] values = new String[headers.size()];
        for (Entry<String, String> entry : headers.entrySet()) {
            keys[i] = (String) entry.getKey();
            values[i] = (String) entry.getValue();
            i++;
        }
        _setDataSource(MediaHTTPService.createHttpServiceBinderIfNecessary(uri), uri, keys, values);
    }

    public void setDataSource(FileDescriptor fd) throws IllegalArgumentException {
        setDataSource(fd, 0, 576460752303423487L);
    }

    public void setDataSource(Context context, Uri uri) throws IllegalArgumentException, SecurityException {
        if (uri != null) {
            String scheme = uri.getScheme();
            if (scheme == null || scheme.equals(ContentResolver.SCHEME_FILE)) {
                setDataSource(uri.getPath());
                return;
            }
            AssetFileDescriptor fd = null;
            try {
                fd = context.getContentResolver().openAssetFileDescriptor(uri, "r");
                if (fd != null) {
                    FileDescriptor descriptor = fd.getFileDescriptor();
                    if (descriptor.valid()) {
                        if (fd.getDeclaredLength() < 0) {
                            setDataSource(descriptor);
                        } else {
                            setDataSource(descriptor, fd.getStartOffset(), fd.getDeclaredLength());
                        }
                        try {
                            fd.close();
                        } catch (IOException e) {
                        }
                        return;
                    }
                    throw new IllegalArgumentException();
                }
                throw new IllegalArgumentException();
            } catch (FileNotFoundException e2) {
                throw new IllegalArgumentException();
            } catch (SecurityException e3) {
                if (fd != null) {
                    try {
                        fd.close();
                    } catch (IOException e4) {
                    }
                }
                setDataSource(uri.toString());
            } catch (Throwable th) {
                if (fd != null) {
                    try {
                        fd.close();
                    } catch (IOException e5) {
                    }
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setDataSource(MediaDataSource dataSource) throws IllegalArgumentException {
        _setDataSource(dataSource);
    }

    public Bitmap getFrameAtTime(long timeUs, int option) {
        if (option >= 0 && option <= 3) {
            return _getFrameAtTime(timeUs, option, -1, -1);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported option: ");
        stringBuilder.append(option);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public Bitmap getScaledFrameAtTime(long timeUs, int option, int dstWidth, int dstHeight) {
        StringBuilder stringBuilder;
        if (option < 0 || option > 3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported option: ");
            stringBuilder.append(option);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (dstWidth <= 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid width: ");
            stringBuilder.append(dstWidth);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (dstHeight > 0) {
            return _getFrameAtTime(timeUs, option, dstWidth, dstHeight);
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid height: ");
            stringBuilder.append(dstHeight);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public Bitmap getFrameAtTime(long timeUs) {
        return getFrameAtTime(timeUs, 2);
    }

    public Bitmap getFrameAtTime() {
        return _getFrameAtTime(-1, 2, -1, -1);
    }

    public Bitmap getFrameAtIndex(int frameIndex, BitmapParams params) {
        return (Bitmap) getFramesAtIndex(frameIndex, 1, params).get(0);
    }

    public Bitmap getFrameAtIndex(int frameIndex) {
        return (Bitmap) getFramesAtIndex(frameIndex, 1).get(0);
    }

    public List<Bitmap> getFramesAtIndex(int frameIndex, int numFrames, BitmapParams params) {
        return getFramesAtIndexInternal(frameIndex, numFrames, params);
    }

    public List<Bitmap> getFramesAtIndex(int frameIndex, int numFrames) {
        return getFramesAtIndexInternal(frameIndex, numFrames, null);
    }

    private List<Bitmap> getFramesAtIndexInternal(int frameIndex, int numFrames, BitmapParams params) {
        if ("yes".equals(extractMetadata(17))) {
            int frameCount = Integer.parseInt(extractMetadata(32));
            if (frameIndex >= 0 && numFrames >= 1 && frameIndex < frameCount && frameIndex <= frameCount - numFrames) {
                return _getFrameAtIndex(frameIndex, numFrames, params);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid frameIndex or numFrames: ");
            stringBuilder.append(frameIndex);
            stringBuilder.append(", ");
            stringBuilder.append(numFrames);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new IllegalStateException("Does not contail video or image sequences");
    }

    public Bitmap getImageAtIndex(int imageIndex, BitmapParams params) {
        return getImageAtIndexInternal(imageIndex, params);
    }

    public Bitmap getImageAtIndex(int imageIndex) {
        return getImageAtIndexInternal(imageIndex, null);
    }

    public Bitmap getPrimaryImage(BitmapParams params) {
        return getImageAtIndexInternal(-1, params);
    }

    public Bitmap getPrimaryImage() {
        return getImageAtIndexInternal(-1, null);
    }

    private Bitmap getImageAtIndexInternal(int imageIndex, BitmapParams params) {
        if ("yes".equals(extractMetadata(26))) {
            String imageCount = extractMetadata(27);
            if (imageIndex < Integer.parseInt(imageCount)) {
                return _getImageAtIndex(imageIndex, params);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid image index: ");
            stringBuilder.append(imageCount);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new IllegalStateException("Does not contail still images");
    }

    public byte[] getEmbeddedPicture() {
        return getEmbeddedPicture(65535);
    }

    public void close() {
        release();
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            native_finalize();
        } finally {
            super.finalize();
        }
    }
}
