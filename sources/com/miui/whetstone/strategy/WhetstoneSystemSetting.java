package com.miui.whetstone.strategy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;
import com.miui.whetstone.server.WhetstoneActivityManagerService;
import java.util.Iterator;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WhetstoneSystemSetting extends Observable {
    private static final String TAG = WhetstoneSystemSetting.class.getSimpleName();
    private ConcurrentHashMap<String, Object> mConfigMap = new ConcurrentHashMap();
    private Context mContext;
    private WhetstoneActivityManagerService mWhetstoneActivityManagerService;

    public WhetstoneSystemSetting(Context context, WhetstoneActivityManagerService service) {
        this.mContext = context;
        this.mWhetstoneActivityManagerService = service;
    }

    public boolean finishIsStartPorcessAllowedByBroadcast(int callingPid, Intent intent, ComponentName name, Boolean ret) {
        if (Intent.ACTION_BOOT_COMPLETED != intent.getAction()) {
            return finishCommonAllowed(callingPid, name, ret);
        }
        ret = Boolean.valueOf(true);
        return true;
    }

    public boolean finishstartServiceAllowed(int callingPid, Intent service, Boolean ret) {
        ret = Boolean.valueOf(true);
        return true;
    }

    private boolean finishCommonAllowed(int callingPid, ComponentName name, Boolean ret) {
        if (null != null || name.getPackageName() == null) {
            return false;
        }
        String packageName = this.mWhetstoneActivityManagerService.getPackageNamebyPid(callingPid);
        if (packageName == null || !packageName.equals(name.getPackageName())) {
            return false;
        }
        ret = Boolean.valueOf(true);
        return true;
    }

    public boolean finishstartServiceAllowed(int callingPid, ComponentName name, int intentFlags, boolean whileRestarting, boolean ret) {
        boolean finish = false;
        if (callingPid == Process.myPid()) {
            return true;
        }
        if (name != null) {
            finish = finishCommonAllowed(callingPid, name, Boolean.valueOf(ret));
        }
        return finish;
    }

    public void updateFrameworkCommonConfig(String json) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("updateFrameworkCommonConfig json:");
        stringBuilder.append(json);
        Log.d(str, stringBuilder.toString());
        try {
            JSONObject jobj = new JSONObject(json);
            Iterator<String> it = jobj.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                this.mConfigMap.put(key, jobj.get(key));
            }
        } catch (JSONException e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("updateFrameworkCommonConfig JSONException e:");
            stringBuilder2.append(e.getMessage());
            Log.w(str2, stringBuilder2.toString());
        }
        setChanged();
        notifyObservers();
    }

    public boolean getCommonConfigInBoolean(String key, boolean defValue) {
        boolean ret = defValue;
        Object value = this.mConfigMap.get(key);
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        return ret;
    }

    public int getCommonConfigInInt(String key, int defValue) {
        int ret = defValue;
        Object value = this.mConfigMap.get(key);
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        }
        return ret;
    }

    public String getCommonConfigInString(String key, String defValue) {
        String ret = defValue;
        String value = this.mConfigMap.get(key);
        if (value instanceof String) {
            return value;
        }
        return ret;
    }

    public JSONObject getCommonConfigInJSONObject(String key) {
        Object value = this.mConfigMap.get(key);
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        return null;
    }

    public JSONArray getCommonConfigInJSONArray(String key) {
        Object value = this.mConfigMap.get(key);
        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }
        return null;
    }
}
