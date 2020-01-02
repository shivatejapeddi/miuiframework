package android.hardware.camera2.params;

import android.hardware.camera2.CameraManager;
import android.hardware.camera2.legacy.LegacyCameraDevice;
import android.hardware.camera2.utils.HashCodeHelpers;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class MandatoryStreamCombination {
    private static final String TAG = "MandatoryStreamCombination";
    private static StreamCombinationTemplate[] sBurstCombinations;
    private static StreamCombinationTemplate[] sFullCombinations;
    private static StreamCombinationTemplate[] sFullPrivateReprocCombinations;
    private static StreamCombinationTemplate[] sFullYUVReprocCombinations;
    private static StreamCombinationTemplate[] sLegacyCombinations;
    private static StreamCombinationTemplate[] sLevel3Combinations;
    private static StreamCombinationTemplate[] sLevel3PrivateReprocCombinations;
    private static StreamCombinationTemplate[] sLevel3YUVReprocCombinations;
    private static StreamCombinationTemplate[] sLimitedCombinations;
    private static StreamCombinationTemplate[] sLimitedPrivateReprocCombinations;
    private static StreamCombinationTemplate[] sLimitedYUVReprocCombinations;
    private static StreamCombinationTemplate[] sRAWPrivateReprocCombinations;
    private static StreamCombinationTemplate[] sRAWYUVReprocCombinations;
    private static StreamCombinationTemplate[] sRawCombinations;
    private final String mDescription;
    private final boolean mIsReprocessable;
    private final ArrayList<MandatoryStreamInformation> mStreamsInformation = new ArrayList();

    public static final class Builder {
        private final Size kPreviewSizeBound = new Size(LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING, 1088);
        private int mCameraId;
        private List<Integer> mCapabilities;
        private Size mDisplaySize;
        private int mHwLevel;
        private boolean mIsHiddenPhysicalCamera;
        private StreamConfigurationMap mStreamConfigMap;

        public static class SizeComparator implements Comparator<Size> {
            public int compare(Size lhs, Size rhs) {
                return Builder.compareSizes(lhs.getWidth(), lhs.getHeight(), rhs.getWidth(), rhs.getHeight());
            }
        }

        public Builder(int cameraId, int hwLevel, Size displaySize, List<Integer> capabilities, StreamConfigurationMap sm) {
            this.mCameraId = cameraId;
            this.mDisplaySize = displaySize;
            this.mCapabilities = capabilities;
            this.mStreamConfigMap = sm;
            this.mHwLevel = hwLevel;
            this.mIsHiddenPhysicalCamera = CameraManager.isHiddenPhysicalCamera(Integer.toString(this.mCameraId));
        }

        public List<MandatoryStreamCombination> getAvailableMandatoryStreamCombinations() {
            boolean isColorOutputSupported = isColorOutputSupported();
            String str = MandatoryStreamCombination.TAG;
            if (!isColorOutputSupported) {
                Log.v(str, "Device is not backward compatible!");
                return null;
            } else if (this.mCameraId >= 0 || isExternalCamera()) {
                ArrayList<StreamCombinationTemplate> availableTemplates = new ArrayList();
                if (isHardwareLevelAtLeastLegacy()) {
                    availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sLegacyCombinations));
                }
                if (isHardwareLevelAtLeastLimited() || isExternalCamera()) {
                    availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sLimitedCombinations));
                    if (isPrivateReprocessingSupported()) {
                        availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sLimitedPrivateReprocCombinations));
                    }
                    if (isYUVReprocessingSupported()) {
                        availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sLimitedYUVReprocCombinations));
                    }
                }
                if (isCapabilitySupported(6)) {
                    availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sBurstCombinations));
                }
                if (isHardwareLevelAtLeastFull()) {
                    availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sFullCombinations));
                    if (isPrivateReprocessingSupported()) {
                        availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sFullPrivateReprocCombinations));
                    }
                    if (isYUVReprocessingSupported()) {
                        availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sFullYUVReprocCombinations));
                    }
                }
                if (isCapabilitySupported(3)) {
                    availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sRawCombinations));
                    if (isPrivateReprocessingSupported()) {
                        availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sRAWPrivateReprocCombinations));
                    }
                    if (isYUVReprocessingSupported()) {
                        availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sRAWYUVReprocCombinations));
                    }
                }
                if (isHardwareLevelAtLeastLevel3()) {
                    availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sLevel3Combinations));
                    if (isPrivateReprocessingSupported()) {
                        availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sLevel3PrivateReprocCombinations));
                    }
                    if (isYUVReprocessingSupported()) {
                        availableTemplates.addAll(Arrays.asList(MandatoryStreamCombination.sLevel3YUVReprocCombinations));
                    }
                }
                return generateAvailableCombinations(availableTemplates);
            } else {
                Log.i(str, "Invalid camera id");
                return null;
            }
        }

        private List<MandatoryStreamCombination> generateAvailableCombinations(ArrayList<StreamCombinationTemplate> availableTemplates) {
            boolean isEmpty = availableTemplates.isEmpty();
            String str = MandatoryStreamCombination.TAG;
            if (isEmpty) {
                Log.e(str, "No available stream templates!");
                return null;
            }
            HashMap<Pair<SizeThreshold, Integer>, List<Size>> availableSizes = enumerateAvailableSizes();
            if (availableSizes == null) {
                Log.e(str, "Available size enumeration failed!");
                return null;
            }
            Size maxPrivateInputSize;
            Size maxYUVInputSize;
            Size[] rawSizes = this.mStreamConfigMap.getOutputSizes(32);
            ArrayList<Size> availableRawSizes = new ArrayList();
            if (rawSizes != null) {
                availableRawSizes.ensureCapacity(rawSizes.length);
                availableRawSizes.addAll(Arrays.asList(rawSizes));
            }
            Size maxPrivateInputSize2 = new Size(0, 0);
            if (isPrivateReprocessingSupported()) {
                maxPrivateInputSize = getMaxSize(this.mStreamConfigMap.getInputSizes(34));
            } else {
                maxPrivateInputSize = maxPrivateInputSize2;
            }
            maxPrivateInputSize2 = new Size(0, 0);
            if (isYUVReprocessingSupported()) {
                maxYUVInputSize = getMaxSize(this.mStreamConfigMap.getInputSizes(35));
            } else {
                maxYUVInputSize = maxPrivateInputSize2;
            }
            ArrayList<MandatoryStreamCombination> availableStreamCombinations = new ArrayList();
            availableStreamCombinations.ensureCapacity(availableTemplates.size());
            IllegalArgumentException e = availableTemplates.iterator();
            while (e.hasNext()) {
                IllegalArgumentException illegalArgumentException;
                StreamCombinationTemplate combTemplate = (StreamCombinationTemplate) e.next();
                ArrayList<MandatoryStreamInformation> streamsInfo = new ArrayList();
                streamsInfo.ensureCapacity(combTemplate.mStreamTemplates.length);
                boolean isReprocessable = combTemplate.mReprocessType != ReprocessType.NONE;
                if (isReprocessable) {
                    int format;
                    ArrayList<Size> inputSize = new ArrayList();
                    if (combTemplate.mReprocessType == ReprocessType.PRIVATE) {
                        inputSize.add(maxPrivateInputSize);
                        format = 34;
                    } else {
                        inputSize.add(maxYUVInputSize);
                        format = 35;
                    }
                    streamsInfo.add(new MandatoryStreamInformation(inputSize, format, true));
                    streamsInfo.add(new MandatoryStreamInformation(inputSize, format));
                }
                StreamTemplate[] streamTemplateArr = combTemplate.mStreamTemplates;
                int length = streamTemplateArr.length;
                int i = 0;
                while (i < length) {
                    List<Size> sizes;
                    StreamTemplate[] streamTemplateArr2;
                    int i2;
                    StreamTemplate template = streamTemplateArr[i];
                    illegalArgumentException = e;
                    if (template.mFormat == 32) {
                        sizes = availableRawSizes;
                        streamTemplateArr2 = streamTemplateArr;
                        i2 = length;
                    } else {
                        streamTemplateArr2 = streamTemplateArr;
                        i2 = length;
                        sizes = (List) availableSizes.get(new Pair(template.mSizeThreshold, new Integer(template.mFormat)));
                    }
                    try {
                        streamsInfo.add(new MandatoryStreamInformation(sizes, template.mFormat));
                        i++;
                        e = illegalArgumentException;
                        streamTemplateArr = streamTemplateArr2;
                        length = i2;
                    } catch (IllegalArgumentException e2) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("No available sizes found for format: ");
                        stringBuilder.append(template.mFormat);
                        stringBuilder.append(" size threshold: ");
                        stringBuilder.append(template.mSizeThreshold);
                        stringBuilder.append(" combination: ");
                        stringBuilder.append(combTemplate.mDescription);
                        Log.e(str, stringBuilder.toString());
                        return null;
                    }
                }
                illegalArgumentException = e;
                try {
                    availableStreamCombinations.add(new MandatoryStreamCombination(streamsInfo, combTemplate.mDescription, isReprocessable));
                    e = illegalArgumentException;
                } catch (IllegalArgumentException e3) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("No stream information for mandatory combination: ");
                    stringBuilder2.append(combTemplate.mDescription);
                    Log.e(str, stringBuilder2.toString());
                    return null;
                }
            }
            return Collections.unmodifiableList(availableStreamCombinations);
        }

        private HashMap<Pair<SizeThreshold, Integer>, List<Size>> enumerateAvailableSizes() {
            int[] formats = new int[]{34, 35, 256};
            int i = 0;
            Size recordingMaxSize = new Size(0, 0);
            Size previewMaxSize = new Size(0, 0);
            Size vgaSize = new Size(640, 480);
            if (isExternalCamera() || this.mIsHiddenPhysicalCamera) {
                recordingMaxSize = getMaxCameraRecordingSize();
            } else {
                recordingMaxSize = getMaxRecordingSize();
            }
            String str = MandatoryStreamCombination.TAG;
            if (recordingMaxSize == null) {
                Log.e(str, "Failed to find maximum recording size!");
                return null;
            }
            int length;
            HashMap<Integer, Size[]> allSizes = new HashMap();
            for (int format : formats) {
                allSizes.put(new Integer(format), this.mStreamConfigMap.getOutputSizes(format));
            }
            List<Size> previewSizes = getSizesWithinBound((Size[]) allSizes.get(new Integer(34)), this.kPreviewSizeBound);
            if (previewSizes == null || previewSizes.isEmpty()) {
                Log.e(str, "No preview sizes within preview size bound!");
                return null;
            }
            previewMaxSize = getMaxPreviewSize(getAscendingOrderSizes(previewSizes, false));
            HashMap<Pair<SizeThreshold, Integer>, List<Size>> availableSizes = new HashMap();
            length = formats.length;
            while (i < length) {
                Integer intFormat = new Integer(formats[i]);
                Size[] sizes = (Size[]) allSizes.get(intFormat);
                availableSizes.put(new Pair(SizeThreshold.VGA, intFormat), getSizesWithinBound(sizes, vgaSize));
                availableSizes.put(new Pair(SizeThreshold.PREVIEW, intFormat), getSizesWithinBound(sizes, previewMaxSize));
                availableSizes.put(new Pair(SizeThreshold.RECORD, intFormat), getSizesWithinBound(sizes, recordingMaxSize));
                availableSizes.put(new Pair(SizeThreshold.MAXIMUM, intFormat), Arrays.asList(sizes));
                i++;
            }
            return availableSizes;
        }

        private static List<Size> getSizesWithinBound(Size[] sizes, Size bound) {
            ArrayList<Size> ret = new ArrayList();
            for (Size size : sizes) {
                if (size.getWidth() <= bound.getWidth() && size.getHeight() <= bound.getHeight()) {
                    ret.add(size);
                }
            }
            return ret;
        }

        public static Size getMaxSize(Size... sizes) {
            if (sizes == null || sizes.length == 0) {
                throw new IllegalArgumentException("sizes was empty");
            }
            int i = 0;
            Size sz = sizes[0];
            int length = sizes.length;
            while (i < length) {
                Size size = sizes[i];
                if (size.getWidth() * size.getHeight() > sz.getWidth() * sz.getHeight()) {
                    sz = size;
                }
                i++;
            }
            return sz;
        }

        private boolean isHardwareLevelAtLeast(int level) {
            int[] sortedHwLevels = new int[]{2, 4, 0, 1, 3};
            if (level == this.mHwLevel) {
                return true;
            }
            for (int sortedlevel : sortedHwLevels) {
                if (sortedlevel == level) {
                    return true;
                }
                if (sortedlevel == this.mHwLevel) {
                    return false;
                }
            }
            return false;
        }

        private boolean isExternalCamera() {
            return this.mHwLevel == 4;
        }

        private boolean isHardwareLevelAtLeastLegacy() {
            return isHardwareLevelAtLeast(2);
        }

        private boolean isHardwareLevelAtLeastLimited() {
            return isHardwareLevelAtLeast(0);
        }

        private boolean isHardwareLevelAtLeastFull() {
            return isHardwareLevelAtLeast(1);
        }

        private boolean isHardwareLevelAtLeastLevel3() {
            return isHardwareLevelAtLeast(3);
        }

        private boolean isCapabilitySupported(int capability) {
            return this.mCapabilities.contains(Integer.valueOf(capability));
        }

        private boolean isColorOutputSupported() {
            return isCapabilitySupported(0);
        }

        private boolean isPrivateReprocessingSupported() {
            return isCapabilitySupported(4);
        }

        private boolean isYUVReprocessingSupported() {
            return isCapabilitySupported(7);
        }

        private Size getMaxRecordingSize() {
            int i = 8;
            if (!CamcorderProfile.hasProfile(this.mCameraId, 8)) {
                if (CamcorderProfile.hasProfile(this.mCameraId, 6)) {
                    i = 6;
                } else if (CamcorderProfile.hasProfile(this.mCameraId, 5)) {
                    i = 5;
                } else if (CamcorderProfile.hasProfile(this.mCameraId, 4)) {
                    i = 4;
                } else if (CamcorderProfile.hasProfile(this.mCameraId, 7)) {
                    i = 7;
                } else if (CamcorderProfile.hasProfile(this.mCameraId, 3)) {
                    i = 3;
                } else if (CamcorderProfile.hasProfile(this.mCameraId, 2)) {
                    i = 2;
                } else {
                    i = -1;
                }
            }
            int quality = i;
            if (quality < 0) {
                return null;
            }
            CamcorderProfile maxProfile = CamcorderProfile.get(this.mCameraId, quality);
            return new Size(maxProfile.videoFrameWidth, maxProfile.videoFrameHeight);
        }

        private Size getMaxCameraRecordingSize() {
            Size FULLHD = new Size(LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING, 1080);
            Size[] videoSizeArr = this.mStreamConfigMap.getOutputSizes(MediaRecorder.class);
            List<Size> sizes = new ArrayList();
            for (Size sz : videoSizeArr) {
                if (sz.getWidth() <= FULLHD.getWidth() && sz.getHeight() <= FULLHD.getHeight()) {
                    sizes.add(sz);
                }
            }
            Iterator it = getAscendingOrderSizes(sizes, false).iterator();
            while (true) {
                boolean hasNext = it.hasNext();
                String str = MandatoryStreamCombination.TAG;
                StringBuilder stringBuilder;
                if (hasNext) {
                    Size sz2 = (Size) it.next();
                    if (((double) this.mStreamConfigMap.getOutputMinFrameDuration(MediaRecorder.class, sz2)) > 3.3222591362126246E7d) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("External camera ");
                        stringBuilder.append(this.mCameraId);
                        stringBuilder.append(" has max video size:");
                        stringBuilder.append(sz2);
                        Log.i(str, stringBuilder.toString());
                        return sz2;
                    }
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Camera ");
                    stringBuilder.append(this.mCameraId);
                    stringBuilder.append(" does not support any 30fps video output");
                    Log.w(str, stringBuilder.toString());
                    return FULLHD;
                }
            }
        }

        private Size getMaxPreviewSize(List<Size> orderedPreviewSizes) {
            if (orderedPreviewSizes != null) {
                for (Size size : orderedPreviewSizes) {
                    if (this.mDisplaySize.getWidth() >= size.getWidth() && this.mDisplaySize.getWidth() >= size.getHeight()) {
                        return size;
                    }
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Camera ");
            stringBuilder.append(this.mCameraId);
            stringBuilder.append(" maximum preview size search failed with display size ");
            stringBuilder.append(this.mDisplaySize);
            Log.w(MandatoryStreamCombination.TAG, stringBuilder.toString());
            return this.kPreviewSizeBound;
        }

        private static int compareSizes(int widthA, int heightA, int widthB, int heightB) {
            long left = ((long) widthA) * ((long) heightA);
            long right = ((long) widthB) * ((long) heightB);
            if (left == right) {
                left = (long) widthA;
                right = (long) widthB;
            }
            if (left < right) {
                return -1;
            }
            return left > right ? 1 : 0;
        }

        private static List<Size> getAscendingOrderSizes(List<Size> sizeList, boolean ascending) {
            Comparator<Size> comparator = new SizeComparator();
            List<Size> sortedSizes = new ArrayList();
            sortedSizes.addAll(sizeList);
            Collections.sort(sortedSizes, comparator);
            if (!ascending) {
                Collections.reverse(sortedSizes);
            }
            return sortedSizes;
        }
    }

    public static final class MandatoryStreamInformation {
        private final ArrayList<Size> mAvailableSizes;
        private final int mFormat;
        private final boolean mIsInput;

        public MandatoryStreamInformation(List<Size> availableSizes, int format) {
            this(availableSizes, format, false);
        }

        public MandatoryStreamInformation(List<Size> availableSizes, int format, boolean isInput) {
            this.mAvailableSizes = new ArrayList();
            if (availableSizes.isEmpty()) {
                throw new IllegalArgumentException("No available sizes");
            }
            this.mAvailableSizes.addAll(availableSizes);
            this.mFormat = StreamConfigurationMap.checkArgumentFormat(format);
            this.mIsInput = isInput;
        }

        public boolean isInput() {
            return this.mIsInput;
        }

        public List<Size> getAvailableSizes() {
            return Collections.unmodifiableList(this.mAvailableSizes);
        }

        public int getFormat() {
            return this.mFormat;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof MandatoryStreamInformation)) {
                return false;
            }
            MandatoryStreamInformation other = (MandatoryStreamInformation) obj;
            if (this.mFormat == other.mFormat && this.mIsInput == other.mIsInput && this.mAvailableSizes.size() == other.mAvailableSizes.size()) {
                return this.mAvailableSizes.equals(other.mAvailableSizes);
            }
            return false;
        }

        public int hashCode() {
            return HashCodeHelpers.hashCode(this.mFormat, Boolean.hashCode(this.mIsInput), this.mAvailableSizes.hashCode());
        }
    }

    private enum ReprocessType {
        NONE,
        PRIVATE,
        YUV
    }

    private enum SizeThreshold {
        VGA,
        PREVIEW,
        RECORD,
        MAXIMUM
    }

    private static final class StreamCombinationTemplate {
        public String mDescription;
        public ReprocessType mReprocessType;
        public StreamTemplate[] mStreamTemplates;

        public StreamCombinationTemplate(StreamTemplate[] streamTemplates, String description) {
            this(streamTemplates, description, ReprocessType.NONE);
        }

        public StreamCombinationTemplate(StreamTemplate[] streamTemplates, String description, ReprocessType reprocessType) {
            this.mStreamTemplates = streamTemplates;
            this.mReprocessType = reprocessType;
            this.mDescription = description;
        }
    }

    private static final class StreamTemplate {
        public int mFormat;
        public boolean mIsInput;
        public SizeThreshold mSizeThreshold;

        public StreamTemplate(int format, SizeThreshold sizeThreshold) {
            this(format, sizeThreshold, false);
        }

        public StreamTemplate(int format, SizeThreshold sizeThreshold, boolean isInput) {
            this.mFormat = format;
            this.mSizeThreshold = sizeThreshold;
            this.mIsInput = isInput;
        }
    }

    public MandatoryStreamCombination(List<MandatoryStreamInformation> streamsInformation, String description, boolean isReprocessable) {
        if (streamsInformation.isEmpty()) {
            throw new IllegalArgumentException("Empty stream information");
        }
        this.mStreamsInformation.addAll(streamsInformation);
        this.mDescription = description;
        this.mIsReprocessable = isReprocessable;
    }

    public CharSequence getDescription() {
        return this.mDescription;
    }

    public boolean isReprocessable() {
        return this.mIsReprocessable;
    }

    public List<MandatoryStreamInformation> getStreamsInformation() {
        return Collections.unmodifiableList(this.mStreamsInformation);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MandatoryStreamCombination)) {
            return false;
        }
        MandatoryStreamCombination other = (MandatoryStreamCombination) obj;
        if (this.mDescription == other.mDescription && this.mIsReprocessable == other.mIsReprocessable && this.mStreamsInformation.size() == other.mStreamsInformation.size()) {
            return this.mStreamsInformation.equals(other.mStreamsInformation);
        }
        return false;
    }

    public int hashCode() {
        return HashCodeHelpers.hashCode(Boolean.hashCode(this.mIsReprocessable), this.mDescription.hashCode(), this.mStreamsInformation.hashCode());
    }

    static {
        r1 = new StreamCombinationTemplate[8];
        r1[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.MAXIMUM)}, "Simple preview, GPU video processing, or no-preview video recording");
        r1[1] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "No-viewfinder still image capture");
        r1[2] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.MAXIMUM)}, "In-application video/image processing");
        r1[3] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "Standard still imaging");
        r1[4] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "In-app processing plus still capture");
        r1[5] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(34, SizeThreshold.PREVIEW)}, "Standard recording");
        r1[6] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW)}, "Preview plus in-app processing");
        r1[7] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "Still capture plus in-app processing");
        sLegacyCombinations = r1;
        r1 = new StreamCombinationTemplate[6];
        r1[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(34, SizeThreshold.RECORD)}, "High-resolution video recording with preview");
        r1[1] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.RECORD)}, "High-resolution in-app video processing with preview");
        r1[2] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.RECORD)}, "Two-input in-app video processing");
        r1[3] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(34, SizeThreshold.RECORD), new StreamTemplate(256, SizeThreshold.RECORD)}, "High-resolution recording with video snapshot");
        r1[4] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.RECORD), new StreamTemplate(256, SizeThreshold.RECORD)}, "High-resolution in-app processing with video snapshot");
        r1[5] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "Two-input in-app processing with still capture");
        sLimitedCombinations = r1;
        r1 = new StreamCombinationTemplate[3];
        StreamTemplate[] streamTemplateArr = new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(34, SizeThreshold.MAXIMUM)};
        String str = "Maximum-resolution GPU processing with preview";
        r1[0] = new StreamCombinationTemplate(streamTemplateArr, str);
        streamTemplateArr = new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.MAXIMUM)};
        String str2 = "Maximum-resolution in-app processing with preview";
        r1[1] = new StreamCombinationTemplate(streamTemplateArr, str2);
        r1[2] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.MAXIMUM)}, "Maximum-resolution two-input in-app processsing");
        sBurstCombinations = r1;
        r1 = new StreamCombinationTemplate[6];
        r1[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.MAXIMUM), new StreamTemplate(34, SizeThreshold.MAXIMUM)}, str);
        r1[1] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.MAXIMUM), new StreamTemplate(35, SizeThreshold.MAXIMUM)}, str2);
        r1[2] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.MAXIMUM), new StreamTemplate(35, SizeThreshold.MAXIMUM)}, "Maximum-resolution two-input in-app processsing");
        r1[3] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "Video recording with maximum-size video snapshot");
        r1[4] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.VGA), new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.MAXIMUM)}, "Standard video recording plus maximum-resolution in-app processing");
        r1[5] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.VGA), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.MAXIMUM)}, "Preview plus two-input maximum-resolution in-app processing");
        sFullCombinations = r1;
        StreamCombinationTemplate[] streamCombinationTemplateArr = new StreamCombinationTemplate[8];
        streamCombinationTemplateArr[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "No-preview DNG capture");
        streamCombinationTemplateArr[1] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Standard DNG capture");
        streamCombinationTemplateArr[2] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "In-app processing plus DNG capture");
        streamCombinationTemplateArr[3] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Video recording with DNG capture");
        streamCombinationTemplateArr[4] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Preview with in-app processing and DNG capture");
        streamCombinationTemplateArr[5] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Two-input in-app processing plus DNG capture");
        streamCombinationTemplateArr[6] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Still capture with simultaneous JPEG and DNG");
        streamCombinationTemplateArr[7] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "In-app processing with simultaneous JPEG and DNG");
        sRawCombinations = streamCombinationTemplateArr;
        streamCombinationTemplateArr = new StreamCombinationTemplate[2];
        streamCombinationTemplateArr[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(34, SizeThreshold.VGA), new StreamTemplate(35, SizeThreshold.MAXIMUM), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "In-app viewfinder analysis with dynamic selection of output format");
        streamCombinationTemplateArr[1] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(34, SizeThreshold.VGA), new StreamTemplate(256, SizeThreshold.MAXIMUM), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "In-app viewfinder analysis with dynamic selection of output format");
        sLevel3Combinations = streamCombinationTemplateArr;
        streamCombinationTemplateArr = new StreamCombinationTemplate[4];
        streamCombinationTemplateArr[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "No-viewfinder still image reprocessing", ReprocessType.PRIVATE);
        streamCombinationTemplateArr[1] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "ZSL(Zero-Shutter-Lag) still imaging", ReprocessType.PRIVATE);
        streamCombinationTemplateArr[2] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "ZSL still and in-app processing imaging", ReprocessType.PRIVATE);
        streamCombinationTemplateArr[3] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "ZSL in-app processing with still capture", ReprocessType.PRIVATE);
        sLimitedPrivateReprocCombinations = streamCombinationTemplateArr;
        streamCombinationTemplateArr = new StreamCombinationTemplate[4];
        streamCombinationTemplateArr[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "No-viewfinder still image reprocessing", ReprocessType.YUV);
        streamCombinationTemplateArr[1] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "ZSL(Zero-Shutter-Lag) still imaging", ReprocessType.YUV);
        streamCombinationTemplateArr[2] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "ZSL still and in-app processing imaging", ReprocessType.YUV);
        streamCombinationTemplateArr[3] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "ZSL in-app processing with still capture", ReprocessType.YUV);
        sLimitedYUVReprocCombinations = streamCombinationTemplateArr;
        streamCombinationTemplateArr = new StreamCombinationTemplate[4];
        streamCombinationTemplateArr[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.RECORD)}, "High-resolution ZSL in-app video processing with regular preview", ReprocessType.PRIVATE);
        streamCombinationTemplateArr[1] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.MAXIMUM)}, "Maximum-resolution ZSL in-app processing with regular preview", ReprocessType.PRIVATE);
        streamCombinationTemplateArr[2] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.MAXIMUM)}, "Maximum-resolution two-input ZSL in-app processing", ReprocessType.PRIVATE);
        streamCombinationTemplateArr[3] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "ZSL still capture and in-app processing", ReprocessType.PRIVATE);
        sFullPrivateReprocCombinations = streamCombinationTemplateArr;
        streamCombinationTemplateArr = new StreamCombinationTemplate[4];
        streamCombinationTemplateArr[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW)}, "Maximum-resolution multi-frame image fusion in-app processing with regular preview", ReprocessType.YUV);
        streamCombinationTemplateArr[1] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW)}, "Maximum-resolution multi-frame image fusion two-input in-app processing", ReprocessType.YUV);
        streamCombinationTemplateArr[2] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.RECORD)}, "High-resolution ZSL in-app video processing with regular preview", ReprocessType.YUV);
        streamCombinationTemplateArr[3] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "ZSL still capture and in-app processing", ReprocessType.YUV);
        sFullYUVReprocCombinations = streamCombinationTemplateArr;
        r1 = new StreamCombinationTemplate[5];
        r1[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Mutually exclusive ZSL in-app processing and DNG capture", ReprocessType.PRIVATE);
        r1[1] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Mutually exclusive ZSL in-app processing and preview with DNG capture", ReprocessType.PRIVATE);
        r1[2] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Mutually exclusive ZSL two-input in-app processing and DNG capture", ReprocessType.PRIVATE);
        r1[3] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Mutually exclusive ZSL still capture and preview with DNG capture", ReprocessType.PRIVATE);
        r1[4] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Mutually exclusive ZSL in-app processing with still capture and DNG capture", ReprocessType.PRIVATE);
        sRAWPrivateReprocCombinations = r1;
        streamCombinationTemplateArr = new StreamCombinationTemplate[5];
        streamCombinationTemplateArr[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Mutually exclusive ZSL in-app processing and DNG capture", ReprocessType.YUV);
        streamCombinationTemplateArr[1] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Mutually exclusive ZSL in-app processing and preview with DNG capture", ReprocessType.YUV);
        streamCombinationTemplateArr[2] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Mutually exclusive ZSL two-input in-app processing and DNG capture", ReprocessType.YUV);
        streamCombinationTemplateArr[3] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Mutually exclusive ZSL still capture and preview with DNG capture", ReprocessType.YUV);
        streamCombinationTemplateArr[4] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(35, SizeThreshold.PREVIEW), new StreamTemplate(256, SizeThreshold.MAXIMUM), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "Mutually exclusive ZSL in-app processing with still capture and DNG capture", ReprocessType.YUV);
        sRAWYUVReprocCombinations = streamCombinationTemplateArr;
        streamCombinationTemplateArr = new StreamCombinationTemplate[1];
        streamCombinationTemplateArr[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(34, SizeThreshold.VGA), new StreamTemplate(32, SizeThreshold.MAXIMUM), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "In-app viewfinder analysis with ZSL, RAW, and JPEG reprocessing output", ReprocessType.PRIVATE);
        sLevel3PrivateReprocCombinations = streamCombinationTemplateArr;
        streamCombinationTemplateArr = new StreamCombinationTemplate[2];
        streamCombinationTemplateArr[0] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(34, SizeThreshold.VGA), new StreamTemplate(32, SizeThreshold.MAXIMUM)}, "In-app viewfinder analysis with ZSL and RAW", ReprocessType.YUV);
        streamCombinationTemplateArr[1] = new StreamCombinationTemplate(new StreamTemplate[]{new StreamTemplate(34, SizeThreshold.PREVIEW), new StreamTemplate(34, SizeThreshold.VGA), new StreamTemplate(32, SizeThreshold.MAXIMUM), new StreamTemplate(256, SizeThreshold.MAXIMUM)}, "In-app viewfinder analysis with ZSL, RAW, and JPEG reprocessing output", ReprocessType.YUV);
        sLevel3YUVReprocCombinations = streamCombinationTemplateArr;
    }
}
