package miui.maml.elements;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import miui.maml.RendererController;
import miui.maml.RendererController.EmptyListener;
import miui.maml.ScreenElementRoot;
import org.w3c.dom.Element;

public class WindowScreenElement extends ElementGroupRC {
    public static final String TAG_NAME = "Window";
    private LayoutParams mLayoutParams;
    private WindowView mView = new WindowView(this.mWindowContext);
    private boolean mViewAdded;
    private Context mWindowContext;
    private WindowManager mWindowManager = ((WindowManager) this.mWindowContext.getSystemService(Context.WINDOW_SERVICE));

    private class ProxyListener extends EmptyListener {
        private ProxyListener() {
        }

        /* synthetic */ ProxyListener(WindowScreenElement x0, AnonymousClass1 x1) {
            this();
        }

        public void tick(long currentTime) {
            WindowScreenElement.this.doTick(currentTime);
        }

        public void doRender() {
            WindowScreenElement.this.mView.postInvalidate();
        }

        public void onTouch(MotionEvent event) {
            WindowScreenElement.this.onTouch(event);
        }

        public void onHover(MotionEvent event) {
            WindowScreenElement.this.onHover(event);
        }

        public void triggerUpdate() {
            WindowScreenElement.this.mRoot.getRendererController().triggerUpdate();
        }
    }

    private class WindowView extends View {
        public WindowView(Context context) {
            super(context);
        }

        /* Access modifiers changed, original: protected */
        public void onDraw(Canvas canvas) {
            WindowScreenElement.this.doRenderWithTranslation(canvas);
            WindowScreenElement.this.mController.doneRender();
        }

        public boolean onTouchEvent(MotionEvent event) {
            WindowScreenElement.this.mController.postMessage(event);
            return super.onTouchEvent(event);
        }
    }

    public WindowScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mWindowContext = root.getContext().mContext;
        this.mLayoutParams = new LayoutParams((int) root.getWidth(), (int) root.getHeight());
        LayoutParams layoutParams = this.mLayoutParams;
        layoutParams.format = 1;
        layoutParams.flags = 256;
    }

    public void init() {
        super.init();
        if (isVisible()) {
            addView();
        }
    }

    public void render(Canvas c) {
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChange(boolean visible) {
        final boolean _v = visible;
        getContext().getHandler().post(new Runnable() {
            public void run() {
                if (_v) {
                    WindowScreenElement.this.addView();
                } else {
                    WindowScreenElement.this.removeView();
                }
            }
        });
    }

    private final void addView() {
        if (!this.mViewAdded) {
            this.mWindowManager.addView(this.mView, this.mLayoutParams);
            this.mViewAdded = true;
        }
    }

    private final void removeView() {
        if (this.mViewAdded) {
            this.mWindowManager.removeView(this.mView);
            this.mViewAdded = false;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onControllerCreated(RendererController c) {
        c.setListener(new ProxyListener(this, null));
    }
}
