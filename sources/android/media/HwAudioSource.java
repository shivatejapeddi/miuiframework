package android.media;

import android.annotation.SystemApi;
import android.media.VolumeShaper.Configuration;
import android.media.VolumeShaper.Operation;
import android.media.VolumeShaper.State;
import com.android.internal.util.Preconditions;

@SystemApi
public class HwAudioSource extends PlayerBase {
    private final AudioAttributes mAudioAttributes;
    private final AudioDeviceInfo mAudioDeviceInfo;
    private int mNativeHandle;

    public static final class Builder {
        private AudioAttributes mAudioAttributes;
        private AudioDeviceInfo mAudioDeviceInfo;

        public Builder setAudioAttributes(AudioAttributes attributes) {
            Preconditions.checkNotNull(attributes);
            this.mAudioAttributes = attributes;
            return this;
        }

        public Builder setAudioDeviceInfo(AudioDeviceInfo info) {
            Preconditions.checkNotNull(info);
            Preconditions.checkArgument(info.isSource());
            this.mAudioDeviceInfo = info;
            return this;
        }

        public HwAudioSource build() {
            Preconditions.checkNotNull(this.mAudioDeviceInfo);
            if (this.mAudioAttributes == null) {
                this.mAudioAttributes = new android.media.AudioAttributes.Builder().setUsage(1).build();
            }
            return new HwAudioSource(this.mAudioDeviceInfo, this.mAudioAttributes);
        }
    }

    private HwAudioSource(AudioDeviceInfo device, AudioAttributes attributes) {
        super(attributes, 14);
        Preconditions.checkNotNull(device);
        Preconditions.checkNotNull(attributes);
        Preconditions.checkArgument(device.isSource(), "Requires a source device");
        this.mAudioDeviceInfo = device;
        this.mAudioAttributes = attributes;
        baseRegisterPlayer();
    }

    /* Access modifiers changed, original: 0000 */
    public void playerSetVolume(boolean muting, float leftVolume, float rightVolume) {
    }

    /* Access modifiers changed, original: 0000 */
    public int playerApplyVolumeShaper(Configuration configuration, Operation operation) {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public State playerGetVolumeShaperState(int id) {
        return new State(1.0f, 1.0f);
    }

    /* Access modifiers changed, original: 0000 */
    public int playerSetAuxEffectSendLevel(boolean muting, float level) {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public void playerStart() {
        start();
    }

    /* Access modifiers changed, original: 0000 */
    public void playerPause() {
        stop();
    }

    /* Access modifiers changed, original: 0000 */
    public void playerStop() {
        stop();
    }

    public void start() {
        Preconditions.checkState(isPlaying() ^ 1, "HwAudioSource is currently playing");
        baseStart();
        this.mNativeHandle = AudioSystem.startAudioSource(this.mAudioDeviceInfo.getPort().activeConfig(), this.mAudioAttributes);
    }

    public boolean isPlaying() {
        return this.mNativeHandle != 0;
    }

    public void stop() {
        baseStop();
        int i = this.mNativeHandle;
        if (i > 0) {
            AudioSystem.stopAudioSource(i);
            this.mNativeHandle = 0;
        }
    }
}
