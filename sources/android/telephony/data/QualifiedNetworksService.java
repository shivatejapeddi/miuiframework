package android.telephony.data;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.Rlog;
import android.telephony.data.IQualifiedNetworksService.Stub;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import java.util.List;

@SystemApi
public abstract class QualifiedNetworksService extends Service {
    private static final int QNS_CREATE_NETWORK_AVAILABILITY_PROVIDER = 1;
    private static final int QNS_REMOVE_ALL_NETWORK_AVAILABILITY_PROVIDERS = 3;
    private static final int QNS_REMOVE_NETWORK_AVAILABILITY_PROVIDER = 2;
    private static final int QNS_UPDATE_QUALIFIED_NETWORKS = 4;
    public static final String QUALIFIED_NETWORKS_SERVICE_INTERFACE = "android.telephony.data.QualifiedNetworksService";
    private static final String TAG = QualifiedNetworksService.class.getSimpleName();
    @VisibleForTesting
    public final IQualifiedNetworksServiceWrapper mBinder = new IQualifiedNetworksServiceWrapper();
    private final QualifiedNetworksServiceHandler mHandler;
    private final HandlerThread mHandlerThread = new HandlerThread(TAG);
    private final SparseArray<NetworkAvailabilityProvider> mProviders = new SparseArray();

    private class IQualifiedNetworksServiceWrapper extends Stub {
        private IQualifiedNetworksServiceWrapper() {
        }

        public void createNetworkAvailabilityProvider(int slotIndex, IQualifiedNetworksServiceCallback callback) {
            QualifiedNetworksService.this.mHandler.obtainMessage(1, slotIndex, 0, callback).sendToTarget();
        }

        public void removeNetworkAvailabilityProvider(int slotIndex) {
            QualifiedNetworksService.this.mHandler.obtainMessage(2, slotIndex, 0).sendToTarget();
        }
    }

    public abstract class NetworkAvailabilityProvider implements AutoCloseable {
        private IQualifiedNetworksServiceCallback mCallback;
        private SparseArray<int[]> mQualifiedNetworkTypesList = new SparseArray();
        private final int mSlotIndex;

        public abstract void close();

        public NetworkAvailabilityProvider(int slotIndex) {
            this.mSlotIndex = slotIndex;
        }

        public final int getSlotIndex() {
            return this.mSlotIndex;
        }

        private void registerForQualifiedNetworkTypesChanged(IQualifiedNetworksServiceCallback callback) {
            this.mCallback = callback;
            if (this.mCallback != null) {
                for (int i = 0; i < this.mQualifiedNetworkTypesList.size(); i++) {
                    try {
                        this.mCallback.onQualifiedNetworkTypesChanged(this.mQualifiedNetworkTypesList.keyAt(i), (int[]) this.mQualifiedNetworkTypesList.valueAt(i));
                    } catch (RemoteException e) {
                        QualifiedNetworksService qualifiedNetworksService = QualifiedNetworksService.this;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed to call onQualifiedNetworksChanged. ");
                        stringBuilder.append(e);
                        qualifiedNetworksService.loge(stringBuilder.toString());
                    }
                }
            }
        }

        public final void updateQualifiedNetworkTypes(int apnTypes, List<Integer> qualifiedNetworkTypes) {
            QualifiedNetworksService.this.mHandler.obtainMessage(4, this.mSlotIndex, apnTypes, qualifiedNetworkTypes.stream().mapToInt(-$$Lambda$QualifiedNetworksService$NetworkAvailabilityProvider$sNPqwkqArvqymBmHYmxAc4rF5Es.INSTANCE).toArray()).sendToTarget();
        }

        private void onUpdateQualifiedNetworkTypes(int apnTypes, int[] qualifiedNetworkTypes) {
            this.mQualifiedNetworkTypesList.put(apnTypes, qualifiedNetworkTypes);
            IQualifiedNetworksServiceCallback iQualifiedNetworksServiceCallback = this.mCallback;
            if (iQualifiedNetworksServiceCallback != null) {
                try {
                    iQualifiedNetworksServiceCallback.onQualifiedNetworkTypesChanged(apnTypes, qualifiedNetworkTypes);
                } catch (RemoteException e) {
                    QualifiedNetworksService qualifiedNetworksService = QualifiedNetworksService.this;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to call onQualifiedNetworksChanged. ");
                    stringBuilder.append(e);
                    qualifiedNetworksService.loge(stringBuilder.toString());
                }
            }
        }
    }

    private class QualifiedNetworksServiceHandler extends Handler {
        QualifiedNetworksServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int slotIndex = message.arg1;
            NetworkAvailabilityProvider provider = (NetworkAvailabilityProvider) QualifiedNetworksService.this.mProviders.get(slotIndex);
            int i = message.what;
            QualifiedNetworksService qualifiedNetworksService;
            StringBuilder stringBuilder;
            if (i != 1) {
                if (i != 2) {
                    if (i == 3) {
                        for (i = 0; i < QualifiedNetworksService.this.mProviders.size(); i++) {
                            provider = (NetworkAvailabilityProvider) QualifiedNetworksService.this.mProviders.get(i);
                            if (provider != null) {
                                provider.close();
                            }
                        }
                        QualifiedNetworksService.this.mProviders.clear();
                    } else if (i == 4 && provider != null) {
                        provider.onUpdateQualifiedNetworkTypes(message.arg2, (int[]) message.obj);
                    }
                } else if (provider != null) {
                    provider.close();
                    QualifiedNetworksService.this.mProviders.remove(slotIndex);
                }
            } else if (QualifiedNetworksService.this.mProviders.get(slotIndex) != null) {
                qualifiedNetworksService = QualifiedNetworksService.this;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Network availability provider for slot ");
                stringBuilder.append(slotIndex);
                stringBuilder.append(" already existed.");
                qualifiedNetworksService.loge(stringBuilder.toString());
            } else {
                provider = QualifiedNetworksService.this.onCreateNetworkAvailabilityProvider(slotIndex);
                if (provider != null) {
                    QualifiedNetworksService.this.mProviders.put(slotIndex, provider);
                    provider.registerForQualifiedNetworkTypesChanged(message.obj);
                } else {
                    qualifiedNetworksService = QualifiedNetworksService.this;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to create network availability provider. slot index = ");
                    stringBuilder.append(slotIndex);
                    qualifiedNetworksService.loge(stringBuilder.toString());
                }
            }
        }
    }

    public abstract NetworkAvailabilityProvider onCreateNetworkAvailabilityProvider(int i);

    public QualifiedNetworksService() {
        this.mHandlerThread.start();
        this.mHandler = new QualifiedNetworksServiceHandler(this.mHandlerThread.getLooper());
        log("Qualified networks service created");
    }

    public IBinder onBind(Intent intent) {
        if (intent != null) {
            if (QUALIFIED_NETWORKS_SERVICE_INTERFACE.equals(intent.getAction())) {
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
    }

    private void log(String s) {
        Rlog.d(TAG, s);
    }

    private void loge(String s) {
        Rlog.e(TAG, s);
    }
}
