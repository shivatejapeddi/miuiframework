package miui.maml.component;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import miui.maml.RenderVsyncUpdater;
import miui.maml.ScreenElementRoot;
import miui.maml.ScreenElementRoot.OnHoverChangeListener;
import miui.maml.data.VariableNames;
import miui.maml.data.Variables;
import miui.maml.util.MamlAccessHelper;

public class MamlView extends FrameLayout {
    private static final String BLUR_VAR_NAME = "__blur_ratio";
    private static final String TAG = "MamlView";
    private boolean mHasDelay;
    private int mLastBlurRatio;
    private LayoutParams mLp;
    private MamlAccessHelper mMamlAccessHelper;
    protected boolean mNeedDisallowInterceptTouchEvent;
    private int mPivotX;
    private int mPivotY;
    protected ScreenElementRoot mRoot;
    private float mScale;
    private RenderVsyncUpdater mUpdater;
    private InnerView mView;
    private WindowManager mWindowManager;

    private class InnerView extends View {
        public InnerView(Context context) {
            super(context);
        }

        /* Access modifiers changed, original: protected */
        public void onDraw(Canvas canvas) {
            if (!MamlView.this.mHasDelay || MamlView.this.mUpdater.isStarted()) {
                if (MamlView.this.mScale != 0.0f) {
                    int sa = canvas.save();
                    canvas.scale(MamlView.this.mScale, MamlView.this.mScale, (float) MamlView.this.mPivotX, (float) MamlView.this.mPivotY);
                    MamlView.this.mRoot.render(canvas);
                    canvas.restoreToCount(sa);
                } else {
                    MamlView.this.mRoot.render(canvas);
                }
                MamlView.this.mUpdater.doneRender();
            }
        }
    }

    public MamlView(Context context, ScreenElementRoot root) {
        this(context, root, new Handler(), 0);
    }

    public MamlView(Context context, ScreenElementRoot root, long startDelay) {
        this(context, root, new Handler(), startDelay);
    }

    public MamlView(Context context, ScreenElementRoot root, Handler h, long startDelay) {
        super(context);
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        if (root != null) {
            this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            this.mView = new InnerView(context);
            addView((View) this.mView, new ViewGroup.LayoutParams(-1, -1));
            this.mRoot = root;
            this.mRoot.setViewManager(this);
            this.mRoot.setOnHoverChangeListener(new OnHoverChangeListener() {
                public void onHoverChange(String contentDescription) {
                    MamlView.this.setContentDescription(contentDescription);
                    MamlView.this.sendAccessibilityEvent(32768);
                }
            });
            this.mUpdater = new RenderVsyncUpdater(this.mRoot, h) {
                /* Access modifiers changed, original: protected */
                public void doRenderImp() {
                    MamlView.this.mView.postInvalidate();
                    MamlView.this.blurBackground();
                }
            };
            if (startDelay > 0) {
                this.mHasDelay = true;
                this.mUpdater.setStartDelay(SystemClock.elapsedRealtime(), startDelay);
            }
            init();
            if (VERSION.SDK_INT >= 23) {
                this.mMamlAccessHelper = new MamlAccessHelper(this.mRoot, this);
                setAccessibilityDelegate(this.mMamlAccessHelper);
                return;
            }
            return;
        }
        throw new NullPointerException();
    }

    /* Access modifiers changed, original: protected */
    public boolean dispatchHoverEvent(MotionEvent event) {
        MamlAccessHelper mamlAccessHelper = this.mMamlAccessHelper;
        if (mamlAccessHelper == null || !mamlAccessHelper.dispatchHoverEvent(event)) {
            return super.dispatchHoverEvent(event);
        }
        return true;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        info.setText(this.mRoot.getRawAttr("accessibilityText"));
        super.onInitializeAccessibilityNodeInfo(info);
    }

    public MamlView setAutoCleanup(boolean autoCleanup) {
        this.mUpdater.setAutoCleanup(autoCleanup);
        return this;
    }

