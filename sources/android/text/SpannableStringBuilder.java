package android.text;

import android.annotation.UnsupportedAppUsage;
import android.graphics.BaseCanvas;
import android.graphics.Paint;
import android.net.wifi.WifiEnterpriseConfig;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.lang.reflect.Array;
import java.util.IdentityHashMap;
import libcore.util.EmptyArray;

public class SpannableStringBuilder implements CharSequence, GetChars, Spannable, Editable, Appendable, GraphicsOperations {
    private static final int END_MASK = 15;
    private static final int MARK = 1;
    private static final InputFilter[] NO_FILTERS = new InputFilter[0];
    private static final int PARAGRAPH = 3;
    private static final int POINT = 2;
    private static final int SPAN_ADDED = 2048;
    private static final int SPAN_END_AT_END = 32768;
    private static final int SPAN_END_AT_START = 16384;
    private static final int SPAN_START_AT_END = 8192;
    private static final int SPAN_START_AT_START = 4096;
    private static final int SPAN_START_END_MASK = 61440;
    private static final int START_MASK = 240;
    private static final int START_SHIFT = 4;
    private static final String TAG = "SpannableStringBuilder";
    @GuardedBy({"sCachedIntBuffer"})
    private static final int[][] sCachedIntBuffer = ((int[][]) Array.newInstance(int.class, new int[]{6, 0}));
    private InputFilter[] mFilters;
    @UnsupportedAppUsage
    private int mGapLength;
    @UnsupportedAppUsage
    private int mGapStart;
    private IdentityHashMap<Object, Integer> mIndexOfSpan;
    private int mLowWaterMark;
    @UnsupportedAppUsage
    private int mSpanCount;
    @UnsupportedAppUsage
    private int[] mSpanEnds;
    @UnsupportedAppUsage
    private int[] mSpanFlags;
    private int mSpanInsertCount;
    private int[] mSpanMax;
    private int[] mSpanOrder;
    @UnsupportedAppUsage
    private int[] mSpanStarts;
    @UnsupportedAppUsage
    private Object[] mSpans;
    @UnsupportedAppUsage
    private char[] mText;
    private int mTextWatcherDepth;

    public SpannableStringBuilder() {
        this("");
    }

    public SpannableStringBuilder(CharSequence text) {
        this(text, 0, text.length());
    }

    public SpannableStringBuilder(CharSequence text, int start, int end) {
        CharSequence charSequence = text;
        int i = start;
        int i2 = end;
        this.mFilters = NO_FILTERS;
        int srclen = i2 - i;
        if (srclen >= 0) {
            this.mText = ArrayUtils.newUnpaddedCharArray(GrowingArrayUtils.growSize(srclen));
            this.mGapStart = srclen;
            char[] cArr = this.mText;
            this.mGapLength = cArr.length - srclen;
            TextUtils.getChars(charSequence, i, i2, cArr, 0);
            this.mSpanCount = 0;
            this.mSpanInsertCount = 0;
            this.mSpans = EmptyArray.OBJECT;
            this.mSpanStarts = EmptyArray.INT;
            this.mSpanEnds = EmptyArray.INT;
            this.mSpanFlags = EmptyArray.INT;
            this.mSpanMax = EmptyArray.INT;
            this.mSpanOrder = EmptyArray.INT;
            if (charSequence instanceof Spanned) {
                Spanned sp = (Spanned) charSequence;
                Object[] spans = sp.getSpans(i, i2, Object.class);
                for (int i3 = 0; i3 < spans.length; i3++) {
                    if (!(spans[i3] instanceof NoCopySpan)) {
                        int st;
                        int en;
                        int st2 = sp.getSpanStart(spans[i3]) - i;
                        int en2 = sp.getSpanEnd(spans[i3]) - i;
                        int fl = sp.getSpanFlags(spans[i3]);
                        if (st2 < 0) {
                            st2 = 0;
                        }
                        if (st2 > i2 - i) {
                            st = i2 - i;
                        } else {
                            st = st2;
                        }
                        if (en2 < 0) {
                            en2 = 0;
                        }
                        if (en2 > i2 - i) {
                            en = i2 - i;
                        } else {
                            en = en2;
                        }
                        setSpan(false, spans[i3], st, en, fl, false);
                    }
                }
                restoreInvariants();
                return;
            }
            return;
        }
        throw new StringIndexOutOfBoundsException();
    }

    public static SpannableStringBuilder valueOf(CharSequence source) {
        if (source instanceof SpannableStringBuilder) {
            return (SpannableStringBuilder) source;
        }
        return new SpannableStringBuilder(source);
    }

