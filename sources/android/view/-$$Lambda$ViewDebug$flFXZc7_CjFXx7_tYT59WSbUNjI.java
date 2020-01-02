package android.view;

import android.graphics.RecordingCanvas;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ViewDebug$flFXZc7_CjFXx7_tYT59WSbUNjI implements ViewOperation {
    private final /* synthetic */ View f$0;
    private final /* synthetic */ RecordingCanvas f$1;

    public /* synthetic */ -$$Lambda$ViewDebug$flFXZc7_CjFXx7_tYT59WSbUNjI(View view, RecordingCanvas recordingCanvas) {
        this.f$0 = view;
        this.f$1 = recordingCanvas;
    }

    public final void run() {
        this.f$0.draw(this.f$1);
    }
}
