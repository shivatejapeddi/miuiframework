package android.hardware.camera2.params;

import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.legacy.LegacyCameraDevice;
import android.hardware.camera2.utils.HashCodeHelpers;
import android.hardware.camera2.utils.SurfaceUtils;
import android.media.ImageReader;
import android.media.MediaCodec;
import android.media.MediaRecorder;
import android.renderscript.Allocation;
import android.util.Range;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public final class StreamConfigurationMap {
    private static final long DURATION_20FPS_NS = 50000000;
    private static final int DURATION_MIN_FRAME = 0;
    private static final int DURATION_STALL = 1;
    private static final int HAL_DATASPACE_DEPTH = 4096;
    private static final int HAL_DATASPACE_DYNAMIC_DEPTH = 4098;
    private static final int HAL_DATASPACE_HEIF = 4099;
    private static final int HAL_DATASPACE_RANGE_SHIFT = 27;
    private static final int HAL_DATASPACE_STANDARD_SHIFT = 16;
    private static final int HAL_DATASPACE_TRANSFER_SHIFT = 22;
    private static final int HAL_DATASPACE_UNKNOWN = 0;
    private static final int HAL_DATASPACE_V0_JFIF = 146931712;
    private static final int HAL_PIXEL_FORMAT_BLOB = 33;
    private static final int HAL_PIXEL_FORMAT_IMPLEMENTATION_DEFINED = 34;
    private static final int HAL_PIXEL_FORMAT_RAW10 = 37;
    private static final int HAL_PIXEL_FORMAT_RAW12 = 38;
    private static final int HAL_PIXEL_FORMAT_RAW16 = 32;
    private static final int HAL_PIXEL_FORMAT_RAW_OPAQUE = 36;
    private static final int HAL_PIXEL_FORMAT_Y16 = 540422489;
    private static final int HAL_PIXEL_FORMAT_YCbCr_420_888 = 35;
    private static final String TAG = "StreamConfigurationMap";
    private final SparseIntArray mAllOutputFormats;
    private final StreamConfiguration[] mConfigurations;
    private final StreamConfiguration[] mDepthConfigurations;
    private final StreamConfigurationDuration[] mDepthMinFrameDurations;
    private final SparseIntArray mDepthOutputFormats;
    private final StreamConfigurationDuration[] mDepthStallDurations;
    private final StreamConfiguration[] mDynamicDepthConfigurations;
    private final StreamConfigurationDuration[] mDynamicDepthMinFrameDurations;
    private final SparseIntArray mDynamicDepthOutputFormats;
    private final StreamConfigurationDuration[] mDynamicDepthStallDurations;
    private final StreamConfiguration[] mHeicConfigurations;
    private final StreamConfigurationDuration[] mHeicMinFrameDurations;
    private final SparseIntArray mHeicOutputFormats;
    private final StreamConfigurationDuration[] mHeicStallDurations;
    private final SparseIntArray mHighResOutputFormats;
    private final HighSpeedVideoConfiguration[] mHighSpeedVideoConfigurations;
    private final HashMap<Range<Integer>, Integer> mHighSpeedVideoFpsRangeMap;
    private final HashMap<Size, Integer> mHighSpeedVideoSizeMap;
    private final SparseIntArray mInputFormats;
    private final ReprocessFormatsMap mInputOutputFormatsMap;
    private final boolean mListHighResolution;
    private final StreamConfigurationDuration[] mMinFrameDurations;
    private final SparseIntArray mOutputFormats;
    private final StreamConfigurationDuration[] mStallDurations;

    public StreamConfigurationMap(StreamConfiguration[] configurations, StreamConfigurationDuration[] minFrameDurations, StreamConfigurationDuration[] stallDurations, StreamConfiguration[] depthConfigurations, StreamConfigurationDuration[] depthMinFrameDurations, StreamConfigurationDuration[] depthStallDurations, StreamConfiguration[] dynamicDepthConfigurations, StreamConfigurationDuration[] dynamicDepthMinFrameDurations, StreamConfigurationDuration[] dynamicDepthStallDurations, StreamConfiguration[] heicConfigurations, StreamConfigurationDuration[] heicMinFrameDurations, StreamConfigurationDuration[] heicStallDurations, HighSpeedVideoConfiguration[] highSpeedVideoConfigurations, ReprocessFormatsMap inputOutputFormatsMap, boolean listHighResolution) {
        this(configurations, minFrameDurations, stallDurations, depthConfigurations, depthMinFrameDurations, depthStallDurations, dynamicDepthConfigurations, dynamicDepthMinFrameDurations, dynamicDepthStallDurations, heicConfigurations, heicMinFrameDurations, heicStallDurations, highSpeedVideoConfigurations, inputOutputFormatsMap, listHighResolution, true);
    }

    public StreamConfigurationMap(StreamConfiguration[] configurations, StreamConfigurationDuration[] minFrameDurations, StreamConfigurationDuration[] stallDurations, StreamConfiguration[] depthConfigurations, StreamConfigurationDuration[] depthMinFrameDurations, StreamConfigurationDuration[] depthStallDurations, StreamConfiguration[] dynamicDepthConfigurations, StreamConfigurationDuration[] dynamicDepthMinFrameDurations, StreamConfigurationDuration[] dynamicDepthStallDurations, StreamConfiguration[] heicConfigurations, StreamConfigurationDuration[] heicMinFrameDurations, StreamConfigurationDuration[] heicStallDurations, HighSpeedVideoConfiguration[] highSpeedVideoConfigurations, ReprocessFormatsMap inputOutputFormatsMap, boolean listHighResolution, boolean enforceImplementationDefined) {
        StreamConfiguration[] streamConfigurationArr = configurations;
        StreamConfiguration[] streamConfigurationArr2 = depthConfigurations;
        StreamConfiguration[] streamConfigurationArr3 = dynamicDepthConfigurations;
        StreamConfiguration[] streamConfigurationArr4 = heicConfigurations;
        HighSpeedVideoConfiguration[] highSpeedVideoConfigurationArr = highSpeedVideoConfigurations;
        this.mOutputFormats = new SparseIntArray();
        this.mHighResOutputFormats = new SparseIntArray();
        this.mAllOutputFormats = new SparseIntArray();
        this.mInputFormats = new SparseIntArray();
        this.mDepthOutputFormats = new SparseIntArray();
        this.mDynamicDepthOutputFormats = new SparseIntArray();
        this.mHeicOutputFormats = new SparseIntArray();
        this.mHighSpeedVideoSizeMap = new HashMap();
        this.mHighSpeedVideoFpsRangeMap = new HashMap();
        if (streamConfigurationArr == null && streamConfigurationArr2 == null && streamConfigurationArr4 == null) {
            throw new NullPointerException("At least one of color/depth/heic configurations must not be null");
        }
        StreamConfigurationDuration[] streamConfigurationDurationArr;
        StreamConfiguration config;
        StreamConfiguration[] streamConfigurationArr5;
        int i;
        if (streamConfigurationArr == null) {
            this.mConfigurations = new StreamConfiguration[0];
            this.mMinFrameDurations = new StreamConfigurationDuration[0];
            this.mStallDurations = new StreamConfigurationDuration[0];
            StreamConfigurationDuration[] streamConfigurationDurationArr2 = minFrameDurations;
            StreamConfigurationDuration[] streamConfigurationDurationArr3 = stallDurations;
        } else {
            this.mConfigurations = (StreamConfiguration[]) Preconditions.checkArrayElementsNotNull(streamConfigurationArr, "configurations");
            this.mMinFrameDurations = (StreamConfigurationDuration[]) Preconditions.checkArrayElementsNotNull(minFrameDurations, "minFrameDurations");
            this.mStallDurations = (StreamConfigurationDuration[]) Preconditions.checkArrayElementsNotNull(stallDurations, "stallDurations");
        }
        this.mListHighResolution = listHighResolution;
        if (streamConfigurationArr2 == null) {
            this.mDepthConfigurations = new StreamConfiguration[0];
            this.mDepthMinFrameDurations = new StreamConfigurationDuration[0];
            this.mDepthStallDurations = new StreamConfigurationDuration[0];
            StreamConfigurationDuration[] streamConfigurationDurationArr4 = depthMinFrameDurations;
            StreamConfigurationDuration[] streamConfigurationDurationArr5 = depthStallDurations;
        } else {
            this.mDepthConfigurations = (StreamConfiguration[]) Preconditions.checkArrayElementsNotNull(streamConfigurationArr2, "depthConfigurations");
            this.mDepthMinFrameDurations = (StreamConfigurationDuration[]) Preconditions.checkArrayElementsNotNull(depthMinFrameDurations, "depthMinFrameDurations");
            this.mDepthStallDurations = (StreamConfigurationDuration[]) Preconditions.checkArrayElementsNotNull(depthStallDurations, "depthStallDurations");
        }
        if (streamConfigurationArr3 == null) {
            this.mDynamicDepthConfigurations = new StreamConfiguration[0];
            this.mDynamicDepthMinFrameDurations = new StreamConfigurationDuration[0];
            this.mDynamicDepthStallDurations = new StreamConfigurationDuration[0];
            StreamConfigurationDuration[] streamConfigurationDurationArr6 = dynamicDepthMinFrameDurations;
            StreamConfigurationDuration[] streamConfigurationDurationArr7 = dynamicDepthStallDurations;
        } else {
            this.mDynamicDepthConfigurations = (StreamConfiguration[]) Preconditions.checkArrayElementsNotNull(streamConfigurationArr3, "dynamicDepthConfigurations");
            this.mDynamicDepthMinFrameDurations = (StreamConfigurationDuration[]) Preconditions.checkArrayElementsNotNull(dynamicDepthMinFrameDurations, "dynamicDepthMinFrameDurations");
            this.mDynamicDepthStallDurations = (StreamConfigurationDuration[]) Preconditions.checkArrayElementsNotNull(dynamicDepthStallDurations, "dynamicDepthStallDurations");
        }
        if (streamConfigurationArr4 == null) {
            this.mHeicConfigurations = new StreamConfiguration[0];
            this.mHeicMinFrameDurations = new StreamConfigurationDuration[0];
            this.mHeicStallDurations = new StreamConfigurationDuration[0];
            StreamConfigurationDuration[] streamConfigurationDurationArr8 = heicMinFrameDurations;
            streamConfigurationDurationArr = heicStallDurations;
        } else {
            this.mHeicConfigurations = (StreamConfiguration[]) Preconditions.checkArrayElementsNotNull(streamConfigurationArr4, "heicConfigurations");
            this.mHeicMinFrameDurations = (StreamConfigurationDuration[]) Preconditions.checkArrayElementsNotNull(heicMinFrameDurations, "heicMinFrameDurations");
            this.mHeicStallDurations = (StreamConfigurationDuration[]) Preconditions.checkArrayElementsNotNull(heicStallDurations, "heicStallDurations");
        }
        if (highSpeedVideoConfigurationArr == null) {
            this.mHighSpeedVideoConfigurations = new HighSpeedVideoConfiguration[0];
        } else {
            this.mHighSpeedVideoConfigurations = (HighSpeedVideoConfiguration[]) Preconditions.checkArrayElementsNotNull(highSpeedVideoConfigurationArr, "highSpeedVideoConfigurations");
        }
        streamConfigurationArr2 = this.mConfigurations;
        int length = streamConfigurationArr2.length;
        int i2 = 0;
        while (i2 < length) {
            SparseIntArray sparseIntArray;
            StreamConfiguration config2 = streamConfigurationArr2[i2];
            StreamConfiguration[] streamConfigurationArr6 = streamConfigurationArr2;
            int fmt = config2.getFormat();
            if (config2.isOutput()) {
                sparseIntArray = this.mAllOutputFormats;
                sparseIntArray.put(fmt, sparseIntArray.get(fmt) + 1);
                long duration = 0;
                if (this.mListHighResolution) {
                    StreamConfigurationDuration[] streamConfigurationDurationArr9 = this.mMinFrameDurations;
                    int length2 = streamConfigurationDurationArr9.length;
                    int i3 = 0;
                    while (i3 < length2) {
                        int i4;
                        StreamConfigurationDuration configurationDuration = streamConfigurationDurationArr9[i3];
                        StreamConfigurationDuration[] streamConfigurationDurationArr10 = streamConfigurationDurationArr9;
                        if (configurationDuration.getFormat() == fmt) {
                            i4 = length2;
                            if (configurationDuration.getWidth() == config2.getSize().getWidth() && configurationDuration.getHeight() == config2.getSize().getHeight()) {
                                duration = configurationDuration.getDuration();
                                break;
                            }
                        }
                        i4 = length2;
                        i3++;
                        streamConfigurationDurationArr9 = streamConfigurationDurationArr10;
                        length2 = i4;
                    }
                }
                sparseIntArray = duration <= DURATION_20FPS_NS ? this.mOutputFormats : this.mHighResOutputFormats;
            } else {
                sparseIntArray = this.mInputFormats;
            }
            sparseIntArray.put(fmt, sparseIntArray.get(fmt) + 1);
            i2++;
            streamConfigurationArr4 = heicConfigurations;
            streamConfigurationDurationArr = heicStallDurations;
            highSpeedVideoConfigurationArr = highSpeedVideoConfigurations;
            streamConfigurationArr2 = streamConfigurationArr6;
        }
        streamConfigurationArr2 = this.mDepthConfigurations;
        i2 = streamConfigurationArr2.length;
        int i5 = 0;
        while (i5 < i2) {
            config = streamConfigurationArr2[i5];
            if (config.isOutput()) {
                streamConfigurationArr5 = streamConfigurationArr2;
                i = i2;
                this.mDepthOutputFormats.put(config.getFormat(), this.mDepthOutputFormats.get(config.getFormat()) + 1);
            } else {
                streamConfigurationArr5 = streamConfigurationArr2;
                i = i2;
            }
            i5++;
            streamConfigurationArr2 = streamConfigurationArr5;
            i2 = i;
        }
        streamConfigurationArr2 = this.mDynamicDepthConfigurations;
        i2 = streamConfigurationArr2.length;
        i5 = 0;
        while (i5 < i2) {
            config = streamConfigurationArr2[i5];
            if (config.isOutput()) {
                streamConfigurationArr5 = streamConfigurationArr2;
                i = i2;
                this.mDynamicDepthOutputFormats.put(config.getFormat(), this.mDynamicDepthOutputFormats.get(config.getFormat()) + 1);
            } else {
                streamConfigurationArr5 = streamConfigurationArr2;
                i = i2;
            }
            i5++;
            streamConfigurationArr2 = streamConfigurationArr5;
            i2 = i;
        }
        streamConfigurationArr2 = this.mHeicConfigurations;
        i2 = streamConfigurationArr2.length;
        i5 = 0;
        while (i5 < i2) {
            config = streamConfigurationArr2[i5];
            if (config.isOutput()) {
                streamConfigurationArr5 = streamConfigurationArr2;
                i = i2;
                this.mHeicOutputFormats.put(config.getFormat(), this.mHeicOutputFormats.get(config.getFormat()) + 1);
            } else {
                streamConfigurationArr5 = streamConfigurationArr2;
                i = i2;
            }
            i5++;
            streamConfigurationArr2 = streamConfigurationArr5;
            i2 = i;
        }
        if (streamConfigurationArr == null || !enforceImplementationDefined || this.mOutputFormats.indexOfKey(34) >= 0) {
            HighSpeedVideoConfiguration[] highSpeedVideoConfigurationArr2 = this.mHighSpeedVideoConfigurations;
            i2 = highSpeedVideoConfigurationArr2.length;
            i5 = 0;
            while (i5 < i2) {
                HighSpeedVideoConfiguration config3 = highSpeedVideoConfigurationArr2[i5];
                Size size = config3.getSize();
                Range<Integer> fpsRange = config3.getFpsRange();
                Integer fpsRangeCount = (Integer) this.mHighSpeedVideoSizeMap.get(size);
                if (fpsRangeCount == null) {
                    fpsRangeCount = Integer.valueOf(0);
                }
                HighSpeedVideoConfiguration[] highSpeedVideoConfigurationArr3 = highSpeedVideoConfigurationArr2;
                this.mHighSpeedVideoSizeMap.put(size, Integer.valueOf(fpsRangeCount.intValue() + 1));
                fpsRangeCount = (Integer) this.mHighSpeedVideoFpsRangeMap.get(fpsRange);
                if (fpsRangeCount == null) {
                    fpsRangeCount = Integer.valueOf(0);
                }
                this.mHighSpeedVideoFpsRangeMap.put(fpsRange, Integer.valueOf(fpsRangeCount.intValue() + 1));
                i5++;
                streamConfigurationArr = configurations;
                highSpeedVideoConfigurationArr2 = highSpeedVideoConfigurationArr3;
            }
            this.mInputOutputFormatsMap = inputOutputFormatsMap;
            return;
        }
        throw new AssertionError("At least one stream configuration for IMPLEMENTATION_DEFINED must exist");
    }

    public int[] getOutputFormats() {
        return getPublicFormats(true);
    }

    public int[] getValidOutputFormatsForInput(int inputFormat) {
        int[] outputs = this.mInputOutputFormatsMap;
        if (outputs == null) {
            return new int[0];
        }
        outputs = outputs.getOutputs(inputFormat);
        if (this.mHeicOutputFormats.size() <= 0) {
            return outputs;
        }
        int[] outputsWithHeic = Arrays.copyOf(outputs, outputs.length + 1);
        outputsWithHeic[outputs.length] = ImageFormat.HEIC;
        return outputsWithHeic;
    }

    public int[] getInputFormats() {
        return getPublicFormats(false);
    }

    public Size[] getInputSizes(int format) {
        return getPublicFormatSizes(format, false, false);
    }

    public boolean isOutputSupportedFor(int format) {
        checkArgumentFormat(format);
        int internalFormat = imageFormatToInternal(format);
        int dataspace = imageFormatToDataspace(format);
        boolean z = false;
        if (dataspace == 4096) {
            if (this.mDepthOutputFormats.indexOfKey(internalFormat) >= 0) {
                z = true;
            }
            return z;
        } else if (dataspace == 4098) {
            if (this.mDynamicDepthOutputFormats.indexOfKey(internalFormat) >= 0) {
                z = true;
            }
            return z;
        } else if (dataspace == 4099) {
            if (this.mHeicOutputFormats.indexOfKey(internalFormat) >= 0) {
                z = true;
            }
            return z;
        } else {
            if (getFormatsMap(true).indexOfKey(internalFormat) >= 0) {
                z = true;
            }
            return z;
        }
    }

    public static <T> boolean isOutputSupportedFor(Class<T> klass) {
        Preconditions.checkNotNull(klass, "klass must not be null");
        if (klass == ImageReader.class || klass == MediaRecorder.class || klass == MediaCodec.class || klass == Allocation.class || klass == SurfaceHolder.class || klass == SurfaceTexture.class) {
            return true;
        }
        return false;
    }

    public boolean isOutputSupportedFor(Surface surface) {
        Preconditions.checkNotNull(surface, "surface must not be null");
        Size surfaceSize = SurfaceUtils.getSurfaceSize(surface);
        int surfaceFormat = SurfaceUtils.getSurfaceFormat(surface);
        int surfaceDataspace = SurfaceUtils.getSurfaceDataspace(surface);
        boolean isFlexible = SurfaceUtils.isFlexibleConsumer(surface);
        StreamConfiguration[] configs;
        if (surfaceDataspace == 4096) {
            configs = this.mDepthConfigurations;
        } else if (surfaceDataspace == 4098) {
            configs = this.mDynamicDepthConfigurations;
        } else if (surfaceDataspace == 4099) {
            configs = this.mHeicConfigurations;
        } else {
            configs = this.mConfigurations;
        }
        for (StreamConfiguration config : configs) {
            if (config.getFormat() == surfaceFormat && config.isOutput()) {
                if (config.getSize().equals(surfaceSize)) {
                    return true;
                }
                if (isFlexible && config.getSize().getWidth() <= LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOutputSupportedFor(Size size, int format) {
        int internalFormat = imageFormatToInternal(format);
        int dataspace = imageFormatToDataspace(format);
        StreamConfiguration[] configs;
        if (dataspace == 4096) {
            configs = this.mDepthConfigurations;
        } else if (dataspace == 4098) {
            configs = this.mDynamicDepthConfigurations;
        } else if (dataspace == 4099) {
            configs = this.mHeicConfigurations;
        } else {
            configs = this.mConfigurations;
        }
        for (StreamConfiguration config : configs) {
            if (config.getFormat() == internalFormat && config.isOutput() && config.getSize().equals(size)) {
                return true;
            }
        }
        return false;
    }

    public <T> Size[] getOutputSizes(Class<T> klass) {
        if (isOutputSupportedFor((Class) klass)) {
            return getInternalFormatSizes(34, 0, true, false);
        }
        return null;
    }

    public Size[] getOutputSizes(int format) {
        return getPublicFormatSizes(format, true, false);
    }

    public Size[] getHighSpeedVideoSizes() {
        Set<Size> keySet = this.mHighSpeedVideoSizeMap.keySet();
        return (Size[]) keySet.toArray(new Size[keySet.size()]);
    }

    public Range<Integer>[] getHighSpeedVideoFpsRangesFor(Size size) {
        Integer fpsRangeCount = (Integer) this.mHighSpeedVideoSizeMap.get(size);
        int i = 0;
        if (fpsRangeCount == null || fpsRangeCount.intValue() == 0) {
            throw new IllegalArgumentException(String.format("Size %s does not support high speed video recording", new Object[]{size}));
        }
        Range<Integer>[] fpsRanges = new Range[fpsRangeCount.intValue()];
        int i2 = 0;
        HighSpeedVideoConfiguration[] highSpeedVideoConfigurationArr = this.mHighSpeedVideoConfigurations;
        int length = highSpeedVideoConfigurationArr.length;
        while (i < length) {
            HighSpeedVideoConfiguration config = highSpeedVideoConfigurationArr[i];
            if (size.equals(config.getSize())) {
                int i3 = i2 + 1;
                fpsRanges[i2] = config.getFpsRange();
                i2 = i3;
            }
            i++;
        }
        return fpsRanges;
    }

    public Range<Integer>[] getHighSpeedVideoFpsRanges() {
        Set<Range<Integer>> keySet = this.mHighSpeedVideoFpsRangeMap.keySet();
        return (Range[]) keySet.toArray(new Range[keySet.size()]);
    }

    public Size[] getHighSpeedVideoSizesFor(Range<Integer> fpsRange) {
        Integer sizeCount = (Integer) this.mHighSpeedVideoFpsRangeMap.get(fpsRange);
        int i = 0;
        if (sizeCount == null || sizeCount.intValue() == 0) {
            throw new IllegalArgumentException(String.format("FpsRange %s does not support high speed video recording", new Object[]{fpsRange}));
        }
        Size[] sizes = new Size[sizeCount.intValue()];
        int i2 = 0;
        HighSpeedVideoConfiguration[] highSpeedVideoConfigurationArr = this.mHighSpeedVideoConfigurations;
        int length = highSpeedVideoConfigurationArr.length;
        while (i < length) {
            HighSpeedVideoConfiguration config = highSpeedVideoConfigurationArr[i];
            if (fpsRange.equals(config.getFpsRange())) {
                int i3 = i2 + 1;
                sizes[i2] = config.getSize();
                i2 = i3;
            }
            i++;
        }
        return sizes;
    }

    public Size[] getHighResolutionOutputSizes(int format) {
        if (this.mListHighResolution) {
            return getPublicFormatSizes(format, true, true);
        }
        return null;
    }

    public long getOutputMinFrameDuration(int format, Size size) {
        Preconditions.checkNotNull(size, "size must not be null");
        checkArgumentFormatSupported(format, true);
        return getInternalFormatDuration(imageFormatToInternal(format), imageFormatToDataspace(format), size, 0);
    }

    public <T> long getOutputMinFrameDuration(Class<T> klass, Size size) {
        if (isOutputSupportedFor((Class) klass)) {
            return getInternalFormatDuration(34, 0, size, 0);
        }
        throw new IllegalArgumentException("klass was not supported");
    }

    public long getOutputStallDuration(int format, Size size) {
        checkArgumentFormatSupported(format, true);
        return getInternalFormatDuration(imageFormatToInternal(format), imageFormatToDataspace(format), size, 1);
    }

    public <T> long getOutputStallDuration(Class<T> klass, Size size) {
        if (isOutputSupportedFor((Class) klass)) {
            return getInternalFormatDuration(34, 0, size, 1);
        }
        throw new IllegalArgumentException("klass was not supported");
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StreamConfigurationMap)) {
            return false;
        }
        StreamConfigurationMap other = (StreamConfigurationMap) obj;
        if (Arrays.equals(this.mConfigurations, other.mConfigurations) && Arrays.equals(this.mMinFrameDurations, other.mMinFrameDurations) && Arrays.equals(this.mStallDurations, other.mStallDurations) && Arrays.equals(this.mDepthConfigurations, other.mDepthConfigurations) && Arrays.equals(this.mDepthMinFrameDurations, other.mDepthMinFrameDurations) && Arrays.equals(this.mDepthStallDurations, other.mDepthStallDurations) && Arrays.equals(this.mDynamicDepthConfigurations, other.mDynamicDepthConfigurations) && Arrays.equals(this.mDynamicDepthMinFrameDurations, other.mDynamicDepthMinFrameDurations) && Arrays.equals(this.mDynamicDepthStallDurations, other.mDynamicDepthStallDurations) && Arrays.equals(this.mHeicConfigurations, other.mHeicConfigurations) && Arrays.equals(this.mHeicMinFrameDurations, other.mHeicMinFrameDurations) && Arrays.equals(this.mHeicStallDurations, other.mHeicStallDurations) && Arrays.equals(this.mHighSpeedVideoConfigurations, other.mHighSpeedVideoConfigurations)) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return HashCodeHelpers.hashCodeGeneric(this.mConfigurations, this.mMinFrameDurations, this.mStallDurations, this.mDepthConfigurations, this.mDepthMinFrameDurations, this.mDepthStallDurations, this.mDynamicDepthConfigurations, this.mDynamicDepthMinFrameDurations, this.mDynamicDepthStallDurations, this.mHeicConfigurations, this.mHeicMinFrameDurations, this.mHeicStallDurations, this.mHighSpeedVideoConfigurations);
    }

    private int checkArgumentFormatSupported(int format, boolean output) {
        checkArgumentFormat(format);
        int internalFormat = imageFormatToInternal(format);
        int internalDataspace = imageFormatToDataspace(format);
        if (output) {
            if (internalDataspace == 4096) {
                if (this.mDepthOutputFormats.indexOfKey(internalFormat) >= 0) {
                    return format;
                }
            } else if (internalDataspace == 4098) {
                if (this.mDynamicDepthOutputFormats.indexOfKey(internalFormat) >= 0) {
                    return format;
                }
            } else if (internalDataspace == 4099) {
                if (this.mHeicOutputFormats.indexOfKey(internalFormat) >= 0) {
                    return format;
                }
            } else if (this.mAllOutputFormats.indexOfKey(internalFormat) >= 0) {
                return format;
            }
        } else if (this.mInputFormats.indexOfKey(internalFormat) >= 0) {
            return format;
        }
        throw new IllegalArgumentException(String.format("format %x is not supported by this stream configuration map", new Object[]{Integer.valueOf(format)}));
    }

    static int checkArgumentFormatInternal(int format) {
        if (!(format == 33 || format == 34 || format == 36)) {
            if (format != 256) {
                if (format != 540422489) {
                    if (format != ImageFormat.HEIC) {
                        return checkArgumentFormat(format);
                    }
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("An unknown internal format: ");
            stringBuilder.append(format);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        return format;
    }

    static int checkArgumentFormat(int format) {
        if (ImageFormat.isPublicFormat(format) || PixelFormat.isPublicFormat(format)) {
            return format;
        }
        throw new IllegalArgumentException(String.format("format 0x%x was not defined in either ImageFormat or PixelFormat", new Object[]{Integer.valueOf(format)}));
    }

    public static int imageFormatToPublic(int format) {
        if (format == 33) {
            return 256;
        }
        if (format != 256) {
            return format;
        }
        throw new IllegalArgumentException("ImageFormat.JPEG is an unknown internal format");
    }

    public static int depthFormatToPublic(int format) {
        if (format == 256) {
            throw new IllegalArgumentException("ImageFormat.JPEG is an unknown internal format");
        } else if (format == 540422489) {
            return ImageFormat.DEPTH16;
        } else {
            switch (format) {
                case 32:
                    return 4098;
                case 33:
                    return 257;
                case 34:
                    throw new IllegalArgumentException("IMPLEMENTATION_DEFINED must not leak to public API");
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown DATASPACE_DEPTH format ");
                    stringBuilder.append(format);
                    throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
    }

    static int[] imageFormatToPublic(int[] formats) {
        if (formats == null) {
            return null;
        }
        for (int i = 0; i < formats.length; i++) {
            formats[i] = imageFormatToPublic(formats[i]);
        }
        return formats;
    }

    static int imageFormatToInternal(int format) {
        if (!(format == 256 || format == 257)) {
            if (format == 4098) {
                return 32;
            }
            if (format == ImageFormat.DEPTH16) {
                return 540422489;
            }
            if (!(format == ImageFormat.HEIC || format == ImageFormat.DEPTH_JPEG)) {
                return format;
            }
        }
        return 33;
    }

    static int imageFormatToDataspace(int format) {
        if (format == 256) {
            return HAL_DATASPACE_V0_JFIF;
        }
        if (format == 257 || format == 4098 || format == ImageFormat.DEPTH16) {
            return 4096;
        }
        if (format != ImageFormat.HEIC) {
            return format != ImageFormat.DEPTH_JPEG ? 0 : 4098;
        } else {
            return 4099;
        }
    }

    public static int[] imageFormatToInternal(int[] formats) {
        if (formats == null) {
            return null;
        }
        for (int i = 0; i < formats.length; i++) {
            formats[i] = imageFormatToInternal(formats[i]);
        }
        return formats;
    }

    private Size[] getPublicFormatSizes(int format, boolean output, boolean highRes) {
        try {
            checkArgumentFormatSupported(format, output);
            return getInternalFormatSizes(imageFormatToInternal(format), imageFormatToDataspace(format), output, highRes);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Size[] getInternalFormatSizes(int format, int dataspace, boolean output, boolean highRes) {
        StreamConfigurationMap streamConfigurationMap = this;
        int i = format;
        int i2 = dataspace;
        boolean z = output;
        boolean z2 = highRes;
        Object obj = 4096;
        if (i2 == 4096 && z2) {
            return new Size[0];
        }
        SparseIntArray formatsMap;
        if (!z) {
            formatsMap = streamConfigurationMap.mInputFormats;
        } else if (i2 == 4096) {
            formatsMap = streamConfigurationMap.mDepthOutputFormats;
        } else if (i2 == 4098) {
            formatsMap = streamConfigurationMap.mDynamicDepthOutputFormats;
        } else if (i2 == 4099) {
            formatsMap = streamConfigurationMap.mHeicOutputFormats;
        } else if (z2) {
            formatsMap = streamConfigurationMap.mHighResOutputFormats;
        } else {
            formatsMap = streamConfigurationMap.mOutputFormats;
        }
        int sizesCount = formatsMap.get(i);
        if (((!z || i2 == 4096 || i2 == 4098 || i2 == 4099) && sizesCount == 0) || (z && i2 != 4096 && i2 != 4098 && i2 != 4099 && streamConfigurationMap.mAllOutputFormats.get(i) == 0)) {
            return null;
        }
        StreamConfiguration[] configurations;
        StreamConfigurationDuration[] minFrameDurations;
        Size[] sizes = new Size[sizesCount];
        if (i2 == 4096) {
            configurations = streamConfigurationMap.mDepthConfigurations;
        } else if (i2 == 4098) {
            configurations = streamConfigurationMap.mDynamicDepthConfigurations;
        } else if (i2 == 4099) {
            configurations = streamConfigurationMap.mHeicConfigurations;
        } else {
            configurations = streamConfigurationMap.mConfigurations;
        }
        if (i2 == 4096) {
            minFrameDurations = streamConfigurationMap.mDepthMinFrameDurations;
        } else if (i2 == 4098) {
            minFrameDurations = streamConfigurationMap.mDynamicDepthMinFrameDurations;
        } else if (i2 == 4099) {
            minFrameDurations = streamConfigurationMap.mHeicMinFrameDurations;
        } else {
            minFrameDurations = streamConfigurationMap.mMinFrameDurations;
        }
        int length = configurations.length;
        int sizeIndex = 0;
        int sizeIndex2 = 0;
        while (sizeIndex < length) {
            Object obj2;
            StreamConfiguration config = configurations[sizeIndex];
            int fmt = config.getFormat();
            if (fmt == i && config.isOutput() == z) {
                if (z && streamConfigurationMap.mListHighResolution) {
                    long duration = 0;
                    int i3 = 0;
                    while (i3 < minFrameDurations.length) {
                        StreamConfigurationDuration d = minFrameDurations[i3];
                        if (d.getFormat() == fmt && d.getWidth() == config.getSize().getWidth() && d.getHeight() == config.getSize().getHeight()) {
                            duration = d.getDuration();
                            break;
                        }
                        i3++;
                        i = format;
                    }
                    obj2 = 4096;
                    if (i2 != 4096) {
                        if (z2 != (duration > DURATION_20FPS_NS)) {
                        }
                    }
                } else {
                    obj2 = obj;
                }
                i = sizeIndex2 + 1;
                sizes[sizeIndex2] = config.getSize();
                sizeIndex2 = i;
            } else {
                obj2 = obj;
            }
            sizeIndex++;
            i = format;
            obj = obj2;
            streamConfigurationMap = this;
        }
        String str = ")";
        String str2 = ", actual ";
        StringBuilder stringBuilder;
        if (sizeIndex2 == sizesCount || !(i2 == 4098 || i2 == 4099)) {
            if (sizeIndex2 != sizesCount) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Too few sizes (expected ");
                stringBuilder.append(sizesCount);
                stringBuilder.append(str2);
                stringBuilder.append(sizeIndex2);
                stringBuilder.append(str);
                throw new AssertionError(stringBuilder.toString());
            }
        } else if (sizeIndex2 > sizesCount) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Too many dynamic depth sizes (expected ");
            stringBuilder.append(sizesCount);
            stringBuilder.append(str2);
            stringBuilder.append(sizeIndex2);
            stringBuilder.append(str);
            throw new AssertionError(stringBuilder.toString());
        } else if (sizeIndex2 <= 0) {
            sizes = new Size[0];
        } else {
            sizes = (Size[]) Arrays.copyOf(sizes, sizeIndex2);
        }
        return sizes;
    }

    private int[] getPublicFormats(boolean output) {
        int[] formats = new int[getPublicFormatCount(output)];
        int i = 0;
        SparseIntArray map = getFormatsMap(output);
        int j = 0;
        while (j < map.size()) {
            int i2 = i + 1;
            formats[i] = imageFormatToPublic(map.keyAt(j));
            j++;
            i = i2;
        }
        if (output) {
            j = 0;
            while (j < this.mDepthOutputFormats.size()) {
                int i3 = i + 1;
                formats[i] = depthFormatToPublic(this.mDepthOutputFormats.keyAt(j));
                j++;
                i = i3;
            }
            if (this.mDynamicDepthOutputFormats.size() > 0) {
                j = i + 1;
                formats[i] = ImageFormat.DEPTH_JPEG;
                i = j;
            }
            if (this.mHeicOutputFormats.size() > 0) {
                j = i + 1;
                formats[i] = ImageFormat.HEIC;
                i = j;
            }
        }
        if (formats.length == i) {
            return formats;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Too few formats ");
        stringBuilder.append(i);
        stringBuilder.append(", expected ");
        stringBuilder.append(formats.length);
        throw new AssertionError(stringBuilder.toString());
    }

    private SparseIntArray getFormatsMap(boolean output) {
        return output ? this.mAllOutputFormats : this.mInputFormats;
    }

    private long getInternalFormatDuration(int format, int dataspace, Size size, int duration) {
        if (isSupportedInternalConfiguration(format, dataspace, size)) {
            for (StreamConfigurationDuration configurationDuration : getDurations(duration, dataspace)) {
                if (configurationDuration.getFormat() == format && configurationDuration.getWidth() == size.getWidth() && configurationDuration.getHeight() == size.getHeight()) {
                    return configurationDuration.getDuration();
                }
            }
            return 0;
        }
        throw new IllegalArgumentException("size was not supported");
    }

    private StreamConfigurationDuration[] getDurations(int duration, int dataspace) {
        StreamConfigurationDuration[] streamConfigurationDurationArr;
        if (duration == 0) {
            if (dataspace == 4096) {
                streamConfigurationDurationArr = this.mDepthMinFrameDurations;
            } else if (dataspace == 4098) {
                streamConfigurationDurationArr = this.mDynamicDepthMinFrameDurations;
            } else if (dataspace == 4099) {
                streamConfigurationDurationArr = this.mHeicMinFrameDurations;
            } else {
                streamConfigurationDurationArr = this.mMinFrameDurations;
            }
            return streamConfigurationDurationArr;
        } else if (duration == 1) {
            if (dataspace == 4096) {
                streamConfigurationDurationArr = this.mDepthStallDurations;
            } else if (dataspace == 4098) {
                streamConfigurationDurationArr = this.mDynamicDepthStallDurations;
            } else if (dataspace == 4099) {
                streamConfigurationDurationArr = this.mHeicStallDurations;
            } else {
                streamConfigurationDurationArr = this.mStallDurations;
            }
            return streamConfigurationDurationArr;
        } else {
            throw new IllegalArgumentException("duration was invalid");
        }
    }

    private int getPublicFormatCount(boolean output) {
        int size = getFormatsMap(output).size();
        if (output) {
            return ((size + this.mDepthOutputFormats.size()) + this.mDynamicDepthOutputFormats.size()) + this.mHeicOutputFormats.size();
        }
        return size;
    }

    private static <T> boolean arrayContains(T[] array, T element) {
        if (array == null) {
            return false;
        }
        for (T el : array) {
            if (Objects.equals(el, element)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSupportedInternalConfiguration(int format, int dataspace, Size size) {
        StreamConfiguration[] configurations;
        if (dataspace == 4096) {
            configurations = this.mDepthConfigurations;
        } else if (dataspace == 4098) {
            configurations = this.mDynamicDepthConfigurations;
        } else if (dataspace == 4099) {
            configurations = this.mHeicConfigurations;
        } else {
            configurations = this.mConfigurations;
        }
        int i = 0;
        while (i < configurations.length) {
            if (configurations[i].getFormat() == format && configurations[i].getSize().equals(size)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("StreamConfiguration(");
        appendOutputsString(sb);
        String str = ", ";
        sb.append(str);
        appendHighResOutputsString(sb);
        sb.append(str);
        appendInputsString(sb);
        sb.append(str);
        appendValidOutputFormatsForInputString(sb);
        sb.append(str);
        appendHighSpeedVideoConfigurationsString(sb);
        sb.append(")");
        return sb.toString();
    }

    private void appendOutputsString(StringBuilder sb) {
        StringBuilder stringBuilder = sb;
        stringBuilder.append("Outputs(");
        int[] formats = getOutputFormats();
        int length = formats.length;
        int i = 0;
        int i2 = 0;
        while (i2 < length) {
            int format = formats[i2];
            Size[] sizes = getOutputSizes(format);
            int length2 = sizes.length;
            int i3 = i;
            while (i3 < length2) {
                Size size = sizes[i3];
                long minFrameDuration = getOutputMinFrameDuration(format, size);
                long stallDuration = getOutputStallDuration(format, size);
                stringBuilder.append(String.format("[w:%d, h:%d, format:%s(%d), min_duration:%d, stall:%d], ", new Object[]{Integer.valueOf(size.getWidth()), Integer.valueOf(size.getHeight()), formatToString(format), Integer.valueOf(format), Long.valueOf(minFrameDuration), Long.valueOf(stallDuration)}));
                i3++;
                i = 0;
            }
            i2++;
            i = 0;
        }
        if (stringBuilder.charAt(sb.length() - 1) == ' ') {
            stringBuilder.delete(sb.length() - 2, sb.length());
        }
        stringBuilder.append(")");
    }

    private void appendHighResOutputsString(StringBuilder sb) {
        StringBuilder stringBuilder = sb;
        stringBuilder.append("HighResolutionOutputs(");
        int[] formats = getOutputFormats();
        int length = formats.length;
        int i = 0;
        int i2 = 0;
        while (i2 < length) {
            int format = formats[i2];
            Size[] sizes = getHighResolutionOutputSizes(format);
            if (sizes != null) {
                int length2 = sizes.length;
                int i3 = i;
                while (i3 < length2) {
                    Size size = sizes[i3];
                    long minFrameDuration = getOutputMinFrameDuration(format, size);
                    long stallDuration = getOutputStallDuration(format, size);
                    stringBuilder.append(String.format("[w:%d, h:%d, format:%s(%d), min_duration:%d, stall:%d], ", new Object[]{Integer.valueOf(size.getWidth()), Integer.valueOf(size.getHeight()), formatToString(format), Integer.valueOf(format), Long.valueOf(minFrameDuration), Long.valueOf(stallDuration)}));
                    i3++;
                    i = 0;
                }
            }
            i2++;
            i = 0;
        }
        if (stringBuilder.charAt(sb.length() - 1) == ' ') {
            stringBuilder.delete(sb.length() - 2, sb.length());
        }
        stringBuilder.append(")");
    }

    private void appendInputsString(StringBuilder sb) {
        sb.append("Inputs(");
        for (int format : getInputFormats()) {
            for (Size size : getInputSizes(format)) {
                sb.append(String.format("[w:%d, h:%d, format:%s(%d)], ", new Object[]{Integer.valueOf(size.getWidth()), Integer.valueOf(size.getHeight()), formatToString(format), Integer.valueOf(format)}));
            }
        }
        if (sb.charAt(sb.length() - 1) == ' ') {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(")");
    }

    private void appendValidOutputFormatsForInputString(StringBuilder sb) {
        sb.append("ValidOutputFormatsForInput(");
        for (int inputFormat : getInputFormats()) {
            sb.append(String.format("[in:%s(%d), out:", new Object[]{formatToString(inputFormat), Integer.valueOf(inputFormats[r3])}));
            int[] outputFormats = getValidOutputFormatsForInput(inputFormat);
            for (int i = 0; i < outputFormats.length; i++) {
                sb.append(String.format("%s(%d)", new Object[]{formatToString(outputFormats[i]), Integer.valueOf(outputFormats[i])}));
                if (i < outputFormats.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("], ");
        }
        if (sb.charAt(sb.length() - 1) == ' ') {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(")");
    }

    private void appendHighSpeedVideoConfigurationsString(StringBuilder sb) {
        sb.append("HighSpeedVideoConfigurations(");
        for (Size size : getHighSpeedVideoSizes()) {
            for (Range<Integer> range : getHighSpeedVideoFpsRangesFor(size)) {
                sb.append(String.format("[w:%d, h:%d, min_fps:%d, max_fps:%d], ", new Object[]{Integer.valueOf(size.getWidth()), Integer.valueOf(size.getHeight()), range.getLower(), range.getUpper()}));
            }
        }
        if (sb.charAt(sb.length() - 1) == ' ') {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(")");
    }

    private String formatToString(int format) {
        if (format == 1) {
            return "RGBA_8888";
        }
        if (format == 2) {
            return "RGBX_8888";
        }
        if (format == 3) {
            return "RGB_888";
        }
        if (format == 4) {
            return "RGB_565";
        }
        if (format == 16) {
            return "NV16";
        }
        if (format == 17) {
            return "NV21";
        }
        if (format == 256) {
            return "JPEG";
        }
        if (format == 257) {
            return "DEPTH_POINT_CLOUD";
        }
        switch (format) {
            case 20:
                return "YUY2";
            case 32:
                return "RAW_SENSOR";
            case 4098:
                return "RAW_DEPTH";
            case ImageFormat.Y8 /*538982489*/:
                return "Y8";
            case 540422489:
                return "Y16";
            case ImageFormat.YV12 /*842094169*/:
                return "YV12";
            case ImageFormat.DEPTH16 /*1144402265*/:
                return "DEPTH16";
            case ImageFormat.HEIC /*1212500294*/:
                return "HEIC";
            case ImageFormat.DEPTH_JPEG /*1768253795*/:
                return "DEPTH_JPEG";
            default:
                switch (format) {
                    case 34:
                        return "PRIVATE";
                    case 35:
                        return "YUV_420_888";
                    case 36:
                        return "RAW_PRIVATE";
                    case 37:
                        return "RAW10";
                    default:
                        return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
                }
        }
    }
}
