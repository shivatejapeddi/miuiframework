package android.view.inspector;

public class StaticInspectionCompanionProvider implements InspectionCompanionProvider {
    private static final String COMPANION_SUFFIX = "$InspectionCompanion";

    public <T> InspectionCompanion<T> provide(Class<T> cls) {
        String companionName = new StringBuilder();
        companionName.append(cls.getName());
        companionName.append(COMPANION_SUFFIX);
        try {
            Class<InspectionCompanion<T>> companionClass = cls.getClassLoader().loadClass(companionName.toString());
            if (InspectionCompanion.class.isAssignableFrom(companionClass)) {
                return (InspectionCompanion) companionClass.newInstance();
            }
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        } catch (InstantiationException e3) {
            Throwable cause = e3.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            } else if (cause instanceof Error) {
                throw ((Error) cause);
            } else {
                throw new RuntimeException(cause);
            }
        }
    }
}
