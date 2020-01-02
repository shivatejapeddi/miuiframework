package org.apache.miui.commons.lang3.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.miui.commons.lang3.ClassUtils;

public class TypeUtils {
    public static boolean isAssignable(Type type, Type toType) {
        return isAssignable(type, toType, null);
    }

    private static boolean isAssignable(Type type, Type toType, Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (toType == null || (toType instanceof Class)) {
            return isAssignable(type, (Class) toType);
        }
        if (toType instanceof ParameterizedType) {
            return isAssignable(type, (ParameterizedType) toType, (Map) typeVarAssigns);
        }
        if (toType instanceof GenericArrayType) {
            return isAssignable(type, (GenericArrayType) toType, (Map) typeVarAssigns);
        }
        if (toType instanceof WildcardType) {
            return isAssignable(type, (WildcardType) toType, (Map) typeVarAssigns);
        }
        if (toType instanceof TypeVariable) {
            return isAssignable(type, (TypeVariable) toType, (Map) typeVarAssigns);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("found an unhandled type: ");
        stringBuilder.append(toType);
        throw new IllegalStateException(stringBuilder.toString());
    }

    private static boolean isAssignable(Type type, Class<?> toClass) {
        boolean z = true;
        if (type == null) {
            if (toClass != null && toClass.isPrimitive()) {
                z = false;
            }
            return z;
        } else if (toClass == null) {
            return false;
        } else {
            if (toClass.equals(type)) {
                return true;
            }
            if (type instanceof Class) {
                return ClassUtils.isAssignable((Class) type, (Class) toClass);
            }
            if (type instanceof ParameterizedType) {
                return isAssignable(getRawType((ParameterizedType) type), (Class) toClass);
            }
            if (type instanceof TypeVariable) {
                for (Type bound : ((TypeVariable) type).getBounds()) {
                    if (isAssignable(bound, (Class) toClass)) {
                        return true;
                    }
                }
                return false;
            } else if (type instanceof GenericArrayType) {
                if (!(toClass.equals(Object.class) || (toClass.isArray() && isAssignable(((GenericArrayType) type).getGenericComponentType(), toClass.getComponentType())))) {
                    z = false;
                }
                return z;
            } else if (type instanceof WildcardType) {
                return false;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("found an unhandled type: ");
                stringBuilder.append(type);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }
    }

    private static boolean isAssignable(Type type, ParameterizedType toParameterizedType, Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (type == null) {
            return true;
        }
        if (toParameterizedType == null) {
            return false;
        }
        if (toParameterizedType.equals(type)) {
            return true;
        }
        Class toClass = getRawType(toParameterizedType);
        Map<TypeVariable<?>, Type> fromTypeVarAssigns = getTypeArguments(type, toClass, (Map) null);
        if (fromTypeVarAssigns == null) {
            return false;
        }
        if (fromTypeVarAssigns.isEmpty()) {
            return true;
        }
        for (Entry<TypeVariable<?>, Type> entry : getTypeArguments(toParameterizedType, toClass, (Map) typeVarAssigns).entrySet()) {
            Type toTypeArg = (Type) entry.getValue();
            Type fromTypeArg = (Type) fromTypeVarAssigns.get(entry.getKey());
            if (fromTypeArg != null && !toTypeArg.equals(fromTypeArg) && (!(toTypeArg instanceof WildcardType) || !isAssignable(fromTypeArg, toTypeArg, (Map) typeVarAssigns))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAssignable(Type type, GenericArrayType toGenericArrayType, Map<TypeVariable<?>, Type> typeVarAssigns) {
        boolean z = true;
        if (type == null) {
            return true;
        }
        if (toGenericArrayType == null) {
            return false;
        }
        if (toGenericArrayType.equals(type)) {
            return true;
        }
        Type toComponentType = toGenericArrayType.getGenericComponentType();
        if (type instanceof Class) {
            Class<?> cls = (Class) type;
            if (!(cls.isArray() && isAssignable(cls.getComponentType(), toComponentType, (Map) typeVarAssigns))) {
                z = false;
            }
            return z;
        } else if (type instanceof GenericArrayType) {
            return isAssignable(((GenericArrayType) type).getGenericComponentType(), toComponentType, (Map) typeVarAssigns);
        } else {
            if (type instanceof WildcardType) {
                for (Type bound : getImplicitUpperBounds((WildcardType) type)) {
                    if (isAssignable(bound, (Type) toGenericArrayType)) {
                        return true;
                    }
                }
                return false;
            } else if (type instanceof TypeVariable) {
                for (Type bound2 : getImplicitBounds((TypeVariable) type)) {
                    if (isAssignable(bound2, (Type) toGenericArrayType)) {
                        return true;
                    }
                }
                return false;
            } else if (type instanceof ParameterizedType) {
                return false;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("found an unhandled type: ");
                stringBuilder.append(type);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }
    }

    private static boolean isAssignable(Type type, WildcardType toWildcardType, Map<TypeVariable<?>, Type> typeVarAssigns) {
        Type type2 = type;
        WildcardType wildcardType = toWildcardType;
        Map map = typeVarAssigns;
        if (type2 == null) {
            return true;
        }
        if (wildcardType == null) {
            return false;
        }
        if (wildcardType.equals(type2)) {
            return true;
        }
        Type[] toUpperBounds = getImplicitUpperBounds(toWildcardType);
        Type[] toLowerBounds = getImplicitLowerBounds(toWildcardType);
        if (type2 instanceof WildcardType) {
            Type toBound;
            WildcardType wildcardType2 = (WildcardType) type2;
            Type[] upperBounds = getImplicitUpperBounds(wildcardType2);
            Type[] lowerBounds = getImplicitLowerBounds(wildcardType2);
            for (Type toBound2 : toUpperBounds) {
                toBound2 = substituteTypeVariables(toBound2, map);
                for (Type bound : upperBounds) {
                    if (!isAssignable(bound, toBound2, map)) {
                        return false;
                    }
                }
            }
            for (Type toBound22 : toLowerBounds) {
                toBound22 = substituteTypeVariables(toBound22, map);
                for (Type bound2 : lowerBounds) {
                    if (!isAssignable(toBound22, bound2, map)) {
                        return false;
                    }
                }
            }
            return true;
        }
        for (Type toBound3 : toUpperBounds) {
            if (!isAssignable(type2, substituteTypeVariables(toBound3, map), map)) {
                return false;
            }
        }
        for (Type toBound32 : toLowerBounds) {
            if (!isAssignable(substituteTypeVariables(toBound32, map), type2, map)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAssignable(Type type, TypeVariable<?> toTypeVariable, Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (type == null) {
            return true;
        }
        if (toTypeVariable == null) {
            return false;
        }
        if (toTypeVariable.equals(type)) {
            return true;
        }
        if (type instanceof TypeVariable) {
            for (Type bound : getImplicitBounds((TypeVariable) type)) {
                if (isAssignable(bound, (TypeVariable) toTypeVariable, (Map) typeVarAssigns)) {
                    return true;
                }
            }
        }
        if ((type instanceof Class) || (type instanceof ParameterizedType) || (type instanceof GenericArrayType) || (type instanceof WildcardType)) {
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("found an unhandled type: ");
        stringBuilder.append(type);
        throw new IllegalStateException(stringBuilder.toString());
    }

    private static Type substituteTypeVariables(Type type, Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (!(type instanceof TypeVariable) || typeVarAssigns == null) {
            return type;
        }
        Type replacementType = (Type) typeVarAssigns.get(type);
        if (replacementType != null) {
            return replacementType;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("missing assignment type for type variable ");
        stringBuilder.append(type);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static Map<TypeVariable<?>, Type> getTypeArguments(ParameterizedType type) {
        return getTypeArguments(type, getRawType(type), null);
    }

    public static Map<TypeVariable<?>, Type> getTypeArguments(Type type, Class<?> toClass) {
        return getTypeArguments(type, (Class) toClass, null);
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(Type type, Class<?> toClass, Map<TypeVariable<?>, Type> subtypeVarAssigns) {
        if (type instanceof Class) {
            return getTypeArguments((Class) type, (Class) toClass, (Map) subtypeVarAssigns);
        }
        if (type instanceof ParameterizedType) {
            return getTypeArguments((ParameterizedType) type, (Class) toClass, (Map) subtypeVarAssigns);
        }
        if (type instanceof GenericArrayType) {
            return getTypeArguments(((GenericArrayType) type).getGenericComponentType(), toClass.isArray() ? toClass.getComponentType() : toClass, (Map) subtypeVarAssigns);
        }
        int i = 0;
        Type[] implicitUpperBounds;
        int length;
        Type bound;
        if (type instanceof WildcardType) {
            implicitUpperBounds = getImplicitUpperBounds((WildcardType) type);
            length = implicitUpperBounds.length;
            while (i < length) {
                bound = implicitUpperBounds[i];
                if (isAssignable(bound, (Class) toClass)) {
                    return getTypeArguments(bound, (Class) toClass, (Map) subtypeVarAssigns);
                }
                i++;
            }
            return null;
        } else if (type instanceof TypeVariable) {
            implicitUpperBounds = getImplicitBounds((TypeVariable) type);
            length = implicitUpperBounds.length;
            while (i < length) {
                bound = implicitUpperBounds[i];
                if (isAssignable(bound, (Class) toClass)) {
                    return getTypeArguments(bound, (Class) toClass, (Map) subtypeVarAssigns);
                }
                i++;
            }
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("found an unhandled type: ");
            stringBuilder.append(type);
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(ParameterizedType parameterizedType, Class<?> toClass, Map<TypeVariable<?>, Type> subtypeVarAssigns) {
        Type cls = getRawType(parameterizedType);
        if (!isAssignable(cls, (Class) toClass)) {
            return null;
        }
        Map typeVarAssigns;
        Type ownerType = parameterizedType.getOwnerType();
        if (ownerType instanceof ParameterizedType) {
            ParameterizedType typeVarAssigns2 = (ParameterizedType) ownerType;
            typeVarAssigns = getTypeArguments(typeVarAssigns2, getRawType(typeVarAssigns2), (Map) subtypeVarAssigns);
        } else if (subtypeVarAssigns == null) {
            typeVarAssigns = new HashMap();
        } else {
            typeVarAssigns = new HashMap(subtypeVarAssigns);
        }
        Type[] typeArgs = parameterizedType.getActualTypeArguments();
        TypeVariable<?>[] typeParams = cls.getTypeParameters();
        for (int i = 0; i < typeParams.length; i++) {
            Type typeArg = typeArgs[i];
            typeVarAssigns.put(typeParams[i], typeVarAssigns.containsKey(typeArg) ? (Type) typeVarAssigns.get(typeArg) : typeArg);
        }
        if (toClass.equals(cls)) {
            return typeVarAssigns;
        }
        return getTypeArguments(getClosestParentType(cls, toClass), (Class) toClass, typeVarAssigns);
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(Class<?> cls, Class<?> toClass, Map<TypeVariable<?>, Type> subtypeVarAssigns) {
        if (!isAssignable((Type) cls, (Class) toClass)) {
            return null;
        }
        Map typeVarAssigns;
        if (cls.isPrimitive()) {
            if (toClass.isPrimitive()) {
                return new HashMap();
            }
            cls = ClassUtils.primitiveToWrapper(cls);
        }
        if (subtypeVarAssigns == null) {
            typeVarAssigns = new HashMap();
        } else {
            typeVarAssigns = new HashMap(subtypeVarAssigns);
        }
        if (cls.getTypeParameters().length > 0 || toClass.equals(cls)) {
            return typeVarAssigns;
        }
        return getTypeArguments(getClosestParentType(cls, toClass), (Class) toClass, typeVarAssigns);
    }

    public static Map<TypeVariable<?>, Type> determineTypeArguments(Class<?> cls, ParameterizedType superType) {
        Class superClass = getRawType(superType);
        if (!isAssignable((Type) cls, superClass)) {
            return null;
        }
        if (cls.equals(superClass)) {
            return getTypeArguments(superType, superClass, null);
        }
        Type midType = getClosestParentType(cls, superClass);
        if (midType instanceof Class) {
            return determineTypeArguments((Class) midType, superType);
        }
        ParameterizedType midParameterizedType = (ParameterizedType) midType;
        Map<TypeVariable<?>, Type> typeVarAssigns = determineTypeArguments(getRawType(midParameterizedType), superType);
        mapTypeVariablesToArguments(cls, midParameterizedType, typeVarAssigns);
        return typeVarAssigns;
    }

    private static <T> void mapTypeVariablesToArguments(Class<T> cls, ParameterizedType parameterizedType, Map<TypeVariable<?>, Type> typeVarAssigns) {
        Type ownerType = parameterizedType.getOwnerType();
        if (ownerType instanceof ParameterizedType) {
            mapTypeVariablesToArguments(cls, (ParameterizedType) ownerType, typeVarAssigns);
        }
        Type[] typeArgs = parameterizedType.getActualTypeArguments();
        TypeVariable<?>[] typeVars = getRawType(parameterizedType).getTypeParameters();
        List<TypeVariable<Class<T>>> typeVarList = Arrays.asList(cls.getTypeParameters());
        for (int i = 0; i < typeArgs.length; i++) {
            TypeVariable<?> typeVar = typeVars[i];
            Type typeArg = typeArgs[i];
            if (typeVarList.contains(typeArg) && typeVarAssigns.containsKey(typeVar)) {
                typeVarAssigns.put((TypeVariable) typeArg, (Type) typeVarAssigns.get(typeVar));
            }
        }
    }

    private static Type getClosestParentType(Class<?> cls, Class<?> superClass) {
        if (superClass.isInterface()) {
            Type genericInterface = null;
            for (Type midType : cls.getGenericInterfaces()) {
                Type midClass;
                if (midType instanceof ParameterizedType) {
                    midClass = getRawType((ParameterizedType) midType);
                } else if (midType instanceof Class) {
                    midClass = (Class) midType;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unexpected generic interface type found: ");
                    stringBuilder.append(midType);
                    throw new IllegalStateException(stringBuilder.toString());
                }
                if (isAssignable(midClass, (Class) superClass) && isAssignable(genericInterface, midClass)) {
                    genericInterface = midType;
                }
            }
            if (genericInterface != null) {
                return genericInterface;
            }
        }
        return cls.getGenericSuperclass();
    }

    public static boolean isInstance(Object value, Type type) {
        boolean z = false;
        if (type == null) {
            return false;
        }
        if (value != null) {
            z = isAssignable(value.getClass(), type, null);
        } else if (!((type instanceof Class) && ((Class) type).isPrimitive())) {
            z = true;
        }
        return z;
    }

    public static Type[] normalizeUpperBounds(Type[] bounds) {
        if (bounds.length < 2) {
            return bounds;
        }
        Set<Type> types = new HashSet(bounds.length);
        for (Type type1 : bounds) {
            boolean subtypeFound = false;
            for (Type type2 : bounds) {
                if (type1 != type2 && isAssignable(type2, type1, null)) {
                    subtypeFound = true;
                    break;
                }
            }
            if (!subtypeFound) {
                types.add(type1);
            }
        }
        return (Type[]) types.toArray(new Type[types.size()]);
    }

    public static Type[] getImplicitBounds(TypeVariable<?> typeVariable) {
        Type[] bounds = typeVariable.getBounds();
        if (bounds.length != 0) {
            return normalizeUpperBounds(bounds);
        }
        return new Type[]{Object.class};
    }

    public static Type[] getImplicitUpperBounds(WildcardType wildcardType) {
        Type[] bounds = wildcardType.getUpperBounds();
        if (bounds.length != 0) {
            return normalizeUpperBounds(bounds);
        }
        return new Type[]{Object.class};
    }

    public static Type[] getImplicitLowerBounds(WildcardType wildcardType) {
        Type[] bounds = wildcardType.getLowerBounds();
        if (bounds.length != 0) {
            return bounds;
        }
        return new Type[]{null};
    }

    public static boolean typesSatisfyVariables(Map<TypeVariable<?>, Type> typeVarAssigns) {
        for (Entry<TypeVariable<?>, Type> entry : typeVarAssigns.entrySet()) {
            Type type = (Type) entry.getValue();
            for (Type bound : getImplicitBounds((TypeVariable) entry.getKey())) {
                if (!isAssignable(type, substituteTypeVariables(bound, typeVarAssigns), (Map) typeVarAssigns)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Class<?> getRawType(ParameterizedType parameterizedType) {
        Type rawType = parameterizedType.getRawType();
        if (rawType instanceof Class) {
            return (Class) rawType;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Wait... What!? Type of rawType: ");
        stringBuilder.append(rawType);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public static Class<?> getRawType(Type type, Type assigningType) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getRawType((ParameterizedType) type);
        }
        if (type instanceof TypeVariable) {
            if (assigningType == null) {
                return null;
            }
            Object genericDeclaration = ((TypeVariable) type).getGenericDeclaration();
            if (!(genericDeclaration instanceof Class)) {
                return null;
            }
            Map<TypeVariable<?>, Type> typeVarAssigns = getTypeArguments(assigningType, (Class) genericDeclaration);
            if (typeVarAssigns == null) {
                return null;
            }
            Type typeArgument = (Type) typeVarAssigns.get(type);
            if (typeArgument == null) {
                return null;
            }
            return getRawType(typeArgument, assigningType);
        } else if (type instanceof GenericArrayType) {
            return Array.newInstance(getRawType(((GenericArrayType) type).getGenericComponentType(), assigningType), 0).getClass();
        } else {
            if (type instanceof WildcardType) {
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unknown type: ");
            stringBuilder.append(type);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static boolean isArrayType(Type type) {
        return (type instanceof GenericArrayType) || ((type instanceof Class) && ((Class) type).isArray());
    }

    public static Type getArrayComponentType(Type type) {
        Type type2 = null;
        if (type instanceof Class) {
            Class<?> clazz = (Class) type;
            if (clazz.isArray()) {
                type2 = clazz.getComponentType();
            }
            return type2;
        } else if (type instanceof GenericArrayType) {
            return ((GenericArrayType) type).getGenericComponentType();
        } else {
            return null;
        }
    }
}
