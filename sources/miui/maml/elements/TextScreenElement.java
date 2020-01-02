package miui.maml.elements;

import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.wifi.WifiEnterpriseConfig;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.app.DumpHeapActivity;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import miui.maml.util.ColorParser;
import miui.maml.util.TextFormatter;
import miui.maml.util.Utils;
import miui.util.TypefaceUtils;
import org.w3c.dom.Element;

public class TextScreenElement extends AnimatedScreenElement {
    private static final String CRLF = "\n";
    private static final int DEFAULT_SIZE = 18;
    private static final String LOG_TAG = "TextScreenElement";
    private static final int MARQUEE_FRAMERATE = 45;
    private static final int PADDING = 50;
    private static final String RAW_CRLF = "\\n";
    public static final String TAG_NAME = "Text";
    public static final String TEXT_HEIGHT = "text_height";
    public static final String TEXT_WIDTH = "text_width";
    private static final Object mLock = new Object();
    private ColorParser mColorParser;
    private boolean mFontScaleEnabled;
    protected TextFormatter mFormatter;
    private float mLayoutWidth;
    private int mMarqueeGap;
    private float mMarqueePos = Float.MAX_VALUE;
    private int mMarqueeSpeed;
    private boolean mMultiLine;
    private TextPaint mPaint = new TextPaint();
    private long mPreviousTime;
    private String mSetText;
    private ColorParser mShadowColorParser;
    private float mShadowDx;
    private float mShadowDy;
    private float mShadowRadius;
    private boolean mShouldMarquee;
    private Expression mSizeExpression;
    private float mSpacingAdd;
    private float mSpacingMult;
    private String mText;
    private float mTextHeight;
    private IndexedVariable mTextHeightVar;
    private StaticLayout mTextLayout;
    private float mTextSize = scale(18.0d);
    private float mTextWidth;
    private IndexedVariable mTextWidthVar;

