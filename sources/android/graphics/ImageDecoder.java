package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.content.res.Resources;
import android.graphics.ColorSpace.Named;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Size;
import android.util.TypedValue;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import libcore.io.IoUtils;

public final class ImageDecoder implements AutoCloseable {
    public static final int ALLOCATOR_DEFAULT = 0;
    public static final int ALLOCATOR_HARDWARE = 3;
    public static final int ALLOCATOR_SHARED_MEMORY = 2;
    public static final int ALLOCATOR_SOFTWARE = 1;
    @Deprecated
    public static final int ERROR_SOURCE_ERROR = 3;
    @Deprecated
    public static final int ERROR_SOURCE_EXCEPTION = 1;
    @Deprecated
    public static final int ERROR_SOURCE_INCOMPLETE = 2;
    public static final int MEMORY_POLICY_DEFAULT = 1;
    public static final int MEMORY_POLICY_LOW_RAM = 0;
    public static int sApiLevel;
    private int mAllocator = 0;
    private final boolean mAnimated;
    private AssetFileDescriptor mAssetFd;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final AtomicBoolean mClosed = new AtomicBoolean();
    private boolean mConserveMemory = false;
    private Rect mCropRect;
    private boolean mDecodeAsAlphaMask = false;
    private ColorSpace mDesiredColorSpace = null;
    private int mDesiredHeight;
    private int mDesiredWidth;
    private final int mHeight;
    private InputStream mInputStream;
    private final boolean mIsNinePatch;
    private boolean mMutable = false;
    private long mNativePtr;
    private OnPartialImageListener mOnPartialImageListener;
    private Rect mOutPaddingRect;
    private boolean mOwnsInputStream;
    private PostProcessor mPostProcessor;
    private Source mSource;
    private byte[] mTempStorage;
    private boolean mUnpremultipliedRequired = false;
    private final int mWidth;

    public interface OnHeaderDecodedListener {
        void onHeaderDecoded(ImageDecoder imageDecoder, ImageInfo imageInfo, Source source);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Allocator {
    }

    public static abstract class Source {
        public abstract ImageDecoder createImageDecoder() throws IOException;

        private Source() {
        }

        /* Access modifiers changed, original: 0000 */
        public Resources getResources() {
            return null;
        }

        /* Access modifiers changed, original: 0000 */
        public int getDensity() {
            return 0;
        }

        /* Access modifiers changed, original: final */
        public final int computeDstDensity() {
            Resources res = getResources();
            if (res == null) {
                return Bitmap.getDefaultDensity();
            }
            return res.getDisplayMetrics().densityDpi;
        }
    }

    public static class AssetInputStreamSource extends Source {
        private AssetInputStream mAssetInputStream;
        private final int mDensity;
        private final Resources mResources;

        public AssetInputStreamSource(AssetInputStream ais, Resources res, TypedValue value) {
            super();
            this.mAssetInputStream = ais;
            this.mResources = res;
            if (value.density == 0) {
                this.mDensity = 160;
            } else if (value.density != 65535) {
                this.mDensity = value.density;
            } else {
                this.mDensity = 0;
            }
        }

        public Resources getResources() {
            return this.mResources;
        }

        public int getDensity() {
            return this.mDensity;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            ImageDecoder access$500;
            synchronized (this) {
                if (this.mAssetInputStream != null) {
                    AssetInputStream ais = this.mAssetInputStream;
                    this.mAssetInputStream = null;
                    access$500 = ImageDecoder.createFromAsset(ais, this);
                } else {
                    throw new IOException("Cannot reuse AssetInputStreamSource");
                }
            }
            return access$500;
        }
    }

    private static class AssetSource extends Source {
        private final AssetManager mAssets;
        private final String mFileName;

        AssetSource(AssetManager assets, String fileName) {
            super();
            this.mAssets = assets;
            this.mFileName = fileName;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            return ImageDecoder.createFromAsset((AssetInputStream) this.mAssets.open(this.mFileName), this);
        }
    }

    private static class ByteArraySource extends Source {
        private final byte[] mData;
        private final int mLength;
        private final int mOffset;

        ByteArraySource(byte[] data, int offset, int length) {
            super();
            this.mData = data;
            this.mOffset = offset;
            this.mLength = length;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            return ImageDecoder.nCreate(this.mData, this.mOffset, this.mLength, (Source) this);
        }
    }

    private static class ByteBufferSource extends Source {
        private final ByteBuffer mBuffer;

        ByteBufferSource(ByteBuffer buffer) {
            super();
            this.mBuffer = buffer;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            if (this.mBuffer.isDirect() || !this.mBuffer.hasArray()) {
                ByteBuffer buffer = this.mBuffer.slice();
                return ImageDecoder.nCreate(buffer, buffer.position(), buffer.limit(), (Source) this);
            }
            return ImageDecoder.nCreate(this.mBuffer.array(), this.mBuffer.arrayOffset() + this.mBuffer.position(), this.mBuffer.limit() - this.mBuffer.position(), (Source) this);
        }
    }

