package com.android.qualcomm.qti.seccamapi;

import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

public class SecureSurface {
    public static final int FLIP_HORIZONTALLY = 1;
    public static final int FLIP_VERTICALLY = 2;
    public static final int IMAGE_FORMAT_PRIVATE = 34;
    public static final int IMAGE_FORMAT_RAW = 36;
    public static final int IMAGE_FORMAT_RAW10 = 37;
    public static final int IMAGE_FORMAT_RAW_SENSOR = 32;
    public static final int IMAGE_FORMAT_YUV420 = 35;
    public static final int IMAGE_FORMAT_YUV420SP = 17;
    public static final int IMAGE_FORMAT_YUV420SP_UBWC = 2141391878;
    public static final int NO_ROTATION = 0;
    public static final int ROTATE_180 = 3;
    public static final int ROTATE_270 = 7;
    public static final int ROTATE_90 = 4;
    public static final int ROTATE_90_HORIZONTAL_FLIP = 5;
    public static final int ROTATE_90_VERTICAL_FLIP = 6;
    protected static final String SECCAM_API_LOG_TAG = "SECCAM-API";
    private int cameraId_ = -1;
    protected SurfaceInfo captureSurfaceInfo_ = null;
    protected int imageFormat_;
    private Callback previewSerfaceCallback_ = null;
    protected SurfaceHolder previewSurfaceHolder_ = null;

    public interface FrameCallback {
        void onSecureFrameAvalable(FrameInfo frameInfo, byte[] bArr);
    }

    public static class FrameInfo {
        public int format_ = 0;
        public long frameNumber_ = 0;
        public int height_ = 0;
        public int stride_ = 0;
        public long timeStamp_ = 0;
        public int width_ = 0;
    }

    public static class SurfaceInfo {
        private long surfaceId_ = 0;
        private Surface surface_ = null;

        public SurfaceInfo(Surface surface, Long surfaceId) {
            this.surface_ = surface;
            this.surfaceId_ = surfaceId.longValue();
        }

        public Surface getSurface() {
            return this.surface_;
        }

        public Long getSurfaceId() {
            return Long.valueOf(this.surfaceId_);
        }
    }

    protected SecureSurface(int cameraId, int width, int height, int format, int numOfBuffers) {
        StringBuilder stringBuilder;
        String str = "SecureSurface::SecureSurface - ERROR: ";
        String str2 = SECCAM_API_LOG_TAG;
        try {
            this.cameraId_ = cameraId;
            this.imageFormat_ = format;
            this.captureSurfaceInfo_ = SecCamServiceClient.getInstance().getSecureCameraSurface(cameraId, width, height, format, numOfBuffers);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("SecureSurface::SecureSurface - CaptureSurface:");
            stringBuilder2.append(this.captureSurfaceInfo_.getSurface());
            stringBuilder2.append(", surfaceId: ");
            stringBuilder2.append(this.captureSurfaceInfo_.getSurfaceId());
            Log.d(str2, stringBuilder2.toString());
        } catch (RuntimeException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(e);
            Log.e(str2, stringBuilder.toString());
        } catch (Exception e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(e2);
            Log.e(str2, stringBuilder.toString());
        }
    }

    public Long getCaptureSurfaceId() {
        return this.captureSurfaceInfo_.getSurfaceId();
    }

    public Surface getCaptureSurface() {
        return this.captureSurfaceInfo_.getSurface();
    }

    public Surface getPreviewSurface() {
        SurfaceHolder surfaceHolder = this.previewSurfaceHolder_;
        if (surfaceHolder != null) {
            return surfaceHolder.getSurface();
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    public boolean releaseCaptureSurface() {
        return SecCamServiceClient.getInstance().releaseCaptureSurface(this);
    }

    public int getCameraId() {
        return this.cameraId_;
    }

    public int getImageFormat() {
        return this.imageFormat_;
    }

    public static String imageFormatToString(int imageFormat) {
        if (imageFormat == 17) {
            return "YUV420SP";
        }
        if (imageFormat == 32) {
            return "RAW(SENSOR)";
        }
        if (imageFormat == IMAGE_FORMAT_YUV420SP_UBWC) {
            return "YUV420SP_UBWC";
        }
        switch (imageFormat) {
            case 34:
                return "PRIVATE";
            case 35:
                return "YUV420";
            case 36:
                return "RAW(PRIVATE)";
            case 37:
                return "RAW10";
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UNKNOWN(");
                stringBuilder.append(imageFormat);
                stringBuilder.append(")");
                return stringBuilder.toString();
        }
    }

    public static String rotationToString(int rotation) {
        switch (rotation) {
            case 1:
                return "HFLIP";
            case 2:
                return "VFLIP";
            case 3:
                return Parameters.VIDEO_ROTATION_180;
            case 4:
                return "90";
            case 5:
                return "90+HFLIP";
            case 6:
                return "90+VFLIP";
            case 7:
                return Parameters.VIDEO_ROTATION_270;
            default:
                return "0";
        }
    }

    public boolean release() {
        if (this.captureSurfaceInfo_ == null) {
            return true;
        }
        boolean result = SecCamServiceClient.getInstance().releaseCaptureSurface(this);
        this.captureSurfaceInfo_ = null;
        return result;
    }

    public boolean assignPreviewSurface(SurfaceHolder previewSurfaceHolder, int width, int height, int format, int rotation, int numOfBuffers) {
        boolean result = SecCamServiceClient.getInstance().setSecurePreviewSurface(previewSurfaceHolder.getSurface(), this, width, height, format, rotation, numOfBuffers);
        if (result) {
            this.previewSurfaceHolder_ = previewSurfaceHolder;
            this.previewSerfaceCallback_ = new Callback() {
                public void surfaceCreated(SurfaceHolder holder) {
                    Log.d(SecureSurface.SECCAM_API_LOG_TAG, "SecureSurface::assignPreviewSurface::surfaceCreated");
                }

                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    Log.d(SecureSurface.SECCAM_API_LOG_TAG, "SecureSurface::assignPreviewSurface::surfaceChanged");
                }

                public void surfaceDestroyed(SurfaceHolder holder) {
                    String str = SecureSurface.SECCAM_API_LOG_TAG;
                    Log.d(str, "SecureSurface::assignPreviewSurface::surfaceDestroyed - Enter");
                    SecCamServiceClient.getInstance().releasePreviewSurface(SecureSurface.this.previewSurfaceHolder_.getSurface(), SecureSurface.this.captureSurfaceInfo_);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("SecureSurface::assignPreviewSurface::surfaceDestroyed ");
                        stringBuilder.append(ex);
                        Log.e(str, stringBuilder.toString());
                    }
                    SecureSurface.this.previewSurfaceHolder_.removeCallback(SecureSurface.this.previewSerfaceCallback_);
                    SecureSurface.this.previewSurfaceHolder_ = null;
                    Log.d(str, "SecureSurface::assignPreviewSurface::surfaceDestroyed - Done");
                }
            };
            this.previewSurfaceHolder_.addCallback(this.previewSerfaceCallback_);
        }
        return result;
    }

    public boolean enableFrameCallback(FrameCallback frameCallback, int returnParamsSize) {
        return SecCamServiceClient.getInstance().enableFrameCallback(this, frameCallback, returnParamsSize);
    }
}