    /* renamed from: miui.maml.elements.TextScreenElement$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$elements$ScreenElement$Align = new int[Align.values().length];

        static {
            try {
                $SwitchMap$miui$maml$elements$ScreenElement$Align[Align.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$elements$ScreenElement$Align[Align.CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$elements$ScreenElement$Align[Align.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public TextScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        load(node);
    }

    private void load(Element node) {
        if (node != null) {
            String str;
            Variables vars = getVariables();
            if (this.mHasName) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                str = ".";
                stringBuilder.append(str);
                stringBuilder.append(TEXT_WIDTH);
                this.mTextWidthVar = new IndexedVariable(stringBuilder.toString(), vars, true);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(str);
                stringBuilder.append(TEXT_HEIGHT);
                this.mTextHeightVar = new IndexedVariable(stringBuilder.toString(), vars, true);
            }
            this.mFormatter = TextFormatter.fromElement(vars, node, this.mStyle);
            this.mColorParser = ColorParser.fromElement(vars, node, this.mStyle);
            this.mSizeExpression = Expression.build(vars, getAttr(node, DumpHeapActivity.KEY_SIZE));
            this.mMarqueeSpeed = getAttrAsInt(node, "marqueeSpeed", 0);
            this.mSpacingMult = getAttrAsFloat(node, "spacingMult", 1.0f);
            this.mSpacingAdd = getAttrAsFloat(node, "spacingAdd", 0.0f);
            this.mMarqueeGap = getAttrAsInt(node, "marqueeGap", 2);
            this.mMultiLine = Boolean.parseBoolean(getAttr(node, "multiLine"));
            this.mFontScaleEnabled = Boolean.parseBoolean(getAttr(node, "enableFontScale"));
            String fontFamily = getAttr(node, "fontFamily");
            str = getAttr(node, "fontPath");
            if (!TextUtils.isEmpty(fontFamily)) {
                this.mPaint.setTypeface(Typeface.create(fontFamily, parseFontStyle(getAttr(node, "fontStyle"))));
            } else if (TextUtils.isEmpty(str)) {
                this.mPaint.setFakeBoldText(Boolean.parseBoolean(getAttr(node, "bold")));
                this.mPaint.setTypeface(TypefaceUtils.replaceTypeface(getContext().mContext, this.mPaint.getTypeface()));
            } else {
                Typeface typeface = null;
                try {
                    typeface = Typeface.createFromAsset(getContext().mContext.getAssets(), str);
                } catch (Exception e) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("create typeface from asset fail :");
                    stringBuilder2.append(e);
                    Log.e(LOG_TAG, stringBuilder2.toString());
                }
                if (typeface != null) {
                    this.mPaint.setTypeface(typeface);
                }
            }
            this.mPaint.setColor(getColor());
            this.mPaint.setTextSize(scale(18.0d));
            this.mPaint.setAntiAlias(true);
            this.mShadowRadius = getAttrAsFloat(node, "shadowRadius", 0.0f);
            this.mShadowDx = getAttrAsFloat(node, "shadowDx", 0.0f);
            this.mShadowDy = getAttrAsFloat(node, "shadowDy", 0.0f);
            this.mShadowColorParser = ColorParser.fromElement(vars, node, "shadowColor", this.mStyle);
            this.mPaint.setShadowLayer(this.mShadowRadius, this.mShadowDx, this.mShadowDy, getShadowColor());
        }
    }

    private static int parseFontStyle(String s) {
        if (TextUtils.isEmpty(s) || "normal".equals(s)) {
            return 0;
        }
        if ("bold".equals(s)) {
            return 1;
        }
        if ("italic".equals(s)) {
            return 2;
        }
        if ("bold_italic".equals(s)) {
            return 3;
        }
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
        Canvas canvas = c;
        if (!TextUtils.isEmpty(this.mText)) {
            float width;
            float height;
            float extendLeft;
            float extendRight;
            float extendBottom;
            this.mPaint.setColor(getColor());
            TextPaint textPaint = this.mPaint;
            textPaint.setAlpha(Utils.mixAlpha(textPaint.getAlpha(), getAlpha()));
            this.mPaint.setShadowLayer(this.mShadowRadius, this.mShadowDx, this.mShadowDy, getShadowColor());
            float width2 = getWidth();
            boolean specifyWidth = width2 >= 0.0f;
            if (width2 < 0.0f || width2 > this.mTextWidth) {
                width = this.mTextWidth;
            } else {
                width = width2;
            }
            width2 = getHeight();
            float lineHeight = this.mPaint.getTextSize();
            if (width2 < 0.0f) {
                height = this.mTextHeight;
            } else {
                height = width2;
            }
            float x = getLeft(0.0f, width);
            float y = getTop(0.0f, height);
            c.save();
            float f = this.mShadowRadius;
            if (f != 0.0f) {
                extendLeft = Math.min(0.0f, this.mShadowDx - f);
                extendRight = Math.max(0.0f, this.mShadowDx + this.mShadowRadius);
                f = Math.min(0.0f, this.mShadowDy - this.mShadowRadius);
                extendBottom = Math.max(0.0f, this.mShadowDy + this.mShadowRadius);
            } else {
                extendLeft = 0.0f;
                extendRight = 0.0f;
                f = 0.0f;
                extendBottom = 0.0f;
            }
            canvas.translate(x, y);
            canvas.clipRect(specifyWidth ? 0.0f : extendLeft, f, (specifyWidth ? 0.0f : extendRight) + width, height + extendBottom);
            StaticLayout staticLayout = this.mTextLayout;
            if (staticLayout != null) {
                int count = staticLayout.getLineCount();
                if (count == 1 && this.mShouldMarquee) {
                    int start = this.mTextLayout.getLineStart(0);
                    int end = this.mTextLayout.getLineEnd(0);
                    int top = this.mTextLayout.getLineTop(0);
                    float left = this.mTextLayout.getLineLeft(0);
                    int top2 = top;
                    float f2 = lineHeight + ((float) top);
                    c.drawText(this.mText, start, end, left + this.mMarqueePos, f2, this.mPaint);
                    width2 = this.mMarqueePos;
                    if (width2 != 0.0f) {
                        width2 = (width2 + this.mTextWidth) + (this.mTextSize * ((float) this.mMarqueeGap));
                        if (width2 < width) {
                            canvas.drawText(this.mText, left + width2, ((float) top2) + lineHeight, this.mPaint);
                        }
                    }
                } else {
                    float f3 = f;
                    this.mTextLayout.draw(canvas);
                }
            }
            c.restore();
        }
    }

    public void init() {
        super.init();
    }

    public void finish() {
        super.finish();
        this.mText = null;
        this.mSetText = null;
        this.mMarqueePos = Float.MAX_VALUE;
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00c9  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x007a  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x007a  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x007b  */
    /* JADX WARNING: Missing block: B:56:0x0104, code skipped:
            if (r1.mShouldMarquee == false) goto L_0x0109;
     */
    /* JADX WARNING: Missing block: B:57:0x0106, code skipped:
            r0 = 45.0f;
     */
    /* JADX WARNING: Missing block: B:58:0x0109, code skipped:
            r0 = 0.0f;
     */
    /* JADX WARNING: Missing block: B:59:0x010a, code skipped:
            requestFramerate(r0);
     */
    /* JADX WARNING: Missing block: B:60:0x010d, code skipped:
            return;
     */
    public void doTick(long r23) {
        /*
        r22 = this;
        r1 = r22;
        r2 = r23;
        r4 = mLock;
        monitor-enter(r4);
        super.doTick(r23);	 Catch:{ all -> 0x010e }
        r0 = r22.isVisible();	 Catch:{ all -> 0x010e }
        if (r0 != 0) goto L_0x0012;
    L_0x0010:
        monitor-exit(r4);	 Catch:{ all -> 0x010e }
        return;
    L_0x0012:
        r0 = 0;
        r1.mShouldMarquee = r0;	 Catch:{ all -> 0x010e }
        r5 = r1.mText;	 Catch:{ all -> 0x010e }
        r6 = r22.getText();	 Catch:{ all -> 0x010e }
        r1.mText = r6;	 Catch:{ all -> 0x010e }
        r6 = r1.mText;	 Catch:{ all -> 0x010e }
        r6 = android.text.TextUtils.isEmpty(r6);	 Catch:{ all -> 0x010e }
        if (r6 == 0) goto L_0x002d;
    L_0x0025:
        r0 = 0;
        r1.mTextLayout = r0;	 Catch:{ all -> 0x010e }
        r22.updateTextWidth();	 Catch:{ all -> 0x010e }
        monitor-exit(r4);	 Catch:{ all -> 0x010e }
        return;
    L_0x002d:
        r6 = r1.mTextSize;	 Catch:{ all -> 0x010e }
        r22.updateTextSize();	 Catch:{ all -> 0x010e }
        r7 = r1.mText;	 Catch:{ all -> 0x010e }
        r7 = android.text.TextUtils.equals(r5, r7);	 Catch:{ all -> 0x010e }
        if (r7 == 0) goto L_0x0043;
    L_0x003a:
        r7 = r1.mTextSize;	 Catch:{ all -> 0x010e }
        r7 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r7 == 0) goto L_0x0041;
    L_0x0040:
        goto L_0x0043;
    L_0x0041:
        r7 = r0;
        goto L_0x0044;
    L_0x0043:
        r7 = 1;
    L_0x0044:
        if (r7 == 0) goto L_0x0049;
    L_0x0046:
        r22.updateTextWidth();	 Catch:{ all -> 0x010e }
    L_0x0049:
        r9 = r22.getWidth();	 Catch:{ all -> 0x010e }
        r10 = r1.mMultiLine;	 Catch:{ all -> 0x010e }
        if (r10 != 0) goto L_0x005c;
    L_0x0051:
        r10 = r1.mMarqueeSpeed;	 Catch:{ all -> 0x010e }
        if (r10 <= 0) goto L_0x005c;
    L_0x0055:
        r10 = r1.mTextWidth;	 Catch:{ all -> 0x010e }
        r10 = (r10 > r9 ? 1 : (r10 == r9 ? 0 : -1));
        if (r10 <= 0) goto L_0x005c;
    L_0x005b:
        r0 = 1;
    L_0x005c:
        r10 = r1.mMultiLine;	 Catch:{ all -> 0x010e }
        if (r10 == 0) goto L_0x0069;
    L_0x0060:
        r10 = r1.mTextWidth;	 Catch:{ all -> 0x010e }
        r10 = (r9 > r10 ? 1 : (r9 == r10 ? 0 : -1));
        if (r10 <= 0) goto L_0x0067;
    L_0x0066:
        goto L_0x0069;
    L_0x0067:
        r10 = r9;
        goto L_0x006b;
    L_0x0069:
        r10 = r1.mTextWidth;	 Catch:{ all -> 0x010e }
    L_0x006b:
        r11 = r1.mTextLayout;	 Catch:{ all -> 0x010e }
        r12 = 2139095039; // 0x7f7fffff float:3.4028235E38 double:1.056853372E-314;
        if (r11 == 0) goto L_0x007e;
    L_0x0072:
        if (r7 != 0) goto L_0x007e;
    L_0x0074:
        r11 = r1.mLayoutWidth;	 Catch:{ all -> 0x010e }
        r11 = (r11 > r10 ? 1 : (r11 == r10 ? 0 : -1));
        if (r11 == 0) goto L_0x007b;
    L_0x007a:
        goto L_0x007e;
    L_0x007b:
        r21 = r9;
        goto L_0x00c7;
    L_0x007e:
        r1.mLayoutWidth = r10;	 Catch:{ all -> 0x010e }
        r11 = new android.text.StaticLayout;	 Catch:{ all -> 0x010e }
        r14 = r1.mText;	 Catch:{ all -> 0x010e }
        r15 = r1.mPaint;	 Catch:{ all -> 0x010e }
        r13 = r1.mLayoutWidth;	 Catch:{ all -> 0x010e }
        r21 = r9;
        r8 = (double) r13;	 Catch:{ all -> 0x010e }
        r8 = java.lang.Math.ceil(r8);	 Catch:{ all -> 0x010e }
        r8 = (int) r8;	 Catch:{ all -> 0x010e }
        r17 = r22.getAlignment();	 Catch:{ all -> 0x010e }
        r9 = r1.mSpacingMult;	 Catch:{ all -> 0x010e }
        r13 = r1.mSpacingAdd;	 Catch:{ all -> 0x010e }
        r20 = 0;
        r19 = r13;
        r13 = r11;
        r16 = r8;
        r18 = r9;
        r13.<init>(r14, r15, r16, r17, r18, r19, r20);	 Catch:{ all -> 0x010e }
        r1.mTextLayout = r11;	 Catch:{ all -> 0x010e }
        r8 = r1.mTextLayout;	 Catch:{ all -> 0x010e }
        r9 = r1.mTextLayout;	 Catch:{ all -> 0x010e }
        r9 = r9.getLineCount();	 Catch:{ all -> 0x010e }
        r8 = r8.getLineTop(r9);	 Catch:{ all -> 0x010e }
        r8 = (float) r8;	 Catch:{ all -> 0x010e }
        r1.mTextHeight = r8;	 Catch:{ all -> 0x010e }
        r8 = r1.mHasName;	 Catch:{ all -> 0x010e }
        if (r8 == 0) goto L_0x00c5;
    L_0x00b9:
        r8 = r1.mTextHeightVar;	 Catch:{ all -> 0x010e }
        r9 = r1.mTextHeight;	 Catch:{ all -> 0x010e }
        r13 = (double) r9;	 Catch:{ all -> 0x010e }
        r13 = r1.descale(r13);	 Catch:{ all -> 0x010e }
        r8.set(r13);	 Catch:{ all -> 0x010e }
    L_0x00c5:
        r1.mMarqueePos = r12;	 Catch:{ all -> 0x010e }
    L_0x00c7:
        if (r0 == 0) goto L_0x0101;
    L_0x00c9:
        r8 = r1.mMarqueePos;	 Catch:{ all -> 0x010e }
        r8 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1));
        if (r8 != 0) goto L_0x00d4;
    L_0x00cf:
        r8 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r1.mMarqueePos = r8;	 Catch:{ all -> 0x010e }
        goto L_0x00fc;
    L_0x00d4:
        r8 = r1.mMarqueePos;	 Catch:{ all -> 0x010e }
        r9 = r1.mMarqueeSpeed;	 Catch:{ all -> 0x010e }
        r11 = (long) r9;	 Catch:{ all -> 0x010e }
        r13 = r1.mPreviousTime;	 Catch:{ all -> 0x010e }
        r13 = r2 - r13;
        r11 = r11 * r13;
        r9 = (float) r11;	 Catch:{ all -> 0x010e }
        r11 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r9 = r9 / r11;
        r8 = r8 - r9;
        r1.mMarqueePos = r8;	 Catch:{ all -> 0x010e }
        r8 = r1.mMarqueePos;	 Catch:{ all -> 0x010e }
        r9 = r1.mTextWidth;	 Catch:{ all -> 0x010e }
        r9 = -r9;
        r8 = (r8 > r9 ? 1 : (r8 == r9 ? 0 : -1));
        if (r8 >= 0) goto L_0x00fc;
    L_0x00ee:
        r8 = r1.mMarqueePos;	 Catch:{ all -> 0x010e }
        r9 = r1.mTextWidth;	 Catch:{ all -> 0x010e }
        r11 = r1.mTextSize;	 Catch:{ all -> 0x010e }
        r12 = r1.mMarqueeGap;	 Catch:{ all -> 0x010e }
        r12 = (float) r12;	 Catch:{ all -> 0x010e }
        r11 = r11 * r12;
        r9 = r9 + r11;
        r8 = r8 + r9;
        r1.mMarqueePos = r8;	 Catch:{ all -> 0x010e }
    L_0x00fc:
        r1.mPreviousTime = r2;	 Catch:{ all -> 0x010e }
        r8 = 1;
        r1.mShouldMarquee = r8;	 Catch:{ all -> 0x010e }
    L_0x0101:
        monitor-exit(r4);	 Catch:{ all -> 0x010e }
        r0 = r1.mShouldMarquee;
        if (r0 == 0) goto L_0x0109;
    L_0x0106:
        r0 = 1110704128; // 0x42340000 float:45.0 double:5.487607523E-315;
        goto L_0x010a;
    L_0x0109:
        r0 = 0;
    L_0x010a:
        r1.requestFramerate(r0);
        return;
    L_0x010e:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x010e }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.elements.TextScreenElement.doTick(long):void");
    }

    private void updateTextWidth() {
        this.mTextWidth = 0.0f;
        if (!TextUtils.isEmpty(this.mText)) {
            if (this.mMultiLine) {
                String[] lines = this.mText.split(CRLF);
                for (String measureText : lines) {
                    float w = this.mPaint.measureText(measureText);
                    if (w > this.mTextWidth) {
                        this.mTextWidth = w;
                    }
                }
            } else {
                this.mTextWidth = this.mPaint.measureText(this.mText);
            }
        }
        if (this.mHasName) {
            this.mTextWidthVar.set(descale((double) this.mTextWidth));
        }
    }

    private void updateTextSize() {
        Expression expression = this.mSizeExpression;
        if (expression != null) {
            this.mTextSize = scale(evaluate(expression));
            if (this.mFontScaleEnabled) {
                this.mTextSize *= this.mRoot.getFontScale();
            }
            this.mPaint.setTextSize(this.mTextSize);
        }
    }

    private Alignment getAlignment() {
        Alignment align = Alignment.ALIGN_NORMAL;
        int i = AnonymousClass1.$SwitchMap$miui$maml$elements$ScreenElement$Align[this.mAlign.ordinal()];
        if (i == 1) {
            return Alignment.ALIGN_LEFT;
        }
        if (i == 2) {
            return Alignment.ALIGN_CENTER;
        }
        if (i != 3) {
            return align;
        }
        return Alignment.ALIGN_RIGHT;
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChange(boolean visible) {
        super.onVisibilityChange(visible);
        float f = (this.mShouldMarquee && visible) ? 45.0f : 0.0f;
        requestFramerate(f);
    }

    /* Access modifiers changed, original: protected */
    public String getFormat() {
        return this.mFormatter.getFormat();
    }

    public void setParams(Object... params) {
        this.mFormatter.setParams(params);
    }

    public void setText(String text) {
        this.mSetText = text;
    }

    /* Access modifiers changed, original: protected */
    public String getText() {
        String str = this.mSetText;
        if (str != null) {
            return str;
        }
        str = this.mFormatter.getText();
        if (str != null) {
            String str2 = CRLF;
            str = str.replace(RAW_CRLF, str2);
            if (!this.mMultiLine) {
                str = str.replace(str2, WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
        }
        return str;
    }

    /* Access modifiers changed, original: protected */
    public int getColor() {
        return this.mColorParser.getColor();
    }

    /* Access modifiers changed, original: protected */
    public int getShadowColor() {
        return this.mShadowColorParser.getColor();
    }

    public float getWidth() {
        float width = super.getWidth();
        return width <= 0.0f ? this.mTextWidth : width;
    }

    public float getHeight() {
        float height = super.getHeight();
        if (height <= 0.0f) {
            return this.mTextHeight;
        }
        return height;
    }
}
