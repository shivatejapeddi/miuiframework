package android.os;

import android.os.PowerManager.OnThermalStatusChangedListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PowerManager$1$-RL9hKNKSaGL1mmR-EjQ-Cm9KuA implements Runnable {
    private final /* synthetic */ OnThermalStatusChangedListener f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$PowerManager$1$-RL9hKNKSaGL1mmR-EjQ-Cm9KuA(OnThermalStatusChangedListener onThermalStatusChangedListener, int i) {
        this.f$0 = onThermalStatusChangedListener;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onThermalStatusChanged(this.f$1);
    }
}
