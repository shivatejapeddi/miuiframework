package android.content.res;

import android.content.res.Resources.Theme;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.net.wifi.WifiEnterpriseConfig;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.R;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class GradientColor extends ComplexColor {
    private static final boolean DBG_GRADIENT = false;
    private static final String TAG = "GradientColor";
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;
    private int mCenterColor = 0;
    private float mCenterX = 0.0f;
    private float mCenterY = 0.0f;
    private int mChangingConfigurations;
    private int mDefaultColor;
    private int mEndColor = 0;
    private float mEndX = 0.0f;
    private float mEndY = 0.0f;
    private GradientColorFactory mFactory;
    private float mGradientRadius = 0.0f;
    private int mGradientType = 0;
    private boolean mHasCenterColor = false;
    private int[] mItemColors;
    private float[] mItemOffsets;
    private int[][] mItemsThemeAttrs;
    private Shader mShader = null;
    private int mStartColor = 0;
    private float mStartX = 0.0f;
    private float mStartY = 0.0f;
    private int[] mThemeAttrs;
    private int mTileMode = 0;

    private static class GradientColorFactory extends ConstantState<ComplexColor> {
        private final GradientColor mSrc;

        public GradientColorFactory(GradientColor src) {
            this.mSrc = src;
        }

        public int getChangingConfigurations() {
            return this.mSrc.mChangingConfigurations;
        }

        public GradientColor newInstance() {
            return this.mSrc;
        }

        public GradientColor newInstance(Resources res, Theme theme) {
            return this.mSrc.obtainForTheme(theme);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface GradientTileMode {
    }

    private GradientColor() {
    }

    private GradientColor(GradientColor copy) {
        if (copy != null) {
            this.mChangingConfigurations = copy.mChangingConfigurations;
            this.mDefaultColor = copy.mDefaultColor;
            this.mShader = copy.mShader;
            this.mGradientType = copy.mGradientType;
            this.mCenterX = copy.mCenterX;
            this.mCenterY = copy.mCenterY;
            this.mStartX = copy.mStartX;
            this.mStartY = copy.mStartY;
            this.mEndX = copy.mEndX;
            this.mEndY = copy.mEndY;
            this.mStartColor = copy.mStartColor;
            this.mCenterColor = copy.mCenterColor;
            this.mEndColor = copy.mEndColor;
            this.mHasCenterColor = copy.mHasCenterColor;
            this.mGradientRadius = copy.mGradientRadius;
            this.mTileMode = copy.mTileMode;
            int[] iArr = copy.mItemColors;
            if (iArr != null) {
                this.mItemColors = (int[]) iArr.clone();
            }
            float[] fArr = copy.mItemOffsets;
            if (fArr != null) {
                this.mItemOffsets = (float[]) fArr.clone();
            }
            iArr = copy.mThemeAttrs;
            if (iArr != null) {
                this.mThemeAttrs = (int[]) iArr.clone();
            }
            int[][] iArr2 = copy.mItemsThemeAttrs;
            if (iArr2 != null) {
                this.mItemsThemeAttrs = (int[][]) iArr2.clone();
            }
        }
    }

    private static TileMode parseTileMode(int tileMode) {
        if (tileMode == 0) {
            return TileMode.CLAMP;
        }
        if (tileMode == 1) {
            return TileMode.REPEAT;
        }
        if (tileMode != 2) {
            return TileMode.CLAMP;
        }
        return TileMode.MIRROR;
    }

    private void updateRootElementState(TypedArray a) {
        this.mThemeAttrs = a.extractThemeAttrs();
        this.mStartX = a.getFloat(8, this.mStartX);
        this.mStartY = a.getFloat(9, this.mStartY);
        this.mEndX = a.getFloat(10, this.mEndX);
        this.mEndY = a.getFloat(11, this.mEndY);
        this.mCenterX = a.getFloat(3, this.mCenterX);
        this.mCenterY = a.getFloat(4, this.mCenterY);
        this.mGradientType = a.getInt(2, this.mGradientType);
        this.mStartColor = a.getColor(0, this.mStartColor);
        this.mHasCenterColor |= a.hasValue(7);
        this.mCenterColor = a.getColor(7, this.mCenterColor);
        this.mEndColor = a.getColor(1, this.mEndColor);
        this.mTileMode = a.getInt(6, this.mTileMode);
        this.mGradientRadius = a.getFloat(5, this.mGradientRadius);
    }

    private void validateXmlContent() throws XmlPullParserException {
        if (this.mGradientRadius <= 0.0f && this.mGradientType == 1) {
            throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
        }
    }

    public Shader getShader() {
        return this.mShader;
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0017  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0012  */
    public static android.content.res.GradientColor createFromXml(android.content.res.Resources r4, android.content.res.XmlResourceParser r5, android.content.res.Resources.Theme r6) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r0 = android.util.Xml.asAttributeSet(r5);
    L_0x0004:
        r1 = r5.next();
        r2 = r1;
        r3 = 2;
        if (r1 == r3) goto L_0x0010;
    L_0x000c:
        r1 = 1;
        if (r2 == r1) goto L_0x0010;
    L_0x000f:
        goto L_0x0004;
    L_0x0010:
        if (r2 != r3) goto L_0x0017;
    L_0x0012:
        r1 = createFromXmlInner(r4, r5, r0, r6);
        return r1;
    L_0x0017:
        r1 = new org.xmlpull.v1.XmlPullParserException;
        r3 = "No start tag found";
        r1.<init>(r3);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.GradientColor.createFromXml(android.content.res.Resources, android.content.res.XmlResourceParser, android.content.res.Resources$Theme):android.content.res.GradientColor");
    }

    static GradientColor createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        String name = parser.getName();
        if (name.equals("gradient")) {
            GradientColor gradientColor = new GradientColor();
            gradientColor.inflate(r, parser, attrs, theme);
            return gradientColor;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(parser.getPositionDescription());
        stringBuilder.append(": invalid gradient color tag ");
        stringBuilder.append(name);
        throw new XmlPullParserException(stringBuilder.toString());
    }

    private void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        TypedArray a = Resources.obtainAttributes(r, theme, attrs, R.styleable.GradientColor);
        updateRootElementState(a);
        this.mChangingConfigurations |= a.getChangingConfigurations();
        a.recycle();
        validateXmlContent();
        inflateChildElements(r, parser, attrs, theme);
        onColorsChange();
    }

    /* JADX WARNING: Missing block: B:27:0x00e1, code skipped:
            if (r6 <= 0) goto L_?;
     */
    /* JADX WARNING: Missing block: B:28:0x00e3, code skipped:
            if (r7 == false) goto L_0x00ef;
     */
    /* JADX WARNING: Missing block: B:29:0x00e5, code skipped:
            r0.mItemsThemeAttrs = new int[r6][];
            java.lang.System.arraycopy(r5, 0, r0.mItemsThemeAttrs, 0, r6);
     */
    /* JADX WARNING: Missing block: B:30:0x00ef, code skipped:
            r0.mItemsThemeAttrs = null;
     */
    /* JADX WARNING: Missing block: B:31:0x00f2, code skipped:
            r0.mItemColors = new int[r6];
            r0.mItemOffsets = new float[r6];
            java.lang.System.arraycopy(r4, 0, r0.mItemColors, 0, r6);
            java.lang.System.arraycopy(r3, 0, r0.mItemOffsets, 0, r6);
     */
    /* JADX WARNING: Missing block: B:39:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:40:?, code skipped:
            return;
     */
    private void inflateChildElements(android.content.res.Resources r21, org.xmlpull.v1.XmlPullParser r22, android.util.AttributeSet r23, android.content.res.Resources.Theme r24) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r20 = this;
        r0 = r20;
        r1 = r22.getDepth();
        r2 = 1;
        r1 = r1 + r2;
        r3 = 20;
        r3 = new float[r3];
        r4 = r3.length;
        r4 = new int[r4];
        r5 = r3.length;
        r5 = new int[r5][];
        r6 = 0;
        r7 = 0;
    L_0x0014:
        r8 = r22.next();
        r9 = r8;
        r10 = 0;
        if (r8 == r2) goto L_0x00d7;
    L_0x001c:
        r8 = r22.getDepth();
        r11 = r8;
        if (r8 >= r1) goto L_0x0033;
    L_0x0023:
        r8 = 3;
        if (r9 == r8) goto L_0x0027;
    L_0x0026:
        goto L_0x0033;
    L_0x0027:
        r12 = r21;
        r13 = r23;
        r14 = r24;
        r17 = r1;
        r18 = r9;
        goto L_0x00e1;
    L_0x0033:
        r8 = 2;
        if (r9 == r8) goto L_0x0040;
    L_0x0036:
        r12 = r21;
        r13 = r23;
        r14 = r24;
        r17 = r1;
        goto L_0x00d2;
    L_0x0040:
        if (r11 > r1) goto L_0x00c8;
    L_0x0042:
        r8 = r22.getName();
        r12 = "item";
        r8 = r8.equals(r12);
        if (r8 != 0) goto L_0x0058;
    L_0x004e:
        r12 = r21;
        r13 = r23;
        r14 = r24;
        r17 = r1;
        goto L_0x00d2;
    L_0x0058:
        r8 = com.android.internal.R.styleable.GradientColorItem;
        r12 = r21;
        r13 = r23;
        r14 = r24;
        r8 = android.content.res.Resources.obtainAttributes(r12, r14, r13, r8);
        r15 = r8.hasValue(r10);
        r16 = r8.hasValue(r2);
        if (r15 == 0) goto L_0x00a9;
    L_0x006e:
        if (r16 == 0) goto L_0x00a9;
    L_0x0070:
        r2 = r8.extractThemeAttrs();
        r10 = r8.getColor(r10, r10);
        r17 = r1;
        r1 = 0;
        r18 = r9;
        r9 = 1;
        r1 = r8.getFloat(r9, r1);
        r9 = r0.mChangingConfigurations;
        r19 = r8.getChangingConfigurations();
        r9 = r9 | r19;
        r0.mChangingConfigurations = r9;
        r8.recycle();
        if (r2 == 0) goto L_0x0092;
    L_0x0091:
        r7 = 1;
    L_0x0092:
        r4 = com.android.internal.util.GrowingArrayUtils.append(r4, r6, r10);
        r3 = com.android.internal.util.GrowingArrayUtils.append(r3, r6, r1);
        r9 = com.android.internal.util.GrowingArrayUtils.append(r5, r6, r2);
        r5 = r9;
        r5 = (int[][]) r5;
        r6 = r6 + 1;
        r1 = r17;
        r2 = 1;
        goto L_0x0014;
    L_0x00a9:
        r17 = r1;
        r18 = r9;
        r1 = new org.xmlpull.v1.XmlPullParserException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r9 = r22.getPositionDescription();
        r2.append(r9);
        r9 = ": <item> tag requires a 'color' attribute and a 'offset' attribute!";
        r2.append(r9);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x00c8:
        r12 = r21;
        r13 = r23;
        r14 = r24;
        r17 = r1;
        r18 = r9;
    L_0x00d2:
        r1 = r17;
        r2 = 1;
        goto L_0x0014;
    L_0x00d7:
        r12 = r21;
        r13 = r23;
        r14 = r24;
        r17 = r1;
        r18 = r9;
    L_0x00e1:
        if (r6 <= 0) goto L_0x0104;
    L_0x00e3:
        if (r7 == 0) goto L_0x00ef;
    L_0x00e5:
        r1 = new int[r6][];
        r0.mItemsThemeAttrs = r1;
        r1 = r0.mItemsThemeAttrs;
        java.lang.System.arraycopy(r5, r10, r1, r10, r6);
        goto L_0x00f2;
    L_0x00ef:
        r1 = 0;
        r0.mItemsThemeAttrs = r1;
    L_0x00f2:
        r1 = new int[r6];
        r0.mItemColors = r1;
        r1 = new float[r6];
        r0.mItemOffsets = r1;
        r1 = r0.mItemColors;
        java.lang.System.arraycopy(r4, r10, r1, r10, r6);
        r1 = r0.mItemOffsets;
        java.lang.System.arraycopy(r3, r10, r1, r10, r6);
    L_0x0104:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.GradientColor.inflateChildElements(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.content.res.Resources$Theme):void");
    }

    private void applyItemsAttrsTheme(Theme t) {
        if (this.mItemsThemeAttrs != null) {
            boolean hasUnresolvedAttrs = false;
            int[][] themeAttrsList = this.mItemsThemeAttrs;
            int N = themeAttrsList.length;
            for (int i = 0; i < N; i++) {
                if (themeAttrsList[i] != null) {
                    TypedArray a = t.resolveAttributes(themeAttrsList[i], R.styleable.GradientColorItem);
                    themeAttrsList[i] = a.extractThemeAttrs(themeAttrsList[i]);
                    if (themeAttrsList[i] != null) {
                        hasUnresolvedAttrs = true;
                    }
                    int[] iArr = this.mItemColors;
                    iArr[i] = a.getColor(0, iArr[i]);
                    float[] fArr = this.mItemOffsets;
                    fArr[i] = a.getFloat(1, fArr[i]);
                    this.mChangingConfigurations |= a.getChangingConfigurations();
                    a.recycle();
                }
            }
            if (!hasUnresolvedAttrs) {
                this.mItemsThemeAttrs = null;
            }
        }
    }

    private void onColorsChange() {
        int[] tempColors;
        float[] tempOffsets = null;
        int length = this.mItemColors;
        if (length != 0) {
            length = length.length;
            tempColors = new int[length];
            tempOffsets = new float[length];
            for (int i = 0; i < length; i++) {
                tempColors[i] = this.mItemColors[i];
                tempOffsets[i] = this.mItemOffsets[i];
            }
        } else if (this.mHasCenterColor) {
            tempColors = new int[]{this.mStartColor, this.mCenterColor, this.mEndColor};
            tempOffsets = new float[]{0.0f, 0.5f, 1.0f};
        } else {
            tempColors = new int[]{this.mStartColor, this.mEndColor};
        }
        if (tempColors.length < 2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<gradient> tag requires 2 color values specified!");
            stringBuilder.append(tempColors.length);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(tempColors);
            Log.w(TAG, stringBuilder.toString());
        }
        length = this.mGradientType;
        if (length == 0) {
            this.mShader = new LinearGradient(this.mStartX, this.mStartY, this.mEndX, this.mEndY, tempColors, tempOffsets, parseTileMode(this.mTileMode));
        } else if (length == 1) {
            this.mShader = new RadialGradient(this.mCenterX, this.mCenterY, this.mGradientRadius, tempColors, tempOffsets, parseTileMode(this.mTileMode));
        } else {
            this.mShader = new SweepGradient(this.mCenterX, this.mCenterY, tempColors, tempOffsets);
        }
        this.mDefaultColor = tempColors[0];
    }

    public int getDefaultColor() {
        return this.mDefaultColor;
    }

    public ConstantState<ComplexColor> getConstantState() {
        if (this.mFactory == null) {
            this.mFactory = new GradientColorFactory(this);
        }
        return this.mFactory;
    }

    public GradientColor obtainForTheme(Theme t) {
        if (t == null || !canApplyTheme()) {
            return this;
        }
        GradientColor clone = new GradientColor(this);
        clone.applyTheme(t);
        return clone;
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mChangingConfigurations;
    }

    private void applyTheme(Theme t) {
        if (this.mThemeAttrs != null) {
            applyRootAttrsTheme(t);
        }
        if (this.mItemsThemeAttrs != null) {
            applyItemsAttrsTheme(t);
        }
        onColorsChange();
    }

    private void applyRootAttrsTheme(Theme t) {
        TypedArray a = t.resolveAttributes(this.mThemeAttrs, R.styleable.GradientColor);
        this.mThemeAttrs = a.extractThemeAttrs(this.mThemeAttrs);
        updateRootElementState(a);
        this.mChangingConfigurations |= a.getChangingConfigurations();
        a.recycle();
    }

    public boolean canApplyTheme() {
        return (this.mThemeAttrs == null && this.mItemsThemeAttrs == null) ? false : true;
    }
}