    private static class CallableSource extends Source {
        private final Callable<AssetFileDescriptor> mCallable;

        CallableSource(Callable<AssetFileDescriptor> callable) {
            super();
            this.mCallable = callable;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            try {
                return ImageDecoder.createFromAssetFileDescriptor((AssetFileDescriptor) this.mCallable.call(), this);
            } catch (Exception e) {
                if (e instanceof IOException) {
                    throw ((IOException) e);
                }
                throw new IOException(e);
            }
        }
    }

    private static class ContentResolverSource extends Source {
        private final ContentResolver mResolver;
        private final Resources mResources;
        private final Uri mUri;

        ContentResolverSource(ContentResolver resolver, Uri uri, Resources res) {
            super();
            this.mResolver = resolver;
            this.mUri = uri;
            this.mResources = res;
        }

        /* Access modifiers changed, original: 0000 */
        public Resources getResources() {
            return this.mResources;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            try {
                AssetFileDescriptor assetFd;
                if (this.mUri.getScheme() == "content") {
                    assetFd = this.mResolver.openTypedAssetFileDescriptor(this.mUri, "image/*", null);
                } else {
                    assetFd = this.mResolver.openAssetFileDescriptor(this.mUri, "r");
                }
                return ImageDecoder.createFromAssetFileDescriptor(assetFd, this);
            } catch (FileNotFoundException e) {
                InputStream is = this.mResolver.openInputStream(this.mUri);
                if (is != null) {
                    return ImageDecoder.createFromStream(is, true, this);
                }
                throw new FileNotFoundException(this.mUri.toString());
            }
        }
    }

    public static final class DecodeException extends IOException {
        public static final int SOURCE_EXCEPTION = 1;
        public static final int SOURCE_INCOMPLETE = 2;
        public static final int SOURCE_MALFORMED_DATA = 3;
        final int mError;
        final Source mSource;

        @Retention(RetentionPolicy.SOURCE)
        public @interface Error {
        }

        DecodeException(int error, Throwable cause, Source source) {
            super(errorMessage(error, cause), cause);
            this.mError = error;
            this.mSource = source;
        }

        DecodeException(int error, String msg, Throwable cause, Source source) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(msg);
            stringBuilder.append(errorMessage(error, cause));
            super(stringBuilder.toString(), cause);
            this.mError = error;
            this.mSource = source;
        }

        public int getError() {
            return this.mError;
        }

        public Source getSource() {
            return this.mSource;
        }

        private static String errorMessage(int error, Throwable cause) {
            if (error == 1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Exception in input: ");
                stringBuilder.append(cause);
                return stringBuilder.toString();
            } else if (error == 2) {
                return "Input was incomplete.";
            } else {
                if (error != 3) {
                    return "";
                }
                return "Input contained an error.";
            }
        }
    }

    private static class FileSource extends Source {
        private final File mFile;

        FileSource(File file) {
            super();
            this.mFile = file;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            return ImageDecoder.createFromFile(this.mFile, this);
        }
    }

    public static class ImageInfo {
        private ImageDecoder mDecoder;
        private final Size mSize;

        private ImageInfo(ImageDecoder decoder) {
            this.mSize = new Size(decoder.mWidth, decoder.mHeight);
            this.mDecoder = decoder;
        }

        public Size getSize() {
            return this.mSize;
        }

        public String getMimeType() {
            return this.mDecoder.getMimeType();
        }

        public boolean isAnimated() {
            return this.mDecoder.mAnimated;
        }

        public ColorSpace getColorSpace() {
            return this.mDecoder.getColorSpace();
        }
    }

    @Deprecated
    public static class IncompleteException extends IOException {
    }

    private static class InputStreamSource extends Source {
        final int mInputDensity;
        InputStream mInputStream;
        final Resources mResources;

        InputStreamSource(Resources res, InputStream is, int inputDensity) {
            super();
            if (is != null) {
                this.mResources = res;
                this.mInputStream = is;
                this.mInputDensity = inputDensity;
                return;
            }
            throw new IllegalArgumentException("The InputStream cannot be null");
        }

        public Resources getResources() {
            return this.mResources;
        }

