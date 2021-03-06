package org.apache.miui.commons.lang3.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.apache.miui.commons.lang3.ArrayUtils;
import org.apache.miui.commons.lang3.ClassUtils;

public class ConstructorUtils {
    public static <T> T invokeConstructor(Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        Class<?>[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeConstructor(cls, args, parameterTypes);
    }

    public static <T> T invokeConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        Constructor<T> ctor = getMatchingAccessibleConstructor(cls, parameterTypes);
        if (ctor != null) {
            return ctor.newInstance(args);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No such accessible constructor on object: ");
        stringBuilder.append(cls.getName());
        throw new NoSuchMethodException(stringBuilder.toString());
    }

    public static <T> T invokeExactConstructor(Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        int arguments = args.length;
        Class<?>[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeExactConstructor(cls, args, parameterTypes);
    }

    public static <T> T invokeExactConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Constructor<T> ctor = getAccessibleConstructor(cls, parameterTypes);
        if (ctor != null) {
            return ctor.newInstance(args);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No such accessible constructor on object: ");
        stringBuilder.append(cls.getName());
        throw new NoSuchMethodException(stringBuilder.toString());
    }

    public static <T> Constructor<T> getAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) {
        try {
            return getAccessibleConstructor(cls.getConstructor(parameterTypes));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static <T> Constructor<T> getAccessibleConstructor(Constructor<T> ctor) {
        return (MemberUtils.isAccessible(ctor) && Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) ? ctor : null;
    }

    public static <T> Constructor<T> getMatchingAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) {
        Constructor<T> ctor;
        try {
            ctor = cls.getConstructor(parameterTypes);
            MemberUtils.setAccessibleWorkaround(ctor);
            return ctor;
        } catch (NoSuchMethodException e) {
            ctor = null;
            for (Constructor<?> ctor2 : cls.getConstructors()) {
                Constructor<?> ctor22;
                if (ClassUtils.isAssignable((Class[]) parameterTypes, ctor22.getParameterTypes(), true)) {
                    ctor22 = getAccessibleConstructor(ctor22);
                    if (ctor22 != null) {
                        MemberUtils.setAccessibleWorkaround(ctor22);
                        if (ctor == null || MemberUtils.compareParameterTypes(ctor22.getParameterTypes(), ctor.getParameterTypes(), parameterTypes) < 0) {
                            ctor = ctor22;
                        }
                    }
                }
            }
            return ctor;
        }
    }
}
