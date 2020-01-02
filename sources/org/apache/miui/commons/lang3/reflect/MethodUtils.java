package org.apache.miui.commons.lang3.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.miui.commons.lang3.ArrayUtils;
import org.apache.miui.commons.lang3.ClassUtils;

public class MethodUtils {
    public static Object invokeMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        int arguments = args.length;
        Class<?>[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeMethod(object, methodName, args, parameterTypes);
    }

    public static Object invokeMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        Method method = getMatchingAccessibleMethod(object.getClass(), methodName, parameterTypes);
        if (method != null) {
            return method.invoke(object, args);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No such accessible method: ");
        stringBuilder.append(methodName);
        stringBuilder.append("() on object: ");
        stringBuilder.append(object.getClass().getName());
        throw new NoSuchMethodException(stringBuilder.toString());
    }

    public static Object invokeExactMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        int arguments = args.length;
        Class<?>[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeExactMethod(object, methodName, args, parameterTypes);
    }

    public static Object invokeExactMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Method method = getAccessibleMethod(object.getClass(), methodName, parameterTypes);
        if (method != null) {
            return method.invoke(object, args);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No such accessible method: ");
        stringBuilder.append(methodName);
        stringBuilder.append("() on object: ");
        stringBuilder.append(object.getClass().getName());
        throw new NoSuchMethodException(stringBuilder.toString());
    }

    public static Object invokeExactStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Method method = getAccessibleMethod(cls, methodName, parameterTypes);
        if (method != null) {
            return method.invoke(null, args);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No such accessible method: ");
        stringBuilder.append(methodName);
        stringBuilder.append("() on class: ");
        stringBuilder.append(cls.getName());
        throw new NoSuchMethodException(stringBuilder.toString());
    }

    public static Object invokeStaticMethod(Class<?> cls, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        int arguments = args.length;
        Class<?>[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeStaticMethod(cls, methodName, args, parameterTypes);
    }

    public static Object invokeStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        Method method = getMatchingAccessibleMethod(cls, methodName, parameterTypes);
        if (method != null) {
            return method.invoke(null, args);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No such accessible method: ");
        stringBuilder.append(methodName);
        stringBuilder.append("() on class: ");
        stringBuilder.append(cls.getName());
        throw new NoSuchMethodException(stringBuilder.toString());
    }

    public static Object invokeExactStaticMethod(Class<?> cls, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        int arguments = args.length;
        Class<?>[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeExactStaticMethod(cls, methodName, args, parameterTypes);
    }

    public static Method getAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        try {
            return getAccessibleMethod(cls.getMethod(methodName, parameterTypes));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getAccessibleMethod(Method method) {
        if (!MemberUtils.isAccessible(method)) {
            return null;
        }
        Class<?> cls = method.getDeclaringClass();
        if (Modifier.isPublic(cls.getModifiers())) {
            return method;
        }
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        method = getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);
        if (method == null) {
            method = getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
        }
        return method;
    }

    private static Method getAccessibleMethodFromSuperclass(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        Class<?> parentClass = cls.getSuperclass();
        while (true) {
            Method method = null;
            if (parentClass == null) {
                return null;
            }
            if (Modifier.isPublic(parentClass.getModifiers())) {
                try {
                    method = parentClass.getMethod(methodName, parameterTypes);
                    return method;
                } catch (NoSuchMethodException e) {
                    return method;
                }
            }
            parentClass = parentClass.getSuperclass();
        }
    }

    private static Method getAccessibleMethodFromInterfaceNest(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        while (cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (Modifier.isPublic(interfaces[i].getModifiers())) {
                    try {
                        method = interfaces[i].getDeclaredMethod(methodName, parameterTypes);
                    } catch (NoSuchMethodException e) {
                    }
                    if (method != null) {
                        break;
                    }
                    method = getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
                    if (method != null) {
                        break;
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return method;
    }

    public static Method getMatchingAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        Method method;
        try {
            method = cls.getMethod(methodName, parameterTypes);
            MemberUtils.setAccessibleWorkaround(method);
            return method;
        } catch (NoSuchMethodException e) {
            method = null;
            for (Method method2 : cls.getMethods()) {
                if (method2.getName().equals(methodName) && ClassUtils.isAssignable((Class[]) parameterTypes, method2.getParameterTypes(), true)) {
                    Method accessibleMethod = getAccessibleMethod(method2);
                    if (accessibleMethod != null && (method == null || MemberUtils.compareParameterTypes(accessibleMethod.getParameterTypes(), method.getParameterTypes(), parameterTypes) < 0)) {
                        method = accessibleMethod;
                    }
                }
            }
            if (method != null) {
                MemberUtils.setAccessibleWorkaround(method);
            }
            return method;
        }
    }
}
