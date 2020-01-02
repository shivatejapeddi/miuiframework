package org.apache.miui.commons.lang3.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import org.apache.miui.commons.lang3.ClassUtils;

public abstract class MemberUtils {
    private static final int ACCESS_TEST = 7;
    private static final Class<?>[] ORDERED_PRIMITIVE_TYPES = new Class[]{Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};

    static void setAccessibleWorkaround(AccessibleObject o) {
        if (o != null && !o.isAccessible()) {
            Member m = (Member) o;
            if (Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
                try {
                    o.setAccessible(true);
                } catch (SecurityException e) {
                }
            }
        }
    }

    static boolean isPackageAccess(int modifiers) {
        return (modifiers & 7) == 0;
    }

    static boolean isAccessible(Member m) {
        return (m == null || !Modifier.isPublic(m.getModifiers()) || m.isSynthetic()) ? false : true;
    }

    public static int compareParameterTypes(Class<?>[] left, Class<?>[] right, Class<?>[] actual) {
        float leftCost = getTotalTransformationCost(actual, left);
        float rightCost = getTotalTransformationCost(actual, right);
        if (leftCost < rightCost) {
            return -1;
        }
        return rightCost < leftCost ? 1 : 0;
    }

    private static float getTotalTransformationCost(Class<?>[] srcArgs, Class<?>[] destArgs) {
        float totalCost = 0.0f;
        for (int i = 0; i < srcArgs.length; i++) {
            totalCost += getObjectTransformationCost(srcArgs[i], destArgs[i]);
        }
        return totalCost;
    }

    private static float getObjectTransformationCost(Class<?> srcClass, Class<?> destClass) {
        if (destClass.isPrimitive()) {
            return getPrimitivePromotionCost(srcClass, destClass);
        }
        Class srcClass2;
        float cost = 0.0f;
        while (srcClass2 != null && !destClass.equals(srcClass2)) {
            if (destClass.isInterface() && ClassUtils.isAssignable(srcClass2, (Class) destClass)) {
                cost += 0.25f;
                break;
            }
            cost += 1.0f;
            srcClass2 = srcClass2.getSuperclass();
        }
        if (srcClass2 == null) {
            cost += 1.5f;
        }
        return cost;
    }

    private static float getPrimitivePromotionCost(Class<?> srcClass, Class<?> destClass) {
        float cost = 0.0f;
        Class<?> cls = srcClass;
        if (!cls.isPrimitive()) {
            cost = 0.0f + 0.1f;
            cls = ClassUtils.wrapperToPrimitive(cls);
        }
        int i = 0;
        while (cls != destClass) {
            Class[] clsArr = ORDERED_PRIMITIVE_TYPES;
            if (i >= clsArr.length) {
                break;
            }
            if (cls == clsArr[i]) {
                cost += 0.1f;
                if (i < clsArr.length - 1) {
                    cls = clsArr[i + 1];
                }
            }
            i++;
        }
        return cost;
    }
}
