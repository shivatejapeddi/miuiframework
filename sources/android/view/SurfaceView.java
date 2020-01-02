package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.CompatibilityInfo.Translator;
import android.content.res.Configuration;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.graphics.RenderNode.PositionUpdateListener;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceControl.Transaction;
import android.view.SurfaceHolder.Callback;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class SurfaceView extends View implements WindowStoppedCallback {
    private static final boolean DEBUG = false;
    private static final String TAG = "SurfaceView";
    private boolean mAttachedToWindow;
    SurfaceControl mBackgroundControl;
    @UnsupportedAppUsage
    final ArrayList<Callback> mCallbacks;
    final Configuration mConfiguration;
    float mCornerRadius;
    SurfaceControl mDeferredDestroySurfaceControl;
    boolean mDrawFinished;
    @UnsupportedAppUsage
    private final OnPreDrawListener mDrawListener;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    boolean mDrawingStopped;
    @UnsupportedAppUsage
    int mFormat;
    private boolean mGlobalListenersAdded;
    @UnsupportedAppUsage
    boolean mHaveFrame;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    boolean mIsCreating;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    long mLastLockTime;
    int mLastSurfaceHeight;
    int mLastSurfaceWidth;
    boolean mLastWindowVisibility;
    final int[] mLocation;
    private int mPendingReportDraws;
    private PositionUpdateListener mPositionListener;
    private Rect mRTLastReportedPosition;
    @UnsupportedAppUsage
    int mRequestedFormat;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    int mRequestedHeight;
    boolean mRequestedVisible;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    int mRequestedWidth;
    Paint mRoundedViewportPaint;
    private volatile boolean mRtHandlingPositionUpdates;
    private Transaction mRtTransaction;
    final Rect mScreenRect;
    private final OnScrollChangedListener mScrollChangedListener;
    int mSubLayer;
    @UnsupportedAppUsage
    final Surface mSurface;
    SurfaceControl mSurfaceControl;
    boolean mSurfaceCreated;
    private int mSurfaceFlags;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    final Rect mSurfaceFrame;
    int mSurfaceHeight;
    @UnsupportedAppUsage
    private final SurfaceHolder mSurfaceHolder;
    @UnsupportedAppUsage
    final ReentrantLock mSurfaceLock;
    SurfaceSession mSurfaceSession;
    int mSurfaceWidth;
    final Rect mTmpRect;
    private Translator mTranslator;
    boolean mViewVisibility;
    boolean mVisible;
    int mWindowSpaceLeft;
    int mWindowSpaceTop;
    boolean mWindowStopped;
    boolean mWindowVisibility;

    public SurfaceView(Context context) {
        this(context, null);
    }

    public SurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCallbacks = new ArrayList();
        this.mLocation = new int[2];
        this.mSurfaceLock = new ReentrantLock();
        this.mSurface = new Surface();
        this.mDrawingStopped = true;
        this.mDrawFinished = false;
        this.mScreenRect = new Rect();
        this.mTmpRect = new Rect();
        this.mConfiguration = new Configuration();
        this.mSubLayer = -2;
        this.mIsCreating = false;
        this.mRtHandlingPositionUpdates = false;
        this.mScrollChangedListener = new OnScrollChangedListener() {
            public void onScrollChanged() {
                SurfaceView.this.updateSurface();
            }
        };
        this.mDrawListener = new OnPreDrawListener() {
            public boolean onPreDraw() {
                SurfaceView surfaceView = SurfaceView.this;
                boolean z = surfaceView.getWidth() > 0 && SurfaceView.this.getHeight() > 0;
                surfaceView.mHaveFrame = z;
                SurfaceView.this.updateSurface();
                return true;
            }
        };
        this.mRequestedVisible = false;
        this.mWindowVisibility = false;
        this.mLastWindowVisibility = false;
        this.mViewVisibility = false;
        this.mWindowStopped = false;
        this.mRequestedWidth = -1;
        this.mRequestedHeight = -1;
        this.mRequestedFormat = 4;
        this.mHaveFrame = false;
        this.mSurfaceCreated = false;
        this.mLastLockTime = 0;
        this.mVisible = false;
        this.mWindowSpaceLeft = -1;
        this.mWindowSpaceTop = -1;
        this.mSurfaceWidth = -1;
        this.mSurfaceHeight = -1;
        this.mFormat = -1;
        this.mSurfaceFrame = new Rect();
        this.mLastSurfaceWidth = -1;
        this.mLastSurfaceHeight = -1;
        this.mSurfaceFlags = 4;
        this.mRtTransaction = new Transaction();
        this.mRTLastReportedPosition = new Rect();
        this.mPositionListener = new PositionUpdateListener() {
            public void positionChanged(long frameNumber, int left, int top, int right, int bottom) {
                if (SurfaceView.this.mSurfaceControl != null) {
                    SurfaceView.this.mRtHandlingPositionUpdates = true;
                    if (SurfaceView.this.mRTLastReportedPosition.left != left || SurfaceView.this.mRTLastReportedPosition.top != top || SurfaceView.this.mRTLastReportedPosition.right != right || SurfaceView.this.mRTLastReportedPosition.bottom != bottom) {
                        try {
                            SurfaceView.this.mRTLastReportedPosition.set(left, top, right, bottom);
                            SurfaceView.this.setParentSpaceRectangle(SurfaceView.this.mRTLastReportedPosition, frameNumber);
                        } catch (Exception ex) {
                            Log.e(SurfaceView.TAG, "Exception from repositionChild", ex);
                        }
                    }
                }
            }

            public void positionLost(long frameNumber) {
                SurfaceView.this.mRTLastReportedPosition.setEmpty();
                if (SurfaceView.this.mSurfaceControl != null) {
                    if (frameNumber > 0) {
                        SurfaceView.this.mRtTransaction.deferTransactionUntilSurface(SurfaceView.this.mSurfaceControl, SurfaceView.this.getViewRootImpl().mSurface, frameNumber);
                    }
                    SurfaceView.this.mRtTransaction.hide(SurfaceView.this.mSurfaceControl);
                    SurfaceView.this.mRtTransaction.apply();
                }
            }
        };
        this.mSurfaceHolder = new SurfaceHolder() {
            private static final String LOG_TAG = "SurfaceHolder";

            public boolean isCreating() {
                return SurfaceView.this.mIsCreating;
            }

            public void addCallback(Callback callback) {
                synchronized (SurfaceView.this.mCallbacks) {
                    if (!SurfaceView.this.mCallbacks.contains(callback)) {
                        SurfaceView.this.mCallbacks.add(callback);
                    }
                }
            }

            public void removeCallback(Callback callback) {
                synchronized (SurfaceView.this.mCallbacks) {
                    SurfaceView.this.mCallbacks.remove(callback);
                }
            }

            public void setFixedSize(int width, int height) {
                if (SurfaceView.this.mRequestedWidth != width || SurfaceView.this.mRequestedHeight != height) {
                    SurfaceView surfaceView = SurfaceView.this;
                    surfaceView.mRequestedWidth = width;
                    surfaceView.mRequestedHeight = height;
                    surfaceView.requestLayout();
                }
            }

            public void setSizeFromLayout() {
                if (SurfaceView.this.mRequestedWidth != -1 || SurfaceView.this.mRequestedHeight != -1) {
                    SurfaceView surfaceView = SurfaceView.this;
                    surfaceView.mRequestedHeight = -1;
                    surfaceView.mRequestedWidth = -1;
                    surfaceView.requestLayout();
                }
            }

            public void setFormat(int format) {
                if (format == -1) {
                    format = 4;
                }
                SurfaceView surfaceView = SurfaceView.this;
                surfaceView.mRequestedFormat = format;
                if (surfaceView.mSurfaceControl != null) {
                    SurfaceView.this.updateSurface();
                }
            }

            @Deprecated
            public void setType(int type) {
            }

            public /* synthetic */ void lambda$setKeepScreenOn$0$SurfaceView$4(boolean screenOn) {
                SurfaceView.this.setKeepScreenOn(screenOn);
            }

            public void setKeepScreenOn(boolean screenOn) {
                SurfaceView.this.runOnUiThread(new -$$Lambda$SurfaceView$4$wAwzCgpoBmqWbw6GlT0xJXSxjm4(this, screenOn));
            }

            public Canvas lockCanvas() {
                return internalLockCanvas(null, false);
            }

            public Canvas lockCanvas(Rect inOutDirty) {
                return internalLockCanvas(inOutDirty, false);
            }

            public Canvas lockHardwareCanvas() {
                return internalLockCanvas(null, true);
            }

            private Canvas internalLockCanvas(Rect dirty, boolean hardware) {
                SurfaceView.this.mSurfaceLock.lock();
                Canvas c = null;
                if (!(SurfaceView.this.mDrawingStopped || SurfaceView.this.mSurfaceControl == null)) {
                    if (hardware) {
                        try {
                            c = SurfaceView.this.mSurface.lockHardwareCanvas();
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "Exception locking surface", e);
                        }
                    } else {
                        c = SurfaceView.this.mSurface.lockCanvas(dirty);
                    }
                }
                if (c != null) {
                    SurfaceView.this.mLastLockTime = SystemClock.uptimeMillis();
                    return c;
                }
                long now = SystemClock.uptimeMillis();
                long nextTime = SurfaceView.this.mLastLockTime + 100;
                if (nextTime > now) {
                    try {
                        Thread.sleep(nextTime - now);
                    } catch (InterruptedException e2) {
                    }
                    now = SystemClock.uptimeMillis();
                }
                SurfaceView surfaceView = SurfaceView.this;
                surfaceView.mLastLockTime = now;
                surfaceView.mSurfaceLock.unlock();
                return null;
            }

            public void unlockCanvasAndPost(Canvas canvas) {
                SurfaceView.this.mSurface.unlockCanvasAndPost(canvas);
                SurfaceView.this.mSurfaceLock.unlock();
            }

            public Surface getSurface() {
                return SurfaceView.this.mSurface;
            }

            public Rect getSurfaceFrame() {
                return SurfaceView.this.mSurfaceFrame;
            }
        };
        this.mRenderNode.addPositionUpdateListener(this.mPositionListener);
        setWillNotDraw(true);
    }

    public SurfaceHolder getHolder() {
        return this.mSurfaceHolder;
    }

    private void updateRequestedVisibility() {
        boolean z = this.mViewVisibility && this.mWindowVisibility && !this.mWindowStopped;
        this.mRequestedVisible = z;
    }

    public void windowStopped(boolean stopped) {
        this.mWindowStopped = stopped;
        updateRequestedVisibility();
        updateSurface();
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewRootImpl().addWindowStoppedCallback(this);
        boolean z = false;
        this.mWindowStopped = false;
        if (getVisibility() == 0) {
            z = true;
        }
        this.mViewVisibility = z;
        updateRequestedVisibility();
        this.mAttachedToWindow = true;
        this.mParent.requestTransparentRegion(this);
        if (!this.mGlobalListenersAdded) {
            ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnScrollChangedListener(this.mScrollChangedListener);
            observer.addOnPreDrawListener(this.mDrawListener);
            this.mGlobalListenersAdded = true;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.mWindowVisibility = visibility == 0;
        updateRequestedVisibility();
        updateSurface();
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        boolean newRequestedVisible = true;
        this.mViewVisibility = visibility == 0;
        if (!(this.mWindowVisibility && this.mViewVisibility && !this.mWindowStopped)) {
            newRequestedVisible = false;
        }
        if (newRequestedVisible != this.mRequestedVisible) {
            requestLayout();
        }
        this.mRequestedVisible = newRequestedVisible;
        updateSurface();
    }

    private void performDrawFinished() {
        if (this.mPendingReportDraws > 0) {
            this.mDrawFinished = true;
            if (this.mAttachedToWindow) {
                notifyDrawFinished();
                invalidate();
                return;
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.identityHashCode(this));
        stringBuilder.append("finished drawing but no pending report draw (extra call to draw completion runnable?)");
        Log.e(TAG, stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyDrawFinished() {
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot != null) {
            viewRoot.pendingDrawFinished();
        }
        this.mPendingReportDraws--;
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot != null) {
            viewRoot.removeWindowStoppedCallback(this);
        }
        this.mAttachedToWindow = false;
        if (this.mGlobalListenersAdded) {
            ViewTreeObserver observer = getViewTreeObserver();
            observer.removeOnScrollChangedListener(this.mScrollChangedListener);
            observer.removeOnPreDrawListener(this.mDrawListener);
            this.mGlobalListenersAdded = false;
        }
        while (this.mPendingReportDraws > 0) {
            notifyDrawFinished();
        }
        this.mRequestedVisible = false;
        updateSurface();
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl != null) {
            surfaceControl.remove();
        }
        this.mSurfaceControl = null;
        this.mHaveFrame = false;
        super.onDetachedFromWindow();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int i = this.mRequestedWidth;
        if (i >= 0) {
            i = View.resolveSizeAndState(i, widthMeasureSpec, 0);
        } else {
            i = View.getDefaultSize(0, widthMeasureSpec);
        }
        int i2 = this.mRequestedHeight;
        if (i2 >= 0) {
            height = View.resolveSizeAndState(i2, heightMeasureSpec, 0);
        } else {
            height = View.getDefaultSize(0, heightMeasureSpec);
        }
        setMeasuredDimension(i, height);
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public boolean setFrame(int left, int top, int right, int bottom) {
        boolean result = super.setFrame(left, top, right, bottom);
        updateSurface();
        return result;
    }

    public boolean gatherTransparentRegion(Region region) {
        if (isAboveParent() || !this.mDrawFinished) {
            return super.gatherTransparentRegion(region);
        }
        boolean opaque = true;
        if ((this.mPrivateFlags & 128) == 0) {
            opaque = super.gatherTransparentRegion(region);
        } else if (region != null) {
            int w = getWidth();
            int h = getHeight();
            if (w > 0 && h > 0) {
                getLocationInWindow(this.mLocation);
                int t = this.mLocation;
                int l = t[0];
                t = t[1];
                region.op(l, t, l + w, t + h, Op.UNION);
            }
        }
        if (PixelFormat.formatHasAlpha(this.mRequestedFormat)) {
            opaque = false;
        }
        return opaque;
    }

    public void draw(Canvas canvas) {
        if (this.mDrawFinished && !isAboveParent() && (this.mPrivateFlags & 128) == 0) {
            clearSurfaceViewPort(canvas);
        }
        super.draw(canvas);
    }

    /* Access modifiers changed, original: protected */
    public void dispatchDraw(Canvas canvas) {
        if (this.mDrawFinished && !isAboveParent() && (this.mPrivateFlags & 128) == 128) {
            clearSurfaceViewPort(canvas);
        }
        super.dispatchDraw(canvas);
    }

    private void clearSurfaceViewPort(Canvas canvas) {
        if (this.mCornerRadius > 0.0f) {
            canvas.getClipBounds(this.mTmpRect);
            float f = (float) this.mTmpRect.left;
            float f2 = (float) this.mTmpRect.top;
            float f3 = (float) this.mTmpRect.right;
            float f4 = (float) this.mTmpRect.bottom;
            float f5 = this.mCornerRadius;
            canvas.drawRoundRect(f, f2, f3, f4, f5, f5, this.mRoundedViewportPaint);
            return;
        }
        canvas.drawColor(0, Mode.CLEAR);
    }

    public void setCornerRadius(float cornerRadius) {
        this.mCornerRadius = cornerRadius;
        if (this.mCornerRadius > 0.0f && this.mRoundedViewportPaint == null) {
            this.mRoundedViewportPaint = new Paint(1);
            this.mRoundedViewportPaint.setBlendMode(BlendMode.CLEAR);
            this.mRoundedViewportPaint.setColor(0);
        }
        invalidate();
    }

    public void setZOrderMediaOverlay(boolean isMediaOverlay) {
        this.mSubLayer = isMediaOverlay ? -1 : -2;
    }

    public void setZOrderOnTop(boolean onTop) {
        if (onTop) {
            this.mSubLayer = 1;
        } else {
            this.mSubLayer = -2;
        }
    }

    public void setSecure(boolean isSecure) {
        if (isSecure) {
            this.mSurfaceFlags |= 128;
        } else {
            this.mSurfaceFlags &= -129;
        }
    }

    private void updateOpaqueFlag() {
        if (PixelFormat.formatHasAlpha(this.mRequestedFormat)) {
            this.mSurfaceFlags &= -1025;
        } else {
            this.mSurfaceFlags |= 1024;
        }
    }

    private Rect getParentSurfaceInsets() {
        ViewRootImpl root = getViewRootImpl();
        if (root == null) {
            return null;
        }
        return root.mWindowAttributes.surfaceInsets;
    }

    private void updateBackgroundVisibilityInTransaction(SurfaceControl viewRoot) {
        SurfaceControl surfaceControl = this.mBackgroundControl;
        if (surfaceControl != null) {
            if (this.mSubLayer >= 0 || (this.mSurfaceFlags & 1024) == 0) {
                this.mBackgroundControl.hide();
            } else {
                surfaceControl.show();
                this.mBackgroundControl.setRelativeLayer(viewRoot, Integer.MIN_VALUE);
            }
        }
    }

    private void releaseSurfaces() {
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl != null) {
            surfaceControl.remove();
            this.mSurfaceControl = null;
        }
        surfaceControl = this.mBackgroundControl;
        if (surfaceControl != null) {
            surfaceControl.remove();
            this.mBackgroundControl = null;
        }
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:254:0x044f A:{Catch:{ all -> 0x046f }} */
    /* JADX WARNING: Removed duplicated region for block: B:197:0x03a2 A:{Catch:{ all -> 0x039e, all -> 0x03ae }} */
    /* JADX WARNING: Removed duplicated region for block: B:177:0x0353  */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x03a6 A:{Catch:{ all -> 0x039e, all -> 0x03ae }} */
    /* JADX WARNING: Removed duplicated region for block: B:207:0x03ce A:{SYNTHETIC, Splitter:B:207:0x03ce} */
    /* JADX WARNING: Removed duplicated region for block: B:166:0x033c A:{SYNTHETIC, Splitter:B:166:0x033c} */
    /* JADX WARNING: Removed duplicated region for block: B:177:0x0353  */
    /* JADX WARNING: Removed duplicated region for block: B:197:0x03a2 A:{Catch:{ all -> 0x039e, all -> 0x03ae }} */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x03a6 A:{Catch:{ all -> 0x039e, all -> 0x03ae }} */
    /* JADX WARNING: Removed duplicated region for block: B:207:0x03ce A:{SYNTHETIC, Splitter:B:207:0x03ce} */
    /* JADX WARNING: Removed duplicated region for block: B:151:0x02ff A:{SYNTHETIC, Splitter:B:151:0x02ff} */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x02e9 A:{SYNTHETIC, Splitter:B:147:0x02e9} */
    /* JADX WARNING: Removed duplicated region for block: B:155:0x0327 A:{SYNTHETIC, Splitter:B:155:0x0327} */
    /* JADX WARNING: Removed duplicated region for block: B:166:0x033c A:{SYNTHETIC, Splitter:B:166:0x033c} */
    /* JADX WARNING: Removed duplicated region for block: B:197:0x03a2 A:{Catch:{ all -> 0x039e, all -> 0x03ae }} */
    /* JADX WARNING: Removed duplicated region for block: B:177:0x0353  */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x03a6 A:{Catch:{ all -> 0x039e, all -> 0x03ae }} */
    /* JADX WARNING: Removed duplicated region for block: B:207:0x03ce A:{SYNTHETIC, Splitter:B:207:0x03ce} */
    public void updateSurface() {
        /*
        r23 = this;
        r1 = r23;
        r0 = r1.mHaveFrame;
        if (r0 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r2 = r23.getViewRootImpl();
        if (r2 == 0) goto L_0x0502;
    L_0x000d:
        r0 = r2.mSurface;
        if (r0 == 0) goto L_0x0502;
    L_0x0011:
        r0 = r2.mSurface;
        r0 = r0.isValid();
        if (r0 != 0) goto L_0x001b;
    L_0x0019:
        goto L_0x0502;
    L_0x001b:
        r0 = r2.mTranslator;
        r1.mTranslator = r0;
        r0 = r1.mTranslator;
        if (r0 == 0) goto L_0x0028;
    L_0x0023:
        r3 = r1.mSurface;
        r3.setCompatibilityTranslator(r0);
    L_0x0028:
        r0 = r1.mRequestedWidth;
        if (r0 > 0) goto L_0x0030;
    L_0x002c:
        r0 = r23.getWidth();
    L_0x0030:
        r3 = r0;
        r0 = r1.mRequestedHeight;
        if (r0 > 0) goto L_0x0039;
    L_0x0035:
        r0 = r23.getHeight();
    L_0x0039:
        r4 = r0;
        r0 = r1.mFormat;
        r5 = r1.mRequestedFormat;
        r7 = 0;
        if (r0 == r5) goto L_0x0043;
    L_0x0041:
        r0 = 1;
        goto L_0x0044;
    L_0x0043:
        r0 = r7;
    L_0x0044:
        r5 = r0;
        r0 = r1.mVisible;
        r8 = r1.mRequestedVisible;
        if (r0 == r8) goto L_0x004d;
    L_0x004b:
        r0 = 1;
        goto L_0x004e;
    L_0x004d:
        r0 = r7;
    L_0x004e:
        r8 = r0;
        r0 = r1.mSurfaceControl;
        if (r0 == 0) goto L_0x0057;
    L_0x0053:
        if (r5 != 0) goto L_0x0057;
    L_0x0055:
        if (r8 == 0) goto L_0x005d;
    L_0x0057:
        r0 = r1.mRequestedVisible;
        if (r0 == 0) goto L_0x005d;
    L_0x005b:
        r0 = 1;
        goto L_0x005e;
    L_0x005d:
        r0 = r7;
    L_0x005e:
        r9 = r0;
        r0 = r1.mSurfaceWidth;
        if (r0 != r3) goto L_0x006a;
    L_0x0063:
        r0 = r1.mSurfaceHeight;
        if (r0 == r4) goto L_0x0068;
    L_0x0067:
        goto L_0x006a;
    L_0x0068:
        r0 = r7;
        goto L_0x006b;
    L_0x006a:
        r0 = 1;
    L_0x006b:
        r10 = r0;
        r0 = r1.mWindowVisibility;
        r11 = r1.mLastWindowVisibility;
        if (r0 == r11) goto L_0x0074;
    L_0x0072:
        r0 = 1;
        goto L_0x0075;
    L_0x0074:
        r0 = r7;
    L_0x0075:
        r11 = r0;
        r12 = 0;
        r13 = "Exception configuring surface";
        r14 = "SurfaceView";
        if (r9 != 0) goto L_0x012a;
    L_0x007d:
        if (r5 != 0) goto L_0x012a;
    L_0x007f:
        if (r10 != 0) goto L_0x012a;
    L_0x0081:
        if (r8 != 0) goto L_0x012a;
    L_0x0083:
        if (r11 == 0) goto L_0x008b;
    L_0x0085:
        r18 = r11;
        r19 = r12;
        goto L_0x012e;
    L_0x008b:
        r0 = r1.mLocation;
        r1.getLocationInSurface(r0);
        r0 = r1.mWindowSpaceLeft;
        r15 = r1.mLocation;
        r6 = r15[r7];
        if (r0 != r6) goto L_0x00a2;
    L_0x0098:
        r0 = r1.mWindowSpaceTop;
        r6 = 1;
        r15 = r15[r6];
        if (r0 == r15) goto L_0x00a0;
    L_0x009f:
        goto L_0x00a2;
    L_0x00a0:
        r0 = r7;
        goto L_0x00a3;
    L_0x00a2:
        r0 = 1;
    L_0x00a3:
        r6 = r0;
        r0 = r23.getWidth();
        r15 = r1.mScreenRect;
        r15 = r15.width();
        if (r0 != r15) goto L_0x00bf;
    L_0x00b0:
        r0 = r23.getHeight();
        r15 = r1.mScreenRect;
        r15 = r15.height();
        if (r0 == r15) goto L_0x00bd;
    L_0x00bc:
        goto L_0x00bf;
    L_0x00bd:
        r0 = r7;
        goto L_0x00c0;
    L_0x00bf:
        r0 = 1;
    L_0x00c0:
        r15 = r0;
        if (r6 != 0) goto L_0x00cb;
    L_0x00c3:
        if (r15 == 0) goto L_0x00c6;
    L_0x00c5:
        goto L_0x00cb;
    L_0x00c6:
        r18 = r11;
        r19 = r12;
        goto L_0x0124;
    L_0x00cb:
        r0 = r1.mLocation;
        r17 = r6;
        r6 = r0[r7];
        r1.mWindowSpaceLeft = r6;
        r6 = 1;
        r7 = r0[r6];
        r1.mWindowSpaceTop = r7;
        r7 = r23.getWidth();
        r16 = 0;
        r0[r16] = r7;
        r0 = r1.mLocation;
        r7 = r23.getHeight();
        r0[r6] = r7;
        r0 = r1.mScreenRect;
        r7 = r1.mWindowSpaceLeft;
        r6 = r1.mWindowSpaceTop;
        r18 = r11;
        r11 = r1.mLocation;
        r16 = r11[r16];
        r19 = r12;
        r12 = r7 + r16;
        r16 = 1;
        r11 = r11[r16];
        r11 = r11 + r6;
        r0.set(r7, r6, r12, r11);
        r0 = r1.mTranslator;
        if (r0 == 0) goto L_0x0109;
    L_0x0104:
        r6 = r1.mScreenRect;
        r0.translateRectInAppWindowToScreen(r6);
    L_0x0109:
        r0 = r1.mSurfaceControl;
        if (r0 != 0) goto L_0x010e;
    L_0x010d:
        return;
    L_0x010e:
        r0 = r23.isHardwareAccelerated();
        if (r0 == 0) goto L_0x0118;
    L_0x0114:
        r0 = r1.mRtHandlingPositionUpdates;
        if (r0 != 0) goto L_0x0124;
    L_0x0118:
        r0 = r1.mScreenRect;	 Catch:{ Exception -> 0x0120 }
        r6 = -1;
        r1.setParentSpaceRectangle(r0, r6);	 Catch:{ Exception -> 0x0120 }
        goto L_0x0124;
    L_0x0120:
        r0 = move-exception;
        android.util.Log.e(r14, r13, r0);
    L_0x0124:
        r21 = r5;
        r12 = r19;
        goto L_0x0501;
    L_0x012a:
        r18 = r11;
        r19 = r12;
    L_0x012e:
        r0 = r1.mLocation;
        r1.getLocationInWindow(r0);
        r0 = r1.mRequestedVisible;	 Catch:{ Exception -> 0x04f4 }
        r1.mVisible = r0;	 Catch:{ Exception -> 0x04f4 }
        r6 = r0;
        r0 = r1.mLocation;	 Catch:{ Exception -> 0x04f4 }
        r7 = 0;
        r0 = r0[r7];	 Catch:{ Exception -> 0x04f4 }
        r1.mWindowSpaceLeft = r0;	 Catch:{ Exception -> 0x04f4 }
        r0 = r1.mLocation;	 Catch:{ Exception -> 0x04f4 }
        r7 = 1;
        r0 = r0[r7];	 Catch:{ Exception -> 0x04f4 }
        r1.mWindowSpaceTop = r0;	 Catch:{ Exception -> 0x04f4 }
        r1.mSurfaceWidth = r3;	 Catch:{ Exception -> 0x04f4 }
        r1.mSurfaceHeight = r4;	 Catch:{ Exception -> 0x04f4 }
        r0 = r1.mRequestedFormat;	 Catch:{ Exception -> 0x04f4 }
        r1.mFormat = r0;	 Catch:{ Exception -> 0x04f4 }
        r0 = r1.mWindowVisibility;	 Catch:{ Exception -> 0x04f4 }
        r1.mLastWindowVisibility = r0;	 Catch:{ Exception -> 0x04f4 }
        r0 = r1.mScreenRect;	 Catch:{ Exception -> 0x04f4 }
        r7 = r1.mWindowSpaceLeft;	 Catch:{ Exception -> 0x04f4 }
        r0.left = r7;	 Catch:{ Exception -> 0x04f4 }
        r0 = r1.mScreenRect;	 Catch:{ Exception -> 0x04f4 }
        r7 = r1.mWindowSpaceTop;	 Catch:{ Exception -> 0x04f4 }
        r0.top = r7;	 Catch:{ Exception -> 0x04f4 }
        r0 = r1.mScreenRect;	 Catch:{ Exception -> 0x04f4 }
        r7 = r1.mWindowSpaceLeft;	 Catch:{ Exception -> 0x04f4 }
        r11 = r23.getWidth();	 Catch:{ Exception -> 0x04f4 }
        r7 = r7 + r11;
        r0.right = r7;	 Catch:{ Exception -> 0x04f4 }
        r0 = r1.mScreenRect;	 Catch:{ Exception -> 0x04f4 }
        r7 = r1.mWindowSpaceTop;	 Catch:{ Exception -> 0x04f4 }
        r11 = r23.getHeight();	 Catch:{ Exception -> 0x04f4 }
        r7 = r7 + r11;
        r0.bottom = r7;	 Catch:{ Exception -> 0x04f4 }
        r0 = r1.mTranslator;	 Catch:{ Exception -> 0x04f4 }
        if (r0 == 0) goto L_0x0189;
    L_0x0178:
        r0 = r1.mTranslator;	 Catch:{ Exception -> 0x0180 }
        r7 = r1.mScreenRect;	 Catch:{ Exception -> 0x0180 }
        r0.translateRectInAppWindowToScreen(r7);	 Catch:{ Exception -> 0x0180 }
        goto L_0x0189;
    L_0x0180:
        r0 = move-exception;
        r21 = r5;
        r20 = r13;
        r12 = r19;
        goto L_0x04fb;
    L_0x0189:
        r0 = r23.getParentSurfaceInsets();	 Catch:{ Exception -> 0x04f4 }
        r7 = r0;
        r0 = r1.mScreenRect;	 Catch:{ Exception -> 0x04f4 }
        r11 = r7.left;	 Catch:{ Exception -> 0x04f4 }
        r12 = r7.top;	 Catch:{ Exception -> 0x04f4 }
        r0.offset(r11, r12);	 Catch:{ Exception -> 0x04f4 }
        if (r9 == 0) goto L_0x0231;
    L_0x0199:
        r0 = r1.mSubLayer;	 Catch:{ Exception -> 0x0180 }
        r2.createBoundsSurface(r0);	 Catch:{ Exception -> 0x0180 }
        r0 = new android.view.SurfaceSession;	 Catch:{ Exception -> 0x0180 }
        r0.<init>();	 Catch:{ Exception -> 0x0180 }
        r1.mSurfaceSession = r0;	 Catch:{ Exception -> 0x0180 }
        r0 = r1.mSurfaceControl;	 Catch:{ Exception -> 0x0180 }
        r1.mDeferredDestroySurfaceControl = r0;	 Catch:{ Exception -> 0x0180 }
        r23.updateOpaqueFlag();	 Catch:{ Exception -> 0x0180 }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0180 }
        r0.<init>();	 Catch:{ Exception -> 0x0180 }
        r11 = "SurfaceView - ";
        r0.append(r11);	 Catch:{ Exception -> 0x0180 }
        r11 = r2.getTitle();	 Catch:{ Exception -> 0x0180 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x0180 }
        r0.append(r11);	 Catch:{ Exception -> 0x0180 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0180 }
        r11 = new android.view.SurfaceControl$Builder;	 Catch:{ Exception -> 0x0180 }
        r12 = r1.mSurfaceSession;	 Catch:{ Exception -> 0x0180 }
        r11.<init>(r12);	 Catch:{ Exception -> 0x0180 }
        r11 = r11.setName(r0);	 Catch:{ Exception -> 0x0180 }
        r12 = r1.mSurfaceFlags;	 Catch:{ Exception -> 0x0180 }
        r12 = r12 & 1024;
        if (r12 == 0) goto L_0x01d8;
    L_0x01d6:
        r12 = 1;
        goto L_0x01d9;
    L_0x01d8:
        r12 = 0;
    L_0x01d9:
        r11 = r11.setOpaque(r12);	 Catch:{ Exception -> 0x0180 }
        r12 = r1.mSurfaceWidth;	 Catch:{ Exception -> 0x0180 }
        r15 = r1.mSurfaceHeight;	 Catch:{ Exception -> 0x0180 }
        r11 = r11.setBufferSize(r12, r15);	 Catch:{ Exception -> 0x0180 }
        r12 = r1.mFormat;	 Catch:{ Exception -> 0x0180 }
        r11 = r11.setFormat(r12);	 Catch:{ Exception -> 0x0180 }
        r12 = r2.getSurfaceControl();	 Catch:{ Exception -> 0x0180 }
        r11 = r11.setParent(r12);	 Catch:{ Exception -> 0x0180 }
        r12 = r1.mSurfaceFlags;	 Catch:{ Exception -> 0x0180 }
        r11 = r11.setFlags(r12);	 Catch:{ Exception -> 0x0180 }
        r11 = r11.build();	 Catch:{ Exception -> 0x0180 }
        r1.mSurfaceControl = r11;	 Catch:{ Exception -> 0x0180 }
        r11 = new android.view.SurfaceControl$Builder;	 Catch:{ Exception -> 0x0180 }
        r12 = r1.mSurfaceSession;	 Catch:{ Exception -> 0x0180 }
        r11.<init>(r12);	 Catch:{ Exception -> 0x0180 }
        r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0180 }
        r12.<init>();	 Catch:{ Exception -> 0x0180 }
        r15 = "Background for -";
        r12.append(r15);	 Catch:{ Exception -> 0x0180 }
        r12.append(r0);	 Catch:{ Exception -> 0x0180 }
        r12 = r12.toString();	 Catch:{ Exception -> 0x0180 }
        r11 = r11.setName(r12);	 Catch:{ Exception -> 0x0180 }
        r12 = 1;
        r11 = r11.setOpaque(r12);	 Catch:{ Exception -> 0x0180 }
        r11 = r11.setColorLayer();	 Catch:{ Exception -> 0x0180 }
        r12 = r1.mSurfaceControl;	 Catch:{ Exception -> 0x0180 }
        r11 = r11.setParent(r12);	 Catch:{ Exception -> 0x0180 }
        r11 = r11.build();	 Catch:{ Exception -> 0x0180 }
        r1.mBackgroundControl = r11;	 Catch:{ Exception -> 0x0180 }
        goto L_0x0236;
    L_0x0231:
        r0 = r1.mSurfaceControl;	 Catch:{ Exception -> 0x04f4 }
        if (r0 != 0) goto L_0x0236;
    L_0x0235:
        return;
        r11 = 0;
        r0 = r1.mSurfaceLock;	 Catch:{ Exception -> 0x04f4 }
        r0.lock();	 Catch:{ Exception -> 0x04f4 }
        if (r6 != 0) goto L_0x0241;
    L_0x023f:
        r0 = 1;
        goto L_0x0242;
    L_0x0241:
        r0 = 0;
    L_0x0242:
        r1.mDrawingStopped = r0;	 Catch:{ all -> 0x04e1 }
        android.view.SurfaceControl.openTransaction();	 Catch:{ all -> 0x04e1 }
        r0 = r1.mSurfaceControl;	 Catch:{ all -> 0x04d0 }
        r12 = r1.mSubLayer;	 Catch:{ all -> 0x04d0 }
        r0.setLayer(r12);	 Catch:{ all -> 0x04d0 }
        r0 = r1.mViewVisibility;	 Catch:{ all -> 0x04d0 }
        if (r0 == 0) goto L_0x0263;
    L_0x0252:
        r0 = r1.mSurfaceControl;	 Catch:{ all -> 0x0258 }
        r0.show();	 Catch:{ all -> 0x0258 }
        goto L_0x0268;
    L_0x0258:
        r0 = move-exception;
        r21 = r5;
        r22 = r6;
        r17 = r7;
        r20 = r13;
        goto L_0x04d9;
    L_0x0263:
        r0 = r1.mSurfaceControl;	 Catch:{ all -> 0x04d0 }
        r0.hide();	 Catch:{ all -> 0x04d0 }
    L_0x0268:
        r0 = r2.getSurfaceControl();	 Catch:{ all -> 0x04d0 }
        r1.updateBackgroundVisibilityInTransaction(r0);	 Catch:{ all -> 0x04d0 }
        if (r10 != 0) goto L_0x027b;
    L_0x0271:
        if (r9 != 0) goto L_0x027b;
    L_0x0273:
        r0 = r1.mRtHandlingPositionUpdates;	 Catch:{ all -> 0x0258 }
        if (r0 != 0) goto L_0x0278;
    L_0x0277:
        goto L_0x027b;
    L_0x0278:
        r17 = r7;
        goto L_0x02b1;
    L_0x027b:
        r0 = r1.mSurfaceControl;	 Catch:{ all -> 0x04d0 }
        r12 = r1.mScreenRect;	 Catch:{ all -> 0x04d0 }
        r12 = r12.left;	 Catch:{ all -> 0x04d0 }
        r12 = (float) r12;	 Catch:{ all -> 0x04d0 }
        r15 = r1.mScreenRect;	 Catch:{ all -> 0x04d0 }
        r15 = r15.top;	 Catch:{ all -> 0x04d0 }
        r15 = (float) r15;	 Catch:{ all -> 0x04d0 }
        r0.setPosition(r12, r15);	 Catch:{ all -> 0x04d0 }
        r0 = r1.mSurfaceControl;	 Catch:{ all -> 0x04d0 }
        r12 = r1.mScreenRect;	 Catch:{ all -> 0x04d0 }
        r12 = r12.width();	 Catch:{ all -> 0x04d0 }
        r12 = (float) r12;	 Catch:{ all -> 0x04d0 }
        r15 = r1.mSurfaceWidth;	 Catch:{ all -> 0x04d0 }
        r15 = (float) r15;	 Catch:{ all -> 0x04d0 }
        r12 = r12 / r15;
        r15 = r1.mScreenRect;	 Catch:{ all -> 0x04d0 }
        r15 = r15.height();	 Catch:{ all -> 0x04d0 }
        r15 = (float) r15;
        r17 = r7;
        r7 = r1.mSurfaceHeight;	 Catch:{ all -> 0x04c8 }
        r7 = (float) r7;	 Catch:{ all -> 0x04c8 }
        r15 = r15 / r7;
        r7 = 0;
        r0.setMatrix(r12, r7, r7, r15);	 Catch:{ all -> 0x04c8 }
        r0 = r1.mSurfaceControl;	 Catch:{ all -> 0x04c8 }
        r7 = r1.mSurfaceWidth;	 Catch:{ all -> 0x04c8 }
        r12 = r1.mSurfaceHeight;	 Catch:{ all -> 0x04c8 }
        r0.setWindowCrop(r7, r12);	 Catch:{ all -> 0x04c8 }
    L_0x02b1:
        r0 = r1.mSurfaceControl;	 Catch:{ all -> 0x04c8 }
        r7 = r1.mCornerRadius;	 Catch:{ all -> 0x04c8 }
        r0.setCornerRadius(r7);	 Catch:{ all -> 0x04c8 }
        if (r10 == 0) goto L_0x02cf;
    L_0x02ba:
        if (r9 != 0) goto L_0x02cf;
    L_0x02bc:
        r0 = r1.mSurfaceControl;	 Catch:{ all -> 0x02c6 }
        r7 = r1.mSurfaceWidth;	 Catch:{ all -> 0x02c6 }
        r12 = r1.mSurfaceHeight;	 Catch:{ all -> 0x02c6 }
        r0.setBufferSize(r7, r12);	 Catch:{ all -> 0x02c6 }
        goto L_0x02cf;
    L_0x02c6:
        r0 = move-exception;
        r21 = r5;
        r22 = r6;
        r20 = r13;
        goto L_0x04d9;
    L_0x02cf:
        android.view.SurfaceControl.closeTransaction();	 Catch:{ all -> 0x04be }
        if (r10 != 0) goto L_0x02db;
    L_0x02d5:
        if (r9 == 0) goto L_0x02d8;
    L_0x02d7:
        goto L_0x02db;
    L_0x02d8:
        r12 = r19;
        goto L_0x02dc;
    L_0x02db:
        r12 = 1;
    L_0x02dc:
        r0 = r1.mSurfaceFrame;	 Catch:{ all -> 0x04b6 }
        r7 = 0;
        r0.left = r7;	 Catch:{ all -> 0x04b6 }
        r0 = r1.mSurfaceFrame;	 Catch:{ all -> 0x04b6 }
        r0.top = r7;	 Catch:{ all -> 0x04b6 }
        r0 = r1.mTranslator;	 Catch:{ all -> 0x04b6 }
        if (r0 != 0) goto L_0x02ff;
    L_0x02e9:
        r0 = r1.mSurfaceFrame;	 Catch:{ all -> 0x02f6 }
        r7 = r1.mSurfaceWidth;	 Catch:{ all -> 0x02f6 }
        r0.right = r7;	 Catch:{ all -> 0x02f6 }
        r0 = r1.mSurfaceFrame;	 Catch:{ all -> 0x02f6 }
        r7 = r1.mSurfaceHeight;	 Catch:{ all -> 0x02f6 }
        r0.bottom = r7;	 Catch:{ all -> 0x02f6 }
        goto L_0x031b;
    L_0x02f6:
        r0 = move-exception;
        r21 = r5;
        r22 = r6;
        r20 = r13;
        goto L_0x04ec;
    L_0x02ff:
        r0 = r1.mTranslator;	 Catch:{ all -> 0x04b6 }
        r0 = r0.applicationInvertedScale;	 Catch:{ all -> 0x04b6 }
        r7 = r1.mSurfaceFrame;	 Catch:{ all -> 0x04b6 }
        r15 = r1.mSurfaceWidth;	 Catch:{ all -> 0x04b6 }
        r15 = (float) r15;	 Catch:{ all -> 0x04b6 }
        r15 = r15 * r0;
        r19 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r15 = r15 + r19;
        r15 = (int) r15;	 Catch:{ all -> 0x04b6 }
        r7.right = r15;	 Catch:{ all -> 0x04b6 }
        r7 = r1.mSurfaceFrame;	 Catch:{ all -> 0x04b6 }
        r15 = r1.mSurfaceHeight;	 Catch:{ all -> 0x04b6 }
        r15 = (float) r15;	 Catch:{ all -> 0x04b6 }
        r15 = r15 * r0;
        r15 = r15 + r19;
        r15 = (int) r15;	 Catch:{ all -> 0x04b6 }
        r7.bottom = r15;	 Catch:{ all -> 0x04b6 }
    L_0x031b:
        r0 = r1.mSurfaceFrame;	 Catch:{ all -> 0x04b6 }
        r0 = r0.right;	 Catch:{ all -> 0x04b6 }
        r7 = r1.mSurfaceFrame;	 Catch:{ all -> 0x04b6 }
        r7 = r7.bottom;	 Catch:{ all -> 0x04b6 }
        r15 = r1.mLastSurfaceWidth;	 Catch:{ all -> 0x04b6 }
        if (r15 != r0) goto L_0x032e;
    L_0x0327:
        r15 = r1.mLastSurfaceHeight;	 Catch:{ all -> 0x02f6 }
        if (r15 == r7) goto L_0x032c;
    L_0x032b:
        goto L_0x032e;
    L_0x032c:
        r15 = 0;
        goto L_0x032f;
    L_0x032e:
        r15 = 1;
    L_0x032f:
        r11 = r15;
        r1.mLastSurfaceWidth = r0;	 Catch:{ all -> 0x04b6 }
        r1.mLastSurfaceHeight = r7;	 Catch:{ all -> 0x04b6 }
        r0 = r1.mSurfaceLock;	 Catch:{ Exception -> 0x04b0 }
        r0.unlock();	 Catch:{ Exception -> 0x04b0 }
        if (r6 == 0) goto L_0x034b;
    L_0x033c:
        r0 = r1.mDrawFinished;	 Catch:{ all -> 0x0342 }
        if (r0 != 0) goto L_0x034b;
    L_0x0340:
        r0 = 1;
        goto L_0x034c;
    L_0x0342:
        r0 = move-exception;
        r21 = r5;
        r22 = r6;
        r20 = r13;
        goto L_0x049b;
    L_0x034b:
        r0 = 0;
    L_0x034c:
        r12 = r12 | r0;
        r0 = 0;
        r7 = r9;
        r15 = r1.mSurfaceCreated;	 Catch:{ all -> 0x0494 }
        if (r15 == 0) goto L_0x03a2;
    L_0x0353:
        if (r7 != 0) goto L_0x035d;
    L_0x0355:
        if (r6 != 0) goto L_0x035a;
    L_0x0357:
        if (r8 == 0) goto L_0x035a;
    L_0x0359:
        goto L_0x035d;
    L_0x035a:
        r20 = r13;
        goto L_0x03a4;
    L_0x035d:
        r15 = 0;
        r1.mSurfaceCreated = r15;	 Catch:{ all -> 0x039e }
        r15 = r1.mSurface;	 Catch:{ all -> 0x039e }
        r15 = r15.isValid();	 Catch:{ all -> 0x039e }
        if (r15 == 0) goto L_0x039b;
    L_0x0368:
        r15 = r23.getSurfaceCallbacks();	 Catch:{ all -> 0x039e }
        r0 = r15;
        r15 = r0.length;	 Catch:{ all -> 0x039e }
        r20 = r13;
        r13 = 0;
    L_0x0371:
        if (r13 >= r15) goto L_0x0389;
    L_0x0373:
        r19 = r0[r13];	 Catch:{ all -> 0x03ae }
        r21 = r19;
        r19 = r0;
        r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x03ae }
        r22 = r15;
        r15 = r21;
        r15.surfaceDestroyed(r0);	 Catch:{ all -> 0x03ae }
        r13 = r13 + 1;
        r0 = r19;
        r15 = r22;
        goto L_0x0371;
    L_0x0389:
        r19 = r0;
        r0 = r1.mSurface;	 Catch:{ all -> 0x03ae }
        r0 = r0.isValid();	 Catch:{ all -> 0x03ae }
        if (r0 == 0) goto L_0x0398;
    L_0x0393:
        r0 = r1.mSurface;	 Catch:{ all -> 0x03ae }
        r0.forceScopedDisconnect();	 Catch:{ all -> 0x03ae }
    L_0x0398:
        r0 = r19;
        goto L_0x03a4;
    L_0x039b:
        r20 = r13;
        goto L_0x03a4;
    L_0x039e:
        r0 = move-exception;
        r20 = r13;
        goto L_0x03af;
    L_0x03a2:
        r20 = r13;
    L_0x03a4:
        if (r9 == 0) goto L_0x03b5;
    L_0x03a6:
        r13 = r1.mSurface;	 Catch:{ all -> 0x03ae }
        r15 = r1.mSurfaceControl;	 Catch:{ all -> 0x03ae }
        r13.copyFrom(r15);	 Catch:{ all -> 0x03ae }
        goto L_0x03b5;
    L_0x03ae:
        r0 = move-exception;
    L_0x03af:
        r21 = r5;
        r22 = r6;
        goto L_0x049b;
    L_0x03b5:
        if (r10 == 0) goto L_0x03cc;
    L_0x03b7:
        r13 = r23.getContext();	 Catch:{ all -> 0x03ae }
        r13 = r13.getApplicationInfo();	 Catch:{ all -> 0x03ae }
        r13 = r13.targetSdkVersion;	 Catch:{ all -> 0x03ae }
        r15 = 26;
        if (r13 >= r15) goto L_0x03cc;
    L_0x03c5:
        r13 = r1.mSurface;	 Catch:{ all -> 0x03ae }
        r15 = r1.mSurfaceControl;	 Catch:{ all -> 0x03ae }
        r13.createFrom(r15);	 Catch:{ all -> 0x03ae }
    L_0x03cc:
        if (r6 == 0) goto L_0x047b;
    L_0x03ce:
        r13 = r1.mSurface;	 Catch:{ all -> 0x0475 }
        r13 = r13.isValid();	 Catch:{ all -> 0x0475 }
        if (r13 == 0) goto L_0x047b;
    L_0x03d6:
        r13 = r1.mSurfaceCreated;	 Catch:{ all -> 0x0475 }
        if (r13 != 0) goto L_0x0414;
    L_0x03da:
        if (r7 != 0) goto L_0x03e2;
    L_0x03dc:
        if (r8 == 0) goto L_0x03df;
    L_0x03de:
        goto L_0x03e2;
    L_0x03df:
        r22 = r6;
        goto L_0x0416;
    L_0x03e2:
        r13 = 1;
        r1.mSurfaceCreated = r13;	 Catch:{ all -> 0x040d }
        r1.mIsCreating = r13;	 Catch:{ all -> 0x040d }
        if (r0 != 0) goto L_0x03ee;
    L_0x03e9:
        r13 = r23.getSurfaceCallbacks();	 Catch:{ all -> 0x03ae }
        r0 = r13;
    L_0x03ee:
        r13 = r0.length;	 Catch:{ all -> 0x040d }
        r15 = 0;
    L_0x03f0:
        if (r15 >= r13) goto L_0x0408;
    L_0x03f2:
        r19 = r0[r15];	 Catch:{ all -> 0x040d }
        r21 = r19;
        r19 = r0;
        r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x040d }
        r22 = r6;
        r6 = r21;
        r6.surfaceCreated(r0);	 Catch:{ all -> 0x042c }
        r15 = r15 + 1;
        r0 = r19;
        r6 = r22;
        goto L_0x03f0;
    L_0x0408:
        r19 = r0;
        r22 = r6;
        goto L_0x0416;
    L_0x040d:
        r0 = move-exception;
        r22 = r6;
        r21 = r5;
        goto L_0x049b;
    L_0x0414:
        r22 = r6;
    L_0x0416:
        if (r9 != 0) goto L_0x0424;
    L_0x0418:
        if (r5 != 0) goto L_0x0424;
    L_0x041a:
        if (r10 != 0) goto L_0x0424;
    L_0x041c:
        if (r8 != 0) goto L_0x0424;
    L_0x041e:
        if (r11 == 0) goto L_0x0421;
    L_0x0420:
        goto L_0x0424;
    L_0x0421:
        r21 = r5;
        goto L_0x044d;
    L_0x0424:
        if (r0 != 0) goto L_0x0431;
    L_0x0426:
        r6 = r23.getSurfaceCallbacks();	 Catch:{ all -> 0x042c }
        r0 = r6;
        goto L_0x0431;
    L_0x042c:
        r0 = move-exception;
        r21 = r5;
        goto L_0x049b;
    L_0x0431:
        r6 = r0.length;	 Catch:{ all -> 0x0471 }
        r13 = 0;
    L_0x0433:
        if (r13 >= r6) goto L_0x0449;
    L_0x0435:
        r15 = r0[r13];	 Catch:{ all -> 0x0471 }
        r19 = r0;
        r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x0471 }
        r21 = r5;
        r5 = r1.mFormat;	 Catch:{ all -> 0x046f }
        r15.surfaceChanged(r0, r5, r3, r4);	 Catch:{ all -> 0x046f }
        r13 = r13 + 1;
        r0 = r19;
        r5 = r21;
        goto L_0x0433;
    L_0x0449:
        r19 = r0;
        r21 = r5;
    L_0x044d:
        if (r12 == 0) goto L_0x047f;
    L_0x044f:
        if (r0 != 0) goto L_0x0456;
    L_0x0451:
        r5 = r23.getSurfaceCallbacks();	 Catch:{ all -> 0x046f }
        r0 = r5;
    L_0x0456:
        r5 = r1.mPendingReportDraws;	 Catch:{ all -> 0x046f }
        r6 = 1;
        r5 = r5 + r6;
        r1.mPendingReportDraws = r5;	 Catch:{ all -> 0x046f }
        r2.drawPending();	 Catch:{ all -> 0x046f }
        r5 = new com.android.internal.view.SurfaceCallbackHelper;	 Catch:{ all -> 0x046f }
        r6 = new android.view.-$$Lambda$SurfaceView$SyyzxOgxKwZMRgiiTGcRYbOU5JY;	 Catch:{ all -> 0x046f }
        r6.<init>(r1);	 Catch:{ all -> 0x046f }
        r5.<init>(r6);	 Catch:{ all -> 0x046f }
        r6 = r1.mSurfaceHolder;	 Catch:{ all -> 0x046f }
        r5.dispatchSurfaceRedrawNeededAsync(r6, r0);	 Catch:{ all -> 0x046f }
        goto L_0x047f;
    L_0x046f:
        r0 = move-exception;
        goto L_0x049b;
    L_0x0471:
        r0 = move-exception;
        r21 = r5;
        goto L_0x049b;
    L_0x0475:
        r0 = move-exception;
        r21 = r5;
        r22 = r6;
        goto L_0x049b;
    L_0x047b:
        r21 = r5;
        r22 = r6;
    L_0x047f:
        r5 = 0;
        r1.mIsCreating = r5;	 Catch:{ Exception -> 0x04f2 }
        r0 = r1.mSurfaceControl;	 Catch:{ Exception -> 0x04f2 }
        if (r0 == 0) goto L_0x0500;
    L_0x0486:
        r0 = r1.mSurfaceCreated;	 Catch:{ Exception -> 0x04f2 }
        if (r0 != 0) goto L_0x0500;
    L_0x048a:
        r0 = r1.mSurface;	 Catch:{ Exception -> 0x04f2 }
        r0.release();	 Catch:{ Exception -> 0x04f2 }
        r23.releaseSurfaces();	 Catch:{ Exception -> 0x04f2 }
        goto L_0x0500;
    L_0x0494:
        r0 = move-exception;
        r21 = r5;
        r22 = r6;
        r20 = r13;
    L_0x049b:
        r5 = 0;
        r1.mIsCreating = r5;	 Catch:{ Exception -> 0x04f2 }
        r5 = r1.mSurfaceControl;	 Catch:{ Exception -> 0x04f2 }
        if (r5 == 0) goto L_0x04ae;
    L_0x04a2:
        r5 = r1.mSurfaceCreated;	 Catch:{ Exception -> 0x04f2 }
        if (r5 != 0) goto L_0x04ae;
    L_0x04a6:
        r5 = r1.mSurface;	 Catch:{ Exception -> 0x04f2 }
        r5.release();	 Catch:{ Exception -> 0x04f2 }
        r23.releaseSurfaces();	 Catch:{ Exception -> 0x04f2 }
        throw r0;	 Catch:{ Exception -> 0x04f2 }
    L_0x04b0:
        r0 = move-exception;
        r21 = r5;
        r20 = r13;
        goto L_0x04fb;
    L_0x04b6:
        r0 = move-exception;
        r21 = r5;
        r22 = r6;
        r20 = r13;
        goto L_0x04ec;
    L_0x04be:
        r0 = move-exception;
        r21 = r5;
        r22 = r6;
        r20 = r13;
        r12 = r19;
        goto L_0x04ec;
    L_0x04c8:
        r0 = move-exception;
        r21 = r5;
        r22 = r6;
        r20 = r13;
        goto L_0x04d9;
    L_0x04d0:
        r0 = move-exception;
        r21 = r5;
        r22 = r6;
        r17 = r7;
        r20 = r13;
    L_0x04d9:
        android.view.SurfaceControl.closeTransaction();	 Catch:{ all -> 0x04dd }
        throw r0;	 Catch:{ all -> 0x04dd }
    L_0x04dd:
        r0 = move-exception;
        r12 = r19;
        goto L_0x04ec;
    L_0x04e1:
        r0 = move-exception;
        r21 = r5;
        r22 = r6;
        r17 = r7;
        r20 = r13;
        r12 = r19;
    L_0x04ec:
        r5 = r1.mSurfaceLock;	 Catch:{ Exception -> 0x04f2 }
        r5.unlock();	 Catch:{ Exception -> 0x04f2 }
        throw r0;	 Catch:{ Exception -> 0x04f2 }
    L_0x04f2:
        r0 = move-exception;
        goto L_0x04fb;
    L_0x04f4:
        r0 = move-exception;
        r21 = r5;
        r20 = r13;
        r12 = r19;
    L_0x04fb:
        r5 = r20;
        android.util.Log.e(r14, r5, r0);
    L_0x0501:
        return;
    L_0x0502:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.SurfaceView.updateSurface():void");
    }

    private void onDrawFinished() {
        SurfaceControl surfaceControl = this.mDeferredDestroySurfaceControl;
        if (surfaceControl != null) {
            surfaceControl.remove();
            this.mDeferredDestroySurfaceControl = null;
        }
        runOnUiThread(new -$$Lambda$SurfaceView$Cs7TGTdA1lXf9qW8VOJAfEsMjdk(this));
    }

    public /* synthetic */ void lambda$onDrawFinished$0$SurfaceView() {
        performDrawFinished();
    }

    /* Access modifiers changed, original: protected */
    public void applyChildSurfaceTransaction_renderWorker(Transaction t, Surface viewRootSurface, long nextViewRootFrameNumber) {
    }

    private void applySurfaceTransforms(SurfaceControl surface, Rect position, long frameNumber) {
        if (frameNumber > 0) {
            this.mRtTransaction.deferTransactionUntilSurface(surface, getViewRootImpl().mSurface, frameNumber);
        }
        this.mRtTransaction.setPosition(surface, (float) position.left, (float) position.top);
        this.mRtTransaction.setMatrix(surface, ((float) position.width()) / ((float) this.mSurfaceWidth), 0.0f, 0.0f, ((float) position.height()) / ((float) this.mSurfaceHeight));
        if (this.mViewVisibility) {
            this.mRtTransaction.show(surface);
        }
    }

    private void setParentSpaceRectangle(Rect position, long frameNumber) {
        ViewRootImpl viewRoot = getViewRootImpl();
        applySurfaceTransforms(this.mSurfaceControl, position, frameNumber);
        applyChildSurfaceTransaction_renderWorker(this.mRtTransaction, viewRoot.mSurface, frameNumber);
        this.mRtTransaction.apply();
    }

    private Callback[] getSurfaceCallbacks() {
        Callback[] callbacks;
        synchronized (this.mCallbacks) {
            callbacks = new Callback[this.mCallbacks.size()];
            this.mCallbacks.toArray(callbacks);
        }
        return callbacks;
    }

    private void runOnUiThread(Runnable runnable) {
        Handler handler = getHandler();
        if (handler == null || handler.getLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            handler.post(runnable);
        }
    }

    @UnsupportedAppUsage
    public boolean isFixedSize() {
        return (this.mRequestedWidth == -1 && this.mRequestedHeight == -1) ? false : true;
    }

    private boolean isAboveParent() {
        return this.mSubLayer >= 0;
    }

    public void setResizeBackgroundColor(int bgColor) {
        if (this.mBackgroundControl != null) {
            float[] colorComponents = new float[]{((float) Color.red(bgColor)) / 255.0f, ((float) Color.green(bgColor)) / 255.0f, ((float) Color.blue(bgColor)) / 255.0f};
            SurfaceControl.openTransaction();
            try {
                this.mBackgroundControl.setColor(colorComponents);
            } finally {
                SurfaceControl.closeTransaction();
            }
        }
    }

    public SurfaceControl getSurfaceControl() {
        return this.mSurfaceControl;
    }
}
