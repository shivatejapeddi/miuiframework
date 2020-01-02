package miui.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import miui.os.Environment;
import org.json.JSONObject;

public class Network {
    public static final String CMWAP_GATEWAY = "10.0.0.172";
    public static final String CMWAP_HEADER_HOST_KEY = "X-Online-Host";
    public static final int CONNECTION_TIMEOUT = 10000;
    private static final String LogTag = Network.class.getSimpleName();
    public static final String OPERATOR_TYPE_FILE_NAME = "ot";
    public static final int READ_TIMEOUT = 15000;
    public static final String RESPONSE_BODY = "RESPONSE_BODY";
    public static final String RESPONSE_CODE = "RESPONSE_CODE";

    public static JSONObject doHttpPostWithResponseStatus(Context context, String url, String postJson) {
        if (context == null) {
            throw new IllegalArgumentException("context");
        } else if (url != null && url.trim().length() != 0) {
            return new JSONObject();
        } else {
            throw new IllegalArgumentException("url");
        }
    }

    public static boolean isCmwap(Context context) {
        if (!"CN".equalsIgnoreCase(((TelephonyManager) context.getSystemService("phone")).getSimCountryIso())) {
            return false;
        }
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        String extraInfo = info.getExtraInfo();
        if (TextUtils.isEmpty(extraInfo) || extraInfo.length() < 3 || extraInfo.contains("ctwap")) {
            return false;
        }
        return extraInfo.regionMatches(true, extraInfo.length() - 3, "wap", 0, 3);
    }

    public static String getCMWapUrl(URL oriUrl) {
        StringBuilder gatewayBuilder = new StringBuilder();
        gatewayBuilder.append(oriUrl.getProtocol());
        gatewayBuilder.append("://");
        gatewayBuilder.append(CMWAP_GATEWAY);
        gatewayBuilder.append(oriUrl.getPath());
        if (!TextUtils.isEmpty(oriUrl.getQuery())) {
            gatewayBuilder.append("?");
            gatewayBuilder.append(oriUrl.getQuery());
        }
        return gatewayBuilder.toString();
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            NetworkInfo mNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String getOperatorType() {
        String ot = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(Environment.getMiuiDataDirectory(), OPERATOR_TYPE_FILE_NAME)));
            ot = reader.readLine();
            try {
                reader.close();
            } catch (IOException e) {
            }
        } catch (IOException e2) {
            if (reader != null) {
                reader.close();
            }
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e3) {
                }
            }
        }
        return ot;
    }
}
