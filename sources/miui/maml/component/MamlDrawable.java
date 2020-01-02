package miui.maml.component;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import miui.maml.RenderUpdater;
import miui.maml.ScreenElementRoot;

public class MamlDrawable extends Drawable {
    private static final boolean DBG = true;
    private static final String LOG_TAG = "MamlDrawable";
    private static final int RENDER_TIMEOUT = 100;
    private Handler mHandler;
    private int mHeight;
    private int mIntrinsicHeight;
    private int mIntrinsicWidth;
    private Runnable mInvalidateSelf;
    private boolean mPaused;
    private Runnable mRenderTimeout;
    private ScreenElementRoot mRoot;
    private RenderUpdater mUpdater;
    private int mWidth;

    public MamlDrawable(ScreenElementRoot root) {
        this(root, false);
    }

    public MamlDrawable(ScreenElementRoot root, boolean autoCleanup) {
        this.mPaused = true;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mRenderTimeout = new Runnable() {
            public void run() {
                MamlDrawable.this.doPause();
            }
        };
        this.mInvalidateSelf = new Runnable() {
            public void run() {
                MamlDrawable.this.invalidateSelf();
            }
        };
        this.mRoot = root;
        setIntrinsicSize((int) this.mRoot.getWidth(), (int) this.mRoot.getHeight());
        this.mUpdater = new RenderUpdater(this.mRoot, new Handler(), autoCleanup) {
            public void doRenderImp() {
                MamlDrawable.this.mHandler.removeCallbacks(MamlDrawable.this.mRenderTimeout);
                MamlDrawable.this.mHandler.postDelayed(MamlDrawable.this.mRenderTimeout, 100);
                MamlDrawable.this.mHandler.post(MamlDrawable.this.mInvalidateSelf);
            }
        };
        this.mUpdater.init();
        this.mUpdater.runUpdater();
    }

    public MamlDrawable setAutoCleanup(boolean autoCleanup) {
        this.mUpdater.setAutoCleanup(autoCleanup);
        return this;
    }

    public void setIntrinsicSize(int width, int height) {
        this.mIntrinsicWidth = width;
        this.mIntrinsicHeight = height;
    }

    public void cleanUp() {
        logd("cleanUp: ");
        this.mUpdater.cleanUp();
    }

    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        this.mWidth = right - left;
        this.mHeight = bottom - top;
    }

    public int getIntrinsicWidth() {
        return this.mIntrinsicWidth;
    }

    public int getIntrinsicHeight() {
        return this.mIntrinsicHeight;
    }

    public void draw(Canvas canvas) {
        String str = LOG_TAG;
        this.mHandler.removeCallbacks(this.mRenderTimeout);
        doResume();
        try {
            int sa = canvas.save();
            canvas.translate((float) getBounds().left, (float) getBounds().top);
            canvas.scale(((float) this.mWidth) / ((float) this.mIntrinsicWidth), ((float) this.mHeight) / ((float) this.mIntrinsicHeight), 0.0f, 0.0f);
            this.mRoot.render(canvas);
            canvas.restoreToCount(sa);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(str, e.toString());
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
            Log.e(str, e2.toString());
        }
    }

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int alpha) {
    }

    public void setColorFilter(ColorFilter cf) {
    }

    private void doPause() {
        if (!this.mPaused) {
            logd("doPause: ");
            this.mPaused = true;
            this.mUpdater.onPause();
        }
    }

    private void doResume() {
        if (this.mPaused) {
            logd("doResume: ");
            this.mPaused = false;
            this.mUpdater.onResume();
        }
    }

    public ScreenElementRoot getRoot() {
        return this.mRoot;
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
