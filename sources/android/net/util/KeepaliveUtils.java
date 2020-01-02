package android.net.util;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.net.NetworkCapabilities;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import com.android.internal.R;

public final class KeepaliveUtils {
    public static final String TAG = "KeepaliveUtils";

    public static class KeepaliveDeviceConfigurationException extends AndroidRuntimeException {
        public KeepaliveDeviceConfigurationException(String msg) {
            super(msg);
        }
    }

    public static int[] getSupportedKeepalives(Context context) {
        String[] res = null;
        try {
            res = context.getResources().getStringArray(R.array.config_networkSupportedKeepaliveCount);
        } catch (NotFoundException e) {
        }
        if (res != null) {
            int[] ret = new int[8];
            int length = res.length;
            int i = 0;
            while (i < length) {
                String row = res[i];
                if (TextUtils.isEmpty(row)) {
                    throw new KeepaliveDeviceConfigurationException("Empty string");
                }
                String[] arr = row.split(",");
                if (arr.length == 2) {
                    try {
                        int transport = Integer.parseInt(arr[0]);
                        int supported = Integer.parseInt(arr[1]);
                        StringBuilder stringBuilder;
                        if (!NetworkCapabilities.isValidTransport(transport)) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Invalid transport ");
                            stringBuilder.append(transport);
                            throw new KeepaliveDeviceConfigurationException(stringBuilder.toString());
                        } else if (supported >= 0) {
                            ret[transport] = supported;
                            i++;
                        } else {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Invalid supported count ");
                            stringBuilder.append(supported);
                            stringBuilder.append(" for ");
                            stringBuilder.append(NetworkCapabilities.transportNameOf(transport));
                            throw new KeepaliveDeviceConfigurationException(stringBuilder.toString());
                        }
                    } catch (NumberFormatException e2) {
                        throw new KeepaliveDeviceConfigurationException("Invalid number format");
                    }
                }
                throw new KeepaliveDeviceConfigurationException("Invalid parameter length");
            }
            return ret;
        }
        throw new KeepaliveDeviceConfigurationException("invalid resource");
    }

    public static int getSupportedKeepalivesForNetworkCapabilities(int[] supportedKeepalives, NetworkCapabilities nc) {
        int[] transports = nc.getTransportTypes();
        int i = 0;
        if (transports.length == 0) {
            return 0;
        }
        int supportedCount = supportedKeepalives[transports[0]];
        int length = transports.length;
        while (i < length) {
            int transport = transports[i];
            if (supportedCount > supportedKeepalives[transport]) {
                supportedCount = supportedKeepalives[transport];
            }
            i++;
        }
        return supportedCount;
    }
}
