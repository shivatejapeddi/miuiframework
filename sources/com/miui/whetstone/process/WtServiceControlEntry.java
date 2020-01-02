package com.miui.whetstone.process;

import android.util.Slog;
import com.miui.whetstone.client.WhetstoneClientManager;
import com.miui.whetstone.strategy.WhetstoneSystemSetting;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;

public class WtServiceControlEntry implements Observer {
    private static WtServiceControlEntry mSCE = null;
    private static Set<String> mServiceControlWhitelist;

    static {
        mServiceControlWhitelist = null;
        mServiceControlWhitelist = Collections.newSetFromMap(new ConcurrentHashMap());
    }

    private WtServiceControlEntry() {
        mServiceControlWhitelist.add("com.android.cts");
    }

    public static WtServiceControlEntry getInstance() {
        if (mSCE == null) {
            mSCE = new WtServiceControlEntry();
        }
        return mSCE;
    }

    public void update(Observable obj, Object arg) {
        setServiceControlWhitelist();
    }

    private void setServiceControlWhitelist() {
        mServiceControlWhitelist.clear();
        mServiceControlWhitelist.add("com.android.cts");
        WhetstoneSystemSetting whetstoneSetting = WhetstoneClientManager.getWhetstoneSystemSetting();
        if (whetstoneSetting != null) {
            JSONArray jsonArray = whetstoneSetting.getCommonConfigInJSONArray("servicecontrol_whitelist");
            if (jsonArray != null) {
                int i = 0;
                while (i < jsonArray.length()) {
                    try {
                        mServiceControlWhitelist.add(jsonArray.getString(i));
                        i++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    public static void addAppToServiceControlWhitelist(String pkgName) {
        mServiceControlWhitelist.add(pkgName);
    }

    public static void addAppToServiceControlWhitelist(List<String> listPkg) {
        if (listPkg != null) {
            mServiceControlWhitelist.addAll(listPkg);
        }
    }

    public static void removeAppFromServiceControlWhitelist(String pkgName) {
        if (mServiceControlWhitelist.contains(pkgName)) {
            mServiceControlWhitelist.remove(pkgName);
        } else {
            Slog.d("ServiceControlEntry", "could not remove an nonexist package from whitelist");
        }
    }

    public static boolean isServiceControlEnabled() {
        WhetstoneSystemSetting whetstoneSetting = WhetstoneClientManager.getWhetstoneSystemSetting();
        if (whetstoneSetting != null) {
            return whetstoneSetting.getCommonConfigInBoolean("servicecontrol_enabled", false);
        }
        return false;
    }

    public static boolean isAppInServiceControlWhitelist(String appName) {
        return mServiceControlWhitelist.contains(appName);
    }
}
