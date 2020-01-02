package android.os;

import android.os.IBinder.DeathRecipient;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$IncidentManager$yGukxCMuLDmoRlrh5jGUmq5BOTk implements DeathRecipient {
    private final /* synthetic */ IncidentManager f$0;

    public /* synthetic */ -$$Lambda$IncidentManager$yGukxCMuLDmoRlrh5jGUmq5BOTk(IncidentManager incidentManager) {
        this.f$0 = incidentManager;
    }

    public final void binderDied() {
        this.f$0.lambda$getIIncidentManagerLocked$0$IncidentManager();
    }
}
