package android.app;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.os.RemoteException;
import android.service.vr.IPersistentVrStateCallbacks;
import android.service.vr.IPersistentVrStateCallbacks.Stub;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.util.ArrayMap;
import java.util.Map;
import java.util.concurrent.Executor;

@SystemApi
public class VrManager {
    private Map<VrStateCallback, CallbackEntry> mCallbackMap = new ArrayMap();
    @UnsupportedAppUsage
    private final IVrManager mService;

    private static class CallbackEntry {
        final VrStateCallback mCallback;
        final Executor mExecutor;
        final IPersistentVrStateCallbacks mPersistentStateCallback = new Stub() {
            public /* synthetic */ void lambda$onPersistentVrStateChanged$0$VrManager$CallbackEntry$2(boolean enabled) {
                CallbackEntry.this.mCallback.onPersistentVrStateChanged(enabled);
            }

            public void onPersistentVrStateChanged(boolean enabled) {
                CallbackEntry.this.mExecutor.execute(new -$$Lambda$VrManager$CallbackEntry$2$KvHLIXm3-7igcOqTEl46YdjhHMk(this, enabled));
            }
        };
        final IVrStateCallbacks mStateCallback = new IVrStateCallbacks.Stub() {
            public /* synthetic */ void lambda$onVrStateChanged$0$VrManager$CallbackEntry$1(boolean enabled) {
                CallbackEntry.this.mCallback.onVrStateChanged(enabled);
            }

            public void onVrStateChanged(boolean enabled) {
                CallbackEntry.this.mExecutor.execute(new -$$Lambda$VrManager$CallbackEntry$1$rgUBVVG1QhelpvAp8W3UQHDHJdU(this, enabled));
            }
        };

        CallbackEntry(VrStateCallback callback, Executor executor) {
            this.mCallback = callback;
            this.mExecutor = executor;
        }
    }

    public VrManager(IVrManager service) {
        this.mService = service;
    }

    public void registerVrStateCallback(Executor executor, VrStateCallback callback) {
        if (callback != null && !this.mCallbackMap.containsKey(callback)) {
            CallbackEntry entry = new CallbackEntry(callback, executor);
            this.mCallbackMap.put(callback, entry);
            try {
                this.mService.registerListener(entry.mStateCallback);
                this.mService.registerPersistentVrStateListener(entry.mPersistentStateCallback);
            } catch (RemoteException e) {
                try {
                    unregisterVrStateCallback(callback);
                } catch (Exception e2) {
                    e.rethrowFromSystemServer();
                }
            }
        }
    }

    public void unregisterVrStateCallback(VrStateCallback callback) {
        CallbackEntry entry = (CallbackEntry) this.mCallbackMap.remove(callback);
        if (entry != null) {
            try {
                this.mService.unregisterListener(entry.mStateCallback);
            } catch (RemoteException e) {
            }
            try {
                this.mService.unregisterPersistentVrStateListener(entry.mPersistentStateCallback);
            } catch (RemoteException e2) {
            }
        }
    }

    public boolean isVrModeEnabled() {
        try {
            return this.mService.getVrModeState();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean isPersistentVrModeEnabled() {
        try {
            return this.mService.getPersistentVrModeEnabled();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public void setPersistentVrModeEnabled(boolean enabled) {
        try {
            this.mService.setPersistentVrModeEnabled(enabled);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void setVr2dDisplayProperties(Vr2dDisplayProperties vr2dDisplayProp) {
        try {
            this.mService.setVr2dDisplayProperties(vr2dDisplayProp);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void setAndBindVrCompositor(ComponentName componentName) {
        try {
            this.mService.setAndBindCompositor(componentName == null ? null : componentName.flattenToString());
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void setStandbyEnabled(boolean standby) {
        try {
            this.mService.setStandbyEnabled(standby);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void setVrInputMethod(ComponentName componentName) {
    }

    public int getVr2dDisplayId() {
        try {
            return this.mService.getVr2dDisplayId();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return -1;
        }
    }
}
