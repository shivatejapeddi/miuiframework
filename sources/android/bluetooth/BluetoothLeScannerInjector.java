package android.bluetooth;

import android.accounts.GrantCredentialsPermissionActivity;
import android.app.ActivityThread;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.le.IScannerCallback.Stub;
import android.bluetooth.le.ResultStorageDescriptor;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.WorkSource;
import android.util.Log;
import com.miui.whetstone.server.IWhetstoneActivityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothLeScannerInjector {
    private static final boolean DBG = Log.isLoggable("power.bluetooth", 3);
    private static final String TAG = "BluetoothLeScannerInjector";
    private static final Map<LeScanCallback, BleScanStatisticsCallbackWrapper> sLeScanStatisticsClients = new HashMap();

    private static class BleScanStatisticsCallbackWrapper extends Stub {
        private IBluetoothGatt mBluetoothGatt;
        private final ScanCallback mScanCallback;

        public BleScanStatisticsCallbackWrapper(IBluetoothGatt bluetoothGatt, ScanCallback scanCallback) {
            this.mBluetoothGatt = bluetoothGatt;
            this.mScanCallback = scanCallback;
        }

        public void startBleScanStatistics() {
            try {
                this.mBluetoothGatt.registerStatisticsClient(this);
            } catch (RemoteException e) {
                Log.e(BluetoothLeScannerInjector.TAG, "Failed to start ble scan statistics.", e);
            }
        }

        public void stopBleScanStatistics() {
            try {
                this.mBluetoothGatt.unregisterStatisticsClient(this);
            } catch (RemoteException e) {
                Log.e(BluetoothLeScannerInjector.TAG, "Failed to stop ble scan statistics.", e);
            }
        }

        public void onScanResult(ScanResult scanResult) {
            this.mScanCallback.onScanResult(1, scanResult);
        }

        public void onScannerRegistered(int status, int scannerId) {
        }

        public void onBatchScanResults(List<ScanResult> list) {
        }

        public void onFoundOrLost(boolean onFound, ScanResult scanResult) {
        }

        public void onScanManagerErrorCallback(int errorCode) {
        }
    }

    public static boolean isLeScanAllowed() {
        return isLeScanAllowedInternal(Process.myUid());
    }

    private static boolean isLeScanAllowedInternal(int uid) {
        try {
            IWhetstoneActivityManager ws = IWhetstoneActivityManager.Stub.asInterface(ServiceManager.getService("whetstone.activity"));
            if (ws != null) {
                return ws.getPowerKeeperPolicy().isLeScanAllowed(uid);
            }
            return true;
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("isLeScanAllowedInternal error=");
            stringBuilder.append(e);
            Log.w(TAG, stringBuilder.toString());
            return true;
        }
    }

    public static void startLeScan(IBluetoothGatt gatt, int clientIf, ScanSettings settings, List<ScanFilter> filters, WorkSource workSource, List<List<ResultStorageDescriptor>> resultStorages, IBinder iBinder) throws RemoteException {
        if (clientIf <= 0) {
            Log.w(TAG, "startLeScan invalid clientIf");
        } else {
            startLeScan(gatt, clientIf, null, settings, filters, workSource, resultStorages, iBinder);
        }
    }

    public static void startLeScan(IBluetoothGatt gatt, PendingIntent callbackIntent, ScanSettings settings, List<ScanFilter> filters, IBinder iBinder) throws RemoteException {
        if (callbackIntent == null) {
            Log.w(TAG, "startLeScan invalid callbackIntent");
        } else {
            startLeScan(gatt, -1, callbackIntent, settings, filters, null, null, iBinder);
        }
    }

    private static void startLeScan(IBluetoothGatt gatt, int clientIf, PendingIntent callbackIntent, ScanSettings settings, List<ScanFilter> filters, WorkSource workSource, List<List<ResultStorageDescriptor>> resultStorages, IBinder iBinder) throws RemoteException {
        BleScanWrapper bleScanWrapper;
        int i = clientIf;
        PendingIntent pendingIntent = callbackIntent;
        ScanSettings scanSettings = settings;
        List<ScanFilter> list = filters;
        IBinder iBinder2 = iBinder;
        int uid = Process.myUid();
        String opPackageName = ActivityThread.currentOpPackageName();
        boolean isAllowed = isLeScanAllowedInternal(uid);
        boolean z = DBG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("startLeScan uid=");
            stringBuilder.append(uid);
            stringBuilder.append(" isAllowed=");
            stringBuilder.append(isAllowed);
            stringBuilder.append(" clientIf=");
            stringBuilder.append(i);
            stringBuilder.append(" pi=");
            stringBuilder.append(pendingIntent);
            stringBuilder.append(" pkg=");
            stringBuilder.append(opPackageName);
            stringBuilder.append(" iBinder=");
            stringBuilder.append(iBinder2);
            Log.d(str, stringBuilder.toString());
        }
        IBluetoothGatt iBluetoothGatt;
        if (pendingIntent != null) {
            if (isAllowed) {
                gatt.startScanForIntent(pendingIntent, scanSettings, list, opPackageName);
            } else {
                iBluetoothGatt = gatt;
            }
            bleScanWrapper = new BleScanWrapper(pendingIntent, scanSettings, list, opPackageName);
        } else {
            iBluetoothGatt = gatt;
            if (i > 0) {
                if (isAllowed) {
                    gatt.startScan(clientIf, settings, filters, resultStorages, opPackageName);
                }
                bleScanWrapper = new BleScanWrapper(clientIf, settings, filters, workSource, resultStorages, opPackageName);
            } else {
                Log.w(str, "startLeScan shoud not access here");
                return;
            }
        }
        if (iBinder2 == null) {
            iBinder2 = new Binder();
        }
        startLeScanInternal(uid, bleScanWrapper, iBinder2);
    }

    public static void startLeScan(int clientIf, ScanSettings settings, List<ScanFilter> filters, WorkSource workSource, List<List<ResultStorageDescriptor>> resultStorages, String opPackageName, IBinder iBinder) {
        startLeScanInternal(Process.myUid(), new BleScanWrapper(clientIf, settings, filters, workSource, resultStorages, opPackageName), iBinder);
    }

    private static void startLeScanInternal(int uid, BleScanWrapper bleScanWrapper, IBinder iBinder) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(GrantCredentialsPermissionActivity.EXTRAS_REQUESTING_UID, uid);
            bundle.putParcelable("BleScanWrapper", bleScanWrapper);
            bundle.putBinder("IBinder", iBinder);
            IWhetstoneActivityManager ws = IWhetstoneActivityManager.Stub.asInterface(ServiceManager.getService("whetstone.activity"));
            if (ws != null) {
                ws.getPowerKeeperPolicy().startLeScan(bundle);
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("startLeScanInternal uid=");
            stringBuilder.append(uid);
            stringBuilder.append(" bleScanWrapper=");
            stringBuilder.append(bleScanWrapper);
            stringBuilder.append(e);
            Log.w(TAG, stringBuilder.toString());
        }
    }

    public static void stopLeScan(PendingIntent callbackIntent, IBinder iBinder) {
        int uid = Process.myUid();
        if (callbackIntent == null) {
            Log.w(TAG, "stopLeScan invalid callbackIntent");
        } else {
            stopLeScanInternal(uid, new BleScanWrapper(callbackIntent, null, null, null), iBinder);
        }
    }

    public static void stopLeScan(int clientIf, IBinder iBinder) {
        stopLeScanInternal(Process.myUid(), new BleScanWrapper(clientIf, null, null, null, null, null), iBinder);
    }

    private static void stopLeScanInternal(int uid, BleScanWrapper bleScanWrapper, IBinder iBinder) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(GrantCredentialsPermissionActivity.EXTRAS_REQUESTING_UID, uid);
            bundle.putParcelable("BleScanWrapper", bleScanWrapper);
            bundle.putBinder("IBinder", iBinder);
            IWhetstoneActivityManager ws = IWhetstoneActivityManager.Stub.asInterface(ServiceManager.getService("whetstone.activity"));
            if (ws != null) {
                ws.getPowerKeeperPolicy().stopLeScan(bundle);
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("stopLeScanInternal uid=");
            stringBuilder.append(uid);
            stringBuilder.append(" bleScanWrapper=");
            stringBuilder.append(bleScanWrapper);
            stringBuilder.append(e);
            Log.w(TAG, stringBuilder.toString());
        }
    }

    public static void cleanupLeScanStatistics() {
        Map map = sLeScanStatisticsClients;
        if (map != null) {
            map.clear();
        }
    }

    public static boolean startLeScanStatistics(IBluetoothManager btManager, final LeScanCallback callback) {
        Log.d(TAG, "startLeScanStatistics()");
        if (callback == null) {
            Log.e(TAG, "startLeScanStatistics: null callback");
            return false;
        }
        synchronized (sLeScanStatisticsClients) {
            try {
                IBluetoothGatt iGatt = btManager.getBluetoothGatt();
                if (iGatt == null) {
                    return false;
                }
                BleScanStatisticsCallbackWrapper wrapper = new BleScanStatisticsCallbackWrapper(iGatt, new ScanCallback() {
                    public void onScanResult(int callbackType, ScanResult result) {
                        ScanRecord scanRecord = result.getScanRecord();
                        if (scanRecord != null) {
                            callback.onLeScan(result.getDevice(), result.getRssi(), scanRecord.getBytes());
                        }
                    }
                });
                wrapper.startBleScanStatistics();
                sLeScanStatisticsClients.put(callback, wrapper);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
                return false;
            } catch (Throwable th) {
            }
        }
    }

    public static void stopLeScanStatistics(LeScanCallback callback) {
        Log.d(TAG, "stopLeScanStatistics()");
        synchronized (sLeScanStatisticsClients) {
            BleScanStatisticsCallbackWrapper wrapper = (BleScanStatisticsCallbackWrapper) sLeScanStatisticsClients.remove(callback);
            if (wrapper == null) {
                Log.d(TAG, "no ble scan statistics callback found.");
                return;
            }
            wrapper.stopBleScanStatistics();
        }
    }
}
