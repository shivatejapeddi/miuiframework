package miui.maml.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class WifiApHelper {
    private static final String TAG = "WifiApHelper";
    private ConnectivityManager mConnectivityManager = ((ConnectivityManager) this.mContext.getSystemService("connectivity"));
    private Context mContext;

    public WifiApHelper(Context context) {
        this.mContext = context;
    }

    public void setWifiApEnabled(boolean enable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setWifiApEnabled() enable=");
        stringBuilder.append(enable);
        Log.d(TAG, stringBuilder.toString());
        if (enable) {
            this.mConnectivityManager.startTethering(0, true, null);
        } else {
            this.mConnectivityManager.stopTethering(0);
        }
    }
}
