package com.android.internal.alsa;

public class LineTokenizer {
    public static final int kTokenNotFound = -1;
    private final String mDelimiters;

    public LineTokenizer(String delimiters) {
        this.mDelimiters = delimiters;
    }

    /* Access modifiers changed, original: 0000 */
    public int nextToken(String line, int startIndex) {
        int len = line.length();
        int offset = startIndex;
        while (offset < len && this.mDelimiters.indexOf(line.charAt(offset)) != -1) {
            offset++;
        }
        if (offset < len) {
            return offset;
        }
        return -1;
    }

    /* Access modifiers changed, original: 0000 */
    public int nextDelimiter(String line, int startIndex) {
        int len = line.length();
        int offset = startIndex;
        while (offset < len && this.mDelimiters.indexOf(line.charAt(offset)) == -1) {
            offset++;
        }
        if (offset < len) {
            return offset;
        }
        return -1;
    }
}
