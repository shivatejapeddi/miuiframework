package android.app.role;

import android.os.UserHandle;
import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$o94o2jK_ei-IVw-3oY_QJ49zpAA implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$o94o2jK_ei-IVw-3oY_QJ49zpAA INSTANCE = new -$$Lambda$o94o2jK_ei-IVw-3oY_QJ49zpAA();

    private /* synthetic */ -$$Lambda$o94o2jK_ei-IVw-3oY_QJ49zpAA() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((OnRoleHoldersChangedListener) obj).onRoleHoldersChanged((String) obj2, (UserHandle) obj3);
    }
}
