package android.service.voice;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.RemoteCallback.OnResultListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$VoiceInteractionSession$KRmvXWbKzOj6uOiuAkDjhkUvQiw implements OnResultListener {
    private final /* synthetic */ CancellationSignal f$0;

    public /* synthetic */ -$$Lambda$VoiceInteractionSession$KRmvXWbKzOj6uOiuAkDjhkUvQiw(CancellationSignal cancellationSignal) {
        this.f$0 = cancellationSignal;
    }

    public final void onResult(Bundle bundle) {
        VoiceInteractionSession.lambda$requestDirectActions$0(this.f$0, bundle);
    }
}
