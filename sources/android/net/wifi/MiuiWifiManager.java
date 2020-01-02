package android.net.wifi;

import android.content.Context;
import android.net.wifi.IWifiManager.Stub;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.util.AsyncChannel;
import java.util.ArrayList;
import java.util.List;

public class MiuiWifiManager {
    private static final int BASE = 155552;
    public static final int CMD_FIXED_FREQ_DISCOVER = 155573;
    public static final int CMD_GET_CONNECTED_STATIONS = 155562;
    public static final int CMD_GET_OBSERVED_ACCESSPOINTS = 155555;
    public static final int CMD_IGNORE_OBSERVED_AP = 155559;
    public static final int CMD_SET_LATENCY_LEVEL = 155563;
    public static final int CMD_SET_OBSERVED_ACCESSPOINTS = 155554;
    public static final int CMD_SET_P2P_CONFIG = 155574;
    public static final int CMD_SET_POWER_SAVE_ENABLED = 155561;
    public static final int CMD_SET_SAR_LIMIT = 155560;
    public static final int CMD_SET_WIFI_EXPLICITED = 155557;
    public static final int CMD_SET_WIRELESS_CONNECT_MODE = 155556;
    public static final int CMD_VERIFY_PRE_SHARED_KEY = 155558;
    public static final String EXTRA_APS = "extra_aps";
    public static final String EXTRA_BSSID = "bssid";
    public static final String EXTRA_CONFIG = "config";
    public static final String EXTRA_KEY = "key";
    public static final String EXTRA_SSID = "ssid";
    public static final String EXTRA_STATIONS = "stations";
    public static final int FAILED = 2;
    public static final int GET_SUPPLICANT_CONFIGURATION = 155553;
    private static final int MAX_RSSI = -65;
    private static final int MIN_RSSI = -100;
    public static final String OBSERVED_ACCESSPOINTS_CHANGED = "android.net.wifi.observed_accesspionts_changed";
    public static final String OBSERVED_OPENAPS_CHANGED = "android.net.wifi.observed_open_accesspionts_changed";
    public static final int SUCCESS = 1;
    private static final String TAG = "MiuiWifiManager";
    public static final String WPS_DEVICE_GUEST = "guest";
    public static final String WPS_DEVICE_XIAOMI = "xiaomi";
    private static MiuiWifiManager sInstance;
    private AsyncChannel mAsyncChannel;
    private Context mContext;

    public static int calculateSignalLevel(int rssi, int numLevels) {
        if (rssi <= -100) {
            return 0;
        }
        if (rssi >= MAX_RSSI) {
            return numLevels - 1;
        }
        return (int) ((((float) (rssi + 100)) * ((float) (numLevels - 1))) / 35.0f);
    }

