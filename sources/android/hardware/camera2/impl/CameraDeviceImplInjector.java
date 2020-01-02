package android.hardware.camera2.impl;

import android.hardware.CameraInjector;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraCharacteristics.Key;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.HighSpeedVideoConfiguration;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.ReprocessFormatsMap;
import android.hardware.camera2.params.StreamConfiguration;
import android.hardware.camera2.params.StreamConfigurationDuration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;
import android.util.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class CameraDeviceImplInjector {
    private static final String TAG = CameraDeviceImplInjector.class.getSimpleName();
    private static final Key<StreamConfiguration[]> XIAOMI_SCALER_AVAILABLE_STREAM_CONFIGURATIONS = new Key("xiaomi.scaler.availableStreamConfigurations", StreamConfiguration[].class);

    public static boolean isInputConfigurationFormatValid(CameraCharacteristics characteristics, InputConfiguration inputConfig) {
        if (characteristics == null || inputConfig == null) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Null ");
            stringBuilder.append(characteristics == null ? "CameraCharacteristics" : "InputConfiguration");
            Log.e(str, stringBuilder.toString());
            return false;
        } else if (isFormatValid(getStreamConfigurationMap(characteristics, getAllVendorKeys(characteristics)), inputConfig)) {
            Log.d(TAG, String.format(Locale.ENGLISH, "valid format: %d", new Object[]{Integer.valueOf(inputConfig.getFormat())}));
            return true;
        } else {
            Log.w(TAG, String.format(Locale.ENGLISH, "invalid format: %d", new Object[]{Integer.valueOf(inputConfig.getFormat())}));
            return false;
        }
    }

    private static boolean isFormatValid(StreamConfigurationMap configMap, InputConfiguration inputConfig) {
        if (configMap != null) {
            int[] inputFormats = configMap.getInputFormats();
            if (inputFormats != null) {
                for (int format : inputFormats) {
                    if (format == inputConfig.getFormat()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isInputConfigurationSizeValid(CameraCharacteristics characteristics, InputConfiguration inputConfig) {
        if (characteristics == null || inputConfig == null) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Null ");
            stringBuilder.append(characteristics == null ? "CameraCharacteristics" : "InputConfiguration");
            Log.e(str, stringBuilder.toString());
            return false;
        } else if (isSizeValid(getStreamConfigurationMap(characteristics, getAllVendorKeys(characteristics)), inputConfig)) {
            Log.d(TAG, String.format(Locale.ENGLISH, "valid size: %dx%d", new Object[]{Integer.valueOf(inputConfig.getWidth()), Integer.valueOf(inputConfig.getHeight())}));
            return true;
        } else {
            Log.w(TAG, String.format(Locale.ENGLISH, "invalid size: %dx%d", new Object[]{Integer.valueOf(inputConfig.getWidth()), Integer.valueOf(inputConfig.getHeight())}));
            return false;
        }
    }

    private static boolean isSizeValid(StreamConfigurationMap configMap, InputConfiguration inputConfig) {
        if (configMap != null) {
            Size[] inputSizes = configMap.getInputSizes(inputConfig.getFormat());
            if (inputSizes != null) {
                for (Size size : inputSizes) {
                    if (inputConfig.getWidth() == size.getWidth() && inputConfig.getHeight() == size.getHeight()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static StreamConfigurationMap getStreamConfigurationMap(CameraCharacteristics characteristics, HashSet<String> vendorKeys) {
        CameraCharacteristics cameraCharacteristics = characteristics;
        if (vendorKeys.contains(XIAOMI_SCALER_AVAILABLE_STREAM_CONFIGURATIONS.getName())) {
            return new StreamConfigurationMap((StreamConfiguration[]) cameraCharacteristics.get(XIAOMI_SCALER_AVAILABLE_STREAM_CONFIGURATIONS), (StreamConfigurationDuration[]) cameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MIN_FRAME_DURATIONS), (StreamConfigurationDuration[]) cameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_STALL_DURATIONS), (StreamConfiguration[]) cameraCharacteristics.get(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_STREAM_CONFIGURATIONS), (StreamConfigurationDuration[]) cameraCharacteristics.get(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_MIN_FRAME_DURATIONS), (StreamConfigurationDuration[]) cameraCharacteristics.get(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_STALL_DURATIONS), null, null, null, null, null, null, (HighSpeedVideoConfiguration[]) cameraCharacteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_HIGH_SPEED_VIDEO_CONFIGURATIONS), (ReprocessFormatsMap) cameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_INPUT_OUTPUT_FORMATS_MAP), true);
        }
        return null;
    }

    private static HashSet<String> getAllVendorKeys(CameraCharacteristics characteristics) {
        List<CaptureRequest.Key> vendorKeys = characteristics.getNativeCopy().getAllVendorKeys(CaptureRequest.Key.class);
        HashSet<String> vendorNames = new HashSet(vendorKeys.size());
        for (CaptureRequest.Key key : vendorKeys) {
            vendorNames.add(key.getName());
        }
        return vendorNames;
    }

    public static void initCamera(CameraDeviceImpl cameraDevice, String cameraId) {
        CameraInjector.notifyCameraStateChange(true);
    }

    public static void releaseCamera(CameraDeviceImpl cameraDevice, String cameraId) {
        CameraInjector.notifyCameraStateChange(false);
    }
}
