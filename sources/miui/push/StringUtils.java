package miui.push;

import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import miui.maml.util.net.SimpleRequest;
import org.apache.miui.commons.lang3.CharUtils;

public class StringUtils {
    private static final char[] AMP_ENCODE = "&amp;".toCharArray();
    private static final char[] APOS_ENCODE = "&apos;".toCharArray();
    private static final char[] GT_ENCODE = "&gt;".toCharArray();
    private static final char[] LT_ENCODE = "&lt;".toCharArray();
    private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
    private static char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static Random randGen = new Random();

    public static String escapeForXML(String string) {
        if (string == null) {
            return null;
        }
        int i = 0;
        int last = 0;
        char[] input = string.toCharArray();
        int len = input.length;
        StringBuilder out = new StringBuilder((int) (((double) len) * 1.3d));
        while (i < len) {
            char ch = input[i];
            if (ch <= '>') {
                if (ch == '<') {
                    if (i > last) {
                        out.append(input, last, i - last);
                    }
                    last = i + 1;
                    out.append(LT_ENCODE);
                } else if (ch == '>') {
                    if (i > last) {
                        out.append(input, last, i - last);
                    }
                    last = i + 1;
                    out.append(GT_ENCODE);
                } else if (ch == '&') {
                    if (i > last) {
                        out.append(input, last, i - last);
                    }
                    if (len <= i + 5 || input[i + 1] != '#' || !Character.isDigit(input[i + 2]) || !Character.isDigit(input[i + 3]) || !Character.isDigit(input[i + 4]) || input[i + 5] != ';') {
                        last = i + 1;
                        out.append(AMP_ENCODE);
                    }
                } else if (ch == '\"') {
                    if (i > last) {
                        out.append(input, last, i - last);
                    }
                    last = i + 1;
                    out.append(QUOTE_ENCODE);
                } else if (ch == DateFormat.QUOTE) {
                    if (i > last) {
                        out.append(input, last, i - last);
                    }
                    last = i + 1;
                    out.append(APOS_ENCODE);
                }
            }
            i++;
        }
        if (last == 0) {
            return string;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    public static final String unescapeFromXML(String string) {
        return replace(replace(replace(replace(replace(string, "&lt;", "<"), "&gt;", ">"), "&quot;", "\""), "&apos;", "'"), "&amp;", "&");
    }

    public static final String replace(String string, String oldString, String newString) {
        if (string == null) {
            return null;
        }
        int indexOf = string.indexOf(oldString, 0);
        int i = indexOf;
        if (indexOf < 0) {
            return string;
        }
        char[] string2 = string.toCharArray();
        char[] newString2 = newString.toCharArray();
        int oLength = oldString.length();
        StringBuilder buf = new StringBuilder(string2.length);
        buf.append(string2, 0, i);
        buf.append(newString2);
        while (true) {
            i += oLength;
            int j = i;
            int indexOf2 = string.indexOf(oldString, i);
            i = indexOf2;
            if (indexOf2 > 0) {
                buf.append(string2, j, i - j);
                buf.append(newString2);
            } else {
                buf.append(string2, j, string2.length - j);
                return buf.toString();
            }
        }
    }

    public static String encodeBase64(String data) {
        byte[] bytes = null;
        try {
            bytes = data.getBytes(SimpleRequest.ISO_8859_1);
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
        return encodeBase64(bytes);
    }

    public static String encodeBase64(byte[] data) {
        return encodeBase64(data, false);
    }

    public static String encodeBase64(byte[] data, boolean lineBreaks) {
        return encodeBase64(data, 0, data.length, lineBreaks);
    }

    public static String encodeBase64(byte[] data, int offset, int len, boolean lineBreaks) {
        int i;
        if (lineBreaks) {
            i = 0;
        } else {
            i = 2;
        }
        return Base64.encodeToString(data, offset, len, i);
    }

    public static String randomString(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    public static String stripInvalidXMLChars(String in) {
        if (TextUtils.isEmpty(in)) {
            return in;
        }
        StringBuilder out = new StringBuilder(in.length());
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (isValidXmlChar(c)) {
                out.append(c);
            }
        }
        return out.toString();
    }

    public static boolean isValidXmlChar(char c) {
        return (c >= ' ' && c <= 55295) || ((c >= 57344 && c <= 65533) || ((c >= 0 && c <= 65535) || c == 9 || c == 10 || c == CharUtils.CR));
    }

    private StringUtils() {
    }

    public static String getMd5Digest(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(KeyProperties.DIGEST_MD5);
            digest.update(getBytes(input));
            BigInteger hashInt = new BigInteger(1, digest.digest());
            return String.format("%1$032X", new Object[]{hashInt});
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getBytes(String s) {
        try {
            return s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return s.getBytes();
        }
    }
}
