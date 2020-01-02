package android.bluetooth;

import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothAvrcpController.Stub;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothAvrcpController implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.avrcp-controller.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_PLAYER_SETTING = "android.bluetooth.avrcp-controller.profile.action.PLAYER_SETTING";
    private static final boolean DBG = false;
    public static final String EXTRA_PLAYER_SETTING = "android.bluetooth.avrcp-controller.profile.extra.PLAYER_SETTING";
    private static final String TAG = "BluetoothAvrcpController";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private final BluetoothProfileConnector<IBluetoothAvrcpController> mProfileConnector = new BluetoothProfileConnector(this, 12, TAG, IBluetoothAvrcpController.class.getName()) {
        public IBluetoothAvrcpController getServiceInterface(IBinder service) {
            return Stub.asInterface(Binder.allowBlocking(service));
        }
    };

    BluetoothAvrcpController(Context context, ServiceListener listener) {
        this.mProfileConnector.connect(context, listener);
    }

    /* Access modifiers changed, original: 0000 */
    public void close() {
        this.mProfileConnector.disconnect();
    }

    private IBluetoothAvrcpController getService() {
        return (IBluetoothAvrcpController) this.mProfileConnector.getService();
    }

    public void finalize() {
        close();
    }

    public List<BluetoothDevice> getConnectedDevices() {
        IBluetoothAvrcpController service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            str = service.getConnectedDevices();
            return str;
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stack:");
            stringBuilder.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder.toString());
            return new ArrayList();
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        IBluetoothAvrcpController service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            str = service.getDevicesMatchingConnectionStates(states);
            return str;
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stack:");
            stringBuilder.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder.toString());
            return new ArrayList();
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        IBluetoothAvrcpController service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getConnectionState(device);
                return str;
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Stack:");
                stringBuilder.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder.toString());
                return 0;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return 0;
    }

    public BluetoothAvrcpPlayerSettings getPlayerSettings(BluetoothDevice device) {
        BluetoothAvrcpPlayerSettings settings = null;
        IBluetoothAvrcpController service = getService();
        if (service != null && isEnabled()) {
            try {
                settings = service.getPlayerSettings(device);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error talking to BT service in getMetadata() ");
                stringBuilder.append(e);
                Log.e(TAG, stringBuilder.toString());
                return null;
            }
        }
        return settings;
    }

    public boolean setPlayerApplicationSetting(BluetoothAvrcpPlayerSettings plAppSetting) {
        IBluetoothAvrcpController service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return false;
        }
        try {
            str = service.setPlayerApplicationSetting(plAppSetting);
            return str;
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error talking to BT service in setPlayerApplicationSetting() ");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
            return false;
        }
    }

    public void sendGroupNavigationCmd(BluetoothDevice device, int keyCode, int keyState) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("sendGroupNavigationCmd dev = ");
        stringBuilder.append(device);
        stringBuilder.append(" key ");
        stringBuilder.append(keyCode);
        stringBuilder.append(" State = ");
        stringBuilder.append(keyState);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        IBluetoothAvrcpController service = getService();
        if (service == null || !isEnabled()) {
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return;
        }
        try {
            service.sendGroupNavigationCmd(device, keyCode, keyState);
        } catch (RemoteException e) {
            Log.e(str, "Error talking to BT service in sendGroupNavigationCmd()", e);
        }
    }

    private boolean isEnabled() {
        return this.mAdapter.getState() == 12;
    }

    private static boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
}
