package miui.securitycenter.powercenter;

import android.os.Build.VERSION;

public class HistoryItemWrapper {
    static final byte CMD_CURRENT_TIME = (byte) 5;
    static final byte CMD_NULL = (byte) -1;
    static final byte CMD_OVERFLOW = (byte) 6;
    static final byte CMD_RESET = (byte) 7;
    static final byte CMD_SHUTDOWN = (byte) 8;
    static final byte CMD_START = (byte) 4;
    static final byte CMD_UPDATE = (byte) 0;
    byte batteryHealth;
    byte batteryLevel;
    byte batteryPlugType;
    byte batteryStatus;
    short batteryTemperature;
    char batteryVoltage;
    boolean charging;
    byte cmd = (byte) -1;
    boolean cpuRunning;
    boolean gpsOn;
    int phoneSignalStrength;
    boolean screenOn;
    long time;
    boolean wakelockOn;
    boolean wifiOn;

    public boolean isDeltaData() {
        boolean z = false;
        if (VERSION.SDK_INT >= 21) {
            if (this.cmd == (byte) 0) {
                z = true;
            }
            return z;
        }
        if (this.cmd == (byte) 1) {
            z = true;
        }
        return z;
    }

    public boolean isOverflow() {
        return this.cmd == (byte) 6;
    }

    public Object getObjectValue(String key) {
        if (key.equals("time")) {
            return Long.valueOf(this.time);
        }
        if (key.equals("cmd")) {
            return Integer.valueOf(this.cmd);
        }
        if (key.equals("batteryLevel")) {
            return Integer.valueOf(this.batteryLevel);
        }
        if (key.equals("batteryStatus")) {
            return Integer.valueOf(this.batteryStatus);
        }
        if (key.equals("batteryHealth")) {
            return Integer.valueOf(this.batteryHealth);
        }
        if (key.equals("batteryPlugType")) {
            return Integer.valueOf(this.batteryPlugType);
        }
        if (key.equals("batteryTemperature")) {
            return Integer.valueOf(this.batteryTemperature);
        }
        if (key.equals("batteryVoltage")) {
            return Integer.valueOf(this.batteryVoltage);
        }
        if (key.equals("wifiOn")) {
            return Boolean.valueOf(this.wifiOn);
        }
        if (key.equals("gpsOn")) {
            return Boolean.valueOf(this.gpsOn);
        }
        if (key.equals("charging")) {
            return Boolean.valueOf(this.charging);
        }
        if (key.equals("screenOn")) {
            return Boolean.valueOf(this.screenOn);
        }
        if (key.equals("wakelockOn")) {
            return Boolean.valueOf(this.wakelockOn);
        }
        if (key.equals("phoneSignalStrength")) {
            return Integer.valueOf(this.phoneSignalStrength);
        }
        if (key.equals("cpuRunning")) {
            return Boolean.valueOf(this.cpuRunning);
        }
        return null;
    }

    public long getTime() {
        return this.time;
    }
}
