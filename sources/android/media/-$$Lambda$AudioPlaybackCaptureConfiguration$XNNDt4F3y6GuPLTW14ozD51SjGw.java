package android.media;

import android.media.audiopolicy.AudioMixingRule.AudioMixMatchCriterion;
import java.util.function.ToIntFunction;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AudioPlaybackCaptureConfiguration$XNNDt4F3y6GuPLTW14ozD51SjGw implements ToIntFunction {
    public static final /* synthetic */ -$$Lambda$AudioPlaybackCaptureConfiguration$XNNDt4F3y6GuPLTW14ozD51SjGw INSTANCE = new -$$Lambda$AudioPlaybackCaptureConfiguration$XNNDt4F3y6GuPLTW14ozD51SjGw();

    private /* synthetic */ -$$Lambda$AudioPlaybackCaptureConfiguration$XNNDt4F3y6GuPLTW14ozD51SjGw() {
    }

    public final int applyAsInt(Object obj) {
        return ((AudioMixMatchCriterion) obj).getAudioAttributes().getUsage();
    }
}
