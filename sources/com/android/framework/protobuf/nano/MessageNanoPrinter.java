package com.android.framework.protobuf.nano;

import android.content.IntentFilter;
import android.telecom.Logging.Session;
import android.text.format.DateFormat;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

public final class MessageNanoPrinter {
    private static final String INDENT = "  ";
    private static final int MAX_STRING_LEN = 200;

    private MessageNanoPrinter() {
    }

    public static <T extends MessageNano> String print(T message) {
        StringBuilder stringBuilder;
        String str = "Error printing proto: ";
        if (message == null) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        try {
            print(null, message, new StringBuffer(), buf);
            return buf.toString();
        } catch (IllegalAccessException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(e.getMessage());
            return stringBuilder.toString();
        } catch (InvocationTargetException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(e2.getMessage());
            return stringBuilder.toString();
        }
    }

    private static void print(String identifier, Object object, StringBuffer indentBuf, StringBuffer buf) throws IllegalAccessException, InvocationTargetException {
        String map;
        Map<?, ?> map2 = object;
        StringBuffer stringBuffer = indentBuf;
        StringBuffer stringBuffer2 = buf;
        if (map2 != null) {
            boolean z = map2 instanceof MessageNano;
            String str = ">\n";
            String str2 = INDENT;
            String str3 = " <\n";
            if (z) {
                String fieldName;
                int len;
                int origIndentBufLength = indentBuf.length();
                if (identifier != null) {
                    stringBuffer2.append(stringBuffer);
                    stringBuffer2.append(deCamelCaseify(identifier));
                    stringBuffer2.append(str3);
                    stringBuffer.append(str2);
                }
                Class<?> clazz = object.getClass();
                Field[] fields = clazz.getFields();
                int length = fields.length;
                int i = 0;
                while (i < length) {
                    Field[] fieldArr;
                    int i2;
                    Field field = fields[i];
                    int modifiers = field.getModifiers();
                    fieldName = field.getName();
                    if ("cachedSize".equals(fieldName)) {
                        fieldArr = fields;
                        i2 = length;
                    } else if ((modifiers & 1) != 1 || (modifiers & 8) == 8) {
                        fieldArr = fields;
                        i2 = length;
                    } else {
                        String str4 = Session.SESSION_SEPARATION_CHAR_CHILD;
                        if (fieldName.startsWith(str4)) {
                            fieldArr = fields;
                            i2 = length;
                        } else if (fieldName.endsWith(str4)) {
                            fieldArr = fields;
                            i2 = length;
                        } else {
                            Class<?> fieldType = field.getType();
                            Object value = field.get(map2);
                            if (!fieldType.isArray()) {
                                fieldArr = fields;
                                i2 = length;
                                print(fieldName, value, stringBuffer, stringBuffer2);
                            } else if (fieldType.getComponentType() == Byte.TYPE) {
                                print(fieldName, value, stringBuffer, stringBuffer2);
                                fieldArr = fields;
                                i2 = length;
                            } else {
                                len = value == null ? 0 : Array.getLength(value);
                                fieldArr = fields;
                                int i3 = 0;
                                while (i3 < len) {
                                    i2 = length;
                                    print(fieldName, Array.get(value, i3), stringBuffer, stringBuffer2);
                                    i3++;
                                    length = i2;
                                }
                                i2 = length;
                            }
                        }
                    }
                    i++;
                    length = i2;
                    fields = fieldArr;
                }
                Method[] methods = clazz.getMethods();
                len = methods.length;
                i = 0;
                while (i < len) {
                    Method[] methodArr;
                    String name = methods[i].getName();
                    if (name.startsWith("set")) {
                        fieldName = name.substring(3);
                        try {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("has");
                            stringBuilder.append(fieldName);
                            try {
                                if (((Boolean) clazz.getMethod(stringBuilder.toString(), new Class[0]).invoke(map2, new Object[0])).booleanValue()) {
                                    try {
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("get");
                                        stringBuilder.append(fieldName);
                                        methodArr = methods;
                                        try {
                                            print(fieldName, clazz.getMethod(stringBuilder.toString(), new Class[0]).invoke(map2, new Object[0]), stringBuffer, stringBuffer2);
                                        } catch (NoSuchMethodException e) {
                                        }
                                    } catch (NoSuchMethodException e2) {
                                        methodArr = methods;
                                    }
                                } else {
                                    methodArr = methods;
                                }
                            } catch (NoSuchMethodException e3) {
                                methodArr = methods;
                                int i4 = 0;
                            }
                        } catch (NoSuchMethodException e4) {
                            methodArr = methods;
                        }
                    } else {
                        methodArr = methods;
                    }
                    i++;
                    methods = methodArr;
                }
                if (identifier != null) {
                    stringBuffer.setLength(origIndentBufLength);
                    stringBuffer2.append(stringBuffer);
                    stringBuffer2.append(str);
                }
            } else if (map2 instanceof Map) {
                Map<?, ?> map3 = map2;
                String identifier2 = deCamelCaseify(identifier);
                for (Entry<?, ?> entry : map3.entrySet()) {
                    stringBuffer2.append(stringBuffer);
                    stringBuffer2.append(identifier2);
                    stringBuffer2.append(str3);
                    int origIndentBufLength2 = indentBuf.length();
                    stringBuffer.append(str2);
                    print("key", entry.getKey(), stringBuffer, stringBuffer2);
                    print("value", entry.getValue(), stringBuffer, stringBuffer2);
                    stringBuffer.setLength(origIndentBufLength2);
                    stringBuffer2.append(stringBuffer);
                    stringBuffer2.append(str);
                }
                map = identifier2;
                return;
            } else {
                map = deCamelCaseify(identifier);
                stringBuffer2.append(stringBuffer);
                stringBuffer2.append(map);
                stringBuffer2.append(": ");
                if (map2 instanceof String) {
                    str = sanitizeString((String) map2);
                    str2 = "\"";
                    stringBuffer2.append(str2);
                    stringBuffer2.append(str);
                    stringBuffer2.append(str2);
                } else if (map2 instanceof byte[]) {
                    appendQuotedBytes((byte[]) map2, stringBuffer2);
                } else {
                    stringBuffer2.append(map2);
                }
                stringBuffer2.append("\n");
                return;
            }
        }
        map = identifier;
    }

