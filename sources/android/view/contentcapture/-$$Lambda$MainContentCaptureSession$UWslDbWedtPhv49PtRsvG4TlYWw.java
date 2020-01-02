package android.view.contentcapture;

import android.os.IBinder.DeathRecipient;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MainContentCaptureSession$UWslDbWedtPhv49PtRsvG4TlYWw implements DeathRecipient {
    private final /* synthetic */ MainContentCaptureSession f$0;

    public /* synthetic */ -$$Lambda$MainContentCaptureSession$UWslDbWedtPhv49PtRsvG4TlYWw(MainContentCaptureSession mainContentCaptureSession) {
        this.f$0 = mainContentCaptureSession;
    }

    public final void binderDied() {
        this.f$0.lambda$onSessionStarted$1$MainContentCaptureSession();
    }
}
