package miui.util;

@Deprecated
public class XmlUtils {
    public static int convertValueToUnsignedInt(String value, int defaultValue) {
        return com.android.internal.util.XmlUtils.convertValueToUnsignedInt(value, defaultValue);
    }
}
