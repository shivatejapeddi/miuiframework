package org.apache.miui.commons.lang3.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.apache.miui.commons.lang3.ClassUtils;

public class FieldUtils {
    public static Field getField(Class<?> cls, String fieldName) {
        Field field = getField(cls, fieldName, null);
        MemberUtils.setAccessibleWorkaround(field);
        return field;
    }

    public static Field getField(Class<?> cls, String fieldName, boolean forceAccess) {
        if (cls == null) {
            throw new IllegalArgumentException("The class must not be null");
        } else if (fieldName != null) {
            Class<?> acls = cls;
            while (acls != null) {
                try {
                    Field field = acls.getDeclaredField(fieldName);
                    if (!Modifier.isPublic(field.getModifiers())) {
                        if (forceAccess) {
                            field.setAccessible(true);
                        } else {
                            acls = acls.getSuperclass();
                        }
                    }
                    return field;
                } catch (NoSuchFieldException e) {
                }
            }
            Field match = null;
            for (Class<?> class1 : ClassUtils.getAllInterfaces(cls)) {
                try {
                    Field test = class1.getField(fieldName);
                    if (match == null) {
                        match = test;
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Reference to field ");
                        stringBuilder.append(fieldName);
                        stringBuilder.append(" is ambiguous relative to ");
                        stringBuilder.append(cls);
                        stringBuilder.append("; a matching field exists on two or more implemented interfaces.");
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                } catch (NoSuchFieldException e2) {
                }
            }
            return match;
        } else {
            throw new IllegalArgumentException("The field name must not be null");
        }
    }

    public static Field getDeclaredField(Class<?> cls, String fieldName) {
        return getDeclaredField(cls, fieldName, false);
    }

    public static Field getDeclaredField(Class<?> cls, String fieldName, boolean forceAccess) {
        if (cls == null) {
            throw new IllegalArgumentException("The class must not be null");
        } else if (fieldName != null) {
            try {
                Field field = cls.getDeclaredField(fieldName);
                if (!MemberUtils.isAccessible(field)) {
                    if (!forceAccess) {
                        return null;
                    }
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                return null;
            }
        } else {
            throw new IllegalArgumentException("The field name must not be null");
        }
    }

    public static Object readStaticField(Field field) throws IllegalAccessException {
        return readStaticField(field, false);
    }

    public static Object readStaticField(Field field, boolean forceAccess) throws IllegalAccessException {
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        } else if (Modifier.isStatic(field.getModifiers())) {
            return readField(field, null, forceAccess);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The field '");
            stringBuilder.append(field.getName());
            stringBuilder.append("' is not static");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static Object readStaticField(Class<?> cls, String fieldName) throws IllegalAccessException {
        return readStaticField(cls, fieldName, false);
    }

    public static Object readStaticField(Class<?> cls, String fieldName, boolean forceAccess) throws IllegalAccessException {
        Field field = getField(cls, fieldName, forceAccess);
        if (field != null) {
            return readStaticField(field, false);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot locate field ");
        stringBuilder.append(fieldName);
        stringBuilder.append(" on ");
        stringBuilder.append(cls);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static Object readDeclaredStaticField(Class<?> cls, String fieldName) throws IllegalAccessException {
        return readDeclaredStaticField(cls, fieldName, false);
    }

    public static Object readDeclaredStaticField(Class<?> cls, String fieldName, boolean forceAccess) throws IllegalAccessException {
        Field field = getDeclaredField(cls, fieldName, forceAccess);
        if (field != null) {
            return readStaticField(field, false);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot locate declared field ");
        stringBuilder.append(cls.getName());
        stringBuilder.append(".");
        stringBuilder.append(fieldName);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static Object readField(Field field, Object target) throws IllegalAccessException {
        return readField(field, target, false);
    }

    public static Object readField(Field field, Object target, boolean forceAccess) throws IllegalAccessException {
        if (field != null) {
            if (!forceAccess || field.isAccessible()) {
                MemberUtils.setAccessibleWorkaround(field);
            } else {
                field.setAccessible(true);
            }
            return field.get(target);
        }
        throw new IllegalArgumentException("The field must not be null");
    }

    public static Object readField(Object target, String fieldName) throws IllegalAccessException {
        return readField(target, fieldName, false);
    }

    public static Object readField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException {
        if (target != null) {
            Class<?> cls = target.getClass();
            Field field = getField(cls, fieldName, forceAccess);
            if (field != null) {
                return readField(field, target);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot locate field ");
            stringBuilder.append(fieldName);
            stringBuilder.append(" on ");
            stringBuilder.append(cls);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new IllegalArgumentException("target object must not be null");
    }

    public static Object readDeclaredField(Object target, String fieldName) throws IllegalAccessException {
        return readDeclaredField(target, fieldName, false);
    }

    public static Object readDeclaredField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException {
        if (target != null) {
            Class<?> cls = target.getClass();
            Field field = getDeclaredField(cls, fieldName, forceAccess);
            if (field != null) {
                return readField(field, target);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot locate declared field ");
            stringBuilder.append(cls.getName());
            stringBuilder.append(".");
            stringBuilder.append(fieldName);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new IllegalArgumentException("target object must not be null");
    }

    public static void writeStaticField(Field field, Object value) throws IllegalAccessException {
        writeStaticField(field, value, false);
    }

    public static void writeStaticField(Field field, Object value, boolean forceAccess) throws IllegalAccessException {
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        } else if (Modifier.isStatic(field.getModifiers())) {
            writeField(field, null, value, forceAccess);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The field '");
            stringBuilder.append(field.getName());
            stringBuilder.append("' is not static");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static void writeStaticField(Class<?> cls, String fieldName, Object value) throws IllegalAccessException {
        writeStaticField(cls, fieldName, value, false);
    }

    public static void writeStaticField(Class<?> cls, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
        Field field = getField(cls, fieldName, forceAccess);
        if (field != null) {
            writeStaticField(field, value);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot locate field ");
        stringBuilder.append(fieldName);
        stringBuilder.append(" on ");
        stringBuilder.append(cls);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static void writeDeclaredStaticField(Class<?> cls, String fieldName, Object value) throws IllegalAccessException {
        writeDeclaredStaticField(cls, fieldName, value, false);
    }

    public static void writeDeclaredStaticField(Class<?> cls, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
        Field field = getDeclaredField(cls, fieldName, forceAccess);
        if (field != null) {
            writeField(field, null, value);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot locate declared field ");
        stringBuilder.append(cls.getName());
        stringBuilder.append(".");
        stringBuilder.append(fieldName);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static void writeField(Field field, Object target, Object value) throws IllegalAccessException {
        writeField(field, target, value, false);
    }

    public static void writeField(Field field, Object target, Object value, boolean forceAccess) throws IllegalAccessException {
        if (field != null) {
            if (!forceAccess || field.isAccessible()) {
                MemberUtils.setAccessibleWorkaround(field);
            } else {
                field.setAccessible(true);
            }
            field.set(target, value);
            return;
        }
        throw new IllegalArgumentException("The field must not be null");
    }

    public static void writeField(Object target, String fieldName, Object value) throws IllegalAccessException {
        writeField(target, fieldName, value, false);
    }

    public static void writeField(Object target, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
        if (target != null) {
            Class<?> cls = target.getClass();
            Field field = getField(cls, fieldName, forceAccess);
            if (field != null) {
                writeField(field, target, value);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot locate declared field ");
            stringBuilder.append(cls.getName());
            stringBuilder.append(".");
            stringBuilder.append(fieldName);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new IllegalArgumentException("target object must not be null");
    }

    public static void writeDeclaredField(Object target, String fieldName, Object value) throws IllegalAccessException {
        writeDeclaredField(target, fieldName, value, false);
    }

    public static void writeDeclaredField(Object target, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
        if (target != null) {
            Class<?> cls = target.getClass();
            Field field = getDeclaredField(cls, fieldName, forceAccess);
            if (field != null) {
                writeField(field, target, value);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot locate declared field ");
            stringBuilder.append(cls.getName());
            stringBuilder.append(".");
            stringBuilder.append(fieldName);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new IllegalArgumentException("target object must not be null");
    }
}
