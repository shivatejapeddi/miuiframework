package android.media;

import android.media.audiopolicy.AudioMixingRule.AudioMixMatchCriterion;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AudioPlaybackCaptureConfiguration$CbJtSEmgD3swIYuOWlTCDMPxK1s implements Predicate {
    private final /* synthetic */ int f$0;

    public /* synthetic */ -$$Lambda$AudioPlaybackCaptureConfiguration$CbJtSEmgD3swIYuOWlTCDMPxK1s(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return AudioPlaybackCaptureConfiguration.lambda$getIntPredicates$4(this.f$0, (AudioMixMatchCriterion) obj);
    }
}
