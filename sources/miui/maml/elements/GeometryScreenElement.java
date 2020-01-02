package miui.maml.elements;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PorterDuffXfermode;
import android.media.tv.TvContract.PreviewPrograms;
import android.text.TextUtils;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import miui.maml.data.Variables;
import miui.maml.shader.ShadersElement;
import miui.maml.util.ColorParser;
import miui.maml.util.Utils;
import org.w3c.dom.Element;

public abstract class GeometryScreenElement extends AnimatedScreenElement {
    private static final String LOG_TAG = "GeometryScreenElement";
    private int mFillColor;
    protected ColorParser mFillColorParser;
    protected ShadersElement mFillShadersElement;
    protected Paint mPaint = new Paint();
    private final DrawMode mStrokeAlign;
    private int mStrokeColor;
    protected ColorParser mStrokeColorParser;
    protected ShadersElement mStrokeShadersElement;
    protected float mWeight = scale(1.0d);
    protected Expression mWeightExp;
    protected Expression mXfermodeNumExp;

    protected enum DrawMode {
        STROKE_CENTER,
        STROKE_OUTER,
        STROKE_INNER,
        FILL;

        public static DrawMode getStrokeAlign(String strMode) {
            if ("inner".equalsIgnoreCase(strMode)) {
                return STROKE_INNER;
            }
            if ("center".equalsIgnoreCase(strMode)) {
                return STROKE_CENTER;
            }
            return STROKE_OUTER;
        }
    }

    public abstract void onDraw(Canvas canvas, DrawMode drawMode);

    public GeometryScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        String color = getAttr(node, "strokeColor");
        Variables vars = getVariables();
        if (!TextUtils.isEmpty(color)) {
            this.mStrokeColorParser = new ColorParser(vars, color);
        }
        color = getAttr(node, "fillColor");
        if (!TextUtils.isEmpty(color)) {
            this.mFillColorParser = new ColorParser(vars, color);
        }
        this.mWeightExp = Expression.build(vars, getAttr(node, PreviewPrograms.COLUMN_WEIGHT));
        this.mPaint.setStrokeCap(getCap(getAttr(node, "cap")));
        float[] dashIntervals = resolveDashIntervals(node);
        if (dashIntervals != null) {
            this.mPaint.setPathEffect(new DashPathEffect(dashIntervals, 0.0f));
        }
        this.mStrokeAlign = DrawMode.getStrokeAlign(getAttr(node, "strokeAlign"));
        this.mXfermodeNumExp = Expression.build(vars, getAttr(node, "xfermodeNum"));
        if (this.mXfermodeNumExp == null) {
            this.mPaint.setXfermode(new PorterDuffXfermode(Utils.getPorterDuffMode(getAttr(node, "xfermode"))));
        }
        this.mPaint.setAntiAlias(true);
        loadShadersElement(node, root);
    }

    private final Cap getCap(String strCap) {
        Cap cap = Cap.BUTT;
        if (TextUtils.isEmpty(strCap)) {
            return cap;
        }
        if (strCap.equalsIgnoreCase("round")) {
            cap = Cap.ROUND;
        } else if (strCap.equalsIgnoreCase("square")) {
            cap = Cap.SQUARE;
        }
        return cap;
    }

    private float[] resolveDashIntervals(Element node) {
        String strDash = getAttr(node, "dash");
        if (TextUtils.isEmpty(strDash)) {
            return null;
        }
        String[] intervals = strDash.split(",");
        if (intervals.length < 2 || intervals.length % 2 != 0) {
            return null;
        }
        float[] dashIntervals = new float[intervals.length];
        int i = 0;
        while (i < intervals.length) {
            try {
                dashIntervals[i] = Float.parseFloat(intervals[i]);
                i++;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return dashIntervals;
    }

    private void loadShadersElement(Element node, ScreenElementRoot root) {
        Element ele = Utils.getChild(node, ShadersElement.STROKE_TAG_NAME);
        if (ele != null) {
            this.mStrokeShadersElement = new ShadersElement(ele, root);
        }
        ele = Utils.getChild(node, ShadersElement.FILL_TAG_NAME);
        if (ele != null) {
            this.mFillShadersElement = new ShadersElement(ele, root);
        }
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
        ShadersElement shadersElement;
        Paint paint;
        if (!(this.mFillShadersElement == null && this.mFillColorParser == null)) {
            this.mPaint.setStyle(Style.FILL);
            shadersElement = this.mFillShadersElement;
            if (shadersElement != null) {
                this.mPaint.setShader(shadersElement.getShader());
                this.mPaint.setAlpha(this.mAlpha);
            } else {
                this.mPaint.setShader(null);
                this.mPaint.setColor(this.mFillColor);
                paint = this.mPaint;
                paint.setAlpha(Utils.mixAlpha(paint.getAlpha(), this.mAlpha));
            }
            onDraw(c, DrawMode.FILL);
        }
        if (this.mWeight <= 0.0f) {
            return;
        }
        if (this.mStrokeShadersElement != null || this.mStrokeColorParser != null) {
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setStrokeWidth(this.mWeight);
            shadersElement = this.mStrokeShadersElement;
            if (shadersElement != null) {
                this.mPaint.setShader(shadersElement.getShader());
                this.mPaint.setAlpha(this.mAlpha);
            } else {
                this.mPaint.setShader(null);
                this.mPaint.setColor(this.mStrokeColor);
                paint = this.mPaint;
                paint.setAlpha(Utils.mixAlpha(paint.getAlpha(), this.mAlpha));
            }
            onDraw(c, this.mStrokeAlign);
        }
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        super.doTick(currentTime);
        if (isVisible()) {
            ColorParser colorParser = this.mStrokeColorParser;
            if (colorParser != null) {
                this.mStrokeColor = colorParser.getColor();
            }
            colorParser = this.mFillColorParser;
            if (colorParser != null) {
                this.mFillColor = colorParser.getColor();
            }
            ShadersElement shadersElement = this.mStrokeShadersElement;
            if (shadersElement != null) {
                shadersElement.updateShader();
            }
            shadersElement = this.mFillShadersElement;
            if (shadersElement != null) {
                shadersElement.updateShader();
            }
            Expression expression = this.mWeightExp;
            if (expression != null) {
                this.mWeight = scale(expression.evaluate());
            }
            expression = this.mXfermodeNumExp;
            if (expression != null) {
                this.mPaint.setXfermode(new PorterDuffXfermode(Utils.getPorterDuffMode((int) expression.evaluate())));
            }
        }
    }
}
