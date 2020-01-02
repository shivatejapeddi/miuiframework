package miui.maml.shader;

import android.graphics.SweepGradient;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import org.w3c.dom.Element;

public class SweepGradientElement extends ShaderElement {
    public static final String TAG_NAME = "SweepGradient";
    private float mAngle;
    private Expression mAngleExp;

    public SweepGradientElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mAngleExp = Expression.build(getVariables(), node.getAttribute("rotation"));
        this.mGradientStops.update();
    }

    public void onGradientStopsChanged() {
        this.mX = 0.0f;
        this.mY = 0.0f;
        this.mAngle = 0.0f;
        this.mShader = new SweepGradient(0.0f, 0.0f, this.mGradientStops.getColors(), this.mGradientStops.getPositions());
    }

    public boolean updateShaderMatrix() {
        float x = getX();
        float y = getY();
        float a = getAngle();
        if (x == this.mX && y == this.mY && a == this.mAngle) {
            return false;
        }
        this.mX = x;
        this.mY = y;
        this.mAngle = a;
        this.mShaderMatrix.reset();
        this.mShaderMatrix.preTranslate(-x, -y);
        this.mShaderMatrix.setRotate(a);
        this.mShaderMatrix.postTranslate(x, y);
        return true;
    }

    private final float getAngle() {
        Expression expression = this.mAngleExp;
        return expression != null ? (float) expression.evaluate() : 0.0f;
    }
}
