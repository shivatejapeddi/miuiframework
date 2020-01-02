package android.bluetooth;

import android.annotation.UnsupportedAppUsage;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothHearingAid.Stub;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.ims.ImsConferenceState;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothHearingAid implements BluetoothProfile {
    @UnsupportedAppUsage
    public static final String ACTION_ACTIVE_DEVICE_CHANGED = "android.bluetooth.hearingaid.profile.action.ACTIVE_DEVICE_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED";
    private static final boolean DBG = true;
    public static final long HI_SYNC_ID_INVALID = 0;
    public static final int MODE_BINAURAL = 1;
    public static final int MODE_MONAURAL = 0;
    public static final int SIDE_LEFT = 0;
    public static final int SIDE_RIGHT = 1;
    private static final String TAG = "BluetoothHearingAid";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private final BluetoothProfileConnector<IBluetoothHearingAid> mProfileConnector = new BluetoothProfileConnector(this, 21, TAG, IBluetoothHearingAid.class.getName()) {
        public IBluetoothHearingAid getServiceInterface(IBinder service) {
            return Stub.asInterface(Binder.allowBlocking(service));
        }
    };

    BluetoothHearingAid(Context context, ServiceListener listener) {
        this.mProfileConnector.connect(context, listener);
    }

    /* Access modifiers changed, original: 0000 */
    public void close() {
        this.mProfileConnector.disconnect();
    }

    private IBluetoothHearingAid getService() {
        return (IBluetoothHearingAid) this.mProfileConnector.getService();
    }

    public boolean connect(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("connect(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.connect(device);
                }
            } catch (RemoteException e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Stack:");
                stringBuilder2.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder2.toString());
                return false;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public boolean disconnect(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("disconnect(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.disconnect(device);
                }
            } catch (RemoteException e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Stack:");
                stringBuilder2.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder2.toString());
                return false;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service != null) {
            try {
                if (isEnabled()) {
                    return service.getConnectedDevices();
                }
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Stack:");
                stringBuilder.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder.toString());
                return new ArrayList();
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return new ArrayList();
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service != null) {
            try {
                if (isEnabled()) {
                    return service.getDevicesMatchingConnectionStates(states);
                }
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Stack:");
                stringBuilder.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder.toString());
                return new ArrayList();
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return new ArrayList();
    }

    public int getConnectionState(BluetoothDevice device) {
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.getConnectionState(device);
                }
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

    @UnsupportedAppUsage
    public boolean setActiveDevice(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setActiveDevice(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service != null) {
            try {
                if (isEnabled() && (device == null || isValidDevice(device))) {
                    service.setActiveDevice(device);
                    return true;
                }
            } catch (RemoteException e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Stack:");
                stringBuilder2.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder2.toString());
                return false;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    @UnsupportedAppUsage
    public List<BluetoothDevice> getActiveDevices() {
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service != null) {
            try {
                if (isEnabled()) {
                    return service.getActiveDevices();
                }
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Stack:");
                stringBuilder.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder.toString());
                return new ArrayList();
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return new ArrayList();
    }

    public boolean setPriority(BluetoothDevice device, int priority) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setPriority(");
        stringBuilder.append(device);
        stringBuilder.append(", ");
        stringBuilder.append(priority);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    if (priority == 0 || priority == 100) {
                        return service.setPriority(device, priority);
                    }
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
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public int getPriority(BluetoothDevice device) {
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.getPriority(device);
                }
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<unknown state ");
        stringBuilder.append(state);
        stringBuilder.append(">");
        return stringBuilder.toString();
    }

    public int getVolume() {
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service != null) {
            try {
                if (isEnabled()) {
                    return service.getVolume();
                }
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

    public void adjustVolume(int direction) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("adjustVolume(");
        stringBuilder.append(direction);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service == null) {
            try {
                Log.w(str, "Proxy not attached to service");
            } catch (RemoteException e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Stack:");
                stringBuilder2.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder2.toString());
            }
        } else if (isEnabled()) {
            service.adjustVolume(direction);
        }
    }

    public void setVolume(int volume) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setVolume(");
        stringBuilder.append(volume);
        stringBuilder.append(")");
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        IBluetoothHearingAid service = getService();
        if (service == null) {
            try {
                Log.w(str, "Proxy not attached to service");
            } catch (RemoteException e) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Stack:");
                stringBuilder3.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder3.toString());
            }
        } else if (isEnabled()) {
            service.setVolume(volume);
        }
    }

    public long getHiSyncId(BluetoothDevice device) {
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service == null) {
            try {
                Log.w(str, "Proxy not attached to service");
                return 0;
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Stack:");
                stringBuilder.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder.toString());
                return 0;
            }
        }
        if (isEnabled()) {
            if (isValidDevice(device)) {
                return service.getHiSyncId(device);
            }
        }
        return 0;
    }

    public int getDeviceSide(BluetoothDevice device) {
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.getDeviceSide(device);
                }
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

    public int getDeviceMode(BluetoothDevice device) {
        IBluetoothHearingAid service = getService();
        String str = TAG;
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device)) {
                    return service.getDeviceMode(device);
                }
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
