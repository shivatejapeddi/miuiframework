package android.text;

import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.text.MeasuredText;
import android.graphics.text.MeasuredText.Builder;
import android.text.AutoGrowArray.ByteArray;
import android.text.AutoGrowArray.FloatArray;
import android.text.AutoGrowArray.IntArray;
import android.text.Layout.Directions;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;
import android.util.Pools.SynchronizedPool;
import java.util.Arrays;

public class MeasuredParagraph {
    private static final char OBJECT_REPLACEMENT_CHARACTER = '￼';
    private static final SynchronizedPool<MeasuredParagraph> sPool = new SynchronizedPool(1);
    private FontMetricsInt mCachedFm;
    private TextPaint mCachedPaint = new TextPaint();
    private char[] mCopiedBuffer;
    private IntArray mFontMetrics = new IntArray(16);
    private ByteArray mLevels = new ByteArray();
    private boolean mLtrWithoutBidi;
    private MeasuredText mMeasuredText;
    private int mParaDir;
    private IntArray mSpanEndCache = new IntArray(4);
    private Spanned mSpanned;
    private int mTextLength;
    private int mTextStart;
    private float mWholeWidth;
    private FloatArray mWidths = new FloatArray();

    private MeasuredParagraph() {
    }

    private static MeasuredParagraph obtain() {
        MeasuredParagraph mt = (MeasuredParagraph) sPool.acquire();
        return mt != null ? mt : new MeasuredParagraph();
    }

    public void recycle() {
        release();
        sPool.release(this);
    }

    public void release() {
        reset();
        this.mLevels.clearWithReleasingLargeArray();
        this.mWidths.clearWithReleasingLargeArray();
        this.mFontMetrics.clearWithReleasingLargeArray();
        this.mSpanEndCache.clearWithReleasingLargeArray();
    }

    private void reset() {
        this.mSpanned = null;
        this.mCopiedBuffer = null;
        this.mWholeWidth = 0.0f;
        this.mLevels.clear();
        this.mWidths.clear();
        this.mFontMetrics.clear();
        this.mSpanEndCache.clear();
        this.mMeasuredText = null;
    }

    public int getTextLength() {
        return this.mTextLength;
    }

    public char[] getChars() {
        return this.mCopiedBuffer;
    }

    public int getParagraphDir() {
        return this.mParaDir;
    }

    public Directions getDirections(int start, int end) {
        if (this.mLtrWithoutBidi) {
            return Layout.DIRS_ALL_LEFT_TO_RIGHT;
        }
        return AndroidBidi.directions(this.mParaDir, this.mLevels.getRawArray(), start, this.mCopiedBuffer, start, end - start);
    }

    public float getWholeWidth() {
        return this.mWholeWidth;
    }

    public FloatArray getWidths() {
        return this.mWidths;
    }

    public IntArray getSpanEndCache() {
        return this.mSpanEndCache;
    }

    public IntArray getFontMetrics() {
        return this.mFontMetrics;
    }

    public MeasuredText getMeasuredText() {
        return this.mMeasuredText;
    }

    public float getWidth(int start, int end) {
        float[] widths = this.mMeasuredText;
        if (widths != null) {
            return widths.getWidth(start, end);
        }
        widths = this.mWidths.getRawArray();
        float r = 0.0f;
        for (int i = start; i < end; i++) {
            r += widths[i];
        }
        return r;
    }

    public void getBounds(int start, int end, Rect bounds) {
        this.mMeasuredText.getBounds(start, end, bounds);
    }

    public float getCharWidthAt(int offset) {
        return this.mMeasuredText.getCharWidthAt(offset);
    }

    public static MeasuredParagraph buildForBidi(CharSequence text, int start, int end, TextDirectionHeuristic textDir, MeasuredParagraph recycle) {
        MeasuredParagraph mt = recycle == null ? obtain() : recycle;
        mt.resetAndAnalyzeBidi(text, start, end, textDir);
        return mt;
    }

    public static MeasuredParagraph buildForMeasurement(TextPaint paint, CharSequence text, int start, int end, TextDirectionHeuristic textDir, MeasuredParagraph recycle) {
        MeasuredParagraph mt = recycle == null ? obtain() : recycle;
        mt.resetAndAnalyzeBidi(text, start, end, textDir);
        mt.mWidths.resize(mt.mTextLength);
        if (mt.mTextLength == 0) {
            return mt;
        }
        if (mt.mSpanned == null) {
            mt.applyMetricsAffectingSpan(paint, null, start, end, null);
        } else {
            int spanStart = start;
            while (spanStart < end) {
                int spanEnd = mt.mSpanned.nextSpanTransition(spanStart, end, MetricAffectingSpan.class);
                mt.applyMetricsAffectingSpan(paint, (MetricAffectingSpan[]) TextUtils.removeEmptySpans((MetricAffectingSpan[]) mt.mSpanned.getSpans(spanStart, spanEnd, MetricAffectingSpan.class), mt.mSpanned, MetricAffectingSpan.class), spanStart, spanEnd, null);
                spanStart = spanEnd;
            }
        }
        return mt;
    }

