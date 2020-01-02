package android.bluetooth;

import android.app.job.JobInfo;
import android.bluetooth.IBluetoothGattServerCallback.Stub;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BluetoothGattServer implements BluetoothProfile {
    private static final int CALLBACK_REG_TIMEOUT = 10000;
    private static final boolean DBG = true;
    private static final String TAG = "BluetoothGattServer";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter;
    private final IBluetoothGattServerCallback mBluetoothGattServerCallback = new Stub() {
        public void onServerRegistered(int status, int serverIf) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onServerRegistered() - status=");
            stringBuilder.append(status);
            stringBuilder.append(" serverIf=");
            stringBuilder.append(serverIf);
            Log.d(BluetoothGattServer.TAG, stringBuilder.toString());
            synchronized (BluetoothGattServer.this.mServerIfLock) {
                if (BluetoothGattServer.this.mCallback != null) {
                    BluetoothGattServer.this.mServerIf = serverIf;
                    BluetoothGattServer.this.mServerIfLock.notify();
                } else {
                    Log.e(BluetoothGattServer.TAG, "onServerRegistered: mCallback is null");
                }
            }
        }

        public void onServerConnectionState(int status, int serverIf, boolean connected, String address) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onServerConnectionState() - status=");
            stringBuilder.append(status);
            stringBuilder.append(" serverIf=");
            stringBuilder.append(serverIf);
            stringBuilder.append(" device=");
            stringBuilder.append(address);
            String stringBuilder2 = stringBuilder.toString();
            String str = BluetoothGattServer.TAG;
            Log.d(str, stringBuilder2);
            try {
                int i;
                BluetoothGattServerCallback access$100 = BluetoothGattServer.this.mCallback;
                BluetoothDevice remoteDevice = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
                if (connected) {
                    i = 2;
                } else {
                    i = 0;
                }
                access$100.onConnectionStateChange(remoteDevice, status, i);
            } catch (Exception ex) {
                Log.w(str, "Unhandled exception in callback", ex);
            }
        }

        public void onServiceAdded(int status, BluetoothGattService service) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onServiceAdded() - handle=");
            stringBuilder.append(service.getInstanceId());
            stringBuilder.append(" uuid=");
            stringBuilder.append(service.getUuid());
            stringBuilder.append(" status=");
            stringBuilder.append(status);
            String stringBuilder2 = stringBuilder.toString();
            String str = BluetoothGattServer.TAG;
            Log.d(str, stringBuilder2);
            if (BluetoothGattServer.this.mPendingService != null) {
                BluetoothGattService tmp = BluetoothGattServer.this.mPendingService;
                BluetoothGattServer.this.mPendingService = null;
                tmp.setInstanceId(service.getInstanceId());
                List<BluetoothGattCharacteristic> temp_chars = tmp.getCharacteristics();
                List<BluetoothGattCharacteristic> svc_chars = service.getCharacteristics();
                for (int i = 0; i < svc_chars.size(); i++) {
                    BluetoothGattCharacteristic temp_char = (BluetoothGattCharacteristic) temp_chars.get(i);
                    BluetoothGattCharacteristic svc_char = (BluetoothGattCharacteristic) svc_chars.get(i);
                    temp_char.setInstanceId(svc_char.getInstanceId());
                    List<BluetoothGattDescriptor> temp_descs = temp_char.getDescriptors();
                    List<BluetoothGattDescriptor> svc_descs = svc_char.getDescriptors();
                    for (int j = 0; j < svc_descs.size(); j++) {
                        ((BluetoothGattDescriptor) temp_descs.get(j)).setInstanceId(((BluetoothGattDescriptor) svc_descs.get(j)).getInstanceId());
                    }
                }
                BluetoothGattServer.this.mServices.add(tmp);
                try {
                    BluetoothGattServer.this.mCallback.onServiceAdded(status, tmp);
                } catch (Exception ex) {
                    Log.w(str, "Unhandled exception in callback", ex);
                }
            }
        }

        public void onCharacteristicReadRequest(String address, int transId, int offset, boolean isLong, int handle) {
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            BluetoothGattCharacteristic characteristic = BluetoothGattServer.this.getCharacteristicByHandle(handle);
            String str = BluetoothGattServer.TAG;
            if (characteristic == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onCharacteristicReadRequest() no char for handle ");
                stringBuilder.append(handle);
                Log.w(str, stringBuilder.toString());
                return;
            }
            try {
                BluetoothGattServer.this.mCallback.onCharacteristicReadRequest(device, transId, offset, characteristic);
            } catch (Exception ex) {
                Log.w(str, "Unhandled exception in callback", ex);
            }
        }

        public void onDescriptorReadRequest(String address, int transId, int offset, boolean isLong, int handle) {
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            BluetoothGattDescriptor descriptor = BluetoothGattServer.this.getDescriptorByHandle(handle);
            String str = BluetoothGattServer.TAG;
            if (descriptor == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onDescriptorReadRequest() no desc for handle ");
                stringBuilder.append(handle);
                Log.w(str, stringBuilder.toString());
                return;
            }
            try {
                BluetoothGattServer.this.mCallback.onDescriptorReadRequest(device, transId, offset, descriptor);
            } catch (Exception ex) {
                Log.w(str, "Unhandled exception in callback", ex);
            }
        }

        public void onCharacteristicWriteRequest(String address, int transId, int offset, int length, boolean isPrep, boolean needRsp, int handle, byte[] value) {
            int i = handle;
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            BluetoothGattCharacteristic characteristic = BluetoothGattServer.this.getCharacteristicByHandle(i);
            String str = BluetoothGattServer.TAG;
            if (characteristic == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onCharacteristicWriteRequest() no char for handle ");
                stringBuilder.append(i);
                Log.w(str, stringBuilder.toString());
                return;
            }
            try {
                BluetoothGattServer.this.mCallback.onCharacteristicWriteRequest(device, transId, characteristic, isPrep, needRsp, offset, value);
            } catch (Exception ex) {
                Log.w(str, "Unhandled exception in callback", ex);
            }
        }

        public void onDescriptorWriteRequest(String address, int transId, int offset, int length, boolean isPrep, boolean needRsp, int handle, byte[] value) {
            int i = handle;
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            BluetoothGattDescriptor descriptor = BluetoothGattServer.this.getDescriptorByHandle(i);
            String str = BluetoothGattServer.TAG;
            if (descriptor == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onDescriptorWriteRequest() no desc for handle ");
                stringBuilder.append(i);
                Log.w(str, stringBuilder.toString());
                return;
            }
            try {
                BluetoothGattServer.this.mCallback.onDescriptorWriteRequest(device, transId, descriptor, isPrep, needRsp, offset, value);
            } catch (Exception ex) {
                Log.w(str, "Unhandled exception in callback", ex);
            }
        }

        public void onExecuteWrite(String address, int transId, boolean execWrite) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onExecuteWrite() - device=");
            stringBuilder.append(address);
            stringBuilder.append(", transId=");
            stringBuilder.append(transId);
            stringBuilder.append("execWrite=");
            stringBuilder.append(execWrite);
            String stringBuilder2 = stringBuilder.toString();
            String str = BluetoothGattServer.TAG;
            Log.d(str, stringBuilder2);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device != null) {
                try {
                    BluetoothGattServer.this.mCallback.onExecuteWrite(device, transId, execWrite);
                } catch (Exception ex) {
                    Log.w(str, "Unhandled exception in callback", ex);
                }
            }
        }

        public void onNotificationSent(String address, int status) {
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device != null) {
                try {
                    BluetoothGattServer.this.mCallback.onNotificationSent(device, status);
                } catch (Exception ex) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unhandled exception: ");
                    stringBuilder.append(ex);
                    Log.w(BluetoothGattServer.TAG, stringBuilder.toString());
                }
            }
        }

        public void onMtuChanged(String address, int mtu) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onMtuChanged() - device=");
            stringBuilder.append(address);
            stringBuilder.append(", mtu=");
            stringBuilder.append(mtu);
            String stringBuilder2 = stringBuilder.toString();
            String str = BluetoothGattServer.TAG;
            Log.d(str, stringBuilder2);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device != null) {
                try {
                    BluetoothGattServer.this.mCallback.onMtuChanged(device, mtu);
                } catch (Exception ex) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Unhandled exception: ");
                    stringBuilder3.append(ex);
                    Log.w(str, stringBuilder3.toString());
                }
            }
        }

        public void onPhyUpdate(String address, int txPhy, int rxPhy, int status) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onPhyUpdate() - device=");
            stringBuilder.append(address);
            stringBuilder.append(", txPHy=");
            stringBuilder.append(txPhy);
            stringBuilder.append(", rxPHy=");
            stringBuilder.append(rxPhy);
            String stringBuilder2 = stringBuilder.toString();
            String str = BluetoothGattServer.TAG;
            Log.d(str, stringBuilder2);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device != null) {
                try {
                    BluetoothGattServer.this.mCallback.onPhyUpdate(device, txPhy, rxPhy, status);
                } catch (Exception ex) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Unhandled exception: ");
                    stringBuilder3.append(ex);
                    Log.w(str, stringBuilder3.toString());
                }
            }
        }

        public void onPhyRead(String address, int txPhy, int rxPhy, int status) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onPhyUpdate() - device=");
            stringBuilder.append(address);
            stringBuilder.append(", txPHy=");
            stringBuilder.append(txPhy);
            stringBuilder.append(", rxPHy=");
            stringBuilder.append(rxPhy);
            String stringBuilder2 = stringBuilder.toString();
            String str = BluetoothGattServer.TAG;
            Log.d(str, stringBuilder2);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device != null) {
                try {
                    BluetoothGattServer.this.mCallback.onPhyRead(device, txPhy, rxPhy, status);
                } catch (Exception ex) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Unhandled exception: ");
                    stringBuilder3.append(ex);
                    Log.w(str, stringBuilder3.toString());
                }
            }
        }

        public void onConnectionUpdated(String address, int interval, int latency, int timeout, int status) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onConnectionUpdated() - Device=");
            stringBuilder.append(address);
            stringBuilder.append(" interval=");
            stringBuilder.append(interval);
            stringBuilder.append(" latency=");
            stringBuilder.append(latency);
            stringBuilder.append(" timeout=");
            stringBuilder.append(timeout);
            stringBuilder.append(" status=");
            stringBuilder.append(status);
            String stringBuilder2 = stringBuilder.toString();
            String str = BluetoothGattServer.TAG;
            Log.d(str, stringBuilder2);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device != null) {
                try {
                    BluetoothGattServer.this.mCallback.onConnectionUpdated(device, interval, latency, timeout, status);
                } catch (Exception ex) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Unhandled exception: ");
                    stringBuilder3.append(ex);
                    Log.w(str, stringBuilder3.toString());
                }
            }
        }
    };
    private BluetoothGattServerCallback mCallback;
    private BluetoothGattService mPendingService;
    private int mServerIf;
    private Object mServerIfLock = new Object();
    private IBluetoothGatt mService;
    private List<BluetoothGattService> mServices;
    private int mTransport;

    BluetoothGattServer(IBluetoothGatt iGatt, int transport) {
        this.mService = iGatt;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mCallback = null;
        this.mServerIf = 0;
        this.mTransport = transport;
        this.mServices = new ArrayList();
    }

    /* Access modifiers changed, original: 0000 */
    public BluetoothGattCharacteristic getCharacteristicByHandle(int handle) {
        for (BluetoothGattService svc : this.mServices) {
            for (BluetoothGattCharacteristic charac : svc.getCharacteristics()) {
                if (charac.getInstanceId() == handle) {
                    return charac;
                }
            }
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public BluetoothGattDescriptor getDescriptorByHandle(int handle) {
        for (BluetoothGattService svc : this.mServices) {
            for (BluetoothGattCharacteristic charac : svc.getCharacteristics()) {
                for (BluetoothGattDescriptor desc : charac.getDescriptors()) {
                    if (desc.getInstanceId() == handle) {
                        return desc;
                    }
                }
            }
        }
        return null;
    }

    public void close() {
        Log.d(TAG, "close()");
        unregisterCallback();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean registerCallback(BluetoothGattServerCallback callback) {
        Log.d(TAG, "registerCallback()");
        if (this.mService == null) {
            Log.e(TAG, "GATT service not available");
            return false;
        }
        UUID uuid = UUID.randomUUID();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("registerCallback() - UUID=");
        stringBuilder.append(uuid);
        Log.d(TAG, stringBuilder.toString());
        synchronized (this.mServerIfLock) {
            if (this.mCallback != null) {
                Log.e(TAG, "App can register callback only once");
                return false;
            }
            this.mCallback = callback;
            try {
                this.mService.registerServer(new ParcelUuid(uuid), this.mBluetoothGattServerCallback);
                try {
                    this.mServerIfLock.wait(JobInfo.MIN_BACKOFF_MILLIS);
                } catch (InterruptedException e) {
                    String str = TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("");
                    stringBuilder2.append(e);
                    Log.e(str, stringBuilder2.toString());
                    this.mCallback = null;
                }
                if (this.mServerIf == 0) {
                    this.mCallback = null;
                    return false;
                }
                return true;
            } catch (RemoteException e2) {
                Log.e(TAG, "", e2);
                this.mCallback = null;
                return false;
            }
        }
    }

    private void unregisterCallback() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unregisterCallback() - mServerIf=");
        stringBuilder.append(this.mServerIf);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        IBluetoothGatt iBluetoothGatt = this.mService;
        if (iBluetoothGatt != null) {
            int i = this.mServerIf;
            if (i != 0) {
                try {
                    this.mCallback = null;
                    iBluetoothGatt.unregisterServer(i);
                    this.mServerIf = 0;
                } catch (RemoteException e) {
                    Log.e(str, "", e);
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public BluetoothGattService getService(UUID uuid, int instanceId, int type) {
        for (BluetoothGattService svc : this.mServices) {
            if (svc.getType() == type && svc.getInstanceId() == instanceId && svc.getUuid().equals(uuid)) {
                return svc;
            }
        }
        return null;
    }

    public boolean connect(BluetoothDevice device, boolean autoConnect) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("connect() - device: ");
        stringBuilder.append(device.getAddress());
        stringBuilder.append(", auto: ");
        stringBuilder.append(autoConnect);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        IBluetoothGatt iBluetoothGatt = this.mService;
        if (iBluetoothGatt != null) {
            int i = this.mServerIf;
            if (i != 0) {
                try {
                    iBluetoothGatt.serverConnect(i, device.getAddress(), !autoConnect, this.mTransport);
                    return true;
                } catch (RemoteException e) {
                    Log.e(str, "", e);
                    return false;
                }
            }
        }
        return false;
    }

    public void cancelConnection(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("cancelConnection() - device: ");
        stringBuilder.append(device.getAddress());
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        IBluetoothGatt iBluetoothGatt = this.mService;
        if (iBluetoothGatt != null) {
            int i = this.mServerIf;
            if (i != 0) {
                try {
                    iBluetoothGatt.serverDisconnect(i, device.getAddress());
                } catch (RemoteException e) {
                    Log.e(str, "", e);
                }
            }
        }
    }

    public void setPreferredPhy(BluetoothDevice device, int txPhy, int rxPhy, int phyOptions) {
        try {
            this.mService.serverSetPreferredPhy(this.mServerIf, device.getAddress(), txPhy, rxPhy, phyOptions);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    public void readPhy(BluetoothDevice device) {
        try {
            this.mService.serverReadPhy(this.mServerIf, device.getAddress());
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    public boolean sendResponse(BluetoothDevice device, int requestId, int status, int offset, byte[] value) {
        IBluetoothGatt iBluetoothGatt = this.mService;
        if (iBluetoothGatt != null) {
            int i = this.mServerIf;
            if (i != 0) {
                try {
                    iBluetoothGatt.sendResponse(i, device.getAddress(), requestId, status, offset, value);
                    return true;
                } catch (RemoteException e) {
                    Log.e(TAG, "", e);
                    return false;
                }
            }
        }
        return false;
    }

    /* JADX WARNING: Missing block: B:18:0x003f, code skipped:
            return false;
     */
    public boolean notifyCharacteristicChanged(android.bluetooth.BluetoothDevice r10, android.bluetooth.BluetoothGattCharacteristic r11, boolean r12) {
        /*
        r9 = this;
        r0 = r9.mService;
        r1 = 0;
        if (r0 == 0) goto L_0x003f;
    L_0x0005:
        r0 = r9.mServerIf;
        if (r0 != 0) goto L_0x000a;
    L_0x0009:
        goto L_0x003f;
    L_0x000a:
        r0 = r11.getService();
        if (r0 != 0) goto L_0x0011;
    L_0x0010:
        return r1;
    L_0x0011:
        r2 = r11.getValue();
        if (r2 == 0) goto L_0x0037;
    L_0x0017:
        r3 = r9.mService;	 Catch:{ RemoteException -> 0x002e }
        r4 = r9.mServerIf;	 Catch:{ RemoteException -> 0x002e }
        r5 = r10.getAddress();	 Catch:{ RemoteException -> 0x002e }
        r6 = r11.getInstanceId();	 Catch:{ RemoteException -> 0x002e }
        r8 = r11.getValue();	 Catch:{ RemoteException -> 0x002e }
        r7 = r12;
        r3.sendNotification(r4, r5, r6, r7, r8);	 Catch:{ RemoteException -> 0x002e }
        r1 = 1;
        return r1;
    L_0x002e:
        r2 = move-exception;
        r3 = "BluetoothGattServer";
        r4 = "";
        android.util.Log.e(r3, r4, r2);
        return r1;
    L_0x0037:
        r1 = new java.lang.IllegalArgumentException;
        r2 = "Chracteristic value is empty. Use BluetoothGattCharacteristic#setvalue to update";
        r1.<init>(r2);
        throw r1;
    L_0x003f:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.BluetoothGattServer.notifyCharacteristicChanged(android.bluetooth.BluetoothDevice, android.bluetooth.BluetoothGattCharacteristic, boolean):boolean");
    }

    public boolean addService(BluetoothGattService service) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("addService() - service: ");
        stringBuilder.append(service.getUuid());
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        IBluetoothGatt iBluetoothGatt = this.mService;
        if (iBluetoothGatt != null) {
            int i = this.mServerIf;
            if (i != 0) {
                this.mPendingService = service;
                try {
                    iBluetoothGatt.addService(i, service);
                    return true;
                } catch (RemoteException e) {
                    Log.e(str, "", e);
                    return false;
                }
            }
        }
        return false;
    }

    public boolean removeService(BluetoothGattService service) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("removeService() - service: ");
        stringBuilder.append(service.getUuid());
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        if (this.mService == null || this.mServerIf == 0) {
            return false;
        }
        BluetoothGattService intService = getService(service.getUuid(), service.getInstanceId(), service.getType());
        if (intService == null) {
            return false;
        }
        try {
            this.mService.removeService(this.mServerIf, service.getInstanceId());
            this.mServices.remove(intService);
            return true;
        } catch (RemoteException e) {
            Log.e(str, "", e);
            return false;
        }
    }

    public void clearServices() {
        String str = TAG;
        Log.d(str, "clearServices()");
        IBluetoothGatt iBluetoothGatt = this.mService;
        if (iBluetoothGatt != null) {
            int i = this.mServerIf;
            if (i != 0) {
                try {
                    iBluetoothGatt.clearServices(i);
                    this.mServices.clear();
                } catch (RemoteException e) {
                    Log.e(str, "", e);
                }
            }
        }
    }

    public List<BluetoothGattService> getServices() {
        return this.mServices;
    }

    public BluetoothGattService getService(UUID uuid) {
        for (BluetoothGattService service : this.mServices) {
            if (service.getUuid().equals(uuid)) {
                return service;
            }
        }
        return null;
    }

    public int getConnectionState(BluetoothDevice device) {
        throw new UnsupportedOperationException("Use BluetoothManager#getConnectionState instead.");
    }

    public List<BluetoothDevice> getConnectedDevices() {
        throw new UnsupportedOperationException("Use BluetoothManager#getConnectedDevices instead.");
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        throw new UnsupportedOperationException("Use BluetoothManager#getDevicesMatchingConnectionStates instead.");
    }
}
