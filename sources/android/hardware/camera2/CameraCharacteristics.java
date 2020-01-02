package android.hardware.camera2;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.PublicKey;
import android.hardware.camera2.impl.SyntheticKey;
import android.hardware.camera2.params.BlackLevelPattern;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.hardware.camera2.params.HighSpeedVideoConfiguration;
import android.hardware.camera2.params.MandatoryStreamCombination;
import android.hardware.camera2.params.RecommendedStreamConfiguration;
import android.hardware.camera2.params.RecommendedStreamConfigurationMap;
import android.hardware.camera2.params.ReprocessFormatsMap;
import android.hardware.camera2.params.StreamConfiguration;
import android.hardware.camera2.params.StreamConfigurationDuration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.utils.ArrayUtils;
import android.hardware.camera2.utils.TypeReference;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import android.util.SizeF;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CameraCharacteristics extends CameraMetadata<Key<?>> {
    @PublicKey
    public static final Key<int[]> COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES = new Key("android.colorCorrection.availableAberrationModes", int[].class);
    @PublicKey
    public static final Key<int[]> CONTROL_AE_AVAILABLE_ANTIBANDING_MODES = new Key("android.control.aeAvailableAntibandingModes", int[].class);
    @PublicKey
    public static final Key<int[]> CONTROL_AE_AVAILABLE_MODES = new Key("android.control.aeAvailableModes", int[].class);
    @PublicKey
    public static final Key<Range<Integer>[]> CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES = new Key("android.control.aeAvailableTargetFpsRanges", new TypeReference<Range<Integer>[]>() {
    });
    @PublicKey
    public static final Key<Range<Integer>> CONTROL_AE_COMPENSATION_RANGE = new Key("android.control.aeCompensationRange", new TypeReference<Range<Integer>>() {
    });
    @PublicKey
    public static final Key<Rational> CONTROL_AE_COMPENSATION_STEP = new Key("android.control.aeCompensationStep", Rational.class);
    @PublicKey
    public static final Key<Boolean> CONTROL_AE_LOCK_AVAILABLE = new Key("android.control.aeLockAvailable", Boolean.TYPE);
    @PublicKey
    public static final Key<int[]> CONTROL_AF_AVAILABLE_MODES = new Key("android.control.afAvailableModes", int[].class);
    @PublicKey
    public static final Key<int[]> CONTROL_AVAILABLE_EFFECTS = new Key("android.control.availableEffects", int[].class);
    public static final Key<HighSpeedVideoConfiguration[]> CONTROL_AVAILABLE_HIGH_SPEED_VIDEO_CONFIGURATIONS = new Key("android.control.availableHighSpeedVideoConfigurations", HighSpeedVideoConfiguration[].class);
    @PublicKey
    public static final Key<int[]> CONTROL_AVAILABLE_MODES = new Key("android.control.availableModes", int[].class);
    @PublicKey
    public static final Key<int[]> CONTROL_AVAILABLE_SCENE_MODES = new Key("android.control.availableSceneModes", int[].class);
    @PublicKey
    public static final Key<int[]> CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES = new Key("android.control.availableVideoStabilizationModes", int[].class);
    @PublicKey
    public static final Key<int[]> CONTROL_AWB_AVAILABLE_MODES = new Key("android.control.awbAvailableModes", int[].class);
    @PublicKey
    public static final Key<Boolean> CONTROL_AWB_LOCK_AVAILABLE = new Key("android.control.awbLockAvailable", Boolean.TYPE);
    public static final Key<int[]> CONTROL_MAX_REGIONS = new Key("android.control.maxRegions", int[].class);
    @PublicKey
    @SyntheticKey
    public static final Key<Integer> CONTROL_MAX_REGIONS_AE = new Key("android.control.maxRegionsAe", Integer.TYPE);
    @PublicKey
    @SyntheticKey
    public static final Key<Integer> CONTROL_MAX_REGIONS_AF = new Key("android.control.maxRegionsAf", Integer.TYPE);
    @PublicKey
    @SyntheticKey
    public static final Key<Integer> CONTROL_MAX_REGIONS_AWB = new Key("android.control.maxRegionsAwb", Integer.TYPE);
    @PublicKey
    public static final Key<Range<Integer>> CONTROL_POST_RAW_SENSITIVITY_BOOST_RANGE = new Key("android.control.postRawSensitivityBoostRange", new TypeReference<Range<Integer>>() {
    });
    public static final Key<StreamConfigurationDuration[]> DEPTH_AVAILABLE_DEPTH_MIN_FRAME_DURATIONS = new Key("android.depth.availableDepthMinFrameDurations", StreamConfigurationDuration[].class);
    public static final Key<StreamConfigurationDuration[]> DEPTH_AVAILABLE_DEPTH_STALL_DURATIONS = new Key("android.depth.availableDepthStallDurations", StreamConfigurationDuration[].class);
    public static final Key<StreamConfiguration[]> DEPTH_AVAILABLE_DEPTH_STREAM_CONFIGURATIONS = new Key("android.depth.availableDepthStreamConfigurations", StreamConfiguration[].class);
    public static final Key<StreamConfigurationDuration[]> DEPTH_AVAILABLE_DYNAMIC_DEPTH_MIN_FRAME_DURATIONS = new Key("android.depth.availableDynamicDepthMinFrameDurations", StreamConfigurationDuration[].class);
    public static final Key<StreamConfigurationDuration[]> DEPTH_AVAILABLE_DYNAMIC_DEPTH_STALL_DURATIONS = new Key("android.depth.availableDynamicDepthStallDurations", StreamConfigurationDuration[].class);
    public static final Key<StreamConfiguration[]> DEPTH_AVAILABLE_DYNAMIC_DEPTH_STREAM_CONFIGURATIONS = new Key("android.depth.availableDynamicDepthStreamConfigurations", StreamConfiguration[].class);
    public static final Key<RecommendedStreamConfiguration[]> DEPTH_AVAILABLE_RECOMMENDED_DEPTH_STREAM_CONFIGURATIONS = new Key("android.depth.availableRecommendedDepthStreamConfigurations", RecommendedStreamConfiguration[].class);
    @PublicKey
    public static final Key<Boolean> DEPTH_DEPTH_IS_EXCLUSIVE = new Key("android.depth.depthIsExclusive", Boolean.TYPE);
    @PublicKey
    public static final Key<int[]> DISTORTION_CORRECTION_AVAILABLE_MODES = new Key("android.distortionCorrection.availableModes", int[].class);
    @PublicKey
    public static final Key<int[]> EDGE_AVAILABLE_EDGE_MODES = new Key("android.edge.availableEdgeModes", int[].class);
    @PublicKey
    public static final Key<Boolean> FLASH_INFO_AVAILABLE = new Key("android.flash.info.available", Boolean.TYPE);
    public static final Key<StreamConfigurationDuration[]> HEIC_AVAILABLE_HEIC_MIN_FRAME_DURATIONS = new Key("android.heic.availableHeicMinFrameDurations", StreamConfigurationDuration[].class);
    public static final Key<StreamConfigurationDuration[]> HEIC_AVAILABLE_HEIC_STALL_DURATIONS = new Key("android.heic.availableHeicStallDurations", StreamConfigurationDuration[].class);
    public static final Key<StreamConfiguration[]> HEIC_AVAILABLE_HEIC_STREAM_CONFIGURATIONS = new Key("android.heic.availableHeicStreamConfigurations", StreamConfiguration[].class);
    @PublicKey
    public static final Key<int[]> HOT_PIXEL_AVAILABLE_HOT_PIXEL_MODES = new Key("android.hotPixel.availableHotPixelModes", int[].class);
    @PublicKey
    public static final Key<Integer> INFO_SUPPORTED_HARDWARE_LEVEL = new Key("android.info.supportedHardwareLevel", Integer.TYPE);
    @PublicKey
    public static final Key<String> INFO_VERSION = new Key("android.info.version", String.class);
    @PublicKey
    public static final Key<Size[]> JPEG_AVAILABLE_THUMBNAIL_SIZES = new Key("android.jpeg.availableThumbnailSizes", Size[].class);
    public static final Key<int[]> LED_AVAILABLE_LEDS = new Key("android.led.availableLeds", int[].class);
    @PublicKey
    public static final Key<float[]> LENS_DISTORTION = new Key("android.lens.distortion", float[].class);
    @PublicKey
    public static final Key<Integer> LENS_FACING = new Key("android.lens.facing", Integer.TYPE);
    @PublicKey
    public static final Key<float[]> LENS_INFO_AVAILABLE_APERTURES = new Key("android.lens.info.availableApertures", float[].class);
    @PublicKey
    public static final Key<float[]> LENS_INFO_AVAILABLE_FILTER_DENSITIES = new Key("android.lens.info.availableFilterDensities", float[].class);
    @PublicKey
    public static final Key<float[]> LENS_INFO_AVAILABLE_FOCAL_LENGTHS = new Key("android.lens.info.availableFocalLengths", float[].class);
    @PublicKey
    public static final Key<int[]> LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION = new Key("android.lens.info.availableOpticalStabilization", int[].class);
    @PublicKey
    public static final Key<Integer> LENS_INFO_FOCUS_DISTANCE_CALIBRATION = new Key("android.lens.info.focusDistanceCalibration", Integer.TYPE);
    @PublicKey
    public static final Key<Float> LENS_INFO_HYPERFOCAL_DISTANCE = new Key("android.lens.info.hyperfocalDistance", Float.TYPE);
    @PublicKey
    public static final Key<Float> LENS_INFO_MINIMUM_FOCUS_DISTANCE = new Key("android.lens.info.minimumFocusDistance", Float.TYPE);
    public static final Key<Size> LENS_INFO_SHADING_MAP_SIZE = new Key("android.lens.info.shadingMapSize", Size.class);
    @PublicKey
    public static final Key<float[]> LENS_INTRINSIC_CALIBRATION = new Key("android.lens.intrinsicCalibration", float[].class);
    @PublicKey
    public static final Key<Integer> LENS_POSE_REFERENCE = new Key("android.lens.poseReference", Integer.TYPE);
    @PublicKey
    public static final Key<float[]> LENS_POSE_ROTATION = new Key("android.lens.poseRotation", float[].class);
    @PublicKey
    public static final Key<float[]> LENS_POSE_TRANSLATION = new Key("android.lens.poseTranslation", float[].class);
    @PublicKey
    @Deprecated
    public static final Key<float[]> LENS_RADIAL_DISTORTION = new Key("android.lens.radialDistortion", float[].class);
    public static final Key<byte[]> LOGICAL_MULTI_CAMERA_PHYSICAL_IDS = new Key("android.logicalMultiCamera.physicalIds", byte[].class);
    @PublicKey
    public static final Key<Integer> LOGICAL_MULTI_CAMERA_SENSOR_SYNC_TYPE = new Key("android.logicalMultiCamera.sensorSyncType", Integer.TYPE);
    @PublicKey
    public static final Key<int[]> NOISE_REDUCTION_AVAILABLE_NOISE_REDUCTION_MODES = new Key("android.noiseReduction.availableNoiseReductionModes", int[].class);
    @Deprecated
    public static final Key<Byte> QUIRKS_USE_PARTIAL_RESULT = new Key("android.quirks.usePartialResult", Byte.TYPE);
    @PublicKey
    public static final Key<Integer> REPROCESS_MAX_CAPTURE_STALL = new Key("android.reprocess.maxCaptureStall", Integer.TYPE);
    @PublicKey
    public static final Key<int[]> REQUEST_AVAILABLE_CAPABILITIES = new Key("android.request.availableCapabilities", int[].class);
    public static final Key<int[]> REQUEST_AVAILABLE_CHARACTERISTICS_KEYS = new Key("android.request.availableCharacteristicsKeys", int[].class);
    public static final Key<int[]> REQUEST_AVAILABLE_PHYSICAL_CAMERA_REQUEST_KEYS = new Key("android.request.availablePhysicalCameraRequestKeys", int[].class);
    public static final Key<int[]> REQUEST_AVAILABLE_REQUEST_KEYS = new Key("android.request.availableRequestKeys", int[].class);
    public static final Key<int[]> REQUEST_AVAILABLE_RESULT_KEYS = new Key("android.request.availableResultKeys", int[].class);
    public static final Key<int[]> REQUEST_AVAILABLE_SESSION_KEYS = new Key("android.request.availableSessionKeys", int[].class);
    public static final Key<int[]> REQUEST_CHARACTERISTIC_KEYS_NEEDING_PERMISSION = new Key("android.request.characteristicKeysNeedingPermission", int[].class);
    @PublicKey
    public static final Key<Integer> REQUEST_MAX_NUM_INPUT_STREAMS = new Key("android.request.maxNumInputStreams", Integer.TYPE);
    @PublicKey
    @SyntheticKey
    public static final Key<Integer> REQUEST_MAX_NUM_OUTPUT_PROC = new Key("android.request.maxNumOutputProc", Integer.TYPE);
    @PublicKey
    @SyntheticKey
    public static final Key<Integer> REQUEST_MAX_NUM_OUTPUT_PROC_STALLING = new Key("android.request.maxNumOutputProcStalling", Integer.TYPE);
    @PublicKey
    @SyntheticKey
    public static final Key<Integer> REQUEST_MAX_NUM_OUTPUT_RAW = new Key("android.request.maxNumOutputRaw", Integer.TYPE);
    public static final Key<int[]> REQUEST_MAX_NUM_OUTPUT_STREAMS = new Key("android.request.maxNumOutputStreams", int[].class);
    @PublicKey
    public static final Key<Integer> REQUEST_PARTIAL_RESULT_COUNT = new Key("android.request.partialResultCount", Integer.TYPE);
    @PublicKey
    public static final Key<Byte> REQUEST_PIPELINE_MAX_DEPTH = new Key("android.request.pipelineMaxDepth", Byte.TYPE);
    @Deprecated
    public static final Key<int[]> SCALER_AVAILABLE_FORMATS = new Key("android.scaler.availableFormats", int[].class);
    public static final Key<ReprocessFormatsMap> SCALER_AVAILABLE_INPUT_OUTPUT_FORMATS_MAP = new Key("android.scaler.availableInputOutputFormatsMap", ReprocessFormatsMap.class);
    @Deprecated
    public static final Key<long[]> SCALER_AVAILABLE_JPEG_MIN_DURATIONS = new Key("android.scaler.availableJpegMinDurations", long[].class);
    @Deprecated
    public static final Key<Size[]> SCALER_AVAILABLE_JPEG_SIZES = new Key("android.scaler.availableJpegSizes", Size[].class);
    @PublicKey
    public static final Key<Float> SCALER_AVAILABLE_MAX_DIGITAL_ZOOM = new Key("android.scaler.availableMaxDigitalZoom", Float.TYPE);
    public static final Key<StreamConfigurationDuration[]> SCALER_AVAILABLE_MIN_FRAME_DURATIONS = new Key("android.scaler.availableMinFrameDurations", StreamConfigurationDuration[].class);
    @Deprecated
    public static final Key<long[]> SCALER_AVAILABLE_PROCESSED_MIN_DURATIONS = new Key("android.scaler.availableProcessedMinDurations", long[].class);
    @Deprecated
    public static final Key<Size[]> SCALER_AVAILABLE_PROCESSED_SIZES = new Key("android.scaler.availableProcessedSizes", Size[].class);
    public static final Key<ReprocessFormatsMap> SCALER_AVAILABLE_RECOMMENDED_INPUT_OUTPUT_FORMATS_MAP = new Key("android.scaler.availableRecommendedInputOutputFormatsMap", ReprocessFormatsMap.class);
    public static final Key<RecommendedStreamConfiguration[]> SCALER_AVAILABLE_RECOMMENDED_STREAM_CONFIGURATIONS = new Key("android.scaler.availableRecommendedStreamConfigurations", RecommendedStreamConfiguration[].class);
    public static final Key<StreamConfigurationDuration[]> SCALER_AVAILABLE_STALL_DURATIONS = new Key("android.scaler.availableStallDurations", StreamConfigurationDuration[].class);
    public static final Key<StreamConfiguration[]> SCALER_AVAILABLE_STREAM_CONFIGURATIONS = new Key("android.scaler.availableStreamConfigurations", StreamConfiguration[].class);
    @PublicKey
    public static final Key<Integer> SCALER_CROPPING_TYPE = new Key("android.scaler.croppingType", Integer.TYPE);
    @PublicKey
    @SyntheticKey
    public static final Key<MandatoryStreamCombination[]> SCALER_MANDATORY_STREAM_COMBINATIONS = new Key("android.scaler.mandatoryStreamCombinations", MandatoryStreamCombination[].class);
    @PublicKey
    @SyntheticKey
    public static final Key<StreamConfigurationMap> SCALER_STREAM_CONFIGURATION_MAP = new Key("android.scaler.streamConfigurationMap", StreamConfigurationMap.class);
    @PublicKey
    public static final Key<int[]> SENSOR_AVAILABLE_TEST_PATTERN_MODES = new Key("android.sensor.availableTestPatternModes", int[].class);
    @PublicKey
    public static final Key<BlackLevelPattern> SENSOR_BLACK_LEVEL_PATTERN = new Key("android.sensor.blackLevelPattern", BlackLevelPattern.class);
    @PublicKey
    public static final Key<ColorSpaceTransform> SENSOR_CALIBRATION_TRANSFORM1 = new Key("android.sensor.calibrationTransform1", ColorSpaceTransform.class);
    @PublicKey
    public static final Key<ColorSpaceTransform> SENSOR_CALIBRATION_TRANSFORM2 = new Key("android.sensor.calibrationTransform2", ColorSpaceTransform.class);
    @PublicKey
    public static final Key<ColorSpaceTransform> SENSOR_COLOR_TRANSFORM1 = new Key("android.sensor.colorTransform1", ColorSpaceTransform.class);
    @PublicKey
    public static final Key<ColorSpaceTransform> SENSOR_COLOR_TRANSFORM2 = new Key("android.sensor.colorTransform2", ColorSpaceTransform.class);
    @PublicKey
    public static final Key<ColorSpaceTransform> SENSOR_FORWARD_MATRIX1 = new Key("android.sensor.forwardMatrix1", ColorSpaceTransform.class);
    @PublicKey
    public static final Key<ColorSpaceTransform> SENSOR_FORWARD_MATRIX2 = new Key("android.sensor.forwardMatrix2", ColorSpaceTransform.class);
    @PublicKey
    public static final Key<Rect> SENSOR_INFO_ACTIVE_ARRAY_SIZE = new Key("android.sensor.info.activeArraySize", Rect.class);
    @PublicKey
    public static final Key<Integer> SENSOR_INFO_COLOR_FILTER_ARRANGEMENT = new Key("android.sensor.info.colorFilterArrangement", Integer.TYPE);
    @PublicKey
    public static final Key<Range<Long>> SENSOR_INFO_EXPOSURE_TIME_RANGE = new Key("android.sensor.info.exposureTimeRange", new TypeReference<Range<Long>>() {
    });
    @PublicKey
    public static final Key<Boolean> SENSOR_INFO_LENS_SHADING_APPLIED = new Key("android.sensor.info.lensShadingApplied", Boolean.TYPE);
    @PublicKey
    public static final Key<Long> SENSOR_INFO_MAX_FRAME_DURATION = new Key("android.sensor.info.maxFrameDuration", Long.TYPE);
    @PublicKey
    public static final Key<SizeF> SENSOR_INFO_PHYSICAL_SIZE = new Key("android.sensor.info.physicalSize", SizeF.class);
    @PublicKey
    public static final Key<Size> SENSOR_INFO_PIXEL_ARRAY_SIZE = new Key("android.sensor.info.pixelArraySize", Size.class);
    @PublicKey
    public static final Key<Rect> SENSOR_INFO_PRE_CORRECTION_ACTIVE_ARRAY_SIZE = new Key("android.sensor.info.preCorrectionActiveArraySize", Rect.class);
    @PublicKey
    public static final Key<Range<Integer>> SENSOR_INFO_SENSITIVITY_RANGE = new Key("android.sensor.info.sensitivityRange", new TypeReference<Range<Integer>>() {
    });
    @PublicKey
    public static final Key<Integer> SENSOR_INFO_TIMESTAMP_SOURCE = new Key("android.sensor.info.timestampSource", Integer.TYPE);
    @PublicKey
    public static final Key<Integer> SENSOR_INFO_WHITE_LEVEL = new Key("android.sensor.info.whiteLevel", Integer.TYPE);
    @PublicKey
    public static final Key<Integer> SENSOR_MAX_ANALOG_SENSITIVITY = new Key("android.sensor.maxAnalogSensitivity", Integer.TYPE);
    @PublicKey
    public static final Key<Rect[]> SENSOR_OPTICAL_BLACK_REGIONS = new Key("android.sensor.opticalBlackRegions", Rect[].class);
    @PublicKey
    public static final Key<Integer> SENSOR_ORIENTATION = new Key(Sensor.STRING_TYPE_ORIENTATION, Integer.TYPE);
    @PublicKey
    public static final Key<Integer> SENSOR_REFERENCE_ILLUMINANT1 = new Key("android.sensor.referenceIlluminant1", Integer.TYPE);
    @PublicKey
    public static final Key<Byte> SENSOR_REFERENCE_ILLUMINANT2 = new Key("android.sensor.referenceIlluminant2", Byte.TYPE);
    @PublicKey
    public static final Key<int[]> SHADING_AVAILABLE_MODES = new Key("android.shading.availableModes", int[].class);
    @PublicKey
    public static final Key<int[]> STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES = new Key("android.statistics.info.availableFaceDetectModes", int[].class);
    @PublicKey
    public static final Key<boolean[]> STATISTICS_INFO_AVAILABLE_HOT_PIXEL_MAP_MODES = new Key("android.statistics.info.availableHotPixelMapModes", boolean[].class);
    @PublicKey
    public static final Key<int[]> STATISTICS_INFO_AVAILABLE_LENS_SHADING_MAP_MODES = new Key("android.statistics.info.availableLensShadingMapModes", int[].class);
    @PublicKey
    public static final Key<int[]> STATISTICS_INFO_AVAILABLE_OIS_DATA_MODES = new Key("android.statistics.info.availableOisDataModes", int[].class);
    @PublicKey
    public static final Key<Integer> STATISTICS_INFO_MAX_FACE_COUNT = new Key("android.statistics.info.maxFaceCount", Integer.TYPE);
    @PublicKey
    public static final Key<Integer> SYNC_MAX_LATENCY = new Key("android.sync.maxLatency", Integer.TYPE);
    @PublicKey
    public static final Key<int[]> TONEMAP_AVAILABLE_TONE_MAP_MODES = new Key("android.tonemap.availableToneMapModes", int[].class);
    @PublicKey
    public static final Key<Integer> TONEMAP_MAX_CURVE_POINTS = new Key("android.tonemap.maxCurvePoints", Integer.TYPE);
    private List<android.hardware.camera2.CaptureRequest.Key<?>> mAvailablePhysicalRequestKeys;
    private List<android.hardware.camera2.CaptureRequest.Key<?>> mAvailableRequestKeys;
    private List<android.hardware.camera2.CaptureResult.Key<?>> mAvailableResultKeys;
    private List<android.hardware.camera2.CaptureRequest.Key<?>> mAvailableSessionKeys;
    private List<Key<?>> mKeys;
    private List<Key<?>> mKeysNeedingPermission;
    @UnsupportedAppUsage
    private final CameraMetadataNative mProperties;
    private ArrayList<RecommendedStreamConfigurationMap> mRecommendedConfigurations;

    public static final class Key<T> {
        private final android.hardware.camera2.impl.CameraMetadataNative.Key<T> mKey;

        @UnsupportedAppUsage
        public Key(String name, Class<T> type, long vendorId) {
            this.mKey = new android.hardware.camera2.impl.CameraMetadataNative.Key(name, (Class) type, vendorId);
        }

        public Key(String name, String fallbackName, Class<T> type) {
            this.mKey = new android.hardware.camera2.impl.CameraMetadataNative.Key(name, fallbackName, (Class) type);
        }

        public Key(String name, Class<T> type) {
            this.mKey = new android.hardware.camera2.impl.CameraMetadataNative.Key(name, (Class) type);
        }

        @UnsupportedAppUsage
        public Key(String name, TypeReference<T> typeReference) {
            this.mKey = new android.hardware.camera2.impl.CameraMetadataNative.Key(name, (TypeReference) typeReference);
        }

        public String getName() {
            return this.mKey.getName();
        }

        public long getVendorId() {
            return this.mKey.getVendorId();
        }

        public final int hashCode() {
            return this.mKey.hashCode();
        }

        public final boolean equals(Object o) {
            return (o instanceof Key) && ((Key) o).mKey.equals(this.mKey);
        }

        public String toString() {
            return String.format("CameraCharacteristics.Key(%s)", new Object[]{this.mKey.getName()});
        }

        @UnsupportedAppUsage
        public android.hardware.camera2.impl.CameraMetadataNative.Key<T> getNativeKey() {
            return this.mKey;
        }

        private Key(android.hardware.camera2.impl.CameraMetadataNative.Key<?> nativeKey) {
            this.mKey = nativeKey;
        }
    }

    public CameraCharacteristics(CameraMetadataNative properties) {
        this.mProperties = CameraMetadataNative.move(properties);
        setNativeInstance(this.mProperties);
    }

    public CameraMetadataNative getNativeCopy() {
        return new CameraMetadataNative(this.mProperties);
    }

    public <T> T get(Key<T> key) {
        return this.mProperties.get((Key) key);
    }

    /* Access modifiers changed, original: protected */
    public <T> T getProtected(Key<?> key) {
        return this.mProperties.get((Key) key);
    }

    /* Access modifiers changed, original: protected */
    public Class<Key<?>> getKeyClass() {
        return Key.class;
    }

    public List<Key<?>> getKeys() {
        List list = this.mKeys;
        if (list != null) {
            return list;
        }
        int[] filterTags = (int[]) get(REQUEST_AVAILABLE_CHARACTERISTICS_KEYS);
        if (filterTags != null) {
            this.mKeys = Collections.unmodifiableList(getKeys(getClass(), getKeyClass(), this, filterTags, true));
            return this.mKeys;
        }
        throw new AssertionError("android.request.availableCharacteristicsKeys must be non-null in the characteristics");
    }

    public List<Key<?>> getKeysNeedingPermission() {
        if (this.mKeysNeedingPermission == null) {
            Class<Key<?>> crKeyTyped = Key.class;
            int[] filterTags = (int[]) get(REQUEST_CHARACTERISTIC_KEYS_NEEDING_PERMISSION);
            if (filterTags == null) {
                this.mKeysNeedingPermission = Collections.unmodifiableList(new ArrayList());
                return this.mKeysNeedingPermission;
            }
            this.mKeysNeedingPermission = getAvailableKeyList(CameraCharacteristics.class, crKeyTyped, filterTags, false);
        }
        return this.mKeysNeedingPermission;
    }

    public RecommendedStreamConfigurationMap getRecommendedStreamConfigurationMap(int usecase) {
        if ((usecase < 0 || usecase > 6) && (usecase < 24 || usecase >= 32)) {
            throw new IllegalArgumentException(String.format("Invalid use case: %d", new Object[]{Integer.valueOf(usecase)}));
        }
        if (this.mRecommendedConfigurations == null) {
            this.mRecommendedConfigurations = this.mProperties.getRecommendedStreamConfigurations();
            if (this.mRecommendedConfigurations == null) {
                return null;
            }
        }
        return (RecommendedStreamConfigurationMap) this.mRecommendedConfigurations.get(usecase);
    }

    public List<android.hardware.camera2.CaptureRequest.Key<?>> getAvailableSessionKeys() {
        if (this.mAvailableSessionKeys == null) {
            Class<android.hardware.camera2.CaptureRequest.Key<?>> crKeyTyped = android.hardware.camera2.CaptureRequest.Key.class;
            int[] filterTags = (int[]) get(REQUEST_AVAILABLE_SESSION_KEYS);
            if (filterTags == null) {
                return null;
            }
            this.mAvailableSessionKeys = getAvailableKeyList(CaptureRequest.class, crKeyTyped, filterTags, false);
        }
        return this.mAvailableSessionKeys;
    }

    public List<android.hardware.camera2.CaptureRequest.Key<?>> getAvailablePhysicalCameraRequestKeys() {
        if (this.mAvailablePhysicalRequestKeys == null) {
            Class<android.hardware.camera2.CaptureRequest.Key<?>> crKeyTyped = android.hardware.camera2.CaptureRequest.Key.class;
            int[] filterTags = (int[]) get(REQUEST_AVAILABLE_PHYSICAL_CAMERA_REQUEST_KEYS);
            if (filterTags == null) {
                return null;
            }
            this.mAvailablePhysicalRequestKeys = getAvailableKeyList(CaptureRequest.class, crKeyTyped, filterTags, false);
        }
        return this.mAvailablePhysicalRequestKeys;
    }

    public List<android.hardware.camera2.CaptureRequest.Key<?>> getAvailableCaptureRequestKeys() {
        if (this.mAvailableRequestKeys == null) {
            Class<android.hardware.camera2.CaptureRequest.Key<?>> crKeyTyped = android.hardware.camera2.CaptureRequest.Key.class;
            int[] filterTags = (int[]) get(REQUEST_AVAILABLE_REQUEST_KEYS);
            if (filterTags != null) {
                this.mAvailableRequestKeys = getAvailableKeyList(CaptureRequest.class, crKeyTyped, filterTags, true);
            } else {
                throw new AssertionError("android.request.availableRequestKeys must be non-null in the characteristics");
            }
        }
        return this.mAvailableRequestKeys;
    }

    public List<android.hardware.camera2.CaptureResult.Key<?>> getAvailableCaptureResultKeys() {
        if (this.mAvailableResultKeys == null) {
            Class<android.hardware.camera2.CaptureResult.Key<?>> crKeyTyped = android.hardware.camera2.CaptureResult.Key.class;
            int[] filterTags = (int[]) get(REQUEST_AVAILABLE_RESULT_KEYS);
            if (filterTags != null) {
                this.mAvailableResultKeys = getAvailableKeyList(CaptureResult.class, crKeyTyped, filterTags, true);
            } else {
                throw new AssertionError("android.request.availableResultKeys must be non-null in the characteristics");
            }
        }
        return this.mAvailableResultKeys;
    }

    private <TKey> List<TKey> getAvailableKeyList(Class<?> metadataClass, Class<TKey> keyClass, int[] filterTags, boolean includeSynthetic) {
        if (metadataClass.equals(CameraMetadata.class)) {
            throw new AssertionError("metadataClass must be a strict subclass of CameraMetadata");
        } else if (CameraMetadata.class.isAssignableFrom(metadataClass)) {
            return Collections.unmodifiableList(getKeys(metadataClass, keyClass, null, filterTags, includeSynthetic));
        } else {
            throw new AssertionError("metadataClass must be a subclass of CameraMetadata");
        }
    }

    public Set<String> getPhysicalCameraIds() {
        int[] availableCapabilities = (int[]) get(REQUEST_AVAILABLE_CAPABILITIES);
        if (availableCapabilities == null) {
            throw new AssertionError("android.request.availableCapabilities must be non-null in the characteristics");
        } else if (!ArrayUtils.contains(availableCapabilities, 11)) {
            return Collections.emptySet();
        } else {
            try {
                return Collections.unmodifiableSet(new HashSet(Arrays.asList(new String((byte[]) get(LOGICAL_MULTI_CAMERA_PHYSICAL_IDS), "UTF-8").split("\u0000"))));
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError("android.logicalCam.physicalIds must be UTF-8 string");
            }
        }
    }
}
