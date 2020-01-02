package android.view.textclassifier;

import android.provider.DeviceConfig;
import android.util.ArrayMap;
import android.util.KeyValueListParser;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@VisibleForTesting(visibility = Visibility.PACKAGE)
public final class ConfigParser {
    static final boolean ENABLE_DEVICE_CONFIG = true;
    private static final String STRING_LIST_DELIMITER = ":";
    private static final String TAG = "ConfigParser";
    @GuardedBy({"mLock"})
    private final Map<String, Object> mCache = new ArrayMap();
    private final Supplier<String> mLegacySettingsSupplier;
    private final Object mLock = new Object();
    @GuardedBy({"mLock"})
    private KeyValueListParser mSettingsParser;

    public ConfigParser(Supplier<String> legacySettingsSupplier) {
        this.mLegacySettingsSupplier = (Supplier) Preconditions.checkNotNull(legacySettingsSupplier);
    }

    private KeyValueListParser getLegacySettings() {
        KeyValueListParser keyValueListParser;
        synchronized (this.mLock) {
            if (this.mSettingsParser == null) {
                String legacySettings = (String) this.mLegacySettingsSupplier.get();
                try {
                    this.mSettingsParser = new KeyValueListParser(',');
                    this.mSettingsParser.setString(legacySettings);
                } catch (IllegalArgumentException e) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Bad text_classifier_constants: ");
                    stringBuilder.append(legacySettings);
                    Log.w(str, stringBuilder.toString());
                }
            }
            keyValueListParser = this.mSettingsParser;
        }
        return keyValueListParser;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        synchronized (this.mLock) {
            Object cached = this.mCache.get(key);
            boolean booleanValue;
            if (cached instanceof Boolean) {
                booleanValue = ((Boolean) cached).booleanValue();
                return booleanValue;
            }
            booleanValue = DeviceConfig.getBoolean(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, getLegacySettings().getBoolean(key, defaultValue));
            this.mCache.put(key, Boolean.valueOf(booleanValue));
            return booleanValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        synchronized (this.mLock) {
            Object cached = this.mCache.get(key);
            int intValue;
            if (cached instanceof Integer) {
                intValue = ((Integer) cached).intValue();
                return intValue;
            }
            intValue = DeviceConfig.getInt(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, getLegacySettings().getInt(key, defaultValue));
            this.mCache.put(key, Integer.valueOf(intValue));
            return intValue;
        }
    }

    public float getFloat(String key, float defaultValue) {
        synchronized (this.mLock) {
            Object cached = this.mCache.get(key);
            float floatValue;
            if (cached instanceof Float) {
                floatValue = ((Float) cached).floatValue();
                return floatValue;
            }
            floatValue = DeviceConfig.getFloat(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, getLegacySettings().getFloat(key, defaultValue));
            this.mCache.put(key, Float.valueOf(floatValue));
            return floatValue;
        }
    }

    public String getString(String key, String defaultValue) {
        synchronized (this.mLock) {
            Object cached = this.mCache.get(key);
            String str;
            if (cached instanceof String) {
                str = (String) cached;
                return str;
            }
            str = DeviceConfig.getString(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, getLegacySettings().getString(key, defaultValue));
            this.mCache.put(key, str);
            return str;
        }
    }

    public List<String> getStringList(String key, List<String> defaultValue) {
        synchronized (this.mLock) {
            List cached = this.mCache.get(key);
            if (cached instanceof List) {
                List asList = cached;
                List emptyList;
                if (asList.isEmpty()) {
                    emptyList = Collections.emptyList();
                    return emptyList;
                } else if (asList.get(0) instanceof String) {
                    emptyList = cached;
                    return emptyList;
                }
            }
            List<String> value = getDeviceConfigStringList(key, getSettingsStringList(key, defaultValue));
            this.mCache.put(key, value);
            return value;
        }
    }

    public float[] getFloatArray(String key, float[] defaultValue) {
        synchronized (this.mLock) {
            Object cached = this.mCache.get(key);
            float[] fArr;
            if (cached instanceof float[]) {
                fArr = (float[]) cached;
                return fArr;
            }
            fArr = getDeviceConfigFloatArray(key, getSettingsFloatArray(key, defaultValue));
            this.mCache.put(key, fArr);
            return fArr;
        }
    }

    private List<String> getSettingsStringList(String key, List<String> defaultValue) {
        return parse(this.mSettingsParser.getString(key, null), (List) defaultValue);
    }

    private static List<String> getDeviceConfigStringList(String key, List<String> defaultValue) {
        return parse(DeviceConfig.getString(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, null), (List) defaultValue);
    }

    private static float[] getDeviceConfigFloatArray(String key, float[] defaultValue) {
        return parse(DeviceConfig.getString(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, null), defaultValue);
    }

    private float[] getSettingsFloatArray(String key, float[] defaultValue) {
        return parse(this.mSettingsParser.getString(key, null), defaultValue);
    }

    private static List<String> parse(String listStr, List<String> defaultValue) {
        if (listStr != null) {
            return Collections.unmodifiableList(Arrays.asList(listStr.split(":")));
        }
        return defaultValue;
    }

    private static float[] parse(String arrayStr, float[] defaultValue) {
        if (arrayStr == null) {
            return defaultValue;
        }
        String[] split = arrayStr.split(":");
        if (split.length != defaultValue.length) {
            return defaultValue;
        }
        float[] result = new float[split.length];
        int i = 0;
        while (i < split.length) {
            try {
                result[i] = Float.parseFloat(split[i]);
                i++;
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return result;
    }
}
