package miui.maml.elements;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.miui.BiometricConnect;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import miui.graphics.BitmapFactory;
import miui.maml.ScreenElementRoot;
import miui.maml.animation.BaseAnimation;
import miui.maml.animation.SourcesAnimation;
import miui.maml.data.Expression;
import miui.maml.data.Expression.NumberExpression;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import miui.maml.elements.BitmapProvider.IBitmapHolder;
import miui.maml.elements.BitmapProvider.VersionedBitmap;
import miui.maml.util.TextFormatter;
import miui.maml.util.Utils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ImageScreenElement extends AnimatedScreenElement implements IBitmapHolder {
    private static final String LOG_TAG = "ImageScreenElement";
    public static final String MASK_TAG_NAME = "Mask";
    private static final double PI = 3.1415926535897d;
    public static final String TAG_NAME = "Image";
    private static final String VAR_BMP_HEIGHT = "bmp_height";
    private static final String VAR_BMP_WIDTH = "bmp_width";
    private static final String VAR_HAS_BITMAP = "has_bitmap";
    private boolean mAntiAlias;
    protected VersionedBitmap mBitmap = new VersionedBitmap(null);
    private BitmapProvider mBitmapProvider;
    private Bitmap mBlurBitmap;
    private int mBlurRadius;
    private IndexedVariable mBmpSizeHeightVar;
    private IndexedVariable mBmpSizeWidthVar;
    protected VersionedBitmap mCurrentBitmap = new VersionedBitmap(null);
    private Rect mDesRect = new Rect();
    private Expression mExpH;
    private Expression mExpSrcH;
    private Expression mExpSrcW;
    private Expression mExpSrcX;
    private Expression mExpSrcY;
    private Expression mExpW;
    private int mH = -1;
    private IndexedVariable mHasBitmapVar;
    private boolean mHasSrcRect;
    private boolean mHasWidthAndHeight;
    private boolean mLoadAsync;
    private Paint mMaskPaint = new Paint();
    private ArrayList<Mask> mMasks;
    private int mMeshHeight;
    private float[] mMeshVerts;
    private int mMeshWidth;
    protected Paint mPaint = new Paint();
    private boolean mPendingBlur;
    private int mRawBlurRadius;
    private boolean mRetainWhenInvisible;
    private pair<Double, Double> mRotateXYpair;
    private SourcesAnimation mSources;
    private String mSrc;
    private TextFormatter mSrcFormatter;
    private int mSrcH;
    private Expression mSrcIdExpression;
    private Rect mSrcRect;
    private int mSrcW;
    private int mSrcX;
    private int mSrcY;
    private int mW = -1;
    private Expression mXfermodeNumExp;

    private class Mask extends ImageScreenElement {
        private boolean mAlignAbsolute;

        public Mask(Element node, ScreenElementRoot root) {
            super(node, root);
            if (getAttr(node, "align").equalsIgnoreCase("absolute")) {
                this.mAlignAbsolute = true;
            }
        }

        public final boolean isAlignAbsolute() {
            return this.mAlignAbsolute;
        }

        /* Access modifiers changed, original: protected */
        public void doRender(Canvas c) {
        }
    }

    private static class pair<T1, T2> {
        public T1 p1;
        public T2 p2;

        private pair() {
        }
    }

    public ImageScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        load(node);
    }

    private void load(Element node) {
        if (node != null) {
            Variables vars = getVariables();
            this.mSrcFormatter = TextFormatter.fromElement(vars, node, "src", "srcFormat", "srcParas", "srcExp", "srcFormatExp");
            this.mSrcIdExpression = Expression.build(vars, getAttr(node, "srcid"));
            this.mAntiAlias = getAttr(node, "antiAlias").equals("false") ^ 1;
            this.mPaint.setFilterBitmap(this.mAntiAlias);
            this.mMaskPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
            this.mMaskPaint.setFilterBitmap(this.mAntiAlias);
            this.mExpSrcX = Expression.build(vars, getAttr(node, "srcX"));
            this.mExpSrcY = Expression.build(vars, getAttr(node, "srcY"));
            this.mExpSrcW = Expression.build(vars, getAttr(node, "srcW"));
            this.mExpSrcH = Expression.build(vars, getAttr(node, "srcH"));
            this.mExpW = Expression.build(vars, getAttr(node, BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_W));
            this.mExpH = Expression.build(vars, getAttr(node, BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_H));
            if (!(this.mExpSrcW == null || this.mExpSrcH == null)) {
                this.mHasSrcRect = true;
                this.mSrcRect = new Rect();
            }
            if (!(this.mExpH == null || this.mExpW == null)) {
                this.mHasWidthAndHeight = true;
            }
            this.mRawBlurRadius = getAttrAsInt(node, "blur", 0);
            loadMesh(node);
            this.mXfermodeNumExp = Expression.build(vars, getAttr(node, "xfermodeNum"));
            if (this.mXfermodeNumExp == null) {
                this.mPaint.setXfermode(new PorterDuffXfermode(Utils.getPorterDuffMode(getAttr(node, "xfermode"))));
            }
            setSrcType(Boolean.parseBoolean(getAttr(node, "useVirtualScreen")) ? "VirtualScreen" : getAttr(node, "srcType"));
            this.mLoadAsync = Boolean.parseBoolean(getAttr(node, "loadAsync"));
            this.mRetainWhenInvisible = Boolean.parseBoolean(getAttr(node, "retainWhenInvisible"));
            if (this.mHasName) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                String str = ".";
                stringBuilder.append(str);
                stringBuilder.append(VAR_BMP_WIDTH);
                this.mBmpSizeWidthVar = new IndexedVariable(stringBuilder.toString(), vars, true);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(str);
                stringBuilder.append(VAR_BMP_HEIGHT);
                this.mBmpSizeHeightVar = new IndexedVariable(stringBuilder.toString(), vars, true);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(str);
                stringBuilder.append(VAR_HAS_BITMAP);
                this.mHasBitmapVar = new IndexedVariable(stringBuilder.toString(), vars, true);
            }
            loadMask(node);
        }
    }

    /* Access modifiers changed, original: protected */
    public void loadMesh(Element node) {
        String str = LOG_TAG;
        String meshStr = getAttr(node, "mesh");
        int comma = meshStr.indexOf(",");
        if (comma != -1) {
            try {
                this.mMeshWidth = Integer.parseInt(meshStr.substring(0, comma));
                this.mMeshHeight = Integer.parseInt(meshStr.substring(comma + 1));
            } catch (NumberFormatException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid mesh format:");
                stringBuilder.append(meshStr);
                Log.w(str, stringBuilder.toString());
            }
            if (this.mMeshWidth != 0 && this.mMeshHeight != 0) {
                String meshArr = getAttr(node, "meshVertsArr");
                Object arr = getVariables().get(meshArr);
                if (arr == null || !(arr instanceof float[])) {
                    this.mMeshHeight = 0;
                    this.mMeshWidth = 0;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Invalid meshVertsArr:");
                    stringBuilder2.append(meshArr);
                    stringBuilder2.append("  undifined or not float[] type");
                    Log.w(str, stringBuilder2.toString());
                    return;
                }
                this.mMeshVerts = (float[]) arr;
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public BaseAnimation onCreateAnimation(String tag, Element ele) {
        if (!SourcesAnimation.TAG_NAME.equals(tag)) {
            return super.onCreateAnimation(tag, ele);
        }
        SourcesAnimation sourcesAnimation = new SourcesAnimation(ele, this);
        this.mSources = sourcesAnimation;
        return sourcesAnimation;
    }

    public void setSrcType(String srcType) {
        this.mBitmapProvider = BitmapProvider.create(this.mRoot, srcType);
    }

    private void loadMask(Element node) {
        if (this.mMasks == null) {
            this.mMasks = new ArrayList();
        }
        this.mMasks.clear();
        NodeList images = node.getElementsByTagName(MASK_TAG_NAME);
        for (int i = 0; i < images.getLength(); i++) {
            this.mMasks.add(new Mask((Element) images.item(i), this.mRoot));
        }
    }

    public void init() {
        super.init();
        TextFormatter textFormatter = this.mSrcFormatter;
        this.mSrc = textFormatter != null ? textFormatter.getText() : null;
        this.mBitmap.reset();
        ArrayList arrayList = this.mMasks;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((Mask) it.next()).init();
            }
        }
        BitmapProvider bitmapProvider = this.mBitmapProvider;
        if (bitmapProvider != null) {
            bitmapProvider.init(getSrc());
        }
        if (isVisible()) {
            updateBitmap(this.mLoadAsync);
        }
        this.mBlurRadius = (int) scale((double) this.mRawBlurRadius);
        if (this.mBlurRadius > 0) {
            this.mPendingBlur = true;
        }
    }

    /* Access modifiers changed, original: protected */
    public int getBitmapWidth() {
        Bitmap bmp = this.mCurrentBitmap.getBitmap();
        return bmp != null ? bmp.getWidth() : 0;
    }

    /* Access modifiers changed, original: protected */
    public int getBitmapHeight() {
        Bitmap bmp = this.mCurrentBitmap.getBitmap();
        return bmp != null ? bmp.getHeight() : 0;
    }

    public float getX() {
        float x = super.getX();
        SourcesAnimation sourcesAnimation = this.mSources;
        if (sourcesAnimation != null) {
            return x + scale(sourcesAnimation.getX());
        }
        return x;
    }

    public float getY() {
        float y = super.getY();
        SourcesAnimation sourcesAnimation = this.mSources;
        if (sourcesAnimation != null) {
            return y + scale(sourcesAnimation.getY());
        }
        return y;
    }

    public float getWidth() {
        float w = super.getWidth();
        if (w >= 0.0f) {
            return w;
        }
        if (this.mHasSrcRect) {
            return (float) this.mSrcW;
        }
        return (float) getBitmapWidth();
    }

    public float getHeight() {
        float h = super.getHeight();
        if (h >= 0.0f) {
            return h;
        }
        if (this.mHasSrcRect) {
            return (float) this.mSrcH;
        }
        return (float) getBitmapHeight();
    }

    public final String getSrc() {
        String src;
        SourcesAnimation sourcesAnimation = this.mSources;
        if (sourcesAnimation != null) {
            src = sourcesAnimation.getSrc();
        } else {
            src = this.mSrc;
        }
        if (src == null) {
            return src;
        }
        Expression expression = this.mSrcIdExpression;
        if (expression != null) {
            return Utils.addFileNameSuffix(src, String.valueOf((long) expression.evaluate()));
        }
        return src;
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
        Canvas canvas = c;
        Bitmap bmp = this.mCurrentBitmap.getBitmap();
        if (bmp != null) {
            Bitmap bmp2;
            if (this.mPendingBlur) {
                if (!(this.mBlurBitmap != null && bmp.getWidth() == this.mBlurBitmap.getWidth() && bmp.getHeight() == this.mBlurBitmap.getHeight())) {
                    this.mBlurBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Config.ARGB_8888);
                }
                this.mPendingBlur = false;
                this.mBlurBitmap = BitmapFactory.fastBlur(bmp, this.mBlurBitmap, this.mBlurRadius);
            }
            if (this.mBlurBitmap == null || this.mBlurRadius <= 0) {
                bmp2 = bmp;
            } else {
                bmp2 = this.mBlurBitmap;
            }
            int alpha = getAlpha();
            this.mPaint.setAlpha(alpha);
            int oldDensity = c.getDensity();
            canvas.setDensity(0);
            float width = getWidth();
            float height = getHeight();
            float aniWidth = super.getWidth();
            float aniHeight = super.getHeight();
            int i;
            if (width == 0.0f) {
            } else if (height == 0.0f) {
                i = alpha;
            } else {
                int x = (int) getLeft(0.0f, width);
                int y = (int) getTop(0.0f, height);
                c.save();
                int bufferHeight;
                Rect rect;
                int i2;
                int i3;
                if (this.mMasks.size() != 0) {
                    alpha = x;
                    x = y;
                    float maxWidth = getMaxWidth();
                    float maxHeight = getMaxHeight();
                    float maxWidth2 = Math.max(maxWidth, width);
                    float maxHeight2 = Math.max(maxHeight, height);
                    int bufferWidth = (int) Math.ceil((double) maxWidth2);
                    bufferHeight = (int) Math.ceil((double) maxHeight2);
                    float f = (float) (x + bufferHeight);
                    float f2 = (float) (alpha + bufferWidth);
                    float f3 = f2;
                    float f4 = f;
                    c.saveLayer((float) alpha, (float) x, f3, f4, this.mPaint, 31);
                    if (aniWidth > 0.0f || aniHeight > 0.0f || this.mSrcRect != null) {
                        this.mDesRect.set(alpha, x, ((int) width) + alpha, x + ((int) height));
                        rect = this.mSrcRect;
                        if (rect != null) {
                            i2 = this.mSrcX;
                            i3 = this.mSrcY;
                            rect.set(i2, i3, this.mSrcW + i2, this.mSrcH + i3);
                        }
                        canvas.drawBitmap(bmp2, this.mSrcRect, this.mDesRect, this.mPaint);
                    } else {
                        canvas.drawBitmap(bmp2, (float) alpha, (float) x, this.mPaint);
                    }
                    Iterator it = this.mMasks.iterator();
                    while (it.hasNext()) {
                        renderWithMask(canvas, (Mask) it.next(), alpha, x);
                    }
                    c.restore();
                } else if (bmp2.getNinePatchChunk() != null) {
                    NinePatch np = getContext().mResourceManager.getNinePatch(getSrc());
                    if (np != null) {
                        this.mDesRect.set(x, y, (int) (((float) x) + width), (int) (((float) y) + height));
                        np.draw(canvas, this.mDesRect, this.mPaint);
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("the image contains ninepatch chunk but couldn't get NinePatch object: ");
                        stringBuilder.append(getSrc());
                        Log.e(LOG_TAG, stringBuilder.toString());
                    }
                    i = alpha;
                    alpha = x;
                    x = y;
                } else {
                    if (aniWidth > 0.0f || aniHeight > 0.0f) {
                        alpha = x;
                        x = y;
                    } else if (this.mSrcRect != null) {
                        i = alpha;
                        alpha = x;
                        x = y;
                    } else {
                        i3 = this.mMeshWidth;
                        if (i3 > 0) {
                            bufferHeight = this.mMeshHeight;
                            if (bufferHeight > 0) {
                                int y2 = y;
                                alpha = x;
                                c.drawBitmapMesh(bmp2, i3, bufferHeight, this.mMeshVerts, 0, null, 0, this.mPaint);
                                x = y2;
                            }
                        }
                        i = alpha;
                        canvas.drawBitmap(bmp2, (float) x, (float) y, this.mPaint);
                    }
                    this.mDesRect.set(alpha, x, (int) (((float) alpha) + width), (int) (((float) x) + height));
                    rect = this.mSrcRect;
                    if (rect != null) {
                        i2 = this.mSrcX;
                        i3 = this.mSrcY;
                        rect.set(i2, i3, this.mSrcW + i2, this.mSrcH + i3);
                    }
                    canvas.drawBitmap(bmp2, this.mSrcRect, this.mDesRect, this.mPaint);
                }
                c.restore();
                canvas.setDensity(oldDensity);
            }
        }
    }

    public void setBitmap(Bitmap bmp) {
        if (bmp != this.mBitmap.getBitmap()) {
            this.mBitmap.setBitmap(bmp);
            updateBitmap(this.mLoadAsync);
            requestUpdate();
        }
    }

    /* Access modifiers changed, original: protected */
    public VersionedBitmap getBitmap(boolean async) {
        if (this.mBitmap.getBitmap() != null) {
            return this.mBitmap;
        }
        BitmapProvider bitmapProvider = this.mBitmapProvider;
        if (bitmapProvider != null) {
            return bitmapProvider.getBitmap(getSrc(), async ^ 1, this.mW, this.mH);
        }
        return null;
    }

    public void setSrc(String src) {
        TextFormatter textFormatter = this.mSrcFormatter;
        if (textFormatter != null) {
            textFormatter.setText(src);
        }
    }

    public void setSrcId(double srcId) {
        Expression expression = this.mSrcIdExpression;
        if (expression == null || !(expression instanceof NumberExpression)) {
            this.mSrcIdExpression = new NumberExpression(String.valueOf(srcId));
        } else {
            ((NumberExpression) expression).setValue(srcId);
        }
    }

    private void rotateXY(double centerX, double centerY, double angle, pair<Double, Double> pr) {
        double cm = Math.sqrt((centerX * centerX) + (centerY * centerY));
        Double valueOf = Double.valueOf(0.0d);
        if (cm > 0.0d) {
            double angle2 = (PI - Math.acos(centerX / cm)) - angle;
            pr.p1 = Double.valueOf((Math.cos(angle2) * cm) + centerX);
            pr.p2 = Double.valueOf(centerY - (Math.sin(angle2) * cm));
            return;
        }
        pr.p1 = valueOf;
        pr.p2 = valueOf;
    }

    private void renderWithMask(Canvas bufferCanvas, Mask mask, int x, int y) {
        Canvas canvas = bufferCanvas;
        int i = x;
        int i2 = y;
        Bitmap rawMask = getContext().mResourceManager.getBitmap(mask.getSrc());
        if (rawMask != null) {
            bufferCanvas.save();
            double maskX = (double) mask.getX();
            double maskY = (double) mask.getY();
            float maskAngle = mask.getRotation();
            double maskY2;
            if (mask.isAlignAbsolute()) {
                float angle = getRotation();
                if (angle == 0.0f) {
                    maskX -= (double) i;
                    maskY -= (double) i2;
                    float f = angle;
                } else {
                    float maskAngle2 = maskAngle - angle;
                    double angleA = (((double) angle) * PI) / 180.0d;
                    float cx = getPivotX();
                    float cy = getPivotY();
                    if (this.mRotateXYpair == null) {
                        this.mRotateXYpair = new pair();
                    }
                    maskY2 = maskY;
                    rotateXY((double) cx, (double) cy, angleA, this.mRotateXYpair);
                    double rx = ((double) i) + ((Double) this.mRotateXYpair.p1).doubleValue();
                    double ry = ((double) i2) + ((Double) this.mRotateXYpair.p2).doubleValue();
                    rotateXY(descale((double) mask.getPivotX()), descale((double) mask.getPivotY()), (((double) mask.getRotation()) * PI) / 180.0d, this.mRotateXYpair);
                    double dx = (maskX + ((double) scale(((Double) this.mRotateXYpair.p1).doubleValue()))) - rx;
                    double dy = (maskY2 + ((double) scale(((Double) this.mRotateXYpair.p2).doubleValue()))) - ry;
                    double dm = Math.sqrt((dx * dx) + (dy * dy));
                    maskY2 = Math.asin(dx / dm);
                    double angleC = dy > 0.0d ? angleA + maskY2 : (angleA + PI) - maskY2;
                    maskX = dm * Math.sin(angleC);
                    maskY = dm * Math.cos(angleC);
                    maskAngle = maskAngle2;
                }
                maskX -= (double) getX();
                maskY -= (double) getY();
            } else {
                maskY2 = maskY;
            }
            canvas.rotate(maskAngle, (float) ((((double) mask.getPivotX()) + maskX) + ((double) i)), (float) ((((double) mask.getPivotY()) + maskY) + ((double) i2)));
            int mx = (int) maskX;
            int my = (int) maskY;
            int width = Math.round(mask.getWidth());
            if (width < 0) {
                width = rawMask.getWidth();
            }
            int height = Math.round(mask.getHeight());
            if (height < 0) {
                height = rawMask.getHeight();
            }
            this.mDesRect.set(mx + i, my + i2, (mx + i) + width, (my + i2) + height);
            this.mMaskPaint.setAlpha(mask.getAlpha());
            canvas.drawBitmap(rawMask, null, this.mDesRect, this.mMaskPaint);
            bufferCanvas.restore();
        }
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        super.doTick(currentTime);
        if (isVisible()) {
            TextFormatter textFormatter = this.mSrcFormatter;
            this.mSrc = textFormatter != null ? textFormatter.getText() : null;
            ArrayList arrayList = this.mMasks;
            if (arrayList != null) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    ((Mask) it.next()).doTick(currentTime);
                }
            }
            Expression expression = this.mXfermodeNumExp;
            if (expression != null) {
                this.mPaint.setXfermode(new PorterDuffXfermode(Utils.getPorterDuffMode((int) expression.evaluate())));
            }
            if (this.mHasSrcRect) {
                this.mSrcX = (int) scale(evaluate(this.mExpSrcX));
                this.mSrcY = (int) scale(evaluate(this.mExpSrcY));
                this.mSrcW = (int) scale(evaluate(this.mExpSrcW));
                this.mSrcH = (int) scale(evaluate(this.mExpSrcH));
            }
            if (this.mHasWidthAndHeight) {
                this.mW = (int) scale(evaluate(this.mExpW));
                this.mH = (int) scale(evaluate(this.mExpH));
            }
            updateBitmap(this.mLoadAsync);
        }
    }

    public void finish() {
        super.finish();
        BitmapProvider bitmapProvider = this.mBitmapProvider;
        if (bitmapProvider != null) {
            bitmapProvider.finish();
        }
        ArrayList arrayList = this.mMasks;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((Mask) it.next()).finish();
            }
        }
        this.mBitmap.reset();
        this.mCurrentBitmap.reset();
        this.mBlurBitmap = null;
    }

    public void pause() {
        super.pause();
        ArrayList arrayList = this.mMasks;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((Mask) it.next()).pause();
            }
        }
    }

    public void resume() {
        super.resume();
        ArrayList arrayList = this.mMasks;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((Mask) it.next()).resume();
            }
        }
    }

    public void reset(long time) {
        super.reset(time);
        ArrayList arrayList = this.mMasks;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((Mask) it.next()).reset(time);
            }
        }
        BitmapProvider bitmapProvider = this.mBitmapProvider;
        if (bitmapProvider != null) {
            bitmapProvider.reset();
        }
        if (this.mBlurRadius > 0) {
            this.mPendingBlur = true;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSetAnimBefore() {
        super.onSetAnimBefore();
        this.mSources = null;
    }

    /* Access modifiers changed, original: protected */
    public void onSetAnimEnable(BaseAnimation ani) {
        if (ani instanceof SourcesAnimation) {
            this.mSources = (SourcesAnimation) ani;
        } else {
            super.onSetAnimEnable(ani);
        }
    }

    /* Access modifiers changed, original: protected */
    public void playAnim(long time, long startTime, long endTime, boolean isLoop, boolean isDelay) {
        super.playAnim(time, startTime, endTime, isLoop, isDelay);
        ArrayList arrayList = this.mMasks;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((Mask) it.next()).playAnim(time, startTime, endTime, isLoop, isDelay);
            }
        }
        BitmapProvider bitmapProvider = this.mBitmapProvider;
        if (bitmapProvider != null) {
            bitmapProvider.reset();
        }
    }

    /* Access modifiers changed, original: protected */
    public void pauseAnim(long time) {
        super.pauseAnim(time);
        ArrayList arrayList = this.mMasks;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((Mask) it.next()).pauseAnim(time);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void resumeAnim(long time) {
        super.resumeAnim(time);
        ArrayList arrayList = this.mMasks;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((Mask) it.next()).resumeAnim(time);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChange(boolean visible) {
        super.onVisibilityChange(visible);
        if (visible) {
            updateBitmap(this.mLoadAsync);
        } else if (!this.mRetainWhenInvisible) {
            BitmapProvider bitmapProvider = this.mBitmapProvider;
            if (bitmapProvider != null) {
                bitmapProvider.finish();
            }
            this.mCurrentBitmap.reset();
        }
    }

    /* Access modifiers changed, original: protected */
    public void updateBitmap(boolean sync) {
        VersionedBitmap versionedBmp = getBitmap(sync);
        if (this.mBlurRadius > 0 && !VersionedBitmap.equals(versionedBmp, this.mCurrentBitmap)) {
            this.mPendingBlur = true;
        }
        this.mCurrentBitmap.set(versionedBmp);
        updateBitmapVars();
    }

    /* Access modifiers changed, original: protected */
    public void updateBitmapVars() {
        if (this.mHasName) {
            this.mBmpSizeWidthVar.set(descale((double) getBitmapWidth()));
            this.mBmpSizeHeightVar.set(descale((double) getBitmapHeight()));
            this.mHasBitmapVar.set(this.mCurrentBitmap.getBitmap() != null ? 1.0d : 0.0d);
        }
    }

    public VersionedBitmap getBitmap(String id) {
        return this.mCurrentBitmap;
    }
}
