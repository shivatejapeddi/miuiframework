package android.telephony;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.INetworkService.Stub;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;

@SystemApi
public abstract class NetworkService extends Service {
    private static final int NETWORK_SERVICE_CREATE_NETWORK_SERVICE_PROVIDER = 1;
    private static final int NETWORK_SERVICE_GET_REGISTRATION_INFO = 4;
    private static final int NETWORK_SERVICE_INDICATION_NETWORK_INFO_CHANGED = 7;
    private static final int NETWORK_SERVICE_REGISTER_FOR_INFO_CHANGE = 5;
    private static final int NETWORK_SERVICE_REMOVE_ALL_NETWORK_SERVICE_PROVIDERS = 3;
    private static final int NETWORK_SERVICE_REMOVE_NETWORK_SERVICE_PROVIDER = 2;
    private static final int NETWORK_SERVICE_UNREGISTER_FOR_INFO_CHANGE = 6;
    public static final String SERVICE_INTERFACE = "android.telephony.NetworkService";
    private final String TAG = NetworkService.class.getSimpleName();
    @VisibleForTesting
    public final INetworkServiceWrapper mBinder = new INetworkServiceWrapper();
    private final NetworkServiceHandler mHandler;
    private final HandlerThread mHandlerThread = new HandlerThread(this.TAG);
    private final SparseArray<NetworkServiceProvider> mServiceMap = new SparseArray();

    private class INetworkServiceWrapper extends Stub {
        private INetworkServiceWrapper() {
        }

        public void createNetworkServiceProvider(int slotIndex) {
            NetworkService.this.mHandler.obtainMessage(1, slotIndex, 0, null).sendToTarget();
        }

        public void removeNetworkServiceProvider(int slotIndex) {
            NetworkService.this.mHandler.obtainMessage(2, slotIndex, 0, null).sendToTarget();
        }

        public void requestNetworkRegistrationInfo(int slotIndex, int domain, INetworkServiceCallback callback) {
            NetworkService.this.mHandler.obtainMessage(4, slotIndex, domain, callback).sendToTarget();
        }

        public void registerForNetworkRegistrationInfoChanged(int slotIndex, INetworkServiceCallback callback) {
            NetworkService.this.mHandler.obtainMessage(5, slotIndex, 0, callback).sendToTarget();
        }

        public void unregisterForNetworkRegistrationInfoChanged(int slotIndex, INetworkServiceCallback callback) {
            NetworkService.this.mHandler.obtainMessage(6, slotIndex, 0, callback).sendToTarget();
        }
    }

    private class NetworkServiceHandler extends Handler {
        NetworkServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int slotIndex = message.arg1;
            INetworkServiceCallback callback = message.obj;
            NetworkServiceProvider serviceProvider = (NetworkServiceProvider) NetworkService.this.mServiceMap.get(slotIndex);
            switch (message.what) {
                case 1:
                    if (serviceProvider == null) {
                        NetworkService.this.mServiceMap.put(slotIndex, NetworkService.this.onCreateNetworkServiceProvider(slotIndex));
                        return;
                    }
                    return;
                case 2:
                    if (serviceProvider != null) {
                        serviceProvider.close();
                        NetworkService.this.mServiceMap.remove(slotIndex);
                        return;
                    }
                    return;
                case 3:
                    for (int i = 0; i < NetworkService.this.mServiceMap.size(); i++) {
                        serviceProvider = (NetworkServiceProvider) NetworkService.this.mServiceMap.get(i);
                        if (serviceProvider != null) {
                            serviceProvider.close();
                        }
                    }
                    NetworkService.this.mServiceMap.clear();
                    return;
                case 4:
                    if (serviceProvider != null) {
                        serviceProvider.requestNetworkRegistrationInfo(message.arg2, new NetworkServiceCallback(callback));
                        return;
                    }
                    return;
                case 5:
                    if (serviceProvider != null) {
                        serviceProvider.registerForInfoChanged(callback);
                        return;
                    }
                    return;
                case 6:
                    if (serviceProvider != null) {
                        serviceProvider.unregisterForInfoChanged(callback);
                        return;
                    }
                    return;
                case 7:
                    if (serviceProvider != null) {
                        serviceProvider.notifyInfoChangedToCallbacks();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public abstract class NetworkServiceProvider implements AutoCloseable {
        private final List<INetworkServiceCallback> mNetworkRegistrationInfoChangedCallbacks = new ArrayList();
        private final int mSlotIndex;

        public abstract void close();

        public NetworkServiceProvider(int slotIndex) {
            this.mSlotIndex = slotIndex;
        }

        public final int getSlotIndex() {
            return this.mSlotIndex;
        }

        public void requestNetworkRegistrationInfo(int domain, NetworkServiceCallback callback) {
            callback.onRequestNetworkRegistrationInfoComplete(1, null);
        }

        public final void notifyNetworkRegistrationInfoChanged() {
            NetworkService.this.mHandler.obtainMessage(7, this.mSlotIndex, 0, null).sendToTarget();
        }

        private void registerForInfoChanged(INetworkServiceCallback callback) {
            synchronized (this.mNetworkRegistrationInfoChangedCallbacks) {
                this.mNetworkRegistrationInfoChangedCallbacks.add(callback);
            }
        }

        private void unregisterForInfoChanged(INetworkServiceCallback callback) {
            synchronized (this.mNetworkRegistrationInfoChangedCallbacks) {
                this.mNetworkRegistrationInfoChangedCallbacks.remove(callback);
            }
        }

        private void notifyInfoChangedToCallbacks() {
            for (INetworkServiceCallback callback : this.mNetworkRegistrationInfoChangedCallbacks) {
                try {
                    callback.onNetworkStateChanged();
                } catch (RemoteException e) {
                }
            }
        }
    }

    public abstract NetworkServiceProvider onCreateNetworkServiceProvider(int i);

    public NetworkService() {
        this.mHandlerThread.start();
        this.mHandler = new NetworkServiceHandler(this.mHandlerThread.getLooper());
        log("network service created");
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
        this.mHandler.obtainMessage(3, 0, 0, null).sendToTarget();
        return false;
    }

    public void onDestroy() {
        this.mHandlerThread.quit();
        super.onDestroy();
    }

    private final void log(String s) {
        Rlog.d(this.TAG, s);
    }

    private final void loge(String s) {
        Rlog.e(this.TAG, s);
    }
}
