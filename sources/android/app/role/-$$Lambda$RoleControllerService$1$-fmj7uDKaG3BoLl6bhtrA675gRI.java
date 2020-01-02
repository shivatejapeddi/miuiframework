package android.app.role;

import android.os.RemoteCallback;
import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RoleControllerService$1$-fmj7uDKaG3BoLl6bhtrA675gRI implements BiConsumer {
    public static final /* synthetic */ -$$Lambda$RoleControllerService$1$-fmj7uDKaG3BoLl6bhtrA675gRI INSTANCE = new -$$Lambda$RoleControllerService$1$-fmj7uDKaG3BoLl6bhtrA675gRI();

    private /* synthetic */ -$$Lambda$RoleControllerService$1$-fmj7uDKaG3BoLl6bhtrA675gRI() {
    }

    public final void accept(Object obj, Object obj2) {
        ((RoleControllerService) ((RoleControllerService) obj)).grantDefaultRoles((RemoteCallback) obj2);
    }
}
