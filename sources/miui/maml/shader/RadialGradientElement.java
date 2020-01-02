package miui.maml.shader;

import android.graphics.RadialGradient;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import org.w3c.dom.Element;

public class RadialGradientElement extends ShaderElement {
    public static final String TAG_NAME = "RadialGradient";
    private float mRx;
    private Expression mRxExp;
    private float mRy;
    private Expression mRyExp;

    public RadialGradientElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mRxExp = Expression.build(getVariables(), node.getAttribute("rX"));
        this.mRyExp = Expression.build(getVariables(), node.getAttribute("rY"));
        this.mGradientStops.update();
    }

    public void onGradientStopsChanged() {
        this.mX = 0.0f;
        this.mY = 0.0f;
        this.mRx = 1.0f;
        this.mRy = 1.0f;
        this.mShader = new RadialGradient(0.0f, 0.0f, 1.0f, this.mGradientStops.getColors(), this.mGradientStops.getPositions(), this.mTileMode);
    }

    public boolean updateShaderMatrix() {
        float x = getX();
        float y = getY();
        float rX = getRx();
        float rY = getRy();
        if (x == this.mX && y == this.mY && rX == this.mRx && rY == this.mRy) {
            return false;
        }
        this.mX = x;
        this.mY = y;
        this.mRx = rX;
        this.mRy = rY;
        this.mShaderMatrix.reset();
        this.mShaderMatrix.preTranslate(-x, -y);
        this.mShaderMatrix.setScale(rX, rY);
        this.mShaderMatrix.postTranslate(x, y);
        return true;
    }

    private final float getRx() {
        Expression expression = this.mRxExp;
        return (float) (((double) this.mRoot.getScale()) * (expression != null ? expression.evaluate() : 0.0d));
    }

    private final float getRy() {
        Expression expression = this.mRyExp;
        return (float) (((double) this.mRoot.getScale()) * (expression != null ? expression.evaluate() : 0.0d));
    }
}
