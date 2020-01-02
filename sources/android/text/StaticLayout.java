package android.text;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Paint.FontMetricsInt;
import android.text.Layout.Alignment;
import android.text.Layout.Directions;
import android.text.TextUtils.TruncateAt;
import android.text.style.LineHeightSpan;
import android.text.style.LineHeightSpan.WithDensity;
import android.util.Log;
import android.util.Pools.SynchronizedPool;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;

public class StaticLayout extends Layout {
    private static final char CHAR_NEW_LINE = '\n';
    private static final int COLUMNS_ELLIPSIZE = 7;
    private static final int COLUMNS_NORMAL = 5;
    private static final int DEFAULT_MAX_LINE_HEIGHT = -1;
    private static final int DESCENT = 2;
    private static final int DIR = 0;
    private static final int DIR_SHIFT = 30;
    private static final int ELLIPSIS_COUNT = 6;
    @UnsupportedAppUsage
    private static final int ELLIPSIS_START = 5;
    private static final int END_HYPHEN_MASK = 7;
    private static final int EXTRA = 3;
    private static final double EXTRA_ROUNDING = 0.5d;
    private static final int HYPHEN = 4;
    private static final int HYPHEN_MASK = 255;
    private static final int START = 0;
    private static final int START_HYPHEN_BITS_SHIFT = 3;
    private static final int START_HYPHEN_MASK = 24;
    private static final int START_MASK = 536870911;
    private static final int TAB = 0;
    private static final float TAB_INCREMENT = 20.0f;
    private static final int TAB_MASK = 536870912;
    static final String TAG = "StaticLayout";
    private static final int TOP = 1;
    private int mBottomPadding;
    @UnsupportedAppUsage
    private int mColumns;
    private boolean mEllipsized;
    private int mEllipsizedWidth;
    private int[] mLeftIndents;
    @UnsupportedAppUsage
    private int mLineCount;
    @UnsupportedAppUsage
    private Directions[] mLineDirections;
    @UnsupportedAppUsage
    private int[] mLines;
    private int mMaxLineHeight;
    @UnsupportedAppUsage
    private int mMaximumVisibleLineCount;
    private int[] mRightIndents;
    private int mTopPadding;

    public static final class Builder {
        private static final SynchronizedPool<Builder> sPool = new SynchronizedPool(3);
        private boolean mAddLastLineLineSpacing;
        private Alignment mAlignment;
        private int mBreakStrategy;
        private TruncateAt mEllipsize;
        private int mEllipsizedWidth;
        private int mEnd;
        private boolean mFallbackLineSpacing;
        private final FontMetricsInt mFontMetricsInt = new FontMetricsInt();
        private int mHyphenationFrequency;
        private boolean mIncludePad;
        private int mJustificationMode;
        private int[] mLeftIndents;
        private int mMaxLines;
        private TextPaint mPaint;
        private int[] mRightIndents;
        private float mSpacingAdd;
        private float mSpacingMult;
        private int mStart;
        private CharSequence mText;
        private TextDirectionHeuristic mTextDir;
        private int mWidth;

        private Builder() {
        }

        public static Builder obtain(CharSequence source, int start, int end, TextPaint paint, int width) {
            Builder b = (Builder) sPool.acquire();
            if (b == null) {
                b = new Builder();
            }
            b.mText = source;
            b.mStart = start;
            b.mEnd = end;
            b.mPaint = paint;
            b.mWidth = width;
            b.mAlignment = Alignment.ALIGN_NORMAL;
            b.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
            b.mSpacingMult = 1.0f;
            b.mSpacingAdd = 0.0f;
            b.mIncludePad = true;
            b.mFallbackLineSpacing = false;
            b.mEllipsizedWidth = width;
            b.mEllipsize = null;
            b.mMaxLines = Integer.MAX_VALUE;
            b.mBreakStrategy = 0;
            b.mHyphenationFrequency = 0;
            b.mJustificationMode = 0;
            return b;
        }

        private static void recycle(Builder b) {
            b.mPaint = null;
            b.mText = null;
            b.mLeftIndents = null;
            b.mRightIndents = null;
            sPool.release(b);
        }

        /* Access modifiers changed, original: 0000 */
        public void finish() {
            this.mText = null;
            this.mPaint = null;
            this.mLeftIndents = null;
            this.mRightIndents = null;
        }

        public Builder setText(CharSequence source) {
            return setText(source, 0, source.length());
        }

        public Builder setText(CharSequence source, int start, int end) {
            this.mText = source;
            this.mStart = start;
            this.mEnd = end;
            return this;
        }

        public Builder setPaint(TextPaint paint) {
            this.mPaint = paint;
            return this;
        }

        public Builder setWidth(int width) {
            this.mWidth = width;
            if (this.mEllipsize == null) {
                this.mEllipsizedWidth = width;
            }
            return this;
        }

        public Builder setAlignment(Alignment alignment) {
            this.mAlignment = alignment;
            return this;
        }

        public Builder setTextDirection(TextDirectionHeuristic textDir) {
            this.mTextDir = textDir;
            return this;
        }

        public Builder setLineSpacing(float spacingAdd, float spacingMult) {
            this.mSpacingAdd = spacingAdd;
            this.mSpacingMult = spacingMult;
            return this;
        }

        public Builder setIncludePad(boolean includePad) {
            this.mIncludePad = includePad;
            return this;
        }

        public Builder setUseLineSpacingFromFallbacks(boolean useLineSpacingFromFallbacks) {
            this.mFallbackLineSpacing = useLineSpacingFromFallbacks;
            return this;
        }

        public Builder setEllipsizedWidth(int ellipsizedWidth) {
            this.mEllipsizedWidth = ellipsizedWidth;
            return this;
        }

        public Builder setEllipsize(TruncateAt ellipsize) {
            this.mEllipsize = ellipsize;
            return this;
        }

        public Builder setMaxLines(int maxLines) {
            this.mMaxLines = maxLines;
            return this;
        }

        public Builder setBreakStrategy(int breakStrategy) {
            this.mBreakStrategy = breakStrategy;
            return this;
        }

        public Builder setHyphenationFrequency(int hyphenationFrequency) {
            this.mHyphenationFrequency = hyphenationFrequency;
            return this;
        }

        public Builder setIndents(int[] leftIndents, int[] rightIndents) {
            this.mLeftIndents = leftIndents;
            this.mRightIndents = rightIndents;
            return this;
        }

        public Builder setJustificationMode(int justificationMode) {
            this.mJustificationMode = justificationMode;
            return this;
        }

        /* Access modifiers changed, original: 0000 */
        public Builder setAddLastLineLineSpacing(boolean value) {
            this.mAddLastLineLineSpacing = value;
            return this;
        }

        public StaticLayout build() {
            StaticLayout result = new StaticLayout(this);
            recycle(this);
            return result;
        }
    }

    static class LineBreaks {
        private static final int INITIAL_SIZE = 16;
        @UnsupportedAppUsage
        public float[] ascents = new float[16];
        @UnsupportedAppUsage
        public int[] breaks = new int[16];
        @UnsupportedAppUsage
        public float[] descents = new float[16];
        @UnsupportedAppUsage
        public int[] flags = new int[16];
        @UnsupportedAppUsage
        public float[] widths = new float[16];

        LineBreaks() {
        }
    }

    @Deprecated
    public StaticLayout(CharSequence source, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(source, 0, source.length(), paint, width, align, spacingmult, spacingadd, includepad);
    }

