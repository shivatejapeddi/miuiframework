package android.hardware.camera2.legacy;

import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.legacy.ParameterUtils.ZoomData;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.utils.ParamsUtils;
import android.location.Location;
import android.util.Log;
import android.util.Size;
import java.util.ArrayList;
import java.util.List;

public class LegacyResultMapper {
    private static final boolean DEBUG = false;
    private static final String TAG = "LegacyResultMapper";
    private LegacyRequest mCachedRequest = null;
    private CameraMetadataNative mCachedResult = null;

    public CameraMetadataNative cachedConvertResultMetadata(LegacyRequest legacyRequest, long timestamp) {
        CameraMetadataNative result;
        if (this.mCachedRequest != null && legacyRequest.parameters.same(this.mCachedRequest.parameters) && legacyRequest.captureRequest.equals(this.mCachedRequest.captureRequest)) {
            result = new CameraMetadataNative(this.mCachedResult);
        } else {
            result = convertResultMetadata(legacyRequest);
            this.mCachedRequest = legacyRequest;
            this.mCachedResult = new CameraMetadataNative(result);
        }
        result.set(CaptureResult.SENSOR_TIMESTAMP, Long.valueOf(timestamp));
        return result;
    }

    private static CameraMetadataNative convertResultMetadata(LegacyRequest legacyRequest) {
        CameraCharacteristics characteristics = legacyRequest.characteristics;
        CaptureRequest request = legacyRequest.captureRequest;
        Size previewSize = legacyRequest.previewSize;
        Parameters params = legacyRequest.parameters;
        CameraMetadataNative result = new CameraMetadataNative();
        Rect activeArraySize = (Rect) characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        ZoomData zoomData = ParameterUtils.convertScalerCropRegion(activeArraySize, (Rect) request.get(CaptureRequest.SCALER_CROP_REGION), previewSize, params);
        result.set(CaptureResult.COLOR_CORRECTION_ABERRATION_MODE, (Integer) request.get(CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE));
        mapAe(result, characteristics, request, activeArraySize, zoomData, params);
        mapAf(result, activeArraySize, zoomData, params);
        mapAwb(result, params);
        Key key = CaptureRequest.CONTROL_CAPTURE_INTENT;
        int i = 1;
        Object valueOf = Integer.valueOf(1);
        result.set(CaptureResult.CONTROL_CAPTURE_INTENT, Integer.valueOf(LegacyRequestMapper.filterSupportedCaptureIntent(((Integer) ParamsUtils.getOrDefault(request, key, valueOf)).intValue())));
        if (((Integer) ParamsUtils.getOrDefault(request, CaptureRequest.CONTROL_MODE, valueOf)).intValue() == 2) {
            result.set(CaptureResult.CONTROL_MODE, Integer.valueOf(2));
        } else {
            result.set(CaptureResult.CONTROL_MODE, valueOf);
        }
        String legacySceneMode = params.getSceneMode();
        int mode = LegacyMetadataMapper.convertSceneModeFromLegacy(legacySceneMode);
        String str = TAG;
        if (mode != -1) {
            result.set(CaptureResult.CONTROL_SCENE_MODE, Integer.valueOf(mode));
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown scene mode ");
            stringBuilder.append(legacySceneMode);
            stringBuilder.append(" returned by camera HAL, setting to disabled.");
            Log.w(str, stringBuilder.toString());
            result.set(CaptureResult.CONTROL_SCENE_MODE, Integer.valueOf(0));
        }
        legacySceneMode = params.getColorEffect();
        mode = LegacyMetadataMapper.convertEffectModeFromLegacy(legacySceneMode);
        if (mode != -1) {
            result.set(CaptureResult.CONTROL_EFFECT_MODE, Integer.valueOf(mode));
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unknown effect mode ");
            stringBuilder2.append(legacySceneMode);
            stringBuilder2.append(" returned by camera HAL, setting to off.");
            Log.w(str, stringBuilder2.toString());
            result.set(CaptureResult.CONTROL_EFFECT_MODE, Integer.valueOf(0));
        }
        if (!(params.isVideoStabilizationSupported() && params.getVideoStabilization())) {
            i = 0;
        }
        result.set(CaptureResult.CONTROL_VIDEO_STABILIZATION_MODE, Integer.valueOf(i));
        if (Parameters.FOCUS_MODE_INFINITY.equals(params.getFocusMode())) {
            result.set(CaptureResult.LENS_FOCUS_DISTANCE, Float.valueOf(0.0f));
        }
        result.set(CaptureResult.LENS_FOCAL_LENGTH, Float.valueOf(params.getFocalLength()));
        result.set(CaptureResult.REQUEST_PIPELINE_DEPTH, (Byte) characteristics.get(CameraCharacteristics.REQUEST_PIPELINE_MAX_DEPTH));
        mapScaler(result, zoomData, params);
        result.set(CaptureResult.SENSOR_TEST_PATTERN_MODE, Integer.valueOf(0));
        result.set(CaptureResult.JPEG_GPS_LOCATION, (Location) request.get(CaptureRequest.JPEG_GPS_LOCATION));
        result.set(CaptureResult.JPEG_ORIENTATION, (Integer) request.get(CaptureRequest.JPEG_ORIENTATION));
        result.set(CaptureResult.JPEG_QUALITY, Byte.valueOf((byte) params.getJpegQuality()));
        result.set(CaptureResult.JPEG_THUMBNAIL_QUALITY, Byte.valueOf((byte) params.getJpegThumbnailQuality()));
        Camera.Size s = params.getJpegThumbnailSize();
        if (s != null) {
            result.set(CaptureResult.JPEG_THUMBNAIL_SIZE, ParameterUtils.convertSize(s));
        } else {
            Log.w(str, "Null thumbnail size received from parameters.");
        }
        result.set(CaptureResult.NOISE_REDUCTION_MODE, (Integer) request.get(CaptureRequest.NOISE_REDUCTION_MODE));
        return result;
    }

