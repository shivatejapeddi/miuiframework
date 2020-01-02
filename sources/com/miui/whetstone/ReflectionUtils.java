package com.miui.whetstone;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.WeakHashMap;
import org.apache.miui.commons.lang3.ClassUtils;
import org.apache.miui.commons.lang3.reflect.MemberUtils;

@Deprecated
public class ReflectionUtils {
    public static final ClassLoader BOOTCLASSLOADER = ClassLoader.getSystemClassLoader();
    private static final WeakHashMap<Object, HashMap<String, Object>> additionalFields = new WeakHashMap();
    private static final HashMap<String, Constructor<?>> constructorCache = new HashMap();
    private static final HashMap<String, Field> fieldCache = new HashMap();
    private static final HashMap<String, Method> methodCache = new HashMap();

    public static class ClassNotFoundError extends Error {
        private static final long serialVersionUID = -1070936889459514628L;

        public ClassNotFoundError(Throwable cause) {
            super(cause);
        }

        public ClassNotFoundError(String detailMessage, Throwable cause) {
            super(detailMessage, cause);
        }
    }

    public static class InvocationTargetError extends Error {
        private static final long serialVersionUID = -1070936889459514628L;

        public InvocationTargetError(Throwable cause) {
            super(cause);
        }

        public InvocationTargetError(String detailMessage, Throwable cause) {
            super(detailMessage, cause);
        }
    }

