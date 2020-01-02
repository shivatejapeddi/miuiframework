package miui.maml;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import miui.maml.RendererController.IRenderable;
import miui.maml.ScreenElementRoot.OnHoverChangeListener;
import miui.maml.data.VariableNames;
import miui.maml.data.Variables;
import miui.maml.util.MamlAccessHelper;

public class MiAdvancedView extends View implements IRenderable {
    private static final String LOG_TAG = "MiAdvancedView";
    private boolean mLoggedHardwareRender;
    private MamlAccessHelper mMamlAccessHelper;
    protected boolean mNeedDisallowInterceptTouchEvent;
    private boolean mPaused;
    private int mPivotX;
    private int mPivotY;
    protected ScreenElementRoot mRoot;
    private float mScale;
    private RenderThread mThread;
    private boolean mUseExternalRenderThread;

    public MiAdvancedView(Context context, ScreenElementRoot root) {
        super(context);
        this.mPaused = true;
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.mRoot = root;
        ScreenElementRoot screenElementRoot = this.mRoot;
        if (screenElementRoot != null) {
            screenElementRoot.setOnHoverChangeListener(new OnHoverChangeListener() {
                public void onHoverChange(String contentDescription) {
                    MiAdvancedView.this.setContentDescription(contentDescription);
                    MiAdvancedView.this.sendAccessibilityEvent(32768);
                }
            });
        }
        if (VERSION.SDK_INT >= 23) {
            this.mMamlAccessHelper = new MamlAccessHelper(this.mRoot, this);
            setAccessibilityDelegate(this.mMamlAccessHelper);
        }
    }

    public MiAdvancedView(Context context, ScreenElementRoot root, RenderThread t) {
        this(context, root);
        if (t != null) {
            this.mUseExternalRenderThread = true;
            this.mThread = t;
            init();
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        String text = this.mRoot;
        if (text != null) {
            info.setText(text.getRawAttr("accessibilityText"));
        }
        super.onInitializeAccessibilityNodeInfo(info);
    }

    public void init() {
        this.mRoot.setRenderControllerRenderable(this);
        this.mRoot.setConfiguration(getResources().getConfiguration());
        this.mRoot.attachToRenderThread(this.mThread);
        this.mRoot.selfInit();
    }

    public void cleanUp() {
        cleanUp(false);
    }

    public void cleanUp(boolean keepResource) {
        this.mRoot.setKeepResource(keepResource);
        setOnTouchListener(null);
        RenderThread renderThread = this.mThread;
        if (renderThread == null) {
            return;
        }
        if (this.mUseExternalRenderThread) {
            this.mRoot.detachFromRenderThread(renderThread);
            this.mRoot.selfFinish();
            return;
        }
        renderThread.setStop();
        this.mThread = null;
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

    /* Access modifiers changed, original: protected */
    public boolean dispatchHoverEvent(MotionEvent event) {
        MamlAccessHelper mamlAccessHelper = this.mMamlAccessHelper;
        if (mamlAccessHelper == null || !mamlAccessHelper.dispatchHoverEvent(event)) {
            return super.dispatchHoverEvent(event);
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean b = this.mRoot;
        if (!b) {
            return false;
        }
        b = b.needDisallowInterceptTouchEvent();
        if (this.mNeedDisallowInterceptTouchEvent != b) {
            getParent().requestDisallowInterceptTouchEvent(b);
            this.mNeedDisallowInterceptTouchEvent = b;
        }
        this.mRoot.postMessage(MotionEvent.obtain(event));
        return true;
    }

    public boolean onHoverEvent(MotionEvent event) {
        ScreenElementRoot screenElementRoot = this.mRoot;
        if (screenElementRoot != null) {
            screenElementRoot.postMessage(MotionEvent.obtain(event));
        }
        return super.onHoverEvent(event);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        RenderThread renderThread = this.mThread;
        if (renderThread != null && renderThread.isStarted()) {
            if (!this.mLoggedHardwareRender) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("canvas hardware render: ");
                stringBuilder.append(canvas.isHardwareAccelerated());
                Log.d(LOG_TAG, stringBuilder.toString());
                this.mLoggedHardwareRender = true;
            }
            if (this.mScale != 0.0f) {
                int sa = canvas.save();
                float f = this.mScale;
                canvas.scale(f, f, (float) this.mPivotX, (float) this.mPivotY);
                this.mRoot.render(canvas);
                canvas.restoreToCount(sa);
            } else {
                this.mRoot.render(canvas);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mUseExternalRenderThread && this.mThread == null) {
            this.mThread = new RenderThread();
            init();
            onCreateRenderThread(this.mThread);
            this.mThread.setPaused(this.mPaused);
            this.mThread.start();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void onPause() {
        this.mPaused = true;
        RenderThread renderThread = this.mThread;
        if (renderThread == null) {
            return;
        }
        if (this.mUseExternalRenderThread) {
            this.mRoot.selfPause();
        } else {
            renderThread.setPaused(true);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
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
        this.mRoot.requestUpdate();
    }

    public void onResume() {
        this.mPaused = false;
        RenderThread renderThread = this.mThread;
        if (renderThread == null) {
            return;
        }
        if (this.mUseExternalRenderThread) {
            this.mRoot.selfResume();
        } else {
            renderThread.setPaused(false);
        }
    }

    public void doRender() {
        postInvalidate();
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

    /* Access modifiers changed, original: protected */
    public void onCreateRenderThread(RenderThread t) {
    }
}
