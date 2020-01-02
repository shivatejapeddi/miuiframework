package com.android.framework.protobuf;

import android.telecom.Logging.Session;
import com.android.framework.protobuf.GeneratedMessageLite.ExtendableMessage;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

final class MessageLiteToString {
    private static final String BUILDER_LIST_SUFFIX = "OrBuilderList";
    private static final String BYTES_SUFFIX = "Bytes";
    private static final String LIST_SUFFIX = "List";

    MessageLiteToString() {
    }

    static String toString(MessageLite messageLite, String commentString) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("# ");
        buffer.append(commentString);
        reflectivePrintWithIndent(messageLite, buffer, 0);
        return buffer.toString();
    }

    private static void reflectivePrintWithIndent(MessageLite messageLite, StringBuilder buffer, int indent) {
        String str;
        MessageLite messageLite2 = messageLite;
        StringBuilder stringBuilder = buffer;
        int i = indent;
        Map<String, Method> nameToNoArgMethod = new HashMap();
        Map<String, Method> nameToMethod = new HashMap();
        Set<String> getters = new TreeSet();
        Method[] declaredMethods = messageLite.getClass().getDeclaredMethods();
        int length = declaredMethods.length;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            str = "get";
            if (i3 >= length) {
                break;
            }
            Method method = declaredMethods[i3];
            nameToMethod.put(method.getName(), method);
            if (method.getParameterTypes().length == 0) {
                nameToNoArgMethod.put(method.getName(), method);
                if (method.getName().startsWith(str)) {
                    getters.add(method.getName());
                }
            }
            i3++;
        }
        for (String getter : getters) {
            String suffix = getter.replaceFirst(str, "");
            String str2 = "List";
            if (suffix.endsWith(str2) && !suffix.endsWith(BUILDER_LIST_SUFFIX)) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(suffix.substring(i2, 1).toLowerCase());
                stringBuilder2.append(suffix.substring(1, suffix.length() - str2.length()));
                str2 = stringBuilder2.toString();
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(suffix);
                Method listMethod = (Method) nameToNoArgMethod.get(stringBuilder2.toString());
                if (listMethod != null) {
                    printField(stringBuilder, i, camelCaseToSnakeCase(str2), GeneratedMessageLite.invokeOrDie(listMethod, messageLite2, new Object[i2]));
                }
            }
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("set");
            stringBuilder3.append(suffix);
            if (((Method) nameToMethod.get(stringBuilder3.toString())) != null) {
                StringBuilder stringBuilder4;
                String str3 = BYTES_SUFFIX;
                if (suffix.endsWith(str3)) {
                    stringBuilder4 = new StringBuilder();
                    stringBuilder4.append(str);
                    stringBuilder4.append(suffix.substring(i2, suffix.length() - str3.length()));
                    if (nameToNoArgMethod.containsKey(stringBuilder4.toString())) {
                    }
                }
                str3 = new StringBuilder();
                str3.append(suffix.substring(i2, 1).toLowerCase());
                str3.append(suffix.substring(1));
                str3 = str3.toString();
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append(str);
                stringBuilder4.append(suffix);
                Method getMethod = (Method) nameToNoArgMethod.get(stringBuilder4.toString());
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append("has");
                stringBuilder5.append(suffix);
                Method hasMethod = (Method) nameToNoArgMethod.get(stringBuilder5.toString());
                if (getMethod != null) {
                    Map<String, Method> nameToNoArgMethod2;
                    Object value = GeneratedMessageLite.invokeOrDie(getMethod, messageLite2, new Object[i2]);
                    if (hasMethod == null) {
                        nameToNoArgMethod2 = nameToNoArgMethod;
                        nameToNoArgMethod = !isDefaultValue(value) ? true : i2;
                    } else {
                        nameToNoArgMethod2 = nameToNoArgMethod;
                        nameToNoArgMethod = ((Boolean) GeneratedMessageLite.invokeOrDie(hasMethod, messageLite2, new Object[i2])).booleanValue();
                    }
                    if (nameToNoArgMethod != null) {
                        printField(stringBuilder, i, camelCaseToSnakeCase(str3), value);
                        nameToNoArgMethod = nameToNoArgMethod2;
                        i2 = 0;
                    } else {
                        nameToNoArgMethod = nameToNoArgMethod2;
                        i2 = 0;
                    }
                } else {
                    i2 = 0;
                }
            }
        }
        if (messageLite2 instanceof ExtendableMessage) {
            Iterator<Entry<ExtensionDescriptor, Object>> iter = ((ExtendableMessage) messageLite2).extensions.iterator();
            while (iter.hasNext()) {
                Entry<ExtensionDescriptor, Object> entry = (Entry) iter.next();
                StringBuilder stringBuilder6 = new StringBuilder();
                stringBuilder6.append("[");
                stringBuilder6.append(((ExtensionDescriptor) entry.getKey()).getNumber());
                stringBuilder6.append("]");
                printField(stringBuilder, i, stringBuilder6.toString(), entry.getValue());
            }
        }
        if (((GeneratedMessageLite) messageLite2).unknownFields != null) {
            ((GeneratedMessageLite) messageLite2).unknownFields.printWithIndent(stringBuilder, i);
        }
    }

    private static boolean isDefaultValue(Object o) {
        boolean z = true;
        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue() ^ 1;
        }
        if (o instanceof Integer) {
            if (((Integer) o).intValue() != 0) {
                z = false;
            }
            return z;
        } else if (o instanceof Float) {
            if (((Float) o).floatValue() != 0.0f) {
                z = false;
            }
            return z;
        } else if (o instanceof Double) {
            if (((Double) o).doubleValue() != 0.0d) {
                z = false;
            }
            return z;
        } else if (o instanceof String) {
            return o.equals("");
        } else {
            if (o instanceof ByteString) {
                return o.equals(ByteString.EMPTY);
            }
            if (o instanceof MessageLite) {
                if (o != ((MessageLite) o).getDefaultInstanceForType()) {
                    z = false;
                }
                return z;
            } else if (!(o instanceof Enum)) {
                return false;
            } else {
                if (((Enum) o).ordinal() != 0) {
                    z = false;
                }
                return z;
            }
        }
    }

    static final void printField(StringBuilder buffer, int indent, String name, Object object) {
        if (object instanceof List) {
            for (Object entry : (List) object) {
                printField(buffer, indent, name, entry);
            }
            return;
        }
        int i;
        buffer.append(10);
        for (i = 0; i < indent; i++) {
            buffer.append(' ');
        }
        buffer.append(name);
        String str = ": \"";
        if (object instanceof String) {
            buffer.append(str);
            buffer.append(TextFormatEscaper.escapeText((String) object));
            buffer.append('\"');
        } else if (object instanceof ByteString) {
            buffer.append(str);
            buffer.append(TextFormatEscaper.escapeBytes((ByteString) object));
            buffer.append('\"');
        } else if (object instanceof GeneratedMessageLite) {
            buffer.append(" {");
            reflectivePrintWithIndent((GeneratedMessageLite) object, buffer, indent + 2);
            buffer.append("\n");
            for (i = 0; i < indent; i++) {
                buffer.append(' ');
            }
            buffer.append("}");
        } else {
            buffer.append(": ");
            buffer.append(object.toString());
        }
    }

    private static final String camelCaseToSnakeCase(String camelCase) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char ch = camelCase.charAt(i);
            if (Character.isUpperCase(ch)) {
                builder.append(Session.SESSION_SEPARATION_CHAR_CHILD);
            }
            builder.append(Character.toLowerCase(ch));
        }
        return builder.toString();
    }
}
