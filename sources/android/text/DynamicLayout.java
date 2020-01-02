package android.text;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.text.Layout.Alignment;
import android.text.Layout.Directions;
import android.text.TextUtils.TruncateAt;
import android.text.style.ReplacementSpan;
import android.text.style.UpdateLayout;
import android.text.style.WrapTogetherSpan;
import android.util.ArraySet;
import android.util.Pools.SynchronizedPool;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.lang.ref.WeakReference;

public class DynamicLayout extends Layout {
    private static final int BLOCK_MINIMUM_CHARACTER_LENGTH = 400;
    private static final int COLUMNS_ELLIPSIZE = 7;
    private static final int COLUMNS_NORMAL = 5;
    private static final int DESCENT = 2;
    private static final int DIR = 0;
    private static final int DIR_SHIFT = 30;
    private static final int ELLIPSIS_COUNT = 6;
    private static final int ELLIPSIS_START = 5;
    private static final int ELLIPSIS_UNDEFINED = Integer.MIN_VALUE;
    private static final int EXTRA = 3;
    private static final int HYPHEN = 4;
    private static final int HYPHEN_MASK = 255;
    public static final int INVALID_BLOCK_INDEX = -1;
    private static final int MAY_PROTRUDE_FROM_TOP_OR_BOTTOM = 4;
    private static final int MAY_PROTRUDE_FROM_TOP_OR_BOTTOM_MASK = 256;
    private static final int PRIORITY = 128;
    private static final int START = 0;
    private static final int START_MASK = 536870911;
    private static final int TAB = 0;
    private static final int TAB_MASK = 536870912;
    private static final int TOP = 1;
    private static android.text.StaticLayout.Builder sBuilder = null;
    private static final Object[] sLock = new Object[0];
    @UnsupportedAppUsage
    private static StaticLayout sStaticLayout = null;
    private CharSequence mBase;
    private int[] mBlockEndLines;
    private int[] mBlockIndices;
    private ArraySet<Integer> mBlocksAlwaysNeedToBeRedrawn;
    private int mBottomPadding;
    private int mBreakStrategy;
    private CharSequence mDisplay;
    private boolean mEllipsize;
    private TruncateAt mEllipsizeAt;
    private int mEllipsizedWidth;
    private boolean mFallbackLineSpacing;
    private int mHyphenationFrequency;
    private boolean mIncludePad;
    private int mIndexFirstChangedBlock;
    private PackedIntVector mInts;
    private int mJustificationMode;
    private int mNumberOfBlocks;
    private PackedObjectVector<Directions> mObjects;
    private Rect mTempRect;
    private int mTopPadding;
    private ChangeWatcher mWatcher;

    public static final class Builder {
        private static final SynchronizedPool<Builder> sPool = new SynchronizedPool(3);
        private Alignment mAlignment;
        private CharSequence mBase;
        private int mBreakStrategy;
        private CharSequence mDisplay;
        private TruncateAt mEllipsize;
        private int mEllipsizedWidth;
        private boolean mFallbackLineSpacing;
        private final FontMetricsInt mFontMetricsInt = new FontMetricsInt();
        private int mHyphenationFrequency;
        private boolean mIncludePad;
        private int mJustificationMode;
        private TextPaint mPaint;
        private float mSpacingAdd;
        private float mSpacingMult;
        private TextDirectionHeuristic mTextDir;
        private int mWidth;

        private Builder() {
        }

        public static Builder obtain(CharSequence base, TextPaint paint, int width) {
            Builder b = (Builder) sPool.acquire();
            if (b == null) {
                b = new Builder();
            }
            b.mBase = base;
            b.mDisplay = base;
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
            b.mBreakStrategy = 0;
            b.mHyphenationFrequency = 0;
            b.mJustificationMode = 0;
            return b;
        }

        private static void recycle(Builder b) {
            b.mBase = null;
            b.mDisplay = null;
            b.mPaint = null;
            sPool.release(b);
        }

        public Builder setDisplayText(CharSequence display) {
            this.mDisplay = display;
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

        public Builder setBreakStrategy(int breakStrategy) {
            this.mBreakStrategy = breakStrategy;
            return this;
        }

        public Builder setHyphenationFrequency(int hyphenationFrequency) {
            this.mHyphenationFrequency = hyphenationFrequency;
            return this;
        }

        public Builder setJustificationMode(int justificationMode) {
            this.mJustificationMode = justificationMode;
            return this;
        }

        public DynamicLayout build() {
            DynamicLayout result = new DynamicLayout(this);
            recycle(this);
            return result;
        }
    }

