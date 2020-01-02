package android.bluetooth;

import android.annotation.UnsupportedAppUsage;
import android.bluetooth.IBluetoothStateChangeCallback.Stub;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BluetoothPbap implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.pbap.profile.action.CONNECTION_STATE_CHANGED";
    private static final boolean DBG = false;
    public static final int RESULT_CANCELED = 2;
    public static final int RESULT_FAILURE = 0;
    public static final int RESULT_SUCCESS = 1;
    private static final String TAG = "BluetoothPbap";
    private BluetoothAdapter mAdapter;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new Stub() {
        public void onBluetoothStateChange(boolean up) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onBluetoothStateChange: up=");
            stringBuilder.append(up);
            BluetoothPbap.log(stringBuilder.toString());
            if (up) {
                BluetoothPbap.this.doBind();
            } else {
                BluetoothPbap.this.doUnbind();
            }
        }
    };
    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            BluetoothPbap.log("Proxy object connected");
            BluetoothPbap.this.mService = IBluetoothPbap.Stub.asInterface(service);
            if (BluetoothPbap.this.mServiceListener != null) {
                BluetoothPbap.this.mServiceListener.onServiceConnected(BluetoothPbap.this);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            BluetoothPbap.log("Proxy object disconnected");
            BluetoothPbap.this.doUnbind();
            if (BluetoothPbap.this.mServiceListener != null) {
                BluetoothPbap.this.mServiceListener.onServiceDisconnected();
            }
        }
    };
    private final Context mContext;
    private volatile IBluetoothPbap mService;
    private ServiceListener mServiceListener;

    public interface ServiceListener {
        void onServiceConnected(BluetoothPbap bluetoothPbap);

        void onServiceDisconnected();
    }

    public BluetoothPbap(Context context, ServiceListener l) {
        this.mContext = context;
        this.mServiceListener = l;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (RemoteException re) {
                Log.e(TAG, "", re);
            }
        }
        doBind();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean doBind() {
        synchronized (this.mConnection) {
            try {
                if (this.mService == null) {
                    log("Binding service...");
                    Intent intent = new Intent(IBluetoothPbap.class.getName());
                    ComponentName comp = intent.resolveSystemService(this.mContext.getPackageManager(), 0);
                    intent.setComponent(comp);
                    if (comp == null || !this.mContext.bindServiceAsUser(intent, this.mConnection, 0, UserHandle.CURRENT_OR_SELF)) {
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Could not bind to Bluetooth Pbap Service with ");
                        stringBuilder.append(intent);
                        Log.e(str, stringBuilder.toString());
                        return false;
                    }
                }
                return true;
            } catch (SecurityException se) {
                Log.e(TAG, "", se);
                return false;
            } catch (Throwable th) {
            }
        }
    }

    private void doUnbind() {
        synchronized (this.mConnection) {
            if (this.mService != null) {
                log("Unbinding service...");
                try {
                    this.mContext.unbindService(this.mConnection);
                    this.mService = null;
                } catch (IllegalArgumentException ie) {
                    try {
                        Log.e(TAG, "", ie);
                    } finally {
                        this.mService = null;
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    public synchronized void close() {
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (RemoteException re) {
                Log.e(TAG, "", re);
            }
        }
        doUnbind();
        this.mServiceListener = null;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        log("getConnectedDevices()");
        IBluetoothPbap service = this.mService;
        String str = TAG;
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
            return new ArrayList();
        }
        try {
            str = service.getConnectedDevices();
            return str;
        } catch (RemoteException e) {
            Log.e(str, e.toString());
            return new ArrayList();
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getConnectionState: device=");
        stringBuilder.append(device);
        log(stringBuilder.toString());
        IBluetoothPbap service = this.mService;
        int i = 0;
        String str = TAG;
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
            return 0;
        }
        try {
            i = service.getConnectionState(device);
            return i;
        } catch (RemoteException e) {
            Log.e(str, e.toString());
            return i;
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getDevicesMatchingConnectionStates: states=");
        stringBuilder.append(Arrays.toString(states));
        log(stringBuilder.toString());
        IBluetoothPbap service = this.mService;
        String str = TAG;
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
            return new ArrayList();
        }
        try {
            str = service.getDevicesMatchingConnectionStates(states);
            return str;
        } catch (RemoteException e) {
            Log.e(str, e.toString());
            return new ArrayList();
        }
    }

    public boolean isConnected(BluetoothDevice device) {
        return getConnectionState(device) == 2;
    }

    @UnsupportedAppUsage
    public boolean disconnect(BluetoothDevice device) {
        log("disconnect()");
        IBluetoothPbap service = this.mService;
        String str = TAG;
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
            return false;
        }
        try {
            service.disconnect(device);
            return true;
        } catch (RemoteException e) {
            Log.e(str, e.toString());
            return false;
        }
    }

    private static void log(String msg) {
    }
}
