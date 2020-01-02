package android.text;

import android.content.pm.PermissionInfo;
import android.content.res.Configuration;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.telephony.gsm.SmsCbConstants;
import java.nio.CharBuffer;
import java.util.Locale;

public class TextDirectionHeuristics {
    public static final TextDirectionHeuristic ANYRTL_LTR = new TextDirectionHeuristicInternal(AnyStrong.INSTANCE_RTL, false);
    public static final TextDirectionHeuristic FIRSTSTRONG_LTR = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, false);
    public static final TextDirectionHeuristic FIRSTSTRONG_RTL = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, true);
    public static final TextDirectionHeuristic LOCALE = TextDirectionHeuristicLocale.INSTANCE;
    public static final TextDirectionHeuristic LTR = new TextDirectionHeuristicInternal(null, false);
    public static final TextDirectionHeuristic RTL = new TextDirectionHeuristicInternal(null, true);
    private static final int STATE_FALSE = 1;
    private static final int STATE_TRUE = 0;
    private static final int STATE_UNKNOWN = 2;

    private interface TextDirectionAlgorithm {
        int checkRtl(CharSequence charSequence, int i, int i2);
    }

    private static class AnyStrong implements TextDirectionAlgorithm {
        public static final AnyStrong INSTANCE_LTR = new AnyStrong(false);
        public static final AnyStrong INSTANCE_RTL = new AnyStrong(true);
        private final boolean mLookForRtl;

        public int checkRtl(CharSequence cs, int start, int count) {
            boolean haveUnlookedFor = false;
            int openIsolateCount = 0;
            int i = start;
            int end = start + count;
            while (i < end) {
                int cp = Character.codePointAt(cs, i);
                if (8294 <= cp && cp <= 8296) {
                    openIsolateCount++;
                } else if (cp == 8297) {
                    if (openIsolateCount > 0) {
                        openIsolateCount--;
                    }
                } else if (openIsolateCount == 0) {
                    int access$100 = TextDirectionHeuristics.isRtlCodePoint(cp);
                    if (access$100 != 0) {
                        if (access$100 != 1) {
                            continue;
                        } else if (!this.mLookForRtl) {
                            return 1;
                        } else {
                            haveUnlookedFor = true;
                        }
                    } else if (this.mLookForRtl) {
                        return 0;
                    } else {
                        haveUnlookedFor = true;
                    }
                } else {
                    continue;
                }
                i += Character.charCount(cp);
            }
            if (haveUnlookedFor) {
                return this.mLookForRtl;
            }
            return 2;
        }

        private AnyStrong(boolean lookForRtl) {
            this.mLookForRtl = lookForRtl;
        }
    }

    private static class FirstStrong implements TextDirectionAlgorithm {
        public static final FirstStrong INSTANCE = new FirstStrong();

        public int checkRtl(CharSequence cs, int start, int count) {
            int result = 2;
            int openIsolateCount = 0;
            int i = start;
            int end = start + count;
            while (i < end && result == 2) {
                int cp = Character.codePointAt(cs, i);
                if (8294 <= cp && cp <= 8296) {
                    openIsolateCount++;
                } else if (cp == 8297) {
                    if (openIsolateCount > 0) {
                        openIsolateCount--;
                    }
                } else if (openIsolateCount == 0) {
                    result = TextDirectionHeuristics.isRtlCodePoint(cp);
                }
                i += Character.charCount(cp);
            }
            return result;
        }

        private FirstStrong() {
        }
    }

    private static abstract class TextDirectionHeuristicImpl implements TextDirectionHeuristic {
        private final TextDirectionAlgorithm mAlgorithm;

        public abstract boolean defaultIsRtl();

        public TextDirectionHeuristicImpl(TextDirectionAlgorithm algorithm) {
            this.mAlgorithm = algorithm;
        }

        public boolean isRtl(char[] array, int start, int count) {
            return isRtl(CharBuffer.wrap(array), start, count);
        }

        public boolean isRtl(CharSequence cs, int start, int count) {
            if (cs == null || start < 0 || count < 0 || cs.length() - count < start) {
                throw new IllegalArgumentException();
            } else if (this.mAlgorithm == null) {
                return defaultIsRtl();
            } else {
                return doCheck(cs, start, count);
            }
        }

        private boolean doCheck(CharSequence cs, int start, int count) {
            int checkRtl = this.mAlgorithm.checkRtl(cs, start, count);
            if (checkRtl == 0) {
                return true;
            }
            if (checkRtl != 1) {
                return defaultIsRtl();
            }
            return false;
        }
    }

    private static class TextDirectionHeuristicInternal extends TextDirectionHeuristicImpl {
        private final boolean mDefaultIsRtl;

        private TextDirectionHeuristicInternal(TextDirectionAlgorithm algorithm, boolean defaultIsRtl) {
            super(algorithm);
            this.mDefaultIsRtl = defaultIsRtl;
        }

        /* Access modifiers changed, original: protected */
        public boolean defaultIsRtl() {
            return this.mDefaultIsRtl;
        }
    }

    private static class TextDirectionHeuristicLocale extends TextDirectionHeuristicImpl {
        public static final TextDirectionHeuristicLocale INSTANCE = new TextDirectionHeuristicLocale();

        public TextDirectionHeuristicLocale() {
            super(null);
        }

        /* Access modifiers changed, original: protected */
        public boolean defaultIsRtl() {
            return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
        }
    }

    private static int isRtlCodePoint(int codePoint) {
        byte directionality = Character.getDirectionality(codePoint);
        if (directionality != (byte) -1) {
            if (directionality != (byte) 0) {
                return (directionality == (byte) 1 || directionality == (byte) 2) ? 0 : 2;
            } else {
                return 1;
            }
        } else if ((MetricsEvent.ACTION_USB_AUDIO_CONNECTED > codePoint || codePoint > 2303) && ((64285 > codePoint || codePoint > 64975) && ((65008 > codePoint || codePoint > 65023) && ((65136 > codePoint || codePoint > 65279) && ((67584 > codePoint || codePoint > 69631) && (124928 > codePoint || codePoint > 126975)))))) {
            return ((8293 > codePoint || codePoint > 8297) && ((PermissionInfo.PROTECTION_MASK_FLAGS > codePoint || codePoint > SmsCbConstants.MESSAGE_ID_ETWS_TYPE_MASK) && ((917504 > codePoint || codePoint > 921599) && ((64976 > codePoint || codePoint > 65007) && (codePoint & Configuration.DENSITY_DPI_ANY) != Configuration.DENSITY_DPI_ANY && ((8352 > codePoint || codePoint > 8399) && (55296 > codePoint || codePoint > 57343)))))) ? 1 : 2;
        } else {
            return 0;
        }
    }
}
