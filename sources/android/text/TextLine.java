package android.text;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.text.Layout.Directions;
import android.text.Layout.TabStops;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.ArrayUtils;
import java.util.ArrayList;

@VisibleForTesting(visibility = Visibility.PACKAGE)
public class TextLine {
    private static final boolean DEBUG = false;
    private static final char TAB_CHAR = '\t';
    private static final int TAB_INCREMENT = 20;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static final TextLine[] sCached = new TextLine[3];
    private final TextPaint mActivePaint = new TextPaint();
    private float mAddedWidthForJustify;
    @UnsupportedAppUsage
    private final SpanSet<CharacterStyle> mCharacterStyleSpanSet = new SpanSet(CharacterStyle.class);
    private char[] mChars;
    private boolean mCharsValid;
    private PrecomputedText mComputed;
    private final DecorationInfo mDecorationInfo = new DecorationInfo();
    private final ArrayList<DecorationInfo> mDecorations = new ArrayList();
    private int mDir;
    private Directions mDirections;
    private int mEllipsisEnd;
    private int mEllipsisStart;
    private boolean mHasTabs;
    private boolean mIsJustifying;
    private int mLen;
    @UnsupportedAppUsage
    private final SpanSet<MetricAffectingSpan> mMetricAffectingSpanSpanSet = new SpanSet(MetricAffectingSpan.class);
    private TextPaint mPaint;
    @UnsupportedAppUsage
    private final SpanSet<ReplacementSpan> mReplacementSpanSpanSet = new SpanSet(ReplacementSpan.class);
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private Spanned mSpanned;
    private int mStart;
    private TabStops mTabs;
    @UnsupportedAppUsage
    private CharSequence mText;
    private final TextPaint mWorkPaint = new TextPaint();

    private static final class DecorationInfo {
        public int end;
        public boolean isStrikeThruText;
        public boolean isUnderlineText;
        public int start;
        public int underlineColor;
        public float underlineThickness;

        private DecorationInfo() {
            this.start = -1;
            this.end = -1;
        }

        public boolean hasDecoration() {
            return this.isStrikeThruText || this.isUnderlineText || this.underlineColor != 0;
        }

        public DecorationInfo copyInfo() {
            DecorationInfo copy = new DecorationInfo();
            copy.isStrikeThruText = this.isStrikeThruText;
            copy.isUnderlineText = this.isUnderlineText;
            copy.underlineColor = this.underlineColor;
            copy.underlineThickness = this.underlineThickness;
            return copy;
        }
    }

    @VisibleForTesting(visibility = Visibility.PACKAGE)
    @UnsupportedAppUsage
    public static TextLine obtain() {
        synchronized (sCached) {
            int i = sCached.length;
            do {
                i--;
                if (i < 0) {
                    return new TextLine();
                }
            } while (sCached[i] == null);
            TextLine tl = sCached[i];
            sCached[i] = null;
            return tl;
        }
    }

    @VisibleForTesting(visibility = Visibility.PACKAGE)
    public static TextLine recycle(TextLine tl) {
        tl.mText = null;
        tl.mPaint = null;
        tl.mDirections = null;
        tl.mSpanned = null;
        tl.mTabs = null;
        tl.mChars = null;
        tl.mComputed = null;
        tl.mMetricAffectingSpanSpanSet.recycle();
        tl.mCharacterStyleSpanSet.recycle();
        tl.mReplacementSpanSpanSet.recycle();
        synchronized (sCached) {
            for (int i = 0; i < sCached.length; i++) {
                if (sCached[i] == null) {
                    sCached[i] = tl;
                    break;
                }
            }
        }
        return null;
    }

    @VisibleForTesting(visibility = Visibility.PACKAGE)
    public void set(TextPaint paint, CharSequence text, int start, int limit, int dir, Directions directions, boolean hasTabs, TabStops tabStops, int ellipsisStart, int ellipsisEnd) {
        TextPaint textPaint = paint;
        CharSequence charSequence = text;
        int i = start;
        int i2 = limit;
        int i3 = ellipsisStart;
        int i4 = ellipsisEnd;
        this.mPaint = textPaint;
        this.mText = charSequence;
        this.mStart = i;
        this.mLen = i2 - i;
        this.mDir = dir;
        this.mDirections = directions;
        if (this.mDirections != null) {
            int i5;
            this.mHasTabs = hasTabs;
            this.mSpanned = null;
            boolean hasReplacement = false;
            int i6 = 1;
            if (charSequence instanceof Spanned) {
                this.mSpanned = (Spanned) charSequence;
                this.mReplacementSpanSpanSet.init(this.mSpanned, i, i2);
                hasReplacement = this.mReplacementSpanSpanSet.numberOfSpans > 0;
            }
            this.mComputed = null;
            if (charSequence instanceof PrecomputedText) {
                this.mComputed = (PrecomputedText) charSequence;
                if (!this.mComputed.getParams().getTextPaint().equalsForTextMeasurement(textPaint)) {
                    this.mComputed = null;
                }
            }
            this.mCharsValid = hasReplacement;
            if (this.mCharsValid) {
                char[] cArr = this.mChars;
                if (cArr == null || cArr.length < this.mLen) {
                    this.mChars = ArrayUtils.newUnpaddedCharArray(this.mLen);
                }
                TextUtils.getChars(charSequence, i, i2, this.mChars, 0);
                if (hasReplacement) {
                    cArr = this.mChars;
                    i5 = start;
                    while (i5 < i2) {
                        int inext = this.mReplacementSpanSpanSet.getNextTransition(i5, i2);
                        if (this.mReplacementSpanSpanSet.hasSpansIntersecting(i5, inext) && (i5 - i >= i4 || inext - i <= i3)) {
                            cArr[i5 - i] = 65532;
                            i6 = inext - i;
                            for (int j = (i5 - i) + i6; j < i6; j++) {
                                cArr[j] = 65279;
                            }
                        }
                        i5 = inext;
                        i6 = 1;
                    }
                }
            }
            this.mTabs = tabStops;
            this.mAddedWidthForJustify = 0.0f;
            i5 = 0;
            this.mIsJustifying = false;
            this.mEllipsisStart = i3 != i4 ? i3 : 0;
            if (i3 != i4) {
                i5 = i4;
            }
            this.mEllipsisEnd = i5;
            return;
        }
        boolean z = hasTabs;
        TabStops tabStops2 = tabStops;
        throw new IllegalArgumentException("Directions cannot be null");
    }

    private char charAt(int i) {
        return this.mCharsValid ? this.mChars[i] : this.mText.charAt(this.mStart + i);
    }

