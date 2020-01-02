package miui.maml.elements;

import android.graphics.Canvas;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import org.w3c.dom.Element;

public class CircleScreenElement extends GeometryScreenElement {
    public static final String TAG_NAME = "Circle";
    private Expression mRadiusExp;

    public CircleScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mRadiusExp = Expression.build(root.getVariables(), getAttr(node, "r"));
        this.mAlign = Align.CENTER;
        this.mAlignV = AlignV.CENTER;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas c, DrawMode mode) {
        float r = getRadius();
        if (mode == DrawMode.STROKE_OUTER) {
            r += this.mWeight / 2.0f;
        } else if (mode == DrawMode.STROKE_INNER) {
            r -= this.mWeight / 2.0f;
        }
        c.drawCircle(0.0f, 0.0f, r, this.mPaint);
    }

    private final float getRadius() {
        Expression expression = this.mRadiusExp;
        return scale((double) (expression != null ? (float) expression.evaluate() : 0.0f));
    }
}
