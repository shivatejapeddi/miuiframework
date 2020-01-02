package miui.app;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.miui.R;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.provider.MiuiSettings;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import android.util.Log;
import miui.os.Build;

public class WifiApEnabler {
    private static final String TAG = "WifiApEnabler";
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private boolean mDisabledByAirplane;
    private final IntentFilter mIntentFilter;
    private boolean mOpen;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String str = "wifi_state";
            if (WifiManager.WIFI_AP_STATE_CHANGED_ACTION.equals(action)) {
                WifiApEnabler.this.handleWifiApStateChanged(intent.getIntExtra(str, 14));
            } else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
                WifiApEnabler.this.updateAirplaneMode();
            } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action) && WifiApEnabler.this.mWaitForWifiStateChange) {
                WifiApEnabler.this.handleWifiStateChanged(intent.getIntExtra(str, 4));
            }
        }
    };
    private boolean mStatusChanging;
    private ToggleManager mToggleManager;
    private boolean mWaitForWifiStateChange;
    private WifiConfiguration mWifiConfig;
    private WifiManager mWifiManager;

    public WifiApEnabler(Context context, ToggleManager toggleManager) {
        this.mContext = context;
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        this.mToggleManager = toggleManager;
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.mIntentFilter = new IntentFilter(WifiManager.WIFI_AP_STATE_CHANGED_ACTION);
        this.mIntentFilter.addAction(ConnectivityManager.ACTION_TETHER_STATE_CHANGED);
        this.mIntentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        this.mContext.registerReceiver(this.mReceiver, this.mIntentFilter);
        updateAirplaneMode();
        this.mOpen = isWifiApOn();
    }

    public void unregisterReceiver() {
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    private void updateAirplaneMode() {
        boolean z = false;
        if (System.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) != 0) {
            z = true;
        }
        this.mDisabledByAirplane = z;
        updateToggle();
    }

    /* Access modifiers changed, original: 0000 */
    public void setSoftapEnabled(boolean enable) {
        if (enable) {
            initWifiTethering();
        }
        if (VERSION.SDK_INT < 24) {
            setSoftapEnabledWithWifiManager(enable);
        } else {
            setSoftapEnabledWithConnectivityManager(enable);
        }
    }

    private void setSoftapEnabledWithConnectivityManager(boolean enable) {
        boolean result;
        StringBuilder stringBuilder = new StringBuilder();
        String str = "setSoftapEnabledWithConnectivityManager() enable=";
        stringBuilder.append(str);
        stringBuilder.append(enable);
        String stringBuilder2 = stringBuilder.toString();
        String str2 = TAG;
        Log.d(str2, stringBuilder2);
        if (enable) {
            result = ConnectivityManagerReflector.startTethering(this.mConnectivityManager, 0, true);
        } else {
            result = ConnectivityManagerReflector.stopTethering(this.mConnectivityManager, 0);
        }
        updateToggle();
        if (!result) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str);
            stringBuilder3.append(enable);
            stringBuilder3.append(";result=");
            stringBuilder3.append(result);
            Log.e(str2, stringBuilder3.toString());
        }
    }

    private void setSoftapEnabledWithWifiManager(boolean enable) {
        ContentResolver cr = this.mContext.getContentResolver();
        int wifiState = this.mWifiManager.getWifiState();
        int i = VERSION.SDK_INT;
        String str = Global.WIFI_SAVED_STATE;
        if (i < 23 && enable && (wifiState == 2 || wifiState == 3)) {
            this.mWifiManager.setWifiEnabled(false);
            Global.putInt(cr, str, 1);
        }
        if (CompatibilityP.setWifiApEnabled(this.mWifiManager, enable)) {
            this.mStatusChanging = true;
            this.mOpen = enable;
            updateToggle();
        }
        if (VERSION.SDK_INT < 23 && !enable && Global.getInt(cr, str, 0) == 1) {
            this.mWaitForWifiStateChange = true;
            this.mWifiManager.setWifiEnabled(true);
            Global.putInt(cr, str, 0);
        }
    }

    private void handleWifiApStateChanged(int state) {
        switch (state) {
            case 10:
                this.mOpen = false;
                this.mStatusChanging = true;
                break;
            case 11:
                this.mOpen = false;
                if (!this.mWaitForWifiStateChange) {
                    this.mStatusChanging = false;
                    break;
                }
                break;
            case 12:
                this.mOpen = true;
                this.mStatusChanging = true;
                break;
            case 13:
                this.mOpen = true;
                this.mStatusChanging = false;
                break;
            default:
                this.mOpen = false;
                this.mStatusChanging = false;
                break;
        }
        updateToggle();
    }

    private void updateToggle() {
        updateWifiApToggle(true);
    }

    /* Access modifiers changed, original: 0000 */
    public void updateWifiApToggle(boolean updateMiDrop) {
        int i;
        this.mToggleManager.updateToggleStatus(24, this.mOpen);
        ToggleManager toggleManager = this.mToggleManager;
        boolean z = this.mStatusChanging || this.mDisabledByAirplane;
        toggleManager.updateToggleDisabled(24, z);
        toggleManager = this.mToggleManager;
        if (this.mOpen) {
            i = R.drawable.status_bar_toggle_wifi_ap_on;
        } else {
            i = R.drawable.status_bar_toggle_wifi_ap_off;
        }
        toggleManager.updateToggleImage(24, i);
        if (updateMiDrop && this.mToggleManager.useWifiApForMiDrop()) {
            this.mToggleManager.updateMiDropToggle(false);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isWifiApDisabled() {
        return this.mStatusChanging || this.mDisabledByAirplane;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isWifiApOn() {
        return this.mWifiManager.getWifiApState() == 13;
    }

    public void toggleWifiAp() {
        if (!ToggleManager.isDisabled(24)) {
            if (this.mOpen) {
                setSoftapEnabled(false);
            } else {
                setSoftapEnabled(true);
            }
        }
    }

    private void handleWifiStateChanged(int state) {
        if (state == 3 || state == 4) {
            this.mWaitForWifiStateChange = false;
            this.mStatusChanging = false;
            updateToggle();
        }
    }

    private void initWifiTethering() {
        this.mWifiConfig = this.mWifiManager.getWifiApConfiguration();
        if (this.mWifiConfig != null && this.mContext.getString(R.string.android_wifi_tether_configure_ssid_default).equals(this.mWifiConfig.SSID)) {
            String str;
            WifiConfiguration wifiConfiguration = this.mWifiConfig;
            if (Build.IS_CM_CUSTOMIZATION_TEST) {
                str = Build.DEVICE;
            } else {
                str = MiuiSettings.System.getDeviceName(this.mContext);
            }
            wifiConfiguration.SSID = str;
            this.mWifiManager.setWifiApConfiguration(this.mWifiConfig);
        }
    }
}