    public static MeasuredParagraph buildForStaticLayout(TextPaint paint, CharSequence text, int start, int end, TextDirectionHeuristic textDir, boolean computeHyphenation, boolean computeLayout, MeasuredParagraph hint, MeasuredParagraph recycle) {
        Builder builder;
        boolean z;
        boolean z2;
        int i = end;
        MeasuredParagraph measuredParagraph = hint;
        MeasuredParagraph mt = recycle == null ? obtain() : recycle;
        mt.resetAndAnalyzeBidi(text, start, i, textDir);
        if (measuredParagraph == null) {
            builder = new Builder(mt.mCopiedBuffer).setComputeHyphenation(computeHyphenation).setComputeLayout(computeLayout);
        } else {
            z = computeHyphenation;
            z2 = computeLayout;
            builder = new Builder(measuredParagraph.mMeasuredText);
        }
        if (mt.mTextLength == 0) {
            mt.mMeasuredText = builder.build();
        } else {
            if (mt.mSpanned == null) {
                mt.applyMetricsAffectingSpan(paint, null, start, end, builder);
                mt.mSpanEndCache.append(i);
            } else {
                int spanStart = start;
                while (spanStart < i) {
                    int spanEnd = mt.mSpanned.nextSpanTransition(spanStart, i, MetricAffectingSpan.class);
                    mt.applyMetricsAffectingSpan(paint, (MetricAffectingSpan[]) TextUtils.removeEmptySpans((MetricAffectingSpan[]) mt.mSpanned.getSpans(spanStart, spanEnd, MetricAffectingSpan.class), mt.mSpanned, MetricAffectingSpan.class), spanStart, spanEnd, builder);
                    mt.mSpanEndCache.append(spanEnd);
                    spanStart = spanEnd;
                    int i2 = start;
                    TextDirectionHeuristic textDirectionHeuristic = textDir;
                    z = computeHyphenation;
                    z2 = computeLayout;
                }
            }
            mt.mMeasuredText = builder.build();
        }
        return mt;
    }

    private void resetAndAnalyzeBidi(CharSequence text, int start, int end, TextDirectionHeuristic textDir) {
        int i;
        reset();
        this.mSpanned = text instanceof Spanned ? (Spanned) text : null;
        this.mTextStart = start;
        this.mTextLength = end - start;
        char[] cArr = this.mCopiedBuffer;
        if (cArr == null || cArr.length != this.mTextLength) {
            this.mCopiedBuffer = new char[this.mTextLength];
        }
        TextUtils.getChars(text, start, end, this.mCopiedBuffer, 0);
        Spanned spanned = this.mSpanned;
        if (spanned != null) {
            ReplacementSpan[] spans = (ReplacementSpan[]) spanned.getSpans(start, end, ReplacementSpan.class);
            for (i = 0; i < spans.length; i++) {
                int startInPara = this.mSpanned.getSpanStart(spans[i]) - start;
                int endInPara = this.mSpanned.getSpanEnd(spans[i]) - start;
                if (startInPara < 0) {
                    startInPara = 0;
                }
                if (endInPara > this.mTextLength) {
                    endInPara = this.mTextLength;
                }
                Arrays.fill(this.mCopiedBuffer, startInPara, endInPara, OBJECT_REPLACEMENT_CHARACTER);
            }
        }
        i = 1;
        if ((textDir == TextDirectionHeuristics.LTR || textDir == TextDirectionHeuristics.FIRSTSTRONG_LTR || textDir == TextDirectionHeuristics.ANYRTL_LTR) && TextUtils.doesNotNeedBidi(this.mCopiedBuffer, 0, this.mTextLength)) {
            this.mLevels.clear();
            this.mParaDir = 1;
            this.mLtrWithoutBidi = true;
            return;
        }
        int bidiRequest;
        if (textDir == TextDirectionHeuristics.LTR) {
            bidiRequest = 1;
        } else if (textDir == TextDirectionHeuristics.RTL) {
            bidiRequest = -1;
        } else if (textDir == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
            bidiRequest = 2;
        } else if (textDir == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
            bidiRequest = -2;
        } else {
            if (textDir.isRtl(this.mCopiedBuffer, 0, this.mTextLength)) {
                i = -1;
            }
            bidiRequest = i;
        }
        this.mLevels.resize(this.mTextLength);
        this.mParaDir = AndroidBidi.bidi(bidiRequest, this.mCopiedBuffer, this.mLevels.getRawArray());
        this.mLtrWithoutBidi = false;
    }

