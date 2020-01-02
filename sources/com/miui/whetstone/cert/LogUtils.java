package com.miui.whetstone.cert;

public class LogUtils {
    public static final int LOG_TYPE_IP = 1;

    public static void switchData(int tag, byte[] data) {
        if (data != null && tag == 1) {
            for (int i = 0; i < data.length - 1; i += 2) {
                byte temp = data[i];
                data[i] = data[i + 1];
                data[i + 1] = temp;
            }
        }
    }
}
