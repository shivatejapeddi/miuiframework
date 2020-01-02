package android.text;

import android.annotation.UnsupportedAppUsage;
import android.net.wifi.WifiEnterpriseConfig;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.lang.reflect.Array;
import libcore.util.EmptyArray;

abstract class SpannableStringInternal {
    @UnsupportedAppUsage
    private static final int COLUMNS = 3;
    @UnsupportedAppUsage
    static final Object[] EMPTY = new Object[0];
    @UnsupportedAppUsage
    private static final int END = 1;
    @UnsupportedAppUsage
    private static final int FLAGS = 2;
    @UnsupportedAppUsage
    private static final int START = 0;
    @UnsupportedAppUsage
    private int mSpanCount;
    @UnsupportedAppUsage
    private int[] mSpanData;
    @UnsupportedAppUsage
    private Object[] mSpans;
    @UnsupportedAppUsage
    private String mText;

    SpannableStringInternal(CharSequence source, int start, int end, boolean ignoreNoCopySpan) {
        if (start == 0 && end == source.length()) {
            this.mText = source.toString();
        } else {
            this.mText = source.toString().substring(start, end);
        }
        this.mSpans = EmptyArray.OBJECT;
        this.mSpanData = EmptyArray.INT;
        if (!(source instanceof Spanned)) {
            return;
        }
        if (source instanceof SpannableStringInternal) {
            copySpans((SpannableStringInternal) source, start, end, ignoreNoCopySpan);
        } else {
            copySpans((Spanned) source, start, end, ignoreNoCopySpan);
        }
    }

    @UnsupportedAppUsage
    SpannableStringInternal(CharSequence source, int start, int end) {
        this(source, start, end, false);
    }

    private void copySpans(Spanned src, int start, int end, boolean ignoreNoCopySpan) {
        Object[] spans = src.getSpans(start, end, Object.class);
        int i = 0;
        while (i < spans.length) {
            if (!ignoreNoCopySpan || !(spans[i] instanceof NoCopySpan)) {
                int st = src.getSpanStart(spans[i]);
                int en = src.getSpanEnd(spans[i]);
                int fl = src.getSpanFlags(spans[i]);
                if (st < start) {
                    st = start;
                }
                if (en > end) {
                    en = end;
                }
                setSpan(spans[i], st - start, en - start, fl, false);
            }
            i++;
        }
    }

    private void copySpans(SpannableStringInternal src, int start, int end, boolean ignoreNoCopySpan) {
        int i;
        SpannableStringInternal spannableStringInternal = src;
        int i2 = start;
        int i3 = end;
        int count = 0;
        int[] srcData = spannableStringInternal.mSpanData;
        Object[] srcSpans = spannableStringInternal.mSpans;
        int limit = spannableStringInternal.mSpanCount;
        boolean hasNoCopySpan = false;
        for (i = 0; i < limit; i++) {
            if (!isOutOfCopyRange(i2, i3, srcData[(i * 3) + 0], srcData[(i * 3) + 1])) {
                if (srcSpans[i] instanceof NoCopySpan) {
                    hasNoCopySpan = true;
                    if (ignoreNoCopySpan) {
                    }
                }
                count++;
            }
        }
        if (count != 0) {
            if (!hasNoCopySpan && i2 == 0 && i3 == src.length()) {
                this.mSpans = ArrayUtils.newUnpaddedObjectArray(spannableStringInternal.mSpans.length);
                this.mSpanData = new int[spannableStringInternal.mSpanData.length];
                this.mSpanCount = spannableStringInternal.mSpanCount;
                Object[] objArr = spannableStringInternal.mSpans;
                System.arraycopy(objArr, 0, this.mSpans, 0, objArr.length);
                int[] iArr = spannableStringInternal.mSpanData;
                int[] iArr2 = this.mSpanData;
                System.arraycopy(iArr, 0, iArr2, 0, iArr2.length);
            } else {
                this.mSpanCount = count;
                this.mSpans = ArrayUtils.newUnpaddedObjectArray(this.mSpanCount);
                this.mSpanData = new int[(this.mSpans.length * 3)];
                i = 0;
                int j = 0;
                while (i < limit) {
                    int spanStart = srcData[(i * 3) + 0];
                    int spanEnd = srcData[(i * 3) + 1];
                    if (!(isOutOfCopyRange(i2, i3, spanStart, spanEnd) || (ignoreNoCopySpan && (srcSpans[i] instanceof NoCopySpan)))) {
                        if (spanStart < i2) {
                            spanStart = start;
                        }
                        if (spanEnd > i3) {
                            spanEnd = end;
                        }
                        this.mSpans[j] = srcSpans[i];
                        int[] iArr3 = this.mSpanData;
                        iArr3[(j * 3) + 0] = spanStart - i2;
                        iArr3[(j * 3) + 1] = spanEnd - i2;
                        iArr3[(j * 3) + 2] = srcData[(i * 3) + 2];
                        j++;
                    }
                    i++;
                }
            }
        }
    }

