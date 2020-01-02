package miui.maml.elements;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import miui.maml.data.Variables;
import org.w3c.dom.Element;

public class ArcScreenElement extends GeometryScreenElement {
    public static final String TAG_NAME = "Arc";
    private float mAngle;
    private Expression mAngleExp;
    private Path mArcPath = new Path();
    private boolean mClose;
    RectF mOvalRect = new RectF();
    private float mSweep;
    private Expression mSweepExp;

    public ArcScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        Variables variables = root.getVariables();
        this.mAngleExp = Expression.build(variables, getAttr(node, "startAngle"));
        this.mSweepExp = Expression.build(variables, getAttr(node, "sweep"));
        this.mClose = Boolean.parseBoolean(getAttr(node, "close"));
        this.mAlign = Align.CENTER;
        this.mAlignV = AlignV.CENTER;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas c, DrawMode mode) {
        float width = getWidth();
        float height = getHeight();
        float left = 0.0f - (width / 2.0f);
        float top = 0.0f - (height / 2.0f);
        this.mArcPath.reset();
        RectF rectF = this.mOvalRect;
        rectF.left = left;
        rectF.top = top;
        rectF.right = left + width;
        rectF.bottom = top + height;
        if (Math.abs(this.mSweep) >= 360.0f) {
            c.drawOval(this.mOvalRect, this.mPaint);
            return;
        }
        if (this.mClose) {
            this.mArcPath.moveTo(this.mOvalRect.centerX(), this.mOvalRect.centerY());
        }
        this.mArcPath.arcTo(this.mOvalRect, this.mAngle, this.mSweep, this.mClose ^ 1);
        if (this.mClose) {
            this.mArcPath.close();
        }
        c.drawPath(this.mArcPath, this.mPaint);
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        super.doTick(currentTime);
        if (isVisible()) {
            Variables vars = this.mRoot.getVariables();
            this.mAngle = (float) this.mAngleExp.evaluate();
            this.mSweep = (float) this.mSweepExp.evaluate();
        }
    }
}
