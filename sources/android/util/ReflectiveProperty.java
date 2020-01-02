package android.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ReflectiveProperty<T, V> extends Property<T, V> {
    private static final String PREFIX_GET = "get";
    private static final String PREFIX_IS = "is";
    private static final String PREFIX_SET = "set";
    private Field mField;
    private Method mGetter;
    private Method mSetter;

    public ReflectiveProperty(Class<T> propertyHolder, Class<V> valueType, String name) {
        Class<T> cls = propertyHolder;
        Class<V> cls2 = valueType;
        String str = name;
        String str2 = ")";
        String str3 = ") does not match Property type (";
        String str4 = "Underlying type (";
        super(cls2, str);
        char firstLetter = Character.toUpperCase(str.charAt(0));
        String theRest = str.substring(1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(firstLetter);
        stringBuilder.append(theRest);
        String capitalizedName = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(PREFIX_GET);
        stringBuilder.append(capitalizedName);
        try {
            this.mGetter = cls.getMethod(stringBuilder.toString(), (Class[]) null);
        } catch (NoSuchMethodException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(PREFIX_IS);
            stringBuilder.append(capitalizedName);
            try {
                this.mGetter = cls.getMethod(stringBuilder.toString(), (Class[]) null);
            } catch (NoSuchMethodException e2) {
                NoSuchMethodException e1 = e2;
                this.mField = cls.getField(str);
                Class fieldType = this.mField.getType();
                if (!typesMatch(cls2, fieldType)) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str4);
                    stringBuilder2.append(fieldType);
                    stringBuilder2.append(str3);
                    stringBuilder2.append(cls2);
                    stringBuilder2.append(str2);
                    throw new NoSuchPropertyException(stringBuilder2.toString());
                }
                return;
            } catch (NoSuchFieldException e3) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("No accessor method or field found for property with name ");
                stringBuilder3.append(str);
                throw new NoSuchPropertyException(stringBuilder3.toString());
            }
        }
        Class getterType = this.mGetter.getReturnType();
        if (typesMatch(cls2, getterType)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(PREFIX_SET);
            stringBuilder.append(capitalizedName);
            str2 = stringBuilder.toString();
            try {
                this.mSetter = cls.getMethod(str2, new Class[]{getterType});
            } catch (NoSuchMethodException e4) {
            }
            return;
        }
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append(str4);
        stringBuilder4.append(getterType);
        stringBuilder4.append(str3);
        stringBuilder4.append(cls2);
        stringBuilder4.append(str2);
        throw new NoSuchPropertyException(stringBuilder4.toString());
    }

    private boolean typesMatch(Class<V> valueType, Class getterType) {
        boolean z = true;
        if (getterType == valueType) {
            return true;
        }
        if (!getterType.isPrimitive()) {
            return false;
        }
        if (!((getterType == Float.TYPE && valueType == Float.class) || ((getterType == Integer.TYPE && valueType == Integer.class) || ((getterType == Boolean.TYPE && valueType == Boolean.class) || ((getterType == Long.TYPE && valueType == Long.class) || ((getterType == Double.TYPE && valueType == Double.class) || ((getterType == Short.TYPE && valueType == Short.class) || ((getterType == Byte.TYPE && valueType == Byte.class) || (getterType == Character.TYPE && valueType == Character.class))))))))) {
            z = false;
        }
        return z;
    }

    public void set(T object, V value) {
        Method method = this.mSetter;
        if (method != null) {
            try {
                method.invoke(object, new Object[]{value});
                return;
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        }
        Field field = this.mField;
        if (field != null) {
            try {
                field.set(object, value);
                return;
            } catch (IllegalAccessException e3) {
                throw new AssertionError();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Property ");
        stringBuilder.append(getName());
        stringBuilder.append(" is read-only");
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    public V get(T object) {
        Method method = this.mGetter;
        if (method != null) {
            try {
                return method.invoke(object, (Object[]) null);
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        }
        Field field = this.mField;
        if (field != null) {
            try {
                return field.get(object);
            } catch (IllegalAccessException e3) {
                throw new AssertionError();
            }
        }
        throw new AssertionError();
    }

    public boolean isReadOnly() {
        return this.mSetter == null && this.mField == null;
    }
}
