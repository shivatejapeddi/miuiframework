package miui.util;

import android.content.Context;
import android.provider.MiuiSettings.System;
import android.provider.Settings;
import android.text.TextUtils;
import miui.securityspace.CrossUserUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class AutoDisableScreenButtonsHelper {
    public static final String CLOUD_SETTING = "auto_disable_screen_button_cloud_setting";
    public static final int ENABLE_ASK = 1;
    public static final int ENABLE_AUTO = 2;
    public static final String MODULE_AUTO_DIS_NAV_BTN = "AutoDisableNavigationButton1";
    public static final int NO = 3;
    public static final int NONE = 0;
    private static final String TAG = "AutoDisableHelper";
    private static JSONObject mCloudJson;
    private static JSONObject mUserJson;

    public static int getAppFlag(Context context, String pkg) {
        Object flagObj = getValue(context, pkg);
        return flagObj == null ? 3 : ((Integer) flagObj).intValue();
    }

    public static Object getValue(Context context, String key) {
        checkJson(context);
        try {
            if (mUserJson != null && mUserJson.has(key)) {
                return mUserJson.get(key);
            }
            if (mCloudJson != null && mCloudJson.has(key)) {
                return mCloudJson.get(key);
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setFlag(Context context, String pkg, int flag) {
        setValue(context, pkg, Integer.valueOf(flag));
    }

    public static void setValue(Context context, String key, Object value) {
        checkJson(context);
        JSONObject jSONObject = mUserJson;
        if (jSONObject != null && context != null) {
            try {
                jSONObject.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.putStringForUser(context.getContentResolver(), System.KEY_AUTO_DISABLE_SCREEN_BUTTON, mUserJson.toString(), CrossUserUtils.getCurrentUserId());
        }
    }

    public static void updateUserJson(String config) {
        if (!TextUtils.isEmpty(config)) {
            JSONObject jSONObject = mUserJson;
            if (jSONObject == null || !config.equals(jSONObject.toString())) {
                try {
                    mUserJson = new JSONObject(config);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void updateCloudJson(String config) {
        if (!TextUtils.isEmpty(config)) {
            JSONObject jSONObject = mCloudJson;
            if (jSONObject == null || !config.equals(jSONObject.toString())) {
                try {
                    mCloudJson = new JSONObject(config);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void checkJson(Context context) {
        if (context != null) {
            if (mUserJson == null) {
                String userSetting = System.getStringForUser(context.getContentResolver(), System.KEY_AUTO_DISABLE_SCREEN_BUTTON, CrossUserUtils.getCurrentUserId());
                if (userSetting == null) {
                    mUserJson = new JSONObject();
                } else {
                    updateUserJson(userSetting);
                }
            }
            if (mCloudJson == null) {
                updateCloudJson(Settings.System.getString(context.getContentResolver(), CLOUD_SETTING));
            }
        }
    }
}
