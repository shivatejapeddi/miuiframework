package android.app.prediction;

import android.content.pm.ParceledListSlice;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AppPredictor$CallbackWrapper$gCs3O3sYRlsXAOdelds31867YXo implements Runnable {
    private final /* synthetic */ CallbackWrapper f$0;
    private final /* synthetic */ ParceledListSlice f$1;

    public /* synthetic */ -$$Lambda$AppPredictor$CallbackWrapper$gCs3O3sYRlsXAOdelds31867YXo(CallbackWrapper callbackWrapper, ParceledListSlice parceledListSlice) {
        this.f$0 = callbackWrapper;
        this.f$1 = parceledListSlice;
    }

    public final void run() {
        this.f$0.lambda$onResult$0$AppPredictor$CallbackWrapper(this.f$1);
    }
}
