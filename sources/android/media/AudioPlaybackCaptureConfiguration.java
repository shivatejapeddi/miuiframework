package android.media;

import android.media.audiopolicy.AudioMix;
import android.media.audiopolicy.AudioMixingRule;
import android.media.audiopolicy.AudioMixingRule.AudioMixMatchCriterion;
import android.media.projection.MediaProjection;
import android.os.RemoteException;
import com.android.internal.util.Preconditions;
import java.util.function.ToIntFunction;

public final class AudioPlaybackCaptureConfiguration {
    private final AudioMixingRule mAudioMixingRule;
    private final MediaProjection mProjection;

    public static final class Builder {
        private static final String ERROR_MESSAGE_MISMATCHED_RULES = "Inclusive and exclusive usage rules cannot be combined";
        private static final String ERROR_MESSAGE_NON_AUDIO_PROJECTION = "MediaProjection can not project audio";
        private static final String ERROR_MESSAGE_START_ACTIVITY_FAILED = "startActivityForResult failed";
        private static final int MATCH_TYPE_EXCLUSIVE = 2;
        private static final int MATCH_TYPE_INCLUSIVE = 1;
        private static final int MATCH_TYPE_UNSPECIFIED = 0;
        private final android.media.audiopolicy.AudioMixingRule.Builder mAudioMixingRuleBuilder;
        private final MediaProjection mProjection;
        private int mUidMatchType = 0;
        private int mUsageMatchType = 0;

        public Builder(MediaProjection projection) {
            Preconditions.checkNotNull(projection);
            try {
                Preconditions.checkArgument(projection.getProjection().canProjectAudio(), ERROR_MESSAGE_NON_AUDIO_PROJECTION);
                this.mProjection = projection;
                this.mAudioMixingRuleBuilder = new android.media.audiopolicy.AudioMixingRule.Builder();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public Builder addMatchingUsage(int usage) {
            Preconditions.checkState(this.mUsageMatchType != 2, ERROR_MESSAGE_MISMATCHED_RULES);
            this.mAudioMixingRuleBuilder.addRule(new android.media.AudioAttributes.Builder().setUsage(usage).build(), 1);
            this.mUsageMatchType = 1;
            return this;
        }

        public Builder addMatchingUid(int uid) {
            Preconditions.checkState(this.mUidMatchType != 2, ERROR_MESSAGE_MISMATCHED_RULES);
            this.mAudioMixingRuleBuilder.addMixRule(4, Integer.valueOf(uid));
            this.mUidMatchType = 1;
            return this;
        }

        public Builder excludeUsage(int usage) {
            Preconditions.checkState(this.mUsageMatchType != 1, ERROR_MESSAGE_MISMATCHED_RULES);
            this.mAudioMixingRuleBuilder.excludeRule(new android.media.AudioAttributes.Builder().setUsage(usage).build(), 1);
            this.mUsageMatchType = 2;
            return this;
        }

        public Builder excludeUid(int uid) {
            boolean z = true;
            if (this.mUidMatchType == 1) {
                z = false;
            }
            Preconditions.checkState(z, ERROR_MESSAGE_MISMATCHED_RULES);
            this.mAudioMixingRuleBuilder.excludeMixRule(4, Integer.valueOf(uid));
            this.mUidMatchType = 2;
            return this;
        }

        public AudioPlaybackCaptureConfiguration build() {
            return new AudioPlaybackCaptureConfiguration(this.mAudioMixingRuleBuilder.build(), this.mProjection);
        }
    }

    private AudioPlaybackCaptureConfiguration(AudioMixingRule audioMixingRule, MediaProjection projection) {
        this.mAudioMixingRule = audioMixingRule;
        this.mProjection = projection;
    }

    public MediaProjection getMediaProjection() {
        return this.mProjection;
    }

    public int[] getMatchingUsages() {
        return getIntPredicates(1, -$$Lambda$AudioPlaybackCaptureConfiguration$JTl6zvocylA2c1D_zvOeuHFBuXc.INSTANCE);
    }

    public int[] getMatchingUids() {
        return getIntPredicates(4, -$$Lambda$AudioPlaybackCaptureConfiguration$lExv8IaPEEDrexk0ZpgJOYug6js.INSTANCE);
    }

    public int[] getExcludeUsages() {
        return getIntPredicates(32769, -$$Lambda$AudioPlaybackCaptureConfiguration$XNNDt4F3y6GuPLTW14ozD51SjGw.INSTANCE);
    }

    public int[] getExcludeUids() {
        return getIntPredicates(32772, -$$Lambda$AudioPlaybackCaptureConfiguration$OOmSH4uNi7bw-cxkUNQt_briVbM.INSTANCE);
    }

    private int[] getIntPredicates(int rule, ToIntFunction<AudioMixMatchCriterion> getPredicate) {
        return this.mAudioMixingRule.getCriteria().stream().filter(new -$$Lambda$AudioPlaybackCaptureConfiguration$CbJtSEmgD3swIYuOWlTCDMPxK1s(rule)).mapToInt(getPredicate).toArray();
    }

    static /* synthetic */ boolean lambda$getIntPredicates$4(int rule, AudioMixMatchCriterion criterion) {
        return criterion.getRule() == rule;
    }

    /* Access modifiers changed, original: 0000 */
    public AudioMix createAudioMix(AudioFormat audioFormat) {
        return new android.media.audiopolicy.AudioMix.Builder(this.mAudioMixingRule).setFormat(audioFormat).setRouteFlags(3).build();
    }
}
