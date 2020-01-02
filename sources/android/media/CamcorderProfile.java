package android.media;

import android.annotation.UnsupportedAppUsage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

public class CamcorderProfile {
    public static final int QUALITY_1080P = 6;
    public static final int QUALITY_2160P = 8;
    public static final int QUALITY_2K = 12;
    public static final int QUALITY_480P = 4;
    public static final int QUALITY_4KDCI = 10;
    public static final int QUALITY_720P = 5;
    public static final int QUALITY_8KUHD = 3001;
    public static final int QUALITY_CIF = 3;
    public static final int QUALITY_HIGH = 1;
    public static final int QUALITY_HIGH_SPEED_1080P = 2004;
    public static final int QUALITY_HIGH_SPEED_2160P = 2005;
    public static final int QUALITY_HIGH_SPEED_480P = 2002;
    public static final int QUALITY_HIGH_SPEED_4KDCI = 2008;
    public static final int QUALITY_HIGH_SPEED_720P = 2003;
    public static final int QUALITY_HIGH_SPEED_CIF = 2006;
    public static final int QUALITY_HIGH_SPEED_HIGH = 2001;
    private static final int QUALITY_HIGH_SPEED_LIST_END = 2008;
    private static final int QUALITY_HIGH_SPEED_LIST_START = 2000;
    public static final int QUALITY_HIGH_SPEED_LOW = 2000;
    public static final int QUALITY_HIGH_SPEED_VGA = 2007;
    private static final int QUALITY_LIST_END = 12;
    private static final int QUALITY_LIST_START = 0;
    public static final int QUALITY_LOW = 0;
    public static final int QUALITY_QCIF = 2;
    public static final int QUALITY_QHD = 11;
    public static final int QUALITY_QVGA = 7;
    public static final int QUALITY_TIME_LAPSE_1080P = 1006;
    public static final int QUALITY_TIME_LAPSE_2160P = 1008;
    public static final int QUALITY_TIME_LAPSE_2K = 1012;
    public static final int QUALITY_TIME_LAPSE_480P = 1004;
    public static final int QUALITY_TIME_LAPSE_4KDCI = 1010;
    public static final int QUALITY_TIME_LAPSE_720P = 1005;
    public static final int QUALITY_TIME_LAPSE_8KUHD = 3002;
    public static final int QUALITY_TIME_LAPSE_CIF = 1003;
    public static final int QUALITY_TIME_LAPSE_HIGH = 1001;
    private static final int QUALITY_TIME_LAPSE_LIST_END = 1012;
    private static final int QUALITY_TIME_LAPSE_LIST_START = 1000;
    public static final int QUALITY_TIME_LAPSE_LOW = 1000;
    public static final int QUALITY_TIME_LAPSE_QCIF = 1002;
    public static final int QUALITY_TIME_LAPSE_QHD = 1011;
    public static final int QUALITY_TIME_LAPSE_QVGA = 1007;
    public static final int QUALITY_TIME_LAPSE_VGA = 1009;
    private static final int QUALITY_VENDOR_LIST_END = 3002;
    private static final int QUALITY_VENDOR_LIST_START = 3001;
    public static final int QUALITY_VGA = 9;
    public int audioBitRate;
    public int audioChannels;
    public int audioCodec;
    public int audioSampleRate;
    public int duration;
    public int fileFormat;
    public int quality;
    public int videoBitRate;
    public int videoCodec;
    public int videoFrameHeight;
    public int videoFrameRate;
    public int videoFrameWidth;

    @UnsupportedAppUsage
    private static final native CamcorderProfile native_get_camcorder_profile(int i, int i2);

    private static final native boolean native_has_camcorder_profile(int i, int i2);

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static final native void native_init();

    public static CamcorderProfile get(int quality) {
        int numberOfCameras = Camera.getNumberOfCameras();
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == 0) {
                return get(i, quality);
            }
        }
        return null;
    }

    public static CamcorderProfile get(int cameraId, int quality) {
        if ((quality >= 0 && quality <= 12) || ((quality >= 1000 && quality <= 1012) || ((quality >= 2000 && quality <= 2008) || (quality >= 3001 && quality <= 3002)))) {
            return native_get_camcorder_profile(cameraId, quality);
        }
        String errMessage = new StringBuilder();
        errMessage.append("Unsupported quality level: ");
        errMessage.append(quality);
        throw new IllegalArgumentException(errMessage.toString());
    }

    public static boolean hasProfile(int quality) {
        int numberOfCameras = Camera.getNumberOfCameras();
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == 0) {
                return hasProfile(i, quality);
            }
        }
        return false;
    }

    public static boolean hasProfile(int cameraId, int quality) {
        return native_has_camcorder_profile(cameraId, quality);
    }

    static {
        System.loadLibrary("media_jni");
        native_init();
    }

    private CamcorderProfile(int duration, int quality, int fileFormat, int videoCodec, int videoBitRate, int videoFrameRate, int videoWidth, int videoHeight, int audioCodec, int audioBitRate, int audioSampleRate, int audioChannels) {
        this.duration = duration;
        this.quality = quality;
        this.fileFormat = fileFormat;
        this.videoCodec = videoCodec;
        this.videoBitRate = videoBitRate;
        this.videoFrameRate = videoFrameRate;
        this.videoFrameWidth = videoWidth;
        this.videoFrameHeight = videoHeight;
        this.audioCodec = audioCodec;
        this.audioBitRate = audioBitRate;
        this.audioSampleRate = audioSampleRate;
        this.audioChannels = audioChannels;
    }
}
