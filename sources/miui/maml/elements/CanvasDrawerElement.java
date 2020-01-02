package miui.maml.elements;

import android.graphics.Canvas;
import android.util.Log;
import miui.maml.CommandTrigger;
import miui.maml.ScreenElementRoot;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import org.w3c.dom.Element;

public class CanvasDrawerElement extends AnimatedScreenElement {
    public static final String TAG_NAME = "CanvasDrawer";
    private IndexedVariable mCanvasVar;
    private CommandTrigger mDrawCommands;
    private IndexedVariable mHVar;
    private IndexedVariable mWVar;
    private IndexedVariable mXVar;
    private IndexedVariable mYVar;

    public CanvasDrawerElement(Element node, ScreenElementRoot root) {
        super(node, root);
        if (this.mTriggers != null) {
            this.mDrawCommands = this.mTriggers.find("draw");
        }
        if (this.mDrawCommands == null) {
            Log.e(TAG_NAME, "no draw commands.");
        }
        Variables vars = getVariables();
        this.mXVar = new IndexedVariable("__x", vars, true);
        this.mYVar = new IndexedVariable("__y", vars, true);
        this.mWVar = new IndexedVariable("__w", vars, true);
        this.mHVar = new IndexedVariable("__h", vars, true);
        this.mCanvasVar = new IndexedVariable("__objCanvas", getVariables(), false);
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
        if (this.mDrawCommands != null) {
            float w = getWidthRaw();
            float h = getHeightRaw();
            float x = getLeft(0.0f, w);
            float y = getTop(0.0f, h);
            this.mXVar.set((double) x);
            this.mYVar.set((double) y);
            this.mWVar.set((double) w);
            this.mHVar.set((double) h);
            this.mCanvasVar.set((Object) c);
            this.mDrawCommands.perform();
            this.mCanvasVar.set(null);
        }
    }
}
