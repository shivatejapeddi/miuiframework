package android.text;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.text.method.MetaKeyKeyListener;
import android.text.style.AlignmentSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2;
import android.text.style.LineBackgroundSpan;
import android.text.style.ParagraphStyle;
import android.text.style.ReplacementSpan;
import android.text.style.TabStopSpan;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public abstract class Layout {
    public static final int BREAK_STRATEGY_BALANCED = 2;
    public static final int BREAK_STRATEGY_HIGH_QUALITY = 1;
    public static final int BREAK_STRATEGY_SIMPLE = 0;
    public static final float DEFAULT_LINESPACING_ADDITION = 0.0f;
    public static final float DEFAULT_LINESPACING_MULTIPLIER = 1.0f;
    @VisibleForTesting(visibility = Visibility.PACKAGE)
    @UnsupportedAppUsage
    public static final Directions DIRS_ALL_LEFT_TO_RIGHT = new Directions(new int[]{0, RUN_LENGTH_MASK});
    @VisibleForTesting(visibility = Visibility.PACKAGE)
    @UnsupportedAppUsage
    public static final Directions DIRS_ALL_RIGHT_TO_LEFT = new Directions(new int[]{0, 134217727});
    public static final int DIR_LEFT_TO_RIGHT = 1;
    @UnsupportedAppUsage
    static final int DIR_REQUEST_DEFAULT_LTR = 2;
    static final int DIR_REQUEST_DEFAULT_RTL = -2;
    static final int DIR_REQUEST_LTR = 1;
    static final int DIR_REQUEST_RTL = -1;
    public static final int DIR_RIGHT_TO_LEFT = -1;
    public static final int HYPHENATION_FREQUENCY_FULL = 2;
    public static final int HYPHENATION_FREQUENCY_NONE = 0;
    public static final int HYPHENATION_FREQUENCY_NORMAL = 1;
    public static final int JUSTIFICATION_MODE_INTER_WORD = 1;
    public static final int JUSTIFICATION_MODE_NONE = 0;
    private static final ParagraphStyle[] NO_PARA_SPANS = ((ParagraphStyle[]) ArrayUtils.emptyArray(ParagraphStyle.class));
    static final int RUN_LENGTH_MASK = 67108863;
    static final int RUN_LEVEL_MASK = 63;
    static final int RUN_LEVEL_SHIFT = 26;
    static final int RUN_RTL_FLAG = 67108864;
    private static final float TAB_INCREMENT = 20.0f;
    public static final int TEXT_SELECTION_LAYOUT_LEFT_TO_RIGHT = 1;
    public static final int TEXT_SELECTION_LAYOUT_RIGHT_TO_LEFT = 0;
    private static final Rect sTempRect = new Rect();
    private Alignment mAlignment;
    private int mJustificationMode;
    private SpanSet<LineBackgroundSpan> mLineBackgroundSpans;
    @UnsupportedAppUsage
    private TextPaint mPaint;
    private float mSpacingAdd;
    private float mSpacingMult;
    private boolean mSpannedText;
    private CharSequence mText;
    private TextDirectionHeuristic mTextDir;
    private int mWidth;
    private TextPaint mWorkPaint;

    @FunctionalInterface
    public interface SelectionRectangleConsumer {
        void accept(float f, float f2, float f3, float f4, int i);
    }

    /* renamed from: android.text.Layout$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$text$Layout$Alignment = new int[Alignment.values().length];

        static {
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_NORMAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_OPPOSITE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_RIGHT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_LEFT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public enum Alignment {
        ALIGN_NORMAL,
        ALIGN_OPPOSITE,
        ALIGN_CENTER,
        ALIGN_LEFT,
        ALIGN_RIGHT
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BreakStrategy {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
    }

    public static class Directions {
        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public int[] mDirections;

        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public Directions(int[] dirs) {
            this.mDirections = dirs;
        }

        public int getRunCount() {
            return this.mDirections.length / 2;
        }

        public int getRunStart(int runIndex) {
            return this.mDirections[runIndex * 2];
        }

        public int getRunLength(int runIndex) {
            return this.mDirections[(runIndex * 2) + 1] & Layout.RUN_LENGTH_MASK;
        }

        public boolean isRunRtl(int runIndex) {
            return (this.mDirections[(runIndex * 2) + 1] & 67108864) != 0;
        }
    }

    static class Ellipsizer implements CharSequence, GetChars {
        Layout mLayout;
        TruncateAt mMethod;
        CharSequence mText;
        int mWidth;

        public Ellipsizer(CharSequence s) {
            this.mText = s;
        }

        public char charAt(int off) {
            char[] buf = TextUtils.obtain(1);
            getChars(off, off + 1, buf, 0);
            char ret = buf[0];
            TextUtils.recycle(buf);
            return ret;
        }

        public void getChars(int start, int end, char[] dest, int destoff) {
            int line1 = this.mLayout.getLineForOffset(start);
            int line2 = this.mLayout.getLineForOffset(end);
            TextUtils.getChars(this.mText, start, end, dest, destoff);
            for (int i = line1; i <= line2; i++) {
                this.mLayout.ellipsize(start, end, i, dest, destoff, this.mMethod);
            }
        }

        public int length() {
            return this.mText.length();
        }

        public CharSequence subSequence(int start, int end) {
            char[] s = new char[(end - start)];
            getChars(start, end, s, 0);
            return new String(s);
        }

        public String toString() {
            char[] s = new char[length()];
            getChars(0, length(), s, 0);
            return new String(s);
        }
    }

    private class HorizontalMeasurementProvider {
        private float[] mHorizontals;
        private final int mLine;
        private int mLineStartOffset;
        private final boolean mPrimary;

        HorizontalMeasurementProvider(int line, boolean primary) {
            this.mLine = line;
            this.mPrimary = primary;
            init();
        }

        private void init() {
            if (Layout.this.getLineDirections(this.mLine) != Layout.DIRS_ALL_LEFT_TO_RIGHT) {
                this.mHorizontals = Layout.this.getLineHorizontals(this.mLine, false, this.mPrimary);
                this.mLineStartOffset = Layout.this.getLineStart(this.mLine);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public float get(int offset) {
            int index = offset - this.mLineStartOffset;
            float[] fArr = this.mHorizontals;
            if (fArr == null || index < 0 || index >= fArr.length) {
                return Layout.this.getHorizontal(offset, this.mPrimary);
            }
            return fArr[index];
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface HyphenationFrequency {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface JustificationMode {
    }

    static class SpannedEllipsizer extends Ellipsizer implements Spanned {
        private Spanned mSpanned;

        public SpannedEllipsizer(CharSequence display) {
            super(display);
            this.mSpanned = (Spanned) display;
        }

        public <T> T[] getSpans(int start, int end, Class<T> type) {
            return this.mSpanned.getSpans(start, end, type);
        }

        public int getSpanStart(Object tag) {
            return this.mSpanned.getSpanStart(tag);
        }

        public int getSpanEnd(Object tag) {
            return this.mSpanned.getSpanEnd(tag);
        }

        public int getSpanFlags(Object tag) {
            return this.mSpanned.getSpanFlags(tag);
        }

        public int nextSpanTransition(int start, int limit, Class type) {
            return this.mSpanned.nextSpanTransition(start, limit, type);
        }

        public CharSequence subSequence(int start, int end) {
            char[] s = new char[(end - start)];
            getChars(start, end, s, 0);
            SpannableString ss = new SpannableString(new String(s));
            TextUtils.copySpansFrom(this.mSpanned, start, end, Object.class, ss, 0);
            return ss;
        }
    }

    @VisibleForTesting(visibility = Visibility.PACKAGE)
    public static class TabStops {
        private float mIncrement;
        private int mNumStops;
        private float[] mStops;

        public TabStops(float increment, Object[] spans) {
            reset(increment, spans);
        }

        /* Access modifiers changed, original: 0000 */
        public void reset(float increment, Object[] spans) {
            this.mIncrement = increment;
            int ns = 0;
            if (spans != null) {
                float[] stops = this.mStops;
                float[] stops2 = stops;
                int ns2 = 0;
                for (Object o : spans) {
                    if (o instanceof TabStopSpan) {
                        if (stops2 == null) {
                            stops2 = new float[10];
                        } else if (ns2 == stops2.length) {
                            float[] nstops = new float[(ns2 * 2)];
                            for (int i = 0; i < ns2; i++) {
                                nstops[i] = stops2[i];
                            }
                            stops2 = nstops;
                        }
                        int ns3 = ns2 + 1;
                        stops2[ns2] = (float) ((TabStopSpan) o).getTabStop();
                        ns2 = ns3;
                    }
                }
                if (ns2 > 1) {
                    Arrays.sort(stops2, 0, ns2);
                }
                if (stops2 != this.mStops) {
                    this.mStops = stops2;
                }
                ns = ns2;
            }
            this.mNumStops = ns;
        }

        /* Access modifiers changed, original: 0000 */
        public float nextTab(float h) {
            int ns = this.mNumStops;
            if (ns > 0) {
                float[] stops = this.mStops;
                for (int i = 0; i < ns; i++) {
                    float stop = stops[i];
                    if (stop > h) {
                        return stop;
                    }
                }
            }
            return nextDefaultStop(h, this.mIncrement);
        }

        public static float nextDefaultStop(float h, float inc) {
            return ((float) ((int) ((h + inc) / inc))) * inc;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TextSelectionLayout {
    }

    public abstract int getBottomPadding();

    public abstract int getEllipsisCount(int i);

    public abstract int getEllipsisStart(int i);

    public abstract boolean getLineContainsTab(int i);

    public abstract int getLineCount();

    public abstract int getLineDescent(int i);

    public abstract Directions getLineDirections(int i);

    public abstract int getLineStart(int i);

    public abstract int getLineTop(int i);

    public abstract int getParagraphDirection(int i);

    public abstract int getTopPadding();

    public static float getDesiredWidth(CharSequence source, TextPaint paint) {
        return getDesiredWidth(source, 0, source.length(), paint);
    }

    public static float getDesiredWidth(CharSequence source, int start, int end, TextPaint paint) {
        return getDesiredWidth(source, start, end, paint, TextDirectionHeuristics.FIRSTSTRONG_LTR);
    }

    public static float getDesiredWidth(CharSequence source, int start, int end, TextPaint paint, TextDirectionHeuristic textDir) {
        return getDesiredWidthWithLimit(source, start, end, paint, textDir, Float.MAX_VALUE);
    }

    public static float getDesiredWidthWithLimit(CharSequence source, int start, int end, TextPaint paint, TextDirectionHeuristic textDir, float upperLimit) {
        float need = 0.0f;
        int i = start;
        while (i <= end) {
            int next = TextUtils.indexOf(source, (char) 10, i, end);
            if (next < 0) {
                next = end;
            }
            float w = measurePara(paint, source, i, next, textDir);
            if (w > upperLimit) {
                return upperLimit;
            }
            if (w > need) {
                need = w;
            }
            i = next + 1;
        }
        return need;
    }

    protected Layout(CharSequence text, TextPaint paint, int width, Alignment align, float spacingMult, float spacingAdd) {
        this(text, paint, width, align, TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingMult, spacingAdd);
    }

    protected Layout(CharSequence text, TextPaint paint, int width, Alignment align, TextDirectionHeuristic textDir, float spacingMult, float spacingAdd) {
        this.mWorkPaint = new TextPaint();
        this.mAlignment = Alignment.ALIGN_NORMAL;
        if (width >= 0) {
            if (paint != null) {
                paint.bgColor = 0;
                paint.baselineShift = 0;
            }
            this.mText = text;
            this.mPaint = paint;
            this.mWidth = width;
            this.mAlignment = align;
            this.mSpacingMult = spacingMult;
            this.mSpacingAdd = spacingAdd;
            this.mSpannedText = text instanceof Spanned;
            this.mTextDir = textDir;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Layout: ");
        stringBuilder.append(width);
        stringBuilder.append(" < 0");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: protected */
    public void setJustificationMode(int justificationMode) {
        this.mJustificationMode = justificationMode;
    }

    /* Access modifiers changed, original: 0000 */
    public void replaceWith(CharSequence text, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd) {
        if (width >= 0) {
            this.mText = text;
            this.mPaint = paint;
            this.mWidth = width;
            this.mAlignment = align;
            this.mSpacingMult = spacingmult;
            this.mSpacingAdd = spacingadd;
            this.mSpannedText = text instanceof Spanned;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Layout: ");
        stringBuilder.append(width);
        stringBuilder.append(" < 0");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void draw(Canvas c) {
        draw(c, null, null, 0);
    }

    public void draw(Canvas canvas, Path highlight, Paint highlightPaint, int cursorOffsetVertical) {
        long lineRange = getLineRangeForDraw(canvas);
        int firstLine = TextUtils.unpackRangeStartFromLong(lineRange);
        int lastLine = TextUtils.unpackRangeEndFromLong(lineRange);
        if (lastLine >= 0) {
            drawBackground(canvas, highlight, highlightPaint, cursorOffsetVertical, firstLine, lastLine);
            drawText(canvas, firstLine, lastLine);
        }
    }

    private boolean isJustificationRequired(int lineNum) {
        boolean z = false;
        if (this.mJustificationMode == 0) {
            return false;
        }
        int lineEnd = getLineEnd(lineNum);
        if (lineEnd < this.mText.length() && this.mText.charAt(lineEnd - 1) != 10) {
            z = true;
        }
        return z;
    }

    private float getJustifyWidth(int lineNum) {
        int n;
        int i = lineNum;
        Alignment paraAlign = this.mAlignment;
        int left = 0;
        int right = this.mWidth;
        int dir = getParagraphDirection(lineNum);
        ParagraphStyle[] spans = NO_PARA_SPANS;
        if (this.mSpannedText) {
            Spanned sp = this.mText;
            int start = getLineStart(lineNum);
            boolean isFirstParaLine = start == 0 || this.mText.charAt(start - 1) == 10;
            if (isFirstParaLine) {
                spans = (ParagraphStyle[]) getParagraphSpans(sp, start, sp.nextSpanTransition(start, this.mText.length(), ParagraphStyle.class), ParagraphStyle.class);
                for (int n2 = spans.length - 1; n2 >= 0; n2--) {
                    if (spans[n2] instanceof AlignmentSpan) {
                        paraAlign = ((AlignmentSpan) spans[n2]).getAlignment();
                        break;
                    }
                }
            }
            int length = spans.length;
            boolean useFirstLineMargin = isFirstParaLine;
            for (int n3 = 0; n3 < length; n3++) {
                if (spans[n3] instanceof LeadingMarginSpan2) {
                    if (i < getLineForOffset(sp.getSpanStart(spans[n3])) + ((LeadingMarginSpan2) spans[n3]).getLeadingMarginLineCount()) {
                        useFirstLineMargin = true;
                        break;
                    }
                }
            }
            for (n = 0; n < length; n++) {
                if (spans[n] instanceof LeadingMarginSpan) {
                    LeadingMarginSpan margin = spans[n];
                    if (dir == -1) {
                        right -= margin.getLeadingMargin(useFirstLineMargin);
                    } else {
                        left += margin.getLeadingMargin(useFirstLineMargin);
                    }
                }
            }
        }
        Alignment align = paraAlign == Alignment.ALIGN_LEFT ? dir == 1 ? Alignment.ALIGN_NORMAL : Alignment.ALIGN_OPPOSITE : paraAlign == Alignment.ALIGN_RIGHT ? dir == 1 ? Alignment.ALIGN_OPPOSITE : Alignment.ALIGN_NORMAL : paraAlign;
        if (align == Alignment.ALIGN_NORMAL) {
            if (dir == 1) {
                n = getIndentAdjust(i, Alignment.ALIGN_LEFT);
            } else {
                n = -getIndentAdjust(i, Alignment.ALIGN_RIGHT);
            }
        } else if (align != Alignment.ALIGN_OPPOSITE) {
            n = getIndentAdjust(i, Alignment.ALIGN_CENTER);
        } else if (dir == 1) {
            n = -getIndentAdjust(i, Alignment.ALIGN_RIGHT);
        } else {
            n = getIndentAdjust(i, Alignment.ALIGN_LEFT);
        }
        return (float) ((right - left) - n);
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x011d  */
    @android.annotation.UnsupportedAppUsage
    public void drawText(android.graphics.Canvas r44, int r45, int r46) {
        /*
        r43 = this;
        r15 = r43;
        r14 = r45;
        r0 = r15.getLineTop(r14);
        r1 = r15.getLineStart(r14);
        r2 = NO_PARA_SPANS;
        r3 = 0;
        r13 = r15.mWorkPaint;
        r4 = r15.mPaint;
        r13.set(r4);
        r12 = r15.mText;
        r4 = r15.mAlignment;
        r5 = 0;
        r6 = 0;
        r11 = android.text.TextLine.obtain();
        r7 = r45;
        r9 = r5;
        r10 = r7;
    L_0x0024:
        r8 = r46;
        if (r10 > r8) goto L_0x0363;
    L_0x0028:
        r7 = r1;
        r5 = r10 + 1;
        r1 = r15.getLineStart(r5);
        r20 = r15.isJustificationRequired(r10);
        r21 = r15.getLineVisibleEnd(r10, r7, r1);
        r5 = r15.getStartHyphenEdit(r10);
        r13.setStartHyphenEdit(r5);
        r5 = r15.getEndHyphenEdit(r10);
        r13.setEndHyphenEdit(r5);
        r5 = r0;
        r16 = r0;
        r0 = r10 + 1;
        r22 = r15.getLineTop(r0);
        r23 = r22;
        r0 = r15.getLineDescent(r10);
        r0 = r22 - r0;
        r24 = r5;
        r5 = r15.getParagraphDirection(r10);
        r16 = 0;
        r17 = r0;
        r0 = r15.mWidth;
        r18 = r0;
        r0 = r15.mSpannedText;
        r19 = r11;
        if (r0 == 0) goto L_0x0209;
    L_0x006a:
        r0 = r12;
        r0 = (android.text.Spanned) r0;
        r11 = r12.length();
        if (r7 == 0) goto L_0x0084;
    L_0x0073:
        r27 = r1;
        r1 = r7 + -1;
        r1 = r12.charAt(r1);
        r28 = r2;
        r2 = 10;
        if (r1 != r2) goto L_0x0082;
    L_0x0081:
        goto L_0x0088;
    L_0x0082:
        r1 = 0;
        goto L_0x0089;
    L_0x0084:
        r27 = r1;
        r28 = r2;
    L_0x0088:
        r1 = 1;
    L_0x0089:
        r29 = r1;
        if (r7 < r3) goto L_0x00d1;
    L_0x008d:
        if (r10 == r14) goto L_0x0095;
    L_0x008f:
        if (r29 == 0) goto L_0x0092;
    L_0x0091:
        goto L_0x0095;
    L_0x0092:
        r26 = 1;
        goto L_0x00d3;
    L_0x0095:
        r1 = android.text.style.ParagraphStyle.class;
        r3 = r0.nextSpanTransition(r7, r11, r1);
        r1 = android.text.style.ParagraphStyle.class;
        r1 = getParagraphSpans(r0, r7, r3, r1);
        r2 = r1;
        r2 = (android.text.style.ParagraphStyle[]) r2;
        r1 = r15.mAlignment;
        r4 = r2.length;
        r26 = 1;
        r4 = r4 + -1;
    L_0x00ab:
        if (r4 < 0) goto L_0x00c4;
    L_0x00ad:
        r28 = r1;
        r1 = r2[r4];
        r1 = r1 instanceof android.text.style.AlignmentSpan;
        if (r1 == 0) goto L_0x00bf;
    L_0x00b5:
        r1 = r2[r4];
        r1 = (android.text.style.AlignmentSpan) r1;
        r1 = r1.getAlignment();
        r4 = r1;
        goto L_0x00c8;
    L_0x00bf:
        r4 = r4 + -1;
        r1 = r28;
        goto L_0x00ab;
    L_0x00c4:
        r28 = r1;
        r4 = r28;
    L_0x00c8:
        r1 = 0;
        r30 = r1;
        r6 = r2;
        r31 = r3;
        r28 = r4;
        goto L_0x00db;
    L_0x00d1:
        r26 = 1;
    L_0x00d3:
        r31 = r3;
        r30 = r6;
        r6 = r28;
        r28 = r4;
    L_0x00db:
        r4 = r6.length;
        r1 = r29;
        r2 = 0;
    L_0x00df:
        if (r2 >= r4) goto L_0x010f;
    L_0x00e1:
        r3 = r6[r2];
        r3 = r3 instanceof android.text.style.LeadingMarginSpan.LeadingMarginSpan2;
        if (r3 == 0) goto L_0x0104;
    L_0x00e7:
        r3 = r6[r2];
        r3 = (android.text.style.LeadingMarginSpan.LeadingMarginSpan2) r3;
        r3 = r3.getLeadingMarginLineCount();
        r32 = r1;
        r1 = r6[r2];
        r1 = r0.getSpanStart(r1);
        r1 = r15.getLineForOffset(r1);
        r33 = r0;
        r0 = r1 + r3;
        if (r10 >= r0) goto L_0x0108;
    L_0x0101:
        r0 = 1;
        r3 = r0;
        goto L_0x0115;
    L_0x0104:
        r33 = r0;
        r32 = r1;
    L_0x0108:
        r2 = r2 + 1;
        r1 = r32;
        r0 = r33;
        goto L_0x00df;
    L_0x010f:
        r33 = r0;
        r32 = r1;
        r3 = r32;
    L_0x0115:
        r0 = 0;
        r2 = r0;
        r32 = r16;
        r34 = r18;
    L_0x011b:
        if (r2 >= r4) goto L_0x01e7;
    L_0x011d:
        r0 = r6[r2];
        r0 = r0 instanceof android.text.style.LeadingMarginSpan;
        if (r0 == 0) goto L_0x01a8;
    L_0x0123:
        r0 = r6[r2];
        r1 = r0;
        r1 = (android.text.style.LeadingMarginSpan) r1;
        r0 = -1;
        if (r5 != r0) goto L_0x0168;
    L_0x012b:
        r35 = r17;
        r0 = r1;
        r15 = r1;
        r1 = r44;
        r36 = r2;
        r2 = r13;
        r14 = r3;
        r3 = r34;
        r37 = r4;
        r4 = r5;
        r38 = r6;
        r6 = r35;
        r39 = r7;
        r7 = r22;
        r8 = r12;
        r40 = r9;
        r9 = r39;
        r41 = r10;
        r10 = r21;
        r42 = r19;
        r19 = r11;
        r11 = r29;
        r25 = r12;
        r12 = r43;
        r26 = r5;
        r5 = r24;
        r0.drawLeadingMargin(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12);
        r0 = r15.getLeadingMargin(r14);
        r34 = r34 - r0;
        r2 = r43;
        r0 = r13;
        r1 = r14;
        goto L_0x01c3;
    L_0x0168:
        r15 = r1;
        r36 = r2;
        r14 = r3;
        r37 = r4;
        r26 = r5;
        r38 = r6;
        r39 = r7;
        r40 = r9;
        r41 = r10;
        r25 = r12;
        r35 = r17;
        r42 = r19;
        r5 = r24;
        r19 = r11;
        r6 = r15;
        r7 = r44;
        r8 = r13;
        r9 = r32;
        r10 = r26;
        r11 = r5;
        r12 = r35;
        r0 = r13;
        r13 = r22;
        r1 = r14;
        r14 = r25;
        r2 = r43;
        r3 = r15;
        r15 = r39;
        r16 = r21;
        r17 = r29;
        r18 = r43;
        r6.drawLeadingMargin(r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);
        r4 = r3.getLeadingMargin(r1);
        r32 = r32 + r4;
        goto L_0x01c3;
    L_0x01a8:
        r36 = r2;
        r1 = r3;
        r37 = r4;
        r26 = r5;
        r38 = r6;
        r39 = r7;
        r40 = r9;
        r41 = r10;
        r25 = r12;
        r0 = r13;
        r2 = r15;
        r35 = r17;
        r42 = r19;
        r5 = r24;
        r19 = r11;
    L_0x01c3:
        r3 = r36 + 1;
        r14 = r45;
        r8 = r46;
        r13 = r0;
        r15 = r2;
        r2 = r3;
        r24 = r5;
        r11 = r19;
        r12 = r25;
        r5 = r26;
        r17 = r35;
        r4 = r37;
        r6 = r38;
        r7 = r39;
        r9 = r40;
        r10 = r41;
        r19 = r42;
        r26 = 1;
        r3 = r1;
        goto L_0x011b;
    L_0x01e7:
        r36 = r2;
        r1 = r3;
        r37 = r4;
        r26 = r5;
        r38 = r6;
        r39 = r7;
        r40 = r9;
        r41 = r10;
        r25 = r12;
        r0 = r13;
        r2 = r15;
        r35 = r17;
        r42 = r19;
        r5 = r24;
        r19 = r11;
        r4 = r28;
        r3 = r31;
        r1 = r38;
        goto L_0x0227;
    L_0x0209:
        r27 = r1;
        r28 = r2;
        r26 = r5;
        r39 = r7;
        r40 = r9;
        r41 = r10;
        r25 = r12;
        r0 = r13;
        r2 = r15;
        r35 = r17;
        r42 = r19;
        r5 = r24;
        r30 = r6;
        r32 = r16;
        r34 = r18;
        r1 = r28;
    L_0x0227:
        r15 = r41;
        r24 = r2.getLineContainsTab(r15);
        if (r24 == 0) goto L_0x0247;
    L_0x022f:
        if (r30 != 0) goto L_0x0247;
    L_0x0231:
        r6 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r7 = r40;
        if (r7 != 0) goto L_0x023f;
    L_0x0237:
        r8 = new android.text.Layout$TabStops;
        r8.<init>(r6, r1);
        r9 = r8;
        r7 = r9;
        goto L_0x0242;
    L_0x023f:
        r7.reset(r6, r1);
    L_0x0242:
        r6 = 1;
        r30 = r6;
        r14 = r7;
        goto L_0x024a;
    L_0x0247:
        r7 = r40;
        r14 = r7;
    L_0x024a:
        r6 = r4;
        r7 = android.text.Layout.Alignment.ALIGN_LEFT;
        if (r6 != r7) goto L_0x025c;
    L_0x024f:
        r13 = r26;
        r7 = 1;
        if (r13 != r7) goto L_0x0257;
    L_0x0254:
        r8 = android.text.Layout.Alignment.ALIGN_NORMAL;
        goto L_0x0259;
    L_0x0257:
        r8 = android.text.Layout.Alignment.ALIGN_OPPOSITE;
    L_0x0259:
        r6 = r8;
        r12 = r6;
        goto L_0x026e;
    L_0x025c:
        r13 = r26;
        r7 = 1;
        r8 = android.text.Layout.Alignment.ALIGN_RIGHT;
        if (r6 != r8) goto L_0x026d;
    L_0x0263:
        if (r13 != r7) goto L_0x0268;
    L_0x0265:
        r8 = android.text.Layout.Alignment.ALIGN_OPPOSITE;
        goto L_0x026a;
    L_0x0268:
        r8 = android.text.Layout.Alignment.ALIGN_NORMAL;
    L_0x026a:
        r6 = r8;
        r12 = r6;
        goto L_0x026e;
    L_0x026d:
        r12 = r6;
    L_0x026e:
        r6 = android.text.Layout.Alignment.ALIGN_NORMAL;
        if (r12 != r6) goto L_0x028d;
    L_0x0272:
        if (r13 != r7) goto L_0x0280;
    L_0x0274:
        r6 = android.text.Layout.Alignment.ALIGN_LEFT;
        r6 = r2.getIndentAdjust(r15, r6);
        r7 = r32 + r6;
        r26 = r6;
        r11 = r7;
        goto L_0x02c5;
    L_0x0280:
        r6 = android.text.Layout.Alignment.ALIGN_RIGHT;
        r6 = r2.getIndentAdjust(r15, r6);
        r6 = -r6;
        r7 = r34 - r6;
        r26 = r6;
        r11 = r7;
        goto L_0x02c5;
    L_0x028d:
        r6 = 0;
        r6 = r2.getLineExtent(r15, r14, r6);
        r6 = (int) r6;
        r8 = android.text.Layout.Alignment.ALIGN_OPPOSITE;
        if (r12 != r8) goto L_0x02b4;
    L_0x0297:
        if (r13 != r7) goto L_0x02a7;
    L_0x0299:
        r7 = android.text.Layout.Alignment.ALIGN_RIGHT;
        r7 = r2.getIndentAdjust(r15, r7);
        r7 = -r7;
        r8 = r34 - r6;
        r8 = r8 - r7;
        r26 = r7;
        r11 = r8;
        goto L_0x02c5;
    L_0x02a7:
        r7 = android.text.Layout.Alignment.ALIGN_LEFT;
        r7 = r2.getIndentAdjust(r15, r7);
        r8 = r32 - r6;
        r8 = r8 + r7;
        r26 = r7;
        r11 = r8;
        goto L_0x02c5;
    L_0x02b4:
        r8 = android.text.Layout.Alignment.ALIGN_CENTER;
        r8 = r2.getIndentAdjust(r15, r8);
        r6 = r6 & -2;
        r9 = r34 + r32;
        r9 = r9 - r6;
        r7 = r9 >> 1;
        r7 = r7 + r8;
        r11 = r7;
        r26 = r8;
    L_0x02c5:
        r10 = r2.getLineDirections(r15);
        r6 = DIRS_ALL_LEFT_TO_RIGHT;
        if (r10 != r6) goto L_0x02fd;
    L_0x02cd:
        r6 = r2.mSpannedText;
        if (r6 != 0) goto L_0x02fd;
    L_0x02d1:
        if (r24 != 0) goto L_0x02fd;
    L_0x02d3:
        if (r20 != 0) goto L_0x02fd;
    L_0x02d5:
        r9 = (float) r11;
        r8 = r35;
        r7 = (float) r8;
        r6 = r44;
        r16 = r7;
        r7 = r25;
        r28 = r8;
        r8 = r39;
        r17 = r9;
        r9 = r21;
        r29 = r10;
        r10 = r17;
        r31 = r1;
        r1 = r11;
        r11 = r16;
        r33 = r12;
        r12 = r0;
        r6.drawText(r7, r8, r9, r10, r11, r12);
        r36 = r14;
        r37 = r15;
        r8 = r42;
        goto L_0x034e;
    L_0x02fd:
        r31 = r1;
        r29 = r10;
        r1 = r11;
        r33 = r12;
        r28 = r35;
        r16 = r2.getEllipsisStart(r15);
        r6 = r2.getEllipsisStart(r15);
        r7 = r2.getEllipsisCount(r15);
        r17 = r6 + r7;
        r6 = r42;
        r7 = r0;
        r8 = r25;
        r9 = r39;
        r10 = r21;
        r11 = r13;
        r12 = r29;
        r35 = r13;
        r13 = r24;
        r36 = r14;
        r37 = r15;
        r15 = r16;
        r16 = r17;
        r6.set(r7, r8, r9, r10, r11, r12, r13, r14, r15, r16);
        if (r20 == 0) goto L_0x033d;
    L_0x0332:
        r6 = r34 - r32;
        r6 = r6 - r26;
        r6 = (float) r6;
        r8 = r42;
        r8.justify(r6);
        goto L_0x033f;
    L_0x033d:
        r8 = r42;
    L_0x033f:
        r6 = (float) r1;
        r14 = r8;
        r15 = r44;
        r16 = r6;
        r17 = r5;
        r18 = r28;
        r19 = r22;
        r14.draw(r15, r16, r17, r18, r19);
    L_0x034e:
        r10 = r37 + 1;
        r14 = r45;
        r13 = r0;
        r15 = r2;
        r11 = r8;
        r0 = r23;
        r12 = r25;
        r1 = r27;
        r6 = r30;
        r2 = r31;
        r9 = r36;
        goto L_0x0024;
    L_0x0363:
        r8 = r11;
        android.text.TextLine.recycle(r8);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.Layout.drawText(android.graphics.Canvas, int, int):void");
    }

    @UnsupportedAppUsage
    public void drawBackground(Canvas canvas, Path highlight, Paint highlightPaint, int cursorOffsetVertical, int firstLine, int lastLine) {
        Canvas canvas2 = canvas;
        int i = cursorOffsetVertical;
        int i2 = firstLine;
        if (this.mSpannedText) {
            if (this.mLineBackgroundSpans == null) {
                this.mLineBackgroundSpans = new SpanSet(LineBackgroundSpan.class);
            }
            Spanned buffer = (Spanned) this.mText;
            int textLength = buffer.length();
            this.mLineBackgroundSpans.init(buffer, 0, textLength);
            Spanned buffer2;
            if (this.mLineBackgroundSpans.numberOfSpans > 0) {
                int previousLineEnd;
                int i3;
                int width;
                int textLength2;
                int previousLineBottom = getLineTop(i2);
                int previousLineEnd2 = getLineStart(i2);
                ParagraphStyle[] spans = NO_PARA_SPANS;
                int spansLength = 0;
                TextPaint paint = this.mPaint;
                int spanEnd = 0;
                int width2 = this.mWidth;
                int i4 = firstLine;
                while (i4 <= lastLine) {
                    int spanEnd2;
                    int i5;
                    ParagraphStyle[] spans2;
                    int start;
                    int n;
                    Paint paint2;
                    Paint paint3;
                    int start2 = previousLineEnd2;
                    previousLineEnd2 = getLineStart(i4 + 1);
                    previousLineEnd = previousLineEnd2;
                    int spansLength2 = spansLength;
                    spansLength = start2;
                    start2 = previousLineBottom;
                    int lbottom = getLineTop(i4 + 1);
                    int previousLineBottom2 = lbottom;
                    int lbaseline = lbottom - getLineDescent(i4);
                    if (previousLineEnd2 >= spanEnd) {
                        previousLineBottom = this.mLineBackgroundSpans.getNextTransition(spansLength, textLength);
                        spanEnd = 0;
                        if (spansLength != previousLineEnd2 || spansLength == 0) {
                            spanEnd2 = previousLineBottom;
                            previousLineBottom = spans;
                            spans = null;
                            while (spans < this.mLineBackgroundSpans.numberOfSpans) {
                                if (this.mLineBackgroundSpans.spanStarts[spans] < previousLineEnd2 && this.mLineBackgroundSpans.spanEnds[spans] > spansLength) {
                                    previousLineBottom = (ParagraphStyle[]) GrowingArrayUtils.append((Object[]) previousLineBottom, spanEnd, ((LineBackgroundSpan[]) this.mLineBackgroundSpans.spans)[spans]);
                                    spanEnd++;
                                }
                                spans++;
                                i5 = lastLine;
                            }
                            spans2 = previousLineBottom;
                            i5 = spanEnd;
                        } else {
                            spanEnd2 = previousLineBottom;
                            spans2 = spans;
                            i5 = 0;
                        }
                    } else {
                        spanEnd2 = spanEnd;
                        i5 = spansLength2;
                        spans2 = spans;
                    }
                    spanEnd = 0;
                    while (spanEnd < i5) {
                        int end = previousLineEnd2;
                        start = spansLength;
                        n = spanEnd;
                        int spansLength3 = i5;
                        i3 = i4;
                        width = width2;
                        paint2 = paint3;
                        textLength2 = textLength;
                        buffer2 = buffer;
                        ((LineBackgroundSpan) spans2[spanEnd]).drawBackground(canvas, paint3, 0, width2, start2, lbaseline, lbottom, buffer, start, end, i3);
                        spanEnd = n + 1;
                        previousLineEnd2 = end;
                        spansLength = start;
                        i5 = spansLength3;
                        i4 = i3;
                        width2 = width;
                        paint3 = paint2;
                        textLength = textLength2;
                        buffer = buffer2;
                    }
                    start = spansLength;
                    n = spanEnd;
                    width = width2;
                    paint2 = paint3;
                    textLength2 = textLength;
                    buffer2 = buffer;
                    i4++;
                    previousLineEnd2 = previousLineEnd;
                    spans = spans2;
                    previousLineBottom = previousLineBottom2;
                    spanEnd = spanEnd2;
                    spansLength = i5;
                }
                previousLineEnd = previousLineEnd2;
                i3 = i4;
                width = width2;
                TextPaint textPaint = paint3;
                textLength2 = textLength;
                buffer2 = buffer;
            } else {
                buffer2 = buffer;
            }
            this.mLineBackgroundSpans.recycle();
        }
        if (highlight != null) {
            if (i != 0) {
                canvas2.translate(0.0f, (float) i);
            }
            canvas.drawPath(highlight, highlightPaint);
            if (i != 0) {
                canvas2.translate(0.0f, (float) (-i));
            }
        }
    }

    /* JADX WARNING: Missing block: B:10:0x001c, code skipped:
            r0 = java.lang.Math.max(r1, 0);
            r5 = java.lang.Math.min(getLineTop(getLineCount()), r4);
     */
    /* JADX WARNING: Missing block: B:11:0x002c, code skipped:
            if (r0 < r5) goto L_0x0033;
     */
    /* JADX WARNING: Missing block: B:13:0x0032, code skipped:
            return android.text.TextUtils.packRangeInLong(0, -1);
     */
    /* JADX WARNING: Missing block: B:15:0x003f, code skipped:
            return android.text.TextUtils.packRangeInLong(getLineForVertical(r0), getLineForVertical(r5));
     */
    @android.annotation.UnsupportedAppUsage
    public long getLineRangeForDraw(android.graphics.Canvas r7) {
        /*
        r6 = this;
        r0 = sTempRect;
        monitor-enter(r0);
        r1 = sTempRect;	 Catch:{ all -> 0x0040 }
        r1 = r7.getClipBounds(r1);	 Catch:{ all -> 0x0040 }
        r2 = -1;
        r3 = 0;
        if (r1 != 0) goto L_0x0013;
    L_0x000d:
        r1 = android.text.TextUtils.packRangeInLong(r3, r2);	 Catch:{ all -> 0x0040 }
        monitor-exit(r0);	 Catch:{ all -> 0x0040 }
        return r1;
    L_0x0013:
        r1 = sTempRect;	 Catch:{ all -> 0x0040 }
        r1 = r1.top;	 Catch:{ all -> 0x0040 }
        r4 = sTempRect;	 Catch:{ all -> 0x0040 }
        r4 = r4.bottom;	 Catch:{ all -> 0x0040 }
        monitor-exit(r0);	 Catch:{ all -> 0x0040 }
        r0 = java.lang.Math.max(r1, r3);
        r5 = r6.getLineCount();
        r5 = r6.getLineTop(r5);
        r5 = java.lang.Math.min(r5, r4);
        if (r0 < r5) goto L_0x0033;
    L_0x002e:
        r2 = android.text.TextUtils.packRangeInLong(r3, r2);
        return r2;
    L_0x0033:
        r2 = r6.getLineForVertical(r0);
        r3 = r6.getLineForVertical(r5);
        r2 = android.text.TextUtils.packRangeInLong(r2, r3);
        return r2;
    L_0x0040:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0040 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.Layout.getLineRangeForDraw(android.graphics.Canvas):long");
    }

    private int getLineStartPos(int line, int left, int right) {
        Alignment align = getParagraphAlignment(line);
        int dir = getParagraphDirection(line);
        if (align == Alignment.ALIGN_LEFT) {
            align = dir == 1 ? Alignment.ALIGN_NORMAL : Alignment.ALIGN_OPPOSITE;
        } else if (align == Alignment.ALIGN_RIGHT) {
            align = dir == 1 ? Alignment.ALIGN_OPPOSITE : Alignment.ALIGN_NORMAL;
        }
        if (align != Alignment.ALIGN_NORMAL) {
            TabStops tabStops = null;
            if (this.mSpannedText && getLineContainsTab(line)) {
                Spanned spanned = this.mText;
                int start = getLineStart(line);
                TabStopSpan[] tabSpans = (TabStopSpan[]) getParagraphSpans(spanned, start, spanned.nextSpanTransition(start, spanned.length(), TabStopSpan.class), TabStopSpan.class);
                if (tabSpans.length > 0) {
                    tabStops = new TabStops(TAB_INCREMENT, tabSpans);
                }
            }
            int max = (int) getLineExtent(line, tabStops, false);
            if (align != Alignment.ALIGN_OPPOSITE) {
                return ((left + right) - (max & -2)) >> (getIndentAdjust(line, Alignment.ALIGN_CENTER) + 1);
            } else if (dir == 1) {
                return (right - max) + getIndentAdjust(line, Alignment.ALIGN_RIGHT);
            } else {
                return (left - max) + getIndentAdjust(line, Alignment.ALIGN_LEFT);
            }
        } else if (dir == 1) {
            return getIndentAdjust(line, Alignment.ALIGN_LEFT) + left;
        } else {
            return getIndentAdjust(line, Alignment.ALIGN_RIGHT) + right;
        }
    }

    public final CharSequence getText() {
        return this.mText;
    }

    public final TextPaint getPaint() {
        return this.mPaint;
    }

    public final int getWidth() {
        return this.mWidth;
    }

    public int getEllipsizedWidth() {
        return this.mWidth;
    }

    public final void increaseWidthTo(int wid) {
        if (wid >= this.mWidth) {
            this.mWidth = wid;
            return;
        }
        throw new RuntimeException("attempted to reduce Layout width");
    }

    public int getHeight() {
        return getLineTop(getLineCount());
    }

    public int getHeight(boolean cap) {
        return getHeight();
    }

    public final Alignment getAlignment() {
        return this.mAlignment;
    }

    public final float getSpacingMultiplier() {
        return this.mSpacingMult;
    }

    public final float getSpacingAdd() {
        return this.mSpacingAdd;
    }

    public final TextDirectionHeuristic getTextDirectionHeuristic() {
        return this.mTextDir;
    }

    public int getLineBounds(int line, Rect bounds) {
        if (bounds != null) {
            bounds.left = 0;
            bounds.top = getLineTop(line);
            bounds.right = this.mWidth;
            bounds.bottom = getLineTop(line + 1);
        }
        return getLineBaseline(line);
    }

    public int getStartHyphenEdit(int line) {
        return 0;
    }

    public int getEndHyphenEdit(int line) {
        return 0;
    }

    public int getIndentAdjust(int line, Alignment alignment) {
        return 0;
    }

    @UnsupportedAppUsage
    public boolean isLevelBoundary(int offset) {
        int line = getLineForOffset(offset);
        Directions dirs = getLineDirections(line);
        boolean z = false;
        if (dirs == DIRS_ALL_LEFT_TO_RIGHT || dirs == DIRS_ALL_RIGHT_TO_LEFT) {
            return false;
        }
        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        if (offset == lineStart || offset == lineEnd) {
            if (((runs[(offset == lineStart ? 0 : runs.length - 2) + 1] >>> 26) & 63) != (getParagraphDirection(line) == 1 ? 0 : 1)) {
                z = true;
            }
            return z;
        }
        offset -= lineStart;
        for (int i = 0; i < runs.length; i += 2) {
            if (offset == runs[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean isRtlCharAt(int offset) {
        int line = getLineForOffset(offset);
        Directions dirs = getLineDirections(line);
        boolean z = false;
        if (dirs == DIRS_ALL_LEFT_TO_RIGHT) {
            return false;
        }
        if (dirs == DIRS_ALL_RIGHT_TO_LEFT) {
            return true;
        }
        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        int i = 0;
        while (i < runs.length) {
            int start = runs[i] + lineStart;
            int limit = (runs[i + 1] & RUN_LENGTH_MASK) + start;
            if (offset < start || offset >= limit) {
                i += 2;
            } else {
                if ((((runs[i + 1] >>> 26) & 63) & 1) != 0) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    public long getRunRange(int offset) {
        int line = getLineForOffset(offset);
        Directions dirs = getLineDirections(line);
        if (dirs == DIRS_ALL_LEFT_TO_RIGHT || dirs == DIRS_ALL_RIGHT_TO_LEFT) {
            return TextUtils.packRangeInLong(0, getLineEnd(line));
        }
        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        for (int i = 0; i < runs.length; i += 2) {
            int start = runs[i] + lineStart;
            int limit = (runs[i + 1] & RUN_LENGTH_MASK) + start;
            if (offset >= start && offset < limit) {
                return TextUtils.packRangeInLong(start, limit);
            }
        }
        return TextUtils.packRangeInLong(0, getLineEnd(line));
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x007b  */
    @com.android.internal.annotations.VisibleForTesting
    public boolean primaryIsTrailingPrevious(int r13) {
        /*
        r12 = this;
        r0 = r12.getLineForOffset(r13);
        r1 = r12.getLineStart(r0);
        r2 = r12.getLineEnd(r0);
        r3 = r12.getLineDirections(r0);
        r3 = r3.mDirections;
        r4 = -1;
        r5 = 0;
    L_0x0014:
        r6 = r3.length;
        r7 = 67108863; // 0x3ffffff float:1.5046327E-36 double:3.31561837E-316;
        r8 = 0;
        if (r5 >= r6) goto L_0x003a;
    L_0x001b:
        r6 = r3[r5];
        r6 = r6 + r1;
        r9 = r5 + 1;
        r9 = r3[r9];
        r9 = r9 & r7;
        r9 = r9 + r6;
        if (r9 <= r2) goto L_0x0027;
    L_0x0026:
        r9 = r2;
    L_0x0027:
        if (r13 < r6) goto L_0x0037;
    L_0x0029:
        if (r13 >= r9) goto L_0x0037;
    L_0x002b:
        if (r13 <= r6) goto L_0x002e;
    L_0x002d:
        return r8;
    L_0x002e:
        r10 = r5 + 1;
        r10 = r3[r10];
        r10 = r10 >>> 26;
        r4 = r10 & 63;
        goto L_0x003a;
    L_0x0037:
        r5 = r5 + 2;
        goto L_0x0014;
    L_0x003a:
        r5 = -1;
        r6 = 1;
        if (r4 != r5) goto L_0x0048;
    L_0x003e:
        r5 = r12.getParagraphDirection(r0);
        if (r5 != r6) goto L_0x0046;
    L_0x0044:
        r5 = r8;
        goto L_0x0047;
    L_0x0046:
        r5 = r6;
    L_0x0047:
        r4 = r5;
    L_0x0048:
        r5 = -1;
        if (r13 != r1) goto L_0x0056;
    L_0x004b:
        r7 = r12.getParagraphDirection(r0);
        if (r7 != r6) goto L_0x0053;
    L_0x0051:
        r7 = r8;
        goto L_0x0054;
    L_0x0053:
        r7 = r6;
    L_0x0054:
        r5 = r7;
        goto L_0x0078;
    L_0x0056:
        r13 = r13 + -1;
        r9 = 0;
    L_0x0059:
        r10 = r3.length;
        if (r9 >= r10) goto L_0x0078;
    L_0x005c:
        r10 = r3[r9];
        r10 = r10 + r1;
        r11 = r9 + 1;
        r11 = r3[r11];
        r11 = r11 & r7;
        r11 = r11 + r10;
        if (r11 <= r2) goto L_0x0068;
    L_0x0067:
        r11 = r2;
    L_0x0068:
        if (r13 < r10) goto L_0x0075;
    L_0x006a:
        if (r13 >= r11) goto L_0x0075;
    L_0x006c:
        r7 = r9 + 1;
        r7 = r3[r7];
        r7 = r7 >>> 26;
        r5 = r7 & 63;
        goto L_0x0078;
    L_0x0075:
        r9 = r9 + 2;
        goto L_0x0059;
    L_0x0078:
        if (r5 >= r4) goto L_0x007b;
    L_0x007a:
        goto L_0x007c;
    L_0x007b:
        r6 = r8;
    L_0x007c:
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.Layout.primaryIsTrailingPrevious(int):boolean");
    }

    @VisibleForTesting
    public boolean[] primaryIsTrailingPreviousAllLineOffsets(int line) {
        int i;
        int start;
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int[] runs = getLineDirections(line).mDirections;
        boolean[] trailing = new boolean[((lineEnd - lineStart) + 1)];
        byte[] level = new byte[((lineEnd - lineStart) + 1)];
        for (i = 0; i < runs.length; i += 2) {
            start = runs[i] + lineStart;
            int limit = (runs[i + 1] & RUN_LENGTH_MASK) + start;
            if (limit > lineEnd) {
                limit = lineEnd;
            }
            if (limit != start) {
                level[(limit - lineStart) - 1] = (byte) ((runs[i + 1] >>> 26) & 63);
            }
        }
        for (i = 0; i < runs.length; i += 2) {
            start = runs[i] + lineStart;
            byte currentLevel = (byte) ((runs[i + 1] >>> 26) & 63);
            int i2 = start - lineStart;
            boolean z = false;
            byte b = start == lineStart ? getParagraphDirection(line) == 1 ? (byte) 0 : (byte) 1 : level[(start - lineStart) - 1];
            if (currentLevel > b) {
                z = true;
            }
            trailing[i2] = z;
        }
        return trailing;
    }

    public float getPrimaryHorizontal(int offset) {
        return getPrimaryHorizontal(offset, false);
    }

    @UnsupportedAppUsage
    public float getPrimaryHorizontal(int offset, boolean clamped) {
        return getHorizontal(offset, primaryIsTrailingPrevious(offset), clamped);
    }

    public float getSecondaryHorizontal(int offset) {
        return getSecondaryHorizontal(offset, false);
    }

    @UnsupportedAppUsage
    public float getSecondaryHorizontal(int offset, boolean clamped) {
        return getHorizontal(offset, primaryIsTrailingPrevious(offset) ^ 1, clamped);
    }

    private float getHorizontal(int offset, boolean primary) {
        return primary ? getPrimaryHorizontal(offset) : getSecondaryHorizontal(offset);
    }

    private float getHorizontal(int offset, boolean trailing, boolean clamped) {
        return getHorizontal(offset, trailing, getLineForOffset(offset), clamped);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0071  */
    private float getHorizontal(int r22, boolean r23, int r24, boolean r25) {
        /*
        r21 = this;
        r0 = r21;
        r1 = r24;
        r13 = r0.getLineStart(r1);
        r14 = r0.getLineEnd(r1);
        r15 = r0.getParagraphDirection(r1);
        r16 = r0.getLineContainsTab(r1);
        r17 = r0.getLineDirections(r1);
        r2 = 0;
        if (r16 == 0) goto L_0x0039;
    L_0x001b:
        r3 = r0.mText;
        r4 = r3 instanceof android.text.Spanned;
        if (r4 == 0) goto L_0x0039;
    L_0x0021:
        r3 = (android.text.Spanned) r3;
        r4 = android.text.style.TabStopSpan.class;
        r3 = getParagraphSpans(r3, r13, r14, r4);
        r3 = (android.text.style.TabStopSpan[]) r3;
        r4 = r3.length;
        if (r4 <= 0) goto L_0x0039;
    L_0x002e:
        r4 = new android.text.Layout$TabStops;
        r5 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4.<init>(r5, r3);
        r2 = r4;
        r18 = r2;
        goto L_0x003b;
    L_0x0039:
        r18 = r2;
    L_0x003b:
        r12 = android.text.TextLine.obtain();
        r3 = r0.mPaint;
        r4 = r0.mText;
        r11 = r0.getEllipsisStart(r1);
        r2 = r0.getEllipsisStart(r1);
        r5 = r0.getEllipsisCount(r1);
        r19 = r2 + r5;
        r2 = r12;
        r5 = r13;
        r6 = r14;
        r7 = r15;
        r8 = r17;
        r9 = r16;
        r10 = r18;
        r20 = r14;
        r14 = r12;
        r12 = r19;
        r2.set(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12);
        r2 = r22 - r13;
        r3 = 0;
        r4 = r23;
        r2 = r14.measure(r2, r4, r3);
        android.text.TextLine.recycle(r14);
        if (r25 == 0) goto L_0x0079;
    L_0x0071:
        r3 = r0.mWidth;
        r5 = (float) r3;
        r5 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1));
        if (r5 <= 0) goto L_0x0079;
    L_0x0078:
        r2 = (float) r3;
    L_0x0079:
        r3 = r0.getParagraphLeft(r1);
        r5 = r0.getParagraphRight(r1);
        r6 = r0.getLineStartPos(r1, r3, r5);
        r6 = (float) r6;
        r6 = r6 + r2;
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.Layout.getHorizontal(int, boolean, int, boolean):float");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00a8 A:{LOOP_END, LOOP:2: B:24:0x00a5->B:26:0x00a8} */
    private float[] getLineHorizontals(int r21, boolean r22, boolean r23) {
        /*
        r20 = this;
        r0 = r20;
        r12 = r20.getLineStart(r21);
        r13 = r20.getLineEnd(r21);
        r14 = r20.getParagraphDirection(r21);
        r15 = r20.getLineContainsTab(r21);
        r16 = r20.getLineDirections(r21);
        r1 = 0;
        if (r15 == 0) goto L_0x0037;
    L_0x0019:
        r2 = r0.mText;
        r3 = r2 instanceof android.text.Spanned;
        if (r3 == 0) goto L_0x0037;
    L_0x001f:
        r2 = (android.text.Spanned) r2;
        r3 = android.text.style.TabStopSpan.class;
        r2 = getParagraphSpans(r2, r12, r13, r3);
        r2 = (android.text.style.TabStopSpan[]) r2;
        r3 = r2.length;
        if (r3 <= 0) goto L_0x0037;
    L_0x002c:
        r3 = new android.text.Layout$TabStops;
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r3.<init>(r4, r2);
        r1 = r3;
        r17 = r1;
        goto L_0x0039;
    L_0x0037:
        r17 = r1;
    L_0x0039:
        r11 = android.text.TextLine.obtain();
        r2 = r0.mPaint;
        r3 = r0.mText;
        r10 = r20.getEllipsisStart(r21);
        r1 = r20.getEllipsisStart(r21);
        r4 = r20.getEllipsisCount(r21);
        r18 = r1 + r4;
        r1 = r11;
        r4 = r12;
        r5 = r13;
        r6 = r14;
        r7 = r16;
        r8 = r15;
        r9 = r17;
        r19 = r14;
        r14 = r11;
        r11 = r18;
        r1.set(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
        r1 = r20.primaryIsTrailingPreviousAllLineOffsets(r21);
        if (r23 != 0) goto L_0x0073;
    L_0x0066:
        r2 = 0;
    L_0x0067:
        r3 = r1.length;
        if (r2 >= r3) goto L_0x0073;
    L_0x006a:
        r3 = r1[r2];
        r3 = r3 ^ 1;
        r1[r2] = r3;
        r2 = r2 + 1;
        goto L_0x0067;
    L_0x0073:
        r2 = 0;
        r2 = r14.measureAllOffsets(r1, r2);
        android.text.TextLine.recycle(r14);
        if (r22 == 0) goto L_0x0090;
    L_0x007d:
        r3 = 0;
    L_0x007e:
        r4 = r2.length;
        if (r3 >= r4) goto L_0x0090;
    L_0x0081:
        r4 = r2[r3];
        r5 = r0.mWidth;
        r6 = (float) r5;
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 <= 0) goto L_0x008d;
    L_0x008a:
        r4 = (float) r5;
        r2[r3] = r4;
    L_0x008d:
        r3 = r3 + 1;
        goto L_0x007e;
    L_0x0090:
        r3 = r20.getParagraphLeft(r21);
        r4 = r20.getParagraphRight(r21);
        r5 = r21;
        r6 = r0.getLineStartPos(r5, r3, r4);
        r7 = r13 - r12;
        r7 = r7 + 1;
        r7 = new float[r7];
        r8 = 0;
    L_0x00a5:
        r9 = r7.length;
        if (r8 >= r9) goto L_0x00b1;
    L_0x00a8:
        r9 = (float) r6;
        r10 = r2[r8];
        r9 = r9 + r10;
        r7[r8] = r9;
        r8 = r8 + 1;
        goto L_0x00a5;
    L_0x00b1:
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.Layout.getLineHorizontals(int, boolean, boolean):float[]");
    }

    public float getLineLeft(int line) {
        int dir = getParagraphDirection(line);
        Alignment align = getParagraphAlignment(line);
        if (align == null) {
            align = Alignment.ALIGN_CENTER;
        }
        int i = AnonymousClass1.$SwitchMap$android$text$Layout$Alignment[align.ordinal()];
        Alignment resultAlign = i != 1 ? i != 2 ? i != 3 ? i != 4 ? Alignment.ALIGN_LEFT : Alignment.ALIGN_RIGHT : Alignment.ALIGN_CENTER : dir == -1 ? Alignment.ALIGN_LEFT : Alignment.ALIGN_RIGHT : dir == -1 ? Alignment.ALIGN_RIGHT : Alignment.ALIGN_LEFT;
        int i2 = AnonymousClass1.$SwitchMap$android$text$Layout$Alignment[resultAlign.ordinal()];
        if (i2 == 3) {
            return (float) Math.floor((double) (((float) getParagraphLeft(line)) + ((((float) this.mWidth) - getLineMax(line)) / 2.0f)));
        } else if (i2 != 4) {
            return 0.0f;
        } else {
            return ((float) this.mWidth) - getLineMax(line);
        }
    }

    public float getLineRight(int line) {
        int dir = getParagraphDirection(line);
        Alignment align = getParagraphAlignment(line);
        if (align == null) {
            align = Alignment.ALIGN_CENTER;
        }
        int i = AnonymousClass1.$SwitchMap$android$text$Layout$Alignment[align.ordinal()];
        Alignment resultAlign = i != 1 ? i != 2 ? i != 3 ? i != 4 ? Alignment.ALIGN_LEFT : Alignment.ALIGN_RIGHT : Alignment.ALIGN_CENTER : dir == -1 ? Alignment.ALIGN_LEFT : Alignment.ALIGN_RIGHT : dir == -1 ? Alignment.ALIGN_RIGHT : Alignment.ALIGN_LEFT;
        int i2 = AnonymousClass1.$SwitchMap$android$text$Layout$Alignment[resultAlign.ordinal()];
        if (i2 == 3) {
            return (float) Math.ceil((double) (((float) getParagraphRight(line)) - ((((float) this.mWidth) - getLineMax(line)) / 2.0f)));
        } else if (i2 != 4) {
            return getLineMax(line);
        } else {
            return (float) this.mWidth;
        }
    }

    public float getLineMax(int line) {
        float margin = (float) getParagraphLeadingMargin(line);
        float signedExtent = getLineExtent(line, 0.0f);
        return (signedExtent >= 0.0f ? signedExtent : -signedExtent) + margin;
    }

    public float getLineWidth(int line) {
        float margin = (float) getParagraphLeadingMargin(line);
        float signedExtent = getLineExtent(line, Float.MIN_VALUE);
        return (signedExtent >= 0.0f ? signedExtent : -signedExtent) + margin;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x003d  */
    private float getLineExtent(int r22, boolean r23) {
        /*
        r21 = this;
        r0 = r21;
        r12 = r21.getLineStart(r22);
        if (r23 == 0) goto L_0x000d;
    L_0x0008:
        r1 = r21.getLineEnd(r22);
        goto L_0x0011;
    L_0x000d:
        r1 = r21.getLineVisibleEnd(r22);
    L_0x0011:
        r13 = r1;
        r14 = r21.getLineContainsTab(r22);
        r1 = 0;
        if (r14 == 0) goto L_0x0036;
    L_0x0019:
        r2 = r0.mText;
        r3 = r2 instanceof android.text.Spanned;
        if (r3 == 0) goto L_0x0036;
    L_0x001f:
        r2 = (android.text.Spanned) r2;
        r3 = android.text.style.TabStopSpan.class;
        r2 = getParagraphSpans(r2, r12, r13, r3);
        r2 = (android.text.style.TabStopSpan[]) r2;
        r3 = r2.length;
        if (r3 <= 0) goto L_0x0036;
    L_0x002c:
        r3 = new android.text.Layout$TabStops;
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r3.<init>(r4, r2);
        r1 = r3;
        r15 = r1;
        goto L_0x0037;
    L_0x0036:
        r15 = r1;
    L_0x0037:
        r16 = r21.getLineDirections(r22);
        if (r16 != 0) goto L_0x003f;
    L_0x003d:
        r1 = 0;
        return r1;
    L_0x003f:
        r17 = r21.getParagraphDirection(r22);
        r11 = android.text.TextLine.obtain();
        r10 = r0.mWorkPaint;
        r1 = r0.mPaint;
        r10.set(r1);
        r1 = r21.getStartHyphenEdit(r22);
        r10.setStartHyphenEdit(r1);
        r1 = r21.getEndHyphenEdit(r22);
        r10.setEndHyphenEdit(r1);
        r3 = r0.mText;
        r18 = r21.getEllipsisStart(r22);
        r1 = r21.getEllipsisStart(r22);
        r2 = r21.getEllipsisCount(r22);
        r19 = r1 + r2;
        r1 = r11;
        r2 = r10;
        r4 = r12;
        r5 = r13;
        r6 = r17;
        r7 = r16;
        r8 = r14;
        r9 = r15;
        r20 = r10;
        r10 = r18;
        r0 = r11;
        r11 = r19;
        r1.set(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
        r1 = r21.isJustificationRequired(r22);
        if (r1 == 0) goto L_0x008d;
    L_0x0086:
        r1 = r21.getJustifyWidth(r22);
        r0.justify(r1);
    L_0x008d:
        r1 = 0;
        r1 = r0.metrics(r1);
        android.text.TextLine.recycle(r0);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.Layout.getLineExtent(int, boolean):float");
    }

    private float getLineExtent(int line, TabStops tabStops, boolean full) {
        int start = getLineStart(line);
        int end = full ? getLineEnd(line) : getLineVisibleEnd(line);
        boolean hasTabs = getLineContainsTab(line);
        Directions directions = getLineDirections(line);
        int dir = getParagraphDirection(line);
        TextLine tl = TextLine.obtain();
        TextPaint paint = this.mWorkPaint;
        paint.set(this.mPaint);
        paint.setStartHyphenEdit(getStartHyphenEdit(line));
        paint.setEndHyphenEdit(getEndHyphenEdit(line));
        TextLine tl2 = tl;
        tl.set(paint, this.mText, start, end, dir, directions, hasTabs, tabStops, getEllipsisStart(line), getEllipsisStart(line) + getEllipsisCount(line));
        if (isJustificationRequired(line)) {
            tl2.justify(getJustifyWidth(line));
        }
        float width = tl2.metrics(0.0f);
        TextLine.recycle(tl2);
        return width;
    }

    public int getLineForVertical(int vertical) {
        int high = getLineCount();
        int low = -1;
        while (high - low > 1) {
            int guess = (high + low) / 2;
            if (getLineTop(guess) > vertical) {
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

    public int getLineForOffset(int offset) {
        int high = getLineCount();
        int low = -1;
        while (high - low > 1) {
            int guess = (high + low) / 2;
            if (getLineStart(guess) > offset) {
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

    public int getOffsetForHorizontal(int line, float horiz) {
        return getOffsetForHorizontal(line, horiz, true);
    }

    public int getOffsetForHorizontal(int line, float horiz, boolean primary) {
        int max;
        Layout swap = this;
        int i = line;
        int lineEndOffset = getLineEnd(line);
        int lineStartOffset = getLineStart(line);
        Directions dirs = getLineDirections(line);
        TextLine tl = TextLine.obtain();
        Directions directions = dirs;
        Directions dirs2 = dirs;
        TextLine tl2 = tl;
        tl.set(swap.mPaint, swap.mText, lineStartOffset, lineEndOffset, getParagraphDirection(line), directions, false, null, getEllipsisStart(line), getEllipsisStart(line) + getEllipsisCount(line));
        HorizontalMeasurementProvider horizontal = new HorizontalMeasurementProvider(i, primary);
        boolean z = true;
        if (i == getLineCount() - 1) {
            max = lineEndOffset;
        } else {
            max = tl2.getOffsetToLeftRightOf(lineEndOffset - lineStartOffset, swap.isRtlCharAt(lineEndOffset - 1) ^ 1) + lineStartOffset;
        }
        int best = lineStartOffset;
        float bestdist = Math.abs(horizontal.get(lineStartOffset) - horiz);
        int i2 = 0;
        while (true) {
            Directions dirs3 = dirs2;
            if (i2 >= dirs3.mDirections.length) {
                break;
            }
            int adguess;
            int swap2;
            boolean z2;
            int here = dirs3.mDirections[i2] + lineStartOffset;
            int there = (dirs3.mDirections[i2 + 1] & RUN_LENGTH_MASK) + here;
            boolean isRtl = (dirs3.mDirections[i2 + 1] & 67108864) != 0 ? z : false;
            int swap3 = isRtl ? true : z;
            if (there > max) {
                there = max;
            }
            int high = (there - 1) + 1;
            int low = (here + 1) - 1;
            while (high - low > 1) {
                i = (high + low) / 2;
                adguess = swap.getOffsetAtStartOf(i);
                swap2 = swap3;
                swap3 = adguess;
                if (horizontal.get(adguess) * ((float) swap2) >= ((float) swap2) * horiz) {
                    high = i;
                } else {
                    low = i;
                }
                i = line;
                z2 = primary;
                swap3 = swap2;
                swap = this;
            }
            swap2 = swap3;
            if (low < here + 1) {
                low = here + 1;
            }
            if (low < there) {
                i = tl2.getOffsetToLeftRightOf(low - lineStartOffset, isRtl) + lineStartOffset;
                swap3 = swap2;
                low = tl2.getOffsetToLeftRightOf(i - lineStartOffset, !isRtl ? 1 : 0) + lineStartOffset;
                if (low >= here && low < there) {
                    swap2 = Math.abs(horizontal.get(low) - horiz);
                    if (i < there) {
                        adguess = Math.abs(horizontal.get(i) - horiz);
                        if (adguess < swap2) {
                            swap2 = adguess;
                            low = i;
                        }
                    }
                    if (swap2 < bestdist) {
                        bestdist = swap2;
                        best = low;
                    }
                }
            }
            float dist = Math.abs(horizontal.get(here) - horiz);
            if (dist < bestdist) {
                bestdist = dist;
                best = here;
            }
            i2 += 2;
            swap = this;
            i = line;
            z2 = primary;
            dirs2 = dirs3;
            z = true;
        }
        if (Math.abs(horizontal.get(max) - horiz) <= bestdist) {
            best = max;
        }
        TextLine.recycle(tl2);
        return best;
    }

    public final int getLineEnd(int line) {
        return getLineStart(line + 1);
    }

    public int getLineVisibleEnd(int line) {
        return getLineVisibleEnd(line, getLineStart(line), getLineStart(line + 1));
    }

    private int getLineVisibleEnd(int line, int start, int end) {
        CharSequence text = this.mText;
        if (line == getLineCount() - 1) {
            return end;
        }
        while (end > start) {
            char ch = text.charAt(end - 1);
            if (ch == 10) {
                return end - 1;
            }
            if (!TextLine.isLineEndSpace(ch)) {
                break;
            }
            end--;
        }
        return end;
    }

    public final int getLineBottom(int line) {
        return getLineTop(line + 1);
    }

    public final int getLineBottomWithoutSpacing(int line) {
        return getLineTop(line + 1) - getLineExtra(line);
    }

    public final int getLineBaseline(int line) {
        return getLineTop(line + 1) - getLineDescent(line);
    }

    public final int getLineAscent(int line) {
        return getLineTop(line) - (getLineTop(line + 1) - getLineDescent(line));
    }

    public int getLineExtra(int line) {
        return 0;
    }

    public int getOffsetToLeftOf(int offset) {
        return getOffsetToLeftRightOf(offset, true);
    }

    public int getOffsetToRightOf(int offset) {
        return getOffsetToLeftRightOf(offset, false);
    }

    private int getOffsetToLeftRightOf(int caret, boolean toLeft) {
        int i = caret;
        boolean toLeft2 = toLeft;
        int line = getLineForOffset(caret);
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int lineDir = getParagraphDirection(line);
        boolean lineChanged = false;
        boolean advance = false;
        if (toLeft2 == (lineDir == -1)) {
            advance = true;
        }
        if (advance) {
            if (i == lineEnd) {
                if (line >= getLineCount() - 1) {
                    return i;
                }
                lineChanged = true;
                line++;
            }
        } else if (i == lineStart) {
            if (line <= 0) {
                return i;
            }
            lineChanged = true;
            line--;
        }
        if (lineChanged) {
            lineStart = getLineStart(line);
            lineEnd = getLineEnd(line);
            int newDir = getParagraphDirection(line);
            if (newDir != lineDir) {
                toLeft2 ^= 1;
                lineDir = newDir;
            }
        }
        Directions directions = getLineDirections(line);
        TextLine tl = TextLine.obtain();
        TextPaint textPaint = this.mPaint;
        CharSequence charSequence = this.mText;
        int ellipsisStart = getEllipsisStart(line);
        int ellipsisStart2 = getEllipsisStart(line) + getEllipsisCount(line);
        TextLine tl2 = tl;
        tl.set(textPaint, charSequence, lineStart, lineEnd, lineDir, directions, false, null, ellipsisStart, ellipsisStart2);
        int caret2 = tl2.getOffsetToLeftRightOf(i - lineStart, toLeft2) + lineStart;
        TextLine.recycle(tl2);
        return caret2;
    }

    private int getOffsetAtStartOf(int offset) {
        if (offset == 0) {
            return 0;
        }
        CharSequence text = this.mText;
        char c = text.charAt(offset);
        if (c >= 56320 && c <= 57343) {
            char c1 = text.charAt(offset - 1);
            if (c1 >= 55296 && c1 <= 56319) {
                offset--;
            }
        }
        if (this.mSpannedText) {
            ReplacementSpan[] spans = (ReplacementSpan[]) ((Spanned) text).getSpans(offset, offset, ReplacementSpan.class);
            for (int i = 0; i < spans.length; i++) {
                int start = ((Spanned) text).getSpanStart(spans[i]);
                int end = ((Spanned) text).getSpanEnd(spans[i]);
                if (start < offset && end > offset) {
                    offset = start;
                }
            }
        }
        return offset;
    }

    @UnsupportedAppUsage
    public boolean shouldClampCursor(int line) {
        int i = AnonymousClass1.$SwitchMap$android$text$Layout$Alignment[getParagraphAlignment(line).ordinal()];
        boolean z = false;
        if (i != 1) {
            return i == 5;
        } else {
            if (getParagraphDirection(line) > 0) {
                z = true;
            }
            return z;
        }
    }

    public void getCursorPath(int point, Path dest, CharSequence editingBuffer) {
        Path path = dest;
        CharSequence charSequence = editingBuffer;
        dest.reset();
        int line = getLineForOffset(point);
        int top = getLineTop(line);
        int bottom = getLineBottomWithoutSpacing(line);
        float h1 = getPrimaryHorizontal(point, shouldClampCursor(line)) - 0.5f;
        int caps = MetaKeyKeyListener.getMetaState(charSequence, 1) | MetaKeyKeyListener.getMetaState(charSequence, 2048);
        int fn = MetaKeyKeyListener.getMetaState(charSequence, 2);
        int dist = 0;
        if (!(caps == 0 && fn == 0)) {
            dist = (bottom - top) >> 2;
            if (fn != 0) {
                top += dist;
            }
            if (caps != 0) {
                bottom -= dist;
            }
        }
        if (h1 < 0.5f) {
            h1 = 0.5f;
        }
        path.moveTo(h1, (float) top);
        path.lineTo(h1, (float) bottom);
        if (caps == 2) {
            path.moveTo(h1, (float) bottom);
            path.lineTo(h1 - ((float) dist), (float) (bottom + dist));
            path.lineTo(h1, (float) bottom);
            path.lineTo(((float) dist) + h1, (float) (bottom + dist));
        } else if (caps == 1) {
            path.moveTo(h1, (float) bottom);
            path.lineTo(h1 - ((float) dist), (float) (bottom + dist));
            path.moveTo(h1 - ((float) dist), ((float) (bottom + dist)) - 0.5f);
            path.lineTo(((float) dist) + h1, ((float) (bottom + dist)) - 0.5f);
            path.moveTo(((float) dist) + h1, (float) (bottom + dist));
            path.lineTo(h1, (float) bottom);
        }
        if (fn == 2) {
            path.moveTo(h1, (float) top);
            path.lineTo(h1 - ((float) dist), (float) (top - dist));
            path.lineTo(h1, (float) top);
            path.lineTo(((float) dist) + h1, (float) (top - dist));
        } else if (fn == 1) {
            path.moveTo(h1, (float) top);
            path.lineTo(h1 - ((float) dist), (float) (top - dist));
            path.moveTo(h1 - ((float) dist), ((float) (top - dist)) + 0.5f);
            path.lineTo(((float) dist) + h1, ((float) (top - dist)) + 0.5f);
            path.moveTo(((float) dist) + h1, (float) (top - dist));
            path.lineTo(h1, (float) top);
        }
    }

    private void addSelection(int line, int start, int end, int top, int bottom, SelectionRectangleConsumer consumer) {
        int i;
        int i2;
        Layout layout = this;
        int i3 = line;
        int i4 = start;
        int i5 = end;
        int linestart = getLineStart(line);
        int lineend = getLineEnd(line);
        Directions dirs = getLineDirections(line);
        if (lineend > linestart && layout.mText.charAt(lineend - 1) == 10) {
            lineend--;
        }
        int i6 = 0;
        while (i6 < dirs.mDirections.length) {
            int here = dirs.mDirections[i6] + linestart;
            int there = (dirs.mDirections[i6 + 1] & RUN_LENGTH_MASK) + here;
            if (there > lineend) {
                there = lineend;
            }
            if (i4 > there || i5 < here) {
                i = top;
                i2 = bottom;
            } else {
                int st = Math.max(i4, here);
                int en = Math.min(i5, there);
                if (st != en) {
                    int layout2;
                    float h1 = layout.getHorizontal(st, false, i3, false);
                    float h2 = layout.getHorizontal(en, true, i3, false);
                    float left = Math.min(h1, h2);
                    float right = Math.max(h1, h2);
                    if ((dirs.mDirections[i6 + 1] & 67108864) != 0) {
                        layout2 = 0;
                    } else {
                        layout2 = 1;
                    }
                    consumer.accept(left, (float) top, right, (float) bottom, layout2);
                } else {
                    i = top;
                    i2 = bottom;
                }
            }
            i6 += 2;
            layout = this;
            i3 = line;
        }
        i = top;
        i2 = bottom;
    }

    public void getSelectionPath(int start, int end, Path dest) {
        dest.reset();
        getSelection(start, end, new -$$Lambda$Layout$MzjK2UE2G8VG0asK8_KWY3gHAmY(dest));
    }

    public final void getSelection(int start, int end, SelectionRectangleConsumer consumer) {
        int start2 = start;
        int i = end;
        if (start2 != i) {
            int start3;
            int end2;
            if (i < start2) {
                start3 = end;
                end2 = start;
            } else {
                start3 = start2;
                end2 = i;
            }
            int startline = getLineForOffset(start3);
            int endline = getLineForOffset(end2);
            int top = getLineTop(startline);
            int bottom = getLineBottomWithoutSpacing(endline);
            if (startline == endline) {
                addSelection(startline, start3, end2, top, bottom, consumer);
            } else {
                float width = (float) this.mWidth;
                addSelection(startline, start3, getLineEnd(startline), top, getLineBottom(startline), consumer);
                if (getParagraphDirection(startline) == -1) {
                    consumer.accept(getLineLeft(startline), (float) top, 0.0f, (float) getLineBottom(startline), 0);
                } else {
                    consumer.accept(getLineRight(startline), (float) top, width, (float) getLineBottom(startline), 1);
                }
                for (start2 = startline + 1; start2 < endline; start2++) {
                    top = getLineTop(start2);
                    bottom = getLineBottom(start2);
                    if (getParagraphDirection(start2) == -1) {
                        consumer.accept(0.0f, (float) top, width, (float) bottom, 0);
                    } else {
                        consumer.accept(0.0f, (float) top, width, (float) bottom, 1);
                    }
                }
                top = getLineTop(endline);
                bottom = getLineBottomWithoutSpacing(endline);
                addSelection(endline, getLineStart(endline), end2, top, bottom, consumer);
                if (getParagraphDirection(endline) == -1) {
                    consumer.accept(width, (float) top, getLineRight(endline), (float) bottom, 0);
                } else {
                    consumer.accept(0.0f, (float) top, getLineLeft(endline), (float) bottom, 1);
                }
            }
        }
    }

    public final Alignment getParagraphAlignment(int line) {
        Alignment align = this.mAlignment;
        if (!this.mSpannedText) {
            return align;
        }
        AlignmentSpan[] spans = (AlignmentSpan[]) getParagraphSpans(this.mText, getLineStart(line), getLineEnd(line), AlignmentSpan.class);
        int spanLength = spans.length;
        if (spanLength > 0) {
            return spans[spanLength - 1].getAlignment();
        }
        return align;
    }

    public final int getParagraphLeft(int line) {
        if (getParagraphDirection(line) == -1 || !this.mSpannedText) {
            return 0;
        }
        return getParagraphLeadingMargin(line);
    }

    public final int getParagraphRight(int line) {
        int right = this.mWidth;
        if (getParagraphDirection(line) == 1 || !this.mSpannedText) {
            return right;
        }
        return right - getParagraphLeadingMargin(line);
    }

    private int getParagraphLeadingMargin(int line) {
        if (!this.mSpannedText) {
            return 0;
        }
        Spanned spanned = this.mText;
        int lineStart = getLineStart(line);
        LeadingMarginSpan[] spans = (LeadingMarginSpan[]) getParagraphSpans(spanned, lineStart, spanned.nextSpanTransition(lineStart, getLineEnd(line), LeadingMarginSpan.class), LeadingMarginSpan.class);
        if (spans.length == 0) {
            return 0;
        }
        int margin = 0;
        boolean useFirstLineMargin = lineStart == 0 || spanned.charAt(lineStart - 1) == 10;
        for (int i = 0; i < spans.length; i++) {
            if (spans[i] instanceof LeadingMarginSpan2) {
                useFirstLineMargin |= line < getLineForOffset(spanned.getSpanStart(spans[i])) + ((LeadingMarginSpan2) spans[i]).getLeadingMarginLineCount() ? 1 : 0;
            }
        }
        for (LeadingMarginSpan span : spans) {
            margin += span.getLeadingMargin(useFirstLineMargin);
        }
        return margin;
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x00e6  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00e6  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00e6  */
    private static float measurePara(android.text.TextPaint r23, java.lang.CharSequence r24, int r25, int r26, android.text.TextDirectionHeuristic r27) {
        /*
        r12 = r24;
        r13 = r25;
        r14 = r26;
        r1 = 0;
        r15 = android.text.TextLine.obtain();
        r11 = r27;
        r0 = android.text.MeasuredParagraph.buildForBidi(r12, r13, r14, r11, r1);	 Catch:{ all -> 0x00df }
        r10 = r0;
        r0 = r10.getChars();	 Catch:{ all -> 0x00db }
        r1 = r0.length;	 Catch:{ all -> 0x00db }
        r9 = r1;
        r1 = 0;
        r7 = r10.getDirections(r1, r9);	 Catch:{ all -> 0x00db }
        r6 = r10.getParagraphDir();	 Catch:{ all -> 0x00db }
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = r12 instanceof android.text.Spanned;	 Catch:{ all -> 0x00db }
        if (r5 == 0) goto L_0x0059;
    L_0x0028:
        r5 = r12;
        r5 = (android.text.Spanned) r5;	 Catch:{ all -> 0x0056 }
        r8 = android.text.style.LeadingMarginSpan.class;
        r8 = getParagraphSpans(r5, r13, r14, r8);	 Catch:{ all -> 0x0056 }
        r8 = (android.text.style.LeadingMarginSpan[]) r8;	 Catch:{ all -> 0x0056 }
        r1 = r8.length;	 Catch:{ all -> 0x0056 }
        r16 = r4;
        r4 = 0;
    L_0x0037:
        if (r4 >= r1) goto L_0x0051;
    L_0x0039:
        r17 = r8[r4];	 Catch:{ all -> 0x0056 }
        r18 = r17;
        r17 = r1;
        r1 = 1;
        r19 = r2;
        r2 = r18;
        r1 = r2.getLeadingMargin(r1);	 Catch:{ all -> 0x0056 }
        r16 = r16 + r1;
        r4 = r4 + 1;
        r1 = r17;
        r2 = r19;
        goto L_0x0037;
    L_0x0051:
        r19 = r2;
        r8 = r16;
        goto L_0x005c;
    L_0x0056:
        r0 = move-exception;
        goto L_0x00e1;
    L_0x0059:
        r19 = r2;
        r8 = r4;
    L_0x005c:
        r1 = 0;
    L_0x005d:
        if (r1 >= r9) goto L_0x00a0;
    L_0x005f:
        r2 = r0[r1];	 Catch:{ all -> 0x0056 }
        r4 = 9;
        if (r2 != r4) goto L_0x0099;
    L_0x0065:
        r2 = 1;
        r4 = r12 instanceof android.text.Spanned;	 Catch:{ all -> 0x0056 }
        if (r4 == 0) goto L_0x0092;
    L_0x006a:
        r4 = r12;
        r4 = (android.text.Spanned) r4;	 Catch:{ all -> 0x0056 }
        r5 = android.text.style.TabStopSpan.class;
        r5 = r4.nextSpanTransition(r13, r14, r5);	 Catch:{ all -> 0x0056 }
        r16 = r0;
        r0 = android.text.style.TabStopSpan.class;
        r0 = getParagraphSpans(r4, r13, r5, r0);	 Catch:{ all -> 0x0056 }
        r0 = (android.text.style.TabStopSpan[]) r0;	 Catch:{ all -> 0x0056 }
        r17 = r2;
        r2 = r0.length;	 Catch:{ all -> 0x0056 }
        if (r2 <= 0) goto L_0x008f;
    L_0x0082:
        r2 = new android.text.Layout$TabStops;	 Catch:{ all -> 0x0056 }
        r18 = r3;
        r3 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r2.<init>(r3, r0);	 Catch:{ all -> 0x0056 }
        r3 = r2;
        r18 = r3;
        goto L_0x0091;
    L_0x008f:
        r18 = r3;
    L_0x0091:
        goto L_0x00a6;
    L_0x0092:
        r16 = r0;
        r17 = r2;
        r18 = r3;
        goto L_0x00a6;
    L_0x0099:
        r16 = r0;
        r18 = r3;
        r1 = r1 + 1;
        goto L_0x005d;
    L_0x00a0:
        r16 = r0;
        r18 = r3;
        r17 = r19;
    L_0x00a6:
        r0 = 0;
        r19 = 0;
        r1 = r15;
        r2 = r23;
        r3 = r24;
        r4 = r25;
        r5 = r26;
        r20 = r8;
        r8 = r17;
        r21 = r9;
        r9 = r18;
        r22 = r10;
        r10 = r0;
        r11 = r19;
        r1.set(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);	 Catch:{ all -> 0x00d7 }
        r4 = r20;
        r0 = (float) r4;	 Catch:{ all -> 0x00d7 }
        r1 = 0;
        r1 = r15.metrics(r1);	 Catch:{ all -> 0x00d7 }
        r1 = java.lang.Math.abs(r1);	 Catch:{ all -> 0x00d7 }
        r0 = r0 + r1;
        android.text.TextLine.recycle(r15);
        r22.recycle();
        return r0;
    L_0x00d7:
        r0 = move-exception;
        r10 = r22;
        goto L_0x00e1;
    L_0x00db:
        r0 = move-exception;
        r22 = r10;
        goto L_0x00e1;
    L_0x00df:
        r0 = move-exception;
        r10 = r1;
    L_0x00e1:
        android.text.TextLine.recycle(r15);
        if (r10 == 0) goto L_0x00e9;
    L_0x00e6:
        r10.recycle();
    L_0x00e9:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.Layout.measurePara(android.text.TextPaint, java.lang.CharSequence, int, int, android.text.TextDirectionHeuristic):float");
    }

    static float nextTab(CharSequence text, int start, int end, float h, Object[] tabs) {
        float nh = Float.MAX_VALUE;
        boolean alltabs = false;
        if (text instanceof Spanned) {
            if (tabs == null) {
                tabs = getParagraphSpans((Spanned) text, start, end, TabStopSpan.class);
                alltabs = true;
            }
            int i = 0;
            while (i < tabs.length) {
                if (alltabs || (tabs[i] instanceof TabStopSpan)) {
                    int where = ((TabStopSpan) tabs[i]).getTabStop();
                    if (((float) where) < nh && ((float) where) > h) {
                        nh = (float) where;
                    }
                }
                i++;
            }
            if (nh != Float.MAX_VALUE) {
                return nh;
            }
        }
        return ((float) ((int) ((h + TAB_INCREMENT) / TAB_INCREMENT))) * TAB_INCREMENT;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isSpanned() {
        return this.mSpannedText;
    }

    static <T> T[] getParagraphSpans(Spanned text, int start, int end, Class<T> type) {
        if (start == end && start > 0) {
            return ArrayUtils.emptyArray(type);
        }
        if (text instanceof SpannableStringBuilder) {
            return ((SpannableStringBuilder) text).getSpans(start, end, type, false);
        }
        return text.getSpans(start, end, type);
    }

    private void ellipsize(int start, int end, int line, char[] dest, int destoff, TruncateAt method) {
        int i = start;
        int i2 = line;
        int ellipsisCount = getEllipsisCount(i2);
        if (ellipsisCount != 0) {
            int i3;
            int ellipsisStart = getEllipsisStart(i2);
            int lineStart = getLineStart(i2);
            String ellipsisString = TextUtils.getEllipsisString(method);
            int ellipsisStringLen = ellipsisString.length();
            boolean useEllipsisString = ellipsisCount >= ellipsisStringLen;
            int i4 = 0;
            while (i4 < ellipsisCount) {
                char c;
                if (!useEllipsisString || i4 >= ellipsisStringLen) {
                    c = 65279;
                } else {
                    c = ellipsisString.charAt(i4);
                }
                int a = (i4 + ellipsisStart) + lineStart;
                if (i > a) {
                    i3 = end;
                } else if (a < end) {
                    dest[(destoff + a) - i] = c;
                }
                i4++;
            }
            i3 = end;
        }
    }
}
