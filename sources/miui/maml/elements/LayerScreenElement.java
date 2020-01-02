package miui.maml.elements;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import miui.maml.ScreenElementRoot;
import org.w3c.dom.Element;

public class LayerScreenElement extends ViewHolderScreenElement {
    public static final String TAG_NAME = "Layer";
    private LayerView mView;

    private class LayerView extends View {
        public LayerView(Context context) {
            super(context);
        }

        /* Access modifiers changed, original: protected */
        public void onDraw(Canvas canvas) {
            LayerScreenElement.this.doRender(canvas);
            LayerScreenElement.this.mController.doneRender();
        }
    }

    public LayerScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mView = new LayerView(root.getContext().mContext);
    }

    /* Access modifiers changed, original: protected */
    public View getView() {
        return this.mView;
    }
}
