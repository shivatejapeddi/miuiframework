package android.app;

import android.app.IUriGrantsManager.Stub;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Singleton;

public class UriGrantsManager {
    private static final Singleton<IUriGrantsManager> IUriGrantsManagerSingleton = new Singleton<IUriGrantsManager>() {
        /* Access modifiers changed, original: protected */
        public IUriGrantsManager create() {
            return Stub.asInterface(ServiceManager.getService(Context.URI_GRANTS_SERVICE));
        }
    };
    private final Context mContext;

    UriGrantsManager(Context context, Handler handler) {
        this.mContext = context;
    }

    public static IUriGrantsManager getService() {
        return (IUriGrantsManager) IUriGrantsManagerSingleton.get();
    }

    public void clearGrantedUriPermissions(String packageName) {
        try {
            getService().clearGrantedUriPermissions(packageName, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ParceledListSlice<GrantedUriPermission> getGrantedUriPermissions(String packageName) {
        try {
            return getService().getGrantedUriPermissions(packageName, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
