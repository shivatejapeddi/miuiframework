package android.inputmethodservice;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

public class InputMethodAnalyticsUtil {
    private static final String KEY_FUNCTION_CLICKED = "click";
    private static final String KEY_INPUT_METHOD_ANALYTICS = "input_method_analytics";
    private static final String KEY_INPUT_METHOD_PROVIDER_URI = "content://com.miui.input.provider";
    private static final String TAG = "InputMethodAnalytics";
    private static final String TRACK_KEY_HIGH_KEYBOARD_CLICK = "high_keyboard_click";

    public static void addInputMethodAnalytics(final Context context, String selectValue) {
        final HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        Handler subHandler = new Handler(handlerThread.getLooper());
        final Bundle bundle = new Bundle();
        bundle.putString("clickKey", KEY_FUNCTION_CLICKED);
        bundle.putString("clickValue", selectValue);
        bundle.putString("recordKey", TRACK_KEY_HIGH_KEYBOARD_CLICK);
        subHandler.post(new Runnable() {
            public void run() {
                try {
                    context.getContentResolver().call(Uri.parse(InputMethodAnalyticsUtil.KEY_INPUT_METHOD_PROVIDER_URI), InputMethodAnalyticsUtil.KEY_INPUT_METHOD_ANALYTICS, null, bundle);
                    handlerThread.quitSafely();
                } catch (Exception e) {
                    Log.e(InputMethodAnalyticsUtil.TAG, "call input method provider error", e);
                }
            }
        });
    }
}
