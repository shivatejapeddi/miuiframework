package miui.maml.elements;

import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewManager;
import android.view.WindowManager;
import miui.maml.RendererController;
import miui.maml.RendererController.EmptyListener;
import miui.maml.ScreenElementRoot;
import miui.maml.animation.BaseAnimation;
import org.w3c.dom.Element;

public abstract class ViewHolderScreenElement extends ElementGroupRC {
    private static final String LOG_TAG = "MAML ViewHolderScreenElement";
    private boolean mHardware;
    private LayoutParams mLayoutParams = getLayoutParam();
    protected boolean mUpdatePosition;
    protected boolean mUpdateSize;
    protected boolean mUpdateTranslation;
    private boolean mViewAdded;

    private class ProxyListener extends EmptyListener {
        private ProxyListener() {
        }

        /* synthetic */ ProxyListener(ViewHolderScreenElement x0, AnonymousClass1 x1) {
            this();
        }

        public void tick(long currentTime) {
            ViewHolderScreenElement.this.doTickChildren(currentTime);
        }

        public void doRender() {
            ViewHolderScreenElement.this.getView().postInvalidate();
        }

        public void triggerUpdate() {
            ViewHolderScreenElement.this.mRoot.getRendererController().triggerUpdate();
        }
    }

    public abstract View getView();

    public ViewHolderScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mHardware = Boolean.parseBoolean(getAttr(node, "hardware"));
        this.mUpdatePosition = getAttrAsBoolean(getAttr(node, "updatePosition"), true);
        this.mUpdateSize = getAttrAsBoolean(getAttr(node, "updateSize"), true);
        this.mUpdateTranslation = getAttrAsBoolean(getAttr(node, "updateTranslation"), true);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams getLayoutParam() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(-1, -1);
        lp.format = 1;
        lp.flags = 256;
        return lp;
    }

    private static boolean getAttrAsBoolean(String att, boolean def) {
        if (TextUtils.isEmpty(att)) {
            return def;
        }
        return Boolean.parseBoolean(att);
    }

    public void init() {
        super.init();
        if (this.mRoot.getViewManager() != null) {
            initView();
        } else {
            Log.e(LOG_TAG, "ViewManager must be set before init");
        }
    }

    public void finish() {
        super.finish();
        finishView();
    }

    public void render(Canvas c) {
    }

    public void setHardwareLayer(final boolean b) {
        postInMainThread(new Runnable() {
            public void run() {
                ViewHolderScreenElement.this.getView().setLayerType(b ? 2 : 0, null);
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        if (this.mController == null) {
            super.doTick(currentTime);
            getView().postInvalidate();
        } else {
            doTickSelf(currentTime);
        }
        udpateView();
    }

    /* Access modifiers changed, original: protected */
    public void doTickSelf(long currentTime) {
        if (this.mAnimations != null) {
            int N = this.mAnimations.size();
            for (int i = 0; i < N; i++) {
                ((BaseAnimation) this.mAnimations.get(i)).tick(currentTime);
            }
        }
        this.mAlpha = evaluateAlpha();
        this.mAlpha = this.mAlpha < 0 ? 0 : this.mAlpha;
    }

    /* Access modifiers changed, original: protected */
    public void udpateView() {
        if (this.mUpdatePosition || this.mUpdateTranslation || this.mUpdateSize) {
            postInMainThread(new Runnable() {
                public void run() {
                    if (ViewHolderScreenElement.this.mViewAdded) {
                        ViewHolderScreenElement.this.onUpdateView(ViewHolderScreenElement.this.getView());
                    }
                }
            });
        }
    }

    /* Access modifiers changed, original: protected */
    public void onUpdateView(View v) {
        if (this.mUpdatePosition) {
            v.setX(getAbsoluteLeft());
            v.setY(getAbsoluteTop());
        }
        if (this.mUpdateTranslation) {
            v.setPivotX(getPivotX());
            v.setPivotY(getPivotY());
            v.setRotation(getRotation());
            v.setRotationX(getRotationX());
            v.setRotationY(getRotationY());
            v.setAlpha(((float) getAlpha()) / 255.0f);
            v.setScaleX(getScaleX());
            v.setScaleY(getScaleY());
        }
        if (this.mUpdateSize && updateLayoutParams(this.mLayoutParams)) {
            v.setLayoutParams(this.mLayoutParams);
        }
    }

    private final void initView() {
        if (!this.mViewAdded) {
            postInMainThread(new Runnable() {
                public void run() {
                    ViewManager vm = ViewHolderScreenElement.this.mRoot.getViewManager();
                    if (!ViewHolderScreenElement.this.mViewAdded && vm != null) {
                        View v = ViewHolderScreenElement.this.getView();
                        ViewHolderScreenElement.this.onUpdateView(v);
                        vm.addView(v, ViewHolderScreenElement.this.mLayoutParams);
                        if (ViewHolderScreenElement.this.mHardware) {
                            v.setLayerType(2, null);
                        }
                        ViewHolderScreenElement.this.mViewAdded = true;
                        ViewHolderScreenElement.this.onViewAdded(v);
                    }
                }
            });
        }
    }

    /* Access modifiers changed, original: protected */
    public void onViewAdded(View v) {
    }

    /* Access modifiers changed, original: protected */
    public void onViewRemoved(View v) {
    }

    private final void finishView() {
        if (this.mViewAdded && this.mRoot.getViewManager() != null) {
            postInMainThread(new Runnable() {
                public void run() {
                    ViewManager vm = ViewHolderScreenElement.this.mRoot.getViewManager();
                    if (vm != null) {
                        View v = ViewHolderScreenElement.this.getView();
                        vm.removeView(v);
                        ViewHolderScreenElement.this.mViewAdded = false;
                        ViewHolderScreenElement.this.onViewRemoved(v);
                    }
                }
            });
        }
    }

    private boolean updateLayoutParams(LayoutParams lp) {
        boolean changed = false;
        int width = (int) getWidth();
        if (lp.width != width) {
            lp.width = width;
            changed = true;
        }
        int height = (int) getHeight();
        if (lp.height == height) {
            return changed;
        }
        lp.height = height;
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChange(boolean visible) {
        final boolean _v = visible;
        postInMainThread(new Runnable() {
            public void run() {
                ViewHolderScreenElement.this.getView().setVisibility(_v ? 0 : 4);
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public void onControllerCreated(RendererController c) {
        c.setListener(new ProxyListener(this, null));
    }

    /* Access modifiers changed, original: protected */
    public boolean isViewAdded() {
        return this.mViewAdded;
    }
}
