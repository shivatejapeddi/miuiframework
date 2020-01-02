package com.miui.mishare.app.model;

import android.net.Uri;
import com.miui.mishare.RemoteDevice;
import java.util.List;
import java.util.Random;

public class MiShareDevice {
    public String deviceId;
    public String deviceModelName;
    public String deviceName;
    public int deviceStatus = 1;
    public int deviceType;
    public List<Uri> files;
    public RemoteDevice remoteDevice;
    public String taskId;

    private static class TokenGenerator {
        private static final Random RANDOM = new Random();

        private TokenGenerator() {
        }

        private static long get() {
            return RANDOM.nextLong();
        }
    }

    public MiShareDevice(int guidingNetworkType) {
        if (guidingNetworkType == 2) {
            this.taskId = generatePCTaskId();
        } else {
            this.taskId = Long.toString(TokenGenerator.get());
        }
    }

    public boolean isPC() {
        RemoteDevice remoteDevice = this.remoteDevice;
        return remoteDevice != null && remoteDevice.isPC();
    }

    public static String generatePCTaskId() {
        return Integer.toString(TokenGenerator.RANDOM.nextInt(65532) + 2);
    }
}
