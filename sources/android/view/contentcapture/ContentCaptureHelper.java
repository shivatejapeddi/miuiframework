package android.view.contentcapture;

import android.os.Build;
import android.provider.DeviceConfig;
import android.util.ArraySet;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class ContentCaptureHelper {
    private static final String TAG = ContentCaptureHelper.class.getSimpleName();
    public static boolean sDebug = true;
    public static boolean sVerbose = false;

    public static String getSanitizedString(CharSequence text) {
        if (text == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(text.length());
        stringBuilder.append("_chars");
        return stringBuilder.toString();
    }

    public static int getDefaultLoggingLevel() {
        return Build.IS_DEBUGGABLE;
    }

    public static void setLoggingLevel() {
        setLoggingLevel(DeviceConfig.getInt("content_capture", ContentCaptureManager.DEVICE_CONFIG_PROPERTY_LOGGING_LEVEL, getDefaultLoggingLevel()));
    }

    public static void setLoggingLevel(int level) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Setting logging level to ");
        stringBuilder.append(getLoggingLevelAsString(level));
        Log.i(str, stringBuilder.toString());
        sDebug = false;
        sVerbose = false;
        if (level != 0) {
            if (level != 1) {
                if (level != 2) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("setLoggingLevel(): invalud level: ");
                    stringBuilder.append(level);
                    Log.w(str, stringBuilder.toString());
                    return;
                }
                sVerbose = true;
            }
            sDebug = true;
        }
    }

    public static String getLoggingLevelAsString(int level) {
        if (level == 0) {
            return "OFF";
        }
        if (level == 1) {
            return "DEBUG";
        }
        if (level == 2) {
            return "VERBOSE";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UNKNOWN-");
        stringBuilder.append(level);
        return stringBuilder.toString();
    }

    public static <T> ArrayList<T> toList(Set<T> set) {
        return set == null ? null : new ArrayList(set);
    }

    public static <T> ArraySet<T> toSet(List<T> list) {
        return list == null ? null : new ArraySet((Collection) list);
    }

    private ContentCaptureHelper() {
        throw new UnsupportedOperationException("contains only static methods");
    }
}
