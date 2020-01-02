package android.content.pm;

import android.content.pm.PackageInstaller.SessionCallback;
import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$T1UQAuePWRRmVQ1KzTyMAktZUPM implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$T1UQAuePWRRmVQ1KzTyMAktZUPM INSTANCE = new -$$Lambda$T1UQAuePWRRmVQ1KzTyMAktZUPM();

    private /* synthetic */ -$$Lambda$T1UQAuePWRRmVQ1KzTyMAktZUPM() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((SessionCallback) obj).onActiveChanged(((Integer) obj2).intValue(), ((Boolean) obj3).booleanValue());
    }
}
