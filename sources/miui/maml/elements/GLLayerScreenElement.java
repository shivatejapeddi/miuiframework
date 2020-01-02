package miui.maml.elements;

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import miui.maml.CommandTrigger;
import miui.maml.RendererController;
import miui.maml.RendererController.EmptyListener;
import miui.maml.ScreenElementRoot;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import org.w3c.dom.Element;

public class GLLayerScreenElement extends ViewHolderScreenElement {
    private static final String LOG_TAG = "MAML GLLayerScreenElement";
    public static final String TAG_NAME = "GLLayer";
    private IndexedVariable mCanvasVar;
    private IndexedVariable mHVar;
    private LayoutParams mLayoutParams;
    private CommandTrigger mOnSurfaceChangeCommands;
    private CommandTrigger mOnSurfaceCreateCommands;
    private CommandTrigger mOnSurfaceDrawCommands;
    private GLSurfaceView mView;
    private IndexedVariable mViewVar;
    private IndexedVariable mWVar;

    private class GLRenderer implements Renderer {
        private GLRenderer() {
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            if (GLLayerScreenElement.this.mOnSurfaceCreateCommands != null) {
                GLLayerScreenElement.this.mCanvasVar.set((Object) gl);
                GLLayerScreenElement.this.mOnSurfaceCreateCommands.perform();
                GLLayerScreenElement.this.mCanvasVar.set(null);
            }
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            if (GLLayerScreenElement.this.mOnSurfaceChangeCommands != null) {
                GLLayerScreenElement.this.mCanvasVar.set((Object) gl);
                GLLayerScreenElement.this.mWVar.set((double) width);
                GLLayerScreenElement.this.mHVar.set((double) height);
                GLLayerScreenElement.this.mOnSurfaceChangeCommands.perform();
                GLLayerScreenElement.this.mCanvasVar.set(null);
            }
        }

        public void onDrawFrame(GL10 gl) {
            if (GLLayerScreenElement.this.mOnSurfaceDrawCommands != null) {
                GLLayerScreenElement.this.mCanvasVar.set((Object) gl);
                GLLayerScreenElement.this.mOnSurfaceDrawCommands.perform();
                GLLayerScreenElement.this.mCanvasVar.set(null);
            }
            if (GLLayerScreenElement.this.mController != null) {
                GLLayerScreenElement.this.mController.doneRender();
            }
        }
    }

    private class ProxyListener extends EmptyListener {
        private ProxyListener() {
        }

        public void tick(long currentTime) {
        }

        public void doRender() {
            GLLayerScreenElement.this.mView.requestRender();
        }

        public void triggerUpdate() {
            GLLayerScreenElement.this.mRoot.getRendererController().triggerUpdate();
        }
    }

    public GLLayerScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        load(node);
    }

    private void load(Element node) {
        int renderMode;
        this.mView = new GLSurfaceView(this.mRoot.getContext().mContext);
        this.mLayoutParams = new LayoutParams((int) this.mRoot.getWidth(), (int) this.mRoot.getHeight());
        LayoutParams layoutParams = this.mLayoutParams;
        layoutParams.format = 1;
        layoutParams.flags = 256;
        this.mView.setRenderer(new GLRenderer());
        if (this.mController != null) {
            renderMode = 0;
        } else {
            renderMode = 1;
        }
        this.mView.setRenderMode(renderMode);
        if (this.mTriggers != null) {
            this.mOnSurfaceCreateCommands = this.mTriggers.find("create");
            this.mOnSurfaceChangeCommands = this.mTriggers.find("change");
            this.mOnSurfaceDrawCommands = this.mTriggers.find("draw");
        }
        if (this.mOnSurfaceDrawCommands == null) {
            Log.e("GLLayerScreenElement", "no draw commands.");
        }
        Variables vars = getVariables();
        this.mCanvasVar = new IndexedVariable("__objGLCanvas", vars, false);
        this.mViewVar = new IndexedVariable("__objGLView", vars, false);
        this.mWVar = new IndexedVariable("__w", vars, true);
        this.mHVar = new IndexedVariable("__h", vars, true);
    }

    /* Access modifiers changed, original: protected */
    public void onControllerCreated(RendererController c) {
        c.setListener(new ProxyListener());
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        doTickSelf(currentTime);
        udpateView();
    }

    public void init() {
        this.mViewVar.set(this.mView);
        super.init();
    }

    /* Access modifiers changed, original: protected */
    public View getView() {
        return this.mView;
    }
}
