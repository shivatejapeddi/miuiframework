package miui.telephony.livetalk;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Settings.System;
import android.util.Pair;
import miui.telephony.TelephonyManager;
import org.json.JSONArray;
import org.json.JSONException;

public class LivetalkUtils {
    public static final String DAIL_MODE = "dial_mode";
    public static final int DIAL_MODE_GENERAL = 0;
    public static final int DIAL_MODE_LIVETALK = 1;
    public static final String ENABLE_LIVETALK_SUMMARY_CN = "enable_livetalk_summary_cn";
    public static final String ENABLE_LIVETALK_SUMMARY_EN = "enable_livetalk_summary_en";
    public static final String ENABLE_LIVETALK_TITLE_CN = "enable_livetalk_title_cn";
    public static final String ENABLE_LIVETALK_TITLE_EN = "enable_livetalk_title_en";
    public static final String FROM_VIEW = "fromView";
    public static final String INTENT_ACCEPT_BACK_CALL = "com.miui.livetalk_ACCEPT_BACK_CALL";
    private static final String INTENT_MY_LIVETALK = "com.miui.livetalk.MY_LIVETALK_VIEW";
    private static final String INTENT_PURCHASE_ACTION = "com.miui.livetalk.PURCHASE_VIEW";
    public static final String INTENT_RECORD_CALL_BACK_INFO = "com.miui.livetalk_RECORD_CALLBACK_INFO";
    private static final String INTENT_WELCOME_ACTION = "com.miui.livetalk.WELCOME_VIEW";
    public static final String IS_LIVETALK_DIAL = "isLivetalk";
    public static final int IS_NEED_PROMPT = 1;
    public static final int LIVETALK_AVAILABLE = 1;
    public static final String LIVETALK_AVAILABLE_STATUS = "livetalk_available_status";
    public static final String LIVETALK_DIAL_RANGE = "livetalk_dial_range";
    public static final int LIVETALK_DIAL_RANGE_DEMOSTIC = 1;
    public static final int LIVETALK_DIAL_RANGE_INTERNATIONAL = 2;
    public static final int LIVETALK_DIAL_RANGE_WHOLE = 0;
    public static final String LIVETALK_ENABLED = "livetalk_enabled";
    public static final String LIVETALK_INTERNAL_DIAL_AVAIABLE = "internal_dial_avaiable";
    public static final String LIVETALK_INTERNAL_DIAL_ENABLE = "internal_dial_enable";
    public static final String LIVETALK_INTERNATIONAL_DIAL_AVAIABLE = "international_dial_avaiable";
    public static final String LIVETALK_INTERNATIONAL_DIAL_ENABLE = "international_dial_enable";
    public static final int LIVETALK_NOT_AVAILABLE = 0;
    public static int LIVETALK_NUMBER_POOL_VERSION = 0;
    public static final String LIVETALK_RECENT_COUNTRY_REMAIN_MINS = "recent_country_remain_mins";
    public static final String LIVETALK_REMAIN_MINUTES = "livetalk_remain_minutes";
    public static final String LIVETALK_SERVICE_NAME = "com.miui.livetalk.service.LivetalkService";
    public static final String LIVETALK_SERVICE_STATUS = "livetalk_service_status";
    public static final String LIVETALK_SWITCH_STATE = "livetalk_switch_state";
    public static final String LIVETALK_USE_CURRENT_MI_ACCOUNT = "livetalk_use_current_account";
    public static final int LIVETALK_WITH_170 = 2;
    private static final String META_DATA_SUPPORT_LIVETALK = "support_livetalk";
    public static final int MY_LIVETALK_FROM_CONTACTS = 202;
    public static final int MY_LIVETALK_FROM_NOTIFICATION = 200;
    public static final int MY_LIVETALK_FROM_SETTING = 201;
    public static final String NEED_PROMPT = "need_prompt";
    public static final int NOT_NEED_PROMPT = 0;
    public static final String ONLY_REGULAR_CALL = "only_regular_call";
    public static final String PARAM_NUMBER = "number";
    public static final int PURCHASE_FROM_DIALPAGE = 2;
    public static final int PURCHASE_FROM_INTERNATIONAL = 8;
    public static final int PURCHASE_FROM_NOTIFICATION = 5;
    public static final int PURCHASE_FROM_SAFE_CENTER_CLEANER = 7;
    public static final int PURCHASE_FROM_SAFE_CENTER_OPTIMIZE = 6;
    public static final int PURCHASE_FROM_SETTING = 4;
    public static final int PURCHASE_FROM_SMS = 1;
    public static final int PURCHASE_FROM_YELLOWPAGE = 3;
    public static final String SAFE_CENTER_CLEANER_SUMMARY = "safe_center_cleaner_summary";
    public static final String SAFE_CENTER_CLEANER_TITLE = "safe_center_cleaner_title";
    public static final String SAFE_CENTER_OPTIMIZE_SUMMARY_CN = "safe_center_optimize_summary_cn";
    public static final String SAFE_CENTER_OPTIMIZE_SUMMARY_EN = "safe_center_optimize_summary_en";
    public static final String SAFE_CENTER_OPTIMIZE_TITLE_CN = "safe_center_optimize_title_cn";
    public static final String SAFE_CENTER_OPTIMIZE_TITLE_EN = "safe_center_optimize_title_en";
    public static final String SIM_CARD_ACTIVATED_STATE = "sim_card_activated_status";
    public static final String SIM_CARD_NUMBER = "sim_card_number";
    private static final String TAG = "LivetalkUtils";
    public static final String USER_CONFIG_COMPLETED = "user_config_completed";
    public static final int WELCOME_FROM_PURCHASE = 102;
    public static final int WELCOME_FROM_SETTING = 101;
    private static String[] sCallBackNumbers;

