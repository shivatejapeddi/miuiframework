package miui.util;

public class ExquisiteModeUtils {
    public static final float DEFAULT_EXQUISITE_SCALE_VALUE = ((((float) FeatureParser.getInteger("exquisite_mode_target_density", 1)) * 1.0f) / ((float) FeatureParser.getInteger("exquisite_mode_origin_density", 1)));
    public static final float DEFAULT_MIUI_SCALE_VALUE = 1.0f;
    public static final String MIUI_SCALE_FIELD_NAME = "miuiScale";
    public static final boolean SUPPORT_EXQUISITE_MODE;

    static {
        boolean z = true;
        if (DEFAULT_EXQUISITE_SCALE_VALUE == 1.0f) {
            z = false;
        }
        SUPPORT_EXQUISITE_MODE = z;
    }
}
