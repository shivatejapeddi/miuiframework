package miui.maml.elements;

import android.graphics.Canvas;
import android.graphics.RectF;
import miui.maml.ScreenElementRoot;
import org.w3c.dom.Element;

public class EllipseScreenElement extends GeometryScreenElement {
    public static final String TAG_NAME = "Ellipse";

    public EllipseScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mAlign = Align.CENTER;
        this.mAlignV = AlignV.CENTER;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas c, DrawMode mode) {
        float width = getWidth();
        float height = getHeight();
        if (width >= 0.0f && height >= 0.0f) {
            if (mode == DrawMode.STROKE_OUTER) {
                width += this.mWeight;
                height += this.mWeight;
            } else if (mode == DrawMode.STROKE_INNER) {
                width -= this.mWeight;
                height -= this.mWeight;
                if (width < 0.0f || height < 0.0f) {
                    return;
                }
            }
            float left = 0.0f - (width / 2.0f);
            float top = 0.0f - (height / 2.0f);
            c.drawOval(new RectF(left, top, left + width, top + height), this.mPaint);
        }
    }
}
