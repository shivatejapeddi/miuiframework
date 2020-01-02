package android.util;

import android.os.AsyncTask;
import android.os.RemoteException;
import android.view.WindowManagerGlobal;
import miui.util.FeatureParser;

public class MiuiFontSizeUtils {
    private static final SparseIntArray DEFAULT_DENSITY_320 = new SparseIntArray();
    private static final SparseIntArray DEFAULT_DENSITY_440 = new SparseIntArray();
    private static final SparseIntArray DEFAULT_DENSITY_480 = new SparseIntArray();
    private static final SparseArray<Float> DEFAULT_FONT_SCALE_320 = new SparseArray();
    private static final SparseArray<Float> DEFAULT_FONT_SCALE_440 = new SparseArray();
    private static final SparseArray<Float> DEFAULT_FONT_SCALE_480 = new SparseArray();
    private static final int DPI_320 = 320;
    private static final int DPI_480 = 480;
    private static String LOG_TAG = "MiuiFontSizeUtils";

    static {
        DEFAULT_DENSITY_440.put(12, 440);
        DEFAULT_DENSITY_440.put(13, 440);
        DEFAULT_DENSITY_440.put(14, 440);
        DEFAULT_DENSITY_440.put(15, 540);
        DEFAULT_DENSITY_440.put(11, 540);
        DEFAULT_DENSITY_480.put(12, 480);
        DEFAULT_DENSITY_480.put(13, 480);
        DEFAULT_DENSITY_480.put(14, 480);
        DEFAULT_DENSITY_480.put(15, 540);
        DEFAULT_DENSITY_480.put(11, 540);
        DEFAULT_DENSITY_320.put(12, 320);
        DEFAULT_DENSITY_320.put(13, 320);
        DEFAULT_DENSITY_320.put(14, 320);
        DEFAULT_DENSITY_320.put(15, 360);
        DEFAULT_DENSITY_320.put(11, 360);
        SparseArray sparseArray = DEFAULT_FONT_SCALE_440;
        Float valueOf = Float.valueOf(0.86f);
        sparseArray.put(12, valueOf);
        sparseArray = DEFAULT_FONT_SCALE_440;
        Float valueOf2 = Float.valueOf(1.15f);
        sparseArray.put(13, valueOf2);
        sparseArray = DEFAULT_FONT_SCALE_440;
        Float valueOf3 = Float.valueOf(1.32f);
        sparseArray.put(14, valueOf3);
        DEFAULT_FONT_SCALE_440.put(15, Float.valueOf(1.1f));
        DEFAULT_FONT_SCALE_440.put(11, Float.valueOf(1.14f));
        DEFAULT_FONT_SCALE_480.put(12, valueOf);
        DEFAULT_FONT_SCALE_480.put(13, valueOf2);
        DEFAULT_FONT_SCALE_480.put(14, valueOf3);
        DEFAULT_FONT_SCALE_480.put(15, Float.valueOf(1.17f));
        sparseArray = DEFAULT_FONT_SCALE_480;
        Float valueOf4 = Float.valueOf(1.2f);
        sparseArray.put(11, valueOf4);
        DEFAULT_FONT_SCALE_320.put(12, valueOf);
        DEFAULT_FONT_SCALE_320.put(13, valueOf2);
        DEFAULT_FONT_SCALE_320.put(14, valueOf3);
        DEFAULT_FONT_SCALE_320.put(15, valueOf4);
        DEFAULT_FONT_SCALE_320.put(11, Float.valueOf(1.26f));
    }

    public static void setDensityDpi(int newUiModeType, int userHandle) {
        int defaultDensityDpi = MiuiDisplayMetrics.DENSITY_DEVICE;
        int densityDpi = getDensity(newUiModeType, defaultDensityDpi);
        if (densityDpi == defaultDensityDpi) {
            clearForcedDisplayDensity(0, userHandle);
        } else {
            setForcedDisplayDensity(0, densityDpi, userHandle);
        }
    }

    public static int getDensity(int fontUiMode, int defaultDensity) {
        SparseIntArray defaultValuesMap;
        if (defaultDensity == 320) {
            defaultValuesMap = DEFAULT_DENSITY_320;
        } else if (defaultDensity != 480) {
            defaultValuesMap = DEFAULT_DENSITY_440;
        } else {
            defaultValuesMap = DEFAULT_DENSITY_480;
        }
        int defaultUiModeDensity = defaultValuesMap.get(fontUiMode, defaultDensity);
        switch (fontUiMode) {
            case 11:
                return FeatureParser.getInteger("godzillaui_density", defaultUiModeDensity);
            case 12:
                return FeatureParser.getInteger("smallui_density", defaultUiModeDensity);
            case 13:
                return FeatureParser.getInteger("mediumui_density", defaultUiModeDensity);
            case 14:
                return FeatureParser.getInteger("largeui_density", defaultUiModeDensity);
            case 15:
                return FeatureParser.getInteger("hugeui_density", defaultUiModeDensity);
            default:
                return defaultDensity;
        }
    }

    public static float getFontScaleV2(int fontUiMode, int defaultDensity) {
        SparseArray<Float> defaultValuesMap;
        if (defaultDensity == 320) {
            defaultValuesMap = DEFAULT_FONT_SCALE_320;
        } else if (defaultDensity != 480) {
            defaultValuesMap = DEFAULT_FONT_SCALE_440;
        } else {
            defaultValuesMap = DEFAULT_FONT_SCALE_480;
        }
        float defaultUiModeScale = ((Float) defaultValuesMap.get(fontUiMode, Float.valueOf(1.0f))).floatValue();
        switch (fontUiMode) {
            case 11:
                return FeatureParser.getFloat("godzillaui_font_scale_v2", defaultUiModeScale).floatValue();
            case 12:
                return FeatureParser.getFloat("smallui_font_scale", defaultUiModeScale).floatValue();
            case 13:
                return FeatureParser.getFloat("mediumui_font_scale", defaultUiModeScale).floatValue();
            case 14:
                return FeatureParser.getFloat("largeui_font_scale", defaultUiModeScale).floatValue();
            case 15:
                return FeatureParser.getFloat("hugeui_font_scale_v2", defaultUiModeScale).floatValue();
            default:
                return 1.0f;
        }
    }

    private static void clearForcedDisplayDensity(final int displayId, final int userHandle) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                try {
                    WindowManagerGlobal.getWindowManagerService().clearForcedDisplayDensityForUser(displayId, userHandle);
                } catch (RemoteException e) {
                    Log.w(MiuiFontSizeUtils.LOG_TAG, "Unable to clear forced display density setting");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    private static void setForcedDisplayDensity(final int displayId, final int density, final int userHandle) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                try {
                    WindowManagerGlobal.getWindowManagerService().setForcedDisplayDensityForUser(displayId, density, userHandle);
                } catch (RemoteException e) {
                    Log.w(MiuiFontSizeUtils.LOG_TAG, "Unable to save forced display density setting");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }
}