    @UnsupportedAppUsage
    private final boolean isOutOfCopyRange(int start, int end, int spanStart, int spanEnd) {
        if (spanStart > end || spanEnd < start) {
            return true;
        }
        if (spanStart == spanEnd || start == end || (spanStart != end && spanEnd != start)) {
            return false;
        }
        return true;
    }

    public final int length() {
        return this.mText.length();
    }

    public final char charAt(int i) {
        return this.mText.charAt(i);
    }

    public final String toString() {
        return this.mText;
    }

    public final void getChars(int start, int end, char[] dest, int off) {
        this.mText.getChars(start, end, dest, off);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void setSpan(Object what, int start, int end, int flags) {
        setSpan(what, start, end, flags, true);
    }

    @UnsupportedAppUsage
    private boolean isIndexFollowsNextLine(int index) {
        return (index == 0 || index == length() || charAt(index - 1) == 10) ? false : true;
    }

    @UnsupportedAppUsage
    private void setSpan(Object what, int start, int end, int flags, boolean enforceParagraph) {
        Object[] newtags;
        Object obj = what;
        int i = start;
        int i2 = end;
        int nstart = start;
        int nend = end;
        checkRange("setSpan", i, i2);
        if ((flags & 51) == 51) {
            String str = ")";
            String str2 = " follows ";
            StringBuilder stringBuilder;
            if (isIndexFollowsNextLine(i)) {
                if (enforceParagraph) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("PARAGRAPH span must start at paragraph boundary (");
                    stringBuilder.append(i);
                    stringBuilder.append(str2);
                    stringBuilder.append(charAt(i - 1));
                    stringBuilder.append(str);
                    throw new RuntimeException(stringBuilder.toString());
                }
                return;
            } else if (isIndexFollowsNextLine(i2)) {
                if (enforceParagraph) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("PARAGRAPH span must end at paragraph boundary (");
                    stringBuilder.append(i2);
                    stringBuilder.append(str2);
                    stringBuilder.append(charAt(i2 - 1));
                    stringBuilder.append(str);
                    throw new RuntimeException(stringBuilder.toString());
                }
                return;
            }
        }
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        for (int i3 = 0; i3 < count; i3++) {
            if (spans[i3] == obj) {
                int ostart = data[(i3 * 3) + 0];
                int oend = data[(i3 * 3) + 1];
                data[(i3 * 3) + 0] = i;
                data[(i3 * 3) + 1] = i2;
                data[(i3 * 3) + 2] = flags;
                sendSpanChanged(what, ostart, oend, nstart, nend);
                return;
            }
        }
        int i4 = this.mSpanCount;
        if (i4 + 1 >= this.mSpans.length) {
            newtags = ArrayUtils.newUnpaddedObjectArray(GrowingArrayUtils.growSize(i4));
            int[] newdata = new int[(newtags.length * 3)];
            System.arraycopy(this.mSpans, 0, newtags, 0, this.mSpanCount);
            System.arraycopy(this.mSpanData, 0, newdata, 0, this.mSpanCount * 3);
            this.mSpans = newtags;
            this.mSpanData = newdata;
        }
        newtags = this.mSpans;
        int i5 = this.mSpanCount;
        newtags[i5] = obj;
        int[] iArr = this.mSpanData;
        iArr[(i5 * 3) + 0] = i;
        iArr[(i5 * 3) + 1] = i2;
        iArr[(i5 * 3) + 2] = flags;
        this.mSpanCount = i5 + 1;
        if (this instanceof Spannable) {
            sendSpanAdded(obj, nstart, nend);
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void removeSpan(Object what) {
        removeSpan(what, 0);
    }

    public void removeSpan(Object what, int flags) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                int ostart = data[(i * 3) + 0];
                int oend = data[(i * 3) + 1];
                int c = count - (i + 1);
                System.arraycopy(spans, i + 1, spans, i, c);
                System.arraycopy(data, (i + 1) * 3, data, i * 3, c * 3);
                this.mSpanCount--;
                if ((flags & 512) == 0) {
                    sendSpanRemoved(what, ostart, oend);
                }
                return;
            }
        }
    }

    @UnsupportedAppUsage
    public int getSpanStart(Object what) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                return data[(i * 3) + 0];
            }
        }
        return -1;
    }

    @UnsupportedAppUsage
    public int getSpanEnd(Object what) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                return data[(i * 3) + 1];
            }
        }
        return -1;
    }

    @UnsupportedAppUsage
    public int getSpanFlags(Object what) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                return data[(i * 3) + 2];
            }
        }
        return 0;
    }

    @UnsupportedAppUsage
    public <T> T[] getSpans(int queryStart, int queryEnd, Class<T> kind) {
        int i = queryStart;
        int i2 = queryEnd;
        Class<T> cls = kind;
        int count = 0;
        int spanCount = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        Object[] ret = null;
        Object ret1 = null;
        int i3 = 0;
        while (i3 < spanCount) {
            int spanStart = data[(i3 * 3) + 0];
            int spanEnd = data[(i3 * 3) + 1];
            if (spanStart <= i2 && spanEnd >= i && ((spanStart == spanEnd || i == i2 || !(spanStart == i2 || spanEnd == i)) && (cls == null || cls == Object.class || cls.isInstance(spans[i3])))) {
                if (count == 0) {
                    ret1 = spans[i3];
                    count++;
                } else {
                    if (count == 1) {
                        ret = (Object[]) Array.newInstance(cls, (spanCount - i3) + 1);
                        ret[0] = ret1;
                    }
                    int prio = data[(i3 * 3) + 2] & Spanned.SPAN_PRIORITY;
                    if (prio != 0) {
                        int j = 0;
                        while (j < count && prio <= (getSpanFlags(ret[j]) & Spanned.SPAN_PRIORITY)) {
                            j++;
                            i = queryStart;
                        }
                        System.arraycopy(ret, j, ret, j + 1, count - j);
                        ret[j] = spans[i3];
                        count++;
                    } else {
                        i = count + 1;
                        ret[count] = spans[i3];
                        count = i;
                    }
                }
            }
            i3++;
            i = queryStart;
        }
        if (count == 0) {
            return ArrayUtils.emptyArray(kind);
        }
        Object[] ret2;
        if (count == 1) {
            ret2 = (Object[]) Array.newInstance(cls, 1);
            ret2[0] = ret1;
            return ret2;
        } else if (count == ret.length) {
            return ret;
        } else {
            ret2 = (Object[]) Array.newInstance(cls, count);
            System.arraycopy(ret, 0, ret2, 0, count);
            return ret2;
        }
    }

    @UnsupportedAppUsage
    public int nextSpanTransition(int start, int limit, Class kind) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        if (kind == null) {
            kind = Object.class;
        }
        int i = 0;
        while (i < count) {
            int st = data[(i * 3) + 0];
            int en = data[(i * 3) + 1];
            if (st > start && st < limit && kind.isInstance(spans[i])) {
                limit = st;
            }
            if (en > start && en < limit && kind.isInstance(spans[i])) {
                limit = en;
            }
            i++;
        }
        return limit;
    }

    @UnsupportedAppUsage
    private void sendSpanAdded(Object what, int start, int end) {
        for (SpanWatcher onSpanAdded : (SpanWatcher[]) getSpans(start, end, SpanWatcher.class)) {
            onSpanAdded.onSpanAdded((Spannable) this, what, start, end);
        }
    }

    @UnsupportedAppUsage
    private void sendSpanRemoved(Object what, int start, int end) {
        for (SpanWatcher onSpanRemoved : (SpanWatcher[]) getSpans(start, end, SpanWatcher.class)) {
            onSpanRemoved.onSpanRemoved((Spannable) this, what, start, end);
        }
    }

    @UnsupportedAppUsage
    private void sendSpanChanged(Object what, int s, int e, int st, int en) {
        for (SpanWatcher onSpanChanged : (SpanWatcher[]) getSpans(Math.min(s, st), Math.max(e, en), SpanWatcher.class)) {
            onSpanChanged.onSpanChanged((Spannable) this, what, s, e, st, en);
        }
    }

    @UnsupportedAppUsage
    private static String region(int start, int end) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(start);
        stringBuilder.append(" ... ");
        stringBuilder.append(end);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @UnsupportedAppUsage
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

    public boolean equals(Object o) {
        if ((o instanceof Spanned) && toString().equals(o.toString())) {
            Spanned other = (Spanned) o;
            Object[] otherSpans = other.getSpans(0, other.length(), Object.class);
            Object[] thisSpans = getSpans(0, length(), Object.class);
            if (this.mSpanCount == otherSpans.length) {
                for (int i = 0; i < this.mSpanCount; i++) {
                    SpannableStringInternal thisSpan = thisSpans[i];
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
            SpannableStringInternal span = this.mSpans[i];
            if (span != this) {
                hash = (hash * 31) + span.hashCode();
            }
            hash = (((((hash * 31) + getSpanStart(span)) * 31) + getSpanEnd(span)) * 31) + getSpanFlags(span);
        }
        return hash;
    }

    @UnsupportedAppUsage
    private void copySpans(Spanned src, int start, int end) {
        copySpans(src, start, end, false);
    }

    @UnsupportedAppUsage
    private void copySpans(SpannableStringInternal src, int start, int end) {
        copySpans(src, start, end, false);
    }
}
