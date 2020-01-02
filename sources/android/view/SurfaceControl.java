package android.view;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.ColorSpace.Named;
import android.graphics.GraphicBuffer;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.display.DisplayedContentSample;
import android.hardware.display.DisplayedContentSamplingAttributes;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.proto.ProtoOutputStream;
import android.view.Display.HdrCapabilities;
import android.view.Surface.OutOfResourcesException;
import com.android.internal.annotations.GuardedBy;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
import libcore.util.NativeAllocationRegistry;

public final class SurfaceControl implements Parcelable {
    public static final Creator<SurfaceControl> CREATOR = new Creator<SurfaceControl>() {
        public SurfaceControl createFromParcel(Parcel in) {
            return new SurfaceControl(in, null);
        }

        public SurfaceControl[] newArray(int size) {
            return new SurfaceControl[size];
        }
    };
    public static final int CURSOR_WINDOW = 8192;
    private static final int FLAG_BLUR = 16;
    private static final int FLAG_RECORD_HIDE = 32;
    public static final int FX_SURFACE_CONTAINER = 524288;
    public static final int FX_SURFACE_DIM = 131072;
    public static final int FX_SURFACE_MASK = 983040;
    public static final int FX_SURFACE_NORMAL = 0;
    @UnsupportedAppUsage
    public static final int HIDDEN = 4;
    private static final int INTERNAL_DATASPACE_DISPLAY_P3 = 143261696;
    private static final int INTERNAL_DATASPACE_SCRGB = 411107328;
    private static final int INTERNAL_DATASPACE_SRGB = 142671872;
    public static final int METADATA_OWNER_UID = 1;
    public static final int METADATA_TASK_ID = 3;
    public static final int METADATA_WINDOW_TYPE = 2;
    public static final int NON_PREMULTIPLIED = 256;
    public static final int OPAQUE = 1024;
    public static final int POWER_MODE_DOZE = 1;
    public static final int POWER_MODE_DOZE_SUSPEND = 3;
    public static final int POWER_MODE_NORMAL = 2;
    public static final int POWER_MODE_OFF = 0;
    public static final int POWER_MODE_ON_SUSPEND = 4;
    public static final int PROTECTED_APP = 2048;
    public static final int SECURE = 128;
    private static final int SURFACE_HIDDEN = 1;
    private static final int SURFACE_OPAQUE = 2;
    private static final String TAG = "SurfaceControl";
    public static final int WINDOW_TYPE_DONT_SCREENSHOT = 441731;
    static Transaction sGlobalTransaction;
    static long sTransactionNestCount = 0;
    private final CloseGuard mCloseGuard;
    @GuardedBy({"mSizeLock"})
    private int mHeight;
    private String mName;
    long mNativeObject;
    private final Object mSizeLock;
    @GuardedBy({"mSizeLock"})
    private int mWidth;

    public static class Builder {
        private int mFlags = 4;
        private int mFormat = -1;
        private int mHeight;
        private SparseIntArray mMetadata;
        private String mName;
        private SurfaceControl mParent;
        private SurfaceSession mSession;
        private int mWidth;

        public Builder(SurfaceSession session) {
            this.mSession = session;
        }

        public SurfaceControl build() {
            int i = this.mWidth;
            if (i >= 0) {
                int i2 = this.mHeight;
                if (i2 >= 0) {
                    if ((i <= 0 && i2 <= 0) || (!isColorLayerSet() && !isContainerLayerSet())) {
                        return new SurfaceControl(this.mSession, this.mName, this.mWidth, this.mHeight, this.mFormat, this.mFlags, this.mParent, this.mMetadata, null);
                    }
                    throw new IllegalStateException("Only buffer layers can set a valid buffer size.");
                }
            }
            throw new IllegalStateException("width and height must be positive or unset");
        }

        public SurfaceControl build(Surface parentSurface) {
            int i = this.mWidth;
            if (i >= 0) {
                int i2 = this.mHeight;
                if (i2 >= 0) {
                    if ((i > 0 || i2 > 0) && (isColorLayerSet() || isContainerLayerSet())) {
                        throw new IllegalStateException("Only buffer layers can set a valid buffer size.");
                    }
                    return new SurfaceControl(this.mSession, this.mName, this.mWidth, this.mHeight, this.mFormat, this.mFlags, parentSurface, this.mMetadata, null);
                }
            }
            throw new IllegalStateException("width and height must be positive or unset");
        }

        public Builder setName(String name) {
            this.mName = name;
            return this;
        }

        public Builder setBufferSize(int width, int height) {
            if (width < 0 || height < 0) {
                throw new IllegalArgumentException("width and height must be positive");
            }
            this.mWidth = width;
            this.mHeight = height;
            return setFlags(0, SurfaceControl.FX_SURFACE_MASK);
        }

        private void unsetBufferSize() {
            this.mWidth = 0;
            this.mHeight = 0;
        }

        public Builder setFormat(int format) {
            this.mFormat = format;
            return this;
        }

        public Builder setProtected(boolean protectedContent) {
            if (protectedContent) {
                this.mFlags |= 2048;
            } else {
                this.mFlags &= -2049;
            }
            return this;
        }

        public Builder setSecure(boolean secure) {
            if (secure) {
                this.mFlags |= 128;
            } else {
                this.mFlags &= -129;
            }
            return this;
        }

        public Builder setOpaque(boolean opaque) {
            if (opaque) {
                this.mFlags |= 1024;
            } else {
                this.mFlags &= -1025;
            }
            return this;
        }

        public Builder setParent(SurfaceControl parent) {
            this.mParent = parent;
            return this;
        }

        public Builder setMetadata(int key, int data) {
            if (this.mMetadata == null) {
                this.mMetadata = new SparseIntArray();
            }
            this.mMetadata.put(key, data);
            return this;
        }

        public Builder setColorLayer() {
            unsetBufferSize();
            return setFlags(131072, SurfaceControl.FX_SURFACE_MASK);
        }

        private boolean isColorLayerSet() {
            return (this.mFlags & 131072) == 131072;
        }

        public Builder setContainerLayer() {
            unsetBufferSize();
            return setFlags(524288, SurfaceControl.FX_SURFACE_MASK);
        }

        private boolean isContainerLayerSet() {
            return (this.mFlags & 524288) == 524288;
        }

        public Builder setFlags(int flags) {
            this.mFlags = flags;
            return this;
        }

        private Builder setFlags(int flags, int mask) {
            this.mFlags = (this.mFlags & (~mask)) | flags;
            return this;
        }
    }

