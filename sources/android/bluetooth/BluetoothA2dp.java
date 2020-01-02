package android.bluetooth;

import android.annotation.UnsupportedAppUsage;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothA2dp.Stub;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.telephony.ims.ImsConferenceState;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothA2dp implements BluetoothProfile {
    @UnsupportedAppUsage
    public static final String ACTION_ACTIVE_DEVICE_CHANGED = "android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED";
    public static final String ACTION_AVRCP_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.AVRCP_CONNECTION_STATE_CHANGED";
    @UnsupportedAppUsage
    public static final String ACTION_CODEC_CONFIG_CHANGED = "android.bluetooth.a2dp.profile.action.CODEC_CONFIG_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_PLAYING_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED";
    private static final boolean DBG = true;
    @UnsupportedAppUsage
    public static final int OPTIONAL_CODECS_NOT_SUPPORTED = 0;
    @UnsupportedAppUsage
    public static final int OPTIONAL_CODECS_PREF_DISABLED = 0;
    @UnsupportedAppUsage
    public static final int OPTIONAL_CODECS_PREF_ENABLED = 1;
    @UnsupportedAppUsage
    public static final int OPTIONAL_CODECS_PREF_UNKNOWN = -1;
    @UnsupportedAppUsage
    public static final int OPTIONAL_CODECS_SUPPORTED = 1;
    @UnsupportedAppUsage
    public static final int OPTIONAL_CODECS_SUPPORT_UNKNOWN = -1;
    public static final int STATE_NOT_PLAYING = 11;
    public static final int STATE_PLAYING = 10;
    private static final String TAG = "BluetoothA2dp";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private final BluetoothProfileConnector<IBluetoothA2dp> mProfileConnector = new BluetoothProfileConnector(this, 2, TAG, IBluetoothA2dp.class.getName()) {
        public IBluetoothA2dp getServiceInterface(IBinder service) {
            return Stub.asInterface(Binder.allowBlocking(service));
        }
    };

    BluetoothA2dp(Context context, ServiceListener listener) {
        this.mProfileConnector.connect(context, listener);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void close() {
        this.mProfileConnector.disconnect();
    }

    private IBluetoothA2dp getService() {
        return (IBluetoothA2dp) this.mProfileConnector.getService();
    }

    public void finalize() {
    }

    @UnsupportedAppUsage
    public boolean connect(BluetoothDevice device) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("connect(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled() && isValidDevice(device)) {
                return service.connect(device);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return false;
        } catch (RemoteException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Stack:");
            stringBuilder2.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder2.toString());
            return false;
        }
    }

    @UnsupportedAppUsage
    public boolean disconnect(BluetoothDevice device) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("disconnect(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled() && isValidDevice(device)) {
                return service.disconnect(device);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return false;
        } catch (RemoteException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Stack:");
            stringBuilder2.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder2.toString());
            return false;
        }
    }

    public List<BluetoothDevice> getConnectedDevices() {
        String str = TAG;
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled()) {
                return service.getConnectedDevices();
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return new ArrayList();
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stack:");
            stringBuilder.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder.toString());
            return new ArrayList();
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        String str = TAG;
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled()) {
                return service.getDevicesMatchingConnectionStates(states);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return new ArrayList();
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stack:");
            stringBuilder.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder.toString());
            return new ArrayList();
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        String str = TAG;
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled() && isValidDevice(device)) {
                return service.getConnectionState(device);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return 0;
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stack:");
            stringBuilder.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder.toString());
            return 0;
        }
    }

    @UnsupportedAppUsage
    public boolean setActiveDevice(BluetoothDevice device) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setActiveDevice(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled() && (device == null || isValidDevice(device))) {
                return service.setActiveDevice(device);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return false;
        } catch (RemoteException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Stack:");
            stringBuilder2.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder2.toString());
            return false;
        }
    }

    @UnsupportedAppUsage
    public BluetoothDevice getActiveDevice() {
        String str = TAG;
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled()) {
                return service.getActiveDevice();
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return null;
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stack:");
            stringBuilder.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder.toString());
            return null;
        }
    }

    public boolean setPriority(BluetoothDevice device, int priority) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setPriority(");
        stringBuilder.append(device);
        stringBuilder.append(", ");
        stringBuilder.append(priority);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        try {
            IBluetoothA2dp service = getService();
            if (service == null || !isEnabled() || !isValidDevice(device)) {
                if (service == null) {
                    Log.w(str, "Proxy not attached to service");
                }
                return false;
            } else if (priority == 0 || priority == 100) {
                return service.setPriority(device, priority);
            } else {
                return false;
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Stack:");
            stringBuilder2.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder2.toString());
            return false;
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public int getPriority(BluetoothDevice device) {
        String str = TAG;
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled() && isValidDevice(device)) {
                return service.getPriority(device);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return 0;
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stack:");
            stringBuilder.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder.toString());
            return 0;
        }
    }

    public boolean isAvrcpAbsoluteVolumeSupported() {
        String str = TAG;
        Log.d(str, "isAvrcpAbsoluteVolumeSupported");
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled()) {
                return service.isAvrcpAbsoluteVolumeSupported();
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return false;
        } catch (RemoteException e) {
            Log.e(str, "Error talking to BT service in isAvrcpAbsoluteVolumeSupported()", e);
            return false;
        }
    }

    public void setAvrcpAbsoluteVolume(int volume) {
        String str = TAG;
        Log.d(str, "setAvrcpAbsoluteVolume");
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled()) {
                service.setAvrcpAbsoluteVolume(volume);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
        } catch (RemoteException e) {
            Log.e(str, "Error talking to BT service in setAvrcpAbsoluteVolume()", e);
        }
    }

    public boolean isA2dpPlaying(BluetoothDevice device) {
        String str = TAG;
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled() && isValidDevice(device)) {
                return service.isA2dpPlaying(device);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return false;
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stack:");
            stringBuilder.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder.toString());
            return false;
        }
    }

    public boolean shouldSendVolumeKeys(BluetoothDevice device) {
        if (isEnabled() && isValidDevice(device)) {
            ParcelUuid[] uuids = device.getUuids();
            if (uuids == null) {
                return false;
            }
            for (ParcelUuid uuid : uuids) {
                if (BluetoothUuid.isAvrcpTarget(uuid)) {
                    return true;
                }
            }
        }
        return false;
    }

    @UnsupportedAppUsage
    public BluetoothCodecStatus getCodecStatus(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getCodecStatus(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled()) {
                return service.getCodecStatus(device);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return null;
        } catch (RemoteException e) {
            Log.e(str, "Error talking to BT service in getCodecStatus()", e);
            return null;
        }
    }

    @UnsupportedAppUsage
    public void setCodecConfigPreference(BluetoothDevice device, BluetoothCodecConfig codecConfig) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setCodecConfigPreference(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled()) {
                service.setCodecConfigPreference(device, codecConfig);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
        } catch (RemoteException e) {
            Log.e(str, "Error talking to BT service in setCodecConfigPreference()", e);
        }
    }

    @UnsupportedAppUsage
    public void enableOptionalCodecs(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("enableOptionalCodecs(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        Log.d(TAG, stringBuilder.toString());
        enableDisableOptionalCodecs(device, true);
    }

    @UnsupportedAppUsage
    public void disableOptionalCodecs(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("disableOptionalCodecs(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        Log.d(TAG, stringBuilder.toString());
        enableDisableOptionalCodecs(device, false);
    }

    private void enableDisableOptionalCodecs(BluetoothDevice device, boolean enable) {
        String str = TAG;
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled()) {
                if (enable) {
                    service.enableOptionalCodecs(device);
                } else {
                    service.disableOptionalCodecs(device);
                }
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
        } catch (RemoteException e) {
            Log.e(str, "Error talking to BT service in enableDisableOptionalCodecs()", e);
        }
    }

    @UnsupportedAppUsage
    public int supportsOptionalCodecs(BluetoothDevice device) {
        String str = TAG;
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled() && isValidDevice(device)) {
                return service.supportsOptionalCodecs(device);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return -1;
        } catch (RemoteException e) {
            Log.e(str, "Error talking to BT service in getSupportsOptionalCodecs()", e);
            return -1;
        }
    }

    @UnsupportedAppUsage
    public int getOptionalCodecsEnabled(BluetoothDevice device) {
        String str = TAG;
        try {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled() && isValidDevice(device)) {
                return service.getOptionalCodecsEnabled(device);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return -1;
        } catch (RemoteException e) {
            Log.e(str, "Error talking to BT service in getSupportsOptionalCodecs()", e);
            return -1;
        }
    }

    @UnsupportedAppUsage
    public void setOptionalCodecsEnabled(BluetoothDevice device, int value) {
        String str = TAG;
        if (value == -1 || value == 0 || value == 1) {
            IBluetoothA2dp service = getService();
            if (service != null && isEnabled() && isValidDevice(device)) {
                service.setOptionalCodecsEnabled(device, value);
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return;
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid value passed to setOptionalCodecsEnabled: ");
            stringBuilder.append(value);
            Log.e(str, stringBuilder.toString());
        } catch (RemoteException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Stack:");
            stringBuilder2.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder2.toString());
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static String stateToString(int state) {
        if (state == 0) {
            return ImsConferenceState.STATUS_DISCONNECTED;
        }
        if (state == 1) {
            return "connecting";
        }
        if (state == 2) {
            return "connected";
        }
        if (state == 3) {
            return ImsConferenceState.STATUS_DISCONNECTING;
        }
        if (state == 10) {
            return "playing";
        }
        if (state == 11) {
            return "not playing";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<unknown state ");
        stringBuilder.append(state);
        stringBuilder.append(">");
        return stringBuilder.toString();
    }

    private boolean isEnabled() {
        if (this.mAdapter.getState() == 12) {
            return true;
        }
        return false;
    }

    private boolean isValidDevice(BluetoothDevice device) {
        if (device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress())) {
            return true;
        }
        return false;
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
}
