package android.inputmethodservice;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.MultiClientInputMethodServiceDelegate.ClientCallback;
import android.inputmethodservice.MultiClientInputMethodServiceDelegate.ServiceCallback;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.InputChannel;
import android.view.KeyEvent.DispatcherState;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.inputmethod.IMultiClientInputMethod.Stub;
import com.android.internal.inputmethod.IMultiClientInputMethodPrivilegedOperations;
import com.android.internal.inputmethod.MultiClientInputMethodPrivilegedOperations;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

final class MultiClientInputMethodServiceDelegateImpl {
    private static final String TAG = "MultiClientInputMethodServiceDelegateImpl";
    private final Context mContext;
    @GuardedBy({"mLock"})
    private int mInitializationPhase = 1;
    private final Object mLock = new Object();
    private final MultiClientInputMethodPrivilegedOperations mPrivOps = new MultiClientInputMethodPrivilegedOperations();
    private final ServiceCallback mServiceCallback;

    @Retention(RetentionPolicy.SOURCE)
    private @interface InitializationPhase {
        public static final int INITIALIZE_CALLED = 3;
        public static final int INSTANTIATED = 1;
        public static final int ON_BIND_CALLED = 2;
        public static final int ON_DESTROY_CALLED = 5;
        public static final int ON_UNBIND_CALLED = 4;
    }

    private static final class ServiceImpl extends Stub {
        private final WeakReference<MultiClientInputMethodServiceDelegateImpl> mImpl;

        ServiceImpl(MultiClientInputMethodServiceDelegateImpl service) {
            this.mImpl = new WeakReference(service);
        }

        public void initialize(IMultiClientInputMethodPrivilegedOperations privOps) {
            MultiClientInputMethodServiceDelegateImpl service = (MultiClientInputMethodServiceDelegateImpl) this.mImpl.get();
            if (service != null) {
                synchronized (service.mLock) {
                    if (service.mInitializationPhase != 2) {
                        String str = MultiClientInputMethodServiceDelegateImpl.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("unexpected state=");
                        stringBuilder.append(service.mInitializationPhase);
                        Log.e(str, stringBuilder.toString());
                    } else {
                        service.mPrivOps.set(privOps);
                        service.mInitializationPhase = 3;
                        service.mServiceCallback.initialized();
                    }
                }
            }
        }

        public void addClient(int clientId, int uid, int pid, int selfReportedDisplayId) {
            MultiClientInputMethodServiceDelegateImpl service = (MultiClientInputMethodServiceDelegateImpl) this.mImpl.get();
            if (service != null) {
                service.mServiceCallback.addClient(clientId, uid, pid, selfReportedDisplayId);
            }
        }

        public void removeClient(int clientId) {
            MultiClientInputMethodServiceDelegateImpl service = (MultiClientInputMethodServiceDelegateImpl) this.mImpl.get();
            if (service != null) {
                service.mServiceCallback.removeClient(clientId);
            }
        }
    }

    MultiClientInputMethodServiceDelegateImpl(Context context, ServiceCallback serviceCallback) {
        this.mContext = context;
        this.mServiceCallback = serviceCallback;
    }

    /* Access modifiers changed, original: 0000 */
    public void onDestroy() {
        synchronized (this.mLock) {
            int i = this.mInitializationPhase;
            if (i == 1 || i == 4) {
                this.mInitializationPhase = 5;
            } else {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unexpected state=");
                stringBuilder.append(this.mInitializationPhase);
                Log.e(str, stringBuilder.toString());
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public IBinder onBind(Intent intent) {
        synchronized (this.mLock) {
            if (this.mInitializationPhase != 1) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unexpected state=");
                stringBuilder.append(this.mInitializationPhase);
                Log.e(str, stringBuilder.toString());
                return null;
            }
            this.mInitializationPhase = 2;
            ServiceImpl serviceImpl = new ServiceImpl(this);
            return serviceImpl;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean onUnbind(Intent intent) {
        synchronized (this.mLock) {
            int i = this.mInitializationPhase;
            if (i == 2 || i == 3) {
                this.mInitializationPhase = 4;
                this.mPrivOps.dispose();
            } else {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unexpected state=");
                stringBuilder.append(this.mInitializationPhase);
                Log.e(str, stringBuilder.toString());
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public IBinder createInputMethodWindowToken(int displayId) {
        return this.mPrivOps.createInputMethodWindowToken(displayId);
    }

    /* Access modifiers changed, original: 0000 */
    public void acceptClient(int clientId, ClientCallback clientCallback, DispatcherState dispatcherState, Looper looper) {
        InputChannel[] channels = InputChannel.openInputChannelPair("MSIMS-session");
        InputChannel writeChannel = channels[null];
        try {
            MultiClientInputMethodClientCallbackAdaptor callbackAdaptor = new MultiClientInputMethodClientCallbackAdaptor(clientCallback, looper, dispatcherState, channels[1]);
            this.mPrivOps.acceptClient(clientId, callbackAdaptor.createIInputMethodSession(), callbackAdaptor.createIMultiClientInputMethodSession(), writeChannel);
        } finally {
            writeChannel.dispose();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void reportImeWindowTarget(int clientId, int targetWindowHandle, IBinder imeWindowToken) {
        this.mPrivOps.reportImeWindowTarget(clientId, targetWindowHandle, imeWindowToken);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isUidAllowedOnDisplay(int displayId, int uid) {
        return this.mPrivOps.isUidAllowedOnDisplay(displayId, uid);
    }

    /* Access modifiers changed, original: 0000 */
    public void setActive(int clientId, boolean active) {
        this.mPrivOps.setActive(clientId, active);
    }
}
