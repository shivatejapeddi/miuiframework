package miui.maml;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import miui.maml.MamlDrawable.MamlDrawableState;
import miui.maml.RendererController.IRenderable;
import miui.maml.ScreenElementRootFactory.Parameter;
import miui.maml.util.Utils;

public class FancyDrawable extends MamlDrawable implements IRenderable {
    private static final boolean DBG = true;
    private static final String LOG_TAG = "FancyDrawable";
    private static final String QUIET_IMAGE_NAME = "quietImage.png";
    private static final int RENDER_TIMEOUT = 100;
    private static final String START_IMAGE_NAME = "startImage.png";
    private static final String USE_QUIET_IMAGE_TAG = "useQuietImage";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Object mPauseLock = new Object();
    private boolean mPaused;
    private Drawable mQuietDrawable;
    private Runnable mRenderTimeout = new Runnable() {
        public void run() {
            FancyDrawable.this.mTimeOut = true;
            FancyDrawable.this.doPause();
        }
    };
    private RendererCore mRendererCore;
    private Drawable mStartDrawable;
    private boolean mTimeOut;

    static final class FancyDrawableState extends MamlDrawableState {
        RendererCore mRendererCore;

        public FancyDrawableState(RendererCore rendererCore) {
            this.mRendererCore = rendererCore;
        }

        /* Access modifiers changed, original: protected */
        public MamlDrawable createDrawable() {
            return new FancyDrawable(this.mRendererCore);
        }
    }

    public FancyDrawable(RendererCore rc) {
        init(rc);
    }

    public FancyDrawable(ScreenElementRoot root, RenderThread t) {
        init(root, t);
    }

    public void setBadgeInfo(Drawable badgeDrawable, Rect badgeLocation) {
        if (badgeLocation == null || (badgeLocation.left >= 0 && badgeLocation.top >= 0 && badgeLocation.width() <= this.mIntrinsicWidth && badgeLocation.height() <= this.mIntrinsicHeight)) {
            this.mBadgeDrawable = badgeDrawable;
            this.mBadgeLocation = badgeLocation;
            this.mState.mStateBadgeDrawable = badgeDrawable;
            this.mState.mStateBadgeLocation = badgeLocation;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Badge location ");
        stringBuilder.append(badgeLocation);
        stringBuilder.append(" not in badged drawable bounds ");
        stringBuilder.append(new Rect(0, 0, this.mIntrinsicWidth, this.mIntrinsicHeight));
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public Drawable getQuietDrawable() {
        return this.mQuietDrawable;
    }

    public Drawable getStartDrawable() {
        return this.mStartDrawable;
    }

    private void init(RendererCore rc) {
        if (rc != null) {
            this.mState = new FancyDrawableState(rc);
            this.mRendererCore = rc;
            this.mRendererCore.addRenderable(this);
            setIntrinsicSize((int) this.mRendererCore.getRoot().getWidth(), (int) this.mRendererCore.getRoot().getHeight());
            ScreenContext context = this.mRendererCore.getRoot().getContext();
            this.mQuietDrawable = context.mResourceManager.getDrawable(context.mContext.getResources(), QUIET_IMAGE_NAME);
            Drawable drawable = this.mQuietDrawable;
            if (drawable != null) {
                this.mQuietDrawable = drawable.mutate();
                drawable = this.mQuietDrawable;
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.mQuietDrawable.getIntrinsicHeight());
            }
            this.mStartDrawable = context.mResourceManager.getDrawable(context.mContext.getResources(), START_IMAGE_NAME);
            drawable = this.mStartDrawable;
            if (drawable != null) {
                this.mStartDrawable = drawable.mutate();
                drawable = this.mStartDrawable;
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.mStartDrawable.getIntrinsicHeight());
                return;
            }
            return;
        }
        throw new NullPointerException();
    }

