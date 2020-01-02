package org.apache.miui.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.miui.commons.lang3.ArrayUtils;
import org.apache.miui.commons.lang3.tuple.Pair;

public class EqualsBuilder implements Builder<Boolean> {
    private static final ThreadLocal<Set<Pair<IDKey, IDKey>>> REGISTRY = new ThreadLocal();
    private boolean isEquals = true;

    static Set<Pair<IDKey, IDKey>> getRegistry() {
        return (Set) REGISTRY.get();
    }

    static Pair<IDKey, IDKey> getRegisterPair(Object lhs, Object rhs) {
        return Pair.of(new IDKey(lhs), new IDKey(rhs));
    }

    static boolean isRegistered(Object lhs, Object rhs) {
        Set<Pair<IDKey, IDKey>> registry = getRegistry();
        Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
        return registry != null && (registry.contains(pair) || registry.contains(Pair.of((IDKey) pair.getLeft(), (IDKey) pair.getRight())));
    }

    static void register(Object lhs, Object rhs) {
        synchronized (EqualsBuilder.class) {
            if (getRegistry() == null) {
                REGISTRY.set(new HashSet());
            }
        }
        getRegistry().add(getRegisterPair(lhs, rhs));
    }

    static void unregister(Object lhs, Object rhs) {
        Set<Pair<IDKey, IDKey>> registry = getRegistry();
        if (registry != null) {
            registry.remove(getRegisterPair(lhs, rhs));
            synchronized (EqualsBuilder.class) {
                registry = getRegistry();
                if (registry != null && registry.isEmpty()) {
                    REGISTRY.remove();
                }
            }
        }
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, Collection<String> excludeFields) {
        return reflectionEquals(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray((Collection) excludeFields));
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, String... excludeFields) {
        return reflectionEquals(lhs, rhs, false, null, excludeFields);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients) {
        return reflectionEquals(lhs, rhs, testTransients, null, new String[0]);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class<?> reflectUpToClass, String... excludeFields) {
        Object obj = lhs;
        Object obj2 = rhs;
        if (obj == obj2) {
            return true;
        }
        Class<?> cls;
        if (obj == null) {
            cls = reflectUpToClass;
        } else if (obj2 == null) {
            cls = reflectUpToClass;
        } else {
            Class<?> testClass;
            Class<?> lhsClass = lhs.getClass();
            Class<?> rhsClass = rhs.getClass();
            Class<?> testClass2;
            if (lhsClass.isInstance(obj2)) {
                testClass2 = lhsClass;
                if (rhsClass.isInstance(obj)) {
                    testClass = testClass2;
                } else {
                    testClass = rhsClass;
                }
            } else if (rhsClass.isInstance(obj)) {
                testClass2 = rhsClass;
                if (lhsClass.isInstance(obj2)) {
                    testClass = testClass2;
                } else {
                    testClass = lhsClass;
                }
            } else {
                cls = reflectUpToClass;
                return false;
            }
            EqualsBuilder equalsBuilder = new EqualsBuilder();
            try {
                reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
                while (testClass.getSuperclass() != null) {
                    if (testClass == reflectUpToClass) {
                        break;
                    }
                    try {
                        testClass = testClass.getSuperclass();
                        reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                }
                cls = reflectUpToClass;
                return equalsBuilder.isEquals();
            } catch (IllegalArgumentException e2) {
                cls = reflectUpToClass;
                return false;
            }
        }
        return false;
    }

    private static void reflectionAppend(Object lhs, Object rhs, Class<?> clazz, EqualsBuilder builder, boolean useTransients, String[] excludeFields) {
        if (!isRegistered(lhs, rhs)) {
            try {
                register(lhs, rhs);
                Field[] fields = clazz.getDeclaredFields();
                AccessibleObject.setAccessible(fields, true);
                for (int i = 0; i < fields.length && builder.isEquals; i++) {
                    Field f = fields[i];
                    if (!ArrayUtils.contains((Object[]) excludeFields, f.getName()) && f.getName().indexOf(36) == -1 && ((useTransients || !Modifier.isTransient(f.getModifiers())) && !Modifier.isStatic(f.getModifiers()))) {
                        builder.append(f.get(lhs), f.get(rhs));
                    }
                }
                unregister(lhs, rhs);
            } catch (IllegalAccessException e) {
                throw new InternalError("Unexpected IllegalAccessException");
            } catch (Throwable th) {
                unregister(lhs, rhs);
            }
        }
    }

    public EqualsBuilder appendSuper(boolean superEquals) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = superEquals;
        return this;
    }

    public EqualsBuilder append(Object lhs, Object rhs) {
        if (!this.isEquals || lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        }
        if (!lhs.getClass().isArray()) {
            this.isEquals = lhs.equals(rhs);
        } else if (lhs.getClass() != rhs.getClass()) {
            setEquals(false);
        } else if (lhs instanceof long[]) {
            append((long[]) lhs, (long[]) rhs);
        } else if (lhs instanceof int[]) {
            append((int[]) lhs, (int[]) rhs);
        } else if (lhs instanceof short[]) {
            append((short[]) lhs, (short[]) rhs);
        } else if (lhs instanceof char[]) {
            append((char[]) lhs, (char[]) rhs);
        } else if (lhs instanceof byte[]) {
            append((byte[]) lhs, (byte[]) rhs);
        } else if (lhs instanceof double[]) {
            append((double[]) lhs, (double[]) rhs);
        } else if (lhs instanceof float[]) {
            append((float[]) lhs, (float[]) rhs);
        } else if (lhs instanceof boolean[]) {
            append((boolean[]) lhs, (boolean[]) rhs);
        } else {
            append((Object[]) lhs, (Object[]) rhs);
        }
        return this;
    }

    public EqualsBuilder append(long lhs, long rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = lhs == rhs;
        return this;
    }

    public EqualsBuilder append(int lhs, int rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = lhs == rhs;
        return this;
    }

    public EqualsBuilder append(short lhs, short rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = lhs == rhs;
        return this;
    }

    public EqualsBuilder append(char lhs, char rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = lhs == rhs;
        return this;
    }

    public EqualsBuilder append(byte lhs, byte rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = lhs == rhs;
        return this;
    }

    public EqualsBuilder append(double lhs, double rhs) {
        if (this.isEquals) {
            return append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
        }
        return this;
    }

    public EqualsBuilder append(float lhs, float rhs) {
        if (this.isEquals) {
            return append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
        }
        return this;
    }

    public EqualsBuilder append(boolean lhs, boolean rhs) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = lhs == rhs;
        return this;
    }

    public EqualsBuilder append(Object[] lhs, Object[] rhs) {
        if (!this.isEquals || lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(long[] lhs, long[] rhs) {
        if (!this.isEquals || lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(int[] lhs, int[] rhs) {
        if (!this.isEquals || lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(short[] lhs, short[] rhs) {
        if (!this.isEquals || lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(char[] lhs, char[] rhs) {
        if (!this.isEquals || lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(byte[] lhs, byte[] rhs) {
        if (!this.isEquals || lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(double[] lhs, double[] rhs) {
        if (!this.isEquals || lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(float[] lhs, float[] rhs) {
        if (!this.isEquals || lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
        if (!this.isEquals || lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public boolean isEquals() {
        return this.isEquals;
    }

    public Boolean build() {
        return Boolean.valueOf(isEquals());
    }

    /* Access modifiers changed, original: protected */
    public void setEquals(boolean isEquals) {
        this.isEquals = isEquals;
    }

    public void reset() {
        this.isEquals = true;
    }
}
