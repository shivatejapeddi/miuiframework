package com.android.internal.util;

import android.annotation.UnsupportedAppUsage;
import android.app.slice.Slice;
import android.app.slice.SliceItem;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Xml;
import com.android.ims.ImsConfig;
import com.miui.enterprise.sdk.ApplicationManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class XmlUtils {
    private static final String STRING_ARRAY_SEPARATOR = ":";

    public interface ReadMapCallback {
        Object readThisUnknownObjectXml(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException;
    }

    public interface WriteMapCallback {
        void writeUnknownObject(Object obj, String str, XmlSerializer xmlSerializer) throws XmlPullParserException, IOException;
    }

    @UnsupportedAppUsage
    public static void skipCurrentTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1) {
                return;
            }
            if (type == 3) {
                if (parser.getDepth() <= outerDepth) {
                    return;
                }
            }
        }
    }

    public static final int convertValueToList(CharSequence value, String[] options, int defaultValue) {
        if (!TextUtils.isEmpty(value)) {
            for (int i = 0; i < options.length; i++) {
                if (value.equals(options[i])) {
                    return i;
                }
            }
        }
        return defaultValue;
    }

    @UnsupportedAppUsage
    public static final boolean convertValueToBoolean(CharSequence value, boolean defaultValue) {
        boolean result = false;
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        if (value.equals("1") || value.equals("true") || value.equals("TRUE")) {
            result = true;
        }
        return result;
    }

    @UnsupportedAppUsage
    public static final int convertValueToInt(CharSequence charSeq, int defaultValue) {
        if (TextUtils.isEmpty(charSeq)) {
            return defaultValue;
        }
        String nm = charSeq.toString();
        int sign = 1;
        int index = 0;
        int len = nm.length();
        int base = 10;
        if ('-' == nm.charAt(0)) {
            sign = -1;
            index = 0 + 1;
        }
        if ('0' == nm.charAt(index)) {
            if (index == len - 1) {
                return 0;
            }
            char c = nm.charAt(index + 1);
            if (StateProperty.TARGET_X == c || 'X' == c) {
                index += 2;
                base = 16;
            } else {
                index++;
                base = 8;
            }
        } else if ('#' == nm.charAt(index)) {
            index++;
            base = 16;
        }
        return Integer.parseInt(nm.substring(index), base) * sign;
    }

    public static int convertValueToUnsignedInt(String value, int defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        return parseUnsignedIntAttribute(value);
    }

    public static int parseUnsignedIntAttribute(CharSequence charSeq) {
        String value = charSeq.toString();
        int index = 0;
        int len = value.length();
        int base = 10;
        if ('0' == value.charAt(0)) {
            if (0 == len - 1) {
                return 0;
            }
            char c = value.charAt(0 + 1);
            if (StateProperty.TARGET_X == c || 'X' == c) {
                index = 0 + 2;
                base = 16;
            } else {
                index = 0 + 1;
                base = 8;
            }
        } else if ('#' == value.charAt(0)) {
            index = 0 + 1;
            base = 16;
        }
        return (int) Long.parseLong(value.substring(index), base);
    }

    @UnsupportedAppUsage
    public static final void writeMapXml(Map val, OutputStream out) throws XmlPullParserException, IOException {
        XmlSerializer serializer = new FastXmlSerializer();
        serializer.setOutput(out, StandardCharsets.UTF_8.name());
        serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        writeMapXml(val, null, serializer);
        serializer.endDocument();
    }

    public static final void writeListXml(List val, OutputStream out) throws XmlPullParserException, IOException {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, StandardCharsets.UTF_8.name());
        serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        writeListXml(val, null, serializer);
        serializer.endDocument();
    }

    public static final void writeMapXml(Map val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        writeMapXml(val, name, out, null);
    }

    public static final void writeMapXml(Map val, String name, XmlSerializer out, WriteMapCallback callback) throws XmlPullParserException, IOException {
        String str;
        if (val == null) {
            str = "null";
            out.startTag(null, str);
            out.endTag(null, str);
            return;
        }
        str = "map";
        out.startTag(null, str);
        if (name != null) {
            out.attribute(null, "name", name);
        }
        writeMapXml(val, out, callback);
        out.endTag(null, str);
    }

    public static final void writeMapXml(Map val, XmlSerializer out, WriteMapCallback callback) throws XmlPullParserException, IOException {
        if (val != null) {
            for (Entry e : val.entrySet()) {
                writeValueXml(e.getValue(), (String) e.getKey(), out, callback);
            }
        }
    }

    public static final void writeListXml(List val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        String str;
        if (val == null) {
            str = "null";
            out.startTag(null, str);
            out.endTag(null, str);
            return;
        }
        str = Slice.HINT_LIST;
        out.startTag(null, str);
        if (name != null) {
            out.attribute(null, "name", name);
        }
        int N = val.size();
        for (int i = 0; i < N; i++) {
            writeValueXml(val.get(i), null, out);
        }
        out.endTag(null, str);
    }

    public static final void writeSetXml(Set val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        String str;
        if (val == null) {
            str = "null";
            out.startTag(null, str);
            out.endTag(null, str);
            return;
        }
        str = "set";
        out.startTag(null, str);
        if (name != null) {
            out.attribute(null, "name", name);
        }
        for (Object v : val) {
            writeValueXml(v, null, out);
        }
        out.endTag(null, str);
    }

    public static final void writeByteArrayXml(byte[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        String str;
        if (val == null) {
            str = "null";
            out.startTag(null, str);
            out.endTag(null, str);
            return;
        }
        str = "byte-array";
        out.startTag(null, str);
        if (name != null) {
            out.attribute(null, "name", name);
        }
        out.attribute(null, "num", Integer.toString(N));
        StringBuilder sb = new StringBuilder(val.length * 2);
        for (int b : val) {
            int h = (b >> 4) & 15;
            sb.append((char) (h >= 10 ? (h + 97) - 10 : h + 48));
            h = b & 15;
            sb.append((char) (h >= 10 ? (h + 97) - 10 : h + 48));
        }
        out.text(sb.toString());
        out.endTag(null, str);
    }

    public static final void writeIntArrayXml(int[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        String str;
        if (val == null) {
            str = "null";
            out.startTag(null, str);
            out.endTag(null, str);
            return;
        }
        str = "int-array";
        out.startTag(null, str);
        if (name != null) {
            out.attribute(null, "name", name);
        }
        out.attribute(null, "num", Integer.toString(N));
        for (int num : val) {
            String str2 = ImsConfig.EXTRA_CHANGED_ITEM;
            out.startTag(null, str2);
            out.attribute(null, "value", Integer.toString(num));
            out.endTag(null, str2);
        }
        out.endTag(null, str);
    }

    public static final void writeLongArrayXml(long[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        String str;
        if (val == null) {
            str = "null";
            out.startTag(null, str);
            out.endTag(null, str);
            return;
        }
        str = "long-array";
        out.startTag(null, str);
        if (name != null) {
            out.attribute(null, "name", name);
        }
        out.attribute(null, "num", Integer.toString(N));
        for (long l : val) {
            String str2 = ImsConfig.EXTRA_CHANGED_ITEM;
            out.startTag(null, str2);
            out.attribute(null, "value", Long.toString(l));
            out.endTag(null, str2);
        }
        out.endTag(null, str);
    }

    public static final void writeDoubleArrayXml(double[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        String str;
        if (val == null) {
            str = "null";
            out.startTag(null, str);
            out.endTag(null, str);
            return;
        }
        str = "double-array";
        out.startTag(null, str);
        if (name != null) {
            out.attribute(null, "name", name);
        }
        out.attribute(null, "num", Integer.toString(N));
        for (double d : val) {
            String str2 = ImsConfig.EXTRA_CHANGED_ITEM;
            out.startTag(null, str2);
            out.attribute(null, "value", Double.toString(d));
            out.endTag(null, str2);
        }
        out.endTag(null, str);
    }

    public static final void writeStringArrayXml(String[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        String str;
        if (val == null) {
            str = "null";
            out.startTag(null, str);
            out.endTag(null, str);
            return;
        }
        str = "string-array";
        out.startTag(null, str);
        if (name != null) {
            out.attribute(null, "name", name);
        }
        out.attribute(null, "num", Integer.toString(N));
        for (String attribute : val) {
            String str2 = ImsConfig.EXTRA_CHANGED_ITEM;
            out.startTag(null, str2);
            out.attribute(null, "value", attribute);
            out.endTag(null, str2);
        }
        out.endTag(null, str);
    }

    public static final void writeBooleanArrayXml(boolean[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        String str;
        if (val == null) {
            str = "null";
            out.startTag(null, str);
            out.endTag(null, str);
            return;
        }
        str = "boolean-array";
        out.startTag(null, str);
        if (name != null) {
            out.attribute(null, "name", name);
        }
        out.attribute(null, "num", Integer.toString(N));
        for (boolean bool : val) {
            String str2 = ImsConfig.EXTRA_CHANGED_ITEM;
            out.startTag(null, str2);
            out.attribute(null, "value", Boolean.toString(bool));
            out.endTag(null, str2);
        }
        out.endTag(null, str);
    }

    public static final void writeValueXml(Object v, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        writeValueXml(v, name, out, null);
    }

    private static final void writeValueXml(Object v, String name, XmlSerializer out, WriteMapCallback callback) throws XmlPullParserException, IOException {
        String str = "name";
        String str2;
        if (v == null) {
            str2 = "null";
            out.startTag(null, str2);
            if (name != null) {
                out.attribute(null, str, name);
            }
            out.endTag(null, str2);
            return;
        }
        String str3 = "string";
        if (v instanceof String) {
            out.startTag(null, str3);
            if (name != null) {
                out.attribute(null, str, name);
            }
            out.text(v.toString());
            out.endTag(null, str3);
            return;
        }
        if (v instanceof Integer) {
            str2 = SliceItem.FORMAT_INT;
        } else if (v instanceof Long) {
            str2 = "long";
        } else if (v instanceof Float) {
            str2 = ApplicationManager.FLOAT;
        } else if (v instanceof Double) {
            str2 = "double";
        } else if (v instanceof Boolean) {
            str2 = "boolean";
        } else if (v instanceof byte[]) {
            writeByteArrayXml((byte[]) v, name, out);
            return;
        } else if (v instanceof int[]) {
            writeIntArrayXml((int[]) v, name, out);
            return;
        } else if (v instanceof long[]) {
            writeLongArrayXml((long[]) v, name, out);
            return;
        } else if (v instanceof double[]) {
            writeDoubleArrayXml((double[]) v, name, out);
            return;
        } else if (v instanceof String[]) {
            writeStringArrayXml((String[]) v, name, out);
            return;
        } else if (v instanceof boolean[]) {
            writeBooleanArrayXml((boolean[]) v, name, out);
            return;
        } else if (v instanceof Map) {
            writeMapXml((Map) v, name, out);
            return;
        } else if (v instanceof List) {
            writeListXml((List) v, name, out);
            return;
        } else if (v instanceof Set) {
            writeSetXml((Set) v, name, out);
            return;
        } else if (v instanceof CharSequence) {
            out.startTag(null, str3);
            if (name != null) {
                out.attribute(null, str, name);
            }
            out.text(v.toString());
            out.endTag(null, str3);
            return;
        } else if (callback != null) {
            callback.writeUnknownObject(v, name, out);
            return;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("writeValueXml: unable to write value ");
            stringBuilder.append(v);
            throw new RuntimeException(stringBuilder.toString());
        }
        out.startTag(null, str2);
        if (name != null) {
            out.attribute(null, str, name);
        }
        out.attribute(null, "value", v.toString());
        out.endTag(null, str2);
    }

    @UnsupportedAppUsage
    public static final HashMap<String, ?> readMapXml(InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, StandardCharsets.UTF_8.name());
        return (HashMap) readValueXml(parser, new String[1]);
    }

    public static final ArrayList readListXml(InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, StandardCharsets.UTF_8.name());
        return (ArrayList) readValueXml(parser, new String[1]);
    }

    public static final HashSet readSetXml(InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, null);
        return (HashSet) readValueXml(parser, new String[1]);
    }

    public static final HashMap<String, ?> readThisMapXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        return readThisMapXml(parser, endTag, name, null);
    }

    public static final HashMap<String, ?> readThisMapXml(XmlPullParser parser, String endTag, String[] name, ReadMapCallback callback) throws XmlPullParserException, IOException {
        HashMap<String, Object> map = new HashMap();
        int eventType = parser.getEventType();
        while (true) {
            StringBuilder stringBuilder;
            if (eventType == 2) {
                map.put(name[0], readThisValueXml(parser, name, callback, false));
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return map;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Expected ");
                stringBuilder.append(endTag);
                stringBuilder.append(" end tag at: ");
                stringBuilder.append(parser.getName());
                throw new XmlPullParserException(stringBuilder.toString());
            }
            eventType = parser.next();
            if (eventType == 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Document ended before ");
                stringBuilder.append(endTag);
                stringBuilder.append(" end tag");
                throw new XmlPullParserException(stringBuilder.toString());
            }
        }
    }

    public static final ArrayMap<String, ?> readThisArrayMapXml(XmlPullParser parser, String endTag, String[] name, ReadMapCallback callback) throws XmlPullParserException, IOException {
        ArrayMap<String, Object> map = new ArrayMap();
        int eventType = parser.getEventType();
        while (true) {
            StringBuilder stringBuilder;
            if (eventType == 2) {
                map.put(name[0], readThisValueXml(parser, name, callback, true));
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return map;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Expected ");
                stringBuilder.append(endTag);
                stringBuilder.append(" end tag at: ");
                stringBuilder.append(parser.getName());
                throw new XmlPullParserException(stringBuilder.toString());
            }
            eventType = parser.next();
            if (eventType == 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Document ended before ");
                stringBuilder.append(endTag);
                stringBuilder.append(" end tag");
                throw new XmlPullParserException(stringBuilder.toString());
            }
        }
    }

    public static final ArrayList readThisListXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        return readThisListXml(parser, endTag, name, null, false);
    }

    private static final ArrayList readThisListXml(XmlPullParser parser, String endTag, String[] name, ReadMapCallback callback, boolean arrayMap) throws XmlPullParserException, IOException {
        ArrayList list = new ArrayList();
        int eventType = parser.getEventType();
        while (true) {
            StringBuilder stringBuilder;
            if (eventType == 2) {
                list.add(readThisValueXml(parser, name, callback, arrayMap));
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return list;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Expected ");
                stringBuilder.append(endTag);
                stringBuilder.append(" end tag at: ");
                stringBuilder.append(parser.getName());
                throw new XmlPullParserException(stringBuilder.toString());
            }
            eventType = parser.next();
            if (eventType == 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Document ended before ");
                stringBuilder.append(endTag);
                stringBuilder.append(" end tag");
                throw new XmlPullParserException(stringBuilder.toString());
            }
        }
    }

    public static final HashSet readThisSetXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        return readThisSetXml(parser, endTag, name, null, false);
    }

    private static final HashSet readThisSetXml(XmlPullParser parser, String endTag, String[] name, ReadMapCallback callback, boolean arrayMap) throws XmlPullParserException, IOException {
        HashSet set = new HashSet();
        int eventType = parser.getEventType();
        while (true) {
            StringBuilder stringBuilder;
            if (eventType == 2) {
                set.add(readThisValueXml(parser, name, callback, arrayMap));
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return set;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Expected ");
                stringBuilder.append(endTag);
                stringBuilder.append(" end tag at: ");
                stringBuilder.append(parser.getName());
                throw new XmlPullParserException(stringBuilder.toString());
            }
            eventType = parser.next();
            if (eventType == 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Document ended before ");
                stringBuilder.append(endTag);
                stringBuilder.append(" end tag");
                throw new XmlPullParserException(stringBuilder.toString());
            }
        }
    }

    public static final byte[] readThisByteArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            String values;
            StringBuilder stringBuilder;
            int num = Integer.parseInt(parser.getAttributeValue(null, "num"));
            byte[] array = new byte[num];
            int eventType = parser.getEventType();
            while (true) {
                if (eventType == 4) {
                    if (num > 0) {
                        values = parser.getText();
                        if (values == null || values.length() != num * 2) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Invalid value found in byte-array: ");
                            stringBuilder.append(values);
                        } else {
                            for (int i = 0; i < num; i++) {
                                int nibbleHigh;
                                int nibbleLow;
                                char nibbleHighChar = values.charAt(i * 2);
                                char nibbleLowChar = values.charAt((i * 2) + 1);
                                if (nibbleHighChar > DateFormat.AM_PM) {
                                    nibbleHigh = (nibbleHighChar - 97) + 10;
                                } else {
                                    nibbleHigh = nibbleHighChar - 48;
                                }
                                if (nibbleLowChar > DateFormat.AM_PM) {
                                    nibbleLow = (nibbleLowChar - 97) + 10;
                                } else {
                                    nibbleLow = nibbleLowChar - 48;
                                }
                                array[i] = (byte) (((nibbleHigh & 15) << 4) | (nibbleLow & 15));
                            }
                        }
                    }
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Expected ");
                    stringBuilder.append(endTag);
                    stringBuilder.append(" end tag at: ");
                    stringBuilder.append(parser.getName());
                    throw new XmlPullParserException(stringBuilder.toString());
                }
                eventType = parser.next();
                if (eventType == 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Document ended before ");
                    stringBuilder.append(endTag);
                    stringBuilder.append(" end tag");
                    throw new XmlPullParserException(stringBuilder.toString());
                }
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid value found in byte-array: ");
            stringBuilder.append(values);
            throw new XmlPullParserException(stringBuilder.toString());
        } catch (NullPointerException e) {
            throw new XmlPullParserException("Need num attribute in byte-array");
        } catch (NumberFormatException e2) {
            throw new XmlPullParserException("Not a number in num attribute in byte-array");
        }
    }

    public static final int[] readThisIntArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            int num = Integer.parseInt(parser.getAttributeValue(null, "num"));
            parser.next();
            int[] array = new int[num];
            int i = 0;
            int eventType = parser.getEventType();
            while (true) {
                StringBuilder stringBuilder;
                String str = ImsConfig.EXTRA_CHANGED_ITEM;
                if (eventType == 2) {
                    if (parser.getName().equals(str)) {
                        try {
                            array[i] = Integer.parseInt(parser.getAttributeValue(null, "value"));
                        } catch (NullPointerException e) {
                            throw new XmlPullParserException("Need value attribute in item");
                        } catch (NumberFormatException e2) {
                            throw new XmlPullParserException("Not a number in value attribute in item");
                        }
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Expected item tag at: ");
                    stringBuilder.append(parser.getName());
                    throw new XmlPullParserException(stringBuilder.toString());
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    if (parser.getName().equals(str)) {
                        i++;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Expected ");
                        stringBuilder.append(endTag);
                        stringBuilder.append(" end tag at: ");
                        stringBuilder.append(parser.getName());
                        throw new XmlPullParserException(stringBuilder.toString());
                    }
                }
                eventType = parser.next();
                if (eventType == 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Document ended before ");
                    stringBuilder.append(endTag);
                    stringBuilder.append(" end tag");
                    throw new XmlPullParserException(stringBuilder.toString());
                }
            }
        } catch (NullPointerException e3) {
            throw new XmlPullParserException("Need num attribute in int-array");
        } catch (NumberFormatException e4) {
            throw new XmlPullParserException("Not a number in num attribute in int-array");
        }
    }

    public static final long[] readThisLongArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            int num = Integer.parseInt(parser.getAttributeValue(null, "num"));
            parser.next();
            long[] array = new long[num];
            int i = 0;
            int eventType = parser.getEventType();
            while (true) {
                StringBuilder stringBuilder;
                String str = ImsConfig.EXTRA_CHANGED_ITEM;
                if (eventType == 2) {
                    if (parser.getName().equals(str)) {
                        try {
                            array[i] = Long.parseLong(parser.getAttributeValue(null, "value"));
                        } catch (NullPointerException e) {
                            throw new XmlPullParserException("Need value attribute in item");
                        } catch (NumberFormatException e2) {
                            throw new XmlPullParserException("Not a number in value attribute in item");
                        }
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Expected item tag at: ");
                    stringBuilder.append(parser.getName());
                    throw new XmlPullParserException(stringBuilder.toString());
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    if (parser.getName().equals(str)) {
                        i++;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Expected ");
                        stringBuilder.append(endTag);
                        stringBuilder.append(" end tag at: ");
                        stringBuilder.append(parser.getName());
                        throw new XmlPullParserException(stringBuilder.toString());
                    }
                }
                eventType = parser.next();
                if (eventType == 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Document ended before ");
                    stringBuilder.append(endTag);
                    stringBuilder.append(" end tag");
                    throw new XmlPullParserException(stringBuilder.toString());
                }
            }
        } catch (NullPointerException e3) {
            throw new XmlPullParserException("Need num attribute in long-array");
        } catch (NumberFormatException e4) {
            throw new XmlPullParserException("Not a number in num attribute in long-array");
        }
    }

    public static final double[] readThisDoubleArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            int num = Integer.parseInt(parser.getAttributeValue(null, "num"));
            parser.next();
            double[] array = new double[num];
            int i = 0;
            int eventType = parser.getEventType();
            while (true) {
                StringBuilder stringBuilder;
                String str = ImsConfig.EXTRA_CHANGED_ITEM;
                if (eventType == 2) {
                    if (parser.getName().equals(str)) {
                        try {
                            array[i] = Double.parseDouble(parser.getAttributeValue(null, "value"));
                        } catch (NullPointerException e) {
                            throw new XmlPullParserException("Need value attribute in item");
                        } catch (NumberFormatException e2) {
                            throw new XmlPullParserException("Not a number in value attribute in item");
                        }
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Expected item tag at: ");
                    stringBuilder.append(parser.getName());
                    throw new XmlPullParserException(stringBuilder.toString());
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    if (parser.getName().equals(str)) {
                        i++;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Expected ");
                        stringBuilder.append(endTag);
                        stringBuilder.append(" end tag at: ");
                        stringBuilder.append(parser.getName());
                        throw new XmlPullParserException(stringBuilder.toString());
                    }
                }
                eventType = parser.next();
                if (eventType == 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Document ended before ");
                    stringBuilder.append(endTag);
                    stringBuilder.append(" end tag");
                    throw new XmlPullParserException(stringBuilder.toString());
                }
            }
        } catch (NullPointerException e3) {
            throw new XmlPullParserException("Need num attribute in double-array");
        } catch (NumberFormatException e4) {
            throw new XmlPullParserException("Not a number in num attribute in double-array");
        }
    }

    public static final String[] readThisStringArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            int num = Integer.parseInt(parser.getAttributeValue(null, "num"));
            parser.next();
            String[] array = new String[num];
            int i = 0;
            int eventType = parser.getEventType();
            while (true) {
                StringBuilder stringBuilder;
                String str = ImsConfig.EXTRA_CHANGED_ITEM;
                if (eventType == 2) {
                    if (parser.getName().equals(str)) {
                        try {
                            array[i] = parser.getAttributeValue(null, "value");
                        } catch (NullPointerException e) {
                            throw new XmlPullParserException("Need value attribute in item");
                        } catch (NumberFormatException e2) {
                            throw new XmlPullParserException("Not a number in value attribute in item");
                        }
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Expected item tag at: ");
                    stringBuilder.append(parser.getName());
                    throw new XmlPullParserException(stringBuilder.toString());
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    if (parser.getName().equals(str)) {
                        i++;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Expected ");
                        stringBuilder.append(endTag);
                        stringBuilder.append(" end tag at: ");
                        stringBuilder.append(parser.getName());
                        throw new XmlPullParserException(stringBuilder.toString());
                    }
                }
                eventType = parser.next();
                if (eventType == 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Document ended before ");
                    stringBuilder.append(endTag);
                    stringBuilder.append(" end tag");
                    throw new XmlPullParserException(stringBuilder.toString());
                }
            }
        } catch (NullPointerException e3) {
            throw new XmlPullParserException("Need num attribute in string-array");
        } catch (NumberFormatException e4) {
            throw new XmlPullParserException("Not a number in num attribute in string-array");
        }
    }

    public static final boolean[] readThisBooleanArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            int num = Integer.parseInt(parser.getAttributeValue(null, "num"));
            parser.next();
            boolean[] array = new boolean[num];
            int i = 0;
            int eventType = parser.getEventType();
            while (true) {
                StringBuilder stringBuilder;
                String str = ImsConfig.EXTRA_CHANGED_ITEM;
                if (eventType == 2) {
                    if (parser.getName().equals(str)) {
                        try {
                            array[i] = Boolean.parseBoolean(parser.getAttributeValue(null, "value"));
                        } catch (NullPointerException e) {
                            throw new XmlPullParserException("Need value attribute in item");
                        } catch (NumberFormatException e2) {
                            throw new XmlPullParserException("Not a number in value attribute in item");
                        }
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Expected item tag at: ");
                    stringBuilder.append(parser.getName());
                    throw new XmlPullParserException(stringBuilder.toString());
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    if (parser.getName().equals(str)) {
                        i++;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Expected ");
                        stringBuilder.append(endTag);
                        stringBuilder.append(" end tag at: ");
                        stringBuilder.append(parser.getName());
                        throw new XmlPullParserException(stringBuilder.toString());
                    }
                }
                eventType = parser.next();
                if (eventType == 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Document ended before ");
                    stringBuilder.append(endTag);
                    stringBuilder.append(" end tag");
                    throw new XmlPullParserException(stringBuilder.toString());
                }
            }
        } catch (NullPointerException e3) {
            throw new XmlPullParserException("Need num attribute in string-array");
        } catch (NumberFormatException e4) {
            throw new XmlPullParserException("Not a number in num attribute in string-array");
        }
    }

    public static final Object readValueXml(XmlPullParser parser, String[] name) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (eventType != 2) {
            StringBuilder stringBuilder;
            if (eventType == 3) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected end tag at: ");
                stringBuilder.append(parser.getName());
                throw new XmlPullParserException(stringBuilder.toString());
            } else if (eventType != 4) {
                eventType = parser.next();
                if (eventType == 1) {
                    throw new XmlPullParserException("Unexpected end of document");
                }
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected text: ");
                stringBuilder.append(parser.getText());
                throw new XmlPullParserException(stringBuilder.toString());
            }
        }
        return readThisValueXml(parser, name, null, false);
    }

    private static final Object readThisValueXml(XmlPullParser parser, String[] name, ReadMapCallback callback, boolean arrayMap) throws XmlPullParserException, IOException {
        Object res;
        String value;
        int next;
        String valueName = parser.getAttributeValue(null, "name");
        String tagName = parser.getName();
        if (tagName.equals("null")) {
            res = null;
        } else {
            String str = "string";
            StringBuilder stringBuilder;
            if (tagName.equals(str)) {
                value = "";
                while (true) {
                    next = parser.next();
                    int eventType = next;
                    if (next == 1) {
                        throw new XmlPullParserException("Unexpected end of document in <string>");
                    } else if (eventType == 3) {
                        if (parser.getName().equals(str)) {
                            name[0] = valueName;
                            return value;
                        }
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unexpected end tag in <string>: ");
                        stringBuilder.append(parser.getName());
                        throw new XmlPullParserException(stringBuilder.toString());
                    } else if (eventType == 4) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(value);
                        stringBuilder2.append(parser.getText());
                        value = stringBuilder2.toString();
                    } else if (eventType == 2) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unexpected start tag in <string>: ");
                        stringBuilder.append(parser.getName());
                        throw new XmlPullParserException(stringBuilder.toString());
                    }
                }
            } else {
                res = readThisPrimitiveValueXml(parser, tagName);
                Object res2 = res;
                if (res != null) {
                    res = res2;
                } else {
                    str = "byte-array";
                    if (tagName.equals(str)) {
                        res = readThisByteArrayXml(parser, str, name);
                        name[0] = valueName;
                        return res;
                    }
                    str = "int-array";
                    if (tagName.equals(str)) {
                        res = readThisIntArrayXml(parser, str, name);
                        name[0] = valueName;
                        return res;
                    }
                    str = "long-array";
                    if (tagName.equals(str)) {
                        res = readThisLongArrayXml(parser, str, name);
                        name[0] = valueName;
                        return res;
                    }
                    str = "double-array";
                    if (tagName.equals(str)) {
                        res = readThisDoubleArrayXml(parser, str, name);
                        name[0] = valueName;
                        return res;
                    }
                    str = "string-array";
                    if (tagName.equals(str)) {
                        res = readThisStringArrayXml(parser, str, name);
                        name[0] = valueName;
                        return res;
                    }
                    str = "boolean-array";
                    if (tagName.equals(str)) {
                        res = readThisBooleanArrayXml(parser, str, name);
                        name[0] = valueName;
                        return res;
                    }
                    str = "map";
                    if (tagName.equals(str)) {
                        parser.next();
                        if (arrayMap) {
                            res = readThisArrayMapXml(parser, str, name, callback);
                        } else {
                            res = readThisMapXml(parser, str, name, callback);
                        }
                        name[0] = valueName;
                        return res;
                    }
                    str = Slice.HINT_LIST;
                    if (tagName.equals(str)) {
                        parser.next();
                        res = readThisListXml(parser, str, name, callback, arrayMap);
                        name[0] = valueName;
                        return res;
                    } else if (tagName.equals("set")) {
                        parser.next();
                        res = readThisSetXml(parser, "set", name, callback, arrayMap);
                        name[0] = valueName;
                        return res;
                    } else if (callback != null) {
                        res = callback.readThisUnknownObjectXml(parser, tagName);
                        name[0] = valueName;
                        return res;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown tag: ");
                        stringBuilder.append(tagName);
                        throw new XmlPullParserException(stringBuilder.toString());
                    }
                }
            }
        }
        while (true) {
            int next2 = parser.next();
            next = next2;
            StringBuilder stringBuilder3;
            if (next2 != 1) {
                value = ">: ";
                if (next == 3) {
                    if (parser.getName().equals(tagName)) {
                        name[0] = valueName;
                        return res;
                    }
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Unexpected end tag in <");
                    stringBuilder3.append(tagName);
                    stringBuilder3.append(value);
                    stringBuilder3.append(parser.getName());
                    throw new XmlPullParserException(stringBuilder3.toString());
                } else if (next == 4) {
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Unexpected text in <");
                    stringBuilder3.append(tagName);
                    stringBuilder3.append(value);
                    stringBuilder3.append(parser.getName());
                    throw new XmlPullParserException(stringBuilder3.toString());
                } else if (next == 2) {
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Unexpected start tag in <");
                    stringBuilder3.append(tagName);
                    stringBuilder3.append(value);
                    stringBuilder3.append(parser.getName());
                    throw new XmlPullParserException(stringBuilder3.toString());
                }
            } else {
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Unexpected end of document in <");
                stringBuilder3.append(tagName);
                stringBuilder3.append(">");
                throw new XmlPullParserException(stringBuilder3.toString());
            }
        }
    }

    private static final Object readThisPrimitiveValueXml(XmlPullParser parser, String tagName) throws XmlPullParserException, IOException {
        StringBuilder stringBuilder;
        String str = ">";
        try {
            String str2 = "value";
            if (tagName.equals(SliceItem.FORMAT_INT)) {
                return Integer.valueOf(Integer.parseInt(parser.getAttributeValue(null, str2)));
            }
            if (tagName.equals("long")) {
                return Long.valueOf(parser.getAttributeValue(null, str2));
            }
            if (tagName.equals(ApplicationManager.FLOAT)) {
                return new Float(parser.getAttributeValue(null, str2));
            }
            if (tagName.equals("double")) {
                return new Double(parser.getAttributeValue(null, str2));
            }
            if (tagName.equals("boolean")) {
                return Boolean.valueOf(parser.getAttributeValue(null, str2));
            }
            return null;
        } catch (NullPointerException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Need value attribute in <");
            stringBuilder.append(tagName);
            stringBuilder.append(str);
            throw new XmlPullParserException(stringBuilder.toString());
        } catch (NumberFormatException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Not a number in value attribute in <");
            stringBuilder.append(tagName);
            stringBuilder.append(str);
            throw new XmlPullParserException(stringBuilder.toString());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x000e  */
    @android.annotation.UnsupportedAppUsage
    public static final void beginDocument(org.xmlpull.v1.XmlPullParser r4, java.lang.String r5) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
    L_0x0000:
        r0 = r4.next();
        r1 = r0;
        r2 = 2;
        if (r0 == r2) goto L_0x000c;
    L_0x0008:
        r0 = 1;
        if (r1 == r0) goto L_0x000c;
    L_0x000b:
        goto L_0x0000;
    L_0x000c:
        if (r1 != r2) goto L_0x003c;
    L_0x000e:
        r0 = r4.getName();
        r0 = r0.equals(r5);
        if (r0 == 0) goto L_0x0019;
    L_0x0018:
        return;
    L_0x0019:
        r0 = new org.xmlpull.v1.XmlPullParserException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Unexpected start tag: found ";
        r2.append(r3);
        r3 = r4.getName();
        r2.append(r3);
        r3 = ", expected ";
        r2.append(r3);
        r2.append(r5);
        r2 = r2.toString();
        r0.<init>(r2);
        throw r0;
    L_0x003c:
        r0 = new org.xmlpull.v1.XmlPullParserException;
        r2 = "No start tag found";
        r0.<init>(r2);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.util.XmlUtils.beginDocument(org.xmlpull.v1.XmlPullParser, java.lang.String):void");
    }

    @UnsupportedAppUsage
    public static final void nextElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 2 || type == 1) {
                return;
            }
        }
    }

    public static boolean nextElementWithin(XmlPullParser parser, int outerDepth) throws IOException, XmlPullParserException {
        while (true) {
            int type = parser.next();
            if (type != 1 && (type != 3 || parser.getDepth() != outerDepth)) {
                if (type == 2 && parser.getDepth() == outerDepth + 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int readIntAttribute(XmlPullParser in, String name, int defaultValue) {
        String value = in.getAttributeValue(null, name);
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int readIntAttribute(XmlPullParser in, String name) throws IOException {
        String value = in.getAttributeValue(null, name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("problem parsing ");
            stringBuilder.append(name);
            stringBuilder.append("=");
            stringBuilder.append(value);
            stringBuilder.append(" as int");
            throw new ProtocolException(stringBuilder.toString());
        }
    }

    public static void writeIntAttribute(XmlSerializer out, String name, int value) throws IOException {
        out.attribute(null, name, Integer.toString(value));
    }

    public static long readLongAttribute(XmlPullParser in, String name, long defaultValue) {
        String value = in.getAttributeValue(null, name);
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long readLongAttribute(XmlPullParser in, String name) throws IOException {
        String value = in.getAttributeValue(null, name);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("problem parsing ");
            stringBuilder.append(name);
            stringBuilder.append("=");
            stringBuilder.append(value);
            stringBuilder.append(" as long");
            throw new ProtocolException(stringBuilder.toString());
        }
    }

    public static void writeLongAttribute(XmlSerializer out, String name, long value) throws IOException {
        out.attribute(null, name, Long.toString(value));
    }

    public static float readFloatAttribute(XmlPullParser in, String name) throws IOException {
        String value = in.getAttributeValue(null, name);
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("problem parsing ");
            stringBuilder.append(name);
            stringBuilder.append("=");
            stringBuilder.append(value);
            stringBuilder.append(" as long");
            throw new ProtocolException(stringBuilder.toString());
        }
    }

    public static void writeFloatAttribute(XmlSerializer out, String name, float value) throws IOException {
        out.attribute(null, name, Float.toString(value));
    }

    public static boolean readBooleanAttribute(XmlPullParser in, String name) {
        return Boolean.parseBoolean(in.getAttributeValue(null, name));
    }

    public static boolean readBooleanAttribute(XmlPullParser in, String name, boolean defaultValue) {
        String value = in.getAttributeValue(null, name);
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public static void writeBooleanAttribute(XmlSerializer out, String name, boolean value) throws IOException {
        out.attribute(null, name, Boolean.toString(value));
    }

    public static Uri readUriAttribute(XmlPullParser in, String name) {
        String value = in.getAttributeValue(null, name);
        if (value != null) {
            return Uri.parse(value);
        }
        return null;
    }

    public static void writeUriAttribute(XmlSerializer out, String name, Uri value) throws IOException {
        if (value != null) {
            out.attribute(null, name, value.toString());
        }
    }

    public static String readStringAttribute(XmlPullParser in, String name) {
        return in.getAttributeValue(null, name);
    }

    public static void writeStringAttribute(XmlSerializer out, String name, CharSequence value) throws IOException {
        if (value != null) {
            out.attribute(null, name, value.toString());
        }
    }

    public static byte[] readByteArrayAttribute(XmlPullParser in, String name) {
        String value = in.getAttributeValue(null, name);
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        return Base64.decode(value, 0);
    }

    public static void writeByteArrayAttribute(XmlSerializer out, String name, byte[] value) throws IOException {
        if (value != null) {
            out.attribute(null, name, Base64.encodeToString(value, 0));
        }
    }

    public static Bitmap readBitmapAttribute(XmlPullParser in, String name) {
        byte[] value = readByteArrayAttribute(in, name);
        if (value != null) {
            return BitmapFactory.decodeByteArray(value, 0, value.length);
        }
        return null;
    }

    @Deprecated
    public static void writeBitmapAttribute(XmlSerializer out, String name, Bitmap value) throws IOException {
        if (value != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            value.compress(CompressFormat.PNG, 90, os);
            writeByteArrayAttribute(out, name, os.toByteArray());
        }
    }
}