    public void init() {
        this.mRoot.setConfiguration(getResources().getConfiguration());
        this.mUpdater.init();
    }

    public void cleanUp() {
        cleanUp(false);
    }

    public void cleanUp(boolean keepResource) {
        setOnTouchListener(null);
        this.mRoot.setKeepResource(keepResource);
        this.mUpdater.cleanUp();
    }

    public final ScreenElementRoot getRoot() {
        return this.mRoot;
    }

    /* Access modifiers changed, original: protected */
    public int getSuggestedMinimumWidth() {
        return (int) this.mRoot.getWidth();
    }

    /* Access modifiers changed, original: protected */
    public int getSuggestedMinimumHeight() {
        return (int) this.mRoot.getHeight();
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == 0) {
            onResume();
        } else if (visibility == 4 || visibility == 8) {
            onPause();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean b = this.mRoot.needDisallowInterceptTouchEvent();
        if (this.mNeedDisallowInterceptTouchEvent != b) {
            getParent().requestDisallowInterceptTouchEvent(b);
            this.mNeedDisallowInterceptTouchEvent = b;
        }
        this.mRoot.postMessage(MotionEvent.obtain(event));
        return true;
    }

    public boolean onHoverEvent(MotionEvent event) {
        this.mRoot.postMessage(MotionEvent.obtain(event));
        return super.onHoverEvent(event);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        onResume();
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onPause();
    }

    public void onPause() {
        this.mUpdater.onPause();
    }

    public void onResume() {
        this.mUpdater.onResume();
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Variables vars = this.mRoot.getContext().mVariables;
        vars.put(VariableNames.VARIABLE_VIEW_WIDTH, (double) (((float) (right - left)) / this.mRoot.getScale()));
        vars.put(VariableNames.VARIABLE_VIEW_HEIGHT, (double) (((float) (bottom - top)) / this.mRoot.getScale()));
        int x = left;
        int y = top;
        ViewParent parent = this.mParent;
        while (parent instanceof View) {
            View parentView = (View) parent;
            x += parentView.getLeft() - parentView.getScrollX();
            y += parentView.getTop() - parentView.getScrollY();
            parent = parentView.getParent();
        }
        vars.put(VariableNames.VARIABLE_VIEW_X, (double) (((float) x) / this.mRoot.getScale()));
        vars.put(VariableNames.VARIABLE_VIEW_Y, (double) (((float) y) / this.mRoot.getScale()));
        this.mUpdater.triggerUpdate();
    }

    public void setScale(float scale, int x, int y) {
        this.mScale = scale;
        this.mPivotX = x;
        this.mPivotY = y;
    }

    /* Access modifiers changed, original: protected */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mRoot.onConfigurationChanged(newConfig);
    }

    public void setWindowLayoutParams(LayoutParams layoutParams) {
        this.mLp = layoutParams;
    }

    private void blurBackground() {
        String str = BLUR_VAR_NAME;
        try {
            if (this.mRoot.isMamlBlurWindow() && this.mLp != null && this.mRoot.getVariables().existsDouble(str)) {
                int blurRatio = (int) this.mRoot.getVariables().getDouble(str);
                if (blurRatio < 0) {
                    blurRatio = 0;
                } else if (blurRatio > 100) {
                    blurRatio = 100;
                }
                if (blurRatio != this.mLastBlurRatio) {
                    this.mLastBlurRatio = blurRatio;
                    LayoutParams layoutParams;
                    if (blurRatio == 0) {
                        layoutParams = this.mLp;
                        layoutParams.flags &= -5;
                    } else {
                        this.mLp.blurRatio = (((float) blurRatio) * 1.0f) / 100.0f;
                        layoutParams = this.mLp;
                        layoutParams.flags |= 4;
                    }
                    this.mWindowManager.updateViewLayout(this, this.mLp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
