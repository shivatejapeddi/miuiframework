package android.widget;

import android.util.SparseArray;
import java.util.function.IntFunction;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI implements IntFunction {
    private final /* synthetic */ SparseArray f$0;

    public /* synthetic */ -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(SparseArray sparseArray) {
        this.f$0 = sparseArray;
    }

    public final Object apply(int i) {
        return (String) this.f$0.get(i);
    }
}