    public char charAt(int where) {
        int len = length();
        String str = "charAt: ";
        StringBuilder stringBuilder;
        if (where < 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(where);
            stringBuilder.append(" < 0");
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        } else if (where >= len) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(where);
            stringBuilder.append(" >= length ");
            stringBuilder.append(len);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        } else if (where >= this.mGapStart) {
            return this.mText[this.mGapLength + where];
        } else {
            return this.mText[where];
        }
    }

    public int length() {
        return this.mText.length - this.mGapLength;
    }

    private void resizeFor(int size) {
        int oldLength = this.mText.length;
        if (size + 1 > oldLength) {
            char[] newText = ArrayUtils.newUnpaddedCharArray(GrowingArrayUtils.growSize(size));
            System.arraycopy(this.mText, 0, newText, 0, this.mGapStart);
            int newLength = newText.length;
            int delta = newLength - oldLength;
            int after = oldLength - (this.mGapStart + this.mGapLength);
            System.arraycopy(this.mText, oldLength - after, newText, newLength - after, after);
            this.mText = newText;
            this.mGapLength += delta;
            if (this.mGapLength < 1) {
                new Exception("mGapLength < 1").printStackTrace();
            }
            if (this.mSpanCount != 0) {
                for (int i = 0; i < this.mSpanCount; i++) {
                    int[] iArr = this.mSpanStarts;
                    if (iArr[i] > this.mGapStart) {
                        iArr[i] = iArr[i] + delta;
                    }
                    iArr = this.mSpanEnds;
                    if (iArr[i] > this.mGapStart) {
                        iArr[i] = iArr[i] + delta;
                    }
                }
                calcMax(treeRoot());
            }
        }
    }

    private void moveGapTo(int where) {
        if (where != this.mGapStart) {
            int overlap;
            boolean atEnd = where == length();
            int i = this.mGapStart;
            char[] cArr;
            if (where < i) {
                overlap = i - where;
                cArr = this.mText;
                System.arraycopy(cArr, where, cArr, (i + this.mGapLength) - overlap, overlap);
            } else {
                overlap = where - i;
                cArr = this.mText;
                System.arraycopy(cArr, (this.mGapLength + where) - overlap, cArr, i, overlap);
            }
            if (this.mSpanCount != 0) {
                for (i = 0; i < this.mSpanCount; i++) {
                    int flag;
                    overlap = this.mSpanStarts[i];
                    int end = this.mSpanEnds[i];
                    if (overlap > this.mGapStart) {
                        overlap -= this.mGapLength;
                    }
                    if (overlap > where) {
                        overlap += this.mGapLength;
                    } else if (overlap == where) {
                        flag = (this.mSpanFlags[i] & 240) >> 4;
                        if (flag == 2 || (atEnd && flag == 3)) {
                            overlap += this.mGapLength;
                        }
                    }
                    if (end > this.mGapStart) {
                        end -= this.mGapLength;
                    }
                    if (end > where) {
                        end += this.mGapLength;
                    } else if (end == where) {
                        flag = this.mSpanFlags[i] & 15;
                        if (flag == 2 || (atEnd && flag == 3)) {
                            end += this.mGapLength;
                        }
                    }
                    this.mSpanStarts[i] = overlap;
                    this.mSpanEnds[i] = end;
                }
                calcMax(treeRoot());
            }
            this.mGapStart = where;
        }
    }

    public SpannableStringBuilder insert(int where, CharSequence tb, int start, int end) {
        return replace(where, where, tb, start, end);
    }

    public SpannableStringBuilder insert(int where, CharSequence tb) {
        return replace(where, where, tb, 0, tb.length());
    }

    public SpannableStringBuilder delete(int start, int end) {
        SpannableStringBuilder ret = replace(start, end, (CharSequence) "", 0, 0);
        if (this.mGapLength > length() * 2) {
            resizeFor(length());
        }
        return ret;
    }

    public void clear() {
        replace(0, length(), (CharSequence) "", 0, 0);
        this.mSpanInsertCount = 0;
    }

    public void clearSpans() {
        for (int i = this.mSpanCount - 1; i >= 0; i--) {
            Object what = this.mSpans[i];
            int ostart = this.mSpanStarts[i];
            int oend = this.mSpanEnds[i];
            if (ostart > this.mGapStart) {
                ostart -= this.mGapLength;
            }
            if (oend > this.mGapStart) {
                oend -= this.mGapLength;
            }
            this.mSpanCount = i;
            this.mSpans[i] = null;
            sendSpanRemoved(what, ostart, oend);
        }
        IdentityHashMap identityHashMap = this.mIndexOfSpan;
        if (identityHashMap != null) {
            identityHashMap.clear();
        }
        this.mSpanInsertCount = 0;
    }

    public SpannableStringBuilder append(CharSequence text) {
        int length = length();
        return replace(length, length, text, 0, text.length());
    }

    public SpannableStringBuilder append(CharSequence text, Object what, int flags) {
        int start = length();
        append(text);
        setSpan(what, start, length(), flags);
        return this;
    }

    public SpannableStringBuilder append(CharSequence text, int start, int end) {
        int length = length();
        return replace(length, length, text, start, end);
    }

    public SpannableStringBuilder append(char text) {
        return append(String.valueOf(text));
    }

    private boolean removeSpansForChange(int start, int end, boolean textIsRemoved, int i) {
        boolean z = true;
        if ((i & 1) != 0 && resolveGap(this.mSpanMax[i]) >= start && removeSpansForChange(start, end, textIsRemoved, leftChild(i))) {
            return true;
        }
        if (i >= this.mSpanCount) {
            return false;
        }
        if ((this.mSpanFlags[i] & 33) == 33) {
            int[] iArr = this.mSpanStarts;
            if (iArr[i] >= start) {
                int i2 = iArr[i];
                int i3 = this.mGapStart;
                int i4 = this.mGapLength;
                if (i2 < i3 + i4) {
                    int[] iArr2 = this.mSpanEnds;
                    if (iArr2[i] >= start && iArr2[i] < i4 + i3 && (textIsRemoved || iArr[i] > start || iArr2[i] < i3)) {
                        this.mIndexOfSpan.remove(this.mSpans[i]);
                        removeSpan(i, 0);
                        return true;
                    }
                }
            }
        }
        if (resolveGap(this.mSpanStarts[i]) > end || (i & 1) == 0 || !removeSpansForChange(start, end, textIsRemoved, rightChild(i))) {
            z = false;
        }
        return z;
    }

    private void change(int start, int end, CharSequence cs, int csStart, int csEnd) {
        int spanStart;
        int spanEnd;
        int ost;
        int clen;
        int i;
        int spanEnd2;
        int spanStart2;
        CharSequence charSequence;
        int i2 = start;
        int i3 = end;
        CharSequence charSequence2 = cs;
        int i4 = csStart;
        int i5 = csEnd;
        int replacedLength = i3 - i2;
        int replacementLength = i5 - i4;
        int nbNewChars = replacementLength - replacedLength;
        boolean changed = false;
        for (int i6 = this.mSpanCount - 1; i6 >= 0; i6--) {
            spanStart = this.mSpanStarts[i6];
            if (spanStart > this.mGapStart) {
                spanStart -= this.mGapLength;
            }
            spanEnd = this.mSpanEnds[i6];
            if (spanEnd > this.mGapStart) {
                spanEnd -= this.mGapLength;
            }
            if ((this.mSpanFlags[i6] & 51) == 51) {
                ost = spanStart;
                int oen = spanEnd;
                clen = length();
                if (spanStart > i2 && spanStart <= i3) {
                    spanStart = end;
                    while (spanStart < clen && (spanStart <= i3 || charAt(spanStart - 1) != 10)) {
                        spanStart++;
                    }
                }
                i = spanStart;
                if (spanEnd <= i2 || spanEnd > i3) {
                    spanEnd2 = spanEnd;
                } else {
                    spanStart = end;
                    while (spanStart < clen && (spanStart <= i3 || charAt(spanStart - 1) != 10)) {
                        spanStart++;
                    }
                    spanEnd2 = spanStart;
                }
                if (i == ost && spanEnd2 == oen) {
                    spanEnd = spanEnd2;
                    spanStart = i;
                } else {
                    int spanEnd3 = spanEnd2;
                    spanStart2 = i;
                    setSpan(false, this.mSpans[i6], i, spanEnd3, this.mSpanFlags[i6], true);
                    changed = true;
                    spanStart = spanStart2;
                    spanEnd = spanEnd3;
                }
            }
            spanEnd2 = 0;
            if (spanStart == i2) {
                spanEnd2 = 0 | 4096;
            } else if (spanStart == i3 + nbNewChars) {
                spanEnd2 = 0 | 8192;
            }
            if (spanEnd == i2) {
                spanEnd2 |= 16384;
            } else if (spanEnd == i3 + nbNewChars) {
                spanEnd2 |= 32768;
            }
            int[] iArr = this.mSpanFlags;
            iArr[i6] = iArr[i6] | spanEnd2;
        }
        if (changed) {
            restoreInvariants();
        }
        moveGapTo(i3);
        spanStart = this.mGapLength;
        if (nbNewChars >= spanStart) {
            resizeFor((this.mText.length + nbNewChars) - spanStart);
        }
        boolean textIsRemoved = replacementLength == 0;
        if (replacedLength > 0) {
            while (this.mSpanCount > 0 && removeSpansForChange(i2, i3, textIsRemoved, treeRoot())) {
            }
        }
        this.mGapStart += nbNewChars;
        this.mGapLength -= nbNewChars;
        if (this.mGapLength < 1) {
            new Exception("mGapLength < 1").printStackTrace();
        }
        TextUtils.getChars(charSequence2, i4, i5, this.mText, i2);
        int i7;
        if (replacedLength > 0) {
            boolean textIsRemoved2;
            boolean atEnd = this.mGapStart + this.mGapLength == this.mText.length;
            int i8 = 0;
            while (i8 < this.mSpanCount) {
                spanStart2 = (this.mSpanFlags[i8] & 240) >> 4;
                int[] iArr2 = this.mSpanStarts;
                iArr2[i8] = updatedIntervalBound(iArr2[i8], start, nbNewChars, spanStart2, atEnd, textIsRemoved);
                spanStart = this.mSpanFlags[i8] & 15;
                int[] iArr3 = this.mSpanEnds;
                i = i8;
                textIsRemoved2 = textIsRemoved;
                clen = i5;
                spanEnd2 = i4;
                charSequence = charSequence2;
                iArr3[i] = updatedIntervalBound(iArr3[i8], start, nbNewChars, spanStart, atEnd, textIsRemoved2);
                i8 = i + 1;
                i4 = spanEnd2;
                i5 = clen;
                charSequence2 = charSequence;
                textIsRemoved = textIsRemoved2;
                i3 = end;
            }
            i = i8;
            textIsRemoved2 = textIsRemoved;
            i7 = nbNewChars;
            clen = i5;
            spanEnd2 = i4;
            charSequence = charSequence2;
            restoreInvariants();
        } else {
            i7 = nbNewChars;
            clen = i5;
            spanEnd2 = i4;
            charSequence = charSequence2;
        }
        if (charSequence instanceof Spanned) {
            Spanned sp = (Spanned) charSequence;
            Object[] spans = sp.getSpans(spanEnd2, clen, Object.class);
            nbNewChars = 0;
            while (nbNewChars < spans.length) {
                spanStart = sp.getSpanStart(spans[nbNewChars]);
                spanEnd = sp.getSpanEnd(spans[nbNewChars]);
                if (spanStart < spanEnd2) {
                    spanStart = csStart;
                }
                i5 = spanStart;
                if (spanEnd > clen) {
                    spanEnd = csEnd;
                }
                i4 = spanEnd;
                if (getSpanStart(spans[nbNewChars]) < 0) {
                    ost = sp.getSpanFlags(spans[nbNewChars]) | 2048;
                    setSpan(false, spans[nbNewChars], (i5 - spanEnd2) + i2, (i4 - spanEnd2) + i2, ost, false);
                }
                nbNewChars++;
                spanEnd2 = csStart;
                clen = csEnd;
            }
            restoreInvariants();
        }
    }

    private int updatedIntervalBound(int offset, int start, int nbNewChars, int flag, boolean atEnd, boolean textIsRemoved) {
        if (offset >= start) {
            int i = this.mGapStart;
            int i2 = this.mGapLength;
            if (offset < i + i2) {
                if (flag == 2) {
                    if (textIsRemoved || offset > start) {
                        return this.mGapStart + this.mGapLength;
                    }
                } else if (flag == 3) {
                    if (atEnd) {
                        return i + i2;
                    }
                } else if (textIsRemoved || offset < i - nbNewChars) {
                    return start;
                } else {
                    return i;
                }
            }
        }
        return offset;
    }

    private void removeSpan(int i, int flags) {
        Object object = this.mSpans[i];
        int start = this.mSpanStarts[i];
        int end = this.mSpanEnds[i];
        if (start > this.mGapStart) {
            start -= this.mGapLength;
        }
        if (end > this.mGapStart) {
            end -= this.mGapLength;
        }
        int count = this.mSpanCount - (i + 1);
        Object[] objArr = this.mSpans;
        System.arraycopy(objArr, i + 1, objArr, i, count);
        int[] iArr = this.mSpanStarts;
        System.arraycopy(iArr, i + 1, iArr, i, count);
        iArr = this.mSpanEnds;
        System.arraycopy(iArr, i + 1, iArr, i, count);
        iArr = this.mSpanFlags;
        System.arraycopy(iArr, i + 1, iArr, i, count);
        iArr = this.mSpanOrder;
        System.arraycopy(iArr, i + 1, iArr, i, count);
        this.mSpanCount--;
        invalidateIndex(i);
        this.mSpans[this.mSpanCount] = null;
        restoreInvariants();
        if ((flags & 512) == 0) {
            sendSpanRemoved(object, start, end);
        }
    }

    public SpannableStringBuilder replace(int start, int end, CharSequence tb) {
        return replace(start, end, tb, 0, tb.length());
    }

    public SpannableStringBuilder replace(int start, int end, CharSequence tb, int tbstart, int tbend) {
        int i = start;
        int i2 = end;
        checkRange("replace", i, i2);
        CharSequence tb2 = tb;
        int tbstart2 = tbstart;
        int tbend2 = tbend;
        for (CharSequence repl : this.mFilters) {
            CharSequence repl2 = repl2.filter(tb2, tbstart2, tbend2, this, start, end);
            if (repl2 != null) {
                tb2 = repl2;
                tbstart2 = 0;
                tbend2 = repl2.length();
            }
        }
        int i3 = i2 - i;
        int newLen = tbend2 - tbstart2;
        if (i3 == 0 && newLen == 0 && !hasNonExclusiveExclusiveSpanAt(tb2, tbstart2)) {
            return this;
        }
        int selectionStart;
        int selectionEnd;
        TextWatcher[] textWatchers;
        TextWatcher[] textWatchers2 = (TextWatcher[]) getSpans(i, i + i3, TextWatcher.class);
        sendBeforeTextChanged(textWatchers2, i, i3, newLen);
        boolean z = (i3 == 0 || newLen == 0) ? false : true;
        boolean adjustSelection = z;
        if (adjustSelection) {
            selectionStart = Selection.getSelectionStart(this);
            selectionEnd = Selection.getSelectionEnd(this);
        } else {
            selectionStart = 0;
            selectionEnd = 0;
        }
        int selectionEnd2 = selectionEnd;
        TextWatcher[] textWatchers3 = textWatchers2;
        int selectionStart2 = selectionStart;
        change(start, end, tb2, tbstart2, tbend2);
        int i4;
        if (adjustSelection) {
            boolean changed;
            z = false;
            if (selectionStart2 <= i || selectionStart2 >= i2) {
                textWatchers = textWatchers3;
                i4 = selectionStart2;
            } else {
                long diff = (long) (selectionStart2 - i);
                i4 = i + Math.toIntExact((((long) newLen) * diff) / ((long) i3));
                textWatchers = textWatchers3;
                setSpan(false, Selection.SELECTION_START, i4, i4, 34, true);
                z = true;
            }
            int selectionEnd3 = selectionEnd2;
            if (selectionEnd3 <= i || selectionEnd3 >= i2) {
                changed = z;
                selectionEnd = selectionEnd3;
            } else {
                long diff2 = (long) (selectionEnd3 - i);
                selectionEnd2 = selectionEnd3;
                selectionEnd2 = i + Math.toIntExact((((long) newLen) * diff2) / ((long) i3));
                changed = true;
                selectionEnd = selectionEnd2;
                setSpan(false, Selection.SELECTION_END, selectionEnd2, selectionEnd, 34, true);
            }
            if (changed) {
                restoreInvariants();
            }
            selectionEnd2 = selectionEnd;
        } else {
            textWatchers = textWatchers3;
            i4 = selectionStart2;
        }
        sendTextChanged(textWatchers, i, i3, newLen);
        sendAfterTextChanged(textWatchers);
        sendToSpanWatchers(i, i2, newLen - i3);
        return this;
    }

    private static boolean hasNonExclusiveExclusiveSpanAt(CharSequence text, int offset) {
        if (text instanceof Spanned) {
            Spanned spanned = (Spanned) text;
            for (Object span : spanned.getSpans(offset, offset, Object.class)) {
                if (spanned.getSpanFlags(span) != 33) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x005a A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0052  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0052  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x005a A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0075  */
    @android.annotation.UnsupportedAppUsage
    private void sendToSpanWatchers(int r17, int r18, int r19) {
        /*
        r16 = this;
        r6 = r16;
        r7 = r17;
        r0 = 0;
        r8 = r0;
    L_0x0006:
        r0 = r6.mSpanCount;
        if (r8 >= r0) goto L_0x0090;
    L_0x000a:
        r0 = r6.mSpanFlags;
        r9 = r0[r8];
        r0 = r9 & 2048;
        if (r0 == 0) goto L_0x0014;
    L_0x0012:
        goto L_0x008c;
    L_0x0014:
        r0 = r6.mSpanStarts;
        r0 = r0[r8];
        r1 = r6.mSpanEnds;
        r1 = r1[r8];
        r2 = r6.mGapStart;
        if (r0 <= r2) goto L_0x0023;
    L_0x0020:
        r2 = r6.mGapLength;
        r0 = r0 - r2;
    L_0x0023:
        r10 = r0;
        r0 = r6.mGapStart;
        if (r1 <= r0) goto L_0x002b;
    L_0x0028:
        r0 = r6.mGapLength;
        r1 = r1 - r0;
    L_0x002b:
        r11 = r1;
        r12 = r18 + r19;
        r0 = 0;
        r1 = r10;
        if (r10 <= r12) goto L_0x0039;
    L_0x0032:
        if (r19 == 0) goto L_0x004e;
    L_0x0034:
        r1 = r1 - r19;
        r0 = 1;
        r13 = r1;
        goto L_0x004f;
    L_0x0039:
        if (r10 < r7) goto L_0x004e;
    L_0x003b:
        if (r10 != r7) goto L_0x0043;
    L_0x003d:
        r2 = r9 & 4096;
        r3 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        if (r2 == r3) goto L_0x004e;
    L_0x0043:
        if (r10 != r12) goto L_0x004b;
    L_0x0045:
        r2 = r9 & 8192;
        r3 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        if (r2 == r3) goto L_0x004e;
    L_0x004b:
        r0 = 1;
        r13 = r1;
        goto L_0x004f;
    L_0x004e:
        r13 = r1;
    L_0x004f:
        r1 = r11;
        if (r11 <= r12) goto L_0x005a;
    L_0x0052:
        if (r19 == 0) goto L_0x0071;
    L_0x0054:
        r1 = r1 - r19;
        r0 = 1;
        r14 = r0;
        r15 = r1;
        goto L_0x0073;
    L_0x005a:
        if (r11 < r7) goto L_0x0071;
    L_0x005c:
        if (r11 != r7) goto L_0x0064;
    L_0x005e:
        r2 = r9 & 16384;
        r3 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r2 == r3) goto L_0x0071;
    L_0x0064:
        if (r11 != r12) goto L_0x006d;
    L_0x0066:
        r2 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r3 = r9 & r2;
        if (r3 == r2) goto L_0x0071;
    L_0x006d:
        r0 = 1;
        r14 = r0;
        r15 = r1;
        goto L_0x0073;
    L_0x0071:
        r14 = r0;
        r15 = r1;
    L_0x0073:
        if (r14 == 0) goto L_0x0082;
    L_0x0075:
        r0 = r6.mSpans;
        r1 = r0[r8];
        r0 = r16;
        r2 = r13;
        r3 = r15;
        r4 = r10;
        r5 = r11;
        r0.sendSpanChanged(r1, r2, r3, r4, r5);
    L_0x0082:
        r0 = r6.mSpanFlags;
        r1 = r0[r8];
        r2 = -61441; // 0xffffffffffff0fff float:NaN double:NaN;
        r1 = r1 & r2;
        r0[r8] = r1;
    L_0x008c:
        r8 = r8 + 1;
        goto L_0x0006;
    L_0x0090:
        r0 = 0;
    L_0x0091:
        r1 = r6.mSpanCount;
        if (r0 >= r1) goto L_0x00c3;
    L_0x0095:
        r1 = r6.mSpanFlags;
        r2 = r1[r0];
        r3 = r2 & 2048;
        if (r3 == 0) goto L_0x00c0;
    L_0x009d:
        r3 = r1[r0];
        r3 = r3 & -2049;
        r1[r0] = r3;
        r1 = r6.mSpanStarts;
        r1 = r1[r0];
        r3 = r6.mSpanEnds;
        r3 = r3[r0];
        r4 = r6.mGapStart;
        if (r1 <= r4) goto L_0x00b2;
    L_0x00af:
        r4 = r6.mGapLength;
        r1 = r1 - r4;
    L_0x00b2:
        r4 = r6.mGapStart;
        if (r3 <= r4) goto L_0x00b9;
    L_0x00b6:
        r4 = r6.mGapLength;
        r3 = r3 - r4;
    L_0x00b9:
        r4 = r6.mSpans;
        r4 = r4[r0];
        r6.sendSpanAdded(r4, r1, r3);
    L_0x00c0:
        r0 = r0 + 1;
        goto L_0x0091;
    L_0x00c3:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.SpannableStringBuilder.sendToSpanWatchers(int, int, int):void");
    }

    public void setSpan(Object what, int start, int end, int flags) {
        setSpan(true, what, start, end, flags, true);
    }

    private void setSpan(boolean send, Object what, int start, int end, int flags, boolean enforceParagraph) {
        Object obj = what;
        int start2 = start;
        int i = end;
        int i2 = flags;
        checkRange("setSpan", start2, i);
        int flagsStart = (i2 & 240) >> 4;
        String str = ")";
        String str2 = " follows ";
        StringBuilder stringBuilder;
        if (!isInvalidParagraph(start2, flagsStart)) {
            int flagsEnd = i2 & 15;
            if (isInvalidParagraph(i, flagsEnd)) {
                if (enforceParagraph) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("PARAGRAPH span must end at paragraph boundary (");
                    stringBuilder.append(i);
                    stringBuilder.append(str2);
                    stringBuilder.append(charAt(i - 1));
                    stringBuilder.append(str);
                    throw new RuntimeException(stringBuilder.toString());
                }
            } else if (flagsStart == 2 && flagsEnd == 1 && start2 == i) {
                if (send) {
                    Log.e(TAG, "SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length");
                }
            } else {
                int start3;
                int end2;
                int nstart = start;
                int nend = end;
                int i3 = this.mGapStart;
                if (start2 > i3) {
                    start3 = start2 + this.mGapLength;
                } else if (start2 == i3 && (flagsStart == 2 || (flagsStart == 3 && start2 == length()))) {
                    start3 = start2 + this.mGapLength;
                } else {
                    start3 = start2;
                }
                start2 = this.mGapStart;
                if (i > start2) {
                    end2 = this.mGapLength + i;
                } else if (i == start2 && (flagsEnd == 2 || (flagsEnd == 3 && i == length()))) {
                    end2 = this.mGapLength + i;
                } else {
                    end2 = i;
                }
                IdentityHashMap identityHashMap = this.mIndexOfSpan;
                if (identityHashMap != null) {
                    Integer index = (Integer) identityHashMap.get(obj);
                    if (index != null) {
                        int ostart;
                        int oend;
                        int i4 = index.intValue();
                        start2 = this.mSpanStarts[i4];
                        i = this.mSpanEnds[i4];
                        if (start2 > this.mGapStart) {
                            ostart = start2 - this.mGapLength;
                        } else {
                            ostart = start2;
                        }
                        if (i > this.mGapStart) {
                            oend = i - this.mGapLength;
                        } else {
                            oend = i;
                        }
                        this.mSpanStarts[i4] = start3;
                        this.mSpanEnds[i4] = end2;
                        this.mSpanFlags[i4] = i2;
                        if (send) {
                            restoreInvariants();
                            sendSpanChanged(what, ostart, oend, nstart, nend);
                        }
                        return;
                    }
                }
                this.mSpans = GrowingArrayUtils.append(this.mSpans, this.mSpanCount, obj);
                this.mSpanStarts = GrowingArrayUtils.append(this.mSpanStarts, this.mSpanCount, start3);
                this.mSpanEnds = GrowingArrayUtils.append(this.mSpanEnds, this.mSpanCount, end2);
                this.mSpanFlags = GrowingArrayUtils.append(this.mSpanFlags, this.mSpanCount, i2);
                this.mSpanOrder = GrowingArrayUtils.append(this.mSpanOrder, this.mSpanCount, this.mSpanInsertCount);
                invalidateIndex(this.mSpanCount);
                this.mSpanCount++;
                this.mSpanInsertCount++;
                start2 = (treeRoot() * 2) + 1;
                if (this.mSpanMax.length < start2) {
                    this.mSpanMax = new int[start2];
                }
                if (send) {
                    restoreInvariants();
                    sendSpanAdded(obj, nstart, nend);
                }
            }
        } else if (enforceParagraph) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("PARAGRAPH span must start at paragraph boundary (");
            stringBuilder.append(start2);
            stringBuilder.append(str2);
            stringBuilder.append(charAt(start2 - 1));
            stringBuilder.append(str);
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    private boolean isInvalidParagraph(int index, int flag) {
        return (flag != 3 || index == 0 || index == length() || charAt(index - 1) == 10) ? false : true;
    }

    public void removeSpan(Object what) {
        removeSpan(what, 0);
    }

    public void removeSpan(Object what, int flags) {
        IdentityHashMap identityHashMap = this.mIndexOfSpan;
        if (identityHashMap != null) {
            Integer i = (Integer) identityHashMap.remove(what);
            if (i != null) {
                removeSpan(i.intValue(), flags);
            }
        }
    }

    private int resolveGap(int i) {
        return i > this.mGapStart ? i - this.mGapLength : i;
    }

    public int getSpanStart(Object what) {
        IdentityHashMap identityHashMap = this.mIndexOfSpan;
        int i = -1;
        if (identityHashMap == null) {
            return -1;
        }
        Integer i2 = (Integer) identityHashMap.get(what);
        if (i2 != null) {
            i = resolveGap(this.mSpanStarts[i2.intValue()]);
        }
        return i;
    }

    public int getSpanEnd(Object what) {
        IdentityHashMap identityHashMap = this.mIndexOfSpan;
        int i = -1;
        if (identityHashMap == null) {
            return -1;
        }
        Integer i2 = (Integer) identityHashMap.get(what);
        if (i2 != null) {
            i = resolveGap(this.mSpanEnds[i2.intValue()]);
        }
        return i;
    }

    public int getSpanFlags(Object what) {
        IdentityHashMap identityHashMap = this.mIndexOfSpan;
        int i = 0;
        if (identityHashMap == null) {
            return 0;
        }
        Integer i2 = (Integer) identityHashMap.get(what);
        if (i2 != null) {
            i = this.mSpanFlags[i2.intValue()];
        }
        return i;
    }

    public <T> T[] getSpans(int queryStart, int queryEnd, Class<T> kind) {
        return getSpans(queryStart, queryEnd, kind, true);
    }

    @UnsupportedAppUsage
    public <T> T[] getSpans(int queryStart, int queryEnd, Class<T> kind, boolean sortByInsertionOrder) {
        Class<T> cls = kind;
        if (cls == null) {
            return ArrayUtils.emptyArray(Object.class);
        }
        if (this.mSpanCount == 0) {
            return ArrayUtils.emptyArray(kind);
        }
        int count = countSpans(queryStart, queryEnd, cls, treeRoot());
        if (count == 0) {
            return ArrayUtils.emptyArray(kind);
        }
        Object[] ret = (Object[]) Array.newInstance(cls, count);
        int[] prioSortBuffer = sortByInsertionOrder ? obtain(count) : EmptyArray.INT;
        int[] orderSortBuffer = sortByInsertionOrder ? obtain(count) : EmptyArray.INT;
        int[] orderSortBuffer2 = orderSortBuffer;
        int[] prioSortBuffer2 = prioSortBuffer;
        getSpansRec(queryStart, queryEnd, kind, treeRoot(), ret, prioSortBuffer, orderSortBuffer, 0, sortByInsertionOrder);
        if (sortByInsertionOrder) {
            sort(ret, prioSortBuffer2, orderSortBuffer2);
            recycle(prioSortBuffer2);
            recycle(orderSortBuffer2);
        }
        return ret;
    }

    private int countSpans(int queryStart, int queryEnd, Class kind, int i) {
        int left;
        int spanMax;
        int count = 0;
        if ((i & 1) != 0) {
            left = leftChild(i);
            spanMax = this.mSpanMax[left];
            if (spanMax > this.mGapStart) {
                spanMax -= this.mGapLength;
            }
            if (spanMax >= queryStart) {
                count = countSpans(queryStart, queryEnd, kind, left);
            }
        }
        if (i >= this.mSpanCount) {
            return count;
        }
        left = this.mSpanStarts[i];
        if (left > this.mGapStart) {
            left -= this.mGapLength;
        }
        if (left > queryEnd) {
            return count;
        }
        spanMax = this.mSpanEnds[i];
        if (spanMax > this.mGapStart) {
            spanMax -= this.mGapLength;
        }
        if (spanMax >= queryStart && ((left == spanMax || queryStart == queryEnd || !(left == queryEnd || spanMax == queryStart)) && (Object.class == kind || kind.isInstance(this.mSpans[i])))) {
            count++;
        }
        if ((i & 1) != 0) {
            return count + countSpans(queryStart, queryEnd, kind, rightChild(i));
        }
        return count;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0048  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0047 A:{RETURN} */
    private <T> int getSpansRec(int r20, int r21, java.lang.Class<T> r22, int r23, T[] r24, int[] r25, int[] r26, int r27, boolean r28) {
        /*
        r19 = this;
        r10 = r19;
        r11 = r20;
        r12 = r21;
        r13 = r22;
        r14 = r23;
        r15 = r24;
        r0 = r14 & 1;
        if (r0 == 0) goto L_0x0041;
    L_0x0010:
        r16 = leftChild(r23);
        r0 = r10.mSpanMax;
        r0 = r0[r16];
        r1 = r10.mGapStart;
        if (r0 <= r1) goto L_0x0021;
    L_0x001c:
        r1 = r10.mGapLength;
        r0 = r0 - r1;
        r9 = r0;
        goto L_0x0022;
    L_0x0021:
        r9 = r0;
    L_0x0022:
        if (r9 < r11) goto L_0x003f;
    L_0x0024:
        r0 = r19;
        r1 = r20;
        r2 = r21;
        r3 = r22;
        r4 = r16;
        r5 = r24;
        r6 = r25;
        r7 = r26;
        r8 = r27;
        r17 = r9;
        r9 = r28;
        r0 = r0.getSpansRec(r1, r2, r3, r4, r5, r6, r7, r8, r9);
        goto L_0x0043;
    L_0x003f:
        r17 = r9;
    L_0x0041:
        r0 = r27;
    L_0x0043:
        r1 = r10.mSpanCount;
        if (r14 < r1) goto L_0x0048;
    L_0x0047:
        return r0;
    L_0x0048:
        r1 = r10.mSpanStarts;
        r1 = r1[r14];
        r2 = r10.mGapStart;
        if (r1 <= r2) goto L_0x0055;
    L_0x0050:
        r2 = r10.mGapLength;
        r1 = r1 - r2;
        r9 = r1;
        goto L_0x0056;
    L_0x0055:
        r9 = r1;
    L_0x0056:
        if (r9 > r12) goto L_0x00e7;
    L_0x0058:
        r1 = r10.mSpanEnds;
        r1 = r1[r14];
        r2 = r10.mGapStart;
        if (r1 <= r2) goto L_0x0065;
    L_0x0060:
        r2 = r10.mGapLength;
        r1 = r1 - r2;
        r8 = r1;
        goto L_0x0066;
    L_0x0065:
        r8 = r1;
    L_0x0066:
        if (r8 < r11) goto L_0x00b5;
    L_0x0068:
        if (r9 == r8) goto L_0x0070;
    L_0x006a:
        if (r11 == r12) goto L_0x0070;
    L_0x006c:
        if (r9 == r12) goto L_0x00b5;
    L_0x006e:
        if (r8 == r11) goto L_0x00b5;
    L_0x0070:
        r1 = java.lang.Object.class;
        if (r1 == r13) goto L_0x007e;
    L_0x0074:
        r1 = r10.mSpans;
        r1 = r1[r14];
        r1 = r13.isInstance(r1);
        if (r1 == 0) goto L_0x00b5;
    L_0x007e:
        r1 = r10.mSpanFlags;
        r1 = r1[r14];
        r2 = 16711680; // 0xff0000 float:2.3418052E-38 double:8.256667E-317;
        r1 = r1 & r2;
        r3 = r0;
        if (r28 == 0) goto L_0x0091;
    L_0x0088:
        r25[r3] = r1;
        r2 = r10.mSpanOrder;
        r2 = r2[r14];
        r26[r3] = r2;
        goto L_0x00ab;
    L_0x0091:
        if (r1 == 0) goto L_0x00ab;
    L_0x0093:
        r4 = 0;
    L_0x0094:
        if (r4 >= r0) goto L_0x00a3;
    L_0x0096:
        r5 = r15[r4];
        r5 = r10.getSpanFlags(r5);
        r5 = r5 & r2;
        if (r1 <= r5) goto L_0x00a0;
    L_0x009f:
        goto L_0x00a3;
    L_0x00a0:
        r4 = r4 + 1;
        goto L_0x0094;
    L_0x00a3:
        r2 = r4 + 1;
        r5 = r0 - r4;
        java.lang.System.arraycopy(r15, r4, r15, r2, r5);
        r3 = r4;
    L_0x00ab:
        r2 = r10.mSpans;
        r2 = r2[r14];
        r15[r3] = r2;
        r0 = r0 + 1;
        r7 = r0;
        goto L_0x00b6;
    L_0x00b5:
        r7 = r0;
    L_0x00b6:
        r0 = r15.length;
        if (r7 >= r0) goto L_0x00de;
    L_0x00b9:
        r0 = r14 & 1;
        if (r0 == 0) goto L_0x00de;
    L_0x00bd:
        r4 = rightChild(r23);
        r0 = r19;
        r1 = r20;
        r2 = r21;
        r3 = r22;
        r5 = r24;
        r6 = r25;
        r16 = r7;
        r7 = r26;
        r17 = r8;
        r8 = r16;
        r18 = r9;
        r9 = r28;
        r0 = r0.getSpansRec(r1, r2, r3, r4, r5, r6, r7, r8, r9);
        goto L_0x00e9;
    L_0x00de:
        r16 = r7;
        r17 = r8;
        r18 = r9;
        r0 = r16;
        goto L_0x00e9;
    L_0x00e7:
        r18 = r9;
    L_0x00e9:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.SpannableStringBuilder.getSpansRec(int, int, java.lang.Class, int, java.lang.Object[], int[], int[], int, boolean):int");
    }

    private static int[] obtain(int elementCount) {
        int[] result = null;
        synchronized (sCachedIntBuffer) {
            int candidateIndex = -1;
            for (int i = sCachedIntBuffer.length - 1; i >= 0; i--) {
                if (sCachedIntBuffer[i] != null) {
                    if (sCachedIntBuffer[i].length >= elementCount) {
                        candidateIndex = i;
                        break;
                    } else if (candidateIndex == -1) {
                        candidateIndex = i;
                    }
                }
            }
            if (candidateIndex != -1) {
                result = sCachedIntBuffer[candidateIndex];
                sCachedIntBuffer[candidateIndex] = null;
            }
        }
        return checkSortBuffer(result, elementCount);
    }

    private static void recycle(int[] buffer) {
        synchronized (sCachedIntBuffer) {
            int i = 0;
            while (i < sCachedIntBuffer.length) {
                if (sCachedIntBuffer[i] != null) {
                    if (buffer.length <= sCachedIntBuffer[i].length) {
                        i++;
                    }
                }
                sCachedIntBuffer[i] = buffer;
            }
        }
    }

    private static int[] checkSortBuffer(int[] buffer, int size) {
        if (buffer == null || size > buffer.length) {
            return ArrayUtils.newUnpaddedIntArray(GrowingArrayUtils.growSize(size));
        }
        return buffer;
    }

    private final <T> void sort(T[] array, int[] priority, int[] insertionOrder) {
        int i;
        int size = array.length;
        for (i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i, array, size, priority, insertionOrder);
        }
        for (i = size - 1; i > 0; i--) {
            T tmpSpan = array[0];
            array[0] = array[i];
            array[i] = tmpSpan;
            int tmpPriority = priority[0];
            priority[0] = priority[i];
            priority[i] = tmpPriority;
            int tmpOrder = insertionOrder[0];
            insertionOrder[0] = insertionOrder[i];
            insertionOrder[i] = tmpOrder;
            siftDown(0, array, i, priority, insertionOrder);
        }
    }

    private final <T> void siftDown(int index, T[] array, int size, int[] priority, int[] insertionOrder) {
        int left = (index * 2) + 1;
        while (left < size) {
            if (left < size - 1 && compareSpans(left, left + 1, priority, insertionOrder) < 0) {
                left++;
            }
            if (compareSpans(index, left, priority, insertionOrder) < 0) {
                T tmpSpan = array[index];
                array[index] = array[left];
                array[left] = tmpSpan;
                int tmpPriority = priority[index];
                priority[index] = priority[left];
                priority[left] = tmpPriority;
                int tmpOrder = insertionOrder[index];
                insertionOrder[index] = insertionOrder[left];
                insertionOrder[left] = tmpOrder;
                index = left;
                left = (index * 2) + 1;
            } else {
                return;
            }
        }
    }

    private final int compareSpans(int left, int right, int[] priority, int[] insertionOrder) {
        int priority1 = priority[left];
        int priority2 = priority[right];
        if (priority1 == priority2) {
            return Integer.compare(insertionOrder[left], insertionOrder[right]);
        }
        return Integer.compare(priority2, priority1);
    }

    public int nextSpanTransition(int start, int limit, Class kind) {
        if (this.mSpanCount == 0) {
            return limit;
        }
        if (kind == null) {
            kind = Object.class;
        }
        return nextSpanTransitionRec(start, limit, kind, treeRoot());
    }

    private int nextSpanTransitionRec(int start, int limit, Class kind, int i) {
        int left;
        if ((i & 1) != 0) {
            left = leftChild(i);
            if (resolveGap(this.mSpanMax[left]) > start) {
                limit = nextSpanTransitionRec(start, limit, kind, left);
            }
        }
        if (i >= this.mSpanCount) {
            return limit;
        }
        left = resolveGap(this.mSpanStarts[i]);
        int en = resolveGap(this.mSpanEnds[i]);
        if (left > start && left < limit && kind.isInstance(this.mSpans[i])) {
            limit = left;
        }
        if (en > start && en < limit && kind.isInstance(this.mSpans[i])) {
            limit = en;
        }
        if (left >= limit || (i & 1) == 0) {
            return limit;
        }
        return nextSpanTransitionRec(start, limit, kind, rightChild(i));
    }

    public CharSequence subSequence(int start, int end) {
        return new SpannableStringBuilder(this, start, end);
    }

    public void getChars(int start, int end, char[] dest, int destoff) {
        checkRange("getChars", start, end);
        int i = this.mGapStart;
        if (end <= i) {
            System.arraycopy(this.mText, start, dest, destoff, end - start);
        } else if (start >= i) {
            System.arraycopy(this.mText, this.mGapLength + start, dest, destoff, end - start);
        } else {
            System.arraycopy(this.mText, start, dest, destoff, i - start);
            char[] cArr = this.mText;
            int i2 = this.mGapStart;
            System.arraycopy(cArr, this.mGapLength + i2, dest, (i2 - start) + destoff, end - i2);
        }
    }

    public String toString() {
        int len = length();
        char[] buf = new char[len];
        getChars(0, len, buf, 0);
        return new String(buf);
    }

    @UnsupportedAppUsage
    public String substring(int start, int end) {
        char[] buf = new char[(end - start)];
        getChars(start, end, buf, 0);
        return new String(buf);
    }

    public int getTextWatcherDepth() {
        return this.mTextWatcherDepth;
    }

    private void sendBeforeTextChanged(TextWatcher[] watchers, int start, int before, int after) {
        this.mTextWatcherDepth++;
        for (TextWatcher beforeTextChanged : watchers) {
            beforeTextChanged.beforeTextChanged(this, start, before, after);
        }
        this.mTextWatcherDepth--;
    }

    private void sendTextChanged(TextWatcher[] watchers, int start, int before, int after) {
        this.mTextWatcherDepth++;
        for (TextWatcher onTextChanged : watchers) {
            onTextChanged.onTextChanged(this, start, before, after);
        }
        this.mTextWatcherDepth--;
    }

    private void sendAfterTextChanged(TextWatcher[] watchers) {
        this.mTextWatcherDepth++;
        for (TextWatcher afterTextChanged : watchers) {
            afterTextChanged.afterTextChanged(this);
        }
        this.mTextWatcherDepth--;
    }

    private void sendSpanAdded(Object what, int start, int end) {
        for (SpanWatcher onSpanAdded : (SpanWatcher[]) getSpans(start, end, SpanWatcher.class)) {
            onSpanAdded.onSpanAdded(this, what, start, end);
        }
    }

    private void sendSpanRemoved(Object what, int start, int end) {
        for (SpanWatcher onSpanRemoved : (SpanWatcher[]) getSpans(start, end, SpanWatcher.class)) {
            onSpanRemoved.onSpanRemoved(this, what, start, end);
        }
    }

    private void sendSpanChanged(Object what, int oldStart, int oldEnd, int start, int end) {
        for (SpanWatcher onSpanChanged : (SpanWatcher[]) getSpans(Math.min(oldStart, start), Math.min(Math.max(oldEnd, end), length()), SpanWatcher.class)) {
            onSpanChanged.onSpanChanged(this, what, oldStart, oldEnd, start, end);
        }
    }

    private static String region(int start, int end) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(start);
        stringBuilder.append(" ... ");
        stringBuilder.append(end);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private void checkRange(String operation, int start, int end) {
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        if (end >= start) {
            int len = length();
            StringBuilder stringBuilder;
            if (start > len || end > len) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(operation);
                stringBuilder.append(str);
                stringBuilder.append(region(start, end));
                stringBuilder.append(" ends beyond length ");
                stringBuilder.append(len);
                throw new IndexOutOfBoundsException(stringBuilder.toString());
            } else if (start < 0 || end < 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(operation);
                stringBuilder.append(str);
                stringBuilder.append(region(start, end));
                stringBuilder.append(" starts before 0");
                throw new IndexOutOfBoundsException(stringBuilder.toString());
            } else {
                return;
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(operation);
        stringBuilder2.append(str);
        stringBuilder2.append(region(start, end));
        stringBuilder2.append(" has end before start");
        throw new IndexOutOfBoundsException(stringBuilder2.toString());
    }

    public void drawText(BaseCanvas c, int start, int end, float x, float y, Paint p) {
        int i = start;
        int i2 = end;
        checkRange("drawText", i, i2);
        int i3 = this.mGapStart;
        if (i2 <= i3) {
            c.drawText(this.mText, start, i2 - i, x, y, p);
        } else if (i >= i3) {
            c.drawText(this.mText, i + this.mGapLength, i2 - i, x, y, p);
        } else {
            char[] buf = TextUtils.obtain(i2 - i);
            getChars(i, i2, buf, 0);
            c.drawText(buf, 0, i2 - i, x, y, p);
            TextUtils.recycle(buf);
        }
    }

    public void drawTextRun(BaseCanvas c, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, Paint p) {
        int i = start;
        int i2 = end;
        int i3 = contextStart;
        int i4 = contextEnd;
        checkRange("drawTextRun", i, i2);
        int contextLen = i4 - i3;
        int len = i2 - i;
        int i5 = this.mGapStart;
        char[] cArr;
        if (i4 <= i5) {
            c.drawTextRun(this.mText, start, len, contextStart, contextLen, x, y, isRtl, p);
        } else if (i3 >= i5) {
            cArr = this.mText;
            int i6 = this.mGapLength;
            c.drawTextRun(cArr, i + i6, len, i3 + i6, contextLen, x, y, isRtl, p);
        } else {
            cArr = TextUtils.obtain(contextLen);
            getChars(i3, i4, cArr, 0);
            c.drawTextRun(cArr, i - i3, len, 0, contextLen, x, y, isRtl, p);
            TextUtils.recycle(cArr);
        }
    }

    public float measureText(int start, int end, Paint p) {
        checkRange("measureText", start, end);
        int ret = this.mGapStart;
        if (end <= ret) {
            return p.measureText(this.mText, start, end - start);
        }
        if (start >= ret) {
            return p.measureText(this.mText, this.mGapLength + start, end - start);
        }
        char[] buf = TextUtils.obtain(end - start);
        getChars(start, end, buf, 0);
        float ret2 = p.measureText(buf, 0, end - start);
        TextUtils.recycle(buf);
        return ret2;
    }

    public int getTextWidths(int start, int end, float[] widths, Paint p) {
        checkRange("getTextWidths", start, end);
        int ret = this.mGapStart;
        if (end <= ret) {
            return p.getTextWidths(this.mText, start, end - start, widths);
        }
        if (start >= ret) {
            return p.getTextWidths(this.mText, this.mGapLength + start, end - start, widths);
        }
        char[] buf = TextUtils.obtain(end - start);
        getChars(start, end, buf, 0);
        int ret2 = p.getTextWidths(buf, 0, end - start, widths);
        TextUtils.recycle(buf);
        return ret2;
    }

    public float getTextRunAdvances(int start, int end, int contextStart, int contextEnd, boolean isRtl, float[] advances, int advancesPos, Paint p) {
        int i = start;
        int i2 = end;
        int i3 = contextStart;
        int i4 = contextEnd;
        int contextLen = i4 - i3;
        int len = i2 - i;
        int ret = this.mGapStart;
        if (i2 <= ret) {
            return p.getTextRunAdvances(this.mText, start, len, contextStart, contextLen, isRtl, advances, advancesPos);
        } else if (i >= ret) {
            char[] cArr = this.mText;
            ret = this.mGapLength;
            return p.getTextRunAdvances(cArr, i + ret, len, i3 + ret, contextLen, isRtl, advances, advancesPos);
        } else {
            char[] buf = TextUtils.obtain(contextLen);
            getChars(i3, i4, buf, 0);
            char[] buf2 = buf;
            float ret2 = p.getTextRunAdvances(buf, i - i3, len, 0, contextLen, isRtl, advances, advancesPos);
            TextUtils.recycle(buf2);
            return ret2;
        }
    }

    @Deprecated
    public int getTextRunCursor(int contextStart, int contextEnd, int dir, int offset, int cursorOpt, Paint p) {
        boolean z = true;
        if (dir != 1) {
            z = false;
        }
        return getTextRunCursor(contextStart, contextEnd, z, offset, cursorOpt, p);
    }

    public int getTextRunCursor(int contextStart, int contextEnd, boolean isRtl, int offset, int cursorOpt, Paint p) {
        int contextLen = contextEnd - contextStart;
        int ret = this.mGapStart;
        if (contextEnd <= ret) {
            return p.getTextRunCursor(this.mText, contextStart, contextLen, isRtl, offset, cursorOpt);
        } else if (contextStart >= ret) {
            char[] cArr = this.mText;
            ret = this.mGapLength;
            return p.getTextRunCursor(cArr, contextStart + ret, contextLen, isRtl, offset + ret, cursorOpt) - this.mGapLength;
        } else {
            char[] buf = TextUtils.obtain(contextLen);
            getChars(contextStart, contextEnd, buf, 0);
            ret = p.getTextRunCursor(buf, 0, contextLen, isRtl, offset - contextStart, cursorOpt) + contextStart;
            TextUtils.recycle(buf);
            return ret;
        }
    }

    public void setFilters(InputFilter[] filters) {
        if (filters != null) {
            this.mFilters = filters;
            return;
        }
        throw new IllegalArgumentException();
    }

    public InputFilter[] getFilters() {
        return this.mFilters;
    }

    public boolean equals(Object o) {
        if ((o instanceof Spanned) && toString().equals(o.toString())) {
            Spanned other = (Spanned) o;
            Object[] otherSpans = other.getSpans(0, other.length(), Object.class);
            Object[] thisSpans = getSpans(0, length(), Object.class);
            if (this.mSpanCount == otherSpans.length) {
                for (int i = 0; i < this.mSpanCount; i++) {
                    SpannableStringBuilder thisSpan = thisSpans[i];
                    Spanned otherSpan = otherSpans[i];
                    if (thisSpan == this) {
                        if (other != otherSpan || getSpanStart(thisSpan) != other.getSpanStart(otherSpan) || getSpanEnd(thisSpan) != other.getSpanEnd(otherSpan) || getSpanFlags(thisSpan) != other.getSpanFlags(otherSpan)) {
                            return false;
                        }
                    } else if (!thisSpan.equals(otherSpan) || getSpanStart(thisSpan) != other.getSpanStart(otherSpan) || getSpanEnd(thisSpan) != other.getSpanEnd(otherSpan) || getSpanFlags(thisSpan) != other.getSpanFlags(otherSpan)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int hash = (toString().hashCode() * 31) + this.mSpanCount;
        for (int i = 0; i < this.mSpanCount; i++) {
            SpannableStringBuilder span = this.mSpans[i];
            if (span != this) {
                hash = (hash * 31) + span.hashCode();
            }
            hash = (((((hash * 31) + getSpanStart(span)) * 31) + getSpanEnd(span)) * 31) + getSpanFlags(span);
        }
        return hash;
    }

    private int treeRoot() {
        return Integer.highestOneBit(this.mSpanCount) - 1;
    }

    private static int leftChild(int i) {
        return i - (((i + 1) & (~i)) >> 1);
    }

    private static int rightChild(int i) {
        return (((i + 1) & (~i)) >> 1) + i;
    }

    private int calcMax(int i) {
        int max = 0;
        if ((i & 1) != 0) {
            max = calcMax(leftChild(i));
        }
        if (i < this.mSpanCount) {
            max = Math.max(max, this.mSpanEnds[i]);
            if ((i & 1) != 0) {
                max = Math.max(max, calcMax(rightChild(i)));
            }
        }
        this.mSpanMax[i] = max;
        return max;
    }

    private void restoreInvariants() {
        if (this.mSpanCount != 0) {
            int i;
            for (i = 1; i < this.mSpanCount; i++) {
                int start = this.mSpanStarts;
                if (start[i] < start[i - 1]) {
                    Object span = this.mSpans[i];
                    start = start[i];
                    int end = this.mSpanEnds[i];
                    int flags = this.mSpanFlags[i];
                    int insertionOrder = this.mSpanOrder[i];
                    int j = i;
                    int[] iArr;
                    do {
                        Object[] objArr = this.mSpans;
                        objArr[j] = objArr[j - 1];
                        iArr = this.mSpanStarts;
                        iArr[j] = iArr[j - 1];
                        int[] iArr2 = this.mSpanEnds;
                        iArr2[j] = iArr2[j - 1];
                        iArr2 = this.mSpanFlags;
                        iArr2[j] = iArr2[j - 1];
                        iArr2 = this.mSpanOrder;
                        iArr2[j] = iArr2[j - 1];
                        j--;
                        if (j <= 0) {
                            break;
                        }
                    } while (start < iArr[j - 1]);
                    this.mSpans[j] = span;
                    this.mSpanStarts[j] = start;
                    this.mSpanEnds[j] = end;
                    this.mSpanFlags[j] = flags;
                    this.mSpanOrder[j] = insertionOrder;
                    invalidateIndex(j);
                }
            }
            calcMax(treeRoot());
            if (this.mIndexOfSpan == null) {
                this.mIndexOfSpan = new IdentityHashMap();
            }
            i = this.mLowWaterMark;
            while (i < this.mSpanCount) {
                Integer existing = (Integer) this.mIndexOfSpan.get(this.mSpans[i]);
                if (existing == null || existing.intValue() != i) {
                    this.mIndexOfSpan.put(this.mSpans[i], Integer.valueOf(i));
                }
                i++;
            }
            this.mLowWaterMark = Integer.MAX_VALUE;
        }
    }

    private void invalidateIndex(int i) {
        this.mLowWaterMark = Math.min(i, this.mLowWaterMark);
    }
}
