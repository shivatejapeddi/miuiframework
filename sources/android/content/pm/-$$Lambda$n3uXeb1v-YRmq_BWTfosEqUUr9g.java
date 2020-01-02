package android.content.pm;

import android.content.pm.PackageInstaller.SessionCallback;
import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$n3uXeb1v-YRmq_BWTfosEqUUr9g implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$n3uXeb1v-YRmq_BWTfosEqUUr9g INSTANCE = new -$$Lambda$n3uXeb1v-YRmq_BWTfosEqUUr9g();

    private /* synthetic */ -$$Lambda$n3uXeb1v-YRmq_BWTfosEqUUr9g() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((SessionCallback) obj).onProgressChanged(((Integer) obj2).intValue(), ((Float) obj3).floatValue());
    }
}
