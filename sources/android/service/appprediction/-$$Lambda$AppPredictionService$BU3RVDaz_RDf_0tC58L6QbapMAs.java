package android.service.appprediction;

import java.util.ArrayList;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AppPredictionService$BU3RVDaz_RDf_0tC58L6QbapMAs implements Consumer {
    private final /* synthetic */ AppPredictionService f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ -$$Lambda$AppPredictionService$BU3RVDaz_RDf_0tC58L6QbapMAs(AppPredictionService appPredictionService, ArrayList arrayList) {
        this.f$0 = appPredictionService;
        this.f$1 = arrayList;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$doRegisterPredictionUpdates$1$AppPredictionService(this.f$1, (CallbackWrapper) obj);
    }
}
