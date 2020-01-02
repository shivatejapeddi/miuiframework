package miui.maml.elements;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import miui.maml.ScreenElementRoot;
import org.w3c.dom.Element;

public class RectangleScreenElement extends GeometryScreenElement {
    private static final String LOG_TAG = "RectangleScreenElement";
    public static final String TAG_NAME = "Rectangle";
    private float mCornerRadiusX;
    private float mCornerRadiusY;

    public RectangleScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        resolveCornerRadius(node);
    }

    private void resolveCornerRadius(Element node) {
        String[] rs = getAttr(node, "cornerRadius").split(",");
        try {
            if (rs.length >= 1) {
                if (rs.length == 1) {
                    float scale = scale((double) Float.parseFloat(rs[0]));
                    this.mCornerRadiusY = scale;
                    this.mCornerRadiusX = scale;
                } else {
                    this.mCornerRadiusX = scale((double) Float.parseFloat(rs[0]));
                    this.mCornerRadiusY = scale((double) Float.parseFloat(rs[1]));
                }
            }
        } catch (NumberFormatException e) {
            Log.w(LOG_TAG, "illegal number format of cornerRadius.");
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas c, DrawMode mode) {
        float width = getWidth();
        float height = getHeight();
        float left = getLeft(0.0f, width);
        float top = getTop(0.0f, height);
        float right = (width > 0.0f ? width : 0.0f) + left;
        float bottom = (height > 0.0f ? height : 0.0f) + top;
        if (mode == DrawMode.STROKE_OUTER) {
            left -= this.mWeight / 2.0f;
            top -= this.mWeight / 2.0f;
            right += this.mWeight / 2.0f;
            bottom += this.mWeight / 2.0f;
        } else if (mode == DrawMode.STROKE_INNER) {
            left += this.mWeight / 2.0f;
            top += this.mWeight / 2.0f;
            right -= this.mWeight / 2.0f;
            bottom -= this.mWeight / 2.0f;
        }
        if (this.mCornerRadiusX <= 0.0f || this.mCornerRadiusY <= 0.0f) {
            c.drawRect(left, top, right, bottom, this.mPaint);
        } else {
            c.drawRoundRect(new RectF(left, top, right, bottom), this.mCornerRadiusX, this.mCornerRadiusY, this.mPaint);
        }
    }
}
