package miui.log;

import android.text.TextUtils;
import java.util.HashMap;
import miui.util.ObjectReference;
import miui.util.ReflectionUtils;

public final class AndroidTags {
    static final String TAG_AM_DEBUG_BROADCAST = "com.android.server.am.ActivityManagerService.DEBUG_BROADCAST";
    private static final HashMap<String, AndroidTag> androidTagMap = new HashMap();

    public static synchronized AndroidTag add(Class<?> clazz, String fieldName, boolean defaultOn) {
        AndroidTag tag;
        synchronized (AndroidTags.class) {
            tag = (AndroidTag) androidTagMap.get(AndroidTag.buildFieldFullPath((Class) clazz, fieldName));
            if (tag == null) {
                tag = new AndroidTag((Class) clazz, fieldName, defaultOn);
                androidTagMap.put(tag.fieldFullPath, tag);
            }
        }
        return tag;
    }

    public static synchronized AndroidTag get(String fieldFullPath) {
        synchronized (AndroidTags.class) {
            AndroidTag androidTag;
            if (androidTagMap.containsKey(fieldFullPath)) {
                androidTag = (AndroidTag) androidTagMap.get(fieldFullPath);
                return androidTag;
            }
            androidTag = create(fieldFullPath);
            androidTagMap.put(fieldFullPath, androidTag);
            return androidTag;
        }
    }

    public static synchronized boolean isOn(String fieldFullPath) {
        synchronized (AndroidTags.class) {
            AndroidTag tag = get(fieldFullPath);
            if (tag != null) {
                boolean isOn = tag.isOn();
                return isOn;
            }
            return false;
        }
    }

    public static synchronized void switchOn(String fieldFullPath) {
        synchronized (AndroidTags.class) {
            AndroidTag tag = get(fieldFullPath);
            if (tag != null) {
                tag.switchOn();
            }
        }
    }

    public static synchronized void switchOff(String fieldFullPath) {
        synchronized (AndroidTags.class) {
            AndroidTag tag = get(fieldFullPath);
            if (tag != null) {
                tag.switchOff();
            }
        }
    }

    private static AndroidTag create(String fieldFullPath) {
        int sepIndex = fieldFullPath.lastIndexOf(46);
        if (sepIndex < 0) {
            return null;
        }
        boolean defaultOn = false;
        String className = fieldFullPath.substring(0, sepIndex).trim();
        String fieldName = fieldFullPath.substring(sepIndex + 1, fieldFullPath.length()).trim();
        if (TextUtils.isEmpty(className) || TextUtils.isEmpty(fieldName)) {
            return null;
        }
        Class clazz = ReflectionUtils.tryFindClass(className, AndroidTag.BOOTCLASSLOADER);
        if (clazz == null) {
            return null;
        }
        ObjectReference<Boolean> defaultValue = ReflectionUtils.tryGetStaticObjectField(clazz, fieldName, Boolean.class);
        if (!(defaultValue == null || defaultValue.get() == null)) {
            defaultOn = ((Boolean) defaultValue.get()).booleanValue();
        }
        return new AndroidTag(clazz, fieldName, defaultOn);
    }
}
