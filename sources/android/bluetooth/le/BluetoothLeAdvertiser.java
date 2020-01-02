package android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothUuid;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.bluetooth.le.AdvertisingSetParameters.Builder;
import android.bluetooth.le.IAdvertisingSetCallback.Stub;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class BluetoothLeAdvertiser {
    private static final int FLAGS_FIELD_BYTES = 3;
    private static final int MANUFACTURER_SPECIFIC_DATA_LENGTH = 2;
    private static final int MAX_ADVERTISING_DATA_BYTES = 1650;
    private static final int MAX_LEGACY_ADVERTISING_DATA_BYTES = 31;
    private static final int OVERHEAD_BYTES_PER_FIELD = 2;
    private static final String TAG = "BluetoothLeAdvertiser";
    private final Map<Integer, AdvertisingSet> mAdvertisingSets = Collections.synchronizedMap(new HashMap());
    private BluetoothAdapter mBluetoothAdapter;
    private final IBluetoothManager mBluetoothManager;
    private final Map<AdvertisingSetCallback, IAdvertisingSetCallback> mCallbackWrappers = Collections.synchronizedMap(new HashMap());
    private final Handler mHandler;
    private final Map<AdvertiseCallback, AdvertisingSetCallback> mLegacyAdvertisers = new HashMap();

    public BluetoothLeAdvertiser(IBluetoothManager bluetoothManager) {
        this.mBluetoothManager = bluetoothManager;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public void startAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseCallback callback) {
        startAdvertising(settings, advertiseData, null, callback);
    }

    public void startAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseData scanResponse, AdvertiseCallback callback) {
        Throwable th;
        AdvertiseCallback advertiseCallback = callback;
        synchronized (this.mLegacyAdvertisers) {
            AdvertiseData advertiseData2;
            AdvertiseData advertiseData3;
            try {
                BluetoothLeUtils.checkAdapterStateOn(this.mBluetoothAdapter);
                if (advertiseCallback != null) {
                    boolean isConnectable = settings.isConnectable();
                    try {
                        boolean z = true;
                        if (totalBytes(advertiseData, isConnectable) > 31) {
                            advertiseData2 = scanResponse;
                        } else if (totalBytes(scanResponse, false) <= 31) {
                            if (this.mLegacyAdvertisers.containsKey(advertiseCallback)) {
                                postStartFailure(advertiseCallback, 3);
                                return;
                            }
                            int duration;
                            Builder parameters = new Builder();
                            parameters.setLegacyMode(true);
                            parameters.setConnectable(isConnectable);
                            parameters.setScannable(true);
                            if (settings.getMode() == 0) {
                                parameters.setInterval(1600);
                            } else if (settings.getMode() == 1) {
                                parameters.setInterval(400);
                            } else if (settings.getMode() == 2) {
                                parameters.setInterval(160);
                            }
                            if (settings.getTxPowerLevel() == 0) {
                                parameters.setTxPowerLevel(-21);
                            } else if (settings.getTxPowerLevel() == 1) {
                                parameters.setTxPowerLevel(-15);
                            } else if (settings.getTxPowerLevel() == 2) {
                                parameters.setTxPowerLevel(-7);
                            } else if (settings.getTxPowerLevel() == 3) {
                                parameters.setTxPowerLevel(1);
                            }
                            int timeoutMillis = settings.getTimeout();
                            if (timeoutMillis > 0) {
                                if (timeoutMillis >= 10) {
                                    z = timeoutMillis / 10;
                                }
                                duration = z;
                            } else {
                                duration = 0;
                            }
                            AdvertisingSetCallback wrapped = wrapOldCallback(advertiseCallback, settings);
                            this.mLegacyAdvertisers.put(advertiseCallback, wrapped);
                            startAdvertisingSet(parameters.build(), advertiseData, scanResponse, null, null, duration, 0, wrapped);
                            return;
                        }
                        postStartFailure(advertiseCallback, 1);
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
                advertiseData3 = advertiseData;
                advertiseData2 = scanResponse;
                throw new IllegalArgumentException("callback cannot be null");
            } catch (Throwable th3) {
                th = th3;
                advertiseData3 = advertiseData;
                advertiseData2 = scanResponse;
                throw th;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public AdvertisingSetCallback wrapOldCallback(final AdvertiseCallback callback, final AdvertiseSettings settings) {
        return new AdvertisingSetCallback() {
            public void onAdvertisingSetStarted(AdvertisingSet advertisingSet, int txPower, int status) {
                if (status != 0) {
                    BluetoothLeAdvertiser.this.postStartFailure(callback, status);
                } else {
                    BluetoothLeAdvertiser.this.postStartSuccess(callback, settings);
                }
            }

            public void onAdvertisingEnabled(AdvertisingSet advertisingSet, boolean enabled, int status) {
                if (enabled) {
                    Log.e(BluetoothLeAdvertiser.TAG, "Legacy advertiser should be only disabled on timeout, but was enabled!");
                } else {
                    BluetoothLeAdvertiser.this.stopAdvertising(callback);
                }
            }
        };
    }

    public void stopAdvertising(AdvertiseCallback callback) {
        synchronized (this.mLegacyAdvertisers) {
            if (callback != null) {
                try {
                    AdvertisingSetCallback wrapper = (AdvertisingSetCallback) this.mLegacyAdvertisers.get(callback);
                    if (wrapper == null) {
                        return;
                    }
                    stopAdvertisingSet(wrapper);
                    this.mLegacyAdvertisers.remove(callback);
                } catch (Throwable th) {
                }
            } else {
                throw new IllegalArgumentException("callback cannot be null");
            }
        }
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, AdvertisingSetCallback callback) {
        startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, 0, 0, callback, new Handler(Looper.getMainLooper()));
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, AdvertisingSetCallback callback, Handler handler) {
        startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, 0, 0, callback, handler);
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, int duration, int maxExtendedAdvertisingEvents, AdvertisingSetCallback callback) {
        startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, duration, maxExtendedAdvertisingEvents, callback, new Handler(Looper.getMainLooper()));
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, int duration, int maxExtendedAdvertisingEvents, AdvertisingSetCallback callback, Handler handler) {
        boolean supportCodedPhy;
        Handler handler2;
        AdvertiseData advertiseData2 = advertiseData;
        AdvertiseData advertiseData3 = scanResponse;
        boolean z = duration;
        boolean z2 = maxExtendedAdvertisingEvents;
        AdvertisingSetCallback advertisingSetCallback = callback;
        Handler handler3 = handler;
        String str = TAG;
        BluetoothLeUtils.checkAdapterStateOn(this.mBluetoothAdapter);
        if (advertisingSetCallback != null) {
            boolean isConnectable = parameters.isConnectable();
            if (!parameters.isLegacy()) {
                supportCodedPhy = this.mBluetoothAdapter.isLeCodedPhySupported();
                boolean support2MPhy = this.mBluetoothAdapter.isLe2MPhySupported();
                int pphy = parameters.getPrimaryPhy();
                int sphy = parameters.getSecondaryPhy();
                if (pphy == 3 && !supportCodedPhy) {
                    throw new IllegalArgumentException("Unsupported primary PHY selected");
                } else if ((sphy != 3 || supportCodedPhy) && (sphy != 2 || support2MPhy)) {
                    int maxData = this.mBluetoothAdapter.getLeMaximumAdvertisingDataLength();
                    if (totalBytes(advertiseData2, isConnectable) > maxData) {
                        throw new IllegalArgumentException("Advertising data too big");
                    } else if (totalBytes(advertiseData3, false) > maxData) {
                        throw new IllegalArgumentException("Scan response data too big");
                    } else if (totalBytes(periodicData, false) <= maxData) {
                        boolean supportPeriodic = this.mBluetoothAdapter.isLePeriodicAdvertisingSupported();
                        if (periodicParameters == null) {
                        } else if (!supportPeriodic) {
                            throw new IllegalArgumentException("Controller does not support LE Periodic Advertising");
                        }
                    } else {
                        throw new IllegalArgumentException("Periodic advertising data too big");
                    }
                } else {
                    throw new IllegalArgumentException("Unsupported secondary PHY selected");
                }
            } else if (totalBytes(advertiseData2, isConnectable) > 31) {
                throw new IllegalArgumentException("Legacy advertising data too big");
            } else if (totalBytes(advertiseData3, false) <= 31) {
                AdvertiseData advertiseData4 = periodicData;
            } else {
                throw new IllegalArgumentException("Legacy scan response data too big");
            }
            StringBuilder stringBuilder;
            if (z2 >= false || z2 > true) {
                handler2 = handler3;
                stringBuilder = new StringBuilder();
                stringBuilder.append("maxExtendedAdvertisingEvents out of range: ");
                stringBuilder.append(z2);
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (z2 && !this.mBluetoothAdapter.isLePeriodicAdvertisingSupported()) {
                throw new IllegalArgumentException("Can't use maxExtendedAdvertisingEvents with controller that don't support LE Extended Advertising");
            } else if (z >= false || z > true) {
                handler2 = handler3;
                stringBuilder = new StringBuilder();
                stringBuilder.append("duration out of range: ");
                stringBuilder.append(z);
                throw new IllegalArgumentException(stringBuilder.toString());
            } else {
                try {
                    IBluetoothGatt gatt = this.mBluetoothManager.getBluetoothGatt();
                    if (gatt == null) {
                        Log.e(str, "Bluetooth GATT is null");
                        postStartSetFailure(handler3, advertisingSetCallback, 4);
                        return;
                    }
                    IAdvertisingSetCallback wrapped = wrap(advertisingSetCallback, handler3);
                    if (this.mCallbackWrappers.putIfAbsent(advertisingSetCallback, wrapped)) {
                        throw new IllegalArgumentException("callback instance already associated with advertising");
                    }
                    int i = 4;
                    String str2 = str;
                    handler2 = handler3;
                    try {
                        gatt.startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, duration, maxExtendedAdvertisingEvents, wrapped);
                        return;
                    } catch (RemoteException supportCodedPhy2) {
                        Log.e(str2, "Failed to start advertising set - ", supportCodedPhy2);
                        postStartSetFailure(handler2, advertisingSetCallback, 4);
                        return;
                    }
                } catch (RemoteException supportCodedPhy22) {
                    boolean z3 = isConnectable;
                    handler2 = handler3;
                    Log.e(str, "Failed to get Bluetooth GATT - ", supportCodedPhy22);
                    postStartSetFailure(handler2, advertisingSetCallback, 4);
                    return;
                }
            }
        }
        handler2 = handler3;
        throw new IllegalArgumentException("callback cannot be null");
    }

    public void stopAdvertisingSet(AdvertisingSetCallback callback) {
        if (callback != null) {
            IAdvertisingSetCallback wrapped = (IAdvertisingSetCallback) this.mCallbackWrappers.remove(callback);
            if (wrapped != null) {
                try {
                    this.mBluetoothManager.getBluetoothGatt().stopAdvertisingSet(wrapped);
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to stop advertising - ", e);
                }
                return;
            }
            return;
        }
        throw new IllegalArgumentException("callback cannot be null");
    }

    public void cleanup() {
        this.mLegacyAdvertisers.clear();
        this.mCallbackWrappers.clear();
        this.mAdvertisingSets.clear();
    }

    private int totalBytes(AdvertiseData data, boolean isFlagsIncluded) {
        int size = 0;
        if (data == null) {
            return 0;
        }
        int num16BitUuids;
        if (isFlagsIncluded) {
            size = 3;
        }
        if (data.getServiceUuids() != null) {
            num16BitUuids = 0;
            int num32BitUuids = 0;
            int num128BitUuids = 0;
            for (ParcelUuid uuid : data.getServiceUuids()) {
                if (BluetoothUuid.is16BitUuid(uuid)) {
                    num16BitUuids++;
                } else if (BluetoothUuid.is32BitUuid(uuid)) {
                    num32BitUuids++;
                } else {
                    num128BitUuids++;
                }
            }
            if (num16BitUuids != 0) {
                size += (num16BitUuids * 2) + 2;
            }
            if (num32BitUuids != 0) {
                size += (num32BitUuids * 4) + 2;
            }
            if (num128BitUuids != 0) {
                size += (num128BitUuids * 16) + 2;
            }
        }
        for (ParcelUuid uuid2 : data.getServiceData().keySet()) {
            size += (BluetoothUuid.uuidToBytes(uuid2).length + 2) + byteLength((byte[]) data.getServiceData().get(uuid2));
        }
        for (num16BitUuids = 0; num16BitUuids < data.getManufacturerSpecificData().size(); num16BitUuids++) {
            size += byteLength((byte[]) data.getManufacturerSpecificData().valueAt(num16BitUuids)) + 4;
        }
        if (data.getIncludeTxPowerLevel()) {
            size += 3;
        }
        if (data.getIncludeDeviceName() && this.mBluetoothAdapter.getName() != null) {
            size += this.mBluetoothAdapter.getName().length() + 2;
        }
        return size;
    }

    private int byteLength(byte[] array) {
        return array == null ? 0 : array.length;
    }

    /* Access modifiers changed, original: 0000 */
    public IAdvertisingSetCallback wrap(final AdvertisingSetCallback callback, final Handler handler) {
        return new Stub() {
            public void onAdvertisingSetStarted(final int advertiserId, final int txPower, final int status) {
                handler.post(new Runnable() {
                    public void run() {
                        if (status != 0) {
                            callback.onAdvertisingSetStarted(null, 0, status);
                            BluetoothLeAdvertiser.this.mCallbackWrappers.remove(callback);
                            return;
                        }
                        AdvertisingSet advertisingSet = new AdvertisingSet(advertiserId, BluetoothLeAdvertiser.this.mBluetoothManager);
                        BluetoothLeAdvertiser.this.mAdvertisingSets.put(Integer.valueOf(advertiserId), advertisingSet);
                        callback.onAdvertisingSetStarted(advertisingSet, txPower, status);
                    }
                });
            }

            public void onOwnAddressRead(final int advertiserId, final int addressType, final String address) {
                handler.post(new Runnable() {
                    public void run() {
                        callback.onOwnAddressRead((AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId)), addressType, address);
                    }
                });
            }

            public void onAdvertisingSetStopped(final int advertiserId) {
                handler.post(new Runnable() {
                    public void run() {
                        callback.onAdvertisingSetStopped((AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId)));
                        BluetoothLeAdvertiser.this.mAdvertisingSets.remove(Integer.valueOf(advertiserId));
                        BluetoothLeAdvertiser.this.mCallbackWrappers.remove(callback);
                    }
                });
            }

            public void onAdvertisingEnabled(final int advertiserId, final boolean enabled, final int status) {
                handler.post(new Runnable() {
                    public void run() {
                        callback.onAdvertisingEnabled((AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId)), enabled, status);
                    }
                });
            }

            public void onAdvertisingDataSet(final int advertiserId, final int status) {
                handler.post(new Runnable() {
                    public void run() {
                        callback.onAdvertisingDataSet((AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId)), status);
                    }
                });
            }

            public void onScanResponseDataSet(final int advertiserId, final int status) {
                handler.post(new Runnable() {
                    public void run() {
                        callback.onScanResponseDataSet((AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId)), status);
                    }
                });
            }

            public void onAdvertisingParametersUpdated(final int advertiserId, final int txPower, final int status) {
                handler.post(new Runnable() {
                    public void run() {
                        callback.onAdvertisingParametersUpdated((AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId)), txPower, status);
                    }
                });
            }

            public void onPeriodicAdvertisingParametersUpdated(final int advertiserId, final int status) {
                handler.post(new Runnable() {
                    public void run() {
                        callback.onPeriodicAdvertisingParametersUpdated((AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId)), status);
                    }
                });
            }

            public void onPeriodicAdvertisingDataSet(final int advertiserId, final int status) {
                handler.post(new Runnable() {
                    public void run() {
                        callback.onPeriodicAdvertisingDataSet((AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId)), status);
                    }
                });
            }

            public void onPeriodicAdvertisingEnabled(final int advertiserId, final boolean enable, final int status) {
                handler.post(new Runnable() {
                    public void run() {
                        callback.onPeriodicAdvertisingEnabled((AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId)), enable, status);
                    }
                });
            }
        };
    }

    private void postStartSetFailure(Handler handler, final AdvertisingSetCallback callback, final int error) {
        handler.post(new Runnable() {
            public void run() {
                callback.onAdvertisingSetStarted(null, 0, error);
            }
        });
    }

    private void postStartFailure(final AdvertiseCallback callback, final int error) {
        this.mHandler.post(new Runnable() {
            public void run() {
                callback.onStartFailure(error);
            }
        });
    }

    private void postStartSuccess(final AdvertiseCallback callback, final AdvertiseSettings settings) {
        this.mHandler.post(new Runnable() {
            public void run() {
                callback.onStartSuccess(settings);
            }
        });
    }
}
