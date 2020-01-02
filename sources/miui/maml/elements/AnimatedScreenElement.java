package miui.maml.elements;

import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.miui.BiometricConnect;
import android.os.BatteryManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import miui.maml.ScreenElementRoot;
import miui.maml.animation.AlphaAnimation;
import miui.maml.animation.BaseAnimation;
import miui.maml.animation.PositionAnimation;
import miui.maml.animation.RotationAnimation;
import miui.maml.animation.ScaleAnimation;
import miui.maml.animation.SizeAnimation;
import miui.maml.data.Expression;
import miui.maml.data.Expression.NumberExpression;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import miui.maml.util.Utils;
import org.w3c.dom.Element;

public abstract class AnimatedScreenElement extends ScreenElement {
    public static final String LOG_TAG = "AnimatedScreenElement";
    private static final int sHoverPaintColor = -2130706688;
    private static final int sPaintColor = -4982518;
    private IndexedVariable mActualXVar;
    private IndexedVariable mActualYVar;
    protected int mAlpha;
    private Expression mAlphaExpression;
    private AlphaAnimation mAlphas;
    private Expression mBaseXExpression;
    private Expression mBaseYExpression;
    private Camera mCamera;
    protected String mContentDescription;
    protected Expression mContentDescriptionExp;
    protected boolean mHasContentDescription;
    private Expression mHeightExpression;
    private Paint mHoverPaint = new Paint();
    protected boolean mInterceptTouch;
    private boolean mIsHaptic;
    private float mMarginBottom;
    private float mMarginLeft;
    private float mMarginRight;
    private float mMarginTop;
    private Matrix mMatrix = new Matrix();
    private Paint mPaint = new Paint();
    private Expression mPivotXExpression;
    private Expression mPivotYExpression;
    private Expression mPivotZ;
    private PositionAnimation mPositions;
    protected boolean mPressed;
    private Expression mRotationExpression;
    private Expression mRotationX;
    private Expression mRotationY;
    private Expression mRotationZ;
    private RotationAnimation mRotations;
    private Expression mScaleExpression;
    private Expression mScaleXExpression;
    private Expression mScaleYExpression;
    private ScaleAnimation mScales;
    private SizeAnimation mSizes;
    protected boolean mTouchable;
    private Expression mWidthExpression;

