package miui.log;

import java.util.HashMap;

public final class TagGroups {
    public static final String TAGGROUP_BROADCAST = "Broadcast";
    public static final TagGroup TagGroupBroadcast = new TagGroup(TAGGROUP_BROADCAST, new String[]{MiuiTags.TAG_SEND_BROADCAST});
    private static final TagGroup[] allTagGroups;
    private static final HashMap<String, TagGroup> tagGroupMap = new HashMap();

    static {
        TagGroup[] tagGroupArr = new TagGroup[1];
        int i = 0;
        tagGroupArr[0] = TagGroupBroadcast;
        allTagGroups = tagGroupArr;
        tagGroupArr = allTagGroups;
        int length = tagGroupArr.length;
        while (i < length) {
            TagGroup tagGroup = tagGroupArr[i];
            tagGroupMap.put(tagGroup.name, tagGroup);
            i++;
        }
    }

    public static TagGroup get(String groupName) {
        return (TagGroup) tagGroupMap.get(groupName);
    }

    public static boolean isOn(String groupName) {
        TagGroup group = (TagGroup) tagGroupMap.get(groupName);
        return group == null ? false : group.isOn();
    }

    public static void switchOn(String groupName) {
        TagGroup group = (TagGroup) tagGroupMap.get(groupName);
        if (group != null) {
            group.switchOn();
        }
    }

    public static void switchOff(String groupName) {
        TagGroup group = (TagGroup) tagGroupMap.get(groupName);
        if (group != null) {
            group.switchOff();
        }
    }
}