    private static class ChangeWatcher implements TextWatcher, SpanWatcher {
        private WeakReference<DynamicLayout> mLayout;

        public ChangeWatcher(DynamicLayout layout) {
            this.mLayout = new WeakReference(layout);
        }

        private void reflow(CharSequence s, int where, int before, int after) {
            DynamicLayout ml = (DynamicLayout) this.mLayout.get();
            if (ml != null) {
                ml.reflow(s, where, before, after);
            } else if (s instanceof Spannable) {
                ((Spannable) s).removeSpan(this);
            }
        }

        public void beforeTextChanged(CharSequence s, int where, int before, int after) {
        }

        public void onTextChanged(CharSequence s, int where, int before, int after) {
            reflow(s, where, before, after);
        }

        public void afterTextChanged(Editable s) {
        }

        public void onSpanAdded(Spannable s, Object o, int start, int end) {
            if (o instanceof UpdateLayout) {
                reflow(s, start, end - start, end - start);
            }
        }

        public void onSpanRemoved(Spannable s, Object o, int start, int end) {
            if (o instanceof UpdateLayout) {
                reflow(s, start, end - start, end - start);
            }
        }

        public void onSpanChanged(Spannable s, Object o, int start, int end, int nstart, int nend) {
            if (o instanceof UpdateLayout) {
                if (start > end) {
                    start = 0;
                }
                reflow(s, start, end - start, end - start);
                reflow(s, nstart, nend - nstart, nend - nstart);
            }
        }
    }

    @Deprecated
    public DynamicLayout(CharSequence base, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(base, base, paint, width, align, spacingmult, spacingadd, includepad);
    }

    @Deprecated
    public DynamicLayout(CharSequence base, CharSequence display, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(base, display, paint, width, align, spacingmult, spacingadd, includepad, null, 0);
    }

