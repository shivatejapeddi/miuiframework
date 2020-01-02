package android.media.audiopolicy;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.AudioSystem;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public class AudioMix {
    private static final int CALLBACK_FLAGS_ALL = 1;
    public static final int CALLBACK_FLAG_NOTIFY_ACTIVITY = 1;
    public static final int MIX_STATE_DISABLED = -1;
    public static final int MIX_STATE_IDLE = 0;
    public static final int MIX_STATE_MIXING = 1;
    public static final int MIX_TYPE_INVALID = -1;
    public static final int MIX_TYPE_PLAYERS = 0;
    public static final int MIX_TYPE_RECORDERS = 1;
    private static final int PRIVILEDGED_CAPTURE_MAX_BYTES_PER_SAMPLE = 2;
    private static final int PRIVILEDGED_CAPTURE_MAX_CHANNEL_NUMBER = 1;
    private static final int PRIVILEDGED_CAPTURE_MAX_SAMPLE_RATE = 16000;
    public static final int ROUTE_FLAG_LOOP_BACK = 2;
    public static final int ROUTE_FLAG_LOOP_BACK_RENDER = 3;
    public static final int ROUTE_FLAG_RENDER = 1;
    private static final int ROUTE_FLAG_SUPPORTED = 3;
    @UnsupportedAppUsage
    int mCallbackFlags;
    @UnsupportedAppUsage
    String mDeviceAddress;
    @UnsupportedAppUsage
    final int mDeviceSystemType;
    @UnsupportedAppUsage
    private AudioFormat mFormat;
    int mMixState;
    @UnsupportedAppUsage
    private int mMixType;
    @UnsupportedAppUsage
    private int mRouteFlags;
    @UnsupportedAppUsage
    private AudioMixingRule mRule;

    public static class Builder {
        private int mCallbackFlags = 0;
        private String mDeviceAddress = null;
        private int mDeviceSystemType = 0;
        private AudioFormat mFormat = null;
        private int mRouteFlags = 0;
        private AudioMixingRule mRule = null;

        Builder() {
        }

        public Builder(AudioMixingRule rule) throws IllegalArgumentException {
            if (rule != null) {
                this.mRule = rule;
                return;
            }
            throw new IllegalArgumentException("Illegal null AudioMixingRule argument");
        }

        /* Access modifiers changed, original: 0000 */
        public Builder setMixingRule(AudioMixingRule rule) throws IllegalArgumentException {
            if (rule != null) {
                this.mRule = rule;
                return this;
            }
            throw new IllegalArgumentException("Illegal null AudioMixingRule argument");
        }

        /* Access modifiers changed, original: 0000 */
        public Builder setCallbackFlags(int flags) throws IllegalArgumentException {
            if (flags == 0 || (flags & 1) != 0) {
                this.mCallbackFlags = flags;
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Illegal callback flags 0x");
            stringBuilder.append(Integer.toHexString(flags).toUpperCase());
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        /* Access modifiers changed, original: 0000 */
        public Builder setDevice(int deviceType, String address) {
            this.mDeviceSystemType = deviceType;
            this.mDeviceAddress = address;
            return this;
        }

        public Builder setFormat(AudioFormat format) throws IllegalArgumentException {
            if (format != null) {
                this.mFormat = format;
                return this;
            }
            throw new IllegalArgumentException("Illegal null AudioFormat argument");
        }

        public Builder setRouteFlags(int routeFlags) throws IllegalArgumentException {
            if (routeFlags != 0) {
                String str = "when configuring an AudioMix";
                StringBuilder stringBuilder;
                if ((routeFlags & 3) == 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid route flags 0x");
                    stringBuilder.append(Integer.toHexString(routeFlags));
                    stringBuilder.append(str);
                    throw new IllegalArgumentException(stringBuilder.toString());
                } else if ((routeFlags & -4) == 0) {
                    this.mRouteFlags = routeFlags;
                    return this;
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown route flags 0x");
                    stringBuilder.append(Integer.toHexString(routeFlags));
                    stringBuilder.append(str);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            throw new IllegalArgumentException("Illegal empty route flags");
        }

        public Builder setDevice(AudioDeviceInfo device) throws IllegalArgumentException {
            if (device == null) {
                throw new IllegalArgumentException("Illegal null AudioDeviceInfo argument");
            } else if (device.isSink()) {
                this.mDeviceSystemType = AudioDeviceInfo.convertDeviceTypeToInternalDevice(device.getType());
                this.mDeviceAddress = device.getAddress();
                return this;
            } else {
                throw new IllegalArgumentException("Unsupported device type on mix, not a sink");
            }
        }

        public AudioMix build() throws IllegalArgumentException {
            if (this.mRule != null) {
                int rate;
                if (this.mRouteFlags == 0) {
                    this.mRouteFlags = 2;
                }
                if (this.mFormat == null) {
                    rate = AudioSystem.getPrimaryOutputSamplingRate();
                    if (rate <= 0) {
                        rate = 44100;
                    }
                    this.mFormat = new android.media.AudioFormat.Builder().setSampleRate(rate).build();
                }
                rate = this.mDeviceSystemType;
                if (rate == 0 || rate == 32768 || rate == AudioSystem.DEVICE_IN_REMOTE_SUBMIX) {
                    rate = this.mRouteFlags;
                    if ((rate & 3) == 1) {
                        throw new IllegalArgumentException("Can't have flag ROUTE_FLAG_RENDER without an audio device");
                    } else if ((rate & 2) == 2) {
                        if (this.mRule.getTargetMixType() == 0) {
                            this.mDeviceSystemType = 32768;
                        } else if (this.mRule.getTargetMixType() == 1) {
                            this.mDeviceSystemType = AudioSystem.DEVICE_IN_REMOTE_SUBMIX;
                        } else {
                            throw new IllegalArgumentException("Unknown mixing rule type");
                        }
                    }
                } else if ((this.mRouteFlags & 1) == 0) {
                    throw new IllegalArgumentException("Can't have audio device without flag ROUTE_FLAG_RENDER");
                } else if (this.mRule.getTargetMixType() != 0) {
                    throw new IllegalArgumentException("Unsupported device on non-playback mix");
                }
                if (this.mRule.allowPrivilegedPlaybackCapture()) {
                    String error = AudioMix.canBeUsedForPrivilegedCapture(this.mFormat);
                    if (error != null) {
                        throw new IllegalArgumentException(error);
                    }
                }
                return new AudioMix(this.mRule, this.mFormat, this.mRouteFlags, this.mCallbackFlags, this.mDeviceSystemType, this.mDeviceAddress);
            }
            throw new IllegalArgumentException("Illegal null AudioMixingRule");
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RouteFlags {
    }

    private AudioMix(AudioMixingRule rule, AudioFormat format, int routeFlags, int callbackFlags, int deviceType, String deviceAddress) {
        this.mMixType = -1;
        this.mMixState = -1;
        this.mRule = rule;
        this.mFormat = format;
        this.mRouteFlags = routeFlags;
        this.mMixType = rule.getTargetMixType();
        this.mCallbackFlags = callbackFlags;
        this.mDeviceSystemType = deviceType;
        this.mDeviceAddress = deviceAddress == null ? new String("") : deviceAddress;
    }

    public int getMixState() {
        return this.mMixState;
    }

    public int getRouteFlags() {
        return this.mRouteFlags;
    }

    public AudioFormat getFormat() {
        return this.mFormat;
    }

    public AudioMixingRule getRule() {
        return this.mRule;
    }

    public int getMixType() {
        return this.mMixType;
    }

    /* Access modifiers changed, original: 0000 */
    public void setRegistration(String regId) {
        this.mDeviceAddress = regId;
    }

    public String getRegistration() {
        return this.mDeviceAddress;
    }

    public boolean isAffectingUsage(int usage) {
        return this.mRule.isAffectingUsage(usage);
    }

    public boolean isRoutedToDevice(int deviceType, String deviceAddress) {
        return (this.mRouteFlags & 1) == 1 && deviceType == this.mDeviceSystemType && deviceAddress.equals(this.mDeviceAddress);
    }

    public static String canBeUsedForPrivilegedCapture(AudioFormat format) {
        int sampleRate = format.getSampleRate();
        String str = " can not be over ";
        if (sampleRate > PRIVILEDGED_CAPTURE_MAX_SAMPLE_RATE || sampleRate <= 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Privileged audio capture sample rate ");
            stringBuilder.append(sampleRate);
            stringBuilder.append(str);
            stringBuilder.append(PRIVILEDGED_CAPTURE_MAX_SAMPLE_RATE);
            stringBuilder.append("kHz");
            return stringBuilder.toString();
        }
        int channelCount = format.getChannelCount();
        StringBuilder stringBuilder2;
        if (channelCount > 1 || channelCount <= 0) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Privileged audio capture channel count ");
            stringBuilder2.append(channelCount);
            stringBuilder2.append(str);
            stringBuilder2.append(1);
            return stringBuilder2.toString();
        }
        int encoding = format.getEncoding();
        String str2 = "Privileged audio capture encoding ";
        if (!AudioFormat.isPublicEncoding(encoding) || !AudioFormat.isEncodingLinearPcm(encoding)) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str2);
            stringBuilder3.append(encoding);
            stringBuilder3.append("is not linear");
            return stringBuilder3.toString();
        } else if (AudioFormat.getBytesPerSample(encoding) <= 2) {
            return null;
        } else {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(encoding);
            stringBuilder2.append(str);
            stringBuilder2.append(2);
            stringBuilder2.append(" bytes per sample");
            return stringBuilder2.toString();
        }
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AudioMix that = (AudioMix) o;
        if (!(this.mRouteFlags == that.mRouteFlags && this.mRule == that.mRule && this.mMixType == that.mMixType && this.mFormat == that.mFormat)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mRouteFlags), this.mRule, Integer.valueOf(this.mMixType), this.mFormat});
    }
}
