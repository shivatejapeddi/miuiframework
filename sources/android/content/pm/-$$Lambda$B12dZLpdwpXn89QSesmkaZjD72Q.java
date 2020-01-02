package android.content.pm;

import android.content.pm.PackageInstaller.SessionCallback;
import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$B12dZLpdwpXn89QSesmkaZjD72Q implements BiConsumer {
    public static final /* synthetic */ -$$Lambda$B12dZLpdwpXn89QSesmkaZjD72Q INSTANCE = new -$$Lambda$B12dZLpdwpXn89QSesmkaZjD72Q();

    private /* synthetic */ -$$Lambda$B12dZLpdwpXn89QSesmkaZjD72Q() {
    }

    public final void accept(Object obj, Object obj2) {
        ((SessionCallback) obj).onBadgingChanged(((Integer) obj2).intValue());
    }
}
