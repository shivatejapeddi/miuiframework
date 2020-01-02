package miui.maml.elements;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.media.tv.TvContract.PreviewPrograms;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import miui.maml.data.VariableNames;
import miui.maml.data.Variables;
import miui.maml.util.ColorParser;
import miui.maml.util.Utils;
import org.w3c.dom.Element;

public class PaintScreenElement extends AnimatedScreenElement {
    private static float DEFAULT_WEIGHT = 1.0f;
    public static final String TAG_NAME = "Paint";
    private Bitmap mCachedBitmap;
    private Canvas mCachedCanvas;
    private Paint mCachedPaint;
    private int mColor;
    private ColorParser mColorParser;
    private Paint mPaint;
    private Path mPath = new Path();
    private boolean mPendingMouseUp;
    private float mWeight;
    private Expression mWeightExp;
    private Xfermode mXfermode;

    public PaintScreenElement(Element ele, ScreenElementRoot root) {
        super(ele, root);
        load(ele, root);
        DEFAULT_WEIGHT = scale((double) DEFAULT_WEIGHT);
        this.mWeight = DEFAULT_WEIGHT;
        this.mPaint = new Paint();
        this.mPaint.setXfermode(this.mXfermode);
        this.mPaint.setAntiAlias(true);
        this.mCachedPaint = new Paint();
        this.mCachedPaint.setStyle(Style.STROKE);
        this.mCachedPaint.setStrokeWidth(DEFAULT_WEIGHT);
        this.mCachedPaint.setStrokeCap(Cap.ROUND);
        this.mCachedPaint.setStrokeJoin(Join.ROUND);
        this.mCachedPaint.setAntiAlias(true);
        this.mTouchable = true;
    }

    private void load(Element node, ScreenElementRoot root) {
        if (node != null) {
            Variables vars = getVariables();
            this.mWeightExp = Expression.build(vars, node.getAttribute(PreviewPrograms.COLUMN_WEIGHT));
            this.mColorParser = ColorParser.fromElement(vars, node);
            this.mXfermode = new PorterDuffXfermode(Utils.getPorterDuffMode(node.getAttribute("xfermode")));
        }
    }

    public void init() {
        super.init();
        float width = getWidth();
        if (width < 0.0f) {
            width = scale(Utils.getVariableNumber(VariableNames.SCREEN_WIDTH, getVariables()));
        }
        float height = getHeight();
        if (height < 0.0f) {
            height = scale(Utils.getVariableNumber(VariableNames.SCREEN_HEIGHT, getVariables()));
        }
        this.mCachedBitmap = Bitmap.createBitmap((int) Math.ceil((double) width), (int) Math.ceil((double) height), Config.ARGB_8888);
        this.mCachedBitmap.setDensity(this.mRoot.getTargetDensity());
        this.mCachedCanvas = new Canvas(this.mCachedBitmap);
    }

    public void reset(long time) {
        super.reset(time);
        this.mCachedCanvas.drawColor(0, Mode.CLEAR);
        this.mPressed = false;
    }

    public void finish() {
        super.finish();
        this.mCachedBitmap.recycle();
        this.mCachedBitmap = null;
        this.mCachedCanvas = null;
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
        float w = getWidth();
        float h = getHeight();
        float x = getLeft(0.0f, w);
        float y = getTop(0.0f, h);
        float absoluteLeft = getAbsoluteLeft();
        float absoluteTop = getAbsoluteTop();
        if (this.mPendingMouseUp) {
            this.mCachedCanvas.save();
            this.mCachedCanvas.translate(-absoluteLeft, -absoluteTop);
            this.mCachedCanvas.drawPath(this.mPath, this.mCachedPaint);
            this.mCachedCanvas.restore();
            this.mPath.reset();
            this.mPendingMouseUp = false;
        }
        c.drawBitmap(this.mCachedBitmap, x, y, this.mPaint);
        if (this.mPressed && this.mWeight > 0.0f && this.mAlpha > 0) {
            this.mCachedPaint.setStrokeWidth(this.mWeight);
            this.mCachedPaint.setColor(this.mColor);
            Paint paint = this.mCachedPaint;
            paint.setAlpha(Utils.mixAlpha(paint.getAlpha(), this.mAlpha));
            c.save();
            c.translate((-absoluteLeft) + x, (-absoluteTop) + y);
            Xfermode oldXfermode = this.mCachedPaint.getXfermode();
            this.mCachedPaint.setXfermode(this.mXfermode);
            c.drawPath(this.mPath, this.mCachedPaint);
            this.mCachedPaint.setXfermode(oldXfermode);
            c.restore();
        }
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        super.doTick(currentTime);
        if (isVisible()) {
            Expression expression = this.mWeightExp;
            if (expression != null) {
                this.mWeight = scale(expression.evaluate());
            }
            this.mColor = this.mColorParser.getColor();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onActionDown(float x, float y) {
        super.onActionDown(x, y);
        this.mPath.reset();
        this.mPath.moveTo(x, y);
    }

    /* Access modifiers changed, original: protected */
    public void onActionMove(float x, float y) {
        super.onActionMove(x, y);
        this.mPath.lineTo(x, y);
    }

    /* Access modifiers changed, original: protected */
    public void onActionUp() {
        super.onActionUp();
        this.mPendingMouseUp = true;
    }

    /* Access modifiers changed, original: protected */
    public void onActionCancel() {
        this.mPendingMouseUp = true;
    }
}
