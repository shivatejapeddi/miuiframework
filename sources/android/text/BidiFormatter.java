package android.text;

import android.text.format.DateFormat;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Locale;

public final class BidiFormatter {
    private static final int DEFAULT_FLAGS = 2;
    private static final BidiFormatter DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
    private static final BidiFormatter DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
    private static TextDirectionHeuristic DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristics.FIRSTSTRONG_LTR;
    private static final int DIR_LTR = -1;
    private static final int DIR_RTL = 1;
    private static final int DIR_UNKNOWN = 0;
    private static final String EMPTY_STRING = "";
    private static final int FLAG_STEREO_RESET = 2;
    private static final char LRE = '‪';
    private static final char LRM = '‎';
    private static final String LRM_STRING = Character.toString(LRM);
    private static final char PDF = '‬';
    private static final char RLE = '‫';
    private static final char RLM = '‏';
    private static final String RLM_STRING = Character.toString(RLM);
    private final TextDirectionHeuristic mDefaultTextDirectionHeuristic;
    private final int mFlags;
    private final boolean mIsRtlContext;

    public static final class Builder {
        private int mFlags;
        private boolean mIsRtlContext;
        private TextDirectionHeuristic mTextDirectionHeuristic;

        public Builder() {
            initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
        }

        public Builder(boolean rtlContext) {
            initialize(rtlContext);
        }

        public Builder(Locale locale) {
            initialize(BidiFormatter.isRtlLocale(locale));
        }

        private void initialize(boolean isRtlContext) {
            this.mIsRtlContext = isRtlContext;
            this.mTextDirectionHeuristic = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
            this.mFlags = 2;
        }

        public Builder stereoReset(boolean stereoReset) {
            if (stereoReset) {
                this.mFlags |= 2;
            } else {
                this.mFlags &= -3;
            }
            return this;
        }

        public Builder setTextDirectionHeuristic(TextDirectionHeuristic heuristic) {
            this.mTextDirectionHeuristic = heuristic;
            return this;
        }

