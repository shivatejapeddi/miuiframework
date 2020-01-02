package android.graphics.improve;

import android.content.Context;
import android.os.AsyncTask;
import miui.mqsas.sdk.MQSEventManagerDelegate;
import org.json.JSONException;
import org.json.JSONObject;

public class SRReporter {
    public static final String ACTION_CLOSE = "3";
    public static final String ACTION_FAIL = "4";
    public static final String ACTION_IMPROVE = "1";
    public static final String ACTION_IMPROVE_TIME = "5";
    public static final String ACTION_OPEN = "2";
    public static final String STR_ACTION = "action";
    public static final String STR_DEAL_TIME = "deal_time";
    public static final String STR_FAIL_PKG = "fail_pkg";
    public static final String STR_FAIL_REASON = "fail_reason";
    public static final String STR_FAIL_VERSION = "fail_version";

    private static void report(final JSONObject jsonObject) {
        new AsyncTask<Void, Void, Void>() {
            /* Access modifiers changed, original: protected|varargs */
            public Void doInBackground(Void... params) {
                MQSEventManagerDelegate.getInstance().reportEvent("superResolution", jsonObject.toString(), false);
                return null;
            }
        }.execute((Object[]) new Void[0]);
    }

    public static void reportFailure(Context context, String reason) {
        JSONObject baseJson = getBaseJson();
        try {
            baseJson.put(STR_FAIL_PKG, context.getPackageName());
            baseJson.put(STR_FAIL_VERSION, getVersion(context));
            baseJson.put(STR_FAIL_REASON, reason);
            baseJson.put("action", "4");
            report(baseJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void reportImproveTime(long time) {
        try {
            JSONObject baseJson = getBaseJson();
            baseJson.put(STR_DEAL_TIME, String.valueOf(time));
            baseJson.put("action", "5");
            report(baseJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void reportImprove() {
        try {
            JSONObject baseJson = getBaseJson();
            baseJson.put("action", "1");
            report(baseJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void reportOpen(String pkg) {
        try {
            JSONObject baseJson = getBaseJson();
            baseJson.put(STR_FAIL_PKG, pkg);
            baseJson.put("action", "2");
            report(baseJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void reportClose(String pkg) {
        try {
            JSONObject baseJson = getBaseJson();
            baseJson.put(STR_FAIL_PKG, pkg);
            baseJson.put("action", "3");
            report(baseJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getBaseJson() {
        String str = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", str);
            jsonObject.put(STR_DEAL_TIME, str);
            jsonObject.put(STR_FAIL_PKG, str);
            jsonObject.put(STR_FAIL_VERSION, str);
            jsonObject.put(STR_FAIL_REASON, str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String getVersion(Context ctx) {
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "unknow";
        }
    }
}
