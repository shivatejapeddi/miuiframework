package android.content.pm;

import android.content.pm.PackageInstaller.SessionCallback;
import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$zO9HBUVgPeroyDQPLJE-MNMvSqc implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$zO9HBUVgPeroyDQPLJE-MNMvSqc INSTANCE = new -$$Lambda$zO9HBUVgPeroyDQPLJE-MNMvSqc();

    private /* synthetic */ -$$Lambda$zO9HBUVgPeroyDQPLJE-MNMvSqc() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((SessionCallback) obj).onFinished(((Integer) obj2).intValue(), ((Boolean) obj3).booleanValue());
    }
}