    private void applyReplacementRun(ReplacementSpan replacement, int start, int end, Builder builder) {
        TextPaint textPaint = this.mCachedPaint;
        Spanned spanned = this.mSpanned;
        int i = this.mTextStart;
        float width = (float) replacement.getSize(textPaint, spanned, start + i, end + i, this.mCachedFm);
        if (builder == null) {
            this.mWidths.set(start, width);
            if (end > start + 1) {
                Arrays.fill(this.mWidths.getRawArray(), start + 1, end, 0.0f);
            }
            this.mWholeWidth += width;
            return;
        }
        builder.appendReplacementRun(this.mCachedPaint, end - start, width);
    }

    private void applyStyleRun(int start, int end, Builder builder) {
        int i = start;
        int i2 = end;
        Builder builder2 = builder;
        if (!this.mLtrWithoutBidi) {
            int level = this.mLevels.get(i);
            int levelStart = start;
            int levelEnd = i + 1;
            while (true) {
                if (levelEnd == i2 || this.mLevels.get(levelEnd) != level) {
                    boolean isRtl = (level & 1) != 0;
                    if (builder2 == null) {
                        int levelLength = levelEnd - levelStart;
                        this.mWholeWidth += this.mCachedPaint.getTextRunAdvances(this.mCopiedBuffer, levelStart, levelLength, levelStart, levelLength, isRtl, this.mWidths.getRawArray(), levelStart);
                    } else {
                        builder2.appendStyleRun(this.mCachedPaint, levelEnd - levelStart, isRtl);
                    }
                    if (levelEnd != i2) {
                        levelStart = levelEnd;
                        level = this.mLevels.get(levelEnd);
                    } else {
                        return;
                    }
                }
                levelEnd++;
            }
        } else if (builder2 == null) {
            this.mWholeWidth += this.mCachedPaint.getTextRunAdvances(this.mCopiedBuffer, start, i2 - i, start, i2 - i, false, this.mWidths.getRawArray(), start);
        } else {
            builder2.appendStyleRun(this.mCachedPaint, i2 - i, false);
        }
    }

    private void applyMetricsAffectingSpan(TextPaint paint, MetricAffectingSpan[] spans, int start, int end, Builder builder) {
        this.mCachedPaint.set(paint);
        boolean z = false;
        this.mCachedPaint.baselineShift = 0;
        if (builder != null) {
            z = true;
        }
        boolean needFontMetrics = z;
        if (needFontMetrics && this.mCachedFm == null) {
            this.mCachedFm = new FontMetricsInt();
        }
        ReplacementSpan replacement = null;
        if (spans != null) {
            for (MetricAffectingSpan span : spans) {
                if (span instanceof ReplacementSpan) {
                    replacement = (ReplacementSpan) span;
                } else {
                    span.updateMeasureState(this.mCachedPaint);
                }
            }
        }
        int i = this.mTextStart;
        int startInCopiedBuffer = start - i;
        i = end - i;
        if (builder != null) {
            this.mCachedPaint.getFontMetricsInt(this.mCachedFm);
        }
        if (replacement != null) {
            applyReplacementRun(replacement, startInCopiedBuffer, i, builder);
        } else {
            applyStyleRun(startInCopiedBuffer, i, builder);
        }
        if (needFontMetrics) {
            FontMetricsInt fontMetricsInt;
            if (this.mCachedPaint.baselineShift < 0) {
                fontMetricsInt = this.mCachedFm;
                fontMetricsInt.ascent += this.mCachedPaint.baselineShift;
                fontMetricsInt = this.mCachedFm;
                fontMetricsInt.top += this.mCachedPaint.baselineShift;
            } else {
                fontMetricsInt = this.mCachedFm;
                fontMetricsInt.descent += this.mCachedPaint.baselineShift;
                fontMetricsInt = this.mCachedFm;
                fontMetricsInt.bottom += this.mCachedPaint.baselineShift;
            }
            this.mFontMetrics.append(this.mCachedFm.top);
            this.mFontMetrics.append(this.mCachedFm.bottom);
            this.mFontMetrics.append(this.mCachedFm.ascent);
            this.mFontMetrics.append(this.mCachedFm.descent);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int breakText(int limit, boolean forwards, float width) {
        float[] w = this.mWidths.getRawArray();
        int i;
        if (forwards) {
            i = 0;
            while (i < limit) {
                width -= w[i];
                if (width < 0.0f) {
                    break;
                }
                i++;
            }
            while (i > 0 && this.mCopiedBuffer[i - 1] == ' ') {
                i--;
            }
            return i;
        }
        i = limit - 1;
        while (i >= 0) {
            width -= w[i];
            if (width < 0.0f) {
                break;
            }
            i--;
        }
        while (i < limit - 1 && (this.mCopiedBuffer[i + 1] == ' ' || w[i + 1] == 0.0f)) {
            i++;
        }
        return (limit - i) - 1;
    }

    /* Access modifiers changed, original: 0000 */
    public float measure(int start, int limit) {
        float width = 0.0f;
        float[] w = this.mWidths.getRawArray();
        for (int i = start; i < limit; i++) {
            width += w[i];
        }
        return width;
    }

    public int getMemoryUsage() {
        return this.mMeasuredText.getMemoryUsage();
    }
}
