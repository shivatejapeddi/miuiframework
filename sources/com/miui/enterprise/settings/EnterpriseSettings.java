package com.miui.enterprise.settings;

import android.content.Context;
import android.os.Binder;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import miui.securityspace.CrossUserUtils;

public final class EnterpriseSettings {
    public static final boolean ENTERPRISE_ACTIVATED = SystemProperties.getBoolean(PROP_ENTERPRISE_ACTIVATED, false);
    public static final String ENTERPRISE_AGENCY = "enterprise_agency";
    public static final String ENTERPRISE_LICENCE = "enterprise_licence";
    public static final String ENTERPRISE_PACKAGE = "enterprise_package";
    public static final String PROP_ENTERPRISE_ACTIVATED = "persist.sys.ent_activated";
    public static final String SPLIT_EXPRESSION = ";";

    public static class APN {
        public static final String APN_SWITCH_MODE = "ep_apn_switch_mode";
    }

    public static class Application {
        public static final String APPLICATION_BLACK_LIST = "ep_app_black_list";
        public static final String APPLICATION_BLACK_XSAPCE = "ep_app_black_xsapce";
        public static final String APPLICATION_DISALLOW_RUNNING_LIST = "ep_app_disallow_running_list";
        public static final String APPLICATION_RESTRICTION_MODE = "ep_app_restriction_mode";
        public static final String APPLICATION_WHITE_LIST = "ep_app_white_list";
        public static final String TRUSTED_APP_STORES = "ep_trusted_app_stores";
        public static final String TRUSTED_APP_STORE_ENABLED = "ep_trusted_app_store_enabled";
    }

    public static class Device {
        public static final String HOME_DEFAULT = "ep_default_home";
        public static final String HOST_BLACK_LIST = "ep_url_black_list";
        public static final String HOST_RESRTICTION_MODE = "ep_host_restriction_mode";
        public static final String HOST_WHITE_LIST = "ep_url_white_list";
        public static final String IP_BLACK_LIST = "ep_ip_black_list";
        public static final String IP_RESTRICTION_MODE = "ep_ip_restriction_mode";
        public static final String IP_WHITE_LIST = "ep_ip_white_list";
        public static final String WIFI_AP_BSSID_BLACK_LIST = "ep_wifi_ap_bssid_black_list";
        public static final String WIFI_AP_BSSID_WHITE_LIST = "ep_wifi_ap_bssid_white_list";
        public static final String WIFI_AP_SSID_BLACK_LIST = "ep_wifi_ap_ssid_black_list";
        public static final String WIFI_AP_SSID_WHITE_LIST = "ep_wifi_ap_ssid_white_list";
        public static final String WIFI_CONN_RESRTICTION_MODE = "ep_wifi_conn_restriction_mode";
    }

    public static class Phone {
        public static final String AUTO_RECORD_PHONECALL = "ep_force_auto_call_record";
        public static final String AUTO_RECORD_PHONECALL_DIR = "ep_force_auto_call_record_dir";
        public static final String CALL_BLACK_LIST = "ep_call_black_list";
        public static final String CALL_RESTRICTION_MODE = "ep_call_restriction_mode";
        public static final String CALL_WHITE_LIST = "ep_call_white_list";
        public static final String CELLULAR_STATUS = "ep_cellular_status";
        public static final String DISABLE_CALL_FORWARD = "ep_disable_call_forward";
        public static final String DISABLE_CALL_LOG = "ep_disable_call_log";
        public static final String PHONECALL_STATUS = "ep_phone_call_status";
        public static final String SMS_BLACK_LIST = "ep_sms_black_list";
        public static final String SMS_RESTRICTION_MODE = "ep_sms_restriction_mode";
        public static final String SMS_STATUS = "ep_sms_status";
        public static final String SMS_WHITE_LIST = "ep_sms_white_list";
    }

    private EnterpriseSettings() {
    }

    public static int getInt(Context context, String key, int def) {
        return Secure.getIntForUser(context.getContentResolver(), key, def, getUserId());
    }

    public static int getInt(Context context, String key, int def, int userId) {
        return Secure.getIntForUser(context.getContentResolver(), key, def, userId);
    }

    public static void putInt(Context context, String key, int value, int userId) {
        Secure.putIntForUser(context.getContentResolver(), key, value, userId);
    }

    public static void putInt(Context context, String key, int value) {
        Secure.putIntForUser(context.getContentResolver(), key, value, getUserId());
    }

    public static String getString(Context context, String key) {
        return Secure.getStringForUser(context.getContentResolver(), key, getUserId());
    }

    public static String getString(Context context, String key, int userId) {
        return Secure.getStringForUser(context.getContentResolver(), key, userId);
    }

    public static void putString(Context context, String key, String value, int userId) {
        Secure.putStringForUser(context.getContentResolver(), key, value, userId);
    }

    public static void putString(Context context, String key, String value) {
        Secure.putStringForUser(context.getContentResolver(), key, value, getUserId());
    }

    public static int getUserId() {
        int appId = UserHandle.getAppId(Binder.getCallingUid());
        if (appId == 1000 || appId == 1001) {
            return CrossUserUtils.getCurrentUserId();
        }
        return UserHandle.myUserId();
    }

    public static String generateListSettings(List<String> value) {
        StringBuilder sb = new StringBuilder();
        if (value == null) {
            value = new ArrayList();
        }
        for (String single : value) {
            sb.append(single);
            sb.append(";");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static List<String> parseListSettings(String settings) {
        ArrayList<String> result = new ArrayList();
        if (!TextUtils.isEmpty(settings)) {
            result.addAll(Arrays.asList(settings.split(";")));
        }
        return result;
    }

    public static boolean isBootComplete() {
        return "1".equals(SystemProperties.get("sys.boot_completed", "0"));
    }
}
