package android.media;

import java.util.ArrayList;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AudioRecordingMonitorImpl$2$cn04v8rie0OYr-_fiLO_SMYka7I implements Runnable {
    private final /* synthetic */ AudioRecordingCallbackInfo f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ -$$Lambda$AudioRecordingMonitorImpl$2$cn04v8rie0OYr-_fiLO_SMYka7I(AudioRecordingCallbackInfo audioRecordingCallbackInfo, ArrayList arrayList) {
        this.f$0 = audioRecordingCallbackInfo;
        this.f$1 = arrayList;
    }

    public final void run() {
        this.f$0.mCb.onRecordingConfigChanged(this.f$1);
    }
}
