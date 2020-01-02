package android.service.contentcapture;

import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ContentCaptureService$1$NhSHlL57JqxWNJ8QcsuGxEhxv1Y implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$ContentCaptureService$1$NhSHlL57JqxWNJ8QcsuGxEhxv1Y INSTANCE = new -$$Lambda$ContentCaptureService$1$NhSHlL57JqxWNJ8QcsuGxEhxv1Y();

    private /* synthetic */ -$$Lambda$ContentCaptureService$1$NhSHlL57JqxWNJ8QcsuGxEhxv1Y() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((ContentCaptureService) ((ContentCaptureService) obj)).handleOnActivitySnapshot(((Integer) obj2).intValue(), (SnapshotData) obj3);
    }
}
