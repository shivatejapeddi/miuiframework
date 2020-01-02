package android.bluetooth;

import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothManager {
    private static final boolean DBG = false;
    private static final String TAG = "BluetoothManager";
    private final BluetoothAdapter mAdapter;

    public BluetoothManager(Context context) {
        context = context.getApplicationContext();
        if (context != null) {
            this.mAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothAdapter bluetoothAdapter = this.mAdapter;
            if (bluetoothAdapter != null) {
                bluetoothAdapter.setContext(context);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("context not associated with any application (using a mock context?)");
    }

    public BluetoothAdapter getAdapter() {
        return this.mAdapter;
    }

    public int getConnectionState(BluetoothDevice device, int profile) {
        for (BluetoothDevice connectedDevice : getConnectedDevices(profile)) {
            if (device.equals(connectedDevice)) {
                return 2;
            }
        }
        return 0;
    }

    public List<BluetoothDevice> getConnectedDevices(int profile) {
        if (profile == 7 || profile == 8) {
            List<BluetoothDevice> connectedDevices = new ArrayList();
            try {
                IBluetoothGatt iGatt = this.mAdapter.getBluetoothManager().getBluetoothGatt();
                if (iGatt == null) {
                    return connectedDevices;
                }
                connectedDevices = iGatt.getDevicesMatchingConnectionStates(new int[]{2});
                return connectedDevices;
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Profile not supported: ");
            stringBuilder.append(profile);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int profile, int[] states) {
        if (profile == 7 || profile == 8) {
            List<BluetoothDevice> devices = new ArrayList();
            try {
                IBluetoothGatt iGatt = this.mAdapter.getBluetoothManager().getBluetoothGatt();
                if (iGatt == null) {
                    return devices;
                }
                devices = iGatt.getDevicesMatchingConnectionStates(states);
                return devices;
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Profile not supported: ");
            stringBuilder.append(profile);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public BluetoothGattServer openGattServer(Context context, BluetoothGattServerCallback callback) {
        return openGattServer(context, callback, 0);
    }

    public BluetoothGattServer openGattServer(Context context, BluetoothGattServerCallback callback, int transport) {
        String str = TAG;
        if (context == null || callback == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("null parameter: ");
            stringBuilder.append(context);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(callback);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        BluetoothGattServer bluetoothGattServer = null;
        try {
            IBluetoothGatt iGatt = this.mAdapter.getBluetoothManager().getBluetoothGatt();
            if (iGatt == null) {
                Log.e(str, "Fail to get GATT Server connection");
                return null;
            }
            BluetoothGattServer mGattServer = new BluetoothGattServer(iGatt, transport);
            if (Boolean.valueOf(mGattServer.registerCallback(callback)).booleanValue()) {
                bluetoothGattServer = mGattServer;
            }
            return bluetoothGattServer;
        } catch (RemoteException e) {
            Log.e(str, "", e);
            return null;
        }
    }
}