    public static MiuiWifiManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MiuiWifiManager(context);
        }
        return sInstance;
    }

    private MiuiWifiManager(Context context) {
        String str = TAG;
        try {
            Context appContext = context.getApplicationContext();
            this.mContext = appContext == null ? context : appContext;
            IWifiManager service = Stub.asInterface(ServiceManager.getService("wifi"));
            HandlerThread workThread = new HandlerThread(str);
            workThread.start();
            this.mAsyncChannel = new AsyncChannel();
            this.mAsyncChannel.connect(null, new Handler(workThread.getLooper()), service.getWifiServiceMessenger(this.mContext.getOpPackageName()));
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("build WifiConfigForSupplicant failed exception ");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mAsyncChannel != null) {
                this.mAsyncChannel.disconnect();
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public void setObservedAccessPionts(List<String> observedAPs) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(EXTRA_APS, new ArrayList(observedAPs));
        Message msg = Message.obtain();
        msg.what = CMD_SET_OBSERVED_ACCESSPOINTS;
        msg.obj = bundle;
        sendAsyncMessage(msg);
    }

    public List<String> getObservedAccessPionts() {
        Message msg = Message.obtain();
        msg.what = CMD_GET_OBSERVED_ACCESSPOINTS;
        Message msgRusult = this.mAsyncChannel.sendMessageSynchronously(msg);
        List<String> observAps = null;
        Bundle bundle = msgRusult.obj;
        if (msgRusult.arg1 == 1 && bundle != null) {
            observAps = bundle.getStringArrayList(EXTRA_APS);
        }
        msgRusult.recycle();
        return observAps;
    }

    public String getConnectedStations() {
        Message msg = Message.obtain();
        msg.what = CMD_GET_CONNECTED_STATIONS;
        Message msgRusult = this.mAsyncChannel.sendMessageSynchronously(msg);
        String stations = null;
        Bundle bundle = msgRusult.obj;
        if (msgRusult.arg1 == 1 && bundle != null) {
            stations = bundle.getString(EXTRA_STATIONS);
        }
        msgRusult.recycle();
        return stations;
    }

    public void setCompatibleMode(boolean enabled) {
        this.mAsyncChannel.sendMessage((int) CMD_SET_WIRELESS_CONNECT_MODE, (int) enabled);
    }

    public void sendAsyncMessage(Message msg) {
        this.mAsyncChannel.sendMessage(msg);
    }

    public Message sendSyncMessage(Message msg) {
        return this.mAsyncChannel.sendMessageSynchronously(msg);
    }

    public void setNetworkExplicited() {
        Message msg = Message.obtain();
        msg.what = CMD_SET_WIFI_EXPLICITED;
        sendAsyncMessage(msg);
    }

    public boolean verifyPreSharedKey(WifiConfiguration config, String preSharedKey) {
        String str = TAG;
        Log.d(str, "Verify shared key");
        boolean result = false;
        if (VERSION.SDK_INT < 23) {
            Log.e(str, "Cannot verify shared key in api lower than 23");
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CONFIG, config);
        bundle.putString("key", preSharedKey);
        Message msg = Message.obtain();
        msg.obj = bundle;
        msg.what = CMD_VERIFY_PRE_SHARED_KEY;
        Message msgRusult = sendSyncMessage(msg);
        if (msgRusult.arg1 == 1) {
            result = true;
        }
        msgRusult.recycle();
        return result;
    }

    public void ignoreApsForScanObserver(ArrayList<String> bssids, boolean ignore) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ignore observed: ");
        stringBuilder.append(ignore);
        Log.d(TAG, stringBuilder.toString());
        Message msg = Message.obtain();
        msg.what = CMD_IGNORE_OBSERVED_AP;
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("bssid", bssids);
        msg.obj = bundle;
        msg.arg1 = ignore;
        sendAsyncMessage(msg);
    }

    public void setSARLimit(int set) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("set SAR limit to SET ");
        stringBuilder.append(set);
        Log.d(TAG, stringBuilder.toString());
        Message msg = Message.obtain();
        msg.what = CMD_SET_SAR_LIMIT;
        msg.arg1 = set;
        sendAsyncMessage(msg);
    }

    public void discoverPeersOnTheFixedFreq(int freq) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Discover Peers on a Fixed freq ");
        stringBuilder.append(freq);
        Log.d(TAG, stringBuilder.toString());
        Message msg = Message.obtain();
        msg.what = CMD_FIXED_FREQ_DISCOVER;
        msg.arg1 = freq;
        sendAsyncMessage(msg);
    }

    public void enablePowerSave(boolean enabled) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Enable power save: ");
        stringBuilder.append(enabled);
        Log.d(TAG, stringBuilder.toString());
        Message msg = Message.obtain();
        msg.what = CMD_SET_POWER_SAVE_ENABLED;
        msg.arg1 = enabled;
        sendAsyncMessage(msg);
    }

    public void setLatencyLevel(int level) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("set latency to level ");
        stringBuilder.append(level);
        Log.d(TAG, stringBuilder.toString());
        Message msg = Message.obtain();
        msg.what = CMD_SET_LATENCY_LEVEL;
        msg.arg1 = level;
        sendAsyncMessage(msg);
    }
}
