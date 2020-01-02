package android.text;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Path;
import android.text.Layout.Alignment;
import android.text.Layout.Directions;
import android.text.TextUtils.EllipsizeCallback;
import android.text.TextUtils.TruncateAt;
import android.text.style.ParagraphStyle;

public class BoringLayout extends Layout implements EllipsizeCallback {
    int mBottom;
    private int mBottomPadding;
    int mDesc;
    private String mDirect;
    private int mEllipsizedCount;
    private int mEllipsizedStart;
    private int mEllipsizedWidth;
    private float mMax;
    private Paint mPaint;
    private int mTopPadding;

    public static class Metrics extends FontMetricsInt {
        public int width;

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(super.toString());
            stringBuilder.append(" width=");
            stringBuilder.append(this.width);
            return stringBuilder.toString();
        }

        private void reset() {
            this.top = 0;
            this.bottom = 0;
            this.ascent = 0;
            this.descent = 0;
            this.width = 0;
            this.leading = 0;
        }
    }

    public static BoringLayout make(CharSequence source, TextPaint paint, int outerWidth, Alignment align, float spacingMult, float spacingAdd, Metrics metrics, boolean includePad) {
        return new BoringLayout(source, paint, outerWidth, align, spacingMult, spacingAdd, metrics, includePad);
    }

    public static BoringLayout make(CharSequence source, TextPaint paint, int outerWidth, Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includePad, TruncateAt ellipsize, int ellipsizedWidth) {
        return new BoringLayout(source, paint, outerWidth, align, spacingmult, spacingadd, metrics, includePad, ellipsize, ellipsizedWidth);
    }

    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int outerwidth, Alignment align, float spacingMult, float spacingAdd, Metrics metrics, boolean includePad) {
        replaceWith(source, paint, outerwidth, align, spacingMult, spacingAdd);
        this.mEllipsizedWidth = outerwidth;
        this.mEllipsizedStart = 0;
        this.mEllipsizedCount = 0;
        init(source, paint, align, metrics, includePad, true);
        return this;
    }

    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int outerWidth, Alignment align, float spacingMult, float spacingAdd, Metrics metrics, boolean includePad, TruncateAt ellipsize, int ellipsizedWidth) {
        boolean trust;
        TruncateAt truncateAt = ellipsize;
        int i = ellipsizedWidth;
        if (truncateAt == null || truncateAt == TruncateAt.MARQUEE) {
            replaceWith(source, paint, outerWidth, align, spacingMult, spacingAdd);
            this.mEllipsizedWidth = outerWidth;
            this.mEllipsizedStart = 0;
            this.mEllipsizedCount = 0;
            trust = true;
        } else {
            replaceWith(TextUtils.ellipsize(source, paint, (float) i, ellipsize, true, this), paint, outerWidth, align, spacingMult, spacingAdd);
            this.mEllipsizedWidth = i;
            int i2 = outerWidth;
            trust = false;
        }
        init(getText(), paint, align, metrics, includePad, trust);
        return this;
    }

    public BoringLayout(CharSequence source, TextPaint paint, int outerwidth, Alignment align, float spacingMult, float spacingAdd, Metrics metrics, boolean includePad) {
        super(source, paint, outerwidth, align, spacingMult, spacingAdd);
        this.mEllipsizedWidth = outerwidth;
        this.mEllipsizedStart = 0;
        this.mEllipsizedCount = 0;
        init(source, paint, align, metrics, includePad, true);
    }

    public BoringLayout(CharSequence source, TextPaint paint, int outerWidth, Alignment align, float spacingMult, float spacingAdd, Metrics metrics, boolean includePad, TruncateAt ellipsize, int ellipsizedWidth) {
        boolean trust;
        TruncateAt truncateAt = ellipsize;
        int i = ellipsizedWidth;
        super(source, paint, outerWidth, align, spacingMult, spacingAdd);
        if (truncateAt == null || truncateAt == TruncateAt.MARQUEE) {
            this.mEllipsizedWidth = outerWidth;
            this.mEllipsizedStart = 0;
            this.mEllipsizedCount = 0;
            trust = true;
        } else {
            replaceWith(TextUtils.ellipsize(source, paint, (float) i, ellipsize, true, this), paint, outerWidth, align, spacingMult, spacingAdd);
            this.mEllipsizedWidth = i;
            int i2 = outerWidth;
            trust = false;
        }
        init(getText(), paint, align, metrics, includePad, trust);
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0022  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:18:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x007e  */
    public void init(java.lang.CharSequence r20, android.text.TextPaint r21, android.text.Layout.Alignment r22, android.text.BoringLayout.Metrics r23, boolean r24, boolean r25) {
        /*
        r19 = this;
        r0 = r19;
        r1 = r23;
        r13 = r20;
        r2 = r13 instanceof java.lang.String;
        r14 = 0;
        if (r2 == 0) goto L_0x0018;
    L_0x000b:
        r2 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r15 = r22;
        if (r15 != r2) goto L_0x001a;
    L_0x0011:
        r2 = r20.toString();
        r0.mDirect = r2;
        goto L_0x001c;
    L_0x0018:
        r15 = r22;
    L_0x001a:
        r0.mDirect = r14;
    L_0x001c:
        r12 = r21;
        r0.mPaint = r12;
        if (r24 == 0) goto L_0x002d;
    L_0x0022:
        r2 = r1.bottom;
        r3 = r1.top;
        r2 = r2 - r3;
        r3 = r1.bottom;
        r0.mDesc = r3;
        r11 = r2;
        goto L_0x0037;
    L_0x002d:
        r2 = r1.descent;
        r3 = r1.ascent;
        r2 = r2 - r3;
        r3 = r1.descent;
        r0.mDesc = r3;
        r11 = r2;
    L_0x0037:
        r0.mBottom = r11;
        if (r25 == 0) goto L_0x0043;
    L_0x003b:
        r2 = r1.width;
        r2 = (float) r2;
        r0.mMax = r2;
        r16 = r11;
        goto L_0x007c;
    L_0x0043:
        r10 = android.text.TextLine.obtain();
        r5 = 0;
        r6 = r20.length();
        r7 = 1;
        r8 = android.text.Layout.DIRS_ALL_LEFT_TO_RIGHT;
        r9 = 0;
        r16 = 0;
        r4 = r0.mEllipsizedStart;
        r2 = r0.mEllipsizedCount;
        r17 = r4 + r2;
        r2 = r10;
        r3 = r21;
        r18 = r4;
        r4 = r20;
        r14 = r10;
        r10 = r16;
        r16 = r11;
        r11 = r18;
        r12 = r17;
        r2.set(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12);
        r2 = 0;
        r2 = r14.metrics(r2);
        r2 = (double) r2;
        r2 = java.lang.Math.ceil(r2);
        r2 = (int) r2;
        r2 = (float) r2;
        r0.mMax = r2;
        android.text.TextLine.recycle(r14);
    L_0x007c:
        if (r24 == 0) goto L_0x008c;
    L_0x007e:
        r2 = r1.top;
        r3 = r1.ascent;
        r2 = r2 - r3;
        r0.mTopPadding = r2;
        r2 = r1.bottom;
        r3 = r1.descent;
        r2 = r2 - r3;
        r0.mBottomPadding = r2;
    L_0x008c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.BoringLayout.init(java.lang.CharSequence, android.text.TextPaint, android.text.Layout$Alignment, android.text.BoringLayout$Metrics, boolean, boolean):void");
    }

    public static Metrics isBoring(CharSequence text, TextPaint paint) {
        return isBoring(text, paint, TextDirectionHeuristics.FIRSTSTRONG_LTR, null);
    }

    public static Metrics isBoring(CharSequence text, TextPaint paint, Metrics metrics) {
        return isBoring(text, paint, TextDirectionHeuristics.FIRSTSTRONG_LTR, metrics);
    }

    private static boolean hasAnyInterestingChars(CharSequence text, int textLength) {
        char[] buffer = TextUtils.obtain(500);
        int start = 0;
        while (start < textLength) {
            try {
                int end = Math.min(start + 500, textLength);
                TextUtils.getChars(text, start, end, buffer, 0);
                int len = end - start;
                for (int i = 0; i < len; i++) {
                    char c = buffer[i];
                    if (c == 10 || c == 9 || TextUtils.couldAffectRtl(c)) {
                        TextUtils.recycle(buffer);
                        return true;
                    }
                }
                start += 500;
            } catch (Throwable th) {
                TextUtils.recycle(buffer);
            }
        }
        TextUtils.recycle(buffer);
        return false;
    }

    @UnsupportedAppUsage
    public static Metrics isBoring(CharSequence text, TextPaint paint, TextDirectionHeuristic textDir, Metrics metrics) {
        CharSequence charSequence = text;
        TextDirectionHeuristic textDirectionHeuristic = textDir;
        int textLength = text.length();
        if (hasAnyInterestingChars(charSequence, textLength)) {
            return null;
        }
        if (textDirectionHeuristic != null && textDirectionHeuristic.isRtl(charSequence, 0, textLength)) {
            return null;
        }
        if ((charSequence instanceof Spanned) && ((Spanned) charSequence).getSpans(0, textLength, ParagraphStyle.class).length > 0) {
            return null;
        }
        Metrics fm;
        Metrics fm2 = metrics;
        if (fm2 == null) {
            fm = new Metrics();
        } else {
            fm2.reset();
            fm = fm2;
        }
        TextLine line = TextLine.obtain();
        line.set(paint, text, 0, textLength, 1, Layout.DIRS_ALL_LEFT_TO_RIGHT, false, null, 0, 0);
        fm.width = (int) Math.ceil((double) line.metrics(fm));
        TextLine.recycle(line);
        return fm;
    }

    public int getHeight() {
        return this.mBottom;
    }

    public int getLineCount() {
        return 1;
    }

    public int getLineTop(int line) {
        if (line == 0) {
            return 0;
        }
        return this.mBottom;
    }

    public int getLineDescent(int line) {
        return this.mDesc;
    }

    public int getLineStart(int line) {
        if (line == 0) {
            return 0;
        }
        return getText().length();
    }

    public int getParagraphDirection(int line) {
        return 1;
    }

    public boolean getLineContainsTab(int line) {
        return false;
    }

    public float getLineMax(int line) {
        return this.mMax;
    }

    public float getLineWidth(int line) {
        return line == 0 ? this.mMax : 0.0f;
    }

    public final Directions getLineDirections(int line) {
        return Layout.DIRS_ALL_LEFT_TO_RIGHT;
    }

    public int getTopPadding() {
        return this.mTopPadding;
    }

    public int getBottomPadding() {
        return this.mBottomPadding;
    }

    public int getEllipsisCount(int line) {
        return this.mEllipsizedCount;
    }

    public int getEllipsisStart(int line) {
        return this.mEllipsizedStart;
    }

    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    public void draw(Canvas c, Path highlight, Paint highlightpaint, int cursorOffset) {
        String str = this.mDirect;
        if (str == null || highlight != null) {
            super.draw(c, highlight, highlightpaint, cursorOffset);
        } else {
            c.drawText(str, 0.0f, (float) (this.mBottom - this.mDesc), this.mPaint);
        }
    }

    public void ellipsized(int start, int end) {
        this.mEllipsizedStart = start;
        this.mEllipsizedCount = end - start;
    }
}
