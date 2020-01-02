package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.PixelCopy;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceSession;
import android.view.SurfaceView;
import android.view.ThreadedRenderer.SimpleRenderer;
import android.view.View;
import android.view.ViewRootImpl;
import com.android.internal.R;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Magnifier {
    private static final int NONEXISTENT_PREVIOUS_CONFIG_VALUE = -1;
    public static final int SOURCE_BOUND_MAX_IN_SURFACE = 0;
    public static final int SOURCE_BOUND_MAX_VISIBLE = 1;
    private static final String TAG = "Magnifier";
    private static final HandlerThread sPixelCopyHandlerThread = new HandlerThread("magnifier pixel copy result handler");
    private int mBottomContentBound;
    private Callback mCallback;
    private final Point mClampedCenterZoomCoords;
    private final boolean mClippingEnabled;
    private SurfaceInfo mContentCopySurface;
    private final int mDefaultHorizontalSourceToMagnifierOffset;
    private final int mDefaultVerticalSourceToMagnifierOffset;
    private final Object mDestroyLock;
    private boolean mDirtyState;
    private int mLeftContentBound;
    private final Object mLock;
    private final Drawable mOverlay;
    private SurfaceInfo mParentSurface;
    private final Rect mPixelCopyRequestRect;
    private final PointF mPrevShowSourceCoords;
    private final PointF mPrevShowWindowCoords;
    private final Point mPrevStartCoordsInSurface;
    private int mRightContentBound;
    private int mSourceHeight;
    private int mSourceWidth;
    private int mTopContentBound;
    private final View mView;
    private final int[] mViewCoordinatesInSurface;
    private InternalPopupWindow mWindow;
    private final Point mWindowCoords;
    private final float mWindowCornerRadius;
    private final float mWindowElevation;
    private final int mWindowHeight;
    private final int mWindowWidth;
    private float mZoom;

    public static final class Builder {
        private int mBottomContentBound;
        private boolean mClippingEnabled;
        private float mCornerRadius;
        private float mElevation;
        private int mHeight;
        private int mHorizontalDefaultSourceToMagnifierOffset;
        private int mLeftContentBound;
        private Drawable mOverlay;
        private int mRightContentBound;
        private int mTopContentBound;
        private int mVerticalDefaultSourceToMagnifierOffset;
        private View mView;
        private int mWidth;
        private float mZoom;

        public Builder(View view) {
            this.mView = (View) Preconditions.checkNotNull(view);
            applyDefaults();
        }

        private void applyDefaults() {
            Resources resources = this.mView.getContext().getResources();
            this.mWidth = resources.getDimensionPixelSize(R.dimen.default_magnifier_width);
            this.mHeight = resources.getDimensionPixelSize(R.dimen.default_magnifier_height);
            this.mElevation = resources.getDimension(R.dimen.default_magnifier_elevation);
            this.mCornerRadius = resources.getDimension(R.dimen.default_magnifier_corner_radius);
            this.mZoom = resources.getFloat(R.dimen.default_magnifier_zoom);
            this.mHorizontalDefaultSourceToMagnifierOffset = resources.getDimensionPixelSize(R.dimen.default_magnifier_horizontal_offset);
            this.mVerticalDefaultSourceToMagnifierOffset = resources.getDimensionPixelSize(R.dimen.default_magnifier_vertical_offset);
            this.mOverlay = new ColorDrawable(resources.getColor(R.color.default_magnifier_color_overlay, null));
            this.mClippingEnabled = true;
            this.mLeftContentBound = 1;
            this.mTopContentBound = 1;
            this.mRightContentBound = 1;
            this.mBottomContentBound = 1;
        }

        public Builder setSize(int width, int height) {
            Preconditions.checkArgumentPositive(width, "Width should be positive");
            Preconditions.checkArgumentPositive(height, "Height should be positive");
            this.mWidth = width;
            this.mHeight = height;
            return this;
        }

        public Builder setInitialZoom(float zoom) {
            Preconditions.checkArgumentPositive(zoom, "Zoom should be positive");
            this.mZoom = zoom;
            return this;
        }

        public Builder setElevation(float elevation) {
            Preconditions.checkArgumentNonNegative(elevation, "Elevation should be non-negative");
            this.mElevation = elevation;
            return this;
        }

        public Builder setCornerRadius(float cornerRadius) {
            Preconditions.checkArgumentNonNegative(cornerRadius, "Corner radius should be non-negative");
            this.mCornerRadius = cornerRadius;
            return this;
        }

        public Builder setOverlay(Drawable overlay) {
            this.mOverlay = overlay;
            return this;
        }

        public Builder setDefaultSourceToMagnifierOffset(int horizontalOffset, int verticalOffset) {
            this.mHorizontalDefaultSourceToMagnifierOffset = horizontalOffset;
            this.mVerticalDefaultSourceToMagnifierOffset = verticalOffset;
            return this;
        }

        public Builder setClippingEnabled(boolean clip) {
            this.mClippingEnabled = clip;
            return this;
        }

        public Builder setSourceBounds(int left, int top, int right, int bottom) {
            this.mLeftContentBound = left;
            this.mTopContentBound = top;
            this.mRightContentBound = right;
            this.mBottomContentBound = bottom;
            return this;
        }

        public Magnifier build() {
            return new Magnifier(this);
        }
    }

    public interface Callback {
        void onOperationComplete();
    }

    private static class InternalPopupWindow {
        private static final int SURFACE_Z = 5;
        private Bitmap mBitmap;
        private final RenderNode mBitmapRenderNode;
        private Callback mCallback;
        private final int mContentHeight;
        private final int mContentWidth;
        private Bitmap mCurrentContent;
        private final Display mDisplay;
        private boolean mFirstDraw = true;
        private boolean mFrameDrawScheduled;
        private final Handler mHandler;
        private int mLastDrawContentPositionX;
        private int mLastDrawContentPositionY;
        private final Object mLock;
        private final Runnable mMagnifierUpdater;
        private final int mOffsetX;
        private final int mOffsetY;
        private final Drawable mOverlay;
        private final RenderNode mOverlayRenderNode;
        private boolean mPendingWindowPositionUpdate;
        private final SimpleRenderer mRenderer;
        private final Surface mSurface;
        private final SurfaceControl mSurfaceControl;
        private final int mSurfaceHeight;
        private final SurfaceSession mSurfaceSession;
        private final int mSurfaceWidth;
        private int mWindowPositionX;
        private int mWindowPositionY;

        InternalPopupWindow(Context context, Display display, SurfaceControl parentSurfaceControl, int width, int height, float elevation, float cornerRadius, Drawable overlay, Handler handler, Object lock, Callback callback) {
            this.mDisplay = display;
            this.mOverlay = overlay;
            this.mLock = lock;
            this.mCallback = callback;
            this.mContentWidth = width;
            this.mContentHeight = height;
            this.mOffsetX = (int) (elevation * 1.05f);
            this.mOffsetY = (int) (1.05f * elevation);
            this.mSurfaceWidth = this.mContentWidth + (this.mOffsetX * 2);
            this.mSurfaceHeight = this.mContentHeight + (this.mOffsetY * 2);
            this.mSurfaceSession = new SurfaceSession();
            this.mSurfaceControl = new android.view.SurfaceControl.Builder(this.mSurfaceSession).setFormat(-3).setBufferSize(this.mSurfaceWidth, this.mSurfaceHeight).setName("magnifier surface").setFlags(4).setParent(parentSurfaceControl).build();
            this.mSurface = new Surface();
            this.mSurface.copyFrom(this.mSurfaceControl);
            this.mRenderer = new SimpleRenderer(context, "magnifier renderer", this.mSurface);
            this.mBitmapRenderNode = createRenderNodeForBitmap("magnifier content", elevation, cornerRadius);
            this.mOverlayRenderNode = createRenderNodeForOverlay("magnifier overlay", cornerRadius);
            setupOverlay();
            RecordingCanvas canvas = this.mRenderer.getRootNode().beginRecording(width, height);
            try {
                canvas.insertReorderBarrier();
                canvas.drawRenderNode(this.mBitmapRenderNode);
                canvas.insertInorderBarrier();
                canvas.drawRenderNode(this.mOverlayRenderNode);
                canvas.insertInorderBarrier();
                if (this.mCallback != null) {
                    this.mCurrentContent = Bitmap.createBitmap(this.mContentWidth, this.mContentHeight, Config.ARGB_8888);
                    updateCurrentContentForTesting();
                }
                this.mHandler = handler;
                this.mMagnifierUpdater = new -$$Lambda$Magnifier$InternalPopupWindow$t9Cn2sIi2LBUhAVikvRPKKoAwIU(this);
                this.mFrameDrawScheduled = false;
            } finally {
                this.mRenderer.getRootNode().endRecording();
            }
        }

        private RenderNode createRenderNodeForBitmap(String name, float elevation, float cornerRadius) {
            RenderNode bitmapRenderNode = RenderNode.create(name, null);
            int i = this.mOffsetX;
            int i2 = this.mOffsetY;
            bitmapRenderNode.setLeftTopRightBottom(i, i2, this.mContentWidth + i, this.mContentHeight + i2);
            bitmapRenderNode.setElevation(elevation);
            Outline outline = new Outline();
            outline.setRoundRect(0, 0, this.mContentWidth, this.mContentHeight, cornerRadius);
            outline.setAlpha(1.0f);
            bitmapRenderNode.setOutline(outline);
            bitmapRenderNode.setClipToOutline(true);
            try {
                bitmapRenderNode.beginRecording(this.mContentWidth, this.mContentHeight).drawColor(Color.GREEN);
                return bitmapRenderNode;
            } finally {
                bitmapRenderNode.endRecording();
            }
        }

        private RenderNode createRenderNodeForOverlay(String name, float cornerRadius) {
            RenderNode overlayRenderNode = RenderNode.create(name, null);
            int i = this.mOffsetX;
            int i2 = this.mOffsetY;
            overlayRenderNode.setLeftTopRightBottom(i, i2, this.mContentWidth + i, this.mContentHeight + i2);
            Outline outline = new Outline();
            outline.setRoundRect(0, 0, this.mContentWidth, this.mContentHeight, cornerRadius);
            outline.setAlpha(1.0f);
            overlayRenderNode.setOutline(outline);
            overlayRenderNode.setClipToOutline(true);
            return overlayRenderNode;
        }

        private void setupOverlay() {
            drawOverlay();
            this.mOverlay.setCallback(new android.graphics.drawable.Drawable.Callback() {
                public void invalidateDrawable(Drawable who) {
                    InternalPopupWindow.this.drawOverlay();
                    if (InternalPopupWindow.this.mCallback != null) {
                        InternalPopupWindow.this.updateCurrentContentForTesting();
                    }
                }

                public void scheduleDrawable(Drawable who, Runnable what, long when) {
                    Handler.getMain().postAtTime(what, who, when);
                }

                public void unscheduleDrawable(Drawable who, Runnable what) {
                    Handler.getMain().removeCallbacks(what, who);
                }
            });
        }

        private void drawOverlay() {
            RecordingCanvas canvas = this.mOverlayRenderNode.beginRecording(this.mContentWidth, this.mContentHeight);
            try {
                this.mOverlay.setBounds(0, 0, this.mContentWidth, this.mContentHeight);
                this.mOverlay.draw(canvas);
            } finally {
                this.mOverlayRenderNode.endRecording();
            }
        }

        public void setContentPositionForNextDraw(int contentX, int contentY) {
            this.mWindowPositionX = contentX - this.mOffsetX;
            this.mWindowPositionY = contentY - this.mOffsetY;
            this.mPendingWindowPositionUpdate = true;
            requestUpdate();
        }

        public void updateContent(Bitmap bitmap) {
            Bitmap bitmap2 = this.mBitmap;
            if (bitmap2 != null) {
                bitmap2.recycle();
            }
            this.mBitmap = bitmap;
            requestUpdate();
        }

        private void requestUpdate() {
            if (!this.mFrameDrawScheduled) {
                Message request = Message.obtain(this.mHandler, this.mMagnifierUpdater);
                request.setAsynchronous(true);
                request.sendToTarget();
                this.mFrameDrawScheduled = true;
            }
        }

        public void destroy() {
            this.mRenderer.destroy();
            this.mSurface.destroy();
            this.mSurfaceControl.remove();
            this.mSurfaceSession.kill();
            this.mHandler.removeCallbacks(this.mMagnifierUpdater);
            Bitmap bitmap = this.mBitmap;
            if (bitmap != null) {
                bitmap.recycle();
            }
        }

        /* JADX WARNING: Missing block: B:19:0x007f, code skipped:
            r12.mRenderer.draw(r2);
     */
        /* JADX WARNING: Missing block: B:20:0x0086, code skipped:
            if (r12.mCallback == null) goto L_0x0090;
     */
        /* JADX WARNING: Missing block: B:21:0x0088, code skipped:
            updateCurrentContentForTesting();
            r12.mCallback.onOperationComplete();
     */
        /* JADX WARNING: Missing block: B:22:0x0090, code skipped:
            return;
     */
        private void doDraw() {
            /*
            r12 = this;
            r0 = r12.mLock;
            monitor-enter(r0);
            r1 = r12.mSurface;	 Catch:{ all -> 0x0098 }
            r1 = r1.isValid();	 Catch:{ all -> 0x0098 }
            if (r1 != 0) goto L_0x000d;
        L_0x000b:
            monitor-exit(r0);	 Catch:{ all -> 0x0098 }
            return;
        L_0x000d:
            r1 = r12.mBitmapRenderNode;	 Catch:{ all -> 0x0098 }
            r2 = r12.mContentWidth;	 Catch:{ all -> 0x0098 }
            r3 = r12.mContentHeight;	 Catch:{ all -> 0x0098 }
            r1 = r1.beginRecording(r2, r3);	 Catch:{ all -> 0x0098 }
            r2 = new android.graphics.Rect;	 Catch:{ all -> 0x0091 }
            r3 = r12.mBitmap;	 Catch:{ all -> 0x0091 }
            r3 = r3.getWidth();	 Catch:{ all -> 0x0091 }
            r4 = r12.mBitmap;	 Catch:{ all -> 0x0091 }
            r4 = r4.getHeight();	 Catch:{ all -> 0x0091 }
            r5 = 0;
            r2.<init>(r5, r5, r3, r4);	 Catch:{ all -> 0x0091 }
            r3 = new android.graphics.Rect;	 Catch:{ all -> 0x0091 }
            r4 = r12.mContentWidth;	 Catch:{ all -> 0x0091 }
            r6 = r12.mContentHeight;	 Catch:{ all -> 0x0091 }
            r3.<init>(r5, r5, r4, r6);	 Catch:{ all -> 0x0091 }
            r4 = new android.graphics.Paint;	 Catch:{ all -> 0x0091 }
            r4.<init>();	 Catch:{ all -> 0x0091 }
            r6 = 1;
            r4.setFilterBitmap(r6);	 Catch:{ all -> 0x0091 }
            r6 = r12.mBitmap;	 Catch:{ all -> 0x0091 }
            r1.drawBitmap(r6, r2, r3, r4);	 Catch:{ all -> 0x0091 }
            r2 = r12.mBitmapRenderNode;	 Catch:{ all -> 0x0098 }
            r2.endRecording();	 Catch:{ all -> 0x0098 }
            r2 = r12.mPendingWindowPositionUpdate;	 Catch:{ all -> 0x0098 }
            if (r2 != 0) goto L_0x0051;
        L_0x004a:
            r2 = r12.mFirstDraw;	 Catch:{ all -> 0x0098 }
            if (r2 == 0) goto L_0x004f;
        L_0x004e:
            goto L_0x0051;
        L_0x004f:
            r2 = 0;
            goto L_0x006e;
        L_0x0051:
            r11 = r12.mFirstDraw;	 Catch:{ all -> 0x0098 }
            r12.mFirstDraw = r5;	 Catch:{ all -> 0x0098 }
            r8 = r12.mPendingWindowPositionUpdate;	 Catch:{ all -> 0x0098 }
            r12.mPendingWindowPositionUpdate = r5;	 Catch:{ all -> 0x0098 }
            r2 = r12.mWindowPositionX;	 Catch:{ all -> 0x0098 }
            r3 = r12.mWindowPositionY;	 Catch:{ all -> 0x0098 }
            r4 = new android.widget.-$$Lambda$Magnifier$InternalPopupWindow$qfjMrDJVvOQUv9_kKVdpLzbaJ-A;	 Catch:{ all -> 0x0098 }
            r6 = r4;
            r7 = r12;
            r9 = r2;
            r10 = r3;
            r6.<init>(r7, r8, r9, r10, r11);	 Catch:{ all -> 0x0098 }
            r6 = r12.mRenderer;	 Catch:{ all -> 0x0098 }
            r7 = r12.mDisplay;	 Catch:{ all -> 0x0098 }
            r6.setLightCenter(r7, r2, r3);	 Catch:{ all -> 0x0098 }
            r2 = r4;
        L_0x006e:
            r3 = r12.mWindowPositionX;	 Catch:{ all -> 0x0098 }
            r4 = r12.mOffsetX;	 Catch:{ all -> 0x0098 }
            r3 = r3 + r4;
            r12.mLastDrawContentPositionX = r3;	 Catch:{ all -> 0x0098 }
            r3 = r12.mWindowPositionY;	 Catch:{ all -> 0x0098 }
            r4 = r12.mOffsetY;	 Catch:{ all -> 0x0098 }
            r3 = r3 + r4;
            r12.mLastDrawContentPositionY = r3;	 Catch:{ all -> 0x0098 }
            r12.mFrameDrawScheduled = r5;	 Catch:{ all -> 0x0098 }
            monitor-exit(r0);	 Catch:{ all -> 0x0098 }
            r0 = r12.mRenderer;
            r0.draw(r2);
            r0 = r12.mCallback;
            if (r0 == 0) goto L_0x0090;
        L_0x0088:
            r12.updateCurrentContentForTesting();
            r0 = r12.mCallback;
            r0.onOperationComplete();
        L_0x0090:
            return;
        L_0x0091:
            r2 = move-exception;
            r3 = r12.mBitmapRenderNode;	 Catch:{ all -> 0x0098 }
            r3.endRecording();	 Catch:{ all -> 0x0098 }
            throw r2;	 Catch:{ all -> 0x0098 }
        L_0x0098:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0098 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Magnifier$InternalPopupWindow.doDraw():void");
        }

        public /* synthetic */ void lambda$doDraw$0$Magnifier$InternalPopupWindow(boolean updateWindowPosition, int pendingX, int pendingY, boolean firstDraw, long frame) {
            if (this.mSurface.isValid()) {
                SurfaceControl.openTransaction();
                this.mSurfaceControl.deferTransactionUntil(this.mSurface, frame);
                if (updateWindowPosition) {
                    this.mSurfaceControl.setPosition((float) pendingX, (float) pendingY);
                }
                if (firstDraw) {
                    this.mSurfaceControl.setLayer(5);
                    this.mSurfaceControl.show();
                }
                SurfaceControl.closeTransaction();
            }
        }

        private void updateCurrentContentForTesting() {
            Canvas canvas = new Canvas(this.mCurrentContent);
            Rect bounds = new Rect(0, 0, this.mContentWidth, this.mContentHeight);
            Bitmap bitmap = this.mBitmap;
            if (!(bitmap == null || bitmap.isRecycled())) {
                canvas.drawBitmap(this.mBitmap, new Rect(0, 0, this.mBitmap.getWidth(), this.mBitmap.getHeight()), bounds, null);
            }
            this.mOverlay.setBounds(bounds);
            this.mOverlay.draw(canvas);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SourceBound {
    }

    private static class SurfaceInfo {
        public static final SurfaceInfo NULL = new SurfaceInfo(null, null, 0, 0, null, false);
        private int mHeight;
        private Rect mInsets;
        private boolean mIsMainWindowSurface;
        private Surface mSurface;
        private SurfaceControl mSurfaceControl;
        private int mWidth;

        SurfaceInfo(SurfaceControl surfaceControl, Surface surface, int width, int height, Rect insets, boolean isMainWindowSurface) {
            this.mSurfaceControl = surfaceControl;
            this.mSurface = surface;
            this.mWidth = width;
            this.mHeight = height;
            this.mInsets = insets;
            this.mIsMainWindowSurface = isMainWindowSurface;
        }
    }

    static {
        sPixelCopyHandlerThread.start();
    }

    @Deprecated
    public Magnifier(View view) {
        this(createBuilderWithOldMagnifierDefaults(view));
    }

    static Builder createBuilderWithOldMagnifierDefaults(View view) {
        Builder params = new Builder(view);
        Context context = view.getContext();
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.Magnifier, R.attr.magnifierStyle, 0);
        params.mWidth = a.getDimensionPixelSize(5, 0);
        params.mHeight = a.getDimensionPixelSize(2, 0);
        params.mElevation = a.getDimension(1, 0.0f);
        params.mCornerRadius = getDeviceDefaultDialogCornerRadius(context);
        params.mZoom = a.getFloat(6, 0.0f);
        params.mHorizontalDefaultSourceToMagnifierOffset = a.getDimensionPixelSize(3, 0);
        params.mVerticalDefaultSourceToMagnifierOffset = a.getDimensionPixelSize(4, 0);
        params.mOverlay = new ColorDrawable(a.getColor(0, 0));
        a.recycle();
        params.mClippingEnabled = true;
        params.mLeftContentBound = 1;
        params.mTopContentBound = 0;
        params.mRightContentBound = 1;
        params.mBottomContentBound = 0;
        return params;
    }

    private static float getDeviceDefaultDialogCornerRadius(Context context) {
        TypedArray ta = new ContextThemeWrapper(context, 16974120).obtainStyledAttributes(new int[]{16844145});
        float dialogCornerRadius = ta.getDimension(0, 0.0f);
        ta.recycle();
        return dialogCornerRadius;
    }

    private Magnifier(Builder params) {
        this.mWindowCoords = new Point();
        this.mClampedCenterZoomCoords = new Point();
        this.mPrevStartCoordsInSurface = new Point(-1, -1);
        this.mPrevShowSourceCoords = new PointF(-1.0f, -1.0f);
        this.mPrevShowWindowCoords = new PointF(-1.0f, -1.0f);
        this.mPixelCopyRequestRect = new Rect();
        this.mLock = new Object();
        this.mDestroyLock = new Object();
        this.mView = params.mView;
        this.mWindowWidth = params.mWidth;
        this.mWindowHeight = params.mHeight;
        this.mZoom = params.mZoom;
        this.mSourceWidth = Math.round(((float) this.mWindowWidth) / this.mZoom);
        this.mSourceHeight = Math.round(((float) this.mWindowHeight) / this.mZoom);
        this.mWindowElevation = params.mElevation;
        this.mWindowCornerRadius = params.mCornerRadius;
        this.mOverlay = params.mOverlay;
        this.mDefaultHorizontalSourceToMagnifierOffset = params.mHorizontalDefaultSourceToMagnifierOffset;
        this.mDefaultVerticalSourceToMagnifierOffset = params.mVerticalDefaultSourceToMagnifierOffset;
        this.mClippingEnabled = params.mClippingEnabled;
        this.mLeftContentBound = params.mLeftContentBound;
        this.mTopContentBound = params.mTopContentBound;
        this.mRightContentBound = params.mRightContentBound;
        this.mBottomContentBound = params.mBottomContentBound;
        this.mViewCoordinatesInSurface = new int[2];
    }

    public void show(float sourceCenterX, float sourceCenterY) {
        show(sourceCenterX, sourceCenterY, ((float) this.mDefaultHorizontalSourceToMagnifierOffset) + sourceCenterX, ((float) this.mDefaultVerticalSourceToMagnifierOffset) + sourceCenterY);
    }

    public void show(float sourceCenterX, float sourceCenterY, float magnifierCenterX, float magnifierCenterY) {
        float f = sourceCenterX;
        float f2 = sourceCenterY;
        float f3 = magnifierCenterX;
        float f4 = magnifierCenterY;
        obtainSurfaces();
        obtainContentCoordinates(sourceCenterX, sourceCenterY);
        obtainWindowCoordinates(f3, f4);
        int startX = this.mClampedCenterZoomCoords.x - (this.mSourceWidth / 2);
        int startY = this.mClampedCenterZoomCoords.y - (this.mSourceHeight / 2);
        if (f != this.mPrevShowSourceCoords.x || f2 != this.mPrevShowSourceCoords.y || this.mDirtyState) {
            if (this.mWindow == null) {
                synchronized (this.mLock) {
                    this.mWindow = new InternalPopupWindow(this.mView.getContext(), this.mView.getDisplay(), this.mParentSurface.mSurfaceControl, this.mWindowWidth, this.mWindowHeight, this.mWindowElevation, this.mWindowCornerRadius, this.mOverlay != null ? this.mOverlay : new ColorDrawable(0), Handler.getMain(), this.mLock, this.mCallback);
                }
            }
            performPixelCopy(startX, startY, true);
        } else if (!(f3 == this.mPrevShowWindowCoords.x && f4 == this.mPrevShowWindowCoords.y)) {
            sPixelCopyHandlerThread.getThreadHandler().post(new -$$Lambda$Magnifier$sEUKNU2_gseoDMBt_HOs-JGAfZ8(this, this.mWindow, getCurrentClampedWindowCoordinates()));
        }
        PointF pointF = this.mPrevShowSourceCoords;
        pointF.x = f;
        pointF.y = f2;
        pointF = this.mPrevShowWindowCoords;
        pointF.x = f3;
        pointF.y = magnifierCenterY;
    }

    public /* synthetic */ void lambda$show$0$Magnifier(InternalPopupWindow currentWindowInstance, Point windowCoords) {
        synchronized (this.mLock) {
            if (this.mWindow != currentWindowInstance) {
                return;
            }
            this.mWindow.setContentPositionForNextDraw(windowCoords.x, windowCoords.y);
        }
    }

    public void dismiss() {
        if (this.mWindow != null) {
            synchronized (this.mLock) {
                this.mWindow.destroy();
                this.mWindow = null;
            }
            PointF pointF = this.mPrevShowSourceCoords;
            pointF.x = -1.0f;
            pointF.y = -1.0f;
            pointF = this.mPrevShowWindowCoords;
            pointF.x = -1.0f;
            pointF.y = -1.0f;
            Point point = this.mPrevStartCoordsInSurface;
            point.x = -1;
            point.y = -1;
        }
    }

    public void update() {
        if (this.mWindow != null) {
            obtainSurfaces();
            if (this.mDirtyState) {
                show(this.mPrevShowSourceCoords.x, this.mPrevShowSourceCoords.y, this.mPrevShowWindowCoords.x, this.mPrevShowWindowCoords.y);
            } else {
                performPixelCopy(this.mPrevStartCoordsInSurface.x, this.mPrevStartCoordsInSurface.y, false);
            }
        }
    }

    public int getWidth() {
        return this.mWindowWidth;
    }

    public int getHeight() {
        return this.mWindowHeight;
    }

    public int getSourceWidth() {
        return this.mSourceWidth;
    }

    public int getSourceHeight() {
        return this.mSourceHeight;
    }

    public void setZoom(float zoom) {
        Preconditions.checkArgumentPositive(zoom, "Zoom should be positive");
        this.mZoom = zoom;
        this.mSourceWidth = Math.round(((float) this.mWindowWidth) / this.mZoom);
        this.mSourceHeight = Math.round(((float) this.mWindowHeight) / this.mZoom);
        this.mDirtyState = true;
    }

    public float getZoom() {
        return this.mZoom;
    }

    public float getElevation() {
        return this.mWindowElevation;
    }

    public float getCornerRadius() {
        return this.mWindowCornerRadius;
    }

    public int getDefaultHorizontalSourceToMagnifierOffset() {
        return this.mDefaultHorizontalSourceToMagnifierOffset;
    }

    public int getDefaultVerticalSourceToMagnifierOffset() {
        return this.mDefaultVerticalSourceToMagnifierOffset;
    }

    public Drawable getOverlay() {
        return this.mOverlay;
    }

    public boolean isClippingEnabled() {
        return this.mClippingEnabled;
    }

    public Point getPosition() {
        if (this.mWindow == null) {
            return null;
        }
        Point position = getCurrentClampedWindowCoordinates();
        position.offset(-this.mParentSurface.mInsets.left, -this.mParentSurface.mInsets.top);
        return new Point(position);
    }

    public Point getSourcePosition() {
        if (this.mWindow == null) {
            return null;
        }
        Point position = new Point(this.mPixelCopyRequestRect.left, this.mPixelCopyRequestRect.top);
        position.offset(-this.mContentCopySurface.mInsets.left, -this.mContentCopySurface.mInsets.top);
        return new Point(position);
    }

    private void obtainSurfaces() {
        SurfaceInfo validMainWindowSurface = SurfaceInfo.NULL;
        if (this.mView.getViewRootImpl() != null) {
            ViewRootImpl viewRootImpl = this.mView.getViewRootImpl();
            Surface mainWindowSurface = viewRootImpl.mSurface;
            if (mainWindowSurface != null && mainWindowSurface.isValid()) {
                Rect surfaceInsets = viewRootImpl.mWindowAttributes.surfaceInsets;
                validMainWindowSurface = new SurfaceInfo(viewRootImpl.getSurfaceControl(), mainWindowSurface, (viewRootImpl.getWidth() + surfaceInsets.left) + surfaceInsets.right, (viewRootImpl.getHeight() + surfaceInsets.top) + surfaceInsets.bottom, surfaceInsets, true);
            }
        }
        SurfaceInfo validSurfaceViewSurface = SurfaceInfo.NULL;
        View view = this.mView;
        if (view instanceof SurfaceView) {
            SurfaceControl sc = ((SurfaceView) view).getSurfaceControl();
            SurfaceHolder surfaceHolder = ((SurfaceView) this.mView).getHolder();
            Surface surfaceViewSurface = surfaceHolder.getSurface();
            if (sc != null && sc.isValid()) {
                Rect surfaceFrame = surfaceHolder.getSurfaceFrame();
                validSurfaceViewSurface = new SurfaceInfo(sc, surfaceViewSurface, surfaceFrame.right, surfaceFrame.bottom, new Rect(), false);
            }
        }
        this.mParentSurface = validMainWindowSurface != SurfaceInfo.NULL ? validMainWindowSurface : validSurfaceViewSurface;
        this.mContentCopySurface = this.mView instanceof SurfaceView ? validSurfaceViewSurface : validMainWindowSurface;
    }

    private void obtainContentCoordinates(float xPosInView, float yPosInView) {
        int zoomCenterX;
        int zoomCenterY;
        int i;
        int i2;
        int i3;
        int[] iArr = this.mViewCoordinatesInSurface;
        int prevViewXInSurface = iArr[0];
        int prevViewYInSurface = iArr[1];
        this.mView.getLocationInSurface(iArr);
        iArr = this.mViewCoordinatesInSurface;
        if (!(iArr[0] == prevViewXInSurface && iArr[1] == prevViewYInSurface)) {
            this.mDirtyState = true;
        }
        if (this.mView instanceof SurfaceView) {
            zoomCenterX = Math.round(xPosInView);
            zoomCenterY = Math.round(yPosInView);
        } else {
            zoomCenterX = Math.round(xPosInView + ((float) this.mViewCoordinatesInSurface[0]));
            zoomCenterY = Math.round(yPosInView + ((float) this.mViewCoordinatesInSurface[1]));
        }
        Rect[] bounds = new Rect[2];
        bounds[0] = new Rect(0, 0, this.mContentCopySurface.mWidth, this.mContentCopySurface.mHeight);
        Rect viewVisibleRegion = new Rect();
        this.mView.getGlobalVisibleRect(viewVisibleRegion);
        if (this.mView.getViewRootImpl() != null) {
            Rect surfaceInsets = this.mView.getViewRootImpl().mWindowAttributes.surfaceInsets;
            viewVisibleRegion.offset(surfaceInsets.left, surfaceInsets.top);
        }
        if (this.mView instanceof SurfaceView) {
            int[] iArr2 = this.mViewCoordinatesInSurface;
            viewVisibleRegion.offset(-iArr2[0], -iArr2[1]);
        }
        bounds[1] = viewVisibleRegion;
        int resolvedLeft = Integer.MIN_VALUE;
        for (i = this.mLeftContentBound; i >= 0; i--) {
            resolvedLeft = Math.max(resolvedLeft, bounds[i].left);
        }
        i = Integer.MIN_VALUE;
        for (i2 = this.mTopContentBound; i2 >= 0; i2--) {
            i = Math.max(i, bounds[i2].top);
        }
        i2 = Integer.MAX_VALUE;
        for (i3 = this.mRightContentBound; i3 >= 0; i3--) {
            i2 = Math.min(i2, bounds[i3].right);
        }
        i3 = Integer.MAX_VALUE;
        for (int i4 = this.mBottomContentBound; i4 >= 0; i4--) {
            i3 = Math.min(i3, bounds[i4].bottom);
        }
        resolvedLeft = Math.min(resolvedLeft, this.mContentCopySurface.mWidth - this.mSourceWidth);
        i = Math.min(i, this.mContentCopySurface.mHeight - this.mSourceHeight);
        if (resolvedLeft < 0 || i < 0) {
            Log.e(TAG, "Magnifier's content is copied from a surface smaller thanthe content requested size. The magnifier will be dismissed.");
        }
        i2 = Math.max(i2, this.mSourceWidth + resolvedLeft);
        i3 = Math.max(i3, this.mSourceHeight + i);
        Point point = this.mClampedCenterZoomCoords;
        int i5 = this.mSourceWidth;
        point.x = Math.max((i5 / 2) + resolvedLeft, Math.min(zoomCenterX, i2 - (i5 / 2)));
        point = this.mClampedCenterZoomCoords;
        i5 = this.mSourceHeight;
        point.y = Math.max((i5 / 2) + i, Math.min(zoomCenterY, i3 - (i5 / 2)));
    }

    private void obtainWindowCoordinates(float xWindowPos, float yWindowPos) {
        int windowCenterX;
        int windowCenterY;
        if (this.mView instanceof SurfaceView) {
            windowCenterX = Math.round(xWindowPos);
            windowCenterY = Math.round(yWindowPos);
        } else {
            windowCenterX = Math.round(((float) this.mViewCoordinatesInSurface[0]) + xWindowPos);
            windowCenterY = Math.round(((float) this.mViewCoordinatesInSurface[1]) + yWindowPos);
        }
        Point point = this.mWindowCoords;
        point.x = windowCenterX - (this.mWindowWidth / 2);
        point.y = windowCenterY - (this.mWindowHeight / 2);
        if (this.mParentSurface != this.mContentCopySurface) {
            point.x += this.mViewCoordinatesInSurface[0];
            Point point2 = this.mWindowCoords;
            point2.y += this.mViewCoordinatesInSurface[1];
        }
    }

    private void performPixelCopy(int startXInSurface, int startYInSurface, boolean updateWindowPosition) {
        if (this.mContentCopySurface.mSurface == null || !this.mContentCopySurface.mSurface.isValid()) {
            onPixelCopyFailed();
            return;
        }
        Point windowCoords = getCurrentClampedWindowCoordinates();
        this.mPixelCopyRequestRect.set(startXInSurface, startYInSurface, this.mSourceWidth + startXInSurface, this.mSourceHeight + startYInSurface);
        InternalPopupWindow currentWindowInstance = this.mWindow;
        Bitmap bitmap = Bitmap.createBitmap(this.mSourceWidth, this.mSourceHeight, Config.ARGB_8888);
        PixelCopy.request(this.mContentCopySurface.mSurface, this.mPixelCopyRequestRect, bitmap, new -$$Lambda$Magnifier$K0um0QSTAb4wXwua60CgJIIwGaI(this, currentWindowInstance, updateWindowPosition, windowCoords, bitmap), sPixelCopyHandlerThread.getThreadHandler());
        Point point = this.mPrevStartCoordsInSurface;
        point.x = startXInSurface;
        point.y = startYInSurface;
        this.mDirtyState = false;
    }

    public /* synthetic */ void lambda$performPixelCopy$1$Magnifier(InternalPopupWindow currentWindowInstance, boolean updateWindowPosition, Point windowCoords, Bitmap bitmap, int result) {
        if (result != 0) {
            onPixelCopyFailed();
            return;
        }
        synchronized (this.mLock) {
            if (this.mWindow != currentWindowInstance) {
                return;
            }
            if (updateWindowPosition) {
                this.mWindow.setContentPositionForNextDraw(windowCoords.x, windowCoords.y);
            }
            this.mWindow.updateContent(bitmap);
        }
    }

    private void onPixelCopyFailed() {
        Log.e(TAG, "Magnifier failed to copy content from the view Surface. It will be dismissed.");
        Handler.getMain().postAtFrontOfQueue(new -$$Lambda$Magnifier$esRj9C7NyDvOX8eqqqLKuB6jpTw(this));
    }

    public /* synthetic */ void lambda$onPixelCopyFailed$2$Magnifier() {
        dismiss();
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onOperationComplete();
        }
    }

    private Point getCurrentClampedWindowCoordinates() {
        if (!this.mClippingEnabled) {
            return new Point(this.mWindowCoords);
        }
        Rect windowBounds;
        if (this.mParentSurface.mIsMainWindowSurface) {
            Insets systemInsets = this.mView.getRootWindowInsets().getSystemWindowInsets();
            windowBounds = new Rect(systemInsets.left + this.mParentSurface.mInsets.left, systemInsets.top + this.mParentSurface.mInsets.top, (this.mParentSurface.mWidth - systemInsets.right) - this.mParentSurface.mInsets.right, (this.mParentSurface.mHeight - systemInsets.bottom) - this.mParentSurface.mInsets.bottom);
        } else {
            windowBounds = new Rect(0, 0, this.mParentSurface.mWidth, this.mParentSurface.mHeight);
        }
        return new Point(Math.max(windowBounds.left, Math.min(windowBounds.right - this.mWindowWidth, this.mWindowCoords.x)), Math.max(windowBounds.top, Math.min(windowBounds.bottom - this.mWindowHeight, this.mWindowCoords.y)));
    }

    public void setOnOperationCompleteCallback(Callback callback) {
        this.mCallback = callback;
        InternalPopupWindow internalPopupWindow = this.mWindow;
        if (internalPopupWindow != null) {
            internalPopupWindow.mCallback = callback;
        }
    }

    public Bitmap getContent() {
        InternalPopupWindow internalPopupWindow = this.mWindow;
        if (internalPopupWindow == null) {
            return null;
        }
        Bitmap access$2500;
        synchronized (internalPopupWindow.mLock) {
            access$2500 = this.mWindow.mCurrentContent;
        }
        return access$2500;
    }

    public Bitmap getOriginalContent() {
        InternalPopupWindow internalPopupWindow = this.mWindow;
        if (internalPopupWindow == null) {
            return null;
        }
        Bitmap createBitmap;
        synchronized (internalPopupWindow.mLock) {
            createBitmap = Bitmap.createBitmap(this.mWindow.mBitmap);
        }
        return createBitmap;
    }

    public static PointF getMagnifierDefaultSize() {
        Resources resources = Resources.getSystem();
        float density = resources.getDisplayMetrics().density;
        PointF size = new PointF();
        size.x = resources.getDimension(R.dimen.default_magnifier_width) / density;
        size.y = resources.getDimension(R.dimen.default_magnifier_height) / density;
        return size;
    }
}
