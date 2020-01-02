package miui.log;

import android.util.SparseArray;
import java.util.HashMap;

public final class MiuiTags {
    public static final int TAG_ID_SEND_BROADCAST = 0;
    public static final String TAG_SEND_BROADCAST = "SendBroadcast";
    public static final boolean TAG_SEND_BROADCAST_DEFAULT = false;
    public static final MiuiTag TagSendBroadcast = new MiuiTag(0, TAG_SEND_BROADCAST, false);
    private static final MiuiTag[] allMiuiTags = new MiuiTag[]{TagSendBroadcast};
    private static final HashMap<String, MiuiTag> miuiTagMap = new HashMap();
    private static final SparseArray<MiuiTag> miuiTagSparseArray = new SparseArray();

    static {
        int i = 0;
        while (true) {
            MiuiTag tag = allMiuiTags;
            if (i < tag.length) {
                tag = tag[i];
                miuiTagMap.put(tag.name, tag);
                miuiTagSparseArray.append(tag.id, tag);
                i++;
            } else {
                return;
            }
        }
    }

    public static MiuiTag get(int tagID) {
        return (MiuiTag) miuiTagSparseArray.get(tagID);
    }

    public static MiuiTag get(String tagName) {
        return (MiuiTag) miuiTagMap.get(tagName);
    }

    public static boolean isOn(int tagID) {
        MiuiTag tag = (MiuiTag) miuiTagSparseArray.get(tagID);
        return tag == null ? false : tag.isOn();
    }

    public static boolean isOn(String tagName) {
        MiuiTag tag = (MiuiTag) miuiTagMap.get(tagName);
        return tag == null ? false : tag.isOn();
    }

    public static void switchOn(int tagID) {
        MiuiTag tag = (MiuiTag) miuiTagSparseArray.get(tagID);
        if (tag != null) {
            tag.switchOn();
        }
    }

    public static void switchOn(String tagName) {
        MiuiTag tag = (MiuiTag) miuiTagMap.get(tagName);
        if (tag != null) {
            tag.switchOn();
        }
    }

    public static void switchOff(int tagID) {
        MiuiTag tag = (MiuiTag) miuiTagSparseArray.get(tagID);
        if (tag != null) {
            tag.switchOff();
        }
    }

    public static void switchOff(String tagName) {
        MiuiTag tag = (MiuiTag) miuiTagMap.get(tagName);
        if (tag != null) {
            tag.switchOff();
        }
    }
}
