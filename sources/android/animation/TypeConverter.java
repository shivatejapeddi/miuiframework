package android.animation;

public abstract class TypeConverter<T, V> {
    private Class<T> mFromClass;
    private Class<V> mToClass;

    public abstract V convert(T t);

    public TypeConverter(Class<T> fromClass, Class<V> toClass) {
        this.mFromClass = fromClass;
        this.mToClass = toClass;
    }

    /* Access modifiers changed, original: 0000 */
    public Class<V> getTargetType() {
        return this.mToClass;
    }

    /* Access modifiers changed, original: 0000 */
    public Class<T> getSourceType() {
        return this.mFromClass;
    }
}
