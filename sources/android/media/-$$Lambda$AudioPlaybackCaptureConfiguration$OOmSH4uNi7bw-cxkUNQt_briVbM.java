package android.media;

import android.media.audiopolicy.AudioMixingRule.AudioMixMatchCriterion;
import java.util.function.ToIntFunction;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AudioPlaybackCaptureConfiguration$OOmSH4uNi7bw-cxkUNQt_briVbM implements ToIntFunction {
    public static final /* synthetic */ -$$Lambda$AudioPlaybackCaptureConfiguration$OOmSH4uNi7bw-cxkUNQt_briVbM INSTANCE = new -$$Lambda$AudioPlaybackCaptureConfiguration$OOmSH4uNi7bw-cxkUNQt_briVbM();

    private /* synthetic */ -$$Lambda$AudioPlaybackCaptureConfiguration$OOmSH4uNi7bw-cxkUNQt_briVbM() {
    }

    public final int applyAsInt(Object obj) {
        return ((AudioMixMatchCriterion) obj).getIntProp();
    }
}