    public static boolean isLiveTalkCallbackNumber(String number) {
        return false;
    }

    public static boolean updateLivetalkCallBackNumber(Cursor c) {
        if (c == null || c.getCount() == 0) {
            return false;
        }
        try {
            sCallBackNumbers = new String[c.getCount()];
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                sCallBackNumbers[i] = c.getString(c.getColumnIndex("number"));
                c.moveToNext();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            c.close();
        }
    }

    public static void updateLivetalkCallBackNumber(JSONArray numberPool) {
        if (numberPool != null) {
            sCallBackNumbers = new String[numberPool.length()];
            int i = 0;
            while (i < numberPool.length()) {
                try {
                    sCallBackNumbers[i] = numberPool.getString(i);
                    i++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void updateLivetalkCallBackNumber(JSONArray numberPool, int version) throws JSONException {
        if (numberPool != null) {
            int len = numberPool.length();
            String[] arr = new String[len];
            for (int i = 0; i < len; i++) {
                arr[i] = numberPool.getString(i);
            }
            sCallBackNumbers = arr;
            LIVETALK_NUMBER_POOL_VERSION = version;
        }
    }

    public static int getLivetalkStatus(Context context) {
        return 0;
    }

    private static boolean supportLivetalk(Context context) {
        return false;
    }

    public static boolean isLivetalkEnabled(Context context) {
        return System.getInt(context.getContentResolver(), LIVETALK_ENABLED, 0) == 1;
    }

    public static int[] getSimActivatedState(Context context) {
        ContentResolver resolver = context.getContentResolver();
        int slotCount = TelephonyManager.getDefault().getPhoneCount();
        int[] simActivateState = new int[slotCount];
        for (int i = 0; i < slotCount; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(SIM_CARD_ACTIVATED_STATE);
            stringBuilder.append(i);
            simActivateState[i] = System.getInt(resolver, stringBuilder.toString(), 0);
        }
        return simActivateState;
    }

    public static String[] getSimNumber(Context context) {
        ContentResolver resolver = context.getContentResolver();
        int slotCount = TelephonyManager.getDefault().getPhoneCount();
        String[] simNumber = new String[slotCount];
        for (int i = 0; i < slotCount; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(SIM_CARD_NUMBER);
            stringBuilder.append(i);
            simNumber[i] = System.getString(resolver, stringBuilder.toString());
        }
        return simNumber;
    }

    public static int getRemainMins(Context context) {
        return System.getInt(context.getContentResolver(), LIVETALK_REMAIN_MINUTES, 0);
    }

    public static Intent getLivetalkIntentWithParam(int fromView) {
        Intent intent = new Intent();
        intent.setAction(INTENT_MY_LIVETALK);
        intent.putExtra(FROM_VIEW, fromView);
        return intent;
    }

    public static Intent getPurchaseIntentWithParam(int fromView) {
        Intent intent = new Intent();
        intent.setAction(INTENT_PURCHASE_ACTION);
        intent.putExtra(FROM_VIEW, fromView);
        return intent;
    }

    public static Intent getWelComeIntentWithParam(int fromView) {
        Intent intent = new Intent();
        intent.setAction(INTENT_WELCOME_ACTION);
        intent.putExtra(FROM_VIEW, fromView);
        return intent;
    }

    public static boolean getInternalDialAvaiable(Context context) {
        return false;
    }

    public static boolean getInternationalDialAvaiable(Context context) {
        return false;
    }

    public static void setInternalDialEnable(Context context, boolean isEnable) {
        System.putInt(context.getContentResolver(), LIVETALK_INTERNAL_DIAL_ENABLE, isEnable);
    }

    public static void setInternationalDialEnable(Context context, boolean isEnable) {
        System.putInt(context.getContentResolver(), LIVETALK_INTERNATIONAL_DIAL_ENABLE, isEnable);
    }

    public static boolean isLivetalkSwitchOn(Context context) {
        return System.getInt(context.getContentResolver(), LIVETALK_SWITCH_STATE, 1) == 1;
    }

    public static int getLivetalkDialRange(Context context) {
        return System.getInt(context.getContentResolver(), LIVETALK_DIAL_RANGE, 0);
    }

    public static boolean isLivetalkUseCurrentAccount(Context context) {
        return System.getInt(context.getContentResolver(), LIVETALK_USE_CURRENT_MI_ACCOUNT, 0) == 1;
    }

    public static boolean isInternalDialEnable(Context context) {
        return false;
    }

    public static boolean isInternationalDialEnable(Context context) {
        return false;
    }

    public static int getLivetalkServiceStatus(Context context) {
        return 0;
    }

    public static Pair<String, String> getLivetalkInfo(Context context) {
        return null;
    }

    public static Pair<String, String> getLivetalkinfoForKK(ContentResolver resolver, Context context) {
        return null;
    }

    public static int getInternationalRemainMins(Context context) {
        return System.getInt(context.getContentResolver(), LIVETALK_RECENT_COUNTRY_REMAIN_MINS, 0);
    }

    public static boolean isPrompt(Context context) {
        return false;
    }

    public static void removePrompt(Context context) {
        System.putInt(context.getContentResolver(), NEED_PROMPT, 0);
    }

    public static void addPrompt(Context context) {
        System.putInt(context.getContentResolver(), NEED_PROMPT, 1);
    }

    public static boolean isShowInSafeCenter(Context context) {
        return false;
    }

    public static Pair<String, String> getLivetalkOptimizeInfo(Context context) {
        return null;
    }

    public static Pair<String, String> getLivetalkCleanerInfo(Context context) {
        return null;
    }
}
