package android.view.contentcapture;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MainContentCaptureSession$49zT7C2BXrEdkyggyGk1Qs4d46k implements Runnable {
    private final /* synthetic */ MainContentCaptureSession f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$MainContentCaptureSession$49zT7C2BXrEdkyggyGk1Qs4d46k(MainContentCaptureSession mainContentCaptureSession, int i) {
        this.f$0 = mainContentCaptureSession;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$scheduleFlush$2$MainContentCaptureSession(this.f$1);
    }
}
