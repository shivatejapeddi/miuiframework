package com.android.qualcomm.qti.seccamapi;

import android.os.Message;
import android.util.Log;

public class SecCamServiceVendorClient {
    private static final String LOG_TAG = "SECCAM-SERVICE-VENDOR-CLIENT";
    private static final String MSG_DATA_RESULT = "result";
    private static final String MSG_DATA_TZTIMESTAMP = "tzTimestamp";
    public static final int MSG_VENDOR_EXCHANGE_TIMESTAMP = 2000;
    public static final int MSG_VENDOR_INIT_BIOMETRICS_BUFFER = 2001;
    public static final int MSG_VENDOR_RUN_BIOMETRICS_TEST = 2002;
    public static final int MSG_VENDOR_START_SECCAM_USECASE = 2003;
    public static final int MSG_VENDOR_STOP_SECCAM_USECASE = 2004;

    public static boolean handleVendorMessage(Message msg) {
        String str = LOG_TAG;
        Log.d(str, "handleVendorMessage");
        String str2 = "result";
        long result;
        StringBuilder stringBuilder;
        switch (msg.what) {
            case 2000:
                result = msg.getData().getLong(MSG_DATA_TZTIMESTAMP);
                stringBuilder = new StringBuilder();
                stringBuilder.append("handleVendorMessage(EXCHANGE TIMESTAMP) - TA timestamp recieved: ");
                stringBuilder.append(result);
                Log.d(str, stringBuilder.toString());
                return true;
            case 2001:
                result = msg.getData().getLong(str2);
                stringBuilder = new StringBuilder();
                stringBuilder.append("handleVendorMessage(INIT BIOMETRICS BUFFER) - result: ");
                stringBuilder.append(result);
                Log.d(str, stringBuilder.toString());
                return true;
            case 2002:
                result = msg.getData().getLong(str2);
                stringBuilder = new StringBuilder();
                stringBuilder.append("handleVendorMessage(RUN BIOMETRICS TEST) - result: ");
                stringBuilder.append(result);
                Log.d(str, stringBuilder.toString());
                return true;
            case 2003:
                result = msg.getData().getLong(str2);
                stringBuilder = new StringBuilder();
                stringBuilder.append("handleVendorMessage(MSG_VENDOR_START_SECCAM_USECASE) - result: ");
                stringBuilder.append(result);
                Log.d(str, stringBuilder.toString());
                return true;
            case 2004:
                result = msg.getData().getLong(str2);
                stringBuilder = new StringBuilder();
                stringBuilder.append("handleVendorMessage(MSG_VENDOR_STOP_SECCAM_USECASE) - result: ");
                stringBuilder.append(result);
                Log.d(str, stringBuilder.toString());
                return true;
            default:
                Log.d(str, "recieved unhandled command ID");
                return false;
        }
    }
}
