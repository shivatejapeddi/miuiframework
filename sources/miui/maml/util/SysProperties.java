package miui.maml.util;

import android.sysprop.DisplayProperties;

public class SysProperties {
    public static boolean isShowDebugLayout() {
        return ((Boolean) DisplayProperties.debug_layout().orElse(Boolean.valueOf(false))).booleanValue();
    }
}
