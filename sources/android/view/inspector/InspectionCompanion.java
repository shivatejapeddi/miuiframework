package android.view.inspector;

public interface InspectionCompanion<T> {

    public static class UninitializedPropertyMapException extends RuntimeException {
        public UninitializedPropertyMapException() {
            super("Unable to read properties of an inspectable before mapping their IDs.");
        }
    }

    void mapProperties(PropertyMapper propertyMapper);

    void readProperties(T t, PropertyReader propertyReader);
}
