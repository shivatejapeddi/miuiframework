package android.telephony.data;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.net.LinkProperties;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.Rlog;
import android.telephony.data.IDataService.Stub;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

@SystemApi
public abstract class DataService extends Service {
    private static final int DATA_SERVICE_CREATE_DATA_SERVICE_PROVIDER = 1;
    private static final int DATA_SERVICE_INDICATION_DATA_CALL_LIST_CHANGED = 11;
    private static final int DATA_SERVICE_REMOVE_ALL_DATA_SERVICE_PROVIDERS = 3;
    private static final int DATA_SERVICE_REMOVE_DATA_SERVICE_PROVIDER = 2;
    private static final int DATA_SERVICE_REQUEST_DEACTIVATE_DATA_CALL = 5;
    private static final int DATA_SERVICE_REQUEST_REGISTER_DATA_CALL_LIST_CHANGED = 9;
    private static final int DATA_SERVICE_REQUEST_REQUEST_DATA_CALL_LIST = 8;
    private static final int DATA_SERVICE_REQUEST_SETUP_DATA_CALL = 4;
    private static final int DATA_SERVICE_REQUEST_SET_DATA_PROFILE = 7;
    private static final int DATA_SERVICE_REQUEST_SET_INITIAL_ATTACH_APN = 6;
    private static final int DATA_SERVICE_REQUEST_UNREGISTER_DATA_CALL_LIST_CHANGED = 10;
    public static final int REQUEST_REASON_HANDOVER = 3;
    public static final int REQUEST_REASON_NORMAL = 1;
    public static final int REQUEST_REASON_SHUTDOWN = 2;
    public static final int REQUEST_REASON_UNKNOWN = 0;
    public static final String SERVICE_INTERFACE = "android.telephony.data.DataService";
    private static final String TAG = DataService.class.getSimpleName();
    @VisibleForTesting
    public final IDataServiceWrapper mBinder = new IDataServiceWrapper();
    private final DataServiceHandler mHandler;
    private final HandlerThread mHandlerThread = new HandlerThread(TAG);
    private final SparseArray<DataServiceProvider> mServiceMap = new SparseArray();

    private static final class DataCallListChangedIndication {
        public final IDataServiceCallback callback;
        public final List<DataCallResponse> dataCallList;

        DataCallListChangedIndication(List<DataCallResponse> dataCallList, IDataServiceCallback callback) {
            this.dataCallList = dataCallList;
            this.callback = callback;
        }
    }

