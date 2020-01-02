package miui.maml.util;

import android.app.slice.SliceItem;
import android.text.TextUtils;
import com.miui.enterprise.sdk.ApplicationManager;
import java.util.HashMap;

public class ReflectionHelper {
    static HashMap<String, Class<?>> PRIMITIVE_TYPE = new HashMap();

    static {
        PRIMITIVE_TYPE.put("byte", Byte.TYPE);
        PRIMITIVE_TYPE.put("short", Short.TYPE);
        PRIMITIVE_TYPE.put(SliceItem.FORMAT_INT, Integer.TYPE);
        PRIMITIVE_TYPE.put("long", Long.TYPE);
        PRIMITIVE_TYPE.put("char", Character.TYPE);
        PRIMITIVE_TYPE.put("boolean", Boolean.TYPE);
        PRIMITIVE_TYPE.put(ApplicationManager.FLOAT, Float.TYPE);
        PRIMITIVE_TYPE.put("double", Double.TYPE);
        PRIMITIVE_TYPE.put("byte[]", byte[].class);
        PRIMITIVE_TYPE.put("short[]", short[].class);
        PRIMITIVE_TYPE.put("int[]", int[].class);
        PRIMITIVE_TYPE.put("long[]", long[].class);
        PRIMITIVE_TYPE.put("char[]", char[].class);
        PRIMITIVE_TYPE.put("boolean[]", boolean[].class);
        PRIMITIVE_TYPE.put("float[]", float[].class);
        PRIMITIVE_TYPE.put("double[]", double[].class);
    }

    public static Class<?>[] strTypesToClass(String[] paramTypes) throws ClassNotFoundException {
        if (paramTypes == null) {
            return null;
        }
        Class<?>[] paramClass = new Class[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramClass[i] = strTypeToClassThrows(paramTypes[i]);
        }
        return paramClass;
    }

    public static Class<?> strTypeToClass(String type) {
        try {
            return strTypeToClassThrows(type);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Object getEnumConstant(String enumTypeName, String name) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(name)) {
            return null;
        }
        try {
            return Enum.valueOf(Class.forName(enumTypeName), name);
        } catch (ClassCastException | ClassNotFoundException | IllegalArgumentException e) {
            return null;
        }
    }

    private static Class<?> strTypeToClassThrows(String type) throws ClassNotFoundException {
        if (PRIMITIVE_TYPE.containsKey(type)) {
            return (Class) PRIMITIVE_TYPE.get(type);
        }
        String str;
        if (type.contains(".")) {
            str = type;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("java.lang.");
            stringBuilder.append(type);
            str = stringBuilder.toString();
        }
        return Class.forName(str);
    }
}
