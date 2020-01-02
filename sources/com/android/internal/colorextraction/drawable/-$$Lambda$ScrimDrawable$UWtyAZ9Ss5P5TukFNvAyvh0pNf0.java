package com.android.internal.colorextraction.drawable;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ScrimDrawable$UWtyAZ9Ss5P5TukFNvAyvh0pNf0 implements AnimatorUpdateListener {
    private final /* synthetic */ ScrimDrawable f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$ScrimDrawable$UWtyAZ9Ss5P5TukFNvAyvh0pNf0(ScrimDrawable scrimDrawable, int i, int i2) {
        this.f$0 = scrimDrawable;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setColor$0$ScrimDrawable(this.f$1, this.f$2, valueAnimator);
    }
}
