package android.bluetooth;

import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothHidDevice.Stub;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public final class BluetoothHidDevice implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.hiddevice.profile.action.CONNECTION_STATE_CHANGED";
    public static final byte ERROR_RSP_INVALID_PARAM = (byte) 4;
    public static final byte ERROR_RSP_INVALID_RPT_ID = (byte) 2;
    public static final byte ERROR_RSP_NOT_READY = (byte) 1;
    public static final byte ERROR_RSP_SUCCESS = (byte) 0;
    public static final byte ERROR_RSP_UNKNOWN = (byte) 14;
    public static final byte ERROR_RSP_UNSUPPORTED_REQ = (byte) 3;
    public static final byte PROTOCOL_BOOT_MODE = (byte) 0;
    public static final byte PROTOCOL_REPORT_MODE = (byte) 1;
    public static final byte REPORT_TYPE_FEATURE = (byte) 3;
    public static final byte REPORT_TYPE_INPUT = (byte) 1;
    public static final byte REPORT_TYPE_OUTPUT = (byte) 2;
    public static final byte SUBCLASS1_COMBO = (byte) -64;
    public static final byte SUBCLASS1_KEYBOARD = (byte) 64;
    public static final byte SUBCLASS1_MOUSE = Byte.MIN_VALUE;
    public static final byte SUBCLASS1_NONE = (byte) 0;
    public static final byte SUBCLASS2_CARD_READER = (byte) 6;
    public static final byte SUBCLASS2_DIGITIZER_TABLET = (byte) 5;
    public static final byte SUBCLASS2_GAMEPAD = (byte) 2;
    public static final byte SUBCLASS2_JOYSTICK = (byte) 1;
    public static final byte SUBCLASS2_REMOTE_CONTROL = (byte) 3;
    public static final byte SUBCLASS2_SENSING_DEVICE = (byte) 4;
    public static final byte SUBCLASS2_UNCATEGORIZED = (byte) 0;
    private static final String TAG = BluetoothHidDevice.class.getSimpleName();
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private final BluetoothProfileConnector<IBluetoothHidDevice> mProfileConnector = new BluetoothProfileConnector(this, 19, "BluetoothHidDevice", IBluetoothHidDevice.class.getName()) {
        public IBluetoothHidDevice getServiceInterface(IBinder service) {
            return Stub.asInterface(Binder.allowBlocking(service));
        }
    };

    public static abstract class Callback {
        private static final String TAG = "BluetoothHidDevCallback";

        public void onAppStatusChanged(BluetoothDevice pluggedDevice, boolean registered) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onAppStatusChanged: pluggedDevice=");
            stringBuilder.append(pluggedDevice);
            stringBuilder.append(" registered=");
            stringBuilder.append(registered);
            Log.d(TAG, stringBuilder.toString());
        }

        public void onConnectionStateChanged(BluetoothDevice device, int state) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onConnectionStateChanged: device=");
            stringBuilder.append(device);
            stringBuilder.append(" state=");
            stringBuilder.append(state);
            Log.d(TAG, stringBuilder.toString());
        }

        public void onGetReport(BluetoothDevice device, byte type, byte id, int bufferSize) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onGetReport: device=");
            stringBuilder.append(device);
            stringBuilder.append(" type=");
            stringBuilder.append(type);
            stringBuilder.append(" id=");
            stringBuilder.append(id);
            stringBuilder.append(" bufferSize=");
            stringBuilder.append(bufferSize);
            Log.d(TAG, stringBuilder.toString());
        }

        public void onSetReport(BluetoothDevice device, byte type, byte id, byte[] data) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onSetReport: device=");
            stringBuilder.append(device);
            stringBuilder.append(" type=");
            stringBuilder.append(type);
            stringBuilder.append(" id=");
            stringBuilder.append(id);
            Log.d(TAG, stringBuilder.toString());
        }

        public void onSetProtocol(BluetoothDevice device, byte protocol) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onSetProtocol: device=");
            stringBuilder.append(device);
            stringBuilder.append(" protocol=");
            stringBuilder.append(protocol);
            Log.d(TAG, stringBuilder.toString());
        }

        public void onInterruptData(BluetoothDevice device, byte reportId, byte[] data) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onInterruptData: device=");
            stringBuilder.append(device);
            stringBuilder.append(" reportId=");
            stringBuilder.append(reportId);
            Log.d(TAG, stringBuilder.toString());
        }

        public void onVirtualCableUnplug(BluetoothDevice device) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onVirtualCableUnplug: device=");
            stringBuilder.append(device);
            Log.d(TAG, stringBuilder.toString());
        }
    }

    private static class CallbackWrapper extends IBluetoothHidDeviceCallback.Stub {
        private final Callback mCallback;
        private final Executor mExecutor;

        CallbackWrapper(Executor executor, Callback callback) {
            this.mExecutor = executor;
            this.mCallback = callback;
        }

        public void onAppStatusChanged(BluetoothDevice pluggedDevice, boolean registered) {
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new -$$Lambda$BluetoothHidDevice$CallbackWrapper$NFluHjT4zTfYBRXClu_2k6mPKFI(this, pluggedDevice, registered));
        }

        public /* synthetic */ void lambda$onAppStatusChanged$0$BluetoothHidDevice$CallbackWrapper(BluetoothDevice pluggedDevice, boolean registered) {
            this.mCallback.onAppStatusChanged(pluggedDevice, registered);
        }

        public void onConnectionStateChanged(BluetoothDevice device, int state) {
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new -$$Lambda$BluetoothHidDevice$CallbackWrapper$qtStwQVkGfOs2iJIiePWqJJpi0w(this, device, state));
        }

        public /* synthetic */ void lambda$onConnectionStateChanged$1$BluetoothHidDevice$CallbackWrapper(BluetoothDevice device, int state) {
            this.mCallback.onConnectionStateChanged(device, state);
        }

        public void onGetReport(BluetoothDevice device, byte type, byte id, int bufferSize) {
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new -$$Lambda$BluetoothHidDevice$CallbackWrapper$Eyz_qG6mvTlh6a8Bp41ZoEJzQCQ(this, device, type, id, bufferSize));
        }

        public /* synthetic */ void lambda$onGetReport$2$BluetoothHidDevice$CallbackWrapper(BluetoothDevice device, byte type, byte id, int bufferSize) {
            this.mCallback.onGetReport(device, type, id, bufferSize);
        }

        public void onSetReport(BluetoothDevice device, byte type, byte id, byte[] data) {
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new -$$Lambda$BluetoothHidDevice$CallbackWrapper$3bTGVlfKj7Y0SZdifW_Ya2myDKs(this, device, type, id, data));
        }

        public /* synthetic */ void lambda$onSetReport$3$BluetoothHidDevice$CallbackWrapper(BluetoothDevice device, byte type, byte id, byte[] data) {
            this.mCallback.onSetReport(device, type, id, data);
        }

        public void onSetProtocol(BluetoothDevice device, byte protocol) {
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new -$$Lambda$BluetoothHidDevice$CallbackWrapper$ypkr5GGxsAkGSBiLjIRwg-PzqCM(this, device, protocol));
        }

        public /* synthetic */ void lambda$onSetProtocol$4$BluetoothHidDevice$CallbackWrapper(BluetoothDevice device, byte protocol) {
            this.mCallback.onSetProtocol(device, protocol);
        }

        public void onInterruptData(BluetoothDevice device, byte reportId, byte[] data) {
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new -$$Lambda$BluetoothHidDevice$CallbackWrapper$xW99-tc95OmGApoKnpQ9q1TXb9k(this, device, reportId, data));
        }

        public /* synthetic */ void lambda$onInterruptData$5$BluetoothHidDevice$CallbackWrapper(BluetoothDevice device, byte reportId, byte[] data) {
            this.mCallback.onInterruptData(device, reportId, data);
        }

        public void onVirtualCableUnplug(BluetoothDevice device) {
            Binder.clearCallingIdentity();
            this.mExecutor.execute(new -$$Lambda$BluetoothHidDevice$CallbackWrapper$jiodzbAJAcleQCwlDcBjvDddELM(this, device));
        }

        public /* synthetic */ void lambda$onVirtualCableUnplug$6$BluetoothHidDevice$CallbackWrapper(BluetoothDevice device) {
            this.mCallback.onVirtualCableUnplug(device);
        }
    }

    BluetoothHidDevice(Context context, ServiceListener listener) {
        this.mProfileConnector.connect(context, listener);
    }

    /* Access modifiers changed, original: 0000 */
    public void close() {
        this.mProfileConnector.disconnect();
    }

    private IBluetoothHidDevice getService() {
        return (IBluetoothHidDevice) this.mProfileConnector.getService();
    }

    public List<BluetoothDevice> getConnectedDevices() {
        IBluetoothHidDevice service = getService();
        if (service != null) {
            try {
                return service.getConnectedDevices();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            return new ArrayList();
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        IBluetoothHidDevice service = getService();
        if (service != null) {
            try {
                return service.getDevicesMatchingConnectionStates(states);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            return new ArrayList();
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        IBluetoothHidDevice service = getService();
        if (service != null) {
            try {
                return service.getConnectionState(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            return 0;
        }
    }

    public boolean registerApp(BluetoothHidDeviceAppSdpSettings sdp, BluetoothHidDeviceAppQosSettings inQos, BluetoothHidDeviceAppQosSettings outQos, Executor executor, Callback callback) {
        if (sdp == null) {
            throw new IllegalArgumentException("sdp parameter cannot be null");
        } else if (executor == null) {
            throw new IllegalArgumentException("executor parameter cannot be null");
        } else if (callback != null) {
            IBluetoothHidDevice service = getService();
            if (service != null) {
                try {
                    return service.registerApp(sdp, inQos, outQos, new CallbackWrapper(executor, callback));
                } catch (RemoteException e) {
                    Log.e(TAG, e.toString());
                    return false;
                }
            }
            Log.w(TAG, "Proxy not attached to service");
            return false;
        } else {
            throw new IllegalArgumentException("callback parameter cannot be null");
        }
    }

    public boolean unregisterApp() {
        IBluetoothHidDevice service = getService();
        if (service != null) {
            try {
                return service.unregisterApp();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    public boolean sendReport(BluetoothDevice device, int id, byte[] data) {
        IBluetoothHidDevice service = getService();
        if (service != null) {
            try {
                return service.sendReport(device, id, data);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    public boolean replyReport(BluetoothDevice device, byte type, byte id, byte[] data) {
        IBluetoothHidDevice service = getService();
        if (service != null) {
            try {
                return service.replyReport(device, type, id, data);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    public boolean reportError(BluetoothDevice device, byte error) {
        IBluetoothHidDevice service = getService();
        if (service != null) {
            try {
                return service.reportError(device, error);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    public String getUserAppName() {
        IBluetoothHidDevice service = getService();
        if (service != null) {
            try {
                return service.getUserAppName();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            return "";
        }
    }

    public boolean connect(BluetoothDevice device) {
        IBluetoothHidDevice service = getService();
        if (service != null) {
            try {
                return service.connect(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    public boolean disconnect(BluetoothDevice device) {
        IBluetoothHidDevice service = getService();
        if (service != null) {
            try {
                return service.disconnect(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }
}