    public AnimatedScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        load(node);
        if (this.mHasContentDescription) {
            this.mRoot.addAccessibleElements(this);
        }
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth(1.0f);
        this.mPaint.setColor((int) sPaintColor);
        this.mHoverPaint.setStyle(Style.STROKE);
        this.mHoverPaint.setStrokeWidth(1.0f);
        this.mHoverPaint.setColor((int) sHoverPaintColor);
    }

    /* Access modifiers changed, original: protected */
    public BaseAnimation onCreateAnimation(String tag, Element ele) {
        if (AlphaAnimation.TAG_NAME.equals(tag)) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(ele, this);
            this.mAlphas = alphaAnimation;
            return alphaAnimation;
        } else if (PositionAnimation.TAG_NAME.equals(tag)) {
            PositionAnimation positionAnimation = new PositionAnimation(ele, this);
            this.mPositions = positionAnimation;
            return positionAnimation;
        } else if (RotationAnimation.TAG_NAME.equals(tag)) {
            RotationAnimation rotationAnimation = new RotationAnimation(ele, this);
            this.mRotations = rotationAnimation;
            return rotationAnimation;
        } else if (SizeAnimation.TAG_NAME.equals(tag)) {
            SizeAnimation sizeAnimation = new SizeAnimation(ele, this);
            this.mSizes = sizeAnimation;
            return sizeAnimation;
        } else if (!ScaleAnimation.TAG_NAME.equals(tag)) {
            return super.onCreateAnimation(tag, ele);
        } else {
            ScaleAnimation scaleAnimation = new ScaleAnimation(ele, this);
            this.mScales = scaleAnimation;
            return scaleAnimation;
        }
    }

    private void load(Element node) {
        if (node != null) {
            Variables vars = getVariables();
            this.mBaseXExpression = createExp(vars, node, "x", "left");
            this.mBaseYExpression = createExp(vars, node, "y", "top");
            this.mWidthExpression = createExp(vars, node, BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_W, "width");
            this.mHeightExpression = createExp(vars, node, BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_H, "height");
            this.mRotationExpression = createExp(vars, node, "angle", "rotation");
            this.mPivotXExpression = createExp(vars, node, "centerX", "pivotX");
            this.mPivotYExpression = createExp(vars, node, "centerY", "pivotY");
            this.mAlphaExpression = createExp(vars, node, "alpha", null);
            this.mScaleExpression = createExp(vars, node, BatteryManager.EXTRA_SCALE, null);
            this.mScaleXExpression = createExp(vars, node, "scaleX", null);
            this.mScaleYExpression = createExp(vars, node, "scaleY", null);
            this.mRotationX = createExp(vars, node, "angleX", "rotationX");
            this.mRotationY = createExp(vars, node, "angleY", "rotationY");
            this.mRotationZ = createExp(vars, node, "angleZ", "rotationZ");
            this.mPivotZ = createExp(vars, node, "centerZ", "pivotZ");
            boolean z = true;
            if (this.mHasName) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                String str = ".";
                stringBuilder.append(str);
                stringBuilder.append(ScreenElement.ACTUAL_X);
                this.mActualXVar = new IndexedVariable(stringBuilder.toString(), vars, true);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(str);
                stringBuilder.append(ScreenElement.ACTUAL_Y);
                this.mActualYVar = new IndexedVariable(stringBuilder.toString(), vars, true);
            }
            if (!(this.mRotationX == null && this.mRotationY == null && this.mRotationZ == null)) {
                this.mCamera = new Camera();
            }
            this.mTouchable = Boolean.parseBoolean(getAttr(node, "touchable"));
            this.mInterceptTouch = Boolean.parseBoolean(getAttr(node, "interceptTouch"));
            this.mIsHaptic = Boolean.parseBoolean(getAttr(node, "haptic"));
            this.mContentDescription = getAttr(node, "contentDescription");
            this.mContentDescriptionExp = Expression.build(vars, getAttr(node, "contentDescriptionExp"));
            if (TextUtils.isEmpty(this.mContentDescription) && this.mContentDescriptionExp == null) {
                z = false;
            }
            this.mHasContentDescription = z;
            this.mMarginLeft = Utils.getAttrAsFloat(node, "marginLeft", 0.0f);
            this.mMarginRight = Utils.getAttrAsFloat(node, "marginRight", 0.0f);
            this.mMarginTop = Utils.getAttrAsFloat(node, "marginTop", 0.0f);
            this.mMarginBottom = Utils.getAttrAsFloat(node, "marginBottom", 0.0f);
        }
    }

    private Expression createExp(Variables vars, Element node, String name, String compatibleName) {
        Expression exp = Expression.build(vars, getAttr(node, name));
        if (exp != null || TextUtils.isEmpty(compatibleName)) {
            return exp;
        }
        return Expression.build(vars, getAttr(node, compatibleName));
    }

    public void render(Canvas c) {
        updateVisibility();
        if (isVisible()) {
            doRenderWithTranslation(c);
        }
    }

    /* Access modifiers changed, original: protected */
    public void doRenderWithTranslation(Canvas c) {
        float width;
        float left;
        Canvas canvas = c;
        int sc = c.save();
        this.mMatrix.reset();
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.save();
            float rx = getRotationX();
            float ry = getRotationY();
            float rz = getRotationZ();
            if (!(rx == 0.0f && ry == 0.0f && rz == 0.0f)) {
                this.mCamera.rotate(rx, ry, rz);
                Expression expression = this.mPivotZ;
                if (expression != null) {
                    this.mCamera.translate(0.0f, 0.0f, (float) expression.evaluate());
                }
                this.mCamera.getMatrix(this.mMatrix);
                this.mCamera.restore();
            }
        }
        float rotation = getRotation();
        if (rotation != 0.0f) {
            this.mMatrix.preRotate(rotation);
        }
        float scaleX = getScaleX();
        float scaleY = getScaleY();
        if (!(scaleX == 1.0f && scaleY == 1.0f)) {
            this.mMatrix.preScale(scaleX, scaleY);
        }
        float x = getX();
        float y = getY();
        float pivotXToX = getPivotX() - (x - getLeft());
        float pivotYToY = getPivotY() - (y - getTop());
        this.mMatrix.preTranslate(-pivotXToX, -pivotYToY);
        this.mMatrix.postTranslate(pivotXToX + x, pivotYToY + y);
        canvas.concat(this.mMatrix);
        doRender(c);
        if (this.mRoot.mShowDebugLayout) {
            width = getWidth();
            float height = getHeight();
            if (width <= 0.0f || height <= 0.0f) {
                float f = width;
                float f2 = pivotYToY;
            } else {
                left = getLeft(0.0f, width);
                float top = getTop(0.0f, height);
                c.drawRect(left, top, left + width, top + height, this.mPaint);
            }
        }
        if (this.mRoot.getHoverElement() == this) {
            pivotYToY = getWidth();
            width = getHeight();
            if (pivotYToY <= 0.0f || width <= 0.0f) {
                float f3 = pivotYToY;
            } else {
                left = getLeft(0.0f, pivotYToY);
                float top2 = getTop(0.0f, width);
                c.drawRect(left, top2, left + pivotYToY, top2 + width, this.mHoverPaint);
            }
        }
        canvas.restoreToCount(sc);
    }

    /* Access modifiers changed, original: protected */
    public Matrix getMatrix() {
        return this.mMatrix;
    }

    /* Access modifiers changed, original: protected */
    public float getLeft() {
        return getLeft(getX(), getWidth());
    }

    /* Access modifiers changed, original: protected */
    public float getTop() {
        return getTop(getY(), getHeight());
    }

    public float getAbsoluteLeft() {
        return getLeft() + (this.mParent == null ? 0.0f : this.mParent.getParentLeft());
    }

    public float getAbsoluteTop() {
        return getTop() + (this.mParent == null ? 0.0f : this.mParent.getParentTop());
    }

    public boolean touched(float x, float y) {
        return touched(x, y, true);
    }

    public boolean touched(float x, float y, boolean isAbsolute) {
        float parentTop;
        float parentLeft;
        if (isAbsolute) {
            parentTop = 0.0f;
            parentLeft = this.mParent == null ? 0.0f : this.mParent.getParentLeft();
            if (this.mParent != null) {
                parentTop = this.mParent.getParentTop();
            }
            x -= parentLeft;
            y -= parentTop;
        }
        parentLeft = getLeft();
        parentTop = getTop();
        return x >= parentLeft && x <= parentLeft + getWidth() && y >= parentTop && y <= parentTop + getHeight();
    }

    public boolean onTouch(MotionEvent event) {
        boolean z = false;
        if (!isVisible() || !this.mTouchable) {
            return false;
        }
        float x = event.getX();
        float y = event.getY();
        boolean ret = super.onTouch(event);
        int actionMasked = event.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked == 3) {
                        handleCancel();
                    }
                } else if (this.mPressed) {
                    ret = touched(x, y);
                    performAction("move");
                    onActionMove(x, y);
                }
            } else if (this.mPressed) {
                this.mPressed = false;
                if (touched(x, y)) {
                    if (this.mIsHaptic) {
                        this.mRoot.haptic(1);
                    }
                    performAction("up");
                    onActionUp();
                } else {
                    performAction("cancel");
                    onActionCancel();
                }
                ret = true;
            }
        } else if (touched(x, y)) {
            this.mPressed = true;
            if (this.mIsHaptic) {
                this.mRoot.haptic(1);
            }
            performAction("down");
            onActionDown(x, y);
            ret = true;
        }
        if (ret) {
            requestUpdate();
        }
        if (ret && this.mInterceptTouch) {
            z = true;
        }
        return z;
    }

    public boolean onHover(MotionEvent event) {
        boolean z = false;
        if (!isVisible() || !this.mHasContentDescription) {
            return false;
        }
        String contentDescription = getContentDescription();
        float x = event.getX();
        float y = event.getY();
        boolean ret = super.onHover(event);
        int actionMasked = event.getActionMasked();
        if (actionMasked != 7) {
            if (actionMasked == 9 && touched(x, y)) {
                this.mRoot.onHoverChange(this, contentDescription);
                ret = true;
            }
        } else if (touched(x, y)) {
            if (this.mRoot.getHoverElement() != this) {
                this.mRoot.onHoverChange(this, contentDescription);
            }
            ret = true;
        }
        if (ret) {
            requestUpdate();
        }
        if (ret && this.mInterceptTouch) {
            z = true;
        }
        return z;
    }

    public String getContentDescription() {
        String contentDescription = this.mContentDescriptionExp;
        if (contentDescription == null) {
            return this.mContentDescription;
        }
        contentDescription = contentDescription.evaluateStr();
        if (contentDescription == null) {
            contentDescription = "";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("element.getContentDescription() == null ");
            stringBuilder.append(this.mName);
            Log.e(LOG_TAG, stringBuilder.toString());
        }
        return contentDescription;
    }

    /* Access modifiers changed, original: protected */
    public void onActionDown(float x, float y) {
        this.mRoot.onUIInteractive(this, "down");
    }

    /* Access modifiers changed, original: protected */
    public void onActionMove(float x, float y) {
        this.mRoot.onUIInteractive(this, "move");
    }

    /* Access modifiers changed, original: protected */
    public void onActionUp() {
        this.mRoot.onUIInteractive(this, "up");
    }

    /* Access modifiers changed, original: protected */
    public void onActionCancel() {
    }

    private void handleCancel() {
        if (this.mTouchable && this.mPressed) {
            this.mPressed = false;
            performAction("cancel");
            onActionCancel();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChange(boolean visible) {
        super.onVisibilityChange(visible);
        if (!visible) {
            handleCancel();
        }
    }

    public void init() {
        super.init();
    }

    public void pause() {
        super.pause();
        handleCancel();
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        super.doTick(currentTime);
        if (this.mHasName) {
            this.mActualXVar.set(descale((double) getX()));
            this.mActualYVar.set(descale((double) getY()));
        }
        this.mAlpha = evaluateAlpha();
        int i = this.mAlpha;
        if (i < 0) {
            i = 0;
        }
        this.mAlpha = i;
    }

    /* Access modifiers changed, original: protected */
    public void onSetAnimBefore() {
        this.mAlphas = null;
        this.mPositions = null;
        this.mRotations = null;
        this.mSizes = null;
        this.mScales = null;
    }

    /* Access modifiers changed, original: protected */
    public void onSetAnimEnable(BaseAnimation ani) {
        if (ani instanceof AlphaAnimation) {
            this.mAlphas = (AlphaAnimation) ani;
        } else if (ani instanceof PositionAnimation) {
            this.mPositions = (PositionAnimation) ani;
        } else if (ani instanceof RotationAnimation) {
            this.mRotations = (RotationAnimation) ani;
        } else if (ani instanceof SizeAnimation) {
            this.mSizes = (SizeAnimation) ani;
        } else if (ani instanceof ScaleAnimation) {
            this.mScales = (ScaleAnimation) ani;
        }
    }

    /* Access modifiers changed, original: protected */
    public float getX() {
        return scale((double) getXRaw());
    }

    /* Access modifiers changed, original: protected */
    public float getXRaw() {
        Expression expression = this.mBaseXExpression;
        double x = expression != null ? expression.evaluate() : 0.0d;
        PositionAnimation positionAnimation = this.mPositions;
        if (positionAnimation != null) {
            x += positionAnimation.getX();
        }
        return (float) x;
    }

    /* Access modifiers changed, original: protected */
    public float getY() {
        return scale((double) getYRaw());
    }

    /* Access modifiers changed, original: protected */
    public float getYRaw() {
        Expression expression = this.mBaseYExpression;
        double y = expression != null ? expression.evaluate() : 0.0d;
        PositionAnimation positionAnimation = this.mPositions;
        if (positionAnimation != null) {
            y += positionAnimation.getY();
        }
        return (float) y;
    }

    public float getWidth() {
        return scale((double) getWidthRaw());
    }

    public float getWidthRaw() {
        SizeAnimation sizeAnimation = this.mSizes;
        if (sizeAnimation != null) {
            return (float) sizeAnimation.getWidth();
        }
        Expression expression = this.mWidthExpression;
        return (float) (expression != null ? expression.evaluate() : -1.0d);
    }

    public float getHeight() {
        return scale((double) getHeightRaw());
    }

    public float getHeightRaw() {
        SizeAnimation sizeAnimation = this.mSizes;
        if (sizeAnimation != null) {
            return (float) sizeAnimation.getHeight();
        }
        Expression expression = this.mHeightExpression;
        return (float) (expression != null ? expression.evaluate() : -1.0d);
    }

    /* Access modifiers changed, original: protected */
    public float getMaxWidth() {
        SizeAnimation sizeAnimation = this.mSizes;
        if (sizeAnimation != null) {
            return scale(sizeAnimation.getMaxWidth());
        }
        Expression expression = this.mWidthExpression;
        return scale(expression != null ? expression.evaluate() : -1.0d);
    }

    /* Access modifiers changed, original: protected */
    public float getMaxHeight() {
        SizeAnimation sizeAnimation = this.mSizes;
        if (sizeAnimation != null) {
            return scale(sizeAnimation.getMaxHeight());
        }
        Expression expression = this.mHeightExpression;
        return scale(expression != null ? expression.evaluate() : -1.0d);
    }

    /* Access modifiers changed, original: protected */
    public float getPivotX() {
        return scale((double) getPivotXRaw());
    }

    /* Access modifiers changed, original: protected */
    public float getPivotXRaw() {
        Expression expression = this.mPivotXExpression;
        return (float) (expression != null ? expression.evaluate() : 0.0d);
    }

    /* Access modifiers changed, original: protected */
    public float getPivotY() {
        return scale((double) getPivotYRaw());
    }

    /* Access modifiers changed, original: protected */
    public float getPivotYRaw() {
        Expression expression = this.mPivotYExpression;
        return (float) (expression != null ? expression.evaluate() : 0.0d);
    }

    /* Access modifiers changed, original: protected */
    public float getRotation() {
        Expression expression = this.mRotationExpression;
        double angle = expression != null ? expression.evaluate() : 0.0d;
        RotationAnimation rotationAnimation = this.mRotations;
        if (rotationAnimation != null) {
            return ((float) angle) + rotationAnimation.getAngle();
        }
        return (float) angle;
    }

    /* Access modifiers changed, original: protected */
    public float getRotationX() {
        Expression expression = this.mRotationX;
        return (float) (expression != null ? expression.evaluate() : 0.0d);
    }

    /* Access modifiers changed, original: protected */
    public float getRotationY() {
        Expression expression = this.mRotationY;
        return (float) (expression != null ? expression.evaluate() : 0.0d);
    }

    /* Access modifiers changed, original: protected */
    public float getRotationZ() {
        Expression expression = this.mRotationZ;
        return (float) (expression != null ? expression.evaluate() : 0.0d);
    }

    /* Access modifiers changed, original: protected */
    public float getScaleX() {
        double scale;
        Expression expression = this.mScaleExpression;
        if (expression != null) {
            scale = expression.evaluate();
        } else {
            expression = this.mScaleXExpression;
            scale = expression != null ? expression.evaluate() : 1.0d;
        }
        ScaleAnimation scaleAnimation = this.mScales;
        if (scaleAnimation != null) {
            return (float) (scaleAnimation.getScaleX() * scale);
        }
        return (float) scale;
    }

    /* Access modifiers changed, original: protected */
    public float getScaleY() {
        double scale;
        Expression expression = this.mScaleExpression;
        if (expression != null) {
            scale = expression.evaluate();
        } else {
            expression = this.mScaleYExpression;
            scale = expression != null ? expression.evaluate() : 1.0d;
        }
        ScaleAnimation scaleAnimation = this.mScales;
        if (scaleAnimation != null) {
            return (float) (scaleAnimation.getScaleY() * scale);
        }
        return (float) scale;
    }

    /* Access modifiers changed, original: protected */
    public int evaluateAlpha() {
        Expression expression = this.mAlphaExpression;
        int alpha1 = 255;
        int alpha = expression != null ? (int) expression.evaluate() : 255;
        AlphaAnimation alphaAnimation = this.mAlphas;
        if (alphaAnimation != null) {
            alpha1 = alphaAnimation.getAlpha();
        }
        alpha = Utils.mixAlpha(alpha, alpha1);
        return (this.mParent == null || (this.mParent instanceof LayerScreenElement) || ((this.mParent instanceof ElementGroup) && this.mParent.isLayered())) ? alpha : Utils.mixAlpha(alpha, this.mParent.getAlpha());
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public void setY(double y) {
        Expression expression = this.mBaseYExpression;
        if (expression == null || !(expression instanceof NumberExpression)) {
            this.mBaseYExpression = new NumberExpression(descale(y));
        } else {
            ((NumberExpression) expression).setValue(descale(y));
        }
    }

    public void setX(double x) {
        Expression expression = this.mBaseXExpression;
        if (expression == null || !(expression instanceof NumberExpression)) {
            this.mBaseXExpression = new NumberExpression(descale(x));
        } else {
            ((NumberExpression) expression).setValue(descale(x));
        }
    }

    public void setH(double h) {
        Expression expression = this.mHeightExpression;
        if (expression == null || !(expression instanceof NumberExpression)) {
            this.mHeightExpression = new NumberExpression(descale(h));
        } else {
            ((NumberExpression) expression).setValue(descale(h));
        }
    }

    public void setW(double w) {
        Expression expression = this.mWidthExpression;
        if (expression == null || !(expression instanceof NumberExpression)) {
            this.mWidthExpression = new NumberExpression(descale(w));
        } else {
            ((NumberExpression) expression).setValue(descale(w));
        }
    }

    public final float getMarginLeft() {
        return scale((double) this.mMarginLeft);
    }

    public final float getMarginRight() {
        return scale((double) this.mMarginRight);
    }

    public final float getMarginTop() {
        return scale((double) this.mMarginTop);
    }

    public final float getMarginBottom() {
        return scale((double) this.mMarginBottom);
    }
}