    @VisibleForTesting(visibility = Visibility.PACKAGE)
    public void justify(float justifyWidth) {
        int end = this.mLen;
        while (end > 0 && isLineEndSpace(this.mText.charAt((this.mStart + end) - 1))) {
            end--;
        }
        int spaces = countStretchableSpaces(0, end);
        if (spaces != 0) {
            this.mAddedWidthForJustify = (justifyWidth - Math.abs(measure(end, false, null))) / ((float) spaces);
            this.mIsJustifying = true;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void draw(Canvas c, float x, int top, int y, int bottom) {
        float h = 0.0f;
        int runCount = this.mDirections.getRunCount();
        int runIndex = 0;
        while (runIndex < runCount) {
            int runCount2;
            int runStart = this.mDirections.getRunStart(runIndex);
            int runLimit = Math.min(this.mDirections.getRunLength(runIndex) + runStart, this.mLen);
            boolean runIsRtl = this.mDirections.isRunRtl(runIndex);
            float h2 = h;
            int segStart = runStart;
            int j = this.mHasTabs ? runStart : runLimit;
            while (j <= runLimit) {
                if (j == runLimit || charAt(j) == TAB_CHAR) {
                    float f = x + h2;
                    boolean z = (runIndex == runCount + -1 && j == this.mLen) ? false : true;
                    runCount2 = runCount;
                    runCount = j;
                    h2 += drawRun(c, segStart, j, runIsRtl, f, top, y, bottom, z);
                    if (runCount != runLimit) {
                        int i = this.mDir;
                        h2 = ((float) i) * nextTab(((float) i) * h2);
                    }
                    segStart = runCount + 1;
                } else {
                    runCount2 = runCount;
                    runCount = j;
                }
                j = runCount + 1;
                runCount = runCount2;
            }
            runCount2 = runCount;
            runCount = j;
            runIndex++;
            h = h2;
            runCount = runCount2;
        }
    }

    @VisibleForTesting(visibility = Visibility.PACKAGE)
    public float metrics(FontMetricsInt fmi) {
        return measure(this.mLen, false, fmi);
    }

    public float measure(int offset, boolean trailing, FontMetricsInt fmi) {
        int i = offset;
        if (i <= this.mLen) {
            int target = trailing ? i - 1 : i;
            if (target < 0) {
                return 0.0f;
            }
            float h = 0.0f;
            int runIndex = 0;
            while (runIndex < this.mDirections.getRunCount()) {
                int runStart = this.mDirections.getRunStart(runIndex);
                int runLimit = Math.min(this.mDirections.getRunLength(runIndex) + runStart, this.mLen);
                boolean runIsRtl = this.mDirections.isRunRtl(runIndex);
                float h2 = h;
                int segStart = runStart;
                int j = this.mHasTabs ? runStart : runLimit;
                while (j <= runLimit) {
                    if (j == runLimit || charAt(j) == TAB_CHAR) {
                        boolean z = false;
                        boolean z2 = target >= segStart && target < j;
                        boolean targetIsInThisSegment = z2;
                        if ((this.mDir == -1) == runIsRtl) {
                            z = true;
                        }
                        boolean sameDirection = z;
                        if (targetIsInThisSegment && sameDirection) {
                            return measureRun(segStart, offset, j, runIsRtl, fmi) + h2;
                        }
                        float segmentWidth = measureRun(segStart, j, j, runIsRtl, fmi);
                        h2 += sameDirection ? segmentWidth : -segmentWidth;
                        if (targetIsInThisSegment) {
                            return measureRun(segStart, offset, j, runIsRtl, null) + h2;
                        }
                        if (j != runLimit) {
                            if (i == j) {
                                return h2;
                            }
                            int i2 = this.mDir;
                            float h3 = ((float) i2) * nextTab(((float) i2) * h2);
                            if (target == j) {
                                return h3;
                            }
                            h2 = h3;
                        }
                        segStart = j + 1;
                    }
                    j++;
                }
                runIndex++;
                h = h2;
            }
            return h;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("offset(");
        stringBuilder.append(i);
        stringBuilder.append(") should be less than line limit(");
        stringBuilder.append(this.mLen);
        stringBuilder.append(")");
        throw new IndexOutOfBoundsException(stringBuilder.toString());
    }

    @VisibleForTesting
    public float[] measureAllOffsets(boolean[] trailing, FontMetricsInt fmi) {
        int i = this.mLen;
        float[] measurement = new float[(i + 1)];
        int[] target = new int[(i + 1)];
        for (i = 0; i < target.length; i++) {
            target[i] = trailing[i] ? i - 1 : i;
        }
        if (target[0] < 0) {
            measurement[0] = 0.0f;
        }
        float h = 0.0f;
        int runIndex = 0;
        while (runIndex < this.mDirections.getRunCount()) {
            int segStart;
            int runStart = this.mDirections.getRunStart(runIndex);
            int runLimit = Math.min(this.mDirections.getRunLength(runIndex) + runStart, this.mLen);
            boolean runIsRtl = this.mDirections.isRunRtl(runIndex);
            float h2 = h;
            int segStart2 = runStart;
            int j = this.mHasTabs ? runStart : runLimit;
            while (j <= runLimit) {
                if (j == runLimit || charAt(j) == TAB_CHAR) {
                    int offset;
                    float w;
                    float oldh = h2;
                    boolean advance = (this.mDir == -1) == runIsRtl;
                    segStart = segStart2;
                    float w2 = measureRun(segStart2, j, j, runIsRtl, fmi);
                    h2 += advance ? w2 : -w2;
                    float baseh = advance ? oldh : h2;
                    FontMetricsInt crtfmi = advance ? fmi : null;
                    int offset2 = segStart;
                    while (offset2 <= j && offset2 <= this.mLen) {
                        int segStart3 = segStart;
                        if (target[offset2] < segStart3 || target[offset2] >= j) {
                            segStart = segStart3;
                            offset = offset2;
                            w = w2;
                        } else {
                            segStart = segStart3;
                            offset = offset2;
                            w = w2;
                            measurement[offset] = baseh + measureRun(segStart3, offset2, j, runIsRtl, crtfmi);
                        }
                        offset2 = offset + 1;
                        w2 = w;
                    }
                    offset = offset2;
                    w = w2;
                    if (j != runLimit) {
                        if (target[j] == j) {
                            measurement[j] = h2;
                        }
                        i = this.mDir;
                        float h3 = ((float) i) * nextTab(((float) i) * h2);
                        if (target[j + 1] == j) {
                            measurement[j + 1] = h3;
                        }
                        h2 = h3;
                    }
                    segStart2 = j + 1;
                }
                j++;
            }
            segStart = segStart2;
            runIndex++;
            h = h2;
        }
        int i2 = this.mLen;
        if (target[i2] == i2) {
            measurement[i2] = h;
        }
        return measurement;
    }

    private float drawRun(Canvas c, int start, int limit, boolean runIsRtl, float x, int top, int y, int bottom, boolean needWidth) {
        boolean z = true;
        if (this.mDir != 1) {
            z = false;
        }
        if (z != runIsRtl) {
            return handleRun(start, limit, limit, runIsRtl, c, x, top, y, bottom, null, needWidth);
        }
        int i = start;
        int i2 = limit;
        int i3 = limit;
        boolean z2 = runIsRtl;
        float w = -measureRun(i, i2, i3, z2, null);
        handleRun(i, i2, i3, z2, c, x + w, top, y, bottom, null, false);
        return w;
    }

    private float measureRun(int start, int offset, int limit, boolean runIsRtl, FontMetricsInt fmi) {
        return handleRun(start, offset, limit, runIsRtl, null, 0.0f, 0, 0, 0, fmi, true);
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00bf  */
    /* JADX WARNING: Missing block: B:108:0x0189, code skipped:
            if (r13 != -1) goto L_0x0193;
     */
    /* JADX WARNING: Missing block: B:109:0x018b, code skipped:
            if (r0 == false) goto L_0x0191;
     */
    /* JADX WARNING: Missing block: B:110:0x018d, code skipped:
            r1 = r7.mLen + 1;
     */
    /* JADX WARNING: Missing block: B:111:0x0191, code skipped:
            r13 = r1;
     */
    /* JADX WARNING: Missing block: B:112:0x0193, code skipped:
            if (r13 > r11) goto L_0x019b;
     */
    /* JADX WARNING: Missing block: B:113:0x0195, code skipped:
            if (r0 == false) goto L_0x0199;
     */
    /* JADX WARNING: Missing block: B:114:0x0197, code skipped:
            r1 = r11;
     */
    /* JADX WARNING: Missing block: B:115:0x0199, code skipped:
            r1 = 0;
     */
    /* JADX WARNING: Missing block: B:116:0x019a, code skipped:
            r13 = r1;
     */
    public int getOffsetToLeftRightOf(int r29, boolean r30) {
        /*
        r28 = this;
        r7 = r28;
        r8 = r29;
        r9 = r30;
        r10 = 0;
        r11 = r7.mLen;
        r0 = r7.mDir;
        r12 = -1;
        if (r0 != r12) goto L_0x0010;
    L_0x000e:
        r0 = 1;
        goto L_0x0011;
    L_0x0010:
        r0 = 0;
    L_0x0011:
        r15 = r0;
        r0 = r7.mDirections;
        r6 = r0.mDirections;
        r0 = 0;
        r1 = r10;
        r2 = r11;
        r16 = -1;
        r3 = 0;
        r17 = 67108863; // 0x3ffffff float:1.5046327E-36 double:3.31561837E-316;
        if (r8 != r10) goto L_0x002f;
    L_0x0021:
        r4 = -2;
        r14 = r0;
        r19 = r1;
        r22 = r3;
        r12 = r4;
        r8 = r6;
        r13 = r16;
        r16 = r2;
        goto L_0x0109;
    L_0x002f:
        if (r8 != r11) goto L_0x003f;
    L_0x0031:
        r4 = r6.length;
        r14 = r0;
        r19 = r1;
        r22 = r3;
        r12 = r4;
        r8 = r6;
        r13 = r16;
        r16 = r2;
        goto L_0x0109;
    L_0x003f:
        r4 = 0;
    L_0x0040:
        r5 = r6.length;
        if (r4 >= r5) goto L_0x00b6;
    L_0x0043:
        r5 = r6[r4];
        r1 = r10 + r5;
        if (r8 < r1) goto L_0x00ae;
    L_0x0049:
        r5 = r4 + 1;
        r5 = r6[r5];
        r5 = r5 & r17;
        r5 = r5 + r1;
        if (r5 <= r11) goto L_0x0053;
    L_0x0052:
        r5 = r11;
    L_0x0053:
        if (r8 >= r5) goto L_0x00aa;
    L_0x0055:
        r2 = r4 + 1;
        r2 = r6[r2];
        r2 = r2 >>> 26;
        r0 = r2 & 63;
        if (r8 != r1) goto L_0x00a1;
    L_0x005f:
        r2 = r8 + -1;
        r18 = 0;
        r13 = r18;
    L_0x0065:
        r14 = r6.length;
        if (r13 >= r14) goto L_0x0098;
    L_0x0068:
        r14 = r6[r13];
        r14 = r14 + r10;
        if (r2 < r14) goto L_0x0090;
    L_0x006d:
        r19 = r13 + 1;
        r19 = r6[r19];
        r19 = r19 & r17;
        r12 = r14 + r19;
        if (r12 <= r11) goto L_0x0078;
    L_0x0077:
        r12 = r11;
    L_0x0078:
        if (r2 >= r12) goto L_0x008d;
    L_0x007a:
        r19 = r13 + 1;
        r19 = r6[r19];
        r19 = r19 >>> 26;
        r20 = r1;
        r1 = r19 & 63;
        if (r1 >= r0) goto L_0x0092;
    L_0x0086:
        r4 = r13;
        r0 = r1;
        r19 = r14;
        r5 = r12;
        r3 = 1;
        goto L_0x009c;
    L_0x008d:
        r20 = r1;
        goto L_0x0092;
    L_0x0090:
        r20 = r1;
    L_0x0092:
        r13 = r13 + 2;
        r1 = r20;
        r12 = -1;
        goto L_0x0065;
    L_0x0098:
        r20 = r1;
        r19 = r20;
    L_0x009c:
        r13 = r0;
        r12 = r4;
        r14 = r5;
        r5 = r3;
        goto L_0x00bc;
    L_0x00a1:
        r20 = r1;
        r13 = r0;
        r12 = r4;
        r14 = r5;
        r19 = r20;
        r5 = r3;
        goto L_0x00bc;
    L_0x00aa:
        r20 = r1;
        r2 = r5;
        goto L_0x00b0;
    L_0x00ae:
        r20 = r1;
    L_0x00b0:
        r4 = r4 + 2;
        r1 = r20;
        r12 = -1;
        goto L_0x0040;
    L_0x00b6:
        r13 = r0;
        r19 = r1;
        r14 = r2;
        r5 = r3;
        r12 = r4;
    L_0x00bc:
        r0 = r6.length;
        if (r12 == r0) goto L_0x00ff;
    L_0x00bf:
        r0 = r13 & 1;
        if (r0 == 0) goto L_0x00c5;
    L_0x00c3:
        r0 = 1;
        goto L_0x00c6;
    L_0x00c5:
        r0 = 0;
    L_0x00c6:
        r4 = r0;
        if (r9 != r4) goto L_0x00cb;
    L_0x00c9:
        r0 = 1;
        goto L_0x00cc;
    L_0x00cb:
        r0 = 0;
    L_0x00cc:
        r3 = r0;
        if (r3 == 0) goto L_0x00d1;
    L_0x00cf:
        r0 = r14;
        goto L_0x00d3;
    L_0x00d1:
        r0 = r19;
    L_0x00d3:
        if (r8 != r0) goto L_0x00dc;
    L_0x00d5:
        if (r3 == r5) goto L_0x00d8;
    L_0x00d7:
        goto L_0x00dc;
    L_0x00d8:
        r22 = r5;
        r8 = r6;
        goto L_0x0102;
    L_0x00dc:
        r0 = r28;
        r1 = r12;
        r2 = r19;
        r20 = r3;
        r3 = r14;
        r21 = r4;
        r22 = r5;
        r5 = r29;
        r8 = r6;
        r6 = r20;
        r0 = r0.getOffsetBeforeAfter(r1, r2, r3, r4, r5, r6);
        if (r20 == 0) goto L_0x00f5;
    L_0x00f3:
        r1 = r14;
        goto L_0x00f7;
    L_0x00f5:
        r1 = r19;
    L_0x00f7:
        if (r0 == r1) goto L_0x00fa;
    L_0x00f9:
        return r0;
    L_0x00fa:
        r16 = r14;
        r14 = r13;
        r13 = r0;
        goto L_0x0109;
    L_0x00ff:
        r22 = r5;
        r8 = r6;
    L_0x0102:
        r27 = r14;
        r14 = r13;
        r13 = r16;
        r16 = r27;
    L_0x0109:
        if (r9 != r15) goto L_0x010d;
    L_0x010b:
        r0 = 1;
        goto L_0x010e;
    L_0x010d:
        r0 = 0;
    L_0x010e:
        if (r0 == 0) goto L_0x0112;
    L_0x0110:
        r1 = 2;
        goto L_0x0113;
    L_0x0112:
        r1 = -2;
    L_0x0113:
        r6 = r12 + r1;
        if (r6 < 0) goto L_0x0186;
    L_0x0117:
        r1 = r8.length;
        if (r6 >= r1) goto L_0x0186;
    L_0x011a:
        r1 = r8[r6];
        r20 = r10 + r1;
        r1 = r6 + 1;
        r1 = r8[r1];
        r1 = r1 & r17;
        r1 = r20 + r1;
        if (r1 <= r11) goto L_0x012c;
    L_0x0128:
        r1 = r11;
        r21 = r1;
        goto L_0x012e;
    L_0x012c:
        r21 = r1;
    L_0x012e:
        r1 = r6 + 1;
        r1 = r8[r1];
        r1 = r1 >>> 26;
        r5 = r1 & 63;
        r1 = r5 & 1;
        if (r1 == 0) goto L_0x013c;
    L_0x013a:
        r1 = 1;
        goto L_0x013d;
    L_0x013c:
        r1 = 0;
    L_0x013d:
        r4 = r1;
        if (r9 != r4) goto L_0x0142;
    L_0x0140:
        r1 = 1;
        goto L_0x0143;
    L_0x0142:
        r1 = 0;
    L_0x0143:
        r23 = r1;
        r0 = -1;
        if (r13 != r0) goto L_0x0173;
        if (r23 == 0) goto L_0x014e;
    L_0x014b:
        r24 = r20;
        goto L_0x0150;
    L_0x014e:
        r24 = r21;
    L_0x0150:
        r0 = r28;
        r1 = r6;
        r2 = r20;
        r3 = r21;
        r25 = r4;
        r26 = r5;
        r5 = r24;
        r24 = r6;
        r6 = r23;
        r13 = r0.getOffsetBeforeAfter(r1, r2, r3, r4, r5, r6);
        if (r23 == 0) goto L_0x016a;
    L_0x0167:
        r0 = r21;
        goto L_0x016c;
    L_0x016a:
        r0 = r20;
    L_0x016c:
        if (r13 != r0) goto L_0x019b;
    L_0x016e:
        r12 = r24;
        r14 = r26;
        goto L_0x0109;
    L_0x0173:
        r25 = r4;
        r26 = r5;
        r24 = r6;
        r0 = r26;
        if (r0 >= r14) goto L_0x019b;
    L_0x017d:
        if (r23 == 0) goto L_0x0182;
    L_0x017f:
        r1 = r20;
        goto L_0x0184;
    L_0x0182:
        r1 = r21;
    L_0x0184:
        r13 = r1;
        goto L_0x019b;
    L_0x0186:
        r24 = r6;
        r1 = -1;
        if (r13 != r1) goto L_0x0193;
    L_0x018b:
        if (r0 == 0) goto L_0x0191;
    L_0x018d:
        r1 = r7.mLen;
        r2 = 1;
        r1 = r1 + r2;
    L_0x0191:
        r13 = r1;
        goto L_0x019b;
    L_0x0193:
        if (r13 > r11) goto L_0x019b;
    L_0x0195:
        if (r0 == 0) goto L_0x0199;
    L_0x0197:
        r1 = r11;
        goto L_0x019a;
    L_0x0199:
        r1 = r10;
    L_0x019a:
        r13 = r1;
    L_0x019b:
        return r13;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.TextLine.getOffsetToLeftRightOf(int, boolean):int");
    }

    private int getOffsetBeforeAfter(int runIndex, int runStart, int runLimit, boolean runIsRtl, int offset, boolean after) {
        int i = offset;
        if (runIndex >= 0) {
            int i2 = 0;
            if (i != (after ? this.mLen : 0)) {
                int spanStart;
                int spanLimit;
                TextPaint wp = this.mWorkPaint;
                wp.set(this.mPaint);
                if (this.mIsJustifying) {
                    wp.setWordSpacing(this.mAddedWidthForJustify);
                }
                int spanStart2 = runStart;
                if (this.mSpanned == null) {
                    spanStart = spanStart2;
                    spanLimit = runLimit;
                } else {
                    int spanLimit2;
                    int i3;
                    int target = after ? i + 1 : i;
                    int limit = this.mStart + runLimit;
                    while (true) {
                        spanLimit2 = this.mSpanned.nextSpanTransition(this.mStart + spanStart2, limit, MetricAffectingSpan.class);
                        i3 = this.mStart;
                        spanLimit2 -= i3;
                        if (spanLimit2 >= target) {
                            break;
                        }
                        spanStart2 = spanLimit2;
                    }
                    MetricAffectingSpan[] spans = (MetricAffectingSpan[]) TextUtils.removeEmptySpans((MetricAffectingSpan[]) this.mSpanned.getSpans(i3 + spanStart2, i3 + spanLimit2, MetricAffectingSpan.class), this.mSpanned, MetricAffectingSpan.class);
                    if (spans.length > 0) {
                        ReplacementSpan replacement = null;
                        for (MetricAffectingSpan span : spans) {
                            if (span instanceof ReplacementSpan) {
                                replacement = (ReplacementSpan) span;
                            } else {
                                span.updateMeasureState(wp);
                            }
                        }
                        if (replacement != null) {
                            return after ? spanLimit2 : spanStart2;
                        }
                    }
                    spanStart = spanStart2;
                    spanLimit = spanLimit2;
                }
                if (!after) {
                    i2 = 2;
                }
                int cursorOpt = i2;
                if (this.mCharsValid) {
                    return wp.getTextRunCursor(this.mChars, spanStart, spanLimit - spanStart, runIsRtl, offset, cursorOpt);
                }
                CharSequence charSequence = this.mText;
                i2 = this.mStart;
                return wp.getTextRunCursor(charSequence, i2 + spanStart, i2 + spanLimit, runIsRtl, i2 + i, cursorOpt) - this.mStart;
            }
        }
        if (after) {
            return TextUtils.getOffsetAfter(this.mText, this.mStart + i) - this.mStart;
        }
        return TextUtils.getOffsetBefore(this.mText, this.mStart + i) - this.mStart;
    }

    private static void expandMetricsFromPaint(FontMetricsInt fmi, TextPaint wp) {
        int previousTop = fmi.top;
        int previousAscent = fmi.ascent;
        int previousDescent = fmi.descent;
        int previousBottom = fmi.bottom;
        int previousLeading = fmi.leading;
        wp.getFontMetricsInt(fmi);
        updateMetrics(fmi, previousTop, previousAscent, previousDescent, previousBottom, previousLeading);
    }

    static void updateMetrics(FontMetricsInt fmi, int previousTop, int previousAscent, int previousDescent, int previousBottom, int previousLeading) {
        fmi.top = Math.min(fmi.top, previousTop);
        fmi.ascent = Math.min(fmi.ascent, previousAscent);
        fmi.descent = Math.max(fmi.descent, previousDescent);
        fmi.bottom = Math.max(fmi.bottom, previousBottom);
        fmi.leading = Math.max(fmi.leading, previousLeading);
    }

    private static void drawStroke(TextPaint wp, Canvas c, int color, float position, float thickness, float xleft, float xright, float baseline) {
        float strokeTop = (baseline + ((float) wp.baselineShift)) + position;
        int previousColor = wp.getColor();
        Style previousStyle = wp.getStyle();
        boolean previousAntiAlias = wp.isAntiAlias();
        wp.setStyle(Style.FILL);
        wp.setAntiAlias(true);
        int i = color;
        wp.setColor(color);
        c.drawRect(xleft, strokeTop, xright, strokeTop + thickness, wp);
        wp.setStyle(previousStyle);
        wp.setColor(previousColor);
        wp.setAntiAlias(previousAntiAlias);
    }

    private float getRunAdvance(TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, int offset) {
        if (this.mCharsValid) {
            return wp.getRunAdvance(this.mChars, start, end, contextStart, contextEnd, runIsRtl, offset);
        }
        int delta = this.mStart;
        PrecomputedText precomputedText = this.mComputed;
        if (precomputedText != null) {
            return precomputedText.getWidth(start + delta, end + delta);
        }
        return wp.getRunAdvance(this.mText, delta + start, delta + end, delta + contextStart, delta + contextEnd, runIsRtl, delta + offset);
    }

    private float handleText(TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, Canvas c, float x, int top, int y, int bottom, FontMetricsInt fmi, boolean needWidth, int offset, ArrayList<DecorationInfo> decorations) {
        TextPaint textPaint = wp;
        int i = start;
        int i2 = y;
        FontMetricsInt fontMetricsInt = fmi;
        ArrayList<DecorationInfo> arrayList = decorations;
        if (this.mIsJustifying) {
            textPaint.setWordSpacing(this.mAddedWidthForJustify);
        }
        if (fontMetricsInt != null) {
            expandMetricsFromPaint(fontMetricsInt, textPaint);
        }
        if (end == i) {
            return 0.0f;
        }
        int numDecorations;
        float totalWidth;
        float totalWidth2 = 0.0f;
        int numDecorations2 = arrayList == null ? 0 : decorations.size();
        if (needWidth || !(c == null || (textPaint.bgColor == 0 && numDecorations2 == 0 && !runIsRtl))) {
            numDecorations = numDecorations2;
            totalWidth2 = getRunAdvance(wp, start, end, contextStart, contextEnd, runIsRtl, offset);
        } else {
            numDecorations = numDecorations2;
        }
        int numDecorations3;
        if (c != null) {
            float leftX;
            float rightX;
            if (runIsRtl) {
                leftX = x - totalWidth2;
                rightX = x;
            } else {
                leftX = x;
                rightX = x + totalWidth2;
            }
            if (textPaint.bgColor != 0) {
                int previousColor = wp.getColor();
                Style previousStyle = wp.getStyle();
                textPaint.setColor(textPaint.bgColor);
                textPaint.setStyle(Style.FILL);
                c.drawRect(leftX, (float) top, rightX, (float) bottom, wp);
                textPaint.setStyle(previousStyle);
                textPaint.setColor(previousColor);
            }
            int i3 = i2 + textPaint.baselineShift;
            totalWidth = totalWidth2;
            drawTextRun(c, wp, start, end, contextStart, contextEnd, runIsRtl, leftX, i3);
            if (numDecorations != 0) {
                int totalWidth3 = 0;
                while (totalWidth3 < numDecorations) {
                    float decorationXLeft;
                    float decorationXRight;
                    DecorationInfo info = (DecorationInfo) arrayList.get(totalWidth3);
                    i3 = Math.max(info.start, i);
                    int decorationEnd = Math.min(info.end, offset);
                    TextPaint textPaint2 = wp;
                    int i4 = start;
                    int i5 = end;
                    int i6 = contextStart;
                    int i7 = contextEnd;
                    boolean z = runIsRtl;
                    float decorationStartAdvance = getRunAdvance(textPaint2, i4, i5, i6, i7, z, i3);
                    float decorationEndAdvance = getRunAdvance(textPaint2, i4, i5, i6, i7, z, decorationEnd);
                    if (runIsRtl) {
                        decorationXLeft = rightX - decorationEndAdvance;
                        decorationXRight = rightX - decorationStartAdvance;
                    } else {
                        decorationXLeft = leftX + decorationStartAdvance;
                        decorationXRight = leftX + decorationEndAdvance;
                    }
                    if (info.underlineColor != 0) {
                        drawStroke(wp, c, info.underlineColor, wp.getUnderlinePosition(), info.underlineThickness, decorationXLeft, decorationXRight, (float) i2);
                    }
                    if (info.isUnderlineText) {
                        numDecorations3 = numDecorations;
                        numDecorations = 1065353216;
                        drawStroke(wp, c, wp.getColor(), wp.getUnderlinePosition(), Math.max(wp.getUnderlineThickness(), 1.0f), decorationXLeft, decorationXRight, (float) i2);
                    } else {
                        numDecorations3 = numDecorations;
                        numDecorations = 1065353216;
                    }
                    if (info.isStrikeThruText) {
                        drawStroke(wp, c, wp.getColor(), wp.getStrikeThruPosition(), Math.max(wp.getStrikeThruThickness(), numDecorations), decorationXLeft, decorationXRight, (float) i2);
                    }
                    totalWidth3++;
                    numDecorations = numDecorations3;
                }
            }
        } else {
            totalWidth = totalWidth2;
            numDecorations3 = numDecorations;
        }
        return runIsRtl ? -totalWidth : totalWidth;
    }

    private float handleReplacement(ReplacementSpan replacement, TextPaint wp, int start, int limit, boolean runIsRtl, Canvas c, float x, int top, int y, int bottom, FontMetricsInt fmi, boolean needWidth) {
        FontMetricsInt fontMetricsInt = fmi;
        float ret = 0.0f;
        int i = this.mStart;
        int textStart = i + start;
        int textLimit = i + limit;
        if (needWidth || (c != null && runIsRtl)) {
            int previousTop;
            int previousAscent;
            int ret2;
            int previousBottom;
            int previousLeading;
            boolean needUpdateMetrics = fontMetricsInt != null;
            if (needUpdateMetrics) {
                i = fontMetricsInt.top;
                previousTop = i;
                previousAscent = fontMetricsInt.ascent;
                ret2 = fontMetricsInt.descent;
                previousBottom = fontMetricsInt.bottom;
                previousLeading = fontMetricsInt.leading;
            } else {
                previousTop = 0;
                previousAscent = 0;
                ret2 = 0;
                previousBottom = 0;
                previousLeading = 0;
            }
            ret = (float) replacement.getSize(wp, this.mText, textStart, textLimit, fmi);
            if (needUpdateMetrics) {
                updateMetrics(fmi, previousTop, previousAscent, ret2, previousBottom, previousLeading);
            }
        }
        float ret3 = ret;
        if (c != null) {
            float x2;
            if (runIsRtl) {
                x2 = x - ret3;
            } else {
                x2 = x;
            }
            replacement.draw(c, this.mText, textStart, textLimit, x2, top, y, bottom, wp);
        }
        return runIsRtl ? -ret3 : ret3;
    }

    private int adjustStartHyphenEdit(int start, int startHyphenEdit) {
        return start > 0 ? 0 : startHyphenEdit;
    }

    private int adjustEndHyphenEdit(int limit, int endHyphenEdit) {
        return limit < this.mLen ? 0 : endHyphenEdit;
    }

    private void extractDecorationInfo(TextPaint paint, DecorationInfo info) {
        info.isStrikeThruText = paint.isStrikeThruText();
        if (info.isStrikeThruText) {
            paint.setStrikeThruText(false);
        }
        info.isUnderlineText = paint.isUnderlineText();
        if (info.isUnderlineText) {
            paint.setUnderlineText(false);
        }
        info.underlineColor = paint.underlineColor;
        info.underlineThickness = paint.underlineThickness;
        paint.setUnderlineText(0, 0.0f);
    }

    /* JADX WARNING: Removed duplicated region for block: B:82:0x0281  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x026f  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x026f  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0281  */
    private float handleRun(int r36, int r37, int r38, boolean r39, android.graphics.Canvas r40, float r41, int r42, int r43, int r44, android.graphics.Paint.FontMetricsInt r45, boolean r46) {
        /*
        r35 = this;
        r15 = r35;
        r14 = r36;
        r13 = r37;
        r12 = r38;
        r11 = r45;
        if (r13 < r14) goto L_0x0312;
    L_0x000c:
        if (r13 > r12) goto L_0x0312;
    L_0x000e:
        if (r14 != r13) goto L_0x001e;
    L_0x0010:
        r0 = r15.mWorkPaint;
        r1 = r15.mPaint;
        r0.set(r1);
        if (r11 == 0) goto L_0x001c;
    L_0x0019:
        expandMetricsFromPaint(r11, r0);
    L_0x001c:
        r1 = 0;
        return r1;
    L_0x001e:
        r0 = r15.mSpanned;
        r16 = 0;
        r17 = 1;
        if (r0 != 0) goto L_0x002a;
    L_0x0026:
        r0 = 0;
        r18 = r0;
        goto L_0x0054;
    L_0x002a:
        r1 = r15.mMetricAffectingSpanSpanSet;
        r2 = r15.mStart;
        r3 = r2 + r14;
        r2 = r2 + r12;
        r1.init(r0, r3, r2);
        r0 = r15.mCharacterStyleSpanSet;
        r1 = r15.mSpanned;
        r2 = r15.mStart;
        r3 = r2 + r14;
        r2 = r2 + r12;
        r0.init(r1, r3, r2);
        r0 = r15.mMetricAffectingSpanSpanSet;
        r0 = r0.numberOfSpans;
        if (r0 != 0) goto L_0x0050;
    L_0x0046:
        r0 = r15.mCharacterStyleSpanSet;
        r0 = r0.numberOfSpans;
        if (r0 == 0) goto L_0x004d;
    L_0x004c:
        goto L_0x0050;
    L_0x004d:
        r0 = r16;
        goto L_0x0052;
    L_0x0050:
        r0 = r17;
    L_0x0052:
        r18 = r0;
    L_0x0054:
        if (r18 != 0) goto L_0x009b;
    L_0x0056:
        r10 = r15.mWorkPaint;
        r0 = r15.mPaint;
        r10.set(r0);
        r0 = r10.getStartHyphenEdit();
        r0 = r15.adjustStartHyphenEdit(r14, r0);
        r10.setStartHyphenEdit(r0);
        r0 = r10.getEndHyphenEdit();
        r0 = r15.adjustEndHyphenEdit(r12, r0);
        r10.setEndHyphenEdit(r0);
        r16 = 0;
        r0 = r35;
        r1 = r10;
        r2 = r36;
        r3 = r38;
        r4 = r36;
        r5 = r38;
        r6 = r39;
        r7 = r40;
        r8 = r41;
        r9 = r42;
        r17 = r10;
        r10 = r43;
        r11 = r44;
        r12 = r45;
        r13 = r46;
        r14 = r37;
        r15 = r16;
        r0 = r0.handleText(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);
        return r0;
    L_0x009b:
        r19 = r41;
        r0 = r36;
        r13 = r41;
        r15 = r0;
    L_0x00a2:
        r14 = r37;
        if (r15 >= r14) goto L_0x030f;
    L_0x00a6:
        r12 = r35;
        r11 = r12.mWorkPaint;
        r0 = r12.mPaint;
        r11.set(r0);
        r0 = r12.mMetricAffectingSpanSpanSet;
        r1 = r12.mStart;
        r2 = r1 + r15;
        r10 = r38;
        r1 = r1 + r10;
        r0 = r0.getNextTransition(r2, r1);
        r1 = r12.mStart;
        r9 = r0 - r1;
        r8 = java.lang.Math.min(r9, r14);
        r0 = 0;
        r1 = 0;
        r20 = r0;
    L_0x00c8:
        r0 = r12.mMetricAffectingSpanSpanSet;
        r0 = r0.numberOfSpans;
        if (r1 >= r0) goto L_0x011f;
    L_0x00ce:
        r0 = r12.mMetricAffectingSpanSpanSet;
        r0 = r0.spanStarts;
        r0 = r0[r1];
        r2 = r12.mStart;
        r2 = r2 + r8;
        if (r0 >= r2) goto L_0x011c;
    L_0x00d9:
        r0 = r12.mMetricAffectingSpanSpanSet;
        r0 = r0.spanEnds;
        r0 = r0[r1];
        r2 = r12.mStart;
        r3 = r2 + r15;
        if (r0 > r3) goto L_0x00e6;
    L_0x00e5:
        goto L_0x011c;
    L_0x00e6:
        r0 = r12.mEllipsisStart;
        r2 = r2 + r0;
        r0 = r12.mMetricAffectingSpanSpanSet;
        r0 = r0.spanStarts;
        r0 = r0[r1];
        if (r2 > r0) goto L_0x0101;
    L_0x00f1:
        r0 = r12.mMetricAffectingSpanSpanSet;
        r0 = r0.spanEnds;
        r0 = r0[r1];
        r2 = r12.mStart;
        r3 = r12.mEllipsisEnd;
        r2 = r2 + r3;
        if (r0 > r2) goto L_0x0101;
    L_0x00fe:
        r0 = r17;
        goto L_0x0103;
    L_0x0101:
        r0 = r16;
    L_0x0103:
        r2 = r12.mMetricAffectingSpanSpanSet;
        r2 = r2.spans;
        r2 = (android.text.style.MetricAffectingSpan[]) r2;
        r2 = r2[r1];
        r3 = r2 instanceof android.text.style.ReplacementSpan;
        if (r3 == 0) goto L_0x0119;
    L_0x010f:
        if (r0 != 0) goto L_0x0115;
    L_0x0111:
        r3 = r2;
        r3 = (android.text.style.ReplacementSpan) r3;
        goto L_0x0116;
    L_0x0115:
        r3 = 0;
    L_0x0116:
        r20 = r3;
        goto L_0x011c;
    L_0x0119:
        r2.updateDrawState(r11);
    L_0x011c:
        r1 = r1 + 1;
        goto L_0x00c8;
    L_0x011f:
        if (r20 == 0) goto L_0x0152;
    L_0x0121:
        if (r46 != 0) goto L_0x0129;
    L_0x0123:
        if (r8 >= r14) goto L_0x0126;
    L_0x0125:
        goto L_0x0129;
    L_0x0126:
        r21 = r16;
        goto L_0x012b;
    L_0x0129:
        r21 = r17;
    L_0x012b:
        r0 = r35;
        r1 = r20;
        r2 = r11;
        r3 = r15;
        r4 = r8;
        r5 = r39;
        r6 = r40;
        r7 = r13;
        r41 = r8;
        r8 = r42;
        r22 = r9;
        r9 = r43;
        r10 = r44;
        r14 = r11;
        r11 = r45;
        r23 = r14;
        r14 = r12;
        r12 = r21;
        r0 = r0.handleReplacement(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12);
        r13 = r13 + r0;
        r23 = r15;
        goto L_0x030b;
    L_0x0152:
        r41 = r8;
        r22 = r9;
        r23 = r11;
        r14 = r12;
        r12 = r14.mActivePaint;
        r0 = r14.mPaint;
        r12.set(r0);
        r0 = r15;
        r1 = r41;
        r11 = r14.mDecorationInfo;
        r2 = r14.mDecorations;
        r2.clear();
        r2 = r15;
        r10 = r0;
        r9 = r1;
        r21 = r13;
        r13 = r2;
    L_0x0170:
        r8 = r41;
        if (r13 >= r8) goto L_0x0295;
    L_0x0174:
        r0 = r14.mCharacterStyleSpanSet;
        r1 = r14.mStart;
        r2 = r1 + r13;
        r1 = r1 + r22;
        r0 = r0.getNextTransition(r2, r1);
        r1 = r14.mStart;
        r7 = r0 - r1;
        r24 = java.lang.Math.min(r7, r8);
        r0 = r14.mPaint;
        r1 = r23;
        r1.set(r0);
        r0 = 0;
    L_0x0190:
        r2 = r14.mCharacterStyleSpanSet;
        r2 = r2.numberOfSpans;
        if (r0 >= r2) goto L_0x01bc;
    L_0x0196:
        r2 = r14.mCharacterStyleSpanSet;
        r2 = r2.spanStarts;
        r2 = r2[r0];
        r3 = r14.mStart;
        r3 = r3 + r24;
        if (r2 >= r3) goto L_0x01b9;
    L_0x01a2:
        r2 = r14.mCharacterStyleSpanSet;
        r2 = r2.spanEnds;
        r2 = r2[r0];
        r3 = r14.mStart;
        r3 = r3 + r13;
        if (r2 > r3) goto L_0x01ae;
    L_0x01ad:
        goto L_0x01b9;
    L_0x01ae:
        r2 = r14.mCharacterStyleSpanSet;
        r2 = r2.spans;
        r2 = (android.text.style.CharacterStyle[]) r2;
        r2 = r2[r0];
        r2.updateDrawState(r1);
    L_0x01b9:
        r0 = r0 + 1;
        goto L_0x0190;
    L_0x01bc:
        r14.extractDecorationInfo(r1, r11);
        if (r13 != r15) goto L_0x01d7;
    L_0x01c1:
        r12.set(r1);
        r41 = r7;
        r28 = r8;
        r29 = r9;
        r30 = r10;
        r31 = r11;
        r33 = r13;
        r13 = r14;
        r23 = r15;
        r15 = r1;
        r14 = r12;
        goto L_0x0265;
    L_0x01d7:
        r0 = equalAttributes(r1, r12);
        if (r0 != 0) goto L_0x0254;
    L_0x01dd:
        r0 = r14.mPaint;
        r0 = r0.getStartHyphenEdit();
        r0 = r14.adjustStartHyphenEdit(r10, r0);
        r12.setStartHyphenEdit(r0);
        r0 = r14.mPaint;
        r0 = r0.getEndHyphenEdit();
        r0 = r14.adjustEndHyphenEdit(r9, r0);
        r12.setEndHyphenEdit(r0);
        if (r46 != 0) goto L_0x0202;
    L_0x01f9:
        r6 = r37;
        r5 = r1;
        if (r9 >= r6) goto L_0x01ff;
    L_0x01fe:
        goto L_0x0205;
    L_0x01ff:
        r23 = r16;
        goto L_0x0207;
    L_0x0202:
        r6 = r37;
        r5 = r1;
    L_0x0205:
        r23 = r17;
    L_0x0207:
        r25 = java.lang.Math.min(r9, r8);
        r4 = r14.mDecorations;
        r0 = r35;
        r1 = r12;
        r2 = r10;
        r3 = r9;
        r26 = r4;
        r4 = r15;
        r27 = r5;
        r5 = r22;
        r6 = r39;
        r41 = r7;
        r7 = r40;
        r28 = r8;
        r8 = r21;
        r29 = r9;
        r9 = r42;
        r30 = r10;
        r10 = r43;
        r31 = r11;
        r11 = r44;
        r32 = r12;
        r12 = r45;
        r33 = r13;
        r13 = r23;
        r14 = r25;
        r23 = r15;
        r15 = r26;
        r0 = r0.handleText(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);
        r21 = r21 + r0;
        r10 = r33;
        r15 = r27;
        r14 = r32;
        r14.set(r15);
        r13 = r35;
        r0 = r13.mDecorations;
        r0.clear();
        goto L_0x0267;
    L_0x0254:
        r41 = r7;
        r28 = r8;
        r29 = r9;
        r30 = r10;
        r31 = r11;
        r33 = r13;
        r13 = r14;
        r23 = r15;
        r15 = r1;
        r14 = r12;
    L_0x0265:
        r10 = r30;
    L_0x0267:
        r9 = r41;
        r0 = r31.hasDecoration();
        if (r0 == 0) goto L_0x0281;
    L_0x026f:
        r0 = r31.copyInfo();
        r2 = r33;
        r0.start = r2;
        r1 = r41;
        r0.end = r1;
        r3 = r13.mDecorations;
        r3.add(r0);
        goto L_0x0285;
    L_0x0281:
        r1 = r41;
        r2 = r33;
    L_0x0285:
        r0 = r1;
        r12 = r14;
        r41 = r28;
        r11 = r31;
        r14 = r13;
        r13 = r0;
        r34 = r23;
        r23 = r15;
        r15 = r34;
        goto L_0x0170;
    L_0x0295:
        r28 = r8;
        r29 = r9;
        r30 = r10;
        r31 = r11;
        r2 = r13;
        r13 = r14;
        r14 = r12;
        r34 = r23;
        r23 = r15;
        r15 = r34;
        r0 = r13.mPaint;
        r0 = r0.getStartHyphenEdit();
        r12 = r30;
        r0 = r13.adjustStartHyphenEdit(r12, r0);
        r14.setStartHyphenEdit(r0);
        r0 = r13.mPaint;
        r0 = r0.getEndHyphenEdit();
        r11 = r29;
        r0 = r13.adjustEndHyphenEdit(r11, r0);
        r14.setEndHyphenEdit(r0);
        if (r46 != 0) goto L_0x02ce;
    L_0x02c6:
        r10 = r37;
        if (r11 >= r10) goto L_0x02cb;
    L_0x02ca:
        goto L_0x02d0;
    L_0x02cb:
        r24 = r16;
        goto L_0x02d2;
    L_0x02ce:
        r10 = r37;
    L_0x02d0:
        r24 = r17;
    L_0x02d2:
        r9 = r28;
        r25 = java.lang.Math.min(r11, r9);
        r8 = r13.mDecorations;
        r0 = r35;
        r1 = r14;
        r2 = r12;
        r3 = r11;
        r4 = r23;
        r5 = r22;
        r6 = r39;
        r7 = r40;
        r26 = r8;
        r8 = r21;
        r27 = r9;
        r9 = r42;
        r10 = r43;
        r28 = r11;
        r11 = r44;
        r29 = r12;
        r12 = r45;
        r13 = r24;
        r24 = r14;
        r14 = r25;
        r25 = r15;
        r15 = r26;
        r0 = r0.handleText(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);
        r21 = r21 + r0;
        r13 = r21;
    L_0x030b:
        r15 = r22;
        goto L_0x00a2;
    L_0x030f:
        r0 = r13 - r19;
        return r0;
    L_0x0312:
        r0 = new java.lang.IndexOutOfBoundsException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "measureLimit (";
        r1.append(r2);
        r2 = r37;
        r1.append(r2);
        r3 = ") is out of start (";
        r1.append(r3);
        r3 = r36;
        r1.append(r3);
        r4 = ") and limit (";
        r1.append(r4);
        r4 = r38;
        r1.append(r4);
        r5 = ") bounds";
        r1.append(r5);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.TextLine.handleRun(int, int, int, boolean, android.graphics.Canvas, float, int, int, int, android.graphics.Paint$FontMetricsInt, boolean):float");
    }

    private void drawTextRun(Canvas c, TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, float x, int y) {
        int i = y;
        if (this.mCharsValid) {
            c.drawTextRun(this.mChars, start, end - start, contextStart, contextEnd - contextStart, x, (float) i, runIsRtl, (Paint) wp);
            return;
        }
        int delta = this.mStart;
        c.drawTextRun(this.mText, delta + start, delta + end, delta + contextStart, delta + contextEnd, x, (float) i, runIsRtl, (Paint) wp);
    }

    /* Access modifiers changed, original: 0000 */
    public float nextTab(float h) {
        TabStops tabStops = this.mTabs;
        if (tabStops != null) {
            return tabStops.nextTab(h);
        }
        return TabStops.nextDefaultStop(h, 20.0f);
    }

    private boolean isStretchableWhitespace(int ch) {
        return ch == 32;
    }

    private int countStretchableSpaces(int start, int end) {
        int count = 0;
        int i = start;
        while (i < end) {
            if (isStretchableWhitespace(this.mCharsValid ? this.mChars[i] : this.mText.charAt(this.mStart + i))) {
                count++;
            }
            i++;
        }
        return count;
    }

    public static boolean isLineEndSpace(char ch) {
        return ch == ' ' || ch == TAB_CHAR || ch == 5760 || ((8192 <= ch && ch <= 8202 && ch != 8199) || ch == 8287 || ch == 12288);
    }

    private static boolean equalAttributes(TextPaint lp, TextPaint rp) {
        return lp.getColorFilter() == rp.getColorFilter() && lp.getMaskFilter() == rp.getMaskFilter() && lp.getShader() == rp.getShader() && lp.getTypeface() == rp.getTypeface() && lp.getXfermode() == rp.getXfermode() && lp.getTextLocales().equals(rp.getTextLocales()) && TextUtils.equals(lp.getFontFeatureSettings(), rp.getFontFeatureSettings()) && TextUtils.equals(lp.getFontVariationSettings(), rp.getFontVariationSettings()) && lp.getShadowLayerRadius() == rp.getShadowLayerRadius() && lp.getShadowLayerDx() == rp.getShadowLayerDx() && lp.getShadowLayerDy() == rp.getShadowLayerDy() && lp.getShadowLayerColor() == rp.getShadowLayerColor() && lp.getFlags() == rp.getFlags() && lp.getHinting() == rp.getHinting() && lp.getStyle() == rp.getStyle() && lp.getColor() == rp.getColor() && lp.getStrokeWidth() == rp.getStrokeWidth() && lp.getStrokeMiter() == rp.getStrokeMiter() && lp.getStrokeCap() == rp.getStrokeCap() && lp.getStrokeJoin() == rp.getStrokeJoin() && lp.getTextAlign() == rp.getTextAlign() && lp.isElegantTextHeight() == rp.isElegantTextHeight() && lp.getTextSize() == rp.getTextSize() && lp.getTextScaleX() == rp.getTextScaleX() && lp.getTextSkewX() == rp.getTextSkewX() && lp.getLetterSpacing() == rp.getLetterSpacing() && lp.getWordSpacing() == rp.getWordSpacing() && lp.getStartHyphenEdit() == rp.getStartHyphenEdit() && lp.getEndHyphenEdit() == rp.getEndHyphenEdit() && lp.bgColor == rp.bgColor && lp.baselineShift == rp.baselineShift && lp.linkColor == rp.linkColor && lp.drawableState == rp.drawableState && lp.density == rp.density && lp.underlineColor == rp.underlineColor && lp.underlineThickness == rp.underlineThickness;
    }
}
