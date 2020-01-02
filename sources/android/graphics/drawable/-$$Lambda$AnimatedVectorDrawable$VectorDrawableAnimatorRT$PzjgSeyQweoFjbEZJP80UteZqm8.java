package android.graphics.drawable;

import android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AnimatedVectorDrawable$VectorDrawableAnimatorRT$PzjgSeyQweoFjbEZJP80UteZqm8 implements Runnable {
    private final /* synthetic */ VectorDrawableAnimatorRT f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$AnimatedVectorDrawable$VectorDrawableAnimatorRT$PzjgSeyQweoFjbEZJP80UteZqm8(VectorDrawableAnimatorRT vectorDrawableAnimatorRT, int i) {
        this.f$0 = vectorDrawableAnimatorRT;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onAnimationEnd(this.f$1);
    }
}
