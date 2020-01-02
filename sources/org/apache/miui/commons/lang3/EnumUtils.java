package org.apache.miui.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumUtils {
    public static <E extends Enum<E>> Map<String, E> getEnumMap(Class<E> enumClass) {
        Map<String, E> map = new LinkedHashMap();
        for (E e : (Enum[]) enumClass.getEnumConstants()) {
            map.put(e.name(), e);
        }
        return map;
    }

    public static <E extends Enum<E>> List<E> getEnumList(Class<E> enumClass) {
        return new ArrayList(Arrays.asList((Enum[]) enumClass.getEnumConstants()));
    }

    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName) {
        if (enumName == null) {
            return false;
        }
        try {
            Enum.valueOf(enumClass, enumName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName) {
        E e = null;
        if (enumName == null) {
            return null;
        }
        try {
            e = Enum.valueOf(enumClass, enumName);
            return e;
        } catch (IllegalArgumentException e2) {
            return e;
        }
    }

    public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, Iterable<E> values) {
        checkBitVectorable(enumClass);
        Validate.notNull(values);
        long total = 0;
        for (E constant : values) {
            total |= (long) (1 << constant.ordinal());
        }
        return total;
    }

    public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, E... values) {
        Validate.noNullElements((Object[]) values);
        return generateBitVector((Class) enumClass, Arrays.asList(values));
    }

    public static <E extends Enum<E>> EnumSet<E> processBitVector(Class<E> enumClass, long value) {
        Enum[] constants = (Enum[]) checkBitVectorable(enumClass).getEnumConstants();
        EnumSet<E> results = EnumSet.noneOf(enumClass);
        for (E constant : constants) {
            if ((((long) (1 << constant.ordinal())) & value) != 0) {
                results.add(constant);
            }
        }
        return results;
    }

    private static <E extends Enum<E>> Class<E> checkBitVectorable(Class<E> enumClass) {
        Validate.notNull(enumClass, "EnumClass must be defined.", new Object[0]);
        Enum[] constants = (Enum[]) enumClass.getEnumConstants();
        Validate.isTrue(constants != null, "%s does not seem to be an Enum type", enumClass);
        Validate.isTrue(constants.length <= 64, "Cannot store %s %s values in %s bits", Integer.valueOf(constants.length), enumClass.getSimpleName(), Integer.valueOf(64));
        return enumClass;
    }
}
