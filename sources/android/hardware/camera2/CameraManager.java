package android.hardware.camera2;

import android.content.Context;
import android.hardware.CameraInjector;
import android.hardware.CameraStatus;
import android.hardware.ICameraService;
import android.hardware.ICameraServiceListener.Stub;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.impl.CameraDeviceImpl;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.legacy.LegacyMetadataMapper;
import android.os.Binder;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class CameraManager {
    private static final int API_VERSION_1 = 1;
    private static final int API_VERSION_2 = 2;
    private static final int CAMERA_TYPE_ALL = 1;
    private static final int CAMERA_TYPE_BACKWARD_COMPATIBLE = 0;
    private static final String TAG = "CameraManager";
    private static final int USE_CALLING_UID = -1;
    private final boolean DEBUG = false;
    private final Context mContext;
    private ArrayList<String> mDeviceIdList;
    private final Object mLock = new Object();

    public static abstract class AvailabilityCallback {
        public void onCameraAvailable(String cameraId) {
        }

        public void onCameraUnavailable(String cameraId) {
        }

        public void onCameraAccessPrioritiesChanged() {
        }
    }

    private static final class CameraManagerGlobal extends Stub implements DeathRecipient {
        private static final String CAMERA_SERVICE_BINDER_NAME = "media.camera";
        private static final String TAG = "CameraManagerGlobal";
        private static final CameraManagerGlobal gCameraManager = new CameraManagerGlobal();
        public static final boolean sCameraServiceDisabled = SystemProperties.getBoolean("config.disable_cameraservice", false);
        private final int CAMERA_SERVICE_RECONNECT_DELAY_MS = 1000;
        private final boolean DEBUG = false;
        private final ArrayMap<AvailabilityCallback, Executor> mCallbackMap = new ArrayMap();
        private ICameraService mCameraService;
        private final ArrayMap<String, Integer> mDeviceStatus = new ArrayMap();
        private final Object mLock = new Object();
        private final ScheduledExecutorService mScheduler = Executors.newScheduledThreadPool(1);
        private final ArrayMap<TorchCallback, Executor> mTorchCallbackMap = new ArrayMap();
        private Binder mTorchClientBinder = new Binder();
        private final ArrayMap<String, Integer> mTorchStatus = new ArrayMap();

        private CameraManagerGlobal() {
        }

        public static CameraManagerGlobal get() {
            return gCameraManager;
        }

        public IBinder asBinder() {
            return this;
        }

        public ICameraService getCameraService() {
            ICameraService iCameraService;
            synchronized (this.mLock) {
                connectCameraServiceLocked();
                if (this.mCameraService == null && !sCameraServiceDisabled) {
                    Log.e(TAG, "Camera service is unavailable");
                }
                iCameraService = this.mCameraService;
            }
            return iCameraService;
        }

        private void connectCameraServiceLocked() {
            if (this.mCameraService == null && !sCameraServiceDisabled) {
                Log.i(TAG, "Connecting to camera service");
                IBinder cameraServiceBinder = ServiceManager.getService(CAMERA_SERVICE_BINDER_NAME);
                if (cameraServiceBinder != null) {
                    int i = 0;
                    try {
                        cameraServiceBinder.linkToDeath(this, 0);
                        ICameraService cameraService = ICameraService.Stub.asInterface(cameraServiceBinder);
                        try {
                            CameraMetadataNative.setupGlobalVendorTagDescriptor();
                        } catch (ServiceSpecificException e) {
                            handleRecoverableSetupErrors(e);
                        }
                        try {
                            CameraStatus[] cameraStatuses = cameraService.addListener(this);
                            int length = cameraStatuses.length;
                            while (i < length) {
                                CameraStatus c = cameraStatuses[i];
                                onStatusChangedLocked(c.status, c.cameraId);
                                i++;
                            }
                            this.mCameraService = cameraService;
                        } catch (ServiceSpecificException e2) {
                            throw new IllegalStateException("Failed to register a camera service listener", e2);
                        } catch (RemoteException e3) {
                        }
                    } catch (RemoteException e4) {
                    }
                }
            }
        }

        public String[] getCameraIdList() {
            String[] cameraIds;
            synchronized (this.mLock) {
                connectCameraServiceLocked();
                boolean exposeAuxCamera = CameraInjector.isExposeAuxCamera();
                int idCount = 0;
                int i = 0;
                int i2 = 0;
                while (i2 < this.mDeviceStatus.size()) {
                    if (!exposeAuxCamera && i2 == 2) {
                        break;
                    }
                    int status = ((Integer) this.mDeviceStatus.valueAt(i2)).intValue();
                    if (status != 0) {
                        if (status != 2) {
                            idCount++;
                        }
                    }
                    i2++;
                }
                cameraIds = new String[idCount];
                idCount = 0;
                while (i < this.mDeviceStatus.size()) {
                    if (!exposeAuxCamera && i == 2) {
                        break;
                    }
                    i2 = ((Integer) this.mDeviceStatus.valueAt(i)).intValue();
                    if (i2 != 0) {
                        if (i2 != 2) {
                            cameraIds[idCount] = (String) this.mDeviceStatus.keyAt(i);
                            idCount++;
                        }
                    }
                    i++;
                }
            }
            Arrays.sort(cameraIds, new Comparator<String>() {
                public int compare(String s1, String s2) {
                    int s1Int;
                    int s2Int;
                    try {
                        s1Int = Integer.parseInt(s1);
                    } catch (NumberFormatException e) {
                        s1Int = -1;
                    }
                    try {
                        s2Int = Integer.parseInt(s2);
                    } catch (NumberFormatException e2) {
                        s2Int = -1;
                    }
                    if (s1Int >= 0 && s2Int >= 0) {
                        return s1Int - s2Int;
                    }
                    if (s1Int >= 0) {
                        return -1;
                    }
                    if (s2Int >= 0) {
                        return 1;
                    }
                    return s1.compareTo(s2);
                }
            });
            return cameraIds;
        }

        public void setTorchMode(String cameraId, boolean enabled) throws CameraAccessException {
            synchronized (this.mLock) {
                if (cameraId != null) {
                    try {
                        if (!CameraInjector.isExposeAuxCamera()) {
                            if (Integer.parseInt(cameraId) >= 2) {
                                throw new IllegalArgumentException("invalid cameraId");
                            }
                        }
                        ICameraService cameraService = getCameraService();
                        if (cameraService != null) {
                            cameraService.setTorchMode(cameraId, enabled, this.mTorchClientBinder);
                        } else {
                            throw new CameraAccessException(2, "Camera service is currently unavailable");
                        }
                    } catch (ServiceSpecificException e) {
                        CameraManager.throwAsPublicException(e);
                    } catch (RemoteException e2) {
                        throw new CameraAccessException(2, "Camera service is currently unavailable");
                    } catch (Throwable th) {
                    }
                } else {
                    throw new IllegalArgumentException("cameraId was null");
                }
            }
        }

        private void handleRecoverableSetupErrors(ServiceSpecificException e) {
            if (e.errorCode == 4) {
                Log.w(TAG, e.getMessage());
                return;
            }
            throw new IllegalStateException(e);
        }

        private boolean isAvailable(int status) {
            if (status != 1) {
                return false;
            }
            return true;
        }

        private boolean validStatus(int status) {
            if (status == -2 || status == 0 || status == 1 || status == 2) {
                return true;
            }
            return false;
        }

        private boolean validTorchStatus(int status) {
            if (status == 0 || status == 1 || status == 2) {
                return true;
            }
            return false;
        }

        private void postSingleAccessPriorityChangeUpdate(final AvailabilityCallback callback, Executor executor) {
            long ident = Binder.clearCallingIdentity();
            try {
                executor.execute(new Runnable() {
                    public void run() {
                        callback.onCameraAccessPrioritiesChanged();
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(ident);
            }
        }

        private void postSingleUpdate(final AvailabilityCallback callback, Executor executor, final String id, int status) {
            long ident;
            if (isAvailable(status)) {
                ident = Binder.clearCallingIdentity();
                try {
                    executor.execute(new Runnable() {
                        public void run() {
                            callback.onCameraAvailable(id);
                        }
                    });
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            } else {
                ident = Binder.clearCallingIdentity();
                try {
                    executor.execute(new Runnable() {
                        public void run() {
                            callback.onCameraUnavailable(id);
                        }
                    });
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            }
        }

        private void postSingleTorchUpdate(TorchCallback callback, Executor executor, String id, int status) {
            long ident;
            if (status == 1 || status == 2) {
                ident = Binder.clearCallingIdentity();
                try {
                    executor.execute(new -$$Lambda$CameraManager$CameraManagerGlobal$CONvadOBAEkcHSpx8j61v67qRGM(callback, id, status));
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            } else {
                ident = Binder.clearCallingIdentity();
                try {
                    executor.execute(new -$$Lambda$CameraManager$CameraManagerGlobal$6Ptxoe4wF_VCkE_pml8t66mklao(callback, id));
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            }
        }

        static /* synthetic */ void lambda$postSingleTorchUpdate$0(TorchCallback callback, String id, int status) {
            callback.onTorchModeChanged(id, status == 2);
        }

        private void updateCallbackLocked(AvailabilityCallback callback, Executor executor) {
            for (int i = 0; i < this.mDeviceStatus.size(); i++) {
                postSingleUpdate(callback, executor, (String) this.mDeviceStatus.keyAt(i), ((Integer) this.mDeviceStatus.valueAt(i)).intValue());
            }
        }

        private void onStatusChangedLocked(int status, String id) {
            boolean exposeMonoCamera = CameraInjector.isExposeAuxCamera();
            String str = TAG;
            if (!exposeMonoCamera && Integer.parseInt(id) >= 2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[soar.cts] ignore the status update of camera: ");
                stringBuilder.append(id);
                Log.w(str, stringBuilder.toString());
            } else if (validStatus(status)) {
                Integer oldStatus;
                if (status == 0) {
                    oldStatus = (Integer) this.mDeviceStatus.remove(id);
                } else {
                    oldStatus = (Integer) this.mDeviceStatus.put(id, Integer.valueOf(status));
                }
                if (oldStatus != null && oldStatus.intValue() == status) {
                    return;
                }
                if (oldStatus == null || isAvailable(status) != isAvailable(oldStatus.intValue())) {
                    int callbackCount = this.mCallbackMap.size();
                    for (int i = 0; i < callbackCount; i++) {
                        postSingleUpdate((AvailabilityCallback) this.mCallbackMap.keyAt(i), (Executor) this.mCallbackMap.valueAt(i), id, status);
                    }
                }
            } else {
                Log.e(str, String.format("Ignoring invalid device %s status 0x%x", new Object[]{id, Integer.valueOf(status)}));
            }
        }

        private void updateTorchCallbackLocked(TorchCallback callback, Executor executor) {
            for (int i = 0; i < this.mTorchStatus.size(); i++) {
                postSingleTorchUpdate(callback, executor, (String) this.mTorchStatus.keyAt(i), ((Integer) this.mTorchStatus.valueAt(i)).intValue());
            }
        }

        private void onTorchStatusChangedLocked(int status, String id) {
            boolean exposeMonoCamera = CameraInjector.isExposeAuxCamera();
            String str = TAG;
            if (!exposeMonoCamera && Integer.parseInt(id) >= 2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ignore the torch status update of camera: ");
                stringBuilder.append(id);
                Log.w(str, stringBuilder.toString());
            } else if (validTorchStatus(status)) {
                Integer oldStatus = (Integer) this.mTorchStatus.put(id, Integer.valueOf(status));
                if (oldStatus == null || oldStatus.intValue() != status) {
                    int callbackCount = this.mTorchCallbackMap.size();
                    for (int i = 0; i < callbackCount; i++) {
                        postSingleTorchUpdate((TorchCallback) this.mTorchCallbackMap.keyAt(i), (Executor) this.mTorchCallbackMap.valueAt(i), id, status);
                    }
                }
            } else {
                Log.e(str, String.format("Ignoring invalid device %s torch status 0x%x", new Object[]{id, Integer.valueOf(status)}));
            }
        }

        public void registerAvailabilityCallback(AvailabilityCallback callback, Executor executor) {
            synchronized (this.mLock) {
                connectCameraServiceLocked();
                if (((Executor) this.mCallbackMap.put(callback, executor)) == null) {
                    updateCallbackLocked(callback, executor);
                }
                if (this.mCameraService == null) {
                    scheduleCameraServiceReconnectionLocked();
                }
            }
        }

        public void unregisterAvailabilityCallback(AvailabilityCallback callback) {
            synchronized (this.mLock) {
                this.mCallbackMap.remove(callback);
            }
        }

        public void registerTorchCallback(TorchCallback callback, Executor executor) {
            synchronized (this.mLock) {
                connectCameraServiceLocked();
                if (((Executor) this.mTorchCallbackMap.put(callback, executor)) == null) {
                    updateTorchCallbackLocked(callback, executor);
                }
                if (this.mCameraService == null) {
                    scheduleCameraServiceReconnectionLocked();
                }
            }
        }

        public void unregisterTorchCallback(TorchCallback callback) {
            synchronized (this.mLock) {
                this.mTorchCallbackMap.remove(callback);
            }
        }

        public void onStatusChanged(int status, String cameraId) throws RemoteException {
            synchronized (this.mLock) {
                onStatusChangedLocked(status, cameraId);
            }
        }

        public void onTorchStatusChanged(int status, String cameraId) throws RemoteException {
            synchronized (this.mLock) {
                onTorchStatusChangedLocked(status, cameraId);
            }
        }

        public void onCameraAccessPrioritiesChanged() {
            synchronized (this.mLock) {
                int callbackCount = this.mCallbackMap.size();
                for (int i = 0; i < callbackCount; i++) {
                    postSingleAccessPriorityChangeUpdate((AvailabilityCallback) this.mCallbackMap.keyAt(i), (Executor) this.mCallbackMap.valueAt(i));
                }
            }
        }

        private void scheduleCameraServiceReconnectionLocked() {
            if (!this.mCallbackMap.isEmpty() || !this.mTorchCallbackMap.isEmpty()) {
                try {
                    this.mScheduler.schedule(new -$$Lambda$CameraManager$CameraManagerGlobal$w1y8myi6vgxAcTEs8WArI-NN3R0(this), 1000, TimeUnit.MILLISECONDS);
                } catch (RejectedExecutionException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to schedule camera service re-connect: ");
                    stringBuilder.append(e);
                    Log.e(TAG, stringBuilder.toString());
                }
            }
        }

        public /* synthetic */ void lambda$scheduleCameraServiceReconnectionLocked$2$CameraManager$CameraManagerGlobal() {
            if (getCameraService() == null) {
                synchronized (this.mLock) {
                    scheduleCameraServiceReconnectionLocked();
                }
            }
        }

        public void binderDied() {
            synchronized (this.mLock) {
                if (this.mCameraService == null) {
                    return;
                }
                int i;
                this.mCameraService = null;
                for (i = 0; i < this.mDeviceStatus.size(); i++) {
                    onStatusChangedLocked(0, (String) this.mDeviceStatus.keyAt(i));
                }
                for (i = 0; i < this.mTorchStatus.size(); i++) {
                    onTorchStatusChangedLocked(0, (String) this.mTorchStatus.keyAt(i));
                }
                scheduleCameraServiceReconnectionLocked();
            }
        }
    }

    public static abstract class TorchCallback {
        public void onTorchModeUnavailable(String cameraId) {
        }

        public void onTorchModeChanged(String cameraId, boolean enabled) {
        }
    }

    public CameraManager(Context context) {
        synchronized (this.mLock) {
            this.mContext = context;
        }
    }

    public String[] getCameraIdList() throws CameraAccessException {
        return CameraManagerGlobal.get().getCameraIdList();
    }

    public void registerAvailabilityCallback(AvailabilityCallback callback, Handler handler) {
        CameraManagerGlobal.get().registerAvailabilityCallback(callback, CameraDeviceImpl.checkAndWrapHandler(handler));
    }

    public void registerAvailabilityCallback(Executor executor, AvailabilityCallback callback) {
        if (executor != null) {
            CameraManagerGlobal.get().registerAvailabilityCallback(callback, executor);
            return;
        }
        throw new IllegalArgumentException("executor was null");
    }

    public void unregisterAvailabilityCallback(AvailabilityCallback callback) {
        CameraManagerGlobal.get().unregisterAvailabilityCallback(callback);
    }

    public void registerTorchCallback(TorchCallback callback, Handler handler) {
        CameraManagerGlobal.get().registerTorchCallback(callback, CameraDeviceImpl.checkAndWrapHandler(handler));
    }

    public void registerTorchCallback(Executor executor, TorchCallback callback) {
        if (executor != null) {
            CameraManagerGlobal.get().registerTorchCallback(callback, executor);
            return;
        }
        throw new IllegalArgumentException("executor was null");
    }

    public void unregisterTorchCallback(TorchCallback callback) {
        CameraManagerGlobal.get().unregisterTorchCallback(callback);
    }

    private Size getDisplaySize() {
        Size ret = new Size(0, 0);
        try {
            Display display = ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            if (height > width) {
                height = width;
                width = display.getHeight();
            }
            return new Size(width, height);
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getDisplaySize Failed. ");
            stringBuilder.append(e.toString());
            Log.e(TAG, stringBuilder.toString());
            return ret;
        }
    }

    public CameraCharacteristics getCameraCharacteristics(String cameraId) throws CameraAccessException {
        CameraCharacteristics characteristics = null;
        if (CameraManagerGlobal.sCameraServiceDisabled) {
            throw new IllegalArgumentException("No cameras available on device");
        }
        synchronized (this.mLock) {
            ICameraService cameraService = CameraManagerGlobal.get().getCameraService();
            if (cameraService != null) {
                try {
                    Size displaySize = getDisplaySize();
                    if (isHiddenPhysicalCamera(cameraId) || supportsCamera2ApiLocked(cameraId)) {
                        CameraMetadataNative info = cameraService.getCameraCharacteristics(cameraId);
                        try {
                            info.setCameraId(Integer.parseInt(cameraId));
                        } catch (NumberFormatException e) {
                            String str = TAG;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Failed to parse camera Id ");
                            stringBuilder.append(cameraId);
                            stringBuilder.append(" to integer");
                            Log.e(str, stringBuilder.toString());
                        }
                        info.setDisplaySize(displaySize);
                        characteristics = new CameraCharacteristics(info);
                    } else {
                        int id = Integer.parseInt(cameraId);
                        characteristics = LegacyMetadataMapper.createCharacteristics(cameraService.getLegacyParameters(id), cameraService.getCameraInfo(id), id, displaySize);
                    }
                } catch (ServiceSpecificException e2) {
                    throwAsPublicException(e2);
                } catch (RemoteException e3) {
                    throw new CameraAccessException(2, "Camera service is currently unavailable", e3);
                }
            }
            throw new CameraAccessException(2, "Camera service is currently unavailable");
        }
        return characteristics;
    }

    /* JADX WARNING: Removed duplicated region for block: B:60:0x00dc A:{Catch:{ ServiceSpecificException -> 0x009b, RemoteException -> 0x0089, all -> 0x00e9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a4 A:{Catch:{ ServiceSpecificException -> 0x009b, RemoteException -> 0x0089, all -> 0x00e9 }} */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:13:0x0040, B:18:0x0053] */
    /* JADX WARNING: Missing block: B:25:0x006d, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:27:0x006f, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:28:0x0070, code skipped:
            r0 = r0;
            r7 = new java.lang.StringBuilder();
            r7.append("Expected cameraId to be numeric, but it was: ");
            r7.append(r8);
     */
    /* JADX WARNING: Missing block: B:29:0x0088, code skipped:
            throw new java.lang.IllegalArgumentException(r7.toString());
     */
    /* JADX WARNING: Missing block: B:47:0x00bc, code skipped:
            if (r0.errorCode == 10) goto L_0x00be;
     */
    /* JADX WARNING: Missing block: B:49:0x00bf, code skipped:
            throwAsPublicException(r0);
     */
    /* JADX WARNING: Missing block: B:50:0x00c3, code skipped:
            r2.setRemoteFailure(r0);
     */
    /* JADX WARNING: Missing block: B:56:0x00d2, code skipped:
            throwAsPublicException(r0);
     */
    /* JADX WARNING: Missing block: B:61:0x00e3, code skipped:
            throw new java.lang.AssertionError("Should've gone down the shim path");
     */
    private android.hardware.camera2.CameraDevice openCameraDeviceUserAsync(java.lang.String r16, android.hardware.camera2.CameraDevice.StateCallback r17, java.util.concurrent.Executor r18, int r19) throws android.hardware.camera2.CameraAccessException {
        /*
        r15 = this;
        r1 = r15;
        r8 = r16;
        r9 = r15.getCameraCharacteristics(r16);
        r10 = 0;
        r11 = r1.mLock;
        monitor-enter(r11);
        r12 = 0;
        r0 = new android.hardware.camera2.impl.CameraDeviceImpl;	 Catch:{ all -> 0x00e4 }
        r2 = r1.mContext;	 Catch:{ all -> 0x00e4 }
        r2 = r2.getApplicationInfo();	 Catch:{ all -> 0x00e4 }
        r7 = r2.targetSdkVersion;	 Catch:{ all -> 0x00e4 }
        r2 = r0;
        r3 = r16;
        r4 = r17;
        r5 = r18;
        r6 = r9;
        r2.<init>(r3, r4, r5, r6, r7);	 Catch:{ all -> 0x00e4 }
        r2 = r0;
        r0 = r2.getCallbacks();	 Catch:{ all -> 0x00e4 }
        r3 = r0;
        r4 = 4;
        r0 = r15.supportsCamera2ApiLocked(r16);	 Catch:{ ServiceSpecificException -> 0x009b, RemoteException -> 0x0089 }
        if (r0 == 0) goto L_0x0051;
    L_0x002e:
        r0 = android.hardware.camera2.CameraManager.CameraManagerGlobal.get();	 Catch:{ ServiceSpecificException -> 0x009b, RemoteException -> 0x0089 }
        r0 = r0.getCameraService();	 Catch:{ ServiceSpecificException -> 0x009b, RemoteException -> 0x0089 }
        if (r0 == 0) goto L_0x0047;
    L_0x0038:
        r5 = r1.mContext;	 Catch:{ ServiceSpecificException -> 0x009b, RemoteException -> 0x0089 }
        r5 = r5.getOpPackageName();	 Catch:{ ServiceSpecificException -> 0x009b, RemoteException -> 0x0089 }
        r6 = r19;
        r4 = r0.connectDevice(r3, r8, r5, r6);	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        r0 = r4;
        r12 = r0;
        goto L_0x0069;
    L_0x0047:
        r6 = r19;
        r5 = new android.os.ServiceSpecificException;	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        r7 = "Camera service is currently unavailable";
        r5.<init>(r4, r7);	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        throw r5;	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
    L_0x0051:
        r6 = r19;
        r0 = java.lang.Integer.parseInt(r16);	 Catch:{ NumberFormatException -> 0x006f }
        r5 = "CameraManager";
        r7 = "Using legacy camera HAL.";
        android.util.Log.i(r5, r7);	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        r5 = r15.getDisplaySize();	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        r4 = android.hardware.camera2.legacy.CameraDeviceUserShim.connectBinderShim(r3, r0, r5);	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        r12 = r4;
    L_0x0069:
        goto L_0x00d6;
    L_0x006b:
        r0 = move-exception;
        goto L_0x008c;
    L_0x006d:
        r0 = move-exception;
        goto L_0x009e;
    L_0x006f:
        r0 = move-exception;
        r5 = r0;
        r0 = r5;
        r5 = new java.lang.IllegalArgumentException;	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        r7 = new java.lang.StringBuilder;	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        r7.<init>();	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        r13 = "Expected cameraId to be numeric, but it was: ";
        r7.append(r13);	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        r7.append(r8);	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        r7 = r7.toString();	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        r5.<init>(r7);	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
        throw r5;	 Catch:{ ServiceSpecificException -> 0x006d, RemoteException -> 0x006b }
    L_0x0089:
        r0 = move-exception;
        r6 = r19;
    L_0x008c:
        r5 = new android.os.ServiceSpecificException;	 Catch:{ all -> 0x00e9 }
        r7 = "Camera service is currently unavailable";
        r5.<init>(r4, r7);	 Catch:{ all -> 0x00e9 }
        r4 = r5;
        r2.setRemoteFailure(r4);	 Catch:{ all -> 0x00e9 }
        throwAsPublicException(r4);	 Catch:{ all -> 0x00e9 }
        goto L_0x00d6;
    L_0x009b:
        r0 = move-exception;
        r6 = r19;
    L_0x009e:
        r5 = r0.errorCode;	 Catch:{ all -> 0x00e9 }
        r7 = 9;
        if (r5 == r7) goto L_0x00dc;
    L_0x00a4:
        r5 = r0.errorCode;	 Catch:{ all -> 0x00e9 }
        r7 = 6;
        r13 = 7;
        if (r5 == r13) goto L_0x00c3;
    L_0x00aa:
        r5 = r0.errorCode;	 Catch:{ all -> 0x00e9 }
        r14 = 8;
        if (r5 == r14) goto L_0x00c3;
    L_0x00b0:
        r5 = r0.errorCode;	 Catch:{ all -> 0x00e9 }
        if (r5 == r7) goto L_0x00c3;
    L_0x00b4:
        r5 = r0.errorCode;	 Catch:{ all -> 0x00e9 }
        if (r5 == r4) goto L_0x00c3;
    L_0x00b8:
        r5 = r0.errorCode;	 Catch:{ all -> 0x00e9 }
        r14 = 10;
        if (r5 != r14) goto L_0x00bf;
    L_0x00be:
        goto L_0x00c3;
    L_0x00bf:
        throwAsPublicException(r0);	 Catch:{ all -> 0x00e9 }
        goto L_0x0069;
    L_0x00c3:
        r2.setRemoteFailure(r0);	 Catch:{ all -> 0x00e9 }
        r5 = r0.errorCode;	 Catch:{ all -> 0x00e9 }
        if (r5 == r7) goto L_0x00d2;
    L_0x00ca:
        r5 = r0.errorCode;	 Catch:{ all -> 0x00e9 }
        if (r5 == r4) goto L_0x00d2;
    L_0x00ce:
        r4 = r0.errorCode;	 Catch:{ all -> 0x00e9 }
        if (r4 != r13) goto L_0x0069;
    L_0x00d2:
        throwAsPublicException(r0);	 Catch:{ all -> 0x00e9 }
        goto L_0x0069;
    L_0x00d6:
        r2.setRemoteDevice(r12);	 Catch:{ all -> 0x00e9 }
        r10 = r2;
        monitor-exit(r11);	 Catch:{ all -> 0x00e9 }
        return r10;
    L_0x00dc:
        r4 = new java.lang.AssertionError;	 Catch:{ all -> 0x00e9 }
        r5 = "Should've gone down the shim path";
        r4.<init>(r5);	 Catch:{ all -> 0x00e9 }
        throw r4;	 Catch:{ all -> 0x00e9 }
    L_0x00e4:
        r0 = move-exception;
        r6 = r19;
    L_0x00e7:
        monitor-exit(r11);	 Catch:{ all -> 0x00e9 }
        throw r0;
    L_0x00e9:
        r0 = move-exception;
        goto L_0x00e7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.CameraManager.openCameraDeviceUserAsync(java.lang.String, android.hardware.camera2.CameraDevice$StateCallback, java.util.concurrent.Executor, int):android.hardware.camera2.CameraDevice");
    }

    public void openCamera(String cameraId, StateCallback callback, Handler handler) throws CameraAccessException {
        openCameraForUid(cameraId, callback, CameraDeviceImpl.checkAndWrapHandler(handler), -1);
    }

    public void openCamera(String cameraId, Executor executor, StateCallback callback) throws CameraAccessException {
        if (executor != null) {
            openCameraForUid(cameraId, callback, executor, -1);
            return;
        }
        throw new IllegalArgumentException("executor was null");
    }

    public void openCameraForUid(String cameraId, StateCallback callback, Executor executor, int clientUid) throws CameraAccessException {
        if (cameraId == null) {
            throw new IllegalArgumentException("cameraId was null");
        } else if (callback == null) {
            throw new IllegalArgumentException("callback was null");
        } else if (CameraManagerGlobal.sCameraServiceDisabled) {
            throw new IllegalArgumentException("No cameras available on device");
        } else {
            openCameraDeviceUserAsync(cameraId, callback, executor, clientUid);
        }
    }

    public void setTorchMode(String cameraId, boolean enabled) throws CameraAccessException {
        if (CameraManagerGlobal.sCameraServiceDisabled) {
            throw new IllegalArgumentException("No cameras available on device");
        }
        CameraManagerGlobal.get().setTorchMode(cameraId, enabled);
    }

    public static void throwAsPublicException(Throwable t) throws CameraAccessException {
        if (t instanceof ServiceSpecificException) {
            int reason;
            ServiceSpecificException e = (ServiceSpecificException) t;
            switch (e.errorCode) {
                case 1:
                    throw new SecurityException(e.getMessage(), e);
                case 2:
                case 3:
                    throw new IllegalArgumentException(e.getMessage(), e);
                case 4:
                    reason = 2;
                    break;
                case 6:
                    reason = 1;
                    break;
                case 7:
                    reason = 4;
                    break;
                case 8:
                    reason = 5;
                    break;
                case 9:
                    reason = 1000;
                    break;
                default:
                    reason = 3;
                    break;
            }
            throw new CameraAccessException(reason, e.getMessage(), e);
        } else if (t instanceof DeadObjectException) {
            throw new CameraAccessException(2, "Camera service has died unexpectedly", t);
        } else if (t instanceof RemoteException) {
            throw new UnsupportedOperationException("An unknown RemoteException was thrown which should never happen.", t);
        } else if (t instanceof RuntimeException) {
            throw ((RuntimeException) t);
        }
    }

    private boolean supportsCamera2ApiLocked(String cameraId) {
        return supportsCameraApiLocked(cameraId, 2);
    }

    private boolean supportsCameraApiLocked(String cameraId, int apiVersion) {
        try {
            ICameraService cameraService = CameraManagerGlobal.get().getCameraService();
            if (cameraService == null) {
                return false;
            }
            return cameraService.supportsCameraApi(cameraId, apiVersion);
        } catch (RemoteException e) {
            return false;
        }
    }

    public static boolean isHiddenPhysicalCamera(String cameraId) {
        try {
            ICameraService cameraService = CameraManagerGlobal.get().getCameraService();
            if (cameraService == null) {
                return false;
            }
            return cameraService.isHiddenPhysicalCamera(cameraId);
        } catch (RemoteException e) {
            return false;
        }
    }
}
