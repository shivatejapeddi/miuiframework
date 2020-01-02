package android.service.contentcapture;

import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ContentCaptureService$1$V1mxGgTDjVVHroIjJrHvYfUHCKE implements BiConsumer {
    public static final /* synthetic */ -$$Lambda$ContentCaptureService$1$V1mxGgTDjVVHroIjJrHvYfUHCKE INSTANCE = new -$$Lambda$ContentCaptureService$1$V1mxGgTDjVVHroIjJrHvYfUHCKE();

    private /* synthetic */ -$$Lambda$ContentCaptureService$1$V1mxGgTDjVVHroIjJrHvYfUHCKE() {
    }

    public final void accept(Object obj, Object obj2) {
        ((ContentCaptureService) ((ContentCaptureService) obj)).handleOnActivityEvent((ActivityEvent) obj2);
    }
}
