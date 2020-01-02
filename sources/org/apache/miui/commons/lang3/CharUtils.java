package org.apache.miui.commons.lang3;

import android.text.format.DateFormat;

public class CharUtils {
    private static final String[] CHAR_STRING_ARRAY = new String[128];
    public static final char CR = '\r';
    public static final char LF = '\n';

    static {
        char c = 0;
        while (true) {
            String[] strArr = CHAR_STRING_ARRAY;
            if (c < strArr.length) {
                strArr[c] = String.valueOf(c);
                c = (char) (c + 1);
            } else {
                return;
            }
        }
    }

    @Deprecated
    public static Character toCharacterObject(char ch) {
        return Character.valueOf(ch);
    }

    public static Character toCharacterObject(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return Character.valueOf(str.charAt(0));
    }

    public static char toChar(Character ch) {
        if (ch != null) {
            return ch.charValue();
        }
        throw new IllegalArgumentException("The Character must not be null");
    }

    public static char toChar(Character ch, char defaultValue) {
        if (ch == null) {
            return defaultValue;
        }
        return ch.charValue();
    }

    public static char toChar(String str) {
        if (!StringUtils.isEmpty(str)) {
            return str.charAt(0);
        }
        throw new IllegalArgumentException("The String must not be empty");
    }

    public static char toChar(String str, char defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return str.charAt(0);
    }

    public static int toIntValue(char ch) {
        if (isAsciiNumeric(ch)) {
            return ch - 48;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The character ");
        stringBuilder.append(ch);
        stringBuilder.append(" is not in the range '0' - '9'");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static int toIntValue(char ch, int defaultValue) {
        if (isAsciiNumeric(ch)) {
            return ch - 48;
        }
        return defaultValue;
    }

    public static int toIntValue(Character ch) {
        if (ch != null) {
            return toIntValue(ch.charValue());
        }
        throw new IllegalArgumentException("The character must not be null");
    }

    public static int toIntValue(Character ch, int defaultValue) {
        if (ch == null) {
            return defaultValue;
        }
        return toIntValue(ch.charValue(), defaultValue);
    }

    public static String toString(char ch) {
        if (ch < 128) {
            return CHAR_STRING_ARRAY[ch];
        }
        return new String(new char[]{ch});
    }

    public static String toString(Character ch) {
        if (ch == null) {
            return null;
        }
        return toString(ch.charValue());
    }

    public static String unicodeEscaped(char ch) {
        StringBuilder stringBuilder;
        if (ch < 16) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("\\u000");
            stringBuilder.append(Integer.toHexString(ch));
            return stringBuilder.toString();
        } else if (ch < 256) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("\\u00");
            stringBuilder.append(Integer.toHexString(ch));
            return stringBuilder.toString();
        } else if (ch < 4096) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("\\u0");
            stringBuilder.append(Integer.toHexString(ch));
            return stringBuilder.toString();
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("\\u");
            stringBuilder.append(Integer.toHexString(ch));
            return stringBuilder.toString();
        }
    }

    public static String unicodeEscaped(Character ch) {
        if (ch == null) {
            return null;
        }
        return unicodeEscaped(ch.charValue());
    }

    public static boolean isAscii(char ch) {
        return ch < 128;
    }

    public static boolean isAsciiPrintable(char ch) {
        return ch >= ' ' && ch < 127;
    }

    public static boolean isAsciiControl(char ch) {
        return ch < ' ' || ch == 127;
    }

    public static boolean isAsciiAlpha(char ch) {
        return (ch >= DateFormat.CAPITAL_AM_PM && ch <= 'Z') || (ch >= DateFormat.AM_PM && ch <= DateFormat.TIME_ZONE);
    }

    public static boolean isAsciiAlphaUpper(char ch) {
        return ch >= DateFormat.CAPITAL_AM_PM && ch <= 'Z';
    }

    public static boolean isAsciiAlphaLower(char ch) {
        return ch >= DateFormat.AM_PM && ch <= DateFormat.TIME_ZONE;
    }

    public static boolean isAsciiNumeric(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isAsciiAlphanumeric(char ch) {
        return (ch >= DateFormat.CAPITAL_AM_PM && ch <= 'Z') || ((ch >= DateFormat.AM_PM && ch <= DateFormat.TIME_ZONE) || (ch >= '0' && ch <= '9'));
    }
}
