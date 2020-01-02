package com.android.internal.telephony.uicc;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.text.format.DateFormat;
import com.android.internal.R;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class IccUtils {
    private static final char[] HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', DateFormat.CAPITAL_AM_PM, 'B', 'C', 'D', DateFormat.DAY, 'F'};
    static final String LOG_TAG = "IccUtils";

    public static String bcdToString(byte[] data, int offset, int length) {
        StringBuilder ret = new StringBuilder(length * 2);
        for (int i = offset; i < offset + length; i++) {
            int v = data[i] & 15;
            if (v > 9) {
                break;
            }
            ret.append((char) (v + 48));
            v = (data[i] >> 4) & 15;
            if (v != 15) {
                if (v > 9) {
                    break;
                }
                ret.append((char) (v + 48));
            }
        }
        return ret.toString();
    }

    public static String bcdToString(byte[] data) {
        return bcdToString(data, 0, data.length);
    }

    public static byte[] bcdToBytes(String bcd) {
        byte[] output = new byte[((bcd.length() + 1) / 2)];
        bcdToBytes(bcd, output);
        return output;
    }

    public static void bcdToBytes(String bcd, byte[] bytes) {
        bcdToBytes(bcd, bytes, 0);
    }

    public static void bcdToBytes(String bcd, byte[] bytes, int offset) {
        if (bcd.length() % 2 != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(bcd);
            stringBuilder.append("0");
            bcd = stringBuilder.toString();
        }
        int size = Math.min((bytes.length - offset) * 2, bcd.length());
        int i = 0;
        int j = offset;
        while (i + 1 < size) {
            bytes[j] = (byte) ((charToByte(bcd.charAt(i + 1)) << 4) | charToByte(bcd.charAt(i)));
            i += 2;
            j++;
        }
    }

    public static String bcdPlmnToString(byte[] data, int offset) {
        if (offset + 3 > data.length) {
            return null;
        }
        String ret = bytesToHexString(new byte[]{(byte) ((data[offset + 0] << 4) | ((data[offset + 0] >> 4) & 15)), (byte) ((data[offset + 1] << 4) | (data[offset + 2] & 15)), (byte) ((data[offset + 2] & 240) | ((data[offset + 1] >> 4) & 15))});
        String str = "F";
        if (ret.contains(str)) {
            ret = ret.replaceAll(str, "");
        }
        return ret;
    }

    public static void stringToBcdPlmn(String plmn, byte[] data, int offset) {
        char digit6 = plmn.length() > 5 ? plmn.charAt(5) : 'F';
        data[offset] = (byte) ((charToByte(plmn.charAt(1)) << 4) | charToByte(plmn.charAt(0)));
        data[offset + 1] = (byte) ((charToByte(digit6) << 4) | charToByte(plmn.charAt(2)));
        data[offset + 2] = (byte) ((charToByte(plmn.charAt(4)) << 4) | charToByte(plmn.charAt(3)));
    }

    public static String bchToString(byte[] data, int offset, int length) {
        StringBuilder ret = new StringBuilder(length * 2);
        for (int i = offset; i < offset + length; i++) {
            ret.append(HEX_CHARS[data[i] & 15]);
            ret.append(HEX_CHARS[(data[i] >> 4) & 15]);
        }
        return ret.toString();
    }

    public static String cdmaBcdToString(byte[] data, int offset, int length) {
        StringBuilder ret = new StringBuilder(length);
        int count = 0;
        int i = offset;
        while (count < length) {
            int v = data[i] & 15;
            if (v > 9) {
                v = 0;
            }
            ret.append((char) (v + 48));
            count++;
            if (count == length) {
                break;
            }
            v = (data[i] >> 4) & 15;
            if (v > 9) {
                v = 0;
            }
            ret.append((char) (v + 48));
            count++;
            i++;
        }
        return ret.toString();
    }

    public static int gsmBcdByteToInt(byte b) {
        int ret = 0;
        if ((b & 240) <= 144) {
            ret = (b >> 4) & 15;
        }
        if ((b & 15) <= 9) {
            return ret + ((b & 15) * 10);
        }
        return ret;
    }

    public static int cdmaBcdByteToInt(byte b) {
        int ret = 0;
        if ((b & 240) <= 144) {
            ret = ((b >> 4) & 15) * 10;
        }
        if ((b & 15) <= 9) {
            return ret + (b & 15);
        }
        return ret;
    }

    public static String adnStringFieldToString(byte[] data, int offset, int length) {
        if (length == 0) {
            return "";
        }
        if (length >= 1 && data[offset] == Byte.MIN_VALUE) {
            String ret = null;
            try {
                ret = new String(data, offset + 1, ((length - 1) / 2) * 2, "utf-16be");
            } catch (UnsupportedEncodingException ex) {
                Rlog.e(LOG_TAG, "implausible UnsupportedEncodingException", ex);
            }
            if (ret != null) {
                int ucslen = ret.length();
                while (ucslen > 0 && ret.charAt(ucslen - 1) == 65535) {
                    ucslen--;
                }
                return ret.substring(0, ucslen);
            }
        }
        boolean isucs2 = false;
        char base = 0;
        int len = 0;
        if (length >= 3 && data[offset] == (byte) -127) {
            len = data[offset + 1] & 255;
            if (len > length - 3) {
                len = length - 3;
            }
            base = (char) ((data[offset + 2] & 255) << 7);
            offset += 3;
            isucs2 = true;
        } else if (length >= 4 && data[offset] == (byte) -126) {
            len = data[offset + 1] & 255;
            if (len > length - 4) {
                len = length - 4;
            }
            base = (char) (((data[offset + 2] & 255) << 8) | (data[offset + 3] & 255));
            offset += 4;
            isucs2 = true;
        }
        if (isucs2) {
            StringBuilder ret2 = new StringBuilder();
            while (len > 0) {
                if (data[offset] < (byte) 0) {
                    ret2.append((char) ((data[offset] & 127) + base));
                    offset++;
                    len--;
                }
                int count = 0;
                while (count < len && data[offset + count] >= (byte) 0) {
                    count++;
                }
                ret2.append(GsmAlphabet.gsm8BitUnpackedToString(data, offset, count));
                offset += count;
                len -= count;
            }
            return ret2.toString();
        }
        String defaultCharset = "";
        try {
            defaultCharset = Resources.getSystem().getString(R.string.gsm_alphabet_default_charset);
        } catch (NotFoundException e) {
        }
        return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length, defaultCharset.trim());
    }

    public static int hexCharToInt(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= DateFormat.CAPITAL_AM_PM && c <= 'F') {
            return (c - 65) + 10;
        }
        if (c >= DateFormat.AM_PM && c <= 'f') {
            return (c - 97) + 10;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("invalid hex char '");
        stringBuilder.append(c);
        stringBuilder.append("'");
        throw new RuntimeException(stringBuilder.toString());
    }

    public static byte[] hexStringToBytes(String s) {
        if (s == null) {
            return null;
        }
        int sz = s.length();
        byte[] ret = new byte[(sz / 2)];
        for (int i = 0; i < sz; i += 2) {
            ret[i / 2] = (byte) ((hexCharToInt(s.charAt(i)) << 4) | hexCharToInt(s.charAt(i + 1)));
        }
        return ret;
    }

    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_CHARS[(bytes[i] >> 4) & 15]);
            ret.append(HEX_CHARS[bytes[i] & 15]);
        }
        return ret.toString();
    }

    public static String networkNameToString(byte[] data, int offset, int length) {
        if ((data[offset] & 128) != 128 || length < 1) {
            return "";
        }
        String ret;
        int i = (data[offset] >>> 4) & 7;
        if (i == 0) {
            String ret2 = offset + 1;
            ret = GsmAlphabet.gsm7BitPackedToString(data, ret2, (((length - 1) * 8) - (data[offset] & 7)) / 7);
        } else if (i != 1) {
            ret = "";
        } else {
            try {
                ret = new String(data, offset + 1, length - 1, "utf-16");
            } catch (UnsupportedEncodingException ex) {
                Rlog.e(LOG_TAG, "implausible UnsupportedEncodingException", ex);
                ret = "";
            }
        }
        i = data[offset];
        return ret;
    }

    public static Bitmap parseToBnW(byte[] data, int length) {
        int valueIndex = 0 + 1;
        int width = data[0] & 255;
        byte currentByte = valueIndex + 1;
        valueIndex = data[valueIndex] & 255;
        int numOfPixels = width * valueIndex;
        int[] pixels = new int[numOfPixels];
        int pixelIndex = 0;
        int bitIndex = 7;
        byte currentByte2 = (byte) 0;
        while (pixelIndex < numOfPixels) {
            if (pixelIndex % 8 == 0) {
                byte valueIndex2 = currentByte + 1;
                bitIndex = 7;
                currentByte2 = data[currentByte];
                currentByte = valueIndex2;
            }
            int pixelIndex2 = pixelIndex + 1;
            int bitIndex2 = bitIndex - 1;
            pixels[pixelIndex] = bitToRGB((currentByte2 >> bitIndex) & 1);
            pixelIndex = pixelIndex2;
            bitIndex = bitIndex2;
        }
        if (pixelIndex != numOfPixels) {
            Rlog.e(LOG_TAG, "parse end and size error");
        }
        return Bitmap.createBitmap(pixels, width, valueIndex, Config.ARGB_8888);
    }

    private static int bitToRGB(int bit) {
        if (bit == 1) {
            return -1;
        }
        return -16777216;
    }

    public static Bitmap parseToRGB(byte[] data, int length, boolean transparency) {
        int[] resultArray;
        int valueIndex = 0 + 1;
        int width = data[0] & 255;
        int valueIndex2 = valueIndex + 1;
        valueIndex = data[valueIndex] & 255;
        int valueIndex3 = valueIndex2 + 1;
        valueIndex2 = data[valueIndex2] & 255;
        int valueIndex4 = valueIndex3 + 1;
        valueIndex3 = data[valueIndex3] & 255;
        int valueIndex5 = valueIndex4 + 1;
        int valueIndex6 = valueIndex5 + 1;
        int[] colorIndexArray = getCLUT(data, ((data[valueIndex4] & 255) << 8) | (data[valueIndex5] & 255), valueIndex3);
        if (true == transparency) {
            colorIndexArray[valueIndex3 - 1] = 0;
        }
        if (8 % valueIndex2 == 0) {
            resultArray = mapTo2OrderBitColor(data, valueIndex6, width * valueIndex, colorIndexArray, valueIndex2);
        } else {
            resultArray = mapToNon2OrderBitColor(data, valueIndex6, width * valueIndex, colorIndexArray, valueIndex2);
        }
        return Bitmap.createBitmap(resultArray, width, valueIndex, Config.RGB_565);
    }

    private static int[] mapTo2OrderBitColor(byte[] data, int tempByte, int length, int[] colorArray, int bits) {
        if (8 % bits != 0) {
            Rlog.e(LOG_TAG, "not event number of color");
            return mapToNon2OrderBitColor(data, tempByte, length, colorArray, bits);
        }
        int mask = 1;
        if (bits == 1) {
            mask = 1;
        } else if (bits == 2) {
            mask = 3;
        } else if (bits == 4) {
            mask = 15;
        } else if (bits == 8) {
            mask = 255;
        }
        int[] resultArray = new int[length];
        int resultIndex = 0;
        int run = 8 / bits;
        while (resultIndex < length) {
            byte valueIndex = tempByte + 1;
            byte tempByte2 = data[tempByte2];
            int runIndex = 0;
            while (runIndex < run) {
                int resultIndex2 = resultIndex + 1;
                resultArray[resultIndex] = colorArray[(tempByte2 >> (((run - runIndex) - 1) * bits)) & mask];
                runIndex++;
                resultIndex = resultIndex2;
            }
            tempByte2 = valueIndex;
        }
        return resultArray;
    }

    private static int[] mapToNon2OrderBitColor(byte[] data, int valueIndex, int length, int[] colorArray, int bits) {
        if (8 % bits != 0) {
            return new int[length];
        }
        Rlog.e(LOG_TAG, "not odd number of color");
        return mapTo2OrderBitColor(data, valueIndex, length, colorArray, bits);
    }

    private static int[] getCLUT(byte[] rawData, int offset, int number) {
        if (rawData == null) {
            return null;
        }
        int[] result = new int[number];
        int endIndex = (number * 3) + offset;
        int valueIndex = offset;
        int colorIndex = 0;
        while (true) {
            int colorIndex2 = colorIndex + 1;
            int valueIndex2 = valueIndex + 1;
            int valueIndex3 = valueIndex2 + 1;
            valueIndex = (((rawData[valueIndex] & 255) << 16) | -16777216) | ((rawData[valueIndex2] & 255) << 8);
            valueIndex2 = valueIndex3 + 1;
            result[colorIndex] = valueIndex | (rawData[valueIndex3] & 255);
            if (valueIndex2 >= endIndex) {
                return result;
            }
            colorIndex = colorIndex2;
            valueIndex = valueIndex2;
        }
    }

    public static String getDecimalSubstring(String iccId) {
        int position = 0;
        while (position < iccId.length() && Character.isDigit(iccId.charAt(position))) {
            position++;
        }
        return iccId.substring(0, position);
    }

    public static int bytesToInt(byte[] src, int offset, int length) {
        StringBuilder stringBuilder;
        if (length > 4) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("length must be <= 4 (only 32-bit integer supported): ");
            stringBuilder.append(length);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (offset < 0 || length < 0 || offset + length > src.length) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Out of the bounds: src=[");
            stringBuilder.append(src.length);
            stringBuilder.append("], offset=");
            stringBuilder.append(offset);
            stringBuilder.append(", length=");
            stringBuilder.append(length);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        } else {
            int result = 0;
            for (int i = 0; i < length; i++) {
                result = (result << 8) | (src[offset + i] & 255);
            }
            if (result >= 0) {
                return result;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("src cannot be parsed as a positive integer: ");
            stringBuilder2.append(result);
            throw new IllegalArgumentException(stringBuilder2.toString());
        }
    }

    public static long bytesToRawLong(byte[] src, int offset, int length) {
        StringBuilder stringBuilder;
        if (length > 8) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("length must be <= 8 (only 64-bit long supported): ");
            stringBuilder.append(length);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (offset < 0 || length < 0 || offset + length > src.length) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Out of the bounds: src=[");
            stringBuilder.append(src.length);
            stringBuilder.append("], offset=");
            stringBuilder.append(offset);
            stringBuilder.append(", length=");
            stringBuilder.append(length);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        } else {
            long result = 0;
            for (int i = 0; i < length; i++) {
                result = (result << 8) | ((long) (src[offset + i] & 255));
            }
            return result;
        }
    }

    public static byte[] unsignedIntToBytes(int value) {
        if (value >= 0) {
            byte[] bytes = new byte[byteNumForUnsignedInt(value)];
            unsignedIntToBytes(value, bytes, 0);
            return bytes;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("value must be 0 or positive: ");
        stringBuilder.append(value);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static byte[] signedIntToBytes(int value) {
        if (value >= 0) {
            byte[] bytes = new byte[byteNumForSignedInt(value)];
            signedIntToBytes(value, bytes, 0);
            return bytes;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("value must be 0 or positive: ");
        stringBuilder.append(value);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static int unsignedIntToBytes(int value, byte[] dest, int offset) {
        return intToBytes(value, dest, offset, false);
    }

    public static int signedIntToBytes(int value, byte[] dest, int offset) {
        return intToBytes(value, dest, offset, true);
    }

    public static int byteNumForUnsignedInt(int value) {
        return byteNumForInt(value, false);
    }

    public static int byteNumForSignedInt(int value) {
        return byteNumForInt(value, true);
    }

    private static int intToBytes(int value, byte[] dest, int offset, boolean signed) {
        int l = byteNumForInt(value, signed);
        if (offset < 0 || offset + l > dest.length) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Not enough space to write. Required bytes: ");
            stringBuilder.append(l);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }
        int i = l - 1;
        int v = value;
        while (i >= 0) {
            dest[offset + i] = (byte) (v & 255);
            i--;
            v >>>= 8;
        }
        return l;
    }

    private static int byteNumForInt(int value, boolean signed) {
        if (value >= 0) {
            if (signed) {
                if (value <= 127) {
                    return 1;
                }
                if (value <= 32767) {
                    return 2;
                }
                if (value <= 8388607) {
                    return 3;
                }
            } else if (value <= 255) {
                return 1;
            } else {
                if (value <= 65535) {
                    return 2;
                }
                if (value <= 16777215) {
                    return 3;
                }
            }
            return 4;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("value must be 0 or positive: ");
        stringBuilder.append(value);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static byte countTrailingZeros(byte b) {
        if (b == (byte) 0) {
            return (byte) 8;
        }
        int v = b & 255;
        byte c = (byte) 7;
        if ((v & 15) != 0) {
            c = (byte) (7 - 4);
        }
        if ((v & 51) != 0) {
            c = (byte) (c - 2);
        }
        if ((v & 85) != 0) {
            c = (byte) (c - 1);
        }
        return c;
    }

    public static String byteToHex(byte b) {
        r1 = new char[2];
        char[] cArr = HEX_CHARS;
        r1[0] = cArr[(b & 255) >>> 4];
        r1[1] = cArr[b & 15];
        return new String(r1);
    }

    public static String stripTrailingFs(String s) {
        String stripStr = s == null ? null : s.replaceAll("(?i)f*$", "");
        return TextUtils.isEmpty(stripStr) ? s : stripStr;
    }

    private static byte charToByte(char c) {
        if (c >= '0' && c <= '9') {
            return (byte) (c - 48);
        }
        if (c >= DateFormat.CAPITAL_AM_PM && c <= 'F') {
            return (byte) (c - 55);
        }
        if (c < DateFormat.AM_PM || c > 'f') {
            return (byte) 0;
        }
        return (byte) (c - 87);
    }

    static byte[] stringToAdnStringField(String alphaTag) {
        boolean isUcs2 = false;
        int i = 0;
        while (i < alphaTag.length()) {
            try {
                GsmAlphabet.countGsmSeptets(alphaTag.charAt(i), true);
                i++;
            } catch (EncodeException e) {
                isUcs2 = true;
            }
        }
        return stringToAdnStringField(alphaTag, isUcs2);
    }

    static byte[] stringToAdnStringField(String alphaTag, boolean isUcs2) {
        if (!isUcs2) {
            return GsmAlphabet.stringToGsm8BitPacked(alphaTag);
        }
        byte[] alphaTagBytes = alphaTag.getBytes(Charset.forName("UTF-16BE"));
        byte[] ret = new byte[(alphaTagBytes.length + 1)];
        ret[0] = Byte.MIN_VALUE;
        System.arraycopy(alphaTagBytes, 0, ret, 1, alphaTagBytes.length);
        return ret;
    }
}
