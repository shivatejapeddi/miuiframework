package android.hardware.camera2.impl;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.MarshalRegistry;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.marshal.impl.MarshalQueryableArray;
import android.hardware.camera2.marshal.impl.MarshalQueryableBlackLevelPattern;
import android.hardware.camera2.marshal.impl.MarshalQueryableBoolean;
import android.hardware.camera2.marshal.impl.MarshalQueryableColorSpaceTransform;
import android.hardware.camera2.marshal.impl.MarshalQueryableEnum;
import android.hardware.camera2.marshal.impl.MarshalQueryableHighSpeedVideoConfiguration;
import android.hardware.camera2.marshal.impl.MarshalQueryableMeteringRectangle;
import android.hardware.camera2.marshal.impl.MarshalQueryableNativeByteToInteger;
import android.hardware.camera2.marshal.impl.MarshalQueryablePair;
import android.hardware.camera2.marshal.impl.MarshalQueryableParcelable;
import android.hardware.camera2.marshal.impl.MarshalQueryablePrimitive;
import android.hardware.camera2.marshal.impl.MarshalQueryableRange;
import android.hardware.camera2.marshal.impl.MarshalQueryableRecommendedStreamConfiguration;
import android.hardware.camera2.marshal.impl.MarshalQueryableRect;
import android.hardware.camera2.marshal.impl.MarshalQueryableReprocessFormatsMap;
import android.hardware.camera2.marshal.impl.MarshalQueryableRggbChannelVector;
import android.hardware.camera2.marshal.impl.MarshalQueryableSize;
import android.hardware.camera2.marshal.impl.MarshalQueryableSizeF;
import android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfiguration;
import android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfigurationDuration;
import android.hardware.camera2.marshal.impl.MarshalQueryableString;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.HighSpeedVideoConfiguration;
import android.hardware.camera2.params.LensShadingMap;
import android.hardware.camera2.params.MandatoryStreamCombination;
import android.hardware.camera2.params.MandatoryStreamCombination.Builder;
import android.hardware.camera2.params.OisSample;
import android.hardware.camera2.params.RecommendedStreamConfiguration;
import android.hardware.camera2.params.RecommendedStreamConfigurationMap;
import android.hardware.camera2.params.ReprocessFormatsMap;
import android.hardware.camera2.params.StreamConfiguration;
import android.hardware.camera2.params.StreamConfigurationDuration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.params.TonemapCurve;
import android.hardware.camera2.utils.TypeReference;
import android.location.Location;
import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.ServiceSpecificException;
import android.util.Log;
import android.util.Size;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CameraMetadataNative implements Parcelable {
    private static final String CELLID_PROCESS = "CELLID";
    public static final Creator<CameraMetadataNative> CREATOR = new Creator<CameraMetadataNative>() {
        public CameraMetadataNative createFromParcel(Parcel in) {
            CameraMetadataNative metadata = new CameraMetadataNative();
            metadata.readFromParcel(in);
            return metadata;
        }

        public CameraMetadataNative[] newArray(int size) {
            return new CameraMetadataNative[size];
        }
    };
    private static final boolean DEBUG = false;
    private static final int FACE_LANDMARK_SIZE = 6;
    private static final String GPS_PROCESS = "GPS";
    public static final int NATIVE_JPEG_FORMAT = 33;
    public static final int NUM_TYPES = 6;
    private static final String TAG = "CameraMetadataJV";
    public static final int TYPE_BYTE = 0;
    public static final int TYPE_DOUBLE = 4;
    public static final int TYPE_FLOAT = 2;
    public static final int TYPE_INT32 = 1;
    public static final int TYPE_INT64 = 3;
    public static final int TYPE_RATIONAL = 5;
    private static final HashMap<Key<?>, GetCommand> sGetCommandMap = new HashMap();
    private static final HashMap<Key<?>, SetCommand> sSetCommandMap = new HashMap();
    private int mCameraId;
    private Size mDisplaySize;
    @UnsupportedAppUsage
    private long mMetadataPtr;

    public static class Key<T> {
        private final String mFallbackName;
        private boolean mHasTag;
        private final int mHash;
        private final String mName;
        private int mTag;
        private final Class<T> mType;
        private final TypeReference<T> mTypeReference;
        private long mVendorId = Long.MAX_VALUE;

        public Key(String name, Class<T> type, long vendorId) {
            if (name == null) {
                throw new NullPointerException("Key needs a valid name");
            } else if (type != null) {
                this.mName = name;
                this.mFallbackName = null;
                this.mType = type;
                this.mVendorId = vendorId;
                this.mTypeReference = TypeReference.createSpecializedTypeReference((Class) type);
                this.mHash = this.mName.hashCode() ^ this.mTypeReference.hashCode();
            } else {
                throw new NullPointerException("Type needs to be non-null");
            }
        }

        public Key(String name, String fallbackName, Class<T> type) {
            if (name == null) {
                throw new NullPointerException("Key needs a valid name");
            } else if (type != null) {
                this.mName = name;
                this.mFallbackName = fallbackName;
                this.mType = type;
                this.mTypeReference = TypeReference.createSpecializedTypeReference((Class) type);
                this.mHash = this.mName.hashCode() ^ this.mTypeReference.hashCode();
            } else {
                throw new NullPointerException("Type needs to be non-null");
            }
        }

        public Key(String name, Class<T> type) {
            if (name == null) {
                throw new NullPointerException("Key needs a valid name");
            } else if (type != null) {
                this.mName = name;
                this.mFallbackName = null;
                this.mType = type;
                this.mTypeReference = TypeReference.createSpecializedTypeReference((Class) type);
                this.mHash = this.mName.hashCode() ^ this.mTypeReference.hashCode();
            } else {
                throw new NullPointerException("Type needs to be non-null");
            }
        }

        public Key(String name, TypeReference<T> typeReference) {
            if (name == null) {
                throw new NullPointerException("Key needs a valid name");
            } else if (typeReference != null) {
                this.mName = name;
                this.mFallbackName = null;
                this.mType = typeReference.getRawType();
                this.mTypeReference = typeReference;
                this.mHash = this.mName.hashCode() ^ this.mTypeReference.hashCode();
            } else {
                throw new NullPointerException("TypeReference needs to be non-null");
            }
        }

        public final String getName() {
            return this.mName;
        }

        public final int hashCode() {
            return this.mHash;
        }

        public final boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o == null || hashCode() != o.hashCode()) {
                return false;
            }
            Key<?> lhs;
            if (o instanceof android.hardware.camera2.CaptureResult.Key) {
                lhs = ((android.hardware.camera2.CaptureResult.Key) o).getNativeKey();
            } else if (o instanceof android.hardware.camera2.CaptureRequest.Key) {
                lhs = ((android.hardware.camera2.CaptureRequest.Key) o).getNativeKey();
            } else if (o instanceof android.hardware.camera2.CameraCharacteristics.Key) {
                lhs = ((android.hardware.camera2.CameraCharacteristics.Key) o).getNativeKey();
            } else if (!(o instanceof Key)) {
                return false;
            } else {
                lhs = (Key) o;
            }
            if (!(this.mName.equals(lhs.mName) && this.mTypeReference.equals(lhs.mTypeReference))) {
                z = false;
            }
            return z;
        }

        @UnsupportedAppUsage
        public final int getTag() {
            if (!this.mHasTag) {
                this.mTag = CameraMetadataNative.getTag(this.mName, this.mVendorId);
                this.mHasTag = true;
            }
            return this.mTag;
        }

        public final Class<T> getType() {
            return this.mType;
        }

        public final long getVendorId() {
            return this.mVendorId;
        }

        public final TypeReference<T> getTypeReference() {
            return this.mTypeReference;
        }
    }

    private class StreamConfigurationData {
        StreamConfigurationDuration[] minDurationArray;
        StreamConfigurationDuration[] stallDurationArray;
        StreamConfiguration[] streamConfigurationArray;

        private StreamConfigurationData() {
            this.streamConfigurationArray = null;
            this.minDurationArray = null;
            this.stallDurationArray = null;
        }

        /* synthetic */ StreamConfigurationData(CameraMetadataNative x0, AnonymousClass1 x1) {
            this();
        }
    }

    private native long nativeAllocate();

    private native long nativeAllocateCopy(CameraMetadataNative cameraMetadataNative) throws NullPointerException;

    private native synchronized void nativeClose();

    private native synchronized void nativeDump() throws IOException;

    private native synchronized ArrayList nativeGetAllVendorKeys(Class cls);

    private native synchronized int nativeGetEntryCount();

    private static native int nativeGetTagFromKey(String str, long j) throws IllegalArgumentException;

    @UnsupportedAppUsage
    private native synchronized int nativeGetTagFromKeyLocal(String str) throws IllegalArgumentException;

    private static native int nativeGetTypeFromTag(int i, long j) throws IllegalArgumentException;

    @UnsupportedAppUsage
    private native synchronized int nativeGetTypeFromTagLocal(int i) throws IllegalArgumentException;

    private native synchronized boolean nativeIsEmpty();

    private native synchronized void nativeReadFromParcel(Parcel parcel);

    @UnsupportedAppUsage
    private native synchronized byte[] nativeReadValues(int i);

    private static native int nativeSetupGlobalVendorTagDescriptor();

    private native synchronized void nativeSwap(CameraMetadataNative cameraMetadataNative) throws NullPointerException;

    private native synchronized void nativeWriteToParcel(Parcel parcel);

    private native synchronized void nativeWriteValues(int i, byte[] bArr);

    private static String translateLocationProviderToProcess(String provider) {
        if (provider == null) {
            return null;
        }
        Object obj = -1;
        int hashCode = provider.hashCode();
        if (hashCode != 102570) {
            if (hashCode == 1843485230 && provider.equals(LocationManager.NETWORK_PROVIDER)) {
                obj = 1;
            }
        } else if (provider.equals(LocationManager.GPS_PROVIDER)) {
            obj = null;
        }
        if (obj == null) {
            return GPS_PROCESS;
        }
        if (obj != 1) {
            return null;
        }
        return CELLID_PROCESS;
    }

    private static String translateProcessToLocationProvider(String process) {
        if (process == null) {
            return null;
        }
        Object obj = -1;
        int hashCode = process.hashCode();
        if (hashCode != 70794) {
            if (hashCode == 1984215549 && process.equals(CELLID_PROCESS)) {
                obj = 1;
            }
        } else if (process.equals(GPS_PROCESS)) {
            obj = null;
        }
        if (obj == null) {
            return LocationManager.GPS_PROVIDER;
        }
        if (obj != 1) {
            return null;
        }
        return LocationManager.NETWORK_PROVIDER;
    }

    public CameraMetadataNative() {
        this.mCameraId = -1;
        this.mDisplaySize = new Size(0, 0);
        this.mMetadataPtr = nativeAllocate();
        if (this.mMetadataPtr == 0) {
            throw new OutOfMemoryError("Failed to allocate native CameraMetadata");
        }
    }

    public CameraMetadataNative(CameraMetadataNative other) {
        this.mCameraId = -1;
        this.mDisplaySize = new Size(0, 0);
        this.mMetadataPtr = nativeAllocateCopy(other);
        if (this.mMetadataPtr == 0) {
            throw new OutOfMemoryError("Failed to allocate native CameraMetadata");
        }
    }

    public static CameraMetadataNative move(CameraMetadataNative other) {
        CameraMetadataNative newObject = new CameraMetadataNative();
        newObject.swap(other);
        return newObject;
    }

    static {
        sGetCommandMap.put(CameraCharacteristics.SCALER_AVAILABLE_FORMATS.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getAvailableFormats();
            }
        });
        sGetCommandMap.put(CaptureResult.STATISTICS_FACES.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getFaces();
            }
        });
        sGetCommandMap.put(CaptureResult.STATISTICS_FACE_RECTANGLES.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getFaceRectangles();
            }
        });
        sGetCommandMap.put(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getStreamConfigurationMap();
            }
        });
        sGetCommandMap.put(CameraCharacteristics.SCALER_MANDATORY_STREAM_COMBINATIONS.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getMandatoryStreamCombinations();
            }
        });
        sGetCommandMap.put(CameraCharacteristics.CONTROL_MAX_REGIONS_AE.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getMaxRegions(key);
            }
        });
        sGetCommandMap.put(CameraCharacteristics.CONTROL_MAX_REGIONS_AWB.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getMaxRegions(key);
            }
        });
        sGetCommandMap.put(CameraCharacteristics.CONTROL_MAX_REGIONS_AF.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getMaxRegions(key);
            }
        });
        sGetCommandMap.put(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_RAW.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getMaxNumOutputs(key);
            }
        });
        sGetCommandMap.put(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getMaxNumOutputs(key);
            }
        });
        sGetCommandMap.put(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC_STALLING.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getMaxNumOutputs(key);
            }
        });
        sGetCommandMap.put(CaptureRequest.TONEMAP_CURVE.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getTonemapCurve();
            }
        });
        sGetCommandMap.put(CaptureResult.JPEG_GPS_LOCATION.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getGpsLocation();
            }
        });
        sGetCommandMap.put(CaptureResult.STATISTICS_LENS_SHADING_CORRECTION_MAP.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getLensShadingMap();
            }
        });
        sGetCommandMap.put(CaptureResult.STATISTICS_OIS_SAMPLES.getNativeKey(), new GetCommand() {
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return metadata.getOisSamples();
            }
        });
        sSetCommandMap.put(CameraCharacteristics.SCALER_AVAILABLE_FORMATS.getNativeKey(), new SetCommand() {
            public <T> void setValue(CameraMetadataNative metadata, T value) {
                metadata.setAvailableFormats((int[]) value);
            }
        });
        sSetCommandMap.put(CaptureResult.STATISTICS_FACE_RECTANGLES.getNativeKey(), new SetCommand() {
            public <T> void setValue(CameraMetadataNative metadata, T value) {
                metadata.setFaceRectangles((Rect[]) value);
            }
        });
        sSetCommandMap.put(CaptureResult.STATISTICS_FACES.getNativeKey(), new SetCommand() {
            public <T> void setValue(CameraMetadataNative metadata, T value) {
                metadata.setFaces((Face[]) value);
            }
        });
        sSetCommandMap.put(CaptureRequest.TONEMAP_CURVE.getNativeKey(), new SetCommand() {
            public <T> void setValue(CameraMetadataNative metadata, T value) {
                metadata.setTonemapCurve((TonemapCurve) value);
            }
        });
        sSetCommandMap.put(CaptureResult.JPEG_GPS_LOCATION.getNativeKey(), new SetCommand() {
            public <T> void setValue(CameraMetadataNative metadata, T value) {
                metadata.setGpsLocation((Location) value);
            }
        });
        registerAllMarshalers();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        nativeWriteToParcel(dest);
    }

    public <T> T get(android.hardware.camera2.CameraCharacteristics.Key<T> key) {
        return get(key.getNativeKey());
    }

    public <T> T get(android.hardware.camera2.CaptureResult.Key<T> key) {
        return get(key.getNativeKey());
    }

    public <T> T get(android.hardware.camera2.CaptureRequest.Key<T> key) {
        return get(key.getNativeKey());
    }

    public <T> T get(Key<T> key) {
        Preconditions.checkNotNull(key, "key must not be null");
        GetCommand g = (GetCommand) sGetCommandMap.get(key);
        if (g != null) {
            return g.getValue(this, key);
        }
        return getBase((Key) key);
    }

    public void readFromParcel(Parcel in) {
        nativeReadFromParcel(in);
    }

    public static void setupGlobalVendorTagDescriptor() throws ServiceSpecificException {
        int err = nativeSetupGlobalVendorTagDescriptor();
        if (err != 0) {
            throw new ServiceSpecificException(err, "Failure to set up global vendor tags");
        }
    }

    public <T> void set(Key<T> key, T value) {
        SetCommand s = (SetCommand) sSetCommandMap.get(key);
        if (s != null) {
            s.setValue(this, value);
        } else {
            setBase((Key) key, (Object) value);
        }
    }

    public <T> void set(android.hardware.camera2.CaptureRequest.Key<T> key, T value) {
        set(key.getNativeKey(), (Object) value);
    }

    public <T> void set(android.hardware.camera2.CaptureResult.Key<T> key, T value) {
        set(key.getNativeKey(), (Object) value);
    }

    public <T> void set(android.hardware.camera2.CameraCharacteristics.Key<T> key, T value) {
        set(key.getNativeKey(), (Object) value);
    }

    private void close() {
        nativeClose();
        this.mMetadataPtr = 0;
    }

    private <T> T getBase(android.hardware.camera2.CameraCharacteristics.Key<T> key) {
        return getBase(key.getNativeKey());
    }

    private <T> T getBase(android.hardware.camera2.CaptureResult.Key<T> key) {
        return getBase(key.getNativeKey());
    }

    private <T> T getBase(android.hardware.camera2.CaptureRequest.Key<T> key) {
        return getBase(key.getNativeKey());
    }

    private <T> T getBase(Key<T> key) {
        int tag = nativeGetTagFromKeyLocal(key.getName());
        byte[] values = readValues(tag);
        if (values == null) {
            if (key.mFallbackName == null) {
                return null;
            }
            tag = nativeGetTagFromKeyLocal(key.mFallbackName);
            values = readValues(tag);
            if (values == null) {
                return null;
            }
        }
        return getMarshalerForKey(key, nativeGetTypeFromTagLocal(tag)).unmarshal(ByteBuffer.wrap(values).order(ByteOrder.nativeOrder()));
    }

    private int[] getAvailableFormats() {
        int[] availableFormats = (int[]) getBase(CameraCharacteristics.SCALER_AVAILABLE_FORMATS);
        if (availableFormats != null) {
            for (int i = 0; i < availableFormats.length; i++) {
                if (availableFormats[i] == 33) {
                    availableFormats[i] = 256;
                }
            }
        }
        return availableFormats;
    }

    private boolean setFaces(Face[] faces) {
        int i = 0;
        if (faces == null) {
            return false;
        }
        int numFaces = faces.length;
        boolean fullMode = true;
        int numFaces2 = numFaces;
        for (Face face : faces) {
            if (face == null) {
                numFaces2--;
                Log.w(TAG, "setFaces - null face detected, skipping");
            } else if (face.getId() == -1) {
                fullMode = false;
            }
        }
        Object faceRectangles = new Rect[numFaces2];
        Object faceScores = new byte[numFaces2];
        Object faceIds = null;
        Object faceLandmarks = null;
        if (fullMode) {
            faceIds = new int[numFaces2];
            faceLandmarks = new int[(numFaces2 * 6)];
        }
        int i2 = 0;
        int length = faces.length;
        while (i < length) {
            Face face2 = faces[i];
            if (face2 != null) {
                faceRectangles[i2] = face2.getBounds();
                faceScores[i2] = (byte) face2.getScore();
                if (fullMode) {
                    faceIds[i2] = face2.getId();
                    int j = 0 + 1;
                    faceLandmarks[(i2 * 6) + 0] = face2.getLeftEyePosition().x;
                    int j2 = j + 1;
                    faceLandmarks[(i2 * 6) + j] = face2.getLeftEyePosition().y;
                    j = j2 + 1;
                    faceLandmarks[(i2 * 6) + j2] = face2.getRightEyePosition().x;
                    j2 = j + 1;
                    faceLandmarks[(i2 * 6) + j] = face2.getRightEyePosition().y;
                    j = j2 + 1;
                    faceLandmarks[(i2 * 6) + j2] = face2.getMouthPosition().x;
                    j2 = j + 1;
                    faceLandmarks[(i2 * 6) + j] = face2.getMouthPosition().y;
                }
                i2++;
            }
            i++;
        }
        set(CaptureResult.STATISTICS_FACE_RECTANGLES, faceRectangles);
        set(CaptureResult.STATISTICS_FACE_IDS, faceIds);
        set(CaptureResult.STATISTICS_FACE_LANDMARKS, faceLandmarks);
        set(CaptureResult.STATISTICS_FACE_SCORES, faceScores);
        return true;
    }

    private Face[] getFaces() {
        Integer faceDetectMode = (Integer) get(CaptureResult.STATISTICS_FACE_DETECT_MODE);
        byte[] faceScores = (byte[]) get(CaptureResult.STATISTICS_FACE_SCORES);
        Rect[] faceRectangles = (Rect[]) get(CaptureResult.STATISTICS_FACE_RECTANGLES);
        int[] faceIds = (int[]) get(CaptureResult.STATISTICS_FACE_IDS);
        int[] faceLandmarks = (int[]) get(CaptureResult.STATISTICS_FACE_LANDMARKS);
        r7 = new Object[5];
        byte b = (byte) 1;
        r7[1] = faceScores;
        r7[2] = faceRectangles;
        r7[3] = faceIds;
        r7[4] = faceLandmarks;
        if (areValuesAllNull(r7)) {
            return null;
        }
        String str = TAG;
        if (faceDetectMode == null) {
            Log.w(str, "Face detect mode metadata is null, assuming the mode is SIMPLE");
            faceDetectMode = Integer.valueOf(1);
        } else if (faceDetectMode.intValue() > 2) {
            faceDetectMode = Integer.valueOf(2);
        } else if (faceDetectMode.intValue() == 0) {
            return new Face[0];
        } else {
            if (!(faceDetectMode.intValue() == 1 || faceDetectMode.intValue() == 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown face detect mode: ");
                stringBuilder.append(faceDetectMode);
                Log.w(str, stringBuilder.toString());
                return new Face[0];
            }
        }
        if (faceScores == null || faceRectangles == null) {
            Log.w(str, "Expect face scores and rectangles to be non-null");
            return new Face[0];
        }
        if (faceScores.length != faceRectangles.length) {
            Log.w(str, String.format("Face score size(%d) doesn match face rectangle size(%d)!", new Object[]{Integer.valueOf(faceScores.length), Integer.valueOf(faceRectangles.length)}));
        }
        int numFaces = Math.min(faceScores.length, faceRectangles.length);
        if (faceDetectMode.intValue() == 2) {
            if (faceIds == null || faceLandmarks == null) {
                Log.w(str, "Expect face ids and landmarks to be non-null for FULL mode,fallback to SIMPLE mode");
                faceDetectMode = Integer.valueOf(1);
            } else {
                if (!(faceIds.length == numFaces && faceLandmarks.length == numFaces * 6)) {
                    Log.w(str, String.format("Face id size(%d), or face landmark size(%d) don'tmatch face number(%d)!", new Object[]{Integer.valueOf(faceIds.length), Integer.valueOf(faceLandmarks.length * 6), Integer.valueOf(numFaces)}));
                }
                numFaces = Math.min(Math.min(numFaces, faceIds.length), faceLandmarks.length / 6);
            }
        }
        ArrayList<Face> faceList = new ArrayList();
        byte b2 = (byte) 100;
        if (faceDetectMode.intValue() == 1) {
            int i = 0;
            while (i < numFaces) {
                if (faceScores[i] <= (byte) 100 && faceScores[i] >= (byte) 1) {
                    faceList.add(new Face(faceRectangles[i], faceScores[i]));
                }
                i++;
            }
        } else {
            int i2 = 0;
            while (i2 < numFaces) {
                if (faceScores[i2] <= b2 && faceScores[i2] >= b && faceIds[i2] >= 0) {
                    faceList.add(new Face(faceRectangles[i2], faceScores[i2], faceIds[i2], new Point(faceLandmarks[i2 * 6], faceLandmarks[(i2 * 6) + 1]), new Point(faceLandmarks[(i2 * 6) + 2], faceLandmarks[(i2 * 6) + 3]), new Point(faceLandmarks[(i2 * 6) + 4], faceLandmarks[(i2 * 6) + 5])));
                }
                i2++;
                b = (byte) 1;
                b2 = (byte) 100;
            }
        }
        Face[] faces = new Face[faceList.size()];
        faceList.toArray(faces);
        return faces;
    }

    private Rect[] getFaceRectangles() {
        Rect[] faceRectangles = (Rect[]) getBase(CaptureResult.STATISTICS_FACE_RECTANGLES);
        if (faceRectangles == null) {
            return null;
        }
        Rect[] fixedFaceRectangles = new Rect[faceRectangles.length];
        for (int i = 0; i < faceRectangles.length; i++) {
            fixedFaceRectangles[i] = new Rect(faceRectangles[i].left, faceRectangles[i].top, faceRectangles[i].right - faceRectangles[i].left, faceRectangles[i].bottom - faceRectangles[i].top);
        }
        return fixedFaceRectangles;
    }

    private LensShadingMap getLensShadingMap() {
        float[] lsmArray = (float[]) getBase(CaptureResult.STATISTICS_LENS_SHADING_MAP);
        Size s = (Size) get(CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE);
        if (lsmArray == null) {
            return null;
        }
        if (s != null) {
            return new LensShadingMap(lsmArray, s.getHeight(), s.getWidth());
        }
        Log.w(TAG, "getLensShadingMap - Lens shading map size was null.");
        return null;
    }

    private Location getGpsLocation() {
        double[] coords = (double[]) get(CaptureResult.JPEG_GPS_COORDINATES);
        Long timeStamp = (Long) get(CaptureResult.JPEG_GPS_TIMESTAMP);
        if (areValuesAllNull((String) get(CaptureResult.JPEG_GPS_PROCESSING_METHOD), coords, timeStamp)) {
            return null;
        }
        Location l = new Location(translateProcessToLocationProvider((String) get(CaptureResult.JPEG_GPS_PROCESSING_METHOD)));
        String str = TAG;
        if (timeStamp != null) {
            l.setTime(timeStamp.longValue() * 1000);
        } else {
            Log.w(str, "getGpsLocation - No timestamp for GPS location.");
        }
        if (coords != null) {
            l.setLatitude(coords[0]);
            l.setLongitude(coords[1]);
            l.setAltitude(coords[2]);
        } else {
            Log.w(str, "getGpsLocation - No coordinates for GPS location");
        }
        return l;
    }

    private boolean setGpsLocation(Location l) {
        if (l == null) {
            return false;
        }
        Object coords = new double[]{l.getLatitude(), l.getLongitude(), l.getAltitude()};
        Object processMethod = translateLocationProviderToProcess(l.getProvider());
        set(CaptureRequest.JPEG_GPS_TIMESTAMP, Long.valueOf(l.getTime() / 1000));
        set(CaptureRequest.JPEG_GPS_COORDINATES, coords);
        if (processMethod == null) {
            Log.w(TAG, "setGpsLocation - No process method, Location is not from a GPS or NETWORKprovider");
        } else {
            setBase(CaptureRequest.JPEG_GPS_PROCESSING_METHOD, processMethod);
        }
        return true;
    }

    private void parseRecommendedConfigurations(RecommendedStreamConfiguration[] configurations, StreamConfigurationMap fullMap, boolean isDepth, ArrayList<ArrayList<StreamConfiguration>> streamConfigList, ArrayList<ArrayList<StreamConfigurationDuration>> streamDurationList, ArrayList<ArrayList<StreamConfigurationDuration>> streamStallList, boolean[] supportsPrivate) {
        int i;
        RecommendedStreamConfiguration[] recommendedStreamConfigurationArr = configurations;
        StreamConfigurationMap streamConfigurationMap = fullMap;
        ArrayList<ArrayList<StreamConfiguration>> arrayList = streamConfigList;
        ArrayList<ArrayList<StreamConfigurationDuration>> arrayList2 = streamDurationList;
        ArrayList<ArrayList<StreamConfigurationDuration>> arrayList3 = streamStallList;
        arrayList.ensureCapacity(32);
        arrayList2.ensureCapacity(32);
        arrayList3.ensureCapacity(32);
        for (i = 0; i < 32; i++) {
            arrayList.add(new ArrayList());
            arrayList2.add(new ArrayList());
            arrayList3.add(new ArrayList());
        }
        i = recommendedStreamConfigurationArr.length;
        boolean z = false;
        int i2 = 0;
        while (i2 < i) {
            int depthFormatToPublic;
            int i3;
            Object obj;
            RecommendedStreamConfiguration c = recommendedStreamConfigurationArr[i2];
            int width = c.getWidth();
            int height = c.getHeight();
            int internalFormat = c.getFormat();
            if (isDepth) {
                depthFormatToPublic = StreamConfigurationMap.depthFormatToPublic(internalFormat);
            } else {
                depthFormatToPublic = StreamConfigurationMap.imageFormatToPublic(internalFormat);
            }
            int publicFormat = depthFormatToPublic;
            Size sz = new Size(width, height);
            depthFormatToPublic = c.getUsecaseBitmap();
            int internalFormat2;
            int width2;
            int height2;
            if (!c.isInput()) {
                int usecaseBitmap;
                Size sz2;
                StreamConfigurationDuration minDurationConfiguration;
                StreamConfigurationDuration minDurationConfiguration2;
                StreamConfiguration streamConfiguration = new StreamConfiguration(internalFormat, width, height, z);
                long minFrameDuration = streamConfigurationMap.getOutputMinFrameDuration(publicFormat, sz);
                if (minFrameDuration > 0) {
                    usecaseBitmap = depthFormatToPublic;
                    sz2 = sz;
                    i3 = i;
                    i = publicFormat;
                    internalFormat2 = internalFormat;
                    width2 = width;
                    height2 = height;
                    minDurationConfiguration = new StreamConfigurationDuration(internalFormat, width, height, minFrameDuration);
                } else {
                    i3 = i;
                    usecaseBitmap = depthFormatToPublic;
                    sz2 = sz;
                    i = publicFormat;
                    internalFormat2 = internalFormat;
                    width2 = width;
                    height2 = height;
                    minDurationConfiguration = null;
                }
                long stallDuration = streamConfigurationMap.getOutputStallDuration(i, sz2);
                if (stallDuration > 0) {
                    minDurationConfiguration2 = minDurationConfiguration;
                    depthFormatToPublic = new StreamConfigurationDuration(internalFormat2, width2, height2, stallDuration);
                } else {
                    minDurationConfiguration2 = minDurationConfiguration;
                    depthFormatToPublic = null;
                }
                sz = null;
                while (true) {
                    obj = 32;
                    if (sz >= 32) {
                        break;
                    }
                    if ((usecaseBitmap & (1 << sz)) != 0) {
                        ((ArrayList) arrayList.get(sz)).add(streamConfiguration);
                        if (minFrameDuration > 0) {
                            ((ArrayList) arrayList2.get(sz)).add(minDurationConfiguration2);
                        }
                        if (stallDuration > 0) {
                            ((ArrayList) arrayList3.get(sz)).add(depthFormatToPublic);
                        }
                        if (!(supportsPrivate == null || supportsPrivate[sz] || i != 34)) {
                            supportsPrivate[sz] = 1;
                        }
                    }
                    sz++;
                }
            } else {
                i3 = i;
                Size size = sz;
                i = publicFormat;
                internalFormat2 = internalFormat;
                width2 = width;
                height2 = height;
                obj = 32;
                if (depthFormatToPublic == 16) {
                    ((ArrayList) arrayList.get(4)).add(new StreamConfiguration(internalFormat2, width2, height2, true));
                } else {
                    throw new IllegalArgumentException("Recommended input stream configurations should only be advertised in the ZSL use case!");
                }
            }
            i2++;
            recommendedStreamConfigurationArr = configurations;
            Object obj2 = obj;
            i = i3;
            z = false;
        }
    }

    public void initializeStreamConfigurationData(ArrayList<StreamConfiguration> sc, ArrayList<StreamConfigurationDuration> scd, ArrayList<StreamConfigurationDuration> scs, StreamConfigurationData scData) {
        if (scData != null && sc != null) {
            scData.streamConfigurationArray = new StreamConfiguration[sc.size()];
            scData.streamConfigurationArray = (StreamConfiguration[]) sc.toArray(scData.streamConfigurationArray);
            if (scd == null || scd.isEmpty()) {
                scData.minDurationArray = new StreamConfigurationDuration[0];
            } else {
                scData.minDurationArray = new StreamConfigurationDuration[scd.size()];
                scData.minDurationArray = (StreamConfigurationDuration[]) scd.toArray(scData.minDurationArray);
            }
            if (scs == null || scs.isEmpty()) {
                scData.stallDurationArray = new StreamConfigurationDuration[0];
            } else {
                scData.stallDurationArray = new StreamConfigurationDuration[scs.size()];
                scData.stallDurationArray = (StreamConfigurationDuration[]) scs.toArray(scData.stallDurationArray);
            }
        }
    }

    public ArrayList<RecommendedStreamConfigurationMap> getRecommendedStreamConfigurations() {
        String str;
        RecommendedStreamConfiguration[] configurations = (RecommendedStreamConfiguration[]) getBase(CameraCharacteristics.SCALER_AVAILABLE_RECOMMENDED_STREAM_CONFIGURATIONS);
        RecommendedStreamConfiguration[] depthConfigurations = (RecommendedStreamConfiguration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_RECOMMENDED_DEPTH_STREAM_CONFIGURATIONS);
        if (configurations == null && depthConfigurations == null) {
            return null;
        }
        boolean[] supportsPrivate;
        ArrayList<ArrayList<StreamConfigurationDuration>> streamStallList;
        ArrayList<ArrayList<StreamConfigurationDuration>> streamDurationList;
        ArrayList<ArrayList<StreamConfigurationDuration>> depthStreamStallList;
        ArrayList<ArrayList<StreamConfigurationDuration>> depthStreamDurationList;
        ArrayList<ArrayList<StreamConfiguration>> depthStreamConfigList;
        StreamConfigurationMap fullMap = getStreamConfigurationMap();
        ArrayList<RecommendedStreamConfigurationMap> recommendedConfigurations = new ArrayList();
        ArrayList<ArrayList<StreamConfiguration>> streamConfigList = new ArrayList();
        ArrayList<ArrayList<StreamConfigurationDuration>> streamDurationList2 = new ArrayList();
        ArrayList<ArrayList<StreamConfigurationDuration>> streamStallList2 = new ArrayList();
        int i = 32;
        boolean[] supportsPrivate2 = new boolean[32];
        String str2 = TAG;
        if (configurations != null) {
            str = str2;
            supportsPrivate = supportsPrivate2;
            streamStallList = streamStallList2;
            streamDurationList = streamDurationList2;
            try {
                parseRecommendedConfigurations(configurations, fullMap, false, streamConfigList, streamDurationList2, streamStallList2, supportsPrivate);
            } catch (IllegalArgumentException e) {
                Log.e(str, "Failed parsing the recommended stream configurations!");
                return null;
            }
        }
        str = str2;
        supportsPrivate = supportsPrivate2;
        streamStallList = streamStallList2;
        streamDurationList = streamDurationList2;
        ArrayList<ArrayList<StreamConfiguration>> depthStreamConfigList2 = new ArrayList();
        streamStallList2 = new ArrayList();
        ArrayList<ArrayList<StreamConfigurationDuration>> depthStreamStallList2 = new ArrayList();
        if (depthConfigurations != null) {
            StreamConfigurationMap streamConfigurationMap = fullMap;
            depthStreamStallList = depthStreamStallList2;
            depthStreamDurationList = streamStallList2;
            depthStreamConfigList = depthStreamConfigList2;
            try {
                parseRecommendedConfigurations(depthConfigurations, streamConfigurationMap, true, depthStreamConfigList2, streamStallList2, depthStreamStallList, null);
            } catch (IllegalArgumentException illegalArgumentException) {
                IllegalArgumentException illegalArgumentException2 = illegalArgumentException2;
                Log.e(str, "Failed parsing the recommended depth stream configurations!");
                return null;
            }
        }
        depthStreamStallList = depthStreamStallList2;
        depthStreamDurationList = streamStallList2;
        StreamConfigurationMap streamConfigurationMap2 = fullMap;
        depthStreamConfigList = depthStreamConfigList2;
        ReprocessFormatsMap inputOutputFormatsMap = (ReprocessFormatsMap) getBase(CameraCharacteristics.SCALER_AVAILABLE_RECOMMENDED_INPUT_OUTPUT_FORMATS_MAP);
        HighSpeedVideoConfiguration[] highSpeedVideoConfigurations = (HighSpeedVideoConfiguration[]) getBase(CameraCharacteristics.CONTROL_AVAILABLE_HIGH_SPEED_VIDEO_CONFIGURATIONS);
        boolean listHighResolution = isBurstSupported();
        recommendedConfigurations.ensureCapacity(32);
        int i2 = 0;
        while (i2 < i) {
            ArrayList<ArrayList<StreamConfigurationDuration>> streamStallList3;
            ArrayList<ArrayList<StreamConfigurationDuration>> streamDurationList3;
            StreamConfigurationData scData = new StreamConfigurationData(this, null);
            if (configurations != null) {
                streamStallList2 = streamDurationList;
                streamStallList3 = streamStallList;
                initializeStreamConfigurationData((ArrayList) streamConfigList.get(i2), (ArrayList) streamStallList2.get(i2), (ArrayList) streamStallList3.get(i2), scData);
            } else {
                streamStallList3 = streamStallList;
                streamStallList2 = streamDurationList;
            }
            StreamConfigurationData depthScData = new StreamConfigurationData(this, null);
            if (depthConfigurations != null) {
                streamDurationList3 = streamStallList2;
                initializeStreamConfigurationData((ArrayList) depthStreamConfigList.get(i2), (ArrayList) depthStreamDurationList.get(i2), (ArrayList) depthStreamStallList.get(i2), depthScData);
            } else {
                streamDurationList3 = streamStallList2;
            }
            if ((scData.streamConfigurationArray == null || scData.streamConfigurationArray.length == 0) && (depthScData.streamConfigurationArray == null || depthScData.streamConfigurationArray.length == 0)) {
                recommendedConfigurations.add(null);
            } else {
                StreamConfigurationMap map;
                if (i2 != 0) {
                    if (i2 == 1) {
                        map = new StreamConfigurationMap(scData.streamConfigurationArray, scData.minDurationArray, scData.stallDurationArray, null, null, null, null, null, null, null, null, null, highSpeedVideoConfigurations, null, listHighResolution, supportsPrivate[i2]);
                    } else if (i2 != 2) {
                        if (i2 == 4) {
                            map = new StreamConfigurationMap(scData.streamConfigurationArray, scData.minDurationArray, scData.stallDurationArray, depthScData.streamConfigurationArray, depthScData.minDurationArray, depthScData.stallDurationArray, null, null, null, null, null, null, null, inputOutputFormatsMap, listHighResolution, supportsPrivate[i2]);
                        } else if (!(i2 == 5 || i2 == 6)) {
                            map = new StreamConfigurationMap(scData.streamConfigurationArray, scData.minDurationArray, scData.stallDurationArray, depthScData.streamConfigurationArray, depthScData.minDurationArray, depthScData.stallDurationArray, null, null, null, null, null, null, null, null, listHighResolution, supportsPrivate[i2]);
                        }
                    }
                    recommendedConfigurations.add(new RecommendedStreamConfigurationMap(map, i2, supportsPrivate[i2]));
                }
                map = new StreamConfigurationMap(scData.streamConfigurationArray, scData.minDurationArray, scData.stallDurationArray, null, null, null, null, null, null, null, null, null, null, null, listHighResolution, supportsPrivate[i2]);
                recommendedConfigurations.add(new RecommendedStreamConfigurationMap(map, i2, supportsPrivate[i2]));
            }
            i2++;
            streamStallList = streamStallList3;
            streamDurationList = streamDurationList3;
            i = 32;
        }
        return recommendedConfigurations;
    }

    private boolean isBurstSupported() {
        for (int capability : (int[]) getBase(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)) {
            if (capability == 6) {
                return true;
            }
        }
        return false;
    }

    private MandatoryStreamCombination[] getMandatoryStreamCombinations() {
        int[] capabilities = (int[]) getBase(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
        ArrayList<Integer> caps = new ArrayList();
        caps.ensureCapacity(capabilities.length);
        for (int c : capabilities) {
            caps.add(new Integer(c));
        }
        List<MandatoryStreamCombination> combs = new Builder(this.mCameraId, ((Integer) getBase(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue(), this.mDisplaySize, caps, getStreamConfigurationMap()).getAvailableMandatoryStreamCombinations();
        if (combs == null || combs.isEmpty()) {
            return null;
        }
        return (MandatoryStreamCombination[]) combs.toArray(new MandatoryStreamCombination[combs.size()]);
    }

    private StreamConfigurationMap getStreamConfigurationMap() {
        return new StreamConfigurationMap((StreamConfiguration[]) getBase(CameraCharacteristics.SCALER_AVAILABLE_STREAM_CONFIGURATIONS), (StreamConfigurationDuration[]) getBase(CameraCharacteristics.SCALER_AVAILABLE_MIN_FRAME_DURATIONS), (StreamConfigurationDuration[]) getBase(CameraCharacteristics.SCALER_AVAILABLE_STALL_DURATIONS), (StreamConfiguration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_STREAM_CONFIGURATIONS), (StreamConfigurationDuration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_MIN_FRAME_DURATIONS), (StreamConfigurationDuration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_STALL_DURATIONS), (StreamConfiguration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_DYNAMIC_DEPTH_STREAM_CONFIGURATIONS), (StreamConfigurationDuration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_DYNAMIC_DEPTH_MIN_FRAME_DURATIONS), (StreamConfigurationDuration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_DYNAMIC_DEPTH_STALL_DURATIONS), (StreamConfiguration[]) getBase(CameraCharacteristics.HEIC_AVAILABLE_HEIC_STREAM_CONFIGURATIONS), (StreamConfigurationDuration[]) getBase(CameraCharacteristics.HEIC_AVAILABLE_HEIC_MIN_FRAME_DURATIONS), (StreamConfigurationDuration[]) getBase(CameraCharacteristics.HEIC_AVAILABLE_HEIC_STALL_DURATIONS), (HighSpeedVideoConfiguration[]) getBase(CameraCharacteristics.CONTROL_AVAILABLE_HIGH_SPEED_VIDEO_CONFIGURATIONS), (ReprocessFormatsMap) getBase(CameraCharacteristics.SCALER_AVAILABLE_INPUT_OUTPUT_FORMATS_MAP), isBurstSupported());
    }

    private <T> Integer getMaxRegions(Key<T> key) {
        int[] maxRegions = (int[]) getBase(CameraCharacteristics.CONTROL_MAX_REGIONS);
        if (maxRegions == null) {
            return null;
        }
        if (key.equals(CameraCharacteristics.CONTROL_MAX_REGIONS_AE)) {
            return Integer.valueOf(maxRegions[0]);
        }
        if (key.equals(CameraCharacteristics.CONTROL_MAX_REGIONS_AWB)) {
            return Integer.valueOf(maxRegions[1]);
        }
        if (key.equals(CameraCharacteristics.CONTROL_MAX_REGIONS_AF)) {
            return Integer.valueOf(maxRegions[2]);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid key ");
        stringBuilder.append(key);
        throw new AssertionError(stringBuilder.toString());
    }

    private <T> Integer getMaxNumOutputs(Key<T> key) {
        int[] maxNumOutputs = (int[]) getBase(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_STREAMS);
        if (maxNumOutputs == null) {
            return null;
        }
        if (key.equals(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_RAW)) {
            return Integer.valueOf(maxNumOutputs[0]);
        }
        if (key.equals(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC)) {
            return Integer.valueOf(maxNumOutputs[1]);
        }
        if (key.equals(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC_STALLING)) {
            return Integer.valueOf(maxNumOutputs[2]);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid key ");
        stringBuilder.append(key);
        throw new AssertionError(stringBuilder.toString());
    }

    private <T> TonemapCurve getTonemapCurve() {
        float[] red = (float[]) getBase(CaptureRequest.TONEMAP_CURVE_RED);
        float[] green = (float[]) getBase(CaptureRequest.TONEMAP_CURVE_GREEN);
        float[] blue = (float[]) getBase(CaptureRequest.TONEMAP_CURVE_BLUE);
        if (areValuesAllNull(red, green, blue)) {
            return null;
        }
        if (red != null && green != null && blue != null) {
            return new TonemapCurve(red, green, blue);
        }
        Log.w(TAG, "getTonemapCurve - missing tone curve components");
        return null;
    }

    private OisSample[] getOisSamples() {
        long[] timestamps = (long[]) getBase(CaptureResult.STATISTICS_OIS_TIMESTAMPS);
        float[] xShifts = (float[]) getBase(CaptureResult.STATISTICS_OIS_X_SHIFTS);
        float[] yShifts = (float[]) getBase(CaptureResult.STATISTICS_OIS_Y_SHIFTS);
        if (timestamps == null) {
            if (xShifts != null) {
                throw new AssertionError("timestamps is null but xShifts is not");
            } else if (yShifts == null) {
                return null;
            } else {
                throw new AssertionError("timestamps is null but yShifts is not");
            }
        } else if (xShifts == null) {
            throw new AssertionError("timestamps is not null but xShifts is");
        } else if (yShifts == null) {
            throw new AssertionError("timestamps is not null but yShifts is");
        } else if (xShifts.length != timestamps.length) {
            throw new AssertionError(String.format("timestamps has %d entries but xShifts has %d", new Object[]{Integer.valueOf(timestamps.length), Integer.valueOf(xShifts.length)}));
        } else if (yShifts.length == timestamps.length) {
            OisSample[] samples = new OisSample[timestamps.length];
            for (int i = 0; i < timestamps.length; i++) {
                samples[i] = new OisSample(timestamps[i], xShifts[i], yShifts[i]);
            }
            return samples;
        } else {
            throw new AssertionError(String.format("timestamps has %d entries but yShifts has %d", new Object[]{Integer.valueOf(timestamps.length), Integer.valueOf(yShifts.length)}));
        }
    }

    private <T> void setBase(android.hardware.camera2.CameraCharacteristics.Key<T> key, T value) {
        setBase(key.getNativeKey(), (Object) value);
    }

    private <T> void setBase(android.hardware.camera2.CaptureResult.Key<T> key, T value) {
        setBase(key.getNativeKey(), (Object) value);
    }

    private <T> void setBase(android.hardware.camera2.CaptureRequest.Key<T> key, T value) {
        setBase(key.getNativeKey(), (Object) value);
    }

    private <T> void setBase(Key<T> key, T value) {
        int tag = nativeGetTagFromKeyLocal(key.getName());
        if (value == null) {
            writeValues(tag, null);
            return;
        }
        Marshaler<T> marshaler = getMarshalerForKey(key, nativeGetTypeFromTagLocal(tag));
        byte[] values = new byte[marshaler.calculateMarshalSize(value)];
        marshaler.marshal(value, ByteBuffer.wrap(values).order(ByteOrder.nativeOrder()));
        writeValues(tag, values);
    }

    private boolean setAvailableFormats(int[] value) {
        int[] availableFormat = value;
        if (value == null) {
            return false;
        }
        Object newValues = new int[availableFormat.length];
        for (int i = 0; i < availableFormat.length; i++) {
            newValues[i] = availableFormat[i];
            if (availableFormat[i] == 256) {
                newValues[i] = 33;
            }
        }
        setBase(CameraCharacteristics.SCALER_AVAILABLE_FORMATS, newValues);
        return true;
    }

    private boolean setFaceRectangles(Rect[] faceRects) {
        if (faceRects == null) {
            return false;
        }
        Object newFaceRects = new Rect[faceRects.length];
        for (int i = 0; i < newFaceRects.length; i++) {
            newFaceRects[i] = new Rect(faceRects[i].left, faceRects[i].top, faceRects[i].right + faceRects[i].left, faceRects[i].bottom + faceRects[i].top);
        }
        setBase(CaptureResult.STATISTICS_FACE_RECTANGLES, newFaceRects);
        return true;
    }

    private <T> boolean setTonemapCurve(TonemapCurve tc) {
        if (tc == null) {
            return false;
        }
        float[][] curve = new float[3][];
        for (int i = 0; i <= 2; i++) {
            curve[i] = new float[(tc.getPointCount(i) * 2)];
            tc.copyColorCurve(i, curve[i], 0);
        }
        setBase(CaptureRequest.TONEMAP_CURVE_RED, curve[0]);
        setBase(CaptureRequest.TONEMAP_CURVE_GREEN, curve[1]);
        setBase(CaptureRequest.TONEMAP_CURVE_BLUE, curve[2]);
        return true;
    }

    public void setCameraId(int cameraId) {
        this.mCameraId = cameraId;
    }

    public void setDisplaySize(Size displaySize) {
        this.mDisplaySize = displaySize;
    }

    public void swap(CameraMetadataNative other) {
        nativeSwap(other);
        this.mCameraId = other.mCameraId;
        this.mDisplaySize = other.mDisplaySize;
    }

    public int getEntryCount() {
        return nativeGetEntryCount();
    }

    public boolean isEmpty() {
        return nativeIsEmpty();
    }

    public <K> ArrayList<K> getAllVendorKeys(Class<K> keyClass) {
        if (keyClass != null) {
            return nativeGetAllVendorKeys(keyClass);
        }
        throw new NullPointerException();
    }

    public static int getTag(String key) {
        return nativeGetTagFromKey(key, Long.MAX_VALUE);
    }

    public static int getTag(String key, long vendorId) {
        return nativeGetTagFromKey(key, vendorId);
    }

    public static int getNativeType(int tag, long vendorId) {
        return nativeGetTypeFromTag(tag, vendorId);
    }

    public void writeValues(int tag, byte[] src) {
        nativeWriteValues(tag, src);
    }

    public byte[] readValues(int tag) {
        return nativeReadValues(tag);
    }

    public void dumpToLog() {
        try {
            nativeDump();
        } catch (IOException e) {
            Log.wtf(TAG, "Dump logging failed", e);
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private static <T> Marshaler<T> getMarshalerForKey(Key<T> key, int nativeType) {
        return MarshalRegistry.getMarshaler(key.getTypeReference(), nativeType);
    }

    private static void registerAllMarshalers() {
        queryList = new MarshalQueryable[21];
        int i = 0;
        queryList[0] = new MarshalQueryablePrimitive();
        queryList[1] = new MarshalQueryableEnum();
        queryList[2] = new MarshalQueryableArray();
        queryList[3] = new MarshalQueryableBoolean();
        queryList[4] = new MarshalQueryableNativeByteToInteger();
        queryList[5] = new MarshalQueryableRect();
        queryList[6] = new MarshalQueryableSize();
        queryList[7] = new MarshalQueryableSizeF();
        queryList[8] = new MarshalQueryableString();
        queryList[9] = new MarshalQueryableReprocessFormatsMap();
        queryList[10] = new MarshalQueryableRange();
        queryList[11] = new MarshalQueryablePair();
        queryList[12] = new MarshalQueryableMeteringRectangle();
        queryList[13] = new MarshalQueryableColorSpaceTransform();
        queryList[14] = new MarshalQueryableStreamConfiguration();
        queryList[15] = new MarshalQueryableStreamConfigurationDuration();
        queryList[16] = new MarshalQueryableRggbChannelVector();
        queryList[17] = new MarshalQueryableBlackLevelPattern();
        queryList[18] = new MarshalQueryableHighSpeedVideoConfiguration();
        queryList[19] = new MarshalQueryableRecommendedStreamConfiguration();
        queryList[20] = new MarshalQueryableParcelable();
        int length = queryList.length;
        while (i < length) {
            MarshalRegistry.registerMarshalQueryable(queryList[i]);
            i++;
        }
    }

    private static boolean areValuesAllNull(Object... objs) {
        for (Object o : objs) {
            if (o != null) {
                return false;
            }
        }
        return true;
    }
}