    private static void mapAe(CameraMetadataNative m, CameraCharacteristics characteristics, CaptureRequest request, Rect activeArray, ZoomData zoomData, Parameters p) {
        m.set(CaptureResult.CONTROL_AE_ANTIBANDING_MODE, Integer.valueOf(LegacyMetadataMapper.convertAntiBandingModeOrDefault(p.getAntibanding())));
        m.set(CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION, Integer.valueOf(p.getExposureCompensation()));
        boolean lock = p.isAutoExposureLockSupported() ? p.getAutoExposureLock() : false;
        m.set(CaptureResult.CONTROL_AE_LOCK, Boolean.valueOf(lock));
        Boolean requestLock = (Boolean) request.get(CaptureRequest.CONTROL_AE_LOCK);
        if (!(requestLock == null || requestLock.booleanValue() == lock)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("mapAe - android.control.aeLock was requested to ");
            stringBuilder.append(requestLock);
            stringBuilder.append(" but resulted in ");
            stringBuilder.append(lock);
            Log.w(TAG, stringBuilder.toString());
        }
        mapAeAndFlashMode(m, characteristics, p);
        if (p.getMaxNumMeteringAreas() > 0) {
            m.set(CaptureResult.CONTROL_AE_REGIONS, getMeteringRectangles(activeArray, zoomData, p.getMeteringAreas(), "AE"));
        }
    }

    private static void mapAf(CameraMetadataNative m, Rect activeArray, ZoomData zoomData, Parameters p) {
        m.set(CaptureResult.CONTROL_AF_MODE, Integer.valueOf(convertLegacyAfMode(p.getFocusMode())));
        if (p.getMaxNumFocusAreas() > 0) {
            m.set(CaptureResult.CONTROL_AF_REGIONS, getMeteringRectangles(activeArray, zoomData, p.getFocusAreas(), "AF"));
        }
    }

