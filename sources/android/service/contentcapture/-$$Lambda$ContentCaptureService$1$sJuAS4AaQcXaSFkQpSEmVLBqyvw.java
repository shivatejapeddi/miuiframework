package android.service.contentcapture;

import android.view.contentcapture.DataRemovalRequest;
import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ContentCaptureService$1$sJuAS4AaQcXaSFkQpSEmVLBqyvw implements BiConsumer {
    public static final /* synthetic */ -$$Lambda$ContentCaptureService$1$sJuAS4AaQcXaSFkQpSEmVLBqyvw INSTANCE = new -$$Lambda$ContentCaptureService$1$sJuAS4AaQcXaSFkQpSEmVLBqyvw();

    private /* synthetic */ -$$Lambda$ContentCaptureService$1$sJuAS4AaQcXaSFkQpSEmVLBqyvw() {
    }

    public final void accept(Object obj, Object obj2) {
        ((ContentCaptureService) ((ContentCaptureService) obj)).handleOnDataRemovalRequest((DataRemovalRequest) obj2);
    }
}
