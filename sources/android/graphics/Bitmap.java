package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.content.res.ResourcesImpl;
import android.graphics.ColorSpace.Named;
import android.graphics.NinePatch.InsetStruct;
import android.hardware.HardwareBuffer;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.StrictMode;
import android.os.Trace;
import android.util.DisplayMetrics;
import android.util.Half;
import android.util.Log;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import libcore.util.NativeAllocationRegistry;

public final class Bitmap implements Parcelable {
    public static final Creator<Bitmap> CREATOR = new Creator<Bitmap>() {
        public Bitmap createFromParcel(Parcel p) {
            Bitmap bm = Bitmap.nativeCreateFromParcel(p);
            if (bm != null) {
                return bm;
            }
            throw new RuntimeException("Failed to unparcel Bitmap");
        }

        public Bitmap[] newArray(int size) {
            return new Bitmap[size];
        }
    };
    public static final int DENSITY_NONE = 0;
    private static final long NATIVE_ALLOCATION_SIZE = 32;
    private static final String TAG = "Bitmap";
    private static final int WORKING_COMPRESS_STORAGE = 4096;
    private static volatile int sDefaultDensity = -1;
    public static volatile int sPreloadTracingNumInstantiatedBitmaps;
    public static volatile long sPreloadTracingTotalBitmapsSize;
    private ColorSpace mColorSpace;
    public int mDensity;
    @UnsupportedAppUsage
    private int mHeight;
    @UnsupportedAppUsage
    private final long mNativePtr;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769491)
    private byte[] mNinePatchChunk;
    @UnsupportedAppUsage
    private InsetStruct mNinePatchInsets;
    private boolean mRecycled;
    private boolean mRequestPremultiplied;
    @UnsupportedAppUsage
    private int mWidth;

    /* renamed from: android.graphics.Bitmap$2 */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Config.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.RGB_565.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ALPHA_8.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.RGBA_F16.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_4444.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_8888.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public enum CompressFormat {
        JPEG(0),
        PNG(1),
        WEBP(2);
        
        final int nativeInt;

        private CompressFormat(int nativeInt) {
            this.nativeInt = nativeInt;
        }
    }

    public enum Config {
        ALPHA_8(1),
        RGB_565(3),
        ARGB_4444(4),
        ARGB_8888(5),
        RGBA_F16(6),
        HARDWARE(7);
        
        private static Config[] sConfigs;
        @UnsupportedAppUsage
        final int nativeInt;

        static {
            sConfigs = new Config[]{null, r9, null, r10, r11, r12, r13, r14};
        }

        private Config(int ni) {
            this.nativeInt = ni;
        }

        @UnsupportedAppUsage
        static Config nativeToConfig(int ni) {
            return sConfigs[ni];
        }
    }

    private static native boolean nativeCompress(long j, int i, int i2, OutputStream outputStream, byte[] bArr);

    private static native ColorSpace nativeComputeColorSpace(long j);

    private static native int nativeConfig(long j);

    private static native Bitmap nativeCopy(long j, int i, boolean z);

    private static native Bitmap nativeCopyAshmem(long j);

    private static native Bitmap nativeCopyAshmemConfig(long j, int i);

    private static native void nativeCopyPixelsFromBuffer(long j, Buffer buffer);

    private static native void nativeCopyPixelsToBuffer(long j, Buffer buffer);

    private static native Bitmap nativeCopyPreserveInternalConfig(long j);

    private static native Bitmap nativeCreate(int[] iArr, int i, int i2, int i3, int i4, int i5, boolean z, long j);

    private static native Bitmap nativeCreateFromParcel(Parcel parcel);

    private static native GraphicBuffer nativeCreateGraphicBufferHandle(long j);

    private static native void nativeErase(long j, int i);

    private static native void nativeErase(long j, long j2, long j3);

    private static native Bitmap nativeExtractAlpha(long j, long j2, int[] iArr);

    private static native int nativeGenerationId(long j);

    private static native int nativeGetAllocationByteCount(long j);

    private static native long nativeGetColor(long j, int i, int i2);

    private static native long nativeGetNativeFinalizer();

    private static native int nativeGetPixel(long j, int i, int i2);

    private static native void nativeGetPixels(long j, int[] iArr, int i, int i2, int i3, int i4, int i5, int i6);

    private static native boolean nativeHasAlpha(long j);

    private static native boolean nativeHasMipMap(long j);

    private static native boolean nativeIsImmutable(long j);

    private static native boolean nativeIsPremultiplied(long j);

    private static native boolean nativeIsSRGB(long j);

    private static native boolean nativeIsSRGBLinear(long j);

    private static native void nativePrepareToDraw(long j);

    @UnsupportedAppUsage
    private static native void nativeReconfigure(long j, int i, int i2, int i3, boolean z);

    private static native void nativeRecycle(long j);

    private static native int nativeRowBytes(long j);

    private static native boolean nativeSameAs(long j, long j2);

    private static native void nativeSetColorSpace(long j, long j2);

    private static native void nativeSetHasAlpha(long j, boolean z, boolean z2);

    private static native void nativeSetHasMipMap(long j, boolean z);

    private static native void nativeSetImmutable(long j);

    private static native void nativeSetPixel(long j, int i, int i2, int i3);

    private static native void nativeSetPixels(long j, int[] iArr, int i, int i2, int i3, int i4, int i5, int i6);

    private static native void nativeSetPremultiplied(long j, boolean z);

    private static native Bitmap nativeWrapHardwareBufferBitmap(HardwareBuffer hardwareBuffer, long j);

    private static native boolean nativeWriteToParcel(long j, boolean z, int i, Parcel parcel);

    @UnsupportedAppUsage
    public static void setDefaultDensity(int density) {
        sDefaultDensity = density;
    }

    @UnsupportedAppUsage
    static int getDefaultDensity() {
        if (sDefaultDensity >= 0) {
            return sDefaultDensity;
        }
        sDefaultDensity = DisplayMetrics.DENSITY_DEVICE;
        return sDefaultDensity;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    Bitmap(long nativeBitmap, int width, int height, int density, boolean requestPremultiplied, byte[] ninePatchChunk, InsetStruct ninePatchInsets) {
        this(nativeBitmap, width, height, density, requestPremultiplied, ninePatchChunk, ninePatchInsets, true);
    }

    Bitmap(long nativeBitmap, int width, int height, int density, boolean requestPremultiplied, byte[] ninePatchChunk, InsetStruct ninePatchInsets, boolean fromMalloc) {
        this.mDensity = getDefaultDensity();
        if (nativeBitmap != 0) {
            NativeAllocationRegistry registry;
            this.mWidth = width;
            this.mHeight = height;
            this.mRequestPremultiplied = requestPremultiplied;
            this.mNinePatchChunk = ninePatchChunk;
            this.mNinePatchInsets = ninePatchInsets;
            if (density >= 0) {
                this.mDensity = density;
            }
            this.mNativePtr = nativeBitmap;
            int allocationByteCount = getAllocationByteCount();
            if (fromMalloc) {
                registry = NativeAllocationRegistry.createMalloced(Bitmap.class.getClassLoader(), nativeGetNativeFinalizer(), (long) allocationByteCount);
            } else {
                registry = NativeAllocationRegistry.createNonmalloced(Bitmap.class.getClassLoader(), nativeGetNativeFinalizer(), (long) allocationByteCount);
            }
            registry.registerNativeAllocation(this, nativeBitmap);
            if (ResourcesImpl.TRACE_FOR_DETAILED_PRELOAD) {
                sPreloadTracingNumInstantiatedBitmaps++;
                sPreloadTracingTotalBitmapsSize += ((long) allocationByteCount) + 32;
                return;
            }
            return;
        }
        throw new RuntimeException("internal error: native bitmap is 0");
    }

    public long getNativeInstance() {
        return this.mNativePtr;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void reinit(int width, int height, boolean requestPremultiplied) {
        this.mWidth = width;
        this.mHeight = height;
        this.mRequestPremultiplied = requestPremultiplied;
        this.mColorSpace = null;
    }

    public int getDensity() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getDensity() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return this.mDensity;
    }

    public void setDensity(int density) {
        this.mDensity = density;
    }

    public void reconfigure(int width, int height, Config config) {
        checkRecycled("Can't call reconfigure() on a recycled bitmap");
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be > 0");
        } else if (isMutable()) {
            nativeReconfigure(this.mNativePtr, width, height, config.nativeInt, this.mRequestPremultiplied);
            this.mWidth = width;
            this.mHeight = height;
            this.mColorSpace = null;
        } else {
            throw new IllegalStateException("only mutable bitmaps may be reconfigured");
        }
    }

    public void setWidth(int width) {
        reconfigure(width, getHeight(), getConfig());
    }

    public void setHeight(int height) {
        reconfigure(getWidth(), height, getConfig());
    }

    public void setConfig(Config config) {
        reconfigure(getWidth(), getHeight(), config);
    }

    @UnsupportedAppUsage
    public void setNinePatchChunk(byte[] chunk) {
        this.mNinePatchChunk = chunk;
    }

    public void recycle() {
        if (!this.mRecycled) {
            nativeRecycle(this.mNativePtr);
            this.mNinePatchChunk = null;
            this.mRecycled = true;
        }
    }

    public final boolean isRecycled() {
        return this.mRecycled;
    }

    public int getGenerationId() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getGenerationId() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeGenerationId(this.mNativePtr);
    }

    private void checkRecycled(String errorMessage) {
        if (this.mRecycled) {
            throw new IllegalStateException(errorMessage);
        }
    }

    private void checkHardware(String errorMessage) {
        if (getConfig() == Config.HARDWARE) {
            throw new IllegalStateException(errorMessage);
        }
    }

    private static void checkXYSign(int x, int y) {
        if (x < 0) {
            throw new IllegalArgumentException("x must be >= 0");
        } else if (y < 0) {
            throw new IllegalArgumentException("y must be >= 0");
        }
    }

    private static void checkWidthHeight(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("width must be > 0");
        } else if (height <= 0) {
            throw new IllegalArgumentException("height must be > 0");
        }
    }

    public void copyPixelsToBuffer(Buffer dst) {
        int shift;
        checkHardware("unable to copyPixelsToBuffer, pixel access is not supported on Config#HARDWARE bitmaps");
        int elements = dst.remaining();
        if (dst instanceof ByteBuffer) {
            shift = 0;
        } else if (dst instanceof ShortBuffer) {
            shift = 1;
        } else if (dst instanceof IntBuffer) {
            shift = 2;
        } else {
            throw new RuntimeException("unsupported Buffer subclass");
        }
        long pixelSize = (long) getByteCount();
        if ((((long) elements) << shift) >= pixelSize) {
            nativeCopyPixelsToBuffer(this.mNativePtr, dst);
            dst.position((int) (((long) dst.position()) + (pixelSize >> shift)));
            return;
        }
        throw new RuntimeException("Buffer not large enough for pixels");
    }

    public void copyPixelsFromBuffer(Buffer src) {
        int shift;
        checkRecycled("copyPixelsFromBuffer called on recycled bitmap");
        checkHardware("unable to copyPixelsFromBuffer, Config#HARDWARE bitmaps are immutable");
        int elements = src.remaining();
        if (src instanceof ByteBuffer) {
            shift = 0;
        } else if (src instanceof ShortBuffer) {
            shift = 1;
        } else if (src instanceof IntBuffer) {
            shift = 2;
        } else {
            throw new RuntimeException("unsupported Buffer subclass");
        }
        long bitmapBytes = (long) getByteCount();
        if ((((long) elements) << shift) >= bitmapBytes) {
            nativeCopyPixelsFromBuffer(this.mNativePtr, src);
            src.position((int) (((long) src.position()) + (bitmapBytes >> shift)));
            return;
        }
        throw new RuntimeException("Buffer not large enough for pixels");
    }

    private void noteHardwareBitmapSlowCall() {
        if (getConfig() == Config.HARDWARE) {
            StrictMode.noteSlowCall("Warning: attempt to read pixels from hardware bitmap, which is very slow operation");
        }
    }

    public Bitmap copy(Config config, boolean isMutable) {
        checkRecycled("Can't copy a recycled bitmap");
        if (config == Config.HARDWARE && isMutable) {
            throw new IllegalArgumentException("Hardware bitmaps are always immutable");
        }
        noteHardwareBitmapSlowCall();
        Bitmap b = nativeCopy(this.mNativePtr, config.nativeInt, isMutable);
        if (b != null) {
            b.setPremultiplied(this.mRequestPremultiplied);
            b.mDensity = this.mDensity;
        }
        return b;
    }

    @UnsupportedAppUsage
    public Bitmap createAshmemBitmap() {
        checkRecycled("Can't copy a recycled bitmap");
        noteHardwareBitmapSlowCall();
        Bitmap b = nativeCopyAshmem(this.mNativePtr);
        if (b != null) {
            b.setPremultiplied(this.mRequestPremultiplied);
            b.mDensity = this.mDensity;
        }
        return b;
    }

    @UnsupportedAppUsage
    public Bitmap createAshmemBitmap(Config config) {
        checkRecycled("Can't copy a recycled bitmap");
        noteHardwareBitmapSlowCall();
        Bitmap b = nativeCopyAshmemConfig(this.mNativePtr, config.nativeInt);
        if (b != null) {
            b.setPremultiplied(this.mRequestPremultiplied);
            b.mDensity = this.mDensity;
        }
        return b;
    }

    public static Bitmap wrapHardwareBuffer(HardwareBuffer hardwareBuffer, ColorSpace colorSpace) {
        if ((hardwareBuffer.getUsage() & 256) != 0) {
            int format = hardwareBuffer.getFormat();
            if (colorSpace == null) {
                colorSpace = ColorSpace.get(Named.SRGB);
            }
            return nativeWrapHardwareBufferBitmap(hardwareBuffer, colorSpace.getNativeInstance());
        }
        throw new IllegalArgumentException("usage flags must contain USAGE_GPU_SAMPLED_IMAGE.");
    }

    /* JADX WARNING: Missing block: B:10:0x0011, code skipped:
            if (r0 != null) goto L_0x0013;
     */
    /* JADX WARNING: Missing block: B:12:?, code skipped:
            r0.close();
     */
    /* JADX WARNING: Missing block: B:13:0x0017, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:14:0x0018, code skipped:
            r1.addSuppressed(r3);
     */
    public static android.graphics.Bitmap wrapHardwareBuffer(android.graphics.GraphicBuffer r4, android.graphics.ColorSpace r5) {
        /*
        r0 = android.hardware.HardwareBuffer.createFromGraphicBuffer(r4);
        r1 = wrapHardwareBuffer(r0, r5);	 Catch:{ all -> 0x000e }
        if (r0 == 0) goto L_0x000d;
    L_0x000a:
        r0.close();
    L_0x000d:
        return r1;
    L_0x000e:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0010 }
    L_0x0010:
        r2 = move-exception;
        if (r0 == 0) goto L_0x001b;
    L_0x0013:
        r0.close();	 Catch:{ all -> 0x0017 }
        goto L_0x001b;
    L_0x0017:
        r3 = move-exception;
        r1.addSuppressed(r3);
    L_0x001b:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.Bitmap.wrapHardwareBuffer(android.graphics.GraphicBuffer, android.graphics.ColorSpace):android.graphics.Bitmap");
    }

    public static Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        Matrix m = new Matrix();
        int width = src.getWidth();
        int height = src.getHeight();
        if (!(width == dstWidth && height == dstHeight)) {
            m.setScale(((float) dstWidth) / ((float) width), ((float) dstHeight) / ((float) height));
        }
        return createBitmap(src, 0, 0, width, height, m, filter);
    }

    public static Bitmap createBitmap(Bitmap src) {
        return createBitmap(src, 0, 0, src.getWidth(), src.getHeight());
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height) {
        return createBitmap(source, x, y, width, height, null, false);
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter) {
        Bitmap source2 = source;
        int i = x;
        int i2 = y;
        int i3 = width;
        int i4 = height;
        Matrix matrix = m;
        checkXYSign(x, y);
        checkWidthHeight(width, height);
        if (i + i3 > source.getWidth()) {
            throw new IllegalArgumentException("x + width must be <= bitmap.width()");
        } else if (i2 + i4 > source.getHeight()) {
            throw new IllegalArgumentException("y + height must be <= bitmap.height()");
        } else if (source.isRecycled()) {
            throw new IllegalArgumentException("cannot use a recycled source in createBitmap");
        } else if (!source.isMutable() && i == 0 && i2 == 0 && i3 == source.getWidth() && i4 == source.getHeight() && (matrix == null || m.isIdentity())) {
            return source2;
        } else {
            Config newConfig;
            RectF deviceR;
            Bitmap bitmap;
            Paint paint;
            boolean isHardware = source.getConfig() == Config.HARDWARE;
            if (isHardware) {
                source.noteHardwareBitmapSlowCall();
                source2 = nativeCopyPreserveInternalConfig(source2.mNativePtr);
            }
            int neww = width;
            int newh = height;
            Rect srcR = new Rect(i, i2, i + i3, i2 + i4);
            RectF dstR = new RectF(0.0f, 0.0f, (float) i3, (float) i4);
            RectF deviceR2 = new RectF();
            Config newConfig2 = Config.ARGB_8888;
            Config config = source2.getConfig();
            if (config != null) {
                int i5 = AnonymousClass2.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()];
                if (i5 == 1) {
                    newConfig = Config.RGB_565;
                } else if (i5 == 2) {
                    newConfig = Config.ALPHA_8;
                } else if (i5 != 3) {
                    newConfig = Config.ARGB_8888;
                } else {
                    newConfig = Config.RGBA_F16;
                }
            } else {
                newConfig = newConfig2;
            }
            ColorSpace cs = source2.getColorSpace();
            if (matrix == null || m.isIdentity()) {
                deviceR = deviceR2;
                bitmap = createBitmap(null, neww, newh, newConfig, source2.hasAlpha(), cs);
                paint = null;
            } else {
                Config transformedConfig;
                boolean transformed = m.rectStaysRect() ^ true;
                matrix.mapRect(deviceR2, dstR);
                neww = Math.round(deviceR2.width());
                newh = Math.round(deviceR2.height());
                Config transformedConfig2 = newConfig;
                if (!transformed || transformedConfig2 == Config.ARGB_8888 || transformedConfig2 == Config.RGBA_F16) {
                    transformedConfig = transformedConfig2;
                } else {
                    transformedConfig = Config.ARGB_8888;
                    if (cs == null) {
                        cs = ColorSpace.get(Named.SRGB);
                    }
                }
                boolean z = transformed || source2.hasAlpha();
                bitmap = createBitmap(null, neww, newh, transformedConfig, z, cs);
                paint = new Paint();
                paint.setFilterBitmap(filter);
                if (transformed) {
                    paint.setAntiAlias(true);
                }
                deviceR = deviceR2;
            }
            bitmap.mDensity = source2.mDensity;
            bitmap.setHasAlpha(source2.hasAlpha());
            bitmap.setPremultiplied(source2.mRequestPremultiplied);
            Canvas canvas = new Canvas(bitmap);
            canvas.translate(-deviceR.left, -deviceR.top);
            canvas.concat(matrix);
            canvas.drawBitmap(source2, srcR, dstR, paint);
            canvas.setBitmap(null);
            if (isHardware) {
                return bitmap.copy(Config.HARDWARE, false);
            }
            return bitmap;
        }
    }

    public static Bitmap createBitmap(int width, int height, Config config) {
        return createBitmap(width, height, config, true);
    }

    public static Bitmap createBitmap(DisplayMetrics display, int width, int height, Config config) {
        return createBitmap(display, width, height, config, true);
    }

    public static Bitmap createBitmap(int width, int height, Config config, boolean hasAlpha) {
        return createBitmap(null, width, height, config, hasAlpha);
    }

    public static Bitmap createBitmap(int width, int height, Config config, boolean hasAlpha, ColorSpace colorSpace) {
        return createBitmap(null, width, height, config, hasAlpha, colorSpace);
    }

    public static Bitmap createBitmap(DisplayMetrics display, int width, int height, Config config, boolean hasAlpha) {
        return createBitmap(display, width, height, config, hasAlpha, ColorSpace.get(Named.SRGB));
    }

    public static Bitmap createBitmap(DisplayMetrics display, int width, int height, Config config, boolean hasAlpha, ColorSpace colorSpace) {
        DisplayMetrics displayMetrics = display;
        Config config2 = config;
        boolean z = hasAlpha;
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be > 0");
        } else if (config2 == Config.HARDWARE) {
            throw new IllegalArgumentException("can't create mutable bitmap with Config.HARDWARE");
        } else if (colorSpace != null || config2 == Config.ALPHA_8) {
            Bitmap bm = nativeCreate(null, 0, width, width, height, config2.nativeInt, true, colorSpace == null ? 0 : colorSpace.getNativeInstance());
            if (displayMetrics != null) {
                bm.mDensity = displayMetrics.densityDpi;
            }
            bm.setHasAlpha(z);
            if ((config2 == Config.ARGB_8888 || config2 == Config.RGBA_F16) && !z) {
                nativeErase(bm.mNativePtr, -16777216);
            }
            return bm;
        } else {
            throw new IllegalArgumentException("can't create bitmap without a color space");
        }
    }

    public static Bitmap createBitmap(int[] colors, int offset, int stride, int width, int height, Config config) {
        return createBitmap(null, colors, offset, stride, width, height, config);
    }

    public static Bitmap createBitmap(DisplayMetrics display, int[] colors, int offset, int stride, int width, int height, Config config) {
        DisplayMetrics displayMetrics = display;
        int i = width;
        checkWidthHeight(width, height);
        Config config2;
        if (Math.abs(stride) >= i) {
            int lastScanline = offset + ((height - 1) * stride);
            int length = colors.length;
            if (offset < 0 || offset + i > length || lastScanline < 0 || lastScanline + i > length) {
                config2 = config;
                throw new ArrayIndexOutOfBoundsException();
            } else if (i <= 0 || height <= 0) {
                config2 = config;
                throw new IllegalArgumentException("width and height must be > 0");
            } else {
                Bitmap bm = colors;
                int i2 = offset;
                int i3 = stride;
                int i4 = width;
                int i5 = height;
                bm = nativeCreate(bm, i2, i3, i4, i5, config.nativeInt, false, ColorSpace.get(Named.SRGB).getNativeInstance());
                if (displayMetrics != null) {
                    bm.mDensity = displayMetrics.densityDpi;
                }
                return bm;
            }
        }
        int[] iArr = colors;
        config2 = config;
        throw new IllegalArgumentException("abs(stride) must be >= width");
    }

    public static Bitmap createBitmap(int[] colors, int width, int height, Config config) {
        return createBitmap(null, colors, 0, width, width, height, config);
    }

    public static Bitmap createBitmap(DisplayMetrics display, int[] colors, int width, int height, Config config) {
        return createBitmap(display, colors, 0, width, width, height, config);
    }

    public static Bitmap createBitmap(Picture source) {
        return createBitmap(source, source.getWidth(), source.getHeight(), Config.HARDWARE);
    }

    public static Bitmap createBitmap(Picture source, int width, int height, Config config) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width & height must be > 0");
        } else if (config != null) {
            source.endRecording();
            if (source.requiresHardwareAcceleration() && config != Config.HARDWARE) {
                StrictMode.noteSlowCall("GPU readback");
            }
            if (config == Config.HARDWARE || source.requiresHardwareAcceleration()) {
                RenderNode node = RenderNode.create("BitmapTemporary", null);
                node.setLeftTopRightBottom(0, 0, width, height);
                node.setClipToBounds(false);
                node.setForceDarkAllowed(false);
                RecordingCanvas canvas = node.beginRecording(width, height);
                if (!(source.getWidth() == width && source.getHeight() == height)) {
                    canvas.scale(((float) width) / ((float) source.getWidth()), ((float) height) / ((float) source.getHeight()));
                }
                canvas.drawPicture(source);
                node.endRecording();
                Bitmap bitmap = HardwareRenderer.createHardwareBitmap(node, width, height);
                if (config != Config.HARDWARE) {
                    bitmap = bitmap.copy(config, false);
                }
                return bitmap;
            }
            Bitmap bitmap2 = createBitmap(width, height, config);
            Canvas canvas2 = new Canvas(bitmap2);
            if (!(source.getWidth() == width && source.getHeight() == height)) {
                canvas2.scale(((float) width) / ((float) source.getWidth()), ((float) height) / ((float) source.getHeight()));
            }
            canvas2.drawPicture(source);
            canvas2.setBitmap(null);
            bitmap2.setImmutable();
            return bitmap2;
        } else {
            throw new IllegalArgumentException("Config must not be null");
        }
    }

    public byte[] getNinePatchChunk() {
        return this.mNinePatchChunk;
    }

    public void getOpticalInsets(Rect outInsets) {
        InsetStruct insetStruct = this.mNinePatchInsets;
        if (insetStruct == null) {
            outInsets.setEmpty();
        } else {
            outInsets.set(insetStruct.opticalRect);
        }
    }

    public InsetStruct getNinePatchInsets() {
        return this.mNinePatchInsets;
    }

    public boolean compress(CompressFormat format, int quality, OutputStream stream) {
        checkRecycled("Can't compress a recycled bitmap");
        if (stream == null) {
            throw new NullPointerException();
        } else if (quality < 0 || quality > 100) {
            throw new IllegalArgumentException("quality must be 0..100");
        } else {
            StrictMode.noteSlowCall("Compression of a bitmap is slow");
            Trace.traceBegin(8192, "Bitmap.compress");
            boolean result = nativeCompress(this.mNativePtr, format.nativeInt, quality, stream, new byte[4096]);
            Trace.traceEnd(8192);
            return result;
        }
    }

    public final boolean isMutable() {
        return nativeIsImmutable(this.mNativePtr) ^ 1;
    }

    public void setImmutable() {
        if (isMutable()) {
            nativeSetImmutable(this.mNativePtr);
        }
    }

    public final boolean isPremultiplied() {
        if (this.mRecycled) {
            Log.w(TAG, "Called isPremultiplied() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeIsPremultiplied(this.mNativePtr);
    }

    public final void setPremultiplied(boolean premultiplied) {
        checkRecycled("setPremultiplied called on a recycled bitmap");
        this.mRequestPremultiplied = premultiplied;
        nativeSetPremultiplied(this.mNativePtr, premultiplied);
    }

    public final int getWidth() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getWidth() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return this.mWidth;
    }

    public final int getHeight() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getHeight() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return this.mHeight;
    }

    public int getScaledWidth(Canvas canvas) {
        return scaleFromDensity(getWidth(), this.mDensity, canvas.mDensity);
    }

    public int getScaledHeight(Canvas canvas) {
        return scaleFromDensity(getHeight(), this.mDensity, canvas.mDensity);
    }

    public int getScaledWidth(DisplayMetrics metrics) {
        return scaleFromDensity(getWidth(), this.mDensity, metrics.densityDpi);
    }

    public int getScaledHeight(DisplayMetrics metrics) {
        return scaleFromDensity(getHeight(), this.mDensity, metrics.densityDpi);
    }

    public int getScaledWidth(int targetDensity) {
        return scaleFromDensity(getWidth(), this.mDensity, targetDensity);
    }

    public int getScaledHeight(int targetDensity) {
        return scaleFromDensity(getHeight(), this.mDensity, targetDensity);
    }

    @UnsupportedAppUsage
    public static int scaleFromDensity(int size, int sdensity, int tdensity) {
        if (sdensity == 0 || tdensity == 0 || sdensity == tdensity) {
            return size;
        }
        return ((size * tdensity) + (sdensity >> 1)) / sdensity;
    }

    public final int getRowBytes() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getRowBytes() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeRowBytes(this.mNativePtr);
    }

    public final int getByteCount() {
        if (!this.mRecycled) {
            return getRowBytes() * getHeight();
        }
        Log.w(TAG, "Called getByteCount() on a recycle()'d bitmap! This is undefined behavior!");
        return 0;
    }

    public final int getAllocationByteCount() {
        if (!this.mRecycled) {
            return nativeGetAllocationByteCount(this.mNativePtr);
        }
        Log.w(TAG, "Called getAllocationByteCount() on a recycle()'d bitmap! This is undefined behavior!");
        return 0;
    }

    public final Config getConfig() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getConfig() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return Config.nativeToConfig(nativeConfig(this.mNativePtr));
    }

    public final boolean hasAlpha() {
        if (this.mRecycled) {
            Log.w(TAG, "Called hasAlpha() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeHasAlpha(this.mNativePtr);
    }

    public void setHasAlpha(boolean hasAlpha) {
        checkRecycled("setHasAlpha called on a recycled bitmap");
        nativeSetHasAlpha(this.mNativePtr, hasAlpha, this.mRequestPremultiplied);
    }

    public final boolean hasMipMap() {
        if (this.mRecycled) {
            Log.w(TAG, "Called hasMipMap() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeHasMipMap(this.mNativePtr);
    }

    public final void setHasMipMap(boolean hasMipMap) {
        checkRecycled("setHasMipMap called on a recycled bitmap");
        nativeSetHasMipMap(this.mNativePtr, hasMipMap);
    }

    public final ColorSpace getColorSpace() {
        checkRecycled("getColorSpace called on a recycled bitmap");
        if (this.mColorSpace == null) {
            this.mColorSpace = nativeComputeColorSpace(this.mNativePtr);
        }
        return this.mColorSpace;
    }

    public void setColorSpace(ColorSpace colorSpace) {
        checkRecycled("setColorSpace called on a recycled bitmap");
        if (colorSpace == null) {
            throw new IllegalArgumentException("The colorSpace cannot be set to null");
        } else if (getConfig() != Config.ALPHA_8) {
            ColorSpace oldColorSpace = getColorSpace();
            nativeSetColorSpace(this.mNativePtr, colorSpace.getNativeInstance());
            this.mColorSpace = null;
            ColorSpace newColorSpace = getColorSpace();
            try {
                if (oldColorSpace.getComponentCount() == newColorSpace.getComponentCount()) {
                    int i = 0;
                    while (i < oldColorSpace.getComponentCount()) {
                        if (oldColorSpace.getMinValue(i) < newColorSpace.getMinValue(i)) {
                            throw new IllegalArgumentException("The new ColorSpace cannot increase the minimum value for any of the components compared to the current ColorSpace. To perform this type of conversion create a new Bitmap in the desired ColorSpace and draw this Bitmap into it.");
                        } else if (oldColorSpace.getMaxValue(i) <= newColorSpace.getMaxValue(i)) {
                            i++;
                        } else {
                            throw new IllegalArgumentException("The new ColorSpace cannot decrease the maximum value for any of the components compared to the current ColorSpace/ To perform this type of conversion create a new Bitmap in the desired ColorSpace and draw this Bitmap into it.");
                        }
                    }
                    return;
                }
                throw new IllegalArgumentException("The new ColorSpace must have the same component count as the current ColorSpace");
            } catch (IllegalArgumentException e) {
                this.mColorSpace = oldColorSpace;
                nativeSetColorSpace(this.mNativePtr, this.mColorSpace.getNativeInstance());
                throw e;
            }
        } else {
            throw new IllegalArgumentException("Cannot set a ColorSpace on ALPHA_8");
        }
    }

    public void eraseColor(int c) {
        checkRecycled("Can't erase a recycled bitmap");
        if (isMutable()) {
            nativeErase(this.mNativePtr, c);
            return;
        }
        throw new IllegalStateException("cannot erase immutable bitmaps");
    }

    public void eraseColor(long color) {
        checkRecycled("Can't erase a recycled bitmap");
        if (isMutable()) {
            nativeErase(this.mNativePtr, Color.colorSpace(color).getNativeInstance(), color);
            return;
        }
        throw new IllegalStateException("cannot erase immutable bitmaps");
    }

    public int getPixel(int x, int y) {
        checkRecycled("Can't call getPixel() on a recycled bitmap");
        checkHardware("unable to getPixel(), pixel access is not supported on Config#HARDWARE bitmaps");
        checkPixelAccess(x, y);
        return nativeGetPixel(this.mNativePtr, x, y);
    }

    private static float clamp(float value, ColorSpace cs, int index) {
        return Math.max(Math.min(value, cs.getMaxValue(index)), cs.getMinValue(index));
    }

    public Color getColor(int x, int y) {
        checkRecycled("Can't call getColor() on a recycled bitmap");
        checkHardware("unable to getColor(), pixel access is not supported on Config#HARDWARE bitmaps");
        checkPixelAccess(x, y);
        ColorSpace cs = getColorSpace();
        if (cs.equals(ColorSpace.get(Named.SRGB))) {
            return Color.valueOf(nativeGetPixel(this.mNativePtr, x, y));
        }
        long rgba = nativeGetColor(this.mNativePtr, x, y);
        float r = Half.toFloat((short) ((int) ((rgba >> 0) & 65535)));
        float g = Half.toFloat((short) ((int) ((rgba >> 16) & 65535)));
        float b = Half.toFloat((short) ((int) ((rgba >> 32) & 65535)));
        return Color.valueOf(clamp(r, cs, 0), clamp(g, cs, 1), clamp(b, cs, 2), Half.toFloat((short) ((int) (65535 & (rgba >> 48)))), cs);
    }

    public void getPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        checkRecycled("Can't call getPixels() on a recycled bitmap");
        checkHardware("unable to getPixels(), pixel access is not supported on Config#HARDWARE bitmaps");
        if (width != 0 && height != 0) {
            checkPixelsAccess(x, y, width, height, offset, stride, pixels);
            nativeGetPixels(this.mNativePtr, pixels, offset, stride, x, y, width, height);
        }
    }

    private void checkPixelAccess(int x, int y) {
        checkXYSign(x, y);
        if (x >= getWidth()) {
            throw new IllegalArgumentException("x must be < bitmap.width()");
        } else if (y >= getHeight()) {
            throw new IllegalArgumentException("y must be < bitmap.height()");
        }
    }

    private void checkPixelsAccess(int x, int y, int width, int height, int offset, int stride, int[] pixels) {
        checkXYSign(x, y);
        if (width < 0) {
            throw new IllegalArgumentException("width must be >= 0");
        } else if (height < 0) {
            throw new IllegalArgumentException("height must be >= 0");
        } else if (x + width > getWidth()) {
            throw new IllegalArgumentException("x + width must be <= bitmap.width()");
        } else if (y + height > getHeight()) {
            throw new IllegalArgumentException("y + height must be <= bitmap.height()");
        } else if (Math.abs(stride) >= width) {
            int lastScanline = ((height - 1) * stride) + offset;
            int length = pixels.length;
            if (offset < 0 || offset + width > length || lastScanline < 0 || lastScanline + width > length) {
                throw new ArrayIndexOutOfBoundsException();
            }
        } else {
            throw new IllegalArgumentException("abs(stride) must be >= width");
        }
    }

    public void setPixel(int x, int y, int color) {
        checkRecycled("Can't call setPixel() on a recycled bitmap");
        if (isMutable()) {
            checkPixelAccess(x, y);
            nativeSetPixel(this.mNativePtr, x, y, color);
            return;
        }
        throw new IllegalStateException();
    }

    public void setPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        checkRecycled("Can't call setPixels() on a recycled bitmap");
        if (!isMutable()) {
            throw new IllegalStateException();
        } else if (width != 0 && height != 0) {
            checkPixelsAccess(x, y, width, height, offset, stride, pixels);
            nativeSetPixels(this.mNativePtr, pixels, offset, stride, x, y, width, height);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
        checkRecycled("Can't parcel a recycled bitmap");
        noteHardwareBitmapSlowCall();
        if (!nativeWriteToParcel(this.mNativePtr, isMutable(), this.mDensity, p)) {
            throw new RuntimeException("native writeToParcel failed");
        }
    }

    public Bitmap extractAlpha() {
        return extractAlpha(null, null);
    }

    public Bitmap extractAlpha(Paint paint, int[] offsetXY) {
        checkRecycled("Can't extractAlpha on a recycled bitmap");
        long nativePaint = paint != null ? paint.getNativeInstance() : 0;
        noteHardwareBitmapSlowCall();
        Bitmap bm = nativeExtractAlpha(this.mNativePtr, nativePaint, offsetXY);
        if (bm != null) {
            bm.mDensity = this.mDensity;
            return bm;
        }
        throw new RuntimeException("Failed to extractAlpha on Bitmap");
    }

    public boolean sameAs(Bitmap other) {
        checkRecycled("Can't call sameAs on a recycled bitmap!");
        noteHardwareBitmapSlowCall();
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        other.noteHardwareBitmapSlowCall();
        if (!other.isRecycled()) {
            return nativeSameAs(this.mNativePtr, other.mNativePtr);
        }
        throw new IllegalArgumentException("Can't compare to a recycled bitmap!");
    }

    public void prepareToDraw() {
        checkRecycled("Can't prepareToDraw on a recycled bitmap!");
        nativePrepareToDraw(this.mNativePtr);
    }

    @UnsupportedAppUsage
    public GraphicBuffer createGraphicBufferHandle() {
        return nativeCreateGraphicBufferHandle(this.mNativePtr);
    }
}
