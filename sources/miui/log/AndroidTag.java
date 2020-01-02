package miui.log;

import miui.util.ReflectionUtils;

public final class AndroidTag implements ILogTag {
    static final ClassLoader BOOTCLASSLOADER = Thread.currentThread().getContextClassLoader();
    public final String className;
    private Class<?> clazz;
    private boolean clazzLoaded;
    public final boolean defaultOn;
    public final String fieldFullPath;
    public final String fieldName;
    private boolean isTagOn;
    private int onNumber;

    static String buildFieldFullPath(String className, String fieldName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(className);
        stringBuilder.append(".");
        stringBuilder.append(fieldName);
        return stringBuilder.toString();
    }

    static String buildFieldFullPath(Class<?> clazz, String fieldName) {
        return buildFieldFullPath(clazz.getName(), fieldName);
    }

    AndroidTag(Class<?> clazz, String fieldName, boolean defaultOn) {
        this(clazz, clazz.getName(), fieldName, defaultOn);
    }

    AndroidTag(String className, String fieldName, boolean defaultOn) {
        this(null, className, fieldName, defaultOn);
    }

    private AndroidTag(Class<?> clazz, String className, String fieldName, boolean defaultOn) {
        this.className = className;
        this.fieldName = fieldName;
        this.fieldFullPath = buildFieldFullPath(className, fieldName);
        this.defaultOn = defaultOn;
        this.isTagOn = defaultOn;
        boolean z = false;
        this.onNumber = 0;
        if (clazz != null) {
            z = true;
        }
        this.clazzLoaded = z;
        this.clazz = clazz;
    }

    public boolean isOn() {
        return this.isTagOn;
    }

    public synchronized void switchOn() {
        loadClass();
        this.onNumber++;
        if (this.onNumber == 0) {
            this.isTagOn = this.defaultOn;
            if (this.clazz != null) {
                ReflectionUtils.trySetStaticObjectField(this.clazz, this.fieldName, Boolean.valueOf(this.isTagOn));
            }
        } else if (this.onNumber == 1) {
            this.isTagOn = true;
            if (this.clazz != null) {
                ReflectionUtils.trySetStaticObjectField(this.clazz, this.fieldName, Boolean.valueOf(this.isTagOn));
            }
        }
    }

    public synchronized void switchOff() {
        loadClass();
        this.onNumber--;
        if (this.onNumber == 0) {
            this.isTagOn = this.defaultOn;
            if (this.clazz != null) {
                ReflectionUtils.trySetStaticObjectField(this.clazz, this.fieldName, Boolean.valueOf(this.isTagOn));
            }
        } else if (this.onNumber == -1) {
            this.isTagOn = false;
            if (this.clazz != null) {
                ReflectionUtils.trySetStaticObjectField(this.clazz, this.fieldName, Boolean.valueOf(this.isTagOn));
            }
        }
    }

    private void loadClass() {
        if (this.clazz == null && !this.clazzLoaded) {
            this.clazz = ReflectionUtils.tryFindClass(this.className, BOOTCLASSLOADER);
            this.clazzLoaded = true;
        }
    }
}
