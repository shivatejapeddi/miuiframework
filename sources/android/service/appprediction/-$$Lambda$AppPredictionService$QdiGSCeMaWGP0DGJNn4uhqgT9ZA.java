package android.service.appprediction;

import java.util.ArrayList;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AppPredictionService$QdiGSCeMaWGP0DGJNn4uhqgT9ZA implements Runnable {
    private final /* synthetic */ AppPredictionService f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ CallbackWrapper f$2;

    public /* synthetic */ -$$Lambda$AppPredictionService$QdiGSCeMaWGP0DGJNn4uhqgT9ZA(AppPredictionService appPredictionService, ArrayList arrayList, CallbackWrapper callbackWrapper) {
        this.f$0 = appPredictionService;
        this.f$1 = arrayList;
        this.f$2 = callbackWrapper;
    }

    public final void run() {
        this.f$0.lambda$doRegisterPredictionUpdates$0$AppPredictionService(this.f$1, this.f$2);
    }
}
