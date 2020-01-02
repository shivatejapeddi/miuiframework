package com.android.internal.telephony;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SmsApplication$gDx3W-UsTeTFaBSPU-Y_LFPZ9dE implements Consumer {
    private final /* synthetic */ CompletableFuture f$0;

    public /* synthetic */ -$$Lambda$SmsApplication$gDx3W-UsTeTFaBSPU-Y_LFPZ9dE(CompletableFuture completableFuture) {
        this.f$0 = completableFuture;
    }

    public final void accept(Object obj) {
        SmsApplication.lambda$setDefaultApplicationInternal$0(this.f$0, (Boolean) obj);
    }
}
