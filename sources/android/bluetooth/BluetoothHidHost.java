package android.bluetooth;

import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothHidHost.Stub;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothHidHost implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.input.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_HANDSHAKE = "android.bluetooth.input.profile.action.HANDSHAKE";
    public static final String ACTION_IDLE_TIME_CHANGED = "android.bluetooth.input.profile.action.IDLE_TIME_CHANGED";
    public static final String ACTION_PROTOCOL_MODE_CHANGED = "android.bluetooth.input.profile.action.PROTOCOL_MODE_CHANGED";
    public static final String ACTION_REPORT = "android.bluetooth.input.profile.action.REPORT";
    public static final String ACTION_VIRTUAL_UNPLUG_STATUS = "android.bluetooth.input.profile.action.VIRTUAL_UNPLUG_STATUS";
    private static final boolean DBG = true;
    public static final String EXTRA_IDLE_TIME = "android.bluetooth.BluetoothHidHost.extra.IDLE_TIME";
    public static final String EXTRA_PROTOCOL_MODE = "android.bluetooth.BluetoothHidHost.extra.PROTOCOL_MODE";
    public static final String EXTRA_REPORT = "android.bluetooth.BluetoothHidHost.extra.REPORT";
    public static final String EXTRA_REPORT_BUFFER_SIZE = "android.bluetooth.BluetoothHidHost.extra.REPORT_BUFFER_SIZE";
    public static final String EXTRA_REPORT_ID = "android.bluetooth.BluetoothHidHost.extra.REPORT_ID";
    public static final String EXTRA_REPORT_TYPE = "android.bluetooth.BluetoothHidHost.extra.REPORT_TYPE";
    public static final String EXTRA_STATUS = "android.bluetooth.BluetoothHidHost.extra.STATUS";
    public static final String EXTRA_VIRTUAL_UNPLUG_STATUS = "android.bluetooth.BluetoothHidHost.extra.VIRTUAL_UNPLUG_STATUS";
    public static final int INPUT_CONNECT_FAILED_ALREADY_CONNECTED = 5001;
    public static final int INPUT_CONNECT_FAILED_ATTEMPT_FAILED = 5002;
    public static final int INPUT_DISCONNECT_FAILED_NOT_CONNECTED = 5000;
    public static final int INPUT_OPERATION_GENERIC_FAILURE = 5003;
    public static final int INPUT_OPERATION_SUCCESS = 5004;
    public static final int PROTOCOL_BOOT_MODE = 1;
    public static final int PROTOCOL_REPORT_MODE = 0;
    public static final int PROTOCOL_UNSUPPORTED_MODE = 255;
    public static final byte REPORT_TYPE_FEATURE = (byte) 3;
    public static final byte REPORT_TYPE_INPUT = (byte) 1;
    public static final byte REPORT_TYPE_OUTPUT = (byte) 2;
    private static final String TAG = "BluetoothHidHost";
    private static final boolean VDBG = false;
    public static final int VIRTUAL_UNPLUG_STATUS_FAIL = 1;
    public static final int VIRTUAL_UNPLUG_STATUS_SUCCESS = 0;
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private final BluetoothProfileConnector<IBluetoothHidHost> mProfileConnector = new BluetoothProfileConnector(this, 4, TAG, IBluetoothHidHost.class.getName()) {
        public IBluetoothHidHost getServiceInterface(IBinder service) {
            return Stub.asInterface(Binder.allowBlocking(service));
        }
    };

    BluetoothHidHost(Context context, ServiceListener listener) {
        this.mProfileConnector.connect(context, listener);
    }

    /* Access modifiers changed, original: 0000 */
    public void close() {
        this.mProfileConnector.disconnect();
    }

    private IBluetoothHidHost getService() {
        return (IBluetoothHidHost) this.mProfileConnector.getService();
    }

    public boolean connect(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("connect(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHidHost service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.connect(device);
                return str;
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
        IBluetoothHidHost service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.disconnect(device);
                return str;
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
        IBluetoothHidHost service = getService();
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
        IBluetoothHidHost service = getService();
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
        IBluetoothHidHost service = getService();
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

    public boolean setPriority(BluetoothDevice device, int priority) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setPriority(");
        stringBuilder.append(device);
        stringBuilder.append(", ");
        stringBuilder.append(priority);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHidHost service = getService();
        String str = TAG;
        if (service == null || !isEnabled() || !isValidDevice(device)) {
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return false;
        } else if (priority != 0 && priority != 100) {
            return false;
        } else {
            try {
                str = service.setPriority(device, priority);
                return str;
            } catch (RemoteException e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Stack:");
                stringBuilder2.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder2.toString());
                return false;
            }
        }
    }

    public int getPriority(BluetoothDevice device) {
        IBluetoothHidHost service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getPriority(device);
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

    private boolean isEnabled() {
        return this.mAdapter.getState() == 12;
    }

    private static boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }

    public boolean virtualUnplug(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("virtualUnplug(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHidHost service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.virtualUnplug(device);
                return str;
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

    public boolean getProtocolMode(BluetoothDevice device) {
        IBluetoothHidHost service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getProtocolMode(device);
                return str;
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Stack:");
                stringBuilder.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder.toString());
                return false;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public boolean setProtocolMode(BluetoothDevice device, int protocolMode) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setProtocolMode(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHidHost service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.setProtocolMode(device, protocolMode);
                return str;
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

    public boolean getReport(BluetoothDevice device, byte reportType, byte reportId, int bufferSize) {
        IBluetoothHidHost service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getReport(device, reportType, reportId, bufferSize);
                return str;
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Stack:");
                stringBuilder.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder.toString());
                return false;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public boolean setReport(BluetoothDevice device, byte reportType, String report) {
        IBluetoothHidHost service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.setReport(device, reportType, report);
                return str;
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Stack:");
                stringBuilder.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder.toString());
                return false;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public boolean sendData(BluetoothDevice device, String report) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("sendData(");
        stringBuilder.append(device);
        stringBuilder.append("), report=");
        stringBuilder.append(report);
        log(stringBuilder.toString());
        IBluetoothHidHost service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.sendData(device, report);
                return str;
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

    public boolean getIdleTime(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getIdletime(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHidHost service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getIdleTime(device);
                return str;
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

    public boolean setIdleTime(BluetoothDevice device, byte idleTime) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setIdletime(");
        stringBuilder.append(device);
        stringBuilder.append("), idleTime=");
        stringBuilder.append(idleTime);
        log(stringBuilder.toString());
        IBluetoothHidHost service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.setIdleTime(device, idleTime);
                return str;
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

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
}
