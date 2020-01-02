package android.view.contentcapture;

import android.os.IBinder;
import android.view.contentcapture.MainContentCaptureSession.AnonymousClass1;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MainContentCaptureSession$1$Xhq3WJibbalS1G_W3PRC2m7muhM implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ IBinder f$2;

    public /* synthetic */ -$$Lambda$MainContentCaptureSession$1$Xhq3WJibbalS1G_W3PRC2m7muhM(AnonymousClass1 anonymousClass1, int i, IBinder iBinder) {
        this.f$0 = anonymousClass1;
        this.f$1 = i;
        this.f$2 = iBinder;
    }

    public final void run() {
        this.f$0.lambda$send$1$MainContentCaptureSession$1(this.f$1, this.f$2);
    }
}