    private void init(ScreenElementRoot root, RenderThread t) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("init  root:");
        stringBuilder.append(root.toString());
        logd(stringBuilder.toString());
        init(new RendererCore(root, t));
    }

    public static FancyDrawable fromZipFile(Context context, String path) {
        return fromZipFile(context, path, RenderThread.globalThread(true));
    }

    public static FancyDrawable fromZipFile(Context context, String path, RenderThread t) {
        ScreenElementRoot root = ScreenElementRootFactory.create(new Parameter(context, path));
        FancyDrawable fancyDrawable = null;
        if (root == null) {
            return null;
        }
        root.setDefaultFramerate(0.0f);
        RendererCore rc = null;
        if (root.load()) {
            rc = new RendererCore(root, t);
        }
        if (rc != null) {
            fancyDrawable = new FancyDrawable(rc);
        }
        return fancyDrawable;
    }

    public void cleanUp() {
        logd("cleanUp: ");
        this.mRendererCore.removeRenderable(this);
    }

    public int getIntrinsicWidth() {
        return this.mIntrinsicWidth;
    }

    public int getIntrinsicHeight() {
        return this.mIntrinsicHeight;
    }

    /* Access modifiers changed, original: protected */
    public void drawIcon(Canvas canvas) {
        this.mHandler.removeCallbacks(this.mRenderTimeout);
        if (this.mTimeOut) {
            doResume();
            this.mTimeOut = false;
        }
        try {
            int sa = canvas.save();
            canvas.translate((float) getBounds().left, (float) getBounds().top);
            canvas.scale(((float) this.mWidth) / ((float) this.mIntrinsicWidth), ((float) this.mHeight) / ((float) this.mIntrinsicHeight), 0.0f, 0.0f);
            if (Utils.getVariableNumber(USE_QUIET_IMAGE_TAG, this.mRendererCore.getRoot().getVariables()) <= 0.0d || this.mQuietDrawable == null) {
                this.mRendererCore.render(canvas);
            } else {
                this.mQuietDrawable.draw(canvas);
            }
            canvas.restoreToCount(sa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAlpha(int alpha) {
        Drawable drawable = this.mQuietDrawable;
        if (drawable != null) {
            drawable.setAlpha(alpha);
        }
        drawable = this.mStartDrawable;
        if (drawable != null) {
            drawable.setAlpha(alpha);
        }
    }

    public void setColorFilter(ColorFilter cf) {
        Drawable drawable = this.mQuietDrawable;
        if (drawable != null) {
            drawable.setColorFilter(cf);
        }
        drawable = this.mStartDrawable;
        if (drawable != null) {
            drawable.setColorFilter(cf);
        }
        if (this.mBadgeDrawable != null) {
            this.mBadgeDrawable.setColorFilter(cf);
        }
    }

    public void onPause() {
        getRoot().onCommand("pause");
        doPause();
        this.mHandler.removeCallbacks(this.mRenderTimeout);
    }

    public void onResume() {
        getRoot().onCommand("resume");
        doResume();
    }

    private void doPause() {
        synchronized (this.mPauseLock) {
            if (this.mPaused) {
                return;
            }
            logd("doPause: ");
            this.mPaused = true;
            this.mRendererCore.pauseRenderable(this);
        }
    }

    private void doResume() {
        synchronized (this.mPauseLock) {
            if (this.mPaused) {
                logd("doResume: ");
                this.mPaused = false;
                this.mRendererCore.resumeRenderable(this);
                return;
            }
        }
    }

    public void doRender() {
        this.mHandler.removeCallbacks(this.mRenderTimeout);
        this.mHandler.postDelayed(this.mRenderTimeout, 100);
        this.mHandler.post(this.mInvalidateSelf);
    }

    public ScreenElementRoot getRoot() {
        return this.mRendererCore.getRoot();
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        cleanUp();
        super.finalize();
    }

    private void logd(CharSequence info) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(info);
        stringBuilder.append("  [");
        stringBuilder.append(toString());
        stringBuilder.append("]");
        Log.d(LOG_TAG, stringBuilder.toString());
    }
}
