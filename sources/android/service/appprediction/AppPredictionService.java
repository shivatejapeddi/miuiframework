package android.service.appprediction;

import android.annotation.SystemApi;
import android.app.Service;
import android.app.prediction.AppPredictionContext;
import android.app.prediction.AppPredictionSessionId;
import android.app.prediction.AppTarget;
import android.app.prediction.AppTargetEvent;
import android.app.prediction.AppTargetId;
import android.app.prediction.IPredictionCallback;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.RemoteException;
import android.service.appprediction.IPredictionService.Stub;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SystemApi
public abstract class AppPredictionService extends Service {
    public static final String SERVICE_INTERFACE = "android.service.appprediction.AppPredictionService";
    private static final String TAG = "AppPredictionService";
    private Handler mHandler;
    private final IPredictionService mInterface = new Stub() {
        public void onCreatePredictionSession(AppPredictionContext context, AppPredictionSessionId sessionId) {
            AppPredictionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$AppPredictionService$1$dlPwi16n_6u5po2eN8wlW4I1bRw.INSTANCE, AppPredictionService.this, context, sessionId));
        }

        public void notifyAppTargetEvent(AppPredictionSessionId sessionId, AppTargetEvent event) {
            AppPredictionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$L76XW8q2NG5cTm3_D3JVX8JtaW0.INSTANCE, AppPredictionService.this, sessionId, event));
        }

        public void notifyLaunchLocationShown(AppPredictionSessionId sessionId, String launchLocation, ParceledListSlice targetIds) {
            AppPredictionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$GvHA1SFwOCThMjcs4Yg4JTLin4Y.INSTANCE, AppPredictionService.this, sessionId, launchLocation, targetIds.getList()));
        }

        public void sortAppTargets(AppPredictionSessionId sessionId, ParceledListSlice targets, IPredictionCallback callback) {
            AppPredictionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$hL9oFxwFQPM7PIyu9fQyFqB_mBk.INSTANCE, AppPredictionService.this, sessionId, targets.getList(), null, new CallbackWrapper(callback, null)));
        }

        public void registerPredictionUpdates(AppPredictionSessionId sessionId, IPredictionCallback callback) {
            AppPredictionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$AppPredictionService$1$CDfn7BNaxDP2sak-07muIxqD0XM.INSTANCE, AppPredictionService.this, sessionId, callback));
        }

        public void unregisterPredictionUpdates(AppPredictionSessionId sessionId, IPredictionCallback callback) {
            AppPredictionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$AppPredictionService$1$3o4A2wryMBwv4mIbcQKrEaoUyik.INSTANCE, AppPredictionService.this, sessionId, callback));
        }

        public void requestPredictionUpdate(AppPredictionSessionId sessionId) {
            AppPredictionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$AppPredictionService$1$oaGU8LD9Stlihi_KoW_pb0jZjQk.INSTANCE, AppPredictionService.this, sessionId));
        }

        public void onDestroyPredictionSession(AppPredictionSessionId sessionId) {
            AppPredictionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$AppPredictionService$1$oZsrXgV2j_8Zo7GiDdpYvbTz4h8.INSTANCE, AppPredictionService.this, sessionId));
        }
    };
    private final ArrayMap<AppPredictionSessionId, ArrayList<CallbackWrapper>> mSessionCallbacks = new ArrayMap();

    private static final class CallbackWrapper implements Consumer<List<AppTarget>>, DeathRecipient {
        private IPredictionCallback mCallback;
        private final Consumer<CallbackWrapper> mOnBinderDied;

        CallbackWrapper(IPredictionCallback callback, Consumer<CallbackWrapper> onBinderDied) {
            this.mCallback = callback;
            this.mOnBinderDied = onBinderDied;
            try {
                this.mCallback.asBinder().linkToDeath(this, 0);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to link to death: ");
                stringBuilder.append(e);
                Slog.e(AppPredictionService.TAG, stringBuilder.toString());
            }
        }

        public boolean isCallback(IPredictionCallback callback) {
            IPredictionCallback iPredictionCallback = this.mCallback;
            if (iPredictionCallback != null) {
                return iPredictionCallback.equals(callback);
            }
            Slog.e(AppPredictionService.TAG, "Callback is null, likely the binder has died.");
            return false;
        }

        public void accept(List<AppTarget> ts) {
            try {
                if (this.mCallback != null) {
                    this.mCallback.onResult(new ParceledListSlice(ts));
                }
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error sending result:");
                stringBuilder.append(e);
                Slog.e(AppPredictionService.TAG, stringBuilder.toString());
            }
        }

        public void binderDied() {
            this.mCallback = null;
            Consumer consumer = this.mOnBinderDied;
            if (consumer != null) {
                consumer.accept(this);
            }
        }
    }

    public abstract void onAppTargetEvent(AppPredictionSessionId appPredictionSessionId, AppTargetEvent appTargetEvent);

    public abstract void onLaunchLocationShown(AppPredictionSessionId appPredictionSessionId, String str, List<AppTargetId> list);

    public abstract void onRequestPredictionUpdate(AppPredictionSessionId appPredictionSessionId);

    public abstract void onSortAppTargets(AppPredictionSessionId appPredictionSessionId, List<AppTarget> list, CancellationSignal cancellationSignal, Consumer<List<AppTarget>> consumer);

    public void onCreate() {
        super.onCreate();
        this.mHandler = new Handler(Looper.getMainLooper(), null, true);
    }

    public final IBinder onBind(Intent intent) {
        if (SERVICE_INTERFACE.equals(intent.getAction())) {
            return this.mInterface.asBinder();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tried to bind to wrong intent (should be android.service.appprediction.AppPredictionService: ");
        stringBuilder.append(intent);
        Log.w(TAG, stringBuilder.toString());
        return null;
    }

    private void doCreatePredictionSession(AppPredictionContext context, AppPredictionSessionId sessionId) {
        this.mSessionCallbacks.put(sessionId, new ArrayList());
        onCreatePredictionSession(context, sessionId);
    }

    public void onCreatePredictionSession(AppPredictionContext context, AppPredictionSessionId sessionId) {
    }

    private void doRegisterPredictionUpdates(AppPredictionSessionId sessionId, IPredictionCallback callback) {
        ArrayList<CallbackWrapper> callbacks = (ArrayList) this.mSessionCallbacks.get(sessionId);
        if (callbacks == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to register for updates for unknown session: ");
            stringBuilder.append(sessionId);
            Slog.e(TAG, stringBuilder.toString());
            return;
        }
        if (findCallbackWrapper(callbacks, callback) == null) {
            callbacks.add(new CallbackWrapper(callback, new -$$Lambda$AppPredictionService$BU3RVDaz_RDf_0tC58L6QbapMAs(this, callbacks)));
            if (callbacks.size() == 1) {
                onStartPredictionUpdates();
            }
        }
    }

    public /* synthetic */ void lambda$doRegisterPredictionUpdates$0$AppPredictionService(ArrayList callbacks, CallbackWrapper callbackWrapper) {
        removeCallbackWrapper(callbacks, callbackWrapper);
    }

    public /* synthetic */ void lambda$doRegisterPredictionUpdates$1$AppPredictionService(ArrayList callbacks, CallbackWrapper callbackWrapper) {
        this.mHandler.post(new -$$Lambda$AppPredictionService$QdiGSCeMaWGP0DGJNn4uhqgT9ZA(this, callbacks, callbackWrapper));
    }

    public void onStartPredictionUpdates() {
    }

    private void doUnregisterPredictionUpdates(AppPredictionSessionId sessionId, IPredictionCallback callback) {
        ArrayList<CallbackWrapper> callbacks = (ArrayList) this.mSessionCallbacks.get(sessionId);
        if (callbacks == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to unregister for updates for unknown session: ");
            stringBuilder.append(sessionId);
            Slog.e(TAG, stringBuilder.toString());
            return;
        }
        CallbackWrapper wrapper = findCallbackWrapper(callbacks, callback);
        if (wrapper != null) {
            removeCallbackWrapper(callbacks, wrapper);
        }
    }

    private void removeCallbackWrapper(ArrayList<CallbackWrapper> callbacks, CallbackWrapper wrapper) {
        if (callbacks != null) {
            callbacks.remove(wrapper);
            if (callbacks.isEmpty()) {
                onStopPredictionUpdates();
            }
        }
    }

    public void onStopPredictionUpdates() {
    }

    private void doRequestPredictionUpdate(AppPredictionSessionId sessionId) {
        ArrayList<CallbackWrapper> callbacks = (ArrayList) this.mSessionCallbacks.get(sessionId);
        if (callbacks != null && !callbacks.isEmpty()) {
            onRequestPredictionUpdate(sessionId);
        }
    }

    private void doDestroyPredictionSession(AppPredictionSessionId sessionId) {
        this.mSessionCallbacks.remove(sessionId);
        onDestroyPredictionSession(sessionId);
    }

    public void onDestroyPredictionSession(AppPredictionSessionId sessionId) {
    }

    public final void updatePredictions(AppPredictionSessionId sessionId, List<AppTarget> targets) {
        List<CallbackWrapper> callbacks = (List) this.mSessionCallbacks.get(sessionId);
        if (callbacks != null) {
            for (CallbackWrapper callback : callbacks) {
                callback.accept((List) targets);
            }
        }
    }

    private CallbackWrapper findCallbackWrapper(ArrayList<CallbackWrapper> callbacks, IPredictionCallback callback) {
        for (int i = callbacks.size() - 1; i >= 0; i--) {
            if (((CallbackWrapper) callbacks.get(i)).isCallback(callback)) {
                return (CallbackWrapper) callbacks.get(i);
            }
        }
        return null;
    }
}
