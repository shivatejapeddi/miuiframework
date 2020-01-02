package android.bluetooth;

import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothMapClient.Stub;
import android.content.Context;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothMapClient implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.mapmce.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_MESSAGE_DELIVERED_SUCCESSFULLY = "android.bluetooth.mapmce.profile.action.MESSAGE_DELIVERED_SUCCESSFULLY";
    public static final String ACTION_MESSAGE_RECEIVED = "android.bluetooth.mapmce.profile.action.MESSAGE_RECEIVED";
    public static final String ACTION_MESSAGE_SENT_SUCCESSFULLY = "android.bluetooth.mapmce.profile.action.MESSAGE_SENT_SUCCESSFULLY";
    private static final boolean DBG;
    public static final String EXTRA_MESSAGE_HANDLE = "android.bluetooth.mapmce.profile.extra.MESSAGE_HANDLE";
    public static final String EXTRA_MESSAGE_READ_STATUS = "android.bluetooth.mapmce.profile.extra.MESSAGE_READ_STATUS";
    public static final String EXTRA_MESSAGE_TIMESTAMP = "android.bluetooth.mapmce.profile.extra.MESSAGE_TIMESTAMP";
    public static final String EXTRA_SENDER_CONTACT_NAME = "android.bluetooth.mapmce.profile.extra.SENDER_CONTACT_NAME";
    public static final String EXTRA_SENDER_CONTACT_URI = "android.bluetooth.mapmce.profile.extra.SENDER_CONTACT_URI";
    public static final int RESULT_CANCELED = 2;
    public static final int RESULT_FAILURE = 0;
    public static final int RESULT_SUCCESS = 1;
    public static final int STATE_ERROR = -1;
    private static final String TAG = "BluetoothMapClient";
    private static final int UPLOADING_FEATURE_BITMASK = 8;
    private static final boolean VDBG;
    private BluetoothAdapter mAdapter;
    private final BluetoothProfileConnector<IBluetoothMapClient> mProfileConnector = new BluetoothProfileConnector(this, 18, TAG, IBluetoothMapClient.class.getName()) {
        public IBluetoothMapClient getServiceInterface(IBinder service) {
            return Stub.asInterface(Binder.allowBlocking(service));
        }
    };

    static {
        String str = TAG;
        DBG = Log.isLoggable(str, 3);
        VDBG = Log.isLoggable(str, 2);
    }

    BluetoothMapClient(Context context, ServiceListener listener) {
        if (DBG) {
            Log.d(TAG, "Create BluetoothMapClient proxy object");
        }
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mProfileConnector.connect(context, listener);
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    public void close() {
        this.mProfileConnector.disconnect();
    }

    private IBluetoothMapClient getService() {
        return (IBluetoothMapClient) this.mProfileConnector.getService();
    }

    public boolean isConnected(BluetoothDevice device) {
        boolean z = VDBG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("isConnected(");
            stringBuilder.append(device);
            stringBuilder.append(")");
            Log.d(str, stringBuilder.toString());
        }
        IBluetoothMapClient service = getService();
        if (service != null) {
            try {
                str = service.isConnected(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        } else {
            Log.w(str, "Proxy not attached to service");
            if (DBG) {
                Log.d(str, Log.getStackTraceString(new Throwable()));
            }
            return false;
        }
    }

    public boolean connect(BluetoothDevice device) {
        boolean z = DBG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("connect(");
            stringBuilder.append(device);
            stringBuilder.append(")for MAPS MCE");
            Log.d(str, stringBuilder.toString());
        }
        IBluetoothMapClient service = getService();
        if (service != null) {
            try {
                str = service.connect(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        } else {
            Log.w(str, "Proxy not attached to service");
            if (DBG) {
                Log.d(str, Log.getStackTraceString(new Throwable()));
            }
            return false;
        }
    }

    public boolean disconnect(BluetoothDevice device) {
        boolean z = DBG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("disconnect(");
            stringBuilder.append(device);
            stringBuilder.append(")");
            Log.d(str, stringBuilder.toString());
        }
        IBluetoothMapClient service = getService();
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.disconnect(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        boolean z = DBG;
        String str = TAG;
        if (z) {
            Log.d(str, "getConnectedDevices()");
        }
        IBluetoothMapClient service = getService();
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
            Log.e(str, Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        boolean z = DBG;
        String str = TAG;
        if (z) {
            Log.d(str, "getDevicesMatchingStates()");
        }
        IBluetoothMapClient service = getService();
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
            Log.e(str, Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        boolean z = DBG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getConnectionState(");
            stringBuilder.append(device);
            stringBuilder.append(")");
            Log.d(str, stringBuilder.toString());
        }
        IBluetoothMapClient service = getService();
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getConnectionState(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return 0;
    }

    public boolean setPriority(BluetoothDevice device, int priority) {
        boolean z = DBG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setPriority(");
            stringBuilder.append(device);
            stringBuilder.append(", ");
            stringBuilder.append(priority);
            stringBuilder.append(")");
            Log.d(str, stringBuilder.toString());
        }
        IBluetoothMapClient service = getService();
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
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
    }

    public int getPriority(BluetoothDevice device) {
        boolean z = VDBG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getPriority(");
            stringBuilder.append(device);
            stringBuilder.append(")");
            Log.d(str, stringBuilder.toString());
        }
        IBluetoothMapClient service = getService();
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getPriority(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return 0;
    }

    @UnsupportedAppUsage
    public boolean sendMessage(BluetoothDevice device, Uri[] contacts, String message, PendingIntent sentIntent, PendingIntent deliveredIntent) {
        boolean z = DBG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("sendMessage(");
            stringBuilder.append(device);
            String str2 = ", ";
            stringBuilder.append(str2);
            stringBuilder.append(contacts);
            stringBuilder.append(str2);
            stringBuilder.append(message);
            Log.d(str, stringBuilder.toString());
        }
        IBluetoothMapClient service = getService();
        if (service == null || !isEnabled() || !isValidDevice(device)) {
            return false;
        }
        try {
            str = service.sendMessage(device, contacts, message, sentIntent, deliveredIntent);
            return str;
        } catch (RemoteException e) {
            Log.e(str, Log.getStackTraceString(new Throwable()));
            return false;
        }
    }

    public boolean getUnreadMessages(BluetoothDevice device) {
        boolean z = DBG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getUnreadMessages(");
            stringBuilder.append(device);
            stringBuilder.append(")");
            Log.d(str, stringBuilder.toString());
        }
        IBluetoothMapClient service = getService();
        if (service == null || !isEnabled() || !isValidDevice(device)) {
            return false;
        }
        try {
            str = service.getUnreadMessages(device);
            return str;
        } catch (RemoteException e) {
            Log.e(str, Log.getStackTraceString(new Throwable()));
            return false;
        }
    }

    public boolean isUploadingSupported(BluetoothDevice device) {
        IBluetoothMapClient service = getService();
        boolean z = false;
        if (service != null) {
            try {
                if (isEnabled() && isValidDevice(device) && (service.getSupportedFeatures(device) & 8) > 0) {
                    z = true;
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }
        return z;
    }

    private boolean isEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.getState() == 12) {
            return true;
        }
        if (DBG) {
            Log.d(TAG, "Bluetooth is Not enabled");
        }
        return false;
    }

    private static boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }
}
