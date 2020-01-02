package android.inputmethodservice;

import android.text.TextUtils;
import android.util.Log;
import miui.mqsas.sdk.MQSEventManagerDelegate;
import org.json.JSONException;
import org.json.JSONObject;

public class VoiceInputReporter {
    private static final String BAIDU_IME_PKGNAME = "com.baidu.input_mi";
    public static final int EVENT_NO_NETWORK = 3;
    public static final int EVENT_VOICE_BTN_PRESSED = 2;
    public static final int EVENT_VOICE_BTN_SHOW = 1;
    private static final String KEY_NO_NETWORK_BAIDU = "show_no_network_dialog_from_baidu";
    private static final String KEY_NO_NETWORK_SOGOU = "show_no_network_dialog_from_sogou";
    private static final String KEY_VOICE_BTN_PRESSED_BAIDU = "press_voice_input_button_from_baidu";
    private static final String KEY_VOICE_BTN_PRESSED_SOGOU = "press_voice_input_button_from_sogou";
    private static final String KEY_VOICE_BTN_SHOWN_BAIDU = "show_voice_input_button_from_baidu";
    private static final String KEY_VOICE_BTN_SHOWN_SOGOU = "show_voice_input_button_from_sogou";
    private static final String MQS_REPORT_MODULE = "InputMethod";
    private static final String SOGOU_IME_PKGNAME = "com.sohu.inputmethod.sogou.xiaomi";
    private static final String TAG = "VoiceInputReporter";

    static void reportEvent(int event, String pkgName) {
        String str = TAG;
        try {
            boolean isSogou = TextUtils.equals(pkgName, SOGOU_IME_PKGNAME);
            if (isSogou || TextUtils.equals(pkgName, BAIDU_IME_PKGNAME)) {
                JSONObject data = getBaseJson();
                String str2 = "1";
                if (event == 1) {
                    data.put(isSogou ? KEY_VOICE_BTN_SHOWN_SOGOU : KEY_VOICE_BTN_SHOWN_BAIDU, str2);
                } else if (event == 2) {
                    data.put(isSogou ? KEY_VOICE_BTN_PRESSED_SOGOU : KEY_VOICE_BTN_PRESSED_BAIDU, str2);
                } else if (event == 3) {
                    data.put(isSogou ? KEY_NO_NETWORK_SOGOU : KEY_NO_NETWORK_BAIDU, str2);
                } else {
                    Log.w(str, "event type is wrong");
                    return;
                }
                MQSEventManagerDelegate.getInstance().reportEvent(MQS_REPORT_MODULE, data.toString(), false);
                return;
            }
            Log.w(str, "only support baidu or sogou");
        } catch (JSONException e) {
            Log.w(str, "report event fail", e);
        }
    }

    private static JSONObject getBaseJson() {
        String str = "0";
        JSONObject JsonObj = new JSONObject();
        try {
            JsonObj.put(KEY_VOICE_BTN_SHOWN_SOGOU, str);
            JsonObj.put(KEY_VOICE_BTN_SHOWN_BAIDU, str);
            JsonObj.put(KEY_VOICE_BTN_PRESSED_SOGOU, str);
            JsonObj.put(KEY_VOICE_BTN_PRESSED_BAIDU, str);
            JsonObj.put(KEY_NO_NETWORK_SOGOU, str);
            JsonObj.put(KEY_NO_NETWORK_BAIDU, str);
        } catch (JSONException e) {
            Log.w(TAG, "build base json fail", e);
        }
        return JsonObj;
    }
}
