package miui.content.res;

import android.content.res.AssetManager;

public class AssetManagerUtil {
    @Deprecated
    public static String getCookieName(AssetManager am, int cookie) {
        return "";
    }

    public static int findCookieForPath(AssetManager am, String path) {
        return am.findCookieForPath(path);
    }
}
