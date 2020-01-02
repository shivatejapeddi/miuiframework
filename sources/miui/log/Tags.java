package miui.log;

public final class Tags {
    public static AndroidTag getAndroidTag(String fieldFullPath) {
        return AndroidTags.get(fieldFullPath);
    }

    public static boolean isAndroidTagOn(String fieldFullPath) {
        return AndroidTags.isOn(fieldFullPath);
    }

    public static void switchOnAndroidTag(String fieldFullPath) {
        AndroidTags.switchOn(fieldFullPath);
    }

    public static void switchOffAndroidTag(String fieldFullPath) {
        AndroidTags.switchOff(fieldFullPath);
    }

    public static MiuiTag getMiuiTag(int tagID) {
        return MiuiTags.get(tagID);
    }

    public static MiuiTag getMiuiTag(String tagName) {
        return MiuiTags.get(tagName);
    }

    public static boolean isMiuiTagOn(int tagID) {
        return MiuiTags.isOn(tagID);
    }

    public static boolean isMiuiTagOn(String tagName) {
        return MiuiTags.isOn(tagName);
    }

    public static void switchOnMiuiTag(int tagID) {
        MiuiTags.switchOn(tagID);
    }

    public static void switchOnMiuiTag(String tagName) {
        MiuiTags.switchOn(tagName);
    }

    public static void switchOffMiuiTag(int tagID) {
        MiuiTags.switchOff(tagID);
    }

    public static void switchOffMiuiTag(String tagName) {
        MiuiTags.switchOff(tagName);
    }

    public static TagGroup getTagGroup(String groupName) {
        return TagGroups.get(groupName);
    }

    public static boolean isTagGroupOn(String groupName) {
        return TagGroups.isOn(groupName);
    }

    public static synchronized void switchOnTagGroup(String groupName) {
        synchronized (Tags.class) {
            TagGroups.switchOn(groupName);
        }
    }

    public static synchronized void switchOffTagGroup(String groupName) {
        synchronized (Tags.class) {
            TagGroups.switchOff(groupName);
        }
    }
}
