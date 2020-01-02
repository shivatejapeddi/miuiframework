package android.media;

import android.graphics.ImageFormat;
import android.hardware.HardwareBuffer;
import android.media.Image.Plane;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import dalvik.system.VMRuntime;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.NioUtils;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageReader implements AutoCloseable {
    private static final int ACQUIRE_MAX_IMAGES = 2;
    private static final int ACQUIRE_NO_BUFS = 1;
    private static final int ACQUIRE_SUCCESS = 0;
    private List<Image> mAcquiredImages = new CopyOnWriteArrayList();
    private final Object mCloseLock = new Object();
    private int mEstimatedNativeAllocBytes;
    private final int mFormat;
    private final int mHeight;
    private boolean mIsReaderValid = false;
    private OnImageAvailableListener mListener;
    private ListenerHandler mListenerHandler;
    private final Object mListenerLock = new Object();
    private final int mMaxImages;
    private long mNativeContext;
    private final int mNumPlanes;
    private final Surface mSurface;
    private final int mWidth;

    private final class ListenerHandler extends Handler {
        public ListenerHandler(Looper looper) {
            super(looper, null, true);
        }

        public void handleMessage(Message msg) {
            OnImageAvailableListener listener;
            synchronized (ImageReader.this.mListenerLock) {
                listener = ImageReader.this.mListener;
            }
            synchronized (ImageReader.this.mCloseLock) {
                boolean isReaderValid = ImageReader.this.mIsReaderValid;
            }
            if (listener != null && isReaderValid) {
                listener.onImageAvailable(ImageReader.this);
            }
        }
    }

    public interface OnImageAvailableListener {
        void onImageAvailable(ImageReader imageReader);
    }

    private class SurfaceImage extends Image {
        private int mFormat = 0;
        private AtomicBoolean mIsDetached = new AtomicBoolean(false);
        private long mNativeBuffer;
        private SurfacePlane[] mPlanes;
        private int mScalingMode;
        private long mTimestamp;
        private int mTransform;

        private class SurfacePlane extends Plane {
            private ByteBuffer mBuffer;
            private final int mPixelStride;
            private final int mRowStride;

            private SurfacePlane(int rowStride, int pixelStride, ByteBuffer buffer) {
                this.mRowStride = rowStride;
                this.mPixelStride = pixelStride;
                this.mBuffer = buffer;
                this.mBuffer.order(ByteOrder.nativeOrder());
            }

            public ByteBuffer getBuffer() {
                SurfaceImage.this.throwISEIfImageIsInvalid();
                return this.mBuffer;
            }

            public int getPixelStride() {
                SurfaceImage.this.throwISEIfImageIsInvalid();
                if (ImageReader.this.mFormat != 36) {
                    return this.mPixelStride;
                }
                throw new UnsupportedOperationException("getPixelStride is not supported for RAW_PRIVATE plane");
            }

            public int getRowStride() {
                SurfaceImage.this.throwISEIfImageIsInvalid();
                if (ImageReader.this.mFormat != 36) {
                    return this.mRowStride;
                }
                throw new UnsupportedOperationException("getRowStride is not supported for RAW_PRIVATE plane");
            }

            private void clearBuffer() {
                ByteBuffer byteBuffer = this.mBuffer;
                if (byteBuffer != null) {
                    if (byteBuffer.isDirect()) {
                        NioUtils.freeDirectBuffer(this.mBuffer);
                    }
                    this.mBuffer = null;
                }
            }
        }

        private native synchronized SurfacePlane[] nativeCreatePlanes(int i, int i2);

        private native synchronized int nativeGetFormat(int i);

        private native synchronized HardwareBuffer nativeGetHardwareBuffer();

        private native synchronized int nativeGetHeight();

        private native synchronized int nativeGetWidth();

        public SurfaceImage(int format) {
            this.mFormat = format;
        }

        public void close() {
            ImageReader.this.releaseImage(this);
        }

        public ImageReader getReader() {
            return ImageReader.this;
        }

        public int getFormat() {
            int i;
            throwISEIfImageIsInvalid();
            int readerFormat = ImageReader.this.getImageFormat();
            if (readerFormat == 34) {
                i = readerFormat;
            } else {
                i = nativeGetFormat(readerFormat);
            }
            this.mFormat = i;
            return this.mFormat;
        }

        public int getWidth() {
            throwISEIfImageIsInvalid();
            int format = getFormat();
            if (format == 36 || format == ImageFormat.HEIC || format == ImageFormat.DEPTH_JPEG || format == 256 || format == 257) {
                return ImageReader.this.getWidth();
            }
            return nativeGetWidth();
        }

        public int getHeight() {
            throwISEIfImageIsInvalid();
            int format = getFormat();
            if (format == 36 || format == ImageFormat.HEIC || format == ImageFormat.DEPTH_JPEG || format == 256 || format == 257) {
                return ImageReader.this.getHeight();
            }
            return nativeGetHeight();
        }

        public long getTimestamp() {
            throwISEIfImageIsInvalid();
            return this.mTimestamp;
        }

        public int getTransform() {
            throwISEIfImageIsInvalid();
            return this.mTransform;
        }

        public int getScalingMode() {
            throwISEIfImageIsInvalid();
            return this.mScalingMode;
        }

        public HardwareBuffer getHardwareBuffer() {
            throwISEIfImageIsInvalid();
            return nativeGetHardwareBuffer();
        }

        public void setTimestamp(long timestampNs) {
            throwISEIfImageIsInvalid();
            this.mTimestamp = timestampNs;
        }

        public Plane[] getPlanes() {
            throwISEIfImageIsInvalid();
            if (this.mPlanes == null) {
                this.mPlanes = nativeCreatePlanes(ImageReader.this.mNumPlanes, ImageReader.this.mFormat);
            }
            return (Plane[]) this.mPlanes.clone();
        }

        /* Access modifiers changed, original: protected|final */
        public final void finalize() throws Throwable {
            try {
                close();
            } finally {
                super.finalize();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isAttachable() {
            throwISEIfImageIsInvalid();
            return this.mIsDetached.get();
        }

        /* Access modifiers changed, original: 0000 */
        public ImageReader getOwner() {
            throwISEIfImageIsInvalid();
            return ImageReader.this;
        }

        /* Access modifiers changed, original: 0000 */
        public long getNativeContext() {
            throwISEIfImageIsInvalid();
            return this.mNativeBuffer;
        }

        private void setDetached(boolean detached) {
            throwISEIfImageIsInvalid();
            this.mIsDetached.getAndSet(detached);
        }

        private void clearSurfacePlanes() {
            if (this.mIsImageValid && this.mPlanes != null) {
                int i = 0;
                while (true) {
                    SurfacePlane[] surfacePlaneArr = this.mPlanes;
                    if (i < surfacePlaneArr.length) {
                        if (surfacePlaneArr[i] != null) {
                            surfacePlaneArr[i].clearBuffer();
                            this.mPlanes[i] = null;
                        }
                        i++;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    private static native void nativeClassInit();

    private native synchronized void nativeClose();

    private native synchronized int nativeDetachImage(Image image);

    private native synchronized void nativeDiscardFreeBuffers();

    private native synchronized Surface nativeGetSurface();

    private native synchronized int nativeImageSetup(Image image);

    private native synchronized void nativeInit(Object obj, int i, int i2, int i3, int i4, long j);

    private native synchronized void nativeReleaseImage(Image image);

    public static ImageReader newInstance(int width, int height, int format, int maxImages) {
        return new ImageReader(width, height, format, maxImages, format == 34 ? 0 : 3);
    }

    public static ImageReader newInstance(int width, int height, int format, int maxImages, long usage) {
        return new ImageReader(width, height, format, maxImages, usage);
    }

    protected ImageReader(int width, int height, int format, int maxImages, long usage) {
        int i = width;
        int i2 = height;
        int i3 = format;
        this.mWidth = i;
        this.mHeight = i2;
        this.mFormat = i3;
        this.mMaxImages = maxImages;
        if (i < 1 || i2 < 1) {
            throw new IllegalArgumentException("The image dimensions must be positive");
        } else if (this.mMaxImages < 1) {
            throw new IllegalArgumentException("Maximum outstanding image count must be at least 1");
        } else if (i3 != 17) {
            this.mNumPlanes = ImageUtils.getNumPlanesForFormat(this.mFormat);
            nativeInit(new WeakReference(this), width, height, format, maxImages, usage);
            this.mSurface = nativeGetSurface();
            this.mIsReaderValid = true;
            this.mEstimatedNativeAllocBytes = ImageUtils.getEstimatedNativeAllocBytes(width, i2, i3, 1);
            VMRuntime.getRuntime().registerNativeAllocation(this.mEstimatedNativeAllocBytes);
        } else {
            throw new IllegalArgumentException("NV21 format is not supported");
        }
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getImageFormat() {
        return this.mFormat;
    }

    public int getMaxImages() {
        return this.mMaxImages;
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public Image acquireLatestImage() {
        Image image = acquireNextImage();
        if (image == null) {
            return null;
        }
        loop0:
        while (true) {
            Image next;
            try {
                next = acquireNextImageNoThrowISE();
                if (next == null) {
                    break loop0;
                }
                image = next;
            } finally {
                image.close();
                image = next;
            }
        }
        Image result = image;
        image = null;
        if (image != null) {
            image.close();
        }
        return result;
    }

    public Image acquireNextImageNoThrowISE() {
        Image si = new SurfaceImage(this.mFormat);
        return acquireNextSurfaceImage(si) == 0 ? si : null;
    }

    private int acquireNextSurfaceImage(SurfaceImage si) {
        int status;
        synchronized (this.mCloseLock) {
            status = 1;
            if (this.mIsReaderValid) {
                status = nativeImageSetup(si);
            }
            if (status == 0) {
                si.mIsImageValid = true;
            } else if (status != 1) {
                if (status != 2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown nativeImageSetup return code ");
                    stringBuilder.append(status);
                    throw new AssertionError(stringBuilder.toString());
                }
            }
            if (status == 0) {
                this.mAcquiredImages.add(si);
            }
        }
        return status;
    }

    public Image acquireNextImage() {
        SurfaceImage si = new SurfaceImage(this.mFormat);
        int status = acquireNextSurfaceImage(si);
        if (status == 0) {
            return si;
        }
        if (status == 1) {
            return null;
        }
        if (status != 2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown nativeImageSetup return code ");
            stringBuilder.append(status);
            throw new AssertionError(stringBuilder.toString());
        }
        throw new IllegalStateException(String.format("maxImages (%d) has already been acquired, call #close before acquiring more.", new Object[]{Integer.valueOf(this.mMaxImages)}));
    }

    private void releaseImage(Image i) {
        if (i instanceof SurfaceImage) {
            SurfaceImage si = (SurfaceImage) i;
            if (!si.mIsImageValid) {
                return;
            }
            if (si.getReader() == this && this.mAcquiredImages.contains(i)) {
                si.clearSurfacePlanes();
                nativeReleaseImage(i);
                si.mIsImageValid = false;
                this.mAcquiredImages.remove(i);
                return;
            }
            throw new IllegalArgumentException("This image was not produced by this ImageReader");
        }
        throw new IllegalArgumentException("This image was not produced by an ImageReader");
    }

    public void setOnImageAvailableListener(OnImageAvailableListener listener, Handler handler) {
        synchronized (this.mListenerLock) {
            if (listener != null) {
                Looper looper = handler != null ? handler.getLooper() : Looper.myLooper();
                if (looper != null) {
                    if (this.mListenerHandler == null || this.mListenerHandler.getLooper() != looper) {
                        this.mListenerHandler = new ListenerHandler(looper);
                    }
                    this.mListener = listener;
                } else {
                    throw new IllegalArgumentException("handler is null but the current thread is not a looper");
                }
            }
            this.mListener = null;
            this.mListenerHandler = null;
        }
    }

    public void close() {
        setOnImageAvailableListener(null, null);
        Surface surface = this.mSurface;
        if (surface != null) {
            surface.release();
        }
        synchronized (this.mCloseLock) {
            this.mIsReaderValid = false;
            for (Image image : this.mAcquiredImages) {
                image.close();
            }
            this.mAcquiredImages.clear();
            nativeClose();
            if (this.mEstimatedNativeAllocBytes > 0) {
                VMRuntime.getRuntime().registerNativeFree(this.mEstimatedNativeAllocBytes);
                this.mEstimatedNativeAllocBytes = 0;
            }
        }
    }

    public void discardFreeBuffers() {
        synchronized (this.mCloseLock) {
            nativeDiscardFreeBuffers();
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void detachImage(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("input image must not be null");
        } else if (isImageOwnedbyMe(image)) {
            SurfaceImage si = (SurfaceImage) image;
            si.throwISEIfImageIsInvalid();
            if (si.isAttachable()) {
                throw new IllegalStateException("Image was already detached from this ImageReader");
            }
            nativeDetachImage(image);
            si.clearSurfacePlanes();
            si.mPlanes = null;
            si.setDetached(true);
        } else {
            throw new IllegalArgumentException("Trying to detach an image that is not owned by this ImageReader");
        }
    }

    private boolean isImageOwnedbyMe(Image image) {
        boolean z = false;
        if (!(image instanceof SurfaceImage)) {
            return false;
        }
        if (((SurfaceImage) image).getReader() == this) {
            z = true;
        }
        return z;
    }

    private static void postEventFromNative(Object selfRef) {
        ImageReader ir = (ImageReader) ((WeakReference) selfRef).get();
        if (ir != null) {
            Handler handler;
            synchronized (ir.mListenerLock) {
                handler = ir.mListenerHandler;
            }
            if (handler != null) {
                handler.sendEmptyMessage(0);
            }
        }
    }

    static {
        System.loadLibrary("media_jni");
        nativeClassInit();
    }
}
