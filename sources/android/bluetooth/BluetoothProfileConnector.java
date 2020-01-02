package android.bluetooth;

import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothStateChangeCallback.Stub;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public abstract class BluetoothProfileConnector<T> {
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new Stub() {
        public void onBluetoothStateChange(boolean up) {
            if (up) {
                BluetoothProfileConnector.this.doBind();
            } else {
                BluetoothProfileConnector.this.doUnbind();
            }
        }
    };
    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            BluetoothProfileConnector.this.logDebug("Proxy object connected");
            BluetoothProfileConnector bluetoothProfileConnector = BluetoothProfileConnector.this;
            bluetoothProfileConnector.mService = bluetoothProfileConnector.getServiceInterface(service);
            if (BluetoothProfileConnector.this.mServiceListener != null) {
                BluetoothProfileConnector.this.mServiceListener.onServiceConnected(BluetoothProfileConnector.this.mProfileId, BluetoothProfileConnector.this.mProfileProxy);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            BluetoothProfileConnector.this.logDebug("Proxy object disconnected");
            BluetoothProfileConnector.this.doUnbind();
            if (BluetoothProfileConnector.this.mServiceListener != null) {
                BluetoothProfileConnector.this.mServiceListener.onServiceDisconnected(BluetoothProfileConnector.this.mProfileId);
            }
        }
    };
    private Context mContext;
    private final int mProfileId;
    private final String mProfileName;
    private final BluetoothProfile mProfileProxy;
    private volatile T mService;
    private ServiceListener mServiceListener;
    private final String mServiceName;

    public abstract T getServiceInterface(IBinder iBinder);

    BluetoothProfileConnector(BluetoothProfile profile, int profileId, String profileName, String serviceName) {
        this.mProfileId = profileId;
        this.mProfileProxy = profile;
        this.mProfileName = profileName;
        this.mServiceName = serviceName;
    }

    /* JADX WARNING: Missing block: B:24:0x0060, code skipped:
            return true;
     */
    private boolean doBind() {
        /*
        r7 = this;
        r0 = r7.mConnection;
        monitor-enter(r0);
        r1 = r7.mService;	 Catch:{ all -> 0x0061 }
        if (r1 != 0) goto L_0x005e;
    L_0x0007:
        r1 = "Binding service...";
        r7.logDebug(r1);	 Catch:{ all -> 0x0061 }
        r1 = 0;
        r2 = new android.content.Intent;	 Catch:{ SecurityException -> 0x0047 }
        r3 = r7.mServiceName;	 Catch:{ SecurityException -> 0x0047 }
        r2.<init>(r3);	 Catch:{ SecurityException -> 0x0047 }
        r3 = r7.mContext;	 Catch:{ SecurityException -> 0x0047 }
        r3 = r3.getPackageManager();	 Catch:{ SecurityException -> 0x0047 }
        r3 = r2.resolveSystemService(r3, r1);	 Catch:{ SecurityException -> 0x0047 }
        r2.setComponent(r3);	 Catch:{ SecurityException -> 0x0047 }
        if (r3 == 0) goto L_0x0031;
    L_0x0023:
        r4 = r7.mContext;	 Catch:{ SecurityException -> 0x0047 }
        r5 = r7.mConnection;	 Catch:{ SecurityException -> 0x0047 }
        r6 = android.os.UserHandle.CURRENT_OR_SELF;	 Catch:{ SecurityException -> 0x0047 }
        r4 = r4.bindServiceAsUser(r2, r5, r1, r6);	 Catch:{ SecurityException -> 0x0047 }
        if (r4 != 0) goto L_0x0030;
    L_0x002f:
        goto L_0x0031;
    L_0x0030:
        goto L_0x005e;
    L_0x0031:
        r4 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x0047 }
        r4.<init>();	 Catch:{ SecurityException -> 0x0047 }
        r5 = "Could not bind to Bluetooth Service with ";
        r4.append(r5);	 Catch:{ SecurityException -> 0x0047 }
        r4.append(r2);	 Catch:{ SecurityException -> 0x0047 }
        r4 = r4.toString();	 Catch:{ SecurityException -> 0x0047 }
        r7.logError(r4);	 Catch:{ SecurityException -> 0x0047 }
        monitor-exit(r0);	 Catch:{ all -> 0x0061 }
        return r1;
    L_0x0047:
        r2 = move-exception;
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0061 }
        r3.<init>();	 Catch:{ all -> 0x0061 }
        r4 = "Failed to bind service. ";
        r3.append(r4);	 Catch:{ all -> 0x0061 }
        r3.append(r2);	 Catch:{ all -> 0x0061 }
        r3 = r3.toString();	 Catch:{ all -> 0x0061 }
        r7.logError(r3);	 Catch:{ all -> 0x0061 }
        monitor-exit(r0);	 Catch:{ all -> 0x0061 }
        return r1;
    L_0x005e:
        monitor-exit(r0);	 Catch:{ all -> 0x0061 }
        r0 = 1;
        return r0;
    L_0x0061:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0061 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.BluetoothProfileConnector.doBind():boolean");
    }

    private void doUnbind() {
        synchronized (this.mConnection) {
            if (this.mService != null) {
                logDebug("Unbinding service...");
                try {
                    this.mContext.unbindService(this.mConnection);
                    this.mService = null;
                } catch (IllegalArgumentException ie) {
                    try {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unable to unbind service: ");
                        stringBuilder.append(ie);
                        logError(stringBuilder.toString());
                    } finally {
                        this.mService = null;
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void connect(Context context, ServiceListener listener) {
        this.mContext = context;
        this.mServiceListener = listener;
        IBluetoothManager mgr = BluetoothAdapter.getDefaultAdapter().getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (RemoteException re) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to register state change callback. ");
                stringBuilder.append(re);
                logError(stringBuilder.toString());
            }
        }
        doBind();
    }

    /* Access modifiers changed, original: 0000 */
    public void disconnect() {
        this.mServiceListener = null;
        IBluetoothManager mgr = BluetoothAdapter.getDefaultAdapter().getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (RemoteException re) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to unregister state change callback");
                stringBuilder.append(re);
                logError(stringBuilder.toString());
            }
        }
        doUnbind();
    }

    /* Access modifiers changed, original: 0000 */
    public T getService() {
        return this.mService;
    }

    private void logDebug(String log) {
        Log.d(this.mProfileName, log);
    }

    private void logError(String log) {
        Log.e(this.mProfileName, log);
    }
}
