package org.apache.miui.commons.lang3;

import android.net.wifi.WifiEnterpriseConfig;
import android.telecom.Logging.Session;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtils {
    public static final String EMPTY = "";
    public static final int INDEX_NOT_FOUND = -1;
    private static final int PAD_LIMIT = 8192;
    private static final Pattern WHITESPACE_BLOCK = Pattern.compile("\\s+");

    private static class InitStripAccents {
        private static final Throwable java6Exception;
        private static final Method java6NormalizeMethod;
        private static final Object java6NormalizerFormNFD;
        private static final Pattern java6Pattern = sunPattern;
        private static final Method sunDecomposeMethod;
        private static final Throwable sunException;
        private static final Pattern sunPattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

        private InitStripAccents() {
        }

        static {
            Object _java6NormalizerFormNFD = null;
            Method _java6NormalizeMethod = null;
            Method _sunDecomposeMethod = null;
            Throwable _java6Exception = null;
            Throwable _sunException = null;
            int i = 2;
            try {
                _java6NormalizerFormNFD = Thread.currentThread().getContextClassLoader().loadClass("java.text.Normalizer$Form").getField("NFD").get(null);
                i = Thread.currentThread().getContextClassLoader().loadClass("java.text.Normalizer").getMethod("normalize", new Class[]{CharSequence.class, normalizerFormClass});
                _java6NormalizeMethod = i;
            } catch (Exception e1) {
                _java6Exception = e1;
                try {
                    _sunDecomposeMethod = Thread.currentThread().getContextClassLoader().loadClass("sun.text.Normalizer").getMethod("decompose", new Class[]{String.class, Boolean.TYPE, Integer.TYPE});
                } catch (Exception e2) {
                    _sunException = e2;
                }
            }
            java6Exception = _java6Exception;
            java6NormalizerFormNFD = _java6NormalizerFormNFD;
            java6NormalizeMethod = _java6NormalizeMethod;
            sunException = _sunException;
            sunDecomposeMethod = _sunDecomposeMethod;
        }
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return isEmpty(cs) ^ 1;
    }

    public static boolean isBlank(CharSequence cs) {
        if (cs != null) {
            int length = cs.length();
            int strLen = length;
            if (length != 0) {
                for (length = 0; length < strLen; length++) {
                    if (!Character.isWhitespace(cs.charAt(length))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return isBlank(cs) ^ 1;
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    public static String strip(String str) {
        return strip(str, null);
    }

    public static String stripToNull(String str) {
        String str2 = null;
        if (str == null) {
            return null;
        }
        str = strip(str, null);
        if (str.length() != 0) {
            str2 = str;
        }
        return str2;
    }

    public static String stripToEmpty(String str) {
        return str == null ? "" : strip(str, null);
    }

    public static String strip(String str, String stripChars) {
        if (isEmpty(str)) {
            return str;
        }
        return stripEnd(stripStart(str, stripChars), stripChars);
    }

    public static String stripStart(String str, String stripChars) {
        if (str != null) {
            int length = str.length();
            int strLen = length;
            if (length != 0) {
                length = 0;
                if (stripChars == null) {
                    while (length != strLen && Character.isWhitespace(str.charAt(length))) {
                        length++;
                    }
                } else if (stripChars.length() == 0) {
                    return str;
                } else {
                    while (length != strLen && stripChars.indexOf(str.charAt(length)) != -1) {
                        length++;
                    }
                }
                return str.substring(length);
            }
        }
        return str;
    }

    public static String stripEnd(String str, String stripChars) {
        if (str != null) {
            int length = str.length();
            int end = length;
            if (length != 0) {
                if (stripChars == null) {
                    while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                        end--;
                    }
                } else if (stripChars.length() == 0) {
                    return str;
                } else {
                    while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
                        end--;
                    }
                }
                return str.substring(0, end);
            }
        }
        return str;
    }

    public static String[] stripAll(String... strs) {
        return stripAll(strs, null);
    }

    public static String[] stripAll(String[] strs, String stripChars) {
        if (strs != null) {
            int length = strs.length;
            int strsLen = length;
            if (length != 0) {
                String[] newArr = new String[strsLen];
                for (int i = 0; i < strsLen; i++) {
                    newArr[i] = strip(strs[i], stripChars);
                }
                return newArr;
            }
        }
        return strs;
    }

    public static String stripAccents(String input) {
        if (input == null) {
            return null;
        }
        try {
            String result;
            if (InitStripAccents.java6NormalizeMethod != null) {
                result = removeAccentsJava6(input);
            } else if (InitStripAccents.sunDecomposeMethod != null) {
                result = removeAccentsSUN(input);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("The stripAccents(CharSequence) method requires at least Java6, but got: ");
                stringBuilder.append(InitStripAccents.java6Exception);
                stringBuilder.append("; or a Sun JVM: ");
                stringBuilder.append(InitStripAccents.sunException);
                throw new UnsupportedOperationException(stringBuilder.toString());
            }
            return result;
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("IllegalArgumentException occurred", iae);
        } catch (IllegalAccessException iae2) {
            throw new RuntimeException("IllegalAccessException occurred", iae2);
        } catch (InvocationTargetException ite) {
            throw new RuntimeException("InvocationTargetException occurred", ite);
        } catch (SecurityException se) {
            throw new RuntimeException("SecurityException occurred", se);
        }
    }

    private static String removeAccentsJava6(CharSequence text) throws IllegalAccessException, InvocationTargetException {
        if (InitStripAccents.java6NormalizeMethod == null || InitStripAccents.java6NormalizerFormNFD == null) {
            throw new IllegalStateException("java.text.Normalizer is not available", InitStripAccents.java6Exception);
        }
        return InitStripAccents.java6Pattern.matcher((String) InitStripAccents.java6NormalizeMethod.invoke(null, new Object[]{text, InitStripAccents.java6NormalizerFormNFD})).replaceAll("");
    }

    private static String removeAccentsSUN(CharSequence text) throws IllegalAccessException, InvocationTargetException {
        if (InitStripAccents.sunDecomposeMethod != null) {
            return InitStripAccents.sunPattern.matcher((String) InitStripAccents.sunDecomposeMethod.invoke(null, new Object[]{text, Boolean.FALSE, Integer.valueOf(0)})).replaceAll("");
        }
        throw new IllegalStateException("sun.text.Normalizer is not available", InitStripAccents.sunException);
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        if (cs1 == null) {
            return cs2 == null;
        } else {
            return cs1.equals(cs2);
        }
    }

    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        if (str1 == null || str2 == null) {
            return str1 == str2;
        }
        return CharSequenceUtils.regionMatches(str1, true, 0, str2, 0, Math.max(str1.length(), str2.length()));
    }

    public static int indexOf(CharSequence seq, int searchChar) {
        if (isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchChar, 0);
    }

    public static int indexOf(CharSequence seq, int searchChar, int startPos) {
        if (isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchChar, startPos);
    }

    public static int indexOf(CharSequence seq, CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchSeq, 0);
    }

    public static int indexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchSeq, startPos);
    }

    public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        return ordinalIndexOf(str, searchStr, ordinal, false);
    }

    private static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal, boolean lastIndex) {
        int index = -1;
        if (str == null || searchStr == null || ordinal <= 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return lastIndex ? str.length() : 0;
        }
        int found = 0;
        if (lastIndex) {
            index = str.length();
        }
        do {
            if (lastIndex) {
                index = CharSequenceUtils.lastIndexOf(str, searchStr, index - 1);
            } else {
                index = CharSequenceUtils.indexOf(str, searchStr, index + 1);
            }
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return indexOfIgnoreCase(str, searchStr, 0);
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (startPos < 0) {
            startPos = 0;
        }
        int endLimit = (str.length() - searchStr.length()) + 1;
        if (startPos > endLimit) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }
        for (int i = startPos; i < endLimit; i++) {
            if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(CharSequence seq, int searchChar) {
        if (isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchChar, seq.length());
    }

    public static int lastIndexOf(CharSequence seq, int searchChar, int startPos) {
        if (isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchChar, startPos);
    }

    public static int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchSeq, seq.length());
    }

    public static int lastOrdinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        return ordinalIndexOf(str, searchStr, ordinal, true);
    }

    public static int lastIndexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchSeq, startPos);
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return lastIndexOfIgnoreCase(str, searchStr, str.length());
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (startPos > str.length() - searchStr.length()) {
            startPos = str.length() - searchStr.length();
        }
        if (startPos < 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }
        for (int i = startPos; i >= 0; i--) {
            if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(CharSequence seq, int searchChar) {
        boolean z = false;
        if (isEmpty(seq)) {
            return false;
        }
        if (CharSequenceUtils.indexOf(seq, searchChar, 0) >= 0) {
            z = true;
        }
        return z;
    }

    public static boolean contains(CharSequence seq, CharSequence searchSeq) {
        boolean z = false;
        if (seq == null || searchSeq == null) {
            return false;
        }
        if (CharSequenceUtils.indexOf(seq, searchSeq, 0) >= 0) {
            z = true;
        }
        return z;
    }

    public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsWhitespace(CharSequence seq) {
        if (isEmpty(seq)) {
            return false;
        }
        int strLen = seq.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(seq.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Missing block: B:17:0x003c, code skipped:
            return r5;
     */
    public static int indexOfAny(java.lang.CharSequence r10, char... r11) {
        /*
        r0 = isEmpty(r10);
        r1 = -1;
        if (r0 != 0) goto L_0x0044;
    L_0x0007:
        r0 = org.apache.miui.commons.lang3.ArrayUtils.isEmpty(r11);
        if (r0 == 0) goto L_0x000e;
    L_0x000d:
        goto L_0x0044;
    L_0x000e:
        r0 = r10.length();
        r2 = r0 + -1;
        r3 = r11.length;
        r4 = r3 + -1;
        r5 = 0;
    L_0x0018:
        if (r5 >= r0) goto L_0x0043;
    L_0x001a:
        r6 = r10.charAt(r5);
        r7 = 0;
    L_0x001f:
        if (r7 >= r3) goto L_0x0040;
    L_0x0021:
        r8 = r11[r7];
        if (r8 != r6) goto L_0x003d;
    L_0x0025:
        if (r5 >= r2) goto L_0x003c;
    L_0x0027:
        if (r7 >= r4) goto L_0x003c;
    L_0x0029:
        r8 = java.lang.Character.isHighSurrogate(r6);
        if (r8 == 0) goto L_0x003c;
    L_0x002f:
        r8 = r7 + 1;
        r8 = r11[r8];
        r9 = r5 + 1;
        r9 = r10.charAt(r9);
        if (r8 != r9) goto L_0x003d;
    L_0x003b:
        return r5;
    L_0x003c:
        return r5;
    L_0x003d:
        r7 = r7 + 1;
        goto L_0x001f;
    L_0x0040:
        r5 = r5 + 1;
        goto L_0x0018;
    L_0x0043:
        return r1;
    L_0x0044:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.miui.commons.lang3.StringUtils.indexOfAny(java.lang.CharSequence, char[]):int");
    }

    public static int indexOfAny(CharSequence cs, String searchChars) {
        if (isEmpty(cs) || isEmpty(searchChars)) {
            return -1;
        }
        return indexOfAny(cs, searchChars.toCharArray());
    }

    public static boolean containsAny(CharSequence cs, char... searchChars) {
        if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
            return false;
        }
        int csLength = cs.length();
        int searchLength = searchChars.length;
        int csLast = csLength - 1;
        int searchLast = searchLength - 1;
        int i = 0;
        while (i < csLength) {
            char ch = cs.charAt(i);
            int j = 0;
            while (j < searchLength) {
                if (searchChars[j] == ch) {
                    if (!Character.isHighSurrogate(ch) || j == searchLast) {
                        return true;
                    }
                    if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                        return true;
                    }
                }
                j++;
            }
            i++;
        }
        return false;
    }

    public static boolean containsAny(CharSequence cs, CharSequence searchChars) {
        if (searchChars == null) {
            return false;
        }
        return containsAny(cs, CharSequenceUtils.toCharArray(searchChars));
    }

    public static int indexOfAnyBut(CharSequence cs, char... searchChars) {
        if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
            return -1;
        }
        int csLen = cs.length();
        int csLast = csLen - 1;
        int searchLen = searchChars.length;
        int searchLast = searchLen - 1;
        int i = 0;
        while (i < csLen) {
            char ch = cs.charAt(i);
            int j = 0;
            while (j < searchLen) {
                if (searchChars[j] != ch || (i < csLast && j < searchLast && Character.isHighSurrogate(ch) && searchChars[j + 1] != cs.charAt(i + 1))) {
                    j++;
                } else {
                    i++;
                }
            }
            return i;
        }
        return -1;
    }

    public static int indexOfAnyBut(CharSequence seq, CharSequence searchChars) {
        if (isEmpty(seq) || isEmpty(searchChars)) {
            return -1;
        }
        int strLen = seq.length();
        for (int i = 0; i < strLen; i++) {
            int ch = seq.charAt(i);
            boolean chFound = CharSequenceUtils.indexOf(searchChars, ch, 0) >= 0;
            if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
                int ch2 = seq.charAt(i + 1);
                if (chFound && CharSequenceUtils.indexOf(searchChars, ch2, 0) < 0) {
                    return i;
                }
            } else if (!chFound) {
                return i;
            }
        }
        return -1;
    }

    public static boolean containsOnly(CharSequence cs, char... valid) {
        boolean z = false;
        if (valid == null || cs == null) {
            return false;
        }
        if (cs.length() == 0) {
            return true;
        }
        if (valid.length == 0) {
            return false;
        }
        if (indexOfAnyBut(cs, valid) == -1) {
            z = true;
        }
        return z;
    }

    public static boolean containsOnly(CharSequence cs, String validChars) {
        if (cs == null || validChars == null) {
            return false;
        }
        return containsOnly(cs, validChars.toCharArray());
    }

    public static boolean containsNone(CharSequence cs, char... searchChars) {
        if (cs == null || searchChars == null) {
            return true;
        }
        int csLen = cs.length();
        int csLast = csLen - 1;
        int searchLen = searchChars.length;
        int searchLast = searchLen - 1;
        int i = 0;
        while (i < csLen) {
            char ch = cs.charAt(i);
            int j = 0;
            while (j < searchLen) {
                if (searchChars[j] == ch) {
                    if (!Character.isHighSurrogate(ch) || j == searchLast) {
                        return false;
                    }
                    if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                        return false;
                    }
                }
                j++;
            }
            i++;
        }
        return true;
    }

    public static boolean containsNone(CharSequence cs, String invalidChars) {
        if (cs == null || invalidChars == null) {
            return true;
        }
        return containsNone(cs, invalidChars.toCharArray());
    }

    public static int indexOfAny(CharSequence str, CharSequence... searchStrs) {
        int i = -1;
        if (str == null || searchStrs == null) {
            return -1;
        }
        int ret = Integer.MAX_VALUE;
        for (CharSequence search : searchStrs) {
            if (search != null) {
                int tmp = CharSequenceUtils.indexOf(str, search, 0);
                if (tmp != -1 && tmp < ret) {
                    ret = tmp;
                }
            }
        }
        if (ret != Integer.MAX_VALUE) {
            i = ret;
        }
        return i;
    }

    public static int lastIndexOfAny(CharSequence str, CharSequence... searchStrs) {
        if (str == null || searchStrs == null) {
            return -1;
        }
        int ret = -1;
        for (CharSequence search : searchStrs) {
            if (search != null) {
                int tmp = CharSequenceUtils.lastIndexOf(str, search, str.length());
                if (tmp > ret) {
                    ret = tmp;
                }
            }
        }
        return ret;
    }

    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }
        if (start < 0) {
            start += str.length();
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return "";
        }
        return str.substring(start);
    }

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end < 0) {
            end += str.length();
        }
        if (start < 0) {
            start += str.length();
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return "";
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    public static String left(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    public static String mid(String str, int pos, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0 || pos > str.length()) {
            return "";
        }
        if (pos < 0) {
            pos = 0;
        }
        if (str.length() <= pos + len) {
            return str.substring(pos);
        }
        return str.substring(pos, pos + len);
    }

    public static String substringBefore(String str, String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.length() == 0) {
            return "";
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        String str2 = "";
        if (separator == null) {
            return str2;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return str2;
        }
        return str.substring(separator.length() + pos);
    }

    public static String substringBeforeLast(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static String substringAfterLast(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        String str2 = "";
        if (isEmpty(separator)) {
            return str2;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1 || pos == str.length() - separator.length()) {
            return str2;
        }
        return str.substring(separator.length() + pos);
    }

    public static String substringBetween(String str, String tag) {
        return substringBetween(str, tag, tag);
    }

    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, open.length() + start);
            if (end != -1) {
                return str.substring(open.length() + start, end);
            }
        }
        return null;
    }

    public static String[] substringsBetween(String str, String open, String close) {
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        int closeLen = close.length();
        int openLen = open.length();
        List<String> list = new ArrayList();
        int pos = 0;
        while (pos < strLen - closeLen) {
            int start = str.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            start += openLen;
            int end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            list.add(str.substring(start, end));
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static String[] split(String str) {
        return split(str, null, -1);
    }

    public static String[] split(String str, char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    public static String[] split(String str, String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    public static String[] split(String str, String separatorChars, int max) {
        return splitWorker(str, separatorChars, max, false);
    }

    public static String[] splitByWholeSeparator(String str, String separator) {
        return splitByWholeSeparatorWorker(str, separator, -1, false);
    }

    public static String[] splitByWholeSeparator(String str, String separator, int max) {
        return splitByWholeSeparatorWorker(str, separator, max, false);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator) {
        return splitByWholeSeparatorWorker(str, separator, -1, true);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator, int max) {
        return splitByWholeSeparatorWorker(str, separator, max, true);
    }

    private static String[] splitByWholeSeparatorWorker(String str, String separator, int max, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        if (separator != null) {
            String str2 = "";
            if (!str2.equals(separator)) {
                int separatorLength = separator.length();
                ArrayList<String> substrings = new ArrayList();
                int numberOfSubstrings = 0;
                int beg = 0;
                int end = 0;
                while (end < len) {
                    end = str.indexOf(separator, beg);
                    if (end <= -1) {
                        substrings.add(str.substring(beg));
                        end = len;
                    } else if (end > beg) {
                        numberOfSubstrings++;
                        if (numberOfSubstrings == max) {
                            end = len;
                            substrings.add(str.substring(beg));
                        } else {
                            substrings.add(str.substring(beg, end));
                            beg = end + separatorLength;
                        }
                    } else {
                        if (preserveAllTokens) {
                            numberOfSubstrings++;
                            if (numberOfSubstrings == max) {
                                end = len;
                                substrings.add(str.substring(beg));
                            } else {
                                substrings.add(str2);
                            }
                        }
                        beg = end + separatorLength;
                    }
                }
                return (String[]) substrings.toArray(new String[substrings.size()]);
            }
        }
        return splitWorker(str, null, max, preserveAllTokens);
    }

    public static String[] splitPreserveAllTokens(String str) {
        return splitWorker(str, null, -1, true);
    }

    public static String[] splitPreserveAllTokens(String str, char separatorChar) {
        return splitWorker(str, separatorChar, true);
    }

    private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList();
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                i++;
                start = i;
            } else {
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static String[] splitPreserveAllTokens(String str, String separatorChars) {
        return splitWorker(str, separatorChars, -1, true);
    }

    public static String[] splitPreserveAllTokens(String str, String separatorChars, int max) {
        return splitWorker(str, separatorChars, max, true);
    }

    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList();
        int sizePlus1 = 1;
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        int sizePlus12;
        if (separatorChars == null) {
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        sizePlus12 = sizePlus1 + 1;
                        if (sizePlus1 == max) {
                            lastMatch = false;
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                        sizePlus1 = sizePlus12;
                    }
                    i++;
                    start = i;
                } else {
                    lastMatch = false;
                    match = true;
                    i++;
                }
            }
        } else if (separatorChars.length() == 1) {
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        int sizePlus13 = sizePlus1 + 1;
                        if (sizePlus1 == max) {
                            lastMatch = false;
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                        sizePlus1 = sizePlus13;
                    }
                    i++;
                    start = i;
                } else {
                    lastMatch = false;
                    match = true;
                    i++;
                }
            }
        } else {
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        sizePlus12 = sizePlus1 + 1;
                        if (sizePlus1 == max) {
                            lastMatch = false;
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                        sizePlus1 = sizePlus12;
                    }
                    i++;
                    start = i;
                } else {
                    lastMatch = false;
                    match = true;
                    i++;
                }
            }
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static String[] splitByCharacterType(String str) {
        return splitByCharacterType(str, false);
    }

    public static String[] splitByCharacterTypeCamelCase(String str) {
        return splitByCharacterType(str, true);
    }

    private static String[] splitByCharacterType(String str, boolean camelCase) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        char[] c = str.toCharArray();
        List<String> list = new ArrayList();
        int tokenStart = 0;
        int currentType = Character.getType(c[0]);
        for (int pos = 0 + 1; pos < c.length; pos++) {
            int type = Character.getType(c[pos]);
            if (type != currentType) {
                if (camelCase && type == 2 && currentType == 1) {
                    int newTokenStart = pos - 1;
                    if (newTokenStart != tokenStart) {
                        list.add(new String(c, tokenStart, newTokenStart - tokenStart));
                        tokenStart = newTokenStart;
                    }
                } else {
                    list.add(new String(c, tokenStart, pos - tokenStart));
                    tokenStart = pos;
                }
                currentType = type;
            }
        }
        list.add(new String(c, tokenStart, c.length - tokenStart));
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static <T> String join(T... elements) {
        return join((Object[]) elements, null);
    }

    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String join(Iterator<?> iterator, char separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
        }
        StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            buf.append(separator);
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    public static String join(Iterator<?> iterator, String separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
        }
        StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    public static String join(Iterable<?> iterable, char separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    public static String join(Iterable<?> iterable, String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    public static String deleteWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                int count2 = count + 1;
                chs[count] = str.charAt(i);
                count = count2;
            }
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }

    /* JADX WARNING: Missing block: B:9:0x001d, code skipped:
            return r1;
     */
    public static java.lang.String removeStart(java.lang.String r1, java.lang.String r2) {
        /*
        r0 = isEmpty(r1);
        if (r0 != 0) goto L_0x001d;
    L_0x0006:
        r0 = isEmpty(r2);
        if (r0 == 0) goto L_0x000d;
    L_0x000c:
        goto L_0x001d;
    L_0x000d:
        r0 = r1.startsWith(r2);
        if (r0 == 0) goto L_0x001c;
    L_0x0013:
        r0 = r2.length();
        r0 = r1.substring(r0);
        return r0;
    L_0x001c:
        return r1;
    L_0x001d:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.miui.commons.lang3.StringUtils.removeStart(java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Missing block: B:9:0x001d, code skipped:
            return r1;
     */
    public static java.lang.String removeStartIgnoreCase(java.lang.String r1, java.lang.String r2) {
        /*
        r0 = isEmpty(r1);
        if (r0 != 0) goto L_0x001d;
    L_0x0006:
        r0 = isEmpty(r2);
        if (r0 == 0) goto L_0x000d;
    L_0x000c:
        goto L_0x001d;
    L_0x000d:
        r0 = startsWithIgnoreCase(r1, r2);
        if (r0 == 0) goto L_0x001c;
    L_0x0013:
        r0 = r2.length();
        r0 = r1.substring(r0);
        return r0;
    L_0x001c:
        return r1;
    L_0x001d:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.miui.commons.lang3.StringUtils.removeStartIgnoreCase(java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Missing block: B:9:0x0023, code skipped:
            return r3;
     */
    public static java.lang.String removeEnd(java.lang.String r3, java.lang.String r4) {
        /*
        r0 = isEmpty(r3);
        if (r0 != 0) goto L_0x0023;
    L_0x0006:
        r0 = isEmpty(r4);
        if (r0 == 0) goto L_0x000d;
    L_0x000c:
        goto L_0x0023;
    L_0x000d:
        r0 = r3.endsWith(r4);
        if (r0 == 0) goto L_0x0022;
    L_0x0013:
        r0 = 0;
        r1 = r3.length();
        r2 = r4.length();
        r1 = r1 - r2;
        r0 = r3.substring(r0, r1);
        return r0;
    L_0x0022:
        return r3;
    L_0x0023:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.miui.commons.lang3.StringUtils.removeEnd(java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Missing block: B:9:0x0023, code skipped:
            return r3;
     */
    public static java.lang.String removeEndIgnoreCase(java.lang.String r3, java.lang.String r4) {
        /*
        r0 = isEmpty(r3);
        if (r0 != 0) goto L_0x0023;
    L_0x0006:
        r0 = isEmpty(r4);
        if (r0 == 0) goto L_0x000d;
    L_0x000c:
        goto L_0x0023;
    L_0x000d:
        r0 = endsWithIgnoreCase(r3, r4);
        if (r0 == 0) goto L_0x0022;
    L_0x0013:
        r0 = 0;
        r1 = r3.length();
        r2 = r4.length();
        r1 = r1 - r2;
        r0 = r3.substring(r0, r1);
        return r0;
    L_0x0022:
        return r3;
    L_0x0023:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.miui.commons.lang3.StringUtils.removeEndIgnoreCase(java.lang.String, java.lang.String):java.lang.String");
    }

    public static String remove(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        return replace(str, remove, "", -1);
    }

    public static String remove(String str, char remove) {
        if (isEmpty(str) || str.indexOf(remove) == -1) {
            return str;
        }
        char[] chars = str.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != remove) {
                int pos2 = pos + 1;
                chars[pos] = chars[i];
                pos = pos2;
            }
        }
        return new String(chars, 0, pos);
    }

    public static String replaceOnce(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    public static String replace(String text, String searchString, String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, 0);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        int i = 64;
        if (max < 0) {
            i = 16;
        } else if (max <= 64) {
            i = max;
        }
        StringBuilder buf = new StringBuilder(text.length() + (increase * i));
        while (end != -1) {
            buf.append(text.substring(start, end));
            buf.append(replacement);
            start = end + replLength;
            max--;
            if (max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static String replaceEach(String text, String[] searchList, String[] replacementList) {
        return replaceEach(text, searchList, replacementList, false, 0);
    }

    public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
        return replaceEach(text, searchList, replacementList, true, searchList == null ? 0 : searchList.length);
    }

    private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
        String str = text;
        String[] strArr = searchList;
        String[] strArr2 = replacementList;
        boolean z = repeat;
        if (str == null || text.length() == 0 || strArr == null || strArr.length == 0 || strArr2 == null || strArr2.length == 0) {
            return str;
        }
        if (timeToLive >= 0) {
            int searchLength = strArr.length;
            int replacementLength = strArr2.length;
            if (searchLength == replacementLength) {
                int tempIndex;
                boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
                int textIndex = -1;
                int replaceIndex = -1;
                int i = 0;
                while (i < searchLength) {
                    if (!(noMoreMatchesForReplIndex[i] || strArr[i] == null || strArr[i].length() == 0 || strArr2[i] == null)) {
                        tempIndex = str.indexOf(strArr[i]);
                        if (tempIndex == -1) {
                            noMoreMatchesForReplIndex[i] = true;
                        } else if (textIndex == -1 || tempIndex < textIndex) {
                            textIndex = tempIndex;
                            replaceIndex = i;
                        }
                    }
                    i++;
                }
                if (textIndex == -1) {
                    return str;
                }
                int greater;
                int i2;
                i = 0;
                int increase = 0;
                int i3 = 0;
                while (i3 < strArr.length) {
                    if (!(strArr[i3] == null || strArr2[i3] == null)) {
                        greater = strArr2[i3].length() - strArr[i3].length();
                        if (greater > 0) {
                            increase += greater * 3;
                        }
                    }
                    i3++;
                }
                StringBuilder buf = new StringBuilder(text.length() + Math.min(increase, text.length() / 5));
                while (textIndex != -1) {
                    for (greater = i; greater < textIndex; greater++) {
                        buf.append(str.charAt(greater));
                    }
                    buf.append(strArr2[replaceIndex]);
                    i = textIndex + strArr[replaceIndex].length();
                    textIndex = -1;
                    replaceIndex = -1;
                    i2 = 0;
                    while (i2 < searchLength) {
                        if (!noMoreMatchesForReplIndex[i2] && strArr[i2] != null) {
                            if (strArr[i2].length() != 0) {
                                if (strArr2[i2] != null) {
                                    tempIndex = str.indexOf(strArr[i2], i);
                                    if (tempIndex == -1) {
                                        noMoreMatchesForReplIndex[i2] = true;
                                    } else if (textIndex == -1 || tempIndex < textIndex) {
                                        textIndex = tempIndex;
                                        replaceIndex = i2;
                                    }
                                }
                            }
                        }
                        i2++;
                    }
                    i2 = 1;
                }
                int textLength = text.length();
                for (i2 = i; i2 < textLength; i2++) {
                    buf.append(str.charAt(i2));
                }
                String result = buf.toString();
                if (z) {
                    return replaceEach(result, strArr, strArr2, z, timeToLive - 1);
                }
                return result;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Search and Replace array lengths don't match: ");
            stringBuilder.append(searchLength);
            stringBuilder.append(" vs ");
            stringBuilder.append(replacementLength);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
    }

    public static String replaceChars(String str, char searchChar, char replaceChar) {
        if (str == null) {
            return null;
        }
        return str.replace(searchChar, replaceChar);
    }

    public static String replaceChars(String str, String searchChars, String replaceChars) {
        if (isEmpty(str) || isEmpty(searchChars)) {
            return str;
        }
        if (replaceChars == null) {
            replaceChars = "";
        }
        boolean modified = false;
        int replaceCharsLength = replaceChars.length();
        int strLength = str.length();
        StringBuilder buf = new StringBuilder(strLength);
        for (int i = 0; i < strLength; i++) {
            char ch = str.charAt(i);
            int index = searchChars.indexOf(ch);
            if (index >= 0) {
                modified = true;
                if (index < replaceCharsLength) {
                    buf.append(replaceChars.charAt(index));
                }
            } else {
                buf.append(ch);
            }
        }
        if (modified) {
            return buf.toString();
        }
        return str;
    }

    public static String overlay(String str, String overlay, int start, int end) {
        if (str == null) {
            return null;
        }
        if (overlay == null) {
            overlay = "";
        }
        int len = str.length();
        if (start < 0) {
            start = 0;
        }
        if (start > len) {
            start = len;
        }
        if (end < 0) {
            end = 0;
        }
        if (end > len) {
            end = len;
        }
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        StringBuilder stringBuilder = new StringBuilder((((len + start) - end) + overlay.length()) + 1);
        stringBuilder.append(str.substring(0, start));
        stringBuilder.append(overlay);
        stringBuilder.append(str.substring(end));
        return stringBuilder.toString();
    }

    public static String chomp(String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            char ch = str.charAt(0);
            if (ch == CharUtils.CR || ch == 10) {
                return "";
            }
            return str;
        }
        int lastIdx = str.length() - 1;
        char last = str.charAt(lastIdx);
        if (last == 10) {
            if (str.charAt(lastIdx - 1) == CharUtils.CR) {
                lastIdx--;
            }
        } else if (last != CharUtils.CR) {
            lastIdx++;
        }
        return str.substring(0, lastIdx);
    }

    @Deprecated
    public static String chomp(String str, String separator) {
        return removeEnd(str, separator);
    }

    public static String chop(String str) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (strLen < 2) {
            return "";
        }
        int lastIdx = strLen - 1;
        String ret = str.substring(0, lastIdx);
        if (str.charAt(lastIdx) == 10 && ret.charAt(lastIdx - 1) == CharUtils.CR) {
            return ret.substring(0, lastIdx - 1);
        }
        return ret;
    }

    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return "";
        }
        int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= 8192) {
            return repeat(str.charAt(0), repeat);
        }
        int outputLength = inputLength * repeat;
        if (inputLength == 1) {
            return repeat(str.charAt(0), repeat);
        }
        if (inputLength != 2) {
            StringBuilder buf = new StringBuilder(outputLength);
            for (int i = 0; i < repeat; i++) {
                buf.append(str);
            }
            return buf.toString();
        }
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        char[] output2 = new char[outputLength];
        for (int i2 = (repeat * 2) - 2; i2 >= 0; i2 = (i2 - 1) - 1) {
            output2[i2] = ch0;
            output2[i2 + 1] = ch1;
        }
        return new String(output2);
    }

    public static String repeat(String str, String separator, int repeat) {
        if (str == null || separator == null) {
            return repeat(str, repeat);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(separator);
        return removeEnd(repeat(stringBuilder.toString(), repeat), separator);
    }

    public static String repeat(char ch, int repeat) {
        char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    public static String rightPad(String str, int size) {
        return rightPad(str, size, ' ');
    }

    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(repeat(padChar, pads));
    }

    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        }
        int padLen = padStr.length();
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (padLen == 1 && pads <= 8192) {
            return rightPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return str.concat(padStr);
        }
        if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        }
        char[] padding = new char[pads];
        char[] padChars = padStr.toCharArray();
        for (int i = 0; i < pads; i++) {
            padding[i] = padChars[i % padLen];
        }
        return str.concat(new String(padding));
    }

    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(padChar, pads).concat(str);
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        }
        int padLen = padStr.length();
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (padLen == 1 && pads <= 8192) {
            return leftPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return padStr.concat(str);
        }
        if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        }
        char[] padding = new char[pads];
        char[] padChars = padStr.toCharArray();
        for (int i = 0; i < pads; i++) {
            padding[i] = padChars[i % padLen];
        }
        return new String(padding).concat(str);
    }

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static String center(String str, int size) {
        return center(str, size, ' ');
    }

    public static String center(String str, int size, char padChar) {
        if (str == null || size <= 0) {
            return str;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        return rightPad(leftPad(str, (pads / 2) + strLen, padChar), size, padChar);
    }

    public static String center(String str, int size, String padStr) {
        if (str == null || size <= 0) {
            return str;
        }
        if (isEmpty(padStr)) {
            padStr = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        return rightPad(leftPad(str, (pads / 2) + strLen, padStr), size, padStr);
    }

    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }

    public static String upperCase(String str, Locale locale) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase(locale);
    }

    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    public static String lowerCase(String str, Locale locale) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase(locale);
    }

    public static String capitalize(String str) {
        if (str != null) {
            int length = str.length();
            int strLen = length;
            if (length != 0) {
                StringBuilder stringBuilder = new StringBuilder(strLen);
                stringBuilder.append(Character.toTitleCase(str.charAt(0)));
                stringBuilder.append(str.substring(1));
                return stringBuilder.toString();
            }
        }
        return str;
    }

    public static String uncapitalize(String str) {
        if (str != null) {
            int length = str.length();
            int strLen = length;
            if (length != 0) {
                StringBuilder stringBuilder = new StringBuilder(strLen);
                stringBuilder.append(Character.toLowerCase(str.charAt(0)));
                stringBuilder.append(str.substring(1));
                return stringBuilder.toString();
            }
        }
        return str;
    }

    public static String swapCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        char[] buffer = str.toCharArray();
        for (int i = 0; i < buffer.length; i++) {
            char ch = buffer[i];
            if (Character.isUpperCase(ch)) {
                buffer[i] = Character.toLowerCase(ch);
            } else if (Character.isTitleCase(ch)) {
                buffer[i] = Character.toLowerCase(ch);
            } else if (Character.isLowerCase(ch)) {
                buffer[i] = Character.toUpperCase(ch);
            }
        }
        return new String(buffer);
    }

    public static int countMatches(CharSequence str, CharSequence sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while (true) {
            int indexOf = CharSequenceUtils.indexOf(str, sub, idx);
            idx = indexOf;
            if (indexOf == -1) {
                return count;
            }
            count++;
            idx += sub.length();
        }
    }

    public static boolean isAlpha(CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetter(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaSpace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        int i = 0;
        while (i < sz) {
            if (!Character.isLetter(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean isAlphanumeric(CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetterOrDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphanumericSpace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        int i = 0;
        while (i < sz) {
            if (!Character.isLetterOrDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean isAsciiPrintable(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!CharUtils.isAsciiPrintable(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumericSpace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        int i = 0;
        while (i < sz) {
            if (!Character.isDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean isWhitespace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllLowerCase(CharSequence cs) {
        if (cs == null || isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLowerCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllUpperCase(CharSequence cs) {
        if (cs == null || isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isUpperCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String defaultString(String str) {
        return str == null ? "" : str;
    }

    public static String defaultString(String str, String defaultStr) {
        return str == null ? defaultStr : str;
    }

    public static <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    public static String reverseDelimited(String str, char separatorChar) {
        if (str == null) {
            return null;
        }
        Object[] strs = split(str, separatorChar);
        ArrayUtils.reverse(strs);
        return join(strs, separatorChar);
    }

    public static String abbreviate(String str, int maxWidth) {
        return abbreviate(str, 0, maxWidth);
    }

    public static String abbreviate(String str, int offset, int maxWidth) {
        if (str == null) {
            return null;
        }
        if (maxWidth < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        } else if (str.length() <= maxWidth) {
            return str;
        } else {
            if (offset > str.length()) {
                offset = str.length();
            }
            if (str.length() - offset < maxWidth - 3) {
                offset = str.length() - (maxWidth - 3);
            }
            String abrevMarker = Session.TRUNCATE_STRING;
            String str2 = Session.TRUNCATE_STRING;
            StringBuilder stringBuilder;
            if (offset <= 4) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str.substring(0, maxWidth - 3));
                stringBuilder.append(str2);
                return stringBuilder.toString();
            } else if (maxWidth < 7) {
                throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
            } else if ((offset + maxWidth) - 3 < str.length()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(abbreviate(str.substring(offset), maxWidth - 3));
                return stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(str.substring(str.length() - (maxWidth - 3)));
                return stringBuilder.toString();
            }
        }
    }

    /* JADX WARNING: Missing block: B:11:0x004b, code skipped:
            return r5;
     */
    public static java.lang.String abbreviateMiddle(java.lang.String r5, java.lang.String r6, int r7) {
        /*
        r0 = isEmpty(r5);
        if (r0 != 0) goto L_0x004b;
    L_0x0006:
        r0 = isEmpty(r6);
        if (r0 == 0) goto L_0x000d;
    L_0x000c:
        goto L_0x004b;
    L_0x000d:
        r0 = r5.length();
        if (r7 >= r0) goto L_0x004a;
    L_0x0013:
        r0 = r6.length();
        r0 = r0 + 2;
        if (r7 >= r0) goto L_0x001c;
    L_0x001b:
        goto L_0x004a;
    L_0x001c:
        r0 = r6.length();
        r0 = r7 - r0;
        r1 = r0 / 2;
        r2 = r0 % 2;
        r1 = r1 + r2;
        r2 = r5.length();
        r3 = r0 / 2;
        r2 = r2 - r3;
        r3 = new java.lang.StringBuilder;
        r3.<init>(r7);
        r4 = 0;
        r4 = r5.substring(r4, r1);
        r3.append(r4);
        r3.append(r6);
        r4 = r5.substring(r2);
        r3.append(r4);
        r4 = r3.toString();
        return r4;
    L_0x004a:
        return r5;
    L_0x004b:
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.miui.commons.lang3.StringUtils.abbreviateMiddle(java.lang.String, java.lang.String, int):java.lang.String");
    }

    public static String difference(String str1, String str2) {
        if (str1 == null) {
            return str2;
        }
        if (str2 == null) {
            return str1;
        }
        int at = indexOfDifference(str1, str2);
        if (at == -1) {
            return "";
        }
        return str2.substring(at);
    }

    public static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return -1;
        }
        if (cs1 == null || cs2 == null) {
            return 0;
        }
        int i = 0;
        while (i < cs1.length() && i < cs2.length() && cs1.charAt(i) == cs2.charAt(i)) {
            i++;
        }
        if (i < cs2.length() || i < cs1.length()) {
            return i;
        }
        return -1;
    }

    public static int indexOfDifference(CharSequence... css) {
        if (css == null || css.length <= 1) {
            return -1;
        }
        boolean anyStringNull = false;
        boolean allStringsNull = true;
        int arrayLen = css.length;
        int shortestStrLen = Integer.MAX_VALUE;
        int longestStrLen = 0;
        for (int i = 0; i < arrayLen; i++) {
            if (css[i] == null) {
                anyStringNull = true;
                shortestStrLen = 0;
            } else {
                allStringsNull = false;
                shortestStrLen = Math.min(css[i].length(), shortestStrLen);
                longestStrLen = Math.max(css[i].length(), longestStrLen);
            }
        }
        if (allStringsNull || (longestStrLen == 0 && !anyStringNull)) {
            return -1;
        }
        if (shortestStrLen == 0) {
            return 0;
        }
        int firstDiff = -1;
        for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
            char comparisonChar = css[0].charAt(stringPos);
            for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
                if (css[arrayPos].charAt(stringPos) != comparisonChar) {
                    firstDiff = stringPos;
                    break;
                }
            }
            if (firstDiff != -1) {
                break;
            }
        }
        if (firstDiff != -1 || shortestStrLen == longestStrLen) {
            return firstDiff;
        }
        return shortestStrLen;
    }

    public static String getCommonPrefix(String... strs) {
        String str = "";
        if (strs == null || strs.length == 0) {
            return str;
        }
        int smallestIndexOfDiff = indexOfDifference(strs);
        if (smallestIndexOfDiff == -1) {
            if (strs[0] == null) {
                return str;
            }
            return strs[0];
        } else if (smallestIndexOfDiff == 0) {
            return str;
        } else {
            return strs[0].substring(0, smallestIndexOfDiff);
        }
    }

    public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        int n = s.length();
        int m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        int i;
        if (n > m) {
            CharSequence tmp = s;
            s = t;
            t = tmp;
            n = m;
            m = t.length();
        }
        int[] p = new int[(n + 1)];
        int[] d = new int[(n + 1)];
        for (i = 0; i <= n; i++) {
            p[i] = i;
        }
        for (int j = 1; j <= m; j++) {
            char t_j = t.charAt(j - 1);
            d[0] = j;
            for (i = 1; i <= n; i++) {
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + (s.charAt(i + -1) == t_j ? 0 : 1));
            }
            int[] _d = p;
            p = d;
            d = _d;
        }
        return p[n];
    }

    public static int getLevenshteinDistance(CharSequence s, CharSequence t, int threshold) {
        int i = threshold;
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        } else if (i >= 0) {
            int n = s.length();
            int m = t.length();
            int _d = -1;
            if (n == 0) {
                if (m <= i) {
                    _d = m;
                }
                return _d;
            } else if (m == 0) {
                if (n <= i) {
                    _d = n;
                }
                return _d;
            } else {
                CharSequence s2;
                CharSequence t2;
                int i2;
                if (n > m) {
                    s2 = t;
                    t2 = s;
                    n = m;
                    m = t2.length();
                } else {
                    s2 = s;
                    t2 = t;
                }
                int[] p = new int[(n + 1)];
                int[] d = new int[(n + 1)];
                int boundary = Math.min(n, i) + 1;
                for (i2 = 0; i2 < boundary; i2++) {
                    p[i2] = i2;
                }
                int i3 = Integer.MAX_VALUE;
                Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
                Arrays.fill(d, Integer.MAX_VALUE);
                i2 = 1;
                while (i2 <= m) {
                    char t_j = t2.charAt(i2 - 1);
                    d[0] = i2;
                    int min = Math.max(1, i2 - i);
                    int max = Math.min(n, i2 + i);
                    if (min > max) {
                        return _d;
                    }
                    if (min > 1) {
                        d[min - 1] = i3;
                    }
                    for (int i4 = min; i4 <= max; i4++) {
                        if (s2.charAt(i4 - 1) == t_j) {
                            d[i4] = p[i4 - 1];
                        } else {
                            d[i4] = Math.min(Math.min(d[i4 - 1], p[i4]), p[i4 - 1]) + 1;
                        }
                    }
                    int[] _d2 = p;
                    p = d;
                    d = _d2;
                    i2++;
                    _d = -1;
                    i3 = Integer.MAX_VALUE;
                }
                if (p[n] <= i) {
                    return p[n];
                }
                return -1;
            }
        } else {
            throw new IllegalArgumentException("Threshold must not be negative");
        }
    }

    public static boolean startsWith(CharSequence str, CharSequence prefix) {
        return startsWith(str, prefix, false);
    }

    public static boolean startsWithIgnoreCase(CharSequence str, CharSequence prefix) {
        return startsWith(str, prefix, true);
    }

    private static boolean startsWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
        boolean z = false;
        if (str == null || prefix == null) {
            if (str == null && prefix == null) {
                z = true;
            }
            return z;
        } else if (prefix.length() > str.length()) {
            return false;
        } else {
            return CharSequenceUtils.regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length());
        }
    }

    public static boolean startsWithAny(CharSequence string, CharSequence... searchStrings) {
        if (isEmpty(string) || ArrayUtils.isEmpty((Object[]) searchStrings)) {
            return false;
        }
        for (CharSequence searchString : searchStrings) {
            if (startsWith(string, searchString)) {
                return true;
            }
        }
        return false;
    }

    public static boolean endsWith(CharSequence str, CharSequence suffix) {
        return endsWith(str, suffix, false);
    }

    public static boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return endsWith(str, suffix, true);
    }

    private static boolean endsWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
        boolean z = false;
        if (str == null || suffix == null) {
            if (str == null && suffix == null) {
                z = true;
            }
            return z;
        } else if (suffix.length() > str.length()) {
            return false;
        } else {
            return CharSequenceUtils.regionMatches(str, ignoreCase, str.length() - suffix.length(), suffix, 0, suffix.length());
        }
    }

    public static String normalizeSpace(String str) {
        if (str == null) {
            return null;
        }
        return WHITESPACE_BLOCK.matcher(trim(str)).replaceAll(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
    }

    public static boolean endsWithAny(CharSequence string, CharSequence... searchStrings) {
        if (isEmpty(string) || ArrayUtils.isEmpty((Object[]) searchStrings)) {
            return false;
        }
        for (CharSequence searchString : searchStrings) {
            if (endsWith(string, searchString)) {
                return true;
            }
        }
        return false;
    }

    public static String toString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
        String str;
        if (charsetName == null) {
            str = new String(bytes);
        } else {
            str = new String(bytes, charsetName);
        }
        return str;
    }
}