    @Deprecated
    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(source, bufstart, bufend, paint, outerwidth, align, spacingmult, spacingadd, includepad, null, 0);
    }

    @Deprecated
    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, boolean includepad, TruncateAt ellipsize, int ellipsizedWidth) {
        this(source, bufstart, bufend, paint, outerwidth, align, TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingmult, spacingadd, includepad, ellipsize, ellipsizedWidth, Integer.MAX_VALUE);
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 117521430)
    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Alignment align, TextDirectionHeuristic textDir, float spacingmult, float spacingadd, boolean includepad, TruncateAt ellipsize, int ellipsizedWidth, int maxLines) {
        CharSequence charSequence;
        CharSequence charSequence2 = source;
        TruncateAt truncateAt = ellipsize;
        int i = ellipsizedWidth;
        int i2 = maxLines;
        if (truncateAt == null) {
            charSequence = charSequence2;
        } else if (charSequence2 instanceof Spanned) {
            charSequence = new SpannedEllipsizer(source);
        } else {
            charSequence = new Ellipsizer(source);
        }
        super(charSequence, paint, outerwidth, align, textDir, spacingmult, spacingadd);
        this.mMaxLineHeight = -1;
        this.mMaximumVisibleLineCount = Integer.MAX_VALUE;
        Builder b = Builder.obtain(source, bufstart, bufend, paint, outerwidth).setAlignment(align).setTextDirection(textDir).setLineSpacing(spacingadd, spacingmult).setIncludePad(includepad).setEllipsizedWidth(i).setEllipsize(truncateAt).setMaxLines(i2);
        if (truncateAt != null) {
            Ellipsizer e = (Ellipsizer) getText();
            e.mLayout = this;
            e.mWidth = i;
            e.mMethod = truncateAt;
            this.mEllipsizedWidth = i;
            this.mColumns = 7;
            e = outerwidth;
        } else {
            this.mColumns = 5;
            this.mEllipsizedWidth = outerwidth;
        }
        this.mLineDirections = (Directions[]) ArrayUtils.newUnpaddedArray(Directions.class, 2);
        this.mLines = ArrayUtils.newUnpaddedIntArray(this.mColumns * 2);
        this.mMaximumVisibleLineCount = i2;
        generate(b, b.mIncludePad, b.mIncludePad);
        Builder.recycle(b);
    }

    StaticLayout(CharSequence text) {
        super(text, null, 0, null, 0.0f, 0.0f);
        this.mMaxLineHeight = -1;
        this.mMaximumVisibleLineCount = Integer.MAX_VALUE;
        this.mColumns = 7;
        this.mLineDirections = (Directions[]) ArrayUtils.newUnpaddedArray(Directions.class, 2);
        this.mLines = ArrayUtils.newUnpaddedIntArray(this.mColumns * 2);
    }

    private StaticLayout(Builder b) {
        CharSequence access$400;
        if (b.mEllipsize == null) {
            access$400 = b.mText;
        } else if (b.mText instanceof Spanned) {
            access$400 = new SpannedEllipsizer(b.mText);
        } else {
            access$400 = new Ellipsizer(b.mText);
        }
        super(access$400, b.mPaint, b.mWidth, b.mAlignment, b.mTextDir, b.mSpacingMult, b.mSpacingAdd);
        this.mMaxLineHeight = -1;
        this.mMaximumVisibleLineCount = Integer.MAX_VALUE;
        if (b.mEllipsize != null) {
            Ellipsizer e = (Ellipsizer) getText();
            e.mLayout = this;
            e.mWidth = b.mEllipsizedWidth;
            e.mMethod = b.mEllipsize;
            this.mEllipsizedWidth = b.mEllipsizedWidth;
            this.mColumns = 7;
        } else {
            this.mColumns = 5;
            this.mEllipsizedWidth = b.mWidth;
        }
        this.mLineDirections = (Directions[]) ArrayUtils.newUnpaddedArray(Directions.class, 2);
        this.mLines = ArrayUtils.newUnpaddedIntArray(this.mColumns * 2);
        this.mMaximumVisibleLineCount = b.mMaxLines;
        this.mLeftIndents = b.mLeftIndents;
        this.mRightIndents = b.mRightIndents;
        setJustificationMode(b.mJustificationMode);
        generate(b, b.mIncludePad, b.mIncludePad);
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x02c9  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x02a1  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x02dc A:{LOOP_END, LOOP:6: B:97:0x02da->B:98:0x02dc} */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x03aa  */
    /* JADX WARNING: Removed duplicated region for block: B:176:0x05f6 A:{LOOP_END, LOOP:2: B:49:0x0172->B:176:0x05f6} */
    /* JADX WARNING: Removed duplicated region for block: B:192:0x05e4 A:{SYNTHETIC} */
    public void generate(android.text.StaticLayout.Builder r90, boolean r91, boolean r92) {
        /*
        r89 = this;
        r13 = r89;
        r12 = r90.mText;
        r11 = r90.mStart;
        r10 = r90.mEnd;
        r9 = r90.mPaint;
        r46 = r90.mWidth;
        r8 = r90.mTextDir;
        r47 = r90.mFallbackLineSpacing;
        r48 = r90.mSpacingMult;
        r49 = r90.mSpacingAdd;
        r0 = r90.mEllipsizedWidth;
        r7 = (float) r0;
        r15 = r90.mEllipsize;
        r50 = r90.mAddLastLineLineSpacing;
        r14 = 0;
        r17 = 0;
        r18 = 0;
        r19 = 0;
        r20 = 0;
        r21 = 0;
        r22 = 0;
        r6 = 0;
        r13.mLineCount = r6;
        r13.mEllipsized = r6;
        r0 = r13.mMaximumVisibleLineCount;
        r5 = 1;
        if (r0 >= r5) goto L_0x004c;
    L_0x004a:
        r0 = r6;
        goto L_0x004d;
    L_0x004c:
        r0 = -1;
    L_0x004d:
        r13.mMaxLineHeight = r0;
        r23 = 0;
        r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = (r48 > r0 ? 1 : (r48 == r0 ? 0 : -1));
        if (r0 != 0) goto L_0x0060;
    L_0x0057:
        r0 = 0;
        r0 = (r49 > r0 ? 1 : (r49 == r0 ? 0 : -1));
        if (r0 == 0) goto L_0x005d;
    L_0x005c:
        goto L_0x0060;
    L_0x005d:
        r16 = r6;
        goto L_0x0062;
    L_0x0060:
        r16 = r5;
    L_0x0062:
        r4 = r90.mFontMetricsInt;
        r24 = 0;
        r0 = r13.mLeftIndents;
        if (r0 != 0) goto L_0x0074;
    L_0x006c:
        r0 = r13.mRightIndents;
        if (r0 == 0) goto L_0x0071;
    L_0x0070:
        goto L_0x0074;
    L_0x0071:
        r0 = 0;
        r6 = r0;
        goto L_0x00ae;
    L_0x0074:
        r0 = r13.mLeftIndents;
        if (r0 != 0) goto L_0x007a;
    L_0x0078:
        r0 = r6;
        goto L_0x007b;
    L_0x007a:
        r0 = r0.length;
    L_0x007b:
        r1 = r13.mRightIndents;
        if (r1 != 0) goto L_0x0081;
    L_0x007f:
        r1 = r6;
        goto L_0x0082;
    L_0x0081:
        r1 = r1.length;
    L_0x0082:
        r2 = java.lang.Math.max(r0, r1);
        r3 = new int[r2];
        r25 = 0;
        r5 = r25;
    L_0x008c:
        if (r5 >= r0) goto L_0x0098;
    L_0x008e:
        r6 = r13.mLeftIndents;
        r6 = r6[r5];
        r3[r5] = r6;
        r5 = r5 + 1;
        r6 = 0;
        goto L_0x008c;
    L_0x0098:
        r5 = 0;
    L_0x0099:
        if (r5 >= r1) goto L_0x00ab;
    L_0x009b:
        r6 = r3[r5];
        r27 = r0;
        r0 = r13.mRightIndents;
        r0 = r0[r5];
        r6 = r6 + r0;
        r3[r5] = r6;
        r5 = r5 + 1;
        r0 = r27;
        goto L_0x0099;
    L_0x00ab:
        r27 = r0;
        r6 = r3;
    L_0x00ae:
        r0 = new android.graphics.text.LineBreaker$Builder;
        r0.<init>();
        r1 = r90.mBreakStrategy;
        r0 = r0.setBreakStrategy(r1);
        r1 = r90.mHyphenationFrequency;
        r0 = r0.setHyphenationFrequency(r1);
        r1 = r90.mJustificationMode;
        r0 = r0.setJustificationMode(r1);
        r0 = r0.setIndents(r6);
        r5 = r0.build();
        r0 = new android.graphics.text.LineBreaker$ParagraphConstraints;
        r0.<init>();
        r3 = r0;
        r27 = 0;
        r0 = r12 instanceof android.text.Spanned;
        r2 = 0;
        if (r0 == 0) goto L_0x00e4;
    L_0x00e0:
        r0 = r12;
        r0 = (android.text.Spanned) r0;
        goto L_0x00e5;
    L_0x00e4:
        r0 = r2;
    L_0x00e5:
        r1 = r0;
        r0 = r12 instanceof android.text.PrecomputedText;
        r29 = r7;
        if (r0 == 0) goto L_0x0149;
    L_0x00ec:
        r0 = r12;
        r0 = (android.text.PrecomputedText) r0;
        r28 = r90.mBreakStrategy;
        r30 = r90.mHyphenationFrequency;
        r31 = r0;
        r51 = r1;
        r1 = r11;
        r2 = r10;
        r53 = r3;
        r3 = r8;
        r54 = r4;
        r4 = r9;
        r55 = r5;
        r7 = 1;
        r5 = r28;
        r56 = r6;
        r6 = r30;
        r0 = r0.checkResultUsable(r1, r2, r3, r4, r5, r6);
        if (r0 == 0) goto L_0x0146;
    L_0x0113:
        if (r0 == r7) goto L_0x011e;
    L_0x0115:
        r1 = 2;
        if (r0 == r1) goto L_0x0119;
    L_0x0118:
        goto L_0x0154;
    L_0x0119:
        r27 = r31.getParagraphInfo();
        goto L_0x0154;
    L_0x011e:
        r1 = new android.text.PrecomputedText$Params$Builder;
        r1.<init>(r9);
        r2 = r90.mBreakStrategy;
        r1 = r1.setBreakStrategy(r2);
        r2 = r90.mHyphenationFrequency;
        r1 = r1.setHyphenationFrequency(r2);
        r1 = r1.setTextDirection(r8);
        r1 = r1.build();
        r2 = r31;
        r2 = android.text.PrecomputedText.create(r2, r1);
        r27 = r2.getParagraphInfo();
        goto L_0x0154;
    L_0x0146:
        r2 = r31;
        goto L_0x0154;
    L_0x0149:
        r51 = r1;
        r53 = r3;
        r54 = r4;
        r55 = r5;
        r56 = r6;
        r7 = 1;
    L_0x0154:
        if (r27 != 0) goto L_0x016b;
    L_0x0156:
        r0 = new android.text.PrecomputedText$Params;
        r1 = r90.mBreakStrategy;
        r2 = r90.mHyphenationFrequency;
        r0.<init>(r9, r8, r1, r2);
        r6 = 0;
        r27 = android.text.PrecomputedText.createMeasuredParagraphs(r12, r0, r11, r10, r6);
        r3 = r27;
        goto L_0x016e;
    L_0x016b:
        r6 = 0;
        r3 = r27;
    L_0x016e:
        r0 = 0;
        r2 = r0;
        r0 = r24;
    L_0x0172:
        r1 = r3.length;
        if (r2 >= r1) goto L_0x0627;
    L_0x0175:
        if (r2 != 0) goto L_0x0179;
    L_0x0177:
        r1 = r11;
        goto L_0x017f;
    L_0x0179:
        r1 = r2 + -1;
        r1 = r3[r1];
        r1 = r1.paragraphEnd;
    L_0x017f:
        r4 = r3[r2];
        r5 = r4.paragraphEnd;
        r4 = 1;
        r24 = r46;
        r25 = r46;
        r26 = 0;
        r6 = r51;
        if (r6 == 0) goto L_0x021e;
    L_0x018e:
        r7 = android.text.style.LeadingMarginSpan.class;
        r7 = android.text.Layout.getParagraphSpans(r6, r1, r5, r7);
        r7 = (android.text.style.LeadingMarginSpan[]) r7;
        r28 = 0;
        r30 = r8;
        r8 = r4;
        r4 = r28;
    L_0x019d:
        r31 = r9;
        r9 = r7.length;
        if (r4 >= r9) goto L_0x01d3;
    L_0x01a2:
        r9 = r7[r4];
        r33 = r11;
        r11 = r7[r4];
        r34 = r12;
        r12 = 1;
        r11 = r11.getLeadingMargin(r12);
        r24 = r24 - r11;
        r11 = r7[r4];
        r12 = 0;
        r11 = r11.getLeadingMargin(r12);
        r25 = r25 - r11;
        r11 = r9 instanceof android.text.style.LeadingMarginSpan.LeadingMarginSpan2;
        if (r11 == 0) goto L_0x01ca;
    L_0x01be:
        r11 = r9;
        r11 = (android.text.style.LeadingMarginSpan.LeadingMarginSpan2) r11;
        r12 = r11.getLeadingMarginLineCount();
        r8 = java.lang.Math.max(r8, r12);
    L_0x01ca:
        r4 = r4 + 1;
        r9 = r31;
        r11 = r33;
        r12 = r34;
        goto L_0x019d;
    L_0x01d3:
        r33 = r11;
        r34 = r12;
        r4 = android.text.style.LineHeightSpan.class;
        r4 = android.text.Layout.getParagraphSpans(r6, r1, r5, r4);
        r4 = (android.text.style.LineHeightSpan[]) r4;
        r9 = r4.length;
        if (r9 != 0) goto L_0x01ed;
    L_0x01e2:
        r4 = 0;
        r35 = r0;
        r36 = r4;
        r12 = r8;
        r11 = r24;
        r9 = r25;
        goto L_0x022f;
    L_0x01ed:
        if (r0 == 0) goto L_0x01f3;
    L_0x01ef:
        r9 = r0.length;
        r11 = r4.length;
        if (r9 >= r11) goto L_0x01f8;
    L_0x01f3:
        r9 = r4.length;
        r0 = com.android.internal.util.ArrayUtils.newUnpaddedIntArray(r9);
    L_0x01f8:
        r9 = 0;
    L_0x01f9:
        r11 = r4.length;
        if (r9 >= r11) goto L_0x0214;
    L_0x01fc:
        r11 = r4[r9];
        r11 = r6.getSpanStart(r11);
        if (r11 >= r1) goto L_0x020f;
    L_0x0204:
        r12 = r13.getLineForOffset(r11);
        r12 = r13.getLineTop(r12);
        r0[r9] = r12;
        goto L_0x0211;
    L_0x020f:
        r0[r9] = r23;
    L_0x0211:
        r9 = r9 + 1;
        goto L_0x01f9;
    L_0x0214:
        r35 = r0;
        r36 = r4;
        r12 = r8;
        r11 = r24;
        r9 = r25;
        goto L_0x022f;
    L_0x021e:
        r30 = r8;
        r31 = r9;
        r33 = r11;
        r34 = r12;
        r35 = r0;
        r12 = r4;
        r11 = r24;
        r9 = r25;
        r36 = r26;
    L_0x022f:
        r0 = 0;
        if (r6 == 0) goto L_0x025f;
    L_0x0232:
        r4 = android.text.style.TabStopSpan.class;
        r4 = android.text.Layout.getParagraphSpans(r6, r1, r5, r4);
        r4 = (android.text.style.TabStopSpan[]) r4;
        r7 = r4.length;
        if (r7 <= 0) goto L_0x025c;
    L_0x023d:
        r7 = r4.length;
        r7 = new float[r7];
        r8 = 0;
    L_0x0241:
        r24 = r0;
        r0 = r4.length;
        if (r8 >= r0) goto L_0x0254;
    L_0x0246:
        r0 = r4[r8];
        r0 = r0.getTabStop();
        r0 = (float) r0;
        r7[r8] = r0;
        r8 = r8 + 1;
        r0 = r24;
        goto L_0x0241;
    L_0x0254:
        r0 = r7.length;
        r8 = 0;
        java.util.Arrays.sort(r7, r8, r0);
        r0 = r7;
        r8 = r0;
        goto L_0x0263;
    L_0x025c:
        r24 = r0;
        goto L_0x0261;
    L_0x025f:
        r24 = r0;
    L_0x0261:
        r8 = r24;
    L_0x0263:
        r0 = r3[r2];
        r7 = r0.measured;
        r37 = r7.getChars();
        r0 = r7.getSpanEndCache();
        r38 = r0.getRawArray();
        r0 = r7.getFontMetrics();
        r39 = r0.getRawArray();
        r0 = (float) r9;
        r4 = r53;
        r4.setWidth(r0);
        r0 = (float) r11;
        r4.setIndent(r0, r12);
        r0 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4.setTabStops(r8, r0);
        r0 = r7.getMeasuredText();
        r24 = r2;
        r2 = r13.mLineCount;
        r25 = r12;
        r12 = r55;
        r2 = r12.computeLineBreaks(r0, r4, r2);
        r0 = r2.getLineCount();
        if (r14 >= r0) goto L_0x02c9;
    L_0x02a1:
        r14 = r0;
        r40 = r3;
        r3 = new int[r14];
        r17 = r3;
        r3 = new float[r14];
        r18 = r3;
        r3 = new float[r14];
        r19 = r3;
        r3 = new float[r14];
        r20 = r3;
        r3 = new boolean[r14];
        r21 = r3;
        r3 = new int[r14];
        r51 = r3;
        r53 = r14;
        r41 = r17;
        r42 = r18;
        r43 = r19;
        r44 = r20;
        r45 = r21;
        goto L_0x02d9;
    L_0x02c9:
        r40 = r3;
        r53 = r14;
        r41 = r17;
        r42 = r18;
        r43 = r19;
        r44 = r20;
        r45 = r21;
        r51 = r22;
    L_0x02d9:
        r3 = 0;
    L_0x02da:
        if (r3 >= r0) goto L_0x0310;
    L_0x02dc:
        r14 = r2.getLineBreakOffset(r3);
        r41[r3] = r14;
        r14 = r2.getLineWidth(r3);
        r42[r3] = r14;
        r14 = r2.getLineAscent(r3);
        r43[r3] = r14;
        r14 = r2.getLineDescent(r3);
        r44[r3] = r14;
        r14 = r2.hasLineTab(r3);
        r45[r3] = r14;
        r14 = r2.getStartLineHyphenEdit(r3);
        r26 = r4;
        r4 = r2.getEndLineHyphenEdit(r3);
        r4 = packHyphenEdit(r14, r4);
        r51[r3] = r4;
        r3 = r3 + 1;
        r4 = r26;
        goto L_0x02da;
    L_0x0310:
        r26 = r4;
        r3 = r13.mMaximumVisibleLineCount;
        r4 = r13.mLineCount;
        r3 = r3 - r4;
        if (r15 == 0) goto L_0x0328;
    L_0x0319:
        r4 = android.text.TextUtils.TruncateAt.END;
        if (r15 == r4) goto L_0x0326;
    L_0x031d:
        r4 = r13.mMaximumVisibleLineCount;
        r14 = 1;
        if (r4 != r14) goto L_0x0328;
    L_0x0322:
        r4 = android.text.TextUtils.TruncateAt.MARQUEE;
        if (r15 == r4) goto L_0x0328;
    L_0x0326:
        r4 = 1;
        goto L_0x0329;
    L_0x0328:
        r4 = 0;
    L_0x0329:
        r55 = r4;
        if (r3 <= 0) goto L_0x0384;
    L_0x032d:
        if (r3 >= r0) goto L_0x0384;
    L_0x032f:
        if (r55 == 0) goto L_0x0384;
    L_0x0331:
        r4 = 0;
        r14 = 0;
        r17 = r3 + -1;
        r87 = r14;
        r14 = r4;
        r4 = r17;
        r17 = r87;
    L_0x033c:
        if (r4 >= r0) goto L_0x036d;
    L_0x033e:
        r58 = r2;
        r2 = r0 + -1;
        if (r4 != r2) goto L_0x034a;
    L_0x0344:
        r2 = r42[r4];
        r14 = r14 + r2;
        r59 = r6;
        goto L_0x0362;
    L_0x034a:
        if (r4 != 0) goto L_0x034e;
    L_0x034c:
        r2 = 0;
        goto L_0x0352;
    L_0x034e:
        r2 = r4 + -1;
        r2 = r41[r2];
    L_0x0352:
        r59 = r6;
        r6 = r41[r4];
        if (r2 >= r6) goto L_0x0362;
    L_0x0358:
        r6 = r7.getCharWidthAt(r2);
        r14 = r14 + r6;
        r2 = r2 + 1;
        r6 = r59;
        goto L_0x0352;
    L_0x0362:
        r2 = r45[r4];
        r17 = r17 | r2;
        r4 = r4 + 1;
        r2 = r58;
        r6 = r59;
        goto L_0x033c;
    L_0x036d:
        r58 = r2;
        r59 = r6;
        r2 = r3 + -1;
        r4 = r0 + -1;
        r4 = r41[r4];
        r41[r2] = r4;
        r2 = r3 + -1;
        r42[r2] = r14;
        r2 = r3 + -1;
        r45[r2] = r17;
        r0 = r3;
        r6 = r0;
        goto L_0x0389;
    L_0x0384:
        r58 = r2;
        r59 = r6;
        r6 = r0;
    L_0x0389:
        r0 = r1;
        r2 = 0;
        r4 = 0;
        r14 = 0;
        r17 = 0;
        r18 = 0;
        r19 = 0;
        r20 = 0;
        r21 = r1;
        r87 = r20;
        r20 = r0;
        r0 = r2;
        r2 = r21;
        r21 = r87;
        r88 = r17;
        r17 = r3;
        r3 = r14;
        r14 = r4;
        r4 = r88;
    L_0x03a8:
        if (r2 >= r5) goto L_0x05ab;
    L_0x03aa:
        r60 = r19 + 1;
        r13 = r38[r19];
        r19 = r18 * 4;
        r22 = 0;
        r19 = r19 + 0;
        r57 = r5;
        r5 = r39[r19];
        r61 = r12;
        r12 = r54;
        r12.top = r5;
        r5 = r18 * 4;
        r19 = 1;
        r5 = r5 + 1;
        r5 = r39[r5];
        r12.bottom = r5;
        r5 = r18 * 4;
        r27 = 2;
        r5 = r5 + 2;
        r5 = r39[r5];
        r12.ascent = r5;
        r5 = r18 * 4;
        r5 = r5 + 3;
        r5 = r39[r5];
        r12.descent = r5;
        r32 = r18 + 1;
        r5 = r12.top;
        if (r5 >= r0) goto L_0x03e2;
    L_0x03e0:
        r0 = r12.top;
    L_0x03e2:
        r5 = r12.ascent;
        if (r5 >= r3) goto L_0x03e8;
    L_0x03e6:
        r3 = r12.ascent;
    L_0x03e8:
        r5 = r12.descent;
        if (r5 <= r4) goto L_0x03ee;
    L_0x03ec:
        r4 = r12.descent;
    L_0x03ee:
        r5 = r12.bottom;
        if (r5 <= r14) goto L_0x03f7;
    L_0x03f2:
        r14 = r12.bottom;
        r5 = r21;
        goto L_0x03f9;
    L_0x03f7:
        r5 = r21;
    L_0x03f9:
        if (r5 >= r6) goto L_0x0408;
    L_0x03fb:
        r18 = r41[r5];
        r28 = r0;
        r0 = r1 + r18;
        if (r0 >= r2) goto L_0x040a;
    L_0x0403:
        r5 = r5 + 1;
        r0 = r28;
        goto L_0x03f9;
    L_0x0408:
        r28 = r0;
    L_0x040a:
        r0 = r3;
        r3 = r5;
        r64 = r14;
        r62 = r20;
        r54 = r23;
        r63 = r28;
        r14 = r4;
    L_0x0415:
        if (r3 >= r6) goto L_0x052c;
    L_0x0417:
        r4 = r41[r3];
        r4 = r4 + r1;
        if (r4 > r13) goto L_0x052c;
    L_0x041c:
        r4 = r41[r3];
        r5 = r1 + r4;
        if (r5 >= r10) goto L_0x0425;
    L_0x0422:
        r28 = r19;
        goto L_0x0427;
    L_0x0425:
        r28 = r22;
    L_0x0427:
        if (r47 == 0) goto L_0x0434;
    L_0x0429:
        r4 = r43[r3];
        r4 = java.lang.Math.round(r4);
        r4 = java.lang.Math.min(r0, r4);
        goto L_0x0435;
    L_0x0434:
        r4 = r0;
    L_0x0435:
        r65 = r26;
        if (r47 == 0) goto L_0x0446;
    L_0x0439:
        r18 = r44[r3];
        r20 = r0;
        r0 = java.lang.Math.round(r18);
        r0 = java.lang.Math.max(r14, r0);
        goto L_0x0449;
    L_0x0446:
        r20 = r0;
        r0 = r14;
    L_0x0449:
        r66 = r57;
        r57 = r5;
        r5 = r0;
        r0 = r45[r3];
        r67 = r14;
        r14 = r0;
        r0 = r51[r3];
        r68 = r15;
        r15 = r0;
        r26 = r42[r3];
        r69 = r20;
        r0 = r89;
        r70 = r1;
        r1 = r34;
        r72 = r2;
        r71 = r58;
        r58 = r24;
        r2 = r62;
        r74 = r3;
        r73 = r40;
        r40 = r17;
        r3 = r57;
        r75 = r6;
        r76 = r22;
        r6 = r63;
        r79 = r19;
        r78 = r27;
        r77 = r29;
        r29 = r7;
        r7 = r64;
        r80 = r30;
        r30 = r8;
        r8 = r54;
        r81 = r31;
        r31 = r9;
        r9 = r48;
        r82 = r10;
        r10 = r49;
        r83 = r33;
        r33 = r11;
        r11 = r36;
        r84 = r34;
        r85 = r61;
        r61 = r12;
        r34 = r25;
        r12 = r35;
        r86 = r13;
        r13 = r61;
        r17 = r29;
        r18 = r82;
        r19 = r91;
        r20 = r92;
        r21 = r50;
        r22 = r37;
        r23 = r70;
        r24 = r68;
        r25 = r77;
        r27 = r81;
        r54 = r0.out(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28);
        r1 = r57;
        r0 = r86;
        if (r1 >= r0) goto L_0x04d5;
    L_0x04c4:
        r2 = r61;
        r3 = r2.top;
        r6 = r2.bottom;
        r7 = r2.ascent;
        r8 = r2.descent;
        r63 = r3;
        r64 = r6;
        r6 = r7;
        r14 = r8;
        goto L_0x04e4;
    L_0x04d5:
        r2 = r61;
        r3 = r76;
        r6 = r76;
        r7 = r76;
        r8 = r76;
        r14 = r3;
        r64 = r7;
        r63 = r8;
    L_0x04e4:
        r62 = r1;
        r3 = r74 + 1;
        r7 = r89;
        r8 = r7.mLineCount;
        r9 = r7.mMaximumVisibleLineCount;
        if (r8 < r9) goto L_0x04f5;
    L_0x04f0:
        r8 = r7.mEllipsized;
        if (r8 == 0) goto L_0x04f5;
    L_0x04f4:
        return;
    L_0x04f5:
        r13 = r0;
        r12 = r2;
        r0 = r6;
        r7 = r29;
        r8 = r30;
        r9 = r31;
        r11 = r33;
        r25 = r34;
        r17 = r40;
        r24 = r58;
        r26 = r65;
        r57 = r66;
        r15 = r68;
        r1 = r70;
        r58 = r71;
        r2 = r72;
        r40 = r73;
        r6 = r75;
        r22 = r76;
        r29 = r77;
        r27 = r78;
        r19 = r79;
        r30 = r80;
        r31 = r81;
        r10 = r82;
        r33 = r83;
        r34 = r84;
        r61 = r85;
        goto L_0x0415;
    L_0x052c:
        r69 = r0;
        r70 = r1;
        r72 = r2;
        r74 = r3;
        r75 = r6;
        r82 = r10;
        r2 = r12;
        r0 = r13;
        r67 = r14;
        r68 = r15;
        r79 = r19;
        r76 = r22;
        r65 = r26;
        r78 = r27;
        r77 = r29;
        r80 = r30;
        r81 = r31;
        r83 = r33;
        r84 = r34;
        r73 = r40;
        r66 = r57;
        r71 = r58;
        r85 = r61;
        r29 = r7;
        r30 = r8;
        r31 = r9;
        r33 = r11;
        r40 = r17;
        r58 = r24;
        r34 = r25;
        r7 = r89;
        r1 = r0;
        r13 = r7;
        r7 = r29;
        r8 = r30;
        r9 = r31;
        r18 = r32;
        r11 = r33;
        r25 = r34;
        r17 = r40;
        r23 = r54;
        r24 = r58;
        r19 = r60;
        r20 = r62;
        r0 = r63;
        r14 = r64;
        r26 = r65;
        r5 = r66;
        r4 = r67;
        r15 = r68;
        r3 = r69;
        r58 = r71;
        r40 = r73;
        r21 = r74;
        r6 = r75;
        r29 = r77;
        r30 = r80;
        r31 = r81;
        r10 = r82;
        r33 = r83;
        r34 = r84;
        r12 = r85;
        r54 = r2;
        r2 = r1;
        r1 = r70;
        goto L_0x03a8;
    L_0x05ab:
        r70 = r1;
        r72 = r2;
        r66 = r5;
        r75 = r6;
        r82 = r10;
        r85 = r12;
        r68 = r15;
        r65 = r26;
        r77 = r29;
        r80 = r30;
        r81 = r31;
        r83 = r33;
        r84 = r34;
        r73 = r40;
        r2 = r54;
        r71 = r58;
        r76 = 0;
        r78 = 2;
        r79 = 1;
        r29 = r7;
        r30 = r8;
        r31 = r9;
        r33 = r11;
        r7 = r13;
        r40 = r17;
        r58 = r24;
        r34 = r25;
        r1 = r82;
        if (r5 != r1) goto L_0x05f6;
    L_0x05e4:
        r8 = r23;
        r9 = r35;
        r0 = r41;
        r3 = r42;
        r4 = r43;
        r5 = r44;
        r6 = r45;
        r14 = r53;
        goto L_0x0650;
    L_0x05f6:
        r0 = r58 + 1;
        r10 = r1;
        r54 = r2;
        r13 = r7;
        r17 = r41;
        r18 = r42;
        r19 = r43;
        r20 = r44;
        r21 = r45;
        r22 = r51;
        r14 = r53;
        r51 = r59;
        r53 = r65;
        r15 = r68;
        r3 = r73;
        r6 = r76;
        r29 = r77;
        r7 = r79;
        r8 = r80;
        r9 = r81;
        r11 = r83;
        r12 = r84;
        r55 = r85;
        r2 = r0;
        r0 = r35;
        goto L_0x0172;
    L_0x0627:
        r58 = r2;
        r73 = r3;
        r80 = r8;
        r81 = r9;
        r1 = r10;
        r83 = r11;
        r84 = r12;
        r7 = r13;
        r68 = r15;
        r77 = r29;
        r59 = r51;
        r65 = r53;
        r2 = r54;
        r85 = r55;
        r9 = r0;
        r0 = r17;
        r3 = r18;
        r4 = r19;
        r5 = r20;
        r6 = r21;
        r51 = r22;
        r8 = r23;
    L_0x0650:
        r10 = r83;
        if (r1 == r10) goto L_0x0669;
    L_0x0654:
        r11 = r1 + -1;
        r12 = r84;
        r11 = r12.charAt(r11);
        r13 = 10;
        if (r11 != r13) goto L_0x0661;
    L_0x0660:
        goto L_0x066b;
    L_0x0661:
        r52 = r0;
        r11 = r80;
        r15 = r81;
        goto L_0x06cd;
    L_0x0669:
        r12 = r84;
    L_0x066b:
        r11 = r7.mLineCount;
        r13 = r7.mMaximumVisibleLineCount;
        if (r11 >= r13) goto L_0x06c7;
        r11 = r80;
        r13 = 0;
        r13 = android.text.MeasuredParagraph.buildForBidi(r12, r1, r1, r11, r13);
        r34 = r13;
        r15 = r81;
        r15.getFontMetricsInt(r2);
        r52 = r0;
        r0 = r2.ascent;
        r21 = r0;
        r0 = r2.descent;
        r22 = r0;
        r0 = r2.top;
        r23 = r0;
        r0 = r2.bottom;
        r24 = r0;
        r28 = 0;
        r29 = 0;
        r31 = 0;
        r32 = 0;
        r39 = 0;
        r43 = 0;
        r45 = 0;
        r17 = r89;
        r18 = r12;
        r19 = r1;
        r20 = r1;
        r25 = r8;
        r26 = r48;
        r27 = r49;
        r30 = r2;
        r33 = r16;
        r35 = r1;
        r36 = r91;
        r37 = r92;
        r38 = r50;
        r40 = r10;
        r41 = r68;
        r42 = r77;
        r44 = r15;
        r8 = r17.out(r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28, r29, r30, r31, r32, r33, r34, r35, r36, r37, r38, r39, r40, r41, r42, r43, r44, r45);
        goto L_0x06cd;
    L_0x06c7:
        r52 = r0;
        r11 = r80;
        r15 = r81;
    L_0x06cd:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.StaticLayout.generate(android.text.StaticLayout$Builder, boolean, boolean):void");
    }

    private int out(CharSequence text, int start, int end, int above, int below, int top, int bottom, int v, float spacingmult, float spacingadd, LineHeightSpan[] chooseHt, int[] chooseHtv, FontMetricsInt fm, boolean hasTab, int hyphenEdit, boolean needMultiply, MeasuredParagraph measured, int bufEnd, boolean includePad, boolean trackPad, boolean addLastLineLineSpacing, char[] chs, int widthStart, TruncateAt ellipsize, float ellipsisWidth, float textWidth, TextPaint paint, boolean moreChars) {
        int[] lines;
        boolean z;
        int j;
        int i;
        int i2;
        int i3;
        int above2;
        int below2;
        int top2;
        int bottom2;
        boolean z2;
        int i4;
        int extra;
        int i5 = start;
        int i6 = end;
        LineHeightSpan[] lineHeightSpanArr = chooseHt;
        FontMetricsInt fontMetricsInt = fm;
        int i7 = bufEnd;
        int i8 = widthStart;
        TruncateAt truncateAt = ellipsize;
        int j2 = this.mLineCount;
        int i9 = this.mColumns;
        int off = j2 * i9;
        boolean z3 = true;
        int want = (off + i9) + 1;
        int[] lines2 = this.mLines;
        int dir = measured.getParagraphDir();
        if (want >= lines2.length) {
            int[] grow = ArrayUtils.newUnpaddedIntArray(GrowingArrayUtils.growSize(want));
            System.arraycopy(lines2, 0, grow, 0, lines2.length);
            this.mLines = grow;
            lines = grow;
        } else {
            lines = lines2;
        }
        if (j2 >= this.mLineDirections.length) {
            Directions[] grow2 = (Directions[]) ArrayUtils.newUnpaddedArray(Directions.class, GrowingArrayUtils.growSize(j2));
            Directions[] directionsArr = this.mLineDirections;
            System.arraycopy(directionsArr, 0, grow2, 0, directionsArr.length);
            this.mLineDirections = grow2;
        }
        int want2;
        if (lineHeightSpanArr != null) {
            fontMetricsInt.ascent = above;
            fontMetricsInt.descent = below;
            fontMetricsInt.top = top;
            fontMetricsInt.bottom = bottom;
            i8 = 0;
            while (i8 < lineHeightSpanArr.length) {
                Object obj;
                if (lineHeightSpanArr[i8] instanceof WithDensity) {
                    obj = null;
                    want2 = want;
                    z = z3;
                    j = j2;
                    ((WithDensity) lineHeightSpanArr[i8]).chooseHeight(text, start, end, chooseHtv[i8], v, fm, paint);
                } else {
                    want2 = want;
                    z = z3;
                    j = j2;
                    obj = null;
                    lineHeightSpanArr[i8].chooseHeight(text, start, end, chooseHtv[i8], v, fm);
                }
                i8++;
                i = above;
                i2 = below;
                int i10 = top;
                i9 = bottom;
                z3 = z;
                Object obj2 = obj;
                want = want2;
                j2 = j;
                i5 = start;
            }
            z = z3;
            j = j2;
            i3 = 0;
            above2 = fontMetricsInt.ascent;
            below2 = fontMetricsInt.descent;
            top2 = fontMetricsInt.top;
            bottom2 = fontMetricsInt.bottom;
        } else {
            i3 = 0;
            want2 = want;
            z = true;
            j = j2;
            above2 = above;
            below2 = below;
            top2 = top;
            bottom2 = bottom;
        }
        boolean firstLine = j == 0 ? z : i3;
        boolean currentLineIsTheLastVisibleOne = j + 1 == this.mMaximumVisibleLineCount ? z : i3;
        if (truncateAt != null) {
            z2 = (moreChars && this.mLineCount + z == this.mMaximumVisibleLineCount) ? z : i3;
            TruncateAt truncateAt2 = truncateAt;
            boolean forceEllipsis = z2;
            z2 = (((!(this.mMaximumVisibleLineCount == z && moreChars) && (!firstLine || moreChars)) || truncateAt2 == TruncateAt.MARQUEE) && (firstLine || ((!currentLineIsTheLastVisibleOne && moreChars) || truncateAt2 != TruncateAt.END))) ? i3 : z;
            if (z2) {
                i5 = widthStart;
                i4 = i7;
                calculateEllipsis(start, end, measured, widthStart, ellipsisWidth, ellipsize, j, textWidth, paint, forceEllipsis);
            } else {
                i5 = widthStart;
                i4 = i7;
            }
        } else {
            i5 = widthStart;
            i4 = i7;
        }
        CharSequence charSequence;
        if (this.mEllipsized) {
            z2 = true;
            charSequence = text;
            i2 = start;
            i = 1;
        } else {
            if (i5 == i4 || i4 <= 0) {
                charSequence = text;
            } else {
                if (text.charAt(i4 - 1) == 10) {
                    z2 = true;
                    if (i6 == i4 || lastCharIsNewLine) {
                        i2 = start;
                        i = 1;
                        if (i2 == i4 || !lastCharIsNewLine) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                    } else {
                        z2 = true;
                        i = 1;
                        i2 = start;
                    }
                }
            }
            z2 = i3;
            if (i6 == i4) {
            }
            i2 = start;
            i = 1;
            if (i2 == i4) {
            }
            z2 = false;
        }
        if (firstLine) {
            if (trackPad) {
                this.mTopPadding = top2 - above2;
            }
            if (includePad) {
                above2 = top2;
            }
        }
        if (z2) {
            if (trackPad) {
                this.mBottomPadding = bottom2 - below2;
            }
            if (includePad) {
                below2 = bottom2;
            }
        }
        if (!needMultiply || (!addLastLineLineSpacing && z2)) {
            extra = 0;
        } else {
            double ex = (double) ((((float) (below2 - above2)) * (spacingmult - 1.0f)) + spacingadd);
            if (ex >= 0.0d) {
                extra = (int) (EXTRA_ROUNDING + ex);
            } else {
                extra = -((int) ((-ex) + EXTRA_ROUNDING));
            }
        }
        lines[off + 0] = i2;
        lines[off + 1] = v;
        lines[off + 2] = below2 + extra;
        lines[off + 3] = extra;
        if (!this.mEllipsized && currentLineIsTheLastVisibleOne) {
            this.mMaxLineHeight = v + ((includePad ? bottom2 : below2) - above2);
        }
        int v2 = v + ((below2 - above2) + extra);
        want = this.mColumns;
        lines[(off + want) + 0] = i6;
        lines[(off + want) + i] = v2;
        want = off + 0;
        j2 = lines[want];
        if (hasTab) {
            i3 = 536870912;
        }
        lines[want] = j2 | i3;
        lines[off + 4] = hyphenEdit;
        want = off + 0;
        lines[want] = lines[want] | (dir << 30);
        this.mLineDirections[j] = measured.getDirections(i2 - i5, i6 - i5);
        this.mLineCount += i;
        return v2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00cf  */
    private void calculateEllipsis(int r19, int r20, android.text.MeasuredParagraph r21, int r22, float r23, android.text.TextUtils.TruncateAt r24, int r25, float r26, android.text.TextPaint r27, boolean r28) {
        /*
        r18 = this;
        r0 = r18;
        r1 = r21;
        r2 = r24;
        r3 = r25;
        r4 = r0.getTotalInsets(r3);
        r4 = r23 - r4;
        r5 = (r26 > r4 ? 1 : (r26 == r4 ? 0 : -1));
        r6 = 5;
        if (r5 > 0) goto L_0x0025;
    L_0x0013:
        if (r28 != 0) goto L_0x0025;
    L_0x0015:
        r5 = r0.mLines;
        r7 = r0.mColumns;
        r8 = r7 * r3;
        r8 = r8 + r6;
        r6 = 0;
        r5[r8] = r6;
        r7 = r7 * r3;
        r7 = r7 + 6;
        r5[r7] = r6;
        return;
    L_0x0025:
        r5 = android.text.TextUtils.getEllipsisString(r24);
        r7 = r27;
        r5 = r7.measureText(r5);
        r8 = 0;
        r9 = 0;
        r10 = r20 - r19;
        r11 = android.text.TextUtils.TruncateAt.START;
        r12 = 0;
        r13 = "StaticLayout";
        r14 = 1;
        if (r2 != r11) goto L_0x007d;
    L_0x003b:
        r11 = r0.mMaximumVisibleLineCount;
        if (r11 != r14) goto L_0x0070;
    L_0x003f:
        r11 = 0;
        r13 = r10;
    L_0x0041:
        if (r13 <= 0) goto L_0x006c;
    L_0x0043:
        r15 = r13 + -1;
        r15 = r15 + r19;
        r15 = r15 - r22;
        r15 = r1.getCharWidthAt(r15);
        r16 = r15 + r11;
        r16 = r16 + r5;
        r16 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1));
        if (r16 <= 0) goto L_0x0067;
    L_0x0055:
        if (r13 >= r10) goto L_0x006c;
    L_0x0057:
        r16 = r13 + r19;
        r14 = r16 - r22;
        r14 = r1.getCharWidthAt(r14);
        r14 = (r14 > r12 ? 1 : (r14 == r12 ? 0 : -1));
        if (r14 != 0) goto L_0x006c;
    L_0x0063:
        r13 = r13 + 1;
        r14 = 1;
        goto L_0x0055;
    L_0x0067:
        r11 = r11 + r15;
        r13 = r13 + -1;
        r14 = 1;
        goto L_0x0041;
    L_0x006c:
        r8 = 0;
        r9 = r13;
        goto L_0x0118;
    L_0x0070:
        r11 = android.util.Log.isLoggable(r13, r6);
        if (r11 == 0) goto L_0x0118;
    L_0x0076:
        r11 = "Start Ellipsis only supported with one line";
        android.util.Log.w(r13, r11);
        goto L_0x0118;
    L_0x007d:
        r11 = android.text.TextUtils.TruncateAt.END;
        if (r2 == r11) goto L_0x00f3;
    L_0x0081:
        r11 = android.text.TextUtils.TruncateAt.MARQUEE;
        if (r2 == r11) goto L_0x00f3;
    L_0x0085:
        r11 = android.text.TextUtils.TruncateAt.END_SMALL;
        if (r2 != r11) goto L_0x008b;
    L_0x0089:
        goto L_0x00f3;
    L_0x008b:
        r11 = r0.mMaximumVisibleLineCount;
        r14 = 1;
        if (r11 != r14) goto L_0x00e6;
    L_0x0090:
        r11 = 0;
        r13 = 0;
        r14 = 0;
        r15 = r10;
        r16 = r4 - r5;
        r17 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r16 = r16 / r17;
    L_0x009b:
        if (r15 <= 0) goto L_0x00c9;
    L_0x009d:
        r17 = r15 + -1;
        r17 = r17 + r19;
        r6 = r17 - r22;
        r6 = r1.getCharWidthAt(r6);
        r17 = r6 + r13;
        r17 = (r17 > r16 ? 1 : (r17 == r16 ? 0 : -1));
        if (r17 <= 0) goto L_0x00c2;
    L_0x00ad:
        if (r15 >= r10) goto L_0x00c9;
    L_0x00af:
        r17 = r15 + r19;
        r12 = r17 - r22;
        r12 = r1.getCharWidthAt(r12);
        r17 = 0;
        r12 = (r12 > r17 ? 1 : (r12 == r17 ? 0 : -1));
        if (r12 != 0) goto L_0x00c9;
    L_0x00bd:
        r15 = r15 + 1;
        r12 = r17;
        goto L_0x00ad;
    L_0x00c2:
        r17 = r12;
        r13 = r13 + r6;
        r15 = r15 + -1;
        r6 = 5;
        goto L_0x009b;
    L_0x00c9:
        r6 = r4 - r5;
        r6 = r6 - r13;
        r12 = 0;
    L_0x00cd:
        if (r12 >= r15) goto L_0x00e2;
    L_0x00cf:
        r14 = r12 + r19;
        r14 = r14 - r22;
        r14 = r1.getCharWidthAt(r14);
        r17 = r14 + r11;
        r17 = (r17 > r6 ? 1 : (r17 == r6 ? 0 : -1));
        if (r17 <= 0) goto L_0x00de;
    L_0x00dd:
        goto L_0x00e2;
    L_0x00de:
        r11 = r11 + r14;
        r12 = r12 + 1;
        goto L_0x00cd;
    L_0x00e2:
        r8 = r12;
        r9 = r15 - r12;
        goto L_0x0118;
    L_0x00e6:
        r6 = 5;
        r11 = android.util.Log.isLoggable(r13, r6);
        if (r11 == 0) goto L_0x0118;
    L_0x00ed:
        r6 = "Middle Ellipsis only supported with one line";
        android.util.Log.w(r13, r6);
        goto L_0x0118;
    L_0x00f3:
        r6 = 0;
        r11 = 0;
    L_0x00f5:
        if (r11 >= r10) goto L_0x010b;
    L_0x00f7:
        r12 = r11 + r19;
        r12 = r12 - r22;
        r12 = r1.getCharWidthAt(r12);
        r13 = r12 + r6;
        r13 = r13 + r5;
        r13 = (r13 > r4 ? 1 : (r13 == r4 ? 0 : -1));
        if (r13 <= 0) goto L_0x0107;
    L_0x0106:
        goto L_0x010b;
    L_0x0107:
        r6 = r6 + r12;
        r11 = r11 + 1;
        goto L_0x00f5;
    L_0x010b:
        r8 = r11;
        r9 = r10 - r11;
        if (r28 == 0) goto L_0x0117;
    L_0x0110:
        if (r9 != 0) goto L_0x0117;
    L_0x0112:
        if (r10 <= 0) goto L_0x0117;
    L_0x0114:
        r8 = r10 + -1;
        r9 = 1;
    L_0x0118:
        r6 = 1;
        r0.mEllipsized = r6;
        r6 = r0.mLines;
        r11 = r0.mColumns;
        r12 = r11 * r3;
        r13 = 5;
        r12 = r12 + r13;
        r6[r12] = r8;
        r11 = r11 * r3;
        r11 = r11 + 6;
        r6[r11] = r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.StaticLayout.calculateEllipsis(int, int, android.text.MeasuredParagraph, int, float, android.text.TextUtils$TruncateAt, int, float, android.text.TextPaint, boolean):void");
    }

    private float getTotalInsets(int line) {
        int totalIndent = 0;
        int[] iArr = this.mLeftIndents;
        if (iArr != null) {
            totalIndent = iArr[Math.min(line, iArr.length - 1)];
        }
        iArr = this.mRightIndents;
        if (iArr != null) {
            totalIndent += iArr[Math.min(line, iArr.length - 1)];
        }
        return (float) totalIndent;
    }

    public int getLineForVertical(int vertical) {
        int high = this.mLineCount;
        int low = -1;
        int[] lines = this.mLines;
        while (high - low > 1) {
            int guess = (high + low) >> 1;
            if (lines[(this.mColumns * guess) + 1] > vertical) {
                high = guess;
            } else {
                low = guess;
            }
        }
        if (low < 0) {
            return 0;
        }
        return low;
    }

    public int getLineCount() {
        return this.mLineCount;
    }

    public int getLineTop(int line) {
        return this.mLines[(this.mColumns * line) + 1];
    }

    public int getLineExtra(int line) {
        return this.mLines[(this.mColumns * line) + 3];
    }

    public int getLineDescent(int line) {
        return this.mLines[(this.mColumns * line) + 2];
    }

    public int getLineStart(int line) {
        return this.mLines[(this.mColumns * line) + 0] & 536870911;
    }

    public int getParagraphDirection(int line) {
        return this.mLines[(this.mColumns * line) + 0] >> 30;
    }

    public boolean getLineContainsTab(int line) {
        return (this.mLines[(this.mColumns * line) + 0] & 536870912) != 0;
    }

    public final Directions getLineDirections(int line) {
        if (line <= getLineCount()) {
            return this.mLineDirections[line];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getTopPadding() {
        return this.mTopPadding;
    }

    public int getBottomPadding() {
        return this.mBottomPadding;
    }

    static int packHyphenEdit(int start, int end) {
        return (start << 3) | end;
    }

    static int unpackStartHyphenEdit(int packedHyphenEdit) {
        return (packedHyphenEdit & 24) >> 3;
    }

    static int unpackEndHyphenEdit(int packedHyphenEdit) {
        return packedHyphenEdit & 7;
    }

    public int getStartHyphenEdit(int lineNumber) {
        return unpackStartHyphenEdit(this.mLines[(this.mColumns * lineNumber) + 4] & 255);
    }

    public int getEndHyphenEdit(int lineNumber) {
        return unpackEndHyphenEdit(this.mLines[(this.mColumns * lineNumber) + 4] & 255);
    }

    public int getIndentAdjust(int line, Alignment align) {
        int[] iArr;
        if (align == Alignment.ALIGN_LEFT) {
            iArr = this.mLeftIndents;
            if (iArr == null) {
                return 0;
            }
            return iArr[Math.min(line, iArr.length - 1)];
        } else if (align == Alignment.ALIGN_RIGHT) {
            iArr = this.mRightIndents;
            if (iArr == null) {
                return 0;
            }
            return -iArr[Math.min(line, iArr.length - 1)];
        } else if (align == Alignment.ALIGN_CENTER) {
            int left = 0;
            int[] iArr2 = this.mLeftIndents;
            if (iArr2 != null) {
                left = iArr2[Math.min(line, iArr2.length - 1)];
            }
            int right = 0;
            int[] iArr3 = this.mRightIndents;
            if (iArr3 != null) {
                right = iArr3[Math.min(line, iArr3.length - 1)];
            }
            return (left - right) >> 1;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unhandled alignment ");
            stringBuilder.append(align);
            throw new AssertionError(stringBuilder.toString());
        }
    }

    public int getEllipsisCount(int line) {
        int i = this.mColumns;
        if (i < 7) {
            return 0;
        }
        return this.mLines[(i * line) + 6];
    }

    public int getEllipsisStart(int line) {
        int i = this.mColumns;
        if (i < 7) {
            return 0;
        }
        return this.mLines[(i * line) + 5];
    }

    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public int getHeight(boolean cap) {
        if (cap && this.mLineCount > this.mMaximumVisibleLineCount && this.mMaxLineHeight == -1) {
            String str = TAG;
            if (Log.isLoggable(str, 5)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("maxLineHeight should not be -1.  maxLines:");
                stringBuilder.append(this.mMaximumVisibleLineCount);
                stringBuilder.append(" lineCount:");
                stringBuilder.append(this.mLineCount);
                Log.w(str, stringBuilder.toString());
            }
        }
        if (cap && this.mLineCount > this.mMaximumVisibleLineCount) {
            int i = this.mMaxLineHeight;
            if (i != -1) {
                return i;
            }
        }
        return super.getHeight();
    }
}
