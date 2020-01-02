package android.companion;

import android.annotation.UnsupportedAppUsage;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanFilter;
import android.net.wifi.ScanResult;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class BluetoothDeviceFilterUtils {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "BluetoothDeviceFilterUtils";

    private BluetoothDeviceFilterUtils() {
    }

    static String patternToString(Pattern p) {
        return p == null ? null : p.pattern();
    }

    static Pattern patternFromString(String s) {
        return s == null ? null : Pattern.compile(s);
    }

    static boolean matches(ScanFilter filter, BluetoothDevice device) {
        return matchesAddress(filter.getDeviceAddress(), device) && matchesServiceUuid(filter.getServiceUuid(), filter.getServiceUuidMask(), device);
    }

    static boolean matchesAddress(String deviceAddress, BluetoothDevice device) {
        return deviceAddress == null || (device != null && deviceAddress.equals(device.getAddress()));
    }

    static boolean matchesServiceUuids(List<ParcelUuid> serviceUuids, List<ParcelUuid> serviceUuidMasks, BluetoothDevice device) {
        for (int i = 0; i < serviceUuids.size(); i++) {
            if (!matchesServiceUuid((ParcelUuid) serviceUuids.get(i), (ParcelUuid) serviceUuidMasks.get(i), device)) {
                return false;
            }
        }
        return true;
    }

    static boolean matchesServiceUuid(ParcelUuid serviceUuid, ParcelUuid serviceUuidMask, BluetoothDevice device) {
        ParcelUuid[] uuids = device.getUuids();
        if (serviceUuid != null) {
            if (!ScanFilter.matchesServiceUuids(serviceUuid, serviceUuidMask, uuids == null ? Collections.emptyList() : Arrays.asList(uuids))) {
                return false;
            }
        }
        return true;
    }

    static boolean matchesName(Pattern namePattern, BluetoothDevice device) {
        if (namePattern == null) {
            return true;
        }
        if (device == null) {
            return false;
        }
        String name = device.getName();
        boolean z = name != null && namePattern.matcher(name).find();
        return z;
    }

    static boolean matchesName(Pattern namePattern, ScanResult device) {
        if (namePattern == null) {
            return true;
        }
        if (device == null) {
            return false;
        }
        String name = device.SSID;
        boolean z = name != null && namePattern.matcher(name).find();
        return z;
    }

    private static void debugLogMatchResult(boolean result, BluetoothDevice device, Object criteria) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getDeviceDisplayNameInternal(device));
        stringBuilder.append(result ? " ~ " : " !~ ");
        stringBuilder.append(criteria);
        Log.i(LOG_TAG, stringBuilder.toString());
    }

    private static void debugLogMatchResult(boolean result, ScanResult device, Object criteria) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getDeviceDisplayNameInternal(device));
        stringBuilder.append(result ? " ~ " : " !~ ");
        stringBuilder.append(criteria);
        Log.i(LOG_TAG, stringBuilder.toString());
    }

    @UnsupportedAppUsage
    public static String getDeviceDisplayNameInternal(BluetoothDevice device) {
        return TextUtils.firstNotEmpty(device.getAliasName(), device.getAddress());
    }

    @UnsupportedAppUsage
    public static String getDeviceDisplayNameInternal(ScanResult device) {
        return TextUtils.firstNotEmpty(device.SSID, device.BSSID);
    }

    @UnsupportedAppUsage
    public static String getDeviceMacAddress(Parcelable device) {
        if (device instanceof BluetoothDevice) {
            return ((BluetoothDevice) device).getAddress();
        }
        if (device instanceof ScanResult) {
            return ((ScanResult) device).BSSID;
        }
        if (device instanceof android.bluetooth.le.ScanResult) {
            return getDeviceMacAddress(((android.bluetooth.le.ScanResult) device).getDevice());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown device type: ");
        stringBuilder.append(device);
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
