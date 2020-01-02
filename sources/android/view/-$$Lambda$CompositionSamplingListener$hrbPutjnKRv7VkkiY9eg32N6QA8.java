package android.view;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$CompositionSamplingListener$hrbPutjnKRv7VkkiY9eg32N6QA8 implements Runnable {
    private final /* synthetic */ CompositionSamplingListener f$0;
    private final /* synthetic */ float f$1;

    public /* synthetic */ -$$Lambda$CompositionSamplingListener$hrbPutjnKRv7VkkiY9eg32N6QA8(CompositionSamplingListener compositionSamplingListener, float f) {
        this.f$0 = compositionSamplingListener;
        this.f$1 = f;
    }

    public final void run() {
        this.f$0.onSampleCollected(this.f$1);
    }
}