    private static String deCamelCaseify(String identifier) {
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < identifier.length(); i++) {
            char currentChar = identifier.charAt(i);
            if (i == 0) {
                out.append(Character.toLowerCase(currentChar));
            } else if (Character.isUpperCase(currentChar)) {
                out.append('_');
                out.append(Character.toLowerCase(currentChar));
            } else {
                out.append(currentChar);
            }
        }
        return out.toString();
    }

    private static String sanitizeString(String str) {
        if (!str.startsWith(IntentFilter.SCHEME_HTTP) && str.length() > 200) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str.substring(0, 200));
            stringBuilder.append("[...]");
            str = stringBuilder.toString();
        }
        return escapeString(str);
    }

    private static String escapeString(String str) {
        int strLen = str.length();
        StringBuilder b = new StringBuilder(strLen);
        for (int i = 0; i < strLen; i++) {
            char original = str.charAt(i);
            if (original < ' ' || original > '~' || original == '\"' || original == DateFormat.QUOTE) {
                b.append(String.format("\\u%04x", new Object[]{Integer.valueOf(original)}));
            } else {
                b.append(original);
            }
        }
        return b.toString();
    }

    private static void appendQuotedBytes(byte[] bytes, StringBuffer builder) {
        if (bytes == null) {
            builder.append("\"\"");
            return;
        }
        builder.append('\"');
        for (int ch : bytes) {
            int ch2 = ch2 & 255;
            if (ch2 == 92 || ch2 == 34) {
                builder.append('\\');
                builder.append((char) ch2);
            } else if (ch2 < 32 || ch2 >= 127) {
                builder.append(String.format("\\%03o", new Object[]{Integer.valueOf(ch2)}));
            } else {
                builder.append((char) ch2);
            }
        }
        builder.append('\"');
    }
}
