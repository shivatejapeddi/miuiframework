package android.permissionpresenterservice;

import android.os.RemoteCallback;
import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RuntimePermissionPresenterService$1$hIxcH5_fyEVhEY0Z-wjDuhvJriA implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$RuntimePermissionPresenterService$1$hIxcH5_fyEVhEY0Z-wjDuhvJriA INSTANCE = new -$$Lambda$RuntimePermissionPresenterService$1$hIxcH5_fyEVhEY0Z-wjDuhvJriA();

    private /* synthetic */ -$$Lambda$RuntimePermissionPresenterService$1$hIxcH5_fyEVhEY0Z-wjDuhvJriA() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((RuntimePermissionPresenterService) ((RuntimePermissionPresenterService) obj)).getAppPermissions((String) obj2, (RemoteCallback) obj3);
    }
}
