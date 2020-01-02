package miui.maml.elements;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import miui.maml.util.Utils;
import org.apache.miui.commons.lang3.ClassUtils;
import org.w3c.dom.Element;

public class ImageNumberScreenElement extends ImageScreenElement {
    public static final String TAG_NAME = "ImageNumber";
    public static final String TAG_NAME1 = "ImageChars";
    private String LOG_TAG = "ImageNumberScreenElement";
    private int mBmpHeight;
    private int mBmpWidth;
    private Bitmap mCachedBmp;
    private Canvas mCachedCanvas;
    private ArrayList<CharName> mNameMap;
    private Expression mNumExpression;
    private String mOldSrc;
    private double mPreNumber = Double.MIN_VALUE;
    private String mPreStr;
    private int mSpace;
    private Expression mSpaceExpression;
    private Expression mStrExpression;
    private String mStrValue;

    private class CharName {
        public char ch;
        public String name;

        public CharName(char c, String n) {
            this.ch = c;
            this.name = n;
        }
    }

    public ImageNumberScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mNumExpression = Expression.build(getVariables(), getAttr(node, "number"));
        this.mStrExpression = Expression.build(getVariables(), getAttr(node, "string"));
        this.mSpaceExpression = Expression.build(getVariables(), getAttr(node, "space"));
        String tmp = getAttr(node, "charNameMap");
        if (!TextUtils.isEmpty(tmp)) {
            this.mNameMap = new ArrayList();
            for (String s : tmp.split(",")) {
                this.mNameMap.add(new CharName(s.charAt(0), s.substring(1)));
            }
        }
    }

    public void init() {
        super.init();
        Expression expression = this.mSpaceExpression;
        this.mSpace = expression == null ? 0 : (int) scale(expression.evaluate());
        this.mCurrentBitmap.setBitmap(this.mCachedBmp);
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        super.doTick(currentTime);
        Bitmap bitmap = null;
        if (this.mNumExpression == null && this.mStrExpression == null && this.mStrValue == null) {
            if (this.mCachedBmp != null) {
                this.mCachedBmp = null;
                this.mPreStr = null;
                this.mCurrentBitmap.setBitmap(null);
                updateBitmapVars();
            }
            return;
        }
        String numStr = null;
        String src = getSrc();
        boolean srcChanged = TextUtils.equals(src, this.mOldSrc) ^ 1;
        double number = this.mNumExpression;
        if (number != null) {
            number = evaluate(number);
            if (number != this.mPreNumber || srcChanged) {
                this.mPreNumber = number;
                numStr = Utils.doubleToString(number);
            } else {
                return;
            }
        } else if (!(this.mStrExpression == null && this.mStrValue == null)) {
            String str = this.mStrValue;
            if (str == null) {
                str = evaluateStr(this.mStrExpression);
            }
            numStr = str;
            if (!TextUtils.equals(numStr, this.mPreStr) || srcChanged) {
                this.mPreStr = numStr;
            } else {
                return;
            }
        }
        Bitmap bitmap2 = this.mCachedBmp;
        int i = 0;
        if (bitmap2 != null) {
            bitmap2.eraseColor(0);
        }
        this.mOldSrc = src;
        this.mBmpWidth = 0;
        int length = numStr != null ? numStr.length() : 0;
        int i2 = 0;
        while (i2 < length) {
            Bitmap bmp = getNumberBitmap(src, charToStr(numStr.charAt(i2)));
            if (bmp == null) {
                String str2 = this.LOG_TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Fail to get bitmap for number ");
                stringBuilder.append(String.valueOf(numStr.charAt(i2)));
                Log.e(str2, stringBuilder.toString());
                return;
            }
            Paint paint;
            int width = this.mBmpWidth + bmp.getWidth();
            int height = bmp.getHeight();
            int cachedBmpWidth = this.mCachedBmp;
            cachedBmpWidth = cachedBmpWidth == 0 ? i : cachedBmpWidth.getWidth();
            int cachedBmpHeight = this.mCachedBmp;
            cachedBmpHeight = cachedBmpHeight == 0 ? i : cachedBmpHeight.getHeight();
            if (width > cachedBmpWidth || height > cachedBmpHeight) {
                Bitmap oldBmp = this.mCachedBmp;
                if (width > cachedBmpWidth) {
                    int remains = length - i2;
                    width = (this.mBmpWidth + (bmp.getWidth() * remains)) + (this.mSpace * (remains - 1));
                } else {
                    width = cachedBmpWidth;
                }
                if (height <= cachedBmpHeight) {
                    height = cachedBmpHeight;
                }
                this.mBmpHeight = height;
                this.mCachedBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
                this.mCachedBmp.setDensity(bmp.getDensity());
                this.mCurrentBitmap.setBitmap(this.mCachedBmp);
                this.mCachedCanvas = new Canvas(this.mCachedBmp);
                if (oldBmp != null) {
                    paint = null;
                    this.mCachedCanvas.drawBitmap(oldBmp, 0.0f, 0.0f, null);
                } else {
                    paint = null;
                }
            } else {
                paint = bitmap;
            }
            this.mCachedCanvas.drawBitmap(bmp, (float) this.mBmpWidth, 0.0f, paint);
            this.mBmpWidth += bmp.getWidth();
            if (i2 < length - 1) {
                this.mBmpWidth += this.mSpace;
            }
            i2++;
            Object bitmap3 = paint;
            i = 0;
        }
        this.mCurrentBitmap.updateVersion();
        updateBitmapVars();
    }

    public void setValue(double d) {
        setValue(Utils.doubleToString(d));
    }

    public void setValue(String s) {
        this.mStrValue = s;
        requestUpdate();
    }

    /* Access modifiers changed, original: protected */
    public void updateBitmap(boolean sync) {
        this.mCurrentBitmap.setBitmap(this.mCachedBmp);
        updateBitmapVars();
    }

    public void finish() {
        super.finish();
        this.mPreNumber = Double.MIN_VALUE;
        this.mPreStr = null;
    }

    private String charToStr(char c) {
        ArrayList arrayList = this.mNameMap;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                CharName cn = (CharName) it.next();
                if (cn.ch == c) {
                    return cn.name;
                }
            }
        }
        if (c == ClassUtils.PACKAGE_SEPARATOR_CHAR) {
            return "dot";
        }
        return String.valueOf(c);
    }

    /* Access modifiers changed, original: protected */
    public int getBitmapWidth() {
        return this.mBmpWidth;
    }

    /* Access modifiers changed, original: protected */
    public int getBitmapHeight() {
        return this.mBmpHeight;
    }

    private Bitmap getNumberBitmap(String src, String c) {
        return getContext().mResourceManager.getBitmap(Utils.addFileNameSuffix(src, c));
    }
}
