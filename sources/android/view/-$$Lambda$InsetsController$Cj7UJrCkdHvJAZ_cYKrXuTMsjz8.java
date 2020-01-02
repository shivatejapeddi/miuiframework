package android.view;

import android.animation.TypeEvaluator;
import android.graphics.Insets;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$InsetsController$Cj7UJrCkdHvJAZ_cYKrXuTMsjz8 implements TypeEvaluator {
    public static final /* synthetic */ -$$Lambda$InsetsController$Cj7UJrCkdHvJAZ_cYKrXuTMsjz8 INSTANCE = new -$$Lambda$InsetsController$Cj7UJrCkdHvJAZ_cYKrXuTMsjz8();

    private /* synthetic */ -$$Lambda$InsetsController$Cj7UJrCkdHvJAZ_cYKrXuTMsjz8() {
    }

    public final Object evaluate(float f, Object obj, Object obj2) {
        return Insets.of(0, (int) (((float) ((Insets) obj).top) + (((float) (((Insets) obj2).top - ((Insets) obj).top)) * f)), 0, (int) (((float) ((Insets) obj).bottom) + (((float) (((Insets) obj2).bottom - ((Insets) obj).bottom)) * f)));
    }
}
