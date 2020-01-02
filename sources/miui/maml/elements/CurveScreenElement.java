package miui.maml.elements;

import android.graphics.Canvas;
import miui.maml.ScreenElementRoot;
import org.w3c.dom.Element;

public class CurveScreenElement extends GeometryScreenElement {
    public static final String TAG_NAME = "Curve";

    public CurveScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas c, DrawMode mode) {
    }
}