        public int getDensity() {
            return this.mInputDensity;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            ImageDecoder access$300;
            synchronized (this) {
                if (this.mInputStream != null) {
                    InputStream is = this.mInputStream;
                    this.mInputStream = null;
                    access$300 = ImageDecoder.createFromStream(is, false, this);
                } else {
                    throw new IOException("Cannot reuse InputStreamSource");
                }
            }
            return access$300;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MemoryPolicy {
    }

    public interface OnPartialImageListener {
        boolean onPartialImage(DecodeException decodeException);
    }

    private static class ResourceSource extends Source {
        private Object mLock = new Object();
        int mResDensity;
        final int mResId;
        final Resources mResources;

        ResourceSource(Resources res, int resId) {
            super();
            this.mResources = res;
            this.mResId = resId;
            this.mResDensity = 0;
        }

        public Resources getResources() {
            return this.mResources;
        }

        public int getDensity() {
            int i;
            synchronized (this.mLock) {
                i = this.mResDensity;
            }
            return i;
        }

        public ImageDecoder createImageDecoder() throws IOException {
            TypedValue value = new TypedValue();
            InputStream is = this.mResources.openRawResource(this.mResId, value);
            synchronized (this.mLock) {
                if (value.density == 0) {
                    this.mResDensity = 160;
                } else if (value.density != 65535) {
                    this.mResDensity = value.density;
                }
            }
            return ImageDecoder.createFromAsset((AssetInputStream) is, this);
        }
    }

    private static native void nClose(long j);

    private static native ImageDecoder nCreate(long j, Source source) throws IOException;

    private static native ImageDecoder nCreate(FileDescriptor fileDescriptor, Source source) throws IOException;

    private static native ImageDecoder nCreate(InputStream inputStream, byte[] bArr, Source source) throws IOException;

    private static native ImageDecoder nCreate(ByteBuffer byteBuffer, int i, int i2, Source source) throws IOException;

    private static native ImageDecoder nCreate(byte[] bArr, int i, int i2, Source source) throws IOException;

    private static native Bitmap nDecodeBitmap(long j, ImageDecoder imageDecoder, boolean z, int i, int i2, Rect rect, boolean z2, int i3, boolean z3, boolean z4, boolean z5, long j2, boolean z6) throws IOException;

    private static native ColorSpace nGetColorSpace(long j);

    private static native String nGetMimeType(long j);

    private static native void nGetPadding(long j, Rect rect);

    private static native Size nGetSampledSize(long j, int i);

    private static ImageDecoder createFromFile(File file, Source source) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        FileDescriptor fd = stream.getFD();
        try {
            Os.lseek(fd, 0, OsConstants.SEEK_CUR);
            ImageDecoder decoder = null;
            try {
                decoder = nCreate(fd, source);
                return decoder;
            } finally {
                if (decoder == null) {
                    IoUtils.closeQuietly(stream);
                } else {
                    decoder.mInputStream = stream;
                    decoder.mOwnsInputStream = true;
                }
            }
        } catch (ErrnoException e) {
            return createFromStream(stream, true, source);
        }
    }

    private static ImageDecoder createFromStream(InputStream is, boolean closeInputStream, Source source) throws IOException {
        byte[] storage = new byte[16384];
        ImageDecoder decoder = null;
        try {
            decoder = nCreate(is, storage, source);
            return decoder;
        } finally {
            if (decoder != null) {
                decoder.mInputStream = is;
                decoder.mOwnsInputStream = closeInputStream;
                decoder.mTempStorage = storage;
            } else if (closeInputStream) {
                IoUtils.closeQuietly(is);
            }
        }
    }

    private static ImageDecoder createFromAssetFileDescriptor(AssetFileDescriptor assetFd, Source source) throws IOException {
        ImageDecoder decoder;
        FileDescriptor fd = assetFd.getFileDescriptor();
        try {
            Os.lseek(fd, assetFd.getStartOffset(), OsConstants.SEEK_SET);
            decoder = nCreate(fd, source);
        } catch (ErrnoException e) {
            decoder = createFromStream(new FileInputStream(fd), true, source);
        } catch (Throwable th) {
            if (null == null) {
                IoUtils.closeQuietly(assetFd);
            } else {
                null.mAssetFd = assetFd;
            }
        }
        if (decoder == null) {
            IoUtils.closeQuietly(assetFd);
        } else {
            decoder.mAssetFd = assetFd;
        }
        return decoder;
    }

    private static ImageDecoder createFromAsset(AssetInputStream ais, Source source) throws IOException {
        ImageDecoder decoder = null;
        try {
            decoder = nCreate(ais.getNativeAsset(), source);
            return decoder;
        } finally {
            if (decoder == null) {
                IoUtils.closeQuietly(ais);
            } else {
                decoder.mInputStream = ais;
                decoder.mOwnsInputStream = true;
            }
        }
    }

    private ImageDecoder(long nativePtr, int width, int height, boolean animated, boolean isNinePatch) {
        this.mNativePtr = nativePtr;
        this.mWidth = width;
        this.mHeight = height;
        this.mDesiredWidth = width;
        this.mDesiredHeight = height;
        this.mAnimated = animated;
        this.mIsNinePatch = isNinePatch;
        this.mCloseGuard.open("close");
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            this.mInputStream = null;
            this.mAssetFd = null;
            close();
        } finally {
            super.finalize();
        }
    }