    public static Class<?> findClass(String className, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = BOOTCLASSLOADER;
        }
        try {
            return ClassUtils.getClass(classLoader, className, false);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundError(e);
        }
    }

    public static Field findField(Class<?> clazz, String fieldName) {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append('#');
        sb.append(fieldName);
        String fullFieldName = sb.toString();
        Field field;
        if (fieldCache.containsKey(fullFieldName)) {
            field = (Field) fieldCache.get(fullFieldName);
            if (field != null) {
                return field;
            }
            throw new NoSuchFieldError(fullFieldName);
        }
        try {
            field = findFieldRecursiveImpl(clazz, fieldName);
            field.setAccessible(true);
            fieldCache.put(fullFieldName, field);
            return field;
        } catch (NoSuchFieldException e) {
            fieldCache.put(fullFieldName, null);
            throw new NoSuchFieldError(fullFieldName);
        }
    }

    private static Field findFieldRecursiveImpl(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            while (true) {
                clazz = clazz.getSuperclass();
                if (clazz == null || clazz.equals(Object.class)) {
                    throw e;
                }
                try {
                    return clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e2) {
                }
            }
            throw e;
        }
    }

    public static Method findMethodExact(Class<?> clazz, String methodName, Object... parameterTypes) {
        Class[] parameterClasses = null;
        int i = parameterTypes.length - 1;
        while (i >= 0) {
            Object type = parameterTypes[i];
            if (type != null) {
                if (parameterClasses == null) {
                    parameterClasses = new Class[(i + 1)];
                }
                if (type instanceof Class) {
                    parameterClasses[i] = (Class) type;
                } else if (type instanceof String) {
                    parameterClasses[i] = findClass((String) type, clazz.getClassLoader());
                } else {
                    throw new ClassNotFoundError("parameter type must either be specified as Class or String", null);
                }
                i--;
            } else {
                throw new ClassNotFoundError("parameter type must not be null", null);
            }
        }
        if (parameterClasses == null) {
            parameterClasses = new Class[0];
        }
        return findMethodExact((Class) clazz, methodName, parameterClasses);
    }

    public static Method findMethodExact(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append('#');
        sb.append(methodName);
        sb.append(getParametersString(parameterTypes));
        sb.append("#exact");
        String fullMethodName = sb.toString();
        Method method;
        if (methodCache.containsKey(fullMethodName)) {
            method = (Method) methodCache.get(fullMethodName);
            if (method != null) {
                return method;
            }
            throw new NoSuchMethodError(fullMethodName);
        }
        try {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            methodCache.put(fullMethodName, method);
            return method;
        } catch (NoSuchMethodException e) {
            methodCache.put(fullMethodName, null);
            throw new NoSuchMethodError(fullMethodName);
        }
    }

    public static Method findMethodBestMatch(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append('#');
        sb.append(methodName);
        sb.append(getParametersString(parameterTypes));
        sb.append("#bestmatch");
        String fullMethodName = sb.toString();
        Method method;
        if (methodCache.containsKey(fullMethodName)) {
            method = (Method) methodCache.get(fullMethodName);
            if (method != null) {
                return method;
            }
            throw new NoSuchMethodError(fullMethodName);
        }
        try {
            method = findMethodExact((Class) clazz, methodName, (Class[]) parameterTypes);
            methodCache.put(fullMethodName, method);
            return method;
        } catch (NoSuchMethodError e) {
            method = null;
            for (Method method2 : clazz.getDeclaredMethods()) {
                if (method2.getName().equals(methodName) && ClassUtils.isAssignable((Class[]) parameterTypes, method2.getParameterTypes(), true) && (method == null || MemberUtils.compareParameterTypes(method2.getParameterTypes(), method.getParameterTypes(), parameterTypes) < 0)) {
                    method = method2;
                }
            }
            if (method != null) {
                method.setAccessible(true);
                methodCache.put(fullMethodName, method);
                return method;
            }
            NoSuchMethodError e2 = new NoSuchMethodError(fullMethodName);
            methodCache.put(fullMethodName, null);
            throw e2;
        }
    }

    public static Method findMethodBestMatch(Class<?> clazz, String methodName, Object... args) {
        return findMethodBestMatch((Class) clazz, methodName, getParameterTypes(args));
    }

    public static Class<?>[] getParameterTypes(Object... args) {
        Class<?>[] clazzes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            clazzes[i] = args[i] != null ? args[i].getClass() : null;
        }
        return clazzes;
    }

    public static Class<?>[] getClassesAsArray(Class<?>... clazzes) {
        return clazzes;
    }

    private static String getParametersString(Class<?>... clazzes) {
        StringBuilder sb = new StringBuilder("(");
        boolean first = true;
        for (Class<?> clazz : clazzes) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            if (clazz != null) {
                sb.append(clazz.getCanonicalName());
            } else {
                sb.append("null");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public static Constructor<?> findConstructorExact(Class<?> clazz, Class<?>... parameterTypes) {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append(getParametersString(parameterTypes));
        sb.append("#exact");
        String fullConstructorName = sb.toString();
        Constructor<?> constructor;
        if (constructorCache.containsKey(fullConstructorName)) {
            constructor = (Constructor) constructorCache.get(fullConstructorName);
            if (constructor != null) {
                return constructor;
            }
            throw new NoSuchMethodError(fullConstructorName);
        }
        try {
            constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            constructorCache.put(fullConstructorName, constructor);
            return constructor;
        } catch (NoSuchMethodException e) {
            constructorCache.put(fullConstructorName, null);
            throw new NoSuchMethodError(fullConstructorName);
        }
    }

    public static Constructor<?> findConstructorBestMatch(Class<?> clazz, Class<?>... parameterTypes) {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append(getParametersString(parameterTypes));
        sb.append("#bestmatch");
        String fullConstructorName = sb.toString();
        Constructor<?> constructor;
        if (constructorCache.containsKey(fullConstructorName)) {
            constructor = (Constructor) constructorCache.get(fullConstructorName);
            if (constructor != null) {
                return constructor;
            }
            throw new NoSuchMethodError(fullConstructorName);
        }
        try {
            constructor = findConstructorExact(clazz, parameterTypes);
            constructorCache.put(fullConstructorName, constructor);
            return constructor;
        } catch (NoSuchMethodError e) {
            constructor = null;
            for (Constructor<?> constructor2 : clazz.getDeclaredConstructors()) {
                if (ClassUtils.isAssignable((Class[]) parameterTypes, constructor2.getParameterTypes(), true) && (constructor == null || MemberUtils.compareParameterTypes(constructor2.getParameterTypes(), constructor.getParameterTypes(), parameterTypes) < 0)) {
                    constructor = constructor2;
                }
            }
            if (constructor != null) {
                constructor.setAccessible(true);
                constructorCache.put(fullConstructorName, constructor);
                return constructor;
            }
            NoSuchMethodError e2 = new NoSuchMethodError(fullConstructorName);
            constructorCache.put(fullConstructorName, null);
            throw e2;
        }
    }

    public static Constructor<?> findConstructorBestMatch(Class<?> clazz, Object... args) {
        return findConstructorBestMatch((Class) clazz, getParameterTypes(args));
    }

    public static void setObjectField(Object obj, String fieldName, Object value) {
        try {
            findField(obj.getClass(), fieldName).set(obj, value);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e2) {
            throw e2;
        }
    }

    public static Object getObjectField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).get(obj);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e2) {
            throw e2;
        }
    }

    public static Object getSurroundingThis(Object obj) {
        return getObjectField(obj, "this$0");
    }

    public static void setStaticObjectField(Class<?> clazz, String fieldName, Object value) {
        try {
            findField(clazz, fieldName).set(null, value);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e2) {
            throw e2;
        }
    }

    public static Object getStaticObjectField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e2) {
            throw e2;
        }
    }

    public static Object callMethod(Object obj, String methodName, Object... args) {
        try {
            return findMethodBestMatch(obj.getClass(), methodName, args).invoke(obj, args);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e2) {
            throw e2;
        } catch (InvocationTargetException e3) {
            throw new InvocationTargetError(e3.getCause());
        }
    }

    public static Object callStaticMethod(Class<?> clazz, String methodName, Object... args) {
        try {
            return findMethodBestMatch((Class) clazz, methodName, args).invoke(null, args);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e2) {
            throw e2;
        } catch (InvocationTargetException e3) {
            throw new InvocationTargetError(e3.getCause());
        }
    }

    public static Object newInstance(Class<?> clazz, Object... args) {
        try {
            return findConstructorBestMatch((Class) clazz, args).newInstance(args);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e2) {
            throw e2;
        } catch (InvocationTargetException e3) {
            throw new InvocationTargetError(e3.getCause());
        } catch (InstantiationException e4) {
            throw new InstantiationError(e4.getMessage());
        }
    }

    public static Object setAdditionalInstanceField(Object obj, String key, Object value) {
        if (obj == null) {
            throw new NullPointerException("object must not be null");
        } else if (key != null) {
            HashMap<String, Object> objectFields;
            Object put;
            synchronized (additionalFields) {
                objectFields = (HashMap) additionalFields.get(obj);
                if (objectFields == null) {
                    objectFields = new HashMap();
                    additionalFields.put(obj, objectFields);
                }
            }
            synchronized (objectFields) {
                put = objectFields.put(key, value);
            }
            return put;
        } else {
            throw new NullPointerException("key must not be null");
        }
    }

    public static Object getAdditionalInstanceField(Object obj, String key) {
        if (obj == null) {
            throw new NullPointerException("object must not be null");
        } else if (key != null) {
            synchronized (additionalFields) {
                HashMap<String, Object> objectFields = (HashMap) additionalFields.get(obj);
                if (objectFields == null) {
                    return null;
                }
                Object obj2;
                synchronized (objectFields) {
                    obj2 = objectFields.get(key);
                }
                return obj2;
            }
        } else {
            throw new NullPointerException("key must not be null");
        }
    }

    public static Object removeAdditionalInstanceField(Object obj, String key) {
        if (obj == null) {
            throw new NullPointerException("object must not be null");
        } else if (key != null) {
            synchronized (additionalFields) {
                HashMap<String, Object> objectFields = (HashMap) additionalFields.get(obj);
                if (objectFields == null) {
                    return null;
                }
                Object remove;
                synchronized (objectFields) {
                    remove = objectFields.remove(key);
                }
                return remove;
            }
        } else {
            throw new NullPointerException("key must not be null");
        }
    }

    public static Object setAdditionalStaticField(Object obj, String key, Object value) {
        return setAdditionalInstanceField(obj.getClass(), key, value);
    }

    public static Object getAdditionalStaticField(Object obj, String key) {
        return getAdditionalInstanceField(obj.getClass(), key);
    }

    public static Object removeAdditionalStaticField(Object obj, String key) {
        return removeAdditionalInstanceField(obj.getClass(), key);
    }

    public static Object setAdditionalStaticField(Class<?> clazz, String key, Object value) {
        return setAdditionalInstanceField(clazz, key, value);
    }

    public static Object getAdditionalStaticField(Class<?> clazz, String key) {
        return getAdditionalInstanceField(clazz, key);
    }

    public static Object removeAdditionalStaticField(Class<?> clazz, String key) {
        return removeAdditionalInstanceField(clazz, key);
    }
}
