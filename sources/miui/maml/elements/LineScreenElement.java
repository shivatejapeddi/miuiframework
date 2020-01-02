package miui.maml.elements;

import android.graphics.Canvas;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import org.w3c.dom.Element;

public class LineScreenElement extends GeometryScreenElement {
    public static final String TAG_NAME = "Line";
    private Expression mEndXExp;
    private Expression mEndYExp;

    public LineScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mEndXExp = Expression.build(root.getVariables(), node.getAttribute("x1"));
        this.mEndYExp = Expression.build(root.getVariables(), node.getAttribute("y1"));
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas c, DrawMode mode) {
        c.drawLine(0.0f, 0.0f, getEndX() - getX(), getEndY() - getY(), this.mPaint);
    }

    private final float getEndX() {
        Expression expression = this.mEndXExp;
        return scale(expression != null ? expression.evaluate() : 0.0d);
    }

    private final float getEndY() {
        Expression expression = this.mEndYExp;
        return scale(expression != null ? expression.evaluate() : 0.0d);
    }
}
