package android.bluetooth.le;

import android.annotation.SystemApi;
import android.app.ActivityThread;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothLeScannerInjector;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.bluetooth.le.IScannerCallback.Stub;
import android.bluetooth.le.ScanSettings.Builder;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.WorkSource;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BluetoothLeScanner {
    private static final boolean DBG = true;
    public static final String EXTRA_CALLBACK_TYPE = "android.bluetooth.le.extra.CALLBACK_TYPE";
    public static final String EXTRA_ERROR_CODE = "android.bluetooth.le.extra.ERROR_CODE";
    public static final String EXTRA_LIST_SCAN_RESULT = "android.bluetooth.le.extra.LIST_SCAN_RESULT";
    private static final String TAG = "BluetoothLeScanner";
    private static final boolean VDBG = false;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final IBluetoothManager mBluetoothManager;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Map<ScanCallback, BleScanCallbackWrapper> mLeScanClients = new HashMap();

    private class BleScanCallbackWrapper extends Stub {
        private static final int REGISTRATION_CALLBACK_TIMEOUT_MILLIS = 2000;
        private IBluetoothGatt mBluetoothGatt;
        private final List<ScanFilter> mFilters;
        private List<List<ResultStorageDescriptor>> mResultStorages;
        private final ScanCallback mScanCallback;
        private int mScannerId = 0;
        private ScanSettings mSettings;
        private final WorkSource mWorkSource;

        public BleScanCallbackWrapper(IBluetoothGatt bluetoothGatt, List<ScanFilter> filters, ScanSettings settings, WorkSource workSource, ScanCallback scanCallback, List<List<ResultStorageDescriptor>> resultStorages) {
            this.mBluetoothGatt = bluetoothGatt;
            this.mFilters = filters;
            this.mSettings = settings;
            this.mWorkSource = workSource;
            this.mScanCallback = scanCallback;
            this.mResultStorages = resultStorages;
        }

        /* JADX WARNING: Missing block: B:24:0x004e, code skipped:
            return;
     */
        /* JADX WARNING: Missing block: B:26:0x0050, code skipped:
            return;
     */
        public void startRegistration() {
            /*
            r6 = this;
            monitor-enter(r6);
            r0 = r6.mScannerId;	 Catch:{ all -> 0x0051 }
            r1 = -1;
            if (r0 == r1) goto L_0x004f;
        L_0x0006:
            r0 = r6.mScannerId;	 Catch:{ all -> 0x0051 }
            r2 = -2;
            if (r0 != r2) goto L_0x000c;
        L_0x000b:
            goto L_0x004f;
        L_0x000c:
            r0 = r6.mBluetoothGatt;	 Catch:{ RemoteException | InterruptedException -> 0x0019, RemoteException | InterruptedException -> 0x0019 }
            r3 = r6.mWorkSource;	 Catch:{ RemoteException | InterruptedException -> 0x0019, RemoteException | InterruptedException -> 0x0019 }
            r0.registerScanner(r6, r3);	 Catch:{ RemoteException | InterruptedException -> 0x0019, RemoteException | InterruptedException -> 0x0019 }
            r3 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
            r6.wait(r3);	 Catch:{ RemoteException | InterruptedException -> 0x0019, RemoteException | InterruptedException -> 0x0019 }
            goto L_0x0029;
        L_0x0019:
            r0 = move-exception;
            r3 = "BluetoothLeScanner";
            r4 = "application registeration exception";
            android.util.Log.e(r3, r4, r0);	 Catch:{ all -> 0x0051 }
            r3 = android.bluetooth.le.BluetoothLeScanner.this;	 Catch:{ all -> 0x0051 }
            r4 = r6.mScanCallback;	 Catch:{ all -> 0x0051 }
            r5 = 3;
            r3.postCallbackError(r4, r5);	 Catch:{ all -> 0x0051 }
        L_0x0029:
            r0 = r6.mScannerId;	 Catch:{ all -> 0x0051 }
            if (r0 <= 0) goto L_0x0039;
        L_0x002d:
            r0 = android.bluetooth.le.BluetoothLeScanner.this;	 Catch:{ all -> 0x0051 }
            r0 = r0.mLeScanClients;	 Catch:{ all -> 0x0051 }
            r1 = r6.mScanCallback;	 Catch:{ all -> 0x0051 }
            r0.put(r1, r6);	 Catch:{ all -> 0x0051 }
            goto L_0x004d;
        L_0x0039:
            r0 = r6.mScannerId;	 Catch:{ all -> 0x0051 }
            if (r0 != 0) goto L_0x003f;
        L_0x003d:
            r6.mScannerId = r1;	 Catch:{ all -> 0x0051 }
        L_0x003f:
            r0 = r6.mScannerId;	 Catch:{ all -> 0x0051 }
            if (r0 != r2) goto L_0x0045;
        L_0x0043:
            monitor-exit(r6);	 Catch:{ all -> 0x0051 }
            return;
        L_0x0045:
            r0 = android.bluetooth.le.BluetoothLeScanner.this;	 Catch:{ all -> 0x0051 }
            r1 = r6.mScanCallback;	 Catch:{ all -> 0x0051 }
            r2 = 2;
            r0.postCallbackError(r1, r2);	 Catch:{ all -> 0x0051 }
        L_0x004d:
            monitor-exit(r6);	 Catch:{ all -> 0x0051 }
            return;
        L_0x004f:
            monitor-exit(r6);	 Catch:{ all -> 0x0051 }
            return;
        L_0x0051:
            r0 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x0051 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.le.BluetoothLeScanner$BleScanCallbackWrapper.startRegistration():void");
        }

        public void stopLeScan() {
            synchronized (this) {
                if (this.mScannerId <= 0) {
                    String str = BluetoothLeScanner.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error state, mLeHandle: ");
                    stringBuilder.append(this.mScannerId);
                    Log.e(str, stringBuilder.toString());
                    return;
                }
                try {
                    this.mBluetoothGatt.stopScan(this.mScannerId);
                    this.mBluetoothGatt.unregisterScanner(this.mScannerId);
                } catch (RemoteException e) {
                    Log.e(BluetoothLeScanner.TAG, "Failed to stop scan and unregister", e);
                }
                BluetoothLeScannerInjector.stopLeScan(this.mScannerId, asBinder());
                this.mScannerId = -1;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void flushPendingBatchResults() {
            synchronized (this) {
                if (this.mScannerId <= 0) {
                    String str = BluetoothLeScanner.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error state, mLeHandle: ");
                    stringBuilder.append(this.mScannerId);
                    Log.e(str, stringBuilder.toString());
                    return;
                }
                try {
                    this.mBluetoothGatt.flushPendingBatchResults(this.mScannerId);
                } catch (RemoteException e) {
                    Log.e(BluetoothLeScanner.TAG, "Failed to get pending scan results", e);
                }
            }
        }

        public void onScannerRegistered(int status, int scannerId) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onScannerRegistered() - status=");
            stringBuilder.append(status);
            stringBuilder.append(" scannerId=");
            stringBuilder.append(scannerId);
            stringBuilder.append(" mScannerId=");
            stringBuilder.append(this.mScannerId);
            Log.d(BluetoothLeScanner.TAG, stringBuilder.toString());
            synchronized (this) {
                if (status == 0) {
                    try {
                        if (this.mScannerId == -1) {
                            this.mBluetoothGatt.unregisterClient(scannerId);
                        } else {
                            this.mScannerId = scannerId;
                            BluetoothLeScannerInjector.startLeScan(this.mBluetoothGatt, this.mScannerId, this.mSettings, this.mFilters, this.mWorkSource, this.mResultStorages, asBinder());
                        }
                    } catch (RemoteException e) {
                        String str = BluetoothLeScanner.TAG;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("fail to start le scan: ");
                        stringBuilder2.append(e);
                        Log.e(str, stringBuilder2.toString());
                        this.mScannerId = -1;
                    }
                } else if (status == 6) {
                    this.mScannerId = -2;
                } else {
                    this.mScannerId = -1;
                }
                notifyAll();
            }
        }

        public void onScanResult(final ScanResult scanResult) {
            synchronized (this) {
                if (this.mScannerId <= 0) {
                    return;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        BleScanCallbackWrapper.this.mScanCallback.onScanResult(1, scanResult);
                    }
                });
            }
        }

        public void onBatchScanResults(final List<ScanResult> results) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    BleScanCallbackWrapper.this.mScanCallback.onBatchScanResults(results);
                }
            });
        }

        public void onFoundOrLost(final boolean onFound, final ScanResult scanResult) {
            synchronized (this) {
                if (this.mScannerId <= 0) {
                    return;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        if (onFound) {
                            BleScanCallbackWrapper.this.mScanCallback.onScanResult(2, scanResult);
                        } else {
                            BleScanCallbackWrapper.this.mScanCallback.onScanResult(4, scanResult);
                        }
                    }
                });
            }
        }

        public void onScanManagerErrorCallback(int errorCode) {
            synchronized (this) {
                if (this.mScannerId <= 0) {
                    return;
                }
                BluetoothLeScanner.this.postCallbackError(this.mScanCallback, errorCode);
            }
        }
    }

    public BluetoothLeScanner(IBluetoothManager bluetoothManager) {
        this.mBluetoothManager = bluetoothManager;
    }

    public void startScan(ScanCallback callback) {
        startScan(null, new Builder().build(), callback);
    }

    public void startScan(List<ScanFilter> filters, ScanSettings settings, ScanCallback callback) {
        startScan(filters, settings, null, callback, null, null);
    }

    public int startScan(List<ScanFilter> filters, ScanSettings settings, PendingIntent callbackIntent) {
        return startScan(filters, settings != null ? settings : new Builder().build(), null, null, callbackIntent, null);
    }

    @SystemApi
    public void startScanFromSource(WorkSource workSource, ScanCallback callback) {
        startScanFromSource(null, new Builder().build(), workSource, callback);
    }

    @SystemApi
    public void startScanFromSource(List<ScanFilter> filters, ScanSettings settings, WorkSource workSource, ScanCallback callback) {
        startScan(filters, settings, workSource, callback, null, null);
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:82:0x00c6, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:83:0x00c7, code skipped:
            r0 = r0;
     */
    /* JADX WARNING: Missing block: B:85:0x00ca, code skipped:
            return 3;
     */
    /* JADX WARNING: Missing block: B:90:0x00cf, code skipped:
            r0 = th;
     */
    private int startScan(java.util.List<android.bluetooth.le.ScanFilter> r17, android.bluetooth.le.ScanSettings r18, android.os.WorkSource r19, android.bluetooth.le.ScanCallback r20, android.app.PendingIntent r21, java.util.List<java.util.List<android.bluetooth.le.ResultStorageDescriptor>> r22) {
        /*
        r16 = this;
        r9 = r16;
        r10 = r18;
        r11 = r20;
        r12 = r21;
        r0 = r9.mBluetoothAdapter;
        android.bluetooth.le.BluetoothLeUtils.checkAdapterStateOn(r0);
        if (r11 != 0) goto L_0x001a;
    L_0x000f:
        if (r12 == 0) goto L_0x0012;
    L_0x0011:
        goto L_0x001a;
    L_0x0012:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "callback is null";
        r0.<init>(r1);
        throw r0;
    L_0x001a:
        if (r10 == 0) goto L_0x00d1;
    L_0x001c:
        r13 = r9.mLeScanClients;
        monitor-enter(r13);
        r1 = 1;
        if (r11 == 0) goto L_0x0030;
    L_0x0022:
        r0 = r9.mLeScanClients;	 Catch:{ all -> 0x0038 }
        r0 = r0.containsKey(r11);	 Catch:{ all -> 0x0038 }
        if (r0 == 0) goto L_0x0030;
    L_0x002a:
        r0 = r9.postCallbackErrorOrReturn(r11, r1);	 Catch:{ all -> 0x0038 }
        monitor-exit(r13);	 Catch:{ all -> 0x0038 }
        return r0;
    L_0x0030:
        r0 = r9.mBluetoothManager;	 Catch:{ RemoteException -> 0x003d }
        r0 = r0.getBluetoothGatt();	 Catch:{ RemoteException -> 0x003d }
        r14 = r0;
        goto L_0x0040;
    L_0x0038:
        r0 = move-exception;
        r15 = r17;
        goto L_0x00cd;
    L_0x003d:
        r0 = move-exception;
        r0 = 0;
        r14 = r0;
    L_0x0040:
        r2 = 3;
        if (r14 != 0) goto L_0x0049;
    L_0x0043:
        r0 = r9.postCallbackErrorOrReturn(r11, r2);	 Catch:{ all -> 0x0038 }
        monitor-exit(r13);	 Catch:{ all -> 0x0038 }
        return r0;
    L_0x0049:
        r0 = r18.getCallbackType();	 Catch:{ all -> 0x0038 }
        r3 = 8;
        r15 = 0;
        if (r0 != r3) goto L_0x006d;
    L_0x0052:
        if (r17 == 0) goto L_0x005a;
    L_0x0054:
        r0 = r17.isEmpty();	 Catch:{ all -> 0x0038 }
        if (r0 == 0) goto L_0x006d;
    L_0x005a:
        r0 = new android.bluetooth.le.ScanFilter$Builder;	 Catch:{ all -> 0x0038 }
        r0.<init>();	 Catch:{ all -> 0x0038 }
        r0 = r0.build();	 Catch:{ all -> 0x0038 }
        r1 = new android.bluetooth.le.ScanFilter[r1];	 Catch:{ all -> 0x0038 }
        r1[r15] = r0;	 Catch:{ all -> 0x0038 }
        r1 = java.util.Arrays.asList(r1);	 Catch:{ all -> 0x0038 }
        r8 = r1;
        goto L_0x006f;
    L_0x006d:
        r8 = r17;
    L_0x006f:
        r0 = r9.isSettingsConfigAllowedForScan(r10);	 Catch:{ all -> 0x00cb }
        r1 = 4;
        if (r0 != 0) goto L_0x007f;
    L_0x0076:
        r0 = r9.postCallbackErrorOrReturn(r11, r1);	 Catch:{ all -> 0x007c }
        monitor-exit(r13);	 Catch:{ all -> 0x007c }
        return r0;
    L_0x007c:
        r0 = move-exception;
        r15 = r8;
        goto L_0x00cd;
    L_0x007f:
        r0 = r9.isHardwareResourcesAvailableForScan(r10);	 Catch:{ all -> 0x00cb }
        if (r0 != 0) goto L_0x008c;
    L_0x0085:
        r0 = 5;
        r0 = r9.postCallbackErrorOrReturn(r11, r0);	 Catch:{ all -> 0x007c }
        monitor-exit(r13);	 Catch:{ all -> 0x007c }
        return r0;
    L_0x008c:
        r0 = r9.isSettingsAndFilterComboAllowed(r10, r8);	 Catch:{ all -> 0x00cb }
        if (r0 != 0) goto L_0x0098;
    L_0x0092:
        r0 = r9.postCallbackErrorOrReturn(r11, r1);	 Catch:{ all -> 0x007c }
        monitor-exit(r13);	 Catch:{ all -> 0x007c }
        return r0;
    L_0x0098:
        r0 = r9.isRoutingAllowedForScan(r10);	 Catch:{ all -> 0x00cb }
        if (r0 != 0) goto L_0x00a4;
    L_0x009e:
        r0 = r9.postCallbackErrorOrReturn(r11, r1);	 Catch:{ all -> 0x007c }
        monitor-exit(r13);	 Catch:{ all -> 0x007c }
        return r0;
    L_0x00a4:
        if (r11 == 0) goto L_0x00bd;
    L_0x00a6:
        r0 = new android.bluetooth.le.BluetoothLeScanner$BleScanCallbackWrapper;	 Catch:{ all -> 0x00cb }
        r1 = r0;
        r2 = r16;
        r3 = r14;
        r4 = r8;
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r15 = r8;
        r8 = r22;
        r1.<init>(r3, r4, r5, r6, r7, r8);	 Catch:{ all -> 0x00cf }
        r0.startRegistration();	 Catch:{ all -> 0x00cf }
        goto L_0x00c3;
    L_0x00bd:
        r15 = r8;
        r0 = 0;
        android.bluetooth.BluetoothLeScannerInjector.startLeScan(r14, r12, r10, r15, r0);	 Catch:{ RemoteException -> 0x00c6 }
    L_0x00c3:
        monitor-exit(r13);	 Catch:{ all -> 0x00cf }
        r0 = 0;
        return r0;
    L_0x00c6:
        r0 = move-exception;
        r1 = r0;
        r0 = r1;
        monitor-exit(r13);	 Catch:{ all -> 0x00cf }
        return r2;
    L_0x00cb:
        r0 = move-exception;
        r15 = r8;
    L_0x00cd:
        monitor-exit(r13);	 Catch:{ all -> 0x00cf }
        throw r0;
    L_0x00cf:
        r0 = move-exception;
        goto L_0x00cd;
    L_0x00d1:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "settings is null";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.le.BluetoothLeScanner.startScan(java.util.List, android.bluetooth.le.ScanSettings, android.os.WorkSource, android.bluetooth.le.ScanCallback, android.app.PendingIntent, java.util.List):int");
    }

    public void stopScan(ScanCallback callback) {
        BluetoothLeUtils.checkAdapterStateOn(this.mBluetoothAdapter);
        synchronized (this.mLeScanClients) {
            BleScanCallbackWrapper wrapper = (BleScanCallbackWrapper) this.mLeScanClients.remove(callback);
            if (wrapper == null) {
                Log.d(TAG, "could not find callback wrapper");
                return;
            }
            wrapper.stopLeScan();
        }
    }

    public void stopScan(PendingIntent callbackIntent) {
        BluetoothLeUtils.checkAdapterStateOn(this.mBluetoothAdapter);
        try {
            this.mBluetoothManager.getBluetoothGatt().stopScanForIntent(callbackIntent, ActivityThread.currentOpPackageName());
            BluetoothLeScannerInjector.stopLeScan(callbackIntent, null);
        } catch (RemoteException e) {
        }
    }

    public void flushPendingScanResults(ScanCallback callback) {
        BluetoothLeUtils.checkAdapterStateOn(this.mBluetoothAdapter);
        if (callback != null) {
            synchronized (this.mLeScanClients) {
                BleScanCallbackWrapper wrapper = (BleScanCallbackWrapper) this.mLeScanClients.get(callback);
                if (wrapper == null) {
                    return;
                }
                wrapper.flushPendingBatchResults();
                return;
            }
        }
        throw new IllegalArgumentException("callback cannot be null!");
    }

    @SystemApi
    public void startTruncatedScan(List<TruncatedFilter> truncatedFilters, ScanSettings settings, ScanCallback callback) {
        int filterSize = truncatedFilters.size();
        List<ScanFilter> scanFilters = new ArrayList(filterSize);
        List scanStorages = new ArrayList(filterSize);
        for (TruncatedFilter filter : truncatedFilters) {
            scanFilters.add(filter.getFilter());
            scanStorages.add(filter.getStorageDescriptors());
        }
        startScan(scanFilters, settings, null, callback, null, scanStorages);
    }

    public void cleanup() {
        this.mLeScanClients.clear();
        BluetoothLeScannerInjector.cleanupLeScanStatistics();
    }

    private int postCallbackErrorOrReturn(ScanCallback callback, int errorCode) {
        if (callback == null) {
            return errorCode;
        }
        postCallbackError(callback, errorCode);
        return 0;
    }

    private void postCallbackError(final ScanCallback callback, final int errorCode) {
        this.mHandler.post(new Runnable() {
            public void run() {
                callback.onScanFailed(errorCode);
            }
        });
    }

    private boolean isSettingsConfigAllowedForScan(ScanSettings settings) {
        if (this.mBluetoothAdapter.isOffloadedFilteringSupported()) {
            return true;
        }
        if (settings.getCallbackType() == 1 && settings.getReportDelayMillis() == 0) {
            return true;
        }
        return false;
    }

    private boolean isSettingsAndFilterComboAllowed(ScanSettings settings, List<ScanFilter> filterList) {
        if ((settings.getCallbackType() & 6) != 0) {
            if (filterList == null) {
                return false;
            }
            for (ScanFilter filter : filterList) {
                if (filter.isAllFieldsEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isHardwareResourcesAvailableForScan(ScanSettings settings) {
        int callbackType = settings.getCallbackType();
        boolean z = true;
        if ((callbackType & 2) == 0 && (callbackType & 4) == 0) {
            return true;
        }
        if (!(this.mBluetoothAdapter.isOffloadedFilteringSupported() && this.mBluetoothAdapter.isHardwareTrackingFiltersAvailable())) {
            z = false;
        }
        return z;
    }

    private boolean isRoutingAllowedForScan(ScanSettings settings) {
        if (settings.getCallbackType() == 8 && settings.getScanMode() == -1) {
            return false;
        }
        return true;
    }

    public boolean startLeScanStatistics(LeScanCallback callback) {
        return BluetoothLeScannerInjector.startLeScanStatistics(this.mBluetoothManager, callback);
    }

    public void stopLeScanStatistics(LeScanCallback callback) {
        BluetoothLeScannerInjector.stopLeScanStatistics(callback);
    }
}
