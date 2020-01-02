package android.bluetooth;

import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothDun.Stub;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothDun implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "codeaurora.bluetooth.dun.profile.action.CONNECTION_STATE_CHANGED";
    private static final boolean DBG = false;
    private static final String TAG = "BluetoothDun";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            BluetoothDun.this.mDunService = Stub.asInterface(service);
            if (BluetoothDun.this.mServiceListener != null) {
                BluetoothDun.this.mServiceListener.onServiceConnected(22, BluetoothDun.this);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            BluetoothDun.this.mDunService = null;
            if (BluetoothDun.this.mServiceListener != null) {
                BluetoothDun.this.mServiceListener.onServiceDisconnected(22);
            }
        }
    };
    private Context mContext;
    private IBluetoothDun mDunService;
    private ServiceListener mServiceListener;
    private IBluetoothStateChangeCallback mStateChangeCallback = new IBluetoothStateChangeCallback.Stub() {
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        public void onBluetoothStateChange(boolean r5) {
            /*
            r4 = this;
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r1 = "onBluetoothStateChange on: ";
            r0.append(r1);
            r0.append(r5);
            r0 = r0.toString();
            r1 = "BluetoothDun";
            android.util.Log.d(r1, r0);
            if (r5 == 0) goto L_0x0043;
        L_0x0019:
            r0 = android.bluetooth.BluetoothDun.this;	 Catch:{ IllegalStateException -> 0x0039, SecurityException -> 0x002f }
            r0 = r0.mDunService;	 Catch:{ IllegalStateException -> 0x0039, SecurityException -> 0x002f }
            if (r0 != 0) goto L_0x0042;
        L_0x0021:
            r0 = "BluetoothDun";
            r1 = "onBluetoothStateChange call bindService";
            android.util.Log.d(r0, r1);	 Catch:{ IllegalStateException -> 0x0039, SecurityException -> 0x002f }
            r0 = android.bluetooth.BluetoothDun.this;	 Catch:{ IllegalStateException -> 0x0039, SecurityException -> 0x002f }
            r0.doBind();	 Catch:{ IllegalStateException -> 0x0039, SecurityException -> 0x002f }
            goto L_0x0042;
        L_0x002f:
            r0 = move-exception;
            r1 = "BluetoothDun";
            r2 = "onBluetoothStateChange: could not bind to DUN service: ";
            android.util.Log.e(r1, r2, r0);
            goto L_0x0042;
        L_0x0039:
            r0 = move-exception;
            r1 = "BluetoothDun";
            r2 = "onBluetoothStateChange: could not bind to DUN service: ";
            android.util.Log.e(r1, r2, r0);
        L_0x0042:
            goto L_0x0071;
        L_0x0043:
            r0 = android.bluetooth.BluetoothDun.this;
            r0 = r0.mConnection;
            monitor-enter(r0);
            r1 = android.bluetooth.BluetoothDun.this;	 Catch:{ all -> 0x0072 }
            r1 = r1.mDunService;	 Catch:{ all -> 0x0072 }
            if (r1 == 0) goto L_0x0070;
        L_0x0052:
            r1 = android.bluetooth.BluetoothDun.this;	 Catch:{ Exception -> 0x0068 }
            r2 = 0;
            r1.mDunService = r2;	 Catch:{ Exception -> 0x0068 }
            r1 = android.bluetooth.BluetoothDun.this;	 Catch:{ Exception -> 0x0068 }
            r1 = r1.mContext;	 Catch:{ Exception -> 0x0068 }
            r2 = android.bluetooth.BluetoothDun.this;	 Catch:{ Exception -> 0x0068 }
            r2 = r2.mConnection;	 Catch:{ Exception -> 0x0068 }
            r1.unbindService(r2);	 Catch:{ Exception -> 0x0068 }
            goto L_0x0070;
        L_0x0068:
            r1 = move-exception;
            r2 = "BluetoothDun";
            r3 = "";
            android.util.Log.e(r2, r3, r1);	 Catch:{ all -> 0x0072 }
        L_0x0070:
            monitor-exit(r0);	 Catch:{ all -> 0x0072 }
        L_0x0071:
            return;
        L_0x0072:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0072 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.BluetoothDun$AnonymousClass1.onBluetoothStateChange(boolean):void");
        }
    };

    BluetoothDun(Context context, ServiceListener l) {
        String str = TAG;
        this.mContext = context;
        this.mServiceListener = l;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            this.mAdapter.getBluetoothManager().registerStateChangeCallback(this.mStateChangeCallback);
        } catch (RemoteException re) {
            Log.w(str, "Unable to register BluetoothStateChangeCallback", re);
        }
        Log.d(str, "BluetoothDun() call bindService");
        doBind();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean doBind() {
        Intent intent = new Intent(IBluetoothDun.class.getName());
        ComponentName comp = intent.resolveSystemService(this.mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if (comp != null && this.mContext.bindServiceAsUser(intent, this.mConnection, 0, Process.myUserHandle())) {
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Could not bind to Bluetooth Dun Service with ");
        stringBuilder.append(intent);
        Log.e(TAG, stringBuilder.toString());
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public void close() {
        this.mServiceListener = null;
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(this.mStateChangeCallback);
            } catch (RemoteException re) {
                Log.w(TAG, "Unable to unregister BluetoothStateChangeCallback", re);
            }
        }
        synchronized (this.mConnection) {
            if (this.mDunService != null) {
                try {
                    this.mDunService = null;
                    this.mContext.unbindService(this.mConnection);
                } catch (Exception re2) {
                    Log.e(TAG, "", re2);
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        close();
    }

    public boolean disconnect(BluetoothDevice device) {
        IBluetoothDun iBluetoothDun = this.mDunService;
        String str = TAG;
        if (iBluetoothDun != null && isEnabled() && isValidDevice(device)) {
            try {
                return this.mDunService.disconnect(device);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Stack:");
                stringBuilder.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder.toString());
                return false;
            }
        }
        if (this.mDunService == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        IBluetoothDun iBluetoothDun = this.mDunService;
        String str = TAG;
        if (iBluetoothDun == null || !isEnabled()) {
            if (this.mDunService == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            return this.mDunService.getConnectedDevices();
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stack:");
            stringBuilder.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder.toString());
            return new ArrayList();
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        IBluetoothDun iBluetoothDun = this.mDunService;
        String str = TAG;
        if (iBluetoothDun == null || !isEnabled()) {
            if (this.mDunService == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            return this.mDunService.getDevicesMatchingConnectionStates(states);
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stack:");
            stringBuilder.append(Log.getStackTraceString(new Throwable()));
            Log.e(str, stringBuilder.toString());
            return new ArrayList();
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        IBluetoothDun iBluetoothDun = this.mDunService;
        String str = TAG;
        if (iBluetoothDun != null && isEnabled() && isValidDevice(device)) {
            try {
                return this.mDunService.getConnectionState(device);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Stack:");
                stringBuilder.append(Log.getStackTraceString(new Throwable()));
                Log.e(str, stringBuilder.toString());
                return 0;
            }
        }
        if (this.mDunService == null) {
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