    public static boolean isMimeTypeSupported(java.lang.String r4) {
        /*
        java.util.Objects.requireNonNull(r4);
        r0 = java.util.Locale.US;
        r0 = r4.toLowerCase(r0);
        r1 = r0.hashCode();
        r2 = 1;
        r3 = 0;
        switch(r1) {
            case -1875291391: goto L_0x00dc;
            case -1635437028: goto L_0x00d1;
            case -1594371159: goto L_0x00c6;
            case -1487464693: goto L_0x00bc;
            case -1487464690: goto L_0x00b2;
            case -1487394660: goto L_0x00a8;
            case -1487018032: goto L_0x009e;
            case -1423313290: goto L_0x0093;
            case -985160897: goto L_0x0088;
            case -879272239: goto L_0x007d;
            case -879267568: goto L_0x0072;
            case -879258763: goto L_0x0067;
            case -332763809: goto L_0x005b;
            case 741270252: goto L_0x004f;
            case 1146342924: goto L_0x0044;
            case 1378106698: goto L_0x0038;
            case 2099152104: goto L_0x002c;
            case 2099152524: goto L_0x0020;
            case 2111234748: goto L_0x0014;
            default: goto L_0x0012;
        };
    L_0x0012:
        goto L_0x00e7;
    L_0x0014:
        r1 = "image/x-canon-cr2";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x001c:
        r0 = 10;
        goto L_0x00e8;
    L_0x0020:
        r1 = "image/x-nikon-nrw";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0028:
        r0 = 13;
        goto L_0x00e8;
    L_0x002c:
        r1 = "image/x-nikon-nef";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0034:
        r0 = 12;
        goto L_0x00e8;
    L_0x0038:
        r1 = "image/x-olympus-orf";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0040:
        r0 = 14;
        goto L_0x00e8;
    L_0x0044:
        r1 = "image/x-ico";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x004c:
        r0 = 7;
        goto L_0x00e8;
    L_0x004f:
        r1 = "image/vnd.wap.wbmp";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0057:
        r0 = 8;
        goto L_0x00e8;
    L_0x005b:
        r1 = "image/x-pentax-pef";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0063:
        r0 = 17;
        goto L_0x00e8;
    L_0x0067:
        r1 = "image/png";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x006f:
        r0 = r3;
        goto L_0x00e8;
    L_0x0072:
        r1 = "image/gif";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x007a:
        r0 = 3;
        goto L_0x00e8;
    L_0x007d:
        r1 = "image/bmp";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0085:
        r0 = 6;
        goto L_0x00e8;
    L_0x0088:
        r1 = "image/x-panasonic-rw2";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0090:
        r0 = 16;
        goto L_0x00e8;
    L_0x0093:
        r1 = "image/x-adobe-dng";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x009b:
        r0 = 11;
        goto L_0x00e8;
    L_0x009e:
        r1 = "image/webp";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00a6:
        r0 = 2;
        goto L_0x00e8;
    L_0x00a8:
        r1 = "image/jpeg";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00b0:
        r0 = r2;
        goto L_0x00e8;
    L_0x00b2:
        r1 = "image/heif";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00ba:
        r0 = 4;
        goto L_0x00e8;
    L_0x00bc:
        r1 = "image/heic";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00c4:
        r0 = 5;
        goto L_0x00e8;
    L_0x00c6:
        r1 = "image/x-sony-arw";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00ce:
        r0 = 9;
        goto L_0x00e8;
    L_0x00d1:
        r1 = "image/x-samsung-srw";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00d9:
        r0 = 18;
        goto L_0x00e8;
    L_0x00dc:
        r1 = "image/x-fuji-raf";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00e4:
        r0 = 15;
        goto L_0x00e8;
    L_0x00e7:
        r0 = -1;
    L_0x00e8:
        switch(r0) {
            case 0: goto L_0x00ec;
            case 1: goto L_0x00ec;
            case 2: goto L_0x00ec;
            case 3: goto L_0x00ec;
            case 4: goto L_0x00ec;
            case 5: goto L_0x00ec;
            case 6: goto L_0x00ec;
            case 7: goto L_0x00ec;
            case 8: goto L_0x00ec;
            case 9: goto L_0x00ec;
            case 10: goto L_0x00ec;
            case 11: goto L_0x00ec;
            case 12: goto L_0x00ec;
            case 13: goto L_0x00ec;
            case 14: goto L_0x00ec;
            case 15: goto L_0x00ec;
            case 16: goto L_0x00ec;
            case 17: goto L_0x00ec;
            case 18: goto L_0x00ec;
            default: goto L_0x00eb;
        };
    L_0x00eb:
        return r3;
    L_0x00ec:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.ImageDecoder.isMimeTypeSupported(java.lang.String):boolean");
    }

    public static Source createSource(Resources res, int resId) {
        return new ResourceSource(res, resId);
    }

    public static Source createSource(ContentResolver cr, Uri uri) {
        return new ContentResolverSource(cr, uri, null);
    }

    public static Source createSource(ContentResolver cr, Uri uri, Resources res) {
        return new ContentResolverSource(cr, uri, res);
    }

    public static Source createSource(AssetManager assets, String fileName) {
        return new AssetSource(assets, fileName);
    }

    public static Source createSource(byte[] data, int offset, int length) throws ArrayIndexOutOfBoundsException {
        if (data == null) {
            throw new NullPointerException("null byte[] in createSource!");
        } else if (offset >= 0 && length >= 0 && offset < data.length && offset + length <= data.length) {
            return new ByteArraySource(data, offset, length);
        } else {
            throw new ArrayIndexOutOfBoundsException("invalid offset/length!");
        }
    }

    public static Source createSource(byte[] data) {
        return createSource(data, 0, data.length);
    }

    public static Source createSource(ByteBuffer buffer) {
        return new ByteBufferSource(buffer);
    }

    public static Source createSource(Resources res, InputStream is) {
        return new InputStreamSource(res, is, Bitmap.getDefaultDensity());
    }

    public static Source createSource(Resources res, InputStream is, int density) {
        return new InputStreamSource(res, is, density);
    }

    public static Source createSource(File file) {
        return new FileSource(file);
    }

    public static Source createSource(Callable<AssetFileDescriptor> callable) {
        return new CallableSource(callable);
    }

    public Size getSampledSize(int sampleSize) {
        if (sampleSize > 0) {
            long j = this.mNativePtr;
            if (j != 0) {
                return nGetSampledSize(j, sampleSize);
            }
            throw new IllegalStateException("ImageDecoder is closed!");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("sampleSize must be positive! provided ");
        stringBuilder.append(sampleSize);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @Deprecated
    public ImageDecoder setResize(int width, int height) {
        setTargetSize(width, height);
        return this;
    }

    public void setTargetSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Dimensions must be positive! provided (");
            stringBuilder.append(width);
            stringBuilder.append(", ");
            stringBuilder.append(height);
            stringBuilder.append(")");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.mDesiredWidth = width;
        this.mDesiredHeight = height;
    }

    @Deprecated
    public ImageDecoder setResize(int sampleSize) {
        setTargetSampleSize(sampleSize);
        return this;
    }

    private int getTargetDimension(int original, int sampleSize, int computed) {
        if (sampleSize >= original) {
            return 1;
        }
        int target = original / sampleSize;
        if (computed != target && Math.abs((computed * sampleSize) - original) >= sampleSize) {
            return target;
        }
        return computed;
    }

    public void setTargetSampleSize(int sampleSize) {
        Size size = getSampledSize(sampleSize);
        setTargetSize(getTargetDimension(this.mWidth, sampleSize, size.getWidth()), getTargetDimension(this.mHeight, sampleSize, size.getHeight()));
    }

    private boolean requestedResize() {
        return (this.mWidth == this.mDesiredWidth && this.mHeight == this.mDesiredHeight) ? false : true;
    }

    public void setAllocator(int allocator) {
        if (allocator < 0 || allocator > 3) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("invalid allocator ");
            stringBuilder.append(allocator);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.mAllocator = allocator;
    }

    public int getAllocator() {
        return this.mAllocator;
    }

    public void setUnpremultipliedRequired(boolean unpremultipliedRequired) {
        this.mUnpremultipliedRequired = unpremultipliedRequired;
    }

    @Deprecated
    public ImageDecoder setRequireUnpremultiplied(boolean unpremultipliedRequired) {
        setUnpremultipliedRequired(unpremultipliedRequired);
        return this;
    }

    public boolean isUnpremultipliedRequired() {
        return this.mUnpremultipliedRequired;
    }

    @Deprecated
    public boolean getRequireUnpremultiplied() {
        return isUnpremultipliedRequired();
    }

    public void setPostProcessor(PostProcessor postProcessor) {
        this.mPostProcessor = postProcessor;
    }

    public PostProcessor getPostProcessor() {
        return this.mPostProcessor;
    }

    public void setOnPartialImageListener(OnPartialImageListener listener) {
        this.mOnPartialImageListener = listener;
    }

    public OnPartialImageListener getOnPartialImageListener() {
        return this.mOnPartialImageListener;
    }

    public void setCrop(Rect subset) {
        this.mCropRect = subset;
    }

    public Rect getCrop() {
        return this.mCropRect;
    }

    public void setOutPaddingRect(Rect outPadding) {
        this.mOutPaddingRect = outPadding;
    }

    public void setMutableRequired(boolean mutable) {
        this.mMutable = mutable;
    }

    @Deprecated
    public ImageDecoder setMutable(boolean mutable) {
        setMutableRequired(mutable);
        return this;
    }

    public boolean isMutableRequired() {
        return this.mMutable;
    }

    @Deprecated
    public boolean getMutable() {
        return isMutableRequired();
    }

    public void setMemorySizePolicy(int policy) {
        this.mConserveMemory = policy == 0;
    }

    public int getMemorySizePolicy() {
        return this.mConserveMemory ^ 1;
    }

    @Deprecated
    public void setConserveMemory(boolean conserveMemory) {
        this.mConserveMemory = conserveMemory;
    }

    @Deprecated
    public boolean getConserveMemory() {
        return this.mConserveMemory;
    }

    public void setDecodeAsAlphaMaskEnabled(boolean enabled) {
        this.mDecodeAsAlphaMask = enabled;
    }

    @Deprecated
    public ImageDecoder setDecodeAsAlphaMask(boolean enabled) {
        setDecodeAsAlphaMaskEnabled(enabled);
        return this;
    }

    @Deprecated
    public ImageDecoder setAsAlphaMask(boolean asAlphaMask) {
        setDecodeAsAlphaMask(asAlphaMask);
        return this;
    }

    public boolean isDecodeAsAlphaMaskEnabled() {
        return this.mDecodeAsAlphaMask;
    }

    @Deprecated
    public boolean getDecodeAsAlphaMask() {
        return this.mDecodeAsAlphaMask;
    }

    @Deprecated
    public boolean getAsAlphaMask() {
        return getDecodeAsAlphaMask();
    }

    public void setTargetColorSpace(ColorSpace colorSpace) {
        this.mDesiredColorSpace = colorSpace;
    }

    public void close() {
        this.mCloseGuard.close();
        if (this.mClosed.compareAndSet(false, true)) {
            nClose(this.mNativePtr);
            this.mNativePtr = 0;
            if (this.mOwnsInputStream) {
                IoUtils.closeQuietly(this.mInputStream);
            }
            IoUtils.closeQuietly(this.mAssetFd);
            this.mInputStream = null;
            this.mAssetFd = null;
            this.mTempStorage = null;
        }
    }

    private void checkState(boolean animated) {
        if (this.mNativePtr != 0) {
            checkSubset(this.mDesiredWidth, this.mDesiredHeight, this.mCropRect);
            if (!animated && this.mAllocator == 3) {
                if (this.mMutable) {
                    throw new IllegalStateException("Cannot make mutable HARDWARE Bitmap!");
                } else if (this.mDecodeAsAlphaMask) {
                    throw new IllegalStateException("Cannot make HARDWARE Alpha mask Bitmap!");
                }
            }
            if (this.mPostProcessor != null && this.mUnpremultipliedRequired) {
                throw new IllegalStateException("Cannot draw to unpremultiplied pixels!");
            }
            return;
        }
        throw new IllegalStateException("Cannot use closed ImageDecoder!");
    }

    private static void checkSubset(int width, int height, Rect r) {
        if (r != null) {
            if (r.left < 0 || r.top < 0 || r.right > width || r.bottom > height) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Subset ");
                stringBuilder.append(r);
                stringBuilder.append(" not contained by scaled image bounds: (");
                stringBuilder.append(width);
                stringBuilder.append(" x ");
                stringBuilder.append(height);
                stringBuilder.append(")");
                throw new IllegalStateException(stringBuilder.toString());
            }
        }
    }

    private boolean checkForExtended() {
        ColorSpace colorSpace = this.mDesiredColorSpace;
        boolean z = false;
        if (colorSpace == null) {
            return false;
        }
        if (colorSpace == ColorSpace.get(Named.EXTENDED_SRGB) || this.mDesiredColorSpace == ColorSpace.get(Named.LINEAR_EXTENDED_SRGB)) {
            z = true;
        }
        return z;
    }

    private long getColorSpacePtr() {
        ColorSpace colorSpace = this.mDesiredColorSpace;
        if (colorSpace == null) {
            return 0;
        }
        return colorSpace.getNativeInstance();
    }

    private Bitmap decodeBitmapInternal() throws IOException {
        boolean z = false;
        checkState(false);
        long j = this.mNativePtr;
        if (this.mPostProcessor != null) {
            z = true;
        }
        return nDecodeBitmap(j, this, z, this.mDesiredWidth, this.mDesiredHeight, this.mCropRect, this.mMutable, this.mAllocator, this.mUnpremultipliedRequired, this.mConserveMemory, this.mDecodeAsAlphaMask, getColorSpacePtr(), checkForExtended());
    }

    private void callHeaderDecoded(OnHeaderDecodedListener listener, Source src) {
        if (listener != null) {
            ImageInfo info = new ImageInfo();
            try {
                listener.onHeaderDecoded(this, info, src);
            } finally {
                info.mDecoder = null;
            }
        }
    }

    public static Drawable decodeDrawable(Source src, OnHeaderDecodedListener listener) throws IOException {
        if (listener != null) {
            return decodeDrawableImpl(src, listener);
        }
        throw new IllegalArgumentException("listener cannot be null! Use decodeDrawable(Source) to not have a listener");
    }

    private static Drawable decodeDrawableImpl(Source src, OnHeaderDecodedListener listener) throws IOException {
        Throwable th;
        Source source = src;
        ImageDecoder decoder = src.createImageDecoder();
        int srcDensity;
        InputStream inputStream;
        InputStream inputStream2;
        Rect opticalInsets;
        long j;
        int i;
        int i2;
        long colorSpacePtr;
        boolean checkForExtended;
        int computeDstDensity;
        Rect rect;
        int i3;
        Rect rect2;
        Drawable d;
        ImageDecoder postProcessPtr;
        try {
            decoder.mSource = source;
            decoder.callHeaderDecoded(listener, source);
            if (decoder.mUnpremultipliedRequired) {
                throw new IllegalStateException("Cannot decode a Drawable with unpremultiplied pixels!");
            } else if (decoder.mMutable) {
                throw new IllegalStateException("Cannot decode a mutable Drawable!");
            } else {
                srcDensity = decoder.computeDensity(source);
                inputStream = null;
                if (!decoder.mAnimated) {
                    inputStream2 = inputStream;
                    Bitmap bm = decoder.decodeBitmapInternal();
                    bm.setDensity(srcDensity);
                    Resources res = src.getResources();
                    byte[] np = bm.getNinePatchChunk();
                    if (np == null || !NinePatch.isNinePatchChunk(np)) {
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(res, bm);
                        $closeResource(inputStream2, decoder);
                        return bitmapDrawable;
                    }
                    Rect padding;
                    opticalInsets = new Rect();
                    bm.getOpticalInsets(opticalInsets);
                    Rect padding2 = decoder.mOutPaddingRect;
                    if (padding2 == null) {
                        padding = new Rect();
                    } else {
                        padding = padding2;
                    }
                    nGetPadding(decoder.mNativePtr, padding);
                    NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(res, bm, np, padding, opticalInsets, null);
                    $closeResource(inputStream2, decoder);
                    return ninePatchDrawable;
                } else if (decoder.mPostProcessor == null) {
                    decoder.checkState(true);
                    j = decoder.mNativePtr;
                    i = decoder.mDesiredWidth;
                    i2 = decoder.mDesiredHeight;
                    colorSpacePtr = decoder.getColorSpacePtr();
                    checkForExtended = decoder.checkForExtended();
                    computeDstDensity = src.computeDstDensity();
                    opticalInsets = decoder.mCropRect;
                    rect = opticalInsets;
                    i3 = srcDensity;
                    inputStream2 = inputStream;
                    rect2 = rect;
                    d = new AnimatedImageDrawable(j, postProcessPtr, i, i2, colorSpacePtr, checkForExtended, i3, computeDstDensity, rect2, decoder.mInputStream, decoder.mAssetFd);
                    decoder.mInputStream = inputStream2;
                    decoder.mAssetFd = inputStream2;
                    $closeResource(inputStream2, decoder);
                    return d;
                } else {
                    postProcessPtr = decoder;
                    decoder.checkState(true);
                    j = decoder.mNativePtr;
                    i = decoder.mDesiredWidth;
                    i2 = decoder.mDesiredHeight;
                    colorSpacePtr = decoder.getColorSpacePtr();
                    checkForExtended = decoder.checkForExtended();
                    computeDstDensity = src.computeDstDensity();
                    opticalInsets = decoder.mCropRect;
                    rect = opticalInsets;
                    i3 = srcDensity;
                    inputStream2 = inputStream;
                    rect2 = rect;
                    d = new AnimatedImageDrawable(j, postProcessPtr, i, i2, colorSpacePtr, checkForExtended, i3, computeDstDensity, rect2, decoder.mInputStream, decoder.mAssetFd);
                    decoder.mInputStream = inputStream2;
                    decoder.mAssetFd = inputStream2;
                    $closeResource(inputStream2, decoder);
                    return d;
                }
            }
        } catch (Throwable th2) {
            srcDensity = th2;
            if (decoder != null) {
                $closeResource(th, decoder);
            }
        } finally {
            while (true) {
                srcDensity = 
/*
Method generation error in method: android.graphics.ImageDecoder.decodeDrawableImpl(android.graphics.ImageDecoder$Source, android.graphics.ImageDecoder$OnHeaderDecodedListener):android.graphics.drawable.Drawable, dex: classes.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: MERGE  (r0_8 'srcDensity' int) = (r0_7 'srcDensity' int), (r15_5 'inputStream' java.io.InputStream) in method: android.graphics.ImageDecoder.decodeDrawableImpl(android.graphics.ImageDecoder$Source, android.graphics.ImageDecoder$OnHeaderDecodedListener):android.graphics.drawable.Drawable, dex: classes.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:228)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:205)
	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:102)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:52)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:175)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:300)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:183)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:321)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:259)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:221)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:111)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:77)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:10)
	at jadx.core.ProcessClass.process(ProcessClass.java:38)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
Caused by: jadx.core.utils.exceptions.CodegenException: MERGE can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:539)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:511)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:222)
	... 25 more

*/

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    public static Drawable decodeDrawable(Source src) throws IOException {
        return decodeDrawableImpl(src, null);
    }

    public static Bitmap decodeBitmap(Source src, OnHeaderDecodedListener listener) throws IOException {
        if (listener != null) {
            return decodeBitmapImpl(src, listener);
        }
        throw new IllegalArgumentException("listener cannot be null! Use decodeBitmap(Source) to not have a listener");
    }

    /* JADX WARNING: Missing block: B:15:0x0032, code skipped:
            if (r0 != null) goto L_0x0034;
     */
    /* JADX WARNING: Missing block: B:16:0x0034, code skipped:
            $closeResource(r1, r0);
     */
    private static android.graphics.Bitmap decodeBitmapImpl(android.graphics.ImageDecoder.Source r7, android.graphics.ImageDecoder.OnHeaderDecodedListener r8) throws java.io.IOException {
        /*
        r0 = r7.createImageDecoder();
        r0.mSource = r7;	 Catch:{ all -> 0x002f }
        r0.callHeaderDecoded(r8, r7);	 Catch:{ all -> 0x002f }
        r1 = r0.computeDensity(r7);	 Catch:{ all -> 0x002f }
        r2 = r0.decodeBitmapInternal();	 Catch:{ all -> 0x002f }
        r2.setDensity(r1);	 Catch:{ all -> 0x002f }
        r3 = r0.mOutPaddingRect;	 Catch:{ all -> 0x002f }
        if (r3 == 0) goto L_0x0029;
    L_0x0018:
        r4 = r2.getNinePatchChunk();	 Catch:{ all -> 0x002f }
        if (r4 == 0) goto L_0x0029;
    L_0x001e:
        r5 = android.graphics.NinePatch.isNinePatchChunk(r4);	 Catch:{ all -> 0x002f }
        if (r5 == 0) goto L_0x0029;
    L_0x0024:
        r5 = r0.mNativePtr;	 Catch:{ all -> 0x002f }
        nGetPadding(r5, r3);	 Catch:{ all -> 0x002f }
        r4 = 0;
        $closeResource(r4, r0);
        return r2;
    L_0x002f:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0031 }
    L_0x0031:
        r2 = move-exception;
        if (r0 == 0) goto L_0x0037;
    L_0x0034:
        $closeResource(r1, r0);
    L_0x0037:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.ImageDecoder.decodeBitmapImpl(android.graphics.ImageDecoder$Source, android.graphics.ImageDecoder$OnHeaderDecodedListener):android.graphics.Bitmap");
    }

    private int computeDensity(Source src) {
        if (requestedResize()) {
            return 0;
        }
        int srcDensity = src.getDensity();
        if (srcDensity == 0) {
            return srcDensity;
        }
        if (this.mIsNinePatch && this.mPostProcessor == null) {
            return srcDensity;
        }
        Resources res = src.getResources();
        if (res != null && res.getDisplayMetrics().noncompatDensityDpi == srcDensity) {
            return srcDensity;
        }
        int dstDensity = src.computeDstDensity();
        if (srcDensity == dstDensity) {
            return srcDensity;
        }
        if (srcDensity < dstDensity && sApiLevel >= 28) {
            return srcDensity;
        }
        float scale = ((float) dstDensity) / ((float) srcDensity);
        setTargetSize(Math.max((int) ((((float) this.mWidth) * scale) + 0.5f), 1), Math.max((int) ((((float) this.mHeight) * scale) + 0.5f), 1));
        return dstDensity;
    }

    private String getMimeType() {
        return nGetMimeType(this.mNativePtr);
    }

    private ColorSpace getColorSpace() {
        return nGetColorSpace(this.mNativePtr);
    }

    public static Bitmap decodeBitmap(Source src) throws IOException {
        return decodeBitmapImpl(src, null);
    }

    @UnsupportedAppUsage
    private int postProcessAndRelease(Canvas canvas) {
        try {
            int onPostProcess = this.mPostProcessor.onPostProcess(canvas);
            return onPostProcess;
        } finally {
            canvas.release();
        }
    }

    private void onPartialImage(int error, Throwable cause) throws DecodeException {
        DecodeException exception = new DecodeException(error, cause, this.mSource);
        OnPartialImageListener onPartialImageListener = this.mOnPartialImageListener;
        if (onPartialImageListener == null || !onPartialImageListener.onPartialImage(exception)) {
            throw exception;
        }
    }
}
