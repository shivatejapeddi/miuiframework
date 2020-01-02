package android.content.pm;

import android.content.pm.PackageInstaller.SessionCallback;
import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ciir_QAmv6RwJro4I58t77dPnxU implements BiConsumer {
    public static final /* synthetic */ -$$Lambda$ciir_QAmv6RwJro4I58t77dPnxU INSTANCE = new -$$Lambda$ciir_QAmv6RwJro4I58t77dPnxU();

    private /* synthetic */ -$$Lambda$ciir_QAmv6RwJro4I58t77dPnxU() {
    }

    public final void accept(Object obj, Object obj2) {
        ((SessionCallback) obj).onCreated(((Integer) obj2).intValue());
    }
}