    public static final class CieXyz {
        public float X;
        public float Y;
        public float Z;
    }

    public static final class DisplayPrimaries {
        public CieXyz blue;
        public CieXyz green;
        public CieXyz red;
        public CieXyz white;
    }

    public static final class PhysicalDisplayInfo {
        @UnsupportedAppUsage
        public long appVsyncOffsetNanos;
        @UnsupportedAppUsage
        public float density;
        @UnsupportedAppUsage
        public int height;
        @UnsupportedAppUsage
        public long presentationDeadlineNanos;
        @UnsupportedAppUsage
        public float refreshRate;
        @UnsupportedAppUsage
        public boolean secure;
        @UnsupportedAppUsage
        public int width;
        @UnsupportedAppUsage
        public float xDpi;
        @UnsupportedAppUsage
        public float yDpi;

        public PhysicalDisplayInfo(PhysicalDisplayInfo other) {
            copyFrom(other);
        }

        public boolean equals(Object o) {
            return (o instanceof PhysicalDisplayInfo) && equals((PhysicalDisplayInfo) o);
        }

        public boolean equals(PhysicalDisplayInfo other) {
            return other != null && this.width == other.width && this.height == other.height && this.refreshRate == other.refreshRate && this.density == other.density && this.xDpi == other.xDpi && this.yDpi == other.yDpi && this.secure == other.secure && this.appVsyncOffsetNanos == other.appVsyncOffsetNanos && this.presentationDeadlineNanos == other.presentationDeadlineNanos;
        }

        public int hashCode() {
            return 0;
        }

        public void copyFrom(PhysicalDisplayInfo other) {
            this.width = other.width;
            this.height = other.height;
            this.refreshRate = other.refreshRate;
            this.density = other.density;
            this.xDpi = other.xDpi;
            this.yDpi = other.yDpi;
            this.secure = other.secure;
            this.appVsyncOffsetNanos = other.appVsyncOffsetNanos;
            this.presentationDeadlineNanos = other.presentationDeadlineNanos;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("PhysicalDisplayInfo{");
            stringBuilder.append(this.width);
            String str = " x ";
            stringBuilder.append(str);
            stringBuilder.append(this.height);
            String str2 = ", ";
            stringBuilder.append(str2);
            stringBuilder.append(this.refreshRate);
            stringBuilder.append(" fps, density ");
            stringBuilder.append(this.density);
            stringBuilder.append(str2);
            stringBuilder.append(this.xDpi);
            stringBuilder.append(str);
            stringBuilder.append(this.yDpi);
            stringBuilder.append(" dpi, secure ");
            stringBuilder.append(this.secure);
            stringBuilder.append(", appVsyncOffset ");
            stringBuilder.append(this.appVsyncOffsetNanos);
            stringBuilder.append(", bufferDeadline ");
            stringBuilder.append(this.presentationDeadlineNanos);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class ScreenshotGraphicBuffer {
        private final ColorSpace mColorSpace;
        private final boolean mContainsSecureLayers;
        private final GraphicBuffer mGraphicBuffer;

        public ScreenshotGraphicBuffer(GraphicBuffer graphicBuffer, ColorSpace colorSpace, boolean containsSecureLayers) {
            this.mGraphicBuffer = graphicBuffer;
            this.mColorSpace = colorSpace;
            this.mContainsSecureLayers = containsSecureLayers;
        }

        private static ScreenshotGraphicBuffer createFromNative(int width, int height, int format, int usage, long unwrappedNativeObject, int namedColorSpace, boolean containsSecureLayers) {
            return new ScreenshotGraphicBuffer(GraphicBuffer.createFromExisting(width, height, format, usage, unwrappedNativeObject), ColorSpace.get(Named.values()[namedColorSpace]), containsSecureLayers);
        }

        public ColorSpace getColorSpace() {
            return this.mColorSpace;
        }

        public GraphicBuffer getGraphicBuffer() {
            return this.mGraphicBuffer;
        }

        public boolean containsSecureLayers() {
            return this.mContainsSecureLayers;
        }
    }

    public static class Transaction implements Closeable {
        public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(Transaction.class.getClassLoader(), SurfaceControl.nativeGetNativeTransactionFinalizer(), 512);
        Runnable mFreeNativeResources = sRegistry.registerNativeAllocation(this, this.mNativeObject);
        private long mNativeObject = SurfaceControl.nativeCreateTransaction();
        private final ArrayMap<SurfaceControl, Point> mResizedSurfaces = new ArrayMap();

        public void apply() {
            apply(false);
        }

        public void close() {
            this.mFreeNativeResources.run();
            this.mNativeObject = 0;
        }

        public void apply(boolean sync) {
            applyResizedSurfaces();
            SurfaceControl.nativeApplyTransaction(this.mNativeObject, sync);
        }

        private void applyResizedSurfaces() {
            for (int i = this.mResizedSurfaces.size() - 1; i >= 0; i--) {
                Point size = (Point) this.mResizedSurfaces.valueAt(i);
                SurfaceControl surfaceControl = (SurfaceControl) this.mResizedSurfaces.keyAt(i);
                synchronized (surfaceControl.mSizeLock) {
                    surfaceControl.mWidth = size.x;
                    surfaceControl.mHeight = size.y;
                }
            }
            this.mResizedSurfaces.clear();
        }

        public Transaction setVisibility(SurfaceControl sc, boolean visible) {
            sc.checkNotReleased();
            if (visible) {
                return show(sc);
            }
            return hide(sc);
        }

        @UnsupportedAppUsage
        public Transaction show(SurfaceControl sc) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetFlags(this.mNativeObject, sc.mNativeObject, 0, 1);
            return this;
        }

        @UnsupportedAppUsage
        public Transaction hide(SurfaceControl sc) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetFlags(this.mNativeObject, sc.mNativeObject, 1, 1);
            return this;
        }

        @UnsupportedAppUsage
        public Transaction setPosition(SurfaceControl sc, float x, float y) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetPosition(this.mNativeObject, sc.mNativeObject, x, y);
            return this;
        }

        public Transaction setBufferSize(SurfaceControl sc, int w, int h) {
            sc.checkNotReleased();
            this.mResizedSurfaces.put(sc, new Point(w, h));
            SurfaceControl.nativeSetSize(this.mNativeObject, sc.mNativeObject, w, h);
            return this;
        }

