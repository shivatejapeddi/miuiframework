package android.permissionpresenterservice;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.permission.IRuntimePermissionPresenter.Stub;
import android.content.pm.permission.RuntimePermissionPresentationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallback;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.List;

@SystemApi
@Deprecated
public abstract class RuntimePermissionPresenterService extends Service {
    private static final String KEY_RESULT = "android.content.pm.permission.RuntimePermissionPresenter.key.result";
    public static final String SERVICE_INTERFACE = "android.permissionpresenterservice.RuntimePermissionPresenterService";
    private Handler mHandler;

    public abstract List<RuntimePermissionPresentationInfo> onGetAppPermissions(String str);

    public final void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mHandler = new Handler(base.getMainLooper());
    }

    public final IBinder onBind(Intent intent) {
        return new Stub() {
            public void getAppPermissions(String packageName, RemoteCallback callback) {
                Preconditions.checkNotNull(packageName, "packageName");
                Preconditions.checkNotNull(callback, "callback");
                RuntimePermissionPresenterService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$RuntimePermissionPresenterService$1$hIxcH5_fyEVhEY0Z-wjDuhvJriA.INSTANCE, RuntimePermissionPresenterService.this, packageName, callback));
            }
        };
    }

    private void getAppPermissions(String packageName, RemoteCallback callback) {
        List<RuntimePermissionPresentationInfo> permissions = onGetAppPermissions(packageName);
        if (permissions == null || permissions.isEmpty()) {
            callback.sendResult(null);
            return;
        }
        Bundle result = new Bundle();
        result.putParcelableList(KEY_RESULT, permissions);
        callback.sendResult(result);
    }
}
