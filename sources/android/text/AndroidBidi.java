package android.text;

import android.annotation.UnsupportedAppUsage;
import android.icu.lang.UCharacter;
import android.icu.text.Bidi;
import android.icu.text.BidiClassifier;
import android.text.Layout.Directions;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;

@VisibleForTesting(visibility = Visibility.PACKAGE)
public class AndroidBidi {
    private static final EmojiBidiOverride sEmojiBidiOverride = new EmojiBidiOverride();

    public static class EmojiBidiOverride extends BidiClassifier {
        private static final int NO_OVERRIDE = (UCharacter.getIntPropertyMaxValue(4096) + 1);

        public EmojiBidiOverride() {
            super(null);
        }

        public int classify(int c) {
            if (Emoji.isNewEmoji(c)) {
                return 10;
            }
            return NO_OVERRIDE;
        }
    }

    @UnsupportedAppUsage
    public static int bidi(int dir, char[] chs, byte[] chInfo) {
        if (chs == null || chInfo == null) {
            throw new NullPointerException();
        }
        int length = chs.length;
        if (chInfo.length >= length) {
            byte paraLevel;
            if (dir == -2) {
                paraLevel = Byte.MAX_VALUE;
            } else if (dir == -1) {
                paraLevel = (byte) 1;
            } else if (dir == 1) {
                paraLevel = (byte) 0;
            } else if (dir != 2) {
                paraLevel = (byte) 0;
            } else {
                paraLevel = (byte) 126;
            }
            Bidi icuBidi = new Bidi(length, 0);
            icuBidi.setCustomClassifier(sEmojiBidiOverride);
            icuBidi.setPara(chs, paraLevel, null);
            for (int i = 0; i < length; i++) {
                chInfo[i] = icuBidi.getLevelAt(i);
            }
            if ((icuBidi.getParaLevel() & 1) == 0) {
                return 1;
            }
            return -1;
        }
        throw new IndexOutOfBoundsException();
    }

    public static Directions directions(int dir, byte[] levels, int lstart, char[] chars, int cstart, int len) {
        int i = len;
        if (i == 0) {
            return Layout.DIRS_ALL_LEFT_TO_RIGHT;
        }
        int i2;
        byte baseLevel = dir == 1 ? (byte) 0 : (byte) 1;
        int curLevel = levels[lstart];
        byte minLevel = curLevel;
        int runCount = 1;
        int e = lstart + i;
        for (i2 = lstart + 1; i2 < e; i2++) {
            int level = levels[i2];
            if (level != curLevel) {
                curLevel = level;
                runCount++;
            }
        }
        i2 = len;
        if ((curLevel & 1) != (baseLevel & 1)) {
            while (true) {
                i2--;
                if (i2 < 0) {
                    break;
                }
                char ch = chars[cstart + i2];
                if (ch != 10) {
                    if (ch != ' ' && ch != 9) {
                        break;
                    }
                } else {
                    i2--;
                    break;
                }
            }
            i2++;
            if (i2 != i) {
                runCount++;
            }
        }
        if (runCount != 1 || minLevel != baseLevel) {
            int i3;
            boolean swap;
            int[] ld = new int[(runCount * 2)];
            byte maxLevel = minLevel;
            int levelBits = minLevel << 26;
            int n = 1;
            int prev = lstart;
            curLevel = minLevel;
            int e2 = lstart + i2;
            for (i3 = lstart; i3 < e2; i3++) {
                byte level2 = levels[i3];
                if (level2 != curLevel) {
                    curLevel = level2;
                    if (level2 > maxLevel) {
                        maxLevel = level2;
                    } else if (level2 < minLevel) {
                        minLevel = level2;
                    }
                    int i4 = n + 1;
                    ld[n] = (i3 - prev) | levelBits;
                    n = i4 + 1;
                    ld[i4] = i3 - lstart;
                    levelBits = curLevel << 26;
                    prev = i3;
                }
            }
            ld[n] = ((lstart + i2) - prev) | levelBits;
            if (i2 < i) {
                n++;
                ld[n] = i2;
                ld[n + 1] = (i - i2) | (baseLevel << 26);
            }
            if ((minLevel & 1) == baseLevel) {
                minLevel++;
                swap = maxLevel > minLevel;
            } else {
                swap = runCount > 1;
            }
            if (swap) {
                byte level3 = maxLevel - 1;
                while (level3 >= minLevel) {
                    n = 0;
                    while (n < ld.length) {
                        if (levels[ld[n]] >= level3) {
                            prev = n + 2;
                            while (prev < ld.length && levels[ld[prev]] >= level3) {
                                prev += 2;
                            }
                            i3 = n;
                            for (e2 = prev - 2; i3 < e2; e2 -= 2) {
                                int x = ld[i3];
                                ld[i3] = ld[e2];
                                ld[e2] = x;
                                x = ld[i3 + 1];
                                ld[i3 + 1] = ld[e2 + 1];
                                ld[e2 + 1] = x;
                                i3 += 2;
                            }
                            n = prev + 2;
                        }
                        n += 2;
                    }
                    level3--;
                }
            }
            return new Directions(ld);
        } else if ((minLevel & 1) != 0) {
            return Layout.DIRS_ALL_RIGHT_TO_LEFT;
        } else {
            return Layout.DIRS_ALL_LEFT_TO_RIGHT;
        }
    }
}