    @Deprecated
    public DynamicLayout(CharSequence base, CharSequence display, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, boolean includepad, TruncateAt ellipsize, int ellipsizedWidth) {
        this(base, display, paint, width, align, TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingmult, spacingadd, includepad, 0, 0, 0, ellipsize, ellipsizedWidth);
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public DynamicLayout(CharSequence base, CharSequence display, TextPaint paint, int width, Alignment align, TextDirectionHeuristic textDir, float spacingmult, float spacingadd, boolean includepad, int breakStrategy, int hyphenationFrequency, int justificationMode, TruncateAt ellipsize, int ellipsizedWidth) {
        CharSequence charSequence = display;
        TruncateAt truncateAt = ellipsize;
        Alignment alignment = align;
        TextDirectionHeuristic textDirectionHeuristic = textDir;
        float f = spacingmult;
        float f2 = spacingadd;
        super(createEllipsizer(truncateAt, charSequence), paint, width, alignment, textDirectionHeuristic, f, f2);
        this.mTempRect = new Rect();
        Builder b = Builder.obtain(base, paint, width).setAlignment(alignment).setTextDirection(textDirectionHeuristic).setLineSpacing(f2, f).setEllipsizedWidth(ellipsizedWidth).setEllipsize(truncateAt);
        this.mDisplay = charSequence;
        this.mIncludePad = includepad;
        this.mBreakStrategy = breakStrategy;
        this.mJustificationMode = justificationMode;
        this.mHyphenationFrequency = hyphenationFrequency;
        generate(b);
        Builder.recycle(b);
    }

    private DynamicLayout(Builder b) {
        super(createEllipsizer(b.mEllipsize, b.mDisplay), b.mPaint, b.mWidth, b.mAlignment, b.mTextDir, b.mSpacingMult, b.mSpacingAdd);
        this.mTempRect = new Rect();
        this.mDisplay = b.mDisplay;
        this.mIncludePad = b.mIncludePad;
        this.mBreakStrategy = b.mBreakStrategy;
        this.mJustificationMode = b.mJustificationMode;
        this.mHyphenationFrequency = b.mHyphenationFrequency;
        generate(b);
    }

    private static CharSequence createEllipsizer(TruncateAt ellipsize, CharSequence display) {
        if (ellipsize == null) {
            return display;
        }
        if (display instanceof Spanned) {
            return new SpannedEllipsizer(display);
        }
        return new Ellipsizer(display);
    }

    private void generate(Builder b) {
        int[] start;
        this.mBase = b.mBase;
        this.mFallbackLineSpacing = b.mFallbackLineSpacing;
        if (b.mEllipsize != null) {
            this.mInts = new PackedIntVector(7);
            this.mEllipsizedWidth = b.mEllipsizedWidth;
            this.mEllipsizeAt = b.mEllipsize;
            Ellipsizer e = (Ellipsizer) getText();
            e.mLayout = this;
            e.mWidth = b.mEllipsizedWidth;
            e.mMethod = b.mEllipsize;
            this.mEllipsize = true;
        } else {
            this.mInts = new PackedIntVector(5);
            this.mEllipsizedWidth = b.mWidth;
            this.mEllipsizeAt = null;
        }
        this.mObjects = new PackedObjectVector(1);
        if (b.mEllipsize != null) {
            start = new int[7];
            start[5] = Integer.MIN_VALUE;
        } else {
            start = new int[5];
        }
        Directions[] dirs = new Directions[]{DIRS_ALL_LEFT_TO_RIGHT};
        FontMetricsInt fm = b.mFontMetricsInt;
        b.mPaint.getFontMetricsInt(fm);
        int asc = fm.ascent;
        int desc = fm.descent;
        start[0] = 1073741824;
        start[1] = 0;
        start[2] = desc;
        this.mInts.insertAt(0, start);
        start[1] = desc - asc;
        this.mInts.insertAt(1, start);
        this.mObjects.insertAt(0, dirs);
        int baseLength = this.mBase.length();
        reflow(this.mBase, 0, 0, baseLength);
        if (this.mBase instanceof Spannable) {
            if (this.mWatcher == null) {
                this.mWatcher = new ChangeWatcher(this);
            }
            Spannable sp = this.mBase;
            ChangeWatcher[] spans = (ChangeWatcher[]) sp.getSpans(0, baseLength, ChangeWatcher.class);
            for (Object removeSpan : spans) {
                sp.removeSpan(removeSpan);
            }
            sp.setSpan(this.mWatcher, 0, baseLength, 8388626);
        }
    }

    @VisibleForTesting(visibility = Visibility.PACKAGE)
    public void reflow(CharSequence s, int where, int before, int after) {
        if (s == this.mBase) {
            int find;
            int en;
            int diff;
            int where2;
            StaticLayout reflowed;
            android.text.StaticLayout.Builder b;
            int i;
            StaticLayout reflowed2;
            int botpad;
            int ht;
            int toppad;
            CharSequence text = this.mDisplay;
            int len = text.length();
            int find2 = TextUtils.lastIndexOf(text, 10, where - 1);
            if (find2 < 0) {
                find = 0;
            } else {
                find = find2 + 1;
            }
            find2 = where - find;
            int before2 = before + find2;
            int after2 = after + find2;
            find2 = where - find2;
            int look = TextUtils.indexOf(text, 10, find2 + after2);
            if (look < 0) {
                look = len;
            } else {
                look++;
            }
            int change = look - (find2 + after2);
            before2 += change;
            after2 += change;
            if (text instanceof Spanned) {
                Spanned sp = (Spanned) text;
                while (true) {
                    CharSequence charSequence;
                    boolean again = false;
                    Object[] force = sp.getSpans(find2, find2 + after2, WrapTogetherSpan.class);
                    int i2 = 0;
                    while (i2 < force.length) {
                        int st = sp.getSpanStart(force[i2]);
                        en = sp.getSpanEnd(force[i2]);
                        if (st < find2) {
                            again = true;
                            int diff2 = find2 - st;
                            before2 += diff2;
                            after2 += diff2;
                            find2 -= diff2;
                        }
                        if (en > find2 + after2) {
                            diff = en - (find2 + after2);
                            before2 += diff;
                            after2 += diff;
                            again = true;
                        }
                        i2++;
                        charSequence = s;
                    }
                    if (!again) {
                        break;
                    }
                    charSequence = s;
                }
                where2 = find2;
            } else {
                where2 = find2;
            }
            en = getLineForOffset(where2);
            int startv = getLineTop(en);
            find2 = getLineForOffset(where2 + before2);
            if (where2 + after2 == len) {
                diff = getLineCount();
            } else {
                diff = find2;
            }
            int endv = getLineTop(diff);
            boolean islast = diff == getLineCount();
            synchronized (sLock) {
                try {
                    reflowed = sStaticLayout;
                    b = sBuilder;
                    sStaticLayout = null;
                    sBuilder = null;
                } catch (Throwable th) {
                    where = where2;
                    int i3 = len;
                    after = look;
                    int i4 = find;
                    int i5 = before2;
                    i = after2;
                    before = change;
                    boolean z = islast;
                    while (true) {
                    }
                }
            }
            if (reflowed == null) {
                reflowed2 = new StaticLayout(null);
                look = android.text.StaticLayout.Builder.obtain(text, where2, where2 + after2, getPaint(), getWidth());
            } else {
                after = look;
                reflowed2 = reflowed;
                look = b;
            }
            look.setText(text, where2, where2 + after2).setPaint(getPaint()).setWidth(getWidth()).setTextDirection(getTextDirectionHeuristic()).setLineSpacing(getSpacingAdd(), getSpacingMultiplier()).setUseLineSpacingFromFallbacks(this.mFallbackLineSpacing).setEllipsizedWidth(this.mEllipsizedWidth).setEllipsize(this.mEllipsizeAt).setBreakStrategy(this.mBreakStrategy).setHyphenationFrequency(this.mHyphenationFrequency).setJustificationMode(this.mJustificationMode).setAddLastLineLineSpacing(!islast);
            reflowed2.generate(look, false, true);
            find2 = reflowed2.getLineCount();
            if (where2 + after2 == len || reflowed2.getLineStart(find2 - 1) != where2 + after2) {
                find = find2;
            } else {
                find = find2 - 1;
            }
            this.mInts.deleteAt(en, diff - en);
            this.mObjects.deleteAt(en, diff - en);
            find2 = reflowed2.getLineTop(find);
            change = 0;
            if (this.mIncludePad && en == 0) {
                change = reflowed2.getTopPadding();
                this.mTopPadding = change;
                find2 -= change;
            }
            if (this.mIncludePad && islast) {
                len = reflowed2.getBottomPadding();
                this.mBottomPadding = len;
                botpad = len;
                len = find2 + len;
            } else {
                len = find2;
                botpad = 0;
            }
            this.mInts.adjustValuesBelow(en, 0, after2 - before2);
            this.mInts.adjustValuesBelow(en, 1, (startv - endv) + len);
            if (this.mEllipsize) {
                int[] ints = new int[7];
                ints[5] = Integer.MIN_VALUE;
                islast = ints;
            } else {
                islast = new int[5];
            }
            before2 = new Directions[1];
            find2 = 0;
            while (find2 < find) {
                ht = len;
                len = reflowed2.getLineStart(find2);
                islast[0] = len;
                islast[0] = islast[0] | (reflowed2.getParagraphDirection(find2) << 30);
                islast[0] = islast[0] | (reflowed2.getLineContainsTab(find2) ? 536870912 : 0);
                int top = reflowed2.getLineTop(find2) + startv;
                if (find2 > 0) {
                    top -= change;
                }
                islast[1] = top;
                int desc = reflowed2.getLineDescent(find2);
                toppad = change;
                if (find2 == find - 1) {
                    desc += botpad;
                }
                islast[2] = desc;
                islast[3] = reflowed2.getLineExtra(find2);
                before2[0] = reflowed2.getLineDirections(find2);
                change = find2 == find + -1 ? where2 + after2 : reflowed2.getLineStart(find2 + 1);
                where = where2;
                i = after2;
                islast[4] = StaticLayout.packHyphenEdit(reflowed2.getStartHyphenEdit(find2), reflowed2.getEndHyphenEdit(find2));
                islast[4] = islast[4] | (contentMayProtrudeFromLineTopOrBottom(text, len, change) ? 256 : 0);
                if (this.mEllipsize) {
                    islast[5] = reflowed2.getEllipsisStart(find2);
                    islast[6] = reflowed2.getEllipsisCount(find2);
                }
                this.mInts.insertAt(en + find2, islast);
                this.mObjects.insertAt(en + find2, before2);
                find2++;
                where2 = where;
                len = ht;
                change = toppad;
                after2 = i;
            }
            ht = len;
            i = after2;
            toppad = change;
            updateBlocks(en, diff - 1, find);
            look.finish();
            synchronized (sLock) {
                sStaticLayout = reflowed2;
                sBuilder = look;
            }
        }
    }

    private boolean contentMayProtrudeFromLineTopOrBottom(CharSequence text, int start, int end) {
        boolean z = true;
        if ((text instanceof Spanned) && ((ReplacementSpan[]) ((Spanned) text).getSpans(start, end, ReplacementSpan.class)).length > 0) {
            return true;
        }
        Paint paint = getPaint();
        if (text instanceof PrecomputedText) {
            ((PrecomputedText) text).getBounds(start, end, this.mTempRect);
        } else {
            paint.getTextBounds(text, start, end, this.mTempRect);
        }
        FontMetricsInt fm = paint.getFontMetricsInt();
        if (this.mTempRect.top >= fm.top && this.mTempRect.bottom <= fm.bottom) {
            z = false;
        }
        return z;
    }

    private void createBlocks() {
        int offset = 400;
        this.mNumberOfBlocks = 0;
        CharSequence text = this.mDisplay;
        while (true) {
            offset = TextUtils.indexOf(text, 10, offset);
            if (offset < 0) {
                break;
            }
            addBlockAtOffset(offset);
            offset += 400;
        }
        addBlockAtOffset(text.length());
        this.mBlockIndices = new int[this.mBlockEndLines.length];
        for (int i = 0; i < this.mBlockEndLines.length; i++) {
            this.mBlockIndices[i] = -1;
        }
    }

    public ArraySet<Integer> getBlocksAlwaysNeedToBeRedrawn() {
        return this.mBlocksAlwaysNeedToBeRedrawn;
    }

    private void updateAlwaysNeedsToBeRedrawn(int blockIndex) {
        int startLine = blockIndex == 0 ? 0 : this.mBlockEndLines[blockIndex - 1] + 1;
        int endLine = this.mBlockEndLines[blockIndex];
        for (int i = startLine; i <= endLine; i++) {
            if (getContentMayProtrudeFromTopOrBottom(i)) {
                if (this.mBlocksAlwaysNeedToBeRedrawn == null) {
                    this.mBlocksAlwaysNeedToBeRedrawn = new ArraySet();
                }
                this.mBlocksAlwaysNeedToBeRedrawn.add(Integer.valueOf(blockIndex));
                return;
            }
        }
        ArraySet arraySet = this.mBlocksAlwaysNeedToBeRedrawn;
        if (arraySet != null) {
            arraySet.remove(Integer.valueOf(blockIndex));
        }
    }

    private void addBlockAtOffset(int offset) {
        int line = getLineForOffset(offset);
        int[] iArr = this.mBlockEndLines;
        int i;
        if (iArr == null) {
            this.mBlockEndLines = ArrayUtils.newUnpaddedIntArray(1);
            iArr = this.mBlockEndLines;
            i = this.mNumberOfBlocks;
            iArr[i] = line;
            updateAlwaysNeedsToBeRedrawn(i);
            this.mNumberOfBlocks++;
            return;
        }
        i = this.mNumberOfBlocks;
        if (line > iArr[i - 1]) {
            this.mBlockEndLines = GrowingArrayUtils.append(iArr, i, line);
            updateAlwaysNeedsToBeRedrawn(this.mNumberOfBlocks);
            this.mNumberOfBlocks++;
        }
    }

    @VisibleForTesting(visibility = Visibility.PACKAGE)
    public void updateBlocks(int startLine, int endLine, int newLineCount) {
        int i = startLine;
        int i2 = endLine;
        if (this.mBlockEndLines == null) {
            createBlocks();
            return;
        }
        int i3;
        int firstBlock = -1;
        int lastBlock = -1;
        for (i3 = 0; i3 < this.mNumberOfBlocks; i3++) {
            if (this.mBlockEndLines[i3] >= i) {
                firstBlock = i3;
                break;
            }
        }
        for (i3 = firstBlock; i3 < this.mNumberOfBlocks; i3++) {
            if (this.mBlockEndLines[i3] >= i2) {
                lastBlock = i3;
                break;
            }
        }
        int[] iArr = this.mBlockEndLines;
        int lastBlockEndLine = iArr[lastBlock];
        if (firstBlock == 0) {
            i3 = 0;
        } else {
            i3 = iArr[firstBlock - 1] + 1;
        }
        boolean createBlockBefore = i > i3;
        boolean createBlock = newLineCount > 0;
        boolean createBlockAfter = i2 < this.mBlockEndLines[lastBlock];
        int numAddedBlocks = 0;
        if (createBlockBefore) {
            numAddedBlocks = 0 + 1;
        }
        if (createBlock) {
            numAddedBlocks++;
        }
        if (createBlockAfter) {
            numAddedBlocks++;
        }
        int numRemovedBlocks = (lastBlock - firstBlock) + 1;
        int i4 = this.mNumberOfBlocks;
        int newNumberOfBlocks = (i4 + numAddedBlocks) - numRemovedBlocks;
        if (newNumberOfBlocks == 0) {
            this.mBlockEndLines[0] = 0;
            this.mBlockIndices[0] = -1;
            this.mNumberOfBlocks = 1;
            return;
        }
        int lastBlockEndLine2;
        boolean createBlockAfter2;
        int changedBlockCount;
        int i5;
        int[] iArr2 = this.mBlockEndLines;
        if (newNumberOfBlocks > iArr2.length) {
            int[] blockEndLines = ArrayUtils.newUnpaddedIntArray(Math.max(iArr2.length * 2, newNumberOfBlocks));
            int[] blockIndices = new int[blockEndLines.length];
            System.arraycopy(this.mBlockEndLines, 0, blockEndLines, 0, firstBlock);
            System.arraycopy(this.mBlockIndices, 0, blockIndices, 0, firstBlock);
            lastBlockEndLine2 = lastBlockEndLine;
            createBlockAfter2 = createBlockAfter;
            System.arraycopy(this.mBlockEndLines, lastBlock + 1, blockEndLines, firstBlock + numAddedBlocks, (this.mNumberOfBlocks - lastBlock) - 1);
            System.arraycopy(this.mBlockIndices, lastBlock + 1, blockIndices, firstBlock + numAddedBlocks, (this.mNumberOfBlocks - lastBlock) - 1);
            this.mBlockEndLines = blockEndLines;
            this.mBlockIndices = blockIndices;
        } else {
            lastBlockEndLine2 = lastBlockEndLine;
            createBlockAfter2 = createBlockAfter;
            if (numAddedBlocks + numRemovedBlocks != 0) {
                System.arraycopy(iArr2, lastBlock + 1, iArr2, firstBlock + numAddedBlocks, (i4 - lastBlock) - 1);
                int[] iArr3 = this.mBlockIndices;
                System.arraycopy(iArr3, lastBlock + 1, iArr3, firstBlock + numAddedBlocks, (this.mNumberOfBlocks - lastBlock) - 1);
            }
        }
        if (!(numAddedBlocks + numRemovedBlocks == 0 || this.mBlocksAlwaysNeedToBeRedrawn == null)) {
            ArraySet<Integer> set = new ArraySet();
            changedBlockCount = numAddedBlocks - numRemovedBlocks;
            for (i5 = 0; i5 < this.mBlocksAlwaysNeedToBeRedrawn.size(); i5++) {
                Integer block = (Integer) this.mBlocksAlwaysNeedToBeRedrawn.valueAt(i5);
                if (block.intValue() < firstBlock) {
                    set.add(block);
                }
                if (block.intValue() > lastBlock) {
                    set.add(Integer.valueOf(block.intValue() + changedBlockCount));
                }
            }
            this.mBlocksAlwaysNeedToBeRedrawn = set;
        }
        this.mNumberOfBlocks = newNumberOfBlocks;
        lastBlockEndLine = newLineCount - ((i2 - i) + 1);
        if (lastBlockEndLine != 0) {
            changedBlockCount = firstBlock + numAddedBlocks;
            for (i5 = changedBlockCount; i5 < this.mNumberOfBlocks; i5++) {
                int[] iArr4 = this.mBlockEndLines;
                iArr4[i5] = iArr4[i5] + lastBlockEndLine;
            }
        } else {
            changedBlockCount = this.mNumberOfBlocks;
        }
        this.mIndexFirstChangedBlock = Math.min(this.mIndexFirstChangedBlock, changedBlockCount);
        i5 = firstBlock;
        if (createBlockBefore) {
            this.mBlockEndLines[i5] = i - 1;
            updateAlwaysNeedsToBeRedrawn(i5);
            this.mBlockIndices[i5] = -1;
            i5++;
        }
        if (createBlock) {
            this.mBlockEndLines[i5] = (i + newLineCount) - 1;
            updateAlwaysNeedsToBeRedrawn(i5);
            this.mBlockIndices[i5] = -1;
            i5++;
        }
        if (createBlockAfter2) {
            this.mBlockEndLines[i5] = lastBlockEndLine2 + lastBlockEndLine;
            updateAlwaysNeedsToBeRedrawn(i5);
            this.mBlockIndices[i5] = -1;
        }
    }

    @VisibleForTesting(visibility = Visibility.PACKAGE)
    public void setBlocksDataForTest(int[] blockEndLines, int[] blockIndices, int numberOfBlocks, int totalLines) {
        this.mBlockEndLines = new int[blockEndLines.length];
        this.mBlockIndices = new int[blockIndices.length];
        System.arraycopy(blockEndLines, 0, this.mBlockEndLines, 0, blockEndLines.length);
        System.arraycopy(blockIndices, 0, this.mBlockIndices, 0, blockIndices.length);
        this.mNumberOfBlocks = numberOfBlocks;
        while (this.mInts.size() < totalLines) {
            PackedIntVector packedIntVector = this.mInts;
            packedIntVector.insertAt(packedIntVector.size(), new int[5]);
        }
    }

    @UnsupportedAppUsage
    public int[] getBlockEndLines() {
        return this.mBlockEndLines;
    }

    @UnsupportedAppUsage
    public int[] getBlockIndices() {
        return this.mBlockIndices;
    }

    public int getBlockIndex(int index) {
        return this.mBlockIndices[index];
    }

    public void setBlockIndex(int index, int blockIndex) {
        this.mBlockIndices[index] = blockIndex;
    }

    @UnsupportedAppUsage
    public int getNumberOfBlocks() {
        return this.mNumberOfBlocks;
    }

    @UnsupportedAppUsage
    public int getIndexFirstChangedBlock() {
        return this.mIndexFirstChangedBlock;
    }

    @UnsupportedAppUsage
    public void setIndexFirstChangedBlock(int i) {
        this.mIndexFirstChangedBlock = i;
    }

    public int getLineCount() {
        return this.mInts.size() - 1;
    }

    public int getLineTop(int line) {
        return this.mInts.getValue(line, 1);
    }

    public int getLineDescent(int line) {
        return this.mInts.getValue(line, 2);
    }

    public int getLineExtra(int line) {
        return this.mInts.getValue(line, 3);
    }

    public int getLineStart(int line) {
        return this.mInts.getValue(line, 0) & 536870911;
    }

    public boolean getLineContainsTab(int line) {
        return (this.mInts.getValue(line, 0) & 536870912) != 0;
    }

    public int getParagraphDirection(int line) {
        return this.mInts.getValue(line, 0) >> 30;
    }

    public final Directions getLineDirections(int line) {
        return (Directions) this.mObjects.getValue(line, 0);
    }

    public int getTopPadding() {
        return this.mTopPadding;
    }

    public int getBottomPadding() {
        return this.mBottomPadding;
    }

    public int getStartHyphenEdit(int line) {
        return StaticLayout.unpackStartHyphenEdit(this.mInts.getValue(line, 4) & 255);
    }

    public int getEndHyphenEdit(int line) {
        return StaticLayout.unpackEndHyphenEdit(this.mInts.getValue(line, 4) & 255);
    }

    private boolean getContentMayProtrudeFromTopOrBottom(int line) {
        return (this.mInts.getValue(line, 4) & 256) != 0;
    }

    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    public int getEllipsisStart(int line) {
        if (this.mEllipsizeAt == null) {
            return 0;
        }
        return this.mInts.getValue(line, 5);
    }

    public int getEllipsisCount(int line) {
        if (this.mEllipsizeAt == null) {
            return 0;
        }
        return this.mInts.getValue(line, 6);
    }
}