        public BidiFormatter build() {
            if (this.mFlags == 2 && this.mTextDirectionHeuristic == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) {
                return BidiFormatter.getDefaultInstanceFromContext(this.mIsRtlContext);
            }
            return new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristic);
        }
    }

    @VisibleForTesting
    public static class DirectionalityEstimator {
        private static final byte[] DIR_TYPE_CACHE = new byte[1792];
        private static final int DIR_TYPE_CACHE_SIZE = 1792;
        private int charIndex;
        private final boolean isHtml;
        private char lastChar;
        private final int length;
        private final CharSequence text;

        static {
            for (int i = 0; i < 1792; i++) {
                DIR_TYPE_CACHE[i] = Character.getDirectionality(i);
            }
        }

        public static byte getDirectionality(int codePoint) {
            if (Emoji.isNewEmoji(codePoint)) {
                return (byte) 13;
            }
            return Character.getDirectionality(codePoint);
        }

        DirectionalityEstimator(CharSequence text, boolean isHtml) {
            this.text = text;
            this.isHtml = isHtml;
            this.length = text.length();
        }

        /* Access modifiers changed, original: 0000 */
        public int getEntryDir() {
            this.charIndex = 0;
            int embeddingLevel = 0;
            int embeddingLevelDir = 0;
            int firstNonEmptyEmbeddingLevel = 0;
            while (this.charIndex < this.length && firstNonEmptyEmbeddingLevel == 0) {
                byte dirTypeForward = dirTypeForward();
                if (dirTypeForward != (byte) 0) {
                    if (dirTypeForward == (byte) 1 || dirTypeForward == (byte) 2) {
                        if (embeddingLevel == 0) {
                            return 1;
                        }
                        firstNonEmptyEmbeddingLevel = embeddingLevel;
                    } else if (dirTypeForward != (byte) 9) {
                        switch (dirTypeForward) {
                            case (byte) 14:
                            case (byte) 15:
                                embeddingLevel++;
                                embeddingLevelDir = -1;
                                break;
                            case (byte) 16:
                            case (byte) 17:
                                embeddingLevel++;
                                embeddingLevelDir = 1;
                                break;
                            case (byte) 18:
                                embeddingLevel--;
                                embeddingLevelDir = 0;
                                break;
                            default:
                                firstNonEmptyEmbeddingLevel = embeddingLevel;
                                break;
                        }
                    }
                } else if (embeddingLevel == 0) {
                    return -1;
                } else {
                    firstNonEmptyEmbeddingLevel = embeddingLevel;
                }
            }
            if (firstNonEmptyEmbeddingLevel == 0) {
                return 0;
            }
            if (embeddingLevelDir != 0) {
                return embeddingLevelDir;
            }
            while (this.charIndex > 0) {
                switch (dirTypeBackward()) {
                    case (byte) 14:
                    case (byte) 15:
                        if (firstNonEmptyEmbeddingLevel != embeddingLevel) {
                            embeddingLevel--;
                            break;
                        }
                        return -1;
                    case (byte) 16:
                    case (byte) 17:
                        if (firstNonEmptyEmbeddingLevel != embeddingLevel) {
                            embeddingLevel--;
                            break;
                        }
                        return 1;
                    case (byte) 18:
                        embeddingLevel++;
                        break;
                    default:
                        break;
                }
            }
            return 0;
        }

        /* Access modifiers changed, original: 0000 */
        public int getExitDir() {
            this.charIndex = this.length;
            int embeddingLevel = 0;
            int lastNonEmptyEmbeddingLevel = 0;
            while (this.charIndex > 0) {
                byte dirTypeBackward = dirTypeBackward();
                if (dirTypeBackward != (byte) 0) {
                    if (dirTypeBackward == (byte) 1 || dirTypeBackward == (byte) 2) {
                        if (embeddingLevel == 0) {
                            return 1;
                        }
                        if (lastNonEmptyEmbeddingLevel == 0) {
                            lastNonEmptyEmbeddingLevel = embeddingLevel;
                        }
                    } else if (dirTypeBackward != (byte) 9) {
                        switch (dirTypeBackward) {
                            case (byte) 14:
                            case (byte) 15:
                                if (lastNonEmptyEmbeddingLevel != embeddingLevel) {
                                    embeddingLevel--;
                                    break;
                                }
                                return -1;
                            case (byte) 16:
                            case (byte) 17:
                                if (lastNonEmptyEmbeddingLevel != embeddingLevel) {
                                    embeddingLevel--;
                                    break;
                                }
                                return 1;
                            case (byte) 18:
                                embeddingLevel++;
                                break;
                            default:
                                if (lastNonEmptyEmbeddingLevel != 0) {
                                    break;
                                }
                                lastNonEmptyEmbeddingLevel = embeddingLevel;
                                break;
                        }
                    }
                } else if (embeddingLevel == 0) {
                    return -1;
                } else {
                    if (lastNonEmptyEmbeddingLevel == 0) {
                        lastNonEmptyEmbeddingLevel = embeddingLevel;
                    }
                }
            }
            return 0;
        }

        private static byte getCachedDirectionality(char c) {
            return c < 1792 ? DIR_TYPE_CACHE[c] : getDirectionality(c);
        }

        /* Access modifiers changed, original: 0000 */
        public byte dirTypeForward() {
            this.lastChar = this.text.charAt(this.charIndex);
            if (Character.isHighSurrogate(this.lastChar)) {
                int codePoint = Character.codePointAt(this.text, this.charIndex);
                this.charIndex += Character.charCount(codePoint);
                return getDirectionality(codePoint);
            }
            this.charIndex++;
            byte dirType = getCachedDirectionality(this.lastChar);
            if (this.isHtml) {
                char c = this.lastChar;
                if (c == '<') {
                    dirType = skipTagForward();
                } else if (c == '&') {
                    dirType = skipEntityForward();
                }
            }
            return dirType;
        }

        /* Access modifiers changed, original: 0000 */
        public byte dirTypeBackward() {
            this.lastChar = this.text.charAt(this.charIndex - 1);
            if (Character.isLowSurrogate(this.lastChar)) {
                int codePoint = Character.codePointBefore(this.text, this.charIndex);
                this.charIndex -= Character.charCount(codePoint);
                return getDirectionality(codePoint);
            }
            this.charIndex--;
            byte dirType = getCachedDirectionality(this.lastChar);
            if (this.isHtml) {
                char c = this.lastChar;
                if (c == '>') {
                    dirType = skipTagBackward();
                } else if (c == ';') {
                    dirType = skipEntityBackward();
                }
            }
            return dirType;
        }

        private byte skipTagForward() {
            int initialCharIndex = this.charIndex;
            while (true) {
                int i = this.charIndex;
                if (i < this.length) {
                    CharSequence charSequence = this.text;
                    this.charIndex = i + 1;
                    this.lastChar = charSequence.charAt(i);
                    char c = this.lastChar;
                    if (c == '>') {
                        return (byte) 12;
                    }
                    if (c == '\"' || c == DateFormat.QUOTE) {
                        c = this.lastChar;
                        while (true) {
                            int i2 = this.charIndex;
                            if (i2 >= this.length) {
                                break;
                            }
                            CharSequence charSequence2 = this.text;
                            this.charIndex = i2 + 1;
                            char charAt = charSequence2.charAt(i2);
                            this.lastChar = charAt;
                            if (charAt == c) {
                                break;
                            }
                        }
                    }
                } else {
                    this.charIndex = initialCharIndex;
                    this.lastChar = '<';
                    return (byte) 13;
                }
            }
        }

        private byte skipTagBackward() {
            int initialCharIndex = this.charIndex;
            while (true) {
                int i = this.charIndex;
                if (i <= 0) {
                    break;
                }
                CharSequence charSequence = this.text;
                i--;
                this.charIndex = i;
                this.lastChar = charSequence.charAt(i);
                char c = this.lastChar;
                if (c == '<') {
                    return (byte) 12;
                }
                if (c == '>') {
                    break;
                } else if (c == '\"' || c == DateFormat.QUOTE) {
                    c = this.lastChar;
                    while (true) {
                        int i2 = this.charIndex;
                        if (i2 <= 0) {
                            break;
                        }
                        charSequence = this.text;
                        i2--;
                        this.charIndex = i2;
                        char charAt = charSequence.charAt(i2);
                        this.lastChar = charAt;
                        if (charAt == c) {
                            break;
                        }
                    }
                }
            }
            this.charIndex = initialCharIndex;
            this.lastChar = '>';
            return (byte) 13;
        }

        private byte skipEntityForward() {
            while (true) {
                int i = this.charIndex;
                if (i >= this.length) {
                    break;
                }
                CharSequence charSequence = this.text;
                this.charIndex = i + 1;
                char charAt = charSequence.charAt(i);
                this.lastChar = charAt;
                if (charAt == ';') {
                    break;
                }
            }
            return (byte) 12;
        }

        private byte skipEntityBackward() {
            int initialCharIndex = this.charIndex;
            char c;
            do {
                int i = this.charIndex;
                if (i <= 0) {
                    break;
                }
                CharSequence charSequence = this.text;
                i--;
                this.charIndex = i;
                this.lastChar = charSequence.charAt(i);
                c = this.lastChar;
                if (c == '&') {
                    return (byte) 12;
                }
            } while (c != ';');
            this.charIndex = initialCharIndex;
            this.lastChar = ';';
            return (byte) 13;
        }
    }

    public static BidiFormatter getInstance() {
        return getDefaultInstanceFromContext(isRtlLocale(Locale.getDefault()));
    }

    public static BidiFormatter getInstance(boolean rtlContext) {
        return getDefaultInstanceFromContext(rtlContext);
    }

    public static BidiFormatter getInstance(Locale locale) {
        return getDefaultInstanceFromContext(isRtlLocale(locale));
    }

    private BidiFormatter(boolean isRtlContext, int flags, TextDirectionHeuristic heuristic) {
        this.mIsRtlContext = isRtlContext;
        this.mFlags = flags;
        this.mDefaultTextDirectionHeuristic = heuristic;
    }

    public boolean isRtlContext() {
        return this.mIsRtlContext;
    }

    public boolean getStereoReset() {
        return (this.mFlags & 2) != 0;
    }

    public String markAfter(CharSequence str, TextDirectionHeuristic heuristic) {
        boolean isRtl = heuristic.isRtl(str, 0, str.length());
        if (!this.mIsRtlContext && (isRtl || getExitDir(str) == 1)) {
            return LRM_STRING;
        }
        if (!this.mIsRtlContext || (isRtl && getExitDir(str) != -1)) {
            return "";
        }
        return RLM_STRING;
    }

    public String markBefore(CharSequence str, TextDirectionHeuristic heuristic) {
        boolean isRtl = heuristic.isRtl(str, 0, str.length());
        if (!this.mIsRtlContext && (isRtl || getEntryDir(str) == 1)) {
            return LRM_STRING;
        }
        if (!this.mIsRtlContext || (isRtl && getEntryDir(str) != -1)) {
            return "";
        }
        return RLM_STRING;
    }

    public boolean isRtl(String str) {
        return isRtl((CharSequence) str);
    }

    public boolean isRtl(CharSequence str) {
        return this.mDefaultTextDirectionHeuristic.isRtl(str, 0, str.length());
    }

    public String unicodeWrap(String str, TextDirectionHeuristic heuristic, boolean isolate) {
        if (str == null) {
            return null;
        }
        return unicodeWrap((CharSequence) str, heuristic, isolate).toString();
    }

    public CharSequence unicodeWrap(CharSequence str, TextDirectionHeuristic heuristic, boolean isolate) {
        if (str == null) {
            return null;
        }
        boolean isRtl = heuristic.isRtl(str, (int) false, str.length());
        SpannableStringBuilder result = new SpannableStringBuilder();
        if (getStereoReset() && isolate) {
            result.append(markBefore(str, isRtl ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR));
        }
        if (isRtl != this.mIsRtlContext) {
            result.append(isRtl ? RLE : LRE);
            result.append(str);
            result.append((char) PDF);
        } else {
            result.append(str);
        }
        if (isolate) {
            result.append(markAfter(str, isRtl ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR));
        }
        return result;
    }

    public String unicodeWrap(String str, TextDirectionHeuristic heuristic) {
        return unicodeWrap(str, heuristic, true);
    }

    public CharSequence unicodeWrap(CharSequence str, TextDirectionHeuristic heuristic) {
        return unicodeWrap(str, heuristic, true);
    }

    public String unicodeWrap(String str, boolean isolate) {
        return unicodeWrap(str, this.mDefaultTextDirectionHeuristic, isolate);
    }

    public CharSequence unicodeWrap(CharSequence str, boolean isolate) {
        return unicodeWrap(str, this.mDefaultTextDirectionHeuristic, isolate);
    }

    public String unicodeWrap(String str) {
        return unicodeWrap(str, this.mDefaultTextDirectionHeuristic, true);
    }

    public CharSequence unicodeWrap(CharSequence str) {
        return unicodeWrap(str, this.mDefaultTextDirectionHeuristic, true);
    }

    private static BidiFormatter getDefaultInstanceFromContext(boolean isRtlContext) {
        return isRtlContext ? DEFAULT_RTL_INSTANCE : DEFAULT_LTR_INSTANCE;
    }

    private static boolean isRtlLocale(Locale locale) {
        return TextUtils.getLayoutDirectionFromLocale(locale) == 1;
    }

    private static int getExitDir(CharSequence str) {
        return new DirectionalityEstimator(str, false).getExitDir();
    }

    private static int getEntryDir(CharSequence str) {
        return new DirectionalityEstimator(str, false).getEntryDir();
    }
}
