package android.media;

import android.media.audiopolicy.AudioMixingRule.AudioMixMatchCriterion;
import java.util.function.ToIntFunction;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AudioPlaybackCaptureConfiguration$lExv8IaPEEDrexk0ZpgJOYug6js implements ToIntFunction {
    public static final /* synthetic */ -$$Lambda$AudioPlaybackCaptureConfiguration$lExv8IaPEEDrexk0ZpgJOYug6js INSTANCE = new -$$Lambda$AudioPlaybackCaptureConfiguration$lExv8IaPEEDrexk0ZpgJOYug6js();

    private /* synthetic */ -$$Lambda$AudioPlaybackCaptureConfiguration$lExv8IaPEEDrexk0ZpgJOYug6js() {
    }

    public final int applyAsInt(Object obj) {
        return ((AudioMixMatchCriterion) obj).getIntProp();
    }
}
