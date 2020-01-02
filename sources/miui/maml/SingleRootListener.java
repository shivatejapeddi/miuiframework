package miui.maml;

import android.view.MotionEvent;
import java.lang.ref.WeakReference;
import miui.maml.RendererController.IRenderable;
import miui.maml.RendererController.ISelfUpdateRenderable;
import miui.maml.RendererController.Listener;

public class SingleRootListener implements Listener {
    private static final String LOG_TAG = "SingleRootListener";
    private WeakReference<IRenderable> mRenderable;
    private ScreenElementRoot mRoot;

    public SingleRootListener(ScreenElementRoot root, IRenderable r) {
        setRoot(root);
        setRenderable(r);
    }

    public void setRoot(ScreenElementRoot root) {
        if (root != null) {
            this.mRoot = root;
            return;
        }
        throw new NullPointerException("root is null");
    }

    public void setRenderable(IRenderable r) {
        this.mRenderable = r != null ? new WeakReference(r) : null;
    }

    public void finish() {
        this.mRoot.finish();
    }

    public void init() {
        this.mRoot.init();
    }

    public void onTouch(MotionEvent event) {
        this.mRoot.onTouch(event);
        event.recycle();
    }

    public void onHover(MotionEvent event) {
        this.mRoot.onHover(event);
        event.recycle();
    }

    public void pause() {
        this.mRoot.pause();
    }

    public void resume() {
        this.mRoot.resume();
    }

    public void tick(long currentTime) {
        this.mRoot.tick(currentTime);
    }

    public void doRender() {
        WeakReference weakReference = this.mRenderable;
        IRenderable r = weakReference != null ? (IRenderable) weakReference.get() : null;
        if (r != null) {
            r.doRender();
        }
    }

    public void triggerUpdate() {
        WeakReference weakReference = this.mRenderable;
        IRenderable r = weakReference != null ? (IRenderable) weakReference.get() : null;
        if (r != null && (r instanceof ISelfUpdateRenderable)) {
            ((ISelfUpdateRenderable) r).triggerUpdate();
        }
    }
}