    private class DataServiceHandler extends Handler {
        DataServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int slotIndex = message.arg1;
            DataServiceProvider serviceProvider = (DataServiceProvider) DataService.this.mServiceMap.get(slotIndex);
            DataServiceCallback dataServiceCallback = null;
            int i;
            boolean z;
            switch (message.what) {
                case 1:
                    serviceProvider = DataService.this.onCreateDataServiceProvider(message.arg1);
                    if (serviceProvider != null) {
                        DataService.this.mServiceMap.put(slotIndex, serviceProvider);
                        return;
                    }
                    return;
                case 2:
                    if (serviceProvider != null) {
                        serviceProvider.close();
                        DataService.this.mServiceMap.remove(slotIndex);
                        return;
                    }
                    return;
                case 3:
                    for (int i2 = 0; i2 < DataService.this.mServiceMap.size(); i2++) {
                        serviceProvider = (DataServiceProvider) DataService.this.mServiceMap.get(i2);
                        if (serviceProvider != null) {
                            serviceProvider.close();
                        }
                    }
                    DataService.this.mServiceMap.clear();
                    return;
                case 4:
                    if (serviceProvider != null) {
                        DataServiceCallback dataServiceCallback2;
                        SetupDataCallRequest setupDataCallRequest = message.obj;
                        i = setupDataCallRequest.accessNetworkType;
                        DataProfile dataProfile = setupDataCallRequest.dataProfile;
                        boolean z2 = setupDataCallRequest.isRoaming;
                        boolean z3 = setupDataCallRequest.allowRoaming;
                        int i3 = setupDataCallRequest.reason;
                        LinkProperties linkProperties = setupDataCallRequest.linkProperties;
                        if (setupDataCallRequest.callback != null) {
                            dataServiceCallback2 = new DataServiceCallback(setupDataCallRequest.callback);
                        } else {
                            dataServiceCallback2 = null;
                        }
                        serviceProvider.setupDataCall(i, dataProfile, z2, z3, i3, linkProperties, dataServiceCallback2);
                        return;
                    }
                    return;
                case 5:
                    if (serviceProvider != null) {
                        DeactivateDataCallRequest deactivateDataCallRequest = message.obj;
                        i = deactivateDataCallRequest.cid;
                        int i4 = deactivateDataCallRequest.reason;
                        if (deactivateDataCallRequest.callback != null) {
                            dataServiceCallback = new DataServiceCallback(deactivateDataCallRequest.callback);
                        }
                        serviceProvider.deactivateDataCall(i, i4, dataServiceCallback);
                        return;
                    }
                    return;
                case 6:
                    if (serviceProvider != null) {
                        SetInitialAttachApnRequest setInitialAttachApnRequest = message.obj;
                        DataProfile dataProfile2 = setInitialAttachApnRequest.dataProfile;
                        z = setInitialAttachApnRequest.isRoaming;
                        if (setInitialAttachApnRequest.callback != null) {
                            dataServiceCallback = new DataServiceCallback(setInitialAttachApnRequest.callback);
                        }
                        serviceProvider.setInitialAttachApn(dataProfile2, z, dataServiceCallback);
                        return;
                    }
                    return;
                case 7:
                    if (serviceProvider != null) {
                        SetDataProfileRequest setDataProfileRequest = message.obj;
                        List list = setDataProfileRequest.dps;
                        z = setDataProfileRequest.isRoaming;
                        if (setDataProfileRequest.callback != null) {
                            dataServiceCallback = new DataServiceCallback(setDataProfileRequest.callback);
                        }
                        serviceProvider.setDataProfile(list, z, dataServiceCallback);
                        return;
                    }
                    return;
                case 8:
                    if (serviceProvider != null) {
                        serviceProvider.requestDataCallList(new DataServiceCallback((IDataServiceCallback) message.obj));
                        return;
                    }
                    return;
                case 9:
                    if (serviceProvider != null) {
                        serviceProvider.registerForDataCallListChanged((IDataServiceCallback) message.obj);
                        return;
                    }
                    return;
                case 10:
                    if (serviceProvider != null) {
                        serviceProvider.unregisterForDataCallListChanged(message.obj);
                        return;
                    }
                    return;
                case 11:
                    if (serviceProvider != null) {
                        DataCallListChangedIndication indication = message.obj;
                        try {
                            indication.callback.onDataCallListChanged(indication.dataCallList);
                            return;
                        } catch (RemoteException e) {
                            DataService dataService = DataService.this;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Failed to call onDataCallListChanged. ");
                            stringBuilder.append(e);
                            dataService.loge(stringBuilder.toString());
                            return;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public abstract class DataServiceProvider implements AutoCloseable {
        private final List<IDataServiceCallback> mDataCallListChangedCallbacks = new ArrayList();
        private final int mSlotIndex;

        public abstract void close();

        public DataServiceProvider(int slotIndex) {
            this.mSlotIndex = slotIndex;
        }

        public final int getSlotIndex() {
            return this.mSlotIndex;
        }

        public void setupDataCall(int accessNetworkType, DataProfile dataProfile, boolean isRoaming, boolean allowRoaming, int reason, LinkProperties linkProperties, DataServiceCallback callback) {
            if (callback != null) {
                callback.onSetupDataCallComplete(1, null);
            }
        }

        public void deactivateDataCall(int cid, int reason, DataServiceCallback callback) {
            if (callback != null) {
                callback.onDeactivateDataCallComplete(1);
            }
        }

        public void setInitialAttachApn(DataProfile dataProfile, boolean isRoaming, DataServiceCallback callback) {
            if (callback != null) {
                callback.onSetInitialAttachApnComplete(1);
            }
        }

        public void setDataProfile(List<DataProfile> list, boolean isRoaming, DataServiceCallback callback) {
            if (callback != null) {
                callback.onSetDataProfileComplete(1);
            }
        }

        public void requestDataCallList(DataServiceCallback callback) {
            callback.onRequestDataCallListComplete(1, null);
        }

        private void registerForDataCallListChanged(IDataServiceCallback callback) {
            synchronized (this.mDataCallListChangedCallbacks) {
                this.mDataCallListChangedCallbacks.add(callback);
            }
        }

        private void unregisterForDataCallListChanged(IDataServiceCallback callback) {
            synchronized (this.mDataCallListChangedCallbacks) {
                this.mDataCallListChangedCallbacks.remove(callback);
            }
        }

        public final void notifyDataCallListChanged(List<DataCallResponse> dataCallList) {
            synchronized (this.mDataCallListChangedCallbacks) {
                for (IDataServiceCallback callback : this.mDataCallListChangedCallbacks) {
                    DataService.this.mHandler.obtainMessage(11, this.mSlotIndex, 0, new DataCallListChangedIndication(dataCallList, callback)).sendToTarget();
                }
            }
        }
    }

    private static final class DeactivateDataCallRequest {
        public final IDataServiceCallback callback;
        public final int cid;
        public final int reason;

        DeactivateDataCallRequest(int cid, int reason, IDataServiceCallback callback) {
            this.cid = cid;
            this.reason = reason;
            this.callback = callback;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DeactivateDataReason {
    }

    private class IDataServiceWrapper extends Stub {
        private IDataServiceWrapper() {
        }

        public void createDataServiceProvider(int slotIndex) {
            DataService.this.mHandler.obtainMessage(1, slotIndex, 0).sendToTarget();
        }

        public void removeDataServiceProvider(int slotIndex) {
            DataService.this.mHandler.obtainMessage(2, slotIndex, 0).sendToTarget();
        }

        public void setupDataCall(int slotIndex, int accessNetworkType, DataProfile dataProfile, boolean isRoaming, boolean allowRoaming, int reason, LinkProperties linkProperties, IDataServiceCallback callback) {
            int i = slotIndex;
            DataService.this.mHandler.obtainMessage(4, slotIndex, 0, new SetupDataCallRequest(accessNetworkType, dataProfile, isRoaming, allowRoaming, reason, linkProperties, callback)).sendToTarget();
        }

        public void deactivateDataCall(int slotIndex, int cid, int reason, IDataServiceCallback callback) {
            DataService.this.mHandler.obtainMessage(5, slotIndex, 0, new DeactivateDataCallRequest(cid, reason, callback)).sendToTarget();
        }

        public void setInitialAttachApn(int slotIndex, DataProfile dataProfile, boolean isRoaming, IDataServiceCallback callback) {
            DataService.this.mHandler.obtainMessage(6, slotIndex, 0, new SetInitialAttachApnRequest(dataProfile, isRoaming, callback)).sendToTarget();
        }

        public void setDataProfile(int slotIndex, List<DataProfile> dps, boolean isRoaming, IDataServiceCallback callback) {
            DataService.this.mHandler.obtainMessage(7, slotIndex, 0, new SetDataProfileRequest(dps, isRoaming, callback)).sendToTarget();
        }

        public void requestDataCallList(int slotIndex, IDataServiceCallback callback) {
            if (callback == null) {
                DataService.this.loge("requestDataCallList: callback is null");
            } else {
                DataService.this.mHandler.obtainMessage(8, slotIndex, 0, callback).sendToTarget();
            }
        }

        public void registerForDataCallListChanged(int slotIndex, IDataServiceCallback callback) {
            if (callback == null) {
                DataService.this.loge("registerForDataCallListChanged: callback is null");
            } else {
                DataService.this.mHandler.obtainMessage(9, slotIndex, 0, callback).sendToTarget();
            }
        }

        public void unregisterForDataCallListChanged(int slotIndex, IDataServiceCallback callback) {
            if (callback == null) {
                DataService.this.loge("unregisterForDataCallListChanged: callback is null");
            } else {
                DataService.this.mHandler.obtainMessage(10, slotIndex, 0, callback).sendToTarget();
            }
        }
    }

    private static final class SetDataProfileRequest {
        public final IDataServiceCallback callback;
        public final List<DataProfile> dps;
        public final boolean isRoaming;

        SetDataProfileRequest(List<DataProfile> dps, boolean isRoaming, IDataServiceCallback callback) {
            this.dps = dps;
            this.isRoaming = isRoaming;
            this.callback = callback;
        }
    }

    private static final class SetInitialAttachApnRequest {
        public final IDataServiceCallback callback;
        public final DataProfile dataProfile;
        public final boolean isRoaming;

        SetInitialAttachApnRequest(DataProfile dataProfile, boolean isRoaming, IDataServiceCallback callback) {
            this.dataProfile = dataProfile;
            this.isRoaming = isRoaming;
            this.callback = callback;
        }
    }

    private static final class SetupDataCallRequest {
        public final int accessNetworkType;
        public final boolean allowRoaming;
        public final IDataServiceCallback callback;
        public final DataProfile dataProfile;
        public final boolean isRoaming;
        public final LinkProperties linkProperties;
        public final int reason;

        SetupDataCallRequest(int accessNetworkType, DataProfile dataProfile, boolean isRoaming, boolean allowRoaming, int reason, LinkProperties linkProperties, IDataServiceCallback callback) {
            this.accessNetworkType = accessNetworkType;
            this.dataProfile = dataProfile;
            this.isRoaming = isRoaming;
            this.allowRoaming = allowRoaming;
            this.linkProperties = linkProperties;
            this.reason = reason;
            this.callback = callback;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SetupDataReason {
    }

    public abstract DataServiceProvider onCreateDataServiceProvider(int i);

    public DataService() {
        this.mHandlerThread.start();
        this.mHandler = new DataServiceHandler(this.mHandlerThread.getLooper());
        log("Data service created");
    }

    public IBinder onBind(Intent intent) {
        if (intent != null) {
            if (SERVICE_INTERFACE.equals(intent.getAction())) {
                return this.mBinder;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected intent ");
        stringBuilder.append(intent);
        loge(stringBuilder.toString());
        return null;
    }

    public boolean onUnbind(Intent intent) {
        this.mHandler.obtainMessage(3).sendToTarget();
        return false;
    }

    public void onDestroy() {
        this.mHandlerThread.quit();
        super.onDestroy();
    }

    private void log(String s) {
        Rlog.d(TAG, s);
    }

    private void loge(String s) {
        Rlog.e(TAG, s);
    }
}
