package android.bluetooth;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothMetadataListener.Stub;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.PeriodicAdvertisingManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.le.ScanSettings.Builder;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SynchronousResultReceiver;
import android.os.SynchronousResultReceiver.Result;
import android.os.SystemProperties;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.util.SeempLog;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class BluetoothAdapter {
    public static final String ACTION_BLE_ACL_CONNECTED = "android.bluetooth.adapter.action.BLE_ACL_CONNECTED";
    public static final String ACTION_BLE_ACL_DISCONNECTED = "android.bluetooth.adapter.action.BLE_ACL_DISCONNECTED";
    @SystemApi
    public static final String ACTION_BLE_STATE_CHANGED = "android.bluetooth.adapter.action.BLE_STATE_CHANGED";
    public static final String ACTION_BLUETOOTH_ADDRESS_CHANGED = "android.bluetooth.adapter.action.BLUETOOTH_ADDRESS_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_DISCOVERY_FINISHED = "android.bluetooth.adapter.action.DISCOVERY_FINISHED";
    public static final String ACTION_DISCOVERY_STARTED = "android.bluetooth.adapter.action.DISCOVERY_STARTED";
    public static final String ACTION_LOCAL_NAME_CHANGED = "android.bluetooth.adapter.action.LOCAL_NAME_CHANGED";
    @SystemApi
    public static final String ACTION_REQUEST_BLE_SCAN_ALWAYS_AVAILABLE = "android.bluetooth.adapter.action.REQUEST_BLE_SCAN_ALWAYS_AVAILABLE";
    public static final String ACTION_REQUEST_DISABLE = "android.bluetooth.adapter.action.REQUEST_DISABLE";
    public static final String ACTION_REQUEST_DISCOVERABLE = "android.bluetooth.adapter.action.REQUEST_DISCOVERABLE";
    public static final String ACTION_REQUEST_ENABLE = "android.bluetooth.adapter.action.REQUEST_ENABLE";
    public static final String ACTION_SCAN_MODE_CHANGED = "android.bluetooth.adapter.action.SCAN_MODE_CHANGED";
    public static final String ACTION_STATE_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";
    private static final int ADDRESS_LENGTH = 17;
    public static final String BLUETOOTH_MANAGER_SERVICE = "bluetooth_manager";
    private static final boolean DBG = true;
    public static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";
    public static final int ERROR = Integer.MIN_VALUE;
    public static final String EXTRA_BLUETOOTH_ADDRESS = "android.bluetooth.adapter.extra.BLUETOOTH_ADDRESS";
    public static final String EXTRA_CONNECTION_STATE = "android.bluetooth.adapter.extra.CONNECTION_STATE";
    public static final String EXTRA_DISCOVERABLE_DURATION = "android.bluetooth.adapter.extra.DISCOVERABLE_DURATION";
    public static final String EXTRA_LOCAL_NAME = "android.bluetooth.adapter.extra.LOCAL_NAME";
    public static final String EXTRA_PREVIOUS_CONNECTION_STATE = "android.bluetooth.adapter.extra.PREVIOUS_CONNECTION_STATE";
    public static final String EXTRA_PREVIOUS_SCAN_MODE = "android.bluetooth.adapter.extra.PREVIOUS_SCAN_MODE";
    public static final String EXTRA_PREVIOUS_STATE = "android.bluetooth.adapter.extra.PREVIOUS_STATE";
    public static final String EXTRA_SCAN_MODE = "android.bluetooth.adapter.extra.SCAN_MODE";
    public static final String EXTRA_STATE = "android.bluetooth.adapter.extra.STATE";
    public static final int IO_CAPABILITY_IN = 2;
    public static final int IO_CAPABILITY_IO = 1;
    public static final int IO_CAPABILITY_KBDISP = 4;
    public static final int IO_CAPABILITY_MAX = 5;
    public static final int IO_CAPABILITY_NONE = 3;
    public static final int IO_CAPABILITY_OUT = 0;
    public static final int IO_CAPABILITY_UNKNOWN = 255;
    public static final UUID LE_PSM_CHARACTERISTIC_UUID = UUID.fromString("2d410339-82b6-42aa-b34e-e2e01df8cc1a");
    public static final int SCAN_MODE_CONNECTABLE = 21;
    public static final int SCAN_MODE_CONNECTABLE_DISCOVERABLE = 23;
    public static final int SCAN_MODE_NONE = 20;
    public static final int SOCKET_CHANNEL_AUTO_STATIC_NO_SDP = -2;
    public static final int STATE_BLE_ON = 15;
    public static final int STATE_BLE_TURNING_OFF = 16;
    public static final int STATE_BLE_TURNING_ON = 14;
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_DISCONNECTING = 3;
    public static final int STATE_OFF = 10;
    public static final int STATE_ON = 12;
    public static final int STATE_TURNING_OFF = 13;
    public static final int STATE_TURNING_ON = 11;
    private static final String TAG = "BluetoothAdapter";
    private static final boolean VDBG = false;
    private static BluetoothAdapter sAdapter;
    private static BluetoothLeAdvertiser sBluetoothLeAdvertiser;
    private static BluetoothLeScanner sBluetoothLeScanner;
    private static final IBluetoothMetadataListener sBluetoothMetadataListener = new Stub() {
        public void onMetadataChanged(BluetoothDevice device, int key, byte[] value) {
            synchronized (BluetoothAdapter.sMetadataListeners) {
                if (BluetoothAdapter.sMetadataListeners.containsKey(device)) {
                    for (Pair<OnMetadataChangedListener, Executor> pair : (List) BluetoothAdapter.sMetadataListeners.get(device)) {
                        pair.second.execute(new -$$Lambda$BluetoothAdapter$1$I3p3FVKkxuogQU8eug7PAKoZKZc(pair.first, device, key, value));
                    }
                }
            }
        }
    };
    private static final Map<BluetoothDevice, List<Pair<OnMetadataChangedListener, Executor>>> sMetadataListeners = new HashMap();
    private static PeriodicAdvertisingManager sPeriodicAdvertisingManager;
    private Context mContext;
    private final Map<LeScanCallback, ScanCallback> mLeScanClients;
    private final Object mLock = new Object();
    private final IBluetoothManagerCallback mManagerCallback = new IBluetoothManagerCallback.Stub() {
        public void onBluetoothServiceUp(IBluetooth bluetoothService) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onBluetoothServiceUp: ");
            stringBuilder.append(bluetoothService);
            Log.d(BluetoothAdapter.TAG, stringBuilder.toString());
            BluetoothAdapter.this.mServiceLock.writeLock().lock();
            BluetoothAdapter.this.mService = bluetoothService;
            BluetoothAdapter.this.mServiceLock.writeLock().unlock();
            synchronized (BluetoothAdapter.this.mProxyServiceStateCallbacks) {
                Iterator it = BluetoothAdapter.this.mProxyServiceStateCallbacks.iterator();
                while (it.hasNext()) {
                    IBluetoothManagerCallback cb = (IBluetoothManagerCallback) it.next();
                    if (cb != null) {
                        try {
                            cb.onBluetoothServiceUp(bluetoothService);
                        } catch (Exception e) {
                            Log.e(BluetoothAdapter.TAG, "", e);
                        }
                    } else {
                        Log.d(BluetoothAdapter.TAG, "onBluetoothServiceUp: cb is null!");
                    }
                }
            }
            synchronized (BluetoothAdapter.sMetadataListeners) {
                BluetoothAdapter.sMetadataListeners.forEach(new -$$Lambda$BluetoothAdapter$2$INSd_aND-SGWhhPZUtIqya_Uxw4(this));
            }
        }

        public /* synthetic */ void lambda$onBluetoothServiceUp$0$BluetoothAdapter$2(BluetoothDevice device, List pair) {
            try {
                BluetoothAdapter.this.mService.registerMetadataListener(BluetoothAdapter.sBluetoothMetadataListener, device);
            } catch (RemoteException e) {
                Log.e(BluetoothAdapter.TAG, "Failed to register metadata listener", e);
            }
        }

        public void onBluetoothServiceDown() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onBluetoothServiceDown: ");
            stringBuilder.append(BluetoothAdapter.this.mService);
            Log.d(BluetoothAdapter.TAG, stringBuilder.toString());
            try {
                BluetoothAdapter.this.mServiceLock.writeLock().lock();
                BluetoothAdapter.this.mService = null;
                if (BluetoothAdapter.this.mLeScanClients != null) {
                    BluetoothAdapter.this.mLeScanClients.clear();
                }
                if (BluetoothAdapter.sBluetoothLeAdvertiser != null) {
                    BluetoothAdapter.sBluetoothLeAdvertiser.cleanup();
                }
                if (BluetoothAdapter.sBluetoothLeScanner != null) {
                    BluetoothAdapter.sBluetoothLeScanner.cleanup();
                }
                BluetoothAdapter.this.mServiceLock.writeLock().unlock();
                synchronized (BluetoothAdapter.this.mProxyServiceStateCallbacks) {
                    String str = BluetoothAdapter.TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("onBluetoothServiceDown: Sending callbacks to ");
                    stringBuilder2.append(BluetoothAdapter.this.mProxyServiceStateCallbacks.size());
                    stringBuilder2.append(" clients");
                    Log.d(str, stringBuilder2.toString());
                    Iterator it = BluetoothAdapter.this.mProxyServiceStateCallbacks.iterator();
                    while (it.hasNext()) {
                        IBluetoothManagerCallback cb = (IBluetoothManagerCallback) it.next();
                        if (cb != null) {
                            try {
                                cb.onBluetoothServiceDown();
                            } catch (Exception e) {
                                Log.e(BluetoothAdapter.TAG, "", e);
                            }
                        } else {
                            Log.d(BluetoothAdapter.TAG, "onBluetoothServiceDown: cb is null!");
                        }
                    }
                }
                Log.d(BluetoothAdapter.TAG, "onBluetoothServiceDown: Finished sending callbacks to registered clients");
            } catch (Throwable th) {
                BluetoothAdapter.this.mServiceLock.writeLock().unlock();
            }
        }

        public void onBrEdrDown() {
        }
    };
    private final IBluetoothManager mManagerService;
    private final ArrayList<IBluetoothManagerCallback> mProxyServiceStateCallbacks = new ArrayList();
    @UnsupportedAppUsage
    private IBluetooth mService;
    private final ReentrantReadWriteLock mServiceLock = new ReentrantReadWriteLock();
    private final IBinder mToken;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AdapterState {
    }

    public interface BluetoothStateChangeCallback {
        void onBluetoothStateChange(boolean z);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IoCapability {
    }

    public interface LeScanCallback {
        void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr);
    }

    @SystemApi
    public interface OnMetadataChangedListener {
        void onMetadataChanged(BluetoothDevice bluetoothDevice, int i, byte[] bArr);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScanMode {
    }

    public class StateChangeCallbackWrapper extends IBluetoothStateChangeCallback.Stub {
        private BluetoothStateChangeCallback mCallback;

        StateChangeCallbackWrapper(BluetoothStateChangeCallback callback) {
            this.mCallback = callback;
        }

        public void onBluetoothStateChange(boolean on) {
            this.mCallback.onBluetoothStateChange(on);
        }
    }

    public static String nameForState(int state) {
        switch (state) {
            case 10:
                return "OFF";
            case 11:
                return "TURNING_ON";
            case 12:
                return "ON";
            case 13:
                return "TURNING_OFF";
            case 14:
                return "BLE_TURNING_ON";
            case 15:
                return "BLE_ON";
            case 16:
                return "BLE_TURNING_OFF";
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("?!?!? (");
                stringBuilder.append(state);
                stringBuilder.append(")");
                return stringBuilder.toString();
        }
    }

    public static synchronized BluetoothAdapter getDefaultAdapter() {
        BluetoothAdapter bluetoothAdapter;
        synchronized (BluetoothAdapter.class) {
            if (sAdapter == null) {
                IBinder b = ServiceManager.getService(BLUETOOTH_MANAGER_SERVICE);
                if (b != null) {
                    sAdapter = new BluetoothAdapter(IBluetoothManager.Stub.asInterface(b));
                } else {
                    Log.e(TAG, "Bluetooth binder is null");
                }
            }
            bluetoothAdapter = sAdapter;
        }
        return bluetoothAdapter;
    }

    BluetoothAdapter(IBluetoothManager managerService) {
        if (managerService != null) {
            try {
                this.mServiceLock.writeLock().lock();
                this.mService = managerService.registerAdapter(this.mManagerCallback);
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            } catch (Throwable th) {
                this.mServiceLock.writeLock().unlock();
            }
            this.mServiceLock.writeLock().unlock();
            this.mManagerService = managerService;
            this.mLeScanClients = new HashMap();
            this.mToken = new Binder();
            return;
        }
        throw new IllegalArgumentException("bluetooth manager service is null");
    }

    public BluetoothDevice getRemoteDevice(String address) {
        SeempLog.record(62);
        return new BluetoothDevice(address);
    }

    public BluetoothDevice getRemoteDevice(byte[] address) {
        SeempLog.record(62);
        if (address == null || address.length != 6) {
            throw new IllegalArgumentException("Bluetooth address must have 6 bytes");
        }
        return new BluetoothDevice(String.format(Locale.US, "%02X:%02X:%02X:%02X:%02X:%02X", new Object[]{Byte.valueOf(address[0]), Byte.valueOf(address[1]), Byte.valueOf(address[2]), Byte.valueOf(address[3]), Byte.valueOf(address[4]), Byte.valueOf(address[5])}));
    }

    public BluetoothLeAdvertiser getBluetoothLeAdvertiser() {
        if (!getLeAccess()) {
            return null;
        }
        synchronized (this.mLock) {
            if (sBluetoothLeAdvertiser == null) {
                sBluetoothLeAdvertiser = new BluetoothLeAdvertiser(this.mManagerService);
            }
        }
        return sBluetoothLeAdvertiser;
    }

    public PeriodicAdvertisingManager getPeriodicAdvertisingManager() {
        if (!getLeAccess() || !isLePeriodicAdvertisingSupported()) {
            return null;
        }
        synchronized (this.mLock) {
            if (sPeriodicAdvertisingManager == null) {
                sPeriodicAdvertisingManager = new PeriodicAdvertisingManager(this.mManagerService);
            }
        }
        return sPeriodicAdvertisingManager;
    }

    public BluetoothLeScanner getBluetoothLeScanner() {
        if (!getLeAccess()) {
            return null;
        }
        synchronized (this.mLock) {
            if (sBluetoothLeScanner == null) {
                sBluetoothLeScanner = new BluetoothLeScanner(this.mManagerService);
            }
        }
        return sBluetoothLeScanner;
    }

    public boolean isEnabled() {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean isEnabled = this.mService.isEnabled();
                this.mServiceLock.readLock().unlock();
                return isEnabled;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    @SystemApi
    public boolean isLeEnabled() {
        int state = getLeState();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isLeEnabled(): ");
        stringBuilder.append(nameForState(state));
        Log.d(TAG, stringBuilder.toString());
        return state == 12 || state == 15;
    }

    @SystemApi
    public boolean disableBLE() {
        if (!isBleScanAlwaysAvailable()) {
            return false;
        }
        int state = getLeState();
        String str = TAG;
        if (state == 12 || state == 15) {
            String packageName = ActivityThread.currentPackageName();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("disableBLE(): de-registering ");
            stringBuilder.append(packageName);
            Log.d(str, stringBuilder.toString());
            try {
                this.mManagerService.updateBleAppCount(this.mToken, false, packageName);
            } catch (RemoteException e) {
                Log.e(str, "", e);
            }
            return true;
        }
        Log.d(str, "disableBLE(): Already disabled");
        return false;
    }

    @SystemApi
    public boolean enableBLE() {
        String str = TAG;
        if (!isBleScanAlwaysAvailable()) {
            return false;
        }
        try {
            String packageName = ActivityThread.currentPackageName();
            this.mManagerService.updateBleAppCount(this.mToken, true, packageName);
            if (isLeEnabled()) {
                Log.d(str, "enableBLE(): Bluetooth already enabled");
                return true;
            }
            Log.d(str, "enableBLE(): Calling enable");
            return this.mManagerService.enable(packageName);
        } catch (RemoteException e) {
            Log.e(str, "", e);
            return false;
        }
    }

    public int getState() {
        SeempLog.record(63);
        int state = 10;
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                state = this.mService.getState();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        if (state == 15 || state == 14 || state == 16) {
            return 10;
        }
        return state;
    }

    @UnsupportedAppUsage
    public int getLeState() {
        int state = 10;
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                state = this.mService.getState();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return state;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean getLeAccess() {
        if (getLeState() == 12 || getLeState() == 15) {
            return true;
        }
        return false;
    }

    public boolean enable() {
        SeempLog.record(56);
        boolean isEnabled = isEnabled();
        String str = TAG;
        if (isEnabled) {
            Log.d(str, "enable(): BT already enabled!");
            return true;
        }
        try {
            return this.mManagerService.enable(ActivityThread.currentPackageName());
        } catch (RemoteException e) {
            Log.e(str, "", e);
            return false;
        }
    }

    public boolean disable() {
        SeempLog.record(57);
        try {
            return this.mManagerService.disable(ActivityThread.currentPackageName(), true);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    @UnsupportedAppUsage
    public boolean disable(boolean persist) {
        SeempLog.record(57);
        try {
            return this.mManagerService.disable(ActivityThread.currentPackageName(), persist);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public String getAddress() {
        try {
            return this.mManagerService.getAddress();
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return null;
        }
    }

    public String getName() {
        try {
            return this.mManagerService.getName();
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return null;
        }
    }

    @UnsupportedAppUsage
    public boolean factoryReset() {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mManagerService != null) {
                SystemProperties.set("persist.bluetooth.factoryreset", "true");
                boolean factoryReset = this.mManagerService.factoryReset();
                this.mServiceLock.readLock().unlock();
                return factoryReset;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    @UnsupportedAppUsage
    public ParcelUuid[] getUuids() {
        if (getState() != 12) {
            return null;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                ParcelUuid[] uuids = this.mService.getUuids();
                this.mServiceLock.readLock().unlock();
                return uuids;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return null;
    }

    public boolean setName(String name) {
        if (getState() != 12) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean name2 = this.mService.setName(name);
                this.mServiceLock.readLock().unlock();
                return name2;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public BluetoothClass getBluetoothClass() {
        if (getState() != 12) {
            return null;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                BluetoothClass bluetoothClass = this.mService.getBluetoothClass();
                this.mServiceLock.readLock().unlock();
                return bluetoothClass;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return null;
    }

    public boolean setBluetoothClass(BluetoothClass bluetoothClass) {
        if (getState() != 12) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean bluetoothClass2 = this.mService.setBluetoothClass(bluetoothClass);
                this.mServiceLock.readLock().unlock();
                return bluetoothClass2;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public int getIoCapability() {
        if (getState() != 12) {
            return 255;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                int ioCapability = this.mService.getIoCapability();
                this.mServiceLock.readLock().unlock();
                return ioCapability;
            }
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return 255;
    }

    public boolean setIoCapability(int capability) {
        if (getState() != 12) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean ioCapability = this.mService.setIoCapability(capability);
                this.mServiceLock.readLock().unlock();
                return ioCapability;
            }
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public int getLeIoCapability() {
        if (getState() != 12) {
            return 255;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                int leIoCapability = this.mService.getLeIoCapability();
                this.mServiceLock.readLock().unlock();
                return leIoCapability;
            }
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return 255;
    }

    public boolean setLeIoCapability(int capability) {
        if (getState() != 12) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean leIoCapability = this.mService.setLeIoCapability(capability);
                this.mServiceLock.readLock().unlock();
                return leIoCapability;
            }
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public int getScanMode() {
        if (getState() != 12) {
            return 20;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                int scanMode = this.mService.getScanMode();
                this.mServiceLock.readLock().unlock();
                return scanMode;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return 20;
    }

    @UnsupportedAppUsage
    public boolean setScanMode(int mode, int duration) {
        if (getState() != 12) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean scanMode = this.mService.setScanMode(mode, duration);
                this.mServiceLock.readLock().unlock();
                return scanMode;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    @UnsupportedAppUsage
    public boolean setScanMode(int mode) {
        if (getState() != 12) {
            return false;
        }
        return setScanMode(mode, getDiscoverableTimeout());
    }

    @UnsupportedAppUsage
    public int getDiscoverableTimeout() {
        if (getState() != 12) {
            return -1;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                int discoverableTimeout = this.mService.getDiscoverableTimeout();
                this.mServiceLock.readLock().unlock();
                return discoverableTimeout;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return -1;
    }

    @UnsupportedAppUsage
    public void setDiscoverableTimeout(int timeout) {
        if (getState() == 12) {
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    this.mService.setDiscoverableTimeout(timeout);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            } catch (Throwable th) {
                this.mServiceLock.readLock().unlock();
            }
            this.mServiceLock.readLock().unlock();
        }
    }

    public long getDiscoveryEndMillis() {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                long discoveryEndMillis = this.mService.getDiscoveryEndMillis();
                this.mServiceLock.readLock().unlock();
                return discoveryEndMillis;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return -1;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    private String getOpPackageName() {
        Context context = this.mContext;
        if (context != null) {
            return context.getOpPackageName();
        }
        return ActivityThread.currentOpPackageName();
    }

    public boolean startDiscovery() {
        SeempLog.record(58);
        if (getState() != 12) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean startDiscovery = this.mService.startDiscovery(getOpPackageName());
                this.mServiceLock.readLock().unlock();
                return startDiscovery;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public boolean cancelDiscovery() {
        if (getState() != 12) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean cancelDiscovery = this.mService.cancelDiscovery();
                this.mServiceLock.readLock().unlock();
                return cancelDiscovery;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public boolean isDiscovering() {
        if (getState() != 12) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean isDiscovering = this.mService.isDiscovering();
                this.mServiceLock.readLock().unlock();
                return isDiscovering;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public boolean isMultipleAdvertisementSupported() {
        if (getState() != 12) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean isMultiAdvertisementSupported = this.mService.isMultiAdvertisementSupported();
                this.mServiceLock.readLock().unlock();
                return isMultiAdvertisementSupported;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "failed to get isMultipleAdvertisementSupported, error: ", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    @SystemApi
    public boolean isBleScanAlwaysAvailable() {
        try {
            return this.mManagerService.isBleScanAlwaysAvailable();
        } catch (RemoteException e) {
            Log.e(TAG, "remote expection when calling isBleScanAlwaysAvailable", e);
            return false;
        }
    }

    public boolean isOffloadedFilteringSupported() {
        if (!getLeAccess()) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean isOffloadedFilteringSupported = this.mService.isOffloadedFilteringSupported();
                this.mServiceLock.readLock().unlock();
                return isOffloadedFilteringSupported;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "failed to get isOffloadedFilteringSupported, error: ", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public boolean isOffloadedScanBatchingSupported() {
        if (!getLeAccess()) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean isOffloadedScanBatchingSupported = this.mService.isOffloadedScanBatchingSupported();
                this.mServiceLock.readLock().unlock();
                return isOffloadedScanBatchingSupported;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "failed to get isOffloadedScanBatchingSupported, error: ", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public boolean isLe2MPhySupported() {
        if (!getLeAccess()) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean isLe2MPhySupported = this.mService.isLe2MPhySupported();
                this.mServiceLock.readLock().unlock();
                return isLe2MPhySupported;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "failed to get isExtendedAdvertisingSupported, error: ", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public boolean isLeCodedPhySupported() {
        if (!getLeAccess()) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean isLeCodedPhySupported = this.mService.isLeCodedPhySupported();
                this.mServiceLock.readLock().unlock();
                return isLeCodedPhySupported;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "failed to get isLeCodedPhySupported, error: ", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public boolean isLeExtendedAdvertisingSupported() {
        if (!getLeAccess()) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean isLeExtendedAdvertisingSupported = this.mService.isLeExtendedAdvertisingSupported();
                this.mServiceLock.readLock().unlock();
                return isLeExtendedAdvertisingSupported;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "failed to get isLeExtendedAdvertisingSupported, error: ", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public boolean isLePeriodicAdvertisingSupported() {
        if (!getLeAccess()) {
            return false;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                boolean isLePeriodicAdvertisingSupported = this.mService.isLePeriodicAdvertisingSupported();
                this.mServiceLock.readLock().unlock();
                return isLePeriodicAdvertisingSupported;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "failed to get isLePeriodicAdvertisingSupported, error: ", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public int getLeMaximumAdvertisingDataLength() {
        if (!getLeAccess()) {
            return 0;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                int leMaximumAdvertisingDataLength = this.mService.getLeMaximumAdvertisingDataLength();
                this.mServiceLock.readLock().unlock();
                return leMaximumAdvertisingDataLength;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "failed to get getLeMaximumAdvertisingDataLength, error: ", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return 0;
    }

    private boolean isHearingAidProfileSupported() {
        try {
            return this.mManagerService.isHearingAidProfileSupported();
        } catch (RemoteException e) {
            Log.e(TAG, "remote expection when calling isHearingAidProfileSupported", e);
            return false;
        }
    }

    public int getMaxConnectedAudioDevices() {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                int maxConnectedAudioDevices = this.mService.getMaxConnectedAudioDevices();
                this.mServiceLock.readLock().unlock();
                return maxConnectedAudioDevices;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "failed to get getMaxConnectedAudioDevices, error: ", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return 1;
    }

    public boolean isHardwareTrackingFiltersAvailable() {
        boolean z = false;
        if (!getLeAccess()) {
            return false;
        }
        try {
            IBluetoothGatt iGatt = this.mManagerService.getBluetoothGatt();
            if (iGatt == null) {
                return false;
            }
            if (iGatt.numHwTrackFiltersAvailable() != 0) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    @Deprecated
    public BluetoothActivityEnergyInfo getControllerActivityEnergyInfo(int updateType) {
        SynchronousResultReceiver receiver = new SynchronousResultReceiver();
        requestControllerActivityEnergyInfo(receiver);
        try {
            Result result = receiver.awaitResult(1000);
            if (result.bundle != null) {
                return (BluetoothActivityEnergyInfo) result.bundle.getParcelable("controller_activity");
            }
        } catch (TimeoutException e) {
            Log.e(TAG, "getControllerActivityEnergyInfo timed out");
        }
        return null;
    }

    /* JADX WARNING: Failed to extract finally block: empty outs */
    public void requestControllerActivityEnergyInfo(android.os.ResultReceiver r7) {
        /*
        r6 = this;
        r0 = 0;
        r1 = 0;
        r2 = r6.mServiceLock;	 Catch:{ RemoteException -> 0x0026 }
        r2 = r2.readLock();	 Catch:{ RemoteException -> 0x0026 }
        r2.lock();	 Catch:{ RemoteException -> 0x0026 }
        r2 = r6.mService;	 Catch:{ RemoteException -> 0x0026 }
        if (r2 == 0) goto L_0x0015;
    L_0x000f:
        r2 = r6.mService;	 Catch:{ RemoteException -> 0x0026 }
        r2.requestActivityInfo(r7);	 Catch:{ RemoteException -> 0x0026 }
        r7 = 0;
    L_0x0015:
        r2 = r6.mServiceLock;
        r2 = r2.readLock();
        r2.unlock();
        if (r7 == 0) goto L_0x004a;
    L_0x0020:
        r7.send(r1, r0);
        goto L_0x004a;
    L_0x0024:
        r2 = move-exception;
        goto L_0x004b;
    L_0x0026:
        r2 = move-exception;
        r3 = "BluetoothAdapter";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0024 }
        r4.<init>();	 Catch:{ all -> 0x0024 }
        r5 = "getControllerActivityEnergyInfoCallback: ";
        r4.append(r5);	 Catch:{ all -> 0x0024 }
        r4.append(r2);	 Catch:{ all -> 0x0024 }
        r4 = r4.toString();	 Catch:{ all -> 0x0024 }
        android.util.Log.e(r3, r4);	 Catch:{ all -> 0x0024 }
        r2 = r6.mServiceLock;
        r2 = r2.readLock();
        r2.unlock();
        if (r7 == 0) goto L_0x004a;
    L_0x0049:
        goto L_0x0020;
    L_0x004a:
        return;
    L_0x004b:
        r3 = r6.mServiceLock;
        r3 = r3.readLock();
        r3.unlock();
        if (r7 == 0) goto L_0x0059;
    L_0x0056:
        r7.send(r1, r0);
    L_0x0059:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.BluetoothAdapter.requestControllerActivityEnergyInfo(android.os.ResultReceiver):void");
    }

    public Set<BluetoothDevice> getBondedDevices() {
        SeempLog.record(61);
        if (getState() != 12) {
            return toDeviceSet(new BluetoothDevice[0]);
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                Set<BluetoothDevice> toDeviceSet = toDeviceSet(this.mService.getBondedDevices());
                return toDeviceSet;
            }
            Set toDeviceSet2 = toDeviceSet(new BluetoothDevice[0]);
            this.mServiceLock.readLock().unlock();
            return toDeviceSet2;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return null;
        } finally {
            this.mServiceLock.readLock().unlock();
            return null;
        }
    }

    public List<Integer> getSupportedProfiles() {
        ArrayList<Integer> supportedProfiles = new ArrayList();
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    long supportedProfilesBitMask = this.mService.getSupportedProfiles();
                    for (int i = 0; i <= 22; i++) {
                        if ((((long) (1 << i)) & supportedProfilesBitMask) != 0) {
                            supportedProfiles.add(Integer.valueOf(i));
                        }
                    }
                } else if (isHearingAidProfileSupported()) {
                    supportedProfiles.add(Integer.valueOf(21));
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getSupportedProfiles:", e);
        }
        return supportedProfiles;
    }

    @UnsupportedAppUsage
    public int getConnectionState() {
        if (getState() != 12) {
            return 0;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                int adapterConnectionState = this.mService.getAdapterConnectionState();
                this.mServiceLock.readLock().unlock();
                return adapterConnectionState;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getConnectionState:", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return 0;
    }

    public int getProfileConnectionState(int profile) {
        SeempLog.record(64);
        if (getState() != 12) {
            return 0;
        }
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null) {
                int profileConnectionState = this.mService.getProfileConnectionState(profile);
                this.mServiceLock.readLock().unlock();
                return profileConnectionState;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getProfileConnectionState:", e);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        this.mServiceLock.readLock().unlock();
        return 0;
    }

    public BluetoothServerSocket listenUsingRfcommOn(int channel) throws IOException {
        return listenUsingRfcommOn(channel, false, false);
    }

    @UnsupportedAppUsage
    public BluetoothServerSocket listenUsingRfcommOn(int channel, boolean mitm, boolean min16DigitPin) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(1, true, true, channel, mitm, min16DigitPin);
        int errno = socket.mSocket.bindListen();
        if (channel == -2) {
            socket.setChannel(socket.mSocket.getPort());
        }
        if (errno == 0) {
            return socket;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error: ");
        stringBuilder.append(errno);
        throw new IOException(stringBuilder.toString());
    }

    public BluetoothServerSocket listenUsingRfcommWithServiceRecord(String name, UUID uuid) throws IOException {
        return createNewRfcommSocketAndRecord(name, uuid, true, true);
    }

    public BluetoothServerSocket listenUsingInsecureRfcommWithServiceRecord(String name, UUID uuid) throws IOException {
        SeempLog.record(59);
        return createNewRfcommSocketAndRecord(name, uuid, false, false);
    }

    @UnsupportedAppUsage
    public BluetoothServerSocket listenUsingEncryptedRfcommWithServiceRecord(String name, UUID uuid) throws IOException {
        return createNewRfcommSocketAndRecord(name, uuid, false, true);
    }

    private BluetoothServerSocket createNewRfcommSocketAndRecord(String name, UUID uuid, boolean auth, boolean encrypt) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(1, auth, encrypt, new ParcelUuid(uuid));
        socket.setServiceName(name);
        int errno = socket.mSocket.bindListen();
        if (errno == 0) {
            return socket;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error: ");
        stringBuilder.append(errno);
        throw new IOException(stringBuilder.toString());
    }

    public BluetoothServerSocket listenUsingInsecureRfcommOn(int port) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(1, false, false, port);
        int errno = socket.mSocket.bindListen();
        if (port == -2) {
            socket.setChannel(socket.mSocket.getPort());
        }
        if (errno == 0) {
            return socket;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error: ");
        stringBuilder.append(errno);
        throw new IOException(stringBuilder.toString());
    }

    public BluetoothServerSocket listenUsingEncryptedRfcommOn(int port) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(1, false, true, port);
        int errno = socket.mSocket.bindListen();
        if (port == -2) {
            socket.setChannel(socket.mSocket.getPort());
        }
        if (errno >= 0) {
            return socket;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error: ");
        stringBuilder.append(errno);
        throw new IOException(stringBuilder.toString());
    }

    public static BluetoothServerSocket listenUsingScoOn() throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(2, false, false, -1);
        int errno = socket.mSocket.bindListen();
        return socket;
    }

    public BluetoothServerSocket listenUsingL2capOn(int port, boolean mitm, boolean min16DigitPin) throws IOException {
        StringBuilder stringBuilder;
        BluetoothServerSocket socket = new BluetoothServerSocket(3, true, true, port, mitm, min16DigitPin);
        int errno = socket.mSocket.bindListen();
        if (port == -2) {
            int assignedChannel = socket.mSocket.getPort();
            stringBuilder = new StringBuilder();
            stringBuilder.append("listenUsingL2capOn: set assigned channel to ");
            stringBuilder.append(assignedChannel);
            Log.d(TAG, stringBuilder.toString());
            socket.setChannel(assignedChannel);
        }
        if (errno == 0) {
            return socket;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Error: ");
        stringBuilder.append(errno);
        throw new IOException(stringBuilder.toString());
    }

    public BluetoothServerSocket listenUsingL2capOn(int port) throws IOException {
        return listenUsingL2capOn(port, false, false);
    }

    public BluetoothServerSocket listenUsingInsecureL2capOn(int port) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("listenUsingInsecureL2capOn: port=");
        stringBuilder.append(port);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        BluetoothServerSocket bluetoothServerSocket = new BluetoothServerSocket(3, false, false, port, false, false);
        int errno = bluetoothServerSocket.mSocket.bindListen();
        if (port == -2) {
            int assignedChannel = bluetoothServerSocket.mSocket.getPort();
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("listenUsingInsecureL2capOn: set assigned channel to ");
            stringBuilder3.append(assignedChannel);
            Log.d(str, stringBuilder3.toString());
            bluetoothServerSocket.setChannel(assignedChannel);
        }
        if (errno == 0) {
            return bluetoothServerSocket;
        }
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("Error: ");
        stringBuilder4.append(errno);
        throw new IOException(stringBuilder4.toString());
    }

    public Pair<byte[], byte[]> readOutOfBandData() {
        return null;
    }

    public boolean getProfileProxy(Context context, ServiceListener listener, int profile) {
        if (context == null || listener == null) {
            return false;
        }
        if (profile == 1) {
            BluetoothHeadset headset = new BluetoothHeadset(context, listener);
            return true;
        } else if (profile == 2) {
            BluetoothA2dp a2dp = new BluetoothA2dp(context, listener);
            return true;
        } else if (profile == 11) {
            BluetoothA2dpSink a2dpSink = new BluetoothA2dpSink(context, listener);
            return true;
        } else if (profile == 12) {
            BluetoothAvrcpController avrcp = new BluetoothAvrcpController(context, listener);
            return true;
        } else if (profile == 4) {
            BluetoothHidHost iDev = new BluetoothHidHost(context, listener);
            return true;
        } else if (profile == 5) {
            BluetoothPan pan = new BluetoothPan(context, listener);
            return true;
        } else if (profile == 22) {
            BluetoothDun dun = new BluetoothDun(context, listener);
            return true;
        } else if (profile == 3) {
            Log.e(TAG, "getProfileProxy(): BluetoothHealth is deprecated");
            return false;
        } else if (profile == 9) {
            BluetoothMap map = new BluetoothMap(context, listener);
            return true;
        } else if (profile == 16) {
            BluetoothHeadsetClient headsetClient = new BluetoothHeadsetClient(context, listener);
            return true;
        } else if (profile == 10) {
            BluetoothSap sap = new BluetoothSap(context, listener);
            return true;
        } else if (profile == 17) {
            BluetoothPbapClient pbapClient = new BluetoothPbapClient(context, listener);
            return true;
        } else if (profile == 18) {
            BluetoothMapClient mapClient = new BluetoothMapClient(context, listener);
            return true;
        } else if (profile == 19) {
            BluetoothHidDevice hidDevice = new BluetoothHidDevice(context, listener);
            return true;
        } else if (profile != 21 || !isHearingAidProfileSupported()) {
            return false;
        } else {
            BluetoothHearingAid hearingAid = new BluetoothHearingAid(context, listener);
            return true;
        }
    }

    public void closeProfileProxy(int profile, BluetoothProfile proxy) {
        if (proxy != null) {
            switch (profile) {
                case 1:
                    ((BluetoothHeadset) proxy).close();
                    break;
                case 2:
                    ((BluetoothA2dp) proxy).close();
                    break;
                case 4:
                    ((BluetoothHidHost) proxy).close();
                    break;
                case 5:
                    ((BluetoothPan) proxy).close();
                    break;
                case 7:
                    ((BluetoothGatt) proxy).close();
                    break;
                case 8:
                    ((BluetoothGattServer) proxy).close();
                    break;
                case 9:
                    ((BluetoothMap) proxy).close();
                    break;
                case 10:
                    ((BluetoothSap) proxy).close();
                    break;
                case 11:
                    ((BluetoothA2dpSink) proxy).close();
                    break;
                case 12:
                    ((BluetoothAvrcpController) proxy).close();
                    break;
                case 16:
                    ((BluetoothHeadsetClient) proxy).close();
                    break;
                case 17:
                    ((BluetoothPbapClient) proxy).close();
                    break;
                case 18:
                    ((BluetoothMapClient) proxy).close();
                    break;
                case 19:
                    ((BluetoothHidDevice) proxy).close();
                    break;
                case 21:
                    ((BluetoothHearingAid) proxy).close();
                    break;
                case 22:
                    ((BluetoothDun) proxy).close();
                    break;
            }
        }
    }

    @SystemApi
    public boolean enableNoAutoConnect() {
        boolean isEnabled = isEnabled();
        String str = TAG;
        if (isEnabled) {
            Log.d(str, "enableNoAutoConnect(): BT already enabled!");
            return true;
        }
        try {
            return this.mManagerService.enableNoAutoConnect(ActivityThread.currentPackageName());
        } catch (RemoteException e) {
            Log.e(str, "", e);
            return false;
        }
    }

    public boolean changeApplicationBluetoothState(boolean on, BluetoothStateChangeCallback callback) {
        return false;
    }

    public void unregisterAdapter() {
        try {
            if (this.mManagerService != null) {
                this.mManagerService.unregisterAdapter(this.mManagerCallback);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    private Set<BluetoothDevice> toDeviceSet(BluetoothDevice[] devices) {
        return Collections.unmodifiableSet(new HashSet(Arrays.asList(devices)));
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            this.mManagerService.unregisterAdapter(this.mManagerCallback);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Throwable th) {
            super.finalize();
        }
        super.finalize();
    }

    public static boolean checkBluetoothAddress(String address) {
        if (address == null || address.length() != 17) {
            return false;
        }
        for (int i = 0; i < 17; i++) {
            char c = address.charAt(i);
            int i2 = i % 3;
            if (i2 == 0 || i2 == 1) {
                if ((c < '0' || c > '9') && (c < DateFormat.CAPITAL_AM_PM || c > 'F')) {
                    return false;
                }
            } else if (i2 == 2 && c != ':') {
                return false;
            }
        }
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public IBluetoothManager getBluetoothManager() {
        return this.mManagerService;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public IBluetooth getBluetoothService(IBluetoothManagerCallback cb) {
        synchronized (this.mProxyServiceStateCallbacks) {
            if (cb == null) {
                Log.w(TAG, "getBluetoothService() called with no BluetoothManagerCallback");
            } else if (!this.mProxyServiceStateCallbacks.contains(cb)) {
                this.mProxyServiceStateCallbacks.add(cb);
            }
        }
        return this.mService;
    }

    /* Access modifiers changed, original: 0000 */
    public void removeServiceStateCallback(IBluetoothManagerCallback cb) {
        synchronized (this.mProxyServiceStateCallbacks) {
            this.mProxyServiceStateCallbacks.remove(cb);
        }
    }

    @Deprecated
    public boolean startLeScan(LeScanCallback callback) {
        return startLeScan(null, callback);
    }

    @Deprecated
    public boolean startLeScan(final UUID[] serviceUuids, final LeScanCallback callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("startLeScan(): ");
        stringBuilder.append(Arrays.toString(serviceUuids));
        Log.d(TAG, stringBuilder.toString());
        if (callback == null) {
            Log.e(TAG, "startLeScan: null callback");
            return false;
        }
        BluetoothLeScanner scanner = getBluetoothLeScanner();
        if (scanner == null) {
            Log.e(TAG, "startLeScan: cannot get BluetoothLeScanner");
            return false;
        }
        synchronized (this.mLeScanClients) {
            if (this.mLeScanClients.containsKey(callback)) {
                Log.e(TAG, "LE Scan has already started");
                return false;
            }
            try {
                if (this.mManagerService.getBluetoothGatt() == null) {
                    return false;
                }
                ScanCallback scanCallback = new ScanCallback() {
                    public void onScanResult(int callbackType, ScanResult result) {
                        String str = BluetoothAdapter.TAG;
                        if (callbackType != 1) {
                            Log.e(str, "LE Scan has already started");
                            return;
                        }
                        ScanRecord scanRecord = result.getScanRecord();
                        if (scanRecord != null) {
                            if (serviceUuids != null) {
                                List<ParcelUuid> uuids = new ArrayList();
                                for (UUID uuid : serviceUuids) {
                                    uuids.add(new ParcelUuid(uuid));
                                }
                                List<ParcelUuid> scanServiceUuids = scanRecord.getServiceUuids();
                                if (scanServiceUuids == null || !scanServiceUuids.containsAll(uuids)) {
                                    Log.d(str, "uuids does not match");
                                    return;
                                }
                            }
                            callback.onLeScan(result.getDevice(), result.getRssi(), scanRecord.getBytes());
                        }
                    }
                };
                ScanSettings settings = new Builder().setCallbackType(1).setScanMode(2).build();
                List filters = new ArrayList();
                if (serviceUuids != null && serviceUuids.length > 0) {
                    filters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(serviceUuids[0])).build());
                }
                scanner.startScan(filters, settings, scanCallback);
                this.mLeScanClients.put(callback, scanCallback);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
                return false;
            }
        }
    }

    @Deprecated
    public void stopLeScan(LeScanCallback callback) {
        Log.d(TAG, "stopLeScan()");
        BluetoothLeScanner scanner = getBluetoothLeScanner();
        if (scanner != null) {
            synchronized (this.mLeScanClients) {
                ScanCallback scanCallback = (ScanCallback) this.mLeScanClients.remove(callback);
                if (scanCallback == null) {
                    Log.d(TAG, "scan not started yet");
                    return;
                }
                scanner.stopScan(scanCallback);
            }
        }
    }

    public BluetoothServerSocket listenUsingL2capChannel() throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(4, true, true, -2, false, false);
        int errno = socket.mSocket.bindListen();
        StringBuilder stringBuilder;
        if (errno == 0) {
            int assignedPsm = socket.mSocket.getPort();
            if (assignedPsm != 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("listenUsingL2capChannel: set assigned PSM to ");
                stringBuilder.append(assignedPsm);
                Log.d(TAG, stringBuilder.toString());
                socket.setChannel(assignedPsm);
                return socket;
            }
            throw new IOException("Error: Unable to assign PSM value");
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Error: ");
        stringBuilder.append(errno);
        throw new IOException(stringBuilder.toString());
    }

    public BluetoothServerSocket listenUsingL2capCoc(int transport) throws IOException {
        Log.e(TAG, "listenUsingL2capCoc: PLEASE USE THE OFFICIAL API, listenUsingL2capChannel");
        return listenUsingL2capChannel();
    }

    public BluetoothServerSocket listenUsingInsecureL2capChannel() throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(4, false, false, -2, false, false);
        int errno = socket.mSocket.bindListen();
        StringBuilder stringBuilder;
        if (errno == 0) {
            int assignedPsm = socket.mSocket.getPort();
            if (assignedPsm != 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("listenUsingInsecureL2capChannel: set assigned PSM to ");
                stringBuilder.append(assignedPsm);
                Log.d(TAG, stringBuilder.toString());
                socket.setChannel(assignedPsm);
                return socket;
            }
            throw new IOException("Error: Unable to assign PSM value");
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Error: ");
        stringBuilder.append(errno);
        throw new IOException(stringBuilder.toString());
    }

    public BluetoothServerSocket listenUsingInsecureL2capCoc(int transport) throws IOException {
        Log.e(TAG, "listenUsingInsecureL2capCoc: PLEASE USE THE OFFICIAL API, listenUsingInsecureL2capChannel");
        return listenUsingInsecureL2capChannel();
    }

    @SystemApi
    public boolean addOnMetadataChangedListener(BluetoothDevice device, Executor executor, OnMetadataChangedListener listener) {
        Log.d(TAG, "addOnMetadataChangedListener()");
        IBluetooth service = this.mService;
        if (service == null) {
            Log.e(TAG, "Bluetooth is not enabled. Cannot register metadata listener");
            return false;
        } else if (listener == null) {
            throw new NullPointerException("listener is null");
        } else if (device == null) {
            throw new NullPointerException("device is null");
        } else if (executor != null) {
            boolean ret;
            synchronized (sMetadataListeners) {
                List<Pair<OnMetadataChangedListener, Executor>> listenerList = (List) sMetadataListeners.get(device);
                if (listenerList == null) {
                    listenerList = new ArrayList();
                    sMetadataListeners.put(device, listenerList);
                } else if (listenerList.stream().anyMatch(new -$$Lambda$BluetoothAdapter$3qDRCAtPJRu3UbUEFsHnCxkioak(listener))) {
                    throw new IllegalArgumentException("listener was already regestered for the device");
                }
                Pair<OnMetadataChangedListener, Executor> listenerPair = new Pair(listener, executor);
                listenerList.add(listenerPair);
                ret = false;
                Map map;
                try {
                    ret = service.registerMetadataListener(sBluetoothMetadataListener, device);
                    if (!ret) {
                        listenerList.remove(listenerPair);
                        if (listenerList.isEmpty()) {
                            map = sMetadataListeners;
                            map.remove(device);
                        }
                    }
                } catch (RemoteException e) {
                    try {
                        Log.e(TAG, "registerMetadataListener fail", e);
                        if (null == null) {
                            listenerList.remove(listenerPair);
                            if (listenerList.isEmpty()) {
                                map = sMetadataListeners;
                            }
                        }
                    } catch (Throwable th) {
                        if (null == null) {
                            listenerList.remove(listenerPair);
                            if (listenerList.isEmpty()) {
                                sMetadataListeners.remove(device);
                            }
                        }
                    }
                }
            }
            return ret;
        } else {
            throw new NullPointerException("executor is null");
        }
    }

    @SystemApi
    public boolean removeOnMetadataChangedListener(BluetoothDevice device, OnMetadataChangedListener listener) {
        Log.d(TAG, "removeOnMetadataChangedListener()");
        if (device == null) {
            throw new NullPointerException("device is null");
        } else if (listener != null) {
            synchronized (sMetadataListeners) {
                if (sMetadataListeners.containsKey(device)) {
                    ((List) sMetadataListeners.get(device)).removeIf(new -$$Lambda$BluetoothAdapter$dhiyWTssvWZcLytIeq61ARYDHrc(listener));
                    if (((List) sMetadataListeners.get(device)).isEmpty()) {
                        sMetadataListeners.remove(device);
                        IBluetooth service = this.mService;
                        if (service == null) {
                            return true;
                        }
                        try {
                            boolean unregisterMetadataListener = service.unregisterMetadataListener(device);
                            return unregisterMetadataListener;
                        } catch (RemoteException e) {
                            Log.e(TAG, "unregisterMetadataListener fail", e);
                            return false;
                        }
                    }
                    return true;
                }
                throw new IllegalArgumentException("device was not registered");
            }
        } else {
            throw new NullPointerException("listener is null");
        }
    }
}
