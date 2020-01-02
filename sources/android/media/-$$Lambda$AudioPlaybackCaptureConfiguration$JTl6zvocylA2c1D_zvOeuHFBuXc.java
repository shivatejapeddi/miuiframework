package android.media;

import android.media.audiopolicy.AudioMixingRule.AudioMixMatchCriterion;
import java.util.function.ToIntFunction;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AudioPlaybackCaptureConfiguration$JTl6zvocylA2c1D_zvOeuHFBuXc implements ToIntFunction {
    public static final /* synthetic */ -$$Lambda$AudioPlaybackCaptureConfiguration$JTl6zvocylA2c1D_zvOeuHFBuXc INSTANCE = new -$$Lambda$AudioPlaybackCaptureConfiguration$JTl6zvocylA2c1D_zvOeuHFBuXc();

    private /* synthetic */ -$$Lambda$AudioPlaybackCaptureConfiguration$JTl6zvocylA2c1D_zvOeuHFBuXc() {
    }

    public final int applyAsInt(Object obj) {
        return ((AudioMixMatchCriterion) obj).getAudioAttributes().getUsage();
    }
}
