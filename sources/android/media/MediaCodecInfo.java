package android.media;

import android.annotation.UnsupportedAppUsage;
import android.hardware.camera2.legacy.LegacyCameraDevice;
import android.os.AnrMonitor;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Pair;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import android.view.SurfaceControl;
import android.view.autofill.AutofillManager;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.miui.whetstone.Event.EventLogTags;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public final class MediaCodecInfo {
    private static final Range<Integer> BITRATE_RANGE;
    private static final int DEFAULT_MAX_SUPPORTED_INSTANCES = 32;
    private static final int ERROR_NONE_SUPPORTED = 4;
    private static final int ERROR_UNRECOGNIZED = 1;
    private static final int ERROR_UNSUPPORTED = 2;
    private static final int FLAG_IS_ENCODER = 1;
    private static final int FLAG_IS_HARDWARE_ACCELERATED = 8;
    private static final int FLAG_IS_SOFTWARE_ONLY = 4;
    private static final int FLAG_IS_VENDOR = 2;
    private static final Range<Integer> FRAME_RATE_RANGE;
    private static final int MAX_SUPPORTED_INSTANCES_LIMIT = 256;
    private static final Range<Integer> POSITIVE_INTEGERS;
    private static final Range<Long> POSITIVE_LONGS = Range.create(Long.valueOf(1), Long.valueOf(Long.MAX_VALUE));
    private static final Range<Rational> POSITIVE_RATIONALS = Range.create(new Rational(1, Integer.MAX_VALUE), new Rational(Integer.MAX_VALUE, 1));
    private static final Range<Integer> SIZE_RANGE;
    private static final String TAG = "MediaCodecInfo";
    private String mCanonicalName;
    private Map<String, CodecCapabilities> mCaps = new HashMap();
    private int mFlags;
    private String mName;

    public static final class AudioCapabilities {
        private static final int MAX_INPUT_CHANNEL_COUNT = 30;
        private static final String TAG = "AudioCapabilities";
        private Range<Integer> mBitrateRange;
        private int mMaxInputChannelCount;
        private CodecCapabilities mParent;
        private Range<Integer>[] mSampleRateRanges;
        private int[] mSampleRates;

        public Range<Integer> getBitrateRange() {
            return this.mBitrateRange;
        }

        public int[] getSupportedSampleRates() {
            int[] iArr = this.mSampleRates;
            return iArr != null ? Arrays.copyOf(iArr, iArr.length) : null;
        }

        public Range<Integer>[] getSupportedSampleRateRanges() {
            Range[] rangeArr = this.mSampleRateRanges;
            return (Range[]) Arrays.copyOf(rangeArr, rangeArr.length);
        }

        public int getMaxInputChannelCount() {
            return this.mMaxInputChannelCount;
        }

        private AudioCapabilities() {
        }

        public static AudioCapabilities create(MediaFormat info, CodecCapabilities parent) {
            AudioCapabilities caps = new AudioCapabilities();
            caps.init(info, parent);
            return caps;
        }

        private void init(MediaFormat info, CodecCapabilities parent) {
            this.mParent = parent;
            initWithPlatformLimits();
            applyLevelLimits();
            parseFromInfo(info);
        }

        private void initWithPlatformLimits() {
            this.mBitrateRange = Range.create(Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE));
            this.mMaxInputChannelCount = 30;
            int minSampleRate = SystemProperties.getInt("ro.mediacodec.min_sample_rate", 7350);
            int maxSampleRate = SystemProperties.getInt("ro.mediacodec.max_sample_rate", AudioFormat.SAMPLE_RATE_HZ_MAX);
            this.mSampleRateRanges = new Range[]{Range.create(Integer.valueOf(minSampleRate), Integer.valueOf(maxSampleRate))};
            this.mSampleRates = null;
        }

        private boolean supports(Integer sampleRate, Integer inputChannels) {
            if (inputChannels == null || (inputChannels.intValue() >= 1 && inputChannels.intValue() <= this.mMaxInputChannelCount)) {
                return sampleRate == null || Utils.binarySearchDistinctRanges(this.mSampleRateRanges, sampleRate) >= 0;
            } else {
                return false;
            }
        }

        public boolean isSampleRateSupported(int sampleRate) {
            return supports(Integer.valueOf(sampleRate), null);
        }

        private void limitSampleRates(int[] rates) {
            Arrays.sort(rates);
            ArrayList<Range<Integer>> ranges = new ArrayList();
            for (int rate : rates) {
                if (supports(Integer.valueOf(rate), null)) {
                    ranges.add(Range.create(Integer.valueOf(rate), Integer.valueOf(rate)));
                }
            }
            this.mSampleRateRanges = (Range[]) ranges.toArray(new Range[ranges.size()]);
            createDiscreteSampleRates();
        }

        private void createDiscreteSampleRates() {
            this.mSampleRates = new int[this.mSampleRateRanges.length];
            int i = 0;
            while (true) {
                Range[] rangeArr = this.mSampleRateRanges;
                if (i < rangeArr.length) {
                    this.mSampleRates[i] = ((Integer) rangeArr[i].getLower()).intValue();
                    i++;
                } else {
                    return;
                }
            }
        }

        private void limitSampleRates(Range<Integer>[] rateRanges) {
            Utils.sortDistinctRanges(rateRanges);
            this.mSampleRateRanges = Utils.intersectSortedDistinctRanges(this.mSampleRateRanges, rateRanges);
            Range[] rangeArr = this.mSampleRateRanges;
            int length = rangeArr.length;
            int i = 0;
            while (i < length) {
                Range<Integer> range = rangeArr[i];
                if (((Integer) range.getLower()).equals(range.getUpper())) {
                    i++;
                } else {
                    this.mSampleRates = null;
                    return;
                }
            }
            createDiscreteSampleRates();
        }

        private void applyLevelLimits() {
            int[] sampleRates = null;
            Range<Integer> sampleRateRange = null;
            Range<Integer> bitRates = null;
            int maxChannels = 30;
            String mime = this.mParent.getMimeType();
            boolean equalsIgnoreCase = mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_MPEG);
            Integer valueOf = Integer.valueOf(AnrMonitor.INPUT_DISPATCHING_TIMEOUT);
            Integer valueOf2 = Integer.valueOf(1);
            if (equalsIgnoreCase) {
                sampleRates = new int[]{AnrMonitor.INPUT_DISPATCHING_TIMEOUT, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000};
                bitRates = Range.create(valueOf, Integer.valueOf(320000));
                maxChannels = 2;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AMR_NB)) {
                sampleRates = new int[]{AnrMonitor.INPUT_DISPATCHING_TIMEOUT};
                bitRates = Range.create(Integer.valueOf(4750), Integer.valueOf(12200));
                maxChannels = 1;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AMR_WB)) {
                sampleRates = new int[]{16000};
                bitRates = Range.create(Integer.valueOf(6600), Integer.valueOf(23850));
                maxChannels = 1;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AAC)) {
                sampleRates = new int[]{7350, AnrMonitor.INPUT_DISPATCHING_TIMEOUT, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000, 64000, 88200, 96000};
                bitRates = Range.create(valueOf, Integer.valueOf(510000));
                maxChannels = 48;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_VORBIS)) {
                bitRates = Range.create(Integer.valueOf(32000), Integer.valueOf(500000));
                sampleRateRange = Range.create(valueOf, Integer.valueOf(AudioFormat.SAMPLE_RATE_HZ_MAX));
                maxChannels = 255;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_OPUS)) {
                bitRates = Range.create(Integer.valueOf(6000), Integer.valueOf(510000));
                sampleRates = new int[]{AnrMonitor.INPUT_DISPATCHING_TIMEOUT, 12000, 16000, 24000, 48000};
                maxChannels = 255;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_RAW)) {
                sampleRateRange = Range.create(valueOf2, Integer.valueOf(96000));
                bitRates = Range.create(valueOf2, Integer.valueOf(10000000));
                maxChannels = AudioSystem.OUT_CHANNEL_COUNT_MAX;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_FLAC)) {
                sampleRateRange = Range.create(valueOf2, Integer.valueOf(655350));
                maxChannels = 255;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_G711_ALAW) || mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_G711_MLAW)) {
                sampleRates = new int[]{AnrMonitor.INPUT_DISPATCHING_TIMEOUT};
                bitRates = Range.create(Integer.valueOf(64000), Integer.valueOf(64000));
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_MSGSM)) {
                sampleRates = new int[]{AnrMonitor.INPUT_DISPATCHING_TIMEOUT};
                bitRates = Range.create(Integer.valueOf(13000), Integer.valueOf(13000));
                maxChannels = 1;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AC3)) {
                maxChannels = 6;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_EAC3)) {
                maxChannels = 16;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_EAC3_JOC)) {
                sampleRates = new int[]{48000};
                bitRates = Range.create(Integer.valueOf(32000), Integer.valueOf(6144000));
                maxChannels = 16;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AC4)) {
                sampleRates = new int[]{44100, 48000, 96000, AudioFormat.SAMPLE_RATE_HZ_MAX};
                bitRates = Range.create(Integer.valueOf(16000), Integer.valueOf(2688000));
                maxChannels = 24;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported mime ");
                stringBuilder.append(mime);
                Log.w(TAG, stringBuilder.toString());
                CodecCapabilities codecCapabilities = this.mParent;
                codecCapabilities.mError |= 2;
            }
            if (sampleRates != null) {
                limitSampleRates(sampleRates);
            } else if (sampleRateRange != null) {
                limitSampleRates(new Range[]{sampleRateRange});
            }
            applyLimits(maxChannels, bitRates);
        }

        private void applyLimits(int maxInputChannels, Range<Integer> bitRates) {
            this.mMaxInputChannelCount = ((Integer) Range.create(Integer.valueOf(1), Integer.valueOf(this.mMaxInputChannelCount)).clamp(Integer.valueOf(maxInputChannels))).intValue();
            if (bitRates != null) {
                this.mBitrateRange = this.mBitrateRange.intersect(bitRates);
            }
        }

        private void parseFromInfo(MediaFormat info) {
            int maxInputChannels = 30;
            Range<Integer> bitRates = MediaCodecInfo.POSITIVE_INTEGERS;
            String str = "sample-rate-ranges";
            if (info.containsKey(str)) {
                String[] rateStrings = info.getString(str).split(",");
                Range[] rateRanges = new Range[rateStrings.length];
                for (int i = 0; i < rateStrings.length; i++) {
                    rateRanges[i] = Utils.parseIntRange(rateStrings[i], null);
                }
                limitSampleRates(rateRanges);
            }
            str = "max-channel-count";
            if (info.containsKey(str)) {
                maxInputChannels = Utils.parseIntSafely(info.getString(str), 30);
            } else if ((this.mParent.mError & 2) != 0) {
                maxInputChannels = 0;
            }
            str = "bitrate-range";
            if (info.containsKey(str)) {
                bitRates = bitRates.intersect(Utils.parseIntRange(info.getString(str), bitRates));
            }
            applyLimits(maxInputChannels, bitRates);
        }

        public void getDefaultFormat(MediaFormat format) {
            if (((Integer) this.mBitrateRange.getLower()).equals(this.mBitrateRange.getUpper())) {
                format.setInteger(MediaFormat.KEY_BIT_RATE, ((Integer) this.mBitrateRange.getLower()).intValue());
            }
            if (this.mMaxInputChannelCount == 1) {
                format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
            }
            int[] iArr = this.mSampleRates;
            if (iArr != null && iArr.length == 1) {
                format.setInteger(MediaFormat.KEY_SAMPLE_RATE, iArr[0]);
            }
        }

        public boolean supportsFormat(MediaFormat format) {
            Map<String, Object> map = format.getMap();
            if (supports((Integer) map.get(MediaFormat.KEY_SAMPLE_RATE), (Integer) map.get(MediaFormat.KEY_CHANNEL_COUNT)) && CodecCapabilities.supportsBitrate(this.mBitrateRange, format)) {
                return true;
            }
            return false;
        }
    }

    public static final class CodecCapabilities {
        public static final int COLOR_Format12bitRGB444 = 3;
        public static final int COLOR_Format16bitARGB1555 = 5;
        public static final int COLOR_Format16bitARGB4444 = 4;
        public static final int COLOR_Format16bitBGR565 = 7;
        public static final int COLOR_Format16bitRGB565 = 6;
        public static final int COLOR_Format18BitBGR666 = 41;
        public static final int COLOR_Format18bitARGB1665 = 9;
        public static final int COLOR_Format18bitRGB666 = 8;
        public static final int COLOR_Format19bitARGB1666 = 10;
        public static final int COLOR_Format24BitABGR6666 = 43;
        public static final int COLOR_Format24BitARGB6666 = 42;
        public static final int COLOR_Format24bitARGB1887 = 13;
        public static final int COLOR_Format24bitBGR888 = 12;
        public static final int COLOR_Format24bitRGB888 = 11;
        public static final int COLOR_Format25bitARGB1888 = 14;
        public static final int COLOR_Format32bitABGR8888 = 2130747392;
        public static final int COLOR_Format32bitARGB8888 = 16;
        public static final int COLOR_Format32bitBGRA8888 = 15;
        public static final int COLOR_Format8bitRGB332 = 2;
        public static final int COLOR_FormatCbYCrY = 27;
        public static final int COLOR_FormatCrYCbY = 28;
        public static final int COLOR_FormatL16 = 36;
        public static final int COLOR_FormatL2 = 33;
        public static final int COLOR_FormatL24 = 37;
        public static final int COLOR_FormatL32 = 38;
        public static final int COLOR_FormatL4 = 34;
        public static final int COLOR_FormatL8 = 35;
        public static final int COLOR_FormatMonochrome = 1;
        public static final int COLOR_FormatRGBAFlexible = 2134288520;
        public static final int COLOR_FormatRGBFlexible = 2134292616;
        public static final int COLOR_FormatRawBayer10bit = 31;
        public static final int COLOR_FormatRawBayer8bit = 30;
        public static final int COLOR_FormatRawBayer8bitcompressed = 32;
        public static final int COLOR_FormatSurface = 2130708361;
        public static final int COLOR_FormatYCbYCr = 25;
        public static final int COLOR_FormatYCrYCb = 26;
        public static final int COLOR_FormatYUV411PackedPlanar = 18;
        public static final int COLOR_FormatYUV411Planar = 17;
        public static final int COLOR_FormatYUV420Flexible = 2135033992;
        public static final int COLOR_FormatYUV420PackedPlanar = 20;
        public static final int COLOR_FormatYUV420PackedSemiPlanar = 39;
        public static final int COLOR_FormatYUV420Planar = 19;
        public static final int COLOR_FormatYUV420SemiPlanar = 21;
        public static final int COLOR_FormatYUV422Flexible = 2135042184;
        public static final int COLOR_FormatYUV422PackedPlanar = 23;
        public static final int COLOR_FormatYUV422PackedSemiPlanar = 40;
        public static final int COLOR_FormatYUV422Planar = 22;
        public static final int COLOR_FormatYUV422SemiPlanar = 24;
        public static final int COLOR_FormatYUV444Flexible = 2135181448;
        public static final int COLOR_FormatYUV444Interleaved = 29;
        public static final int COLOR_QCOM_FormatYUV420SemiPlanar = 2141391872;
        public static final int COLOR_TI_FormatYUV420PackedSemiPlanar = 2130706688;
        public static final String FEATURE_AdaptivePlayback = "adaptive-playback";
        public static final String FEATURE_DynamicTimestamp = "dynamic-timestamp";
        public static final String FEATURE_FrameParsing = "frame-parsing";
        public static final String FEATURE_IntraRefresh = "intra-refresh";
        public static final String FEATURE_MultipleFrames = "multiple-frames";
        public static final String FEATURE_PartialFrame = "partial-frame";
        public static final String FEATURE_SecurePlayback = "secure-playback";
        public static final String FEATURE_TunneledPlayback = "tunneled-playback";
        private static final String TAG = "CodecCapabilities";
        private static final Feature[] decoderFeatures;
        private static final Feature[] encoderFeatures;
        public int[] colorFormats;
        private AudioCapabilities mAudioCaps;
        private MediaFormat mCapabilitiesInfo;
        private MediaFormat mDefaultFormat;
        private EncoderCapabilities mEncoderCaps;
        int mError;
        private int mFlagsRequired;
        private int mFlagsSupported;
        private int mFlagsVerified;
        private int mMaxSupportedInstances;
        private String mMime;
        private VideoCapabilities mVideoCaps;
        public CodecProfileLevel[] profileLevels;

        public final boolean isFeatureSupported(String name) {
            return checkFeature(name, this.mFlagsSupported);
        }

        public final boolean isFeatureRequired(String name) {
            return checkFeature(name, this.mFlagsRequired);
        }

        static {
            r0 = new Feature[7];
            r0[5] = new Feature(FEATURE_MultipleFrames, 32, false);
            r0[6] = new Feature(FEATURE_DynamicTimestamp, 64, false);
            decoderFeatures = r0;
            encoderFeatures = new Feature[]{new Feature(FEATURE_IntraRefresh, 1, false), new Feature(r7, 2, false), new Feature(r8, 4, false)};
        }

        public String[] validFeatures() {
            Feature[] features = getValidFeatures();
            String[] res = new String[features.length];
            for (int i = 0; i < res.length; i++) {
                res[i] = features[i].mName;
            }
            return res;
        }

        private Feature[] getValidFeatures() {
            if (isEncoder()) {
                return encoderFeatures;
            }
            return decoderFeatures;
        }

        private boolean checkFeature(String name, int flags) {
            boolean z = false;
            for (Feature feat : getValidFeatures()) {
                if (feat.mName.equals(name)) {
                    if ((feat.mValue & flags) != 0) {
                        z = true;
                    }
                    return z;
                }
            }
            return false;
        }

        public boolean isRegular() {
            for (Feature feat : getValidFeatures()) {
                if (!feat.mDefault && isFeatureRequired(feat.mName)) {
                    return false;
                }
            }
            return true;
        }

        public final boolean isFormatSupported(MediaFormat format) {
            MediaFormat mediaFormat = format;
            Map<String, Object> map = format.getMap();
            String mime = (String) map.get(MediaFormat.KEY_MIME);
            if (mime != null && !this.mMime.equalsIgnoreCase(mime)) {
                return false;
            }
            for (Feature feat : getValidFeatures()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(MediaFormat.KEY_FEATURE_);
                stringBuilder.append(feat.mName);
                Integer yesNo = (Integer) map.get(stringBuilder.toString());
                if (yesNo != null && ((yesNo.intValue() == 1 && !isFeatureSupported(feat.mName)) || (yesNo.intValue() == 0 && isFeatureRequired(feat.mName)))) {
                    return false;
                }
            }
            String str = MediaFormat.KEY_PROFILE;
            Integer profile = (Integer) map.get(str);
            Integer level = (Integer) map.get("level");
            if (profile != null) {
                if (!supportsProfileLevel(profile.intValue(), level)) {
                    return false;
                }
                int maxLevel = 0;
                for (CodecProfileLevel pl : this.profileLevels) {
                    if (pl.profile == profile.intValue() && pl.level > maxLevel) {
                        maxLevel = pl.level;
                    }
                }
                CodecCapabilities levelCaps = createFromProfileLevel(this.mMime, profile.intValue(), maxLevel);
                Map mapWithoutProfile = new HashMap(map);
                mapWithoutProfile.remove(str);
                MediaFormat formatWithoutProfile = new MediaFormat(mapWithoutProfile);
                if (!(levelCaps == null || levelCaps.isFormatSupported(formatWithoutProfile))) {
                    return false;
                }
            }
            AudioCapabilities audioCapabilities = this.mAudioCaps;
            if (audioCapabilities != null && !audioCapabilities.supportsFormat(mediaFormat)) {
                return false;
            }
            VideoCapabilities videoCapabilities = this.mVideoCaps;
            if (videoCapabilities != null && !videoCapabilities.supportsFormat(mediaFormat)) {
                return false;
            }
            EncoderCapabilities encoderCapabilities = this.mEncoderCaps;
            if (encoderCapabilities == null || encoderCapabilities.supportsFormat(mediaFormat)) {
                return true;
            }
            return false;
        }

        private static boolean supportsBitrate(Range<Integer> bitrateRange, MediaFormat format) {
            Map<String, Object> map = format.getMap();
            Integer maxBitrate = (Integer) map.get(MediaFormat.KEY_MAX_BIT_RATE);
            Comparable bitrate = (Integer) map.get(MediaFormat.KEY_BIT_RATE);
            if (bitrate == null) {
                bitrate = maxBitrate;
            } else if (maxBitrate != null) {
                bitrate = Integer.valueOf(Math.max(bitrate.intValue(), maxBitrate.intValue()));
            }
            if (bitrate == null || bitrate.intValue() <= 0) {
                return true;
            }
            return bitrateRange.contains(bitrate);
        }

        private boolean supportsProfileLevel(int profile, Integer level) {
            boolean z = false;
            for (CodecProfileLevel pl : this.profileLevels) {
                if (pl.profile == profile) {
                    if (level == null || this.mMime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AAC)) {
                        return true;
                    }
                    if ((!this.mMime.equalsIgnoreCase(MediaFormat.MIMETYPE_VIDEO_H263) || pl.level == level.intValue() || pl.level != 16 || level.intValue() <= 1) && (!this.mMime.equalsIgnoreCase(MediaFormat.MIMETYPE_VIDEO_MPEG4) || pl.level == level.intValue() || pl.level != 4 || level.intValue() <= 1)) {
                        if (this.mMime.equalsIgnoreCase(MediaFormat.MIMETYPE_VIDEO_HEVC)) {
                            boolean supportsHighTier = (pl.level & 44739242) != 0;
                            if (((44739242 & level.intValue()) != 0) && !supportsHighTier) {
                            }
                        }
                        if (pl.level >= level.intValue()) {
                            if (createFromProfileLevel(this.mMime, profile, pl.level) == null) {
                                return true;
                            }
                            if (createFromProfileLevel(this.mMime, profile, level.intValue()) != null) {
                                z = true;
                            }
                            return z;
                        }
                    }
                }
            }
            return false;
        }

        public MediaFormat getDefaultFormat() {
            return this.mDefaultFormat;
        }

        public String getMimeType() {
            return this.mMime;
        }

        public int getMaxSupportedInstances() {
            return this.mMaxSupportedInstances;
        }

        private boolean isAudio() {
            return this.mAudioCaps != null;
        }

        public AudioCapabilities getAudioCapabilities() {
            return this.mAudioCaps;
        }

        private boolean isEncoder() {
            return this.mEncoderCaps != null;
        }

        public EncoderCapabilities getEncoderCapabilities() {
            return this.mEncoderCaps;
        }

        private boolean isVideo() {
            return this.mVideoCaps != null;
        }

        public VideoCapabilities getVideoCapabilities() {
            return this.mVideoCaps;
        }

        public CodecCapabilities dup() {
            CodecCapabilities caps = new CodecCapabilities();
            CodecProfileLevel[] codecProfileLevelArr = this.profileLevels;
            caps.profileLevels = (CodecProfileLevel[]) Arrays.copyOf(codecProfileLevelArr, codecProfileLevelArr.length);
            int[] iArr = this.colorFormats;
            caps.colorFormats = Arrays.copyOf(iArr, iArr.length);
            caps.mMime = this.mMime;
            caps.mMaxSupportedInstances = this.mMaxSupportedInstances;
            caps.mFlagsRequired = this.mFlagsRequired;
            caps.mFlagsSupported = this.mFlagsSupported;
            caps.mFlagsVerified = this.mFlagsVerified;
            caps.mAudioCaps = this.mAudioCaps;
            caps.mVideoCaps = this.mVideoCaps;
            caps.mEncoderCaps = this.mEncoderCaps;
            caps.mDefaultFormat = this.mDefaultFormat;
            caps.mCapabilitiesInfo = this.mCapabilitiesInfo;
            return caps;
        }

        public static CodecCapabilities createFromProfileLevel(String mime, int profile, int level) {
            CodecProfileLevel pl = new CodecProfileLevel();
            pl.profile = profile;
            pl.level = level;
            MediaFormat defaultFormat = new MediaFormat();
            defaultFormat.setString(MediaFormat.KEY_MIME, mime);
            CodecCapabilities ret = new CodecCapabilities(new CodecProfileLevel[]{pl}, new int[0], true, defaultFormat, new MediaFormat());
            if (ret.mError != 0) {
                return null;
            }
            return ret;
        }

        CodecCapabilities(CodecProfileLevel[] profLevs, int[] colFmts, boolean encoder, Map<String, Object> defaultFormatMap, Map<String, Object> capabilitiesMap) {
            this(profLevs, colFmts, encoder, new MediaFormat((Map) defaultFormatMap), new MediaFormat((Map) capabilitiesMap));
        }

        CodecCapabilities(CodecProfileLevel[] profLevs, int[] colFmts, boolean encoder, MediaFormat defaultFormat, MediaFormat info) {
            MediaFormat mediaFormat = info;
            Map<String, Object> map = info.getMap();
            this.colorFormats = colFmts;
            int i = 0;
            this.mFlagsVerified = 0;
            this.mDefaultFormat = defaultFormat;
            this.mCapabilitiesInfo = mediaFormat;
            this.mMime = this.mDefaultFormat.getString(MediaFormat.KEY_MIME);
            CodecProfileLevel[] profLevs2 = profLevs;
            int i2 = 1;
            if (profLevs2.length == 0 && this.mMime.equalsIgnoreCase(MediaFormat.MIMETYPE_VIDEO_VP9)) {
                CodecProfileLevel profLev = new CodecProfileLevel();
                profLev.profile = 1;
                profLev.level = VideoCapabilities.equivalentVP9Level(info);
                profLevs2 = new CodecProfileLevel[]{profLev};
            }
            this.profileLevels = profLevs2;
            if (this.mMime.toLowerCase().startsWith("audio/")) {
                this.mAudioCaps = AudioCapabilities.create(mediaFormat, this);
                this.mAudioCaps.getDefaultFormat(this.mDefaultFormat);
            } else if (this.mMime.toLowerCase().startsWith("video/") || this.mMime.equalsIgnoreCase(MediaFormat.MIMETYPE_IMAGE_ANDROID_HEIC)) {
                this.mVideoCaps = VideoCapabilities.create(mediaFormat, this);
            }
            if (encoder) {
                this.mEncoderCaps = EncoderCapabilities.create(mediaFormat, this);
                this.mEncoderCaps.getDefaultFormat(this.mDefaultFormat);
            }
            String str = "max-concurrent-instances";
            this.mMaxSupportedInstances = Utils.parseIntSafely(MediaCodecList.getGlobalSettings().get(str), 32);
            this.mMaxSupportedInstances = ((Integer) Range.create(Integer.valueOf(1), Integer.valueOf(256)).clamp(Integer.valueOf(Utils.parseIntSafely(map.get(str), this.mMaxSupportedInstances)))).intValue();
            Feature[] validFeatures = getValidFeatures();
            int length = validFeatures.length;
            while (i < length) {
                int i3;
                Feature feat = validFeatures[i];
                String key = new StringBuilder();
                key.append(MediaFormat.KEY_FEATURE_);
                key.append(feat.mName);
                key = key.toString();
                Integer yesNo = (Integer) map.get(key);
                if (yesNo == null) {
                    i3 = i2;
                } else {
                    if (yesNo.intValue() > 0) {
                        this.mFlagsRequired = feat.mValue | this.mFlagsRequired;
                    }
                    this.mFlagsSupported |= feat.mValue;
                    i3 = 1;
                    this.mDefaultFormat.setInteger(key, 1);
                }
                i++;
                i2 = i3;
            }
        }
    }

    public static final class CodecProfileLevel {
        public static final int AACObjectELD = 39;
        public static final int AACObjectERLC = 17;
        public static final int AACObjectERScalable = 20;
        public static final int AACObjectHE = 5;
        public static final int AACObjectHE_PS = 29;
        public static final int AACObjectLC = 2;
        public static final int AACObjectLD = 23;
        public static final int AACObjectLTP = 4;
        public static final int AACObjectMain = 1;
        public static final int AACObjectSSR = 3;
        public static final int AACObjectScalable = 6;
        public static final int AACObjectXHE = 42;
        public static final int AV1Level2 = 1;
        public static final int AV1Level21 = 2;
        public static final int AV1Level22 = 4;
        public static final int AV1Level23 = 8;
        public static final int AV1Level3 = 16;
        public static final int AV1Level31 = 32;
        public static final int AV1Level32 = 64;
        public static final int AV1Level33 = 128;
        public static final int AV1Level4 = 256;
        public static final int AV1Level41 = 512;
        public static final int AV1Level42 = 1024;
        public static final int AV1Level43 = 2048;
        public static final int AV1Level5 = 4096;
        public static final int AV1Level51 = 8192;
        public static final int AV1Level52 = 16384;
        public static final int AV1Level53 = 32768;
        public static final int AV1Level6 = 65536;
        public static final int AV1Level61 = 131072;
        public static final int AV1Level62 = 262144;
        public static final int AV1Level63 = 524288;
        public static final int AV1Level7 = 1048576;
        public static final int AV1Level71 = 2097152;
        public static final int AV1Level72 = 4194304;
        public static final int AV1Level73 = 8388608;
        public static final int AV1ProfileMain10 = 2;
        public static final int AV1ProfileMain10HDR10 = 4096;
        public static final int AV1ProfileMain10HDR10Plus = 8192;
        public static final int AV1ProfileMain8 = 1;
        public static final int AVCLevel1 = 1;
        public static final int AVCLevel11 = 4;
        public static final int AVCLevel12 = 8;
        public static final int AVCLevel13 = 16;
        public static final int AVCLevel1b = 2;
        public static final int AVCLevel2 = 32;
        public static final int AVCLevel21 = 64;
        public static final int AVCLevel22 = 128;
        public static final int AVCLevel3 = 256;
        public static final int AVCLevel31 = 512;
        public static final int AVCLevel32 = 1024;
        public static final int AVCLevel4 = 2048;
        public static final int AVCLevel41 = 4096;
        public static final int AVCLevel42 = 8192;
        public static final int AVCLevel5 = 16384;
        public static final int AVCLevel51 = 32768;
        public static final int AVCLevel52 = 65536;
        public static final int AVCLevel6 = 131072;
        public static final int AVCLevel61 = 262144;
        public static final int AVCLevel62 = 524288;
        public static final int AVCProfileBaseline = 1;
        public static final int AVCProfileConstrainedBaseline = 65536;
        public static final int AVCProfileConstrainedHigh = 524288;
        public static final int AVCProfileExtended = 4;
        public static final int AVCProfileHigh = 8;
        public static final int AVCProfileHigh10 = 16;
        public static final int AVCProfileHigh422 = 32;
        public static final int AVCProfileHigh444 = 64;
        public static final int AVCProfileMain = 2;
        public static final int DolbyVisionLevelFhd24 = 4;
        public static final int DolbyVisionLevelFhd30 = 8;
        public static final int DolbyVisionLevelFhd60 = 16;
        public static final int DolbyVisionLevelHd24 = 1;
        public static final int DolbyVisionLevelHd30 = 2;
        public static final int DolbyVisionLevelUhd24 = 32;
        public static final int DolbyVisionLevelUhd30 = 64;
        public static final int DolbyVisionLevelUhd48 = 128;
        public static final int DolbyVisionLevelUhd60 = 256;
        public static final int DolbyVisionProfileDvavPen = 2;
        public static final int DolbyVisionProfileDvavPer = 1;
        public static final int DolbyVisionProfileDvavSe = 512;
        public static final int DolbyVisionProfileDvheDen = 8;
        public static final int DolbyVisionProfileDvheDer = 4;
        public static final int DolbyVisionProfileDvheDtb = 128;
        public static final int DolbyVisionProfileDvheDth = 64;
        public static final int DolbyVisionProfileDvheDtr = 16;
        public static final int DolbyVisionProfileDvheSt = 256;
        public static final int DolbyVisionProfileDvheStn = 32;
        public static final int H263Level10 = 1;
        public static final int H263Level20 = 2;
        public static final int H263Level30 = 4;
        public static final int H263Level40 = 8;
        public static final int H263Level45 = 16;
        public static final int H263Level50 = 32;
        public static final int H263Level60 = 64;
        public static final int H263Level70 = 128;
        public static final int H263ProfileBackwardCompatible = 4;
        public static final int H263ProfileBaseline = 1;
        public static final int H263ProfileH320Coding = 2;
        public static final int H263ProfileHighCompression = 32;
        public static final int H263ProfileHighLatency = 256;
        public static final int H263ProfileISWV2 = 8;
        public static final int H263ProfileISWV3 = 16;
        public static final int H263ProfileInterlace = 128;
        public static final int H263ProfileInternet = 64;
        public static final int HEVCHighTierLevel1 = 2;
        public static final int HEVCHighTierLevel2 = 8;
        public static final int HEVCHighTierLevel21 = 32;
        public static final int HEVCHighTierLevel3 = 128;
        public static final int HEVCHighTierLevel31 = 512;
        public static final int HEVCHighTierLevel4 = 2048;
        public static final int HEVCHighTierLevel41 = 8192;
        public static final int HEVCHighTierLevel5 = 32768;
        public static final int HEVCHighTierLevel51 = 131072;
        public static final int HEVCHighTierLevel52 = 524288;
        public static final int HEVCHighTierLevel6 = 2097152;
        public static final int HEVCHighTierLevel61 = 8388608;
        public static final int HEVCHighTierLevel62 = 33554432;
        private static final int HEVCHighTierLevels = 44739242;
        public static final int HEVCMainTierLevel1 = 1;
        public static final int HEVCMainTierLevel2 = 4;
        public static final int HEVCMainTierLevel21 = 16;
        public static final int HEVCMainTierLevel3 = 64;
        public static final int HEVCMainTierLevel31 = 256;
        public static final int HEVCMainTierLevel4 = 1024;
        public static final int HEVCMainTierLevel41 = 4096;
        public static final int HEVCMainTierLevel5 = 16384;
        public static final int HEVCMainTierLevel51 = 65536;
        public static final int HEVCMainTierLevel52 = 262144;
        public static final int HEVCMainTierLevel6 = 1048576;
        public static final int HEVCMainTierLevel61 = 4194304;
        public static final int HEVCMainTierLevel62 = 16777216;
        public static final int HEVCProfileMain = 1;
        public static final int HEVCProfileMain10 = 2;
        public static final int HEVCProfileMain10HDR10 = 4096;
        public static final int HEVCProfileMain10HDR10Plus = 8192;
        public static final int HEVCProfileMainStill = 4;
        public static final int MPEG2LevelH14 = 2;
        public static final int MPEG2LevelHL = 3;
        public static final int MPEG2LevelHP = 4;
        public static final int MPEG2LevelLL = 0;
        public static final int MPEG2LevelML = 1;
        public static final int MPEG2Profile422 = 2;
        public static final int MPEG2ProfileHigh = 5;
        public static final int MPEG2ProfileMain = 1;
        public static final int MPEG2ProfileSNR = 3;
        public static final int MPEG2ProfileSimple = 0;
        public static final int MPEG2ProfileSpatial = 4;
        public static final int MPEG4Level0 = 1;
        public static final int MPEG4Level0b = 2;
        public static final int MPEG4Level1 = 4;
        public static final int MPEG4Level2 = 8;
        public static final int MPEG4Level3 = 16;
        public static final int MPEG4Level3b = 24;
        public static final int MPEG4Level4 = 32;
        public static final int MPEG4Level4a = 64;
        public static final int MPEG4Level5 = 128;
        public static final int MPEG4Level6 = 256;
        public static final int MPEG4ProfileAdvancedCoding = 4096;
        public static final int MPEG4ProfileAdvancedCore = 8192;
        public static final int MPEG4ProfileAdvancedRealTime = 1024;
        public static final int MPEG4ProfileAdvancedScalable = 16384;
        public static final int MPEG4ProfileAdvancedSimple = 32768;
        public static final int MPEG4ProfileBasicAnimated = 256;
        public static final int MPEG4ProfileCore = 4;
        public static final int MPEG4ProfileCoreScalable = 2048;
        public static final int MPEG4ProfileHybrid = 512;
        public static final int MPEG4ProfileMain = 8;
        public static final int MPEG4ProfileNbit = 16;
        public static final int MPEG4ProfileScalableTexture = 32;
        public static final int MPEG4ProfileSimple = 1;
        public static final int MPEG4ProfileSimpleFBA = 128;
        public static final int MPEG4ProfileSimpleFace = 64;
        public static final int MPEG4ProfileSimpleScalable = 2;
        public static final int VP8Level_Version0 = 1;
        public static final int VP8Level_Version1 = 2;
        public static final int VP8Level_Version2 = 4;
        public static final int VP8Level_Version3 = 8;
        public static final int VP8ProfileMain = 1;
        public static final int VP9Level1 = 1;
        public static final int VP9Level11 = 2;
        public static final int VP9Level2 = 4;
        public static final int VP9Level21 = 8;
        public static final int VP9Level3 = 16;
        public static final int VP9Level31 = 32;
        public static final int VP9Level4 = 64;
        public static final int VP9Level41 = 128;
        public static final int VP9Level5 = 256;
        public static final int VP9Level51 = 512;
        public static final int VP9Level52 = 1024;
        public static final int VP9Level6 = 2048;
        public static final int VP9Level61 = 4096;
        public static final int VP9Level62 = 8192;
        public static final int VP9Profile0 = 1;
        public static final int VP9Profile1 = 2;
        public static final int VP9Profile2 = 4;
        public static final int VP9Profile2HDR = 4096;
        public static final int VP9Profile2HDR10Plus = 16384;
        public static final int VP9Profile3 = 8;
        public static final int VP9Profile3HDR = 8192;
        public static final int VP9Profile3HDR10Plus = 32768;
        public int level;
        public int profile;

        public boolean equals(Object obj) {
            boolean z = false;
            if (obj == null || !(obj instanceof CodecProfileLevel)) {
                return false;
            }
            CodecProfileLevel other = (CodecProfileLevel) obj;
            if (other.profile == this.profile && other.level == this.level) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return Long.hashCode((((long) this.profile) << 32) | ((long) this.level));
        }
    }

    public static final class EncoderCapabilities {
        public static final int BITRATE_MODE_CBR = 2;
        public static final int BITRATE_MODE_CQ = 0;
        public static final int BITRATE_MODE_VBR = 1;
        private static final Feature[] bitrates = new Feature[]{new Feature("VBR", 1, true), new Feature("CBR", 2, false), new Feature("CQ", 0, false)};
        private int mBitControl;
        private Range<Integer> mComplexityRange;
        private Integer mDefaultComplexity;
        private Integer mDefaultQuality;
        private CodecCapabilities mParent;
        private Range<Integer> mQualityRange;
        private String mQualityScale;

        public Range<Integer> getQualityRange() {
            return this.mQualityRange;
        }

        public Range<Integer> getComplexityRange() {
            return this.mComplexityRange;
        }

        private static int parseBitrateMode(String mode) {
            for (Feature feat : bitrates) {
                if (feat.mName.equalsIgnoreCase(mode)) {
                    return feat.mValue;
                }
            }
            return 0;
        }

        public boolean isBitrateModeSupported(int mode) {
            for (Feature feat : bitrates) {
                if (mode == feat.mValue) {
                    boolean z = true;
                    if ((this.mBitControl & (1 << mode)) == 0) {
                        z = false;
                    }
                    return z;
                }
            }
            return false;
        }

        private EncoderCapabilities() {
        }

        public static EncoderCapabilities create(MediaFormat info, CodecCapabilities parent) {
            EncoderCapabilities caps = new EncoderCapabilities();
            caps.init(info, parent);
            return caps;
        }

        private void init(MediaFormat info, CodecCapabilities parent) {
            this.mParent = parent;
            Integer valueOf = Integer.valueOf(0);
            this.mComplexityRange = Range.create(valueOf, valueOf);
            this.mQualityRange = Range.create(valueOf, valueOf);
            this.mBitControl = 2;
            applyLevelLimits();
            parseFromInfo(info);
        }

        private void applyLevelLimits() {
            String mime = this.mParent.getMimeType();
            if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_FLAC)) {
                this.mComplexityRange = Range.create(Integer.valueOf(0), Integer.valueOf(8));
                this.mBitControl = 1;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AMR_NB) || mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AMR_WB) || mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_G711_ALAW) || mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_G711_MLAW) || mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_MSGSM)) {
                this.mBitControl = 4;
            }
        }

        private void parseFromInfo(MediaFormat info) {
            Map<String, Object> map = info.getMap();
            String str = "complexity-range";
            if (info.containsKey(str)) {
                this.mComplexityRange = Utils.parseIntRange(info.getString(str), this.mComplexityRange);
            }
            str = "quality-range";
            if (info.containsKey(str)) {
                this.mQualityRange = Utils.parseIntRange(info.getString(str), this.mQualityRange);
            }
            str = "feature-bitrate-modes";
            if (info.containsKey(str)) {
                for (String mode : info.getString(str).split(",")) {
                    this.mBitControl |= 1 << parseBitrateMode(mode);
                }
            }
            try {
                this.mDefaultComplexity = Integer.valueOf(Integer.parseInt((String) map.get("complexity-default")));
            } catch (NumberFormatException e) {
            }
            try {
                this.mDefaultQuality = Integer.valueOf(Integer.parseInt((String) map.get("quality-default")));
            } catch (NumberFormatException e2) {
            }
            this.mQualityScale = (String) map.get("quality-scale");
        }

        private boolean supports(Integer complexity, Integer quality, Integer profile) {
            boolean ok = true;
            if (!(1 == null || complexity == null)) {
                ok = this.mComplexityRange.contains((Comparable) complexity);
            }
            if (ok && quality != null) {
                ok = this.mQualityRange.contains((Comparable) quality);
            }
            if (!ok || profile == null) {
                return ok;
            }
            boolean z = false;
            for (CodecProfileLevel pl : this.mParent.profileLevels) {
                if (pl.profile == profile.intValue()) {
                    profile = null;
                    break;
                }
            }
            if (profile == null) {
                z = true;
            }
            return z;
        }

        public void getDefaultFormat(MediaFormat format) {
            Integer num;
            if (!((Integer) this.mQualityRange.getUpper()).equals(this.mQualityRange.getLower())) {
                num = this.mDefaultQuality;
                if (num != null) {
                    format.setInteger(MediaFormat.KEY_QUALITY, num.intValue());
                }
            }
            if (!((Integer) this.mComplexityRange.getUpper()).equals(this.mComplexityRange.getLower())) {
                num = this.mDefaultComplexity;
                if (num != null) {
                    format.setInteger(MediaFormat.KEY_COMPLEXITY, num.intValue());
                }
            }
            for (Feature feat : bitrates) {
                if ((this.mBitControl & (1 << feat.mValue)) != 0) {
                    format.setInteger(MediaFormat.KEY_BITRATE_MODE, feat.mValue);
                    return;
                }
            }
        }

        public boolean supportsFormat(MediaFormat format) {
            Map<String, Object> map = format.getMap();
            String mime = this.mParent.getMimeType();
            Integer mode = (Integer) map.get(MediaFormat.KEY_BITRATE_MODE);
            if (mode != null && !isBitrateModeSupported(mode.intValue())) {
                return false;
            }
            Integer flacComplexity;
            Integer complexity = (Integer) map.get(MediaFormat.KEY_COMPLEXITY);
            if (MediaFormat.MIMETYPE_AUDIO_FLAC.equalsIgnoreCase(mime)) {
                flacComplexity = (Integer) map.get(MediaFormat.KEY_FLAC_COMPRESSION_LEVEL);
                if (complexity == null) {
                    complexity = flacComplexity;
                } else if (!(flacComplexity == null || complexity.equals(flacComplexity))) {
                    throw new IllegalArgumentException("conflicting values for complexity and flac-compression-level");
                }
            }
            flacComplexity = (Integer) map.get(MediaFormat.KEY_PROFILE);
            if (MediaFormat.MIMETYPE_AUDIO_AAC.equalsIgnoreCase(mime)) {
                Integer aacProfile = (Integer) map.get(MediaFormat.KEY_AAC_PROFILE);
                if (flacComplexity == null) {
                    flacComplexity = aacProfile;
                } else if (!(aacProfile == null || aacProfile.equals(flacComplexity))) {
                    throw new IllegalArgumentException("conflicting values for profile and aac-profile");
                }
            }
            return supports(complexity, (Integer) map.get(MediaFormat.KEY_QUALITY), flacComplexity);
        }
    }

    private static class Feature {
        public boolean mDefault;
        public String mName;
        public int mValue;

        public Feature(String name, int value, boolean def) {
            this.mName = name;
            this.mValue = value;
            this.mDefault = def;
        }
    }

    public static final class VideoCapabilities {
        private static final String TAG = "VideoCapabilities";
        private boolean mAllowMbOverride;
        private Range<Rational> mAspectRatioRange;
        private Range<Integer> mBitrateRange;
        private Range<Rational> mBlockAspectRatioRange;
        private Range<Integer> mBlockCountRange;
        private int mBlockHeight;
        private int mBlockWidth;
        private Range<Long> mBlocksPerSecondRange;
        private Range<Integer> mFrameRateRange;
        private int mHeightAlignment;
        private Range<Integer> mHeightRange;
        private Range<Integer> mHorizontalBlockRange;
        private Map<Size, Range<Long>> mMeasuredFrameRates;
        private CodecCapabilities mParent;
        private List<PerformancePoint> mPerformancePoints;
        private int mSmallerDimensionUpperLimit;
        private Range<Integer> mVerticalBlockRange;
        private int mWidthAlignment;
        private Range<Integer> mWidthRange;

        public static final class PerformancePoint {
            public static final PerformancePoint FHD_100 = new PerformancePoint(LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING, 1080, 100);
            public static final PerformancePoint FHD_120 = new PerformancePoint(LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING, 1080, 120);
            public static final PerformancePoint FHD_200 = new PerformancePoint(LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING, 1080, 200);
            public static final PerformancePoint FHD_24 = new PerformancePoint(LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING, 1080, 24);
            public static final PerformancePoint FHD_240 = new PerformancePoint(LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING, 1080, 240);
            public static final PerformancePoint FHD_25 = new PerformancePoint(LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING, 1080, 25);
            public static final PerformancePoint FHD_30 = new PerformancePoint(LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING, 1080, 30);
            public static final PerformancePoint FHD_50 = new PerformancePoint(LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING, 1080, 50);
            public static final PerformancePoint FHD_60 = new PerformancePoint(LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING, 1080, 60);
            public static final PerformancePoint HD_100 = new PerformancePoint(1280, MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 100);
            public static final PerformancePoint HD_120 = new PerformancePoint(1280, MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 120);
            public static final PerformancePoint HD_200 = new PerformancePoint(1280, MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 200);
            public static final PerformancePoint HD_24 = new PerformancePoint(1280, MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 24);
            public static final PerformancePoint HD_240 = new PerformancePoint(1280, MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 240);
            public static final PerformancePoint HD_25 = new PerformancePoint(1280, MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 25);
            public static final PerformancePoint HD_30 = new PerformancePoint(1280, MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 30);
            public static final PerformancePoint HD_50 = new PerformancePoint(1280, MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 50);
            public static final PerformancePoint HD_60 = new PerformancePoint(1280, MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 60);
            public static final PerformancePoint SD_24 = new PerformancePoint(MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 480, 24);
            public static final PerformancePoint SD_25 = new PerformancePoint(MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 576, 25);
            public static final PerformancePoint SD_30 = new PerformancePoint(MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 480, 30);
            public static final PerformancePoint SD_48 = new PerformancePoint(MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 480, 48);
            public static final PerformancePoint SD_50 = new PerformancePoint(MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 576, 50);
            public static final PerformancePoint SD_60 = new PerformancePoint(MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH, 480, 60);
            public static final PerformancePoint UHD_100 = new PerformancePoint(3840, 2160, 100);
            public static final PerformancePoint UHD_120 = new PerformancePoint(3840, 2160, 120);
            public static final PerformancePoint UHD_200 = new PerformancePoint(3840, 2160, 200);
            public static final PerformancePoint UHD_24 = new PerformancePoint(3840, 2160, 24);
            public static final PerformancePoint UHD_240 = new PerformancePoint(3840, 2160, 240);
            public static final PerformancePoint UHD_25 = new PerformancePoint(3840, 2160, 25);
            public static final PerformancePoint UHD_30 = new PerformancePoint(3840, 2160, 30);
            public static final PerformancePoint UHD_50 = new PerformancePoint(3840, 2160, 50);
            public static final PerformancePoint UHD_60 = new PerformancePoint(3840, 2160, 60);
            private Size mBlockSize;
            private int mHeight;
            private int mMaxFrameRate;
            private long mMaxMacroBlockRate;
            private int mWidth;

            public int getMaxMacroBlocks() {
                return saturateLongToInt(((long) this.mWidth) * ((long) this.mHeight));
            }

            public int getMaxFrameRate() {
                return this.mMaxFrameRate;
            }

            public long getMaxMacroBlockRate() {
                return this.mMaxMacroBlockRate;
            }

            public String toString() {
                StringBuilder stringBuilder;
                int blockWidth = this.mBlockSize.getWidth() * 16;
                int blockHeight = this.mBlockSize.getHeight() * 16;
                int origRate = (int) Utils.divUp(this.mMaxMacroBlockRate, (long) getMaxMacroBlocks());
                String info = new StringBuilder();
                info.append(this.mWidth * 16);
                String str = "x";
                info.append(str);
                info.append(this.mHeight * 16);
                info.append("@");
                info.append(origRate);
                info = info.toString();
                if (origRate < this.mMaxFrameRate) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(info);
                    stringBuilder2.append(", max ");
                    stringBuilder2.append(this.mMaxFrameRate);
                    stringBuilder2.append("fps");
                    info = stringBuilder2.toString();
                }
                if (blockWidth > 16 || blockHeight > 16) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(info);
                    stringBuilder.append(", ");
                    stringBuilder.append(blockWidth);
                    stringBuilder.append(str);
                    stringBuilder.append(blockHeight);
                    stringBuilder.append(" blocks");
                    info = stringBuilder.toString();
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("PerformancePoint(");
                stringBuilder.append(info);
                stringBuilder.append(")");
                return stringBuilder.toString();
            }

            public int hashCode() {
                return this.mMaxFrameRate;
            }

            public PerformancePoint(int width, int height, int frameRate, int maxFrameRate, Size blockSize) {
                MediaCodecInfo.checkPowerOfTwo(blockSize.getWidth(), "block width");
                MediaCodecInfo.checkPowerOfTwo(blockSize.getHeight(), "block height");
                this.mBlockSize = new Size(Utils.divUp(blockSize.getWidth(), 16), Utils.divUp(blockSize.getHeight(), 16));
                this.mWidth = (int) (Utils.divUp(Math.max(1, (long) width), (long) Math.max(blockSize.getWidth(), 16)) * ((long) this.mBlockSize.getWidth()));
                this.mHeight = (int) (Utils.divUp(Math.max(1, (long) height), (long) Math.max(blockSize.getHeight(), 16)) * ((long) this.mBlockSize.getHeight()));
                this.mMaxFrameRate = Math.max(1, Math.max(frameRate, maxFrameRate));
                this.mMaxMacroBlockRate = (long) (Math.max(1, frameRate) * getMaxMacroBlocks());
            }

            public PerformancePoint(PerformancePoint pp, Size newBlockSize) {
                this(pp.mWidth * 16, pp.mHeight * 16, (int) Utils.divUp(pp.mMaxMacroBlockRate, (long) pp.getMaxMacroBlocks()), pp.mMaxFrameRate, new Size(Math.max(newBlockSize.getWidth(), pp.mBlockSize.getWidth() * 16), Math.max(newBlockSize.getHeight(), pp.mBlockSize.getHeight() * 16)));
            }

            public PerformancePoint(int width, int height, int frameRate) {
                this(width, height, frameRate, frameRate, new Size(16, 16));
            }

            private int saturateLongToInt(long value) {
                if (value < -2147483648L) {
                    return Integer.MIN_VALUE;
                }
                if (value > 2147483647L) {
                    return Integer.MAX_VALUE;
                }
                return (int) value;
            }

            private int align(int value, int alignment) {
                return Utils.divUp(value, alignment) * alignment;
            }

            private void checkPowerOfTwo2(int value, String description) {
                if (value == 0 || ((value - 1) & value) != 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(description);
                    stringBuilder.append(" (");
                    stringBuilder.append(value);
                    stringBuilder.append(") must be a power of 2");
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }

            public boolean covers(MediaFormat format) {
                return covers(new PerformancePoint(format.getInteger("width", 0), format.getInteger("height", 0), Math.round((float) Math.ceil(format.getNumber(MediaFormat.KEY_FRAME_RATE, Integer.valueOf(0)).doubleValue()))));
            }

            public boolean covers(PerformancePoint other) {
                Size commonSize = getCommonBlockSize(other);
                PerformancePoint aligned = new PerformancePoint(this, commonSize);
                PerformancePoint otherAligned = new PerformancePoint(other, commonSize);
                return aligned.getMaxMacroBlocks() >= otherAligned.getMaxMacroBlocks() && aligned.mMaxFrameRate >= otherAligned.mMaxFrameRate && aligned.mMaxMacroBlockRate >= otherAligned.mMaxMacroBlockRate;
            }

            private Size getCommonBlockSize(PerformancePoint other) {
                return new Size(Math.max(this.mBlockSize.getWidth(), other.mBlockSize.getWidth()) * 16, Math.max(this.mBlockSize.getHeight(), other.mBlockSize.getHeight()) * 16);
            }

            public boolean equals(Object o) {
                boolean z = false;
                if (!(o instanceof PerformancePoint)) {
                    return false;
                }
                PerformancePoint other = (PerformancePoint) o;
                Size commonSize = getCommonBlockSize(other);
                PerformancePoint aligned = new PerformancePoint(this, commonSize);
                PerformancePoint otherAligned = new PerformancePoint(other, commonSize);
                if (aligned.getMaxMacroBlocks() == otherAligned.getMaxMacroBlocks() && aligned.mMaxFrameRate == otherAligned.mMaxFrameRate && aligned.mMaxMacroBlockRate == otherAligned.mMaxMacroBlockRate) {
                    z = true;
                }
                return z;
            }
        }

        public Range<Integer> getBitrateRange() {
            return this.mBitrateRange;
        }

        public Range<Integer> getSupportedWidths() {
            return this.mWidthRange;
        }

        public Range<Integer> getSupportedHeights() {
            return this.mHeightRange;
        }

        public int getWidthAlignment() {
            return this.mWidthAlignment;
        }

        public int getHeightAlignment() {
            return this.mHeightAlignment;
        }

        public int getSmallerDimensionUpperLimit() {
            return this.mSmallerDimensionUpperLimit;
        }

        public Range<Integer> getSupportedFrameRates() {
            return this.mFrameRateRange;
        }

        public Range<Integer> getSupportedWidthsFor(int height) {
            String str = "unsupported height";
            try {
                Range<Integer> range = this.mWidthRange;
                if (this.mHeightRange.contains(Integer.valueOf(height)) && height % this.mHeightAlignment == 0) {
                    int heightInBlocks = Utils.divUp(height, this.mBlockHeight);
                    range = range.intersect(Integer.valueOf(((Math.max(Utils.divUp(((Integer) this.mBlockCountRange.getLower()).intValue(), heightInBlocks), (int) Math.ceil(((Rational) this.mBlockAspectRatioRange.getLower()).doubleValue() * ((double) heightInBlocks))) - 1) * this.mBlockWidth) + this.mWidthAlignment), Integer.valueOf(this.mBlockWidth * Math.min(((Integer) this.mBlockCountRange.getUpper()).intValue() / heightInBlocks, (int) (((Rational) this.mBlockAspectRatioRange.getUpper()).doubleValue() * ((double) heightInBlocks)))));
                    if (height > this.mSmallerDimensionUpperLimit) {
                        range = range.intersect(Integer.valueOf(1), Integer.valueOf(this.mSmallerDimensionUpperLimit));
                    }
                    return range.intersect(Integer.valueOf((int) Math.ceil(((Rational) this.mAspectRatioRange.getLower()).doubleValue() * ((double) height))), Integer.valueOf((int) (((Rational) this.mAspectRatioRange.getUpper()).doubleValue() * ((double) height))));
                }
                throw new IllegalArgumentException(str);
            } catch (IllegalArgumentException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("could not get supported widths for ");
                stringBuilder.append(height);
                Log.v(TAG, stringBuilder.toString());
                throw new IllegalArgumentException(str);
            }
        }

        public Range<Integer> getSupportedHeightsFor(int width) {
            String str = "unsupported width";
            try {
                Range<Integer> range = this.mHeightRange;
                if (this.mWidthRange.contains(Integer.valueOf(width)) && width % this.mWidthAlignment == 0) {
                    int widthInBlocks = Utils.divUp(width, this.mBlockWidth);
                    range = range.intersect(Integer.valueOf(((Math.max(Utils.divUp(((Integer) this.mBlockCountRange.getLower()).intValue(), widthInBlocks), (int) Math.ceil(((double) widthInBlocks) / ((Rational) this.mBlockAspectRatioRange.getUpper()).doubleValue())) - 1) * this.mBlockHeight) + this.mHeightAlignment), Integer.valueOf(this.mBlockHeight * Math.min(((Integer) this.mBlockCountRange.getUpper()).intValue() / widthInBlocks, (int) (((double) widthInBlocks) / ((Rational) this.mBlockAspectRatioRange.getLower()).doubleValue()))));
                    if (width > this.mSmallerDimensionUpperLimit) {
                        range = range.intersect(Integer.valueOf(1), Integer.valueOf(this.mSmallerDimensionUpperLimit));
                    }
                    return range.intersect(Integer.valueOf((int) Math.ceil(((double) width) / ((Rational) this.mAspectRatioRange.getUpper()).doubleValue())), Integer.valueOf((int) (((double) width) / ((Rational) this.mAspectRatioRange.getLower()).doubleValue())));
                }
                throw new IllegalArgumentException(str);
            } catch (IllegalArgumentException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("could not get supported heights for ");
                stringBuilder.append(width);
                Log.v(TAG, stringBuilder.toString());
                throw new IllegalArgumentException(str);
            }
        }

        public Range<Double> getSupportedFrameRatesFor(int width, int height) {
            Range<Integer> range = this.mHeightRange;
            if (supports(Integer.valueOf(width), Integer.valueOf(height), null)) {
                int blockCount = Utils.divUp(width, this.mBlockWidth) * Utils.divUp(height, this.mBlockHeight);
                return Range.create(Double.valueOf(Math.max(((double) ((Long) this.mBlocksPerSecondRange.getLower()).longValue()) / ((double) blockCount), (double) ((Integer) this.mFrameRateRange.getLower()).intValue())), Double.valueOf(Math.min(((double) ((Long) this.mBlocksPerSecondRange.getUpper()).longValue()) / ((double) blockCount), (double) ((Integer) this.mFrameRateRange.getUpper()).intValue())));
            }
            throw new IllegalArgumentException("unsupported size");
        }

        private int getBlockCount(int width, int height) {
            return Utils.divUp(width, this.mBlockWidth) * Utils.divUp(height, this.mBlockHeight);
        }

        private Size findClosestSize(int width, int height) {
            int targetBlockCount = getBlockCount(width, height);
            Size closestSize = null;
            int minDiff = Integer.MAX_VALUE;
            for (Size size : this.mMeasuredFrameRates.keySet()) {
                int diff = Math.abs(targetBlockCount - getBlockCount(size.getWidth(), size.getHeight()));
                if (diff < minDiff) {
                    minDiff = diff;
                    closestSize = size;
                }
            }
            return closestSize;
        }

        private Range<Double> estimateFrameRatesFor(int width, int height) {
            Size size = findClosestSize(width, height);
            Range<Long> range = (Range) this.mMeasuredFrameRates.get(size);
            Double ratio = Double.valueOf(((double) getBlockCount(size.getWidth(), size.getHeight())) / ((double) Math.max(getBlockCount(width, height), 1)));
            return Range.create(Double.valueOf(((double) ((Long) range.getLower()).longValue()) * ratio.doubleValue()), Double.valueOf(((double) ((Long) range.getUpper()).longValue()) * ratio.doubleValue()));
        }

        public Range<Double> getAchievableFrameRatesFor(int width, int height) {
            if (supports(Integer.valueOf(width), Integer.valueOf(height), null)) {
                Map map = this.mMeasuredFrameRates;
                if (map != null && map.size() > 0) {
                    return estimateFrameRatesFor(width, height);
                }
                Log.w(TAG, "Codec did not publish any measurement data.");
                return null;
            }
            throw new IllegalArgumentException("unsupported size");
        }

        public List<PerformancePoint> getSupportedPerformancePoints() {
            return this.mPerformancePoints;
        }

        public boolean areSizeAndRateSupported(int width, int height, double frameRate) {
            return supports(Integer.valueOf(width), Integer.valueOf(height), Double.valueOf(frameRate));
        }

        public boolean isSizeSupported(int width, int height) {
            return supports(Integer.valueOf(width), Integer.valueOf(height), null);
        }

        private boolean supports(Integer width, Integer height, Number rate) {
            boolean z;
            boolean ok = true;
            boolean z2 = true;
            if (!(1 == null || width == null)) {
                z = this.mWidthRange.contains((Comparable) width) && width.intValue() % this.mWidthAlignment == 0;
                ok = z;
            }
            if (ok && height != null) {
                z = this.mHeightRange.contains((Comparable) height) && height.intValue() % this.mHeightAlignment == 0;
                ok = z;
            }
            if (ok && rate != null) {
                ok = this.mFrameRateRange.contains(Utils.intRangeFor(rate.doubleValue()));
            }
            if (!ok || height == null || width == null) {
                return ok;
            }
            ok = Math.min(height.intValue(), width.intValue()) <= this.mSmallerDimensionUpperLimit;
            int widthInBlocks = Utils.divUp(width.intValue(), this.mBlockWidth);
            int heightInBlocks = Utils.divUp(height.intValue(), this.mBlockHeight);
            int blockCount = widthInBlocks * heightInBlocks;
            if (!(ok && this.mBlockCountRange.contains(Integer.valueOf(blockCount)) && this.mBlockAspectRatioRange.contains(new Rational(widthInBlocks, heightInBlocks)) && this.mAspectRatioRange.contains(new Rational(width.intValue(), height.intValue())))) {
                z2 = false;
            }
            ok = z2;
            if (!ok || rate == null) {
                return ok;
            }
            return this.mBlocksPerSecondRange.contains(Utils.longRangeFor(((double) blockCount) * rate.doubleValue()));
        }

        public boolean supportsFormat(MediaFormat format) {
            Map<String, Object> map = format.getMap();
            if (supports((Integer) map.get("width"), (Integer) map.get("height"), (Number) map.get(MediaFormat.KEY_FRAME_RATE)) && CodecCapabilities.supportsBitrate(this.mBitrateRange, format)) {
                return true;
            }
            return false;
        }

        private VideoCapabilities() {
        }

        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        public static VideoCapabilities create(MediaFormat info, CodecCapabilities parent) {
            VideoCapabilities caps = new VideoCapabilities();
            caps.init(info, parent);
            return caps;
        }

        private void init(MediaFormat info, CodecCapabilities parent) {
            this.mParent = parent;
            initWithPlatformLimits();
            applyLevelLimits();
            parseFromInfo(info);
            updateLimits();
        }

        public Size getBlockSize() {
            return new Size(this.mBlockWidth, this.mBlockHeight);
        }

        public Range<Integer> getBlockCountRange() {
            return this.mBlockCountRange;
        }

        public Range<Long> getBlocksPerSecondRange() {
            return this.mBlocksPerSecondRange;
        }

        public Range<Rational> getAspectRatioRange(boolean blocks) {
            return blocks ? this.mBlockAspectRatioRange : this.mAspectRatioRange;
        }

        private void initWithPlatformLimits() {
            this.mBitrateRange = MediaCodecInfo.BITRATE_RANGE;
            this.mWidthRange = MediaCodecInfo.SIZE_RANGE;
            this.mHeightRange = MediaCodecInfo.SIZE_RANGE;
            this.mFrameRateRange = MediaCodecInfo.FRAME_RATE_RANGE;
            this.mHorizontalBlockRange = MediaCodecInfo.SIZE_RANGE;
            this.mVerticalBlockRange = MediaCodecInfo.SIZE_RANGE;
            this.mBlockCountRange = MediaCodecInfo.POSITIVE_INTEGERS;
            this.mBlocksPerSecondRange = MediaCodecInfo.POSITIVE_LONGS;
            this.mBlockAspectRatioRange = MediaCodecInfo.POSITIVE_RATIONALS;
            this.mAspectRatioRange = MediaCodecInfo.POSITIVE_RATIONALS;
            this.mWidthAlignment = 2;
            this.mHeightAlignment = 2;
            this.mBlockWidth = 2;
            this.mBlockHeight = 2;
            this.mSmallerDimensionUpperLimit = ((Integer) MediaCodecInfo.SIZE_RANGE.getUpper()).intValue();
        }

        private List<PerformancePoint> getPerformancePoints(Map<String, Object> map) {
            Map<String, Object> map2;
            String str;
            Set<String> set;
            Vector<PerformancePoint> ret = new Vector();
            String prefix = "performance-point-";
            Set<String> keys = map.keySet();
            Iterator it = keys.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                String str2 = "performance-point-";
                if (key.startsWith(str2)) {
                    if (key.substring(str2.length()).equals("none") && ret.size() == 0) {
                        return Collections.unmodifiableList(ret);
                    }
                    String[] temp = key.split("-");
                    if (temp.length == 4) {
                        Size size = Utils.parseSize(temp[2], null);
                        Iterator it2;
                        String str3;
                        if (size == null) {
                            map2 = map;
                            str = prefix;
                            set = keys;
                            it2 = it;
                            str3 = key;
                        } else if (size.getWidth() * size.getHeight() > 0) {
                            Range<Long> range = Utils.parseLongRange(map.get(key), null);
                            if (range == null || ((Long) range.getLower()).longValue() < 0) {
                                set = keys;
                                it2 = it;
                                str3 = key;
                            } else if (((Long) range.getUpper()).longValue() >= 0) {
                                str = prefix;
                                set = keys;
                                PerformancePoint prefix2 = new PerformancePoint(size.getWidth(), size.getHeight(), ((Long) range.getLower()).intValue(), ((Long) range.getUpper()).intValue(), new Size(this.mBlockWidth, this.mBlockHeight));
                                it2 = it;
                                PerformancePoint performancePoint = new PerformancePoint(size.getHeight(), size.getWidth(), ((Long) range.getLower()).intValue(), ((Long) range.getUpper()).intValue(), new Size(this.mBlockWidth, this.mBlockHeight));
                                ret.add(prefix2);
                                if (!prefix2.covers(performancePoint)) {
                                    ret.add(performancePoint);
                                }
                                it = it2;
                                prefix = str;
                                keys = set;
                            }
                        }
                    }
                }
            }
            map2 = map;
            str = prefix;
            set = keys;
            if (ret.size() == 0) {
                return null;
            }
            ret.sort(-$$Lambda$MediaCodecInfo$VideoCapabilities$DpgwEn-gVFZT9EtP3qcxpiA2G0M.INSTANCE);
            return Collections.unmodifiableList(ret);
        }

        static /* synthetic */ int lambda$getPerformancePoints$0(PerformancePoint a, PerformancePoint b) {
            int i = -1;
            if (a.getMaxMacroBlocks() != b.getMaxMacroBlocks()) {
                if (a.getMaxMacroBlocks() >= b.getMaxMacroBlocks()) {
                    i = 1;
                }
            } else if (a.getMaxMacroBlockRate() != b.getMaxMacroBlockRate()) {
                if (a.getMaxMacroBlockRate() >= b.getMaxMacroBlockRate()) {
                    i = 1;
                }
            } else if (a.getMaxFrameRate() == b.getMaxFrameRate()) {
                i = 0;
            } else if (a.getMaxFrameRate() >= b.getMaxFrameRate()) {
                i = 1;
            }
            return -i;
        }

        private Map<Size, Range<Long>> getMeasuredFrameRates(Map<String, Object> map) {
            Map<Size, Range<Long>> ret = new HashMap();
            String prefix = "measured-frame-rate-";
            for (String key : map.keySet()) {
                String str = "measured-frame-rate-";
                if (key.startsWith(str)) {
                    str = key.substring(str.length());
                    String[] temp = key.split("-");
                    if (temp.length == 5) {
                        Size size = Utils.parseSize(temp[3], null);
                        if (size != null) {
                            if (size.getWidth() * size.getHeight() > 0) {
                                Range<Long> range = Utils.parseLongRange(map.get(key), null);
                                if (range != null && ((Long) range.getLower()).longValue() >= 0) {
                                    if (((Long) range.getUpper()).longValue() >= 0) {
                                        ret.put(size, range);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return ret;
        }

        private static Pair<Range<Integer>, Range<Integer>> parseWidthHeightRanges(Object o) {
            Pair<Size, Size> range = Utils.parseSizeRange(o);
            if (range != null) {
                try {
                    return Pair.create(Range.create(Integer.valueOf(((Size) range.first).getWidth()), Integer.valueOf(((Size) range.second).getWidth())), Range.create(Integer.valueOf(((Size) range.first).getHeight()), Integer.valueOf(((Size) range.second).getHeight())));
                } catch (IllegalArgumentException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("could not parse size range '");
                    stringBuilder.append(o);
                    stringBuilder.append("'");
                    Log.w(TAG, stringBuilder.toString());
                }
            }
            return null;
        }

        public static int equivalentVP9Level(MediaFormat info) {
            Map<String, Object> map = info.getMap();
            Size blockSize = Utils.parseSize(map.get("block-size"), new Size(8, 8));
            int BS = blockSize.getWidth() * blockSize.getHeight();
            Range<Integer> counts = Utils.parseIntRange(map.get("block-count-range"), null);
            int BR = 0;
            int FS = counts == null ? 0 : ((Integer) counts.getUpper()).intValue() * BS;
            Range<Long> blockRates = Utils.parseLongRange(map.get("blocks-per-second-range"), null);
            long SR = blockRates == null ? 0 : ((long) BS) * ((Long) blockRates.getUpper()).longValue();
            Pair<Range<Integer>, Range<Integer>> dimensionRanges = parseWidthHeightRanges(map.get("size-range"));
            int D = dimensionRanges == null ? 0 : Math.max(((Integer) ((Range) dimensionRanges.first).getUpper()).intValue(), ((Integer) ((Range) dimensionRanges.second).getUpper()).intValue());
            Range<Integer> bitRates = Utils.parseIntRange(map.get("bitrate-range"), null);
            if (bitRates != null) {
                BR = Utils.divUp(((Integer) bitRates.getUpper()).intValue(), 1000);
            }
            if (SR <= 829440 && FS <= 36864 && BR <= 200 && D <= 512) {
                return 1;
            }
            if (SR <= 2764800 && FS <= 73728 && BR <= 800 && D <= 768) {
                return 2;
            }
            if (SR <= 4608000 && FS <= 122880 && BR <= 1800 && D <= 960) {
                return 4;
            }
            if (SR <= 9216000 && FS <= 245760 && BR <= 3600 && D <= 1344) {
                return 8;
            }
            if (SR <= 20736000 && FS <= 552960 && BR <= 7200 && D <= 2048) {
                return 16;
            }
            if (SR <= 36864000 && FS <= SurfaceControl.FX_SURFACE_MASK && BR <= 12000 && D <= EventLogTags.NOTIFICATION_CANCEL_ALL) {
                return 32;
            }
            if (SR <= 83558400 && FS <= 2228224 && BR <= 18000 && D <= 4160) {
                return 64;
            }
            if (SR <= 160432128 && FS <= 2228224 && BR <= 30000 && D <= 4160) {
                return 128;
            }
            if (SR <= 311951360 && FS <= 8912896 && BR <= 60000 && D <= 8384) {
                return 256;
            }
            if (SR <= 588251136 && FS <= 8912896 && BR <= AutofillManager.MAX_TEMP_AUGMENTED_SERVICE_DURATION_MS && D <= 8384) {
                return 512;
            }
            if (SR <= 1176502272 && FS <= 8912896 && BR <= 180000 && D <= 8384) {
                return 1024;
            }
            if (SR <= 1176502272 && FS <= 35651584 && BR <= 180000 && D <= 16832) {
                return 2048;
            }
            if (SR > 2353004544L || FS > 35651584 || BR > 240000 || D > 16832) {
                return (SR > 4706009088L || FS > 35651584 || BR > 480000 || D <= 16832) ? 8192 : 8192;
            } else {
                return 4096;
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:16:0x012c  */
        /* JADX WARNING: Removed duplicated region for block: B:11:0x00fb A:{SYNTHETIC, Splitter:B:11:0x00fb} */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x0169  */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x013b A:{SYNTHETIC, Splitter:B:19:0x013b} */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x027c  */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x01d7  */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x0288  */
        /* JADX WARNING: Removed duplicated region for block: B:55:0x0294  */
        /* JADX WARNING: Removed duplicated region for block: B:57:0x02a0  */
        /* JADX WARNING: Removed duplicated region for block: B:59:0x02bf  */
        /* JADX WARNING: Removed duplicated region for block: B:61:0x02df  */
        /* JADX WARNING: Removed duplicated region for block: B:63:0x02fd  */
        /* JADX WARNING: Removed duplicated region for block: B:65:0x0309  */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x0315  */
        private void parseFromInfo(android.media.MediaFormat r30) {
            /*
            r29 = this;
            r11 = r29;
            r12 = r30.getMap();
            r0 = new android.util.Size;
            r1 = r11.mBlockWidth;
            r2 = r11.mBlockHeight;
            r0.<init>(r1, r2);
            r1 = new android.util.Size;
            r2 = r11.mWidthAlignment;
            r3 = r11.mHeightAlignment;
            r1.<init>(r2, r3);
            r2 = 0;
            r3 = 0;
            r4 = 0;
            r5 = 0;
            r6 = 0;
            r7 = 0;
            r8 = 0;
            r9 = 0;
            r10 = "block-size";
            r10 = r12.get(r10);
            r13 = android.media.Utils.parseSize(r10, r0);
            r0 = "alignment";
            r0 = r12.get(r0);
            r14 = android.media.Utils.parseSize(r0, r1);
            r0 = "block-count-range";
            r0 = r12.get(r0);
            r1 = 0;
            r15 = android.media.Utils.parseIntRange(r0, r1);
            r0 = "blocks-per-second-range";
            r0 = r12.get(r0);
            r10 = android.media.Utils.parseLongRange(r0, r1);
            r0 = r11.getMeasuredFrameRates(r12);
            r11.mMeasuredFrameRates = r0;
            r0 = r11.getPerformancePoints(r12);
            r11.mPerformancePoints = r0;
            r0 = "size-range";
            r0 = r12.get(r0);
            r7 = parseWidthHeightRanges(r0);
            if (r7 == 0) goto L_0x006e;
        L_0x0064:
            r0 = r7.first;
            r3 = r0;
            r3 = (android.util.Range) r3;
            r0 = r7.second;
            r4 = r0;
            r4 = (android.util.Range) r4;
        L_0x006e:
            r0 = "feature-can-swap-width-height";
            r0 = r12.containsKey(r0);
            r2 = "VideoCapabilities";
            if (r0 == 0) goto L_0x00d0;
        L_0x0078:
            if (r3 == 0) goto L_0x00a1;
            r0 = r3.getUpper();
            r0 = (java.lang.Integer) r0;
            r0 = r0.intValue();
            r16 = r4.getUpper();
            r16 = (java.lang.Integer) r16;
            r1 = r16.intValue();
            r0 = java.lang.Math.min(r0, r1);
            r11.mSmallerDimensionUpperLimit = r0;
            r0 = r3.extend(r4);
            r4 = r0;
            r3 = r0;
            r28 = r4;
            r4 = r3;
            r3 = r28;
            goto L_0x00d5;
        L_0x00a1:
            r0 = "feature can-swap-width-height is best used with size-range";
            android.util.Log.w(r2, r0);
            r0 = r11.mWidthRange;
            r0 = r0.getUpper();
            r0 = (java.lang.Integer) r0;
            r0 = r0.intValue();
            r1 = r11.mHeightRange;
            r1 = r1.getUpper();
            r1 = (java.lang.Integer) r1;
            r1 = r1.intValue();
            r0 = java.lang.Math.min(r0, r1);
            r11.mSmallerDimensionUpperLimit = r0;
            r0 = r11.mWidthRange;
            r1 = r11.mHeightRange;
            r0 = r0.extend(r1);
            r11.mHeightRange = r0;
            r11.mWidthRange = r0;
        L_0x00d0:
            r28 = r4;
            r4 = r3;
            r3 = r28;
            r0 = "block-aspect-ratio-range";
            r0 = r12.get(r0);
            r1 = 0;
            r8 = android.media.Utils.parseRationalRange(r0, r1);
            r0 = "pixel-aspect-ratio-range";
            r0 = r12.get(r0);
            r9 = android.media.Utils.parseRationalRange(r0, r1);
            r0 = "frame-rate-range";
            r0 = r12.get(r0);
            r5 = android.media.Utils.parseIntRange(r0, r1);
            r1 = ") is out of limits: ";
            if (r5 == 0) goto L_0x012c;
        L_0x00fb:
            r0 = android.media.MediaCodecInfo.FRAME_RATE_RANGE;	 Catch:{ IllegalArgumentException -> 0x0107 }
            r0 = r5.intersect(r0);	 Catch:{ IllegalArgumentException -> 0x0107 }
            r5 = r0;
            r18 = r3;
            goto L_0x012e;
        L_0x0107:
            r0 = move-exception;
            r16 = r0;
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r18 = r3;
            r3 = "frame rate range (";
            r0.append(r3);
            r0.append(r5);
            r0.append(r1);
            r3 = android.media.MediaCodecInfo.FRAME_RATE_RANGE;
            r0.append(r3);
            r0 = r0.toString();
            android.util.Log.w(r2, r0);
            r5 = 0;
            goto L_0x012e;
        L_0x012c:
            r18 = r3;
        L_0x012e:
            r0 = "bitrate-range";
            r0 = r12.get(r0);
            r3 = 0;
            r3 = android.media.Utils.parseIntRange(r0, r3);
            if (r3 == 0) goto L_0x0169;
        L_0x013b:
            r0 = android.media.MediaCodecInfo.BITRATE_RANGE;	 Catch:{ IllegalArgumentException -> 0x0145 }
            r0 = r3.intersect(r0);	 Catch:{ IllegalArgumentException -> 0x0145 }
            r3 = r0;
            goto L_0x016a;
        L_0x0145:
            r0 = move-exception;
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r16 = r0;
            r0 = "bitrate range (";
            r6.append(r0);
            r6.append(r3);
            r6.append(r1);
            r0 = android.media.MediaCodecInfo.BITRATE_RANGE;
            r6.append(r0);
            r0 = r6.toString();
            android.util.Log.w(r2, r0);
            r3 = 0;
            r0 = r3;
            goto L_0x016a;
        L_0x0169:
            r0 = r3;
            r1 = r13.getWidth();
            r2 = "block-size width must be power of two";
            android.media.MediaCodecInfo.checkPowerOfTwo(r1, r2);
            r1 = r13.getHeight();
            r2 = "block-size height must be power of two";
            android.media.MediaCodecInfo.checkPowerOfTwo(r1, r2);
            r1 = r14.getWidth();
            r2 = "alignment width must be power of two";
            android.media.MediaCodecInfo.checkPowerOfTwo(r1, r2);
            r1 = r14.getHeight();
            r2 = "alignment height must be power of two";
            android.media.MediaCodecInfo.checkPowerOfTwo(r1, r2);
            r2 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
            r3 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
            r6 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
            r16 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r19 = r13.getWidth();
            r20 = r13.getHeight();
            r21 = r14.getWidth();
            r22 = r14.getHeight();
            r1 = r29;
            r23 = r18;
            r24 = r4;
            r4 = r6;
            r25 = r5;
            r5 = r16;
            r16 = r7;
            r7 = r19;
            r26 = r8;
            r8 = r20;
            r27 = r9;
            r9 = r21;
            r17 = r12;
            r12 = r10;
            r10 = r22;
            r1.applyMacroBlockLimits(r2, r3, r4, r5, r7, r8, r9, r10);
            r1 = r11.mParent;
            r1 = r1.mError;
            r1 = r1 & 2;
            if (r1 != 0) goto L_0x027c;
        L_0x01d7:
            r1 = r11.mAllowMbOverride;
            if (r1 == 0) goto L_0x01e7;
        L_0x01db:
            r4 = r23;
            r3 = r24;
            r5 = r25;
            r2 = r26;
            r1 = r27;
            goto L_0x0286;
        L_0x01e7:
            r3 = r24;
            if (r3 == 0) goto L_0x01f3;
        L_0x01eb:
            r1 = r11.mWidthRange;
            r1 = r1.intersect(r3);
            r11.mWidthRange = r1;
        L_0x01f3:
            r4 = r23;
            if (r4 == 0) goto L_0x01ff;
        L_0x01f7:
            r1 = r11.mHeightRange;
            r1 = r1.intersect(r4);
            r11.mHeightRange = r1;
        L_0x01ff:
            if (r15 == 0) goto L_0x021c;
        L_0x0201:
            r1 = r11.mBlockCountRange;
            r2 = r11.mBlockWidth;
            r5 = r11.mBlockHeight;
            r2 = r2 * r5;
            r5 = r13.getWidth();
            r2 = r2 / r5;
            r5 = r13.getHeight();
            r2 = r2 / r5;
            r2 = android.media.Utils.factorRange(r15, r2);
            r1 = r1.intersect(r2);
            r11.mBlockCountRange = r1;
        L_0x021c:
            if (r12 == 0) goto L_0x023a;
        L_0x021e:
            r1 = r11.mBlocksPerSecondRange;
            r2 = r11.mBlockWidth;
            r5 = r11.mBlockHeight;
            r2 = r2 * r5;
            r5 = r13.getWidth();
            r2 = r2 / r5;
            r5 = r13.getHeight();
            r2 = r2 / r5;
            r5 = (long) r2;
            r2 = android.media.Utils.factorRange(r12, r5);
            r1 = r1.intersect(r2);
            r11.mBlocksPerSecondRange = r1;
        L_0x023a:
            r1 = r27;
            if (r1 == 0) goto L_0x0258;
        L_0x023e:
            r2 = r11.mBlockAspectRatioRange;
            r5 = r11.mBlockHeight;
            r6 = r13.getHeight();
            r5 = r5 / r6;
            r6 = r11.mBlockWidth;
            r7 = r13.getWidth();
            r6 = r6 / r7;
            r5 = android.media.Utils.scaleRange(r1, r5, r6);
            r2 = r2.intersect(r5);
            r11.mBlockAspectRatioRange = r2;
        L_0x0258:
            r2 = r26;
            if (r2 == 0) goto L_0x0264;
        L_0x025c:
            r5 = r11.mAspectRatioRange;
            r5 = r5.intersect(r2);
            r11.mAspectRatioRange = r5;
        L_0x0264:
            r5 = r25;
            if (r5 == 0) goto L_0x0270;
        L_0x0268:
            r6 = r11.mFrameRateRange;
            r6 = r6.intersect(r5);
            r11.mFrameRateRange = r6;
        L_0x0270:
            if (r0 == 0) goto L_0x0330;
        L_0x0272:
            r6 = r11.mBitrateRange;
            r6 = r6.intersect(r0);
            r11.mBitrateRange = r6;
            goto L_0x0330;
        L_0x027c:
            r4 = r23;
            r3 = r24;
            r5 = r25;
            r2 = r26;
            r1 = r27;
        L_0x0286:
            if (r3 == 0) goto L_0x0292;
        L_0x0288:
            r6 = android.media.MediaCodecInfo.SIZE_RANGE;
            r6 = r6.intersect(r3);
            r11.mWidthRange = r6;
        L_0x0292:
            if (r4 == 0) goto L_0x029e;
        L_0x0294:
            r6 = android.media.MediaCodecInfo.SIZE_RANGE;
            r6 = r6.intersect(r4);
            r11.mHeightRange = r6;
        L_0x029e:
            if (r15 == 0) goto L_0x02bd;
        L_0x02a0:
            r6 = android.media.MediaCodecInfo.POSITIVE_INTEGERS;
            r7 = r11.mBlockWidth;
            r8 = r11.mBlockHeight;
            r7 = r7 * r8;
            r8 = r13.getWidth();
            r7 = r7 / r8;
            r8 = r13.getHeight();
            r7 = r7 / r8;
            r7 = android.media.Utils.factorRange(r15, r7);
            r6 = r6.intersect(r7);
            r11.mBlockCountRange = r6;
        L_0x02bd:
            if (r12 == 0) goto L_0x02dd;
        L_0x02bf:
            r6 = android.media.MediaCodecInfo.POSITIVE_LONGS;
            r7 = r11.mBlockWidth;
            r8 = r11.mBlockHeight;
            r7 = r7 * r8;
            r8 = r13.getWidth();
            r7 = r7 / r8;
            r8 = r13.getHeight();
            r7 = r7 / r8;
            r7 = (long) r7;
            r7 = android.media.Utils.factorRange(r12, r7);
            r6 = r6.intersect(r7);
            r11.mBlocksPerSecondRange = r6;
        L_0x02dd:
            if (r1 == 0) goto L_0x02fb;
        L_0x02df:
            r6 = android.media.MediaCodecInfo.POSITIVE_RATIONALS;
            r7 = r11.mBlockHeight;
            r8 = r13.getHeight();
            r7 = r7 / r8;
            r8 = r11.mBlockWidth;
            r9 = r13.getWidth();
            r8 = r8 / r9;
            r7 = android.media.Utils.scaleRange(r1, r7, r8);
            r6 = r6.intersect(r7);
            r11.mBlockAspectRatioRange = r6;
        L_0x02fb:
            if (r2 == 0) goto L_0x0307;
        L_0x02fd:
            r6 = android.media.MediaCodecInfo.POSITIVE_RATIONALS;
            r6 = r6.intersect(r2);
            r11.mAspectRatioRange = r6;
        L_0x0307:
            if (r5 == 0) goto L_0x0313;
        L_0x0309:
            r6 = android.media.MediaCodecInfo.FRAME_RATE_RANGE;
            r6 = r6.intersect(r5);
            r11.mFrameRateRange = r6;
        L_0x0313:
            if (r0 == 0) goto L_0x0330;
        L_0x0315:
            r6 = r11.mParent;
            r6 = r6.mError;
            r6 = r6 & 2;
            if (r6 == 0) goto L_0x0328;
        L_0x031d:
            r6 = android.media.MediaCodecInfo.BITRATE_RANGE;
            r6 = r6.intersect(r0);
            r11.mBitrateRange = r6;
            goto L_0x0330;
        L_0x0328:
            r6 = r11.mBitrateRange;
            r6 = r6.intersect(r0);
            r11.mBitrateRange = r6;
        L_0x0330:
            r29.updateLimits();
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaCodecInfo$VideoCapabilities.parseFromInfo(android.media.MediaFormat):void");
        }

        private void applyBlockLimits(int blockWidth, int blockHeight, Range<Integer> counts, Range<Long> rates, Range<Rational> ratios) {
            MediaCodecInfo.checkPowerOfTwo(blockWidth, "blockWidth must be a power of two");
            MediaCodecInfo.checkPowerOfTwo(blockHeight, "blockHeight must be a power of two");
            int newBlockWidth = Math.max(blockWidth, this.mBlockWidth);
            int newBlockHeight = Math.max(blockHeight, this.mBlockHeight);
            int factor = ((newBlockWidth * newBlockHeight) / this.mBlockWidth) / this.mBlockHeight;
            if (factor != 1) {
                this.mBlockCountRange = Utils.factorRange(this.mBlockCountRange, factor);
                this.mBlocksPerSecondRange = Utils.factorRange(this.mBlocksPerSecondRange, (long) factor);
                this.mBlockAspectRatioRange = Utils.scaleRange(this.mBlockAspectRatioRange, newBlockHeight / this.mBlockHeight, newBlockWidth / this.mBlockWidth);
                this.mHorizontalBlockRange = Utils.factorRange(this.mHorizontalBlockRange, newBlockWidth / this.mBlockWidth);
                this.mVerticalBlockRange = Utils.factorRange(this.mVerticalBlockRange, newBlockHeight / this.mBlockHeight);
            }
            int factor2 = ((newBlockWidth * newBlockHeight) / blockWidth) / blockHeight;
            if (factor2 != 1) {
                counts = Utils.factorRange((Range) counts, factor2);
                rates = Utils.factorRange((Range) rates, (long) factor2);
                ratios = Utils.scaleRange(ratios, newBlockHeight / blockHeight, newBlockWidth / blockWidth);
            }
            this.mBlockCountRange = this.mBlockCountRange.intersect(counts);
            this.mBlocksPerSecondRange = this.mBlocksPerSecondRange.intersect(rates);
            this.mBlockAspectRatioRange = this.mBlockAspectRatioRange.intersect(ratios);
            this.mBlockWidth = newBlockWidth;
            this.mBlockHeight = newBlockHeight;
        }

        private void applyAlignment(int widthAlignment, int heightAlignment) {
            MediaCodecInfo.checkPowerOfTwo(widthAlignment, "widthAlignment must be a power of two");
            MediaCodecInfo.checkPowerOfTwo(heightAlignment, "heightAlignment must be a power of two");
            if (widthAlignment > this.mBlockWidth || heightAlignment > this.mBlockHeight) {
                applyBlockLimits(Math.max(widthAlignment, this.mBlockWidth), Math.max(heightAlignment, this.mBlockHeight), MediaCodecInfo.POSITIVE_INTEGERS, MediaCodecInfo.POSITIVE_LONGS, MediaCodecInfo.POSITIVE_RATIONALS);
            }
            this.mWidthAlignment = Math.max(widthAlignment, this.mWidthAlignment);
            this.mHeightAlignment = Math.max(heightAlignment, this.mHeightAlignment);
            this.mWidthRange = Utils.alignRange(this.mWidthRange, this.mWidthAlignment);
            this.mHeightRange = Utils.alignRange(this.mHeightRange, this.mHeightAlignment);
        }

        private void updateLimits() {
            this.mHorizontalBlockRange = this.mHorizontalBlockRange.intersect(Utils.factorRange(this.mWidthRange, this.mBlockWidth));
            this.mHorizontalBlockRange = this.mHorizontalBlockRange.intersect(Range.create(Integer.valueOf(((Integer) this.mBlockCountRange.getLower()).intValue() / ((Integer) this.mVerticalBlockRange.getUpper()).intValue()), Integer.valueOf(((Integer) this.mBlockCountRange.getUpper()).intValue() / ((Integer) this.mVerticalBlockRange.getLower()).intValue())));
            this.mVerticalBlockRange = this.mVerticalBlockRange.intersect(Utils.factorRange(this.mHeightRange, this.mBlockHeight));
            this.mVerticalBlockRange = this.mVerticalBlockRange.intersect(Range.create(Integer.valueOf(((Integer) this.mBlockCountRange.getLower()).intValue() / ((Integer) this.mHorizontalBlockRange.getUpper()).intValue()), Integer.valueOf(((Integer) this.mBlockCountRange.getUpper()).intValue() / ((Integer) this.mHorizontalBlockRange.getLower()).intValue())));
            this.mBlockCountRange = this.mBlockCountRange.intersect(Range.create(Integer.valueOf(((Integer) this.mHorizontalBlockRange.getLower()).intValue() * ((Integer) this.mVerticalBlockRange.getLower()).intValue()), Integer.valueOf(((Integer) this.mHorizontalBlockRange.getUpper()).intValue() * ((Integer) this.mVerticalBlockRange.getUpper()).intValue())));
            this.mBlockAspectRatioRange = this.mBlockAspectRatioRange.intersect(new Rational(((Integer) this.mHorizontalBlockRange.getLower()).intValue(), ((Integer) this.mVerticalBlockRange.getUpper()).intValue()), new Rational(((Integer) this.mHorizontalBlockRange.getUpper()).intValue(), ((Integer) this.mVerticalBlockRange.getLower()).intValue()));
            this.mWidthRange = this.mWidthRange.intersect(Integer.valueOf(((((Integer) this.mHorizontalBlockRange.getLower()).intValue() - 1) * this.mBlockWidth) + this.mWidthAlignment), Integer.valueOf(((Integer) this.mHorizontalBlockRange.getUpper()).intValue() * this.mBlockWidth));
            this.mHeightRange = this.mHeightRange.intersect(Integer.valueOf(((((Integer) this.mVerticalBlockRange.getLower()).intValue() - 1) * this.mBlockHeight) + this.mHeightAlignment), Integer.valueOf(((Integer) this.mVerticalBlockRange.getUpper()).intValue() * this.mBlockHeight));
            this.mAspectRatioRange = this.mAspectRatioRange.intersect(new Rational(((Integer) this.mWidthRange.getLower()).intValue(), ((Integer) this.mHeightRange.getUpper()).intValue()), new Rational(((Integer) this.mWidthRange.getUpper()).intValue(), ((Integer) this.mHeightRange.getLower()).intValue()));
            this.mSmallerDimensionUpperLimit = Math.min(this.mSmallerDimensionUpperLimit, Math.min(((Integer) this.mWidthRange.getUpper()).intValue(), ((Integer) this.mHeightRange.getUpper()).intValue()));
            this.mBlocksPerSecondRange = this.mBlocksPerSecondRange.intersect(Long.valueOf(((long) ((Integer) this.mBlockCountRange.getLower()).intValue()) * ((long) ((Integer) this.mFrameRateRange.getLower()).intValue())), Long.valueOf(((long) ((Integer) this.mBlockCountRange.getUpper()).intValue()) * ((long) ((Integer) this.mFrameRateRange.getUpper()).intValue())));
            this.mFrameRateRange = this.mFrameRateRange.intersect(Integer.valueOf((int) (((Long) this.mBlocksPerSecondRange.getLower()).longValue() / ((long) ((Integer) this.mBlockCountRange.getUpper()).intValue()))), Integer.valueOf((int) (((double) ((Long) this.mBlocksPerSecondRange.getUpper()).longValue()) / ((double) ((Integer) this.mBlockCountRange.getLower()).intValue()))));
        }

        private void applyMacroBlockLimits(int maxHorizontalBlocks, int maxVerticalBlocks, int maxBlocks, long maxBlocksPerSecond, int blockWidth, int blockHeight, int widthAlignment, int heightAlignment) {
            applyMacroBlockLimits(1, 1, maxHorizontalBlocks, maxVerticalBlocks, maxBlocks, maxBlocksPerSecond, blockWidth, blockHeight, widthAlignment, heightAlignment);
        }

        private void applyMacroBlockLimits(int minHorizontalBlocks, int minVerticalBlocks, int maxHorizontalBlocks, int maxVerticalBlocks, int maxBlocks, long maxBlocksPerSecond, int blockWidth, int blockHeight, int widthAlignment, int heightAlignment) {
            int i = maxHorizontalBlocks;
            int i2 = maxVerticalBlocks;
            applyAlignment(widthAlignment, heightAlignment);
            applyBlockLimits(blockWidth, blockHeight, Range.create(Integer.valueOf(1), Integer.valueOf(maxBlocks)), Range.create(Long.valueOf(1), Long.valueOf(maxBlocksPerSecond)), Range.create(new Rational(1, maxVerticalBlocks), new Rational(maxHorizontalBlocks, 1)));
            int i3 = minHorizontalBlocks;
            this.mHorizontalBlockRange = this.mHorizontalBlockRange.intersect(Integer.valueOf(Utils.divUp(minHorizontalBlocks, this.mBlockWidth / blockWidth)), Integer.valueOf(i / (this.mBlockWidth / blockWidth)));
            int i4 = minVerticalBlocks;
            this.mVerticalBlockRange = this.mVerticalBlockRange.intersect(Integer.valueOf(Utils.divUp(minVerticalBlocks, this.mBlockHeight / blockHeight)), Integer.valueOf(i2 / (this.mBlockHeight / blockHeight)));
        }

        /* JADX WARNING: Removed duplicated region for block: B:376:0x029e A:{SYNTHETIC} */
        /* JADX WARNING: Removed duplicated region for block: B:54:0x029a  */
        /* JADX WARNING: Removed duplicated region for block: B:54:0x029a  */
        /* JADX WARNING: Removed duplicated region for block: B:376:0x029e A:{SYNTHETIC} */
        private void applyLevelLimits() {
            /*
            r42 = this;
            r12 = r42;
            r0 = 0;
            r2 = 0;
            r3 = 0;
            r13 = 0;
            r4 = 4;
            r5 = r12.mParent;
            r14 = r5.profileLevels;
            r5 = r12.mParent;
            r15 = r5.getMimeType();
            r5 = "video/avc";
            r5 = r15.equalsIgnoreCase(r5);
            r7 = "Unrecognized level ";
            r9 = "Unrecognized profile ";
            r6 = " for ";
            r10 = "VideoCapabilities";
            r11 = 1;
            r8 = java.lang.Integer.valueOf(r11);
            if (r5 == 0) goto L_0x02ec;
        L_0x0028:
            r2 = 99;
            r0 = 1485; // 0x5cd float:2.081E-42 double:7.337E-321;
            r3 = 64000; // 0xfa00 float:8.9683E-41 double:3.162E-319;
            r5 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r13 = r14.length;
            r16 = r4;
            r4 = r2;
            r39 = r5;
            r5 = r3;
            r2 = r0;
            r1 = r39;
            r0 = 0;
        L_0x003c:
            if (r0 >= r13) goto L_0x02c1;
        L_0x003e:
            r11 = r14[r0];
            r21 = 0;
            r22 = 0;
            r23 = 0;
            r24 = 0;
            r25 = 1;
            r26 = r8;
            r8 = r11.level;
            r27 = r13;
            r13 = 1;
            if (r8 == r13) goto L_0x0218;
        L_0x0053:
            r13 = 2;
            if (r8 == r13) goto L_0x0205;
        L_0x0056:
            switch(r8) {
                case 4: goto L_0x01f2;
                case 8: goto L_0x01df;
                case 16: goto L_0x01cc;
                case 32: goto L_0x01b8;
                case 64: goto L_0x01a4;
                case 128: goto L_0x0190;
                case 256: goto L_0x017b;
                case 512: goto L_0x0166;
                case 1024: goto L_0x0151;
                case 2048: goto L_0x013b;
                case 4096: goto L_0x0124;
                case 8192: goto L_0x010d;
                case 16384: goto L_0x00f7;
                case 32768: goto L_0x00e0;
                case 65536: goto L_0x00c8;
                case 131072: goto L_0x00b0;
                case 262144: goto L_0x0098;
                case 524288: goto L_0x0081;
                default: goto L_0x0059;
            };
        L_0x0059:
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r8.append(r7);
            r13 = r11.level;
            r8.append(r13);
            r8.append(r6);
            r8.append(r15);
            r8 = r8.toString();
            android.util.Log.w(r10, r8);
            r16 = r16 | 1;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x0081:
            r21 = 16711680; // 0xff0000 float:2.3418052E-38 double:8.256667E-317;
            r22 = 139264; // 0x22000 float:1.9515E-40 double:6.88056E-319;
            r23 = 800000; // 0xc3500 float:1.121039E-39 double:3.952525E-318;
            r24 = 696320; // 0xaa000 float:9.75752E-40 double:3.44028E-318;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x0098:
            r21 = 8355840; // 0x7f8000 float:1.1709026E-38 double:4.1283335E-317;
            r22 = 139264; // 0x22000 float:1.9515E-40 double:6.88056E-319;
            r23 = 480000; // 0x75300 float:6.72623E-40 double:2.371515E-318;
            r24 = 696320; // 0xaa000 float:9.75752E-40 double:3.44028E-318;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x00b0:
            r21 = 4177920; // 0x3fc000 float:5.854513E-39 double:2.0641667E-317;
            r22 = 139264; // 0x22000 float:1.9515E-40 double:6.88056E-319;
            r23 = 240000; // 0x3a980 float:3.36312E-40 double:1.18576E-318;
            r24 = 696320; // 0xaa000 float:9.75752E-40 double:3.44028E-318;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x00c8:
            r21 = 2073600; // 0x1fa400 float:2.905732E-39 double:1.0244945E-317;
            r22 = 36864; // 0x9000 float:5.1657E-41 double:1.8213E-319;
            r23 = 240000; // 0x3a980 float:3.36312E-40 double:1.18576E-318;
            r24 = 184320; // 0x2d000 float:2.58287E-40 double:9.1066E-319;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x00e0:
            r21 = 983040; // 0xf0000 float:1.377532E-39 double:4.856863E-318;
            r22 = 36864; // 0x9000 float:5.1657E-41 double:1.8213E-319;
            r23 = 240000; // 0x3a980 float:3.36312E-40 double:1.18576E-318;
            r24 = 184320; // 0x2d000 float:2.58287E-40 double:9.1066E-319;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x00f7:
            r21 = 589824; // 0x90000 float:8.2652E-40 double:2.91412E-318;
            r22 = 22080; // 0x5640 float:3.094E-41 double:1.0909E-319;
            r23 = 135000; // 0x20f58 float:1.89175E-40 double:6.6699E-319;
            r24 = 110400; // 0x1af40 float:1.54703E-40 double:5.4545E-319;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x010d:
            r21 = 522240; // 0x7f800 float:7.31814E-40 double:2.58021E-318;
            r22 = 8704; // 0x2200 float:1.2197E-41 double:4.3003E-320;
            r23 = 50000; // 0xc350 float:7.0065E-41 double:2.47033E-319;
            r24 = 34816; // 0x8800 float:4.8788E-41 double:1.72014E-319;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x0124:
            r21 = 245760; // 0x3c000 float:3.44383E-40 double:1.214216E-318;
            r22 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r23 = 50000; // 0xc350 float:7.0065E-41 double:2.47033E-319;
            r24 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x013b:
            r21 = 245760; // 0x3c000 float:3.44383E-40 double:1.214216E-318;
            r22 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r23 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
            r24 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x0151:
            r21 = 216000; // 0x34bc0 float:3.0268E-40 double:1.06718E-318;
            r22 = 5120; // 0x1400 float:7.175E-42 double:2.5296E-320;
            r23 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
            r24 = 20480; // 0x5000 float:2.8699E-41 double:1.01185E-319;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x0166:
            r21 = 108000; // 0x1a5e0 float:1.5134E-40 double:5.3359E-319;
            r22 = 3600; // 0xe10 float:5.045E-42 double:1.7786E-320;
            r23 = 14000; // 0x36b0 float:1.9618E-41 double:6.917E-320;
            r24 = 18000; // 0x4650 float:2.5223E-41 double:8.893E-320;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x017b:
            r21 = 40500; // 0x9e34 float:5.6753E-41 double:2.00097E-319;
            r22 = 1620; // 0x654 float:2.27E-42 double:8.004E-321;
            r23 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
            r24 = 8100; // 0x1fa4 float:1.135E-41 double:4.002E-320;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x0190:
            r21 = 20250; // 0x4f1a float:2.8376E-41 double:1.0005E-319;
            r22 = 1620; // 0x654 float:2.27E-42 double:8.004E-321;
            r23 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
            r24 = 8100; // 0x1fa4 float:1.135E-41 double:4.002E-320;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x01a4:
            r21 = 19800; // 0x4d58 float:2.7746E-41 double:9.7825E-320;
            r22 = 792; // 0x318 float:1.11E-42 double:3.913E-321;
            r23 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
            r24 = 4752; // 0x1290 float:6.659E-42 double:2.348E-320;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x01b8:
            r21 = 11880; // 0x2e68 float:1.6647E-41 double:5.8695E-320;
            r22 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r23 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
            r24 = 2376; // 0x948 float:3.33E-42 double:1.174E-320;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x01cc:
            r21 = 11880; // 0x2e68 float:1.6647E-41 double:5.8695E-320;
            r22 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r23 = 768; // 0x300 float:1.076E-42 double:3.794E-321;
            r24 = 2376; // 0x948 float:3.33E-42 double:1.174E-320;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x01df:
            r21 = 6000; // 0x1770 float:8.408E-42 double:2.9644E-320;
            r22 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r23 = 384; // 0x180 float:5.38E-43 double:1.897E-321;
            r24 = 2376; // 0x948 float:3.33E-42 double:1.174E-320;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x01f2:
            r21 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;
            r22 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r23 = 192; // 0xc0 float:2.69E-43 double:9.5E-322;
            r24 = 900; // 0x384 float:1.261E-42 double:4.447E-321;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x0205:
            r21 = 1485; // 0x5cd float:2.081E-42 double:7.337E-321;
            r22 = 99;
            r23 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
            r24 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
            goto L_0x022a;
        L_0x0218:
            r21 = 1485; // 0x5cd float:2.081E-42 double:7.337E-321;
            r22 = 99;
            r23 = 64;
            r24 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r13 = r21;
            r8 = r23;
            r12 = r24;
            r21 = r7;
            r7 = r22;
        L_0x022a:
            r22 = r14;
            r14 = r11.profile;
            r23 = r0;
            r0 = 1;
            if (r14 == r0) goto L_0x0296;
        L_0x0233:
            r0 = 2;
            if (r14 == r0) goto L_0x0296;
        L_0x0236:
            r0 = 4;
            if (r14 == r0) goto L_0x0276;
        L_0x0239:
            r0 = 8;
            if (r14 == r0) goto L_0x0273;
        L_0x023d:
            r0 = 16;
            if (r14 == r0) goto L_0x0270;
        L_0x0241:
            r0 = 32;
            if (r14 == r0) goto L_0x0276;
        L_0x0245:
            r0 = 64;
            if (r14 == r0) goto L_0x0276;
        L_0x0249:
            r0 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
            if (r14 == r0) goto L_0x0296;
        L_0x024d:
            r0 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
            if (r14 == r0) goto L_0x0273;
        L_0x0251:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r0.append(r9);
            r14 = r11.profile;
            r0.append(r14);
            r0.append(r6);
            r0.append(r15);
            r0 = r0.toString();
            android.util.Log.w(r10, r0);
            r16 = r16 | 1;
            r8 = r8 * 1000;
            goto L_0x0298;
        L_0x0270:
            r8 = r8 * 3000;
            goto L_0x0298;
        L_0x0273:
            r8 = r8 * 1250;
            goto L_0x0298;
        L_0x0276:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r14 = "Unsupported profile ";
            r0.append(r14);
            r14 = r11.profile;
            r0.append(r14);
            r0.append(r6);
            r0.append(r15);
            r0 = r0.toString();
            android.util.Log.w(r10, r0);
            r16 = r16 | 2;
            r25 = 0;
        L_0x0296:
            r8 = r8 * 1000;
        L_0x0298:
            if (r25 == 0) goto L_0x029e;
        L_0x029a:
            r0 = r16 & -5;
            r16 = r0;
        L_0x029e:
            r14 = r10;
            r0 = r11;
            r10 = (long) r13;
            r2 = java.lang.Math.max(r10, r2);
            r4 = java.lang.Math.max(r7, r4);
            r5 = java.lang.Math.max(r8, r5);
            r1 = java.lang.Math.max(r1, r12);
            r0 = r23 + 1;
            r11 = 1;
            r12 = r42;
            r10 = r14;
            r7 = r21;
            r14 = r22;
            r8 = r26;
            r13 = r27;
            goto L_0x003c;
        L_0x02c1:
            r26 = r8;
            r22 = r14;
            r0 = r4 * 8;
            r6 = (double) r0;
            r6 = java.lang.Math.sqrt(r6);
            r10 = (int) r6;
            r6 = 16;
            r7 = 16;
            r8 = 1;
            r9 = 1;
            r0 = r42;
            r11 = r1;
            r1 = r10;
            r12 = r2;
            r2 = r10;
            r3 = r4;
            r14 = r4;
            r17 = r5;
            r4 = r12;
            r28 = r26;
            r0.applyMacroBlockLimits(r1, r2, r3, r4, r6, r7, r8, r9);
            r29 = r12;
            r12 = r14;
            r27 = r22;
            r22 = r11;
            goto L_0x137d;
        L_0x02ec:
            r21 = r7;
            r28 = r8;
            r22 = r14;
            r14 = r10;
            r5 = "video/mpeg2";
            r5 = r15.equalsIgnoreCase(r5);
            r7 = "/";
            r8 = "Unrecognized profile/level ";
            if (r5 == 0) goto L_0x05b1;
        L_0x0300:
            r5 = 11;
            r10 = 9;
            r11 = 15;
            r2 = 99;
            r0 = 1485; // 0x5cd float:2.081E-42 double:7.337E-321;
            r3 = 64000; // 0xfa00 float:8.9683E-41 double:3.162E-319;
            r17 = r0;
            r12 = r22;
            r0 = r12.length;
            r16 = r4;
            r22 = r13;
            r1 = 0;
            r4 = r2;
            r13 = r3;
            r2 = r17;
            r39 = r10;
            r10 = r5;
            r5 = r11;
            r11 = r39;
        L_0x0321:
            if (r1 >= r0) goto L_0x0581;
        L_0x0323:
            r17 = r0;
            r0 = r12[r1];
            r18 = 0;
            r19 = 0;
            r21 = 0;
            r23 = 0;
            r24 = 0;
            r25 = 0;
            r26 = 1;
            r27 = r12;
            r12 = r0.profile;
            if (r12 == 0) goto L_0x04df;
        L_0x033b:
            r29 = r1;
            r1 = 1;
            if (r12 == r1) goto L_0x03c0;
        L_0x0340:
            r1 = 2;
            if (r12 == r1) goto L_0x0384;
        L_0x0343:
            r1 = 3;
            if (r12 == r1) goto L_0x0384;
        L_0x0346:
            r1 = 4;
            if (r12 == r1) goto L_0x0384;
        L_0x0349:
            r1 = 5;
            if (r12 == r1) goto L_0x0384;
        L_0x034c:
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r1.append(r9);
            r12 = r0.profile;
            r1.append(r12);
            r1.append(r6);
            r1.append(r15);
            r1 = r1.toString();
            android.util.Log.w(r14, r1);
            r16 = r16 | 1;
            r1 = r18;
            r12 = r19;
            r18 = r0;
            r0 = r21;
            r39 = r23;
            r23 = r7;
            r7 = r39;
            r40 = r24;
            r24 = r8;
            r8 = r40;
            r41 = r25;
            r25 = r14;
            r14 = r41;
            goto L_0x054c;
        L_0x0384:
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r12 = "Unsupported profile ";
            r1.append(r12);
            r12 = r0.profile;
            r1.append(r12);
            r1.append(r6);
            r1.append(r15);
            r1 = r1.toString();
            android.util.Log.i(r14, r1);
            r16 = r16 | 2;
            r26 = 0;
            r1 = r18;
            r12 = r19;
            r18 = r0;
            r0 = r21;
            r39 = r23;
            r23 = r7;
            r7 = r39;
            r40 = r24;
            r24 = r8;
            r8 = r40;
            r41 = r25;
            r25 = r14;
            r14 = r41;
            goto L_0x054c;
        L_0x03c0:
            r1 = r0.level;
            if (r1 == 0) goto L_0x04b7;
        L_0x03c4:
            r12 = 1;
            if (r1 == r12) goto L_0x048e;
        L_0x03c7:
            r12 = 2;
            if (r1 == r12) goto L_0x0464;
        L_0x03ca:
            r12 = 3;
            if (r1 == r12) goto L_0x043a;
        L_0x03cd:
            r12 = 4;
            if (r1 == r12) goto L_0x0410;
        L_0x03d0:
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r1.append(r8);
            r12 = r0.profile;
            r1.append(r12);
            r1.append(r7);
            r12 = r0.level;
            r1.append(r12);
            r1.append(r6);
            r1.append(r15);
            r1 = r1.toString();
            android.util.Log.w(r14, r1);
            r16 = r16 | 1;
            r1 = r18;
            r12 = r19;
            r18 = r0;
            r0 = r21;
            r39 = r23;
            r23 = r7;
            r7 = r39;
            r40 = r24;
            r24 = r8;
            r8 = r40;
            r41 = r25;
            r25 = r14;
            r14 = r41;
            goto L_0x054c;
        L_0x0410:
            r23 = 60;
            r24 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
            r25 = 68;
            r18 = 489600; // 0x77880 float:6.86076E-40 double:2.418945E-318;
            r19 = 8160; // 0x1fe0 float:1.1435E-41 double:4.0316E-320;
            r21 = 80000; // 0x13880 float:1.12104E-40 double:3.95253E-319;
            r1 = r18;
            r12 = r19;
            r18 = r0;
            r0 = r21;
            r39 = r23;
            r23 = r7;
            r7 = r39;
            r40 = r24;
            r24 = r8;
            r8 = r40;
            r41 = r25;
            r25 = r14;
            r14 = r41;
            goto L_0x054c;
        L_0x043a:
            r23 = 60;
            r24 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
            r25 = 68;
            r18 = 244800; // 0x3bc40 float:3.43038E-40 double:1.209473E-318;
            r19 = 8160; // 0x1fe0 float:1.1435E-41 double:4.0316E-320;
            r21 = 80000; // 0x13880 float:1.12104E-40 double:3.95253E-319;
            r1 = r18;
            r12 = r19;
            r18 = r0;
            r0 = r21;
            r39 = r23;
            r23 = r7;
            r7 = r39;
            r40 = r24;
            r24 = r8;
            r8 = r40;
            r41 = r25;
            r25 = r14;
            r14 = r41;
            goto L_0x054c;
        L_0x0464:
            r23 = 60;
            r24 = 90;
            r25 = 68;
            r18 = 183600; // 0x2cd30 float:2.57278E-40 double:9.07105E-319;
            r19 = 6120; // 0x17e8 float:8.576E-42 double:3.0237E-320;
            r21 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
            r1 = r18;
            r12 = r19;
            r18 = r0;
            r0 = r21;
            r39 = r23;
            r23 = r7;
            r7 = r39;
            r40 = r24;
            r24 = r8;
            r8 = r40;
            r41 = r25;
            r25 = r14;
            r14 = r41;
            goto L_0x054c;
        L_0x048e:
            r23 = 30;
            r24 = 45;
            r25 = 36;
            r18 = 40500; // 0x9e34 float:5.6753E-41 double:2.00097E-319;
            r19 = 1620; // 0x654 float:2.27E-42 double:8.004E-321;
            r21 = 15000; // 0x3a98 float:2.102E-41 double:7.411E-320;
            r1 = r18;
            r12 = r19;
            r18 = r0;
            r0 = r21;
            r39 = r23;
            r23 = r7;
            r7 = r39;
            r40 = r24;
            r24 = r8;
            r8 = r40;
            r41 = r25;
            r25 = r14;
            r14 = r41;
            goto L_0x054c;
        L_0x04b7:
            r23 = 30;
            r24 = 22;
            r25 = 18;
            r18 = 11880; // 0x2e68 float:1.6647E-41 double:5.8695E-320;
            r19 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r21 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
            r1 = r18;
            r12 = r19;
            r18 = r0;
            r0 = r21;
            r39 = r23;
            r23 = r7;
            r7 = r39;
            r40 = r24;
            r24 = r8;
            r8 = r40;
            r41 = r25;
            r25 = r14;
            r14 = r41;
            goto L_0x054c;
        L_0x04df:
            r29 = r1;
            r1 = r0.level;
            r12 = 1;
            if (r1 == r12) goto L_0x0525;
        L_0x04e6:
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r1.append(r8);
            r12 = r0.profile;
            r1.append(r12);
            r1.append(r7);
            r12 = r0.level;
            r1.append(r12);
            r1.append(r6);
            r1.append(r15);
            r1 = r1.toString();
            android.util.Log.w(r14, r1);
            r16 = r16 | 1;
            r1 = r18;
            r12 = r19;
            r18 = r0;
            r0 = r21;
            r39 = r23;
            r23 = r7;
            r7 = r39;
            r40 = r24;
            r24 = r8;
            r8 = r40;
            r41 = r25;
            r25 = r14;
            r14 = r41;
            goto L_0x054c;
        L_0x0525:
            r23 = 30;
            r24 = 45;
            r25 = 36;
            r18 = 40500; // 0x9e34 float:5.6753E-41 double:2.00097E-319;
            r19 = 1620; // 0x654 float:2.27E-42 double:8.004E-321;
            r21 = 15000; // 0x3a98 float:2.102E-41 double:7.411E-320;
            r1 = r18;
            r12 = r19;
            r18 = r0;
            r0 = r21;
            r39 = r23;
            r23 = r7;
            r7 = r39;
            r40 = r24;
            r24 = r8;
            r8 = r40;
            r41 = r25;
            r25 = r14;
            r14 = r41;
        L_0x054c:
            if (r26 == 0) goto L_0x0550;
        L_0x054e:
            r16 = r16 & -5;
        L_0x0550:
            r19 = r5;
            r30 = r6;
            r5 = (long) r1;
            r2 = java.lang.Math.max(r5, r2);
            r4 = java.lang.Math.max(r12, r4);
            r5 = r0 * 1000;
            r13 = java.lang.Math.max(r5, r13);
            r10 = java.lang.Math.max(r8, r10);
            r11 = java.lang.Math.max(r14, r11);
            r5 = r19;
            r5 = java.lang.Math.max(r7, r5);
            r1 = r29 + 1;
            r0 = r17;
            r7 = r23;
            r8 = r24;
            r14 = r25;
            r12 = r27;
            r6 = r30;
            goto L_0x0321;
        L_0x0581:
            r27 = r12;
            r6 = 16;
            r7 = 16;
            r8 = 1;
            r9 = 1;
            r0 = r42;
            r1 = r10;
            r17 = r2;
            r2 = r11;
            r3 = r4;
            r12 = r4;
            r14 = r5;
            r4 = r17;
            r0.applyMacroBlockLimits(r1, r2, r3, r4, r6, r7, r8, r9);
            r8 = r42;
            r0 = r8.mFrameRateRange;
            r1 = 12;
            r1 = java.lang.Integer.valueOf(r1);
            r2 = java.lang.Integer.valueOf(r14);
            r0 = r0.intersect(r1, r2);
            r8.mFrameRateRange = r0;
            r29 = r17;
            r17 = r13;
            goto L_0x137d;
        L_0x05b1:
            r30 = r6;
            r23 = r7;
            r24 = r8;
            r25 = r14;
            r27 = r22;
            r8 = r42;
            r22 = r13;
            r5 = "video/mp4v-es";
            r5 = r15.equalsIgnoreCase(r5);
            if (r5 == 0) goto L_0x09eb;
        L_0x05c8:
            r5 = 11;
            r6 = 9;
            r7 = 15;
            r2 = 99;
            r0 = 1485; // 0x5cd float:2.081E-42 double:7.337E-321;
            r3 = 64000; // 0xfa00 float:8.9683E-41 double:3.162E-319;
            r12 = r27;
            r10 = r12.length;
            r14 = r2;
            r16 = r4;
            r11 = r5;
            r13 = r6;
            r4 = r7;
            r6 = r0;
            r5 = r3;
            r0 = 0;
        L_0x05e1:
            if (r0 >= r10) goto L_0x09b2;
        L_0x05e3:
            r1 = r12[r0];
            r2 = 0;
            r3 = 0;
            r21 = 0;
            r26 = 0;
            r27 = 0;
            r29 = 0;
            r31 = 0;
            r32 = 1;
            r33 = r2;
            r2 = r1.profile;
            r34 = r3;
            r3 = 1;
            if (r2 == r3) goto L_0x07dd;
        L_0x05fc:
            r3 = 2;
            if (r2 == r3) goto L_0x0799;
        L_0x05ff:
            switch(r2) {
                case 4: goto L_0x0799;
                case 8: goto L_0x0799;
                case 16: goto L_0x0799;
                case 32: goto L_0x0799;
                case 64: goto L_0x0799;
                case 128: goto L_0x0799;
                case 256: goto L_0x0799;
                case 512: goto L_0x0799;
                case 1024: goto L_0x0799;
                case 2048: goto L_0x0799;
                case 4096: goto L_0x0799;
                case 8192: goto L_0x0799;
                case 16384: goto L_0x0799;
                case 32768: goto L_0x063e;
                default: goto L_0x0602;
            };
        L_0x0602:
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r2.append(r9);
            r3 = r1.profile;
            r2.append(r3);
            r3 = r30;
            r2.append(r3);
            r2.append(r15);
            r2 = r2.toString();
            r8 = r25;
            android.util.Log.w(r8, r2);
            r16 = r16 | 1;
            r25 = r10;
            r2 = r21;
            r10 = r24;
            r3 = r26;
            r21 = r1;
            r26 = r8;
            r24 = r9;
            r8 = r27;
            r9 = r29;
            r1 = r33;
            r27 = r23;
            r23 = r12;
            r12 = r34;
            goto L_0x094f;
        L_0x063e:
            r8 = r25;
            r3 = r30;
            r2 = r1.level;
            r25 = r10;
            r10 = 1;
            if (r2 == r10) goto L_0x0770;
        L_0x0649:
            r10 = 4;
            if (r2 == r10) goto L_0x0770;
        L_0x064c:
            r10 = 8;
            if (r2 == r10) goto L_0x0747;
        L_0x0650:
            r10 = 16;
            if (r2 == r10) goto L_0x071e;
        L_0x0654:
            r10 = 24;
            if (r2 == r10) goto L_0x06f5;
        L_0x0658:
            r10 = 32;
            if (r2 == r10) goto L_0x06cc;
        L_0x065c:
            r10 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
            if (r2 == r10) goto L_0x06a2;
        L_0x0660:
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r10 = r24;
            r2.append(r10);
            r24 = r9;
            r9 = r1.profile;
            r2.append(r9);
            r9 = r23;
            r2.append(r9);
            r23 = r12;
            r12 = r1.level;
            r2.append(r12);
            r2.append(r3);
            r2.append(r15);
            r2 = r2.toString();
            android.util.Log.w(r8, r2);
            r16 = r16 | 1;
            r30 = r3;
            r2 = r21;
            r3 = r26;
            r12 = r34;
            r21 = r1;
            r26 = r8;
            r8 = r27;
            r1 = r33;
            r27 = r9;
            r9 = r29;
            goto L_0x094f;
        L_0x06a2:
            r10 = r24;
            r24 = r9;
            r9 = r23;
            r23 = r12;
            r26 = 30;
            r27 = 45;
            r29 = 36;
            r2 = 48600; // 0xbdd8 float:6.8103E-41 double:2.40116E-319;
            r12 = 1620; // 0x654 float:2.27E-42 double:8.004E-321;
            r21 = 8000; // 0x1f40 float:1.121E-41 double:3.9525E-320;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x06cc:
            r10 = r24;
            r24 = r9;
            r9 = r23;
            r23 = r12;
            r26 = 30;
            r27 = 44;
            r29 = 36;
            r2 = 23760; // 0x5cd0 float:3.3295E-41 double:1.1739E-319;
            r12 = 792; // 0x318 float:1.11E-42 double:3.913E-321;
            r21 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x06f5:
            r10 = r24;
            r24 = r9;
            r9 = r23;
            r23 = r12;
            r26 = 30;
            r27 = 22;
            r29 = 18;
            r2 = 11880; // 0x2e68 float:1.6647E-41 double:5.8695E-320;
            r12 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r21 = 1500; // 0x5dc float:2.102E-42 double:7.41E-321;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x071e:
            r10 = r24;
            r24 = r9;
            r9 = r23;
            r23 = r12;
            r26 = 30;
            r27 = 22;
            r29 = 18;
            r2 = 11880; // 0x2e68 float:1.6647E-41 double:5.8695E-320;
            r12 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r21 = 768; // 0x300 float:1.076E-42 double:3.794E-321;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x0747:
            r10 = r24;
            r24 = r9;
            r9 = r23;
            r23 = r12;
            r26 = 30;
            r27 = 22;
            r29 = 18;
            r2 = 5940; // 0x1734 float:8.324E-42 double:2.9347E-320;
            r12 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r21 = 384; // 0x180 float:5.38E-43 double:1.897E-321;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x0770:
            r10 = r24;
            r24 = r9;
            r9 = r23;
            r23 = r12;
            r26 = 30;
            r27 = 11;
            r29 = 9;
            r2 = 2970; // 0xb9a float:4.162E-42 double:1.4674E-320;
            r12 = 99;
            r21 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x0799:
            r8 = r25;
            r3 = r30;
            r25 = r10;
            r10 = r24;
            r24 = r9;
            r9 = r23;
            r23 = r12;
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r12 = "Unsupported profile ";
            r2.append(r12);
            r12 = r1.profile;
            r2.append(r12);
            r2.append(r3);
            r2.append(r15);
            r2 = r2.toString();
            android.util.Log.i(r8, r2);
            r16 = r16 | 2;
            r32 = 0;
            r30 = r3;
            r2 = r21;
            r3 = r26;
            r12 = r34;
            r21 = r1;
            r26 = r8;
            r8 = r27;
            r1 = r33;
            r27 = r9;
            r9 = r29;
            goto L_0x094f;
        L_0x07dd:
            r8 = r25;
            r3 = r30;
            r25 = r10;
            r10 = r24;
            r24 = r9;
            r9 = r23;
            r23 = r12;
            r2 = r1.level;
            r12 = 1;
            if (r2 == r12) goto L_0x092e;
        L_0x07f0:
            r12 = 2;
            if (r2 == r12) goto L_0x090c;
        L_0x07f3:
            r12 = 4;
            if (r2 == r12) goto L_0x08ec;
        L_0x07f6:
            r12 = 8;
            if (r2 == r12) goto L_0x08cb;
        L_0x07fa:
            r12 = 16;
            if (r2 == r12) goto L_0x08aa;
        L_0x07fe:
            r12 = 64;
            if (r2 == r12) goto L_0x0888;
        L_0x0802:
            r12 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
            if (r2 == r12) goto L_0x0866;
        L_0x0806:
            r12 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
            if (r2 == r12) goto L_0x0844;
        L_0x080a:
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r2.append(r10);
            r12 = r1.profile;
            r2.append(r12);
            r2.append(r9);
            r12 = r1.level;
            r2.append(r12);
            r2.append(r3);
            r2.append(r15);
            r2 = r2.toString();
            android.util.Log.w(r8, r2);
            r16 = r16 | 1;
            r30 = r3;
            r2 = r21;
            r3 = r26;
            r12 = r34;
            r21 = r1;
            r26 = r8;
            r8 = r27;
            r1 = r33;
            r27 = r9;
            r9 = r29;
            goto L_0x094f;
        L_0x0844:
            r26 = 30;
            r27 = 80;
            r29 = 45;
            r2 = 108000; // 0x1a5e0 float:1.5134E-40 double:5.3359E-319;
            r12 = 3600; // 0xe10 float:5.045E-42 double:1.7786E-320;
            r21 = 12000; // 0x2ee0 float:1.6816E-41 double:5.929E-320;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x0866:
            r26 = 30;
            r27 = 45;
            r29 = 36;
            r2 = 40500; // 0x9e34 float:5.6753E-41 double:2.00097E-319;
            r12 = 1620; // 0x654 float:2.27E-42 double:8.004E-321;
            r21 = 8000; // 0x1f40 float:1.121E-41 double:3.9525E-320;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x0888:
            r26 = 30;
            r27 = 40;
            r29 = 30;
            r2 = 36000; // 0x8ca0 float:5.0447E-41 double:1.77864E-319;
            r12 = 1200; // 0x4b0 float:1.682E-42 double:5.93E-321;
            r21 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x08aa:
            r26 = 30;
            r27 = 22;
            r29 = 18;
            r2 = 11880; // 0x2e68 float:1.6647E-41 double:5.8695E-320;
            r12 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r21 = 384; // 0x180 float:5.38E-43 double:1.897E-321;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x08cb:
            r26 = 30;
            r27 = 22;
            r29 = 18;
            r2 = 5940; // 0x1734 float:8.324E-42 double:2.9347E-320;
            r12 = 396; // 0x18c float:5.55E-43 double:1.956E-321;
            r21 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x08ec:
            r26 = 30;
            r27 = 11;
            r29 = 9;
            r2 = 1485; // 0x5cd float:2.081E-42 double:7.337E-321;
            r12 = 99;
            r21 = 64;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x090c:
            r31 = 1;
            r26 = 15;
            r27 = 11;
            r29 = 9;
            r2 = 1485; // 0x5cd float:2.081E-42 double:7.337E-321;
            r12 = 99;
            r21 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
            goto L_0x094f;
        L_0x092e:
            r31 = 1;
            r26 = 15;
            r27 = 11;
            r29 = 9;
            r2 = 1485; // 0x5cd float:2.081E-42 double:7.337E-321;
            r12 = 99;
            r21 = 64;
            r30 = r3;
            r3 = r26;
            r26 = r8;
            r8 = r27;
            r27 = r9;
            r9 = r29;
            r39 = r21;
            r21 = r1;
            r1 = r2;
            r2 = r39;
        L_0x094f:
            if (r32 == 0) goto L_0x0953;
        L_0x0951:
            r16 = r16 & -5;
        L_0x0953:
            r33 = r3;
            r29 = r4;
            r3 = (long) r1;
            r6 = java.lang.Math.max(r3, r6);
            r14 = java.lang.Math.max(r12, r14);
            r3 = r2 * 1000;
            r5 = java.lang.Math.max(r3, r5);
            if (r31 == 0) goto L_0x097c;
        L_0x0968:
            r3 = java.lang.Math.max(r8, r11);
            r4 = java.lang.Math.max(r9, r13);
            r11 = r29;
            r13 = r33;
            r11 = java.lang.Math.max(r13, r11);
            r13 = r4;
            r4 = r11;
            r11 = r3;
            goto L_0x09a0;
        L_0x097c:
            r4 = r29;
            r3 = r33;
            r29 = r1;
            r1 = r12 * 2;
            r33 = r2;
            r1 = (double) r1;
            r1 = java.lang.Math.sqrt(r1);
            r1 = (int) r1;
            r2 = java.lang.Math.max(r1, r11);
            r11 = java.lang.Math.max(r1, r13);
            r13 = 60;
            r13 = java.lang.Math.max(r3, r13);
            r4 = java.lang.Math.max(r13, r4);
            r13 = r11;
            r11 = r2;
        L_0x09a0:
            r0 = r0 + 1;
            r8 = r42;
            r12 = r23;
            r9 = r24;
            r23 = r27;
            r24 = r10;
            r10 = r25;
            r25 = r26;
            goto L_0x05e1;
        L_0x09b2:
            r23 = r12;
            r8 = 16;
            r9 = 16;
            r10 = 1;
            r12 = 1;
            r0 = r42;
            r1 = r11;
            r2 = r13;
            r3 = r14;
            r17 = r4;
            r18 = r5;
            r4 = r6;
            r19 = r6;
            r6 = r8;
            r7 = r9;
            r9 = r42;
            r8 = r10;
            r10 = r9;
            r9 = r12;
            r0.applyMacroBlockLimits(r1, r2, r3, r4, r6, r7, r8, r9);
            r0 = r10.mFrameRateRange;
            r1 = 12;
            r1 = java.lang.Integer.valueOf(r1);
            r2 = java.lang.Integer.valueOf(r17);
            r0 = r0.intersect(r1, r2);
            r10.mFrameRateRange = r0;
            r12 = r14;
            r17 = r18;
            r29 = r19;
            r27 = r23;
            goto L_0x137d;
        L_0x09eb:
            r12 = r8;
            r10 = r24;
            r26 = r25;
            r5 = r30;
            r24 = r9;
            r39 = r27;
            r27 = r23;
            r23 = r39;
            r6 = "video/3gpp";
            r6 = r15.equalsIgnoreCase(r6);
            if (r6 == 0) goto L_0x0cf0;
        L_0x0a03:
            r6 = 11;
            r7 = 9;
            r8 = 15;
            r9 = r6;
            r11 = r7;
            r13 = 16;
            r2 = 99;
            r0 = 1485; // 0x5cd float:2.081E-42 double:7.337E-321;
            r3 = 64000; // 0xfa00 float:8.9683E-41 double:3.162E-319;
            r29 = r0;
            r14 = r23;
            r0 = r14.length;
            r23 = r4;
            r21 = r13;
            r1 = 0;
            r13 = r9;
            r9 = r2;
            r2 = r3;
            r3 = r29;
            r39 = r8;
            r8 = r6;
            r6 = r7;
            r7 = r39;
        L_0x0a29:
            if (r1 >= r0) goto L_0x0c96;
        L_0x0a2b:
            r25 = r0;
            r0 = r14[r1];
            r29 = 0;
            r30 = 0;
            r31 = 0;
            r32 = 0;
            r33 = 0;
            r34 = r13;
            r35 = r11;
            r36 = 0;
            r37 = r14;
            r14 = r0.level;
            r38 = r1;
            r1 = 1;
            if (r14 == r1) goto L_0x0bc8;
        L_0x0a48:
            r1 = 2;
            if (r14 == r1) goto L_0x0ba3;
        L_0x0a4b:
            r1 = 4;
            if (r14 == r1) goto L_0x0b7e;
        L_0x0a4e:
            r1 = 8;
            if (r14 == r1) goto L_0x0b58;
        L_0x0a52:
            r1 = 16;
            if (r14 == r1) goto L_0x0b1c;
        L_0x0a56:
            r1 = 32;
            if (r14 == r1) goto L_0x0af2;
        L_0x0a5a:
            r1 = 64;
            if (r14 == r1) goto L_0x0ac8;
        L_0x0a5e:
            r1 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
            if (r14 == r1) goto L_0x0a9e;
        L_0x0a62:
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r1.append(r10);
            r14 = r0.profile;
            r1.append(r14);
            r14 = r27;
            r1.append(r14);
            r27 = r10;
            r10 = r0.level;
            r1.append(r10);
            r1.append(r5);
            r1.append(r15);
            r1 = r1.toString();
            r10 = r26;
            android.util.Log.w(r10, r1);
            r23 = r23 | 1;
            r26 = r14;
            r1 = r29;
            r14 = r32;
            r29 = r11;
            r11 = r33;
            r39 = r31;
            r31 = r13;
            r13 = r39;
            goto L_0x0bec;
        L_0x0a9e:
            r14 = r27;
            r27 = r10;
            r10 = r26;
            r34 = 1;
            r35 = 1;
            r21 = 4;
            r31 = 60;
            r32 = 45;
            r33 = 36;
            r30 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
            r1 = r32 * r33;
            r29 = r1 * 50;
            r26 = r14;
            r1 = r29;
            r14 = r32;
            r29 = r11;
            r11 = r33;
            r39 = r31;
            r31 = r13;
            r13 = r39;
            goto L_0x0bec;
        L_0x0ac8:
            r14 = r27;
            r27 = r10;
            r10 = r26;
            r34 = 1;
            r35 = 1;
            r21 = 4;
            r31 = 60;
            r32 = 45;
            r33 = 18;
            r30 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
            r1 = r32 * r33;
            r29 = r1 * 50;
            r26 = r14;
            r1 = r29;
            r14 = r32;
            r29 = r11;
            r11 = r33;
            r39 = r31;
            r31 = r13;
            r13 = r39;
            goto L_0x0bec;
        L_0x0af2:
            r14 = r27;
            r27 = r10;
            r10 = r26;
            r34 = 1;
            r35 = 1;
            r21 = 4;
            r31 = 60;
            r32 = 22;
            r33 = 18;
            r30 = 64;
            r1 = r32 * r33;
            r29 = r1 * 50;
            r26 = r14;
            r1 = r29;
            r14 = r32;
            r29 = r11;
            r11 = r33;
            r39 = r31;
            r31 = r13;
            r13 = r39;
            goto L_0x0bec;
        L_0x0b1c:
            r14 = r27;
            r27 = r10;
            r10 = r26;
            r1 = r0.profile;
            r26 = r14;
            r14 = 1;
            if (r1 == r14) goto L_0x0b31;
        L_0x0b29:
            r1 = r0.profile;
            r14 = 4;
            if (r1 != r14) goto L_0x0b2f;
        L_0x0b2e:
            goto L_0x0b31;
        L_0x0b2f:
            r1 = 0;
            goto L_0x0b32;
        L_0x0b31:
            r1 = 1;
        L_0x0b32:
            r36 = r1;
            if (r36 != 0) goto L_0x0b3c;
        L_0x0b36:
            r34 = 1;
            r35 = 1;
            r21 = 4;
        L_0x0b3c:
            r31 = 15;
            r32 = 11;
            r33 = 9;
            r30 = 2;
            r1 = r32 * r33;
            r29 = r1 * r31;
            r1 = r29;
            r14 = r32;
            r29 = r11;
            r11 = r33;
            r39 = r31;
            r31 = r13;
            r13 = r39;
            goto L_0x0bec;
        L_0x0b58:
            r39 = r27;
            r27 = r10;
            r10 = r26;
            r26 = r39;
            r36 = 1;
            r31 = 30;
            r32 = 22;
            r33 = 18;
            r30 = 32;
            r1 = r32 * r33;
            r29 = r1 * r31;
            r1 = r29;
            r14 = r32;
            r29 = r11;
            r11 = r33;
            r39 = r31;
            r31 = r13;
            r13 = r39;
            goto L_0x0bec;
        L_0x0b7e:
            r39 = r27;
            r27 = r10;
            r10 = r26;
            r26 = r39;
            r36 = 1;
            r31 = 30;
            r32 = 22;
            r33 = 18;
            r30 = 6;
            r1 = r32 * r33;
            r29 = r1 * r31;
            r1 = r29;
            r14 = r32;
            r29 = r11;
            r11 = r33;
            r39 = r31;
            r31 = r13;
            r13 = r39;
            goto L_0x0bec;
        L_0x0ba3:
            r39 = r27;
            r27 = r10;
            r10 = r26;
            r26 = r39;
            r36 = 1;
            r31 = 30;
            r32 = 22;
            r33 = 18;
            r30 = 2;
            r1 = r32 * r33;
            r29 = r1 * 15;
            r1 = r29;
            r14 = r32;
            r29 = r11;
            r11 = r33;
            r39 = r31;
            r31 = r13;
            r13 = r39;
            goto L_0x0bec;
        L_0x0bc8:
            r39 = r27;
            r27 = r10;
            r10 = r26;
            r26 = r39;
            r36 = 1;
            r31 = 15;
            r32 = 11;
            r33 = 9;
            r30 = 1;
            r1 = r32 * r33;
            r29 = r1 * r31;
            r1 = r29;
            r14 = r32;
            r29 = r11;
            r11 = r33;
            r39 = r31;
            r31 = r13;
            r13 = r39;
        L_0x0bec:
            r32 = r7;
            r7 = r0.profile;
            r33 = r13;
            r13 = 1;
            if (r7 == r13) goto L_0x0c32;
        L_0x0bf5:
            r13 = 2;
            if (r7 == r13) goto L_0x0c32;
        L_0x0bf8:
            r13 = 4;
            if (r7 == r13) goto L_0x0c32;
        L_0x0bfb:
            r13 = 8;
            if (r7 == r13) goto L_0x0c32;
        L_0x0bff:
            r13 = 16;
            if (r7 == r13) goto L_0x0c32;
        L_0x0c03:
            r13 = 32;
            if (r7 == r13) goto L_0x0c32;
        L_0x0c07:
            r13 = 64;
            if (r7 == r13) goto L_0x0c32;
        L_0x0c0b:
            r13 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
            if (r7 == r13) goto L_0x0c32;
        L_0x0c0f:
            r13 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
            if (r7 == r13) goto L_0x0c32;
        L_0x0c13:
            r7 = new java.lang.StringBuilder;
            r7.<init>();
            r13 = r24;
            r7.append(r13);
            r13 = r0.profile;
            r7.append(r13);
            r7.append(r5);
            r7.append(r15);
            r7 = r7.toString();
            android.util.Log.w(r10, r7);
            r23 = r23 | 1;
            goto L_0x0c33;
        L_0x0c33:
            if (r36 == 0) goto L_0x0c3e;
        L_0x0c35:
            r34 = 11;
            r35 = 9;
            r7 = r34;
            r13 = r35;
            goto L_0x0c45;
        L_0x0c3e:
            r7 = 1;
            r12.mAllowMbOverride = r7;
            r7 = r34;
            r13 = r35;
        L_0x0c45:
            r23 = r23 & -5;
            r34 = r13;
            r12 = (long) r1;
            r3 = java.lang.Math.max(r12, r3);
            r12 = r14 * r11;
            r9 = java.lang.Math.max(r12, r9);
            r12 = 64000; // 0xfa00 float:8.9683E-41 double:3.162E-319;
            r12 = r12 * r30;
            r2 = java.lang.Math.max(r12, r2);
            r8 = java.lang.Math.max(r14, r8);
            r6 = java.lang.Math.max(r11, r6);
            r12 = r32;
            r13 = r33;
            r12 = java.lang.Math.max(r13, r12);
            r32 = r12;
            r12 = r31;
            r12 = java.lang.Math.min(r7, r12);
            r31 = r11;
            r11 = r29;
            r29 = r0;
            r0 = r34;
            r11 = java.lang.Math.min(r0, r11);
            r1 = r38 + 1;
            r13 = r12;
            r0 = r25;
            r7 = r32;
            r14 = r37;
            r12 = r42;
            r39 = r26;
            r26 = r10;
            r10 = r27;
            r27 = r39;
            goto L_0x0a29;
        L_0x0c96:
            r32 = r7;
            r12 = r13;
            r37 = r14;
            r13 = r42;
            r0 = r13.mAllowMbOverride;
            if (r0 != 0) goto L_0x0cb9;
        L_0x0ca1:
            r0 = new android.util.Rational;
            r1 = 11;
            r5 = 9;
            r0.<init>(r1, r5);
            r1 = new android.util.Rational;
            r5 = 11;
            r7 = 9;
            r1.<init>(r5, r7);
            r0 = android.util.Range.create(r0, r1);
            r13.mBlockAspectRatioRange = r0;
        L_0x0cb9:
            r10 = 16;
            r14 = 16;
            r0 = r42;
            r1 = r12;
            r16 = r2;
            r2 = r11;
            r29 = r3;
            r3 = r8;
            r4 = r6;
            r5 = r9;
            r17 = r6;
            r6 = r29;
            r18 = r8;
            r8 = r10;
            r19 = r9;
            r9 = r14;
            r10 = r21;
            r14 = r11;
            r11 = r21;
            r0.applyMacroBlockLimits(r1, r2, r3, r4, r5, r6, r8, r9, r10, r11);
            r0 = java.lang.Integer.valueOf(r32);
            r11 = r28;
            r0 = android.util.Range.create(r11, r0);
            r13.mFrameRateRange = r0;
            r17 = r16;
            r12 = r19;
            r16 = r23;
            r27 = r37;
            goto L_0x137d;
        L_0x0cf0:
            r13 = r12;
            r37 = r23;
            r10 = r26;
            r11 = r28;
            r6 = "video/x-vnd.on2.vp8";
            r6 = r15.equalsIgnoreCase(r6);
            if (r6 == 0) goto L_0x0d99;
        L_0x0d00:
            r12 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
            r17 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
            r14 = 100000000; // 0x5f5e100 float:2.3122341E-35 double:4.94065646E-316;
            r9 = r37;
            r0 = r9.length;
            r16 = r4;
            r1 = 0;
        L_0x0d0f:
            if (r1 >= r0) goto L_0x0d73;
        L_0x0d11:
            r2 = r9[r1];
            r3 = r2.level;
            r4 = 1;
            if (r3 == r4) goto L_0x0d41;
        L_0x0d18:
            r4 = 2;
            if (r3 == r4) goto L_0x0d41;
        L_0x0d1b:
            r4 = 4;
            if (r3 == r4) goto L_0x0d41;
        L_0x0d1e:
            r4 = 8;
            if (r3 == r4) goto L_0x0d41;
        L_0x0d22:
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r6 = r21;
            r3.append(r6);
            r4 = r2.level;
            r3.append(r4);
            r3.append(r5);
            r3.append(r15);
            r3 = r3.toString();
            android.util.Log.w(r10, r3);
            r16 = r16 | 1;
            goto L_0x0d44;
        L_0x0d41:
            r6 = r21;
        L_0x0d44:
            r3 = r2.profile;
            r4 = 1;
            if (r3 == r4) goto L_0x0d68;
        L_0x0d49:
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r7 = r24;
            r3.append(r7);
            r4 = r2.profile;
            r3.append(r4);
            r3.append(r5);
            r3.append(r15);
            r3 = r3.toString();
            android.util.Log.w(r10, r3);
            r16 = r16 | 1;
            goto L_0x0d6a;
        L_0x0d68:
            r7 = r24;
        L_0x0d6a:
            r16 = r16 & -5;
            r1 = r1 + 1;
            r21 = r6;
            r24 = r7;
            goto L_0x0d0f;
        L_0x0d73:
            r10 = 16;
            r1 = 32767; // 0x7fff float:4.5916E-41 double:1.6189E-319;
            r2 = 32767; // 0x7fff float:4.5916E-41 double:1.6189E-319;
            r6 = 16;
            r7 = 16;
            r8 = 1;
            r19 = 1;
            r0 = r42;
            r3 = r12;
            r4 = r17;
            r21 = r12;
            r12 = r9;
            r9 = r19;
            r0.applyMacroBlockLimits(r1, r2, r3, r4, r6, r7, r8, r9);
            r28 = r11;
            r27 = r12;
            r29 = r17;
            r12 = r21;
            r17 = r14;
            goto L_0x137d;
        L_0x0d99:
            r6 = r21;
            r7 = r24;
            r12 = r37;
            r8 = "video/x-vnd.on2.vp9";
            r8 = r15.equalsIgnoreCase(r8);
            if (r8 == 0) goto L_0x0fff;
        L_0x0da8:
            r0 = 829440; // 0xca800 float:1.162293E-39 double:4.09798E-318;
            r2 = 36864; // 0x9000 float:5.1657E-41 double:1.8213E-319;
            r3 = 200000; // 0x30d40 float:2.8026E-40 double:9.8813E-319;
            r8 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
            r9 = r12.length;
            r16 = r4;
            r14 = r8;
            r8 = r3;
            r3 = r0;
            r0 = 0;
        L_0x0dba:
            if (r0 >= r9) goto L_0x0fcd;
        L_0x0dbc:
            r1 = r12[r0];
            r23 = 0;
            r18 = 0;
            r21 = 0;
            r25 = 0;
            r26 = r9;
            r9 = r1.level;
            r13 = 1;
            if (r9 == r13) goto L_0x0f4f;
        L_0x0dcd:
            r13 = 2;
            if (r9 == r13) goto L_0x0f36;
        L_0x0dd0:
            switch(r9) {
                case 4: goto L_0x0f1d;
                case 8: goto L_0x0f04;
                case 16: goto L_0x0eea;
                case 32: goto L_0x0ed1;
                case 64: goto L_0x0eb8;
                case 128: goto L_0x0e9f;
                case 256: goto L_0x0e85;
                case 512: goto L_0x0e6b;
                case 1024: goto L_0x0e51;
                case 2048: goto L_0x0e37;
                case 4096: goto L_0x0e1b;
                case 8192: goto L_0x0dff;
                default: goto L_0x0dd3;
            };
        L_0x0dd3:
            r9 = new java.lang.StringBuilder;
            r9.<init>();
            r9.append(r6);
            r13 = r1.level;
            r9.append(r13);
            r9.append(r5);
            r9.append(r15);
            r9 = r9.toString();
            android.util.Log.w(r10, r9);
            r16 = r16 | 1;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0dff:
            r23 = 4706009088; // 0x118800000 float:3.3087225E-24 double:2.3250774194E-314;
            r18 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r21 = 480000; // 0x75300 float:6.72623E-40 double:2.371515E-318;
            r25 = 16832; // 0x41c0 float:2.3587E-41 double:8.316E-320;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0e1b:
            r23 = 2353004544; // 0x8c400000 float:-1.4791142E-31 double:1.1625387097E-314;
            r18 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r21 = 240000; // 0x3a980 float:3.36312E-40 double:1.18576E-318;
            r25 = 16832; // 0x41c0 float:2.3587E-41 double:8.316E-320;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0e37:
            r23 = 1176502272; // 0x46200000 float:10240.0 double:5.81269355E-315;
            r18 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r21 = 180000; // 0x2bf20 float:2.52234E-40 double:8.8932E-319;
            r25 = 16832; // 0x41c0 float:2.3587E-41 double:8.316E-320;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0e51:
            r23 = 1176502272; // 0x46200000 float:10240.0 double:5.81269355E-315;
            r18 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r21 = 180000; // 0x2bf20 float:2.52234E-40 double:8.8932E-319;
            r25 = 8384; // 0x20c0 float:1.1748E-41 double:4.142E-320;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0e6b:
            r23 = 588251136; // 0x23100000 float:7.806256E-18 double:2.906346774E-315;
            r18 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r21 = 120000; // 0x1d4c0 float:1.68156E-40 double:5.9288E-319;
            r25 = 8384; // 0x20c0 float:1.1748E-41 double:4.142E-320;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0e85:
            r23 = 311951360; // 0x12980000 float:9.592549E-28 double:1.5412445E-315;
            r18 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r21 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
            r25 = 8384; // 0x20c0 float:1.1748E-41 double:4.142E-320;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0e9f:
            r23 = 160432128; // 0x9900000 float:3.466674E-33 double:7.9264003E-316;
            r18 = 2228224; // 0x220000 float:3.122407E-39 double:1.100889E-317;
            r21 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
            r25 = 4160; // 0x1040 float:5.83E-42 double:2.0553E-320;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0eb8:
            r23 = 83558400; // 0x4fb0000 float:5.9009816E-36 double:4.1283335E-316;
            r18 = 2228224; // 0x220000 float:3.122407E-39 double:1.100889E-317;
            r21 = 18000; // 0x4650 float:2.5223E-41 double:8.893E-320;
            r25 = 4160; // 0x1040 float:5.83E-42 double:2.0553E-320;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0ed1:
            r23 = 36864000; // 0x2328000 float:1.3114109E-37 double:1.8213236E-316;
            r18 = 983040; // 0xf0000 float:1.377532E-39 double:4.856863E-318;
            r21 = 12000; // 0x2ee0 float:1.6816E-41 double:5.929E-320;
            r25 = 2752; // 0xac0 float:3.856E-42 double:1.3597E-320;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0eea:
            r23 = 20736000; // 0x13c6800 float:3.4604763E-38 double:1.0244945E-316;
            r18 = 552960; // 0x87000 float:7.74862E-40 double:2.731985E-318;
            r21 = 7200; // 0x1c20 float:1.009E-41 double:3.5573E-320;
            r25 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0f04:
            r23 = 9216000; // 0x8ca000 float:1.2914367E-38 double:4.553309E-317;
            r18 = 245760; // 0x3c000 float:3.44383E-40 double:1.214216E-318;
            r21 = 3600; // 0xe10 float:5.045E-42 double:1.7786E-320;
            r25 = 1344; // 0x540 float:1.883E-42 double:6.64E-321;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0f1d:
            r23 = 4608000; // 0x465000 float:6.457183E-39 double:2.2766545E-317;
            r18 = 122880; // 0x1e000 float:1.72192E-40 double:6.0711E-319;
            r21 = 1800; // 0x708 float:2.522E-42 double:8.893E-321;
            r25 = 960; // 0x3c0 float:1.345E-42 double:4.743E-321;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0f36:
            r23 = 2764800; // 0x2a3000 float:3.87431E-39 double:1.3659927E-317;
            r18 = 73728; // 0x12000 float:1.03315E-40 double:3.64265E-319;
            r21 = 800; // 0x320 float:1.121E-42 double:3.953E-321;
            r25 = 768; // 0x300 float:1.076E-42 double:3.794E-321;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
            goto L_0x0f67;
        L_0x0f4f:
            r23 = 829440; // 0xca800 float:1.162293E-39 double:4.09798E-318;
            r18 = 36864; // 0x9000 float:5.1657E-41 double:1.8213E-319;
            r21 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            r25 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
            r28 = r11;
            r27 = r12;
            r9 = r18;
            r13 = r21;
            r11 = r23;
            r21 = r6;
            r6 = r25;
        L_0x0f67:
            r18 = r0;
            r0 = r1.profile;
            r23 = r6;
            r6 = 1;
            if (r0 == r6) goto L_0x0fa8;
        L_0x0f70:
            r6 = 2;
            if (r0 == r6) goto L_0x0fa8;
        L_0x0f73:
            r6 = 4;
            if (r0 == r6) goto L_0x0fa8;
        L_0x0f76:
            r6 = 8;
            if (r0 == r6) goto L_0x0fa8;
        L_0x0f7a:
            r6 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            if (r0 == r6) goto L_0x0fa8;
        L_0x0f7e:
            r6 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            if (r0 == r6) goto L_0x0fa8;
        L_0x0f82:
            r6 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
            if (r0 == r6) goto L_0x0fa8;
        L_0x0f86:
            r6 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
            if (r0 == r6) goto L_0x0fa8;
        L_0x0f8b:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r0.append(r7);
            r6 = r1.profile;
            r0.append(r6);
            r0.append(r5);
            r0.append(r15);
            r0 = r0.toString();
            android.util.Log.w(r10, r0);
            r16 = r16 | 1;
            goto L_0x0fa9;
        L_0x0fa9:
            r16 = r16 & -5;
            r3 = java.lang.Math.max(r11, r3);
            r2 = java.lang.Math.max(r9, r2);
            r0 = r13 * 1000;
            r8 = java.lang.Math.max(r0, r8);
            r0 = r23;
            r14 = java.lang.Math.max(r0, r14);
            r0 = r18 + 1;
            r13 = r42;
            r6 = r21;
            r9 = r26;
            r12 = r27;
            r11 = r28;
            goto L_0x0dba;
        L_0x0fcd:
            r28 = r11;
            r27 = r12;
            r10 = 8;
            r0 = 8;
            r11 = android.media.Utils.divUp(r14, r0);
            r0 = 64;
            r12 = android.media.Utils.divUp(r2, r0);
            r0 = 64;
            r17 = android.media.Utils.divUp(r3, r0);
            r6 = 8;
            r7 = 8;
            r9 = 1;
            r13 = 1;
            r0 = r42;
            r1 = r11;
            r2 = r11;
            r3 = r12;
            r4 = r17;
            r19 = r8;
            r8 = r9;
            r9 = r13;
            r0.applyMacroBlockLimits(r1, r2, r3, r4, r6, r7, r8, r9);
            r29 = r17;
            r17 = r19;
            goto L_0x137d;
        L_0x0fff:
            r21 = r6;
            r28 = r11;
            r27 = r12;
            r6 = "video/hevc";
            r6 = r15.equalsIgnoreCase(r6);
            if (r6 == 0) goto L_0x117a;
        L_0x100e:
            r2 = 576; // 0x240 float:8.07E-43 double:2.846E-321;
            r6 = r2 * 15;
            r0 = (long) r6;
            r3 = 128000; // 0x1f400 float:1.79366E-40 double:6.32404E-319;
            r11 = r27;
            r6 = r11.length;
            r13 = r0;
            r12 = r2;
            r9 = r3;
            r16 = r4;
            r0 = 0;
        L_0x101f:
            if (r0 >= r6) goto L_0x115a;
        L_0x1021:
            r1 = r11[r0];
            r2 = 0;
            r4 = 0;
            r8 = 0;
            r17 = r2;
            r2 = r1.level;
            r3 = 1;
            if (r2 == r3) goto L_0x10fc;
        L_0x102e:
            r3 = 2;
            if (r2 == r3) goto L_0x10fc;
        L_0x1031:
            switch(r2) {
                case 4: goto L_0x10f4;
                case 8: goto L_0x10f4;
                case 16: goto L_0x10ec;
                case 32: goto L_0x10ec;
                case 64: goto L_0x10e4;
                case 128: goto L_0x10e4;
                case 256: goto L_0x10da;
                case 512: goto L_0x10da;
                case 1024: goto L_0x10d3;
                case 2048: goto L_0x10cc;
                case 4096: goto L_0x10c5;
                case 8192: goto L_0x10bd;
                case 16384: goto L_0x10b6;
                case 32768: goto L_0x10ae;
                case 65536: goto L_0x10a6;
                case 131072: goto L_0x109e;
                case 262144: goto L_0x1095;
                case 524288: goto L_0x108c;
                case 1048576: goto L_0x1083;
                case 2097152: goto L_0x107a;
                case 4194304: goto L_0x1071;
                case 8388608: goto L_0x1068;
                case 16777216: goto L_0x105f;
                case 33554432: goto L_0x1056;
                default: goto L_0x1034;
            };
        L_0x1034:
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = r21;
            r2.append(r3);
            r3 = r1.level;
            r2.append(r3);
            r2.append(r5);
            r2.append(r15);
            r2 = r2.toString();
            android.util.Log.w(r10, r2);
            r16 = r16 | 1;
            r2 = r17;
            goto L_0x1103;
        L_0x1056:
            r2 = 4638144666238189568; // 0x405e000000000000 float:0.0 double:120.0;
            r4 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r8 = 800000; // 0xc3500 float:1.121039E-39 double:3.952525E-318;
            goto L_0x1103;
        L_0x105f:
            r2 = 4638144666238189568; // 0x405e000000000000 float:0.0 double:120.0;
            r4 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r8 = 240000; // 0x3a980 float:3.36312E-40 double:1.18576E-318;
            goto L_0x1103;
        L_0x1068:
            r2 = 4633641066610819072; // 0x404e000000000000 float:0.0 double:60.0;
            r4 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r8 = 480000; // 0x75300 float:6.72623E-40 double:2.371515E-318;
            goto L_0x1103;
        L_0x1071:
            r2 = 4633641066610819072; // 0x404e000000000000 float:0.0 double:60.0;
            r4 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r8 = 120000; // 0x1d4c0 float:1.68156E-40 double:5.9288E-319;
            goto L_0x1103;
        L_0x107a:
            r2 = 4629137466983448576; // 0x403e000000000000 float:0.0 double:30.0;
            r4 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r8 = 240000; // 0x3a980 float:3.36312E-40 double:1.18576E-318;
            goto L_0x1103;
        L_0x1083:
            r2 = 4629137466983448576; // 0x403e000000000000 float:0.0 double:30.0;
            r4 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r8 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
            goto L_0x1103;
        L_0x108c:
            r2 = 4638144666238189568; // 0x405e000000000000 float:0.0 double:120.0;
            r4 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r8 = 240000; // 0x3a980 float:3.36312E-40 double:1.18576E-318;
            goto L_0x1103;
        L_0x1095:
            r2 = 4638144666238189568; // 0x405e000000000000 float:0.0 double:120.0;
            r4 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r8 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
            goto L_0x1103;
        L_0x109e:
            r2 = 4633641066610819072; // 0x404e000000000000 float:0.0 double:60.0;
            r4 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r8 = 160000; // 0x27100 float:2.24208E-40 double:7.90505E-319;
            goto L_0x1103;
        L_0x10a6:
            r2 = 4633641066610819072; // 0x404e000000000000 float:0.0 double:60.0;
            r4 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r8 = 40000; // 0x9c40 float:5.6052E-41 double:1.97626E-319;
            goto L_0x1103;
        L_0x10ae:
            r2 = 4629137466983448576; // 0x403e000000000000 float:0.0 double:30.0;
            r4 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r8 = 100000; // 0x186a0 float:1.4013E-40 double:4.94066E-319;
            goto L_0x1103;
        L_0x10b6:
            r2 = 4629137466983448576; // 0x403e000000000000 float:0.0 double:30.0;
            r4 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r8 = 25000; // 0x61a8 float:3.5032E-41 double:1.23516E-319;
            goto L_0x1103;
        L_0x10bd:
            r2 = 4633641066610819072; // 0x404e000000000000 float:0.0 double:60.0;
            r4 = 2228224; // 0x220000 float:3.122407E-39 double:1.100889E-317;
            r8 = 50000; // 0xc350 float:7.0065E-41 double:2.47033E-319;
            goto L_0x1103;
        L_0x10c5:
            r2 = 4633641066610819072; // 0x404e000000000000 float:0.0 double:60.0;
            r4 = 2228224; // 0x220000 float:3.122407E-39 double:1.100889E-317;
            r8 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
            goto L_0x1103;
        L_0x10cc:
            r2 = 4629137466983448576; // 0x403e000000000000 float:0.0 double:30.0;
            r4 = 2228224; // 0x220000 float:3.122407E-39 double:1.100889E-317;
            r8 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
            goto L_0x1103;
        L_0x10d3:
            r2 = 4629137466983448576; // 0x403e000000000000 float:0.0 double:30.0;
            r4 = 2228224; // 0x220000 float:3.122407E-39 double:1.100889E-317;
            r8 = 12000; // 0x2ee0 float:1.6816E-41 double:5.929E-320;
            goto L_0x1103;
        L_0x10da:
            r2 = 4629946707541491712; // 0x4040e00000000000 float:0.0 double:33.75;
            r4 = 983040; // 0xf0000 float:1.377532E-39 double:4.856863E-318;
            r8 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
            goto L_0x1103;
        L_0x10e4:
            r2 = 4629137466983448576; // 0x403e000000000000 float:0.0 double:30.0;
            r4 = 552960; // 0x87000 float:7.74862E-40 double:2.731985E-318;
            r8 = 6000; // 0x1770 float:8.408E-42 double:2.9644E-320;
            goto L_0x1103;
        L_0x10ec:
            r2 = 4629137466983448576; // 0x403e000000000000 float:0.0 double:30.0;
            r4 = 245760; // 0x3c000 float:3.44383E-40 double:1.214216E-318;
            r8 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;
            goto L_0x1103;
        L_0x10f4:
            r2 = 4629137466983448576; // 0x403e000000000000 float:0.0 double:30.0;
            r4 = 122880; // 0x1e000 float:1.72192E-40 double:6.0711E-319;
            r8 = 1500; // 0x5dc float:2.102E-42 double:7.41E-321;
            goto L_0x1103;
        L_0x10fc:
            r2 = 4624633867356078080; // 0x402e000000000000 float:0.0 double:15.0;
            r4 = 36864; // 0x9000 float:5.1657E-41 double:1.8213E-319;
            r8 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        L_0x1103:
            r17 = r6;
            r6 = r1.profile;
            r27 = r11;
            r11 = 1;
            if (r6 == r11) goto L_0x1137;
        L_0x110c:
            r11 = 2;
            if (r6 == r11) goto L_0x1137;
        L_0x110f:
            r11 = 4;
            if (r6 == r11) goto L_0x1137;
        L_0x1112:
            r11 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            if (r6 == r11) goto L_0x1137;
        L_0x1116:
            r11 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            if (r6 == r11) goto L_0x1137;
        L_0x111a:
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r6.append(r7);
            r11 = r1.profile;
            r6.append(r11);
            r6.append(r5);
            r6.append(r15);
            r6 = r6.toString();
            android.util.Log.w(r10, r6);
            r16 = r16 | 1;
            goto L_0x1138;
        L_0x1138:
            r4 = r4 >> 6;
            r16 = r16 & -5;
            r24 = r7;
            r6 = (double) r4;
            r6 = r6 * r2;
            r6 = (int) r6;
            r6 = (long) r6;
            r13 = java.lang.Math.max(r6, r13);
            r12 = java.lang.Math.max(r4, r12);
            r6 = r8 * 1000;
            r9 = java.lang.Math.max(r6, r9);
            r0 = r0 + 1;
            r6 = r17;
            r7 = r24;
            r11 = r27;
            goto L_0x101f;
        L_0x115a:
            r27 = r11;
            r0 = r12 * 8;
            r0 = (double) r0;
            r0 = java.lang.Math.sqrt(r0);
            r10 = (int) r0;
            r6 = 8;
            r7 = 8;
            r8 = 1;
            r11 = 1;
            r0 = r42;
            r1 = r10;
            r2 = r10;
            r3 = r12;
            r4 = r13;
            r17 = r9;
            r9 = r11;
            r0.applyMacroBlockLimits(r1, r2, r3, r4, r6, r7, r8, r9);
            r29 = r13;
            goto L_0x137d;
        L_0x117a:
            r24 = r7;
            r6 = r21;
            r7 = "video/av01";
            r7 = r15.equalsIgnoreCase(r7);
            if (r7 == 0) goto L_0x135e;
        L_0x1187:
            r0 = 829440; // 0xca800 float:1.162293E-39 double:4.09798E-318;
            r2 = 36864; // 0x9000 float:5.1657E-41 double:1.8213E-319;
            r3 = 200000; // 0x30d40 float:2.8026E-40 double:9.8813E-319;
            r7 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
            r11 = r27;
            r8 = r11.length;
            r13 = r3;
            r16 = r4;
            r12 = r7;
            r3 = r0;
            r0 = 0;
        L_0x119b:
            if (r0 >= r8) goto L_0x1332;
        L_0x119d:
            r1 = r11[r0];
            r25 = 0;
            r7 = 0;
            r9 = 0;
            r14 = 0;
            r18 = r7;
            r7 = r1.level;
            r20 = r8;
            r8 = 1;
            if (r7 == r8) goto L_0x12cf;
        L_0x11ad:
            r8 = 2;
            if (r7 == r8) goto L_0x12bd;
        L_0x11b0:
            switch(r7) {
                case 4: goto L_0x12bd;
                case 8: goto L_0x12bd;
                case 16: goto L_0x12ab;
                case 32: goto L_0x1299;
                case 64: goto L_0x1299;
                case 128: goto L_0x1299;
                case 256: goto L_0x1288;
                case 512: goto L_0x1276;
                case 1024: goto L_0x1276;
                case 2048: goto L_0x1276;
                case 4096: goto L_0x1264;
                case 8192: goto L_0x1251;
                case 16384: goto L_0x123e;
                case 32768: goto L_0x122b;
                case 65536: goto L_0x1218;
                case 131072: goto L_0x1203;
                case 262144: goto L_0x11ee;
                case 524288: goto L_0x11d9;
                default: goto L_0x11b3;
            };
        L_0x11b3:
            r7 = new java.lang.StringBuilder;
            r7.<init>();
            r7.append(r6);
            r8 = r1.level;
            r7.append(r8);
            r7.append(r5);
            r7.append(r15);
            r7 = r7.toString();
            android.util.Log.w(r10, r7);
            r16 = r16 | 1;
            r21 = r6;
            r6 = r14;
            r7 = r25;
            r14 = r9;
            r9 = r18;
            goto L_0x12e0;
        L_0x11d9:
            r25 = 4706009088; // 0x118800000 float:3.3087225E-24 double:2.3250774194E-314;
            r7 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r9 = 160000; // 0x27100 float:2.24208E-40 double:7.90505E-319;
            r14 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x11ee:
            r25 = 4379443200; // 0x105090000 float:6.441709E-36 double:2.163732433E-314;
            r7 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r9 = 160000; // 0x27100 float:2.24208E-40 double:7.90505E-319;
            r14 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x1203:
            r25 = 2189721600; // 0x82848000 float:-1.9469125E-37 double:1.0818662165E-314;
            r7 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r9 = 100000; // 0x186a0 float:1.4013E-40 double:4.94066E-319;
            r14 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x1218:
            r25 = 1176502272; // 0x46200000 float:10240.0 double:5.81269355E-315;
            r7 = 35651584; // 0x2200000 float:1.1754944E-37 double:1.7614223E-316;
            r9 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
            r14 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x122b:
            r25 = 1176502272; // 0x46200000 float:10240.0 double:5.81269355E-315;
            r7 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r9 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
            r14 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x123e:
            r25 = 1094860800; // 0x41424000 float:12.140625 double:5.409331083E-315;
            r7 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r9 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
            r14 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x1251:
            r25 = 547430400; // 0x20a12000 float:2.7295637E-19 double:2.70466554E-315;
            r7 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r9 = 40000; // 0x9c40 float:5.6052E-41 double:1.97626E-319;
            r14 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x1264:
            r25 = 273715200; // 0x10509000 float:4.11317E-29 double:1.35233277E-315;
            r7 = 8912896; // 0x880000 float:1.2489627E-38 double:4.4035557E-317;
            r9 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
            r14 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x1276:
            r25 = 155713536; // 0x9480000 float:2.4074124E-33 double:7.69327087E-316;
            r7 = 2359296; // 0x240000 float:3.306078E-39 double:1.165647E-317;
            r9 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
            r14 = 6144; // 0x1800 float:8.61E-42 double:3.0355E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x1288:
            r25 = 77856768; // 0x4a40000 float:3.8556215E-36 double:3.84663544E-316;
            r7 = 2359296; // 0x240000 float:3.306078E-39 double:1.165647E-317;
            r9 = 12000; // 0x2ee0 float:1.6816E-41 double:5.929E-320;
            r14 = 6144; // 0x1800 float:8.61E-42 double:3.0355E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x1299:
            r25 = 39938400; // 0x2616960 float:1.656063E-37 double:1.97321914E-316;
            r7 = 1065024; // 0x104040 float:1.492416E-39 double:5.26192E-318;
            r9 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
            r14 = 5504; // 0x1580 float:7.713E-42 double:2.7193E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x12ab:
            r25 = 24969600; // 0x17d0180 float:4.6469837E-38 double:1.23366216E-316;
            r7 = 665856; // 0xa2900 float:9.33063E-40 double:3.289766E-318;
            r9 = 6000; // 0x1770 float:8.408E-42 double:2.9644E-320;
            r14 = 4352; // 0x1100 float:6.098E-42 double:2.15E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x12bd:
            r25 = 10454400; // 0x9f8580 float:1.4649735E-38 double:5.16516E-317;
            r7 = 278784; // 0x44100 float:3.9066E-40 double:1.377376E-318;
            r9 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;
            r14 = 2816; // 0xb00 float:3.946E-42 double:1.3913E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
            goto L_0x12e0;
        L_0x12cf:
            r25 = 5529600; // 0x546000 float:7.74862E-39 double:2.7319854E-317;
            r7 = 147456; // 0x24000 float:2.0663E-40 double:7.2853E-319;
            r9 = 1500; // 0x5dc float:2.102E-42 double:7.41E-321;
            r14 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
            r21 = r6;
            r6 = r14;
            r14 = r9;
            r9 = r7;
            r7 = r25;
        L_0x12e0:
            r27 = r11;
            r11 = r1.profile;
            r18 = r0;
            r0 = 1;
            if (r11 == r0) goto L_0x1313;
        L_0x12e9:
            r0 = 2;
            if (r11 == r0) goto L_0x1313;
        L_0x12ec:
            r0 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            if (r11 == r0) goto L_0x1313;
        L_0x12f0:
            r0 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            if (r11 == r0) goto L_0x1313;
        L_0x12f4:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r11 = r24;
            r0.append(r11);
            r11 = r1.profile;
            r0.append(r11);
            r0.append(r5);
            r0.append(r15);
            r0 = r0.toString();
            android.util.Log.w(r10, r0);
            r16 = r16 | 1;
            goto L_0x1314;
        L_0x1314:
            r16 = r16 & -5;
            r3 = java.lang.Math.max(r7, r3);
            r2 = java.lang.Math.max(r9, r2);
            r0 = r14 * 1000;
            r13 = java.lang.Math.max(r0, r13);
            r12 = java.lang.Math.max(r6, r12);
            r0 = r18 + 1;
            r8 = r20;
            r6 = r21;
            r11 = r27;
            goto L_0x119b;
        L_0x1332:
            r27 = r11;
            r10 = 8;
            r0 = 8;
            r11 = android.media.Utils.divUp(r12, r0);
            r0 = 64;
            r14 = android.media.Utils.divUp(r2, r0);
            r0 = 64;
            r17 = android.media.Utils.divUp(r3, r0);
            r6 = 8;
            r7 = 8;
            r8 = 1;
            r9 = 1;
            r0 = r42;
            r1 = r11;
            r2 = r11;
            r3 = r14;
            r4 = r17;
            r0.applyMacroBlockLimits(r1, r2, r3, r4, r6, r7, r8, r9);
            r12 = r14;
            r29 = r17;
            r17 = r13;
            goto L_0x137d;
        L_0x135e:
            r5 = new java.lang.StringBuilder;
            r5.<init>();
            r6 = "Unsupported mime ";
            r5.append(r6);
            r5.append(r15);
            r5 = r5.toString();
            android.util.Log.w(r10, r5);
            r5 = 64000; // 0xfa00 float:8.9683E-41 double:3.162E-319;
            r3 = 2;
            r16 = r4 | 2;
            r29 = r0;
            r12 = r2;
            r17 = r5;
        L_0x137d:
            r0 = java.lang.Integer.valueOf(r17);
            r1 = r28;
            r0 = android.util.Range.create(r1, r0);
            r1 = r42;
            r1.mBitrateRange = r0;
            r0 = r1.mParent;
            r2 = r0.mError;
            r2 = r2 | r16;
            r0.mError = r2;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaCodecInfo$VideoCapabilities.applyLevelLimits():void");
        }
    }

    MediaCodecInfo(String name, String canonicalName, int flags, CodecCapabilities[] caps) {
        this.mName = name;
        this.mCanonicalName = canonicalName;
        this.mFlags = flags;
        for (CodecCapabilities c : caps) {
            this.mCaps.put(c.getMimeType(), c);
        }
    }

    public final String getName() {
        return this.mName;
    }

    public final String getCanonicalName() {
        return this.mCanonicalName;
    }

    public final boolean isAlias() {
        return this.mName.equals(this.mCanonicalName) ^ 1;
    }

    public final boolean isEncoder() {
        return (this.mFlags & 1) != 0;
    }

    public final boolean isVendor() {
        return (this.mFlags & 2) != 0;
    }

    public final boolean isSoftwareOnly() {
        return (this.mFlags & 4) != 0;
    }

    public final boolean isHardwareAccelerated() {
        return (this.mFlags & 8) != 0;
    }

    public final String[] getSupportedTypes() {
        Set<String> typeSet = this.mCaps.keySet();
        String[] types = (String[]) typeSet.toArray(new String[typeSet.size()]);
        Arrays.sort(types);
        return types;
    }

    private static int checkPowerOfTwo(int value, String message) {
        if (((value - 1) & value) == 0) {
            return value;
        }
        throw new IllegalArgumentException(message);
    }

    static {
        Integer valueOf = Integer.valueOf(1);
        POSITIVE_INTEGERS = Range.create(valueOf, Integer.valueOf(Integer.MAX_VALUE));
        SIZE_RANGE = Range.create(valueOf, Integer.valueOf(32768));
        Integer valueOf2 = Integer.valueOf(0);
        FRAME_RATE_RANGE = Range.create(valueOf2, Integer.valueOf(960));
        BITRATE_RANGE = Range.create(valueOf2, Integer.valueOf(500000000));
    }

    public final CodecCapabilities getCapabilitiesForType(String type) {
        CodecCapabilities caps = (CodecCapabilities) this.mCaps.get(type);
        if (caps != null) {
            return caps.dup();
        }
        throw new IllegalArgumentException("codec does not support type");
    }

    public MediaCodecInfo makeRegular() {
        ArrayList<CodecCapabilities> caps = new ArrayList();
        for (CodecCapabilities c : this.mCaps.values()) {
            if (c.isRegular()) {
                caps.add(c);
            }
        }
        if (caps.size() == 0) {
            return null;
        }
        if (caps.size() == this.mCaps.size()) {
            return this;
        }
        return new MediaCodecInfo(this.mName, this.mCanonicalName, this.mFlags, (CodecCapabilities[]) caps.toArray(new CodecCapabilities[caps.size()]));
    }
}
