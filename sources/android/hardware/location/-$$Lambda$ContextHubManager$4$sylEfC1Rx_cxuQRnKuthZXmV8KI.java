package android.hardware.location;

import android.hardware.location.ContextHubManager.AnonymousClass4;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ContextHubManager$4$sylEfC1Rx_cxuQRnKuthZXmV8KI implements Runnable {
    private final /* synthetic */ AnonymousClass4 f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ ContextHubMessage f$3;

    public /* synthetic */ -$$Lambda$ContextHubManager$4$sylEfC1Rx_cxuQRnKuthZXmV8KI(AnonymousClass4 anonymousClass4, int i, int i2, ContextHubMessage contextHubMessage) {
        this.f$0 = anonymousClass4;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = contextHubMessage;
    }

    public final void run() {
        this.f$0.lambda$onMessageReceipt$0$ContextHubManager$4(this.f$1, this.f$2, this.f$3);
    }
}
