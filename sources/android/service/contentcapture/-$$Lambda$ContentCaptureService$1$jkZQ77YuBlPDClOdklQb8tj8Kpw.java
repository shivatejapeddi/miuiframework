package android.service.contentcapture;

import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ContentCaptureService$1$jkZQ77YuBlPDClOdklQb8tj8Kpw implements BiConsumer {
    public static final /* synthetic */ -$$Lambda$ContentCaptureService$1$jkZQ77YuBlPDClOdklQb8tj8Kpw INSTANCE = new -$$Lambda$ContentCaptureService$1$jkZQ77YuBlPDClOdklQb8tj8Kpw();

    private /* synthetic */ -$$Lambda$ContentCaptureService$1$jkZQ77YuBlPDClOdklQb8tj8Kpw() {
    }

    public final void accept(Object obj, Object obj2) {
        ((ContentCaptureService) ((ContentCaptureService) obj)).handleFinishSession(((Integer) obj2).intValue());
    }
}
