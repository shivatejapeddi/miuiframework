package miui.util;

public final class ObjectReference<T> {
    private final T mObject;

    public ObjectReference(T object) {
        this.mObject = object;
    }

    public final T get() {
        return this.mObject;
    }
}
