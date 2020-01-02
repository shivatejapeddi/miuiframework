package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

public class TextureView extends View {
    private static final String LOG_TAG = "TextureView";
    private Canvas mCanvas;
    private boolean mHadSurface;
    @UnsupportedAppUsage
    private TextureLayer mLayer;
    private SurfaceTextureListener mListener;
    private final Object[] mLock;
    private final Matrix mMatrix;
    private boolean mMatrixChanged;
    @UnsupportedAppUsage
    private long mNativeWindow;
    private final Object[] mNativeWindowLock;
    @UnsupportedAppUsage
    private boolean mOpaque;
    private int mSaveCount;
    @UnsupportedAppUsage
    private SurfaceTexture mSurface;
    private boolean mUpdateLayer;
    @UnsupportedAppUsage
    private final OnFrameAvailableListener mUpdateListener;
    @UnsupportedAppUsage
    private boolean mUpdateSurface;

    public interface SurfaceTextureListener {
        void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2);

        boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture);

        void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2);

        void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture);
    }

    @UnsupportedAppUsage
    private native void nCreateNativeWindow(SurfaceTexture surfaceTexture);

    @UnsupportedAppUsage
    private native void nDestroyNativeWindow();

    private static native boolean nLockCanvas(long j, Canvas canvas, Rect rect);

    private static native void nUnlockCanvasAndPost(long j, Canvas canvas);

    public TextureView(Context context) {
        super(context);
        this.mOpaque = true;
        this.mMatrix = new Matrix();
        this.mLock = new Object[0];
        this.mNativeWindowLock = new Object[0];
        this.mUpdateListener = new OnFrameAvailableListener() {
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                TextureView.this.updateLayer();
                TextureView.this.invalidate();
            }
        };
    }

    public TextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mOpaque = true;
        this.mMatrix = new Matrix();
        this.mLock = new Object[0];
        this.mNativeWindowLock = new Object[0];
        this.mUpdateListener = /* anonymous class already generated */;
    }

    public TextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mOpaque = true;
        this.mMatrix = new Matrix();
        this.mLock = new Object[0];
        this.mNativeWindowLock = new Object[0];
        this.mUpdateListener = /* anonymous class already generated */;
    }

    public TextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mOpaque = true;
        this.mMatrix = new Matrix();
        this.mLock = new Object[0];
        this.mNativeWindowLock = new Object[0];
        this.mUpdateListener = /* anonymous class already generated */;
    }

    public boolean isOpaque() {
        return this.mOpaque;
    }

    public void setOpaque(boolean opaque) {
        if (opaque != this.mOpaque) {
            this.mOpaque = opaque;
            if (this.mLayer != null) {
                updateLayerAndInvalidate();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isHardwareAccelerated()) {
            Log.w(LOG_TAG, "A TextureView or a subclass can only be used with hardware acceleration enabled.");
        }
        if (this.mHadSurface) {
            invalidate(true);
            this.mHadSurface = false;
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void onDetachedFromWindowInternal() {
        destroyHardwareLayer();
        releaseSurfaceTexture();
        super.onDetachedFromWindowInternal();
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void destroyHardwareResources() {
        super.destroyHardwareResources();
        destroyHardwareLayer();
    }

    @UnsupportedAppUsage
    private void destroyHardwareLayer() {
        TextureLayer textureLayer = this.mLayer;
        if (textureLayer != null) {
            textureLayer.detachSurfaceTexture();
            this.mLayer.destroy();
            this.mLayer = null;
            this.mMatrixChanged = true;
        }
    }

    private void releaseSurfaceTexture() {
        SurfaceTexture surfaceTexture = this.mSurface;
        if (surfaceTexture != null) {
            boolean shouldRelease = true;
            SurfaceTextureListener surfaceTextureListener = this.mListener;
            if (surfaceTextureListener != null) {
                shouldRelease = surfaceTextureListener.onSurfaceTextureDestroyed(surfaceTexture);
            }
            synchronized (this.mNativeWindowLock) {
                nDestroyNativeWindow();
            }
            if (shouldRelease) {
                this.mSurface.release();
            }
            this.mSurface = null;
            this.mHadSurface = true;
        }
    }

    public void setLayerType(int layerType, Paint paint) {
        setLayerPaint(paint);
    }

    public void setLayerPaint(Paint paint) {
        if (paint != this.mLayerPaint) {
            this.mLayerPaint = paint;
            invalidate();
        }
    }

    public int getLayerType() {
        return 2;
    }

    public void buildLayer() {
    }

    public void setForeground(Drawable foreground) {
        if (foreground != null && !sTextureViewIgnoresDrawableSetters) {
            throw new UnsupportedOperationException("TextureView doesn't support displaying a foreground drawable");
        }
    }

    public void setBackgroundDrawable(Drawable background) {
        if (background != null && !sTextureViewIgnoresDrawableSetters) {
            throw new UnsupportedOperationException("TextureView doesn't support displaying a background drawable");
        }
    }

    public final void draw(Canvas canvas) {
        this.mPrivateFlags = (this.mPrivateFlags & -2097153) | 32;
        if (canvas.isHardwareAccelerated()) {
            RecordingCanvas recordingCanvas = (RecordingCanvas) canvas;
            TextureLayer layer = getTextureLayer();
            if (layer != null) {
                applyUpdate();
                applyTransformMatrix();
                this.mLayer.setLayerPaint(this.mLayerPaint);
                recordingCanvas.drawTextureLayer(layer);
            }
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void onDraw(Canvas canvas) {
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        SurfaceTexture surfaceTexture = this.mSurface;
        if (surfaceTexture != null) {
            surfaceTexture.setDefaultBufferSize(getWidth(), getHeight());
            updateLayer();
            SurfaceTextureListener surfaceTextureListener = this.mListener;
            if (surfaceTextureListener != null) {
                surfaceTextureListener.onSurfaceTextureSizeChanged(this.mSurface, getWidth(), getHeight());
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public TextureLayer getTextureLayer() {
        if (this.mLayer == null) {
            if (this.mAttachInfo == null || this.mAttachInfo.mThreadedRenderer == null) {
                return null;
            }
            this.mLayer = this.mAttachInfo.mThreadedRenderer.createTextureLayer();
            boolean createNewSurface = this.mSurface == null;
            if (createNewSurface) {
                this.mSurface = new SurfaceTexture(false);
                nCreateNativeWindow(this.mSurface);
            }
            this.mLayer.setSurfaceTexture(this.mSurface);
            this.mSurface.setDefaultBufferSize(getWidth(), getHeight());
            this.mSurface.setOnFrameAvailableListener(this.mUpdateListener, this.mAttachInfo.mHandler);
            SurfaceTextureListener surfaceTextureListener = this.mListener;
            if (surfaceTextureListener != null && createNewSurface) {
                surfaceTextureListener.onSurfaceTextureAvailable(this.mSurface, getWidth(), getHeight());
            }
            this.mLayer.setLayerPaint(this.mLayerPaint);
        }
        if (this.mUpdateSurface) {
            this.mUpdateSurface = false;
            updateLayer();
            this.mMatrixChanged = true;
            this.mLayer.setSurfaceTexture(this.mSurface);
            this.mSurface.setDefaultBufferSize(getWidth(), getHeight());
        }
        return this.mLayer;
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        SurfaceTexture surfaceTexture = this.mSurface;
        if (surfaceTexture == null) {
            return;
        }
        if (visibility == 0) {
            if (this.mLayer != null) {
                surfaceTexture.setOnFrameAvailableListener(this.mUpdateListener, this.mAttachInfo.mHandler);
            }
            updateLayerAndInvalidate();
            return;
        }
        surfaceTexture.setOnFrameAvailableListener(null);
    }

    private void updateLayer() {
        synchronized (this.mLock) {
            this.mUpdateLayer = true;
        }
    }

    private void updateLayerAndInvalidate() {
        synchronized (this.mLock) {
            this.mUpdateLayer = true;
        }
        invalidate();
    }

    /* JADX WARNING: Missing block: B:10:0x0010, code skipped:
            r4.mLayer.prepare(getWidth(), getHeight(), r4.mOpaque);
            r4.mLayer.updateSurfaceTexture();
            r0 = r4.mListener;
     */
    /* JADX WARNING: Missing block: B:11:0x0026, code skipped:
            if (r0 == null) goto L_0x002d;
     */
    /* JADX WARNING: Missing block: B:12:0x0028, code skipped:
            r0.onSurfaceTextureUpdated(r4.mSurface);
     */
    /* JADX WARNING: Missing block: B:13:0x002d, code skipped:
            return;
     */
    private void applyUpdate() {
        /*
        r4 = this;
        r0 = r4.mLayer;
        if (r0 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = r4.mLock;
        monitor-enter(r0);
        r1 = r4.mUpdateLayer;	 Catch:{ all -> 0x0030 }
        if (r1 == 0) goto L_0x002e;
    L_0x000c:
        r1 = 0;
        r4.mUpdateLayer = r1;	 Catch:{ all -> 0x0030 }
        monitor-exit(r0);	 Catch:{ all -> 0x0030 }
        r0 = r4.mLayer;
        r1 = r4.getWidth();
        r2 = r4.getHeight();
        r3 = r4.mOpaque;
        r0.prepare(r1, r2, r3);
        r0 = r4.mLayer;
        r0.updateSurfaceTexture();
        r0 = r4.mListener;
        if (r0 == 0) goto L_0x002d;
    L_0x0028:
        r1 = r4.mSurface;
        r0.onSurfaceTextureUpdated(r1);
    L_0x002d:
        return;
    L_0x002e:
        monitor-exit(r0);	 Catch:{ all -> 0x0030 }
        return;
    L_0x0030:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0030 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.TextureView.applyUpdate():void");
    }

    public void setTransform(Matrix transform) {
        this.mMatrix.set(transform);
        this.mMatrixChanged = true;
        invalidateParentIfNeeded();
    }

    public Matrix getTransform(Matrix transform) {
        if (transform == null) {
            transform = new Matrix();
        }
        transform.set(this.mMatrix);
        return transform;
    }

    private void applyTransformMatrix() {
        if (this.mMatrixChanged) {
            TextureLayer textureLayer = this.mLayer;
            if (textureLayer != null) {
                textureLayer.setTransform(this.mMatrix);
                this.mMatrixChanged = false;
            }
        }
    }

    public Bitmap getBitmap() {
        return getBitmap(getWidth(), getHeight());
    }

    public Bitmap getBitmap(int width, int height) {
        if (!isAvailable() || width <= 0 || height <= 0) {
            return null;
        }
        return getBitmap(Bitmap.createBitmap(getResources().getDisplayMetrics(), width, height, Config.ARGB_8888));
    }

    public Bitmap getBitmap(Bitmap bitmap) {
        if (bitmap != null && isAvailable()) {
            applyUpdate();
            applyTransformMatrix();
            if (this.mLayer == null && this.mUpdateSurface) {
                getTextureLayer();
            }
            TextureLayer textureLayer = this.mLayer;
            if (textureLayer != null) {
                textureLayer.copyInto(bitmap);
            }
        }
        return bitmap;
    }

    public boolean isAvailable() {
        return this.mSurface != null;
    }

    public Canvas lockCanvas() {
        return lockCanvas(null);
    }

    public Canvas lockCanvas(Rect dirty) {
        if (!isAvailable()) {
            return null;
        }
        if (this.mCanvas == null) {
            this.mCanvas = new Canvas();
        }
        synchronized (this.mNativeWindowLock) {
            if (nLockCanvas(this.mNativeWindow, this.mCanvas, dirty)) {
                this.mSaveCount = this.mCanvas.save();
                return this.mCanvas;
            }
            return null;
        }
    }

    public void unlockCanvasAndPost(Canvas canvas) {
        Canvas canvas2 = this.mCanvas;
        if (canvas2 != null && canvas == canvas2) {
            canvas.restoreToCount(this.mSaveCount);
            this.mSaveCount = 0;
            synchronized (this.mNativeWindowLock) {
                nUnlockCanvasAndPost(this.mNativeWindow, this.mCanvas);
            }
        }
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.mSurface;
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        if (surfaceTexture == null) {
            throw new NullPointerException("surfaceTexture must not be null");
        } else if (surfaceTexture == this.mSurface) {
            throw new IllegalArgumentException("Trying to setSurfaceTexture to the same SurfaceTexture that's already set.");
        } else if (surfaceTexture.isReleased()) {
            throw new IllegalArgumentException("Cannot setSurfaceTexture to a released SurfaceTexture");
        } else {
            if (this.mSurface != null) {
                nDestroyNativeWindow();
                this.mSurface.release();
            }
            this.mSurface = surfaceTexture;
            nCreateNativeWindow(this.mSurface);
            if ((this.mViewFlags & 12) == 0 && this.mLayer != null) {
                this.mSurface.setOnFrameAvailableListener(this.mUpdateListener, this.mAttachInfo.mHandler);
            }
            this.mUpdateSurface = true;
            invalidateParentIfNeeded();
        }
    }

    public SurfaceTextureListener getSurfaceTextureListener() {
        return this.mListener;
    }

    public void setSurfaceTextureListener(SurfaceTextureListener listener) {
        this.mListener = listener;
    }
}
