package android.os;

import android.os.IBinder.DeathRecipient;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$IncidentManager$mfBTEJgu7VPkoPMTQdf1KC7oi5g implements DeathRecipient {
    private final /* synthetic */ IncidentManager f$0;

    public /* synthetic */ -$$Lambda$IncidentManager$mfBTEJgu7VPkoPMTQdf1KC7oi5g(IncidentManager incidentManager) {
        this.f$0 = incidentManager;
    }

    public final void binderDied() {
        this.f$0.lambda$getCompanionServiceLocked$1$IncidentManager();
    }
}
