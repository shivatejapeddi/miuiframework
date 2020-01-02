package android.bluetooth;

import android.os.ParcelFileDescriptor;
import android.util.Log;

@Deprecated
public abstract class BluetoothHealthCallback {
    private static final String TAG = "BluetoothHealthCallback";

    @Deprecated
    public void onHealthAppConfigurationStatusChange(BluetoothHealthAppConfiguration config, int status) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onHealthAppConfigurationStatusChange: ");
        stringBuilder.append(config);
        stringBuilder.append("Status: ");
        stringBuilder.append(status);
        Log.d(TAG, stringBuilder.toString());
    }

    @Deprecated
    public void onHealthChannelStateChange(BluetoothHealthAppConfiguration config, BluetoothDevice device, int prevState, int newState, ParcelFileDescriptor fd, int channelId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onHealthChannelStateChange: ");
        stringBuilder.append(config);
        stringBuilder.append("Device: ");
        stringBuilder.append(device);
        stringBuilder.append("prevState:");
        stringBuilder.append(prevState);
        stringBuilder.append("newState:");
        stringBuilder.append(newState);
        stringBuilder.append("ParcelFd:");
        stringBuilder.append(fd);
        stringBuilder.append("ChannelId:");
        stringBuilder.append(channelId);
        Log.d(TAG, stringBuilder.toString());
    }
}