    private static void mapAwb(CameraMetadataNative m, Parameters p) {
        m.set(CaptureResult.CONTROL_AWB_LOCK, Boolean.valueOf(p.isAutoWhiteBalanceLockSupported() ? p.getAutoWhiteBalanceLock() : false));
        m.set(CaptureResult.CONTROL_AWB_MODE, Integer.valueOf(convertLegacyAwbMode(p.getWhiteBalance())));
    }

    private static MeteringRectangle[] getMeteringRectangles(Rect activeArray, ZoomData zoomData, List<Area> meteringAreaList, String regionName) {
        List<MeteringRectangle> meteringRectList = new ArrayList();
        if (meteringAreaList != null) {
            for (Area area : meteringAreaList) {
                meteringRectList.add(ParameterUtils.convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, area).toMetering());
            }
        }
        return (MeteringRectangle[]) meteringRectList.toArray(new MeteringRectangle[0]);
    }

    /* JADX WARNING: Missing block: B:18:0x0051, code skipped:
            if (r4.equals("off") != false) goto L_0x0060;
     */
    private static void mapAeAndFlashMode(android.hardware.camera2.impl.CameraMetadataNative r11, android.hardware.camera2.CameraCharacteristics r12, android.hardware.Camera.Parameters r13) {
        /*
        r0 = 0;
        r1 = android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE;
        r1 = r12.get(r1);
        r1 = (java.lang.Boolean) r1;
        r1 = r1.booleanValue();
        r2 = 0;
        if (r1 == 0) goto L_0x0012;
    L_0x0010:
        r1 = 0;
        goto L_0x0016;
    L_0x0012:
        r1 = java.lang.Integer.valueOf(r2);
    L_0x0016:
        r3 = 1;
        r4 = r13.getFlashMode();
        if (r4 == 0) goto L_0x0096;
    L_0x001d:
        r5 = -1;
        r6 = r4.hashCode();
        r7 = 4;
        r8 = 2;
        r9 = 1;
        r10 = 3;
        switch(r6) {
            case 3551: goto L_0x0054;
            case 109935: goto L_0x004a;
            case 3005871: goto L_0x0040;
            case 110547964: goto L_0x0035;
            case 1081542389: goto L_0x002a;
            default: goto L_0x0029;
        };
    L_0x0029:
        goto L_0x005f;
    L_0x002a:
        r2 = "red-eye";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x0029;
    L_0x0033:
        r2 = r10;
        goto L_0x0060;
    L_0x0035:
        r2 = "torch";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x0029;
    L_0x003e:
        r2 = r7;
        goto L_0x0060;
    L_0x0040:
        r2 = "auto";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x0029;
    L_0x0048:
        r2 = r9;
        goto L_0x0060;
    L_0x004a:
        r6 = "off";
        r6 = r4.equals(r6);
        if (r6 == 0) goto L_0x0029;
    L_0x0053:
        goto L_0x0060;
    L_0x0054:
        r2 = "on";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x0029;
    L_0x005d:
        r2 = r8;
        goto L_0x0060;
    L_0x005f:
        r2 = r5;
    L_0x0060:
        if (r2 == 0) goto L_0x0096;
    L_0x0062:
        if (r2 == r9) goto L_0x0094;
    L_0x0064:
        if (r2 == r8) goto L_0x008d;
    L_0x0066:
        if (r2 == r10) goto L_0x008b;
    L_0x0068:
        if (r2 == r7) goto L_0x0085;
    L_0x006a:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r5 = "mapAeAndFlashMode - Ignoring unknown flash mode ";
        r2.append(r5);
        r5 = r13.getFlashMode();
        r2.append(r5);
        r2 = r2.toString();
        r5 = "LegacyResultMapper";
        android.util.Log.w(r5, r2);
        goto L_0x0096;
    L_0x0085:
        r0 = 2;
        r1 = java.lang.Integer.valueOf(r10);
        goto L_0x0096;
    L_0x008b:
        r3 = 4;
        goto L_0x0096;
    L_0x008d:
        r0 = 1;
        r3 = 3;
        r1 = java.lang.Integer.valueOf(r10);
        goto L_0x0096;
    L_0x0094:
        r3 = 2;
    L_0x0096:
        r2 = android.hardware.camera2.CaptureResult.FLASH_STATE;
        r11.set(r2, r1);
        r2 = android.hardware.camera2.CaptureResult.FLASH_MODE;
        r5 = java.lang.Integer.valueOf(r0);
        r11.set(r2, r5);
        r2 = android.hardware.camera2.CaptureResult.CONTROL_AE_MODE;
        r5 = java.lang.Integer.valueOf(r3);
        r11.set(r2, r5);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.LegacyResultMapper.mapAeAndFlashMode(android.hardware.camera2.impl.CameraMetadataNative, android.hardware.camera2.CameraCharacteristics, android.hardware.Camera$Parameters):void");
    }

    private static int convertLegacyAfMode(String mode) {
        String str = TAG;
        if (mode == null) {
            Log.w(str, "convertLegacyAfMode - no AF mode, default to OFF");
            return 0;
        }
        int i = -1;
        switch (mode.hashCode()) {
            case -194628547:
                if (mode.equals(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    i = 2;
                    break;
                }
                break;
            case 3005871:
                if (mode.equals("auto")) {
                    i = 0;
                    break;
                }
                break;
            case 3108534:
                if (mode.equals(Parameters.FOCUS_MODE_EDOF)) {
                    i = 3;
                    break;
                }
                break;
            case 97445748:
                if (mode.equals(Parameters.FOCUS_MODE_FIXED)) {
                    i = 5;
                    break;
                }
                break;
            case 103652300:
                if (mode.equals(Parameters.FOCUS_MODE_MACRO)) {
                    i = 4;
                    break;
                }
                break;
            case 173173288:
                if (mode.equals(Parameters.FOCUS_MODE_INFINITY)) {
                    i = 6;
                    break;
                }
                break;
            case 910005312:
                if (mode.equals(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    i = 1;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                return 1;
            case 1:
                return 4;
            case 2:
                return 3;
            case 3:
                return 5;
            case 4:
                return 2;
            case 5:
                return 0;
            case 6:
                return 0;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("convertLegacyAfMode - unknown mode ");
                stringBuilder.append(mode);
                stringBuilder.append(" , ignoring");
                Log.w(str, stringBuilder.toString());
                return 0;
        }
    }

    private static int convertLegacyAwbMode(String mode) {
        if (mode == null) {
            return 1;
        }
        int i = -1;
        switch (mode.hashCode()) {
            case -939299377:
                if (mode.equals(Parameters.WHITE_BALANCE_INCANDESCENT)) {
                    i = 1;
                    break;
                }
                break;
            case -719316704:
                if (mode.equals(Parameters.WHITE_BALANCE_WARM_FLUORESCENT)) {
                    i = 3;
                    break;
                }
                break;
            case 3005871:
                if (mode.equals("auto")) {
                    i = 0;
                    break;
                }
                break;
            case 109399597:
                if (mode.equals(Parameters.WHITE_BALANCE_SHADE)) {
                    i = 7;
                    break;
                }
                break;
            case 474934723:
                if (mode.equals(Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT)) {
                    i = 5;
                    break;
                }
                break;
            case 1650323088:
                if (mode.equals(Parameters.WHITE_BALANCE_TWILIGHT)) {
                    i = 6;
                    break;
                }
                break;
            case 1902580840:
                if (mode.equals(Parameters.WHITE_BALANCE_FLUORESCENT)) {
                    i = 2;
                    break;
                }
                break;
            case 1942983418:
                if (mode.equals(Parameters.WHITE_BALANCE_DAYLIGHT)) {
                    i = 4;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            case 7:
                return 8;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("convertAwbMode - unrecognized WB mode ");
                stringBuilder.append(mode);
                Log.w(TAG, stringBuilder.toString());
                return 1;
        }
    }

    private static void mapScaler(CameraMetadataNative m, ZoomData zoomData, Parameters p) {
        m.set(CaptureResult.SCALER_CROP_REGION, zoomData.reportedCrop);
    }
}
