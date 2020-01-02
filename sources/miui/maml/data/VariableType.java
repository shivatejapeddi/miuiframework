package miui.maml.data;

public enum VariableType {
    INVALID(null),
    NUM(Double.TYPE),
    STR(String.class),
    OBJ(Object.class),
    NUM_ARR(Double.TYPE),
    DOUBLE_ARR(Double.TYPE),
    FLOAT_ARR(Float.TYPE),
    INT_ARR(Integer.TYPE),
    SHORT_ARR(Short.TYPE),
    BYTE_ARR(Byte.TYPE),
    LONG_ARR(Long.TYPE),
    BOOLEAN_ARR(Boolean.TYPE),
    CHAR_ARR(Character.TYPE),
    STR_ARR(String.class),
    OBJ_ARR(Object.class);
    
    public final Class<?> mTypeClass;

    public static VariableType parseType(String s) {
        if ("number".equalsIgnoreCase(s)) {
            return NUM;
        }
        if ("string".equalsIgnoreCase(s)) {
            return STR;
        }
        if ("object".equalsIgnoreCase(s)) {
            return OBJ;
        }
        if ("number[]".equalsIgnoreCase(s)) {
            return NUM_ARR;
        }
        if ("double[]".equalsIgnoreCase(s)) {
            return DOUBLE_ARR;
        }
        if ("float[]".equalsIgnoreCase(s)) {
            return FLOAT_ARR;
        }
        if ("int[]".equalsIgnoreCase(s)) {
            return INT_ARR;
        }
        if ("short[]".equalsIgnoreCase(s)) {
            return SHORT_ARR;
        }
        if ("byte[]".equalsIgnoreCase(s)) {
            return BYTE_ARR;
        }
        if ("long[]".equalsIgnoreCase(s)) {
            return LONG_ARR;
        }
        if ("boolean[]".equalsIgnoreCase(s)) {
            return BOOLEAN_ARR;
        }
        if ("char[]".equalsIgnoreCase(s)) {
            return CHAR_ARR;
        }
        if ("string[]".equalsIgnoreCase(s)) {
            return STR_ARR;
        }
        if ("object[]".equalsIgnoreCase(s)) {
            return OBJ_ARR;
        }
        return NUM;
    }

    private VariableType(Class<?> clazz) {
        this.mTypeClass = clazz;
    }

    public boolean isNumber() {
        return this == NUM;
    }

    public boolean isNumberArray() {
        return ordinal() >= NUM_ARR.ordinal() && ordinal() <= CHAR_ARR.ordinal();
    }

    public boolean isArray() {
        return ordinal() >= NUM_ARR.ordinal() && ordinal() <= OBJ_ARR.ordinal();
    }
}