        public Transaction setLayer(SurfaceControl sc, int z) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetLayer(this.mNativeObject, sc.mNativeObject, z);
            return this;
        }

        public Transaction setRelativeLayer(SurfaceControl sc, SurfaceControl relativeTo, int z) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetRelativeLayer(this.mNativeObject, sc.mNativeObject, relativeTo.getHandle(), z);
            return this;
        }

        public Transaction setTransparentRegionHint(SurfaceControl sc, Region transparentRegion) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetTransparentRegionHint(this.mNativeObject, sc.mNativeObject, transparentRegion);
            return this;
        }

        public Transaction setAlpha(SurfaceControl sc, float alpha) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetAlpha(this.mNativeObject, sc.mNativeObject, alpha);
            return this;
        }

        public Transaction setInputWindowInfo(SurfaceControl sc, InputWindowHandle handle) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetInputWindowInfo(this.mNativeObject, sc.mNativeObject, handle);
            return this;
        }

        public Transaction transferTouchFocus(IBinder fromToken, IBinder toToken) {
            SurfaceControl.nativeTransferTouchFocus(this.mNativeObject, fromToken, toToken);
            return this;
        }

        public Transaction syncInputWindows() {
            SurfaceControl.nativeSyncInputWindows(this.mNativeObject);
            return this;
        }

        public Transaction setGeometry(SurfaceControl sc, Rect sourceCrop, Rect destFrame, int orientation) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetGeometry(this.mNativeObject, sc.mNativeObject, sourceCrop, destFrame, (long) orientation);
            return this;
        }

        @UnsupportedAppUsage
        public Transaction setMatrix(SurfaceControl sc, float dsdx, float dtdx, float dtdy, float dsdy) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetMatrix(this.mNativeObject, sc.mNativeObject, dsdx, dtdx, dtdy, dsdy);
            return this;
        }

        @UnsupportedAppUsage
        public Transaction setMatrix(SurfaceControl sc, Matrix matrix, float[] float9) {
            matrix.getValues(float9);
            setMatrix(sc, float9[0], float9[3], float9[1], float9[4]);
            setPosition(sc, float9[2], float9[5]);
            return this;
        }

        public Transaction setColorTransform(SurfaceControl sc, float[] matrix, float[] translation) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetColorTransform(this.mNativeObject, sc.mNativeObject, matrix, translation);
            return this;
        }

        public Transaction setColorSpaceAgnostic(SurfaceControl sc, boolean agnostic) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetColorSpaceAgnostic(this.mNativeObject, sc.mNativeObject, agnostic);
            return this;
        }

        @UnsupportedAppUsage
        public Transaction setWindowCrop(SurfaceControl sc, Rect crop) {
            SurfaceControl surfaceControl = sc;
            Rect rect = crop;
            sc.checkNotReleased();
            if (rect != null) {
                SurfaceControl.nativeSetWindowCrop(this.mNativeObject, surfaceControl.mNativeObject, rect.left, rect.top, rect.right, rect.bottom);
            } else {
                SurfaceControl.nativeSetWindowCrop(this.mNativeObject, surfaceControl.mNativeObject, 0, 0, 0, 0);
            }
            return this;
        }

        public Transaction setWindowCrop(SurfaceControl sc, int width, int height) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetWindowCrop(this.mNativeObject, sc.mNativeObject, 0, 0, width, height);
            return this;
        }

        @UnsupportedAppUsage
        public Transaction setCornerRadius(SurfaceControl sc, float cornerRadius) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetCornerRadius(this.mNativeObject, sc.mNativeObject, cornerRadius);
            return this;
        }

        @UnsupportedAppUsage(maxTargetSdk = 26)
        public Transaction setLayerStack(SurfaceControl sc, int layerStack) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetLayerStack(this.mNativeObject, sc.mNativeObject, layerStack);
            return this;
        }

        @UnsupportedAppUsage
        public Transaction deferTransactionUntil(SurfaceControl sc, IBinder handle, long frameNumber) {
            if (frameNumber < 0) {
                return this;
            }
            sc.checkNotReleased();
            SurfaceControl.nativeDeferTransactionUntil(this.mNativeObject, sc.mNativeObject, handle, frameNumber);
            return this;
        }

        @UnsupportedAppUsage
        public Transaction deferTransactionUntilSurface(SurfaceControl sc, Surface barrierSurface, long frameNumber) {
            if (frameNumber < 0) {
                return this;
            }
            sc.checkNotReleased();
            SurfaceControl.nativeDeferTransactionUntilSurface(this.mNativeObject, sc.mNativeObject, barrierSurface.mNativeObject, frameNumber);
            return this;
        }

        public Transaction reparentChildren(SurfaceControl sc, IBinder newParentHandle) {
            sc.checkNotReleased();
            SurfaceControl.nativeReparentChildren(this.mNativeObject, sc.mNativeObject, newParentHandle);
            return this;
        }

        public Transaction reparent(SurfaceControl sc, SurfaceControl newParent) {
            sc.checkNotReleased();
            long otherObject = 0;
            if (newParent != null) {
                newParent.checkNotReleased();
                otherObject = newParent.mNativeObject;
            }
            SurfaceControl.nativeReparent(this.mNativeObject, sc.mNativeObject, otherObject);
            return this;
        }

        public Transaction detachChildren(SurfaceControl sc) {
            sc.checkNotReleased();
            SurfaceControl.nativeSeverChildren(this.mNativeObject, sc.mNativeObject);
            return this;
        }

        public Transaction setOverrideScalingMode(SurfaceControl sc, int overrideScalingMode) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetOverrideScalingMode(this.mNativeObject, sc.mNativeObject, overrideScalingMode);
            return this;
        }

        @UnsupportedAppUsage
        public Transaction setColor(SurfaceControl sc, float[] color) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetColor(this.mNativeObject, sc.mNativeObject, color);
            return this;
        }

        public Transaction setGeometryAppliesWithResize(SurfaceControl sc) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetGeometryAppliesWithResize(this.mNativeObject, sc.mNativeObject);
            return this;
        }

        public Transaction setSecure(SurfaceControl sc, boolean isSecure) {
            sc.checkNotReleased();
            if (isSecure) {
                SurfaceControl.nativeSetFlags(this.mNativeObject, sc.mNativeObject, 128, 128);
            } else {
                SurfaceControl.nativeSetFlags(this.mNativeObject, sc.mNativeObject, 0, 128);
            }
            return this;
        }

        public Transaction setOpaque(SurfaceControl sc, boolean isOpaque) {
            sc.checkNotReleased();
            if (isOpaque) {
                SurfaceControl.nativeSetFlags(this.mNativeObject, sc.mNativeObject, 2, 2);
            } else {
                SurfaceControl.nativeSetFlags(this.mNativeObject, sc.mNativeObject, 0, 2);
            }
            return this;
        }

        public Transaction setDisplaySurface(IBinder displayToken, Surface surface) {
            if (displayToken != null) {
                if (surface != null) {
                    synchronized (surface.mLock) {
                        SurfaceControl.nativeSetDisplaySurface(this.mNativeObject, displayToken, surface.mNativeObject);
                    }
                } else {
                    SurfaceControl.nativeSetDisplaySurface(this.mNativeObject, displayToken, 0);
                }
                return this;
            }
            throw new IllegalArgumentException("displayToken must not be null");
        }

        public Transaction setDisplayLayerStack(IBinder displayToken, int layerStack) {
            if (displayToken != null) {
                SurfaceControl.nativeSetDisplayLayerStack(this.mNativeObject, displayToken, layerStack);
                return this;
            }
            throw new IllegalArgumentException("displayToken must not be null");
        }

        public Transaction setDisplayProjection(IBinder displayToken, int orientation, Rect layerStackRect, Rect displayRect) {
            Rect rect = layerStackRect;
            Rect rect2 = displayRect;
            if (displayToken == null) {
                throw new IllegalArgumentException("displayToken must not be null");
            } else if (rect == null) {
                throw new IllegalArgumentException("layerStackRect must not be null");
            } else if (rect2 != null) {
                SurfaceControl.nativeSetDisplayProjection(this.mNativeObject, displayToken, orientation, rect.left, rect.top, rect.right, rect.bottom, rect2.left, rect2.top, rect2.right, rect2.bottom);
                return this;
            } else {
                throw new IllegalArgumentException("displayRect must not be null");
            }
        }

        public Transaction setDisplaySize(IBinder displayToken, int width, int height) {
            if (displayToken == null) {
                throw new IllegalArgumentException("displayToken must not be null");
            } else if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("width and height must be positive");
            } else {
                SurfaceControl.nativeSetDisplaySize(this.mNativeObject, displayToken, width, height);
                return this;
            }
        }

        public Transaction setAnimationTransaction() {
            SurfaceControl.nativeSetAnimationTransaction(this.mNativeObject);
            return this;
        }

        public Transaction setEarlyWakeup() {
            SurfaceControl.nativeSetEarlyWakeup(this.mNativeObject);
            return this;
        }

        public Transaction setMetadata(SurfaceControl sc, int key, int data) {
            Parcel parcel = Parcel.obtain();
            parcel.writeInt(data);
            try {
                setMetadata(sc, key, parcel);
                return this;
            } finally {
                parcel.recycle();
            }
        }

        public Transaction setMetadata(SurfaceControl sc, int key, Parcel data) {
            SurfaceControl.nativeSetMetadata(this.mNativeObject, sc.mNativeObject, key, data);
            return this;
        }

        public Transaction merge(Transaction other) {
            if (this == other) {
                return this;
            }
            this.mResizedSurfaces.putAll(other.mResizedSurfaces);
            other.mResizedSurfaces.clear();
            SurfaceControl.nativeMergeTransaction(this.mNativeObject, other.mNativeObject);
            return this;
        }

        public Transaction remove(SurfaceControl sc) {
            reparent(sc, null);
            sc.release();
            return this;
        }

        public Transaction setBlur(SurfaceControl sc, boolean isBlur) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetFlags(this.mNativeObject, sc.mNativeObject, isBlur ? 16 : 0, 16);
            return this;
        }

        public Transaction setBlurRatio(SurfaceControl sc, float blurRatio) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetBlurRatio(this.mNativeObject, sc.mNativeObject, blurRatio);
            return this;
        }

        public Transaction setBlurMode(SurfaceControl sc, int blurMode) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetBlurMode(this.mNativeObject, sc.mNativeObject, blurMode);
            return this;
        }

        public Transaction setRecordHide(SurfaceControl sc, boolean enable) {
            sc.checkNotReleased();
            SurfaceControl.nativeSetFlags(this.mNativeObject, sc.mNativeObject, enable ? 32 : 0, 32);
            return this;
        }
    }

    private static native void nativeApplyTransaction(long j, boolean z);

    private static native ScreenshotGraphicBuffer nativeCaptureLayers(IBinder iBinder, IBinder iBinder2, Rect rect, float f, IBinder[] iBinderArr);

    private static native boolean nativeClearAnimationFrameStats();

    private static native boolean nativeClearContentFrameStats(long j);

    private static native long nativeCopyFromSurfaceControl(long j);

    private static native long nativeCreate(SurfaceSession surfaceSession, String str, int i, int i2, int i3, int i4, long j, Parcel parcel) throws OutOfResourcesException;

    private static native IBinder nativeCreateDisplay(String str, boolean z);

    private static native long nativeCreateTransaction();

    private static native long nativeCreatewithParentSurface(SurfaceSession surfaceSession, String str, int i, int i2, int i3, int i4, long j, Parcel parcel) throws OutOfResourcesException;

    private static native void nativeDeferTransactionUntil(long j, long j2, IBinder iBinder, long j3);

    private static native void nativeDeferTransactionUntilSurface(long j, long j2, long j3, long j4);

    private static native void nativeDestroy(long j);

    private static native void nativeDestroyDisplay(IBinder iBinder);

    private static native void nativeDisconnect(long j);

    private static native int nativeGetActiveColorMode(IBinder iBinder);

    private static native int nativeGetActiveConfig(IBinder iBinder);

    private static native int[] nativeGetAllowedDisplayConfigs(IBinder iBinder);

    private static native boolean nativeGetAnimationFrameStats(WindowAnimationFrameStats windowAnimationFrameStats);

    private static native int[] nativeGetCompositionDataspaces();

    private static native boolean nativeGetContentFrameStats(long j, WindowContentFrameStats windowContentFrameStats);

    private static native boolean nativeGetDisplayBrightnessSupport(IBinder iBinder);

    private static native int[] nativeGetDisplayColorModes(IBinder iBinder);

    private static native PhysicalDisplayInfo[] nativeGetDisplayConfigs(IBinder iBinder);

    private static native DisplayPrimaries nativeGetDisplayNativePrimaries(IBinder iBinder);

    private static native DisplayedContentSample nativeGetDisplayedContentSample(IBinder iBinder, long j, long j2);

    private static native DisplayedContentSamplingAttributes nativeGetDisplayedContentSamplingAttributes(IBinder iBinder);

    private static native IBinder nativeGetHandle(long j);

    private static native HdrCapabilities nativeGetHdrCapabilities(IBinder iBinder);

    private static native long nativeGetNativeTransactionFinalizer();

    private static native long[] nativeGetPhysicalDisplayIds();

    private static native IBinder nativeGetPhysicalDisplayToken(long j);

    private static native boolean nativeGetProtectedContentSupport();

    private static native boolean nativeGetTransformToDisplayInverse(long j);

    private static native void nativeMergeTransaction(long j, long j2);

    private static native long nativeReadFromParcel(Parcel parcel);

    private static native void nativeRelease(long j);

    private static native void nativeReparent(long j, long j2, long j3);

    private static native void nativeReparentChildren(long j, long j2, IBinder iBinder);

    private static native ScreenshotGraphicBuffer nativeScreenshot(IBinder iBinder, Rect rect, int i, int i2, boolean z, int i3, boolean z2);

    private static native boolean nativeSetActiveColorMode(IBinder iBinder, int i);

    private static native boolean nativeSetActiveConfig(IBinder iBinder, int i);

    private static native boolean nativeSetAllowedDisplayConfigs(IBinder iBinder, int[] iArr);

    private static native void nativeSetAlpha(long j, long j2, float f);

    private static native void nativeSetAnimationTransaction(long j);

    private static native void nativeSetBlurMode(long j, long j2, int i);

    private static native void nativeSetBlurRatio(long j, long j2, float f);

    private static native void nativeSetColor(long j, long j2, float[] fArr);

    private static native void nativeSetColorSpaceAgnostic(long j, long j2, boolean z);

    private static native void nativeSetColorTransform(long j, long j2, float[] fArr, float[] fArr2);

    private static native void nativeSetCornerRadius(long j, long j2, float f);

    private static native boolean nativeSetDisplayBrightness(IBinder iBinder, float f);

    private static native void nativeSetDisplayLayerStack(long j, IBinder iBinder, int i);

    private static native void nativeSetDisplayPowerMode(IBinder iBinder, int i);

    private static native void nativeSetDisplayProjection(long j, IBinder iBinder, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    private static native void nativeSetDisplaySize(long j, IBinder iBinder, int i, int i2);

    private static native void nativeSetDisplaySurface(long j, IBinder iBinder, long j2);

    private static native boolean nativeSetDisplayedContentSamplingEnabled(IBinder iBinder, boolean z, int i, int i2);

    private static native void nativeSetEarlyWakeup(long j);

    private static native void nativeSetFlags(long j, long j2, int i, int i2);

    private static native void nativeSetGeometry(long j, long j2, Rect rect, Rect rect2, long j3);

    private static native void nativeSetGeometryAppliesWithResize(long j, long j2);

    private static native void nativeSetInputWindowInfo(long j, long j2, InputWindowHandle inputWindowHandle);

    private static native void nativeSetLayer(long j, long j2, int i);

    private static native void nativeSetLayerStack(long j, long j2, int i);

    private static native void nativeSetMatrix(long j, long j2, float f, float f2, float f3, float f4);

    private static native void nativeSetMetadata(long j, long j2, int i, Parcel parcel);

    private static native void nativeSetOverrideScalingMode(long j, long j2, int i);

    private static native void nativeSetPosition(long j, long j2, float f, float f2);

    private static native void nativeSetRelativeLayer(long j, long j2, IBinder iBinder, int i);

    private static native void nativeSetSize(long j, long j2, int i, int i2);

    private static native void nativeSetTransparentRegionHint(long j, long j2, Region region);

    private static native void nativeSetWindowCrop(long j, long j2, int i, int i2, int i3, int i4);

    private static native void nativeSeverChildren(long j, long j2);

    private static native void nativeSyncInputWindows(long j);

    private static native void nativeTransferTouchFocus(long j, IBinder iBinder, IBinder iBinder2);

    private static native void nativeWriteToParcel(long j, Parcel parcel);

    /* synthetic */ SurfaceControl(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    private void assignNativeObject(long nativeObject) {
        if (this.mNativeObject != 0) {
            release();
        }
        this.mNativeObject = nativeObject;
    }

    public void copyFrom(SurfaceControl other) {
        this.mName = other.mName;
        this.mWidth = other.mWidth;
        this.mHeight = other.mHeight;
        assignNativeObject(nativeCopyFromSurfaceControl(other.mNativeObject));
    }

    private SurfaceControl(SurfaceSession session, String name, int w, int h, int format, int flags, SurfaceControl parent, SparseIntArray metadata) throws OutOfResourcesException, IllegalArgumentException {
        Throwable th;
        Parcel metaParcel;
        String str = name;
        SurfaceControl surfaceControl = parent;
        SparseIntArray sparseIntArray = metadata;
        this.mCloseGuard = CloseGuard.get();
        this.mSizeLock = new Object();
        if (str != null) {
            if ((flags & 4) == 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Surfaces should always be created with the HIDDEN flag set to ensure that they are not made visible prematurely before all of the surface's properties have been configured.  Set the other properties and make the surface visible within a transaction.  New surface name: ");
                stringBuilder.append(str);
                Log.w(TAG, stringBuilder.toString(), new Throwable());
            }
            this.mName = str;
            this.mWidth = w;
            this.mHeight = h;
            Parcel metaParcel2 = Parcel.obtain();
            if (sparseIntArray != null) {
                try {
                    if (metadata.size() > 0) {
                        metaParcel2.writeInt(metadata.size());
                        for (int i = 0; i < metadata.size(); i++) {
                            metaParcel2.writeInt(sparseIntArray.keyAt(i));
                            metaParcel2.writeByteArray(ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(sparseIntArray.valueAt(i)).array());
                        }
                        metaParcel2.setDataPosition(0);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    metaParcel = metaParcel2;
                    metaParcel.recycle();
                    throw th;
                }
            }
            metaParcel = metaParcel2;
            try {
                this.mNativeObject = nativeCreate(session, name, w, h, format, flags, surfaceControl != null ? surfaceControl.mNativeObject : 0, metaParcel2);
                metaParcel.recycle();
                if (this.mNativeObject != 0) {
                    this.mCloseGuard.open("release");
                    return;
                }
                throw new OutOfResourcesException("Couldn't allocate SurfaceControl native object");
            } catch (Throwable th3) {
                th = th3;
                metaParcel.recycle();
                throw th;
            }
        }
        int i2 = w;
        int i3 = h;
        throw new IllegalArgumentException("name must not be null");
    }

    private SurfaceControl(SurfaceSession session, String name, int w, int h, int format, int flags, Surface parentSurface, SparseIntArray metadata) throws OutOfResourcesException, IllegalArgumentException {
        Throwable th;
        Parcel metaParcel;
        String str = name;
        Surface surface = parentSurface;
        SparseIntArray sparseIntArray = metadata;
        this.mCloseGuard = CloseGuard.get();
        this.mSizeLock = new Object();
        if (str != null) {
            if ((flags & 4) == 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Surfaces should always be created with the HIDDEN flag set to ensure that they are not made visible prematurely before all of the surface's properties have been configured.  Set the other properties and make the surface visible within a transaction.  New surface name: ");
                stringBuilder.append(str);
                Log.w(TAG, stringBuilder.toString(), new Throwable());
            }
            this.mName = str;
            this.mWidth = w;
            this.mHeight = h;
            Parcel metaParcel2 = Parcel.obtain();
            if (sparseIntArray != null) {
                try {
                    if (metadata.size() > 0) {
                        metaParcel2.writeInt(metadata.size());
                        for (int i = 0; i < metadata.size(); i++) {
                            metaParcel2.writeInt(sparseIntArray.keyAt(i));
                            metaParcel2.writeByteArray(ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(sparseIntArray.valueAt(i)).array());
                        }
                        metaParcel2.setDataPosition(0);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    metaParcel = metaParcel2;
                    metaParcel.recycle();
                    throw th;
                }
            }
            metaParcel = metaParcel2;
            try {
                this.mNativeObject = nativeCreatewithParentSurface(session, name, w, h, format, flags, surface != null ? surface.mNativeObject : 0, metaParcel2);
                metaParcel.recycle();
                if (this.mNativeObject != 0) {
                    this.mCloseGuard.open("release");
                    return;
                }
                throw new OutOfResourcesException("Couldn't allocate SurfaceControl native object");
            } catch (Throwable th3) {
                th = th3;
                metaParcel.recycle();
                throw th;
            }
        }
        int i2 = w;
        int i3 = h;
        throw new IllegalArgumentException("name must not be null");
    }

    public SurfaceControl(SurfaceControl other) {
        this.mCloseGuard = CloseGuard.get();
        this.mSizeLock = new Object();
        this.mName = other.mName;
        this.mWidth = other.mWidth;
        this.mHeight = other.mHeight;
        this.mNativeObject = other.mNativeObject;
        other.mCloseGuard.close();
        other.mNativeObject = 0;
        this.mCloseGuard.open("release");
    }

    private SurfaceControl(Parcel in) {
        this.mCloseGuard = CloseGuard.get();
        this.mSizeLock = new Object();
        readFromParcel(in);
        this.mCloseGuard.open("release");
    }

    public SurfaceControl() {
        this.mCloseGuard = CloseGuard.get();
        this.mSizeLock = new Object();
        this.mCloseGuard.open("release");
    }

    public void readFromParcel(Parcel in) {
        if (in != null) {
            this.mName = in.readString();
            this.mWidth = in.readInt();
            this.mHeight = in.readInt();
            long object = 0;
            if (in.readInt() != 0) {
                object = nativeReadFromParcel(in);
            }
            assignNativeObject(object);
            return;
        }
        throw new IllegalArgumentException("source must not be null");
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeInt(this.mWidth);
        dest.writeInt(this.mHeight);
        if (this.mNativeObject == 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
        }
        nativeWriteToParcel(this.mNativeObject, dest);
        if ((flags & 1) != 0) {
            release();
        }
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, System.identityHashCode(this));
        proto.write(1138166333442L, this.mName);
        proto.end(token);
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            if (this.mNativeObject != 0) {
                nativeRelease(this.mNativeObject);
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public void release() {
        long j = this.mNativeObject;
        if (j != 0) {
            nativeRelease(j);
            this.mNativeObject = 0;
        }
        this.mCloseGuard.close();
    }

    public void remove() {
        long j = this.mNativeObject;
        if (j != 0) {
            nativeDestroy(j);
            this.mNativeObject = 0;
        }
        this.mCloseGuard.close();
    }

    public void disconnect() {
        long j = this.mNativeObject;
        if (j != 0) {
            nativeDisconnect(j);
        }
    }

    private void checkNotReleased() {
        if (this.mNativeObject == 0) {
            throw new NullPointerException("mNativeObject is null. Have you called release() already?");
        }
    }

    public boolean isValid() {
        return this.mNativeObject != 0;
    }

    @UnsupportedAppUsage
    public static void openTransaction() {
        synchronized (SurfaceControl.class) {
            if (sGlobalTransaction == null) {
                sGlobalTransaction = new Transaction();
            }
            synchronized (SurfaceControl.class) {
                sTransactionNestCount++;
            }
        }
    }

    @Deprecated
    public static void mergeToGlobalTransaction(Transaction t) {
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.merge(t);
        }
    }

    @UnsupportedAppUsage
    public static void closeTransaction() {
        synchronized (SurfaceControl.class) {
            if (sTransactionNestCount == 0) {
                Log.e(TAG, "Call to SurfaceControl.closeTransaction without matching openTransaction");
            } else {
                long j = sTransactionNestCount - 1;
                sTransactionNestCount = j;
                if (j > 0) {
                    return;
                }
            }
            sGlobalTransaction.apply();
        }
    }

    public void deferTransactionUntil(IBinder handle, long frame) {
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.deferTransactionUntil(this, handle, frame);
        }
    }

    public void deferTransactionUntil(Surface barrier, long frame) {
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.deferTransactionUntilSurface(this, barrier, frame);
        }
    }

    public void reparentChildren(IBinder newParentHandle) {
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.reparentChildren(this, newParentHandle);
        }
    }

    public void reparent(SurfaceControl newParent) {
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.reparent(this, newParent);
        }
    }

    public void detachChildren() {
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.detachChildren(this);
        }
    }

    public void setOverrideScalingMode(int scalingMode) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setOverrideScalingMode(this, scalingMode);
        }
    }

    public IBinder getHandle() {
        return nativeGetHandle(this.mNativeObject);
    }

    public static void setAnimationTransaction() {
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setAnimationTransaction();
        }
    }

    @UnsupportedAppUsage
    public void setLayer(int zorder) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setLayer(this, zorder);
        }
    }

    public void setRelativeLayer(SurfaceControl relativeTo, int zorder) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setRelativeLayer(this, relativeTo, zorder);
        }
    }

    @UnsupportedAppUsage
    public void setPosition(float x, float y) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setPosition(this, x, y);
        }
    }

    public void setGeometryAppliesWithResize() {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setGeometryAppliesWithResize(this);
        }
    }

    public void setBufferSize(int w, int h) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setBufferSize(this, w, h);
        }
    }

    @UnsupportedAppUsage
    public void hide() {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.hide(this);
        }
    }

    @UnsupportedAppUsage
    public void show() {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.show(this);
        }
    }

    public void setTransparentRegionHint(Region region) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setTransparentRegionHint(this, region);
        }
    }

    public boolean clearContentFrameStats() {
        checkNotReleased();
        return nativeClearContentFrameStats(this.mNativeObject);
    }

    public boolean getContentFrameStats(WindowContentFrameStats outStats) {
        checkNotReleased();
        return nativeGetContentFrameStats(this.mNativeObject, outStats);
    }

    public static boolean clearAnimationFrameStats() {
        return nativeClearAnimationFrameStats();
    }

    public static boolean getAnimationFrameStats(WindowAnimationFrameStats outStats) {
        return nativeGetAnimationFrameStats(outStats);
    }

    public void setAlpha(float alpha) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setAlpha(this, alpha);
        }
    }

    public void setColor(float[] color) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setColor(this, color);
        }
    }

    public void setMatrix(float dsdx, float dtdx, float dtdy, float dsdy) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setMatrix(this, dsdx, dtdx, dtdy, dsdy);
        }
    }

    public void setMatrix(Matrix matrix, float[] float9) {
        checkNotReleased();
        matrix.getValues(float9);
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setMatrix(this, float9[0], float9[3], float9[1], float9[4]);
            sGlobalTransaction.setPosition(this, float9[2], float9[5]);
        }
    }

    public void setColorTransform(float[] matrix, float[] translation) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setColorTransform(this, matrix, translation);
        }
    }

    public void setColorSpaceAgnostic(boolean agnostic) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setColorSpaceAgnostic(this, agnostic);
        }
    }

    public void setWindowCrop(Rect crop) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setWindowCrop(this, crop);
        }
    }

    public void setWindowCrop(int width, int height) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setWindowCrop(this, width, height);
        }
    }

    public void setCornerRadius(float cornerRadius) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setCornerRadius(this, cornerRadius);
        }
    }

    public void setLayerStack(int layerStack) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setLayerStack(this, layerStack);
        }
    }

    public void setOpaque(boolean isOpaque) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setOpaque(this, isOpaque);
        }
    }

    public void setSecure(boolean isSecure) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setSecure(this, isSecure);
        }
    }

    public int getWidth() {
        int i;
        synchronized (this.mSizeLock) {
            i = this.mWidth;
        }
        return i;
    }

    public int getHeight() {
        int i;
        synchronized (this.mSizeLock) {
            i = this.mHeight;
        }
        return i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Surface(name=");
        stringBuilder.append(this.mName);
        stringBuilder.append(")/@0x");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        return stringBuilder.toString();
    }

    public static void setDisplayPowerMode(IBinder displayToken, int mode) {
        if (displayToken != null) {
            nativeSetDisplayPowerMode(displayToken, mode);
            return;
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    @UnsupportedAppUsage
    public static PhysicalDisplayInfo[] getDisplayConfigs(IBinder displayToken) {
        if (displayToken != null) {
            return nativeGetDisplayConfigs(displayToken);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static int getActiveConfig(IBinder displayToken) {
        if (displayToken != null) {
            return nativeGetActiveConfig(displayToken);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static DisplayedContentSamplingAttributes getDisplayedContentSamplingAttributes(IBinder displayToken) {
        if (displayToken != null) {
            return nativeGetDisplayedContentSamplingAttributes(displayToken);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static boolean setDisplayedContentSamplingEnabled(IBinder displayToken, boolean enable, int componentMask, int maxFrames) {
        if (displayToken == null) {
            throw new IllegalArgumentException("displayToken must not be null");
        } else if ((componentMask >> 4) == 0) {
            return nativeSetDisplayedContentSamplingEnabled(displayToken, enable, componentMask, maxFrames);
        } else {
            throw new IllegalArgumentException("invalid componentMask when enabling sampling");
        }
    }

    public static DisplayedContentSample getDisplayedContentSample(IBinder displayToken, long maxFrames, long timestamp) {
        if (displayToken != null) {
            return nativeGetDisplayedContentSample(displayToken, maxFrames, timestamp);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static boolean setActiveConfig(IBinder displayToken, int id) {
        if (displayToken != null) {
            return nativeSetActiveConfig(displayToken, id);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static boolean setAllowedDisplayConfigs(IBinder displayToken, int[] allowedConfigs) {
        if (displayToken == null) {
            throw new IllegalArgumentException("displayToken must not be null");
        } else if (allowedConfigs != null) {
            return nativeSetAllowedDisplayConfigs(displayToken, allowedConfigs);
        } else {
            throw new IllegalArgumentException("allowedConfigs must not be null");
        }
    }

    public static int[] getAllowedDisplayConfigs(IBinder displayToken) {
        if (displayToken != null) {
            return nativeGetAllowedDisplayConfigs(displayToken);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static int[] getDisplayColorModes(IBinder displayToken) {
        if (displayToken != null) {
            return nativeGetDisplayColorModes(displayToken);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static DisplayPrimaries getDisplayNativePrimaries(IBinder displayToken) {
        if (displayToken != null) {
            return nativeGetDisplayNativePrimaries(displayToken);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static int getActiveColorMode(IBinder displayToken) {
        if (displayToken != null) {
            return nativeGetActiveColorMode(displayToken);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static boolean setActiveColorMode(IBinder displayToken, int colorMode) {
        if (displayToken != null) {
            return nativeSetActiveColorMode(displayToken, colorMode);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static ColorSpace[] getCompositionColorSpaces() {
        int[] dataspaces = nativeGetCompositionDataspaces();
        ColorSpace srgb = ColorSpace.get(Named.SRGB);
        ColorSpace[] colorSpaces = new ColorSpace[]{srgb, srgb};
        if (dataspaces.length == 2) {
            for (int i = 0; i < 2; i++) {
                int i2 = dataspaces[i];
                if (i2 == INTERNAL_DATASPACE_DISPLAY_P3) {
                    colorSpaces[i] = ColorSpace.get(Named.DISPLAY_P3);
                } else if (i2 == INTERNAL_DATASPACE_SCRGB) {
                    colorSpaces[i] = ColorSpace.get(Named.EXTENDED_SRGB);
                }
            }
        }
        return colorSpaces;
    }

    @UnsupportedAppUsage
    public static void setDisplayProjection(IBinder displayToken, int orientation, Rect layerStackRect, Rect displayRect) {
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setDisplayProjection(displayToken, orientation, layerStackRect, displayRect);
        }
    }

    @UnsupportedAppUsage
    public static void setDisplayLayerStack(IBinder displayToken, int layerStack) {
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setDisplayLayerStack(displayToken, layerStack);
        }
    }

    @UnsupportedAppUsage
    public static void setDisplaySurface(IBinder displayToken, Surface surface) {
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setDisplaySurface(displayToken, surface);
        }
    }

    public static void setDisplaySize(IBinder displayToken, int width, int height) {
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setDisplaySize(displayToken, width, height);
        }
    }

    public static HdrCapabilities getHdrCapabilities(IBinder displayToken) {
        if (displayToken != null) {
            return nativeGetHdrCapabilities(displayToken);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    @UnsupportedAppUsage
    public static IBinder createDisplay(String name, boolean secure) {
        if (name != null) {
            return nativeCreateDisplay(name, secure);
        }
        throw new IllegalArgumentException("name must not be null");
    }

    @UnsupportedAppUsage
    public static void destroyDisplay(IBinder displayToken) {
        if (displayToken != null) {
            nativeDestroyDisplay(displayToken);
            return;
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static long[] getPhysicalDisplayIds() {
        return nativeGetPhysicalDisplayIds();
    }

    public static IBinder getPhysicalDisplayToken(long physicalDisplayId) {
        return nativeGetPhysicalDisplayToken(physicalDisplayId);
    }

    public static IBinder getInternalDisplayToken() {
        long[] physicalDisplayIds = getPhysicalDisplayIds();
        if (physicalDisplayIds.length == 0) {
            return null;
        }
        return getPhysicalDisplayToken(physicalDisplayIds[0]);
    }

    public static void screenshot(IBinder display, Surface consumer) {
        screenshot(display, consumer, new Rect(), 0, 0, false, 0);
    }

    public static void screenshot(IBinder display, Surface consumer, Rect sourceCrop, int width, int height, boolean useIdentityTransform, int rotation) {
        if (consumer != null) {
            try {
                consumer.attachAndQueueBuffer(screenshotToBuffer(display, sourceCrop, width, height, useIdentityTransform, rotation).getGraphicBuffer());
                return;
            } catch (RuntimeException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to take screenshot - ");
                stringBuilder.append(e.getMessage());
                Log.w(TAG, stringBuilder.toString());
                return;
            }
        }
        throw new IllegalArgumentException("consumer must not be null");
    }

    @UnsupportedAppUsage
    public static Bitmap screenshot(Rect sourceCrop, int width, int height, int rotation) {
        return screenshot(sourceCrop, width, height, false, rotation);
    }

    @UnsupportedAppUsage
    public static Bitmap screenshot(Rect sourceCrop, int width, int height, boolean useIdentityTransform, int rotation) {
        IBinder displayToken = getInternalDisplayToken();
        String str = TAG;
        if (displayToken == null) {
            Log.w(str, "Failed to take screenshot because internal display is disconnected");
            return null;
        }
        int i = 3;
        if (rotation == 1 || rotation == 3) {
            if (rotation != 1) {
                i = 1;
            }
            rotation = i;
        }
        rotateCropForSF(sourceCrop, rotation);
        ScreenshotGraphicBuffer buffer = screenshotToBuffer(displayToken, sourceCrop, width, height, useIdentityTransform, rotation);
        if (buffer != null) {
            return Bitmap.wrapHardwareBuffer(buffer.getGraphicBuffer(), buffer.getColorSpace());
        }
        Log.w(str, "Failed to take screenshot");
        return null;
    }

    public static ScreenshotGraphicBuffer screenshotToBuffer(IBinder display, Rect sourceCrop, int width, int height, boolean useIdentityTransform, int rotation) {
        if (display != null) {
            return nativeScreenshot(display, sourceCrop, width, height, useIdentityTransform, rotation, false);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static ScreenshotGraphicBuffer screenshotToBufferWithSecureLayersUnsafe(IBinder display, Rect sourceCrop, int width, int height, boolean useIdentityTransform, int rotation) {
        if (display != null) {
            return nativeScreenshot(display, sourceCrop, width, height, useIdentityTransform, rotation, true);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    private static void rotateCropForSF(Rect crop, int rot) {
        if (rot == 1 || rot == 3) {
            int tmp = crop.top;
            crop.top = crop.left;
            crop.left = tmp;
            tmp = crop.right;
            crop.right = crop.bottom;
            crop.bottom = tmp;
        }
    }

    public static ScreenshotGraphicBuffer captureLayers(IBinder layerHandleToken, Rect sourceCrop, float frameScale) {
        return nativeCaptureLayers(getInternalDisplayToken(), layerHandleToken, sourceCrop, frameScale, null);
    }

    public static ScreenshotGraphicBuffer captureLayersExcluding(IBinder layerHandleToken, Rect sourceCrop, float frameScale, IBinder[] exclude) {
        return nativeCaptureLayers(getInternalDisplayToken(), layerHandleToken, sourceCrop, frameScale, exclude);
    }

    public static boolean getProtectedContentSupport() {
        return nativeGetProtectedContentSupport();
    }

    public static boolean getDisplayBrightnessSupport(IBinder displayToken) {
        return nativeGetDisplayBrightnessSupport(displayToken);
    }

    public static boolean setDisplayBrightness(IBinder displayToken, float brightness) {
        Objects.requireNonNull(displayToken);
        if (!Float.isNaN(brightness) && brightness <= 1.0f && (brightness >= 0.0f || brightness == -1.0f)) {
            return nativeSetDisplayBrightness(displayToken, brightness);
        }
        throw new IllegalArgumentException("brightness must be a number between 0.0f and 1.0f, or -1 to turn the backlight off.");
    }

    public void setBlur(boolean isBlur) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setBlur(this, isBlur);
        }
    }

    public void setBlurRatio(float blurRatio) {
        checkNotReleased();
        sGlobalTransaction.setBlurRatio(this, blurRatio);
    }

    public void setBlurMode(int blurMode) {
        checkNotReleased();
        sGlobalTransaction.setBlurMode(this, blurMode);
    }

    public void setRecordHide(boolean enable) {
        checkNotReleased();
        synchronized (SurfaceControl.class) {
            sGlobalTransaction.setRecordHide(this, enable);
        }
    }
}
